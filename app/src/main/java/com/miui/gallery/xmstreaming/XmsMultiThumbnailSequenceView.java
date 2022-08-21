package com.miui.gallery.xmstreaming;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.miui.gallery.search.statistics.SearchStatUtils;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/* loaded from: classes3.dex */
public abstract class XmsMultiThumbnailSequenceView extends HorizontalScrollView {
    private static final String TAG = "XmsMultiThumbnailSequenceView";
    public static final int THUMBNAIL_IMAGE_FILLMODE_ASPECTCROP = 1;
    public static final int THUMBNAIL_IMAGE_FILLMODE_STRETCH = 0;
    private ContentView m_contentView;
    private int m_contentWidth;
    private ArrayList<ThumbnailSequenceDesc> m_descArray;
    private int m_endPadding;
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
    private int m_thumbnailWidth;
    private boolean m_updatingThumbnail;

    /* loaded from: classes3.dex */
    public interface OnScrollChangeListener {
        void onScrollChanged(XmsMultiThumbnailSequenceView xmsMultiThumbnailSequenceView, int i, int i2);
    }

    /* loaded from: classes3.dex */
    public static class Thumbnail {
        public ImageView m_imageView;
        public ThumbnailSequence m_owner;
        public long m_timestamp = 0;
        public long m_iconTaskId = 0;
        public boolean m_imageViewUpToDate = false;
        public boolean m_touched = false;
    }

    /* loaded from: classes3.dex */
    public static class ThumbnailSequenceDesc {
        public String mediaFilePath;
        public long inPoint = 0;
        public long outPoint = 4000000;
        public long trimIn = 0;
        public long trimOut = 4000000;
        public boolean stillImageHint = false;
    }

    public abstract void nativeCancelIconTask(long j);

    public abstract long nativeGetIcon(String str, long j);

    public abstract Bitmap nativeGetIconFromCache(String str, long j);

    public XmsMultiThumbnailSequenceView(Context context) {
        super(context);
        this.m_scrollEnabled = true;
        this.m_thumbnailAspectRatio = 0.5625f;
        this.m_pixelPerMicrosecond = 5.4E-5d;
        this.m_startPadding = 0;
        this.m_endPadding = 0;
        this.m_thumbnailImageFillMode = 0;
        this.m_maxTimelinePosToScroll = 0L;
        this.m_thumbnailSequenceArray = new ArrayList<>();
        this.m_thumbnailSequenceMap = new TreeMap<>();
        this.m_contentWidth = 0;
        this.m_thumbnailMap = new TreeMap<>();
        this.m_thumbnailWidth = 0;
        this.m_updatingThumbnail = false;
        init(context);
    }

    public XmsMultiThumbnailSequenceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.m_scrollEnabled = true;
        this.m_thumbnailAspectRatio = 0.5625f;
        this.m_pixelPerMicrosecond = 5.4E-5d;
        this.m_startPadding = 0;
        this.m_endPadding = 0;
        this.m_thumbnailImageFillMode = 0;
        this.m_maxTimelinePosToScroll = 0L;
        this.m_thumbnailSequenceArray = new ArrayList<>();
        this.m_thumbnailSequenceMap = new TreeMap<>();
        this.m_contentWidth = 0;
        this.m_thumbnailMap = new TreeMap<>();
        this.m_thumbnailWidth = 0;
        this.m_updatingThumbnail = false;
        init(context);
    }

    public XmsMultiThumbnailSequenceView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.m_scrollEnabled = true;
        this.m_thumbnailAspectRatio = 0.5625f;
        this.m_pixelPerMicrosecond = 5.4E-5d;
        this.m_startPadding = 0;
        this.m_endPadding = 0;
        this.m_thumbnailImageFillMode = 0;
        this.m_maxTimelinePosToScroll = 0L;
        this.m_thumbnailSequenceArray = new ArrayList<>();
        this.m_thumbnailSequenceMap = new TreeMap<>();
        this.m_contentWidth = 0;
        this.m_thumbnailMap = new TreeMap<>();
        this.m_thumbnailWidth = 0;
        this.m_updatingThumbnail = false;
        init(context);
    }

    public XmsMultiThumbnailSequenceView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.m_scrollEnabled = true;
        this.m_thumbnailAspectRatio = 0.5625f;
        this.m_pixelPerMicrosecond = 5.4E-5d;
        this.m_startPadding = 0;
        this.m_endPadding = 0;
        this.m_thumbnailImageFillMode = 0;
        this.m_maxTimelinePosToScroll = 0L;
        this.m_thumbnailSequenceArray = new ArrayList<>();
        this.m_thumbnailSequenceMap = new TreeMap<>();
        this.m_contentWidth = 0;
        this.m_thumbnailMap = new TreeMap<>();
        this.m_thumbnailWidth = 0;
        this.m_updatingThumbnail = false;
        init(context);
    }

    public void setThumbnailSequenceDescArray(ArrayList<ThumbnailSequenceDesc> arrayList) {
        if (arrayList != this.m_descArray) {
            clearThumbnailSequences();
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
    }

    public ArrayList<ThumbnailSequenceDesc> getThumbnailSequenceDescArray() {
        return this.m_descArray;
    }

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

    public long mapTimelinePosFromX(int i) {
        return (long) Math.floor((((i + getScrollX()) - this.m_startPadding) / this.m_pixelPerMicrosecond) + 0.5d);
    }

    public int mapXFromTimelinePos(long j) {
        return (((int) Math.floor((j * this.m_pixelPerMicrosecond) + 0.5d)) + this.m_startPadding) - getScrollX();
    }

    public void scaleWithAnchor(double d, int i) {
        if (d > SearchStatUtils.POW) {
            long mapTimelinePosFromX = mapTimelinePosFromX(i);
            this.m_pixelPerMicrosecond *= d;
            updateThumbnailSequenceGeometry();
            scrollTo((getScrollX() + mapXFromTimelinePos(mapTimelinePosFromX)) - i, 0);
        }
    }

    public void setOnScrollChangeListenser(OnScrollChangeListener onScrollChangeListener) {
        this.m_scrollChangeListener = onScrollChangeListener;
    }

    public OnScrollChangeListener getOnScrollChangeListenser() {
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

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        cancelIconTask();
        this.m_scrollChangeListener = null;
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
        new Handler().post(new Runnable() { // from class: com.miui.gallery.xmstreaming.XmsMultiThumbnailSequenceView.1
            @Override // java.lang.Runnable
            public void run() {
                XmsMultiThumbnailSequenceView.this.updateThumbnailSequenceGeometry();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateThumbnailSequenceGeometry() {
        int max;
        cancelIconTask();
        clearThumbnails();
        int height = getHeight();
        if (height != 0) {
            int floor = (int) Math.floor((height * this.m_thumbnailAspectRatio) + 0.5d);
            this.m_thumbnailWidth = floor;
            this.m_thumbnailWidth = Math.max(floor, 1);
            this.m_thumbnailSequenceMap.clear();
            int i = this.m_startPadding;
            Iterator<ThumbnailSequence> it = this.m_thumbnailSequenceArray.iterator();
            while (it.hasNext()) {
                ThumbnailSequence next = it.next();
                int floor2 = ((int) Math.floor((next.m_inPoint * this.m_pixelPerMicrosecond) + 0.5d)) + this.m_startPadding;
                int floor3 = ((int) Math.floor((next.m_outPoint * this.m_pixelPerMicrosecond) + 0.5d)) + this.m_startPadding;
                if (floor3 > floor2) {
                    next.m_x = floor2;
                    next.m_width = floor3 - floor2;
                    this.m_thumbnailSequenceMap.put(Integer.valueOf(floor2), next);
                    i = floor3;
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
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void updateThumbnails() {
        Drawable drawable;
        Bitmap bitmap;
        int i;
        boolean z;
        if (this.m_thumbnailSequenceMap.isEmpty()) {
            clearThumbnails();
            return;
        }
        int i2 = this.m_thumbnailWidth;
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
                    int i6 = this.m_thumbnailWidth;
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
                        int i8 = this.m_thumbnailWidth;
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
                            ImageView imageView = new ImageView(getContext());
                            thumbnail2.m_imageView = imageView;
                            int i9 = this.m_thumbnailImageFillMode;
                            if (i9 == 0) {
                                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                            } else if (i9 == 1) {
                                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            }
                            this.m_contentView.addView(thumbnail2.m_imageView);
                            thumbnail2.m_imageView.layout(i, 0, i + i8, this.m_contentView.getHeight());
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
            ImageView imageView2 = value2.m_imageView;
            if (imageView2 != null && (drawable = imageView2.getDrawable()) != null && (bitmap = ((BitmapDrawable) drawable).getBitmap()) != null) {
                this.m_placeholderBitmap = bitmap;
            }
            if (!value2.m_touched) {
                long j = value2.m_iconTaskId;
                if (j != 0) {
                    nativeCancelIconTask(j);
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
                    Bitmap nativeGetIconFromCache = nativeGetIconFromCache(thumbnailSequence.m_mediaFilePath, j2);
                    if (nativeGetIconFromCache != null) {
                        treeMap.put(next.getKey(), nativeGetIconFromCache);
                        if (setBitmapToImageView(nativeGetIconFromCache, value2.m_imageView)) {
                            value2.m_imageViewUpToDate = true;
                            value2.m_iconTaskId = 0L;
                        }
                    } else {
                        value2.m_iconTaskId = nativeGetIcon(value2.m_owner.m_mediaFilePath, j2);
                        z2 = true;
                    }
                }
            }
        }
        this.m_updatingThumbnail = false;
        if (!z2) {
            return;
        }
        if (!treeMap.isEmpty()) {
            for (Map.Entry<ThumbnailId, Thumbnail> entry2 : this.m_thumbnailMap.entrySet()) {
                Thumbnail value3 = entry2.getValue();
                if (!value3.m_imageViewUpToDate) {
                    Map.Entry ceilingEntry = treeMap.ceilingEntry(entry2.getKey());
                    if (ceilingEntry != null) {
                        setBitmapToImageView((Bitmap) ceilingEntry.getValue(), value3.m_imageView);
                    } else {
                        setBitmapToImageView((Bitmap) treeMap.lastEntry().getValue(), value3.m_imageView);
                    }
                }
            }
        } else if (this.m_placeholderBitmap != null) {
            for (Map.Entry<ThumbnailId, Thumbnail> entry3 : this.m_thumbnailMap.entrySet()) {
                Thumbnail value4 = entry3.getValue();
                if (!value4.m_imageViewUpToDate) {
                    setBitmapToImageView(this.m_placeholderBitmap, value4.m_imageView);
                }
            }
        }
    }

    private boolean setBitmapToImageView(Bitmap bitmap, ImageView imageView) {
        if (bitmap == null || imageView == null) {
            return false;
        }
        Log.d(TAG, "setBitmapToImageView");
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
        if (!isInEditMode()) {
            nativeCancelIconTask(0L);
        }
    }

    public void notifyIconArrived() {
        if (!this.m_updatingThumbnail) {
            updateThumbnails();
        } else {
            new Handler().post(new Runnable() { // from class: com.miui.gallery.xmstreaming.XmsMultiThumbnailSequenceView.2
                @Override // java.lang.Runnable
                public void run() {
                    XmsMultiThumbnailSequenceView.this.updateThumbnails();
                }
            });
        }
    }

    /* loaded from: classes3.dex */
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
            int i3 = XmsMultiThumbnailSequenceView.this.m_contentWidth;
            int mode = View.MeasureSpec.getMode(i2);
            int size = View.MeasureSpec.getSize(i2);
            if (mode != 1073741824 && mode != Integer.MIN_VALUE) {
                size = XmsMultiThumbnailSequenceView.this.getHeight();
            }
            setMeasuredDimension(ViewGroup.resolveSizeAndState(Math.max(i3, getSuggestedMinimumWidth()), i, 0), ViewGroup.resolveSizeAndState(Math.max(size, getSuggestedMinimumHeight()), i2, 0));
        }

        @Override // android.view.ViewGroup, android.view.View
        public void onLayout(boolean z, int i, int i2, int i3, int i4) {
            XmsMultiThumbnailSequenceView.this.updateThumbnails();
        }

        @Override // android.view.View
        public void onSizeChanged(int i, int i2, int i3, int i4) {
            if (i2 != i4) {
                XmsMultiThumbnailSequenceView.this.requestUpdateThumbnailSequenceGeometry();
            }
            super.onSizeChanged(i, i2, i3, i4);
        }
    }

    /* loaded from: classes3.dex */
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

    /* loaded from: classes3.dex */
    public static class ThumbnailSequence {
        public String m_mediaFilePath;
        public int m_index = 0;
        public long m_inPoint = 0;
        public long m_outPoint = 0;
        public long m_trimIn = 0;
        public long m_trimDuration = 0;
        public boolean m_stillImageHint = false;
        public int m_x = 0;
        public int m_width = 0;

        public long calcTimestampFromX(int i) {
            return this.m_trimIn + ((long) Math.floor((((i - this.m_x) / this.m_width) * this.m_trimDuration) + 0.5d));
        }
    }
}
