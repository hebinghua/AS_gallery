package com.miui.gallery.vlog.clip.single;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.clip.BaseClipMenuFragment;
import com.miui.gallery.vlog.clip.single.SingleClipTopView;
import com.miui.gallery.vlog.clip.single.seekbar.SingleVideoClipBar;
import com.miui.gallery.vlog.clip.speed.Speed;
import com.miui.gallery.vlog.clip.widget.VlogTimeView;
import com.miui.gallery.vlog.home.VlogModel;
import com.miui.gallery.vlog.tools.VlogUtils;

/* loaded from: classes2.dex */
public class SingleClipMenuFragment extends BaseClipMenuFragment<SingleClipMenuPresenter> implements SingleClipMenuContract$ISingleClipMenuView {
    public SingleVideoClipBar mClipBar;
    public boolean mIsTouchingClipBar;
    public ViewGroup mNavRootView;
    public View mSeparatedView;
    public View mSingleClipContentView;
    public View mSingleClipTopView;
    public SingleClipTopView mTopView;
    public VlogModel mVlogModel;
    public UIState mUIState = UIState.PAUSED;
    public SingleClipTopView.Listener mTopViewListener = new SingleClipTopView.Listener() { // from class: com.miui.gallery.vlog.clip.single.SingleClipMenuFragment.1
        @Override // com.miui.gallery.vlog.clip.single.SingleClipTopView.Listener
        public void onPlayClick() {
            SingleClipMenuFragment.this.showPause();
            SingleClipMenuFragment.this.getPresenter().handlePlayClick();
        }

        @Override // com.miui.gallery.vlog.clip.single.SingleClipTopView.Listener
        public void onPauseClick() {
            SingleClipMenuFragment.this.showPlay();
            SingleClipMenuFragment.this.getPresenter().handlePauseClick();
        }

        @Override // com.miui.gallery.vlog.clip.single.SingleClipTopView.Listener
        public void onSpeedClick() {
            SingleClipMenuFragment.this.getPresenter().handleSpeedClick();
        }

        @Override // com.miui.gallery.vlog.clip.single.SingleClipTopView.Listener
        public void onSpeedSelect(Speed speed) {
            SingleClipMenuFragment.this.getPresenter().changeSpeed(Speed.convertSpeedToDouble(speed));
        }
    };
    public SingleVideoClipBar.SeekBarCallback mSeekBarCallback = new SingleVideoClipBar.SeekBarCallback() { // from class: com.miui.gallery.vlog.clip.single.SingleClipMenuFragment.2
        @Override // com.miui.gallery.vlog.clip.single.seekbar.SingleVideoClipBar.SeekBarCallback
        public void onDragProgress(int i) {
            SingleClipMenuFragment.this.getPresenter().handleProgressTimeChange(i * 1000);
        }

        @Override // com.miui.gallery.vlog.clip.single.seekbar.SingleVideoClipBar.SeekBarCallback
        public void onDragLeftAxis(int i) {
            if (SingleClipMenuFragment.this.mTopView.getTimeViewState() == VlogTimeView.State.SHOW_BOTH) {
                SingleClipMenuFragment.this.mTopView.setTimeViewState(VlogTimeView.State.SHOW_TOTAL_ONLY);
            }
            SingleClipMenuFragment.this.getPresenter().handleLockedAreaStartTimeChange(i * 1000);
        }

        @Override // com.miui.gallery.vlog.clip.single.seekbar.SingleVideoClipBar.SeekBarCallback
        public void onDragRightAxis(int i) {
            if (SingleClipMenuFragment.this.mTopView.getTimeViewState() == VlogTimeView.State.SHOW_BOTH) {
                SingleClipMenuFragment.this.mTopView.setTimeViewState(VlogTimeView.State.SHOW_TOTAL_ONLY);
            }
            SingleClipMenuFragment.this.getPresenter().handleLockedAreaEndTimeChange(i * 1000);
        }

        @Override // com.miui.gallery.vlog.clip.single.seekbar.SingleVideoClipBar.SeekBarCallback
        public void onAutoMoveLockAreaStartTimeChanged(int i) {
            SingleClipMenuFragment.this.getPresenter().handleLockedAreaStartTimeChange(i * 1000);
        }

        @Override // com.miui.gallery.vlog.clip.single.seekbar.SingleVideoClipBar.SeekBarCallback
        public void onAutoMoveLockAreaEndTimeChanged(int i) {
            SingleClipMenuFragment.this.getPresenter().handleLockedAreaEndTimeChange(i * 1000);
        }

        @Override // com.miui.gallery.vlog.clip.single.seekbar.SingleVideoClipBar.SeekBarCallback
        public void onTouchStateChanged(boolean z) {
            if (!z) {
                SingleClipMenuFragment.this.mTopView.setTimeViewState(VlogTimeView.State.SHOW_BOTH);
            } else {
                SingleClipMenuFragment.this.getIVlogView().hideProgressView();
                if (SingleClipMenuFragment.this.mTopView.isSpeedViewExpand()) {
                    SingleClipMenuFragment.this.mTopView.collapseSpeedView();
                }
            }
            SingleVideoClipBar.TouchArea touchArea = SingleClipMenuFragment.this.mClipBar.getTouchArea();
            if (touchArea != SingleVideoClipBar.TouchArea.OTHER) {
                SingleClipMenuFragment.this.mIsTouchingClipBar = z;
                SingleClipMenuFragment.this.getPresenter().handleTouchSeekBar(z, touchArea);
            }
        }

        @Override // com.miui.gallery.vlog.clip.single.seekbar.SingleVideoClipBar.SeekBarCallback
        public void onZoomStateChanged(SingleVideoClipBar.ZoomState zoomState) {
            if (zoomState == SingleVideoClipBar.ZoomState.ZOOMED) {
                SingleClipMenuFragment.this.mTopView.setTimeViewState(VlogTimeView.State.SHOW_TOTAL_PRECISE);
            } else if (zoomState != SingleVideoClipBar.ZoomState.NORMAL) {
            } else {
                SingleClipMenuFragment.this.mTopView.setTimeViewState(VlogTimeView.State.SHOW_BOTH);
            }
        }
    };

