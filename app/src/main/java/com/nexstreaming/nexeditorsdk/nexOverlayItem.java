package com.nexstreaming.nexeditorsdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import com.nexstreaming.app.common.nexasset.overlay.AwakeAsset;
import com.nexstreaming.app.common.nexasset.overlay.OverlayAsset;
import com.nexstreaming.app.common.nexasset.overlay.OverlayMotion;
import com.nexstreaming.kminternal.kinemaster.config.EditorGlobal;
import com.nexstreaming.kminternal.nexvideoeditor.LayerRenderer;
import com.nexstreaming.nexeditorsdk.exception.ClipIsNotVideoException;
import com.nexstreaming.nexeditorsdk.exception.InvalidRangeException;
import com.nexstreaming.nexeditorsdk.exception.nexSDKException;
import com.nexstreaming.nexeditorsdk.nexAnimate;
import com.xiaomi.miai.api.StatusCode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes3.dex */
public final class nexOverlayItem implements Cloneable {
    public static final int AnchorPoint_LeftBottom = 6;
    public static final int AnchorPoint_LeftMiddle = 3;
    public static final int AnchorPoint_LeftTop = 0;
    public static final int AnchorPoint_MiddleBottom = 7;
    public static final int AnchorPoint_MiddleMiddle = 4;
    public static final int AnchorPoint_MiddleTop = 1;
    public static final int AnchorPoint_RightBottom = 8;
    public static final int AnchorPoint_RightMiddle = 5;
    public static final int AnchorPoint_RightTop = 2;
    private static final String TAG = "nexOverlayItem";
    private static int handleTouchZonePxSize = 36;
    private static int iconSize = 24;
    public static final int kOutLine_Pos_LeftBottom = 3;
    public static final int kOutLine_Pos_LeftTop = 1;
    public static final int kOutLine_Pos_RightBottom = 4;
    public static final int kOutLine_Pos_RightTop = 2;
    private static int marchingAnts_dashSize = 10;
    private static int marchingAnts_width = 4;
    private static Bitmap[] outLineIcon = null;
    private static int sLastId = 1;
    private static Bitmap solidBlackBitmap;
    private static boolean solidOutlen;
    private static Bitmap solidWhiteBitmap;
    private int anchorPoint;
    private int anchorPointX;
    private int anchorPointY;
    private int animateResourceId;
    private transient AwakeAsset awakeAsset;
    public boolean bApplayLayerExpression;
    private OverlayMotion cacheMotion;
    private int lastAnchorPoint;
    private int lastLayerHeight;
    private int lastLayerWidth;
    private Set<nexAnimate> mActiveAnimateList;
    public float mAlpha;
    public List<nexAnimate> mAniList;
    private float mAnimateLastAlpha;
    private float mAnimateLastRotateDegreeX;
    private float mAnimateLastRotateDegreeY;
    private float mAnimateLastRotateDegreeZ;
    private float mAnimateLastScaledX;
    private float mAnimateLastScaledY;
    private float mAnimateLastScaledZ;
    private float mAnimateTranslateXpos;
    private float mAnimateTranslateYpos;
    private float mAnimateTranslateZpos;
    private boolean mAudioOnOff;
    private int mBrightness;
    private ChromaKey mChromaKey;
    private nexColorEffect mColorEffect;
    private int mContrast;
    public int mEndTime;
    private int mFlipMode;
    private int mId;
    private nexOverlayKineMasterExpression mLayerExpression;
    private int mLayerExpressionDuration;
    private Mask mMask;
    private Rect mMaskRect;
    private nexOverlayFilter mOverLayFilter;
    private nexOverlayImage mOverLayImage;
    private boolean mOverlayTitle;
    public float mRotateDegreeX;
    public float mRotateDegreeY;
    public float mRotateDegreeZ;
    private int mSaturation;
    public float mScaledX;
    public float mScaledY;
    public float mScaledZ;
    private int mSpeedControl;
    public int mStartTime;
    private int mTime;
    private int mTrimEndDuration;
    private int mTrimStartDuration;
    private boolean mUpdated;
    public int mVideoEngineId;
    private int mVolume;
    public float mX;
    public float mY;
    public float mZ;
    private int mZOrder;
    private boolean relationCoordinates;
    private Matrix scratchMatrix;
    private float[] scratchPoint;
    private boolean showItem;
    private boolean showOutLien;

    @Deprecated
    public void clearCache() {
    }

    private boolean updateCoordinates(boolean z) {
        boolean z2;
        int i = this.lastAnchorPoint;
        int i2 = this.anchorPoint;
        if (i != i2) {
            if (!z) {
                this.lastAnchorPoint = i2;
            }
            z2 = true;
        } else {
            z2 = false;
        }
        if (this.lastLayerWidth != nexApplicationConfig.getAspectProfile().getWidth()) {
            if (!z) {
                this.lastLayerWidth = nexApplicationConfig.getAspectProfile().getWidth();
            }
            z2 = true;
        }
        if (this.lastLayerHeight != nexApplicationConfig.getAspectProfile().getHeight()) {
            if (z) {
                return true;
            }
            this.lastLayerHeight = nexApplicationConfig.getAspectProfile().getHeight();
            return true;
        }
        return z2;
    }

