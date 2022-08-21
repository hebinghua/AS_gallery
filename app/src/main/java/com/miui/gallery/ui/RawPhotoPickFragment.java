package com.miui.gallery.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.CleanerPhotoPickFragment;

/* loaded from: classes2.dex */
public class RawPhotoPickFragment extends CleanerPhotoPickFragment {
    @Override // com.miui.gallery.ui.PhotoListFragmentBase
    public int getLayoutSource() {
        return R.layout.raw_result_pick_layout;
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "cleaner_raw_photo_pick";
    }

    public RawPhotoPickFragment() {
        super(4);
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment
    public void recordDeleteEvent(int i) {
        TrackController.trackClick("403.27.4.1.11327", AutoTracking.getRef());
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment
    public void recordSelectGroupEvent() {
        TrackController.trackClick("403.27.4.1.11326", AutoTracking.getRef());
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment
    public void recordSelectAllEvent() {
        TrackController.trackClick("403.27.4.1.11325", AutoTracking.getRef());
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment, com.miui.gallery.ui.PhotoListFragmentBase, miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onInflateView = super.onInflateView(layoutInflater, viewGroup, bundle);
        this.mActivity.getAppCompatActionBar().setTitle(R.string.cleaner_raw_title);
        return onInflateView;
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment
    public void recordCancelSelectAllEvent() {
        SamplingStatHelper.recordCountEvent("cleaner", "raw_select_all_cancel");
    }

    @Override // com.miui.gallery.ui.CleanerPhotoPickFragment
    public CleanerPhotoPickFragment.DeleteMessage getDeleteMessage() {
        return new CleanerPhotoPickFragment.DeleteMessage.Builder().setCleanerSubEvent("cleaner_raw_used").setReason(44).build();
    }
}
