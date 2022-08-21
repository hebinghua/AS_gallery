package androidx.preference;

import android.content.Context;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public final class ExpandButton extends Preference {
    public long mId;

    public ExpandButton(Context context, List<Preference> list, long j) {
        super(context);
        initLayout();
        setSummary(list);
        this.mId = j + 1000000;
    }

    public final void initLayout() {
        setLayoutResource(R$layout.expand_button);
        setIcon(R$drawable.ic_arrow_down_24dp);
        setTitle(R$string.expand_button_title);
        setOrder(999);
    }

    public final void setSummary(List<Preference> list) {
        ArrayList arrayList = new ArrayList();
        CharSequence charSequence = null;
        for (Preference preference : list) {
            CharSequence title = preference.getTitle();
            boolean z = preference instanceof PreferenceGroup;
            if (z && !TextUtils.isEmpty(title)) {
                arrayList.add((PreferenceGroup) preference);
            }
            if (arrayList.contains(preference.getParent())) {
                if (z) {
                    arrayList.add((PreferenceGroup) preference);
                }
            } else if (!TextUtils.isEmpty(title)) {
                charSequence = charSequence == null ? title : getContext().getString(R$string.summary_collapsed_preference_list, charSequence, title);
            }
        }
        setSummary(charSequence);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        preferenceViewHolder.setDividerAllowedAbove(false);
    }

    @Override // androidx.preference.Preference
    public long getId() {
        return this.mId;
    }
}
