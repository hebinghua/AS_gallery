package com.miui.gallery.cloudcontrol;

import android.database.Cursor;
import com.xiaomi.stat.a.j;

/* loaded from: classes.dex */
public class FeatureDBItem extends FeatureProfile {
    public static final String[] PROJECTION = {j.c, "featureName", "status", "strategy"};
    public long mId;

    public static FeatureDBItem fromCursor(Cursor cursor) {
        FeatureDBItem featureDBItem = new FeatureDBItem();
        featureDBItem.setId(cursor.getLong(0));
        featureDBItem.setName(cursor.getString(1));
        featureDBItem.setStatus(cursor.getString(2));
        featureDBItem.setStrategy(cursor.getString(3));
        return featureDBItem;
    }

    public void setId(long j) {
        this.mId = j;
    }
}
