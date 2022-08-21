package com.miui.gallery.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceViewHolder;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class GalleryPreferenceCategory extends PreferenceCategory {
    public GalleryPreferenceCategory(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // androidx.preference.PreferenceCategory, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        TextView textView = (TextView) preferenceViewHolder.findViewById(16908310);
        textView.setTextSize(0, getContext().getResources().getDimensionPixelSize(R.dimen.baby_lock_wallpaper_title_size));
        textView.setTypeface(Typeface.defaultFromStyle(0));
        textView.setTextColor(getContext().getResources().getColor(R.color.baby_lock_wall_paper_title_color));
        textView.setPadding(0, 0, 0, getContext().getResources().getDimensionPixelSize(R.dimen.baby_lock_wallpaper_title_padding));
        textView.setAllCaps(false);
    }
}
