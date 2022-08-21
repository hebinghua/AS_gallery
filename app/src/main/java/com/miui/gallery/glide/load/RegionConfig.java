package com.miui.gallery.glide.load;

import android.graphics.RectF;
import com.miui.gallery.util.face.FaceRegionRectF;
import java.util.Objects;

/* loaded from: classes2.dex */
public class RegionConfig {
    public float mEnlargeFactor;
    public RectF mRegion;

    public RegionConfig(RectF rectF, float f) {
        this.mRegion = rectF;
        this.mEnlargeFactor = f;
    }

    public static RegionConfig of(RectF rectF) {
        if (rectF == null) {
            return null;
        }
        return new RegionConfig(rectF, 1.0f);
    }

    public static RegionConfig ofFace(FaceRegionRectF faceRegionRectF, float f) {
        if (faceRegionRectF == null) {
            return null;
        }
        return new RegionConfig(faceRegionRectF, f);
    }

    public RectF getRegion() {
        return this.mRegion;
    }

    public float getEnlargeFactor() {
        return this.mEnlargeFactor;
    }

    public boolean isFace() {
        return this.mRegion instanceof FaceRegionRectF;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        RegionConfig regionConfig = (RegionConfig) obj;
        return Float.compare(regionConfig.mEnlargeFactor, this.mEnlargeFactor) == 0 && Objects.equals(this.mRegion, regionConfig.mRegion);
    }

    public int hashCode() {
        return Objects.hash(this.mRegion, Float.valueOf(this.mEnlargeFactor));
    }
}
