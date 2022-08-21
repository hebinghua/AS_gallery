package com.miui.gallery.ui;

import android.database.ContentObserver;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.fragment.app.FragmentActivity;
import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.stat.SamplingStatHelper;

/* loaded from: classes2.dex */
public class BackupSettingsMoreFragment extends BasePreferenceFragment implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
    public CheckBoxPreference mShowLocalAlbumOnlyPref;

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }

    @Override // androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        getPreferenceManager().setSharedPreferencesName("com.miui.gallery_preferences_new");
        setPreferencesFromResource(R.xml.backup_more_preferences, str);
        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) findPreference("show_local_album_only");
        this.mShowLocalAlbumOnlyPref = checkBoxPreference;
        checkBoxPreference.setOnPreferenceChangeListener(this);
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        SamplingStatHelper.recordPageStart(getActivity(), "backup_settings_more");
        this.mShowLocalAlbumOnlyPref.setChecked(GalleryPreferences.LocalMode.isOnlyShowLocalPhoto());
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        SamplingStatHelper.recordPageEnd(getActivity(), "backup_settings_more");
    }

    @Override // androidx.fragment.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return false;
        }
        getActivity().finish();
        return true;
    }

    @Override // androidx.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        FragmentActivity activity = getActivity();
        if (preference == this.mShowLocalAlbumOnlyPref) {
            Boolean bool = (Boolean) obj;
            GalleryPreferences.LocalMode.setOnlyShowLocalPhoto(bool.booleanValue());
            activity.getContentResolver().notifyChange(GalleryContract.Media.URI, (ContentObserver) null, false);
            activity.getContentResolver().notifyChange(GalleryContract.Album.URI_CACHE, (ContentObserver) null, false);
            activity.getContentResolver().notifyChange(GalleryContract.Album.URI, (ContentObserver) null, false);
            activity.getContentResolver().notifyChange(GalleryContract.PeopleFace.PERSONS_URI, (ContentObserver) null, false);
            activity.getContentResolver().notifyChange(GalleryContract.Media.URI_RECENT_MEDIA, (ContentObserver) null, false);
            TrackController.trackClick("403.22.0.1.11343", AutoTracking.getRef(), bool.booleanValue() ? "open" : "close");
            return true;
        }
        return true;
    }
}
