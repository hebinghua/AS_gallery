package com.xiaomi.push;

import java.io.ByteArrayOutputStream;

/* loaded from: classes3.dex */
public class ix extends ByteArrayOutputStream {
    public ix(int i) {
        super(i);
    }

    public int a() {
        return ((ByteArrayOutputStream) this).count;
    }

    /* renamed from: a  reason: collision with other method in class */
    public byte[] m2361a() {
        return ((ByteArrayOutputStream) this).buf;
    }
}
