package com.miui.gallery.vlog.clip.speed;

import com.miui.gallery.search.statistics.SearchStatUtils;

/* loaded from: classes2.dex */
public enum Speed {
    QUARTER,
    HALF,
    NORMAL,
    DOUBLE,
    TREBLE;

    /* renamed from: com.miui.gallery.vlog.clip.speed.Speed$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$vlog$clip$speed$Speed;

        static {
            int[] iArr = new int[Speed.values().length];
            $SwitchMap$com$miui$gallery$vlog$clip$speed$Speed = iArr;
            try {
                iArr[Speed.TREBLE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$vlog$clip$speed$Speed[Speed.DOUBLE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$vlog$clip$speed$Speed[Speed.NORMAL.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$vlog$clip$speed$Speed[Speed.HALF.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$vlog$clip$speed$Speed[Speed.QUARTER.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    public static double convertSpeedToDouble(Speed speed) {
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$vlog$clip$speed$Speed[speed.ordinal()];
        if (i != 1) {
            if (i == 2) {
                return 2.0d;
            }
            if (i == 3) {
                return 1.0d;
            }
            if (i == 4) {
                return 0.5d;
            }
            if (i == 5) {
                return 0.25d;
            }
            return SearchStatUtils.POW;
        }
        return 3.0d;
    }
}
