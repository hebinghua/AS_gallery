package com.miui.gallery.editor.photo.core.imports.filter.render;

import android.opengl.GLES20;
import com.miui.filtersdk.filter.base.BaseOriginalFilter;
import com.miui.gallery.editor.blocksdk.Block;
import java.nio.FloatBuffer;
import java.util.List;

/* loaded from: classes2.dex */
public class BaseOriginalFilterGroup extends BaseOriginalFilter implements IFilterEmptyValidate, ISpecialProcessFilter {
    public List<BaseOriginalFilter> mFilters;
    public int[] mGroupFrameBufferTextures;
    public int[] mGroupFrameBuffers;
    public int mGroupFrameHeight;
    public int mGroupFrameWidth;

    public BaseOriginalFilterGroup(List<BaseOriginalFilter> list) {
        super("", "");
        this.mGroupFrameBuffers = null;
        this.mGroupFrameBufferTextures = null;
        this.mGroupFrameWidth = -1;
        this.mGroupFrameHeight = -1;
        this.mFilters = list;
    }

    @Override // com.miui.filtersdk.filter.base.GPUImageFilter
    public void onDestroy() {
        super.onDestroy();
        for (BaseOriginalFilter baseOriginalFilter : this.mFilters) {
            baseOriginalFilter.destroy();
        }
        destroyGroupFrameBuffers();
    }

    @Override // com.miui.filtersdk.filter.base.GPUImageFilter
    public void init() {
        for (BaseOriginalFilter baseOriginalFilter : this.mFilters) {
            baseOriginalFilter.init();
        }
    }

