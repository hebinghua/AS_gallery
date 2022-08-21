package com.miui.gallery.provider.cache;

import android.content.ContentResolver;
import android.content.Context;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Printer;
import com.google.common.cache.Cache;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.InternalContract$Album;
import com.miui.gallery.provider.cache.CacheItem;
import com.miui.gallery.provider.cache.SearchIconItem;
import com.miui.gallery.provider.cache.SearchIconManager;
import com.miui.gallery.search.core.display.icon.IconImageLoader;
import com.miui.gallery.search.utils.SearchLog;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.cloudimageloader.CloudUriAdapter;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.TimingTracing;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class SearchIconManager {
    public static final UriMatcher sIconURIMatcher;
    public final List<Uri> mObserveUris = new ArrayList();
    public final Printer mTracingPrinter = SearchIconManager$$ExternalSyntheticLambda0.INSTANCE;
    public final CacheItem.QueryFactory<SearchIconItem> mQueryFactory = new SearchIconItem.QueryFactory();
    public final Cache<String, SearchIconItem> mIconCache = IconImageLoader.getInstance().getMemoryCache();
    public final ContentObserver mContentObserver = new AnonymousClass1(ThreadManager.getMainHandler());

    public static /* synthetic */ void lambda$new$0(String str) {
    }

    /* renamed from: com.miui.gallery.provider.cache.SearchIconManager$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 extends ContentObserver {
        /* renamed from: $r8$lambda$l4sTDjUi4V-tiaCYxZq3rk9B5cw */
        public static /* synthetic */ Object m1223$r8$lambda$l4sTDjUi4VtiaCYxZq3rk9B5cw(AnonymousClass1 anonymousClass1, ThreadPool.JobContext jobContext) {
            return anonymousClass1.lambda$onChange$0(jobContext);
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass1(Handler handler) {
            super(handler);
            SearchIconManager.this = r1;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            ThreadManager.getMiscPool().submit(new ThreadPool.Job() { // from class: com.miui.gallery.provider.cache.SearchIconManager$1$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.concurrent.ThreadPool.Job
                /* renamed from: run */
                public final Object mo1807run(ThreadPool.JobContext jobContext) {
                    return SearchIconManager.AnonymousClass1.m1223$r8$lambda$l4sTDjUi4VtiaCYxZq3rk9B5cw(SearchIconManager.AnonymousClass1.this, jobContext);
                }
            });
        }

        public /* synthetic */ Object lambda$onChange$0(ThreadPool.JobContext jobContext) {
            SearchLog.d(".searchProvider.SearchIconManager", "On notify change, clear cache");
            SearchIconManager.this.releaseCache();
            return null;
        }
    }

    public void releaseCache() {
        this.mIconCache.invalidateAll();
        this.mIconCache.cleanUp();
        getContentResolver().unregisterContentObserver(this.mContentObserver);
        this.mObserveUris.clear();
    }

    public Cursor query(Uri uri, String[] strArr, Bundle bundle) {
        boolean z;
        boolean z2;
        boolean z3;
        TimingTracing.beginTracing("SearchIconManager-" + SystemClock.elapsedRealtimeNanos(), "query");
        try {
            ArrayList arrayList = new ArrayList(1);
            boolean z4 = false;
            if (bundle != null) {
                z2 = bundle.getBoolean("use_disk_cache", false);
                if (!z2 && !bundle.getBoolean("cache_to_disk", false)) {
                    z3 = false;
                    z = bundle.getBoolean("high_accuracy", false);
                }
                z3 = true;
                z = bundle.getBoolean("high_accuracy", false);
            } else {
                z = false;
                z2 = false;
                z3 = false;
            }
            SearchIconItem ifPresent = this.mIconCache.getIfPresent(uri.toString());
            TimingTracing.addSplit("query from memory cache");
            if (ifPresent == null && z2) {
                ifPresent = SearchIconDiskCache.getInstance().getIcon(uri.toString());
                if (ifPresent != null) {
                    z4 = true;
                }
                TimingTracing.addSplit("query from disk blob cache");
            }
            if (ifPresent == null) {
                ifPresent = queryIcon(uri, z);
                TimingTracing.addSplit("query from database");
                onIconQueried(uri.toString(), ifPresent, z3);
                TimingTracing.addSplit("dispatch query result");
            }
            if (ifPresent != null) {
                arrayList.add(ifPresent);
            }
            RawCursor rawCursor = new RawCursor(arrayList, strArr, this.mQueryFactory.getMapper());
            if (z4) {
                Bundle bundle2 = new Bundle();
                bundle2.putBoolean("from_unreliable_cache", true);
                rawCursor.setExtras(bundle2);
            }
            return rawCursor;
        } finally {
            TimingTracing.stopTracing(this.mTracingPrinter);
        }
    }

    public final void onIconQueried(String str, SearchIconItem searchIconItem, boolean z) {
        if (searchIconItem != null) {
            this.mIconCache.put(str, searchIconItem);
            if (z) {
                SearchIconDiskCache.getInstance().putIcon(str, searchIconItem);
            }
            Uri uri = searchIconItem.notifyUri;
            if (uri == null) {
                return;
            }
            observerUri(uri);
        }
    }

    public final Context getContext() {
        return GalleryApp.sGetAndroidContext();
    }

    public final ContentResolver getContentResolver() {
        return getContext().getContentResolver();
    }

    public final void observerUri(Uri uri) {
        if (!this.mObserveUris.contains(uri)) {
            getContentResolver().registerContentObserver(uri, true, this.mContentObserver);
            this.mObserveUris.add(uri);
        }
    }

    static {
        UriMatcher uriMatcher = new UriMatcher(-1);
        sIconURIMatcher = uriMatcher;
        uriMatcher.addURI("people", null, 1);
        uriMatcher.addURI("album", null, 2);
        uriMatcher.addURI("image", null, 3);
    }

    public final SearchIconItem queryIcon(Uri uri, boolean z) {
        int match = sIconURIMatcher.match(uri);
        if (match != 1) {
            if (match == 2) {
                return AlbumCoverIconLoader.queryIcon(uri, getContentResolver());
            }
            if (match != 3) {
                return null;
            }
            if (!TextUtils.isEmpty(uri.getQueryParameter("faceId"))) {
                return LocalImageFaceLoader.queryIcon(uri, getContext());
            }
            return LocalImageIconLoader.queryIcon(uri, z, getContentResolver());
        }
        return PeopleCoverIconLoader.queryIcon(uri, getContentResolver());
    }

    /* loaded from: classes2.dex */
    public static class LocalImageFaceLoader {
        public static final String[] PROJECTION = {"photo_id", "microthumbfile", "thumbnailFile", "localFile", "sha1", "faceXScale", "faceYScale", "faceWScale", "faceHScale"};

        public static /* synthetic */ SearchIconItem $r8$lambda$Dqr98jHdDxRD9wI6QhE4f9bIVh0(Uri uri, Cursor cursor) {
            return lambda$queryIcon$0(uri, cursor);
        }

        public static SearchIconItem queryIcon(final Uri uri, Context context) {
            String queryParameter = uri.getQueryParameter("serverId");
            return (SearchIconItem) SafeDBUtil.safeQuery(context, GalleryContract.PeopleFace.IMAGE_FACE_URI.buildUpon().appendQueryParameter("image_server_id", queryParameter).appendQueryParameter("serverId", uri.getQueryParameter("faceId")).build(), PROJECTION, (String) null, (String[]) null, (String) null, new SafeDBUtil.QueryHandler() { // from class: com.miui.gallery.provider.cache.SearchIconManager$LocalImageFaceLoader$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                /* renamed from: handle */
                public final Object mo1808handle(Cursor cursor) {
                    return SearchIconManager.LocalImageFaceLoader.$r8$lambda$Dqr98jHdDxRD9wI6QhE4f9bIVh0(uri, cursor);
                }
            });
        }

        public static /* synthetic */ SearchIconItem lambda$queryIcon$0(Uri uri, Cursor cursor) {
            if (cursor == null || !cursor.moveToFirst()) {
                return null;
            }
            String string = cursor.getString(2);
            if (TextUtils.isEmpty(string)) {
                string = cursor.getString(3);
            }
            if (TextUtils.isEmpty(string)) {
                string = cursor.getString(1);
            }
            if (TextUtils.isEmpty(string)) {
                string = StorageUtils.getSafePriorMicroThumbnailPath(cursor.getString(4));
            }
            SearchIconItem createIconItem = SearchIconManager.createIconItem(uri, string, cursor.getLong(0), cursor.getNotificationUri());
            createIconItem.decodeRegionX = Float.valueOf(cursor.getFloat(5));
            createIconItem.decodeRegionY = Float.valueOf(cursor.getFloat(6));
            createIconItem.decodeRegionW = Float.valueOf(cursor.getFloat(7));
            createIconItem.decodeRegionH = Float.valueOf(cursor.getFloat(8));
            return createIconItem;
        }
    }

    /* loaded from: classes2.dex */
    public static class LocalImageIconLoader {
        public static final String[] PROJECTION = {j.c, "alias_clear_thumbnail", "sha1"};

        /* JADX WARN: Code restructure failed: missing block: B:74:0x0089, code lost:
            if (r13 != null) goto L12;
         */
        /* JADX WARN: Code restructure failed: missing block: B:75:0x008b, code lost:
            r13.close();
         */
        /* JADX WARN: Code restructure failed: missing block: B:81:0x0098, code lost:
            if (r13 == null) goto L13;
         */
        /* JADX WARN: Code restructure failed: missing block: B:83:0x009b, code lost:
            return r8;
         */
        /* JADX WARN: Removed duplicated region for block: B:87:0x00a0  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public static com.miui.gallery.provider.cache.SearchIconItem queryIcon(android.net.Uri r12, boolean r13, android.content.ContentResolver r14) {
            /*
                android.net.Uri r0 = com.miui.gallery.provider.GalleryContract.Media.URI_ALL
                if (r13 == 0) goto L14
                android.net.Uri$Builder r13 = r0.buildUpon()
                java.lang.String r0 = "require_full_load"
                java.lang.String r1 = "true"
                android.net.Uri$Builder r13 = r13.appendQueryParameter(r0, r1)
                android.net.Uri r0 = r13.build()
            L14:
                r2 = r0
                java.lang.String r13 = "serverId"
                java.lang.String r0 = r12.getQueryParameter(r13)
                java.lang.String r1 = "id"
                java.lang.String r1 = r12.getQueryParameter(r1)
                boolean r3 = android.text.TextUtils.isEmpty(r0)
                java.lang.String r7 = ".searchProvider.SearchIconManager"
                java.lang.String r4 = "%s=%s"
                r8 = 0
                r9 = 1
                r10 = 0
                r11 = 2
                if (r3 != 0) goto L3d
                java.util.Locale r1 = java.util.Locale.US
                java.lang.Object[] r3 = new java.lang.Object[r11]
                r3[r10] = r13
                r3[r9] = r0
                java.lang.String r13 = java.lang.String.format(r1, r4, r3)
            L3b:
                r4 = r13
                goto L52
            L3d:
                boolean r13 = android.text.TextUtils.isEmpty(r1)
                if (r13 != 0) goto La4
                java.util.Locale r13 = java.util.Locale.US
                java.lang.Object[] r0 = new java.lang.Object[r11]
                java.lang.String r3 = "_id"
                r0[r10] = r3
                r0[r9] = r1
                java.lang.String r13 = java.lang.String.format(r13, r4, r0)
                goto L3b
            L52:
                java.lang.String[] r3 = com.miui.gallery.provider.cache.SearchIconManager.LocalImageIconLoader.PROJECTION     // Catch: java.lang.Throwable -> L8f java.lang.Exception -> L91
                r5 = 0
                r6 = 0
                r1 = r14
                android.database.Cursor r13 = r1.query(r2, r3, r4, r5, r6)     // Catch: java.lang.Throwable -> L8f java.lang.Exception -> L91
                if (r13 == 0) goto L89
                boolean r14 = r13.moveToFirst()     // Catch: java.lang.Exception -> L87 java.lang.Throwable -> L9c
                if (r14 == 0) goto L89
                java.lang.String r14 = r13.getString(r9)     // Catch: java.lang.Exception -> L87 java.lang.Throwable -> L9c
                boolean r0 = android.text.TextUtils.isEmpty(r14)     // Catch: java.lang.Exception -> L87 java.lang.Throwable -> L9c
                if (r0 == 0) goto L75
                java.lang.String r14 = r13.getString(r11)     // Catch: java.lang.Exception -> L87 java.lang.Throwable -> L9c
                java.lang.String r14 = com.miui.gallery.util.StorageUtils.getSafePriorMicroThumbnailPath(r14)     // Catch: java.lang.Exception -> L87 java.lang.Throwable -> L9c
            L75:
                android.net.Uri r0 = r13.getNotificationUri()     // Catch: java.lang.Exception -> L87 java.lang.Throwable -> L9c
                if (r0 != 0) goto L7d
                android.net.Uri r0 = com.miui.gallery.provider.GalleryContract.Media.URI     // Catch: java.lang.Exception -> L87 java.lang.Throwable -> L9c
            L7d:
                long r1 = r13.getLong(r10)     // Catch: java.lang.Exception -> L87 java.lang.Throwable -> L9c
                com.miui.gallery.provider.cache.SearchIconItem r12 = com.miui.gallery.provider.cache.SearchIconManager.access$100(r12, r14, r1, r0)     // Catch: java.lang.Exception -> L87 java.lang.Throwable -> L9c
                r8 = r12
                goto L89
            L87:
                r12 = move-exception
                goto L93
            L89:
                if (r13 == 0) goto L9b
            L8b:
                r13.close()
                goto L9b
            L8f:
                r12 = move-exception
                goto L9e
            L91:
                r12 = move-exception
                r13 = r8
            L93:
                java.lang.String r14 = "Error occurred while query icon uri %s"
                com.miui.gallery.search.utils.SearchLog.e(r7, r14, r12)     // Catch: java.lang.Throwable -> L9c
                if (r13 == 0) goto L9b
                goto L8b
            L9b:
                return r8
            L9c:
                r12 = move-exception
                r8 = r13
            L9e:
                if (r8 == 0) goto La3
                r8.close()
            La3:
                throw r12
            La4:
                java.lang.String r12 = "No valid id found for icon uri"
                com.miui.gallery.search.utils.SearchLog.e(r7, r12)
                return r8
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.cache.SearchIconManager.LocalImageIconLoader.queryIcon(android.net.Uri, boolean, android.content.ContentResolver):com.miui.gallery.provider.cache.SearchIconItem");
        }
    }

    /* loaded from: classes2.dex */
    public static class AlbumCoverIconLoader {
        public static final String[] PROJECTION = {"coverId", "coverPath", "coverSha1"};
        public static final String SELECTION = "=? AND (photoCount>0  OR (" + InternalContract$Album.ALIAS_USER_CREATE_ALBUM + ")";

        /* JADX WARN: Code restructure failed: missing block: B:59:0x0083, code lost:
            if (r15 != null) goto L8;
         */
        /* JADX WARN: Code restructure failed: missing block: B:60:0x0085, code lost:
            r15.close();
         */
        /* JADX WARN: Code restructure failed: missing block: B:66:0x0092, code lost:
            if (r15 == null) goto L9;
         */
        /* JADX WARN: Code restructure failed: missing block: B:68:0x0095, code lost:
            return r5;
         */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:72:0x009a  */
        /* JADX WARN: Type inference failed for: r5v0 */
        /* JADX WARN: Type inference failed for: r5v1, types: [android.database.Cursor] */
        /* JADX WARN: Type inference failed for: r5v2 */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public static com.miui.gallery.provider.cache.SearchIconItem queryIcon(android.net.Uri r14, android.content.ContentResolver r15) {
            /*
                java.lang.String r0 = "serverId"
                java.lang.String r1 = r14.getQueryParameter(r0)
                java.lang.String r2 = "id"
                java.lang.String r2 = r14.getQueryParameter(r2)
                boolean r3 = android.text.TextUtils.isEmpty(r1)
                java.lang.String r4 = ".searchProvider.SearchIconManager"
                r5 = 0
                r6 = 0
                r7 = 1
                if (r3 != 0) goto L2f
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                r2.append(r0)
                java.lang.String r0 = com.miui.gallery.provider.cache.SearchIconManager.AlbumCoverIconLoader.SELECTION
                r2.append(r0)
                java.lang.String r0 = r2.toString()
                java.lang.String[] r2 = new java.lang.String[r7]
                r2[r6] = r1
                r11 = r0
                r12 = r2
                goto L4e
            L2f:
                boolean r0 = android.text.TextUtils.isEmpty(r2)
                if (r0 != 0) goto L9e
                java.lang.StringBuilder r0 = new java.lang.StringBuilder
                r0.<init>()
                java.lang.String r1 = "_id"
                r0.append(r1)
                java.lang.String r1 = com.miui.gallery.provider.cache.SearchIconManager.AlbumCoverIconLoader.SELECTION
                r0.append(r1)
                java.lang.String r0 = r0.toString()
                java.lang.String[] r1 = new java.lang.String[r7]
                r1[r6] = r2
                r11 = r0
                r12 = r1
            L4e:
                android.net.Uri r9 = com.miui.gallery.provider.GalleryContract.Album.URI_QUERY_ALL_AND_EXCEPT_DELETED     // Catch: java.lang.Throwable -> L89 java.lang.Exception -> L8b
                java.lang.String[] r10 = com.miui.gallery.provider.cache.SearchIconManager.AlbumCoverIconLoader.PROJECTION     // Catch: java.lang.Throwable -> L89 java.lang.Exception -> L8b
                r13 = 0
                r8 = r15
                android.database.Cursor r15 = r8.query(r9, r10, r11, r12, r13)     // Catch: java.lang.Throwable -> L89 java.lang.Exception -> L8b
                if (r15 == 0) goto L83
                boolean r0 = r15.moveToFirst()     // Catch: java.lang.Exception -> L81 java.lang.Throwable -> L96
                if (r0 == 0) goto L83
                java.lang.String r0 = r15.getString(r7)     // Catch: java.lang.Exception -> L81 java.lang.Throwable -> L96
                boolean r1 = android.text.TextUtils.isEmpty(r0)     // Catch: java.lang.Exception -> L81 java.lang.Throwable -> L96
                if (r1 == 0) goto L73
                r0 = 2
                java.lang.String r0 = r15.getString(r0)     // Catch: java.lang.Exception -> L81 java.lang.Throwable -> L96
                java.lang.String r0 = com.miui.gallery.util.StorageUtils.getSafePriorMicroThumbnailPath(r0)     // Catch: java.lang.Exception -> L81 java.lang.Throwable -> L96
            L73:
                long r1 = r15.getLong(r6)     // Catch: java.lang.Exception -> L81 java.lang.Throwable -> L96
                android.net.Uri r3 = r15.getNotificationUri()     // Catch: java.lang.Exception -> L81 java.lang.Throwable -> L96
                com.miui.gallery.provider.cache.SearchIconItem r14 = com.miui.gallery.provider.cache.SearchIconManager.access$100(r14, r0, r1, r3)     // Catch: java.lang.Exception -> L81 java.lang.Throwable -> L96
                r5 = r14
                goto L83
            L81:
                r14 = move-exception
                goto L8d
            L83:
                if (r15 == 0) goto L95
            L85:
                r15.close()
                goto L95
            L89:
                r14 = move-exception
                goto L98
            L8b:
                r14 = move-exception
                r15 = r5
            L8d:
                java.lang.String r0 = "Error occurred while query icon %s"
                com.miui.gallery.search.utils.SearchLog.e(r4, r0, r14)     // Catch: java.lang.Throwable -> L96
                if (r15 == 0) goto L95
                goto L85
            L95:
                return r5
            L96:
                r14 = move-exception
                r5 = r15
            L98:
                if (r5 == 0) goto L9d
                r5.close()
            L9d:
                throw r14
            L9e:
                java.lang.String r14 = "No valid id found for icon uri"
                com.miui.gallery.search.utils.SearchLog.e(r4, r14)
                return r5
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.cache.SearchIconManager.AlbumCoverIconLoader.queryIcon(android.net.Uri, android.content.ContentResolver):com.miui.gallery.provider.cache.SearchIconItem");
        }
    }

    /* loaded from: classes2.dex */
    public static class PeopleCoverIconLoader {
        public static final String[] PROJECTION = {"photo_id", "microthumbfile", "thumbnailFile", "localFile", "sha1", "exifOrientation", "faceXScale", "faceYScale", "faceWScale", "faceHScale"};

        public static SearchIconItem queryIcon(Uri uri, ContentResolver contentResolver) {
            SearchIconItem searchIconItem;
            String queryParameter = uri.getQueryParameter("serverId");
            String queryParameter2 = uri.getQueryParameter("id");
            Uri.Builder buildUpon = GalleryContract.PeopleFace.PEOPLE_COVER_URI.buildUpon();
            Cursor cursor = null;
            r6 = null;
            r6 = null;
            SearchIconItem searchIconItem2 = null;
            cursor = null;
            if (!TextUtils.isEmpty(queryParameter)) {
                buildUpon.appendQueryParameter("serverId", queryParameter);
            } else if (!TextUtils.isEmpty(queryParameter2)) {
                buildUpon.appendQueryParameter(j.c, queryParameter2).build();
            } else {
                SearchLog.w(".searchProvider.SearchIconManager", "No valid id found for uri");
                return null;
            }
            try {
                try {
                    Cursor query = contentResolver.query(buildUpon.build(), PROJECTION, null, null, null);
                    if (query != null) {
                        try {
                            if (query.moveToFirst()) {
                                String string = query.getString(2);
                                if (TextUtils.isEmpty(string)) {
                                    string = query.getString(3);
                                }
                                if (TextUtils.isEmpty(string)) {
                                    string = query.getString(1);
                                }
                                if (TextUtils.isEmpty(string)) {
                                    string = StorageUtils.getSafePriorMicroThumbnailPath(query.getString(4));
                                }
                                searchIconItem2 = SearchIconManager.createIconItem(uri, string, query.getLong(0), query.getNotificationUri());
                                searchIconItem2.decodeRegionOrientation = query.getInt(5);
                                searchIconItem2.decodeRegionX = Float.valueOf(query.getFloat(6));
                                searchIconItem2.decodeRegionY = Float.valueOf(query.getFloat(7));
                                searchIconItem2.decodeRegionW = Float.valueOf(query.getFloat(8));
                                searchIconItem2.decodeRegionH = Float.valueOf(query.getFloat(9));
                            }
                        } catch (Exception e) {
                            e = e;
                            SearchIconItem searchIconItem3 = searchIconItem2;
                            cursor = query;
                            searchIconItem = searchIconItem3;
                            SearchLog.e(".searchProvider.SearchIconManager", "Error occurred while query icon uri %s", e);
                            if (cursor != null) {
                                cursor.close();
                            }
                            return searchIconItem;
                        } catch (Throwable th) {
                            th = th;
                            cursor = query;
                            if (cursor != null) {
                                cursor.close();
                            }
                            throw th;
                        }
                    }
                    if (query == null) {
                        return searchIconItem2;
                    }
                    query.close();
                    return searchIconItem2;
                } catch (Exception e2) {
                    e = e2;
                    searchIconItem = null;
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    public static SearchIconItem createIconItem(Uri uri, String str, long j, Uri uri2) {
        SearchIconItem searchIconItem = new SearchIconItem();
        searchIconItem.iconUri = uri.toString();
        searchIconItem.filePath = str;
        searchIconItem.downloadUri = getDownloadUri(j);
        searchIconItem.notifyUri = uri2;
        return searchIconItem;
    }

    public static String getDownloadUri(long j) {
        Uri downloadUri = CloudUriAdapter.getDownloadUri(j);
        if (downloadUri == null) {
            return null;
        }
        return downloadUri.toString();
    }
}
