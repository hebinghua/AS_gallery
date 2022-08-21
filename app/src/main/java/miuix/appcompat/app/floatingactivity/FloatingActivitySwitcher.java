package miuix.appcompat.app.floatingactivity;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes3.dex */
public class FloatingActivitySwitcher {
    public static FloatingActivitySwitcher sInstance;
    public final String LAST_ACTIVITY_PANEL_TAG = "last_activity_panel_tag";
    public ArrayList<AppCompatActivity> mActivityCache = new ArrayList<>();
    public boolean mEnableDragToDismiss;
    public View mLastActivityPanel;

    public static FloatingActivitySwitcher getInstance() {
        return sInstance;
    }

    public static void install(AppCompatActivity appCompatActivity) {
        install(appCompatActivity, true, null);
    }

    public static void install(AppCompatActivity appCompatActivity, boolean z, Bundle bundle) {
        if (sInstance == null) {
            FloatingActivitySwitcher floatingActivitySwitcher = new FloatingActivitySwitcher();
            sInstance = floatingActivitySwitcher;
            floatingActivitySwitcher.mEnableDragToDismiss = z;
        }
        sInstance.init(appCompatActivity, bundle);
    }

    public static void onSaveInstanceState(AppCompatActivity appCompatActivity, Bundle bundle) {
        if (getInstance() == null || bundle == null) {
            return;
        }
        bundle.putInt("floating_switcher_saved_key", getInstance().getActivityIndex(appCompatActivity));
    }

    public final void init(AppCompatActivity appCompatActivity, Bundle bundle) {
        appCompatActivity.getLifecycle().addObserver(new SingleAppFloatingLifecycleObserver(appCompatActivity));
        stashActivity(appCompatActivity, bundle);
        appCompatActivity.setEnableSwipToDismiss(this.mEnableDragToDismiss);
        appCompatActivity.setOnFloatingCallback(new DefineOnFloatingActivityCallback(appCompatActivity));
    }

    public final void closeAllActivity() {
        for (int size = this.mActivityCache.size() - 2; size >= 0; size--) {
            this.mActivityCache.get(size).realFinish();
        }
    }

    public final void closeTopActivity() {
        if (this.mActivityCache.size() == 0) {
            return;
        }
        ArrayList<AppCompatActivity> arrayList = this.mActivityCache;
        arrayList.get(arrayList.size() - 1).realFinish();
    }

    public ArrayList<AppCompatActivity> getActivityCache() {
        return this.mActivityCache;
    }

    public int getActivityIndex(AppCompatActivity appCompatActivity) {
        if (appCompatActivity != null) {
            return this.mActivityCache.indexOf(appCompatActivity);
        }
        return -1;
    }

    public AppCompatActivity getPreviousActivity(AppCompatActivity appCompatActivity) {
        int activityIndex = getActivityIndex(appCompatActivity);
        if (activityIndex > 0) {
            return this.mActivityCache.get(activityIndex - 1);
        }
        return null;
    }

    public final boolean isActivityStashed(AppCompatActivity appCompatActivity) {
        return this.mActivityCache.contains(appCompatActivity);
    }

    public final void hideTopBgs() {
        for (int i = 1; i < this.mActivityCache.size(); i++) {
            this.mActivityCache.get(i).hideFloatingDimBackground();
        }
    }

    public final void stashActivity(AppCompatActivity appCompatActivity, Bundle bundle) {
        if (!isActivityStashed(appCompatActivity)) {
            if (bundle != null) {
                int i = 0;
                int i2 = bundle.getInt("floating_switcher_saved_key", 0);
                ArrayList<AppCompatActivity> arrayList = this.mActivityCache;
                if (i2 <= arrayList.size()) {
                    i = i2;
                }
                arrayList.add(i, appCompatActivity);
                hideTopBgs();
                return;
            }
            this.mActivityCache.add(appCompatActivity);
        }
    }

    public void remove(AppCompatActivity appCompatActivity) {
        if (appCompatActivity != null) {
            this.mActivityCache.remove(appCompatActivity);
        }
        if (this.mActivityCache.isEmpty()) {
            clear();
        }
    }

    public View getLastActivityPanel() {
        return this.mLastActivityPanel;
    }

