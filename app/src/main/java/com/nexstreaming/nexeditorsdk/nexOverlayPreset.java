package com.nexstreaming.nexeditorsdk;

import android.content.Context;
import android.graphics.Bitmap;
import com.nexstreaming.app.common.nexasset.assetpackage.ItemCategory;
import com.nexstreaming.app.common.nexasset.assetpackage.ItemType;
import java.io.IOException;
import java.util.ArrayList;

/* loaded from: classes3.dex */
public final class nexOverlayPreset {
    private static String TAG = "OverlayPresetLoad";
    private static nexOverlayPreset sSingleton;

    public static nexOverlayPreset getOverlayPreset(Context context) {
        if (sSingleton == null) {
            sSingleton = new nexOverlayPreset(context);
        }
        return sSingleton;
    }

    public static nexOverlayPreset getOverlayPreset() {
        return sSingleton;
    }

    public nexOverlayPreset(Context context) {
    }

    public nexOverlayImage getOverlayImage(String str) {
        com.nexstreaming.app.common.nexasset.assetpackage.f c = com.nexstreaming.app.common.nexasset.assetpackage.c.a().c(str);
        if (c != null && c.getCategory() == ItemCategory.overlay && c.getType() == ItemType.overlay) {
            return new nexOverlayImage(str, true);
        }
        return null;
    }

    public String[] getIDs() {
        ArrayList arrayList = new ArrayList();
        for (com.nexstreaming.app.common.nexasset.assetpackage.f fVar : com.nexstreaming.app.common.nexasset.assetpackage.c.a().a(ItemCategory.overlay)) {
            if (!fVar.isHidden() && fVar.getType() == ItemType.overlay) {
                arrayList.add(fVar.getId());
            }
        }
        int size = arrayList.size();
        String[] strArr = new String[size];
        for (int i = 0; i < size; i++) {
            strArr[i] = (String) arrayList.get(i);
        }
        return strArr;
    }

    public String[] getIDs(int i) {
        ArrayList arrayList = new ArrayList();
        for (com.nexstreaming.app.common.nexasset.assetpackage.f fVar : com.nexstreaming.app.common.nexasset.assetpackage.c.a().c(i)) {
            if (!fVar.isHidden() && fVar.getCategory() == ItemCategory.overlay && fVar.getType() == ItemType.overlay) {
                arrayList.add(fVar.getId());
            }
        }
        int size = arrayList.size();
        String[] strArr = new String[size];
        for (int i2 = 0; i2 < size; i2++) {
            strArr[i2] = (String) arrayList.get(i2);
        }
        return strArr;
    }

    public Bitmap getIcon(String str, int i, int i2) {
        Bitmap bitmap;
        com.nexstreaming.app.common.nexasset.assetpackage.f c = com.nexstreaming.app.common.nexasset.assetpackage.c.a().c(str);
        if (c != null) {
            try {
                bitmap = com.nexstreaming.app.common.nexasset.assetpackage.e.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), c, i, i2);
            } catch (IOException unused) {
                bitmap = null;
            }
            if (bitmap == null) {
                try {
                    bitmap = com.nexstreaming.app.common.nexasset.assetpackage.e.b(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), c, i, i2);
                } catch (IOException unused2) {
                }
            }
            if (bitmap != null) {
                return bitmap;
            }
        }
        return null;
    }
}
