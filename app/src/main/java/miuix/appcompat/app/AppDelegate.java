package miuix.appcompat.app;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import androidx.appcompat.view.WindowCallbackWrapper;
import androidx.core.os.TraceCompat;
import miuix.appcompat.R$attr;
import miuix.appcompat.R$bool;
import miuix.appcompat.R$id;
import miuix.appcompat.R$layout;
import miuix.appcompat.R$styleable;
import miuix.appcompat.app.floatingactivity.FloatingActivitySwitcher;
import miuix.appcompat.app.floatingactivity.OnFloatingActivityCallback;
import miuix.appcompat.app.floatingactivity.OnFloatingCallback;
import miuix.appcompat.app.floatingactivity.OnFloatingModeCallback;
import miuix.appcompat.app.floatingactivity.helper.BaseFloatingActivityHelper;
import miuix.appcompat.app.floatingactivity.helper.FloatingHelperFactory;
import miuix.appcompat.app.floatingactivity.multiapp.MultiAppFloatingActivitySwitcher;
import miuix.appcompat.internal.app.widget.ActionBarContainer;
import miuix.appcompat.internal.app.widget.ActionBarImpl;
import miuix.appcompat.internal.app.widget.ActionBarOverlayLayout;
import miuix.appcompat.internal.app.widget.ActionBarView;
import miuix.appcompat.internal.view.menu.MenuBuilder;
import miuix.core.util.variable.WindowWrapper;
import miuix.internal.util.AttributeResolver;
import miuix.internal.util.DeviceHelper;
import miuix.view.SearchActionMode;
import org.jcodec.containers.mp4.boxes.Box;

/* loaded from: classes3.dex */
public class AppDelegate extends ActionBarDelegateImpl {
    public ActionBarContainer mActionBarContainer;
    public ActivityCallback mActivityCallback;
    public AppCompatWindowCallback mAppCompatWindowCallback;
    public ViewGroup mContentParent;
    public OnFloatingModeCallback mFloatingModeCallback;
    public ViewGroup mFloatingRoot;
    public BaseFloatingActivityHelper mFloatingWindowHelper;
    public final Runnable mInvalidateMenuRunnable;
    public boolean mIsFloatingTheme;
    public boolean mIsFloatingWindow;
    public LayoutInflater mLayoutInflater;
    public ActionBarOverlayLayout mSubDecor;
    public Window mWindow;

    public AppDelegate(AppCompatActivity appCompatActivity, ActivityCallback activityCallback, OnFloatingModeCallback onFloatingModeCallback) {
        super(appCompatActivity);
        this.mIsFloatingTheme = false;
        this.mIsFloatingWindow = false;
        this.mFloatingRoot = null;
        this.mInvalidateMenuRunnable = new Runnable() { // from class: miuix.appcompat.app.AppDelegate.1
            @Override // java.lang.Runnable
            public void run() {
                MenuBuilder createMenu = AppDelegate.this.createMenu();
                if (!AppDelegate.this.isImmersionMenuEnabled() && AppDelegate.this.mActivityCallback.onCreatePanelMenu(0, createMenu) && AppDelegate.this.mActivityCallback.onPreparePanel(0, null, createMenu)) {
                    AppDelegate.this.setMenu(createMenu);
                } else {
                    AppDelegate.this.setMenu(null);
                }
            }
        };
        this.mActivityCallback = activityCallback;
        this.mFloatingModeCallback = onFloatingModeCallback;
    }

    @Override // miuix.appcompat.app.ActionBarDelegateImpl
    public void onCreate(Bundle bundle) {
        ApplicationInfo applicationInfo;
        Bundle bundle2;
        Bundle bundle3;
        TraceCompat.beginSection("AppDelegate#onCreate");
        this.mActivityCallback.onCreate(bundle);
        installSubDecor();
        ActivityInfo activityInfo = null;
        try {
            applicationInfo = this.mActivity.getPackageManager().getApplicationInfo(this.mActivity.getPackageName(), 128);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            applicationInfo = null;
        }
        boolean z = false;
        int i = (applicationInfo == null || (bundle3 = applicationInfo.metaData) == null) ? 0 : bundle3.getInt("miui.extra.window.padding.level", 0);
        try {
            activityInfo = this.mActivity.getPackageManager().getActivityInfo(this.mActivity.getComponentName(), 128);
        } catch (PackageManager.NameNotFoundException e2) {
            e2.printStackTrace();
        }
        if (activityInfo != null && (bundle2 = activityInfo.metaData) != null) {
            i = bundle2.getInt("miui.extra.window.padding.level", i);
        }
        int resolveInt = AttributeResolver.resolveInt(this.mActivity, R$attr.windowExtraPaddingHorizontal, i);
        if (resolveInt != 0) {
            z = true;
        }
        boolean resolveBoolean = AttributeResolver.resolveBoolean(this.mActivity, R$attr.windowExtraPaddingHorizontalEnable, z);
        setExtraHorizontalPaddingLevel(resolveInt);
        setExtraHorizontalPaddingEnable(resolveBoolean);
        TraceCompat.endSection();
    }

