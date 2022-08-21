package com.miui.gallery.provider.cache;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import androidx.collection.LongSparseArray;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import androidx.tracing.Trace;
import com.google.common.collect.Sets;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.model.datalayer.repository.album.common.QueryParam;
import com.miui.gallery.model.datalayer.repository.album.common.datasource.CommonAlbumFileCacheDataSourceImpl;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.CloudUtils;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.provider.MediaSortDateHelper;
import com.miui.gallery.provider.album.AlbumManager;
import com.miui.gallery.provider.album.config.QueryFlagsBuilder;
import com.miui.gallery.provider.cache.AlbumCacheItem;
import com.miui.gallery.provider.cache.AlbumCacheManager;
import com.miui.gallery.provider.cache.CacheItem;
import com.miui.gallery.provider.cache.CacheManager;
import com.miui.gallery.provider.cache.Filter;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.constants.MIUIStorageConstants;
import com.miui.gallery.util.DebugUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.thread.ThreadManager;
import com.xiaomi.stat.a.j;
import com.xiaomi.stat.d;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import miuix.core.util.Pools;

/* loaded from: classes2.dex */
public class AlbumCacheManager extends CacheManager<AlbumCacheItem> {
    public final Object INITIALIZATION_ATTRIBUTES_LOCK;
    public final Object INITIALIZATION_LOCK;
    public final Object INITIALIZATION_SORT_ARRAY_LOCK;
    public final Executor mAlbumUpdateExecutor;
    public final ConcurrentHashMap<Long, Long> mAttributes;
    public final CountDownLatch mFullLoadDoneSignal;
    public final ArrayList<InitializeListener> mInitializeListeners;
    public volatile boolean mInitialized;
    public volatile boolean mInitializedAttributes;
    public volatile boolean mIsLoadDone;
    public volatile boolean mIsLoadStarted;
    public long mLoadAlbumsCosts;
    public long mLoadAttributesCosts;
    public final Scheduler mMediaCollectorScheduler;
    public volatile Subject<MediaOperationTaskContainer> mMediaTaskCollector;
    public long mScreenRecordersAlbumId;
    public long mScreenshotsAlbumId;
    public volatile LongSparseArray<MediaSortDateHelper.SortDate> mSortDate;
    public final AlbumCacheItem.UpdateManager mUpdateManager;
    public SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener;

    /* loaded from: classes2.dex */
    public interface InitializeListener {
        void onProgressUpdate();
    }

    /* loaded from: classes2.dex */
    public enum TaskType {
        UPDATE_MEDIA,
        INSERT_MEDIA,
        DELETE_MEDIA,
        INSERT_TO_SHARE_MEDIA,
        UPDATE_ALBUM,
        INSERT_ALBUM,
        DELETE_ALBUM
    }

    /* renamed from: $r8$lambda$AVsOC--Y_Utg9VqQP1BmZ3uL4Z0 */
    public static /* synthetic */ void m1202$r8$lambda$AVsOCY_Utg9VqQP1BmZ3uL4Z0(AlbumCacheManager albumCacheManager, LinkedList linkedList) {
        albumCacheManager.lambda$ensureMediaTaskCollector$2(linkedList);
    }

    public static /* synthetic */ void $r8$lambda$CKQcLK3cBphAgQc3WRs9hZcyaBk(AlbumCacheManager albumCacheManager, Throwable th) {
        albumCacheManager.lambda$ensureMediaTaskCollector$3(th);
    }

    public static /* synthetic */ void $r8$lambda$qJdGEhTzjVcrEucpgyRml9VFDdQ(AlbumCacheManager albumCacheManager, List list) {
        albumCacheManager.lambda$dispatchTask$0(list);
    }

    public AlbumCacheManager() {
        this.mIsLoadStarted = false;
        this.mIsLoadDone = false;
        this.mInitializedAttributes = false;
        this.INITIALIZATION_LOCK = new Object();
        this.INITIALIZATION_ATTRIBUTES_LOCK = new Object();
        this.INITIALIZATION_SORT_ARRAY_LOCK = new Object();
        this.mAttributes = new ConcurrentHashMap<>();
        this.mSortDate = new LongSparseArray<>(0);
        this.onSharedPreferenceChangeListener = new AnonymousClass4();
        this.TAG = "AlbumCacheManager";
        this.mFullLoadDoneSignal = new CountDownLatch(1);
        this.mInitializeListeners = new ArrayList<>();
        this.mCache = new LinkedList();
        this.mGenerator = new AlbumCacheItem.Generator();
        this.mQueryFactory = new AlbumCacheItem.QueryFactory();
        this.mUpdateManager = new AlbumCacheItem.UpdateManager();
        Executor executor = ThreadManager.getExecutor(47);
        this.mAlbumUpdateExecutor = executor;
        this.mMediaCollectorScheduler = Schedulers.from(executor);
        initTaskQueueDispatcher();
        initLocalModelChangeListener();
    }

