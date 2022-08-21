package com.miui.gallery.ui.photoPage.bars.manager.edit;

import android.content.Intent;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureHandler;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.model.BaseDataSet;
import com.miui.gallery.scanner.core.ScannerEngine;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.ui.PhotoPageFragment;
import com.miui.gallery.ui.photoPage.bars.data.IDataProvider;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.concurrent.ThreadManager;

/* loaded from: classes2.dex */
public class BaseEditorManager {
    public BaseActivity mActivity;
    public IDataProvider mDataProvider;
    public PhotoPageFragment mFragment;
    public boolean mResultHandled;
    public String mTargetFilePath = null;
    public long mTargetId;
    public MediaUpdateTask mUpdateTask;

    public void onActivityResult(int i, int i2, Intent intent) {
    }

    public void onImageLoadFinish(String str) {
    }

    public void onStartEditor() {
    }

    public BaseEditorManager(IDataProvider iDataProvider, PhotoPageFragment photoPageFragment) {
        this.mDataProvider = iDataProvider;
        this.mFragment = photoPageFragment;
        this.mActivity = (BaseActivity) photoPageFragment.getActivity();
    }

    public void release() {
        MediaUpdateTask mediaUpdateTask = this.mUpdateTask;
        if (mediaUpdateTask != null) {
            mediaUpdateTask.cancel();
            this.mUpdateTask = null;
        }
    }

    public void setTargetPath(String str) {
        this.mTargetFilePath = str;
    }

    public void setTargetId(long j) {
        this.mTargetId = j;
    }

    public void notifyDataSetChange(long j) {
        this.mDataProvider.getFieldData().mArguments.putLong("photo_focused_id", j);
        this.mDataProvider.onContentChanged();
    }

    public void notifyDataSetChange(String str) {
        notifyDataSetChange(str, false);
    }

    public void notifyDataSetChange(String str, boolean z) {
        if (TextUtils.isEmpty(str)) {
            return;
        }
        BaseDataSet dataSet = this.mDataProvider.getFieldData().mCurrent.getDataSet();
        if (dataSet != null) {
            dataSet.addNewFile(str, this.mDataProvider.getFieldData().mCurrent.getPosition() + 1);
        }
        this.mDataProvider.getFieldData().mArguments.putString("photo_focused_path", str);
        if (z) {
            this.mDataProvider.loadInBackground();
        } else {
            this.mDataProvider.onContentChanged();
        }
    }

    public void insertAndNotifyDataSet(String str) {
        insertAndNotifyDataSet(str, true);
    }

    public void insertAndNotifyDataSet(String str, boolean z) {
        insertAndNotifyDataSet(str, z, false);
    }

    public void insertAndNotifyDataSet(String str, boolean z, final boolean z2) {
        if (this.mUpdateTask == null) {
            this.mUpdateTask = new MediaUpdateTask();
        }
        this.mUpdateTask.execute(str, z, new MediaUpdateTask.Callback() { // from class: com.miui.gallery.ui.photoPage.bars.manager.edit.BaseEditorManager.1
            @Override // com.miui.gallery.ui.photoPage.bars.manager.edit.BaseEditorManager.MediaUpdateTask.Callback
            public void onInsertDone(String str2) {
                BaseEditorManager.this.notifyDataSetChange(str2, z2);
            }
        });
    }

    /* loaded from: classes2.dex */
    public static class MediaUpdateTask {
        public Callback mCallback;
        public Future<Void> mFuture;

        /* loaded from: classes2.dex */
        public interface Callback {
            void onInsertDone(String str);
        }

        public MediaUpdateTask() {
        }

        public void execute(final String str, final boolean z, Callback callback) {
            if (TextUtils.isEmpty(str)) {
                return;
            }
            Future<Void> future = this.mFuture;
            if (future != null) {
                future.cancel();
            }
            this.mCallback = callback;
            this.mFuture = ThreadManager.getMiscPool().submit(new ThreadPool.Job<Void>() { // from class: com.miui.gallery.ui.photoPage.bars.manager.edit.BaseEditorManager.MediaUpdateTask.1
                @Override // com.miui.gallery.concurrent.ThreadPool.Job
                /* renamed from: run  reason: collision with other method in class */
                public Void mo1807run(ThreadPool.JobContext jobContext) {
                    ScannerEngine.getInstance().scanFile(GalleryApp.sGetAndroidContext(), str, 13);
                    if (z) {
                        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("BaseEditorManager", "execute"));
                        if (documentFile == null) {
                            return null;
                        }
                        StorageSolutionProvider.get().apply(documentFile);
                        return null;
                    }
                    return null;
                }
            }, new FutureHandler<Void>() { // from class: com.miui.gallery.ui.photoPage.bars.manager.edit.BaseEditorManager.MediaUpdateTask.2
                @Override // com.miui.gallery.concurrent.FutureHandler
                public void onPostExecute(Future<Void> future2) {
                    if (future2.isCancelled() || MediaUpdateTask.this.mCallback == null) {
                        return;
                    }
                    MediaUpdateTask.this.mCallback.onInsertDone(str);
                }
            });
        }

        public void cancel() {
            Future<Void> future = this.mFuture;
            if (future != null) {
                future.cancel();
                this.mFuture = null;
            }
            this.mCallback = null;
        }
    }
}