    public void setLastActivityPanel(View view) {
        this.mLastActivityPanel = view;
    }

    public void clear() {
        this.mActivityCache.clear();
        this.mLastActivityPanel = null;
        sInstance = null;
    }

    public final void hideBehindPages(AppCompatActivity appCompatActivity) {
        for (int activityIndex = getActivityIndex(appCompatActivity) - 1; activityIndex >= 0; activityIndex--) {
            this.mActivityCache.get(activityIndex).hideFloatingBrightPanel();
        }
    }

    /* loaded from: classes3.dex */
    public class DefineOnFloatingActivityCallback implements OnFloatingCallback {
        public WeakReference<AppCompatActivity> mAppCompatActivityWeakReference;

        public boolean checkActivity(AppCompatActivity appCompatActivity) {
            return appCompatActivity != null;
        }

        public DefineOnFloatingActivityCallback(AppCompatActivity appCompatActivity) {
            this.mAppCompatActivityWeakReference = new WeakReference<>(appCompatActivity);
        }

        public AppCompatActivity getActivity() {
            return this.mAppCompatActivityWeakReference.get();
        }

        @Override // miuix.appcompat.app.floatingactivity.OnFloatingActivityCallback
        public boolean onFinish(int i) {
            if (checkFinishEnable(i)) {
                return false;
            }
            if (shouldTopFloatingClose(i)) {
                FloatingActivitySwitcher.this.closeTopActivity();
            } else {
                FloatingActivitySwitcher.this.closeAllActivity();
            }
            return false;
        }

        public final boolean checkFinishEnable(int i) {
            return !FloatingActivitySwitcher.this.mEnableDragToDismiss && (i == 1 || i == 2);
        }

        public final boolean shouldTopFloatingClose(int i) {
            return (i == 4 || i == 3) && FloatingActivitySwitcher.this.mActivityCache.size() > 1;
        }

        @Override // miuix.appcompat.app.floatingactivity.OnFloatingCallback
        public void onDragStart() {
            FloatingActivitySwitcher.this.hideBehindPages(getActivity());
        }

        @Override // miuix.appcompat.app.floatingactivity.OnFloatingCallback
        public void onDragEnd() {
            AppCompatActivity activity = getActivity();
            if (checkActivity(activity)) {
                for (int activityIndex = FloatingActivitySwitcher.this.getActivityIndex(activity) - 1; activityIndex >= 0; activityIndex--) {
                    ((AppCompatActivity) FloatingActivitySwitcher.this.mActivityCache.get(activityIndex)).showFloatingBrightPanel();
                }
            }
        }

        @Override // miuix.appcompat.app.floatingactivity.OnFloatingCallback
        public void onHideBehindPage() {
            FloatingActivitySwitcher.this.hideBehindPages(getActivity());
        }

        @Override // miuix.appcompat.app.floatingactivity.OnFloatingCallback
        public int getPageCount() {
            return FloatingActivitySwitcher.this.mActivityCache.size();
        }

        @Override // miuix.appcompat.app.floatingactivity.OnFloatingCallback
        public void getSnapShotAndSetPanel(AppCompatActivity appCompatActivity) {
            AppCompatActivity previousActivity;
            View generateSnapShotView;
            if (appCompatActivity == null || (previousActivity = FloatingActivitySwitcher.getInstance().getPreviousActivity(appCompatActivity)) == null) {
                return;
            }
            int i = 0;
            do {
                generateSnapShotView = SnapShotViewHelper.generateSnapShotView(previousActivity, appCompatActivity);
                i++;
                if (generateSnapShotView != null) {
                    break;
                }
            } while (i < 3);
            FloatingActivitySwitcher.getInstance().setLastActivityPanel(generateSnapShotView);
            addLastActivityPanel(previousActivity);
        }

        public final void addLastActivityPanel(AppCompatActivity appCompatActivity) {
            ViewGroup viewGroup;
            View lastActivityPanel = FloatingActivitySwitcher.getInstance().getLastActivityPanel();
            if (lastActivityPanel == null || (viewGroup = (ViewGroup) appCompatActivity.getFloatingBrightPanel().getParent()) == null) {
                return;
            }
            viewGroup.getOverlay().clear();
            viewGroup.getOverlay().add(lastActivityPanel);
        }
    }
}
