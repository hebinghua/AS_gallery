package com.miui.gallery.hybrid.pulltorefresh;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.miui.gallery.R$styleable;

/* loaded from: classes2.dex */
public abstract class PullToRefreshBase<T extends View> extends LinearLayout {
    public Mode mCurrentMode;
    public PullToRefreshBase<T>.SmoothScrollRunnable mCurrentSmoothScrollRunnable;
    public boolean mFilterTouchEvents;
    public LoadingLayout mFooterLayout;
    public LoadingLayout mHeaderLayout;
    public float mInitialMotionX;
    public float mInitialMotionY;
    public boolean mIsBeingDragged;
    public float mLastMotionX;
    public float mLastMotionY;
    public boolean mLayoutVisibilityChangesEnabled;
    public AnimationStyle mLoadingAnimationStyle;
    public int mMaximumPullScroll;
    public Mode mMode;
    public OnPullEventListener<T> mOnPullEventListener;
    public OnRefreshListener<T> mOnRefreshListener;
    public OnRefreshListener2<T> mOnRefreshListener2;
    public boolean mOverScrollEnabled;
    public T mRefreshableView;
    public FrameLayout mRefreshableViewWrapper;
    public Interpolator mScrollAnimationInterpolator;
    public boolean mScrollingWhileRefreshingEnabled;
    public boolean mShowViewWhileRefreshing;
    public State mState;
    public int mTouchSlop;

    /* loaded from: classes2.dex */
    public interface OnPullEventListener<V extends View> {
        void onPullEvent(PullToRefreshBase<V> pullToRefreshBase, State state, Mode mode);
    }

    /* loaded from: classes2.dex */
    public interface OnRefreshListener<V extends View> {
        void onRefresh(PullToRefreshBase<V> pullToRefreshBase);
    }

    /* loaded from: classes2.dex */
    public interface OnRefreshListener2<V extends View> {
        void onPullDownToRefresh(PullToRefreshBase<V> pullToRefreshBase);

        void onPullUpToRefresh(PullToRefreshBase<V> pullToRefreshBase);
    }

    /* loaded from: classes2.dex */
    public interface OnSmoothScrollFinishedListener {
        void onSmoothScrollFinished();
    }

    /* loaded from: classes2.dex */
    public enum Orientation {
        VERTICAL,
        HORIZONTAL
    }

    public abstract T createRefreshableView(Context context, AttributeSet attributeSet);

    public abstract Orientation getPullToRefreshScrollDirection();

    public int getPullToRefreshScrollDuration() {
        return 200;
    }

    public int getPullToRefreshScrollDurationLonger() {
        return 325;
    }

    public void handleStyledAttributes(TypedArray typedArray) {
    }

    public abstract boolean isReadyForPullEnd();

    public abstract boolean isReadyForPullStart();

    public void onPtrRestoreInstanceState(Bundle bundle) {
    }

    public void onPtrSaveInstanceState(Bundle bundle) {
    }

    public PullToRefreshBase(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mIsBeingDragged = false;
        this.mState = State.RESET;
        this.mMode = Mode.getDefault();
        this.mShowViewWhileRefreshing = true;
        this.mScrollingWhileRefreshingEnabled = false;
        this.mFilterTouchEvents = true;
        this.mOverScrollEnabled = true;
        this.mLayoutVisibilityChangesEnabled = true;
        this.mLoadingAnimationStyle = AnimationStyle.getDefault();
        init(context, attributeSet);
    }

