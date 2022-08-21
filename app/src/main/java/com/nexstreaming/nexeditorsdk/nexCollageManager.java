package com.nexstreaming.nexeditorsdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import com.google.gson_nex.Gson;
import com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader;
import com.nexstreaming.nexeditorsdk.exception.ExpiredTimeException;
import com.nexstreaming.nexeditorsdk.nexAssetPackageManager;
import com.nexstreaming.nexeditorsdk.nexSaveDataFormat;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class nexCollageManager {
    private static final String TAG = "nexCollageManager";
    private static nexCollageManager sSingleton;
    private Context mAppContext;
    private Context mResContext;
    private List<Collage> collageEntries = new ArrayList();
    private Object m_collageEntryLock = new Object();

    /* loaded from: classes3.dex */
    public enum CollageType {
        StaticCollage,
        DynamicCollage,
        ALL
    }

    /* loaded from: classes3.dex */
    public static class Collage extends nexAssetPackageManager.c {
        private nexCollage collage;

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public /* bridge */ /* synthetic */ nexAssetPackageManager.Category category() {
            return super.category();
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public /* bridge */ /* synthetic */ boolean hidden() {
            return super.hidden();
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public /* bridge */ /* synthetic */ Bitmap icon() {
            return super.icon();
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public /* bridge */ /* synthetic */ boolean isDelete() {
            return super.isDelete();
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public /* bridge */ /* synthetic */ nexAssetPackageManager.Asset packageInfo() {
            return super.packageInfo();
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public /* bridge */ /* synthetic */ Bitmap thumbnail() {
            return super.thumbnail();
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public /* bridge */ /* synthetic */ nexAssetPackageManager.ItemMethodType type() {
            return super.type();
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public /* bridge */ /* synthetic */ boolean validate() {
            return super.validate();
        }

        public Collage() {
        }

        private Collage(nexAssetPackageManager.Item item) {
            super(item);
        }

        public static Collage promote(nexAssetPackageManager.Item item) {
            if (item.category() == nexAssetPackageManager.Category.collage || item.category() == nexAssetPackageManager.Category.staticcollage || item.category() == nexAssetPackageManager.Category.dynamiccollage) {
                return new Collage(item);
            }
            return null;
        }

        public void setCollage(nexCollage nexcollage) {
            this.collage = nexcollage;
        }

        public nexCollage getCollage() {
            return this.collage;
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public String id() {
            return super.id();
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public String name(String str) {
            String assetName = packageInfo().assetName(str);
            return assetName != null ? assetName : super.name(str);
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public String[] getSupportedLocales() {
            return packageInfo() == null ? new String[0] : packageInfo().getSupportedLocales();
        }

        public List<nexCollageInfo> getCollageInfos() {
            if (this.collage == null || !parsingCollage()) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            for (a aVar : this.collage.f()) {
                if (!aVar.a()) {
                    arrayList.add(aVar);
                }
            }
            for (nexCollageTitleInfo nexcollagetitleinfo : this.collage.g()) {
                if (nexcollagetitleinfo.a()) {
                    arrayList.add(nexcollagetitleinfo);
                }
            }
            return arrayList;
        }

        public nexCollageInfo getCollageInfos(float f, float f2) {
            if (this.collage != null && parsingCollage()) {
                for (nexCollageTitleInfo nexcollagetitleinfo : this.collage.g()) {
                    if (nexcollagetitleinfo.a() && nexcollagetitleinfo.a(f, f2)) {
                        return nexcollagetitleinfo;
                    }
                }
                for (a aVar : this.collage.f()) {
                    if (!aVar.a() && aVar.a(f, f2)) {
                        return aVar;
                    }
                }
            }
            return null;
        }

        public CollageType getType() {
            nexAssetPackageManager.ItemMethodType type = type();
            if (type == nexAssetPackageManager.ItemMethodType.ItemDynamicCollage) {
                return CollageType.DynamicCollage;
            }
            if (type == nexAssetPackageManager.ItemMethodType.ItemStaticCollage) {
                return CollageType.StaticCollage;
            }
            if (this.collage != null && parsingCollage()) {
                return this.collage.d();
            }
            return null;
        }

        public int getDuration() {
            if (this.collage == null || !parsingCollage()) {
                return 0;
            }
            return this.collage.c();
        }

        public int getEditTime() {
            if (this.collage == null || !parsingCollage()) {
                return 0;
            }
            return (int) (this.collage.c() * this.collage.a());
        }

        public float getRatio() {
            if (this.collage == null || !parsingCollage()) {
                return 0.0f;
            }
            return this.collage.e();
        }

        public int getRatioMode() {
            if (this.collage == null || !parsingCollage()) {
                return 1;
            }
            float e = this.collage.e();
            if (e == 1.0f) {
                return 2;
            }
            if (e == 1.7777778f) {
                return 1;
            }
            if (e == 0.5625f) {
                return 3;
            }
            if (e == 2.0f) {
                return 4;
            }
            if (e == 0.5f) {
                return 5;
            }
            if (e == 1.3333334f) {
                return 6;
            }
            return e == 0.75f ? 7 : 0;
        }

        public boolean applyCollage2Project(nexProject nexproject, nexEngine nexengine, int i, Context context) throws ExpiredTimeException {
            if (nexAssetPackageManager.checkExpireAsset(packageInfo())) {
                throw new ExpiredTimeException(id());
            }
            if (this.collage == null || !parsingCollage()) {
                return false;
            }
            String a = this.collage.a(nexproject, nexengine, i, context, false);
            if (a == null) {
                return true;
            }
            Log.d(nexCollageManager.TAG, "applyCollage2Project failed with " + a);
            return false;
        }

        public boolean loadCollage2Project(nexProject nexproject, nexEngine nexengine, int i, Context context) {
            if (this.collage == null || !parsingCollage()) {
                return false;
            }
            String a = this.collage.a(nexproject, nexengine, i, context, true);
            if (a == null) {
                return true;
            }
            Log.d(nexCollageManager.TAG, "applyCollage2Project failed with " + a);
            return false;
        }

        public boolean restoreBGM() {
            nexCollage nexcollage = this.collage;
            if (nexcollage == null) {
                return false;
            }
            return nexcollage.a((String) null);
        }

        public boolean isFrameCollage() {
            nexCollage nexcollage = this.collage;
            if (nexcollage == null) {
                return false;
            }
            return nexcollage.h();
        }

        public boolean swapDrawInfoClip(nexCollageInfoDraw nexcollageinfodraw, nexCollageInfoDraw nexcollageinfodraw2) {
            nexCollage nexcollage = this.collage;
            if (nexcollage == null) {
                return false;
            }
            return nexcollage.a((a) nexcollageinfodraw, (a) nexcollageinfodraw2);
        }

        public String saveToString() {
            if (this.collage == null) {
                return null;
            }
            return new Gson().toJson(this.collage.j());
        }

        public boolean loadFromSaveData(Context context, nexEngine nexengine, nexProject nexproject, String str) {
            nexSaveDataFormat nexsavedataformat;
            nexSaveDataFormat.nexCollageOf nexcollageof;
            if (this.collage == null || (nexcollageof = (nexsavedataformat = (nexSaveDataFormat) new Gson().fromJson(str, (Class<Object>) nexSaveDataFormat.class)).collage) == null || nexsavedataformat.project == null) {
                return false;
            }
            this.collage.a(nexcollageof);
            loadCollage2Project(nexproject, nexengine, getDuration(), context);
            nexengine.setProject(nexproject);
            return true;
        }

        public int getSourceCount() {
            if (id().contains(".sc.")) {
                String id = id();
                String substring = id.substring(id.indexOf(".sc.") + 4, id.length());
                if (substring != null && substring.length() > 0) {
                    return Integer.parseInt(substring);
                }
            }
            if (this.collage == null || !parsingCollage()) {
                return 0;
            }
            return this.collage.b();
        }

        private boolean parsingCollage() {
            if (!this.collage.i()) {
                try {
                    if (nexCollageManager.AssetPackageJsonToString(id()) == null) {
                        return true;
                    }
                    String a = this.collage.a(new JSONObject(nexCollageManager.AssetPackageJsonToString(id())));
                    if (a == null) {
                        return true;
                    }
                    Log.d(nexCollageManager.TAG, "collage parsing error" + a);
                    return false;
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(nexCollageManager.TAG, "Collage parsing error" + e.getMessage());
                    return false;
                }
            }
            return true;
        }
    }

    public List<Collage> getCollages(int i, CollageType collageType) {
        ArrayList arrayList = new ArrayList();
        for (Collage collage : this.collageEntries) {
            if (collage.getSourceCount() == i) {
                if (collageType == CollageType.ALL) {
                    arrayList.add(collage);
                }
                if (collage.getType() == collageType) {
                    arrayList.add(collage);
                }
            }
        }
        return arrayList;
    }

    public List<Collage> getCollages(CollageType collageType) {
        ArrayList arrayList = new ArrayList();
        for (Collage collage : this.collageEntries) {
            if (collageType == CollageType.ALL) {
                arrayList.add(collage);
            }
            if (collage.getType() == collageType) {
                arrayList.add(collage);
            }
        }
        return arrayList;
    }

    public List<Collage> getCollages() {
        ArrayList arrayList = new ArrayList();
        for (Collage collage : this.collageEntries) {
            arrayList.add(collage);
        }
        return arrayList;
    }

    public Collage getCollage(String str) {
        synchronized (this.m_collageEntryLock) {
            for (Collage collage : this.collageEntries) {
                if (collage.id() != null && collage.id().compareTo(str) == 0) {
                    return collage;
                }
            }
            return null;
        }
    }

    private nexCollageManager(Context context, Context context2) {
        this.mAppContext = context;
        this.mResContext = context2;
    }

    public static nexCollageManager getCollageManager(Context context, Context context2) {
        nexCollageManager nexcollagemanager = sSingleton;
        if (nexcollagemanager != null && !nexcollagemanager.mAppContext.getPackageName().equals(context.getPackageName())) {
            sSingleton = null;
        }
        if (sSingleton == null) {
            sSingleton = new nexCollageManager(context, context2);
        }
        return sSingleton;
    }

    public static nexCollageManager getCollageManager() {
        return sSingleton;
    }

    private boolean resolveCollage(boolean z) {
        synchronized (this.m_collageEntryLock) {
            this.collageEntries.clear();
            for (nexAssetPackageManager.Item item : nexAssetPackageManager.getAssetPackageManager(this.mAppContext).getInstalledAssetItems(nexAssetPackageManager.Category.collage)) {
                if (!item.hidden()) {
                    if (z) {
                        nexAssetPackageManager.getAssetPackageManager(this.mAppContext);
                        if (nexAssetPackageManager.checkExpireAsset(item.packageInfo())) {
                        }
                    }
                    Collage promote = Collage.promote(item);
                    if (promote != null) {
                        promote.setCollage(new nexCollage());
                        this.collageEntries.add(promote);
                    }
                }
            }
            for (nexAssetPackageManager.Item item2 : nexAssetPackageManager.getAssetPackageManager(this.mAppContext).getInstalledAssetItems(nexAssetPackageManager.Category.staticcollage)) {
                if (!item2.hidden()) {
                    if (z) {
                        nexAssetPackageManager.getAssetPackageManager(this.mAppContext);
                        if (nexAssetPackageManager.checkExpireAsset(item2.packageInfo())) {
                        }
                    }
                    Collage promote2 = Collage.promote(item2);
                    if (promote2 != null) {
                        promote2.setCollage(new nexCollage());
                        this.collageEntries.add(promote2);
                    }
                }
            }
            for (nexAssetPackageManager.Item item3 : nexAssetPackageManager.getAssetPackageManager(this.mAppContext).getInstalledAssetItems(nexAssetPackageManager.Category.dynamiccollage)) {
                if (!item3.hidden()) {
                    if (z) {
                        nexAssetPackageManager.getAssetPackageManager(this.mAppContext);
                        if (nexAssetPackageManager.checkExpireAsset(item3.packageInfo())) {
                        }
                    }
                    Collage promote3 = Collage.promote(item3);
                    if (promote3 != null) {
                        promote3.setCollage(new nexCollage());
                        this.collageEntries.add(promote3);
                    }
                }
            }
        }
        return true;
    }

    public boolean loadCollage() {
        return resolveCollage(false);
    }

    public boolean loadCollage(boolean z) {
        return resolveCollage(z);
    }

    public void uninstallPackageById(String str) {
        nexAssetPackageManager.getAssetPackageManager(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).uninstallPackageById(str);
    }

    public int findNewPackages() {
        return nexAssetPackageManager.getAssetPackageManager(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).findNewPackages();
    }

    public boolean isInstallingPackages() {
        return nexAssetPackageManager.getAssetPackageManager(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).isInstallingPackages();
    }

    public void installPackagesAsync(nexAssetPackageManager.OnInstallPackageListener onInstallPackageListener) {
        nexAssetPackageManager.getAssetPackageManager(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).installPackagesAsync(onInstallPackageListener);
    }

    public void installPackagesAsync(int i, nexAssetPackageManager.OnInstallPackageListener onInstallPackageListener) {
        nexAssetPackageManager.getAssetPackageManager(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).installPackagesAsync(i, onInstallPackageListener);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String AssetPackageJsonToString(String str) {
        com.nexstreaming.app.common.nexasset.assetpackage.f c = com.nexstreaming.app.common.nexasset.assetpackage.c.a().c(str);
        if (c == null) {
            Log.e(TAG, "AssetPackageJsonToString info fail=" + str);
            return null;
        } else if (com.nexstreaming.app.common.nexasset.assetpackage.c.a().a(c.getAssetPackage())) {
            Log.e(TAG, "AssetPackageJsonToString expire id=" + str);
            return null;
        } else {
            try {
                AssetPackageReader a = AssetPackageReader.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), c.getPackageURI(), c.getAssetPackage().getAssetId());
                try {
                    try {
                        InputStream a2 = a.a(c.getFilePath());
                        if (a2 != null) {
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(a2));
                            StringBuilder sb = new StringBuilder();
                            while (true) {
                                String readLine = bufferedReader.readLine();
                                if (readLine != null) {
                                    sb.append(readLine);
                                    sb.append("\n");
                                } else {
                                    a2.close();
                                    return sb.toString();
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                } finally {
                    com.nexstreaming.app.common.util.b.a(a);
                }
            } catch (IOException e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }

    public String getCollageItemId(boolean z, int i, int i2) {
        if (z) {
            loadCollage();
        }
        if (i < 2) {
            return null;
        }
        for (Collage collage : this.collageEntries) {
            if (collage.packageInfo().assetIdx() == i && collage.getSourceCount() == i2) {
                return collage.id();
            }
        }
        return null;
    }

    public boolean updateCollage(boolean z, nexAssetPackageManager.Item item) {
        synchronized (this.m_collageEntryLock) {
            boolean z2 = false;
            if (z) {
                if (item.hidden()) {
                    return false;
                }
                Collage promote = Collage.promote(item);
                if (promote != null) {
                    try {
                        if (AssetPackageJsonToString(promote.id()) != null) {
                            JSONObject jSONObject = new JSONObject(AssetPackageJsonToString(promote.id()));
                            nexCollage nexcollage = new nexCollage();
                            String a = nexcollage.a(jSONObject);
                            if (a != null) {
                                Log.d(TAG, "collage parse error" + a);
                            } else {
                                promote.setCollage(nexcollage);
                                this.collageEntries.add(promote);
                                z2 = true;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, "json create failed" + e.getMessage());
                    }
                }
            } else {
                ArrayList arrayList = new ArrayList();
                for (Collage collage : this.collageEntries) {
                    if (collage.packageInfo().assetIdx() == item.packageInfo().assetIdx()) {
                        arrayList.add(collage);
                        z2 = true;
                    }
                }
                this.collageEntries.removeAll(arrayList);
            }
            return z2;
        }
    }
}
