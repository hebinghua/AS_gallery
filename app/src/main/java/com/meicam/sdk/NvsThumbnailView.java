package com.meicam.sdk;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

/* loaded from: classes.dex */
public class NvsThumbnailView extends View {
    private long m_internalObj;
    private String m_mediaFilePath;
    private boolean m_needUpdate;
    private boolean m_painting;
    private Bitmap m_thumbnail;

    private native void nativeCancelIconTask(long j);

    private native void nativeClose(long j);

    private native void nativeGetThumbnail(long j, String str);

    private native long nativeInit();

    public NvsThumbnailView(Context context) {
        super(context);
        this.m_thumbnail = null;
        this.m_internalObj = 0L;
        this.m_painting = false;
        this.m_needUpdate = false;
    }

    public NvsThumbnailView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.m_thumbnail = null;
        this.m_internalObj = 0L;
        this.m_painting = false;
        this.m_needUpdate = false;
    }

    public NvsThumbnailView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.m_thumbnail = null;
        this.m_internalObj = 0L;
        this.m_painting = false;
        this.m_needUpdate = false;
    }

    public NvsThumbnailView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.m_thumbnail = null;
        this.m_internalObj = 0L;
        this.m_painting = false;
        this.m_needUpdate = false;
    }

    public void setMediaFilePath(String str) {
        NvsUtils.checkFunctionInMainThread();
        String str2 = this.m_mediaFilePath;
        if (str2 == null || str == null || !str2.equals(str)) {
            this.m_mediaFilePath = str;
            this.m_needUpdate = true;
            cancelIconTask();
            invalidate();
        }
    }

    public String getMediaFilePath() {
        NvsUtils.checkFunctionInMainThread();
        return this.m_mediaFilePath;
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String str = this.m_mediaFilePath;
        if (str == null || str.isEmpty()) {
            return;
        }
        if (this.m_thumbnail == null || this.m_needUpdate) {
            if (this.m_internalObj == 0) {
                return;
            }
            this.m_needUpdate = false;
            this.m_painting = true;
            if (!isInEditMode()) {
                nativeGetThumbnail(this.m_internalObj, this.m_mediaFilePath);
            }
            this.m_painting = false;
            return;
        }
        Rect rect = new Rect();
        getDrawingRect(rect);
        int width = this.m_thumbnail.getWidth();
        double d = width;
        double width2 = d / rect.width();
        double height = this.m_thumbnail.getHeight();
        double height2 = height / rect.height();
        if (width2 > height2) {
            double d2 = d / height2;
            int width3 = rect.left + ((int) ((rect.width() - d2) / 2.0d));
            rect.left = width3;
            rect.right = (int) (width3 + d2);
        } else {
            double d3 = height / width2;
            int height3 = rect.top + ((int) ((rect.height() - d3) / 2.0d));
            rect.top = height3;
            rect.bottom = (int) (height3 + d3);
        }
        canvas.drawBitmap(this.m_thumbnail, (Rect) null, rect, new Paint(2));
    }

    @Override // android.view.View
    public void onAttachedToWindow() {
        if (!isInEditMode()) {
            this.m_internalObj = nativeInit();
        }
    }

    @Override // android.view.View
    public void onDetachedFromWindow() {
        cancelIconTask();
        long j = this.m_internalObj;
        if (j != 0) {
            nativeClose(j);
            this.m_internalObj = 0L;
        }
        this.m_thumbnail = null;
        super.onDetachedFromWindow();
    }

    private void cancelIconTask() {
        if (!isInEditMode()) {
            nativeCancelIconTask(this.m_internalObj);
        }
    }

    public void notifyThumbnailArrived(final Bitmap bitmap) {
        if (!this.m_painting) {
            this.m_thumbnail = bitmap;
            invalidate();
            return;
        }
        new Handler().post(new Runnable() { // from class: com.meicam.sdk.NvsThumbnailView.1
            @Override // java.lang.Runnable
            public void run() {
                NvsThumbnailView.this.m_thumbnail = bitmap;
                NvsThumbnailView.this.invalidate();
            }
        });
    }
}
