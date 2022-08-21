package com.miui.gallery.scanner.core.model;

import com.miui.gallery.provider.ContentProviderBatchOperator;
import com.miui.gallery.scanner.core.bulkoperator.IBulkInserter;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.utils.AbsDeDupStrategy;
import com.miui.gallery.scanner.utils.AbsImageValueCalculator;
import com.miui.gallery.scanner.utils.AbsVideoValueCalculator;
import com.miui.gallery.scanner.utils.DefaultDeDupStrategy;
import com.miui.gallery.scanner.utils.DefaultImageValueCalculator;
import com.miui.gallery.scanner.utils.DefaultVideoValueCalculator;
import java.io.File;

/* loaded from: classes2.dex */
public class SaveParams {
    public final long mAlbumId;
    public final int mAlbumSyncState;
    public ContentProviderBatchOperator mBatchOperator;
    public final boolean mBulkNotify;
    public boolean mCredible;
    public AbsDeDupStrategy mDeDupStrategy;
    public String mFileName;
    public final ScanTaskConfig.FileState mFileState;
    public AbsImageValueCalculator mImageValueCalculator;
    public IBulkInserter mInserter;
    public final int mLocalFlag;
    public String mMimeType;
    public final File mSaveFile;
    public final long mSpecifiedModifiedTime;
    public final long mSpecifiedTakenTime;
    public AbsVideoValueCalculator mVideoValueCalculator;

    public SaveParams(Builder builder) {
        this.mSaveFile = builder.mSaveFile;
        this.mFileState = builder.mFileState;
        this.mAlbumId = builder.mAlbumId;
        if (builder.mAlbumAttributes > 0) {
            if (OwnerAlbumEntry.isSyncable(builder.mAlbumAttributes)) {
                this.mAlbumSyncState = 0;
            } else {
                this.mAlbumSyncState = 1;
            }
        } else {
            this.mAlbumSyncState = builder.mAlbumSyncState;
        }
        this.mLocalFlag = builder.mLocalFlag;
        this.mBulkNotify = builder.mIsBulkNotify;
        this.mSpecifiedTakenTime = builder.mSpecifiedTakenTime;
        this.mSpecifiedModifiedTime = builder.mSpecifiedModifiedTime;
        this.mCredible = builder.mCredible;
        this.mInserter = builder.mInserter;
        this.mBatchOperator = builder.mBatchOperator;
        this.mImageValueCalculator = builder.mImageValueCalculator;
        this.mVideoValueCalculator = builder.mVideoValueCalculator;
        this.mDeDupStrategy = builder.mDeDupStrategy;
        this.mMimeType = builder.mMimeType;
        this.mFileName = builder.mFileName;
    }

    public long getSpecifiedModifiedTime() {
        return this.mSpecifiedModifiedTime;
    }

    public long getSpecifiedTakenTime() {
        return this.mSpecifiedTakenTime;
    }

    public File getSaveFile() {
        return this.mSaveFile;
    }

    public ScanTaskConfig.FileState getFileState() {
        return this.mFileState;
    }

    public long getAlbumId() {
        return this.mAlbumId;
    }

    public String getMimeType() {
        return this.mMimeType;
    }

    public String getFileName() {
        return this.mFileName;
    }

    public boolean isValidSyncValue() {
        return this.mAlbumSyncState != -1;
    }

    public boolean isAlbumSyncable() {
        return this.mAlbumSyncState == 0;
    }

    public int getLocalFlag() {
        return this.mLocalFlag;
    }

    public boolean isBulkNotify() {
        return this.mBulkNotify;
    }

    public boolean isSaveToSecretAlbum() {
        return this.mAlbumId == -1000;
    }

    public boolean isCredible() {
        return this.mCredible;
    }

    public IBulkInserter getInserter() {
        return this.mInserter;
    }

    public ContentProviderBatchOperator getBatchOperator() {
        return this.mBatchOperator;
    }

    public AbsImageValueCalculator getImageValueCalculator() {
        return this.mImageValueCalculator;
    }

    public AbsVideoValueCalculator getVideoValueCalculator() {
        return this.mVideoValueCalculator;
    }

    public AbsDeDupStrategy getDeDupStrategy() {
        return this.mDeDupStrategy;
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        public int mAlbumAttributes;
        public ContentProviderBatchOperator mBatchOperator;
        public boolean mCredible;
        public AbsDeDupStrategy mDeDupStrategy;
        public String mFileName;
        public ScanTaskConfig.FileState mFileState;
        public AbsImageValueCalculator mImageValueCalculator;
        public IBulkInserter mInserter;
        public int mLocalFlag;
        public String mMimeType;
        public File mSaveFile;
        public AbsVideoValueCalculator mVideoValueCalculator;
        public long mAlbumId = -1;
        public int mAlbumSyncState = -1;
        public boolean mIsBulkNotify = false;
        public long mSpecifiedModifiedTime = -1;
        public long mSpecifiedTakenTime = -1;

        public Builder setSaveFile(File file) {
            this.mSaveFile = file;
            return this;
        }

        public Builder setFileState(ScanTaskConfig.FileState fileState) {
            this.mFileState = fileState;
            return this;
        }

        public Builder setSpecifiedModifiedTime(long j) {
            this.mSpecifiedModifiedTime = j;
            return this;
        }

        public Builder setSpecifiedTakenTime(long j) {
            this.mSpecifiedTakenTime = j;
            return this;
        }

        public Builder setAlbumId(long j) {
            this.mAlbumId = j;
            return this;
        }

        public Builder setAlbumAttributes(int i) {
            this.mAlbumAttributes = i;
            return this;
        }

        public Builder setAlbumSyncable(boolean z) {
            this.mAlbumSyncState = !z ? 1 : 0;
            return this;
        }

        public Builder setLocalFlag(int i) {
            this.mLocalFlag = i;
            return this;
        }

        public Builder setBulkNotify(boolean z) {
            this.mIsBulkNotify = z;
            return this;
        }

        public Builder setInserter(IBulkInserter iBulkInserter) {
            this.mInserter = iBulkInserter;
            return this;
        }

        public Builder setBatchOperator(ContentProviderBatchOperator contentProviderBatchOperator) {
            this.mBatchOperator = contentProviderBatchOperator;
            return this;
        }

        public Builder setCredible(boolean z) {
            this.mCredible = z;
            return this;
        }

        public Builder setImageValueCalculator(AbsImageValueCalculator absImageValueCalculator) {
            this.mImageValueCalculator = absImageValueCalculator;
            return this;
        }

        public Builder setVideoValueCalculator(AbsVideoValueCalculator absVideoValueCalculator) {
            this.mVideoValueCalculator = absVideoValueCalculator;
            return this;
        }

        public Builder setDeDupStrategy(AbsDeDupStrategy absDeDupStrategy) {
            this.mDeDupStrategy = absDeDupStrategy;
            return this;
        }

        public Builder setMimeType(String str) {
            this.mMimeType = str;
            return this;
        }

        public Builder setFileName(String str) {
            this.mFileName = str;
            return this;
        }

        public SaveParams build() {
            if (this.mImageValueCalculator == null) {
                this.mImageValueCalculator = new DefaultImageValueCalculator(this.mSaveFile.getAbsolutePath());
            }
            if (this.mVideoValueCalculator == null) {
                this.mVideoValueCalculator = new DefaultVideoValueCalculator(this.mSaveFile.getAbsolutePath());
            }
            if (this.mDeDupStrategy == null) {
                this.mDeDupStrategy = new DefaultDeDupStrategy();
            }
            return new SaveParams(this);
        }
    }
}
