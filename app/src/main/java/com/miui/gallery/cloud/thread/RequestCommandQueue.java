package com.miui.gallery.cloud.thread;

import android.os.SystemClock;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.miui.gallery.cloud.RequestCloudItem;
import com.miui.gallery.cloud.SyncConditionManager;
import com.miui.gallery.cloud.UpDownloadManager;
import com.miui.gallery.cloud.thread.CommandQueue;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* loaded from: classes.dex */
public class RequestCommandQueue {
    public final int mAllowBatchMax;
    public final OnItemChangedListener mListener;
    public final CommandQueue<RequestCommand> mPendings;
    public final String mTag;
    public final ReadWriteLock mLock = new ReentrantReadWriteLock();
    public final Map<String, RequestCommand> mExecutings = Maps.newHashMap();

    /* loaded from: classes.dex */
    public interface OnItemChangedListener {
        void onAddItem(RequestCloudItem requestCloudItem);

        void onRemoveItem(RequestCloudItem requestCloudItem);
    }

    public RequestCommandQueue(int i, int i2, int i3, OnItemChangedListener onItemChangedListener, String str) {
        this.mPendings = new CommandQueue<>(i, i3);
        this.mAllowBatchMax = i2;
        this.mListener = onItemChangedListener;
        this.mTag = str;
    }

    public int getPengdingSize() {
        lock(false);
        try {
            return this.mPendings.size();
        } finally {
            unlock(false);
        }
    }

    public int put(List<RequestCommand> list, boolean z) {
        int putAtLast;
        lock(true);
        try {
            long uptimeMillis = SystemClock.uptimeMillis();
            ArrayList newArrayList = Lists.newArrayList();
            for (RequestCommand requestCommand : list) {
                if (!this.mExecutings.containsKey(requestCommand.getKey())) {
                    newArrayList.add(requestCommand);
                }
            }
            CommandQueue.OnQueueChangedListener<RequestCommand> onQueueChangedListener = new CommandQueue.OnQueueChangedListener<RequestCommand>() { // from class: com.miui.gallery.cloud.thread.RequestCommandQueue.1
                @Override // com.miui.gallery.cloud.thread.CommandQueue.OnQueueChangedListener
                public void onAdd(RequestCommand requestCommand2) {
                    requestCommand2.mRequestItem.setStatus(0);
                    RequestCommandQueue.this.onAddCommand(requestCommand2);
                }

                @Override // com.miui.gallery.cloud.thread.CommandQueue.OnQueueChangedListener
                public void onRemove(RequestCommand requestCommand2) {
                    requestCommand2.mRequestItem.compareAndSetStatus(0, 3);
                    RequestCommandQueue.this.onRemoveCommand(requestCommand2);
                }
            };
            if (z) {
                putAtLast = this.mPendings.putAtFrist(newArrayList, uptimeMillis, onQueueChangedListener);
            } else {
                putAtLast = this.mPendings.putAtLast(newArrayList, uptimeMillis, onQueueChangedListener);
            }
            return putAtLast;
        } finally {
            unlock(true);
        }
    }

    public void removeFromExecuting(String str) {
        lock(true);
        try {
            RequestCommand remove = this.mExecutings.remove(str);
            if (remove != null) {
                onRemoveCommand(remove);
            }
        } finally {
            unlock(true);
        }
    }

    public long pollToExecute(List<RequestCommand> list) {
        long minDelay;
        lock(true);
        try {
            long uptimeMillis = SystemClock.uptimeMillis();
            this.mPendings.poll(list, this.mAllowBatchMax, uptimeMillis);
            if (!list.isEmpty()) {
                minDelay = 0;
                for (RequestCommand requestCommand : list) {
                    this.mExecutings.put(requestCommand.getKey(), requestCommand);
                }
            } else {
                minDelay = this.mPendings.getMinDelay(uptimeMillis);
            }
            String str = this.mTag;
            DefaultLogger.d(str, "pollToExecute: remove count=" + list.size() + ", remain count=" + this.mPendings.size());
            return minDelay;
        } finally {
            unlock(true);
        }
    }

    public void cancelAll(boolean z) {
        for (int i = 0; i < 14; i++) {
            if (SyncConditionManager.check(i) != 0 && UpDownloadManager.getThreadType(i) != -1) {
                cancelAll(i, z);
            }
        }
    }

    public void cancelAll(int i, boolean z) {
        String str = this.mTag;
        DefaultLogger.d(str, "cancelAll: remain count=" + this.mPendings.size() + ", abandon=" + z);
        int i2 = z ? 3 : 1;
        lock(true);
        try {
            for (RequestCommand requestCommand : this.mExecutings.values()) {
                RequestCloudItem requestCloudItem = requestCommand.mRequestItem;
                if (requestCloudItem.priority == i) {
                    requestCloudItem.compareAndSetStatus(0, i2);
                }
            }
            ArrayList<RequestCommand> arrayList = new ArrayList();
            for (RequestCommand requestCommand2 : this.mPendings.values()) {
                RequestCloudItem requestCloudItem2 = requestCommand2.mRequestItem;
                if (requestCloudItem2.priority == i) {
                    requestCloudItem2.compareAndSetStatus(0, i2);
                    onRemoveCommand(requestCommand2);
                    arrayList.add(requestCommand2);
                }
            }
            for (RequestCommand requestCommand3 : arrayList) {
                this.mPendings.remove(requestCommand3.getKey());
            }
        } finally {
            unlock(true);
        }
    }

    public List<RequestCommand> interruptIfNotExecuting(List<RequestCommand> list) {
        String str = this.mTag;
        DefaultLogger.d(str, "interruptExecuting: executing count=" + this.mExecutings.size());
        ArrayList newArrayList = Lists.newArrayList();
        lock(true);
        try {
            for (RequestCommand requestCommand : list) {
                if (this.mExecutings.containsKey(requestCommand.getKey())) {
                    return newArrayList;
                }
            }
            for (RequestCommand requestCommand2 : this.mExecutings.values()) {
                requestCommand2.mRequestItem.compareAndSetStatus(0, 1);
                onRemoveCommand(requestCommand2);
                newArrayList.add(requestCommand2);
            }
            this.mExecutings.clear();
            return newArrayList;
        } finally {
            unlock(true);
        }
    }

    public final void lock(boolean z) {
        if (z) {
            this.mLock.writeLock().lock();
        } else {
            this.mLock.readLock().lock();
        }
    }

    public final void unlock(boolean z) {
        if (z) {
            this.mLock.writeLock().unlock();
        } else {
            this.mLock.readLock().unlock();
        }
    }

    public void onAddCommand(RequestCommand requestCommand) {
        this.mListener.onAddItem(requestCommand.mRequestItem);
    }

    public void onRemoveCommand(RequestCommand requestCommand) {
        this.mListener.onRemoveItem(requestCommand.mRequestItem);
    }

    public boolean hasDelayedItem() {
        lock(false);
        try {
            return this.mPendings.hasDelayedItem();
        } finally {
            unlock(false);
        }
    }
}
