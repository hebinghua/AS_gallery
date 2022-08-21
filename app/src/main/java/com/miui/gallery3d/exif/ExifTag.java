package com.miui.gallery3d.exif;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Locale;

/* loaded from: classes3.dex */
public class ExifTag {
    public static final int[] TYPE_TO_SIZE_MAP;
    public int mComponentCountActual;
    public final short mDataType;
    public boolean mHasDefinedDefaultComponentCount;
    public int mIfd;
    public int mOffset;
    public final short mTagId;
    public Object mValue = null;
    public static Charset US_ASCII = Charset.forName("US-ASCII");
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("yyyy:MM:dd kk:mm:ss");

    public static String convertTypeToString(short s) {
        switch (s) {
            case 1:
                return "UNSIGNED_BYTE";
            case 2:
                return "ASCII";
            case 3:
                return "UNSIGNED_SHORT";
            case 4:
                return "UNSIGNED_LONG";
            case 5:
                return "UNSIGNED_RATIONAL";
            case 6:
            case 8:
            default:
                return "";
            case 7:
                return "UNDEFINED";
            case 9:
                return "LONG";
            case 10:
                return "RATIONAL";
        }
    }

    public static boolean isValidIfd(int i) {
        return i == 0 || i == 1 || i == 2 || i == 3 || i == 4;
    }

    public static boolean isValidType(short s) {
        return s == 1 || s == 2 || s == 3 || s == 4 || s == 5 || s == 7 || s == 9 || s == 10;
    }

    static {
        TYPE_TO_SIZE_MAP = r0;
        int[] iArr = {0, 1, 1, 2, 4, 8, 0, 1, 0, 4, 8};
    }

    public ExifTag(short s, short s2, int i, int i2, boolean z) {
        this.mTagId = s;
        this.mDataType = s2;
        this.mComponentCountActual = i;
        this.mHasDefinedDefaultComponentCount = z;
        this.mIfd = i2;
    }

    public static int getElementSize(short s) {
        return TYPE_TO_SIZE_MAP[s];
    }

    public int getIfd() {
        return this.mIfd;
    }

    public void setIfd(int i) {
        this.mIfd = i;
    }

    public short getTagId() {
        return this.mTagId;
    }

    public short getDataType() {
        return this.mDataType;
    }

    public int getDataSize() {
        return getComponentCount() * getElementSize(getDataType());
    }

    public int getComponentCount() {
        return this.mComponentCountActual;
    }

    public void forceSetComponentCount(int i) {
        this.mComponentCountActual = i;
    }

    public boolean hasValue() {
        return this.mValue != null;
    }

    public boolean setValue(int[] iArr) {
        if (checkBadComponentCount(iArr.length)) {
            return false;
        }
        short s = this.mDataType;
        if (s != 3 && s != 9 && s != 4) {
            return false;
        }
        if (s == 3 && checkOverflowForUnsignedShort(iArr)) {
            return false;
        }
        if (this.mDataType == 4 && checkOverflowForUnsignedLong(iArr)) {
            return false;
        }
        long[] jArr = new long[iArr.length];
        for (int i = 0; i < iArr.length; i++) {
            jArr[i] = iArr[i];
        }
        this.mValue = jArr;
        this.mComponentCountActual = iArr.length;
        return true;
    }

    public boolean setValue(int i) {
        return setValue(new int[]{i});
    }

    public boolean setValue(long[] jArr) {
        if (checkBadComponentCount(jArr.length) || this.mDataType != 4 || checkOverflowForUnsignedLong(jArr)) {
            return false;
        }
        this.mValue = jArr;
        this.mComponentCountActual = jArr.length;
        return true;
    }

    public boolean setValue(long j) {
        return setValue(new long[]{j});
    }

    public boolean setValue(String str) {
        short s = this.mDataType;
        if (s == 2 || s == 7) {
            byte[] bytes = str.getBytes(US_ASCII);
            if (bytes.length > 0) {
                byte[] copyOf = (bytes[bytes.length - 1] == 0 || this.mDataType == 7) ? bytes : Arrays.copyOf(bytes, bytes.length + 1);
                if (bytes[bytes.length - 1] != 0 && this.mDataType != 7) {
                    this.mComponentCountActual++;
                }
                bytes = copyOf;
            } else if (this.mDataType == 2 && this.mComponentCountActual == 1) {
                bytes = new byte[]{0};
            }
            int length = bytes.length;
            if (checkBadComponentCount(length)) {
                return false;
            }
            this.mComponentCountActual = length;
            this.mValue = bytes;
            return true;
        }
        return false;
    }

