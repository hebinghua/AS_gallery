package com.meicam.sdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import com.meicam.sdk.NvsIconGenerator;
import com.miui.gallery.search.statistics.SearchStatUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/* loaded from: classes.dex */
public class NvsMultiThumbnailSequenceView extends HorizontalScrollView implements NvsIconGenerator.IconCallback {
    private static final String TAG = "Meicam";
    public static final int THUMBNAIL_IMAGE_FILLMODE_ASPECTCROP = 1;
    public static final int THUMBNAIL_IMAGE_FILLMODE_STRETCH = 0;
    private static final int THUMBNAIL_SEQUENCE_FLAGS_CACHED_KEYFRAME_ONLY = 1;
    private static final int THUMBNAIL_SEQUENCE_FLAGS_CACHED_KEYFRAME_ONLY_VALID = 2;
    private ContentView m_contentView;
    private int m_contentWidth;
    private ArrayList<ThumbnailSequenceDesc> m_descArray;
    private int m_endPadding;
    private NvsIconGenerator m_iconGenerator;
    private int m_maxThumbnailWidth;
    private long m_maxTimelinePosToScroll;
    private double m_pixelPerMicrosecond;
    public Bitmap m_placeholderBitmap;
    private OnScrollChangeListener m_scrollChangeListener;
    private boolean m_scrollEnabled;
    private int m_startPadding;
    private float m_thumbnailAspectRatio;
    private int m_thumbnailImageFillMode;
    private TreeMap<ThumbnailId, Thumbnail> m_thumbnailMap;
    private ArrayList<ThumbnailSequence> m_thumbnailSequenceArray;
    private TreeMap<Integer, ThumbnailSequence> m_thumbnailSequenceMap;
    private boolean m_updatingThumbnail;

    /* loaded from: classes.dex */
    public interface OnScrollChangeListener {
        void onScrollChanged(NvsMultiThumbnailSequenceView nvsMultiThumbnailSequenceView, int i, int i2);
    }

    /* loaded from: classes.dex */
    public static class Thumbnail {
        public ImageView m_imageView;
        public ThumbnailSequence m_owner;
        public long m_timestamp = 0;
        public long m_iconTaskId = 0;
        public boolean m_imageViewUpToDate = false;
        public boolean m_touched = false;
    }

    /* loaded from: classes.dex */
    public static class ThumbnailSequenceDesc {
        public String mediaFilePath;
        public long inPoint = 0;
        public long outPoint = 4000000;
        public long trimIn = 0;
        public long trimOut = 4000000;
        public boolean stillImageHint = false;
        public boolean onlyDecodeKeyFrame = false;
        public float thumbnailAspectRatio = 0.0f;
    }

    /* loaded from: classes.dex */
    public static class ThumbnailSequence {
        public String m_mediaFilePath;
        public int m_index = 0;
        public long m_inPoint = 0;
        public long m_outPoint = 0;
        public long m_trimIn = 0;
        public long m_trimDuration = 0;
        public boolean m_stillImageHint = false;
        public boolean m_onlyDecodeKeyFrame = false;
        public float m_thumbnailAspectRatio = 0.0f;
        public int m_flags = 0;
        public int m_x = 0;
        public int m_width = 0;
        public int m_thumbnailWidth = 0;

        public long calcTimestampFromX(int i) {
            return this.m_trimIn + ((long) Math.floor((((i - this.m_x) / this.m_width) * this.m_trimDuration) + 0.5d));
        }
    }

    /* loaded from: classes.dex */
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

    /* loaded from: classes.dex */
    public class ContentView extends ViewGroup {
        @Override // android.view.ViewGroup
        public boolean shouldDelayChildPressedState() {
            return false;
        }

        public ContentView(Context context) {
            super(context);
        }

