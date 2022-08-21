package org.keyczar.enums;

import com.miui.gallery.assistant.jni.filter.BaiduSceneResult;

/* loaded from: classes3.dex */
public enum KeyPurpose {
    DECRYPT_AND_ENCRYPT(0, "crypt"),
    ENCRYPT(1, "encrypt"),
    SIGN_AND_VERIFY(2, "sign"),
    VERIFY(3, "verify"),
    TEST(BaiduSceneResult.BANK_CARD, "test");
    
    private String name;
    private int value;

    KeyPurpose(int i, String str) {
        this.value = i;
        this.name = str;
    }

    public int getValue() {
        return this.value;
    }

    public String getName() {
        return this.name;
    }

    public static KeyPurpose getPurpose(int i) {
        if (i != 0) {
            if (i == 1) {
                return ENCRYPT;
            }
            if (i == 2) {
                return SIGN_AND_VERIFY;
            }
            if (i == 3) {
                return VERIFY;
            }
            if (i == 127) {
                return TEST;
            }
            return null;
        }
        return DECRYPT_AND_ENCRYPT;
    }

    public static KeyPurpose getPurpose(String str) {
        if (str != null) {
            KeyPurpose keyPurpose = DECRYPT_AND_ENCRYPT;
            if (str.equalsIgnoreCase(keyPurpose.getName())) {
                return keyPurpose;
            }
            KeyPurpose keyPurpose2 = ENCRYPT;
            if (str.equalsIgnoreCase(keyPurpose2.getName())) {
                return keyPurpose2;
            }
            KeyPurpose keyPurpose3 = SIGN_AND_VERIFY;
            if (str.equalsIgnoreCase(keyPurpose3.getName())) {
                return keyPurpose3;
            }
            KeyPurpose keyPurpose4 = VERIFY;
            if (str.equalsIgnoreCase(keyPurpose4.getName())) {
                return keyPurpose4;
            }
            KeyPurpose keyPurpose5 = TEST;
            if (!str.equalsIgnoreCase(keyPurpose5.getName())) {
                return null;
            }
            return keyPurpose5;
        }
        return null;
    }
}
