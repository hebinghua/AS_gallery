package com.adobe.xmp.options;

import com.adobe.xmp.XMPException;
import java.util.Map;

/* loaded from: classes.dex */
public abstract class Options {
    public int options = 0;
    public Map optionNames = null;

    public void assertConsistency(int i) throws XMPException {
    }

    public abstract int getValidOptions();

    public Options() {
    }

    public Options(int i) throws XMPException {
        assertOptionsValid(i);
        setOptions(i);
    }

    public boolean getOption(int i) {
        return (i & this.options) != 0;
    }

    public void setOption(int i, boolean z) {
        int i2;
        if (z) {
            i2 = i | this.options;
        } else {
            i2 = (~i) & this.options;
        }
        this.options = i2;
    }

    public int getOptions() {
        return this.options;
    }

    public void setOptions(int i) throws XMPException {
        assertOptionsValid(i);
        this.options = i;
    }

    public boolean equals(Object obj) {
        return getOptions() == ((Options) obj).getOptions();
    }

    public int hashCode() {
        return getOptions();
    }

    public String toString() {
        return "0x" + Integer.toHexString(this.options);
    }

    public final void assertOptionsValid(int i) throws XMPException {
        int i2 = (~getValidOptions()) & i;
        if (i2 == 0) {
            assertConsistency(i);
            return;
        }
        throw new XMPException("The option bit(s) 0x" + Integer.toHexString(i2) + " are invalid!", 103);
    }
}