    public final void ensureWindow() {
        AppCompatActivity appCompatActivity;
        Window window = this.mWindow;
        if (window != null) {
            return;
        }
        if (window == null && (appCompatActivity = this.mActivity) != null) {
            attachToWindow(appCompatActivity.getWindow());
        }
        if (this.mWindow == null) {
            throw new IllegalStateException("We have not been given a Window");
        }
    }

    public final void attachToWindow(Window window) {
        if (this.mWindow != null) {
            throw new IllegalStateException("AppCompat has already installed itself into the Window");
        }
        Window.Callback callback = window.getCallback();
        if (callback instanceof AppCompatWindowCallback) {
            throw new IllegalStateException("AppCompat has already installed itself into the Window");
        }
        AppCompatWindowCallback appCompatWindowCallback = new AppCompatWindowCallback(callback);
        this.mAppCompatWindowCallback = appCompatWindowCallback;
        window.setCallback(appCompatWindowCallback);
        this.mWindow = window;
    }

    @Override // miuix.appcompat.app.ActionBarDelegate
    public ActionBar createActionBar() {
        if (!this.mSubDecorInstalled) {
            installSubDecor();
        }
        if (this.mSubDecor == null) {
            return null;
        }
        return new ActionBarImpl(this.mActivity, this.mSubDecor);
    }

    @Override // miuix.appcompat.app.ActionBarDelegateImpl
    public Context getThemedContext() {
        return this.mActivity;
    }

    @Override // miuix.appcompat.app.ActionBarDelegateImpl
    public void onStop() {
        this.mActivityCallback.onStop();
        dismissImmersionMenu(false);
        ActionBarImpl actionBarImpl = (ActionBarImpl) getActionBar();
        if (actionBarImpl != null) {
            actionBarImpl.setShowHideAnimationEnabled(false);
        }
    }

    @Override // miuix.appcompat.app.ActionBarDelegateImpl
    public void onPostResume() {
        this.mActivityCallback.onPostResume();
        ActionBarImpl actionBarImpl = (ActionBarImpl) getActionBar();
        if (actionBarImpl != null) {
            actionBarImpl.setShowHideAnimationEnabled(true);
        }
    }

    public View onCreatePanelView(int i) {
        if (i != 0) {
            return this.mActivityCallback.onCreatePanelView(i);
        }
        if (!isImmersionMenuEnabled()) {
            MenuBuilder menuBuilder = this.mMenu;
            boolean z = true;
            if (this.mActionMode == null) {
                if (menuBuilder == null) {
                    menuBuilder = createMenu();
                    setMenu(menuBuilder);
                    menuBuilder.stopDispatchingItemsChanged();
                    z = this.mActivityCallback.onCreatePanelMenu(0, menuBuilder);
                }
                if (z) {
                    menuBuilder.stopDispatchingItemsChanged();
                    z = this.mActivityCallback.onPreparePanel(0, null, menuBuilder);
                }
            } else if (menuBuilder == null) {
                z = false;
            }
            if (z) {
                menuBuilder.startDispatchingItemsChanged();
            } else {
                setMenu(null);
            }
        }
        return null;
    }

    public boolean onCreatePanelMenu(int i, Menu menu) {
        return i != 0 && this.mActivityCallback.onCreatePanelMenu(i, menu);
    }

    public boolean onPreparePanel(int i, View view, Menu menu) {
        return i != 0 && this.mActivityCallback.onPreparePanel(i, view, menu);
    }

