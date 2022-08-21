package com.miui.gallery.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import com.miui.gallery.activity.HomePageActivity;
import com.miui.gallery.analytics.TimeMonitor;

/* loaded from: classes2.dex */
public class GalleryAppLifecycle implements Application.ActivityLifecycleCallbacks {
    public boolean mHasTrackClodStart;
    public int mCreatedActivities = 0;
    public int mStartedActivities = 0;

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityPaused(Activity activity) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityCreated(Activity activity, Bundle bundle) {
        this.mCreatedActivities++;
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStarted(Activity activity) {
        this.mStartedActivities++;
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityResumed(Activity activity) {
        if (this.mCreatedActivities != 1 || this.mHasTrackClodStart) {
            return;
        }
        if (activity instanceof HomePageActivity) {
            TimeMonitor.trackTimeMonitor("403.1.0.1.13757");
        } else {
            TimeMonitor.cancelTimeMonitor("403.1.0.1.13757");
        }
        this.mHasTrackClodStart = true;
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityStopped(Activity activity) {
        this.mStartedActivities--;
    }

    @Override // android.app.Application.ActivityLifecycleCallbacks
    public void onActivityDestroyed(Activity activity) {
        this.mCreatedActivities--;
    }
}
