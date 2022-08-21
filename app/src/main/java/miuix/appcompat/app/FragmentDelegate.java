package miuix.appcompat.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import androidx.fragment.app.FragmentActivity;
import java.lang.ref.WeakReference;
import miuix.appcompat.R$bool;
import miuix.appcompat.R$id;
import miuix.appcompat.R$layout;
import miuix.appcompat.R$styleable;
import miuix.appcompat.internal.app.widget.ActionBarImpl;
import miuix.appcompat.internal.app.widget.ActionBarOverlayLayout;
import miuix.appcompat.internal.app.widget.ActionBarView;
import miuix.appcompat.internal.util.LayoutUIUtils;
import miuix.appcompat.internal.view.SimpleWindowCallback;
import miuix.appcompat.internal.view.menu.MenuBuilder;
import miuix.internal.util.AttributeResolver;
import miuix.view.SearchActionMode;

/* loaded from: classes3.dex */
public class FragmentDelegate extends ActionBarDelegateImpl {
    public int mExtraPaddingLevel;
    public int mExtraThemeRes;
    public androidx.fragment.app.Fragment mFragment;
    public byte mInvalidateMenuFlags;
    public Runnable mInvalidateMenuRunnable;
    public MenuBuilder mMenu;
    public View mSubDecor;
    public Context mThemedContext;
    public final Window.Callback mWindowCallback;

    public static /* synthetic */ byte access$172(FragmentDelegate fragmentDelegate, int i) {
        byte b = (byte) (i & fragmentDelegate.mInvalidateMenuFlags);
        fragmentDelegate.mInvalidateMenuFlags = b;
        return b;
    }

