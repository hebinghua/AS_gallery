package com.adobe.xmp.impl;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

/* loaded from: classes.dex */
public class FixASCIIControlsReader extends PushbackReader {
    public int control;
    public int digits;
    public int state;

    public FixASCIIControlsReader(Reader reader) {
        super(reader, 8);
        this.state = 0;
        this.control = 0;
        this.digits = 0;
    }

    @Override // java.io.PushbackReader, java.io.FilterReader, java.io.Reader
    public int read(char[] cArr, int i, int i2) throws IOException {
        boolean z;
        char[] cArr2 = new char[8];
        int i3 = 0;
        int i4 = 0;
        loop0: while (true) {
            z = true;
            while (z && i3 < i2) {
                z = super.read(cArr2, i4, 1) == 1;
                if (z) {
                    char processChar = processChar(cArr2[i4]);
                    int i5 = this.state;
                    if (i5 == 0) {
                        if (Utils.isControlChar(processChar)) {
                            processChar = ' ';
                        }
                        cArr[i] = processChar;
                        i3++;
                        i++;
                    } else if (i5 == 5) {
                        unread(cArr2, 0, i4 + 1);
                    } else {
                        i4++;
                    }
                    i4 = 0;
                } else if (i4 > 0) {
                    break;
                }
            }
            unread(cArr2, 0, i4);
            this.state = 5;
            i4 = 0;
        }
        if (i3 > 0 || z) {
            return i3;
        }
        return -1;
    }

    public final char processChar(char c) {
        int i;
        int i2 = this.state;
        if (i2 == 0) {
            if (c == '&') {
                this.state = 1;
            }
            return c;
        } else if (i2 == 1) {
            if (c == '#') {
                this.state = 2;
            } else {
                this.state = 5;
            }
            return c;
        } else if (i2 == 2) {
            if (c == 'x') {
                this.control = 0;
                this.digits = 0;
                this.state = 3;
            } else if ('0' <= c && c <= '9') {
                this.control = Character.digit(c, 10);
                this.digits = 1;
                this.state = 4;
            } else {
                this.state = 5;
            }
            return c;
        } else {
            if (i2 == 3) {
                if (('0' <= c && c <= '9') || (('a' <= c && c <= 'f') || ('A' <= c && c <= 'F'))) {
                    this.control = (this.control * 16) + Character.digit(c, 16);
                    int i3 = this.digits + 1;
                    this.digits = i3;
                    if (i3 <= 4) {
                        this.state = 3;
                    } else {
                        this.state = 5;
                    }
                } else if (c == ';' && Utils.isControlChar((char) this.control)) {
                    this.state = 0;
                    i = this.control;
                } else {
                    this.state = 5;
                }
                return c;
            } else if (i2 != 4) {
                if (i2 != 5) {
                    return c;
                }
                this.state = 0;
                return c;
            } else {
                if ('0' <= c && c <= '9') {
                    this.control = (this.control * 10) + Character.digit(c, 10);
                    int i4 = this.digits + 1;
                    this.digits = i4;
                    if (i4 <= 5) {
                        this.state = 4;
                    } else {
                        this.state = 5;
                    }
                } else if (c == ';' && Utils.isControlChar((char) this.control)) {
                    this.state = 0;
                    i = this.control;
                } else {
                    this.state = 5;
                }
                return c;
            }
            return (char) i;
        }
    }
}
