package org.keyczar;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import org.keyczar.enums.KeyPurpose;
import org.keyczar.enums.KeyStatus;
import org.keyczar.enums.RsaPadding;
import org.keyczar.exceptions.KeyczarException;
import org.keyczar.i18n.Messages;
import org.keyczar.interfaces.KeyczarReader;

/* loaded from: classes3.dex */
public class GenericKeyczar extends Keyczar {
    @Override // org.keyczar.Keyczar
    public boolean isAcceptablePurpose(KeyPurpose keyPurpose) {
        return true;
    }

    public GenericKeyczar(KeyczarReader keyczarReader) throws KeyczarException {
        super(keyczarReader);
    }

    public GenericKeyczar(String str) throws KeyczarException {
        super(str);
    }

    public KeyMetadata getMetadata() {
        return this.kmd;
    }

    public Set<KeyVersion> getVersions() {
        return Collections.unmodifiableSet(this.versionMap.keySet());
    }

    public KeyczarKey getKey(KeyVersion keyVersion) {
        return this.versionMap.get(keyVersion);
    }

    public void promote(int i) throws KeyczarException {
        KeyVersion version = getVersion(i);
        int i2 = AnonymousClass1.$SwitchMap$org$keyczar$enums$KeyStatus[version.getStatus().ordinal()];
        if (i2 != 1) {
            if (i2 != 2) {
                if (i2 != 3) {
                    return;
                }
                version.setStatus(KeyStatus.ACTIVE);
                return;
            }
            version.setStatus(KeyStatus.PRIMARY);
            KeyVersion keyVersion = this.primaryVersion;
            if (keyVersion != null) {
                keyVersion.setStatus(KeyStatus.ACTIVE);
            }
            this.primaryVersion = version;
            return;
        }
        throw new KeyczarException(Messages.getString("Keyczar.CantPromotePrimary", new Object[0]));
    }

    public void demote(int i) throws KeyczarException {
        KeyVersion version = getVersion(i);
        int i2 = AnonymousClass1.$SwitchMap$org$keyczar$enums$KeyStatus[version.getStatus().ordinal()];
        if (i2 == 1) {
            version.setStatus(KeyStatus.ACTIVE);
            this.primaryVersion = null;
        } else if (i2 == 2) {
            version.setStatus(KeyStatus.INACTIVE);
        } else if (i2 == 3) {
            throw new KeyczarException(Messages.getString("Keyczar.CantDemoteScheduled", new Object[0]));
        }
    }

    public void addVersion(KeyStatus keyStatus) throws KeyczarException {
        addVersion(keyStatus, null, this.kmd.getType().defaultSize());
    }

    public void addVersion(KeyStatus keyStatus, int i) throws KeyczarException {
        addVersion(keyStatus, null, i);
    }

    public void addVersion(KeyStatus keyStatus, RsaPadding rsaPadding) throws KeyczarException {
        addVersion(keyStatus, rsaPadding, this.kmd.getType().defaultSize());
    }

    public void addVersion(KeyStatus keyStatus, RsaPadding rsaPadding, int i) throws KeyczarException {
        KeyczarKey genKey;
        do {
            genKey = KeyczarKey.genKey(this.kmd.getType(), rsaPadding, i);
        } while (getKey(genKey.hash()) != null);
        addVersion(keyStatus, genKey);
    }

    public void addVersion(KeyStatus keyStatus, KeyczarKey keyczarKey) {
        KeyVersion keyVersion = new KeyVersion(numVersions() + 1, keyStatus, false);
        if (keyStatus == KeyStatus.PRIMARY) {
            KeyVersion keyVersion2 = this.primaryVersion;
            if (keyVersion2 != null) {
                keyVersion2.setStatus(KeyStatus.ACTIVE);
            }
            this.primaryVersion = keyVersion;
        }
        addKey(keyVersion, keyczarKey);
    }

    public KeyVersion getVersion(int i) throws KeyczarException {
        KeyVersion version = this.kmd.getVersion(i);
        if (version != null) {
            return version;
        }
        throw new KeyczarException(Messages.getString("Keyczar.NoSuchVersion", Integer.valueOf(i)));
    }

    public void revoke(int i) throws KeyczarException {
        if (getVersion(i).getStatus() == KeyStatus.INACTIVE) {
            this.kmd.removeVersion(i);
            return;
        }
        throw new KeyczarException(Messages.getString("Keyczar.CantRevoke", new Object[0]));
    }

