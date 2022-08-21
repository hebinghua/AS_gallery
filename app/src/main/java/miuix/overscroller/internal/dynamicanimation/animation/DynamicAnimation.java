package miuix.overscroller.internal.dynamicanimation.animation;

import android.os.Looper;
import android.util.AndroidRuntimeException;
import android.view.View;
import com.baidu.platform.comapi.map.MapBundleKey;
import java.util.ArrayList;
import miuix.overscroller.internal.dynamicanimation.animation.AnimationHandler;
import miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation;

/* loaded from: classes3.dex */
public abstract class DynamicAnimation<T extends DynamicAnimation<T>> implements AnimationHandler.AnimationFrameCallback {
    public boolean mManualAnim;
    public final FloatPropertyCompat mProperty;
    public static final ViewProperty TRANSLATION_X = new ViewProperty("translationX") { // from class: miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation.1
        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setTranslationX(f);
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(View view) {
            return view.getTranslationX();
        }
    };
    public static final ViewProperty TRANSLATION_Y = new ViewProperty("translationY") { // from class: miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation.2
        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setTranslationY(f);
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(View view) {
            return view.getTranslationY();
        }
    };
    public static final ViewProperty TRANSLATION_Z = new ViewProperty("translationZ") { // from class: miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation.3
        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setTranslationZ(f);
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(View view) {
            return view.getTranslationZ();
        }
    };
    public static final ViewProperty SCALE_X = new ViewProperty("scaleX") { // from class: miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation.4
        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setScaleX(f);
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(View view) {
            return view.getScaleX();
        }
    };
    public static final ViewProperty SCALE_Y = new ViewProperty("scaleY") { // from class: miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation.5
        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setScaleY(f);
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(View view) {
            return view.getScaleY();
        }
    };
    public static final ViewProperty ROTATION = new ViewProperty(MapBundleKey.MapObjKey.OBJ_SS_ARROW_ROTATION) { // from class: miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation.6
        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setRotation(f);
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(View view) {
            return view.getRotation();
        }
    };
    public static final ViewProperty ROTATION_X = new ViewProperty("rotationX") { // from class: miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation.7
        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setRotationX(f);
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(View view) {
            return view.getRotationX();
        }
    };
    public static final ViewProperty ROTATION_Y = new ViewProperty("rotationY") { // from class: miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation.8
        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setRotationY(f);
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(View view) {
            return view.getRotationY();
        }
    };
    public static final ViewProperty X = new ViewProperty("x") { // from class: miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation.9
        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setX(f);
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(View view) {
            return view.getX();
        }
    };
    public static final ViewProperty Y = new ViewProperty("y") { // from class: miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation.10
        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setY(f);
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(View view) {
            return view.getY();
        }
    };
    public static final ViewProperty Z = new ViewProperty(MapBundleKey.MapObjKey.OBJ_SS_ARROW_Z) { // from class: miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation.11
        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setZ(f);
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(View view) {
            return view.getZ();
        }
    };
    public static final ViewProperty ALPHA = new ViewProperty("alpha") { // from class: miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation.12
        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setAlpha(f);
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(View view) {
            return view.getAlpha();
        }
    };
    public static final ViewProperty SCROLL_X = new ViewProperty("scrollX") { // from class: miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation.13
        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setScrollX((int) f);
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(View view) {
            return view.getScrollX();
        }
    };
    public static final ViewProperty SCROLL_Y = new ViewProperty("scrollY") { // from class: miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation.14
        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public void setValue(View view, float f) {
            view.setScrollY((int) f);
        }

        @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
        public float getValue(View view) {
            return view.getScrollY();
        }
    };
    public float mVelocity = 0.0f;
    public float mValue = Float.MAX_VALUE;
    public boolean mStartValueIsSet = false;
    public boolean mRunning = false;
    public float mMaxValue = Float.MAX_VALUE;
    public float mMinValue = -Float.MAX_VALUE;
    public long mLastFrameTime = 0;
    public final ArrayList<OnAnimationEndListener> mEndListeners = new ArrayList<>();
    public final ArrayList<OnAnimationUpdateListener> mUpdateListeners = new ArrayList<>();
    public final Object mTarget = null;
    public float mMinVisibleChange = 1.0f;

    /* loaded from: classes3.dex */
    public static class MassState {
        public float mValue;
        public float mVelocity;
    }

