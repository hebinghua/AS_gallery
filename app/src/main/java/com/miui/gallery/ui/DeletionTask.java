package com.miui.gallery.ui;

import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.provider.CloudUtils;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.ui.ProcessTask;
import com.miui.gallery.util.DebugUtil;
import java.io.Serializable;
import java.util.List;

/* loaded from: classes2.dex */
public class DeletionTask extends ProcessTaskForStoragePermissionMiss<Param, List<IStoragePermissionStrategy.PermissionResult>, long[]> {
    public boolean isDeleteAlbum;
    public long mOperationStartTime;

    /* loaded from: classes2.dex */
    public interface OnDeletionCompleteListener {
        void onDeleted(int i, long[] jArr);
    }

    @Override // com.miui.gallery.ui.ProcessTask
    /* renamed from: getDefaultExceptionResult  reason: collision with other method in class */
    public long[] mo1446getDefaultExceptionResult(Exception exc) {
        return new long[]{-121};
    }

    public DeletionTask() {
        super(new ProcessTask.ProcessCallback<Param, List<IStoragePermissionStrategy.PermissionResult>, long[]>() { // from class: com.miui.gallery.ui.DeletionTask.1
            @Override // com.miui.gallery.ui.ProcessTask.ProcessCallback
            public long[] doProcess(Param[] paramArr) {
                try {
                    int i = paramArr[0].mDeleteBy;
                    if (i == 0) {
                        return CloudUtils.deleteById(GalleryApp.sGetAndroidContext(), paramArr[0].mDeleteOptions, paramArr[0].mDeleteReason, paramArr[0].mIds);
                    }
                    if (i == 1) {
                        return CloudUtils.deleteByPath(GalleryApp.sGetAndroidContext(), paramArr[0].mDeleteOptions, paramArr[0].mDeleteReason, paramArr[0].mPaths);
                    }
                    return i != 2 ? new long[0] : CloudUtils.deleteAlbum(GalleryApp.sGetAndroidContext(), paramArr[0].mDeleteOptions, paramArr[0].mDeleteReason, paramArr[0].mIds);
                } catch (StoragePermissionMissingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        this.mOperationStartTime = 0L;
        this.isDeleteAlbum = false;
    }

    @Override // android.os.AsyncTask
    public void onPreExecute() {
        super.onPreExecute();
        this.mOperationStartTime = System.currentTimeMillis();
    }

    @Override // com.miui.gallery.ui.ProcessTask, android.os.AsyncTask
    public long[] doInBackground(Param... paramArr) {
        boolean z = paramArr[0].mDeleteBy == 2;
        this.isDeleteAlbum = z;
        DebugUtil.logEventTime("operationTrace", z ? "delete_album" : "delete_photo", this.mOperationStartTime, false);
        return (long[]) super.doInBackground((Object[]) paramArr);
    }

    @Override // com.miui.gallery.ui.ProcessTask, android.os.AsyncTask
    public void onPostExecute(long[] jArr) {
        super.onPostExecute((DeletionTask) jArr);
        DebugUtil.logEventTime("operationTrace", this.isDeleteAlbum ? "delete_album" : "delete_photo", System.currentTimeMillis(), true);
    }

    public void showProgress(FragmentActivity fragmentActivity) {
        if (fragmentActivity == null) {
            return;
        }
        super.showProgress(fragmentActivity, fragmentActivity.getString(R.string.delete_in_process));
    }

    public void setOnDeletionCompleteListener(final OnDeletionCompleteListener onDeletionCompleteListener) {
        setCompleteListener(new ProcessTask.OnCompleteListener<long[]>() { // from class: com.miui.gallery.ui.DeletionTask.2
            @Override // com.miui.gallery.ui.ProcessTask.OnCompleteListener
            public void onCompleteProcess(long[] jArr) {
                int length = jArr == null ? -1 : jArr.length;
                OnDeletionCompleteListener onDeletionCompleteListener2 = onDeletionCompleteListener;
                if (onDeletionCompleteListener2 != null) {
                    onDeletionCompleteListener2.onDeleted(length, jArr);
                }
            }
        });
    }

    /* loaded from: classes2.dex */
    public static class Param implements Serializable {
        public long mAlbumId;
        public String mAlbumName;
        public int mDeleteBy;
        public int mDeleteOptions;
        public int mDeleteReason;
        public long[] mIds;
        public boolean mIsBurstItems;
        public String[] mPaths;
        public int mSource;

        public Param(long[] jArr, int i, int i2) {
            this.mAlbumId = -1L;
            this.mSource = 0;
            this.mDeleteBy = 2;
            this.mIds = jArr;
            this.mDeleteOptions = i;
            this.mDeleteReason = i2;
        }

        public Param(String[] strArr, int i, int i2) {
            this.mAlbumId = -1L;
            this.mSource = 0;
            this.mDeleteBy = 1;
            this.mPaths = strArr;
            this.mDeleteOptions = i;
            this.mDeleteReason = i2;
        }

        public Param(long[] jArr, long j, String str, int i) {
            this.mAlbumId = -1L;
            this.mSource = 0;
            this.mIds = jArr;
            this.mAlbumId = j;
            this.mAlbumName = str;
            this.mDeleteBy = 0;
            this.mDeleteReason = i;
        }

        public Param(long[] jArr, long j, String str, int i, boolean z, int i2) {
            this.mAlbumId = -1L;
            this.mSource = 0;
            this.mIds = jArr;
            this.mAlbumId = j;
            this.mAlbumName = str;
            this.mDeleteBy = 0;
            this.mDeleteReason = i;
            this.mIsBurstItems = z;
            this.mSource = i2;
        }

        public int getItemsCount() {
            int i = this.mDeleteBy;
            if (i != 0) {
                if (i == 1) {
                    return this.mPaths.length;
                }
                if (i != 2) {
                    return 0;
                }
            }
            return this.mIds.length;
        }
    }
}
