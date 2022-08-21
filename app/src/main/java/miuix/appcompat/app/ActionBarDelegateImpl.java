package miuix.appcompat.app;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import androidx.core.os.TraceCompat;
import miuix.appcompat.R$id;
import miuix.appcompat.R$integer;
import miuix.appcompat.internal.app.widget.ActionBarContainer;
import miuix.appcompat.internal.app.widget.ActionBarContextView;
import miuix.appcompat.internal.app.widget.ActionBarImpl;
import miuix.appcompat.internal.app.widget.ActionBarOverlayLayout;
import miuix.appcompat.internal.app.widget.ActionBarView;
import miuix.appcompat.internal.view.menu.ImmersionMenuPopupWindow;
import miuix.appcompat.internal.view.menu.ImmersionMenuPopupWindowImpl;
import miuix.appcompat.internal.view.menu.MenuBuilder;
import miuix.appcompat.internal.view.menu.MenuPresenter;
import miuix.core.util.variable.WindowWrapper;

/* loaded from: classes3.dex */
public abstract class ActionBarDelegateImpl implements ActionBarDelegate, MenuPresenter.Callback, MenuBuilder.Callback {
    public ActionBar mActionBar;
    public ActionBarView mActionBarView;
    public ActionMode mActionMode;
    public final AppCompatActivity mActivity;
    public boolean mFeatureIndeterminateProgress;
    public boolean mFeatureProgress;
    public boolean mHasActionBar;
    public int mImmersionLayoutResourceId;
    public MenuBuilder mImmersionMenu;
    public boolean mImmersionMenuEnabled;
    public MenuBuilder mMenu;
    public MenuInflater mMenuInflater;
    public ImmersionMenuPopupWindow mMenuPopupWindow;
    public boolean mOverlayActionBar;
    public boolean mSubDecorInstalled;
    public int mTranslucentStatus = 0;
    public boolean mHasAddSplitActionBar = false;

    public abstract Context getThemedContext();

    public abstract View getView();

    public void onCreate(Bundle bundle) {
    }

    public abstract boolean onCreateImmersionMenu(MenuBuilder menuBuilder);

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter.Callback
    public boolean onOpenSubMenu(MenuBuilder menuBuilder) {
        return false;
    }

