package com.miui.gallery.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.miui.gallery.R;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.app.screenChange.IScreenChange;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.ModernAlbumDetailFragment;
import com.miui.gallery.ui.recentdiscovery.RecentDiscoveryFragment;
import com.miui.gallery.widget.tsd.DrawerState;
import com.miui.gallery.widget.tsd.INestedTwoStageDrawer;
import com.miui.gallery.widget.tsd.InestedScrollerStateListener;
import java.util.List;
import miuix.miuixbasewidget.widget.FilterSortView;

/* loaded from: classes.dex */
public class AllPhotosActivity extends BaseActivity {
    public ModernAlbumDetailFragment mAllPhotosFragment;
    public FilterSortView.TabView mAllPhotosSwitch;
    public INestedTwoStageDrawer mDrawer;
    public FilterSortView mFilterSortView;
    public BaseActivity.FragmentCreator mFragmentCreator;
    public RecentDiscoveryFragment mRecentDiscoveryFragment;
    public FilterSortView.TabView mRecentSwitch;
    public String mCurTopFragment = "AllPhotosFragment";
    public View.OnClickListener mOnClickListener = new View.OnClickListener() { // from class: com.miui.gallery.activity.AllPhotosActivity.2
        {
            AllPhotosActivity.this = this;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getId() == R.id.all_photos_switch) {
                AllPhotosActivity.this.mFilterSortView.setFilteredTab(AllPhotosActivity.this.mAllPhotosSwitch);
                SamplingStatHelper.recordCountEvent("album_detail", "all_photos_album_switch_clicked");
                TrackController.trackClick("403.44.1.1.11213", "403.44.0.1.11210", "all");
                if (AllPhotosActivity.this.mCurTopFragment == "AllPhotosFragment") {
                    return;
                }
                AllPhotosActivity.this.startAllPhotosFragment();
                AllPhotosActivity.this.setImmersionMenuEnabled(true);
            } else if (view.getId() != R.id.recent_photos_switch) {
            } else {
                AllPhotosActivity.this.mFilterSortView.setFilteredTab(AllPhotosActivity.this.mRecentSwitch);
                SamplingStatHelper.recordCountEvent("album_detail", "recent_album_switch_clicked");
                TrackController.trackClick("403.44.1.1.11213", "403.44.0.1.11210", "recent");
                if (AllPhotosActivity.this.mCurTopFragment == "RecentDiscoveryFragment") {
                    return;
                }
                AllPhotosActivity.this.startRecentDiscoveryFragment();
                AllPhotosActivity.this.setImmersionMenuEnabled(false);
            }
        }
    };

    public static /* synthetic */ void $r8$lambda$N3VSIcdvxAxc_F5393In1UCUIA0(AllPhotosActivity allPhotosActivity, Configuration configuration) {
        allPhotosActivity.lambda$onCreate$1(configuration);
    }

    @Override // com.miui.gallery.activity.BaseActivity
    public int getFragmentContainerId() {
        return R.id.fragment_container;
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (this.mFragmentLifecycleCallback != null) {
            getSupportFragmentManager().unregisterFragmentLifecycleCallbacks(this.mFragmentLifecycleCallback);
        }
        setContentView(R.layout.all_photos_activity);
        this.mDrawer = (INestedTwoStageDrawer) findViewById(R.id.drawer);
        FilterSortView filterSortView = (FilterSortView) findViewById(R.id.switch_container);
        this.mFilterSortView = filterSortView;
        filterSortView.setTabIncatorVisibility(8);
        this.mAllPhotosSwitch = (FilterSortView.TabView) findViewById(R.id.all_photos_switch);
        this.mRecentSwitch = (FilterSortView.TabView) findViewById(R.id.recent_photos_switch);
        Intent intent = getIntent();
        this.mAllPhotosSwitch.setOnClickListener(this.mOnClickListener);
        this.mRecentSwitch.setOnClickListener(this.mOnClickListener);
        this.mFragmentCreator = AllPhotosActivity$$ExternalSyntheticLambda0.INSTANCE;
        if ((bundle != null && bundle.getString("current_top_fragment").equals("RecentDiscoveryFragment") && (intent == null || intent.getData() == null)) || (intent != null && intent.getData() != null && intent.getData().getPath().equals("/recent-album/"))) {
            this.mFilterSortView.setFilteredTab(this.mRecentSwitch);
            startRecentDiscoveryFragment();
        } else {
            this.mFilterSortView.setFilteredTab(this.mAllPhotosSwitch);
            startAllPhotosFragment();
        }
        AutoTracking.trackNav("403.44.0.1.11534");
        this.mDrawer.addScrollerStateListener(new InestedScrollerStateListener() { // from class: com.miui.gallery.activity.AllPhotosActivity.1
            @Override // com.miui.gallery.widget.tsd.InestedScrollerStateListener
            public void onScrollerUpdate(DrawerState drawerState, int i, int i2) {
            }

            {
                AllPhotosActivity.this = this;
            }

            @Override // com.miui.gallery.widget.tsd.InestedScrollerStateListener
            public void onScrollerStateChanged(DrawerState drawerState, int i) {
                if (drawerState != DrawerState.CLOSE) {
                    if (AllPhotosActivity.this.mAllPhotosFragment != null) {
                        AllPhotosActivity.this.mAllPhotosFragment.hideScrollerBar();
                    }
                    if (AllPhotosActivity.this.mRecentDiscoveryFragment == null) {
                        return;
                    }
                    AllPhotosActivity.this.mRecentDiscoveryFragment.hideScrollerBar();
                }
            }
        });
        addScreenChangeListener(new IScreenChange.OnScreenLayoutSizeChangeListener() { // from class: com.miui.gallery.activity.AllPhotosActivity$$ExternalSyntheticLambda1
            @Override // com.miui.gallery.app.screenChange.IScreenChange.OnScreenLayoutSizeChangeListener
            public final void onScreenLayoutSizeChange(Configuration configuration) {
                AllPhotosActivity.$r8$lambda$N3VSIcdvxAxc_F5393In1UCUIA0(AllPhotosActivity.this, configuration);
            }
        });
    }

    public static /* synthetic */ Fragment lambda$onCreate$0(String str) {
        if ("AllPhotosFragment".equals(str)) {
            return new ModernAlbumDetailFragment();
        }
        return new RecentDiscoveryFragment();
    }

    public /* synthetic */ void lambda$onCreate$1(Configuration configuration) {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mFilterSortView.getLayoutParams();
        int dimensionPixelOffset = getResources().getDimensionPixelOffset(R.dimen.filter_sort_view_margin_start_end);
        layoutParams.setMarginStart(dimensionPixelOffset);
        layoutParams.setMarginEnd(dimensionPixelOffset);
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        if ("AllPhotosFragment".equals(this.mCurTopFragment)) {
            setImmersionMenuEnabled(true);
        } else {
            setImmersionMenuEnabled(false);
        }
    }

    @Override // com.miui.gallery.app.activity.GalleryActivity, miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString("current_top_fragment", this.mCurTopFragment);
        super.onSaveInstanceState(bundle);
    }

    public final void startAllPhotosFragment() {
        Intent intent = new Intent();
        intent.putExtra("album_id", 2147483644L);
        intent.putExtra("album_server_id", String.valueOf(-2147483644L));
        setIntent(intent);
        this.mAllPhotosFragment = (ModernAlbumDetailFragment) startFragment(this.mFragmentCreator, "AllPhotosFragment", false, true);
        RecentDiscoveryFragment recentDiscoveryFragment = (RecentDiscoveryFragment) getSupportFragmentManager().findFragmentByTag("RecentDiscoveryFragment");
        this.mRecentDiscoveryFragment = recentDiscoveryFragment;
        if (recentDiscoveryFragment != null) {
            FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
            beginTransaction.hide(this.mRecentDiscoveryFragment);
            beginTransaction.show(this.mAllPhotosFragment);
            beginTransaction.commitAllowingStateLoss();
        }
        this.mCurTopFragment = "AllPhotosFragment";
        SamplingStatHelper.recordCountEvent("album_detail", "all_photos_album_switch_started");
    }

    public final void startRecentDiscoveryFragment() {
        AutoTracking.trackView("403.44.0.1.11210", AutoTracking.getRef(), "recent");
        this.mRecentDiscoveryFragment = (RecentDiscoveryFragment) startFragment(this.mFragmentCreator, "RecentDiscoveryFragment", false, true);
        ModernAlbumDetailFragment modernAlbumDetailFragment = (ModernAlbumDetailFragment) getSupportFragmentManager().findFragmentByTag("AllPhotosFragment");
        this.mAllPhotosFragment = modernAlbumDetailFragment;
        if (modernAlbumDetailFragment != null) {
            FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
            beginTransaction.hide(this.mAllPhotosFragment);
            beginTransaction.show(this.mRecentDiscoveryFragment);
            beginTransaction.commitAllowingStateLoss();
        }
        this.mCurTopFragment = "RecentDiscoveryFragment";
        SamplingStatHelper.recordCountEvent("album_detail", "recent_album_started");
    }

    @Override // com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, android.app.Activity, android.view.Window.Callback
    public void onActionModeStarted(ActionMode actionMode) {
        super.onActionModeStarted(actionMode);
        this.mAllPhotosSwitch.setClickable(false);
        this.mRecentSwitch.setClickable(false);
    }

    @Override // com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, android.app.Activity, android.view.Window.Callback
    public void onActionModeFinished(ActionMode actionMode) {
        super.onActionModeFinished(actionMode);
        this.mAllPhotosSwitch.setClickable(true);
        this.mRecentSwitch.setClickable(true);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        ModernAlbumDetailFragment modernAlbumDetailFragment = this.mAllPhotosFragment;
        if (modernAlbumDetailFragment == null || this.mCurTopFragment != "AllPhotosFragment") {
            return false;
        }
        modernAlbumDetailFragment.onCreateOptionsMenu(menu, getMenuInflater());
        return true;
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        ModernAlbumDetailFragment modernAlbumDetailFragment = this.mAllPhotosFragment;
        if (modernAlbumDetailFragment != null && this.mCurTopFragment == "AllPhotosFragment") {
            modernAlbumDetailFragment.onPrepareOptionsMenu(menu);
            return true;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        ModernAlbumDetailFragment modernAlbumDetailFragment = this.mAllPhotosFragment;
        if (modernAlbumDetailFragment != null && this.mCurTopFragment == "AllPhotosFragment") {
            return modernAlbumDetailFragment.onOptionsItemSelected(menuItem);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int i) {
        if (this.mCurTopFragment == "AllPhotosFragment") {
            this.mAllPhotosFragment.onProvideKeyboardShortcuts(list, menu, i);
        } else {
            this.mRecentDiscoveryFragment.onProvideKeyboardShortcuts(list, menu, i);
        }
        super.onProvideKeyboardShortcuts(list, menu, i);
    }

    @Override // android.app.Activity
    public boolean onKeyShortcut(int i, KeyEvent keyEvent) {
        if (this.mCurTopFragment != "AllPhotosFragment" || !this.mAllPhotosFragment.onKeyShortcut(i, keyEvent)) {
            if (this.mCurTopFragment == "RecentDiscoveryFragment" && this.mRecentDiscoveryFragment.onKeyShortcut(i, keyEvent)) {
                return true;
            }
            return super.onKeyShortcut(i, keyEvent);
        }
        return true;
    }

    @Override // com.miui.gallery.activity.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        TimeMonitor.cancelTimeMonitor("403.44.0.1.13763");
        super.onDestroy();
    }
}
