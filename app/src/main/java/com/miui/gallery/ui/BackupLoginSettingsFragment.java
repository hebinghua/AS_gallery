package com.miui.gallery.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.miui.gallery.R;
import com.miui.gallery.cloud.download.BatchDownloadManager;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.stat.SamplingStatHelper;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class BackupLoginSettingsFragment extends BasePreferenceFragment implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
    public BroadcastReceiver mAIAlbumStatusObserver = null;
    public CheckBoxPreference mAIAlbumStatusPref;
    public CheckBoxPreference mAutoDownloadPref;
    public CheckBoxPreference mFaceLocalStatusPref;
    public CheckBoxPreference mOriginDownLoad;
    public PreferenceScreen mPreferenceRoot;
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
        setPreferencesFromResource(R.xml.backup_login_preference, str);
        Intent intent = getActivity().getIntent();
        boolean z = false;
        if (intent != null) {
            z = intent.getBooleanExtra("hide_ai_pref", false);
        }
        this.mPreferenceRoot = (PreferenceScreen) findPreference("root");
        this.mAIAlbumStatusPref = (CheckBoxPreference) findPreference("cloud_ai_album_status");
        this.mFaceLocalStatusPref = (CheckBoxPreference) findPreference("cloud_face_local_status");
        if (z) {
            this.mPreferenceRoot.removePreference(this.mAIAlbumStatusPref);
            this.mAIAlbumStatusPref = null;
            this.mPreferenceRoot.removePreference(this.mFaceLocalStatusPref);
            this.mFaceLocalStatusPref = null;
        } else if (AIAlbumStatusHelper.useNewAIAlbumSwitch()) {
            this.mPreferenceRoot.removePreference(this.mFaceLocalStatusPref);
            this.mFaceLocalStatusPref = null;
            this.mAIAlbumStatusPref.setOnPreferenceChangeListener(this);
        } else {
            this.mPreferenceRoot.removePreference(this.mAIAlbumStatusPref);
            this.mAIAlbumStatusPref = null;
            this.mFaceLocalStatusPref.setOnPreferenceChangeListener(this);
        }
        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference("auto_download_media");
        this.mAutoDownloadPref = checkBoxPreference;
        checkBoxPreference.setChecked(GalleryPreferences.Sync.isAutoDownload());
        this.mAutoDownloadPref.setOnPreferenceChangeListener(this);
        CheckBoxPreference checkBoxPreference2 = (CheckBoxPreference) findPreference("download_thumbnail");
        this.mThumbnailDownLoad = checkBoxPreference2;
        checkBoxPreference2.setChecked(DownloadType.THUMBNAIL.equals(GalleryPreferences.Sync.getDownloadType()));
        this.mThumbnailDownLoad.setOnPreferenceClickListener(this);
        CheckBoxPreference checkBoxPreference3 = (CheckBoxPreference) findPreference("download_origin");
        this.mOriginDownLoad = checkBoxPreference3;
        checkBoxPreference3.setChecked(DownloadType.ORIGIN.equals(GalleryPreferences.Sync.getDownloadType()));
        this.mOriginDownLoad.setOnPreferenceClickListener(this);
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
        boolean z = true;
        if (this.mAIAlbumStatusPref != null) {
            if (this.mPreferenceRoot.findPreference("cloud_ai_album_status") == null) {
                this.mPreferenceRoot.addPreference(this.mAIAlbumStatusPref);
            }
            this.mAIAlbumStatusPref.setChecked(getAIAlbumStatus());
        } else if (this.mFaceLocalStatusPref != null) {
            if (this.mPreferenceRoot.findPreference("cloud_face_local_status") == null) {
                this.mPreferenceRoot.addPreference(this.mFaceLocalStatusPref);
            }
            this.mFaceLocalStatusPref.setChecked(getFaceAlbumStatus());
        } else {
            z = false;
        }
        if (!z || this.mAIAlbumStatusObserver != null) {
            return;
        }
        this.mAIAlbumStatusObserver = new BroadcastReceiver() { // from class: com.miui.gallery.ui.BackupLoginSettingsFragment.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context, Intent intent) {
                BackupLoginSettingsFragment.this.updateAIAlbumStatus();
            }
        };
        AIAlbumStatusHelper.registerAIAlbumStatusReceiver(getActivity(), this.mAIAlbumStatusObserver);
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        if (this.mAIAlbumStatusObserver != null) {
            AIAlbumStatusHelper.unregisterAIAlbumStatusReceiver(getActivity(), this.mAIAlbumStatusObserver);
            this.mAIAlbumStatusObserver = null;
        }
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
        if (preference == this.mThumbnailDownLoad) {
            setDownloadType(DownloadType.THUMBNAIL);
            return true;
        } else if (preference != this.mOriginDownLoad) {
            return true;
        } else {
            setDownloadType(DownloadType.ORIGIN);
            return true;
        }
    }

    @Override // androidx.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        FragmentActivity activity = getActivity();
        CheckBoxPreference checkBoxPreference = this.mAIAlbumStatusPref;
        if (checkBoxPreference != null && preference == checkBoxPreference) {
            AIAlbumStatusHelper.setAIAlbumLocalStatus(activity, ((Boolean) obj).booleanValue());
            HashMap hashMap = new HashMap(1);
            hashMap.put("to", obj.toString());
            SamplingStatHelper.recordCountEvent("settings", "settings_change_ai_album_switch", hashMap);
        } else {
            CheckBoxPreference checkBoxPreference2 = this.mFaceLocalStatusPref;
            if (checkBoxPreference2 != null && preference == checkBoxPreference2) {
                AIAlbumStatusHelper.setFaceAlbumStatus(activity, ((Boolean) obj).booleanValue());
                HashMap hashMap2 = new HashMap(1);
                hashMap2.put("to", obj.toString());
                SamplingStatHelper.recordCountEvent("settings", "settings_change_face_switch", hashMap2);
            } else if (preference == this.mAutoDownloadPref) {
                BatchDownloadManager.switchAutoDownload(((Boolean) obj).booleanValue(), getActivity());
            }
        }
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
}
