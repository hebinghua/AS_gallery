package com.miui.gallery.editor.photo.core.imports.doodle.painter.paintbrush;

import ch.qos.logback.core.CoreConstants;
import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: DoodlePen.kt */
/* loaded from: classes2.dex */
public final class DoodlePen {
    public static final Companion Companion = new Companion(null);
    public float alpha;
    public int colorInt;
    public boolean needScale;
    public float size;
    public final String type;

    public String toString() {
        return "DoodlePen(type=" + this.type + ", alpha=" + this.alpha + ", colorInt=" + this.colorInt + ", size=" + this.size + ", needScale=" + this.needScale + CoreConstants.RIGHT_PARENTHESIS_CHAR;
    }

    public DoodlePen(String type, float f, int i, float f2, boolean z) {
        Intrinsics.checkNotNullParameter(type, "type");
        this.type = type;
        this.alpha = f;
        this.colorInt = i;
        this.size = f2;
        this.needScale = z;
    }

    public /* synthetic */ DoodlePen(String str, float f, int i, float f2, boolean z, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(str, (i2 & 2) != 0 ? 1.0f : f, (i2 & 4) != 0 ? -16777216 : i, (i2 & 8) != 0 ? 0.0f : f2, (i2 & 16) != 0 ? false : z);
    }

    public final String getType() {
        return this.type;
    }

    public final float getAlpha() {
        return this.alpha;
    }

    public final void setAlpha(float f) {
        this.alpha = f;
    }

    public final int getColorInt() {
        return this.colorInt;
    }

    public final void setColorInt(int i) {
        this.colorInt = i;
    }

    public final float getSize() {
        return this.size;
    }

    public final void setSize(float f) {
        this.size = f;
    }

    public final boolean getNeedScale() {
        return this.needScale;
    }

    public final boolean isEraser() {
        return Intrinsics.areEqual(this.type, "Eraser_01");
    }

    public final DoodlePen copy() {
        return new DoodlePen(this.type, this.alpha, this.colorInt, this.size, this.needScale);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!Intrinsics.areEqual(DoodlePen.class, obj == null ? null : obj.getClass())) {
            return false;
        }
        Objects.requireNonNull(obj, "null cannot be cast to non-null type com.miui.gallery.editor.photo.core.imports.doodle.painter.paintbrush.DoodlePen");
        DoodlePen doodlePen = (DoodlePen) obj;
        if (!Intrinsics.areEqual(this.type, doodlePen.type)) {
            return false;
        }
        if (!(this.alpha == doodlePen.alpha) || this.colorInt != doodlePen.colorInt) {
            return false;
        }
        return (this.size > doodlePen.size ? 1 : (this.size == doodlePen.size ? 0 : -1)) == 0;
    }

    public int hashCode() {
        return (((((this.type.hashCode() * 31) + Float.hashCode(this.alpha)) * 31) + this.colorInt) * 31) + Float.hashCode(this.size);
    }

    /* compiled from: DoodlePen.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
