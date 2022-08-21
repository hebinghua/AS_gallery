package com.miui.gallery.ui.album.otheralbum.base;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.epoxy.EpoxyModel;
import com.miui.gallery.R;
import com.miui.gallery.model.dto.BaseAlbumCover;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.ui.album.common.BaseAlbumPageView;
import com.miui.gallery.ui.album.common.CommonAlbumItemViewBean;
import com.miui.gallery.ui.album.common.CustomViewItemViewBean;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;
import com.miui.gallery.ui.album.otheralbum.base.BaseOtherAlbumPresenter;
import com.miui.gallery.ui.album.otheralbum.grid.OtherAlbumGridPageView;
import com.miui.gallery.ui.album.otheralbum.linear.OtherAlbumLinearPageView;
import com.miui.gallery.util.DimensionUtils;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.ViewUtils;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.glide.BindImageHelper;
import com.miui.gallery.util.imageloader.imageloadiotion.AlbumImageLoadOptions;
import java.util.List;

/* loaded from: classes2.dex */
public class BaseOtherAlbumFragment<P extends BaseOtherAlbumPresenter> extends BaseOtherAlbumContract$V<P> {
    public LinearLayoutManager mLayoutManager;
    public BaseAlbumPageView mPageView;
    public View.OnClickListener mRubbishTipClickListener;
    public ConstraintLayout mRubbishTipView;
    public CustomViewItemViewBean mRubbishTipViewBean;
    public long mRubbishTipViewId;
    public int mRubbishTipViewMarginBottom;
    public int mRubbishTipViewMarginStartAndEnd;
    public int mRubbishTipViewPaddingStartAndEnd;
    public boolean mIsNeedSrollToHead = true;
    public final Runnable mRubbishTipViewRefreshRunnable = new Runnable() { // from class: com.miui.gallery.ui.album.otheralbum.base.BaseOtherAlbumFragment.1
        /* JADX WARN: Type inference failed for: r1v25, types: [androidx.recyclerview.widget.RecyclerView$ViewHolder, com.miui.epoxy.EpoxyViewHolder] */
        @Override // java.lang.Runnable
        public void run() {
            int findLastVisibleItemPosition;
            if (BaseOtherAlbumFragment.this.mAdapter == null) {
                return;
            }
            int dataSize = BaseOtherAlbumFragment.this.getDataSize();
            int childCount = BaseOtherAlbumFragment.this.mLayoutManager.getChildCount();
            if (childCount < dataSize) {
                if (BaseOtherAlbumFragment.this.mRubbishTipViewBean != null && BaseOtherAlbumFragment.this.mAdapter.getFooterModelById(BaseOtherAlbumFragment.this.mRubbishTipViewId) != null) {
                    return;
                }
                BaseOtherAlbumFragment.this.setRubbishTipViewVisible(true);
                BaseOtherAlbumFragment.this.mAdapter.addFooter(BaseOtherAlbumFragment.this.getRubbishTipModel());
                BaseOtherAlbumFragment.this.mRubbishTipView.setOnClickListener(BaseOtherAlbumFragment.this.getRubbishTipClickListener());
                return;
            }
            if (childCount == dataSize && (findLastVisibleItemPosition = BaseOtherAlbumFragment.this.mLayoutManager.findLastVisibleItemPosition()) > 0) {
                View findViewByPosition = BaseOtherAlbumFragment.this.mLayoutManager.findViewByPosition(findLastVisibleItemPosition);
                if (findViewByPosition == null) {
                    return;
                }
                if (findViewByPosition.getId() == R.id.custom_item_model_layout_view) {
                    findViewByPosition = BaseOtherAlbumFragment.this.mLayoutManager.findViewByPosition(findLastVisibleItemPosition - 1);
                }
                if (findViewByPosition == null) {
                    return;
                }
                if (BaseOtherAlbumFragment.this.mRecyclerView.getHeight() - findViewByPosition.getBottom() <= findViewByPosition.getHeight()) {
                    if (BaseOtherAlbumFragment.this.mRubbishTipViewBean != null && BaseOtherAlbumFragment.this.mAdapter.getFooterModelById(BaseOtherAlbumFragment.this.mRubbishTipViewId) != null) {
                        return;
                    }
                    BaseOtherAlbumFragment.this.setRubbishTipViewVisible(true);
                    BaseOtherAlbumFragment.this.mAdapter.addFooter(BaseOtherAlbumFragment.this.getRubbishTipModel());
                    BaseOtherAlbumFragment.this.mRubbishTipView.setOnClickListener(BaseOtherAlbumFragment.this.getRubbishTipClickListener());
                    return;
                }
            }
            ConstraintLayout constraintLayout = (ConstraintLayout) BaseOtherAlbumFragment.this.getView();
            if (constraintLayout == null || BaseOtherAlbumFragment.this.mRubbishTipView.getParent() == constraintLayout) {
                return;
            }
            if (BaseOtherAlbumFragment.this.mRecyclerView.hasPendingAdapterUpdates() || BaseOtherAlbumFragment.this.mAdapter.getDiffingDatas() != null || BaseOtherAlbumFragment.this.mRecyclerView.isInLayout()) {
                BaseOtherAlbumFragment.this.mRecyclerView.removeCallbacks(this);
                BaseOtherAlbumFragment.this.mRecyclerView.post(this);
                return;
            }
            if (BaseOtherAlbumFragment.this.mRubbishTipViewBean != null) {
                EpoxyModel<?> footerModelById = BaseOtherAlbumFragment.this.mAdapter.getFooterModelById(BaseOtherAlbumFragment.this.mRubbishTipViewId);
                if (footerModelById != null && footerModelById.getViewHolder() != null) {
                    footerModelById.getViewHolder().setIsRecyclable(false);
                }
                BaseOtherAlbumFragment.this.mAdapter.removeFooter(BaseOtherAlbumFragment.this.mRubbishTipViewBean);
            }
            ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(-1, -2);
            layoutParams.bottomToBottom = constraintLayout.getId();
            BaseOtherAlbumFragment.this.mRubbishTipView.setPadding(BaseOtherAlbumFragment.this.mRubbishTipViewPaddingStartAndEnd, 0, BaseOtherAlbumFragment.this.mRubbishTipViewPaddingStartAndEnd, 0);
            layoutParams.setMargins(BaseOtherAlbumFragment.this.mRubbishTipViewMarginStartAndEnd, 0, BaseOtherAlbumFragment.this.mRubbishTipViewMarginStartAndEnd, BaseOtherAlbumFragment.this.mRubbishTipViewMarginBottom);
            ViewUtils.removeParent(BaseOtherAlbumFragment.this.mRubbishTipView);
            constraintLayout.addView(BaseOtherAlbumFragment.this.mRubbishTipView, layoutParams);
            BaseOtherAlbumFragment.this.setRubbishTipViewVisible(true);
            BaseOtherAlbumFragment.this.mRubbishTipView.setOnClickListener(BaseOtherAlbumFragment.this.getRubbishTipClickListener());
        }
    };

