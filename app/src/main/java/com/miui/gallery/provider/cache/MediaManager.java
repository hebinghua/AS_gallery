package com.miui.gallery.provider.cache;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Process;
import android.text.TextUtils;
import androidx.sqlite.db.CursorSpec;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import androidx.tracing.Trace;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.provider.MediaSortDateHelper;
import com.miui.gallery.provider.TimelineHeadersGroup;
import com.miui.gallery.provider.cache.MediaCacheItem;
import com.miui.gallery.provider.cache.RecentMediaHeadersHelper;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.DebugUtil;
import com.miui.gallery.util.GalleryDateUtils;
import com.miui.gallery.util.PackageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.d;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.function.BiConsumer;
import java.util.function.ToLongFunction;
import kotlin.Pair;

/* loaded from: classes2.dex */
public class MediaManager extends CacheManager<MediaCacheItem> {
    public final HashMap<Long, MediaCacheItem> mByPassIds;
    public final FavoritesDelegate mFavoritesDelegate;
    public String mFirstPartIds;
    public final CountDownLatch mFullLoadDoneSignal;
    public long mFullPartCosts;
    public final ArrayList<InitializeListener> mInitializeListeners;
    public volatile boolean mInitialized;
    public volatile boolean mIsFullLoadDone;
    public volatile boolean mIsLoadStarted;
    public volatile boolean mIsMinimumLoadDone;
    public final Object mMinimumLoadLock;
    public long mMinimumPartCosts;

    /* loaded from: classes2.dex */
    public interface InitializeListener {
        void onProgressUpdate(InitializeStatus initializeStatus);
    }

