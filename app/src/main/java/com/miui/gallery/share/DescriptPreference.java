package com.miui.gallery.share;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.miui.gallery.R;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public class DescriptPreference extends Preference {
    public CharSequence mDescriptDetail;
    public int mDescriptDetailVisibility;
    public CharSequence mDescriptTitle;
    public int mDescriptTitleVisibility;
    public WeakReference<View> mRootRef;

    public DescriptPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DescriptPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mDescriptTitleVisibility = 0;
        this.mDescriptDetailVisibility = 8;
        setLayoutResource(R.layout.share_album_public_descript_text);
        setPersistent(false);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        this.mRootRef = new WeakReference<>(preferenceViewHolder.itemView);
        refresh();
    }

    public void setDescriptTitle(int i) {
        setDescriptTitle(getContext().getString(i));
    }

    public void setDescriptTitle(CharSequence charSequence) {
        this.mDescriptTitle = charSequence;
    }

    public void setDescriptDetail(CharSequence charSequence) {
        this.mDescriptDetail = charSequence;
    }

    public void setDescriptDetailVisibility(int i) {
        this.mDescriptDetailVisibility = i;
    }

    public final void refresh() {
        View view;
        WeakReference<View> weakReference = this.mRootRef;
        if (weakReference == null || (view = weakReference.get()) == null) {
            return;
        }
        TextView textView = (TextView) view.findViewById(R.id.descript_title);
        textView.setVisibility(this.mDescriptTitleVisibility);
        textView.setText(this.mDescriptTitle);
        TextView textView2 = (TextView) view.findViewById(R.id.descript_detail);
        textView2.setVisibility(this.mDescriptDetailVisibility);
        textView2.setText(this.mDescriptDetail);
    }
}
