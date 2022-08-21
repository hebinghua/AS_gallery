package com.miui.backup;

import android.app.backup.FullBackupDataOutput;
import android.content.Context;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import java.io.IOException;
import miui.app.backup.FullBackupAgent;

/* loaded from: classes.dex */
public class MiuiBackupAgentImpl extends FullBackupAgent {
    private static final String TAG = "MiuiBackupAgentImpl";
    private IBackupAgentDelegate mDelegate;

    public MiuiBackupAgentImpl(IBackupAgentDelegate iBackupAgentDelegate) {
        this.mDelegate = iBackupAgentDelegate;
    }

    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        this.mDelegate.attachBaseContext(context);
    }

    public int tarAttaches(String str, FullBackupDataOutput fullBackupDataOutput, int i) throws IOException {
        super.tarAttaches(str, fullBackupDataOutput, i);
        return this.mDelegate.tarAttaches(str, fullBackupDataOutput, i);
    }

    public int getVersion(int i) {
        return this.mDelegate.getVersion(i);
    }

    public int onDataRestore(miui.app.backup.BackupMeta backupMeta, ParcelFileDescriptor parcelFileDescriptor) throws IOException {
        return this.mDelegate.onDataRestore(BackupMetaUtils.translate(backupMeta), parcelFileDescriptor);
    }

    public int onAttachRestore(miui.app.backup.BackupMeta backupMeta, ParcelFileDescriptor parcelFileDescriptor, String str) throws IOException {
        return this.mDelegate.onAttachRestore(BackupMetaUtils.translate(backupMeta), parcelFileDescriptor, str);
    }

    public int onRestoreEnd(miui.app.backup.BackupMeta backupMeta) throws IOException {
        return this.mDelegate.onRestoreEnd(BackupMetaUtils.translate(backupMeta));
    }

    public int onFullBackup(ParcelFileDescriptor parcelFileDescriptor, int i) throws IOException {
        SignatureBackupHelper.backupSignature(this, super.getFilesDir().getAbsolutePath());
        return this.mDelegate.onFullBackup(parcelFileDescriptor, i);
    }

    public void addAttachedFile(String str) {
        super.addAttachedFile(str);
    }

    public void onCreate() {
        this.mDelegate.onCreate();
    }

    public void onDestroy() {
        this.mDelegate.onDestroy();
    }

    public void onOriginalAttachesRestore(miui.app.backup.BackupMeta backupMeta, ParcelFileDescriptor parcelFileDescriptor, long j, int i, String str, String str2, long j2, long j3) throws IOException {
        Log.w(TAG, "intercept original onRestoreFile, type=" + i + " domain=" + str + " path=" + str2);
        if (i == 1) {
            consumeBytesFromPipe(parcelFileDescriptor, j);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0024, code lost:
        android.util.Log.w(com.miui.backup.MiuiBackupAgentImpl.TAG, "Incomplete read: expected " + r2 + " but got " + (r8 - r2));
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void consumeBytesFromPipe(android.os.ParcelFileDescriptor r7, long r8) throws java.io.IOException {
        /*
            r6 = this;
            java.io.FileInputStream r0 = new java.io.FileInputStream
            java.io.FileDescriptor r7 = r7.getFileDescriptor()
            r0.<init>(r7)
            r7 = 32768(0x8000, float:4.5918E-41)
            byte[] r1 = new byte[r7]     // Catch: java.lang.Throwable -> L4b
            r2 = r8
        Lf:
            r4 = 0
            int r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r4 <= 0) goto L47
            long r4 = (long) r7     // Catch: java.lang.Throwable -> L4b
            int r4 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r4 <= 0) goto L1c
            r4 = r7
            goto L1d
        L1c:
            int r4 = (int) r2     // Catch: java.lang.Throwable -> L4b
        L1d:
            r5 = 0
            int r4 = r0.read(r1, r5, r4)     // Catch: java.lang.Throwable -> L4b
            if (r4 > 0) goto L44
            java.lang.String r7 = "MiuiBackupAgentImpl"
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L4b
            r1.<init>()     // Catch: java.lang.Throwable -> L4b
            java.lang.String r4 = "Incomplete read: expected "
            r1.append(r4)     // Catch: java.lang.Throwable -> L4b
            r1.append(r2)     // Catch: java.lang.Throwable -> L4b
            java.lang.String r4 = " but got "
            r1.append(r4)     // Catch: java.lang.Throwable -> L4b
            long r8 = r8 - r2
            r1.append(r8)     // Catch: java.lang.Throwable -> L4b
            java.lang.String r8 = r1.toString()     // Catch: java.lang.Throwable -> L4b
            android.util.Log.w(r7, r8)     // Catch: java.lang.Throwable -> L4b
            goto L47
        L44:
            long r4 = (long) r4
            long r2 = r2 - r4
            goto Lf
        L47:
            r0.close()
            return
        L4b:
            r7 = move-exception
            r0.close()     // Catch: java.lang.Throwable -> L50
            goto L54
        L50:
            r8 = move-exception
            r7.addSuppressed(r8)
        L54:
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.backup.MiuiBackupAgentImpl.consumeBytesFromPipe(android.os.ParcelFileDescriptor, long):void");
    }
}
