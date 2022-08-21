package com.miui.gallery.editor.photo.utils;

import android.animation.Animator;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.miui.gallery.R;
import com.miui.gallery.editor.utils.EditorOrientationHelper;
import com.miui.gallery.util.SystemUiUtil;
import com.miui.gallery.widget.ViewUtils;
import com.miui.gallery.widget.imageview.BitmapGestureParamsHolder;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes2.dex */
public class EditorMiscHelper {
    public static WeakReference<ImageView> sPhotoViewRef;

    public static void configProtectiveArea(Context context, BitmapGestureParamsHolder bitmapGestureParamsHolder) {
        if (EditorOrientationHelper.isLayoutPortrait(context)) {
            int dimensionPixelSize = SystemUiUtil.isWaterFallScreen() ? context.getResources().getDimensionPixelSize(R.dimen.editor_waterfall_screen_horizontal_protect_size) : 0;
            bitmapGestureParamsHolder.setDisplayInitOffset(dimensionPixelSize, 0, dimensionPixelSize, 0);
            return;
        }
        int dimensionPixelSize2 = context.getResources().getDimensionPixelSize(R.dimen.photo_editor_landscape_vertical_protect_size);
        bitmapGestureParamsHolder.setDisplayInitOffset(0, dimensionPixelSize2, 0, dimensionPixelSize2);
    }

    public static void configProtectiveArea(View view) {
        ViewGroup.MarginLayoutParams marginLayoutParams;
        if (view == null || view.getContext() == null || (marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams()) == null) {
            return;
        }
        if (EditorOrientationHelper.isLayoutPortrait(view.getContext())) {
            int dimensionPixelSize = SystemUiUtil.isWaterFallScreen() ? view.getResources().getDimensionPixelSize(R.dimen.editor_waterfall_screen_horizontal_protect_size) : 0;
            marginLayoutParams.setMarginStart(dimensionPixelSize);
            marginLayoutParams.setMarginEnd(dimensionPixelSize);
            return;
        }
        int dimensionPixelSize2 = view.getResources().getDimensionPixelSize(R.dimen.photo_editor_landscape_vertical_protect_size);
        marginLayoutParams.topMargin = dimensionPixelSize2;
        marginLayoutParams.bottomMargin = dimensionPixelSize2;
    }

    public static void enterImmersive(View... viewArr) {
        enterImmersive(Arrays.asList(viewArr));
    }

    public static void exitImmersive(View... viewArr) {
        exitImmersive(Arrays.asList(viewArr));
    }

    public static void enterImmersive(List<View> list) {
        ViewUtils.showOrHideView(200, false, list, null);
    }

    public static void exitImmersive(List<View> list) {
        ViewUtils.showOrHideView(200, true, list, null);
    }

    public static void enterImmersive(List<View> list, Animator.AnimatorListener animatorListener) {
        ViewUtils.showOrHideView(200, false, list, animatorListener);
    }

    public static void exitImmersive(List<View> list, Animator.AnimatorListener animatorListener) {
        ViewUtils.showOrHideView(200, true, list, animatorListener);
    }

    public static void setPhotoView(ImageView imageView) {
        sPhotoViewRef = new WeakReference<>(imageView);
    }

    public static ImageView getPhotoView() {
        WeakReference<ImageView> weakReference = sPhotoViewRef;
        if (weakReference == null) {
            return null;
        }
        return weakReference.get();
    }
}
