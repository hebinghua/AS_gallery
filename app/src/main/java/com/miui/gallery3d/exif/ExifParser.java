package com.miui.gallery3d.exif;

import android.util.Log;
import com.nexstreaming.nexeditorsdk.nexCrop;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

/* loaded from: classes3.dex */
public class ExifParser {
    public int mApp1End;
    public boolean mContainExifData;
    public byte[] mDataAboveIfd0;
    public int mIfd0Position;
    public int mIfdType;
    public ImageEvent mImageEvent;
    public final ExifInterface mInterface;
    public ExifTag mJpegSizeTag;
    public boolean mNeedToParseOffsetsInCurrentIfd;
    public final int mOptions;
    public ExifTag mStripSizeTag;
    public ExifTag mTag;
    public int mTiffStartPosition;
    public final CountedDataInputStream mTiffStream;
    public static final Charset US_ASCII = Charset.forName("US-ASCII");
    public static final short TAG_EXIF_IFD = ExifInterface.getTrueTagKey(ExifInterface.TAG_EXIF_IFD);
    public static final short TAG_GPS_IFD = ExifInterface.getTrueTagKey(ExifInterface.TAG_GPS_IFD);
    public static final short TAG_INTEROPERABILITY_IFD = ExifInterface.getTrueTagKey(ExifInterface.TAG_INTEROPERABILITY_IFD);
    public static final short TAG_JPEG_INTERCHANGE_FORMAT = ExifInterface.getTrueTagKey(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT);
    public static final short TAG_JPEG_INTERCHANGE_FORMAT_LENGTH = ExifInterface.getTrueTagKey(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH);
    public static final short TAG_STRIP_OFFSETS = ExifInterface.getTrueTagKey(ExifInterface.TAG_STRIP_OFFSETS);
    public static final short TAG_STRIP_BYTE_COUNTS = ExifInterface.getTrueTagKey(ExifInterface.TAG_STRIP_BYTE_COUNTS);
    public int mIfdStartOffset = 0;
    public int mNumOfTagInIfd = 0;
    public int mOffsetToApp1EndFromSOF = 0;
    public final TreeMap<Integer, Object> mCorrespondingEvent = new TreeMap<>();

    public final boolean isIfdRequested(int i) {
        return i != 0 ? i != 1 ? i != 2 ? i != 3 ? i == 4 && (this.mOptions & 8) != 0 : (this.mOptions & 16) != 0 : (this.mOptions & 4) != 0 : (this.mOptions & 2) != 0 : (this.mOptions & 1) != 0;
    }

    public final boolean isThumbnailRequested() {
        return (this.mOptions & 32) != 0;
    }

    public ExifParser(InputStream inputStream, int i, ExifInterface exifInterface) throws IOException, ExifInvalidFormatException {
        this.mContainExifData = false;
        if (inputStream == null) {
            throw new IOException("Null argument inputStream to ExifParser");
        }
        this.mInterface = exifInterface;
        this.mContainExifData = seekTiffData(inputStream);
        CountedDataInputStream countedDataInputStream = new CountedDataInputStream(inputStream);
        this.mTiffStream = countedDataInputStream;
        this.mOptions = i;
        if (!this.mContainExifData) {
            return;
        }
        parseTiffHeader();
        long readUnsignedInt = countedDataInputStream.readUnsignedInt();
        if (readUnsignedInt > 2147483647L) {
            throw new ExifInvalidFormatException("Invalid offset " + readUnsignedInt);
        }
        int i2 = (int) readUnsignedInt;
        this.mIfd0Position = i2;
        this.mIfdType = 0;
        if (!isIfdRequested(0) && !needToParseOffsetsInCurrentIfd()) {
            return;
        }
        registerIfd(0, readUnsignedInt);
        if (readUnsignedInt == 8) {
            return;
        }
        byte[] bArr = new byte[i2 - 8];
        this.mDataAboveIfd0 = bArr;
        read(bArr);
    }