    public abstract boolean onPrepareImmersionMenu(MenuBuilder menuBuilder);

    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        return null;
    }

    public ActionBarDelegateImpl(AppCompatActivity appCompatActivity) {
        this.mActivity = appCompatActivity;
    }

    public final ActionBar getActionBar() {
        TraceCompat.beginSection("createActionBar");
        try {
            if (!this.mHasActionBar && !this.mOverlayActionBar) {
                this.mActionBar = null;
                return this.mActionBar;
            }
            if (this.mActionBar == null) {
                this.mActionBar = createActionBar();
            }
            return this.mActionBar;
        } finally {
            TraceCompat.endSection();
        }
    }

    public MenuInflater getMenuInflater() {
        if (this.mMenuInflater == null) {
            ActionBar actionBar = getActionBar();
            if (actionBar != null) {
                this.mMenuInflater = new MenuInflater(actionBar.getThemedContext());
            } else {
                this.mMenuInflater = new MenuInflater(this.mActivity);
            }
        }
        return this.mMenuInflater;
    }

    public final String getUiOptionsFromMetadata() {
        try {
            Bundle bundle = this.mActivity.getPackageManager().getActivityInfo(this.mActivity.getComponentName(), 128).metaData;
            if (bundle == null) {
                return null;
            }
            return bundle.getString("android.support.UI_OPTIONS");
        } catch (PackageManager.NameNotFoundException unused) {
            Log.e("ActionBarDelegate", "getUiOptionsFromMetadata: Activity '" + this.mActivity.getClass().getSimpleName() + "' not in manifest");
            return null;
        }
    }

    public final Context getActionBarThemedContext() {
        AppCompatActivity appCompatActivity = this.mActivity;
        ActionBar actionBar = getActionBar();
        return actionBar != null ? actionBar.getThemedContext() : appCompatActivity;
    }

    public AppCompatActivity getActivity() {
        return this.mActivity;
    }

    public void onConfigurationChanged(Configuration configuration) {
        ActionBarImpl actionBarImpl;
        if (!this.mHasActionBar || !this.mSubDecorInstalled || (actionBarImpl = (ActionBarImpl) getActionBar()) == null) {
            return;
        }
        actionBarImpl.onConfigurationChanged(configuration);
    }

    public void onStop() {
        ActionBarImpl actionBarImpl;
        dismissImmersionMenu(false);
        if (!this.mHasActionBar || !this.mSubDecorInstalled || (actionBarImpl = (ActionBarImpl) getActionBar()) == null) {
            return;
        }
        actionBarImpl.setShowHideAnimationEnabled(false);
    }

    public void onPostResume() {
        ActionBarImpl actionBarImpl;
        if (!this.mHasActionBar || !this.mSubDecorInstalled || (actionBarImpl = (ActionBarImpl) getActionBar()) == null) {
            return;
        }
        actionBarImpl.setShowHideAnimationEnabled(true);
    }

    public boolean requestWindowFeature(int i) {
        if (i == 2) {
            this.mFeatureProgress = true;
            return true;
        } else if (i == 5) {
            this.mFeatureIndeterminateProgress = true;
            return true;
        } else if (i == 8) {
            this.mHasActionBar = true;
            return true;
        } else if (i == 9) {
            this.mOverlayActionBar = true;
            return true;
        } else {
            return this.mActivity.requestWindowFeature(i);
        }
    }

    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int i) {
        if (i == 0) {
            return onWindowStartingActionMode(callback);
        }
        return null;
    }

    public void reopenMenu(MenuBuilder menuBuilder, boolean z) {
        ActionBarView actionBarView = this.mActionBarView;
        if (actionBarView != null && actionBarView.isOverflowReserved()) {
            if (!this.mActionBarView.isOverflowMenuShowing() || !z) {
                if (this.mActionBarView.getVisibility() != 0) {
                    return;
                }
                this.mActionBarView.showOverflowMenu();
                return;
            }
            this.mActionBarView.hideOverflowMenu();
            return;
        }
        menuBuilder.close();
    }

    public void setMenu(MenuBuilder menuBuilder) {
        if (menuBuilder != null && menuBuilder.size() > 0 && !this.mHasAddSplitActionBar) {
            boolean equals = "splitActionBarWhenNarrow".equals(getUiOptionsFromMetadata());
            View view = getView();
            if (view instanceof ActionBarOverlayLayout) {
                addSplitActionBar(true, equals, (ActionBarOverlayLayout) view);
            }
        }
        if (menuBuilder == this.mMenu) {
            return;
        }
        this.mMenu = menuBuilder;
        ActionBarView actionBarView = this.mActionBarView;
        if (actionBarView == null) {
            return;
        }
        actionBarView.setMenu(menuBuilder, this);
    }

    public MenuBuilder createMenu() {
        MenuBuilder menuBuilder = new MenuBuilder(getActionBarThemedContext());
        menuBuilder.setCallback(this);
        return menuBuilder;
    }

    @Override // miuix.appcompat.internal.view.menu.MenuPresenter.Callback
    public void onCloseMenu(MenuBuilder menuBuilder, boolean z) {
        this.mActivity.closeOptionsMenu();
    }

    @Override // miuix.appcompat.internal.view.menu.MenuBuilder.Callback
    public void onMenuModeChange(MenuBuilder menuBuilder) {
        reopenMenu(menuBuilder, true);
    }

    public void setTranslucentStatus(int i) {
        int integer = this.mActivity.getResources().getInteger(R$integer.window_translucent_status);
        if (integer >= 0 && integer <= 2) {
            i = integer;
        }
        if (this.mTranslucentStatus == i || !WindowWrapper.setTranslucentStatus(this.mActivity.getWindow(), i)) {
            return;
        }
        this.mTranslucentStatus = i;
    }

    public int getTranslucentStatus() {
        return this.mTranslucentStatus;
    }

    public void setImmersionMenuEnabled(boolean z) {
        this.mImmersionMenuEnabled = z;
        if (!this.mSubDecorInstalled || !this.mHasActionBar) {
            return;
        }
        if (z) {
            if (!this.mActionBarView.showImmersionMore()) {
                this.mActionBarView.initImmersionMore(this.mImmersionLayoutResourceId, this);
            }
        } else {
            this.mActionBarView.hideImmersionMore();
        }
        invalidateOptionsMenu();
    }

    public boolean isImmersionMenuEnabled() {
        return this.mImmersionMenuEnabled;
    }

    public void showImmersionMenu() {
        View findViewById;
        ActionBarView actionBarView = this.mActionBarView;
        if (actionBarView != null && (findViewById = actionBarView.findViewById(R$id.more)) != null) {
            showImmersionMenu(findViewById, this.mActionBarView);
            return;
        }
        throw new IllegalStateException("Can't find anchor view in actionbar. Do you use default actionbar and immersion menu is enabled?");
    }

    public void showImmersionMenu(View view, ViewGroup viewGroup) {
        if (!this.mImmersionMenuEnabled) {
            Log.w("ActionBarDelegate", "Try to show immersion menu when immersion menu disabled");
        } else if (view == null) {
            throw new IllegalArgumentException("You must specify a valid anchor view");
        } else {
            if (this.mImmersionMenu == null) {
                MenuBuilder createMenu = createMenu();
                this.mImmersionMenu = createMenu;
                onCreateImmersionMenu(createMenu);
            }
            if (!onPrepareImmersionMenu(this.mImmersionMenu) || !this.mImmersionMenu.hasVisibleItems()) {
                return;
            }
            ImmersionMenuPopupWindow immersionMenuPopupWindow = this.mMenuPopupWindow;
            if (immersionMenuPopupWindow == null) {
                this.mMenuPopupWindow = new ImmersionMenuPopupWindowImpl(this, this.mImmersionMenu);
            } else {
                immersionMenuPopupWindow.update(this.mImmersionMenu);
            }
            if (this.mMenuPopupWindow.isShowing()) {
                return;
            }
            this.mMenuPopupWindow.show(view, viewGroup);
        }
    }

    public void dismissImmersionMenu(boolean z) {
        ImmersionMenuPopupWindow immersionMenuPopupWindow = this.mMenuPopupWindow;
        if (immersionMenuPopupWindow != null) {
            immersionMenuPopupWindow.dismiss(z);
        }
    }

    public void addSplitActionBar(boolean z, boolean z2, ActionBarOverlayLayout actionBarOverlayLayout) {
        ActionBarContainer actionBarContainer;
        ActionBarContextView actionBarContextView;
        if (this.mHasAddSplitActionBar) {
            return;
        }
        this.mHasAddSplitActionBar = true;
        ViewStub viewStub = (ViewStub) actionBarOverlayLayout.findViewById(R$id.split_action_bar_vs);
        if (viewStub != null) {
            actionBarContainer = (ActionBarContainer) viewStub.inflate();
        } else {
            actionBarContainer = (ActionBarContainer) actionBarOverlayLayout.findViewById(R$id.split_action_bar);
        }
        if (actionBarContainer != null) {
            this.mActionBarView.setSplitView(actionBarContainer);
            this.mActionBarView.setSplitActionBar(z);
            this.mActionBarView.setSplitWhenNarrow(z2);
            actionBarOverlayLayout.setSplitActionBarView(actionBarContainer);
            addContentMask(actionBarOverlayLayout);
        }
        ActionBarContainer actionBarContainer2 = (ActionBarContainer) actionBarOverlayLayout.findViewById(R$id.action_bar_container);
        ViewStub viewStub2 = (ViewStub) actionBarOverlayLayout.findViewById(R$id.action_context_bar_vs);
        if (viewStub2 != null) {
            actionBarContextView = (ActionBarContextView) viewStub2.inflate();
        } else {
            actionBarContextView = (ActionBarContextView) actionBarOverlayLayout.findViewById(R$id.action_context_bar);
        }
        if (actionBarContextView == null) {
            return;
        }
        actionBarContainer2.setActionBarContextView(actionBarContextView);
        actionBarOverlayLayout.setActionBarContextView(actionBarContextView);
        if (actionBarContainer == null) {
            return;
        }
        actionBarContextView.setSplitView(actionBarContainer);
        actionBarContextView.setSplitActionBar(z);
        actionBarContextView.setSplitWhenNarrow(z2);
    }

    public void addContentMask(ActionBarOverlayLayout actionBarOverlayLayout) {
        View findViewById;
        if (actionBarOverlayLayout != null) {
            ViewStub viewStub = (ViewStub) actionBarOverlayLayout.findViewById(R$id.content_mask_vs);
            if (viewStub != null) {
                findViewById = viewStub.inflate();
            } else {
                findViewById = actionBarOverlayLayout.findViewById(R$id.content_mask);
            }
            actionBarOverlayLayout.setContentMask(findViewById);
        }
    }
}
