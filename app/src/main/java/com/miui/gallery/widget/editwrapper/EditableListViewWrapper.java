package com.miui.gallery.widget.editwrapper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.ClipData;
import android.content.ClipDescription;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.ArrayMap;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.tracing.Trace;
import com.baidu.platform.comapi.UIMsg;
import com.google.common.collect.Lists;
import com.h6ah4i.android.widget.advrecyclerview.adapter.ItemIdComposer;
import com.h6ah4i.android.widget.advrecyclerview.adapter.SimpleWrapperAdapter;
import com.h6ah4i.android.widget.advrecyclerview.adapter.WrapperAdapter;
import com.h6ah4i.android.widget.advrecyclerview.composedadapter.ComposedAdapter;
import com.h6ah4i.android.widget.advrecyclerview.headerfooter.AbstractHeaderFooterWrapperAdapter;
import com.h6ah4i.android.widget.advrecyclerview.utils.WrapperAdapterUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.adapter.BaseMediaAdapter;
import com.miui.gallery.adapter.CheckableAdapter;
import com.miui.gallery.provider.GalleryOpenProvider;
import com.miui.gallery.ui.Checkable;
import com.miui.gallery.ui.CheckableView;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.SyncSortUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.InitState;
import com.miui.gallery.widget.ScalableImageView;
import com.miui.gallery.widget.ViewUtils;
import com.miui.gallery.widget.editwrapper.EditableListViewItemAnimHelper;
import com.miui.gallery.widget.editwrapper.EditableListViewWrapper;
import com.miui.gallery.widget.recyclerview.GalleryRecyclerView;
import com.miui.gallery.widget.recyclerview.GroupedItemAdapter;
import com.miui.gallery.widget.recyclerview.ItemClickSupport;
import com.miui.gallery.widget.recyclerview.grouped.GroupedItemManager;
import com.miui.itemdrag.RecyclerViewUtils;
import com.miui.itemdrag.WrapperSource;
import com.xiaomi.milab.videosdk.SurfaceRatio;
import com.xiaomi.mirror.widget.MiuiDragShadowBuilder;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import miui.gallery.support.MiuiSdkCompat;
import miuix.view.ActionModeAnimationListener;
import miuix.view.EditActionMode;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes2.dex */
public class EditableListViewWrapper {
    public boolean isActionModeEnterAnimStarting;
    public boolean isHideActionMode;
    public boolean isPendingActionModeFinish;
    public AdapterHolder mAdapterHolder;
    public ArrayMap<RecyclerView.Adapter, Boolean> mAdapterObservableRegisterStatus;
    public ArrayMap<RecyclerView.Adapter, AdapterHolder> mAdapterWrappers;
    public AnimationManager mAnimationManager;
    public OnLongClickCheck mCanLongClickCheck;
    public CheckState mCheckState;
    public ActionMode mChoiceActionMode;
    public DragDataProvider mDragDataProvider;
    public EditActionMode mEditActionMode;
    public InitState mInitState;
    public boolean mIsAfterBindViewHolderHook;
    public boolean mIsInActionMode;
    public boolean mIsInChoiceMode;
    public boolean mIsPreInChoiceMode;
    public EditableListViewItemAnimHelper mItemAnimHelper;
    public ItemClickSupport.OnItemClickListener mItemClickDelegate;
    public MultiChoiceModeCallback mMultiChoiceModeCallback;
    public OnBindViewHolderHook mOnBindViewHolderHook;
    public OnCallNotifyDataSetChangeListener mOnCallNotifyDataSetChangeListener;
    public OnTouchCallBack mOnTouchCallback;
    public GalleryRecyclerView mRecyclerView;
    public RecyclerView.OnScrollListener mScrollListener;
    public int mListScrollState = 0;
    public boolean enableActionModeItemAnim = true;
    public boolean isItemAnimEnable = true;
    public RecyclerView.OnScrollListener mScrollDelegate = new RecyclerView.OnScrollListener() { // from class: com.miui.gallery.widget.editwrapper.EditableListViewWrapper.1
        {
            EditableListViewWrapper.this = this;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            EditableListViewWrapper.this.mListScrollState = i;
            if (EditableListViewWrapper.this.mScrollListener != null) {
                EditableListViewWrapper.this.mScrollListener.onScrollStateChanged(recyclerView, i);
            }
            if (EditableListViewWrapper.this.mItemAnimHelper == null || EditableListViewWrapper.this.mIsInChoiceMode) {
                return;
            }
            EditableListViewWrapper.this.mItemAnimHelper.onScrollStateChanged(recyclerView, i);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            if (EditableListViewWrapper.this.mScrollListener != null) {
                EditableListViewWrapper.this.mScrollListener.onScrolled(recyclerView, i, i2);
            }
            EditableListViewWrapper.this.mAnimationManager.setMovePickEnable(false);
        }
    };
    public int mEditActionModeButton2Func = 0;
    public final RestoreCheckStateObserver mRestoreCheckStateObserver = new RestoreCheckStateObserver();
    public ItemClickSupport.OnItemLongClickListener mEnterActionModeListener = new ItemClickSupport.OnItemLongClickListener() { // from class: com.miui.gallery.widget.editwrapper.EditableListViewWrapper.3
        {
            EditableListViewWrapper.this = this;
        }

        @Override // com.miui.gallery.widget.recyclerview.ItemClickSupport.OnItemLongClickListener
        public boolean onItemLongClick(RecyclerView recyclerView, View view, final int i, long j, float f, float f2, boolean z) {
            if (EditableListViewWrapper.this.mIsInChoiceMode) {
                EditableListViewWrapper.this.mAnimationManager.setLongPress(true);
                EditableListViewWrapper.this.mAnimationManager.setLongTouchPosition(i);
                return false;
            } else if (!EditableListViewWrapper.this.mAdapterHolder.isValidDataPosition(i)) {
                return false;
            } else {
                if (EditableListViewWrapper.this.mCanLongClickCheck != null && !EditableListViewWrapper.this.mCanLongClickCheck.canNext(recyclerView, view, i, j)) {
                    return false;
                }
                if (EditableListViewWrapper.this.isItemAnimEnable && view != null && EditableListViewWrapper.this.mItemAnimHelper != null && EditableListViewWrapper.this.mItemAnimHelper.isHandleType(view.getClass().getSimpleName()) && EditableListViewWrapper.this.mItemAnimHelper.isNeedFastUp()) {
                    EditableListViewWrapper.this.mItemAnimHelper.fastUpItemView(view, new AnimatorListenerAdapter() { // from class: com.miui.gallery.widget.editwrapper.EditableListViewWrapper.3.1
                        {
                            AnonymousClass3.this = this;
                        }

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationStart(Animator animator) {
                            super.onAnimationStart(animator);
                            EditableListViewWrapper editableListViewWrapper = EditableListViewWrapper.this;
                            editableListViewWrapper.mIsPreInChoiceMode = !editableListViewWrapper.isHideActionMode;
                        }

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public void onAnimationEnd(Animator animator) {
                            super.onAnimationEnd(animator);
                            EditableListViewWrapper.this.startActionMode();
                            EditableListViewWrapper.this.mAnimationManager.setLongTouchPosition(i);
                            EditableListViewWrapper.this.mIsPreInChoiceMode = false;
                        }
                    });
                } else {
                    EditableListViewWrapper.this.startActionMode();
                    EditableListViewWrapper.this.mAnimationManager.setLongTouchPosition(i);
                }
                return true;
            }
        }
    };
    public ItemClickSupport.OnItemClickListener mCheckItemListener = new ItemClickSupport.OnItemClickListener() { // from class: com.miui.gallery.widget.editwrapper.EditableListViewWrapper.5
        {
            EditableListViewWrapper.this = this;
        }

        @Override // com.miui.gallery.widget.recyclerview.ItemClickSupport.OnItemClickListener
        public boolean onItemClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
            if ((view instanceof Checkable) && EditableListViewWrapper.this.mAdapterHolder.isValidDataPosition(i)) {
                boolean z = !EditableListViewWrapper.this.mCheckState.getCheckState(i);
                EditableListViewWrapper.this.mCheckState.setCheckState(i, z);
                ((Checkable) view).setChecked(z);
                EditableListViewWrapper.this.updateActionMode();
                LinearMotorHelper.performHapticFeedback(view, LinearMotorHelper.HAPTIC_MESH_LIGHT);
                EditableListViewWrapper.this.mMultiChoiceModeCallback.mWrapped.onItemCheckedStateChanged(EditableListViewWrapper.this.mChoiceActionMode, i, j, z);
                if (view instanceof PickAnimationHelper$BackgroundImageViewable) {
                    EditableListViewWrapper.this.mAnimationManager.startScaleItemImageViewAnimation(((PickAnimationHelper$BackgroundImageViewable) view).getBackgroundImageView(), i);
                }
                if (z && (view instanceof PickAnimationHelper$ShowNumberWhenPicking)) {
                    EditableListViewWrapper.this.mAnimationManager.startPickingNumberAnimation((PickAnimationHelper$ShowNumberWhenPicking) view);
                }
                return true;
            }
            return false;
        }
    };

    /* loaded from: classes2.dex */
    public interface AdapterHolder extends CheckState.Source, WrapperSource<RecyclerView.Adapter> {
        int decryptPosition(int i);

        int encryptPosition(int i);

        RecyclerView.Adapter getAdapter();

        int getSpanIndex(int i, int i2);

        int getSpanSize(int i, int i2);

        boolean isGroupCheckable();

        boolean isTransformId();

        boolean isValidDataPosition(int i);

        int packGroupedPosition(int i, int i2);

        void registerAdapterObserver(RecyclerView.AdapterDataObserver adapterDataObserver);

        void setAdapter(RecyclerView.Adapter adapter);

        int[] unpackGroupedPosition(int i);

        void unregisterAdapterObserver(RecyclerView.AdapterDataObserver adapterDataObserver);
    }

    /* loaded from: classes2.dex */
    public interface DragDataProvider {
        String getBestPathFromPosition(int i);

        String getMimeTypeFromPosition(int i);

        boolean shouldStartDragInPhone();
    }

    /* loaded from: classes2.dex */
    public interface OnBindViewHolderHook {
        void onBind(RecyclerView.ViewHolder viewHolder, int i);
    }

    /* loaded from: classes2.dex */
    public interface OnCallNotifyDataSetChangeListener {
        boolean onNotifyEvent(RecyclerView.Adapter adapter);
    }

    /* loaded from: classes2.dex */
    public interface OnLongClickCheck {
        boolean canNext(RecyclerView recyclerView, View view, int i, long j);
    }

    /* loaded from: classes2.dex */
    public interface OnTouchCallBack {
        void onTouch(View view, MotionEvent motionEvent);
    }

    public static /* synthetic */ boolean $r8$lambda$8DBRn70hOJtw5dZhoOHQc3cVTlQ(EditableListViewWrapper editableListViewWrapper, ItemClickSupport.OnItemLongClickListener onItemLongClickListener, RecyclerView recyclerView, View view, int i, long j, float f, float f2, boolean z) {
        return editableListViewWrapper.lambda$setOnItemLongClickListener$0(onItemLongClickListener, recyclerView, view, i, j, f, f2, z);
    }

    public static /* synthetic */ boolean $r8$lambda$Ofjw5f4fLStxFqlYdRMUtqotqbg(EditableListViewWrapper editableListViewWrapper, ItemClickSupport.OnItemClickListener onItemClickListener, RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
        return editableListViewWrapper.lambda$setOnItemClickListener$1(onItemClickListener, recyclerView, view, i, j, f, f2);
    }

    /* renamed from: $r8$lambda$dly37b4-lmALbtY1DS9Nw_IPzEg */
    public static /* synthetic */ void m1812$r8$lambda$dly37b4lmALbtY1DS9Nw_IPzEg(EditableListViewWrapper editableListViewWrapper, int i, View view) {
        editableListViewWrapper.lambda$setCheckableHeaderView$2(i, view);
    }

    public EditableListViewWrapper(GalleryRecyclerView galleryRecyclerView) {
        internalInit(galleryRecyclerView, null);
        this.mCheckState = new CheckStateWithGroup(this.mRecyclerView, this.mAnimationManager);
        attachWatcher(galleryRecyclerView);
    }

    public EditableListViewWrapper(GalleryRecyclerView galleryRecyclerView, CheckState checkState) {
        internalInit(galleryRecyclerView, null);
        this.mCheckState = checkState;
        attachWatcher(galleryRecyclerView);
    }

    public final void attachWatcher(final GalleryRecyclerView galleryRecyclerView) {
        galleryRecyclerView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.miui.gallery.widget.editwrapper.EditableListViewWrapper.2
            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewAttachedToWindow(View view) {
            }

            {
                EditableListViewWrapper.this = this;
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewDetachedFromWindow(View view) {
                EditableListViewWrapper.this.release();
                galleryRecyclerView.removeOnAttachStateChangeListener(this);
            }
        });
    }

    public final void release() {
        this.mAnimationManager.stopPickScrolling();
        EditableListViewItemAnimHelper editableListViewItemAnimHelper = this.mItemAnimHelper;
        if (editableListViewItemAnimHelper != null) {
            editableListViewItemAnimHelper.release();
        }
    }

    public void reductionTouchView() {
        EditableListViewItemAnimHelper editableListViewItemAnimHelper = this.mItemAnimHelper;
        if (editableListViewItemAnimHelper != null) {
            editableListViewItemAnimHelper.reductionTouchView();
        }
    }

    public void setOnlyUpAlphaAnimEnable(boolean z) {
        EditableListViewItemAnimHelper editableListViewItemAnimHelper = this.mItemAnimHelper;
        if (editableListViewItemAnimHelper != null) {
            editableListViewItemAnimHelper.setOnlyUpAlphaAnimEnable(z);
        }
    }

    public void setDragDataProvider(DragDataProvider dragDataProvider) {
        this.mDragDataProvider = dragDataProvider;
    }

    public void setItemChecked(int i, boolean z) {
        if (this.mChoiceActionMode == null) {
            startActionMode();
        }
        int encryptPosition = this.mAdapterHolder.encryptPosition(i);
        if (encryptPosition == -1) {
            return;
        }
        this.mCheckState.setCheckState(encryptPosition, z);
        int findFirstVisibleItemPosition = this.mRecyclerView.findFirstVisibleItemPosition();
        if (encryptPosition <= findFirstVisibleItemPosition || encryptPosition >= this.mRecyclerView.getChildCount() + findFirstVisibleItemPosition) {
            return;
        }
        View childAt = this.mRecyclerView.getChildAt(encryptPosition - findFirstVisibleItemPosition);
        if (!(childAt instanceof Checkable)) {
            return;
        }
        ((Checkable) childAt).setChecked(z);
    }

    public final void internalInit(GalleryRecyclerView galleryRecyclerView, DragDataProvider dragDataProvider) {
        if (galleryRecyclerView == null) {
            throw new IllegalArgumentException("recyclerView can't be null");
        }
        this.mRecyclerView = galleryRecyclerView;
        this.mAnimationManager = new AnimationManager();
        this.mDragDataProvider = dragDataProvider;
        this.mAdapterWrappers = new ArrayMap<>();
        this.mAdapterObservableRegisterStatus = new ArrayMap<>(2);
        this.mRecyclerView.setOnTouchListener(this.mAnimationManager);
        this.mRecyclerView.addOnScrollListener(this.mScrollDelegate);
    }

    public void setEditActionModeButton2Func(int i) {
        this.mEditActionModeButton2Func = i;
    }

    public void setInitState(InitState initState) {
        this.mInitState = initState;
    }

    public void enableChoiceMode(boolean z) {
        enableChoiceMode(z, null);
    }

    public void enableChoiceMode(boolean z, OnLongClickCheck onLongClickCheck) {
        this.mRecyclerView.setLongClickable(z);
        this.mCanLongClickCheck = onLongClickCheck;
    }

    public void enterChoiceModeWithLongClick(boolean z) {
        setOnItemLongClickListener(z ? this.mEnterActionModeListener : null);
    }

    public void setOnItemLongClickListener(final ItemClickSupport.OnItemLongClickListener onItemLongClickListener) {
        if (onItemLongClickListener != null) {
            this.mRecyclerView.setOnItemLongClickListener(new ItemClickSupport.OnItemLongClickListener() { // from class: com.miui.gallery.widget.editwrapper.EditableListViewWrapper$$ExternalSyntheticLambda2
                @Override // com.miui.gallery.widget.recyclerview.ItemClickSupport.OnItemLongClickListener
                public final boolean onItemLongClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2, boolean z) {
                    return EditableListViewWrapper.$r8$lambda$8DBRn70hOJtw5dZhoOHQc3cVTlQ(EditableListViewWrapper.this, onItemLongClickListener, recyclerView, view, i, j, f, f2, z);
                }
            });
            return;
        }
        this.mRecyclerView.setOnItemLongClickListener(null);
    }

    public /* synthetic */ boolean lambda$setOnItemLongClickListener$0(ItemClickSupport.OnItemLongClickListener onItemLongClickListener, RecyclerView recyclerView, View view, int i, long j, float f, float f2, boolean z) {
        if (getDataPosition(i) == -1) {
            return false;
        }
        if (this.mDragDataProvider != null) {
            if (isInChoiceMode() && isPositionChecked(i) && (z || this.mDragDataProvider.shouldStartDragInPhone())) {
                return startDragDrop(view);
            }
            if (!isInChoiceMode() && z) {
                startDragDropDirectly(view, i);
            }
        }
        return onItemLongClickListener.onItemLongClick(recyclerView, view, i, j, f, f2, z);
    }

    public final boolean startDragDropDirectly(View view, int i) {
        int decryptPosition = this.mAdapterHolder.decryptPosition(i);
        String bestPathFromPosition = this.mDragDataProvider.getBestPathFromPosition(decryptPosition);
        ArrayList<ClipData.Item> arrayList = new ArrayList<>();
        String[] strArr = new String[1];
        if (bestPathFromPosition != null) {
            Uri translateToContent = GalleryOpenProvider.translateToContent(bestPathFromPosition);
            GalleryApp.sGetAndroidContext().grantUriPermission("com.miui.mishare", translateToContent, 1);
            GalleryApp.sGetAndroidContext().grantUriPermission("com.tencent.mm", translateToContent, 1);
            arrayList.add(new ClipData.Item(translateToContent));
            strArr[0] = this.mDragDataProvider.getMimeTypeFromPosition(decryptPosition);
        }
        return startDragDropInternal(view, 1, strArr, arrayList);
    }

    public final boolean startDragDrop(View view) {
        int[] checkedItemOrderedPositions = getCheckedItemOrderedPositions();
        if (checkedItemOrderedPositions.length == 0) {
            return false;
        }
        ArrayList<ClipData.Item> arrayList = new ArrayList<>();
        String[] strArr = new String[checkedItemOrderedPositions.length];
        for (int i = 0; i < checkedItemOrderedPositions.length; i++) {
            int i2 = checkedItemOrderedPositions[i];
            String bestPathFromPosition = this.mDragDataProvider.getBestPathFromPosition(i2);
            if (bestPathFromPosition != null) {
                Uri translateToContent = GalleryOpenProvider.translateToContent(bestPathFromPosition);
                GalleryApp.sGetAndroidContext().grantUriPermission("com.miui.mishare", translateToContent, 1);
                GalleryApp.sGetAndroidContext().grantUriPermission("com.tencent.mm", translateToContent, 1);
                arrayList.add(new ClipData.Item(translateToContent));
                strArr[i] = this.mDragDataProvider.getMimeTypeFromPosition(i2);
            }
        }
        return startDragDropInternal(view, checkedItemOrderedPositions.length, strArr, arrayList);
    }

    public final boolean startDragDropInternal(View view, int i, String[] strArr, ArrayList<ClipData.Item> arrayList) {
        MiuiDragShadowBuilder miuiDragShadowBuilder = new MiuiDragShadowBuilder(view.getContext());
        miuiDragShadowBuilder.setCount(i);
        Bitmap captureSnapshot = ViewUtils.captureSnapshot(view);
        if (captureSnapshot != null) {
            miuiDragShadowBuilder.setThumb(captureSnapshot);
        }
        ClipDescription clipDescription = new ClipDescription("MiuiGallery", strArr);
        if (arrayList.size() == 0) {
            return false;
        }
        Iterator<ClipData.Item> it = arrayList.iterator();
        ClipData clipData = null;
        while (it.hasNext()) {
            ClipData.Item next = it.next();
            if (clipData == null) {
                clipData = new ClipData(clipDescription, next);
            } else {
                clipData.addItem(next);
            }
        }
        view.startDragAndDrop(clipData, miuiDragShadowBuilder, null, SurfaceRatio.SURFACE_NO_FIT);
        return true;
    }

    public void enableScaleImageViewAniWhenInActionMode() {
        this.mAnimationManager.enableScaleImageViewAni();
    }

    public void setOnBindViewHolderHook(OnBindViewHolderHook onBindViewHolderHook, boolean z) {
        this.mOnBindViewHolderHook = onBindViewHolderHook;
        this.mIsAfterBindViewHolderHook = z;
    }

    public void clearBindViewHolderHook() {
        this.mOnBindViewHolderHook = null;
    }

    public void disableScaleImageViewAniWhenInActionMode() {
        this.mAnimationManager.disableScaleImageViewAni();
    }

    public void setAdapter(RecyclerView.Adapter adapter, HeaderFooterWrapper headerFooterWrapper) {
        AdapterHolder adapterHolder = this.mAdapterHolder;
        AdapterHolder adapterHolder2 = this.mAdapterWrappers.get(adapter);
        this.mAdapterHolder = adapterHolder2;
        if (adapterHolder != null && adapterHolder == adapterHolder2) {
            DefaultLogger.d("EditableListViewWrapper", "setAdapter the same adapter");
            return;
        }
        this.mCheckState.clear();
        AdapterHolder adapterHolder3 = this.mAdapterHolder;
        if (adapterHolder3 != null) {
            adapterHolder3.unregisterAdapterObserver(this.mRestoreCheckStateObserver);
            this.mAdapterObservableRegisterStatus.remove(adapter);
        }
        if (adapter == null) {
            this.mAdapterHolder = null;
            this.mRecyclerView.setAdapter(null);
            handleDataInvalid();
            return;
        }
        if (this.mAdapterHolder == null) {
            if (((GroupedItemAdapter) WrapperAdapterUtils.findWrappedAdapter(adapter, GroupedItemAdapter.class)) == null) {
                throw new IllegalArgumentException("Only GroupedItemAdapter is supported");
            }
            GroupedItemManager groupedItemManager = new GroupedItemManager();
            AbstractHeaderFooterWrapperAdapter wrap = headerFooterWrapper.wrap(groupedItemManager.createWrappedAdapter(new GroupedAdapterHolder(adapter, groupedItemManager)));
            HeaderFooterGroupedAdapterHolder headerFooterGroupedAdapterHolder = new HeaderFooterGroupedAdapterHolder(wrap, groupedItemManager);
            this.mAdapterHolder = headerFooterGroupedAdapterHolder;
            headerFooterGroupedAdapterHolder.setAdapter(wrap);
            this.mAdapterWrappers.put(adapter, this.mAdapterHolder);
        }
        if (this.mAdapterObservableRegisterStatus == null) {
            this.mAdapterObservableRegisterStatus = new ArrayMap<>(2);
        }
        Boolean bool = this.mAdapterObservableRegisterStatus.get(adapter);
        if (bool == null || !bool.booleanValue()) {
            this.mAdapterHolder.registerAdapterObserver(this.mRestoreCheckStateObserver);
            this.mAdapterObservableRegisterStatus.put(adapter, Boolean.TRUE);
        }
        this.mRecyclerView.setAdapter(this.mAdapterHolder.getAdapter());
        this.mCheckState.bind(this.mAdapterHolder);
        handleDataChanged();
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        RecyclerView.Adapter adapter2 = adapter instanceof WrapperSource ? (RecyclerView.Adapter) ((WrapperSource) adapter).mo1836getSource() : null;
        if (adapter2 != null) {
            this.mAdapterWrappers.remove(adapter2);
            adapter = adapter2;
        }
        AdapterHolder adapterHolder = this.mAdapterHolder;
        AdapterHolder adapterHolder2 = this.mAdapterWrappers.get(adapter);
        this.mAdapterHolder = adapterHolder2;
        if (adapterHolder != null && adapterHolder == adapterHolder2) {
            DefaultLogger.d("EditableListViewWrapper", "setAdapter the same adapter");
            return;
        }
        this.mCheckState.clear();
        AdapterHolder adapterHolder3 = this.mAdapterHolder;
        if (adapterHolder3 != null) {
            adapterHolder3.unregisterAdapterObserver(this.mRestoreCheckStateObserver);
            this.mAdapterObservableRegisterStatus.remove(adapter);
        }
        if (adapter == null) {
            this.mAdapterHolder = null;
            this.mRecyclerView.setAdapter(null);
            handleDataInvalid();
            return;
        }
        if (this.mAdapterHolder == null) {
            if (((GroupedItemAdapter) WrapperAdapterUtils.findWrappedAdapter(adapter, GroupedItemAdapter.class)) != null) {
                GroupedItemManager groupedItemManager = new GroupedItemManager();
                GroupedAdapterHolder groupedAdapterHolder = new GroupedAdapterHolder(adapter, groupedItemManager);
                this.mAdapterHolder = groupedAdapterHolder;
                this.mAdapterHolder.setAdapter(groupedItemManager.createWrappedAdapter(groupedAdapterHolder));
            } else {
                SimpleHolder simpleHolder = new SimpleHolder(adapter);
                this.mAdapterHolder = simpleHolder;
                simpleHolder.setAdapter(simpleHolder);
            }
            this.mAdapterWrappers.put(adapter, this.mAdapterHolder);
        }
        if (this.mAdapterObservableRegisterStatus == null) {
            this.mAdapterObservableRegisterStatus = new ArrayMap<>(2);
        }
        Boolean bool = this.mAdapterObservableRegisterStatus.get(adapter);
        if (bool == null || !bool.booleanValue()) {
            this.mAdapterHolder.registerAdapterObserver(this.mRestoreCheckStateObserver);
            this.mAdapterObservableRegisterStatus.put(adapter, Boolean.TRUE);
        }
        this.mRecyclerView.setAdapter(this.mAdapterHolder.getAdapter());
        this.mCheckState.bind(this.mAdapterHolder);
        handleDataChanged();
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.mRecyclerView.setLayoutManager(layoutManager);
    }

    public void setOnItemClickListener(final ItemClickSupport.OnItemClickListener onItemClickListener) {
        if (onItemClickListener != null) {
            ItemClickSupport.OnItemClickListener onItemClickListener2 = new ItemClickSupport.OnItemClickListener() { // from class: com.miui.gallery.widget.editwrapper.EditableListViewWrapper$$ExternalSyntheticLambda1
                @Override // com.miui.gallery.widget.recyclerview.ItemClickSupport.OnItemClickListener
                public final boolean onItemClick(RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
                    return EditableListViewWrapper.$r8$lambda$Ofjw5f4fLStxFqlYdRMUtqotqbg(EditableListViewWrapper.this, onItemClickListener, recyclerView, view, i, j, f, f2);
                }
            };
            this.mItemClickDelegate = onItemClickListener2;
            this.mRecyclerView.setOnItemClickListener(onItemClickListener2);
            return;
        }
        this.mItemClickDelegate = null;
        this.mRecyclerView.setOnItemClickListener(null);
    }

    public /* synthetic */ boolean lambda$setOnItemClickListener$1(ItemClickSupport.OnItemClickListener onItemClickListener, RecyclerView recyclerView, View view, int i, long j, float f, float f2) {
        int dataPosition = getDataPosition(i);
        if (dataPosition == -1) {
            return false;
        }
        return onItemClickListener.onItemClick(recyclerView, view, dataPosition, j, f, f2);
    }

    public void setOnTouchCallback(OnTouchCallBack onTouchCallBack) {
        this.mOnTouchCallback = onTouchCallBack;
    }

    public void setOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        this.mScrollListener = onScrollListener;
    }

    public void setEmptyView(View view) {
        this.mRecyclerView.setEmptyView(view);
    }

    public void setMultiChoiceModeListener(MultiChoiceModeListener multiChoiceModeListener) {
        if (this.mMultiChoiceModeCallback == null) {
            this.mMultiChoiceModeCallback = new MultiChoiceModeCallback();
        }
        this.mMultiChoiceModeCallback.setWrapped(multiChoiceModeListener);
    }

    public List<Integer> getCheckedPositions() {
        List<Integer> checkedPositions = this.mCheckState.getCheckedPositions();
        LinkedList linkedList = new LinkedList();
        for (Integer num : checkedPositions) {
            int decryptPosition = this.mAdapterHolder.decryptPosition(num.intValue());
            if (decryptPosition != -1) {
                linkedList.add(Integer.valueOf(decryptPosition));
            }
        }
        return linkedList;
    }

    public int[] getAfterDeletedFirstVisiblePosAndOffset() {
        int[] iArr = new int[2];
        Rect rect = new Rect();
        int i = 0;
        int i2 = 0;
        while (i < this.mRecyclerView.getChildCount()) {
            View childAt = this.mRecyclerView.getChildAt(i);
            int childAdapterPosition = this.mRecyclerView.getChildAdapterPosition(childAt);
            if (!this.mAdapterHolder.isValidDataPosition(childAdapterPosition)) {
                if (this.mAdapterHolder.isGroupCheckable() && (this.mCheckState instanceof CheckStateWithGroup)) {
                    if (!((CheckStateWithGroup) this.mCheckState).getGroupCheckState(this.mAdapterHolder.getSourceItemGroupIndex(childAdapterPosition))) {
                        childAt.getLocalVisibleRect(rect);
                        i2 = childAdapterPosition;
                        break;
                    }
                }
                i++;
                i2 = childAdapterPosition;
            } else if (!this.mCheckState.getCheckState(childAdapterPosition)) {
                childAt.getLocalVisibleRect(rect);
                i2 = childAdapterPosition;
                break;
            } else {
                i++;
                i2 = childAdapterPosition;
            }
        }
        int i3 = 0;
        for (int i4 = 0; i4 <= i2; i4++) {
            if (!this.mAdapterHolder.isValidDataPosition(i4)) {
                if (this.mAdapterHolder.isGroupCheckable() && (this.mCheckState instanceof CheckStateWithGroup)) {
                    if (!((CheckStateWithGroup) this.mCheckState).getGroupCheckState(this.mAdapterHolder.getSourceItemGroupIndex(i4))) {
                    }
                    i3++;
                }
            } else {
                if (!this.mCheckState.getCheckState(i4)) {
                }
                i3++;
            }
        }
        iArr[0] = i2 - i3;
        int i5 = rect.top;
        if (i5 > 0) {
            iArr[1] = -i5;
        } else {
            iArr[1] = i5;
        }
        return iArr;
    }

    @ViewDebug.CapturedViewProperty
    public int getCount() {
        AdapterHolder adapterHolder = this.mAdapterHolder;
        if (adapterHolder == null) {
            return 0;
        }
        return adapterHolder.getSourceItemCount();
    }

    public boolean isInActionMode() {
        return this.mIsInActionMode;
    }

    public boolean isItemChecked(long j) {
        return this.mCheckState.getCheckState(j);
    }

    public long[] getCheckedItemIds() {
        long[] checkedItemIds = this.mCheckState.getCheckedItemIds();
        AdapterHolder adapterHolder = this.mAdapterHolder;
        if (adapterHolder != null && adapterHolder.isTransformId()) {
            for (int i = 0; i < checkedItemIds.length; i++) {
                checkedItemIds[i] = ItemIdComposer.extractExpandableChildIdPart(checkedItemIds[i]);
            }
        }
        return checkedItemIds;
    }

    public int[] getCheckedItemOrderedPositions() {
        int[] checkedItemOrderedPositions = this.mCheckState.getCheckedItemOrderedPositions();
        int[] iArr = new int[checkedItemOrderedPositions.length];
        for (int i = 0; i < checkedItemOrderedPositions.length; i++) {
            iArr[i] = this.mAdapterHolder.decryptPosition(checkedItemOrderedPositions[i]);
        }
        return iArr;
    }

    public final boolean isPositionChecked(int i) {
        return this.mCheckState.getCheckState(i);
    }

    public boolean isCheckedItemContainVideo() {
        AdapterHolder adapterHolder = this.mAdapterHolder;
        if (adapterHolder != null) {
            RecyclerView.Adapter mo1836getSource = adapterHolder.mo1836getSource();
            while (mo1836getSource instanceof AbstractHeaderFooterWrapperAdapter) {
                mo1836getSource = ((AbstractHeaderFooterWrapperAdapter) mo1836getSource).getWrappedAdapter();
            }
            BaseMediaAdapter baseMediaAdapter = (BaseMediaAdapter) WrapperAdapterUtils.findWrappedAdapter(mo1836getSource, BaseMediaAdapter.class);
            if (baseMediaAdapter != null) {
                List<Integer> checkedPositions = getCheckedPositions();
                for (int i = 0; i < checkedPositions.size(); i++) {
                    if (BaseFileMimeUtil.isVideoFromMimeType(baseMediaAdapter.getMimeType(checkedPositions.get(i).intValue()))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public List<CheckableAdapter.CheckedItem> getCheckedItems() {
        int[] checkedItemOrderedPositions;
        ArrayList arrayList = new ArrayList();
        AdapterHolder adapterHolder = this.mAdapterHolder;
        if (adapterHolder != null) {
            RecyclerView.Adapter mo1836getSource = adapterHolder.mo1836getSource();
            while (mo1836getSource instanceof AbstractHeaderFooterWrapperAdapter) {
                mo1836getSource = ((AbstractHeaderFooterWrapperAdapter) mo1836getSource).getWrappedAdapter();
            }
            BaseMediaAdapter baseMediaAdapter = (BaseMediaAdapter) WrapperAdapterUtils.findWrappedAdapter(mo1836getSource, BaseMediaAdapter.class);
            if (baseMediaAdapter != null && (checkedItemOrderedPositions = getCheckedItemOrderedPositions()) != null) {
                for (int i : checkedItemOrderedPositions) {
                    CheckableAdapter.CheckedItem checkedItem = baseMediaAdapter.getCheckedItem(i);
                    if (checkedItem != null) {
                        arrayList.add(checkedItem);
                    }
                }
            }
        }
        return arrayList;
    }

    public int getCheckedItemCount() {
        return this.mCheckState.getCheckedCount();
    }

    public final void checkMultiChoiceModeCallback() {
        MultiChoiceModeCallback multiChoiceModeCallback = this.mMultiChoiceModeCallback;
        if (multiChoiceModeCallback == null || !multiChoiceModeCallback.hasWrappedCallback()) {
            throw new IllegalStateException("no MultiChoiceModeListener is set");
        }
    }

    public boolean isInChoiceMode() {
        return this.mIsInChoiceMode;
    }

    public boolean isStartingActionMode() {
        return this.isActionModeEnterAnimStarting;
    }

    public void startActionMode() {
        checkMultiChoiceModeCallback();
        if (this.mIsInChoiceMode) {
            return;
        }
        InitState initState = this.mInitState;
        if (initState != null) {
            SparseBooleanArray initState2 = initState.getInitState(this.mAdapterHolder.mo1836getSource());
            SparseBooleanArray sparseBooleanArray = new SparseBooleanArray(initState2.size());
            for (int i = 0; i < initState2.size(); i++) {
                sparseBooleanArray.put(this.mAdapterHolder.encryptPosition(initState2.keyAt(i)), initState2.get(i));
            }
            this.mCheckState.setCheckState(sparseBooleanArray);
        } else {
            this.mCheckState.clear();
        }
        internalStartActionMode();
        if (this.mEditActionModeButton2Func != 0) {
            return;
        }
        this.mMultiChoiceModeCallback.mWrapped.onAllItemsCheckedStateChanged(this.mChoiceActionMode, false);
    }

    public final void internalStartActionMode() {
        if (this.mIsInChoiceMode || this.isHideActionMode) {
            return;
        }
        ActionMode startActionMode = this.mRecyclerView.startActionMode(this.mMultiChoiceModeCallback);
        this.mChoiceActionMode = startActionMode;
        if (startActionMode instanceof EditActionMode) {
            this.mEditActionMode = (EditActionMode) startActionMode;
        }
        this.mEditActionMode.addAnimationListener(new ActionModeAnimationListener() { // from class: com.miui.gallery.widget.editwrapper.EditableListViewWrapper.4
            @Override // miuix.view.ActionModeAnimationListener
            public void onUpdate(boolean z, float f) {
            }

            {
                EditableListViewWrapper.this = this;
            }

            @Override // miuix.view.ActionModeAnimationListener
            public void onStart(boolean z) {
                if (z) {
                    EditableListViewWrapper.this.isActionModeEnterAnimStarting = true;
                }
            }

            @Override // miuix.view.ActionModeAnimationListener
            public void onStop(boolean z) {
                if (z) {
                    EditableListViewWrapper.this.isActionModeEnterAnimStarting = false;
                    if (!EditableListViewWrapper.this.isPendingActionModeFinish) {
                        return;
                    }
                    EditableListViewWrapper.this.mRecyclerView.post(new Runnable() { // from class: com.miui.gallery.widget.editwrapper.EditableListViewWrapper.4.1
                        {
                            AnonymousClass4.this = this;
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            EditableListViewWrapper.this.mChoiceActionMode.finish();
                            EditableListViewWrapper.this.isPendingActionModeFinish = false;
                        }
                    });
                }
            }
        });
        this.mIsInChoiceMode = true;
    }

    public void stopActionMode() {
        if (this.mChoiceActionMode == null || !isInActionMode()) {
            return;
        }
        this.isHideActionMode = false;
        if (this.isActionModeEnterAnimStarting) {
            this.isPendingActionModeFinish = true;
        } else {
            this.mChoiceActionMode.finish();
        }
    }

    public void startChoiceMode() {
        checkMultiChoiceModeCallback();
        if (this.mIsInChoiceMode) {
            return;
        }
        this.mIsInChoiceMode = true;
        enterChoiceMode();
    }

    public int[] unpackGroupedPosition(int i) {
        return this.mAdapterHolder.unpackGroupedPosition(i);
    }

    public int getSpanSize(int i, int i2) {
        return this.mAdapterHolder.getSpanSize(i, i2);
    }

    public int getSpanIndex(int i, int i2) {
        return this.mAdapterHolder.getSpanIndex(i, i2);
    }

    public int getDataPosition(int i) {
        return this.mAdapterHolder.decryptPosition(i);
    }

    public int getRawPosition(int i) {
        return this.mAdapterHolder.encryptPosition(i);
    }

    public void enableActionModeItemAnim(boolean z) {
        this.enableActionModeItemAnim = z;
    }

    public void setEnableContinuousPick(boolean z) {
        this.mAnimationManager.setContinuousPickEnable(z);
    }

    public void setScrollPickEnable(boolean z) {
        this.mAnimationManager.setAutoScrollPickEnable(z);
    }

    public void setLongTouchPosition(int i) {
        this.mAnimationManager.setLongTouchPosition(i);
    }

    public int getLongTouchPosition() {
        return this.mAnimationManager.getLongTouchPosition();
    }

    public void setEditableListViewItemAnimHelper(EditableListViewItemAnimHelper editableListViewItemAnimHelper, String... strArr) {
        this.mItemAnimHelper = editableListViewItemAnimHelper;
        if (editableListViewItemAnimHelper != null) {
            editableListViewItemAnimHelper.saveHandleTouchAnimItemTyp(strArr);
        }
    }

    public final EditableListViewItemAnimHelper createCommonItemAnimHelper() {
        return new EditableListViewItemAnimHelper.Builder().withDownAnim().withUpAnim().build();
    }

    public void setItemAnimEnable(boolean z) {
        this.isItemAnimEnable = z;
    }

    public void setHandleTouchAnimItemType(String... strArr) {
        if (strArr == null || strArr.length == 0) {
            return;
        }
        if (this.mItemAnimHelper == null) {
            this.mItemAnimHelper = createCommonItemAnimHelper();
        }
        this.mItemAnimHelper.saveHandleTouchAnimItemTyp(strArr);
    }

    /* loaded from: classes2.dex */
    public class AnimationManager implements View.OnTouchListener {
        public boolean mLongPress;
        public ImageView.ScaleType mOriginalScaleType = null;
        public Scroller2PickHelper mScroll2PickHelper = new Scroller2PickHelper();
        public boolean isTurnOnScaleImageViewAni = false;

        public AnimationManager() {
            EditableListViewWrapper.this = r1;
        }

        public void setLongPress(boolean z) {
            this.mLongPress = z;
        }

        public void setLongTouchPosition(int i) {
            this.mScroll2PickHelper.setLongPressPosition(i);
        }

        public int getLongTouchPosition() {
            return this.mScroll2PickHelper.getLongTouchPosition();
        }

        public void enableScaleImageViewAni() {
            this.isTurnOnScaleImageViewAni = true;
        }

        public void disableScaleImageViewAni() {
            this.isTurnOnScaleImageViewAni = false;
        }

        /* loaded from: classes2.dex */
        public class MyScaleItemImageViewAnimatorListener implements ValueAnimator.AnimatorUpdateListener {
            public ImageView mImage;
            public Matrix mPrimaryMatrix;

            public MyScaleItemImageViewAnimatorListener(ImageView imageView) {
                AnimationManager.this = r2;
                this.mPrimaryMatrix = new Matrix(imageView.getImageMatrix());
                this.mImage = imageView;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                AnimationManager.this.setImageViewScale(this.mImage, ((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        }

        public final void finishImageViewScaleAnimatorIfNecessary(ImageView imageView) {
            ValueAnimator valueAnimator = (ValueAnimator) imageView.getTag(R.id.tag_animator);
            if (valueAnimator != null && valueAnimator.isRunning()) {
                valueAnimator.end();
            }
            if (valueAnimator != null) {
                float imageViewScale = getImageViewScale(imageView);
                float floatValue = ((Float) imageView.getTag(R.id.tag_target_scale)).floatValue();
                if (Math.abs(imageViewScale - floatValue) <= Float.MIN_NORMAL) {
                    return;
                }
                setImageViewScale(imageView, floatValue);
            }
        }

        public final void startScaleItemImageViewAnimationInternal(ImageView imageView, boolean z, int i, int i2, int i3) {
            ValueAnimator ofFloat;
            if (!this.isTurnOnScaleImageViewAni) {
                return;
            }
            finishImageViewScaleAnimatorIfNecessary(imageView);
            Drawable drawable = imageView.getDrawable();
            if (drawable == null || drawable.getIntrinsicWidth() <= 0) {
                return;
            }
            if (z) {
                setImageViewScale(imageView, 1.0f);
                ofFloat = ValueAnimator.ofFloat(1.0f, 1.0f);
                ofFloat.addUpdateListener(new MyScaleItemImageViewAnimatorListener(imageView));
                ofFloat.setInterpolator(new CubicEaseOutInterpolator());
                imageView.setTag(R.id.tag_matrix, imageView.getImageMatrix());
                if (this.mOriginalScaleType == null) {
                    this.mOriginalScaleType = imageView.getScaleType();
                }
                imageView.setTag(R.id.tag_target_scale, Float.valueOf(1.0f));
            } else {
                setImageViewScale(imageView, 1.0f);
                ofFloat = ValueAnimator.ofFloat(1.0f, 1.0f);
                ofFloat.addUpdateListener(new MyScaleItemImageViewAnimatorListener(imageView));
                ofFloat.setInterpolator(new CubicEaseOutInterpolator());
                imageView.setTag(R.id.tag_matrix, null);
                imageView.setTag(R.id.tag_target_scale, Float.valueOf(1.0f));
            }
            ofFloat.setDuration(i3);
            ofFloat.setStartDelay(i2);
            imageView.setScaleType(ImageView.ScaleType.MATRIX);
            imageView.setTag(R.id.tag_animator, ofFloat);
            ofFloat.start();
        }

        public void startScaleItemImageViewAnimation(ImageView imageView, int i) {
            if (imageView == null) {
                return;
            }
            if (imageView.getTag(R.id.tag_matrix) == null) {
                startScaleItemImageViewAnimationInternal(imageView, true, i, 100, UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME);
            } else {
                startScaleItemImageViewAnimationInternal(imageView, false, i, 100, UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME);
            }
        }

        public void startScaleItemImageViewAnimation(ImageView imageView, int i, boolean z, int i2) {
            startScaleItemImageViewAnimation(imageView, i, z, i2, UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME);
        }

        public void startScaleItemImageViewAnimation(ImageView imageView, int i, boolean z, int i2, int i3) {
            if (imageView == null) {
                return;
            }
            if (imageView.getTag(R.id.tag_matrix) == null && z) {
                startScaleItemImageViewAnimationInternal(imageView, true, i, i2, i3);
            }
            if (imageView.getTag(R.id.tag_matrix) == null || z) {
                return;
            }
            startScaleItemImageViewAnimationInternal(imageView, false, i, i2, i3);
        }

        public void startEnterActionModeAni() {
            if (EditableListViewWrapper.this.enableActionModeItemAnim) {
                startScaleListViewAnimation(EditableListViewWrapper.this.mRecyclerView, false);
            }
        }

        public void startExistActionModeAni() {
            if (EditableListViewWrapper.this.enableActionModeItemAnim) {
                startScaleListViewAnimation(EditableListViewWrapper.this.mRecyclerView, true);
            }
        }

        public final void startScaleListViewAnimation(GalleryRecyclerView galleryRecyclerView, boolean z) {
            for (int i = 0; i < galleryRecyclerView.getChildCount(); i++) {
                View childAt = galleryRecyclerView.getChildAt(i);
                if ((childAt instanceof Checkable) && EditableListViewWrapper.this.mAdapterHolder.isValidDataPosition(galleryRecyclerView.getChildAdapterPositionForExternal(childAt))) {
                    ObjectAnimator duration = ObjectAnimator.ofPropertyValuesHolder(childAt, PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.0f), PropertyValuesHolder.ofFloat("scaleY", 1.0f, 1.0f)).setDuration(300L);
                    duration.setInterpolator(new CubicEaseOutInterpolator());
                    duration.start();
                    childAt.setTag(R.id.tag_scale_factor, Float.valueOf(1.0f));
                    if (z && (childAt instanceof PickAnimationHelper$BackgroundImageViewable)) {
                        ImageView backgroundImageView = ((PickAnimationHelper$BackgroundImageViewable) childAt).getBackgroundImageView();
                        if (backgroundImageView == null) {
                            return;
                        }
                        if (backgroundImageView.getTag(R.id.tag_matrix) != null) {
                            startScaleItemImageViewAnimationInternal(backgroundImageView, false, galleryRecyclerView.getChildAdapterPositionForExternal(childAt), 0, UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME);
                        }
                    }
                }
            }
        }

        public void startPickingNumberAnimation(PickAnimationHelper$ShowNumberWhenPicking pickAnimationHelper$ShowNumberWhenPicking) {
            ImageView backgroundMask = pickAnimationHelper$ShowNumberWhenPicking.getBackgroundMask();
            backgroundMask.setVisibility(0);
            TextView showNumberTextView = pickAnimationHelper$ShowNumberWhenPicking.getShowNumberTextView();
            showNumberTextView.setText(String.format("%d", Integer.valueOf(EditableListViewWrapper.this.getCheckedItemCount())));
            ObjectAnimator duration = ObjectAnimator.ofPropertyValuesHolder(showNumberTextView, PropertyValuesHolder.ofFloat("alpha", 0.0f, 1.0f), PropertyValuesHolder.ofFloat("scaleX", 0.8f, 1.0f), PropertyValuesHolder.ofFloat("scaleY", 0.8f, 1.0f)).setDuration(300L);
            duration.setInterpolator(new CubicEaseOutInterpolator());
            ObjectAnimator duration2 = ObjectAnimator.ofPropertyValuesHolder(showNumberTextView, PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.0f)).setDuration(200L);
            duration2.setInterpolator(new CubicEaseOutInterpolator());
            duration2.setStartDelay(200L);
            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playSequentially(duration, duration2);
            animatorSet.start();
            ObjectAnimator duration3 = ObjectAnimator.ofPropertyValuesHolder(backgroundMask, PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.0f)).setDuration(700L);
            duration3.setInterpolator(new CubicEaseOutInterpolator());
            duration3.start();
        }

        public void resetViewPropertyIfNeed(View view, View view2, int i) {
            if (!this.isTurnOnScaleImageViewAni) {
                return;
            }
            Object tag = view.getTag(R.id.tag_scale_factor);
            if (EditableListViewWrapper.this.isInChoiceMode()) {
                if (tag == null || !BaseMiscUtil.floatEquals(((Float) tag).floatValue(), 1.0f)) {
                    view.setScaleX(1.0f);
                    view.setScaleY(1.0f);
                    view.setTag(R.id.tag_scale_factor, Float.valueOf(1.0f));
                }
            } else if (tag == null || !BaseMiscUtil.floatEquals(((Float) tag).floatValue(), 1.0f)) {
                view.setScaleX(1.0f);
                view.setScaleY(1.0f);
                view.setTag(R.id.tag_scale_factor, Float.valueOf(1.0f));
            }
            if (view instanceof PickAnimationHelper$BackgroundImageViewable) {
                ImageView backgroundImageView = ((PickAnimationHelper$BackgroundImageViewable) view).getBackgroundImageView();
                if (backgroundImageView == null) {
                    return;
                }
                if (EditableListViewWrapper.this.isInChoiceMode()) {
                    if (view2.getTag(R.id.tag_pick_position) != null && ((Integer) view2.getTag(R.id.tag_pick_position)).intValue() != i && !EditableListViewWrapper.this.mCheckState.getCheckState(i)) {
                        setItemImageView2OriginalScaleAfterEnlarge(backgroundImageView);
                    }
                    if (EditableListViewWrapper.this.mCheckState.getCheckState(i) && backgroundImageView.getTag(R.id.tag_matrix) == null) {
                        setItemImageViewEnlargeAfterChecked(backgroundImageView, i);
                    }
                } else if (backgroundImageView.getTag(R.id.tag_matrix) != null) {
                    setItemImageView2OriginalScaleAfterEnlarge(backgroundImageView);
                }
            }
            view2.setTag(R.id.tag_pick_position, Integer.valueOf(i));
        }

        public final void setItemImageView2OriginalScaleAfterEnlarge(ImageView imageView) {
            finishImageViewScaleAnimatorIfNecessary(imageView);
            setImageViewScale(imageView, 1.0f);
            ImageView.ScaleType scaleType = this.mOriginalScaleType;
            if (scaleType != null) {
                imageView.setScaleType(scaleType);
            }
            imageView.setTag(R.id.tag_matrix, null);
            imageView.setTag(R.id.tag_animator, null);
        }

        public final void setItemImageViewEnlargeAfterChecked(ImageView imageView, int i) {
            finishImageViewScaleAnimatorIfNecessary(imageView);
            imageView.setTag(R.id.tag_matrix, imageView.getImageMatrix());
            setImageViewScale(imageView, 1.0f);
            imageView.setTag(R.id.tag_animator, null);
        }

        public final void setImageViewScale(ImageView imageView, float f) {
            if (imageView instanceof ScalableImageView) {
                ((ScalableImageView) imageView).setMatrixScale(f);
            }
        }

        public final float getImageViewScale(ImageView imageView) {
            if (imageView instanceof ScalableImageView) {
                return ((ScalableImageView) imageView).getMatrixScale();
            }
            return 0.0f;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (EditableListViewWrapper.this.mOnTouchCallback != null) {
                EditableListViewWrapper.this.mOnTouchCallback.onTouch(view, motionEvent);
            }
            return this.mScroll2PickHelper.onTouch(view, motionEvent);
        }

        public void setContinuousPickEnable(boolean z) {
            this.mScroll2PickHelper.setContinuousPickEnable(z);
        }

        public void setMovePickEnable(boolean z) {
            this.mScroll2PickHelper.setMovePickEnable(z);
        }

        public void setAutoScrollPickEnable(boolean z) {
            this.mScroll2PickHelper.setAutoScrollPickEnable(z);
        }

        public boolean isAutoPickScrolling() {
            return this.mScroll2PickHelper.isAutoScrolling();
        }

        public void stopPickScrolling() {
            this.mScroll2PickHelper.stopScrolling();
        }

        /* loaded from: classes2.dex */
        public class Scroller2PickHelper implements View.OnTouchListener {
            public boolean fakeEventSent;
            public int mBottomSpotBoundFrom;
            public int mBottomSpotBoundTo;
            public float mCurX;
            public float mCurY;
            public boolean mFirstPickChecked;
            public boolean mHasEverPickLongTouchPosition;
            public boolean mInBottomSpot;
            public boolean mInTopSpot;
            public boolean mIsDownConsume;
            public float mLastX;
            public float mLastY;
            public int mScreenHeight;
            public int mScrollDy;
            public float mScrollSpeedFactor;
            public boolean mScrollXMode2PickEnable;
            public boolean mScrollYMode2PickEnable;
            public int mSpotBoundInListHeight;
            public int mTopSpotBoundFrom;
            public int mTopSpotBoundTo;
            public boolean mUserWantToScroll;
            public boolean mIsEnableContinuousPick = true;
            public int mFirstTouchPosition = -1;
            public int mLastTouchPosition = -1;
            public int mTouchingPosition = -1;
            public int mLongTouchPosition = -1;
            public boolean isAutoScrollPickEnable = true;
            public boolean mIsMovePickEnable = true;
            public int mMaxScrollDy = 15;
            public View touchView = null;
            public Rect mRecyclerViewVisibleRect = new Rect();
            public int[] mRecyclerViewLocation = new int[2];
            public ScrollRunnable mScrollRunnable = new ScrollRunnable();

            public Scroller2PickHelper() {
                AnimationManager.this = r3;
                this.mScreenHeight = EditableListViewWrapper.this.mRecyclerView.getContext().getResources().getDisplayMetrics().heightPixels;
            }

            public final void setLongPressPosition(int i) {
                this.mLongTouchPosition = i;
                this.mHasEverPickLongTouchPosition = false;
                this.mScrollYMode2PickEnable = true;
            }

            public final int getLongTouchPosition() {
                return this.mLongTouchPosition;
            }

            public boolean isAutoScrolling() {
                ScrollRunnable scrollRunnable = this.mScrollRunnable;
                if (scrollRunnable != null) {
                    return !scrollRunnable.isStopped();
                }
                return false;
            }

            public void stopScrolling() {
                ScrollRunnable scrollRunnable = this.mScrollRunnable;
                if (scrollRunnable != null) {
                    scrollRunnable.stop();
                }
            }

            public void setContinuousPickEnable(boolean z) {
                this.mIsEnableContinuousPick = z;
            }

            public void setMovePickEnable(boolean z) {
                this.mIsMovePickEnable = z;
            }

            public void setAutoScrollPickEnable(boolean z) {
                this.isAutoScrollPickEnable = z;
            }

            public final void clearTouchInfo() {
                AnimationManager.this.mLongPress = false;
                this.fakeEventSent = false;
                this.mFirstTouchPosition = -1;
                this.mLastTouchPosition = -1;
                this.mTouchingPosition = -1;
                this.mUserWantToScroll = false;
                this.mScrollXMode2PickEnable = false;
                this.mScrollYMode2PickEnable = false;
                EditableListViewWrapper.this.mRecyclerView.setFastScrollerInvisible(false);
                resetVisibleRectAndHotSpot();
            }

            public final void resetVisibleRectAndHotSpot() {
                this.mScreenHeight = ScreenUtils.getFullScreenVertical(EditableListViewWrapper.this.mRecyclerView.getContext());
                EditableListViewWrapper.this.mRecyclerView.getLocationInWindow(this.mRecyclerViewLocation);
                EditableListViewWrapper.this.mRecyclerView.getLocalVisibleRect(this.mRecyclerViewVisibleRect);
                int height = (int) (this.mRecyclerViewVisibleRect.height() * 0.2f);
                this.mSpotBoundInListHeight = height;
                this.mTopSpotBoundFrom = 0;
                int[] iArr = this.mRecyclerViewLocation;
                int i = iArr[1];
                Rect rect = this.mRecyclerViewVisibleRect;
                this.mTopSpotBoundTo = i + rect.top + height;
                this.mBottomSpotBoundFrom = (iArr[1] + rect.bottom) - height;
                this.mBottomSpotBoundTo = this.mScreenHeight;
                this.mInTopSpot = false;
                this.mInBottomSpot = false;
            }

            public final boolean interceptEvent() {
                return this.mScrollXMode2PickEnable || this.mScrollYMode2PickEnable;
            }

            @Override // android.view.View.OnTouchListener
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int actionMasked = motionEvent.getActionMasked();
                if (actionMasked != 0) {
                    if (actionMasked == 1) {
                        this.mIsDownConsume = false;
                        this.mIsMovePickEnable = true;
                    } else if (actionMasked == 2) {
                        if (!EditableListViewWrapper.this.mRecyclerView.isFastScrollerPressed()) {
                            if ((!EditableListViewWrapper.this.mIsPreInChoiceMode && !EditableListViewWrapper.this.isInChoiceMode()) || !this.mIsEnableContinuousPick) {
                                return false;
                            }
                            EditableListViewWrapper.this.mRecyclerView.requestDisallowInterceptTouchEvent(true);
                            if (!this.mIsDownConsume) {
                                clearTouchInfo();
                                this.mIsDownConsume = true;
                            }
                            boolean onTouchMove = onTouchMove(motionEvent);
                            EditableListViewWrapper.this.mRecyclerView.setFastScrollerInvisible(onTouchMove);
                            if (!this.fakeEventSent && ((onTouchMove && (this.mLongTouchPosition != -1 || this.mFirstTouchPosition != -1)) || AnimationManager.this.mLongPress)) {
                                MotionEvent obtain = MotionEvent.obtain(motionEvent);
                                obtain.setAction(3);
                                EditableListViewWrapper.this.mRecyclerView.dispatchTouchEvent(obtain);
                                ItemClickSupport.removeFromTemp(EditableListViewWrapper.this.mRecyclerView);
                                this.fakeEventSent = true;
                            }
                            return onTouchMove;
                        }
                        if (!EditableListViewWrapper.this.isInChoiceMode()) {
                            EditableListViewWrapper.this.mRecyclerView.requestDisallowInterceptTouchEvent(true);
                        }
                        return false;
                    } else if (actionMasked != 3) {
                        if (actionMasked == 5 && EditableListViewWrapper.this.mItemAnimHelper != null) {
                            EditableListViewWrapper.this.mItemAnimHelper.reductionTouchView();
                        }
                    }
                    if (EditableListViewWrapper.this.isItemAnimEnable && this.touchView != null && EditableListViewWrapper.this.mItemAnimHelper != null && EditableListViewWrapper.this.mItemAnimHelper.isHandleType(this.touchView.getClass().getSimpleName())) {
                        EditableListViewWrapper.this.mItemAnimHelper.onTouchItemView(this.touchView, actionMasked);
                    }
                    if (this.fakeEventSent) {
                        ItemClickSupport.recoverFromTemp(EditableListViewWrapper.this.mRecyclerView);
                        EditableListViewWrapper.this.mRecyclerView.setTag(R.id.ignore_target_view_action_event_disposable, Boolean.TRUE);
                    }
                    if (!EditableListViewWrapper.this.isInChoiceMode()) {
                        return false;
                    }
                    if (!this.mHasEverPickLongTouchPosition && this.mLongTouchPosition != -1 && !performPickLongTouchPosition()) {
                        EditableListViewWrapper.this.mRecyclerView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: com.miui.gallery.widget.editwrapper.EditableListViewWrapper.AnimationManager.Scroller2PickHelper.1
                            {
                                Scroller2PickHelper.this = this;
                            }

                            @Override // android.view.ViewTreeObserver.OnPreDrawListener
                            public boolean onPreDraw() {
                                if (Scroller2PickHelper.this.mLongTouchPosition != -1) {
                                    Scroller2PickHelper.this.performPickLongTouchPosition();
                                    Scroller2PickHelper.this.mLongTouchPosition = -1;
                                }
                                Scroller2PickHelper.this.mHasEverPickLongTouchPosition = true;
                                EditableListViewWrapper.this.mRecyclerView.getViewTreeObserver().removeOnPreDrawListener(this);
                                return true;
                            }
                        });
                    }
                    this.mScrollRunnable.stop();
                } else {
                    this.mLastX = motionEvent.getX();
                    this.mLastY = motionEvent.getY();
                    clearTouchInfo();
                    View findChildViewUnderForExternal = EditableListViewWrapper.this.mRecyclerView.findChildViewUnderForExternal(motionEvent.getX(), motionEvent.getY());
                    this.touchView = findChildViewUnderForExternal;
                    if (findChildViewUnderForExternal != null) {
                        if (EditableListViewWrapper.this.isItemAnimEnable && EditableListViewWrapper.this.mItemAnimHelper != null && EditableListViewWrapper.this.mItemAnimHelper.isHandleType(this.touchView.getClass().getSimpleName())) {
                            EditableListViewWrapper.this.mItemAnimHelper.onTouchItemView(this.touchView, actionMasked);
                        }
                        int childAdapterPosition = EditableListViewWrapper.this.mRecyclerView.getChildAdapterPosition(this.touchView);
                        if (childAdapterPosition != -1 && EditableListViewWrapper.this.mAdapterHolder.isValidDataPosition(childAdapterPosition)) {
                            this.mFirstTouchPosition = childAdapterPosition;
                            this.mLastTouchPosition = childAdapterPosition;
                            this.mFirstPickChecked = EditableListViewWrapper.this.mCheckState.getCheckState(this.mFirstTouchPosition);
                        }
                    }
                    this.mIsDownConsume = true;
                }
                return false;
            }

            public final boolean performPickLongTouchPosition() {
                RecyclerView.ViewHolder findViewHolderForAdapterPositionForExternal = EditableListViewWrapper.this.mRecyclerView.findViewHolderForAdapterPositionForExternal(this.mLongTouchPosition);
                if (findViewHolderForAdapterPositionForExternal != null) {
                    View view = findViewHolderForAdapterPositionForExternal.itemView;
                    if (view instanceof PickAnimationHelper$BackgroundImageViewable) {
                        AnimationManager.this.startScaleItemImageViewAnimation(((PickAnimationHelper$BackgroundImageViewable) view).getBackgroundImageView(), this.mLongTouchPosition, true, 0, 200);
                    }
                    if (view instanceof Checkable) {
                        boolean z = !EditableListViewWrapper.this.mCheckState.getCheckState(this.mLongTouchPosition);
                        EditableListViewWrapper.this.mCheckState.setCheckState(this.mLongTouchPosition, z);
                        ((Checkable) view).setChecked(z);
                        EditableListViewWrapper.this.updateActionMode();
                        if (EditableListViewWrapper.this.mChoiceActionMode != null) {
                            EditableListViewWrapper.this.mMultiChoiceModeCallback.mWrapped.onItemCheckedStateChanged(EditableListViewWrapper.this.mChoiceActionMode, this.mLongTouchPosition, findViewHolderForAdapterPositionForExternal.getItemId(), true);
                        }
                    }
                    this.mHasEverPickLongTouchPosition = true;
                    this.mLongTouchPosition = -1;
                    return true;
                }
                return false;
            }

            public final boolean onTouchMove(MotionEvent motionEvent) {
                View findChildViewUnderForExternal;
                int childAdapterPositionForExternal;
                View findChildViewUnderForExternal2;
                int childAdapterPositionForExternal2;
                this.mCurX = motionEvent.getX();
                this.mCurY = motionEvent.getY();
                if (this.mIsMovePickEnable && !this.mUserWantToScroll && this.mFirstTouchPosition != -1 && Math.abs(this.mCurX - this.mLastX) >= Math.abs(this.mCurY - this.mLastY) * 1.5f && (this.mScrollXMode2PickEnable || Math.abs(this.mCurX - this.mLastX) > 10.0f)) {
                    if (this.mScrollYMode2PickEnable) {
                        if (this.mHasEverPickLongTouchPosition) {
                            resetVisibleRectAndHotSpot();
                        }
                        if (this.isAutoScrollPickEnable) {
                            processAutoScroll();
                        }
                    }
                    if (!this.mInTopSpot && !this.mInBottomSpot && (findChildViewUnderForExternal2 = EditableListViewWrapper.this.mRecyclerView.findChildViewUnderForExternal(motionEvent.getX(), motionEvent.getY())) != null && (childAdapterPositionForExternal2 = EditableListViewWrapper.this.mRecyclerView.getChildAdapterPositionForExternal(findChildViewUnderForExternal2)) != -1) {
                        this.mScrollXMode2PickEnable = true;
                        this.mScrollYMode2PickEnable = true;
                        this.mTouchingPosition = childAdapterPositionForExternal2;
                        if (childAdapterPositionForExternal2 != this.mLastTouchPosition) {
                            pickOrNotPickItems();
                            this.mLastTouchPosition = this.mTouchingPosition;
                        }
                    }
                } else {
                    if (Math.abs(this.mCurY - this.mLastY) > 10.0f && !this.mScrollYMode2PickEnable) {
                        this.mUserWantToScroll = true;
                    }
                    if (this.mFirstTouchPosition != -1 && this.mScrollYMode2PickEnable) {
                        if (this.isAutoScrollPickEnable) {
                            processAutoScroll();
                        }
                        if (!this.mInTopSpot && !this.mInBottomSpot && (findChildViewUnderForExternal = EditableListViewWrapper.this.mRecyclerView.findChildViewUnderForExternal(this.mCurX, this.mCurY)) != null && (childAdapterPositionForExternal = EditableListViewWrapper.this.mRecyclerView.getChildAdapterPositionForExternal(findChildViewUnderForExternal)) != -1) {
                            this.mTouchingPosition = childAdapterPositionForExternal;
                            if (childAdapterPositionForExternal != this.mLastTouchPosition) {
                                pickOrNotPickItems();
                                this.mLastTouchPosition = this.mTouchingPosition;
                            }
                        }
                    }
                }
                this.mLastX = this.mCurX;
                this.mLastY = this.mCurY;
                return interceptEvent();
            }

            /* loaded from: classes2.dex */
            public class ScrollRunnable implements Runnable {
                public boolean mStopped = true;

                public ScrollRunnable() {
                    Scroller2PickHelper.this = r1;
                }

                public void start() {
                    if (EditableListViewWrapper.this.mRecyclerView == null) {
                        return;
                    }
                    if (this.mStopped) {
                        EditableListViewWrapper.this.mRecyclerView.removeCallbacks(this);
                        EditableListViewWrapper.this.mRecyclerView.postOnAnimation(this);
                    }
                    this.mStopped = false;
                }

                public void stop() {
                    if (EditableListViewWrapper.this.mRecyclerView != null && !this.mStopped) {
                        EditableListViewWrapper.this.mRecyclerView.removeCallbacks(this);
                        this.mStopped = true;
                    }
                }

                public boolean isStopped() {
                    return this.mStopped;
                }

                @Override // java.lang.Runnable
                public void run() {
                    if (EditableListViewWrapper.this.mRecyclerView == null) {
                        return;
                    }
                    Scroller2PickHelper scroller2PickHelper = Scroller2PickHelper.this;
                    scroller2PickHelper.viewByOffset(scroller2PickHelper.mScrollDy);
                    if (this.mStopped) {
                        return;
                    }
                    EditableListViewWrapper.this.mRecyclerView.postOnAnimation(this);
                }
            }

            public final void processAutoScroll() {
                float f = this.mCurY;
                int[] iArr = this.mRecyclerViewLocation;
                int i = (int) (f + iArr[1]);
                if (i >= this.mTopSpotBoundFrom && i <= this.mTopSpotBoundTo) {
                    int i2 = iArr[1] + this.mRecyclerViewVisibleRect.top;
                    if (i <= i2) {
                        this.mScrollSpeedFactor = 1.0f;
                    } else {
                        this.mScrollSpeedFactor = 1.0f - ((i - i2) / this.mSpotBoundInListHeight);
                    }
                    this.mScrollDy = (int) (this.mMaxScrollDy * this.mScrollSpeedFactor * (-1.0f));
                    this.mInTopSpot = true;
                    this.mInBottomSpot = false;
                    this.mScrollRunnable.start();
                } else if (i >= this.mBottomSpotBoundFrom && i <= this.mBottomSpotBoundTo) {
                    int i3 = this.mRecyclerViewVisibleRect.bottom + iArr[1];
                    if (i >= i3) {
                        this.mScrollSpeedFactor = 1.0f;
                    } else {
                        this.mScrollSpeedFactor = 1.0f - ((i3 - i) / this.mSpotBoundInListHeight);
                    }
                    this.mScrollDy = (int) (this.mMaxScrollDy * this.mScrollSpeedFactor);
                    this.mInBottomSpot = true;
                    this.mInTopSpot = false;
                    this.mScrollRunnable.start();
                } else {
                    this.mInTopSpot = false;
                    this.mInBottomSpot = false;
                    this.mScrollRunnable.stop();
                }
            }

            public final void viewByOffset(int i) {
                int childAdapterPositionForExternal;
                int findFirstCompletelyVisibleItemPosition = RecyclerViewUtils.findFirstCompletelyVisibleItemPosition(EditableListViewWrapper.this.mRecyclerView);
                int findLastCompletelyVisibleItemPosition = RecyclerViewUtils.findLastCompletelyVisibleItemPosition(EditableListViewWrapper.this.mRecyclerView);
                int itemCount = EditableListViewWrapper.this.mAdapterHolder.getAdapter().getItemCount() - 1;
                boolean z = findFirstCompletelyVisibleItemPosition != -1 && findFirstCompletelyVisibleItemPosition <= 0;
                boolean z2 = findLastCompletelyVisibleItemPosition != -1 && findLastCompletelyVisibleItemPosition >= itemCount;
                int i2 = (int) (this.mCurY + this.mRecyclerViewLocation[1]);
                if (i > 0) {
                    if (!z2) {
                        EditableListViewWrapper.this.mRecyclerView.viewByOffset(0, Math.min(i, 15));
                    }
                } else if (!z) {
                    EditableListViewWrapper.this.mRecyclerView.viewByOffset(0, Math.max(i, -15));
                }
                if (z && i2 < this.mRecyclerViewLocation[1]) {
                    this.mTouchingPosition = 0;
                    pickOrNotPickItems();
                } else if (!z2 || i2 <= this.mRecyclerViewLocation[1] + this.mRecyclerViewVisibleRect.bottom) {
                    View findChildViewUnderForExternal = EditableListViewWrapper.this.mRecyclerView.findChildViewUnderForExternal(this.mCurX, this.mCurY);
                    if (findChildViewUnderForExternal == null || (childAdapterPositionForExternal = EditableListViewWrapper.this.mRecyclerView.getChildAdapterPositionForExternal(findChildViewUnderForExternal)) == -1) {
                        return;
                    }
                    this.mTouchingPosition = childAdapterPositionForExternal;
                    if (childAdapterPositionForExternal == this.mLastTouchPosition) {
                        return;
                    }
                    pickOrNotPickItems();
                    this.mLastTouchPosition = this.mTouchingPosition;
                } else {
                    this.mTouchingPosition = itemCount;
                    pickOrNotPickItems();
                }
            }

            public final void pickOrNotPickItems() {
                int checkedItemCount = EditableListViewWrapper.this.getCheckedItemCount();
                int i = this.mFirstTouchPosition;
                if (i == -1) {
                    DefaultLogger.e("EditableListViewWrapper", "first pick position shouldn't be -1");
                    return;
                }
                int max = Math.max(0, Math.min(i, this.mTouchingPosition));
                int max2 = Math.max(this.mFirstTouchPosition, this.mTouchingPosition);
                for (int i2 = max; i2 <= max2; i2++) {
                    if (EditableListViewWrapper.this.mAdapterHolder.isValidDataPosition(i2) && EditableListViewWrapper.this.mCheckState.getCheckState(i2) == this.mFirstPickChecked) {
                        EditableListViewWrapper.this.mCheckState.setCheckState(i2, !this.mFirstPickChecked);
                    }
                }
                int i3 = this.mLastTouchPosition;
                if (i3 < max) {
                    if (i3 == -1) {
                        DefaultLogger.e("EditableListViewWrapper", "last pick position shouldn't be -1");
                    }
                    for (int max3 = Math.max(0, this.mLastTouchPosition); max3 < max; max3++) {
                        if (EditableListViewWrapper.this.mAdapterHolder.isValidDataPosition(max3) && EditableListViewWrapper.this.mCheckState.getCheckState(max3)) {
                            EditableListViewWrapper.this.mCheckState.setCheckState(max3, false);
                        }
                    }
                }
                if (this.mLastTouchPosition > max2) {
                    while (true) {
                        max2++;
                        if (max2 > this.mLastTouchPosition) {
                            break;
                        } else if (EditableListViewWrapper.this.mAdapterHolder.isValidDataPosition(max2) && EditableListViewWrapper.this.mCheckState.getCheckState(max2)) {
                            EditableListViewWrapper.this.mCheckState.setCheckState(max2, false);
                        }
                    }
                }
                EditableListViewWrapper.this.updateActionMode();
                if (checkedItemCount != EditableListViewWrapper.this.getCheckedItemCount()) {
                    LinearMotorHelper.performHapticFeedback(EditableListViewWrapper.this.mRecyclerView, LinearMotorHelper.HAPTIC_MESH_LIGHT);
                }
                EditableListViewWrapper.this.updateOnScreenViewsState();
                if (EditableListViewWrapper.this.mChoiceActionMode == null || !EditableListViewWrapper.this.isInChoiceMode()) {
                    return;
                }
                EditableListViewWrapper.this.mMultiChoiceModeCallback.mWrapped.onAllItemsCheckedStateChanged(EditableListViewWrapper.this.mChoiceActionMode, !this.mFirstPickChecked);
            }
        }
    }

    public boolean isAllItemsChecked() {
        return this.mCheckState.isAllItemsChecked();
    }

    public void setAllItemsCheckState(boolean z) {
        this.mCheckState.setAllChecked(z);
        updateOnScreenViewsState();
        updateActionMode();
        this.mMultiChoiceModeCallback.mWrapped.onAllItemsCheckedStateChanged(this.mChoiceActionMode, z);
    }

    public final void updateOnScreenViewsState() {
        RecyclerView.ViewHolder childViewHolder;
        int adapterPosition;
        for (int i = 0; i < this.mRecyclerView.getChildCount(); i++) {
            View childAt = this.mRecyclerView.getChildAt(i);
            if ((childAt instanceof Checkable) && (childViewHolder = this.mRecyclerView.getChildViewHolder(childAt)) != null && (adapterPosition = childViewHolder.getAdapterPosition()) != -1 && this.mAdapterHolder.isValidDataPosition(adapterPosition)) {
                ((Checkable) childAt).setChecked(this.mCheckState.getCheckState(adapterPosition));
                if (childAt instanceof PickAnimationHelper$BackgroundImageViewable) {
                    this.mAnimationManager.startScaleItemImageViewAnimation(((PickAnimationHelper$BackgroundImageViewable) childAt).getBackgroundImageView(), adapterPosition);
                }
            }
        }
    }

    public final void updateActionMode() {
        if (this.mChoiceActionMode == null) {
            return;
        }
        if (getCheckedItemCount() == 0) {
            this.mChoiceActionMode.setTitle(this.mRecyclerView.getResources().getString(R.string.miuix_appcompat_select_item));
        } else {
            this.mChoiceActionMode.setTitle(this.mRecyclerView.getResources().getQuantityString(R.plurals.miuix_appcompat_items_selected, getCheckedItemCount(), Integer.valueOf(getCheckedItemCount())));
        }
        if (this.mEditActionMode == null || this.mEditActionModeButton2Func != 0) {
            return;
        }
        MiuiSdkCompat.setEditActionModeButton(this.mRecyclerView.getContext(), this.mEditActionMode, 16908314, isAllItemsChecked() ? 1 : 0);
    }

    public final void handleDataChanged() {
        this.mCheckState.handleDataChanged();
        updateActionMode();
        this.mAnimationManager.setLongTouchPosition(-1);
        this.mAnimationManager.setMovePickEnable(false);
    }

    public void handleDataInvalid() {
        this.mCheckState.clear();
        updateActionMode();
    }

    public void setOnCallNotifyDataChangeEventListener(OnCallNotifyDataSetChangeListener onCallNotifyDataSetChangeListener) {
        this.mOnCallNotifyDataSetChangeListener = onCallNotifyDataSetChangeListener;
    }

    /* loaded from: classes2.dex */
    public class MultiChoiceModeCallback implements ActionMode.Callback {
        public MultiChoiceModeListener mWrapped;

        public MultiChoiceModeCallback() {
            EditableListViewWrapper.this = r1;
        }

        public void setWrapped(MultiChoiceModeListener multiChoiceModeListener) {
            this.mWrapped = multiChoiceModeListener;
        }

        public boolean hasWrappedCallback() {
            return this.mWrapped != null;
        }

        @Override // android.view.ActionMode.Callback
        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            EditableListViewWrapper.this.isActionModeEnterAnimStarting = true;
            if (!this.mWrapped.onCreateActionMode(actionMode, menu)) {
                return false;
            }
            if (EditableListViewWrapper.this.getCheckedItemCount() == 0) {
                actionMode.setTitle(R.string.miuix_appcompat_select_item);
            } else {
                actionMode.setTitle(EditableListViewWrapper.this.mRecyclerView.getResources().getQuantityString(R.plurals.miuix_appcompat_items_selected, EditableListViewWrapper.this.getCheckedItemCount(), Integer.valueOf(EditableListViewWrapper.this.getCheckedItemCount())));
            }
            if (actionMode instanceof EditActionMode) {
                EditActionMode editActionMode = (EditActionMode) actionMode;
                MiuiSdkCompat.setEditActionModeButton(EditableListViewWrapper.this.mRecyclerView.getContext(), editActionMode, 16908313, 3);
                int i = EditableListViewWrapper.this.mEditActionModeButton2Func;
                if (i == 0) {
                    MiuiSdkCompat.setEditActionModeButton(EditableListViewWrapper.this.mRecyclerView.getContext(), editActionMode, 16908314, 0);
                } else if (i == 1) {
                    MiuiSdkCompat.setEditActionModeButton(EditableListViewWrapper.this.mRecyclerView.getContext(), editActionMode, 16908314, 2);
                }
            }
            EditableListViewWrapper.this.enterChoiceMode();
            EditableListViewWrapper.this.mIsInActionMode = true;
            return true;
        }

        @Override // android.view.ActionMode.Callback
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return this.mWrapped.onPrepareActionMode(actionMode, menu);
        }

        @Override // android.view.ActionMode.Callback
        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            if (!EditableListViewWrapper.this.mAnimationManager.isAutoPickScrolling() && !this.mWrapped.onActionItemClicked(actionMode, menuItem)) {
                switch (menuItem.getItemId()) {
                    case 16908313:
                        EditableListViewWrapper.this.isHideActionMode = false;
                        actionMode.finish();
                        return true;
                    case 16908314:
                        EditableListViewWrapper.this.onButton2Click();
                        return true;
                    default:
                        return false;
                }
            }
            return true;
        }

        @Override // android.view.ActionMode.Callback
        public void onDestroyActionMode(ActionMode actionMode) {
            if (EditableListViewWrapper.this.isHideActionMode) {
                return;
            }
            EditableListViewWrapper.this.mEditActionMode = null;
            EditableListViewWrapper.this.mChoiceActionMode = null;
            EditableListViewWrapper.this.mIsInChoiceMode = false;
            EditableListViewWrapper.this.exitChoiceMode();
            this.mWrapped.onDestroyActionMode(actionMode);
            EditableListViewWrapper.this.mIsInActionMode = false;
        }
    }

    public final void onButton2Click() {
        int i = this.mEditActionModeButton2Func;
        if (i == 0) {
            setAllItemsCheckState(!isAllItemsChecked());
        } else if (i != 1) {
        } else {
            this.mMultiChoiceModeCallback.mWrapped.onConfirmMultiChoiceResult(getCheckedPositions());
        }
    }

    public final void enterChoiceMode() {
        this.mRecyclerView.hideScrollerBar();
        this.mRecyclerView.stopScroll();
        this.mRecyclerView.setLongClickable(false);
        this.mRecyclerView.setOnItemClickListener(this.mCheckItemListener);
        this.mAnimationManager.startEnterActionModeAni();
        notifyDataChanged();
    }

    public final void exitChoiceMode() {
        if (this.isHideActionMode) {
            return;
        }
        this.mRecyclerView.hideScrollerBar();
        this.mRecyclerView.stopScroll();
        this.mRecyclerView.setLongClickable(true);
        this.mRecyclerView.setOnItemClickListener(this.mItemClickDelegate);
        this.mAnimationManager.stopPickScrolling();
        this.mAnimationManager.startExistActionModeAni();
        this.mCheckState.clear();
        notifyDataChanged();
    }

    public final void notifyDataChanged() {
        AdapterHolder adapterHolder;
        OnCallNotifyDataSetChangeListener onCallNotifyDataSetChangeListener = this.mOnCallNotifyDataSetChangeListener;
        if (onCallNotifyDataSetChangeListener != null && (adapterHolder = this.mAdapterHolder) != null && onCallNotifyDataSetChangeListener.onNotifyEvent(adapterHolder.mo1836getSource())) {
            DefaultLogger.d("EditableListViewWrapper", "onIntercept notifyDataSetChange by" + this.mOnCallNotifyDataSetChangeListener);
            return;
        }
        RecyclerView.Adapter adapter = this.mRecyclerView.getAdapter();
        if (adapter instanceof WrapperAdapter) {
            LinkedList<RecyclerView.Adapter> newLinkedList = Lists.newLinkedList();
            ((WrapperAdapter) adapter).getWrappedAdapters(newLinkedList);
            if (newLinkedList.size() <= 0) {
                return;
            }
            for (RecyclerView.Adapter adapter2 : newLinkedList) {
                if (adapter2 instanceof WrapperSource) {
                    ((RecyclerView.Adapter) ((WrapperSource) adapter2).mo1836getSource()).notifyDataSetChanged();
                } else if (adapter2 instanceof AbstractHeaderFooterWrapperAdapter) {
                    ((AbstractHeaderFooterWrapperAdapter) adapter2).getWrappedAdapter().notifyDataSetChanged();
                } else {
                    adapter2.notifyDataSetChanged();
                }
            }
        } else if (adapter instanceof WrapperSource) {
            ((RecyclerView.Adapter) ((WrapperSource) adapter).mo1836getSource()).notifyDataSetChanged();
        } else if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    /* loaded from: classes2.dex */
    public class SimpleHolder<VH extends RecyclerView.ViewHolder> extends SimpleWrapperAdapter<VH> implements AdapterHolder {
        public RecyclerView.Adapter mAdapterToSet;

        public int getSourceGroupCount() {
            return 0;
        }

        public boolean isGroupCheckable() {
            return false;
        }

        public int packGroupedPosition(int i, int i2) {
            return i2;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SimpleHolder(RecyclerView.Adapter<VH> adapter) {
            super(adapter);
            EditableListViewWrapper.this = r1;
        }

        public int decryptPosition(int i) {
            if (isHeaderOrFooterPosition(i)) {
                return -1;
            }
            return i - getHeaderCount();
        }

        public int encryptPosition(int i) {
            return isHeaderOrFooterPosition(i) ? i : getHeaderCount() + i;
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.AdapterHolder
        public void setAdapter(RecyclerView.Adapter adapter) {
            this.mAdapterToSet = adapter;
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.AdapterHolder
        public RecyclerView.Adapter getAdapter() {
            return this.mAdapterToSet;
        }

        @Override // com.miui.itemdrag.WrapperSource
        /* renamed from: getSource */
        public RecyclerView.Adapter mo1836getSource() {
            RecyclerView.Adapter<VH> wrappedAdapter = getWrappedAdapter();
            return wrappedAdapter instanceof WrapperSource ? (RecyclerView.Adapter) ((WrapperSource) wrappedAdapter).mo1836getSource() : wrappedAdapter;
        }

        public void bindCheckState(RecyclerView.ViewHolder viewHolder, int i) {
            View view = viewHolder.itemView;
            if (view instanceof Checkable) {
                Checkable checkable = (Checkable) view;
                checkable.setCheckable(EditableListViewWrapper.this.isInChoiceMode());
                checkable.setChecked(EditableListViewWrapper.this.mCheckState.getCheckState(i));
                EditableListViewWrapper.this.mAnimationManager.resetViewPropertyIfNeed(view, view, i);
            }
        }

        public final void bindHook(RecyclerView.ViewHolder viewHolder, int i) {
            if (EditableListViewWrapper.this.mOnBindViewHolderHook != null) {
                EditableListViewWrapper.this.mOnBindViewHolderHook.onBind(viewHolder, i);
            }
        }

        @Override // com.h6ah4i.android.widget.advrecyclerview.adapter.SimpleWrapperAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(VH vh, int i) {
            if (isWrappedAdapterAlive() && !EditableListViewWrapper.this.mIsAfterBindViewHolderHook) {
                bindHook(vh, i);
            }
            super.onBindViewHolder(vh, i);
            if (isWrappedAdapterAlive()) {
                if (EditableListViewWrapper.this.mIsAfterBindViewHolderHook) {
                    bindHook(vh, i);
                }
                if (!isValidDataPosition(i)) {
                    return;
                }
                bindCheckState(vh, i);
            }
        }

        @Override // com.h6ah4i.android.widget.advrecyclerview.adapter.SimpleWrapperAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
        public void onBindViewHolder(VH vh, int i, List<Object> list) {
            if (isWrappedAdapterAlive() && !EditableListViewWrapper.this.mIsAfterBindViewHolderHook) {
                bindHook(vh, i);
            }
            super.onBindViewHolder(vh, i, list);
            if (isWrappedAdapterAlive()) {
                if (EditableListViewWrapper.this.mIsAfterBindViewHolderHook) {
                    bindHook(vh, i);
                }
                if (!isValidDataPosition(i)) {
                    return;
                }
                bindCheckState(vh, i);
            }
        }

        public int getSourceItemCount() {
            RecyclerView.Adapter<VH> wrappedAdapter = getWrappedAdapter();
            if (wrappedAdapter instanceof AbstractHeaderFooterWrapperAdapter) {
                return ((AbstractHeaderFooterWrapperAdapter) wrappedAdapter).getWrappedAdapter().getItemCount();
            }
            return super.getItemCount();
        }

        public long getSourceItemId(int i) {
            RecyclerView.Adapter<VH> wrappedAdapter = getWrappedAdapter();
            if (wrappedAdapter != null) {
                return wrappedAdapter.getItemId(i);
            }
            return super.getItemId(i);
        }

        public boolean isHeaderOrFooterPosition(int i) {
            RecyclerView.Adapter<VH> wrappedAdapter = getWrappedAdapter();
            return (wrappedAdapter instanceof AbstractHeaderFooterWrapperAdapter) && ComposedAdapter.extractSegmentPart(((AbstractHeaderFooterWrapperAdapter) wrappedAdapter).getSegmentedPosition(i)) != 1;
        }

        public int getHeaderCount() {
            RecyclerView.Adapter<VH> wrappedAdapter = getWrappedAdapter();
            if (wrappedAdapter instanceof AbstractHeaderFooterWrapperAdapter) {
                return ((AbstractHeaderFooterWrapperAdapter) wrappedAdapter).getHeaderItemCount();
            }
            return 0;
        }

        public boolean isValidDataPosition(int i) {
            return !isHeaderOrFooterPosition(i);
        }

        public void registerAdapterObserver(RecyclerView.AdapterDataObserver adapterDataObserver) {
            registerAdapterDataObserver(adapterDataObserver);
        }

        public void unregisterAdapterObserver(RecyclerView.AdapterDataObserver adapterDataObserver) {
            unregisterAdapterDataObserver(adapterDataObserver);
        }

        public int[] unpackGroupedPosition(int i) {
            return isHeaderOrFooterPosition(i) ? new int[]{-1, -1} : new int[]{0, i - getHeaderCount()};
        }

        public Iterator<SourceEntry> iterator() {
            return new Iter();
        }

        public boolean isTransformId() {
            return this.mAdapterToSet instanceof AbstractHeaderFooterWrapperAdapter;
        }

        public int getSpanSize(int i, int i2) {
            if (isHeaderOrFooterPosition(i)) {
                return i2;
            }
            return 1;
        }

        public int getSpanIndex(int i, int i2) {
            if (isHeaderOrFooterPosition(i)) {
                return 0;
            }
            return (i - getHeaderCount()) % i2;
        }

        /* loaded from: classes2.dex */
        public class Iter implements Iterator<SourceEntry> {
            public int idx;
            public SourceEntry next;

            public Iter() {
                int itemCount;
                SimpleHolder.this = r5;
                this.idx = 0;
                this.next = null;
                RecyclerView.Adapter<VH> wrappedAdapter = r5.getWrappedAdapter();
                if (wrappedAdapter == 0 || (itemCount = wrappedAdapter.getItemCount()) <= 0) {
                    return;
                }
                if (wrappedAdapter instanceof AbstractHeaderFooterWrapperAdapter) {
                    int headerItemCount = ((AbstractHeaderFooterWrapperAdapter) wrappedAdapter).getHeaderItemCount();
                    if (headerItemCount >= itemCount) {
                        return;
                    }
                    this.idx = headerItemCount;
                    this.idx = headerItemCount + 1;
                    this.next = new SourceEntry(headerItemCount, wrappedAdapter.getItemId(headerItemCount));
                    return;
                }
                int i = this.idx;
                this.idx = i + 1;
                this.next = new SourceEntry(i, wrappedAdapter.getItemId(i));
            }

            public final void advance() {
                if (this.next == null) {
                    throw new NoSuchElementException();
                }
                RecyclerView.Adapter<VH> wrappedAdapter = SimpleHolder.this.getWrappedAdapter();
                if (wrappedAdapter == 0 || this.idx >= wrappedAdapter.getItemCount()) {
                    this.next = null;
                } else if (wrappedAdapter instanceof AbstractHeaderFooterWrapperAdapter) {
                    if (ComposedAdapter.extractSegmentPart(((AbstractHeaderFooterWrapperAdapter) wrappedAdapter).getSegmentedPosition(this.idx)) == 1) {
                        int i = this.idx;
                        this.idx = i + 1;
                        this.next = new SourceEntry(i, wrappedAdapter.getItemId(i));
                        return;
                    }
                    this.next = null;
                } else {
                    int i2 = this.idx;
                    this.idx = i2 + 1;
                    this.next = new SourceEntry(i2, wrappedAdapter.getItemId(i2));
                }
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.next != null;
            }

            @Override // java.util.Iterator
            /* renamed from: next */
            public SourceEntry mo1821next() {
                SourceEntry sourceEntry = this.next;
                advance();
                return sourceEntry;
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class SourceEntry {
        public long id;
        public int position;

        public SourceEntry(int i, long j) {
            this.position = i;
            this.id = j;
        }
    }

    /* loaded from: classes2.dex */
    public static class HeaderFooterGroupedAdapterHolder implements AdapterHolder {
        public RecyclerView.Adapter mAdapterToSet;
        public GroupedItemAdapter mGroupedItemAdapter;
        public GroupedItemManager mGroupedItemManager;
        public AbstractHeaderFooterWrapperAdapter mHeaderFooterAdapter;

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.AdapterHolder
        public boolean isGroupCheckable() {
            return true;
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.AdapterHolder
        public boolean isTransformId() {
            return true;
        }

        public HeaderFooterGroupedAdapterHolder(AbstractHeaderFooterWrapperAdapter abstractHeaderFooterWrapperAdapter, GroupedItemManager groupedItemManager) {
            this.mHeaderFooterAdapter = abstractHeaderFooterWrapperAdapter;
            this.mGroupedItemAdapter = (GroupedItemAdapter) WrapperAdapterUtils.findWrappedAdapter(abstractHeaderFooterWrapperAdapter.getWrappedAdapter(), GroupedItemAdapter.class);
            this.mGroupedItemManager = groupedItemManager;
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.AdapterHolder
        public void setAdapter(RecyclerView.Adapter adapter) {
            this.mAdapterToSet = adapter;
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.AdapterHolder
        public RecyclerView.Adapter getAdapter() {
            return this.mAdapterToSet;
        }

        @Override // com.miui.itemdrag.WrapperSource
        /* renamed from: getSource */
        public RecyclerView.Adapter mo1836getSource() {
            AbstractHeaderFooterWrapperAdapter abstractHeaderFooterWrapperAdapter = this.mHeaderFooterAdapter;
            return abstractHeaderFooterWrapperAdapter instanceof WrapperSource ? (RecyclerView.Adapter) ((WrapperSource) abstractHeaderFooterWrapperAdapter).mo1836getSource() : abstractHeaderFooterWrapperAdapter;
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.CheckState.Source
        public int getSourceGroupCount() {
            return this.mGroupedItemManager.getGroupCount();
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.CheckState.Source
        public int getSourceItemCount() {
            int groupCount = this.mGroupedItemManager.getGroupCount();
            int i = 0;
            for (int i2 = 0; i2 < groupCount; i2++) {
                i += this.mGroupedItemManager.getChildCount(i2);
            }
            return i;
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.CheckState.Source
        public long getSourceItemId(int i) {
            return this.mHeaderFooterAdapter.getItemId(i);
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.CheckState.Source
        public int getSourceItemGroupIndex(int i) {
            long segmentedPosition = this.mHeaderFooterAdapter.getSegmentedPosition(i);
            if (ComposedAdapter.extractSegmentPart(segmentedPosition) != 1) {
                return -1;
            }
            return GroupedItemManager.getPackedPositionGroup(this.mGroupedItemManager.getExpandablePosition(ComposedAdapter.extractSegmentOffsetPart(segmentedPosition)));
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.CheckState.Source
        public Iterator<SourceEntry> iterator() {
            return new Iter();
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.CheckState.Source
        public Iterator<SourceEntry> iterator(int i) {
            if (i < 0 || i >= this.mGroupedItemManager.getGroupCount()) {
                return CheckState.Source.EMPTY_ITERATOR;
            }
            return new Iter(i);
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.AdapterHolder
        public int decryptPosition(int i) {
            int[] unpackGroupedPosition = unpackGroupedPosition(i);
            if (unpackGroupedPosition[0] == -1 || unpackGroupedPosition[1] == -1) {
                return -1;
            }
            return this.mGroupedItemAdapter.packDataPosition(unpackGroupedPosition[0], unpackGroupedPosition[1]);
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.AdapterHolder
        public int encryptPosition(int i) {
            int groupCount = this.mGroupedItemManager.getGroupCount();
            if (groupCount <= 0) {
                AbstractHeaderFooterWrapperAdapter abstractHeaderFooterWrapperAdapter = this.mHeaderFooterAdapter;
                return abstractHeaderFooterWrapperAdapter.wrapPosition(abstractHeaderFooterWrapperAdapter.getWrappedAdapterSegment(), i);
            }
            int i2 = 0;
            int i3 = 0;
            while (i2 < groupCount) {
                int i4 = i + 1;
                i3 += this.mGroupedItemManager.getChildCount(i2);
                if (i4 <= i3) {
                    break;
                }
                i2++;
            }
            AbstractHeaderFooterWrapperAdapter abstractHeaderFooterWrapperAdapter2 = this.mHeaderFooterAdapter;
            return abstractHeaderFooterWrapperAdapter2.wrapPosition(abstractHeaderFooterWrapperAdapter2.getWrappedAdapterSegment(), i2 + 1 + i);
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.AdapterHolder
        public boolean isValidDataPosition(int i) {
            long segmentedPosition = this.mHeaderFooterAdapter.getSegmentedPosition(i);
            if (ComposedAdapter.extractSegmentPart(segmentedPosition) != 1) {
                return false;
            }
            return GroupedItemManager.getPackedPositionChild(this.mGroupedItemManager.getExpandablePosition(ComposedAdapter.extractSegmentOffsetPart(segmentedPosition))) != -1;
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.AdapterHolder
        public void registerAdapterObserver(RecyclerView.AdapterDataObserver adapterDataObserver) {
            this.mHeaderFooterAdapter.registerAdapterDataObserver(adapterDataObserver);
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.AdapterHolder
        public void unregisterAdapterObserver(RecyclerView.AdapterDataObserver adapterDataObserver) {
            this.mHeaderFooterAdapter.unregisterAdapterDataObserver(adapterDataObserver);
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.AdapterHolder
        public int packGroupedPosition(int i, int i2) {
            int flatPosition;
            if (i2 != -1) {
                flatPosition = this.mGroupedItemManager.getFlatPosition(GroupedItemManager.getPackedPositionForChild(i, i2));
            } else {
                flatPosition = this.mGroupedItemManager.getFlatPosition(GroupedItemManager.getPackedPositionForGroup(i));
            }
            AbstractHeaderFooterWrapperAdapter abstractHeaderFooterWrapperAdapter = this.mHeaderFooterAdapter;
            return abstractHeaderFooterWrapperAdapter.wrapPosition(abstractHeaderFooterWrapperAdapter.getWrappedAdapterSegment(), flatPosition);
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.AdapterHolder
        public int[] unpackGroupedPosition(int i) {
            long segmentedPosition = this.mHeaderFooterAdapter.getSegmentedPosition(i);
            if (ComposedAdapter.extractSegmentPart(segmentedPosition) == 1) {
                long expandablePosition = this.mGroupedItemManager.getExpandablePosition(ComposedAdapter.extractSegmentOffsetPart(segmentedPosition));
                return new int[]{GroupedItemManager.getPackedPositionGroup(expandablePosition), GroupedItemManager.getPackedPositionChild(expandablePosition)};
            }
            return new int[]{-1, -1};
        }

        public boolean isGroupPosition(int i) {
            long segmentedPosition = this.mHeaderFooterAdapter.getSegmentedPosition(i);
            if (ComposedAdapter.extractSegmentPart(segmentedPosition) == 1) {
                return this.mGroupedItemManager.isGroupPosition(ComposedAdapter.extractSegmentOffsetPart(segmentedPosition));
            }
            return true;
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.AdapterHolder
        public int getSpanSize(int i, int i2) {
            if (isGroupPosition(i)) {
                return i2;
            }
            return 1;
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.AdapterHolder
        public int getSpanIndex(int i, int i2) {
            int[] unpackGroupedPosition = unpackGroupedPosition(i);
            if (unpackGroupedPosition[0] == -1 || unpackGroupedPosition[1] == -1) {
                return 0;
            }
            return unpackGroupedPosition[1] % i2;
        }

        /* loaded from: classes2.dex */
        public class Iter implements Iterator<SourceEntry> {
            public final int end;
            public int idx;
            public SourceEntry next = null;

            public Iter() {
                HeaderFooterGroupedAdapterHolder.this = r3;
                int headerItemCount = r3.mHeaderFooterAdapter.getHeaderItemCount();
                int itemCount = r3.mHeaderFooterAdapter.getItemCount() - r3.mHeaderFooterAdapter.getFooterItemCount();
                this.end = itemCount;
                this.idx = headerItemCount;
                if (headerItemCount < itemCount) {
                    createNext();
                }
            }

            public Iter(int i) {
                HeaderFooterGroupedAdapterHolder.this = r5;
                int flatPosition = r5.mGroupedItemManager.getFlatPosition(GroupedItemManager.getPackedPositionForChild(i, 0)) + r5.mHeaderFooterAdapter.getHeaderItemCount();
                int childCount = r5.mGroupedItemManager.getChildCount(i) + flatPosition;
                this.end = childCount;
                this.idx = flatPosition;
                if (flatPosition < childCount) {
                    createNext();
                }
            }

            public final void advance() {
                if (this.idx < this.end) {
                    createNext();
                } else {
                    this.next = null;
                }
            }

            public final void createNext() {
                while (GroupedItemManager.getPackedPositionChild(HeaderFooterGroupedAdapterHolder.this.mGroupedItemManager.getExpandablePosition(ComposedAdapter.extractSegmentOffsetPart(HeaderFooterGroupedAdapterHolder.this.mHeaderFooterAdapter.getSegmentedPosition(this.idx)))) == -1) {
                    int i = this.idx + 1;
                    this.idx = i;
                    if (i >= this.end) {
                        this.next = null;
                        return;
                    }
                }
                this.next = new SourceEntry(this.idx, HeaderFooterGroupedAdapterHolder.this.mHeaderFooterAdapter.getItemId(this.idx));
                this.idx++;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.next != null;
            }

            @Override // java.util.Iterator
            /* renamed from: next */
            public SourceEntry mo1819next() {
                SourceEntry sourceEntry = this.next;
                advance();
                return sourceEntry;
            }
        }
    }

    /* loaded from: classes2.dex */
    public class GroupedAdapterHolder extends SimpleHolder implements GroupedItemAdapter {
        public GroupedItemAdapter mGroupedItemAdapter;
        public GroupedItemManager mGroupedItemManager;

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.SimpleHolder, com.miui.gallery.widget.editwrapper.EditableListViewWrapper.AdapterHolder
        public boolean isGroupCheckable() {
            return true;
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.SimpleHolder, com.miui.gallery.widget.editwrapper.EditableListViewWrapper.AdapterHolder
        public boolean isTransformId() {
            return true;
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public GroupedAdapterHolder(RecyclerView.Adapter adapter, GroupedItemManager groupedItemManager) {
            super(adapter);
            EditableListViewWrapper.this = r1;
            this.mGroupedItemAdapter = (GroupedItemAdapter) WrapperAdapterUtils.findWrappedAdapter(adapter, GroupedItemAdapter.class);
            this.mGroupedItemManager = groupedItemManager;
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.SimpleHolder, com.miui.gallery.widget.editwrapper.EditableListViewWrapper.AdapterHolder
        public void registerAdapterObserver(RecyclerView.AdapterDataObserver adapterDataObserver) {
            getAdapter().registerAdapterDataObserver(adapterDataObserver);
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.SimpleHolder, com.miui.gallery.widget.editwrapper.EditableListViewWrapper.AdapterHolder
        public void unregisterAdapterObserver(RecyclerView.AdapterDataObserver adapterDataObserver) {
            getAdapter().unregisterAdapterDataObserver(adapterDataObserver);
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.SimpleHolder, com.miui.gallery.widget.editwrapper.EditableListViewWrapper.AdapterHolder
        public int decryptPosition(int i) {
            int[] unpackGroupedPosition = unpackGroupedPosition(i);
            if (unpackGroupedPosition[0] == -1 || unpackGroupedPosition[1] == -1) {
                return -1;
            }
            return this.mGroupedItemAdapter.packDataPosition(unpackGroupedPosition[0], unpackGroupedPosition[1]);
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.SimpleHolder, com.miui.gallery.widget.editwrapper.EditableListViewWrapper.AdapterHolder
        public int encryptPosition(int i) {
            if (this.mGroupedItemAdapter.isFlatten()) {
                return i;
            }
            int groupCount = this.mGroupedItemAdapter.getGroupCount();
            int i2 = 0;
            int i3 = 0;
            int i4 = 0;
            while (true) {
                if (i3 >= groupCount) {
                    break;
                }
                int childCount = this.mGroupedItemAdapter.getChildCount(i3) + i4;
                if (childCount >= i + 1) {
                    i2 = i - i4;
                    break;
                }
                i3++;
                i4 = childCount;
            }
            return this.mGroupedItemManager.getFlatPosition(GroupedItemManager.getPackedPositionForChild(i3, i2));
        }

        @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
        public boolean isFlatten() {
            return this.mGroupedItemAdapter.isFlatten();
        }

        @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
        public int getGroupCount() {
            return this.mGroupedItemAdapter.getGroupCount();
        }

        @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
        public int getChildCount(int i) {
            return this.mGroupedItemAdapter.getChildCount(i);
        }

        @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
        public long getGroupId(int i) {
            return this.mGroupedItemAdapter.getGroupId(i);
        }

        @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
        public long getChildId(int i, int i2) {
            return this.mGroupedItemAdapter.getChildId(i, i2);
        }

        @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
        public int getGroupItemViewType(int i) {
            return this.mGroupedItemAdapter.getGroupItemViewType(i);
        }

        @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
        public int getChildItemViewType(int i, int i2) {
            return this.mGroupedItemAdapter.getChildItemViewType(i, i2);
        }

        /* JADX WARN: Type inference failed for: r2v1, types: [androidx.recyclerview.widget.RecyclerView$ViewHolder] */
        @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
        /* renamed from: onCreateGroupViewHolder */
        public RecyclerView.ViewHolder mo1338onCreateGroupViewHolder(ViewGroup viewGroup, int i) {
            return this.mGroupedItemAdapter.mo1338onCreateGroupViewHolder(viewGroup, i);
        }

        /* JADX WARN: Type inference failed for: r2v1, types: [androidx.recyclerview.widget.RecyclerView$ViewHolder] */
        @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
        /* renamed from: onCreateChildViewHolder */
        public RecyclerView.ViewHolder mo1337onCreateChildViewHolder(ViewGroup viewGroup, int i) {
            return this.mGroupedItemAdapter.mo1337onCreateChildViewHolder(viewGroup, i);
        }

        @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
        public void onBindGroupViewHolder(RecyclerView.ViewHolder viewHolder, int i, int i2, List list) {
            this.mGroupedItemAdapter.onBindGroupViewHolder(viewHolder, i, i2, list);
            View view = viewHolder.itemView;
            if (view instanceof CheckableView) {
                EditableListViewWrapper.this.setCheckableHeaderView((CheckableView) view, i);
            }
        }

        @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
        public void onBindChildViewHolder(RecyclerView.ViewHolder viewHolder, int i, int i2, int i3, List list) {
            bindCheckState(viewHolder, EditableListViewWrapper.this.mAdapterHolder.packGroupedPosition(i, i2));
            this.mGroupedItemAdapter.onBindChildViewHolder(viewHolder, i, i2, i3, list);
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.SimpleHolder, com.miui.gallery.widget.editwrapper.EditableListViewWrapper.CheckState.Source
        public int getSourceGroupCount() {
            return this.mGroupedItemAdapter.getGroupCount();
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.SimpleHolder, com.miui.gallery.widget.editwrapper.EditableListViewWrapper.CheckState.Source
        public int getSourceItemCount() {
            if (this.mGroupedItemAdapter.isFlatten()) {
                return this.mGroupedItemAdapter.getChildCount(0) + 0;
            }
            int groupCount = this.mGroupedItemAdapter.getGroupCount();
            int i = 0;
            for (int i2 = 0; i2 < groupCount; i2++) {
                i += this.mGroupedItemAdapter.getChildCount(i2);
            }
            return i;
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.SimpleHolder, com.miui.gallery.widget.editwrapper.EditableListViewWrapper.CheckState.Source
        public long getSourceItemId(int i) {
            long expandablePosition = this.mGroupedItemManager.getExpandablePosition(i);
            int packedPositionGroup = GroupedItemManager.getPackedPositionGroup(expandablePosition);
            int packedPositionChild = GroupedItemManager.getPackedPositionChild(expandablePosition);
            if (packedPositionChild == -1) {
                return GroupedItemManager.getCombinedGroupId(this.mGroupedItemAdapter.getGroupId(packedPositionGroup));
            }
            return GroupedItemManager.getCombinedChildId(this.mGroupedItemAdapter.getGroupId(packedPositionGroup), this.mGroupedItemAdapter.getChildId(packedPositionGroup, packedPositionChild));
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.CheckState.Source
        public int getSourceItemGroupIndex(int i) {
            return GroupedItemManager.getPackedPositionGroup(this.mGroupedItemManager.getExpandablePosition(i));
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.SimpleHolder, com.miui.gallery.widget.editwrapper.EditableListViewWrapper.AdapterHolder
        public boolean isValidDataPosition(int i) {
            return GroupedItemManager.getPackedPositionChild(this.mGroupedItemManager.getExpandablePosition(i)) != -1;
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.SimpleHolder, com.miui.gallery.widget.editwrapper.EditableListViewWrapper.AdapterHolder
        public int packGroupedPosition(int i, int i2) {
            if (i2 != -1) {
                return this.mGroupedItemManager.getFlatPosition(GroupedItemManager.getPackedPositionForChild(i, i2));
            }
            return this.mGroupedItemManager.getFlatPosition(GroupedItemManager.getPackedPositionForGroup(i));
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.SimpleHolder, com.miui.gallery.widget.editwrapper.EditableListViewWrapper.AdapterHolder
        public int[] unpackGroupedPosition(int i) {
            long expandablePosition = this.mGroupedItemManager.getExpandablePosition(i);
            return new int[]{GroupedItemManager.getPackedPositionGroup(expandablePosition), GroupedItemManager.getPackedPositionChild(expandablePosition)};
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.SimpleHolder, com.miui.gallery.widget.editwrapper.EditableListViewWrapper.CheckState.Source
        public Iterator<SourceEntry> iterator() {
            return new FullIterator();
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.CheckState.Source
        public Iterator<SourceEntry> iterator(int i) {
            if (i < 0 || i >= this.mGroupedItemManager.getGroupCount()) {
                return CheckState.Source.EMPTY_ITERATOR;
            }
            return new GroupIterator(i);
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.SimpleHolder, com.miui.gallery.widget.editwrapper.EditableListViewWrapper.AdapterHolder
        public int getSpanSize(int i, int i2) {
            if (this.mGroupedItemManager.isGroupPosition(i)) {
                return i2;
            }
            return 1;
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.SimpleHolder, com.miui.gallery.widget.editwrapper.EditableListViewWrapper.AdapterHolder
        public int getSpanIndex(int i, int i2) {
            int[] unpackGroupedPosition = unpackGroupedPosition(i);
            if (unpackGroupedPosition[0] == -1 || unpackGroupedPosition[1] == -1) {
                return 0;
            }
            return unpackGroupedPosition[1] % i2;
        }

        /* loaded from: classes2.dex */
        public class GroupIterator implements Iterator<SourceEntry> {
            public final int childCount;
            public final int groupIdx;
            public int childIdx = 0;
            public SourceEntry next = null;

            public GroupIterator(int i) {
                GroupedAdapterHolder.this = r2;
                this.groupIdx = i;
                int childCount = r2.mGroupedItemAdapter.getChildCount(i);
                this.childCount = childCount;
                if (childCount > 0) {
                    createNext();
                }
            }

            public final void advance() {
                if (this.next == null) {
                    throw new NoSuchElementException();
                }
                if (this.childIdx < this.childCount) {
                    createNext();
                } else {
                    this.next = null;
                }
            }

            public final void createNext() {
                long packedPositionForChild = GroupedItemManager.getPackedPositionForChild(this.groupIdx, this.childIdx);
                long groupId = GroupedAdapterHolder.this.mGroupedItemAdapter.getGroupId(this.groupIdx);
                GroupedItemAdapter groupedItemAdapter = GroupedAdapterHolder.this.mGroupedItemAdapter;
                int i = this.groupIdx;
                int i2 = this.childIdx;
                this.childIdx = i2 + 1;
                this.next = new SourceEntry(GroupedAdapterHolder.this.mGroupedItemManager.getFlatPosition(packedPositionForChild), GroupedItemManager.getCombinedChildId(groupId, groupedItemAdapter.getChildId(i, i2)));
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.next != null;
            }

            @Override // java.util.Iterator
            /* renamed from: next */
            public SourceEntry mo1817next() {
                SourceEntry sourceEntry = this.next;
                advance();
                return sourceEntry;
            }
        }

        /* loaded from: classes2.dex */
        public class FullIterator implements Iterator<SourceEntry> {
            public int childCount;
            public final int groupCount;
            public int groupIdx = 0;
            public int childIdx = 0;
            public SourceEntry next = null;

            public FullIterator() {
                GroupedAdapterHolder.this = r3;
                int groupCount = r3.mGroupedItemAdapter.getGroupCount();
                this.groupCount = groupCount;
                if (groupCount > 0) {
                    while (this.groupIdx < this.groupCount) {
                        int childCount = r3.mGroupedItemAdapter.getChildCount(this.groupIdx);
                        this.childCount = childCount;
                        if (childCount > 0) {
                            break;
                        }
                        this.groupIdx++;
                    }
                    if (this.childCount <= 0) {
                        return;
                    }
                    createNext();
                }
            }

            public final void advance() {
                int i;
                if (this.next == null) {
                    throw new NoSuchElementException();
                }
                if (this.childIdx < this.childCount) {
                    createNext();
                    return;
                }
                this.childCount = 0;
                this.childIdx = 0;
                while (true) {
                    i = this.childCount;
                    if (i > 0) {
                        break;
                    }
                    int i2 = this.groupIdx + 1;
                    this.groupIdx = i2;
                    if (i2 >= this.groupCount) {
                        break;
                    }
                    this.childCount = GroupedAdapterHolder.this.mGroupedItemAdapter.getChildCount(this.groupIdx);
                }
                if (i > 0) {
                    createNext();
                } else {
                    this.next = null;
                }
            }

            public final void createNext() {
                long packedPositionForChild = GroupedItemManager.getPackedPositionForChild(this.groupIdx, this.childIdx);
                long groupId = GroupedAdapterHolder.this.mGroupedItemAdapter.getGroupId(this.groupIdx);
                GroupedItemAdapter groupedItemAdapter = GroupedAdapterHolder.this.mGroupedItemAdapter;
                int i = this.groupIdx;
                int i2 = this.childIdx;
                this.childIdx = i2 + 1;
                this.next = new SourceEntry(GroupedAdapterHolder.this.mGroupedItemManager.getFlatPosition(packedPositionForChild), GroupedItemManager.getCombinedChildId(groupId, groupedItemAdapter.getChildId(i, i2)));
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.next != null;
            }

            @Override // java.util.Iterator
            /* renamed from: next */
            public SourceEntry mo1816next() {
                SourceEntry sourceEntry = this.next;
                advance();
                return sourceEntry;
            }
        }
    }

    public final boolean setGroupCheck(int i) {
        if (this.mAdapterHolder.isGroupCheckable()) {
            CheckState checkState = this.mCheckState;
            if (checkState instanceof CheckStateWithGroup) {
                boolean groupCheckState = ((CheckStateWithGroup) checkState).setGroupCheckState(i);
                updateOnScreenViewsState();
                updateActionMode();
                return groupCheckState;
            }
        }
        return false;
    }

    public final void setCheckableHeaderView(CheckableView checkableView, final int i) {
        checkableView.setCheckable(isInChoiceMode());
        if (isInChoiceMode()) {
            checkableView.setCheckableListener(new View.OnClickListener() { // from class: com.miui.gallery.widget.editwrapper.EditableListViewWrapper$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    EditableListViewWrapper.m1812$r8$lambda$dly37b4lmALbtY1DS9Nw_IPzEg(EditableListViewWrapper.this, i, view);
                }
            });
            checkableView.setChecked(((CheckStateWithGroup) this.mCheckState).getGroupCheckState(i));
        }
    }

    public /* synthetic */ void lambda$setCheckableHeaderView$2(int i, View view) {
        boolean groupCheck = setGroupCheck(i);
        notifyDataChanged();
        if (this.mMultiChoiceModeCallback == null || this.mChoiceActionMode == null) {
            return;
        }
        LinearMotorHelper.performHapticFeedback(this.mRecyclerView, LinearMotorHelper.HAPTIC_MESH_NORMAL);
        this.mMultiChoiceModeCallback.mWrapped.onAllItemsCheckedStateChanged(this.mChoiceActionMode, true);
        this.mMultiChoiceModeCallback.mWrapped.statGroupItemsCheckedStateChanged(groupCheck);
    }

    /* loaded from: classes2.dex */
    public static class CheckStateWithGroup extends CheckState {
        public AnimationManager mAnimationManager;
        public SparseBooleanArray mGroupCheckState = new SparseBooleanArray(0);
        public GalleryRecyclerView mListView;

        /* renamed from: $r8$lambda$Py4DmnHqMJl1-hB79WiIQn4qsm4 */
        public static /* synthetic */ void m1815$r8$lambda$Py4DmnHqMJl1hB79WiIQn4qsm4(CheckStateWithGroup checkStateWithGroup) {
            checkStateWithGroup.lambda$setAllChecked$0();
        }

        public CheckStateWithGroup(GalleryRecyclerView galleryRecyclerView, AnimationManager animationManager) {
            this.mListView = galleryRecyclerView;
            this.mAnimationManager = animationManager;
        }

        public boolean getGroupCheckState(int i) {
            return this.mGroupCheckState.get(i);
        }

        public boolean setGroupCheckState(int i) {
            boolean z = !getGroupCheckState(i);
            this.mGroupCheckState.put(i, z);
            Iterator<SourceEntry> it = this.mSource.iterator(i);
            int i2 = 0;
            while (it.hasNext()) {
                SourceEntry next = it.next();
                setCheckStateInternal(next.position, z, false);
                RecyclerView.ViewHolder findViewHolderForAdapterPositionForExternal = this.mListView.findViewHolderForAdapterPositionForExternal(next.position);
                if (findViewHolderForAdapterPositionForExternal != null) {
                    View view = findViewHolderForAdapterPositionForExternal.itemView;
                    if (view instanceof PickAnimationHelper$BackgroundImageViewable) {
                        this.mAnimationManager.startScaleItemImageViewAnimation(((PickAnimationHelper$BackgroundImageViewable) view).getBackgroundImageView(), next.position, z, i2 * 40);
                        i2++;
                    }
                }
            }
            return z;
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.CheckState
        public void clear() {
            super.clear();
            this.mGroupCheckState.clear();
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.CheckState
        public void setCheckState(int i, boolean z) {
            setCheckStateInternal(i, z, true);
        }

        public final void setCheckStateInternal(int i, boolean z, boolean z2) {
            super.setCheckState(i, z);
            if (this.mStatus != CheckState.Status.CLEAN || !z2) {
                return;
            }
            int sourceItemGroupIndex = this.mSource.getSourceItemGroupIndex(i);
            boolean groupCheckState = getGroupCheckState(sourceItemGroupIndex);
            boolean z3 = true;
            Iterator<SourceEntry> it = this.mSource.iterator(sourceItemGroupIndex);
            while (true) {
                if (it.hasNext()) {
                    if (!getCheckState(it.next().position)) {
                        z3 = false;
                        break;
                    }
                } else {
                    break;
                }
            }
            if (groupCheckState == z3) {
                return;
            }
            this.mGroupCheckState.put(sourceItemGroupIndex, z3);
            this.mListView.getAdapter().notifyDataSetChanged();
        }

        @Override // com.miui.gallery.widget.editwrapper.EditableListViewWrapper.CheckState
        public void setAllChecked(boolean z) {
            super.setAllChecked(z);
            for (int i = 0; i < this.mSource.getSourceGroupCount(); i++) {
                this.mGroupCheckState.put(i, z);
            }
            this.mListView.post(new Runnable() { // from class: com.miui.gallery.widget.editwrapper.EditableListViewWrapper$CheckStateWithGroup$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    EditableListViewWrapper.CheckStateWithGroup.m1815$r8$lambda$Py4DmnHqMJl1hB79WiIQn4qsm4(EditableListViewWrapper.CheckStateWithGroup.this);
                }
            });
        }

        public /* synthetic */ void lambda$setAllChecked$0() {
            RecyclerView.Adapter adapter = this.mListView.getAdapter();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class CheckState {
        public boolean mIsContainsCheckedItemsFromInitState;
        public Source mSource;
        public Map<Long, Long> mUserSelectedIdIndex = new HashMap();
        public Map<Long, Long> mReverseModeSelectedIdIndex = new HashMap();
        public Mode mCheckMode = Mode.NORMAL;
        public BitSet mCheckState = new BitSet();
        public Status mStatus = Status.CLEAN;
        public long mIndex = 0;
        public long mReverseIndex = 0;

        /* loaded from: classes2.dex */
        public enum Mode {
            NORMAL,
            REVERSE
        }

        /* loaded from: classes2.dex */
        public enum Status {
            DIRTY,
            CLEAN
        }

        public static /* synthetic */ void $r8$lambda$iDDJ9abVgCFXlbMwZTCM9bOt4Sw(CheckState checkState, SourceEntry sourceEntry) {
            checkState.lambda$setAllChecked$0(sourceEntry);
        }

        /* renamed from: $r8$lambda$yezY7MBmsdtbP6PbNW8Oakh-CIQ */
        public static /* synthetic */ void m1813$r8$lambda$yezY7MBmsdtbP6PbNW8OakhCIQ(CheckState checkState, SourceEntry sourceEntry) {
            checkState.lambda$refreshCheckStateWhenDirty$1(sourceEntry);
        }

        public void bind(Source source) {
            if (source == null) {
                return;
            }
            this.mSource = source;
        }

        public boolean getCheckState(int i) {
            if (this.mStatus == Status.CLEAN) {
                return this.mCheckState.get(i);
            }
            return getCheckState(this.mSource.getSourceItemId(i));
        }

        public boolean getCheckState(long j) {
            return (this.mCheckMode == Mode.REVERSE) == (this.mUserSelectedIdIndex.get(Long.valueOf(j)) == null);
        }

        public void setCheckState(SparseBooleanArray sparseBooleanArray) {
            for (int i = 0; i < sparseBooleanArray.size(); i++) {
                boolean valueAt = sparseBooleanArray.valueAt(i);
                setCheckState(sparseBooleanArray.keyAt(i), valueAt);
                if (!this.mIsContainsCheckedItemsFromInitState && valueAt) {
                    this.mIsContainsCheckedItemsFromInitState = true;
                }
            }
        }

        public void setCheckState(int i, boolean z) {
            if (this.mStatus == Status.CLEAN) {
                this.mCheckState.set(i, z);
            }
            long sourceItemId = this.mSource.getSourceItemId(i);
            if (this.mCheckMode == Mode.NORMAL) {
                if (z) {
                    Map<Long, Long> map = this.mUserSelectedIdIndex;
                    Long valueOf = Long.valueOf(sourceItemId);
                    long j = this.mIndex;
                    this.mIndex = 1 + j;
                    map.put(valueOf, Long.valueOf(j));
                    return;
                }
                this.mUserSelectedIdIndex.remove(Long.valueOf(sourceItemId));
            } else if (z) {
                this.mUserSelectedIdIndex.remove(Long.valueOf(sourceItemId));
                Map<Long, Long> map2 = this.mReverseModeSelectedIdIndex;
                Long valueOf2 = Long.valueOf(sourceItemId);
                long j2 = this.mReverseIndex;
                this.mReverseIndex = 1 + j2;
                map2.put(valueOf2, Long.valueOf(j2));
            } else {
                Map<Long, Long> map3 = this.mUserSelectedIdIndex;
                Long valueOf3 = Long.valueOf(sourceItemId);
                long j3 = this.mIndex;
                this.mIndex = 1 + j3;
                map3.put(valueOf3, Long.valueOf(j3));
                this.mReverseModeSelectedIdIndex.remove(Long.valueOf(sourceItemId));
            }
        }

        public void setAllChecked(boolean z) {
            this.mStatus = Status.CLEAN;
            this.mCheckMode = z ? Mode.REVERSE : Mode.NORMAL;
            this.mUserSelectedIdIndex.clear();
            this.mReverseModeSelectedIdIndex.clear();
            this.mIndex = 0L;
            this.mReverseIndex = 0L;
            if (z) {
                this.mSource.iterator().forEachRemaining(new Consumer() { // from class: com.miui.gallery.widget.editwrapper.EditableListViewWrapper$CheckState$$ExternalSyntheticLambda1
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        EditableListViewWrapper.CheckState.$r8$lambda$iDDJ9abVgCFXlbMwZTCM9bOt4Sw(EditableListViewWrapper.CheckState.this, (EditableListViewWrapper.SourceEntry) obj);
                    }
                });
            } else {
                this.mCheckState.clear();
            }
        }

        public /* synthetic */ void lambda$setAllChecked$0(SourceEntry sourceEntry) {
            this.mCheckState.set(sourceEntry.position, true);
        }

        public void clear() {
            this.mCheckMode = Mode.NORMAL;
            this.mStatus = Status.CLEAN;
            this.mCheckState.clear();
            this.mUserSelectedIdIndex.clear();
            this.mReverseModeSelectedIdIndex.clear();
            this.mIndex = 0L;
            this.mReverseIndex = 0L;
        }

        public void handleDataChanged() {
            this.mCheckState.clear();
            this.mStatus = Status.DIRTY;
            refreshCheckStateWhenDirty();
        }

        public final void refreshCheckStateWhenDirty() {
            if (this.mStatus == Status.DIRTY) {
                Trace.beginSection("refreshCheckState");
                long currentTimeMillis = System.currentTimeMillis();
                if (this.mCheckState.isEmpty() && this.mCheckMode == Mode.NORMAL && this.mUserSelectedIdIndex.isEmpty() && !this.mIsContainsCheckedItemsFromInitState) {
                    DefaultLogger.d("EditableListViewWrapper", "no user check, already clean");
                } else if (this.mCheckMode == Mode.NORMAL && this.mUserSelectedIdIndex.size() <= 0) {
                    this.mCheckState.clear();
                } else {
                    this.mCheckState.clear();
                    this.mSource.iterator().forEachRemaining(new Consumer() { // from class: com.miui.gallery.widget.editwrapper.EditableListViewWrapper$CheckState$$ExternalSyntheticLambda2
                        @Override // java.util.function.Consumer
                        public final void accept(Object obj) {
                            EditableListViewWrapper.CheckState.m1813$r8$lambda$yezY7MBmsdtbP6PbNW8OakhCIQ(EditableListViewWrapper.CheckState.this, (EditableListViewWrapper.SourceEntry) obj);
                        }
                    });
                }
                this.mStatus = Status.CLEAN;
                DefaultLogger.d("EditableListViewWrapper", "refreshCheckState costs %dms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                Trace.endSection();
            }
        }

        public /* synthetic */ void lambda$refreshCheckStateWhenDirty$1(SourceEntry sourceEntry) {
            this.mCheckState.set(sourceEntry.position, (this.mCheckMode == Mode.NORMAL && this.mUserSelectedIdIndex.containsKey(Long.valueOf(sourceEntry.id))) || (this.mCheckMode == Mode.REVERSE && !this.mUserSelectedIdIndex.containsKey(Long.valueOf(sourceEntry.id))));
        }

        public List<Integer> getCheckedPositions() {
            refreshCheckStateWhenDirty();
            if (this.mCheckState.cardinality() > 0) {
                return (List) this.mCheckState.stream().collect(EditableListViewWrapper$CheckState$$ExternalSyntheticLambda4.INSTANCE, EditableListViewWrapper$CheckState$$ExternalSyntheticLambda3.INSTANCE, EditableListViewWrapper$CheckState$$ExternalSyntheticLambda0.INSTANCE);
            }
            return Collections.emptyList();
        }

        public long[] getCheckedItemIds() {
            int checkedCount = getCheckedCount();
            int i = 0;
            if (checkedCount == 0) {
                return new long[0];
            }
            if (this.mCheckMode == Mode.NORMAL) {
                long[] jArr = new long[this.mUserSelectedIdIndex.size()];
                long[] jArr2 = new long[this.mUserSelectedIdIndex.size()];
                for (Map.Entry<Long, Long> entry : this.mUserSelectedIdIndex.entrySet()) {
                    jArr[i] = entry.getKey().longValue();
                    jArr2[i] = entry.getValue().longValue();
                    i++;
                }
                return getSortedKeysByIndex(jArr, jArr2);
            }
            long[] jArr3 = new long[checkedCount];
            int i2 = 0;
            for (SourceEntry sourceEntry : this.mSource) {
                if (!this.mUserSelectedIdIndex.containsKey(Long.valueOf(sourceEntry.id)) && !this.mReverseModeSelectedIdIndex.containsKey(Long.valueOf(sourceEntry.id))) {
                    jArr3[i2] = sourceEntry.id;
                    i2++;
                }
            }
            if (this.mReverseModeSelectedIdIndex.size() > 0) {
                long[] jArr4 = new long[this.mReverseModeSelectedIdIndex.size()];
                long[] jArr5 = new long[this.mReverseModeSelectedIdIndex.size()];
                int i3 = 0;
                for (Map.Entry<Long, Long> entry2 : this.mReverseModeSelectedIdIndex.entrySet()) {
                    jArr4[i3] = entry2.getKey().longValue();
                    jArr5[i3] = entry2.getValue().longValue();
                    i3++;
                }
                long[] sortedKeysByIndex = getSortedKeysByIndex(jArr4, jArr5);
                while (i < sortedKeysByIndex.length && i2 < checkedCount) {
                    jArr3[i2] = sortedKeysByIndex[i];
                    i++;
                    i2++;
                }
            }
            return jArr3;
        }

        public int[] getCheckedItemOrderedPositions() {
            int checkedCount = getCheckedCount();
            int i = 0;
            if (checkedCount == 0) {
                return new int[0];
            }
            if (this.mCheckMode == Mode.NORMAL) {
                int[] iArr = new int[this.mUserSelectedIdIndex.size()];
                long[] jArr = new long[this.mUserSelectedIdIndex.size()];
                Iterator<SourceEntry> it = this.mSource.iterator();
                while (it.hasNext() && i < checkedCount) {
                    SourceEntry next = it.next();
                    if (this.mUserSelectedIdIndex.containsKey(Long.valueOf(next.id))) {
                        iArr[i] = next.position;
                        jArr[i] = this.mUserSelectedIdIndex.get(Long.valueOf(next.id)).longValue();
                        i++;
                    }
                }
                return getSortedPositionsByIndex(iArr, jArr);
            }
            int[] iArr2 = new int[checkedCount];
            int[] iArr3 = new int[this.mReverseModeSelectedIdIndex.size()];
            long[] jArr2 = new long[this.mReverseModeSelectedIdIndex.size()];
            Iterator<SourceEntry> it2 = this.mSource.iterator();
            int i2 = 0;
            int i3 = 0;
            while (it2.hasNext() && i2 < checkedCount) {
                SourceEntry next2 = it2.next();
                if (!this.mUserSelectedIdIndex.containsKey(Long.valueOf(next2.id)) && !this.mReverseModeSelectedIdIndex.containsKey(Long.valueOf(next2.id))) {
                    iArr2[i3] = next2.position;
                    i3++;
                }
                if (this.mReverseModeSelectedIdIndex.containsKey(Long.valueOf(next2.id))) {
                    iArr3[i2] = next2.position;
                    jArr2[i2] = this.mReverseModeSelectedIdIndex.get(Long.valueOf(next2.id)).longValue();
                    i2++;
                }
            }
            if (this.mReverseModeSelectedIdIndex.size() > 0) {
                int[] sortedPositionsByIndex = getSortedPositionsByIndex(iArr3, jArr2);
                while (i < sortedPositionsByIndex.length && i3 < checkedCount) {
                    iArr2[i3] = sortedPositionsByIndex[i];
                    i++;
                    i3++;
                }
            }
            return iArr2;
        }

        public long[] getSortedKeysByIndex(long[] jArr, long[] jArr2) {
            if (jArr.length != jArr2.length) {
                return jArr;
            }
            SyncSortUtil.sort(jArr, jArr2, 0, jArr.length - 1);
            return jArr;
        }

        public int[] getSortedPositionsByIndex(int[] iArr, long[] jArr) {
            if (iArr.length != jArr.length) {
                return iArr;
            }
            SyncSortUtil.sort(iArr, jArr, 0, iArr.length - 1);
            return iArr;
        }

        public int getCheckedCount() {
            if (this.mCheckMode == Mode.NORMAL) {
                return this.mUserSelectedIdIndex.size();
            }
            return this.mSource.getSourceItemCount() - this.mUserSelectedIdIndex.size();
        }

        public boolean isAllItemsChecked() {
            int checkedCount = getCheckedCount();
            return checkedCount > 0 && checkedCount == this.mSource.getSourceItemCount();
        }

        /* loaded from: classes2.dex */
        public interface Source {
            public static final Iterator<SourceEntry> EMPTY_ITERATOR = new Iterator<SourceEntry>() { // from class: com.miui.gallery.widget.editwrapper.EditableListViewWrapper.CheckState.Source.1
                @Override // java.util.Iterator
                public boolean hasNext() {
                    return false;
                }

                @Override // java.util.Iterator
                /* renamed from: next */
                public SourceEntry mo1814next() {
                    return null;
                }
            };

            int getSourceGroupCount();

            int getSourceItemCount();

            default int getSourceItemGroupIndex(int i) {
                return -1;
            }

            long getSourceItemId(int i);

            Iterator<SourceEntry> iterator();

            default Iterator<SourceEntry> iterator(int i) {
                return EMPTY_ITERATOR;
            }
        }
    }

    /* loaded from: classes2.dex */
    public class RestoreCheckStateObserver extends RecyclerView.AdapterDataObserver {
        public RestoreCheckStateObserver() {
            EditableListViewWrapper.this = r1;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onChanged() {
            super.onChanged();
            EditableListViewWrapper.this.handleDataChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeChanged(int i, int i2) {
            super.onItemRangeChanged(i, i2);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeChanged(int i, int i2, Object obj) {
            super.onItemRangeChanged(i, i2, obj);
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeInserted(int i, int i2) {
            super.onItemRangeInserted(i, i2);
            EditableListViewWrapper.this.handleDataChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeRemoved(int i, int i2) {
            super.onItemRangeRemoved(i, i2);
            EditableListViewWrapper.this.handleDataChanged();
        }

        @Override // androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
        public void onItemRangeMoved(int i, int i2, int i3) {
            super.onItemRangeMoved(i, i2, i3);
            EditableListViewWrapper.this.handleDataChanged();
        }
    }
}