    /* loaded from: classes2.dex */
    public enum UIState {
        PLAYING,
        PAUSED
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public View createView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return layoutInflater.inflate(R$layout.vlog_single_clip_menu_layout, viewGroup, false);
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void initView(View view) {
        VlogModel vlogModel = (VlogModel) VlogUtils.getViewModel((FragmentActivity) this.mContext, VlogModel.class);
        this.mVlogModel = vlogModel;
        ViewGroup viewGroup = (ViewGroup) view;
        View singleClipContentView = vlogModel.getSingleClipContentView(viewGroup);
        this.mSingleClipContentView = singleClipContentView;
        if (singleClipContentView.getParent() != null) {
            ((ViewGroup) this.mSingleClipContentView.getParent()).removeView(this.mSingleClipContentView);
        }
        View singleClipTopView = this.mVlogModel.getSingleClipTopView(viewGroup);
        this.mSingleClipTopView = singleClipTopView;
        if (singleClipTopView.getParent() != null) {
            ((ViewGroup) this.mSingleClipTopView.getParent()).removeView(this.mSingleClipTopView);
        }
        this.mNavRootView = (ViewGroup) view.findViewById(R$id.navRoot);
        if (isLandscape()) {
            view = LayoutInflater.from(getContext()).inflate(R$layout.vlog_single_clip_menu_separated_layout, (ViewGroup) null, false);
            this.mSeparatedView = view;
            ((ViewGroup) view.findViewById(R$id.layout_single_clip_top)).addView(this.mSingleClipTopView);
            ((ViewGroup) this.mSeparatedView.findViewById(R$id.layout_single_clip_content)).addView(this.mSingleClipContentView);
            this.mTopView = (SingleClipTopView) view.findViewById(R$id.top_view);
        } else {
            this.mNavRootView.addView(this.mSingleClipContentView);
            this.mTopView = (SingleClipTopView) this.mSingleClipTopView;
        }
        this.mClipBar = (SingleVideoClipBar) view.findViewById(R$id.clip_seek_bar);
    }

    @Override // com.miui.gallery.vlog.MenuFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mTopView.setListener(this.mTopViewListener);
        updatePlayViewState(getPresenter().isPlaying());
    }

