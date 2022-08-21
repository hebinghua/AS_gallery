package org.keyczar;

import java.util.ArrayList;
import java.util.List;
import org.keyczar.annotations.Experimental;
import org.keyczar.enums.KeyPurpose;
import org.keyczar.enums.KeyStatus;
import org.keyczar.exceptions.KeyczarException;
import org.keyczar.interfaces.KeyczarReader;

@Experimental
/* loaded from: classes3.dex */
public class ImportedKeyReader implements KeyczarReader {
    private final List<KeyczarKey> keys;
    private final KeyMetadata metadata;

    public ImportedKeyReader(KeyMetadata keyMetadata, List<KeyczarKey> list) {
        this.metadata = keyMetadata;
        this.keys = list;
    }

    public ImportedKeyReader(AesKey aesKey) {
        KeyMetadata keyMetadata = new KeyMetadata("Imported AES", KeyPurpose.DECRYPT_AND_ENCRYPT, DefaultKeyType.AES);
        this.metadata = keyMetadata;
        keyMetadata.addVersion(new KeyVersion(0, KeyStatus.PRIMARY, false));
        ArrayList arrayList = new ArrayList();
        this.keys = arrayList;
        arrayList.add(aesKey);
    }

    public ImportedKeyReader(HmacKey hmacKey) {
        KeyMetadata keyMetadata = new KeyMetadata("Imported HMAC", KeyPurpose.SIGN_AND_VERIFY, DefaultKeyType.HMAC_SHA1);
        this.metadata = keyMetadata;
        keyMetadata.addVersion(new KeyVersion(0, KeyStatus.PRIMARY, false));
        ArrayList arrayList = new ArrayList();
        this.keys = arrayList;
        arrayList.add(hmacKey);
    }

    @Override // org.keyczar.interfaces.KeyczarReader
    public String getKey() throws KeyczarException {
        return getKey(KeyMetadata.read(getMetadata()).getPrimaryVersion().getVersionNumber());
    }

    @Override // org.keyczar.interfaces.KeyczarReader
    public String getKey(int i) {
        return this.keys.get(i).toString();
    }

    @Override // org.keyczar.interfaces.KeyczarReader
    public String getMetadata() {
        return this.metadata.toString();
    }
}
