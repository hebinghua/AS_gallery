package com.miui.gallery.magic;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SumPathEffect;
import com.miui.gallery.magic.MattingInvoker;

/* loaded from: classes2.dex */
public class ContourHelper {
    public static int cc_offset;

    /* loaded from: classes2.dex */
    public static class Configure {
        public int style = 1;
        public float strokeWidth = 1.0f;
        public int color = -65536;
        public float distance = 0.0f;
        public int[] rainbow = null;
        public int offsetX = 0;
        public int offsetY = 0;
        public float segmentLength = 0.0f;
        public float segmentDistance = 0.0f;

        public float getSegmentDistance() {
            if (this.segmentLength == 0.0f) {
                return getStrokeWidth();
            }
            return this.segmentDistance;
        }

        public float getSegmentLength() {
            float f = this.segmentLength;
            return f == 0.0f ? getStrokeWidth() * 3.0f : f;
        }

        public int getOffsetX() {
            return this.offsetX;
        }

        public Configure setOffsetX(int i) {
            this.offsetX = i;
            return this;
        }

        public int getOffsetY() {
            return this.offsetY;
        }

        public Configure setOffsetY(int i) {
            this.offsetY = i;
            return this;
        }

        public float getDistance() {
            return this.distance;
        }

        public Configure setDistance(float f) {
            this.distance = f;
            return this;
        }

        public int[] getRainbow() {
            return this.rainbow;
        }

        public Configure setRainbow(int[] iArr) {
            this.rainbow = iArr;
            return this;
        }

        public int getStyle() {
            return this.style;
        }

        public Configure setStyle(int i) {
            this.style = i;
            return this;
        }

        public float getStrokeWidth() {
            return this.strokeWidth;
        }

        public Configure setStrokeWidth(float f) {
            this.strokeWidth = f;
            return this;
        }

        public int getColor() {
            return this.color;
        }

        public Configure setColor(int i) {
            this.color = i;
            return this;
        }

        public Paint getPaint() {
            Paint paint = new Paint(1);
            paint.setStrokeWidth(getStrokeWidth());
            paint.setColor(getColor());
            paint.setStyle(Paint.Style.STROKE);
            float strokeWidth = getStrokeWidth() / 2.0f;
            int style = getStyle();
            if (style == 2) {
                Path path = new Path();
                path.addRoundRect(new RectF(0.0f, 0.0f, getSegmentLength(), getStrokeWidth()), strokeWidth, strokeWidth, Path.Direction.CCW);
                paint.setPathEffect(new ComposePathEffect(new PathDashPathEffect(path, getSegmentLength() + getSegmentDistance(), 100.0f, PathDashPathEffect.Style.MORPH), new DiscretePathEffect(getStrokeWidth(), 0.0f)));
            } else if (style == 3) {
                paint.setPathEffect(ContourHelper.getPathEffects(this)[5]);
            } else if (style == 4) {
                paint.setStrokeWidth(1.0f);
            }
            return paint;
        }
    }

    public static Point[] getNewContourPoints(Bitmap bitmap, MattingInvoker.SegmentResult segmentResult, int i, float f) {
        int i2 = cc_offset;
        Path pathByBox = getPathByBox(segmentResult, i, i2, i2);
        MattingInvoker.BoundingBox personBox = segmentResult.getPersonBox(i);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int i3 = cc_offset;
        int i4 = width + (i3 * 2);
        int i5 = height + (i3 * 2);
        Bitmap newContourBitmap = getNewContourBitmap(i4, i5, pathByBox, f, personBox);
        int i6 = i4 * i5;
        byte[] bArr = new byte[i6 * 2];
        int[] iArr = new int[i6];
        newContourBitmap.getPixels(iArr, 0, i4, 0, 0, i4, i5);
        for (int i7 = 0; i7 < i4; i7++) {
            for (int i8 = 0; i8 < i5; i8++) {
                int i9 = (i8 * i4) + i7;
                if (iArr[i9] != -1) {
                    int i10 = i9 * 2;
                    bArr[i10] = 1;
                    bArr[i10 + 1] = 1;
                }
            }
        }
        newContourBitmap.recycle();
        return MattingInvoker.SegmentResult.getContourByMask(bArr, i4, i5, 1);
    }

    public static Bitmap getNewContourBitmap(int i, int i2, Path path, float f, MattingInvoker.BoundingBox boundingBox) {
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawColor(-1);
        Paint paint = new Paint();
        paint.setStrokeWidth(f * 2.0f);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(-16777216);
        canvas.drawPath(path, paint);
        return createBitmap;
    }

    public static Path getPathByBox(MattingInvoker.SegmentResult segmentResult, int i, int i2, int i3) {
        Point[] contour = segmentResult.getContour(i);
        segmentResult.getPersonBox(i);
        return getPathByPointArray(contour, segmentResult.getPersonBox(i).toRect(), i2, i3);
    }

    public static Path getPathByPointArray(Point[] pointArr, Rect rect, int i, int i2) {
        Path path = new Path();
        if (pointArr.length < 2) {
            return null;
        }
        path.moveTo(pointArr[0].x + i, pointArr[0].y + i2);
        for (int i3 = 1; i3 < pointArr.length; i3++) {
            Point point = pointArr[i3];
            path.lineTo(point.x + i, point.y + i2);
        }
        path.lineTo(pointArr[0].x + i, pointArr[0].y + i2);
        return path;
    }

