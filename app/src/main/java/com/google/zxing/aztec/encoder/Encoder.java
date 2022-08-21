package com.google.zxing.aztec.encoder;

import com.google.zxing.common.BitArray;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.reedsolomon.GenericGF;
import com.google.zxing.common.reedsolomon.ReedSolomonEncoder;

/* loaded from: classes.dex */
public final class Encoder {
    public static final int[] WORD_SIZE = {4, 6, 6, 8, 8, 8, 8, 8, 8, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 10, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12};

    public static int totalBitsInLayer(int i, boolean z) {
        return ((z ? 88 : 112) + (i * 16)) * i;
    }

    public static AztecCode encode(byte[] bArr, int i, int i2) {
        boolean z;
        int i3;
        BitArray bitArray;
        int i4;
        int i5;
        int i6;
        BitArray encode = new HighLevelEncoder(bArr).encode();
        int size = ((encode.getSize() * i) / 100) + 11;
        int size2 = encode.getSize() + size;
        int i7 = 32;
        int i8 = 4;
        boolean z2 = 0;
        if (i2 != 0) {
            boolean z3 = i2 < 0;
            int abs = Math.abs(i2);
            if (z3) {
                i7 = 4;
            }
            if (abs > i7) {
                throw new IllegalArgumentException(String.format("Illegal value %s for layers", Integer.valueOf(i2)));
            }
            i5 = totalBitsInLayer(abs, z3);
            i4 = WORD_SIZE[abs];
            int i9 = i5 - (i5 % i4);
            bitArray = stuffBits(encode, i4);
            if (bitArray.getSize() + size > i9) {
                throw new IllegalArgumentException("Data to large for user specified layer");
            }
            if (z3 && bitArray.getSize() > i4 * 64) {
                throw new IllegalArgumentException("Data to large for user specified layer");
            }
            z = z3;
            i3 = abs;
        } else {
            BitArray bitArray2 = null;
            int i10 = 0;
            int i11 = 0;
            while (i10 <= 32) {
                z = i10 <= 3 ? true : z2;
                i3 = z ? i10 + 1 : i10;
                int i12 = totalBitsInLayer(i3, z);
                if (size2 <= i12) {
                    int[] iArr = WORD_SIZE;
                    if (i11 != iArr[i3]) {
                        int i13 = iArr[i3];
                        i11 = i13;
                        bitArray2 = stuffBits(encode, i13);
                    }
                    int i14 = i12 - (i12 % i11);
                    if ((!z || bitArray2.getSize() <= i11 * 64) && bitArray2.getSize() + size <= i14) {
                        bitArray = bitArray2;
                        i4 = i11;
                        i5 = i12;
                    }
                }
                i10++;
                i8 = 4;
                z2 = 0;
            }
            throw new IllegalArgumentException("Data too large for an Aztec code");
        }
        BitArray generateCheckWords = generateCheckWords(bitArray, i5, i4);
        int size3 = bitArray.getSize() / i4;
        BitArray generateModeMessage = generateModeMessage(z, i3, size3);
        int i15 = z ? (i3 * 4) + 11 : (i3 * 4) + 14;
        int[] iArr2 = new int[i15];
        int i16 = 2;
        if (z) {
            for (int i17 = z2; i17 < i15; i17++) {
                iArr2[i17] = i17;
            }
            i6 = i15;
        } else {
            int i18 = i15 / 2;
            int i19 = i15 + 1 + (((i18 - 1) / 15) * 2);
            int i20 = i19 / 2;
            int i21 = z2;
            while (i21 < i18) {
                int i22 = (i21 / 15) + i21;
                iArr2[(i18 - i21) - 1] = (i20 - i22) - 1;
                iArr2[i18 + i21] = i20 + i22 + 1;
                i21++;
                i16 = 2;
                i8 = 4;
                z2 = 0;
            }
            i6 = i19;
        }
        BitMatrix bitMatrix = new BitMatrix(i6);
        int i23 = z2;
        int i24 = i23;
        while (i23 < i3) {
            int i25 = (i3 - i23) * i8;
            int i26 = z ? i25 + 9 : i25 + 12;
            int i27 = z2;
            while (i27 < i26) {
                int i28 = i27 * 2;
                int i29 = z2;
                while (i29 < i16) {
                    if (generateCheckWords.get(i24 + i28 + i29)) {
                        int i30 = i23 * 2;
                        bitMatrix.set(iArr2[i30 + i29], iArr2[i30 + i27]);
                    }
                    if (generateCheckWords.get(i24 + (i26 * 2) + i28 + i29)) {
                        int i31 = i23 * 2;
                        bitMatrix.set(iArr2[i31 + i27], iArr2[((i15 - 1) - i31) - i29]);
                    }
                    if (generateCheckWords.get(i24 + (i26 * 4) + i28 + i29)) {
                        int i32 = (i15 - 1) - (i23 * 2);
                        bitMatrix.set(iArr2[i32 - i29], iArr2[i32 - i27]);
                    }
                    if (generateCheckWords.get(i24 + (i26 * 6) + i28 + i29)) {
                        int i33 = i23 * 2;
                        bitMatrix.set(iArr2[((i15 - 1) - i33) - i27], iArr2[i33 + i29]);
                    }
                    i29++;
                    i16 = 2;
                    z2 = 0;
                }
                i27++;
                i8 = 4;
            }
            i24 += i26 * 8;
            i23++;
        }
        drawModeMessage(bitMatrix, z, i6, generateModeMessage);
        if (z) {
            drawBullsEye(bitMatrix, i6 / 2, 5);
        } else {
            int i34 = i6 / 2;
            drawBullsEye(bitMatrix, i34, 7);
            int i35 = z2;
            int i36 = i35;
            while (i35 < (i15 / 2) - 1) {
                for (int i37 = i34 & 1; i37 < i6; i37 += 2) {
                    int i38 = i34 - i36;
                    bitMatrix.set(i38, i37);
                    int i39 = i34 + i36;
                    bitMatrix.set(i39, i37);
                    bitMatrix.set(i37, i38);
                    bitMatrix.set(i37, i39);
                }
                i35 += 15;
                i36 += 16;
            }
        }
        AztecCode aztecCode = new AztecCode();
        aztecCode.setCompact(z);
        aztecCode.setSize(i6);
        aztecCode.setLayers(i3);
        aztecCode.setCodeWords(size3);
        aztecCode.setMatrix(bitMatrix);
        return aztecCode;
    }