        @Override // android.view.View
        public void onMeasure(int i, int i2) {
            int i3 = NvsMultiThumbnailSequenceView.this.m_contentWidth;
            int mode = View.MeasureSpec.getMode(i2);
            int size = View.MeasureSpec.getSize(i2);
            if (mode != 1073741824 && mode != Integer.MIN_VALUE) {
                size = NvsMultiThumbnailSequenceView.this.getHeight();
            }
            setMeasuredDimension(ViewGroup.resolveSizeAndState(Math.max(i3, getSuggestedMinimumWidth()), i, 0), ViewGroup.resolveSizeAndState(Math.max(size, getSuggestedMinimumHeight()), i2, 0));
        }

        @Override // android.view.ViewGroup, android.view.View
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            NvsMultiThumbnailSequenceView.this.updateThumbnails();
        }

        @Override // android.view.View
        public void onSizeChanged(int i, int i2, int i3, int i4) {
            if (i2 != i4) {
                NvsMultiThumbnailSequenceView.this.requestUpdateThumbnailSequenceGeometry();
            }
            super.onSizeChanged(i, i2, i3, i4);
        }
    }

    public NvsMultiThumbnailSequenceView(Context context) {
        super(context);
        this.m_iconGenerator = null;
        this.m_scrollEnabled = true;
        this.m_thumbnailAspectRatio = 0.5625f;
        this.m_pixelPerMicrosecond = 7.2E-5d;
        this.m_startPadding = 0;
        this.m_endPadding = 0;
        this.m_thumbnailImageFillMode = 0;
        this.m_maxTimelinePosToScroll = 0L;
        this.m_thumbnailSequenceArray = new ArrayList<>();
        this.m_thumbnailSequenceMap = new TreeMap<>();
        this.m_contentWidth = 0;
        this.m_thumbnailMap = new TreeMap<>();
        this.m_maxThumbnailWidth = 0;
        this.m_updatingThumbnail = false;
        NvsUtils.checkFunctionInMainThread();
        init(context);
    }

    public NvsMultiThumbnailSequenceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.m_iconGenerator = null;
        this.m_scrollEnabled = true;
        this.m_thumbnailAspectRatio = 0.5625f;
        this.m_pixelPerMicrosecond = 7.2E-5d;
        this.m_startPadding = 0;
        this.m_endPadding = 0;
        this.m_thumbnailImageFillMode = 0;
        this.m_maxTimelinePosToScroll = 0L;
        this.m_thumbnailSequenceArray = new ArrayList<>();
        this.m_thumbnailSequenceMap = new TreeMap<>();
        this.m_contentWidth = 0;
        this.m_thumbnailMap = new TreeMap<>();
        this.m_maxThumbnailWidth = 0;
        this.m_updatingThumbnail = false;
        NvsUtils.checkFunctionInMainThread();
        init(context);
    }

    public NvsMultiThumbnailSequenceView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.m_iconGenerator = null;
        this.m_scrollEnabled = true;
        this.m_thumbnailAspectRatio = 0.5625f;
        this.m_pixelPerMicrosecond = 7.2E-5d;
        this.m_startPadding = 0;
        this.m_endPadding = 0;
        this.m_thumbnailImageFillMode = 0;
        this.m_maxTimelinePosToScroll = 0L;
        this.m_thumbnailSequenceArray = new ArrayList<>();
        this.m_thumbnailSequenceMap = new TreeMap<>();
        this.m_contentWidth = 0;
        this.m_thumbnailMap = new TreeMap<>();
        this.m_maxThumbnailWidth = 0;
        this.m_updatingThumbnail = false;
        NvsUtils.checkFunctionInMainThread();
        init(context);
    }

    public NvsMultiThumbnailSequenceView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.m_iconGenerator = null;
        this.m_scrollEnabled = true;
        this.m_thumbnailAspectRatio = 0.5625f;
        this.m_pixelPerMicrosecond = 7.2E-5d;
        this.m_startPadding = 0;
        this.m_endPadding = 0;
        this.m_thumbnailImageFillMode = 0;
        this.m_maxTimelinePosToScroll = 0L;
        this.m_thumbnailSequenceArray = new ArrayList<>();
        this.m_thumbnailSequenceMap = new TreeMap<>();
        this.m_contentWidth = 0;
        this.m_thumbnailMap = new TreeMap<>();
        this.m_maxThumbnailWidth = 0;
        this.m_updatingThumbnail = false;
        NvsUtils.checkFunctionInMainThread();
        init(context);
    }

    public void setThumbnailSequenceDescArray(ArrayList<ThumbnailSequenceDesc> arrayList) {
        NvsUtils.checkFunctionInMainThread();
        if (arrayList == this.m_descArray) {
            return;
        }
        clearThumbnailSequences();
        this.m_placeholderBitmap = null;
        this.m_descArray = arrayList;
        if (arrayList != null) {
            int i = 0;
            Iterator<ThumbnailSequenceDesc> it = arrayList.iterator();
            long j = 0;
            while (it.hasNext()) {
                ThumbnailSequenceDesc next = it.next();
                if (next.mediaFilePath != null) {
                    long j2 = next.inPoint;
                    if (j2 >= j && next.outPoint > j2) {
                        long j3 = next.trimIn;
                        if (j3 >= 0 && next.trimOut > j3) {
                            ThumbnailSequence thumbnailSequence = new ThumbnailSequence();
                            thumbnailSequence.m_index = i;
                            thumbnailSequence.m_mediaFilePath = next.mediaFilePath;
                            thumbnailSequence.m_inPoint = next.inPoint;
                            thumbnailSequence.m_outPoint = next.outPoint;
                            long j4 = next.trimIn;
                            thumbnailSequence.m_trimIn = j4;
                            thumbnailSequence.m_trimDuration = next.trimOut - j4;
                            thumbnailSequence.m_stillImageHint = next.stillImageHint;
                            thumbnailSequence.m_onlyDecodeKeyFrame = next.onlyDecodeKeyFrame;
                            thumbnailSequence.m_thumbnailAspectRatio = next.thumbnailAspectRatio;
                            this.m_thumbnailSequenceArray.add(thumbnailSequence);
                            i++;
                            j = next.outPoint;
                        }
                    }
                }
                Log.e(TAG, "Invalid ThumbnailSequenceDesc!");
            }
        }
        updateThumbnailSequenceGeometry();
    }

    public ArrayList<ThumbnailSequenceDesc> getThumbnailSequenceDescArray() {
        return this.m_descArray;
    }

    public void setThumbnailImageFillMode(int i) {
        NvsUtils.checkFunctionInMainThread();
        int i2 = this.m_thumbnailImageFillMode;
        if (i2 != 1 && i2 != 0) {
            this.m_thumbnailImageFillMode = 0;
        }
        if (this.m_thumbnailImageFillMode == i) {
            return;
        }
        this.m_thumbnailImageFillMode = i;
        updateThumbnailSequenceGeometry();
    }

    public int getThumbnailImageFillMode() {
        return this.m_thumbnailImageFillMode;
    }

    public void setThumbnailAspectRatio(float f) {
        NvsUtils.checkFunctionInMainThread();
        if (f < 0.1f) {
            f = 0.1f;
        } else if (f > 10.0f) {
            f = 10.0f;
        }
        if (Math.abs(this.m_thumbnailAspectRatio - f) < 0.001f) {
            return;
        }
        this.m_thumbnailAspectRatio = f;
        updateThumbnailSequenceGeometry();
    }

    public float getThumbnailAspectRatio() {
        return this.m_thumbnailAspectRatio;
    }

    public void setPixelPerMicrosecond(double d) {
        NvsUtils.checkFunctionInMainThread();
        if (d <= SearchStatUtils.POW || d == this.m_pixelPerMicrosecond) {
            return;
        }
        this.m_pixelPerMicrosecond = d;
        updateThumbnailSequenceGeometry();
    }

    public double getPixelPerMicrosecond() {
        return this.m_pixelPerMicrosecond;
    }

    public void setStartPadding(int i) {
        NvsUtils.checkFunctionInMainThread();
        if (i < 0 || i == this.m_startPadding) {
            return;
        }
        this.m_startPadding = i;
        updateThumbnailSequenceGeometry();
    }

    public int getStartPadding() {
        return this.m_startPadding;
    }

    public void setEndPadding(int i) {
        NvsUtils.checkFunctionInMainThread();
        if (i < 0 || i == this.m_endPadding) {
            return;
        }
        this.m_endPadding = i;
        updateThumbnailSequenceGeometry();
    }

    public int getEndPadding() {
        return this.m_endPadding;
    }

    public void setMaxTimelinePosToScroll(int i) {
        NvsUtils.checkFunctionInMainThread();
        long max = Math.max(i, 0);
        if (max == this.m_maxTimelinePosToScroll) {
            return;
        }
        this.m_maxTimelinePosToScroll = max;
        updateThumbnailSequenceGeometry();
    }

    public long getMaxTimelinePosToScroll() {
        return this.m_maxTimelinePosToScroll;
    }

    public long mapTimelinePosFromX(int i) {
        NvsUtils.checkFunctionInMainThread();
        return (long) Math.floor((((i + getScrollX()) - this.m_startPadding) / this.m_pixelPerMicrosecond) + 0.5d);
    }

    public int mapXFromTimelinePos(long j) {
        NvsUtils.checkFunctionInMainThread();
        return (((int) Math.floor((j * this.m_pixelPerMicrosecond) + 0.5d)) + this.m_startPadding) - getScrollX();
    }

    public void scaleWithAnchor(double d, int i) {
        NvsUtils.checkFunctionInMainThread();
        if (d <= SearchStatUtils.POW) {
            return;
        }
        long mapTimelinePosFromX = mapTimelinePosFromX(i);
        this.m_pixelPerMicrosecond *= d;
        updateThumbnailSequenceGeometry();
        scrollTo((getScrollX() + mapXFromTimelinePos(mapTimelinePosFromX)) - i, 0);
    }

    public void setOnScrollChangeListenser(OnScrollChangeListener onScrollChangeListener) {
        NvsUtils.checkFunctionInMainThread();
        this.m_scrollChangeListener = onScrollChangeListener;
    }

    public OnScrollChangeListener getOnScrollChangeListenser() {
        NvsUtils.checkFunctionInMainThread();
        return this.m_scrollChangeListener;
    }

    public void setScrollEnabled(boolean z) {
        this.m_scrollEnabled = z;
    }

    public boolean getScrollEnabled() {
        return this.m_scrollEnabled;
    }

    @Override // android.widget.HorizontalScrollView, android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
    }

    @Override // android.widget.HorizontalScrollView, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            NvsIconGenerator nvsIconGenerator = new NvsIconGenerator();
            this.m_iconGenerator = nvsIconGenerator;
            nvsIconGenerator.setIconCallback(this);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        cancelIconTask();
        this.m_scrollChangeListener = null;
        NvsIconGenerator nvsIconGenerator = this.m_iconGenerator;
        if (nvsIconGenerator != null) {
            nvsIconGenerator.release();
            this.m_iconGenerator = null;
        }
        super.onDetachedFromWindow();
    }

    @Override // android.view.View
    public void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
        OnScrollChangeListener onScrollChangeListener = this.m_scrollChangeListener;
        if (onScrollChangeListener != null) {
            onScrollChangeListener.onScrollChanged(this, i, i3);
        }
        updateThumbnails();
    }

    @Override // android.widget.HorizontalScrollView, android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (this.m_scrollEnabled) {
            return super.onInterceptTouchEvent(motionEvent);
        }
        return false;
    }

    @Override // android.widget.HorizontalScrollView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.m_scrollEnabled) {
            return super.onTouchEvent(motionEvent);
        }
        return false;
    }

    private void init(Context context) {
        setVerticalScrollBarEnabled(false);
        setHorizontalScrollBarEnabled(false);
        ContentView contentView = new ContentView(context);
        this.m_contentView = contentView;
        addView(contentView, new FrameLayout.LayoutParams(-2, -1));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void requestUpdateThumbnailSequenceGeometry() {
        new Handler().post(new Runnable() { // from class: com.meicam.sdk.NvsMultiThumbnailSequenceView.1
            @Override // java.lang.Runnable
            public void run() {
                NvsMultiThumbnailSequenceView.this.updateThumbnailSequenceGeometry();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateThumbnailSequenceGeometry() {
        int height;
        int max;
        cancelIconTask();
        clearThumbnails();
        if (getHeight() == 0) {
            return;
        }
        this.m_thumbnailSequenceMap.clear();
        int i = this.m_startPadding;
        this.m_maxThumbnailWidth = 0;
        Iterator<ThumbnailSequence> it = this.m_thumbnailSequenceArray.iterator();
        while (it.hasNext()) {
            ThumbnailSequence next = it.next();
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
        this.m_contentView.layout(0, 0, i, getHeight());
        this.m_contentView.requestLayout();
        if (getWidth() + getScrollX() <= this.m_contentWidth || (max = Math.max(getScrollX() - ((getWidth() + getScrollX()) - this.m_contentWidth), 0)) == getScrollX()) {
            return;
        }
        scrollTo(max, 0);
    }

    /* loaded from: classes.dex */
    public static class ClipImageView extends ImageView {
        private int m_clipWidth;

        public ClipImageView(Context context, int i) {
            super(context);
            this.m_clipWidth = i;
        }

        @Override // android.widget.ImageView, android.view.View
        public void onDraw(Canvas canvas) {
            canvas.clipRect(new Rect(0, 0, this.m_clipWidth, getHeight()));
            super.onDraw(canvas);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateThumbnails() {
        Drawable drawable;
        Bitmap bitmap;
        int i;
        boolean z;
        if (this.m_iconGenerator == null) {
            return;
        }
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
        for (Map.Entry<Integer, ThumbnailSequence> entry : this.m_thumbnailSequenceMap.tailMap(floorKey).entrySet()) {
            ThumbnailSequence value = entry.getValue();
            int i4 = value.m_x;
            int i5 = value.m_width;
            if (i4 + i5 >= max) {
                if (i4 >= i3) {
                    break;
                }
                if (i4 < max) {
                    int i6 = value.m_thumbnailWidth;
                    i = (((max - i4) / i6) * i6) + i4;
                } else {
                    i = i4;
                }
                int i7 = i4 + i5;
                while (true) {
                    if (i >= i7) {
                        z = false;
                        break;
                    } else if (i >= i3) {
                        z = true;
                        break;
                    } else {
                        int i8 = value.m_thumbnailWidth;
                        if (i + i8 > i7) {
                            i8 = i7 - i;
                        }
                        long calcTimestampFromX = value.calcTimestampFromX(i);
                        ThumbnailId thumbnailId = new ThumbnailId(value.m_index, calcTimestampFromX);
                        Thumbnail thumbnail = this.m_thumbnailMap.get(thumbnailId);
                        if (thumbnail == null) {
                            Thumbnail thumbnail2 = new Thumbnail();
                            thumbnail2.m_owner = value;
                            thumbnail2.m_timestamp = calcTimestampFromX;
                            thumbnail2.m_imageViewUpToDate = false;
                            thumbnail2.m_touched = true;
                            this.m_thumbnailMap.put(thumbnailId, thumbnail2);
                            if (i8 == value.m_thumbnailWidth) {
                                thumbnail2.m_imageView = new ImageView(getContext());
                            } else {
                                thumbnail2.m_imageView = new ClipImageView(getContext(), i8);
                            }
                            int i9 = this.m_thumbnailImageFillMode;
                            if (i9 == 0) {
                                thumbnail2.m_imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            } else if (i9 == 1) {
                                thumbnail2.m_imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            }
                            this.m_contentView.addView(thumbnail2.m_imageView);
                            thumbnail2.m_imageView.layout(i, 0, value.m_thumbnailWidth + i, this.m_contentView.getHeight());
                        } else {
                            thumbnail.m_touched = true;
                        }
                        i += i8;
                    }
                }
                if (z) {
                    break;
                }
            }
        }
        this.m_updatingThumbnail = true;
        TreeMap treeMap = new TreeMap();
        Iterator<Map.Entry<ThumbnailId, Thumbnail>> it = this.m_thumbnailMap.entrySet().iterator();
        boolean z2 = false;
        while (it.hasNext()) {
            Map.Entry<ThumbnailId, Thumbnail> next = it.next();
            Thumbnail value2 = next.getValue();
            ImageView imageView = value2.m_imageView;
            if (imageView != null && (drawable = imageView.getDrawable()) != null && (bitmap = ((BitmapDrawable) drawable).getBitmap()) != null) {
                this.m_placeholderBitmap = bitmap;
            }
            if (!value2.m_touched) {
                long j = value2.m_iconTaskId;
                if (j != 0) {
                    this.m_iconGenerator.cancelTask(j);
                }
                this.m_contentView.removeView(value2.m_imageView);
                it.remove();
            } else {
                value2.m_touched = false;
                if (value2.m_imageViewUpToDate) {
                    treeMap.put(next.getKey(), ((BitmapDrawable) value2.m_imageView.getDrawable()).getBitmap());
                } else {
                    ThumbnailSequence thumbnailSequence = value2.m_owner;
                    long j2 = thumbnailSequence.m_stillImageHint ? 0L : value2.m_timestamp;
                    updateKeyframeOnlyModeForThumbnailSequence(thumbnailSequence);
                    ThumbnailSequence thumbnailSequence2 = value2.m_owner;
                    int i10 = (thumbnailSequence2.m_flags & 1) != 0 ? 1 : 0;
                    Bitmap iconFromCache = this.m_iconGenerator.getIconFromCache(thumbnailSequence2.m_mediaFilePath, j2, i10);
                    if (iconFromCache != null) {
                        treeMap.put(next.getKey(), iconFromCache);
                        if (setBitmapToThumbnail(iconFromCache, value2)) {
                            value2.m_imageViewUpToDate = true;
                            value2.m_iconTaskId = 0L;
                        }
                    } else {
                        value2.m_iconTaskId = this.m_iconGenerator.getIcon(value2.m_owner.m_mediaFilePath, j2, i10);
                        z2 = true;
                    }
                }
            }
        }
        this.m_updatingThumbnail = false;
        if (!z2) {
            return;
        }
        if (treeMap.isEmpty()) {
            if (this.m_placeholderBitmap == null) {
                return;
            }
            for (Map.Entry<ThumbnailId, Thumbnail> entry2 : this.m_thumbnailMap.entrySet()) {
                Thumbnail value3 = entry2.getValue();
                if (!value3.m_imageViewUpToDate) {
                    setBitmapToThumbnail(this.m_placeholderBitmap, value3);
                }
            }
            return;
        }
        for (Map.Entry<ThumbnailId, Thumbnail> entry3 : this.m_thumbnailMap.entrySet()) {
            Thumbnail value4 = entry3.getValue();
            if (!value4.m_imageViewUpToDate) {
                Map.Entry ceilingEntry = treeMap.ceilingEntry(entry3.getKey());
                if (ceilingEntry != null) {
                    setBitmapToThumbnail((Bitmap) ceilingEntry.getValue(), value4);
                } else {
                    setBitmapToThumbnail((Bitmap) treeMap.lastEntry().getValue(), value4);
                }
            }
        }
    }

    private void updateKeyframeOnlyModeForThumbnailSequence(ThumbnailSequence thumbnailSequence) {
        int i = thumbnailSequence.m_flags;
        if ((i & 2) != 0) {
            return;
        }
        if (thumbnailSequence.m_onlyDecodeKeyFrame) {
            thumbnailSequence.m_flags = i | 3;
            return;
        }
        if (shouldDecodecKeyFrameOnly(thumbnailSequence.m_mediaFilePath, Math.max((long) ((thumbnailSequence.m_thumbnailWidth / this.m_pixelPerMicrosecond) + 0.5d), 1L))) {
            thumbnailSequence.m_flags |= 1;
        } else {
            thumbnailSequence.m_flags &= -2;
        }
        thumbnailSequence.m_flags |= 2;
    }

    private boolean shouldDecodecKeyFrameOnly(String str, long j) {
        NvsAVFileInfo aVFileInfo;
        NvsRational videoStreamFrameRate;
        NvsStreamingContext nvsStreamingContext = NvsStreamingContext.getInstance();
        if (nvsStreamingContext != null && (aVFileInfo = nvsStreamingContext.getAVFileInfo(str)) != null && aVFileInfo.getVideoStreamCount() >= 1 && (videoStreamFrameRate = aVFileInfo.getVideoStreamFrameRate(0)) != null && videoStreamFrameRate.den > 0 && videoStreamFrameRate.num > 0 && aVFileInfo.getVideoStreamDuration(0) >= j) {
            int detectVideoFileKeyframeInterval = nvsStreamingContext.detectVideoFileKeyframeInterval(str);
            if (detectVideoFileKeyframeInterval == 0) {
                detectVideoFileKeyframeInterval = 30;
            } else if (detectVideoFileKeyframeInterval == 1) {
                return false;
            }
            int i = (int) (detectVideoFileKeyframeInterval * (videoStreamFrameRate.den / videoStreamFrameRate.num) * 1000000.0d);
            if (detectVideoFileKeyframeInterval <= 30) {
                if (j > i * 0.9d) {
                    return true;
                }
            } else if (detectVideoFileKeyframeInterval <= 60) {
                if (j > i * 0.8d) {
                    return true;
                }
            } else if (detectVideoFileKeyframeInterval <= 100) {
                if (j > i * 0.7d) {
                    return true;
                }
            } else if (detectVideoFileKeyframeInterval <= 150) {
                if (j > i * 0.5d) {
                    return true;
                }
            } else if (detectVideoFileKeyframeInterval <= 250) {
                if (j > i * 0.3d) {
                    return true;
                }
            } else if (j > i * 0.2d) {
                return true;
            }
            return false;
        }
        return false;
    }

    private boolean setBitmapToThumbnail(Bitmap bitmap, Thumbnail thumbnail) {
        ImageView imageView;
        if (bitmap == null || (imageView = thumbnail.m_imageView) == null) {
            return false;
        }
        imageView.setImageBitmap(bitmap);
        return true;
    }

    private void clearThumbnailSequences() {
        cancelIconTask();
        clearThumbnails();
        this.m_thumbnailSequenceArray.clear();
        this.m_thumbnailSequenceMap.clear();
        this.m_contentWidth = 0;
    }

    private void clearThumbnails() {
        for (Map.Entry<ThumbnailId, Thumbnail> entry : this.m_thumbnailMap.entrySet()) {
            this.m_contentView.removeView(entry.getValue().m_imageView);
        }
        this.m_thumbnailMap.clear();
    }

    private void cancelIconTask() {
        NvsIconGenerator nvsIconGenerator = this.m_iconGenerator;
        if (nvsIconGenerator != null) {
            nvsIconGenerator.cancelTask(0L);
        }
    }

    @Override // com.meicam.sdk.NvsIconGenerator.IconCallback
    public void onIconReady(Bitmap bitmap, long j, long j2) {
        if (!this.m_updatingThumbnail) {
            updateThumbnails();
        } else {
            new Handler().post(new Runnable() { // from class: com.meicam.sdk.NvsMultiThumbnailSequenceView.2
                @Override // java.lang.Runnable
                public void run() {
                    NvsMultiThumbnailSequenceView.this.updateThumbnails();
                }
            });
        }
    }
}
