package com.jakewharton.picnic;

/* compiled from: dsl.kt */
/* loaded from: classes.dex */
public interface CellStyleDsl {
    void setAlignment(TextAlignment textAlignment);

    void setBorderBottom(Boolean bool);

    void setBorderLeft(Boolean bool);

    void setBorderRight(Boolean bool);

    void setBorderTop(Boolean bool);

    void setPaddingLeft(Integer num);

    void setPaddingRight(Integer num);

    default void setBorder(boolean z) {
        setBorderLeft(Boolean.valueOf(z));
        setBorderRight(Boolean.valueOf(z));
        setBorderTop(Boolean.valueOf(z));
        setBorderBottom(Boolean.valueOf(z));
    }
}
