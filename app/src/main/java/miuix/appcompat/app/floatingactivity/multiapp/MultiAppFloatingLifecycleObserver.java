package miuix.appcompat.app.floatingactivity.multiapp;

import miuix.appcompat.app.AppCompatActivity;
import miuix.appcompat.app.floatingactivity.FloatingLifecycleObserver;

/* loaded from: classes3.dex */
public class MultiAppFloatingLifecycleObserver extends FloatingLifecycleObserver {
    @Override // miuix.appcompat.app.floatingactivity.FloatingLifecycleObserver
    public void onDestroy() {
        MultiAppFloatingActivitySwitcher.getInstance();
        getActivity();
        throw null;
    }

    @Override // miuix.appcompat.app.floatingactivity.FloatingLifecycleObserver
    public void onCreate() {
        final AppCompatActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        MultiAppFloatingActivitySwitcher.getInstance();
        new Runnable() { // from class: miuix.appcompat.app.floatingactivity.multiapp.MultiAppFloatingLifecycleObserver.1
            @Override // java.lang.Runnable
            public void run() {
                MultiAppFloatingActivitySwitcher.getInstance();
                throw null;
            }
        };
        throw null;
    }

    @Override // miuix.appcompat.app.floatingactivity.FloatingLifecycleObserver
    public void onResume() {
        if (getActivity() == null) {
            return;
        }
        MultiAppFloatingActivitySwitcher.getInstance();
        throw null;
    }

    @Override // miuix.appcompat.app.floatingactivity.FloatingLifecycleObserver
    public void onPause() {
        if (getActivity() == null) {
            return;
        }
        MultiAppFloatingActivitySwitcher.getInstance();
        throw null;
    }
}
