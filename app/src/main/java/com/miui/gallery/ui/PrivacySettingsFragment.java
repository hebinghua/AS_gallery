package com.miui.gallery.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceScreen;
import com.miui.gallery.R;
import com.miui.gallery.activity.FloatingWindowActivity;
import com.miui.gallery.agreement.core.CtaAgreement;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.map.utils.MapStatHelper;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.widget.TimerDialog;
import miuix.preference.TextPreference;

/* loaded from: classes2.dex */
public class PrivacySettingsFragment extends BasePreferenceFragment implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
    public CheckBoxPreference mImpunityDeclarationPref;
    public PreferenceScreen mPreferenceRoot;
    public TextPreference mRejectedMapPref;
    public TextPreference mSeeMapPrivacyPolicyPref;
    public TextPreference mSeePermissionDetailPref;
    public TextPreference mSeePrivacyPolicyPref;

    public static /* synthetic */ void $r8$lambda$v8I1vRoRv5MnphMF7Z6xQagG2h0(PrivacySettingsFragment privacySettingsFragment, DialogInterface dialogInterface, int i) {
        privacySettingsFragment.lambda$onPreferenceClick$0(dialogInterface, i);
    }

    @Override // androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        getPreferenceManager().setSharedPreferencesName("com.miui.gallery_preferences_new");
        setPreferencesFromResource(R.xml.privacy_preferences, str);
        this.mPreferenceRoot = (PreferenceScreen) findPreference("root");
        TextPreference textPreference = (TextPreference) findPreference("see_privacy_policy");
        this.mSeePrivacyPolicyPref = textPreference;
        textPreference.setOnPreferenceClickListener(this);
        TextPreference textPreference2 = (TextPreference) findPreference("see_map_privacy_policy");
        this.mSeeMapPrivacyPolicyPref = textPreference2;
        textPreference2.setOnPreferenceClickListener(this);
        TextPreference textPreference3 = (TextPreference) findPreference("see_permission_detail");
        this.mSeePermissionDetailPref = textPreference3;
        textPreference3.setOnPreferenceClickListener(this);
        if (!GalleryPreferences.MapAlbum.isPrivacyPolicyShowed() && findPreference("rejected") != null) {
            this.mPreferenceRoot.removePreference(findPreference("rejected"));
        } else {
            TextPreference textPreference4 = (TextPreference) findPreference("rejected_map");
            this.mRejectedMapPref = textPreference4;
            textPreference4.setOnPreferenceClickListener(this);
        }
        this.mImpunityDeclarationPref = (CheckBoxPreference) findPreference("impunity_declaration");
        if (BaseBuildUtil.isInternational()) {
            this.mPreferenceRoot.removePreference(this.mImpunityDeclarationPref);
            return;
        }
        this.mImpunityDeclarationPref.setChecked(BaseGalleryPreferences.CTA.remindConnectNetworkEveryTime());
        this.mImpunityDeclarationPref.setOnPreferenceChangeListener(this);
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        SamplingStatHelper.recordPageStart(getActivity(), "privacy_settings");
        CheckBoxPreference checkBoxPreference = this.mImpunityDeclarationPref;
        if (checkBoxPreference != null) {
            checkBoxPreference.setChecked(BaseGalleryPreferences.CTA.remindConnectNetworkEveryTime());
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        SamplingStatHelper.recordPageEnd(getActivity(), "privacy_settings");
    }

    @Override // androidx.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (preference == this.mImpunityDeclarationPref) {
            Boolean bool = (Boolean) obj;
            BaseGalleryPreferences.CTA.setRemindConnectNetworkEveryTime(bool.booleanValue());
            TrackController.trackClick("403.22.0.1.11350", AutoTracking.getRef(), bool.booleanValue() ? "open" : "close");
            return true;
        }
        return true;
    }

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
        boolean needForceSplit = getActivity() instanceof FloatingWindowActivity ? ((FloatingWindowActivity) getActivity()).needForceSplit() : false;
        if (preference == this.mSeePrivacyPolicyPref) {
            TrackController.trackClick("403.22.0.1.11351", AutoTracking.getRef());
            IntentUtil.gotoPrivacyPolicy(getActivity());
        } else if (preference == this.mSeeMapPrivacyPolicyPref) {
            MapStatHelper.trackViewPrivacyPolicy("setting");
            TrackController.trackClick("403.22.0.1.15332");
            IntentUtil.gotoBDMapPrivacyPolicy(getActivity());
        } else if (preference == this.mSeePermissionDetailPref) {
            TrackController.trackClick("403.22.2.1.16231");
            IntentUtil.gotoPermissionSettingsActivity(getActivity(), getActivity().getIntent().getBooleanExtra("use_dialog", false), needForceSplit);
        } else if (preference == this.mRejectedMapPref) {
            TrackController.trackClick("403.66.2.1.16234");
            new TimerDialog.Builder(getActivity(), true).setTitle(R.string.privacy_rejected).setMessage(CtaAgreement.buildMapPrivacyPolicy(requireActivity(), R.string.privacy_reject_map_detail_new), true).setConfirmTime(11000L).setPositiveButton(R.string.privacy_rejected_pos_btn, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.PrivacySettingsFragment$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    PrivacySettingsFragment.$r8$lambda$v8I1vRoRv5MnphMF7Z6xQagG2h0(PrivacySettingsFragment.this, dialogInterface, i);
                }
            }).setNegativeButton(17039360, null).build().show();
        }
        return true;
    }

    public /* synthetic */ void lambda$onPreferenceClick$0(DialogInterface dialogInterface, int i) {
        Preference findPreference = findPreference("rejected");
        if (this.mPreferenceRoot == null || findPreference == null) {
            return;
        }
        GalleryPreferences.MapAlbum.setPrivacyPolicyShowed(false);
        this.mPreferenceRoot.removePreference(findPreference);
        TrackController.trackClick("403.66.2.1.16235");
    }
}
