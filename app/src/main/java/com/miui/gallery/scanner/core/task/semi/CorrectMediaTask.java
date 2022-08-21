package com.miui.gallery.scanner.core.task.semi;

import android.content.Context;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.convertor.IScanTaskConverter;
import com.miui.gallery.scanner.core.task.eventual.EventualCorrectMediaTask;
import com.miui.gallery.scanner.core.task.eventual.EventualScanTask;
import com.miui.gallery.scanner.core.task.semi.CorrectMediaTask;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import java.util.List;

/* loaded from: classes2.dex */
public class CorrectMediaTask extends SemiScanTask {
    public CorrectMediaTask(Context context, ScanTaskConfig scanTaskConfig) {
        super(context, scanTaskConfig);
        this.mSemiScanTaskConverter = new TaskConverter(this.mContext, this);
    }

    @Override // com.miui.gallery.scanner.core.task.BaseScanTask
    public boolean equals(Object obj) {
        return obj instanceof CorrectMediaTask;
    }

    /* loaded from: classes2.dex */
    public static class TaskConverter implements IScanTaskConverter<EventualScanTask> {
        public final Context mContext;
        public final CorrectMediaTask mTask;

        public static /* synthetic */ void $r8$lambda$BR43ieF3sNeY5AqXYRmzIYGedCc(TaskConverter taskConverter, ObservableEmitter observableEmitter) {
            taskConverter.lambda$convertFlow$0(observableEmitter);
        }

        public TaskConverter(Context context, CorrectMediaTask correctMediaTask) {
            this.mContext = context;
            this.mTask = correctMediaTask;
        }

        public /* synthetic */ void lambda$convertFlow$0(ObservableEmitter observableEmitter) throws Exception {
            convertToEventualScanTask(this.mContext, this.mTask.getConfig(), observableEmitter);
        }

        @Override // com.miui.gallery.scanner.core.task.convertor.IScanTaskConverter
        public Observable<EventualScanTask> convertFlow(List<Throwable> list) {
            return Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.scanner.core.task.semi.CorrectMediaTask$TaskConverter$$ExternalSyntheticLambda0
                @Override // io.reactivex.ObservableOnSubscribe
                public final void subscribe(ObservableEmitter observableEmitter) {
                    CorrectMediaTask.TaskConverter.$r8$lambda$BR43ieF3sNeY5AqXYRmzIYGedCc(CorrectMediaTask.TaskConverter.this, observableEmitter);
                }
            });
        }

        public final void convertToEventualScanTask(Context context, ScanTaskConfig scanTaskConfig, ObservableEmitter<EventualScanTask> observableEmitter) {
            observableEmitter.onNext(new EventualCorrectMediaTask(context, scanTaskConfig));
        }
    }
}
