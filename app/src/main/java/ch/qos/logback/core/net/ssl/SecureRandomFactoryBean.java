package ch.qos.logback.core.net.ssl;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

/* loaded from: classes.dex */
public class SecureRandomFactoryBean {
    private String algorithm;
    private String provider;

    public SecureRandom createSecureRandom() throws NoSuchProviderException, NoSuchAlgorithmException {
        try {
            if (getProvider() != null) {
                return SecureRandom.getInstance(getAlgorithm(), getProvider());
            }
            return SecureRandom.getInstance(getAlgorithm());
        } catch (NoSuchAlgorithmException unused) {
            throw new NoSuchAlgorithmException("no such secure random algorithm: " + getAlgorithm());
        } catch (NoSuchProviderException unused2) {
            throw new NoSuchProviderException("no such secure random provider: " + getProvider());
        }
    }

    public String getAlgorithm() {
        String str = this.algorithm;
        return str == null ? SSL.DEFAULT_SECURE_RANDOM_ALGORITHM : str;
    }

    public void setAlgorithm(String str) {
        this.algorithm = str;
    }

    public String getProvider() {
        return this.provider;
    }

    public void setProvider(String str) {
        this.provider = str;
    }
}