    @Override // miuix.appcompat.app.ActionBarDelegate
    public boolean onMenuItemSelected(int i, MenuItem menuItem) {
        boolean onNavigateUpFromChild;
        if (this.mActivityCallback.onMenuItemSelected(i, menuItem)) {
            return true;
        }
        if (i == 0 && menuItem.getItemId() == 16908332 && getActionBar() != null && (getActionBar().getDisplayOptions() & 4) != 0) {
            if (this.mActivity.getParent() == null) {
                onNavigateUpFromChild = this.mActivity.onNavigateUp();
            } else {
                onNavigateUpFromChild = this.mActivity.getParent().onNavigateUpFromChild(this.mActivity);
            }
            if (!onNavigateUpFromChild) {
                this.mActivity.finish();
            }
        }
        return false;
    }

    public final void installSubDecor() {
        ActionBarOverlayLayout actionBarOverlayLayout;
        boolean z;
        if (this.mSubDecorInstalled) {
            return;
        }
        ensureWindow();
        this.mSubDecorInstalled = true;
        Window window = this.mActivity.getWindow();
        this.mLayoutInflater = window.getLayoutInflater();
        TypedArray obtainStyledAttributes = this.mActivity.obtainStyledAttributes(R$styleable.Window);
        if (obtainStyledAttributes.getInt(R$styleable.Window_windowLayoutMode, 0) == 1) {
            this.mActivity.getWindow().setGravity(80);
        }
        int i = R$styleable.Window_windowActionBar;
        if (!obtainStyledAttributes.hasValue(i)) {
            obtainStyledAttributes.recycle();
            throw new IllegalStateException("You need to use a miui theme (or descendant) with this activity.");
        }
        if (obtainStyledAttributes.getBoolean(i, false)) {
            requestWindowFeature(8);
        }
        if (obtainStyledAttributes.getBoolean(R$styleable.Window_windowActionBarOverlay, false)) {
            requestWindowFeature(9);
        }
        this.mIsFloatingTheme = obtainStyledAttributes.getBoolean(R$styleable.Window_isMiuixFloatingTheme, false);
        this.mIsFloatingWindow = obtainStyledAttributes.getBoolean(R$styleable.Window_windowFloating, false);
        setTranslucentStatus(obtainStyledAttributes.getInt(R$styleable.Window_windowTranslucentStatus, 0));
        installToDecor(window);
        ActionBarOverlayLayout actionBarOverlayLayout2 = this.mSubDecor;
        if (actionBarOverlayLayout2 != null) {
            actionBarOverlayLayout2.setCallback(this.mActivity);
            this.mSubDecor.setTranslucentStatus(getTranslucentStatus());
        }
        if (this.mHasActionBar && (actionBarOverlayLayout = this.mSubDecor) != null) {
            this.mActionBarContainer = (ActionBarContainer) actionBarOverlayLayout.findViewById(R$id.action_bar_container);
            this.mSubDecor.setOverlayMode(this.mOverlayActionBar);
            ActionBarView actionBarView = (ActionBarView) this.mSubDecor.findViewById(R$id.action_bar);
            this.mActionBarView = actionBarView;
            actionBarView.setWindowCallback(this.mActivity);
            if (this.mFeatureIndeterminateProgress) {
                this.mActionBarView.initIndeterminateProgress();
            }
            this.mImmersionLayoutResourceId = obtainStyledAttributes.getResourceId(R$styleable.Window_immersionMenuLayout, 0);
            if (isImmersionMenuEnabled()) {
                this.mActionBarView.initImmersionMore(this.mImmersionLayoutResourceId, this);
            }
            if (this.mActionBarView.getCustomNavigationView() != null) {
                ActionBarView actionBarView2 = this.mActionBarView;
                actionBarView2.setDisplayOptions(actionBarView2.getDisplayOptions() | 16);
            }
            boolean equals = "splitActionBarWhenNarrow".equals(getUiOptionsFromMetadata());
            if (equals) {
                z = this.mActivity.getResources().getBoolean(R$bool.abc_split_action_bar_is_narrow);
            } else {
                z = obtainStyledAttributes.getBoolean(R$styleable.Window_windowSplitActionBar, false);
            }
            if (z) {
                addSplitActionBar(z, equals, this.mSubDecor);
            }
            this.mActivity.getWindow().getDecorView().post(this.mInvalidateMenuRunnable);
        }
        if (obtainStyledAttributes.getBoolean(R$styleable.Window_immersionMenuEnabled, false)) {
            setImmersionMenuEnabled(true);
        }
        obtainStyledAttributes.recycle();
    }

