package com.miui.gallery.scanner.core.scanner;

import android.content.Context;
import com.miui.gallery.assistant.manager.MediaFeatureManager;
import com.miui.gallery.scanner.core.ScanContracts$ScanResultReason;
import com.miui.gallery.scanner.core.model.OwnerAlbumEntry;
import com.miui.gallery.scanner.core.model.OwnerItemEntry;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.eventual.ScanResult;
import com.miui.gallery.util.deleterecorder.DeleteRecord;
import com.miui.gallery.util.deleterecorder.DeleteRecorder;
import com.miui.gallery.util.logger.DefaultLogger;
import java.nio.file.Path;

/* loaded from: classes2.dex */
public class MediaScanner implements IMediaScanner<OwnerAlbumEntry, OwnerItemEntry> {
    @Override // com.miui.gallery.scanner.core.scanner.IMediaScanner
    public ScanResult scanFile(Context context, Path path, OwnerAlbumEntry ownerAlbumEntry, ScanTaskConfig scanTaskConfig) {
        if (ownerAlbumEntry == null && (ownerAlbumEntry = MediaScannerHelper.queryOrInsertAlbum(context, path.getParent().toString())) == null) {
            return ScanResult.failed(ScanContracts$ScanResultReason.QUERY_OR_INSERT_ALBUM_FAILED).build();
        }
        ScanResult scanFile = new ScannerFileClient(scanTaskConfig).scanFile(context, path.toFile(), ownerAlbumEntry);
        DefaultLogger.d("MediaScanner", "scan file [%s], result [%s], reason [%d].", path, scanFile.getResult(), scanFile.getReasonCode());
        return new ScanResult.Builder().cloneFrom(scanFile).setAlbumEntry(ownerAlbumEntry).build();
    }

    @Override // com.miui.gallery.scanner.core.scanner.IMediaScanner
    public ScanResult cleanFile(Context context, Path path, OwnerItemEntry ownerItemEntry, ScanTaskConfig scanTaskConfig) {
        if (ownerItemEntry == null) {
            DefaultLogger.e("MediaScanner", "cloud entry is null, failed to clean file %s.", path);
            return ScanResult.failed(ScanContracts$ScanResultReason.CLOUD_ENTRY_NULL).build();
        }
        DeleteRecord deleteRecord = null;
        String operatorPackageName = scanTaskConfig.getOperatorPackageName() == null ? "MediaScanner" : scanTaskConfig.getOperatorPackageName();
        int i = ownerItemEntry.mLocalFlag;
        if (i == 7 || i == 8) {
            if (scanTaskConfig.getDeleter() != null) {
                scanTaskConfig.getDeleter().delete(context, ownerItemEntry.mId);
            } else {
                MediaScannerHelper.deleteLocalItem(context, ownerItemEntry.mId);
            }
            deleteRecord = new DeleteRecord(11, ownerItemEntry.mPath, operatorPackageName);
            MediaFeatureManager.getInstance().onImageDelete(ownerItemEntry.mId);
        } else if (i == 0) {
            deleteRecord = MediaScannerHelper.checkAndUpdateFileInfo(context, ownerItemEntry.mLocalFile, ownerItemEntry.mThumbnail, ownerItemEntry.mId, scanTaskConfig.getBatchOperator(), scanTaskConfig, operatorPackageName);
        }
        if (deleteRecord != null) {
            if (scanTaskConfig.getDeleteRecords() != null) {
                scanTaskConfig.getDeleteRecords().add(deleteRecord);
            } else {
                DeleteRecorder.getInstance().record(deleteRecord);
            }
            if (scanTaskConfig.getOperatorPackageName() != null) {
                DefaultLogger.fd("MediaScanner", "Delete Record - path: [%s], operatorPackageName: [%s].", path, scanTaskConfig.getOperatorPackageName());
            }
        }
        return ScanResult.success(ScanContracts$ScanResultReason.DEFAULT).build();
    }
}
