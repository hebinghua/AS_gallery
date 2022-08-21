package com.miui.gallery.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import miuix.springback.view.SpringBackLayout;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes2.dex */
public class ViewUtils {
    public static final boolean HAS_PICTURE_BITMAP = false;
    public static AnimatorSet sAnimatorSet = new AnimatorSet();
    public static PropertyValuesHolder sToOpaquePvh = PropertyValuesHolder.ofFloat(View.ALPHA, 0.0f, 1.0f);
    public static PropertyValuesHolder sToTransparentPvh = PropertyValuesHolder.ofFloat(View.ALPHA, 1.0f, 0.0f);

    public static /* synthetic */ void $r8$lambda$Glj1BtOP6Abufd4IHYXKDH1PYm0(boolean z, Animator animator) {
        lambda$showOrHideView$0(z, animator);
    }

    public static Bitmap captureSnapshot(View view) {
        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();
        if (measuredWidth <= 0 || measuredHeight <= 0) {
            return null;
        }
        return captureSnapshot(view, measuredWidth, measuredHeight);
    }

    public static Bitmap captureSnapshot(View view, int i, int i2) {
        if (HAS_PICTURE_BITMAP) {
            Picture picture = new Picture();
            view.draw(picture.beginRecording(i, i2));
            picture.endRecording();
            return Bitmap.createBitmap(picture);
        }
        Bitmap createBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(createBitmap));
        return createBitmap;
    }

    public static boolean isLayoutRtl(View view) {
        return ViewCompat.getLayoutDirection(view) == 1;
    }

    public static void requestParentDisallowInterceptTouchEvent(View view, boolean z) {
        for (ViewParent parent = view.getParent(); parent != null; parent = parent.getParent()) {
            if (parent instanceof SpringBackLayout) {
                ((SpringBackLayout) parent).internalRequestDisallowInterceptTouchEvent(z);
            }
        }
    }

    public static List<View> getAllChildViews(View view) {
        ArrayList arrayList = new ArrayList();
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View childAt = viewGroup.getChildAt(i);
                arrayList.add(childAt);
                arrayList.addAll(getAllChildViews(childAt));
            }
        }
        return arrayList;
    }

    public static void setViewLayoutParams(View view, int i, int i2) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams.height == i2 && layoutParams.width == i) {
            return;
        }
        layoutParams.width = i;
        layoutParams.height = i2;
        view.setLayoutParams(layoutParams);
    }

    public static void showOrHideView(int i, final boolean z, List<View> list, Animator.AnimatorListener animatorListener) {
        ArrayList arrayList = new ArrayList();
        for (View view : list) {
            if (view != null) {
                ObjectAnimator objectAnimator = new ObjectAnimator();
                objectAnimator.setTarget(view);
                arrayList.add(objectAnimator);
            }
        }
        if (arrayList.size() == 0) {
            return;
        }
        sAnimatorSet.playTogether(arrayList);
        sAnimatorSet.setInterpolator(new CubicEaseOutInterpolator());
        arrayList.forEach(new Consumer() { // from class: com.miui.gallery.widget.ViewUtils$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                ViewUtils.$r8$lambda$Glj1BtOP6Abufd4IHYXKDH1PYm0(z, (Animator) obj);
            }
        });
        sAnimatorSet.setDuration(i);
        if (animatorListener != null) {
            sAnimatorSet.removeAllListeners();
            sAnimatorSet.addListener(animatorListener);
        }
        sAnimatorSet.start();
    }

    public static /* synthetic */ void lambda$showOrHideView$0(boolean z, Animator animator) {
        ObjectAnimator objectAnimator = (ObjectAnimator) animator;
        PropertyValuesHolder[] propertyValuesHolderArr = new PropertyValuesHolder[1];
        propertyValuesHolderArr[0] = z ? sToOpaquePvh : sToTransparentPvh;
        objectAnimator.setValues(propertyValuesHolderArr);
    }

    public static void makeTextViewStartMarquee(TextView textView) {
        textView.setSelected(true);
        textView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        textView.setSingleLine(true);
        textView.setMarqueeRepeatLimit(-1);
    }
}
