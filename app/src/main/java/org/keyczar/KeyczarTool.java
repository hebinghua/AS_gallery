package org.keyczar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import org.keyczar.enums.Command;
import org.keyczar.enums.Flag;
import org.keyczar.enums.KeyPurpose;
import org.keyczar.enums.KeyStatus;
import org.keyczar.enums.RsaPadding;
import org.keyczar.exceptions.KeyczarException;
import org.keyczar.i18n.Messages;
import org.keyczar.interfaces.KeyType;
import org.keyczar.interfaces.KeyczarReader;

/* loaded from: classes3.dex */
public class KeyczarTool {
    private static MockKeyczarReader mock;

    public static void setReader(MockKeyczarReader mockKeyczarReader) {
        mock = mockKeyczarReader;
    }

    public static MockKeyczarReader getMock() {
        return mock;
    }

    public static void main(String[] strArr) {
        if (strArr.length == 0) {
            printUsage();
            return;
        }
        try {
            Command command = Command.getCommand(strArr[0]);
            HashMap hashMap = new HashMap();
            for (String str : strArr) {
                if (str.startsWith("--")) {
                    String[] split = str.substring(2).split("=");
                    if (split.length > 1) {
                        hashMap.put(Flag.getFlag(split[0]), split[1]);
                    }
                }
            }
            String str2 = (String) hashMap.get(Flag.LOCATION);
            if (str2 != null) {
                String str3 = File.separator;
                if (!str2.endsWith(str3)) {
                    str2 = str2 + str3;
                }
            }
            String str4 = str2;
            KeyPurpose purpose = KeyPurpose.getPurpose((String) hashMap.get(Flag.PURPOSE));
            KeyStatus status = KeyStatus.getStatus((String) hashMap.get(Flag.STATUS));
            String str5 = (String) hashMap.get(Flag.ASYMMETRIC);
            String str6 = (String) hashMap.get(Flag.CRYPTER);
            String str7 = (String) hashMap.get(Flag.DESTINATION);
            String str8 = (String) hashMap.get(Flag.NAME);
            String str9 = (String) hashMap.get(Flag.PADDING);
            String str10 = (String) hashMap.get(Flag.PASSPHRASE);
            String str11 = (String) hashMap.get(Flag.PEMFILE);
            String str12 = (String) hashMap.get(Flag.VERSION);
            int i = -1;
            Flag flag = Flag.SIZE;
            if (hashMap.containsKey(flag)) {
                i = Integer.parseInt((String) hashMap.get(flag));
            }
            int i2 = i;
            switch (AnonymousClass1.$SwitchMap$org$keyczar$enums$Command[command.ordinal()]) {
                case 1:
                    create(str4, str8, purpose, str5);
                    return;
                case 2:
                    addKey(str4, status, str6, i2, str9);
                    return;
                case 3:
                    publicKeys(str4, str7);
                    return;
                case 4:
                    promote(str4, Integer.parseInt(str12));
                    return;
                case 5:
                    demote(str4, Integer.parseInt(str12));
                    return;
                case 6:
                    revoke(str4, Integer.parseInt(str12));
                    return;
                case 7:
                    if (strArr.length > 2) {
                        useKey(strArr[1], str4, str7, str6);
                        return;
                    } else {
                        printUsage();
                        return;
                    }
                case 8:
                    importKey(str4, str11, status, str6, str9, str10);
                    return;
                case 9:
                    exportKey(str4, str6, Integer.parseInt(str12), str11, str10);
                    return;
                default:
                    return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            printUsage();
        }
    }

    private static void exportKey(String str, String str2, int i, String str3, String str4) throws KeyczarException {
        if (i < 0) {
            throw new KeyczarException(Messages.getString("KeyczarTool.MissingVersion", new Object[0]));
        }
        GenericKeyczar createGenericKeyczar = createGenericKeyczar(str, str2);
        String pemString = createGenericKeyczar.getKey(createGenericKeyczar.getVersion(i)).getPemString(str4);
        try {
            File file = new File(str3);
            if (!file.createNewFile()) {
                throw new KeyczarException(Messages.getString("", file));
            }
            new FileOutputStream(file).write(pemString.getBytes("UTF8"));
        } catch (IOException e) {
            throw new KeyczarException(Messages.getString("", new Object[0]), e);
        }
    }

    private static void importKey(String str, String str2, KeyStatus keyStatus, String str3, String str4, String str5) throws KeyczarException, IOException {
        GenericKeyczar createGenericKeyczar = createGenericKeyczar(str, str3);
        KeyMetadata metadata = createGenericKeyczar.getMetadata();
        GenericKeyczar importingKeyczar = getImportingKeyczar(str2, str4, str5, metadata.getPurpose());
        KeyType type = importingKeyczar.getMetadata().getType();
        if (metadata.getType() != type && createGenericKeyczar.getVersions().isEmpty()) {
            metadata.setType(type);
        }
        createGenericKeyczar.addVersion(keyStatus, importingKeyczar.getPrimaryKey());
        updateGenericKeyczar(createGenericKeyczar, str3, str);
    }

    private static GenericKeyczar getImportingKeyczar(String str, String str2, String str3, KeyPurpose keyPurpose) throws KeyczarException, IOException {
        RsaPadding padding = getPadding(str2);
        InputStream fileStream = getFileStream(str);
        try {
            return new GenericKeyczar(new X509CertificateReader(keyPurpose, fileStream, padding));
        } catch (KeyczarException e) {
            if (e.getCause() instanceof CertificateException) {
                fileStream.close();
                return new GenericKeyczar(new PkcsKeyReader(keyPurpose, getFileStream(str), padding, str3));
            }
            throw e;
        } finally {
            fileStream.close();
        }
    }

    private static InputStream getFileStream(String str) throws KeyczarException {
        try {
            return new FileInputStream(str);
        } catch (FileNotFoundException unused) {
            throw new KeyczarException(Messages.getString("KeyczarTool.FileNotFound", str));
        }
    }

    private static void useKey(String str, String str2, String str3, String str4) throws KeyczarException {
        String encrypt;
        GenericKeyczar createGenericKeyczar = createGenericKeyczar(str2, str4);
        if (str3 == null) {
            throw new KeyczarException(Messages.getString("KeyczarTool.MustDefinePublic", new Object[0]));
        }
        KeyczarReader keyczarFileReader = new KeyczarFileReader(str2);
        if (str4 != null) {
            keyczarFileReader = new KeyczarEncryptedReader(keyczarFileReader, new Crypter(str4));
        }
        int i = AnonymousClass1.$SwitchMap$org$keyczar$enums$KeyPurpose[createGenericKeyczar.getMetadata().getPurpose().ordinal()];
        if (i == 1) {
            encrypt = new Crypter(keyczarFileReader).encrypt(str);
        } else if (i == 2) {
            encrypt = new Signer(keyczarFileReader).sign(str);
        } else {
            throw new KeyczarException(Messages.getString("KeyczarTool.UnsupportedPurpose", createGenericKeyczar.getMetadata().getPurpose()));
        }
        createGenericKeyczar.writeFile(encrypt, str3);
    }

    /* renamed from: org.keyczar.KeyczarTool$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$org$keyczar$enums$Command;
        public static final /* synthetic */ int[] $SwitchMap$org$keyczar$enums$KeyPurpose;

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
            try {
                $SwitchMap$org$keyczar$enums$KeyPurpose[KeyPurpose.TEST.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            int[] iArr2 = new int[Command.values().length];
            $SwitchMap$org$keyczar$enums$Command = iArr2;
            try {
                iArr2[Command.CREATE.ordinal()] = 1;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$org$keyczar$enums$Command[Command.ADDKEY.ordinal()] = 2;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$org$keyczar$enums$Command[Command.PUBKEY.ordinal()] = 3;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$org$keyczar$enums$Command[Command.PROMOTE.ordinal()] = 4;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$org$keyczar$enums$Command[Command.DEMOTE.ordinal()] = 5;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$org$keyczar$enums$Command[Command.REVOKE.ordinal()] = 6;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$org$keyczar$enums$Command[Command.USEKEY.ordinal()] = 7;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$org$keyczar$enums$Command[Command.IMPORT_KEY.ordinal()] = 8;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$org$keyczar$enums$Command[Command.EXPORT_KEY.ordinal()] = 9;
            } catch (NoSuchFieldError unused12) {
            }
        }
    }

    private static void addKey(String str, KeyStatus keyStatus, String str2, int i, String str3) throws KeyczarException {
        GenericKeyczar createGenericKeyczar = createGenericKeyczar(str, str2);
        if (i == -1) {
            createGenericKeyczar.addVersion(keyStatus, getPadding(str3));
        } else {
            createGenericKeyczar.addVersion(keyStatus, getPadding(str3), i);
        }
        updateGenericKeyczar(createGenericKeyczar, str2, str);
    }

    private static RsaPadding getPadding(String str) throws KeyczarException {
        if (str != null) {
            try {
                return RsaPadding.valueOf(str.toUpperCase());
            } catch (IllegalArgumentException unused) {
                throw new KeyczarException(Messages.getString("InvalidPadding", str));
            }
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:26:0x0073  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00e2  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private static void create(java.lang.String r4, java.lang.String r5, org.keyczar.enums.KeyPurpose r6, java.lang.String r7) throws org.keyczar.exceptions.KeyczarException {
        /*
            Method dump skipped, instructions count: 256
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.keyczar.KeyczarTool.create(java.lang.String, java.lang.String, org.keyczar.enums.KeyPurpose, java.lang.String):void");
    }

    private static void promote(String str, int i) throws KeyczarException {
        if (i < 0) {
            throw new KeyczarException(Messages.getString("KeyczarTool.MissingVersion", new Object[0]));
        }
        GenericKeyczar createGenericKeyczar = createGenericKeyczar(str);
        createGenericKeyczar.promote(i);
        updateGenericKeyczar(createGenericKeyczar, str);
    }

    private static void demote(String str, int i) throws KeyczarException {
        if (i < 0) {
            throw new KeyczarException(Messages.getString("KeyczarTool.MissingVersion", new Object[0]));
        }
        GenericKeyczar createGenericKeyczar = createGenericKeyczar(str);
        createGenericKeyczar.demote(i);
        updateGenericKeyczar(createGenericKeyczar, str);
    }

    private static void publicKeys(String str, String str2) throws KeyczarException {
        if (mock == null && str2 == null) {
            throw new KeyczarException(Messages.getString("KeyczarTool.MustDefineDestination", new Object[0]));
        }
        createGenericKeyczar(str).publicKeyExport(str2);
    }

    private static void revoke(String str, int i) throws KeyczarException {
        GenericKeyczar createGenericKeyczar = createGenericKeyczar(str);
        createGenericKeyczar.revoke(i);
        updateGenericKeyczar(createGenericKeyczar, str);
        MockKeyczarReader mockKeyczarReader = mock;
        if (mockKeyczarReader == null) {
            if (!new File(str + i).delete()) {
                throw new KeyczarException(Messages.getString("KeyczarTool.UnableToDelete", new Object[0]));
            }
            return;
        }
        mockKeyczarReader.removeKey(i);
    }

    private static void printUsage() {
        ArrayList arrayList = new ArrayList();
        for (Command command : Command.values()) {
            arrayList.add(command.toString());
        }
        for (Flag flag : Flag.values()) {
            arrayList.add(flag.toString());
        }
        System.out.println(Messages.getString("KeyczarTool.Usage", arrayList.toArray()));
    }

    private static GenericKeyczar createGenericKeyczar(String str) throws KeyczarException {
        return createGenericKeyczar(str, null);
    }

    private static GenericKeyczar createGenericKeyczar(String str, String str2) throws KeyczarException {
        if (mock != null) {
            return new GenericKeyczar(mock);
        }
        if (str == null) {
            throw new KeyczarException(Messages.getString("KeyczarTool.NeedLocation", Messages.getString("KeyczarTool.Location", new Object[0])));
        }
        KeyczarReader keyczarFileReader = new KeyczarFileReader(str);
        if (str2 != null) {
            keyczarFileReader = new KeyczarEncryptedReader(keyczarFileReader, new Crypter(str2));
        }
        return new GenericKeyczar(keyczarFileReader);
    }

    private static void updateGenericKeyczar(GenericKeyczar genericKeyczar, String str) throws KeyczarException {
        updateGenericKeyczar(genericKeyczar, null, str);
    }

    private static void updateGenericKeyczar(GenericKeyczar genericKeyczar, String str, String str2) throws KeyczarException {
        MockKeyczarReader mockKeyczarReader = mock;
        if (mockKeyczarReader == null) {
            if (str != null) {
                genericKeyczar.writeEncrypted(str2, new Encrypter(str));
                return;
            } else {
                genericKeyczar.write(str2);
                return;
            }
        }
        mockKeyczarReader.setMetadata(genericKeyczar.getMetadata());
        for (KeyVersion keyVersion : genericKeyczar.getVersions()) {
            mock.setKey(keyVersion.getVersionNumber(), genericKeyczar.getKey(keyVersion));
        }
    }
}
