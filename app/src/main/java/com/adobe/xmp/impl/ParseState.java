package com.adobe.xmp.impl;

import com.adobe.xmp.XMPException;

/* compiled from: ISO8601Converter.java */
/* loaded from: classes.dex */
public class ParseState {
    public int pos = 0;
    public String str;

    public ParseState(String str) {
        this.str = str;
    }

    public int length() {
        return this.str.length();
    }

    public boolean hasNext() {
        return this.pos < this.str.length();
    }

    public char ch(int i) {
        if (i < this.str.length()) {
            return this.str.charAt(i);
        }
        return (char) 0;
    }

    public char ch() {
        if (this.pos < this.str.length()) {
            return this.str.charAt(this.pos);
        }
        return (char) 0;
    }

    public void skip() {
        this.pos++;
    }

    public int pos() {
        return this.pos;
    }

    public int gatherInt(String str, int i) throws XMPException {
        char ch2 = ch(this.pos);
        int i2 = 0;
        boolean z = false;
        while ('0' <= ch2 && ch2 <= '9') {
            i2 = (i2 * 10) + (ch2 - '0');
            int i3 = this.pos + 1;
            this.pos = i3;
            ch2 = ch(i3);
            z = true;
        }
        if (z) {
            if (i2 > i) {
                return i;
            }
            if (i2 >= 0) {
                return i2;
            }
            return 0;
        }
        throw new XMPException(str, 5);
    }
}
