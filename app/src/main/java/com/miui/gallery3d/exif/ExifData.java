package com.miui.gallery3d.exif;

import android.util.Log;
import java.io.UnsupportedEncodingException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes3.dex */
public class ExifData {
    public static final byte[] USER_COMMENT_ASCII = {65, 83, 67, 73, 73, 0, 0, 0};
    public static final byte[] USER_COMMENT_JIS = {74, 73, 83, 0, 0, 0, 0, 0};
    public static final byte[] USER_COMMENT_UNICODE = {85, 78, 73, 67, 79, 68, 69, 0};
    public final ByteOrder mByteOrder;
    public final IfdData[] mIfdDatas = new IfdData[5];
    public ArrayList<byte[]> mStripBytes = new ArrayList<>();
    public byte[] mThumbnail;

    public ExifData(ByteOrder byteOrder) {
        this.mByteOrder = byteOrder;
    }

    public byte[] getCompressedThumbnail() {
        return this.mThumbnail;
    }

    public void setCompressedThumbnail(byte[] bArr) {
        this.mThumbnail = bArr;
    }

    public boolean hasCompressedThumbnail() {
        return this.mThumbnail != null;
    }

    public void setStripBytes(int i, byte[] bArr) {
        if (i < this.mStripBytes.size()) {
            this.mStripBytes.set(i, bArr);
            return;
        }
        for (int size = this.mStripBytes.size(); size < i; size++) {
            this.mStripBytes.add(null);
        }
        this.mStripBytes.add(bArr);
    }

    public int getStripCount() {
        return this.mStripBytes.size();
    }

    public byte[] getStrip(int i) {
        return this.mStripBytes.get(i);
    }

    public boolean hasUncompressedStrip() {
        return this.mStripBytes.size() != 0;
    }

    public ByteOrder getByteOrder() {
        return this.mByteOrder;
    }

    public IfdData getIfdData(int i) {
        if (ExifTag.isValidIfd(i)) {
            return this.mIfdDatas[i];
        }
        return null;
    }

    public void addIfdData(IfdData ifdData) {
        this.mIfdDatas[ifdData.getId()] = ifdData;
    }

    public IfdData getOrCreateIfdData(int i) {
        IfdData ifdData = this.mIfdDatas[i];
        if (ifdData == null) {
            IfdData ifdData2 = new IfdData(i);
            this.mIfdDatas[i] = ifdData2;
            return ifdData2;
        }
        return ifdData;
    }

    public ExifTag getTag(short s, int i) {
        IfdData ifdData = this.mIfdDatas[i];
        if (ifdData == null) {
            return null;
        }
        return ifdData.getTag(s);
    }

    public ExifTag addTag(ExifTag exifTag) {
        if (exifTag != null) {
            return addTag(exifTag, exifTag.getIfd());
        }
        return null;
    }

    public ExifTag addTag(ExifTag exifTag, int i) {
        if (exifTag == null || !ExifTag.isValidIfd(i)) {
            return null;
        }
        return getOrCreateIfdData(i).setTag(exifTag);
    }

    public void removeTag(short s, int i) {
        IfdData ifdData = this.mIfdDatas[i];
        if (ifdData == null) {
            return;
        }
        ifdData.removeTag(s);
    }

    public String getUserCommentAsASCII() {
        ExifTag tag;
        byte[] stringByte;
        IfdData ifdData = this.mIfdDatas[0];
        if (ifdData != null && (tag = ifdData.getTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_USER_COMMENT))) != null && (stringByte = tag.getStringByte()) != null && stringByte.length > 0) {
            try {
                if (stringByte[stringByte.length - 1] == 0) {
                    return new String(stringByte, 0, stringByte.length - 1, "US-ASCII");
                }
                return new String(stringByte, "US-ASCII");
            } catch (UnsupportedEncodingException unused) {
                Log.w("ExifData", "Failed to decode the usercomment");
            }
        }
        return null;
    }

    public String getXiaomiComment() {
        ExifTag tag;
        IfdData ifdData = this.mIfdDatas[2];
        if (ifdData == null || (tag = ifdData.getTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_XIAOMI_COMMENT))) == null || tag.getComponentCount() < 1) {
            return null;
        }
        byte[] stringByte = tag.getStringByte();
        try {
            if (stringByte[stringByte.length - 1] == 0) {
                return new String(stringByte, 0, stringByte.length - 1, "US-ASCII");
            }
            return new String(stringByte, "US-ASCII");
        } catch (UnsupportedEncodingException unused) {
            Log.w("ExifData", "Failed to decode the xiaomicomment");
            return null;
        }
    }

    public List<ExifTag> getAllTags() {
        IfdData[] ifdDataArr;
        ExifTag[] allTags;
        ArrayList arrayList = new ArrayList();
        for (IfdData ifdData : this.mIfdDatas) {
            if (ifdData != null && (allTags = ifdData.getAllTags()) != null) {
                for (ExifTag exifTag : allTags) {
                    arrayList.add(exifTag);
                }
            }
        }
        if (arrayList.size() == 0) {
            return null;
        }
        return arrayList;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && (obj instanceof ExifData)) {
            ExifData exifData = (ExifData) obj;
            if (exifData.mByteOrder == this.mByteOrder && exifData.mStripBytes.size() == this.mStripBytes.size() && Arrays.equals(exifData.mThumbnail, this.mThumbnail)) {
                for (int i = 0; i < this.mStripBytes.size(); i++) {
                    if (!Arrays.equals(exifData.mStripBytes.get(i), this.mStripBytes.get(i))) {
                        return false;
                    }
                }
                for (int i2 = 0; i2 < 5; i2++) {
                    IfdData ifdData = exifData.getIfdData(i2);
                    IfdData ifdData2 = getIfdData(i2);
                    if (ifdData != ifdData2 && ifdData != null && !ifdData.equals(ifdData2)) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
}