    @Override // android.view.ViewGroup
    public void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        T refreshableView = getRefreshableView();
        if (refreshableView instanceof ViewGroup) {
            ((ViewGroup) refreshableView).addView(view, i, layoutParams);
            return;
        }
        throw new UnsupportedOperationException("Refreshable View is not a ViewGroup so can't addView");
    }

    public final Mode getCurrentMode() {
        return this.mCurrentMode;
    }

    public final boolean getFilterTouchEvents() {
        return this.mFilterTouchEvents;
    }

    public final ILoadingLayout getLoadingLayoutProxy() {
        return getLoadingLayoutProxy(true, true);
    }

    public final ILoadingLayout getLoadingLayoutProxy(boolean z, boolean z2) {
        return createLoadingLayoutProxy(z, z2);
    }

    public final Mode getMode() {
        return this.mMode;
    }

    public final T getRefreshableView() {
        return this.mRefreshableView;
    }

    public final boolean getShowViewWhileRefreshing() {
        return this.mShowViewWhileRefreshing;
    }

    public final State getState() {
        return this.mState;
    }

    public final boolean isPullToRefreshEnabled() {
        return this.mMode.permitsPullToRefresh();
    }

    public final boolean isRefreshing() {
        State state = this.mState;
        return state == State.REFRESHING || state == State.MANUAL_REFRESHING;
    }

    @Override // android.view.ViewGroup
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        float f;
        float f2;
        if (!isPullToRefreshEnabled()) {
            return false;
        }
        int action = motionEvent.getAction();
        if (action == 3 || action == 1) {
            this.mIsBeingDragged = false;
            return false;
        } else if (action != 0 && this.mIsBeingDragged) {
            return true;
        } else {
            if (action != 0) {
                if (action == 2 && isReadyForPull()) {
                    float y = motionEvent.getY();
                    float x = motionEvent.getX();
                    if (AnonymousClass3.$SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Orientation[getPullToRefreshScrollDirection().ordinal()] == 1) {
                        f = x - this.mLastMotionX;
                        f2 = y - this.mLastMotionY;
                    } else {
                        f = y - this.mLastMotionY;
                        f2 = x - this.mLastMotionX;
                    }
                    float abs = Math.abs(f);
                    if (abs > this.mTouchSlop && (!this.mFilterTouchEvents || abs > Math.abs(f2))) {
                        if (this.mMode.showHeaderLoadingLayout() && f >= 1.0f && isReadyForPullStart()) {
                            this.mLastMotionY = y;
                            this.mLastMotionX = x;
                            this.mIsBeingDragged = true;
                            if (this.mMode == Mode.BOTH) {
                                this.mCurrentMode = Mode.PULL_FROM_START;
                            }
                        } else if (this.mMode.showFooterLoadingLayout() && f <= -1.0f && isReadyForPullEnd()) {
                            this.mLastMotionY = y;
                            this.mLastMotionX = x;
                            this.mIsBeingDragged = true;
                            if (this.mMode == Mode.BOTH) {
                                this.mCurrentMode = Mode.PULL_FROM_END;
                            }
                        }
                    }
                }
            } else if (isReadyForPull()) {
                float y2 = motionEvent.getY();
                this.mInitialMotionY = y2;
                this.mLastMotionY = y2;
                float x2 = motionEvent.getX();
                this.mInitialMotionX = x2;
                this.mLastMotionX = x2;
                this.mIsBeingDragged = false;
            }
            return this.mIsBeingDragged;
        }
    }

    public final void onRefreshComplete() {
        if (isRefreshing()) {
            setState(State.RESET, new boolean[0]);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:16:0x0022, code lost:
        if (r0 != 3) goto L17;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final boolean onTouchEvent(android.view.MotionEvent r5) {
        /*
            r4 = this;
            boolean r0 = r4.isPullToRefreshEnabled()
            r1 = 0
            if (r0 != 0) goto L8
            return r1
        L8:
            int r0 = r5.getAction()
            if (r0 != 0) goto L15
            int r0 = r5.getEdgeFlags()
            if (r0 == 0) goto L15
            return r1
        L15:
            int r0 = r5.getAction()
            r2 = 1
            if (r0 == 0) goto L69
            if (r0 == r2) goto L39
            r3 = 2
            if (r0 == r3) goto L25
            r5 = 3
            if (r0 == r5) goto L39
            goto L80
        L25:
            boolean r0 = r4.mIsBeingDragged
            if (r0 == 0) goto L80
            float r0 = r5.getY()
            r4.mLastMotionY = r0
            float r5 = r5.getX()
            r4.mLastMotionX = r5
            r4.pullEvent()
            return r2
        L39:
            boolean r5 = r4.mIsBeingDragged
            if (r5 == 0) goto L80
            r4.mIsBeingDragged = r1
            com.miui.gallery.hybrid.pulltorefresh.PullToRefreshBase$State r5 = r4.mState
            com.miui.gallery.hybrid.pulltorefresh.PullToRefreshBase$State r0 = com.miui.gallery.hybrid.pulltorefresh.PullToRefreshBase.State.RELEASE_TO_REFRESH
            if (r5 != r0) goto L57
            com.miui.gallery.hybrid.pulltorefresh.PullToRefreshBase$OnRefreshListener<T extends android.view.View> r5 = r4.mOnRefreshListener
            if (r5 != 0) goto L4d
            com.miui.gallery.hybrid.pulltorefresh.PullToRefreshBase$OnRefreshListener2<T extends android.view.View> r5 = r4.mOnRefreshListener2
            if (r5 == 0) goto L57
        L4d:
            com.miui.gallery.hybrid.pulltorefresh.PullToRefreshBase$State r5 = com.miui.gallery.hybrid.pulltorefresh.PullToRefreshBase.State.REFRESHING
            boolean[] r0 = new boolean[r2]
            r0[r1] = r2
            r4.setState(r5, r0)
            return r2
        L57:
            boolean r5 = r4.isRefreshing()
            if (r5 == 0) goto L61
            r4.smoothScrollTo(r1)
            return r2
        L61:
            com.miui.gallery.hybrid.pulltorefresh.PullToRefreshBase$State r5 = com.miui.gallery.hybrid.pulltorefresh.PullToRefreshBase.State.RESET
            boolean[] r0 = new boolean[r1]
            r4.setState(r5, r0)
            return r2
        L69:
            boolean r0 = r4.isReadyForPull()
            if (r0 == 0) goto L80
            float r0 = r5.getY()
            r4.mInitialMotionY = r0
            r4.mLastMotionY = r0
            float r5 = r5.getX()
            r4.mInitialMotionX = r5
            r4.mLastMotionX = r5
            return r2
        L80:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.hybrid.pulltorefresh.PullToRefreshBase.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public final void setScrollingWhileRefreshingEnabled(boolean z) {
        this.mScrollingWhileRefreshingEnabled = z;
    }

    public void setDisableScrollingWhileRefreshing(boolean z) {
        setScrollingWhileRefreshingEnabled(!z);
    }

    public final void setFilterTouchEvents(boolean z) {
        this.mFilterTouchEvents = z;
    }

    public void setLastUpdatedLabel(CharSequence charSequence) {
        getLoadingLayoutProxy().setLastUpdatedLabel(charSequence);
    }

    public void setLoadingDrawable(Drawable drawable) {
        getLoadingLayoutProxy().setLoadingDrawable(drawable);
    }

    @Override // android.view.View
    public void setLongClickable(boolean z) {
        getRefreshableView().setLongClickable(z);
    }

    public final void setMode(Mode mode) {
        if (mode != this.mMode) {
            this.mMode = mode;
            updateUIForMode();
        }
    }

    public void setOnPullEventListener(OnPullEventListener<T> onPullEventListener) {
        this.mOnPullEventListener = onPullEventListener;
    }

    public final void setOnRefreshListener(OnRefreshListener<T> onRefreshListener) {
        this.mOnRefreshListener = onRefreshListener;
        this.mOnRefreshListener2 = null;
    }

    public final void setOnRefreshListener(OnRefreshListener2<T> onRefreshListener2) {
        this.mOnRefreshListener2 = onRefreshListener2;
        this.mOnRefreshListener = null;
    }

    public void setPullLabel(CharSequence charSequence) {
        getLoadingLayoutProxy().setPullLabel(charSequence);
    }

    public final void setPullToRefreshEnabled(boolean z) {
        setMode(z ? Mode.getDefault() : Mode.DISABLED);
    }

    public final void setPullToRefreshOverScrollEnabled(boolean z) {
        this.mOverScrollEnabled = z;
    }

    public final void setRefreshing(boolean z) {
        if (!isRefreshing()) {
            setState(State.MANUAL_REFRESHING, z);
        }
    }

    public void setRefreshingLabel(CharSequence charSequence) {
        getLoadingLayoutProxy().setRefreshingLabel(charSequence);
    }

    public void setReleaseLabel(CharSequence charSequence) {
        setReleaseLabel(charSequence, Mode.BOTH);
    }

    public void setReleaseLabel(CharSequence charSequence, Mode mode) {
        getLoadingLayoutProxy(mode.showHeaderLoadingLayout(), mode.showFooterLoadingLayout()).setReleaseLabel(charSequence);
    }

    public void setScrollAnimationInterpolator(Interpolator interpolator) {
        this.mScrollAnimationInterpolator = interpolator;
    }

    public final void setShowViewWhileRefreshing(boolean z) {
        this.mShowViewWhileRefreshing = z;
    }

    public final void setState(State state, boolean... zArr) {
        this.mState = state;
        int i = AnonymousClass3.$SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$State[state.ordinal()];
        if (i == 1) {
            onReset();
        } else if (i == 2) {
            onPullToRefresh();
        } else if (i == 3) {
            onReleaseToRefresh();
        } else if (i == 4 || i == 5) {
            onRefreshing(zArr[0]);
        }
        OnPullEventListener<T> onPullEventListener = this.mOnPullEventListener;
        if (onPullEventListener != null) {
            onPullEventListener.onPullEvent(this, this.mState, this.mCurrentMode);
        }
    }

    public final void addViewInternal(View view, int i, ViewGroup.LayoutParams layoutParams) {
        super.addView(view, i, layoutParams);
    }

    public final void addViewInternal(View view, ViewGroup.LayoutParams layoutParams) {
        super.addView(view, -1, layoutParams);
    }

    public LoadingLayout createLoadingLayout(Context context, Mode mode, TypedArray typedArray) {
        LoadingLayout createLoadingLayout = this.mLoadingAnimationStyle.createLoadingLayout(context, mode, getPullToRefreshScrollDirection(), typedArray);
        createLoadingLayout.setVisibility(4);
        return createLoadingLayout;
    }

    public LoadingLayoutProxy createLoadingLayoutProxy(boolean z, boolean z2) {
        LoadingLayoutProxy loadingLayoutProxy = new LoadingLayoutProxy();
        if (z && this.mMode.showHeaderLoadingLayout()) {
            loadingLayoutProxy.addLayout(this.mHeaderLayout);
        }
        if (z2 && this.mMode.showFooterLoadingLayout()) {
            loadingLayoutProxy.addLayout(this.mFooterLayout);
        }
        return loadingLayoutProxy;
    }

    public final LoadingLayout getFooterLayout() {
        return this.mFooterLayout;
    }

    public final int getFooterSize() {
        return this.mFooterLayout.getContentSize();
    }

    public final LoadingLayout getHeaderLayout() {
        return this.mHeaderLayout;
    }

    public final int getHeaderSize() {
        return this.mHeaderLayout.getContentSize();
    }

    public FrameLayout getRefreshableViewWrapper() {
        return this.mRefreshableViewWrapper;
    }

    public void onPullToRefresh() {
        int i = AnonymousClass3.$SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Mode[this.mCurrentMode.ordinal()];
        if (i == 1) {
            this.mFooterLayout.pullToRefresh();
        } else if (i != 2) {
        } else {
            this.mHeaderLayout.pullToRefresh();
        }
    }

    public void onRefreshing(boolean z) {
        if (this.mMode.showHeaderLoadingLayout()) {
            this.mHeaderLayout.refreshing();
        }
        if (this.mMode.showFooterLoadingLayout()) {
            this.mFooterLayout.refreshing();
        }
        if (z) {
            if (this.mShowViewWhileRefreshing) {
                OnSmoothScrollFinishedListener onSmoothScrollFinishedListener = new OnSmoothScrollFinishedListener() { // from class: com.miui.gallery.hybrid.pulltorefresh.PullToRefreshBase.1
                    @Override // com.miui.gallery.hybrid.pulltorefresh.PullToRefreshBase.OnSmoothScrollFinishedListener
                    public void onSmoothScrollFinished() {
                        PullToRefreshBase.this.callRefreshListener();
                    }
                };
                int i = AnonymousClass3.$SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Mode[this.mCurrentMode.ordinal()];
                if (i == 1 || i == 3) {
                    smoothScrollTo(getFooterSize(), onSmoothScrollFinishedListener);
                    return;
                } else {
                    smoothScrollTo(-getHeaderSize(), onSmoothScrollFinishedListener);
                    return;
                }
            }
            smoothScrollTo(0);
            return;
        }
        callRefreshListener();
    }

    public void onReleaseToRefresh() {
        int i = AnonymousClass3.$SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Mode[this.mCurrentMode.ordinal()];
        if (i == 1) {
            this.mFooterLayout.releaseToRefresh();
        } else if (i != 2) {
        } else {
            this.mHeaderLayout.releaseToRefresh();
        }
    }

    public void onReset() {
        this.mIsBeingDragged = false;
        this.mLayoutVisibilityChangesEnabled = true;
        this.mHeaderLayout.reset();
        this.mFooterLayout.reset();
        smoothScrollTo(0);
    }

    @Override // android.view.View
    public final void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable instanceof Bundle) {
            Bundle bundle = (Bundle) parcelable;
            setMode(Mode.mapIntToValue(bundle.getInt("ptr_mode", 0)));
            this.mCurrentMode = Mode.mapIntToValue(bundle.getInt("ptr_current_mode", 0));
            this.mScrollingWhileRefreshingEnabled = bundle.getBoolean("ptr_disable_scrolling", false);
            this.mShowViewWhileRefreshing = bundle.getBoolean("ptr_show_refreshing_view", true);
            super.onRestoreInstanceState(bundle.getParcelable("ptr_super"));
            State mapIntToValue = State.mapIntToValue(bundle.getInt("ptr_state", 0));
            if (mapIntToValue == State.REFRESHING || mapIntToValue == State.MANUAL_REFRESHING) {
                setState(mapIntToValue, true);
            }
            onPtrRestoreInstanceState(bundle);
            return;
        }
        super.onRestoreInstanceState(parcelable);
    }

    @Override // android.view.View
    public final Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        onPtrSaveInstanceState(bundle);
        bundle.putInt("ptr_state", this.mState.getIntValue());
        bundle.putInt("ptr_mode", this.mMode.getIntValue());
        bundle.putInt("ptr_current_mode", this.mCurrentMode.getIntValue());
        bundle.putBoolean("ptr_disable_scrolling", this.mScrollingWhileRefreshingEnabled);
        bundle.putBoolean("ptr_show_refreshing_view", this.mShowViewWhileRefreshing);
        bundle.putParcelable("ptr_super", super.onSaveInstanceState());
        return bundle;
    }

    @Override // android.view.View
    public final void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        refreshLoadingViewsSize();
    }

    public final void refreshLoadingViewsSize() {
        int maximumPullScroll = (int) (getMaximumPullScroll() * 1.2f);
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int i = AnonymousClass3.$SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Orientation[getPullToRefreshScrollDirection().ordinal()];
        if (i == 1) {
            if (this.mMode.showHeaderLoadingLayout()) {
                this.mHeaderLayout.setWidth(maximumPullScroll);
                paddingLeft = -maximumPullScroll;
            } else {
                paddingLeft = 0;
            }
            if (this.mMode.showFooterLoadingLayout()) {
                this.mFooterLayout.setWidth(maximumPullScroll);
                paddingRight = -maximumPullScroll;
            } else {
                paddingRight = 0;
            }
        } else if (i == 2) {
            if (this.mMode.showHeaderLoadingLayout()) {
                this.mHeaderLayout.setHeight(maximumPullScroll);
                paddingTop = -maximumPullScroll;
            } else {
                paddingTop = 0;
            }
            if (this.mMode.showFooterLoadingLayout()) {
                this.mFooterLayout.setHeight(maximumPullScroll);
                paddingBottom = -maximumPullScroll;
            } else {
                paddingBottom = 0;
            }
        }
        setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
    }

    public final void setHeaderScroll(int i) {
        int maximumPullScroll = getMaximumPullScroll();
        int min = Math.min(maximumPullScroll, Math.max(-maximumPullScroll, i));
        if (this.mLayoutVisibilityChangesEnabled) {
            if (min < 0) {
                this.mHeaderLayout.setVisibility(0);
            } else if (min > 0) {
                this.mFooterLayout.setVisibility(0);
            } else {
                this.mHeaderLayout.setVisibility(4);
                this.mFooterLayout.setVisibility(4);
            }
        }
        int i2 = AnonymousClass3.$SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Orientation[getPullToRefreshScrollDirection().ordinal()];
        if (i2 == 1) {
            scrollTo(min, 0);
        } else if (i2 != 2) {
        } else {
            scrollTo(0, min);
        }
    }

    public final void smoothScrollTo(int i) {
        smoothScrollTo(i, getPullToRefreshScrollDuration());
    }

    public final void smoothScrollTo(int i, OnSmoothScrollFinishedListener onSmoothScrollFinishedListener) {
        smoothScrollTo(i, getPullToRefreshScrollDuration(), 0L, onSmoothScrollFinishedListener);
    }

    public void updateUIForMode() {
        LinearLayout.LayoutParams loadingLayoutLayoutParams = getLoadingLayoutLayoutParams();
        if (this == this.mHeaderLayout.getParent()) {
            removeView(this.mHeaderLayout);
        }
        if (this.mMode.showHeaderLoadingLayout()) {
            addViewInternal(this.mHeaderLayout, 0, loadingLayoutLayoutParams);
        }
        if (this == this.mFooterLayout.getParent()) {
            removeView(this.mFooterLayout);
        }
        if (this.mMode.showFooterLoadingLayout()) {
            addViewInternal(this.mFooterLayout, loadingLayoutLayoutParams);
        }
        refreshLoadingViewsSize();
        Mode mode = this.mMode;
        if (mode == Mode.BOTH) {
            mode = Mode.PULL_FROM_START;
        }
        this.mCurrentMode = mode;
    }

    public final void addRefreshableView(Context context, T t) {
        FrameLayout frameLayout = new FrameLayout(context);
        this.mRefreshableViewWrapper = frameLayout;
        frameLayout.addView(t, -1, -1);
        addViewInternal(this.mRefreshableViewWrapper, new LinearLayout.LayoutParams(-1, -1));
    }

    public final void callRefreshListener() {
        OnRefreshListener<T> onRefreshListener = this.mOnRefreshListener;
        if (onRefreshListener != null) {
            onRefreshListener.onRefresh(this);
            return;
        }
        OnRefreshListener2<T> onRefreshListener2 = this.mOnRefreshListener2;
        if (onRefreshListener2 == null) {
            return;
        }
        Mode mode = this.mCurrentMode;
        if (mode == Mode.PULL_FROM_START) {
            onRefreshListener2.onPullDownToRefresh(this);
        } else if (mode != Mode.PULL_FROM_END) {
        } else {
            onRefreshListener2.onPullUpToRefresh(this);
        }
    }

    public final void init(Context context, AttributeSet attributeSet) {
        Drawable drawable;
        if (AnonymousClass3.$SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Orientation[getPullToRefreshScrollDirection().ordinal()] == 1) {
            setOrientation(0);
        } else {
            setOrientation(1);
        }
        setGravity(17);
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.PullToRefresh);
        if (obtainStyledAttributes.hasValue(13)) {
            this.mMode = Mode.mapIntToValue(obtainStyledAttributes.getInteger(13, 0));
        }
        if (obtainStyledAttributes.hasValue(1)) {
            this.mLoadingAnimationStyle = AnimationStyle.mapIntToValue(obtainStyledAttributes.getInteger(1, 0));
        }
        T createRefreshableView = createRefreshableView(context, attributeSet);
        this.mRefreshableView = createRefreshableView;
        addRefreshableView(context, createRefreshableView);
        this.mHeaderLayout = createLoadingLayout(context, Mode.PULL_FROM_START, obtainStyledAttributes);
        this.mFooterLayout = createLoadingLayout(context, Mode.PULL_FROM_END, obtainStyledAttributes);
        if (obtainStyledAttributes.hasValue(15)) {
            Drawable drawable2 = obtainStyledAttributes.getDrawable(15);
            if (drawable2 != null) {
                this.mRefreshableView.setBackgroundDrawable(drawable2);
            }
        } else if (obtainStyledAttributes.hasValue(0) && (drawable = obtainStyledAttributes.getDrawable(0)) != null) {
            this.mRefreshableView.setBackgroundDrawable(drawable);
        }
        if (obtainStyledAttributes.hasValue(14)) {
            this.mOverScrollEnabled = obtainStyledAttributes.getBoolean(14, true);
        }
        if (obtainStyledAttributes.hasValue(17)) {
            this.mScrollingWhileRefreshingEnabled = obtainStyledAttributes.getBoolean(17, false);
        }
        if (obtainStyledAttributes.hasValue(12)) {
            this.mMaximumPullScroll = obtainStyledAttributes.getDimensionPixelOffset(12, 0);
        }
        handleStyledAttributes(obtainStyledAttributes);
        obtainStyledAttributes.recycle();
        updateUIForMode();
    }

    public final boolean isReadyForPull() {
        int i = AnonymousClass3.$SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Mode[this.mMode.ordinal()];
        if (i != 1) {
            if (i == 2) {
                return isReadyForPullStart();
            }
            if (i != 4) {
                return false;
            }
            return isReadyForPullEnd() || isReadyForPullStart();
        }
        return isReadyForPullEnd();
    }

    public final void pullEvent() {
        float f;
        float f2;
        int round;
        int footerSize;
        if (AnonymousClass3.$SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Orientation[getPullToRefreshScrollDirection().ordinal()] == 1) {
            f = this.mInitialMotionX;
            f2 = this.mLastMotionX;
        } else {
            f = this.mInitialMotionY;
            f2 = this.mLastMotionY;
        }
        int[] iArr = AnonymousClass3.$SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Mode;
        if (iArr[this.mCurrentMode.ordinal()] == 1) {
            round = Math.round(Math.max(f - f2, 0.0f) / 2.0f);
            footerSize = getFooterSize();
        } else {
            round = Math.round(Math.min(f - f2, 0.0f) / 2.0f);
            footerSize = getHeaderSize();
        }
        setHeaderScroll(round);
        if (round == 0 || isRefreshing()) {
            return;
        }
        float abs = Math.abs(round) / footerSize;
        if (iArr[this.mCurrentMode.ordinal()] == 1) {
            this.mFooterLayout.onPull(abs);
        } else {
            this.mHeaderLayout.onPull(abs);
        }
        State state = this.mState;
        State state2 = State.PULL_TO_REFRESH;
        if (state != state2 && footerSize >= Math.abs(round)) {
            setState(state2, new boolean[0]);
        } else if (this.mState != state2 || footerSize >= Math.abs(round)) {
        } else {
            setState(State.RELEASE_TO_REFRESH, new boolean[0]);
        }
    }

    private LinearLayout.LayoutParams getLoadingLayoutLayoutParams() {
        if (AnonymousClass3.$SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Orientation[getPullToRefreshScrollDirection().ordinal()] == 1) {
            return new LinearLayout.LayoutParams(-2, -1);
        }
        return new LinearLayout.LayoutParams(-1, -2);
    }

    private int getMaximumPullScroll() {
        if (AnonymousClass3.$SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Orientation[getPullToRefreshScrollDirection().ordinal()] == 1) {
            return Math.round(getWidth() / 2.0f);
        }
        int i = this.mMaximumPullScroll;
        return i != 0 ? i : Math.round(getHeight() / 2.0f);
    }

    public final void smoothScrollTo(int i, long j) {
        smoothScrollTo(i, j, 0L, null);
    }

    public final void smoothScrollTo(int i, long j, long j2, OnSmoothScrollFinishedListener onSmoothScrollFinishedListener) {
        int scrollX;
        PullToRefreshBase<T>.SmoothScrollRunnable smoothScrollRunnable = this.mCurrentSmoothScrollRunnable;
        if (smoothScrollRunnable != null) {
            smoothScrollRunnable.stop();
        }
        if (AnonymousClass3.$SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Orientation[getPullToRefreshScrollDirection().ordinal()] == 1) {
            scrollX = getScrollX();
        } else {
            scrollX = getScrollY();
        }
        int i2 = scrollX;
        if (i2 != i) {
            if (this.mScrollAnimationInterpolator == null) {
                this.mScrollAnimationInterpolator = new DecelerateInterpolator();
            }
            PullToRefreshBase<T>.SmoothScrollRunnable smoothScrollRunnable2 = new SmoothScrollRunnable(i2, i, j, onSmoothScrollFinishedListener);
            this.mCurrentSmoothScrollRunnable = smoothScrollRunnable2;
            if (j2 > 0) {
                postDelayed(smoothScrollRunnable2, j2);
            } else {
                post(smoothScrollRunnable2);
            }
        }
    }

    /* loaded from: classes2.dex */
    public enum AnimationStyle {
        ROTATE,
        FLIP;

        public static AnimationStyle getDefault() {
            return ROTATE;
        }

        public static AnimationStyle mapIntToValue(int i) {
            if (i != 1) {
                return ROTATE;
            }
            return FLIP;
        }

        public LoadingLayout createLoadingLayout(Context context, Mode mode, Orientation orientation, TypedArray typedArray) {
            int i = AnonymousClass3.$SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$AnimationStyle[ordinal()];
            return new FlipLoadingLayout(context, mode, orientation, typedArray);
        }
    }

    /* renamed from: com.miui.gallery.hybrid.pulltorefresh.PullToRefreshBase$3  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass3 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$AnimationStyle;
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Mode;
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Orientation;
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$State;

        static {
            int[] iArr = new int[AnimationStyle.values().length];
            $SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$AnimationStyle = iArr;
            try {
                iArr[AnimationStyle.FLIP.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            int[] iArr2 = new int[Mode.values().length];
            $SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Mode = iArr2;
            try {
                iArr2[Mode.PULL_FROM_END.ordinal()] = 1;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Mode[Mode.PULL_FROM_START.ordinal()] = 2;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Mode[Mode.MANUAL_REFRESH_ONLY.ordinal()] = 3;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Mode[Mode.BOTH.ordinal()] = 4;
            } catch (NoSuchFieldError unused5) {
            }
            int[] iArr3 = new int[State.values().length];
            $SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$State = iArr3;
            try {
                iArr3[State.RESET.ordinal()] = 1;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$State[State.PULL_TO_REFRESH.ordinal()] = 2;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$State[State.RELEASE_TO_REFRESH.ordinal()] = 3;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$State[State.REFRESHING.ordinal()] = 4;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$State[State.MANUAL_REFRESHING.ordinal()] = 5;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$State[State.OVERSCROLLING.ordinal()] = 6;
            } catch (NoSuchFieldError unused11) {
            }
            int[] iArr4 = new int[Orientation.values().length];
            $SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Orientation = iArr4;
            try {
                iArr4[Orientation.HORIZONTAL.ordinal()] = 1;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$com$miui$gallery$hybrid$pulltorefresh$PullToRefreshBase$Orientation[Orientation.VERTICAL.ordinal()] = 2;
            } catch (NoSuchFieldError unused13) {
            }
        }
    }

    /* loaded from: classes2.dex */
    public enum Mode {
        DISABLED(0),
        PULL_FROM_START(1),
        PULL_FROM_END(2),
        BOTH(3),
        MANUAL_REFRESH_ONLY(4);
        
        public static Mode PULL_DOWN_TO_REFRESH;
        public static Mode PULL_UP_TO_REFRESH;
        private final int mIntValue;

        static {
            Mode mode = PULL_FROM_START;
            Mode mode2 = PULL_FROM_END;
            PULL_DOWN_TO_REFRESH = mode;
            PULL_UP_TO_REFRESH = mode2;
        }

        public static Mode mapIntToValue(int i) {
            Mode[] values;
            for (Mode mode : values()) {
                if (i == mode.getIntValue()) {
                    return mode;
                }
            }
            return getDefault();
        }

        public static Mode getDefault() {
            return PULL_FROM_START;
        }

        Mode(int i) {
            this.mIntValue = i;
        }

        public boolean permitsPullToRefresh() {
            return (this == DISABLED || this == MANUAL_REFRESH_ONLY) ? false : true;
        }

        public boolean showHeaderLoadingLayout() {
            return this == PULL_FROM_START || this == BOTH;
        }

        public boolean showFooterLoadingLayout() {
            return this == PULL_FROM_END || this == BOTH || this == MANUAL_REFRESH_ONLY;
        }

        public int getIntValue() {
            return this.mIntValue;
        }
    }

    /* loaded from: classes2.dex */
    public enum State {
        RESET(0),
        PULL_TO_REFRESH(1),
        RELEASE_TO_REFRESH(2),
        REFRESHING(8),
        MANUAL_REFRESHING(9),
        OVERSCROLLING(16);
        
        private final int mIntValue;

        public static State mapIntToValue(int i) {
            State[] values;
            for (State state : values()) {
                if (i == state.getIntValue()) {
                    return state;
                }
            }
            return RESET;
        }

        State(int i) {
            this.mIntValue = i;
        }

        public int getIntValue() {
            return this.mIntValue;
        }
    }

    /* loaded from: classes2.dex */
    public final class SmoothScrollRunnable implements Runnable {
        public final long mDuration;
        public final Interpolator mInterpolator;
        public OnSmoothScrollFinishedListener mListener;
        public final int mScrollFromY;
        public final int mScrollToY;
        public boolean mContinueRunning = true;
        public long mStartTime = -1;
        public int mCurrentY = -1;

        public SmoothScrollRunnable(int i, int i2, long j, OnSmoothScrollFinishedListener onSmoothScrollFinishedListener) {
            this.mScrollFromY = i;
            this.mScrollToY = i2;
            this.mInterpolator = PullToRefreshBase.this.mScrollAnimationInterpolator;
            this.mDuration = j;
            this.mListener = onSmoothScrollFinishedListener;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (this.mStartTime == -1) {
                this.mStartTime = System.currentTimeMillis();
            } else {
                int round = this.mScrollFromY - Math.round((this.mScrollFromY - this.mScrollToY) * this.mInterpolator.getInterpolation(((float) Math.max(Math.min(((System.currentTimeMillis() - this.mStartTime) * 1000) / this.mDuration, 1000L), 0L)) / 1000.0f));
                this.mCurrentY = round;
                PullToRefreshBase.this.setHeaderScroll(round);
            }
            if (this.mContinueRunning && this.mScrollToY != this.mCurrentY) {
                ViewCompat.postOnAnimation(PullToRefreshBase.this, this);
                return;
            }
            OnSmoothScrollFinishedListener onSmoothScrollFinishedListener = this.mListener;
            if (onSmoothScrollFinishedListener == null) {
                return;
            }
            onSmoothScrollFinishedListener.onSmoothScrollFinished();
        }

        public void stop() {
            this.mContinueRunning = false;
            PullToRefreshBase.this.removeCallbacks(this);
        }
    }
}
