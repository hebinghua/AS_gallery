package miuix.appcompat.app;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

/* loaded from: classes3.dex */
public class Fragment extends androidx.fragment.app.Fragment implements IFragment {
    private FragmentDelegate mDelegate;
    private boolean mHasMenu = true;
    private boolean mMenuVisible = true;

    @Override // miuix.appcompat.app.IFragment
    public final void onActionModeFinished(ActionMode actionMode) {
    }

    @Override // miuix.appcompat.app.IFragment
    public final void onActionModeStarted(ActionMode actionMode) {
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return null;
    }

    public void onVisibilityChanged(boolean z) {
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        FragmentDelegate fragmentDelegate = new FragmentDelegate(this);
        this.mDelegate = fragmentDelegate;
        fragmentDelegate.onCreate(bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        this.mDelegate.onStop();
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        this.mDelegate.onPostResume();
    }

    @Override // androidx.fragment.app.Fragment
    public final View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return this.mDelegate.onCreateView(layoutInflater, viewGroup, bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public View getView() {
        FragmentDelegate fragmentDelegate = this.mDelegate;
        if (fragmentDelegate == null) {
            return null;
        }
        return fragmentDelegate.getView();
    }

    @Override // androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mDelegate.onConfigurationChanged(configuration);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        this.mDelegate.onDestroyView();
    }

    public ActionBar getActionBar() {
        return this.mDelegate.getActionBar();
    }

    public AppCompatActivity getAppCompatActivity() {
        return this.mDelegate.getActivity();
    }

    public MenuInflater getMenuInflater() {
        return this.mDelegate.getMenuInflater();
    }

    public ActionMode startActionMode(ActionMode.Callback callback) {
        return this.mDelegate.startActionMode(callback);
    }

    public void setThemeRes(int i) {
        this.mDelegate.setExtraThemeRes(i);
    }

    public Context getThemedContext() {
        return this.mDelegate.getThemedContext();
    }

    @Override // androidx.fragment.app.Fragment
    public void setHasOptionsMenu(boolean z) {
        FragmentDelegate fragmentDelegate;
        super.setHasOptionsMenu(z);
        if (this.mHasMenu != z) {
            this.mHasMenu = z;
            if (!z || (fragmentDelegate = this.mDelegate) == null || fragmentDelegate.isImmersionMenuEnabled() || isHidden() || !isAdded()) {
                return;
            }
            this.mDelegate.invalidateOptionsMenu();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void setMenuVisibility(boolean z) {
        FragmentDelegate fragmentDelegate;
        super.setMenuVisibility(z);
        if (this.mMenuVisible != z) {
            this.mMenuVisible = z;
            if (isHidden() || !isAdded() || (fragmentDelegate = this.mDelegate) == null) {
                return;
            }
            fragmentDelegate.invalidateOptionsMenu();
        }
    }

    public boolean requestWindowFeature(int i) {
        return this.mDelegate.requestWindowFeature(i);
    }

    public void invalidateOptionsMenu() {
        FragmentDelegate fragmentDelegate = this.mDelegate;
        if (fragmentDelegate != null) {
            fragmentDelegate.updateOptionsMenu(1);
            if (isHidden() || !this.mHasMenu || this.mDelegate.isImmersionMenuEnabled() || !this.mMenuVisible || !isAdded()) {
                return;
            }
            this.mDelegate.invalidateOptionsMenu();
        }
    }

    public void updateOptionsMenuContent() {
        if (this.mDelegate == null || isHidden() || !this.mHasMenu || this.mDelegate.isImmersionMenuEnabled() || !this.mMenuVisible || !isAdded()) {
            return;
        }
        this.mDelegate.invalidateOptionsMenu();
    }

    @Override // miuix.appcompat.app.IFragment
    public boolean onCreatePanelMenu(int i, Menu menu) {
        if (i != 0 || !this.mHasMenu || this.mDelegate.isImmersionMenuEnabled() || !this.mMenuVisible || isHidden() || !isAdded()) {
            return false;
        }
        return onCreateOptionsMenu(menu);
    }

    @Override // miuix.appcompat.app.IFragment
    public void onPreparePanel(int i, View view, Menu menu) {
        if (i != 0 || !this.mHasMenu || this.mDelegate.isImmersionMenuEnabled() || !this.mMenuVisible || isHidden() || !isAdded()) {
            return;
        }
        onPrepareOptionsMenu(menu);
    }

    @Override // androidx.fragment.app.Fragment
    public final void onHiddenChanged(boolean z) {
        FragmentDelegate fragmentDelegate;
        super.onHiddenChanged(z);
        if (!z && (fragmentDelegate = this.mDelegate) != null) {
            fragmentDelegate.invalidateOptionsMenu();
        }
        onVisibilityChanged(!z);
    }

    public void setImmersionMenuEnabled(boolean z) {
        this.mDelegate.setImmersionMenuEnabled(z);
    }

    public void showImmersionMenu() {
        this.mDelegate.showImmersionMenu();
    }

    public void showImmersionMenu(View view, ViewGroup viewGroup) {
        this.mDelegate.showImmersionMenu(view, viewGroup);
    }

    public void dismissImmersionMenu(boolean z) {
        this.mDelegate.dismissImmersionMenu(z);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        this.mDelegate.dismissImmersionMenu(false);
    }

    public void setOnStatusBarChangeListener(OnStatusBarChangeListener onStatusBarChangeListener) {
        this.mDelegate.setOnStatusBarChangeListener(onStatusBarChangeListener);
    }

    public void setExtraHorizontalPaddingLevel(int i) {
        this.mDelegate.setExtraHorizontalPaddingLevel(i);
    }

    public int getExtraHorizontalPaddingLevel() {
        return this.mDelegate.getExtraHorizontalPaddingLevel();
    }

    public void setExtraHorizontalPaddingEnable(boolean z) {
        this.mDelegate.setExtraHorizontalPaddingEnable(z);
    }

    public boolean isExtraHorizontalPaddingEnable() {
        return this.mDelegate.isExtraHorizontalPaddingEnable();
    }
}
