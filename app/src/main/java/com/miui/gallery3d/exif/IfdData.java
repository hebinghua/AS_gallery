package com.miui.gallery3d.exif;

import java.util.HashMap;
import java.util.Map;

/* loaded from: classes3.dex */
public class IfdData {
    public static final int[] sIfds = {0, 1, 2, 3, 4};
    public final int mIfdId;
    public final Map<Short, ExifTag> mExifTags = new HashMap();
    public int mOffsetToNextIfd = 0;

    public IfdData(int i) {
        this.mIfdId = i;
    }

    public static int[] getIfds() {
        return sIfds;
    }

    public ExifTag[] getAllTags() {
        return (ExifTag[]) this.mExifTags.values().toArray(new ExifTag[this.mExifTags.size()]);
    }

    public int getId() {
        return this.mIfdId;
    }

    public ExifTag getTag(short s) {
        return this.mExifTags.get(Short.valueOf(s));
    }

    public ExifTag setTag(ExifTag exifTag) {
        exifTag.setIfd(this.mIfdId);
        return this.mExifTags.put(Short.valueOf(exifTag.getTagId()), exifTag);
    }

    public void removeTag(short s) {
        this.mExifTags.remove(Short.valueOf(s));
    }

    public int getTagCount() {
        return this.mExifTags.size();
    }

    public void setOffsetToNextIfd(int i) {
        this.mOffsetToNextIfd = i;
    }

    public int getOffsetToNextIfd() {
        return this.mOffsetToNextIfd;
    }

    public boolean equals(Object obj) {
        ExifTag[] allTags;
        if (this == obj) {
            return true;
        }
        if (obj != null && (obj instanceof IfdData)) {
            IfdData ifdData = (IfdData) obj;
            if (ifdData.getId() == this.mIfdId && ifdData.getTagCount() == getTagCount()) {
                for (ExifTag exifTag : ifdData.getAllTags()) {
                    if (!ExifInterface.isOffsetTag(exifTag.getTagId()) && !exifTag.equals(this.mExifTags.get(Short.valueOf(exifTag.getTagId())))) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
