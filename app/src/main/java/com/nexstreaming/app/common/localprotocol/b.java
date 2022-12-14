package com.nexstreaming.app.common.localprotocol;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.util.Log;
import com.nexstreaming.app.common.localprotocol.c;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;

/* compiled from: nexClient.java */
/* loaded from: classes3.dex */
public class b {
    public LocalSocket a;
    private String b;

    public b(String str) {
        this.b = str;
    }

    public boolean a() {
        LocalSocket localSocket = new LocalSocket();
        this.a = localSocket;
        try {
            localSocket.connect(new LocalSocketAddress(this.b));
            return true;
        } catch (IOException e) {
            Log.e("nexClient", "Exception " + e + " while connect Socket");
            return false;
        }
    }

    public boolean b() {
        LocalSocket localSocket;
        synchronized (this) {
            localSocket = this.a;
            this.a = null;
        }
        if (localSocket != null) {
            try {
                localSocket.shutdownInput();
                localSocket.shutdownOutput();
                localSocket.close();
                Log.d("nexClient", "Socket is closed");
                return true;
            } catch (IOException e) {
                Log.e("nexClient", "Exception " + e + " while closing Socket");
                return false;
            }
        }
        return false;
    }

    public void a(short s, int i, byte[] bArr) throws IOException {
        LocalSocket localSocket = this.a;
        if (localSocket == null) {
            Log.w("nexClient", "'send' request on closed Socket ");
            throw new ClosedChannelException();
        }
        localSocket.getOutputStream().write(c.a(s, i, bArr.length));
        localSocket.getOutputStream().write(bArr);
    }

    public c.a a(short s, int i, boolean z) throws IOException {
        LocalSocket localSocket = this.a;
        if (localSocket == null) {
            Log.w("nexClient", "'receive' request on closed Socket ");
            throw new ClosedChannelException();
        }
        ByteBuffer allocate = ByteBuffer.allocate(16);
        c.a(localSocket, allocate.array(), allocate.array().length);
        c.b a = c.a(allocate, i, z);
        if (!a.b) {
            Log.w("nexClient", "'receive' Header is invalid. error=" + a.f);
            throw new ClosedChannelException();
        } else if (s != a.d) {
            Log.w("nexClient", "'receive' command is invalid. ");
            throw new ClosedChannelException();
        } else {
            c.a aVar = new c.a(a);
            byte[] bArr = new byte[a.g];
            aVar.a = bArr;
            c.a(localSocket, bArr, bArr.length);
            return aVar;
        }
    }
}
