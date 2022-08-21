package com.miui.gallery.xmstreaming;

import java.util.List;

/* loaded from: classes3.dex */
public class XmsEffectTrack extends XmsObject {
    private int clipDuration;
    private int clipStart;
    private List<XmsEffect> effect;

    public void setClipStart(int i) {
        this.clipStart = i;
    }

    public int getClipStart() {
        return this.clipStart;
    }

    public void setClipDuration(int i) {
        this.clipDuration = i;
    }

    public int getClipDuration() {
        return this.clipDuration;
    }

    public void setEffect(List<XmsEffect> list) {
        this.effect = list;
    }

    public List<XmsEffect> getEffect() {
        return this.effect;
    }

    @Override // com.miui.gallery.xmstreaming.XmsObject
    public void mapData() {
        this.m_attachmentMap.put("effect", this.effect);
        for (XmsEffect xmsEffect : this.effect) {
            xmsEffect.mapData();
        }
    }
}
