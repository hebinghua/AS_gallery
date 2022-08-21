package miuix.springback.trigger;

import android.content.Context;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import miuix.animation.utils.VelocityMonitor;
import miuix.springback.R$id;
import miuix.springback.R$layout;
import miuix.springback.trigger.BaseTrigger;
import miuix.springback.view.SpringBackLayout;
import miuix.view.HapticCompat;
import miuix.view.HapticFeedbackConstants;

/* loaded from: classes3.dex */
public abstract class CustomTrigger extends BaseTrigger {
    public final ActionComplete mActionComplete;
    public int mActionIndex;
    public final ActionStart mActionStart;
    public final ActionTriggered mActionTriggered;
    public BaseTrigger.IndeterminateAction.OnActionCompleteListener mCompleteListener;
    public RelativeLayout mContainer;
    public Context mContext;
    public BaseTrigger.Action mCurrentAction;
    public TriggerState mCurrentState;
    public long mEnterTime;
    public final Idle mIdle;
    public FrameLayout mIndicatorContainer;
    public boolean mIsExitISimpleAction;
    public boolean mIsExitIndeterminateAction;
    public boolean mIsExitIndeterminateUpAction;
    public boolean mIsStartIndeterminate;
    public boolean mIsStartIndeterminateUp;
    public int mLastScrollDistance;
    public SpringBackLayout mLayout;
    public View.OnLayoutChangeListener mLayoutChangeListener;
    public LayoutInflater mLayoutInflater;
    public View mLoadingContainer;
    public OnIndeterminateActionDataListener mOnActionDataListener;
    public BaseTrigger.IndeterminateAction.OnIndeterminateActionViewListener mOnIndeterminateActionViewListener;
    public BaseTrigger.IndeterminateUpAction.OnIndeterminateUpActionViewListener mOnIndeterminateUpActionViewListener;
    public SpringBackLayout.OnScrollListener mOnScrollListener;
    public BaseTrigger.SimpleAction.OnSimpleActionViewListener mOnSimpleActionViewListener;
    public SpringBackLayout.OnSpringListener mOnSpringListener;
    public OnIndeterminateUpActionDataListener mOnUpActionDataListener;
    public int mScrollDistance;
    public int mScrollState;
    public boolean mScrollerFinished;
    public View mSimpleActionView;
    public final Tracking mTracking;
    public boolean mUpActionBegin;
    public BaseTrigger.IndeterminateUpAction.OnUpActionDataListener mUpActionDataListener;
    public View mUpContainer;
    public VelocityMonitor mVelocityMonitor;
    public float mVelocityY;
    public final WaitForIndeterminate mWaitForIndeterminate;

    /* loaded from: classes3.dex */
    public interface OnIndeterminateActionDataListener {
    }

    /* loaded from: classes3.dex */
    public interface OnIndeterminateUpActionDataListener {
    }

