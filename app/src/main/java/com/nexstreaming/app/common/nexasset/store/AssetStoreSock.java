package com.nexstreaming.app.common.nexasset.store;

import com.nexstreaming.app.common.localprotocol.b;
import com.nexstreaming.app.common.localprotocol.c;
import java.io.IOException;

/* loaded from: classes3.dex */
public class AssetStoreSock {
    private static final String ASSET_STORE_ADDR = "com.nexstreaming.appstore";
    public static final int REQUEST_COMMUNICATION_KEY = 1;
    public static final int REQUEST_ENCAES_KEY = 3;
    public static final short REQUEST_END = 30;
    public static final short REQUEST_SEC = 20;
    public static final int REQUEST_SESSIONID = 2;
    public static final short REQUEST_START = 10;
    private static final String TAG = "AssetStoreSock";

    public static c.a Start(byte[] bArr, int i) {
        b bVar = new b(ASSET_STORE_ADDR);
        c.a aVar = null;
        if (bVar.a()) {
            try {
                bVar.a((short) 10, i, bArr);
                aVar = bVar.a((short) 10, i, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            bVar.b();
        }
        return aVar;
    }

    public static c.a requestSECUseCommKey(int i, String str) {
        b bVar = new b(ASSET_STORE_ADDR);
        c.a aVar = null;
        if (bVar.a()) {
            try {
                bVar.a((short) 20, i, str.getBytes());
                aVar = bVar.a((short) 20, i, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            bVar.b();
        }
        return aVar;
    }

    public static int End(int i) {
        b bVar = new b(ASSET_STORE_ADDR);
        c.a aVar = null;
        if (bVar.a()) {
            try {
                bVar.a((short) 30, i, "bye".getBytes());
                aVar = bVar.a((short) 30, i, false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            bVar.b();
        }
        if (aVar == null) {
            return -1;
        }
        return aVar.f;
    }
}
