package com.airbnb.lottie;

import android.animation.Animator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.view.ViewCompat;
import com.airbnb.lottie.model.KeyPath;
import com.airbnb.lottie.utils.Logger;
import com.airbnb.lottie.utils.Utils;
import com.airbnb.lottie.value.LottieValueCallback;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

/* loaded from: classes.dex */
public class LottieAnimationView extends AppCompatImageView {
    public String animationName;
    public int animationResId;
    public boolean autoPlay;
    public int buildDrawingCacheDepth;
    public boolean cacheComposition;
    public LottieComposition composition;
    public LottieTask<LottieComposition> compositionTask;
    public LottieListener<Throwable> failureListener;
    public int fallbackResource;
    public boolean isInitialized;
    public final LottieListener<LottieComposition> loadedListener;
    public final LottieDrawable lottieDrawable;
    public Set<LottieOnCompositionLoadedListener> lottieOnCompositionLoadedListeners;
    public boolean playAnimationWhenShown;
    public RenderMode renderMode;
    public boolean wasAnimatingWhenDetached;
    public boolean wasAnimatingWhenNotShown;
    public final LottieListener<Throwable> wrappedFailureListener;
    public static final String TAG = LottieAnimationView.class.getSimpleName();
    public static final LottieListener<Throwable> DEFAULT_FAILURE_LISTENER = new LottieListener<Throwable>() { // from class: com.airbnb.lottie.LottieAnimationView.1
        @Override // com.airbnb.lottie.LottieListener
        public void onResult(Throwable th) {
            if (Utils.isNetworkException(th)) {
                Logger.warning("Unable to load composition.", th);
                return;
            }
            throw new IllegalStateException("Unable to parse composition", th);
        }
    };

    public LottieAnimationView(Context context) {
        super(context);
        this.loadedListener = new LottieListener<LottieComposition>() { // from class: com.airbnb.lottie.LottieAnimationView.2
            @Override // com.airbnb.lottie.LottieListener
            public void onResult(LottieComposition lottieComposition) {
                LottieAnimationView.this.setComposition(lottieComposition);
            }
        };
        this.wrappedFailureListener = new LottieListener<Throwable>() { // from class: com.airbnb.lottie.LottieAnimationView.3
            @Override // com.airbnb.lottie.LottieListener
            public void onResult(Throwable th) {
                if (LottieAnimationView.this.fallbackResource != 0) {
                    LottieAnimationView lottieAnimationView = LottieAnimationView.this;
                    lottieAnimationView.setImageResource(lottieAnimationView.fallbackResource);
                }
                (LottieAnimationView.this.failureListener == null ? LottieAnimationView.DEFAULT_FAILURE_LISTENER : LottieAnimationView.this.failureListener).onResult(th);
            }
        };
        this.fallbackResource = 0;
        this.lottieDrawable = new LottieDrawable();
        this.playAnimationWhenShown = false;
        this.wasAnimatingWhenNotShown = false;
        this.wasAnimatingWhenDetached = false;
        this.autoPlay = false;
        this.cacheComposition = true;
        this.renderMode = RenderMode.AUTOMATIC;
        this.lottieOnCompositionLoadedListeners = new HashSet();
        this.buildDrawingCacheDepth = 0;
        init(null);
    }

