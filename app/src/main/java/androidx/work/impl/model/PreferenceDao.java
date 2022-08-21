package androidx.work.impl.model;

/* loaded from: classes.dex */
public interface PreferenceDao {
    Long getLongValue(String key);

    void insertPreference(Preference preference);
}
