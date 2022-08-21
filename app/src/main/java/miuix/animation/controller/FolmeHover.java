package miuix.animation.controller;

import android.graphics.Color;
import android.util.ArrayMap;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.miui.gallery.search.statistics.SearchStatUtils;
import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Map;
import java.util.WeakHashMap;
import miuix.animation.Folme;
import miuix.animation.IAnimTarget;
import miuix.animation.IHoverStyle;
import miuix.animation.R$id;
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

/* loaded from: classes3.dex */
public class FolmeHover extends FolmeBase implements IHoverStyle {
    public static WeakHashMap<View, InnerViewHoverListener> sHoverRecord = new WeakHashMap<>();
    public String HoverMoveType;
    public boolean isSetAutoTranslation;
    public WeakReference<View> mChildView;
    public boolean mClearTint;
    public IHoverStyle.HoverEffect mCurrentEffect;
    public TransitionListener mDefListener;
    public AnimConfig mEnterConfig;
    public AnimConfig mExitConfig;
    public WeakReference<View> mHoverView;
    public boolean mIsEnter;
    public int[] mLocation;
    public AnimConfig mMoveConfig;
    public WeakReference<View> mParentView;
    public float mRadius;
    public Map<IHoverStyle.HoverType, Boolean> mScaleSetMap;
    public boolean mSetTint;
    public int mTargetHeight;
    public int mTargetWidth;
    public float mTranslateDist;
    public Map<IHoverStyle.HoverType, Boolean> mTranslationSetMap;

    public final void clearRound() {
    }

    public final float perFromVal(float f, float f2, float f3) {
        return (f - f2) / (f3 - f2);
    }

    public final void setAutoRound() {
    }

    public final float valFromPer(float f, float f2, float f3) {
        return f2 + ((f3 - f2) * f);
    }

    public FolmeHover(IAnimTarget... iAnimTargetArr) {
        super(iAnimTargetArr);
        this.mTranslateDist = Float.MAX_VALUE;
        this.mMoveConfig = new AnimConfig().setEase(EaseManager.getStyle(-2, 0.9f, 0.4f));
        this.mEnterConfig = new AnimConfig();
        this.mExitConfig = new AnimConfig();
        this.mScaleSetMap = new ArrayMap();
        this.mTranslationSetMap = new ArrayMap();
        this.mCurrentEffect = IHoverStyle.HoverEffect.NORMAL;
        this.isSetAutoTranslation = false;
        this.mClearTint = false;
        this.mLocation = new int[2];
        this.mRadius = 0.0f;
        this.mTargetWidth = 0;
        this.mTargetHeight = 0;
        this.HoverMoveType = "MOVE";
        this.mDefListener = new TransitionListener() { // from class: miuix.animation.controller.FolmeHover.1
            @Override // miuix.animation.listener.TransitionListener
            public void onBegin(Object obj, Collection<UpdateInfo> collection) {
                if (obj.equals(IHoverStyle.HoverType.ENTER)) {
                    AnimState.alignState(FolmeHover.this.mState.getState(IHoverStyle.HoverType.EXIT), collection);
                }
            }
        };
        initDist(iAnimTargetArr.length > 0 ? iAnimTargetArr[0] : null);
        updateHoverState(this.mCurrentEffect);
        this.mEnterConfig.setEase(EaseManager.getStyle(-2, 0.99f, 0.6f));
        this.mEnterConfig.addListeners(this.mDefListener);
        this.mExitConfig.setEase(-2, 0.99f, 0.4f).setSpecial(ViewProperty.ALPHA, -2L, 0.9f, 0.2f);
    }

    public final void setAutoScale() {
        Map<IHoverStyle.HoverType, Boolean> map = this.mScaleSetMap;
        IHoverStyle.HoverType hoverType = IHoverStyle.HoverType.ENTER;
        Boolean bool = Boolean.TRUE;
        map.put(hoverType, bool);
        Map<IHoverStyle.HoverType, Boolean> map2 = this.mScaleSetMap;
        IHoverStyle.HoverType hoverType2 = IHoverStyle.HoverType.EXIT;
        map2.put(hoverType2, bool);
        this.mState.getState(hoverType2).add(ViewProperty.SCALE_X, 1.0d).add(ViewProperty.SCALE_Y, 1.0d);
    }

