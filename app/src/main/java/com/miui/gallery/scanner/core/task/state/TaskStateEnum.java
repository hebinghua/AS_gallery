package com.miui.gallery.scanner.core.task.state;

import com.miui.gallery.scanner.core.ScanContracts$StatusReason;

/* loaded from: classes2.dex */
public enum TaskStateEnum implements ITaskState {
    WAITING { // from class: com.miui.gallery.scanner.core.task.state.TaskStateEnum.1
        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoRunning(ScanContracts$StatusReason scanContracts$StatusReason) {
            return TaskStateEnum.RUNNING;
        }

        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoAbandoned(ScanContracts$StatusReason scanContracts$StatusReason) {
            return TaskStateEnum.ABANDONED;
        }

        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoWaiting(ScanContracts$StatusReason scanContracts$StatusReason) {
            throw new IllegalStateException("cannot goto [waiting] from [waiting].");
        }

        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoSelfDone(ScanContracts$StatusReason scanContracts$StatusReason) {
            throw new IllegalStateException("cannot goto [self_done] from [waiting].");
        }

        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoDone(ScanContracts$StatusReason scanContracts$StatusReason) {
            throw new IllegalStateException("cannot goto [done] from [waiting].");
        }

        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoRetry(ScanContracts$StatusReason scanContracts$StatusReason) {
            throw new IllegalStateException("cannot goto [retry] from [waiting].");
        }
    },
    RUNNING { // from class: com.miui.gallery.scanner.core.task.state.TaskStateEnum.2
        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoRunning(ScanContracts$StatusReason scanContracts$StatusReason) {
            throw new IllegalStateException("cannot goto [running] from [running].");
        }

        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoAbandoned(ScanContracts$StatusReason scanContracts$StatusReason) {
            return TaskStateEnum.ABANDONED;
        }

        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoWaiting(ScanContracts$StatusReason scanContracts$StatusReason) {
            throw new IllegalStateException("cannot goto [running] from [running].");
        }

        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoSelfDone(ScanContracts$StatusReason scanContracts$StatusReason) {
            return TaskStateEnum.SELF_DONE;
        }

        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoDone(ScanContracts$StatusReason scanContracts$StatusReason) {
            throw new IllegalStateException("cannot goto [done] from [running].");
        }

        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoRetry(ScanContracts$StatusReason scanContracts$StatusReason) {
            return TaskStateEnum.RETRY;
        }
    },
    SELF_DONE { // from class: com.miui.gallery.scanner.core.task.state.TaskStateEnum.3
        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoRunning(ScanContracts$StatusReason scanContracts$StatusReason) {
            throw new IllegalStateException("cannot goto [running] from [self_done].");
        }

        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoAbandoned(ScanContracts$StatusReason scanContracts$StatusReason) {
            throw new IllegalStateException("cannot goto [abandoned] from [self_done].");
        }

        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoWaiting(ScanContracts$StatusReason scanContracts$StatusReason) {
            throw new IllegalStateException("cannot goto [waiting] from [self_done].");
        }

        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoSelfDone(ScanContracts$StatusReason scanContracts$StatusReason) {
            throw new IllegalStateException("cannot goto [self_done] from [self_done].");
        }

        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoDone(ScanContracts$StatusReason scanContracts$StatusReason) {
            return TaskStateEnum.DONE;
        }

        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoRetry(ScanContracts$StatusReason scanContracts$StatusReason) {
            throw new IllegalStateException("cannot goto [retry] from [self_done].");
        }
    },
    DONE { // from class: com.miui.gallery.scanner.core.task.state.TaskStateEnum.4
        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoRunning(ScanContracts$StatusReason scanContracts$StatusReason) {
            throw new IllegalStateException("cannot goto [running] from [done].");
        }

        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoAbandoned(ScanContracts$StatusReason scanContracts$StatusReason) {
            throw new IllegalStateException("cannot goto [abandoned] from [done].");
        }

        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoWaiting(ScanContracts$StatusReason scanContracts$StatusReason) {
            throw new IllegalStateException("cannot goto [waiting] from [done].");
        }

        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoSelfDone(ScanContracts$StatusReason scanContracts$StatusReason) {
            throw new IllegalStateException("cannot goto [self_done] from [done].");
        }

        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoDone(ScanContracts$StatusReason scanContracts$StatusReason) {
            throw new IllegalStateException("cannot goto [done] from [done].");
        }

        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoRetry(ScanContracts$StatusReason scanContracts$StatusReason) {
            throw new IllegalStateException("cannot goto [retry] from [done].");
        }
    },
    ABANDONED { // from class: com.miui.gallery.scanner.core.task.state.TaskStateEnum.5
        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoRunning(ScanContracts$StatusReason scanContracts$StatusReason) {
            throw new IllegalStateException("cannot goto [running] from [abandoned].");
        }

        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoAbandoned(ScanContracts$StatusReason scanContracts$StatusReason) {
            throw new IllegalStateException("cannot goto [abandoned] from [abandoned].");
        }

        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoWaiting(ScanContracts$StatusReason scanContracts$StatusReason) {
            throw new IllegalStateException("cannot goto [waiting] from [abandoned].");
        }

        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoSelfDone(ScanContracts$StatusReason scanContracts$StatusReason) {
            throw new IllegalStateException("cannot goto [self_done] from [abandoned].");
        }

        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoDone(ScanContracts$StatusReason scanContracts$StatusReason) {
            throw new IllegalStateException("cannot goto [done] from [abandoned].");
        }

        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoRetry(ScanContracts$StatusReason scanContracts$StatusReason) {
            throw new IllegalStateException("cannot goto [retry] from [abandoned].");
        }
    },
    RETRY { // from class: com.miui.gallery.scanner.core.task.state.TaskStateEnum.6
        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoRunning(ScanContracts$StatusReason scanContracts$StatusReason) {
            throw new IllegalStateException("cannot goto [running] from [retry].");
        }

        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoAbandoned(ScanContracts$StatusReason scanContracts$StatusReason) {
            throw new IllegalStateException("cannot goto [abandoned] from [retry].");
        }

        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoWaiting(ScanContracts$StatusReason scanContracts$StatusReason) {
            return TaskStateEnum.WAITING;
        }

        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoSelfDone(ScanContracts$StatusReason scanContracts$StatusReason) {
            throw new IllegalStateException("cannot goto [self_done] from [retry].");
        }

        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoDone(ScanContracts$StatusReason scanContracts$StatusReason) {
            throw new IllegalStateException("cannot goto [done] from [retry].");
        }

        @Override // com.miui.gallery.scanner.core.task.state.ITaskState
        public ITaskState gotoRetry(ScanContracts$StatusReason scanContracts$StatusReason) {
            throw new IllegalStateException("cannot goto [retry] from [retry].");
        }
    }
}
