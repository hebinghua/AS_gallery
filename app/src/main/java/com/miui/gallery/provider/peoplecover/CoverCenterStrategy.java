package com.miui.gallery.provider.peoplecover;

import android.database.Cursor;

/* loaded from: classes2.dex */
public class CoverCenterStrategy extends BaseStrategy {
    public CoverCenterStrategy(int i) {
        super(i);
    }

    @Override // com.miui.gallery.provider.peoplecover.BaseStrategy
    public boolean isValid(Cursor cursor) {
        float f;
        float f2;
        if (cursor == null) {
            return false;
        }
        if (isXYReversed(cursor.getInt(11))) {
            f = cursor.getFloat(1);
            f2 = cursor.getFloat(3);
        } else {
            f = cursor.getFloat(0);
            f2 = cursor.getFloat(2);
        }
        return f2 + f >= 0.25f && f <= 0.75f;
    }
}