    public static AlbumCacheManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final AlbumCacheManager INSTANCE = new AlbumCacheManager();
    }

    public synchronized void load(SupportSQLiteDatabase supportSQLiteDatabase) {
        if (!this.mIsLoadStarted && !this.mIsLoadDone) {
            this.mIsLoadStarted = true;
            if (Looper.getMainLooper() == Looper.myLooper()) {
                new Thread(new InitializeTask(supportSQLiteDatabase), "AlbumManagerInit").start();
            } else {
                new InitializeTask(supportSQLiteDatabase).run();
            }
            MediaManager.getInstance().addInitializeListener(new MediaManager.InitializeListener() { // from class: com.miui.gallery.provider.cache.AlbumCacheManager.1
                {
                    AlbumCacheManager.this = this;
                }

                @Override // com.miui.gallery.provider.cache.MediaManager.InitializeListener
                public void onProgressUpdate(MediaManager.InitializeStatus initializeStatus) {
                    if (initializeStatus == MediaManager.InitializeStatus.END) {
                        AlbumCacheManager.this.refreshAllPhoto();
                    }
                }
            });
        }
    }

    public final void refreshAllPhoto() {
        if (isInitialized()) {
            update(2147483644L, AlbumCacheItem.UPDATE_COVER_AND_COUNT);
        } else {
            ThreadManager.execute(47, new Runnable() { // from class: com.miui.gallery.provider.cache.AlbumCacheManager.2
                {
                    AlbumCacheManager.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    if (AlbumCacheManager.this.isInitializedLock()) {
                        AlbumCacheManager.this.update(2147483644L, AlbumCacheItem.UPDATE_COVER_AND_COUNT);
                    }
                }
            });
        }
    }

    @Override // com.miui.gallery.provider.cache.CacheManager
    public Cursor query(String[] strArr, String str, String[] strArr2, String str2, String str3, String str4, Bundle bundle) {
        String str5 = str;
        long currentTimeMillis = System.currentTimeMillis();
        DefaultLogger.d(this.TAG, "query Album start:[%s]", Long.valueOf(currentTimeMillis));
        if (bundle != null) {
            long j = bundle.getLong("query_flags");
            if (j != 0) {
                StringBuilder acquire = Pools.getStringBuilderPool().acquire();
                if (str5 != null) {
                    acquire.append(str);
                    acquire.append(" AND ");
                }
                acquire.append("query_flags");
                acquire.append("=");
                acquire.append(j);
                str5 = acquire.toString();
                Pools.getStringBuilderPool().release(acquire);
            }
        }
        Cursor query = super.query(strArr, str5, strArr2, str2, str3, str4, bundle);
        DefaultLogger.d(this.TAG, "query Album end:[%s]", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        return query;
    }

    @Override // com.miui.gallery.provider.cache.CacheManager
    public int delete(String str, String[] strArr) {
        this.mWriteLock.lock();
        try {
            try {
                CacheManager.OnOperationListener<AlbumCacheItem> onOperationListener = new CacheManager.OnOperationListener<AlbumCacheItem>() { // from class: com.miui.gallery.provider.cache.AlbumCacheManager.3
                    {
                        AlbumCacheManager.this = this;
                    }

                    @Override // com.miui.gallery.provider.cache.CacheManager.OnOperationListener
                    public void onRemove(List<AlbumCacheItem> list) {
                        for (AlbumCacheItem albumCacheItem : list) {
                            AlbumCacheManager.this.deleteAttributes(albumCacheItem.getId());
                        }
                    }
                };
                addOperationListener(onOperationListener);
                int delete = super.delete(str, strArr);
                removeOperationListener(onOperationListener);
                return delete;
            } catch (Exception e) {
                DefaultLogger.e(this.TAG, "delete error:%s", e);
                this.mWriteLock.unlock();
                return 0;
            }
        } finally {
            this.mWriteLock.unlock();
        }
    }

    @Override // com.miui.gallery.provider.cache.CacheManager
    public long insert(long j, ContentValues contentValues) {
        long j2 = 0;
        if (!isItemDeleted(contentValues)) {
            if (!contentValues.containsKey(j.c)) {
                contentValues.put(j.c, Long.valueOf(j));
            }
            long insert = super.insert(j, contentValues);
            Long asLong = contentValues.getAsLong("attributes");
            if (asLong != null) {
                j2 = asLong.longValue();
            }
            updateOrInsertAttributes(j, j2);
            return insert;
        }
        return 0L;
    }

    @Override // com.miui.gallery.provider.cache.CacheManager
    public int update(String str, String[] strArr, ContentValues contentValues) {
        if (isItemDeleted(contentValues)) {
            delete(str, strArr);
        }
        DefaultLogger.d(this.TAG, "updating %s, args: %s with ContentValues[%s]", str, Arrays.toString(strArr), filterLogInfo(contentValues));
        this.mWriteLock.lock();
        try {
            int updateOrInsert = updateOrInsert(str, strArr, contentValues);
            DefaultLogger.d(this.TAG, "update finished");
            return updateOrInsert;
        } finally {
            this.mWriteLock.unlock();
        }
    }

    public final int updateOrInsert(String str, String[] strArr, ContentValues contentValues) {
        List<AlbumCacheItem> filter = filter(Filter.from(str, strArr, this.mGenerator));
        if (filter.size() > 0) {
            int i = 0;
            for (AlbumCacheItem albumCacheItem : filter) {
                this.mGenerator.update(albumCacheItem, contentValues);
                DefaultLogger.fd(this.TAG, "updated %s", albumCacheItem);
                i++;
            }
            Iterator it = this.mOperationListeners.iterator();
            while (it.hasNext()) {
                ((CacheManager.OnOperationListener) it.next()).onUpdate(filter, contentValues);
            }
            return i;
        }
        List<AlbumCacheItem> insertAlbum = insertAlbum(str, strArr, contentValues);
        Iterator it2 = this.mOperationListeners.iterator();
        while (it2.hasNext()) {
            CacheManager.OnOperationListener onOperationListener = (CacheManager.OnOperationListener) it2.next();
            for (AlbumCacheItem albumCacheItem2 : insertAlbum) {
                onOperationListener.onInsert(albumCacheItem2);
            }
        }
        return insertAlbum.size();
    }

    public final List<AlbumCacheItem> insertAlbum(String str, String[] strArr, ContentValues contentValues) {
        LinkedList linkedList = new LinkedList();
        Cursor query = AlbumManager.query(GalleryDBHelper.getInstance().getReadableDatabase(), AlbumManager.QUERY_ALBUM_PROJECTION, str, strArr, new QueryFlagsBuilder().build());
        try {
            if (query == null) {
                List<AlbumCacheItem> emptyList = Collections.emptyList();
                if (query != null) {
                    query.close();
                }
                return emptyList;
            }
            MediaSortDateHelper.SortDateProvider sortDateProvider = MediaSortDateHelper.getSortDateProvider();
            query.moveToFirst();
            while (!query.isAfterLast()) {
                AlbumCacheItem albumCacheItem = (AlbumCacheItem) this.mGenerator.mo1226from(query);
                this.mGenerator.update(albumCacheItem, contentValues);
                long id = albumCacheItem.getId();
                updateOrInsertAttributes(id, albumCacheItem.getAttributes());
                this.mSortDate.put(id, sortDateProvider.getSortDateByAlbumPath(albumCacheItem.getDirectoryPath()));
                if (this.mCache.remove(albumCacheItem)) {
                    DefaultLogger.e(this.TAG, "insert dup AlbumItem,now remove");
                }
                this.mCache.add(albumCacheItem);
                linkedList.add(albumCacheItem);
                query.moveToNext();
            }
            query.close();
            return linkedList;
        } catch (Throwable th) {
            if (query != null) {
                try {
                    query.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public int update(long j, ContentValues contentValues) {
        return update("_id=?", new String[]{String.valueOf(j)}, contentValues);
    }

    public int update(Collection<Long> collection, ContentValues contentValues) {
        if (collection == null || collection.isEmpty()) {
            return 0;
        }
        return update("_id IN(?)", new String[]{TextUtils.join(",", collection)}, contentValues);
    }

    public final void recordIfIsScreenShotOrRecorderAlbumIds(String str, long j, boolean z) {
        if (!TextUtils.isEmpty(str)) {
            if (str.equalsIgnoreCase(MIUIStorageConstants.DIRECTORY_SCREENSHOT_PATH)) {
                if (z) {
                    j = 0;
                }
                this.mScreenshotsAlbumId = j;
            } else if (!str.equalsIgnoreCase(MIUIStorageConstants.DIRECTORY_SCREENRECORDER_PATH)) {
            } else {
                if (z) {
                    j = 0;
                }
                this.mScreenRecordersAlbumId = j;
            }
        }
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
            if (!"deleted".equals(asString) && !"purged".equals(asString) && !"toBePurged".equals(asString)) {
                return false;
            }
            DefaultLogger.d(this.TAG, "find a deleted server type");
            return true;
        }
    }

    @Override // com.miui.gallery.provider.cache.CacheManager
    public Cursor onCreateCursor(String[] strArr, String str, String[] strArr2, String str2, String str3, String str4, Bundle bundle, List<AlbumCacheItem> list) {
        return new RawCursor(list, strArr, this.mQueryFactory.getMapper());
    }

    public void updateAttributes(long j, boolean z, boolean z2, List<Long> list) {
        long j2;
        if (list == null || list.size() == 0) {
            return;
        }
        long packageAttributeBit = AlbumManager.packageAttributeBit(j, z, z2);
        long packageAttributeBit2 = AlbumManager.packageAttributeBit(j, true, true);
        if (Album.isRubbishAlbum(j)) {
            Bundle updateAttributesIfIsRubbishTag = CloudUtils.updateAttributesIfIsRubbishTag(j, packageAttributeBit, z);
            packageAttributeBit = updateAttributesIfIsRubbishTag.getLong(" packageAttibuteBit", packageAttributeBit);
            packageAttributeBit2 = updateAttributesIfIsRubbishTag.getLong("attributeBitMask", packageAttributeBit2);
        }
        if (!this.mInitializedAttributes) {
            synchronized (this.INITIALIZATION_ATTRIBUTES_LOCK) {
                DefaultLogger.fd(this.TAG, "update album Attributes,but not initialized, wait");
            }
        }
        Iterator<Long> it = list.iterator();
        while (it.hasNext()) {
            long longValue = it.next().longValue();
            long attributes = getAttributes(longValue);
            long j3 = ((~packageAttributeBit2) & attributes) | packageAttributeBit;
            ContentValues contentValues = new ContentValues();
            Iterator<Long> it2 = it;
            contentValues.put("attributes", Long.valueOf(j3));
            if (Album.isRubbishAlbum(j3) || Album.isHiddenAlbum(j3) || Album.isRubbishAlbum(attributes) || Album.isHiddenAlbum(attributes)) {
                if (!z) {
                    updateOrInsertAttributes(longValue, j3);
                }
                MediaManager mediaManager = MediaManager.getInstance();
                CacheItem.QueryFactory<MediaCacheItem> queryFactory = MediaManager.getInstance().getQueryFactory();
                Filter.Comparator comparator = Filter.Comparator.IN;
                StringBuilder sb = new StringBuilder();
                j2 = packageAttributeBit;
                sb.append("(");
                sb.append(TextUtils.join(",", list));
                sb.append(")");
                Collection<Long> calculateUpdatedAlbumIds = this.mUpdateManager.calculateUpdatedAlbumIds(MediaCalcItem.from(mediaManager.internalQueryByFilter(queryFactory.getFilter(2, comparator, sb.toString()))), false);
                if (z) {
                    updateOrInsertAttributes(longValue, j3);
                }
                update(calculateUpdatedAlbumIds, AlbumCacheItem.UPDATE_COVER_AND_COUNT);
            } else {
                j2 = packageAttributeBit;
                updateOrInsertAttributes(longValue, j3);
            }
            update(longValue, contentValues);
            it = it2;
            packageAttributeBit = j2;
        }
    }

    public void updateOrInsertAttributes(long j, long j2) {
        DefaultLogger.d(this.TAG, "update album[%d]'s attributes to %d", Long.valueOf(j), Long.valueOf(j2));
        this.mAttributes.put(Long.valueOf(j), Long.valueOf(j2));
    }

    public void deleteAttributes(long j) {
        DefaultLogger.d(this.TAG, "delete attributes for album[%d]", Long.valueOf(j));
        this.mAttributes.remove(Long.valueOf(j));
    }

    public boolean isShowInAllPhotoAlbum(long j) {
        long attributes = getAttributes(j);
        return !Album.isHiddenAlbum(attributes) && !Album.isRubbishAlbum(attributes);
    }

    public boolean isHidden(long j) {
        return Album.isHiddenAlbum(getAttributes(j));
    }

    public boolean isRubbish(long j) {
        return Album.isRubbishAlbum(getAttributes(j));
    }

    public boolean isAutoUpload(Long l) {
        return (getAttributes(l.longValue()) & 1) != 0;
    }

    public long getAttributes(long j) {
        Long l = this.mAttributes.get(Long.valueOf(j));
        if (l == null) {
            return 0L;
        }
        return l.longValue();
    }

    public long getScreenshotsAlbumId() {
        return this.mScreenshotsAlbumId;
    }

    public long getScreenRecordersAlbumId() {
        return this.mScreenRecordersAlbumId;
    }

    public MediaSortDateHelper.SortDate getSortDate(long j) {
        return this.mSortDate.get(j, MediaSortDateHelper.getSortDateProvider().getDefaultSortDate());
    }

    public List<AlbumCacheItem> filterFrom(List<AlbumCacheItem> list, QueryParam queryParam) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        String selection = queryParam.getSelection();
        String[] bindArgs = queryParam.getBindArgs();
        String groupBy = queryParam.getGroupBy();
        String orderBy = queryParam.getOrderBy();
        if (!TextUtils.isEmpty(queryParam.getHaving())) {
            DefaultLogger.e(this.TAG, "not support having..");
        }
        Filter from = Filter.from(selection, bindArgs, this.mQueryFactory);
        List<AlbumCacheItem> linkedList = new LinkedList<>();
        for (AlbumCacheItem albumCacheItem : list) {
            AlbumCacheItem albumCacheItem2 = (AlbumCacheItem) from.filter(albumCacheItem);
            if (albumCacheItem2 != null) {
                linkedList.add(albumCacheItem2);
            }
        }
        if (!TextUtils.isEmpty(groupBy)) {
            linkedList = group(linkedList, groupBy);
        }
        return (TextUtils.isEmpty(orderBy) || linkedList.isEmpty()) ? linkedList : sort(linkedList, orderBy);
    }

    @Override // com.miui.gallery.provider.cache.CacheManager
    public boolean isInitialized() {
        return this.mInitialized;
    }

    public boolean isInitializedLock() {
        if (this.mIsLoadDone) {
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

    public final void initLocalModelChangeListener() {
        GalleryPreferences.LocalMode.registerOnSharedPreferenceChangeListener(this.onSharedPreferenceChangeListener);
    }

    /* renamed from: com.miui.gallery.provider.cache.AlbumCacheManager$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements SharedPreferences.OnSharedPreferenceChangeListener {
        public static /* synthetic */ void $r8$lambda$3coxdy7Oj_6A9ErDWPPh2Yx0Vc8(AnonymousClass4 anonymousClass4) {
            anonymousClass4.lambda$onSharedPreferenceChanged$0();
        }

        public AnonymousClass4() {
            AlbumCacheManager.this = r1;
        }

        @Override // android.content.SharedPreferences.OnSharedPreferenceChangeListener
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String str) {
            if (GalleryPreferences.LocalMode.isOnlyShowLocalModeKey(str)) {
                AlbumCacheManager.this.mAlbumUpdateExecutor.execute(new Runnable() { // from class: com.miui.gallery.provider.cache.AlbumCacheManager$4$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        AlbumCacheManager.AnonymousClass4.$r8$lambda$3coxdy7Oj_6A9ErDWPPh2Yx0Vc8(AlbumCacheManager.AnonymousClass4.this);
                    }
                });
            }
        }

        public /* synthetic */ void lambda$onSharedPreferenceChanged$0() {
            AlbumCacheManager.this.internalQueryByFilter(new Filter.CompareFilter<AlbumCacheItem>(null, null) { // from class: com.miui.gallery.provider.cache.AlbumCacheManager.4.1
                {
                    AnonymousClass4.this = this;
                }

                @Override // com.miui.gallery.provider.cache.Filter
                public AlbumCacheItem filter(AlbumCacheItem albumCacheItem) {
                    albumCacheItem.fillCoverAndPhotoCount();
                    return null;
                }
            });
            GalleryApp.sGetAndroidContext().getContentResolver().notifyChange(GalleryContract.Album.URI_CACHE, (ContentObserver) null, false);
        }
    }

    @SuppressLint({"CheckResult"})
    public final void initTaskQueueDispatcher() {
        MediaManager.getInstance().addOperationListener(new CacheManager.OnOperationListener<MediaCacheItem>() { // from class: com.miui.gallery.provider.cache.AlbumCacheManager.5
            {
                AlbumCacheManager.this = this;
            }

            @Override // com.miui.gallery.provider.cache.CacheManager.OnOperationListener
            public void onUpdate(List<MediaCacheItem> list, ContentValues contentValues) {
                AlbumCacheItem.UpdateManager.SceneChecker matchChecker = AlbumCacheManager.this.mUpdateManager.matchChecker(list, contentValues);
                if (matchChecker != null) {
                    AlbumCacheManager.this.dispatchTask(TaskType.UPDATE_MEDIA, MediaCalcItem.from(list), matchChecker.preCalculateIfNeed(contentValues));
                }
            }

            @Override // com.miui.gallery.provider.cache.CacheManager.OnOperationListener
            public void onInsert(MediaCacheItem mediaCacheItem) {
                AlbumCacheManager.this.dispatchTask(TaskType.INSERT_MEDIA, Collections.singletonList(MediaCalcItem.from(mediaCacheItem)));
            }

            @Override // com.miui.gallery.provider.cache.CacheManager.OnOperationListener
            public void onRemove(List<MediaCacheItem> list) {
                AlbumCacheManager.this.dispatchTask(TaskType.DELETE_MEDIA, MediaCalcItem.from(list));
            }

            @Override // com.miui.gallery.provider.cache.CacheManager.OnOperationListener
            public void onBulkInsert(List<MediaCacheItem> list) {
                AlbumCacheManager.this.dispatchTask(TaskType.INSERT_MEDIA, MediaCalcItem.from(list));
            }
        });
        ShareMediaManager.getInstance().addOperationListener(new CacheManager.OnOperationListener<ShareMediaCacheItem>() { // from class: com.miui.gallery.provider.cache.AlbumCacheManager.6
            {
                AlbumCacheManager.this = this;
            }

            @Override // com.miui.gallery.provider.cache.CacheManager.OnOperationListener
            public void onUpdate(List<ShareMediaCacheItem> list, ContentValues contentValues) {
                AlbumCacheItem.UpdateManager.SceneChecker matchChecker = AlbumCacheManager.this.mUpdateManager.matchChecker(list, contentValues);
                if (matchChecker != null) {
                    AlbumCacheManager.this.dispatchTask(TaskType.UPDATE_MEDIA, list, matchChecker.preCalculateIfNeed(contentValues));
                }
            }

            @Override // com.miui.gallery.provider.cache.CacheManager.OnOperationListener
            public void onInsert(ShareMediaCacheItem shareMediaCacheItem) {
                AlbumCacheManager.this.dispatchTask(TaskType.INSERT_MEDIA, Collections.singletonList(MediaCalcItem.from(shareMediaCacheItem)));
            }

            @Override // com.miui.gallery.provider.cache.CacheManager.OnOperationListener
            public void onRemove(List<ShareMediaCacheItem> list) {
                AlbumCacheManager.this.dispatchTask(TaskType.DELETE_MEDIA, MediaCalcItem.from(list));
            }

            @Override // com.miui.gallery.provider.cache.CacheManager.OnOperationListener
            public void onBulkInsert(List<ShareMediaCacheItem> list) {
                AlbumCacheManager.this.dispatchTask(TaskType.INSERT_MEDIA, MediaCalcItem.from(list));
            }
        });
        addOperationListener(new AnonymousClass7());
    }

    /* renamed from: com.miui.gallery.provider.cache.AlbumCacheManager$7 */
    /* loaded from: classes2.dex */
    public class AnonymousClass7 implements CacheManager.OnOperationListener<AlbumCacheItem> {
        public AnonymousClass7() {
            AlbumCacheManager.this = r1;
        }

        @Override // com.miui.gallery.provider.cache.CacheManager.OnOperationListener
        public void onUpdate(List<AlbumCacheItem> list, ContentValues contentValues) {
            if (AlbumCacheManager.this.mUpdateManager.check(list, contentValues)) {
                AlbumCacheManager.this.dispatchTask(TaskType.UPDATE_ALBUM, AlbumCalcItem.from(list));
            }
        }

        @Override // com.miui.gallery.provider.cache.CacheManager.OnOperationListener
        public void onInsert(AlbumCacheItem albumCacheItem) {
            AlbumCacheManager.this.recordIfIsScreenShotOrRecorderAlbumIds(albumCacheItem.getDirectoryPath(), albumCacheItem.getId(), false);
            AlbumCacheManager.this.dispatchTask(TaskType.INSERT_ALBUM, Collections.singletonList(AlbumCalcItem.from(albumCacheItem)));
        }

        public static /* synthetic */ boolean lambda$onRemove$0(AlbumCacheItem albumCacheItem) {
            return !TextUtils.isEmpty(albumCacheItem.getDirectoryPath()) && (albumCacheItem.getDirectoryPath().equalsIgnoreCase(MIUIStorageConstants.DIRECTORY_SCREENSHOT_PATH) || albumCacheItem.getDirectoryPath().equalsIgnoreCase(MIUIStorageConstants.DIRECTORY_SCREENRECORDER_PATH));
        }

        @Override // com.miui.gallery.provider.cache.CacheManager.OnOperationListener
        public void onRemove(List<AlbumCacheItem> list) {
            list.stream().filter(AlbumCacheManager$7$$ExternalSyntheticLambda1.INSTANCE).forEach(new Consumer() { // from class: com.miui.gallery.provider.cache.AlbumCacheManager$7$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    AlbumCacheManager.AnonymousClass7.this.lambda$onRemove$1((AlbumCacheItem) obj);
                }
            });
            AlbumCacheManager.this.dispatchTask(TaskType.INSERT_ALBUM, AlbumCalcItem.from(list));
        }

        public /* synthetic */ void lambda$onRemove$1(AlbumCacheItem albumCacheItem) {
            AlbumCacheManager.this.recordIfIsScreenShotOrRecorderAlbumIds(albumCacheItem.getDirectoryPath(), albumCacheItem.getId(), true);
        }
    }

    public final ContentValues calculateScreenshotRecorderAttributes() {
        long attributes = getAttributes(this.mScreenshotsAlbumId);
        long attributes2 = getAttributes(this.mScreenRecordersAlbumId);
        long attributes3 = getAttributes(2147483645L);
        long[] jArr = {1, 2, 4, 8};
        for (int i = 0; i < 4; i++) {
            long j = jArr[i];
            int i2 = ((attributes & j) > 0L ? 1 : ((attributes & j) == 0L ? 0 : -1));
            if (i2 != 0 || (attributes2 & j) != 0) {
                attributes3 |= j;
            }
            if (i2 == 0 && (attributes2 & j) == 0) {
                attributes3 &= ~j;
            }
        }
        ContentValues contentValues = new ContentValues(1);
        contentValues.put("attributes", Long.valueOf(attributes3));
        updateOrInsertAttributes(2147483645L, attributes3);
        return contentValues;
    }

    public void dispatchTask(TaskType taskType, List<Object> list) {
        dispatchTask(taskType, list, null);
    }

    public void dispatchTask(TaskType taskType, final List<Object> list, List<Long> list2) {
        if (list == null || list.isEmpty()) {
            DefaultLogger.w(this.TAG, "nothing to dispatch");
            return;
        }
        DebugUtil.printThreadPoolTaskQueueStatus(this.mAlbumUpdateExecutor);
        DefaultLogger.v(this.TAG, "dispatch tasks: %s of %s", Integer.valueOf(list.size()), taskType.name());
        switch (AnonymousClass8.$SwitchMap$com$miui$gallery$provider$cache$AlbumCacheManager$TaskType[taskType.ordinal()]) {
            case 1:
            case 2:
            case 3:
            case 4:
                ensureMediaTaskCollector().onNext(new MediaOperationTaskContainer(taskType, list, list2));
                return;
            case 5:
            case 6:
            case 7:
                this.mAlbumUpdateExecutor.execute(new Runnable() { // from class: com.miui.gallery.provider.cache.AlbumCacheManager$$ExternalSyntheticLambda3
                    @Override // java.lang.Runnable
                    public final void run() {
                        AlbumCacheManager.$r8$lambda$qJdGEhTzjVcrEucpgyRml9VFDdQ(AlbumCacheManager.this, list);
                    }
                });
                return;
            default:
                return;
        }
    }

    /* renamed from: com.miui.gallery.provider.cache.AlbumCacheManager$8 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass8 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$provider$cache$AlbumCacheManager$TaskType;

        static {
            int[] iArr = new int[TaskType.values().length];
            $SwitchMap$com$miui$gallery$provider$cache$AlbumCacheManager$TaskType = iArr;
            try {
                iArr[TaskType.UPDATE_MEDIA.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$provider$cache$AlbumCacheManager$TaskType[TaskType.INSERT_MEDIA.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$provider$cache$AlbumCacheManager$TaskType[TaskType.DELETE_MEDIA.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$provider$cache$AlbumCacheManager$TaskType[TaskType.INSERT_TO_SHARE_MEDIA.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$provider$cache$AlbumCacheManager$TaskType[TaskType.INSERT_ALBUM.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$miui$gallery$provider$cache$AlbumCacheManager$TaskType[TaskType.UPDATE_ALBUM.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$miui$gallery$provider$cache$AlbumCacheManager$TaskType[TaskType.DELETE_ALBUM.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
        }
    }

    public /* synthetic */ void lambda$dispatchTask$0(List list) {
        if (update(this.mUpdateManager.calculateUpdatedAlbumIds(list, false), calculateScreenshotRecorderAttributes()) > 0) {
            GalleryApp.sGetAndroidContext().getContentResolver().notifyChange(GalleryContract.Album.URI_CACHE, (ContentObserver) null, false);
        }
    }

    public final Subject<MediaOperationTaskContainer> ensureMediaTaskCollector() {
        if (this.mMediaTaskCollector == null) {
            synchronized (this) {
                if (this.mMediaTaskCollector == null) {
                    this.mMediaTaskCollector = PublishSubject.create();
                    this.mMediaTaskCollector.observeOn(this.mMediaCollectorScheduler).buffer(1L, TimeUnit.SECONDS, this.mMediaCollectorScheduler, 200, AlbumCacheManager$$ExternalSyntheticLambda4.INSTANCE, true).filter(AlbumCacheManager$$ExternalSyntheticLambda2.INSTANCE).subscribe(new io.reactivex.functions.Consumer() { // from class: com.miui.gallery.provider.cache.AlbumCacheManager$$ExternalSyntheticLambda1
                        @Override // io.reactivex.functions.Consumer
                        public final void accept(Object obj) {
                            AlbumCacheManager.m1202$r8$lambda$AVsOCY_Utg9VqQP1BmZ3uL4Z0(AlbumCacheManager.this, (LinkedList) obj);
                        }
                    }, new io.reactivex.functions.Consumer() { // from class: com.miui.gallery.provider.cache.AlbumCacheManager$$ExternalSyntheticLambda0
                        @Override // io.reactivex.functions.Consumer
                        public final void accept(Object obj) {
                            AlbumCacheManager.$r8$lambda$CKQcLK3cBphAgQc3WRs9hZcyaBk(AlbumCacheManager.this, (Throwable) obj);
                        }
                    });
                }
            }
        }
        return this.mMediaTaskCollector;
    }

    public static /* synthetic */ boolean lambda$ensureMediaTaskCollector$1(LinkedList linkedList) throws Exception {
        return !linkedList.isEmpty();
    }

    public /* synthetic */ void lambda$ensureMediaTaskCollector$2(LinkedList linkedList) throws Exception {
        int i;
        Collection<Long> collection;
        Collection<Long> collection2;
        Iterator it = linkedList.iterator();
        LinkedList linkedList2 = null;
        LinkedList linkedList3 = null;
        LinkedList linkedList4 = null;
        while (it.hasNext()) {
            MediaOperationTaskContainer mediaOperationTaskContainer = (MediaOperationTaskContainer) it.next();
            if (mediaOperationTaskContainer.mItems != null) {
                TaskType taskType = mediaOperationTaskContainer.mTaskType;
                if (taskType == TaskType.INSERT_MEDIA || taskType == TaskType.INSERT_TO_SHARE_MEDIA) {
                    if (linkedList2 == null) {
                        linkedList2 = new LinkedList();
                    }
                    linkedList2.addAll(mediaOperationTaskContainer.mItems);
                } else {
                    if (linkedList3 == null) {
                        linkedList3 = new LinkedList();
                    }
                    linkedList3.addAll(mediaOperationTaskContainer.mItems);
                }
            }
            if (mediaOperationTaskContainer.mPreCalculateResult != null) {
                if (linkedList4 == null) {
                    linkedList4 = new LinkedList();
                }
                linkedList4.addAll(mediaOperationTaskContainer.mPreCalculateResult);
            }
        }
        if (linkedList2 != null) {
            collection = this.mUpdateManager.calculateUpdatedAlbumIds(linkedList2, false);
            i = collection.size() + 0;
        } else {
            i = 0;
            collection = null;
        }
        boolean z = true;
        if (linkedList3 != null) {
            collection2 = this.mUpdateManager.calculateUpdatedAlbumIds(linkedList3, true);
            i += collection2.size();
        } else {
            collection2 = null;
        }
        if (linkedList4 != null) {
            i += linkedList4.size();
        }
        if (i == 0) {
            return;
        }
        HashSet newHashSetWithExpectedSize = Sets.newHashSetWithExpectedSize(i);
        if (collection != null) {
            newHashSetWithExpectedSize.addAll(collection);
        }
        if (collection2 != null) {
            newHashSetWithExpectedSize.addAll(collection2);
        }
        if (linkedList4 != null) {
            newHashSetWithExpectedSize.addAll(linkedList4);
        }
        if (newHashSetWithExpectedSize.isEmpty() || update(newHashSetWithExpectedSize, AlbumCacheItem.UPDATE_COVER_AND_COUNT) <= 0) {
            z = false;
        }
        if (!z) {
            return;
        }
        GalleryApp.sGetAndroidContext().getContentResolver().notifyChange(GalleryContract.Album.URI_CACHE, (ContentObserver) null, false);
    }

    public /* synthetic */ void lambda$ensureMediaTaskCollector$3(Throwable th) throws Exception {
        DefaultLogger.e(this.TAG, "error info[%s]", th);
    }

    public void dump(PrintWriter printWriter) {
        printWriter.println("AlbumCache:");
        printWriter.print("  attrs initialized=");
        printWriter.println(this.mInitializedAttributes);
        if (this.mInitializedAttributes) {
            printWriter.print("    load attrs costs ");
            printWriter.print(this.mLoadAttributesCosts);
            printWriter.println(d.H);
        }
        printWriter.print("  albums initialized=");
        printWriter.println(this.mInitialized);
        if (this.mInitialized) {
            printWriter.print("    album count: ");
            printWriter.print(this.mCache.size());
            printWriter.print(", load albums costs ");
            printWriter.print(this.mLoadAlbumsCosts);
            printWriter.println(d.H);
        }
    }

    /* loaded from: classes2.dex */
    public static class MediaOperationTaskContainer {
        public List<Object> mItems;
        public List<Long> mPreCalculateResult;
        public TaskType mTaskType;

        public MediaOperationTaskContainer(TaskType taskType, List<Object> list, List<Long> list2) {
            this.mTaskType = taskType;
            this.mItems = list;
            this.mPreCalculateResult = list2;
        }
    }

    /* loaded from: classes2.dex */
    public class InitializeTask implements Runnable {
        public final SupportSQLiteDatabase mDataBases;

        public InitializeTask(SupportSQLiteDatabase supportSQLiteDatabase) {
            AlbumCacheManager.this = r1;
            this.mDataBases = supportSQLiteDatabase;
        }

        @Override // java.lang.Runnable
        public void run() {
            loadAlbumAttributes();
            ThreadManager.execute(31, new Runnable() { // from class: com.miui.gallery.provider.cache.AlbumCacheManager.InitializeTask.1
                {
                    InitializeTask.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    InitializeTask.this.loadAlbums();
                }
            });
        }

        public final void loadAlbumAttributes() {
            Trace.beginSection("AlbumCache#initAttributes");
            synchronized (AlbumCacheManager.this.INITIALIZATION_ATTRIBUTES_LOCK) {
                DefaultLogger.d(AlbumCacheManager.this.TAG, "start load attributes");
                long currentTimeMillis = System.currentTimeMillis();
                try {
                    Cursor query = this.mDataBases.query(SupportSQLiteQueryBuilder.builder("album").columns(new String[]{j.c, "attributes", "localPath"}).selection("(localFlag IS NULL OR localFlag NOT IN (-1, 0, 2) OR (localFlag=0 AND (serverStatus='custom')))", null).create());
                    try {
                        if (query != null) {
                            reInitSortDateArray(query.getCount() + GalleryContract.Album.ALL_VIRTUAL_ALBUM_IDS.length);
                            MediaSortDateHelper.SortDateProvider sortDateProvider = MediaSortDateHelper.getSortDateProvider();
                            query.moveToFirst();
                            while (!query.isAfterLast()) {
                                long j = query.getLong(0);
                                AlbumCacheManager.this.recordIfIsScreenShotOrRecorderAlbumIds(query.getString(2), j, false);
                                AlbumCacheManager.this.updateOrInsertAttributes(j, query.getLong(1));
                                AlbumCacheManager.this.mSortDate.put(j, sortDateProvider.getSortDateByAlbumPath(query.getString(2)));
                                query.moveToNext();
                            }
                        } else {
                            DefaultLogger.e(AlbumCacheManager.this.TAG, "load album attributes failed with a null cursor");
                        }
                        AlbumCacheManager.this.mInitializedAttributes = true;
                        if (query != null) {
                            query.close();
                        }
                        AlbumCacheManager.this.mLoadAttributesCosts = System.currentTimeMillis() - currentTimeMillis;
                        AlbumCacheManager albumCacheManager = AlbumCacheManager.this;
                        DefaultLogger.d(albumCacheManager.TAG, "end load attributes cost: %s", Long.valueOf(albumCacheManager.mLoadAttributesCosts));
                    } catch (Throwable th) {
                        if (query != null) {
                            try {
                                query.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        }
                        throw th;
                    }
                } catch (SQLiteException e) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("version", String.valueOf(this.mDataBases.getVersion()));
                    hashMap.put("error", e.getMessage() + " : " + e.getCause());
                    SamplingStatHelper.recordCountEvent("db_helper", "db_mediamanager_load", hashMap);
                    DebugUtil.exportDB(false);
                    GalleryDBHelper.getInstance().deleteDatabase();
                    throw e;
                }
            }
            Trace.endSection();
        }

        public final void reInitSortDateArray(int i) {
            if (AlbumCacheManager.this.mSortDate == null) {
                synchronized (AlbumCacheManager.this.INITIALIZATION_SORT_ARRAY_LOCK) {
                    if (AlbumCacheManager.this.mSortDate == null) {
                        AlbumCacheManager.this.mSortDate = new LongSparseArray(i);
                    }
                }
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public final void loadAlbums() {
            Trace.beginSection("AlbumCache#initAll");
            synchronized (AlbumCacheManager.this.INITIALIZATION_LOCK) {
                DefaultLogger.d(AlbumCacheManager.this.TAG, "start load albums");
                long currentTimeMillis = System.currentTimeMillis();
                try {
                    Cursor query = AlbumManager.query(this.mDataBases, AlbumManager.QUERY_ALBUM_PROJECTION, null, null, new QueryFlagsBuilder().queryAll().build());
                    try {
                        if (query != null) {
                            reInitSortDateArray(query.getCount());
                            MediaSortDateHelper.SortDateProvider sortDateProvider = MediaSortDateHelper.getSortDateProvider();
                            AlbumCacheItem albumCacheItem = null;
                            query.moveToFirst();
                            while (!query.isAfterLast()) {
                                AlbumCacheItem albumCacheItem2 = (AlbumCacheItem) AlbumCacheManager.this.mGenerator.mo1226from(query);
                                long id = albumCacheItem2.getId();
                                String directoryPath = albumCacheItem2.getDirectoryPath();
                                if (AlbumCacheManager.this.mScreenshotsAlbumId == 0 && AlbumCacheManager.this.mScreenRecordersAlbumId == 0) {
                                    AlbumCacheManager.this.recordIfIsScreenShotOrRecorderAlbumIds(directoryPath, id, false);
                                }
                                if (id == 2147483645) {
                                    albumCacheItem = albumCacheItem2;
                                }
                                AlbumCacheManager.this.updateOrInsertAttributes(id, albumCacheItem2.getAttributes());
                                AlbumCacheManager.this.mSortDate.put(id, sortDateProvider.getSortDateByAlbumPath(albumCacheItem2.getDirectoryPath()));
                                AlbumCacheManager.this.mCache.add(albumCacheItem2);
                                query.moveToNext();
                            }
                            if (albumCacheItem != null) {
                                AlbumCacheManager albumCacheManager = AlbumCacheManager.this;
                                albumCacheManager.mGenerator.update(albumCacheItem, albumCacheManager.calculateScreenshotRecorderAttributes());
                            }
                            AlbumCacheManager albumCacheManager2 = AlbumCacheManager.this;
                            DefaultLogger.d(albumCacheManager2.TAG, "loaded %d albums from cursor[%d]", Integer.valueOf(albumCacheManager2.mAttributes.size()), Integer.valueOf(query.getCount()));
                        } else {
                            DefaultLogger.e(AlbumCacheManager.this.TAG, "load albums failed with a null cursor");
                        }
                        AlbumCacheManager.this.mLoadAlbumsCosts = System.currentTimeMillis() - currentTimeMillis;
                        AlbumCacheManager albumCacheManager3 = AlbumCacheManager.this;
                        DefaultLogger.d(albumCacheManager3.TAG, "end load all albums cost: %s", Long.valueOf(albumCacheManager3.mLoadAlbumsCosts));
                        AlbumCacheManager.this.mInitialized = true;
                        removeUserCreateAlbumDetailSortInSp();
                        if (query != null) {
                            query.close();
                        }
                        notifyProgressUpdate();
                        AlbumCacheManager.this.mIsLoadDone = true;
                        AlbumCacheManager.this.mFullLoadDoneSignal.countDown();
                        synchronized (AlbumCacheManager.this.mInitializeListeners) {
                            AlbumCacheManager.this.mInitializeListeners.clear();
                        }
                        new CommonAlbumFileCacheDataSourceImpl().saveCache(AlbumCacheManager.this.mCache);
                    } catch (Throwable th) {
                        if (query != null) {
                            try {
                                query.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        }
                        throw th;
                    }
                } catch (SQLiteException e) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("version", String.valueOf(this.mDataBases.getVersion()));
                    hashMap.put("error", e.getMessage() + " : " + e.getCause());
                    SamplingStatHelper.recordCountEvent("db_helper", "db_mediamanager_load", hashMap);
                    DebugUtil.exportDB(false);
                    GalleryDBHelper.getInstance().deleteDatabase();
                    throw e;
                } catch (Exception e2) {
                    DefaultLogger.e(AlbumCacheManager.this.TAG, "init error:[%s]", e2.toString());
                    throw e2;
                }
            }
            Trace.endSection();
        }

        public final void notifyProgressUpdate() {
            synchronized (AlbumCacheManager.this.mInitializeListeners) {
                if (AlbumCacheManager.this.mInitializeListeners.size() > 0) {
                    Iterator it = AlbumCacheManager.this.mInitializeListeners.iterator();
                    while (it.hasNext()) {
                        InitializeListener initializeListener = (InitializeListener) it.next();
                        if (initializeListener != null) {
                            initializeListener.onProgressUpdate();
                        }
                    }
                }
            }
        }

        public final void removeUserCreateAlbumDetailSortInSp() {
            int albumDetailSort;
            if (!GalleryPreferences.Album.hasUpdatedAlbumDetailSort()) {
                List<T> list = AlbumCacheManager.this.mCache;
                if (list == 0 || list.isEmpty()) {
                    DefaultLogger.d(AlbumCacheManager.this.TAG, "mCache is empty");
                    return;
                }
                ArrayList arrayList = new ArrayList();
                for (T t : AlbumCacheManager.this.mCache) {
                    if (Album.isUserCreateAlbum(t.getDirectoryPath()) && ((albumDetailSort = GalleryPreferences.Album.getAlbumDetailSort(t.getId(), -1)) == 0 || albumDetailSort == 1)) {
                        arrayList.add(Long.valueOf(t.getId()));
                        DefaultLogger.d(AlbumCacheManager.this.TAG, "id=%s,albumCacheItem.getName()=%s,albumCacheItem.getDirectoryPath()=%s", Long.valueOf(t.getId()), t.getName(), t.getDirectoryPath());
                    }
                }
                GalleryPreferences.Album.removeUserCreateAlbumSort(arrayList);
                GalleryPreferences.Album.setHasUpdatedAlbumDetailSort(true);
            }
        }
    }
}
