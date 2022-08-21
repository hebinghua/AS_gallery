package com.miui.gallery.editor.photo.core.imports.adjust2;

import android.annotation.SuppressLint;
import com.miui.gallery.editor.photo.core.common.model.Adjust2Data;
import java.util.Arrays;
import java.util.List;

@SuppressLint({"ParcelCreator"})
/* loaded from: classes2.dex */
public class Adjust2DataImpl extends Adjust2Data {
    public List<String> mEffects;
    public int mId;
    public boolean mIsMid;

    public Adjust2DataImpl(int i) {
        this(i, (short) 10, null, 0, false, 0);
    }

    public Adjust2DataImpl(int i, short s, String str, int i2, boolean z, int i3) {
        super(s, str, i2, i3);
        this.mEffects = Arrays.asList("AutoBeauty;strength=1.0,category=1", "Brightness;intensity", "Contrast;intensity", "Satuation;intensity", "Sharpen;sharpen", "Vignette;vignette", "Grain;grain", "Exposure;exposure", "Temperature;temperature", "Tint;hueAdjust", "Highlight;hilight", "Shadow;shadows", "Fadegray;fade", "SatuationNatural;intensity", "Clarity;clarity");
        this.mId = i;
        this.mIsMid = z;
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.Adjust2Data
    public int getMax() {
        return Adjust2Data.MAX;
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.Adjust2Data
    public int getMin() {
        return this.mIsMid ? -Adjust2Data.MAX : Adjust2Data.MIN;
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.Adjust2Data
    public boolean isMid() {
        return this.mIsMid;
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.Adjust2Data
    public int getId() {
        return this.mId;
    }

    @Override // com.miui.gallery.editor.photo.core.common.model.Adjust2Data
    public String getEffectName() {
        return this.mEffects.get(this.mId);
    }

    public String getEffect() {
        if (getId() == 0) {
            return this.mEffects.get(this.mId);
        }
        return this.mEffects.get(this.mId) + "=" + ((((this.progress - getMin()) * getMax()) / (getMax() - getMin())) / 100.0f);
    }
}
