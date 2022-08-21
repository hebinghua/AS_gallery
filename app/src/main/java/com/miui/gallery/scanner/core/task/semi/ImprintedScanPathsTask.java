package com.miui.gallery.scanner.core.task.semi;

import android.content.Context;
import com.miui.gallery.scanner.core.ScanContracts$ScanResultReason;
import com.miui.gallery.scanner.core.ScanContracts$StatusReason;
import com.miui.gallery.scanner.core.task.BaseScanTask;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.eventual.EventualScanTask;
import com.miui.gallery.scanner.core.task.eventual.ScanResult;
import com.miui.gallery.scanner.core.task.state.TaskStateEnum;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes2.dex */
public class ImprintedScanPathsTask extends ScanPathsTask {
    public static final DecimalFormat FORMAT = new DecimalFormat("00.00");
    public final AtomicInteger mAbandoned;
    public final AtomicInteger mDone;
    public final AtomicInteger[] mScanResultReason;
    public final AtomicInteger[] mStateReason;
    public final String mTag;
    public final AtomicInteger mTotal;

    public static ScanPathsTask create(Context context, List<String> list, ScanTaskConfig scanTaskConfig, String str) {
        if (!BaseMiscUtil.isValid(list)) {
            return null;
        }
        return new ImprintedScanPathsTask(context, list, scanTaskConfig, str);
    }

    public ImprintedScanPathsTask(Context context, List<String> list, ScanTaskConfig scanTaskConfig, String str) {
        super(context, list, scanTaskConfig);
        this.mTotal = new AtomicInteger();
        this.mDone = new AtomicInteger();
        this.mAbandoned = new AtomicInteger();
        this.mScanResultReason = new AtomicInteger[ScanContracts$ScanResultReason.values().length];
        this.mStateReason = new AtomicInteger[ScanContracts$StatusReason.values().length];
        this.mTag = str;
        int i = 0;
        int i2 = 0;
        while (true) {
            AtomicInteger[] atomicIntegerArr = this.mScanResultReason;
            if (i2 >= atomicIntegerArr.length) {
                break;
            }
            atomicIntegerArr[i2] = new AtomicInteger();
            i2++;
        }
        while (true) {
            AtomicInteger[] atomicIntegerArr2 = this.mStateReason;
            if (i < atomicIntegerArr2.length) {
                atomicIntegerArr2[i] = new AtomicInteger();
                i++;
            } else {
                return;
            }
        }
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public void onChildNotified(BaseScanTask baseScanTask) {
        super.onChildNotified(baseScanTask);
        this.mTotal.incrementAndGet();
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$scanner$core$task$state$TaskStateEnum[((TaskStateEnum) baseScanTask.getState()).ordinal()];
        if (i == 1) {
            this.mDone.incrementAndGet();
        } else if (i == 2) {
            this.mAbandoned.incrementAndGet();
        }
        ScanResult scanResult = ((EventualScanTask) baseScanTask).getScanResult();
        if (scanResult != null) {
            this.mScanResultReason[scanResult.getReasonCode().ordinal()].incrementAndGet();
        }
        this.mStateReason[baseScanTask.getStateReason().ordinal()].incrementAndGet();
    }

    /* renamed from: com.miui.gallery.scanner.core.task.semi.ImprintedScanPathsTask$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$scanner$core$task$state$TaskStateEnum;

        static {
            int[] iArr = new int[TaskStateEnum.values().length];
            $SwitchMap$com$miui$gallery$scanner$core$task$state$TaskStateEnum = iArr;
            try {
                iArr[TaskStateEnum.DONE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$scanner$core$task$state$TaskStateEnum[TaskStateEnum.ABANDONED.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public void printTaskLifeRecord() {
        int i;
        int i2;
        StringBuilder sb = new StringBuilder();
        Locale locale = Locale.US;
        sb.append(String.format(locale, " \nTask Life Record Msg:\nState: [%s]\nName: [%s]\nCreate time: [%d]\nWaiting cost: [%d] ms\nSelf cost: [%d] ms\nExtra work/Waiting children cost: [%d] ms", getState().toString(), this, Long.valueOf(this.mCreateTime), Long.valueOf(this.mStartTime - this.mCreateTime), Long.valueOf(this.mSelfDoneTime - this.mStartTime), Long.valueOf(this.mDoneTime - this.mSelfDoneTime)));
        sb.append(String.format(locale, "\nConfig scene code: [%d]", Integer.valueOf(getConfig().getSceneCode())));
        sb.append(String.format(locale, "\nChildren state: total [%d], done [%d], abandoned [%d]", Integer.valueOf(this.mTotal.get()), Integer.valueOf(this.mDone.get()), Integer.valueOf(this.mAbandoned.get())));
        int sum = Arrays.stream(this.mStateReason).mapToInt(ImprintedScanPathsTask$$ExternalSyntheticLambda0.INSTANCE).sum();
        int i3 = 0;
        while (true) {
            AtomicInteger[] atomicIntegerArr = this.mStateReason;
            if (i3 >= atomicIntegerArr.length) {
                break;
            }
            if (atomicIntegerArr[i3].get() > 0) {
                sb.append(String.format(Locale.US, "\n\tState reason [%s], percent [%s]%%", ScanContracts$StatusReason.values()[i3], FORMAT.format((i2 * 100.0f) / sum)));
            }
            i3++;
        }
        int sum2 = Arrays.stream(this.mScanResultReason).mapToInt(ImprintedScanPathsTask$$ExternalSyntheticLambda0.INSTANCE).sum();
        int i4 = 0;
        while (true) {
            AtomicInteger[] atomicIntegerArr2 = this.mScanResultReason;
            if (i4 < atomicIntegerArr2.length) {
                if (atomicIntegerArr2[i4].get() > 0) {
                    sb.append(String.format(Locale.US, "\n\tScan result reason [%s], percent [%s]%%", ScanContracts$ScanResultReason.values()[i4], FORMAT.format((i * 100.0f) / sum2)));
                }
                i4++;
            } else {
                DefaultLogger.fd("Imprinted_" + this.mTag, sb.toString());
                return;
            }
        }
    }
}
