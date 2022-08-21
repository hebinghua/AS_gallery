package com.xiaomi.micloudsdk.utils;

import android.util.Base64;
import com.xiaomi.micloudsdk.exception.CipherException;
import com.xiaomi.micloudsdk.utils.AESStringDef;
import java.security.SecureRandom;

/* loaded from: classes3.dex */
public class AESWithIVCoder implements CryptCoder {
    public String aesKey;
    public AESCoder coder;
    public byte[] ivRaw;
    public final int IV_LENGTH = 16;
    public long threadId = 0;

    public AESWithIVCoder(String str) {
        this.aesKey = str;
        this.coder = new AESCoder(str) { // from class: com.xiaomi.micloudsdk.utils.AESWithIVCoder.1
            @Override // com.xiaomi.micloudsdk.utils.AESCoder
            public byte[] getInitalVector() {
                return AESWithIVCoder.this.ivRaw;
            }
        };
        initThreadInfo();
    }

    public final void initThreadInfo() {
        this.threadId = Thread.currentThread().getId();
    }

    @Override // com.xiaomi.micloudsdk.utils.CryptCoder
    public String decrypt(String str) throws CipherException {
        checkThreadIdThrow();
        try {
            AESStringDef aESStringDef = AESStringDef.getInstance(str);
            if (!aESStringDef.getVersion().equals("1")) {
                throw new CipherException("aes encrypt format version is wrong" + str);
            }
            this.ivRaw = Base64.decode(aESStringDef.getIV(), 11);
            return this.coder.decrypt(aESStringDef.getData());
        } catch (AESStringDef.InvalidAESDataException e) {
            throw new CipherException(e);
        }
    }

    @Override // com.xiaomi.micloudsdk.utils.CryptCoder
    public String encrypt(String str) throws CipherException {
        checkThreadIdThrow();
        try {
            this.ivRaw = new byte[16];
            new SecureRandom().nextBytes(this.ivRaw);
            return AESStringDef.getInstance("1", Base64.encodeToString(this.ivRaw, 11), this.coder.encrypt(str)).toString();
        } catch (AESStringDef.InvalidAESDataException e) {
            throw new CipherException(e);
        }
    }

    public void checkThreadIdThrow() {
        if (Thread.currentThread().getId() == this.threadId) {
            return;
        }
        throw new IllegalAccessError("this method can not call concurrently");
    }
}
