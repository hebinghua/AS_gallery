package com.nexstreaming.app.common.nexasset.store;

import android.util.Log;
import com.nexstreaming.app.common.localprotocol.a;
import com.nexstreaming.app.common.localprotocol.c;
import com.nexstreaming.app.common.localprotocol.nexProtoErrorCode;
import java.io.File;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/* loaded from: classes3.dex */
public class AssetStoreClient {
    private static final String TAG = "AssetStoreClient";
    private static a cipher = null;
    private static Object m_configLock = new Object();
    public static final String none = "nonedata";

    public static void makeConfig() {
        Log.d(TAG, "makeConfig in");
        synchronized (m_configLock) {
            if (cipher == null) {
                a aVar = new a();
                cipher = aVar;
                aVar.a();
            }
        }
        Log.d(TAG, "makeConfig out");
    }

    public static int getDataSync2(String[] strArr, String[] strArr2, String str) {
        if (cipher == null) {
            return -1;
        }
        byte[] bArr = null;
        int nextInt = new SecureRandom().nextInt();
        c.a Start = AssetStoreSock.Start(cipher.b(), nextInt);
        if (Start == null) {
            Log.d(TAG, "start is null");
            return -1;
        } else if (Start.f != nexProtoErrorCode.OK.getValue()) {
            Log.d(TAG, "start is error=" + Start.f);
            return -1;
        } else {
            ByteBuffer wrap = ByteBuffer.wrap(Start.a);
            int i = nextInt ^ wrap.getInt();
            Log.d(TAG, "newSSID=" + i);
            int length = Start.a.length + (-4);
            byte[] bArr2 = new byte[length];
            wrap.get(bArr2);
            Log.d(TAG, "encCommkey=" + length);
            try {
                bArr = cipher.b(bArr2);
            } catch (InvalidKeyException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e2) {
                e2.printStackTrace();
            } catch (NoSuchProviderException e3) {
                e3.printStackTrace();
            } catch (BadPaddingException e4) {
                e4.printStackTrace();
            } catch (IllegalBlockSizeException e5) {
                e5.printStackTrace();
            } catch (NoSuchPaddingException e6) {
                e6.printStackTrace();
            }
            for (int i2 = 0; i2 < strArr.length; i2++) {
                c.a requestSECUseCommKey = AssetStoreSock.requestSECUseCommKey(i, str + File.separator + strArr[i2]);
                if (requestSECUseCommKey == null) {
                    strArr2[i2] = none;
                } else if (requestSECUseCommKey.f != nexProtoErrorCode.OK.getValue()) {
                    Log.d(TAG, "enckey is fail!");
                    return -1;
                } else {
                    try {
                        strArr2[i2] = new String(a.a(requestSECUseCommKey.a, bArr));
                    } catch (Exception e7) {
                        e7.printStackTrace();
                    }
                }
            }
            return 0;
        }
    }
}
