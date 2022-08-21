package com.miui.gallery.ui.album.main.base;

import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.ui.Checkable;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;
import com.miui.gallery.ui.album.main.base.config.BaseAlbumPageStyle;
import com.miui.gallery.util.AlbumSortHelper;
import com.miui.gallery.widget.GalleryDialogFragment;
import com.miui.gallery.widget.editwrapper.EditableListViewWrapper;
import com.miui.itemdrag.RecyclerViewDragItemManager;

/* loaded from: classes2.dex */
public abstract class BaseAlbumTabPageView extends BaseAlbumListPageView {
    public boolean isHaveDelayRunnable;
    public boolean isSetDragItemOffsetWhenStartDrag;
    public final Runnable mCheckIsNeedSetDragItemOffsetWhenStartDrag;
    public RecyclerView.OnChildAttachStateChangeListener mChildAttachStateChangeListener;
    public RecyclerViewDragItemManager mDragItemManager;
    public EditableListViewWrapper mRecyclerViewWrapper;

    public abstract BaseAlbumPageStyle getStyle();

    public abstract void initChoiceMode(EditableListViewWrapper editableListViewWrapper);

    public abstract RecyclerViewDragItemManager initDragMode(RecyclerViewDragItemManager.Config.Builder builder);

    public void onEnterDragMode() {
    }

    public BaseAlbumTabPageView(BaseAlbumTabContract$V baseAlbumTabContract$V) {
        super(baseAlbumTabContract$V);
        this.mCheckIsNeedSetDragItemOffsetWhenStartDrag = new Runnable() { // from class: com.miui.gallery.ui.album.main.base.BaseAlbumTabPageView.2
            @Override // java.lang.Runnable
            public void run() {
                BaseAlbumTabPageView baseAlbumTabPageView = BaseAlbumTabPageView.this;
                baseAlbumTabPageView.isSetDragItemOffsetWhenStartDrag = baseAlbumTabPageView.isInChoiceMode() && !BaseAlbumTabPageView.this.isInMoveMode();
                BaseAlbumTabPageView.this.isHaveDelayRunnable = false;
            }
        };
        init(null);
    }

    public BaseAlbumTabPageView(BaseAlbumTabPageView baseAlbumTabPageView) {
        super(baseAlbumTabPageView.getParent());
        this.mCheckIsNeedSetDragItemOffsetWhenStartDrag = new Runnable() { // from class: com.miui.gallery.ui.album.main.base.BaseAlbumTabPageView.2
            @Override // java.lang.Runnable
            public void run() {
                BaseAlbumTabPageView baseAlbumTabPageView2 = BaseAlbumTabPageView.this;
                baseAlbumTabPageView2.isSetDragItemOffsetWhenStartDrag = baseAlbumTabPageView2.isInChoiceMode() && !BaseAlbumTabPageView.this.isInMoveMode();
                BaseAlbumTabPageView.this.isHaveDelayRunnable = false;
            }
        };
        init(baseAlbumTabPageView);
    }

    public RecyclerViewDragItemManager getDragItemManager() {
        return this.mDragItemManager;
    }

    public EditableListViewWrapper getRecyclerViewWrapper() {
        return this.mRecyclerViewWrapper;
    }