    public LottieAnimationView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.loadedListener = new LottieListener<LottieComposition>() { // from class: com.airbnb.lottie.LottieAnimationView.2
            @Override // com.airbnb.lottie.LottieListener
            public void onResult(LottieComposition lottieComposition) {
                LottieAnimationView.this.setComposition(lottieComposition);
            }
        };
        this.wrappedFailureListener = new LottieListener<Throwable>() { // from class: com.airbnb.lottie.LottieAnimationView.3
            @Override // com.airbnb.lottie.LottieListener
            public void onResult(Throwable th) {
                if (LottieAnimationView.this.fallbackResource != 0) {
                    LottieAnimationView lottieAnimationView = LottieAnimationView.this;
                    lottieAnimationView.setImageResource(lottieAnimationView.fallbackResource);
                }
                (LottieAnimationView.this.failureListener == null ? LottieAnimationView.DEFAULT_FAILURE_LISTENER : LottieAnimationView.this.failureListener).onResult(th);
            }
        };
        this.fallbackResource = 0;
        this.lottieDrawable = new LottieDrawable();
        this.playAnimationWhenShown = false;
        this.wasAnimatingWhenNotShown = false;
        this.wasAnimatingWhenDetached = false;
        this.autoPlay = false;
        this.cacheComposition = true;
        this.renderMode = RenderMode.AUTOMATIC;
        this.lottieOnCompositionLoadedListeners = new HashSet();
        this.buildDrawingCacheDepth = 0;
        init(attributeSet);
    }

    public final void init(AttributeSet attributeSet) {
        String string;
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.LottieAnimationView);
        boolean z = false;
        if (!isInEditMode()) {
            this.cacheComposition = obtainStyledAttributes.getBoolean(R$styleable.LottieAnimationView_lottie_cacheComposition, true);
            int i = R$styleable.LottieAnimationView_lottie_rawRes;
            boolean hasValue = obtainStyledAttributes.hasValue(i);
            int i2 = R$styleable.LottieAnimationView_lottie_fileName;
            boolean hasValue2 = obtainStyledAttributes.hasValue(i2);
            int i3 = R$styleable.LottieAnimationView_lottie_url;
            boolean hasValue3 = obtainStyledAttributes.hasValue(i3);
            if (hasValue && hasValue2) {
                throw new IllegalArgumentException("lottie_rawRes and lottie_fileName cannot be used at the same time. Please use only one at once.");
            }
            if (hasValue) {
                int resourceId = obtainStyledAttributes.getResourceId(i, 0);
                if (resourceId != 0) {
                    setAnimation(resourceId);
                }
            } else if (hasValue2) {
                String string2 = obtainStyledAttributes.getString(i2);
                if (string2 != null) {
                    setAnimation(string2);
                }
            } else if (hasValue3 && (string = obtainStyledAttributes.getString(i3)) != null) {
                setAnimationFromUrl(string);
            }
            setFallbackResource(obtainStyledAttributes.getResourceId(R$styleable.LottieAnimationView_lottie_fallbackRes, 0));
        }
        if (obtainStyledAttributes.getBoolean(R$styleable.LottieAnimationView_lottie_autoPlay, false)) {
            this.wasAnimatingWhenDetached = true;
            this.autoPlay = true;
        }
        if (obtainStyledAttributes.getBoolean(R$styleable.LottieAnimationView_lottie_loop, false)) {
            this.lottieDrawable.setRepeatCount(-1);
        }
        int i4 = R$styleable.LottieAnimationView_lottie_repeatMode;
        if (obtainStyledAttributes.hasValue(i4)) {
            setRepeatMode(obtainStyledAttributes.getInt(i4, 1));
        }
        int i5 = R$styleable.LottieAnimationView_lottie_repeatCount;
        if (obtainStyledAttributes.hasValue(i5)) {
            setRepeatCount(obtainStyledAttributes.getInt(i5, -1));
        }
        int i6 = R$styleable.LottieAnimationView_lottie_speed;
        if (obtainStyledAttributes.hasValue(i6)) {
            setSpeed(obtainStyledAttributes.getFloat(i6, 1.0f));
        }
        setImageAssetsFolder(obtainStyledAttributes.getString(R$styleable.LottieAnimationView_lottie_imageAssetsFolder));
        setProgress(obtainStyledAttributes.getFloat(R$styleable.LottieAnimationView_lottie_progress, 0.0f));
        enableMergePathsForKitKatAndAbove(obtainStyledAttributes.getBoolean(R$styleable.LottieAnimationView_lottie_enableMergePathsForKitKatAndAbove, false));
        int i7 = R$styleable.LottieAnimationView_lottie_colorFilter;
        if (obtainStyledAttributes.hasValue(i7)) {
            addValueCallback(new KeyPath("**"), LottieProperty.COLOR_FILTER, new LottieValueCallback(new SimpleColorFilter(obtainStyledAttributes.getColor(i7, 0))));
        }
        int i8 = R$styleable.LottieAnimationView_lottie_scale;
        if (obtainStyledAttributes.hasValue(i8)) {
            this.lottieDrawable.setScale(obtainStyledAttributes.getFloat(i8, 1.0f));
        }
        int i9 = R$styleable.LottieAnimationView_lottie_renderMode;
        if (obtainStyledAttributes.hasValue(i9)) {
            RenderMode renderMode = RenderMode.AUTOMATIC;
            int i10 = obtainStyledAttributes.getInt(i9, renderMode.ordinal());
            if (i10 >= RenderMode.values().length) {
                i10 = renderMode.ordinal();
            }
            setRenderMode(RenderMode.values()[i10]);
        }
        if (getScaleType() != null) {
            this.lottieDrawable.setScaleType(getScaleType());
        }
        obtainStyledAttributes.recycle();
        LottieDrawable lottieDrawable = this.lottieDrawable;
        if (Utils.getAnimationScale(getContext()) != 0.0f) {
            z = true;
        }
        lottieDrawable.setSystemAnimationsAreEnabled(Boolean.valueOf(z));
        enableOrDisableHardwareLayer();
        this.isInitialized = true;
    }

    @Override // androidx.appcompat.widget.AppCompatImageView, android.widget.ImageView
    public void setImageResource(int i) {
        cancelLoaderTask();
        super.setImageResource(i);
    }

    @Override // androidx.appcompat.widget.AppCompatImageView, android.widget.ImageView
    public void setImageDrawable(Drawable drawable) {
        cancelLoaderTask();
        super.setImageDrawable(drawable);
    }

    @Override // androidx.appcompat.widget.AppCompatImageView, android.widget.ImageView
    public void setImageBitmap(Bitmap bitmap) {
        cancelLoaderTask();
        super.setImageBitmap(bitmap);
    }

    @Override // android.widget.ImageView, android.view.View, android.graphics.drawable.Drawable.Callback
    public void invalidateDrawable(Drawable drawable) {
        Drawable drawable2 = getDrawable();
        LottieDrawable lottieDrawable = this.lottieDrawable;
        if (drawable2 == lottieDrawable) {
            super.invalidateDrawable(lottieDrawable);
        } else {
            super.invalidateDrawable(drawable);
        }
    }

    @Override // android.view.View
    public Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.animationName = this.animationName;
        savedState.animationResId = this.animationResId;
        savedState.progress = this.lottieDrawable.getProgress();
        savedState.isAnimating = this.lottieDrawable.isAnimating() || (!ViewCompat.isAttachedToWindow(this) && this.wasAnimatingWhenDetached);
        savedState.imageAssetsFolder = this.lottieDrawable.getImageAssetsFolder();
        savedState.repeatMode = this.lottieDrawable.getRepeatMode();
        savedState.repeatCount = this.lottieDrawable.getRepeatCount();
        return savedState;
    }

    @Override // android.view.View
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        String str = savedState.animationName;
        this.animationName = str;
        if (!TextUtils.isEmpty(str)) {
            setAnimation(this.animationName);
        }
        int i = savedState.animationResId;
        this.animationResId = i;
        if (i != 0) {
            setAnimation(i);
        }
        setProgress(savedState.progress);
        if (savedState.isAnimating) {
            playAnimation();
        }
        this.lottieDrawable.setImagesAssetsFolder(savedState.imageAssetsFolder);
        setRepeatMode(savedState.repeatMode);
        setRepeatCount(savedState.repeatCount);
    }

    @Override // android.view.View
    public void onVisibilityChanged(View view, int i) {
        if (!this.isInitialized) {
            return;
        }
        if (isShown()) {
            if (this.wasAnimatingWhenNotShown) {
                resumeAnimation();
            } else if (this.playAnimationWhenShown) {
                playAnimation();
            }
            this.wasAnimatingWhenNotShown = false;
            this.playAnimationWhenShown = false;
        } else if (!isAnimating()) {
        } else {
            pauseAnimation();
            this.wasAnimatingWhenNotShown = true;
        }
    }

    @Override // android.widget.ImageView, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.autoPlay || this.wasAnimatingWhenDetached) {
            playAnimation();
            this.autoPlay = false;
            this.wasAnimatingWhenDetached = false;
        }
        if (Build.VERSION.SDK_INT < 23) {
            onVisibilityChanged(this, getVisibility());
        }
    }

    @Override // android.widget.ImageView, android.view.View
    public void onDetachedFromWindow() {
        if (isAnimating()) {
            cancelAnimation();
            this.wasAnimatingWhenDetached = true;
        }
        super.onDetachedFromWindow();
    }

    public void enableMergePathsForKitKatAndAbove(boolean z) {
        this.lottieDrawable.enableMergePathsForKitKatAndAbove(z);
    }

    public void setCacheComposition(boolean z) {
        this.cacheComposition = z;
    }

    public void setAnimation(int i) {
        this.animationResId = i;
        this.animationName = null;
        setCompositionTask(this.cacheComposition ? LottieCompositionFactory.fromRawRes(getContext(), i) : LottieCompositionFactory.fromRawRes(getContext(), i, null));
    }

    public void setAnimation(String str) {
        this.animationName = str;
        this.animationResId = 0;
        setCompositionTask(this.cacheComposition ? LottieCompositionFactory.fromAsset(getContext(), str) : LottieCompositionFactory.fromAsset(getContext(), str, null));
    }

    @Deprecated
    public void setAnimationFromJson(String str) {
        setAnimationFromJson(str, null);
    }

    public void setAnimationFromJson(String str, String str2) {
        setAnimation(new ByteArrayInputStream(str.getBytes()), str2);
    }

    public void setAnimation(InputStream inputStream, String str) {
        setCompositionTask(LottieCompositionFactory.fromJsonInputStream(inputStream, str));
    }

    public void setAnimationFromUrl(String str) {
        setCompositionTask(this.cacheComposition ? LottieCompositionFactory.fromUrl(getContext(), str) : LottieCompositionFactory.fromUrl(getContext(), str, null));
    }

    public void setFailureListener(LottieListener<Throwable> lottieListener) {
        this.failureListener = lottieListener;
    }

    public void setFallbackResource(int i) {
        this.fallbackResource = i;
    }

    private void setCompositionTask(LottieTask<LottieComposition> lottieTask) {
        clearComposition();
        cancelLoaderTask();
        this.compositionTask = lottieTask.addListener(this.loadedListener).addFailureListener(this.wrappedFailureListener);
    }

    public final void cancelLoaderTask() {
        LottieTask<LottieComposition> lottieTask = this.compositionTask;
        if (lottieTask != null) {
            lottieTask.removeListener(this.loadedListener);
            this.compositionTask.removeFailureListener(this.wrappedFailureListener);
        }
    }

    public void setComposition(LottieComposition lottieComposition) {
        if (L.DBG) {
            String str = TAG;
            Log.v(str, "Set Composition \n" + lottieComposition);
        }
        this.lottieDrawable.setCallback(this);
        this.composition = lottieComposition;
        boolean composition = this.lottieDrawable.setComposition(lottieComposition);
        enableOrDisableHardwareLayer();
        if (getDrawable() != this.lottieDrawable || composition) {
            onVisibilityChanged(this, getVisibility());
            requestLayout();
            for (LottieOnCompositionLoadedListener lottieOnCompositionLoadedListener : this.lottieOnCompositionLoadedListeners) {
                lottieOnCompositionLoadedListener.onCompositionLoaded(lottieComposition);
            }
        }
    }

    public LottieComposition getComposition() {
        return this.composition;
    }

    public void playAnimation() {
        if (isShown()) {
            this.lottieDrawable.playAnimation();
            enableOrDisableHardwareLayer();
            return;
        }
        this.playAnimationWhenShown = true;
    }

    public void resumeAnimation() {
        if (isShown()) {
            this.lottieDrawable.resumeAnimation();
            enableOrDisableHardwareLayer();
            return;
        }
        this.playAnimationWhenShown = false;
        this.wasAnimatingWhenNotShown = true;
    }

    public void setMinFrame(int i) {
        this.lottieDrawable.setMinFrame(i);
    }

    public float getMinFrame() {
        return this.lottieDrawable.getMinFrame();
    }

    public void setMinProgress(float f) {
        this.lottieDrawable.setMinProgress(f);
    }

    public void setMaxFrame(int i) {
        this.lottieDrawable.setMaxFrame(i);
    }

    public float getMaxFrame() {
        return this.lottieDrawable.getMaxFrame();
    }

    public void setMaxProgress(float f) {
        this.lottieDrawable.setMaxProgress(f);
    }

    public void setMinFrame(String str) {
        this.lottieDrawable.setMinFrame(str);
    }

    public void setMaxFrame(String str) {
        this.lottieDrawable.setMaxFrame(str);
    }

    public void setMinAndMaxFrame(String str) {
        this.lottieDrawable.setMinAndMaxFrame(str);
    }

    public void setSpeed(float f) {
        this.lottieDrawable.setSpeed(f);
    }

    public float getSpeed() {
        return this.lottieDrawable.getSpeed();
    }

    public void addAnimatorListener(Animator.AnimatorListener animatorListener) {
        this.lottieDrawable.addAnimatorListener(animatorListener);
    }

    @Deprecated
    public void loop(boolean z) {
        this.lottieDrawable.setRepeatCount(z ? -1 : 0);
    }

    public void setRepeatMode(int i) {
        this.lottieDrawable.setRepeatMode(i);
    }

    public int getRepeatMode() {
        return this.lottieDrawable.getRepeatMode();
    }

    public void setRepeatCount(int i) {
        this.lottieDrawable.setRepeatCount(i);
    }

    public int getRepeatCount() {
        return this.lottieDrawable.getRepeatCount();
    }

    public boolean isAnimating() {
        return this.lottieDrawable.isAnimating();
    }

    public void setImageAssetsFolder(String str) {
        this.lottieDrawable.setImagesAssetsFolder(str);
    }

    public String getImageAssetsFolder() {
        return this.lottieDrawable.getImageAssetsFolder();
    }

    public void setImageAssetDelegate(ImageAssetDelegate imageAssetDelegate) {
        this.lottieDrawable.setImageAssetDelegate(imageAssetDelegate);
    }

    public void setFontAssetDelegate(FontAssetDelegate fontAssetDelegate) {
        this.lottieDrawable.setFontAssetDelegate(fontAssetDelegate);
    }

    public void setTextDelegate(TextDelegate textDelegate) {
        this.lottieDrawable.setTextDelegate(textDelegate);
    }

    public <T> void addValueCallback(KeyPath keyPath, T t, LottieValueCallback<T> lottieValueCallback) {
        this.lottieDrawable.addValueCallback(keyPath, t, lottieValueCallback);
    }

    public void setScale(float f) {
        this.lottieDrawable.setScale(f);
        if (getDrawable() == this.lottieDrawable) {
            setImageDrawable(null);
            setImageDrawable(this.lottieDrawable);
        }
    }

    public float getScale() {
        return this.lottieDrawable.getScale();
    }

    @Override // android.widget.ImageView
    public void setScaleType(ImageView.ScaleType scaleType) {
        super.setScaleType(scaleType);
        LottieDrawable lottieDrawable = this.lottieDrawable;
        if (lottieDrawable != null) {
            lottieDrawable.setScaleType(scaleType);
        }
    }

    public void cancelAnimation() {
        this.wasAnimatingWhenDetached = false;
        this.wasAnimatingWhenNotShown = false;
        this.playAnimationWhenShown = false;
        this.lottieDrawable.cancelAnimation();
        enableOrDisableHardwareLayer();
    }

    public void pauseAnimation() {
        this.autoPlay = false;
        this.wasAnimatingWhenDetached = false;
        this.wasAnimatingWhenNotShown = false;
        this.playAnimationWhenShown = false;
        this.lottieDrawable.pauseAnimation();
        enableOrDisableHardwareLayer();
    }

    public void setFrame(int i) {
        this.lottieDrawable.setFrame(i);
    }

    public int getFrame() {
        return this.lottieDrawable.getFrame();
    }

    public void setProgress(float f) {
        this.lottieDrawable.setProgress(f);
    }

    public float getProgress() {
        return this.lottieDrawable.getProgress();
    }

    public long getDuration() {
        LottieComposition lottieComposition = this.composition;
        if (lottieComposition != null) {
            return lottieComposition.getDuration();
        }
        return 0L;
    }

    public void setPerformanceTrackingEnabled(boolean z) {
        this.lottieDrawable.setPerformanceTrackingEnabled(z);
    }

    public PerformanceTracker getPerformanceTracker() {
        return this.lottieDrawable.getPerformanceTracker();
    }

    public final void clearComposition() {
        this.composition = null;
        this.lottieDrawable.clearComposition();
    }

    public void setSafeMode(boolean z) {
        this.lottieDrawable.setSafeMode(z);
    }

    @Override // android.view.View
    public void buildDrawingCache(boolean z) {
        L.beginSection("buildDrawingCache");
        this.buildDrawingCacheDepth++;
        super.buildDrawingCache(z);
        if (this.buildDrawingCacheDepth == 1 && getWidth() > 0 && getHeight() > 0 && getLayerType() == 1 && getDrawingCache(z) == null) {
            setRenderMode(RenderMode.HARDWARE);
        }
        this.buildDrawingCacheDepth--;
        L.endSection("buildDrawingCache");
    }

    public void setRenderMode(RenderMode renderMode) {
        this.renderMode = renderMode;
        enableOrDisableHardwareLayer();
    }

    public void setApplyingOpacityToLayersEnabled(boolean z) {
        this.lottieDrawable.setApplyingOpacityToLayersEnabled(z);
    }

    /* renamed from: com.airbnb.lottie.LottieAnimationView$5  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass5 {
        public static final /* synthetic */ int[] $SwitchMap$com$airbnb$lottie$RenderMode;

        static {
            int[] iArr = new int[RenderMode.values().length];
            $SwitchMap$com$airbnb$lottie$RenderMode = iArr;
            try {
                iArr[RenderMode.HARDWARE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$airbnb$lottie$RenderMode[RenderMode.SOFTWARE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$airbnb$lottie$RenderMode[RenderMode.AUTOMATIC.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:24:0x003b, code lost:
        if (r3 != false) goto L21;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void enableOrDisableHardwareLayer() {
        /*
            r5 = this;
            int[] r0 = com.airbnb.lottie.LottieAnimationView.AnonymousClass5.$SwitchMap$com$airbnb$lottie$RenderMode
            com.airbnb.lottie.RenderMode r1 = r5.renderMode
            int r1 = r1.ordinal()
            r0 = r0[r1]
            r1 = 2
            r2 = 1
            if (r0 == r2) goto L3d
            if (r0 == r1) goto L13
            r3 = 3
            if (r0 == r3) goto L15
        L13:
            r1 = r2
            goto L3d
        L15:
            com.airbnb.lottie.LottieComposition r0 = r5.composition
            r3 = 0
            if (r0 == 0) goto L27
            boolean r0 = r0.hasDashPattern()
            if (r0 == 0) goto L27
            int r0 = android.os.Build.VERSION.SDK_INT
            r4 = 28
            if (r0 >= r4) goto L27
            goto L3b
        L27:
            com.airbnb.lottie.LottieComposition r0 = r5.composition
            if (r0 == 0) goto L33
            int r0 = r0.getMaskAndMatteCount()
            r4 = 4
            if (r0 <= r4) goto L33
            goto L3b
        L33:
            int r0 = android.os.Build.VERSION.SDK_INT
            r4 = 21
            if (r0 >= r4) goto L3a
            goto L3b
        L3a:
            r3 = r2
        L3b:
            if (r3 == 0) goto L13
        L3d:
            int r0 = r5.getLayerType()
            if (r1 == r0) goto L47
            r0 = 0
            r5.setLayerType(r1, r0)
        L47:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.LottieAnimationView.enableOrDisableHardwareLayer():void");
    }

    /* loaded from: classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: com.airbnb.lottie.LottieAnimationView.SavedState.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public SavedState mo175createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public SavedState[] mo176newArray(int i) {
                return new SavedState[i];
            }
        };
        public String animationName;
        public int animationResId;
        public String imageAssetsFolder;
        public boolean isAnimating;
        public float progress;
        public int repeatCount;
        public int repeatMode;

        public SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public SavedState(Parcel parcel) {
            super(parcel);
            this.animationName = parcel.readString();
            this.progress = parcel.readFloat();
            this.isAnimating = parcel.readInt() != 1 ? false : true;
            this.imageAssetsFolder = parcel.readString();
            this.repeatMode = parcel.readInt();
            this.repeatCount = parcel.readInt();
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeString(this.animationName);
            parcel.writeFloat(this.progress);
            parcel.writeInt(this.isAnimating ? 1 : 0);
            parcel.writeString(this.imageAssetsFolder);
            parcel.writeInt(this.repeatMode);
            parcel.writeInt(this.repeatCount);
        }
    }
}