    public static Path getPathByPointArray2(Point[] pointArr, Rect rect, int i, int i2) {
        int i3;
        Path path = new Path();
        if (pointArr.length < 2) {
            return null;
        }
        path.moveTo(pointArr[0].x + i, pointArr[0].y + i2);
        Point point = pointArr[0];
        for (int i4 = 1; i4 < pointArr.length; i4++) {
            Point point2 = pointArr[i4];
            int i5 = point2.x;
            if (i5 != rect.left && i5 != rect.right && (i3 = point2.y) != rect.top && i3 != rect.bottom) {
                if (getDistanceByPoint(point, point2) > 5.0d) {
                    path.moveTo(point2.x + i, point2.y + i2);
                } else {
                    path.lineTo(point2.x + i, point2.y + i2);
                }
                point = point2;
            }
        }
        return path;
    }

    public static double getDistanceByPoint(Point point, Point point2) {
        int i = point.x;
        int i2 = point2.x;
        int i3 = point.y;
        int i4 = point2.y;
        return Math.sqrt(((i - i2) * (i - i2)) + ((i3 - i4) * (i3 - i4)));
    }

    public static Bitmap drawBitmap(Bitmap bitmap, MattingInvoker.SegmentResult segmentResult, int i, Bitmap bitmap2, Configure configure) {
        if (configure.getStyle() == 4) {
            return drawBitmapMove(bitmap, segmentResult, i, bitmap2, configure, -1, true);
        }
        if (configure.getStyle() == 5) {
            drawBitmapMove(bitmap, segmentResult, i, bitmap2, configure, configure.getRainbow()[0], false);
            configure.setDistance(-configure.getDistance());
            drawBitmapMove(bitmap, segmentResult, i, bitmap2, configure, configure.getRainbow()[1], true);
            return bitmap2;
        } else if (configure.getStyle() == 6) {
            return drawBitmapRainbow(bitmap, segmentResult, i, bitmap2, configure);
        } else {
            return drawBitmapExSolid(bitmap, segmentResult, i, bitmap2, configure);
        }
    }

    public static Bitmap drawBitmapRainbow(Bitmap bitmap, MattingInvoker.SegmentResult segmentResult, int i, Bitmap bitmap2, Configure configure) {
        int[] rainbow = configure.getRainbow();
        if (rainbow == null || rainbow.length < 1) {
            throw new Error("未设置彩虹色彩");
        }
        Canvas canvas = new Canvas(bitmap2);
        Paint paint = configure.getPaint();
        for (int i2 = 0; i2 < rainbow.length; i2++) {
            Path pathByPointArray2 = getPathByPointArray2(getNewContourPoints(bitmap, segmentResult, i, configure.getDistance() + (configure.getStrokeWidth() * ((rainbow.length - i2) - 1)) + (configure.getStrokeWidth() / 2.0f)), segmentResult.getPersonBox(i).toRect(), configure.getOffsetX() - cc_offset, configure.getOffsetY() - cc_offset);
            paint.setColor(rainbow[i2]);
            canvas.drawPath(pathByPointArray2, paint);
        }
        return bitmap2;
    }

    public static Bitmap drawBitmapExSolid(Bitmap bitmap, MattingInvoker.SegmentResult segmentResult, int i, Bitmap bitmap2, Configure configure) {
        new Canvas(bitmap2).drawPath(getPathByPointArray(getNewContourPoints(bitmap, segmentResult, i, configure.getDistance() + (configure.getStrokeWidth() / 2.0f)), segmentResult.getPersonBox(i).toRect(), configure.getOffsetX() - cc_offset, configure.getOffsetY() - cc_offset), configure.getPaint());
        return bitmap2;
    }

    public static Bitmap drawBitmapMove(Bitmap bitmap, MattingInvoker.SegmentResult segmentResult, int i, Bitmap bitmap2, Configure configure, int i2, boolean z) {
        segmentResult.drawPersonByBox(i, bitmap, segmentResult.getPersonBox(i), bitmap2, (int) (configure.getOffsetX() + configure.getDistance()), configure.getOffsetY(), i2);
        if (z) {
            segmentResult.drawPerson(bitmap, i, bitmap2, configure.getOffsetX(), configure.getOffsetY());
        }
        return bitmap2;
    }

    public static PathEffect[] getPathEffects(Configure configure) {
        Path path = new Path();
        path.addRect(0.0f, 0.0f, configure.getStrokeWidth(), configure.getStrokeWidth(), Path.Direction.CCW);
        PathEffect[] pathEffectArr = {null, new CornerPathEffect(configure.getStrokeWidth() / 2.0f), new DashPathEffect(new float[]{20.0f, 10.0f, 5.0f, 1.0f}, 5.0f), new DiscretePathEffect(10.0f, 5.0f), new PathDashPathEffect(path, configure.getStrokeWidth() * 2.0f, configure.getStrokeWidth(), PathDashPathEffect.Style.ROTATE), new ComposePathEffect(pathEffectArr[1], pathEffectArr[4]), new ComposePathEffect(pathEffectArr[4], pathEffectArr[1]), new SumPathEffect(pathEffectArr[1], pathEffectArr[4]), new SumPathEffect(pathEffectArr[4], pathEffectArr[1])};
        return pathEffectArr;
    }
}
