package com.miui.gallery.provider.peoplecover;

import android.database.Cursor;

/* loaded from: classes2.dex */
public class CoverEyeXStrategy extends BaseStrategy {
    public CoverEyeXStrategy(int i) {
        super(i);
    }

    @Override // com.miui.gallery.provider.peoplecover.BaseStrategy
    public boolean isValid(Cursor cursor) {
        float abs;
        float abs2;
        float abs3;
        if (cursor == null) {
            return false;
        }
        if (isXYReversed(cursor.getInt(11))) {
            float f = cursor.getFloat(1);
            float f2 = cursor.getFloat(3) + f;
            abs = Math.abs(f - f2);
            abs2 = Math.abs(cursor.getFloat(5) - f);
            abs3 = Math.abs(cursor.getFloat(7) - f2);
        } else {
            float f3 = cursor.getFloat(0);
            float f4 = cursor.getFloat(2) + f3;
            abs = Math.abs(f3 - f4);
            abs2 = Math.abs(cursor.getFloat(4) - f3);
            abs3 = Math.abs(cursor.getFloat(6) - f4);
        }
        return Math.abs(abs2 - abs3) < abs * 0.05f;
    }
}
