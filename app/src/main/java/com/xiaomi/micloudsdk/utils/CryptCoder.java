package com.xiaomi.micloudsdk.utils;

import com.xiaomi.micloudsdk.exception.CipherException;

/* loaded from: classes3.dex */
public interface CryptCoder {
    String decrypt(String str) throws CipherException;

    String encrypt(String str) throws CipherException;
}
