package com.nexstreaming.app.common.nexasset.assetpackage;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;
import ch.qos.logback.classic.pattern.CallerDataConverter;
import com.google.gson_nex.Gson;
import com.google.gson_nex.JsonSyntaxException;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/* loaded from: classes3.dex */
public class AssetPackageReader implements Closeable {
    private static Map<String, WeakReference<AssetPackageReader>> h = new HashMap();
    private static Map<String, com.nexstreaming.app.common.nexasset.assetpackage.security.b> i = new HashMap();
    private c a;
    private final PackageInfoJSON c;
    private final EncryptionInfoJSON d;
    private final String e;
    private final boolean f;
    private final com.nexstreaming.app.common.nexasset.assetpackage.security.a j;
    private final Gson b = new Gson();
    private List<com.nexstreaming.app.common.nexasset.assetpackage.f> g = null;
    private Map<String, String> k = null;
    private Map<String, String> l = null;

    /* loaded from: classes3.dex */
    public interface c {
        InputStream a(String str) throws FileNotFoundException, IOException;

        Iterable<String> a();

        File b(String str) throws LocalPathNotAvailableException, EncryptedException, FileNotFoundException, IOException;

        void b() throws IOException;

        Typeface c(String str) throws LocalPathNotAvailableException;

        String c();

        File d();
    }

    /* loaded from: classes3.dex */
    public static class a extends WeakReference<AssetPackageReader> {
        private static ReferenceQueue<AssetPackageReader> a = new ReferenceQueue<>();
        private c b;

        public a(AssetPackageReader assetPackageReader) {
            super(assetPackageReader, a);
            this.b = assetPackageReader.k();
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static void b() {
            while (true) {
                a aVar = (a) a.poll();
                if (aVar != null) {
                    c cVar = aVar.b;
                    if (cVar != null) {
                        try {
                            cVar.b();
                            Log.d("AssetPackageReader", "Closed cached container reader");
                        } catch (IOException e) {
                            Log.d("AssetPackageReader", "Error closing container reader", e);
                        }
                        aVar.b = null;
                    }
                } else {
                    return;
                }
            }
        }
    }

    public static String[] a() {
        int i2;
        ArrayList<String> arrayList = new ArrayList();
        Iterator<Map.Entry<String, com.nexstreaming.app.common.nexasset.assetpackage.security.b>> it = i.entrySet().iterator();
        while (true) {
            i2 = 0;
            if (!it.hasNext()) {
                break;
            }
            String[] b2 = it.next().getValue().b();
            int length = b2.length;
            while (i2 < length) {
                arrayList.add(b2[i2]);
                i2++;
            }
        }
        String[] strArr = new String[arrayList.size()];
        for (String str : arrayList) {
            strArr[i2] = str;
            i2++;
        }
        return strArr;
    }

    public static void a(com.nexstreaming.app.common.nexasset.assetpackage.security.b bVar) {
        String a2 = bVar.a();
        if (a2 == null || a2.length() < 1) {
            throw new IllegalArgumentException("id is null or empty");
        }
        if (i.get(a2) != null) {
            throw new IllegalStateException("id already in use :" + i.get(a2));
        } else if (i.values().contains(bVar)) {
            throw new IllegalStateException("provider already registered");
        } else {
            i.put(a2, bVar);
        }
    }

    public static void b(com.nexstreaming.app.common.nexasset.assetpackage.security.b bVar) {
        if (bVar == null) {
            return;
        }
        if (i.get(bVar.a()) != bVar) {
            throw new IllegalStateException();
        }
        i.remove(bVar.a());
    }

    public static AssetPackageReader a(File file, String str) throws IOException {
        return new AssetPackageReader(new g(file), str, false);
    }

    public static AssetPackageReader b(File file, String str) throws IOException {
        return new AssetPackageReader(new e(file), str, false);
    }

    public static AssetPackageReader a(AssetManager assetManager, String str, String str2) throws IOException {
        return new AssetPackageReader(new b(assetManager, str), str2, false);
    }

