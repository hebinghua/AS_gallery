package com.miui.gallery.scanner.core.task;

import com.miui.gallery.scanner.core.ScanContracts$StatusReason;
import com.miui.gallery.scanner.core.task.BaseScanTask;

/* loaded from: classes2.dex */
public interface BaseScanTaskStateListener<T extends BaseScanTask> {
    default void onAbandoned(T t, ScanContracts$StatusReason scanContracts$StatusReason) {
    }

    default void onDone(T t, ScanContracts$StatusReason scanContracts$StatusReason) {
    }

    default void onRetry(T t, ScanContracts$StatusReason scanContracts$StatusReason) {
    }

    default void onRunning(T t, ScanContracts$StatusReason scanContracts$StatusReason) {
    }

    default void onSelfDone(T t, ScanContracts$StatusReason scanContracts$StatusReason) {
    }

    default void onWaiting(T t, ScanContracts$StatusReason scanContracts$StatusReason) {
    }
}
