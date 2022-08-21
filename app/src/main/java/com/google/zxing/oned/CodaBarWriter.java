package com.google.zxing.oned;

import ch.qos.logback.core.CoreConstants;
import java.util.Arrays;

/* loaded from: classes.dex */
public final class CodaBarWriter extends OneDimensionalCodeWriter {
    public static final char[] START_END_CHARS = {'A', 'B', 'C', 'D'};
    public static final char[] ALT_START_END_CHARS = {'T', 'N', '*', 'E'};
    public static final char[] CHARS_WHICH_ARE_TEN_LENGTH_EACH_AFTER_DECODED = {'/', CoreConstants.COLON_CHAR, '+', CoreConstants.DOT};

    @Override // com.google.zxing.oned.OneDimensionalCodeWriter
    public boolean[] encode(String str) {
        int i;
        if (str.length() < 2) {
            throw new IllegalArgumentException("Codabar should start/end with start/stop symbols");
        }
        char upperCase = Character.toUpperCase(str.charAt(0));
        char upperCase2 = Character.toUpperCase(str.charAt(str.length() - 1));
        char[] cArr = START_END_CHARS;
        boolean z = CodaBarReader.arrayContains(cArr, upperCase) && CodaBarReader.arrayContains(cArr, upperCase2);
        char[] cArr2 = ALT_START_END_CHARS;
        boolean z2 = CodaBarReader.arrayContains(cArr2, upperCase) && CodaBarReader.arrayContains(cArr2, upperCase2);
        if (!z && !z2) {
            throw new IllegalArgumentException("Codabar should start/end with " + Arrays.toString(cArr) + ", or start/end with " + Arrays.toString(cArr2));
        }
        int i2 = 20;
        for (int i3 = 1; i3 < str.length() - 1; i3++) {
            if (Character.isDigit(str.charAt(i3)) || str.charAt(i3) == '-' || str.charAt(i3) == '$') {
                i2 += 9;
            } else if (!CodaBarReader.arrayContains(CHARS_WHICH_ARE_TEN_LENGTH_EACH_AFTER_DECODED, str.charAt(i3))) {
                throw new IllegalArgumentException("Cannot encode : '" + str.charAt(i3) + CoreConstants.SINGLE_QUOTE_CHAR);
            } else {
                i2 += 10;
            }
        }
        boolean[] zArr = new boolean[i2 + (str.length() - 1)];
        int i4 = 0;
        for (int i5 = 0; i5 < str.length(); i5++) {
            char upperCase3 = Character.toUpperCase(str.charAt(i5));
            if (i5 == 0 || i5 == str.length() - 1) {
                if (upperCase3 == '*') {
                    upperCase3 = 'C';
                } else if (upperCase3 == 'E') {
                    upperCase3 = 'D';
                } else if (upperCase3 == 'N') {
                    upperCase3 = 'B';
                } else if (upperCase3 == 'T') {
                    upperCase3 = 'A';
                }
            }
            char c = upperCase3;
            int i6 = 0;
            while (true) {
                char[] cArr3 = CodaBarReader.ALPHABET;
                if (i6 >= cArr3.length) {
                    i = 0;
                    break;
                } else if (c == cArr3[i6]) {
                    i = CodaBarReader.CHARACTER_ENCODINGS[i6];
                    break;
                } else {
                    i6++;
                }
            }
            int i7 = 0;
            int i8 = 0;
            boolean z3 = true;
            while (i7 < 7) {
                zArr[i4] = z3;
                i4++;
                if (((i >> (6 - i7)) & 1) == 0 || i8 == 1) {
                    z3 = !z3;
                    i7++;
                    i8 = 0;
                } else {
                    i8++;
                }
            }
            if (i5 < str.length() - 1) {
                zArr[i4] = false;
                i4++;
            }
        }
        return zArr;
    }
}
