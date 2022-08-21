package com.miui.gallery.vlog.clip.single.seekbar;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.sdk.interfaces.VideoFrameLoader;

/* loaded from: classes2.dex */
public class VideoBar extends Drawable {
    public float mAspectRatio;
    public int mBottom;
    public int mEndTime;
    public int mItemTimeLength;
    public int mItemWidth;
    public int mLeft;
    public Listener mListener;
    public String mPath;
    public int mRight;
    public int mStartTime;
    public int mTop;
    public int mVideoBarHeight;
    public int mVideoBarWidth;
    public VideoFrameLoader mVideoFrameLoader;
    public Rect mDrawingRect = new Rect();
    public ExtractFrameStrategy mStrategy = new ExtractFrameStrategy();

    /* loaded from: classes2.dex */
    public interface Listener {
        void onLoadFrameFinished(Bitmap bitmap);
    }

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -3;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    public void updateSpeed(double d) {
        this.mStrategy.setSpeed(d);
    }

    public VideoBar(Listener listener) {
        this.mListener = listener;
    }

    public void setVisibleArea(int i, int i2, int i3, int i4) {
        this.mLeft = i;
        this.mTop = i2;
        this.mRight = i3;
        this.mBottom = i4;
        this.mVideoBarWidth = i3 - i;
        int i5 = i4 - i2;
        this.mVideoBarHeight = i5;
        int round = Math.round(i5 * this.mAspectRatio);
        this.mItemWidth = round;
        this.mItemTimeLength = convertDistanceToTimeLength(round);
    }

    public void setStartTime(int i) {
        this.mStartTime = i;
        this.mItemTimeLength = convertDistanceToTimeLength(this.mItemWidth);
    }

    public void setEndTime(int i) {
        this.mEndTime = i;
        this.mItemTimeLength = convertDistanceToTimeLength(this.mItemWidth);
    }

    public void setTotalTime(int i) {
        this.mStrategy.setTotalTimeMillis(i);
    }

    public void setFrameLoader(VideoFrameLoader videoFrameLoader) {
        this.mVideoFrameLoader = videoFrameLoader;
    }

    public void setVideoPath(String str) {
        this.mPath = str;
    }

    public void setAspectRatio(float f) {
        this.mAspectRatio = f;
        this.mItemTimeLength = convertDistanceToTimeLength(this.mVideoBarHeight * f);
        this.mItemWidth = Math.round(this.mVideoBarHeight * this.mAspectRatio);
    }

    public int getWidth() {
        return this.mVideoBarWidth;
    }

    public int getLeft() {
        return this.mLeft;
    }

    public int getRight() {
        return this.mRight;
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        if (this.mItemTimeLength == 0 || this.mVideoFrameLoader == null) {
            return;
        }
        canvas.save();
        canvas.clipRect(this.mLeft, this.mTop, this.mRight, this.mBottom);
        int i = 0;
        while (true) {
            int i2 = this.mItemTimeLength;
            int i3 = i2 * i;
            int i4 = i2 + i3;
            if (i4 > this.mStartTime) {
                if (i3 < this.mEndTime) {
                    this.mDrawingRect.left = Math.round(convertTimeToXCoordinate(i3));
                    this.mDrawingRect.right = Math.round(convertTimeToXCoordinate(i4));
                    Rect rect = this.mDrawingRect;
                    rect.top = this.mTop;
                    rect.bottom = this.mBottom;
                    Bitmap extractFrame = this.mVideoFrameLoader.extractFrame(this.mPath, 1000 * this.mStrategy.adjustTime(i3), this.mItemWidth, this.mVideoBarHeight, this.mStrategy.isAccurate(), new VideoFrameLoader.LoadListener() { // from class: com.miui.gallery.vlog.clip.single.seekbar.VideoBar.1
                        @Override // com.miui.gallery.vlog.sdk.interfaces.VideoFrameLoader.LoadListener
                        public void onLoadFinished(Bitmap bitmap) {
                            VideoBar.this.mListener.onLoadFrameFinished(bitmap);
                        }
                    });
                    if (extractFrame != null) {
                        canvas.drawBitmap(extractFrame, (Rect) null, this.mDrawingRect, (Paint) null);
                    } else {
                        DefaultLogger.d("VideoBar_", "draw: bitmap is null");
                    }
                } else {
                    canvas.restore();
                    return;
                }
            }
            i++;
        }
    }

    public int convertDistanceToTimeLength(float f) {
        return Math.round((f / this.mVideoBarWidth) * (this.mEndTime - this.mStartTime));
    }

    public float convertTimeLengthToDistance(int i) {
        return (this.mVideoBarWidth / (this.mEndTime - this.mStartTime)) * i;
    }

    public float convertTimeToXCoordinate(int i) {
        return this.mLeft + convertTimeLengthToDistance(i - this.mStartTime);
    }

    public int convertXCoordinateToTime(float f) {
        int i = this.mLeft;
        if (f < i) {
            f = i;
        } else {
            int i2 = this.mRight;
            if (f > i2) {
                f = i2;
            }
        }
        return this.mStartTime + convertDistanceToTimeLength(f - i);
    }

    public int getStartTime() {
        return this.mStartTime;
    }

    public int getEndTime() {
        return this.mEndTime;
    }
}
