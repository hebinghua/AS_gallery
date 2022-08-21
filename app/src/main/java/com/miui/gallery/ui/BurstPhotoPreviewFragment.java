package com.miui.gallery.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.baidu.platform.comapi.UIMsg;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.R;
import com.miui.gallery.app.fragment.GalleryFragment;
import com.miui.gallery.glide.GlideOptions;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.BaseDataSet;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.ui.BurstPhotoPreviewFragment;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.VideoPlayerCompat;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.cloudimageloader.CloudUriAdapter;
import com.miui.gallery.util.glide.BindImageHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.PagerIndicator;
import com.miui.gallery.widget.ViewUtils;
import com.miui.gallery.widget.overscroll.HorizontalOverScrollBounceEffectDecorator;
import com.miui.gallery.widget.recyclerview.BaseViewHolder;
import com.miui.gallery.widget.recyclerview.BlankDivider;
import com.miui.gallery.widget.recyclerview.CustomScrollerLinearLayoutManager;
import java.util.ArrayList;
import java.util.List;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes2.dex */
public class BurstPhotoPreviewFragment extends GalleryFragment {
    public PreviewViewAdapter mAdapter;
    public BlankDivider mBlankDivider;
    public BaseDataSet mContentDataSet;
    public View mDiscard;
    public View mIndicator;
    public OnExitListener mOnExitListener;
    public OnScrollToPositionListener mOnItemScrolledListener;
    public RecyclerView mPreviewRecyclerView;
    public View mSave;
    public TextView mTitle;
    public boolean mIsScrolledByOutside = true;
    public final float PREVIEW_SCALE = 1.18f;
    public View.OnClickListener mOnSaveClickedListener = new View.OnClickListener() { // from class: com.miui.gallery.ui.BurstPhotoPreviewFragment.1
        {
            BurstPhotoPreviewFragment.this = this;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (BurstPhotoPreviewFragment.this.mOnExitListener != null) {
                BurstPhotoPreviewFragment.this.mOnExitListener.onSave();
            }
        }
    };
    public View.OnClickListener mOnDiscardClickedListener = new View.OnClickListener() { // from class: com.miui.gallery.ui.BurstPhotoPreviewFragment.2
        {
            BurstPhotoPreviewFragment.this = this;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            BurstPhotoPreviewFragment.this.discard();
        }
    };

    /* loaded from: classes2.dex */
    public interface OnExitListener {
        void onDiscard();

        void onSave();
    }

