package com.miui.gallery.editor.photo.core.imports.miuibeauty;

import com.miui.filtersdk.beauty.BeautyParameterType;
import com.miui.filtersdk.beauty.BeautyProcessorManager;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class MiuiBeautyManager {
    public static MiuiBeautyEffect[] getBeautyEffects() {
        BeautyParameterType[] supportedBeautyParamTypes = BeautyProcessorManager.INSTANCE.getBeautyProcessor().getSupportedBeautyParamTypes();
        int length = supportedBeautyParamTypes.length;
        for (BeautyParameterType beautyParameterType : supportedBeautyParamTypes) {
            if (BeautyParameterType.RUDDY_STRENGTH == beautyParameterType || BeautyParameterType.IRIS_SHINE_RATIO == beautyParameterType) {
                length--;
            }
        }
        MiuiBeautyEffect[] miuiBeautyEffectArr = null;
        if (length > 0) {
            miuiBeautyEffectArr = new MiuiBeautyEffect[length];
            for (int i = 0; i < supportedBeautyParamTypes.length; i++) {
                switch (AnonymousClass1.$SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[supportedBeautyParamTypes[i].ordinal()]) {
                    case 1:
                        miuiBeautyEffectArr[0] = new MiuiBeautyEffect(GalleryApp.sGetAndroidContext().getString(R.string.photo_editor_miui_beauty_menu_face_thin), 1, supportedBeautyParamTypes[i]);
                        break;
                    case 2:
                        miuiBeautyEffectArr[1] = new MiuiBeautyEffect(GalleryApp.sGetAndroidContext().getString(R.string.photo_editor_miui_beauty_menu_skin_white), 2, supportedBeautyParamTypes[i]);
                        break;
                    case 3:
                        miuiBeautyEffectArr[2] = new MiuiBeautyEffect(GalleryApp.sGetAndroidContext().getString(R.string.photo_editor_miui_beauty_menu_smooth), 3, supportedBeautyParamTypes[i]);
                        break;
                    case 4:
                        miuiBeautyEffectArr[3] = new MiuiBeautyEffect(GalleryApp.sGetAndroidContext().getString(R.string.photo_editor_miui_beauty_menu_eye_large), 4, supportedBeautyParamTypes[i]);
                        break;
                    case 5:
                        miuiBeautyEffectArr[4] = new MiuiBeautyEffect(GalleryApp.sGetAndroidContext().getString(R.string.photo_editor_miui_beauty_menu_eye_bright), 5, supportedBeautyParamTypes[i]);
                        break;
                    case 6:
                        miuiBeautyEffectArr[5] = new MiuiBeautyEffect(GalleryApp.sGetAndroidContext().getString(R.string.photo_editor_miui_beauty_menu_eraser_pouch), 6, supportedBeautyParamTypes[i]);
                        break;
                    case 7:
                        miuiBeautyEffectArr[6] = new MiuiBeautyEffect(GalleryApp.sGetAndroidContext().getString(R.string.photo_editor_miui_beauty_menu_eraser_blemish), 7, supportedBeautyParamTypes[i]);
                        break;
                    case 8:
                        miuiBeautyEffectArr[7] = new MiuiBeautyEffect(GalleryApp.sGetAndroidContext().getString(R.string.photo_editor_miui_beauty_menu_relighting), 8, supportedBeautyParamTypes[i]);
                        break;
                    case 9:
                        miuiBeautyEffectArr[8] = new MiuiBeautyEffect(GalleryApp.sGetAndroidContext().getString(R.string.photo_editor_miui_beauty_menu_nose_thin), 9, supportedBeautyParamTypes[i]);
                        break;
                    case 10:
                        miuiBeautyEffectArr[9] = new MiuiBeautyEffect(GalleryApp.sGetAndroidContext().getString(R.string.photo_editor_miui_beauty_menu_lip_beauty), 10, supportedBeautyParamTypes[i]);
                        break;
                }
            }
        }
        return miuiBeautyEffectArr;
    }

    /* renamed from: com.miui.gallery.editor.photo.core.imports.miuibeauty.MiuiBeautyManager$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType;

        static {
            int[] iArr = new int[BeautyParameterType.values().length];
            $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType = iArr;
            try {
                iArr[BeautyParameterType.SHRINK_FACE_RATIO.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[BeautyParameterType.WHITEN_STRENGTH.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[BeautyParameterType.SMOOTH_STRENGTH.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[BeautyParameterType.ENLARGE_EYE_RATIO.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[BeautyParameterType.BRIGHT_EYE_RATIO.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[BeautyParameterType.DEPOUCH_RATIO.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[BeautyParameterType.DEBLEMISH.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[BeautyParameterType.RELIGHTING_RATIO.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[BeautyParameterType.SHRINK_NOSE_RATIO.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$miui$filtersdk$beauty$BeautyParameterType[BeautyParameterType.LIP_BEAUTY_RATIO.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
        }
    }
}
