package ch.qos.logback.classic.pattern;

import java.io.PrintStream;

/* loaded from: classes.dex */
public class TargetLengthBasedClassNameAbbreviator implements Abbreviator {
    public final int targetLength;

    public TargetLengthBasedClassNameAbbreviator(int i) {
        this.targetLength = i;
    }

    @Override // ch.qos.logback.classic.pattern.Abbreviator
    public String abbreviate(String str) {
        StringBuilder sb = new StringBuilder(this.targetLength);
        if (str == null) {
            throw new IllegalArgumentException("Class name may not be null");
        }
        if (str.length() < this.targetLength) {
            return str;
        }
        int[] iArr = new int[16];
        int[] iArr2 = new int[17];
        int computeDotIndexes = computeDotIndexes(str, iArr);
        if (computeDotIndexes == 0) {
            return str;
        }
        computeLengthArray(str, iArr, iArr2, computeDotIndexes);
        for (int i = 0; i <= computeDotIndexes; i++) {
            if (i == 0) {
                sb.append(str.substring(0, iArr2[i] - 1));
            } else {
                int i2 = i - 1;
                sb.append(str.substring(iArr[i2], iArr[i2] + iArr2[i]));
            }
        }
        return sb.toString();
    }

    public static int computeDotIndexes(String str, int[] iArr) {
        int i = 0;
        int i2 = 0;
        while (true) {
            int indexOf = str.indexOf(46, i);
            if (indexOf == -1 || i2 >= 16) {
                break;
            }
            iArr[i2] = indexOf;
            i2++;
            i = indexOf + 1;
        }
        return i2;
    }

    public void computeLengthArray(String str, int[] iArr, int[] iArr2, int i) {
        int length = str.length() - this.targetLength;
        for (int i2 = 0; i2 < i; i2++) {
            int i3 = -1;
            if (i2 > 0) {
                i3 = iArr[i2 - 1];
            }
            int i4 = (iArr[i2] - i3) - 1;
            int i5 = (length <= 0 || i4 < 1) ? i4 : 1;
            length -= i4 - i5;
            iArr2[i2] = i5 + 1;
        }
        iArr2[i] = str.length() - iArr[i - 1];
    }

    public static void printArray(String str, int[] iArr) {
        System.out.print(str);
        for (int i = 0; i < iArr.length; i++) {
            if (i == 0) {
                System.out.print(iArr[i]);
            } else {
                PrintStream printStream = System.out;
                printStream.print(", " + iArr[i]);
            }
        }
        System.out.println();
    }
}
