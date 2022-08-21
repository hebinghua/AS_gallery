package com.miui.gallery.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.miui.gallery.R;
import com.miui.gallery.adapter.AlbumType;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.CleanerPhotoPickFragment;

/* loaded from: classes2.dex */
public class ScreenshotPhotoPickFragment extends CleanerPhotoPickFragment {
    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public int getLayoutSource() {
        return R.layout.screenshot_photo_pick_layout;
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "cleaner_screenshot_photo_pick";
    }

    public ScreenshotPhotoPickFragment() {
        super(1);
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment
    public void recordDeleteEvent(int i) {
        TrackController.trackClick("403.27.3.1.11324", AutoTracking.getRef());
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment
    public void recordSelectGroupEvent() {
        TrackController.trackClick("403.27.3.1.11323", AutoTracking.getRef());
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment
    public void recordSelectAllEvent() {
        TrackController.trackClick("403.27.3.1.11322", AutoTracking.getRef());
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment, com.miui.gallery.ui.PhotoListFragmentBase, miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onInflateView = super.onInflateView(layoutInflater, viewGroup, bundle);
        this.mActivity.getAppCompatActionBar().setTitle(R.string.cleaner_screen_shot_title);
        mo1564getAdapter().setAlbumType(AlbumType.SCREENSHOT);
        updateConfiguration(getResources().getConfiguration());
        return onInflateView;
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment
    public void recordCancelSelectAllEvent() {
        SamplingStatHelper.recordCountEvent("cleaner", "screenshot_select_all_cancel");
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment
    public CleanerPhotoPickFragment.DeleteMessage getDeleteMessage() {
        return new CleanerPhotoPickFragment.DeleteMessage.Builder().setCleanerSubEvent("cleaner_screenshot_used").setReason(46).build();
    }
}
