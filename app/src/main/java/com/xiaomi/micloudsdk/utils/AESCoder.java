package com.xiaomi.micloudsdk.utils;

import android.util.Base64;
import com.xiaomi.micloudsdk.exception.CipherException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.keyczar.Keyczar;

/* loaded from: classes3.dex */
public class AESCoder implements CryptCoder {
    public SecretKeySpec keySpec;

    public AESCoder(byte[] bArr) {
        if (bArr == null) {
            throw new SecurityException("aes key is null");
        }
        if (bArr.length != 16) {
            MiCloudLog.e("AESCoder", "aesKey is invalid");
        }
        this.keySpec = new SecretKeySpec(bArr, "AES");
    }

    public AESCoder(String str) {
        this(str == null ? null : Base64.decode(str, 2));
    }

    @Override // com.xiaomi.micloudsdk.utils.CryptCoder
    public String decrypt(String str) throws CipherException {
        if (str == null) {
            MiCloudLog.e("AESCoder", "decrypt failed for empty data");
            return null;
        }
        try {
            return new String(decrypt(Base64.decode(str, 2)), Keyczar.DEFAULT_ENCODING);
        } catch (Exception e) {
            throw new CipherException("fail to decrypt by aescoder", e);
        }
    }

    public byte[] decrypt(byte[] bArr) throws CipherException {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(2, this.keySpec, new IvParameterSpec(getInitalVector()));
            if (bArr == null) {
                throw new IllegalBlockSizeException("no block data");
            }
            return cipher.doFinal(bArr);
        } catch (Exception e) {
            throw new CipherException("fail to decrypt by aescoder", e);
        }
    }

    public byte[] getInitalVector() {
        return "0102030405060708".getBytes();
    }

    @Override // com.xiaomi.micloudsdk.utils.CryptCoder
    public String encrypt(String str) throws CipherException {
        try {
            return Base64.encodeToString(encrypt(str.getBytes(Keyczar.DEFAULT_ENCODING)), 2);
        } catch (Exception e) {
            throw new CipherException("fail to encrypt by aescoder", e);
        }
    }

    public byte[] encrypt(byte[] bArr) throws CipherException {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(1, this.keySpec, new IvParameterSpec(getInitalVector()));
            return cipher.doFinal(bArr);
        } catch (Exception e) {
            throw new CipherException("fail to encrypt by aescoder", e);
        }
    }
}
