package com.miui.gallery.collage.render;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.graphics.drawable.Drawable;
import com.miui.gallery.collage.BitmapManager;
import com.miui.gallery.collage.ClipType;
import com.miui.gallery.collage.core.layout.LayoutItemModel;
import com.miui.gallery.collage.core.layout.LayoutModel;
import com.miui.gallery.collage.core.poster.CollagePositionModel;
import com.miui.gallery.collage.core.poster.PosterModel;
import com.miui.gallery.collage.widget.CollageImageView;
import com.miui.gallery.collage.widget.CollageLayout;
import com.miui.gallery.collage.widget.PosterLayout;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Arrays;

/* loaded from: classes.dex */
public class CollageRender {

    /* loaded from: classes.dex */
    public static class RenderData {
        public BitmapRenderData[] bitmapRenders;
        public boolean igonreMarginEdge;
        public int imageHeight;
        public int imageWidth;
        public LayoutModel layoutModel;
        public float margin;
        public PosterElementRender posterElementRender;
        public PosterModel posterModel;
        public float scaleOffset = 1.0f;

        public String toString() {
            return "RenderData{imageWidth=" + this.imageWidth + ", imageHeight=" + this.imageHeight + ", scaleOffset=" + this.scaleOffset + ", margin=" + this.margin + ", bitmapRenders=" + Arrays.toString(this.bitmapRenders) + '}';
        }
    }

    /* loaded from: classes.dex */
    public static class BitmapRenderData {
        public Bitmap bitmap;
        public final RectF bitmapInsideRect = new RectF();
        public Drawable maskDrawable;
        public boolean mirror;
        public float radius;
        public int rotate;
        public boolean transition;

        public String toString() {
            return "BitmapRenderData{bitmap=" + this.bitmap + ", rotate=" + this.rotate + ", mirror=" + this.mirror + ", transition=" + this.transition + ", bitmapInsideRect=" + this.bitmapInsideRect + '}';
        }
    }

    public static RenderData generateRenderData(Context context, PosterModel posterModel, LayoutModel layoutModel, CollageLayout collageLayout, int i) {
        int childCount = collageLayout.getChildCount();
        BitmapRenderData[] bitmapRenderDataArr = new BitmapRenderData[childCount];
        for (int i2 = 0; i2 < childCount; i2++) {
            bitmapRenderDataArr[i2] = ((CollageImageView) collageLayout.getChildAt(i2)).generateBitmapRenderData();
        }
        RenderData generateRenderData = generateRenderData(context.getResources(), posterModel, layoutModel, bitmapRenderDataArr);
        generateRenderData.scaleOffset = generateRenderData.imageWidth / i;
        generateRenderData.margin = collageLayout.getMargin();
        generateRenderData.igonreMarginEdge = collageLayout.isIgnoreEdgeMargin();
        if (posterModel != null) {
            generateRenderData.posterElementRender = new PosterElementRender();
        }
        return generateRenderData;
    }

