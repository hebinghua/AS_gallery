package com.miui.gallery.sdk.download.executor;

import com.miui.gallery.sdk.download.assist.DownloadItem;
import com.miui.gallery.sdk.download.executor.queue.Queue;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* loaded from: classes2.dex */
public class DownloadCommandQueue {
    public final int mAllowBatchMax;
    public final Queue<DownloadCommand> mPendings;
    public final String mTag;
    public final ReadWriteLock mLock = new ReentrantReadWriteLock();
    public final Map<String, DownloadCommand> mExecutings = new HashMap();

    public DownloadCommandQueue(int i, int i2, String str) {
        this.mPendings = new Queue<>(i2);
        this.mAllowBatchMax = i;
        this.mTag = str;
    }

    public int getBatchSize() {
        return this.mAllowBatchMax;
    }

    public int getPendingSize() {
        lock(false);
        try {
            return this.mPendings.size();
        } finally {
            unlock(false);
        }
    }

    public int put(DownloadCommand downloadCommand, boolean z) {
        return put(Arrays.asList(downloadCommand), z);
    }

    public int put(List<DownloadCommand> list, boolean z) {
        int putAtLast;
        lock(true);
        try {
            ArrayList arrayList = new ArrayList();
            for (DownloadCommand downloadCommand : list) {
                if (!this.mExecutings.containsKey(downloadCommand.getKey())) {
                    arrayList.add(downloadCommand);
                }
            }
            if (z) {
                putAtLast = this.mPendings.putAtFirst(arrayList);
            } else {
                putAtLast = this.mPendings.putAtLast(arrayList);
            }
            return putAtLast;
        } finally {
            unlock(true);
        }
    }

    public DownloadCommand removeFromExecuting(String str) {
        lock(true);
        try {
            return this.mExecutings.remove(str);
        } finally {
            unlock(true);
        }
    }

    public DownloadCommand remove(String str) {
        lock(true);
        try {
            DownloadCommand remove = this.mExecutings.remove(str);
            if (remove == null) {
                remove = this.mPendings.remove(str);
            }
            return remove;
        } finally {
            unlock(true);
        }
    }

    public List<DownloadCommand> pollToExecute() {
        lock(true);
        try {
            List<DownloadCommand> poll = this.mPendings.poll(this.mAllowBatchMax);
            if (poll != null) {
                for (DownloadCommand downloadCommand : poll) {
                    this.mExecutings.put(downloadCommand.getKey(), downloadCommand);
                }
            }
            DefaultLogger.d(this.mTag, "pollToExecute: remove count=%d, remain count=%d", Integer.valueOf(poll.size()), Integer.valueOf(this.mPendings.size()));
            return poll;
        } finally {
            unlock(true);
        }
    }

    public int cancel(String str) {
        lock(true);
        int i = -1;
        try {
            DownloadCommand remove = this.mPendings.remove(str);
            if (remove != null) {
                i = 0;
            } else {
                remove = this.mExecutings.remove(str);
                if (remove != null) {
                    i = 1;
                }
            }
            if (remove != null && remove.getItem().compareAnsSetStatus(0, 1)) {
                DownloadItem.callbackCancel(remove.getItem());
            }
            return i;
        } finally {
            unlock(true);
        }
    }

    public int contains(String str) {
        lock(false);
        try {
            if (this.mPendings.get(str) != null) {
                return 0;
            }
            return this.mExecutings.get(str) != null ? 1 : -1;
        } finally {
            unlock(false);
        }
    }

    public DownloadCommand get(String str) {
        lock(false);
        try {
            DownloadCommand downloadCommand = this.mExecutings.get(str);
            if (downloadCommand != null) {
                return downloadCommand;
            }
            DownloadCommand downloadCommand2 = this.mPendings.get(str);
            if (downloadCommand2 != null) {
                return downloadCommand2;
            }
            return null;
        } finally {
            unlock(false);
        }
    }

    public List<DownloadCommand> cancelAll() {
        lock(true);
        try {
            ArrayList<DownloadCommand> arrayList = new ArrayList();
            Collection<DownloadCommand> values = this.mExecutings.values();
            if (values != null) {
                for (DownloadCommand downloadCommand : values) {
                    arrayList.add(downloadCommand);
                }
            }
            this.mExecutings.clear();
            Collection<DownloadCommand> values2 = this.mPendings.values();
            if (values2 != null) {
                for (DownloadCommand downloadCommand2 : values2) {
                    arrayList.add(downloadCommand2);
                }
            }
            this.mPendings.clear();
            for (DownloadCommand downloadCommand3 : arrayList) {
                if (downloadCommand3.getItem().compareAnsSetStatus(0, 1)) {
                    DownloadItem.callbackCancel(downloadCommand3.getItem());
                }
            }
            return arrayList;
        } finally {
            unlock(true);
        }
    }

    public List<DownloadCommand> interrupt() {
        lock(true);
        try {
            LinkedList<DownloadCommand> linkedList = new LinkedList();
            for (DownloadCommand downloadCommand : this.mExecutings.values()) {
                if (downloadCommand.getItem().compareAnsSetStatus(0, 2)) {
                    linkedList.add(downloadCommand);
                }
            }
            this.mExecutings.clear();
            for (DownloadCommand downloadCommand2 : this.mPendings.values()) {
                if (downloadCommand2.getItem().compareAnsSetStatus(0, 2)) {
                    linkedList.add(downloadCommand2);
                }
            }
            this.mPendings.clear();
            LinkedList linkedList2 = new LinkedList();
            for (DownloadCommand downloadCommand3 : linkedList) {
                linkedList2.add(new DownloadCommand(downloadCommand3));
            }
            this.mPendings.putAtLast(linkedList2);
            return linkedList;
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
}
