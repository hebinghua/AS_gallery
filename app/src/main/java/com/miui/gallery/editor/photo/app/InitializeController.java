package com.miui.gallery.editor.photo.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.Toast;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class InitializeController {
    public Activity mActivity;
    public AsyncTask<Void, Void, Integer> mBackgroundTask = new AsyncTask<Void, Void, Integer>() { // from class: com.miui.gallery.editor.photo.app.InitializeController.1
        @Override // android.os.AsyncTask
        public Integer doInBackground(Void... voidArr) {
            return Integer.valueOf(InitializeController.this.mCallbacks.doInitialize());
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Integer num) {
            super.onPostExecute((AnonymousClass1) num);
            InitializeController.this.postResult(num.intValue());
        }
    };
    public Callbacks mCallbacks;

    /* loaded from: classes2.dex */
    public interface Callbacks {
        int doInitialize();

        void onDone();
    }

    public InitializeController(Activity activity, Callbacks callbacks) {
        this.mActivity = activity;
        this.mCallbacks = callbacks;
    }

    public void doInitialize() {
        this.mBackgroundTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public final void postResult(int i) {
        if (!this.mActivity.isFinishing()) {
            if (i == 3) {
                this.mCallbacks.onDone();
                return;
            }
            if (i == 2) {
                Toast.makeText(this.mActivity, (int) R.string.image_decode_failed, 0).show();
            } else if (i == 1) {
                Toast.makeText(this.mActivity, (int) R.string.image_not_found, 0).show();
            }
            this.mActivity.finish();
        }
    }
}
