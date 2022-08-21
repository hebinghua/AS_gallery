package com.miui.gallery.util;

import android.os.MessageQueue;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.LinkedList;
import java.util.Queue;

/* loaded from: classes2.dex */
public class IdleUITaskHelper implements MessageQueue.IdleHandler {
    public static volatile IdleUITaskHelper singleton;
    public boolean isAdded = false;
    public final Object mSync = new Object();
    public final Queue<Runnable> mTaskCache = new LinkedList();

    public static IdleUITaskHelper getInstance() {
        if (singleton == null) {
            synchronized (IdleUITaskHelper.class) {
                if (singleton == null) {
                    singleton = new IdleUITaskHelper();
                }
            }
        }
        return singleton;
    }

    public void addTask(Runnable runnable) {
        synchronized (this.mSync) {
            this.mTaskCache.offer(runnable);
            DefaultLogger.i("IdleUITaskHelper", "IdleHandler add task -> " + this.mTaskCache.size());
            if (!this.isAdded) {
                ThreadManager.getMainHandler().getLooper().getQueue().addIdleHandler(this);
                DefaultLogger.i("IdleUITaskHelper", "addIdleHandler -> ");
                this.isAdded = true;
            }
        }
    }

    @Override // android.os.MessageQueue.IdleHandler
    public boolean queueIdle() {
        synchronized (this.mSync) {
            Runnable poll = this.mTaskCache.poll();
            if (poll != null) {
                poll.run();
            }
            DefaultLogger.i("IdleUITaskHelper", "Idle task size -> " + this.mTaskCache.size());
            if (this.mTaskCache.isEmpty()) {
                ThreadManager.getMainHandler().getLooper().getQueue().removeIdleHandler(this);
                DefaultLogger.i("IdleUITaskHelper", "removeIdleHandler -> ");
                this.isAdded = false;
            }
        }
        return true;
    }

    public void clean() {
        DefaultLogger.i("IdleUITaskHelper", "Idle task -> clean");
        synchronized (this.mSync) {
            this.mTaskCache.clear();
            ThreadManager.getMainHandler().getLooper().getQueue().removeIdleHandler(this);
            this.isAdded = false;
        }
    }
}
