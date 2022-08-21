package com.nexstreaming.nexeditorsdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;
import com.nexstreaming.app.common.nexasset.assetpackage.h;
import com.nexstreaming.app.common.nexasset.assetpackage.i;
import com.nexstreaming.app.common.util.n;
import com.nexstreaming.nexeditorsdk.nexSaveDataFormat;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes3.dex */
public final class nexTransitionEffect extends nexEffect implements Cloneable {
    private static final int TRANSITION_MAX_DURATION = 30000;
    private static final int TRANSITON_MIN_DURATION = 500;
    private static LruCache<String, a> s_cachedProperty = new LruCache<>(100);
    public int mEffectOffset;
    public int mEffectOverlap;
    private int mMaxDuration;
    private int mMinDuration;

    /* loaded from: classes3.dex */
    public class a {
        public int a;
        public int b;

        public a(int i, int i2) {
            this.a = i;
            this.b = i2;
        }
    }

    public static nexTransitionEffect clone(nexTransitionEffect nextransitioneffect) {
        nexTransitionEffect nextransitioneffect2;
        nexTransitionEffect nextransitioneffect3 = null;
        try {
            nextransitioneffect2 = (nexTransitionEffect) nextransitioneffect.clone();
        } catch (CloneNotSupportedException e) {
            e = e;
        }
        try {
            nextransitioneffect2.mMinDuration = nextransitioneffect.mMinDuration;
            nextransitioneffect2.mMaxDuration = nextransitioneffect.mMaxDuration;
            nextransitioneffect2.mEffectOffset = nextransitioneffect.mEffectOffset;
            nextransitioneffect2.mEffectOverlap = nextransitioneffect.mEffectOverlap;
            return nextransitioneffect2;
        } catch (CloneNotSupportedException e2) {
            e = e2;
            nextransitioneffect3 = nextransitioneffect2;
            e.printStackTrace();
            return nextransitioneffect3;
        }
    }

    public nexTransitionEffect() {
        this.mType = 3;
        this.mDuration = 2000;
    }

    public nexTransitionEffect(b bVar) {
        this.mType = 3;
        this.mDuration = 2000;
        setObserver(bVar);
    }

    public nexTransitionEffect(String str) {
        setTransitionEffect(str);
        this.mDuration = 2000;
    }

    public void setTransitionEffect(String str) {
        if (setEffect(str, 4)) {
            setModified(false);
            if (str.compareTo("none") == 0) {
                this.mEffectOffset = 0;
                this.mEffectOverlap = 0;
                return;
            }
            a aVar = s_cachedProperty.get(str);
            if (aVar == null) {
                h hVar = null;
                try {
                    hVar = i.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), str);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e2) {
                    e2.printStackTrace();
                }
                if (hVar == null) {
                    this.mEffectOffset = 0;
                    this.mEffectOverlap = 0;
                } else {
                    this.mEffectOffset = hVar.b();
                    this.mEffectOverlap = hVar.c();
                }
                s_cachedProperty.put(str, new a(this.mEffectOffset, this.mEffectOverlap));
            } else {
                this.mEffectOffset = aVar.a;
                this.mEffectOverlap = aVar.b;
            }
            this.mMinDuration = 500;
            this.mMaxDuration = 30000;
        }
    }

    public void setAutoTheme() {
        if (this.mType != 3) {
            this.mUpdated = true;
            setModified(false);
        }
        this.mType = 3;
    }

    public int getMinDuration() {
        return this.mMinDuration;
    }

    public int getMaxDuration() {
        return this.mMaxDuration;
    }

    public int getOffset() {
        return this.mEffectOffset;
    }

    public int getOverlap() {
        return this.mEffectOverlap;
    }

    public String getName(Context context) {
        int i = this.mType;
        if (i == 0) {
            return "None Transition";
        }
        if (i == 3) {
            return "Theme Transition";
        }
        com.nexstreaming.app.common.nexasset.assetpackage.f c = com.nexstreaming.app.common.nexasset.assetpackage.c.a().c(this.mID);
        if (c != null) {
            if (context != null) {
                String a2 = n.a(context, c.getLabel());
                this.mName = a2;
                if (a2 == null) {
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
            return "None Transition Effect";
        }
        if (i == 3) {
            return "Theme Transition Effect";
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

    public String getCategoryTitle(Context context) {
        com.nexstreaming.app.common.nexasset.assetpackage.f c;
        int i = this.mType;
        if (i == 0) {
            return "None";
        }
        if (i == 3) {
            return "Theme";
        }
        if (context != null && (c = com.nexstreaming.app.common.nexasset.assetpackage.c.a().c(this.mID)) != null) {
            return c.getCategory().toString();
        }
        return null;
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
        nexeffectof.mShowStartTime = 0;
        nexeffectof.mShowEndTime = 0;
        nexeffectof.mMinDuration = this.mMinDuration;
        nexeffectof.mMaxDuration = this.mMaxDuration;
        nexeffectof.mEffectOffset = this.mEffectOffset;
        nexeffectof.mEffectOverlap = this.mEffectOverlap;
        return nexeffectof;
    }

    public nexTransitionEffect(b bVar, nexSaveDataFormat.nexEffectOf nexeffectof) {
        this.mID = nexeffectof.mID;
        this.mAutoID = nexeffectof.mAutoID;
        this.mName = nexeffectof.mName;
        this.mType = nexeffectof.mType;
        this.mDuration = nexeffectof.mDuration;
        this.itemMethodType = nexeffectof.itemMethodType;
        this.mIsResolveOptions = nexeffectof.mIsResolveOptions;
        this.mOptionsUpdate = nexeffectof.mOptionsUpdate;
        this.m_effectOptions = nexeffectof.m_effectOptions;
        this.mMinDuration = nexeffectof.mMinDuration;
        this.mMaxDuration = nexeffectof.mMaxDuration;
        this.mEffectOffset = nexeffectof.mEffectOffset;
        this.mEffectOverlap = nexeffectof.mEffectOverlap;
        setObserver(bVar);
    }
}
