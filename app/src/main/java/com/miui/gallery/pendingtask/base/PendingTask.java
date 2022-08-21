package com.miui.gallery.pendingtask.base;

/* loaded from: classes2.dex */
public abstract class PendingTask<T> {
    public boolean isCancelled = false;
    public Callback mCallback;
    public int mType;

    /* loaded from: classes2.dex */
    public interface Callback {
        boolean isCancelled();
    }

    public long getMinLatency() {
        return 0L;
    }

    public abstract int getNetworkType();

    /* renamed from: parseData */
    public abstract T mo1252parseData(byte[] bArr) throws Exception;

    public abstract boolean process(T t) throws Exception;

    public abstract boolean requireCharging();

    public abstract boolean requireDeviceIdle();

    public abstract byte[] wrapData(T t) throws Exception;

    public PendingTask(int i) {
        this.mType = -1;
        this.mType = i;
    }

    public int getType() {
        return this.mType;
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public boolean isCancelled() {
        Callback callback;
        return this.isCancelled || ((callback = this.mCallback) != null && callback.isCancelled());
    }
}
