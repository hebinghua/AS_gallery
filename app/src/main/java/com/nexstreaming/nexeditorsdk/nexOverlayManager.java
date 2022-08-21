package com.nexstreaming.nexeditorsdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.Log;
import com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader;
import com.nexstreaming.nexeditorsdk.nexAssetPackageManager;
import com.nexstreaming.nexeditorsdk.nexOverlayTitle;
import com.xiaomi.stat.a.j;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class nexOverlayManager {
    private static final String TAG = "nexOverlayManager";
    private static Context mAppContext = null;
    private static boolean sAutoAspectSelect = true;
    private static nexOverlayManager sSingleton;
    private List<Overlay> overlayEntries = new ArrayList();
    private List<Overlay> externalView_overlayEntries = null;
    private nexOverlayTitle overlayTitle = null;
    private Object m_overlayEntryLock = new Object();

    public static void setAutoSelectFromAspect(boolean z) {
        sAutoAspectSelect = z;
    }

    /* loaded from: classes3.dex */
    public static class Overlay extends nexAssetPackageManager.c {
        private float[] aspect;
        private String[] ids;
        private int maxAspect;
        private int selectAspect;
        private int tag;

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

        private Overlay(nexAssetPackageManager.Item item) {
            super(item);
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public String id() {
            if (nexOverlayManager.sAutoAspectSelect) {
                selectAspect();
            }
            return this.ids[this.selectAspect];
        }

        @Override // com.nexstreaming.nexeditorsdk.nexAssetPackageManager.c, com.nexstreaming.nexeditorsdk.nexAssetPackageManager.Item
        public String name(String str) {
            String assetName = packageInfo().assetName(str);
            return assetName != null ? assetName : super.name(str);
        }

        public String getId() {
            return id();
        }

        public String getName(String str) {
            return name(str);
        }

        public Bitmap getIcon() {
            return icon();
        }

        public Bitmap getThumbnail() {
            return thumbnail();
        }

        public float getRatio() {
            if (nexOverlayManager.sAutoAspectSelect) {
                selectAspect();
            }
            return this.aspect[this.selectAspect];
        }

        public boolean includeSubtitle() {
            String[] strArr = this.ids;
            if (strArr != null) {
                for (String str : strArr) {
                    if (str.contains(j.i)) {
                        return true;
                    }
                }
            }
            return false;
        }

        public int getTag() {
            return this.tag;
        }

        public void setTag(int i) {
            this.tag = i;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static Overlay promote(nexAssetPackageManager.Item item) {
            if (item.category() == nexAssetPackageManager.Category.overlay && item.type() == nexAssetPackageManager.ItemMethodType.ItemTemplate) {
                return new Overlay(item);
            }
            return null;
        }

        public float[] getSupportedAspects() {
            int i = this.maxAspect;
            float[] fArr = new float[i];
            for (int i2 = 0; i2 < i; i2++) {
                fArr[i2] = this.aspect[i2];
            }
            return fArr;
        }

        public void selectAspect(int i) {
            if (this.maxAspect <= i) {
                return;
            }
            this.selectAspect = i;
        }

        private void selectAspect() {
            float aspectRatio = nexApplicationConfig.getAspectRatio();
            float f = 3.0f;
            for (int i = 0; i < this.maxAspect; i++) {
                float f2 = aspectRatio - this.aspect[i];
                if (f2 < 0.0f) {
                    f2 *= -1.0f;
                }
                if (f > f2) {
                    this.selectAspect = i;
                    f = f2;
                }
            }
        }

        public void setAspect(String str) {
            if (this.maxAspect >= 5) {
                return;
            }
            if (this.aspect == null) {
                this.aspect = new float[5];
                this.ids = new String[5];
            }
            if (str.contains("9v16")) {
                this.aspect[this.maxAspect] = 0.5625f;
            } else if (str.contains("2v1")) {
                this.aspect[this.maxAspect] = 2.0f;
            } else if (str.contains("1v2")) {
                this.aspect[this.maxAspect] = 0.5f;
            } else if (str.contains("1v1")) {
                this.aspect[this.maxAspect] = 1.0f;
            } else {
                this.aspect[this.maxAspect] = 1.7777778f;
            }
            String[] strArr = this.ids;
            int i = this.maxAspect;
            strArr[i] = str;
            this.maxAspect = i + 1;
            selectAspect();
        }
    }

    /* loaded from: classes3.dex */
    public static class nexTitleInfo {
        private TextPaint cachePaint;
        private boolean changeText;
        private String fontID;
        private int fontSize;
        private String group;
        private int id;
        private int overlayHeight;
        private int overlayWidth;
        private int realMaxFontSize;
        private String text;
        private String textDesc;
        private int textMaxLen;

        private nexTitleInfo(int i, int i2, String str) {
            this.id = i;
            this.textMaxLen = i2;
            this.textDesc = str;
        }

        public int getId() {
            return this.id;
        }

        public String getText() {
            return this.text;
        }

        public void setText(String str) {
            this.text = str;
        }

        public String getTextDesc() {
            return this.textDesc;
        }

        public int getTextMaxLen() {
            return this.textMaxLen;
        }

        public String getFontID() {
            return this.fontID;
        }

        public void setFontID(String str) {
            this.fontID = str;
            this.changeText = true;
        }

        public int getFontSize() {
            return this.fontSize;
        }

        public void setFontSize(int i) {
            this.fontSize = i;
            this.changeText = true;
        }

        public int getOverlayWidth() {
            return this.overlayWidth;
        }

        public int getOverlayHeight() {
            return this.overlayHeight;
        }

        public int getMaxFontSize() {
            calcFontSize();
            return this.realMaxFontSize;
        }

        public void setOverlayResolution(int i, int i2) {
            this.overlayWidth = i;
            this.overlayHeight = i2;
        }

        public int getTextWidth(String str) {
            TextPaint textPaint = new TextPaint();
            textPaint.setTypeface(nexFont.getTypeface(nexOverlayManager.mAppContext, this.fontID));
            textPaint.setTextSize(this.fontSize);
            return (int) textPaint.measureText(str);
        }

        private synchronized void calcFontSize() {
            if (this.changeText || this.realMaxFontSize <= 0) {
                TextPaint textPaint = this.cachePaint;
                if (textPaint == null) {
                    this.cachePaint = new TextPaint();
                } else {
                    textPaint.reset();
                }
                int i = this.overlayHeight - 4;
                this.cachePaint.setTextSize(i);
                this.cachePaint.setTypeface(nexFont.getTypeface(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), this.fontID));
                this.cachePaint.setAntiAlias(true);
                this.cachePaint.setStyle(Paint.Style.FILL_AND_STROKE);
                while (((int) this.cachePaint.getFontSpacing()) >= this.overlayHeight - 4 && i >= 10) {
                    i -= 10;
                    this.cachePaint.setTextSize(i);
                }
                this.realMaxFontSize = i;
                this.changeText = false;
            }
        }

        public synchronized int getTextHeight(String str) {
            TextPaint textPaint = this.cachePaint;
            if (textPaint == null) {
                this.cachePaint = new TextPaint();
            } else {
                textPaint.reset();
            }
            this.cachePaint.setTextSize(this.fontSize);
            this.cachePaint.setTypeface(nexFont.getTypeface(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), this.fontID));
            this.cachePaint.setAntiAlias(true);
            this.cachePaint.setStyle(Paint.Style.FILL_AND_STROKE);
            return (int) this.cachePaint.getFontSpacing();
        }

        public String getGroup() {
            return this.group;
        }

        public void setGroup(String str) {
            this.group = str;
        }
    }

    private nexOverlayManager(Context context, Context context2) {
        mAppContext = context;
    }

    public static nexOverlayManager getOverlayManager(Context context, Context context2) {
        if (sSingleton != null && !mAppContext.getPackageName().equals(context.getPackageName())) {
            sSingleton = null;
        }
        if (sSingleton == null) {
            sSingleton = new nexOverlayManager(context, context2);
        }
        return sSingleton;
    }

    private Overlay getAssetOverlay(String str) {
        for (Overlay overlay : this.overlayEntries) {
            if (overlay.packageInfo().assetId().compareTo(str) == 0) {
                return overlay;
            }
        }
        return null;
    }

    private void resolveOverlay() {
        synchronized (this.m_overlayEntryLock) {
            this.overlayEntries.clear();
            for (nexAssetPackageManager.Item item : nexAssetPackageManager.getAssetPackageManager(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).getInstalledAssetItems(nexAssetPackageManager.Category.overlay)) {
                if (item.type() == nexAssetPackageManager.ItemMethodType.ItemTemplate) {
                    Overlay assetOverlay = getAssetOverlay(item.packageInfo().assetId());
                    if (assetOverlay == null) {
                        assetOverlay = Overlay.promote(item);
                        this.overlayEntries.add(assetOverlay);
                    }
                    assetOverlay.setAspect(item.id());
                }
            }
        }
    }

    public boolean updateOverlayTitle(boolean z, nexAssetPackageManager.Item item) {
        synchronized (this.m_overlayEntryLock) {
            if (z) {
                Overlay assetOverlay = getAssetOverlay(item.packageInfo().assetId());
                if (assetOverlay == null) {
                    assetOverlay = Overlay.promote(item);
                    this.overlayEntries.add(assetOverlay);
                }
                assetOverlay.setAspect(item.id());
            } else {
                Iterator<Overlay> it = this.overlayEntries.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    Overlay next = it.next();
                    if (next.packageInfo().assetId().compareTo(item.packageInfo().assetId()) == 0) {
                        this.overlayEntries.remove(next);
                        break;
                    }
                }
            }
        }
        return false;
    }

    public void loadOverlay() {
        resolveOverlay();
    }

    public List<Overlay> getOverlays(boolean z) {
        ArrayList arrayList;
        Log.d(TAG, "getOverlays total : " + this.overlayEntries.size() + " match : " + z);
        synchronized (this.m_overlayEntryLock) {
            arrayList = new ArrayList();
            float aspectRatio = nexApplicationConfig.getAspectRatio();
            for (Overlay overlay : this.overlayEntries) {
                if (z) {
                    if (overlay.getRatio() == aspectRatio) {
                        arrayList.add(overlay);
                    }
                } else {
                    arrayList.add(overlay);
                }
            }
        }
        return arrayList;
    }

    public List<Overlay> getOverlays() {
        Log.d(TAG, "getOverlays total : " + this.overlayEntries.size());
        if (this.externalView_overlayEntries == null) {
            this.externalView_overlayEntries = Collections.unmodifiableList(this.overlayEntries);
        }
        return this.externalView_overlayEntries;
    }

    public boolean parseOverlay(String str, final List<nexTitleInfo> list) {
        this.overlayTitle = new nexOverlayTitle();
        if (str != null) {
            Log.d(TAG, "parseOverlay with : " + str);
        }
        try {
            if (AssetPackageJsonToString(str) != null) {
                String a = this.overlayTitle.a(new JSONObject(AssetPackageJsonToString(str)), new nexOverlayTitle.TitleInfoListener() { // from class: com.nexstreaming.nexeditorsdk.nexOverlayManager.1
                    @Override // com.nexstreaming.nexeditorsdk.nexOverlayTitle.TitleInfoListener
                    public void OnTitleInfoListener(int i, String str2, int i2, String str3, int i3, String str4, String str5, int i4, int i5) {
                        if (list != null) {
                            nexTitleInfo nextitleinfo = new nexTitleInfo(i, i3, str4);
                            nextitleinfo.setText(str3);
                            nextitleinfo.setFontID(str2);
                            nextitleinfo.setFontSize(i2);
                            nextitleinfo.setGroup(str5);
                            nextitleinfo.setOverlayResolution(i4, i5);
                            list.add(nextitleinfo);
                        }
                    }
                });
                if (a == null) {
                    return true;
                }
                Log.d(TAG, "json parse failed" + a);
                list.clear();
                return false;
            }
            Log.d(TAG, "json create failed overlayid is null");
            return false;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "json create failed" + e.getMessage());
            return false;
        }
    }

    public boolean applyOverlayToProjectById(nexProject nexproject, String str, List<nexTitleInfo> list) {
        Log.d(TAG, "applyOverlayToProjectById with : " + str);
        parseOverlay(str, null);
        nexOverlayTitle nexoverlaytitle = this.overlayTitle;
        if (nexoverlaytitle == null) {
            Log.d(TAG, "Overlay data did not exist");
            return false;
        } else if (nexoverlaytitle.a(mAppContext, nexproject, list) == null) {
            return true;
        } else {
            Log.d(TAG, "Overlay apply failed");
            return false;
        }
    }

    public boolean clearOverlayToProject(nexProject nexproject) {
        nexOverlayTitle nexoverlaytitle = this.overlayTitle;
        if (nexoverlaytitle == null) {
            Log.d(TAG, "Overlay data did not exist");
            return false;
        } else if (nexoverlaytitle.a(nexproject) == null) {
            return true;
        } else {
            Log.d(TAG, "Overlay apply failed");
            return false;
        }
    }

    public void uninstallPackageById(String str) {
        nexAssetPackageManager.getAssetPackageManager(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).uninstallPackageById(str);
    }

    private String AssetPackageJsonToString(String str) {
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
}