    public void init(BaseAlbumTabPageView baseAlbumTabPageView) {
        if (baseAlbumTabPageView != null) {
            this.mRecyclerViewWrapper = baseAlbumTabPageView.getRecyclerViewWrapper();
            RecyclerViewDragItemManager dragItemManager = baseAlbumTabPageView.getDragItemManager();
            this.mDragItemManager = dragItemManager;
            dragItemManager.setConfig(initDragMode(new RecyclerViewDragItemManager.Config.Builder(dragItemManager.getConfig().getDragCallback())).getConfig());
        }
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumListPageView
    public void onInitRecyclerView(final RecyclerView recyclerView) {
        super.onInitRecyclerView(recyclerView);
        if (this.mChildAttachStateChangeListener == null) {
            this.mChildAttachStateChangeListener = new RecyclerView.OnChildAttachStateChangeListener() { // from class: com.miui.gallery.ui.album.main.base.BaseAlbumTabPageView.1
                @Override // androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
                public void onChildViewDetachedFromWindow(View view) {
                }

                @Override // androidx.recyclerview.widget.RecyclerView.OnChildAttachStateChangeListener
                public void onChildViewAttachedToWindow(View view) {
                    RecyclerView.ViewHolder childViewHolder;
                    if (BaseAlbumTabPageView.this.isInChoiceMode() && (view instanceof Checkable) && (childViewHolder = recyclerView.getChildViewHolder(view)) != null) {
                        ((Checkable) view).setChecked(BaseAlbumTabPageView.this.getRecyclerViewWrapper().isItemChecked(childViewHolder.getItemId()));
                    }
                }
            };
        }
        recyclerView.removeOnChildAttachStateChangeListener(this.mChildAttachStateChangeListener);
        recyclerView.addOnChildAttachStateChangeListener(this.mChildAttachStateChangeListener);
    }

    public boolean isInChoiceMode() {
        EditableListViewWrapper editableListViewWrapper = this.mRecyclerViewWrapper;
        return editableListViewWrapper != null && editableListViewWrapper.isInChoiceMode() && this.mRecyclerViewWrapper.isInActionMode();
    }

    public boolean isInMoveMode() {
        RecyclerViewDragItemManager recyclerViewDragItemManager = this.mDragItemManager;
        return recyclerViewDragItemManager != null && recyclerViewDragItemManager.isDragging();
    }

    public boolean isSetDragItemOffsetWhenStartDrag() {
        return this.isSetDragItemOffsetWhenStartDrag;
    }

    public void onResume() {
        checkAndShowDragTipView();
    }

    public void checkAndShowDragTipView() {
        GalleryDialogFragment dragTipViewIfNeed;
        if (AlbumSortHelper.isCustomSortOrder() && (dragTipViewIfNeed = getStyle().getDragTipViewIfNeed()) != null) {
            dragTipViewIfNeed.show(getParent().getChildFragmentManager(), "drag_tip_view");
        }
    }

    public void onEnterChoiceMode() {
        TrackController.trackExpose("403.7.4.1.10542", "403.7.0.1.10328");
        this.isSetDragItemOffsetWhenStartDrag = false;
        View rootView = getRootView();
        if (rootView != null) {
            this.isHaveDelayRunnable = true;
            rootView.postDelayed(this.mCheckIsNeedSetDragItemOffsetWhenStartDrag, AlbumPageConfig.AlbumTabPage.getCurrentAlbumConfig().getEnterDragPressTimeout() + 1);
        }
    }

    public void onExitChoiceMode() {
        this.isSetDragItemOffsetWhenStartDrag = false;
    }

    public void onExitDragMode(boolean z) {
        this.isSetDragItemOffsetWhenStartDrag = true;
        if (z) {
            getAdapter().notifyDataSetChanged();
        }
    }

    public final void onInitChoiceMode(EditableListViewWrapper editableListViewWrapper) {
        this.mRecyclerViewWrapper = editableListViewWrapper;
        initChoiceMode(editableListViewWrapper);
    }

    public final RecyclerViewDragItemManager onInitDragMode(RecyclerViewDragItemManager.Config.Builder builder) {
        RecyclerViewDragItemManager initDragMode = initDragMode(builder);
        this.mDragItemManager = initDragMode;
        return initDragMode;
    }

    public void onDestory() {
        if (this.isHaveDelayRunnable && getRootView() != null) {
            getRootView().removeCallbacks(this.mCheckIsNeedSetDragItemOffsetWhenStartDrag);
        }
        if (this.mChildAttachStateChangeListener == null || getRecyclerView() == null) {
            return;
        }
        getRecyclerView().removeOnChildAttachStateChangeListener(this.mChildAttachStateChangeListener);
    }
}
