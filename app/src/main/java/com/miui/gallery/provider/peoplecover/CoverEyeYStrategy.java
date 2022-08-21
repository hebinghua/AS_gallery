package com.miui.gallery.provider.peoplecover;

import android.database.Cursor;

/* loaded from: classes2.dex */
public class CoverEyeYStrategy extends BaseStrategy {
    public CoverEyeYStrategy(int i) {
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
            f = cursor.getFloat(6) - cursor.getFloat(4);
            f2 = cursor.getFloat(2);
        } else {
            f = cursor.getFloat(7) - cursor.getFloat(5);
            f2 = cursor.getFloat(3);
        }
        return Math.abs(f) < f2 * 0.1f;
    }
}
