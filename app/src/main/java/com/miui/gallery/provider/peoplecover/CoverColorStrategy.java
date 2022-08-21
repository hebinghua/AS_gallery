package com.miui.gallery.provider.peoplecover;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;
import android.text.TextUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.DecodeRegionImageUtils;
import com.miui.gallery.util.ExifUtil;
import com.miui.gallery.util.face.FaceRegionRectF;

/* loaded from: classes2.dex */
public class CoverColorStrategy extends BaseStrategy {
    public PeopleCoverReusedBitCache mBitCache;

    public CoverColorStrategy(int i) {
        super(i);
        this.mBitCache = new PeopleCoverReusedBitCache();
    }

    @Override // com.miui.gallery.provider.peoplecover.BaseStrategy
    public boolean isValid(Cursor cursor) {
        Bitmap coverBitmap = getCoverBitmap(getCoverFilePath(cursor), getFaceRegionRectF(cursor));
        if (coverBitmap == null) {
            return false;
        }
        int height = coverBitmap.getHeight();
        int width = coverBitmap.getWidth();
        for (int i = 0; i < width; i++) {
            for (int i2 = 0; i2 < height; i2++) {
                int pixel = coverBitmap.getPixel(i, i2);
                int red = Color.red(pixel);
                int blue = Color.blue(pixel);
                int green = Color.green(pixel);
                int max = Math.max(red, Math.max(green, blue));
                int min = Math.min(red, Math.min(green, blue));
                float f = max == 0 ? 0.0f : (max - min) / max;
                float f2 = ((max + min) / 255.0f) / 2.0f;
                if (f > 0.25f && f2 > 0.25f && f2 < 0.75f) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // com.miui.gallery.provider.peoplecover.BaseStrategy
    public void onFinish() {
        this.mBitCache.clear();
    }

    public final String getCoverFilePath(Cursor cursor) {
        String string = cursor.getString(15);
        if (TextUtils.isEmpty(string)) {
            string = cursor.getString(16);
        }
        return TextUtils.isEmpty(string) ? cursor.getString(14) : string;
    }

    public final Bitmap getCoverBitmap(String str, RectF rectF) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            Bitmap decodeFaceRegion = DecodeRegionImageUtils.decodeFaceRegion(rectF, str, 1.0f, 50, ExifUtil.sSupportExifCreator.mo1692create(str).getAttributeInt("Orientation", 1), this.mBitCache);
            this.mBitCache.put(decodeFaceRegion);
            BaseMiscUtil.closeSilently(null);
            return decodeFaceRegion;
        } catch (Throwable unused) {
            BaseMiscUtil.closeSilently(null);
            return null;
        }
    }

    public FaceRegionRectF getFaceRegionRectF(Cursor cursor) {
        return new FaceRegionRectF(cursor.getFloat(0), cursor.getFloat(1), cursor.getFloat(2) + cursor.getFloat(0), cursor.getFloat(1) + cursor.getFloat(3), cursor.getInt(11));
    }
}