    public final void setAutoTranslation() {
        this.isSetAutoTranslation = true;
        Map<IHoverStyle.HoverType, Boolean> map = this.mTranslationSetMap;
        IHoverStyle.HoverType hoverType = IHoverStyle.HoverType.ENTER;
        Boolean bool = Boolean.TRUE;
        map.put(hoverType, bool);
        Map<IHoverStyle.HoverType, Boolean> map2 = this.mTranslationSetMap;
        IHoverStyle.HoverType hoverType2 = IHoverStyle.HoverType.EXIT;
        map2.put(hoverType2, bool);
        this.mState.getState(hoverType2).add(ViewProperty.TRANSLATION_X, SearchStatUtils.POW).add(ViewProperty.TRANSLATION_Y, SearchStatUtils.POW);
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
        this.mState.getState(IHoverStyle.HoverType.ENTER).add(foregroundProperty, argb);
        this.mState.getState(IHoverStyle.HoverType.EXIT).add(foregroundProperty, SearchStatUtils.POW);
    }

    public IHoverStyle setCorner(float f) {
        this.mRadius = f;
        Object mo2588getTargetObject = this.mState.getTarget().mo2588getTargetObject();
        if (mo2588getTargetObject instanceof View) {
            ((View) mo2588getTargetObject).setTag(R$id.miuix_animation_tag_view_corner, Float.valueOf(f));
        }
        return this;
    }

    public IHoverStyle setTint(int i) {
        boolean z = true;
        this.mSetTint = true;
        if (i != 0) {
            z = false;
        }
        this.mClearTint = z;
        this.mState.getState(IHoverStyle.HoverType.ENTER).add(ViewPropertyExt.FOREGROUND, i);
        return this;
    }

    @Override // miuix.animation.IHoverStyle
    public IHoverStyle setTint(float f, float f2, float f3, float f4) {
        return setTint(Color.argb((int) (f * 255.0f), (int) (f2 * 255.0f), (int) (f3 * 255.0f), (int) (f4 * 255.0f)));
    }

    @Override // miuix.animation.IHoverStyle
    public IHoverStyle setBackgroundColor(float f, float f2, float f3, float f4) {
        return setBackgroundColor(Color.argb((int) (f * 255.0f), (int) (f2 * 255.0f), (int) (f3 * 255.0f), (int) (f4 * 255.0f)));
    }

    public IHoverStyle setBackgroundColor(int i) {
        ViewPropertyExt.BackgroundProperty backgroundProperty = ViewPropertyExt.BACKGROUND;
        this.mState.getState(IHoverStyle.HoverType.ENTER).add(backgroundProperty, i);
        this.mState.getState(IHoverStyle.HoverType.EXIT).add(backgroundProperty, (int) AnimValueUtils.getValueOfTarget(this.mState.getTarget(), backgroundProperty, SearchStatUtils.POW));
        return this;
    }

    public final void clearScale() {
        IHoverStyle.HoverType hoverType = IHoverStyle.HoverType.ENTER;
        if (isScaleSet(hoverType)) {
            this.mState.getState(hoverType).remove(ViewProperty.SCALE_X);
            this.mState.getState(hoverType).remove(ViewProperty.SCALE_Y);
        }
        IHoverStyle.HoverType hoverType2 = IHoverStyle.HoverType.EXIT;
        if (isScaleSet(hoverType2)) {
            this.mState.getState(hoverType2).remove(ViewProperty.SCALE_X);
            this.mState.getState(hoverType2).remove(ViewProperty.SCALE_Y);
        }
        this.mScaleSetMap.clear();
    }

    public final void clearTranslation() {
        this.isSetAutoTranslation = false;
        IHoverStyle.HoverType hoverType = IHoverStyle.HoverType.ENTER;
        if (isTranslationSet(hoverType)) {
            this.mState.getState(hoverType).remove(ViewProperty.TRANSLATION_X);
            this.mState.getState(hoverType).remove(ViewProperty.TRANSLATION_Y);
        }
        IHoverStyle.HoverType hoverType2 = IHoverStyle.HoverType.EXIT;
        if (isTranslationSet(hoverType2)) {
            this.mState.getState(hoverType2).remove(ViewProperty.TRANSLATION_X);
            this.mState.getState(hoverType2).remove(ViewProperty.TRANSLATION_Y);
        }
        this.mTranslationSetMap.clear();
    }