    public void setFloatingWindowMode(boolean z) {
        setFloatingWindowMode(z, true, false);
    }

    public boolean isFloatingTheme() {
        return this.mIsFloatingTheme;
    }

    public boolean isInFloatingWindowMode() {
        return shouldShowFloatingActivityTabletMode();
    }

    public View getFloatingBrightPanel() {
        BaseFloatingActivityHelper baseFloatingActivityHelper = this.mFloatingWindowHelper;
        if (baseFloatingActivityHelper != null) {
            return baseFloatingActivityHelper.getFloatingBrightPanel();
        }
        return null;
    }

    public final void setFloatingWindowMode(boolean z, boolean z2, boolean z3) {
        if (this.mIsFloatingTheme) {
            if ((!z3 && !DeviceHelper.isTablet(this.mActivity)) || this.mIsFloatingWindow == z || !this.mFloatingModeCallback.onFloatingWindowModeChanging(z)) {
                return;
            }
            this.mIsFloatingWindow = z;
            this.mFloatingWindowHelper.setFloatingWindowMode(z);
            if (this.mSubDecor != null) {
                ViewGroup.LayoutParams floatingLayoutParam = this.mFloatingWindowHelper.getFloatingLayoutParam();
                if (z) {
                    floatingLayoutParam.height = -2;
                    floatingLayoutParam.width = -2;
                } else {
                    floatingLayoutParam.height = -1;
                    floatingLayoutParam.width = -1;
                }
                this.mSubDecor.setLayoutParams(floatingLayoutParam);
                this.mSubDecor.requestLayout();
            }
            ActionBarOverlayLayout actionBarOverlayLayout = this.mSubDecor;
            if (actionBarOverlayLayout != null) {
                actionBarOverlayLayout.onFloatingModeChanged(z);
            }
            if (!z2) {
                return;
            }
            notifyFloatWindowModeChanged(z);
        }
    }

    public final void notifyFloatWindowModeChanged(boolean z) {
        this.mFloatingModeCallback.onFloatingWindowModeChanged(z);
    }

    public void setContentView(int i) {
        if (!this.mSubDecorInstalled) {
            installSubDecor();
        }
        ViewGroup viewGroup = this.mContentParent;
        if (viewGroup != null) {
            viewGroup.removeAllViews();
            this.mLayoutInflater.inflate(i, this.mContentParent);
        }
        this.mAppCompatWindowCallback.getWrapped().onContentChanged();
    }

