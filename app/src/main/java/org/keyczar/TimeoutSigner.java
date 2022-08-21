package org.keyczar;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import org.keyczar.exceptions.KeyczarException;
import org.keyczar.interfaces.KeyczarReader;
import org.keyczar.util.Base64Coder;

/* loaded from: classes3.dex */
public class TimeoutSigner extends TimeoutVerifier {
    private Signer signer;

    public TimeoutSigner(KeyczarReader keyczarReader) throws KeyczarException {
        Signer signer = new Signer(keyczarReader);
        this.signer = signer;
        setVerifier(signer);
    }

    public TimeoutSigner(String str) throws KeyczarException {
        Signer signer = new Signer(str);
        this.signer = signer;
        setVerifier(signer);
    }

    public TimeoutSigner(Signer signer) {
        this.signer = signer;
        setVerifier(signer);
    }

    public String timeoutSign(String str, long j) throws KeyczarException {
        try {
            return Base64Coder.encodeWebSafe(timeoutSign(str.getBytes(Keyczar.DEFAULT_ENCODING), j));
        } catch (UnsupportedEncodingException e) {
            throw new KeyczarException(e);
        }
    }

    public byte[] timeoutSign(byte[] bArr, long j) throws KeyczarException {
        ByteBuffer allocate = ByteBuffer.allocate(this.signer.digestSize() + 8);
        timeoutSign(ByteBuffer.wrap(bArr), j, allocate);
        allocate.reset();
        byte[] bArr2 = new byte[allocate.remaining()];
        allocate.get(bArr2);
        return bArr2;
    }

    public void timeoutSign(ByteBuffer byteBuffer, long j, ByteBuffer byteBuffer2) throws KeyczarException {
        this.signer.sign(byteBuffer, null, j, byteBuffer2);
    }
}
