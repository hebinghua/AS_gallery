package com.miui.backup;

/* loaded from: classes.dex */
class XmsBackupErrorCode implements IBackupErrorCode {
    @Override // com.miui.backup.IBackupErrorCode
    public int ERR_AUTHENTICATION_FAILED() {
        return 3;
    }

    @Override // com.miui.backup.IBackupErrorCode
    public int ERR_BAKFILE_BROKEN() {
        return 6;
    }

    @Override // com.miui.backup.IBackupErrorCode
    public int ERR_BINDER_DIED() {
        return 8;
    }

    @Override // com.miui.backup.IBackupErrorCode
    public int ERR_IO_PERMISSION() {
        return 7;
    }

    @Override // com.miui.backup.IBackupErrorCode
    public int ERR_NONE() {
        return 0;
    }

    @Override // com.miui.backup.IBackupErrorCode
    public int ERR_NO_BACKUPAGENT() {
        return 2;
    }

    @Override // com.miui.backup.IBackupErrorCode
    public int ERR_UNKNOWN() {
        return 1;
    }

    @Override // com.miui.backup.IBackupErrorCode
    public int ERR_VERSION_TOO_OLD() {
        return 5;
    }

    @Override // com.miui.backup.IBackupErrorCode
    public int ERR_VERSION_UNSUPPORTED() {
        return 4;
    }
}