    /* JADX WARN: Code restructure failed: missing block: B:5:0x000c, code lost:
        if (r2 != 0.0f) goto L5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0015, code lost:
        if (r2 != 0.0f) goto L5;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.miui.gallery.collage.render.CollageRender.RenderData generateRenderData(android.content.res.Resources r4, com.miui.gallery.collage.core.poster.PosterModel r5, com.miui.gallery.collage.core.layout.LayoutModel r6, com.miui.gallery.collage.render.CollageRender.BitmapRenderData[] r7) {
        /*
            com.miui.gallery.collage.render.CollageRender$RenderData r0 = new com.miui.gallery.collage.render.CollageRender$RenderData
            r0.<init>()
            r1 = 0
            if (r5 == 0) goto Lf
            float r2 = r5.ratio
            int r3 = (r2 > r1 ? 1 : (r2 == r1 ? 0 : -1))
            if (r3 == 0) goto Lf
            goto L28
        Lf:
            if (r6 == 0) goto L18
            float r2 = r6.ratio
            int r1 = (r2 > r1 ? 1 : (r2 == r1 ? 0 : -1))
            if (r1 == 0) goto L18
            goto L28
        L18:
            android.util.TypedValue r1 = new android.util.TypedValue
            r1.<init>()
            r2 = 2131168483(0x7f070ce3, float:1.795127E38)
            r3 = 1
            r4.getValue(r2, r1, r3)
            float r2 = r1.getFloat()
        L28:
            r4 = 2160(0x870, float:3.027E-42)
            r0.imageWidth = r4
            float r4 = (float) r4
            float r4 = r4 / r2
            int r4 = java.lang.Math.round(r4)
            r0.imageHeight = r4
            r0.posterModel = r5
            r0.layoutModel = r6
            r0.bitmapRenders = r7
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.collage.render.CollageRender.generateRenderData(android.content.res.Resources, com.miui.gallery.collage.core.poster.PosterModel, com.miui.gallery.collage.core.layout.LayoutModel, com.miui.gallery.collage.render.CollageRender$BitmapRenderData[]):com.miui.gallery.collage.render.CollageRender$RenderData");
    }

    public static void doRender(Canvas canvas, RenderData renderData, BitmapManager bitmapManager) {
        float[] fArr;
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        RectF rectF = new RectF();
        PosterModel posterModel = renderData.posterModel;
        if (posterModel != null) {
            fArr = CollagePositionModel.getCollagePositionModelByImageSize(posterModel.collagePositions, renderData.bitmapRenders.length).position;
        } else {
            fArr = PosterLayout.DEFAULT_LAYOUT_PARAMS;
        }
        float f = width;
        float f2 = height;
        rectF.set((int) (fArr[0] * f), (int) (fArr[1] * f2), (int) (f * fArr[2]), (int) (f2 * fArr[3]));
        canvas.drawColor(-1);
        drawBitmap(canvas, renderData, rectF, bitmapManager);
        PosterElementRender posterElementRender = renderData.posterElementRender;
        if (posterElementRender != null) {
            posterElementRender.draw(canvas);
        }
    }

    public static void drawBitmap(Canvas canvas, RenderData renderData, RectF rectF, BitmapManager bitmapManager) {
        LayoutModel layoutModel;
        int i;
        LayoutModel layoutModel2 = renderData.layoutModel;
        float f = renderData.margin;
        BitmapRenderData[] bitmapRenderDataArr = renderData.bitmapRenders;
        ImageLocationProcessor imageLocationProcessor = new ImageLocationProcessor();
        RectF rectF2 = new RectF();
        BitmapItemRender bitmapItemRender = new BitmapItemRender();
        int i2 = 0;
        while (true) {
            LayoutItemModel[] layoutItemModelArr = layoutModel2.items;
            if (i2 < layoutItemModelArr.length) {
                if (i2 >= bitmapRenderDataArr.length) {
                    layoutModel = layoutModel2;
                    i = i2;
                } else {
                    LayoutItemModel layoutItemModel = layoutItemModelArr[i2];
                    ImageLocation imageLocation = new ImageLocation(layoutItemModel.clipType, layoutItemModel.data);
                    layoutModel = layoutModel2;
                    i = i2;
                    imageLocationProcessor.processorImageLocation(imageLocation, rectF.width(), rectF.height(), f * renderData.scaleOffset, renderData.igonreMarginEdge);
                    canvas.save();
                    canvas.translate(rectF.left, rectF.top);
                    canvas.clipPath(imageLocation.mPathForClip);
                    rectF2.set(imageLocation.mLeft, imageLocation.mTop, imageLocation.mRight, imageLocation.mBottom);
                    BitmapRenderData bitmapRenderData = bitmapRenderDataArr[i];
                    Bitmap bitmap = bitmapRenderData.bitmap;
                    if (bitmapManager != null) {
                        bitmapRenderData.bitmap = bitmapManager.loadSuitableBitmapBySize(renderData.imageWidth, renderData.imageHeight, bitmapManager.getOriginUriByBitmap(bitmap));
                    }
                    bitmapItemRender.drawBitmapItem(bitmapRenderData, rectF2, canvas, renderData.scaleOffset);
                    bitmapRenderData.bitmap = bitmap;
                    canvas.restore();
                }
                i2 = i + 1;
                layoutModel2 = layoutModel;
            } else {
                return;
            }
        }
    }

    public static void initBitmapMatrix(RectF rectF, Matrix matrix, RectF rectF2, RectF rectF3) {
        initBitmapMatrix(rectF, matrix, rectF2, false, 0, rectF3);
    }

    public static void initBitmapMatrix(RectF rectF, Matrix matrix, RectF rectF2, boolean z, int i, RectF rectF3) {
        matrix.reset();
        matrix.setRectToRect(rectF, rectF2, Matrix.ScaleToFit.CENTER);
        rectF3.set(rectF);
        matrix.mapRect(rectF3);
        if (z) {
            matrix.postScale(-1.0f, 1.0f, rectF3.centerX(), rectF3.centerY());
        }
        matrix.postRotate(i, rectF3.centerX(), rectF3.centerY());
        rectF3.set(rectF);
        matrix.mapRect(rectF3);
        float max = Math.max(rectF2.width() / rectF3.width(), rectF2.height() / rectF3.height());
        if (max != 1.0f) {
            matrix.postScale(max, max, rectF2.centerX(), rectF2.centerY());
            rectF3.set(rectF);
            matrix.mapRect(rectF3);
        }
    }

    /* loaded from: classes.dex */
    public static class ImageLocation {
        public int mBottom;
        public final float[] mClipArray;
        public final ClipType mClipType;
        public int mLeft;
        public final Path mPathForClip = new Path();
        public final Region mPathRegion = new Region();
        public int mRight;
        public int mTop;

