package com.miui.filtersdk.utils;

import com.nexstreaming.nexeditorsdk.nexClip;

/* loaded from: classes.dex */
public enum Rotation {
    NORMAL,
    ROTATION_90,
    ROTATION_180,
    ROTATION_270;

    /* renamed from: com.miui.filtersdk.utils.Rotation$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$filtersdk$utils$Rotation;

        static {
            int[] iArr = new int[Rotation.values().length];
            $SwitchMap$com$miui$filtersdk$utils$Rotation = iArr;
            try {
                iArr[Rotation.NORMAL.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$utils$Rotation[Rotation.ROTATION_90.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$utils$Rotation[Rotation.ROTATION_180.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$utils$Rotation[Rotation.ROTATION_270.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public int asInt() {
        int i = AnonymousClass1.$SwitchMap$com$miui$filtersdk$utils$Rotation[ordinal()];
        if (i != 1) {
            if (i == 2) {
                return 90;
            }
            if (i == 3) {
                return nexClip.kClip_Rotate_180;
            }
            if (i != 4) {
                throw new IllegalStateException("Unknown Rotation!");
            }
            return nexClip.kClip_Rotate_270;
        }
        return 0;
    }

    public static Rotation fromInt(int i) {
        if (i != 0) {
            if (i == 90) {
                return ROTATION_90;
            }
            if (i == 180) {
                return ROTATION_180;
            }
            if (i == 270) {
                return ROTATION_270;
            }
            if (i == 360) {
                return NORMAL;
            }
            throw new IllegalStateException(i + " is an unknown rotation. Needs to be either 0, 90, 180 or 270!");
        }
        return NORMAL;
    }
}
