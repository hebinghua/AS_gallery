package com.edmodo.cropper.cropwindow.edge;

import android.graphics.RectF;
import com.edmodo.cropper.util.AspectRatioUtil;

/* loaded from: classes.dex */
public enum Edge {
    LEFT,
    TOP,
    RIGHT,
    BOTTOM;
    
    private float mCoordinate;

    public void setCoordinate(float f) {
        this.mCoordinate = f;
    }

    public void offset(float f) {
        this.mCoordinate += f;
    }

    public float getCoordinate() {
        return this.mCoordinate;
    }

    /* renamed from: com.edmodo.cropper.cropwindow.edge.Edge$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$edmodo$cropper$cropwindow$edge$Edge;

        static {
            int[] iArr = new int[Edge.values().length];
            $SwitchMap$com$edmodo$cropper$cropwindow$edge$Edge = iArr;
            try {
                iArr[Edge.LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$edmodo$cropper$cropwindow$edge$Edge[Edge.TOP.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$edmodo$cropper$cropwindow$edge$Edge[Edge.RIGHT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$edmodo$cropper$cropwindow$edge$Edge[Edge.BOTTOM.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public void adjustCoordinate(float f, float f2, RectF rectF, float f3, float f4) {
        int i = AnonymousClass1.$SwitchMap$com$edmodo$cropper$cropwindow$edge$Edge[ordinal()];
        if (i == 1) {
            this.mCoordinate = adjustLeft(f, rectF, f3, f4);
        } else if (i == 2) {
            this.mCoordinate = adjustTop(f2, rectF, f3, f4);
        } else if (i == 3) {
            this.mCoordinate = adjustRight(f, rectF, f3, f4);
        } else if (i != 4) {
        } else {
            this.mCoordinate = adjustBottom(f2, rectF, f3, f4);
        }
    }

    public void adjustCoordinate(float f) {
        float coordinate = LEFT.getCoordinate();
        float coordinate2 = TOP.getCoordinate();
        float coordinate3 = RIGHT.getCoordinate();
        float coordinate4 = BOTTOM.getCoordinate();
        int i = AnonymousClass1.$SwitchMap$com$edmodo$cropper$cropwindow$edge$Edge[ordinal()];
        if (i == 1) {
            this.mCoordinate = AspectRatioUtil.calculateLeft(coordinate2, coordinate3, coordinate4, f);
        } else if (i == 2) {
            this.mCoordinate = AspectRatioUtil.calculateTop(coordinate, coordinate3, coordinate4, f);
        } else if (i == 3) {
            this.mCoordinate = AspectRatioUtil.calculateRight(coordinate, coordinate2, coordinate4, f);
        } else if (i != 4) {
        } else {
            this.mCoordinate = AspectRatioUtil.calculateBottom(coordinate, coordinate2, coordinate3, f);
        }
    }

    public boolean isNewRectangleOutOfBounds(Edge edge, RectF rectF, float f) {
        float snapOffset = edge.snapOffset(rectF);
        int i = AnonymousClass1.$SwitchMap$com$edmodo$cropper$cropwindow$edge$Edge[ordinal()];
        if (i == 1) {
            Edge edge2 = TOP;
            if (edge.equals(edge2)) {
                float f2 = rectF.top;
                float coordinate = BOTTOM.getCoordinate() - snapOffset;
                float coordinate2 = RIGHT.getCoordinate();
                return isOutOfBounds(f2, AspectRatioUtil.calculateLeft(f2, coordinate2, coordinate, f), coordinate, coordinate2, rectF);
            } else if (edge.equals(BOTTOM)) {
                float f3 = rectF.bottom;
                float coordinate3 = edge2.getCoordinate() - snapOffset;
                float coordinate4 = RIGHT.getCoordinate();
                return isOutOfBounds(coordinate3, AspectRatioUtil.calculateLeft(coordinate3, coordinate4, f3, f), f3, coordinate4, rectF);
            }
        } else if (i == 2) {
            Edge edge3 = LEFT;
            if (edge.equals(edge3)) {
                float f4 = rectF.left;
                float coordinate5 = RIGHT.getCoordinate() - snapOffset;
                float coordinate6 = BOTTOM.getCoordinate();
                return isOutOfBounds(AspectRatioUtil.calculateTop(f4, coordinate5, coordinate6, f), f4, coordinate6, coordinate5, rectF);
            } else if (edge.equals(RIGHT)) {
                float f5 = rectF.right;
                float coordinate7 = edge3.getCoordinate() - snapOffset;
                float coordinate8 = BOTTOM.getCoordinate();
                return isOutOfBounds(AspectRatioUtil.calculateTop(coordinate7, f5, coordinate8, f), coordinate7, coordinate8, f5, rectF);
            }
        } else if (i == 3) {
            Edge edge4 = TOP;
            if (edge.equals(edge4)) {
                float f6 = rectF.top;
                float coordinate9 = BOTTOM.getCoordinate() - snapOffset;
                float coordinate10 = LEFT.getCoordinate();
                return isOutOfBounds(f6, coordinate10, coordinate9, AspectRatioUtil.calculateRight(coordinate10, f6, coordinate9, f), rectF);
            } else if (edge.equals(BOTTOM)) {
                float f7 = rectF.bottom;
                float coordinate11 = edge4.getCoordinate() - snapOffset;
                float coordinate12 = LEFT.getCoordinate();
                return isOutOfBounds(coordinate11, coordinate12, f7, AspectRatioUtil.calculateRight(coordinate12, coordinate11, f7, f), rectF);
            }
        } else if (i == 4) {
            Edge edge5 = LEFT;
            if (edge.equals(edge5)) {
                float f8 = rectF.left;
                float coordinate13 = RIGHT.getCoordinate() - snapOffset;
                float coordinate14 = TOP.getCoordinate();
                return isOutOfBounds(coordinate14, f8, AspectRatioUtil.calculateBottom(f8, coordinate14, coordinate13, f), coordinate13, rectF);
            } else if (edge.equals(RIGHT)) {
                float f9 = rectF.right;
                float coordinate15 = edge5.getCoordinate() - snapOffset;
                float coordinate16 = TOP.getCoordinate();
                return isOutOfBounds(coordinate16, coordinate15, AspectRatioUtil.calculateBottom(coordinate15, coordinate16, f9, f), f9, rectF);
            }
        }
        return true;
    }

    public final boolean isOutOfBounds(float f, float f2, float f3, float f4, RectF rectF) {
        return f < rectF.top || f2 < rectF.left || f3 > rectF.bottom || f4 > rectF.right;
    }

    public float snapToRect(RectF rectF) {
        float f = this.mCoordinate;
        int i = AnonymousClass1.$SwitchMap$com$edmodo$cropper$cropwindow$edge$Edge[ordinal()];
        if (i == 1) {
            this.mCoordinate = rectF.left;
        } else if (i == 2) {
            this.mCoordinate = rectF.top;
        } else if (i == 3) {
            this.mCoordinate = rectF.right;
        } else if (i == 4) {
            this.mCoordinate = rectF.bottom;
        }
        return this.mCoordinate - f;
    }

    public float snapOffset(RectF rectF) {
        float f;
        float f2 = this.mCoordinate;
        int i = AnonymousClass1.$SwitchMap$com$edmodo$cropper$cropwindow$edge$Edge[ordinal()];
        if (i == 1) {
            f = rectF.left;
        } else if (i == 2) {
            f = rectF.top;
        } else if (i == 3) {
            f = rectF.right;
        } else {
            f = rectF.bottom;
        }
        return f - f2;
    }

    public static float getWidth() {
        return RIGHT.getCoordinate() - LEFT.getCoordinate();
    }

    public static float getHeight() {
        return BOTTOM.getCoordinate() - TOP.getCoordinate();
    }

    public boolean isOutsideMargin(RectF rectF, float f) {
        int i = AnonymousClass1.$SwitchMap$com$edmodo$cropper$cropwindow$edge$Edge[ordinal()];
        if (i != 1) {
            if (i != 2) {
                if (i == 3) {
                    if (rectF.right - this.mCoordinate >= f) {
                        return false;
                    }
                } else if (rectF.bottom - this.mCoordinate >= f) {
                    return false;
                }
            } else if (this.mCoordinate - rectF.top >= f) {
                return false;
            }
        } else if (this.mCoordinate - rectF.left >= f) {
            return false;
        }
        return true;
    }

    public static float adjustLeft(float f, RectF rectF, float f2, float f3) {
        float f4 = rectF.left;
        if (f - f4 < f2) {
            return f4;
        }
        Edge edge = RIGHT;
        float f5 = Float.POSITIVE_INFINITY;
        float coordinate = f >= edge.getCoordinate() - 40.0f ? edge.getCoordinate() - 40.0f : Float.POSITIVE_INFINITY;
        if ((edge.getCoordinate() - f) / f3 <= 40.0f) {
            f5 = edge.getCoordinate() - (f3 * 40.0f);
        }
        return Math.max(rectF.left, Math.min(f, Math.min(coordinate, f5)));
    }

    public static float adjustRight(float f, RectF rectF, float f2, float f3) {
        float f4 = rectF.right;
        if (f4 - f < f2) {
            return f4;
        }
        Edge edge = LEFT;
        float f5 = Float.NEGATIVE_INFINITY;
        float coordinate = f <= edge.getCoordinate() + 40.0f ? edge.getCoordinate() + 40.0f : Float.NEGATIVE_INFINITY;
        if ((f - edge.getCoordinate()) / f3 <= 40.0f) {
            f5 = edge.getCoordinate() + (f3 * 40.0f);
        }
        return Math.min(rectF.right, Math.max(f, Math.max(coordinate, f5)));
    }

    public static float adjustTop(float f, RectF rectF, float f2, float f3) {
        float f4 = rectF.top;
        if (f - f4 < f2) {
            return f4;
        }
        Edge edge = BOTTOM;
        float f5 = Float.POSITIVE_INFINITY;
        float coordinate = f >= edge.getCoordinate() - 40.0f ? edge.getCoordinate() - 40.0f : Float.POSITIVE_INFINITY;
        if ((edge.getCoordinate() - f) * f3 <= 40.0f) {
            f5 = edge.getCoordinate() - (40.0f / f3);
        }
        return Math.max(rectF.top, Math.min(f, Math.min(coordinate, f5)));
    }

    public static float adjustBottom(float f, RectF rectF, float f2, float f3) {
        float f4 = rectF.bottom;
        if (f4 - f < f2) {
            return f4;
        }
        Edge edge = TOP;
        float f5 = Float.NEGATIVE_INFINITY;
        float coordinate = f <= edge.getCoordinate() + 40.0f ? edge.getCoordinate() + 40.0f : Float.NEGATIVE_INFINITY;
        if ((f - edge.getCoordinate()) * f3 <= 40.0f) {
            f5 = edge.getCoordinate() + (40.0f / f3);
        }
        return Math.min(rectF.bottom, Math.max(f, Math.max(f5, coordinate)));
    }
}
