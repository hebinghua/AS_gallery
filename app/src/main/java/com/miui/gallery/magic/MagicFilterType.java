package com.miui.gallery.magic;

/* loaded from: classes2.dex */
public enum MagicFilterType {
    RGB_SHIFT(0),
    WAVE(1),
    SHUTTER(2),
    SKETCH(3),
    WATER_COLOR(4),
    GLASS_WINDOW(5);
    
    private int type;

    MagicFilterType(int i) {
        this.type = 0;
        this.type = i;
    }

    public int getType() {
        return this.type;
    }
}
