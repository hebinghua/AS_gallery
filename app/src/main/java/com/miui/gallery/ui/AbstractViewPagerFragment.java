package com.miui.gallery.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.tracing.Trace;
import com.miui.gallery.R;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.adapter.PhotoPageAdapter;
import com.miui.gallery.app.fragment.GalleryFragment;
import com.miui.gallery.ktx.DisplayKt;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.BaseDataSet;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.provider.ProcessingMedia;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.PhotoPagerHelper;
import com.miui.gallery.ui.photoPage.EnterTypeUtils;
import com.miui.gallery.ui.photoPage.bars.data.DataProvider;
import com.miui.gallery.ui.photoPage.bars.data.IDataProvider;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.photoview.ItemViewInfo;
import com.miui.gallery.util.photoview.preload.PhotoPagePreloadHelper;
import com.miui.gallery.util.photoview.preload.PreloadHelperProvider;
import com.miui.gallery.widget.ViewPager;
import com.miui.gallery.widget.slip.VerticalSlipLayout;
import java.util.Map;
import miuix.appcompat.app.ActionBar;

/* loaded from: classes2.dex */
public abstract class AbstractViewPagerFragment extends GalleryFragment implements PhotoPagerHelper.OnPageChangedListener, PhotoPagerHelper.OnPageSettledListener, PhotoPagerHelper.OnImageLoadFinishListener, PhotoPageAdapter.OnPreviewedListener, DataProvider.ViewModelData.IBaseDataSetObserver {
    public boolean isPendingUpdateItem;
    public boolean isShareOngoing;
    public BaseActivity mActivity;
    public PhotoPageAdapter mAdapter;
    public View mChoice;
    public int mChoiceHeight;
    public IDataProvider mDataProvider;
    public long mLastStopMillis;
    public ViewPager mPager;
    public PhotoPagerHelper mPagerHelper;
    public PhotoPagePreloadHelper mPreloadHelper;
    public long mAlbumId = -1;
    public boolean isFirstLoad = true;

    /* renamed from: $r8$lambda$Kfu4pG0VlswsdW3INDnGE-4PjBU */
    public static /* synthetic */ void m1421$r8$lambda$Kfu4pG0VlswsdW3INDnGE4PjBU(AbstractViewPagerFragment abstractViewPagerFragment) {
        abstractViewPagerFragment.lambda$onResume$2();
    }

    public ItemViewInfo getItemViewInfo(int i, long j) {
        return null;
    }

    public abstract View getLayout(LayoutInflater layoutInflater, ViewGroup viewGroup);

    public abstract String getPageName();

    public void onDataSetLoaded(BaseDataSet baseDataSet, boolean z) {
    }

    @Override // com.miui.gallery.ui.PhotoPagerHelper.OnImageLoadFinishListener
    public void onImageLoadFinish(String str) {
    }

    public void onItemChanged(int i) {
    }

    public void onItemSettled(int i) {
    }

    @Override // com.miui.gallery.ui.PhotoPagerHelper.OnPageChangedListener
    public void onPageScrollStateChanged(int i) {
    }

    @Override // com.miui.gallery.ui.PhotoPagerHelper.OnPageChangedListener
    public void onPageScrolled(int i, float f, int i2) {
    }

    public void onPlayVideo(BaseDataItem baseDataItem, String str) {
    }

    public abstract void onViewInflated(View view);