    public void setContentView(View view) {
        setContentView(view, new ViewGroup.LayoutParams(-1, -1));
    }

    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        if (!this.mSubDecorInstalled) {
            installSubDecor();
        }
        ViewGroup viewGroup = this.mContentParent;
        if (viewGroup != null) {
            viewGroup.removeAllViews();
            this.mContentParent.addView(view, layoutParams);
        }
        this.mAppCompatWindowCallback.getWrapped().onContentChanged();
    }

    public void addContentView(View view, ViewGroup.LayoutParams layoutParams) {
        if (!this.mSubDecorInstalled) {
            installSubDecor();
        }
        ViewGroup viewGroup = this.mContentParent;
        if (viewGroup != null) {
            viewGroup.addView(view, layoutParams);
        }
        this.mAppCompatWindowCallback.getWrapped().onContentChanged();
    }

    public final boolean shouldShowFloatingActivityTabletMode() {
        BaseFloatingActivityHelper baseFloatingActivityHelper = this.mFloatingWindowHelper;
        return baseFloatingActivityHelper != null && baseFloatingActivityHelper.isFloatingModeSupport();
    }

    public final void installToDecor(Window window) {
        this.mFloatingWindowHelper = this.mIsFloatingTheme ? FloatingHelperFactory.get(this.mActivity) : null;
        this.mFloatingRoot = null;
        View inflate = View.inflate(this.mActivity, getDecorViewLayoutRes(window), null);
        ViewGroup viewGroup = inflate;
        if (this.mFloatingWindowHelper != null) {
            boolean shouldShowFloatingActivityTabletMode = shouldShowFloatingActivityTabletMode();
            this.mIsFloatingWindow = shouldShowFloatingActivityTabletMode;
            this.mFloatingWindowHelper.setFloatingWindowMode(shouldShowFloatingActivityTabletMode);
            ViewGroup replaceSubDecor = this.mFloatingWindowHelper.replaceSubDecor(inflate, this.mIsFloatingWindow);
            this.mFloatingRoot = replaceSubDecor;
            this.mActivity.getWindow().setFlags(Box.MAX_BOX_SIZE, Box.MAX_BOX_SIZE);
            viewGroup = replaceSubDecor;
        }
        View findViewById = viewGroup.findViewById(R$id.action_bar_overlay_layout);
        if (findViewById instanceof ActionBarOverlayLayout) {
            ActionBarOverlayLayout actionBarOverlayLayout = (ActionBarOverlayLayout) findViewById;
            this.mSubDecor = actionBarOverlayLayout;
            ViewGroup viewGroup2 = (ViewGroup) actionBarOverlayLayout.findViewById(16908290);
            ViewGroup viewGroup3 = (ViewGroup) window.findViewById(16908290);
            if (viewGroup3 != null) {
                while (viewGroup3.getChildCount() > 0) {
                    View childAt = viewGroup3.getChildAt(0);
                    viewGroup3.removeViewAt(0);
                    viewGroup2.addView(childAt);
                }
                viewGroup3.setId(-1);
                viewGroup2.setId(16908290);
                if (viewGroup3 instanceof FrameLayout) {
                    ((FrameLayout) viewGroup3).setForeground(null);
                }
            }
        }
        window.setContentView(viewGroup);
        ActionBarOverlayLayout actionBarOverlayLayout2 = this.mSubDecor;
        if (actionBarOverlayLayout2 != null) {
            this.mContentParent = (ViewGroup) actionBarOverlayLayout2.findViewById(16908290);
        }
        BaseFloatingActivityHelper baseFloatingActivityHelper = this.mFloatingWindowHelper;
        if (baseFloatingActivityHelper != null) {
            baseFloatingActivityHelper.init(this.mFloatingRoot, shouldShowFloatingActivityTabletMode());
        }
    }

    public void onTitleChanged(CharSequence charSequence) {
        ActionBarView actionBarView = this.mActionBarView;
        if (actionBarView != null) {
            actionBarView.setWindowTitle(charSequence);
        }
    }

    @Override // miuix.appcompat.app.ActionBarDelegateImpl
    public View getView() {
        return this.mSubDecor;
    }

    public final int getDecorViewLayoutRes(Window window) {
        int i;
        Context context = window.getContext();
        if (AttributeResolver.resolveBoolean(context, R$attr.windowActionBar, false)) {
            if (AttributeResolver.resolveBoolean(context, R$attr.windowActionBarMovable, false)) {
                i = R$layout.miuix_appcompat_screen_action_bar_movable;
            } else {
                i = R$layout.miuix_appcompat_screen_action_bar;
            }
        } else {
            i = R$layout.miuix_appcompat_screen_simple;
        }
        int resolve = AttributeResolver.resolve(context, R$attr.startingWindowOverlay);
        if (resolve > 0 && isSystemProcess() && isWindowActionBarEnabled(context)) {
            i = resolve;
        }
        if (!window.isFloating() && (window.getCallback() instanceof Dialog)) {
            WindowWrapper.setTranslucentStatus(window, AttributeResolver.resolveInt(context, R$attr.windowTranslucentStatus, 0));
        }
        return i;
    }

    public final boolean isSystemProcess() {
        return "android".equals(getActivity().getApplicationContext().getApplicationInfo().packageName);
    }

    public static boolean isWindowActionBarEnabled(Context context) {
        return AttributeResolver.resolveBoolean(context, R$attr.windowActionBar, true);
    }

    @Override // miuix.appcompat.app.ActionBarDelegateImpl
    public boolean onCreateImmersionMenu(MenuBuilder menuBuilder) {
        return this.mActivity.onCreateOptionsMenu(menuBuilder);
    }

    @Override // miuix.appcompat.app.ActionBarDelegateImpl
    public boolean onPrepareImmersionMenu(MenuBuilder menuBuilder) {
        return this.mActivity.onPrepareOptionsMenu(menuBuilder);
    }

    public void setOnStatusBarChangeListener(OnStatusBarChangeListener onStatusBarChangeListener) {
        ActionBarOverlayLayout actionBarOverlayLayout = this.mSubDecor;
        if (actionBarOverlayLayout != null) {
            actionBarOverlayLayout.setOnStatusBarChangeListener(onStatusBarChangeListener);
        }
    }

    public void setOnFloatingCallback(OnFloatingCallback onFloatingCallback) {
        BaseFloatingActivityHelper baseFloatingActivityHelper = this.mFloatingWindowHelper;
        if (baseFloatingActivityHelper != null) {
            baseFloatingActivityHelper.setOnFloatingCallback(onFloatingCallback);
        }
    }

    public void setOnFloatingWindowCallback(OnFloatingActivityCallback onFloatingActivityCallback) {
        BaseFloatingActivityHelper baseFloatingActivityHelper = this.mFloatingWindowHelper;
        if (baseFloatingActivityHelper != null) {
            baseFloatingActivityHelper.setOnFloatingWindowCallback(onFloatingActivityCallback);
        }
    }

    public void exitFloatingActivityAll() {
        BaseFloatingActivityHelper baseFloatingActivityHelper = this.mFloatingWindowHelper;
        if (baseFloatingActivityHelper != null) {
            baseFloatingActivityHelper.exitFloatingActivityAll();
        }
    }

    public void setEnableSwipToDismiss(boolean z) {
        BaseFloatingActivityHelper baseFloatingActivityHelper = this.mFloatingWindowHelper;
        if (baseFloatingActivityHelper != null) {
            baseFloatingActivityHelper.setEnableSwipToDismiss(z);
        }
    }

    public void hideFloatingDimBackground() {
        BaseFloatingActivityHelper baseFloatingActivityHelper = this.mFloatingWindowHelper;
        if (baseFloatingActivityHelper != null) {
            baseFloatingActivityHelper.hideFloatingDimBackground();
        }
    }

    public void hideFloatingBrightPanel() {
        BaseFloatingActivityHelper baseFloatingActivityHelper = this.mFloatingWindowHelper;
        if (baseFloatingActivityHelper != null) {
            baseFloatingActivityHelper.hideFloatingBrightPanel();
        }
    }

    public void showFloatingBrightPanel() {
        BaseFloatingActivityHelper baseFloatingActivityHelper = this.mFloatingWindowHelper;
        if (baseFloatingActivityHelper != null) {
            baseFloatingActivityHelper.showFloatingBrightPanel();
        }
    }

    public void executeOpenEnterAnimation() {
        BaseFloatingActivityHelper baseFloatingActivityHelper = this.mFloatingWindowHelper;
        if (baseFloatingActivityHelper != null) {
            baseFloatingActivityHelper.executeOpenEnterAnimation();
        }
    }

    public void executeOpenExitAnimation() {
        BaseFloatingActivityHelper baseFloatingActivityHelper = this.mFloatingWindowHelper;
        if (baseFloatingActivityHelper != null) {
            baseFloatingActivityHelper.executeOpenExitAnimation();
        }
    }

    public void executeCloseEnterAnimation() {
        BaseFloatingActivityHelper baseFloatingActivityHelper = this.mFloatingWindowHelper;
        if (baseFloatingActivityHelper != null) {
            baseFloatingActivityHelper.executeCloseEnterAnimation();
        }
    }

    public void executeCloseExitAnimation() {
        BaseFloatingActivityHelper baseFloatingActivityHelper = this.mFloatingWindowHelper;
        if (baseFloatingActivityHelper != null) {
            baseFloatingActivityHelper.executeCloseExitAnimation();
        }
    }

    public void onBackPressed() {
        ActionMode actionMode = this.mActionMode;
        if (actionMode != null) {
            actionMode.finish();
            return;
        }
        ActionBarView actionBarView = this.mActionBarView;
        if (actionBarView != null && actionBarView.hasExpandedActionView()) {
            this.mActionBarView.collapseActionView();
            return;
        }
        BaseFloatingActivityHelper baseFloatingActivityHelper = this.mFloatingWindowHelper;
        if (baseFloatingActivityHelper != null && baseFloatingActivityHelper.onBackPressed()) {
            return;
        }
        this.mActivityCallback.onBackPressed();
    }

    public boolean shouldDelegateActivityFinish() {
        BaseFloatingActivityHelper baseFloatingActivityHelper = this.mFloatingWindowHelper;
        if (baseFloatingActivityHelper != null) {
            return baseFloatingActivityHelper.delegateFinishFloatingActivityInternal();
        }
        return false;
    }

    @Override // miuix.appcompat.app.ActionBarDelegateImpl
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        setFloatingWindowMode(isInFloatingWindowMode(), true, DeviceHelper.isFoldDevice());
        this.mActivityCallback.onConfigurationChanged(configuration);
    }

    public void onSaveInstanceState(Bundle bundle) {
        this.mActivityCallback.onSaveInstanceState(bundle);
        if (bundle != null && this.mFloatingWindowHelper != null) {
            FloatingActivitySwitcher.onSaveInstanceState(this.mActivity, bundle);
            MultiAppFloatingActivitySwitcher.onSaveInstanceState(this.mActivity, bundle);
        }
        if (this.mActionBarContainer != null) {
            SparseArray<? extends Parcelable> sparseArray = new SparseArray<>();
            this.mActionBarContainer.saveHierarchyState(sparseArray);
            bundle.putSparseParcelableArray("miuix:ActionBar", sparseArray);
        }
    }

    public void onRestoreInstanceState(Bundle bundle) {
        SparseArray sparseParcelableArray;
        this.mActivityCallback.onRestoreInstanceState(bundle);
        if (this.mActionBarContainer == null || (sparseParcelableArray = bundle.getSparseParcelableArray("miuix:ActionBar")) == null) {
            return;
        }
        this.mActionBarContainer.restoreHierarchyState(sparseParcelableArray);
    }

    @Override // miuix.appcompat.app.ActionBarDelegate
    public void invalidateOptionsMenu() {
        this.mInvalidateMenuRunnable.run();
    }

    public ActionMode startActionMode(ActionMode.Callback callback) {
        if (callback instanceof SearchActionMode.Callback) {
            addContentMask(this.mSubDecor);
        }
        ActionBarOverlayLayout actionBarOverlayLayout = this.mSubDecor;
        if (actionBarOverlayLayout != null) {
            return actionBarOverlayLayout.startActionMode(callback);
        }
        return null;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuBuilder.Callback
    public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
        return this.mActivity.onMenuItemSelected(0, menuItem);
    }

    public void onActionModeStarted(ActionMode actionMode) {
        this.mActionMode = actionMode;
    }

    public void onActionModeFinished(ActionMode actionMode) {
        this.mActionMode = null;
    }

    @Override // miuix.appcompat.app.ActionBarDelegateImpl
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        if (getActionBar() != null) {
            return ((ActionBarImpl) getActionBar()).startActionMode(callback);
        }
        return super.onWindowStartingActionMode(callback);
    }

    public void setExtraHorizontalPaddingLevel(int i) {
        ActionBarOverlayLayout actionBarOverlayLayout = this.mSubDecor;
        if (actionBarOverlayLayout != null) {
            actionBarOverlayLayout.setExtraHorizontalPaddingLevel(i);
        }
    }

    public int getExtraHorizontalPaddingLevel() {
        ActionBarOverlayLayout actionBarOverlayLayout = this.mSubDecor;
        if (actionBarOverlayLayout != null) {
            return actionBarOverlayLayout.getExtraHorizontalPaddingLevel();
        }
        return 0;
    }

    public void setExtraHorizontalPaddingEnable(boolean z) {
        ActionBarOverlayLayout actionBarOverlayLayout = this.mSubDecor;
        if (actionBarOverlayLayout != null) {
            actionBarOverlayLayout.setExtraHorizontalPaddingEnable(z);
        }
    }

    public boolean isExtraHorizontalPaddingEnable() {
        ActionBarOverlayLayout actionBarOverlayLayout = this.mSubDecor;
        if (actionBarOverlayLayout != null) {
            return actionBarOverlayLayout.isExtraHorizontalPaddingEnable();
        }
        return false;
    }

    /* loaded from: classes3.dex */
    public class AppCompatWindowCallback extends WindowCallbackWrapper {
        @Override // android.view.Window.Callback
        public void onContentChanged() {
        }

        public AppCompatWindowCallback(Window.Callback callback) {
            super(callback);
        }
    }
}
