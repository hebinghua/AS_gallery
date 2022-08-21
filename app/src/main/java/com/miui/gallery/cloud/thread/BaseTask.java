package com.miui.gallery.cloud.thread;

import android.accounts.Account;
import android.content.Context;
import android.os.SystemClock;
import com.google.common.collect.Lists;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.cloud.RequestCloudItem;
import com.miui.gallery.cloud.SyncConditionManager;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.cloud.base.GallerySyncResult;
import com.miui.gallery.cloud.thread.RequestCommandQueue;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes.dex */
public abstract class BaseTask<T> implements Runnable {
    public final String TAG;
    public final RequestCommandQueue mCommandQueue;
    public volatile boolean isRunning = false;
    public AtomicBoolean mHasInterruptted = new AtomicBoolean(false);

    public abstract GallerySyncResult<T> handle(Context context, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken, List<RequestCloudItem> list) throws Exception;

    public abstract void onPostExecute();

    public abstract void onPreExecute();

    public int resumeInterruptted() {
        return 0;
    }

    public BaseTask(int i, int i2, int i3, int i4, RequestCommandQueue.OnItemChangedListener onItemChangedListener) {
        String format = String.format(Locale.US, "%s#%s", getClass().getSimpleName(), Integer.valueOf(i));
        this.TAG = format;
        this.mCommandQueue = new RequestCommandQueue(i2, i3, i4, onItemChangedListener, format);
    }

    @Override // java.lang.Runnable
    public void run() {
        GallerySyncResult<T> onPerformSync;
        DefaultLogger.i(this.TAG, "thread start %s", Integer.valueOf(System.identityHashCode(Thread.currentThread())));
        this.isRunning = true;
        onPreExecute();
        do {
            try {
                if (Thread.currentThread().isInterrupted() || (onPerformSync = onPerformSync()) == null) {
                    break;
                }
            } finally {
                onPostExecute();
                DefaultLogger.i(this.TAG, "thread end %s", Integer.valueOf(System.identityHashCode(Thread.currentThread())));
                this.isRunning = false;
            }
        } while (needContinue(onPerformSync));
    }

