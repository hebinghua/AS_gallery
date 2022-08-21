package com.miui.gallery.movie.core;

import android.view.View;

/* loaded from: classes2.dex */
public abstract class MovieManager<T> implements IMovieManager<T>, IMovieController {
    public View mDisplayView;
    public int mState;

    /* loaded from: classes2.dex */
    public interface EncodeStateInterface {
        void onEncodeEnd(boolean z, boolean z2, int i);

        void onEncodeProgress(int i);

        void onEncodeStart();
    }

    /* loaded from: classes2.dex */
    public interface StateChangeListener {
        void onPlaybackEOF();

        void onPlaybackPreloadingCompletion();

        void onPlaybackTimeChanged(int i);

        void onStateChanged(int i);
    }

    public abstract void addStateChangeListener(StateChangeListener stateChangeListener);

    public abstract void removeStateChangeListener(StateChangeListener stateChangeListener);

    public View getDisplayView() {
        return this.mDisplayView;
    }

    public void setState(int i) {
        this.mState = i;
    }

    public int getState() {
        return this.mState;
    }

    public void keepScreenOn(boolean z) {
        View view = this.mDisplayView;
        if (view != null) {
            view.setKeepScreenOn(z);
        }
    }

    public void onResume() {
        keepScreenOn(true);
    }

    public void onPause() {
        keepScreenOn(false);
    }

    public void destroy() {
        this.mDisplayView = null;
    }
}
