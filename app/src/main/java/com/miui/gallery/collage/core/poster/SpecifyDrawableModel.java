package com.miui.gallery.collage.core.poster;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import androidx.annotation.Keep;
import com.miui.gallery.collage.drawable.ColorDrawable;
import com.miui.gallery.collage.drawable.RectTimeDrawable;
import com.miui.gallery.collage.drawable.TimeFormatDrawable;

@Keep
/* loaded from: classes.dex */
public class SpecifyDrawableModel extends ElementPositionModel {
    public Extras extras;
    public SpecifyDrawableType specifyDrawableType;

    @Keep
    /* loaded from: classes.dex */
    public static class Extras {
        public String color;
        public float letterSpace;
        public String textColor;
        public float textSize;
    }

    /* renamed from: com.miui.gallery.collage.core.poster.SpecifyDrawableModel$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$collage$core$poster$SpecifyDrawableModel$SpecifyDrawableType;

        static {
            int[] iArr = new int[SpecifyDrawableType.values().length];
            $SwitchMap$com$miui$gallery$collage$core$poster$SpecifyDrawableModel$SpecifyDrawableType = iArr;
            try {
                iArr[SpecifyDrawableType.TIME_FORMAT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$collage$core$poster$SpecifyDrawableModel$SpecifyDrawableType[SpecifyDrawableType.TIME_RECT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$collage$core$poster$SpecifyDrawableModel$SpecifyDrawableType[SpecifyDrawableType.COLOR.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    @Keep
    /* loaded from: classes.dex */
    public enum SpecifyDrawableType {
        COLOR,
        TIME_FORMAT,
        TIME_RECT;

        public Drawable getDrawable(Resources resources, SpecifyDrawableModel specifyDrawableModel) {
            Drawable timeFormatDrawable;
            int i = AnonymousClass1.$SwitchMap$com$miui$gallery$collage$core$poster$SpecifyDrawableModel$SpecifyDrawableType[ordinal()];
            if (i == 1) {
                timeFormatDrawable = new TimeFormatDrawable(resources, specifyDrawableModel);
            } else if (i != 2) {
                if (i == 3) {
                    return new ColorDrawable(specifyDrawableModel);
                }
                return null;
            } else {
                timeFormatDrawable = new RectTimeDrawable(resources, specifyDrawableModel);
            }
            return timeFormatDrawable;
        }
    }
}
