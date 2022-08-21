package com.miui.gallery.editor.photo.core.imports.doodle.painter.paintbrush;

/* compiled from: DoodlePenManager.kt */
/* loaded from: classes2.dex */
public final class DoodlePenManager {
    public static final DoodlePenManager INSTANCE;
    public static final DoodlePen eraser;
    public static final DoodlePen markPen;
    public static final DoodlePen normal;

    static {
        DoodlePenManager doodlePenManager = new DoodlePenManager();
        INSTANCE = doodlePenManager;
        normal = doodlePenManager.createPen("Normal_01");
        markPen = doodlePenManager.createPen("MarkPen_01");
        eraser = doodlePenManager.createPen("Eraser_01");
    }

    public final DoodlePen getNormal() {
        return normal;
    }

    public final DoodlePen getMarkPen() {
        return markPen;
    }

    public final DoodlePen getEraser() {
        return eraser;
    }

    public final DoodlePen createPen(String str) {
        int hashCode = str.hashCode();
        if (hashCode != -2054401991) {
            if (hashCode != -1798555596) {
                if (hashCode == 234237044 && str.equals("Eraser_01")) {
                    return new DoodlePen(str, 1.0f, 0, 1.0f, false, 16, null);
                }
            } else if (str.equals("MarkPen_01")) {
                return new DoodlePen(str, 1.0f, 22208, 5.0f, false, 16, null);
            }
        } else if (str.equals("Normal_01")) {
            return new DoodlePen(str, 1.0f, 9187011, 1.0f, true);
        }
        return new DoodlePen(str, 1.0f, -16777216, 1.0f, false, 16, null);
    }
}
