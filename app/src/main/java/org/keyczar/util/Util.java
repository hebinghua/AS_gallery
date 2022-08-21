package org.keyczar.util;

import com.google.gson_nex.Gson;
import com.google.gson_nex.GsonBuilder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.keyczar.exceptions.Base64DecodingException;
import org.keyczar.exceptions.KeyczarException;
import org.keyczar.interfaces.KeyType;

/* loaded from: classes3.dex */
public class Util {
    private static final int READ_BUF_SIZE = 8192;
    private static final ConcurrentLinkedQueue<MessageDigest> DIGEST_QUEUE = new ConcurrentLinkedQueue<>();
    private static final ConcurrentLinkedQueue<SecureRandom> RAND_QUEUE = new ConcurrentLinkedQueue<>();
    private static final ThreadLocal<Gson> GSON_THREAD_LOCAL = new ThreadLocal<Gson>() { // from class: org.keyczar.util.Util.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.lang.ThreadLocal
        /* renamed from: initialValue */
        public Gson mo2645initialValue() {
            return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().registerTypeAdapter(KeyType.class, new KeyType.KeyTypeSerializer()).registerTypeAdapter(KeyType.class, new KeyType.KeyTypeDeserializer()).create();
        }
    };

    private Util() {
    }

    public static Gson gson() {
        return GSON_THREAD_LOCAL.get();
    }

    public static byte[] stripLeadingZeros(byte[] bArr) {
        int i = 0;
        while (i < bArr.length && bArr[i] == 0) {
            i++;
        }
        if (i == 0) {
            return bArr;
        }
        int length = bArr.length - i;
        byte[] bArr2 = new byte[length];
        System.arraycopy(bArr, i, bArr2, 0, length);
        return bArr2;
    }

    public static byte[] fromInt(int i) {
        byte[] bArr = new byte[4];
        writeInt(i, bArr, 0);
        return bArr;
    }

    public static byte[] fromLong(long j) {
        byte[] bArr = new byte[8];
        writeLong(j, bArr, 0);
        return bArr;
    }

    public static byte[] prefixHash(byte[]... bArr) throws KeyczarException {
        MessageDigest poll = DIGEST_QUEUE.poll();
        if (poll == null) {
            try {
                poll = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                throw new KeyczarException(e);
            }
        }
        for (byte[] bArr2 : bArr) {
            poll.update(fromInt(bArr2.length));
            poll.update(bArr2);
        }
        byte[] digest = poll.digest();
        DIGEST_QUEUE.add(poll);
        return digest;
    }

