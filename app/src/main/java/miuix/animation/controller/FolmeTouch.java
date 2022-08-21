package miuix.animation.controller;

import android.graphics.Color;
import android.util.ArrayMap;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AbsListView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.search.statistics.SearchStatUtils;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;
import miuix.animation.IAnimTarget;
import miuix.animation.ITouchStyle;
import miuix.animation.ViewTarget;
import miuix.animation.base.AnimConfig;
import miuix.animation.internal.AnimValueUtils;
import miuix.animation.listener.TransitionListener;
import miuix.animation.listener.UpdateInfo;
import miuix.animation.property.ViewProperty;
import miuix.animation.property.ViewPropertyExt;
import miuix.animation.utils.CommonUtils;
import miuix.animation.utils.EaseManager;
import miuix.animation.utils.LogUtils;
import miuix.folme.R$color;
import miuix.folme.R$id;

/* loaded from: classes3.dex */
public class FolmeTouch extends FolmeBase implements ITouchStyle {
    public static WeakHashMap<View, InnerViewTouchListener> sTouchRecord = new WeakHashMap<>();
    public boolean mClearTint;
    public boolean mClickInvoked;
    public View.OnClickListener mClickListener;
    public TransitionListener mDefListener;
    public AnimConfig mDownConfig;
    public int mDownWeight;
    public float mDownX;
    public float mDownY;
    public FolmeFont mFontStyle;
    public boolean mIsDown;
    public WeakReference<View> mListView;
    public int[] mLocation;
    public boolean mLongClickInvoked;
    public View.OnLongClickListener mLongClickListener;
    public LongClickTask mLongClickTask;
    public float mScaleDist;
    public Map<ITouchStyle.TouchType, Boolean> mScaleSetMap;
    public boolean mSetTint;
    public int mTouchIndex;
    public WeakReference<View> mTouchView;
    public AnimConfig mUpConfig;
    public int mUpWeight;

    public FolmeTouch(IAnimTarget... iAnimTargetArr) {
        super(iAnimTargetArr);
        this.mLocation = new int[2];
        this.mScaleSetMap = new ArrayMap();
        this.mDownConfig = new AnimConfig();
        this.mUpConfig = new AnimConfig();
        this.mClearTint = false;
        this.mDefListener = new TransitionListener() { // from class: miuix.animation.controller.FolmeTouch.1
            @Override // miuix.animation.listener.TransitionListener
            public void onBegin(Object obj, Collection<UpdateInfo> collection) {
                if (obj.equals(ITouchStyle.TouchType.DOWN)) {
                    AnimState.alignState(FolmeTouch.this.mState.getState(ITouchStyle.TouchType.UP), collection);
                }
            }
        };
        initScaleDist(iAnimTargetArr.length > 0 ? iAnimTargetArr[0] : null);
        this.mState.getState(ITouchStyle.TouchType.UP).add(ViewProperty.SCALE_X, 1.0d).add(ViewProperty.SCALE_Y, 1.0d);
        setTintColor();
        this.mDownConfig.setEase(EaseManager.getStyle(-2, 0.99f, 0.15f));
        this.mDownConfig.addListeners(this.mDefListener);
        this.mUpConfig.setEase(-2, 0.99f, 0.3f).setSpecial(ViewProperty.ALPHA, -2L, 0.9f, 0.2f);
    }

    public final void setTintColor() {
        if (this.mSetTint || this.mClearTint) {
            return;
        }
        int argb = Color.argb(20, 0, 0, 0);
        Object mo2588getTargetObject = this.mState.getTarget().mo2588getTargetObject();
        if (mo2588getTargetObject instanceof View) {
            argb = ((View) mo2588getTargetObject).getResources().getColor(R$color.miuix_folme_color_touch_tint);
        }
        ViewPropertyExt.ForegroundProperty foregroundProperty = ViewPropertyExt.FOREGROUND;
        this.mState.getState(ITouchStyle.TouchType.DOWN).add(foregroundProperty, argb);
        this.mState.getState(ITouchStyle.TouchType.UP).add(foregroundProperty, SearchStatUtils.POW);
    }