    @Override // com.miui.filtersdk.filter.base.GPUImageFilter
    public void onInputSizeChanged(int i, int i2) {
        super.onInputSizeChanged(i, i2);
        int size = this.mFilters.size();
        for (int i3 = 0; i3 < size; i3++) {
            this.mFilters.get(i3).onInputSizeChanged(i, i2);
        }
        int[] iArr = this.mGroupFrameBuffers;
        if (iArr != null && (this.mGroupFrameWidth != i || this.mGroupFrameHeight != i2 || iArr.length != size - 1)) {
            destroyGroupFrameBuffers();
            this.mGroupFrameWidth = i;
            this.mGroupFrameHeight = i2;
        }
        if (this.mGroupFrameBuffers == null) {
            this.mGroupFrameBuffers = new int[2];
            this.mGroupFrameBufferTextures = new int[2];
            int i4 = 0;
            while (true) {
                int[] iArr2 = this.mGroupFrameBuffers;
                if (i4 >= iArr2.length) {
                    return;
                }
                GLES20.glGenFramebuffers(1, iArr2, i4);
                GLES20.glGenTextures(1, this.mGroupFrameBufferTextures, i4);
                GLES20.glBindTexture(3553, this.mGroupFrameBufferTextures[i4]);
                GLES20.glTexImage2D(3553, 0, 6408, i, i2, 0, 6408, 5121, null);
                GLES20.glTexParameterf(3553, 10240, 9729.0f);
                GLES20.glTexParameterf(3553, 10241, 9729.0f);
                GLES20.glTexParameterf(3553, 10242, 33071.0f);
                GLES20.glTexParameterf(3553, 10243, 33071.0f);
                GLES20.glBindFramebuffer(36160, this.mGroupFrameBuffers[i4]);
                GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.mGroupFrameBufferTextures[i4], 0);
                GLES20.glBindTexture(3553, 0);
                GLES20.glBindFramebuffer(36160, 0);
                i4++;
            }
        }
    }

    @Override // com.miui.filtersdk.filter.base.GPUImageFilter
    public void onDisplaySizeChanged(int i, int i2) {
        super.onDisplaySizeChanged(i, i2);
        for (int i3 = 0; i3 < this.mFilters.size(); i3++) {
            this.mFilters.get(i3).onDisplaySizeChanged(i, i2);
        }
    }

    @Override // com.miui.filtersdk.filter.base.GPUImageFilter
    public void initFrameBuffers(int i, int i2) {
        for (int i3 = 0; i3 < this.mFilters.size(); i3++) {
            this.mFilters.get(i3).initFrameBuffers(i, i2);
        }
    }

    @Override // com.miui.filtersdk.filter.base.GPUImageFilter
    public int onDrawFrame(int i, FloatBuffer floatBuffer, FloatBuffer floatBuffer2) {
        if (this.mGroupFrameBuffers == null || this.mGroupFrameBufferTextures == null) {
            return -1;
        }
        int size = this.mFilters.size();
        int i2 = 0;
        while (true) {
            boolean z = true;
            if (i2 >= size) {
                return 1;
            }
            BaseOriginalFilter baseOriginalFilter = this.mFilters.get(i2);
            if (i2 >= size - 1) {
                z = false;
            }
            if (z) {
                GLES20.glViewport(0, 0, this.mInputWidth, this.mInputHeight);
                int[] iArr = this.mGroupFrameBuffers;
                GLES20.glBindFramebuffer(36160, iArr[i2 % iArr.length]);
                baseOriginalFilter.onDrawFrame(i, this.mGLCubeBuffer, this.mGLTextureBuffer);
                GLES20.glBindFramebuffer(36160, 0);
                i = this.mGroupFrameBufferTextures[i2 % this.mGroupFrameBuffers.length];
            } else {
                baseOriginalFilter.onDrawFrame(i, floatBuffer, floatBuffer2);
            }
            i2++;
        }
    }

    @Override // com.miui.filtersdk.filter.base.GPUImageFilter
    public int onDrawToTexture(int i, FloatBuffer floatBuffer, FloatBuffer floatBuffer2) {
        int size = this.mFilters.size();
        int i2 = 0;
        while (i2 < size) {
            boolean z = i2 < size + (-1);
            BaseOriginalFilter baseOriginalFilter = this.mFilters.get(i2);
            if (z) {
                i = baseOriginalFilter.onDrawToTexture(i, this.mGLCubeBuffer, this.mGLTextureBuffer);
            } else {
                i = baseOriginalFilter.onDrawToTexture(i, floatBuffer, floatBuffer2);
            }
            i2++;
        }
        return i;
    }

    public final void destroyGroupFrameBuffers() {
        int[] iArr = this.mGroupFrameBufferTextures;
        if (iArr != null) {
            GLES20.glDeleteTextures(iArr.length, iArr, 0);
            this.mGroupFrameBufferTextures = null;
        }
        int[] iArr2 = this.mGroupFrameBuffers;
        if (iArr2 != null) {
            GLES20.glDeleteFramebuffers(iArr2.length, iArr2, 0);
            this.mGroupFrameBuffers = null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x000c  */
    @Override // com.miui.gallery.editor.photo.core.imports.filter.render.IFilterEmptyValidate
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean isEmpty() {
        /*
            r3 = this;
            java.util.List<com.miui.filtersdk.filter.base.BaseOriginalFilter> r0 = r3.mFilters
            java.util.Iterator r0 = r0.iterator()
        L6:
            boolean r1 = r0.hasNext()
            if (r1 == 0) goto L21
            java.lang.Object r1 = r0.next()
            com.miui.filtersdk.filter.base.BaseOriginalFilter r1 = (com.miui.filtersdk.filter.base.BaseOriginalFilter) r1
            boolean r2 = r1 instanceof com.miui.gallery.editor.photo.core.imports.filter.render.IFilterEmptyValidate
            if (r2 == 0) goto L1f
            com.miui.gallery.editor.photo.core.imports.filter.render.IFilterEmptyValidate r1 = (com.miui.gallery.editor.photo.core.imports.filter.render.IFilterEmptyValidate) r1
            boolean r1 = r1.isEmpty()
            if (r1 == 0) goto L1f
            goto L6
        L1f:
            r0 = 0
            return r0
        L21:
            r0 = 1
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.editor.photo.core.imports.filter.render.BaseOriginalFilterGroup.isEmpty():boolean");
    }

    @Override // com.miui.gallery.editor.photo.core.imports.filter.render.ISpecialProcessFilter
    public int getSpecialBoard() {
        int i = 0;
        for (BaseOriginalFilter baseOriginalFilter : this.mFilters) {
            if (baseOriginalFilter instanceof ISpecialProcessFilter) {
                i = Math.max(i, ((ISpecialProcessFilter) baseOriginalFilter).getSpecialBoard());
            }
        }
        return i;
    }

    @Override // com.miui.gallery.editor.photo.core.imports.filter.render.ISpecialProcessFilter
    public void setBlock(Block block) {
        for (BaseOriginalFilter baseOriginalFilter : this.mFilters) {
            if (baseOriginalFilter instanceof ISpecialProcessFilter) {
                ((ISpecialProcessFilter) baseOriginalFilter).setBlock(block);
            }
        }
    }
}
