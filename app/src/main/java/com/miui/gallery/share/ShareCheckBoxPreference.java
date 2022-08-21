package com.miui.gallery.share;

import android.content.Context;
import android.util.AttributeSet;
import androidx.preference.CheckBoxPreference;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class ShareCheckBoxPreference extends CheckBoxPreference {
    public ShareCheckBoxPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setLayoutResource(R.layout.share_check_box_preference);
    }

    public ShareCheckBoxPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public ShareCheckBoxPreference(Context context) {
        this(context, null);
    }
}
