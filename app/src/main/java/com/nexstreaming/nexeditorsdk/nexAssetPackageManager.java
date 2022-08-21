package com.nexstreaming.nexeditorsdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;
import android.util.SparseArray;
import ch.qos.logback.core.CoreConstants;
import com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader;
import com.nexstreaming.app.common.nexasset.assetpackage.InstallSourceType;
import com.nexstreaming.app.common.nexasset.assetpackage.ItemCategory;
import com.nexstreaming.app.common.nexasset.assetpackage.ItemType;
import com.nexstreaming.app.common.nexasset.store.AssetLocalInstallDB;
import com.nexstreaming.app.common.task.Task;
import com.nexstreaming.app.common.util.n;
import com.xiaomi.stat.b.h;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes3.dex */
public class nexAssetPackageManager {
    public static final int Mode_Hot = 1;
    public static final int Mode_New = 2;
    private static final String TAG = "nexAssetPackageMan";
    private static nexAssetPackageManager sSingleton;
    private List<b> featuredLists;
    private Context mAppContext;
    private int installPackagesCount = 0;
    private int installPackagesMaxCount = 0;
    private List<Integer> uninstalledAssetIdxList = new ArrayList();
    private SparseArray<List<Item>> uninstalledItems = new SparseArray<>();
    private Object uninstalledAssetLock = new Object();

    /* loaded from: classes3.dex */
    public interface Asset {
        String assetId();

        int assetIdx();

        String assetName(String str);

        long expireRemain();

        int getAssetVersionCode();

        String getCategoryAlias();

        String[] getSupportedLocales();

        String getThumbnailPath();

        long installedTime();

        AssetInstallType installedType();

        String priceType();
    }

    /* loaded from: classes3.dex */
    public enum AssetInstallType {
        STORE,
        SHARE,
        APP_ASSETS,
        EXTRA
    }

    /* loaded from: classes3.dex */
    public enum Category {
        audio,
        audiofilter,
        background,
        effect,
        filter,
        font,
        overlay,
        template,
        transition,
        extra,
        collage,
        staticcollage,
        dynamiccollage,
        beattemplate
    }

    /* loaded from: classes3.dex */
    public interface Item {
        Category category();

        String[] getSupportedLocales();

        boolean hidden();

        Bitmap icon();

        String id();

        boolean isDelete();

        String name(String str);

        Asset packageInfo();

        Bitmap thumbnail();

        ItemMethodType type();

        boolean validate();
    }

    /* loaded from: classes3.dex */
    public enum ItemMethodType {
        ItemOverlay,
        ItemRenderitem,
        ItemKedl,
        ItemAudio,
        ItemFont,
        ItemTemplate,
        ItemLut,
        ItemMedia,
        ItemExtra,
        ItemCollage,
        ItemStaticCollage,
        ItemDynamicCollage,
        ItemBeat
    }

    /* loaded from: classes3.dex */
    public static abstract class OnInstallPackageListener {
        public static int kEvent_installOk = 0;
        public static int kEvent_linstallFail = -1;

        public abstract void onCompleted(int i, int i2);

        public abstract void onProgress(int i, int i2, int i3);
    }

    /* loaded from: classes3.dex */
    public enum PreAssetCategoryAlias {
        Effect,
        Transition,
        Font,
        Overlay,
        Audio,
        Template,
        ClipGraphics,
        TextEffect,
        Extra,
        Collage,
        StaticCollage,
        DynamicCollage,
        BeatTemplate
    }

    /* loaded from: classes3.dex */
    public interface RemoteAssetInfo {
        String getCategoryAlias();

        Bitmap icon();

        String id();

        int idx();

        String name();
    }

    /* loaded from: classes3.dex */
    public static class c implements Item {
        public Category category;
        private boolean getNamesMap;
        public String id;
        public boolean isDelete;
        public boolean isHidden;
        public String name;
        private Map<String, String> namesMap;
        public Asset packInfo;
        public ItemMethodType type;

        public c() {
        }

        private void loadLabels() {
            if (this.namesMap != null || this.getNamesMap) {
                return;
            }
            this.getNamesMap = true;
            this.namesMap = new HashMap();
            com.nexstreaming.app.common.nexasset.assetpackage.f c = com.nexstreaming.app.common.nexasset.assetpackage.c.a().c(id());
            if (c == null) {
                Log.d(nexAssetPackageManager.TAG, "No ItemInfo! id=" + id());
                return;
            }
            Map<String, String> label = c.getLabel();
            if (label == null) {
                return;
            }
            for (String str : label.keySet()) {
                if (this.name == null) {
                    this.name = label.get(str);
                }
                this.namesMap.put(str.toLowerCase(Locale.ENGLISH), label.get(str));
            }
        }

