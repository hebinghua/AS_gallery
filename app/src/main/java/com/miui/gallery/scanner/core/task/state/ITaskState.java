package com.miui.gallery.scanner.core.task.state;

import com.miui.gallery.scanner.core.ScanContracts$StatusReason;

/* loaded from: classes2.dex */
public interface ITaskState {
    ITaskState gotoAbandoned(ScanContracts$StatusReason scanContracts$StatusReason);

    ITaskState gotoDone(ScanContracts$StatusReason scanContracts$StatusReason);

    ITaskState gotoRetry(ScanContracts$StatusReason scanContracts$StatusReason);

    ITaskState gotoRunning(ScanContracts$StatusReason scanContracts$StatusReason);

    ITaskState gotoSelfDone(ScanContracts$StatusReason scanContracts$StatusReason);

    ITaskState gotoWaiting(ScanContracts$StatusReason scanContracts$StatusReason);
}
