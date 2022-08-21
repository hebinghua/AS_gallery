package com.miui.gallery.xmstreaming;

import java.util.List;

/* loaded from: classes3.dex */
public class XmsEffect extends XmsObject {
    private List<XmsAnimation> animation;
    private String name;

    public void setName(String str) {
        this.name = str;
    }

    public String getName() {
        return this.name;
    }

    public void setAnimation(List<XmsAnimation> list) {
        this.animation = list;
    }

    public List<XmsAnimation> getAnimation() {
        return this.animation;
    }

    @Override // com.miui.gallery.xmstreaming.XmsObject
    public void mapData() {
        super.mapData();
        this.m_attachmentMap.put("name", this.name);
        this.m_attachmentMap.put("animation", this.animation);
        for (XmsAnimation xmsAnimation : this.animation) {
            xmsAnimation.mapData();
        }
    }
}
