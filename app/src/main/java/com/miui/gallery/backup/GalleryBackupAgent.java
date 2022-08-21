package com.miui.gallery.backup;

import android.app.backup.BackupAgent;
import android.app.backup.BackupDataInput;
import android.app.backup.BackupDataOutput;
import android.app.backup.FullBackupDataOutput;
import android.content.Context;
import android.os.ParcelFileDescriptor;
import com.miui.backup.BackupCompatHelper;
import com.miui.backup.BackupErrorCode;
import com.miui.backup.BackupMeta;
import com.miui.backup.IBackupAgentDelegate;
import com.miui.backup.SignatureBackupHelper;
import com.miui.gallery.backup.GalleryBackupProtos;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class GalleryBackupAgent extends BackupAgent {
    private static final int GALLERY_BACKUP_DATA_VERSION = 2;
    public static final int GALLERY_BACKUP_FEATURE_DEFAULT = 0;
    public static final int GALLERY_BACKUP_FEATURE_MI_MOVER = 1;
    private static final String TAG = "GalleryBackupAgent";
    private BackupAgent mBackupAgentImpl;

    @Override // android.app.backup.BackupAgent
    public void onBackup(ParcelFileDescriptor parcelFileDescriptor, BackupDataOutput backupDataOutput, ParcelFileDescriptor parcelFileDescriptor2) throws IOException {
    }

    @Override // android.app.backup.BackupAgent
    public void onRestore(BackupDataInput backupDataInput, int i, ParcelFileDescriptor parcelFileDescriptor) throws IOException {
    }

    private void ensureBackupAgent() {
        if (this.mBackupAgentImpl == null) {
            this.mBackupAgentImpl = BackupCompatHelper.createBackupAgentImpl(new IBackupAgentDelegate() { // from class: com.miui.gallery.backup.GalleryBackupAgent.1
                private Context mContext;

                @Override // com.miui.backup.IBackupAgentDelegate
                public int getVersion(int i) {
                    return 2;
                }

                @Override // com.miui.backup.IBackupAgentDelegate
                public void attachBaseContext(Context context) {
                    this.mContext = context;
                }

                @Override // com.miui.backup.IBackupAgentDelegate
                public int onFullBackup(ParcelFileDescriptor parcelFileDescriptor, int i) throws IOException {
                    DefaultLogger.d(GalleryBackupAgent.TAG, "onFullBackup start.");
                    long currentTimeMillis = System.currentTimeMillis();
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(parcelFileDescriptor.getFileDescriptor());
                        GalleryBackupHelper.backup(i).writeTo(fileOutputStream);
                        fileOutputStream.close();
                        DefaultLogger.d(GalleryBackupAgent.TAG, "onFullBackup done, cost [%d] ms.", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                        return BackupErrorCode.ERR_NONE();
                    } catch (Throwable th) {
                        DefaultLogger.d(GalleryBackupAgent.TAG, "onFullBackup done, cost [%d] ms.", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                        throw th;
                    }
                }

                @Override // com.miui.backup.IBackupAgentDelegate
                public int tarAttaches(String str, FullBackupDataOutput fullBackupDataOutput, int i) {
                    return BackupErrorCode.ERR_NONE();
                }

                @Override // com.miui.backup.IBackupAgentDelegate
                public int onDataRestore(BackupMeta backupMeta, ParcelFileDescriptor parcelFileDescriptor) throws IOException {
                    DefaultLogger.d(GalleryBackupAgent.TAG, "onDataRestore start.");
                    long currentTimeMillis = System.currentTimeMillis();
                    try {
                        FileInputStream fileInputStream = new FileInputStream(parcelFileDescriptor.getFileDescriptor());
                        GalleryBackupHelper.restore(GalleryBackupProtos.BackupMessage.parseFrom(fileInputStream));
                        fileInputStream.close();
                        DefaultLogger.d(GalleryBackupAgent.TAG, "onDataRestore done, cost [%d] ms.", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                        return BackupErrorCode.ERR_NONE();
                    } catch (Throwable th) {
                        DefaultLogger.d(GalleryBackupAgent.TAG, "onDataRestore done, cost [%d] ms.", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                        throw th;
                    }
                }

                @Override // com.miui.backup.IBackupAgentDelegate
                public int onAttachRestore(BackupMeta backupMeta, ParcelFileDescriptor parcelFileDescriptor, String str) {
                    SignatureBackupHelper.restoreSignature(GalleryBackupAgent.this.getFilesDir().getAbsolutePath(), backupMeta, parcelFileDescriptor, str);
                    return BackupErrorCode.ERR_NONE();
                }

                @Override // com.miui.backup.IBackupAgentDelegate
                public int onRestoreEnd(BackupMeta backupMeta) {
                    BackupCompatHelper.setIsNeedBeKilled(this.mContext);
                    return BackupErrorCode.ERR_NONE();
                }
            });
        }
    }

    @Override // android.app.backup.BackupAgent
    public void onCreate() {
        BackupAgent backupAgent = this.mBackupAgentImpl;
        if (backupAgent != null) {
            backupAgent.onCreate();
        }
    }

    @Override // android.content.ContextWrapper
    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        ensureBackupAgent();
        if (this.mBackupAgentImpl != null) {
            try {
                Method declaredMethod = Class.forName("android.content.ContextWrapper").getDeclaredMethod("attachBaseContext", Context.class);
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(this.mBackupAgentImpl, context);
            } catch (Exception e) {
                DefaultLogger.e(TAG, e);
            }
        }
    }

    @Override // android.app.backup.BackupAgent
    public final void onFullBackup(FullBackupDataOutput fullBackupDataOutput) throws IOException {
        BackupAgent backupAgent = this.mBackupAgentImpl;
        if (backupAgent != null) {
            backupAgent.onFullBackup(fullBackupDataOutput);
        }
    }

    public final void onRestoreFile(ParcelFileDescriptor parcelFileDescriptor, long j, int i, String str, String str2, long j2, long j3) throws IOException {
        BackupAgent backupAgent = this.mBackupAgentImpl;
        if (backupAgent != null) {
            BackupCompatHelper.invokeOnRestoreFile(backupAgent, parcelFileDescriptor, j, i, str, str2, j2, j3);
        }
    }
}
