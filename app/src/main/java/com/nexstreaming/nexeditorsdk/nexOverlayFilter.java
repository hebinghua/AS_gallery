package com.nexstreaming.nexeditorsdk;

import android.graphics.Bitmap;
import android.graphics.Rect;
import com.nexstreaming.app.common.nexasset.overlay.OverlayAsset;
import com.nexstreaming.app.common.nexasset.overlay.OverlayAssetFactory;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes3.dex */
public class nexOverlayFilter {
    private static final int kMaxStringTrackCount = 8;
    private OverlayAsset cachedOverlayAsset;
    private String id;
    private nexEffectOptions option;
    private boolean updateBound;
    private boolean updateOption;
    private String cacheEncodeOption = "";
    private int width = 700;
    private int height = 700;
    private float relativeWidth = 1.0f;
    private float relativeHeight = 1.0f;
    private boolean isRelativeWidth = true;
    private boolean isRelativeHeight = true;
    private String[] mTitles = null;

    public nexOverlayFilter(String str) {
        this.id = str;
    }

    public String getId() {
        return this.id;
    }

    public String getName(String str) {
        com.nexstreaming.app.common.nexasset.assetpackage.f c = com.nexstreaming.app.common.nexasset.assetpackage.c.a().c(this.id);
        if (c != null) {
            return c.getLabel().get("en");
        }
        return null;
    }

    public Bitmap getIcon() {
        com.nexstreaming.app.common.nexasset.assetpackage.f c;
        if (this.id == null || (c = com.nexstreaming.app.common.nexasset.assetpackage.c.a().c(this.id)) == null) {
            return null;
        }
        try {
            return com.nexstreaming.app.common.nexasset.assetpackage.e.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), c, 0, 0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setTitle(int i, String str) {
        if (this.mTitles == null) {
            this.mTitles = new String[8];
        }
        if (i < 8) {
            this.mTitles[i] = str;
        }
    }

    public String getTitle(int i) {
        String[] strArr = this.mTitles;
        if (strArr != null && i < 8) {
            return strArr[i];
        }
        return null;
    }

    public String[] getTitles() {
        return this.mTitles;
    }

    public void setWidth(int i) {
        this.isRelativeWidth = false;
        if (this.width != i) {
            this.updateBound = true;
        }
        this.width = i;
    }

    public void setHeight(int i) {
        this.isRelativeHeight = false;
        if (this.height != i) {
            this.updateBound = true;
        }
        this.height = i;
    }

    public int getWidth() {
        if (!this.isRelativeWidth) {
            return this.width;
        }
        return (int) (nexApplicationConfig.getAspectProfile().getWidth() * this.relativeWidth);
    }

    public int getHeight() {
        if (!this.isRelativeWidth) {
            return this.height;
        }
        return (int) (nexApplicationConfig.getAspectProfile().getHeight() * this.relativeHeight);
    }

    public float getRelativeWidth() {
        return this.relativeWidth;
    }

    public void setRelativeWidth(float f) {
        this.isRelativeWidth = true;
        if (this.relativeWidth != f) {
            this.updateBound = true;
        }
        this.relativeWidth = f;
    }

    public float getRelativeHeight() {
        return this.relativeHeight;
    }

    public void setRelativeHeight(float f) {
        this.isRelativeHeight = true;
        if (this.relativeHeight != f) {
            this.updateBound = true;
        }
        this.relativeHeight = f;
    }

    public nexEffectOptions getEffectOption() {
        if (this.option == null) {
            nexEffectOptions effectOptions = nexEffectLibrary.getEffectLibrary(com.nexstreaming.kminternal.kinemaster.config.a.a().b()).getEffectOptions(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), this.id);
            this.option = effectOptions;
            effectOptions.setEffectType(5);
        }
        this.updateOption = true;
        return this.option;
    }

    public OverlayAsset getOverlayAssetFilter() throws IOException, XmlPullParserException {
        if (this.cachedOverlayAsset == null) {
            this.cachedOverlayAsset = OverlayAssetFactory.forItem(this.id);
        }
        return this.cachedOverlayAsset;
    }

    public String getEncodedEffectOptions() {
        nexEffectOptions nexeffectoptions = this.option;
        if (nexeffectoptions == null) {
            return "";
        }
        if (this.updateOption) {
            this.updateOption = false;
            nexeffectoptions.clearUpadted();
            this.cacheEncodeOption = nexEffect.getTitleOptions(this.option);
        }
        return this.cacheEncodeOption;
    }

    public void getBound(Rect rect) {
        rect.left = 0 - (getWidth() / 2);
        rect.top = 0 - (getHeight() / 2);
        rect.right = (getWidth() / 2) + 0;
        rect.bottom = (getHeight() / 2) + 0;
    }

    public boolean isUpdated() {
        if (this.updateBound) {
            return true;
        }
        nexEffectOptions nexeffectoptions = this.option;
        if (nexeffectoptions != null) {
            return nexeffectoptions.isUpdated();
        }
        return false;
    }
}
