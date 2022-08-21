package com.miui.gallery.editor.photo.core.common.fragment;

import com.miui.gallery.editor.photo.app.crop.AutoCropData;
import com.miui.gallery.editor.photo.core.RenderFragment;
import com.miui.gallery.editor.photo.core.common.model.CropData;

/* loaded from: classes2.dex */
public abstract class AbstractCropFragment extends RenderFragment {
    public OnCropStateChangedListener mCropChangedListener;
    public boolean mHasChanged;

    /* loaded from: classes2.dex */
    public interface OnCropStateChangedListener {
        void changeRotationState(boolean z);

        void onAutoCropFinished();

        void onCropped();

        void onRestored();
    }

    public abstract void finishTuning();

    public abstract void hideGuideLines();

    public abstract void onAutoCrop(AutoCropData autoCropData);

    public abstract void onDoMirror();

    public abstract void onDoRotate();

    public abstract void onSetAspectRatio(CropData.AspectRatio aspectRatio);

    public abstract void onTurning(float f);

    public abstract void prepareTuning();

    public void setAspectRatio(CropData.AspectRatio aspectRatio) {
        onChanged();
        onSetAspectRatio(aspectRatio);
    }

    public void doMirror() {
        onChanged();
        onDoMirror();
    }

    public void doRotate() {
        onChanged();
        onDoRotate();
    }

    public void tuning(float f) {
        onChanged();
        onTurning(f);
    }

    public void autoCrop(AutoCropData autoCropData) {
        onAutoCrop(autoCropData);
    }

    public void onChanged() {
        if (!this.mHasChanged) {
            this.mHasChanged = true;
            notifyCropped();
        }
    }

    public void restore() {
        if (this.mHasChanged) {
            this.mHasChanged = false;
            notifyRestored();
        }
    }

    public void changeRotationState(boolean z) {
        OnCropStateChangedListener onCropStateChangedListener = this.mCropChangedListener;
        if (onCropStateChangedListener != null) {
            onCropStateChangedListener.changeRotationState(z);
        }
    }

    public final void notifyCropped() {
        OnCropStateChangedListener onCropStateChangedListener = this.mCropChangedListener;
        if (onCropStateChangedListener != null) {
            onCropStateChangedListener.onCropped();
        }
    }

    public final void notifyRestored() {
        OnCropStateChangedListener onCropStateChangedListener = this.mCropChangedListener;
        if (onCropStateChangedListener != null) {
            onCropStateChangedListener.onRestored();
        }
    }

    public final void notifyAutoCropFinished() {
        OnCropStateChangedListener onCropStateChangedListener = this.mCropChangedListener;
        if (onCropStateChangedListener != null) {
            onCropStateChangedListener.onAutoCropFinished();
        }
    }

    public void setOnCropChangedListener(OnCropStateChangedListener onCropStateChangedListener) {
        this.mCropChangedListener = onCropStateChangedListener;
    }
}
