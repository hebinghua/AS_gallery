package androidx.asynclayoutinflater.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.util.Pools$SynchronizedPool;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/* loaded from: classes.dex */
public final class GalleryAsyncLayoutInflater {
    public LayoutInflater mInflater;
    public static Pools$SynchronizedPool<InflateTask> sTaskPool = new Pools$SynchronizedPool<>(10);
    public static ExecutorService sExecutor = Executors.newSingleThreadExecutor();
    public static Handler.Callback mHandlerCallback = GalleryAsyncLayoutInflater$$ExternalSyntheticLambda0.INSTANCE;

    public static /* synthetic */ boolean lambda$static$0(Message message) {
        InflateTask inflateTask = (InflateTask) message.obj;
        try {
            ViewGroup parent = inflateTask.getParent();
            if (inflateTask.getView() == null) {
                inflateTask.setView(inflateTask.getInflater().inflate(inflateTask.getResId(), parent, false));
                if (inflateTask.getAsyncCallback() != null) {
                    inflateTask.getAsyncCallback().onInflateFinished(inflateTask.getView(), inflateTask.getResId(), parent);
                }
            }
            inflateTask.getSyncCallback().onInflateFinished(inflateTask.getView(), inflateTask.getResId(), parent);
            releaseTask(inflateTask);
            return true;
        } catch (Throwable th) {
            releaseTask(inflateTask);
            throw th;
        }
    }

    public GalleryAsyncLayoutInflater(Context context) {
        this(context, null);
    }

    public GalleryAsyncLayoutInflater(Context context, LayoutInflater.Factory2 factory2) {
        BasicInflater basicInflater = new BasicInflater(context);
        this.mInflater = basicInflater;
        if (factory2 != null) {
            if (basicInflater.getFactory2() != null) {
                this.mInflater = this.mInflater.cloneInContext(context);
            }
            this.mInflater.setFactory2(factory2);
        }
    }

    public void inflate(int i, ViewGroup viewGroup, OnInflateFinishedListener onInflateFinishedListener, OnInflateFinishedListener onInflateFinishedListener2) {
        InflateTask obtainTask = obtainTask();
        obtainTask.setInflater(this);
        obtainTask.setResId(i);
        obtainTask.setParent(viewGroup);
        obtainTask.setAsyncCallback(onInflateFinishedListener);
        obtainTask.setSyncCallback(new Handler(mHandlerCallback), onInflateFinishedListener2);
        sExecutor.submit(obtainTask);
    }

    public void inflate(int i, ViewGroup viewGroup, OnInflateFinishedListener onInflateFinishedListener) {
        InflateTask obtainTask = obtainTask();
        obtainTask.setInflater(this);
        obtainTask.setResId(i);
        obtainTask.setParent(viewGroup);
        obtainTask.setSyncCallback(new Handler(mHandlerCallback), onInflateFinishedListener);
        sExecutor.submit(obtainTask);
    }

    public View inflate(int i, ViewGroup viewGroup, boolean z) {
        return this.mInflater.inflate(i, viewGroup, z);
    }

    public InflateTask obtainTask() {
        InflateTask acquire = sTaskPool.acquire();
        return acquire == null ? new InflateTask() : acquire;
    }

    public static void releaseTask(InflateTask inflateTask) {
        inflateTask.release();
        sTaskPool.release(inflateTask);
    }
}
