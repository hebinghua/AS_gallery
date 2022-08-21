package miuix.appcompat.internal.app.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.graphics.Rect;
import android.os.Looper;
import android.os.MessageQueue;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import miuix.animation.Folme;
import miuix.animation.ITouchStyle;
import miuix.animation.base.AnimConfig;
import miuix.animation.utils.EaseManager;
import miuix.appcompat.R$dimen;
import miuix.appcompat.R$id;
import miuix.internal.util.ActionBarUtils;
import miuix.internal.util.DeviceHelper;
import miuix.internal.util.ViewUtils;
import miuix.view.ActionModeAnimationListener;
import miuix.view.CompatViewMethod;
import miuix.view.inputmethod.InputMethodHelper;
import miuix.viewpager.widget.ViewPager;

/* loaded from: classes3.dex */
public class SearchActionModeView extends FrameLayout implements Animator.AnimatorListener, ActionModeView, TextWatcher, View.OnClickListener, MessageQueue.IdleHandler {
    public ActionBarContainer mActionBarContainer;
    public int mActionBarLocation;
    public int mActionBarTopMargin;
    public ActionBarView mActionBarView;
    public WeakReference<View> mAnchorView;
    public boolean mAnimateToVisible;
    public WeakReference<View> mAnimateView;
    public int mAnimateViewTranslationYLength;
    public int mAnimateViewTranslationYStart;
    public boolean mAnimationCanceled;
    public List<ActionModeAnimationListener> mAnimationListeners;
    public float mAnimationProgress;
    public int mContentOriginPaddingBottom;
    public int mContentOriginPaddingTop;
    public ObjectAnimator mCurrentAnimation;
    public View mDimView;
    public int mInputPaddingRight;
    public int mInputPaddingTop;
    public EditText mInputView;
    public int mInputViewTranslationYLength;
    public int mInputViewTranslationYStart;
    public int[] mLocation;
    public int mOriginalPaddingTop;
    public boolean mRequestAnimation;
    public WeakReference<View> mResultView;
    public int mResultViewOriginMarginBottom;
    public int mResultViewOriginMarginTop;
    public boolean mResultViewSet;
    public ViewGroup mSearchContainer;
    public int mSearchViewHeight;
    public ActionBarContainer mSplitActionBarContainer;
    public int mStatusBarPaddingTop;
    public TextView mTextCancel;
    public int mTextLengthBeforeChanged;

    public static /* synthetic */ void $r8$lambda$ztP5N15TNntg240c7ykAiDl3nPM(SearchActionModeView searchActionModeView) {
        searchActionModeView.lambda$onAnimationEnd$0();
    }

    @Override // miuix.appcompat.internal.app.widget.ActionModeView
    public void initForMode(ActionMode actionMode) {
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationRepeat(Animator animator) {
    }

    @Override // android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return true;
    }