    public static ExifParser parse(InputStream inputStream, int i, ExifInterface exifInterface) throws IOException, ExifInvalidFormatException {
        return new ExifParser(inputStream, i, exifInterface);
    }

    public static ExifParser parse(InputStream inputStream, ExifInterface exifInterface) throws IOException, ExifInvalidFormatException {
        return new ExifParser(inputStream, 63, exifInterface);
    }

    public int next() throws IOException, ExifInvalidFormatException {
        if (!this.mContainExifData) {
            return 5;
        }
        int readByteCount = this.mTiffStream.getReadByteCount();
        int i = this.mIfdStartOffset + 2 + (this.mNumOfTagInIfd * 12);
        if (readByteCount < i) {
            ExifTag readTag = readTag();
            this.mTag = readTag;
            if (readTag == null) {
                return next();
            }
            if (this.mNeedToParseOffsetsInCurrentIfd) {
                checkOffsetOrImageTag(readTag);
            }
            return 1;
        }
        if (readByteCount == i) {
            if (this.mIfdType == 0) {
                long readUnsignedLong = readUnsignedLong();
                if ((isIfdRequested(1) || isThumbnailRequested()) && readUnsignedLong != 0) {
                    registerIfd(1, readUnsignedLong);
                }
            } else {
                int intValue = this.mCorrespondingEvent.size() > 0 ? this.mCorrespondingEvent.firstEntry().getKey().intValue() - this.mTiffStream.getReadByteCount() : 4;
                if (intValue < 4) {
                    Log.w("ExifParser", "Invalid size of link to next IFD: " + intValue);
                } else {
                    long readUnsignedLong2 = readUnsignedLong();
                    if (readUnsignedLong2 != 0) {
                        Log.w("ExifParser", "Invalid link to next IFD: " + readUnsignedLong2);
                    }
                }
            }
        }
        while (this.mCorrespondingEvent.size() != 0) {
            Map.Entry<Integer, Object> pollFirstEntry = this.mCorrespondingEvent.pollFirstEntry();
            Object value = pollFirstEntry.getValue();
            try {
                skipTo(pollFirstEntry.getKey().intValue());
                if (value instanceof IfdEvent) {
                    IfdEvent ifdEvent = (IfdEvent) value;
                    this.mIfdType = ifdEvent.ifd;
                    this.mNumOfTagInIfd = this.mTiffStream.readUnsignedShort();
                    int intValue2 = pollFirstEntry.getKey().intValue();
                    this.mIfdStartOffset = intValue2;
                    if ((this.mNumOfTagInIfd * 12) + intValue2 + 2 > this.mApp1End) {
                        Log.w("ExifParser", "Invalid size of IFD " + this.mIfdType);
                        return 5;
                    }
                    this.mNeedToParseOffsetsInCurrentIfd = needToParseOffsetsInCurrentIfd();
                    if (ifdEvent.isRequested) {
                        return 0;
                    }
                    skipRemainingTagsInCurrentIfd();
                } else if (value instanceof ImageEvent) {
                    ImageEvent imageEvent = (ImageEvent) value;
                    this.mImageEvent = imageEvent;
                    return imageEvent.type;
                } else {
                    ExifTagEvent exifTagEvent = (ExifTagEvent) value;
                    ExifTag exifTag = exifTagEvent.tag;
                    this.mTag = exifTag;
                    if (exifTag.getDataType() != 7) {
                        readFullTagValue(this.mTag);
                        checkOffsetOrImageTag(this.mTag);
                    }
                    if (exifTagEvent.isRequested) {
                        return 2;
                    }
                }
            } catch (IOException unused) {
                Log.w("ExifParser", "Failed to skip to data at: " + pollFirstEntry.getKey() + " for " + value.getClass().getName() + ", the file may be broken.");
            }
        }
        return 5;
    }