    public boolean recordPageByDefault() {
        return true;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        this.mAlbumId = this.mActivity.getIntent().getLongExtra("album_id", -1L);
        if (isSecretAlbum()) {
            this.mActivity.getWindow().addFlags(8192);
        }
        if (TextUtils.isEmpty(getArguments().getString("photo_uri"))) {
            DefaultLogger.e("AbstractViewPagerFragment", "photo uri should not be null %s", getArguments());
            finish();
            return;
        }
        this.mDataProvider.startLoadData(this, this, new DataProvider.ViewModelData.IProcessingMediaMapObserver() { // from class: com.miui.gallery.ui.AbstractViewPagerFragment$$ExternalSyntheticLambda1
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Map<String, ProcessingMedia> map) {
                AbstractViewPagerFragment.this.lambda$onActivityCreated$0(map);
            }
        }, new DataProvider.ViewModelData.IIsFirstLoadProcessingMediaObserver() { // from class: com.miui.gallery.ui.AbstractViewPagerFragment$$ExternalSyntheticLambda0
            @Override // androidx.lifecycle.Observer
            public final void onChanged(Boolean bool) {
                AbstractViewPagerFragment.this.lambda$onActivityCreated$1(bool);
            }
        });
    }

    public /* synthetic */ void lambda$onActivityCreated$0(Map map) {
        this.mAdapter.setProcessingMedias(map);
    }

    public /* synthetic */ void lambda$onActivityCreated$1(Boolean bool) {
        this.mAdapter.notifyDataSetChanged();
    }

    /* JADX WARN: Removed duplicated region for block: B:120:0x0117  */
    @Override // androidx.lifecycle.Observer
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void onChanged(com.miui.gallery.model.BaseDataSet r10) {
        /*
            Method dump skipped, instructions count: 364
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.ui.AbstractViewPagerFragment.onChanged(com.miui.gallery.model.BaseDataSet):void");
    }

    public final boolean isRtl() {
        return getResources().getConfiguration().getLayoutDirection() == 1;
    }

    public boolean isSecretAlbum() {
        return this.mAlbumId == -1000;
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Trace.beginSection("onInflateView");
        View layout = getLayout(layoutInflater, viewGroup);
        this.mPager = (ViewPager) layout.findViewById(R.id.photo_pager);
        this.mChoice = layout.findViewById(R.id.child_container);
        this.mPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.viewpager_page_margin));
        this.mAdapter = getPhotoAdapter();
        PhotoPagerHelper photoPagerHelper = new PhotoPagerHelper(this.mPager);
        this.mPagerHelper = photoPagerHelper;
        photoPagerHelper.setOnPageChangedListener(this);
        this.mPagerHelper.setOnPageSettledListener(this);
        this.mPagerHelper.setOnImageLoadFinishListener(this);
        onViewInflated(layout);
        this.mPager.setAdapter(this.mAdapter);
        if (BaseBuildUtil.isLowRamDevice() && bundle == null && !isRtl()) {
            this.mPager.preloadFirstItem();
        }
        if (layout.isAttachedToWindow()) {
            this.mAdapter.updateDisplaySize(DisplayKt.getDisplaySize(layout));
        } else {
            layout.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.miui.gallery.ui.AbstractViewPagerFragment.1
                @Override // android.view.View.OnAttachStateChangeListener
                public void onViewDetachedFromWindow(View view) {
                }

                {
                    AbstractViewPagerFragment.this = this;
                }

                @Override // android.view.View.OnAttachStateChangeListener
                public void onViewAttachedToWindow(View view) {
                    view.removeOnAttachStateChangeListener(this);
                    PhotoPageAdapter photoPageAdapter = AbstractViewPagerFragment.this.mAdapter;
                    if (photoPageAdapter != null) {
                        photoPageAdapter.updateDisplaySize(DisplayKt.getDisplaySize(view));
                    }
                }
            });
        }
        Trace.endSection();
        return layout;
    }

    public void configViewLayout(View view, int i) {
        if (ShareStateRouter.IS_MISHARE_AVAILABLE.get(this.mActivity).booleanValue()) {
            if (!ShareStateRouter.SHOULD_UPGRADE_NEARBY_SHARE.get(this.mActivity).booleanValue()) {
                this.mChoiceHeight = getResources().getDimensionPixelSize(R.dimen.fast_share_height);
            } else {
                this.mChoiceHeight = getResources().getDimensionPixelSize(R.dimen.fast_share_height_with_nearby_share);
            }
        } else {
            this.mChoiceHeight = getResources().getDimensionPixelSize(R.dimen.fast_share_height_without_mishare);
        }
        this.mChoice.getLayoutParams().height = this.mChoiceHeight;
        ((VerticalSlipLayout) view.findViewById(R.id.slip_layout)).setFixedSideSlipDistance(i);
        configPager(view, i);
    }

    public void configPager(View view, int i) {
        int fullScreenHeight = ScreenUtils.getFullScreenHeight(this.mActivity);
        int screenWidth = ScreenUtils.getScreenWidth();
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.photo_slip_horizontal_margin);
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(R.dimen.viewpager_page_margin);
        int dimensionPixelSize3 = getResources().getDimensionPixelSize(R.dimen.viewpager_slip_page_margin);
        int i2 = (fullScreenHeight - i) - this.mChoiceHeight;
        this.mAdapter.setSlippedRect(i, i + i2);
        this.mPager.setHeightSlipRatio((i2 * 1.0f) / fullScreenHeight);
        this.mPager.setSlippedHeight(i2);
        this.mPager.setWidthSlipRatio(1.0f - ((dimensionPixelSize * 2.0f) / screenWidth));
        this.mPager.setMarginSlipRatio((dimensionPixelSize3 * 1.0f) / dimensionPixelSize2);
    }

    public PhotoPageAdapter getPhotoAdapter() {
        ImageLoadParams imageLoadParams = getImageLoadParams();
        long key = imageLoadParams != null ? imageLoadParams.getKey() : -1L;
        FragmentActivity activity = getActivity();
        int initCount = getInitCount();
        PhotoPagePreloadHelper photoPagePreloadHelper = this.mPreloadHelper;
        return new PhotoPageAdapter(activity, initCount, imageLoadParams, photoPagePreloadHelper != null ? photoPagePreloadHelper.getPhotoPageViewProviderIfHave() : null, getEnterViewInfo(getArguments().getInt("photo_init_position", 0), key), this, getPhotoPageInteractionListener());
    }

    public int getInitCount() {
        int i = getArguments().getInt("photo_count", 0);
        if (i != 0 || !EnterTypeUtils.isFromCamera(getArguments())) {
            return i;
        }
        return 1;
    }

    public ImageLoadParams getImageLoadParams() {
        ImageLoadParams imageLoadParams = (ImageLoadParams) getArguments().getParcelable("photo_transition_data");
        return imageLoadParams != null ? new ImageLoadParams.Builder().cloneFrom(imageLoadParams).fromCamera(EnterTypeUtils.isFromCamera(getArguments())).build() : imageLoadParams;
    }

    public ItemViewInfo getEnterViewInfo(int i, long j) {
        if (getArguments().getBoolean("photo_enter_transit", false)) {
            getArguments().remove("photo_enter_transit");
            return getItemViewInfo(i, j);
        }
        return null;
    }

    public PhotoPageAdapter.PhotoPageInteractionListener getPhotoPageInteractionListener() {
        return new PhotoPageAdapter.PhotoPageInteractionListener() { // from class: com.miui.gallery.ui.AbstractViewPagerFragment.2
            @Override // com.miui.gallery.adapter.PhotoPageAdapter.PhotoPageInteractionListener
            public int getMenuBarHeight() {
                return 0;
            }

            {
                AbstractViewPagerFragment.this = this;
            }

            @Override // com.miui.gallery.adapter.PhotoPageAdapter.PhotoPageInteractionListener
            public void notifyDataChanged() {
                AbstractViewPagerFragment.this.onContentChanged();
            }

            @Override // com.miui.gallery.adapter.PhotoPageAdapter.PhotoPageInteractionListener
            public int getActionBarHeight() {
                ActionBar actionBar = AbstractViewPagerFragment.this.getActionBar();
                if (!AbstractViewPagerFragment.this.isAdded() || actionBar == null) {
                    return 0;
                }
                return actionBar.getHeight();
            }

            @Override // com.miui.gallery.adapter.PhotoPageAdapter.PhotoPageInteractionListener
            public void playVideo(BaseDataItem baseDataItem, String str) {
                AbstractViewPagerFragment.this.onPlayVideo(baseDataItem, str);
            }
        };
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewStateRestored(Bundle bundle) {
        super.onViewStateRestored(bundle);
        if (getArguments().containsKey("photo_init_position")) {
            int i = getArguments().getInt("photo_init_position", 0);
            int i2 = getArguments().getInt("photo_count", 0);
            if (i < 0 || i >= i2) {
                return;
            }
            this.mPager.setCurrentItem(i, false);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        BaseActivity baseActivity = (BaseActivity) activity;
        this.mActivity = baseActivity;
        if (baseActivity instanceof PreloadHelperProvider) {
            this.mPreloadHelper = ((PreloadHelperProvider) baseActivity).providePreloadHelper();
        }
    }

    @Override // com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initDataProvider();
        if (bundle != null) {
            restoreState(bundle);
        }
    }

    public void initDataProvider() {
        if (this.mDataProvider == null) {
            this.mDataProvider = new DataProvider(this);
        }
    }

    @Override // com.miui.gallery.app.fragment.GalleryFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        saveState(bundle);
    }

    public void restoreState(Bundle bundle) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            arguments.putInt("photo_init_position", bundle.getInt("photo_init_position"));
            arguments.putInt("photo_count", bundle.getInt("photo_count"));
            arguments.remove("photo_enter_transit");
            arguments.remove("photo_transition_data");
        }
    }

    public void saveState(Bundle bundle) {
        if (bundle != null) {
            ViewPager viewPager = this.mPager;
            if (viewPager != null) {
                bundle.putInt("photo_init_position", viewPager.getCurrentItem());
            }
            PhotoPageAdapter photoPageAdapter = this.mAdapter;
            if (photoPageAdapter == null) {
                return;
            }
            bundle.putInt("photo_count", photoPageAdapter.getCount());
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        this.mPagerHelper.onStart();
        this.mDataProvider.cancelBackgroundLoad();
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        this.mPagerHelper.onStop();
        this.mLastStopMillis = System.currentTimeMillis();
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        ThreadManager.getWorkHandler().post(new Runnable() { // from class: com.miui.gallery.ui.AbstractViewPagerFragment$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                AbstractViewPagerFragment.m1421$r8$lambda$Kfu4pG0VlswsdW3INDnGE4PjBU(AbstractViewPagerFragment.this);
            }
        });
    }

    public /* synthetic */ void lambda$onResume$2() {
        if (recordPageByDefault()) {
            SamplingStatHelper.recordPageStart(this.mActivity, getPageName());
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        if (recordPageByDefault()) {
            SamplingStatHelper.recordPageEnd(this.mActivity, getPageName());
        }
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        this.mPager.removeAllViews();
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        this.mDataProvider.release(this);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.mActivity = null;
    }

    @Override // com.miui.gallery.ui.PhotoPagerHelper.OnPageChangedListener
    public final void onPageChanged(int i) {
        if (!this.isPendingUpdateItem) {
            onItemChanged(i);
        }
    }

    @Override // com.miui.gallery.ui.PhotoPagerHelper.OnPageSettledListener
    public final void onPageSettled(int i) {
        if (!this.isPendingUpdateItem) {
            onItemSettled(i);
        }
    }

    public void onContentChanged() {
        this.mDataProvider.onContentChanged();
    }

    public void loadInBackground() {
        this.mDataProvider.loadInBackground();
    }

    @Override // com.miui.gallery.adapter.PhotoPageAdapter.OnPreviewedListener
    public void onPreviewed() {
        if (this.isPendingUpdateItem) {
            this.isPendingUpdateItem = false;
            this.mPagerHelper.setPageChanged(this.mPager.getCurrentItem());
            this.mPagerHelper.setPageSettled(this.mPager.getCurrentItem());
            onDataSetLoaded(this.mAdapter.getDataSet(), true);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        boolean z = true;
        if (i == 1) {
            if (i2 != -1) {
                z = false;
            }
            onShared(z);
        }
    }

    public void onShared(boolean z) {
        this.isShareOngoing = true;
    }

    public void finish() {
        BaseActivity baseActivity = this.mActivity;
        if (baseActivity == null || baseActivity.isFinishing()) {
            return;
        }
        this.mActivity.finish();
    }

    public boolean isNeedConfirmPassWord(BaseDataItem baseDataItem) {
        if (this.isShareOngoing && this.mDataProvider.getFieldData().isNeedConfirmPassWord && this.mLastStopMillis > 0) {
            this.mDataProvider.getFieldData().isNeedConfirmPassWord = System.currentTimeMillis() - this.mLastStopMillis > 15000;
            this.isShareOngoing = false;
        }
        return baseDataItem != null && baseDataItem.isSecret() && this.mDataProvider.getFieldData().isNeedConfirmPassWord;
    }

    @Override // com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.mAdapter == null || !isAdded() || getView() == null) {
            return;
        }
        this.mAdapter.updateDisplaySize(DisplayKt.getDisplaySize(getView()));
        View view = this.mChoice;
        if (view == null) {
            return;
        }
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
        layoutParams.topMargin = getResources().getDimensionPixelOffset(R.dimen.preview_chooser_margin_top);
        if (ShareStateRouter.IS_MISHARE_AVAILABLE.get(this.mActivity).booleanValue()) {
            if (!ShareStateRouter.SHOULD_UPGRADE_NEARBY_SHARE.get(this.mActivity).booleanValue()) {
                layoutParams.height = getResources().getDimensionPixelSize(R.dimen.fast_share_height);
            } else {
                layoutParams.height = getResources().getDimensionPixelSize(R.dimen.fast_share_height_with_nearby_share);
            }
        } else {
            layoutParams.height = getResources().getDimensionPixelSize(R.dimen.fast_share_height_without_mishare);
        }
        this.mChoice.setLayoutParams(layoutParams);
    }
}
