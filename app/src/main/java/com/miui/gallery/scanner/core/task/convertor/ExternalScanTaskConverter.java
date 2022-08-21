package com.miui.gallery.scanner.core.task.convertor;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.google.common.collect.Lists;
import com.miui.gallery.scanner.core.task.ScanTaskConfig;
import com.miui.gallery.scanner.core.task.semi.ScanPathsTask;
import com.miui.gallery.scanner.core.task.semi.SemiParallelProcessingImageScanTask;
import com.miui.gallery.scanner.core.task.semi.SemiScanTask;
import com.miui.gallery.storage.constants.AndroidStorageConstants;
import com.miui.gallery.util.ProcessingMediaHelper;
import java.util.Collections;
import java.util.List;

/* loaded from: classes2.dex */
public class ExternalScanTaskConverter implements IScanTaskConverter<SemiScanTask> {
    public final IScanTaskConverter<SemiScanTask> mInnerConverter;

    public ExternalScanTaskConverter(Context context, long j, String str, boolean z, String str2, String str3, int i, ScanTaskConfig scanTaskConfig) {
        this.mInnerConverter = buildInnerConverter(context, j, str, z, str2, str3, i, scanTaskConfig);
    }

    @Override // com.miui.gallery.scanner.core.task.convertor.IScanTaskConverter
    public List<SemiScanTask> convert(List<Throwable> list) {
        IScanTaskConverter<SemiScanTask> iScanTaskConverter = this.mInnerConverter;
        return iScanTaskConverter == null ? Collections.emptyList() : iScanTaskConverter.convert(list);
    }

    public static IScanTaskConverter<SemiScanTask> buildInnerConverter(Context context, long j, String str, boolean z, String str2, String str3, int i, ScanTaskConfig scanTaskConfig) {
        if (TextUtils.isEmpty(str2)) {
            return null;
        }
        if (TextUtils.equals(str2, AndroidStorageConstants.PACKAGE_NAME_MEDIA_STORE)) {
            return new MediaStoreScanTaskConverter(context, str, str3, scanTaskConfig);
        }
        if (!TextUtils.equals(str2, "com.android.camera")) {
            return null;
        }
        return new CameraScanTaskConverter(context, j, str, z, i, scanTaskConfig);
    }

    /* loaded from: classes2.dex */
    public static class MediaStoreScanTaskConverter implements IScanTaskConverter<SemiScanTask> {
        public final ScanTaskConfig mConfig;
        public final Context mContext;
        public final String mPath;

        public MediaStoreScanTaskConverter(Context context, String str, String str2, ScanTaskConfig scanTaskConfig) {
            this.mContext = context;
            this.mPath = str;
            this.mConfig = new ScanTaskConfig.Builder().cloneFrom(scanTaskConfig).setOperatorPackageName(str2).build();
        }

        @Override // com.miui.gallery.scanner.core.task.convertor.IScanTaskConverter
        public List<SemiScanTask> convert(List<Throwable> list) {
            ScanPathsTask create = ScanPathsTask.create(this.mContext, Lists.newArrayList(this.mPath), this.mConfig);
            if (create == null) {
                return Collections.emptyList();
            }
            return Lists.newArrayList(create);
        }
    }

    /* loaded from: classes2.dex */
    public static class CameraScanTaskConverter implements IScanTaskConverter<SemiScanTask> {
        public final ScanTaskConfig mConfig;
        public final Context mContext;
        public final long mMediaStoreId;
        public final int mParallelProcessState;
        public final String mPath;
        public final boolean mUsingGaussian;

        public CameraScanTaskConverter(Context context, long j, String str, boolean z, int i, ScanTaskConfig scanTaskConfig) {
            this.mContext = context;
            this.mMediaStoreId = j;
            this.mPath = str;
            this.mUsingGaussian = z;
            this.mParallelProcessState = i;
            this.mConfig = scanTaskConfig;
        }

        @Override // com.miui.gallery.scanner.core.task.convertor.IScanTaskConverter
        public List<SemiScanTask> convert(List<Throwable> list) {
            Intent intent = new Intent();
            intent.setAction("com.miui.gallery.SAVE_TO_CLOUD");
            intent.putExtra("extra_file_path", this.mPath);
            intent.putExtra("extra_is_temp_file", this.mParallelProcessState == 1);
            LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(intent);
            ProcessingMediaHelper.ProcessingItem build = ProcessingMediaHelper.ProcessingItem.build(this.mMediaStoreId, this.mPath, this.mUsingGaussian);
            if (this.mParallelProcessState == 1) {
                ProcessingMediaHelper.getInstance().addProcessingItem(build);
                return Lists.newArrayList(new SemiParallelProcessingImageScanTask(this.mContext, this.mPath, this.mConfig));
            }
            ProcessingMediaHelper.getInstance().removeProcessingItem(build);
            return Lists.newArrayList(ScanPathsTask.create(this.mContext, Lists.newArrayList(this.mPath), this.mConfig));
        }
    }
}
