package miuix.appcompat.app.floatingactivity;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import java.lang.ref.WeakReference;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes3.dex */
public class FloatingLifecycleObserver implements LifecycleObserver {
    public WeakReference<AppCompatActivity> mAppCompatActivityWeakReference;

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void onCreate() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
    }

    public FloatingLifecycleObserver(AppCompatActivity appCompatActivity) {
        this.mAppCompatActivityWeakReference = new WeakReference<>(appCompatActivity);
    }

    public AppCompatActivity getActivity() {
        return this.mAppCompatActivityWeakReference.get();
    }
}
