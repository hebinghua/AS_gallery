package com.miui.gallery.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.FrameLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.tracing.Trace;
import ch.qos.logback.core.CoreConstants;
import com.android.internal.CompatHandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.common.base.Joiner;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.activity.HomePageStartupHelper2;
import com.miui.gallery.adapter.HomePageAdapter2;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.dao.GalleryLiteEntityManager;
import com.miui.gallery.dao.base.EntityTransaction;
import com.miui.gallery.data.Cluster;
import com.miui.gallery.data.ClusteredList;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.glide.request.target.HomeMediaPreloadTarget;
import com.miui.gallery.loader.HomeMediaLoader;
import com.miui.gallery.model.HomeMedia;
import com.miui.gallery.model.HomeMediaHeader;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.cache.CacheLiveData;
import com.miui.gallery.provider.cache.IMediaSnapshot;
import com.miui.gallery.provider.cache.IRecord;
import com.miui.gallery.provider.cache.MediaCacheItem;
import com.miui.gallery.provider.cache.MediaProcessor;
import com.miui.gallery.scanner.extra.snapshot.InsertedValue;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.threadpool.GallerySchedulers;
import com.miui.gallery.ui.AsyncViewPrefetcher;
import com.miui.gallery.ui.ViewProvider;
import com.miui.gallery.ui.album.main.utils.AlbumConfigSharedPreferences;
import com.miui.gallery.ui.album.main.utils.factory.GalleryViewCreator;
import com.miui.gallery.ui.pictures.PictureViewMode;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.GalleryMemoryCacheHelper;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.UriUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.security.CrossUserCompatActivity;
import com.xiaomi.stat.d;
import io.reactivex.functions.Function;
import io.reactivex.internal.functions.Functions;
import io.reactivex.subjects.PublishSubject;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.Unit;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.io.CloseableKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt___RangesKt;

/* compiled from: HomePageStartupHelper2.kt */
/* loaded from: classes.dex */
public final class HomePageStartupHelper2 {
    public static final Companion Companion = new Companion(null);
    public static volatile PublishSubject<CacheLiveData<MediaCacheItem, IRecord>> sSnapshotUpdater;
    public Attacher attacher;
    public final Context context;
    public Future<Unit> dataLoadFuture;
    public volatile List<? extends IRecord> homePageList;
    public volatile boolean isDataInitialized;
    public volatile boolean isDestroyed;
    public Runnable prefetchRunnable;
    public AsyncViewPrefetcher prefetcher;
    public Function1<? super List<? extends IRecord>, Unit> preloadListener;

    /* compiled from: HomePageStartupHelper2.kt */
    /* loaded from: classes.dex */
    public interface Attacher {
        HomePageStartupHelper2 getStartupHelper();

        void onStartup();
    }

    public static /* synthetic */ Unit $r8$lambda$PmKqAfmGhXd4_X_CzMmQIm5twvQ(HomePageStartupHelper2 homePageStartupHelper2, ThreadPool.JobContext jobContext) {
        return m461loadHomePageData$lambda6(homePageStartupHelper2, jobContext);
    }

    /* renamed from: $r8$lambda$UmyHBM2m7-ylOpuG_FycNz-R3N0 */
    public static /* synthetic */ void m460$r8$lambda$UmyHBM2m7ylOpuG_FycNzR3N0(HomePageStartupHelper2 homePageStartupHelper2) {
        m463loadHomePageData$lambda6$lambda5(homePageStartupHelper2);
    }

    public static /* synthetic */ void $r8$lambda$VlQdnStAWpox6sjXj1V2Xkgivjw(HomePageStartupHelper2 homePageStartupHelper2) {
        m462loadHomePageData$lambda6$lambda2(homePageStartupHelper2);
    }

    public static /* synthetic */ void $r8$lambda$ycMwRhiu7R6El3tFv0bKLhHOe7o(HomePageStartupHelper2 homePageStartupHelper2) {
        m464onActivityDestroy$lambda10(homePageStartupHelper2);
    }

    public static final void registerSnapshotUpdateListener(Context context) {
        Companion.registerSnapshotUpdateListener(context);
    }

    public static final void startPreloadHomePageDatasTask(Context context) {
        Companion.startPreloadHomePageDatasTask(context);
    }

    /* compiled from: HomePageStartupHelper2.kt */
    /* loaded from: classes.dex */
    public static final class Snapshot {
        public static final Companion Companion = new Companion(null);
        public final HomeMediaHeader header;
        public final List<HomeMedia> medias;
        public final List<PreloadItem> preloadItems;

        /* compiled from: HomePageStartupHelper2.kt */
        /* loaded from: classes.dex */
        public static final class Companion {
            public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
                this();
            }

