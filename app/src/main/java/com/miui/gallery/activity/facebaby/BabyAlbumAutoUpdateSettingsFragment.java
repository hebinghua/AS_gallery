package com.miui.gallery.activity.facebaby;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import com.miui.gallery.R;
import com.miui.gallery.app.fragment.MiuiPreferenceFragment;
import com.miui.gallery.cloud.baby.BabyInfo;
import com.miui.gallery.provider.FaceManager;
import java.lang.ref.SoftReference;
import miuix.preference.TextPreference;

/* loaded from: classes.dex */
public class BabyAlbumAutoUpdateSettingsFragment extends MiuiPreferenceFragment implements Preference.OnPreferenceChangeListener {
    public TextPreference mAssociateFaceAlbumPreference;
    public CheckBoxPreference mAutoAddedPreference;
    public BabyInfo mBabyInfo;
    public long mPeopleFaceLocalId;
    public AsyncTask mPeopleFaceNameTask;

    /* renamed from: $r8$lambda$-qTZe4nrjVOJPWZfjbHEjFhuv0c */
    public static /* synthetic */ boolean m478$r8$lambda$qTZe4nrjVOJPWZfjbHEjFhuv0c(BabyAlbumAutoUpdateSettingsFragment babyAlbumAutoUpdateSettingsFragment, Preference preference) {
        return babyAlbumAutoUpdateSettingsFragment.lambda$onViewCreated$0(preference);
    }

    @Override // androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        setPreferencesFromResource(R.xml.baby_album_auto_update_preferences, str);
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        refreshPreference();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        AsyncTask asyncTask = this.mPeopleFaceNameTask;
        if (asyncTask != null) {
            asyncTask.cancel(true);
        }
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference("auto_update_face");
        this.mAutoAddedPreference = checkBoxPreference;
        checkBoxPreference.setOnPreferenceChangeListener(this);
        Intent intent = getActivity().getIntent();
        boolean booleanExtra = intent.getBooleanExtra("allow_to_reassociate", false);
        TextPreference textPreference = (TextPreference) findPreference("associate_face_album");
        this.mAssociateFaceAlbumPreference = textPreference;
        if (booleanExtra) {
            textPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.miui.gallery.activity.facebaby.BabyAlbumAutoUpdateSettingsFragment$$ExternalSyntheticLambda0
                @Override // androidx.preference.Preference.OnPreferenceClickListener
                public final boolean onPreferenceClick(Preference preference) {
                    return BabyAlbumAutoUpdateSettingsFragment.m478$r8$lambda$qTZe4nrjVOJPWZfjbHEjFhuv0c(BabyAlbumAutoUpdateSettingsFragment.this, preference);
                }
            });
        } else {
            textPreference.setEnabled(false);
        }
        this.mBabyInfo = (BabyInfo) intent.getParcelableExtra("baby-info");
        getPeopleNameAsync();
    }

    public /* synthetic */ boolean lambda$onViewCreated$0(Preference preference) {
        pickBabyFace();
        return true;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i != 14) {
            super.onActivityResult(i, i2, intent);
        } else if (i2 != -1 || intent == null) {
        } else {
            String stringExtra = intent.getStringExtra("local_id_of_album");
            if (TextUtils.isEmpty(stringExtra)) {
                return;
            }
            this.mPeopleFaceLocalId = Long.parseLong(stringExtra);
            this.mBabyInfo.mPeopleId = intent.getStringExtra("server_id_of_album");
            this.mBabyInfo.mAutoupdate = true;
            this.mAutoAddedPreference.setChecked(true);
            this.mAssociateFaceAlbumPreference.setText(intent.getStringExtra("album_name"));
        }
    }

    @Override // androidx.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (preference == this.mAutoAddedPreference) {
            if (TextUtils.isEmpty(this.mBabyInfo.mPeopleId)) {
                if (!((Boolean) obj).booleanValue()) {
                    return true;
                }
                pickBabyFace();
                return false;
            }
            this.mBabyInfo.mAutoupdate = ((Boolean) obj).booleanValue();
            return true;
        }
        return true;
    }

    public final void refreshPreference() {
        this.mAutoAddedPreference.setChecked(this.mBabyInfo.mAutoupdate);
    }

    public final void pickBabyFace() {
        Intent intent = new Intent("com.miui.gallery.action.PICK_PEOPLE");
        intent.putExtra("pick_people", true);
        startActivityForResult(intent, 14);
    }

    public final void getPeopleNameAsync() {
        if (TextUtils.isEmpty(this.mBabyInfo.mPeopleId)) {
            return;
        }
        final SoftReference softReference = new SoftReference(this.mAssociateFaceAlbumPreference);
        this.mPeopleFaceNameTask = new AsyncTask<Void, Void, String>() { // from class: com.miui.gallery.activity.facebaby.BabyAlbumAutoUpdateSettingsFragment.1
            {
                BabyAlbumAutoUpdateSettingsFragment.this = this;
            }

            @Override // android.os.AsyncTask
            public String doInBackground(Void... voidArr) {
                return FaceManager.queryPersonName(BabyAlbumAutoUpdateSettingsFragment.this.mBabyInfo.mPeopleId);
            }

            @Override // android.os.AsyncTask
            public void onPostExecute(String str) {
                TextPreference textPreference;
                if (isCancelled() || TextUtils.isEmpty(str) || (textPreference = (TextPreference) softReference.get()) == null) {
                    return;
                }
                textPreference.setText(str);
            }
        }.execute(new Void[0]);
    }

    public BabyInfo getBabyInfo() {
        return this.mBabyInfo;
    }

    public long getPeopleFaceLocalId() {
        return this.mPeopleFaceLocalId;
    }
}
