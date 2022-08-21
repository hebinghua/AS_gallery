package com.miui.gallery.vlog.clip;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.vlog.R$dimen;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.base.stat.VlogStatUtils;
import com.miui.gallery.vlog.clip.DeleteDialogFragment;
import com.miui.gallery.vlog.home.VlogConfig;
import com.miui.gallery.vlog.sdk.interfaces.IVideoClip;
import com.miui.gallery.vlog.tools.VlogUtils;
import com.miui.gallery.widget.recyclerview.SingleChoiceRecyclerView;

/* loaded from: classes2.dex */
public class ClipEditNavView extends FrameLayout implements View.OnClickListener {
    public ClipEditNavAdapter mAdapter;
    public ViewGroup mBackLayout;
    public ViewTreeObserver.OnGlobalLayoutListener mBackLayoutListener;
    public DeleteDialogFragment.DialogClickListener mClickListener;
    public Context mContext;
    public ClipMenuContract$IClipMenuView mIClipMenuView;
    public LinearLayoutManager mLinearLayoutManager;
    public SingleChoiceRecyclerView mRecyclerView;
    public DeleteDialogFragment mScreenDeleteDialogFragment;

    public ClipEditNavView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mClickListener = new DeleteDialogFragment.DialogClickListener() { // from class: com.miui.gallery.vlog.clip.ClipEditNavView.1
            @Override // com.miui.gallery.vlog.clip.DeleteDialogFragment.DialogClickListener
            public void onDelete() {
                ClipEditNavView.this.mIClipMenuView.getPresenter().doDelete();
            }
        };
        this.mBackLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.miui.gallery.vlog.clip.ClipEditNavView.3
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public void onGlobalLayout() {
                ClipEditNavView.this.mBackLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                ClipEditNavView.this.updateRecyclerView();
            }
        };
        init(context);
    }

    public final void init(Context context) {
        this.mContext = context;
        FrameLayout.inflate(context, R$layout.vlog_clip_edit_nav_layout, this);
        ViewGroup viewGroup = (ViewGroup) findViewById(R$id.back_layout);
        this.mBackLayout = viewGroup;
        viewGroup.setOnClickListener(this);
        initRecyclerView();
        if (this.mScreenDeleteDialogFragment == null) {
            this.mScreenDeleteDialogFragment = new DeleteDialogFragment();
        }
        FolmeUtil.setCustomTouchAnim(this.mBackLayout, new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build(), null, null, true);
    }

    public void setIClipMenuView(ClipMenuContract$IClipMenuView clipMenuContract$IClipMenuView) {
        this.mIClipMenuView = clipMenuContract$IClipMenuView;
    }

    public void updateSpeedXItemState(IVideoClip iVideoClip) {
        this.mAdapter.updateSpeedXItemState(iVideoClip);
    }

    public final void initRecyclerView() {
        this.mRecyclerView = (SingleChoiceRecyclerView) findViewById(R$id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.mContext, 0, false) { // from class: com.miui.gallery.vlog.clip.ClipEditNavView.2
            @Override // androidx.recyclerview.widget.LinearLayoutManager, androidx.recyclerview.widget.RecyclerView.LayoutManager
            public boolean canScrollHorizontally() {
                return false;
            }
        };
        this.mLinearLayoutManager = linearLayoutManager;
        this.mRecyclerView.setLayoutManager(linearLayoutManager);
        ClipEditNavAdapter clipEditNavAdapter = new ClipEditNavAdapter(this.mContext, VlogConfig.getClipEditData());
        this.mAdapter = clipEditNavAdapter;
        clipEditNavAdapter.setSelectedItemPosition(-1);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mAdapter.setItemSelectChangeListener(new ClipEditNavItemListener());
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view == this.mBackLayout) {
            this.mIClipMenuView.exitEditMode();
            VlogUtils.hideViews(this);
        }
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        ViewGroup.LayoutParams layoutParams = this.mBackLayout.getLayoutParams();
        layoutParams.width = this.mContext.getResources().getDimensionPixelSize(R$dimen.clip_edit_nav_back_width);
        this.mBackLayout.setLayoutParams(layoutParams);
        this.mBackLayout.getViewTreeObserver().addOnGlobalLayoutListener(this.mBackLayoutListener);
    }

    public void updateDeleteItemState() {
        ClipEditNavAdapter clipEditNavAdapter = this.mAdapter;
        if (clipEditNavAdapter == null) {
            return;
        }
        boolean z = true;
        if (this.mIClipMenuView.getPresenter().getVideoClips().size() <= 1) {
            z = false;
        }
        clipEditNavAdapter.setEnableDelete(z);
    }

    public void updateRecyclerView() {
        this.mAdapter.updateItemWidth();
    }

    public void updateCutItemState(IVideoClip iVideoClip) {
        ClipEditNavAdapter clipEditNavAdapter = this.mAdapter;
        if (clipEditNavAdapter == null) {
            return;
        }
        clipEditNavAdapter.updateCutItemState(iVideoClip);
    }

    public void updateReverseItemState(IVideoClip iVideoClip) {
        ClipEditNavItem item;
        if (iVideoClip == null || (item = this.mAdapter.getItem(2)) == null) {
            return;
        }
        int i = iVideoClip.isInReverse() ? R$string.vlog_clip_is_reversed : R$string.clip_menu_reveerse;
        if (i == item.getTitleResId()) {
            return;
        }
        item.setTitleResId(i);
        this.mAdapter.notifyItemChanged(2);
    }

    /* loaded from: classes2.dex */
    public class ClipEditNavItemListener implements SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter.ItemSelectChangeListener {
        public ClipEditNavItemListener() {
        }

        @Override // com.miui.gallery.widget.recyclerview.SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter.ItemSelectChangeListener
        public boolean onItemSelect(SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter singleChoiceRecyclerViewAdapter, int i, boolean z) {
            singleChoiceRecyclerViewAdapter.clearLastSelectedPostion();
            singleChoiceRecyclerViewAdapter.setSelectedItemPosition(i);
            ClipEditNavItem selectedItem = ((ClipEditNavAdapter) singleChoiceRecyclerViewAdapter).getSelectedItem();
            if (!selectedItem.getType().equals("type_speed") || ClipEditNavView.this.mIClipMenuView.isSpeedViewVisible()) {
                ClipEditNavView.this.mIClipMenuView.setSpeedViewVisible(false);
            } else {
                ClipEditNavView.this.mIClipMenuView.setSpeedViewVisible(true);
            }
            if (selectedItem.getType().equals("type_cut")) {
                ClipEditNavView.this.mIClipMenuView.getPresenter().doCut();
            } else if (selectedItem.getType().equals("type_speed") && ClipEditNavView.this.mIClipMenuView.isSpeedViewVisible()) {
                ClipEditNavView.this.mIClipMenuView.getPresenter().readySpeed();
                IVideoClip currentVideoClip = ClipEditNavView.this.mIClipMenuView.getPresenter().getCurrentVideoClip();
                ClipEditNavView.this.updateSpeedXItemState(null);
                if (currentVideoClip != null) {
                    ClipEditNavView.this.mIClipMenuView.setSpeed(currentVideoClip.getSpeed());
                    ClipEditNavView.this.mIClipMenuView.setSpeedItemEnable(currentVideoClip.getTimelineDuration());
                }
            } else if (selectedItem.getType().equals("type_reverse")) {
                ClipEditNavView.this.mIClipMenuView.getPresenter().doReverse();
            } else if (selectedItem.getType().equals("type_delete") && ClipEditNavView.this.mAdapter.isEnableDelete()) {
                ClipEditNavView.this.mScreenDeleteDialogFragment.setDialogClickListener(ClipEditNavView.this.mClickListener);
                if (!ClipEditNavView.this.mScreenDeleteDialogFragment.isAdded()) {
                    ClipEditNavView.this.mScreenDeleteDialogFragment.showAllowingStateLoss(ClipEditNavView.this.mIClipMenuView.getClipFragmentManager(), "DeleteDialogFragment");
                }
            }
            VlogStatUtils.statEvent("clip_nav", selectedItem.getType());
            return false;
        }
    }
}
