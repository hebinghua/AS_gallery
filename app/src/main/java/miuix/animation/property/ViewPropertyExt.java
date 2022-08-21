package miuix.animation.property;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import miuix.animation.R$id;

/* loaded from: classes3.dex */
public class ViewPropertyExt {
    public static final ForegroundProperty FOREGROUND = new ForegroundProperty();
    public static final BackgroundProperty BACKGROUND = new BackgroundProperty();

    /* loaded from: classes3.dex */
    public static class ForegroundProperty extends ViewProperty implements IIntValueProperty<View> {
        @Override // miuix.animation.property.FloatProperty
        public float getValue(View view) {
            return 0.0f;
        }

        @Override // miuix.animation.property.FloatProperty
        public void setValue(View view, float f) {
        }

        public ForegroundProperty() {
            super("foreground");
        }

        @Override // miuix.animation.property.IIntValueProperty
        public int getIntValue(View view) {
            Object tag = view.getTag(R$id.miuix_animation_tag_foreground_color);
            if (tag instanceof Integer) {
                return ((Integer) tag).intValue();
            }
            return 0;
        }

        @Override // miuix.animation.property.IIntValueProperty
        public void setIntValue(View view, int i) {
            Drawable foreground;
            view.setTag(R$id.miuix_animation_tag_foreground_color, Integer.valueOf(i));
            if (Build.VERSION.SDK_INT < 23 || (foreground = view.getForeground()) == null) {
                return;
            }
            foreground.invalidateSelf();
        }
    }

    /* loaded from: classes3.dex */
    public static class BackgroundProperty extends ViewProperty implements IIntValueProperty<View> {
        @Override // miuix.animation.property.FloatProperty
        public float getValue(View view) {
            return 0.0f;
        }

        @Override // miuix.animation.property.FloatProperty
        public void setValue(View view, float f) {
        }

        public BackgroundProperty() {
            super("background");
        }

        @Override // miuix.animation.property.IIntValueProperty
        public void setIntValue(View view, int i) {
            view.setBackgroundColor(i);
        }

        @Override // miuix.animation.property.IIntValueProperty
        public int getIntValue(View view) {
            Drawable background = view.getBackground();
            if (background instanceof ColorDrawable) {
                return ((ColorDrawable) background).getColor();
            }
            return 0;
        }
    }
}
