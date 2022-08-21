package com.miui.gallery.provider;

import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.SparseArray;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public abstract class GalleryContentResolver implements Handler.Callback {
    public final SparseArray<Integer> mDelayNums;
    public final Handler mHandler;

    public abstract Object matchUri(Uri uri);

    public abstract void notifyInternal(Uri uri, ContentObserver contentObserver, long j, Bundle bundle);

    public GalleryContentResolver() {
        if (Looper.myLooper() != null) {
            this.mHandler = new Handler(Looper.myLooper(), this);
        } else {
            this.mHandler = new Handler(Looper.getMainLooper(), this);
        }
        this.mDelayNums = new SparseArray<>();
    }

    public final void notifyChange(Uri uri, ContentObserver contentObserver, long j) {
        notifyChange(uri, contentObserver, j, null);
    }

    public final void notifyChange(Uri uri, ContentObserver contentObserver, long j, Bundle bundle) {
        notifyInternal(uri, contentObserver, j, bundle);
    }

    public final void notifyChangeDelay(Uri uri, ContentObserver contentObserver, long j) {
        notifyChangeDelay(uri, contentObserver, j, null);
    }

    public final void notifyChangeDelay(Uri uri, ContentObserver contentObserver, long j, Bundle bundle) {
        int hashCode = matchUri(uri).hashCode();
        this.mHandler.removeMessages(hashCode);
        NotifyParams notifyParams = new NotifyParams();
        notifyParams.uri = uri;
        notifyParams.observer = contentObserver;
        notifyParams.syncReason = j;
        notifyParams.extra = bundle;
        Message obtainMessage = this.mHandler.obtainMessage(hashCode, notifyParams);
        int i = 0;
        int intValue = this.mDelayNums.get(hashCode, 0).intValue() + 1;
        if (intValue > 100) {
            this.mHandler.sendMessage(obtainMessage);
        } else {
            this.mHandler.sendMessageDelayed(obtainMessage, 1000L);
            i = intValue;
        }
        this.mDelayNums.put(hashCode, Integer.valueOf(i));
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        NotifyParams notifyParams = (NotifyParams) message.obj;
        notifyChange(notifyParams.uri, notifyParams.observer, notifyParams.syncReason);
        this.mDelayNums.put(message.what, 0);
        DefaultLogger.d("GalleryContentResolver", "delay notify %s", notifyParams.uri);
        return false;
    }

    /* loaded from: classes2.dex */
    public static class NotifyParams {
        public Bundle extra;
        public ContentObserver observer;
        public long syncReason;
        public Uri uri;

        public NotifyParams() {
        }
    }
}
