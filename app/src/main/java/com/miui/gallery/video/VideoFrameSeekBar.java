package com.miui.gallery.video;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.List;

/* loaded from: classes2.dex */
public class VideoFrameSeekBar extends FrameLayout {
    public Context mContext;
    public boolean mIsRtl;
    public OnSeekBarChangeListener mOnSeekBarChangeListener;
    public float mProgress;
    public VideoFrameThumbAdapter mRecyclerAdapter;
    public RecyclerView mRecyclerView;
    public boolean mScrollIdle;
    public VideoTagsView mTagsView;
    public AnimatorListenerAdapter mTransitionAnimListener;
    public View mTransitionView;

    /* loaded from: classes2.dex */
    public interface OnSeekBarChangeListener {
        void onProgressChanged(float f);

        void onScrollStateChanged(boolean z);
    }

    public VideoFrameSeekBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mScrollIdle = true;
        this.mTransitionAnimListener = new AnimatorListenerAdapter() { // from class: com.miui.gallery.video.VideoFrameSeekBar.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                VideoFrameSeekBar.this.hideTransitionView();
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                VideoFrameSeekBar.this.hideTransitionView();
            }
        };
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mContext = getContext();
        this.mIsRtl = getResources().getConfiguration().getLayoutDirection() == 1;
        this.mTransitionView = findViewById(R.id.transition_view);
        this.mTagsView = (VideoTagsView) findViewById(R.id.video_tags);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.video_frame_thumb_list);
        this.mRecyclerView = recyclerView;
        recyclerView.setDrawingCacheEnabled(true);
        this.mRecyclerAdapter = new VideoFrameThumbAdapter(this.mContext);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.mContext, 0, false));
        this.mRecyclerView.setAdapter(this.mRecyclerAdapter);
        this.mRecyclerView.setOverScrollMode(2);
        this.mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() { // from class: com.miui.gallery.video.VideoFrameSeekBar.1
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView2, int i, int i2) {
                if (!VideoFrameSeekBar.this.mScrollIdle) {
                    VideoFrameSeekBar videoFrameSeekBar = VideoFrameSeekBar.this;
                    videoFrameSeekBar.mProgress = videoFrameSeekBar.mRecyclerAdapter.getScrollPercent(recyclerView2);
                    VideoFrameSeekBar.this.updateVideoTags();
                    if (VideoFrameSeekBar.this.mOnSeekBarChangeListener == null) {
                        return;
                    }
                    VideoFrameSeekBar.this.mOnSeekBarChangeListener.onProgressChanged(VideoFrameSeekBar.this.mProgress);
                }
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView2, int i) {
                boolean z = false;
                if (VideoFrameSeekBar.this.mScrollIdle && i != 0) {
                    VideoFrameSeekBar.this.mScrollIdle = false;
                }
                if (VideoFrameSeekBar.this.mOnSeekBarChangeListener != null) {
                    OnSeekBarChangeListener onSeekBarChangeListener = VideoFrameSeekBar.this.mOnSeekBarChangeListener;
                    if (i != 0) {
                        z = true;
                    }
                    onSeekBarChangeListener.onScrollStateChanged(z);
                }
            }
        });
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        float f = this.mProgress;
        super.onLayout(z, i, i2, i3, i4);
        if (z) {
            VideoFrameThumbAdapter videoFrameThumbAdapter = this.mRecyclerAdapter;
            if (videoFrameThumbAdapter != null) {
                videoFrameThumbAdapter.configLayoutParams(i3 - i);
            }
            setProgress(f);
        }
    }

    public void updateFrameList(List<Bitmap> list, boolean z, boolean z2) {
        if (this.mRecyclerAdapter == null) {
            return;
        }
        DefaultLogger.d("VideoFrameSeekBar", "scrollToPosition 0");
        boolean z3 = z & (this.mRecyclerAdapter.getDataListSize() != 0 && this.mRecyclerView.getAlpha() == 1.0f && this.mRecyclerView.getScrollState() == 0);
        if (z3) {
            configTransitionView();
        }
        this.mScrollIdle = true;
        if (z2) {
            this.mProgress = 0.0f;
            this.mRecyclerView.scrollToPosition(0);
            updateVideoTags();
        } else {
            setProgress(this.mProgress);
        }
        this.mRecyclerAdapter.updateDataList(list);
        if (!z3) {
            return;
        }
        this.mRecyclerView.setAlpha(0.0f);
        this.mRecyclerView.animate().alpha(1.0f).setDuration(200L).setListener(this.mTransitionAnimListener).start();
    }

    public final void configTransitionView() {
        VideoFrameThumbAdapter videoFrameThumbAdapter = this.mRecyclerAdapter;
        if (videoFrameThumbAdapter == null || videoFrameThumbAdapter.getDataListSize() == 0) {
            return;
        }
        this.mRecyclerView.destroyDrawingCache();
        Bitmap drawingCache = this.mRecyclerView.getDrawingCache();
        if (drawingCache == null) {
            return;
        }
        Bitmap createBitmap = Bitmap.createBitmap(drawingCache);
        this.mRecyclerView.destroyDrawingCache();
        this.mTransitionView.setBackground(new BitmapDrawable(getResources(), createBitmap));
        this.mTransitionView.setVisibility(0);
    }

    public final void hideTransitionView() {
        View view = this.mTransitionView;
        if (view != null) {
            view.setVisibility(4);
            this.mTransitionView.setBackground(null);
        }
    }

    public void setOnSeekBarChangeListener(OnSeekBarChangeListener onSeekBarChangeListener) {
        this.mOnSeekBarChangeListener = onSeekBarChangeListener;
    }

    public float getProgress() {
        return this.mProgress;
    }

    public void setProgress(float f) {
        VideoFrameThumbAdapter videoFrameThumbAdapter = this.mRecyclerAdapter;
        if (videoFrameThumbAdapter != null) {
            int scrollOffset = videoFrameThumbAdapter.getScrollOffset(this.mRecyclerView, f);
            if (scrollOffset != 0) {
                RecyclerView recyclerView = this.mRecyclerView;
                if (this.mIsRtl) {
                    scrollOffset = -scrollOffset;
                }
                recyclerView.scrollBy(scrollOffset, 0);
            } else if (this.mIsRtl) {
                this.mRecyclerView.scrollToPosition(0);
            }
            this.mProgress = f;
            updateVideoTags();
        }
    }

    public void setVideoTags(List<Float> list) {
        if (this.mTagsView == null) {
            return;
        }
        if (!BaseMiscUtil.isValid(list)) {
            this.mTagsView.setVisibility(8);
            return;
        }
        this.mTagsView.setTags(list);
        this.mTagsView.setCurrentProgress(this.mProgress);
        this.mTagsView.setTotalLength(this.mRecyclerAdapter.getTotalWidth());
        this.mTagsView.setVisibility(0);
    }

    public void updateVideoTags() {
        VideoTagsView videoTagsView = this.mTagsView;
        if (videoTagsView == null || videoTagsView.getVisibility() != 0) {
            return;
        }
        this.mTagsView.setTotalLength(this.mRecyclerAdapter.getTotalWidth());
        this.mTagsView.setCurrentProgress(this.mProgress);
    }

    public void stopScroll() {
        RecyclerView recyclerView = this.mRecyclerView;
        if (recyclerView != null) {
            recyclerView.stopScroll();
        }
    }
}