    public boolean setValue(Rational[] rationalArr) {
        if (checkBadComponentCount(rationalArr.length)) {
            return false;
        }
        short s = this.mDataType;
        if (s != 5 && s != 10) {
            return false;
        }
        if (s == 5 && checkOverflowForUnsignedRational(rationalArr)) {
            return false;
        }
        if (this.mDataType == 10 && checkOverflowForRational(rationalArr)) {
            return false;
        }
        this.mValue = rationalArr;
        this.mComponentCountActual = rationalArr.length;
        return true;
    }

    public boolean setValue(Rational rational) {
        return setValue(new Rational[]{rational});
    }

    public boolean setValue(byte[] bArr, int i, int i2) {
        if (checkBadComponentCount(i2)) {
            return false;
        }
        short s = this.mDataType;
        if (s != 1 && s != 7) {
            return false;
        }
        byte[] bArr2 = new byte[i2];
        this.mValue = bArr2;
        System.arraycopy(bArr, i, bArr2, 0, i2);
        this.mComponentCountActual = i2;
        return true;
    }

    public boolean setValue(byte[] bArr) {
        return setValue(bArr, 0, bArr.length);
    }

    public boolean setValue(byte b) {
        return setValue(new byte[]{b});
    }

    public boolean setValue(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Short) {
            return setValue(((Short) obj).shortValue() & 65535);
        }
        if (obj instanceof String) {
            return setValue((String) obj);
        }
        if (obj instanceof int[]) {
            return setValue((int[]) obj);
        }
        if (obj instanceof long[]) {
            return setValue((long[]) obj);
        }
        if (obj instanceof Rational) {
            return setValue((Rational) obj);
        }
        if (obj instanceof Rational[]) {
            return setValue((Rational[]) obj);
        }
        if (obj instanceof byte[]) {
            return setValue((byte[]) obj);
        }
        if (obj instanceof Integer) {
            return setValue(((Integer) obj).intValue());
        }
        if (obj instanceof Long) {
            return setValue(((Long) obj).longValue());
        }
        if (obj instanceof Byte) {
            return setValue(((Byte) obj).byteValue());
        }
        if (obj instanceof Short[]) {
            Short[] shArr = (Short[]) obj;
            int[] iArr = new int[shArr.length];
            for (int i = 0; i < shArr.length; i++) {
                iArr[i] = shArr[i] == null ? 0 : shArr[i].shortValue() & 65535;
            }
            return setValue(iArr);
        } else if (obj instanceof Integer[]) {
            Integer[] numArr = (Integer[]) obj;
            int[] iArr2 = new int[numArr.length];
            for (int i2 = 0; i2 < numArr.length; i2++) {
                iArr2[i2] = numArr[i2] == null ? 0 : numArr[i2].intValue();
            }
            return setValue(iArr2);
        } else if (obj instanceof Long[]) {
            Long[] lArr = (Long[]) obj;
            long[] jArr = new long[lArr.length];
            for (int i3 = 0; i3 < lArr.length; i3++) {
                jArr[i3] = lArr[i3] == null ? 0L : lArr[i3].longValue();
            }
            return setValue(jArr);
        } else if (!(obj instanceof Byte[])) {
            return false;
        } else {
            Byte[] bArr = (Byte[]) obj;
            byte[] bArr2 = new byte[bArr.length];
            for (int i4 = 0; i4 < bArr.length; i4++) {
                bArr2[i4] = bArr[i4] == null ? (byte) 0 : bArr[i4].byteValue();
            }
            return setValue(bArr2);
        }
    }

    public String getValueAsString() {
        Object obj = this.mValue;
        if (obj == null) {
            return null;
        }
        if (obj instanceof String) {
            return (String) obj;
        }
        if (!(obj instanceof byte[])) {
            return null;
        }
        return new String((byte[]) obj, US_ASCII);
    }

    public byte[] getValueAsBytes() {
        Object obj = this.mValue;
        if (obj instanceof byte[]) {
            return (byte[]) obj;
        }
        return null;
    }

    public int[] getValueAsInts() {
        Object obj = this.mValue;
        int[] iArr = null;
        if (obj == null) {
            return null;
        }
        if (obj instanceof long[]) {
            long[] jArr = (long[]) obj;
            iArr = new int[jArr.length];
            for (int i = 0; i < jArr.length; i++) {
                iArr[i] = (int) jArr[i];
            }
        }
        return iArr;
    }

    public Object getValue() {
        return this.mValue;
    }

    public String forceGetValueAsString() {
        Object obj = this.mValue;
        if (obj == null) {
            return "";
        }
        if (obj instanceof byte[]) {
            if (this.mDataType == 2) {
                return new String((byte[]) obj, US_ASCII);
            }
            return Arrays.toString((byte[]) obj);
        } else if (obj instanceof long[]) {
            if (((long[]) obj).length == 1) {
                return String.valueOf(((long[]) obj)[0]);
            }
            return Arrays.toString((long[]) obj);
        } else if (obj instanceof Object[]) {
            if (((Object[]) obj).length == 1) {
                Object obj2 = ((Object[]) obj)[0];
                return obj2 == null ? "" : obj2.toString();
            }
            return Arrays.toString((Object[]) obj);
        } else {
            return obj.toString();
        }
    }

    public long getValueAt(int i) {
        Object obj = this.mValue;
        if (obj instanceof long[]) {
            return ((long[]) obj)[i];
        }
        if (obj instanceof byte[]) {
            return ((byte[]) obj)[i];
        }
        throw new IllegalArgumentException("Cannot get integer value from " + convertTypeToString(this.mDataType));
    }

    public byte[] getStringByte() {
        return (byte[]) this.mValue;
    }

    public Rational getRational(int i) {
        short s = this.mDataType;
        if (s != 10 && s != 5) {
            throw new IllegalArgumentException("Cannot get RATIONAL value from " + convertTypeToString(this.mDataType));
        }
        return ((Rational[]) this.mValue)[i];
    }

    public void getBytes(byte[] bArr) {
        getBytes(bArr, 0, bArr.length);
    }

    public void getBytes(byte[] bArr, int i, int i2) {
        short s = this.mDataType;
        if (s != 7 && s != 1) {
            throw new IllegalArgumentException("Cannot get BYTE value from " + convertTypeToString(this.mDataType));
        }
        Object obj = this.mValue;
        int i3 = this.mComponentCountActual;
        if (i2 > i3) {
            i2 = i3;
        }
        System.arraycopy(obj, 0, bArr, i, i2);
    }

    public int getOffset() {
        return this.mOffset;
    }

    public void setOffset(int i) {
        this.mOffset = i;
    }

    public void setHasDefinedCount(boolean z) {
        this.mHasDefinedDefaultComponentCount = z;
    }

    public boolean hasDefinedCount() {
        return this.mHasDefinedDefaultComponentCount;
    }

    public final boolean checkBadComponentCount(int i) {
        return this.mHasDefinedDefaultComponentCount && this.mComponentCountActual != i;
    }

    public final boolean checkOverflowForUnsignedShort(int[] iArr) {
        for (int i : iArr) {
            if (i > 65535 || i < 0) {
                return true;
            }
        }
        return false;
    }

    public final boolean checkOverflowForUnsignedLong(long[] jArr) {
        for (long j : jArr) {
            if (j < 0 || j > 4294967295L) {
                return true;
            }
        }
        return false;
    }

    public final boolean checkOverflowForUnsignedLong(int[] iArr) {
        for (int i : iArr) {
            if (i < 0) {
                return true;
            }
        }
        return false;
    }

    public final boolean checkOverflowForUnsignedRational(Rational[] rationalArr) {
        for (Rational rational : rationalArr) {
            if (rational.getNumerator() < 0 || rational.getDenominator() < 0 || rational.getNumerator() > 4294967295L || rational.getDenominator() > 4294967295L) {
                return true;
            }
        }
        return false;
    }

    public final boolean checkOverflowForRational(Rational[] rationalArr) {
        for (Rational rational : rationalArr) {
            if (rational.getNumerator() < -2147483648L || rational.getDenominator() < -2147483648L || rational.getNumerator() > 2147483647L || rational.getDenominator() > 2147483647L) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof ExifTag)) {
            ExifTag exifTag = (ExifTag) obj;
            if (exifTag.mTagId != this.mTagId || exifTag.mComponentCountActual != this.mComponentCountActual || exifTag.mDataType != this.mDataType) {
                return false;
            }
            Object obj2 = this.mValue;
            if (obj2 == null) {
                return exifTag.mValue == null;
            }
            Object obj3 = exifTag.mValue;
            if (obj3 == null) {
                return false;
            }
            if (obj2 instanceof long[]) {
                if (obj3 instanceof long[]) {
                    return Arrays.equals((long[]) obj2, (long[]) obj3);
                }
                return false;
            } else if (obj2 instanceof Rational[]) {
                if (obj3 instanceof Rational[]) {
                    return Arrays.equals((Rational[]) obj2, (Rational[]) obj3);
                }
                return false;
            } else if (obj2 instanceof byte[]) {
                if (obj3 instanceof byte[]) {
                    return Arrays.equals((byte[]) obj2, (byte[]) obj3);
                }
                return false;
            } else {
                return obj2.equals(obj3);
            }
        }
        return false;
    }

    public String toString() {
        return String.format(Locale.ENGLISH, "tag id: %04X\n", Short.valueOf(this.mTagId)) + "ifd id: " + this.mIfd + "\ntype: " + convertTypeToString(this.mDataType) + "\ncount: " + this.mComponentCountActual + "\noffset: " + this.mOffset + "\nvalue: " + forceGetValueAsString() + "\n";
    }
}
