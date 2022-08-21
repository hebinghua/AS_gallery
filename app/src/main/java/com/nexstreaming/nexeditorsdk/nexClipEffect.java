package com.nexstreaming.nexeditorsdk;

import android.content.Context;
import android.graphics.Bitmap;
import com.nexstreaming.app.common.util.n;
import com.nexstreaming.nexeditorsdk.exception.InvalidRangeException;
import com.nexstreaming.nexeditorsdk.nexSaveDataFormat;
import java.io.IOException;

/* loaded from: classes3.dex */
public final class nexClipEffect extends nexEffect implements Cloneable {
    private int mShowEndTime;
    private int mShowStartTime;

    public static nexClipEffect clone(nexClipEffect nexclipeffect) {
        nexClipEffect nexclipeffect2 = null;
        try {
            nexClipEffect nexclipeffect3 = (nexClipEffect) nexclipeffect.clone();
            try {
                nexclipeffect3.mShowEndTime = nexclipeffect.mShowEndTime;
                nexclipeffect3.mShowStartTime = nexclipeffect.mShowStartTime;
                return nexclipeffect3;
            } catch (CloneNotSupportedException e) {
                e = e;
                nexclipeffect2 = nexclipeffect3;
                e.printStackTrace();
                return nexclipeffect2;
            }
        } catch (CloneNotSupportedException e2) {
            e = e2;
        }
    }

    public nexClipEffect() {
        this.mShowStartTime = 0;
        this.mShowEndTime = 10000;
        this.mType = 1;
    }

    public nexClipEffect(String str) {
        this.mShowStartTime = 0;
        this.mShowEndTime = 10000;
        this.mID = str;
        this.mType = 2;
    }

    public void setEffect(String str) {
        super.setEffect(str, 2);
    }

    public void setTitle(String str) {
        this.mUpdated = true;
        setTitle(0, str);
    }

    public String getTitle() {
        return getTitle(0);
    }

    public String getName(Context context) {
        int i = this.mType;
        if (i == 0) {
            return "None Clip Effect";
        }
        if (i == 1) {
            return "Theme Clip Effect";
        }
        com.nexstreaming.app.common.nexasset.assetpackage.f c = com.nexstreaming.app.common.nexasset.assetpackage.c.a().c(this.mID);
        if (c != null) {
            if (context != null) {
                String a = n.a(context, c.getLabel());
                this.mName = a;
                if (a == null) {
                    this.mName = c.getLabel().get("en");
                }
            } else {
                this.mName = c.getLabel().get("en");
            }
        }
        return this.mName;
    }

    public String getDesc(Context context) {
        com.nexstreaming.app.common.nexasset.assetpackage.f c;
        int i = this.mType;
        if (i == 0) {
            return "None Clip Effect";
        }
        if (i == 1) {
            return "Theme Clip Effect";
        }
        if (context != null && (c = com.nexstreaming.app.common.nexasset.assetpackage.c.a().c(this.mID)) != null) {
            return c.getLabel().get("en");
        }
        return null;
    }

    public Bitmap getIcon() {
        com.nexstreaming.app.common.nexasset.assetpackage.f c;
        if (this.mID == null || (c = com.nexstreaming.app.common.nexasset.assetpackage.c.a().c(this.mID)) == null) {
            return null;
        }
        try {
            return com.nexstreaming.app.common.nexasset.assetpackage.e.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), c, 0, 0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setShowStartTime(int i) {
        if (this.mShowEndTime < i) {
            throw new InvalidRangeException(i, this.mShowEndTime);
        }
        this.mShowStartTime = i;
    }

    public void setShowEndTime(int i) {
        if (this.mShowStartTime > i) {
            throw new InvalidRangeException(this.mShowStartTime, i);
        }
        this.mShowEndTime = i;
    }

    public void setEffectShowTime(int i, int i2) {
        if (i2 < i) {
            throw new InvalidRangeException(i, i2);
        }
        this.mShowStartTime = i;
        this.mShowEndTime = i2;
    }

    public int getShowStartTime() {
        return this.mShowStartTime;
    }

    public int getShowEndTime() {
        return this.mShowEndTime;
    }

    public void setAutoTheme() {
        this.mType = 1;
    }

    public String getCategoryTitle(Context context) {
        int i = this.mType;
        if (i == 0) {
            return "None";
        }
        if (i == 1) {
            return "Theme";
        }
        com.nexstreaming.app.common.nexasset.assetpackage.f c = com.nexstreaming.app.common.nexasset.assetpackage.c.a().c(this.mID);
        if (c == null) {
            return null;
        }
        return c.getCategory().toString();
    }

    public nexSaveDataFormat.nexEffectOf getSaveData() {
        nexSaveDataFormat.nexEffectOf nexeffectof = new nexSaveDataFormat.nexEffectOf();
        nexeffectof.mID = this.mID;
        nexeffectof.mAutoID = this.mAutoID;
        nexeffectof.mName = this.mName;
        nexeffectof.mType = this.mType;
        nexeffectof.mDuration = this.mDuration;
        nexeffectof.itemMethodType = this.itemMethodType;
        nexeffectof.mIsResolveOptions = this.mIsResolveOptions;
        nexeffectof.mOptionsUpdate = this.mOptionsUpdate;
        nexeffectof.m_effectOptions = this.m_effectOptions;
        nexeffectof.mShowStartTime = this.mShowStartTime;
        nexeffectof.mShowEndTime = this.mShowEndTime;
        nexeffectof.mMinDuration = 0;
        nexeffectof.mMaxDuration = 0;
        nexeffectof.mEffectOffset = 0;
        nexeffectof.mEffectOverlap = 0;
        return nexeffectof;
    }

    public nexClipEffect(nexSaveDataFormat.nexEffectOf nexeffectof) {
        this.mShowStartTime = 0;
        this.mShowEndTime = 10000;
        this.mID = nexeffectof.mID;
        this.mAutoID = nexeffectof.mAutoID;
        this.mName = nexeffectof.mName;
        this.mType = nexeffectof.mType;
        this.mDuration = nexeffectof.mDuration;
        this.itemMethodType = nexeffectof.itemMethodType;
        this.mIsResolveOptions = nexeffectof.mIsResolveOptions;
        this.mOptionsUpdate = nexeffectof.mOptionsUpdate;
        this.m_effectOptions = nexeffectof.m_effectOptions;
        this.mShowStartTime = nexeffectof.mShowStartTime;
        this.mShowEndTime = nexeffectof.mShowEndTime;
    }
}
