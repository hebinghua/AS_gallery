package com.miui.gallery.security;

import android.os.AsyncTask;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
abstract class NotifyTask<Params, Result> implements DefaultLifecycleObserver {
    public WeakReference<FragmentActivity> mActivityRef;
    public Params[] mParams;
    public AsyncTask<Params, Void, Result> mTask;

    public abstract void doNotify(FragmentActivity fragmentActivity, Result result);

    public abstract Result doProcess(Params... paramsArr);

    public NotifyTask(FragmentActivity fragmentActivity) {
        this.mActivityRef = new WeakReference<>(fragmentActivity);
    }

    public void start(Params... paramsArr) {
        this.mParams = paramsArr;
        FragmentActivity fragmentActivity = this.mActivityRef.get();
        if (fragmentActivity == null) {
            throw new IllegalStateException("the activity has been recycled");
        }
        fragmentActivity.getLifecycle().addObserver(this);
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public final void onResume(LifecycleOwner lifecycleOwner) {
        AsyncTask<Params, Void, Result> asyncTask = new AsyncTask<Params, Void, Result>() { // from class: com.miui.gallery.security.NotifyTask.1
            @Override // android.os.AsyncTask
            public Result doInBackground(Params... paramsArr) {
                return (Result) NotifyTask.this.doProcess(paramsArr);
            }

            @Override // android.os.AsyncTask
            public void onPostExecute(Result result) {
                if (NotifyTask.this.mTask.isCancelled() || NotifyTask.this.mActivityRef.get() == null) {
                    return;
                }
                NotifyTask notifyTask = NotifyTask.this;
                notifyTask.doNotify((FragmentActivity) notifyTask.mActivityRef.get(), result);
            }
        };
        this.mTask = asyncTask;
        asyncTask.execute(this.mParams);
    }

    public final void release() {
        AsyncTask<Params, Void, Result> asyncTask = this.mTask;
        if (asyncTask != null) {
            asyncTask.cancel(true);
        }
        FragmentActivity fragmentActivity = this.mActivityRef.get();
        if (fragmentActivity != null) {
            fragmentActivity.getLifecycle().removeObserver(this);
        }
    }

    @Override // androidx.lifecycle.DefaultLifecycleObserver, androidx.lifecycle.FullLifecycleObserver
    public final void onDestroy(LifecycleOwner lifecycleOwner) {
        release();
    }
}