    @Override // com.miui.gallery.app.base.BaseListPageFragment, com.miui.gallery.base_optimization.mvp.view.Fragment
    public int getLayoutId() {
        return R.layout.album_other_list_page;
    }

    @Override // com.miui.gallery.app.base.BaseFragment, com.miui.gallery.base_optimization.mvp.view.Fragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mPageView = AlbumPageConfig.getInstance().isGridPageMode() ? new OtherAlbumGridPageView(this) : new OtherAlbumLinearPageView(this);
    }

    @Override // com.miui.gallery.app.base.BaseListPageFragment
    public void initRecyclerView(RecyclerView recyclerView) {
        setEmptyPage(R.id.album_other_empty_view, getDefaultEmptyConfig().setTitle(R.string.empty_album_other_list_title));
        this.mPageView.onInitRecyclerView(recyclerView);
        this.mRubbishTipViewPaddingStartAndEnd = (int) DimensionUtils.getDimensionPixelSize(R.dimen.album_rubbish_tip_padding_start_end);
        this.mRubbishTipViewMarginStartAndEnd = (int) DimensionUtils.getDimensionPixelSize(R.dimen.album_rubbish_tip_margin_start_end);
        this.mRubbishTipViewMarginBottom = (int) DimensionUtils.getDimensionPixelSize(R.dimen.album_rubbish_tip_margin_bottom);
        this.mLayoutManager = (LinearLayoutManager) this.mPageView.getLayoutManager();
        addOnScrollListener(new RecyclerView.OnScrollListener() { // from class: com.miui.gallery.ui.album.otheralbum.base.BaseOtherAlbumFragment.2
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView2, int i) {
                super.onScrollStateChanged(recyclerView2, i);
                BaseOtherAlbumFragment.this.mIsNeedSrollToHead = false;
                BaseOtherAlbumFragment.this.mRecyclerView.removeOnScrollListener(this);
            }
        });
    }

    @Override // com.miui.gallery.ui.album.otheralbum.base.BaseOtherAlbumContract$V
    public void showOthersAlbumResult(List<CommonAlbumItemViewBean> list) {
        ConstraintLayout constraintLayout;
        if (list.size() > getDataSize() && (constraintLayout = this.mRubbishTipView) != null && constraintLayout.getParent() == getView()) {
            ViewUtils.removeParent(this.mRubbishTipView);
        }
        if (this.mIsNeedSrollToHead) {
            this.mRecyclerView.scrollToPosition(0);
        }
        setDatas(list);
    }

    @Override // com.miui.gallery.app.base.BaseListPageFragment
    public RecyclerView.ItemDecoration[] getRecyclerViewDecorations() {
        return this.mPageView.getRecyclerViewDecorations();
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        doOnActivityCreated();
    }

    public void doOnActivityCreated() {
        ((BaseOtherAlbumPresenter) getPresenter()).initData();
    }

    @Override // com.miui.gallery.ui.album.otheralbum.base.BaseOtherAlbumContract$V
    public void showRubbishAlbumResult(List<BaseAlbumCover> list) {
        if (list != null && list.isEmpty()) {
            openClipPadding();
            ConstraintLayout constraintLayout = this.mRubbishTipView;
            if (constraintLayout == null) {
                return;
            }
            constraintLayout.setVisibility(8);
            return;
        }
        closeClipPadding();
        fillRubbishTipViewImages(list);
        refreshRubbishTipView();
    }

    @Override // com.miui.gallery.app.base.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mPageView.onConfigurationChanged(configuration);
        refreshRubbishTipView();
    }

    @Override // com.miui.gallery.ui.album.otheralbum.base.BaseOtherAlbumContract$V
    public void refreshRubbishTipView() {
        if (this.mRubbishTipView != null) {
            this.mRecyclerView.post(this.mRubbishTipViewRefreshRunnable);
        }
    }

    public final View.OnClickListener getRubbishTipClickListener() {
        if (this.mRubbishTipClickListener == null) {
            this.mRubbishTipClickListener = new View.OnClickListener() { // from class: com.miui.gallery.ui.album.otheralbum.base.BaseOtherAlbumFragment.3
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    BaseOtherAlbumFragment.this.gotoRubbishAlbumPage(view);
                }
            };
        }
        return this.mRubbishTipClickListener;
    }

    public void gotoRubbishAlbumPage(View view) {
        IntentUtil.gotoRubbishAlbumPage(getSafeActivity());
    }

    public final void fillRubbishTipViewImages(List<BaseAlbumCover> list) {
        initRubbishTipViewIfNeed();
        int i = 0;
        while (i < 3) {
            Resources resources = getResources();
            StringBuilder sb = new StringBuilder();
            sb.append("ivImg");
            int i2 = i + 1;
            sb.append(i2);
            ImageView imageView = (ImageView) this.mRubbishTipView.findViewById(resources.getIdentifier(sb.toString(), "id", getSafeActivity().getPackageName()));
            if (i >= list.size()) {
                imageView.setVisibility(8);
            } else {
                BaseAlbumCover baseAlbumCover = list.get(i);
                imageView.setVisibility(0);
                BindImageHelper.bindImage(baseAlbumCover.coverPath, ((BaseOtherAlbumPresenter) getPresenter()).getDownLoadUri(baseAlbumCover), DownloadType.MICRO, imageView, AlbumImageLoadOptions.getInstance().getDefaultAlbumImageOptions(baseAlbumCover.coverSize));
            }
            i = i2;
        }
        this.mRubbishTipView.setVisibility(0);
    }

    public final CustomViewItemViewBean getRubbishTipModel() {
        ConstraintLayout constraintLayout;
        initRubbishTipViewIfNeed();
        if (this.mRubbishTipViewBean == null && (constraintLayout = this.mRubbishTipView) != null) {
            this.mRubbishTipViewId = constraintLayout.getId();
            this.mRubbishTipViewBean = new CustomViewItemViewBean(this.mRubbishTipView, new CustomViewItemViewBean.CustomViewLayoutParamConfig(new int[]{0, 0, 0, this.mRubbishTipViewMarginBottom - ((int) DimensionUtils.getDimensionPixelSize(R.dimen.album_grid_veritily_space))}, -1, -2), true);
        }
        if (this.mRubbishTipView.getParent() != null) {
            ViewUtils.removeParent(this.mRubbishTipView);
        }
        return this.mRubbishTipViewBean;
    }

    public View initRubbishTipViewIfNeed() {
        if (this.mRubbishTipView == null) {
            ConstraintLayout constraintLayout = (ConstraintLayout) LayoutInflater.from(getSafeActivity()).inflate(R.layout.layout_rubbish_tip, (ViewGroup) null);
            this.mRubbishTipView = constraintLayout;
            FolmeUtil.setDefaultTouchAnim(constraintLayout, null, false, false, true);
        }
        return this.mRubbishTipView;
    }

    public final void closeClipPadding() {
        if (this.mRecyclerView == null || getRecyclerViewPadding() == null || this.mRecyclerView == null) {
            return;
        }
        int[] recyclerViewPadding = getRecyclerViewPadding();
        this.mRecyclerView.setPadding(recyclerViewPadding[0], recyclerViewPadding[1], recyclerViewPadding[2], 0);
        this.mRecyclerView.setClipToPadding(true);
    }

    public final void openClipPadding() {
        if (getRecyclerViewPadding() == null || this.mRecyclerView == null) {
            return;
        }
        int[] recyclerViewPadding = getRecyclerViewPadding();
        this.mRecyclerView.setPadding(recyclerViewPadding[0], recyclerViewPadding[1], recyclerViewPadding[2], recyclerViewPadding[3]);
        this.mRecyclerView.setClipToPadding(false);
    }

    public void setRubbishTipViewVisible(boolean z) {
        ConstraintLayout constraintLayout = this.mRubbishTipView;
        if (constraintLayout != null) {
            constraintLayout.setVisibility(z ? 0 : 4);
        }
    }
}
