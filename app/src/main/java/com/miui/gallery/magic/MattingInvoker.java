package com.miui.gallery.magic;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.Log;
import com.miui.gallery.magic.tools.MagicUtils;
import com.miui.gallery.magic.util.ResourceUtil;
import com.miui.gallery.search.statistics.SearchStatUtils;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class MattingInvoker {
    public static final String TAG;

    public static float max(float f, float f2) {
        return f > f2 ? f : f2;
    }

    public static float min(float f, float f2) {
        return f < f2 ? f : f2;
    }

    private native void nativeBlending(Bitmap bitmap, byte[] bArr, Bitmap bitmap2, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, Bitmap bitmap3, int i9, int i10, int i11, int i12, int i13, int i14, int i15);

    private native int nativeDestoryModel();

    /* JADX INFO: Access modifiers changed from: private */
    public native int nativeGetContour(byte[] bArr, int i, int i2, int i3, int[] iArr);

    private native int nativeInPainting(Bitmap bitmap, byte[] bArr, int i, int i2, int i3, int i4);

    private native int nativeInitModel();

    private native int nativeSegmentPredict(byte[] bArr, byte[] bArr2, int[] iArr, int i, int i2, double d);

    static {
        String libraryDirPath = ResourceUtil.getLibraryDirPath(MagicUtils.getGalleryApp());
        System.load(libraryDirPath + "/libmace.so");
        System.load(libraryDirPath + "/libremove.so");
        System.load(libraryDirPath + "/libmibokeh_mask.so");
        System.load(libraryDirPath + "/libmatting.so");
        System.load(libraryDirPath + "/libvis.so");
        System.load(libraryDirPath + "/libmatting-lib.so");
        TAG = MattingInvoker.class.getName();
    }

    /* loaded from: classes2.dex */
    public static class BoundingBox implements Serializable {
        private int x = 0;
        private int y = 0;
        private int width = 0;
        private int height = 0;
        private int idx = 0;

        public BoundingBox cloneBoundingBox() {
            BoundingBox boundingBox = new BoundingBox();
            boundingBox.x = this.x;
            boundingBox.y = this.y;
            boundingBox.width = this.width;
            boundingBox.height = this.height;
            boundingBox.idx = this.idx;
            return boundingBox;
        }

        public int getX() {
            return this.x;
        }

        public void setX(int i) {
            this.x = i;
        }

        public int getY() {
            return this.y;
        }

        public void setY(int i) {
            this.y = i;
        }

        public int getWidth() {
            return this.width;
        }

        public void setWidth(int i) {
            this.width = i;
        }

        public int getHeight() {
            return this.height;
        }

        public void setHeight(int i) {
            this.height = i;
        }

        public int getIdx() {
            return this.idx;
        }

        public Rect toRect() {
            int i = this.x;
            int i2 = this.y;
            return new Rect(i, i2, this.width + i, this.height + i2);
        }
    }

    /* loaded from: classes2.dex */
    public static class SegmentResult implements Serializable {
        private byte[] orginalMask;
        private ArrayList<byte[]> personMasks = new ArrayList<>();
        private List<BoundingBox> boxes = null;
        private int width = 0;
        private int height = 0;
        private Point[][] pointCache = null;

        public int mirrorPerson(int i) {
            BoundingBox personBox = getPersonBox(i);
            byte[] personMask = getPersonMask(i);
            clearContour();
            this.boxes.add(personBox.cloneBoundingBox());
            byte[] bArr = new byte[personMask.length];
            System.arraycopy(personMask, 0, bArr, 0, personMask.length);
            this.personMasks.add(bArr);
            return this.boxes.size() - 1;
        }

        public void clearContour() {
            this.pointCache = null;
        }

        public void saveToFile(Activity activity, String str) throws IOException {
            Point[][] pointArr = this.pointCache;
            this.pointCache = null;
            try {
                FileOutputStream openFileOutput = activity.openFileOutput(str, 0);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(openFileOutput);
                objectOutputStream.writeObject(this);
                this.pointCache = pointArr;
                objectOutputStream.close();
                if (openFileOutput == null) {
                    return;
                }
                openFileOutput.close();
            } catch (FileNotFoundException e) {
                throw new IOException(e);
            }
        }

        public static SegmentResult loadFromFile(Activity activity, String str) {
            try {
                FileInputStream openFileInput = activity.openFileInput(str);
                ObjectInputStream objectInputStream = new ObjectInputStream(openFileInput);
                SegmentResult segmentResult = (SegmentResult) objectInputStream.readObject();
                objectInputStream.close();
                if (openFileInput != null) {
                    openFileInput.close();
                }
                return segmentResult;
            } catch (IOException | ClassNotFoundException unused) {
                return null;
            }
        }

        public int getWidth() {
            return this.width;
        }

        public void setWidth(int i) {
            this.width = i;
        }

        public int getHeight() {
            return this.height;
        }

        public void setHeight(int i) {
            this.height = i;
        }

        public byte[] getOrginalMask() {
            return this.orginalMask;
        }

        public void resetMaskByBitmap(int i, Bitmap bitmap, int i2, boolean z) {
            boolean z2;
            boolean z3;
            byte[] bArr;
            int i3;
            int i4;
            int width = getWidth();
            int height = getHeight();
            if (bitmap.getWidth() != width && bitmap.getHeight() != height) {
                throw new Error("input的宽高必须与当前result相同");
            }
            BoundingBox personBox = getPersonBox(i);
            byte b = (byte) personBox.idx;
            int[] iArr = new int[width * height];
            int i5 = personBox.x;
            int i6 = personBox.y;
            int i7 = personBox.x + personBox.width;
            int i8 = personBox.y + personBox.height;
            bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
            byte[] personMask = getPersonMask(i);
            boolean z4 = true;
            for (int i9 = 0; i9 < width; i9++) {
                int i10 = 0;
                while (i10 < height) {
                    int i11 = iArr[(i10 * width) + i9];
                    if (i11 < i2 - 20 || i11 > i2 + 20) {
                        z2 = z;
                        z3 = false;
                    } else {
                        z2 = z;
                        z3 = true;
                    }
                    byte b2 = z3 == z2 ? (byte) 0 : b;
                    byte[] maskValueByPoint = setMaskValueByPoint(personMask, b2, i9, i10);
                    if (b2 != 0) {
                        if (z4) {
                            i3 = i9;
                            i5 = i3;
                            i4 = i10;
                            i6 = i4;
                            z4 = false;
                        } else {
                            i3 = i7;
                            i4 = i8;
                        }
                        float f = i9;
                        i5 = (int) MattingInvoker.min(i5, f);
                        bArr = maskValueByPoint;
                        float f2 = i10;
                        i6 = (int) MattingInvoker.min(i6, f2);
                        i8 = (int) MattingInvoker.max(i4, f2);
                        i7 = (int) MattingInvoker.max(i3, f);
                    } else {
                        bArr = maskValueByPoint;
                    }
                    i10++;
                    personMask = bArr;
                }
            }
            personBox.setX(i5);
            personBox.setY(i6);
            personBox.setWidth((i7 - i5) + 1);
            personBox.setHeight((i8 - i6) + 1);
            this.pointCache[i] = null;
        }

        public final byte[] setMaskValueByPoint(byte[] bArr, byte b, int i, int i2) {
            int i3 = (((i2 * this.width) + i) * 2) + 1;
            if (i3 >= 1 && i3 < bArr.length) {
                bArr[i3 - 1] = b;
                bArr[i3] = b == 0 ? (byte) 0 : (byte) -2;
            }
            return bArr;
        }

        public PathResult getPersonDrawPath(float f, float f2, int i, Matrix matrix) {
            PathResult pathResult = new PathResult();
            float width = getWidth();
            float height = getHeight();
            float f3 = f / width;
            float height2 = (f2 - (getHeight() * f3)) / 2.0f;
            float f4 = 0.0f;
            if (height / width > 1.4f) {
                f3 = f2 / height;
                height2 = 0.0f;
                f4 = (f - (width * f3)) / 2.0f;
            }
            pathResult.setScale(f3);
            pathResult.setX(f4);
            pathResult.setY(height2);
            Path path = new Path();
            Point[] contour = getContour(i);
            float[] fArr = new float[2];
            if (contour.length > 2) {
                Point point = contour[0];
                apply(fArr, pathResult, point.x, point.y, matrix);
                path.moveTo(fArr[0], fArr[1]);
                for (int i2 = 1; i2 < contour.length; i2++) {
                    Point point2 = contour[i2];
                    apply(fArr, pathResult, point2.x, point2.y, matrix);
                    path.lineTo(fArr[0], fArr[1]);
                }
                apply(fArr, pathResult, point.x, point.y, matrix);
                path.lineTo(fArr[0], fArr[1]);
            }
            pathResult.setPath(path);
            return pathResult;
        }

        public final void apply(float[] fArr, PathResult pathResult, float f, float f2, Matrix matrix) {
            fArr[0] = pathResult.getNX(f);
            fArr[1] = pathResult.getNY(f2);
            matrix.mapPoints(fArr);
        }

        public boolean isEmpty() {
            return getPersonCount() == 0;
        }

        public boolean isEmpty(int i) {
            return isEmpty(i, 100);
        }

        public boolean isEmpty(int i, int i2) {
            if (i2 < 0) {
                throw new Error("Max cannot be less than 0");
            }
            byte[] personMask = getPersonMask(i);
            int i3 = 0;
            for (int i4 = 0; i4 < personMask.length; i4 += 2) {
                if (personMask[i4] != 0 && (i3 = i3 + 1) >= i2) {
                    return false;
                }
            }
            return true;
        }

        public int getPersonCount() {
            List<BoundingBox> list = this.boxes;
            if (list == null) {
                return 0;
            }
            return list.size();
        }

        public Point[] getContour(int i) {
            if (this.pointCache == null) {
                this.pointCache = new Point[getPersonCount()];
            }
            BoundingBox personBox = getPersonBox(i);
            Point[][] pointArr = this.pointCache;
            if (i >= pointArr.length) {
                return getContourByMask(getPersonMask(i), getWidth(), getHeight(), personBox.getIdx());
            }
            Point[] pointArr2 = pointArr[i];
            if (pointArr2 != null) {
                return pointArr2;
            }
            Point[] contourByMask = getContourByMask(getPersonMask(i), getWidth(), getHeight(), personBox.getIdx());
            this.pointCache[i] = contourByMask;
            return contourByMask;
        }

        public static Point[] getContourByMask(byte[] bArr, int i, int i2, int i3) {
            int[] iArr = new int[i * i2 * 2];
            return toPoints(iArr, new MattingInvoker().nativeGetContour(bArr, i, i2, i3, iArr));
        }

        public static Point[] toPoints(int[] iArr, int i) {
            if (i < 1) {
                return new Point[0];
            }
            Point[] pointArr = new Point[i];
            for (int i2 = 0; i2 < i; i2++) {
                int i3 = i2 * 2;
                pointArr[i2] = new Point(iArr[i3], iArr[i3 + 1]);
            }
            return pointArr;
        }

        public BoundingBox getPersonBox(int i) {
            return this.boxes.get(i);
        }

        public Rect getPersonRect(int i) {
            return convertToRect(getPersonBox(i));
        }

        public final Rect convertToRect(BoundingBox boundingBox) {
            return boundingBox.toRect();
        }

        public static Bitmap getMaskBitmapForFilter(byte[] bArr, int i, int i2) {
            Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ALPHA_8);
            for (int i3 = 0; i3 < i2; i3++) {
                for (int i4 = 0; i4 < i; i4++) {
                    int i5 = ((i3 * i) + i4) * 2;
                    if (bArr[i5] != 0) {
                        createBitmap.setPixel(i4, i3, (bArr[i5 + 1] & 255) << 24);
                    }
                }
            }
            return createBitmap;
        }

        public void drawPerson(Bitmap bitmap, int i, Bitmap bitmap2, int i2, int i3) {
            drawPerson(bitmap, i, bitmap2, i2, i3, -1717);
        }

        public void drawPerson(Bitmap bitmap, int i, Bitmap bitmap2, int i2, int i3, int i4) {
            drawPersonByBox(i, bitmap, getPersonBox(i), bitmap2, i2, i3, i4);
        }

        public void drawPersonByBox(int i, Bitmap bitmap, BoundingBox boundingBox, Bitmap bitmap2, int i2, int i3, int i4) {
            Canvas canvas = new Canvas(bitmap2);
            for (int x = boundingBox.getX(); x < boundingBox.getWidth() + boundingBox.getX(); x++) {
                for (int y = boundingBox.getY(); y < boundingBox.getHeight() + boundingBox.getY(); y++) {
                    drawPixel(canvas, i, bitmap, bitmap2, i4, boundingBox.idx, i2, i3, x, y);
                }
            }
        }

        public final void drawPixel(Canvas canvas, int i, Bitmap bitmap, Bitmap bitmap2, int i2, int i3, int i4, int i5, int i6, int i7) {
            byte[] personMask = getPersonMask(i);
            int i8 = ((this.width * i7) + i6) * 2;
            if (i8 >= personMask.length || personMask[i8] != i3) {
                return;
            }
            int i9 = i4 + i6;
            int i10 = i5 + i7;
            if (i9 < 0 || i10 < 0 || i9 >= bitmap2.getWidth() || i10 >= bitmap2.getHeight()) {
                return;
            }
            if (i2 == -1717) {
                if (bitmap == null || bitmap.isRecycled()) {
                    return;
                }
                int pixel = bitmap.getPixel(i6, i7);
                Paint paint = new Paint();
                paint.setARGB(personMask[i8 + 1], Color.red(pixel), Color.green(pixel), Color.blue(pixel));
                paint.setStrokeWidth(1.0f);
                canvas.drawPoint(i9, i10, paint);
                return;
            }
            bitmap2.setPixel(i9, i10, i2);
        }

        public final void copyMaskToPersonMask(SegmentResult segmentResult) {
            segmentResult.personMasks.clear();
            byte[] orginalMask = segmentResult.getOrginalMask();
            for (int i = 0; i < segmentResult.boxes.size(); i++) {
                byte[] bArr = new byte[orginalMask.length];
                System.arraycopy(orginalMask, 0, bArr, 0, orginalMask.length);
                segmentResult.personMasks.add(bArr);
            }
        }

        public byte[] getPersonMask(int i) {
            if (this.personMasks.size() < 1) {
                copyMaskToPersonMask(this);
            }
            return this.personMasks.get(i);
        }
    }

    public static byte[] bitmap2BGR(Bitmap bitmap) {
        Log.d("IMAGE UTIL", "bitmap2BGR: bitmap2BGR");
        ByteBuffer allocate = ByteBuffer.allocate(bitmap.getByteCount());
        bitmap.copyPixelsToBuffer(allocate);
        byte[] array = allocate.array();
        byte[] bArr = new byte[(array.length / 4) * 3];
        for (int i = 0; i < array.length / 4; i++) {
            int i2 = i * 3;
            int i3 = i * 4;
            bArr[i2] = array[i3 + 2];
            bArr[i2 + 1] = array[i3 + 1];
            bArr[i2 + 2] = array[i3];
        }
        return bArr;
    }

    public SegmentResult segmentPredict(Bitmap bitmap) {
        byte[] bitmap2BGR = bitmap2BGR(bitmap);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        byte[] bArr = new byte[width * height * 2];
        int[] iArr = new int[60];
        nativeSegmentPredict(bitmap2BGR, bArr, iArr, width, height, SearchStatUtils.POW);
        SegmentResult segmentResult = new SegmentResult();
        segmentResult.boxes = getBoundingBox(iArr);
        segmentResult.orginalMask = bArr;
        segmentResult.setHeight(height);
        segmentResult.setWidth(width);
        return segmentResult;
    }

    public final List<BoundingBox> getBoundingBox(int[] iArr) {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < 10; i++) {
            int i2 = (i * 6) + 5;
            if (iArr[i2] == 1) {
                BoundingBox boundingBox = new BoundingBox();
                boundingBox.x = iArr[i2 - 5];
                boundingBox.y = iArr[i2 - 4];
                boundingBox.width = iArr[i2 - 3];
                boundingBox.height = iArr[i2 - 2];
                boundingBox.idx = iArr[i2 - 1];
                arrayList.add(boundingBox);
            }
        }
        return arrayList;
    }

    public int inPainting(Bitmap bitmap, SegmentResult segmentResult) {
        return inPainting(bitmap, segmentResult, -1);
    }

    public int inPainting(Bitmap bitmap, SegmentResult segmentResult, int i) {
        byte[] orginalMask;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (i >= 0) {
            orginalMask = segmentResult.getPersonMask(i);
        } else {
            orginalMask = segmentResult.getOrginalMask();
        }
        return nativeInPainting(bitmap, orginalMask, width, height, width, height);
    }

    public Bitmap halfBlending(Bitmap bitmap, Bitmap bitmap2, SegmentResult segmentResult, int i, BlendConfig blendConfig) {
        BoundingBox personBox = segmentResult.getPersonBox(i);
        int width = personBox.getWidth();
        int height = personBox.getHeight();
        new Matrix();
        float scale = 1.0f / blendConfig.getScale();
        float degrees = 360.0f - blendConfig.getDegrees();
        PointF pointF = new PointF(blendConfig.getPoint().x, blendConfig.getPoint().y);
        new PointF((pointF.x - (width / 2.0f)) * scale, (pointF.y - (height / 2.0f)) * scale);
        Paint paint = new Paint(3);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20.0f);
        paint.setColor(-65536);
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees, pointF.x, pointF.y);
        matrix.postTranslate(-pointF.x, -pointF.y);
        matrix.postScale(scale, scale);
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawColor(-256);
        canvas.drawBitmap(bitmap2, matrix, paint);
        if (blendConfig.isMirrorImage()) {
            createBitmap = mirrorIt(createBitmap);
        }
        return blending2(bitmap, createBitmap, segmentResult, i, new Rect(0, 0, width, height));
    }

    public Bitmap blending(Bitmap bitmap, Bitmap bitmap2, SegmentResult segmentResult, int i, BlendConfig blendConfig) {
        BoundingBox personBox = segmentResult.getPersonBox(i);
        int width = personBox.getWidth();
        int height = personBox.getHeight();
        new Matrix();
        float scale = 1.0f / blendConfig.getScale();
        float degrees = 360.0f - blendConfig.getDegrees();
        PointF pointF = new PointF(blendConfig.getPoint().x, blendConfig.getPoint().y);
        new PointF((pointF.x - (width / 2.0f)) * scale, (pointF.y - (height / 2.0f)) * scale);
        Paint paint = new Paint(3);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(20.0f);
        paint.setColor(-65536);
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees, pointF.x, pointF.y);
        matrix.postTranslate(-pointF.x, -pointF.y);
        matrix.postScale(scale, scale);
        Bitmap createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawColor(0);
        canvas.drawBitmap(bitmap2, matrix, paint);
        if (blendConfig.isMirrorImage()) {
            createBitmap = mirrorIt(createBitmap);
        }
        Bitmap bitmap3 = createBitmap;
        blending(bitmap, bitmap3, segmentResult, i, new Rect(0, 0, width, height));
        if (blendConfig.getContourConfigure() != null) {
            ContourHelper.drawBitmap(bitmap, segmentResult, i, bitmap3, blendConfig.getContourConfigure());
        }
        if (blendConfig.isMirrorImage()) {
            bitmap3 = mirrorIt(bitmap3);
        }
        Canvas canvas2 = new Canvas(bitmap2);
        Matrix matrix2 = new Matrix();
        matrix2.postScale(blendConfig.getScale(), blendConfig.getScale());
        matrix2.postTranslate(pointF.x, pointF.y);
        matrix2.postRotate(blendConfig.getDegrees(), pointF.x, pointF.y);
        canvas2.drawBitmap(bitmap3, matrix2, paint);
        bitmap3.recycle();
        return bitmap2;
    }

    public final Bitmap mirrorIt(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        matrix.setScale(-1.0f, 1.0f);
        Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        bitmap.recycle();
        return createBitmap;
    }

    public Bitmap blending(Bitmap bitmap, Bitmap bitmap2, SegmentResult segmentResult, int i, Rect rect) {
        int i2;
        int i3;
        Rect personRect = segmentResult.getPersonRect(i);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        BoundingBox personBox = segmentResult.getPersonBox(i);
        Bitmap createBitmap = Bitmap.createBitmap(personBox.width, personBox.height, Bitmap.Config.ARGB_8888);
        if (bitmap2 != null) {
            i2 = bitmap2.getWidth();
            i3 = bitmap2.getHeight();
        } else {
            i2 = 0;
            i3 = 0;
        }
        nativeBlending(bitmap, segmentResult.getPersonMask(i), createBitmap, width, height, personRect.left, personRect.top, personRect.width(), personRect.height(), width, height, bitmap2, i2, i3, rect.left, rect.top, rect.width(), rect.height(), personBox.idx);
        new Canvas(bitmap2).drawBitmap(createBitmap, rect.left, rect.top, new Paint());
        return bitmap2;
    }

    public Bitmap blending2(Bitmap bitmap, Bitmap bitmap2, SegmentResult segmentResult, int i, Rect rect) {
        int i2;
        int i3;
        Rect personRect = segmentResult.getPersonRect(i);
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        BoundingBox personBox = segmentResult.getPersonBox(i);
        Bitmap createBitmap = Bitmap.createBitmap(personBox.width, personBox.height, Bitmap.Config.ARGB_8888);
        if (bitmap2 != null) {
            i2 = bitmap2.getWidth();
            i3 = bitmap2.getHeight();
        } else {
            i2 = 0;
            i3 = 0;
        }
        nativeBlending(bitmap, segmentResult.getPersonMask(i), createBitmap, width, height, personRect.left, personRect.top, personRect.width(), personRect.height(), width, height, bitmap2, i2, i3, rect.left, rect.top, rect.width(), rect.height(), personBox.idx);
        return createBitmap;
    }

    public void initModel() {
        if (nativeInitModel() == 0) {
            return;
        }
        throw new Error("Model init was fail.");
    }

    public void destoryModel() {
        if (nativeDestoryModel() != 0) {
            Log.e(TAG, "Model destory was fail.");
        }
    }
}