    public static AssetPackageReader a(Context context, String str, String str2) throws IOException {
        AssetPackageReader assetPackageReader;
        AssetPackageReader assetPackageReader2;
        a.b();
        WeakReference<AssetPackageReader> weakReference = h.get(str);
        if (weakReference == null || (assetPackageReader2 = weakReference.get()) == null) {
            String substring = str.substring(str.indexOf(58) + 1);
            if (str.startsWith("assets:")) {
                assetPackageReader = new AssetPackageReader(new b(context.getApplicationContext().getAssets(), substring), str2, true);
            } else if (str.startsWith("file:")) {
                assetPackageReader = new AssetPackageReader(new e(new File(substring)), str2, true);
            } else if (str.startsWith("zipfile:")) {
                assetPackageReader = new AssetPackageReader(new g(new File(substring)), str2, true);
            } else {
                throw new PackageReaderException();
            }
            h.put(str, new a(assetPackageReader));
            return assetPackageReader;
        }
        return assetPackageReader2;
    }

    private AssetPackageReader(c cVar, String str, boolean z) throws IOException {
        Log.d("AssetPackageReader", "NEW APR Instance (Container:" + cVar.getClass().getSimpleName() + ") baseId=" + str + " shared=" + z);
        this.a = cVar;
        this.e = str;
        this.f = z;
        EncryptionInfoJSON h2 = h();
        this.d = h2;
        com.nexstreaming.app.common.nexasset.assetpackage.security.a a2 = a(h2);
        this.j = a2;
        if (a2 != null) {
            this.a = new d(cVar, a2);
        }
        this.c = i();
    }

    public int b() {
        return this.c.packageContentVersion;
    }

    public int c() {
        return this.c.minVersionCode;
    }

    public List<com.nexstreaming.app.common.nexasset.assetpackage.f> d() throws IOException {
        j();
        return this.g;
    }

    private EncryptionInfoJSON h() throws IOException {
        InputStream inputStream;
        Throwable th;
        try {
            inputStream = this.a.a("e.json");
        } catch (FileNotFoundException unused) {
            inputStream = null;
        } catch (Throwable th2) {
            inputStream = null;
            th = th2;
        }
        try {
            EncryptionInfoJSON encryptionInfoJSON = (EncryptionInfoJSON) this.b.fromJson((Reader) new InputStreamReader(inputStream), (Class<Object>) EncryptionInfoJSON.class);
            Log.d("AssetPackageReader", "Parse e.json file! : " + encryptionInfoJSON.provider.toString() + " / " + encryptionInfoJSON.psd.toString());
            com.nexstreaming.app.common.util.b.a(inputStream);
            return encryptionInfoJSON;
        } catch (FileNotFoundException unused2) {
            com.nexstreaming.app.common.util.b.a(inputStream);
            return null;
        } catch (Throwable th3) {
            th = th3;
            com.nexstreaming.app.common.util.b.a(inputStream);
            throw th;
        }
    }

    private com.nexstreaming.app.common.nexasset.assetpackage.security.a a(EncryptionInfoJSON encryptionInfoJSON) throws PackageReaderException {
        String str;
        if (encryptionInfoJSON == null || (str = encryptionInfoJSON.provider) == null || str.length() <= 0) {
            return null;
        }
        com.nexstreaming.app.common.nexasset.assetpackage.security.b bVar = i.get(encryptionInfoJSON.provider);
        if (bVar == null) {
            throw new PackageReaderException(this, "invalid provider");
        }
        return bVar.a(encryptionInfoJSON.psd);
    }

