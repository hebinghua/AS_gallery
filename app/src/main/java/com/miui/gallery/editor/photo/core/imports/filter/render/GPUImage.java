package com.miui.gallery.editor.photo.core.imports.filter.render;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.opengl.GLSurfaceView;
import com.miui.filtersdk.filter.base.GPUImageFilter;
import com.miui.filtersdk.utils.Rotation;
import com.miui.gallery.editor.blocksdk.Block;
import com.miui.gallery.editor.blocksdk.BlockSdkUtils;
import com.miui.gallery.editor.blocksdk.SplitUtils;
import com.miui.gallery.util.CounterUtil;
import java.util.List;

/* loaded from: classes2.dex */
public class GPUImage {
    public Bitmap mCurrentBitmap;
    public GPUImageFilter mFilter;
    public GLSurfaceView mGlSurfaceView;
    public final GPUImageRenderer mRenderer;
    public ScaleType mScaleType = ScaleType.CENTER_INSIDE;

    /* loaded from: classes2.dex */
    public enum ScaleType {
        CENTER_INSIDE,
        CENTER_CROP
    }

    public GPUImage(Context context) {
        if (!supportsOpenGLES2(context)) {
            throw new IllegalStateException("OpenGL ES 2.0 is not supported on this phone.");
        }
        this.mFilter = new GPUImageFilter();
        GPUImageRenderer gPUImageRenderer = new GPUImageRenderer(this.mFilter);
        this.mRenderer = gPUImageRenderer;
        gPUImageRenderer.setDrawBoundLines(false);
    }

    public final boolean supportsOpenGLES2(Context context) {
        return ((ActivityManager) context.getSystemService("activity")).getDeviceConfigurationInfo().reqGlEsVersion >= 131072;
    }