    public void skipRemainingTagsInCurrentIfd() throws IOException, ExifInvalidFormatException {
        int i = this.mIfdStartOffset + 2 + (this.mNumOfTagInIfd * 12);
        int readByteCount = this.mTiffStream.getReadByteCount();
        if (readByteCount > i) {
            return;
        }
        if (this.mNeedToParseOffsetsInCurrentIfd) {
            while (readByteCount < i) {
                ExifTag readTag = readTag();
                this.mTag = readTag;
                readByteCount += 12;
                if (readTag != null) {
                    checkOffsetOrImageTag(readTag);
                }
            }
        } else {
            skipTo(i);
        }
        long readUnsignedLong = readUnsignedLong();
        if (this.mIfdType != 0) {
            return;
        }
        if ((!isIfdRequested(1) && !isThumbnailRequested()) || readUnsignedLong <= 0) {
            return;
        }
        registerIfd(1, readUnsignedLong);
    }

    public final boolean needToParseOffsetsInCurrentIfd() {
        int i = this.mIfdType;
        if (i == 0) {
            return isIfdRequested(2) || isIfdRequested(4) || isIfdRequested(3) || isIfdRequested(1);
        } else if (i == 1) {
            return isThumbnailRequested();
        } else {
            if (i == 2) {
                return isIfdRequested(3);
            }
            return false;
        }
    }

    public ExifTag getTag() {
        return this.mTag;
    }

    public int getCurrentIfd() {
        return this.mIfdType;
    }

    public int getStripIndex() {
        return this.mImageEvent.stripIndex;
    }

    public int getStripSize() {
        ExifTag exifTag = this.mStripSizeTag;
        if (exifTag == null) {
            return 0;
        }
        return (int) exifTag.getValueAt(0);
    }

    public int getCompressedImageSize() {
        ExifTag exifTag = this.mJpegSizeTag;
        if (exifTag == null) {
            return 0;
        }
        return (int) exifTag.getValueAt(0);
    }

    public final void skipTo(int i) throws IOException {
        this.mTiffStream.skipTo(i);
        while (!this.mCorrespondingEvent.isEmpty() && this.mCorrespondingEvent.firstKey().intValue() < i) {
            this.mCorrespondingEvent.pollFirstEntry();
        }
    }

    public void registerForTagValue(ExifTag exifTag) {
        if (exifTag.getOffset() >= this.mTiffStream.getReadByteCount()) {
            this.mCorrespondingEvent.put(Integer.valueOf(exifTag.getOffset()), new ExifTagEvent(exifTag, true));
        }
    }

    public final void registerIfd(int i, long j) {
        this.mCorrespondingEvent.put(Integer.valueOf((int) j), new IfdEvent(i, isIfdRequested(i)));
    }

    public final void registerCompressedImage(long j) {
        this.mCorrespondingEvent.put(Integer.valueOf((int) j), new ImageEvent(3));
    }

    public final void registerUncompressedStrip(int i, long j) {
        this.mCorrespondingEvent.put(Integer.valueOf((int) j), new ImageEvent(4, i));
    }

