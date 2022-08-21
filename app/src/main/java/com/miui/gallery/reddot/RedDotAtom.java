package com.miui.gallery.reddot;

import com.miui.gallery.preference.GalleryPreferences;

/* loaded from: classes2.dex */
public class RedDotAtom extends RedDot {
    public boolean mIsPushValid;
    public boolean mIsUpdate;

    @Override // com.miui.gallery.reddot.Rules
    public void onUpdate() {
        GalleryPreferences.FeatureRedDot.setUpdateFeatureUsed(this.mKey, false);
    }

    public RedDotAtom(String str, boolean z, boolean z2) {
        super(str);
        this.mIsUpdate = z;
        this.mIsPushValid = z2;
    }

    @Override // com.miui.gallery.reddot.Rules
    public void onClick() {
        setRedDotUsed();
    }

    @Override // com.miui.gallery.reddot.Rules
    public void onSaw() {
        if (!DisplayStatusManager.getRedDotStatus(this.mKey, false) || GalleryPreferences.FeatureRedDot.getRedDotSawTime(this.mKey) != 0) {
            return;
        }
        GalleryPreferences.FeatureRedDot.setRedDotSawTime(this.mKey, System.currentTimeMillis());
    }

    public boolean queryOriginalStatus() {
        if ((!this.mIsUpdate || GalleryPreferences.FeatureRedDot.hasUpdateFeatureUsed(this.mKey)) && (!this.mIsPushValid || !GalleryPreferences.FeatureRedDot.isFeatureRedDotValid(this.mKey, System.currentTimeMillis()))) {
            return false;
        }
        if (GalleryPreferences.FeatureRedDot.getRedDotSawTime(this.mKey) == 0 || System.currentTimeMillis() - GalleryPreferences.FeatureRedDot.getRedDotSawTime(this.mKey) < 172800000) {
            return true;
        }
        setRedDotUsed();
        return false;
    }

    @Override // com.miui.gallery.reddot.Rules
    public boolean processDisplayStatus() {
        return queryOriginalStatus();
    }

    public final void setRedDotUsed() {
        GalleryPreferences.FeatureRedDot.setUpdateFeatureUsed(this.mKey, true);
        if (GalleryPreferences.FeatureRedDot.isFeatureRedDotValid(this.mKey, System.currentTimeMillis())) {
            GalleryPreferences.FeatureRedDot.setFeatureRedDotValidTime(this.mKey, 0L, 0L);
        }
        GalleryPreferences.FeatureRedDot.setRedDotSawTime(this.mKey, 0L);
    }
}
