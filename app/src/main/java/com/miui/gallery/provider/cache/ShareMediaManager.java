package com.miui.gallery.provider.cache;

import android.accounts.Account;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Looper;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import com.miui.account.AccountHelper;
import com.miui.gallery.Config$PictureView;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.DownloadPathHelper;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.TimelineHeadersGroup;
import com.miui.gallery.provider.cache.CacheManager;
import com.miui.gallery.provider.cache.RecentMediaHeadersHelper;
import com.miui.gallery.provider.cache.ShareMediaCacheItem;
import com.miui.gallery.util.GalleryDateUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.thread.ThreadManager;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import kotlin.Pair;
import miuix.core.util.Pools;

/* loaded from: classes2.dex */
public class ShareMediaManager extends CacheManager<ShareMediaCacheItem> {
    public final CountDownLatch mFullLoadDoneSignal;
    public volatile boolean mInitialized;
    public volatile boolean mIsFullLoadDone;
    public volatile boolean mIsLoadStarted;

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final ShareMediaManager INSTANCE = new ShareMediaManager();
    }

    public static /* synthetic */ int $r8$lambda$GJqHunn3D21W41VaZiQrbF2CVis(int i, MediaCacheItem mediaCacheItem, MediaCacheItem mediaCacheItem2) {
        return lambda$generateHeaderInfo$1(i, mediaCacheItem, mediaCacheItem2);
    }

    public static /* synthetic */ int $r8$lambda$QwY43VvYH6s8nJrRYA62O9B7BAM(int i, MediaCacheItem mediaCacheItem, MediaCacheItem mediaCacheItem2) {
        return lambda$generateHeaderInfo$2(i, mediaCacheItem, mediaCacheItem2);
    }

    /* renamed from: $r8$lambda$p8HHQbRlB7phAKeBI-Ym4I7Jy8o */
    public static /* synthetic */ int m1227$r8$lambda$p8HHQbRlB7phAKeBIYm4I7Jy8o(int i, MediaCacheItem mediaCacheItem, MediaCacheItem mediaCacheItem2) {
        return lambda$generateHeaderInfo$0(i, mediaCacheItem, mediaCacheItem2);
    }

    public static long convertToMediaId(long j) {
        return j + 16777215;
    }

    public static long getOriginalMediaId(long j) {
        return j - 16777215;
    }

    public static boolean isOtherShareMediaId(long j) {
        return j >= 16777215 && j < 67108863;
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

    public ShareMediaManager() {
        this.mIsFullLoadDone = false;
        this.mIsLoadStarted = false;
        this.mInitialized = false;
        this.mFullLoadDoneSignal = new CountDownLatch(1);
        this.mCache = new LinkedList();
        this.mGenerator = new ShareMediaCacheItem.Generator(new FavoritesDelegate());
        this.mQueryFactory = new ShareMediaCacheItem.QueryFactory();
    }

    public static ShareMediaManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public synchronized void load(SupportSQLiteDatabase supportSQLiteDatabase) {
        if (!this.mIsLoadStarted && !this.mIsFullLoadDone) {
            this.mIsLoadStarted = true;
            if (Looper.getMainLooper() == Looper.myLooper()) {
                ThreadManager.execute(31, new InitializeTask(supportSQLiteDatabase));
            } else {
                new InitializeTask(supportSQLiteDatabase).run();
            }
        }
    }

    /* loaded from: classes2.dex */
    public class InitializeTask implements Runnable {
        public SupportSQLiteDatabase mDatabase;

        public InitializeTask(SupportSQLiteDatabase supportSQLiteDatabase) {
            ShareMediaManager.this = r1;
            this.mDatabase = supportSQLiteDatabase;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                DefaultLogger.d("ShareMediaManagerNew", "start initialize");
                loadFullPart(this.mDatabase);
                ShareMediaManager.this.mInitialized = true;
            } finally {
                ShareMediaManager.this.mIsFullLoadDone = true;
                ShareMediaManager.this.mFullLoadDoneSignal.countDown();
            }
        }

        public final void loadFullPart(SupportSQLiteDatabase supportSQLiteDatabase) {
            DefaultLogger.d("ShareMediaManagerNew", "start load otherShareMedia");
            long currentTimeMillis = System.currentTimeMillis();
            ArrayList arrayList = new ArrayList();
            Cursor cursor = null;
            try {
                Cursor query = supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("shareImage").columns(ShareMediaManager.this.mGenerator.getProjection()).selection("(localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))", null).create());
                if (query == null) {
                    if (query == null) {
                        return;
                    }
                    query.close();
                    return;
                }
                while (query.moveToNext()) {
                    arrayList.add((ShareMediaCacheItem) ShareMediaManager.this.mGenerator.mo1226from(query));
                }
                publishResult(arrayList);
                query.close();
                DefaultLogger.d("ShareMediaManagerNew", "load shareMedia cost: %d, count = %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis), Integer.valueOf(ShareMediaManager.this.mCache.size()));
            } catch (Throwable th) {
                if (0 != 0) {
                    cursor.close();
                }
                throw th;
            }
        }

        public final void publishResult(List<ShareMediaCacheItem> list) {
            ShareMediaManager.this.mWriteLock.lock();
            try {
                ShareMediaManager.this.mCache.addAll(list);
            } finally {
                ShareMediaManager.this.mWriteLock.unlock();
            }
        }
    }

    @Override // com.miui.gallery.provider.cache.CacheManager
    public boolean isInitialized() {
        return this.mInitialized;
    }

    @Override // com.miui.gallery.provider.cache.CacheManager
    public <R> R query(String[] strArr, String str, String[] strArr2, String str2, String str3, String str4, Bundle bundle, CacheManager.ResultConverter<ShareMediaCacheItem, R> resultConverter) {
        String str5 = "(localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))";
        if (str != null) {
            StringBuilder acquire = Pools.getStringBuilderPool().acquire();
            acquire.append(str);
            acquire.append(" AND ");
            acquire.append(str5);
            str5 = acquire.toString();
            Pools.getStringBuilderPool().release(acquire);
        }
        return (R) super.query(strArr, str5, strArr2, str2, str3, str4, bundle, resultConverter);
    }

    @Override // com.miui.gallery.provider.cache.CacheManager
    public long insert(long j, ContentValues contentValues) {
        if (!isItemDeleted(contentValues)) {
            return super.insert(j, contentValues);
        }
        return 0L;
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
            DefaultLogger.d("ShareMediaManagerNew", "find a deleted local flag");
            return true;
        } else if (!contentValues.containsKey("serverStatus")) {
            return false;
        } else {
            String asString = contentValues.getAsString("serverStatus");
            if (!"deleted".equals(asString) && !"purged".equals(asString) && !"toBePurged".equals(asString)) {
                return false;
            }
            DefaultLogger.d("ShareMediaManagerNew", "find a deleted server type");
            return true;
        }
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
    public <R> List<R> query(String str, String[] strArr, String str2, String str3, String str4, Bundle bundle, IMediaProcessor<? super ShareMediaCacheItem, R> iMediaProcessor) {
        List<ShareMediaCacheItem> doQuery = doQuery(str, strArr, str2, str3, str4);
        if (bundle != null && bundle.getBoolean("extra_generate_header")) {
            bundle.putInt("extra_group_sort_column", parseOrderByColumnIndex(str2));
        }
        return iMediaProcessor.processCache(doQuery, bundle);
    }

    @Override // com.miui.gallery.provider.cache.CacheManager
    public Cursor onCreateCursor(String[] strArr, String str, String[] strArr2, String str2, String str3, String str4, Bundle bundle, List<ShareMediaCacheItem> list) {
        RawCursor rawCursor = new RawCursor(list, strArr, this.mQueryFactory.getMapper());
        return (rawCursor.getCount() <= 0 || !isSupportTimelineCursor(rawCursor) || bundle == null) ? rawCursor : generateHeaderInfo(rawCursor, list, strArr, str2, bundle);
    }

    public static String getMediaFileName(String str, String str2) {
        return DownloadPathHelper.addPostfixToFileName(str, str2);
    }

    public static ArrayList<Long> getOwnerSharedImageIds(Map<Long, String> map) {
        Account xiaomiAccount;
        ArrayList<Long> arrayList = null;
        if (map != null && map.size() != 0 && (xiaomiAccount = AccountHelper.getXiaomiAccount(GalleryApp.sGetAndroidContext())) != null) {
            arrayList = new ArrayList<>();
            for (Map.Entry<Long, String> entry : map.entrySet()) {
                if (TextUtils.equals(entry.getValue(), xiaomiAccount.name) || isSharedImageUnsynced(entry.getKey().longValue())) {
                    arrayList.add(entry.getKey());
                }
            }
        }
        return arrayList;
    }

    public static boolean isSharedImageUnsynced(long j) {
        boolean z = true;
        Cursor query = GalleryApp.sGetAndroidContext().getContentResolver().query(GalleryContract.ShareImage.SHARE_URI, null, "_id = ? AND serverId IS NULL", new String[]{String.valueOf(getOriginalMediaId(j))}, null);
        if (query != null) {
            try {
                if (query.getCount() <= 0) {
                    z = false;
                }
                return z;
            } finally {
                query.close();
            }
        }
        return false;
    }

    public static boolean isSupportTimelineCursor(Cursor cursor) {
        return (cursor == null || cursor.getColumnIndex("alias_create_date") == -1 || cursor.getColumnIndex("location") == -1) ? false : true;
    }

    public final Cursor generateHeaderInfo(Cursor cursor, List<ShareMediaCacheItem> list, String[] strArr, String str, Bundle bundle) {
        final int i;
        RawCursor rawCursor;
        if (cursor == null || cursor.getCount() <= 0 || cursor.getColumnIndex("alias_create_date") == -1 || cursor.getColumnIndex("location") == -1) {
            return cursor;
        }
        boolean z = bundle.getBoolean("extra_timeline_only_show_valid_location", true);
        int i2 = bundle.getInt("extra_media_group_by");
        if ((i2 & 7) != 0) {
            int indexOf = str.indexOf(32);
            if (indexOf <= 0) {
                indexOf = str.length();
            }
            String substring = str.substring(0, indexOf);
            int index = this.mQueryFactory.getMapper().getIndex(substring);
            if (index < 0) {
                throw new IllegalArgumentException(substring + " not found");
            }
            i = transformOrderByColumnIndex(index);
            ArrayList arrayList = new ArrayList(3);
            if ((i2 & 1) == 1) {
                arrayList.add(new Pair(1, new Comparator() { // from class: com.miui.gallery.provider.cache.ShareMediaManager$$ExternalSyntheticLambda2
                    @Override // java.util.Comparator
                    public final int compare(Object obj, Object obj2) {
                        return ShareMediaManager.m1227$r8$lambda$p8HHQbRlB7phAKeBIYm4I7Jy8o(i, (MediaCacheItem) obj, (MediaCacheItem) obj2);
                    }
                }));
            }
            if (!Config$PictureView.isFilterImagesForMonthView()) {
                if ((i2 & 2) == 2) {
                    arrayList.add(new Pair(2, new Comparator() { // from class: com.miui.gallery.provider.cache.ShareMediaManager$$ExternalSyntheticLambda0
                        @Override // java.util.Comparator
                        public final int compare(Object obj, Object obj2) {
                            return ShareMediaManager.$r8$lambda$GJqHunn3D21W41VaZiQrbF2CVis(i, (MediaCacheItem) obj, (MediaCacheItem) obj2);
                        }
                    }));
                }
                if ((i2 & 4) == 4) {
                    arrayList.add(new Pair(4, new Comparator() { // from class: com.miui.gallery.provider.cache.ShareMediaManager$$ExternalSyntheticLambda1
                        @Override // java.util.Comparator
                        public final int compare(Object obj, Object obj2) {
                            return ShareMediaManager.$r8$lambda$QwY43VvYH6s8nJrRYA62O9B7BAM(i, (MediaCacheItem) obj, (MediaCacheItem) obj2);
                        }
                    }));
                }
            }
            rawCursor = new RawCursor(list, strArr, this.mQueryFactory.getMapper());
            TimelineHeadersGroup.bindGroups(MediaGroupingHelper.generateGroups(list, arrayList, z), rawCursor);
        } else if (i2 == 8) {
            i = 31;
            ArrayList arrayList2 = new ArrayList(list.size());
            List<RecentMediaHeadersHelper.Header> resortAndGenerateHeaders = RecentMediaHeadersHelper.resortAndGenerateHeaders(list, arrayList2);
            rawCursor = new RawCursor(arrayList2, strArr, this.mQueryFactory.getMapper());
            RecentMediaHeadersHelper.bindGroup2Cursor(resortAndGenerateHeaders, rawCursor);
        } else {
            throw new IllegalArgumentException(String.format("unsupported group type %s", Integer.valueOf(i2)));
        }
        Bundle extras = rawCursor.getExtras();
        if (extras == null || extras == Bundle.EMPTY) {
            extras = new Bundle();
            rawCursor.setExtras(extras);
        }
        extras.putInt("extra_group_sort_column", i);
        return rawCursor;
    }

    public static /* synthetic */ int lambda$generateHeaderInfo$0(int i, MediaCacheItem mediaCacheItem, MediaCacheItem mediaCacheItem2) {
        if (mediaCacheItem.columnEquals(mediaCacheItem2, i)) {
            return 0;
        }
        return mediaCacheItem2.getAliasSortDate() - mediaCacheItem.getAliasSortDate();
    }

    public static /* synthetic */ int lambda$generateHeaderInfo$1(int i, MediaCacheItem mediaCacheItem, MediaCacheItem mediaCacheItem2) {
        if (mediaCacheItem.columnEquals(mediaCacheItem2, i) || GalleryDateUtils.isSameMonth(mediaCacheItem.getAliasSortDate(), mediaCacheItem2.getAliasSortDate())) {
            return 0;
        }
        return mediaCacheItem2.getAliasSortDate() - mediaCacheItem.getAliasSortDate();
    }

    public static /* synthetic */ int lambda$generateHeaderInfo$2(int i, MediaCacheItem mediaCacheItem, MediaCacheItem mediaCacheItem2) {
        if (mediaCacheItem.columnEquals(mediaCacheItem2, i) || GalleryDateUtils.isSameYear(mediaCacheItem.getAliasSortDate(), mediaCacheItem2.getAliasSortDate())) {
            return 0;
        }
        return mediaCacheItem2.getAliasSortDate() - mediaCacheItem.getAliasSortDate();
    }
}
