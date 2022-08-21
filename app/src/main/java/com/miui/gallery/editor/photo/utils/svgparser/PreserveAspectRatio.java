package com.miui.gallery.editor.photo.utils.svgparser;

import java.util.Objects;

/* loaded from: classes2.dex */
public class PreserveAspectRatio {
    public static final PreserveAspectRatio BOTTOM;
    public static final PreserveAspectRatio END;
    public static final PreserveAspectRatio LETTERBOX;
    public static final PreserveAspectRatio START;
    public static final PreserveAspectRatio STRETCH = new PreserveAspectRatio(Alignment.None, null);
    public static final PreserveAspectRatio TOP;
    public Alignment alignment;
    public Scale scale;

    /* loaded from: classes2.dex */
    public enum Alignment {
        None,
        XMinYMin,
        XMidYMin,
        XMaxYMin,
        XMinYMid,
        XMidYMid,
        XMaxYMid,
        XMinYMax,
        XMidYMax,
        XMaxYMax
    }

    /* loaded from: classes2.dex */
    public enum Scale {
        Meet,
        Slice
    }

    static {
        Alignment alignment = Alignment.XMidYMid;
        Scale scale = Scale.Meet;
        LETTERBOX = new PreserveAspectRatio(alignment, scale);
        START = new PreserveAspectRatio(Alignment.XMinYMin, scale);
        END = new PreserveAspectRatio(Alignment.XMaxYMax, scale);
        TOP = new PreserveAspectRatio(Alignment.XMidYMin, scale);
        BOTTOM = new PreserveAspectRatio(Alignment.XMidYMax, scale);
    }

    public PreserveAspectRatio(Alignment alignment, Scale scale) {
        this.alignment = alignment;
        this.scale = scale;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PreserveAspectRatio preserveAspectRatio = (PreserveAspectRatio) obj;
        return this.alignment == preserveAspectRatio.alignment && this.scale == preserveAspectRatio.scale;
    }

    public int hashCode() {
        return Objects.hash(this.alignment, this.scale);
    }
}
