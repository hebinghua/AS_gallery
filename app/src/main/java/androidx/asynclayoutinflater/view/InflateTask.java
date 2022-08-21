package androidx.asynclayoutinflater.view;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes.dex */
public class InflateTask implements Runnable {
    public OnInflateFinishedListener asyncCallback;
    public Handler handler;
    public GalleryAsyncLayoutInflater inflater;
    public ViewGroup parent;
    public int resId;
    public OnInflateFinishedListener syncCallback;
    public View view;

    public void setInflater(GalleryAsyncLayoutInflater galleryAsyncLayoutInflater) {
        this.inflater = galleryAsyncLayoutInflater;
    }

    public void setParent(ViewGroup viewGroup) {
        this.parent = viewGroup;
    }

    public void setResId(int i) {
        this.resId = i;
    }

    public void setAsyncCallback(OnInflateFinishedListener onInflateFinishedListener) {
        this.asyncCallback = onInflateFinishedListener;
    }

    public void setSyncCallback(Handler handler, OnInflateFinishedListener onInflateFinishedListener) {
        this.handler = handler;
        this.syncCallback = onInflateFinishedListener;
    }

    public void setView(View view) {
        this.view = view;
    }

    public GalleryAsyncLayoutInflater getInflater() {
        return this.inflater;
    }

    public ViewGroup getParent() {
        return this.parent;
    }

    public int getResId() {
        return this.resId;
    }

    public View getView() {
        return this.view;
    }

    public OnInflateFinishedListener getAsyncCallback() {
        return this.asyncCallback;
    }

    public OnInflateFinishedListener getSyncCallback() {
        return this.syncCallback;
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            View inflate = this.inflater.inflate(this.resId, this.parent, false);
            this.view = inflate;
            OnInflateFinishedListener onInflateFinishedListener = this.asyncCallback;
            if (onInflateFinishedListener != null) {
                onInflateFinishedListener.onInflateFinished(inflate, this.resId, this.parent);
            }
        } catch (RuntimeException e) {
            DefaultLogger.w("InflateRequest", "Failed to inflate resource in the background! Retrying on the UI thread", e);
        }
        postToMainHandler();
    }

    public final void postToMainHandler() {
        Message obtain = Message.obtain(this.handler, 0, this);
        obtain.setAsynchronous(true);
        obtain.sendToTarget();
    }

    public void release() {
        this.asyncCallback = null;
        this.syncCallback = null;
        this.inflater = null;
        this.parent = null;
        this.resId = -1;
        this.view = null;
    }
}
