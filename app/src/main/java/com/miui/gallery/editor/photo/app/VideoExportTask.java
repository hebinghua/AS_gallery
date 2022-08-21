package com.miui.gallery.editor.photo.app;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.text.format.DateFormat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.FragmentActivity;
import ch.qos.logback.core.joran.action.Action;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.provider.CloudUtils;
import com.miui.gallery.scanner.core.ScannerEngine;
import com.miui.gallery.scanner.core.model.SaveParams;
import com.miui.gallery.scanner.utils.SaveToCloudUtil;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.constants.StorageConstants;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.FileUtils;
import com.miui.gallery.util.IoUtils;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.MiStat;
import java.io.Closeable;
import java.io.File;
import java.util.Locale;

/* loaded from: classes2.dex */
public class VideoExportTask {
    public FragmentActivity mActivity;
    public VideoExportFragment mExportFragment;
    public Uri mExportUri;
    public boolean mExternalCall;
    public long mSecretId;
    public Uri mSource;

    public void onPostExport(boolean z) {
    }

    public boolean onExport(DraftManager draftManager, boolean z) {
        if (this.mExternalCall) {
            DefaultLogger.d("VideoExportTask", "onExport not support export external");
            return false;
        }
        String path = this.mExportUri.getPath();
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("VideoExportTask", "onExport");
        if (z) {
            DefaultLogger.d("VideoExportTask", "internal call, scan to db :%s", path);
            if (draftManager.isSecret()) {
                long mediaId = SaveToCloudUtil.saveToCloudDB(this.mActivity, new SaveParams.Builder().setSaveFile(new File(path)).setAlbumId(-1000L).setLocalFlag(8).setCredible(true).build()).getMediaId();
                this.mSecretId = mediaId;
                if (mediaId <= 0) {
                    DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(path, IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
                    if (documentFile == null) {
                        return false;
                    }
                    documentFile.delete();
                    return false;
                }
            } else {
                ScannerEngine.getInstance().scanFile(this.mActivity, path, 13);
                DocumentFile documentFile2 = StorageSolutionProvider.get().getDocumentFile(path, IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag);
                if (documentFile2 != null) {
                    StorageSolutionProvider.get().apply(documentFile2);
                }
                if (draftManager.isFavorite()) {
                    CloudUtils.addToFavoritesByPath(this.mActivity, path);
                }
            }
            DefaultLogger.d("VideoExportTask", "internal call, scan to db done secretId:%d", Long.valueOf(this.mSecretId));
        }
        return z;
    }

    public void prepareToExport() {
        if (this.mExternalCall) {
            DefaultLogger.d("VideoExportTask", "prepareToExport not support export external");
        } else {
            this.mExportUri = Uri.fromFile(createExportFile(this.mSource));
        }
    }

    public void showExportDialog() {
        if (this.mExternalCall) {
            DefaultLogger.d("VideoExportTask", "showExportDialog not support export external");
        } else if (this.mExportFragment != null) {
        } else {
            VideoExportFragment videoExportFragment = new VideoExportFragment();
            this.mExportFragment = videoExportFragment;
            videoExportFragment.show(this.mActivity.getSupportFragmentManager().beginTransaction().addToBackStack(null), (String) null);
        }
    }

    public void closeExportDialog() {
        VideoExportFragment videoExportFragment = this.mExportFragment;
        if (videoExportFragment == null || !videoExportFragment.isAdded()) {
            return;
        }
        this.mExportFragment.dismissAllowingStateLoss();
        this.mExportFragment = null;
    }

    public void setProgress(int i) {
        VideoExportFragment videoExportFragment = this.mExportFragment;
        if (videoExportFragment != null) {
            videoExportFragment.setProgress(i);
        }
    }

    public static VideoExportTask from(FragmentActivity fragmentActivity) {
        Intent intent = fragmentActivity.getIntent();
        if (intent == null || intent.getData() == null) {
            return null;
        }
        VideoExportTask videoExportTask = new VideoExportTask();
        videoExportTask.mActivity = fragmentActivity;
        videoExportTask.mExternalCall = TextUtils.equals("android.intent.action.EDIT", intent.getAction());
        Uri data = intent.getData();
        videoExportTask.mSource = data;
        DefaultLogger.d("VideoExportTask", "editting %s", data);
        return videoExportTask;
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
    public final File createExportFile(Uri uri) {
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
                        IoUtils.close("VideoExportTask", closeable);
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
                            DefaultLogger.d("VideoExportTask", "receive an exception when look for parent for %s", uri);
                            equals = equals;
                            IoUtils.close("VideoExportTask", equals);
                            return createFile(str);
                        }
                    }
                    IoUtils.close("VideoExportTask", equals);
                }
            } catch (Throwable th2) {
                th = th2;
                closeable = equals;
            }
        }
        return createFile(str);
    }

    public final File createFile(String str) {
        String format = String.format(Locale.US, "VID_%s.%s", DateFormat.format("yyyyMMdd_HHmmss", System.currentTimeMillis()), "mp4");
        if (!TextUtils.isEmpty(str) || (str = StorageUtils.getPathInPriorStorage(StorageConstants.RELATIVE_DIRECTORY_CREATIVE)) != null) {
            StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
            String concat = BaseFileUtils.concat(str, format);
            IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.INSERT;
            if (!storageStrategyManager.checkPermission(concat, permission).granted) {
                str = StorageUtils.getPathInPrimaryStorage(StorageUtils.getRelativePath(GalleryApp.sGetAndroidContext(), str));
                if (!StorageSolutionProvider.get().checkPermission(BaseFileUtils.concat(str, format), permission).granted && (str = StorageUtils.getPathInPriorStorage(StorageConstants.RELATIVE_DIRECTORY_CREATIVE)) == null) {
                    return null;
                }
                StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("VideoExportTask", "createFile"));
            }
            return FileUtils.forceCreate(str, format, 0);
        }
        return null;
    }
}
