package com.miui.gallery.picker;

import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.stat.SamplingStatHelper;

/* loaded from: classes2.dex */
public abstract class PickerCompatFragment extends PickerBaseFragment {
    public FragmentActivity mActivity;
    public boolean mUserFirstVisible = false;

    public abstract String getPageName();

    public int getThemeRes() {
        return 2131952018;
    }

    public void onUserFirstVisible() {
    }

    public boolean recordPageByDefault() {
        return true;
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof FragmentActivity)) {
            throw new IllegalArgumentException("Host activity should be FragmentActivity");
        }
        this.mActivity = (FragmentActivity) activity;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        this.mActivity = null;
    }

    @Override // com.miui.gallery.picker.PickerBaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        int themeRes = getThemeRes();
        if (themeRes != 0) {
            setThemeRes(themeRes);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        if (recordPageByDefault()) {
            SamplingStatHelper.recordPageEnd(this.mActivity, getPageName());
        }
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        if (recordPageByDefault()) {
            SamplingStatHelper.recordPageStart(this.mActivity, getPageName());
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void setUserVisibleHint(boolean z) {
        super.setUserVisibleHint(z);
        if (!getUserVisibleHint() || this.mUserFirstVisible) {
            return;
        }
        onUserFirstVisible();
        this.mUserFirstVisible = true;
    }
}
