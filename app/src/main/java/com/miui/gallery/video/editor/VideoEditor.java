package com.miui.gallery.video.editor;

import android.content.Context;
import android.graphics.Bitmap;
import com.miui.gallery.video.editor.widget.DisplayWrapper;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class VideoEditor {
    public DisplayWrapper mDisplayWrapper;
    public OnVideoThumbnailChangedListener mOnVideoThumbnailChangedListener;
    public boolean mIsInit = false;
    public int mState = -1;
    public LinkedList<StateChangeListener> mStateChangeListeners = new LinkedList<>();

    /* loaded from: classes2.dex */
    public interface EnocdeStateInterface {
        void onEncodeEnd(boolean z, int i, int i2);

        void onEncodeProgress(int i);

        void onEncodeStart();
    }

    /* loaded from: classes2.dex */
    public static class NotSupportVideoException extends Exception {
    }

    /* loaded from: classes2.dex */
    public interface OnCompletedListener {
        void onCompleted();
    }

    /* loaded from: classes2.dex */
    public interface OnGetVideoThumbnailListener {
        void onGetVideoThumbnailCompleted(List<VideoThumbnail> list);
    }

    /* loaded from: classes2.dex */
    public interface OnVideoThumbnailChangedListener {
        void onVideoThumbnailChanged();
    }

    /* loaded from: classes2.dex */
    public interface StateChangeListener {
        void onStateChanged(int i);

        void onTimeChanged(int i);
    }

    /* loaded from: classes2.dex */
    public interface TrimStateInterface extends EnocdeStateInterface {
        void onTrimEnd(String str);

        void onTrimProgress(int i);

        void onTrimStart();
    }

    public abstract void adjustBrightness(int i);

    public abstract void adjustContrast(int i);

    public abstract void adjustSaturation(int i);

    public abstract void adjustSharpness(int i);

    public abstract void adjustVignetteRange(int i);

    public abstract boolean apply(OnCompletedListener onCompletedListener);

    public abstract void autoTrim(int i, TrimStateInterface trimStateInterface);

    public abstract void cancelExport(OnCompletedListener onCompletedListener);

    public abstract void export(String str, EnocdeStateInterface enocdeStateInterface);

    public abstract float getCurrentDisplayRatio();

    public abstract int getProjectTotalTime();

    public abstract int getVideoFrames();

    public abstract String getVideoPath();

    public abstract int getVideoStartTime();

    public abstract int getVideoTotalTime();

    public abstract boolean hasEdit();

    public abstract boolean haveSavedEditState();

    public abstract boolean isSourceAudioEnable();

    public abstract boolean isSupportAutoTrim();

    public abstract boolean isTouchSeekBar();

    public abstract void load(String str, OnCompletedListener onCompletedListener);

    public abstract void pause();

    public abstract Bitmap pickThumbnail(int i);

    public abstract void play();

    public abstract void release();

    public abstract boolean resetProject(OnCompletedListener onCompletedListener);

    public abstract void restoreEditState();

    public abstract void resume();

    public abstract void saveEditState();

    public abstract void seek(int i, OnCompletedListener onCompletedListener);

    public abstract boolean setAutoWaterMark(String str, int i);

    public abstract void setBackgroundMusic(String str);

    public abstract void setFilter(Filter filter);

    public abstract boolean setSmartEffectTemplate(SmartEffect smartEffect);

    public abstract void setSourceAudioEnable(Boolean bool);

    public abstract void setTouchSeekBar(boolean z);

    public abstract void setTrimInfo(int i, int i2);

    public abstract void setVideoEditorAdjust(boolean z);

    public abstract boolean setWarterMark(int i, String str);

    public abstract void startPreview();

    public abstract void toThirdEditor(Context context);

    public boolean isInit() {
        return this.mIsInit;
    }

    public static VideoEditor create(Context context, String str) {
        if ("nex".equals(str)) {
            return new NexVideoEditor(context);
        }
        return null;
    }

    public void setOnVideoThumbnailChangedListener(OnVideoThumbnailChangedListener onVideoThumbnailChangedListener) {
        this.mOnVideoThumbnailChangedListener = onVideoThumbnailChangedListener;
    }

    public void notifyThumbnailChanged() {
        OnVideoThumbnailChangedListener onVideoThumbnailChangedListener = this.mOnVideoThumbnailChangedListener;
        if (onVideoThumbnailChangedListener != null) {
            onVideoThumbnailChangedListener.onVideoThumbnailChanged();
        }
    }

    public int getState() {
        return this.mState;
    }

    public void setState(int i) {
        if (this.mState != i) {
            this.mState = i;
            Iterator<StateChangeListener> it = this.mStateChangeListeners.iterator();
            while (it.hasNext()) {
                it.next().onStateChanged(this.mState);
            }
        }
    }

    public void onTimeChanged(int i) {
        Iterator<StateChangeListener> it = this.mStateChangeListeners.iterator();
        while (it.hasNext()) {
            it.next().onTimeChanged(i);
        }
    }

    public void addStateChangeListener(StateChangeListener stateChangeListener) {
        if (!this.mStateChangeListeners.contains(stateChangeListener)) {
            this.mStateChangeListeners.add(stateChangeListener);
        }
    }

    public void removeStateChangeListener(StateChangeListener stateChangeListener) {
        if (this.mStateChangeListeners.contains(stateChangeListener)) {
            this.mStateChangeListeners.remove(stateChangeListener);
        }
    }

    public void setDisplayWrapper(DisplayWrapper displayWrapper) {
        this.mDisplayWrapper = displayWrapper;
    }

    public DisplayWrapper getDisplayWrapper() {
        return this.mDisplayWrapper;
    }

    public void autoTrim(TrimStateInterface trimStateInterface) {
        autoTrim(25000, trimStateInterface);
    }
}