    /* loaded from: classes3.dex */
    public interface OnAnimationEndListener {
        void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2);
    }

    /* loaded from: classes3.dex */
    public interface OnAnimationUpdateListener {
        void onAnimationUpdate(DynamicAnimation dynamicAnimation, float f, float f2);
    }

    public abstract void setValueThreshold(float f);

    public abstract boolean updateValueAndVelocity(long j);

    /* loaded from: classes3.dex */
    public static abstract class ViewProperty extends FloatPropertyCompat<View> {
        public ViewProperty(String str) {
            super(str);
        }
    }

    public DynamicAnimation(final FloatValueHolder floatValueHolder) {
        this.mProperty = new FloatPropertyCompat("FloatValueHolder") { // from class: miuix.overscroller.internal.dynamicanimation.animation.DynamicAnimation.15
            @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
            public float getValue(Object obj) {
                return floatValueHolder.getValue();
            }

            @Override // miuix.overscroller.internal.dynamicanimation.animation.FloatPropertyCompat
            public void setValue(Object obj, float f) {
                floatValueHolder.setValue(f);
            }
        };
    }

    public T setStartValue(float f) {
        this.mValue = f;
        this.mStartValueIsSet = true;
        return this;
    }

    public T setStartVelocity(float f) {
        this.mVelocity = f;
        return this;
    }

    public T setMaxValue(float f) {
        this.mMaxValue = f;
        return this;
    }

    public T setMinValue(float f) {
        this.mMinValue = f;
        return this;
    }

    public T addUpdateListener(OnAnimationUpdateListener onAnimationUpdateListener) {
        if (isRunning()) {
            throw new UnsupportedOperationException("Error: Update listeners must be added beforethe animation.");
        }
        if (!this.mUpdateListeners.contains(onAnimationUpdateListener)) {
            this.mUpdateListeners.add(onAnimationUpdateListener);
        }
        return this;
    }

    public void removeUpdateListener(OnAnimationUpdateListener onAnimationUpdateListener) {
        removeEntry(this.mUpdateListeners, onAnimationUpdateListener);
    }

    public T setMinimumVisibleChange(float f) {
        if (f <= 0.0f) {
            throw new IllegalArgumentException("Minimum visible change must be positive.");
        }
        this.mMinVisibleChange = f;
        setValueThreshold(f * 0.75f);
        return this;
    }

    public static <T> void removeNullEntries(ArrayList<T> arrayList) {
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            if (arrayList.get(size) == null) {
                arrayList.remove(size);
            }
        }
    }

    public static <T> void removeEntry(ArrayList<T> arrayList, T t) {
        int indexOf = arrayList.indexOf(t);
        if (indexOf >= 0) {
            arrayList.set(indexOf, null);
        }
    }

    public void start(boolean z) {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new AndroidRuntimeException("Animations may only be started on the main thread");
        }
        if (this.mRunning) {
            return;
        }
        startAnimationInternal(z);
    }

    public void cancel() {
        if (Looper.myLooper() != Looper.getMainLooper()) {
            throw new AndroidRuntimeException("Animations may only be canceled on the main thread");
        }
        if (!this.mRunning) {
            return;
        }
        endAnimationInternal(true);
    }

    public boolean isRunning() {
        return this.mRunning;
    }

    public final void startAnimationInternal(boolean z) {
        if (!this.mRunning) {
            this.mManualAnim = z;
            this.mRunning = true;
            if (!this.mStartValueIsSet) {
                this.mValue = getPropertyValue();
            }
            float f = this.mValue;
            if (f <= this.mMaxValue && f >= this.mMinValue) {
                if (z) {
                    return;
                }
                AnimationHandler.getInstance().addAnimationFrameCallback(this, 0L);
                return;
            }
            throw new IllegalArgumentException("Starting value(" + this.mValue + ") need to be in between min value(" + this.mMinValue + ") and max value(" + this.mMaxValue + ")");
        }
    }

    @Override // miuix.overscroller.internal.dynamicanimation.animation.AnimationHandler.AnimationFrameCallback
    public boolean doAnimationFrame(long j) {
        long j2 = this.mLastFrameTime;
        if (j2 == 0) {
            this.mLastFrameTime = j;
            setPropertyValue(this.mValue);
            return false;
        }
        this.mLastFrameTime = j;
        boolean updateValueAndVelocity = updateValueAndVelocity(j - j2);
        float min = Math.min(this.mValue, this.mMaxValue);
        this.mValue = min;
        float max = Math.max(min, this.mMinValue);
        this.mValue = max;
        setPropertyValue(max);
        if (updateValueAndVelocity) {
            endAnimationInternal(false);
        }
        return updateValueAndVelocity;
    }

    public final void endAnimationInternal(boolean z) {
        this.mRunning = false;
        if (!this.mManualAnim) {
            AnimationHandler.getInstance().removeCallback(this);
        }
        this.mManualAnim = false;
        this.mLastFrameTime = 0L;
        this.mStartValueIsSet = false;
        for (int i = 0; i < this.mEndListeners.size(); i++) {
            if (this.mEndListeners.get(i) != null) {
                this.mEndListeners.get(i).onAnimationEnd(this, z, this.mValue, this.mVelocity);
            }
        }
        removeNullEntries(this.mEndListeners);
    }

    public void setPropertyValue(float f) {
        this.mProperty.setValue(this.mTarget, f);
        for (int i = 0; i < this.mUpdateListeners.size(); i++) {
            if (this.mUpdateListeners.get(i) != null) {
                this.mUpdateListeners.get(i).onAnimationUpdate(this, this.mValue, this.mVelocity);
            }
        }
        removeNullEntries(this.mUpdateListeners);
    }

    public float getValueThreshold() {
        return this.mMinVisibleChange * 0.75f;
    }

    public final float getPropertyValue() {
        return this.mProperty.getValue(this.mTarget);
    }
}
