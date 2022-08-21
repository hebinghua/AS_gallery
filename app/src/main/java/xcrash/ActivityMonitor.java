package xcrash;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import java.util.Iterator;
import java.util.LinkedList;

/* loaded from: classes3.dex */
public class ActivityMonitor {
    public static final ActivityMonitor instance = new ActivityMonitor();
    public LinkedList<Activity> activities = null;
    public boolean isAppForeground = false;

    public static ActivityMonitor getInstance() {
        return instance;
    }

    public void initialize(Application application) {
        this.activities = new LinkedList<>();
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() { // from class: xcrash.ActivityMonitor.1
            public int activityReferences = 0;
            public boolean isActivityChangingConfigurations = false;

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityPaused(Activity activity) {
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityResumed(Activity activity) {
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityCreated(Activity activity, Bundle bundle) {
                ActivityMonitor.this.activities.addFirst(activity);
                if (ActivityMonitor.this.activities.size() > 100) {
                    ActivityMonitor.this.activities.removeLast();
                }
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityStarted(Activity activity) {
                int i = this.activityReferences + 1;
                this.activityReferences = i;
                if (i != 1 || this.isActivityChangingConfigurations) {
                    return;
                }
                ActivityMonitor.this.isAppForeground = true;
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityStopped(Activity activity) {
                boolean isChangingConfigurations = activity.isChangingConfigurations();
                this.isActivityChangingConfigurations = isChangingConfigurations;
                int i = this.activityReferences - 1;
                this.activityReferences = i;
                if (i != 0 || isChangingConfigurations) {
                    return;
                }
                ActivityMonitor.this.isAppForeground = false;
            }

            @Override // android.app.Application.ActivityLifecycleCallbacks
            public void onActivityDestroyed(Activity activity) {
                ActivityMonitor.this.activities.remove(activity);
            }
        });
    }

    public void finishAllActivities() {
        LinkedList<Activity> linkedList = this.activities;
        if (linkedList != null) {
            Iterator<Activity> it = linkedList.iterator();
            while (it.hasNext()) {
                it.next().finish();
            }
            this.activities.clear();
        }
    }

    public boolean isApplicationForeground() {
        return this.isAppForeground;
    }
}
