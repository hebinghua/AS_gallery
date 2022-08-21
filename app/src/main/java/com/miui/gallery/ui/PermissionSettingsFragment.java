package com.miui.gallery.ui;

import android.os.Bundle;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.permission.core.Permission;
import com.miui.gallery.permission.core.PermissionCheckCallback;
import com.miui.gallery.permission.core.PermissionInjector;
import com.miui.gallery.permission.core.PermissionUtils;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.IntentUtil;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.HashMap;
import miuix.preference.TextPreference;

/* loaded from: classes2.dex */
public class PermissionSettingsFragment extends BasePreferenceFragment implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
    public static final HashMap<String, Permission[]> sPermissionMap = new HashMap<>();
    public TextPreference mContactPref;
    public TextPreference mLocationPref;
    public PreferenceScreen mPreferenceRoot;
    public TextPreference mStoragePref;

    @Override // androidx.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        return false;
    }

    @Override // androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        getPreferenceManager().setSharedPreferencesName("com.miui.gallery_preferences_new");
        setPreferencesFromResource(R.xml.permission_preferences, str);
        this.mPreferenceRoot = (PreferenceScreen) findPreference("root");
        TextPreference textPreference = (TextPreference) findPreference("storage");
        this.mStoragePref = textPreference;
        textPreference.setOnPreferenceClickListener(this);
        HashMap<String, Permission[]> hashMap = sPermissionMap;
        hashMap.put("storage", new Permission[]{new Permission("android.permission.WRITE_EXTERNAL_STORAGE", getString(R.string.privacy_permission_storage_detail), false), new Permission("android.permission.READ_EXTERNAL_STORAGE", getString(R.string.privacy_permission_storage_detail), false)});
        TextPreference textPreference2 = (TextPreference) findPreference("contact");
        this.mContactPref = textPreference2;
        textPreference2.setOnPreferenceClickListener(this);
        hashMap.put("contact", new Permission[]{new Permission("android.permission.READ_CONTACTS", getString(R.string.privacy_permission_contact_detail), false)});
        TextPreference textPreference3 = (TextPreference) findPreference("location");
        this.mLocationPref = textPreference3;
        textPreference3.setOnPreferenceClickListener(this);
        hashMap.put("location", new Permission[]{new Permission("android.permission.ACCESS_FINE_LOCATION", getString(R.string.privacy_permission_location_detail), false), new Permission("android.permission.ACCESS_COARSE_LOCATION", getString(R.string.privacy_permission_location_detail), false)});
        updatePrefText();
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        SamplingStatHelper.recordPageStart(getActivity(), "permission_settings");
        updatePrefText();
    }

    public final void updatePrefText() {
        TextPreference textPreference = this.mStoragePref;
        HashMap<String, Permission[]> hashMap = sPermissionMap;
        textPreference.setText(getPrefText(hashMap.get("storage")));
        this.mContactPref.setText(getPrefText(hashMap.get("contact")));
        this.mLocationPref.setText(getPrefText(hashMap.get("location")));
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        SamplingStatHelper.recordPageEnd(getActivity(), "permission_settings");
    }

    public final boolean getPrefAgreed(Permission[] permissionArr) {
        if (permissionArr == null) {
            return true;
        }
        for (Permission permission : permissionArr) {
            int permissionState = PermissionUtils.getPermissionState(getActivity(), permission);
            if (permissionState == -1 || permissionState == 1) {
                return false;
            }
        }
        return true;
    }

    public final String getPrefText(Permission[] permissionArr) {
        if (getPrefAgreed(permissionArr)) {
            return getString(R.string.privacy_permission_agree);
        }
        return getString(R.string.privacy_permission_disagree);
    }

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(final Preference preference) {
        final Permission[] permissionArr;
        String key = preference.getKey();
        if (key != null && (permissionArr = sPermissionMap.get(key)) != null) {
            boolean z = false;
            for (Permission permission : permissionArr) {
                if (PermissionUtils.getPermissionState(getActivity(), permission) == 1) {
                    z = true;
                }
                if (z) {
                    break;
                }
            }
            HashMap hashMap = new HashMap();
            hashMap.put("tip", "403.66.1.1.16233");
            hashMap.put("status", Boolean.valueOf(getPrefAgreed(permissionArr)));
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, key);
            TrackController.trackClick(hashMap);
            if (z) {
                PermissionInjector.injectIfNeededIn(getActivity(), new PermissionCheckCallback() { // from class: com.miui.gallery.ui.PermissionSettingsFragment.1
                    @Override // com.miui.gallery.permission.core.PermissionCheckCallback
                    public Permission[] getRuntimePermissions() {
                        return permissionArr;
                    }

                    @Override // com.miui.gallery.permission.core.PermissionCheckCallback
                    public void onPermissionsChecked(Permission[] permissionArr2, int[] iArr, boolean[] zArr) {
                        ((TextPreference) preference).setText(PermissionSettingsFragment.this.getPrefText(permissionArr2));
                    }
                }, null);
            } else {
                IntentUtil.goToAppPermissionEditor(getContext());
            }
        }
        return true;
    }
}