            /* JADX WARN: Removed duplicated region for block: B:65:0x001f A[Catch: all -> 0x0019, TRY_LEAVE, TryCatch #1 {all -> 0x0119, blocks: (B:54:0x0002, B:66:0x0031, B:90:0x00f3, B:57:0x0010, B:65:0x001f, B:68:0x0043, B:69:0x0062, B:71:0x0068, B:73:0x0072, B:75:0x0087, B:81:0x0093, B:82:0x00a7, B:86:0x00cf, B:88:0x00d7, B:83:0x00ac, B:85:0x00b0, B:89:0x00e1), top: B:99:0x0002 }] */
            /* JADX WARN: Removed duplicated region for block: B:68:0x0043 A[Catch: all -> 0x0019, TRY_ENTER, TryCatch #1 {all -> 0x0119, blocks: (B:54:0x0002, B:66:0x0031, B:90:0x00f3, B:57:0x0010, B:65:0x001f, B:68:0x0043, B:69:0x0062, B:71:0x0068, B:73:0x0072, B:75:0x0087, B:81:0x0093, B:82:0x00a7, B:86:0x00cf, B:88:0x00d7, B:83:0x00ac, B:85:0x00b0, B:89:0x00e1), top: B:99:0x0002 }] */
            /* JADX WARN: Removed duplicated region for block: B:81:0x0093 A[Catch: all -> 0x0019, TryCatch #1 {all -> 0x0119, blocks: (B:54:0x0002, B:66:0x0031, B:90:0x00f3, B:57:0x0010, B:65:0x001f, B:68:0x0043, B:69:0x0062, B:71:0x0068, B:73:0x0072, B:75:0x0087, B:81:0x0093, B:82:0x00a7, B:86:0x00cf, B:88:0x00d7, B:83:0x00ac, B:85:0x00b0, B:89:0x00e1), top: B:99:0x0002 }] */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public final com.miui.gallery.activity.HomePageStartupHelper2.Snapshot build(java.util.List<? extends com.miui.gallery.provider.cache.IRecord> r23) {
                /*
                    Method dump skipped, instructions count: 286
                    To view this dump change 'Code comments level' option to 'DEBUG'
                */
                throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.activity.HomePageStartupHelper2.Snapshot.Companion.build(java.util.List):com.miui.gallery.activity.HomePageStartupHelper2$Snapshot");
            }

            public Companion() {
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        public Snapshot(List<? extends HomeMedia> medias, HomeMediaHeader header, List<PreloadItem> preloadItems) {
            Intrinsics.checkNotNullParameter(medias, "medias");
            Intrinsics.checkNotNullParameter(header, "header");
            Intrinsics.checkNotNullParameter(preloadItems, "preloadItems");
            this.medias = medias;
            this.header = header;
            this.preloadItems = preloadItems;
        }

        public final List<HomeMedia> getMedias() {
            return this.medias;
        }

        public final HomeMediaHeader getHeader() {
            return this.header;
        }

        public final List<PreloadItem> getPreloadItems() {
            return this.preloadItems;
        }
    }

    public final void attach(Attacher attacher) {
        Intrinsics.checkNotNullParameter(attacher, "attacher");
        try {
            Trace.beginSection("starter#attach");
            this.attacher = attacher;
            this.prefetcher = new AsyncViewPrefetcher(this.context, new FrameLayout(this.context), HomePageStartupHelper2$attach$1$2.INSTANCE) { // from class: com.miui.gallery.activity.HomePageStartupHelper2$attach$1$1
                @Override // com.miui.gallery.ui.ViewProvider
                public int getViewResId(int i) {
                    if (i != 1) {
                        if (i == 2) {
                            return R.layout.normal_time_line_grid_header_item;
                        }
                        return 0;
                    }
                    return R.layout.base_image_grid_item;
                }
            };
            AsyncViewPrefetcher asyncViewPrefetcher = this.prefetcher;
            Intrinsics.checkNotNull(asyncViewPrefetcher);
            asyncViewPrefetcher.setLayoutFactory(GalleryViewCreator.getViewFactory());
            this.prefetchRunnable = new Runnable() { // from class: com.miui.gallery.activity.HomePageStartupHelper2$attach$1$3
                @Override // java.lang.Runnable
                public final void run() {
                    AsyncViewPrefetcher access$getPrefetcher$p = HomePageStartupHelper2.access$getPrefetcher$p(HomePageStartupHelper2.this);
                    if (access$getPrefetcher$p == null) {
                        return;
                    }
                    access$getPrefetcher$p.prefetch();
                }
            };
            CompatHandler workHandler = ThreadManager.Companion.getWorkHandler();
            Runnable runnable = this.prefetchRunnable;
            if (runnable == null) {
                Intrinsics.throwUninitializedPropertyAccessException("prefetchRunnable");
                runnable = null;
            }
            workHandler.post(runnable);
        } finally {
            Trace.endSection();
        }
    }

    public HomePageStartupHelper2(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.context = context;
    }

    public static final /* synthetic */ AsyncViewPrefetcher access$getPrefetcher$p(HomePageStartupHelper2 homePageStartupHelper2) {
        return homePageStartupHelper2.prefetcher;
    }

    public final void onActivityCreate() {
        this.dataLoadFuture = loadHomePageData();
    }

    public final Future<Unit> loadHomePageData() {
        Future<Unit> submit = ThreadManager.Companion.getPreviewPool().submit(new ThreadPool.Job() { // from class: com.miui.gallery.activity.HomePageStartupHelper2$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public final Object mo1807run(ThreadPool.JobContext jobContext) {
                return HomePageStartupHelper2.$r8$lambda$PmKqAfmGhXd4_X_CzMmQIm5twvQ(HomePageStartupHelper2.this, jobContext);
            }
        });
        Intrinsics.checkNotNullExpressionValue(submit, "previewPool.submit {\n   …)\n            }\n        }");
        return submit;
    }

