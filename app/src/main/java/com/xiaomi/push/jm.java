package com.xiaomi.push;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/* loaded from: classes3.dex */
public class jm extends jp {
    public InputStream a = null;

    /* renamed from: a  reason: collision with other field name */
    public OutputStream f797a;

    public jm(OutputStream outputStream) {
        this.f797a = null;
        this.f797a = outputStream;
    }

    @Override // com.xiaomi.push.jp
    /* renamed from: a */
    public int mo2394a(byte[] bArr, int i, int i2) {
        InputStream inputStream = this.a;
        if (inputStream != null) {
            try {
                int read = inputStream.read(bArr, i, i2);
                if (read < 0) {
                    throw new jq(4);
                }
                return read;
            } catch (IOException e) {
                throw new jq(0, e);
            }
        }
        throw new jq(1, "Cannot read from null inputStream");
    }

    @Override // com.xiaomi.push.jp
    /* renamed from: a  reason: collision with other method in class */
    public void mo2394a(byte[] bArr, int i, int i2) {
        OutputStream outputStream = this.f797a;
        if (outputStream != null) {
            try {
                outputStream.write(bArr, i, i2);
                return;
            } catch (IOException e) {
                throw new jq(0, e);
            }
        }
        throw new jq(1, "Cannot write to null outputStream");
    }
}
