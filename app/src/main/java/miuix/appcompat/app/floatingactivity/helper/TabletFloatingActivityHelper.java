package miuix.appcompat.app.floatingactivity.helper;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import java.lang.ref.WeakReference;
import miuix.animation.Folme;
import miuix.animation.IStateStyle;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.TransitionListener;
import miuix.animation.property.ViewProperty;
import miuix.appcompat.R$color;
import miuix.appcompat.R$dimen;
import miuix.appcompat.R$id;
import miuix.appcompat.R$layout;
import miuix.appcompat.app.AppCompatActivity;
import miuix.appcompat.app.floatingactivity.FloatingSwitcherAnimHelper;
import miuix.appcompat.app.floatingactivity.OnFloatingActivityCallback;
import miuix.appcompat.app.floatingactivity.OnFloatingCallback;
import miuix.appcompat.app.floatingactivity.SplitUtils;
import miuix.appcompat.widget.dialoganim.DimAnimator;
import miuix.internal.util.AttributeResolver;
import miuix.internal.util.ViewUtils;
import miuix.internal.widget.RoundFrameLayout;
import miuix.view.CompatViewMethod;

/* loaded from: classes3.dex */
public abstract class TabletFloatingActivityHelper extends BaseFloatingActivityHelper {
    public AppCompatActivity mActivity;
    public View mBg;
    public Drawable mDefaultPanelBg;
    public ViewGroup.LayoutParams mFloatingLayoutParam;
    public float mFloatingRadius;
    public View mFloatingRoot;
    public View mHandle;
    public float mLastMoveY;
    public float mMoveMaxY;
    public float mOffsetY;
    public OnFloatingActivityCallback mOnFloatingActivityCallback;
    public OnFloatingCallback mOnFloatingCallback;
    public View mPanel;
    public View mPanelParent;
    public RoundFrameLayout mRoundFrameLayout;
    public float mTouchDownY;
    public final int PANEL_SHOW_DELAY_TIME = 90;
    public boolean mEnableSwipToDismiss = true;
    public final Handler mFloatingActivitySlidDownHandler = new Handler(Looper.getMainLooper());
    public boolean mAnimationDoing = false;
    public boolean mIsFloatingWindow = true;
    public int mFloatingActivityFinishingFlag = 0;

    public static /* synthetic */ void $r8$lambda$5memfReVSwK_eg8d29qpXxKNEfU(TabletFloatingActivityHelper tabletFloatingActivityHelper, View view) {
        tabletFloatingActivityHelper.lambda$init$1(view);
    }

    /* renamed from: $r8$lambda$JX0RSWqsqVonC-WonNk7CbW4IJ0 */
    public static /* synthetic */ boolean m2592$r8$lambda$JX0RSWqsqVonCWonNk7CbW4IJ0(TabletFloatingActivityHelper tabletFloatingActivityHelper, View view, MotionEvent motionEvent) {
        return tabletFloatingActivityHelper.lambda$init$2(view, motionEvent);
    }

    public static /* synthetic */ void $r8$lambda$NN9Rn0TXDLqthBJLVaVxCb1xqLU(TabletFloatingActivityHelper tabletFloatingActivityHelper, float f) {
        tabletFloatingActivityHelper.lambda$panelDelayShow$0(f);
    }

    public static /* synthetic */ void $r8$lambda$uWUnkxOXg6_5VzmS4Ip5dflvLOI(TabletFloatingActivityHelper tabletFloatingActivityHelper) {
        tabletFloatingActivityHelper.lambda$firstFloatingTranslationTop$3();
    }

    public TabletFloatingActivityHelper(AppCompatActivity appCompatActivity) {
        this.mActivity = appCompatActivity;
        this.mDefaultPanelBg = AttributeResolver.resolveDrawable(appCompatActivity, 16842836);
    }