    /* loaded from: classes2.dex */
    public interface OnScrollToPositionListener {
        void onScrollToPosition(int i);
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.burst_photo_preview, viewGroup, false);
        View findViewById = inflate.findViewById(R.id.save);
        this.mSave = findViewById;
        findViewById.setOnClickListener(this.mOnSaveClickedListener);
        View findViewById2 = inflate.findViewById(R.id.discard);
        this.mDiscard = findViewById2;
        findViewById2.setOnClickListener(this.mOnDiscardClickedListener);
        this.mTitle = (TextView) inflate.findViewById(R.id.choice_title);
        View findViewById3 = inflate.findViewById(R.id.indicator);
        this.mIndicator = findViewById3;
        ((PagerIndicator) findViewById3).showIndex(0, 1);
        this.mPreviewRecyclerView = (RecyclerView) inflate.findViewById(R.id.preview_view);
        BurstScrollControlLinearLayoutManager burstScrollControlLinearLayoutManager = new BurstScrollControlLinearLayoutManager(getActivity());
        burstScrollControlLinearLayoutManager.setOrientation(0);
        this.mPreviewRecyclerView.setLayoutManager(burstScrollControlLinearLayoutManager);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.burst_preview_decoration_width);
        int curScreenWidth = (((ScreenUtils.getCurScreenWidth() / 2) - (getResources().getDimensionPixelSize(R.dimen.burst_preview_width) / 2)) - (getResources().getDimensionPixelSize(R.dimen.burst_preview_decoration_width) * 2)) + (dimensionPixelSize * 2);
        BlankDivider blankDivider = new BlankDivider(curScreenWidth, curScreenWidth, dimensionPixelSize, 0, 0);
        this.mBlankDivider = blankDivider;
        this.mPreviewRecyclerView.addItemDecoration(blankDivider);
        PreviewViewAdapter previewViewAdapter = new PreviewViewAdapter(getActivity());
        this.mAdapter = previewViewAdapter;
        BaseDataSet baseDataSet = this.mContentDataSet;
        if (baseDataSet != null) {
            previewViewAdapter.setDateSet(baseDataSet);
        }
        this.mPreviewRecyclerView.setAdapter(this.mAdapter);
        this.mPreviewRecyclerView.addOnScrollListener(new BurstOnScrollListener());
        HorizontalOverScrollBounceEffectDecorator.setOverScrollEffect(this.mPreviewRecyclerView);
        return inflate;
    }

    @Override // com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int curScreenWidth = (((ScreenUtils.getCurScreenWidth() / 2) - (getResources().getDimensionPixelSize(R.dimen.burst_preview_width) / 2)) - (getResources().getDimensionPixelSize(R.dimen.burst_preview_decoration_width) * 2)) + (getResources().getDimensionPixelSize(R.dimen.burst_preview_decoration_width) * 2);
        this.mBlankDivider.updateItemDecorationStartEnd(curScreenWidth, curScreenWidth);
        this.mPreviewRecyclerView.invalidateItemDecorations();
    }

    public void setDataSet(BaseDataSet baseDataSet) {
        this.mContentDataSet = baseDataSet;
        PreviewViewAdapter previewViewAdapter = this.mAdapter;
        if (previewViewAdapter != null) {
            previewViewAdapter.setDateSet(baseDataSet);
        }
    }

    public void scrollToPosition(int i) {
        RecyclerView recyclerView;
        if (!this.mIsScrolledByOutside || (recyclerView = this.mPreviewRecyclerView) == null) {
            return;
        }
        recyclerView.smoothScrollToPosition(i);
    }

    public void setCheckedItem(int i, boolean z, boolean z2) {
        this.mAdapter.setSelectDataPosition(i, z);
        this.mSave.setEnabled(z2);
    }

    public void setOnItemScrolledListener(OnScrollToPositionListener onScrollToPositionListener) {
        this.mOnItemScrolledListener = onScrollToPositionListener;
    }

    public void setOnExitListener(OnExitListener onExitListener) {
        this.mOnExitListener = onExitListener;
    }

    public void discard() {
        OnExitListener onExitListener = this.mOnExitListener;
        if (onExitListener != null) {
            onExitListener.onDiscard();
        }
        this.mPreviewRecyclerView.stopScroll();
    }

    /* loaded from: classes2.dex */
    public class PreviewViewAdapter extends RecyclerView.Adapter<BaseViewHolder> {
        public Context mContext;
        public BaseDataSet mDataSet;
        public int mItemHeight;
        public int mItemWidth;
        public List<Integer> mSelectDataPos = new ArrayList();
        public RequestOptions mDisplayOptions = GlideOptions.microThumbOf().autoClone();

        public PreviewViewAdapter(Context context) {
            BurstPhotoPreviewFragment.this = r2;
            this.mContext = context;
            this.mItemWidth = r2.getActivity().getResources().getDimensionPixelSize(R.dimen.burst_preview_width);
            this.mItemHeight = r2.getActivity().getResources().getDimensionPixelSize(R.dimen.burst_preview_height);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* renamed from: onCreateViewHolder */
        public BaseViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new PreviewViewHolder(LayoutInflater.from(this.mContext).inflate(R.layout.burst_preview_item, viewGroup, false));
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(BaseViewHolder baseViewHolder, int i) {
            baseViewHolder.itemView.setTag(Integer.valueOf(i));
            BaseDataSet baseDataSet = this.mDataSet;
            if (baseDataSet == null) {
                return;
            }
            Uri uri = null;
            BaseDataItem item = baseDataSet.getItem(null, i);
            long key = item.getKey();
            String microPath = item.getOriginalPath() == null ? item.getThumnailPath() == null ? item.getMicroPath() : item.getThumnailPath() : item.getOriginalPath();
            if (item.isSynced()) {
                uri = CloudUriAdapter.getDownloadUri(key);
            }
            Uri uri2 = uri;
            if (!(baseViewHolder instanceof PreviewViewHolder)) {
                return;
            }
            ((PreviewViewHolder) baseViewHolder).bindImage(this.mSelectDataPos.contains(Integer.valueOf(i)), microPath, uri2, this.mItemWidth, this.mItemHeight, this.mDisplayOptions);
        }

        public void validateItems(int i) {
            for (int i2 = 0; i2 < getItemCount(); i2++) {
                if (i2 != i && i2 != i - 1 && i2 != i + 1) {
                    BaseViewHolder baseViewHolder = (BaseViewHolder) BurstPhotoPreviewFragment.this.mPreviewRecyclerView.findViewHolderForAdapterPosition(i2);
                    if (baseViewHolder instanceof PreviewViewHolder) {
                        ((PreviewViewHolder) baseViewHolder).validateImage(this.mItemWidth, this.mItemHeight);
                    }
                }
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            BaseDataSet baseDataSet = this.mDataSet;
            if (baseDataSet != null) {
                return baseDataSet.getCount();
            }
            return 0;
        }

        public void setDateSet(BaseDataSet baseDataSet) {
            this.mDataSet = baseDataSet;
            notifyDataSetChanged();
        }

        public void setSelectDataPosition(int i, boolean z) {
            if (z) {
                this.mSelectDataPos.add(Integer.valueOf(i));
            } else if (this.mSelectDataPos.contains(Integer.valueOf(i))) {
                this.mSelectDataPos.remove(Integer.valueOf(i));
            }
            BaseViewHolder baseViewHolder = (BaseViewHolder) BurstPhotoPreviewFragment.this.mPreviewRecyclerView.findViewHolderForAdapterPosition(i);
            if (baseViewHolder instanceof PreviewViewHolder) {
                ((PreviewViewHolder) baseViewHolder).setSelect(z);
            }
        }

        public int getSelectCount() {
            return this.mSelectDataPos.size();
        }
    }

    /* loaded from: classes2.dex */
    public class PreviewViewHolder extends BaseViewHolder {
        public ImageView mPreview;
        public View mSelectView;

        public static /* synthetic */ void $r8$lambda$biXCxT6Q7Q4j73mcNZNBXfwkVOI(PreviewViewHolder previewViewHolder, View view) {
            previewViewHolder.lambda$bindImage$0(view);
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public PreviewViewHolder(View view) {
            super(view);
            BurstPhotoPreviewFragment.this = r1;
            this.mPreview = (ImageView) view.findViewById(R.id.preview);
            this.mSelectView = view.findViewById(R.id.select_view);
        }

        public void bindImage(boolean z, String str, Uri uri, int i, int i2, RequestOptions requestOptions) {
            setSelect(z);
            this.mPreview.setLayoutParams(new FrameLayout.LayoutParams(i, i2, 80));
            BindImageHelper.bindImage(str, uri, DownloadType.MICRO, this.mPreview, requestOptions.mo971override(i, i2));
            this.itemView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.BurstPhotoPreviewFragment$PreviewViewHolder$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    BurstPhotoPreviewFragment.PreviewViewHolder.$r8$lambda$biXCxT6Q7Q4j73mcNZNBXfwkVOI(BurstPhotoPreviewFragment.PreviewViewHolder.this, view);
                }
            });
        }

        public /* synthetic */ void lambda$bindImage$0(View view) {
            BurstPhotoPreviewFragment.this.scrollToPosition(((Integer) this.itemView.getTag()).intValue());
        }

        public void setSelect(boolean z) {
            if (z) {
                FolmeUtil.setCustomVisibleAnim(this.mSelectView, true, null, null);
            } else {
                FolmeUtil.setCustomVisibleAnim(this.mSelectView, false, null, null);
            }
            int itemCount = BurstPhotoPreviewFragment.this.mAdapter.getItemCount();
            BurstPhotoPreviewFragment.this.mTitle.setText(BurstPhotoPreviewFragment.this.getString(R.string.burst_save_choice_title, Integer.valueOf(BurstPhotoPreviewFragment.this.mAdapter.getSelectCount()), Integer.valueOf(itemCount)));
        }

        public void validateImage(int i, int i2) {
            this.mPreview.setLayoutParams(new FrameLayout.LayoutParams(i, i2, 80));
        }
    }

    /* loaded from: classes2.dex */
    public static class BurstScrollControlLinearLayoutManager extends CustomScrollerLinearLayoutManager {
        public BurstScrollControlLinearLayoutManager(Context context) {
            super(context);
        }

        @Override // com.miui.gallery.widget.recyclerview.CustomScrollerLinearLayoutManager, androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int i) {
            BurstLinearLayoutManager burstLinearLayoutManager = new BurstLinearLayoutManager(recyclerView.getContext());
            burstLinearLayoutManager.setTargetPosition(i);
            startSmoothScroll(burstLinearLayoutManager);
        }
    }

    /* loaded from: classes2.dex */
    public class BurstOnScrollListener extends RecyclerView.OnScrollListener {
        public BurstOnScrollListener() {
            BurstPhotoPreviewFragment.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            FragmentActivity activity = BurstPhotoPreviewFragment.this.getActivity();
            if (activity == null || activity.isDestroyed()) {
                DefaultLogger.w("BurstPhotoPreviewFragment", "Scroll when activity is destroyed");
            } else if (i == 1) {
                BurstPhotoPreviewFragment.this.mIsScrolledByOutside = false;
            } else if (i != 0) {
            } else {
                int currentPosition = getCurrentPosition(recyclerView);
                if (isScrollPositionValid(currentPosition)) {
                    BurstPhotoPreviewFragment.this.mPreviewRecyclerView.smoothScrollToPosition(currentPosition);
                    if (BurstPhotoPreviewFragment.this.mOnItemScrolledListener != null) {
                        BurstPhotoPreviewFragment.this.mOnItemScrolledListener.onScrollToPosition(currentPosition);
                    }
                }
                BurstPhotoPreviewFragment.this.mIsScrolledByOutside = true;
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            super.onScrolled(BurstPhotoPreviewFragment.this.mPreviewRecyclerView, i, i2);
            int currentPosition = getCurrentPosition(recyclerView);
            if (BurstPhotoPreviewFragment.this.mOnItemScrolledListener != null && !BurstPhotoPreviewFragment.this.mIsScrolledByOutside && isScrollPositionValid(currentPosition)) {
                BurstPhotoPreviewFragment.this.mOnItemScrolledListener.onScrollToPosition(currentPosition);
            }
            setAnimationPlayer(currentPosition);
            BurstPhotoPreviewFragment.this.mAdapter.validateItems(currentPosition);
        }

        public void setAnimationPlayer(int i) {
            View view = null;
            View findViewByPosition = i > 0 ? BurstPhotoPreviewFragment.this.mPreviewRecyclerView.getLayoutManager().findViewByPosition(i - 1) : null;
            View findViewByPosition2 = BurstPhotoPreviewFragment.this.mPreviewRecyclerView.getLayoutManager().findViewByPosition(i);
            if (i < BurstPhotoPreviewFragment.this.mPreviewRecyclerView.getAdapter().getItemCount() - 1) {
                view = BurstPhotoPreviewFragment.this.mPreviewRecyclerView.getLayoutManager().findViewByPosition(i + 1);
            }
            if (findViewByPosition2 != null) {
                float curScreenWidth = ((ScreenUtils.getCurScreenWidth() / 2) - findViewByPosition2.getLeft()) - (findViewByPosition2.getWidth() / 2);
                if (Math.abs(curScreenWidth) >= (BurstPhotoPreviewFragment.this.getResources().getDimensionPixelSize(R.dimen.burst_preview_width) + BurstPhotoPreviewFragment.this.getResources().getDimensionPixelSize(R.dimen.burst_preview_decoration_width)) / 2) {
                    return;
                }
                if (curScreenWidth < 0.0f) {
                    if (findViewByPosition != null) {
                        findViewByPosition.setPivotY(findViewByPosition.getHeight());
                        ViewUtils.setViewLayoutParams((ImageView) findViewByPosition.findViewById(R.id.preview), BurstPhotoPreviewFragment.this.mAdapter.mItemWidth, Math.round(BurstPhotoPreviewFragment.this.getActivity().getResources().getDimensionPixelSize(R.dimen.burst_preview_height) + (Math.abs(curScreenWidth) * 0.17999995f)));
                    }
                } else if (view != null) {
                    view.setPivotY(view.getHeight());
                    ViewUtils.setViewLayoutParams((ImageView) view.findViewById(R.id.preview), BurstPhotoPreviewFragment.this.mAdapter.mItemWidth, Math.round(BurstPhotoPreviewFragment.this.getActivity().getResources().getDimensionPixelSize(R.dimen.burst_preview_height) + (curScreenWidth * 0.17999995f)));
                }
                findViewByPosition2.setPivotY(findViewByPosition2.getHeight());
                ViewUtils.setViewLayoutParams((ImageView) findViewByPosition2.findViewById(R.id.preview), BurstPhotoPreviewFragment.this.mAdapter.mItemWidth, Math.round(BurstPhotoPreviewFragment.this.getResources().getDimensionPixelSize(R.dimen.burst_preview_item_frame_height) - (Math.abs(curScreenWidth) * 0.17999995f)));
            }
        }

        public final boolean isScrollPositionValid(int i) {
            return i >= 0 && i < BurstPhotoPreviewFragment.this.mAdapter.getItemCount();
        }

        public final int getCurrentPosition(RecyclerView recyclerView) {
            int left = (BurstPhotoPreviewFragment.this.mIndicator.getLeft() + BurstPhotoPreviewFragment.this.mIndicator.getRight()) / 2;
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            int itemCount = layoutManager.getItemCount();
            int i = Integer.MAX_VALUE;
            int i2 = -1;
            for (int i3 = 0; i3 < itemCount; i3++) {
                View findViewByPosition = layoutManager.findViewByPosition(i3);
                if (findViewByPosition != null) {
                    int abs = Math.abs(((findViewByPosition.getLeft() + findViewByPosition.getRight()) / 2) - left);
                    if (abs >= i) {
                        break;
                    }
                    i2 = i3;
                    i = abs;
                }
            }
            return i2;
        }
    }

    /* loaded from: classes2.dex */
    public static class BurstLinearLayoutManager extends StartEndSmoothScrollerController {
        public BurstLinearLayoutManager(Context context) {
            super(context);
        }

        @Override // com.miui.gallery.ui.StartEndSmoothScrollerController, androidx.recyclerview.widget.LinearSmoothScroller, androidx.recyclerview.widget.RecyclerView.SmoothScroller
        public void onTargetFound(View view, RecyclerView.State state, RecyclerView.SmoothScroller.Action action) {
            if (getLayoutManager() == null) {
                return;
            }
            int curScreenWidth = (ScreenUtils.getCurScreenWidth() / 2) - ((view.getLeft() + view.getRight()) / 2);
            if (Math.abs(curScreenWidth) <= this.mMinDistance) {
                return;
            }
            action.update(-curScreenWidth, 0, UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME, new CubicEaseOutInterpolator());
        }
    }

    @Override // com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.strategy.IStrategyFollower
    public boolean isSupportCutoutModeShortEdges() {
        return VideoPlayerCompat.isVideoPlayerSupportCutoutModeShortEdges();
    }
}
