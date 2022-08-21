package com.miui.gallery.ui;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.cloud.syncstate.SyncStateUtil;
import com.miui.gallery.content.ExtendedAsyncTaskLoader;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.actionBar.BaseCommonActionBarHelper;
import com.miui.gallery.ui.actionBar.SimpleThemeActionBarHelper;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.ColorRingProgress;
import com.miui.gallery.widget.GalleryPullZoomLayout;
import com.miui.gallery.widget.recyclerview.CleanerItemAnimator;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class CloudSpaceStatusFragment extends BaseFragment {
    public SimpleThemeActionBarHelper mActionBarHelper;
    public StatusAdapter mAdapter;
    public List<DataItem> mDataList = new ArrayList();
    public ColorRingProgress mProgress;
    public GalleryRecyclerView mRecyclerView;
    public GalleryPullZoomLayout mScrollingLayout;
    public View mStorageFrame;
    public TextView mStorageLeftDetail;
    public TextView mStorageTotalDetail;
    public TextView mUsedPercent;

    public static /* synthetic */ void $r8$lambda$9K2iR7cBhyJqrAILP_mXo71pHzU(CloudSpaceStatusFragment cloudSpaceStatusFragment, GalleryPullZoomLayout.ScrollBy scrollBy, float f) {
        cloudSpaceStatusFragment.lambda$onInflateView$1(scrollBy, f);
    }

    /* renamed from: $r8$lambda$EYur78445xwdG9SM682HS-7BqSw */
    public static /* synthetic */ void m1443$r8$lambda$EYur78445xwdG9SM682HS7BqSw(CloudSpaceStatusFragment cloudSpaceStatusFragment) {
        cloudSpaceStatusFragment.lambda$onInflateView$0();
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "cloud_space_status";
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public int getThemeRes() {
        return 2131952019;
    }

    @Override // com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(false);
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.cloud_space_status, viewGroup, false);
        this.mStorageFrame = inflate.findViewById(R.id.storage_frame);
        this.mProgress = (ColorRingProgress) inflate.findViewById(R.id.used_progress);
        this.mUsedPercent = (TextView) inflate.findViewById(R.id.used_percent);
        this.mStorageLeftDetail = (TextView) inflate.findViewById(R.id.storage_left_detail);
        this.mStorageTotalDetail = (TextView) inflate.findViewById(R.id.storage_total_detail);
        this.mAdapter = new StatusAdapter(this.mDataList);
        GalleryRecyclerView galleryRecyclerView = (GalleryRecyclerView) inflate.findViewById(R.id.list);
        this.mRecyclerView = galleryRecyclerView;
        galleryRecyclerView.setLayoutManager(new LinearLayoutManager(this.mActivity));
        this.mRecyclerView.setAdapter(this.mAdapter);
        CleanerItemAnimator cleanerItemAnimator = new CleanerItemAnimator();
        cleanerItemAnimator.setSupportsChangeAnimations(false);
        this.mRecyclerView.setItemAnimator(cleanerItemAnimator);
        this.mRecyclerView.setNestedScrollingEnabled(true);
        BaseCommonActionBarHelper.DefaultThemeConfig defaultThemeConfig = new BaseCommonActionBarHelper.DefaultThemeConfig();
        defaultThemeConfig.setActionBarLayoutRes(R.layout.cloud_page_title);
        SimpleThemeActionBarHelper simpleThemeActionBarHelper = new SimpleThemeActionBarHelper(this.mActivity, defaultThemeConfig);
        this.mActionBarHelper = simpleThemeActionBarHelper;
        simpleThemeActionBarHelper.inflateActionBar();
        this.mActionBarHelper.setNullStyleActionBar();
        GalleryPullZoomLayout galleryPullZoomLayout = (GalleryPullZoomLayout) inflate.findViewById(R.id.scrolling_layout);
        this.mScrollingLayout = galleryPullZoomLayout;
        galleryPullZoomLayout.post(new Runnable() { // from class: com.miui.gallery.ui.CloudSpaceStatusFragment$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                CloudSpaceStatusFragment.m1443$r8$lambda$EYur78445xwdG9SM682HS7BqSw(CloudSpaceStatusFragment.this);
            }
        });
        this.mScrollingLayout.setOnScrollListener(new GalleryPullZoomLayout.OnScrollListener() { // from class: com.miui.gallery.ui.CloudSpaceStatusFragment$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.widget.GalleryPullZoomLayout.OnScrollListener
            public final void onScrolled(GalleryPullZoomLayout.ScrollBy scrollBy, float f) {
                CloudSpaceStatusFragment.$r8$lambda$9K2iR7cBhyJqrAILP_mXo71pHzU(CloudSpaceStatusFragment.this, scrollBy, f);
            }
        });
        return inflate;
    }

    public /* synthetic */ void lambda$onInflateView$0() {
        this.mScrollingLayout.setActionBarHeight(this.mActivity.getAppCompatActionBar().getHeight());
    }

    public /* synthetic */ void lambda$onInflateView$1(GalleryPullZoomLayout.ScrollBy scrollBy, float f) {
        this.mActionBarHelper.refreshTopBar(f);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getLoaderManager().initLoader(1, null, new SpaceStatusLoaderCallBack());
        SamplingStatHelper.recordCountEvent("Sync", "sync_cloud_space_status");
    }

    public final void refresh(CloudStatusData cloudStatusData) {
        DefaultLogger.d("CloudSpaceStatusFragment", "refresh %s", cloudStatusData);
        if (cloudStatusData != null) {
            long j = cloudStatusData.mTotalCloudSpace;
            if (j != 0) {
                long j2 = cloudStatusData.mLeftCloudSpace;
                if (j2 <= j) {
                    float min = Math.min(1.0f, 1.0f - ((((float) j2) * 1.0f) / ((float) j)));
                    this.mProgress.setProgress(min);
                    this.mUsedPercent.setText(String.format("%d", Integer.valueOf((int) (min * 100.0f))));
                }
            }
            String quantityStringWithUnit = SyncStateUtil.getQuantityStringWithUnit(cloudStatusData.mLeftCloudSpace);
            String quantityStringWithUnit2 = SyncStateUtil.getQuantityStringWithUnit(cloudStatusData.mTotalCloudSpace);
            this.mStorageLeftDetail.setText(quantityStringWithUnit);
            this.mStorageTotalDetail.setText(quantityStringWithUnit2);
            Resources resources = getResources();
            this.mDataList.clear();
            DataItem dataItem = new DataItem();
            dataItem.mTitle = resources.getString(R.string.cloud_storage_photo_title);
            dataItem.mAppendTitle = SyncStateUtil.getQuantityStringWithUnit(cloudStatusData.mSyncedPhotoSize);
            int i = cloudStatusData.mSyncedPhotoCount;
            dataItem.mValue = resources.getQuantityString(R.plurals.cloud_storage_photo_num, i, Integer.valueOf(i));
            DataItem dataItem2 = new DataItem();
            dataItem2.mTitle = resources.getString(R.string.cloud_storage_video_title);
            dataItem2.mAppendTitle = SyncStateUtil.getQuantityStringWithUnit(cloudStatusData.mSyncedVideoSize);
            int i2 = cloudStatusData.mSyncedVideoCount;
            dataItem2.mValue = resources.getQuantityString(R.plurals.cloud_storage_video_num, i2, Integer.valueOf(i2));
            DataItem dataItem3 = new DataItem();
            dataItem3.mTitle = resources.getString(R.string.cloud_storage_extend);
            if (cloudStatusData.mIsSpaceLow) {
                this.mProgress.setBackgroundColor(resources.getColor(R.color.space_progress_back_warn));
                this.mStorageFrame.setBackgroundResource(R.drawable.cloud_space_full_background);
            } else {
                this.mProgress.setBackgroundColor(resources.getColor(R.color.space_progress_back));
                this.mStorageFrame.setBackgroundResource(R.color.status_ok_color);
            }
            dataItem3.mClickListener = new View.OnClickListener() { // from class: com.miui.gallery.ui.CloudSpaceStatusFragment.1
                {
                    CloudSpaceStatusFragment.this = this;
                }

                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    IntentUtil.gotoMiCloudVipPage(CloudSpaceStatusFragment.this.getActivity(), new Pair("source", IntentUtil.getMiCloudVipPageSource("gallery_textlink_syncsuc")));
                    SamplingStatHelper.recordCountEvent("Sync", "sync_cloud_space_enlarge");
                    AutoTracking.trackView("403.28.1.1.11283", AutoTracking.getRef());
                }
            };
            dataItem3.mIsShowArrow = true;
            this.mDataList.add(dataItem);
            this.mDataList.add(dataItem2);
            this.mDataList.add(dataItem3);
            this.mAdapter.notifyDataSetChanged();
        }
    }

    /* loaded from: classes2.dex */
    public class SpaceStatusLoaderCallBack implements LoaderManager.LoaderCallbacks<CloudStatusData> {
        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoaderReset(Loader<CloudStatusData> loader) {
        }

        public SpaceStatusLoaderCallBack() {
            CloudSpaceStatusFragment.this = r1;
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public Loader<CloudStatusData> onCreateLoader(int i, Bundle bundle) {
            return new SpaceStatusLoader(CloudSpaceStatusFragment.this.getActivity());
        }

        @Override // androidx.loader.app.LoaderManager.LoaderCallbacks
        public void onLoadFinished(Loader<CloudStatusData> loader, CloudStatusData cloudStatusData) {
            CloudSpaceStatusFragment.this.refresh(cloudStatusData);
        }
    }

    /* loaded from: classes2.dex */
    public static class SpaceStatusLoader extends ExtendedAsyncTaskLoader<CloudStatusData> {
        public CloudStatusData mData;

        @Override // androidx.loader.content.AsyncTaskLoader
        public void onCanceled(CloudStatusData cloudStatusData) {
        }

        public SpaceStatusLoader(Context context) {
            super(context);
        }

        @Override // androidx.loader.content.AsyncTaskLoader
        /* renamed from: loadInBackground */
        public CloudStatusData mo1444loadInBackground() {
            CloudStatusData cloudStatusData = new CloudStatusData();
            int[] syncedCount = SyncStateUtil.getSyncedCount(getContext());
            long[] syncedSize = SyncStateUtil.getSyncedSize(getContext());
            cloudStatusData.mSyncedPhotoCount = syncedCount[0];
            cloudStatusData.mSyncedPhotoSize = syncedSize[0];
            cloudStatusData.mSyncedVideoCount = syncedCount[1];
            cloudStatusData.mSyncedVideoSize = syncedSize[1];
            SyncStateUtil.CloudSpaceInfo cloudSpaceInfo = SyncStateUtil.getCloudSpaceInfo(getContext());
            long total = cloudSpaceInfo.getTotal();
            cloudStatusData.mTotalCloudSpace = total;
            cloudStatusData.mLeftCloudSpace = total - cloudSpaceInfo.getUsed();
            cloudStatusData.mIsSpaceLow = cloudSpaceInfo.isSpaceLow();
            return cloudStatusData;
        }

        @Override // com.miui.gallery.content.ExtendedAsyncTaskLoader, androidx.loader.content.Loader
        public void onStartLoading() {
            CloudStatusData cloudStatusData = this.mData;
            if (cloudStatusData != null) {
                deliverResult(cloudStatusData);
            }
            if (takeContentChanged() || this.mData == null) {
                forceLoad();
            }
        }

        @Override // com.miui.gallery.content.ExtendedAsyncTaskLoader, androidx.loader.content.Loader
        public void onStopLoading() {
            cancelLoad();
        }

        @Override // androidx.loader.content.Loader
        public void onReset() {
            super.onReset();
            onStopLoading();
            this.mData = null;
        }

        @Override // androidx.loader.content.Loader
        public final void deliverResult(CloudStatusData cloudStatusData) {
            if (isReset()) {
                return;
            }
            this.mData = cloudStatusData;
            if (!isStarted()) {
                return;
            }
            super.deliverResult((SpaceStatusLoader) cloudStatusData);
        }
    }

    /* loaded from: classes2.dex */
    public static class CloudStatusData {
        public boolean mIsSpaceLow;
        public long mLeftCloudSpace;
        public int mSyncedPhotoCount;
        public long mSyncedPhotoSize;
        public int mSyncedVideoCount;
        public long mSyncedVideoSize;
        public long mTotalCloudSpace;

        public CloudStatusData() {
        }
    }

    /* loaded from: classes2.dex */
    public static class DataItem {
        public String mAppendSummary;
        public String mAppendTitle;
        public View.OnClickListener mClickListener;
        public boolean mIsShowArrow;
        public String mSummary;
        public String mTitle;
        public String mValue;

        public DataItem() {
        }
    }

    /* loaded from: classes2.dex */
    public class StatusAdapter extends RecyclerView.Adapter<ViewHolder> {
        public List<DataItem> mList;

        public StatusAdapter(List<DataItem> list) {
            CloudSpaceStatusFragment.this = r1;
            this.mList = list;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            List<DataItem> list = this.mList;
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* renamed from: onCreateViewHolder */
        public ViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
            viewGroup.setLayoutParams(new FrameLayout.LayoutParams(-1, -2));
            return new ViewHolder(LayoutInflater.from(CloudSpaceStatusFragment.this.getActivity()).inflate(R.layout.cloud_space_status_item, viewGroup, false));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.bindData((DataItem) CloudSpaceStatusFragment.this.mDataList.get(i));
        }

        /* loaded from: classes2.dex */
        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView mAppendSummary;
            public TextView mAppendTitle;
            public View mRightArrow;
            public TextView mSummary;
            public View mSummayDivider;
            public TextView mTitle;
            public View mTitleDivider;
            public TextView mValue;

            public static /* synthetic */ boolean lambda$bindData$0(View view, MotionEvent motionEvent) {
                return true;
            }

            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            public ViewHolder(View view) {
                super(view);
                StatusAdapter.this = r3;
                this.mTitle = (TextView) view.findViewById(R.id.title);
                this.mTitleDivider = view.findViewById(R.id.title_divider);
                this.mAppendTitle = (TextView) view.findViewById(R.id.append_title);
                this.mSummary = (TextView) view.findViewById(R.id.summary);
                this.mSummayDivider = view.findViewById(R.id.summary_divider);
                this.mAppendSummary = (TextView) view.findViewById(R.id.append_summary);
                this.mValue = (TextView) view.findViewById(R.id.value);
                this.mRightArrow = view.findViewById(R.id.arrow_right);
                FolmeUtil.setDefaultTouchAnim(view, null, false, false, true);
            }

            public void bindData(DataItem dataItem) {
                if (dataItem == null) {
                    return;
                }
                this.mTitle.setText(dataItem.mTitle);
                int i = 0;
                if (!TextUtils.isEmpty(dataItem.mAppendTitle)) {
                    this.mAppendTitle.setText(dataItem.mAppendTitle);
                    this.mTitleDivider.setVisibility(0);
                    this.mAppendTitle.setVisibility(0);
                } else {
                    this.mTitleDivider.setVisibility(8);
                    this.mAppendTitle.setVisibility(8);
                }
                if (!TextUtils.isEmpty(dataItem.mSummary)) {
                    this.mSummary.setText(dataItem.mSummary);
                    this.mSummary.setVisibility(0);
                    if (!TextUtils.isEmpty(dataItem.mAppendSummary)) {
                        this.mAppendSummary.setText(dataItem.mAppendSummary);
                        this.mSummayDivider.setVisibility(0);
                        this.mAppendSummary.setVisibility(0);
                    } else {
                        this.mSummayDivider.setVisibility(8);
                        this.mAppendSummary.setVisibility(8);
                    }
                } else {
                    this.mSummary.setVisibility(8);
                    this.mSummayDivider.setVisibility(8);
                    this.mAppendSummary.setVisibility(8);
                }
                if (!TextUtils.isEmpty(dataItem.mValue)) {
                    this.mValue.setText(dataItem.mValue);
                    this.mValue.setVisibility(0);
                } else {
                    this.mValue.setVisibility(8);
                }
                View view = this.mRightArrow;
                if (!dataItem.mIsShowArrow) {
                    i = 8;
                }
                view.setVisibility(i);
                this.itemView.setOnClickListener(dataItem.mClickListener);
                if (dataItem.mClickListener != null) {
                    return;
                }
                this.itemView.setOnTouchListener(CloudSpaceStatusFragment$StatusAdapter$ViewHolder$$ExternalSyntheticLambda0.INSTANCE);
            }
        }
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        GalleryPullZoomLayout galleryPullZoomLayout = this.mScrollingLayout;
        if (galleryPullZoomLayout != null) {
            galleryPullZoomLayout.setOnScrollListener(null);
        }
    }
}
