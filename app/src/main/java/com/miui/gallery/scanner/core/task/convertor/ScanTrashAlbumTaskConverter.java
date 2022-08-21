package com.miui.gallery.scanner.core.task.convertor;

import android.content.Context;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.eventual.CleanTrashFileTask;
import com.miui.gallery.scanner.core.task.eventual.EventualScanTask;
import com.miui.gallery.scanner.core.task.semi.ScanTrashAlbumTask;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import java.util.List;

/* loaded from: classes2.dex */
public class ScanTrashAlbumTaskConverter implements IScanTaskConverter<EventualScanTask> {
    public Context mContext;
    public ScanTrashAlbumTask mTask;

    /* renamed from: $r8$lambda$jH-OefMj7FcSSIvEkgLwokRPe5U */
    public static /* synthetic */ void m1278$r8$lambda$jHOefMj7FcSSIvEkgLwokRPe5U(ScanTrashAlbumTaskConverter scanTrashAlbumTaskConverter, ObservableEmitter observableEmitter) {
        scanTrashAlbumTaskConverter.lambda$convertFlow$0(observableEmitter);
    }

    public ScanTrashAlbumTaskConverter(Context context, ScanTrashAlbumTask scanTrashAlbumTask) {
        this.mContext = context;
        this.mTask = scanTrashAlbumTask;
    }

    public /* synthetic */ void lambda$convertFlow$0(ObservableEmitter observableEmitter) throws Exception {
        convertToEventualScanTask(this.mContext, this.mTask.getConfig(), observableEmitter);
    }

    @Override // com.miui.gallery.scanner.core.task.convertor.IScanTaskConverter
    public Observable<EventualScanTask> convertFlow(List<Throwable> list) {
        return Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.scanner.core.task.convertor.ScanTrashAlbumTaskConverter$$ExternalSyntheticLambda0
            @Override // io.reactivex.ObservableOnSubscribe
            public final void subscribe(ObservableEmitter observableEmitter) {
                ScanTrashAlbumTaskConverter.m1278$r8$lambda$jHOefMj7FcSSIvEkgLwokRPe5U(ScanTrashAlbumTaskConverter.this, observableEmitter);
            }
        });
    }

    public final void convertToEventualScanTask(Context context, ScanTaskConfig scanTaskConfig, ObservableEmitter<EventualScanTask> observableEmitter) {
        observableEmitter.onNext(new CleanTrashFileTask(context, scanTaskConfig));
    }
}
