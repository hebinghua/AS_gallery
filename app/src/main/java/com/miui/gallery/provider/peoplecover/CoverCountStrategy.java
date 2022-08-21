package com.miui.gallery.provider.peoplecover;

import android.database.Cursor;
import com.miui.gallery.cloud.peopleface.PeopleFace;
import com.miui.gallery.provider.deprecated.NormalPeopleFaceMediaSet;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class CoverCountStrategy extends BaseStrategy {
    public CoverCountStrategy(int i) {
        super(i);
    }

    @Override // com.miui.gallery.provider.peoplecover.BaseStrategy
    public boolean isValid(Cursor cursor) {
        if (cursor == null) {
            return false;
        }
        ArrayList<PeopleFace> brothers = NormalPeopleFaceMediaSet.getBrothers(cursor.getString(9));
        return (brothers == null ? 0 : brothers.size()) <= 3;
    }
}
