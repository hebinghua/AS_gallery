package com.meicam.sdk;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import com.meicam.sdk.NvsWaveformDataGenerator;

/* loaded from: classes.dex */
public class NvsWaveformView extends View implements NvsWaveformDataGenerator.WaveformDataCallback {
    private long m_audioFileDuration;
    private String m_audioFilePath;
    private long m_audioFileSampleCount;
    private long m_currentTaskId;
    private float[] m_leftWaveformData;
    private boolean m_needUpdateWaveformData;
    private float[] m_rightWaveformData;
    private long m_samplesPerGroup;
    private boolean m_singleChannelMode;
    private long m_trimIn;
    private long m_trimOut;
    private int m_waveformColor;
    private NvsWaveformDataGenerator m_waveformDataGenerator;

    @Override // android.view.View
    public void onAttachedToWindow() {
    }

    @Override // com.meicam.sdk.NvsWaveformDataGenerator.WaveformDataCallback
    public void onWaveformDataGenerationFailed(long j, String str, long j2) {
    }

    public NvsWaveformView(Context context) {
        super(context);
        this.m_audioFileDuration = 0L;
        this.m_audioFileSampleCount = 0L;
        this.m_trimIn = 0L;
        this.m_trimOut = 0L;
        this.m_waveformColor = -16777216;
        this.m_singleChannelMode = false;
        this.m_needUpdateWaveformData = false;
        this.m_currentTaskId = 0L;
        this.m_samplesPerGroup = 0L;
        NvsUtils.checkFunctionInMainThread();
        init();
    }

