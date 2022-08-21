package com.miui.gallery.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.preference.PreferenceViewHolder;
import com.miui.gallery.R;
import miuix.preference.TextPreference;

/* loaded from: classes2.dex */
public class DrawablePreference extends TextPreference {
    public Drawable mDrawable;
    public int mDrawablePadding;
    public boolean mShowDrawable;
    public int mTextColor;

    public DrawablePreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public DrawablePreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public DrawablePreference(Context context) {
        super(context);
    }

    @Override // miuix.preference.TextPreference, androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        TextView textView = (TextView) preferenceViewHolder.itemView.findViewById(R.id.text_right);
        if (textView != null) {
            CharSequence text = getText();
            if (!TextUtils.isEmpty(text)) {
                textView.setText(text);
                textView.setContentDescription(text);
                textView.setTypeface(Typeface.create("mipro-medium", 0));
                textView.setVisibility(0);
                if (this.mTextColor != 0) {
                    textView.setTextColor(getContext().getColor(this.mTextColor));
                }
                if (this.mDrawable != null && this.mShowDrawable) {
                    if (getContext().getResources().getConfiguration().getLayoutDirection() == 0) {
                        textView.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, this.mDrawable, (Drawable) null);
                    } else {
                        textView.setCompoundDrawablesWithIntrinsicBounds(this.mDrawable, (Drawable) null, (Drawable) null, (Drawable) null);
                    }
                    textView.setCompoundDrawablePadding(this.mDrawablePadding);
                    return;
                }
                textView.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, (Drawable) null, (Drawable) null, (Drawable) null);
                return;
            }
            textView.setVisibility(8);
        }
    }

    public void setTextColor(int i) {
        this.mTextColor = i;
    }

    public void setDrawable(int i) {
        this.mDrawable = getContext().getDrawable(i);
        this.mShowDrawable = true;
    }

    public void setDrawableDisplay(boolean z) {
        this.mShowDrawable = z;
        notifyChanged();
    }

    public void setDrawablePadding(int i) {
        this.mDrawablePadding = i;
    }
}
