package ch.qos.logback.core.pattern;

import java.util.Objects;

/* loaded from: classes.dex */
public class FormatInfo {
    private boolean leftPad;
    private boolean leftTruncate;
    private int max;
    private int min;

    public FormatInfo() {
        this.min = Integer.MIN_VALUE;
        this.max = Integer.MAX_VALUE;
        this.leftPad = true;
        this.leftTruncate = true;
    }

    public FormatInfo(int i, int i2) {
        this.min = Integer.MIN_VALUE;
        this.max = Integer.MAX_VALUE;
        this.leftPad = true;
        this.leftTruncate = true;
        this.min = i;
        this.max = i2;
    }

    public FormatInfo(int i, int i2, boolean z, boolean z2) {
        this.min = Integer.MIN_VALUE;
        this.max = Integer.MAX_VALUE;
        this.leftPad = true;
        this.leftTruncate = true;
        this.min = i;
        this.max = i2;
        this.leftPad = z;
        this.leftTruncate = z2;
    }

    public static FormatInfo valueOf(String str) throws IllegalArgumentException {
        Objects.requireNonNull(str, "Argument cannot be null");
        FormatInfo formatInfo = new FormatInfo();
        int indexOf = str.indexOf(46);
        String str2 = null;
        if (indexOf != -1) {
            String substring = str.substring(0, indexOf);
            int i = indexOf + 1;
            if (i == str.length()) {
                throw new IllegalArgumentException("Formatting string [" + str + "] should not end with '.'");
            }
            str2 = str.substring(i);
            str = substring;
        }
        if (str != null && str.length() > 0) {
            int parseInt = Integer.parseInt(str);
            if (parseInt >= 0) {
                formatInfo.min = parseInt;
            } else {
                formatInfo.min = -parseInt;
                formatInfo.leftPad = false;
            }
        }
        if (str2 != null && str2.length() > 0) {
            int parseInt2 = Integer.parseInt(str2);
            if (parseInt2 >= 0) {
                formatInfo.max = parseInt2;
            } else {
                formatInfo.max = -parseInt2;
                formatInfo.leftTruncate = false;
            }
        }
        return formatInfo;
    }

    public boolean isLeftPad() {
        return this.leftPad;
    }

    public void setLeftPad(boolean z) {
        this.leftPad = z;
    }

    public int getMax() {
        return this.max;
    }

    public void setMax(int i) {
        this.max = i;
    }

    public int getMin() {
        return this.min;
    }

    public void setMin(int i) {
        this.min = i;
    }

    public boolean isLeftTruncate() {
        return this.leftTruncate;
    }

    public void setLeftTruncate(boolean z) {
        this.leftTruncate = z;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof FormatInfo)) {
            return false;
        }
        FormatInfo formatInfo = (FormatInfo) obj;
        return this.min == formatInfo.min && this.max == formatInfo.max && this.leftPad == formatInfo.leftPad && this.leftTruncate == formatInfo.leftTruncate;
    }

    public int hashCode() {
        return (((((this.min * 31) + this.max) * 31) + (this.leftPad ? 1 : 0)) * 31) + (this.leftTruncate ? 1 : 0);
    }

    public String toString() {
        return "FormatInfo(" + this.min + ", " + this.max + ", " + this.leftPad + ", " + this.leftTruncate + ")";
    }
}
