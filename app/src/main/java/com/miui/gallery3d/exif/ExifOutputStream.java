package com.miui.gallery3d.exif;

import java.io.BufferedOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes3.dex */
public class ExifOutputStream extends FilterOutputStream {
    public ByteBuffer mBuffer;
    public int mByteToCopy;
    public int mByteToSkip;
    public ExifData mExifData;
    public final ExifInterface mInterface;
    public byte[] mSingleByteArray;
    public int mState;

    public ExifOutputStream(OutputStream outputStream, ExifInterface exifInterface) {
        super(new BufferedOutputStream(outputStream, 65536));
        this.mState = 0;
        this.mSingleByteArray = new byte[1];
        this.mBuffer = ByteBuffer.allocate(4);
        this.mInterface = exifInterface;
    }

    public void setExifData(ExifData exifData) {
        this.mExifData = exifData;
    }

    public final int requestByteToBuffer(int i, byte[] bArr, int i2, int i3) {
        int position = i - this.mBuffer.position();
        if (i3 > position) {
            i3 = position;
        }
        this.mBuffer.put(bArr, i2, i3);
        return i3;
    }

    /* JADX WARN: Code restructure failed: missing block: B:50:0x00f9, code lost:
        if (r9 <= 0) goto L12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x00fb, code lost:
        ((java.io.FilterOutputStream) r6).out.write(r7, r8, r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x0100, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:?, code lost:
        return;
     */
    @Override // java.io.FilterOutputStream, java.io.OutputStream
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void write(byte[] r7, int r8, int r9) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 257
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery3d.exif.ExifOutputStream.write(byte[], int, int):void");
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int i) throws IOException {
        byte[] bArr = this.mSingleByteArray;
        bArr[0] = (byte) (i & 255);
        write(bArr);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] bArr) throws IOException {
        write(bArr, 0, bArr.length);
    }

    public final void writeExifData() throws IOException {
        ExifData exifData = this.mExifData;
        if (exifData == null) {
            return;
        }
        ArrayList<ExifTag> stripNullValueTags = stripNullValueTags(exifData);
        createRequiredIfdAndTag();
        int calculateAllOffset = calculateAllOffset() + 8;
        if (calculateAllOffset > 65535) {
            throw new IOException("Exif header is too large (>64Kb)");
        }
        OrderedDataOutputStream orderedDataOutputStream = new OrderedDataOutputStream(((FilterOutputStream) this).out);
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        orderedDataOutputStream.setByteOrder(byteOrder);
        orderedDataOutputStream.writeShort((short) -31);
        orderedDataOutputStream.writeShort((short) calculateAllOffset);
        orderedDataOutputStream.writeInt(1165519206);
        orderedDataOutputStream.writeShort((short) 0);
        if (this.mExifData.getByteOrder() == byteOrder) {
            orderedDataOutputStream.writeShort((short) 19789);
        } else {
            orderedDataOutputStream.writeShort((short) 18761);
        }
        orderedDataOutputStream.setByteOrder(this.mExifData.getByteOrder());
        orderedDataOutputStream.writeShort((short) 42);
        orderedDataOutputStream.writeInt(8);
        writeAllTags(orderedDataOutputStream);
        writeThumbnail(orderedDataOutputStream);
        Iterator<ExifTag> it = stripNullValueTags.iterator();
        while (it.hasNext()) {
            this.mExifData.addTag(it.next());
        }
    }

    public final ArrayList<ExifTag> stripNullValueTags(ExifData exifData) {
        ArrayList<ExifTag> arrayList = new ArrayList<>();
        for (ExifTag exifTag : exifData.getAllTags()) {
            if (exifTag.getValue() == null && !ExifInterface.isOffsetTag(exifTag.getTagId())) {
                exifData.removeTag(exifTag.getTagId(), exifTag.getIfd());
                arrayList.add(exifTag);
            }
        }
        return arrayList;
    }

    public final void writeThumbnail(OrderedDataOutputStream orderedDataOutputStream) throws IOException {
        if (this.mExifData.hasCompressedThumbnail()) {
            orderedDataOutputStream.write(this.mExifData.getCompressedThumbnail());
        } else if (this.mExifData.hasUncompressedStrip()) {
            for (int i = 0; i < this.mExifData.getStripCount(); i++) {
                orderedDataOutputStream.write(this.mExifData.getStrip(i));
            }
        }
    }

    public final void writeAllTags(OrderedDataOutputStream orderedDataOutputStream) throws IOException {
        writeIfd(this.mExifData.getIfdData(0), orderedDataOutputStream);
        writeIfd(this.mExifData.getIfdData(2), orderedDataOutputStream);
        IfdData ifdData = this.mExifData.getIfdData(3);
        if (ifdData != null) {
            writeIfd(ifdData, orderedDataOutputStream);
        }
        IfdData ifdData2 = this.mExifData.getIfdData(4);
        if (ifdData2 != null) {
            writeIfd(ifdData2, orderedDataOutputStream);
        }
        if (this.mExifData.getIfdData(1) != null) {
            writeIfd(this.mExifData.getIfdData(1), orderedDataOutputStream);
        }
    }

    public final void writeIfd(IfdData ifdData, OrderedDataOutputStream orderedDataOutputStream) throws IOException {
        ExifTag[] allTags = ifdData.getAllTags();
        orderedDataOutputStream.writeShort((short) allTags.length);
        for (ExifTag exifTag : allTags) {
            orderedDataOutputStream.writeShort(exifTag.getTagId());
            orderedDataOutputStream.writeShort(exifTag.getDataType());
            orderedDataOutputStream.writeInt(exifTag.getComponentCount());
            if (exifTag.getDataSize() > 4) {
                orderedDataOutputStream.writeInt(exifTag.getOffset());
            } else {
                writeTagValue(exifTag, orderedDataOutputStream);
                int dataSize = 4 - exifTag.getDataSize();
                for (int i = 0; i < dataSize; i++) {
                    orderedDataOutputStream.write(0);
                }
            }
        }
        orderedDataOutputStream.writeInt(ifdData.getOffsetToNextIfd());
        for (ExifTag exifTag2 : allTags) {
            if (exifTag2.getDataSize() > 4) {
                writeTagValue(exifTag2, orderedDataOutputStream);
            }
        }
    }

    public final int calculateOffsetOfIfd(IfdData ifdData, int i) {
        ExifTag[] allTags;
        int tagCount = i + (ifdData.getTagCount() * 12) + 2 + 4;
        for (ExifTag exifTag : ifdData.getAllTags()) {
            if (exifTag.getDataSize() > 4) {
                exifTag.setOffset(tagCount);
                tagCount += exifTag.getDataSize();
            }
        }
        return tagCount;
    }

    public final void createRequiredIfdAndTag() throws IOException {
        IfdData ifdData = this.mExifData.getIfdData(0);
        if (ifdData == null) {
            ifdData = new IfdData(0);
            this.mExifData.addIfdData(ifdData);
        }
        ExifInterface exifInterface = this.mInterface;
        int i = ExifInterface.TAG_EXIF_IFD;
        ExifTag buildUninitializedTag = exifInterface.buildUninitializedTag(i);
        if (buildUninitializedTag == null) {
            throw new IOException("No definition for crucial exif tag: " + i);
        }
        ifdData.setTag(buildUninitializedTag);
        IfdData ifdData2 = this.mExifData.getIfdData(2);
        if (ifdData2 == null) {
            ifdData2 = new IfdData(2);
            this.mExifData.addIfdData(ifdData2);
        }
        if (this.mExifData.getIfdData(4) != null) {
            ExifInterface exifInterface2 = this.mInterface;
            int i2 = ExifInterface.TAG_GPS_IFD;
            ExifTag buildUninitializedTag2 = exifInterface2.buildUninitializedTag(i2);
            if (buildUninitializedTag2 == null) {
                throw new IOException("No definition for crucial exif tag: " + i2);
            }
            ifdData.setTag(buildUninitializedTag2);
        }
        if (this.mExifData.getIfdData(3) != null) {
            ExifInterface exifInterface3 = this.mInterface;
            int i3 = ExifInterface.TAG_INTEROPERABILITY_IFD;
            ExifTag buildUninitializedTag3 = exifInterface3.buildUninitializedTag(i3);
            if (buildUninitializedTag3 == null) {
                throw new IOException("No definition for crucial exif tag: " + i3);
            }
            ifdData2.setTag(buildUninitializedTag3);
        }
        IfdData ifdData3 = this.mExifData.getIfdData(1);
        if (this.mExifData.hasCompressedThumbnail()) {
            if (ifdData3 == null) {
                ifdData3 = new IfdData(1);
                this.mExifData.addIfdData(ifdData3);
            }
            ExifInterface exifInterface4 = this.mInterface;
            int i4 = ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT;
            ExifTag buildUninitializedTag4 = exifInterface4.buildUninitializedTag(i4);
            if (buildUninitializedTag4 == null) {
                throw new IOException("No definition for crucial exif tag: " + i4);
            }
            ifdData3.setTag(buildUninitializedTag4);
            ExifInterface exifInterface5 = this.mInterface;
            int i5 = ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH;
            ExifTag buildUninitializedTag5 = exifInterface5.buildUninitializedTag(i5);
            if (buildUninitializedTag5 == null) {
                throw new IOException("No definition for crucial exif tag: " + i5);
            }
            buildUninitializedTag5.setValue(this.mExifData.getCompressedThumbnail().length);
            ifdData3.setTag(buildUninitializedTag5);
            ifdData3.removeTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_STRIP_OFFSETS));
            ifdData3.removeTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_STRIP_BYTE_COUNTS));
        } else if (this.mExifData.hasUncompressedStrip()) {
            if (ifdData3 == null) {
                ifdData3 = new IfdData(1);
                this.mExifData.addIfdData(ifdData3);
            }
            int stripCount = this.mExifData.getStripCount();
            ExifInterface exifInterface6 = this.mInterface;
            int i6 = ExifInterface.TAG_STRIP_OFFSETS;
            ExifTag buildUninitializedTag6 = exifInterface6.buildUninitializedTag(i6);
            if (buildUninitializedTag6 == null) {
                throw new IOException("No definition for crucial exif tag: " + i6);
            }
            ExifInterface exifInterface7 = this.mInterface;
            int i7 = ExifInterface.TAG_STRIP_BYTE_COUNTS;
            ExifTag buildUninitializedTag7 = exifInterface7.buildUninitializedTag(i7);
            if (buildUninitializedTag7 == null) {
                throw new IOException("No definition for crucial exif tag: " + i7);
            }
            long[] jArr = new long[stripCount];
            for (int i8 = 0; i8 < this.mExifData.getStripCount(); i8++) {
                jArr[i8] = this.mExifData.getStrip(i8).length;
            }
            buildUninitializedTag7.setValue(jArr);
            ifdData3.setTag(buildUninitializedTag6);
            ifdData3.setTag(buildUninitializedTag7);
            ifdData3.removeTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT));
            ifdData3.removeTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH));
        } else {
            if (ifdData3 == null) {
                return;
            }
            ifdData3.removeTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_STRIP_OFFSETS));
            ifdData3.removeTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_STRIP_BYTE_COUNTS));
            ifdData3.removeTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT));
            ifdData3.removeTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH));
        }
    }

    public final int calculateAllOffset() {
        IfdData ifdData = this.mExifData.getIfdData(0);
        int calculateOffsetOfIfd = calculateOffsetOfIfd(ifdData, 8);
        ifdData.getTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_EXIF_IFD)).setValue(calculateOffsetOfIfd);
        IfdData ifdData2 = this.mExifData.getIfdData(2);
        int calculateOffsetOfIfd2 = calculateOffsetOfIfd(ifdData2, calculateOffsetOfIfd);
        IfdData ifdData3 = this.mExifData.getIfdData(3);
        if (ifdData3 != null) {
            ifdData2.getTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_INTEROPERABILITY_IFD)).setValue(calculateOffsetOfIfd2);
            calculateOffsetOfIfd2 = calculateOffsetOfIfd(ifdData3, calculateOffsetOfIfd2);
        }
        IfdData ifdData4 = this.mExifData.getIfdData(4);
        if (ifdData4 != null) {
            ifdData.getTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_GPS_IFD)).setValue(calculateOffsetOfIfd2);
            calculateOffsetOfIfd2 = calculateOffsetOfIfd(ifdData4, calculateOffsetOfIfd2);
        }
        IfdData ifdData5 = this.mExifData.getIfdData(1);
        if (ifdData5 != null) {
            ifdData.setOffsetToNextIfd(calculateOffsetOfIfd2);
            calculateOffsetOfIfd2 = calculateOffsetOfIfd(ifdData5, calculateOffsetOfIfd2);
        }
        if (this.mExifData.hasCompressedThumbnail()) {
            ifdData5.getTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT)).setValue(calculateOffsetOfIfd2);
            return calculateOffsetOfIfd2 + this.mExifData.getCompressedThumbnail().length;
        } else if (!this.mExifData.hasUncompressedStrip()) {
            return calculateOffsetOfIfd2;
        } else {
            long[] jArr = new long[this.mExifData.getStripCount()];
            for (int i = 0; i < this.mExifData.getStripCount(); i++) {
                jArr[i] = calculateOffsetOfIfd2;
                calculateOffsetOfIfd2 += this.mExifData.getStrip(i).length;
            }
            ifdData5.getTag(ExifInterface.getTrueTagKey(ExifInterface.TAG_STRIP_OFFSETS)).setValue(jArr);
            return calculateOffsetOfIfd2;
        }
    }

    public static void writeTagValue(ExifTag exifTag, OrderedDataOutputStream orderedDataOutputStream) throws IOException {
        int i = 0;
        switch (exifTag.getDataType()) {
            case 1:
            case 7:
                byte[] bArr = new byte[exifTag.getComponentCount()];
                exifTag.getBytes(bArr);
                orderedDataOutputStream.write(bArr);
                return;
            case 2:
                byte[] stringByte = exifTag.getStringByte();
                if (stringByte.length == exifTag.getComponentCount()) {
                    if (stringByte.length < 1) {
                        return;
                    }
                    stringByte[stringByte.length - 1] = 0;
                    orderedDataOutputStream.write(stringByte);
                    return;
                }
                orderedDataOutputStream.write(stringByte);
                orderedDataOutputStream.write(0);
                return;
            case 3:
                int componentCount = exifTag.getComponentCount();
                while (i < componentCount) {
                    orderedDataOutputStream.writeShort((short) exifTag.getValueAt(i));
                    i++;
                }
                return;
            case 4:
            case 9:
                int componentCount2 = exifTag.getComponentCount();
                while (i < componentCount2) {
                    orderedDataOutputStream.writeInt((int) exifTag.getValueAt(i));
                    i++;
                }
                return;
            case 5:
            case 10:
                int componentCount3 = exifTag.getComponentCount();
                while (i < componentCount3) {
                    orderedDataOutputStream.writeRational(exifTag.getRational(i));
                    i++;
                }
                return;
            case 6:
            case 8:
            default:
                return;
        }
    }
}