    private void getAnchorPosition(boolean z) {
        if (!z) {
            z = updateCoordinates(false);
        }
        if (z) {
            switch (this.anchorPoint) {
                case 0:
                    this.anchorPointX = 0;
                    this.anchorPointY = 0;
                    return;
                case 1:
                    this.anchorPointX = this.lastLayerWidth / 2;
                    this.anchorPointY = 0;
                    return;
                case 2:
                    this.anchorPointX = this.lastLayerWidth;
                    this.anchorPointY = 0;
                    return;
                case 3:
                    this.anchorPointX = 0;
                    this.anchorPointY = this.lastLayerHeight / 2;
                    return;
                case 4:
                    this.anchorPointX = this.lastLayerWidth / 2;
                    this.anchorPointY = this.lastLayerHeight / 2;
                    return;
                case 5:
                    this.anchorPointX = this.lastLayerWidth;
                    this.anchorPointY = this.lastLayerHeight / 2;
                    return;
                case 6:
                    this.anchorPointX = 0;
                    this.anchorPointY = this.lastLayerHeight;
                    return;
                case 7:
                    this.anchorPointX = this.lastLayerWidth / 2;
                    this.anchorPointY = this.lastLayerHeight;
                    return;
                case 8:
                    this.anchorPointX = this.lastLayerWidth;
                    this.anchorPointY = this.lastLayerHeight;
                    return;
                default:
                    return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public int[] getRealPositions(boolean z) {
        int[] iArr = new int[3];
        getAnchorPosition(z);
        if (this.relationCoordinates) {
            iArr[0] = this.anchorPointX + ((int) (this.lastLayerWidth * this.mX));
            iArr[1] = this.anchorPointY + ((int) (this.lastLayerHeight * this.mY));
            iArr[2] = 0;
        } else {
            iArr[0] = this.anchorPointX + ((int) this.mX);
            iArr[1] = this.anchorPointY + ((int) this.mY);
            iArr[2] = (int) this.mZ;
        }
        return iArr;
    }

    public static nexOverlayItem clone(nexOverlayItem nexoverlayitem) {
        nexOverlayItem nexoverlayitem2 = null;
        try {
            nexOverlayItem nexoverlayitem3 = (nexOverlayItem) nexoverlayitem.clone();
            try {
                nexoverlayitem3.mColorEffect = nexColorEffect.clone(nexoverlayitem.mColorEffect);
                nexOverlayImage nexoverlayimage = nexoverlayitem.mOverLayImage;
                if (nexoverlayimage == null) {
                    return nexoverlayitem3;
                }
                nexoverlayitem3.mOverLayImage = nexOverlayImage.clone(nexoverlayimage);
                return nexoverlayitem3;
            } catch (CloneNotSupportedException e) {
                e = e;
                nexoverlayitem2 = nexoverlayitem3;
                e.printStackTrace();
                return nexoverlayitem2;
            }
        } catch (CloneNotSupportedException e2) {
            e = e2;
        }
    }

    @Deprecated
    public void setLayerExpression(nexOverlayKineMasterExpression nexoverlaykinemasterexpression) {
        this.mLayerExpression = nexoverlaykinemasterexpression;
    }

    @Deprecated
    public nexOverlayKineMasterExpression getLayerExpression() {
        return this.mLayerExpression;
    }

    @Deprecated
    public void setLayerExpressionDuration(int i) {
        this.mLayerExpressionDuration = i;
    }

    @Deprecated
    public int getLayerExpressionDuration() {
        return this.mLayerExpressionDuration;
    }

    public void setColorEffect(nexColorEffect nexcoloreffect) {
        this.mColorEffect = nexcoloreffect;
    }

    private int getCombinedBrightness() {
        nexColorEffect nexcoloreffect = this.mColorEffect;
        if (nexcoloreffect == null) {
            return this.mBrightness;
        }
        return this.mBrightness + ((int) (nexcoloreffect.getBrightness() * 255.0f));
    }

    private int getCombinedContrast() {
        nexColorEffect nexcoloreffect = this.mColorEffect;
        if (nexcoloreffect == null) {
            return this.mContrast;
        }
        return this.mContrast + ((int) (nexcoloreffect.getContrast() * 255.0f));
    }

    private int getCombinedSaturation() {
        nexColorEffect nexcoloreffect = this.mColorEffect;
        if (nexcoloreffect == null) {
            return this.mSaturation;
        }
        return this.mSaturation + ((int) (nexcoloreffect.getSaturation() * 255.0f));
    }

    private int getTintColor() {
        nexColorEffect nexcoloreffect = this.mColorEffect;
        if (nexcoloreffect == null) {
            return 0;
        }
        return nexcoloreffect.getTintColor();
    }

    public int getId() {
        return this.mId;
    }

    @Deprecated
    public nexOverlayImage getOverlayImage() {
        return this.mOverLayImage;
    }

    public nexOverlayItem(String str, int i, int i2, int i3, int i4) {
        this.mUpdated = true;
        this.showItem = true;
        this.mId = 0;
        this.mAnimateTranslateXpos = 0.0f;
        this.mAnimateTranslateYpos = 0.0f;
        this.mAnimateTranslateZpos = 0.0f;
        this.anchorPoint = 0;
        this.mActiveAnimateList = new HashSet();
        this.mAlpha = 1.0f;
        this.mScaledX = 1.0f;
        this.mScaledY = 1.0f;
        this.mScaledZ = 1.0f;
        this.mRotateDegreeX = 0.0f;
        this.mRotateDegreeY = 0.0f;
        this.mRotateDegreeZ = 0.0f;
        this.mBrightness = 0;
        this.mContrast = 0;
        this.mSaturation = 0;
        this.mColorEffect = nexColorEffect.NONE;
        this.mLayerExpressionDuration = 1000;
        this.mMaskRect = new Rect();
        this.mTrimStartDuration = 0;
        this.mTrimEndDuration = 0;
        this.mAudioOnOff = true;
        this.mVolume = 100;
        this.mSpeedControl = 100;
        this.scratchPoint = new float[]{0.0f, 0.0f};
        this.scratchMatrix = new Matrix();
        this.mLayerExpression = nexOverlayKineMasterExpression.NONE;
        this.mOverlayTitle = false;
        this.bApplayLayerExpression = true;
        this.mZOrder = 0;
        this.mFlipMode = 0;
        nexOverlayPreset overlayPreset = nexOverlayPreset.getOverlayPreset();
        if (overlayPreset == null) {
            throw new nexSDKException("nexOverlayPreset is null");
        }
        nexOverlayImage overlayImage = overlayPreset.getOverlayImage(str);
        this.mOverLayImage = overlayImage;
        if (overlayImage == null) {
            throw new nexSDKException("Not found OverlayImage. id=" + str);
        } else if (i4 <= i3) {
            throw new InvalidRangeException(i3, i4);
        } else {
            int i5 = sLastId;
            this.mId = i5;
            sLastId = i5 + 1;
            this.mX = i;
            this.mY = i2;
            this.mStartTime = i3;
            this.mEndTime = i4;
            resetAnimate();
        }
    }

    public nexOverlayItem(String str, int i, boolean z, float f, float f2, int i2, int i3) {
        this.mUpdated = true;
        this.showItem = true;
        this.mId = 0;
        this.mAnimateTranslateXpos = 0.0f;
        this.mAnimateTranslateYpos = 0.0f;
        this.mAnimateTranslateZpos = 0.0f;
        this.anchorPoint = 0;
        this.mActiveAnimateList = new HashSet();
        this.mAlpha = 1.0f;
        this.mScaledX = 1.0f;
        this.mScaledY = 1.0f;
        this.mScaledZ = 1.0f;
        this.mRotateDegreeX = 0.0f;
        this.mRotateDegreeY = 0.0f;
        this.mRotateDegreeZ = 0.0f;
        this.mBrightness = 0;
        this.mContrast = 0;
        this.mSaturation = 0;
        this.mColorEffect = nexColorEffect.NONE;
        this.mLayerExpressionDuration = 1000;
        this.mMaskRect = new Rect();
        this.mTrimStartDuration = 0;
        this.mTrimEndDuration = 0;
        this.mAudioOnOff = true;
        this.mVolume = 100;
        this.mSpeedControl = 100;
        this.scratchPoint = new float[]{0.0f, 0.0f};
        this.scratchMatrix = new Matrix();
        this.mLayerExpression = nexOverlayKineMasterExpression.NONE;
        this.mOverlayTitle = false;
        this.bApplayLayerExpression = true;
        this.mZOrder = 0;
        this.mFlipMode = 0;
        nexOverlayPreset overlayPreset = nexOverlayPreset.getOverlayPreset();
        if (overlayPreset == null) {
            throw new nexSDKException("nexOverlayPreset is null");
        }
        nexOverlayImage overlayImage = overlayPreset.getOverlayImage(str);
        this.mOverLayImage = overlayImage;
        if (overlayImage == null) {
            throw new nexSDKException("Not found OverlayImage. id=" + str);
        } else if (i3 <= i2) {
            throw new InvalidRangeException(i2, i3);
        } else {
            int i4 = sLastId;
            this.mId = i4;
            sLastId = i4 + 1;
            this.anchorPoint = i;
            this.relationCoordinates = z;
            this.mX = f;
            this.mY = f2;
            this.mStartTime = i2;
            this.mEndTime = i3;
            resetAnimate();
        }
    }

    public nexOverlayItem(nexOverlayImage nexoverlayimage, int i, int i2, int i3, int i4) {
        this.mUpdated = true;
        this.showItem = true;
        this.mId = 0;
        this.mAnimateTranslateXpos = 0.0f;
        this.mAnimateTranslateYpos = 0.0f;
        this.mAnimateTranslateZpos = 0.0f;
        this.anchorPoint = 0;
        this.mActiveAnimateList = new HashSet();
        this.mAlpha = 1.0f;
        this.mScaledX = 1.0f;
        this.mScaledY = 1.0f;
        this.mScaledZ = 1.0f;
        this.mRotateDegreeX = 0.0f;
        this.mRotateDegreeY = 0.0f;
        this.mRotateDegreeZ = 0.0f;
        this.mBrightness = 0;
        this.mContrast = 0;
        this.mSaturation = 0;
        this.mColorEffect = nexColorEffect.NONE;
        this.mLayerExpressionDuration = 1000;
        this.mMaskRect = new Rect();
        this.mTrimStartDuration = 0;
        this.mTrimEndDuration = 0;
        this.mAudioOnOff = true;
        this.mVolume = 100;
        this.mSpeedControl = 100;
        this.scratchPoint = new float[]{0.0f, 0.0f};
        this.scratchMatrix = new Matrix();
        this.mLayerExpression = nexOverlayKineMasterExpression.NONE;
        this.mOverlayTitle = false;
        this.bApplayLayerExpression = true;
        this.mZOrder = 0;
        this.mFlipMode = 0;
        int i5 = sLastId;
        this.mId = i5;
        sLastId = i5 + 1;
        if (i4 <= i3) {
            throw new InvalidRangeException(i3, i4);
        }
        this.mOverLayImage = nexoverlayimage;
        this.mX = i;
        this.mY = i2;
        this.mStartTime = i3;
        this.mEndTime = i4;
        resetAnimate();
    }

    public nexOverlayItem(nexOverlayImage nexoverlayimage, int i, boolean z, float f, float f2, int i2, int i3) {
        this.mUpdated = true;
        this.showItem = true;
        this.mId = 0;
        this.mAnimateTranslateXpos = 0.0f;
        this.mAnimateTranslateYpos = 0.0f;
        this.mAnimateTranslateZpos = 0.0f;
        this.anchorPoint = 0;
        this.mActiveAnimateList = new HashSet();
        this.mAlpha = 1.0f;
        this.mScaledX = 1.0f;
        this.mScaledY = 1.0f;
        this.mScaledZ = 1.0f;
        this.mRotateDegreeX = 0.0f;
        this.mRotateDegreeY = 0.0f;
        this.mRotateDegreeZ = 0.0f;
        this.mBrightness = 0;
        this.mContrast = 0;
        this.mSaturation = 0;
        this.mColorEffect = nexColorEffect.NONE;
        this.mLayerExpressionDuration = 1000;
        this.mMaskRect = new Rect();
        this.mTrimStartDuration = 0;
        this.mTrimEndDuration = 0;
        this.mAudioOnOff = true;
        this.mVolume = 100;
        this.mSpeedControl = 100;
        this.scratchPoint = new float[]{0.0f, 0.0f};
        this.scratchMatrix = new Matrix();
        this.mLayerExpression = nexOverlayKineMasterExpression.NONE;
        this.mOverlayTitle = false;
        this.bApplayLayerExpression = true;
        this.mZOrder = 0;
        this.mFlipMode = 0;
        int i4 = sLastId;
        this.mId = i4;
        sLastId = i4 + 1;
        if (i3 <= i2) {
            throw new InvalidRangeException(i2, i3);
        }
        this.mOverLayImage = nexoverlayimage;
        this.anchorPoint = i;
        this.relationCoordinates = z;
        this.mX = f;
        this.mY = f2;
        this.mStartTime = i2;
        this.mEndTime = i3;
        resetAnimate();
    }

    @Deprecated
    public nexOverlayItem(nexOverlayKineMasterText nexoverlaykinemastertext, int i, int i2, int i3, int i4) {
        this.mUpdated = true;
        this.showItem = true;
        this.mId = 0;
        this.mAnimateTranslateXpos = 0.0f;
        this.mAnimateTranslateYpos = 0.0f;
        this.mAnimateTranslateZpos = 0.0f;
        this.anchorPoint = 0;
        this.mActiveAnimateList = new HashSet();
        this.mAlpha = 1.0f;
        this.mScaledX = 1.0f;
        this.mScaledY = 1.0f;
        this.mScaledZ = 1.0f;
        this.mRotateDegreeX = 0.0f;
        this.mRotateDegreeY = 0.0f;
        this.mRotateDegreeZ = 0.0f;
        this.mBrightness = 0;
        this.mContrast = 0;
        this.mSaturation = 0;
        this.mColorEffect = nexColorEffect.NONE;
        this.mLayerExpressionDuration = 1000;
        this.mMaskRect = new Rect();
        this.mTrimStartDuration = 0;
        this.mTrimEndDuration = 0;
        this.mAudioOnOff = true;
        this.mVolume = 100;
        this.mSpeedControl = 100;
        this.scratchPoint = new float[]{0.0f, 0.0f};
        this.scratchMatrix = new Matrix();
        this.mLayerExpression = nexOverlayKineMasterExpression.NONE;
        this.mOverlayTitle = false;
        this.bApplayLayerExpression = true;
        this.mZOrder = 0;
        this.mFlipMode = 0;
        nexoverlaykinemastertext.getClass();
        int i5 = sLastId;
        this.mId = i5;
        sLastId = i5 + 1;
        if (i4 <= i3) {
            throw new InvalidRangeException(i3, i4);
        }
        this.mOverLayImage = nexoverlaykinemastertext;
        this.mX = i;
        this.mY = i2;
        this.mStartTime = i3;
        this.mEndTime = i4;
        resetAnimate();
    }

    @Deprecated
    public nexOverlayItem(nexOverlayKineMasterText nexoverlaykinemastertext, int i, boolean z, float f, float f2, int i2, int i3) {
        this.mUpdated = true;
        this.showItem = true;
        this.mId = 0;
        this.mAnimateTranslateXpos = 0.0f;
        this.mAnimateTranslateYpos = 0.0f;
        this.mAnimateTranslateZpos = 0.0f;
        this.anchorPoint = 0;
        this.mActiveAnimateList = new HashSet();
        this.mAlpha = 1.0f;
        this.mScaledX = 1.0f;
        this.mScaledY = 1.0f;
        this.mScaledZ = 1.0f;
        this.mRotateDegreeX = 0.0f;
        this.mRotateDegreeY = 0.0f;
        this.mRotateDegreeZ = 0.0f;
        this.mBrightness = 0;
        this.mContrast = 0;
        this.mSaturation = 0;
        this.mColorEffect = nexColorEffect.NONE;
        this.mLayerExpressionDuration = 1000;
        this.mMaskRect = new Rect();
        this.mTrimStartDuration = 0;
        this.mTrimEndDuration = 0;
        this.mAudioOnOff = true;
        this.mVolume = 100;
        this.mSpeedControl = 100;
        this.scratchPoint = new float[]{0.0f, 0.0f};
        this.scratchMatrix = new Matrix();
        this.mLayerExpression = nexOverlayKineMasterExpression.NONE;
        this.mOverlayTitle = false;
        this.bApplayLayerExpression = true;
        this.mZOrder = 0;
        this.mFlipMode = 0;
        nexoverlaykinemastertext.getClass();
        int i4 = sLastId;
        this.mId = i4;
        sLastId = i4 + 1;
        if (i3 <= i2) {
            throw new InvalidRangeException(i2, i3);
        }
        this.mOverLayImage = nexoverlaykinemastertext;
        this.anchorPoint = i;
        this.relationCoordinates = z;
        this.mX = f;
        this.mY = f2;
        this.mStartTime = i2;
        this.mEndTime = i3;
        resetAnimate();
    }

    public nexOverlayItem(nexOverlayFilter nexoverlayfilter, int i, int i2, int i3, int i4) {
        this.mUpdated = true;
        this.showItem = true;
        this.mId = 0;
        this.mAnimateTranslateXpos = 0.0f;
        this.mAnimateTranslateYpos = 0.0f;
        this.mAnimateTranslateZpos = 0.0f;
        this.anchorPoint = 0;
        this.mActiveAnimateList = new HashSet();
        this.mAlpha = 1.0f;
        this.mScaledX = 1.0f;
        this.mScaledY = 1.0f;
        this.mScaledZ = 1.0f;
        this.mRotateDegreeX = 0.0f;
        this.mRotateDegreeY = 0.0f;
        this.mRotateDegreeZ = 0.0f;
        this.mBrightness = 0;
        this.mContrast = 0;
        this.mSaturation = 0;
        this.mColorEffect = nexColorEffect.NONE;
        this.mLayerExpressionDuration = 1000;
        this.mMaskRect = new Rect();
        this.mTrimStartDuration = 0;
        this.mTrimEndDuration = 0;
        this.mAudioOnOff = true;
        this.mVolume = 100;
        this.mSpeedControl = 100;
        this.scratchPoint = new float[]{0.0f, 0.0f};
        this.scratchMatrix = new Matrix();
        this.mLayerExpression = nexOverlayKineMasterExpression.NONE;
        this.mOverlayTitle = false;
        this.bApplayLayerExpression = true;
        this.mZOrder = 0;
        this.mFlipMode = 0;
        this.mOverLayFilter = nexoverlayfilter;
        if (i4 <= i3) {
            throw new InvalidRangeException(i3, i4);
        }
        int i5 = sLastId;
        this.mId = i5;
        sLastId = i5 + 1;
        this.mX = i;
        this.mY = i2;
        this.mStartTime = i3;
        this.mEndTime = i4;
        resetAnimate();
    }

    public nexOverlayItem(nexOverlayFilter nexoverlayfilter, int i, boolean z, float f, float f2, int i2, int i3) {
        this.mUpdated = true;
        this.showItem = true;
        this.mId = 0;
        this.mAnimateTranslateXpos = 0.0f;
        this.mAnimateTranslateYpos = 0.0f;
        this.mAnimateTranslateZpos = 0.0f;
        this.anchorPoint = 0;
        this.mActiveAnimateList = new HashSet();
        this.mAlpha = 1.0f;
        this.mScaledX = 1.0f;
        this.mScaledY = 1.0f;
        this.mScaledZ = 1.0f;
        this.mRotateDegreeX = 0.0f;
        this.mRotateDegreeY = 0.0f;
        this.mRotateDegreeZ = 0.0f;
        this.mBrightness = 0;
        this.mContrast = 0;
        this.mSaturation = 0;
        this.mColorEffect = nexColorEffect.NONE;
        this.mLayerExpressionDuration = 1000;
        this.mMaskRect = new Rect();
        this.mTrimStartDuration = 0;
        this.mTrimEndDuration = 0;
        this.mAudioOnOff = true;
        this.mVolume = 100;
        this.mSpeedControl = 100;
        this.scratchPoint = new float[]{0.0f, 0.0f};
        this.scratchMatrix = new Matrix();
        this.mLayerExpression = nexOverlayKineMasterExpression.NONE;
        this.mOverlayTitle = false;
        this.bApplayLayerExpression = true;
        this.mZOrder = 0;
        this.mFlipMode = 0;
        this.mOverLayFilter = nexoverlayfilter;
        if (i3 <= i2) {
            throw new InvalidRangeException(i2, i3);
        }
        int i4 = sLastId;
        this.mId = i4;
        sLastId = i4 + 1;
        this.anchorPoint = i;
        this.relationCoordinates = z;
        this.mX = f;
        this.mY = f2;
        this.mStartTime = i2;
        this.mEndTime = i3;
        resetAnimate();
    }

    public boolean getAudioOnOff() {
        return this.mAudioOnOff;
    }

    public void setAudioOnOff(boolean z) {
        if (!isVideo()) {
            throw new ClipIsNotVideoException();
        }
        if (this.mAudioOnOff != z) {
            this.mUpdated = true;
        }
        this.mAudioOnOff = z;
    }

    public int getVolume() {
        return this.mVolume;
    }

    public void setVolume(int i) {
        if (!isVideo()) {
            throw new ClipIsNotVideoException();
        }
        if (this.mVolume != i) {
            this.mUpdated = true;
        }
        this.mVolume = i;
    }

    public void setSpeedControl(int i) {
        if (!isVideo()) {
            throw new ClipIsNotVideoException();
        }
        int speedControlTab = speedControlTab(i);
        if (this.mSpeedControl == speedControlTab) {
            return;
        }
        this.mUpdated = true;
        this.mSpeedControl = speedControlTab;
    }

    public int getSpeedControl() {
        return this.mSpeedControl;
    }

    private int speedControlTab(int i) {
        int[] iArr = {13, 25, 50, 75, 100, 125, 150, 175, 200, StatusCode.BAD_REQUEST};
        for (int i2 = 0; i2 < 10; i2++) {
            if (iArr[i2] >= i) {
                return iArr[i2];
            }
        }
        return StatusCode.BAD_REQUEST;
    }

    public void resetAnimate() {
        this.mAnimateTranslateXpos = 0.0f;
        this.mAnimateTranslateYpos = 0.0f;
        this.mAnimateTranslateZpos = 0.0f;
        this.mAnimateLastAlpha = this.mAlpha;
        this.mAnimateLastRotateDegreeX = this.mRotateDegreeX;
        this.mAnimateLastRotateDegreeY = this.mRotateDegreeY;
        this.mAnimateLastRotateDegreeZ = this.mRotateDegreeZ;
        this.mAnimateLastScaledX = this.mScaledX;
        this.mAnimateLastScaledY = this.mScaledY;
        this.mAnimateLastScaledZ = this.mScaledZ;
        this.mActiveAnimateList.clear();
    }

    public int getPositionX() {
        return getRealPositions(false)[0];
    }

    public int getPositionY() {
        return getRealPositions(false)[1];
    }

    public void setPosition(int i, int i2) {
        updateCoordinates(false);
        if (this.relationCoordinates) {
            this.mX = (i - this.anchorPointX) / this.lastLayerWidth;
            this.mY = (i2 - this.anchorPointY) / this.lastLayerHeight;
        } else {
            this.mX = i - this.anchorPointX;
            this.mY = i2 - this.anchorPointY;
        }
        this.mUpdated = true;
        resetAnimate();
    }

    public void movePosition(float f, float f2) {
        this.mX = f;
        this.mY = f2;
        this.mUpdated = true;
        resetAnimate();
    }

    public void setAnchor(int i) {
        this.anchorPoint = i;
        this.mUpdated = true;
        resetAnimate();
    }

    public int getAnchor() {
        return this.anchorPoint;
    }

    public void setRelationCoordinates(boolean z) {
        if (this.relationCoordinates != z) {
            this.mUpdated = true;
        }
        this.relationCoordinates = z;
    }

    public boolean getRelationCoordinates() {
        return this.relationCoordinates;
    }

    public float getAlpha() {
        return this.mAlpha;
    }

    public void setAlpha(float f) {
        if (this.mAlpha != f) {
            this.mUpdated = true;
        }
        this.mAlpha = f;
        resetAnimate();
    }

    public float getScaledX() {
        return this.mScaledX;
    }

    public float getScaledY() {
        return this.mScaledY;
    }

    public float getScaledZ() {
        return this.mScaledZ;
    }

    public void setScale(float f, float f2) {
        setScale(f, f2, 1.0f);
    }

    public void setScale(float f, float f2, float f3) {
        this.mScaledX = f;
        this.mScaledY = f2;
        this.mScaledZ = f3;
        this.mUpdated = true;
        resetAnimate();
    }

    public int getRotate() {
        return getRotateZ();
    }

    public int getRotateX() {
        return (int) this.mRotateDegreeX;
    }

    public int getRotateY() {
        return (int) this.mRotateDegreeY;
    }

    public int getRotateZ() {
        return (int) this.mRotateDegreeZ;
    }

    public void setTrim(int i, int i2) {
        if (isVideo()) {
            if (i2 <= i) {
                throw new InvalidRangeException(i, i2);
            }
            this.mTrimStartDuration = i;
            int totalTime = this.mOverLayImage.getVideoClipInfo().getTotalTime() - i2;
            this.mTrimEndDuration = totalTime;
            this.mUpdated = true;
            if (totalTime >= 0 && this.mTrimStartDuration >= 0) {
                return;
            }
            throw new InvalidRangeException(this.mTrimStartDuration, this.mTrimEndDuration);
        }
        throw new ClipIsNotVideoException();
    }

    public void clearTrim() {
        if (!isVideo()) {
            throw new ClipIsNotVideoException();
        }
        this.mTrimStartDuration = 0;
        this.mTrimEndDuration = 0;
    }

    public int getStartTrimTime() {
        return this.mTrimStartDuration;
    }

    public int getEndTrimTime() {
        return this.mTrimEndDuration;
    }

    public void setRotate(int i) {
        setRotate(0, 0, i);
    }

    public void setRotate(int i, int i2, int i3) {
        this.mRotateDegreeX = i;
        this.mRotateDegreeY = i2;
        this.mRotateDegreeZ = i3;
        this.mUpdated = true;
        resetAnimate();
    }

    public void setRotate(float f) {
        setRotate(0.0f, 0.0f, f);
    }

    public void setRotate(float f, float f2, float f3) {
        this.mRotateDegreeX = f;
        this.mRotateDegreeY = f2;
        this.mRotateDegreeZ = f3;
        this.mUpdated = true;
        resetAnimate();
    }

    public int getBrightness() {
        return this.mBrightness;
    }

    public int getContrast() {
        return this.mContrast;
    }

    public int getSaturation() {
        return this.mSaturation;
    }

    public boolean setBrightness(int i) {
        if (i < -255 || i > 255) {
            return false;
        }
        if (this.mBrightness != i) {
            this.mUpdated = true;
        }
        this.mBrightness = i;
        return true;
    }

    public boolean setContrast(int i) {
        if (i < -255 || i > 255) {
            return false;
        }
        if (this.mContrast != i) {
            this.mUpdated = true;
        }
        this.mContrast = i;
        return true;
    }

    public boolean setSaturation(int i) {
        if (i < -255 || i > 255) {
            return false;
        }
        if (this.mSaturation != i) {
            this.mUpdated = true;
        }
        this.mSaturation = i;
        return true;
    }

    public void setTime(int i) {
        if (this.mTime > i) {
            resetAnimate();
        }
        this.mTime = i;
    }

    @Deprecated
    public void setLayerExpressionParam(boolean z) {
        if (this.bApplayLayerExpression != z) {
            this.mUpdated = true;
        }
        this.bApplayLayerExpression = z;
    }

    @Deprecated
    public boolean getLayerExpressiontParam() {
        return this.bApplayLayerExpression;
    }

    private void runAnimate(nexAnimate nexanimate, int i) {
        if (nexanimate instanceof nexAnimate.AnimateImages) {
            this.animateResourceId = nexanimate.getImageResourceId(i);
            Log.d(TAG, "[" + getId() + "][" + i + "]AnimateImages=(" + this.animateResourceId + ")");
        } else if (nexanimate instanceof nexAnimate.Move) {
            if (this.mLayerExpression.getID() != 0) {
                return;
            }
            this.mAnimateTranslateXpos = nexanimate.getTranslatePosition(i, 1);
            this.mAnimateTranslateYpos = nexanimate.getTranslatePosition(i, 2);
            this.mAnimateTranslateZpos = nexanimate.getTranslatePosition(i, 3);
            Log.d(TAG, "[" + getId() + "][" + i + "]Move to=(" + this.mAnimateTranslateXpos + "," + this.mAnimateTranslateYpos + "," + this.mAnimateTranslateZpos + ")");
        } else if (nexanimate instanceof nexAnimate.Alpha) {
            if (this.mLayerExpression.getID() != 0) {
                return;
            }
            this.mAnimateLastAlpha = nexanimate.getAlpha(i);
            Log.d(TAG, "[" + getId() + "][" + i + "]Alpha =(" + this.mAnimateLastAlpha + ")");
        } else if (nexanimate instanceof nexAnimate.Rotate) {
            if (this.mLayerExpression.getID() != 0) {
                return;
            }
            this.mAnimateLastRotateDegreeX = nexanimate.getAngleDegree(i, this.mRotateDegreeX, 1);
            this.mAnimateLastRotateDegreeY = nexanimate.getAngleDegree(i, this.mRotateDegreeY, 2);
            this.mAnimateLastRotateDegreeZ = nexanimate.getAngleDegree(i, this.mRotateDegreeZ, 3);
            Log.d(TAG, "[" + getId() + "][" + i + "]Rotate =(" + this.mAnimateLastRotateDegreeX + "," + this.mAnimateLastRotateDegreeY + "," + this.mAnimateLastRotateDegreeZ + ")");
        } else if (nexanimate instanceof nexAnimate.Scale) {
            if (this.mLayerExpression.getID() != 0) {
                return;
            }
            this.mAnimateLastScaledX = nexanimate.getScaledRatio(i, this.mScaledX, 1);
            this.mAnimateLastScaledY = nexanimate.getScaledRatio(i, this.mScaledY, 2);
            this.mAnimateLastScaledZ = nexanimate.getScaledRatio(i, this.mScaledY, 3);
            Log.d(TAG, "[" + getId() + "][" + i + "]Scale =(" + this.mAnimateLastScaledX + "," + this.mAnimateLastScaledY + "," + this.mAnimateLastScaledZ + ")");
        } else if (this.mLayerExpression.getID() != 0 || !nexanimate.onFreeTypeAnimate(i, this)) {
        } else {
            this.mAnimateTranslateXpos = nexanimate.mdX;
            this.mAnimateTranslateYpos = nexanimate.mdY;
            this.mAnimateTranslateZpos = nexanimate.mdZ;
            this.mAnimateLastAlpha = nexanimate.mAlpha;
            this.mAnimateLastRotateDegreeX = nexanimate.mRotateDegreeX;
            this.mAnimateLastRotateDegreeY = nexanimate.mRotateDegreeY;
            this.mAnimateLastRotateDegreeZ = nexanimate.mRotateDegreeZ;
            this.mAnimateLastScaledX = nexanimate.mScaledX;
            this.mAnimateLastScaledY = nexanimate.mScaledY;
            this.mAnimateLastScaledZ = nexanimate.mScaledZ;
            Log.d(TAG, "[" + getId() + "][" + i + "]FreeType =(" + this.mAnimateTranslateXpos + "," + this.mAnimateTranslateYpos + "," + this.mAnimateTranslateZpos + ")");
        }
    }

    private float getRelativeScale(int i, int i2) {
        int width = nexApplicationConfig.getAspectProfile().getWidth();
        nexApplicationConfig.getAspectProfile().getHeight();
        float f = width > i ? i / width : 1.0f;
        if (1.0f > f) {
            return f;
        }
        return 1.0f;
    }

    public void renderOverlay(LayerRenderer layerRenderer, Context context) {
        String str;
        Bitmap bitmap;
        if (!this.showItem) {
            return;
        }
        this.lastLayerWidth = (int) layerRenderer.a();
        int b = (int) layerRenderer.b();
        this.lastLayerHeight = b;
        float relativeScale = this.relationCoordinates ? getRelativeScale(this.lastLayerWidth, b) : 1.0f;
        setTime(layerRenderer.g());
        int[] realPositions = getRealPositions(true);
        int i = realPositions[0];
        int i2 = realPositions[1];
        int i3 = realPositions[2];
        this.animateResourceId = 0;
        List<nexAnimate> list = this.mAniList;
        if (list != null) {
            for (nexAnimate nexanimate : list) {
                int i4 = this.mTime - this.mStartTime;
                boolean contains = this.mActiveAnimateList.contains(nexanimate);
                if (nexanimate.mStartTime > i4 || nexanimate.getEndTime() <= i4) {
                    if (contains) {
                        this.mActiveAnimateList.remove(nexanimate);
                        runAnimate(nexanimate, nexanimate.getEndTime());
                    }
                } else if (!contains) {
                    this.mActiveAnimateList.add(nexanimate);
                    if (i4 - nexanimate.mStartTime < 33) {
                        runAnimate(nexanimate, 0);
                    } else {
                        runAnimate(nexanimate, i4);
                    }
                } else {
                    runAnimate(nexanimate, i4);
                }
            }
        }
        layerRenderer.i();
        Mask mask = this.mMask;
        if (mask != null && mask.onOff && !this.mMask.syncAnimationOverlayItem) {
            layerRenderer.h();
            layerRenderer.a(LayerRenderer.RenderTarget.Mask);
            this.mMask.getPosition(this.mMaskRect);
            if (this.mMask.getMaskImage() != null) {
                Bitmap maskImage = this.mMask.getMaskImage();
                Rect rect = this.mMaskRect;
                layerRenderer.a(maskImage, rect.left, rect.top, rect.right, rect.bottom);
            } else {
                Rect rect2 = this.mMaskRect;
                layerRenderer.a(-1, rect2.left, rect2.top, rect2.right, rect2.bottom);
            }
            layerRenderer.a(true);
            layerRenderer.a(LayerRenderer.RenderTarget.Normal);
        }
        layerRenderer.a(i, i2);
        layerRenderer.a(this.mAnimateTranslateXpos, this.mAnimateTranslateYpos);
        layerRenderer.b(this.mAnimateLastScaledX * relativeScale, this.mAnimateLastScaledY * relativeScale);
        layerRenderer.a(this.mAnimateLastRotateDegreeX, 1.0f, 0.0f, 0.0f);
        layerRenderer.a(this.mAnimateLastRotateDegreeY, 0.0f, 1.0f, 0.0f);
        layerRenderer.a(this.mAnimateLastRotateDegreeZ, 0.0f, 0.0f, 1.0f);
        if (this.showOutLien) {
            layerRenderer.a(1.0f);
        } else {
            layerRenderer.a(this.mAnimateLastAlpha);
        }
        Mask mask2 = this.mMask;
        if (mask2 != null && mask2.onOff && this.mMask.syncAnimationOverlayItem) {
            layerRenderer.h();
            layerRenderer.a(LayerRenderer.RenderTarget.Mask);
            this.mMask.getPosition(this.mMaskRect);
            if (this.mMask.getMaskImage() != null) {
                Bitmap maskImage2 = this.mMask.getMaskImage();
                Rect rect3 = this.mMaskRect;
                layerRenderer.a(maskImage2, rect3.left, rect3.top, rect3.right, rect3.bottom);
            } else {
                Rect rect4 = this.mMaskRect;
                layerRenderer.a(-1, rect4.left, rect4.top, rect4.right, rect4.bottom);
            }
            layerRenderer.a(true);
            layerRenderer.a(LayerRenderer.RenderTarget.Normal);
        }
        AwakeAsset awakeAsset = this.awakeAsset;
        if (awakeAsset != null) {
            awakeAsset.onRender(layerRenderer, this.cacheMotion, this.mStartTime, this.mEndTime);
        } else {
            nexOverlayImage nexoverlayimage = this.mOverLayImage;
            if (nexoverlayimage != null) {
                Bitmap bitmap2 = null;
                if (nexoverlayimage.isVideo()) {
                    if (getChromaKey().getChromaKeyEnabled()) {
                        layerRenderer.b(getChromaKey().getChromaKeyEnabled());
                        layerRenderer.c(getChromaKey().getChromaKeyMaskEnabled());
                        layerRenderer.a(getChromaKey().m_chromaKeyColor, getChromaKey().m_chromaKeyClipFG, getChromaKey().m_chromaKeyClipBG, getChromaKey().m_chromaKeyBlend_x0, getChromaKey().m_chromaKeyBlend_y0, getChromaKey().m_chromaKeyBlend_x1, getChromaKey().m_chromaKeyBlend_y1);
                    }
                    int b2 = EditorGlobal.a().b(layerRenderer.o().id, this.mVideoEngineId);
                    layerRenderer.a(com.nexstreaming.app.common.thememath.a.a(getCombinedBrightness() / 255.0f, getCombinedContrast() / 255.0f, getCombinedSaturation() / 255.0f, getTintColor()));
                    layerRenderer.a(b2, 0.0f, 0.0f, this.mOverLayImage.getVideoClipInfo().getWidth(), this.mOverLayImage.getVideoClipInfo().getHeight(), this.mFlipMode);
                    layerRenderer.b(false);
                    layerRenderer.a((Bitmap) null);
                } else {
                    com.nexstreaming.kminternal.nexvideoeditor.b a2 = com.nexstreaming.kminternal.nexvideoeditor.b.a();
                    int i5 = this.animateResourceId;
                    if (i5 == 0) {
                        i5 = this.mOverLayImage.mResourceId;
                    }
                    if (i5 == 0) {
                        str = this.mOverLayImage.getUserBitmapID();
                        nexOverlayImage nexoverlayimage2 = this.mOverLayImage;
                        if (nexoverlayimage2.mUpdate) {
                            nexoverlayimage2.mUpdate = false;
                            a2.b(str);
                        } else {
                            bitmap2 = a2.a(str);
                        }
                        if (bitmap2 == null) {
                            Bitmap userBitmap = this.mOverLayImage.getUserBitmap();
                            if (userBitmap != null) {
                                try {
                                    a2.a(str, userBitmap);
                                } catch (NullPointerException e) {
                                    Log.d(TAG, "exception: message=" + e.getMessage());
                                }
                            }
                            bitmap = userBitmap;
                        } else {
                            bitmap = bitmap2;
                        }
                    } else {
                        String str2 = this.mOverLayImage.getUserBitmapID() + i5;
                        Bitmap a3 = a2.a(str2);
                        if (a3 == null) {
                            Bitmap decodeResource = BitmapFactory.decodeResource(context.getResources(), i5);
                            if (decodeResource != null) {
                                try {
                                    a2.a(str2, decodeResource);
                                } catch (NullPointerException e2) {
                                    Log.d(TAG, "exception: message=" + e2.getMessage());
                                }
                            }
                            bitmap = decodeResource;
                            str = str2;
                        } else {
                            str = str2;
                            bitmap = a3;
                        }
                    }
                    if (bitmap != null) {
                        Log.d(TAG, "renderOverlay bitmap id = " + str + ", wid = " + bitmap.getWidth() + ", hei = " + bitmap.getHeight() + ", X=" + i + ", Y=" + i2 + ", Z=" + i3 + ", ScaledX=" + this.mAnimateLastScaledX + ", Alpha=" + this.mAnimateLastAlpha + ", Rx=" + this.mAnimateLastRotateDegreeX + ", Ry=" + this.mAnimateLastRotateDegreeY + ", Rz=" + this.mAnimateLastRotateDegreeZ + ", flip=" + this.mFlipMode + ", baseScale=" + relativeScale + ", cts=" + layerRenderer.g());
                        layerRenderer.a(com.nexstreaming.app.common.thememath.a.a(((float) getCombinedBrightness()) / 255.0f, ((float) getCombinedContrast()) / 255.0f, ((float) getCombinedSaturation()) / 255.0f, getTintColor()));
                        nexOverlayImage nexoverlayimage3 = this.mOverLayImage;
                        if (nexoverlayimage3 != null) {
                            Rect drawBitmapPosition = getDrawBitmapPosition(nexoverlayimage3.getAnchorPoint(), bitmap.getWidth(), bitmap.getHeight());
                            layerRenderer.a(bitmap, drawBitmapPosition.left, drawBitmapPosition.top, drawBitmapPosition.right, drawBitmapPosition.bottom, this.mFlipMode);
                        } else {
                            layerRenderer.b(bitmap, this.mFlipMode);
                        }
                    }
                }
            }
        }
        layerRenderer.j();
        if (!this.showOutLien) {
            return;
        }
        renderOutLine(this, layerRenderer);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Rect getDrawBitmapPosition(int i, int i2, int i3) {
        int i4 = -i2;
        int i5 = i4 / 2;
        int i6 = -i3;
        int i7 = i6 / 2;
        int i8 = i2 / 2;
        int i9 = i3 / 2;
        Rect rect = new Rect(i5, i7, i8, i9);
        switch (i) {
            case 0:
                rect.set(0, 0, i2, i3);
                break;
            case 1:
                rect.set(i5, 0, i8, i3);
                break;
            case 2:
                rect.set(i4, 0, 0, i3);
                break;
            case 3:
                rect.set(0, i7, i2, i9);
                break;
            case 5:
                rect.set(i4, i7, 0, i9);
                break;
            case 6:
                rect.set(0, i6, i2, 0);
                break;
            case 7:
                rect.set(i5, i6, i8, 0);
                break;
            case 8:
                rect.set(i4, i6, 0, 0);
                break;
        }
        return rect;
    }

    public int getStartTime() {
        return this.mStartTime;
    }

    public int getEndTime() {
        return this.mEndTime;
    }

    public void setTimePosition(int i, int i2) {
        if (i2 <= i) {
            throw new InvalidRangeException(i, i2);
        }
        this.mUpdated = true;
        this.mStartTime = i;
        this.mEndTime = i2;
    }

    public int getAnimateEndTime() {
        List<nexAnimate> list = this.mAniList;
        int i = 0;
        if (list == null) {
            return 0;
        }
        for (nexAnimate nexanimate : list) {
            if (i < nexanimate.getEndTime()) {
                i = nexanimate.getEndTime();
            }
        }
        return i;
    }

    public void addAnimate(nexAnimate nexanimate) {
        if (this.mAniList == null) {
            this.mAniList = new ArrayList();
        }
        this.mAniList.add(nexanimate);
        this.mUpdated = true;
        Collections.sort(this.mAniList, new a());
    }

    public void clearAnimate() {
        List<nexAnimate> list = this.mAniList;
        if (list != null) {
            list.clear();
            this.mUpdated = true;
            resetAnimate();
        }
    }

    /* loaded from: classes3.dex */
    public static class a implements Comparator<nexAnimate> {
        private a() {
        }

        @Override // java.util.Comparator
        /* renamed from: a */
        public int compare(nexAnimate nexanimate, nexAnimate nexanimate2) {
            int i = nexanimate.mStartTime;
            int i2 = nexanimate2.mStartTime;
            if (i > i2) {
                return -1;
            }
            return i < i2 ? 1 : 0;
        }
    }

    public boolean isVideo() {
        nexOverlayImage nexoverlayimage = this.mOverLayImage;
        if (nexoverlayimage == null) {
            return false;
        }
        return nexoverlayimage.isVideo();
    }

    public void setOverlayTitle(boolean z) {
        this.mOverlayTitle = z;
    }

    public boolean getOverlayTitle() {
        return this.mOverlayTitle;
    }

    /* loaded from: classes3.dex */
    public static class ChromaKey {
        private static final float CHROMA_DEF_BLEND_X0 = 0.25f;
        private static final float CHROMA_DEF_BLEND_X1 = 0.75f;
        private static final float CHROMA_DEF_BLEND_Y0 = 0.25f;
        private static final float CHROMA_DEF_BLEND_Y1 = 0.75f;
        private static final float CHROMA_DEF_CLIP_BG = 0.5f;
        private static final float CHROMA_DEF_CLIP_FG = 0.72f;
        private static final int CHROMA_DEF_COLOR = -16711936;
        private static final int CHROMA_UNSET_COLOR = 0;
        private boolean m_chromaKeyEnabled;
        private boolean m_chromaKeyMaskEnabled;
        private float[] m_chromaKeyDivisions = {0.05f, 0.3f, CHROMA_DEF_CLIP_BG, 0.65f};
        private float[] m_chromaKeyStrengths = {0.0f, 0.25f, 0.75f, 1.0f};
        private int m_chromaKeyColor = 0;
        private float m_chromaKeyClipFG = CHROMA_DEF_CLIP_FG;
        private float m_chromaKeyClipBG = CHROMA_DEF_CLIP_BG;
        private float m_chromaKeyBlend_x0 = 0.25f;
        private float m_chromaKeyBlend_y0 = 0.25f;
        private float m_chromaKeyBlend_x1 = 0.75f;
        private float m_chromaKeyBlend_y1 = 0.75f;

        public static int[] getChromaKeyRecommendedColors(Bitmap bitmap) {
            if (bitmap != null) {
                float[] fArr = new float[3];
                int[] iArr = new int[360];
                int width = bitmap.getWidth() * bitmap.getHeight();
                int[] iArr2 = new int[width];
                bitmap.getPixels(iArr2, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
                for (int i = 0; i < width; i++) {
                    Color.colorToHSV(iArr2[i], fArr);
                    if (fArr[1] >= 0.3f && fArr[2] >= 0.2f) {
                        int i2 = ((int) ((fArr[0] / 360.0f) * 360.0f)) % 360;
                        iArr[i2] = iArr[i2] + 1;
                    }
                }
                for (int i3 = 0; i3 < 360; i3++) {
                }
                int[] iArr3 = new int[14];
                int i4 = 0;
                for (int i5 = 0; i5 < 14; i5++) {
                    int i6 = -1;
                    int i7 = -1;
                    for (int i8 = 0; i8 < 360; i8++) {
                        if (iArr[i8] > i7) {
                            i7 = iArr[i8];
                            i6 = i8;
                        }
                    }
                    if (i6 < 0 || i7 < 5) {
                        break;
                    }
                    fArr[0] = (i6 * 360) / 360;
                    fArr[1] = 1.0f;
                    fArr[2] = 1.0f;
                    iArr3[i4] = Color.HSVToColor(fArr);
                    i4++;
                    for (int i9 = i6 - 3; i9 < i6 + 3; i9++) {
                        iArr[(i9 + 360) % 360] = -1;
                    }
                }
                if (i4 >= 14) {
                    return iArr3;
                }
                int[] iArr4 = new int[i4];
                System.arraycopy(iArr3, 0, iArr4, 0, i4);
                return iArr4;
            }
            return new int[0];
        }

        public void getChromaKeyDivisions(float[] fArr) {
            float[] fArr2 = this.m_chromaKeyDivisions;
            System.arraycopy(fArr2, 0, fArr, 0, fArr2.length);
        }

        public void getChromaKeyStrengths(float[] fArr) {
            float[] fArr2 = this.m_chromaKeyStrengths;
            System.arraycopy(fArr2, 0, fArr, 0, fArr2.length);
        }

        public void setChromaKeyStrengths(float[] fArr) {
            float[] fArr2 = this.m_chromaKeyStrengths;
            System.arraycopy(fArr, 0, fArr2, 0, fArr2.length);
        }

        public int getChromaKeyColor() {
            return this.m_chromaKeyColor;
        }

        public void setChromaKeyColor(int i) {
            this.m_chromaKeyColor = i;
        }

        public boolean getChromaKeyEnabled() {
            return this.m_chromaKeyEnabled;
        }

        public void setChromaKeyMaskEnabled(boolean z) {
            this.m_chromaKeyMaskEnabled = z;
        }

        public boolean getChromaKeyMaskEnabled() {
            return this.m_chromaKeyMaskEnabled;
        }

        public void setChromaKeyEnabled(boolean z) {
            this.m_chromaKeyEnabled = z;
        }

        public void setChromaKeyFGClip(float f) {
            this.m_chromaKeyClipFG = f;
        }

        public void setChromaKeyBGClip(float f) {
            this.m_chromaKeyClipBG = f;
        }

        public float getChromaKeyFGClip() {
            return this.m_chromaKeyClipFG;
        }

        public float getChromaKeyBGClip() {
            return this.m_chromaKeyClipBG;
        }

        public void setChromaKeyBlend(float[] fArr) {
            this.m_chromaKeyBlend_x0 = fArr[0];
            this.m_chromaKeyBlend_y0 = fArr[1];
            this.m_chromaKeyBlend_x1 = fArr[2];
            this.m_chromaKeyBlend_y1 = fArr[3];
        }

        public void getChromaKeyBlend(float[] fArr) {
            fArr[0] = this.m_chromaKeyBlend_x0;
            fArr[1] = this.m_chromaKeyBlend_y0;
            fArr[2] = this.m_chromaKeyBlend_x1;
            fArr[3] = this.m_chromaKeyBlend_y1;
        }
    }

    public ChromaKey getChromaKey() {
        if (this.mChromaKey == null) {
            this.mChromaKey = new ChromaKey();
        }
        return this.mChromaKey;
    }

    /* loaded from: classes3.dex */
    public class Mask {
        public static final int kSplit_Bottom = 4;
        public static final int kSplit_Left = 1;
        public static final int kSplit_LeftBottom = 7;
        public static final int kSplit_LeftTop = 5;
        public static final int kSplit_Right = 2;
        public static final int kSplit_RightBottom = 8;
        public static final int kSplit_RightTop = 6;
        public static final int kSplit_Top = 3;
        private int angle;
        private Bitmap maskImage;
        private boolean onOff;
        private Rect rectPosition = new Rect();
        private int splitMode;
        private boolean syncAnimationOverlayItem;
        private boolean vertical;

        public Mask() {
        }

        public void setState(boolean z) {
            this.onOff = z;
        }

        public boolean getState() {
            return this.onOff;
        }

        public void setAngle(int i) {
            this.angle = i;
        }

        public int getAngle() {
            return this.angle;
        }

        public int getSplitMode() {
            return this.splitMode;
        }

        public int width() {
            return this.rectPosition.width();
        }

        public int height() {
            return this.rectPosition.height();
        }

        public void setMaskImage(Bitmap bitmap) {
            this.maskImage = bitmap;
        }

        public Bitmap getMaskImage() {
            return this.maskImage;
        }

        public void setSplitMode(int i, boolean z) {
            if (this.splitMode == i && this.vertical == z) {
                return;
            }
            this.splitMode = i;
            this.vertical = z;
            int width = nexApplicationConfig.getAspectProfile().getWidth();
            int height = nexApplicationConfig.getAspectProfile().getHeight();
            switch (i) {
                case 1:
                    Rect rect = this.rectPosition;
                    rect.left = 0;
                    rect.top = 0;
                    rect.right = width / 2;
                    rect.bottom = height;
                    return;
                case 2:
                    Rect rect2 = this.rectPosition;
                    rect2.top = 0;
                    rect2.left = width / 2;
                    rect2.right = width;
                    rect2.bottom = height;
                    return;
                case 3:
                    Rect rect3 = this.rectPosition;
                    rect3.top = 0;
                    rect3.left = 0;
                    rect3.right = width;
                    rect3.bottom = height / 2;
                    return;
                case 4:
                    Rect rect4 = this.rectPosition;
                    rect4.top = height / 2;
                    rect4.left = 0;
                    rect4.right = width;
                    rect4.bottom = height;
                    return;
                case 5:
                    Rect rect5 = this.rectPosition;
                    rect5.top = 0;
                    rect5.left = 0;
                    rect5.right = width / 2;
                    rect5.bottom = height / 2;
                    return;
                case 6:
                    Rect rect6 = this.rectPosition;
                    rect6.top = 0;
                    rect6.left = width / 2;
                    rect6.right = width;
                    rect6.bottom = height / 2;
                    return;
                case 7:
                    Rect rect7 = this.rectPosition;
                    rect7.top = height / 2;
                    rect7.left = 0;
                    rect7.right = width / 2;
                    rect7.bottom = height;
                    return;
                case 8:
                    Rect rect8 = this.rectPosition;
                    rect8.top = height / 2;
                    rect8.left = width / 2;
                    rect8.right = width;
                    rect8.bottom = height;
                    return;
                default:
                    return;
            }
        }

        public void setPosition(int i, int i2, int i3, int i4) {
            this.splitMode = 0;
            Rect rect = this.rectPosition;
            rect.bottom = i4;
            rect.top = i2;
            rect.left = i;
            rect.right = i3;
        }

        public void setPosition(Rect rect) {
            this.splitMode = 0;
            Rect rect2 = this.rectPosition;
            rect2.bottom = rect.bottom;
            rect2.top = rect.top;
            rect2.left = rect.left;
            rect2.right = rect.right;
        }

        public void getPosition(Rect rect) {
            Rect rect2 = this.rectPosition;
            rect.bottom = rect2.bottom;
            rect.top = rect2.top;
            rect.left = rect2.left;
            rect.right = rect2.right;
        }

        public void setAnimateSyncFromOverlayItem(boolean z) {
            this.syncAnimationOverlayItem = z;
        }

        public boolean getAnimateSyncFromOverlayItem() {
            return this.syncAnimationOverlayItem;
        }
    }

    public Mask getMask() {
        if (this.mMask == null) {
            this.mMask = new Mask();
        }
        return this.mMask;
    }

    /* loaded from: classes3.dex */
    public class BoundInfo {
        private float angleX;
        private float angleY;
        private float angleZ;
        private Rect drawPosition;
        private int height;
        private Rect mask;
        private boolean maskOn;
        private float scaleX;
        private float scaleY;
        private int time;
        private int width;
        private float x;
        private float y;

        public String toString() {
            return "BoundInfo{scaleX=" + this.scaleX + ", scaleY=" + this.scaleY + ", x=" + this.x + ", y=" + this.y + ", angleX=" + this.angleX + ", angleY=" + this.angleY + ", angleZ=" + this.angleZ + ", width=" + this.width + ", height=" + this.height + ", time=" + this.time + ", maskOn=" + this.maskOn + ", mask=" + this.mask + ", drawPosition=" + this.drawPosition + '}';
        }

        public float getScaleX() {
            return this.scaleX;
        }

        public float getScaleY() {
            return this.scaleY;
        }

        public float getTranslateX() {
            return this.x;
        }

        public float getTranslateY() {
            return this.y;
        }

        public float getAngle() {
            return this.angleZ;
        }

        public int getWidth() {
            return this.width;
        }

        public int getHeight() {
            return this.height;
        }

        public int getTime() {
            return this.time;
        }

        public float getAngleX() {
            return this.angleX;
        }

        public float getAngleY() {
            return this.angleY;
        }

        public void getMaskBound(Rect rect) {
            Rect rect2 = this.mask;
            rect.set(rect2.left, rect2.top, rect2.right, rect2.bottom);
        }

        public void getDrawBound(Rect rect) {
            Rect rect2 = this.drawPosition;
            rect.set(rect2.left, rect2.top, rect2.right, rect2.bottom);
        }

        public boolean getMaskState() {
            return this.maskOn;
        }

        private BoundInfo() {
        }

        private BoundInfo(int i) {
            this.mask = new Rect();
            this.time = i;
            this.scaleX = nexOverlayItem.this.mScaledX;
            this.scaleY = nexOverlayItem.this.mScaledY;
            int[] realPositions = nexOverlayItem.this.getRealPositions(false);
            this.x = realPositions[0];
            this.y = realPositions[1];
            this.angleX = nexOverlayItem.this.mRotateDegreeX;
            this.angleY = nexOverlayItem.this.mRotateDegreeY;
            this.angleZ = nexOverlayItem.this.mRotateDegreeZ;
            if (nexOverlayItem.this.mOverLayImage != null) {
                this.width = nexOverlayItem.this.mOverLayImage.getWidth();
                this.height = nexOverlayItem.this.mOverLayImage.getHeight();
                this.drawPosition = nexOverlayItem.getDrawBitmapPosition(nexOverlayItem.this.mOverLayImage.getAnchorPoint(), this.width, this.height);
            } else {
                this.width = nexOverlayItem.this.mOverLayFilter.getWidth();
                this.height = nexOverlayItem.this.mOverLayFilter.getHeight();
                int i2 = this.width;
                int i3 = this.height;
                this.drawPosition = new Rect((-i2) / 2, (-i3) / 2, i2 / 2, i3 / 2);
            }
            this.maskOn = false;
            if (nexOverlayItem.this.mMask != null && nexOverlayItem.this.mMask.onOff) {
                nexOverlayItem.this.mMask.getPosition(this.mask);
            }
            List<nexAnimate> list = nexOverlayItem.this.mAniList;
            if (list != null) {
                for (nexAnimate nexanimate : list) {
                    int i4 = this.time - nexOverlayItem.this.mStartTime;
                    if (nexanimate.mStartTime <= i4 && nexanimate.getEndTime() > i4) {
                        if (nexanimate instanceof nexAnimate.Move) {
                            if (nexOverlayItem.this.mLayerExpression.getID() == 0) {
                                this.x += nexanimate.getTranslatePosition(i4, 1);
                                this.y += nexanimate.getTranslatePosition(i4, 2);
                            }
                        } else if (nexanimate instanceof nexAnimate.Rotate) {
                            if (nexOverlayItem.this.mLayerExpression.getID() == 0) {
                                this.angleX = nexanimate.getAngleDegree(i4, nexOverlayItem.this.mRotateDegreeX, 1);
                                this.angleY = nexanimate.getAngleDegree(i4, nexOverlayItem.this.mRotateDegreeY, 2);
                                this.angleZ = nexanimate.getAngleDegree(i4, nexOverlayItem.this.mRotateDegreeZ, 3);
                            }
                        } else if ((nexanimate instanceof nexAnimate.Scale) && nexOverlayItem.this.mLayerExpression.getID() == 0) {
                            this.scaleX = nexanimate.getScaledRatio(i4, nexOverlayItem.this.mScaledX, 1);
                            this.scaleY = nexanimate.getScaledRatio(i4, nexOverlayItem.this.mScaledY, 2);
                        }
                    }
                }
            }
        }
    }

    public BoundInfo getBoundInfo(int i) {
        return new BoundInfo(i);
    }

    /* loaded from: classes3.dex */
    public static class HitPoint {
        private int id;
        public int mTime;
        public float mViewHeight;
        public float mViewWidth;
        public float mViewX;
        public float mViewY;
        private int position;

        public int getID() {
            return this.id;
        }

        public int getHitInPosition() {
            return this.position;
        }
    }

    public boolean isPointInOverlayItem(HitPoint hitPoint) {
        float[] fArr = this.scratchPoint;
        Matrix matrix = this.scratchMatrix;
        BoundInfo boundInfo = getBoundInfo(hitPoint.mTime);
        matrix.reset();
        matrix.postScale(nexApplicationConfig.getAspectProfile().getWidth() / hitPoint.mViewWidth, nexApplicationConfig.getAspectProfile().getHeight() / hitPoint.mViewHeight);
        matrix.postTranslate(-boundInfo.x, -boundInfo.y);
        matrix.postScale(1.0f / boundInfo.scaleX, 1.0f / boundInfo.scaleY);
        matrix.postRotate(-boundInfo.angleZ, 0.0f, 0.0f);
        fArr[0] = hitPoint.mViewX;
        fArr[1] = hitPoint.mViewY;
        matrix.mapPoints(fArr);
        float f = fArr[0];
        float f2 = fArr[1];
        Rect rect = new Rect();
        boundInfo.getDrawBound(rect);
        float f3 = rect.left * boundInfo.scaleX;
        float f4 = rect.right * boundInfo.scaleX;
        float f5 = rect.top * boundInfo.scaleY;
        float f6 = rect.bottom * boundInfo.scaleY;
        Log.d(TAG, "new pos(" + f + "," + f2 + ") , Rect(" + f3 + "," + f5 + "," + f4 + "," + f6 + ") , handleRadius=36.0");
        int i = (f > (f3 - 36.0f) ? 1 : (f == (f3 - 36.0f) ? 0 : -1));
        if (i < 0 || f > f3 + 36.0f || f2 < f5 - 36.0f || f2 > f5 + 36.0f) {
            int i2 = (f > (f4 - 36.0f) ? 1 : (f == (f4 - 36.0f) ? 0 : -1));
            if (i2 >= 0 && f <= f4 + 36.0f && f2 >= f5 - 36.0f && f2 <= f5 + 36.0f) {
                hitPoint.position = 2;
            } else if (i >= 0 && f <= f3 + 36.0f && f2 >= f6 - 36.0f && f2 <= f6 + 36.0f) {
                hitPoint.position = 3;
            } else if (i2 >= 0 && f <= f4 + 36.0f && f2 >= f6 - 36.0f && f2 <= 36.0f + f6) {
                hitPoint.position = 4;
            } else if (f < f3 || f > f4 || f2 < f5 || f2 > f6) {
                hitPoint.id = 0;
                hitPoint.position = 0;
                return false;
            } else {
                hitPoint.position = 0;
            }
        } else {
            hitPoint.position = 1;
        }
        hitPoint.id = getId();
        return true;
    }

    public void showOutline(boolean z) {
        this.showOutLien = z;
    }

    public static void setOutLine() {
        solidOutlen = false;
        solidBlackBitmap = Bitmap.createBitmap(16, 16, Bitmap.Config.ARGB_8888);
        new Canvas(solidBlackBitmap).drawColor(-16777216);
        solidWhiteBitmap = Bitmap.createBitmap(16, 16, Bitmap.Config.ARGB_8888);
        new Canvas(solidWhiteBitmap).drawColor(-1);
        outLineIcon = new Bitmap[4];
    }

    public static void setOutlineType(boolean z) {
        solidOutlen = z;
    }

    public static void clearOutLine() {
        solidBlackBitmap = null;
        solidWhiteBitmap = null;
        outLineIcon = null;
    }

    public static boolean setOutLineIcon(Context context, int i, int i2) {
        Bitmap[] bitmapArr = outLineIcon;
        boolean z = false;
        if (bitmapArr != null && i > 0 && i <= 4) {
            z = true;
            if (i2 == 0) {
                bitmapArr[i - 1] = null;
            } else {
                bitmapArr[i - 1] = BitmapFactory.decodeResource(context.getResources(), i2);
            }
        }
        return z;
    }

    private static void renderOutLine(nexOverlayItem nexoverlayitem, LayerRenderer layerRenderer) {
        Rect rect;
        int i;
        float f;
        float f2;
        if (solidBlackBitmap == null || solidWhiteBitmap == null) {
            return;
        }
        BoundInfo boundInfo = nexoverlayitem.getBoundInfo(nexoverlayitem.mTime);
        layerRenderer.i();
        layerRenderer.a(boundInfo.x, boundInfo.y);
        layerRenderer.b(boundInfo.scaleX, boundInfo.scaleY);
        layerRenderer.a(boundInfo.angleZ, 0.0f, 0.0f, 1.0f);
        layerRenderer.i();
        layerRenderer.b(1.0f / boundInfo.scaleX, 1.0f / boundInfo.scaleY);
        boundInfo.getDrawBound(new Rect());
        float f3 = rect.left * boundInfo.scaleX;
        float f4 = rect.right * boundInfo.scaleX;
        float f5 = rect.top * boundInfo.scaleY;
        float f6 = rect.bottom * boundInfo.scaleY;
        if (solidOutlen) {
            layerRenderer.a(solidWhiteBitmap, f3, f5, f3 + marchingAnts_width, f6);
            layerRenderer.a(solidWhiteBitmap, f4 - marchingAnts_width, f5, f4, f6);
            layerRenderer.a(solidWhiteBitmap, f3, f5, f4, f5 + marchingAnts_width);
            layerRenderer.a(solidWhiteBitmap, f3, f6 - marchingAnts_width, f4, f6);
        } else {
            layerRenderer.a(solidBlackBitmap, f3, f5, f3 + marchingAnts_width, f6);
            layerRenderer.a(solidBlackBitmap, f4 - marchingAnts_width, f5, f4, f6);
            layerRenderer.a(solidBlackBitmap, f3, f5, f4, f5 + marchingAnts_width);
            layerRenderer.a(solidBlackBitmap, f3, f6 - marchingAnts_width, f4, f6);
            float f7 = marchingAnts_dashSize * 2.0f;
            for (float f8 = f3 - (i * 2); f8 < f4; f8 += marchingAnts_dashSize * 2) {
                float max = Math.max(f3, f8 + f7);
                float min = Math.min(f4, max + marchingAnts_dashSize);
                if (min >= f3 && max <= f4) {
                    layerRenderer.a(solidWhiteBitmap, max, f5, min, f5 + marchingAnts_width);
                    layerRenderer.a(solidWhiteBitmap, max, f6 - marchingAnts_width, min, f6);
                }
            }
            for (float f9 = f5 - (marchingAnts_dashSize * 2); f9 < f6; f9 += marchingAnts_dashSize * 2) {
                float max2 = Math.max(f5, f9 + f7);
                float min2 = Math.min(f6, max2 + marchingAnts_dashSize);
                if (min2 >= f5 && max2 <= f6) {
                    layerRenderer.a(solidWhiteBitmap, f3, max2, f3 + marchingAnts_width, min2);
                    layerRenderer.a(solidWhiteBitmap, f4 - marchingAnts_width, max2, f4, min2);
                }
            }
        }
        layerRenderer.j();
        for (int i2 = 0; i2 < 4; i2++) {
            if (outLineIcon[i2] != null) {
                int i3 = i2 + 1;
                if (i3 == 1) {
                    f = f3;
                } else if (i3 != 2) {
                    if (i3 == 3) {
                        f = f3;
                    } else if (i3 != 4) {
                        f = 0.0f;
                        f2 = 0.0f;
                        layerRenderer.i();
                        layerRenderer.b(1.0f / boundInfo.scaleX, 1.0f / boundInfo.scaleY);
                        layerRenderer.a(outLineIcon[i2], f, f2);
                        layerRenderer.j();
                    } else {
                        f = f4;
                    }
                    f2 = f6;
                    layerRenderer.i();
                    layerRenderer.b(1.0f / boundInfo.scaleX, 1.0f / boundInfo.scaleY);
                    layerRenderer.a(outLineIcon[i2], f, f2);
                    layerRenderer.j();
                } else {
                    f = f4;
                }
                f2 = f5;
                layerRenderer.i();
                layerRenderer.b(1.0f / boundInfo.scaleX, 1.0f / boundInfo.scaleY);
                layerRenderer.a(outLineIcon[i2], f, f2);
                layerRenderer.j();
            }
        }
        layerRenderer.j();
    }

    public int getZOrder() {
        return this.mZOrder;
    }

    public void setZOrder(int i) {
        if (this.mZOrder != i) {
            this.mUpdated = true;
        }
        this.mZOrder = i;
    }

    public void onRenderAwake(LayerRenderer layerRenderer) {
        nexOverlayFilter nexoverlayfilter = this.mOverLayFilter;
        if (nexoverlayfilter != null) {
            try {
                OverlayAsset overlayAssetFilter = nexoverlayfilter.getOverlayAssetFilter();
                Rect rect = new Rect();
                this.mOverLayFilter.getBound(rect);
                this.awakeAsset = overlayAssetFilter.onAwake(layerRenderer, new RectF(rect), this.mOverLayFilter.getEncodedEffectOptions(), null);
                return;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            } catch (XmlPullParserException e2) {
                e2.printStackTrace();
                return;
            }
        }
        nexOverlayImage nexoverlayimage = this.mOverLayImage;
        if (nexoverlayimage == null || !nexoverlayimage.isAssetManager()) {
            return;
        }
        try {
            OverlayAsset overlayAssetBitmap = this.mOverLayImage.getOverlayAssetBitmap();
            Rect rect2 = new Rect();
            this.mOverLayImage.getBound(rect2);
            this.awakeAsset = overlayAssetBitmap.onAwake(layerRenderer, new RectF(rect2), null, null);
        } catch (IOException e3) {
            e3.printStackTrace();
        } catch (XmlPullParserException e4) {
            e4.printStackTrace();
        }
    }

    public void onRenderAsleep(LayerRenderer layerRenderer) {
        renderOverlay(layerRenderer, com.nexstreaming.kminternal.kinemaster.config.a.a().b());
        AwakeAsset awakeAsset = this.awakeAsset;
        if (awakeAsset != null) {
            awakeAsset.onAsleep(layerRenderer);
            this.awakeAsset = null;
        }
    }

    public void onRender(LayerRenderer layerRenderer) {
        nexOverlayFilter nexoverlayfilter = this.mOverLayFilter;
        if (nexoverlayfilter != null && nexoverlayfilter.isUpdated() && this.awakeAsset != null) {
            Rect rect = new Rect();
            this.mOverLayFilter.getBound(rect);
            this.awakeAsset.onRefresh(layerRenderer, new RectF(rect), this.mOverLayFilter.getEncodedEffectOptions());
        }
        renderOverlay(layerRenderer, com.nexstreaming.kminternal.kinemaster.config.a.a().b());
    }

    public boolean updated(boolean z) {
        boolean z2 = this.mUpdated;
        this.mUpdated = z;
        return z2;
    }

    public void flipVertical(boolean z) {
        if (z) {
            this.mFlipMode |= 1;
        } else {
            this.mFlipMode &= -2;
        }
    }

    public void flipHorizontal(boolean z) {
        if (z) {
            this.mFlipMode |= 2;
        } else {
            this.mFlipMode &= -3;
        }
    }

    public boolean isFlipVertical() {
        return (this.mFlipMode & 1) == 1;
    }

    public boolean isFlipHorizontal() {
        return (this.mFlipMode & 2) == 2;
    }

    public void setEnableShow(boolean z) {
        this.showItem = z;
    }

    public boolean getEnableShow() {
        return this.showItem;
    }
}