    public static void drawBullsEye(BitMatrix bitMatrix, int i, int i2) {
        for (int i3 = 0; i3 < i2; i3 += 2) {
            int i4 = i - i3;
            int i5 = i4;
            while (true) {
                int i6 = i + i3;
                if (i5 > i6) {
                    break;
                }
                bitMatrix.set(i5, i4);
                bitMatrix.set(i5, i6);
                bitMatrix.set(i4, i5);
                bitMatrix.set(i6, i5);
                i5++;
            }
        }
        int i7 = i - i2;
        bitMatrix.set(i7, i7);
        int i8 = i7 + 1;
        bitMatrix.set(i8, i7);
        bitMatrix.set(i7, i8);
        int i9 = i + i2;
        bitMatrix.set(i9, i7);
        bitMatrix.set(i9, i8);
        bitMatrix.set(i9, i9 - 1);
    }

    public static BitArray generateModeMessage(boolean z, int i, int i2) {
        BitArray bitArray = new BitArray();
        if (z) {
            bitArray.appendBits(i - 1, 2);
            bitArray.appendBits(i2 - 1, 6);
            return generateCheckWords(bitArray, 28, 4);
        }
        bitArray.appendBits(i - 1, 5);
        bitArray.appendBits(i2 - 1, 11);
        return generateCheckWords(bitArray, 40, 4);
    }

