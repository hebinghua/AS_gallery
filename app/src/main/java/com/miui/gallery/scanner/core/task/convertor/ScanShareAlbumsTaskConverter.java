package com.miui.gallery.scanner.core.task.convertor;

import android.content.Context;
import android.text.TextUtils;
import com.miui.gallery.scanner.core.model.ShareAlbumEntry;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.eventual.EventualScanTask;
import com.miui.gallery.scanner.core.task.eventual.ScanShareImageTask;
import com.miui.gallery.scanner.core.task.semi.ScanShareAlbumsTask;
import com.miui.gallery.storage.constants.StorageConstants;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import java.io.File;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class ScanShareAlbumsTaskConverter implements IScanTaskConverter<EventualScanTask> {
    public Context mContext;
    public ScanShareAlbumsTask mTask;

    /* renamed from: $r8$lambda$Sr3sux1QqBf_KefV-sskkbZzHaE */
    public static /* synthetic */ void m1277$r8$lambda$Sr3sux1QqBf_KefVsskkbZzHaE(ScanShareAlbumsTaskConverter scanShareAlbumsTaskConverter, ObservableEmitter observableEmitter) {
        scanShareAlbumsTaskConverter.lambda$convertFlow$0(observableEmitter);
    }

    public ScanShareAlbumsTaskConverter(Context context, ScanShareAlbumsTask scanShareAlbumsTask) {
        this.mContext = context;
        this.mTask = scanShareAlbumsTask;
    }

    public /* synthetic */ void lambda$convertFlow$0(ObservableEmitter observableEmitter) throws Exception {
        convertToEventualScanTask(this.mContext, this.mTask.getConfig(), observableEmitter);
    }

    @Override // com.miui.gallery.scanner.core.task.convertor.IScanTaskConverter
    public Observable<EventualScanTask> convertFlow(List<Throwable> list) {
        return Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.scanner.core.task.convertor.ScanShareAlbumsTaskConverter$$ExternalSyntheticLambda0
            @Override // io.reactivex.ObservableOnSubscribe
            public final void subscribe(ObservableEmitter observableEmitter) {
                ScanShareAlbumsTaskConverter.m1277$r8$lambda$Sr3sux1QqBf_KefVsskkbZzHaE(ScanShareAlbumsTaskConverter.this, observableEmitter);
            }
        });
    }

    public final void convertToEventualScanTask(Context context, ScanTaskConfig scanTaskConfig, ObservableEmitter<EventualScanTask> observableEmitter) {
        File[] listFiles;
        Map<String, ShareAlbumEntry> albumEntryMap = ShareAlbumEntry.getAlbumEntryMap(context);
        if (!BaseMiscUtil.isValid(albumEntryMap)) {
            DefaultLogger.d("ScanShareAlbumsTaskConverter", "no valid share album.");
            return;
        }
        for (String str : StorageUtils.getPathsInExternalStorage(context, StorageConstants.RELATIVE_DIRECTORY_SHARER_ALBUM)) {
            File file = new File(str);
            if (file.exists() && file.isDirectory() && (listFiles = file.listFiles()) != null && listFiles.length > 0) {
                for (File file2 : listFiles) {
                    dealWithShareImageFile(context, albumEntryMap, file2, scanTaskConfig, observableEmitter);
                }
            }
        }
    }

    public final void dealWithShareImageFile(Context context, Map<String, ShareAlbumEntry> map, File file, ScanTaskConfig scanTaskConfig, ObservableEmitter<EventualScanTask> observableEmitter) {
        ShareAlbumEntry shareAlbumEntry;
        if (file == null || !file.exists()) {
            return;
        }
        String absolutePath = file.getAbsolutePath();
        String fileNameWithoutExtension = BaseFileUtils.getFileNameWithoutExtension(absolutePath);
        if (TextUtils.isEmpty(fileNameWithoutExtension)) {
            return;
        }
        String[] split = fileNameWithoutExtension.split("_");
        if (split.length < 1) {
            return;
        }
        String str = split[split.length - 1];
        if (!map.containsKey(str) || (shareAlbumEntry = map.get(str)) == null) {
            return;
        }
        observableEmitter.onNext(new ScanShareImageTask(context, scanTaskConfig, Paths.get(absolutePath, new String[0]), shareAlbumEntry));
    }
}
