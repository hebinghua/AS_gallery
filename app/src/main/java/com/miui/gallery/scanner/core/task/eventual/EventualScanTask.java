package com.miui.gallery.scanner.core.task.eventual;

import android.content.Context;
import com.miui.gallery.scanner.core.ScanContracts$ScanResultReason;
import com.miui.gallery.scanner.core.ScanContracts$StatusReason;
import com.miui.gallery.scanner.core.task.BaseScanTask;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.eventual.ScanResult;
import com.miui.gallery.scanner.core.task.eventual.scansinglefile.ScanSingleFileTask;
import java.nio.file.Path;
import java.util.Objects;

/* loaded from: classes2.dex */
public abstract class EventualScanTask extends BaseScanTask<EventualScanTask, ScanResult> {
    public final int mHashCode;
    public final Path mPath;
    public ScanResult mScanResult;

    public EventualScanTask(Context context, ScanTaskConfig scanTaskConfig, Path path) {
        super(context, scanTaskConfig);
        this.mHashCode = Objects.hash(getClass().getSimpleName(), path);
        this.mPath = path;
    }

    public void mergePriority(long j) {
        this.mConfig = new ScanTaskConfig.Builder().cloneFrom(this.mConfig).setPriority(j | this.mConfig.getPriority()).build();
    }

    public void demote() {
        long priority = getPriority() >> 1;
        ScanTaskConfig.Builder cloneFrom = new ScanTaskConfig.Builder().cloneFrom(this.mConfig);
        if (priority == 0) {
            priority = getPriority();
        }
        this.mConfig = cloneFrom.setPriority(priority).build();
    }

    public Path getPath() {
        return this.mPath;
    }

    public ScanResult getScanResult() {
        return this.mScanResult;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    /* renamed from: genDefaultScanResult */
    public ScanResult mo1305genDefaultScanResult() {
        return ScanResult.failed(ScanContracts$ScanResultReason.DEFAULT).build();
    }

    /* renamed from: com.miui.gallery.scanner.core.task.eventual.EventualScanTask$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$scanner$core$task$eventual$ScanResult$Result;

        static {
            int[] iArr = new int[ScanResult.Result.values().length];
            $SwitchMap$com$miui$gallery$scanner$core$task$eventual$ScanResult$Result = iArr;
            try {
                iArr[ScanResult.Result.RETRY.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$scanner$core$task$eventual$ScanResult$Result[ScanResult.Result.FAILED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$scanner$core$task$eventual$ScanResult$Result[ScanResult.Result.SUCCESS.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public void dealWithResult(ScanResult scanResult) {
        this.mScanResult = scanResult;
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$scanner$core$task$eventual$ScanResult$Result[scanResult.getResult().ordinal()];
        if (i == 1) {
            gotoRetry(ScanContracts$StatusReason.DEFAULT);
        } else if (i == 2) {
            gotoAbandoned(ScanContracts$StatusReason.SELF_FAILED);
        } else {
            super.dealWithResult((EventualScanTask) scanResult);
        }
    }

    public String toString() {
        return String.format("---%s %s", getClass().getSimpleName(), this.mPath);
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public boolean equals(Object obj) {
        if (obj instanceof EventualScanTask) {
            EventualScanTask eventualScanTask = (EventualScanTask) obj;
            if (this.mHashCode == eventualScanTask.mHashCode && Objects.equals(eventualScanTask.mPath, this.mPath)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public int hashCode() {
        return this.mHashCode;
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask, java.lang.Comparable
    public int compareTo(EventualScanTask eventualScanTask) {
        boolean z = this instanceof ScanSingleFileTask;
        boolean z2 = eventualScanTask instanceof ScanSingleFileTask;
        if (!z || z2) {
            if (z2 && !z) {
                return -1;
            }
            return super.compareTo(eventualScanTask);
        }
        return 1;
    }
}