    @Override // miuix.appcompat.app.floatingactivity.helper.BaseFloatingActivityHelper
    public void setFloatingWindowMode(boolean z) {
        this.mIsFloatingWindow = z;
        if (!SplitUtils.isIntentFromSettingsSplit(this.mActivity.getIntent())) {
            CompatViewMethod.setActivityTranslucent(this.mActivity, z);
        }
        if (this.mRoundFrameLayout != null) {
            float dimensionPixelSize = this.mActivity.getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_floating_window_background_radius);
            this.mFloatingRadius = dimensionPixelSize;
            RoundFrameLayout roundFrameLayout = this.mRoundFrameLayout;
            if (!z) {
                dimensionPixelSize = 0.0f;
            }
            roundFrameLayout.setRadius(dimensionPixelSize);
        }
        if (this.mPanel != null) {
            if (!z && ViewUtils.isNightMode(this.mActivity)) {
                this.mPanel.setBackground(new ColorDrawable(-16777216));
            } else {
                this.mPanel.setBackground(this.mDefaultPanelBg);
            }
        }
    }

    @Override // miuix.appcompat.app.floatingactivity.helper.BaseFloatingActivityHelper
    public ViewGroup replaceSubDecor(View view, boolean z) {
        ViewGroup viewGroup = (ViewGroup) View.inflate(this.mActivity, R$layout.miuix_appcompat_screen_floating_window, null);
        View findViewById = viewGroup.findViewById(R$id.action_bar_overlay_layout);
        View findViewById2 = viewGroup.findViewById(R$id.sliding_drawer_handle);
        if (findViewById2 != null && (findViewById2.getParent() instanceof ViewGroup)) {
            ((ViewGroup) findViewById2.getParent()).removeView(findViewById2);
        }
        if (view instanceof ViewGroup) {
            ((ViewGroup) view).addView(findViewById2);
        }
        ViewGroup.LayoutParams layoutParams = findViewById.getLayoutParams();
        this.mFloatingLayoutParam = layoutParams;
        if (!z) {
            layoutParams.width = -1;
            layoutParams.height = -1;
        } else {
            layoutParams.height = -2;
            layoutParams.width = -2;
        }
        view.setLayoutParams(layoutParams);
        viewGroup.removeView(findViewById);
        ViewParent parent = view.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(view);
        }
        this.mFloatingRadius = this.mActivity.getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_floating_window_background_radius);
        RoundFrameLayout roundFrameLayout = new RoundFrameLayout(this.mActivity);
        this.mRoundFrameLayout = roundFrameLayout;
        roundFrameLayout.setLayoutParams(this.mFloatingLayoutParam);
        this.mRoundFrameLayout.addView(view);
        this.mRoundFrameLayout.setRadius(this.mFloatingRadius);
        panelDelayShow();
        viewGroup.addView(this.mRoundFrameLayout);
        setPanelParent(this.mRoundFrameLayout);
        return viewGroup;
    }

    public final void panelDelayShow() {
        if (!this.mIsFloatingWindow) {
            return;
        }
        final float alpha = this.mRoundFrameLayout.getAlpha();
        this.mRoundFrameLayout.setAlpha(0.0f);
        this.mRoundFrameLayout.postDelayed(new Runnable() { // from class: miuix.appcompat.app.floatingactivity.helper.TabletFloatingActivityHelper$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                TabletFloatingActivityHelper.$r8$lambda$NN9Rn0TXDLqthBJLVaVxCb1xqLU(TabletFloatingActivityHelper.this, alpha);
            }
        }, 90L);
    }

    public /* synthetic */ void lambda$panelDelayShow$0(float f) {
        this.mRoundFrameLayout.setAlpha(f);
    }

    public final void setPanelParent(View view) {
        this.mPanelParent = view;
    }

    @Override // miuix.appcompat.app.floatingactivity.helper.BaseFloatingActivityHelper
    public void init(View view, boolean z) {
        this.mHandle = view.findViewById(R$id.sliding_drawer_handle);
        View findViewById = view.findViewById(R$id.action_bar_overlay_bg);
        this.mBg = findViewById;
        findViewById.setAlpha(0.3f);
        this.mPanel = view.findViewById(R$id.action_bar_overlay_layout);
        View findViewById2 = view.findViewById(R$id.action_bar_overlay_floating_root);
        this.mFloatingRoot = findViewById2;
        this.mIsFloatingWindow = z;
        this.mEnableSwipToDismiss = false;
        findViewById2.setOnTouchListener(new DisMisclickistener());
        this.mFloatingRoot.setOnClickListener(new View.OnClickListener() { // from class: miuix.appcompat.app.floatingactivity.helper.TabletFloatingActivityHelper$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                TabletFloatingActivityHelper.$r8$lambda$5memfReVSwK_eg8d29qpXxKNEfU(TabletFloatingActivityHelper.this, view2);
            }
        });
        this.mHandle.setOnTouchListener(new View.OnTouchListener() { // from class: miuix.appcompat.app.floatingactivity.helper.TabletFloatingActivityHelper$$ExternalSyntheticLambda1
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view2, MotionEvent motionEvent) {
                return TabletFloatingActivityHelper.m2592$r8$lambda$JX0RSWqsqVonCWonNk7CbW4IJ0(TabletFloatingActivityHelper.this, view2, motionEvent);
            }
        });
        firstFloatingTranslationTop();
        this.mActivity.getWindow().setBackgroundDrawableResource(R$color.miuix_appcompat_transparent);
        if (!this.mIsFloatingWindow && ViewUtils.isNightMode(this.mActivity)) {
            this.mPanel.setBackground(new ColorDrawable(-16777216));
        } else {
            this.mPanel.setBackground(this.mDefaultPanelBg);
        }
    }

    public /* synthetic */ void lambda$init$1(View view) {
        if (this.mEnableSwipToDismiss) {
            getSnapShotAndSetPanel();
            makeDownMoveMaxY();
            notifyPageHide();
            triggerFinishCallback(true, 2);
        }
    }

    public /* synthetic */ boolean lambda$init$2(View view, MotionEvent motionEvent) {
        if (this.mEnableSwipToDismiss) {
            handleFingerMove(motionEvent);
            return true;
        }
        return true;
    }

    public final void notifyPageHide() {
        OnFloatingCallback onFloatingCallback = this.mOnFloatingCallback;
        if (onFloatingCallback != null) {
            onFloatingCallback.onHideBehindPage();
        }
    }

    public final void triggerFinishCallback(boolean z, int i) {
        updateFloatingActivityFinishingFlag(i);
        boolean z2 = false;
        if (z) {
            OnFloatingActivityCallback onFloatingActivityCallback = this.mOnFloatingActivityCallback;
            if (onFloatingActivityCallback != null && onFloatingActivityCallback.onFinish(i)) {
                executeFolme(false, i);
                return;
            }
            OnFloatingCallback onFloatingCallback = this.mOnFloatingCallback;
            if (onFloatingCallback == null || !onFloatingCallback.onFinish(i)) {
                z2 = true;
            }
            executeFolme(z2, i);
            return;
        }
        executeFolme(false, i);
    }

    public final void executeFolme(boolean z, int i) {
        float f;
        String str;
        int i2;
        if (!this.mAnimationDoing || !z) {
            this.mAnimationDoing = true;
            if (z) {
                i2 = (int) this.mMoveMaxY;
                f = 0.0f;
                str = "dismiss";
            } else {
                f = 0.3f;
                str = "init";
                i2 = 0;
            }
            AnimConfig animConfig = FloatingSwitcherAnimHelper.getAnimConfig(1, null);
            animConfig.addListeners(new FloatingAnimTransitionListener(i));
            AnimState add = new AnimState(str).add(ViewProperty.TRANSLATION_Y, i2);
            AnimState add2 = new AnimState(str).add(ViewProperty.ALPHA, f);
            Folme.useAt(getAnimPanel()).state().to(add, animConfig);
            Folme.useAt(this.mBg).state().to(add2, new AnimConfig[0]);
        }
    }

    public final View getAnimPanel() {
        View view = this.mPanelParent;
        return view == null ? this.mPanel : view;
    }

    public final void notifyDragEnd() {
        OnFloatingCallback onFloatingCallback = this.mOnFloatingCallback;
        if (onFloatingCallback != null) {
            onFloatingCallback.onDragEnd();
        }
    }

    public final void onEnd(Object obj) {
        if (TextUtils.equals("dismiss", obj.toString())) {
            this.mActivity.realFinish();
        } else if (TextUtils.equals("init", obj.toString())) {
            notifyDragEnd();
        }
        this.mAnimationDoing = false;
    }

    public final void updateFloatingActivityFinishingFlag(int i) {
        this.mFloatingActivityFinishingFlag = i;
    }

    public final void getSnapShotAndSetPanel() {
        OnFloatingCallback onFloatingCallback = this.mOnFloatingCallback;
        if (onFloatingCallback == null || !this.mEnableSwipToDismiss) {
            return;
        }
        onFloatingCallback.getSnapShotAndSetPanel(this.mActivity);
    }

    public final void handleFingerMove(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            notifyDragStart();
            float rawY = motionEvent.getRawY();
            this.mTouchDownY = rawY;
            this.mLastMoveY = rawY;
            this.mOffsetY = 0.0f;
            makeDownMoveMaxY();
        } else if (action != 1) {
            if (action != 2) {
                return;
            }
            float rawY2 = motionEvent.getRawY();
            float f = this.mOffsetY + (rawY2 - this.mLastMoveY);
            this.mOffsetY = f;
            if (f >= 0.0f) {
                movePanel(f);
                dimBg(this.mOffsetY / this.mMoveMaxY);
            }
            this.mLastMoveY = rawY2;
        } else {
            boolean z = false;
            boolean z2 = motionEvent.getRawY() - this.mTouchDownY > ((float) this.mPanel.getHeight()) * 0.5f;
            updateFloatingActivityFinishingFlag(1);
            if (z2) {
                getSnapShotAndSetPanel();
                OnFloatingCallback onFloatingCallback = this.mOnFloatingCallback;
                if (onFloatingCallback == null || !onFloatingCallback.onFinish(1)) {
                    z = true;
                }
                executeFolme(z, 1);
                return;
            }
            executeFolme(false, 1);
        }
    }

    public final void firstFloatingTranslationTop() {
        this.mPanel.post(new Runnable() { // from class: miuix.appcompat.app.floatingactivity.helper.TabletFloatingActivityHelper$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                TabletFloatingActivityHelper.$r8$lambda$uWUnkxOXg6_5VzmS4Ip5dflvLOI(TabletFloatingActivityHelper.this);
            }
        });
    }

    public /* synthetic */ void lambda$firstFloatingTranslationTop$3() {
        if (isEnableFirstFloatingTranslationY()) {
            folmeShow();
        }
    }

    public final boolean isEnableFirstFloatingTranslationY() {
        return getPageCount() == 1 && this.mIsFloatingWindow;
    }

    public final int getPageCount() {
        OnFloatingCallback onFloatingCallback = this.mOnFloatingCallback;
        if (onFloatingCallback == null) {
            return 0;
        }
        return onFloatingCallback.getPageCount();
    }

    public final void singleFloatingSlipExit(boolean z, int i) {
        if (!z || this.mAnimationDoing) {
            return;
        }
        makeDownMoveMaxY();
        notifyPageHide();
        executeFolme(true, i);
    }

    public final void notifyDragStart() {
        OnFloatingCallback onFloatingCallback = this.mOnFloatingCallback;
        if (onFloatingCallback != null) {
            onFloatingCallback.onDragStart();
        }
    }

    public final void movePanel(float f) {
        getAnimPanel().setTranslationY(f);
    }

    public final void dimBg(float f) {
        this.mBg.setAlpha((1.0f - Math.max(0.0f, Math.min(f, 1.0f))) * 0.3f);
    }

    public final void folmeShow() {
        View animPanel = getAnimPanel();
        IStateStyle state = Folme.useAt(animPanel).state();
        ViewProperty viewProperty = ViewProperty.TRANSLATION_Y;
        state.setTo(viewProperty, Integer.valueOf(animPanel.getHeight() + ((this.mFloatingRoot.getHeight() - animPanel.getHeight()) / 2))).to(viewProperty, 0, FloatingSwitcherAnimHelper.getAnimConfig(1, null));
        DimAnimator.show(this.mBg);
    }

    @Override // miuix.appcompat.app.floatingactivity.helper.BaseFloatingActivityHelper
    public void hideFloatingDimBackground() {
        this.mBg.setVisibility(8);
    }

    @Override // miuix.appcompat.app.floatingactivity.helper.BaseFloatingActivityHelper
    public void hideFloatingBrightPanel() {
        this.mPanel.setVisibility(8);
    }

    @Override // miuix.appcompat.app.floatingactivity.helper.BaseFloatingActivityHelper
    public void showFloatingBrightPanel() {
        this.mPanel.setVisibility(0);
    }

    @Override // miuix.appcompat.app.floatingactivity.helper.BaseFloatingActivityHelper
    public void setEnableSwipToDismiss(boolean z) {
        this.mEnableSwipToDismiss = z;
        this.mHandle.setVisibility(z ? 0 : 8);
    }

    @Override // miuix.appcompat.app.floatingactivity.helper.BaseFloatingActivityHelper
    public View getFloatingBrightPanel() {
        return this.mPanel;
    }

    @Override // miuix.appcompat.app.floatingactivity.helper.BaseFloatingActivityHelper
    public boolean onBackPressed() {
        if (this.mIsFloatingWindow) {
            getSnapShotAndSetPanel();
        }
        backOneByOne(4);
        return true;
    }

    public final void backOneByOne(int i) {
        updateFloatingActivityFinishingFlag(i);
        if (!isEnableFirstFloatingTranslationY()) {
            this.mActivity.realFinish();
        } else if (this.mAnimationDoing) {
        } else {
            triggerBottomExit(i);
        }
    }

    public final void triggerBottomExit(int i) {
        makeDownMoveMaxY();
        notifyPageHide();
        executeFolme(true, i);
    }

    public final void makeDownMoveMaxY() {
        int[] iArr = new int[2];
        this.mPanel.getLocationInWindow(iArr);
        this.mMoveMaxY = this.mFloatingRoot.getHeight() - iArr[1];
    }

    @Override // miuix.appcompat.app.floatingactivity.helper.BaseFloatingActivityHelper
    public boolean delegateFinishFloatingActivityInternal() {
        if (this.mIsFloatingWindow) {
            getSnapShotAndSetPanel();
            this.mFloatingActivitySlidDownHandler.postDelayed(new FinishFloatingActivityDelegate(this, this.mActivity), 110L);
            return true;
        }
        OnFloatingCallback onFloatingCallback = this.mOnFloatingCallback;
        if (onFloatingCallback != null) {
            onFloatingCallback.getSnapShotAndSetPanel(null);
        }
        new FinishFloatingActivityDelegate(this, this.mActivity).delegatePadPhoneFinishFloatingActivity();
        return true;
    }

    @Override // miuix.appcompat.app.floatingactivity.helper.BaseFloatingActivityHelper
    public ViewGroup.LayoutParams getFloatingLayoutParam() {
        return this.mFloatingLayoutParam;
    }

    @Override // miuix.appcompat.app.floatingactivity.helper.BaseFloatingActivityHelper
    public void setOnFloatingWindowCallback(OnFloatingActivityCallback onFloatingActivityCallback) {
        this.mOnFloatingActivityCallback = onFloatingActivityCallback;
    }

    @Override // miuix.appcompat.app.floatingactivity.helper.BaseFloatingActivityHelper
    public void setOnFloatingCallback(OnFloatingCallback onFloatingCallback) {
        this.mOnFloatingCallback = onFloatingCallback;
    }

    @Override // miuix.appcompat.app.floatingactivity.helper.BaseFloatingActivityHelper
    public void exitFloatingActivityAll() {
        makeDownMoveMaxY();
        notifyPageHide();
        executeFolme(true, 0);
    }

    @Override // miuix.appcompat.app.floatingactivity.IActivitySwitcherAnimation
    public void executeOpenEnterAnimation() {
        if (this.mIsFloatingWindow) {
            FloatingSwitcherAnimHelper.executeOpenEnterAnimation(this.mPanel);
        }
    }

    @Override // miuix.appcompat.app.floatingactivity.IActivitySwitcherAnimation
    public void executeOpenExitAnimation() {
        if (this.mIsFloatingWindow) {
            FloatingSwitcherAnimHelper.executeOpenExitAnimation(this.mPanel);
        }
    }

    @Override // miuix.appcompat.app.floatingactivity.IActivitySwitcherAnimation
    public void executeCloseEnterAnimation() {
        if (this.mIsFloatingWindow) {
            FloatingSwitcherAnimHelper.executeCloseEnterAnimation(this.mPanel);
        }
    }

    @Override // miuix.appcompat.app.floatingactivity.IActivitySwitcherAnimation
    public void executeCloseExitAnimation() {
        if (this.mIsFloatingWindow) {
            FloatingSwitcherAnimHelper.executeCloseExitAnimation(this.mPanel);
        }
    }

    /* loaded from: classes3.dex */
    public static class FinishFloatingActivityDelegate implements Runnable {
        public WeakReference<AppCompatActivity> mActivity;
        public WeakReference<TabletFloatingActivityHelper> mRefs;

        public FinishFloatingActivityDelegate(TabletFloatingActivityHelper tabletFloatingActivityHelper, AppCompatActivity appCompatActivity) {
            this.mRefs = new WeakReference<>(tabletFloatingActivityHelper);
            this.mActivity = new WeakReference<>(appCompatActivity);
        }

        public final void delegatePadPhoneFinishFloatingActivity() {
            TabletFloatingActivityHelper tabletFloatingActivityHelper = this.mRefs.get();
            if (tabletFloatingActivityHelper != null) {
                tabletFloatingActivityHelper.updateFloatingActivityFinishingFlag(3);
            }
            AppCompatActivity appCompatActivity = this.mActivity.get();
            if (tabletFloatingActivityHelper != null) {
                activityExitActuator(appCompatActivity, tabletFloatingActivityHelper, true, 3);
            }
        }

        public final void activityExitActuator(AppCompatActivity appCompatActivity, TabletFloatingActivityHelper tabletFloatingActivityHelper, boolean z, int i) {
            if (tabletFloatingActivityHelper.isEnableFirstFloatingTranslationY()) {
                tabletFloatingActivityHelper.singleFloatingSlipExit(z, i);
            } else if (appCompatActivity == null) {
            } else {
                appCompatActivity.realFinish();
            }
        }

        @Override // java.lang.Runnable
        public void run() {
            delegatePadPhoneFinishFloatingActivity();
        }
    }

    /* loaded from: classes3.dex */
    public static class FloatingAnimTransitionListener extends TransitionListener {
        public WeakReference<TabletFloatingActivityHelper> mRefs;
        public int mType;

        public FloatingAnimTransitionListener(TabletFloatingActivityHelper tabletFloatingActivityHelper, int i) {
            this.mRefs = new WeakReference<>(tabletFloatingActivityHelper);
            this.mType = i;
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onComplete(Object obj) {
            super.onComplete(obj);
            WeakReference<TabletFloatingActivityHelper> weakReference = this.mRefs;
            TabletFloatingActivityHelper tabletFloatingActivityHelper = weakReference == null ? null : weakReference.get();
            if (tabletFloatingActivityHelper != null) {
                tabletFloatingActivityHelper.onEnd(obj);
            }
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onCancel(Object obj) {
            super.onCancel(obj);
            WeakReference<TabletFloatingActivityHelper> weakReference = this.mRefs;
            TabletFloatingActivityHelper tabletFloatingActivityHelper = weakReference == null ? null : weakReference.get();
            if (tabletFloatingActivityHelper != null) {
                tabletFloatingActivityHelper.onEnd(obj);
            }
        }
    }

    /* loaded from: classes3.dex */
    public static class DisMisclickistener implements View.OnTouchListener {
        public long mStartTime;
        public float mStartX;
        public float mStartY;

        public DisMisclickistener() {
        }

        @Override // android.view.View.OnTouchListener
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            if (action != 0) {
                if (action != 1) {
                    return false;
                }
                return Math.sqrt(Math.pow((double) (motionEvent.getX() - this.mStartX), 2.0d) + Math.pow((double) (motionEvent.getY() - this.mStartY), 2.0d)) > ((double) ViewConfiguration.get(view.getContext()).getScaledTouchSlop()) || System.currentTimeMillis() - this.mStartTime > ((long) ViewConfiguration.getLongPressTimeout());
            }
            this.mStartX = motionEvent.getX();
            this.mStartY = motionEvent.getY();
            this.mStartTime = System.currentTimeMillis();
            return false;
        }
    }
}
