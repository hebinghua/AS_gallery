package com.miui.gallery.editor.photo.core.imports.text.base;

import com.miui.gallery.editor.photo.core.imports.text.typeface.TextStyle;
import com.miui.gallery.editor.photo.core.imports.text.utils.AutoLineLayout;

/* loaded from: classes2.dex */
public interface ITextSetting {
    int getColor();

    default int getGradientsColor() {
        return 0;
    }

    default int[] getSubstrateColors() {
        return null;
    }

    String getText();

    AutoLineLayout.TextAlignment getTextAlignment();

    TextStyle getTextStyle();

    float getTextTransparent();

    boolean isBoldText();

    boolean isShadow();

    default boolean isStroke() {
        return false;
    }

    boolean isSubstrate();

    void setBoldText(boolean z);

    void setColor(int i);

    default void setGradientsColor(int i) {
    }

    default void setIsStroke(boolean z) {
    }

    void setShadow(boolean z);

    void setSubstrate(boolean z);

    default void setSubstrateColors(int... iArr) {
    }

    void setText(String str);

    void setTextAlignment(AutoLineLayout.TextAlignment textAlignment);

    void setTextStyle(TextStyle textStyle);

    void setTextTransparent(float f);
}