    public static byte[] lenPrefix(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return fromInt(0);
        }
        return ByteBuffer.allocate(bArr.length + 4).putInt(bArr.length).put(bArr).array();
    }

    public static byte[] lenPrefixPack(byte[]... bArr) {
        int length = (bArr.length + 1) * 4;
        for (byte[] bArr2 : bArr) {
            length += bArr2.length;
        }
        byte[] bArr3 = new byte[length];
        ByteBuffer wrap = ByteBuffer.wrap(bArr3);
        wrap.putInt(bArr.length);
        for (byte[] bArr4 : bArr) {
            wrap.putInt(bArr4.length);
            wrap.put(bArr4);
        }
        return bArr3;
    }

    public static byte[][] lenPrefixUnpack(byte[] bArr) {
        ByteBuffer wrap = ByteBuffer.wrap(bArr);
        int i = wrap.getInt();
        byte[][] bArr2 = new byte[i];
        for (int i2 = 0; i2 < i; i2++) {
            byte[] bArr3 = new byte[wrap.getInt()];
            wrap.get(bArr3);
            bArr2[i2] = bArr3;
        }
        return bArr2;
    }

    public static byte[] hash(byte[]... bArr) throws KeyczarException {
        MessageDigest poll = DIGEST_QUEUE.poll();
        if (poll == null) {
            try {
                poll = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                throw new KeyczarException(e);
            }
        }
        for (byte[] bArr2 : bArr) {
            poll.update(bArr2);
        }
        byte[] digest = poll.digest();
        DIGEST_QUEUE.add(poll);
        return digest;
    }

    public static void rand(byte[] bArr) {
        ConcurrentLinkedQueue<SecureRandom> concurrentLinkedQueue = RAND_QUEUE;
        SecureRandom poll = concurrentLinkedQueue.poll();
        if (poll == null) {
            poll = new SecureRandom();
        }
        poll.nextBytes(bArr);
        concurrentLinkedQueue.add(poll);
    }

    public static byte[] rand(int i) {
        byte[] bArr = new byte[i];
        rand(bArr);
        return bArr;
    }

    public static int readInt(byte[] bArr, int i) {
        int i2 = i + 1;
        int i3 = i2 + 1;
        int i4 = ((bArr[i] & 255) << 24) | 0 | ((bArr[i2] & 255) << 16);
        return (bArr[i3 + 1] & 255) | i4 | ((bArr[i3] & 255) << 8);
    }

    public static long readLong(byte[] bArr, int i) {
        int i2 = i + 1;
        int i3 = i2 + 1;
        int i4 = i3 + 1;
        int i5 = i4 + 1;
        int i6 = i5 + 1;
        int i7 = i6 + 1;
        return (bArr[i7 + 1] & 255) | ((bArr[i] & 255) << 56) | 0 | ((bArr[i2] & 255) << 48) | ((bArr[i3] & 255) << 40) | ((bArr[i4] & 255) << 32) | ((bArr[i5] & 255) << 24) | ((bArr[i6] & 255) << 16) | ((bArr[i7] & 255) << 8);
    }

    public static int toInt(byte[] bArr) {
        return readInt(bArr, 0);
    }

    public static long toLong(byte[] bArr) {
        return readLong(bArr, 0);
    }

    public static void writeInt(int i, byte[] bArr, int i2) {
        int i3 = i2 + 1;
        bArr[i2] = (byte) (i >> 24);
        int i4 = i3 + 1;
        bArr[i3] = (byte) (i >> 16);
        bArr[i4] = (byte) (i >> 8);
        bArr[i4 + 1] = (byte) i;
    }

    public static void writeLong(long j, byte[] bArr, int i) {
        int i2 = i + 1;
        bArr[i] = (byte) (j >> 56);
        int i3 = i2 + 1;
        bArr[i2] = (byte) (j >> 48);
        int i4 = i3 + 1;
        bArr[i3] = (byte) (j >> 40);
        int i5 = i4 + 1;
        bArr[i4] = (byte) (j >> 32);
        int i6 = i5 + 1;
        bArr[i5] = (byte) (j >> 24);
        int i7 = i6 + 1;
        bArr[i6] = (byte) (j >> 16);
        bArr[i7] = (byte) (j >> 8);
        bArr[i7 + 1] = (byte) j;
    }

    public static boolean safeArrayEquals(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr2 == null) {
            return bArr == bArr2;
        } else if (bArr.length != bArr2.length) {
            return false;
        } else {
            byte b = 0;
            for (int i = 0; i < bArr.length; i++) {
                b = (byte) (b | (bArr[i] ^ bArr2[i]));
            }
            return b == 0;
        }
    }

    public static byte[] cat(byte[]... bArr) {
        int i = 0;
        for (byte[] bArr2 : bArr) {
            i += bArr2.length;
        }
        byte[] bArr3 = new byte[i];
        int i2 = 0;
        for (byte[] bArr4 : bArr) {
            System.arraycopy(bArr4, 0, bArr3, i2, bArr4.length);
            i2 += bArr4.length;
        }
        return bArr3;
    }

    public static List<String> split(String str, int i) {
        ArrayList arrayList = new ArrayList();
        int length = str.length();
        int i2 = 0;
        while (i2 < length) {
            int i3 = i2 + i;
            arrayList.add(str.substring(i2, Math.min(length, i3)));
            i2 = i3;
        }
        return arrayList;
    }

    public static byte[] readStreamFully(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[8192];
        while (true) {
            int read = inputStream.read(bArr);
            if (read != -1) {
                byteArrayOutputStream.write(bArr, 0, read);
            } else {
                return byteArrayOutputStream.toByteArray();
            }
        }
    }

    public static String encodeBigInteger(BigInteger bigInteger) {
        return Base64Coder.encodeWebSafe(bigInteger.toByteArray());
    }

    public static BigInteger decodeBigInteger(String str) throws Base64DecodingException {
        return new BigInteger(Base64Coder.decodeWebSafe(str));
    }

    public static KeyPair generateKeyPair(String str, int i) throws KeyczarException {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(str);
            keyPairGenerator.initialize(i);
            return keyPairGenerator.generateKeyPair();
        } catch (GeneralSecurityException e) {
            throw new KeyczarException(e);
        }
    }
}
