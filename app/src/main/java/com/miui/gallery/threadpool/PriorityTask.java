package com.miui.gallery.threadpool;

import com.miui.gallery.concurrent.ThreadPool;

/* loaded from: classes2.dex */
public interface PriorityTask<TASK, RESULT> extends Comparable<TASK>, ThreadPool.Job<RESULT> {
}