    public final ExifTag readTag() throws IOException, ExifInvalidFormatException {
        int dataSize;
        short readShort = this.mTiffStream.readShort();
        short readShort2 = this.mTiffStream.readShort();
        long readUnsignedInt = this.mTiffStream.readUnsignedInt();
        boolean z = true;
        if (readUnsignedInt > 100000) {
            throw new ExifInvalidFormatException(String.format(Locale.US, "Number of component of tag %d [%d] is larger than [%d]", Short.valueOf(readShort), Long.valueOf(readUnsignedInt), Integer.valueOf((int) nexCrop.ABSTRACT_DIMENSION)));
        }
        if (!ExifTag.isValidType(readShort2)) {
            Log.w("ExifParser", String.format(Locale.ENGLISH, "Tag %04x: Invalid data type %d", Short.valueOf(readShort), Short.valueOf(readShort2)));
            this.mTiffStream.skip(4L);
            return null;
        }
        int i = (int) readUnsignedInt;
        int i2 = this.mIfdType;
        if (i == 0) {
            z = false;
        }
        ExifTag exifTag = new ExifTag(readShort, readShort2, i, i2, z);
        if (exifTag.getDataSize() > 4) {
            long readUnsignedInt2 = this.mTiffStream.readUnsignedInt();
            if (readUnsignedInt2 > 2147483647L) {
                throw new ExifInvalidFormatException("offset is larger then Integer.MAX_VALUE");
            }
            if (readUnsignedInt2 < this.mIfd0Position && readShort2 == 7) {
                byte[] bArr = new byte[i];
                System.arraycopy(this.mDataAboveIfd0, ((int) readUnsignedInt2) - 8, bArr, 0, i);
                exifTag.setValue(bArr);
            } else {
                exifTag.setOffset((int) readUnsignedInt2);
            }
        } else {
            boolean hasDefinedCount = exifTag.hasDefinedCount();
            exifTag.setHasDefinedCount(false);
            readFullTagValue(exifTag);
            exifTag.setHasDefinedCount(hasDefinedCount);
            this.mTiffStream.skip(4 - dataSize);
            exifTag.setOffset(this.mTiffStream.getReadByteCount() - 4);
        }
        return exifTag;
    }

    public final void checkOffsetOrImageTag(ExifTag exifTag) {
        if (exifTag.getComponentCount() == 0) {
            return;
        }
        short tagId = exifTag.getTagId();
        int ifd = exifTag.getIfd();
        if (tagId == TAG_EXIF_IFD && checkAllowed(ifd, ExifInterface.TAG_EXIF_IFD)) {
            if (!isIfdRequested(2) && !isIfdRequested(3)) {
                return;
            }
            registerIfd(2, exifTag.getValueAt(0));
        } else if (tagId == TAG_GPS_IFD && checkAllowed(ifd, ExifInterface.TAG_GPS_IFD)) {
            if (!isIfdRequested(4)) {
                return;
            }
            registerIfd(4, exifTag.getValueAt(0));
        } else if (tagId == TAG_INTEROPERABILITY_IFD && checkAllowed(ifd, ExifInterface.TAG_INTEROPERABILITY_IFD)) {
            if (!isIfdRequested(3)) {
                return;
            }
            registerIfd(3, exifTag.getValueAt(0));
        } else if (tagId == TAG_JPEG_INTERCHANGE_FORMAT && checkAllowed(ifd, ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT)) {
            if (!isThumbnailRequested()) {
                return;
            }
            registerCompressedImage(exifTag.getValueAt(0));
        } else if (tagId == TAG_JPEG_INTERCHANGE_FORMAT_LENGTH && checkAllowed(ifd, ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH)) {
            if (!isThumbnailRequested()) {
                return;
            }
            this.mJpegSizeTag = exifTag;
        } else if (tagId == TAG_STRIP_OFFSETS && checkAllowed(ifd, ExifInterface.TAG_STRIP_OFFSETS)) {
            if (!isThumbnailRequested()) {
                return;
            }
            if (exifTag.hasValue()) {
                for (int i = 0; i < exifTag.getComponentCount(); i++) {
                    if (exifTag.getDataType() == 3) {
                        registerUncompressedStrip(i, exifTag.getValueAt(i));
                    } else {
                        registerUncompressedStrip(i, exifTag.getValueAt(i));
                    }
                }
                return;
            }
            this.mCorrespondingEvent.put(Integer.valueOf(exifTag.getOffset()), new ExifTagEvent(exifTag, false));
        } else if (tagId == TAG_STRIP_BYTE_COUNTS && checkAllowed(ifd, ExifInterface.TAG_STRIP_BYTE_COUNTS) && isThumbnailRequested() && exifTag.hasValue()) {
            this.mStripSizeTag = exifTag;
        }
    }

