package com.miui.gallery.editor.photo.utils.svgparser;

/* loaded from: classes2.dex */
public class IntegerParser {
    public int pos;
    public long value;

    public IntegerParser(long j, int i) {
        this.value = j;
        this.pos = i;
    }

    public int getEndPos() {
        return this.pos;
    }

    public int value() {
        return (int) this.value;
    }

    public static IntegerParser parseHex(String str, int i) {
        long j;
        int i2;
        if (1 >= i) {
            return null;
        }
        long j2 = 0;
        int i3 = 1;
        while (i3 < i) {
            char charAt = str.charAt(i3);
            if (charAt < '0' || charAt > '9') {
                if (charAt >= 'A' && charAt <= 'F') {
                    j = j2 * 16;
                    i2 = charAt - 'A';
                } else if (charAt < 'a' || charAt > 'f') {
                    break;
                } else {
                    j = j2 * 16;
                    i2 = charAt - 'a';
                }
                j2 = j + i2 + 10;
            } else {
                j2 = (j2 * 16) + (charAt - '0');
            }
            if (j2 > 4294967295L) {
                return null;
            }
            i3++;
        }
        if (i3 != 1) {
            return new IntegerParser(j2, i3);
        }
        return null;
    }
}
