package com.miui.gallery.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceViewHolder;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class ValueListPreference extends ListPreference {
    public int mShowWhichAsValue;

    public ValueListPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mShowWhichAsValue = 0;
        setLayoutResource(R.layout.preference_value_list);
    }

    public ValueListPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public final CharSequence getRealValue() {
        int i = this.mShowWhichAsValue;
        if (i != 0) {
            if (i == 1) {
                return getValue();
            }
            return null;
        }
        return getEntry();
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        TextView textView = (TextView) preferenceViewHolder.findViewById(R.id.value_right);
        if (textView != null) {
            CharSequence realValue = getRealValue();
            if (!TextUtils.isEmpty(realValue)) {
                textView.setText(String.valueOf(realValue));
                textView.setVisibility(0);
                return;
            }
            textView.setVisibility(8);
        }
    }
}
