package com.miui.gallery.util;

import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/* loaded from: classes2.dex */
public final class ProcessLock {
    public static final Map<String, Lock> sThreadLocks = new HashMap();
    public final boolean mFileLevelLock;
    public FileChannel mLockChannel;
    public final File mLockFile;
    public final Lock mThreadLock;

    public ProcessLock(String str, File file, boolean z) {
        File file2 = new File(file, str + ".lck");
        this.mLockFile = file2;
        this.mThreadLock = getThreadLock(file2.getAbsolutePath());
        this.mFileLevelLock = z;
    }

    public void lock() {
        lock(this.mFileLevelLock);
    }

    public void lock(boolean z) {
        this.mThreadLock.lock();
        if (z) {
            try {
                File parentFile = this.mLockFile.getParentFile();
                if (parentFile != null) {
                    parentFile.mkdirs();
                }
                FileChannel channel = new FileOutputStream(this.mLockFile).getChannel();
                this.mLockChannel = channel;
                channel.lock();
            } catch (IOException e) {
                this.mLockChannel = null;
                DefaultLogger.w("ProcessLock", "Unable to grab file lock.", e);
            }
        }
    }

    public void unlock() {
        FileChannel fileChannel = this.mLockChannel;
        if (fileChannel != null) {
            try {
                fileChannel.close();
            } catch (IOException unused) {
            }
        }
        this.mThreadLock.unlock();
    }

    public static Lock getThreadLock(String str) {
        Lock lock;
        Map<String, Lock> map = sThreadLocks;
        synchronized (map) {
            lock = map.get(str);
            if (lock == null) {
                lock = new ReentrantLock();
                map.put(str, lock);
            }
        }
        return lock;
    }
}
