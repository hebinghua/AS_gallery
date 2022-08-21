package com.miui.gallery.vlog.caption.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.vlog.R$dimen;
import com.miui.gallery.vlog.sdk.interfaces.VideoFrameLoader;
import com.miui.gallery.vlog.sdk.manager.MiVideoFrameLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/* loaded from: classes2.dex */
public class MiVideoFrameView extends ViewGroup implements IVideoFrameView, VideoFrameLoader.OnImageLoadedListener {
    public final String TAG;
    public Path mContentOutlinePath;
    public int mCorner;
    public VideoFrameLoader mVideoFrameLoader;
    public int m_contentWidth;
    public ArrayList<VideoClipInfo> m_descArray;
    public int m_endPadding;
    public int m_maxThumbnailWidth;
    public long m_maxTimelinePosToScroll;
    public double m_pixelPerMicrosecond;
    public Bitmap m_placeholderBitmap;
    public boolean m_scrollEnabled;
    public int m_startPadding;
    public float m_thumbnailAspectRatio;
    public int m_thumbnailImageFillMode;
    public TreeMap<ThumbnailId, Thumbnail> m_thumbnailMap;
    public TreeMap<Integer, VideoSubClipInfo> m_thumbnailSequenceMap;
    public boolean m_updatingThumbnail;
    public ArrayList<VideoSubClipInfo> m_videoSubClipInfoArray;

    public final void cancelIconTask() {
    }

    @Override // com.miui.gallery.vlog.sdk.interfaces.VideoFrameLoader.OnImageLoadedListener
    public void onImageDisplayed() {
    }

