package com.nexstreaming.nexeditorsdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.FaceDetector;
import android.util.Log;
import com.nexstreaming.nexeditorsdk.nexSaveDataFormat;
import java.io.File;

/* loaded from: classes3.dex */
public final class nexCrop implements Cloneable {
    public static final int ABSTRACT_DIMENSION = 100000;
    public static final String TAG = "nexCrop";
    private int clip_druation;
    private int facedetect_speed;
    private boolean m_endLock;
    private float[] m_endMatrix;
    public int m_endPositionBottom;
    public int m_endPositionLeft;
    public int m_endPositionRight;
    public int m_endPositionTop;
    private float m_faceBounds_bottom;
    private float m_faceBounds_left;
    private float m_faceBounds_right;
    private boolean m_faceBounds_set;
    private float m_faceBounds_top;
    public int m_facePositionBottom;
    public int m_facePositionLeft;
    public int m_facePositionRight;
    public int m_facePositionTop;
    private int m_height;
    private final String m_mediaPath;
    public int m_rotatedEndPositionBottom;
    public int m_rotatedEndPositionLeft;
    public int m_rotatedEndPositionRight;
    public int m_rotatedEndPositionTop;
    public int m_rotatedFacePositionBottom;
    public int m_rotatedFacePositionLeft;
    public int m_rotatedFacePositionRight;
    public int m_rotatedFacePositionTop;
    public int m_rotatedStartPositionBottom;
    public int m_rotatedStartPositionLeft;
    public int m_rotatedStartPositionRight;
    public int m_rotatedStartPositionTop;
    public int m_rotation;
    private boolean m_startLock;
    private float[] m_startMatrix;
    public int m_startPositionBottom;
    public int m_startPositionLeft;
    public int m_startPositionRight;
    public int m_startPositionTop;
    private int m_width;

    /* loaded from: classes3.dex */
    public enum CropMode {
        FIT,
        FILL,
        PAN_RAND,
        PAN_FACE,
        NONE,
        PANORAMA
    }

    private boolean isSolid() {
        String str = this.m_mediaPath;
        return str != null && str.startsWith("@solid:") && this.m_mediaPath.endsWith(".jpg");
    }

