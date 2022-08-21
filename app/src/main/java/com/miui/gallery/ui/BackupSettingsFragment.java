package com.miui.gallery.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.ContentObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.miui.account.AccountHelper;
import com.miui.gallery.R;
import com.miui.gallery.activity.FloatingWindowActivity;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.cleaner.ScanResult;
import com.miui.gallery.cleaner.ScannerManager;
import com.miui.gallery.cleaner.slim.SlimScanner;
import com.miui.gallery.cloud.download.BatchDownloadManager;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.deprecated.GalleryCloudProvider;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.AlertDialogFragment;
import com.miui.gallery.ui.ProcessTask;
import com.miui.gallery.util.FeatureUtil;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.SyncUtil;
import com.miui.os.Rom;
import com.xiaomi.stat.MiStat;
import java.util.HashMap;
import miuix.preference.TextPreference;

/* loaded from: classes2.dex */
public class BackupSettingsFragment extends BasePreferenceFragment implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
    public BroadcastReceiver mAIAlbumStatusObserver = null;
    public CheckBoxPreference mAIAlbumStatusPref;
    public CheckBoxPreference mAutoDownloadPref;
    public CheckBoxPreference mBackupOnlyInWifiPref;
    public TextPreference mCloudAlbumListPref;
    public TextPreference mCloudPrivacyPref;
    public CheckBoxPreference mFaceLocalStatusPref;
    public CheckBoxPreference mOriginDownLoad;
    public PreferenceScreen mPreferenceRoot;
    public TextPreference mSeeMoreSettingsPref;
    public TextPreference mSlimPref;
    public CheckBoxPreference mThumbnailDownLoad;

    public final boolean getAIAlbumStatus() {
        return AIAlbumStatusHelper.isAIAlbumEnabled();
    }

    public final boolean getFaceAlbumStatus() {
        return AIAlbumStatusHelper.isFaceAlbumEnabled();
    }

    public final void updateAIAlbumStatus() {
        CheckBoxPreference checkBoxPreference = this.mAIAlbumStatusPref;
        if (checkBoxPreference != null) {
            checkBoxPreference.setChecked(getAIAlbumStatus());
            return;
        }
        CheckBoxPreference checkBoxPreference2 = this.mFaceLocalStatusPref;
        if (checkBoxPreference2 == null) {
            return;
        }
        checkBoxPreference2.setChecked(getFaceAlbumStatus());
    }

    @Override // androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        getPreferenceManager().setSharedPreferencesName("com.miui.gallery_preferences_new");
        setPreferencesFromResource(R.xml.backup_preferences, str);
        this.mPreferenceRoot = (PreferenceScreen) findPreference("root");
        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference(GalleryPreferences.PrefKeys.SYNC_BACKUP_ONLY_IN_WIFI);
        this.mBackupOnlyInWifiPref = checkBoxPreference;
        checkBoxPreference.setOnPreferenceChangeListener(this);
        TextPreference textPreference = (TextPreference) findPreference("cloud_album_list");
        this.mCloudAlbumListPref = textPreference;
        textPreference.setOnPreferenceClickListener(this);
        TextPreference textPreference2 = (TextPreference) findPreference("slim");
        this.mSlimPref = textPreference2;
        textPreference2.setSummary(getString(R.string.slim_preference_summery, 30));
        this.mSlimPref.setOnPreferenceClickListener(this);
        TextPreference textPreference3 = (TextPreference) findPreference("cloud_privacy");
        this.mCloudPrivacyPref = textPreference3;
        textPreference3.setOnPreferenceClickListener(this);
        TextPreference textPreference4 = (TextPreference) findPreference("see_more_settings");
        this.mSeeMoreSettingsPref = textPreference4;
        textPreference4.setOnPreferenceClickListener(this);
        this.mAIAlbumStatusPref = (CheckBoxPreference) findPreference("cloud_ai_album_status");
        this.mFaceLocalStatusPref = (CheckBoxPreference) findPreference("cloud_face_local_status");
        if (AIAlbumStatusHelper.useNewAIAlbumSwitch()) {
            this.mPreferenceRoot.removePreference(this.mFaceLocalStatusPref);
            this.mFaceLocalStatusPref = null;
            this.mAIAlbumStatusPref.setOnPreferenceChangeListener(this);
            this.mCloudPrivacyPref.setTitle(R.string.cloud_privacy_with_ai_album);
        } else {
            this.mPreferenceRoot.removePreference(this.mAIAlbumStatusPref);
            this.mAIAlbumStatusPref = null;
            this.mFaceLocalStatusPref.setOnPreferenceChangeListener(this);
            this.mCloudPrivacyPref.setTitle(R.string.cloud_privacy_with_face);
        }
        if (Rom.IS_INTERNATIONAL) {
            this.mPreferenceRoot.removePreference(this.mCloudPrivacyPref);
        }
        CheckBoxPreference checkBoxPreference2 = (CheckBoxPreference) findPreference("auto_download_media");
        this.mAutoDownloadPref = checkBoxPreference2;
        checkBoxPreference2.setChecked(GalleryPreferences.Sync.isAutoDownload());
        this.mAutoDownloadPref.setOnPreferenceChangeListener(this);
        CheckBoxPreference checkBoxPreference3 = (CheckBoxPreference) findPreference("download_thumbnail");
        this.mThumbnailDownLoad = checkBoxPreference3;
        checkBoxPreference3.setChecked(DownloadType.THUMBNAIL.equals(GalleryPreferences.Sync.getDownloadType()));
        this.mThumbnailDownLoad.setOnPreferenceClickListener(this);
        CheckBoxPreference checkBoxPreference4 = (CheckBoxPreference) findPreference("download_origin");
        this.mOriginDownLoad = checkBoxPreference4;
        checkBoxPreference4.setChecked(DownloadType.ORIGIN.equals(GalleryPreferences.Sync.getDownloadType()));
        this.mOriginDownLoad.setOnPreferenceClickListener(this);
        if (!FeatureUtil.isSupportBackupOnlyWifi()) {
            this.mPreferenceRoot.removePreference(this.mBackupOnlyInWifiPref);
        }
    }

    public final boolean setDownloadType(DownloadType downloadType) {
        if (downloadType == DownloadType.THUMBNAIL) {
            this.mThumbnailDownLoad.setChecked(true);
            this.mOriginDownLoad.setChecked(false);
            return onDownloadTypePreferenceChange(downloadType);
        } else if (downloadType != DownloadType.ORIGIN) {
            return false;
        } else {
            this.mOriginDownLoad.setChecked(true);
            this.mThumbnailDownLoad.setChecked(false);
            return onDownloadTypePreferenceChange(downloadType);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        SamplingStatHelper.recordPageStart(getActivity(), "backup_settings");
        if (AccountHelper.getXiaomiAccount(getActivity()) != null) {
            boolean z = false;
            if (this.mAIAlbumStatusPref != null) {
                if (this.mPreferenceRoot.findPreference("cloud_ai_album_status") == null) {
                    this.mPreferenceRoot.addPreference(this.mAIAlbumStatusPref);
                }
                this.mAIAlbumStatusPref.setChecked(getAIAlbumStatus());
            } else {
                if (this.mFaceLocalStatusPref != null) {
                    if (this.mPreferenceRoot.findPreference("cloud_face_local_status") == null) {
                        this.mPreferenceRoot.addPreference(this.mFaceLocalStatusPref);
                    }
                    this.mFaceLocalStatusPref.setChecked(getFaceAlbumStatus());
                }
                if (!z || this.mAIAlbumStatusObserver != null) {
                }
                this.mAIAlbumStatusObserver = new BroadcastReceiver() { // from class: com.miui.gallery.ui.BackupSettingsFragment.1
                    @Override // android.content.BroadcastReceiver
                    public void onReceive(Context context, Intent intent) {
                        BackupSettingsFragment.this.updateAIAlbumStatus();
                    }
                };
                AIAlbumStatusHelper.registerAIAlbumStatusReceiver(getActivity(), this.mAIAlbumStatusObserver);
                return;
            }
            z = true;
            if (!z) {
            }
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        if (this.mAIAlbumStatusObserver != null) {
            AIAlbumStatusHelper.unregisterAIAlbumStatusReceiver(getActivity(), this.mAIAlbumStatusObserver);
            this.mAIAlbumStatusObserver = null;
        }
        SamplingStatHelper.recordPageEnd(getActivity(), "backup_settings");
    }

    @Override // androidx.fragment.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return false;
        }
        getActivity().finish();
        return true;
    }

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
        FragmentActivity activity = getActivity();
        boolean needForceSplit = activity instanceof FloatingWindowActivity ? ((FloatingWindowActivity) activity).needForceSplit() : false;
        if (preference == this.mCloudAlbumListPref) {
            TrackController.trackClick("403.22.1.1.11337", AutoTracking.getRef());
            IntentUtil.gotoCloudAlbumListPage(getActivity(), getActivity().getIntent().getBooleanExtra("use_dialog", false), needForceSplit);
            return true;
        } else if (preference == this.mCloudPrivacyPref) {
            IntentUtil.gotoCloudPrivacy(getActivity());
            return true;
        } else if (preference == this.mThumbnailDownLoad) {
            setDownloadType(DownloadType.THUMBNAIL);
            TrackController.trackClick("403.22.1.1.11340", AutoTracking.getRef(), "HD");
            return true;
        } else if (preference == this.mOriginDownLoad) {
            setDownloadType(DownloadType.ORIGIN);
            TrackController.trackClick("403.22.1.1.11340", AutoTracking.getRef(), MiStat.Param.ORIGIN);
            return true;
        } else if (preference == this.mSeeMoreSettingsPref) {
            IntentUtil.gotoSeeMoreSettingsActivity(getActivity(), getActivity().getIntent().getBooleanExtra("use_dialog", false), needForceSplit);
            return true;
        } else if (preference != this.mSlimPref) {
            return true;
        } else {
            doSlimScan();
            return true;
        }
    }

    @Override // androidx.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        FragmentActivity activity = getActivity();
        CheckBoxPreference checkBoxPreference = this.mAIAlbumStatusPref;
        String str = "open";
        if (checkBoxPreference != null && preference == checkBoxPreference) {
            AIAlbumStatusHelper.setAIAlbumLocalStatus(activity, ((Boolean) obj).booleanValue());
            HashMap hashMap = new HashMap(1);
            hashMap.put("to", obj.toString());
            SamplingStatHelper.recordCountEvent("settings", "settings_change_ai_album_switch", hashMap);
            String ref = AutoTracking.getRef();
            if (!((Boolean) obj).booleanValue()) {
                str = "close";
            }
            TrackController.trackClick("403.22.1.1.11338", ref, str);
        } else {
            CheckBoxPreference checkBoxPreference2 = this.mFaceLocalStatusPref;
            if (checkBoxPreference2 != null && preference == checkBoxPreference2) {
                AIAlbumStatusHelper.setFaceAlbumStatus(activity, ((Boolean) obj).booleanValue());
                HashMap hashMap2 = new HashMap(1);
                hashMap2.put("to", obj.toString());
                SamplingStatHelper.recordCountEvent("settings", "settings_change_face_switch", hashMap2);
            } else if (preference == this.mBackupOnlyInWifiPref) {
                String ref2 = AutoTracking.getRef();
                Boolean bool = (Boolean) obj;
                if (!bool.booleanValue()) {
                    str = "close";
                }
                TrackController.trackClick("403.22.1.1.11336", ref2, str);
                return onBackupOnlyWifiChange(bool.booleanValue());
            } else if (preference == this.mAutoDownloadPref) {
                Boolean bool2 = (Boolean) obj;
                BatchDownloadManager.switchAutoDownload(bool2.booleanValue(), getActivity());
                String ref3 = AutoTracking.getRef();
                if (!bool2.booleanValue()) {
                    str = "close";
                }
                TrackController.trackClick("403.22.1.1.11339", ref3, str);
            }
        }
        return true;
    }

    public final void changeBackupOnlyWifi(boolean z) {
        SyncUtil.setBackupOnlyWifi(getActivity(), z);
        getActivity().getContentResolver().notifyChange(GalleryCloudProvider.SYNC_SETTINGS_URI, (ContentObserver) null, false);
    }

    public final boolean onBackupOnlyWifiChange(final boolean z) {
        if (!z) {
            new AlertDialogFragment.Builder().setTitle(getResources().getString(R.string.title_tip)).setMessage(getResources().getString(R.string.backup_only_wifi_dialog_message)).setNegativeButton(getResources().getString(R.string.cancel), null).setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.BackupSettingsFragment.2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    if (BackupSettingsFragment.this.mBackupOnlyInWifiPref != null) {
                        BackupSettingsFragment.this.mBackupOnlyInWifiPref.setChecked(z);
                        BackupSettingsFragment.this.changeBackupOnlyWifi(z);
                    }
                }
            }).create().showAllowingStateLoss(getChildFragmentManager(), "slimDialog");
            return false;
        }
        changeBackupOnlyWifi(z);
        return true;
    }

    public final boolean onDownloadTypePreferenceChange(DownloadType downloadType) {
        if (downloadType == null) {
            return false;
        }
        changeDownloadType(downloadType);
        return true;
    }

    public final void changeDownloadType(DownloadType downloadType) {
        GalleryPreferences.Sync.setDownloadType(downloadType);
        if (BatchDownloadManager.canAutoDownload()) {
            BatchDownloadManager.getInstance().startBatchDownload(getActivity(), true);
        }
    }

    public final void doSlimScan() {
        SamplingStatHelper.recordCountEvent("settings", "settings_slim");
        TrackController.trackClick("403.22.1.1.15961", AutoTracking.getRef());
        ProcessTask processTask = new ProcessTask(new ProcessTask.ProcessCallback<Void, Void, ScanResult>() { // from class: com.miui.gallery.ui.BackupSettingsFragment.3
            @Override // com.miui.gallery.ui.ProcessTask.ProcessCallback
            public ScanResult doProcess(Void[] voidArr) {
                return ((SlimScanner) ScannerManager.getInstance().getScanner(0)).scan();
            }
        });
        processTask.setCompleteListener(new ProcessTask.OnCompleteListener() { // from class: com.miui.gallery.ui.BackupSettingsFragment.4
            @Override // com.miui.gallery.ui.ProcessTask.OnCompleteListener
            public void onCompleteProcess(Object obj) {
                ScanResult scanResult = obj == null ? null : (ScanResult) obj;
                SlimConfirmDialogFragment.newInstance(scanResult == null ? 0L : scanResult.getSize()).showAllowingStateLoss(BackupSettingsFragment.this.getChildFragmentManager(), "SlimConfirmDialogFragment");
            }
        });
        processTask.showProgress(getActivity(), getString(R.string.slim_preference_scanning));
        processTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 62) {
            this.mAutoDownloadPref.setChecked(GalleryPreferences.Sync.isAutoDownload());
            this.mThumbnailDownLoad.setChecked(DownloadType.THUMBNAIL.equals(GalleryPreferences.Sync.getDownloadType()));
            this.mOriginDownLoad.setChecked(DownloadType.ORIGIN.equals(GalleryPreferences.Sync.getDownloadType()));
            return;
        }
        super.onActivityResult(i, i2, intent);
    }
}