    private PackageInfoJSON i() throws IOException {
        Log.d("AssetPackageReader", "readPackageInfo IN");
        try {
            InputStream a2 = this.a.a("packageinfo.json");
            PackageInfoJSON packageInfoJSON = (PackageInfoJSON) this.b.fromJson((Reader) new InputStreamReader(a2), (Class<Object>) PackageInfoJSON.class);
            Log.d("AssetPackageReader", "readPackageInfo(), asset name: " + packageInfoJSON.assetName);
            Map<String, String> map = packageInfoJSON.assetName;
            if (map != null && map.size() > 0) {
                Set<String> keySet = packageInfoJSON.assetName.keySet();
                HashMap hashMap = new HashMap();
                for (String str : keySet) {
                    hashMap.put(str.toLowerCase(Locale.ENGLISH), packageInfoJSON.assetName.get(str));
                }
                packageInfoJSON.assetName.clear();
                packageInfoJSON.assetName.putAll(hashMap);
            }
            a2.close();
            if (packageInfoJSON.minVersionCode > 6) {
                Log.w("AssetPackageReader", "Unsupported package format version: " + packageInfoJSON.minVersionCode);
                throw new PackageReaderException(this, "Unsupported package format version");
            }
            String str2 = packageInfoJSON.format;
            if (str2 == null) {
                Log.w("AssetPackageReader", "Missing package format");
                throw new PackageReaderException(this, "Missing package format");
            } else if (str2.equals("com.kinemaster.assetpackage")) {
                Log.d("AssetPackageReader", "readPackageInfo OUT");
                return packageInfoJSON;
            } else {
                Log.w("AssetPackageReader", "Unsupported package format: " + packageInfoJSON.format);
                throw new PackageReaderException(this, "Unsupported package format: " + packageInfoJSON.format);
            }
        } catch (JsonSyntaxException e2) {
            Log.w("AssetPackageReader", "PackageInfoJSON file : packageinfo.json", e2);
            throw new PackageReaderException(this, "PackageInfoJSON file: packageinfo.json", e2);
        } catch (FileNotFoundException e3) {
            Log.w("AssetPackageReader", "Package missing file: packageinfo.json", e3);
            throw new PackageReaderException(this, "Package missing file: packageinfo.json", e3);
        }
    }

    public Map<String, String> e() {
        return this.c.assetName;
    }

    private void j() throws IOException {
        com.nexstreaming.app.common.nexasset.assetpackage.f e2;
        com.nexstreaming.app.common.nexasset.assetpackage.f e3;
        if (this.g != null) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        List<String> list = this.c.itemRoots;
        if (list != null && list.size() > 0) {
            Log.d("AssetPackageReader", "makeItemList: using root index");
            for (String str : this.c.itemRoots) {
                if (str != null && (e3 = e(b(str, "_info.json"))) != null) {
                    arrayList.add(e3);
                }
            }
        } else {
            Log.d("AssetPackageReader", "makeItemList: no root index; scanning entire package");
            for (String str2 : this.a.a()) {
                if (str2 != null && (e2 = e(str2)) != null) {
                    arrayList.add(e2);
                }
            }
        }
        this.g = arrayList;
    }

