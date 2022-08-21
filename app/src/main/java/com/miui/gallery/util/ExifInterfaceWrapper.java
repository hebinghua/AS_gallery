package com.miui.gallery.util;

import com.miui.gallery3d.exif.ExifInterface;

/* compiled from: ExifUtil.java */
/* loaded from: classes2.dex */
public class ExifInterfaceWrapper {
    public ExifInterface mExifI;
    public android.media.ExifInterface mMediaExif;
    public androidx.exifinterface.media.ExifInterface mSupportExifI;

    public ExifInterfaceWrapper(androidx.exifinterface.media.ExifInterface exifInterface) {
        this.mSupportExifI = exifInterface;
    }

    public ExifInterfaceWrapper(ExifInterface exifInterface) {
        this.mExifI = exifInterface;
    }

    public ExifInterfaceWrapper(android.media.ExifInterface exifInterface) {
        this.mMediaExif = exifInterface;
    }

    public String getUserComment() {
        ExifInterface exifInterface = this.mExifI;
        if (exifInterface != null) {
            return exifInterface.getUserCommentAsASCII();
        }
        androidx.exifinterface.media.ExifInterface exifInterface2 = this.mSupportExifI;
        if (exifInterface2 != null) {
            return exifInterface2.getAttribute("UserComment");
        }
        android.media.ExifInterface exifInterface3 = this.mMediaExif;
        if (exifInterface3 == null) {
            return null;
        }
        return exifInterface3.getAttribute("UserComment");
    }

    public void setUserComment(String str) {
        ExifInterface exifInterface = this.mExifI;
        if (exifInterface != null) {
            exifInterface.addUserComment(str);
        }
        androidx.exifinterface.media.ExifInterface exifInterface2 = this.mSupportExifI;
        if (exifInterface2 != null) {
            exifInterface2.setAttribute("UserComment", str);
        }
        android.media.ExifInterface exifInterface3 = this.mMediaExif;
        if (exifInterface3 != null) {
            exifInterface3.setAttribute("UserComment", str);
        }
    }
}
