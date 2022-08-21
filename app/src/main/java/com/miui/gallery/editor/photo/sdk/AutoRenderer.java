package com.miui.gallery.editor.photo.sdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import com.miui.filtersdk.BeautificationSDK;
import com.miui.gallery.editor.photo.core.imports.filter.FilterEngine;
import com.miui.gallery.editor.photo.core.imports.filter.FilterManager;

/* loaded from: classes2.dex */
public class AutoRenderer {
    public AutoRenderTask mAutoRenderTask;
    public FilterEngine mFilterEngine;

    /* loaded from: classes2.dex */
    public interface Callback {
        void onDone(Bitmap bitmap);

        void onError(int i, Object obj);
    }

    public AutoRenderer(Context context) {
        Context applicationContext = context.getApplicationContext();
        this.mFilterEngine = new FilterEngine(applicationContext);
        BeautificationSDK.init(applicationContext);
    }

    public void render(Bitmap bitmap, Callback callback) {
        AutoRenderTask autoRenderTask = new AutoRenderTask(callback);
        this.mAutoRenderTask = autoRenderTask;
        autoRenderTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, bitmap);
    }

    public Bitmap render(Bitmap bitmap) {
        return this.mFilterEngine.render(bitmap, FilterManager.getAutoBeautifyData(), (Object) null);
    }

    public void release() {
        this.mFilterEngine.release();
        this.mFilterEngine = null;
        AutoRenderTask autoRenderTask = this.mAutoRenderTask;
        if (autoRenderTask != null) {
            autoRenderTask.cancel(true);
        }
    }

    /* loaded from: classes2.dex */
    public class AutoRenderTask extends AsyncTask<Bitmap, Void, Bitmap> {
        public Callback mCallback;

        public AutoRenderTask(Callback callback) {
            this.mCallback = callback;
        }

        @Override // android.os.AsyncTask
        public Bitmap doInBackground(Bitmap... bitmapArr) {
            if (AutoRenderer.this.mFilterEngine != null) {
                return AutoRenderer.this.mFilterEngine.render(bitmapArr[0], FilterManager.getAutoBeautifyData(), (Object) null);
            }
            return null;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                this.mCallback.onDone(bitmap);
            } else {
                this.mCallback.onError(0, bitmap);
            }
            super.onPostExecute((AutoRenderTask) bitmap);
        }
    }
}
