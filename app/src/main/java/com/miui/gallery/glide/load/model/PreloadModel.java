package com.miui.gallery.glide.load.model;

import android.graphics.Bitmap;
import ch.qos.logback.core.CoreConstants;
import java.util.Objects;

/* loaded from: classes2.dex */
public class PreloadModel {
    public Bitmap mBitmap;
    public String mUri;

    public PreloadModel(String str) {
        this.mUri = str + "_cache_for_preview";
    }

    public PreloadModel(String str, Bitmap bitmap) {
        this.mUri = str + "_cache_for_preview";
        this.mBitmap = bitmap;
    }

    public static PreloadModel of(String str) {
        return new PreloadModel(str);
    }

    public static PreloadModel of(String str, Bitmap bitmap) {
        return new PreloadModel(str, bitmap);
    }

    public Bitmap getBitmap() {
        return this.mBitmap;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            return Objects.equals(this.mUri, ((PreloadModel) obj).mUri);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.mUri);
    }

    public String toString() {
        return "PreloadModel{mUri='" + this.mUri + CoreConstants.SINGLE_QUOTE_CHAR + ", mBitmap=" + this.mBitmap + '}';
    }
}