    private int numVersions() {
        return this.versionMap.size();
    }

    public void publicKeyExport(String str) throws KeyczarException {
        if (str != null) {
            String str2 = File.separator;
            if (!str.endsWith(str2)) {
                str = str + str2;
            }
        }
        KeyMetadata metadata = getMetadata();
        KeyMetadata keyMetadata = null;
        if (metadata.getType() == DefaultKeyType.DSA_PRIV) {
            if (metadata.getPurpose() == KeyPurpose.SIGN_AND_VERIFY) {
                keyMetadata = new KeyMetadata(metadata.getName(), KeyPurpose.VERIFY, DefaultKeyType.DSA_PUB);
            }
        } else if (metadata.getType() == DefaultKeyType.RSA_PRIV) {
            int i = AnonymousClass1.$SwitchMap$org$keyczar$enums$KeyPurpose[metadata.getPurpose().ordinal()];
            if (i == 1) {
                keyMetadata = new KeyMetadata(metadata.getName(), KeyPurpose.ENCRYPT, DefaultKeyType.RSA_PUB);
            } else if (i == 2) {
                keyMetadata = new KeyMetadata(metadata.getName(), KeyPurpose.VERIFY, DefaultKeyType.RSA_PUB);
            }
        }
        if (keyMetadata == null) {
            throw new KeyczarException(Messages.getString("KeyczarTool.CannotExportPubKey", metadata.getType(), metadata.getPurpose()));
        }
        for (KeyVersion keyVersion : getVersions()) {
            KeyczarPublicKey keyczarPublicKey = ((KeyczarPrivateKey) getKey(keyVersion)).getPublic();
            if (KeyczarTool.getMock() == null) {
                writeFile(keyczarPublicKey.toString(), str + keyVersion.getVersionNumber());
            } else {
                KeyczarTool.getMock().setPublicKey(keyVersion.getVersionNumber(), keyczarPublicKey);
            }
            keyMetadata.addVersion(keyVersion);
        }
        if (KeyczarTool.getMock() == null) {
            writeFile(keyMetadata.toString(), str + KeyczarFileReader.META_FILE);
            return;
        }
        KeyczarTool.getMock().setPublicKeyMetadata(keyMetadata);
    }

    /* renamed from: org.keyczar.GenericKeyczar$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$org$keyczar$enums$KeyPurpose;
        public static final /* synthetic */ int[] $SwitchMap$org$keyczar$enums$KeyStatus;

        static {
            int[] iArr = new int[KeyPurpose.values().length];
            $SwitchMap$org$keyczar$enums$KeyPurpose = iArr;
            try {
                iArr[KeyPurpose.DECRYPT_AND_ENCRYPT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$org$keyczar$enums$KeyPurpose[KeyPurpose.SIGN_AND_VERIFY.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            int[] iArr2 = new int[KeyStatus.values().length];
            $SwitchMap$org$keyczar$enums$KeyStatus = iArr2;
            try {
                iArr2[KeyStatus.PRIMARY.ordinal()] = 1;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$org$keyczar$enums$KeyStatus[KeyStatus.ACTIVE.ordinal()] = 2;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$org$keyczar$enums$KeyStatus[KeyStatus.INACTIVE.ordinal()] = 3;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    public void write(String str) throws KeyczarException {
        String keyMetadata = this.kmd.toString();
        writeFile(keyMetadata, str + KeyczarFileReader.META_FILE);
        for (KeyVersion keyVersion : getVersions()) {
            String keyczarKey = getKey(keyVersion).toString();
            writeFile(keyczarKey, str + keyVersion.getVersionNumber());
        }
    }

    public void writeEncrypted(String str, Encrypter encrypter) throws KeyczarException {
        KeyMetadata metadata = getMetadata();
        metadata.setEncrypted(true);
        String keyMetadata = metadata.toString();
        writeFile(keyMetadata, str + KeyczarFileReader.META_FILE);
        for (KeyVersion keyVersion : getVersions()) {
            String encrypt = encrypter.encrypt(getKey(keyVersion).toString());
            writeFile(encrypt, str + keyVersion.getVersionNumber());
        }
    }

    public void writeFile(String str, String str2) throws KeyczarException {
        File file = new File(str2);
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(str);
            fileWriter.close();
        } catch (IOException e) {
            throw new KeyczarException(Messages.getString("KeyczarTool.UnableToWrite", file.toString()), e);
        }
    }
}
