package miuix.appcompat.app.floatingactivity;

import android.view.View;
import android.view.ViewGroup;
import java.lang.ref.WeakReference;
import miuix.animation.base.AnimConfig;
import miuix.animation.listener.TransitionListener;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes3.dex */
public class SingleAppFloatingLifecycleObserver extends FloatingLifecycleObserver {
    public static /* synthetic */ void $r8$lambda$wyPUwucF5VoB6sYXCVAP3ofo5Yo(SingleAppFloatingLifecycleObserver singleAppFloatingLifecycleObserver, View view, AppCompatActivity appCompatActivity) {
        singleAppFloatingLifecycleObserver.lambda$executeCloseExit$0(view, appCompatActivity);
    }

    public SingleAppFloatingLifecycleObserver(AppCompatActivity appCompatActivity) {
        super(appCompatActivity);
    }

    @Override // miuix.appcompat.app.floatingactivity.FloatingLifecycleObserver
    public void onCreate() {
        AppCompatActivity activity = getActivity();
        if (activity == null || FloatingActivitySwitcher.getInstance().getPreviousActivity(activity) == null || !activity.isInFloatingWindowMode()) {
            return;
        }
        activity.executeOpenEnterAnimation();
    }

    @Override // miuix.appcompat.app.floatingactivity.FloatingLifecycleObserver
    public void onResume() {
        AppCompatActivity activity = getActivity();
        if (activity == null || !activity.isInFloatingWindowMode()) {
            return;
        }
        if (FloatingActivitySwitcher.getInstance().getPreviousActivity(activity) != null) {
            activity.hideFloatingDimBackground();
        }
        reenterTransition(activity);
    }

    @Override // miuix.appcompat.app.floatingactivity.FloatingLifecycleObserver
    public void onDestroy() {
        FloatingActivitySwitcher.getInstance().remove(getActivity());
    }

    public final void reenterTransition(AppCompatActivity appCompatActivity) {
        AppCompatActivity appCompatActivity2;
        int activityIndex = FloatingActivitySwitcher.getInstance().getActivityIndex(appCompatActivity) + 1;
        if (activityIndex >= FloatingActivitySwitcher.getInstance().getActivityCache().size() || (appCompatActivity2 = FloatingActivitySwitcher.getInstance().getActivityCache().get(activityIndex)) == null || !appCompatActivity2.isFinishing()) {
            return;
        }
        executeCloseExit(appCompatActivity);
    }

    public final void executeCloseExit(final AppCompatActivity appCompatActivity) {
        final View lastActivityPanel = FloatingActivitySwitcher.getInstance().getLastActivityPanel();
        if (lastActivityPanel != null) {
            lastActivityPanel.post(new Runnable() { // from class: miuix.appcompat.app.floatingactivity.SingleAppFloatingLifecycleObserver$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    SingleAppFloatingLifecycleObserver.$r8$lambda$wyPUwucF5VoB6sYXCVAP3ofo5Yo(SingleAppFloatingLifecycleObserver.this, lastActivityPanel, appCompatActivity);
                }
            });
        }
    }

    public /* synthetic */ void lambda$executeCloseExit$0(View view, AppCompatActivity appCompatActivity) {
        View childAt = ((ViewGroup) view).getChildAt(0);
        if (childAt != null) {
            AnimConfig animConfig = FloatingSwitcherAnimHelper.getAnimConfig(0, null);
            animConfig.addListeners(new CloseExitListener(appCompatActivity));
            FloatingSwitcherAnimHelper.executeCloseExitAnimation(childAt, animConfig);
        }
    }

    /* loaded from: classes3.dex */
    public class CloseExitListener extends TransitionListener {
        public WeakReference<AppCompatActivity> mHostActivity;

        public CloseExitListener(AppCompatActivity appCompatActivity) {
            SingleAppFloatingLifecycleObserver.this = r1;
            this.mHostActivity = new WeakReference<>(appCompatActivity);
        }

        @Override // miuix.animation.listener.TransitionListener
        public void onComplete(Object obj) {
            View lastActivityPanel;
            super.onComplete(obj);
            AppCompatActivity appCompatActivity = this.mHostActivity.get();
            if (appCompatActivity == null || appCompatActivity.isDestroyed() || (lastActivityPanel = FloatingActivitySwitcher.getInstance().getLastActivityPanel()) == null) {
                return;
            }
            ((ViewGroup) appCompatActivity.getFloatingBrightPanel().getParent()).getOverlay().remove(lastActivityPanel);
        }
    }
}
