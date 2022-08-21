package cn.kuaipan.android.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/* loaded from: classes.dex */
public class SyncAccessor extends Handler {
    public Object handleAccess(int i, Object... objArr) {
        return null;
    }

    /* loaded from: classes.dex */
    public static class Args {
        public static Args mPool;
        public static int mPoolSize;
        public static Object mPoolSync = new Object();
        public RuntimeException err;
        public volatile boolean handled = false;
        public Args next;
        public Object[] params;
        public Object result;

        public static Args obtain() {
            synchronized (mPoolSync) {
                Args args = mPool;
                if (args != null) {
                    mPool = args.next;
                    args.next = null;
                    return args;
                }
                return new Args();
            }
        }

        public void recycle() {
            synchronized (mPoolSync) {
                if (mPoolSize < 10) {
                    clearForRecycle();
                    this.next = mPool;
                    mPool = this;
                }
            }
        }

        public final void clearForRecycle() {
            this.params = null;
            this.result = null;
            this.err = null;
            this.handled = false;
        }
    }

    public SyncAccessor(Looper looper) {
        super(looper);
    }

    public synchronized <T> T access(int i, Object... objArr) throws InterruptedException {
        T t;
        Args obtain = Args.obtain();
        obtain.params = objArr;
        if (sendMessage(obtainMessage(i, obtain))) {
            while (!obtain.handled) {
                if (!isAlive()) {
                    throw new RuntimeException("SyncAccessor has dead.");
                }
                synchronized (obtain) {
                    obtain.wait(50L);
                }
            }
            t = (T) obtain.result;
            RuntimeException runtimeException = obtain.err;
            obtain.recycle();
            if (runtimeException != null) {
                throw runtimeException;
            }
        } else {
            throw new RuntimeException("SyncAccessor has dead.");
        }
        return t;
    }

    public final boolean isAlive() {
        Thread thread;
        Looper looper = getLooper();
        return (looper == null || (thread = looper.getThread()) == null || !thread.isAlive()) ? false : true;
    }

    @Override // android.os.Handler
    public void dispatchMessage(Message message) {
        Object obj = message.obj;
        if (obj != null && (obj instanceof Args)) {
            Args args = (Args) obj;
            try {
                try {
                    args.result = handleAccess(message.what, args.params);
                    args.handled = true;
                    synchronized (args) {
                        args.notifyAll();
                    }
                } catch (RuntimeException e) {
                    args.err = e;
                    args.handled = true;
                    synchronized (args) {
                        args.notifyAll();
                    }
                }
                return;
            } catch (Throwable th) {
                args.handled = true;
                synchronized (args) {
                    args.notifyAll();
                    throw th;
                }
            }
        }
        super.dispatchMessage(message);
    }
}