        public ImageLocation(ClipType clipType, float[] fArr) {
            this.mClipType = clipType;
            this.mClipArray = fArr;
        }

        public Path getPathForClip() {
            return this.mPathForClip;
        }

        public Region getPathRegion() {
            return this.mPathRegion;
        }

        public int getLeft() {
            return this.mLeft;
        }

        public int getTop() {
            return this.mTop;
        }

        public int getRight() {
            return this.mRight;
        }

        public int getBottom() {
            return this.mBottom;
        }
    }

    /* loaded from: classes.dex */
    public static class ImageLocationProcessor {
        public float[] mClipArray;
        public ImageLocation mImageLocation;
        public RectF mPathRectF = new RectF();
        public Region mPathRegion = new Region();
        public Matrix mMatrix = new Matrix();

        public void processorImageLocation(ImageLocation imageLocation, float f, float f2, float f3, boolean z) {
            this.mImageLocation = imageLocation;
            if (f3 > 0.0f) {
                generateClipArrayBySize(f, f2);
                generatePath();
                enableMargin(f3, f, f2, z);
            } else {
                generateClipArrayBySize(f, f2);
                generatePath();
            }
            generateLayoutSizeByRect();
        }

        public final void generateLayoutSizeByRect() {
            this.mImageLocation.mLeft = Math.round(this.mPathRectF.left);
            this.mImageLocation.mTop = Math.round(this.mPathRectF.top);
            this.mImageLocation.mRight = Math.round(this.mPathRectF.right);
            this.mImageLocation.mBottom = Math.round(this.mPathRectF.bottom);
            this.mImageLocation.mPathRegion.set(this.mPathRegion);
        }

        public final void generateClipArrayBySize(float f, float f2) {
            float[] fArr = this.mImageLocation.mClipArray;
            this.mClipArray = new float[fArr.length];
            for (int i = 0; i < fArr.length; i += 2) {
                float[] fArr2 = this.mClipArray;
                fArr2[i] = fArr[i] * f;
                int i2 = i + 1;
                fArr2[i2] = fArr[i2] * f2;
            }
        }

        public final void generatePath() {
            Path path = this.mImageLocation.mPathForClip;
            path.reset();
            int i = 0;
            if (AnonymousClass1.$SwitchMap$com$miui$gallery$collage$ClipType[this.mImageLocation.mClipType.ordinal()] == 1) {
                float[] fArr = this.mClipArray;
                path.addOval(new RectF(fArr[0], fArr[1], fArr[2], fArr[3]), Path.Direction.CW);
            } else {
                while (true) {
                    float[] fArr2 = this.mClipArray;
                    if (i >= fArr2.length) {
                        break;
                    }
                    float f = fArr2[i];
                    float f2 = fArr2[i + 1];
                    if (path.isEmpty()) {
                        path.moveTo(f, f2);
                    } else {
                        path.lineTo(f, f2);
                    }
                    i += 2;
                }
                path.close();
            }
            path.computeBounds(this.mPathRectF, true);
            this.mPathRegion.setEmpty();
            Region region = this.mPathRegion;
            RectF rectF = this.mPathRectF;
            region.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
        }