    @Override // com.miui.gallery.vlog.MenuFragment, androidx.fragment.app.Fragment
    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
        if (!z) {
            updatePlayViewState(getPresenter().isPlaying());
        }
    }

    public final void updatePlayViewState(boolean z) {
        if (z) {
            showPause();
        } else {
            showPlay();
        }
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public View getOperationView() {
        if (isLandscape()) {
            return null;
        }
        return this.mTopView;
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public View getSeparatedView() {
        return this.mSeparatedView;
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    /* renamed from: createPresenter  reason: collision with other method in class */
    public SingleClipMenuPresenter mo1801createPresenter() {
        return new SingleClipMenuPresenter(this.mContext, this);
    }

    public SingleClipMenuPresenter getPresenter() {
        return (SingleClipMenuPresenter) this.mMenuPresenter;
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuView
    public void updateTotalTime(long j) {
        this.mTopView.setTotalTime(j);
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuView
    public void updateCurrentTime(long j) {
        this.mTopView.setCurrentTime(j);
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void updateClipList() {
        super.updateClipList();
        getPresenter().handleVideoClipReady();
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuView
    public void initSeekBar(long j, float f) {
        this.mClipBar.setVideoFrameLoader(getPresenter().getVideoFrameLoader());
        this.mClipBar.setSeekBarCallback(this.mSeekBarCallback);
        this.mClipBar.setTotalTime((int) (j / 1000));
        DefaultLogger.d("SingleClipMenuFragment", "initSeekBar totalTime=%d", Long.valueOf(j));
        this.mClipBar.setVideoPath(getPresenter().getVideoPath());
        this.mClipBar.setThumbnailAspectRatio(f);
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuView
    public void updateSeekBar(long j, long j2, long j3, long j4, double d) {
        this.mClipBar.updateSpeed(d);
        this.mClipBar.updateTimes((int) (j / 1000), (int) (j2 / 1000), (int) (j3 / 1000), (int) (j4 / 1000));
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuView
    public void updateSpeedPickerEnables(boolean[] zArr) {
        this.mTopView.setSpeedPickerItemEnables(zArr);
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        if (this.mSingleClipContentView.getParent() == this.mNavRootView) {
            ((ViewGroup) this.mSingleClipContentView.getParent()).removeView(this.mSingleClipContentView);
            if (this.mSingleClipTopView.getParent() == null) {
                return;
            }
            ((ViewGroup) this.mSingleClipTopView.getParent()).removeView(this.mSingleClipTopView);
        }
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void onPlaybackStopped() {
        super.onPlaybackStopped();
        updatePlayViewState(false);
        DefaultLogger.d("SingleClipMenuFragment", "onPlaybackStopped");
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void onPlaybackTimelinePosition(long j) {
        super.onPlaybackTimelinePosition(j);
        DefaultLogger.d("SingleClipMenuFragment", "onPlaybackTimelinePosition:%d", Long.valueOf(j));
        this.mTopView.setCurrentTime(j);
        this.mClipBar.setProgress((int) calcSeekBarProgressTimeMillis(j));
    }

    public long calcSeekBarProgressTimeMillis(long j) {
        return (j + getPresenter().getActualTrimInMicros()) / 1000;
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void onPlaybackEOF() {
        super.onPlaybackEOF();
        DefaultLogger.d("SingleClipMenuFragment", "onPlaybackEOF");
        showPlay();
        updatePlayViewState(false);
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void onSeek(long j) {
        DefaultLogger.d("SingleClipMenuFragment", "onSeek time=%d", Long.valueOf(j));
        ((SingleClipMenuPresenter) this.mMenuPresenter).handleVlogPlayViewSeek(j);
        this.mClipBar.setProgress((int) calcSeekBarProgressTimeMillis(j));
    }

    @Override // com.miui.gallery.vlog.MenuFragment
    public void seekTime(long j) {
        super.seekTime(j);
        DefaultLogger.d("SingleClipMenuFragment", "seekTime time=%d,current=%d", Long.valueOf(j), Long.valueOf(getPresenter().getCurrentTimeMicros()));
        getPresenter().handleSeekDone(j);
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuView
    public long calcClipProgressTimeMicros(long j) {
        return getPresenter().getActualTrimInMicros() + j;
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuView
    public void showPlay() {
        this.mTopView.showPlay();
        this.mUIState = UIState.PAUSED;
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuView
    public void showPause() {
        this.mTopView.showPause();
        this.mUIState = UIState.PLAYING;
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuView
    public double[] getSupportSpeeds() {
        return this.mTopView.getSupportSpeeds();
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuView
    public void showPlayProgress() {
        getIVlogView().showProgressView();
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuView
    public void setPlayProgress(long j) {
        getIVlogView().setPlayViewProgress(j);
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuView
    public boolean isTouchingClipBar() {
        return this.mIsTouchingClipBar;
    }

    @Override // com.miui.gallery.vlog.clip.single.SingleClipMenuContract$ISingleClipMenuView
    public UIState getUIState() {
        return this.mUIState;
    }
}
