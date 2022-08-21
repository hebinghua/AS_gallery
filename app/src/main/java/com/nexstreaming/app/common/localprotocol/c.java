package com.nexstreaming.app.common.localprotocol;

import android.net.LocalSocket;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/* compiled from: nexProtocol.java */
/* loaded from: classes3.dex */
public class c {

    /* compiled from: nexProtocol.java */
    /* loaded from: classes3.dex */
    public static class b {
        public boolean b;
        public boolean c;
        public int d;
        public int e;
        public int f;
        public int g;
    }

    /* compiled from: nexProtocol.java */
    /* loaded from: classes3.dex */
    public static class a extends b {
        public byte[] a;

        public a(b bVar) {
            this.b = bVar.b;
            this.c = bVar.c;
            this.d = bVar.d;
            this.e = bVar.e;
            this.f = bVar.f;
            this.g = bVar.g;
        }
    }

    public static byte[] a(short s, int i, int i2) {
        ByteBuffer allocate = ByteBuffer.allocate(16);
        allocate.putInt(1315272784);
        allocate.putChar('Q');
        allocate.putShort(s);
        allocate.putInt(i);
        allocate.putInt(i2);
        return allocate.array();
    }

    public static void a(LocalSocket localSocket, byte[] bArr, int i) throws IOException {
        InputStream inputStream = localSocket.getInputStream();
        int i2 = 0;
        while (i2 != i) {
            int read = inputStream.read(bArr, i2, i - i2);
            if (read < 0) {
                throw new IOException("I/O failure while receiving SDK controller data from socket.");
            }
            i2 += read;
        }
    }

    public static b a(ByteBuffer byteBuffer, int i, boolean z) {
        b bVar = new b();
        if (byteBuffer.getInt() != 1315272784) {
            bVar.f = nexProtoErrorCode.InvalidHDR.getValue();
            return bVar;
        }
        char c = byteBuffer.getChar();
        if (c == 'Q') {
            bVar.c = true;
            if (!z) {
                bVar.f = nexProtoErrorCode.InvalidRQ.getValue();
                return bVar;
            }
        } else if (c == 'S') {
            bVar.c = false;
            if (z) {
                bVar.f = nexProtoErrorCode.InvalidRS.getValue();
                return bVar;
            }
        }
        bVar.d = byteBuffer.getShort();
        if (z) {
            int i2 = byteBuffer.getInt();
            bVar.e = i2;
            if (z && i2 != i) {
                bVar.f = nexProtoErrorCode.InvalidSSID.getValue();
                return bVar;
            }
        } else {
            bVar.f = byteBuffer.getInt();
        }
        bVar.b = true;
        bVar.g = byteBuffer.getInt();
        return bVar;
    }
}
