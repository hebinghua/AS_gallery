package com.miui.gallery.cloud.control;

import android.text.TextUtils;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.util.SyncLogger;
import java.util.LinkedList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/* loaded from: classes.dex */
public class ServerTagCache {
    public static final int MAX_CAPACITY = CloudControlStrategyHelper.getSyncStrategy().getMaxTempOperationServerTagCapacity();
    public final ReadWriteLock mLock;
    public final LinkedList<String> mPushTags;

    /* loaded from: classes.dex */
    public static class Singleton {
        public static final ServerTagCache SINSTANCE = new ServerTagCache();
    }

    public ServerTagCache() {
        this.mPushTags = new LinkedList<>();
        this.mLock = new ReentrantReadWriteLock();
    }

    public static ServerTagCache getInstance() {
        return Singleton.SINSTANCE;
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

    public boolean contains(String str) {
        lock(false);
        try {
            return this.mPushTags.contains(str);
        } finally {
            unlock(false);
        }
    }

    public boolean add(String str) {
        lock(true);
        try {
            if (TextUtils.isEmpty(str)) {
                return false;
            }
            SyncLogger.d("ServerTagCache", "add server tag: %s", str);
            this.mPushTags.addFirst(str);
            trim();
            return true;
        } finally {
            unlock(true);
        }
    }

    public final void trim() {
        while (this.mPushTags.size() > MAX_CAPACITY) {
            this.mPushTags.removeLast();
        }
    }
}