    /* renamed from: loadHomePageData$lambda-6 */
    public static final Unit m461loadHomePageData$lambda6(HomePageStartupHelper2 this$0, ThreadPool.JobContext jobContext) {
        List<IRecord> loadHomePageDatas;
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        Context context = this$0.context;
        boolean z = (context instanceof CrossUserCompatActivity) && ((CrossUserCompatActivity) context).isCrossUserPick();
        long currentTimeMillis = System.currentTimeMillis();
        Trace.beginSection("starter#loadSnapshot");
        Cluster cluster = null;
        try {
            try {
                PictureViewMode homePageViewMode = Companion.getHomePageViewMode();
                int clusterKey = homePageViewMode.getClusterKey();
                if (this$0.homePageList == null && !z) {
                    Object remove = GalleryMemoryCacheHelper.getInstance().remove("first_home_page_datas");
                    if ((remove instanceof Pair) && Intrinsics.areEqual(((Pair) remove).getFirst(), Integer.valueOf(clusterKey)) && (((Pair) remove).getSecond() instanceof List)) {
                        Object second = ((Pair) remove).getSecond();
                        if (second == null) {
                            throw new NullPointerException("null cannot be cast to non-null type kotlin.collections.List<com.miui.gallery.provider.cache.IRecord>");
                        }
                        this$0.homePageList = (List) second;
                        this$0.isDataInitialized = true;
                        ThreadManager.Companion.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.activity.HomePageStartupHelper2$$ExternalSyntheticLambda2
                            @Override // java.lang.Runnable
                            public final void run() {
                                HomePageStartupHelper2.$r8$lambda$VlQdnStAWpox6sjXj1V2Xkgivjw(HomePageStartupHelper2.this);
                            }
                        });
                        return Unit.INSTANCE;
                    } else if (!z && (loadHomePageDatas = this$0.loadHomePageDatas(homePageViewMode)) != null) {
                        this$0.homePageList = loadHomePageDatas;
                    }
                }
                boolean z2 = this$0.homePageList != null;
                if (this$0.homePageList == null) {
                    CacheLiveData cacheLiveData = new CacheLiveData(this$0.context, HomeMediaLoader.URI, null, HomeMediaLoader.getHomePageSelection(GalleryPreferences.HomePage.isHomePageShowAllPhotos()), null, "alias_sort_time DESC ", null, new MediaProcessor(true), 84, null);
                    try {
                        List<? extends IRecord> mo1205compute = cacheLiveData.mo1205compute();
                        CloseableKt.closeFinally(cacheLiveData, null);
                        this$0.homePageList = mo1205compute;
                        List<? extends IRecord> list = this$0.homePageList;
                        if (list instanceof ClusteredList) {
                            ((ClusteredList) list).select(clusterKey, false);
                        }
                    } finally {
                    }
                }
                if (this$0.homePageList != null && (this$0.homePageList instanceof ClusteredList)) {
                    List<? extends IRecord> list2 = this$0.homePageList;
                    if (list2 == null) {
                        throw new NullPointerException("null cannot be cast to non-null type com.miui.gallery.data.ClusteredList<*>");
                    }
                    cluster = ((ClusteredList) list2).clusterOf(clusterKey);
                }
                currentTimeMillis = System.currentTimeMillis() - currentTimeMillis;
                DefaultLogger.d("HomePageStartupHelper2", "load preview cost %d, from snap %s", Long.valueOf(currentTimeMillis), Boolean.valueOf(z2));
            } catch (Exception e) {
                DefaultLogger.e("HomePageStartupHelper2", "error when load data in home page", e);
            }
            Trace.endSection();
            if (this$0.isDestroyed) {
                return Unit.INSTANCE;
            }
            try {
                Trace.beginSection("starter#preloadImages");
                List<ImageDisplayItem> parsePreloadItems$app_cnRelease = this$0.parsePreloadItems$app_cnRelease(this$0.homePageList, cluster);
                ThreadManager.Companion.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.activity.HomePageStartupHelper2$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        HomePageStartupHelper2.m460$r8$lambda$UmyHBM2m7ylOpuG_FycNzR3N0(HomePageStartupHelper2.this);
                    }
                });
                this$0.isDataInitialized = true;
                this$0.preloadHomePageImages(parsePreloadItems$app_cnRelease);
                if (!parsePreloadItems$app_cnRelease.isEmpty()) {
                    this$0.statSnapshotLoadDuration(currentTimeMillis);
                }
                Trace.endSection();
                return Unit.INSTANCE;
            } finally {
            }
        } finally {
        }
    }

    /* renamed from: loadHomePageData$lambda-6$lambda-2 */
    public static final void m462loadHomePageData$lambda6$lambda2(HomePageStartupHelper2 this$0) {
        Function1<? super List<? extends IRecord>, Unit> function1;
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        if (this$0.isDestroyed || (function1 = this$0.preloadListener) == null) {
            return;
        }
        function1.mo2577invoke(this$0.homePageList);
        this$0.homePageList = null;
    }

    /* renamed from: loadHomePageData$lambda-6$lambda-5 */
    public static final void m463loadHomePageData$lambda6$lambda5(HomePageStartupHelper2 this$0) {
        Function1<? super List<? extends IRecord>, Unit> function1;
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        if (this$0.isDestroyed || (function1 = this$0.preloadListener) == null) {
            return;
        }
        function1.mo2577invoke(this$0.homePageList);
        this$0.homePageList = null;
    }

    /* JADX WARN: Removed duplicated region for block: B:80:0x0029 A[Catch: all -> 0x016b, Exception -> 0x016d, TryCatch #0 {Exception -> 0x016d, blocks: (B:72:0x000b, B:74:0x001d, B:80:0x0029, B:82:0x003f, B:88:0x004b, B:95:0x005c, B:97:0x0066, B:107:0x00b1, B:114:0x00e4, B:116:0x00ea, B:118:0x013f, B:120:0x0148, B:110:0x00da, B:99:0x0070, B:103:0x0092, B:105:0x00a8, B:102:0x008a, B:91:0x0053), top: B:131:0x000b, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:88:0x004b A[Catch: all -> 0x016b, Exception -> 0x016d, TryCatch #0 {Exception -> 0x016d, blocks: (B:72:0x000b, B:74:0x001d, B:80:0x0029, B:82:0x003f, B:88:0x004b, B:95:0x005c, B:97:0x0066, B:107:0x00b1, B:114:0x00e4, B:116:0x00ea, B:118:0x013f, B:120:0x0148, B:110:0x00da, B:99:0x0070, B:103:0x0092, B:105:0x00a8, B:102:0x008a, B:91:0x0053), top: B:131:0x000b, outer: #1 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.util.List<com.miui.gallery.provider.cache.IRecord> loadHomePageDatas(com.miui.gallery.ui.pictures.PictureViewMode r20) {
        /*
            Method dump skipped, instructions count: 378
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.activity.HomePageStartupHelper2.loadHomePageDatas(com.miui.gallery.ui.pictures.PictureViewMode):java.util.List");
    }

    public final ViewProvider getViewProvider() {
        return this.prefetcher;
    }

    public final void statSnapshotLoadDuration(long j) {
        SamplingStatHelper.recordCountEvent("home", "snapshot_load_duration", MapsKt__MapsJVMKt.mapOf(TuplesKt.to("cost", String.valueOf(j))));
    }

    public final int getPreloadImageCount(Cluster cluster) {
        int i = 0;
        if (cluster == null) {
            return 0;
        }
        int i2 = Config$ThumbConfig.get().sMicroThumbColumnsPortrait;
        int i3 = Config$ThumbConfig.get().sPredictItemsOneScreen;
        int groupCount = cluster.getGroupCount(true);
        int i4 = 0;
        int i5 = 0;
        while (i < groupCount) {
            int i6 = i + 1;
            int childCount = cluster.getChildCount(i, true);
            i4 += childCount;
            if (i4 + i5 > i3) {
                break;
            }
            int i7 = childCount % i2;
            if (i7 != 0) {
                i5 += i2 - i7;
            }
            i = i6;
        }
        if (i5 + i4 >= i3) {
            i4 = i3 - i5;
        }
        DefaultLogger.fd("HomePageStartupHelper2", "preload image count %d", Integer.valueOf(i4));
        return i4;
    }

    public final void preloadHomePageImages(List<ImageDisplayItem> list) {
        if (this.isDestroyed || list.isEmpty()) {
            return;
        }
        boolean isLowRamDevice = BaseBuildUtil.isLowRamDevice();
        RequestManager with = Glide.with(this.context);
        Intrinsics.checkNotNullExpressionValue(with, "with(context)");
        PreloadTracker preloadTracker = new PreloadTracker(list.size());
        for (ImageDisplayItem imageDisplayItem : list) {
            if (this.isDestroyed) {
                DefaultLogger.w("HomePageStartupHelper2", "preload home page images break");
                return;
            } else if (imageDisplayItem.getThumbBlob() != null) {
                with.mo985asBitmap().mo962load(GalleryModel.of(imageDisplayItem.getFilePath(), imageDisplayItem.getThumbBlob())).mo946apply((BaseRequestOptions<?>) GlideOptions.pixelsThumbOf(imageDisplayItem.getFileLength())).mo974priority(Priority.HIGH).mo950diskCacheStrategy(DiskCacheStrategy.NONE).mo945addListener(preloadTracker).preload();
            } else {
                RequestBuilder<Bitmap> mo962load = with.mo985asBitmap().mo962load(GalleryModel.of(imageDisplayItem.getFilePath()));
                long fileLength = imageDisplayItem.getFileLength();
                mo962load.mo946apply((BaseRequestOptions<?>) (isLowRamDevice ? GlideOptions.microThumbOf(fileLength) : GlideOptions.tinyThumbOf(fileLength))).mo974priority(Priority.HIGH).mo945addListener(preloadTracker).preload();
            }
        }
    }

    public final void onStartup() {
        Attacher attacher = this.attacher;
        if (attacher == null) {
            return;
        }
        attacher.onStartup();
    }

    public final void onActivityDestroy() {
        this.isDestroyed = true;
        Runnable runnable = null;
        if (this.attacher != null) {
            this.attacher = null;
        }
        Future<Unit> future = this.dataLoadFuture;
        if (future != null) {
            Intrinsics.checkNotNull(future);
            future.cancel();
            this.dataLoadFuture = null;
        }
        if (this.prefetcher != null) {
            ThreadManager.Companion companion = ThreadManager.Companion;
            CompatHandler workHandler = companion.getWorkHandler();
            Runnable runnable2 = this.prefetchRunnable;
            if (runnable2 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("prefetchRunnable");
            } else {
                runnable = runnable2;
            }
            workHandler.removeCallbacks(runnable);
            companion.getWorkHandler().post(new Runnable() { // from class: com.miui.gallery.activity.HomePageStartupHelper2$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public final void run() {
                    HomePageStartupHelper2.$r8$lambda$ycMwRhiu7R6El3tFv0bKLhHOe7o(HomePageStartupHelper2.this);
                }
            });
        }
    }

    /* renamed from: onActivityDestroy$lambda-10 */
    public static final void m464onActivityDestroy$lambda10(HomePageStartupHelper2 this$0) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        AsyncViewPrefetcher asyncViewPrefetcher = this$0.prefetcher;
        if (asyncViewPrefetcher == null) {
            return;
        }
        asyncViewPrefetcher.release();
    }

    public final void setDataLoaderListener(Function1<? super List<? extends IRecord>, Unit> function1) {
        this.preloadListener = function1;
        if (function1 != null) {
            if (this.isDataInitialized) {
                function1.mo2577invoke(this.homePageList);
                this.homePageList = null;
                return;
            }
            List<IRecord> checkIsContainsPreLoadCursor = checkIsContainsPreLoadCursor();
            if (checkIsContainsPreLoadCursor != null) {
                function1.mo2577invoke(checkIsContainsPreLoadCursor);
            } else {
                DefaultLogger.w("HomePageStartupHelper2", "preload home page cursor failed");
            }
        }
    }

    public final List<IRecord> checkIsContainsPreLoadCursor() {
        Object remove = GalleryMemoryCacheHelper.getInstance().remove("first_home_page_datas");
        if (remove instanceof Pair) {
            Pair pair = (Pair) remove;
            if (!(pair.getSecond() instanceof List)) {
                return null;
            }
            this.isDataInitialized = true;
            Object second = pair.getSecond();
            Objects.requireNonNull(second, "null cannot be cast to non-null type kotlin.collections.List<com.miui.gallery.provider.cache.IRecord>");
            return (List) second;
        }
        return null;
    }

    public final List<ImageDisplayItem> parsePreloadItems$app_cnRelease(List<? extends IRecord> list, Cluster cluster) {
        LinkedList linkedList = new LinkedList();
        int preloadImageCount = getPreloadImageCount(cluster);
        int size = list == null ? 0 : list.size();
        int i = 0;
        int i2 = 0;
        while (i < size && i2 < preloadImageCount) {
            Intrinsics.checkNotNull(list);
            int i3 = i + 1;
            IRecord iRecord = list.get(i);
            if (iRecord instanceof IMediaSnapshot) {
                i2++;
                IMediaSnapshot iMediaSnapshot = (IMediaSnapshot) iRecord;
                String defaultThumbFilePath = HomePageAdapter2.Companion.getDefaultThumbFilePath(iMediaSnapshot);
                if (!(defaultThumbFilePath == null || defaultThumbFilePath.length() == 0)) {
                    linkedList.add(new ImageDisplayItem(defaultThumbFilePath, iMediaSnapshot.getSize(), iMediaSnapshot.getThumbBlob()));
                }
            }
            i = i3;
        }
        return linkedList;
    }

    /* compiled from: HomePageStartupHelper2.kt */
    /* loaded from: classes.dex */
    public static final class ImageDisplayItem {
        public final long fileLength;
        public final String filePath;
        public final byte[] thumbBlob;

        public ImageDisplayItem(String filePath, long j, byte[] bArr) {
            Intrinsics.checkNotNullParameter(filePath, "filePath");
            this.filePath = filePath;
            this.fileLength = j;
            this.thumbBlob = bArr;
        }

        public final String getFilePath() {
            return this.filePath;
        }

        public final long getFileLength() {
            return this.fileLength;
        }

        public final byte[] getThumbBlob() {
            return this.thumbBlob;
        }
    }

    /* compiled from: HomePageStartupHelper2.kt */
    /* loaded from: classes.dex */
    public static final class PreloadItem {
        public long fileLength;
        public long id;
        public String path;

        public PreloadItem(String path, long j, long j2) {
            Intrinsics.checkNotNullParameter(path, "path");
            this.path = path;
            this.id = j;
            this.fileLength = j2;
        }

        public final long getFileLength() {
            return this.fileLength;
        }

        public final long getId() {
            return this.id;
        }

        public final String getPath() {
            return this.path;
        }
    }

    /* compiled from: HomePageStartupHelper2.kt */
    /* loaded from: classes.dex */
    public static final class SnapshotFunction implements Function<Snapshot, Boolean> {
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r1v0, types: [java.lang.String] */
        /* JADX WARN: Type inference failed for: r1v4, types: [boolean] */
        @Override // io.reactivex.functions.Function
        /* renamed from: apply */
        public Boolean mo2564apply(Snapshot snapshot) {
            boolean z;
            String str = d.H;
            String str2 = "save snap cost ";
            Intrinsics.checkNotNullParameter(snapshot, "snapshot");
            long currentTimeMillis = System.currentTimeMillis();
            List<HomeMedia> medias = snapshot.getMedias();
            LinkedList linkedList = new LinkedList();
            for (HomeMedia homeMedia : medias) {
                linkedList.add(Long.valueOf(homeMedia.getMediaId()));
            }
            List subList = linkedList.subList(0, RangesKt___RangesKt.coerceAtMost(linkedList.size(), 128));
            Intrinsics.checkNotNullExpressionValue(subList, "newIds.subList(0, newIds…diaManager.MINIMUM_PART))");
            GalleryPreferences.HomePage.setHomePageImageIds(CollectionsKt___CollectionsKt.joinToString$default(subList, ",", null, null, 0, null, null, 62, null));
            GalleryLiteEntityManager galleryLiteEntityManager = GalleryLiteEntityManager.getInstance();
            List<HomeMedia> oldMedias = galleryLiteEntityManager.query(HomeMedia.class, "media_id IN (" + ((Object) Joiner.on((char) CoreConstants.COMMA_CHAR).join(linkedList)) + ") AND thumbnail_blob NOT NULL", null);
            for (HomeMedia homeMedia2 : oldMedias) {
                Iterator<HomeMedia> it = snapshot.getMedias().iterator();
                while (true) {
                    if (it.hasNext()) {
                        HomeMedia next = it.next();
                        if (next.getMediaId() == homeMedia2.getMediaId() && TextUtils.equals(next.getSha1(), homeMedia2.getSha1())) {
                            next.setThumbnailBlob(homeMedia2.getThumbnailBlob());
                            break;
                        }
                    }
                }
            }
            EntityTransaction transaction = GalleryLiteEntityManager.getInstance().getTransaction();
            transaction.begin();
            try {
                try {
                    GalleryLiteEntityManager.getInstance().deleteAll(HomeMedia.class);
                    GalleryLiteEntityManager.getInstance().deleteAll(HomeMediaHeader.class);
                    GalleryLiteEntityManager.getInstance().insert(snapshot.getMedias());
                    GalleryLiteEntityManager.getInstance().insert(snapshot.getHeader());
                    transaction.commit();
                    transaction.end();
                    DefaultLogger.d("HomePageStartupHelper2", str2 + (System.currentTimeMillis() - currentTimeMillis) + ((String) str));
                    z = true;
                } catch (Exception e) {
                    DefaultLogger.e("HomePageStartupHelper2", "save snapshot error", e);
                    transaction.end();
                    DefaultLogger.d("HomePageStartupHelper2", str2 + (System.currentTimeMillis() - currentTimeMillis) + ((String) str));
                    z = false;
                }
                AlbumConfigSharedPreferences albumConfigSharedPreferences = AlbumConfigSharedPreferences.getInstance();
                str2 = GalleryPreferences.PrefKeys.IS_NEED_REFRESH_HOME_PAGE_THUNMNAIL_DATA;
                str = albumConfigSharedPreferences.getBoolean(str2, true);
                if (z) {
                    Intrinsics.checkNotNullExpressionValue(oldMedias, "oldMedias");
                    HashSet hashSet = new HashSet();
                    for (HomeMedia homeMedia3 : oldMedias) {
                        hashSet.add(Long.valueOf(homeMedia3.getMediaId()));
                    }
                    Context context = GalleryApp.sGetAndroidContext();
                    RequestManager with = Glide.with(context);
                    Intrinsics.checkNotNullExpressionValue(with, "with(context)");
                    for (PreloadItem preloadItem : snapshot.getPreloadItems()) {
                        if (!hashSet.contains(Long.valueOf(preloadItem.getId())) || str != 0) {
                            DefaultLogger.v("HomePageStartupHelper2", Intrinsics.stringPlus("Preload snapshot thumbnail for: ", preloadItem.getPath()));
                            Intrinsics.checkNotNullExpressionValue(context, "context");
                            with.mo985asBitmap().mo962load(GalleryModel.of(preloadItem.getPath())).mo946apply((BaseRequestOptions<?>) GlideOptions.pixelsThumbOf(preloadItem.getFileLength())).mo974priority(Priority.LOW).into((RequestBuilder) new HomeMediaPreloadTarget(context, preloadItem.getId()));
                        }
                    }
                    if (str != 0) {
                        DefaultLogger.v("HomePageStartupHelper2", "force update homePage snapshot thumbnailBlob data");
                        AlbumConfigSharedPreferences.getInstance().putBoolean(str2, false);
                    }
                }
                return Boolean.valueOf(z);
            } catch (Throwable th) {
                transaction.end();
                DefaultLogger.d("HomePageStartupHelper2", str2 + (System.currentTimeMillis() - currentTimeMillis) + str);
                throw th;
            }
        }
    }

    /* compiled from: HomePageStartupHelper2.kt */
    /* loaded from: classes.dex */
    public static final class SnapshotUpdate implements Function<CacheLiveData<MediaCacheItem, IRecord>, Boolean> {
        @Override // io.reactivex.functions.Function
        /* renamed from: apply */
        public Boolean mo2564apply(CacheLiveData<MediaCacheItem, IRecord> liveData) {
            Intrinsics.checkNotNullParameter(liveData, "liveData");
            try {
                if (MiscUtil.isAppProcessInForeground()) {
                    DefaultLogger.w("HomePageStartupHelper2", "app has focused");
                    Boolean bool = Boolean.FALSE;
                    CloseableKt.closeFinally(liveData, null);
                    return bool;
                }
                long currentTimeMillis = System.currentTimeMillis();
                List<IRecord> mo1205compute = liveData.mo1205compute();
                DefaultLogger.d("HomePageStartupHelper2", Intrinsics.stringPlus("SnapshotUpdate: load medias cost ", Long.valueOf(System.currentTimeMillis() - currentTimeMillis)));
                long currentTimeMillis2 = System.currentTimeMillis();
                int clusterKey = HomePageStartupHelper2.Companion.getHomePageViewMode().getClusterKey();
                if (mo1205compute instanceof ClusteredList) {
                    ((ClusteredList) mo1205compute).select(clusterKey, false);
                }
                Boolean mo2564apply = new SnapshotFunction().mo2564apply(Snapshot.Companion.build(mo1205compute));
                DefaultLogger.d("HomePageStartupHelper2", Intrinsics.stringPlus("SnapshotUpdate: save snap cost ", Long.valueOf(System.currentTimeMillis() - currentTimeMillis2)));
                CloseableKt.closeFinally(liveData, null);
                return mo2564apply;
            } catch (Throwable th) {
                try {
                    throw th;
                } catch (Throwable th2) {
                    CloseableKt.closeFinally(liveData, th);
                    throw th2;
                }
            }
        }
    }

    /* compiled from: HomePageStartupHelper2.kt */
    /* loaded from: classes.dex */
    public static final class PreloadTracker implements RequestListener<Bitmap> {
        public int failed;
        public long startMs = System.currentTimeMillis();
        public int successed;
        public final int total;

        public PreloadTracker(int i) {
            this.total = i;
        }

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onLoadFailed(GlideException glideException, Object obj, Target<Bitmap> target, boolean z) {
            arrive(true);
            return false;
        }

        @Override // com.bumptech.glide.request.RequestListener
        public boolean onResourceReady(Bitmap bitmap, Object obj, Target<Bitmap> target, DataSource dataSource, boolean z) {
            arrive(false);
            return false;
        }

        public final void arrive(boolean z) {
            if (z) {
                this.failed++;
            } else {
                this.successed++;
            }
            int i = this.successed;
            int i2 = this.failed;
            if (i + i2 == this.total) {
                DefaultLogger.fi("HomePageStartupHelper2", "Image preloading finish, %d of %d failed, costs %dms", Integer.valueOf(i2), Integer.valueOf(this.total), Long.valueOf(System.currentTimeMillis() - this.startMs));
            }
        }
    }

    /* compiled from: HomePageStartupHelper2.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public static /* synthetic */ void $r8$lambda$AVKzD3FxqWs7fC_8KsniwyxXMH0(Context context) {
            m467updateHomeSnapshot$lambda3(context);
        }

        public static /* synthetic */ void $r8$lambda$jRCAtUGLda6jsB3AsM6uc7LI66k(Context context) {
            m466startPreloadHomePageDatasTask$lambda4(context);
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        public static final /* synthetic */ void access$updateHomeSnapshot(Companion companion, Context context) {
            companion.updateHomeSnapshot(context);
        }

        public final PublishSubject<CacheLiveData<MediaCacheItem, IRecord>> snapshotUpdater() {
            if (HomePageStartupHelper2.sSnapshotUpdater == null) {
                synchronized (HomePageStartupHelper2.class) {
                    if (HomePageStartupHelper2.sSnapshotUpdater == null) {
                        Companion companion = HomePageStartupHelper2.Companion;
                        PublishSubject create = PublishSubject.create();
                        create.observeOn(GallerySchedulers.misc()).throttleLatest(3000L, TimeUnit.MILLISECONDS, GallerySchedulers.misc(), true).map(new SnapshotUpdate()).subscribe(Functions.emptyConsumer(), HomePageStartupHelper2$Companion$$ExternalSyntheticLambda0.INSTANCE);
                        HomePageStartupHelper2.sSnapshotUpdater = create;
                    }
                    Unit unit = Unit.INSTANCE;
                }
            }
            return HomePageStartupHelper2.sSnapshotUpdater;
        }

        public final void updateHomeSnapshot(final Context context) {
            ThreadManager.Companion.getWorkHandler().post(new Runnable() { // from class: com.miui.gallery.activity.HomePageStartupHelper2$Companion$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    HomePageStartupHelper2.Companion.$r8$lambda$AVKzD3FxqWs7fC_8KsniwyxXMH0(context);
                }
            });
        }

        /* renamed from: updateHomeSnapshot$lambda-3 */
        public static final void m467updateHomeSnapshot$lambda3(Context context) {
            Intrinsics.checkNotNullParameter(context, "$context");
            boolean isHomePageShowAllPhotos = GalleryPreferences.HomePage.isHomePageShowAllPhotos();
            PublishSubject<CacheLiveData<MediaCacheItem, IRecord>> snapshotUpdater = HomePageStartupHelper2.Companion.snapshotUpdater();
            if (snapshotUpdater == null) {
                return;
            }
            Context applicationContext = context.getApplicationContext();
            Intrinsics.checkNotNullExpressionValue(applicationContext, "context.applicationContext");
            snapshotUpdater.onNext(new CacheLiveData<>(applicationContext, UriUtil.appendLimit(HomeMediaLoader.URI, Config$ThumbConfig.get().sPredictItemsOneScreen), null, HomeMediaLoader.getHomePageSelection(isHomePageShowAllPhotos), null, "alias_sort_time DESC ", null, new MediaProcessor(true), 84, null));
        }

        public final void registerSnapshotUpdateListener(Context context) {
            Intrinsics.checkNotNullParameter(context, "context");
            LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() { // from class: com.miui.gallery.activity.HomePageStartupHelper2$Companion$registerSnapshotUpdateListener$1
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context2, Intent intent) {
                    InsertedValue insertedValue;
                    Intrinsics.checkNotNullParameter(context2, "context");
                    Intrinsics.checkNotNullParameter(intent, "intent");
                    if (MiscUtil.isAppProcessInForeground() || (insertedValue = (InsertedValue) intent.getParcelableExtra("gallery.scanner.PARAMS")) == null || (insertedValue.mAlbumAttributes & 4) == 0) {
                        return;
                    }
                    HomePageStartupHelper2.Companion.access$updateHomeSnapshot(HomePageStartupHelper2.Companion, context2);
                }
            }, new IntentFilter("gallery.scanner.INSERTED"));
        }

        public final void startPreloadHomePageDatasTask(final Context base) {
            Intrinsics.checkNotNullParameter(base, "base");
            ThreadManager.Companion.getPreviewPool().asExecutorService().execute(new Runnable() { // from class: com.miui.gallery.activity.HomePageStartupHelper2$Companion$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    HomePageStartupHelper2.Companion.$r8$lambda$jRCAtUGLda6jsB3AsM6uc7LI66k(base);
                }
            });
        }

        /* JADX WARN: Removed duplicated region for block: B:39:0x002d A[Catch: all -> 0x006e, Exception -> 0x0070, TryCatch #0 {Exception -> 0x0070, blocks: (B:31:0x0007, B:33:0x0021, B:39:0x002d, B:41:0x0043, B:42:0x004a, B:43:0x0068), top: B:53:0x0007, outer: #1 }] */
        /* renamed from: startPreloadHomePageDatasTask$lambda-4 */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public static final void m466startPreloadHomePageDatasTask$lambda4(android.content.Context r6) {
            /*
                java.lang.String r0 = "HomePageStartupHelper2"
                java.lang.String r1 = "$base"
                kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r6, r1)
                java.lang.String r1 = "InitHomePageDatasTask"
                androidx.tracing.Trace.beginSection(r1)     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L70
                com.miui.gallery.activity.HomePageStartupHelper2 r1 = new com.miui.gallery.activity.HomePageStartupHelper2     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L70
                r1.<init>(r6)     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L70
                com.miui.gallery.activity.HomePageStartupHelper2$Companion r6 = com.miui.gallery.activity.HomePageStartupHelper2.Companion     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L70
                com.miui.gallery.ui.pictures.PictureViewMode r6 = r6.getHomePageViewMode()     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L70
                int r2 = r6.getClusterKey()     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L70
                java.util.List r6 = r1.loadHomePageDatas(r6)     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L70
                if (r6 == 0) goto L2a
                boolean r3 = r6.isEmpty()     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L70
                if (r3 == 0) goto L28
                goto L2a
            L28:
                r3 = 0
                goto L2b
            L2a:
                r3 = 1
            L2b:
                if (r3 != 0) goto L68
                com.miui.gallery.util.GalleryMemoryCacheHelper r3 = com.miui.gallery.util.GalleryMemoryCacheHelper.getInstance()     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L70
                java.lang.String r4 = "first_home_page_datas"
                java.lang.Integer r5 = java.lang.Integer.valueOf(r2)     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L70
                kotlin.Pair r5 = kotlin.TuplesKt.to(r5, r6)     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L70
                r3.save(r4, r5)     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L70
                r3 = 0
                boolean r4 = r6 instanceof com.miui.gallery.data.ClusteredList     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L70
                if (r4 == 0) goto L4a
                r3 = r6
                com.miui.gallery.data.ClusteredList r3 = (com.miui.gallery.data.ClusteredList) r3     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L70
                com.miui.gallery.data.Cluster r3 = r3.clusterOf(r2)     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L70
            L4a:
                java.util.List r6 = r1.parsePreloadItems$app_cnRelease(r6, r3)     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L70
                java.lang.String r2 = "preload items size:[%s],count:[%s]"
                int r3 = r6.size()     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L70
                java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L70
                com.miui.gallery.Config$ThumbConfig r4 = com.miui.gallery.Config$ThumbConfig.get()     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L70
                int r4 = r4.sPredictItemsOneScreen     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L70
                java.lang.Integer r4 = java.lang.Integer.valueOf(r4)     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L70
                com.miui.gallery.util.logger.DefaultLogger.fd(r0, r2, r3, r4)     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L70
                com.miui.gallery.activity.HomePageStartupHelper2.access$preloadHomePageImages(r1, r6)     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L70
            L68:
                java.lang.String r6 = "preload thread create bitmap finished"
                com.miui.gallery.util.logger.DefaultLogger.d(r0, r6)     // Catch: java.lang.Throwable -> L6e java.lang.Exception -> L70
                goto L7f
            L6e:
                r6 = move-exception
                goto L83
            L70:
                r6 = move-exception
                java.lang.String r1 = "pre create bitmap error"
                com.miui.gallery.util.logger.DefaultLogger.e(r0, r1, r6)     // Catch: java.lang.Throwable -> L6e
                com.miui.gallery.ui.album.main.utils.AlbumConfigSharedPreferences r6 = com.miui.gallery.ui.album.main.utils.AlbumConfigSharedPreferences.getInstance()     // Catch: java.lang.Throwable -> L6e
                java.lang.String r0 = "is_need_refresh_home_page_data1"
                r6.removeKey(r0)     // Catch: java.lang.Throwable -> L6e
            L7f:
                androidx.tracing.Trace.endSection()
                return
            L83:
                androidx.tracing.Trace.endSection()
                throw r6
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.activity.HomePageStartupHelper2.Companion.m466startPreloadHomePageDatasTask$lambda4(android.content.Context):void");
        }

        public final PictureViewMode getHomePageViewMode() {
            if (PictureViewMode.isLargeDevice()) {
                PictureViewMode viewModeByOrdinal = PictureViewMode.getViewModeByOrdinal(GalleryPreferences.HomePage.getHomePageViewMode());
                Intrinsics.checkNotNullExpressionValue(viewModeByOrdinal, "{\n                Pictur…ViewMode())\n            }");
                return viewModeByOrdinal;
            }
            PictureViewMode pictureViewMode = PictureViewMode.MICRO_THUMB;
            Intrinsics.checkNotNullExpressionValue(pictureViewMode, "{\n                Pictur…MICRO_THUMB\n            }");
            return pictureViewMode;
        }
    }
}