    public FragmentDelegate(androidx.fragment.app.Fragment fragment) {
        super((AppCompatActivity) fragment.getActivity());
        this.mExtraPaddingLevel = 0;
        this.mWindowCallback = new SimpleWindowCallback() { // from class: miuix.appcompat.app.FragmentDelegate.1
            @Override // android.view.Window.Callback
            public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
                return FragmentDelegate.this.onWindowStartingActionMode(callback);
            }

            @Override // android.view.Window.Callback
            public void onActionModeStarted(ActionMode actionMode) {
                ((IFragment) FragmentDelegate.this.mFragment).onActionModeStarted(actionMode);
            }

            @Override // android.view.Window.Callback
            public void onActionModeFinished(ActionMode actionMode) {
                ((IFragment) FragmentDelegate.this.mFragment).onActionModeFinished(actionMode);
            }

            @Override // android.view.Window.Callback
            public boolean onMenuItemSelected(int i, MenuItem menuItem) {
                return FragmentDelegate.this.onMenuItemSelected(i, menuItem);
            }

            @Override // android.view.Window.Callback
            public void onPanelClosed(int i, Menu menu) {
                if (FragmentDelegate.this.getActivity() != null) {
                    FragmentDelegate.this.getActivity().onPanelClosed(i, menu);
                }
            }
        };
        this.mFragment = fragment;
    }

    @Override // miuix.appcompat.app.ActionBarDelegate
    public ActionBar createActionBar() {
        if (this.mFragment.isAdded()) {
            return new ActionBarImpl(this.mFragment);
        }
        return null;
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        TypedArray obtainStyledAttributes = getThemedContext().obtainStyledAttributes(R$styleable.Window);
        int i = R$styleable.Window_windowActionBar;
        if (!obtainStyledAttributes.hasValue(i)) {
            obtainStyledAttributes.recycle();
            throw new IllegalStateException("You need to use a miui theme (or descendant) with this fragment.");
        }
        if (obtainStyledAttributes.getBoolean(i, false)) {
            requestWindowFeature(8);
        }
        if (obtainStyledAttributes.getBoolean(R$styleable.Window_windowActionBarOverlay, false)) {
            requestWindowFeature(9);
        }
        setTranslucentStatus(obtainStyledAttributes.getInt(R$styleable.Window_windowTranslucentStatus, 0));
        setImmersionMenuEnabled(obtainStyledAttributes.getBoolean(R$styleable.Window_immersionMenuEnabled, false));
        this.mImmersionLayoutResourceId = obtainStyledAttributes.getResourceId(R$styleable.Window_immersionMenuLayout, 0);
        obtainStyledAttributes.recycle();
        LayoutInflater cloneInContext = layoutInflater.cloneInContext(getThemedContext());
        if (this.mHasActionBar) {
            installSubDecor(getThemedContext(), viewGroup, cloneInContext);
            ViewGroup viewGroup2 = (ViewGroup) this.mSubDecor.findViewById(16908290);
            View onInflateView = ((IFragment) this.mFragment).onInflateView(cloneInContext, viewGroup2, bundle);
            if (onInflateView != null && onInflateView.getParent() != viewGroup2) {
                if (onInflateView.getParent() != null) {
                    ((ViewGroup) onInflateView.getParent()).removeView(onInflateView);
                }
                viewGroup2.removeAllViews();
                viewGroup2.addView(onInflateView);
            }
        } else {
            this.mSubDecor = ((IFragment) this.mFragment).onInflateView(cloneInContext, viewGroup, bundle);
        }
        return this.mSubDecor;
    }

    public final void installSubDecor(Context context, ViewGroup viewGroup, LayoutInflater layoutInflater) {
        boolean z;
        if (!this.mSubDecorInstalled) {
            FragmentActivity activity = this.mFragment.getActivity();
            if (activity instanceof AppCompatActivity) {
                AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
                setExtraHorizontalPaddingLevel(appCompatActivity.getExtraHorizontalPaddingLevel());
                appCompatActivity.setExtraHorizontalPaddingEnable(false);
            }
            this.mSubDecorInstalled = true;
            ActionBarOverlayLayout actionBarOverlayLayout = (ActionBarOverlayLayout) layoutInflater.inflate(R$layout.miuix_appcompat_screen_action_bar, viewGroup, false);
            actionBarOverlayLayout.setCallback(this.mWindowCallback);
            actionBarOverlayLayout.setRootSubDecor(false);
            actionBarOverlayLayout.setOverlayMode(this.mOverlayActionBar);
            actionBarOverlayLayout.setTranslucentStatus(getTranslucentStatus());
            if (this.mExtraThemeRes != 0) {
                actionBarOverlayLayout.setBackground(AttributeResolver.resolveDrawable(context, 16842836));
            }
            ActionBarView actionBarView = (ActionBarView) actionBarOverlayLayout.findViewById(R$id.action_bar);
            this.mActionBarView = actionBarView;
            actionBarView.setWindowCallback(this.mWindowCallback);
            if (this.mFeatureIndeterminateProgress) {
                this.mActionBarView.initIndeterminateProgress();
            }
            if (isImmersionMenuEnabled()) {
                this.mActionBarView.initImmersionMore(this.mImmersionLayoutResourceId, this);
            }
            boolean equals = "splitActionBarWhenNarrow".equals(getUiOptionsFromMetadata());
            if (equals) {
                z = context.getResources().getBoolean(R$bool.abc_split_action_bar_is_narrow);
            } else {
                TypedArray obtainStyledAttributes = context.obtainStyledAttributes(R$styleable.Window);
                boolean z2 = obtainStyledAttributes.getBoolean(R$styleable.Window_windowSplitActionBar, false);
                obtainStyledAttributes.recycle();
                z = z2;
            }
            if (z) {
                addSplitActionBar(z, equals, actionBarOverlayLayout);
            }
            updateOptionsMenu(1);
            invalidateOptionsMenu();
            this.mSubDecor = actionBarOverlayLayout;
        } else if (this.mSubDecor.getParent() == null || !(this.mSubDecor.getParent() instanceof ViewGroup)) {
        } else {
            ViewGroup viewGroup2 = (ViewGroup) this.mSubDecor.getParent();
            if (viewGroup2.getChildCount() != 0) {
                return;
            }
            viewGroup2.endViewTransition(this.mSubDecor);
        }
    }

    public void setExtraHorizontalPaddingLevel(int i) {
        if (!LayoutUIUtils.isLevelValid(i) || this.mExtraPaddingLevel == i) {
            return;
        }
        this.mExtraPaddingLevel = i;
        View view = this.mSubDecor;
        if (!(view instanceof ActionBarOverlayLayout)) {
            return;
        }
        ((ActionBarOverlayLayout) view).setExtraHorizontalPaddingLevel(i);
    }

    public int getExtraHorizontalPaddingLevel() {
        View view = this.mSubDecor;
        if (view instanceof ActionBarOverlayLayout) {
            return ((ActionBarOverlayLayout) view).getExtraHorizontalPaddingLevel();
        }
        return 0;
    }

    public void setExtraHorizontalPaddingEnable(boolean z) {
        View view = this.mSubDecor;
        if (view instanceof ActionBarOverlayLayout) {
            ((ActionBarOverlayLayout) view).setExtraHorizontalPaddingEnable(z);
        }
    }

    public boolean isExtraHorizontalPaddingEnable() {
        View view = this.mSubDecor;
        if (view instanceof ActionBarOverlayLayout) {
            return ((ActionBarOverlayLayout) view).isExtraHorizontalPaddingEnable();
        }
        return false;
    }

    @Override // miuix.appcompat.app.ActionBarDelegateImpl
    public View getView() {
        return this.mSubDecor;
    }

    public boolean onCreatePanelMenu(int i, Menu menu) {
        if (i == 0) {
            return ((IFragment) this.mFragment).onCreatePanelMenu(i, menu);
        }
        return false;
    }

    @Override // miuix.appcompat.app.ActionBarDelegate
    public boolean onMenuItemSelected(int i, MenuItem menuItem) {
        if (i == 0) {
            return this.mFragment.onOptionsItemSelected(menuItem);
        }
        if (i != 6) {
            return false;
        }
        return this.mFragment.onContextItemSelected(menuItem);
    }

    public boolean onPreparePanel(int i, View view, Menu menu) {
        if (i == 0) {
            ((IFragment) this.mFragment).onPreparePanel(i, null, menu);
            return true;
        }
        return false;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuBuilder.Callback
    public boolean onMenuItemSelected(MenuBuilder menuBuilder, MenuItem menuItem) {
        return onMenuItemSelected(0, menuItem);
    }

    @Override // miuix.appcompat.app.ActionBarDelegate
    public void invalidateOptionsMenu() {
        FragmentActivity activity = this.mFragment.getActivity();
        if (activity != null) {
            byte b = this.mInvalidateMenuFlags;
            if ((b & 16) != 0) {
                return;
            }
            this.mInvalidateMenuFlags = (byte) (b | 16);
            activity.getWindow().getDecorView().post(getInvalidateMenuRunnable());
        }
    }

    public final Runnable getInvalidateMenuRunnable() {
        if (this.mInvalidateMenuRunnable == null) {
            this.mInvalidateMenuRunnable = new InvalidateMenuRunnable(this);
        }
        return this.mInvalidateMenuRunnable;
    }

    public void updateOptionsMenu(int i) {
        this.mInvalidateMenuFlags = (byte) ((i & 1) | this.mInvalidateMenuFlags);
    }

    public ActionMode startActionMode(ActionMode.Callback callback) {
        if (callback instanceof SearchActionMode.Callback) {
            addContentMask((ActionBarOverlayLayout) this.mSubDecor);
        }
        return this.mSubDecor.startActionMode(callback);
    }

    @Override // miuix.appcompat.app.ActionBarDelegateImpl
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        if (getActionBar() != null) {
            return ((ActionBarImpl) getActionBar()).startActionMode(callback);
        }
        return null;
    }

    public void setExtraThemeRes(int i) {
        this.mExtraThemeRes = i;
    }

    @Override // miuix.appcompat.app.ActionBarDelegateImpl
    public Context getThemedContext() {
        if (this.mThemedContext == null) {
            this.mThemedContext = this.mActivity;
            if (this.mExtraThemeRes != 0) {
                this.mThemedContext = new ContextThemeWrapper(this.mThemedContext, this.mExtraThemeRes);
            }
        }
        return this.mThemedContext;
    }

    @Override // miuix.appcompat.app.ActionBarDelegateImpl
    public boolean onCreateImmersionMenu(MenuBuilder menuBuilder) {
        androidx.fragment.app.Fragment fragment = this.mFragment;
        if (fragment instanceof IFragment) {
            return ((IFragment) fragment).onCreateOptionsMenu(menuBuilder);
        }
        return false;
    }

    @Override // miuix.appcompat.app.ActionBarDelegateImpl
    public boolean onPrepareImmersionMenu(MenuBuilder menuBuilder) {
        androidx.fragment.app.Fragment fragment = this.mFragment;
        if (fragment instanceof IFragment) {
            fragment.onPrepareOptionsMenu(menuBuilder);
            return true;
        }
        return false;
    }

    public void setOnStatusBarChangeListener(OnStatusBarChangeListener onStatusBarChangeListener) {
        View view = this.mSubDecor;
        if (view == null || !(view instanceof ActionBarOverlayLayout)) {
            return;
        }
        ((ActionBarOverlayLayout) view).setOnStatusBarChangeListener(onStatusBarChangeListener);
    }

    public void onDestroyView() {
        this.mSubDecor = null;
        this.mSubDecorInstalled = false;
        this.mActionBar = null;
        this.mActionBarView = null;
        this.mInvalidateMenuRunnable = null;
    }

    /* loaded from: classes3.dex */
    public static class InvalidateMenuRunnable implements Runnable {
        public WeakReference<FragmentDelegate> mRefs;

        public InvalidateMenuRunnable(FragmentDelegate fragmentDelegate) {
            this.mRefs = null;
            this.mRefs = new WeakReference<>(fragmentDelegate);
        }

        @Override // java.lang.Runnable
        public void run() {
            WeakReference<FragmentDelegate> weakReference = this.mRefs;
            FragmentDelegate fragmentDelegate = weakReference == null ? null : weakReference.get();
            if (fragmentDelegate == null) {
                return;
            }
            boolean z = true;
            if ((fragmentDelegate.mInvalidateMenuFlags & 1) == 1) {
                fragmentDelegate.mMenu = null;
            }
            if (fragmentDelegate.mMenu == null) {
                fragmentDelegate.mMenu = fragmentDelegate.createMenu();
                z = fragmentDelegate.onCreatePanelMenu(0, fragmentDelegate.mMenu);
            }
            if (z) {
                z = fragmentDelegate.onPreparePanel(0, null, fragmentDelegate.mMenu);
            }
            if (z) {
                fragmentDelegate.setMenu(fragmentDelegate.mMenu);
            } else {
                fragmentDelegate.setMenu(null);
                fragmentDelegate.mMenu = null;
            }
            FragmentDelegate.access$172(fragmentDelegate, -18);
        }
    }
}
