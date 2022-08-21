package org.keyczar;

import com.google.gson_nex.annotations.Expose;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.DSAPrivateKey;
import java.security.spec.DSAPrivateKeySpec;
import org.keyczar.exceptions.KeyczarException;
import org.keyczar.interfaces.KeyType;
import org.keyczar.interfaces.SigningStream;
import org.keyczar.interfaces.Stream;
import org.keyczar.interfaces.VerifyingStream;
import org.keyczar.util.Base64Coder;
import org.keyczar.util.Util;

/* loaded from: classes3.dex */
public class DsaPrivateKey extends KeyczarKey implements KeyczarPrivateKey {
    private static final String KEY_GEN_ALGORITHM = "DSA";
    private static final String SIG_ALGORITHM = "SHA1withDSA";
    private DSAPrivateKey jcePrivateKey;
    @Expose
    private final DsaPublicKey publicKey;
    @Expose
    private final String x;

    public String getKeyGenAlgorithm() {
        return KEY_GEN_ALGORITHM;
    }

    public static DsaPrivateKey generate() throws KeyczarException {
        return generate(DefaultKeyType.DSA_PRIV.defaultSize());
    }

    public static DsaPrivateKey generate(int i) throws KeyczarException {
        return new DsaPrivateKey((DSAPrivateKey) Util.generateKeyPair(KEY_GEN_ALGORITHM, i).getPrivate());
    }

    public static DsaPrivateKey read(String str) throws KeyczarException {
        return ((DsaPrivateKey) Util.gson().fromJson(str, (Class<Object>) DsaPrivateKey.class)).initFromJson();
    }

    public DsaPrivateKey(DSAPrivateKey dSAPrivateKey) throws KeyczarException {
        super(dSAPrivateKey.getParams().getP().bitLength());
        this.publicKey = new DsaPublicKey(dSAPrivateKey);
        this.jcePrivateKey = dSAPrivateKey;
        this.x = Base64Coder.encodeWebSafe(dSAPrivateKey.getX().toByteArray());
    }

    private DsaPrivateKey() {
        super(0);
        this.publicKey = null;
        this.x = null;
    }

    @Override // org.keyczar.KeyczarKey
    public byte[] hash() {
        return getPublic().hash();
    }

    @Override // org.keyczar.KeyczarPrivateKey
    public KeyczarPublicKey getPublic() {
        return this.publicKey;
    }

    @Override // org.keyczar.KeyczarKey
    public Stream getStream() throws KeyczarException {
        return new DsaSigningStream();
    }

    @Override // org.keyczar.KeyczarKey
    public KeyType getType() {
        return DefaultKeyType.DSA_PRIV;
    }

    private DsaPrivateKey initFromJson() throws KeyczarException {
        this.publicKey.initFromJson();
        try {
            this.jcePrivateKey = (DSAPrivateKey) KeyFactory.getInstance(KEY_GEN_ALGORITHM).generatePrivate(new DSAPrivateKeySpec(new BigInteger(Base64Coder.decodeWebSafe(this.x)), new BigInteger(Base64Coder.decodeWebSafe(this.publicKey.p)), new BigInteger(Base64Coder.decodeWebSafe(this.publicKey.q)), new BigInteger(Base64Coder.decodeWebSafe(this.publicKey.g))));
            return this;
        } catch (GeneralSecurityException e) {
            throw new KeyczarException(e);
        }
    }

    @Override // org.keyczar.KeyczarKey
    /* renamed from: getJceKey  reason: collision with other method in class */
    public DSAPrivateKey mo2644getJceKey() {
        return this.jcePrivateKey;
    }

    /* loaded from: classes3.dex */
    public class DsaSigningStream implements SigningStream, VerifyingStream {
        private Signature signature;
        private VerifyingStream verifyingStream;

        public DsaSigningStream() throws KeyczarException {
            try {
                this.signature = Signature.getInstance(DsaPrivateKey.SIG_ALGORITHM);
                this.verifyingStream = (VerifyingStream) DsaPrivateKey.this.publicKey.getStream();
            } catch (GeneralSecurityException e) {
                throw new KeyczarException(e);
            }
        }

        @Override // org.keyczar.interfaces.SigningStream, org.keyczar.interfaces.VerifyingStream
        public int digestSize() {
            return DsaPrivateKey.this.getType().getOutputSize();
        }

        @Override // org.keyczar.interfaces.SigningStream
        public void initSign() throws KeyczarException {
            try {
                this.signature.initSign(DsaPrivateKey.this.jcePrivateKey);
            } catch (GeneralSecurityException e) {
                throw new KeyczarException(e);
            }
        }

        @Override // org.keyczar.interfaces.VerifyingStream
        public void initVerify() throws KeyczarException {
            this.verifyingStream.initVerify();
        }

        @Override // org.keyczar.interfaces.SigningStream
        public void sign(ByteBuffer byteBuffer) throws KeyczarException {
            try {
                byteBuffer.put(this.signature.sign());
            } catch (SignatureException e) {
                throw new KeyczarException(e);
            }
        }

        @Override // org.keyczar.interfaces.SigningStream
        public void updateSign(ByteBuffer byteBuffer) throws KeyczarException {
            try {
                this.signature.update(byteBuffer);
            } catch (SignatureException e) {
                throw new KeyczarException(e);
            }
        }

        @Override // org.keyczar.interfaces.VerifyingStream
        public void updateVerify(ByteBuffer byteBuffer) throws KeyczarException {
            this.verifyingStream.updateVerify(byteBuffer);
        }

        @Override // org.keyczar.interfaces.VerifyingStream
        public boolean verify(ByteBuffer byteBuffer) throws KeyczarException {
            return this.verifyingStream.verify(byteBuffer);
        }
    }
}
