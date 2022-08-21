package com.miui.gallery.editor.photo.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Process;
import android.text.TextUtils;
import android.text.format.DateFormat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.FragmentActivity;
import ch.qos.logback.core.joran.action.Action;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.provider.CloudUtils;
import com.miui.gallery.scanner.core.ScannerEngine;
import com.miui.gallery.scanner.core.model.SaveParams;
import com.miui.gallery.scanner.utils.SaveToCloudUtil;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.constants.StorageConstants;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.FileUtils;
import com.miui.gallery.util.IoUtils;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.MiStat;
import java.io.Closeable;
import java.io.File;
import java.util.Locale;

/* loaded from: classes2.dex */
public class ExportTask {
    public FragmentActivity mActivity;
    public ExportFragment mExportFragment;
    public Uri mExportUri;
    public boolean mExternalCall;
    public long mSecretId;
    public Uri mSource;

    public void onCancelled(boolean z) {
    }

    public boolean onExport(DraftManager draftManager, boolean z) {
        String path = this.mExportUri.getPath();
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("ExportTask_", "onExport");
        if (z && !this.mExternalCall) {
            DefaultLogger.d("ExportTask_", "internal call, scan to db :%s", path);
            if (draftManager.isSecret()) {
                long mediaId = SaveToCloudUtil.saveToCloudDB(this.mActivity, new SaveParams.Builder().setSaveFile(new File(path)).setAlbumId(-1000L).setLocalFlag(8).setCredible(true).build()).getMediaId();
                this.mSecretId = mediaId;
                if (mediaId <= 0) {
                    DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(path, IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
                    if (documentFile != null) {
                        documentFile.delete();
                    }
                    return false;
                }
                DefaultLogger.d("ExportTask_", "internal call, scan to db done secretId:%d", Long.valueOf(mediaId));
                return true;
            }
            ScannerEngine.getInstance().scanFile(this.mActivity, path, 13);
            DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(path, IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag);
            if (documentFile2 != null) {
                StorageSolutionProvider.get().apply(documentFile2);
            }
            if (draftManager.isFavorite()) {
                CloudUtils.addToFavoritesByPath(this.mActivity, path);
            }
        }
        if (z && this.mExternalCall && MiStat.Param.CONTENT.equals(this.mSource.getScheme())) {
            DocumentFile documentFile3 = StorageSolutionProvider.get().getDocumentFile(path, IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag);
            if (documentFile3 != null) {
                StorageSolutionProvider.get().apply(documentFile3);
            }
            DefaultLogger.d("ExportTask_", "external call, update media store:%s", this.mExportUri.toString());
        }
        if (this.mSource == this.mExportUri) {
            long[] jArr = new long[0];
            try {
                jArr = CloudUtils.deleteCloudByPath(this.mActivity, 55, path);
            } catch (StoragePermissionMissingException e) {
                DefaultLogger.e("ExportTask_", e);
            }
            DefaultLogger.d("ExportTask_", "deleteCloudByPath path:%s,result:%s", path, (jArr == null || jArr.length <= 0) ? null : String.valueOf(jArr[0]));
        }
        return z;
    }

    public void onPostExport(boolean z) {
        if (!z || !this.mExternalCall || this.mSource.equals(this.mExportUri) || !Action.FILE_ATTRIBUTE.equals(this.mExportUri.getScheme())) {
            return;
        }
        FragmentActivity fragmentActivity = this.mActivity;
        ToastUtils.makeTextLong(fragmentActivity, fragmentActivity.getString(R.string.photo_save_to_msg, new Object[]{this.mExportUri.getPath()}));
    }

    public void prepareToExport(DraftManager draftManager) {
        if (!this.mExternalCall) {
            prepareToExport(draftManager, true, this.mSource);
            DefaultLogger.d("ExportTask_", "export to a new file %s", this.mExportUri);
        } else if (Action.FILE_ATTRIBUTE.equals(this.mSource.getScheme()) || (MiStat.Param.CONTENT.equals(this.mSource.getScheme()) && "media".equals(this.mSource.getAuthority()))) {
            prepareToExport(draftManager, false, null);
            DefaultLogger.d("ExportTask_", "export to origin file or media uri %s", this.mExportUri);
        } else {
            if (this.mActivity.checkUriPermission(this.mSource, Process.myPid(), Process.myUid(), 2) == 0) {
                prepareToExport(draftManager, false, null);
                DefaultLogger.d("ExportTask_", "export to origin uri %s", this.mExportUri);
                return;
            }
            Uri fromFile = Uri.fromFile(createFile(draftManager, null));
            this.mExportUri = fromFile;
            DefaultLogger.d("ExportTask_", "export to a specified dir %s", fromFile);
        }
    }

    public final void prepareToExport(DraftManager draftManager, boolean z, Uri uri) {
        if (z) {
            this.mExportUri = Uri.fromFile(createExportFile(draftManager, uri));
        } else {
            this.mExportUri = this.mSource;
        }
    }

    public void showExportDialog() {
        ExportFragment exportFragment = new ExportFragment();
        this.mExportFragment = exportFragment;
        exportFragment.setCancelable(false);
        this.mExportFragment.show(this.mActivity.getSupportFragmentManager().beginTransaction().addToBackStack(null), (String) null);
    }

    public void closeExportDialog() {
        ExportFragment exportFragment = this.mExportFragment;
        if (exportFragment == null || !exportFragment.isAdded()) {
            return;
        }
        this.mExportFragment.dismissAllowingStateLoss();
        this.mExportFragment = null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v11, types: [android.database.Cursor] */
    /* JADX WARN: Type inference failed for: r1v12 */
    /* JADX WARN: Type inference failed for: r1v13 */
    /* JADX WARN: Type inference failed for: r1v14 */
    /* JADX WARN: Type inference failed for: r1v3, types: [boolean] */
    /* JADX WARN: Type inference failed for: r1v4 */
    /* JADX WARN: Type inference failed for: r1v5 */
    /* JADX WARN: Type inference failed for: r1v6 */
    /* JADX WARN: Type inference failed for: r1v7 */
    /* JADX WARN: Type inference failed for: r1v8, types: [java.io.Closeable] */
    public final File createExportFile(DraftManager draftManager, Uri uri) {
        String str = null;
        str = null;
        str = null;
        str = null;
        Closeable closeable = null;
        if (Action.FILE_ATTRIBUTE.equals(uri.getScheme())) {
            str = new File(uri.getPath()).getParent();
        } else {
            ?? equals = MiStat.Param.CONTENT.equals(uri.getScheme());
            try {
                if (equals != 0) {
                    try {
                        equals = this.mActivity.getContentResolver().query(uri, new String[]{"_data"}, null, null, null);
                    } catch (Exception unused) {
                        equals = 0;
                    } catch (Throwable th) {
                        th = th;
                        IoUtils.close("ExportTask_", closeable);
                        throw th;
                    }
                    if (equals != 0) {
                        try {
                            boolean moveToNext = equals.moveToNext();
                            equals = equals;
                            if (moveToNext) {
                                str = new File(equals.getString(0)).getParent();
                                equals = equals;
                            }
                        } catch (Exception unused2) {
                            DefaultLogger.d("ExportTask_", "receive an exception when look for parent for %s", uri);
                            equals = equals;
                            IoUtils.close("ExportTask_", equals);
                            return createFile(draftManager, str);
                        }
                    }
                    IoUtils.close("ExportTask_", equals);
                }
            } catch (Throwable th2) {
                th = th2;
                closeable = equals;
            }
        }
        return createFile(draftManager, str);
    }

    public final File createFile(DraftManager draftManager, String str) {
        String format = String.format(Locale.US, "IMG_%s.%s", DateFormat.format("yyyyMMdd_HHmmss", System.currentTimeMillis()), draftManager.getExportFileSuffix());
        if (TextUtils.isEmpty(str)) {
            str = StorageUtils.getPathInPriorStorage(StorageConstants.RELATIVE_DIRECTORY_CREATIVE);
            if (str == null) {
                return null;
            }
            StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("ExportTask_", "createFile"));
        }
        StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
        String concat = BaseFileUtils.concat(str, format);
        IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.INSERT;
        if (!storageStrategyManager.checkPermission(concat, permission).granted) {
            str = StorageUtils.getPathInPrimaryStorage(StorageUtils.getRelativePath(GalleryApp.sGetAndroidContext(), str));
            if (!StorageSolutionProvider.get().checkPermission(BaseFileUtils.concat(str, format), permission).granted && (str = StorageUtils.getPathInPriorStorage(StorageConstants.RELATIVE_DIRECTORY_CREATIVE)) == null) {
                return null;
            }
            StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("ExportTask_", "createFile"));
        }
        return FileUtils.forceCreate(str, format, 0);
    }

    public static ExportTask from(FragmentActivity fragmentActivity) {
        Intent intent = fragmentActivity.getIntent();
        if (intent == null || intent.getData() == null) {
            return null;
        }
        ExportTask exportTask = new ExportTask();
        exportTask.mActivity = fragmentActivity;
        exportTask.mExternalCall = TextUtils.equals("android.intent.action.EDIT", intent.getAction());
        Uri data = intent.getData();
        exportTask.mSource = data;
        DefaultLogger.d("ExportTask_", "editting %s", data);
        return exportTask;
    }

    public boolean isExternalCall() {
        return this.mExternalCall;
    }

    public long getSecretId() {
        return this.mSecretId;
    }

    public Uri getExportUri() {
        Uri uri = this.mExportUri;
        if (uri != null) {
            return uri;
        }
        throw new IllegalStateException("call prepareToExport first");
    }

    public Uri getSource() {
        return this.mSource;
    }
}
