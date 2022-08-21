package com.miui.backup;

import android.app.backup.FullBackupDataOutput;
import android.content.Context;
import android.os.ParcelFileDescriptor;
import java.io.IOException;

/* loaded from: classes.dex */
public interface IBackupAgentDelegate {
    default void attachBaseContext(Context context) {
    }

    int getVersion(int i);

    int onAttachRestore(BackupMeta backupMeta, ParcelFileDescriptor parcelFileDescriptor, String str);

    default void onCreate() {
    }

    int onDataRestore(BackupMeta backupMeta, ParcelFileDescriptor parcelFileDescriptor) throws IOException;

    default void onDestroy() {
    }

    int onFullBackup(ParcelFileDescriptor parcelFileDescriptor, int i) throws IOException;

    int onRestoreEnd(BackupMeta backupMeta);

    int tarAttaches(String str, FullBackupDataOutput fullBackupDataOutput, int i);
}
