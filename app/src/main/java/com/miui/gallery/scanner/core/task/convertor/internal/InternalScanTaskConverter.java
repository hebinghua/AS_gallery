package com.miui.gallery.scanner.core.task.convertor.internal;

import android.content.Context;
import com.miui.gallery.scanner.core.ScanRequest;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.convertor.IScanTaskConverter;
import com.miui.gallery.scanner.core.task.convertor.internal.base.AbsClassicalScanStrategy;
import com.miui.gallery.scanner.core.task.convertor.internal.base.TaskEmitter;
import com.miui.gallery.scanner.core.task.convertor.internal.legacy.LegacyClassicalScanStrategy;
import com.miui.gallery.scanner.core.task.convertor.internal.modern.ModernClassicalScanStrategy;
import com.miui.gallery.scanner.core.task.raw.InternalScanTask;
import com.miui.gallery.scanner.core.task.semi.CancelRunningTask;
import com.miui.gallery.scanner.core.task.semi.CorrectMediaTask;
import com.miui.gallery.scanner.core.task.semi.ScanPathsTask;
import com.miui.gallery.scanner.core.task.semi.SemiScanTask;
import com.miui.gallery.util.BaseMiscUtil;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import java.util.Collections;
import java.util.List;

/* loaded from: classes2.dex */
public class InternalScanTaskConverter implements IScanTaskConverter<SemiScanTask> {
    public final AbsClassicalScanStrategy mClassicalScanStrategy;
    public final Context mContext;
    public final InternalScanTask mTask;

    public static /* synthetic */ void $r8$lambda$2UASjNwCjCN6vBPHektlH5WlbJc(InternalScanTaskConverter internalScanTaskConverter, ObservableEmitter observableEmitter) {
        internalScanTaskConverter.lambda$convertFlow$0(observableEmitter);
    }

    public InternalScanTaskConverter(Context context, InternalScanTask internalScanTask) {
        this.mContext = context;
        this.mTask = internalScanTask;
        if (internalScanTask.getScanRequest().isMediaStoreSupportGalleryScan()) {
            this.mClassicalScanStrategy = new ModernClassicalScanStrategy(context);
        } else {
            this.mClassicalScanStrategy = new LegacyClassicalScanStrategy(context);
        }
    }

    @Override // com.miui.gallery.scanner.core.task.convertor.IScanTaskConverter
    public List<SemiScanTask> convert(List<Throwable> list) {
        int sceneCode = this.mTask.getConfig().getSceneCode();
        if (sceneCode != 1) {
            if (sceneCode == 21 || sceneCode == 22) {
                return Collections.singletonList(new CorrectMediaTask(this.mContext, this.mTask.getConfig()));
            }
            return newScanPathsTask(this.mContext, this.mTask.getScanRequest(), this.mTask.getConfig());
        }
        return Collections.singletonList(new CancelRunningTask(this.mContext));
    }

    public /* synthetic */ void lambda$convertFlow$0(ObservableEmitter observableEmitter) throws Exception {
        this.mClassicalScanStrategy.newClassicalScanTask(new TaskEmitter(this.mTask, observableEmitter));
    }

    @Override // com.miui.gallery.scanner.core.task.convertor.IScanTaskConverter
    public Observable<SemiScanTask> convertFlow(List<Throwable> list) {
        return Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.scanner.core.task.convertor.internal.InternalScanTaskConverter$$ExternalSyntheticLambda0
            @Override // io.reactivex.ObservableOnSubscribe
            public final void subscribe(ObservableEmitter observableEmitter) {
                InternalScanTaskConverter.$r8$lambda$2UASjNwCjCN6vBPHektlH5WlbJc(InternalScanTaskConverter.this, observableEmitter);
            }
        });
    }

    public static List<SemiScanTask> newScanPathsTask(Context context, ScanRequest scanRequest, ScanTaskConfig scanTaskConfig) {
        List<String> paths = scanRequest.getPaths();
        if (!BaseMiscUtil.isValid(paths)) {
            return Collections.emptyList();
        }
        ScanPathsTask create = ScanPathsTask.create(context, paths, scanTaskConfig);
        if (create == null) {
            return Collections.emptyList();
        }
        return Collections.singletonList(create);
    }
}
