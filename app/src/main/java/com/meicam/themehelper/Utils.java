package com.meicam.themehelper;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import com.meicam.sdk.NvsAVFileInfo;
import com.meicam.sdk.NvsSize;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoFx;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import org.keyczar.Keyczar;

/* loaded from: classes.dex */
public class Utils {
    private static int[] motionIdxList = {0, 0, 0, 0};

    public static String readFile(String str, AssetManager assetManager) {
        InputStream open;
        try {
            if (assetManager == null) {
                open = new FileInputStream(new File(str));
            } else {
                open = assetManager.open(str);
            }
            byte[] bArr = new byte[open.available()];
            open.read(bArr);
            open.close();
            return new String(bArr, Keyczar.DEFAULT_ENCODING);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static float getImgRatio(NvsAVFileInfo nvsAVFileInfo) {
        NvsSize videoStreamDimension = nvsAVFileInfo.getVideoStreamDimension(0);
        int videoStreamRotation = nvsAVFileInfo.getVideoStreamRotation(0);
        NvsSize nvsSize = new NvsSize(videoStreamDimension.width, videoStreamDimension.height);
        if (videoStreamRotation == 1 || videoStreamRotation == 3) {
            nvsSize.width = videoStreamDimension.height;
            nvsSize.height = videoStreamDimension.width;
        }
        return nvsSize.width / nvsSize.height;
    }

    public static void clipImage(NvsVideoClip nvsVideoClip, float f) {
        if (f < 0.75f) {
            nvsVideoClip.setImageMotionMode(2);
            nvsVideoClip.setAttachment("fullscreenMode", "true");
        } else if (f > 0.75f) {
            nvsVideoClip.setImageMotionMode(0);
            if (f > 1.0f) {
                NvsVideoFx appendBuiltinFx = nvsVideoClip.appendBuiltinFx("Transform 2D");
                double d = f;
                appendBuiltinFx.setFloatVal("Scale X", d);
                appendBuiltinFx.setFloatVal("Scale Y", d);
            } else if (f < 1.0f) {
                float f2 = NvsThemeHelper.m_timelineRatio;
                nvsVideoClip.setImageMaskROI(new RectF(-1.0f, f2, 1.0f, -f2));
                nvsVideoClip.setAttachment("fullscreenMode", "false");
            }
            nvsVideoClip.setAttachment("fullscreenMode", "false");
        } else {
            nvsVideoClip.setImageMotionMode(0);
            nvsVideoClip.setAttachment("fullscreenMode", "false");
        }
    }

    public static void setImageMotion(NvsVideoClip nvsVideoClip, float f, boolean z, RectF rectF) {
        boolean z2 = false;
        if (z) {
            if (getMotionIdx(2, 4) == 0) {
                nvsVideoClip.setImageMotionMode(0);
            } else {
                nvsVideoClip.setImageMotionMode(1);
            }
        } else {
            RectF rectF2 = new RectF();
            RectF rectF3 = new RectF();
            setImageROI(rectF2, rectF3, rectF, f);
            nvsVideoClip.setImageMotionROI(rectF2, rectF3);
        }
        if (f <= 0.75f) {
            z2 = true;
        }
        setClipMask(nvsVideoClip, z2);
    }

    public static void setImageROI(RectF rectF, RectF rectF2, RectF rectF3, float f) {
        boolean z = f < 1.0f;
        if (rectF3 == null) {
            if (z) {
                setPortraitImgMotion(rectF, rectF2, f);
                return;
            } else {
                setLandscapeImgMotion(rectF, rectF2, f);
                return;
            }
        }
        float[] fArr = {rectF3.left, rectF3.top, rectF3.width(), rectF3.height()};
        if (z) {
            setPortraitFaceImgMotion(rectF, rectF2, f, fArr);
        } else {
            setLandscapeFaceImgMotion(rectF, rectF2, f, fArr);
        }
    }

    private static void setClipMask(NvsVideoClip nvsVideoClip, boolean z) {
        if (z) {
            nvsVideoClip.setImageMaskROI(new RectF(-1.0f, 1.0f, 1.0f, -1.0f));
            nvsVideoClip.setAttachment("fullscreenMode", "true");
            return;
        }
        float f = NvsThemeHelper.m_timelineRatio;
        nvsVideoClip.setImageMaskROI(new RectF(-1.0f, f, 1.0f, -f));
        nvsVideoClip.setAttachment("fullscreenMode", "false");
    }

    private static void setLandscapeImgMotion(RectF rectF, RectF rectF2, float f) {
        int motionIdx = getMotionIdx(4, 3);
        if (motionIdx == 2) {
            setZoomIn(rectF, rectF2, f);
        } else if (motionIdx == 3) {
            setZoomIn(rectF2, rectF, f);
        } else if (motionIdx == 0) {
            calcLandscapeLeftRec(rectF, f);
            calcLandscapeRightRec(rectF2, f);
        } else {
            calcLandscapeLeftRec(rectF2, f);
            calcLandscapeRightRec(rectF, f);
        }
    }

    private static void setZoomIn(RectF rectF, RectF rectF2, float f) {
        rectF.top = 1.4f;
        rectF.bottom = -1.4f;
        float f2 = NvsThemeHelper.m_timelineRatio;
        float f3 = (((-(1.4f - (-1.4f))) * f2) / f) / 2.0f;
        rectF.left = f3;
        rectF.right = -f3;
        rectF2.top = 1.2f;
        rectF2.bottom = -1.2f;
        float f4 = (((-(1.2f - (-1.2f))) / f) * f2) / 2.0f;
        rectF2.left = f4;
        rectF2.right = -f4;
    }

    private static RectF calcLandscapeLeftRec(RectF rectF, float f) {
        rectF.left = -1.0f;
        rectF.top = 1.4f;
        rectF.bottom = -1.4f;
        rectF.right = (((1.4f - (-1.4f)) / f) * NvsThemeHelper.m_timelineRatio) - 1.0f;
        return rectF;
    }

    private static RectF calcLandscapeRightRec(RectF rectF, float f) {
        rectF.left = -0.8f;
        rectF.top = 1.4f;
        rectF.bottom = -1.4f;
        rectF.right = (((1.4f - (-1.4f)) / f) * NvsThemeHelper.m_timelineRatio) - 0.8f;
        return rectF;
    }

    private static void setPortraitImgMotion(RectF rectF, RectF rectF2, float f) {
        int motionIdx = getMotionIdx(f <= 0.4f ? 4 : 6, 2);
        if (motionIdx == 4) {
            setPortraitZoomin(rectF, rectF2, f);
        } else if (motionIdx == 5) {
            setPortraitZoomin(rectF2, rectF, f);
        } else if (motionIdx == 0) {
            normalLtToRb(rectF, rectF2, f);
        } else if (motionIdx == 1) {
            normalLtToRb(rectF2, rectF, f);
        } else if (motionIdx == 2) {
            normalLbToRt(rectF, rectF2, f);
        } else if (motionIdx != 3) {
        } else {
            normalLbToRt(rectF2, rectF, f);
        }
    }

    private static void setPortraitZoomin(RectF rectF, RectF rectF2, float f) {
        float f2 = NvsThemeHelper.m_timelineRatio;
        if (f >= f2) {
            rectF.top = 1.0f;
            rectF.bottom = -1.0f;
            float f3 = (((-(1.0f - (-1.0f))) / f) * f2) / 2.0f;
            rectF.left = f3;
            rectF.right = -f3;
            rectF2.top = 0.87f;
            rectF2.bottom = -0.87f;
            float f4 = (((-(0.87f - (-0.87f))) / f) * f2) / 2.0f;
            rectF2.left = f4;
            rectF2.right = -f4;
            return;
        }
        rectF.left = -1.0f;
        rectF.right = 1.0f;
        float f5 = (((1.0f - (-1.0f)) * f) / f2) / 2.0f;
        rectF.top = f5;
        rectF.bottom = -f5;
        rectF2.left = -0.87f;
        rectF2.right = 0.87f;
        float f6 = (((0.87f - (-0.87f)) * f) / f2) / 2.0f;
        rectF2.top = f6;
        rectF2.bottom = -f6;
    }

    private static void setPortraitFaceImgMotion(RectF rectF, RectF rectF2, float f, float[] fArr) {
        int motionIdx = getMotionIdx(2, 0);
        if (motionIdx == 0) {
            upToBottom(rectF, rectF2, fArr, f);
        } else if (motionIdx != 1) {
        } else {
            upToBottom(rectF2, rectF, fArr, f);
        }
    }

    private static void setLandscapeFaceImgMotion(RectF rectF, RectF rectF2, float f, float[] fArr) {
        int motionIdx = getMotionIdx(2, 1);
        if (motionIdx == 0) {
            leftToRight(rectF, rectF2, fArr, f);
        } else if (motionIdx != 1) {
        } else {
            leftToRight(rectF2, rectF, fArr, f);
        }
    }

    private static void leftToRight(RectF rectF, RectF rectF2, float[] fArr, float f) {
        float max = Math.max(-1.0f, fArr[0] - 0.3f);
        rectF.left = max;
        rectF.top = 1.4f;
        rectF.bottom = -1.4f;
        float f2 = (((1.4f - (-1.4f)) / f) * NvsThemeHelper.m_timelineRatio) + max;
        rectF.right = f2;
        rectF2.top = 1.4f;
        rectF2.bottom = -1.4f;
        float min = Math.min(1.0f, f2 + 0.15f);
        rectF2.right = min;
        rectF2.left = (rectF.left + min) - rectF.right;
    }

    private static void leftToRightByFx(RectF rectF, RectF rectF2, float[] fArr, float f) {
        if (f > 1.5d) {
            rectF.top = 1.4f;
            rectF.bottom = -1.4f;
            float max = Math.max(-1.0f, ((-(((1.4f - (-1.4f)) * NvsThemeHelper.m_timelineRatio) / f)) / 2.0f) - 0.075f);
            rectF.left = max;
            float f2 = rectF.top;
            float f3 = rectF.bottom;
            float f4 = (((f2 - f3) / f) * NvsThemeHelper.m_timelineRatio) + max;
            rectF.right = f4;
            rectF2.top = f2;
            rectF2.bottom = f3;
            float min = Math.min(1.0f, f4 + 0.15f);
            rectF2.right = min;
            rectF2.left = (rectF.left + min) - rectF.right;
            return;
        }
        float f5 = ((fArr[1] * 2.0f) - fArr[3]) / 2.0f;
        rectF.left = -0.9f;
        rectF.right = 0.7f;
        float f6 = NvsThemeHelper.m_timelineRatio;
        float f7 = ((((0.7f - (-0.9f)) * f) / f6) / 2.0f) + f5;
        rectF.top = f7;
        float f8 = ((((-(0.7f - (-0.9f))) * f) / f6) / 2.0f) + f5;
        rectF.bottom = f8;
        rectF2.left = -0.7f;
        rectF2.right = 0.9f;
        rectF2.top = f7;
        rectF2.bottom = f8;
    }

    private static void upToBottom(RectF rectF, RectF rectF2, float[] fArr, float f) {
        rectF.left = Math.max(-1.0f, fArr[0] - 0.35f);
        rectF.right = Math.min(1.0f, fArr[0] + fArr[2] + 0.35f);
        float min = Math.min(1.0f, fArr[1] + 0.5f);
        rectF.top = min;
        float f2 = rectF.right;
        float f3 = rectF.left;
        float f4 = min - (((f2 - f3) / NvsThemeHelper.m_timelineRatio) * f);
        rectF.bottom = f4;
        if (f4 < -1.0f) {
            rectF.bottom = -1.0f;
            float f5 = min + ((-1.0f) - f4);
            rectF.top = f5;
            if (f5 > 1.0f) {
                setPortraitZoomin(rectF, rectF2, f);
                return;
            }
            float f6 = 1.0f - f5;
            rectF2.top = f5 + f6;
            rectF2.bottom = (-1.0f) + f6;
        } else {
            float f7 = f4 - 0.13f;
            rectF2.bottom = f7;
            float f8 = min - 0.13f;
            rectF2.top = f8;
            if (f7 < -1.0f) {
                rectF2.bottom = -1.0f;
                rectF2.top = f8 + ((-1.0f) - f7);
            }
        }
        rectF2.left = f3;
        rectF2.right = f2;
    }

    private static void upToBottomByFx(RectF rectF, RectF rectF2, float[] fArr, float f) {
        float f2 = ((fArr[0] * 2.0f) + fArr[2]) / 2.0f;
        rectF.top = 1.0f;
        rectF.bottom = -0.8f;
        float f3 = NvsThemeHelper.m_timelineRatio;
        float f4 = ((((-(1.0f - (-0.8f))) * f3) / f) / 2.0f) + f2;
        rectF.left = f4;
        float f5 = ((((1.0f - (-0.8f)) * f3) / f) / 2.0f) + f2;
        rectF.right = f5;
        if (f4 < -1.0f) {
            rectF.right = f5 + ((-1.0f) - f4);
            rectF.left = -1.0f;
        }
        float f6 = rectF.right;
        if (f6 > 1.0f) {
            rectF.left -= f6 - 1.0f;
            rectF.right = 1.0f;
        }
        rectF2.left = rectF.left;
        rectF2.right = rectF.right;
        rectF2.top = 0.8f;
        rectF2.bottom = -1.0f;
    }

    private static void LtToRb(RectF rectF, RectF rectF2, float[] fArr, float f) {
        rectF.left = Math.max(-1.0f, fArr[0] - 0.25f);
        float min = Math.min(1.0f, fArr[0] + fArr[2] + 0.35f);
        rectF.right = min;
        float f2 = rectF.left;
        float f3 = ((((min - f2) / NvsThemeHelper.m_timelineRatio) * f) / 2.0f) - 0.05f;
        rectF.top = f3;
        rectF.bottom = (-f3) - 0.1f;
        rectF2.left = Math.max(-1.0f, f2 - 0.1f);
        float min2 = Math.min(1.0f, rectF.top + 0.1f);
        rectF2.top = min2;
        float f4 = rectF.right - 0.1f;
        rectF2.right = f4;
        rectF2.bottom = min2 - (((f4 - rectF2.left) / NvsThemeHelper.m_timelineRatio) * f);
    }

    private static void RtToLb(RectF rectF, RectF rectF2, float[] fArr, float f) {
        rectF.right = Math.min(1.0f, fArr[0] + fArr[2] + 0.25f);
        float max = Math.max(-1.0f, fArr[0] - 0.35f);
        rectF.left = max;
        float f2 = rectF.right;
        float f3 = ((((f2 - max) / NvsThemeHelper.m_timelineRatio) * f) / 2.0f) - 0.05f;
        rectF.top = f3;
        rectF.bottom = (-f3) - 0.1f;
        rectF2.right = Math.min(1.0f, f2 + 0.1f);
        float min = Math.min(1.0f, rectF.top + 0.1f);
        rectF2.top = min;
        float f4 = rectF.left + 0.1f;
        rectF2.left = f4;
        rectF2.bottom = min - (((rectF2.right - f4) / NvsThemeHelper.m_timelineRatio) * f);
    }

    private static void normalLtToRb(RectF rectF, RectF rectF2, float f) {
        rectF.left = -1.0f;
        rectF.top = 1.0f;
        rectF.right = 0.8f;
        float width = 1.0f - ((rectF.width() * f) / NvsThemeHelper.m_timelineRatio);
        rectF.bottom = width;
        if (width < -1.0f) {
            rectF.bottom = -1.0f;
            rectF.right = changeROTBottom(rectF, f);
        }
        rectF2.left = -0.85f;
        rectF2.top = 0.9f;
        rectF2.right = 1.0f;
        float width2 = 0.9f - ((rectF2.width() * f) / NvsThemeHelper.m_timelineRatio);
        rectF2.bottom = width2;
        if (width2 < -1.0f) {
            rectF2.bottom = -1.0f;
            rectF2.right = changeROTBottom(rectF2, f);
        }
    }

    private static void normalLbToRt(RectF rectF, RectF rectF2, float f) {
        rectF.left = -0.9f;
        rectF.top = 0.9f;
        rectF.right = 0.85f;
        float width = 0.9f - ((rectF.width() * f) / NvsThemeHelper.m_timelineRatio);
        rectF.bottom = width;
        if (width < -1.0f) {
            rectF.bottom = -1.0f;
            rectF.right = changeROTBottom(rectF, f);
        }
        rectF2.left = -0.75f;
        rectF2.top = 1.0f;
        rectF2.right = 1.0f;
        float width2 = 1.0f - ((rectF2.width() * f) / NvsThemeHelper.m_timelineRatio);
        rectF2.bottom = width2;
        if (width2 < -1.0f) {
            rectF2.bottom = -1.0f;
            rectF2.right = changeROTBottom(rectF2, f);
        }
    }

    public static void normalLeftToRightByFx(RectF rectF, RectF rectF2, float f) {
        if (f > 1.5d) {
            rectF.top = 1.4f;
            rectF.bottom = -1.4f;
            float max = Math.max(-1.0f, ((-(((1.4f - (-1.4f)) * NvsThemeHelper.m_timelineRatio) / f)) / 2.0f) - 0.075f);
            rectF.left = max;
            float f2 = rectF.top;
            float f3 = rectF.bottom;
            float f4 = (((f2 - f3) / f) * NvsThemeHelper.m_timelineRatio) + max;
            rectF.right = f4;
            rectF2.top = f2;
            rectF2.bottom = f3;
            float min = Math.min(1.0f, f4 + 0.15f);
            rectF2.right = min;
            rectF2.left = (rectF.left + min) - rectF.right;
            return;
        }
        rectF.left = -0.8f;
        rectF.right = 0.7f;
        float f5 = (((0.7f - (-0.8f)) * f) / NvsThemeHelper.m_timelineRatio) / 2.0f;
        rectF.top = f5;
        float f6 = -f5;
        rectF.bottom = f6;
        rectF2.left = -0.7f;
        rectF2.right = 0.8f;
        rectF2.top = f5;
        rectF2.bottom = f6;
    }

    public static void normalUpToBottomByFx(RectF rectF, RectF rectF2, float f) {
        rectF.top = 1.0f;
        rectF.bottom = -0.8f;
        float f2 = (((-(1.0f - (-0.8f))) * NvsThemeHelper.m_timelineRatio) / f) / 2.0f;
        rectF.left = f2;
        float f3 = -f2;
        rectF.right = f3;
        rectF2.left = f2;
        rectF2.right = f3;
        rectF2.top = 0.8f;
        rectF2.bottom = -1.0f;
    }

    private static float changeROTBottom(RectF rectF, float f) {
        return rectF.left + (((rectF.top - rectF.bottom) / f) * NvsThemeHelper.m_timelineRatio);
    }

    public static String changeHoriROI(float f, RectF rectF, String str, NvsVideoClip nvsVideoClip) {
        RectF rectF2 = new RectF();
        RectF rectF3 = new RectF();
        if (rectF != null) {
            upToBottomByFx(rectF2, rectF3, new float[]{rectF.left, rectF.top, rectF.width(), rectF.height()}, f);
        } else {
            normalUpToBottomByFx(rectF2, rectF3, f);
        }
        if (nvsVideoClip != null) {
            nvsVideoClip.setImageMotionROI(rectF2, rectF3);
            return str;
        }
        return str.replace("xiaomiEndROI", "" + rectF2.left + "," + rectF2.right + "," + rectF2.bottom + "," + rectF2.top).replace("xiaomiStartROI", "" + rectF3.left + "," + rectF3.right + "," + rectF3.bottom + "," + rectF3.top);
    }

    public static String changeHoriROIV3(float f, RectF rectF, String str, boolean z) {
        RectF rectF2 = new RectF();
        RectF rectF3 = new RectF();
        if (rectF != null) {
            upToBottomByFx(rectF2, rectF3, new float[]{rectF.left, rectF.top, rectF.width(), rectF.height()}, f);
        } else {
            normalUpToBottomByFx(rectF2, rectF3, f);
        }
        String str2 = "" + rectF2.left + "," + rectF2.right + "," + rectF2.bottom + "," + rectF2.top;
        String str3 = "" + rectF3.left + "," + rectF3.right + "," + rectF3.bottom + "," + rectF3.top;
        if (z) {
            return str.replace("xiaomiStartROI", str2).replace("xiaomiEndROI", str3);
        }
        return str.replace("jieshu", str2).replace("kaishi", str3);
    }

    public static String changeVertROI(float f, RectF rectF, String str, NvsVideoClip nvsVideoClip) {
        RectF rectF2 = new RectF();
        RectF rectF3 = new RectF();
        if (rectF != null) {
            leftToRightByFx(rectF2, rectF3, new float[]{rectF.left, rectF.top, rectF.width(), rectF.height()}, f);
        } else {
            normalLeftToRightByFx(rectF2, rectF3, f);
        }
        if (nvsVideoClip != null) {
            nvsVideoClip.setImageMotionROI(rectF2, rectF3);
            return str;
        }
        return str.replace("xiaomiEndROI", "" + rectF2.left + "," + rectF2.right + "," + rectF2.bottom + "," + rectF2.top).replace("xiaomiStartROI", "" + rectF3.left + "," + rectF3.right + "," + rectF3.bottom + "," + rectF3.top);
    }

    public static String changeVertROIV3(float f, RectF rectF, String str, boolean z) {
        RectF rectF2 = new RectF();
        RectF rectF3 = new RectF();
        if (rectF != null) {
            leftToRightByFx(rectF2, rectF3, new float[]{rectF.left, rectF.top, rectF.width(), rectF.height()}, f);
        } else {
            normalLeftToRightByFx(rectF2, rectF3, f);
        }
        String str2 = "" + rectF2.left + "," + rectF2.right + "," + rectF2.bottom + "," + rectF2.top;
        String str3 = "" + rectF3.left + "," + rectF3.right + "," + rectF3.bottom + "," + rectF3.top;
        if (z) {
            return str.replace("xiaomiStartROI", str2).replace("xiaomiEndROI", str3);
        }
        return str.replace("jieshu", str2).replace("kaishi", str3);
    }

    public static String changeROI(float f, RectF rectF, String str) {
        RectF rectF2;
        RectF rectF3;
        setImageROI(new RectF(), new RectF(), rectF, f);
        return str.replace("xiaomiStartROI", "" + rectF2.left + "," + rectF2.right + "," + rectF2.bottom + "," + rectF2.top).replace("xiaomiEndROI", "" + rectF3.left + "," + rectF3.right + "," + rectF3.bottom + "," + rectF3.top);
    }

    public static String changeROIV3(float f, RectF rectF, String str, boolean z) {
        RectF rectF2;
        RectF rectF3;
        setImageROI(new RectF(), new RectF(), rectF, f);
        String str2 = "" + rectF2.left + "," + rectF2.right + "," + rectF2.bottom + "," + rectF2.top;
        String str3 = "" + rectF3.left + "," + rectF3.right + "," + rectF3.bottom + "," + rectF3.top;
        if (z) {
            return str.replace("xiaomiStartROI", str2).replace("xiaomiEndROI", str3);
        }
        return str.replace("kaishi", str2).replace("jieshu", str3);
    }

    private static int getMotionIdx(int i, int i2) {
        int nextInt = NvsThemeHelper.rand.nextInt(i);
        int[] iArr = motionIdxList;
        if (i2 >= iArr.length) {
            return nextInt;
        }
        if (nextInt == iArr[i2] && (nextInt = nextInt + 1) >= i) {
            nextInt = 0;
        }
        iArr[i2] = nextInt;
        return nextInt;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int dip2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }
}
