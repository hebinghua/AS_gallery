package com.miui.gallery.movie.ui.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.miui.gallery.movie.core.MovieManager;
import com.miui.gallery.movie.entity.MovieInfo;
import com.miui.gallery.movie.ui.factory.MovieFactory;
import com.miui.gallery.movie.ui.view.BaseMovieView;

/* loaded from: classes2.dex */
public class MovieControllerView extends BaseMovieView {
    public boolean isLoopPlay;
    public int mCurrentTime;
    public boolean mDeleteVisible;
    public DeleteIconVisibleListener mDeleteVisibleListener;
    public boolean mIgnoreProgressHide;
    public boolean mIsSeekDisable;
    public MovieManager mMovieManager;
    public boolean mPlayProgressVisible;
    public ProgressChangeListener mProgressChangeListener;
    public boolean mShowDeleteMode;
    public MovieManager.StateChangeListener mStateChangeListener;

    /* loaded from: classes2.dex */
    public interface DeleteIconVisibleListener {
        void onChanged(boolean z);
    }

    /* loaded from: classes2.dex */
    public interface ProgressChangeListener {
        void onChanged(int i, int i2);

        void onPlaybackEOF();

        void onStateChanged(int i);
    }

    public MovieControllerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mStateChangeListener = new MovieManager.StateChangeListener() { // from class: com.miui.gallery.movie.ui.view.MovieControllerView.2
            @Override // com.miui.gallery.movie.core.MovieManager.StateChangeListener
            public void onStateChanged(int i) {
                MovieControllerView.this.mMovieManager.setState(i);
                if (i != 3) {
                    MovieControllerView.this.setDeleteVisible(false);
                }
                if (MovieControllerView.this.mProgressChangeListener != null) {
                    MovieControllerView.this.mProgressChangeListener.onStateChanged(i);
                }
                if (MovieControllerView.this.mIgnoreProgressHide) {
                    MovieControllerView.this.mIgnoreProgressHide = false;
                }
            }

            @Override // com.miui.gallery.movie.core.MovieManager.StateChangeListener
            public void onPlaybackEOF() {
                onPlaybackTimeChanged(0);
                if (MovieControllerView.this.isLoopPlay) {
                    MovieControllerView.this.mMovieManager.replay();
                } else {
                    MovieControllerView.this.mIgnoreProgressHide = true;
                    MovieControllerView.this.mMovieManager.seek(0);
                    MovieControllerView.this.mMovieManager.pause();
                }
                if (MovieControllerView.this.mProgressChangeListener != null) {
                    MovieControllerView.this.mProgressChangeListener.onPlaybackEOF();
                }
            }

            @Override // com.miui.gallery.movie.core.MovieManager.StateChangeListener
            public void onPlaybackTimeChanged(int i) {
                MovieControllerView.this.setCurrentPlayTime(i, i / MovieControllerView.this.mMovieManager.getTotalTime());
                if (MovieControllerView.this.mProgressChangeListener != null) {
                    MovieControllerView.this.mProgressChangeListener.onChanged(i, MovieControllerView.this.mMovieManager.getTotalTime());
                }
            }

            @Override // com.miui.gallery.movie.core.MovieManager.StateChangeListener
            public void onPlaybackPreloadingCompletion() {
                MovieControllerView.this.showLoadingView(false);
            }
        };
    }

    public void init(MovieManager movieManager) {
        this.mMovieManager = movieManager;
        init();
    }

    @Override // com.miui.gallery.movie.ui.view.BaseMovieView
    public void init() {
        if (this.mMovieManager == null) {
            this.mMovieManager = MovieFactory.createMovieManager(getContext());
        }
        super.init();
        setProgressChangeListener(new BaseMovieView.IProgressChangeListener() { // from class: com.miui.gallery.movie.ui.view.MovieControllerView.1
            public int tempCurrentTime;

            @Override // com.miui.gallery.movie.ui.view.BaseMovieView.IProgressChangeListener
            public void onVideoProgressChanging(int i, float f) {
                if (MovieControllerView.this.mIsSeekDisable) {
                    return;
                }
                this.tempCurrentTime = MovieControllerView.this.mCurrentTime;
                if (f <= 0.0f || MovieControllerView.this.mMovieManager == null) {
                    return;
                }
                int totalTime = (int) (MovieControllerView.this.mMovieManager.getTotalTime() * f);
                if (i == 0) {
                    if (MovieControllerView.this.mCurrentTime <= 0) {
                        return;
                    }
                    int i2 = this.tempCurrentTime;
                    this.tempCurrentTime = i2 > totalTime ? i2 - totalTime : 0;
                } else if (MovieControllerView.this.mCurrentTime >= MovieControllerView.this.mMovieManager.getTotalTime()) {
                    return;
                } else {
                    int i3 = this.tempCurrentTime + totalTime;
                    this.tempCurrentTime = i3;
                    if (i3 > MovieControllerView.this.mMovieManager.getTotalTime()) {
                        this.tempCurrentTime = MovieControllerView.this.mMovieManager.getTotalTime();
                    }
                }
                MovieControllerView.this.mMovieManager.seek(this.tempCurrentTime);
                MovieControllerView.this.updatePlayProgress(this.tempCurrentTime / MovieControllerView.this.mMovieManager.getTotalTime());
                MovieControllerView.this.setDeleteVisible(false);
                if (MovieControllerView.this.mProgressChangeListener == null) {
                    return;
                }
                MovieControllerView.this.mProgressChangeListener.onChanged(this.tempCurrentTime, MovieControllerView.this.mMovieManager.getTotalTime());
            }

            @Override // com.miui.gallery.movie.ui.view.BaseMovieView.IProgressChangeListener
            public void onVideoProgressChanged() {
                MovieControllerView.this.mCurrentTime = this.tempCurrentTime;
                if (MovieControllerView.this.mMovieManager != null) {
                    MovieControllerView.this.mMovieManager.pause();
                }
            }
        });
        this.mMovieManager.addStateChangeListener(this.mStateChangeListener);
    }

    public void setCurrentPlayTime(int i, float f) {
        this.mCurrentTime = i;
        updatePlayProgress(f);
    }

    @Override // com.miui.gallery.movie.ui.view.BaseMovieView
    public View createDisplayView() {
        return this.mMovieManager.getDisplayView();
    }

    public void destroy() {
        this.mMovieManager.removeStateChangeListener(this.mStateChangeListener);
        this.mMovieManager = null;
    }

    public void onResume() {
        MovieManager movieManager = this.mMovieManager;
        if (movieManager != null) {
            movieManager.onResume();
        }
    }

    public void onPause() {
        MovieManager movieManager = this.mMovieManager;
        if (movieManager != null) {
            movieManager.onPause();
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public MovieManager getMovieManager() {
        return this.mMovieManager;
    }

    public void setPlayProgressVisible(boolean z) {
        if (this.mPlayProgressVisible == z) {
            return;
        }
        this.mPlayProgressVisible = z;
        showPlayProgress(z);
    }

    public void setShowDeleteMode(boolean z) {
        if (this.mShowDeleteMode == z) {
            return;
        }
        this.mShowDeleteMode = z;
    }

    public void setProgressChangeListener(ProgressChangeListener progressChangeListener) {
        this.mProgressChangeListener = progressChangeListener;
    }

    public void setDeleteVisible(boolean z) {
        if (this.mDeleteVisible == z) {
            return;
        }
        DeleteIconVisibleListener deleteIconVisibleListener = this.mDeleteVisibleListener;
        if (deleteIconVisibleListener != null) {
            deleteIconVisibleListener.onChanged(z);
        }
        this.mDeleteVisible = z;
    }

    public void setDeleteVisibleListener(DeleteIconVisibleListener deleteIconVisibleListener) {
        this.mDeleteVisibleListener = deleteIconVisibleListener;
    }

    public void setMovieInfo(MovieInfo movieInfo) {
        MovieManager movieManager = this.mMovieManager;
        if (movieManager != null) {
            movieManager.setInfo(movieInfo);
        }
    }

    public void setLoopPlay(boolean z) {
        this.isLoopPlay = z;
    }

    public void setSeekDisable(boolean z) {
        this.mIsSeekDisable = z;
    }
}