    public IHoverStyle clearTintColor() {
        this.mClearTint = true;
        ViewPropertyExt.ForegroundProperty foregroundProperty = ViewPropertyExt.FOREGROUND;
        this.mState.getState(IHoverStyle.HoverType.ENTER).remove(foregroundProperty);
        this.mState.getState(IHoverStyle.HoverType.EXIT).remove(foregroundProperty);
        return this;
    }

    @Override // miuix.animation.IHoverStyle
    public IHoverStyle setEffect(IHoverStyle.HoverEffect hoverEffect) {
        updateHoverState(hoverEffect);
        return this;
    }

    @Override // miuix.animation.IHoverStyle
    public void handleHoverOf(View view, AnimConfig... animConfigArr) {
        doHandleHoverOf(view, animConfigArr);
    }

    public final void doHandleHoverOf(View view, AnimConfig... animConfigArr) {
        handleViewHover(view, animConfigArr);
        if (!setHoverView(view) || !LogUtils.isLogEnabled()) {
            return;
        }
        LogUtils.debug("handleViewHover for " + view, new Object[0]);
    }

    /* renamed from: miuix.animation.controller.FolmeHover$2  reason: invalid class name */
    /* loaded from: classes3.dex */
    public static /* synthetic */ class AnonymousClass2 {
        public static final /* synthetic */ int[] $SwitchMap$miuix$animation$IHoverStyle$HoverEffect;

        static {
            int[] iArr = new int[IHoverStyle.HoverEffect.values().length];
            $SwitchMap$miuix$animation$IHoverStyle$HoverEffect = iArr;
            try {
                iArr[IHoverStyle.HoverEffect.NORMAL.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$miuix$animation$IHoverStyle$HoverEffect[IHoverStyle.HoverEffect.FLOATED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$miuix$animation$IHoverStyle$HoverEffect[IHoverStyle.HoverEffect.FLOATED_WRAPPED.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public final void updateHoverState(IHoverStyle.HoverEffect hoverEffect) {
        int i = AnonymousClass2.$SwitchMap$miuix$animation$IHoverStyle$HoverEffect[hoverEffect.ordinal()];
        if (i == 1) {
            IHoverStyle.HoverEffect hoverEffect2 = this.mCurrentEffect;
            if (hoverEffect2 == IHoverStyle.HoverEffect.FLOATED) {
                clearScale();
                clearTranslation();
            } else if (hoverEffect2 == IHoverStyle.HoverEffect.FLOATED_WRAPPED) {
                clearScale();
                clearTranslation();
                clearRound();
            }
            setTintColor();
            this.mCurrentEffect = hoverEffect;
        } else if (i == 2) {
            if (this.mCurrentEffect == IHoverStyle.HoverEffect.FLOATED_WRAPPED) {
                clearRound();
            }
            setTintColor();
            setAutoScale();
            setAutoTranslation();
            this.mCurrentEffect = hoverEffect;
        } else if (i != 3) {
        } else {
            IHoverStyle.HoverEffect hoverEffect3 = this.mCurrentEffect;
            if (hoverEffect3 == IHoverStyle.HoverEffect.NORMAL || hoverEffect3 == IHoverStyle.HoverEffect.FLOATED) {
                clearTintColor();
            }
            setAutoScale();
            setAutoTranslation();
            setAutoRound();
            this.mCurrentEffect = hoverEffect;
        }
    }

    public final boolean setHoverView(View view) {
        WeakReference<View> weakReference = this.mHoverView;
        if ((weakReference != null ? weakReference.get() : null) == view) {
            return false;
        }
        this.mHoverView = new WeakReference<>(view);
        return true;
    }

    public final void handleViewHover(View view, AnimConfig... animConfigArr) {
        InnerViewHoverListener innerViewHoverListener = sHoverRecord.get(view);
        if (innerViewHoverListener == null) {
            innerViewHoverListener = new InnerViewHoverListener();
            sHoverRecord.put(view, innerViewHoverListener);
        }
        view.setOnHoverListener(innerViewHoverListener);
        innerViewHoverListener.addHover(this, animConfigArr);
    }

    /* loaded from: classes3.dex */
    public static class InnerViewHoverListener implements View.OnHoverListener {
        public WeakHashMap<FolmeHover, AnimConfig[]> mHoverMap;

        public InnerViewHoverListener() {
            this.mHoverMap = new WeakHashMap<>();
        }

        public void addHover(FolmeHover folmeHover, AnimConfig... animConfigArr) {
            this.mHoverMap.put(folmeHover, animConfigArr);
        }

        @Override // android.view.View.OnHoverListener
        public boolean onHover(View view, MotionEvent motionEvent) {
            for (Map.Entry<FolmeHover, AnimConfig[]> entry : this.mHoverMap.entrySet()) {
                entry.getKey().handleMotionEvent(view, motionEvent, entry.getValue());
            }
            return false;
        }
    }

    public final void handleMotionEvent(View view, MotionEvent motionEvent, AnimConfig... animConfigArr) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 7) {
            onEventMove(view, motionEvent, animConfigArr);
        } else if (actionMasked == 9) {
            onEventEnter(motionEvent, animConfigArr);
        } else if (actionMasked != 10) {
        } else {
            onEventExit(motionEvent, animConfigArr);
        }
    }

    public final void onEventEnter(MotionEvent motionEvent, AnimConfig... animConfigArr) {
        if (LogUtils.isLogEnabled()) {
            LogUtils.debug("onEventEnter, touchEnter", new Object[0]);
        }
        hoverEnter(motionEvent, animConfigArr);
    }

    public final void onEventMove(View view, MotionEvent motionEvent, AnimConfig... animConfigArr) {
        if (!this.mIsEnter || view == null || !isTranslationSet(IHoverStyle.HoverType.ENTER) || !this.isSetAutoTranslation) {
            return;
        }
        actualTranslatDist(view, motionEvent);
    }

    public final void onEventExit(MotionEvent motionEvent, AnimConfig... animConfigArr) {
        if (this.mIsEnter) {
            if (LogUtils.isLogEnabled()) {
                LogUtils.debug("onEventExit, touchExit", new Object[0]);
            }
            hoverExit(motionEvent, animConfigArr);
            resetTouchStatus();
        }
    }

    public final void resetTouchStatus() {
        this.mIsEnter = false;
    }

    public static boolean isOnHoverView(View view, int[] iArr, MotionEvent motionEvent) {
        if (view != null) {
            view.getLocationOnScreen(iArr);
            int rawX = (int) motionEvent.getRawX();
            int rawY = (int) motionEvent.getRawY();
            return rawX >= iArr[0] && rawX <= iArr[0] + view.getWidth() && rawY >= iArr[1] && rawY <= iArr[1] + view.getHeight();
        }
        return true;
    }

    @Override // miuix.animation.IHoverStyle
    public void hoverEnter(AnimConfig... animConfigArr) {
        hoverEnterAuto(true, animConfigArr);
    }

    public final void hoverEnterAuto(boolean z, AnimConfig... animConfigArr) {
        this.isSetAutoTranslation = z;
        this.mIsEnter = true;
        if (this.mCurrentEffect == IHoverStyle.HoverEffect.FLOATED_WRAPPED) {
            WeakReference<View> weakReference = this.mHoverView;
            View view = weakReference != null ? weakReference.get() : null;
            if (view != null) {
                setMagicView(view, true);
                setWrapped(view, true);
            }
        }
        if (isHideHover()) {
            setMagicView(true);
            setPointerHide(true);
        }
        setCorner(this.mRadius);
        setTintColor();
        AnimConfig[] enterConfig = getEnterConfig(animConfigArr);
        IFolmeStateStyle iFolmeStateStyle = this.mState;
        IHoverStyle.HoverType hoverType = IHoverStyle.HoverType.ENTER;
        AnimState state = iFolmeStateStyle.getState(hoverType);
        if (isScaleSet(hoverType)) {
            IAnimTarget target = this.mState.getTarget();
            float max = Math.max(target.getValue(ViewProperty.WIDTH), target.getValue(ViewProperty.HEIGHT));
            double min = Math.min((12.0f + max) / max, 1.15f);
            state.add(ViewProperty.SCALE_X, min).add(ViewProperty.SCALE_Y, min);
        }
        WeakReference<View> weakReference2 = this.mParentView;
        if (weakReference2 != null) {
            Folme.useAt(weakReference2.get()).state().add(ViewProperty.SCALE_X, 1.0f).add(ViewProperty.SCALE_Y, 1.0f).to(enterConfig);
        }
        this.mState.to(state, enterConfig);
    }

    public final void hoverEnterToolType(int i, AnimConfig... animConfigArr) {
        if (i == 1 || i == 3 || i == 0) {
            hoverEnter(animConfigArr);
        } else if (i != 4 && i != 2) {
        } else {
            hoverEnterAuto(false, animConfigArr);
        }
    }

    public void hoverEnter(MotionEvent motionEvent, AnimConfig... animConfigArr) {
        hoverEnterToolType(motionEvent.getToolType(0), animConfigArr);
    }

    public void hoverExit(MotionEvent motionEvent, AnimConfig... animConfigArr) {
        if (this.mParentView != null && !isOnHoverView(this.mHoverView.get(), this.mLocation, motionEvent)) {
            Folme.useAt(this.mParentView.get()).hover().hoverEnter(this.mEnterConfig);
        }
        IHoverStyle.HoverType hoverType = IHoverStyle.HoverType.EXIT;
        if (isTranslationSet(hoverType) && this.isSetAutoTranslation) {
            this.mState.getState(hoverType).add(ViewProperty.TRANSLATION_X, SearchStatUtils.POW).add(ViewProperty.TRANSLATION_Y, SearchStatUtils.POW);
        }
        hoverExit(animConfigArr);
    }

    @Override // miuix.animation.IHoverStyle
    public void hoverExit(AnimConfig... animConfigArr) {
        AnimConfig[] exitConfig = getExitConfig(animConfigArr);
        IFolmeStateStyle iFolmeStateStyle = this.mState;
        iFolmeStateStyle.to(iFolmeStateStyle.getState(IHoverStyle.HoverType.EXIT), exitConfig);
    }

    public void setMagicView(boolean z) {
        Object mo2588getTargetObject = this.mState.getTarget().mo2588getTargetObject();
        if (mo2588getTargetObject instanceof View) {
            setMagicView((View) mo2588getTargetObject, z);
        }
    }

    public void setPointerHide(boolean z) {
        Object mo2588getTargetObject = this.mState.getTarget().mo2588getTargetObject();
        if (mo2588getTargetObject instanceof View) {
            setPointerHide((View) mo2588getTargetObject, z);
        }
    }

    @Override // miuix.animation.IHoverStyle
    public void setFeedbackRadius(float f) {
        Object mo2588getTargetObject = this.mState.getTarget().mo2588getTargetObject();
        if (mo2588getTargetObject instanceof View) {
            setFeedbackRadius((View) mo2588getTargetObject, f);
        }
    }

    public final boolean isScaleSet(IHoverStyle.HoverType hoverType) {
        return Boolean.TRUE.equals(this.mScaleSetMap.get(hoverType));
    }

    public final boolean isTranslationSet(IHoverStyle.HoverType hoverType) {
        return Boolean.TRUE.equals(this.mTranslationSetMap.get(hoverType));
    }

    @Override // miuix.animation.controller.FolmeBase, miuix.animation.IStateContainer
    public void clean() {
        super.clean();
        this.mScaleSetMap.clear();
        WeakReference<View> weakReference = this.mHoverView;
        if (weakReference != null) {
            resetView(weakReference);
            this.mHoverView = null;
        }
        WeakReference<View> weakReference2 = this.mChildView;
        if (weakReference2 != null) {
            resetView(weakReference2);
            this.mChildView = null;
        }
        WeakReference<View> weakReference3 = this.mParentView;
        if (weakReference3 != null) {
            resetView(weakReference3);
            this.mParentView = null;
        }
    }

    public final void initDist(IAnimTarget iAnimTarget) {
        View mo2588getTargetObject = iAnimTarget instanceof ViewTarget ? ((ViewTarget) iAnimTarget).mo2588getTargetObject() : null;
        if (mo2588getTargetObject != null) {
            float max = Math.max(iAnimTarget.getValue(ViewProperty.WIDTH), iAnimTarget.getValue(ViewProperty.HEIGHT));
            float min = Math.min((12.0f + max) / max, 1.15f);
            this.mTargetWidth = mo2588getTargetObject.getWidth();
            int height = mo2588getTargetObject.getHeight();
            this.mTargetHeight = height;
            float f = 0.0f;
            float min2 = Math.min(15.0f, valFromPer(Math.max(0.0f, Math.min(1.0f, perFromVal(this.mTargetWidth - 40, 0.0f, 360.0f))), 15.0f, 0.0f));
            float min3 = Math.min(15.0f, valFromPer(Math.max(0.0f, Math.min(1.0f, perFromVal(height - 40, 0.0f, 360.0f))), 15.0f, 0.0f));
            if (min != 1.0f) {
                f = Math.min(min2, min3);
            }
            this.mTranslateDist = f;
            int i = this.mTargetWidth;
            int i2 = this.mTargetHeight;
            if (i == i2 && i < 100 && i2 < 100) {
                setCorner((int) (i * 0.5f));
            } else {
                setCorner(36.0f);
            }
        }
    }

    public final void actualTranslatDist(View view, MotionEvent motionEvent) {
        float rawX = motionEvent.getRawX();
        float rawY = motionEvent.getRawY();
        view.getLocationOnScreen(this.mLocation);
        float width = this.mLocation[0] + (view.getWidth() * 0.5f);
        float height = (rawY - (this.mLocation[1] + (view.getHeight() * 0.5f))) / view.getHeight();
        float f = 1.0f;
        float max = Math.max(-1.0f, Math.min(1.0f, (rawX - width) / view.getWidth()));
        float max2 = Math.max(-1.0f, Math.min(1.0f, height));
        float f2 = this.mTranslateDist;
        float f3 = max * (f2 == Float.MAX_VALUE ? 1.0f : f2);
        if (f2 != Float.MAX_VALUE) {
            f = f2;
        }
        this.mState.to(this.mState.getState(this.HoverMoveType).add(ViewProperty.TRANSLATION_X, f3).add(ViewProperty.TRANSLATION_Y, max2 * f), this.mMoveConfig);
    }

    public final View resetView(WeakReference<View> weakReference) {
        View view = weakReference.get();
        if (view != null) {
            view.setOnHoverListener(null);
        }
        return view;
    }

    public final AnimConfig[] getEnterConfig(AnimConfig... animConfigArr) {
        return (AnimConfig[]) CommonUtils.mergeArray(animConfigArr, this.mEnterConfig);
    }

    public final AnimConfig[] getExitConfig(AnimConfig... animConfigArr) {
        return (AnimConfig[]) CommonUtils.mergeArray(animConfigArr, this.mExitConfig);
    }

    public boolean isHideHover() {
        boolean z;
        IHoverStyle.HoverEffect hoverEffect;
        return this.mTargetWidth < 100 && this.mTargetHeight < 100 && (!(z = this.isSetAutoTranslation) || (z && ((hoverEffect = this.mCurrentEffect) == IHoverStyle.HoverEffect.FLOATED || hoverEffect == IHoverStyle.HoverEffect.FLOATED_WRAPPED)));
    }

    public static void setMagicView(View view, boolean z) {
        try {
            Class.forName("android.view.View").getMethod("setMagicView", Boolean.TYPE).invoke(view, Boolean.valueOf(z));
        } catch (Exception e) {
            Log.e("", "setMagicView failed , e:" + e.toString());
        }
    }

    public static void setWrapped(View view, boolean z) {
        try {
            Class.forName("android.view.View").getMethod("setWrapped", Boolean.TYPE).invoke(view, Boolean.valueOf(z));
        } catch (Exception e) {
            Log.e("", "setWrapped failed , e:" + e.toString());
        }
    }

    public static void setPointerHide(View view, boolean z) {
        try {
            Class.forName("android.view.View").getMethod("setPointerHide", Boolean.TYPE).invoke(view, Boolean.valueOf(z));
        } catch (Exception e) {
            Log.e("", "setPointerHide failed , e:" + e.toString());
        }
    }

    public static void setFeedbackRadius(View view, float f) {
        try {
            Class.forName("android.view.View").getMethod("setFeedbackRadius", Float.TYPE).invoke(view, Float.valueOf(f));
        } catch (Exception e) {
            Log.e("", "setFeedbackRadius failed , e:" + e.toString());
        }
    }
}