    public final void initScaleDist(IAnimTarget iAnimTarget) {
        View mo2588getTargetObject = iAnimTarget instanceof ViewTarget ? ((ViewTarget) iAnimTarget).mo2588getTargetObject() : null;
        if (mo2588getTargetObject != null) {
            this.mScaleDist = TypedValue.applyDimension(1, 10.0f, mo2588getTargetObject.getResources().getDisplayMetrics());
        }
    }

    @Override // miuix.animation.controller.FolmeBase, miuix.animation.IStateContainer
    public void clean() {
        super.clean();
        FolmeFont folmeFont = this.mFontStyle;
        if (folmeFont != null) {
            folmeFont.clean();
        }
        this.mScaleSetMap.clear();
        WeakReference<View> weakReference = this.mTouchView;
        if (weakReference != null) {
            resetView(weakReference);
            this.mTouchView = null;
        }
        WeakReference<View> weakReference2 = this.mListView;
        if (weakReference2 != null) {
            View resetView = resetView(weakReference2);
            if (resetView != null) {
                resetView.setTag(R$id.miuix_animation_tag_touch_listener, null);
            }
            this.mListView = null;
        }
        resetTouchStatus();
    }

    public final View resetView(WeakReference<View> weakReference) {
        View view = weakReference.get();
        if (view != null) {
            view.setOnTouchListener(null);
        }
        return view;
    }

    public void setFontStyle(FolmeFont folmeFont) {
        this.mFontStyle = folmeFont;
    }

    @Override // miuix.animation.ITouchStyle
    public ITouchStyle setTintMode(int i) {
        this.mDownConfig.setTintMode(i);
        this.mUpConfig.setTintMode(i);
        return this;
    }

    public static boolean isOnTouchView(View view, int[] iArr, MotionEvent motionEvent) {
        if (view != null) {
            view.getLocationOnScreen(iArr);
            int rawX = (int) motionEvent.getRawX();
            int rawY = (int) motionEvent.getRawY();
            return rawX >= iArr[0] && rawX <= iArr[0] + view.getWidth() && rawY >= iArr[1] && rawY <= iArr[1] + view.getHeight();
        }
        return true;
    }

    public final boolean setTouchView(View view) {
        WeakReference<View> weakReference = this.mTouchView;
        if ((weakReference != null ? weakReference.get() : null) == view) {
            return false;
        }
        this.mTouchView = new WeakReference<>(view);
        return true;
    }

    @Override // miuix.animation.ITouchStyle
    public void handleTouchOf(View view, AnimConfig... animConfigArr) {
        handleTouchOf(view, false, animConfigArr);
    }

    @Override // miuix.animation.ITouchStyle
    public void handleTouchOf(View view, boolean z, AnimConfig... animConfigArr) {
        doHandleTouchOf(view, null, null, z, animConfigArr);
    }

    public final void doHandleTouchOf(final View view, View.OnClickListener onClickListener, View.OnLongClickListener onLongClickListener, final boolean z, final AnimConfig... animConfigArr) {
        setClickAndLongClickListener(onClickListener, onLongClickListener);
        handleViewTouch(view, animConfigArr);
        if (setTouchView(view)) {
            if (LogUtils.isLogEnabled()) {
                LogUtils.debug("handleViewTouch for " + view, new Object[0]);
            }
            final boolean isClickable = view.isClickable();
            view.setClickable(true);
            CommonUtils.runOnPreDraw(view, new CommonUtils.PreDrawTask() { // from class: miuix.animation.controller.FolmeTouch.3
                @Override // miuix.animation.utils.CommonUtils.PreDrawTask
                public boolean call() {
                    if (z) {
                        return true;
                    }
                    if (view.isAttachedToWindow()) {
                        if (FolmeTouch.this.bindListView(view, true, animConfigArr)) {
                            FolmeTouch.this.resetViewTouch(view, isClickable);
                        }
                        return true;
                    }
                    if (LogUtils.isLogEnabled()) {
                        LogUtils.debug("detached and retry bindListView later for " + view, new Object[0]);
                    }
                    return false;
                }
            });
        }
    }