    public static nexCrop clone(nexCrop nexcrop) {
        try {
            return (nexCrop) nexcrop.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public nexCrop(String str, int i, int i2, int i3, int i4) {
        this.m_startPositionLeft = 0;
        this.m_startPositionBottom = 0;
        this.m_startPositionRight = 0;
        this.m_startPositionTop = 0;
        this.m_endPositionLeft = 0;
        this.m_endPositionBottom = 0;
        this.m_endPositionRight = 0;
        this.m_endPositionTop = 0;
        this.m_rotatedStartPositionLeft = 0;
        this.m_rotatedStartPositionBottom = 0;
        this.m_rotatedStartPositionRight = 0;
        this.m_rotatedStartPositionTop = 0;
        this.m_rotatedEndPositionLeft = 0;
        this.m_rotatedEndPositionBottom = 0;
        this.m_rotatedEndPositionRight = 0;
        this.m_rotatedEndPositionTop = 0;
        this.m_facePositionLeft = 0;
        this.m_facePositionTop = 0;
        this.m_facePositionRight = 0;
        this.m_facePositionBottom = 0;
        this.m_rotatedFacePositionLeft = 0;
        this.m_rotatedFacePositionTop = 0;
        this.m_rotatedFacePositionRight = 0;
        this.m_rotatedFacePositionBottom = 0;
        this.m_startLock = false;
        this.m_endLock = false;
        this.m_rotation = 0;
        this.m_width = 0;
        this.m_height = 0;
        this.facedetect_speed = 101;
        this.clip_druation = 0;
        this.m_mediaPath = str;
        this.m_width = i;
        this.m_height = i2;
        this.m_rotation = i3;
        this.clip_druation = i4;
        randomizeStartEndPosition(false, CropMode.PAN_RAND);
    }

    public void setRotate(int i) {
        if (i != this.m_rotation) {
            this.m_rotation = i;
            if (i == 90 || i == 270) {
                if (this.m_rotatedStartPositionLeft != 0 || this.m_rotatedStartPositionBottom != 0 || this.m_rotatedStartPositionRight != 0 || this.m_rotatedStartPositionTop != 0) {
                    return;
                }
                randomizeStartEndPosition(false, CropMode.PAN_RAND);
            } else if (this.m_startPositionLeft != 0 || this.m_startPositionBottom != 0 || this.m_startPositionRight != 0 || this.m_startPositionTop != 0) {
            } else {
                randomizeStartEndPosition(false, CropMode.PAN_RAND);
            }
        }
    }

    public int getRotate() {
        return this.m_rotation;
    }

    public int getWidth() {
        return this.m_width;
    }

    public int getHeight() {
        return this.m_height;
    }

    private Matrix matrixForRect(Rect rect) {
        return matrixForRect(rect.left, rect.top, rect.right, rect.bottom);
    }

    private Matrix matrixForRect(float f, float f2, float f3, float f4) {
        Matrix matrix = new Matrix();
        matrix.postTranslate(-f, -f2);
        matrix.postScale(1.7777778f / (f3 - f), 1.0f / (f4 - f2));
        return matrix;
    }

    public void shrinkToAspect(Rect rect, float f) {
        if (rect.width() / rect.height() < f) {
            int width = (int) (rect.width() / f);
            int centerY = rect.centerY() - (width / 2);
            rect.top = centerY;
            rect.bottom = centerY + width;
            return;
        }
        int height = (int) (rect.height() * f);
        int centerX = rect.centerX() - (height / 2);
        rect.left = centerX;
        rect.right = centerX + height;
    }

    public void growToAspect(Rect rect, float f) {
        if (rect.width() / rect.height() < f) {
            int height = (int) (rect.height() * f);
            int centerX = rect.centerX() - (height / 2);
            rect.left = centerX;
            rect.right = centerX + height;
            return;
        }
        int width = (int) (rect.width() / f);
        int centerY = rect.centerY() - (width / 2);
        rect.top = centerY;
        rect.bottom = centerY + width;
    }

    public void randomizeStartEndPosition(boolean z, CropMode cropMode) {
        int height;
        int width;
        Rect rect = new Rect();
        Rect rect2 = new Rect();
        getStartPosition(rect);
        getEndPosition(rect2);
        int i = this.m_rotation;
        if (i == 90 || i == 270) {
            height = getHeight();
            width = getWidth();
        } else {
            height = getWidth();
            width = getHeight();
        }
        int i2 = height;
        int i3 = width;
        if (i2 < 1 || i3 < 1) {
            return;
        }
        if (isSolid()) {
            Rect rect3 = new Rect(2, 2, i2 - 2, i3 - 2);
            setStartPosition(rect3);
            setEndPosition(rect3);
        } else if (cropMode == CropMode.FILL) {
            Rect rect4 = new Rect(0, 0, i2, i3);
            shrinkToAspect(rect4, nexApplicationConfig.getAspectRatio());
            setStartPosition(rect4);
            setEndPosition(rect4);
        } else if (cropMode == CropMode.FIT) {
            Rect rect5 = new Rect(0, 0, i2, i3);
            growToAspect(rect5, nexApplicationConfig.getAspectRatio());
            setStartPosition(rect5);
            setEndPosition(rect5);
        } else if (cropMode == CropMode.PANORAMA) {
            float f = i2 / i3;
            if (f < nexApplicationConfig.getAspectRatio()) {
                int aspectRatio = (int) (i2 / nexApplicationConfig.getAspectRatio());
                Rect rect6 = new Rect(0, 0, i2, aspectRatio);
                Rect rect7 = new Rect(0, i3 - aspectRatio, i2, i3);
                if (Math.random() < 0.5d) {
                    setStartPosition(rect6);
                    setEndPosition(rect7);
                    return;
                }
                setStartPosition(rect7);
                setEndPosition(rect6);
            } else if (f > nexApplicationConfig.getAspectRatio()) {
                int aspectRatio2 = (int) (i3 * nexApplicationConfig.getAspectRatio());
                Rect rect8 = new Rect(0, 0, aspectRatio2, i3);
                Rect rect9 = new Rect(i2 - aspectRatio2, 0, i2, i3);
                if (Math.random() < 0.5d) {
                    setStartPosition(rect8);
                    setEndPosition(rect9);
                    return;
                }
                setStartPosition(rect9);
                setEndPosition(rect8);
            } else {
                Rect rect10 = new Rect(0, 0, i2, i3);
                shrinkToAspect(rect10, nexApplicationConfig.getAspectRatio());
                setStartPosition(rect10);
                setEndPosition(rect10);
            }
        } else {
            Rect rect11 = new Rect();
            if (rect11.isEmpty()) {
                rect11.set(0, 0, (i2 * 3) / 4, (i3 * 3) / 4);
                rect11.offset((int) ((i2 * Math.random()) / 4.0d), (int) ((i3 * Math.random()) / 4.0d));
            } else {
                int width2 = (i2 / 4) - rect11.width();
                if (width2 >= 2) {
                    int i4 = width2 / 2;
                    rect11.left -= i4;
                    rect11.right += i4;
                }
                int height2 = (i3 / 4) - rect11.height();
                if (height2 >= 2) {
                    int i5 = height2 / 2;
                    rect11.top -= i5;
                    rect11.bottom += i5;
                }
                growToAspect(rect11, nexApplicationConfig.getAspectRatio());
                if (!rect11.intersect(0, 0, i2, i3)) {
                    rect11.set(0, 0, (i2 * 2) / 3, (i3 * 2) / 3);
                    rect11.offset((int) ((i2 * Math.random()) / 3.0d), (int) ((i3 * Math.random()) / 3.0d));
                }
            }
            shrinkToAspect(rect11, nexApplicationConfig.getAspectRatio());
            Rect rect12 = new Rect(0, 0, i2, i3);
            applyCropSpeed(rect12, rect11, i2, i3, getFaceDetectSpeed(), getClipDuration());
            shrinkToAspect(rect12, nexApplicationConfig.getAspectRatio());
            shrinkToAspect(rect11, nexApplicationConfig.getAspectRatio());
            if (!rect11.isEmpty()) {
                if (Math.random() < 0.5d) {
                    setStartPosition(rect12);
                    setEndPosition(rect11);
                } else {
                    setStartPosition(rect11);
                    setEndPosition(rect12);
                }
            }
            Math.random();
        }
    }

    public void randomizeStartEndPosition(boolean z, CropMode cropMode, float f) {
        int height;
        int width;
        Rect rect = new Rect();
        Rect rect2 = new Rect();
        getStartPosition(rect);
        getEndPosition(rect2);
        int i = this.m_rotation;
        if (i == 90 || i == 270) {
            height = getHeight();
            width = getWidth();
        } else {
            height = getWidth();
            width = getHeight();
        }
        int i2 = height;
        int i3 = width;
        if (i2 < 1 || i3 < 1) {
            return;
        }
        if (isSolid()) {
            Rect rect3 = new Rect(2, 2, i2 - 2, i3 - 2);
            setStartPosition(rect3);
            setEndPosition(rect3);
        } else if (cropMode == CropMode.FILL) {
            Rect rect4 = new Rect(0, 0, i2, i3);
            shrinkToAspect(rect4, f);
            setStartPosition(rect4);
            setEndPosition(rect4);
        } else if (cropMode == CropMode.FIT) {
            Rect rect5 = new Rect(0, 0, i2, i3);
            growToAspect(rect5, f);
            setStartPosition(rect5);
            setEndPosition(rect5);
        } else if (cropMode == CropMode.PANORAMA) {
            float f2 = i2 / i3;
            if (f2 < f) {
                int i4 = (int) (i2 / f);
                Rect rect6 = new Rect(0, 0, i2, i4);
                Rect rect7 = new Rect(0, i3 - i4, i2, i3);
                if (Math.random() < 0.5d) {
                    setStartPosition(rect6);
                    setEndPosition(rect7);
                    return;
                }
                setStartPosition(rect7);
                setEndPosition(rect6);
            } else if (f2 > f) {
                int i5 = (int) (i3 * f);
                Rect rect8 = new Rect(0, 0, i5, i3);
                Rect rect9 = new Rect(i2 - i5, 0, i2, i3);
                if (Math.random() < 0.5d) {
                    setStartPosition(rect8);
                    setEndPosition(rect9);
                    return;
                }
                setStartPosition(rect9);
                setEndPosition(rect8);
            } else {
                Rect rect10 = new Rect(0, 0, i2, i3);
                shrinkToAspect(rect10, f);
                setStartPosition(rect10);
                setEndPosition(rect10);
            }
        } else {
            Rect rect11 = new Rect();
            if (rect11.isEmpty()) {
                rect11.set(0, 0, (i2 * 3) / 4, (i3 * 3) / 4);
                rect11.offset((int) ((i2 * Math.random()) / 4.0d), (int) ((i3 * Math.random()) / 4.0d));
            } else {
                int width2 = (i2 / 4) - rect11.width();
                if (width2 >= 2) {
                    int i6 = width2 / 2;
                    rect11.left -= i6;
                    rect11.right += i6;
                }
                int height2 = (i3 / 4) - rect11.height();
                if (height2 >= 2) {
                    int i7 = height2 / 2;
                    rect11.top -= i7;
                    rect11.bottom += i7;
                }
                growToAspect(rect11, f);
                if (!rect11.intersect(0, 0, i2, i3)) {
                    rect11.set(0, 0, (i2 * 2) / 3, (i3 * 2) / 3);
                    rect11.offset((int) ((i2 * Math.random()) / 3.0d), (int) ((i3 * Math.random()) / 3.0d));
                }
            }
            shrinkToAspect(rect11, f);
            Rect rect12 = new Rect(0, 0, i2, i3);
            applyCropSpeed(rect12, rect11, i2, i3, getFaceDetectSpeed(), this.clip_druation);
            shrinkToAspect(rect12, nexApplicationConfig.getAspectRatio());
            shrinkToAspect(rect11, nexApplicationConfig.getAspectRatio());
            if (!rect11.isEmpty()) {
                if (Math.random() < 0.5d) {
                    setStartPosition(rect12);
                    setEndPosition(rect11);
                } else {
                    setStartPosition(rect11);
                    setEndPosition(rect12);
                }
            }
            Math.random();
        }
    }

    public void getStartPosition(Rect rect) {
        int i = this.m_rotation;
        if (i == 90 || i == 270) {
            int i2 = this.m_rotatedStartPositionBottom;
            int i3 = this.m_width;
            rect.bottom = (i2 * i3) / ABSTRACT_DIMENSION;
            int i4 = this.m_rotatedStartPositionLeft;
            int i5 = this.m_height;
            rect.left = (i4 * i5) / ABSTRACT_DIMENSION;
            rect.right = (this.m_rotatedStartPositionRight * i5) / ABSTRACT_DIMENSION;
            rect.top = (this.m_rotatedStartPositionTop * i3) / ABSTRACT_DIMENSION;
            return;
        }
        int i6 = this.m_startPositionBottom;
        int i7 = this.m_height;
        rect.bottom = (i6 * i7) / ABSTRACT_DIMENSION;
        int i8 = this.m_startPositionLeft;
        int i9 = this.m_width;
        rect.left = (i8 * i9) / ABSTRACT_DIMENSION;
        rect.right = (this.m_startPositionRight * i9) / ABSTRACT_DIMENSION;
        rect.top = (this.m_startPositionTop * i7) / ABSTRACT_DIMENSION;
    }

    public void setStartPosition(Rect rect) {
        int i = this.m_rotation;
        if (i == 90 || i == 270) {
            int i2 = rect.bottom * ABSTRACT_DIMENSION;
            int i3 = this.m_width;
            this.m_rotatedStartPositionBottom = i2 / i3;
            int i4 = rect.left * ABSTRACT_DIMENSION;
            int i5 = this.m_height;
            this.m_rotatedStartPositionLeft = i4 / i5;
            this.m_rotatedStartPositionRight = (rect.right * ABSTRACT_DIMENSION) / i5;
            this.m_rotatedStartPositionTop = (rect.top * ABSTRACT_DIMENSION) / i3;
        } else {
            int i6 = rect.bottom * ABSTRACT_DIMENSION;
            int i7 = this.m_height;
            this.m_startPositionBottom = i6 / i7;
            int i8 = rect.left * ABSTRACT_DIMENSION;
            int i9 = this.m_width;
            this.m_startPositionLeft = i8 / i9;
            this.m_startPositionRight = (rect.right * ABSTRACT_DIMENSION) / i9;
            this.m_startPositionTop = (rect.top * ABSTRACT_DIMENSION) / i7;
        }
        if (this.m_startMatrix == null) {
            this.m_startMatrix = new float[9];
        }
        matrixForRect(rect).setValues(this.m_startMatrix);
    }

    public void setStartPositionRaw(Rect rect) {
        int i = this.m_rotation;
        if (i == 90 || i == 270) {
            this.m_rotatedStartPositionBottom = rect.bottom;
            this.m_rotatedStartPositionLeft = rect.left;
            this.m_rotatedStartPositionRight = rect.right;
            this.m_rotatedStartPositionTop = rect.top;
            return;
        }
        this.m_startPositionBottom = rect.bottom;
        this.m_startPositionLeft = rect.left;
        this.m_startPositionRight = rect.right;
        this.m_startPositionTop = rect.top;
    }

    public void setEndPositionRaw(Rect rect) {
        int i = this.m_rotation;
        if (i == 90 || i == 270) {
            this.m_rotatedEndPositionBottom = rect.bottom;
            this.m_rotatedEndPositionLeft = rect.left;
            this.m_rotatedEndPositionRight = rect.right;
            this.m_rotatedEndPositionTop = rect.top;
            return;
        }
        this.m_endPositionBottom = rect.bottom;
        this.m_endPositionLeft = rect.left;
        this.m_endPositionRight = rect.right;
        this.m_endPositionTop = rect.top;
    }

    public void getEndPosition(Rect rect) {
        int i = this.m_rotation;
        if (i == 90 || i == 270) {
            int i2 = this.m_rotatedEndPositionBottom;
            int i3 = this.m_width;
            rect.bottom = (i2 * i3) / ABSTRACT_DIMENSION;
            int i4 = this.m_rotatedEndPositionLeft;
            int i5 = this.m_height;
            rect.left = (i4 * i5) / ABSTRACT_DIMENSION;
            rect.right = (this.m_rotatedEndPositionRight * i5) / ABSTRACT_DIMENSION;
            rect.top = (this.m_rotatedEndPositionTop * i3) / ABSTRACT_DIMENSION;
            return;
        }
        int i6 = this.m_endPositionBottom;
        int i7 = this.m_height;
        rect.bottom = (i6 * i7) / ABSTRACT_DIMENSION;
        int i8 = this.m_endPositionLeft;
        int i9 = this.m_width;
        rect.left = (i8 * i9) / ABSTRACT_DIMENSION;
        rect.right = (this.m_endPositionRight * i9) / ABSTRACT_DIMENSION;
        rect.top = (this.m_endPositionTop * i7) / ABSTRACT_DIMENSION;
    }

    public void setEndPosition(Rect rect) {
        int i = this.m_rotation;
        if (i == 90 || i == 270) {
            int i2 = rect.bottom * ABSTRACT_DIMENSION;
            int i3 = this.m_width;
            this.m_rotatedEndPositionBottom = i2 / i3;
            int i4 = rect.left * ABSTRACT_DIMENSION;
            int i5 = this.m_height;
            this.m_rotatedEndPositionLeft = i4 / i5;
            this.m_rotatedEndPositionRight = (rect.right * ABSTRACT_DIMENSION) / i5;
            this.m_rotatedEndPositionTop = (rect.top * ABSTRACT_DIMENSION) / i3;
        } else {
            int i6 = rect.bottom * ABSTRACT_DIMENSION;
            int i7 = this.m_height;
            this.m_endPositionBottom = i6 / i7;
            int i8 = rect.left * ABSTRACT_DIMENSION;
            int i9 = this.m_width;
            this.m_endPositionLeft = i8 / i9;
            this.m_endPositionRight = (rect.right * ABSTRACT_DIMENSION) / i9;
            this.m_endPositionTop = (rect.top * ABSTRACT_DIMENSION) / i7;
        }
        if (this.m_endMatrix == null) {
            this.m_endMatrix = new float[9];
        }
        matrixForRect(rect).setValues(this.m_endMatrix);
    }

    public void setFacePosition(Rect rect) {
        int i = this.m_rotation;
        if (i == 90 || i == 270) {
            int i2 = rect.bottom * ABSTRACT_DIMENSION;
            int i3 = this.m_width;
            this.m_rotatedFacePositionBottom = i2 / i3;
            int i4 = rect.left * ABSTRACT_DIMENSION;
            int i5 = this.m_height;
            this.m_rotatedFacePositionLeft = i4 / i5;
            this.m_rotatedFacePositionRight = (rect.right * ABSTRACT_DIMENSION) / i5;
            this.m_rotatedFacePositionTop = (rect.top * ABSTRACT_DIMENSION) / i3;
            return;
        }
        int i6 = rect.bottom * ABSTRACT_DIMENSION;
        int i7 = this.m_height;
        this.m_facePositionBottom = i6 / i7;
        int i8 = rect.left * ABSTRACT_DIMENSION;
        int i9 = this.m_width;
        this.m_facePositionLeft = i8 / i9;
        this.m_facePositionRight = (rect.right * ABSTRACT_DIMENSION) / i9;
        this.m_facePositionTop = (rect.top * ABSTRACT_DIMENSION) / i7;
    }

    public void getFacePositionRaw(Rect rect) {
        int i = this.m_rotation;
        if (i == 90 || i == 270) {
            rect.left = this.m_rotatedFacePositionLeft;
            rect.top = this.m_rotatedFacePositionTop;
            rect.right = this.m_rotatedFacePositionRight;
            rect.bottom = this.m_rotatedFacePositionBottom;
            return;
        }
        rect.left = this.m_facePositionLeft;
        rect.top = this.m_facePositionTop;
        rect.right = this.m_facePositionRight;
        rect.bottom = this.m_facePositionBottom;
    }

    public void getStartPositionRaw(Rect rect) {
        int i = this.m_rotation;
        if (i == 90 || i == 270) {
            rect.bottom = this.m_rotatedStartPositionBottom;
            rect.left = this.m_rotatedStartPositionLeft;
            rect.right = this.m_rotatedStartPositionRight;
            rect.top = this.m_rotatedStartPositionTop;
            return;
        }
        rect.bottom = this.m_startPositionBottom;
        rect.left = this.m_startPositionLeft;
        rect.right = this.m_startPositionRight;
        rect.top = this.m_startPositionTop;
    }

    public void getEndPositionRaw(Rect rect) {
        int i = this.m_rotation;
        if (i == 90 || i == 270) {
            rect.bottom = this.m_rotatedEndPositionBottom;
            rect.left = this.m_rotatedEndPositionLeft;
            rect.right = this.m_rotatedEndPositionRight;
            rect.top = this.m_rotatedEndPositionTop;
            return;
        }
        rect.bottom = this.m_endPositionBottom;
        rect.left = this.m_endPositionLeft;
        rect.right = this.m_endPositionRight;
        rect.top = this.m_endPositionTop;
    }

    public void resetStartEndPosition() {
        this.m_startPositionLeft = 0;
        this.m_startPositionBottom = ABSTRACT_DIMENSION;
        this.m_startPositionRight = ABSTRACT_DIMENSION;
        this.m_startPositionTop = 0;
        this.m_endPositionLeft = 0;
        this.m_endPositionBottom = ABSTRACT_DIMENSION;
        this.m_endPositionRight = ABSTRACT_DIMENSION;
        this.m_endPositionTop = 0;
        this.m_rotatedStartPositionLeft = 0;
        this.m_rotatedStartPositionBottom = ABSTRACT_DIMENSION;
        this.m_rotatedStartPositionRight = ABSTRACT_DIMENSION;
        this.m_rotatedStartPositionTop = 0;
        this.m_rotatedEndPositionLeft = 0;
        this.m_rotatedEndPositionBottom = ABSTRACT_DIMENSION;
        this.m_rotatedEndPositionRight = ABSTRACT_DIMENSION;
        this.m_rotatedEndPositionTop = 0;
    }

    public void fitStartEndPosition(int i, int i2) {
        int width = getWidth();
        int height = getHeight();
        int i3 = this.m_rotation;
        if (i3 == 90 || i3 == 270) {
            width = getHeight();
            height = getWidth();
        }
        Rect rect = new Rect(0, 0, width, height);
        growToAspect(rect, i / i2);
        setStartPosition(rect);
        setEndPosition(rect);
    }

    private static Rect getFitRwaPosition(int i, int i2, float f, float f2, boolean z) {
        Rect rect = new Rect(0, 0, i, i2);
        float f3 = f / f2;
        if (rect.width() / rect.height() < f3) {
            int height = (int) (rect.height() * f3);
            int centerX = rect.centerX() - (height / 2);
            rect.left = centerX;
            rect.right = centerX + height;
        } else {
            int width = (int) (rect.width() / f3);
            int centerY = rect.centerY() - (width / 2);
            rect.top = centerY;
            rect.bottom = centerY + width;
        }
        int i3 = (rect.left * ABSTRACT_DIMENSION) / i;
        int i4 = (rect.right * ABSTRACT_DIMENSION) / i;
        int i5 = (rect.top * ABSTRACT_DIMENSION) / i2;
        rect.set(i3, i5, i4, (rect.bottom * ABSTRACT_DIMENSION) / i2);
        if (i5 != 0) {
            if (z) {
                enhancedCrop(rect, i, i2, f, f2);
            }
            return rect;
        }
        Log.d(TAG, "getFitRwaPosition() raw=" + rect.toString());
        return rect;
    }

    public static void enhancedCrop(Rect rect, int i, int i2, float f, float f2) {
        int i3 = rect.left;
        int i4 = rect.top;
        int i5 = rect.right;
        int i6 = rect.bottom;
        if (i4 == 0) {
            return;
        }
        float f3 = i6 - i4;
        float f4 = f3 / f2;
        float f5 = f4 / ((1.0f / i2) * 100000.0f);
        int abs = (int) (Math.abs(i4) / f4);
        int i7 = (int) (abs / 10.0f);
        int i8 = abs + i7;
        int i9 = abs - i7;
        float f6 = 1.0f;
        int i10 = i9;
        while (i9 < i8) {
            double d = i9 * f5;
            float min = (float) Math.min(Math.ceil(d) - d, d - Math.floor(d));
            if (f6 > min) {
                i10 = i9;
                f6 = min;
            }
            i9++;
        }
        int round = Math.round(f4 * i10) * (-1);
        rect.set(i3, round, i5, (int) (f3 + round));
        Log.d(TAG, "enhancedCrop() highQuality=" + i10 + ", q=" + rect.toString());
    }

    public void getFaceBounds(Rect rect, Context context) {
        com.nexstreaming.kminternal.kinemaster.utils.facedetect.a a = com.nexstreaming.kminternal.kinemaster.utils.facedetect.a.a(this.m_mediaPath);
        if (a == null) {
            Log.d(TAG, "Second face detection with use filter");
            a = new com.nexstreaming.kminternal.kinemaster.utils.facedetect.a(new File(this.m_mediaPath), true, context);
        }
        RectF rectF = new RectF();
        a.a(rectF);
        float width = getWidth();
        float height = getHeight();
        rect.set((int) (rectF.left * width), (int) (rectF.top * height), (int) (rectF.right * width), (int) (rectF.bottom * height));
    }

    private void calculateFaceBounds() {
        Bitmap loadImageForFaceRecognition;
        float f;
        int i;
        FaceDetector.Face[] faceArr;
        if (!this.m_faceBounds_set && (loadImageForFaceRecognition = loadImageForFaceRecognition()) != null) {
            FaceDetector.Face[] faceArr2 = new FaceDetector.Face[32];
            int findFaces = new FaceDetector(loadImageForFaceRecognition.getWidth(), loadImageForFaceRecognition.getHeight(), 32).findFaces(loadImageForFaceRecognition, faceArr2);
            RectF rectF = new RectF();
            RectF rectF2 = new RectF();
            PointF pointF = new PointF();
            float width = getWidth();
            float height = getHeight();
            float f2 = width / 30.0f;
            float f3 = height / 30.0f;
            int i2 = 0;
            while (i2 < findFaces) {
                Log.d(TAG, "face : " + i2 + " c=" + faceArr2[i2].confidence() + " e=" + faceArr2[i2].eyesDistance() + " faceMinWidth=" + f2 + " faceMinHeight=" + f3);
                if (faceArr2[i2].confidence() >= 0.4d) {
                    faceArr2[i2].getMidPoint(pointF);
                    float eyesDistance = faceArr2[i2].eyesDistance();
                    if (eyesDistance >= f2 && eyesDistance >= f3) {
                        float f4 = pointF.x;
                        float f5 = eyesDistance / 2.0f;
                        i = findFaces;
                        float f6 = pointF.y;
                        float f7 = eyesDistance / 3.0f;
                        faceArr = faceArr2;
                        rectF2.set((f4 - f5) / width, (f6 - f7) / height, (f4 + f5) / width, (f6 + (f7 * 2.0f)) / height);
                        rectF.union(rectF2);
                        i2++;
                        findFaces = i;
                        faceArr2 = faceArr;
                    }
                }
                i = findFaces;
                faceArr = faceArr2;
                i2++;
                findFaces = i;
                faceArr2 = faceArr;
            }
            loadImageForFaceRecognition.recycle();
            Log.d(TAG, "face bounds : " + rectF);
            if (!rectF.isEmpty()) {
                float width2 = 0.6f - rectF.width();
                float height2 = 0.6f - rectF.height();
                if (width2 > 0.0f) {
                    f = 2.0f;
                    float f8 = width2 / 2.0f;
                    rectF.left -= f8;
                    rectF.right += f8;
                } else {
                    f = 2.0f;
                }
                if (height2 > 0.0f) {
                    float f9 = height2 / f;
                    rectF.top -= f9;
                    rectF.bottom += f9;
                }
            }
            Log.d(TAG, "face bounds (final) : " + rectF);
            this.m_faceBounds_set = true;
            this.m_faceBounds_left = Math.max(0.0f, rectF.left);
            this.m_faceBounds_top = Math.max(0.0f, rectF.top);
            this.m_faceBounds_right = Math.min(rectF.right, 1.0f);
            this.m_faceBounds_bottom = Math.min(rectF.bottom, 1.0f);
        }
    }

    private Bitmap loadImageForFaceRecognition() {
        int i;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(this.m_mediaPath, options);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int i2 = options.outWidth;
        if (i2 < 1 || (i = options.outHeight) < 1) {
            return null;
        }
        int i3 = (i * 720) / i2;
        options.inSampleSize = 1;
        while (true) {
            int i4 = options.outHeight;
            int i5 = options.inSampleSize;
            if (i4 / i5 <= i3 * 1.5d && options.outWidth / i5 <= 720 * 1.5d) {
                break;
            }
            options.inSampleSize = i5 * 2;
        }
        Log.d(TAG, "loadImageForFaceRecognition:   bounds decoded : width,height=" + options.outWidth + "," + options.outHeight + "; target w,h=720," + i3 + "; sampleSize=" + options.inSampleSize);
        Bitmap decodeFile = BitmapFactory.decodeFile(this.m_mediaPath, options);
        if (decodeFile == null) {
            return null;
        }
        if (decodeFile.getConfig() == Bitmap.Config.RGB_565) {
            return decodeFile;
        }
        Bitmap createBitmap = Bitmap.createBitmap(decodeFile.getWidth(), decodeFile.getHeight(), Bitmap.Config.RGB_565);
        new Canvas(createBitmap).drawBitmap(decodeFile, 0.0f, 0.0f, (Paint) null);
        decodeFile.recycle();
        return createBitmap;
    }

    public nexSaveDataFormat.nexCropOf getSaveData() {
        nexSaveDataFormat.nexCropOf nexcropof = new nexSaveDataFormat.nexCropOf();
        nexcropof.m_startPositionLeft = this.m_startPositionLeft;
        nexcropof.m_startPositionBottom = this.m_startPositionBottom;
        nexcropof.m_startPositionRight = this.m_startPositionRight;
        nexcropof.m_startPositionTop = this.m_startPositionTop;
        nexcropof.m_endPositionLeft = this.m_endPositionLeft;
        nexcropof.m_endPositionBottom = this.m_endPositionBottom;
        nexcropof.m_endPositionRight = this.m_endPositionRight;
        nexcropof.m_endPositionTop = this.m_endPositionTop;
        nexcropof.m_rotatedStartPositionLeft = this.m_rotatedStartPositionLeft;
        nexcropof.m_rotatedStartPositionBottom = this.m_rotatedStartPositionBottom;
        nexcropof.m_rotatedStartPositionRight = this.m_rotatedStartPositionRight;
        nexcropof.m_rotatedStartPositionTop = this.m_rotatedStartPositionTop;
        nexcropof.m_rotatedEndPositionLeft = this.m_rotatedEndPositionLeft;
        nexcropof.m_rotatedEndPositionBottom = this.m_rotatedEndPositionBottom;
        nexcropof.m_rotatedEndPositionRight = this.m_rotatedEndPositionRight;
        nexcropof.m_rotatedEndPositionTop = this.m_rotatedEndPositionTop;
        nexcropof.m_facePositionLeft = this.m_facePositionLeft;
        nexcropof.m_facePositionTop = this.m_facePositionTop;
        nexcropof.m_facePositionRight = this.m_facePositionRight;
        nexcropof.m_facePositionBottom = this.m_facePositionBottom;
        nexcropof.m_rotatedFacePositionLeft = this.m_rotatedFacePositionLeft;
        nexcropof.m_rotatedFacePositionTop = this.m_rotatedFacePositionTop;
        nexcropof.m_rotatedFacePositionRight = this.m_rotatedFacePositionRight;
        nexcropof.m_rotatedFacePositionBottom = this.m_rotatedFacePositionBottom;
        nexcropof.m_rotation = this.m_rotation;
        nexcropof.m_width = this.m_width;
        nexcropof.m_height = this.m_height;
        nexcropof.m_faceBounds_left = this.m_faceBounds_left;
        nexcropof.m_faceBounds_top = this.m_faceBounds_top;
        nexcropof.m_faceBounds_right = this.m_faceBounds_right;
        nexcropof.m_faceBounds_bottom = this.m_faceBounds_bottom;
        nexcropof.m_faceBounds_set = this.m_faceBounds_set;
        return nexcropof;
    }

    public nexCrop(String str, nexSaveDataFormat.nexCropOf nexcropof) {
        this.m_startPositionLeft = 0;
        this.m_startPositionBottom = 0;
        this.m_startPositionRight = 0;
        this.m_startPositionTop = 0;
        this.m_endPositionLeft = 0;
        this.m_endPositionBottom = 0;
        this.m_endPositionRight = 0;
        this.m_endPositionTop = 0;
        this.m_rotatedStartPositionLeft = 0;
        this.m_rotatedStartPositionBottom = 0;
        this.m_rotatedStartPositionRight = 0;
        this.m_rotatedStartPositionTop = 0;
        this.m_rotatedEndPositionLeft = 0;
        this.m_rotatedEndPositionBottom = 0;
        this.m_rotatedEndPositionRight = 0;
        this.m_rotatedEndPositionTop = 0;
        this.m_facePositionLeft = 0;
        this.m_facePositionTop = 0;
        this.m_facePositionRight = 0;
        this.m_facePositionBottom = 0;
        this.m_rotatedFacePositionLeft = 0;
        this.m_rotatedFacePositionTop = 0;
        this.m_rotatedFacePositionRight = 0;
        this.m_rotatedFacePositionBottom = 0;
        this.m_startLock = false;
        this.m_endLock = false;
        this.m_rotation = 0;
        this.m_width = 0;
        this.m_height = 0;
        this.facedetect_speed = 101;
        this.clip_druation = 0;
        this.m_startPositionLeft = nexcropof.m_startPositionLeft;
        this.m_startPositionBottom = nexcropof.m_startPositionBottom;
        this.m_startPositionRight = nexcropof.m_startPositionRight;
        this.m_startPositionTop = nexcropof.m_startPositionTop;
        this.m_endPositionLeft = nexcropof.m_endPositionLeft;
        this.m_endPositionBottom = nexcropof.m_endPositionBottom;
        this.m_endPositionRight = nexcropof.m_endPositionRight;
        this.m_endPositionTop = nexcropof.m_endPositionTop;
        this.m_rotatedStartPositionLeft = nexcropof.m_rotatedStartPositionLeft;
        this.m_rotatedStartPositionBottom = nexcropof.m_rotatedStartPositionBottom;
        this.m_rotatedStartPositionRight = nexcropof.m_rotatedStartPositionRight;
        this.m_rotatedStartPositionTop = nexcropof.m_rotatedStartPositionTop;
        this.m_rotatedEndPositionLeft = nexcropof.m_rotatedEndPositionLeft;
        this.m_rotatedEndPositionBottom = nexcropof.m_rotatedEndPositionBottom;
        this.m_rotatedEndPositionRight = nexcropof.m_rotatedEndPositionRight;
        this.m_rotatedEndPositionTop = nexcropof.m_rotatedEndPositionTop;
        this.m_facePositionLeft = nexcropof.m_facePositionLeft;
        this.m_facePositionTop = nexcropof.m_facePositionTop;
        this.m_facePositionRight = nexcropof.m_facePositionRight;
        this.m_facePositionBottom = nexcropof.m_facePositionBottom;
        this.m_rotatedFacePositionLeft = nexcropof.m_rotatedFacePositionLeft;
        this.m_rotatedFacePositionTop = nexcropof.m_rotatedFacePositionTop;
        this.m_rotatedFacePositionRight = nexcropof.m_rotatedFacePositionRight;
        this.m_rotatedFacePositionBottom = nexcropof.m_rotatedFacePositionBottom;
        this.m_rotation = nexcropof.m_rotation;
        this.m_width = nexcropof.m_width;
        this.m_height = nexcropof.m_height;
        this.m_mediaPath = str;
        this.m_faceBounds_left = nexcropof.m_faceBounds_left;
        this.m_faceBounds_top = nexcropof.m_faceBounds_top;
        this.m_faceBounds_right = nexcropof.m_faceBounds_right;
        this.m_faceBounds_bottom = nexcropof.m_faceBounds_bottom;
        this.m_faceBounds_set = nexcropof.m_faceBounds_set;
    }

    public void setFaceDetectSpeed(int i) {
        this.facedetect_speed = i;
    }

    public int getFaceDetectSpeed() {
        return this.facedetect_speed;
    }

    public void setClipDuration(int i) {
        this.clip_druation = i;
    }

    public int getClipDuration() {
        return this.clip_druation;
    }

    public void applyCropSpeed(Rect rect, Rect rect2, int i, int i2, int i3, int i4) {
        if (!getEndPosionLock() && !rect2.intersect(0, 0, i, i2)) {
            rect2.set(0, 0, (i * 2) / 3, (i2 * 2) / 3);
            rect2.offset((int) ((i * Math.random()) / 3.0d), (int) ((i2 * Math.random()) / 3.0d));
        }
        int i5 = (int) (i3 * ((i4 * 0.5d) / 1000.0d));
        if (i3 < 101) {
            int i6 = (i * i5) / 1000;
            int i7 = (i5 * i2) / 1000;
            rect.set(rect2.left - i6, rect2.top - i7, rect2.right + i6, rect2.bottom + (i7 * 3));
        } else {
            int i8 = i / 4;
            int i9 = i2 / 4;
            rect.set(rect2.left - i8, rect2.top - i9, rect2.right + i8, rect2.bottom + i9);
        }
        if (!rect.intersect(0, 0, i, i2)) {
            rect.set(0, 0, (i * 3) / 4, (i2 * 3) / 4);
            rect.offset((int) ((i * Math.random()) / 4.0d), (int) ((i2 * Math.random()) / 4.0d));
        }
    }

    public void setStartPositionLock(boolean z) {
        this.m_startLock = z;
    }

    public boolean getStartPosionLock() {
        return this.m_startLock;
    }

    public void setEndPositionLock(boolean z) {
        this.m_endLock = z;
    }

    public boolean getEndPosionLock() {
        return this.m_endLock;
    }
}