    public final boolean checkAllowed(int i, int i2) {
        int i3 = this.mInterface.getTagInfo().get(i2);
        if (i3 == 0) {
            return false;
        }
        return ExifInterface.isIfdAllowed(i3, i);
    }

    public void readFullTagValue(ExifTag exifTag) throws IOException, ExifInvalidFormatException {
        short dataType = exifTag.getDataType();
        if (dataType == 2 || dataType == 7 || dataType == 1) {
            int componentCount = exifTag.getComponentCount();
            if (this.mCorrespondingEvent.size() > 0 && this.mCorrespondingEvent.firstEntry().getKey().intValue() < this.mTiffStream.getReadByteCount() + componentCount) {
                Object value = this.mCorrespondingEvent.firstEntry().getValue();
                if (value instanceof ImageEvent) {
                    Log.w("ExifParser", "Thumbnail overlaps value for tag: \n" + exifTag.toString());
                    Map.Entry<Integer, Object> pollFirstEntry = this.mCorrespondingEvent.pollFirstEntry();
                    Log.w("ExifParser", "Invalid thumbnail offset: " + pollFirstEntry.getKey());
                } else {
                    if (value instanceof IfdEvent) {
                        Log.w("ExifParser", "Ifd " + ((IfdEvent) value).ifd + " overlaps value for tag: \n" + exifTag.toString());
                    } else if (value instanceof ExifTagEvent) {
                        Log.w("ExifParser", "Tag value for tag: \n" + ((ExifTagEvent) value).tag.toString() + " overlaps value for tag: \n" + exifTag.toString());
                    }
                    int intValue = this.mCorrespondingEvent.firstEntry().getKey().intValue() - this.mTiffStream.getReadByteCount();
                    Log.w("ExifParser", "Invalid size of tag: \n" + exifTag.toString() + " setting count to: " + intValue);
                    exifTag.forceSetComponentCount(intValue);
                }
            }
        }
        int componentCount2 = exifTag.getComponentCount();
        int i = 0;
        if (componentCount2 < 0) {
            throw new ExifInvalidFormatException(String.format(Locale.US, "Invalid size [%d] of tag [%s]", Integer.valueOf(componentCount2), exifTag));
        }
        switch (exifTag.getDataType()) {
            case 1:
            case 7:
                byte[] bArr = new byte[componentCount2];
                read(bArr);
                exifTag.setValue(bArr);
                return;
            case 2:
                exifTag.setValue(readString(componentCount2));
                return;
            case 3:
                int[] iArr = new int[componentCount2];
                while (i < componentCount2) {
                    iArr[i] = readUnsignedShort();
                    i++;
                }
                exifTag.setValue(iArr);
                return;
            case 4:
                long[] jArr = new long[componentCount2];
                while (i < componentCount2) {
                    jArr[i] = readUnsignedLong();
                    i++;
                }
                exifTag.setValue(jArr);
                return;
            case 5:
                Rational[] rationalArr = new Rational[componentCount2];
                while (i < componentCount2) {
                    rationalArr[i] = readUnsignedRational();
                    i++;
                }
                exifTag.setValue(rationalArr);
                return;
            case 6:
            case 8:
            default:
                return;
            case 9:
                int[] iArr2 = new int[componentCount2];
                while (i < componentCount2) {
                    iArr2[i] = readLong();
                    i++;
                }
                exifTag.setValue(iArr2);
                return;
            case 10:
                Rational[] rationalArr2 = new Rational[componentCount2];
                while (i < componentCount2) {
                    rationalArr2[i] = readRational();
                    i++;
                }
                exifTag.setValue(rationalArr2);
                return;
        }
    }

