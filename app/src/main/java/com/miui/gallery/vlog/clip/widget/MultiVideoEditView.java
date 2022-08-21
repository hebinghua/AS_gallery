package com.miui.gallery.vlog.clip.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.util.FormatUtil;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.vlog.R$dimen;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.clip.widget.MultiVideoFrameAdapter;
import com.miui.gallery.vlog.clip.widget.VideoClipBar;
import com.miui.gallery.vlog.clip.widget.VideoTransitionEnterView;
import com.miui.gallery.vlog.home.VlogConfig;
import com.miui.gallery.vlog.sdk.interfaces.IVideoClip;
import com.miui.gallery.vlog.sdk.interfaces.VideoFrameLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class MultiVideoEditView extends FrameLayout implements VideoClipBar.OnVideoClipRegionChangedListener, ViewTreeObserver.OnGlobalLayoutListener, MultiVideoFrameAdapter.OnItemSelectedListener, VideoTransitionEnterView.OnTransitionItemClipListener {
    public VideoClipBar mClipBar;
    public VideoClipInfo mCurrentVideoClip;
    public int mCurrentVideoClipScrollX;
    public List<VideoClipInfo> mDataList;
    public FrameLayout.LayoutParams mDurationLayoutParams;
    public TextView mDurationView;
    public boolean mHasPendingClipScroll;
    public boolean mIsDraggingClipBar;
    public boolean mIsInClipMode;
    public boolean mIsPendingDraggingEnd;
    public boolean mIsSingleVideoEdit;
    public boolean mLayoutToUpdated;
    public MultiVideoEditListener mListener;
    public int mMinClipWidth;
    public int mMinClipWidthSingleVideoEdit;
    public boolean mPendingSeekExactly;
    public long mPendingSeekTime;
    public double mPixelPerMicroSecond;
    public MultiVideoFrameAdapter mRecyclerAdapter;
    public RecyclerView mRecyclerView;
    public int mScreenWidth;
    public RecyclerView.OnScrollListener mScrollListener;
    public int mTargetLeft;
    public long mTotalDuration;
    public long mTotalDurationClipEnd;
    public long mTotalDurationClipIng;
    public VideoTransitionEnterView mTransitionEnterView;

    /* loaded from: classes2.dex */
    public interface MultiVideoEditListener {
        void onClipModeChanged(boolean z);

        void onCurrentClipChanged(IVideoClip iVideoClip);

        void onTransitionSelected(IVideoClip iVideoClip);

        void onVideoClipLongClick(IVideoClip iVideoClip);

        void onVideoClipRegionChanged(IVideoClip iVideoClip, long j, long j2);

        void onVideoClipRegionEnd();

        void onVideoClipSelected(IVideoClip iVideoClip);

        void onVideoSeek(long j, long j2, long j3);
    }

    public MultiVideoEditView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mDataList = new ArrayList();
        this.mPendingSeekTime = -1L;
        this.mIsInClipMode = false;
        this.mIsPendingDraggingEnd = false;
        this.mIsSingleVideoEdit = false;
        this.mScrollListener = new RecyclerView.OnScrollListener() { // from class: com.miui.gallery.vlog.clip.widget.MultiVideoEditView.1
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                MultiVideoEditView.this.updateLayout();
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                MultiVideoEditView.this.mClipBar.setDragEnable(i == 0);
            }
        };
    }

    public final void initScreenRelatedValues() {
        this.mPixelPerMicroSecond = VlogConfig.sPixelPerMicroSecond;
        this.mMinClipWidth = convertMsToPixel(500000L);
        this.mMinClipWidthSingleVideoEdit = convertMsToPixel(1000000L);
        this.mScreenWidth = ScreenUtils.getCurScreenWidth();
    }

    public void setListener(MultiVideoEditListener multiVideoEditListener) {
        this.mListener = multiVideoEditListener;
    }

    public IVideoClip getCurrentVideoClip() {
        VideoClipInfo currentVideoClip;
        MultiVideoFrameAdapter multiVideoFrameAdapter = this.mRecyclerAdapter;
        if (multiVideoFrameAdapter == null || (currentVideoClip = multiVideoFrameAdapter.getCurrentVideoClip()) == null) {
            return null;
        }
        return currentVideoClip.getOwner();
    }

    public long getSeekTime() {
        MultiVideoFrameAdapter.CurrentScrollState currentScrollState;
        MultiVideoFrameAdapter multiVideoFrameAdapter = this.mRecyclerAdapter;
        if (multiVideoFrameAdapter == null || (currentScrollState = multiVideoFrameAdapter.getCurrentScrollState()) == null) {
            return 0L;
        }
        return convertPixelToMs(currentScrollState.seekPosition);
    }

    public long getDuration() {
        return this.mTotalDuration;
    }

    public void seekTo(long j) {
        seekTo(j, false);
    }

    public void seekTo(long j, boolean z) {
        if (this.mRecyclerView == null) {
            return;
        }
        if (this.mLayoutToUpdated) {
            this.mPendingSeekTime = j;
            this.mPendingSeekExactly = z;
            requestLayout();
            return;
        }
        seekToInternal(j, z);
    }

    public final void seekToInternal(long j, boolean z) {
        if (this.mRecyclerAdapter == null) {
            return;
        }
        int convertMsToPixel = convertMsToPixel(j);
        if (z) {
            int size = this.mDataList.size();
            int i = 0;
            while (true) {
                if (i >= size) {
                    break;
                }
                VideoClipInfo videoClipInfo = this.mDataList.get(i);
                long inPoint = videoClipInfo.getInPoint();
                if (videoClipInfo.hasPreviousTransition()) {
                    inPoint -= 450000;
                }
                if (j >= inPoint && j < videoClipInfo.getDurationWithTransition() + inPoint) {
                    convertMsToPixel = this.mRecyclerAdapter.getScrollX(i, (((float) (j - inPoint)) * 1.0f) / ((float) videoClipInfo.getDurationWithTransition()));
                    break;
                }
                i++;
            }
        }
        this.mRecyclerView.scrollBy(this.mRecyclerAdapter.getScrollOffset(convertMsToPixel), 0);
    }

    public void setSingleVideoEdit(boolean z) {
        this.mIsSingleVideoEdit = z;
    }

    public void setClipMode(boolean z) {
        if (this.mIsInClipMode == z) {
            return;
        }
        this.mIsInClipMode = z;
        updateLayout();
        MultiVideoEditListener multiVideoEditListener = this.mListener;
        if (multiVideoEditListener == null) {
            return;
        }
        multiVideoEditListener.onClipModeChanged(z);
    }

    public boolean isInClipMode() {
        return this.mIsInClipMode;
    }

    public void updateVideoClipList(List<IVideoClip> list) {
        if (this.mRecyclerAdapter == null) {
            return;
        }
        this.mDataList.clear();
        this.mTotalDuration = 0L;
        this.mTotalDurationClipEnd = 0L;
        this.mTotalDurationClipIng = 0L;
        if (list != null) {
            VideoClipInfo videoClipInfo = null;
            int size = list.size();
            int i = 0;
            while (i < size) {
                IVideoClip iVideoClip = list.get(i);
                VideoClipInfo videoClipInfo2 = new VideoClipInfo(iVideoClip, i);
                videoClipInfo2.setFilePath(iVideoClip.getFilePath());
                videoClipInfo2.setHasTransition(((double) Math.abs(iVideoClip.getTrimOut() - iVideoClip.getTrimOutWithTrans())) >= 1.0E-6d);
                videoClipInfo2.setHasPreviousTransition(((double) Math.abs(iVideoClip.getTrimIn() - iVideoClip.getTrimInWithTrans())) >= 1.0E-6d);
                videoClipInfo2.setInPoint(iVideoClip.getInPoint());
                videoClipInfo2.setOutPoint(iVideoClip.getOutPoint());
                videoClipInfo2.setTrimIn(iVideoClip.getTrimIn());
                videoClipInfo2.setTrimOut(iVideoClip.getTrimOut());
                videoClipInfo2.setTransTrimIn(iVideoClip.getTrimInWithTrans());
                videoClipInfo2.setTransTrimOut(iVideoClip.getTrimOutWithTrans());
                videoClipInfo2.setSpeed(iVideoClip.getSpeed());
                videoClipInfo2.setVideoDuration(iVideoClip.getOriginDuration());
                if (videoClipInfo != null) {
                    videoClipInfo.setNext(videoClipInfo2);
                }
                videoClipInfo2.updateOriginalDuration();
                long durationWithTransition = this.mTotalDuration + videoClipInfo2.getDurationWithTransition();
                this.mTotalDuration = durationWithTransition;
                this.mTotalDurationClipEnd = durationWithTransition;
                this.mTotalDurationClipIng = durationWithTransition;
                this.mDataList.add(videoClipInfo2);
                i++;
                videoClipInfo = videoClipInfo2;
            }
        }
        this.mRecyclerAdapter.updateDataList(this.mDataList);
        this.mTransitionEnterView.updateVideoClipList(this.mDataList, this.mRecyclerAdapter.getVideoEndPositionMap());
        if (this.mIsSingleVideoEdit) {
            this.mIsInClipMode = true;
        }
        updateLayout();
        this.mLayoutToUpdated = true;
    }

    public void setVideoFrameLoader(VideoFrameLoader videoFrameLoader) {
        MultiVideoFrameAdapter multiVideoFrameAdapter = this.mRecyclerAdapter;
        if (multiVideoFrameAdapter != null) {
            multiVideoFrameAdapter.setVideoFrameLoader(videoFrameLoader);
        }
    }

    public boolean isScrolling() {
        RecyclerView recyclerView = this.mRecyclerView;
        if (recyclerView == null) {
            return false;
        }
        return recyclerView.getScrollState() != 0 || this.mIsDraggingClipBar;
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mRecyclerView = (RecyclerView) findViewById(R$id.recycle_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), 0, false);
        this.mRecyclerView.setLayoutManager(linearLayoutManager);
        this.mRecyclerView.setOverScrollMode(2);
        this.mRecyclerView.addOnScrollListener(this.mScrollListener);
        this.mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(this);
        int dimensionPixelSize = getContext().getResources().getDimensionPixelSize(R$dimen.vlog_drag_bar_width);
        this.mScreenWidth = ScreenUtils.getCurScreenWidth();
        MultiVideoFrameAdapter multiVideoFrameAdapter = new MultiVideoFrameAdapter(getContext(), linearLayoutManager, this, VlogConfig.sPixelPerMicroSecond, dimensionPixelSize);
        this.mRecyclerAdapter = multiVideoFrameAdapter;
        multiVideoFrameAdapter.setEmptyViewWidth(this.mScreenWidth / 2);
        this.mRecyclerView.setAdapter(this.mRecyclerAdapter);
        VideoClipBar videoClipBar = (VideoClipBar) findViewById(R$id.video_clip_view);
        this.mClipBar = videoClipBar;
        videoClipBar.setBarWidth(dimensionPixelSize);
        this.mClipBar.setListener(this);
        TextView textView = (TextView) findViewById(R$id.duration_text);
        this.mDurationView = textView;
        this.mDurationLayoutParams = (FrameLayout.LayoutParams) textView.getLayoutParams();
        VideoTransitionEnterView videoTransitionEnterView = (VideoTransitionEnterView) findViewById(R$id.video_transition_enter);
        this.mTransitionEnterView = videoTransitionEnterView;
        videoTransitionEnterView.setMinDuration(1000000);
        this.mTransitionEnterView.setListener(this);
    }

    public final void updateLayout() {
        MultiVideoFrameAdapter.CurrentScrollState currentScrollState;
        int i;
        MultiVideoFrameAdapter multiVideoFrameAdapter = this.mRecyclerAdapter;
        if (multiVideoFrameAdapter == null || this.mIsDraggingClipBar || (currentScrollState = multiVideoFrameAdapter.getCurrentScrollState()) == null) {
            return;
        }
        MultiVideoFrameAdapter.ThumbnailItem thumbnailItem = currentScrollState.thumbnailItem;
        VideoClipInfo videoClipInfo = thumbnailItem.owner;
        if (this.mCurrentVideoClip != videoClipInfo) {
            this.mCurrentVideoClip = videoClipInfo;
            if (videoClipInfo != null) {
                this.mListener.onCurrentClipChanged(videoClipInfo.getOwner());
            }
        }
        int i2 = 0;
        if (isInClipMode()) {
            this.mClipBar.setVisibility(0);
            this.mTransitionEnterView.setVisibility(8);
            this.mDurationView.setVisibility(0);
            this.mCurrentVideoClipScrollX = thumbnailItem.firstItem.scrollX;
            int convertMsToPixel = convertMsToPixel((long) (videoClipInfo.getTransTrimIn() / videoClipInfo.getSpeed()));
            int convertMsToPixel2 = convertMsToPixel((long) ((videoClipInfo.getVideoDuration() - videoClipInfo.getTransTrimOut()) / videoClipInfo.getSpeed()));
            int convertMsToPixel3 = convertMsToPixel((long) (videoClipInfo.getVideoDuration() / videoClipInfo.getSpeed()));
            int i3 = currentScrollState.videoRectLeft;
            if (videoClipInfo.hasPreviousTransition()) {
                int convertMsToPixel4 = convertMsToPixel(450000L);
                i3 -= convertMsToPixel4;
                i = convertMsToPixel4;
            } else {
                i = 0;
            }
            if (videoClipInfo.hasTransition()) {
                i2 = convertMsToPixel(450000L);
            }
            this.mClipBar.setClipRegion(convertMsToPixel, convertMsToPixel2, convertMsToPixel3, this.mIsSingleVideoEdit ? this.mMinClipWidthSingleVideoEdit : this.mMinClipWidth, i3, i, i2, 1.0d);
            updateDurationView();
        } else {
            this.mClipBar.setVisibility(8);
            this.mDurationView.setVisibility(8);
            this.mTransitionEnterView.setVisibility(0);
            this.mTransitionEnterView.scrollTo(currentScrollState.scrollX);
        }
        if (!isScrolling() || this.mListener == null) {
            return;
        }
        long convertPixelToMs = convertPixelToMs(currentScrollState.seekPosition);
        this.mListener.onVideoSeek(convertPixelToMs, convertPixelToMs, this.mTotalDurationClipIng);
    }

    public void stopScroll() {
        if (isScrolling()) {
            this.mRecyclerView.stopScroll();
        }
    }

    public final int convertMsToPixel(long j) {
        return (int) ((j * this.mPixelPerMicroSecond) + 0.5d);
    }

    public final long convertPixelToMs(int i) {
        return (long) ((i / this.mPixelPerMicroSecond) + 0.5d);
    }

    @Override // com.miui.gallery.vlog.clip.widget.MultiVideoFrameAdapter.OnItemSelectedListener
    public void onItemSelected(VideoClipInfo videoClipInfo) {
        if (this.mCurrentVideoClip != null && !this.mIsSingleVideoEdit) {
            long j = -1;
            if (isInClipMode() && videoClipInfo == this.mCurrentVideoClip) {
                setClipMode(false);
            } else {
                setClipMode(true);
                if (videoClipInfo.getIndex() < this.mCurrentVideoClip.getIndex()) {
                    j = videoClipInfo.getOutPoint();
                } else if (videoClipInfo.getIndex() > this.mCurrentVideoClip.getIndex()) {
                    j = videoClipInfo.getInPoint() + convertPixelToMs(2);
                }
            }
            long j2 = j;
            if (j2 >= 0) {
                seekTo(j2, true);
                MultiVideoEditListener multiVideoEditListener = this.mListener;
                if (multiVideoEditListener != null) {
                    multiVideoEditListener.onVideoSeek(j2, j2, this.mTotalDurationClipIng);
                }
            }
            MultiVideoEditListener multiVideoEditListener2 = this.mListener;
            if (multiVideoEditListener2 == null) {
                return;
            }
            multiVideoEditListener2.onVideoClipSelected(videoClipInfo.getOwner());
        }
    }

    @Override // com.miui.gallery.vlog.clip.widget.MultiVideoFrameAdapter.OnItemSelectedListener
    public void onItemLongClick(VideoClipInfo videoClipInfo) {
        MultiVideoEditListener multiVideoEditListener;
        if (!this.mIsSingleVideoEdit && (multiVideoEditListener = this.mListener) != null) {
            multiVideoEditListener.onVideoClipLongClick(videoClipInfo.getOwner());
        }
    }

    @Override // com.miui.gallery.vlog.clip.widget.VideoClipBar.OnVideoClipRegionChangedListener
    public void onVideoClipRegionDragStart() {
        this.mIsDraggingClipBar = true;
        if (this.mCurrentVideoClip == null) {
            return;
        }
        this.mRecyclerAdapter.setEmptyViewWidth(this.mScreenWidth);
        this.mCurrentVideoClipScrollX += this.mScreenWidth / 2;
        sendPendingScroll(this.mClipBar.getClipLeft() + this.mClipBar.getClipLeftDelata());
        MultiVideoEditListener multiVideoEditListener = this.mListener;
        if (multiVideoEditListener == null) {
            return;
        }
        multiVideoEditListener.onVideoClipRegionChanged(this.mCurrentVideoClip.getOwner(), 0L, this.mCurrentVideoClip.getVideoDuration());
    }

    @Override // com.miui.gallery.vlog.clip.widget.VideoClipBar.OnVideoClipRegionChangedListener
    public void onVideoClipRegionChanged(boolean z, int i, int i2, int i3, int i4) {
        long inPoint;
        if (this.mCurrentVideoClip == null) {
            return;
        }
        long convertPixelToMs = (long) (convertPixelToMs(i2) * this.mCurrentVideoClip.getSpeed());
        long convertPixelToMs2 = (long) (convertPixelToMs(i - i3) * this.mCurrentVideoClip.getSpeed());
        this.mCurrentVideoClip.setTransTrimIn(convertPixelToMs);
        this.mCurrentVideoClip.setTransTrimOut(convertPixelToMs2);
        this.mCurrentVideoClip.setTrimIn(Math.min(this.mCurrentVideoClip.hasPreviousTransition() ? ((long) (this.mCurrentVideoClip.getSpeed() * 900000.0d)) + convertPixelToMs : convertPixelToMs, convertPixelToMs2));
        if (this.mCurrentVideoClip.hasTransition()) {
            convertPixelToMs2 -= (long) (this.mCurrentVideoClip.getSpeed() * 900000.0d);
        }
        this.mCurrentVideoClip.setTrimOut(Math.max(convertPixelToMs, convertPixelToMs2));
        if (this.mListener != null) {
            long speed = (long) (convertPixelToMs / this.mCurrentVideoClip.getSpeed());
            if (z) {
                inPoint = this.mCurrentVideoClip.getInPoint();
            } else {
                inPoint = this.mCurrentVideoClip.getInPoint() + this.mCurrentVideoClip.getDurationWithOutTransition();
            }
            long j = inPoint + speed;
            long originalDuration = (this.mTotalDurationClipEnd - this.mCurrentVideoClip.getOriginalDuration()) + this.mCurrentVideoClip.getDurationWithOutTransition();
            this.mTotalDurationClipIng = originalDuration;
            this.mListener.onVideoSeek(j, j - speed, originalDuration);
        }
        onVideoClipRegionScroll(i4 + this.mClipBar.getClipLeftDelata());
    }

    @Override // com.miui.gallery.vlog.clip.widget.VideoClipBar.OnVideoClipRegionChangedListener
    public void onVideoClipRegionScroll(int i) {
        updateDurationView();
        sendPendingScroll(i);
    }

    @Override // com.miui.gallery.vlog.clip.widget.VideoClipBar.OnVideoClipRegionChangedListener
    public void onVideoClipRegionDragEnd(int i, int i2, int i3, int i4) {
        this.mIsPendingDraggingEnd = true;
        if (this.mCurrentVideoClip == null) {
            return;
        }
        long convertPixelToMs = (long) (convertPixelToMs(i2) * this.mCurrentVideoClip.getSpeed());
        long convertPixelToMs2 = (long) (convertPixelToMs(i - i3) * this.mCurrentVideoClip.getSpeed());
        this.mCurrentVideoClip.setTransTrimIn(convertPixelToMs);
        this.mCurrentVideoClip.setTransTrimOut(convertPixelToMs2);
        this.mTotalDurationClipEnd = (this.mTotalDurationClipEnd - this.mCurrentVideoClip.getOriginalDuration()) + this.mCurrentVideoClip.getDurationWithTransition();
        this.mCurrentVideoClip.updateOriginalDuration();
        IVideoClip owner = this.mCurrentVideoClip.getOwner();
        MultiVideoEditListener multiVideoEditListener = this.mListener;
        if (multiVideoEditListener != null) {
            multiVideoEditListener.onVideoClipRegionChanged(owner, convertPixelToMs, convertPixelToMs2);
        }
        this.mRecyclerAdapter.setEmptyViewWidth(this.mScreenWidth / 2);
        this.mCurrentVideoClipScrollX -= this.mScreenWidth / 2;
        sendPendingScroll(i4);
        postDelayed(new Runnable() { // from class: com.miui.gallery.vlog.clip.widget.MultiVideoEditView.2
            @Override // java.lang.Runnable
            public void run() {
                if (MultiVideoEditView.this.mListener != null) {
                    MultiVideoEditView.this.mListener.onVideoClipRegionEnd();
                }
            }
        }, 100L);
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        initScreenRelatedValues();
        MultiVideoFrameAdapter multiVideoFrameAdapter = this.mRecyclerAdapter;
        if (multiVideoFrameAdapter != null) {
            multiVideoFrameAdapter.setPixelPerMs(this.mPixelPerMicroSecond);
            this.mRecyclerAdapter.setEmptyViewWidth(this.mScreenWidth / 2);
            this.mRecyclerAdapter.updateScreenWidth(this.mScreenWidth);
            this.mRecyclerAdapter.updateDataList(this.mDataList);
            updateLayout();
        }
    }

    @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
    public void onGlobalLayout() {
        if (this.mHasPendingClipScroll) {
            this.mHasPendingClipScroll = false;
            syncPositionWithClipBar();
        }
        if (this.mLayoutToUpdated) {
            long j = this.mPendingSeekTime;
            if (j >= 0) {
                seekToInternal(j, this.mPendingSeekExactly);
                this.mPendingSeekTime = -1L;
            }
            this.mLayoutToUpdated = false;
        }
        if (this.mIsPendingDraggingEnd) {
            this.mIsDraggingClipBar = false;
            updateLayout();
            this.mIsPendingDraggingEnd = false;
        }
    }

    public final void sendPendingScroll(int i) {
        this.mTargetLeft = i;
        this.mHasPendingClipScroll = true;
        this.mRecyclerAdapter.updateDataList(this.mDataList);
        this.mRecyclerView.requestLayout();
    }

    public final void updateDurationView() {
        VideoClipInfo videoClipInfo = this.mCurrentVideoClip;
        if (videoClipInfo == null) {
            return;
        }
        this.mDurationView.setText(getClipDuration((((float) videoClipInfo.getDurationForDrag()) / 1000.0f) + 0.5f));
        this.mDurationLayoutParams.leftMargin = Math.max(0, this.mClipBar.getClipLeft());
        this.mDurationView.setLayoutParams(this.mDurationLayoutParams);
    }

    public final String getClipDuration(long j) {
        if (j <= 60000) {
            return getResources().getString(R$string.vlog_clip_duration_time, String.format(Locale.US, "%.1f", Float.valueOf(((float) j) / 1000.0f)));
        }
        return FormatUtil.formatVideoDuration(j / 1000);
    }

    public final void syncPositionWithClipBar() {
        this.mRecyclerView.scrollBy(-(this.mTargetLeft - (this.mCurrentVideoClipScrollX - this.mRecyclerAdapter.getScrollX())), 0);
    }

    @Override // com.miui.gallery.vlog.clip.widget.VideoTransitionEnterView.OnTransitionItemClipListener
    public void onTransitionItemClick(VideoClipInfo videoClipInfo) {
        MultiVideoEditListener multiVideoEditListener = this.mListener;
        if (multiVideoEditListener != null) {
            multiVideoEditListener.onTransitionSelected(videoClipInfo.getOwner());
        }
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        VideoTransitionEnterView videoTransitionEnterView;
        if (this.mIsPendingDraggingEnd) {
            return true;
        }
        if (!this.mIsInClipMode && motionEvent.getAction() == 1 && (videoTransitionEnterView = this.mTransitionEnterView) != null) {
            return videoTransitionEnterView.performClick(motionEvent);
        }
        return super.onInterceptTouchEvent(motionEvent);
    }
}
