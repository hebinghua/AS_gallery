package com.miui.gallery.base_optimization.fragment.support;

import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.miui.gallery.base_optimization.fragment.utils.FragmentUtils;
import java.lang.ref.WeakReference;
import java.util.List;

/* loaded from: classes.dex */
public class ActivityFragmentSupportDelegate implements FragmentSupport$IActivityFragmentSupport<Fragment> {
    public WeakReference<FragmentActivity> mActivityWeakReference;
    public int mContainerId;
    public String mRootFragmentName;

    public ActivityFragmentSupportDelegate(int i, FragmentActivity fragmentActivity) {
        this.mActivityWeakReference = new WeakReference<>(fragmentActivity);
        this.mContainerId = i;
    }

    @Override // com.miui.gallery.base_optimization.fragment.support.FragmentSupport$IActivityFragmentSupport
    public void loadRootFragment(int i, Fragment fragment) {
        this.mContainerId = i;
        this.mRootFragmentName = getName(fragment);
        Bundle arguments = fragment.getArguments();
        if (arguments != null) {
            arguments.putInt("fragment_contail_id", this.mContainerId);
        }
        FragmentUtils.startFragment(this.mContainerId, getFragmentManager(), fragment, null, true, false, this.mRootFragmentName);
    }

    public List<Fragment> getFragments() {
        if (Build.VERSION.SDK_INT >= 26) {
            return getFragmentManager().getFragments();
        }
        return null;
    }

    @Override // com.miui.gallery.base_optimization.fragment.support.FragmentSupport$IActivityFragmentSupport
    public Fragment getTopFragment() {
        List<Fragment> fragments = getFragments();
        if (fragments == null || fragments.isEmpty()) {
            return null;
        }
        return fragments.get(fragments.size() - 1);
    }

    public final String getName(Fragment fragment) {
        return fragment == null ? "" : fragment.getClass().getCanonicalName();
    }

    public FragmentManager getFragmentManager() {
        WeakReference<FragmentActivity> weakReference = this.mActivityWeakReference;
        if (weakReference == null || weakReference.get() == null) {
            return null;
        }
        return this.mActivityWeakReference.get().getSupportFragmentManager();
    }
}