    private com.nexstreaming.app.common.nexasset.assetpackage.f e(String str) throws IOException {
        ItemCategory itemCategory;
        Map<String, String> map;
        if (!str.endsWith("/_info.json")) {
            return null;
        }
        int indexOf = str.indexOf(47);
        int i2 = indexOf + 1;
        int indexOf2 = str.indexOf(47, i2);
        int i3 = indexOf2 + 1;
        int indexOf3 = str.indexOf(47, i3);
        if (indexOf == -1 || indexOf2 == -1 || indexOf3 != -1) {
            Log.w("AssetPackageReader", "Malformed path");
            return null;
        } else if (str.startsWith("merge/")) {
            return null;
        } else {
            ItemCategory[] values = ItemCategory.values();
            int length = values.length;
            int i4 = 0;
            while (true) {
                if (i4 >= length) {
                    itemCategory = null;
                    break;
                }
                itemCategory = values[i4];
                String name = itemCategory.name();
                if (name.length() == indexOf && str.startsWith(name)) {
                    break;
                }
                i4++;
            }
            if (itemCategory == null) {
                Log.w("AssetPackageReader", "Unrecognized item category");
                return null;
            }
            String substring = str.substring(i2, indexOf2);
            String substring2 = str.substring(0, i3);
            try {
                InputStream a2 = this.a.a(str);
                try {
                    ItemInfoJSON itemInfoJSON = (ItemInfoJSON) this.b.fromJson((Reader) new InputStreamReader(a2), (Class<Object>) ItemInfoJSON.class);
                    if (itemInfoJSON != null && (map = itemInfoJSON.label) != null && map.size() > 0) {
                        Set<String> keySet = itemInfoJSON.label.keySet();
                        HashMap hashMap = new HashMap();
                        for (String str2 : keySet) {
                            hashMap.put(str2.toLowerCase(Locale.ENGLISH), itemInfoJSON.label.get(str2));
                        }
                        itemInfoJSON.label.clear();
                        itemInfoJSON.label.putAll(hashMap);
                    }
                    if (itemInfoJSON.filename == null) {
                        throw new PackageReaderException(this, "Missing base file for: " + str);
                    }
                    if (itemInfoJSON.icon == null) {
                        itemInfoJSON.icon = "_icon.svg";
                    }
                    if (itemInfoJSON.thumbnail == null) {
                        itemInfoJSON.thumbnail = "_thumb.jpeg";
                    }
                    if (itemInfoJSON.id == null) {
                        itemInfoJSON.id = this.e + ".items." + substring;
                    }
                    ItemType fromId = ItemType.fromId(itemInfoJSON.type);
                    if (fromId == null) {
                        throw new PackageReaderException(this, "Unrecognized item type '" + itemInfoJSON.type + "' for: " + str);
                    }
                    f fVar = new f();
                    fVar.a = this.a.getClass();
                    fVar.b = this.a.c();
                    fVar.c = b(substring2, itemInfoJSON.filename);
                    fVar.d = b(substring2, itemInfoJSON.icon);
                    fVar.e = b(substring2, itemInfoJSON.thumbnail);
                    fVar.f = itemInfoJSON.id;
                    fVar.h = itemInfoJSON.label;
                    fVar.i = fromId;
                    fVar.j = itemCategory;
                    fVar.g = itemInfoJSON.sampleText;
                    fVar.k = itemInfoJSON.hidden;
                    return fVar;
                } finally {
                    a2.close();
                }
            } catch (JsonSyntaxException e2) {
                throw new PackageReaderException(this, "JSON Syntax Error in: " + str, e2);
            } catch (FileNotFoundException e3) {
                Log.w("AssetPackageReader", "Item in index but missing in package", e3);
                return null;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String b(String str, String str2) {
        if (str2.startsWith(CallerDataConverter.DEFAULT_RANGE_DELIMITER) || str2.contains("/..")) {
            throw new SecurityException("Parent Path References Not Allowed");
        }
        if (str.endsWith(com.xiaomi.stat.b.h.g)) {
            return str + str2;
        }
        return str + com.xiaomi.stat.b.h.g + str2;
    }

    public InputStream a(String str) throws FileNotFoundException, IOException {
        InputStream inputStream;
        ItemInfoJSON itemInfoJSON;
        String str2;
        Map<String, String> map = this.k;
        if (map != null && (str2 = map.get(str)) != null && str2.length() > 0) {
            return this.a.a(str2);
        }
        try {
            return this.a.a(str);
        } catch (FileNotFoundException e2) {
            String d2 = com.nexstreaming.app.common.util.i.d(str);
            InputStream inputStream2 = null;
            ItemInfoJSON itemInfoJSON2 = null;
            while (true) {
                if (d2 == null) {
                    break;
                }
                try {
                    inputStream = this.a.a(com.nexstreaming.app.common.util.i.b(d2, "_info.json"));
                    try {
                        itemInfoJSON = (ItemInfoJSON) this.b.fromJson((Reader) new InputStreamReader(inputStream), (Class<Object>) ItemInfoJSON.class);
                    } catch (FileNotFoundException unused) {
                        com.nexstreaming.app.common.util.b.a(inputStream);
                        d2 = com.nexstreaming.app.common.util.i.b(d2);
                    } catch (Throwable th) {
                        th = th;
                        inputStream2 = inputStream;
                        com.nexstreaming.app.common.util.b.a(inputStream2);
                        throw th;
                    }
                } catch (FileNotFoundException unused2) {
                    inputStream = null;
                } catch (Throwable th2) {
                    th = th2;
                }
                if (itemInfoJSON != null) {
                    com.nexstreaming.app.common.util.b.a(inputStream);
                    itemInfoJSON2 = itemInfoJSON;
                    break;
                }
                com.nexstreaming.app.common.util.b.a(inputStream);
                itemInfoJSON2 = itemInfoJSON;
                d2 = com.nexstreaming.app.common.util.i.b(d2);
            }
            if (itemInfoJSON2 != null && itemInfoJSON2.mergePaths != null) {
                String substring = str.substring(d2.length(), str.length());
                for (String str3 : itemInfoJSON2.mergePaths) {
                    if (str3 != null) {
                        String a2 = com.nexstreaming.app.common.util.i.a("merge", str3.trim());
                        if (!a2.endsWith(com.xiaomi.stat.b.h.g)) {
                            a2 = a2 + com.xiaomi.stat.b.h.g;
                        }
                        if (a2.length() > 0) {
                            String b2 = com.nexstreaming.app.common.util.i.b(a2, substring);
                            try {
                                InputStream a3 = this.a.a(b2);
                                if (this.k == null) {
                                    this.k = new HashMap();
                                }
                                this.k.put(str, b2);
                                return a3;
                            } catch (FileNotFoundException unused3) {
                                continue;
                            }
                        } else {
                            continue;
                        }
                    }
                }
            }
            throw e2;
        }
    }

    public String b(String str) {
        InputStream inputStream;
        ItemInfoJSON itemInfoJSON;
        String str2;
        Map<String, String> map = this.l;
        if (map == null || (str2 = map.get(str)) == null || str2.length() <= 0) {
            String d2 = com.nexstreaming.app.common.util.i.d(str);
            InputStream inputStream2 = null;
            ItemInfoJSON itemInfoJSON2 = null;
            while (true) {
                if (d2 == null) {
                    break;
                }
                try {
                    inputStream = this.a.a(com.nexstreaming.app.common.util.i.b(d2, "_info.json"));
                    try {
                        itemInfoJSON = (ItemInfoJSON) this.b.fromJson((Reader) new InputStreamReader(inputStream), (Class<Object>) ItemInfoJSON.class);
                    } catch (FileNotFoundException | IOException unused) {
                        com.nexstreaming.app.common.util.b.a(inputStream);
                        d2 = com.nexstreaming.app.common.util.i.b(d2);
                    } catch (Throwable th) {
                        th = th;
                        inputStream2 = inputStream;
                        com.nexstreaming.app.common.util.b.a(inputStream2);
                        throw th;
                    }
                } catch (FileNotFoundException | IOException unused2) {
                    inputStream = null;
                } catch (Throwable th2) {
                    th = th2;
                }
                if (itemInfoJSON != null) {
                    com.nexstreaming.app.common.util.b.a(inputStream);
                    itemInfoJSON2 = itemInfoJSON;
                    break;
                }
                com.nexstreaming.app.common.util.b.a(inputStream);
                itemInfoJSON2 = itemInfoJSON;
                d2 = com.nexstreaming.app.common.util.i.b(d2);
            }
            if (itemInfoJSON2 != null && itemInfoJSON2.mergePaths != null) {
                String substring = str.substring(d2.length(), str.length());
                for (String str3 : itemInfoJSON2.mergePaths) {
                    if (str3 != null) {
                        String a2 = com.nexstreaming.app.common.util.i.a("merge", str3.trim());
                        if (!a2.endsWith(com.xiaomi.stat.b.h.g)) {
                            a2 = a2 + com.xiaomi.stat.b.h.g;
                        }
                        if (a2.length() > 0) {
                            String b2 = com.nexstreaming.app.common.util.i.b(a2, substring);
                            if (this.l == null) {
                                this.l = new HashMap();
                            }
                            Log.d("AssetPackageReader", "getFilePath mergePath : " + b2);
                            this.l.put(str, b2);
                            return b2;
                        }
                    }
                }
            }
            return null;
        }
        return str2;
    }

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.f) {
            return;
        }
        this.a.b();
    }

    public String f() {
        return this.a.c();
    }

    public File g() {
        return this.a.d();
    }

    /* loaded from: classes3.dex */
    public static class EncryptionInfoJSON {
        public String provider;
        public String psd;

        private EncryptionInfoJSON() {
        }
    }

    /* loaded from: classes3.dex */
    public static class PackageInfoJSON {
        public Map<String, String> assetName;
        public String format;
        public List<String> itemRoots;
        public int minVersionCode;
        public int packageContentVersion;
        public int targetVersionCode;

        private PackageInfoJSON() {
        }
    }

    /* loaded from: classes3.dex */
    public static class ItemInfoJSON {
        public String filename;
        public boolean hidden;
        public String icon;
        public String id;
        public Map<String, String> label;
        public List<String> mergePaths;
        public String sampleText;
        public String thumbnail;
        public String type;

        private ItemInfoJSON() {
        }
    }

    /* loaded from: classes3.dex */
    public static class f implements com.nexstreaming.app.common.nexasset.assetpackage.f {
        public Class<? extends c> a;
        public String b;
        public String c;
        public String d;
        public String e;
        public String f;
        public String g;
        public Map<String, String> h;
        public ItemType i;
        public ItemCategory j;
        public boolean k;

        private f() {
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.f
        public String getId() {
            return this.f;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.f
        public String getPackageURI() {
            return this.b;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.f
        public String getFilePath() {
            return this.c;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.f
        public String getIconPath() {
            return this.d;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.f
        public String getThumbPath() {
            return this.e;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.f
        public Map<String, String> getLabel() {
            return this.h;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.f
        public String getSampleText() {
            return this.g;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.f
        public ItemType getType() {
            return this.i;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.f
        public ItemCategory getCategory() {
            return this.j;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.f
        public com.nexstreaming.app.common.nexasset.assetpackage.b getAssetPackage() {
            throw new UnsupportedOperationException();
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.f
        public boolean isHidden() {
            return this.k;
        }
    }

    /* loaded from: classes3.dex */
    public static class EncryptedException extends IOException {
        public EncryptedException() {
        }

        public EncryptedException(String str) {
            super(str);
        }

        public EncryptedException(String str, Throwable th) {
            super(str, th);
        }

        public EncryptedException(Throwable th) {
            super(th);
        }
    }

    /* loaded from: classes3.dex */
    public static class LocalPathNotAvailableException extends IOException {
        public LocalPathNotAvailableException() {
        }

        public LocalPathNotAvailableException(String str) {
            super(str);
        }

        public LocalPathNotAvailableException(String str, Throwable th) {
            super(str, th);
        }

        public LocalPathNotAvailableException(Throwable th) {
            super(th);
        }
    }

    /* loaded from: classes3.dex */
    public static class g implements c {
        private final ZipFile a;
        private final File b;

        public g(File file) throws IOException {
            this.a = new ZipFile(file);
            this.b = file;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.c
        public InputStream a(String str) throws FileNotFoundException, IOException {
            ZipEntry entry = this.a.getEntry(str);
            if (entry == null && (entry = this.a.getEntry(com.nexstreaming.app.common.util.i.a(com.nexstreaming.app.common.util.i.d(str), com.nexstreaming.app.common.util.i.c(str).toLowerCase(Locale.ENGLISH)))) == null) {
                throw new FileNotFoundException("File '" + str + "' not found in '" + this.a.getName() + "'");
            }
            return this.a.getInputStream(entry);
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.c
        public Iterable<String> a() {
            return new Iterable<String>() { // from class: com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.g.1
                @Override // java.lang.Iterable
                public Iterator<String> iterator() {
                    final Enumeration<? extends ZipEntry> entries = g.this.a.entries();
                    return new Iterator<String>() { // from class: com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.g.1.1
                        @Override // java.util.Iterator
                        public boolean hasNext() {
                            return entries.hasMoreElements();
                        }

                        @Override // java.util.Iterator
                        /* renamed from: a */
                        public String next() {
                            return ((ZipEntry) entries.nextElement()).getName();
                        }

                        @Override // java.util.Iterator
                        public void remove() {
                            throw new UnsupportedOperationException();
                        }
                    };
                }
            };
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.c
        public void b() throws IOException {
            this.a.close();
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.c
        public String c() {
            return "zipfile:" + this.a.getName();
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.c
        public File b(String str) throws LocalPathNotAvailableException, FileNotFoundException, IOException {
            throw new LocalPathNotAvailableException();
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.c
        public Typeface c(String str) throws LocalPathNotAvailableException {
            throw new LocalPathNotAvailableException();
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.c
        public File d() {
            return this.b;
        }
    }

    public File c(String str) throws IOException, LocalPathNotAvailableException {
        return this.a.b(str);
    }

    public Typeface d(String str) throws LocalPathNotAvailableException {
        return this.a.c(str);
    }

    /* loaded from: classes3.dex */
    public static class e implements c {
        private final File a;

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.c
        public void b() throws IOException {
        }

        private e(File file) {
            this.a = file;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.c
        public InputStream a(String str) throws FileNotFoundException, IOException {
            return new FileInputStream(d(str));
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.c
        public File b(String str) throws LocalPathNotAvailableException, FileNotFoundException, IOException {
            return d(str);
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.c
        public Typeface c(String str) throws LocalPathNotAvailableException {
            return Typeface.createFromFile(d(str));
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.c
        public File d() {
            return this.a;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public String a(File file) {
            String absolutePath = file.getAbsolutePath();
            String absolutePath2 = this.a.getAbsolutePath();
            if (!absolutePath.startsWith(absolutePath2)) {
                throw new IllegalStateException();
            }
            if (absolutePath.length() <= absolutePath2.length()) {
                return "";
            }
            if (absolutePath.charAt(absolutePath2.length()) == '/') {
                return absolutePath.substring(absolutePath2.length() + 1);
            }
            return absolutePath.substring(absolutePath2.length());
        }

        private File d(String str) {
            return new File(this.a, str);
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.c
        public Iterable<String> a() {
            return new Iterable<String>() { // from class: com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.e.1
                @Override // java.lang.Iterable
                public Iterator<String> iterator() {
                    final ArrayList arrayList = new ArrayList();
                    File[] listFiles = e.this.a.listFiles();
                    if (listFiles != null) {
                        for (File file : listFiles) {
                            arrayList.add(file);
                        }
                    }
                    return new Iterator<String>() { // from class: com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.e.1.1
                        @Override // java.util.Iterator
                        public void remove() {
                        }

                        @Override // java.util.Iterator
                        public boolean hasNext() {
                            return !arrayList.isEmpty();
                        }

                        @Override // java.util.Iterator
                        /* renamed from: a */
                        public String next() {
                            File[] listFiles2;
                            File file2 = (File) arrayList.remove(0);
                            if (file2.isDirectory() && (listFiles2 = file2.listFiles()) != null) {
                                for (File file3 : listFiles2) {
                                    arrayList.add(file3);
                                }
                            }
                            return e.this.a(file2);
                        }
                    };
                }
            };
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.c
        public String c() {
            return "file:" + this.a.getAbsolutePath();
        }
    }

    /* loaded from: classes3.dex */
    public static class d implements c {
        private final c a;
        private final com.nexstreaming.app.common.nexasset.assetpackage.security.a b;

        private d(c cVar, com.nexstreaming.app.common.nexasset.assetpackage.security.a aVar) {
            this.a = cVar;
            this.b = aVar;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.c
        public InputStream a(String str) throws FileNotFoundException, IOException {
            return this.b.a(this.a.a(str), str);
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.c
        public Iterable<String> a() {
            return this.a.a();
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.c
        public void b() throws IOException {
            this.a.b();
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.c
        public String c() {
            return this.a.c();
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.c
        public File b(String str) throws LocalPathNotAvailableException, EncryptedException, FileNotFoundException, IOException {
            if (!this.b.a(str)) {
                throw new EncryptedException();
            }
            return this.a.b(str);
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.c
        public Typeface c(String str) throws LocalPathNotAvailableException {
            if (!this.b.a(str)) {
                throw new LocalPathNotAvailableException();
            }
            return this.a.c(str);
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.c
        public File d() {
            return this.a.d();
        }
    }

    /* loaded from: classes3.dex */
    public static class b implements c {
        private final String a;
        private AssetManager b;

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.c
        public File d() {
            return null;
        }

        private b(AssetManager assetManager, String str) {
            if (assetManager != null) {
                if (str == null) {
                    throw new IllegalArgumentException();
                }
                this.a = str;
                this.b = assetManager;
                Log.d("AssetPackageReader", "Created ACR:" + String.valueOf(this));
                return;
            }
            throw new IllegalArgumentException();
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.c
        public InputStream a(String str) throws FileNotFoundException, IOException {
            Log.d("AssetPackageReader", "openFile:" + String.valueOf(this));
            return this.b.open(AssetPackageReader.b(this.a, str));
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.c
        public Iterable<String> a() {
            return new Iterable<String>() { // from class: com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.b.1
                @Override // java.lang.Iterable
                public Iterator<String> iterator() {
                    String[] strArr;
                    final ArrayList arrayList = new ArrayList();
                    try {
                        strArr = b.this.b.list(b.this.a);
                    } catch (IOException e) {
                        e.printStackTrace();
                        strArr = null;
                    }
                    if (strArr != null) {
                        for (String str : strArr) {
                            arrayList.add(AssetPackageReader.b(b.this.a, str));
                        }
                    }
                    return new Iterator<String>() { // from class: com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.b.1.1
                        @Override // java.util.Iterator
                        public void remove() {
                        }

                        @Override // java.util.Iterator
                        public boolean hasNext() {
                            return !arrayList.isEmpty();
                        }

                        @Override // java.util.Iterator
                        /* renamed from: a */
                        public String next() {
                            int i = 0;
                            String str2 = (String) arrayList.remove(0);
                            Log.d("AssetPackageReader", "iter:next -> " + str2 + " (todo list size: " + arrayList.size() + ")");
                            String[] strArr2 = null;
                            try {
                                strArr2 = b.this.b.list(str2);
                                Log.d("AssetPackageReader", "Has " + strArr2.length + " children.");
                            } catch (IOException unused) {
                                Log.d("AssetPackageReader", "Has no children.");
                            }
                            if (strArr2 != null && strArr2.length > 0) {
                                for (String str3 : strArr2) {
                                    arrayList.add(AssetPackageReader.b(str2, str3));
                                }
                                Log.d("AssetPackageReader", "Added " + strArr2.length + " children; todo list size: " + arrayList.size());
                            }
                            int length = b.this.a.length();
                            if (length > 0) {
                                i = length + 1;
                            }
                            return str2.substring(i);
                        }
                    };
                }
            };
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.c
        public void b() throws IOException {
            this.b = null;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.c
        public String c() {
            return "assets:" + this.a;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.c
        public File b(String str) throws LocalPathNotAvailableException, FileNotFoundException, IOException {
            throw new LocalPathNotAvailableException();
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader.c
        public Typeface c(String str) throws LocalPathNotAvailableException {
            return Typeface.createFromAsset(this.b, AssetPackageReader.b(this.a, str));
        }
    }

    /* loaded from: classes3.dex */
    public static class PackageReaderException extends IOException {
        public PackageReaderException() {
        }

        public PackageReaderException(AssetPackageReader assetPackageReader, String str) {
            super(str + " (in package '" + assetPackageReader.f() + "' via " + assetPackageReader.getClass().getSimpleName() + ")");
        }

        public PackageReaderException(AssetPackageReader assetPackageReader, String str, Throwable th) {
            super(str + " (in package '" + assetPackageReader.f() + "' via " + assetPackageReader.getClass().getSimpleName() + ")", th);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public c k() {
        return this.a;
    }
}