    public void setGLSurfaceView(GLSurfaceView gLSurfaceView) {
        this.mGlSurfaceView = gLSurfaceView;
        gLSurfaceView.setEGLContextClientVersion(2);
        this.mGlSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0);
        this.mGlSurfaceView.setZOrderOnTop(true);
        this.mGlSurfaceView.getHolder().setFormat(-2);
        this.mGlSurfaceView.setRenderer(this.mRenderer);
        this.mGlSurfaceView.setRenderMode(0);
        this.mGlSurfaceView.requestRender();
    }

    public void setBackgroundColor(float f, float f2, float f3) {
        this.mRenderer.setBackgroundColor(f, f2, f3);
    }

    public void requestRender() {
        GLSurfaceView gLSurfaceView = this.mGlSurfaceView;
        if (gLSurfaceView != null) {
            gLSurfaceView.requestRender();
        }
    }

    public void setFilter(GPUImageFilter gPUImageFilter) {
        this.mFilter = gPUImageFilter;
        this.mRenderer.setFilter(gPUImageFilter);
    }

    public void setImage(Bitmap bitmap) {
        if (this.mCurrentBitmap != bitmap) {
            this.mCurrentBitmap = bitmap;
            this.mRenderer.setImageBitmap(bitmap, false);
        }
    }

    public void deleteImage() {
        this.mRenderer.deleteImage();
        this.mCurrentBitmap = null;
        requestRender();
    }

    public Bitmap getBitmapWithFilterApplied(boolean z) {
        return getBitmapWithFilterApplied(this.mCurrentBitmap, z);
    }

    public Bitmap getBitmapWithFilterApplied(Bitmap bitmap, boolean z) {
        if (bitmap == null || bitmap.isRecycled()) {
            return null;
        }
        GPUImageRenderer gPUImageRenderer = new GPUImageRenderer(this.mFilter);
        gPUImageRenderer.setRotation(Rotation.NORMAL, this.mRenderer.isFlippedHorizontally(), !this.mRenderer.isFlippedVertically());
        gPUImageRenderer.setScaleType(this.mScaleType);
        CounterUtil counterUtil = new CounterUtil("GPUImage");
        List<Block> split = SplitUtils.split(bitmap.getWidth(), bitmap.getHeight());
        if (split != null) {
            return getBitmapWithBlock(bitmap, gPUImageRenderer, split);
        }
        PixelBuffer pixelBuffer = new PixelBuffer(bitmap.getWidth(), bitmap.getHeight());
        gPUImageRenderer.setImageBitmap(bitmap, false);
        pixelBuffer.setRenderer(gPUImageRenderer);
        gPUImageRenderer.setDrawBoundLines(false);
        if (!z) {
            bitmap = null;
        }
        Bitmap bitmap2 = pixelBuffer.getBitmap(bitmap);
        counterUtil.tick("getBitmapDone");
        this.mFilter.destroy();
        gPUImageRenderer.deleteImage();
        pixelBuffer.destroy();
        this.mRenderer.setFilter(this.mFilter);
        return bitmap2;
    }

    public final Bitmap getBitmapWithBlock(Bitmap bitmap, GPUImageRenderer gPUImageRenderer, List<Block> list) {
        ISpecialProcessFilter iSpecialProcessFilter;
        int i;
        int i2;
        int i3;
        List<Block> list2 = list;
        CounterUtil counterUtil = new CounterUtil("GPUImage");
        GPUImageFilter gPUImageFilter = this.mFilter;
        if (gPUImageFilter instanceof ISpecialProcessFilter) {
            iSpecialProcessFilter = (ISpecialProcessFilter) gPUImageFilter;
            i = iSpecialProcessFilter.getSpecialBoard();
        } else {
            iSpecialProcessFilter = null;
            i = 0;
        }
        Block.TotalBlockInfo totalBlockInfo = list2.get(0).mTotalBlockInfo;
        int i4 = totalBlockInfo.mTotalColumn > 1 ? i : 0;
        int i5 = totalBlockInfo.mTotalWidth;
        int i6 = i4 * 2;
        int i7 = totalBlockInfo.mBlockWidth + i6;
        int i8 = i * 2;
        int i9 = totalBlockInfo.mBlockHeight + i8;
        PixelBuffer pixelBuffer = new PixelBuffer(i7, i9);
        int genTexture = OpenGlUtils.genTexture();
        gPUImageRenderer.setGLTextureId(genTexture, i7, i9);
        pixelBuffer.setRenderer(gPUImageRenderer);
        gPUImageRenderer.setDrawBoundLines(false);
        BlockSdkUtils.bindBitmap(bitmap);
        int i10 = 0;
        while (i10 < list.size()) {
            Block block = list2.get(i10);
            int i11 = block.mColumn;
            if (i11 == 0) {
                i2 = 0;
            } else {
                i2 = i11 == totalBlockInfo.mTotalColumn + (-1) ? i6 : i4;
            }
            int i12 = block.mRow;
            if (i12 == 0) {
                i3 = 0;
            } else {
                i3 = i12 == totalBlockInfo.mTotalRow + (-1) ? i8 : i;
            }
            BlockSdkUtils.updateTextureWidthStride(genTexture, i7, i9, i5, ((block.mOffset - i2) - (i3 * i5)) * 4);
            if (iSpecialProcessFilter != null) {
                iSpecialProcessFilter.setBlock(block);
            }
            pixelBuffer.draw();
            BlockSdkUtils.readPixelsAndMerge(i2, i3, block.mWidth, block.mHeight, i5, block.mOffset * 4);
            i10++;
            genTexture = genTexture;
            pixelBuffer = pixelBuffer;
            i7 = i7;
            i9 = i9;
            iSpecialProcessFilter = iSpecialProcessFilter;
            i5 = i5;
            list2 = list;
        }
        BlockSdkUtils.unbindBitmap(bitmap);
        counterUtil.tick(String.format("getBitmapDoneForBlock:%d", Integer.valueOf(list.size())));
        this.mFilter.destroy();
        gPUImageRenderer.deleteImage();
        pixelBuffer.destroy();
        return bitmap;
    }

    public void updateGLPosition(float[] fArr) {
        GPUImageRenderer gPUImageRenderer = this.mRenderer;
        if (gPUImageRenderer == null) {
            return;
        }
        gPUImageRenderer.updateGLCubePosition(fArr);
    }
}
