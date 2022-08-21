package com.miui.backup;

import android.app.backup.BackupAgent;
import android.content.Context;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.miui.core.SdkHelper;
import java.lang.reflect.Method;
import miui.app.backup.BackupManager;
import miui.app.backup.FullBackupAgent;

/* loaded from: classes.dex */
public class BackupCompatHelper {
    private static final String TAG = "BackupCompatHelper";

    private BackupCompatHelper() {
    }

    public static BackupAgent createBackupAgentImpl(IBackupAgentDelegate iBackupAgentDelegate) {
        if (SdkHelper.IS_MIUI) {
            return new MiuiBackupAgentImpl(iBackupAgentDelegate);
        }
        return null;
    }

    public static void invokeOnRestoreFile(BackupAgent backupAgent, ParcelFileDescriptor parcelFileDescriptor, long j, int i, String str, String str2, long j2, long j3) {
        Class<FullBackupAgent> cls;
        try {
            if (SdkHelper.IS_MIUI) {
                cls = FullBackupAgent.class;
            } else {
                cls = BackupAgent.class;
            }
            Class<?> cls2 = Long.TYPE;
            Method declaredMethod = cls.getDeclaredMethod("onRestoreFile", ParcelFileDescriptor.class, cls2, Integer.TYPE, String.class, String.class, cls2, cls2);
            declaredMethod.setAccessible(true);
            declaredMethod.invoke(backupAgent, parcelFileDescriptor, Long.valueOf(j), Integer.valueOf(i), str, str2, Long.valueOf(j2), Long.valueOf(j3));
        } catch (IllegalArgumentException e) {
            Log.e(TAG, "IllegalArgumentException", e);
        } catch (NoSuchMethodException e2) {
            Log.e(TAG, "NoSuchMethodException", e2);
        } catch (Exception e3) {
            Log.e(TAG, "", e3);
        }
    }

    public static void setIsNeedBeKilled(Context context) {
        if (SdkHelper.IS_MIUI) {
            try {
                BackupManager backupManager = BackupManager.getBackupManager(context);
                if (backupManager == null) {
                    return;
                }
                backupManager.setIsNeedBeKilled(true);
            } catch (Exception e) {
                Log.e(TAG, "EncounterErrorWhenKillApp", e);
            }
        }
    }
}
