package com.miui.gallery.editor.photo.app.sky;

import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/* loaded from: classes2.dex */
public class SeqExecutor {
    public List<TypeFuture> mFutureList = new LinkedList();
    public ExecutorService mExecutor = Executors.newSingleThreadExecutor();
    public final Object mLock = new Object();

    /* loaded from: classes2.dex */
    public static class TypeFuture<T> {
        public Future<T> future;
        public int type;

        public TypeFuture(int i, Future<T> future) {
            this.type = i;
            this.future = future;
        }
    }

    public <T> T runExclusive(int i, Callable<T> callable, int... iArr) {
        TypeFuture<T> submitExclusiveInner = submitExclusiveInner(i, callable, iArr);
        T t = null;
        boolean z = false;
        try {
            t = submitExclusiveInner.future.get();
        } catch (InterruptedException unused) {
            z = true;
            DefaultLogger.d("SeqExecutor", "interrupt task");
        } catch (CancellationException unused2) {
            DefaultLogger.d("SeqExecutor", "cancel task");
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        if (!z) {
            synchronized (this.mLock) {
                this.mFutureList.remove(submitExclusiveInner);
            }
        }
        return t;
    }

    public Future submitExclusive(int i, Runnable runnable, int... iArr) {
        return submitExclusive(i, Executors.callable(runnable), iArr);
    }

    public <T> Future<T> submitExclusive(int i, Callable<T> callable, int... iArr) {
        return submitExclusiveInner(i, callable, iArr).future;
    }

    public final <T> TypeFuture<T> submitExclusiveInner(int i, Callable<T> callable, int... iArr) {
        TypeFuture<T> typeFuture;
        synchronized (this.mLock) {
            if (iArr.length > 0) {
                if (iArr[0] == -1) {
                    for (TypeFuture typeFuture2 : this.mFutureList) {
                        typeFuture2.future.cancel(false);
                        DefaultLogger.d("SeqExecutor", "cancel ", typeFuture2.toString());
                    }
                    this.mFutureList.clear();
                } else {
                    Iterator<TypeFuture> it = this.mFutureList.iterator();
                    while (it.hasNext()) {
                        TypeFuture next = it.next();
                        int length = iArr.length;
                        int i2 = 0;
                        while (true) {
                            if (i2 < length) {
                                if (next.type == iArr[i2]) {
                                    next.future.cancel(false);
                                    DefaultLogger.d("SeqExecutor", "cancel ", next.toString());
                                    it.remove();
                                    break;
                                }
                                i2++;
                            }
                        }
                    }
                }
            }
            typeFuture = new TypeFuture<>(i, this.mExecutor.submit(callable));
            this.mFutureList.add(typeFuture);
        }
        return typeFuture;
    }
}
