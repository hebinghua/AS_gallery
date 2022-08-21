package com.miui.gallery.ui.album.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.transition.Transition;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.h6ah4i.android.widget.advrecyclerview.adapter.SimpleWrapperAdapter;
import com.miui.epoxy.EpoxyAdapter;
import com.miui.epoxy.EpoxyModel;
import com.miui.gallery.R;
import com.miui.gallery.activity.HomePageActivity;
import com.miui.gallery.adapter.itemmodel.base.BaseGalleryItemModel;
import com.miui.gallery.adapter.itemmodel.base.BaseGalleryWrapperItemModel;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.app.screenChange.IScreenChange;
import com.miui.gallery.app.screenChange.ScreenSize;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.ui.CommonWrapperCheckableGridItemLayout;
import com.miui.gallery.ui.ImmersionMenuHolder;
import com.miui.gallery.ui.ImmersionMenuSupport;
import com.miui.gallery.ui.KeyboardShortcutGroupManager;
import com.miui.gallery.ui.OnHomePageResetListener;
import com.miui.gallery.ui.album.common.AlbumTabToolItemBean;
import com.miui.gallery.ui.album.common.CommonAlbumItemViewBean;
import com.miui.gallery.ui.album.common.MediaGroupTypeViewBean;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.main.base.BaseAlbumListPageView;
import com.miui.gallery.ui.album.main.base.BaseAlbumTabPageView;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;
import com.miui.gallery.ui.album.main.base.config.BaseAlbumPageStyle;
import com.miui.gallery.ui.album.main.base.config.GridAlbumPageStyle;
import com.miui.gallery.ui.album.main.base.config.LinearAlbumPageStyle;
import com.miui.gallery.ui.album.main.component.lock.AlbumTabLockAction;
import com.miui.gallery.ui.album.main.component.multichoice.AlbumCheckState;
import com.miui.gallery.ui.album.main.component.multichoice.AlbumTabMultiChoiceModeListener;
import com.miui.gallery.ui.album.main.grid.AlbumTabGridPageView;
import com.miui.gallery.ui.album.main.linear.AlbumTabLinearPageView;
import com.miui.gallery.ui.album.main.viewbean.FourPalaceGridCoverViewBean;
import com.miui.gallery.util.ActionURIHandler;
import com.miui.gallery.util.AlbumSortHelper;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.RequirementHelper$MediaTypeGroup;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.StringUtils;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.IFloatingButtonHandler;
import com.miui.gallery.widget.editwrapper.EditableListViewItemAnimHelper;
import com.miui.gallery.widget.editwrapper.EditableListViewWrapper;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.recyclerview.ItemClickSupport;
import com.miui.gallery.widget.tsd.INestedTwoStageDrawer;
import com.miui.gallery.widget.tsd.NestedTwoStageDrawer;
import com.miui.itemdrag.RecyclerViewDragItemManager;
import com.miui.itemdrag.RecyclerViewUtils;
import com.xiaomi.stat.MiStat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import miuix.springback.trigger.DefaultTrigger;
import miuix.springback.view.SpringBackLayout;

/* loaded from: classes2.dex */
public class AlbumTabFragment extends AlbumTabContract$V<AlbumTabPresenter> implements ImmersionMenuSupport, OnHomePageResetListener, IFloatingButtonHandler, RecyclerViewDragItemManager.OnSwapItemListener {
    public boolean isOpenDragMode;
    public EditableListViewWrapper mAlbumRecycleViewWrapper;
    public RecyclerViewDragItemManager mDragItemManager;
    public INestedTwoStageDrawer mDrawer;
    public AlbumTabLockAction mLockAction;
    public AlbumTabMultiChoiceModeListener mMultiChoiceListener;
    public Runnable mNotifyDataChangeRunnable;
    public CleanerActivityDestroyReceiver mReceiver;
    public AlbumPageKeyboardShortcutCallback mShortcutCallback = new AlbumPageKeyboardShortcutCallback();

    /* renamed from: $r8$lambda$hAfmeXPvBFvLJ3QMu3gDO6tzx-k */
    public static /* synthetic */ void m1593$r8$lambda$hAfmeXPvBFvLJ3QMu3gDO6tzxk(AlbumTabFragment albumTabFragment, View view) {
        albumTabFragment.lambda$getHandleClickListener$0(view);
    }

