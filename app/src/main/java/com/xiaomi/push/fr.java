package com.xiaomi.push;

import java.nio.ByteBuffer;

/* loaded from: classes3.dex */
public final class fr extends fl {
    public fr() {
        a("PING", (String) null);
        a("0");
        a(0);
    }

    @Override // com.xiaomi.push.fl
    /* renamed from: a */
    public ByteBuffer mo2159a(ByteBuffer byteBuffer) {
        return m2162a().length == 0 ? byteBuffer : super.mo2159a(byteBuffer);
    }

    @Override // com.xiaomi.push.fl
    public int c() {
        if (m2162a().length == 0) {
            return 0;
        }
        return super.c();
    }
}
