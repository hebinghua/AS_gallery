package com.nexstreaming.nexeditorsdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import com.nexstreaming.nexeditorsdk.nexAssetPackageManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes3.dex */
public class nexAssetMediaManager {
    private static final String TAG = "nexAudioManager";
    private static Context mAppContext;
    private static nexAssetMediaManager sSingleton;
    private List<AssetMedia> mediaEntries = new ArrayList();
    private List<AssetMedia> externalView_mediaEntries = null;
    private Object m_mediaentryLock = new Object();

    /* loaded from: classes3.dex */
    public static class AssetMedia extends nexAssetPackageManager.c {
        private boolean filter;
        private boolean getPath;
        private boolean getpreloadedAssetPath;
        private String mediaPath;
        private String preloadedAssetPath;
        private int type;

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public /* bridge */ /* synthetic */ nexAssetPackageManager.Category category() {
            return super.category();
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public /* bridge */ /* synthetic */ String[] getSupportedLocales() {
            return super.getSupportedLocales();
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
        public /* bridge */ /* synthetic */ String id() {
            return super.id();
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public /* bridge */ /* synthetic */ boolean isDelete() {
            return super.isDelete();
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public /* bridge */ /* synthetic */ String name(String str) {
            return super.name(str);
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

        public AssetMedia(nexAssetPackageManager.Item item) {
            super(item);
        }

        public static AssetMedia promote(nexAssetPackageManager.Item item) {
            AssetMedia assetMedia = new AssetMedia(item);
            assetMedia.type = 3;
            assetMedia.filter = false;
            return assetMedia;
        }

        public String getPath() {
            if (!this.getPath) {
                this.mediaPath = nexAssetPackageManager.getAssetPackageMediaPath(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), id());
                this.getPath = true;
            }
            return this.mediaPath;
        }

        public int getClipType() {
            return this.type;
        }

        public boolean filter() {
            return this.filter;
        }

        public Bitmap getImageThumbnail() {
            com.nexstreaming.app.common.nexasset.assetpackage.f c;
            if (this.type == 1 && (c = com.nexstreaming.app.common.nexasset.assetpackage.c.a().c(id())) != null) {
                try {
                    return com.nexstreaming.app.common.nexasset.assetpackage.e.b(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), c, 0, 0);
                } catch (IOException unused) {
                    return null;
                }
            }
            return null;
        }

        public String getPreloadedAssetPath() {
            if (!this.getpreloadedAssetPath) {
                this.preloadedAssetPath = nexAssetPackageManager.getPreloadedMediaAppAssetPath(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), id());
                this.getpreloadedAssetPath = true;
            }
            return this.preloadedAssetPath;
        }
    }

    private nexAssetMediaManager(Context context) {
        mAppContext = context;
    }

    public static nexAssetMediaManager getAudioManager(Context context) {
        if (sSingleton != null && !mAppContext.getPackageName().equals(context.getPackageName())) {
            sSingleton = null;
        }
        if (sSingleton == null) {
            sSingleton = new nexAssetMediaManager(context);
        }
        return sSingleton;
    }

    private void resolveMedia(int i, boolean z) {
        synchronized (this.m_mediaentryLock) {
            this.mediaEntries.clear();
            if (i == 3) {
                for (nexAssetPackageManager.Item item : nexAssetPackageManager.getAssetPackageManager(mAppContext).getInstalledAssetItems(nexAssetPackageManager.Category.audio)) {
                    if (!item.hidden()) {
                        if (z) {
                            nexAssetPackageManager.getAssetPackageManager(mAppContext);
                            if (nexAssetPackageManager.checkExpireAsset(item.packageInfo())) {
                            }
                        }
                        AssetMedia assetMedia = new AssetMedia(item);
                        assetMedia.type = 3;
                        assetMedia.filter = false;
                        this.mediaEntries.add(assetMedia);
                    }
                }
            }
        }
    }

    public void loadMedia(int i) {
        resolveMedia(i, false);
    }

    public void loadMedia(int i, boolean z) {
        resolveMedia(i, z);
    }

    public String[] getAssetMediaIds(int i) {
        String[] strArr;
        synchronized (this.m_mediaentryLock) {
            ArrayList arrayList = new ArrayList();
            for (AssetMedia assetMedia : this.mediaEntries) {
                if (assetMedia.getClipType() == 3) {
                    arrayList.add(assetMedia.id());
                }
            }
            int size = arrayList.size();
            strArr = new String[size];
            for (int i2 = 0; i2 < size; i2++) {
                strArr[i2] = (String) arrayList.get(i2);
            }
        }
        return strArr;
    }

    public AssetMedia getAssetMedia(String str) {
        synchronized (this.m_mediaentryLock) {
            for (AssetMedia assetMedia : this.mediaEntries) {
                if (assetMedia.id().compareTo(str) == 0) {
                    return assetMedia;
                }
            }
            return null;
        }
    }

    public List<AssetMedia> getAssetMedias() {
        if (this.externalView_mediaEntries == null) {
            this.externalView_mediaEntries = Collections.unmodifiableList(this.mediaEntries);
        }
        return this.externalView_mediaEntries;
    }

    public AssetMedia[] getAssetMedias(int i) {
        AssetMedia[] assetMediaArr;
        synchronized (this.m_mediaentryLock) {
            ArrayList arrayList = new ArrayList();
            for (AssetMedia assetMedia : this.mediaEntries) {
                if (assetMedia.getClipType() == i) {
                    arrayList.add(assetMedia);
                }
            }
            int size = arrayList.size();
            assetMediaArr = new AssetMedia[size];
            for (int i2 = 0; i2 < size; i2++) {
                assetMediaArr[i2] = (AssetMedia) arrayList.get(i2);
            }
        }
        return assetMediaArr;
    }

    public nexClip createAudioClip(String str) {
        AssetMedia assetMedia = getAssetMedia(str);
        if (assetMedia == null) {
            return null;
        }
        return nexClip.getSupportedClip(assetMedia.getPath());
    }

    public void applyProjectBGM(nexProject nexproject, String str) {
        String str2;
        AssetMedia assetMedia = getAssetMedia(str);
        if (assetMedia != null) {
            if (nexAssetPackageManager.checkExpireAsset(assetMedia.packageInfo())) {
                Log.d(TAG, "applyProjectBGM expire Id=" + str);
                return;
            } else if (assetMedia.getClipType() == 3) {
                str2 = assetMedia.getPath();
                nexproject.setBackgroundMusicPath(str2);
            }
        }
        str2 = null;
        nexproject.setBackgroundMusicPath(str2);
    }

    public void uninstallPackageById(String str) {
        nexAssetPackageManager.getAssetPackageManager(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).uninstallPackageById(str);
    }

    public boolean updateMedia(boolean z, int i, boolean z2, nexAssetPackageManager.Item item) {
        synchronized (this.m_mediaentryLock) {
            Log.d(TAG, "updateMedia(" + z + "," + i + "," + item.packageInfo().assetIdx() + ")");
            if (z) {
                if (!item.hidden()) {
                    AssetMedia assetMedia = new AssetMedia(item);
                    assetMedia.type = i;
                    assetMedia.filter = z2;
                    this.mediaEntries.add(assetMedia);
                }
            } else {
                Iterator<AssetMedia> it = this.mediaEntries.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    AssetMedia next = it.next();
                    if (next.id().compareTo(item.id()) == 0) {
                        this.mediaEntries.remove(next);
                        break;
                    }
                }
            }
        }
        return false;
    }
}
