package com.nexstreaming.app.common.nexasset.assetpackage;

/* loaded from: classes3.dex */
public enum ItemType {
    overlay("nex.overlay"),
    renderitem("nex.renderitem"),
    kedl("nex.kedl"),
    audio("nex.audio"),
    font("nex.font"),
    template("nex.template"),
    lut("nex.lut"),
    media("nex.media"),
    collage("nex.collage"),
    staticcollage("nex.staticcollage"),
    dynamiccollage("nex.dynamiccollage"),
    beat("nex.beat");
    
    private final String typeId;

    ItemType(String str) {
        this.typeId = str;
    }

    public static ItemType fromId(String str) {
        ItemType[] values;
        for (ItemType itemType : values()) {
            if (itemType.typeId.equals(str)) {
                return itemType;
            }
        }
        return null;
    }

    public String getTypeId() {
        return this.typeId;
    }
}
