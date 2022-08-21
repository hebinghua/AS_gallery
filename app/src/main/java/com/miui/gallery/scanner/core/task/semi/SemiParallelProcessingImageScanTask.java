package com.miui.gallery.scanner.core.task.semi;

import android.content.Context;
import com.miui.gallery.scanner.core.scanner.ParallelProcessingImageScanner;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.convertor.IScanTaskConverter;
import com.miui.gallery.scanner.core.task.eventual.EventualParallelProcessingImageScanTask;
import com.miui.gallery.scanner.core.task.eventual.EventualScanTask;
import com.miui.gallery.scanner.core.task.semi.SemiParallelProcessingImageScanTask;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

/* loaded from: classes2.dex */
public class SemiParallelProcessingImageScanTask extends SemiScanTask {
    public final String mPath;

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public boolean equals(Object obj) {
        return this == obj;
    }

    public SemiParallelProcessingImageScanTask(Context context, String str, ScanTaskConfig scanTaskConfig) {
        super(context, scanTaskConfig);
        this.mPath = str;
        this.mSemiScanTaskConverter = new SemiParallelProcessingImageScanTaskConverter(context, this, str, scanTaskConfig);
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public int hashCode() {
        return ((527 + Objects.hash(this.mPath)) * 31) + super.hashCode();
    }

    public String toString() {
        return String.format("--%s %s", getClass().getSimpleName(), this.mPath);
    }

    /* loaded from: classes2.dex */
    public static class SemiParallelProcessingImageScanTaskConverter implements IScanTaskConverter<EventualScanTask> {
        public final ScanTaskConfig mConfig;
        public final Context mContext;
        public final String mPath;
        public final SemiScanTask mSemiTask;

        /* renamed from: $r8$lambda$VLTSPLyHHFZla-nIx3kBL478RLI */
        public static /* synthetic */ void m1303$r8$lambda$VLTSPLyHHFZlanIx3kBL478RLI(SemiParallelProcessingImageScanTaskConverter semiParallelProcessingImageScanTaskConverter, ObservableEmitter observableEmitter) {
            semiParallelProcessingImageScanTaskConverter.lambda$convertFlow$0(observableEmitter);
        }

        public SemiParallelProcessingImageScanTaskConverter(Context context, SemiScanTask semiScanTask, String str, ScanTaskConfig scanTaskConfig) {
            this.mContext = context;
            this.mSemiTask = semiScanTask;
            this.mPath = str;
            this.mConfig = scanTaskConfig;
        }

        public /* synthetic */ void lambda$convertFlow$0(ObservableEmitter observableEmitter) throws Exception {
            convertToEventualScanTask(this.mContext, this.mSemiTask, this.mPath, this.mConfig, observableEmitter);
        }

        @Override // com.miui.gallery.scanner.core.task.convertor.IScanTaskConverter
        public Observable<EventualScanTask> convertFlow(List<Throwable> list) {
            return Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.scanner.core.task.semi.SemiParallelProcessingImageScanTask$SemiParallelProcessingImageScanTaskConverter$$ExternalSyntheticLambda0
                @Override // io.reactivex.ObservableOnSubscribe
                public final void subscribe(ObservableEmitter observableEmitter) {
                    SemiParallelProcessingImageScanTask.SemiParallelProcessingImageScanTaskConverter.m1303$r8$lambda$VLTSPLyHHFZlanIx3kBL478RLI(SemiParallelProcessingImageScanTask.SemiParallelProcessingImageScanTaskConverter.this, observableEmitter);
                }
            });
        }

        public final void convertToEventualScanTask(Context context, SemiScanTask semiScanTask, String str, ScanTaskConfig scanTaskConfig, ObservableEmitter<EventualScanTask> observableEmitter) {
            EventualParallelProcessingImageScanTask eventualParallelProcessingImageScanTask = new EventualParallelProcessingImageScanTask(context, Paths.get(str, new String[0]), new ScanTaskConfig.Builder().cloneFrom(scanTaskConfig).setScanner(new ParallelProcessingImageScanner()).build());
            eventualParallelProcessingImageScanTask.setParentTask(semiScanTask);
            observableEmitter.onNext(eventualParallelProcessingImageScanTask);
        }
    }
}