    public abstract void onSpringBackLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8);

    public abstract void onSpringBackScrolled(SpringBackLayout springBackLayout, int i, int i2, int i3);

    public CustomTrigger(Context context) {
        super(context);
        this.mVelocityY = 0.0f;
        this.mScrollerFinished = true;
        this.mUpActionBegin = false;
        this.mEnterTime = -1L;
        this.mActionIndex = -1;
        this.mIsExitIndeterminateAction = false;
        this.mIsExitIndeterminateUpAction = false;
        this.mIsExitISimpleAction = false;
        this.mIsStartIndeterminate = false;
        this.mIsStartIndeterminateUp = false;
        this.mLayoutChangeListener = new View.OnLayoutChangeListener() { // from class: miuix.springback.trigger.CustomTrigger.1
            @Override // android.view.View.OnLayoutChangeListener
            public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.getWidth(), 1073741824);
                int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(-view.getScrollY(), 0);
                CustomTrigger.this.mContainer.measure(makeMeasureSpec, makeMeasureSpec2);
                if (CustomTrigger.this.getIndeterminateUpView() != null) {
                    CustomTrigger.this.getIndeterminateUpView().measure(makeMeasureSpec, makeMeasureSpec2);
                }
                if (CustomTrigger.this.getIndeterminateView() != null) {
                    CustomTrigger.this.getIndeterminateView().measure(makeMeasureSpec, makeMeasureSpec2);
                }
                CustomTrigger.this.mContainer.layout(0, view.getScrollY(), view.getWidth(), 0);
                if (CustomTrigger.this.getIndeterminateUpView() != null) {
                    CustomTrigger.this.getIndeterminateUpView().layout(0, CustomTrigger.this.mLayout.getBottom(), view.getWidth(), CustomTrigger.this.mLayout.getBottom() + view.getScrollY());
                }
                if (CustomTrigger.this.getIndeterminateView() != null) {
                    CustomTrigger.this.getIndeterminateView().layout(0, view.getScrollY(), view.getWidth(), 0);
                }
                CustomTrigger.this.onSpringBackLayoutChange(view, i, i2, i3, i4, i5, i6, i7, i8);
            }
        };
        this.mOnSpringListener = new SpringBackLayout.OnSpringListener() { // from class: miuix.springback.trigger.CustomTrigger.2
            @Override // miuix.springback.view.SpringBackLayout.OnSpringListener
            public boolean onSpringBack() {
                return CustomTrigger.this.mCurrentState.handleSpringBack();
            }
        };
        this.mOnScrollListener = new SpringBackLayout.OnScrollListener() { // from class: miuix.springback.trigger.CustomTrigger.3
            @Override // miuix.springback.view.SpringBackLayout.OnScrollListener
            public void onStateChanged(int i, int i2, boolean z) {
                CustomTrigger.this.mScrollState = i2;
                CustomTrigger.this.mScrollerFinished = z;
                CustomTrigger.this.mCurrentState.handleScrollStateChange(i, i2);
                TriggerState triggerState = CustomTrigger.this.mCurrentState;
                CustomTrigger customTrigger = CustomTrigger.this;
                if (triggerState != customTrigger.mIdle) {
                    customTrigger.mContainer.setVisibility(0);
                    if (CustomTrigger.this.getIndeterminateUpView() == null) {
                        return;
                    }
                    CustomTrigger.this.getIndeterminateUpView().setVisibility(0);
                }
            }

            @Override // miuix.springback.view.SpringBackLayout.OnScrollListener
            public void onScrolled(SpringBackLayout springBackLayout, int i, int i2) {
                CustomTrigger customTrigger = CustomTrigger.this;
                customTrigger.mLastScrollDistance = customTrigger.mScrollDistance;
                customTrigger.mScrollDistance = -springBackLayout.getScrollY();
                CustomTrigger.this.mVelocityMonitor.update(CustomTrigger.this.mScrollDistance);
                CustomTrigger customTrigger2 = CustomTrigger.this;
                customTrigger2.mVelocityY = customTrigger2.mVelocityMonitor.getVelocity(0);
                CustomTrigger.this.mContainer.setTop(springBackLayout.getScrollY());
                if (CustomTrigger.this.getIndeterminateUpView() != null) {
                    CustomTrigger.this.getIndeterminateUpView().setBottom(CustomTrigger.this.mLayout.getBottom() + springBackLayout.getScrollY());
                }
                CustomTrigger customTrigger3 = CustomTrigger.this;
                if (customTrigger3.mScrollDistance < 0 && customTrigger3.mCurrentAction == CustomTrigger.this.getIndeterminateUpAction() && CustomTrigger.this.getIndeterminateUpAction() != null) {
                    CustomTrigger customTrigger4 = CustomTrigger.this;
                    float actionRestartOffsetPoint = customTrigger4.actionRestartOffsetPoint(customTrigger4.mCurrentAction);
                    if (CustomTrigger.this.mScrollState == 1 && (Math.abs(CustomTrigger.this.mLastScrollDistance) < actionRestartOffsetPoint || Math.abs(CustomTrigger.this.mScrollDistance) < actionRestartOffsetPoint)) {
                        TriggerState triggerState = CustomTrigger.this.mCurrentState;
                        CustomTrigger customTrigger5 = CustomTrigger.this;
                        if (triggerState == customTrigger5.mActionComplete) {
                            customTrigger5.transition(customTrigger5.mTracking);
                        }
                    }
                }
                if (CustomTrigger.this.mCurrentAction != null && (CustomTrigger.this.mCurrentAction instanceof BaseTrigger.IndeterminateAction)) {
                    CustomTrigger customTrigger6 = CustomTrigger.this;
                    float actionRestartOffsetPoint2 = customTrigger6.actionRestartOffsetPoint(customTrigger6.mCurrentAction);
                    if (CustomTrigger.this.mScrollState == 1 && (Math.abs(CustomTrigger.this.mLastScrollDistance) < actionRestartOffsetPoint2 || Math.abs(CustomTrigger.this.mScrollDistance) < actionRestartOffsetPoint2)) {
                        TriggerState triggerState2 = CustomTrigger.this.mCurrentState;
                        CustomTrigger customTrigger7 = CustomTrigger.this;
                        if (triggerState2 == customTrigger7.mActionComplete) {
                            customTrigger7.transition(customTrigger7.mTracking);
                        }
                    }
                    if (CustomTrigger.this.mScrollState == 1) {
                        TriggerState triggerState3 = CustomTrigger.this.mCurrentState;
                        CustomTrigger customTrigger8 = CustomTrigger.this;
                        if (triggerState3 == customTrigger8.mWaitForIndeterminate && Math.abs(customTrigger8.mLastScrollDistance) > CustomTrigger.this.mCurrentAction.mEnterPoint) {
                            CustomTrigger customTrigger9 = CustomTrigger.this;
                            customTrigger9.transition(customTrigger9.mTracking);
                        }
                    }
                }
                CustomTrigger.this.mCurrentState.handleScrolled(i2, springBackLayout.getScrollY());
                CustomTrigger customTrigger10 = CustomTrigger.this;
                customTrigger10.onSpringBackScrolled(springBackLayout, i, i2, customTrigger10.mScrollDistance);
            }
        };
        this.mUpActionDataListener = new BaseTrigger.IndeterminateUpAction.OnUpActionDataListener() { // from class: miuix.springback.trigger.CustomTrigger.4
        };
        this.mCompleteListener = new BaseTrigger.IndeterminateAction.OnActionCompleteListener() { // from class: miuix.springback.trigger.CustomTrigger.5
        };
        Idle idle = new Idle();
        this.mIdle = idle;
        this.mTracking = new Tracking();
        this.mActionStart = new ActionStart();
        this.mActionComplete = new ActionComplete();
        this.mWaitForIndeterminate = new WaitForIndeterminate();
        this.mActionTriggered = new ActionTriggered();
        this.mCurrentState = idle;
        init(context);
    }

    public final void init(Context context) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mVelocityMonitor = new VelocityMonitor();
        RelativeLayout relativeLayout = (RelativeLayout) this.mLayoutInflater.inflate(R$layout.miuix_sbl_trigger_layout, (ViewGroup) null);
        this.mContainer = relativeLayout;
        this.mIndicatorContainer = (FrameLayout) relativeLayout.findViewById(R$id.indicator_container);
    }

    public void setOnIndeterminateActionViewListener(BaseTrigger.IndeterminateAction.OnIndeterminateActionViewListener onIndeterminateActionViewListener) {
        this.mOnIndeterminateActionViewListener = onIndeterminateActionViewListener;
    }

    public void setOnSimpleActionViewListener(BaseTrigger.SimpleAction.OnSimpleActionViewListener onSimpleActionViewListener) {
        this.mOnSimpleActionViewListener = onSimpleActionViewListener;
    }

    public void setOnIndeterminateUpActionViewListener(BaseTrigger.IndeterminateUpAction.OnIndeterminateUpActionViewListener onIndeterminateUpActionViewListener) {
        this.mOnIndeterminateUpActionViewListener = onIndeterminateUpActionViewListener;
    }

    public void setOnActionDataListener(OnIndeterminateActionDataListener onIndeterminateActionDataListener) {
        this.mOnActionDataListener = onIndeterminateActionDataListener;
    }

    public void setOnUpActionDataListener(OnIndeterminateUpActionDataListener onIndeterminateUpActionDataListener) {
        this.mOnUpActionDataListener = onIndeterminateUpActionDataListener;
    }

    public TriggerState getCurrentState() {
        return this.mCurrentState;
    }

    public BaseTrigger.Action getCurrentAction() {
        return this.mCurrentAction;
    }

    @Override // miuix.springback.trigger.BaseTrigger
    public void addAction(BaseTrigger.Action action) {
        View view;
        View view2;
        View view3;
        super.addAction(action);
        if (action instanceof BaseTrigger.IndeterminateUpAction) {
            this.mIsExitIndeterminateUpAction = true;
            BaseTrigger.IndeterminateUpAction indeterminateUpAction = (BaseTrigger.IndeterminateUpAction) action;
            indeterminateUpAction.mUpDataListener = this.mUpActionDataListener;
            if (this.mUpContainer != null) {
                return;
            }
            View onCreateIndicator = indeterminateUpAction.onCreateIndicator(this.mLayoutInflater, this.mLayout);
            this.mUpContainer = onCreateIndicator;
            if (onCreateIndicator == null) {
                this.mUpContainer = this.mLayoutInflater.inflate(R$layout.miuix_sbl_trigger_up_layout, (ViewGroup) null);
            }
            SpringBackLayout springBackLayout = this.mLayout;
            if (springBackLayout == null || (view3 = this.mUpContainer) == null) {
                return;
            }
            springBackLayout.addView(view3);
        } else if (action instanceof BaseTrigger.IndeterminateAction) {
            this.mIsExitIndeterminateAction = true;
            BaseTrigger.IndeterminateAction indeterminateAction = (BaseTrigger.IndeterminateAction) action;
            indeterminateAction.mCompleteListener = this.mCompleteListener;
            if (this.mLoadingContainer != null) {
                return;
            }
            View onCreateIndicator2 = indeterminateAction.onCreateIndicator(this.mLayoutInflater, this.mContainer);
            this.mLoadingContainer = onCreateIndicator2;
            if (onCreateIndicator2 == null) {
                View inflate = this.mLayoutInflater.inflate(R$layout.miuix_sbl_trigger_loading_progress, (ViewGroup) null);
                View inflate2 = this.mLayoutInflater.inflate(R$layout.miuix_sbl_trigger_tracking_progress, (ViewGroup) null);
                View inflate3 = this.mLayoutInflater.inflate(R$layout.miuix_sbl_trigger_tracking_progress_label, (ViewGroup) null);
                this.mContainer.addView(inflate);
                this.mContainer.addView(inflate2);
                this.mContainer.addView(inflate3);
            }
            RelativeLayout relativeLayout = this.mContainer;
            if (relativeLayout == null || (view2 = this.mLoadingContainer) == null) {
                return;
            }
            relativeLayout.addView(view2);
        } else if (!(action instanceof BaseTrigger.SimpleAction)) {
        } else {
            this.mIsExitISimpleAction = true;
            BaseTrigger.SimpleAction simpleAction = (BaseTrigger.SimpleAction) action;
            if (this.mSimpleActionView != null) {
                return;
            }
            View onCreateIndicator3 = simpleAction.onCreateIndicator(this.mLayoutInflater, this.mIndicatorContainer);
            this.mSimpleActionView = onCreateIndicator3;
            if (onCreateIndicator3 == null) {
                this.mSimpleActionView = this.mLayoutInflater.inflate(R$layout.miuix_sbl_simple_indicator, (ViewGroup) this.mIndicatorContainer, false);
            }
            FrameLayout frameLayout = this.mIndicatorContainer;
            if (frameLayout == null || (view = this.mSimpleActionView) == null) {
                return;
            }
            frameLayout.addView(view);
        }
    }

    public ViewGroup getRootContainer() {
        return this.mContainer;
    }

    public ViewGroup getIndicatorContainer() {
        return this.mIndicatorContainer;
    }

    public View getSimpleActionView() {
        return this.mSimpleActionView;
    }

    public View getIndeterminateView() {
        return this.mLoadingContainer;
    }

    public View getIndeterminateUpView() {
        return this.mUpContainer;
    }

    public boolean isExitIndeterminateAction() {
        return this.mIsExitIndeterminateAction;
    }

    public boolean isExitIndeterminateUpAction() {
        return this.mIsExitIndeterminateUpAction;
    }

    public boolean isExitSimpleAction() {
        return this.mIsExitISimpleAction;
    }

    public void attach(SpringBackLayout springBackLayout) {
        if (!springBackLayout.springBackEnable()) {
            springBackLayout.setSpringBackEnable(true);
        }
        this.mLayout = springBackLayout;
        springBackLayout.addView(this.mContainer);
        if (this.mUpContainer != null) {
            boolean z = false;
            for (int i = 0; i < this.mLayout.getChildCount(); i++) {
                if (this.mLayout.getChildAt(i) == this.mUpContainer) {
                    z = true;
                }
            }
            if (!z) {
                this.mLayout.addView(this.mUpContainer);
            }
        }
        if (this.mSimpleActionView != null) {
            boolean z2 = false;
            for (int i2 = 0; i2 < this.mIndicatorContainer.getChildCount(); i2++) {
                if (this.mIndicatorContainer.getChildAt(i2) == this.mSimpleActionView) {
                    z2 = true;
                }
            }
            if (!z2) {
                this.mIndicatorContainer.addView(this.mSimpleActionView);
            }
        }
        springBackLayout.addOnLayoutChangeListener(this.mLayoutChangeListener);
        springBackLayout.setOnSpringListener(this.mOnSpringListener);
        springBackLayout.addOnScrollListener(this.mOnScrollListener);
    }

    public final float actionRestartOffsetPoint(BaseTrigger.Action action) {
        float simpleViewRestartOffsetPoint;
        int i;
        float f;
        if (action != null && (action instanceof BaseTrigger.IndeterminateAction)) {
            simpleViewRestartOffsetPoint = getIndeterminateViewRestartOffsetPoint();
        } else if (action != null && (action instanceof BaseTrigger.IndeterminateUpAction)) {
            simpleViewRestartOffsetPoint = getIndeterminateUpViewRestartOffsetPoint();
        } else {
            simpleViewRestartOffsetPoint = (action == null || !(action instanceof BaseTrigger.SimpleAction)) ? -1.0f : getSimpleViewRestartOffsetPoint();
        }
        if (simpleViewRestartOffsetPoint < 0.0f) {
            if (this.mScrollDistance < 0 && action == getIndeterminateUpAction() && getIndeterminateUpAction() != null) {
                f = (getIndeterminateUpAction().mTriggerPoint - getIndeterminateUpAction().mEnterPoint) * 0.25f;
                i = getIndeterminateUpAction().mEnterPoint;
            } else {
                BaseTrigger.Action action2 = this.mCurrentAction;
                if (action2 != null && (action instanceof BaseTrigger.IndeterminateAction)) {
                    int i2 = action2.mTriggerPoint;
                    i = action2.mEnterPoint;
                    f = (i2 - i) * 0.25f;
                }
            }
            return f + i;
        }
        return 0.0f;
    }

    /* loaded from: classes3.dex */
    public class Idle extends TriggerState {
        public Idle() {
        }

        @Override // miuix.springback.trigger.TriggerState
        public void handleScrollStateChange(int i, int i2) {
            if (i == 0) {
                if (i2 != 1 && i2 != 2) {
                    return;
                }
                CustomTrigger customTrigger = CustomTrigger.this;
                customTrigger.transition(customTrigger.mTracking);
            }
        }
    }

    /* loaded from: classes3.dex */
    public class Tracking extends TriggerState {
        public boolean mLockActivated;
        public boolean mTriggerable;
        public boolean mUpTriggerable;

        public Tracking() {
            this.mTriggerable = false;
            this.mUpTriggerable = false;
            this.mLockActivated = false;
        }

        @Override // miuix.springback.trigger.TriggerState
        public void handleScrollStateChange(int i, int i2) {
            if (i2 == 0) {
                CustomTrigger customTrigger = CustomTrigger.this;
                customTrigger.transition(customTrigger.mIdle);
                this.mUpTriggerable = false;
                this.mLockActivated = false;
            }
        }

        @Override // miuix.springback.trigger.TriggerState
        public boolean handleSpringBack() {
            if ((!this.mTriggerable || CustomTrigger.this.mCurrentAction == null) && CustomTrigger.this.mCurrentAction != null && (CustomTrigger.this.mCurrentAction instanceof BaseTrigger.SimpleAction) && CustomTrigger.this.getSimpleActionView() != null) {
                CustomTrigger.this.getSimpleActionView().setVisibility(8);
            }
            if (CustomTrigger.this.mCurrentAction == null) {
                return false;
            }
            if (CustomTrigger.this.mCurrentAction instanceof BaseTrigger.IndeterminateAction) {
                CustomTrigger customTrigger = CustomTrigger.this;
                if (customTrigger.mScrollDistance > customTrigger.mCurrentAction.mEnterPoint) {
                    if (!this.mTriggerable) {
                        if (Math.abs(CustomTrigger.this.mLayout.getScaleY()) < Math.abs(CustomTrigger.this.mCurrentAction.mTriggerPoint)) {
                            CustomTrigger.this.mCurrentAction.notifyExit();
                            CustomTrigger customTrigger2 = CustomTrigger.this;
                            customTrigger2.notifyViewsExit(customTrigger2.mCurrentAction, CustomTrigger.this.mScrollDistance);
                        }
                        CustomTrigger.this.mLayout.smoothScrollTo(0, 0);
                    } else {
                        CustomTrigger customTrigger3 = CustomTrigger.this;
                        customTrigger3.mLayout.smoothScrollTo(0, -customTrigger3.mCurrentAction.mTriggerPoint);
                        CustomTrigger customTrigger4 = CustomTrigger.this;
                        customTrigger4.transition(customTrigger4.mWaitForIndeterminate);
                    }
                    return true;
                }
            }
            if (CustomTrigger.this.mCurrentAction instanceof BaseTrigger.IndeterminateUpAction) {
                CustomTrigger customTrigger5 = CustomTrigger.this;
                customTrigger5.mLayout.smoothScrollTo(0, customTrigger5.mCurrentAction.mTriggerPoint);
                CustomTrigger customTrigger6 = CustomTrigger.this;
                customTrigger6.transition(customTrigger6.mWaitForIndeterminate);
                return true;
            }
            CustomTrigger customTrigger7 = CustomTrigger.this;
            customTrigger7.transition(customTrigger7.mActionTriggered);
            if (this.mLockActivated) {
                CustomTrigger.this.mCurrentAction.notifyTriggered();
                CustomTrigger customTrigger8 = CustomTrigger.this;
                customTrigger8.notifyViewsTriggered(customTrigger8.mCurrentAction, CustomTrigger.this.mScrollDistance);
            } else {
                CustomTrigger.this.mCurrentAction.notifyExit();
                CustomTrigger customTrigger9 = CustomTrigger.this;
                customTrigger9.notifyViewsExit(customTrigger9.mCurrentAction, CustomTrigger.this.mScrollDistance);
            }
            if (CustomTrigger.this.getSimpleActionView() != null) {
                CustomTrigger.this.getSimpleActionView().setVisibility(8);
            }
            return false;
        }

        @Override // miuix.springback.trigger.TriggerState
        public void handleScrolled(int i, int i2) {
            if (CustomTrigger.this.mScrollState == 1 || CustomTrigger.this.mScrollState == 2) {
                BaseTrigger.Action action = CustomTrigger.this.mCurrentAction;
                CustomTrigger customTrigger = CustomTrigger.this;
                if (customTrigger.mScrollDistance < 0) {
                    if (!customTrigger.mUpActionBegin) {
                        this.mUpTriggerable = false;
                    }
                    boolean z = this.mUpTriggerable;
                    BaseTrigger.IndeterminateUpAction indeterminateUpAction = CustomTrigger.this.getIndeterminateUpAction();
                    if (indeterminateUpAction != null) {
                        if (CustomTrigger.this.getIndeterminateUpView() != null && CustomTrigger.this.getIndeterminateUpView().getVisibility() != 0) {
                            CustomTrigger.this.getIndeterminateUpView().setVisibility(0);
                        }
                        CustomTrigger.this.mCurrentAction = indeterminateUpAction;
                        CustomTrigger customTrigger2 = CustomTrigger.this;
                        customTrigger2.notifyViewsStart(customTrigger2.mCurrentAction, action, CustomTrigger.this.mLastScrollDistance);
                        if (Math.abs(CustomTrigger.this.mScrollDistance) > CustomTrigger.this.getIndeterminateUpAction().mEnterPoint && !CustomTrigger.this.mUpActionBegin) {
                            CustomTrigger.this.mUpActionBegin = true;
                            this.mUpTriggerable = true;
                            CustomTrigger.this.mEnterTime = SystemClock.elapsedRealtime();
                            indeterminateUpAction.notifyEntered();
                            CustomTrigger customTrigger3 = CustomTrigger.this;
                            customTrigger3.notifyViewsEntered(customTrigger3.mCurrentAction, CustomTrigger.this.mScrollDistance);
                        }
                        boolean z2 = this.mUpTriggerable;
                        if (z != z2 && z2) {
                            indeterminateUpAction.notifyActivated();
                            CustomTrigger customTrigger4 = CustomTrigger.this;
                            customTrigger4.notifyViewsActivated(customTrigger4.mCurrentAction, CustomTrigger.this.mScrollDistance);
                            if (CustomTrigger.this.mScrollState == 2) {
                                CustomTrigger.this.mLayout.smoothScrollTo(0, indeterminateUpAction.mTriggerPoint);
                                CustomTrigger customTrigger5 = CustomTrigger.this;
                                customTrigger5.transition(customTrigger5.mWaitForIndeterminate);
                            }
                        }
                    }
                    CustomTrigger customTrigger6 = CustomTrigger.this;
                    customTrigger6.notifyViewsAnimator(customTrigger6.mCurrentAction, action, CustomTrigger.this.mScrollDistance);
                    return;
                }
                this.mUpTriggerable = false;
                int i3 = customTrigger.mActionIndex;
                boolean z3 = this.mTriggerable;
                BaseTrigger.Action action2 = CustomTrigger.this.mCurrentAction;
                for (int i4 = 0; i4 < CustomTrigger.this.getActions().size(); i4++) {
                    CustomTrigger customTrigger7 = CustomTrigger.this;
                    if (customTrigger7.mScrollDistance <= customTrigger7.getActions().get(i4).mEnterPoint) {
                        break;
                    }
                    CustomTrigger.this.mActionIndex = i4;
                }
                if (CustomTrigger.this.mActionIndex >= 0) {
                    BaseTrigger.Action action3 = CustomTrigger.this.getActions().get(CustomTrigger.this.mActionIndex);
                    boolean z4 = action3 != null && (action3 instanceof BaseTrigger.SimpleAction);
                    if ((!z4 || CustomTrigger.this.mVelocityY >= 1000.0f || CustomTrigger.this.mScrollState != 1) && z4) {
                        CustomTrigger.this.mActionIndex = i3;
                    } else {
                        CustomTrigger.this.mCurrentAction = action3;
                        CustomTrigger customTrigger8 = CustomTrigger.this;
                        customTrigger8.notifyViewsStart(customTrigger8.mCurrentAction, action, CustomTrigger.this.mLastScrollDistance);
                        CustomTrigger customTrigger9 = CustomTrigger.this;
                        this.mTriggerable = customTrigger9.mScrollDistance >= customTrigger9.mCurrentAction.mTriggerPoint;
                    }
                } else {
                    CustomTrigger.this.mCurrentAction = null;
                    this.mTriggerable = false;
                }
                if (i3 != CustomTrigger.this.mActionIndex) {
                    if (action2 != null) {
                        action2.onExit();
                        if (CustomTrigger.this.getSimpleActionView() != null) {
                            CustomTrigger.this.getSimpleActionView().setVisibility(8);
                        }
                    }
                    if (CustomTrigger.this.mCurrentAction != null) {
                        if (!(CustomTrigger.this.mCurrentAction instanceof BaseTrigger.IndeterminateAction)) {
                            if ((CustomTrigger.this.mCurrentAction instanceof BaseTrigger.SimpleAction) && CustomTrigger.this.getSimpleActionView() != null) {
                                CustomTrigger.this.getSimpleActionView().setVisibility(0);
                            }
                        } else if (CustomTrigger.this.getSimpleActionView() != null) {
                            CustomTrigger.this.getSimpleActionView().setVisibility(8);
                        }
                        CustomTrigger.this.mEnterTime = SystemClock.elapsedRealtime();
                        CustomTrigger.this.mCurrentAction.notifyEntered();
                        CustomTrigger customTrigger10 = CustomTrigger.this;
                        customTrigger10.notifyViewsEntered(customTrigger10.mCurrentAction, CustomTrigger.this.mScrollDistance);
                        this.mLockActivated = false;
                        if (this.mTriggerable) {
                            if (CustomTrigger.this.mCurrentAction instanceof BaseTrigger.SimpleAction) {
                                this.mLockActivated = true;
                                HapticCompat.performHapticFeedback(CustomTrigger.this.mLayout, HapticFeedbackConstants.MIUI_SWITCH);
                            }
                            CustomTrigger.this.mCurrentAction.notifyActivated();
                            CustomTrigger customTrigger11 = CustomTrigger.this;
                            customTrigger11.notifyViewsActivated(customTrigger11.mCurrentAction, CustomTrigger.this.mScrollDistance);
                        }
                    } else if (CustomTrigger.this.getSimpleActionView() != null) {
                        CustomTrigger.this.getSimpleActionView().setVisibility(8);
                    }
                } else if (action2 != null && z3 != this.mTriggerable) {
                    if (z3) {
                        CustomTrigger.this.mEnterTime = SystemClock.elapsedRealtime();
                        action2.notifyEntered();
                        CustomTrigger customTrigger12 = CustomTrigger.this;
                        customTrigger12.notifyViewsEntered(customTrigger12.mCurrentAction, CustomTrigger.this.mScrollDistance);
                        this.mLockActivated = false;
                    } else {
                        if (CustomTrigger.this.mCurrentAction instanceof BaseTrigger.SimpleAction) {
                            this.mLockActivated = true;
                        }
                        HapticCompat.performHapticFeedback(CustomTrigger.this.mLayout, HapticFeedbackConstants.MIUI_MESH_NORMAL);
                        action2.notifyActivated();
                        CustomTrigger customTrigger13 = CustomTrigger.this;
                        customTrigger13.notifyViewsActivated(customTrigger13.mCurrentAction, CustomTrigger.this.mScrollDistance);
                    }
                }
                CustomTrigger customTrigger14 = CustomTrigger.this;
                customTrigger14.notifyViewsAnimator(customTrigger14.mCurrentAction, action, CustomTrigger.this.mScrollDistance);
            }
        }
    }

    /* loaded from: classes3.dex */
    public class WaitForIndeterminate extends TriggerState {
        public WaitForIndeterminate() {
        }

        @Override // miuix.springback.trigger.TriggerState
        public void handleScrollStateChange(int i, int i2) {
            if (i2 == 0) {
                CustomTrigger customTrigger = CustomTrigger.this;
                customTrigger.transition(customTrigger.mActionStart);
                if (CustomTrigger.this.mCurrentAction != null && (CustomTrigger.this.mCurrentAction instanceof BaseTrigger.IndeterminateAction)) {
                    CustomTrigger.this.mCurrentAction.notifyTriggered();
                    CustomTrigger customTrigger2 = CustomTrigger.this;
                    customTrigger2.notifyViewsTriggered(customTrigger2.mCurrentAction, CustomTrigger.this.mScrollDistance);
                } else if (CustomTrigger.this.getIndeterminateUpAction() == null || !(CustomTrigger.this.mCurrentAction instanceof BaseTrigger.IndeterminateUpAction)) {
                } else {
                    CustomTrigger.this.getIndeterminateUpAction().notifyTriggered();
                    CustomTrigger customTrigger3 = CustomTrigger.this;
                    customTrigger3.notifyViewsTriggered(customTrigger3.mCurrentAction, CustomTrigger.this.mScrollDistance);
                }
            }
        }
    }

    /* loaded from: classes3.dex */
    public class ActionTriggered extends TriggerState {
        public ActionTriggered() {
        }

        @Override // miuix.springback.trigger.TriggerState
        public void handleScrollStateChange(int i, int i2) {
            if (i2 == 0) {
                CustomTrigger customTrigger = CustomTrigger.this;
                customTrigger.transition(customTrigger.mIdle);
            }
        }

        @Override // miuix.springback.trigger.TriggerState
        public void handleScrolled(int i, int i2) {
            if (CustomTrigger.this.mCurrentAction == null || !(CustomTrigger.this.mCurrentAction instanceof BaseTrigger.SimpleAction)) {
                return;
            }
            CustomTrigger customTrigger = CustomTrigger.this;
            if (customTrigger.mScrollDistance >= customTrigger.mCurrentAction.mEnterPoint || CustomTrigger.this.mScrollState != 1) {
                return;
            }
            CustomTrigger.this.mActionIndex = -1;
            CustomTrigger customTrigger2 = CustomTrigger.this;
            customTrigger2.transition(customTrigger2.mTracking);
        }
    }

    /* loaded from: classes3.dex */
    public class ActionStart extends TriggerState {
        public ActionStart() {
        }

        @Override // miuix.springback.trigger.TriggerState
        public void handleScrollStateChange(int i, int i2) {
            super.handleScrollStateChange(i, i2);
        }

        @Override // miuix.springback.trigger.TriggerState
        public void handleScrolled(int i, int i2) {
            super.handleScrolled(i, i2);
        }

        @Override // miuix.springback.trigger.TriggerState
        public boolean handleSpringBack() {
            return super.handleSpringBack();
        }
    }

    /* loaded from: classes3.dex */
    public class ActionComplete extends TriggerState {
        public ActionComplete() {
        }

        @Override // miuix.springback.trigger.TriggerState
        public void handleScrollStateChange(int i, int i2) {
            super.handleScrollStateChange(i, i2);
            if (i2 == 0) {
                CustomTrigger customTrigger = CustomTrigger.this;
                customTrigger.transition(customTrigger.mIdle);
            }
        }
    }

    public void transition(TriggerState triggerState) {
        BaseTrigger.Action action;
        this.mCurrentState = triggerState;
        if (triggerState == this.mIdle) {
            if (this.mScrollerFinished && (action = this.mCurrentAction) != null) {
                action.notifyFinished();
                BaseTrigger.Action action2 = this.mCurrentAction;
                if (action2 instanceof BaseTrigger.IndeterminateAction) {
                    notifyIndeterminateViewFinished(this.mScrollDistance);
                } else if (action2 instanceof BaseTrigger.IndeterminateUpAction) {
                    notifyIndeterminateUpViewFinished(this.mScrollDistance);
                } else if (action2 instanceof BaseTrigger.SimpleAction) {
                    notifySimpleViewFinished(this.mScrollDistance);
                }
            }
            this.mCurrentAction = null;
            this.mActionIndex = -1;
            this.mVelocityMonitor.clear();
        }
    }

    public final void notifyViewsStart(BaseTrigger.Action action, BaseTrigger.Action action2, int i) {
        if (action != null && (action instanceof BaseTrigger.IndeterminateAction) && action2 != action) {
            notifyIndeterminateViewStart(i);
        } else if (action != null && (action instanceof BaseTrigger.SimpleAction) && action2 != action) {
            notifySimpleViewStart(i);
        } else if (action == null || !(action instanceof BaseTrigger.IndeterminateUpAction) || action2 == action) {
        } else {
            notifyIndeterminateUpViewStart(i);
        }
    }

    public final void notifyViewsEntered(BaseTrigger.Action action, int i) {
        if (action != null && (action instanceof BaseTrigger.IndeterminateAction)) {
            notifyIndeterminateViewEntered(i);
        } else if (action != null && (action instanceof BaseTrigger.SimpleAction)) {
            notifySimpleViewEntered(i);
        } else if (action == null || !(action instanceof BaseTrigger.IndeterminateUpAction)) {
        } else {
            notifyIndeterminateUpViewEntered(i);
        }
    }

    public final void notifyViewsActivated(BaseTrigger.Action action, int i) {
        if (action != null && (action instanceof BaseTrigger.IndeterminateAction)) {
            notifyIndeterminateViewActivated(i);
        } else if (action != null && (action instanceof BaseTrigger.SimpleAction)) {
            notifySimpleViewActivated(i);
        } else if (action == null || !(action instanceof BaseTrigger.IndeterminateUpAction)) {
        } else {
            notifyIndeterminateUpViewActivated(i);
        }
    }

    public final void notifyViewsTriggered(BaseTrigger.Action action, int i) {
        if (action != null && (action instanceof BaseTrigger.IndeterminateAction)) {
            notifyIndeterminateViewTriggered(i);
        } else if (action != null && (action instanceof BaseTrigger.SimpleAction)) {
            notifySimpleViewTriggered(i);
        } else if (action == null || !(action instanceof BaseTrigger.IndeterminateUpAction)) {
        } else {
            notifyIndeterminateUpViewTriggered(i);
        }
    }

    public final void notifyViewsExit(BaseTrigger.Action action, int i) {
        if (action != null && (action instanceof BaseTrigger.IndeterminateAction)) {
            notifyIndeterminateViewExit(i);
        } else if (action != null && (action instanceof BaseTrigger.SimpleAction)) {
            notifySimpleViewExit(i);
        } else if (action == null || !(action instanceof BaseTrigger.IndeterminateUpAction)) {
        } else {
            notifyIndeterminateUpViewExit(i);
        }
    }

    public final void notifyViewsAnimator(BaseTrigger.Action action, BaseTrigger.Action action2, int i) {
        if (action != null && (action instanceof BaseTrigger.IndeterminateAction)) {
            if (Math.abs(i) < action.mEnterPoint) {
                notifyIndeterminateViewStarting(i);
            }
            if (Math.abs(i) >= action.mEnterPoint && Math.abs(i) < action.mTriggerPoint) {
                notifyIndeterminateViewEntering(i);
            }
            if (Math.abs(i) < action.mTriggerPoint) {
                return;
            }
            notifyIndeterminateViewActivating(i);
        } else if (action != null && (action instanceof BaseTrigger.SimpleAction)) {
            if (Math.abs(i) < action.mEnterPoint) {
                notifySimpleViewStarting(i);
            }
            if (Math.abs(i) >= action.mEnterPoint && Math.abs(i) < action.mTriggerPoint) {
                notifySimpleViewEntering(i);
            }
            if (Math.abs(i) < action.mTriggerPoint) {
                return;
            }
            notifySimpleViewActivating(i);
        } else if (action == null || !(action instanceof BaseTrigger.IndeterminateUpAction)) {
        } else {
            if (Math.abs(i) < action.mEnterPoint) {
                notifyIndeterminateUpViewStarting(i);
            }
            if (Math.abs(i) >= action.mEnterPoint && Math.abs(i) < action.mTriggerPoint) {
                notifyIndeterminateUpViewEntering(i);
            }
            if (Math.abs(i) < action.mTriggerPoint) {
                return;
            }
            notifyIndeterminateUpViewActivating(i);
        }
    }

    public final float getIndeterminateViewRestartOffsetPoint() {
        BaseTrigger.IndeterminateAction.OnIndeterminateActionViewListener onIndeterminateActionViewListener = this.mOnIndeterminateActionViewListener;
        if (onIndeterminateActionViewListener != null) {
            return onIndeterminateActionViewListener.getViewRestartOffsetPoint();
        }
        return 0.0f;
    }

    public final void notifyIndeterminateViewStart(int i) {
        BaseTrigger.IndeterminateAction.OnIndeterminateActionViewListener onIndeterminateActionViewListener = this.mOnIndeterminateActionViewListener;
        if (onIndeterminateActionViewListener != null) {
            onIndeterminateActionViewListener.onViewStart(i);
        }
    }

    public final void notifyIndeterminateViewStarting(int i) {
        BaseTrigger.IndeterminateAction.OnIndeterminateActionViewListener onIndeterminateActionViewListener = this.mOnIndeterminateActionViewListener;
        if (onIndeterminateActionViewListener != null) {
            onIndeterminateActionViewListener.onViewStarting(i);
        }
    }

    public final void notifyIndeterminateViewEntered(int i) {
        BaseTrigger.IndeterminateAction.OnIndeterminateActionViewListener onIndeterminateActionViewListener = this.mOnIndeterminateActionViewListener;
        if (onIndeterminateActionViewListener != null) {
            onIndeterminateActionViewListener.onViewEntered(i);
        }
    }

    public final void notifyIndeterminateViewEntering(int i) {
        BaseTrigger.IndeterminateAction.OnIndeterminateActionViewListener onIndeterminateActionViewListener = this.mOnIndeterminateActionViewListener;
        if (onIndeterminateActionViewListener != null) {
            onIndeterminateActionViewListener.onViewEntering(i);
        }
    }

    public final void notifyIndeterminateViewActivated(int i) {
        BaseTrigger.IndeterminateAction.OnIndeterminateActionViewListener onIndeterminateActionViewListener = this.mOnIndeterminateActionViewListener;
        if (onIndeterminateActionViewListener != null) {
            onIndeterminateActionViewListener.onViewActivated(i);
        }
    }

    public final void notifyIndeterminateViewActivating(int i) {
        BaseTrigger.IndeterminateAction.OnIndeterminateActionViewListener onIndeterminateActionViewListener = this.mOnIndeterminateActionViewListener;
        if (onIndeterminateActionViewListener != null) {
            onIndeterminateActionViewListener.onViewActivating(i);
        }
    }

    public final void notifyIndeterminateViewTriggered(int i) {
        BaseTrigger.IndeterminateAction.OnIndeterminateActionViewListener onIndeterminateActionViewListener = this.mOnIndeterminateActionViewListener;
        if (onIndeterminateActionViewListener != null) {
            onIndeterminateActionViewListener.onViewTriggered(i);
        }
    }

    public final void notifyIndeterminateViewFinished(int i) {
        BaseTrigger.IndeterminateAction.OnIndeterminateActionViewListener onIndeterminateActionViewListener = this.mOnIndeterminateActionViewListener;
        if (onIndeterminateActionViewListener != null) {
            onIndeterminateActionViewListener.onViewFinished(i);
        }
    }

    public final void notifyIndeterminateViewExit(int i) {
        BaseTrigger.IndeterminateAction.OnIndeterminateActionViewListener onIndeterminateActionViewListener = this.mOnIndeterminateActionViewListener;
        if (onIndeterminateActionViewListener != null) {
            onIndeterminateActionViewListener.onViewExit(i);
        }
    }

    public final float getSimpleViewRestartOffsetPoint() {
        BaseTrigger.SimpleAction.OnSimpleActionViewListener onSimpleActionViewListener = this.mOnSimpleActionViewListener;
        if (onSimpleActionViewListener != null) {
            return onSimpleActionViewListener.getViewRestartOffsetPoint();
        }
        return 0.0f;
    }

    public final void notifySimpleViewStart(int i) {
        BaseTrigger.SimpleAction.OnSimpleActionViewListener onSimpleActionViewListener = this.mOnSimpleActionViewListener;
        if (onSimpleActionViewListener != null) {
            onSimpleActionViewListener.onViewStart(i);
        }
    }

    public final void notifySimpleViewStarting(int i) {
        BaseTrigger.SimpleAction.OnSimpleActionViewListener onSimpleActionViewListener = this.mOnSimpleActionViewListener;
        if (onSimpleActionViewListener != null) {
            onSimpleActionViewListener.onViewStarting(i);
        }
    }

    public final void notifySimpleViewEntered(int i) {
        BaseTrigger.SimpleAction.OnSimpleActionViewListener onSimpleActionViewListener = this.mOnSimpleActionViewListener;
        if (onSimpleActionViewListener != null) {
            onSimpleActionViewListener.onViewEntered(i);
        }
    }

    public final void notifySimpleViewEntering(int i) {
        BaseTrigger.SimpleAction.OnSimpleActionViewListener onSimpleActionViewListener = this.mOnSimpleActionViewListener;
        if (onSimpleActionViewListener != null) {
            onSimpleActionViewListener.onViewEntering(i);
        }
    }

    public final void notifySimpleViewActivated(int i) {
        BaseTrigger.SimpleAction.OnSimpleActionViewListener onSimpleActionViewListener = this.mOnSimpleActionViewListener;
        if (onSimpleActionViewListener != null) {
            onSimpleActionViewListener.onViewActivated(i);
        }
    }

    public final void notifySimpleViewActivating(int i) {
        BaseTrigger.SimpleAction.OnSimpleActionViewListener onSimpleActionViewListener = this.mOnSimpleActionViewListener;
        if (onSimpleActionViewListener != null) {
            onSimpleActionViewListener.onViewActivating(i);
        }
    }

    public final void notifySimpleViewTriggered(int i) {
        BaseTrigger.SimpleAction.OnSimpleActionViewListener onSimpleActionViewListener = this.mOnSimpleActionViewListener;
        if (onSimpleActionViewListener != null) {
            onSimpleActionViewListener.onViewTriggered(i);
        }
    }

    public final void notifySimpleViewFinished(int i) {
        BaseTrigger.SimpleAction.OnSimpleActionViewListener onSimpleActionViewListener = this.mOnSimpleActionViewListener;
        if (onSimpleActionViewListener != null) {
            onSimpleActionViewListener.onViewFinished(i);
        }
    }

    public final void notifySimpleViewExit(int i) {
        BaseTrigger.SimpleAction.OnSimpleActionViewListener onSimpleActionViewListener = this.mOnSimpleActionViewListener;
        if (onSimpleActionViewListener != null) {
            onSimpleActionViewListener.onViewExit(i);
        }
    }

    public final float getIndeterminateUpViewRestartOffsetPoint() {
        BaseTrigger.IndeterminateUpAction.OnIndeterminateUpActionViewListener onIndeterminateUpActionViewListener = this.mOnIndeterminateUpActionViewListener;
        if (onIndeterminateUpActionViewListener != null) {
            return onIndeterminateUpActionViewListener.getViewRestartOffsetPoint();
        }
        return 0.0f;
    }

    public final void notifyIndeterminateUpViewStart(int i) {
        BaseTrigger.IndeterminateUpAction.OnIndeterminateUpActionViewListener onIndeterminateUpActionViewListener = this.mOnIndeterminateUpActionViewListener;
        if (onIndeterminateUpActionViewListener != null) {
            onIndeterminateUpActionViewListener.onViewStart(i);
        }
    }

    public final void notifyIndeterminateUpViewStarting(int i) {
        BaseTrigger.IndeterminateUpAction.OnIndeterminateUpActionViewListener onIndeterminateUpActionViewListener = this.mOnIndeterminateUpActionViewListener;
        if (onIndeterminateUpActionViewListener != null) {
            onIndeterminateUpActionViewListener.onViewStarting(i);
        }
    }

    public final void notifyIndeterminateUpViewEntered(int i) {
        BaseTrigger.IndeterminateUpAction.OnIndeterminateUpActionViewListener onIndeterminateUpActionViewListener = this.mOnIndeterminateUpActionViewListener;
        if (onIndeterminateUpActionViewListener != null) {
            onIndeterminateUpActionViewListener.onViewEntered(i);
        }
    }

    public final void notifyIndeterminateUpViewEntering(int i) {
        BaseTrigger.IndeterminateUpAction.OnIndeterminateUpActionViewListener onIndeterminateUpActionViewListener = this.mOnIndeterminateUpActionViewListener;
        if (onIndeterminateUpActionViewListener != null) {
            onIndeterminateUpActionViewListener.onViewEntering(i);
        }
    }

    public final void notifyIndeterminateUpViewActivated(int i) {
        BaseTrigger.IndeterminateUpAction.OnIndeterminateUpActionViewListener onIndeterminateUpActionViewListener = this.mOnIndeterminateUpActionViewListener;
        if (onIndeterminateUpActionViewListener != null) {
            onIndeterminateUpActionViewListener.onViewActivated(i);
        }
    }

    public final void notifyIndeterminateUpViewActivating(int i) {
        BaseTrigger.IndeterminateUpAction.OnIndeterminateUpActionViewListener onIndeterminateUpActionViewListener = this.mOnIndeterminateUpActionViewListener;
        if (onIndeterminateUpActionViewListener != null) {
            onIndeterminateUpActionViewListener.onViewActivating(i);
        }
    }

    public final void notifyIndeterminateUpViewTriggered(int i) {
        BaseTrigger.IndeterminateUpAction.OnIndeterminateUpActionViewListener onIndeterminateUpActionViewListener = this.mOnIndeterminateUpActionViewListener;
        if (onIndeterminateUpActionViewListener != null) {
            onIndeterminateUpActionViewListener.onViewTriggered(i);
        }
    }

    public final void notifyIndeterminateUpViewFinished(int i) {
        BaseTrigger.IndeterminateUpAction.OnIndeterminateUpActionViewListener onIndeterminateUpActionViewListener = this.mOnIndeterminateUpActionViewListener;
        if (onIndeterminateUpActionViewListener != null) {
            onIndeterminateUpActionViewListener.onViewFinished(i);
        }
    }

    public final void notifyIndeterminateUpViewExit(int i) {
        BaseTrigger.IndeterminateUpAction.OnIndeterminateUpActionViewListener onIndeterminateUpActionViewListener = this.mOnIndeterminateUpActionViewListener;
        if (onIndeterminateUpActionViewListener != null) {
            onIndeterminateUpActionViewListener.onViewExit(i);
        }
    }
}