    public final void setClickAndLongClickListener(View.OnClickListener onClickListener, View.OnLongClickListener onLongClickListener) {
        IAnimTarget target = this.mState.getTarget();
        View mo2588getTargetObject = target instanceof ViewTarget ? ((ViewTarget) target).mo2588getTargetObject() : null;
        if (mo2588getTargetObject == null) {
            return;
        }
        if (this.mClickListener != null && onClickListener == null) {
            mo2588getTargetObject.setOnClickListener(null);
        } else if (onClickListener != null) {
            mo2588getTargetObject.setOnClickListener(new View.OnClickListener() { // from class: miuix.animation.controller.FolmeTouch.4
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    FolmeTouch.this.invokeClick(view);
                }
            });
        }
        this.mClickListener = onClickListener;
        if (this.mLongClickListener != null && onLongClickListener == null) {
            mo2588getTargetObject.setOnLongClickListener(null);
        } else if (onLongClickListener != null) {
            mo2588getTargetObject.setOnLongClickListener(new View.OnLongClickListener() { // from class: miuix.animation.controller.FolmeTouch.5
                @Override // android.view.View.OnLongClickListener
                public boolean onLongClick(View view) {
                    if (!FolmeTouch.this.mLongClickInvoked) {
                        FolmeTouch.this.invokeLongClick(view);
                        return true;
                    }
                    return false;
                }
            });
        }
        this.mLongClickListener = onLongClickListener;
    }

    public final boolean bindListView(View view, boolean z, AnimConfig... animConfigArr) {
        ListViewInfo listViewInfo;
        if (this.mState.getTarget() == null || (listViewInfo = getListViewInfo(view)) == null || listViewInfo.listView == null) {
            return false;
        }
        if (LogUtils.isLogEnabled()) {
            LogUtils.debug("handleListViewTouch for " + view, new Object[0]);
        }
        handleListViewTouch(listViewInfo.listView, view, z, animConfigArr);
        return true;
    }

    /* loaded from: classes3.dex */
    public static class ListViewInfo {
        public View itemView;
        public ViewGroup listView;

        public ListViewInfo() {
        }
    }

    public final ListViewInfo getListViewInfo(View view) {
        ViewGroup viewGroup = null;
        ListViewInfo listViewInfo = new ListViewInfo();
        ViewParent parent = view.getParent();
        while (true) {
            if (parent == null) {
                break;
            } else if (parent instanceof AbsListView) {
                viewGroup = (AbsListView) parent;
                break;
            } else if (parent instanceof RecyclerView) {
                viewGroup = (RecyclerView) parent;
                break;
            } else {
                if (parent instanceof View) {
                    view = (View) parent;
                }
                parent = parent.getParent();
            }
        }
        if (viewGroup != null) {
            this.mListView = new WeakReference<>(listViewInfo.listView);
            listViewInfo.listView = viewGroup;
            listViewInfo.itemView = view;
        }
        return listViewInfo;
    }

    public static ListViewTouchListener getListViewTouchListener(ViewGroup viewGroup) {
        return (ListViewTouchListener) viewGroup.getTag(R$id.miuix_animation_tag_touch_listener);
    }

    public final void handleListViewTouch(ViewGroup viewGroup, View view, boolean z, AnimConfig... animConfigArr) {
        ListViewTouchListener listViewTouchListener = getListViewTouchListener(viewGroup);
        if (listViewTouchListener == null) {
            listViewTouchListener = new ListViewTouchListener(viewGroup);
            viewGroup.setTag(R$id.miuix_animation_tag_touch_listener, listViewTouchListener);
        }
        if (z) {
            viewGroup.setOnTouchListener(listViewTouchListener);
        }
        listViewTouchListener.putListener(view, new InnerListViewTouchListener(this, animConfigArr));
    }

    /* loaded from: classes3.dex */
    public static class InnerListViewTouchListener implements View.OnTouchListener {
        public AnimConfig[] mConfigs;
        public WeakReference<FolmeTouch> mFolmeTouchRef;

        public InnerListViewTouchListener(FolmeTouch folmeTouch, AnimConfig... animConfigArr) {
            this.mFolmeTouchRef = new WeakReference<>(folmeTouch);
            this.mConfigs = animConfigArr;
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            WeakReference<FolmeTouch> weakReference = this.mFolmeTouchRef;
            FolmeTouch folmeTouch = weakReference == null ? null : weakReference.get();
            if (folmeTouch != null) {
                if (motionEvent == null) {
                    folmeTouch.onEventUp(this.mConfigs);
                    return false;
                }
                folmeTouch.handleMotionEvent(view, motionEvent, this.mConfigs);
                return false;
            }
            return false;
        }
    }

    public final void handleViewTouch(View view, AnimConfig... animConfigArr) {
        InnerViewTouchListener innerViewTouchListener = sTouchRecord.get(view);
        if (innerViewTouchListener == null) {
            innerViewTouchListener = new InnerViewTouchListener();
            sTouchRecord.put(view, innerViewTouchListener);
        }
        view.setOnTouchListener(innerViewTouchListener);
        innerViewTouchListener.addTouch(this, animConfigArr);
    }

    /* loaded from: classes3.dex */
    public static class InnerViewTouchListener implements View.OnTouchListener {
        public WeakHashMap<FolmeTouch, AnimConfig[]> mTouchMap;

        public InnerViewTouchListener() {
            this.mTouchMap = new WeakHashMap<>();
        }

        public void addTouch(FolmeTouch folmeTouch, AnimConfig... animConfigArr) {
            this.mTouchMap.put(folmeTouch, animConfigArr);
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            for (Map.Entry<FolmeTouch, AnimConfig[]> entry : this.mTouchMap.entrySet()) {
                entry.getKey().handleMotionEvent(view, motionEvent, entry.getValue());
            }
            return false;
        }
    }

    public final void resetViewTouch(View view, boolean z) {
        view.setClickable(z);
        view.setOnTouchListener(null);
    }

    public final void onEventDown(AnimConfig... animConfigArr) {
        if (LogUtils.isLogEnabled()) {
            LogUtils.debug("onEventDown, touchDown", new Object[0]);
        }
        this.mIsDown = true;
        touchDown(animConfigArr);
    }

    public final void onEventMove(MotionEvent motionEvent, View view, AnimConfig... animConfigArr) {
        if (this.mIsDown) {
            if (!isOnTouchView(view, this.mLocation, motionEvent)) {
                touchUp(animConfigArr);
                resetTouchStatus();
            } else if (this.mLongClickTask == null || isInTouchSlop(view, motionEvent)) {
            } else {
                this.mLongClickTask.stop(this);
            }
        }
    }

    public final void onEventUp(AnimConfig... animConfigArr) {
        if (this.mIsDown) {
            if (LogUtils.isLogEnabled()) {
                LogUtils.debug("onEventUp, touchUp", new Object[0]);
            }
            touchUp(animConfigArr);
            resetTouchStatus();
        }
    }

    public final void resetTouchStatus() {
        LongClickTask longClickTask = this.mLongClickTask;
        if (longClickTask != null) {
            longClickTask.stop(this);
        }
        this.mIsDown = false;
        this.mTouchIndex = 0;
        this.mDownX = 0.0f;
        this.mDownY = 0.0f;
    }

    public final void recordDownEvent(MotionEvent motionEvent) {
        if (this.mClickListener == null && this.mLongClickListener == null) {
            return;
        }
        this.mTouchIndex = motionEvent.getActionIndex();
        this.mDownX = motionEvent.getRawX();
        this.mDownY = motionEvent.getRawY();
        this.mClickInvoked = false;
        this.mLongClickInvoked = false;
        startLongClickTask();
    }

    /* loaded from: classes3.dex */
    public static final class LongClickTask implements Runnable {
        public WeakReference<FolmeTouch> mTouchRef;

        public LongClickTask() {
        }

        public void start(FolmeTouch folmeTouch) {
            View mo2588getTargetObject;
            IAnimTarget target = folmeTouch.mState.getTarget();
            if (!(target instanceof ViewTarget) || (mo2588getTargetObject = ((ViewTarget) target).mo2588getTargetObject()) == null) {
                return;
            }
            this.mTouchRef = new WeakReference<>(folmeTouch);
            mo2588getTargetObject.postDelayed(this, ViewConfiguration.getLongPressTimeout());
        }

        public void stop(FolmeTouch folmeTouch) {
            View mo2588getTargetObject;
            IAnimTarget target = folmeTouch.mState.getTarget();
            if (!(target instanceof ViewTarget) || (mo2588getTargetObject = ((ViewTarget) target).mo2588getTargetObject()) == null) {
                return;
            }
            mo2588getTargetObject.removeCallbacks(this);
        }

        @Override // java.lang.Runnable
        public void run() {
            View view;
            FolmeTouch folmeTouch = this.mTouchRef.get();
            if (folmeTouch != null) {
                IAnimTarget target = folmeTouch.mState.getTarget();
                if (!(target instanceof ViewTarget) || (view = (View) target.mo2588getTargetObject()) == null || folmeTouch.mLongClickListener == null) {
                    return;
                }
                view.performLongClick();
                folmeTouch.invokeLongClick(view);
            }
        }
    }

    public final void startLongClickTask() {
        if (this.mLongClickListener == null) {
            return;
        }
        if (this.mLongClickTask == null) {
            this.mLongClickTask = new LongClickTask();
        }
        this.mLongClickTask.start(this);
    }

    public final void invokeLongClick(View view) {
        if (!this.mLongClickInvoked) {
            this.mLongClickInvoked = true;
            this.mLongClickListener.onLongClick(view);
        }
    }

    public final void handleClick(View view, MotionEvent motionEvent) {
        if (!this.mIsDown || this.mClickListener == null || this.mTouchIndex != motionEvent.getActionIndex()) {
            return;
        }
        IAnimTarget target = this.mState.getTarget();
        if (!(target instanceof ViewTarget) || !isInTouchSlop(view, motionEvent)) {
            return;
        }
        View mo2588getTargetObject = ((ViewTarget) target).mo2588getTargetObject();
        mo2588getTargetObject.performClick();
        invokeClick(mo2588getTargetObject);
    }

    public final void invokeClick(View view) {
        if (this.mClickInvoked || this.mLongClickInvoked) {
            return;
        }
        this.mClickInvoked = true;
        this.mClickListener.onClick(view);
    }

    public final boolean isInTouchSlop(View view, MotionEvent motionEvent) {
        return CommonUtils.getDistance(this.mDownX, this.mDownY, motionEvent.getRawX(), motionEvent.getRawY()) < ((double) CommonUtils.getTouchSlop(view));
    }

    public final void handleMotionEvent(View view, MotionEvent motionEvent, AnimConfig... animConfigArr) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            recordDownEvent(motionEvent);
            onEventDown(animConfigArr);
            return;
        }
        if (actionMasked == 1) {
            handleClick(view, motionEvent);
        } else if (actionMasked == 2) {
            onEventMove(motionEvent, view, animConfigArr);
            return;
        }
        onEventUp(animConfigArr);
    }

    @Override // miuix.animation.ITouchStyle
    public ITouchStyle setAlpha(float f, ITouchStyle.TouchType... touchTypeArr) {
        this.mState.getState(getType(touchTypeArr)).add(ViewProperty.ALPHA, f);
        return this;
    }

    @Override // miuix.animation.ITouchStyle
    public ITouchStyle setScale(float f, ITouchStyle.TouchType... touchTypeArr) {
        ITouchStyle.TouchType type = getType(touchTypeArr);
        this.mScaleSetMap.put(type, Boolean.TRUE);
        double d = f;
        this.mState.getState(type).add(ViewProperty.SCALE_X, d).add(ViewProperty.SCALE_Y, d);
        return this;
    }

    public final boolean isScaleSet(ITouchStyle.TouchType touchType) {
        return Boolean.TRUE.equals(this.mScaleSetMap.get(touchType));
    }

    public final ITouchStyle.TouchType getType(ITouchStyle.TouchType... touchTypeArr) {
        return touchTypeArr.length > 0 ? touchTypeArr[0] : ITouchStyle.TouchType.DOWN;
    }

    @Override // miuix.animation.ITouchStyle
    public ITouchStyle setTint(int i) {
        boolean z = true;
        this.mSetTint = true;
        if (i != 0) {
            z = false;
        }
        this.mClearTint = z;
        this.mState.getState(ITouchStyle.TouchType.DOWN).add(ViewPropertyExt.FOREGROUND, i);
        return this;
    }

    @Override // miuix.animation.ITouchStyle
    public ITouchStyle setTint(float f, float f2, float f3, float f4) {
        return setTint(Color.argb((int) (f * 255.0f), (int) (f2 * 255.0f), (int) (f3 * 255.0f), (int) (f4 * 255.0f)));
    }

    public ITouchStyle setBackgroundColor(int i) {
        ViewPropertyExt.BackgroundProperty backgroundProperty = ViewPropertyExt.BACKGROUND;
        this.mState.getState(ITouchStyle.TouchType.DOWN).add(backgroundProperty, i);
        this.mState.getState(ITouchStyle.TouchType.UP).add(backgroundProperty, (int) AnimValueUtils.getValueOfTarget(this.mState.getTarget(), backgroundProperty, SearchStatUtils.POW));
        return this;
    }

    @Override // miuix.animation.ITouchStyle
    public ITouchStyle setBackgroundColor(float f, float f2, float f3, float f4) {
        return setBackgroundColor(Color.argb((int) (f * 255.0f), (int) (f2 * 255.0f), (int) (f3 * 255.0f), (int) (f4 * 255.0f)));
    }

    @Override // miuix.animation.ITouchStyle
    public void touchDown(AnimConfig... animConfigArr) {
        setCorner(0.0f);
        setTintColor();
        AnimConfig[] downConfig = getDownConfig(animConfigArr);
        FolmeFont folmeFont = this.mFontStyle;
        if (folmeFont != null) {
            folmeFont.to(this.mDownWeight, downConfig);
        }
        IFolmeStateStyle iFolmeStateStyle = this.mState;
        ITouchStyle.TouchType touchType = ITouchStyle.TouchType.DOWN;
        AnimState state = iFolmeStateStyle.getState(touchType);
        if (!isScaleSet(touchType)) {
            IAnimTarget target = this.mState.getTarget();
            float max = Math.max(target.getValue(ViewProperty.WIDTH), target.getValue(ViewProperty.HEIGHT));
            double max2 = Math.max((max - this.mScaleDist) / max, 0.9f);
            state.add(ViewProperty.SCALE_X, max2).add(ViewProperty.SCALE_Y, max2);
        }
        this.mState.to(state, downConfig);
    }

    @Override // miuix.animation.ITouchStyle
    public void touchUp(AnimConfig... animConfigArr) {
        AnimConfig[] upConfig = getUpConfig(animConfigArr);
        FolmeFont folmeFont = this.mFontStyle;
        if (folmeFont != null) {
            folmeFont.to(this.mUpWeight, upConfig);
        }
        IFolmeStateStyle iFolmeStateStyle = this.mState;
        iFolmeStateStyle.to(iFolmeStateStyle.getState(ITouchStyle.TouchType.UP), upConfig);
    }

    @Override // miuix.animation.ITouchStyle
    public void setTouchUp() {
        this.mState.setTo(ITouchStyle.TouchType.UP);
    }

    public final AnimConfig[] getDownConfig(AnimConfig... animConfigArr) {
        return (AnimConfig[]) CommonUtils.mergeArray(animConfigArr, this.mDownConfig);
    }

    public final AnimConfig[] getUpConfig(AnimConfig... animConfigArr) {
        return (AnimConfig[]) CommonUtils.mergeArray(animConfigArr, this.mUpConfig);
    }

    public final void setCorner(float f) {
        Object mo2588getTargetObject = this.mState.getTarget().mo2588getTargetObject();
        if (mo2588getTargetObject instanceof View) {
            ((View) mo2588getTargetObject).setTag(miuix.animation.R$id.miuix_animation_tag_view_corner, Float.valueOf(f));
        }
    }
}
