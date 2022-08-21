package com.miui.gallery.scanner.core.task;

import com.miui.gallery.provider.ContentProviderBatchOperator;
import com.miui.gallery.scanner.core.ScanContracts$Mode;
import com.miui.gallery.scanner.core.bulkoperator.IBulkInserter;
import com.miui.gallery.scanner.core.bulkoperator.MediaBulkDeleter;
import com.miui.gallery.scanner.core.scanner.IMediaScanner;
import com.miui.gallery.scanner.core.scanner.MediaScanner;
import com.miui.gallery.util.deleterecorder.DeleteRecord;
import java.util.List;

/* loaded from: classes2.dex */
public class ScanTaskConfig {
    public final boolean activelyScan;
    public final ContentProviderBatchOperator batchOperator;
    public final boolean bulkNotify;
    public final boolean credible;
    public final List<DeleteRecord> deleteRecords;
    public final MediaBulkDeleter deleter;
    public final FileState fileState;
    public final IBulkInserter inserter;
    public final boolean isForceScan;
    public final boolean linkFile;
    public final ScanContracts$Mode mode;
    public final boolean needTriggerSync;
    public final String operatorPackageName;
    public final long priority;
    public final boolean recursiveScan;
    public final IMediaScanner scanner;
    public final int sceneCode;
    public final boolean showInRecentAlbum;

    /* loaded from: classes2.dex */
    public static class FileState {
        public long modified;
        public long size;
    }

    public ScanTaskConfig(Builder builder) {
        this.sceneCode = builder.sceneCode;
        this.priority = builder.priority;
        this.bulkNotify = builder.bulkNotify;
        this.recursiveScan = builder.recursiveScan;
        this.isForceScan = builder.isForceScan;
        this.activelyScan = builder.activeScan;
        this.scanner = builder.scanner;
        this.linkFile = builder.linkFile;
        this.needTriggerSync = builder.needTriggerSync;
        this.inserter = builder.inserter;
        this.deleter = builder.deleter;
        this.batchOperator = builder.batchOperator;
        this.deleteRecords = builder.deleteRecords;
        this.credible = builder.credible;
        this.operatorPackageName = builder.operatorPackageName;
        this.showInRecentAlbum = builder.showInRecentAlbum;
        this.mode = builder.mode;
        this.fileState = builder.fileState;
    }

    public int getSceneCode() {
        return this.sceneCode;
    }

    public long getPriority() {
        return this.priority;
    }

    public boolean isBulkNotify() {
        return this.bulkNotify;
    }

    public boolean isRecursiveScan() {
        return this.recursiveScan;
    }

    public boolean isForceScan() {
        return this.isForceScan;
    }

    public IMediaScanner getScanner() {
        return this.scanner;
    }

    public boolean linkFile() {
        return this.linkFile;
    }

    public IBulkInserter getInserter() {
        return this.inserter;
    }

    public MediaBulkDeleter getDeleter() {
        return this.deleter;
    }

    public ContentProviderBatchOperator getBatchOperator() {
        return this.batchOperator;
    }

    public List<DeleteRecord> getDeleteRecords() {
        return this.deleteRecords;
    }

    public boolean needTriggerSync() {
        return this.needTriggerSync;
    }

    public boolean isCredible() {
        return this.credible;
    }

    public String getOperatorPackageName() {
        return this.operatorPackageName;
    }

    public boolean showInRecentAlbum() {
        return this.showInRecentAlbum;
    }

    public FileState getFileState() {
        return this.fileState;
    }

    public boolean equals(Object obj) {
        return (obj instanceof ScanTaskConfig) && this.sceneCode == ((ScanTaskConfig) obj).sceneCode;
    }

    public int hashCode() {
        return 527 + this.sceneCode;
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        public boolean activeScan;
        public ContentProviderBatchOperator batchOperator;
        public boolean bulkNotify;
        public boolean credible;
        public List<DeleteRecord> deleteRecords;
        public MediaBulkDeleter deleter;
        public FileState fileState;
        public IBulkInserter inserter;
        public boolean isForceScan;
        public boolean linkFile;
        public ScanContracts$Mode mode;
        public String operatorPackageName;
        public boolean recursiveScan;
        public IMediaScanner scanner;
        public int sceneCode;
        public boolean showInRecentAlbum;
        public long priority = 0;
        public boolean needTriggerSync = true;

        public Builder cloneFrom(ScanTaskConfig scanTaskConfig) {
            this.sceneCode = scanTaskConfig.sceneCode;
            this.priority = scanTaskConfig.priority;
            this.bulkNotify = scanTaskConfig.bulkNotify;
            this.recursiveScan = scanTaskConfig.recursiveScan;
            this.isForceScan = scanTaskConfig.isForceScan;
            this.activeScan = scanTaskConfig.activelyScan;
            this.scanner = scanTaskConfig.scanner;
            this.linkFile = scanTaskConfig.linkFile;
            this.needTriggerSync = scanTaskConfig.needTriggerSync;
            this.inserter = scanTaskConfig.inserter;
            this.deleter = scanTaskConfig.deleter;
            this.batchOperator = scanTaskConfig.batchOperator;
            this.deleteRecords = scanTaskConfig.deleteRecords;
            this.credible = scanTaskConfig.credible;
            this.operatorPackageName = scanTaskConfig.operatorPackageName;
            this.showInRecentAlbum = scanTaskConfig.showInRecentAlbum;
            this.mode = scanTaskConfig.mode;
            this.fileState = scanTaskConfig.fileState;
            return this;
        }

        public Builder setSceneCode(int i) {
            this.sceneCode = i;
            return this;
        }

        public Builder setPriority(long j) {
            this.priority = j;
            return this;
        }

        public Builder setBulkNotify(boolean z) {
            this.bulkNotify = z;
            return this;
        }

        public Builder setRecursiveScan(boolean z) {
            this.recursiveScan = z;
            return this;
        }

        public Builder forceScan(boolean z) {
            this.isForceScan = z;
            return this;
        }

        public Builder isActiveScan(boolean z) {
            this.activeScan = z;
            return this;
        }

        public Builder setScanner(IMediaScanner iMediaScanner) {
            this.scanner = iMediaScanner;
            return this;
        }

        public Builder setInserter(IBulkInserter iBulkInserter) {
            this.inserter = iBulkInserter;
            return this;
        }

        public Builder setDeleter(MediaBulkDeleter mediaBulkDeleter) {
            this.deleter = mediaBulkDeleter;
            return this;
        }

        public Builder setBatchOperator(ContentProviderBatchOperator contentProviderBatchOperator) {
            this.batchOperator = contentProviderBatchOperator;
            return this;
        }

        public Builder needTriggerSync(boolean z) {
            this.needTriggerSync = z;
            return this;
        }

        public Builder setDeleteRecords(List<DeleteRecord> list) {
            this.deleteRecords = list;
            return this;
        }

        public Builder setCredible(boolean z) {
            this.credible = z;
            return this;
        }

        public Builder setOperatorPackageName(String str) {
            this.operatorPackageName = str;
            return this;
        }

        public Builder showInRecentAlbum(boolean z) {
            this.showInRecentAlbum = z;
            return this;
        }

        public Builder setMode(ScanContracts$Mode scanContracts$Mode) {
            this.mode = scanContracts$Mode;
            return this;
        }

        public Builder setFileState(FileState fileState) {
            this.fileState = fileState;
            return this;
        }

        public ScanTaskConfig build() {
            if (this.scanner == null) {
                this.scanner = new MediaScanner();
            }
            return new ScanTaskConfig(this);
        }
    }
}
