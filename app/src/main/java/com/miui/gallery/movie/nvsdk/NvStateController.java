package com.miui.gallery.movie.nvsdk;

import com.meicam.sdk.NvsRational;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.miui.gallery.movie.core.IMovieController;
import com.miui.gallery.movie.core.MovieManager;
import com.miui.gallery.util.logger.DefaultLogger;
import io.reactivex.android.schedulers.AndroidSchedulers;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/* loaded from: classes2.dex */
public class NvStateController implements IMovieController, NvsStreamingContext.PlaybackCallback, NvsStreamingContext.StreamingEngineCallback, NvsStreamingContext.PlaybackCallback2 {
    public NvsRational mNvsRational = new NvsRational(3, 4);
    public CopyOnWriteArrayList<MovieManager.StateChangeListener> mStateChangeListeners;
    public NvsStreamingContext mStreamingContext;
    public NvsTimeline mTimeline;

    public static /* synthetic */ void $r8$lambda$Wh3BvYdLOCggnle1fEZXDoA1I8g(NvStateController nvStateController) {
        nvStateController.lambda$onPlaybackEOF$0();
    }

    public final int getStateFromNvEngine(int i) {
        if (i != 0) {
            if (i == 3) {
                return 1;
            }
            if (i == 4) {
                return 3;
            }
            return i != 5 ? -500 : 200;
        }
        return 2;
    }

    @Override // com.meicam.sdk.NvsStreamingContext.PlaybackCallback
    public void onPlaybackPreloadingCompletion(NvsTimeline nvsTimeline) {
    }

    @Override // com.meicam.sdk.NvsStreamingContext.PlaybackCallback
    public void onPlaybackStopped(NvsTimeline nvsTimeline) {
    }

    public void onResume() {
    }

    public NvStateController(NvsStreamingContext nvsStreamingContext, NvsTimeline nvsTimeline) {
        this.mStreamingContext = nvsStreamingContext;
        this.mTimeline = nvsTimeline;
    }

    public void replay() {
        play(0L);
    }

    public void play(long j) {
        NvsStreamingContext nvsStreamingContext = this.mStreamingContext;
        NvsTimeline nvsTimeline = this.mTimeline;
        nvsStreamingContext.playbackTimeline(nvsTimeline, j, nvsTimeline.getDuration(), this.mNvsRational, true, 0);
    }

    @Override // com.miui.gallery.movie.core.IMovieController
    public void pause() {
        if (getCurrentEngineState() != 5) {
            this.mStreamingContext.stop(2);
        }
    }

    @Override // com.miui.gallery.movie.core.IMovieController
    public void resume() {
        if (getCurrentEngineState() != 3) {
            long timelineCurrentPosition = this.mStreamingContext.getTimelineCurrentPosition(this.mTimeline);
            NvsStreamingContext nvsStreamingContext = this.mStreamingContext;
            NvsTimeline nvsTimeline = this.mTimeline;
            nvsStreamingContext.playbackTimeline(nvsTimeline, timelineCurrentPosition, nvsTimeline.getDuration(), this.mNvsRational, true, 0);
        }
    }

    public void onPause() {
        pause();
    }

    public boolean isResume() {
        return getCurrentEngineState() == 4 || getCurrentEngineState() == 0;
    }

    @Override // com.miui.gallery.movie.core.IMovieController
    public void seek(int i) {
        seekLong(i * 1000);
    }

    public final void seekLong(long j) {
        this.mStreamingContext.seekTimeline(this.mTimeline, j, this.mNvsRational, 0);
    }

    @Override // com.miui.gallery.movie.core.IMovieController
    public void cancelExport() {
        DefaultLogger.d("NvStateController", "cancelExport");
        if (getCurrentEngineState() == 5) {
            this.mStreamingContext.stop(2);
        }
        DefaultLogger.d("NvStateController", "cancelExport done");
    }

    public void addStateChangeListener(MovieManager.StateChangeListener stateChangeListener) {
        if (stateChangeListener != null) {
            if (this.mStateChangeListeners == null) {
                this.mStateChangeListeners = new CopyOnWriteArrayList<>();
            }
            if (this.mStateChangeListeners.contains(stateChangeListener)) {
                return;
            }
            this.mStateChangeListeners.add(stateChangeListener);
        }
    }

    public void removeStateChangeListener(MovieManager.StateChangeListener stateChangeListener) {
        CopyOnWriteArrayList<MovieManager.StateChangeListener> copyOnWriteArrayList = this.mStateChangeListeners;
        if (copyOnWriteArrayList != null) {
            copyOnWriteArrayList.remove(stateChangeListener);
        }
    }

    @Override // com.meicam.sdk.NvsStreamingContext.PlaybackCallback
    public void onPlaybackEOF(NvsTimeline nvsTimeline) {
        AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() { // from class: com.miui.gallery.movie.nvsdk.NvStateController$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                NvStateController.$r8$lambda$Wh3BvYdLOCggnle1fEZXDoA1I8g(NvStateController.this);
            }
        });
    }

    public /* synthetic */ void lambda$onPlaybackEOF$0() {
        Iterator<MovieManager.StateChangeListener> it = this.mStateChangeListeners.iterator();
        while (it.hasNext()) {
            it.next().onPlaybackEOF();
        }
    }

    @Override // com.meicam.sdk.NvsStreamingContext.StreamingEngineCallback
    public void onStreamingEngineStateChanged(int i) {
        DefaultLogger.d("NvStateController", "state: %d", Integer.valueOf(i));
        int stateFromNvEngine = getStateFromNvEngine(i);
        Iterator<MovieManager.StateChangeListener> it = this.mStateChangeListeners.iterator();
        while (it.hasNext()) {
            it.next().onStateChanged(stateFromNvEngine);
        }
    }

    public final int getCurrentEngineState() {
        return this.mStreamingContext.getStreamingEngineState();
    }

    @Override // com.meicam.sdk.NvsStreamingContext.StreamingEngineCallback
    public void onFirstVideoFramePresented(NvsTimeline nvsTimeline) {
        Iterator<MovieManager.StateChangeListener> it = this.mStateChangeListeners.iterator();
        while (it.hasNext()) {
            it.next().onPlaybackPreloadingCompletion();
        }
        DefaultLogger.d("NvMovieManager", "onPlaybackPreloadingCompletion");
    }

    @Override // com.meicam.sdk.NvsStreamingContext.PlaybackCallback2
    public void onPlaybackTimelinePosition(NvsTimeline nvsTimeline, long j) {
        Iterator<MovieManager.StateChangeListener> it = this.mStateChangeListeners.iterator();
        while (it.hasNext()) {
            it.next().onPlaybackTimeChanged((int) (j / 1000));
        }
    }
}
