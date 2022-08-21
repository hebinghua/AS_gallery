package com.miui.gallery.editor.photo.core.imports.mosaic;

import android.opengl.GLES20;
import com.miui.gallery.editor.photo.core.imports.mosaic.MosaicOperationItem;
import com.miui.gallery.editor.photo.widgets.glview.GLFBOManager;
import com.miui.gallery.editor.photo.widgets.glview.shader.GLTextureShader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

/* loaded from: classes2.dex */
public class MosaicUndoManager {
    public final int mBitmapHeight;
    public final int mBitmapWidth;
    public CaptureListener mCaptureListener;
    public int mViewHeight;
    public int mViewWidth;
    public final LinkedList<GLFBOManager> mBufferItems = new LinkedList<>();
    public final LinkedList<GLFBOManager> mRevokedBufferItems = new LinkedList<>();
    public final LinkedList<MosaicOperationItem> mMosaicOperationItems = new LinkedList<>();
    public final LinkedList<RevokedItem> mRevokedOperationItems = new LinkedList<>();
    public final Stack<GLFBOManager> mReuseBufferItem = new Stack<>();

    /* loaded from: classes2.dex */
    public interface CaptureListener {
        void onCapture();
    }

    public MosaicUndoManager(int i, int i2, int i3, int i4) {
        this.mBitmapWidth = i;
        this.mBitmapHeight = i2;
        this.mViewWidth = i3;
        this.mViewHeight = i4;
    }

    public void record(MosaicOperationItem.PaintingItem paintingItem, MosaicGLEntity mosaicGLEntity, boolean z) {
        if (z) {
            clearRevokedItem();
        }
        if (this.mMosaicOperationItems.isEmpty()) {
            addNewOperationItem(paintingItem, mosaicGLEntity);
            return;
        }
        MosaicOperationItem last = this.mMosaicOperationItems.getLast();
        if (last.mosaicGLEntity == mosaicGLEntity) {
            last.add(paintingItem);
        } else {
            addNewOperationItem(paintingItem, mosaicGLEntity);
        }
    }

    public void updateViewPort(int i, int i2) {
        this.mViewWidth = i;
        this.mViewHeight = i2;
        Iterator<GLFBOManager> it = this.mBufferItems.iterator();
        while (it.hasNext()) {
            it.next().updateViewPort(i, i2);
        }
        Iterator<GLFBOManager> it2 = this.mReuseBufferItem.iterator();
        while (it2.hasNext()) {
            it2.next().updateViewPort(i, i2);
        }
        Iterator<GLFBOManager> it3 = this.mReuseBufferItem.iterator();
        while (it3.hasNext()) {
            it3.next().updateViewPort(i, i2);
        }
    }

    public LinkedList<MosaicOperationItem> exportRecord() {
        return this.mMosaicOperationItems;
    }

    public List<String> generateSample() {
        ArrayList arrayList = new ArrayList();
        Iterator<MosaicOperationItem> it = this.mMosaicOperationItems.iterator();
        while (it.hasNext()) {
            arrayList.add(it.next().mosaicGLEntity.name);
        }
        return arrayList;
    }

    public void capture(GLFBOManager gLFBOManager, GLTextureShader gLTextureShader) {
        GLFBOManager gLFBOManager2;
        if (this.mBufferItems.size() < 6) {
            if (!this.mReuseBufferItem.isEmpty()) {
                gLFBOManager2 = this.mReuseBufferItem.pop();
            } else {
                gLFBOManager2 = new GLFBOManager(this.mBitmapWidth, this.mBitmapHeight, this.mViewWidth, this.mViewHeight);
            }
            gLFBOManager2.bind();
            GLES20.glClear(16640);
            gLTextureShader.drawFBO(gLFBOManager.getTextureId());
            gLFBOManager2.unBind();
            this.mBufferItems.add(gLFBOManager2);
        } else {
            GLFBOManager removeFirst = this.mBufferItems.removeFirst();
            removeFirst.bind();
            GLES20.glClear(16640);
            gLTextureShader.drawFBO(gLFBOManager.getTextureId());
            removeFirst.unBind();
            this.mBufferItems.add(removeFirst);
        }
        CaptureListener captureListener = this.mCaptureListener;
        if (captureListener != null) {
            captureListener.onCapture();
        }
    }

    public final void clearRevokedItem() {
        this.mRevokedOperationItems.clear();
        while (!this.mRevokedBufferItems.isEmpty()) {
            this.mReuseBufferItem.push(this.mRevokedBufferItems.removeLast());
        }
    }

    public boolean isEmpty() {
        if (this.mMosaicOperationItems.isEmpty()) {
            return true;
        }
        if (this.mMosaicOperationItems.size() != 1) {
            return false;
        }
        return this.mMosaicOperationItems.getLast().isEmpty();
    }

    public boolean canRevoke() {
        return this.mBufferItems.size() > 1 && !this.mMosaicOperationItems.isEmpty();
    }

    public boolean canRevert() {
        return !this.mRevokedBufferItems.isEmpty() && !this.mRevokedOperationItems.isEmpty();
    }

    public GLFBOManager doRevoke() {
        if (!canRevoke()) {
            return null;
        }
        MosaicOperationItem last = this.mMosaicOperationItems.getLast();
        this.mRevokedOperationItems.add(new RevokedItem(last.mosaicGLEntity, last.removeLast()));
        if (last.isEmpty()) {
            this.mMosaicOperationItems.removeLast();
        }
        this.mRevokedBufferItems.add(this.mBufferItems.removeLast());
        if (!this.mBufferItems.isEmpty()) {
            return this.mBufferItems.getLast();
        }
        return null;
    }

    public GLFBOManager doRevert() {
        if (!canRevert()) {
            return null;
        }
        RevokedItem removeLast = this.mRevokedOperationItems.removeLast();
        record(removeLast.paintingItem, removeLast.mosaicGLEntity, false);
        GLFBOManager removeLast2 = this.mRevokedBufferItems.removeLast();
        this.mBufferItems.add(removeLast2);
        return removeLast2;
    }

    public final void addNewOperationItem(MosaicOperationItem.PaintingItem paintingItem, MosaicGLEntity mosaicGLEntity) {
        MosaicOperationItem mosaicOperationItem = new MosaicOperationItem(mosaicGLEntity);
        mosaicOperationItem.add(paintingItem);
        this.mMosaicOperationItems.add(mosaicOperationItem);
    }

    public void setCaptureListener(CaptureListener captureListener) {
        this.mCaptureListener = captureListener;
    }

    public void clearBuffer() {
        while (!this.mBufferItems.isEmpty()) {
            this.mBufferItems.removeLast().clear();
        }
        while (!this.mRevokedBufferItems.isEmpty()) {
            this.mRevokedBufferItems.removeLast().clear();
        }
        while (!this.mReuseBufferItem.isEmpty()) {
            this.mReuseBufferItem.pop().clear();
        }
    }

    /* loaded from: classes2.dex */
    public static class RevokedItem {
        public final MosaicGLEntity mosaicGLEntity;
        public final MosaicOperationItem.PaintingItem paintingItem;

        public RevokedItem(MosaicGLEntity mosaicGLEntity, MosaicOperationItem.PaintingItem paintingItem) {
            this.mosaicGLEntity = mosaicGLEntity;
            this.paintingItem = paintingItem;
        }
    }
}