    /* loaded from: classes2.dex */
    public enum InitializeStatus {
        START,
        END,
        UPDATE
    }

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final MediaManager INSTANCE = new MediaManager();
    }

    public static /* synthetic */ void $r8$lambda$0_RyOGbFXccgXbtxQlgrDKh3ALE(String[] strArr, String str, Integer num) {
        lambda$onCreateCursor$0(strArr, str, num);
    }

    public final int transformOrderByColumnIndex(int i) {
        if (i != 18) {
            if (i == 30) {
                return 31;
            }
            if (i == 36) {
                return 37;
            }
            return i;
        }
        return 19;
    }

    public MediaManager() {
        this.mIsLoadStarted = false;
        this.mIsFullLoadDone = false;
        this.mMinimumLoadLock = new Object();
        this.mIsMinimumLoadDone = false;
        this.mInitialized = false;
        this.mByPassIds = new HashMap<>();
        this.TAG = "MediaManager";
        this.mFullLoadDoneSignal = new CountDownLatch(1);
        this.mInitializeListeners = new ArrayList<>();
        FavoritesDelegate favoritesDelegate = new FavoritesDelegate();
        this.mFavoritesDelegate = favoritesDelegate;
        this.mCache = new LinkedList();
        this.mGenerator = new MediaCacheItem.Generator(favoritesDelegate);
        this.mQueryFactory = new MediaCacheItem.QueryFactory();
    }

    public static MediaManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public synchronized void load(GalleryDBHelper galleryDBHelper) {
        if (!this.mIsLoadStarted && !this.mIsFullLoadDone) {
            this.mIsLoadStarted = true;
            new Thread(new InitializeTask(galleryDBHelper), "MediaManagerInit").start();
        }
    }

    public int insert(SupportSQLiteDatabase supportSQLiteDatabase, String str, String[] strArr) {
        int i = 0;
        Cursor query = supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("cloud").columns(this.mGenerator.getProjection()).selection(String.format("%s AND (%s)", "(localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))", str), strArr).create());
        if (query != null) {
            this.mWriteLock.lock();
            while (query.moveToNext()) {
                try {
                    this.mCache.add((MediaCacheItem) this.mGenerator.mo1226from(query));
                    i++;
                } finally {
                    query.close();
                    this.mWriteLock.unlock();
                }
            }
        }
        return i;
    }

    @Override // com.miui.gallery.provider.cache.CacheManager
    public long insert(long j, ContentValues contentValues) {
        if (!isItemDeleted(contentValues)) {
            String asString = contentValues.getAsString("source_pkg");
            if (!TextUtils.isEmpty(asString)) {
                contentValues.put("location", PackageUtils.getAppNameByPackage(asString));
            }
            return super.insert(j, contentValues);
        }
        return 0L;
    }

    public long insertByPass(long j, ContentValues contentValues) {
        this.mWriteLock.lock();
        try {
            insert(j, contentValues);
            this.mByPassIds.put(Long.valueOf(j), null);
            this.mWriteLock.unlock();
            return 0L;
        } catch (Throwable th) {
            this.mWriteLock.unlock();
            throw th;
        }
    }

    @Override // com.miui.gallery.provider.cache.CacheManager
    public ContentValues filterLogInfo(ContentValues contentValues) {
        if (contentValues != null) {
            ContentValues contentValues2 = new ContentValues(contentValues);
            contentValues2.remove("location");
            contentValues2.remove("extraGPS");
            contentValues2.remove("address");
            contentValues2.remove("exifGPSLatitude");
            contentValues2.remove("exifGPSLongitude");
            contentValues2.remove("exifGPSAltitude");
            contentValues2.remove("creatorId");
            contentValues2.remove("fileName");
            return contentValues2;
        }
        return null;
    }

    @Override // com.miui.gallery.provider.cache.CacheManager
    public int update(String str, String[] strArr, ContentValues contentValues) {
        if (isItemDeleted(contentValues)) {
            delete(str, strArr);
        }
        return super.update(str, strArr, contentValues);
    }

    public boolean isItemDeleted(ContentValues contentValues) {
        Integer asInteger = contentValues.getAsInteger("localFlag");
        if (asInteger != null && (asInteger.intValue() == 11 || asInteger.intValue() == 2)) {
            DefaultLogger.d(this.TAG, "find a deleted local flag");
            return true;
        } else if (!contentValues.containsKey("serverStatus")) {
            return false;
        } else {
            String asString = contentValues.getAsString("serverStatus");
            if (!"deleted".equals(asString) && !"purged".equals(asString) && !"toBePurged".equals(asString) && !"cleanLocal".equals(asString)) {
                return false;
            }
            DefaultLogger.d(this.TAG, "find a deleted server type");
            return true;
        }
    }

    public static String groupByFlags2String(int i) {
        String str = "";
        if ((i & 4) == 4) {
            str = str + "YEAR|";
        }
        if ((i & 2) == 2) {
            str = str + "MONTH|";
        }
        if ((i & 1) == 1) {
            return str + "DAY";
        }
        return str;
    }

    public int parseOrderByColumnIndex(String str) {
        int indexOf = str.indexOf(32);
        if (indexOf <= 0) {
            indexOf = str.length();
        }
        String substring = str.substring(0, indexOf);
        int index = this.mQueryFactory.getMapper().getIndex(substring);
        if (index < 0) {
            throw new IllegalArgumentException(substring + " not found");
        }
        return transformOrderByColumnIndex(index);
    }

    @Override // com.miui.gallery.provider.cache.CacheManager
    public <R> List<R> query(String str, String[] strArr, String str2, String str3, String str4, Bundle bundle, IMediaProcessor<? super MediaCacheItem, R> iMediaProcessor) {
        List<MediaCacheItem> doQuery = doQuery(str, strArr, str2, str3, str4);
        if (bundle != null && bundle.getBoolean("extra_generate_header")) {
            bundle.putInt("extra_group_sort_column", parseOrderByColumnIndex(str2));
        }
        return iMediaProcessor.processCache(doQuery, bundle);
    }

    @Override // com.miui.gallery.provider.cache.CacheManager
    public Cursor onCreateCursor(String[] strArr, String str, String[] strArr2, String str2, String str3, String str4, Bundle bundle, List<MediaCacheItem> list) {
        RawCursor rawCursor;
        if (strArr == null) {
            HashMap<String, Integer> hashMap = MediaCacheItem.QueryFactory.PROJECTION;
            final String[] strArr3 = new String[hashMap.size()];
            hashMap.forEach(new BiConsumer() { // from class: com.miui.gallery.provider.cache.MediaManager$$ExternalSyntheticLambda2
                @Override // java.util.function.BiConsumer
                public final void accept(Object obj, Object obj2) {
                    MediaManager.$r8$lambda$0_RyOGbFXccgXbtxQlgrDKh3ALE(strArr3, (String) obj, (Integer) obj2);
                }
            });
            strArr = strArr3;
        }
        if (bundle != null && bundle.getBoolean("extra_generate_header")) {
            DefaultLogger.d(this.TAG, "caller need a header data, start generate for %d items", Integer.valueOf(list.size()));
            long currentTimeMillis = System.currentTimeMillis();
            boolean z = bundle.getBoolean("extra_timeline_only_show_valid_location", true);
            int i = bundle.getInt("extra_media_group_by");
            final int parseOrderByColumnIndex = parseOrderByColumnIndex(str2);
            if ((i & 7) != 0) {
                ArrayList arrayList = new ArrayList(3);
                if ((i & 1) == 1) {
                    arrayList.add(new Pair(1, Comparator.comparingLong(new ToLongFunction() { // from class: com.miui.gallery.provider.cache.MediaManager$$ExternalSyntheticLambda3
                        @Override // java.util.function.ToLongFunction
                        public final long applyAsLong(Object obj) {
                            long j;
                            j = ((MediaCacheItem) obj).getLong(parseOrderByColumnIndex);
                            return j;
                        }
                    })));
                }
                if ((i & 2) == 2) {
                    arrayList.add(new Pair(2, new Comparator() { // from class: com.miui.gallery.provider.cache.MediaManager$$ExternalSyntheticLambda0
                        @Override // java.util.Comparator
                        public final int compare(Object obj, Object obj2) {
                            int lambda$onCreateCursor$2;
                            lambda$onCreateCursor$2 = MediaManager.lambda$onCreateCursor$2(parseOrderByColumnIndex, (MediaCacheItem) obj, (MediaCacheItem) obj2);
                            return lambda$onCreateCursor$2;
                        }
                    }));
                }
                if ((i & 4) == 4) {
                    arrayList.add(new Pair(4, new Comparator() { // from class: com.miui.gallery.provider.cache.MediaManager$$ExternalSyntheticLambda1
                        @Override // java.util.Comparator
                        public final int compare(Object obj, Object obj2) {
                            int lambda$onCreateCursor$3;
                            lambda$onCreateCursor$3 = MediaManager.lambda$onCreateCursor$3(parseOrderByColumnIndex, (MediaCacheItem) obj, (MediaCacheItem) obj2);
                            return lambda$onCreateCursor$3;
                        }
                    }));
                }
                rawCursor = new RawCursor(list, strArr, this.mQueryFactory.getMapper());
                TimelineHeadersGroup.bindGroups(MediaGroupingHelper.generateGroups(list, arrayList, z), rawCursor);
            } else if (i == 8) {
                ArrayList arrayList2 = new ArrayList(list.size());
                List<RecentMediaHeadersHelper.Header> resortAndGenerateHeaders = RecentMediaHeadersHelper.resortAndGenerateHeaders(list, arrayList2);
                RawCursor rawCursor2 = new RawCursor(arrayList2, strArr, this.mQueryFactory.getMapper());
                RecentMediaHeadersHelper.bindGroup2Cursor(resortAndGenerateHeaders, rawCursor2);
                rawCursor = rawCursor2;
            } else {
                throw new IllegalArgumentException(String.format("unsupported group type %s", Integer.valueOf(i)));
            }
            Bundle extras = rawCursor.getExtras();
            if (extras == null || extras == Bundle.EMPTY) {
                extras = new Bundle();
                rawCursor.setExtras(extras);
            }
            extras.putInt("extra_group_sort_column", parseOrderByColumnIndex);
            DefaultLogger.d(this.TAG, "generate header for [%s] cost %d", groupByFlags2String(i), Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            return rawCursor;
        }
        return new RawCursor(list, strArr, this.mQueryFactory.getMapper());
    }

    public static /* synthetic */ void lambda$onCreateCursor$0(String[] strArr, String str, Integer num) {
        strArr[num.intValue()] = str;
    }

    public static /* synthetic */ int lambda$onCreateCursor$2(int i, MediaCacheItem mediaCacheItem, MediaCacheItem mediaCacheItem2) {
        if (mediaCacheItem.columnEquals(mediaCacheItem2, i) || GalleryDateUtils.isSameMonth(mediaCacheItem.getLong(i), mediaCacheItem2.getLong(i))) {
            return 0;
        }
        return Long.compare(mediaCacheItem.getLong(i), mediaCacheItem2.getLong(i));
    }

    public static /* synthetic */ int lambda$onCreateCursor$3(int i, MediaCacheItem mediaCacheItem, MediaCacheItem mediaCacheItem2) {
        if (mediaCacheItem.columnEquals(mediaCacheItem2, i) || GalleryDateUtils.isSameYear(mediaCacheItem.getLong(i), mediaCacheItem2.getLong(i))) {
            return 0;
        }
        return Long.compare(mediaCacheItem.getLong(i), mediaCacheItem2.getLong(i));
    }

    public void insertToFavorites(Long l) {
        if (this.mFavoritesDelegate.insertToFavorites(l)) {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put("virtual_field_is_favorites", Boolean.TRUE);
            update("_id=" + l, null, contentValues);
        }
    }

    public void removeFromFavorites(Long l) {
        if (this.mFavoritesDelegate.removeFromFavorites(l)) {
            ContentValues contentValues = new ContentValues(1);
            contentValues.put("virtual_field_is_favorites", Boolean.FALSE);
            update("_id=" + l, null, contentValues);
        }
    }

    public boolean isFavorite(long j) {
        return this.mFavoritesDelegate.isFavorite(Long.valueOf(j));
    }

    /* loaded from: classes2.dex */
    public class InitializeTask implements Runnable {
        public final GalleryDBHelper mDBHelper;

        public InitializeTask(GalleryDBHelper galleryDBHelper) {
            MediaManager.this = r1;
            this.mDBHelper = galleryDBHelper;
        }

        @Override // java.lang.Runnable
        public void run() {
            DefaultLogger.d(MediaManager.this.TAG, "acquire initialize lock");
            try {
                try {
                    notifyInitializeStatus(InitializeStatus.START);
                    DefaultLogger.d(MediaManager.this.TAG, "start initialize");
                    if (BaseBuildUtil.isLowRamDevice()) {
                        Thread.sleep(800L);
                    }
                    SupportSQLiteDatabase readableDatabase = this.mDBHelper.getReadableDatabase();
                    lockedLoadMinimumPart(readableDatabase);
                    Process.setThreadPriority(10);
                    loadFullPart(readableDatabase);
                    MediaManager.this.mInitialized = true;
                    MediaManager.this.mIsFullLoadDone = true;
                    MediaManager.this.mFullLoadDoneSignal.countDown();
                    synchronized (MediaManager.this.mInitializeListeners) {
                        notifyInitializeStatus(InitializeStatus.END);
                        MediaManager.this.mInitializeListeners.clear();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    MediaManager.this.mIsFullLoadDone = true;
                    MediaManager.this.mFullLoadDoneSignal.countDown();
                    synchronized (MediaManager.this.mInitializeListeners) {
                        notifyInitializeStatus(InitializeStatus.END);
                        MediaManager.this.mInitializeListeners.clear();
                    }
                }
            } catch (Throwable th) {
                MediaManager.this.mIsFullLoadDone = true;
                MediaManager.this.mFullLoadDoneSignal.countDown();
                synchronized (MediaManager.this.mInitializeListeners) {
                    notifyInitializeStatus(InitializeStatus.END);
                    MediaManager.this.mInitializeListeners.clear();
                    throw th;
                }
            }
        }

        public final void lockedLoadMinimumPart(SupportSQLiteDatabase supportSQLiteDatabase) {
            synchronized (MediaManager.this.mMinimumLoadLock) {
                AlbumCacheManager.getInstance().load(supportSQLiteDatabase);
                MediaManager.this.mFavoritesDelegate.load(supportSQLiteDatabase);
                MediaManager.this.mFirstPartIds = GalleryPreferences.HomePage.getHomePageImageIds();
                loadMinimumPart(supportSQLiteDatabase);
                MediaManager.this.mIsMinimumLoadDone = true;
                MediaManager.this.mMinimumLoadLock.notifyAll();
            }
        }

        public final void loadMinimumPart(SupportSQLiteDatabase supportSQLiteDatabase) {
            try {
                Trace.beginSection("MediaManager#loadMinimumPart");
                DefaultLogger.fd(MediaManager.this.TAG, "read first part");
                long currentTimeMillis = System.currentTimeMillis();
                try {
                    SupportSQLiteQueryBuilder selection = SupportSQLiteQueryBuilder.builder("cloud").columns(MediaManager.this.mGenerator.getProjection()).selection(TextUtils.isEmpty(MediaManager.this.mFirstPartIds) ? "(localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))" : String.format("(localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) AND _id IN (%s)", MediaManager.this.mFirstPartIds), null);
                    Cursor query = supportSQLiteDatabase.query(selection.orderBy(getLoadSortBy() + " DESC").limit(String.valueOf(128)).create());
                    if (query == null) {
                        DefaultLogger.e(MediaManager.this.TAG, "fill provider failed with a null cursor");
                    }
                    LinkedList linkedList = new LinkedList();
                    while (query != null && query.moveToNext()) {
                        MediaCacheItem mediaCacheItem = (MediaCacheItem) MediaManager.this.mGenerator.mo1226from(query);
                        if (!MediaManager.this.mByPassIds.containsKey(Long.valueOf(mediaCacheItem.getId()))) {
                            linkedList.add(mediaCacheItem);
                        }
                    }
                    publishResult(linkedList);
                    MediaManager.this.mMinimumPartCosts = System.currentTimeMillis() - currentTimeMillis;
                    DefaultLogger.fd(MediaManager.this.TAG, "load %d item for the first time, costs %dms", Integer.valueOf(linkedList.size()), Long.valueOf(MediaManager.this.mMinimumPartCosts));
                    if (query != null) {
                        query.close();
                    }
                } catch (SQLiteException e) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("version", String.valueOf(supportSQLiteDatabase.getVersion()));
                    hashMap.put("error", e.getMessage() + " : " + e.getCause());
                    SamplingStatHelper.recordCountEvent("db_helper", "db_mediamanager_load", hashMap);
                    DebugUtil.exportDB(false);
                    GalleryDBHelper.getInstance().deleteDatabase();
                    throw e;
                }
            } finally {
                Trace.endSection();
            }
        }

        public final void loadFullPart(SupportSQLiteDatabase supportSQLiteDatabase) {
            String format;
            String str;
            int i;
            try {
                Trace.beginSection("MediaManager#loadFullPart");
                DefaultLogger.fd(MediaManager.this.TAG, "start load second part");
                long currentTimeMillis = System.currentTimeMillis();
                ArrayList arrayList = new ArrayList();
                if (TextUtils.isEmpty(MediaManager.this.mFirstPartIds)) {
                    format = "(localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))";
                    str = getLoadSortBy() + " DESC LIMIT -1 OFFSET 128";
                } else {
                    format = String.format("(localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) AND _id NOT IN (%s)", MediaManager.this.mFirstPartIds);
                    str = getLoadSortBy() + " DESC ";
                }
                Cursor query = supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("cloud").columns(MediaManager.this.mGenerator.getProjection()).selection(format, null).orderBy(str).create(), CursorSpec.FORWARD_ONLY);
                if (query == null) {
                    return;
                }
                arrayList.ensureCapacity(8192);
                loop0: while (true) {
                    i = 0;
                    while (query.moveToNext()) {
                        MediaCacheItem mediaCacheItem = (MediaCacheItem) MediaManager.this.mGenerator.mo1226from(query);
                        if (!MediaManager.this.mByPassIds.containsKey(Long.valueOf(mediaCacheItem.getId()))) {
                            arrayList.add(mediaCacheItem);
                            i++;
                            if (i == 8192) {
                                break;
                            }
                        }
                    }
                    publishResult(arrayList);
                    arrayList.clear();
                }
                if (i > 0) {
                    publishResult(arrayList);
                    arrayList.clear();
                    MediaManager.this.mByPassIds.clear();
                }
                query.close();
                MediaManager.this.mFullPartCosts = System.currentTimeMillis() - currentTimeMillis;
                MediaManager mediaManager = MediaManager.this;
                DefaultLogger.fd(mediaManager.TAG, "load second part cost: %d, count = %d", Long.valueOf(mediaManager.mFullPartCosts), Integer.valueOf(MediaManager.this.mCache.size()));
                ShareMediaManager.getInstance().load(supportSQLiteDatabase);
            } finally {
                Trace.endSection();
            }
        }

        public final void publishResult(List<MediaCacheItem> list) {
            MediaManager.this.mWriteLock.lock();
            try {
                MediaManager.this.mCache.addAll(list);
                MediaManager.this.mWriteLock.unlock();
                notifyInitializeStatus(InitializeStatus.UPDATE);
            } catch (Throwable th) {
                MediaManager.this.mWriteLock.unlock();
                throw th;
            }
        }

        public final void notifyInitializeStatus(InitializeStatus initializeStatus) {
            synchronized (MediaManager.this.mInitializeListeners) {
                if (MediaManager.this.mInitializeListeners.size() > 0) {
                    Iterator it = MediaManager.this.mInitializeListeners.iterator();
                    while (it.hasNext()) {
                        InitializeListener initializeListener = (InitializeListener) it.next();
                        if (initializeListener != null) {
                            initializeListener.onProgressUpdate(initializeStatus);
                        }
                    }
                }
            }
        }

        public final String getLoadSortBy() {
            List<Long> albumIdsBySortDate = MediaSortDateHelper.getSortDateProvider().getAlbumIdsBySortDate(MediaSortDateHelper.SortDate.CREATE_TIME);
            return !BaseMiscUtil.isValid(albumIdsBySortDate) ? "dateModified" : String.format(Locale.US, "(case when %s in (%s) then %s else %s end)", "localGroupId", TextUtils.join(",", albumIdsBySortDate), "mixedDateTime", "dateModified");
        }
    }

    public boolean isInitializedLock() {
        if (this.mIsFullLoadDone) {
            return this.mInitialized;
        }
        DefaultLogger.d(this.TAG, "initializing, waiting lock from:\n%s", TextUtils.join("\n\t", Thread.currentThread().getStackTrace()));
        long currentTimeMillis = System.currentTimeMillis();
        try {
            this.mFullLoadDoneSignal.await();
            DefaultLogger.d(this.TAG, "wait full load done costs %d ms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        } catch (InterruptedException e) {
            DefaultLogger.e(this.TAG, e);
        }
        DefaultLogger.d(this.TAG, "initialization finished: %b", Boolean.valueOf(this.mInitialized));
        return this.mInitialized;
    }

    @Override // com.miui.gallery.provider.cache.CacheManager
    public boolean isInitialized() {
        return this.mInitialized;
    }

    public void ensureMinimumPartDone() {
        if (this.mIsMinimumLoadDone) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        synchronized (this.mMinimumLoadLock) {
            while (!this.mIsMinimumLoadDone) {
                try {
                    this.mMinimumLoadLock.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        DefaultLogger.d(this.TAG, "wait for minimum part loading cost : %dms ", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
    }

    public void addInitializeListener(InitializeListener initializeListener) {
        if (this.mIsFullLoadDone) {
            DefaultLogger.d(this.TAG, "no need to add listener after full load done!");
            return;
        }
        synchronized (this.mInitializeListeners) {
            this.mInitializeListeners.add(initializeListener);
        }
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("MediaManager:");
        printWriter.print("  initialized=");
        printWriter.println(this.mInitialized);
        if (this.mInitialized) {
            printWriter.print("    media count: ");
            printWriter.print(this.mCache.size());
            printWriter.print(", load minimum part costs ");
            printWriter.print(this.mMinimumPartCosts);
            printWriter.print("ms, load full part costs ");
            printWriter.print(this.mFullPartCosts);
            printWriter.println(d.H);
        }
    }
}
