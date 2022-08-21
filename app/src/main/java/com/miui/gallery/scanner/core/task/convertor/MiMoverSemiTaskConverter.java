package com.miui.gallery.scanner.core.task.convertor;

import android.content.Context;
import com.miui.gallery.scanner.core.scanner.MediaScannerHelper;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.eventual.EventualScanTask;
import com.miui.gallery.scanner.core.task.eventual.scansinglefile.ScanSingleFileTask;
import com.miui.gallery.scanner.core.task.semi.MiMoverSemiTask;
import com.miui.gallery.storage.constants.StorageConstants;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;

/* loaded from: classes2.dex */
public class MiMoverSemiTaskConverter implements IScanTaskConverter<EventualScanTask> {
    public Context mContext;
    public MiMoverSemiTask mTask;

    /* renamed from: $r8$lambda$iRkuOZ8GZ_I-d7KzfXDlFj84C4w */
    public static /* synthetic */ void m1276$r8$lambda$iRkuOZ8GZ_Id7KzfXDlFj84C4w(MiMoverSemiTaskConverter miMoverSemiTaskConverter, ObservableEmitter observableEmitter) {
        miMoverSemiTaskConverter.lambda$convertFlow$0(observableEmitter);
    }

    public MiMoverSemiTaskConverter(Context context, MiMoverSemiTask miMoverSemiTask) {
        this.mContext = context;
        this.mTask = miMoverSemiTask;
    }

    public /* synthetic */ void lambda$convertFlow$0(ObservableEmitter observableEmitter) throws Exception {
        convertToEventualScanTask(this.mContext, this.mTask, observableEmitter);
    }

    @Override // com.miui.gallery.scanner.core.task.convertor.IScanTaskConverter
    public Observable<EventualScanTask> convertFlow(List<Throwable> list) {
        return Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.scanner.core.task.convertor.MiMoverSemiTaskConverter$$ExternalSyntheticLambda0
            @Override // io.reactivex.ObservableOnSubscribe
            public final void subscribe(ObservableEmitter observableEmitter) {
                MiMoverSemiTaskConverter.m1276$r8$lambda$iRkuOZ8GZ_Id7KzfXDlFj84C4w(MiMoverSemiTaskConverter.this, observableEmitter);
            }
        });
    }

    public final void convertToEventualScanTask(Context context, MiMoverSemiTask miMoverSemiTask, ObservableEmitter<EventualScanTask> observableEmitter) {
        if (miMoverSemiTask == null) {
            return;
        }
        List<String> paths = miMoverSemiTask.getPaths();
        if (!BaseMiscUtil.isValid(paths)) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        doConvertToEventualScanTasks(context, miMoverSemiTask, paths, miMoverSemiTask.getConfig(), observableEmitter);
        DefaultLogger.d("MiMoverSemiTaskConverter", String.format("convertToEventualScanTask cost [%s] ms.", Long.valueOf(System.currentTimeMillis() - currentTimeMillis)));
    }

    public final void doConvertToEventualScanTasks(Context context, MiMoverSemiTask miMoverSemiTask, List<String> list, ScanTaskConfig scanTaskConfig, ObservableEmitter<EventualScanTask> observableEmitter) {
        if (context == null || !BaseMiscUtil.isValid(list)) {
            return;
        }
        for (String str : list) {
            dealWithPath(context, miMoverSemiTask, str, scanTaskConfig, observableEmitter);
        }
    }

    public final void dealWithPath(Context context, MiMoverSemiTask miMoverSemiTask, String str, ScanTaskConfig scanTaskConfig, ObservableEmitter<EventualScanTask> observableEmitter) {
        File file = new File(str);
        if (!file.exists() || !isMediaFile(str)) {
            DefaultLogger.w("MiMoverSemiTaskConverter", "file [%s] not exists or not a media file.", str);
        } else if (isInBlackList(file.getParent())) {
            DefaultLogger.w("MiMoverSemiTaskConverter", "file [%s] is in black list.", str);
        } else if (!MediaScannerHelper.isScannableDirectory(file.getParentFile())) {
            DefaultLogger.w("MiMoverSemiTaskConverter", "file [%s] is not scannable.", str);
        } else {
            ScanSingleFileTask scanSingleFileTask = new ScanSingleFileTask(context, Paths.get(str, new String[0]), scanTaskConfig, 2L);
            scanSingleFileTask.setParentTask(miMoverSemiTask);
            observableEmitter.onNext(scanSingleFileTask);
        }
    }

    public static boolean isMediaFile(String str) {
        String mimeType = BaseFileMimeUtil.getMimeType(str);
        return BaseFileMimeUtil.isImageFromMimeType(mimeType) || BaseFileMimeUtil.isVideoFromMimeType(mimeType);
    }

    public static boolean isInBlackList(String str) {
        return str == null || str.contains(StorageConstants.RELATIVE_DIRECTORY_SHARER_ALBUM) || str.contains(StorageConstants.RELATIVE_DIRECTORY_GALLERY_ALBUM) || str.contains("/MIUI/Gallery");
    }
}