    public final void parseTiffHeader() throws IOException, ExifInvalidFormatException {
        short readShort = this.mTiffStream.readShort();
        if (18761 == readShort) {
            this.mTiffStream.setByteOrder(ByteOrder.LITTLE_ENDIAN);
        } else if (19789 == readShort) {
            this.mTiffStream.setByteOrder(ByteOrder.BIG_ENDIAN);
        } else {
            throw new ExifInvalidFormatException("Invalid TIFF header");
        }
        if (this.mTiffStream.readShort() == 42) {
            return;
        }
        throw new ExifInvalidFormatException("Invalid TIFF header");
    }

    public final boolean seekTiffData(InputStream inputStream) throws IOException, ExifInvalidFormatException {
        CountedDataInputStream countedDataInputStream = new CountedDataInputStream(inputStream);
        if (countedDataInputStream.readShort() != -40) {
            throw new ExifInvalidFormatException("Invalid JPEG format");
        }
        for (short readShort = countedDataInputStream.readShort(); readShort != -39 && !JpegHeader.isSofMarker(readShort); readShort = countedDataInputStream.readShort()) {
            int readUnsignedShort = countedDataInputStream.readUnsignedShort();
            if (readShort == -31 && readUnsignedShort >= 8) {
                int readInt = countedDataInputStream.readInt();
                short readShort2 = countedDataInputStream.readShort();
                readUnsignedShort -= 6;
                if (readInt == 1165519206 && readShort2 == 0) {
                    int readByteCount = countedDataInputStream.getReadByteCount();
                    this.mTiffStartPosition = readByteCount;
                    this.mApp1End = readUnsignedShort;
                    this.mOffsetToApp1EndFromSOF = readByteCount + readUnsignedShort;
                    return true;
                }
            }
            if (readUnsignedShort >= 2) {
                long j = readUnsignedShort - 2;
                if (j == countedDataInputStream.skip(j)) {
                }
            }
            Log.w("ExifParser", "Invalid JPEG format.");
        }
        return false;
    }

    public int getOffsetToExifEndFromSOF() {
        return this.mOffsetToApp1EndFromSOF;
    }

    public int getTiffStartPosition() {
        return this.mTiffStartPosition;
    }

    public int read(byte[] bArr) throws IOException {
        return this.mTiffStream.read(bArr);
    }

    public String readString(int i) throws IOException {
        return readString(i, US_ASCII);
    }

    public String readString(int i, Charset charset) throws IOException {
        return i > 0 ? this.mTiffStream.readString(i, charset) : "";
    }

    public int readUnsignedShort() throws IOException {
        return this.mTiffStream.readShort() & 65535;
    }

    public long readUnsignedLong() throws IOException {
        return readLong() & 4294967295L;
    }

    public Rational readUnsignedRational() throws IOException {
        return new Rational(readUnsignedLong(), readUnsignedLong());
    }

    public int readLong() throws IOException {
        return this.mTiffStream.readInt();
    }

    public Rational readRational() throws IOException {
        return new Rational(readLong(), readLong());
    }

    /* loaded from: classes3.dex */
    public static class ImageEvent {
        public int stripIndex;
        public int type;

        public ImageEvent(int i) {
            this.stripIndex = 0;
            this.type = i;
        }

        public ImageEvent(int i, int i2) {
            this.type = i;
            this.stripIndex = i2;
        }
    }

    /* loaded from: classes3.dex */
    public static class IfdEvent {
        public int ifd;
        public boolean isRequested;

        public IfdEvent(int i, boolean z) {
            this.ifd = i;
            this.isRequested = z;
        }
    }

    /* loaded from: classes3.dex */
    public static class ExifTagEvent {
        public boolean isRequested;
        public ExifTag tag;

        public ExifTagEvent(ExifTag exifTag, boolean z) {
            this.tag = exifTag;
            this.isRequested = z;
        }
    }

    public ByteOrder getByteOrder() {
        return this.mTiffStream.getByteOrder();
    }
}
