package com.miui.gallery.share;

import android.content.Context;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class PublishProgressPreference extends Preference {
    public boolean mOpenPublic;
    public boolean mProgressOn;

    public PublishProgressPreference(Context context) {
        super(context);
        this.mProgressOn = false;
        this.mOpenPublic = false;
        setLayoutResource(R.layout.share_album_preference_progress);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        preferenceViewHolder.findViewById(R.id.publishing_progress).setVisibility(this.mProgressOn ? 0 : 8);
        if (this.mProgressOn) {
            ((TextView) preferenceViewHolder.findViewById(R.id.progress_text)).setText(this.mOpenPublic ? R.string.publishing : R.string.unpublishing);
        }
    }

    public void setProgress(boolean z, boolean z2) {
        this.mProgressOn = z;
        this.mOpenPublic = z2;
        notifyChanged();
    }
}
