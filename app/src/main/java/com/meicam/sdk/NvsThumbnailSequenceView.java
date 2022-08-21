package com.meicam.sdk;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.ViewGroup;
import com.meicam.sdk.NvsMultiThumbnailSequenceView;
import java.util.ArrayList;

/* loaded from: classes.dex */
public class NvsThumbnailSequenceView extends ViewGroup {
    private long m_duration;
    private NvsMultiThumbnailSequenceView m_internalView;
    private String m_mediaFilePath;
    private boolean m_needsUpdateInternalView;
    private long m_startTime;
    private boolean m_stillImageHint;
    private float m_thumbnailAspectRatio;
    private int m_thumbnailImageFillMode;

    @Override // android.view.ViewGroup
    public boolean shouldDelayChildPressedState() {
        return false;
    }

    public NvsThumbnailSequenceView(Context context) {
        super(context);
        this.m_startTime = 0L;
        this.m_duration = 4000000L;
        this.m_thumbnailAspectRatio = 0.5625f;
        this.m_stillImageHint = false;
        this.m_thumbnailImageFillMode = 0;
        this.m_needsUpdateInternalView = false;
        NvsUtils.checkFunctionInMainThread();
        init(context);
    }

    public NvsThumbnailSequenceView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.m_startTime = 0L;
        this.m_duration = 4000000L;
        this.m_thumbnailAspectRatio = 0.5625f;
        this.m_stillImageHint = false;
        this.m_thumbnailImageFillMode = 0;
        this.m_needsUpdateInternalView = false;
        NvsUtils.checkFunctionInMainThread();
        init(context);
    }

    public NvsThumbnailSequenceView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.m_startTime = 0L;
        this.m_duration = 4000000L;
        this.m_thumbnailAspectRatio = 0.5625f;
        this.m_stillImageHint = false;
        this.m_thumbnailImageFillMode = 0;
        this.m_needsUpdateInternalView = false;
        NvsUtils.checkFunctionInMainThread();
        init(context);
    }

    public NvsThumbnailSequenceView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.m_startTime = 0L;
        this.m_duration = 4000000L;
        this.m_thumbnailAspectRatio = 0.5625f;
        this.m_stillImageHint = false;
        this.m_thumbnailImageFillMode = 0;
        this.m_needsUpdateInternalView = false;
        NvsUtils.checkFunctionInMainThread();
        init(context);
    }

    private void init(Context context) {
        NvsMultiThumbnailSequenceView nvsMultiThumbnailSequenceView = new NvsMultiThumbnailSequenceView(context);
        this.m_internalView = nvsMultiThumbnailSequenceView;
        nvsMultiThumbnailSequenceView.setScrollEnabled(false);
        addView(this.m_internalView, new ViewGroup.LayoutParams(-1, -1));
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
        updateInternalView();
    }

    public int getThumbnailImageFillMode() {
        NvsUtils.checkFunctionInMainThread();
        return this.m_thumbnailImageFillMode;
    }

    public void setMediaFilePath(String str) {
        NvsUtils.checkFunctionInMainThread();
        String str2 = this.m_mediaFilePath;
        if (str2 == null || str == null || !str2.equals(str)) {
            this.m_mediaFilePath = str;
            updateInternalView();
        }
    }

    public String getMediaFilePath() {
        NvsUtils.checkFunctionInMainThread();
        return this.m_mediaFilePath;
    }

    public void setStartTime(long j) {
        NvsUtils.checkFunctionInMainThread();
        if (j < 0) {
            j = 0;
        }
        if (j == this.m_startTime) {
            return;
        }
        this.m_startTime = j;
        updateInternalView();
    }

    public long getStartTime() {
        NvsUtils.checkFunctionInMainThread();
        return this.m_startTime;
    }

    public void setDuration(long j) {
        NvsUtils.checkFunctionInMainThread();
        if (j <= 0) {
            j = 1;
        }
        if (j == this.m_duration) {
            return;
        }
        this.m_duration = j;
        updateInternalView();
    }

    public long getDuration() {
        NvsUtils.checkFunctionInMainThread();
        return this.m_duration;
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
        updateInternalView();
    }

    public float getThumbnailAspectRatio() {
        NvsUtils.checkFunctionInMainThread();
        return this.m_thumbnailAspectRatio;
    }

    public void setStillImageHint(boolean z) {
        NvsUtils.checkFunctionInMainThread();
        if (z == this.m_stillImageHint) {
            return;
        }
        this.m_stillImageHint = z;
        updateInternalView();
    }

    public boolean getStillImageHint() {
        NvsUtils.checkFunctionInMainThread();
        return this.m_stillImageHint;
    }

    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        this.m_internalView.measure(i, i2);
        super.onMeasure(i, i2);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        this.m_internalView.layout(0, 0, getWidth(), getHeight());
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        if (i3 != i) {
            updateInternalView();
        }
        super.onSizeChanged(i, i2, i3, i4);
    }

    private void updateInternalView() {
        this.m_needsUpdateInternalView = true;
        new Handler().post(new Runnable() { // from class: com.meicam.sdk.NvsThumbnailSequenceView.1
            @Override // java.lang.Runnable
            public void run() {
                NvsThumbnailSequenceView.this.doUpdateInternalView();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void doUpdateInternalView() {
        if (!this.m_needsUpdateInternalView) {
            return;
        }
        this.m_needsUpdateInternalView = false;
        this.m_internalView.setThumbnailAspectRatio(this.m_thumbnailAspectRatio);
        this.m_internalView.setPixelPerMicrosecond(getWidth() / this.m_duration);
        this.m_internalView.setThumbnailImageFillMode(this.m_thumbnailImageFillMode);
        if (this.m_mediaFilePath == null) {
            this.m_internalView.setThumbnailSequenceDescArray(null);
            return;
        }
        NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc thumbnailSequenceDesc = new NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc();
        thumbnailSequenceDesc.mediaFilePath = this.m_mediaFilePath;
        thumbnailSequenceDesc.inPoint = 0L;
        long j = this.m_duration;
        thumbnailSequenceDesc.outPoint = j;
        long j2 = this.m_startTime;
        thumbnailSequenceDesc.trimIn = j2;
        thumbnailSequenceDesc.trimOut = j2 + j;
        thumbnailSequenceDesc.stillImageHint = this.m_stillImageHint;
        ArrayList<NvsMultiThumbnailSequenceView.ThumbnailSequenceDesc> arrayList = new ArrayList<>();
        arrayList.add(thumbnailSequenceDesc);
        this.m_internalView.setThumbnailSequenceDescArray(arrayList);
    }
}
