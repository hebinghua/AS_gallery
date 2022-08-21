package com.nexstreaming.app.common.nexasset.assetpackage;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteFullException;
import android.graphics.Typeface;
import android.util.Log;
import ch.qos.logback.classic.pattern.CallerDataConverter;
import com.nexstreaming.app.common.nexasset.assetpackage.db.AssetPackageDb;
import com.nexstreaming.app.common.nexasset.assetpackage.db.AssetPackageRecord;
import com.nexstreaming.app.common.nexasset.assetpackage.db.CategoryRecord;
import com.nexstreaming.app.common.nexasset.assetpackage.db.InstallSourceRecord;
import com.nexstreaming.app.common.nexasset.assetpackage.db.ItemRecord;
import com.nexstreaming.app.common.nexasset.assetpackage.db.SubCategoryRecord;
import com.nexstreaming.app.common.nexasset.store.AbstractStoreAssetInfo;
import com.nexstreaming.app.common.nexasset.store.StoreAssetInfo;
import com.nexstreaming.app.common.util.m;
import com.nexstreaming.kminternal.kinemaster.fonts.Font;
import com.nexstreaming.kminternal.nexvideoeditor.NexEditor;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* compiled from: AssetPackageManager.java */
/* loaded from: classes3.dex */
public class c {
    private static c a;
    private final AssetPackageDb b;
    private InstallSourceRecord c = null;

    public static c a(Context context) {
        if (a == null) {
            a = new c(context.getApplicationContext());
        }
        return a;
    }

    public static c a() {
        if (a == null) {
            a = new c(com.nexstreaming.kminternal.kinemaster.config.a.a().b());
        }
        return a;
    }

    private c(Context context) {
        this.b = new AssetPackageDb(context);
    }

    public void a(File file, File file2, StoreAssetInfo storeAssetInfo) throws IOException {
        AssetPackageReader a2;
        if (file.isDirectory()) {
            a2 = AssetPackageReader.b(file, storeAssetInfo.getAssetId());
        } else {
            a2 = AssetPackageReader.a(file, storeAssetInfo.getAssetId());
        }
        a(a2, file2, storeAssetInfo, f());
    }

    private InstallSourceRecord f() {
        InstallSourceRecord installSourceRecord = this.c;
        if (installSourceRecord != null) {
            return installSourceRecord;
        }
        InstallSourceRecord installSourceRecord2 = (InstallSourceRecord) this.b.findFirst(InstallSourceRecord.class, "install_source_id = ?", "store");
        if (installSourceRecord2 == null) {
            installSourceRecord2 = new InstallSourceRecord();
            installSourceRecord2.installSourceId = "store";
            installSourceRecord2.installSourceType = InstallSourceType.STORE;
            installSourceRecord2.installSourceVersion = 0L;
            this.b.add(installSourceRecord2);
        }
        this.c = installSourceRecord2;
        return installSourceRecord2;
    }

