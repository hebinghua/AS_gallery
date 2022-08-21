package com.miui.gallery.activity;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.FrameLayout;
import androidx.tracing.Trace;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.google.common.collect.Lists;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.R;
import com.miui.gallery.activity.HomePageStartupHelper;
import com.miui.gallery.adapter.HomePageAdapter;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.dao.GalleryLiteEntityManager;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.loader.HomeMediaLoader;
import com.miui.gallery.model.HomeMedia;
import com.miui.gallery.model.HomeMediaHeader;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.AsyncViewPrefetcher;
import com.miui.gallery.ui.album.main.utils.factory.GalleryViewCreator;
import com.miui.gallery.ui.pictures.cluster.TimelineClusterFactory;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.BurstFilterCursor;
import com.miui.gallery.util.GalleryDateUtils;
import com.miui.gallery.util.GalleryMemoryCacheHelper;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.security.CrossUserCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes.dex */
public class HomePageStartupHelper {
    public static String TAG = "HomePageStartupHelper";
    public final int MICRO_THUMB_COLUMN_NUMBER;
    public final int MICRO_THUMB_PRELOAD_MAX_COUNT;
    public Attacher mAttacher;
    public Context mContext;
    public final Object mCursorLock;
    public Future mDataLoadFuture;
    public DataLoadListener mDataLoadListener;
    public volatile Cursor mHomePageCursor;
    public volatile boolean mIsDataInitialized;
    public volatile boolean mIsDestroyed;
    public Runnable mPrefetchRunnable;
    public AsyncViewPrefetcher mPrefetcher;

    /* loaded from: classes.dex */
    public interface Attacher {
        HomePageStartupHelper getStartupHelper();
    }

    /* loaded from: classes.dex */
    public interface DataLoadListener {
        void onDataLoadFinish(Cursor cursor);
    }

    /* renamed from: $r8$lambda$k-3DyV1JzeTFPbvf82gNo2OzQmc */
    public static /* synthetic */ void m459$r8$lambda$k3DyV1JzeTFPbvf82gNo2OzQmc(HomePageStartupHelper homePageStartupHelper) {
        homePageStartupHelper.lambda$attach$0();
    }

    public HomePageStartupHelper(Context context) {
        int i = Config$ThumbConfig.get().sMicroThumbColumnsPortrait;
        this.MICRO_THUMB_COLUMN_NUMBER = i;
        this.MICRO_THUMB_PRELOAD_MAX_COUNT = i * 7;
        this.mCursorLock = new Object();
        this.mIsDataInitialized = false;
        this.mIsDestroyed = false;
        this.mContext = context;
    }