    public static void drawModeMessage(BitMatrix bitMatrix, boolean z, int i, BitArray bitArray) {
        int i2 = i / 2;
        int i3 = 0;
        if (z) {
            while (i3 < 7) {
                int i4 = (i2 - 3) + i3;
                if (bitArray.get(i3)) {
                    bitMatrix.set(i4, i2 - 5);
                }
                if (bitArray.get(i3 + 7)) {
                    bitMatrix.set(i2 + 5, i4);
                }
                if (bitArray.get(20 - i3)) {
                    bitMatrix.set(i4, i2 + 5);
                }
                if (bitArray.get(27 - i3)) {
                    bitMatrix.set(i2 - 5, i4);
                }
                i3++;
            }
            return;
        }
        while (i3 < 10) {
            int i5 = (i2 - 5) + i3 + (i3 / 5);
            if (bitArray.get(i3)) {
                bitMatrix.set(i5, i2 - 7);
            }
            if (bitArray.get(i3 + 10)) {
                bitMatrix.set(i2 + 7, i5);
            }
            if (bitArray.get(29 - i3)) {
                bitMatrix.set(i5, i2 + 7);
            }
            if (bitArray.get(39 - i3)) {
                bitMatrix.set(i2 - 7, i5);
            }
            i3++;
        }
    }

    public static BitArray generateCheckWords(BitArray bitArray, int i, int i2) {
        ReedSolomonEncoder reedSolomonEncoder = new ReedSolomonEncoder(getGF(i2));
        int i3 = i / i2;
        int[] bitsToWords = bitsToWords(bitArray, i2, i3);
        reedSolomonEncoder.encode(bitsToWords, i3 - (bitArray.getSize() / i2));
        BitArray bitArray2 = new BitArray();
        bitArray2.appendBits(0, i % i2);
        for (int i4 : bitsToWords) {
            bitArray2.appendBits(i4, i2);
        }
        return bitArray2;
    }

    public static int[] bitsToWords(BitArray bitArray, int i, int i2) {
        int[] iArr = new int[i2];
        int size = bitArray.getSize() / i;
        for (int i3 = 0; i3 < size; i3++) {
            int i4 = 0;
            for (int i5 = 0; i5 < i; i5++) {
                i4 |= bitArray.get((i3 * i) + i5) ? 1 << ((i - i5) - 1) : 0;
            }
            iArr[i3] = i4;
        }
        return iArr;
    }

    public static GenericGF getGF(int i) {
        if (i != 4) {
            if (i == 6) {
                return GenericGF.AZTEC_DATA_6;
            }
            if (i == 8) {
                return GenericGF.AZTEC_DATA_8;
            }
            if (i == 10) {
                return GenericGF.AZTEC_DATA_10;
            }
            if (i == 12) {
                return GenericGF.AZTEC_DATA_12;
            }
            return null;
        }
        return GenericGF.AZTEC_PARAM;
    }

    public static BitArray stuffBits(BitArray bitArray, int i) {
        BitArray bitArray2 = new BitArray();
        int size = bitArray.getSize();
        int i2 = (1 << i) - 2;
        int i3 = 0;
        while (i3 < size) {
            int i4 = 0;
            for (int i5 = 0; i5 < i; i5++) {
                int i6 = i3 + i5;
                if (i6 >= size || bitArray.get(i6)) {
                    i4 |= 1 << ((i - 1) - i5);
                }
            }
            int i7 = i4 & i2;
            if (i7 == i2) {
                bitArray2.appendBits(i7, i);
            } else if (i7 == 0) {
                bitArray2.appendBits(i4 | 1, i);
            } else {
                bitArray2.appendBits(i4, i);
                i3 += i;
            }
            i3--;
            i3 += i;
        }
        return bitArray2;
    }
}