    public SearchActionModeView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mLocation = new int[2];
        this.mActionBarLocation = Integer.MAX_VALUE;
        setAlpha(0.0f);
        this.mSearchViewHeight = context.getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_search_view_default_height);
        this.mInputPaddingTop = context.getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_search_mode_bg_padding_top);
        this.mInputPaddingRight = shouldHideCancelText() ? 0 : context.getResources().getDimensionPixelSize(R$dimen.miuix_appcompat_search_mode_bg_padding);
    }

    public final boolean shouldHideCancelText() {
        String language = Locale.getDefault().getLanguage();
        return !"zh".equalsIgnoreCase(language) && !"en".equalsIgnoreCase(language);
    }

    @Override // miuix.appcompat.internal.app.widget.ActionModeView
    public void animateToVisibility(boolean z) {
        if (this.mAnimateToVisible == z) {
            this.mRequestAnimation = false;
            return;
        }
        pollViews();
        this.mAnimateToVisible = z;
        this.mCurrentAnimation = makeAnimation();
        createAnimationListeners();
        if (z) {
            setOverlayMode(true);
        }
        notifyAnimationStart(z);
        if (shouldAnimateContent()) {
            requestLayout();
            this.mRequestAnimation = true;
        } else {
            this.mCurrentAnimation.start();
        }
        if (this.mAnimateToVisible) {
            return;
        }
        this.mInputView.clearFocus();
        ((InputMethodManager) getContext().getSystemService("input_method")).hideSoftInputFromWindow(this.mInputView.getWindowToken(), 0);
    }

    public void setOnBackClickListener(View.OnClickListener onClickListener) {
        this.mTextCancel.setOnClickListener(onClickListener);
    }

    public void setAnchorView(View view) {
        if (view != null) {
            this.mAnchorView = new WeakReference<>(view);
        }
    }

    public void setAnimateView(View view) {
        if (view != null) {
            this.mAnimateView = new WeakReference<>(view);
        }
    }

    @Override // miuix.appcompat.internal.app.widget.ActionModeView
    public void addAnimationListener(ActionModeAnimationListener actionModeAnimationListener) {
        if (actionModeAnimationListener == null) {
            return;
        }
        if (this.mAnimationListeners == null) {
            this.mAnimationListeners = new ArrayList();
        }
        this.mAnimationListeners.add(actionModeAnimationListener);
    }

    public void setResultView(View view) {
        if (view != null) {
            this.mResultView = new WeakReference<>(view);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            if (!(layoutParams instanceof ViewGroup.MarginLayoutParams)) {
                return;
            }
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
            this.mResultViewOriginMarginTop = marginLayoutParams.topMargin;
            this.mResultViewOriginMarginBottom = marginLayoutParams.bottomMargin;
            this.mResultViewSet = true;
        }
    }

    public EditText getSearchInput() {
        return this.mInputView;
    }

    @Override // miuix.appcompat.internal.app.widget.ActionModeView
    public void closeMode() {
        ObjectAnimator objectAnimator = this.mCurrentAnimation;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
    }

    @Override // miuix.appcompat.internal.app.widget.ActionModeView
    public void killMode() {
        finishAnimation();
        ViewGroup viewGroup = (ViewGroup) getParent();
        if (viewGroup != null) {
            viewGroup.removeView(this);
        }
        this.mActionBarContainer = null;
        this.mActionBarView = null;
        List<ActionModeAnimationListener> list = this.mAnimationListeners;
        if (list != null) {
            list.clear();
            this.mAnimationListeners = null;
        }
        this.mSplitActionBarContainer = null;
    }

    public float getAnimationProgress() {
        return this.mAnimationProgress;
    }

    public void setAnimationProgress(float f) {
        this.mAnimationProgress = f;
        notifyAnimationUpdate(this.mAnimateToVisible, f);
    }

    public void finishAnimation() {
        ObjectAnimator objectAnimator = this.mCurrentAnimation;
        if (objectAnimator != null) {
            objectAnimator.cancel();
            this.mCurrentAnimation = null;
        }
    }

    public ObjectAnimator makeAnimation() {
        ObjectAnimator objectAnimator = this.mCurrentAnimation;
        if (objectAnimator != null) {
            objectAnimator.cancel();
            this.mCurrentAnimation = null;
            removeIdleHandler();
        }
        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, "AnimationProgress", 0.0f, 1.0f);
        ofFloat.addListener(this);
        ofFloat.setDuration(DeviceHelper.isFeatureWholeAnim() ? 400L : 0L);
        ofFloat.setInterpolator(obtainInterpolator());
        return ofFloat;
    }

    public TimeInterpolator obtainInterpolator() {
        EaseManager.InterpolateEaseStyle interpolateEaseStyle = new EaseManager.InterpolateEaseStyle(0, new float[0]);
        interpolateEaseStyle.setFactors(0.98f, 0.75f);
        return EaseManager.getInterpolator(interpolateEaseStyle);
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mTextCancel = (TextView) findViewById(R$id.search_text_cancel);
        ViewGroup viewGroup = (ViewGroup) findViewById(R$id.search_container);
        this.mSearchContainer = viewGroup;
        Folme.useAt(viewGroup).touch().setScale(1.0f, new ITouchStyle.TouchType[0]).handleTouchOf(this.mSearchContainer, new AnimConfig[0]);
        CompatViewMethod.setForceDarkAllowed(this.mSearchContainer, false);
        if (shouldHideCancelText()) {
            this.mTextCancel.setVisibility(8);
            if (getPaddingEnd() == 0) {
                setPaddingRelative(getPaddingStart(), getPaddingTop(), getPaddingStart(), getPaddingBottom());
            }
        }
        this.mInputView = (EditText) findViewById(16908297);
        this.mOriginalPaddingTop = getPaddingTop();
        View contentView = getContentView();
        if (contentView != null) {
            this.mContentOriginPaddingTop = contentView.getPaddingTop();
            this.mContentOriginPaddingBottom = contentView.getPaddingBottom();
        }
    }

    public void rePaddingAndRelayout(Rect rect) {
        int i = this.mStatusBarPaddingTop;
        int i2 = rect.top;
        if (i != i2) {
            setStatusBarPaddingTop(i2);
            setPadding(getPaddingLeft(), this.mOriginalPaddingTop + this.mStatusBarPaddingTop + this.mInputPaddingTop, getPaddingRight(), getPaddingBottom());
            getLayoutParams().height = this.mSearchViewHeight + this.mStatusBarPaddingTop;
            requestLayout();
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.mRequestAnimation) {
            WeakReference<View> weakReference = this.mAnimateView;
            View view = weakReference != null ? weakReference.get() : null;
            if (this.mAnimateToVisible && shouldAnimateContent() && view != null) {
                view.setTranslationY(this.mAnimateViewTranslationYStart);
            }
            queueIdleHandler();
            this.mRequestAnimation = false;
        }
    }

    public ActionBarContainer getActionBarContainer() {
        if (this.mActionBarContainer == null) {
            ActionBarOverlayLayout actionBarOverlayLayout = (ActionBarOverlayLayout) ActionBarUtils.getActionBarOverlayLayout(this);
            if (actionBarOverlayLayout != null) {
                int i = 0;
                while (true) {
                    if (i >= actionBarOverlayLayout.getChildCount()) {
                        break;
                    }
                    View childAt = actionBarOverlayLayout.getChildAt(i);
                    if (childAt.getId() == R$id.action_bar_container && (childAt instanceof ActionBarContainer)) {
                        this.mActionBarContainer = (ActionBarContainer) childAt;
                        break;
                    }
                    i++;
                }
            }
            ActionBarContainer actionBarContainer = this.mActionBarContainer;
            if (actionBarContainer != null) {
                int i2 = ((ViewGroup.MarginLayoutParams) actionBarContainer.getLayoutParams()).topMargin;
                this.mActionBarTopMargin = i2;
                if (i2 > 0) {
                    setPadding(getPaddingLeft(), this.mOriginalPaddingTop + this.mActionBarTopMargin, getPaddingRight(), getPaddingBottom());
                }
            }
        }
        return this.mActionBarContainer;
    }

    public ActionBarContainer getSplitActionBarContainer() {
        ViewGroup actionBarOverlayLayout;
        if (this.mSplitActionBarContainer == null && (actionBarOverlayLayout = ActionBarUtils.getActionBarOverlayLayout(this)) != null) {
            int i = 0;
            while (true) {
                if (i >= actionBarOverlayLayout.getChildCount()) {
                    break;
                }
                View childAt = actionBarOverlayLayout.getChildAt(i);
                if (childAt.getId() == R$id.split_action_bar && (childAt instanceof ActionBarContainer)) {
                    this.mSplitActionBarContainer = (ActionBarContainer) childAt;
                    break;
                }
                i++;
            }
        }
        return this.mSplitActionBarContainer;
    }

    public ActionBarView getActionBarView() {
        ViewGroup actionBarOverlayLayout;
        if (this.mActionBarView == null && (actionBarOverlayLayout = ActionBarUtils.getActionBarOverlayLayout(this)) != null) {
            this.mActionBarView = (ActionBarView) actionBarOverlayLayout.findViewById(R$id.action_bar);
        }
        return this.mActionBarView;
    }

    public View getDimView() {
        ViewGroup actionBarOverlayLayout;
        if (this.mDimView == null && (actionBarOverlayLayout = ActionBarUtils.getActionBarOverlayLayout(this)) != null) {
            ViewStub viewStub = (ViewStub) actionBarOverlayLayout.findViewById(R$id.search_mask_vs);
            if (viewStub != null) {
                this.mDimView = viewStub.inflate();
            } else {
                this.mDimView = actionBarOverlayLayout.findViewById(R$id.search_mask);
            }
        }
        return this.mDimView;
    }

    public void pollViews() {
        getActionBarView();
        getActionBarContainer();
        getSplitActionBarContainer();
    }

    public void setOverlayMode(boolean z) {
        ((ActionBarOverlayLayout) ActionBarUtils.getActionBarOverlayLayout(this)).setOverlayMode(z);
    }

    public ViewPager getViewPager() {
        ActionBarOverlayLayout actionBarOverlayLayout = (ActionBarOverlayLayout) ActionBarUtils.getActionBarOverlayLayout(this);
        if (((ActionBarImpl) actionBarOverlayLayout.getActionBar()).isFragmentViewPagerMode()) {
            return (ViewPager) actionBarOverlayLayout.findViewById(R$id.view_pager);
        }
        return null;
    }

    public void setResultViewMargin(boolean z) {
        int i;
        int i2;
        WeakReference<View> weakReference = this.mResultView;
        View view = weakReference != null ? weakReference.get() : null;
        if (view == null || !this.mResultViewSet) {
            return;
        }
        if (z) {
            i = (getMeasuredHeight() - this.mStatusBarPaddingTop) - this.mActionBarTopMargin;
            i2 = 0;
        } else {
            i = this.mResultViewOriginMarginTop;
            i2 = this.mResultViewOriginMarginBottom;
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        marginLayoutParams.topMargin = i;
        marginLayoutParams.bottomMargin = i2;
        view.setLayoutParams(marginLayoutParams);
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationStart(Animator animator) {
        this.mAnimationCanceled = false;
        if (this.mAnimateToVisible) {
            setAlpha(1.0f);
            return;
        }
        View tabContainer = getActionBarContainer().getTabContainer();
        if (tabContainer == null) {
            return;
        }
        tabContainer.setVisibility(0);
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationEnd(Animator animator) {
        ActionBarContainer actionBarContainer;
        if (this.mAnimationCanceled) {
            return;
        }
        View view = null;
        this.mCurrentAnimation = null;
        notifyAnimationEnd(this.mAnimateToVisible);
        if (this.mAnimateToVisible) {
            InputMethodHelper.getInstance(getContext()).showKeyBoard(this.mInputView);
        } else {
            InputMethodHelper.getInstance(getContext()).hideKeyBoard(this.mInputView);
        }
        if (Settings.Global.getFloat(getContext().getContentResolver(), "animator_duration_scale", 1.0f) != 0.0f) {
            setResultViewMargin(this.mAnimateToVisible);
        } else {
            post(new Runnable() { // from class: miuix.appcompat.internal.app.widget.SearchActionModeView$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SearchActionModeView.$r8$lambda$ztP5N15TNntg240c7ykAiDl3nPM(SearchActionModeView.this);
                }
            });
        }
        if (this.mAnimateToVisible && (actionBarContainer = this.mActionBarContainer) != null && actionBarContainer.isBlurEnable()) {
            setContentViewTranslation(-this.mContentOriginPaddingTop);
        } else {
            setContentViewTranslation(0);
            setContentViewPadding(this.mAnimateToVisible ? this.mStatusBarPaddingTop : 0, 0);
        }
        if (this.mAnimateToVisible) {
            return;
        }
        setOverlayMode(false);
        WeakReference<View> weakReference = this.mAnchorView;
        if (weakReference != null) {
            view = weakReference.get();
        }
        if (view != null) {
            view.setAlpha(1.0f);
        }
        setAlpha(0.0f);
        killMode();
    }

    public /* synthetic */ void lambda$onAnimationEnd$0() {
        setResultViewMargin(this.mAnimateToVisible);
    }

    @Override // android.animation.Animator.AnimatorListener
    public void onAnimationCancel(Animator animator) {
        this.mAnimationCanceled = true;
    }

    @Override // android.text.TextWatcher
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        this.mTextLengthBeforeChanged = charSequence == null ? 0 : charSequence.length();
    }

    @Override // android.text.TextWatcher
    public void afterTextChanged(Editable editable) {
        View view;
        if ((editable == null ? 0 : editable.length()) == 0) {
            View view2 = this.mDimView;
            if (view2 != null) {
                view2.setVisibility(0);
            }
            InputMethodHelper.getInstance(getContext()).showKeyBoard(this.mInputView);
        } else if (this.mTextLengthBeforeChanged != 0 || (view = this.mDimView) == null) {
        } else {
            view.setVisibility(8);
        }
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R$id.search_mask) {
            this.mTextCancel.performClick();
        }
    }

    public void setContentViewTranslation(int i) {
        View contentView = getContentView();
        if (contentView != null) {
            contentView.setTranslationY(i);
        }
    }

    public void setContentViewPadding(int i, int i2) {
        View contentView = getContentView();
        if (contentView != null) {
            contentView.setPadding(contentView.getPaddingLeft(), i + this.mContentOriginPaddingTop, contentView.getPaddingRight(), i2 + this.mContentOriginPaddingBottom);
        }
    }

    public View getContentView() {
        ViewGroup actionBarOverlayLayout = ActionBarUtils.getActionBarOverlayLayout(this);
        if (actionBarOverlayLayout != null) {
            return actionBarOverlayLayout.findViewById(16908290);
        }
        return null;
    }

    @Override // android.os.MessageQueue.IdleHandler
    public boolean queueIdle() {
        this.mCurrentAnimation.start();
        return false;
    }

    public final void queueIdleHandler() {
        removeIdleHandler();
        getMessageQueue().addIdleHandler(this);
    }

    public final void removeIdleHandler() {
        getMessageQueue().removeIdleHandler(this);
    }

    private MessageQueue getMessageQueue() {
        return Looper.myQueue();
    }

    public void createAnimationListeners() {
        if (this.mAnimationListeners == null) {
            this.mAnimationListeners = new ArrayList();
        }
        this.mAnimationListeners.add(new SearchViewAnimationProcessor());
        if (shouldAnimateContent()) {
            this.mAnimationListeners.add(new ContentViewAnimationProcessor());
            this.mAnimationListeners.add(new ActionBarAnimationProcessor());
            this.mAnimationListeners.add(new SplitActionBarAnimationProcessor());
        }
        if (getDimView() != null) {
            this.mAnimationListeners.add(new DimViewAnimationProcessor());
        }
    }

    public void notifyAnimationStart(boolean z) {
        List<ActionModeAnimationListener> list = this.mAnimationListeners;
        if (list == null) {
            return;
        }
        for (ActionModeAnimationListener actionModeAnimationListener : list) {
            actionModeAnimationListener.onStart(z);
        }
    }

    public void notifyAnimationUpdate(boolean z, float f) {
        List<ActionModeAnimationListener> list = this.mAnimationListeners;
        if (list == null) {
            return;
        }
        for (ActionModeAnimationListener actionModeAnimationListener : list) {
            actionModeAnimationListener.onUpdate(z, f);
        }
    }

    public void notifyAnimationEnd(boolean z) {
        List<ActionModeAnimationListener> list = this.mAnimationListeners;
        if (list == null) {
            return;
        }
        for (ActionModeAnimationListener actionModeAnimationListener : list) {
            actionModeAnimationListener.onStop(z);
        }
    }

    public void setStatusBarPaddingTop(int i) {
        this.mStatusBarPaddingTop = i;
    }

    public final boolean shouldAnimateContent() {
        return (this.mAnchorView == null || this.mAnimateView == null) ? false : true;
    }

    /* loaded from: classes3.dex */
    public class SearchViewAnimationProcessor implements ActionModeAnimationListener {
        public SearchViewAnimationProcessor() {
            SearchActionModeView.this = r1;
        }

        @Override // miuix.view.ActionModeAnimationListener
        public void onStart(boolean z) {
            if (z) {
                SearchActionModeView.this.mInputView.getText().clear();
                SearchActionModeView.this.mInputView.addTextChangedListener(SearchActionModeView.this);
                SearchActionModeView.this.mTextCancel.setTranslationX(SearchActionModeView.this.mTextCancel.getWidth());
            }
        }

        @Override // miuix.view.ActionModeAnimationListener
        public void onUpdate(boolean z, float f) {
            if (!z) {
                f = 1.0f - f;
            }
            SearchActionModeView searchActionModeView = SearchActionModeView.this;
            searchActionModeView.setPadding(searchActionModeView.getPaddingLeft(), (int) (SearchActionModeView.this.mOriginalPaddingTop + (SearchActionModeView.this.mStatusBarPaddingTop * f) + (SearchActionModeView.this.mInputPaddingTop * f)), SearchActionModeView.this.getPaddingRight(), SearchActionModeView.this.getPaddingBottom());
            SearchActionModeView.this.getLayoutParams().height = SearchActionModeView.this.mSearchViewHeight + ((int) (SearchActionModeView.this.mStatusBarPaddingTop * f));
            SearchActionModeView.this.requestLayout();
            updateCancelView(f, SearchActionModeView.this.mInputPaddingRight);
        }

        public void updateCancelView(float f, int i) {
            float f2 = 1.0f - f;
            if (ViewUtils.isLayoutRtl(SearchActionModeView.this.mTextCancel)) {
                f2 = f - 1.0f;
            }
            SearchActionModeView.this.mTextCancel.setTranslationX(SearchActionModeView.this.mTextCancel.getWidth() * f2);
            if (SearchActionModeView.this.mSearchContainer.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                ((ViewGroup.MarginLayoutParams) SearchActionModeView.this.mSearchContainer.getLayoutParams()).setMarginEnd((int) (((SearchActionModeView.this.mTextCancel.getWidth() - i) * f) + i));
            }
            SearchActionModeView.this.mSearchContainer.requestLayout();
        }

        @Override // miuix.view.ActionModeAnimationListener
        public void onStop(boolean z) {
            if (!z) {
                SearchActionModeView.this.mInputView.removeTextChangedListener(SearchActionModeView.this);
            }
        }
    }

    /* loaded from: classes3.dex */
    public class ActionBarAnimationProcessor implements ActionModeAnimationListener {
        @Override // miuix.view.ActionModeAnimationListener
        public void onUpdate(boolean z, float f) {
        }

        public ActionBarAnimationProcessor() {
            SearchActionModeView.this = r1;
        }

        @Override // miuix.view.ActionModeAnimationListener
        public void onStart(boolean z) {
            if (z) {
                SearchActionModeView.this.mActionBarContainer.setVisibility(4);
            } else {
                SearchActionModeView.this.mActionBarContainer.setVisibility(0);
            }
        }

        @Override // miuix.view.ActionModeAnimationListener
        public void onStop(boolean z) {
            View tabContainer;
            if (!z || (tabContainer = SearchActionModeView.this.getActionBarContainer().getTabContainer()) == null) {
                return;
            }
            tabContainer.setVisibility(8);
        }
    }

    /* loaded from: classes3.dex */
    public class SplitActionBarAnimationProcessor implements ActionModeAnimationListener {
        @Override // miuix.view.ActionModeAnimationListener
        public void onStart(boolean z) {
        }

        @Override // miuix.view.ActionModeAnimationListener
        public void onStop(boolean z) {
        }

        public SplitActionBarAnimationProcessor() {
            SearchActionModeView.this = r1;
        }

        @Override // miuix.view.ActionModeAnimationListener
        public void onUpdate(boolean z, float f) {
            if (!z) {
                f = 1.0f - f;
            }
            ActionBarContainer splitActionBarContainer = SearchActionModeView.this.getSplitActionBarContainer();
            if (splitActionBarContainer != null) {
                splitActionBarContainer.setTranslationY(f * splitActionBarContainer.getHeight());
            }
        }
    }

    /* loaded from: classes3.dex */
    public class ContentViewAnimationProcessor implements ActionModeAnimationListener {
        public boolean mDimViewVisible;
        public int mTmpAccessibilityMode = 0;

        public ContentViewAnimationProcessor() {
            SearchActionModeView.this = r1;
        }

        @Override // miuix.view.ActionModeAnimationListener
        public void onStart(boolean z) {
            View contentView = SearchActionModeView.this.getContentView();
            if (contentView != null && z) {
                SearchActionModeView.this.mContentOriginPaddingTop = contentView.getPaddingTop();
                SearchActionModeView.this.mContentOriginPaddingBottom = contentView.getPaddingBottom();
            }
            View view = null;
            View view2 = SearchActionModeView.this.mAnchorView != null ? (View) SearchActionModeView.this.mAnchorView.get() : null;
            if (view2 != null) {
                view2.setAlpha(0.0f);
            }
            if (z) {
                if (SearchActionModeView.this.mActionBarLocation == Integer.MAX_VALUE) {
                    SearchActionModeView.this.getActionBarContainer().getLocationInWindow(SearchActionModeView.this.mLocation);
                    SearchActionModeView searchActionModeView = SearchActionModeView.this;
                    searchActionModeView.mActionBarLocation = searchActionModeView.mLocation[1];
                }
                SearchActionModeView searchActionModeView2 = SearchActionModeView.this;
                searchActionModeView2.mInputViewTranslationYStart = searchActionModeView2.getActionBarContainer().getHeight();
                SearchActionModeView searchActionModeView3 = SearchActionModeView.this;
                searchActionModeView3.mInputViewTranslationYLength = -searchActionModeView3.mInputViewTranslationYStart;
                if (view2 != null) {
                    view2.getLocationInWindow(SearchActionModeView.this.mLocation);
                }
                SearchActionModeView searchActionModeView4 = SearchActionModeView.this;
                searchActionModeView4.mAnimateViewTranslationYStart = (searchActionModeView4.mLocation[1] - SearchActionModeView.this.mActionBarLocation) - SearchActionModeView.this.mContentOriginPaddingTop;
                SearchActionModeView searchActionModeView5 = SearchActionModeView.this;
                searchActionModeView5.mAnimateViewTranslationYLength = searchActionModeView5.mInputViewTranslationYLength;
            } else {
                this.mDimViewVisible = SearchActionModeView.this.mDimView != null && SearchActionModeView.this.mDimView.getVisibility() == 0;
                if (SearchActionModeView.this.mActionBarContainer != null && SearchActionModeView.this.mActionBarContainer.isBlurEnable()) {
                    SearchActionModeView searchActionModeView6 = SearchActionModeView.this;
                    searchActionModeView6.setContentViewTranslation(this.mDimViewVisible ? searchActionModeView6.mStatusBarPaddingTop : -searchActionModeView6.mContentOriginPaddingTop);
                } else {
                    SearchActionModeView searchActionModeView7 = SearchActionModeView.this;
                    searchActionModeView7.setContentViewTranslation(searchActionModeView7.mStatusBarPaddingTop);
                    SearchActionModeView.this.setContentViewPadding(0, 0);
                }
            }
            View view3 = SearchActionModeView.this.mAnimateView != null ? (View) SearchActionModeView.this.mAnimateView.get() : null;
            if (SearchActionModeView.this.mResultView != null) {
                view = (View) SearchActionModeView.this.mResultView.get();
            }
            if (!z) {
                if (view == null) {
                    return;
                }
                if (view3 != null) {
                    view3.setImportantForAccessibility(this.mTmpAccessibilityMode);
                }
                view.setImportantForAccessibility(4);
            } else if (view == null) {
            } else {
                if (view3 != null) {
                    this.mTmpAccessibilityMode = view3.getImportantForAccessibility();
                    view3.setImportantForAccessibility(4);
                }
                view.setImportantForAccessibility(1);
            }
        }

        @Override // miuix.view.ActionModeAnimationListener
        public void onUpdate(boolean z, float f) {
            if (!z) {
                f = 1.0f - f;
            }
            View view = SearchActionModeView.this.mAnimateView != null ? (View) SearchActionModeView.this.mAnimateView.get() : null;
            SearchActionModeView searchActionModeView = SearchActionModeView.this;
            searchActionModeView.setContentViewTranslation((int) (searchActionModeView.mStatusBarPaddingTop * f));
            if (view != null) {
                view.setTranslationY(SearchActionModeView.this.mAnimateViewTranslationYStart + (SearchActionModeView.this.mAnimateViewTranslationYLength * f));
            }
            SearchActionModeView searchActionModeView2 = SearchActionModeView.this;
            searchActionModeView2.setTranslationY(searchActionModeView2.mInputViewTranslationYStart + (f * SearchActionModeView.this.mInputViewTranslationYLength));
        }

        @Override // miuix.view.ActionModeAnimationListener
        public void onStop(boolean z) {
            View view = null;
            if (!z) {
                View view2 = SearchActionModeView.this.mAnimateView != null ? (View) SearchActionModeView.this.mAnimateView.get() : null;
                if (view2 != null) {
                    view2.setTranslationY(0.0f);
                }
            }
            if (SearchActionModeView.this.mAnchorView != null) {
                view = (View) SearchActionModeView.this.mAnchorView.get();
            }
            if (view != null) {
                view.setEnabled(!z);
            }
            if (SearchActionModeView.this.mStatusBarPaddingTop > 0) {
                SearchActionModeView.this.setContentViewTranslation(0);
                SearchActionModeView searchActionModeView = SearchActionModeView.this;
                searchActionModeView.setContentViewPadding(z ? searchActionModeView.mStatusBarPaddingTop : 0, 0);
            }
            if (!z || SearchActionModeView.this.mActionBarContainer == null || !SearchActionModeView.this.mActionBarContainer.isBlurEnable()) {
                return;
            }
            SearchActionModeView searchActionModeView2 = SearchActionModeView.this;
            searchActionModeView2.setContentViewTranslation(-searchActionModeView2.mContentOriginPaddingTop);
        }
    }

    /* loaded from: classes3.dex */
    public class DimViewAnimationProcessor implements ActionModeAnimationListener {
        public DimViewAnimationProcessor() {
            SearchActionModeView.this = r1;
        }

        @Override // miuix.view.ActionModeAnimationListener
        public void onStart(boolean z) {
            if (z) {
                SearchActionModeView.this.mDimView.setOnClickListener(SearchActionModeView.this);
                SearchActionModeView.this.mDimView.setVisibility(0);
                SearchActionModeView.this.mDimView.setAlpha(0.0f);
            }
        }

        @Override // miuix.view.ActionModeAnimationListener
        public void onUpdate(boolean z, float f) {
            if (!z) {
                f = 1.0f - f;
            }
            SearchActionModeView.this.mDimView.setAlpha(f);
            if (SearchActionModeView.this.shouldAnimateContent()) {
                float f2 = 0.0f;
                View view = (View) SearchActionModeView.this.mAnimateView.get();
                if (view != null) {
                    f2 = view.getTranslationY();
                }
                SearchActionModeView.this.mDimView.setTranslationY(f2 + (SearchActionModeView.this.mActionBarContainer != null ? SearchActionModeView.this.mContentOriginPaddingTop : 0));
            }
        }

        @Override // miuix.view.ActionModeAnimationListener
        public void onStop(boolean z) {
            if (z) {
                if (SearchActionModeView.this.mInputView.getText().length() <= 0) {
                    return;
                }
                SearchActionModeView.this.mDimView.setVisibility(8);
                return;
            }
            SearchActionModeView.this.mDimView.setVisibility(8);
            SearchActionModeView.this.mDimView.setAlpha(1.0f);
            SearchActionModeView.this.mDimView.setTranslationY(0.0f);
        }
    }
}
