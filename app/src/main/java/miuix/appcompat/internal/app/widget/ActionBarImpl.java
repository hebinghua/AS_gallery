package miuix.appcompat.internal.app.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Trace;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.miui.gallery.search.statistics.SearchStatUtils;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import miuix.animation.Folme;
import miuix.animation.IStateStyle;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.TransitionListener;
import miuix.animation.listener.UpdateInfo;
import miuix.animation.property.ViewProperty;
import miuix.animation.utils.EaseManager;
import miuix.appcompat.R$id;
import miuix.appcompat.R$layout;
import miuix.appcompat.app.ActionBar;
import miuix.appcompat.app.AppCompatActivity;
import miuix.appcompat.app.IFragment;
import miuix.appcompat.internal.view.ActionBarPolicy;
import miuix.appcompat.internal.view.ActionModeImpl;
import miuix.appcompat.internal.view.EditActionModeImpl;
import miuix.appcompat.internal.view.SearchActionModeImpl;
import miuix.appcompat.internal.view.menu.action.ActionMenuView;
import miuix.appcompat.internal.view.menu.action.PhoneActionMenuView;
import miuix.view.SearchActionMode;

/* loaded from: classes3.dex */
public class ActionBarImpl extends ActionBar {
    public static ActionBar.TabListener sTabListenerWrapper = new ActionBar.TabListener() { // from class: miuix.appcompat.internal.app.widget.ActionBarImpl.1
        @Override // androidx.appcompat.app.ActionBar.TabListener
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            TabImpl tabImpl = (TabImpl) tab;
            if (tabImpl.mInternalCallback != null) {
                tabImpl.mInternalCallback.onTabSelected(tab, fragmentTransaction);
            }
            if (tabImpl.mCallback != null) {
                tabImpl.mCallback.onTabSelected(tab, fragmentTransaction);
            }
        }

