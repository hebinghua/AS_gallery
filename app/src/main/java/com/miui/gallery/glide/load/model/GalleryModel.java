package com.miui.gallery.glide.load.model;

import android.os.Bundle;
import com.miui.gallery.util.Scheme;
import java.util.Objects;

/* loaded from: classes2.dex */
public class GalleryModel {
    public byte[] mBlob;
    public transient Bundle mExtras;
    public boolean mIsCameraPreview;
    public boolean mIsJustEditExported;
    public String mPath;

    public GalleryModel(String str, byte[] bArr) {
        this.mPath = configPath(str);
        this.mBlob = bArr;
    }

    public static GalleryModel of(String str) {
        return of(str, null);
    }

    public static GalleryModel of(String str, byte[] bArr) {
        if (Scheme.ofUri(str) == Scheme.UNKNOWN) {
            str = Scheme.FILE.wrap(str);
        }
        return new GalleryModel(str, bArr);
    }

    public String getPath() {
        return this.mPath;
    }

    public synchronized Bundle getExtras() {
        if (this.mExtras == null) {
            this.mExtras = new Bundle();
        }
        return this.mExtras;
    }

    public GalleryModel setIsCameraPreview(boolean z) {
        this.mIsCameraPreview = z;
        return this;
    }

    public Boolean isCameraPreview() {
        return Boolean.valueOf(this.mIsCameraPreview);
    }

    public static String configPath(String str) {
        if (str == null) {
            return null;
        }
        return str.startsWith("content://media/") ? str.replace("content://media/external_primary", "content://media/external") : str;
    }

    public boolean isIsJustEditExported() {
        return this.mIsJustEditExported;
    }

    public GalleryModel setIsJustEditExported(boolean z) {
        this.mIsJustEditExported = z;
        return this;
    }

    public byte[] getBlob() {
        return this.mBlob;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            return Objects.equals(this.mPath, ((GalleryModel) obj).mPath);
        }
        return false;
    }

    public int hashCode() {
        return this.mPath.hashCode();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.mPath);
        sb.append(this.mIsCameraPreview ? "true" : "");
        return sb.toString();
    }
}