    public GallerySyncResult<T> onPerformSync() throws Throwable {
        ArrayList<RequestCommand> newArrayList = Lists.newArrayList();
        ArrayList<RequestCloudItem> newArrayList2 = Lists.newArrayList();
        GallerySyncResult.Builder builder = new GallerySyncResult.Builder();
        try {
            try {
                long pollToExecute = this.mCommandQueue.pollToExecute(newArrayList);
                if (newArrayList.isEmpty()) {
                    if (pollToExecute > 0) {
                        synchronized (Thread.currentThread()) {
                            try {
                                String str = this.TAG;
                                DefaultLogger.i(str, "wait for " + pollToExecute);
                                Thread.currentThread().wait(pollToExecute);
                                DefaultLogger.i(this.TAG, "wait time out or notified");
                            } catch (InterruptedException unused) {
                                DefaultLogger.i(this.TAG, "resume from waiting by interuptted");
                            }
                        }
                    }
                    GallerySyncResult<T> build = builder.setCode(GallerySyncCode.OK).build();
                    for (RequestCloudItem requestCloudItem : newArrayList2) {
                        this.mCommandQueue.removeFromExecuting(RequestCommand.getKey(requestCloudItem));
                    }
                    for (RequestCommand requestCommand : newArrayList) {
                        DefaultLogger.d(this.TAG, "execute: %s, invoke~finish cost=%s", requestCommand.getKey(), Long.valueOf(SystemClock.uptimeMillis() - requestCommand.mInvokeTime));
                    }
                    return build;
                }
                for (RequestCommand requestCommand2 : newArrayList) {
                    newArrayList2.add(requestCommand2.mRequestItem);
                }
                RequestCommand requestCommand3 = (RequestCommand) newArrayList.get(0);
                if (SyncConditionManager.checkForItem(requestCommand3.mRequestItem) == 2) {
                    GallerySyncResult<T> build2 = builder.setCode(GallerySyncCode.CONDITION_INTERRUPTED).build();
                    for (RequestCloudItem requestCloudItem2 : newArrayList2) {
                        this.mCommandQueue.removeFromExecuting(RequestCommand.getKey(requestCloudItem2));
                    }
                    for (RequestCommand requestCommand4 : newArrayList) {
                        DefaultLogger.d(this.TAG, "execute: %s, invoke~finish cost=%s", requestCommand4.getKey(), Long.valueOf(SystemClock.uptimeMillis() - requestCommand4.mInvokeTime));
                    }
                    return build2;
                }
                Account account = requestCommand3.mAccount;
                AccountCache.AccountInfo accountInfo = AccountCache.getAccountInfo();
                if (accountInfo != null && accountInfo.mAccount.equals(account)) {
                    GallerySyncResult<T> handle = handle(GalleryApp.sGetAndroidContext(), accountInfo.mAccount, accountInfo.mToken, newArrayList2);
                    for (RequestCloudItem requestCloudItem3 : newArrayList2) {
                        this.mCommandQueue.removeFromExecuting(RequestCommand.getKey(requestCloudItem3));
                    }
                    for (RequestCommand requestCommand5 : newArrayList) {
                        DefaultLogger.d(this.TAG, "execute: %s, invoke~finish cost=%s", requestCommand5.getKey(), Long.valueOf(SystemClock.uptimeMillis() - requestCommand5.mInvokeTime));
                    }
                    return handle;
                }
                if (accountInfo != null) {
                    String str2 = this.TAG;
                    DefaultLogger.e(str2, "execute: account is changed. old=" + account + ", current=" + accountInfo.mAccount);
                } else {
                    DefaultLogger.e(this.TAG, "execute: account is null");
                }
                for (RequestCloudItem requestCloudItem4 : newArrayList2) {
                    this.mCommandQueue.removeFromExecuting(RequestCommand.getKey(requestCloudItem4));
                }
                for (RequestCommand requestCommand6 : newArrayList) {
                    DefaultLogger.d(this.TAG, "execute: %s, invoke~finish cost=%s", requestCommand6.getKey(), Long.valueOf(SystemClock.uptimeMillis() - requestCommand6.mInvokeTime));
                }
                return builder.setCode(GallerySyncCode.UNKNOWN).build();
            } catch (InterruptedException e) {
                DefaultLogger.d(this.TAG, "interruptted", e);
                GallerySyncResult<T> build3 = builder.setCode(GallerySyncCode.CANCEL).setException(e).build();
                for (RequestCloudItem requestCloudItem5 : newArrayList2) {
                    this.mCommandQueue.removeFromExecuting(RequestCommand.getKey(requestCloudItem5));
                }
                for (RequestCommand requestCommand7 : newArrayList) {
                    DefaultLogger.d(this.TAG, "execute: %s, invoke~finish cost=%s", requestCommand7.getKey(), Long.valueOf(SystemClock.uptimeMillis() - requestCommand7.mInvokeTime));
                }
                return build3;
            }
        } catch (Throwable th) {
            for (RequestCloudItem requestCloudItem6 : newArrayList2) {
                this.mCommandQueue.removeFromExecuting(RequestCommand.getKey(requestCloudItem6));
            }
            for (RequestCommand requestCommand8 : newArrayList) {
                DefaultLogger.d(this.TAG, "execute: %s, invoke~finish cost=%s", requestCommand8.getKey(), Long.valueOf(SystemClock.uptimeMillis() - requestCommand8.mInvokeTime));
            }
            throw th;
        }
    }

    public boolean needContinue(GallerySyncResult<T> gallerySyncResult) {
        if (Thread.currentThread().isInterrupted()) {
            return false;
        }
        GallerySyncCode gallerySyncCode = gallerySyncResult.code;
        if (gallerySyncCode == GallerySyncCode.CONDITION_INTERRUPTED || gallerySyncCode == GallerySyncCode.NOT_CONTINUE_ERROR || gallerySyncCode == GallerySyncCode.CANCEL) {
            this.mCommandQueue.cancelAll(true);
            return false;
        } else if (this.mCommandQueue.getPengdingSize() > 0) {
            return true;
        } else {
            return this.mHasInterruptted.compareAndSet(true, false) && resumeInterruptted() > 0;
        }
    }

    public int[] invoke(List<RequestCloudItem> list, boolean z, boolean z2) {
        int i;
        int i2;
        Account account = AccountCache.getAccount();
        if (account == null) {
            DefaultLogger.e(this.TAG, "invoke: no account");
            return new int[]{0, 0};
        }
        if (!list.isEmpty()) {
            ArrayList newArrayList = Lists.newArrayList();
            for (RequestCloudItem requestCloudItem : list) {
                newArrayList.add(new RequestCommand(account, requestCloudItem));
            }
            i = this.mCommandQueue.put(newArrayList, z2);
            if (z) {
                List<RequestCommand> interruptIfNotExecuting = this.mCommandQueue.interruptIfNotExecuting(newArrayList);
                i2 = interruptIfNotExecuting != null ? interruptIfNotExecuting.size() : 0;
                this.mHasInterruptted.compareAndSet(false, i2 > 0);
            } else {
                i2 = 0;
            }
        } else {
            i = 0;
            i2 = 0;
        }
        return new int[]{i, i2};
    }

    public void cancelAll(int i, boolean z) {
        this.mCommandQueue.cancelAll(i, z);
    }

    public int getPendingSize() {
        return this.mCommandQueue.getPengdingSize();
    }

    public boolean hasDelayedItem() {
        return this.mCommandQueue.hasDelayedItem();
    }
}
