package com.nexstreaming.app.common.nexasset.assetpackage.security.provider;

import com.google.gson_nex.Gson;
import com.nexstreaming.app.common.nexasset.assetpackage.security.b;
import com.nexstreaming.app.common.util.m;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import org.keyczar.Crypter;
import org.keyczar.exceptions.KeyczarException;
import org.keyczar.interfaces.KeyczarReader;

/* loaded from: classes3.dex */
public class BasicEncryptionProvider implements b {
    private final int[] a;
    private final String[] b;
    private KeyczarReader c = new KeyczarReader() { // from class: com.nexstreaming.app.common.nexasset.assetpackage.security.provider.BasicEncryptionProvider.1
        @Override // org.keyczar.interfaces.KeyczarReader
        public String getKey(int i) throws KeyczarException {
            return BasicEncryptionProvider.this.a(i);
        }

        @Override // org.keyczar.interfaces.KeyczarReader
        public String getKey() throws KeyczarException {
            return BasicEncryptionProvider.this.a(1);
        }

        @Override // org.keyczar.interfaces.KeyczarReader
        public String getMetadata() throws KeyczarException {
            return BasicEncryptionProvider.this.a(0);
        }
    };

    public BasicEncryptionProvider(int[] iArr, String[] strArr) {
        this.a = iArr;
        this.b = strArr;
    }

    private int a(String str, int i, int i2) {
        int i3;
        int i4;
        int length = str.length();
        int i5 = 0;
        while (i < i2 && i < length) {
            char charAt = str.charAt(i);
            i5 *= 16;
            if (charAt < '0' || charAt > '9') {
                if (charAt < 'a' || charAt > 'f') {
                    if (charAt >= 'A' && charAt <= 'F') {
                        i3 = charAt - 'A';
                    }
                    i++;
                } else {
                    i3 = charAt - 'a';
                }
                i4 = i3 + 10;
            } else {
                i4 = charAt - '0';
            }
            i5 += i4;
            i++;
        }
        return i5;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String a(int i) {
        String str = this.b[i];
        int length = str.length();
        int a = a(str, 0, 2) ^ 32;
        int i2 = (length - 2) / 2;
        byte[] bArr = new byte[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            int i4 = i3 * 2;
            byte a2 = (byte) a(str, i4 + 2, i4 + 4);
            bArr[i3] = (byte) ((((a2 << 4) & 240) | ((a2 >> 4) & 15)) ^ a);
        }
        return new String(bArr);
    }

    private String a(String str, String str2) {
        String substring = str.substring(str.indexOf(str2));
        String substring2 = substring.substring(substring.indexOf(":"));
        String substring3 = substring2.substring(substring2.indexOf("\"") + 1);
        return substring3.substring(0, substring3.indexOf("\""));
    }

    private String b(String str) {
        byte[] bytes = str.getBytes(StandardCharsets.US_ASCII);
        int nextInt = new Random().nextInt(238) + 17;
        String format = String.format("%02X", Integer.valueOf(nextInt ^ 32));
        for (byte b : bytes) {
            byte b2 = (byte) (b ^ nextInt);
            format = format + String.format("%02X", Byte.valueOf((byte) (((b2 << 4) & 240) | ((b2 >> 4) & 15))));
        }
        return format;
    }

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.security.b
    public String[] b() {
        String[] strArr = new String[12];
        int i = 0;
        int i2 = 0;
        while (i < 6) {
            try {
                i++;
                String key = this.c.getKey(i);
                String a = a(key, "hmacKeyString");
                if (a.length() > 16) {
                    a = a.substring(a.length() - 16, a.length());
                }
                int i3 = i2 + 1;
                strArr[i2] = b(a);
                String a2 = a(key, "aesKeyString");
                if (a2.length() > 16) {
                    a2 = a2.substring(a2.length() - 16, a2.length());
                }
                strArr[i3] = b(a2);
                i2 = i3 + 1;
            } catch (KeyczarException unused) {
            }
        }
        return strArr;
    }

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.security.b
    public String a() {
        char[] cArr = new char[this.a.length];
        int i = 0;
        while (true) {
            int[] iArr = this.a;
            if (i < iArr.length) {
                cArr[i] = (char) (iArr[i] ^ 90);
                i++;
            } else {
                return new String(cArr);
            }
        }
    }

    /* loaded from: classes3.dex */
    public class ProviderSpecificDataJSON {
        public List<String> f;
        public int v;

        private ProviderSpecificDataJSON() {
        }
    }

    @Override // com.nexstreaming.app.common.nexasset.assetpackage.security.b
    public com.nexstreaming.app.common.nexasset.assetpackage.security.a a(String str) {
        final HashSet hashSet = new HashSet();
        hashSet.addAll(((ProviderSpecificDataJSON) new Gson().fromJson(str, (Class<Object>) ProviderSpecificDataJSON.class)).f);
        try {
            final Crypter crypter = new Crypter(this.c);
            return new com.nexstreaming.app.common.nexasset.assetpackage.security.a() { // from class: com.nexstreaming.app.common.nexasset.assetpackage.security.provider.BasicEncryptionProvider.2
                public ByteArrayOutputStream a = new ByteArrayOutputStream();

                @Override // com.nexstreaming.app.common.nexasset.assetpackage.security.a
                public InputStream a(InputStream inputStream, String str2) throws IOException {
                    ByteArrayInputStream byteArrayInputStream;
                    if (hashSet.contains(str2)) {
                        this.a.reset();
                        m.a(inputStream, this.a);
                        inputStream.close();
                        try {
                            synchronized (this) {
                                byteArrayInputStream = new ByteArrayInputStream(crypter.decrypt(this.a.toByteArray()));
                            }
                            return byteArrayInputStream;
                        } catch (KeyczarException unused) {
                            throw new IllegalStateException();
                        }
                    }
                    return inputStream;
                }

                @Override // com.nexstreaming.app.common.nexasset.assetpackage.security.a
                public boolean a(String str2) {
                    return !hashSet.contains(str2);
                }
            };
        } catch (KeyczarException unused) {
            throw new IllegalStateException();
        }
    }
}
