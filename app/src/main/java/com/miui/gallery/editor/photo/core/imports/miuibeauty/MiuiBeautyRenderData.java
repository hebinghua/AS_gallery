package com.miui.gallery.editor.photo.core.imports.miuibeauty;

import com.miui.filtersdk.beauty.BeautyParameterType;
import com.miui.gallery.editor.photo.core.RenderData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class MiuiBeautyRenderData extends RenderData {
    public List<BeautyParams> mBeautyParamsList = new ArrayList();

    public MiuiBeautyRenderData(Map<BeautyParameterType, Float> map) {
        if (map != null) {
            this.mBeautyParamsList.add(new BeautyParams(map));
        }
    }

    public void addParams(Map<BeautyParameterType, Float> map) {
        this.mBeautyParamsList.add(new BeautyParams(map));
    }

    /* loaded from: classes2.dex */
    public static class BeautyParams {
        public int mBrightEyeRatio;
        public int mDeblemish;
        public int mDepouchRatio;
        public int mEyeLarge;
        public int mFaceThin;
        public int mIrisShineRatio;
        public int mLipBeautyRatio;
        public int mNoseThin;
        public int mRelightingRatio;
        public int mRuddyRatio;
        public int mSmooth;
        public int mWhite;

        public BeautyParams(Map<BeautyParameterType, Float> map) {
            for (Map.Entry<BeautyParameterType, Float> entry : map.entrySet()) {
                int round = Math.round(entry.getValue().floatValue());
                switch (AnonymousClass1.$SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[entry.getKey().ordinal()]) {
                    case 1:
                        this.mEyeLarge = round;
                        break;
                    case 2:
                        this.mFaceThin = round;
                        break;
                    case 3:
                        this.mWhite = round;
                        break;
                    case 4:
                        this.mSmooth = round;
                        break;
                    case 5:
                        this.mRuddyRatio = round;
                        break;
                    case 6:
                        this.mBrightEyeRatio = round;
                        break;
                    case 7:
                        this.mDeblemish = round;
                        break;
                    case 8:
                        this.mDepouchRatio = round;
                        break;
                    case 9:
                        this.mRelightingRatio = round;
                        break;
                    case 10:
                        this.mLipBeautyRatio = round;
                        break;
                    case 11:
                        this.mIrisShineRatio = round;
                        break;
                    case 12:
                        this.mNoseThin = round;
                        break;
                }
            }
        }
    }

    /* renamed from: com.miui.gallery.editor.photo.core.imports.miuibeauty.MiuiBeautyRenderData$1  reason: invalid class name */
    /* loaded from: classes2.dex */
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
                $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[BeautyParameterType.RUDDY_STRENGTH.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[BeautyParameterType.BRIGHT_EYE_RATIO.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[BeautyParameterType.DEBLEMISH.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[BeautyParameterType.DEPOUCH_RATIO.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[BeautyParameterType.RELIGHTING_RATIO.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[BeautyParameterType.LIP_BEAUTY_RATIO.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[BeautyParameterType.IRIS_SHINE_RATIO.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[BeautyParameterType.SHRINK_NOSE_RATIO.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
        }
    }
}
