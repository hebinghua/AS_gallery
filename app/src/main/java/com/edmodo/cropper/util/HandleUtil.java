package com.edmodo.cropper.util;

import android.graphics.PointF;
import com.edmodo.cropper.cropwindow.handle.Handle;

/* loaded from: classes.dex */
public class HandleUtil {
    public static boolean isWithinBounds(float f, float f2, float f3, float f4, float f5, float f6) {
        return f >= f3 && f <= f5 && f2 >= f4 && f2 <= f6;
    }

    public static Handle getPressedHandle(float f, float f2, float f3, float f4, float f5, float f6, float f7) {
        float f8;
        Handle handle;
        float calculateDistance = MathUtil.calculateDistance(f, f2, f3, f4);
        if (calculateDistance < Float.POSITIVE_INFINITY) {
            handle = Handle.TOP_LEFT;
            f8 = f4;
        } else {
            f8 = f4;
            calculateDistance = Float.POSITIVE_INFINITY;
            handle = null;
        }
        float calculateDistance2 = MathUtil.calculateDistance(f, f2, f5, f8);
        if (calculateDistance2 < calculateDistance) {
            handle = Handle.TOP_RIGHT;
            calculateDistance = calculateDistance2;
        }
        float calculateDistance3 = MathUtil.calculateDistance(f, f2, f3, f6);
        if (calculateDistance3 < calculateDistance) {
            handle = Handle.BOTTOM_LEFT;
            calculateDistance = calculateDistance3;
        }
        float calculateDistance4 = MathUtil.calculateDistance(f, f2, f5, f6);
        if (calculateDistance4 < calculateDistance) {
            handle = Handle.BOTTOM_RIGHT;
            calculateDistance = calculateDistance4;
        }
        if (calculateDistance <= f7) {
            return handle;
        }
        if (isInHorizontalTargetZone(f, f2, f3, f5, f4, f7)) {
            return Handle.TOP;
        }
        if (isInHorizontalTargetZone(f, f2, f3, f5, f6, f7)) {
            return Handle.BOTTOM;
        }
        if (isInVerticalTargetZone(f, f2, f3, f4, f6, f7)) {
            return Handle.LEFT;
        }
        if (isInVerticalTargetZone(f, f2, f5, f4, f6, f7)) {
            return Handle.RIGHT;
        }
        if (!isWithinBounds(f, f2, f3, f4, f5, f6)) {
            return null;
        }
        return Handle.CENTER;
    }

    /* renamed from: com.edmodo.cropper.util.HandleUtil$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$edmodo$cropper$cropwindow$handle$Handle;

        static {
            int[] iArr = new int[Handle.values().length];
            $SwitchMap$com$edmodo$cropper$cropwindow$handle$Handle = iArr;
            try {
                iArr[Handle.TOP_LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$edmodo$cropper$cropwindow$handle$Handle[Handle.TOP_RIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$edmodo$cropper$cropwindow$handle$Handle[Handle.BOTTOM_LEFT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$edmodo$cropper$cropwindow$handle$Handle[Handle.BOTTOM_RIGHT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$edmodo$cropper$cropwindow$handle$Handle[Handle.LEFT.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$edmodo$cropper$cropwindow$handle$Handle[Handle.TOP.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$edmodo$cropper$cropwindow$handle$Handle[Handle.RIGHT.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$edmodo$cropper$cropwindow$handle$Handle[Handle.BOTTOM.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$edmodo$cropper$cropwindow$handle$Handle[Handle.CENTER.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public static void getOffset(Handle handle, float f, float f2, float f3, float f4, float f5, float f6, PointF pointF) {
        float f7;
        float f8 = 0.0f;
        switch (AnonymousClass1.$SwitchMap$com$edmodo$cropper$cropwindow$handle$Handle[handle.ordinal()]) {
            case 1:
                f8 = f3 - f;
                f7 = f4 - f2;
                break;
            case 2:
                f8 = f5 - f;
                f7 = f4 - f2;
                break;
            case 3:
                f8 = f3 - f;
                f7 = f6 - f2;
                break;
            case 4:
                f8 = f5 - f;
                f7 = f6 - f2;
                break;
            case 5:
                f7 = 0.0f;
                f8 = f3 - f;
                break;
            case 6:
                f7 = f4 - f2;
                break;
            case 7:
                f7 = 0.0f;
                f8 = f5 - f;
                break;
            case 8:
                f7 = f6 - f2;
                break;
            case 9:
                f5 = (f5 + f3) / 2.0f;
                f4 = (f4 + f6) / 2.0f;
                f8 = f5 - f;
                f7 = f4 - f2;
                break;
            default:
                f7 = 0.0f;
                break;
        }
        pointF.x = f8;
        pointF.y = f7;
    }

    public static boolean isInHorizontalTargetZone(float f, float f2, float f3, float f4, float f5, float f6) {
        return f > f3 && f < f4 && Math.abs(f2 - f5) <= f6;
    }

    public static boolean isInVerticalTargetZone(float f, float f2, float f3, float f4, float f5, float f6) {
        return Math.abs(f - f3) <= f6 && f2 > f4 && f2 < f5;
    }
}
