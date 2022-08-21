package miuix.preference;

import androidx.preference.Preference;

/* loaded from: classes3.dex */
public interface OnPreferenceChangeInternalListener {
    void notifyPreferenceChangeInternal(Preference preference);

    boolean onPreferenceChangeInternal(Preference preference, Object obj);
}