    public NvsWaveformView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.m_audioFileDuration = 0L;
        this.m_audioFileSampleCount = 0L;
        this.m_trimIn = 0L;
        this.m_trimOut = 0L;
        this.m_waveformColor = -16777216;
        this.m_singleChannelMode = false;
        this.m_needUpdateWaveformData = false;
        this.m_currentTaskId = 0L;
        this.m_samplesPerGroup = 0L;
        NvsUtils.checkFunctionInMainThread();
        init();
    }

    public NvsWaveformView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.m_audioFileDuration = 0L;
        this.m_audioFileSampleCount = 0L;
        this.m_trimIn = 0L;
        this.m_trimOut = 0L;
        this.m_waveformColor = -16777216;
        this.m_singleChannelMode = false;
        this.m_needUpdateWaveformData = false;
        this.m_currentTaskId = 0L;
        this.m_samplesPerGroup = 0L;
        NvsUtils.checkFunctionInMainThread();
        init();
    }

    public NvsWaveformView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.m_audioFileDuration = 0L;
        this.m_audioFileSampleCount = 0L;
        this.m_trimIn = 0L;
        this.m_trimOut = 0L;
        this.m_waveformColor = -16777216;
        this.m_singleChannelMode = false;
        this.m_needUpdateWaveformData = false;
        this.m_currentTaskId = 0L;
        this.m_samplesPerGroup = 0L;
        NvsUtils.checkFunctionInMainThread();
        init();
    }

    public void setAudioFilePath(String str) {
        NvsUtils.checkFunctionInMainThread();
        String str2 = this.m_audioFilePath;
        if (str2 == null || str2 == null || !str2.equals(str)) {
            if (str == null) {
                this.m_audioFilePath = null;
                this.m_audioFileSampleCount = 0L;
                cancelCurrentTask();
                invalidate();
                return;
            }
            NvsWaveformDataGenerator nvsWaveformDataGenerator = this.m_waveformDataGenerator;
            if (nvsWaveformDataGenerator == null) {
                return;
            }
            long audioFileDuration = nvsWaveformDataGenerator.getAudioFileDuration(str);
            long audioFileSampleCount = this.m_waveformDataGenerator.getAudioFileSampleCount(str);
            if (audioFileDuration <= 0 || audioFileSampleCount <= 0) {
                return;
            }
            this.m_audioFilePath = str;
            this.m_audioFileDuration = audioFileDuration;
            this.m_audioFileSampleCount = audioFileSampleCount;
            this.m_trimIn = 0L;
            this.m_trimOut = audioFileDuration;
            this.m_needUpdateWaveformData = true;
            cancelCurrentTask();
            invalidate();
        }
    }

    public String getAudioFilePath() {
        NvsUtils.checkFunctionInMainThread();
        return this.m_audioFilePath;
    }

    public void setTrimIn(long j) {
        NvsUtils.checkFunctionInMainThread();
        long max = Math.max(j, 0L);
        if (max == this.m_trimIn) {
            return;
        }
        this.m_trimIn = max;
        validateWaveformData();
    }

    public long getTrimIn() {
        NvsUtils.checkFunctionInMainThread();
        return this.m_trimIn;
    }

    public void setTrimOut(long j) {
        NvsUtils.checkFunctionInMainThread();
        long min = Math.min(Math.max(j, this.m_trimIn + 1), this.m_audioFileDuration);
        if (min == this.m_trimOut) {
            return;
        }
        this.m_trimOut = min;
        validateWaveformData();
    }

    public long getTrimOut() {
        NvsUtils.checkFunctionInMainThread();
        return this.m_trimOut;
    }

    public void setWaveformColor(int i) {
        NvsUtils.checkFunctionInMainThread();
        if (i == this.m_waveformColor) {
            return;
        }
        this.m_waveformColor = i;
        invalidate();
    }

    public int getWaveformColor() {
        NvsUtils.checkFunctionInMainThread();
        return this.m_waveformColor;
    }

    public void setSingleChannelMode(boolean z) {
        NvsUtils.checkFunctionInMainThread();
        if (z == this.m_singleChannelMode) {
            return;
        }
        this.m_singleChannelMode = z;
        invalidate();
    }

    public boolean getSingleChannelMode() {
        NvsUtils.checkFunctionInMainThread();
        return this.m_singleChannelMode;
    }

    @Override // com.meicam.sdk.NvsWaveformDataGenerator.WaveformDataCallback
    public void onWaveformDataReady(long j, String str, long j2, long j3, float[] fArr, float[] fArr2) {
        this.m_leftWaveformData = fArr;
        this.m_rightWaveformData = fArr2;
        this.m_samplesPerGroup = j3;
        this.m_currentTaskId = 0L;
        invalidate();
    }

    private void init() {
        if (!isInEditMode()) {
            NvsWaveformDataGenerator nvsWaveformDataGenerator = new NvsWaveformDataGenerator();
            this.m_waveformDataGenerator = nvsWaveformDataGenerator;
            nvsWaveformDataGenerator.setWaveformDataCallback(this);
        }
        setBackgroundColor(-1);
    }

    private long calcExpectedSamplesPerGroup() {
        long j = (long) (this.m_audioFileSampleCount * ((this.m_trimOut - this.m_trimIn) / this.m_audioFileDuration));
        int width = getWidth();
        if (width <= 0) {
            return 1L;
        }
        return Math.max((j + (width / 2)) / width, 1L);
    }

    private void validateWaveformData() {
        if (this.m_samplesPerGroup <= 0) {
            this.m_needUpdateWaveformData = true;
            cancelCurrentTask();
        } else if (calcExpectedSamplesPerGroup() == this.m_samplesPerGroup) {
        } else {
            this.m_needUpdateWaveformData = true;
            cancelCurrentTask();
            invalidate();
        }
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        if (i3 != i) {
            validateWaveformData();
        }
        super.onSizeChanged(i, i2, i3, i4);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        float[] fArr;
        Rect rect;
        Paint paint;
        NvsWaveformDataGenerator nvsWaveformDataGenerator;
        NvsWaveformView nvsWaveformView = this;
        super.onDraw(canvas);
        if (!isInEditMode() && nvsWaveformView.m_audioFileDuration > 0) {
            if (nvsWaveformView.m_needUpdateWaveformData && (nvsWaveformDataGenerator = nvsWaveformView.m_waveformDataGenerator) != null) {
                nvsWaveformView.m_needUpdateWaveformData = false;
                nvsWaveformView.m_currentTaskId = nvsWaveformDataGenerator.generateWaveformData(nvsWaveformView.m_audioFilePath, calcExpectedSamplesPerGroup(), 0L, 0L, 0);
            }
            if (nvsWaveformView.m_samplesPerGroup <= 0 || (fArr = nvsWaveformView.m_leftWaveformData) == null) {
                return;
            }
            int length = fArr.length / 2;
            float[] fArr2 = nvsWaveformView.m_rightWaveformData;
            int length2 = (fArr2 == null || nvsWaveformView.m_singleChannelMode) ? 0 : fArr2.length / 2;
            if (length == 0) {
                return;
            }
            int width = getWidth();
            int height = getHeight();
            if (length2 != 0) {
                height /= 2;
            }
            Rect rect2 = new Rect();
            Paint paint2 = new Paint();
            paint2.setStyle(Paint.Style.FILL);
            paint2.setAntiAlias(false);
            paint2.setColor(nvsWaveformView.m_waveformColor);
            Color.alpha(nvsWaveformView.m_waveformColor);
            paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
            long j = nvsWaveformView.m_trimIn;
            long j2 = nvsWaveformView.m_audioFileDuration;
            int i = length2;
            long j3 = nvsWaveformView.m_audioFileSampleCount;
            Rect rect3 = rect2;
            Paint paint3 = paint2;
            long j4 = (long) ((j / j2) * j3);
            long j5 = (long) (((nvsWaveformView.m_trimOut - j) / j2) * j3);
            if (j5 == 0) {
                return;
            }
            int i2 = 0;
            while (i2 < width) {
                int i3 = (int) ((((long) ((i2 / width) * j5)) + j4) / nvsWaveformView.m_samplesPerGroup);
                if (i3 < length) {
                    float f = height;
                    float[] fArr3 = nvsWaveformView.m_leftWaveformData;
                    int i4 = i3 * 2;
                    rect = rect3;
                    rect.set(i2, (int) (f * (1.0f - ((fArr3[i4 + 1] + 1.0f) / 2.0f))), i2 + 1, (int) (f * (1.0f - ((fArr3[i4] + 1.0f) / 2.0f))));
                    paint = paint3;
                    canvas.drawRect(rect, paint);
                } else {
                    rect = rect3;
                    paint = paint3;
                }
                int i5 = i;
                if (i3 < i5) {
                    float f2 = height;
                    float[] fArr4 = nvsWaveformView.m_rightWaveformData;
                    int i6 = i3 * 2;
                    rect.set(i2, ((int) (f2 * (1.0f - ((fArr4[i6 + 1] + 1.0f) / 2.0f)))) + height, i2 + 1, ((int) (f2 * (1.0f - ((fArr4[i6] + 1.0f) / 2.0f)))) + height);
                    canvas.drawRect(rect, paint);
                }
                i2++;
                nvsWaveformView = this;
                paint3 = paint;
                i = i5;
                rect3 = rect;
            }
        }
    }

    @Override // android.view.View
    public void onDetachedFromWindow() {
        cancelCurrentTask();
        NvsWaveformDataGenerator nvsWaveformDataGenerator = this.m_waveformDataGenerator;
        if (nvsWaveformDataGenerator != null) {
            nvsWaveformDataGenerator.setWaveformDataCallback(null);
            this.m_waveformDataGenerator.release();
            this.m_waveformDataGenerator = null;
        }
        super.onDetachedFromWindow();
    }

    private void cancelCurrentTask() {
        if (!isInEditMode()) {
            long j = this.m_currentTaskId;
            if (j == 0) {
                return;
            }
            NvsWaveformDataGenerator nvsWaveformDataGenerator = this.m_waveformDataGenerator;
            if (nvsWaveformDataGenerator != null) {
                nvsWaveformDataGenerator.cancelTask(j);
            }
            this.m_currentTaskId = 0L;
        }
    }
}