        public c(Item item) {
            this.id = item.id();
            this.packInfo = item.packageInfo();
            this.category = item.category();
            this.isDelete = item.isDelete();
            this.isHidden = item.hidden();
            this.category = item.category();
            this.type = item.type();
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public String name(String str) {
            String str2;
            loadLabels();
            if (str == null) {
                this.name = n.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), this.namesMap, this.id);
            }
            if (str == null) {
                String str3 = this.name;
                return str3 == null ? this.id : str3;
            }
            Map<String, String> map = this.namesMap;
            if (map != null && (str2 = map.get(str.toLowerCase(Locale.ENGLISH))) != null) {
                return str2;
            }
            String str4 = this.name;
            return str4 == null ? this.id : str4;
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public String id() {
            return this.id;
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public Asset packageInfo() {
            return this.packInfo;
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public boolean validate() {
            return com.nexstreaming.app.common.nexasset.assetpackage.c.a().c(this.id) != null;
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public boolean isDelete() {
            return this.isDelete;
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public Bitmap icon() {
            Bitmap bitmap;
            com.nexstreaming.app.common.nexasset.assetpackage.f c = com.nexstreaming.app.common.nexasset.assetpackage.c.a().c(this.id);
            if (c != null) {
                try {
                    bitmap = com.nexstreaming.app.common.nexasset.assetpackage.e.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), c, 0, 0);
                } catch (IOException unused) {
                    bitmap = null;
                }
                if (bitmap == null) {
                    try {
                        bitmap = com.nexstreaming.app.common.nexasset.assetpackage.e.b(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), c, 0, 0);
                    } catch (IOException unused2) {
                    }
                }
                if (bitmap != null) {
                    return bitmap;
                }
            }
            return null;
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public Category category() {
            return this.category;
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public boolean hidden() {
            return this.isHidden;
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public Bitmap thumbnail() {
            com.nexstreaming.app.common.nexasset.assetpackage.b assetPackage;
            String thumbPath;
            Asset asset = this.packInfo;
            if (asset == null) {
                com.nexstreaming.app.common.nexasset.assetpackage.f c = com.nexstreaming.app.common.nexasset.assetpackage.c.a().c(this.id);
                if (c != null && (assetPackage = c.getAssetPackage()) != null && (thumbPath = assetPackage.getThumbPath()) != null && new File(thumbPath).isFile()) {
                    return BitmapFactory.decodeFile(thumbPath);
                }
                return null;
            }
            String thumbnailPath = asset.getThumbnailPath();
            if (thumbnailPath != null && new File(thumbnailPath).isFile()) {
                return BitmapFactory.decodeFile(thumbnailPath);
            }
            return null;
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public String[] getSupportedLocales() {
            loadLabels();
            Map<String, String> map = this.namesMap;
            int i = 0;
            if (map == null) {
                return new String[0];
            }
            if (map.size() == 0) {
                return new String[0];
            }
            String[] strArr = new String[this.namesMap.size()];
            for (String str : this.namesMap.keySet()) {
                strArr[i] = str;
                i++;
            }
            return strArr;
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public ItemMethodType type() {
            return this.type;
        }
    }

    /* loaded from: classes3.dex */
    public static class a implements Asset {
        public int a;
        public String b;
        public String c;
        public Map<String, String> d;
        public String e;
        public long f;
        public long g;
        public AssetInstallType h;
        public String i;
        public String j;
        public int k;
        private boolean l;

        private void a() {
            if (this.d != null || this.l) {
                return;
            }
            this.l = true;
            this.d = new HashMap();
            com.nexstreaming.app.common.nexasset.assetpackage.b a = com.nexstreaming.app.common.nexasset.assetpackage.c.a().a(this.b);
            if (a == null) {
                Log.d(nexAssetPackageManager.TAG, "No AssetInfo! id=" + this.b);
                return;
            }
            Map<String, String> assetName = a.getAssetName();
            if (assetName != null) {
                for (String str : assetName.keySet()) {
                    if (this.c == null) {
                        this.c = assetName.get(str);
                    }
                    this.d.put(str.toLowerCase(Locale.ENGLISH), assetName.get(str));
                }
                return;
            }
            try {
                AssetPackageReader a2 = AssetPackageReader.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), com.nexstreaming.app.common.nexasset.assetpackage.c.a().b(this.b).get(0).getPackageURI(), this.b);
                Map<String, String> e = a2.e();
                if (e != null) {
                    for (String str2 : e.keySet()) {
                        if (this.c == null) {
                            this.c = e.get(str2);
                        }
                        this.d.put(str2.toLowerCase(Locale.ENGLISH), e.get(str2));
                    }
                }
                a2.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Asset
        public int assetIdx() {
            return this.a;
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Asset
        public String assetId() {
            return this.b;
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Asset
        public String assetName(String str) {
            a();
            if (str == null) {
                this.c = n.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), this.d, this.b);
            }
            Map<String, String> map = this.d;
            if (map != null) {
                if (str == null) {
                    return this.c;
                }
                String str2 = map.get(str.toLowerCase(Locale.ENGLISH));
                if (str2 != null) {
                    return str2;
                }
            }
            String str3 = this.c;
            return str3 == null ? this.b : str3;
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Asset
        public String priceType() {
            return this.e;
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Asset
        public long installedTime() {
            return this.f;
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Asset
        public long expireRemain() {
            return this.g;
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Asset
        public AssetInstallType installedType() {
            return this.h;
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Asset
        public String getCategoryAlias() {
            return this.i;
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Asset
        public String getThumbnailPath() {
            return this.j;
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Asset
        public String[] getSupportedLocales() {
            a();
            Map<String, String> map = this.d;
            int i = 0;
            if (map == null) {
                return new String[0];
            }
            if (map.size() == 0) {
                return new String[0];
            }
            String[] strArr = new String[this.d.size()];
            for (String str : this.d.keySet()) {
                Log.d(nexAssetPackageManager.TAG, "AssetEnt locale=" + str);
                strArr[i] = str;
                i++;
            }
            return strArr;
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Asset
        public int getAssetVersionCode() {
            return this.k;
        }
    }

    private nexAssetPackageManager(Context context) {
        this.mAppContext = context;
    }

    public static nexAssetPackageManager getAssetPackageManager(Context context) {
        nexAssetPackageManager nexassetpackagemanager = sSingleton;
        if (nexassetpackagemanager != null && !nexassetpackagemanager.mAppContext.getPackageName().equals(context.getPackageName())) {
            sSingleton = null;
        }
        if (sSingleton == null) {
            sSingleton = new nexAssetPackageManager(context);
            AssetLocalInstallDB.getInstance(context);
        }
        return sSingleton;
    }

    private ItemCategory getItemCategory(Category category) {
        if (category == Category.audio) {
            return ItemCategory.audio;
        }
        if (category == Category.audiofilter) {
            return ItemCategory.audiofilter;
        }
        if (category == Category.background) {
            return ItemCategory.background;
        }
        if (category == Category.effect) {
            return ItemCategory.effect;
        }
        if (category == Category.filter) {
            return ItemCategory.filter;
        }
        if (category == Category.font) {
            return ItemCategory.font;
        }
        if (category == Category.overlay) {
            return ItemCategory.overlay;
        }
        if (category == Category.template) {
            return ItemCategory.template;
        }
        if (category == Category.transition) {
            return ItemCategory.transition;
        }
        if (category == Category.collage) {
            return ItemCategory.collage;
        }
        if (category == Category.staticcollage) {
            return ItemCategory.staticcollage;
        }
        if (category == Category.dynamiccollage) {
            return ItemCategory.dynamiccollage;
        }
        if (category != Category.beattemplate) {
            return null;
        }
        return ItemCategory.beattemplate;
    }

    private Category getCategory(ItemCategory itemCategory) {
        if (itemCategory == ItemCategory.audio) {
            return Category.audio;
        }
        if (itemCategory == ItemCategory.audiofilter) {
            return Category.audiofilter;
        }
        if (itemCategory == ItemCategory.background) {
            return Category.background;
        }
        if (itemCategory == ItemCategory.effect) {
            return Category.effect;
        }
        if (itemCategory == ItemCategory.filter) {
            return Category.filter;
        }
        if (itemCategory == ItemCategory.font) {
            return Category.font;
        }
        if (itemCategory == ItemCategory.overlay) {
            return Category.overlay;
        }
        if (itemCategory == ItemCategory.template) {
            return Category.template;
        }
        if (itemCategory == ItemCategory.transition) {
            return Category.transition;
        }
        if (itemCategory == ItemCategory.collage) {
            return Category.collage;
        }
        if (itemCategory == ItemCategory.staticcollage) {
            return Category.staticcollage;
        }
        if (itemCategory == ItemCategory.dynamiccollage) {
            return Category.dynamiccollage;
        }
        if (itemCategory == ItemCategory.beattemplate) {
            return Category.beattemplate;
        }
        return Category.extra;
    }

    public static ItemMethodType getMethodType(ItemType itemType) {
        if (itemType == ItemType.overlay) {
            return ItemMethodType.ItemOverlay;
        }
        if (itemType == ItemType.renderitem) {
            return ItemMethodType.ItemRenderitem;
        }
        if (itemType == ItemType.kedl) {
            return ItemMethodType.ItemKedl;
        }
        if (itemType == ItemType.audio) {
            return ItemMethodType.ItemAudio;
        }
        if (itemType == ItemType.font) {
            return ItemMethodType.ItemFont;
        }
        if (itemType == ItemType.template) {
            return ItemMethodType.ItemTemplate;
        }
        if (itemType == ItemType.lut) {
            return ItemMethodType.ItemLut;
        }
        if (itemType == ItemType.media) {
            return ItemMethodType.ItemMedia;
        }
        if (itemType == ItemType.collage) {
            return ItemMethodType.ItemCollage;
        }
        if (itemType == ItemType.staticcollage) {
            return ItemMethodType.ItemStaticCollage;
        }
        if (itemType == ItemType.dynamiccollage) {
            return ItemMethodType.ItemDynamicCollage;
        }
        if (itemType != ItemType.beat) {
            return null;
        }
        return ItemMethodType.ItemBeat;
    }

    private void resolveAssets(List<Item> list, Category category) {
        ItemCategory itemCategory;
        List<? extends com.nexstreaming.app.common.nexasset.assetpackage.f> a2;
        if (category != null) {
            itemCategory = getItemCategory(category);
            if (itemCategory == null) {
                return;
            }
        } else {
            itemCategory = null;
        }
        if (itemCategory == null || (a2 = com.nexstreaming.app.common.nexasset.assetpackage.c.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).a(itemCategory)) == null) {
            return;
        }
        for (com.nexstreaming.app.common.nexasset.assetpackage.f fVar : a2) {
            list.add(getItemEnt(fVar));
        }
    }

    public List<Item> getInstalledAssetItems() {
        ArrayList arrayList = new ArrayList();
        List<? extends com.nexstreaming.app.common.nexasset.assetpackage.f> c2 = com.nexstreaming.app.common.nexasset.assetpackage.c.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).c();
        if (c2 != null) {
            for (com.nexstreaming.app.common.nexasset.assetpackage.f fVar : c2) {
                arrayList.add(getItemEnt(fVar));
            }
        }
        return arrayList;
    }

    public List<Item> getInstalledAssetItems(Category category) {
        ArrayList arrayList = new ArrayList();
        resolveAssets(arrayList, category);
        return arrayList;
    }

    public void getInstalledAssetItems(List<Item> list, Category category) {
        resolveAssets(list, category);
    }

    public List<Item> getInstalledAssetItemsByAssetIDx(int i) {
        List<? extends com.nexstreaming.app.common.nexasset.assetpackage.f> c2 = com.nexstreaming.app.common.nexasset.assetpackage.c.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).c(i);
        ArrayList arrayList = new ArrayList();
        for (com.nexstreaming.app.common.nexasset.assetpackage.f fVar : c2) {
            arrayList.add(getItemEnt(fVar));
        }
        return arrayList;
    }

    public List<Item> getInstalledAssetItemsByAssetID(String str) {
        List<? extends com.nexstreaming.app.common.nexasset.assetpackage.f> b2 = com.nexstreaming.app.common.nexasset.assetpackage.c.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).b(str);
        ArrayList arrayList = new ArrayList();
        for (com.nexstreaming.app.common.nexasset.assetpackage.f fVar : b2) {
            arrayList.add(getItemEnt(fVar));
        }
        return arrayList;
    }

    public List<Asset> getInstalledAsset() {
        List<? extends com.nexstreaming.app.common.nexasset.assetpackage.b> b2 = com.nexstreaming.app.common.nexasset.assetpackage.c.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).b();
        ArrayList arrayList = new ArrayList();
        for (com.nexstreaming.app.common.nexasset.assetpackage.b bVar : b2) {
            arrayList.add(getAssetEnt(bVar));
        }
        return arrayList;
    }

    public List<Item> getInstalledAssetItemsByAssetIDx(int i, Category category) {
        List<? extends com.nexstreaming.app.common.nexasset.assetpackage.f> a2 = com.nexstreaming.app.common.nexasset.assetpackage.c.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).a(i, getItemCategory(category));
        ArrayList arrayList = new ArrayList();
        for (com.nexstreaming.app.common.nexasset.assetpackage.f fVar : a2) {
            arrayList.add(getItemEnt(fVar));
        }
        return arrayList;
    }

    public List<Asset> getInstalledAssetByAppAsset() {
        List<? extends com.nexstreaming.app.common.nexasset.assetpackage.b> b2 = com.nexstreaming.app.common.nexasset.assetpackage.c.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).b();
        ArrayList arrayList = new ArrayList();
        for (com.nexstreaming.app.common.nexasset.assetpackage.b bVar : b2) {
            if (bVar.getInstallSourceType() == InstallSourceType.APP_ASSETS) {
                arrayList.add(getAssetEnt(bVar));
            }
        }
        return arrayList;
    }

    public List<Asset> getInstalledAssetByAppShare() {
        List<? extends com.nexstreaming.app.common.nexasset.assetpackage.b> b2 = com.nexstreaming.app.common.nexasset.assetpackage.c.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).b();
        ArrayList arrayList = new ArrayList();
        for (com.nexstreaming.app.common.nexasset.assetpackage.b bVar : b2) {
            if (bVar.getInstallSourceType() == InstallSourceType.FOLDER) {
                arrayList.add(getAssetEnt(bVar));
            }
        }
        return arrayList;
    }

    public List<Asset> getInstalledAssetByStore() {
        List<? extends com.nexstreaming.app.common.nexasset.assetpackage.b> b2 = com.nexstreaming.app.common.nexasset.assetpackage.c.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).b();
        ArrayList arrayList = new ArrayList();
        for (com.nexstreaming.app.common.nexasset.assetpackage.b bVar : b2) {
            if (bVar.getInstallSourceType() == InstallSourceType.STORE) {
                arrayList.add(getAssetEnt(bVar));
            }
        }
        return arrayList;
    }

    public Asset getInstalledAssetByIdx(int i) {
        com.nexstreaming.app.common.nexasset.assetpackage.b b2 = com.nexstreaming.app.common.nexasset.assetpackage.c.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).b(i);
        if (b2 == null) {
            return null;
        }
        return getAssetEnt(b2);
    }

    private c getItemEnt(com.nexstreaming.app.common.nexasset.assetpackage.f fVar) {
        c cVar = new c();
        cVar.id = fVar.getId();
        Map<String, String> label = fVar.getLabel();
        if (label != null) {
            cVar.name = label.get("en");
        }
        if (cVar.name == null) {
            cVar.name = cVar.id;
        }
        a assetEnt = getAssetEnt(fVar.getAssetPackage());
        cVar.packInfo = assetEnt;
        if (assetEnt.installedType() == AssetInstallType.STORE) {
            cVar.isDelete = true;
        } else {
            cVar.isDelete = false;
        }
        cVar.isHidden = fVar.isHidden();
        cVar.category = getCategory(fVar.getCategory());
        cVar.type = getMethodType(fVar.getType());
        return cVar;
    }

    private a getAssetEnt(com.nexstreaming.app.common.nexasset.assetpackage.b bVar) {
        a aVar = new a();
        if (bVar == null) {
            Log.d(TAG, "getAssetEnt AssetInfo is null!");
            aVar.h = AssetInstallType.EXTRA;
            aVar.i = "Unknown";
            aVar.e = "Free";
        } else {
            aVar.a = bVar.getAssetIdx();
            aVar.b = bVar.getAssetId();
            Map<String, String> assetName = bVar.getAssetName();
            if (assetName != null) {
                aVar.c = assetName.get("en");
            }
            String priceType = bVar.getPriceType();
            aVar.e = priceType;
            if (priceType == null) {
                aVar.e = "Free";
            }
            aVar.g = bVar.getExpireTime();
            aVar.f = bVar.getInstalledTime();
            if (bVar.getInstallSourceType() == InstallSourceType.APP_ASSETS) {
                aVar.h = AssetInstallType.APP_ASSETS;
            } else if (bVar.getInstallSourceType() == InstallSourceType.FOLDER) {
                aVar.h = AssetInstallType.SHARE;
            } else if (bVar.getInstallSourceType() == InstallSourceType.STORE) {
                aVar.h = AssetInstallType.STORE;
            }
            if (bVar.getAssetCategory() != null) {
                aVar.i = bVar.getAssetCategory().getCategoryAlias();
            } else {
                aVar.i = "Unknown";
            }
            aVar.j = bVar.getThumbPath();
            aVar.k = bVar.getPackageVersion();
        }
        return aVar;
    }

    public Item getInstalledAssetItemById(String str) {
        com.nexstreaming.app.common.nexasset.assetpackage.f c2 = com.nexstreaming.app.common.nexasset.assetpackage.c.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).c(str);
        if (c2 == null) {
            return null;
        }
        return getItemEnt(c2);
    }

    public void uninstallPackageByAssetIdx(int i) {
        putUninstallItem(i);
        try {
            AssetLocalInstallDB.getInstance(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).uninstallPackage(i);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void uninstallPackageById(String str) {
        com.nexstreaming.app.common.nexasset.assetpackage.f c2 = com.nexstreaming.app.common.nexasset.assetpackage.c.a().c(str);
        if (c2 == null) {
            return;
        }
        com.nexstreaming.app.common.nexasset.assetpackage.b assetPackage = c2.getAssetPackage();
        if (assetPackage != null) {
            uninstallPackageByAssetIdx(assetPackage.getAssetIdx());
        } else {
            Log.d(TAG, "AssetInfo is null");
        }
    }

    public int findNewPackages() {
        return AssetLocalInstallDB.getInstance(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).checkStoreInstall();
    }

    public boolean isInstallingPackages() {
        return AssetLocalInstallDB.getInstance(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).isInstallingPackages();
    }

    public void installPackagesAsync(OnInstallPackageListener onInstallPackageListener) {
        installPackagesAsync(0, onInstallPackageListener);
    }

    public void installPackagesAsync(final int i, final OnInstallPackageListener onInstallPackageListener) {
        this.installPackagesCount = 0;
        this.installPackagesMaxCount = 0;
        AssetLocalInstallDB.getInstance(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).installPackageAsync(i).mo1850onComplete(new Task.OnTaskEventListener() { // from class: com.nexstreaming.nexeditorsdk.nexAssetPackageManager.3
            @Override // com.nexstreaming.app.common.task.Task.OnTaskEventListener
            public void onTaskEvent(Task task, Task.Event event) {
                OnInstallPackageListener onInstallPackageListener2 = onInstallPackageListener;
                if (onInstallPackageListener2 != null) {
                    onInstallPackageListener2.onCompleted(0, i);
                }
            }
        }).mo1851onFailure(new Task.OnFailListener() { // from class: com.nexstreaming.nexeditorsdk.nexAssetPackageManager.2
            @Override // com.nexstreaming.app.common.task.Task.OnFailListener
            public void onFail(Task task, Task.Event event, Task.TaskError taskError) {
                OnInstallPackageListener onInstallPackageListener2 = onInstallPackageListener;
                if (onInstallPackageListener2 != null) {
                    onInstallPackageListener2.onCompleted(-1, i);
                }
            }
        }).mo1852onProgress(new Task.OnProgressListener() { // from class: com.nexstreaming.nexeditorsdk.nexAssetPackageManager.1
            @Override // com.nexstreaming.app.common.task.Task.OnProgressListener
            public void onProgress(Task task, Task.Event event, int i2, int i3) {
                OnInstallPackageListener onInstallPackageListener2 = onInstallPackageListener;
                if (onInstallPackageListener2 != null) {
                    if (i3 == 100) {
                        onInstallPackageListener2.onProgress(nexAssetPackageManager.this.installPackagesCount, nexAssetPackageManager.this.installPackagesMaxCount, i2);
                        return;
                    }
                    nexAssetPackageManager.this.installPackagesCount = i2;
                    nexAssetPackageManager.this.installPackagesMaxCount = i3;
                    onInstallPackageListener.onProgress(i2, i3, 0);
                }
            }
        });
    }

    public boolean installPackageFromStorePath(int i) {
        return AssetLocalInstallDB.getInstance(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).installPackageSync(i);
    }

    /* JADX WARN: Removed duplicated region for block: B:20:0x0076  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x008b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String getAssetPackageMediaPath(android.content.Context r11, java.lang.String r12) {
        /*
            Method dump skipped, instructions count: 514
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.nexeditorsdk.nexAssetPackageManager.getAssetPackageMediaPath(android.content.Context, java.lang.String):java.lang.String");
    }

    public static Rect getAssetPackageMediaOptions(String str) {
        com.nexstreaming.app.common.nexasset.assetpackage.f c2 = com.nexstreaming.app.common.nexasset.assetpackage.c.a().c(str);
        BitmapFactory.Options options = new BitmapFactory.Options();
        Rect rect = new Rect();
        if (c2 != null) {
            try {
                AssetPackageReader a2 = AssetPackageReader.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), c2.getPackageURI(), c2.getAssetPackage().getAssetId());
                try {
                    InputStream a3 = a2.a(c2.getFilePath());
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(a3);
                    bufferedInputStream.mark(bufferedInputStream.available());
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(bufferedInputStream, null, options);
                    rect.set(0, 0, options.outWidth, options.outHeight);
                    bufferedInputStream.close();
                    a3.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                a2.close();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        return rect;
    }

    public static String getPreloadedMediaAppAssetPath(Context context, String str) {
        AssetLocalInstallDB.getInstance(context);
        com.nexstreaming.app.common.nexasset.assetpackage.f c2 = com.nexstreaming.app.common.nexasset.assetpackage.c.a().c(str);
        if (c2 != null) {
            if (com.nexstreaming.app.common.nexasset.assetpackage.c.a().a(c2.getAssetPackage())) {
                Log.d(TAG, "getPreloadedAssetPath expire id=" + str);
                return null;
            }
            String packageURI = c2.getPackageURI();
            if (packageURI != null) {
                String substring = packageURI.substring(packageURI.indexOf(58) + 1);
                if (packageURI.startsWith("assets:")) {
                    String str2 = substring + h.g + c2.getFilePath();
                    Log.d(TAG, "getPreloadedAssetPath assets path=" + str2);
                    return str2;
                }
            }
        }
        return null;
    }

    public String[] getAssetCategoriesWithInstalledItems() {
        List<? extends com.nexstreaming.app.common.nexasset.assetpackage.a> d2 = com.nexstreaming.app.common.nexasset.assetpackage.c.a().d();
        int size = d2.size();
        String[] strArr = new String[size];
        for (int i = 0; i < size; i++) {
            strArr[i] = new String(d2.get(i).getCategoryAlias());
        }
        return strArr;
    }

    /* loaded from: classes3.dex */
    public static class d implements RemoteAssetInfo {
        private int a;
        private String b;
        private String c;
        private String d;
        private String e;

        public d(String str, String str2, int i, String str3, String str4) {
            this.d = str;
            this.b = str2;
            this.a = i;
            this.c = str3;
            this.e = str4;
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.RemoteAssetInfo
        public int idx() {
            return this.a;
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.RemoteAssetInfo
        public String id() {
            return this.b;
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.RemoteAssetInfo
        public String name() {
            return this.c;
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.RemoteAssetInfo
        public Bitmap icon() {
            String str = this.d;
            if (str == null) {
                return null;
            }
            return BitmapFactory.decodeFile(str);
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.RemoteAssetInfo
        public String getCategoryAlias() {
            return this.e;
        }

        public String toString() {
            return "RemoteAssetInfoEnt{category='" + this.e + CoreConstants.SINGLE_QUOTE_CHAR + ", idx=" + this.a + ", id='" + this.b + CoreConstants.SINGLE_QUOTE_CHAR + ", name='" + this.c + CoreConstants.SINGLE_QUOTE_CHAR + ", iconPath='" + this.d + CoreConstants.SINGLE_QUOTE_CHAR + '}';
        }
    }

    public RemoteAssetInfo[] getRemoteAssetInfos(int i) {
        List<AssetLocalInstallDB.remoteAssetItem> list = AssetLocalInstallDB.getInstance(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).getList(i, nexAssetStoreAppUtils.vendorName());
        d[] dVarArr = new d[list.size()];
        int i2 = 0;
        for (AssetLocalInstallDB.remoteAssetItem remoteassetitem : list) {
            dVarArr[i2] = new d(remoteassetitem.thumbnailPath, remoteassetitem.id, remoteassetitem.idx, remoteassetitem.name, remoteassetitem.category);
            i2++;
        }
        return dVarArr;
    }

    /* loaded from: classes3.dex */
    public class b {
        private int b;
        private int c;
        private List<d> d;

        private b() {
        }
    }

    private void updateList(int i) {
        b bVar;
        if (this.featuredLists == null) {
            this.featuredLists = new ArrayList();
        }
        List<AssetLocalInstallDB.remoteAssetItem> list = AssetLocalInstallDB.getInstance(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).getList(i, nexAssetStoreAppUtils.vendorName());
        if (list.size() == 0) {
            return;
        }
        Iterator<b> it = this.featuredLists.iterator();
        while (true) {
            if (!it.hasNext()) {
                bVar = null;
                break;
            }
            bVar = it.next();
            if (bVar.b == i) {
                break;
            }
        }
        if (bVar != null) {
            bVar.d.clear();
            bVar.c = 0;
        } else {
            bVar = new b();
            bVar.b = i;
            bVar.d = new ArrayList();
            this.featuredLists.add(bVar);
        }
        for (AssetLocalInstallDB.remoteAssetItem remoteassetitem : list) {
            bVar.d.add(new d(remoteassetitem.thumbnailPath, remoteassetitem.id, remoteassetitem.idx, remoteassetitem.name, remoteassetitem.category));
        }
    }

    public RemoteAssetInfo getRemoteAssetInfo(int i) {
        b bVar;
        if (this.featuredLists == null) {
            updateList(i);
        }
        Iterator<b> it = this.featuredLists.iterator();
        while (true) {
            if (!it.hasNext()) {
                bVar = null;
                break;
            }
            bVar = it.next();
            if (bVar.b == i) {
                break;
            }
        }
        if (bVar == null || bVar.d.size() == 0) {
            return null;
        }
        if (bVar.c >= bVar.d.size()) {
            updateList(i);
            bVar.c = 0;
        }
        Log.d(TAG, "afl.current=" + bVar.c + ", afl.list.size()=" + bVar.d.size());
        RemoteAssetInfo remoteAssetInfo = (RemoteAssetInfo) bVar.d.get(bVar.c);
        bVar.c = bVar.c + 1;
        return remoteAssetInfo;
    }

    @Deprecated
    public void uninstallFromAssetStoreApp() {
        AssetLocalInstallDB.getInstance(this.mAppContext).uninstallFromAssetStoreApp();
    }

    public boolean validateAssetPackage(int i) {
        if (i < 3) {
            return true;
        }
        List<? extends com.nexstreaming.app.common.nexasset.assetpackage.f> c2 = com.nexstreaming.app.common.nexasset.assetpackage.c.a().c(i);
        if (c2.size() == 0) {
            return false;
        }
        String packageURI = c2.get(0).getPackageURI();
        if (!packageURI.startsWith("file:")) {
            return true;
        }
        String substring = packageURI.substring(packageURI.indexOf(58) + 1);
        if (!new File(substring).isDirectory()) {
            return false;
        }
        for (com.nexstreaming.app.common.nexasset.assetpackage.f fVar : c2) {
            if (!new File(substring, fVar.getFilePath()).isFile()) {
                Log.d(TAG, "file not found " + substring + h.g + fVar.getFilePath());
                return false;
            }
        }
        return true;
    }

    public static boolean checkExpireAsset(Asset asset) {
        if (asset == null) {
            Log.d(TAG, "checkExpireAsset() : asset is null! return true.");
            return true;
        }
        return com.nexstreaming.app.common.nexasset.assetpackage.c.a().a(com.nexstreaming.app.common.nexasset.assetpackage.c.a().b(asset.assetIdx()));
    }

    public static long expireRemainTime(Asset asset) {
        if (asset.expireRemain() > 0) {
            long installedTime = (asset.installedTime() + asset.expireRemain()) - System.currentTimeMillis();
            if (installedTime >= 0) {
                return installedTime;
            }
            return 0L;
        }
        return Long.MAX_VALUE;
    }

    public PreAssetCategoryAlias getPreAssetCategoryAliasFromItem(String str) {
        Item installedAssetItemById = getAssetPackageManager(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).getInstalledAssetItemById(str);
        if (installedAssetItemById == null) {
            return PreAssetCategoryAlias.Extra;
        }
        return getPreAssetCategoryAliasFromItem(installedAssetItemById);
    }

    public static PreAssetCategoryAlias getPreAssetCategoryAliasFromItem(Item item) {
        if (item.category() == Category.template) {
            return PreAssetCategoryAlias.Template;
        }
        Category category = item.category();
        Category category2 = Category.overlay;
        if (category == category2 && item.type() == ItemMethodType.ItemTemplate) {
            return PreAssetCategoryAlias.TextEffect;
        }
        if (item.category() == category2 && item.type() == ItemMethodType.ItemOverlay) {
            return PreAssetCategoryAlias.Overlay;
        }
        if (item.category() == Category.audio) {
            return PreAssetCategoryAlias.Audio;
        }
        if (item.category() == Category.effect) {
            return PreAssetCategoryAlias.Effect;
        }
        if (item.category() == Category.transition) {
            return PreAssetCategoryAlias.Transition;
        }
        if (item.category() == Category.font) {
            return PreAssetCategoryAlias.Font;
        }
        if (item.category() == Category.collage) {
            return PreAssetCategoryAlias.Collage;
        }
        if (item.category() == Category.staticcollage) {
            return PreAssetCategoryAlias.StaticCollage;
        }
        if (item.category() == Category.dynamiccollage) {
            return PreAssetCategoryAlias.DynamicCollage;
        }
        if (item.category() == Category.beattemplate) {
            return PreAssetCategoryAlias.BeatTemplate;
        }
        return PreAssetCategoryAlias.Extra;
    }

    public PreAssetCategoryAlias[] updateAssetInManager(boolean z, int i) {
        List<Item> list;
        HashSet<PreAssetCategoryAlias> hashSet = new HashSet();
        if (!z) {
            list = popUninstallItems(i);
            if (list == null) {
                Log.d(TAG, "updateAssetInManager can not found uninstalled items assetIdx=" + i);
            }
        } else {
            list = null;
        }
        if (list == null) {
            list = getAssetPackageManager(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).getInstalledAssetItemsByAssetIDx(i);
        }
        int i2 = 0;
        if (list == null) {
            Log.d(TAG, "updateAssetInManager asset not found assetIdx=" + i);
            return new PreAssetCategoryAlias[0];
        } else if (list.size() == 0) {
            Log.d(TAG, "updateAssetInManager asset size 0 assetIdx=" + i);
            return new PreAssetCategoryAlias[0];
        } else {
            boolean z2 = true;
            boolean z3 = true;
            boolean z4 = true;
            boolean z5 = true;
            for (Item item : list) {
                if (item.category() == Category.filter && item.type() == ItemMethodType.ItemLut) {
                    Log.d(TAG, "updateAssetInManager update color effect assetIdx=" + i);
                    com.nexstreaming.kminternal.kinemaster.editorwrapper.b a2 = com.nexstreaming.kminternal.kinemaster.editorwrapper.b.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b());
                    if (a2 != null) {
                        if (item.hidden()) {
                            a2.h();
                        } else {
                            nexColorEffect.setNeedUpdate();
                        }
                    }
                } else {
                    Category category = item.category();
                    Category category2 = Category.template;
                    if (category == category2 || item.category() == Category.beattemplate) {
                        nexTemplateManager templateManager = nexTemplateManager.getTemplateManager(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), com.nexstreaming.kminternal.kinemaster.config.a.a().b());
                        if (z2) {
                            templateManager.updateTemplate(false, item);
                            z2 = false;
                        }
                        if (z) {
                            templateManager.updateTemplate(true, item);
                        }
                        if (item.category() == Category.beattemplate) {
                            nexBeatTemplateManager beatTemplateManager = nexBeatTemplateManager.getBeatTemplateManager(com.nexstreaming.kminternal.kinemaster.config.a.a().b());
                            if (z3) {
                                beatTemplateManager.updateBeatTemplate(false, item);
                                z3 = false;
                            }
                            if (z) {
                                beatTemplateManager.updateBeatTemplate(true, item);
                            }
                        }
                        if (!item.hidden()) {
                            if (item.category() == category2) {
                                hashSet.add(PreAssetCategoryAlias.Template);
                            } else {
                                hashSet.add(PreAssetCategoryAlias.BeatTemplate);
                            }
                        }
                    } else {
                        Category category3 = item.category();
                        Category category4 = Category.overlay;
                        if (category3 == category4 && item.type() == ItemMethodType.ItemTemplate) {
                            nexOverlayManager overlayManager = nexOverlayManager.getOverlayManager(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), com.nexstreaming.kminternal.kinemaster.config.a.a().b());
                            if (z4) {
                                overlayManager.updateOverlayTitle(false, item);
                                z4 = false;
                            }
                            if (z) {
                                overlayManager.updateOverlayTitle(true, item);
                            }
                            if (!item.hidden()) {
                                hashSet.add(PreAssetCategoryAlias.TextEffect);
                            }
                        } else if (item.category() == Category.audio) {
                            nexAssetMediaManager audioManager = nexAssetMediaManager.getAudioManager(com.nexstreaming.kminternal.kinemaster.config.a.a().b());
                            audioManager.updateMedia(false, 0, false, item);
                            if (z) {
                                audioManager.updateMedia(true, 3, false, item);
                            }
                            if (!item.hidden()) {
                                hashSet.add(PreAssetCategoryAlias.Audio);
                            }
                        } else if (item.category() == Category.font) {
                            nexFont.needUpdate();
                            if (!item.hidden()) {
                                hashSet.add(PreAssetCategoryAlias.Font);
                            }
                        } else if (item.category() == Category.effect) {
                            if (!item.hidden()) {
                                hashSet.add(PreAssetCategoryAlias.Effect);
                            }
                        } else if (item.category() == Category.transition) {
                            if (!item.hidden()) {
                                hashSet.add(PreAssetCategoryAlias.Transition);
                            }
                        } else if (item.category() == category4 && item.type() == ItemMethodType.ItemOverlay) {
                            if (!item.hidden()) {
                                hashSet.add(PreAssetCategoryAlias.Overlay);
                            }
                        } else if (item.category() == Category.collage || item.category() == Category.staticcollage || item.category() == Category.dynamiccollage) {
                            nexCollageManager collageManager = nexCollageManager.getCollageManager();
                            if (z5) {
                                collageManager.updateCollage(false, item);
                                z5 = false;
                            }
                            if (z) {
                                collageManager.updateCollage(true, item);
                            }
                            if (!item.hidden()) {
                                PreAssetCategoryAlias preAssetCategoryAlias = PreAssetCategoryAlias.Collage;
                                if (item.category() == Category.staticcollage) {
                                    preAssetCategoryAlias = PreAssetCategoryAlias.StaticCollage;
                                } else if (item.category() == Category.dynamiccollage) {
                                    preAssetCategoryAlias = PreAssetCategoryAlias.DynamicCollage;
                                }
                                hashSet.add(preAssetCategoryAlias);
                            }
                        }
                    }
                }
            }
            PreAssetCategoryAlias[] preAssetCategoryAliasArr = new PreAssetCategoryAlias[hashSet.size()];
            for (PreAssetCategoryAlias preAssetCategoryAlias2 : hashSet) {
                preAssetCategoryAliasArr[i2] = preAssetCategoryAlias2;
                i2++;
            }
            return preAssetCategoryAliasArr;
        }
    }

    private void removeUninstallItem(int i) {
        Iterator<Integer> it = this.uninstalledAssetIdxList.iterator();
        while (true) {
            if (it.hasNext()) {
                if (it.next().intValue() == i) {
                    it.remove();
                    break;
                }
            } else {
                break;
            }
        }
        this.uninstalledItems.remove(i);
        if (this.uninstalledAssetIdxList.size() > 5) {
            this.uninstalledItems.remove(this.uninstalledAssetIdxList.remove(0).intValue());
        }
    }

    public void putUninstallItem(int i) {
        synchronized (this.uninstalledAssetLock) {
            removeUninstallItem(i);
            List<Item> installedAssetItemsByAssetIDx = getInstalledAssetItemsByAssetIDx(i);
            if (installedAssetItemsByAssetIDx == null) {
                return;
            }
            this.uninstalledItems.put(i, installedAssetItemsByAssetIDx);
            this.uninstalledAssetIdxList.add(Integer.valueOf(i));
        }
    }

    public List<Item> popUninstallItems(int i) {
        List<Item> list;
        synchronized (this.uninstalledAssetLock) {
            list = this.uninstalledItems.get(i);
            removeUninstallItem(i);
        }
        return list;
    }
}
