package com.miui.gallery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.miui.gallery.R;
import com.miui.gallery.adapter.AlbumDetailGroupingAdapter;
import com.miui.gallery.adapter.SortBy;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.ui.ModernAlbumDetailFragment;
import com.miui.gallery.viewmodel.AlbumDetailViewModel;
import com.miui.gallery.widget.SwitchView;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes.dex */
public class AlbumDetailGroupingActivity extends BaseActivity {
    public boolean mIsScreenshotRecorderAlbum;
    public boolean mIsShowFirstFragment;
    public int mPosition = 0;
    public SwitchView mSwitchView;
    public AlbumDetailViewModel mViewModel;
    public ViewPager2 mViewPager;

    public static /* synthetic */ void $r8$lambda$sqSklNSFXTL7AQysA5QKrVB_oC4(AlbumDetailGroupingActivity albumDetailGroupingActivity, int i) {
        albumDetailGroupingActivity.lambda$initView$0(i);
    }

    public static /* synthetic */ void lambda$initView$1(int i) {
    }

    @Override // com.miui.gallery.activity.BaseActivity
    public boolean hasCustomContentView() {
        return true;
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.grouping_album_layout);
        AlbumDetailViewModel albumDetailViewModel = (AlbumDetailViewModel) new ViewModelProvider(this).get(AlbumDetailViewModel.class);
        this.mViewModel = albumDetailViewModel;
        albumDetailViewModel.setIsAlbumGroup(true);
        boolean booleanExtra = getIntent().getBooleanExtra("screenshot_recorder_album", false);
        this.mIsScreenshotRecorderAlbum = booleanExtra;
        if (booleanExtra) {
            boolean isShowScreenshot = GalleryPreferences.Album.isShowScreenshot();
            this.mIsShowFirstFragment = isShowScreenshot;
            this.mPosition = !isShowScreenshot ? 1 : 0;
        }
        trackView();
        initView();
    }

    public final void initView() {
        Intent intent = getIntent();
        this.mSwitchView = (SwitchView) findViewById(R.id.switch_container);
        this.mViewPager = (ViewPager2) findViewById(R.id.fragment_container);
        this.mSwitchView.addTab(intent.getStringExtra("group_first_album_name"));
        this.mSwitchView.addTab(intent.getStringExtra("group_second_album_name"));
        this.mSwitchView.setSelectedTab(this.mPosition);
        this.mSwitchView.setOnSwitchClickListener(new SwitchView.OnSwitchChangeListener() { // from class: com.miui.gallery.activity.AlbumDetailGroupingActivity$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.widget.SwitchView.OnSwitchChangeListener
            public final void onSwitchChange(int i) {
                AlbumDetailGroupingActivity.$r8$lambda$sqSklNSFXTL7AQysA5QKrVB_oC4(AlbumDetailGroupingActivity.this, i);
            }
        }, AlbumDetailGroupingActivity$$ExternalSyntheticLambda1.INSTANCE);
        this.mViewPager.setAdapter(new AlbumDetailGroupingAdapter(this, new ArrayList(Arrays.asList(Long.valueOf(intent.getLongExtra("group_first_album_id", -1L)), Long.valueOf(intent.getLongExtra("group_second_album_id", -1L))))));
        this.mViewPager.setCurrentItem(this.mPosition);
        this.mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() { // from class: com.miui.gallery.activity.AlbumDetailGroupingActivity.1
            {
                AlbumDetailGroupingActivity.this = this;
            }

            @Override // androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
            public void onPageSelected(int i) {
                AlbumDetailGroupingActivity.this.mSwitchView.setSelectedTab(i);
            }
        });
        View childAt = this.mViewPager.getChildAt(0);
        if (childAt instanceof RecyclerView) {
            childAt.setOverScrollMode(2);
        }
    }

    public /* synthetic */ void lambda$initView$0(int i) {
        if (i != this.mPosition) {
            this.mPosition = i;
            this.mIsShowFirstFragment = i == 0;
            this.mViewPager.setCurrentItem(i);
            trackClick();
        }
    }

    public final ModernAlbumDetailFragment getCurrentFragment() {
        return (ModernAlbumDetailFragment) getSupportFragmentManager().findFragmentByTag(getFragmentTag(this.mPosition));
    }

    public final String getFragmentTag(int i) {
        return "f" + i;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        ModernAlbumDetailFragment currentFragment = getCurrentFragment();
        if (currentFragment == null || !currentFragment.isVisible()) {
            return false;
        }
        currentFragment.onCreateOptionsMenu(menu, getMenuInflater());
        return true;
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        ModernAlbumDetailFragment currentFragment = getCurrentFragment();
        if (currentFragment != null && currentFragment.isVisible()) {
            currentFragment.onPrepareOptionsMenu(menu);
            return true;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        ModernAlbumDetailFragment currentFragment = getCurrentFragment();
        if (currentFragment == null || !currentFragment.isVisible() || !currentFragment.onOptionsItemSelected(menuItem)) {
            return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.MiuiActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        ModernAlbumDetailFragment currentFragment = getCurrentFragment();
        if (currentFragment != null) {
            currentFragment.onActivityResult(i, i2, intent);
        }
    }

    @Override // com.miui.gallery.activity.BaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        saveAlbumSortToPreference();
        if (this.mIsScreenshotRecorderAlbum) {
            GalleryPreferences.Album.setIsShowScreenshot(this.mIsShowFirstFragment);
        }
    }

    public final void saveAlbumSortToPreference() {
        boolean equals = this.mViewModel.getSortOrder().equals(" DESC ");
        int i = this.mViewModel.getSortBy() == SortBy.UPDATE_DATE ? !equals : 0;
        if (this.mViewModel.getSortBy() == SortBy.CREATE_DATE) {
            i = equals ? 2 : 3;
        }
        if (this.mViewModel.getSortBy() == SortBy.NAME) {
            i = equals ? 4 : 5;
        }
        if (this.mViewModel.getSortBy() == SortBy.SIZE) {
            i = equals ? 6 : 7;
        }
        GalleryPreferences.Album.setAlbumDetailSort(getIntent().getLongExtra("album_id", -1L), i);
    }

    @Override // com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, android.app.Activity, android.view.Window.Callback
    public void onActionModeStarted(ActionMode actionMode) {
        super.onActionModeStarted(actionMode);
        this.mViewPager.setUserInputEnabled(false);
        this.mSwitchView.setSwitchable(false);
    }

    @Override // com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, android.app.Activity, android.view.Window.Callback
    public void onActionModeFinished(ActionMode actionMode) {
        super.onActionModeFinished(actionMode);
        this.mViewPager.setUserInputEnabled(true);
        this.mSwitchView.setSwitchable(true);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int i) {
        ModernAlbumDetailFragment currentFragment = getCurrentFragment();
        if (currentFragment != null) {
            currentFragment.onProvideKeyboardShortcuts(list, menu, i);
        }
        super.onProvideKeyboardShortcuts(list, menu, i);
    }

    @Override // android.app.Activity
    public boolean onKeyShortcut(int i, KeyEvent keyEvent) {
        ModernAlbumDetailFragment currentFragment = getCurrentFragment();
        if (currentFragment == null || !currentFragment.onKeyShortcut(i, keyEvent)) {
            return super.onKeyShortcut(i, keyEvent);
        }
        return true;
    }

    public final void trackView() {
        if (this.mIsScreenshotRecorderAlbum) {
            HashMap hashMap = new HashMap(4, 1.0f);
            hashMap.put("tip", "403.73.1.1.18832");
            hashMap.put("ref_tip", AutoTracking.getRef());
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, this.mIsShowFirstFragment ? "screenshot" : "screen_recorder");
            TrackController.trackView(hashMap);
        }
    }

    public final void trackClick() {
        if (this.mIsScreenshotRecorderAlbum) {
            TrackController.trackClick("403.73.1.1.18831", AutoTracking.getRef(), this.mIsShowFirstFragment ? "screenshot" : "screen_recorder");
        }
    }
}
