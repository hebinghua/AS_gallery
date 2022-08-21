package com.miui.filtersdk.beauty;

import android.graphics.Bitmap;
import com.miui.filtersdk.BeautyArcsoftJni;
import java.util.Map;

/* loaded from: classes.dex */
public class ArcsoftBeautyProcessor extends IntelligentBeautyProcessor {
    public int[] mBeautyParameters = new int[11];
    public int mBrightEyeRatio;
    public int mDeblemish;
    public int mDepouchRatio;
    public int mEnlargeEyeRatio;
    public int mIrisShineRatio;
    public int mLipBeautyRatio;
    public int mRelightingRatio;
    public int mRuddyRatio;
    public int mShrinkFaceRatio;
    public int mShrinkNooseRatio;
    public int mSmoothStrength;
    public int mWhiteStrength;

    @Override // com.miui.filtersdk.beauty.BeautyProcessor
    public void init(int i, int i2) {
    }

    public ArcsoftBeautyProcessor() {
        for (int i = 0; i < 11; i++) {
            this.mBeautyParameters[i] = 0;
        }
        setExtraSpan(50.0f);
        this.mLevelParameters = new float[][]{new float[]{0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f}, new float[]{10.0f, 20.0f, 10.0f, 30.0f, 15.0f, 10.0f, 1.0f, 70.0f, 0.0f, 25.0f, 10.0f, 0.0f}, new float[]{10.0f, 28.0f, 10.0f, 30.0f, 15.0f, 10.0f, 1.0f, 70.0f, 0.0f, 25.0f, 10.0f, 0.0f}, new float[]{10.0f, 35.0f, 10.0f, 30.0f, 15.0f, 10.0f, 1.0f, 70.0f, 0.0f, 25.0f, 10.0f, 0.0f}, new float[]{10.0f, 42.0f, 10.0f, 30.0f, 15.0f, 15.0f, 1.0f, 70.0f, 0.0f, 25.0f, 10.0f, 0.0f}, new float[]{10.0f, 50.0f, 10.0f, 30.0f, 15.0f, 15.0f, 1.0f, 70.0f, 0.0f, 25.0f, 10.0f, 0.0f}};
    }

    @Override // com.miui.filtersdk.beauty.BeautyProcessor
    public BeautyParameterType[] getSupportedBeautyParamTypes() {
        return new BeautyParameterType[]{BeautyParameterType.WHITEN_STRENGTH, BeautyParameterType.SMOOTH_STRENGTH, BeautyParameterType.ENLARGE_EYE_RATIO, BeautyParameterType.SHRINK_FACE_RATIO, BeautyParameterType.BRIGHT_EYE_RATIO, BeautyParameterType.IRIS_SHINE_RATIO, BeautyParameterType.DEBLEMISH, BeautyParameterType.DEPOUCH_RATIO, BeautyParameterType.RELIGHTING_RATIO, BeautyParameterType.LIP_BEAUTY_RATIO, BeautyParameterType.RUDDY_STRENGTH, BeautyParameterType.SHRINK_NOSE_RATIO};
    }