        public final void enableMargin(float f, float f2, float f3, boolean z) {
            float f4;
            float f5 = 0.0f;
            if (f == 0.0f) {
                return;
            }
            ImageLocation imageLocation = this.mImageLocation;
            Path path = imageLocation.mPathForClip;
            int i = AnonymousClass1.$SwitchMap$com$miui$gallery$collage$ClipType[imageLocation.mClipType.ordinal()];
            int i2 = 2;
            char c = 1;
            if (i == 1) {
                path.reset();
                float[] fArr = this.mClipArray;
                path.addOval(new RectF(fArr[0] + f, fArr[1] + f, fArr[2] - f, fArr[3] - f), Path.Direction.CW);
                path.offset(Math.round(f), Math.round(f));
                path.computeBounds(this.mPathRectF, true);
                this.mPathRegion.setEmpty();
                Region region = this.mPathRegion;
                RectF rectF = this.mPathRectF;
                region.setPath(path, new Region((int) rectF.left, (int) rectF.top, (int) rectF.right, (int) rectF.bottom));
            } else if (i != 2) {
            } else {
                Path path2 = new Path();
                RectF rectF2 = new RectF();
                Region region2 = new Region();
                int i3 = 0;
                while (true) {
                    float[] fArr2 = this.mClipArray;
                    if (i3 < fArr2.length) {
                        float[] fArr3 = new float[i2];
                        float[] fArr4 = new float[i2];
                        fArr3[0] = fArr2[i3];
                        fArr3[c] = fArr2[i3 + 1];
                        if (i3 == fArr2.length - i2) {
                            fArr4[0] = fArr2[0];
                            fArr4[c] = fArr2[c];
                        } else {
                            fArr4[0] = fArr2[i3 + 2];
                            fArr4[c] = fArr2[i3 + 3];
                        }
                        float f6 = fArr3[0];
                        float f7 = fArr3[c];
                        float f8 = fArr4[0];
                        float f9 = fArr4[c];
                        if (((f6 != f8 ? !(f7 == f9 && (f7 == f5 || f7 == f3)) : !(f6 == f5 || f6 == f2)) ? (char) 0 : c) != 0) {
                            f4 = z ? f5 : 2.0f * f;
                        } else {
                            f4 = f;
                        }
                        RectF rectF3 = rectF2;
                        Region region3 = region2;
                        double degrees = Math.toDegrees(Math.atan2(fArr4[c] - fArr3[c], fArr4[0] - fArr3[0]));
                        DefaultLogger.d("CollageRender", "xStart:%f yStart:%f xEnd:%f yEnd:%f degree：%f", Float.valueOf(fArr3[0]), Float.valueOf(fArr3[1]), Float.valueOf(fArr4[0]), Float.valueOf(fArr4[1]), Double.valueOf(degrees));
                        this.mMatrix.reset();
                        this.mMatrix.postRotate((float) (-degrees));
                        this.mMatrix.postTranslate(0.0f, -f4);
                        this.mMatrix.postRotate((float) degrees);
                        this.mMatrix.mapPoints(fArr3);
                        this.mMatrix.mapPoints(fArr4);
                        DefaultLogger.d("CollageRender", "xStart:%f yStart:%f", Float.valueOf(fArr3[0]), Float.valueOf(fArr3[1]));
                        path2.reset();
                        path2.moveTo(f6, f7);
                        path2.lineTo(f8, f9);
                        path2.lineTo(fArr4[0], fArr4[1]);
                        path2.lineTo(fArr3[0], fArr3[1]);
                        path2.close();
                        path2.computeBounds(rectF3, true);
                        region3.setEmpty();
                        region3.setPath(path2, new Region((int) rectF3.left, (int) rectF3.top, (int) rectF3.right, (int) rectF3.bottom));
                        this.mPathRegion.op(region3, Region.Op.DIFFERENCE);
                        rectF2 = rectF3;
                        f5 = 0.0f;
                        region2 = region3;
                        i2 = 2;
                        c = 1;
                        i3 += 2;
                        path = path;
                    } else {
                        Path path3 = path;
                        path3.reset();
                        this.mPathRegion.getBoundaryPath(path3);
                        path3.computeBounds(this.mPathRectF, true);
                        return;
                    }
                }
            }
        }
    }

    /* renamed from: com.miui.gallery.collage.render.CollageRender$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$collage$ClipType;

        static {
            int[] iArr = new int[ClipType.values().length];
            $SwitchMap$com$miui$gallery$collage$ClipType = iArr;
            try {
                iArr[ClipType.CIRCLE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$collage$ClipType[ClipType.PATH.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }
}