    /* compiled from: AssetPackageManager.java */
    /* renamed from: com.nexstreaming.app.common.nexasset.assetpackage.c$5  reason: invalid class name */
    /* loaded from: classes3.dex */
    public static /* synthetic */ class AnonymousClass5 {
        public static final /* synthetic */ int[] a;
        public static final /* synthetic */ int[] b;

        static {
            int[] iArr = new int[InstallSourceType.values().length];
            b = iArr;
            try {
                iArr[InstallSourceType.STORE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                b[InstallSourceType.APP_ASSETS.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                b[InstallSourceType.FOLDER.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            int[] iArr2 = new int[ItemType.values().length];
            a = iArr2;
            try {
                iArr2[ItemType.audio.ordinal()] = 1;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                a[ItemType.font.ordinal()] = 2;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    private InstallSourceRecord a(InstallSourceType installSourceType, String str) {
        String str2;
        int i = AnonymousClass5.b[installSourceType.ordinal()];
        if (i != 1) {
            if (i == 2) {
                str2 = "assets:" + str;
            } else if (i == 3) {
                str2 = "file:" + str;
            } else {
                throw new IllegalArgumentException();
            }
            InstallSourceRecord installSourceRecord = (InstallSourceRecord) this.b.findFirst(InstallSourceRecord.class, "install_source_id = ?", str2);
            if (installSourceRecord != null) {
                return installSourceRecord;
            }
            InstallSourceRecord installSourceRecord2 = new InstallSourceRecord();
            installSourceRecord2.installSourceId = str2;
            installSourceRecord2.installSourceType = installSourceType;
            installSourceRecord2.installSourceVersion = 0L;
            this.b.add(installSourceRecord2);
            return installSourceRecord2;
        }
        return f();
    }

    private void a(AssetPackageReader assetPackageReader, File file, StoreAssetInfo storeAssetInfo, InstallSourceRecord installSourceRecord) throws IOException {
        CategoryRecord categoryRecord;
        SubCategoryRecord subCategoryRecord;
        this.b.beginTransaction();
        try {
            int i = 0;
            String str = null;
            if (storeAssetInfo.getCategoryIndex() != 0) {
                categoryRecord = (CategoryRecord) this.b.findFirst(CategoryRecord.class, "category_id = ?", Integer.valueOf(storeAssetInfo.getCategoryIndex()));
                if (categoryRecord == null) {
                    categoryRecord = new CategoryRecord();
                    categoryRecord.categoryId = storeAssetInfo.getCategoryIndex();
                    categoryRecord.categoryName = storeAssetInfo.getCategoryAliasName();
                    categoryRecord.categoryIconURL = storeAssetInfo.getCategoryIconURL();
                    this.b.addOrUpdate(categoryRecord);
                } else if (categoryRecord.categoryIconURL != null && storeAssetInfo.getCategoryIconURL() != null && !categoryRecord.categoryIconURL.equals(storeAssetInfo.getCategoryIconURL())) {
                    categoryRecord.categoryIconURL = storeAssetInfo.getCategoryIconURL();
                    this.b.update(categoryRecord);
                }
            } else {
                categoryRecord = null;
            }
            if (storeAssetInfo.getSubCategoryIndex() != 0) {
                subCategoryRecord = (SubCategoryRecord) this.b.findFirst(SubCategoryRecord.class, "sub_category_id = ?", Integer.valueOf(storeAssetInfo.getSubCategoryIndex()));
                if (subCategoryRecord == null) {
                    subCategoryRecord = new SubCategoryRecord();
                    subCategoryRecord.subCategoryId = storeAssetInfo.getSubCategoryIndex();
                    subCategoryRecord.subCategoryName = storeAssetInfo.getSubCategoryNameMap();
                    subCategoryRecord.subCategoryAlias = storeAssetInfo.getSubCategoryAliasName();
                    this.b.add(subCategoryRecord);
                } else {
                    subCategoryRecord.subCategoryName = storeAssetInfo.getSubCategoryNameMap();
                    this.b.update(subCategoryRecord);
                }
            } else {
                subCategoryRecord = (SubCategoryRecord) this.b.findFirst(SubCategoryRecord.class, "sub_category_id = ?", -1L);
                if (subCategoryRecord == null) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("en", "Local");
                    SubCategoryRecord subCategoryRecord2 = new SubCategoryRecord();
                    subCategoryRecord2.subCategoryId = -1L;
                    subCategoryRecord2.subCategoryAlias = "local";
                    subCategoryRecord2.subCategoryName = hashMap;
                    this.b.add(subCategoryRecord2);
                    subCategoryRecord = subCategoryRecord2;
                }
            }
            AssetPackageRecord assetPackageRecord = new AssetPackageRecord();
            assetPackageRecord.assetIdx = storeAssetInfo.getAssetIndex();
            assetPackageRecord.packageURI = assetPackageReader.f();
            assetPackageRecord.assetId = storeAssetInfo.getAssetId();
            assetPackageRecord.assetUrl = storeAssetInfo.getAssetPackageDownloadURL();
            assetPackageRecord.thumbPath = file == null ? null : file.getAbsolutePath();
            assetPackageRecord.assetDesc = null;
            Map<String, String> assetNameMap = storeAssetInfo.getAssetNameMap();
            assetPackageRecord.assetName = assetNameMap;
            if (assetNameMap.size() < 1) {
                assetPackageRecord.assetName = assetPackageReader.e();
            }
            assetPackageRecord.priceType = storeAssetInfo.getPriceType();
            assetPackageRecord.thumbUrl = storeAssetInfo.getAssetThumbnailURL();
            assetPackageRecord.installSource = installSourceRecord;
            assetPackageRecord.category = categoryRecord;
            assetPackageRecord.subCategory = subCategoryRecord;
            File g = assetPackageReader.g();
            if (g != null) {
                str = g.getAbsolutePath();
            }
            assetPackageRecord.localPath = str;
            assetPackageRecord.expireTime = storeAssetInfo.getExpireTime();
            assetPackageRecord.installedTime = System.currentTimeMillis();
            assetPackageRecord.minVersion = storeAssetInfo.getAssetScopeVersion();
            assetPackageRecord.packageVersion = storeAssetInfo.getAssetVersion();
            this.b.add(assetPackageRecord);
            for (f fVar : assetPackageReader.d()) {
                ItemRecord itemRecord = new ItemRecord();
                itemRecord.assetPackageRecord = assetPackageRecord;
                itemRecord.itemId = fVar.getId();
                itemRecord.packageURI = fVar.getPackageURI();
                itemRecord.filePath = fVar.getFilePath();
                itemRecord.iconPath = fVar.getIconPath();
                itemRecord.hidden = fVar.isHidden();
                itemRecord.thumbPath = fVar.getThumbPath();
                itemRecord.label = fVar.getLabel();
                itemRecord.itemType = fVar.getType();
                itemRecord.itemCategory = fVar.getCategory();
                itemRecord.sampleText = fVar.getSampleText();
                this.b.add(itemRecord);
                i++;
            }
            assetPackageReader.close();
            this.b.setTransactionSuccessful();
            Log.d("AssetPackageManager", "Added DB Record for: " + assetPackageRecord.assetId + " and " + i + " items.");
            try {
                this.b.endTransaction();
            } catch (SQLiteFullException e) {
                Log.d("AssetPackageManager", "AssetPackageDb.endTransaction() throws SQLiteFullException. e=" + e);
                throw new IOException(e);
            }
        } catch (Throwable th) {
            try {
                this.b.endTransaction();
                throw th;
            } catch (SQLiteFullException e2) {
                Log.d("AssetPackageManager", "AssetPackageDb.endTransaction() throws SQLiteFullException. e=" + e2);
                throw new IOException(e2);
            }
        }
    }

    public void a(Context context, String str, long j) throws IOException {
        String[] list;
        String[] strArr;
        Log.d("AssetPackageManager", "syncPackagesFromAndroidAssets - IN : " + str);
        InstallSourceRecord a2 = a(InstallSourceType.APP_ASSETS, str);
        Log.d("AssetPackageManager", "syncPackagesFromAndroidAssets - ISR CHECK: " + a2.installSourceVersion + " / " + j);
        if (a2.installSourceVersion != j) {
            Log.d("AssetPackageManager", "syncPackagesFromAndroidAssets - ISR MISMATCH; UPDATING : " + a2.installSourceVersion + " -> " + j);
            a2.installSourceVersion = 0L;
            this.b.update(a2);
            a(a2);
            AssetManager assets = context.getAssets();
            for (final String str2 : assets.list(str)) {
                String b = b(str, str2);
                try {
                    strArr = assets.list(b);
                } catch (IOException unused) {
                    strArr = null;
                }
                if (strArr != null && strArr.length > 0) {
                    Log.d("AssetPackageManager", "syncPackagesFromAndroidAssets - Processing package: " + str2 + " (in " + b + ")");
                    AssetPackageReader a3 = AssetPackageReader.a(assets, b, str2);
                    a(a3, (File) null, new AbstractStoreAssetInfo(a3.c(), a3.b()) { // from class: com.nexstreaming.app.common.nexasset.assetpackage.c.1
                        @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
                        public String getSubCategoryAliasName() {
                            return "local";
                        }

                        @Override // com.nexstreaming.app.common.nexasset.store.AbstractStoreAssetInfo, com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
                        public String getAssetId() {
                            return str2;
                        }

                        @Override // com.nexstreaming.app.common.nexasset.store.AbstractStoreAssetInfo, com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
                        public String getAssetTitle() {
                            return str2;
                        }
                    }, a2);
                }
            }
            a2.installSourceVersion = j;
            this.b.update(a2);
        }
        Log.d("AssetPackageManager", "syncPackagesFromAndroidAssets - OUT");
    }

    public void a(File file) {
        InstallSourceRecord a2 = a(InstallSourceType.FOLDER, file.getAbsolutePath());
        a(a2);
        a2.installSourceVersion = 0L;
        this.b.update(a2);
    }

    public boolean b(File file) throws IOException {
        AssetPackageReader assetPackageReader;
        final String str;
        String name;
        InstallSourceRecord a2 = a(InstallSourceType.FOLDER, file.getAbsolutePath());
        File[] listFiles = file.listFiles();
        if (listFiles == null || listFiles.length < 1) {
            a(a2);
            return false;
        }
        long j = 0;
        for (File file2 : listFiles) {
            j = Math.max(j, file2.lastModified());
        }
        if (a2.installSourceVersion == j) {
            return false;
        }
        a2.installSourceVersion = 0L;
        this.b.update(a2);
        a(a2);
        for (File file3 : listFiles) {
            if (file3.isDirectory()) {
                str = file3.getName();
                assetPackageReader = AssetPackageReader.b(file3, str);
            } else if (file3.getName().endsWith(".zip")) {
                str = file3.getName().substring(0, name.length() - 4);
                assetPackageReader = AssetPackageReader.a(file3, str);
            } else {
                assetPackageReader = null;
                str = null;
            }
            if (assetPackageReader != null) {
                a(assetPackageReader, (File) null, new AbstractStoreAssetInfo(assetPackageReader.c(), assetPackageReader.b()) { // from class: com.nexstreaming.app.common.nexasset.assetpackage.c.2
                    @Override // com.nexstreaming.app.common.nexasset.store.AbstractStoreAssetInfo, com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
                    public int getAssetIndex() {
                        return 1;
                    }

                    @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
                    public String getSubCategoryAliasName() {
                        return "local";
                    }

                    @Override // com.nexstreaming.app.common.nexasset.store.AbstractStoreAssetInfo, com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
                    public String getAssetId() {
                        return str;
                    }
                }, a2);
            }
        }
        a2.installSourceVersion = j;
        this.b.update(a2);
        return true;
    }

    public void a(InstallSourceRecord installSourceRecord) {
        this.b.beginTransaction();
        try {
            for (AssetPackageRecord assetPackageRecord : this.b.query(AssetPackageRecord.class, "install_source = ?", Long.valueOf(installSourceRecord.getDbRowID()))) {
                for (ItemRecord itemRecord : this.b.query(ItemRecord.class, "asset_package_record = ?", Long.valueOf(assetPackageRecord.getDbRowID()))) {
                    this.b.delete(itemRecord);
                }
                this.b.delete(assetPackageRecord);
            }
            this.b.setTransactionSuccessful();
            try {
                this.b.endTransaction();
            } catch (SQLiteFullException e) {
                Log.d("AssetPackageManager", "AssetPackageDb.endTransaction() throws SQLiteFullException. e=" + e);
            }
        } catch (Throwable th) {
            try {
                this.b.endTransaction();
            } catch (SQLiteFullException e2) {
                Log.d("AssetPackageManager", "AssetPackageDb.endTransaction() throws SQLiteFullException. e=" + e2);
            }
            throw th;
        }
    }

    public void a(int i) {
        this.b.beginTransaction();
        try {
            AssetPackageRecord assetPackageRecord = (AssetPackageRecord) this.b.findFirst(AssetPackageRecord.class, "asset_idx = ?", Integer.valueOf(i));
            for (ItemRecord itemRecord : this.b.query(ItemRecord.class, "asset_package_record = ?", Long.valueOf(assetPackageRecord.getDbRowID()))) {
                this.b.delete(itemRecord);
            }
            this.b.delete(assetPackageRecord);
            this.b.setTransactionSuccessful();
            try {
                this.b.endTransaction();
            } catch (SQLiteFullException e) {
                Log.d("AssetPackageManager", "AssetPackageDb.endTransaction() throws SQLiteFullException. e=" + e);
            }
        } catch (Throwable th) {
            try {
                this.b.endTransaction();
            } catch (SQLiteFullException e2) {
                Log.d("AssetPackageManager", "AssetPackageDb.endTransaction() throws SQLiteFullException. e=" + e2);
            }
            throw th;
        }
    }

    public b a(String str) {
        return (b) this.b.findFirst(AssetPackageRecord.class, "asset_id = ?", str);
    }

    public b b(int i) {
        return (b) this.b.findFirst(AssetPackageRecord.class, "asset_idx = ?", Integer.valueOf(i));
    }

    public List<? extends b> b() {
        return this.b.query(AssetPackageRecord.class);
    }

    public List<? extends f> b(String str) {
        return this.b.query(ItemRecord.class, "asset_package_record = ?", Long.valueOf(this.b.findFirstRowId(AssetPackageRecord.class, "asset_id = ?", str)));
    }

    public List<? extends f> c(int i) {
        return this.b.query(ItemRecord.class, "asset_package_record = ?", Long.valueOf(this.b.findFirstRowId(AssetPackageRecord.class, "asset_idx = ?", Integer.valueOf(i))));
    }

    public List<? extends f> a(ItemCategory itemCategory) {
        return this.b.query(ItemRecord.class, "item_category = ?", itemCategory);
    }

    public List<? extends f> a(int i, ItemCategory itemCategory) {
        return this.b.query(ItemRecord.class, "asset_package_record = ? AND item_category = ?", Long.valueOf(this.b.findFirstRowId(AssetPackageRecord.class, "asset_idx = ?", Integer.valueOf(i))), itemCategory);
    }

    public boolean a(String str, ItemCategory itemCategory) {
        return this.b.count(ItemRecord.class, "asset_package_record = ? AND item_category = ?", Long.valueOf(this.b.findFirstRowId(AssetPackageRecord.class, "asset_id = ?", str)), itemCategory) > 0;
    }

    public List<? extends f> c() {
        return this.b.query(ItemRecord.class);
    }

    public List<? extends a> d() {
        ArrayList arrayList = new ArrayList();
        for (CategoryRecord categoryRecord : this.b.query(CategoryRecord.class)) {
            if (this.b.count(AssetPackageRecord.class, "category = ?", Long.valueOf(categoryRecord.getDbRowID())) > 0) {
                arrayList.add(categoryRecord);
            }
        }
        return a((List<? extends a>) arrayList);
    }

    private List<a> a(List<? extends a> list) {
        AssetCategoryAlias[] values;
        ArrayList arrayList = new ArrayList();
        ArrayList<a> arrayList2 = new ArrayList(list);
        for (AssetCategoryAlias assetCategoryAlias : AssetCategoryAlias.values()) {
            a aVar = null;
            for (a aVar2 : arrayList2) {
                if (!aVar2.getCategoryAlias().equalsIgnoreCase(assetCategoryAlias.name())) {
                    if (aVar2.getCategoryAlias().equalsIgnoreCase(assetCategoryAlias.name() + "s")) {
                    }
                }
                aVar = aVar2;
            }
            if (aVar != null) {
                arrayList.add(aVar);
                arrayList2.remove(aVar);
            }
        }
        if (!arrayList2.isEmpty()) {
            Collections.sort(arrayList2, new Comparator<a>() { // from class: com.nexstreaming.app.common.nexasset.assetpackage.c.3
                @Override // java.util.Comparator
                /* renamed from: a */
                public int compare(a aVar3, a aVar4) {
                    return aVar3.getCategoryAlias().compareTo(aVar4.getCategoryAlias());
                }
            });
            arrayList.addAll(arrayList2);
        }
        return arrayList;
    }

    public f c(String str) {
        return (f) this.b.findFirst(ItemRecord.class, "item_id = ?", str);
    }

    private static String b(String str, String str2) {
        if (str2.startsWith(CallerDataConverter.DEFAULT_RANGE_DELIMITER) || str2.contains("/..")) {
            throw new SecurityException("Parent Path References Not Allowed");
        }
        if (str.endsWith(com.xiaomi.stat.b.h.g)) {
            return str + str2;
        }
        return str + com.xiaomi.stat.b.h.g + str2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String c(String str, String str2) {
        if (str2.startsWith(CallerDataConverter.DEFAULT_RANGE_DELIMITER) || str2.contains("/..")) {
            throw new SecurityException("Parent Path References Not Allowed");
        }
        if (str.endsWith(com.xiaomi.stat.b.h.g)) {
            return str + str2;
        }
        int lastIndexOf = str.lastIndexOf(47);
        if (lastIndexOf < 0) {
            return str2;
        }
        return str.substring(0, lastIndexOf + 1) + str2;
    }

    public void a(Iterable<String> iterable, NexEditor nexEditor, boolean z, boolean z2) {
        if (nexEditor == null || iterable == null) {
            return;
        }
        try {
            nexEditor.b(a(iterable, z2), z);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void a(Iterable<String> iterable, NexEditor nexEditor, boolean z) {
        f c;
        if (nexEditor == null || iterable == null) {
            return;
        }
        Log.d("AssetPackageManager", "loadRenderItemsInEditor");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Context b = com.nexstreaming.kminternal.kinemaster.config.a.a().b();
        nexEditor.a(z);
        for (String str : iterable) {
            if (str != null) {
                if (str.endsWith(".force_effect")) {
                    c = c(str.substring(0, str.length() - 13));
                } else {
                    c = c(str);
                }
                if (c == null) {
                    Log.w("AssetPackageManager", "Could not find item for id: " + str);
                } else if (c.getType() != ItemType.renderitem) {
                    continue;
                } else {
                    try {
                        AssetPackageReader a2 = AssetPackageReader.a(b, c.getPackageURI(), c.getAssetPackage().getAssetId());
                        InputStream a3 = a2.a(c.getFilePath());
                        byteArrayOutputStream.reset();
                        m.a(a3, byteArrayOutputStream);
                        a2.close();
                        nexEditor.a(str, byteArrayOutputStream.toString(), z);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public String a(Iterable<String> iterable) throws IOException {
        f c;
        Context b = com.nexstreaming.kminternal.kinemaster.config.a.a().b();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        StringBuilder sb = new StringBuilder();
        for (String str : iterable) {
            if (str != null) {
                if (str.endsWith(".force_effect")) {
                    c = c(str.substring(0, str.length() - 13));
                } else {
                    c = c(str);
                }
                if (c == null) {
                    Log.w("AssetPackageManager", "Could not find item for id: " + str);
                } else if (c.getType() != ItemType.renderitem) {
                    continue;
                } else {
                    try {
                        AssetPackageReader a2 = AssetPackageReader.a(b, c.getPackageURI(), c.getAssetPackage().getAssetId());
                        InputStream a3 = a2.a(c.getFilePath());
                        byteArrayOutputStream.reset();
                        m.a(a3, byteArrayOutputStream);
                        a2.close();
                        sb.append(byteArrayOutputStream.toString());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return sb.toString();
    }

    public String a(Iterable<String> iterable, boolean z) throws IOException {
        f c;
        Context b = com.nexstreaming.kminternal.kinemaster.config.a.a().b();
        StringBuilder sb = new StringBuilder();
        sb.append("<themeset name=\"KM\" defaultTheme=\"none\" defaultTransition=\"none\" >");
        sb.append("<texture id=\"video_out\" video=\"1\" />");
        sb.append("<texture id=\"video_in\" video=\"2\" />");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        for (String str : iterable) {
            if (str != null) {
                if (str.endsWith(".force_effect")) {
                    c = c(str.substring(0, str.length() - 13));
                } else {
                    c = c(str);
                }
                if (c == null) {
                    Log.w("AssetPackageManager", "Could not find item for id: " + str);
                } else if (c.getType() == ItemType.renderitem) {
                    continue;
                } else {
                    AssetPackageReader a2 = AssetPackageReader.a(b, c.getPackageURI(), c.getAssetPackage().getAssetId());
                    try {
                        InputStream a3 = a2.a(c.getFilePath());
                        byteArrayOutputStream.reset();
                        m.a(a3, byteArrayOutputStream);
                        a2.close();
                        Log.d("AssetPackageManager", "template effects: " + c.getId());
                        if (!z && str.endsWith(".force_effect")) {
                            Log.d("AssetPackageManager", "Original template transition: " + byteArrayOutputStream.toString());
                            String a4 = com.nexstreaming.app.common.util.h.a(byteArrayOutputStream.toString(), str);
                            Log.d("AssetPackageManager", "Modify template transition: " + a4);
                            sb.append(a4);
                        } else {
                            sb.append(byteArrayOutputStream.toString());
                        }
                    } catch (Throwable th) {
                        a2.close();
                        throw th;
                    }
                }
            }
        }
        sb.append("</themeset>");
        return sb.toString();
    }

    public boolean a(b bVar) {
        if (bVar == null) {
            Log.d("AssetPackageManager", "checkExpireAsset assetinfo is null.");
            return true;
        } else if (bVar.getExpireTime() <= 0) {
            return false;
        } else {
            long installedTime = bVar.getInstalledTime() + bVar.getExpireTime();
            long installedTime2 = bVar.getInstalledTime() - 86400000;
            long currentTimeMillis = System.currentTimeMillis();
            return installedTime < currentTimeMillis || installedTime2 > currentTimeMillis;
        }
    }

    public com.nexstreaming.kminternal.nexvideoeditor.a e() {
        return new com.nexstreaming.kminternal.nexvideoeditor.a() { // from class: com.nexstreaming.app.common.nexasset.assetpackage.c.4
            @Override // com.nexstreaming.kminternal.nexvideoeditor.a
            public File a(String str, String str2) throws IOException {
                throw new UnsupportedOperationException();
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.a
            public InputStream b(String str, String str2) throws IOException {
                String c;
                f c2 = c.this.c(str);
                AssetPackageReader a2 = AssetPackageReader.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), c2.getPackageURI(), c2.getAssetPackage().getAssetId());
                if (str2 != null) {
                    c = c.c(c2.getFilePath(), str2);
                } else {
                    c = c2.getFilePath();
                }
                return a2.a(c);
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.a
            public Typeface a(String str) throws Font.TypefaceLoadException {
                return com.nexstreaming.kminternal.kinemaster.fonts.c.a().b(str);
            }

            @Override // com.nexstreaming.kminternal.nexvideoeditor.a
            public Typeface c(String str, String str2) throws Font.TypefaceLoadException, IOException {
                AssetManager assets;
                f c = c.this.c(str);
                AssetPackageReader a2 = AssetPackageReader.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), c.getPackageURI(), c.getAssetPackage().getAssetId());
                String f = a2.f();
                String c2 = c.c(c.getFilePath(), str2);
                if (f.startsWith("file:")) {
                    String substring = f.substring(5);
                    try {
                        return Typeface.createFromFile(substring + com.xiaomi.stat.b.h.g + a2.b(c2));
                    } catch (RuntimeException unused) {
                        throw new Font.TypefaceLoadException();
                    }
                } else if (!f.startsWith("assets:") || (assets = com.nexstreaming.kminternal.kinemaster.config.a.a().b().getAssets()) == null) {
                    return null;
                } else {
                    String substring2 = f.substring(7);
                    try {
                        return Typeface.createFromAsset(assets, substring2 + com.xiaomi.stat.b.h.g + a2.b(c2));
                    } catch (RuntimeException unused2) {
                        throw new Font.TypefaceLoadException();
                    }
                }
            }
        };
    }
}
