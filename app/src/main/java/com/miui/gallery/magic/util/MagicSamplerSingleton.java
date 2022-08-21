package com.miui.gallery.magic.util;

/* loaded from: classes2.dex */
public class MagicSamplerSingleton {
    public String selectArtName;
    public String selectIdCardColor;
    public String selectIdCardSize;
    public int selectMagicIndex;

    /* loaded from: classes2.dex */
    public static class SingletonClassInstance {
        public static final MagicSamplerSingleton instance = new MagicSamplerSingleton();
    }

    public MagicSamplerSingleton() {
    }

    public static MagicSamplerSingleton getInstance() {
        return SingletonClassInstance.instance;
    }

    public int getSelectMagicIndex() {
        return this.selectMagicIndex;
    }

    public void setSelectMagicIndex(int i) {
        this.selectMagicIndex = i;
    }

    public String getSelectIdCardColor() {
        String str = this.selectIdCardColor;
        return str == null ? MagicSamplerConstants.COLORS[0] : str;
    }

    public void setSelectIdCardColor(String str) {
        this.selectIdCardColor = str;
    }

    public String getSelectIdCardSize() {
        String str = this.selectIdCardSize;
        return str == null ? "295×413 px" : str;
    }

    public void setSelectIdCardSize(String str) {
        this.selectIdCardSize = str;
    }

    public String getSelectArtName() {
        String str = this.selectArtName;
        return str == null ? "素描" : str;
    }

    public void setSelectArtName(String str) {
        this.selectArtName = str;
    }
}
