package com.miui.gallery.editor.photo.core.imports.miuibeauty;

import android.graphics.Bitmap;
import com.miui.arcsoftbeauty.ArcsoftBeautyJni;
import com.miui.gallery.editor.photo.core.RenderData;
import com.miui.gallery.editor.photo.core.RenderEngine;
import com.miui.gallery.editor.photo.core.imports.miuibeauty.MiuiBeautyRenderData;
import java.util.List;

/* loaded from: classes2.dex */
public class MiuiBeautyEngine extends RenderEngine {
    @Override // com.miui.gallery.editor.photo.core.RenderEngine
    public Bitmap render(Bitmap bitmap, RenderData renderData, boolean z) {
        if (renderData instanceof MiuiBeautyRenderData) {
            Bitmap preProcessBitmap = preProcessBitmap(bitmap);
            MiuiBeautyRenderData miuiBeautyRenderData = (MiuiBeautyRenderData) renderData;
            List<MiuiBeautyRenderData.BeautyParams> list = miuiBeautyRenderData.mBeautyParamsList;
            if (list != null && list.size() > 0) {
                int size = miuiBeautyRenderData.mBeautyParamsList.size();
                int[] iArr = new int[size];
                int[] iArr2 = new int[size];
                int[] iArr3 = new int[size];
                int[] iArr4 = new int[size];
                int[] iArr5 = new int[size];
                int[] iArr6 = new int[size];
                int[] iArr7 = new int[size];
                int[] iArr8 = new int[size];
                int[] iArr9 = new int[size];
                int[] iArr10 = new int[size];
                int[] iArr11 = new int[size];
                int[] iArr12 = new int[size];
                int i = 0;
                while (i < size) {
                    int i2 = size;
                    MiuiBeautyRenderData.BeautyParams beautyParams = miuiBeautyRenderData.mBeautyParamsList.get(i);
                    iArr[i] = beautyParams.mWhite;
                    iArr2[i] = beautyParams.mSmooth;
                    iArr3[i] = beautyParams.mEyeLarge;
                    iArr4[i] = beautyParams.mFaceThin;
                    iArr5[i] = beautyParams.mBrightEyeRatio;
                    iArr6[i] = beautyParams.mDeblemish;
                    iArr7[i] = beautyParams.mDepouchRatio;
                    iArr8[i] = beautyParams.mRelightingRatio;
                    iArr9[i] = beautyParams.mIrisShineRatio;
                    iArr10[i] = beautyParams.mLipBeautyRatio;
                    iArr11[i] = beautyParams.mRuddyRatio;
                    iArr12[i] = beautyParams.mNoseThin;
                    i++;
                    size = i2;
                    miuiBeautyRenderData = miuiBeautyRenderData;
                }
                ArcsoftBeautyJni.beautifyProcessBitmap(preProcessBitmap, preProcessBitmap.getWidth(), preProcessBitmap.getHeight(), iArr, iArr2, iArr3, iArr4, iArr5, iArr6, iArr7, iArr9, iArr10, iArr8, iArr11, iArr12);
            }
            return preProcessBitmap;
        }
        return null;
    }

    public static Bitmap preProcessBitmap(Bitmap bitmap) {
        if (bitmap.getWidth() % 2 == 0) {
            return bitmap;
        }
        int width = bitmap.getWidth();
        if (width == 1) {
            return null;
        }
        int height = bitmap.getHeight();
        if (width % 2 != 0) {
            width--;
            height = (bitmap.getHeight() * width) / bitmap.getWidth();
        }
        return Bitmap.createBitmap(bitmap, 0, 0, width, height);
    }

    public static Bitmap preProcessBitmapForPreview(Bitmap bitmap) {
        int i = 4;
        if (bitmap.getWidth() % 4 == 0) {
            return bitmap;
        }
        int width = bitmap.getWidth();
        if (width == 1) {
            return null;
        }
        if (width >= 4) {
            i = width;
        }
        while (i % 4 != 0) {
            i--;
        }
        return Bitmap.createScaledBitmap(bitmap, i, (bitmap.getHeight() * i) / bitmap.getWidth(), true);
    }
}
