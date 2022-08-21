package com.miui.gallery.ui.album.main.base;

import android.app.Activity;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.View;
import androidx.fragment.app.Fragment;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public abstract class AbsAlbumPageView {
    public WeakReference<Activity> mActivityRef;
    public Fragment mParent;

    public void onConfigurationChanged(Configuration configuration) {
    }

    public void onInit(View view) {
    }

    public AbsAlbumPageView(Fragment fragment) {
        this.mParent = fragment;
    }

    public AbsAlbumPageView(Activity activity) {
        this.mActivityRef = new WeakReference<>(activity);
    }

    public Activity getActivity() {
        WeakReference<Activity> weakReference = this.mActivityRef;
        return (weakReference == null || weakReference.get() == null) ? this.mParent.getActivity() : this.mActivityRef.get();
    }

    public Resources getResources() {
        WeakReference<Activity> weakReference = this.mActivityRef;
        return (weakReference == null || weakReference.get() == null) ? this.mParent.getResources() : this.mActivityRef.get().getResources();
    }

    public View getRootView() {
        WeakReference<Activity> weakReference = this.mActivityRef;
        if (weakReference == null || weakReference.get() == null || this.mActivityRef.get().getWindow() == null) {
            Fragment fragment = this.mParent;
            if (fragment == null) {
                return null;
            }
            return fragment.getView();
        }
        return this.mActivityRef.get().getWindow().getDecorView();
    }
}