    /* renamed from: com.miui.filtersdk.beauty.ArcsoftBeautyProcessor$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType;

        static {
            int[] iArr = new int[BeautyParameterType.values().length];
            $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType = iArr;
            try {
                iArr[BeautyParameterType.ENLARGE_EYE_RATIO.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[BeautyParameterType.SHRINK_FACE_RATIO.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[BeautyParameterType.WHITEN_STRENGTH.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[BeautyParameterType.SMOOTH_STRENGTH.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[BeautyParameterType.BRIGHT_EYE_RATIO.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[BeautyParameterType.RUDDY_STRENGTH.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[BeautyParameterType.RELIGHTING_RATIO.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[BeautyParameterType.LIP_BEAUTY_RATIO.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[BeautyParameterType.DEPOUCH_RATIO.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[BeautyParameterType.IRIS_SHINE_RATIO.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[BeautyParameterType.SHRINK_NOSE_RATIO.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[BeautyParameterType.DEBLEMISH.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
        }
    }

    @Override // com.miui.filtersdk.beauty.BeautyProcessor
    public float[] getSupportedParamRange(BeautyParameterType beautyParameterType) {
        switch (AnonymousClass1.$SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[beautyParameterType.ordinal()]) {
            case 1:
                return new float[]{0.0f, 40.0f};
            case 2:
                return new float[]{0.0f, 100.0f};
            case 3:
                return new float[]{0.0f, 70.0f};
            case 4:
                return new float[]{0.0f, 70.0f};
            case 5:
                return new float[]{0.0f, 40.0f};
            case 6:
                return new float[]{0.0f, 80.0f};
            case 7:
                return new float[]{0.0f, 60.0f};
            case 8:
                return new float[]{0.0f, 100.0f};
            case 9:
                return new float[]{0.0f, 100.0f};
            case 10:
                return new float[]{0.0f, 40.0f};
            case 11:
                return new float[]{0.0f, 100.0f};
            case 12:
                return new float[]{0.0f, 1.0f};
            default:
                return new float[0];
        }
    }

    @Override // com.miui.filtersdk.beauty.BeautyProcessor
    public void setBeautyParamsDegree(Map<BeautyParameterType, Float> map) {
        for (Map.Entry<BeautyParameterType, Float> entry : map.entrySet()) {
            setBeautyParamDegree(entry.getKey(), entry.getValue());
        }
    }

    public void setBeautyParamDegree(BeautyParameterType beautyParameterType, Float f) {
        switch (AnonymousClass1.$SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[beautyParameterType.ordinal()]) {
            case 1:
                this.mEnlargeEyeRatio = Math.round(f.floatValue());
                return;
            case 2:
                this.mShrinkFaceRatio = Math.round(f.floatValue());
                return;
            case 3:
                this.mWhiteStrength = Math.round(f.floatValue());
                return;
            case 4:
                this.mSmoothStrength = Math.round(f.floatValue());
                return;
            case 5:
                this.mBrightEyeRatio = Math.round(f.floatValue());
                return;
            case 6:
                this.mRuddyRatio = Math.round(f.floatValue());
                return;
            case 7:
                this.mRelightingRatio = Math.round(f.floatValue());
                return;
            case 8:
                this.mLipBeautyRatio = Math.round(f.floatValue());
                return;
            case 9:
                this.mDepouchRatio = Math.round(f.floatValue());
                return;
            case 10:
                this.mIrisShineRatio = Math.round(f.floatValue());
                return;
            case 11:
                this.mShrinkNooseRatio = Math.round(f.floatValue());
                return;
            case 12:
                this.mDeblemish = Math.round(f.floatValue());
                return;
            default:
                return;
        }
    }

    @Override // com.miui.filtersdk.beauty.BeautyProcessor
    public void beautify(byte[] bArr, int i, int i2, int i3, byte[] bArr2, int i4) {
        if (isParametersEmpty()) {
            return;
        }
        BeautyArcsoftJni.beautifyProcess(bArr, i2, i3, this.mWhiteStrength, this.mSmoothStrength, this.mEnlargeEyeRatio, this.mShrinkFaceRatio, this.mBrightEyeRatio, this.mDeblemish, this.mDepouchRatio, this.mIrisShineRatio, this.mLipBeautyRatio, this.mRelightingRatio, this.mRuddyRatio, this.mShrinkNooseRatio);
    }

    @Override // com.miui.filtersdk.beauty.BeautyProcessor
    public void beautify(Bitmap bitmap, int i, int i2) {
        if (isParametersEmpty()) {
            return;
        }
        BeautyArcsoftJni.beautifyProcessBitmap(bitmap, i, i2, this.mWhiteStrength, this.mSmoothStrength, this.mEnlargeEyeRatio, this.mShrinkFaceRatio, this.mBrightEyeRatio, this.mDeblemish, this.mDepouchRatio, this.mIrisShineRatio, this.mLipBeautyRatio, this.mRelightingRatio, this.mRuddyRatio, this.mShrinkNooseRatio);
    }

    public final boolean isParametersEmpty() {
        return this.mBrightEyeRatio == 0 && this.mSmoothStrength == 0 && this.mWhiteStrength == 0 && this.mShrinkFaceRatio == 0 && this.mEnlargeEyeRatio == 0 && this.mDeblemish == 0 && this.mDepouchRatio == 0 && this.mIrisShineRatio == 0 && this.mLipBeautyRatio == 0 && this.mRelightingRatio == 0 && this.mRuddyRatio == 0 && this.mShrinkNooseRatio == 0;
    }

    @Override // com.miui.filtersdk.beauty.IntelligentBeautyProcessor
    public void clearBeautyParameters() {
        this.mBrightEyeRatio = 0;
        this.mSmoothStrength = 0;
        this.mWhiteStrength = 0;
        this.mShrinkFaceRatio = 0;
        this.mEnlargeEyeRatio = 0;
        this.mIrisShineRatio = 0;
        this.mDeblemish = 0;
        this.mDepouchRatio = 0;
        this.mRelightingRatio = 0;
        this.mLipBeautyRatio = 0;
        this.mRuddyRatio = 0;
        this.mShrinkNooseRatio = 0;
    }
}
