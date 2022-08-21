package com.miui.gallery.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.cloud.ClearDataManager;
import com.miui.gallery.cloud.download.BatchDownloadManager;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.ui.BackupSettingsFragment;
import com.miui.gallery.ui.ProcessTask;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.deprecated.Preference;
import com.miui.gallery.widget.TimerDialog;

/* loaded from: classes.dex */
public class BackupSettingsActivity extends FloatingWindowActivity {
    public static final int[] ORIGIN_CONFIRM_MESSAGE = {R.string.sync_backup_disable_confirm_message_downloading, 0, R.string.sync_backup_disable_confirm_message_wait, R.string.sync_backup_disable_confirm_message_wait};
    public View mDisableBackupButton;
    public BackupSettingsFragment mFragment;

    /* renamed from: $r8$lambda$CVdAsaTQ8fVxcF-Eilq-5IFawmM */
    public static /* synthetic */ void m455$r8$lambda$CVdAsaTQ8fVxcFEilq5IFawmM(BackupSettingsActivity backupSettingsActivity, Void r1) {
        backupSettingsActivity.lambda$disableAutoBackup$1(r1);
    }

    @Override // com.miui.gallery.activity.FloatingWindowActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.backup_settings);
        this.mFragment = (BackupSettingsFragment) getSupportFragmentManager().findFragmentById(R.id.backup_setting);
        View findViewById = findViewById(R.id.backup);
        this.mDisableBackupButton = findViewById;
        findViewById.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.activity.BackupSettingsActivity.1
            {
                BackupSettingsActivity.this = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                BackupSettingsActivity.this.doConfirmQuery();
            }
        });
    }

    public final void doConfirmQuery() {
        ProcessTask processTask = new ProcessTask(new ProcessTask.ProcessCallback<Void, Void, Integer>() { // from class: com.miui.gallery.activity.BackupSettingsActivity.2
            {
                BackupSettingsActivity.this = this;
            }

            @Override // com.miui.gallery.ui.ProcessTask.ProcessCallback
            public Integer doProcess(Void[] voidArr) {
                return Integer.valueOf(((Integer) SafeDBUtil.safeQuery(BackupSettingsActivity.this, GalleryContract.Cloud.CLOUD_URI, new String[]{"localFile"}, "(localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) AND serverType IN (1,2) AND (localFile IS NULL OR localFile = '') AND (serverId IS NOT NULL)", (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Integer>() { // from class: com.miui.gallery.activity.BackupSettingsActivity.2.1
                    {
                        AnonymousClass2.this = this;
                    }

                    @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                    /* renamed from: handle */
                    public Integer mo1808handle(Cursor cursor) {
                        boolean isAutoDownloadOrigin = BackupSettingsActivity.this.isAutoDownloadOrigin();
                        int i = 3;
                        if (cursor == null) {
                            return 3;
                        }
                        if (cursor.getCount() == 0) {
                            return 1;
                        }
                        while (cursor.moveToNext()) {
                            if (cursor.getString(0) == null) {
                                if (isAutoDownloadOrigin) {
                                    i = 0;
                                }
                                return Integer.valueOf(i);
                            }
                        }
                        return 2;
                    }
                })).intValue());
            }
        });
        processTask.setCompleteListener(new ProcessTask.OnCompleteListener() { // from class: com.miui.gallery.activity.BackupSettingsActivity.3
            {
                BackupSettingsActivity.this = this;
            }

            @Override // com.miui.gallery.ui.ProcessTask.OnCompleteListener
            public void onCompleteProcess(Object obj) {
                if (obj == null) {
                    return;
                }
                BackupSettingsActivity.this.showConfirmDialog(((Integer) obj).intValue());
            }
        });
        processTask.showProgress(this, getString(R.string.cloud_bacup_disable_processing));
        processTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public final boolean isAutoDownloadOrigin() {
        return GalleryPreferences.Sync.isAutoDownload() && GalleryPreferences.Sync.getDownloadType() == DownloadType.ORIGIN;
    }

    public final void showConfirmDialog(int i) {
        if (i == 1) {
            disableAutoBackup();
        } else {
            new TimerDialog.Builder(this).setTitle(R.string.sync_backup_disable_confirm_title).setMessage(ORIGIN_CONFIRM_MESSAGE[i]).setConfirmTime(6000L).setPositiveButton(17039370, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.activity.BackupSettingsActivity.4
                {
                    BackupSettingsActivity.this = this;
                }

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i2) {
                    BackupSettingsActivity.this.disableAutoBackup();
                }
            }).setNegativeButton(17039360, null).build().show();
        }
    }

    public final void disableAutoBackup() {
        SyncUtil.setSyncAutomatically(this, false, 60);
        BatchDownloadManager.getInstance().stopBatchDownload(GalleryApp.sGetAndroidContext());
        ProcessTask processTask = new ProcessTask(BackupSettingsActivity$$ExternalSyntheticLambda1.INSTANCE);
        processTask.setCompleteListener(new ProcessTask.OnCompleteListener() { // from class: com.miui.gallery.activity.BackupSettingsActivity$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.ui.ProcessTask.OnCompleteListener
            public final void onCompleteProcess(Object obj) {
                BackupSettingsActivity.m455$r8$lambda$CVdAsaTQ8fVxcFEilq5IFawmM(BackupSettingsActivity.this, (Void) obj);
            }
        });
        processTask.showProgress(this, getString(R.string.cloud_bacup_disable_processing));
        processTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public static /* synthetic */ Void lambda$disableAutoBackup$0(Void[] voidArr) {
        Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
        Preference.setSyncShouldClearDataBase(true);
        ClearDataManager.getInstance().clearDataBaseIfNeeded(sGetAndroidContext, AccountCache.getAccount());
        return null;
    }

    public /* synthetic */ void lambda$disableAutoBackup$1(Void r3) {
        TrackController.trackClick("403.22.1.1.11335", AutoTracking.getRef(), "close");
        finish();
    }

    @Override // com.miui.gallery.app.activity.MiuiActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        BackupSettingsFragment backupSettingsFragment = this.mFragment;
        if (backupSettingsFragment != null) {
            backupSettingsFragment.onActivityResult(i, i2, intent);
        }
    }
}
