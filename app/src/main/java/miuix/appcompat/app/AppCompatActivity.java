package miuix.appcompat.app;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentActivity;
import miuix.appcompat.app.floatingactivity.IActivitySwitcherAnimation;
import miuix.appcompat.app.floatingactivity.OnFloatingActivityCallback;
import miuix.appcompat.app.floatingactivity.OnFloatingCallback;
import miuix.appcompat.app.floatingactivity.OnFloatingModeCallback;

@SuppressLint({"MissingSuperCall"})
/* loaded from: classes3.dex */
public class AppCompatActivity extends FragmentActivity implements IActivity, IActivitySwitcherAnimation {
    private AppDelegate mAppDelegate = new AppDelegate(this, new Callback(), new FloatingModeCallback());

    public void onFloatingWindowModeChanged(boolean z) {
    }

    public boolean onFloatingWindowModeChanging(boolean z) {
        return true;
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        this.mAppDelegate.onCreate(bundle);
    }

    public ActionBar getAppCompatActionBar() {
        return this.mAppDelegate.getActionBar();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void setContentView(int i) {
        this.mAppDelegate.setContentView(i);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void setContentView(View view) {
        this.mAppDelegate.setContentView(view);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void setContentView(View view, ViewGroup.LayoutParams layoutParams) {
        this.mAppDelegate.setContentView(view, layoutParams);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void addContentView(View view, ViewGroup.LayoutParams layoutParams) {
        this.mAppDelegate.addContentView(view, layoutParams);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPostResume() {
        this.mAppDelegate.onPostResume();
    }

    @Override // android.app.Activity
    public void onTitleChanged(CharSequence charSequence, int i) {
        this.mAppDelegate.onTitleChanged(charSequence);
    }

    @Override // android.app.Activity
    public void invalidateOptionsMenu() {
        this.mAppDelegate.invalidateOptionsMenu();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        this.mAppDelegate.onBackPressed();
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public View onCreatePanelView(int i) {
        return this.mAppDelegate.onCreatePanelView(i);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity, android.view.Window.Callback
    public boolean onMenuItemSelected(int i, MenuItem menuItem) {
        return this.mAppDelegate.onMenuItemSelected(i, menuItem);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onActionModeStarted(ActionMode actionMode) {
        this.mAppDelegate.onActionModeStarted(actionMode);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onActionModeFinished(ActionMode actionMode) {
        this.mAppDelegate.onActionModeFinished(actionMode);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int i) {
        return this.mAppDelegate.onWindowStartingActionMode(callback, i);
    }

    @Override // android.app.Activity
    public MenuInflater getMenuInflater() {
        return this.mAppDelegate.getMenuInflater();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        this.mAppDelegate.onConfigurationChanged(configuration);
    }

    @Override // androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        this.mAppDelegate.onSaveInstanceState(bundle);
    }

    @Override // android.app.Activity
    public void onRestoreInstanceState(Bundle bundle) {
        this.mAppDelegate.onRestoreInstanceState(bundle);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        this.mAppDelegate.onStop();
    }

    @Override // android.app.Activity
    public void finish() {
        if (!this.mAppDelegate.shouldDelegateActivityFinish()) {
            realFinish();
        }
    }

    public void exitFloatingActivityAll() {
        this.mAppDelegate.exitFloatingActivityAll();
    }

    public void realFinish() {
        super.finish();
    }

    public boolean requestExtraWindowFeature(int i) {
        return this.mAppDelegate.requestWindowFeature(i);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity, android.view.Window.Callback
    public boolean onCreatePanelMenu(int i, Menu menu) {
        return this.mAppDelegate.onCreatePanelMenu(i, menu);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity, android.view.Window.Callback
    public boolean onPreparePanel(int i, View view, Menu menu) {
        return this.mAppDelegate.onPreparePanel(i, view, menu);
    }

    @Override // android.app.Activity
    public ActionMode startActionMode(ActionMode.Callback callback) {
        return this.mAppDelegate.startActionMode(callback);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
        return this.mAppDelegate.onWindowStartingActionMode(callback);
    }

    public void setTranslucentStatus(int i) {
        this.mAppDelegate.setTranslucentStatus(i);
    }

    public int getTranslucentStatus() {
        return this.mAppDelegate.getTranslucentStatus();
    }

    public void setFloatingWindowMode(boolean z) {
        this.mAppDelegate.setFloatingWindowMode(z);
    }

    public boolean isFloatingWindowTheme() {
        return this.mAppDelegate.isFloatingTheme();
    }

    @Override // miuix.appcompat.app.IActivity
    public boolean isInFloatingWindowMode() {
        return this.mAppDelegate.isInFloatingWindowMode();
    }

    public View getFloatingBrightPanel() {
        return this.mAppDelegate.getFloatingBrightPanel();
    }

    public void setOnStatusBarChangeListener(OnStatusBarChangeListener onStatusBarChangeListener) {
        this.mAppDelegate.setOnStatusBarChangeListener(onStatusBarChangeListener);
    }

    public void setOnFloatingWindowCallback(OnFloatingActivityCallback onFloatingActivityCallback) {
        this.mAppDelegate.setOnFloatingWindowCallback(onFloatingActivityCallback);
    }

    public void setEnableSwipToDismiss(boolean z) {
        this.mAppDelegate.setEnableSwipToDismiss(z);
    }

    public void setOnFloatingCallback(OnFloatingCallback onFloatingCallback) {
        this.mAppDelegate.setOnFloatingCallback(onFloatingCallback);
    }

    public void hideFloatingDimBackground() {
        this.mAppDelegate.hideFloatingDimBackground();
    }

    public void hideFloatingBrightPanel() {
        this.mAppDelegate.hideFloatingBrightPanel();
    }

    public void showFloatingBrightPanel() {
        this.mAppDelegate.showFloatingBrightPanel();
    }

    public void setImmersionMenuEnabled(boolean z) {
        this.mAppDelegate.setImmersionMenuEnabled(z);
    }

    public void showImmersionMenu() {
        this.mAppDelegate.showImmersionMenu();
    }

    public void showImmersionMenu(View view, ViewGroup viewGroup) {
        this.mAppDelegate.showImmersionMenu(view, viewGroup);
    }

    public void dismissImmersionMenu(boolean z) {
        this.mAppDelegate.dismissImmersionMenu(z);
    }

    @Override // miuix.appcompat.app.floatingactivity.IActivitySwitcherAnimation
    public void executeOpenEnterAnimation() {
        this.mAppDelegate.executeOpenEnterAnimation();
    }

    @Override // miuix.appcompat.app.floatingactivity.IActivitySwitcherAnimation
    public void executeOpenExitAnimation() {
        this.mAppDelegate.executeOpenExitAnimation();
    }

    @Override // miuix.appcompat.app.floatingactivity.IActivitySwitcherAnimation
    public void executeCloseEnterAnimation() {
        this.mAppDelegate.executeCloseEnterAnimation();
    }

    @Override // miuix.appcompat.app.floatingactivity.IActivitySwitcherAnimation
    public void executeCloseExitAnimation() {
        this.mAppDelegate.executeCloseExitAnimation();
    }

    public void setExtraHorizontalPaddingLevel(int i) {
        this.mAppDelegate.setExtraHorizontalPaddingLevel(i);
    }

    public int getExtraHorizontalPaddingLevel() {
        return this.mAppDelegate.getExtraHorizontalPaddingLevel();
    }

    public void setExtraHorizontalPaddingEnable(boolean z) {
        this.mAppDelegate.setExtraHorizontalPaddingEnable(z);
    }

    public boolean isExtraHorizontalPaddingEnable() {
        return this.mAppDelegate.isExtraHorizontalPaddingEnable();
    }

    /* loaded from: classes3.dex */
    public class Callback implements ActivityCallback {
        public Callback() {
        }

        @Override // miuix.appcompat.app.ActivityCallback
        public void onCreate(Bundle bundle) {
            AppCompatActivity.super.onCreate(bundle);
        }

        @Override // miuix.appcompat.app.ActivityCallback
        public void onPostResume() {
            AppCompatActivity.super.onPostResume();
        }

        @Override // miuix.appcompat.app.ActivityCallback
        public void onStop() {
            AppCompatActivity.super.onStop();
        }

        @Override // miuix.appcompat.app.ActivityCallback
        public boolean onMenuItemSelected(int i, MenuItem menuItem) {
            return AppCompatActivity.super.onMenuItemSelected(i, menuItem);
        }

        @Override // miuix.appcompat.app.ActivityCallback
        public View onCreatePanelView(int i) {
            return AppCompatActivity.super.onCreatePanelView(i);
        }

        @Override // miuix.appcompat.app.ActivityCallback
        public boolean onCreatePanelMenu(int i, Menu menu) {
            return AppCompatActivity.super.onCreatePanelMenu(i, menu);
        }

        @Override // miuix.appcompat.app.ActivityCallback
        public boolean onPreparePanel(int i, View view, Menu menu) {
            return AppCompatActivity.super.onPreparePanel(i, view, menu);
        }

        @Override // miuix.appcompat.app.ActivityCallback
        public void onBackPressed() {
            AppCompatActivity.super.onBackPressed();
        }

        @Override // miuix.appcompat.app.ActivityCallback
        public void onConfigurationChanged(Configuration configuration) {
            AppCompatActivity.super.onConfigurationChanged(configuration);
        }

        @Override // miuix.appcompat.app.ActivityCallback
        public void onSaveInstanceState(Bundle bundle) {
            AppCompatActivity.super.onSaveInstanceState(bundle);
        }

        @Override // miuix.appcompat.app.ActivityCallback
        public void onRestoreInstanceState(Bundle bundle) {
            AppCompatActivity.super.onRestoreInstanceState(bundle);
        }
    }

    /* loaded from: classes3.dex */
    public class FloatingModeCallback implements OnFloatingModeCallback {
        public FloatingModeCallback() {
        }

        @Override // miuix.appcompat.app.floatingactivity.OnFloatingModeCallback
        public boolean onFloatingWindowModeChanging(boolean z) {
            return AppCompatActivity.this.onFloatingWindowModeChanging(z);
        }

        @Override // miuix.appcompat.app.floatingactivity.OnFloatingModeCallback
        public void onFloatingWindowModeChanged(boolean z) {
            AppCompatActivity.this.onFloatingWindowModeChanged(z);
        }
    }
}
