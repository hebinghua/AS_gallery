package miuix.animation.property;

import android.os.Build;
import android.view.View;
import ch.qos.logback.core.CoreConstants;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import miuix.animation.R$id;

/* loaded from: classes3.dex */
public abstract class ViewProperty extends FloatProperty<View> {
    public static final ViewProperty TRANSLATION_X = new ViewProperty("translationX") { // from class: miuix.animation.property.ViewProperty.1
        @Override // miuix.animation.property.FloatProperty
        public void setValue(View view, float f) {
            view.setTranslationX(f);
        }

        @Override // miuix.animation.property.FloatProperty
        public float getValue(View view) {
            return view.getTranslationX();
        }
    };
    public static final ViewProperty TRANSLATION_Y = new ViewProperty("translationY") { // from class: miuix.animation.property.ViewProperty.2
        @Override // miuix.animation.property.FloatProperty
        public void setValue(View view, float f) {
            view.setTranslationY(f);
        }

        @Override // miuix.animation.property.FloatProperty
        public float getValue(View view) {
            return view.getTranslationY();
        }
    };
    public static final ViewProperty TRANSLATION_Z = new ViewProperty("translationZ") { // from class: miuix.animation.property.ViewProperty.3
        @Override // miuix.animation.property.FloatProperty
        public void setValue(View view, float f) {
            if (Build.VERSION.SDK_INT >= 21) {
                view.setTranslationZ(f);
            }
        }

        @Override // miuix.animation.property.FloatProperty
        public float getValue(View view) {
            if (Build.VERSION.SDK_INT >= 21) {
                return view.getTranslationZ();
            }
            return 0.0f;
        }
    };
    public static final ViewProperty SCALE_X = new ViewProperty("scaleX") { // from class: miuix.animation.property.ViewProperty.4
        @Override // miuix.animation.property.FloatProperty
        public void setValue(View view, float f) {
            view.setScaleX(f);
        }

        @Override // miuix.animation.property.FloatProperty
        public float getValue(View view) {
            return view.getScaleX();
        }
    };
    public static final ViewProperty SCALE_Y = new ViewProperty("scaleY") { // from class: miuix.animation.property.ViewProperty.5
        @Override // miuix.animation.property.FloatProperty
        public void setValue(View view, float f) {
            view.setScaleY(f);
        }

        @Override // miuix.animation.property.FloatProperty
        public float getValue(View view) {
            return view.getScaleY();
        }
    };
    public static final ViewProperty ROTATION = new ViewProperty(MapBundleKey.MapObjKey.OBJ_SS_ARROW_ROTATION) { // from class: miuix.animation.property.ViewProperty.6
        @Override // miuix.animation.property.FloatProperty
        public void setValue(View view, float f) {
            view.setRotation(f);
        }

        @Override // miuix.animation.property.FloatProperty
        public float getValue(View view) {
            return view.getRotation();
        }
    };
    public static final ViewProperty ROTATION_X = new ViewProperty("rotationX") { // from class: miuix.animation.property.ViewProperty.7
        @Override // miuix.animation.property.FloatProperty
        public void setValue(View view, float f) {
            view.setRotationX(f);
        }

        @Override // miuix.animation.property.FloatProperty
        public float getValue(View view) {
            return view.getRotationX();
        }
    };
    public static final ViewProperty ROTATION_Y = new ViewProperty("rotationY") { // from class: miuix.animation.property.ViewProperty.8
        @Override // miuix.animation.property.FloatProperty
        public void setValue(View view, float f) {
            view.setRotationY(f);
        }

        @Override // miuix.animation.property.FloatProperty
        public float getValue(View view) {
            return view.getRotationY();
        }
    };
    public static final ViewProperty X = new ViewProperty("x") { // from class: miuix.animation.property.ViewProperty.9
        @Override // miuix.animation.property.FloatProperty
        public void setValue(View view, float f) {
            view.setX(f);
        }

        @Override // miuix.animation.property.FloatProperty
        public float getValue(View view) {
            return view.getX();
        }
    };
    public static final ViewProperty Y = new ViewProperty("y") { // from class: miuix.animation.property.ViewProperty.10
        @Override // miuix.animation.property.FloatProperty
        public void setValue(View view, float f) {
            view.setY(f);
        }

        @Override // miuix.animation.property.FloatProperty
        public float getValue(View view) {
            return view.getY();
        }
    };
    public static final ViewProperty Z = new ViewProperty(MapBundleKey.MapObjKey.OBJ_SS_ARROW_Z) { // from class: miuix.animation.property.ViewProperty.11
        @Override // miuix.animation.property.FloatProperty
        public void setValue(View view, float f) {
            if (Build.VERSION.SDK_INT >= 21) {
                view.setZ(f);
            }
        }

        @Override // miuix.animation.property.FloatProperty
        public float getValue(View view) {
            if (Build.VERSION.SDK_INT >= 21) {
                return view.getZ();
            }
            return 0.0f;
        }
    };
    public static final ViewProperty HEIGHT = new ViewProperty(nexExportFormat.TAG_FORMAT_HEIGHT) { // from class: miuix.animation.property.ViewProperty.12
        @Override // miuix.animation.property.FloatProperty
        public void setValue(View view, float f) {
            view.getLayoutParams().height = (int) f;
            view.setTag(R$id.miuix_animation_tag_set_height, Float.valueOf(f));
            view.requestLayout();
        }

        @Override // miuix.animation.property.FloatProperty
        public float getValue(View view) {
            int height = view.getHeight();
            Float f = (Float) view.getTag(R$id.miuix_animation_tag_set_height);
            if (f == null) {
                if (height == 0 && ViewProperty.isInInitLayout(view)) {
                    height = view.getMeasuredHeight();
                }
                return height;
            }
            return f.floatValue();
        }
    };
    public static final ViewProperty WIDTH = new ViewProperty(nexExportFormat.TAG_FORMAT_WIDTH) { // from class: miuix.animation.property.ViewProperty.13
        @Override // miuix.animation.property.FloatProperty
        public void setValue(View view, float f) {
            view.getLayoutParams().width = (int) f;
            view.setTag(R$id.miuix_animation_tag_set_width, Float.valueOf(f));
            view.requestLayout();
        }

        @Override // miuix.animation.property.FloatProperty
        public float getValue(View view) {
            int width = view.getWidth();
            Float f = (Float) view.getTag(R$id.miuix_animation_tag_set_width);
            if (f == null) {
                if (width == 0 && ViewProperty.isInInitLayout(view)) {
                    width = view.getMeasuredWidth();
                }
                return width;
            }
            return f.floatValue();
        }
    };
    public static final ViewProperty ALPHA = new ViewProperty("alpha") { // from class: miuix.animation.property.ViewProperty.14
        @Override // miuix.animation.property.FloatProperty
        public void setValue(View view, float f) {
            view.setAlpha(f);
        }

        @Override // miuix.animation.property.FloatProperty
        public float getValue(View view) {
            return view.getAlpha();
        }
    };
    public static final ViewProperty AUTO_ALPHA = new ViewProperty("autoAlpha") { // from class: miuix.animation.property.ViewProperty.15
        @Override // miuix.animation.property.FloatProperty
        public void setValue(View view, float f) {
            view.setAlpha(f);
            boolean z = Math.abs(f) <= 0.00390625f;
            if (view.getVisibility() != 0 && f > 0.0f && !z) {
                view.setVisibility(0);
            } else if (!z) {
            } else {
                view.setVisibility(8);
            }
        }

        @Override // miuix.animation.property.FloatProperty
        public float getValue(View view) {
            return view.getAlpha();
        }
    };
    public static final ViewProperty SCROLL_X = new ViewProperty("scrollX") { // from class: miuix.animation.property.ViewProperty.16
        @Override // miuix.animation.property.FloatProperty
        public void setValue(View view, float f) {
            view.setScrollX((int) f);
        }

        @Override // miuix.animation.property.FloatProperty
        public float getValue(View view) {
            return view.getScrollX();
        }
    };
    public static final ViewProperty SCROLL_Y = new ViewProperty("scrollY") { // from class: miuix.animation.property.ViewProperty.17
        @Override // miuix.animation.property.FloatProperty
        public void setValue(View view, float f) {
            view.setScrollY((int) f);
        }

        @Override // miuix.animation.property.FloatProperty
        public float getValue(View view) {
            return view.getScrollY();
        }
    };
    public static final ViewProperty FOREGROUND = new ViewProperty("deprecated_foreground") { // from class: miuix.animation.property.ViewProperty.18
        @Override // miuix.animation.property.FloatProperty
        public float getValue(View view) {
            return 0.0f;
        }

        @Override // miuix.animation.property.FloatProperty
        public void setValue(View view, float f) {
        }
    };
    public static final ViewProperty BACKGROUND = new ViewProperty("deprecated_background") { // from class: miuix.animation.property.ViewProperty.19
        @Override // miuix.animation.property.FloatProperty
        public float getValue(View view) {
            return 0.0f;
        }

        @Override // miuix.animation.property.FloatProperty
        public void setValue(View view, float f) {
        }
    };

    public ViewProperty(String str) {
        super(str);
    }

    @Override // miuix.animation.property.FloatProperty
    public String toString() {
        return "ViewProperty{mPropertyName='" + this.mPropertyName + CoreConstants.SINGLE_QUOTE_CHAR + '}';
    }

    public static boolean isInInitLayout(View view) {
        return view.getTag(R$id.miuix_animation_tag_init_layout) != null;
    }
}
