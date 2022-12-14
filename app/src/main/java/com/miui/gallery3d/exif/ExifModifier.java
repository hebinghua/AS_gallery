package com.miui.gallery3d.exif;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class ExifModifier {
    public final ByteBuffer mByteBuffer;
    public final ExifInterface mInterface;
    public int mOffsetBase;
    public final List<TagOffset> mTagOffsets = new ArrayList();
    public final ExifData mTagToModified;

    /* loaded from: classes3.dex */
    public static class TagOffset {
        public final int mOffset;
        public final ExifTag mTag;

        public TagOffset(ExifTag exifTag, int i) {
            this.mTag = exifTag;
            this.mOffset = i;
        }
    }

    public ExifModifier(ByteBuffer byteBuffer, ExifInterface exifInterface) throws IOException, ExifInvalidFormatException {
        this.mByteBuffer = byteBuffer;
        this.mOffsetBase = byteBuffer.position();
        this.mInterface = exifInterface;
        ByteBufferInputStream byteBufferInputStream = null;
        try {
            ByteBufferInputStream byteBufferInputStream2 = new ByteBufferInputStream(byteBuffer);
            try {
                ExifParser parse = ExifParser.parse(byteBufferInputStream2, exifInterface);
                this.mTagToModified = new ExifData(parse.getByteOrder());
                this.mOffsetBase += parse.getTiffStartPosition();
                byteBuffer.position(0);
                ExifInterface.closeSilently(byteBufferInputStream2);
            } catch (Throwable th) {
                th = th;
                byteBufferInputStream = byteBufferInputStream2;
                ExifInterface.closeSilently(byteBufferInputStream);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public ByteOrder getByteOrder() {
        return this.mTagToModified.getByteOrder();
    }

    public boolean commit() throws IOException, ExifInvalidFormatException {
        ByteBufferInputStream byteBufferInputStream;
        Throwable th;
        IfdData ifdData = null;
        try {
            byteBufferInputStream = new ByteBufferInputStream(this.mByteBuffer);
            try {
                IfdData[] ifdDataArr = {this.mTagToModified.getIfdData(0), this.mTagToModified.getIfdData(1), this.mTagToModified.getIfdData(2), this.mTagToModified.getIfdData(3), this.mTagToModified.getIfdData(4)};
                int i = ifdDataArr[0] != null ? 1 : 0;
                if (ifdDataArr[1] != null) {
                    i |= 2;
                }
                if (ifdDataArr[2] != null) {
                    i |= 4;
                }
                if (ifdDataArr[4] != null) {
                    i |= 8;
                }
                if (ifdDataArr[3] != null) {
                    i |= 16;
                }
                ExifParser parse = ExifParser.parse(byteBufferInputStream, i, this.mInterface);
                for (int next = parse.next(); next != 5; next = parse.next()) {
                    if (next == 0) {
                        ifdData = ifdDataArr[parse.getCurrentIfd()];
                        if (ifdData == null) {
                            parse.skipRemainingTagsInCurrentIfd();
                        }
                    } else if (next == 1) {
                        ExifTag tag = parse.getTag();
                        ExifTag tag2 = ifdData.getTag(tag.getTagId());
                        if (tag2 != null) {
                            if (tag2.getComponentCount() == tag.getComponentCount() && tag2.getDataType() == tag.getDataType()) {
                                this.mTagOffsets.add(new TagOffset(tag2, tag.getOffset()));
                                ifdData.removeTag(tag.getTagId());
                                if (ifdData.getTagCount() == 0) {
                                    parse.skipRemainingTagsInCurrentIfd();
                                }
                            }
                            ExifInterface.closeSilently(byteBufferInputStream);
                            return false;
                        }
                        continue;
                    }
                }
                for (int i2 = 0; i2 < 5; i2++) {
                    IfdData ifdData2 = ifdDataArr[i2];
                    if (ifdData2 != null && ifdData2.getTagCount() > 0) {
                        ExifInterface.closeSilently(byteBufferInputStream);
                        return false;
                    }
                }
                modify();
                ExifInterface.closeSilently(byteBufferInputStream);
                return true;
            } catch (Throwable th2) {
                th = th2;
                ExifInterface.closeSilently(byteBufferInputStream);
                throw th;
            }
        } catch (Throwable th3) {
            byteBufferInputStream = null;
            th = th3;
        }
    }

    public final void modify() {
        this.mByteBuffer.order(getByteOrder());
        for (TagOffset tagOffset : this.mTagOffsets) {
            writeTagValue(tagOffset.mTag, tagOffset.mOffset);
        }
    }

    public final void writeTagValue(ExifTag exifTag, int i) {
        this.mByteBuffer.position(i + this.mOffsetBase);
        int i2 = 0;
        switch (exifTag.getDataType()) {
            case 1:
            case 7:
                byte[] bArr = new byte[exifTag.getComponentCount()];
                exifTag.getBytes(bArr);
                this.mByteBuffer.put(bArr);
                return;
            case 2:
                byte[] stringByte = exifTag.getStringByte();
                if (stringByte.length == exifTag.getComponentCount()) {
                    stringByte[stringByte.length - 1] = 0;
                    this.mByteBuffer.put(stringByte);
                    return;
                }
                this.mByteBuffer.put(stringByte);
                this.mByteBuffer.put((byte) 0);
                return;
            case 3:
                int componentCount = exifTag.getComponentCount();
                while (i2 < componentCount) {
                    this.mByteBuffer.putShort((short) exifTag.getValueAt(i2));
                    i2++;
                }
                return;
            case 4:
            case 9:
                int componentCount2 = exifTag.getComponentCount();
                while (i2 < componentCount2) {
                    this.mByteBuffer.putInt((int) exifTag.getValueAt(i2));
                    i2++;
                }
                return;
            case 5:
            case 10:
                int componentCount3 = exifTag.getComponentCount();
                while (i2 < componentCount3) {
                    Rational rational = exifTag.getRational(i2);
                    this.mByteBuffer.putInt((int) rational.getNumerator());
                    this.mByteBuffer.putInt((int) rational.getDenominator());
                    i2++;
                }
                return;
            case 6:
            case 8:
            default:
                return;
        }
    }

    public void modifyTag(ExifTag exifTag) {
        this.mTagToModified.addTag(exifTag);
    }
}