    public void attach(Attacher attacher) {
        Trace.beginSection("HomeAc#attach");
        this.mAttacher = attacher;
        AsyncViewPrefetcher asyncViewPrefetcher = new AsyncViewPrefetcher(this.mContext, new FrameLayout(this.mContext), Lists.newArrayList(new AsyncViewPrefetcher.PrefetchSpec(2, 1), new AsyncViewPrefetcher.PrefetchSpec(1, Config$ThumbConfig.get().sPredictItemsOneScreen), new AsyncViewPrefetcher.PrefetchSpec(2, Config$ThumbConfig.get().sPredictHeadersOneScreen - 1))) { // from class: com.miui.gallery.activity.HomePageStartupHelper.1
            @Override // com.miui.gallery.ui.ViewProvider
            public int getViewResId(int i) {
                if (i == 1) {
                    return R.layout.base_image_grid_item;
                }
                if (i != 2) {
                    return 0;
                }
                return R.layout.normal_time_line_grid_header_item;
            }

            {
                HomePageStartupHelper.this = this;
            }
        };
        this.mPrefetcher = asyncViewPrefetcher;
        asyncViewPrefetcher.setLayoutFactory(GalleryViewCreator.getViewFactory());
        this.mPrefetchRunnable = new Runnable() { // from class: com.miui.gallery.activity.HomePageStartupHelper$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                HomePageStartupHelper.m459$r8$lambda$k3DyV1JzeTFPbvf82gNo2OzQmc(HomePageStartupHelper.this);
            }
        };
        ThreadManager.getWorkHandler().post(this.mPrefetchRunnable);
        Trace.endSection();
    }

    public /* synthetic */ void lambda$attach$0() {
        this.mPrefetcher.prefetch();
    }

    public void onActivityCreate() {
        this.mDataLoadFuture = startLoadHomePageDatasTask();
    }

    /* renamed from: com.miui.gallery.activity.HomePageStartupHelper$2 */
    /* loaded from: classes.dex */
    public class AnonymousClass2 implements ThreadPool.Job<Void> {
        public static /* synthetic */ void $r8$lambda$EW2o7G9gEjCIXkAQE0xrlXymDW4(AnonymousClass2 anonymousClass2) {
            anonymousClass2.lambda$run$1();
        }

        public static /* synthetic */ void $r8$lambda$IDfkvK3ZB3nv4COl4I6xMuR5PHs(AnonymousClass2 anonymousClass2) {
            anonymousClass2.lambda$run$0();
        }

        public AnonymousClass2() {
            HomePageStartupHelper.this = r1;
        }

        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run */
        public Void mo1807run(ThreadPool.JobContext jobContext) {
            Cursor loadHomePageDatas;
            boolean z = false;
            boolean z2 = (HomePageStartupHelper.this.mContext instanceof CrossUserCompatActivity) && ((CrossUserCompatActivity) HomePageStartupHelper.this.mContext).isCrossUserPick();
            long currentTimeMillis = System.currentTimeMillis();
            if (HomePageStartupHelper.this.mHomePageCursor == null) {
                if (!z2) {
                    Object remove = GalleryMemoryCacheHelper.getInstance().remove("first_home_page_datas");
                    if (remove instanceof Cursor) {
                        HomePageStartupHelper.this.mHomePageCursor = (Cursor) remove;
                        HomePageStartupHelper.this.mIsDataInitialized = true;
                        ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.activity.HomePageStartupHelper$2$$ExternalSyntheticLambda1
                            @Override // java.lang.Runnable
                            public final void run() {
                                HomePageStartupHelper.AnonymousClass2.$r8$lambda$IDfkvK3ZB3nv4COl4I6xMuR5PHs(HomePageStartupHelper.AnonymousClass2.this);
                            }
                        });
                        return null;
                    }
                }
                if (!z2 && (loadHomePageDatas = HomePageStartupHelper.this.loadHomePageDatas()) != null) {
                    HomePageStartupHelper.this.mHomePageCursor = loadHomePageDatas;
                }
                try {
                    try {
                        Trace.beginSection("HomePageStartupHelper#loadSnapshotFromGalleryDB");
                        if (HomePageStartupHelper.this.mHomePageCursor != null) {
                            z = true;
                        }
                        if (HomePageStartupHelper.this.mHomePageCursor == null) {
                            boolean isHomePageShowAllPhotos = GalleryPreferences.HomePage.isHomePageShowAllPhotos();
                            HomePageStartupHelper homePageStartupHelper = HomePageStartupHelper.this;
                            homePageStartupHelper.mHomePageCursor = homePageStartupHelper.mContext.getContentResolver().query(HomeMediaLoader.URI, HomeMediaLoader.PROJECTION, HomeMediaLoader.getHomePageSelection(isHomePageShowAllPhotos), null, "alias_sort_time DESC ");
                        }
                        currentTimeMillis = System.currentTimeMillis() - currentTimeMillis;
                        DefaultLogger.d(HomePageStartupHelper.TAG, "load preview cost %d, from snap %s", Long.valueOf(currentTimeMillis), Boolean.valueOf(z));
                    } finally {
                    }
                } catch (Exception e) {
                    DefaultLogger.e(HomePageStartupHelper.TAG, "error when load data in home page ", e);
                }
            }
            if (HomePageStartupHelper.this.mIsDestroyed) {
                HomePageStartupHelper.this.closeCursor();
                return null;
            }
            try {
                synchronized (HomePageStartupHelper.this.mCursorLock) {
                    Trace.beginSection("HomePageStartupHelper#preloadImages");
                    HomePageStartupHelper homePageStartupHelper2 = HomePageStartupHelper.this;
                    ArrayList<ImageDisplayItem> genLoadHomePageImageItems = homePageStartupHelper2.genLoadHomePageImageItems(homePageStartupHelper2.mHomePageCursor);
                    if (HomePageStartupHelper.this.mHomePageCursor.getExtras() != Bundle.EMPTY) {
                        HomePageStartupHelper.this.mHomePageCursor.getExtras().putBoolean("is_first_home_page_datas", true);
                    }
                    ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.activity.HomePageStartupHelper$2$$ExternalSyntheticLambda0
                        @Override // java.lang.Runnable
                        public final void run() {
                            HomePageStartupHelper.AnonymousClass2.$r8$lambda$EW2o7G9gEjCIXkAQE0xrlXymDW4(HomePageStartupHelper.AnonymousClass2.this);
                        }
                    });
                    HomePageStartupHelper.this.preLoadHomePageImageItems(genLoadHomePageImageItems);
                    HomePageStartupHelper.this.mIsDataInitialized = true;
                    if (HomePageStartupHelper.this.mIsDestroyed) {
                        HomePageStartupHelper.this.closeCursor();
                    }
                    if (genLoadHomePageImageItems.size() > 0) {
                        HomePageStartupHelper.this.statSnapshotLoadDuration(currentTimeMillis);
                    }
                }
                return null;
            } finally {
            }
        }

        public /* synthetic */ void lambda$run$0() {
            if (HomePageStartupHelper.this.mDataLoadListener == null || HomePageStartupHelper.this.mIsDestroyed) {
                return;
            }
            HomePageStartupHelper.this.mDataLoadListener.onDataLoadFinish(HomePageStartupHelper.this.mHomePageCursor);
        }

        public /* synthetic */ void lambda$run$1() {
            if (HomePageStartupHelper.this.mDataLoadListener == null || HomePageStartupHelper.this.mIsDestroyed) {
                return;
            }
            HomePageStartupHelper.this.mDataLoadListener.onDataLoadFinish(HomePageStartupHelper.this.mHomePageCursor);
        }
    }

    public final Future startLoadHomePageDatasTask() {
        return ThreadManager.getPreviewPool().submit(new AnonymousClass2());
    }

    public Cursor loadHomePageDatas() {
        try {
            try {
                Trace.beginSection("HomeStartup#loadHomePageDatas");
                GalleryLiteEntityManager galleryLiteEntityManager = GalleryLiteEntityManager.getInstance();
                List query = galleryLiteEntityManager.query(HomeMediaHeader.class, null, null);
                if (BaseMiscUtil.isValid(query)) {
                    List<HomeMedia> query2 = galleryLiteEntityManager.query(HomeMedia.class, null, null);
                    if (BaseMiscUtil.isValid(query2)) {
                        MatrixCursor matrixCursor = new MatrixCursor(HomeMedia.Constants.PROJECTION, query2.size());
                        for (HomeMedia homeMedia : query2) {
                            matrixCursor.addRow(homeMedia.values());
                        }
                        HomeMediaHeader homeMediaHeader = (HomeMediaHeader) query.get(0);
                        Bundle bundle = new Bundle(4);
                        ArrayList<Integer> groupItemCount = HomeMediaHeader.getGroupItemCount(homeMediaHeader);
                        ArrayList<Integer> groupItemStartPos = HomeMediaHeader.getGroupItemStartPos(homeMediaHeader);
                        bundle.putIntegerArrayList("extra_timeline_item_count_in_group", groupItemCount);
                        bundle.putIntegerArrayList("extra_timeline_group_start_pos", groupItemStartPos);
                        bundle.putStringArrayList("extra_timeline_group_labels", HomeMediaHeader.getGroupLocations(homeMediaHeader));
                        matrixCursor.setExtras(bundle);
                        BurstFilterCursor wrapCursor = HomePageAdapter.wrapCursor(matrixCursor);
                        bundle.putSerializable("first_home_page_timeline_cluster", TimelineClusterFactory.createAndPack(matrixCursor, wrapCursor.getResultPos()));
                        try {
                            int i = Config$ThumbConfig.get().sPredictHeadersOneScreen;
                            int size = groupItemStartPos.size();
                            LinkedList linkedList = new LinkedList();
                            wrapCursor.moveToFirst();
                            for (int i2 = 0; i2 < i && i2 < size; i2++) {
                                wrapCursor.moveToPosition(groupItemStartPos.get(i2).intValue());
                                linkedList.add(GalleryDateUtils.formatRelativeDate(wrapCursor.getLong(3)));
                            }
                            bundle.putSerializable("first_home_page_timeline_titles", linkedList);
                            wrapCursor.setExtras(bundle);
                        } catch (Exception e) {
                            DefaultLogger.e(TAG, e);
                        }
                        return wrapCursor;
                    }
                }
            } catch (Exception e2) {
                DefaultLogger.e(TAG, e2);
            }
            return null;
        } finally {
            Trace.endSection();
        }
    }

    public ArrayList<ImageDisplayItem> genLoadHomePageImageItems(Cursor cursor) {
        Trace.beginSection("HomeStartup#genloadImages");
        int preloadImageCount = getPreloadImageCount((cursor == null || cursor.isClosed()) ? null : cursor.getExtras().getIntegerArrayList("extra_timeline_item_count_in_group"));
        ArrayList<ImageDisplayItem> arrayList = new ArrayList<>(preloadImageCount);
        int i = 0;
        BurstFilterCursor wrapCursor = HomePageAdapter.wrapCursor(cursor);
        wrapCursor.moveToFirst();
        while (true) {
            int i2 = i + 1;
            if (i < preloadImageCount) {
                String defaultThumbFilePath = HomePageAdapter.getDefaultThumbFilePath(wrapCursor);
                if (defaultThumbFilePath != null) {
                    arrayList.add(new ImageDisplayItem(defaultThumbFilePath, wrapCursor.getLong(16), wrapCursor.getBlob(23)));
                }
                if (cursor == null || wrapCursor.isClosed() || !wrapCursor.moveToNext()) {
                    break;
                }
                i = i2;
            } else {
                break;
            }
        }
        Trace.endSection();
        return arrayList;
    }

    public final void statSnapshotLoadDuration(long j) {
        HashMap hashMap = new HashMap();
        hashMap.put("cost", String.valueOf(j));
        SamplingStatHelper.recordCountEvent("home", "snapshot_load_duration", hashMap);
    }

    public final int getPreloadImageCount(ArrayList<Integer> arrayList) {
        try {
            Trace.beginSection("HomeStartup#genloadImages");
            int i = 0;
            if (!BaseMiscUtil.isValid(arrayList)) {
                return 0;
            }
            int i2 = this.MICRO_THUMB_COLUMN_NUMBER;
            int i3 = this.MICRO_THUMB_PRELOAD_MAX_COUNT;
            Iterator<Integer> it = arrayList.iterator();
            int i4 = 0;
            while (it.hasNext()) {
                int intValue = it.next().intValue();
                i += intValue;
                if (i + i4 > i3) {
                    break;
                }
                int i5 = intValue % i2;
                if (i5 != 0) {
                    i4 += i2 - i5;
                }
            }
            if (i4 + i >= i3) {
                i = i3 - i4;
            }
            DefaultLogger.d(TAG, "preload image count %d", Integer.valueOf(i));
            return i;
        } finally {
            Trace.endSection();
        }
    }

    public final void preLoadHomePageImageItems(List<ImageDisplayItem> list) {
        if (this.mContext == null || this.mIsDestroyed || !BaseMiscUtil.isValid(list)) {
            return;
        }
        boolean isLowRamDevice = BaseBuildUtil.isLowRamDevice();
        RequestManager with = Glide.with(this.mContext);
        for (ImageDisplayItem imageDisplayItem : list) {
            if (this.mIsDestroyed) {
                DefaultLogger.w(TAG, "preload home page images break");
                return;
            } else if (imageDisplayItem.mThumbBlob != null) {
                with.mo985asBitmap().mo962load(GalleryModel.of(imageDisplayItem.mFilePath, imageDisplayItem.mThumbBlob)).mo946apply((BaseRequestOptions<?>) GlideOptions.pixelsThumbOf(imageDisplayItem.mFileLength)).mo974priority(Priority.HIGH).mo950diskCacheStrategy(DiskCacheStrategy.NONE).preload();
            } else {
                RequestBuilder<Bitmap> mo962load = with.mo985asBitmap().mo962load(GalleryModel.of(imageDisplayItem.mFilePath));
                long j = imageDisplayItem.mFileLength;
                mo962load.mo946apply((BaseRequestOptions<?>) (isLowRamDevice ? GlideOptions.microThumbOf(j) : GlideOptions.tinyThumbOf(j))).mo974priority(Priority.HIGH).preload();
            }
        }
    }

    public final void closeCursor() {
        synchronized (this.mCursorLock) {
            if (this.mHomePageCursor != null) {
                this.mHomePageCursor.close();
            }
        }
    }

    public void setDataLoaderListener(DataLoadListener dataLoadListener) {
        this.mDataLoadListener = dataLoadListener;
        if (dataLoadListener != null) {
            Cursor cursor = null;
            if (this.mIsDataInitialized || (cursor = checkIsContainsPreLoadCursor()) != null) {
                if (this.mHomePageCursor == null) {
                    this.mHomePageCursor = cursor;
                }
                dataLoadListener.onDataLoadFinish(this.mHomePageCursor);
                return;
            }
            DefaultLogger.w(TAG, "preload home page cursor failed");
        }
    }

    public final Cursor checkIsContainsPreLoadCursor() {
        Object remove = GalleryMemoryCacheHelper.getInstance().remove("first_home_page_datas");
        if (remove instanceof Cursor) {
            this.mIsDataInitialized = true;
            return (Cursor) remove;
        }
        return null;
    }

    /* loaded from: classes.dex */
    public static class ImageDisplayItem {
        public long mFileLength;
        public String mFilePath;
        public byte[] mThumbBlob;

        public ImageDisplayItem(String str, long j, byte[] bArr) {
            this.mFilePath = str;
            this.mFileLength = j;
            this.mThumbBlob = bArr;
        }
    }
}