        @Override // androidx.appcompat.app.ActionBar.TabListener
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            TabImpl tabImpl = (TabImpl) tab;
            if (tabImpl.mInternalCallback != null) {
                tabImpl.mInternalCallback.onTabUnselected(tab, fragmentTransaction);
            }
            if (tabImpl.mCallback != null) {
                tabImpl.mCallback.onTabUnselected(tab, fragmentTransaction);
            }
        }

        @Override // androidx.appcompat.app.ActionBar.TabListener
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            TabImpl tabImpl = (TabImpl) tab;
            if (tabImpl.mInternalCallback != null) {
                tabImpl.mInternalCallback.onTabReselected(tab, fragmentTransaction);
            }
            if (tabImpl.mCallback != null) {
                tabImpl.mCallback.onTabReselected(tab, fragmentTransaction);
            }
        }
    };
    public ActionMode mActionMode;
    public ActionModeView mActionModeView;
    public ActionBarView mActionView;
    public ScrollingTabContainerView mCollapseTabScrollView;
    public IStateStyle mContainerAnim;
    public ActionBarContainer mContainerView;
    public View mContentMask;
    public View.OnClickListener mContentMaskOnClickListenr;
    public Context mContext;
    public int mContextDisplayMode;
    public ActionBarContextView mContextView;
    public int mCurrentAccessibilityImportant;
    public int mCurrentExpandState;
    public boolean mCurrentResizable;
    public boolean mDisplayHomeAsUpSet;
    public ScrollingTabContainerView mExpandTabScrollView;
    public ScrollingTabContainerView mExpanedTabScrollView;
    public FragmentManager mFragmentManager;
    public boolean mHiddenByApp;
    public boolean mHiddenBySystem;
    public ActionBarOverlayLayout mOverlayLayout;
    public SearchActionModeView mSearchActionModeView;
    public TabImpl mSelectedTab;
    public boolean mShowHideAnimationEnabled;
    public boolean mShowingForMode;
    public PhoneActionMenuView mSplitMenuView;
    public ActionBarContainer mSplitView;
    public IStateStyle mSpliterAnim;
    public ScrollingTabContainerView mTabScrollView;
    public Context mThemedContext;
    public ActionBarViewPagerController mViewPagerController;
    public ArrayList<TabImpl> mTabs = new ArrayList<>();
    public int mSavedTabPosition = -1;
    public ArrayList<ActionBar.OnMenuVisibilityListener> mMenuVisibilityListeners = new ArrayList<>();
    public int mCurWindowVisibility = 0;
    public boolean mNowShowing = true;
    public ActionModeImpl.ActionModeCallback mActionModeCallback = new ActionModeImpl.ActionModeCallback() { // from class: miuix.appcompat.internal.app.widget.ActionBarImpl.2
        @Override // miuix.appcompat.internal.view.ActionModeImpl.ActionModeCallback
        public void onActionModeFinish(ActionMode actionMode) {
            ActionBarImpl.this.animateToMode(false);
            ActionBarImpl.this.mActionMode = null;
        }
    };

    public static boolean checkShowingFlags(boolean z, boolean z2, boolean z3) {
        if (z3) {
            return true;
        }
        return !z && !z2;
    }

    public ActionBarImpl(AppCompatActivity appCompatActivity, ViewGroup viewGroup) {
        this.mContext = appCompatActivity;
        this.mFragmentManager = appCompatActivity.getSupportFragmentManager();
        init(viewGroup);
        setTitle(appCompatActivity.getTitle());
    }

    public ActionBarImpl(Fragment fragment) {
        this.mContext = ((IFragment) fragment).getThemedContext();
        this.mFragmentManager = fragment.getChildFragmentManager();
        init((ViewGroup) fragment.getView());
        FragmentActivity activity = fragment.getActivity();
        setTitle(activity != null ? activity.getTitle() : null);
    }

    public void init(ViewGroup viewGroup) {
        if (viewGroup == null) {
            return;
        }
        ActionBarOverlayLayout actionBarOverlayLayout = (ActionBarOverlayLayout) viewGroup;
        this.mOverlayLayout = actionBarOverlayLayout;
        actionBarOverlayLayout.setActionBar(this);
        this.mActionView = (ActionBarView) viewGroup.findViewById(R$id.action_bar);
        this.mContextView = (ActionBarContextView) viewGroup.findViewById(R$id.action_context_bar);
        this.mContainerView = (ActionBarContainer) viewGroup.findViewById(R$id.action_bar_container);
        this.mSplitView = (ActionBarContainer) viewGroup.findViewById(R$id.split_action_bar);
        View findViewById = viewGroup.findViewById(R$id.content_mask);
        this.mContentMask = findViewById;
        if (findViewById != null) {
            this.mContentMaskOnClickListenr = new View.OnClickListener() { // from class: miuix.appcompat.internal.app.widget.ActionBarImpl.3
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (ActionBarImpl.this.mSplitMenuView == null || !ActionBarImpl.this.mSplitMenuView.isOverflowMenuShowing()) {
                        return;
                    }
                    ActionBarImpl.this.mSplitMenuView.getPresenter().hideOverflowMenu(true);
                }
            };
        }
        ActionBarView actionBarView = this.mActionView;
        if (actionBarView == null && this.mContextView == null && this.mContainerView == null) {
            throw new IllegalStateException(getClass().getSimpleName() + " can only be used with a compatible window decor layout");
        }
        this.mContextDisplayMode = actionBarView.isSplitActionBar() ? 1 : 0;
        boolean z = false;
        Object[] objArr = (this.mActionView.getDisplayOptions() & 4) != 0 ? 1 : null;
        if (objArr != null) {
            this.mDisplayHomeAsUpSet = true;
        }
        ActionBarPolicy actionBarPolicy = ActionBarPolicy.get(this.mContext);
        if (actionBarPolicy.enableHomeButtonByDefault() || objArr != null) {
            z = true;
        }
        setHomeButtonEnabled(z);
        setHasEmbeddedTabs(actionBarPolicy.hasEmbeddedTabs());
    }

    @Override // androidx.appcompat.app.ActionBar
    public void onConfigurationChanged(Configuration configuration) {
        setHasEmbeddedTabs(ActionBarPolicy.get(this.mContext).hasEmbeddedTabs());
    }

    public final void setHasEmbeddedTabs(boolean z) {
        this.mContainerView.setTabContainer(null);
        this.mActionView.setEmbeddedTabView(this.mTabScrollView, this.mExpanedTabScrollView, this.mCollapseTabScrollView, this.mExpandTabScrollView);
        boolean z2 = getNavigationMode() == 2;
        ScrollingTabContainerView scrollingTabContainerView = this.mTabScrollView;
        if (scrollingTabContainerView != null) {
            if (z2) {
                scrollingTabContainerView.setVisibility(0);
            } else {
                scrollingTabContainerView.setVisibility(8);
            }
            this.mTabScrollView.setEmbeded(true);
        }
        ScrollingTabContainerView scrollingTabContainerView2 = this.mExpanedTabScrollView;
        if (scrollingTabContainerView2 != null) {
            if (z2) {
                scrollingTabContainerView2.setVisibility(0);
            } else {
                scrollingTabContainerView2.setVisibility(8);
            }
            this.mExpanedTabScrollView.setEmbeded(true);
        }
        ScrollingTabContainerView scrollingTabContainerView3 = this.mCollapseTabScrollView;
        if (scrollingTabContainerView3 != null) {
            if (z2) {
                scrollingTabContainerView3.setVisibility(0);
            } else {
                scrollingTabContainerView3.setVisibility(8);
            }
            this.mCollapseTabScrollView.setEmbeded(true);
        }
        ScrollingTabContainerView scrollingTabContainerView4 = this.mExpandTabScrollView;
        if (scrollingTabContainerView4 != null) {
            if (z2) {
                scrollingTabContainerView4.setVisibility(0);
            } else {
                scrollingTabContainerView4.setVisibility(8);
            }
            this.mExpandTabScrollView.setEmbeded(true);
        }
        this.mActionView.setCollapsable(false);
    }

    @Override // miuix.appcompat.app.ActionBar
    public void setTabsMode(boolean z) {
        setHasEmbeddedTabs(z);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void setCustomView(View view) {
        this.mActionView.setCustomNavigationView(view);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void setCustomView(View view, ActionBar.LayoutParams layoutParams) {
        view.setLayoutParams(layoutParams);
        this.mActionView.setCustomNavigationView(view);
    }

    public void setSelectedNavigationItem(int i) {
        int navigationMode = this.mActionView.getNavigationMode();
        if (navigationMode == 1) {
            this.mActionView.setDropdownSelectedPosition(i);
        } else if (navigationMode == 2) {
            selectTab(this.mTabs.get(i));
        } else {
            throw new IllegalStateException("setSelectedNavigationIndex not valid for current navigation mode");
        }
    }

    public int getSelectedNavigationIndex() {
        TabImpl tabImpl;
        int navigationMode = this.mActionView.getNavigationMode();
        if (navigationMode != 1) {
            if (navigationMode != 2 || (tabImpl = this.mSelectedTab) == null) {
                return -1;
            }
            return tabImpl.getPosition();
        }
        return this.mActionView.getDropdownSelectedPosition();
    }

    @Override // androidx.appcompat.app.ActionBar
    public void setTitle(CharSequence charSequence) {
        this.mActionView.setTitle(charSequence);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void setDisplayOptions(int i, int i2) {
        ActionBarContainer actionBarContainer;
        int displayOptions = this.mActionView.getDisplayOptions();
        if ((i2 & 4) != 0) {
            this.mDisplayHomeAsUpSet = true;
        }
        this.mActionView.setDisplayOptions(((~i2) & displayOptions) | (i & i2));
        int displayOptions2 = this.mActionView.getDisplayOptions();
        ActionBarContainer actionBarContainer2 = this.mContainerView;
        if (actionBarContainer2 != null) {
            actionBarContainer2.setBlurBackground((displayOptions2 & 32768) != 0);
        }
        if ((i & 16384) != 0 && (actionBarContainer = this.mSplitView) != null) {
            actionBarContainer.setBlurBackground(true);
            return;
        }
        ActionBarContainer actionBarContainer3 = this.mSplitView;
        if (actionBarContainer3 == null) {
            return;
        }
        actionBarContainer3.setBlurBackground(false);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void setDisplayShowHomeEnabled(boolean z) {
        setDisplayOptions(z ? getBlurOptioons() | 2 : 0, getBlurOptioons() | 2);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void setDisplayShowTitleEnabled(boolean z) {
        setDisplayOptions(z ? getBlurOptioons() | 8 : 0, getBlurOptioons() | 8);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void setDisplayShowCustomEnabled(boolean z) {
        setDisplayOptions(z ? getBlurOptioons() | 16 : 0, getBlurOptioons() | 16);
    }

    public final int getBlurOptioons() {
        int i = 32768;
        boolean z = true;
        int i2 = 0;
        boolean z2 = (getDisplayOptions() & 32768) != 0;
        if ((getDisplayOptions() & 16384) == 0) {
            z = false;
        }
        if (!z2) {
            i = 0;
        }
        if (z) {
            i2 = 16384;
        }
        return i | i2;
    }

    @Override // androidx.appcompat.app.ActionBar
    public void setHomeButtonEnabled(boolean z) {
        this.mActionView.setHomeButtonEnabled(z);
    }

    @Override // androidx.appcompat.app.ActionBar
    public void setBackgroundDrawable(Drawable drawable) {
        boolean z = (getDisplayOptions() & 32768) != 0;
        ActionBarContainer actionBarContainer = this.mContainerView;
        if (z) {
            drawable = null;
        }
        actionBarContainer.setPrimaryBackground(drawable);
    }

    @Override // androidx.appcompat.app.ActionBar
    public View getCustomView() {
        return this.mActionView.getCustomNavigationView();
    }

    @Override // androidx.appcompat.app.ActionBar
    public void setCustomView(int i) {
        setCustomView(LayoutInflater.from(getThemedContext()).inflate(i, (ViewGroup) this.mActionView, false));
    }

    @Override // androidx.appcompat.app.ActionBar
    public void setTitle(int i) {
        setTitle(this.mContext.getString(i));
    }

    public int getNavigationMode() {
        return this.mActionView.getNavigationMode();
    }

    public void setNavigationMode(int i) {
        if (this.mActionView.getNavigationMode() == 2) {
            this.mSavedTabPosition = getSelectedNavigationIndex();
            selectTab(null);
            this.mTabScrollView.setVisibility(8);
            this.mExpanedTabScrollView.setVisibility(8);
            this.mCollapseTabScrollView.setVisibility(8);
            this.mExpandTabScrollView.setVisibility(8);
        }
        this.mActionView.setNavigationMode(i);
        if (i == 2) {
            ensureTabsExist();
            this.mTabScrollView.setVisibility(0);
            this.mExpanedTabScrollView.setVisibility(0);
            this.mCollapseTabScrollView.setVisibility(0);
            this.mExpandTabScrollView.setVisibility(0);
            int i2 = this.mSavedTabPosition;
            if (i2 != -1) {
                setSelectedNavigationItem(i2);
                this.mSavedTabPosition = -1;
            }
        }
        this.mActionView.setCollapsable(false);
    }

    @Override // androidx.appcompat.app.ActionBar
    public int getDisplayOptions() {
        return this.mActionView.getDisplayOptions();
    }

    @Override // androidx.appcompat.app.ActionBar
    public void setDisplayOptions(int i) {
        ActionBarContainer actionBarContainer;
        if ((i & 4) != 0) {
            this.mDisplayHomeAsUpSet = true;
        }
        this.mActionView.setDisplayOptions(i);
        int displayOptions = this.mActionView.getDisplayOptions();
        ActionBarContainer actionBarContainer2 = this.mContainerView;
        if (actionBarContainer2 != null) {
            actionBarContainer2.setBlurBackground((displayOptions & 32768) != 0);
        }
        if ((i & 16384) != 0 && (actionBarContainer = this.mSplitView) != null) {
            actionBarContainer.setBlurBackground(true);
            return;
        }
        ActionBarContainer actionBarContainer3 = this.mSplitView;
        if (actionBarContainer3 == null) {
            return;
        }
        actionBarContainer3.setBlurBackground(false);
    }

    @Override // androidx.appcompat.app.ActionBar
    public ActionBar.Tab newTab() {
        return new TabImpl();
    }

    public ActionBarOverlayLayout getActionBarOverlayLayout() {
        return this.mOverlayLayout;
    }

    public void internalAddTab(ActionBar.Tab tab, boolean z) {
        ensureTabsExist();
        this.mTabScrollView.addTab(tab, z);
        this.mExpanedTabScrollView.addTab(tab, z);
        this.mCollapseTabScrollView.addTab(tab, z);
        this.mExpandTabScrollView.addTab(tab, z);
        configureTab(tab, this.mTabs.size());
        if (z) {
            selectTab(tab);
        }
    }

    public void internalAddTab(ActionBar.Tab tab, int i, boolean z) {
        ensureTabsExist();
        this.mTabScrollView.addTab(tab, i, z);
        this.mExpanedTabScrollView.addTab(tab, i, z);
        this.mCollapseTabScrollView.addTab(tab, i, z);
        this.mExpandTabScrollView.addTab(tab, i, z);
        configureTab(tab, i);
        if (z) {
            selectTab(tab);
        }
    }

    public void removeAllTabs() {
        if (isFragmentViewPagerMode()) {
            throw new IllegalStateException("Cannot add tab directly in fragment view pager mode!\n Please using addFragmentTab().");
        }
        internalRemoveAllTabs();
    }

    public void internalRemoveTabAt(int i) {
        if (this.mTabScrollView == null) {
            return;
        }
        TabImpl tabImpl = this.mSelectedTab;
        int position = tabImpl != null ? tabImpl.getPosition() : this.mSavedTabPosition;
        this.mTabScrollView.removeTabAt(i);
        this.mExpanedTabScrollView.removeTabAt(i);
        this.mCollapseTabScrollView.removeTabAt(i);
        this.mExpandTabScrollView.removeTabAt(i);
        TabImpl remove = this.mTabs.remove(i);
        if (remove != null) {
            remove.setPosition(-1);
        }
        int size = this.mTabs.size();
        for (int i2 = i; i2 < size; i2++) {
            this.mTabs.get(i2).setPosition(i2);
        }
        if (position != i) {
            return;
        }
        selectTab(this.mTabs.isEmpty() ? null : this.mTabs.get(Math.max(0, i - 1)));
    }

    public void internalRemoveAllTabs() {
        cleanupTabs();
    }

    @Override // androidx.appcompat.app.ActionBar
    public void selectTab(ActionBar.Tab tab) {
        selectTab(tab, true);
    }

    @Override // miuix.appcompat.app.ActionBar
    public void selectTab(ActionBar.Tab tab, boolean z) {
        int i = -1;
        if (getNavigationMode() != 2) {
            if (tab != null) {
                i = tab.getPosition();
            }
            this.mSavedTabPosition = i;
            return;
        }
        FragmentTransaction disallowAddToBackStack = this.mFragmentManager.beginTransaction().disallowAddToBackStack();
        TabImpl tabImpl = this.mSelectedTab;
        if (tabImpl != tab) {
            this.mTabScrollView.setTabSelected(tab != null ? tab.getPosition() : -1, z);
            this.mExpanedTabScrollView.setTabSelected(tab != null ? tab.getPosition() : -1, z);
            this.mCollapseTabScrollView.setTabSelected(tab != null ? tab.getPosition() : -1, z);
            ScrollingTabContainerView scrollingTabContainerView = this.mExpandTabScrollView;
            if (tab != null) {
                i = tab.getPosition();
            }
            scrollingTabContainerView.setTabSelected(i, z);
            TabImpl tabImpl2 = this.mSelectedTab;
            if (tabImpl2 != null) {
                tabImpl2.getCallback().onTabUnselected(this.mSelectedTab, disallowAddToBackStack);
            }
            TabImpl tabImpl3 = (TabImpl) tab;
            this.mSelectedTab = tabImpl3;
            if (tabImpl3 != null) {
                tabImpl3.mWithAnim = z;
                tabImpl3.getCallback().onTabSelected(this.mSelectedTab, disallowAddToBackStack);
            }
        } else if (tabImpl != null) {
            tabImpl.getCallback().onTabReselected(this.mSelectedTab, disallowAddToBackStack);
            this.mTabScrollView.animateToTab(tab.getPosition());
            this.mExpanedTabScrollView.animateToTab(tab.getPosition());
            this.mCollapseTabScrollView.animateToTab(tab.getPosition());
            this.mExpandTabScrollView.animateToTab(tab.getPosition());
        }
        if (disallowAddToBackStack.isEmpty()) {
            return;
        }
        disallowAddToBackStack.commit();
    }

    @Override // androidx.appcompat.app.ActionBar
    public ActionBar.Tab getTabAt(int i) {
        return this.mTabs.get(i);
    }

    @Override // androidx.appcompat.app.ActionBar
    public int getTabCount() {
        return this.mTabs.size();
    }

    @Override // androidx.appcompat.app.ActionBar
    public Context getThemedContext() {
        if (this.mThemedContext == null) {
            TypedValue typedValue = new TypedValue();
            this.mContext.getTheme().resolveAttribute(16843671, typedValue, true);
            int i = typedValue.resourceId;
            if (i != 0) {
                this.mThemedContext = new ContextThemeWrapper(this.mContext, i);
            } else {
                this.mThemedContext = this.mContext;
            }
        }
        return this.mThemedContext;
    }

    @Override // androidx.appcompat.app.ActionBar
    public int getHeight() {
        return this.mContainerView.getHeight();
    }

    @Override // androidx.appcompat.app.ActionBar
    public void show() {
        show(null);
    }

    public void show(AnimState animState) {
        if (this.mHiddenByApp) {
            this.mHiddenByApp = false;
            updateVisibility(false, animState);
        }
    }

    public void showForActionMode() {
        if (!this.mShowingForMode) {
            boolean z = true;
            this.mShowingForMode = true;
            updateVisibility(false);
            this.mCurrentExpandState = getExpandState();
            this.mCurrentResizable = isResizable();
            ActionModeView actionModeView = this.mActionModeView;
            if (actionModeView instanceof SearchActionModeView) {
                setExpandState(0, true);
                setResizable(false);
            } else {
                ((ActionBarContextView) actionModeView).setExpandState(this.mCurrentExpandState);
                ((ActionBarContextView) this.mActionModeView).setResizable(this.mCurrentResizable);
            }
            this.mCurrentAccessibilityImportant = this.mActionView.getImportantForAccessibility();
            this.mActionView.setImportantForAccessibility(4);
            ActionBarView actionBarView = this.mActionView;
            boolean z2 = this.mActionModeView instanceof SearchActionModeView;
            if ((getDisplayOptions() & 32768) == 0) {
                z = false;
            }
            actionBarView.onActionModeStart(z2, z);
        }
    }

    @Override // androidx.appcompat.app.ActionBar
    public void hide() {
        hide(null);
    }

    public void hide(AnimState animState) {
        if (!this.mHiddenByApp) {
            this.mHiddenByApp = true;
            updateVisibility(false, animState);
        }
    }

    public void hideForActionMode() {
        if (this.mShowingForMode) {
            this.mShowingForMode = false;
            this.mActionView.onActionModeEnd((getDisplayOptions() & 32768) != 0);
            updateVisibility(false);
            setResizable(true);
            ActionModeView actionModeView = this.mActionModeView;
            if (actionModeView instanceof SearchActionModeView) {
                setExpandState(this.mCurrentExpandState, true);
                setResizable(this.mCurrentResizable);
            } else {
                this.mCurrentExpandState = ((ActionBarContextView) actionModeView).getExpandState();
                this.mCurrentResizable = ((ActionBarContextView) this.mActionModeView).isResizable();
                setExpandState(this.mCurrentExpandState);
                setResizable(this.mCurrentResizable);
            }
            this.mActionView.setImportantForAccessibility(this.mCurrentAccessibilityImportant);
        }
    }

    @Override // androidx.appcompat.app.ActionBar
    public boolean isShowing() {
        return this.mNowShowing;
    }

    public ActionMode startActionMode(ActionMode.Callback callback) {
        ActionMode actionMode = this.mActionMode;
        if (actionMode != null) {
            actionMode.finish();
        }
        ActionMode createActionMode = createActionMode(callback);
        ActionModeView actionModeView = this.mActionModeView;
        if (((actionModeView instanceof SearchActionModeView) && (createActionMode instanceof SearchActionModeImpl)) || ((actionModeView instanceof ActionBarContextView) && (createActionMode instanceof EditActionModeImpl))) {
            actionModeView.closeMode();
            this.mActionModeView.killMode();
        }
        ActionModeView createActionModeView = createActionModeView(callback);
        this.mActionModeView = createActionModeView;
        if (createActionModeView == null) {
            throw new IllegalStateException("not set windowSplitActionBar true in activity style!");
        }
        if (!(createActionMode instanceof ActionModeImpl)) {
            return null;
        }
        ActionModeImpl actionModeImpl = (ActionModeImpl) createActionMode;
        actionModeImpl.setActionModeView(createActionModeView);
        actionModeImpl.setActionModeCallback(this.mActionModeCallback);
        if (!actionModeImpl.dispatchOnCreate()) {
            return null;
        }
        createActionMode.invalidate();
        this.mActionModeView.initForMode(createActionMode);
        animateToMode(true);
        ActionBarContainer actionBarContainer = this.mSplitView;
        if (actionBarContainer != null && this.mContextDisplayMode == 1 && actionBarContainer.getVisibility() != 0) {
            this.mSplitView.setVisibility(0);
        }
        ActionModeView actionModeView2 = this.mActionModeView;
        if (actionModeView2 instanceof ActionBarContextView) {
            ((ActionBarContextView) actionModeView2).sendAccessibilityEvent(32);
        }
        this.mActionMode = createActionMode;
        return createActionMode;
    }

    public void animateToMode(boolean z) {
        if (z) {
            showForActionMode();
        } else {
            hideForActionMode();
        }
        this.mActionModeView.animateToVisibility(z);
        if (this.mTabScrollView == null || this.mActionView.isTightTitleWithEmbeddedTabs() || !this.mActionView.isCollapsed()) {
            return;
        }
        this.mTabScrollView.setEnabled(!z);
        this.mExpanedTabScrollView.setEnabled(!z);
        this.mCollapseTabScrollView.setEnabled(!z);
        this.mCollapseTabScrollView.setEnabled(!z);
    }

    public final ActionMode createActionMode(ActionMode.Callback callback) {
        if (callback instanceof SearchActionMode.Callback) {
            return new SearchActionModeImpl(this.mContext, callback);
        }
        return new EditActionModeImpl(this.mContext, callback);
    }

    public ActionModeView createActionModeView(ActionMode.Callback callback) {
        Rect pendingInsets;
        if (callback instanceof SearchActionMode.Callback) {
            if (this.mSearchActionModeView == null) {
                this.mSearchActionModeView = createSearchActionModeView();
            }
            if (!this.mContainerView.isMiuixFloating() && (pendingInsets = this.mContainerView.getPendingInsets()) != null) {
                this.mSearchActionModeView.setStatusBarPaddingTop(pendingInsets.top);
            }
            if (this.mOverlayLayout != this.mSearchActionModeView.getParent()) {
                this.mOverlayLayout.addView(this.mSearchActionModeView);
            }
            return this.mSearchActionModeView;
        }
        if (this.mContextView == null) {
            createContextView(true, true);
        }
        ActionBarContextView actionBarContextView = this.mContextView;
        if (actionBarContextView == null) {
            throw new IllegalStateException("not set windowSplitActionBar true in activity style!");
        }
        return actionBarContextView;
    }

    public SearchActionModeView createSearchActionModeView() {
        SearchActionModeView searchActionModeView = (SearchActionModeView) LayoutInflater.from(getThemedContext()).inflate(R$layout.miuix_appcompat_search_action_mode_view, (ViewGroup) this.mOverlayLayout, false);
        searchActionModeView.setOnBackClickListener(new View.OnClickListener() { // from class: miuix.appcompat.internal.app.widget.ActionBarImpl.4
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ActionMode actionMode = ActionBarImpl.this.mActionMode;
                if (actionMode != null) {
                    actionMode.finish();
                }
            }
        });
        return searchActionModeView;
    }

    public boolean isFragmentViewPagerMode() {
        return this.mViewPagerController != null;
    }

    @Override // miuix.appcompat.app.ActionBar
    public void setFragmentViewPagerMode(FragmentActivity fragmentActivity) {
        setFragmentViewPagerMode(fragmentActivity, true);
    }

    public void setFragmentViewPagerMode(FragmentActivity fragmentActivity, boolean z) {
        try {
            Trace.beginSection("setFragmentViewPagerMode");
            if (isFragmentViewPagerMode()) {
                return;
            }
            removeAllTabs();
            setNavigationMode(2);
            this.mViewPagerController = new ActionBarViewPagerController(this, this.mFragmentManager, fragmentActivity.getLifecycle(), z);
            ScrollingTabContainerView scrollingTabContainerView = this.mTabScrollView;
            if (scrollingTabContainerView != null) {
                addOnFragmentViewPagerChangeListener(scrollingTabContainerView);
            }
            ScrollingTabContainerView scrollingTabContainerView2 = this.mExpanedTabScrollView;
            if (scrollingTabContainerView2 != null) {
                addOnFragmentViewPagerChangeListener(scrollingTabContainerView2);
            }
            ScrollingTabContainerView scrollingTabContainerView3 = this.mCollapseTabScrollView;
            if (scrollingTabContainerView3 != null) {
                addOnFragmentViewPagerChangeListener(scrollingTabContainerView3);
            }
            ScrollingTabContainerView scrollingTabContainerView4 = this.mExpandTabScrollView;
            if (scrollingTabContainerView4 != null) {
                addOnFragmentViewPagerChangeListener(scrollingTabContainerView4);
            }
            ActionBarContainer actionBarContainer = this.mSplitView;
            if (actionBarContainer != null) {
                addOnFragmentViewPagerChangeListener(actionBarContainer);
            }
        } finally {
            Trace.endSection();
        }
    }

    @Override // miuix.appcompat.app.ActionBar
    public void addOnFragmentViewPagerChangeListener(ActionBar.FragmentViewPagerChangeListener fragmentViewPagerChangeListener) {
        this.mViewPagerController.addOnFragmentViewPagerChangeListener(fragmentViewPagerChangeListener);
    }

    @Override // miuix.appcompat.app.ActionBar
    public void removeOnFragmentViewPagerChangeListener(ActionBar.FragmentViewPagerChangeListener fragmentViewPagerChangeListener) {
        this.mViewPagerController.removeOnFragmentViewPagerChangeListener(fragmentViewPagerChangeListener);
    }

    @Override // miuix.appcompat.app.ActionBar
    public int getFragmentTabCount() {
        return this.mViewPagerController.getFragmentTabCount();
    }

    @Override // miuix.appcompat.app.ActionBar
    public Fragment getFragmentAt(int i) {
        return this.mViewPagerController.getFragmentAt(i);
    }

    @Override // miuix.appcompat.app.ActionBar
    public int addFragmentTab(String str, ActionBar.Tab tab, Class<? extends Fragment> cls, Bundle bundle, boolean z) {
        try {
            Trace.beginSection("addFragmentTab");
            return this.mViewPagerController.addFragmentTab(str, tab, cls, bundle, z);
        } finally {
            Trace.endSection();
        }
    }

    @Override // miuix.appcompat.app.ActionBar
    public int addFragmentTab(String str, ActionBar.Tab tab, int i, Class<? extends Fragment> cls, Bundle bundle, boolean z) {
        try {
            Trace.beginSection("addFragmentTab");
            return this.mViewPagerController.addFragmentTab(str, tab, i, cls, bundle, z);
        } finally {
            Trace.endSection();
        }
    }

    @Override // miuix.appcompat.app.ActionBar
    public void removeFragmentTabAt(int i) {
        this.mViewPagerController.removeFragmentAt(i);
    }

    @Override // miuix.appcompat.app.ActionBar
    public void removeAllFragmentTab() {
        this.mViewPagerController.removeAllFragmentTab();
    }

    @Override // miuix.appcompat.app.ActionBar
    public void setStartView(View view) {
        this.mActionView.setStartView(view);
    }

    @Override // miuix.appcompat.app.ActionBar
    public void setEndView(View view) {
        this.mActionView.setEndView(view);
    }

    @Override // miuix.appcompat.app.ActionBar
    public View getEndView() {
        return this.mActionView.getEndView();
    }

    @Override // miuix.appcompat.app.ActionBar
    public void setResizable(boolean z) {
        this.mActionView.setResizable(z);
    }

    public boolean isResizable() {
        return this.mActionView.isResizable();
    }

    public int getExpandState() {
        return this.mActionView.getExpandState();
    }

    @Override // miuix.appcompat.app.ActionBar
    public void setExpandState(int i) {
        this.mActionView.setExpandState(i);
    }

    public void setExpandState(int i, boolean z) {
        this.mActionView.setExpandState(i, z);
    }

    public final void createContextView(boolean z, boolean z2) {
        ActionBarContainer actionBarContainer;
        ViewStub viewStub = (ViewStub) this.mOverlayLayout.findViewById(R$id.split_action_bar_vs);
        if (viewStub != null) {
            actionBarContainer = (ActionBarContainer) viewStub.inflate();
        } else {
            actionBarContainer = (ActionBarContainer) this.mOverlayLayout.findViewById(R$id.split_action_bar);
        }
        if (actionBarContainer != null) {
            this.mActionView.setSplitView(actionBarContainer);
            this.mActionView.setSplitActionBar(z);
            this.mActionView.setSplitWhenNarrow(z2);
            this.mOverlayLayout.setSplitActionBarView(actionBarContainer);
            addContentMask();
            this.mOverlayLayout.setSplitActionBarView(actionBarContainer);
            addContentMask();
            this.mOverlayLayout.setSplitActionBarView(actionBarContainer);
            addContentMask();
            ViewStub viewStub2 = (ViewStub) this.mOverlayLayout.findViewById(R$id.action_context_bar_vs);
            if (viewStub2 != null) {
                this.mContextView = (ActionBarContextView) viewStub2.inflate();
            } else {
                this.mContextView = (ActionBarContextView) this.mOverlayLayout.findViewById(R$id.action_context_bar);
            }
            ActionBarContextView actionBarContextView = this.mContextView;
            if (actionBarContextView == null) {
                return;
            }
            this.mContainerView.setActionBarContextView(actionBarContextView);
            this.mOverlayLayout.setActionBarContextView(this.mContextView);
            this.mContextView.setSplitView(actionBarContainer);
            this.mContextView.setSplitActionBar(z);
            this.mContextView.setSplitWhenNarrow(z2);
        }
    }

    public final void addContentMask() {
        View findViewById;
        ViewStub viewStub = (ViewStub) this.mOverlayLayout.findViewById(R$id.content_mask_vs);
        if (viewStub != null) {
            findViewById = viewStub.inflate();
        } else {
            findViewById = this.mOverlayLayout.findViewById(R$id.content_mask);
        }
        this.mOverlayLayout.setContentMask(findViewById);
    }

    public final void ensureTabsExist() {
        if (this.mTabScrollView != null) {
            return;
        }
        CollapseTabContainer collapseTabContainer = new CollapseTabContainer(this.mContext);
        ExpandTabContainer expandTabContainer = new ExpandTabContainer(this.mContext);
        SecondaryCollapseTabContainer secondaryCollapseTabContainer = new SecondaryCollapseTabContainer(this.mContext);
        SecondaryExpandTabContainer secondaryExpandTabContainer = new SecondaryExpandTabContainer(this.mContext);
        collapseTabContainer.setVisibility(0);
        expandTabContainer.setVisibility(0);
        secondaryCollapseTabContainer.setVisibility(0);
        secondaryExpandTabContainer.setVisibility(0);
        this.mActionView.setEmbeddedTabView(collapseTabContainer, expandTabContainer, secondaryCollapseTabContainer, secondaryExpandTabContainer);
        collapseTabContainer.setEmbeded(true);
        this.mTabScrollView = collapseTabContainer;
        this.mExpanedTabScrollView = expandTabContainer;
        this.mCollapseTabScrollView = secondaryCollapseTabContainer;
        this.mExpandTabScrollView = secondaryExpandTabContainer;
    }

    public final void configureTab(ActionBar.Tab tab, int i) {
        TabImpl tabImpl = (TabImpl) tab;
        if (tabImpl.getCallback() == null) {
            throw new IllegalStateException("Action Bar Tab must have a Callback");
        }
        tabImpl.setPosition(i);
        this.mTabs.add(i, tabImpl);
        int size = this.mTabs.size();
        while (true) {
            i++;
            if (i >= size) {
                return;
            }
            this.mTabs.get(i).setPosition(i);
        }
    }

    public final void cleanupTabs() {
        if (this.mSelectedTab != null) {
            selectTab(null);
        }
        this.mTabs.clear();
        ScrollingTabContainerView scrollingTabContainerView = this.mTabScrollView;
        if (scrollingTabContainerView != null) {
            scrollingTabContainerView.removeAllTabs();
        }
        ScrollingTabContainerView scrollingTabContainerView2 = this.mExpanedTabScrollView;
        if (scrollingTabContainerView2 != null) {
            scrollingTabContainerView2.removeAllTabs();
        }
        ScrollingTabContainerView scrollingTabContainerView3 = this.mCollapseTabScrollView;
        if (scrollingTabContainerView3 != null) {
            scrollingTabContainerView3.removeAllTabs();
        }
        ScrollingTabContainerView scrollingTabContainerView4 = this.mExpandTabScrollView;
        if (scrollingTabContainerView4 != null) {
            scrollingTabContainerView4.removeAllTabs();
        }
        this.mSavedTabPosition = -1;
    }

    public final void updateVisibility(boolean z) {
        updateVisibility(z, null);
    }

    public final void updateVisibility(boolean z, AnimState animState) {
        if (checkShowingFlags(this.mHiddenByApp, this.mHiddenBySystem, this.mShowingForMode)) {
            if (this.mNowShowing) {
                return;
            }
            this.mNowShowing = true;
            doShow(z, animState);
        } else if (!this.mNowShowing) {
        } else {
            this.mNowShowing = false;
            doHide(z, animState);
        }
    }

    public final IStateStyle startContainerViewAnimation(boolean z, String str, AnimState animState, AnimState animState2) {
        int height = this.mContainerView.getHeight();
        if (z) {
            AnimConfig animConfig = new AnimConfig();
            animConfig.setEase(EaseManager.getStyle(-2, 0.9f, 0.25f));
            if (animState2 == null) {
                animState2 = new AnimState(str).add(ViewProperty.TRANSLATION_Y, SearchStatUtils.POW).add(ViewProperty.ALPHA, 1.0d);
            }
            IStateStyle state = Folme.useAt(this.mContainerView).state();
            if (animState != null) {
                animState.setTag(str);
                state = state.setTo(animState);
            }
            return state.to(animState2, animConfig);
        }
        AnimConfig animConfig2 = new AnimConfig();
        animConfig2.setEase(EaseManager.getStyle(-2, 1.0f, 0.35f));
        animConfig2.addListeners(new ViewHideTransitionListener(this.mContainerView, this));
        if (animState2 == null) {
            animState2 = new AnimState(str).add(ViewProperty.TRANSLATION_Y, (-height) - 100).add(ViewProperty.ALPHA, SearchStatUtils.POW);
        }
        IStateStyle state2 = Folme.useAt(this.mContainerView).state();
        if (animState != null) {
            animState.setTag(str);
            state2 = state2.setTo(animState);
        }
        return state2.to(animState2, animConfig2);
    }

    public final int getSplitHeight() {
        View childAt;
        int height = this.mSplitView.getHeight();
        if (this.mSplitView.getChildCount() != 1 || (childAt = this.mSplitView.getChildAt(0)) == null || !(childAt instanceof PhoneActionMenuView)) {
            return height;
        }
        PhoneActionMenuView phoneActionMenuView = (PhoneActionMenuView) childAt;
        return !phoneActionMenuView.isOverflowMenuShowing() ? phoneActionMenuView.getCollapsedHeight() : height;
    }

    public final IStateStyle startSplitViewAnimation(boolean z, String str, AnimState animState) {
        int splitHeight = getSplitHeight();
        if (z) {
            AnimConfig animConfig = new AnimConfig();
            animConfig.setEase(EaseManager.getStyle(-2, 0.9f, 0.25f));
            AnimState add = new AnimState(str).add(ViewProperty.TRANSLATION_Y, SearchStatUtils.POW).add(ViewProperty.ALPHA, 1.0d);
            IStateStyle state = Folme.useAt(this.mSplitView).state();
            if (animState != null) {
                animState.setTag(str);
                state = state.setTo(animState);
            }
            return state.to(add, animConfig);
        }
        AnimConfig animConfig2 = new AnimConfig();
        animConfig2.setEase(EaseManager.getStyle(-2, 1.0f, 0.35f));
        animConfig2.addListeners(new ViewHideTransitionListener(this.mSplitView, this));
        AnimState add2 = new AnimState(str).add(ViewProperty.TRANSLATION_Y, splitHeight + 100).add(ViewProperty.ALPHA, SearchStatUtils.POW);
        IStateStyle state2 = Folme.useAt(this.mSplitView).state();
        if (animState != null) {
            animState.setTag(str);
            state2 = state2.setTo(animState);
        }
        return state2.to(add2, animConfig2);
    }

    public final void doShow(boolean z) {
        doShow(z, null);
    }

    public final void doShow(boolean z, AnimState animState) {
        AnimState animState2;
        View childAt;
        IStateStyle iStateStyle = this.mContainerAnim;
        AnimState animState3 = null;
        if (iStateStyle != null) {
            animState2 = iStateStyle.getCurrentState();
            this.mContainerAnim.cancel();
        } else {
            animState2 = null;
        }
        boolean z2 = isShowHideAnimationEnabled() || z;
        this.mContainerView.setVisibility(this.mActionMode instanceof SearchActionMode ? 8 : 0);
        if (z2) {
            this.mContainerAnim = startContainerViewAnimation(true, "ShowActionBar", animState2, animState);
        } else {
            this.mContainerView.setTranslationY(0.0f);
            this.mContainerView.setAlpha(1.0f);
        }
        if (this.mSplitView != null) {
            IStateStyle iStateStyle2 = this.mSpliterAnim;
            if (iStateStyle2 != null) {
                animState3 = iStateStyle2.getCurrentState();
                this.mSpliterAnim.cancel();
            }
            this.mSplitView.setVisibility(0);
            if (z2) {
                this.mSpliterAnim = startSplitViewAnimation(true, "SpliterShow", animState3);
                if (this.mActionView.isSplitActionBar() && this.mSplitView.getChildCount() > 0 && (childAt = this.mSplitView.getChildAt(0)) != null && (childAt instanceof PhoneActionMenuView) && (!((PhoneActionMenuView) childAt).isOverflowMenuShowing())) {
                    ((ActionMenuView) childAt).startLayoutAnimation();
                }
            } else {
                this.mSplitView.setTranslationY(0.0f);
                this.mSplitView.setAlpha(1.0f);
            }
            updateContentMaskVisibility(true);
        }
    }

    public final void doHide(boolean z) {
        doHide(z, null);
    }

    public final void doHide(boolean z, AnimState animState) {
        AnimState animState2;
        ActionBarContainer actionBarContainer;
        IStateStyle iStateStyle = this.mContainerAnim;
        AnimState animState3 = null;
        if (iStateStyle != null) {
            animState2 = iStateStyle.getCurrentState();
            this.mContainerAnim.cancel();
        } else {
            animState2 = null;
        }
        boolean z2 = isShowHideAnimationEnabled() || z;
        if (z2) {
            this.mContainerAnim = startContainerViewAnimation(false, "HideActionBar", animState2, animState);
        } else {
            this.mContainerView.setTranslationY(-actionBarContainer.getHeight());
            this.mContainerView.setAlpha(0.0f);
            this.mContainerView.setVisibility(8);
        }
        if (this.mSplitView != null) {
            IStateStyle iStateStyle2 = this.mSpliterAnim;
            if (iStateStyle2 != null) {
                animState3 = iStateStyle2.getCurrentState();
                this.mSpliterAnim.cancel();
            }
            if (z2) {
                this.mSpliterAnim = startSplitViewAnimation(false, "SpliterHide", animState3);
            } else {
                this.mSplitView.setTranslationY(getSplitHeight());
                this.mSplitView.setAlpha(0.0f);
                this.mSplitView.setVisibility(8);
            }
            updateContentMaskVisibility(false);
        }
    }

    public boolean isShowHideAnimationEnabled() {
        return this.mShowHideAnimationEnabled;
    }

    @Override // androidx.appcompat.app.ActionBar
    public void setShowHideAnimationEnabled(boolean z) {
        this.mShowHideAnimationEnabled = z;
        if (!z) {
            if (isShowing()) {
                doShow(false);
            } else {
                doHide(false);
            }
        }
    }

    public final void updateContentMaskVisibility(boolean z) {
        if (this.mSplitView.getChildCount() != 2 || !(this.mSplitView.getChildAt(1) instanceof PhoneActionMenuView)) {
            return;
        }
        PhoneActionMenuView phoneActionMenuView = (PhoneActionMenuView) this.mSplitView.getChildAt(1);
        this.mSplitMenuView = phoneActionMenuView;
        if (!phoneActionMenuView.isOverflowMenuShowing() || this.mContentMask == null) {
            return;
        }
        if (z) {
            this.mOverlayLayout.getContentMaskAnimator(this.mContentMaskOnClickListenr).show().start();
        } else {
            this.mOverlayLayout.getContentMaskAnimator(null).hide().start();
        }
    }

    /* loaded from: classes3.dex */
    public class TabImpl extends ActionBar.Tab {
        public ActionBar.TabListener mCallback;
        public CharSequence mContentDesc;
        public View mCustomView;
        public Drawable mIcon;
        public ActionBar.TabListener mInternalCallback;
        public CharSequence mText;
        public int mPosition = -1;
        public boolean mWithAnim = true;

        public TabImpl() {
        }

        public ActionBar.TabListener getCallback() {
            return ActionBarImpl.sTabListenerWrapper;
        }

        public ActionBar.Tab setInternalTabListener(ActionBar.TabListener tabListener) {
            this.mInternalCallback = tabListener;
            return this;
        }

        @Override // androidx.appcompat.app.ActionBar.Tab
        public View getCustomView() {
            return this.mCustomView;
        }

        @Override // androidx.appcompat.app.ActionBar.Tab
        public ActionBar.Tab setCustomView(View view) {
            this.mCustomView = view;
            ActionBarImpl.this.setExpandState(0);
            ActionBarImpl.this.setResizable(false);
            if (this.mPosition >= 0) {
                ActionBarImpl.this.mTabScrollView.updateTab(this.mPosition);
            }
            return this;
        }

        @Override // androidx.appcompat.app.ActionBar.Tab
        public Drawable getIcon() {
            return this.mIcon;
        }

        @Override // androidx.appcompat.app.ActionBar.Tab
        public int getPosition() {
            return this.mPosition;
        }

        public void setPosition(int i) {
            this.mPosition = i;
        }

        @Override // androidx.appcompat.app.ActionBar.Tab
        public CharSequence getText() {
            return this.mText;
        }

        public ActionBar.Tab setText(CharSequence charSequence) {
            this.mText = charSequence;
            if (this.mPosition >= 0) {
                if (ActionBarImpl.this.mTabScrollView != null) {
                    ActionBarImpl.this.mTabScrollView.updateTab(this.mPosition);
                }
                if (ActionBarImpl.this.mExpanedTabScrollView != null) {
                    ActionBarImpl.this.mExpanedTabScrollView.updateTab(this.mPosition);
                }
                if (ActionBarImpl.this.mCollapseTabScrollView != null) {
                    ActionBarImpl.this.mCollapseTabScrollView.updateTab(this.mPosition);
                }
                if (ActionBarImpl.this.mCollapseTabScrollView != null) {
                    ActionBarImpl.this.mCollapseTabScrollView.updateTab(this.mPosition);
                }
            }
            return this;
        }

        @Override // androidx.appcompat.app.ActionBar.Tab
        public ActionBar.Tab setText(int i) {
            return setText(ActionBarImpl.this.mContext.getResources().getText(i));
        }

        @Override // androidx.appcompat.app.ActionBar.Tab
        public void select() {
            ActionBarImpl.this.selectTab(this);
        }

        @Override // androidx.appcompat.app.ActionBar.Tab
        public CharSequence getContentDescription() {
            return this.mContentDesc;
        }
    }

    /* loaded from: classes3.dex */
    public static class ViewHideTransitionListener extends TransitionListener {
        public final WeakReference<ActionBarImpl> mActionBarRef;
        public final WeakReference<View> mRef;

        public ViewHideTransitionListener(View view, ActionBarImpl actionBarImpl) {
            this.mRef = new WeakReference<>(view);
            this.mActionBarRef = new WeakReference<>(actionBarImpl);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onComplete(Object obj, UpdateInfo updateInfo) {
            ActionBarImpl actionBarImpl = this.mActionBarRef.get();
            View view = this.mRef.get();
            if (view == null || actionBarImpl.mNowShowing) {
                return;
            }
            view.setVisibility(8);
        }
    }
}