    public final boolean canDrag(long j) {
        return (j == 2131362609 || j == 2131361967 || j == 2131361957 || j == 2131362616 || j == 2131362613 || j == 2131362624 || j == 2131362621) ? false : true;
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabFragment, com.miui.gallery.app.base.BaseListPageFragment, com.miui.gallery.base_optimization.mvp.view.Fragment
    public int getLayoutId() {
        return R.layout.album_tab_page;
    }

    @Override // com.miui.gallery.ui.ImmersionMenuSupport
    public int getSupportedAction() {
        return 14;
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabFragment, com.miui.gallery.app.base.BaseListPageFragment
    public void initRecyclerView(RecyclerView recyclerView) {
        super.initRecyclerView(recyclerView);
        ((GalleryRecyclerView) recyclerView).setSpringEnabled(false);
        this.mDrawer = (INestedTwoStageDrawer) findViewById(R.id.nested_two_stage_drawer);
        addScreenChangeListener(new IScreenChange.OnLargeScreenChangeListener() { // from class: com.miui.gallery.ui.album.main.AlbumTabFragment.1
            @Override // com.miui.gallery.app.screenChange.IScreenChange.OnLargeScreenChangeListener
            public void onCreatedWhileNormalDevice(ScreenSize screenSize) {
            }

            {
                AlbumTabFragment.this = this;
            }

            @Override // com.miui.gallery.app.screenChange.IScreenChange.OnLargeScreenChangeListener
            public void onCreatedWhileLargeDevice(ScreenSize screenSize) {
                refreshUI();
            }

            @Override // com.miui.gallery.app.screenChange.IScreenChange.OnLargeScreenChangeListener
            public void onScreenSizeToLargeOrNormal(ScreenSize screenSize) {
                refreshUI();
            }

            public final void refreshUI() {
                if (AlbumTabFragment.this.mDrawer instanceof NestedTwoStageDrawer) {
                    ((ViewGroup.MarginLayoutParams) ((ConstraintLayout.LayoutParams) ((NestedTwoStageDrawer) AlbumTabFragment.this.mDrawer).getLayoutParams())).topMargin = AlbumTabFragment.this.getResources().getDimensionPixelOffset(R.dimen.tab_or_assistant_place_holder_height);
                }
            }
        });
        DefaultTrigger defaultTrigger = new DefaultTrigger(getContext());
        AlbumTabLockAction albumTabLockAction = new AlbumTabLockAction(this);
        this.mLockAction = albumTabLockAction;
        defaultTrigger.addAction(albumTabLockAction);
        defaultTrigger.attach((SpringBackLayout) findViewById(R.id.spring_back_layout));
        initCheckable();
        recyclerView.setHapticFeedbackEnabled(false);
        setActionEnterPoint(this.mLockAction);
    }

    public final void setActionEnterPoint(AlbumTabLockAction albumTabLockAction) {
        if (albumTabLockAction != null && getResources().getConfiguration().orientation == 2) {
            int min = Math.min(getResources().getDimensionPixelSize(R.dimen.album_action_simple_enter), albumTabLockAction.mEnterPoint);
            albumTabLockAction.mEnterPoint = min;
            albumTabLockAction.mTriggerPoint = min;
        }
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabFragment, androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        Transition sharedElementExitTransition = getActivity().getWindow().getSharedElementExitTransition();
        if (sharedElementExitTransition == null) {
            return;
        }
        sharedElementExitTransition.excludeTarget(R.id.album_common_main, true);
        sharedElementExitTransition.excludeTarget(R.id.album_common_wrapper_main, true);
        sharedElementExitTransition.excludeTarget(R.id.album_common_wrapper_checkable_main, true);
        sharedElementExitTransition.excludeTarget(R.id.item_group_album_main, true);
        sharedElementExitTransition.excludeTarget(R.id.album_tool_item_main, true);
        getActivity().getWindow().setSharedElementExitTransition(sharedElementExitTransition);
    }

    public final void initCheckable() {
        boolean isGridPageMode = AlbumPageConfig.getInstance().isGridPageMode();
        RecyclerViewDragItemManager onInitDragMode = mo1594getPageView().onInitDragMode(new RecyclerViewDragItemManager.Config.Builder(((AlbumTabPresenter) getPresenter()).getDragItemTouchCallback()));
        this.mDragItemManager = onInitDragMode;
        onInitDragMode.setEnlargeItemEnable(isGridPageMode);
        this.mDragItemManager.setSwapItemListener(this);
        EditableListViewWrapper editableListViewWrapper = new EditableListViewWrapper((GalleryRecyclerView) this.mRecyclerView, new AlbumCheckState(this.mAdapter));
        this.mAlbumRecycleViewWrapper = editableListViewWrapper;
        editableListViewWrapper.enableChoiceMode(true, new EditableListViewWrapper.OnLongClickCheck() { // from class: com.miui.gallery.ui.album.main.AlbumTabFragment.2
            {
                AlbumTabFragment.this = this;
            }

            @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.OnLongClickCheck
            public boolean canNext(RecyclerView recyclerView, View view, int i, long j) {
                return j != 2131558483 && AlbumTabFragment.this.canDrag((long) view.getId());
            }
        });
        this.mAlbumRecycleViewWrapper.setEnableContinuousPick(false);
        this.mAlbumRecycleViewWrapper.enterChoiceModeWithLongClick(true);
        this.mAlbumRecycleViewWrapper.enableActionModeItemAnim(false);
        this.mAlbumRecycleViewWrapper.disableScaleImageViewAniWhenInActionMode();
        AlbumTabMultiChoiceModeListener albumTabMultiChoiceModeListener = new AlbumTabMultiChoiceModeListener(this);
        this.mMultiChoiceListener = albumTabMultiChoiceModeListener;
        this.mAlbumRecycleViewWrapper.setMultiChoiceModeListener(albumTabMultiChoiceModeListener);
        this.mAlbumRecycleViewWrapper.setItemAnimEnable(isGridPageMode);
        this.mAlbumRecycleViewWrapper.setOnItemClickListener(new ItemClickSupport.OnItemClickListener() { // from class: com.miui.gallery.ui.album.main.AlbumTabFragment.3
            {
                AlbumTabFragment.this = this;
            }

            @Override // com.miui.gallery.widget.recyclerview.ItemClickSupport.OnItemClickListener
            public boolean onItemClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
                AlbumTabFragment.this.onItemClick(view);
                return true;
            }
        });
        this.mAlbumRecycleViewWrapper.setEditableListViewItemAnimHelper(new EditableListViewItemAnimHelper.Builder().withDownAlphaEnlargeAnim().withUpAlphaNarrowAnim().build(), CommonWrapperCheckableGridItemLayout.class.getSimpleName(), ConstraintLayout.class.getSimpleName());
        ItemClickSupport.from(this.mRecyclerView).setIsClickedItemRecyclable(false);
        this.mAlbumRecycleViewWrapper.setAdapter(this.mAdapter);
        this.mAlbumRecycleViewWrapper.setOnCallNotifyDataChangeEventListener(new EditableListViewWrapper.OnCallNotifyDataSetChangeListener() { // from class: com.miui.gallery.ui.album.main.AlbumTabFragment.4
            {
                AlbumTabFragment.this = this;
            }

            @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.OnCallNotifyDataSetChangeListener
            public boolean onNotifyEvent(final RecyclerView.Adapter adapter) {
                if (AlbumTabFragment.this.mAlbumRecycleViewWrapper.isStartingActionMode() && !AlbumPageConfig.getInstance().isGridPageMode()) {
                    AlbumTabFragment.this.mRecyclerView.postDelayed(AlbumTabFragment.this.mNotifyDataChangeRunnable = new Runnable() { // from class: com.miui.gallery.ui.album.main.AlbumTabFragment.4.1
                        {
                            AnonymousClass4.this = this;
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            int longTouchPosition = AlbumTabFragment.this.mAlbumRecycleViewWrapper.getLongTouchPosition();
                            RecyclerView.Adapter adapter2 = adapter;
                            adapter2.notifyItemRangeChanged(0, adapter2.getItemCount(), EpoxyAdapter.DEFAULT_PAYLOAD);
                            AlbumTabFragment.this.mAlbumRecycleViewWrapper.setLongTouchPosition(longTouchPosition);
                            AlbumTabFragment.this.mNotifyDataChangeRunnable = null;
                        }
                    }, 10L);
                    return true;
                }
                adapter.notifyItemRangeChanged(0, adapter.getItemCount(), EpoxyAdapter.DEFAULT_PAYLOAD);
                return true;
            }
        });
        mo1594getPageView().onInitChoiceMode(this.mAlbumRecycleViewWrapper);
    }

    @Override // com.miui.gallery.ui.album.main.AlbumTabContract$V
    public boolean canDrag(RecyclerView.ViewHolder viewHolder, int i, int i2) {
        if (viewHolder == null) {
            return false;
        }
        return canDrag(viewHolder.itemView.getId());
    }

    @Override // com.miui.gallery.ui.album.main.AlbumTabContract$V
    public boolean isInChoiceMode() {
        return mo1594getPageView().isInChoiceMode();
    }

    @Override // com.miui.gallery.ui.album.main.AlbumTabContract$V
    public boolean isInMoveMode() {
        return mo1594getPageView().isInMoveMode();
    }

    public final void onItemClick(View view) {
        RecyclerView.ViewHolder findContainingViewHolder;
        EpoxyModel<?> model;
        if (view == null || isInChoiceMode() || getSafeActivity() == null || (findContainingViewHolder = this.mRecyclerView.findContainingViewHolder(view)) == null || (model = this.mAdapter.getModel(findContainingViewHolder.getAdapterPosition())) == null) {
            return;
        }
        if (model instanceof BaseGalleryWrapperItemModel) {
            onItemClick(((BaseGalleryWrapperItemModel) model).getItemData());
        } else {
            onItemClick(((BaseGalleryItemModel) model).getItemData());
        }
    }

    public final void onItemClick(Object obj) {
        if (isInChoiceMode() || obj == null || getSafeActivity() == null) {
            return;
        }
        if (obj instanceof FourPalaceGridCoverViewBean) {
            long id = ((FourPalaceGridCoverViewBean) obj).getId();
            if (Album.isAIAlbums(id)) {
                TimeMonitor.createNewTimeMonitor("403.16.0.1.13782");
                IntentUtil.gotoAIAlbumPage(getSafeActivity());
            } else if (!Album.isOtherAlbums(id)) {
            } else {
                IntentUtil.gotoOtherAlbumPage(getSafeActivity());
            }
        } else if (obj instanceof CommonAlbumItemViewBean) {
            IntentUtil.gotoAlbumDetailPage(getSafeActivity(), (Album) ((CommonAlbumItemViewBean) obj).getSource());
        } else if (obj instanceof AlbumTabToolItemBean) {
            IntentUtil.gotoGalleryPage(getActivity(), ((AlbumTabToolItemBean) obj).getGotoLink());
            trackItemClickEvent(obj);
        } else if (obj instanceof MediaGroupTypeViewBean) {
            ActionURIHandler.handleUri(getActivity(), Uri.parse(((MediaGroupTypeViewBean) obj).getGotoLink()));
            trackItemClickEvent(obj);
        } else if (!(obj instanceof BaseViewBean) || !Album.isTrashAlbums(((BaseViewBean) obj).getId())) {
        } else {
            TimeMonitor.createNewTimeMonitor("403.21.0.1.13766");
            IntentUtil.gotoTrashBin(getContext(), "HomePageActivity");
        }
    }

    public final void trackItemClickEvent(Object obj) {
        if (obj instanceof AlbumTabToolItemBean) {
            AlbumTabToolItemBean albumTabToolItemBean = (AlbumTabToolItemBean) obj;
            if (albumTabToolItemBean.getEventTip() == null) {
                return;
            }
            TrackController.trackClick(albumTabToolItemBean.getEventTip(), "403.7.0.1.10328");
        } else if (!(obj instanceof MediaGroupTypeViewBean)) {
        } else {
            TrackController.trackClick(RequirementHelper$MediaTypeGroup.getEventTipById((int) ((MediaGroupTypeViewBean) obj).getId()), "403.7.0.1.10328");
        }
    }

    @Override // com.miui.gallery.ui.album.main.AlbumTabContract$V
    public int getCheckedCount() {
        return this.mAlbumRecycleViewWrapper.getCheckedItemCount();
    }

    @Override // com.miui.gallery.ui.album.main.AlbumTabContract$V
    public long[] getCheckedItemIds() {
        return this.mAlbumRecycleViewWrapper.getCheckedItemIds();
    }

    @Override // com.miui.gallery.ui.album.main.AlbumTabContract$V
    public int[] getCheckedItemOrderedPositions() {
        return this.mAlbumRecycleViewWrapper.getCheckedItemOrderedPositions();
    }

    public final void exitActionMode() {
        EditableListViewWrapper editableListViewWrapper = this.mAlbumRecycleViewWrapper;
        if (editableListViewWrapper == null || !editableListViewWrapper.isInActionMode()) {
            return;
        }
        this.mRecyclerView.post(new Runnable() { // from class: com.miui.gallery.ui.album.main.AlbumTabFragment.5
            {
                AlbumTabFragment.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                AlbumTabFragment.this.mAlbumRecycleViewWrapper.stopActionMode();
            }
        });
    }

    @Override // com.miui.gallery.ui.album.main.AlbumTabContract$V
    public void onStartChoiceMode() {
        changeCreateAlbumButtonVisibleStatus(false);
        ((AlbumTabPresenter) getPresenter()).onStartChoiceMode();
        mo1594getPageView().onEnterChoiceMode();
    }

    public final void changeCreateAlbumButtonVisibleStatus(boolean z) {
        FragmentActivity activity = getActivity();
        if (activity == null || activity.isFinishing() || !(activity instanceof HomePageActivity)) {
            return;
        }
        ((HomePageActivity) activity).changeCreateAlbumButtonVisibleStatus(z);
    }

    @Override // com.miui.gallery.ui.album.main.AlbumTabContract$V
    public void onStopChoiceMode() {
        if (mo1594getPageView().isSetDragItemOffsetWhenStartDrag()) {
            changeCreateAlbumButtonVisibleStatus(true);
        }
        mo1594getPageView().onExitChoiceMode();
        ((AlbumTabPresenter) getPresenter()).onStopChoiceMode();
        if (isInMoveMode() || getAdapter() == null) {
            return;
        }
        getAdapter().notifyItemRangeChanged(0, getDataSize(), EpoxyAdapter.DEFAULT_PAYLOAD);
    }

    @Override // com.miui.gallery.ui.ImmersionMenuSupport
    public void onActionClick(int i) {
        View immersionMenuAnchor;
        if (isInMoveMode()) {
            return;
        }
        if (i == 2) {
            if (isInMoveMode() || !(getSafeActivity() instanceof ImmersionMenuHolder) || (immersionMenuAnchor = ((ImmersionMenuHolder) getSafeActivity()).getImmersionMenuAnchor()) == null) {
                return;
            }
            super.showSortImmersionMenu(immersionMenuAnchor);
        } else if (i == 4 && !isEmptyDatas()) {
            if (this.mRecyclerView == null) {
                DefaultLogger.e("AlbumTabFragment", "=======================can't change view mode========================");
                return;
            }
            boolean z = AlbumPageConfig.getAlbumTabConfig().toggleAlbumPageMode();
            BaseAlbumListPageView albumTabGridPageView = z ? new AlbumTabGridPageView(mo1594getPageView()) : new AlbumTabLinearPageView(mo1594getPageView());
            albumTabGridPageView.onInitRecyclerView(this.mRecyclerView);
            setPageView(albumTabGridPageView);
            mo1594getPageView().onInitChoiceMode(this.mAlbumRecycleViewWrapper);
            this.mRecyclerView.suppressLayout(true);
            clearItemDecoration();
            for (RecyclerView.ItemDecoration itemDecoration : albumTabGridPageView.getRecyclerViewDecorations()) {
                this.mRecyclerView.addItemDecoration(itemDecoration);
            }
            setDatas(getDatas(), true);
            this.mRecyclerView.suppressLayout(false);
            EditableListViewWrapper editableListViewWrapper = this.mAlbumRecycleViewWrapper;
            if (editableListViewWrapper != null) {
                editableListViewWrapper.setItemAnimEnable(z);
            }
            RecyclerViewDragItemManager recyclerViewDragItemManager = this.mDragItemManager;
            if (recyclerViewDragItemManager == null) {
                return;
            }
            recyclerViewDragItemManager.setEnlargeItemEnable(z);
        }
    }

    @Override // com.miui.gallery.ui.album.main.AlbumTabContract$V
    public void openDragMode(boolean z) {
        this.isOpenDragMode = true;
        this.mDragItemManager.setSwapEnableStatus(z);
        this.mDragItemManager.attachToRecyclerView(this.mRecyclerView);
        if (z) {
            openItemSwapWhenDragMode();
        } else {
            closeItemSwapWhenDragMode();
        }
    }

    @Override // com.miui.gallery.ui.album.main.AlbumTabContract$V
    public void openItemSwapWhenDragMode() {
        RecyclerViewDragItemManager recyclerViewDragItemManager = this.mDragItemManager;
        if (recyclerViewDragItemManager != null) {
            recyclerViewDragItemManager.setSwapEnableStatus(true);
        }
    }

    @Override // com.miui.gallery.ui.album.main.AlbumTabContract$V
    public void closeItemSwapWhenDragMode() {
        RecyclerViewDragItemManager recyclerViewDragItemManager = this.mDragItemManager;
        if (recyclerViewDragItemManager != null) {
            recyclerViewDragItemManager.setSwapEnableStatus(false);
        }
    }

    @Override // com.miui.gallery.ui.album.main.AlbumTabContract$V
    public void changeDragStatus(final boolean z, final boolean z2) {
        if (this.mRecyclerView.isComputingLayout()) {
            this.mRecyclerView.post(new Runnable() { // from class: com.miui.gallery.ui.album.main.AlbumTabFragment.6
                {
                    AlbumTabFragment.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    AlbumTabFragment.this.changeDragStatus(z, z2);
                }
            });
        } else if (z) {
            ((SpringBackLayout) this.mRecyclerView.getParent()).internalRequestDisallowInterceptTouchEvent(false);
            if (!isInChoiceMode()) {
                changeCreateAlbumButtonVisibleStatus(true);
            }
            mo1594getPageView().onExitDragMode(z2);
        } else {
            ((SpringBackLayout) this.mRecyclerView.getParent()).internalRequestDisallowInterceptTouchEvent(true);
        }
    }

    @Override // com.miui.gallery.ui.album.main.AlbumTabContract$V
    public void onChangeHeadGroupEmptyStatus(boolean z) {
        invalidateItemDecorations();
    }

    public final RecyclerView.Adapter getAdapter() {
        RecyclerViewDragItemManager recyclerViewDragItemManager = this.mDragItemManager;
        if (recyclerViewDragItemManager != null) {
            return recyclerViewDragItemManager.getAdapter();
        }
        RecyclerView.Adapter adapter = this.mRecyclerView.getAdapter();
        while (adapter instanceof SimpleWrapperAdapter) {
            adapter = ((SimpleWrapperAdapter) adapter).getWrappedAdapter();
        }
        return adapter;
    }

    @Override // com.miui.gallery.ui.album.main.AlbumTabContract$V
    public void onFirstMoveWhenDragItem() {
        if (!mo1594getPageView().isSetDragItemOffsetWhenStartDrag()) {
            exitActionMode();
        }
        mo1594getPageView().onEnterDragMode();
        this.mAlbumRecycleViewWrapper.setLongTouchPosition(-1);
    }

    @Override // com.miui.gallery.ui.album.main.AlbumTabContract$V
    public int getTouchSlop() {
        return ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override // com.miui.gallery.ui.album.main.AlbumTabContract$V
    public void onMoveAlbumFailed(int i) {
        if (i == 1) {
            ToastUtils.makeText(getContext(), (int) R.string.drag_failed_because_not_custom_sort);
        }
    }

    public final void closeDragMode() {
        RecyclerViewDragItemManager recyclerViewDragItemManager = this.mDragItemManager;
        if (recyclerViewDragItemManager == null || !this.isOpenDragMode) {
            return;
        }
        this.isOpenDragMode = false;
        this.mAlbumRecycleViewWrapper.setAdapter(recyclerViewDragItemManager.getAdapter());
        this.mDragItemManager.release();
    }

    @Override // com.miui.gallery.app.base.BaseListPageFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        mo1594getPageView().onDestory();
        closeDragMode();
        TimeMonitor.cancelTimeMonitor("403.7.0.1.13764");
        if (this.mReceiver != null) {
            LocalBroadcastManager.getInstance(getSafeActivity()).unregisterReceiver(this.mReceiver);
        }
    }

    @Override // com.miui.gallery.ui.album.main.AlbumTabContract$V
    public void onFillItemWhenEmptyHeadGroup(int i, int i2) {
        this.mRecyclerView.scrollToPosition(0);
        moveData(i, i2);
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageFragment, com.miui.gallery.base_optimization.mvp.view.Fragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onStop() {
        getView().removeCallbacks(this.mNotifyDataChangeRunnable);
        super.onStop();
        EditableListViewWrapper editableListViewWrapper = this.mAlbumRecycleViewWrapper;
        if (editableListViewWrapper != null) {
            editableListViewWrapper.reductionTouchView();
        }
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabFragment, com.miui.gallery.base_optimization.mvp.view.Fragment
    public void onFragmentViewVisible(boolean z) {
        super.onFragmentViewVisible(z);
        if (z) {
            mo1594getPageView().onResume();
        }
        if (this.mReceiver == null) {
            this.mReceiver = new CleanerActivityDestroyReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("com.miui.gallery.action.ACTION_CLEANER_DESTROY");
            LocalBroadcastManager.getInstance(getSafeActivity()).registerReceiver(this.mReceiver, intentFilter);
        }
    }

    @Override // com.miui.gallery.app.base.BaseFragment, com.miui.gallery.base_optimization.mvp.view.Fragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        if (AlbumSortHelper.isUpdateTimeSortMode()) {
            ((AlbumTabPresenter) getPresenter()).queryAlbums();
        }
    }

    public final Rect getItemOffset() {
        BaseAlbumPageStyle currentAlbumConfig = AlbumPageConfig.AlbumTabPage.getCurrentAlbumConfig();
        Rect rect = new Rect();
        if (currentAlbumConfig instanceof GridAlbumPageStyle) {
            GridAlbumPageStyle gridAlbumPageStyle = (GridAlbumPageStyle) currentAlbumConfig;
            int itemHorizontalSpacing = gridAlbumPageStyle.getItemHorizontalSpacing();
            rect.right = itemHorizontalSpacing;
            rect.left = itemHorizontalSpacing;
            int itemVerticalSpacing = gridAlbumPageStyle.getItemVerticalSpacing();
            rect.bottom = itemVerticalSpacing;
            rect.top = itemVerticalSpacing;
        } else {
            int itemVerticalSpacing2 = ((LinearAlbumPageStyle) currentAlbumConfig).getItemVerticalSpacing();
            rect.bottom = itemVerticalSpacing2;
            rect.top = itemVerticalSpacing2;
        }
        return rect;
    }

    @Override // com.miui.gallery.ui.album.main.AlbumTabContract$V
    public Pair<Integer, Rect> findAdjacentItemByPoint(RecyclerView recyclerView, int i, int i2) {
        int i3;
        int i4;
        Rect rect = new Rect();
        Rect rect2 = new Rect();
        Rect itemOffset = getItemOffset();
        int i5 = itemOffset.left + itemOffset.right;
        int i6 = itemOffset.top + itemOffset.bottom;
        int spanCount = RecyclerViewUtils.getSpanCount(recyclerView);
        boolean z = spanCount == 1;
        boolean isRtl = ScreenUtils.isRtl(getContext());
        for (int childCount = recyclerView.getChildCount() - 1; childCount >= 0; childCount--) {
            View childAt = recyclerView.getChildAt(childCount);
            if (canDrag(childAt.getId())) {
                rect.set(childAt.getLeft(), childAt.getTop(), childAt.getRight(), childAt.getBottom());
                if (z) {
                    int i7 = rect.bottom;
                    int i8 = i7 - rect.top;
                    int i9 = i7 + i6;
                    rect2.set(rect.left, i9, rect.right, i8 + i9);
                } else if (RecyclerViewUtils.getSpanIndex(recyclerView, childAt) == spanCount - 1) {
                    int i10 = childCount - 2;
                    if (i10 >= 0 && childCount + 1 >= recyclerView.getChildCount()) {
                        View childAt2 = recyclerView.getChildAt(i10);
                        int bottom = childAt2.getBottom() - childAt2.getTop();
                        int bottom2 = childAt2.getBottom() + i6;
                        rect2.set(childAt2.getLeft(), bottom2, childAt2.getRight(), bottom + bottom2);
                    }
                } else {
                    int i11 = rect.right;
                    int i12 = rect.left;
                    int i13 = i11 - i12;
                    if (isRtl) {
                        i4 = i12 - i5;
                        i3 = i4 - i13;
                    } else {
                        i3 = i11 + i5;
                        i4 = i3 + i13;
                    }
                    rect2.set(i3, rect.top, i4, rect.bottom);
                }
                if (rect2.contains(i, i2)) {
                    return Pair.create(Integer.valueOf(childCount), rect2);
                }
            }
        }
        return null;
    }

    @Override // com.miui.gallery.ui.album.main.AlbumTabContract$V
    public int getCurrentListVisiblePosition() {
        if (this.mRecyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            return ((LinearLayoutManager) this.mRecyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        }
        return -1;
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageFragment, androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (this.mLockAction.onResult(i, i2, intent)) {
            return;
        }
        super.onActivityResult(i, i2, intent);
    }

    public /* synthetic */ void lambda$getHandleClickListener$0(View view) {
        doCreateAlbum();
    }

    @Override // com.miui.gallery.widget.IFloatingButtonHandler
    public View.OnClickListener getHandleClickListener() {
        return new View.OnClickListener() { // from class: com.miui.gallery.ui.album.main.AlbumTabFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                AlbumTabFragment.m1593$r8$lambda$hAfmeXPvBFvLJ3QMu3gDO6tzxk(AlbumTabFragment.this, view);
            }
        };
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageFragment
    public void onOperationEnd() {
        super.onOperationEnd();
        exitActionMode();
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabFragment
    /* renamed from: getPageView */
    public BaseAlbumTabPageView mo1594getPageView() {
        return (BaseAlbumTabPageView) super.mo1594getPageView();
    }

    @Override // com.miui.gallery.ui.OnHomePageResetListener
    public void onHomePageReset() {
        exitActionMode();
    }

    @Override // com.miui.itemdrag.RecyclerViewDragItemManager.OnSwapItemListener
    public void onSwapItem() {
        EditableListViewWrapper editableListViewWrapper = this.mAlbumRecycleViewWrapper;
        if (editableListViewWrapper != null) {
            editableListViewWrapper.setOnlyUpAlphaAnimEnable(true);
        }
    }

    @Override // com.miui.itemdrag.RecyclerViewDragItemManager.OnSwapItemListener
    public void onSwapItemFinish() {
        EditableListViewWrapper editableListViewWrapper = this.mAlbumRecycleViewWrapper;
        if (editableListViewWrapper != null) {
            editableListViewWrapper.setOnlyUpAlphaAnimEnable(false);
        }
    }

    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int i) {
        ArrayList arrayList = new ArrayList();
        if (this.mAlbumRecycleViewWrapper.isInActionMode()) {
            arrayList.add(KeyboardShortcutGroupManager.getInstance().getSelectAllShortcutInfo());
            if (this.mMultiChoiceListener.isDeleteEnable()) {
                arrayList.addAll(KeyboardShortcutGroupManager.getInstance().getDeleteShortcutInfo());
            }
        }
        list.add(new KeyboardShortcutGroup(getPageName(), arrayList));
    }

    public boolean onKeyShortcut(int i, KeyEvent keyEvent) {
        return KeyboardShortcutGroupManager.getInstance().onKeyShortcut(i, keyEvent, this.mShortcutCallback);
    }

    /* loaded from: classes2.dex */
    public class AlbumPageKeyboardShortcutCallback implements KeyboardShortcutGroupManager.OnKeyShortcutCallback {
        public AlbumPageKeyboardShortcutCallback() {
            AlbumTabFragment.this = r1;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onSearchPressed() {
            if (AlbumTabFragment.this.mAlbumRecycleViewWrapper == null || !AlbumTabFragment.this.mAlbumRecycleViewWrapper.isInActionMode()) {
                Bundle bundle = new Bundle(1);
                bundle.putString("from", "from_album_page");
                ActionURIHandler.handleUri(AlbumTabFragment.this.getActivity(), GalleryContract.Search.URI_SEARCH_PAGE.buildUpon().build(), bundle);
                AlbumTabFragment.this.getActivity().overridePendingTransition(R.anim.appear, R.anim.disappear);
                return true;
            }
            return false;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onSelectAllPressed() {
            if (AlbumTabFragment.this.mAlbumRecycleViewWrapper == null || !AlbumTabFragment.this.mAlbumRecycleViewWrapper.isInActionMode()) {
                return false;
            }
            AlbumTabFragment.this.mAlbumRecycleViewWrapper.setAllItemsCheckState(true);
            return true;
        }

        @Override // com.miui.gallery.ui.KeyboardShortcutGroupManager.OnKeyShortcutCallback
        public boolean onDeletePressed() {
            if (AlbumTabFragment.this.mMultiChoiceListener == null || !AlbumTabFragment.this.mMultiChoiceListener.isDeleteEnable() || AlbumTabFragment.this.mAlbumRecycleViewWrapper == null || !AlbumTabFragment.this.mAlbumRecycleViewWrapper.isInActionMode()) {
                return false;
            }
            AlbumTabFragment.this.mMultiChoiceListener.doDelete();
            return true;
        }
    }

    public final void trackEnterAlbumTab() {
        if (getView() == null) {
            return;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("tip", "403.7.0.1.10328");
        hashMap.put(MiStat.Param.COUNT, String.valueOf(getDataSize()));
        String[] supportGroups = ((AlbumTabPresenter) getPresenter()).getSupportGroups();
        int i = 0;
        if (((AlbumTabPresenter) getPresenter()).getGroupDatas(supportGroups[0]) != null) {
            i = ((AlbumTabPresenter) getPresenter()).getGroupDatas(supportGroups[0]).size();
        }
        hashMap.put("count_extra", Integer.valueOf(i));
        AutoTracking.trackView(hashMap);
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabFragment, com.miui.gallery.app.base.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mRecyclerView.invalidateItemDecorations();
    }

    @Override // com.miui.gallery.app.fragment.MiuiFragment, com.miui.gallery.listener.OnVisibilityChangeListener
    public void onVisibleChange(boolean z) {
        super.onVisibleChange(z);
        if (z) {
            trackEnterAlbumTab();
        }
    }

    /* loaded from: classes2.dex */
    public class CleanerActivityDestroyReceiver extends BroadcastReceiver {
        public CleanerActivityDestroyReceiver() {
            AlbumTabFragment.this = r1;
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action != null && action.equals("com.miui.gallery.action.ACTION_CLEANER_DESTROY")) {
                ((AlbumTabPresenter) AlbumTabFragment.this.getPresenter()).dispatchCleaner(StringUtils.getNumberStringInRange(GalleryPreferences.Album.getAlbumCleanableCount()));
            }
        }
    }
}