    @Override // android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public MiVideoFrameView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.TAG = "MiVideoFrameView";
        this.m_scrollEnabled = true;
        this.m_thumbnailAspectRatio = 0.5625f;
        this.m_pixelPerMicrosecond = 7.2E-5d;
        this.m_startPadding = 0;
        this.m_endPadding = 0;
        this.m_thumbnailImageFillMode = 0;
        this.m_maxTimelinePosToScroll = 0L;
        this.m_videoSubClipInfoArray = new ArrayList<>();
        this.m_thumbnailSequenceMap = new TreeMap<>();
        this.m_contentWidth = 0;
        this.m_thumbnailMap = new TreeMap<>();
        this.m_maxThumbnailWidth = 0;
        this.m_updatingThumbnail = false;
        init(context);
    }

    public final void init(Context context) {
        this.mCorner = getResources().getDimensionPixelSize(R$dimen.vlog_caption_video_frame_corner);
        this.mContentOutlinePath = new Path();
        MiVideoFrameLoader miVideoFrameLoader = new MiVideoFrameLoader();
        this.mVideoFrameLoader = miVideoFrameLoader;
        miVideoFrameLoader.setListener(this);
    }

    @Override // com.miui.gallery.vlog.caption.widget.IVideoFrameView
    public void setThumbnailSequenceDescArray(ArrayList<VideoClipInfo> arrayList) {
        if (arrayList != this.m_descArray) {
            clearThumbnailSequences();
            this.m_placeholderBitmap = null;
            this.m_descArray = arrayList;
            if (arrayList != null) {
                int i = 0;
                Iterator<VideoClipInfo> it = arrayList.iterator();
                long j = 0;
                while (it.hasNext()) {
                    VideoClipInfo next = it.next();
                    if (next.mediaFilePath != null) {
                        long j2 = next.inPoint;
                        if (j2 >= j && next.outPoint > j2) {
                            long j3 = next.trimIn;
                            if (j3 >= 0 && next.trimOut > j3) {
                                VideoSubClipInfo videoSubClipInfo = new VideoSubClipInfo();
                                videoSubClipInfo.m_index = i;
                                videoSubClipInfo.m_mediaFilePath = next.mediaFilePath;
                                videoSubClipInfo.m_inPoint = next.inPoint;
                                videoSubClipInfo.m_outPoint = next.outPoint;
                                long j4 = next.trimIn;
                                videoSubClipInfo.m_trimIn = j4;
                                videoSubClipInfo.m_trimDuration = next.trimOut - j4;
                                videoSubClipInfo.m_stillImageHint = next.stillImageHint;
                                videoSubClipInfo.m_onlyDecodeKeyFrame = next.onlyDecodeKeyFrame;
                                videoSubClipInfo.m_thumbnailAspectRatio = next.thumbnailAspectRatio;
                                this.m_videoSubClipInfoArray.add(videoSubClipInfo);
                                i++;
                                j = next.outPoint;
                            }
                        }
                    }
                    Log.e("Meicam", "Invalid VideoClipInfo!");
                }
            }
            updateThumbnailSequenceGeometry();
        }
    }

    public ArrayList<VideoClipInfo> getThumbnailSequenceDescArray() {
        return this.m_descArray;
    }

    @Override // com.miui.gallery.vlog.caption.widget.IVideoFrameView
    public void setThumbnailImageFillMode(int i) {
        int i2 = this.m_thumbnailImageFillMode;
        if (i2 != 1 && i2 != 0) {
            this.m_thumbnailImageFillMode = 0;
        }
        if (this.m_thumbnailImageFillMode != i) {
            this.m_thumbnailImageFillMode = i;
            updateThumbnailSequenceGeometry();
        }
    }

    public int getThumbnailImageFillMode() {
        return this.m_thumbnailImageFillMode;
    }

    @Override // com.miui.gallery.vlog.caption.widget.IVideoFrameView
    public void setThumbnailAspectRatio(float f) {
        if (f < 0.1f) {
            f = 0.1f;
        } else if (f > 10.0f) {
            f = 10.0f;
        }
        if (Math.abs(this.m_thumbnailAspectRatio - f) >= 0.001f) {
            this.m_thumbnailAspectRatio = f;
            updateThumbnailSequenceGeometry();
        }
    }

    public float getThumbnailAspectRatio() {
        return this.m_thumbnailAspectRatio;
    }

    @Override // com.miui.gallery.vlog.caption.widget.IVideoFrameView
    public void setPixelPerMicrosecond(double d) {
        if (d <= SearchStatUtils.POW || d == this.m_pixelPerMicrosecond) {
            return;
        }
        this.m_pixelPerMicrosecond = d;
        updateThumbnailSequenceGeometry();
    }

    public double getPixelPerMicrosecond() {
        return this.m_pixelPerMicrosecond;
    }

    @Override // com.miui.gallery.vlog.caption.widget.IVideoFrameView
    public void setStartPadding(int i) {
        if (i < 0 || i == this.m_startPadding) {
            return;
        }
        this.m_startPadding = i;
        updateThumbnailSequenceGeometry();
    }

    public int getStartPadding() {
        return this.m_startPadding;
    }

    @Override // com.miui.gallery.vlog.caption.widget.IVideoFrameView
    public void setEndPadding(int i) {
        if (i < 0 || i == this.m_endPadding) {
            return;
        }
        this.m_endPadding = i;
        updateThumbnailSequenceGeometry();
    }

    public int getEndPadding() {
        return this.m_endPadding;
    }

    @Override // com.miui.gallery.vlog.caption.widget.IVideoFrameView
    public void release() {
        this.mVideoFrameLoader.release();
        this.mVideoFrameLoader = null;
    }

    @Override // com.miui.gallery.vlog.caption.widget.IVideoFrameView
    public void reInit() {
        if (this.mVideoFrameLoader == null) {
            MiVideoFrameLoader miVideoFrameLoader = new MiVideoFrameLoader();
            this.mVideoFrameLoader = miVideoFrameLoader;
            miVideoFrameLoader.setListener(this);
        }
    }

    public void setMaxTimelinePosToScroll(int i) {
        long max = Math.max(i, 0);
        if (max != this.m_maxTimelinePosToScroll) {
            this.m_maxTimelinePosToScroll = max;
            updateThumbnailSequenceGeometry();
        }
    }

    public long getMaxTimelinePosToScroll() {
        return this.m_maxTimelinePosToScroll;
    }

    public void setScrollEnabled(boolean z) {
        this.m_scrollEnabled = z;
    }

    public boolean getScrollEnabled() {
        return this.m_scrollEnabled;
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        cancelIconTask();
        super.onDetachedFromWindow();
    }

    public final void requestUpdateThumbnailSequenceGeometry() {
        new Handler().post(new Runnable() { // from class: com.miui.gallery.vlog.caption.widget.MiVideoFrameView.1
            @Override // java.lang.Runnable
            public void run() {
                MiVideoFrameView.this.updateThumbnailSequenceGeometry();
            }
        });
    }

    public final void updateThumbnailSequenceGeometry() {
        int height;
        int max;
        cancelIconTask();
        clearThumbnails();
        if (getHeight() != 0) {
            this.m_thumbnailSequenceMap.clear();
            int i = this.m_startPadding;
            this.m_maxThumbnailWidth = 0;
            Iterator<VideoSubClipInfo> it = this.m_videoSubClipInfoArray.iterator();
            while (it.hasNext()) {
                VideoSubClipInfo next = it.next();
                next.m_flags &= -3;
                int floor = ((int) Math.floor((next.m_inPoint * this.m_pixelPerMicrosecond) + 0.5d)) + this.m_startPadding;
                int floor2 = ((int) Math.floor((next.m_outPoint * this.m_pixelPerMicrosecond) + 0.5d)) + this.m_startPadding;
                if (floor2 > floor) {
                    next.m_x = floor;
                    next.m_width = floor2 - floor;
                    float f = next.m_thumbnailAspectRatio;
                    if (f <= 0.0f) {
                        f = this.m_thumbnailAspectRatio;
                    }
                    int floor3 = (int) Math.floor((height * f) + 0.5d);
                    next.m_thumbnailWidth = floor3;
                    int max2 = Math.max(floor3, 1);
                    next.m_thumbnailWidth = max2;
                    this.m_maxThumbnailWidth = Math.max(max2, this.m_maxThumbnailWidth);
                    this.m_thumbnailSequenceMap.put(Integer.valueOf(floor), next);
                    i = floor2;
                }
            }
            long j = this.m_maxTimelinePosToScroll;
            if (j <= 0) {
                i += this.m_endPadding;
            } else {
                int floor4 = (int) Math.floor(this.m_startPadding + (j * this.m_pixelPerMicrosecond) + 0.5d);
                if (floor4 < i) {
                    i = floor4;
                }
            }
            this.m_contentWidth = i;
            layout(0, 0, i, getHeight());
            requestLayout();
            if (getWidth() + getScrollX() <= this.m_contentWidth || (max = Math.max(getScrollX() - ((getWidth() + getScrollX()) - this.m_contentWidth), 0)) == getScrollX()) {
                return;
            }
            scrollTo(max, 0);
        }
    }

    public final void updateThumbnails() {
        boolean z;
        Drawable drawable;
        Bitmap bitmap;
        int i;
        boolean z2;
        if (this.m_thumbnailSequenceMap.isEmpty()) {
            clearThumbnails();
            return;
        }
        int i2 = this.m_maxThumbnailWidth;
        int scrollX = getScrollX();
        int width = getWidth();
        int max = Math.max(scrollX - i2, this.m_startPadding);
        int i3 = width + max + i2;
        if (i3 <= max) {
            clearThumbnails();
            return;
        }
        Integer floorKey = this.m_thumbnailSequenceMap.floorKey(Integer.valueOf(max));
        if (floorKey == null) {
            floorKey = this.m_thumbnailSequenceMap.firstKey();
        }
        int i4 = 0;
        for (Map.Entry<Integer, VideoSubClipInfo> entry : this.m_thumbnailSequenceMap.tailMap(floorKey).entrySet()) {
            VideoSubClipInfo value = entry.getValue();
            int i5 = value.m_x;
            int i6 = value.m_width;
            if (i5 + i6 >= max) {
                if (i5 >= i3) {
                    break;
                }
                if (i5 < max) {
                    int i7 = value.m_thumbnailWidth;
                    i = (((max - i5) / i7) * i7) + i5;
                } else {
                    i = i5;
                }
                int i8 = i5 + i6;
                while (true) {
                    if (i >= i8) {
                        z = true;
                        z2 = false;
                        break;
                    } else if (i >= i3) {
                        z2 = true;
                        z = true;
                        break;
                    } else {
                        int i9 = value.m_thumbnailWidth;
                        if (i + i9 > i8) {
                            i9 = i8 - i;
                        }
                        long calcTimestampFromX = value.calcTimestampFromX(i);
                        ThumbnailId thumbnailId = new ThumbnailId(value.m_index, calcTimestampFromX);
                        Thumbnail thumbnail = this.m_thumbnailMap.get(thumbnailId);
                        if (thumbnail == null) {
                            if (i4 <= 0 || i <= i4) {
                                i4 = i;
                            }
                            int i10 = value.m_thumbnailWidth + i;
                            Thumbnail thumbnail2 = new Thumbnail();
                            thumbnail2.m_owner = value;
                            thumbnail2.m_timestamp = calcTimestampFromX;
                            thumbnail2.m_imageViewUpToDate = false;
                            thumbnail2.m_touched = true;
                            this.m_thumbnailMap.put(thumbnailId, thumbnail2);
                            if (i9 == value.m_thumbnailWidth) {
                                thumbnail2.m_imageView = new ImageView(getContext());
                            } else {
                                thumbnail2.m_imageView = new ImageView(getContext());
                            }
                            int i11 = this.m_thumbnailImageFillMode;
                            if (i11 == 0) {
                                thumbnail2.m_imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            } else if (i11 == 1) {
                                thumbnail2.m_imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            }
                            addView(thumbnail2.m_imageView);
                            thumbnail2.m_imageView.layout(i4, 0, i10, getHeight());
                            i4 = i10;
                        } else {
                            thumbnail.m_touched = true;
                        }
                        i += i9;
                    }
                }
                if (z2) {
                    break;
                }
            }
        }
        z = true;
        this.m_updatingThumbnail = z;
        TreeMap treeMap = new TreeMap();
        Iterator<Map.Entry<ThumbnailId, Thumbnail>> it = this.m_thumbnailMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<ThumbnailId, Thumbnail> next = it.next();
            Thumbnail value2 = next.getValue();
            ImageView imageView = value2.m_imageView;
            if (imageView != null && (drawable = imageView.getDrawable()) != null && (bitmap = ((BitmapDrawable) drawable).getBitmap()) != null) {
                this.m_placeholderBitmap = bitmap;
            }
            if (!value2.m_touched) {
                removeView(value2.m_imageView);
                it.remove();
            } else {
                value2.m_touched = false;
                if (value2.m_imageViewUpToDate) {
                    treeMap.put(next.getKey(), ((BitmapDrawable) value2.m_imageView.getDrawable()).getBitmap());
                } else {
                    VideoSubClipInfo videoSubClipInfo = value2.m_owner;
                    this.mVideoFrameLoader.loadImage(value2.m_imageView, videoSubClipInfo.m_mediaFilePath, 120, videoSubClipInfo.m_stillImageHint ? 0L : value2.m_timestamp, this.m_placeholderBitmap);
                }
            }
        }
        this.m_updatingThumbnail = false;
    }

    public final void clearThumbnailSequences() {
        cancelIconTask();
        clearThumbnails();
        this.m_videoSubClipInfoArray.clear();
        this.m_thumbnailSequenceMap.clear();
        this.m_contentWidth = 0;
    }

    public final void clearThumbnails() {
        removeAllViewsInLayout();
        this.m_thumbnailMap.clear();
    }

    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        int i3 = this.m_contentWidth;
        int mode = View.MeasureSpec.getMode(i2);
        int size = View.MeasureSpec.getSize(i2);
        if (mode != 1073741824 && mode != Integer.MIN_VALUE) {
            size = getHeight();
        }
        setMeasuredDimension(ViewGroup.resolveSizeAndState(Math.max(i3, getSuggestedMinimumWidth()), i, 0), ViewGroup.resolveSizeAndState(Math.max(size, getSuggestedMinimumHeight()), i2, 0));
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        updateOutlinePath();
        updateThumbnails();
    }

    @Override // android.view.ViewGroup, android.view.View
    public void dispatchDraw(Canvas canvas) {
        canvas.clipPath(this.mContentOutlinePath);
        super.dispatchDraw(canvas);
    }

    public final void updateOutlinePath() {
        int i;
        int width = getWidth();
        int height = getHeight();
        this.mContentOutlinePath.reset();
        this.mContentOutlinePath.moveTo(this.m_startPadding + this.mCorner, 0.0f);
        this.mContentOutlinePath.lineTo((width - this.m_endPadding) - this.mCorner, 0.0f);
        Path path = this.mContentOutlinePath;
        int i2 = this.m_endPadding;
        path.quadTo(width - i2, 0.0f, width - i2, this.mCorner);
        this.mContentOutlinePath.lineTo(width - this.m_endPadding, height - this.mCorner);
        Path path2 = this.mContentOutlinePath;
        int i3 = this.m_endPadding;
        float f = height;
        path2.quadTo(width - i3, f, (width - i3) - this.mCorner, f);
        this.mContentOutlinePath.lineTo(this.m_startPadding + this.mCorner, f);
        Path path3 = this.mContentOutlinePath;
        int i4 = this.m_startPadding;
        path3.quadTo(i4, f, i4, height - this.mCorner);
        this.mContentOutlinePath.lineTo(this.m_startPadding, this.mCorner);
        this.mContentOutlinePath.quadTo(this.m_startPadding, 0.0f, i + this.mCorner, 0.0f);
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        if (i2 != i4) {
            requestUpdateThumbnailSequenceGeometry();
        }
        super.onSizeChanged(i, i2, i3, i4);
    }

    /* loaded from: classes2.dex */
    public static class Thumbnail {
        public long m_iconTaskId;
        public ImageView m_imageView;
        public boolean m_imageViewUpToDate;
        public VideoSubClipInfo m_owner;
        public long m_timestamp;
        public boolean m_touched;

        public Thumbnail() {
            this.m_timestamp = 0L;
            this.m_iconTaskId = 0L;
            this.m_imageViewUpToDate = false;
            this.m_touched = false;
        }
    }

    /* loaded from: classes2.dex */
    public static class ThumbnailId implements Comparable<ThumbnailId> {
        public int m_seqIndex;
        public long m_timestamp;

        public ThumbnailId(int i, long j) {
            this.m_seqIndex = i;
            this.m_timestamp = j;
        }

        @Override // java.lang.Comparable
        public int compareTo(ThumbnailId thumbnailId) {
            int i = this.m_seqIndex;
            int i2 = thumbnailId.m_seqIndex;
            if (i < i2) {
                return -1;
            }
            if (i > i2) {
                return 1;
            }
            long j = this.m_timestamp;
            long j2 = thumbnailId.m_timestamp;
            if (j < j2) {
                return -1;
            }
            return j > j2 ? 1 : 0;
        }
    }

    /* loaded from: classes2.dex */
    public static class VideoSubClipInfo {
        public int m_flags;
        public long m_inPoint;
        public int m_index;
        public String m_mediaFilePath;
        public boolean m_onlyDecodeKeyFrame;
        public long m_outPoint;
        public boolean m_stillImageHint;
        public float m_thumbnailAspectRatio;
        public int m_thumbnailWidth;
        public long m_trimDuration;
        public long m_trimIn;
        public int m_width;
        public int m_x;

        public VideoSubClipInfo() {
            this.m_index = 0;
            this.m_inPoint = 0L;
            this.m_outPoint = 0L;
            this.m_trimIn = 0L;
            this.m_trimDuration = 0L;
            this.m_stillImageHint = false;
            this.m_onlyDecodeKeyFrame = false;
            this.m_thumbnailAspectRatio = 0.0f;
            this.m_flags = 0;
            this.m_x = 0;
            this.m_width = 0;
            this.m_thumbnailWidth = 0;
        }

        public long calcTimestampFromX(int i) {
            return this.m_trimIn + ((long) Math.floor((((i - this.m_x) / this.m_width) * this.m_trimDuration) + 0.5d));
        }
    }
}
