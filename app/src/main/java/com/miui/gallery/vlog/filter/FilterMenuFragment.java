package com.miui.gallery.vlog.filter;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.miui.gallery.editor.photo.utils.ScreenAdaptationHelper;
import com.miui.gallery.editor.ui.view.BubbleSeekBar;
import com.miui.gallery.ui.CenterSmoothScrollerController;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.MenuFragment;
import com.miui.gallery.vlog.R$color;
import com.miui.gallery.vlog.R$dimen;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.base.net.VlogResource;
import com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager;
import com.miui.gallery.vlog.base.stat.VlogStatUtils;
import com.miui.gallery.vlog.entity.FilterData;
import com.miui.gallery.vlog.home.VlogConfig;
import com.miui.gallery.vlog.tools.VlogOrientationProvider;
import com.miui.gallery.vlog.tools.VlogUtils;
import com.miui.gallery.widget.recyclerview.BlankDivider;
import com.miui.gallery.widget.recyclerview.CustomScrollerLinearLayoutManager;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerView;
import java.io.File;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class FilterMenuFragment extends MenuFragment implements FilterMenuContract$IFilterMenuView, View.OnClickListener {
    public static long mLastClickTime;
    public FilterAdapter mAdapter;
    public FilterZipFileConfig mFilterZipFileConfig;
    public boolean mIsProcessingData;
    public LottieAnimationView mMasterCloseView;
    public LottieAnimationView mMasterOpenView;
    public FrameLayout mMasterViewLayout;
    public View mOperationView;
    public ImageView mPlayView;
    public FrameLayout mPlayViewLayout;
    public SimpleRecyclerView mRecyclerView;
    public ResourceDownloadManager mResourceDownloadManager;
    public BubbleSeekBar mSeekBar;
    public FrameLayout mSeekBarLayout;
    public FilterData mSelectedItem;
    public View mSeparatedView;
    public View.OnTouchListener mSeekBarLayoutOnTouchListener = new View.OnTouchListener() { // from class: com.miui.gallery.vlog.filter.FilterMenuFragment.1
        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            return FilterMenuFragment.this.mSeekBar.onTouchEvent(motionEvent);
        }
    };
    public Animator.AnimatorListener mMasterOpenViewAnimatorListener = new AnimatorListenerAdapter() { // from class: com.miui.gallery.vlog.filter.FilterMenuFragment.2
        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            FilterMenuFragment.this.updateMasterViewStatus();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            FilterMenuFragment.this.updateMasterViewStatus();
        }
    };
    public Animator.AnimatorListener mMasterCloseViewAnimatorListener = new AnimatorListenerAdapter() { // from class: com.miui.gallery.vlog.filter.FilterMenuFragment.3
        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            FilterMenuFragment.this.updateMasterViewStatus();
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            FilterMenuFragment.this.updateMasterViewStatus();
        }
    };
    public BubbleSeekBar.ProgressListener mProgressListener = new BubbleSeekBar.ProgressListener() { // from class: com.miui.gallery.vlog.filter.FilterMenuFragment.4
        @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
        public void onStartTrackingTouch(BubbleSeekBar bubbleSeekBar) {
        }

        @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
        public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int i) {
            if (i == 0 || i == 100) {
                LinearMotorHelper.performHapticFeedback(FilterMenuFragment.this.mSeekBar, LinearMotorHelper.HAPTIC_MESH_HEAVY);
            }
            if (FilterMenuFragment.this.mSelectedItem == null || FilterMenuFragment.this.mSelectedItem.isNone()) {
                return;
            }
            FilterMenuFragment.this.mSelectedItem.setProgress(i);
            FilterMenuFragment filterMenuFragment = FilterMenuFragment.this;
            filterMenuFragment.notifyDateSetChanged(filterMenuFragment.mAdapter.getSelection());
            ((FilterMenuPresenter) FilterMenuPresenter.class.cast(FilterMenuFragment.this.mMenuPresenter)).updateFilterIntensity(FilterMenuFragment.this.mSelectedItem);
        }

        @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
        public void onStopTrackingTouch(BubbleSeekBar bubbleSeekBar) {
            if (FilterMenuFragment.this.mSelectedItem == null || FilterMenuFragment.this.mSelectedItem.isNone()) {
                return;
            }
            FilterMenuFragment.this.mSeekBar.setContentDescription(String.format("%s%s%d", FilterMenuFragment.this.getResources().getString(FilterMenuFragment.this.mSelectedItem.getNameResId()), FilterMenuFragment.this.getResources().getString(R$string.vlog_talkback_filter_seekbar), Integer.valueOf(FilterMenuFragment.this.mSelectedItem.getProgress())));
        }
    };
    public ResourceDownloadManager.IDownloadTaskListener mIDownloadTaskListener = new ResourceDownloadManager.IDownloadTaskListener<VlogResource>() { // from class: com.miui.gallery.vlog.filter.FilterMenuFragment.5
        @Override // com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager.IDownloadTaskListener
        public void onCommandStart(VlogResource vlogResource, int i) {
            if (vlogResource != null) {
                vlogResource.setDownloadState(18);
                FilterMenuFragment.this.notifyDateSetChanged(i);
                DefaultLogger.d("FilterMenuFragment", "download start: %s", vlogResource.getLabel());
            }
        }

        @Override // com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager.IDownloadTaskListener
        public void onCommandSuccess(VlogResource vlogResource, int i) {
            if (vlogResource instanceof FilterData) {
                vlogResource.setDownloadState(17);
                StringBuilder sb = new StringBuilder();
                sb.append(VlogConfig.FILTER_PATH);
                sb.append(File.separator);
                FilterData filterData = (FilterData) vlogResource;
                sb.append(filterData.getFileName());
                filterData.setPath(sb.toString());
                FilterMenuFragment.this.notifyDateSetChanged(i);
                FilterMenuFragment.this.performItem(filterData, i);
                DefaultLogger.d("FilterMenuFragment", "download success: %s", vlogResource.getLabel());
            }
        }

        @Override // com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager.IDownloadTaskListener
        public void onCommandFail(VlogResource vlogResource, int i) {
            if (vlogResource != null) {
                vlogResource.setDownloadState(20);
                FilterMenuFragment.this.notifyDateSetChanged(i);
                DefaultLogger.d("FilterMenuFragment", "download fail: %s", vlogResource.getLabel());
            }
        }

        @Override // com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager.IDownloadTaskListener
        public void onTaskCancel(VlogResource vlogResource, int i) {
            if (vlogResource != null) {
                int downloadState = vlogResource.getDownloadState();
                if (downloadState != 0 && downloadState != 17) {
                    vlogResource.setDownloadState(20);
                    FilterMenuFragment.this.notifyDateSetChanged(i);
                }
                DefaultLogger.d("FilterMenuFragment", "download cancel: %s", vlogResource.getLabel());
            }
        }
    };
    public OnItemClickListener mOnItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.vlog.filter.FilterMenuFragment.6
        @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
        public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
            if (FilterMenuFragment.isFastClick()) {
                return false;
            }
            ((SimpleRecyclerView) recyclerView).setSpringEnabled(false);
            if (FilterMenuFragment.this.mIsProcessingData) {
                return false;
            }
            recyclerView.smoothScrollToPosition(i);
            FilterAdapter filterAdapter = (FilterAdapter) recyclerView.getAdapter();
            FilterData itemData = filterAdapter.getItemData(i);
            if (itemData == null) {
                return false;
            }
            if (itemData.isNone()) {
                VlogUtils.hideViews(FilterMenuFragment.this.mSeekBar, FilterMenuFragment.this.mMasterViewLayout);
                ((FilterMenuPresenter) FilterMenuPresenter.class.cast(FilterMenuFragment.this.mMenuPresenter)).removeFilterEffect();
                if (filterAdapter.isInEditMode()) {
                    filterAdapter.exitEditMode();
                }
                filterAdapter.setSelection(i);
                FilterMenuFragment.this.mSelectedItem = filterAdapter.getSelectedItem();
                ((FilterMenuPresenter) FilterMenuPresenter.class.cast(FilterMenuFragment.this.mMenuPresenter)).updateFilterIntensity(FilterMenuFragment.this.mSelectedItem);
                ((FilterMenuPresenter) FilterMenuPresenter.class.cast(FilterMenuFragment.this.mMenuPresenter)).tryToPlayVideo();
            } else if (itemData.isExtra()) {
                VlogUtils.showViews(FilterMenuFragment.this.mSeekBar, FilterMenuFragment.this.mMasterViewLayout);
                FilterMenuFragment.this.performItem(itemData, i);
            }
            VlogStatUtils.statEvent("filter", itemData.getNameKey());
            return true;
        }
    };

    @Override // com.miui.gallery.vlog.MenuFragment
    public View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return layoutInflater.inflate(R$layout.vlog_menu_filter_layout, (ViewGroup) null);
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void initView(View view) {
        initDirectionView();
        initRecyclerView();
        FrameLayout frameLayout = (FrameLayout) this.mOperationView.findViewById(R$id.seek_bar_layout);
        this.mSeekBarLayout = frameLayout;
        frameLayout.setOnTouchListener(this.mSeekBarLayoutOnTouchListener);
        BubbleSeekBar bubbleSeekBar = (BubbleSeekBar) this.mOperationView.findViewById(R$id.seek_bar);
        this.mSeekBar = bubbleSeekBar;
        bubbleSeekBar.setContentDescription(getResources().getString(R$string.vlog_talkback_filter_seekbar));
        this.mPlayViewLayout.setOnClickListener(this);
        this.mPlayViewLayout.setContentDescription(getResources().getString(R$string.vlog_talkback_view_play));
        FolmeUtil.setCustomTouchAnim(this.mPlayViewLayout, new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(0.95f).build(), null, null, true);
        this.mMasterViewLayout = (FrameLayout) this.mOperationView.findViewById(R$id.layout_master);
        this.mMasterOpenView = (LottieAnimationView) this.mOperationView.findViewById(R$id.image_master_open);
        this.mMasterCloseView = (LottieAnimationView) this.mOperationView.findViewById(R$id.image_master_close);
        this.mMasterViewLayout.setOnClickListener(this);
        this.mMasterOpenView.addAnimatorListener(this.mMasterOpenViewAnimatorListener);
        this.mMasterCloseView.addAnimatorListener(this.mMasterCloseViewAnimatorListener);
        this.mMasterCloseView.setContentDescription(getResources().getString(R$string.vlog_talkback_filter_master_open));
        this.mMasterOpenView.setContentDescription(getResources().getString(R$string.vlog_talkback_filter_master_close));
        FolmeUtil.setCustomTouchAnim(this.mMasterViewLayout, new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(0.95f).build(), null, null, true);
        this.mFilterZipFileConfig = new FilterZipFileConfig();
        ResourceDownloadManager resourceDownloadManager = new ResourceDownloadManager(this.mContext, getFragmentManager(), this.mFilterZipFileConfig);
        this.mResourceDownloadManager = resourceDownloadManager;
        resourceDownloadManager.setDownloadTaskListener(this.mIDownloadTaskListener);
        this.mSeekBar.setProgressListener(this.mProgressListener);
    }

    @Override // com.miui.gallery.vlog.MenuFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        updateMasterViewStatus();
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void onScreenSizeChanged(int i) {
        super.onScreenSizeChanged(i);
        ScreenAdaptationHelper.updateBSBWidth(getContext(), this.mSeekBar);
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public View getOperationView() {
        return this.mOperationView;
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public View getSeparatedView() {
        return this.mSeparatedView;
    }

    public final void initDirectionView() {
        if (isLandscape()) {
            this.mOperationView = LayoutInflater.from(this.mContext).inflate(R$layout.vlog_menu_filter_operation_layout_land, (ViewGroup) null, false);
            View inflate = LayoutInflater.from(this.mContext).inflate(R$layout.vlog_menu_filter_separated_layout, (ViewGroup) null, false);
            this.mSeparatedView = inflate;
            this.mPlayViewLayout = (FrameLayout) inflate.findViewById(R$id.play_layout);
            this.mPlayView = (ImageView) this.mSeparatedView.findViewById(R$id.play_view);
            return;
        }
        View inflate2 = LayoutInflater.from(this.mContext).inflate(R$layout.vlog_menu_filter_operation_layout, (ViewGroup) null, false);
        this.mOperationView = inflate2;
        this.mPlayViewLayout = (FrameLayout) inflate2.findViewById(R$id.play_layout);
        this.mPlayView = (ImageView) this.mOperationView.findViewById(R$id.play_view);
    }

    public final void initRecyclerView() {
        SimpleRecyclerView simpleRecyclerView = (SimpleRecyclerView) getViewById(R$id.recycler_view);
        this.mRecyclerView = simpleRecyclerView;
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) simpleRecyclerView.getLayoutParams();
        CustomScrollerLinearLayoutManager customScrollerLinearLayoutManager = new CustomScrollerLinearLayoutManager(getActivity());
        customScrollerLinearLayoutManager.setSmoothScroller(new CenterSmoothScrollerController(getActivity()));
        int i = R$dimen.vlog_common_menu_recyclerview_item_gap;
        if (VlogUtils.isLandscape(getContext())) {
            customScrollerLinearLayoutManager.setOrientation(1);
            layoutParams.width = (int) getResources().getDimension(com.miui.gallery.editor.R$dimen.editor_menu_filter_item_height);
            layoutParams.height = -1;
            layoutParams.setMarginStart(getResources().getDimensionPixelSize(R$dimen.vlog_filter_recycler_view_padding_top));
        } else {
            customScrollerLinearLayoutManager.setOrientation(0);
            layoutParams.width = -1;
            layoutParams.height = (int) getResources().getDimension(com.miui.gallery.editor.R$dimen.editor_menu_filter_item_height);
            layoutParams.setMargins(0, getResources().getDimensionPixelSize(R$dimen.vlog_filter_recycler_view_padding_top), 0, 0);
        }
        this.mRecyclerView.setLayoutManager(customScrollerLinearLayoutManager);
        this.mRecyclerView.setEnableItemClickWhileSettling(true);
        if (this.mRecyclerView.getItemDecorationCount() == 0) {
            BlankDivider blankDivider = new BlankDivider(getResources(), i);
            blankDivider.setOrientationProvider(new VlogOrientationProvider());
            this.mRecyclerView.addItemDecoration(blankDivider);
        }
    }

    public final void updateMasterViewStatus() {
        boolean isMasterOpened = ((FilterMenuPresenter) FilterMenuPresenter.class.cast(this.mMenuPresenter)).isMasterOpened();
        int i = 0;
        this.mMasterOpenView.setVisibility(isMasterOpened ? 0 : 4);
        LottieAnimationView lottieAnimationView = this.mMasterCloseView;
        if (isMasterOpened) {
            i = 4;
        }
        lottieAnimationView.setVisibility(i);
        if (isMasterOpened) {
            this.mMasterCloseView.setProgress(0.0f);
        } else {
            this.mMasterOpenView.setProgress(0.0f);
        }
    }

    public final void performItem(FilterData filterData, int i) {
        if (filterData == null || filterData.isNone()) {
            DefaultLogger.d("FilterMenuFragment", "performItem: filterData is invalid.");
        } else if (this.mAdapter == null) {
            DefaultLogger.d("FilterMenuFragment", "mAdapter is null");
        } else {
            T t = this.mMenuPresenter;
            if (t == 0 || t.isSaving()) {
                DefaultLogger.d("FilterMenuFragment", "in save progress");
            } else if (filterData.isDownloaded()) {
                this.mAdapter.setSelection(i);
                ((FilterMenuPresenter) FilterMenuPresenter.class.cast(this.mMenuPresenter)).buildFilter(filterData);
                if (!this.mAdapter.isInEditMode()) {
                    this.mAdapter.enterEditMode();
                }
                this.mSeekBar.setCurrentProgress(filterData.getProgress());
                this.mSelectedItem = this.mAdapter.getSelectedItem();
                ((FilterMenuPresenter) FilterMenuPresenter.class.cast(this.mMenuPresenter)).updateFilterIntensity(this.mSelectedItem);
                ((FilterMenuPresenter) FilterMenuPresenter.class.cast(this.mMenuPresenter)).tryToPlayVideo();
            } else {
                this.mResourceDownloadManager.createDownloadCommand(filterData, i);
            }
        }
    }

    @Override // com.miui.gallery.vlog.MenuFragment, androidx.fragment.app.Fragment
    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
        T t = this.mMenuPresenter;
        if (t == 0 || this.mAdapter == null) {
            return;
        }
        if (z) {
            VlogUtils.hideViews(this.mSeekBar, this.mMasterViewLayout);
            return;
        }
        updatePlayViewState(t.isPlaying());
        this.mIsProcessingData = true;
        ((FilterMenuPresenter) this.mMenuPresenter).updateDataList(this.mAdapter.getEffects());
    }

    @Override // com.miui.gallery.vlog.filter.FilterMenuContract$IFilterMenuView
    public void updateSelectItem() {
        FilterMenuPresenter filterMenuPresenter = (FilterMenuPresenter) this.mMenuPresenter;
        int findFilterIndexByLabel = filterMenuPresenter.findFilterIndexByLabel(this.mAdapter.getEffects());
        int i = findFilterIndexByLabel == -1 ? 0 : findFilterIndexByLabel;
        this.mIsProcessingData = false;
        if (findFilterIndexByLabel == -1) {
            VlogUtils.hideViews(this.mSeekBar, this.mMasterViewLayout);
            this.mAdapter.setSelection(i);
            this.mAdapter.exitEditMode();
        } else {
            int currentFilterStrength = filterMenuPresenter.getCurrentFilterStrength();
            FilterData itemData = this.mAdapter.getItemData(i);
            if (itemData != null) {
                itemData.setProgress(currentFilterStrength);
            }
            this.mOnItemClickListener.OnItemClick(this.mRecyclerView, null, findFilterIndexByLabel);
        }
        this.mRecyclerView.scrollToPosition(i);
        this.mAdapter.notifyDataSetChanged();
        updateMasterViewStatus();
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    /* renamed from: createPresenter  reason: collision with other method in class */
    public FilterMenuPresenter mo1801createPresenter() {
        return new FilterMenuPresenter(this.mContext, this, this.mFilterZipFileConfig);
    }

    @Override // com.miui.gallery.vlog.filter.FilterMenuContract$IFilterMenuView
    public void loadRecyclerView(ArrayList<FilterData> arrayList) {
        FilterAdapter filterAdapter = new FilterAdapter(arrayList, getActivity().getResources().getColor(R$color.vlog_filter_item_high_color));
        this.mAdapter = filterAdapter;
        this.mRecyclerView.setAdapter(filterAdapter);
        this.mAdapter.setOnItemClickListener(this.mOnItemClickListener);
        this.mAdapter.setSelection(0);
        updateSelectItem();
    }

    public static boolean isFastClick() {
        long currentTimeMillis = System.currentTimeMillis();
        boolean z = currentTimeMillis - mLastClickTime < 300;
        mLastClickTime = currentTimeMillis;
        return z;
    }

    public final void notifyDateSetChanged(int i) {
        FilterAdapter filterAdapter = this.mAdapter;
        if (filterAdapter != null) {
            filterAdapter.notifyItemChanged(i, 1);
        }
    }

    @Override // com.miui.gallery.vlog.filter.FilterMenuContract$IFilterMenuView
    public void updatePlayViewState(boolean z) {
        Resources resources;
        int i;
        if (this.mPlayView.isSelected() != z) {
            this.mPlayView.setSelected(z);
        }
        ImageView imageView = this.mPlayView;
        if (z) {
            resources = getResources();
            i = R$string.vlog_talkback_view_pause;
        } else {
            resources = getResources();
            i = R$string.vlog_talkback_view_play;
        }
        imageView.setContentDescription(resources.getString(i));
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R$id.play_layout) {
            ((FilterMenuPresenter) FilterMenuPresenter.class.cast(this.mMenuPresenter)).doPlayViewClickEvent();
        } else if (view.getId() != R$id.layout_master || this.mMasterCloseView.isAnimating() || this.mMasterOpenView.isAnimating()) {
        } else {
            if (((FilterMenuPresenter) FilterMenuPresenter.class.cast(this.mMenuPresenter)).changeMasterStatus()) {
                this.mMasterCloseView.playAnimation();
                ToastUtils.makeText(getContext(), R$string.vlog_filter_master_open);
                return;
            }
            this.mMasterOpenView.playAnimation();
            ToastUtils.makeText(getContext(), R$string.vlog_filter_master_close);
        }
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void onPlaybackStopped() {
        if (!this.mMenuPresenter.isPlaying()) {
            updatePlayViewState(false);
        }
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void onPlaybackEOF() {
        if (!this.mMenuPresenter.isPlaying()) {
            updatePlayViewState(false);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        FilterAdapter filterAdapter = this.mAdapter;
        if (filterAdapter != null) {
            filterAdapter.setOnItemClickListener(null);
            this.mAdapter = null;
        }
        this.mMenuPresenter.destroy();
    }
}
