package com.miui.gallery.editor.photo.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.util.AttributeSet;
import com.miui.arcsoftbeauty.ArcsoftBeautyJni;
import com.miui.filtersdk.beauty.BeautyParameterType;
import com.miui.filtersdk.beauty.BeautyProcessorManager;
import com.miui.filtersdk.beauty.IntelligentBeautyProcessor;
import com.miui.filtersdk.filter.YUVBeautyFilter;
import com.miui.filtersdk.utils.Rotation;
import com.miui.gallery.editor.photo.app.miuibeautify.MiuiBeautyFragment;
import com.miui.gallery.editor.photo.widgets.base.MagicBaseView;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.util.Map;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/* loaded from: classes2.dex */
public class BeautyImageView extends MagicBaseView {
    public IntelligentBeautyProcessor mBeautyProcessor;
    public int mBufferSize;
    public Context mContext;
    public boolean mFirstRenderFinished;
    public boolean mHasRawYuv;
    public InitRenderCallback mInitRenderCallback;
    public byte[] mPicData;
    public int mRenderRecordIndex;
    public YUVBeautyFilter mYUVFilter;

    /* loaded from: classes2.dex */
    public interface InitRenderCallback {
        void onRenderFinish();
    }

    public BeautyImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mRenderRecordIndex = -1;
        this.mFirstRenderFinished = false;
        this.mContext = context;
        getHolder().addCallback(this);
        this.scaleType = MagicBaseView.ScaleType.CENTER_INSIDE;
        this.mYUVFilter = getYUVBeautyRender();
        this.mHasRawYuv = false;
    }

    @Override // com.miui.gallery.editor.photo.widgets.base.MagicBaseView, android.opengl.GLSurfaceView.Renderer
    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        super.onSurfaceCreated(gl10, eGLConfig);
        this.mYUVFilter.init();
    }

    @Override // com.miui.gallery.editor.photo.widgets.base.MagicBaseView, android.opengl.GLSurfaceView.Renderer
    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        super.onSurfaceChanged(gl10, i, i2);
        adjustSize(Rotation.NORMAL.asInt(), false, false);
    }

    @Override // com.miui.gallery.editor.photo.widgets.base.MagicBaseView, android.opengl.GLSurfaceView.Renderer
    public void onDrawFrame(GL10 gl10) {
        if (BaseMiscUtil.isNightMode(this.mContext)) {
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        } else {
            GLES20.glClearColor(0.97f, 0.97f, 0.97f, 0.0f);
        }
        GLES20.glClear(16640);
        this.mYUVFilter.onDrawFrame(0, this.gLCubeBuffer, this.gLTextureBuffer);
        if (!this.mFirstRenderFinished) {
            InitRenderCallback initRenderCallback = this.mInitRenderCallback;
            if (initRenderCallback != null) {
                initRenderCallback.onRenderFinish();
            }
            this.mFirstRenderFinished = true;
        }
    }

    public final void refreshDisplay() {
        requestRender();
    }

    public void setPicData(Bitmap bitmap) {
        setPicData(bitmap, true);
    }

    public void setPicData(Bitmap bitmap, boolean z) {
        this.imageWidth = bitmap.getWidth();
        int height = bitmap.getHeight();
        this.imageHeight = height;
        if (!this.mHasRawYuv) {
            int i = this.imageWidth;
            int i2 = ((i * height) * 3) / 2;
            this.mBufferSize = i2;
            byte[] bArr = new byte[i2];
            this.mPicData = bArr;
            ArcsoftBeautyJni.encodeYUV420SP(bitmap, i, height, bArr);
            writeRecordFile();
            this.mHasRawYuv = true;
        }
        reloadTexture(false);
        if (z) {
            refreshDisplay();
        }
    }

    private YUVBeautyFilter getYUVBeautyRender() {
        YUVBeautyFilter yUVBeautyFilter = new YUVBeautyFilter();
        IntelligentBeautyProcessor beautyProcessor = BeautyProcessorManager.INSTANCE.getBeautyProcessor();
        beautyProcessor.setRotation(0);
        this.mBeautyProcessor = beautyProcessor;
        yUVBeautyFilter.setBeautyProcessor(beautyProcessor);
        return yUVBeautyFilter;
    }

    public void reloadTexture(boolean z) {
        if (z) {
            this.mYUVFilter.genYUVTextures(this.mPicData, this.imageWidth, this.imageHeight);
            this.mBeautyProcessor.clearBeautyParameters();
            return;
        }
        int i = this.imageWidth;
        int i2 = this.imageHeight;
        byte[] bArr = new byte[((i * i2) * 3) / 2];
        System.arraycopy(this.mPicData, 0, bArr, 0, ((i * i2) * 3) / 2);
        this.mYUVFilter.genYUVTextures(bArr, this.imageWidth, this.imageHeight);
    }

    public void updateBeautyProcessor(Map<BeautyParameterType, Float> map) {
        if (map == null || map.size() == 0) {
            this.mBeautyProcessor.clearBeautyParameters();
        } else {
            this.mBeautyProcessor.setBeautyParamsDegree(map);
        }
    }

    public void getBmpFromCurrBuffer(Bitmap bitmap) {
        ArcsoftBeautyJni.updateBmpWithYuvBuffer(bitmap, this.imageWidth, this.imageHeight, this.mPicData);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v1, types: [int] */
    /* JADX WARN: Type inference failed for: r1v11 */
    /* JADX WARN: Type inference failed for: r1v12 */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* JADX WARN: Type inference failed for: r1v6, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r1v7 */
    /* JADX WARN: Type inference failed for: r1v9, types: [java.io.Closeable] */
    public void writeRecordFile() {
        Throwable th;
        RandomAccessFile randomAccessFile;
        IOException e;
        this.mYUVFilter.genYUVTextures(this.mPicData, this.imageWidth, this.imageHeight);
        this.mBeautyProcessor.clearBeautyParameters();
        ?? r1 = MiuiBeautyFragment.BEAUTY_RECORDS_MAX;
        this.mRenderRecordIndex = (this.mRenderRecordIndex + 1) % r1;
        try {
            try {
                randomAccessFile = new RandomAccessFile(getTmpRecordFile(this.mRenderRecordIndex), "rw");
                try {
                    randomAccessFile.getChannel().map(FileChannel.MapMode.READ_WRITE, 0L, this.mBufferSize).put(this.mPicData);
                    r1 = randomAccessFile;
                } catch (IOException e2) {
                    e = e2;
                    e.printStackTrace();
                    r1 = randomAccessFile;
                    BaseMiscUtil.closeSilently(r1);
                }
            } catch (Throwable th2) {
                th = th2;
                BaseMiscUtil.closeSilently(r1);
                throw th;
            }
        } catch (IOException e3) {
            randomAccessFile = null;
            e = e3;
        } catch (Throwable th3) {
            r1 = 0;
            th = th3;
            BaseMiscUtil.closeSilently(r1);
            throw th;
        }
        BaseMiscUtil.closeSilently(r1);
    }

    public void setInitRenderCallback(InitRenderCallback initRenderCallback) {
        this.mInitRenderCallback = initRenderCallback;
    }

    public void renderPreviousBuffer() {
        int i = MiuiBeautyFragment.BEAUTY_RECORDS_MAX;
        int i2 = (this.mRenderRecordIndex - 1) % i;
        this.mRenderRecordIndex = i2;
        if (i2 < 0) {
            this.mRenderRecordIndex = i2 + i;
        }
        readRecordBuffer();
        reloadTexture(false);
        refreshDisplay();
    }

    public void renderNextBuffer() {
        this.mRenderRecordIndex = (this.mRenderRecordIndex + 1) % MiuiBeautyFragment.BEAUTY_RECORDS_MAX;
        readRecordBuffer();
        reloadTexture(false);
        refreshDisplay();
    }

    public final void readRecordBuffer() {
        RandomAccessFile randomAccessFile;
        Throwable th;
        IOException e;
        try {
            try {
                randomAccessFile = new RandomAccessFile(getTmpRecordFile(this.mRenderRecordIndex), "r");
                try {
                    FileChannel channel = randomAccessFile.getChannel();
                    channel.map(FileChannel.MapMode.READ_ONLY, 0L, channel.size()).get(this.mPicData);
                } catch (IOException e2) {
                    e = e2;
                    e.printStackTrace();
                    BaseMiscUtil.closeSilently(randomAccessFile);
                }
            } catch (Throwable th2) {
                th = th2;
                BaseMiscUtil.closeSilently(randomAccessFile);
                throw th;
            }
        } catch (IOException e3) {
            randomAccessFile = null;
            e = e3;
        } catch (Throwable th3) {
            randomAccessFile = null;
            th = th3;
            BaseMiscUtil.closeSilently(randomAccessFile);
            throw th;
        }
        BaseMiscUtil.closeSilently(randomAccessFile);
    }

    public boolean clearAllRecords() {
        boolean z = true;
        for (int i = 0; i < MiuiBeautyFragment.BEAUTY_RECORDS_MAX; i++) {
            File tmpRecordFile = getTmpRecordFile(i);
            if (tmpRecordFile != null && tmpRecordFile.exists() && !tmpRecordFile.delete()) {
                DefaultLogger.e("BeautyImageView", "Failed to delete beauty record file: " + tmpRecordFile.getPath());
                z = false;
            }
        }
        return z;
    }

    public final File getTmpRecordFile(int i) {
        File file = new File(this.mContext.getCacheDir(), "beauty-records");
        if (file.exists() || file.mkdir()) {
            return new File(file, "beauty_record_temp_" + i);
        }
        return null;
    }
}
