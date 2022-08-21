package com.miui.gallery.editor.photo.screen.mosaic;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import com.miui.gallery.editor.photo.screen.mosaic.shader.MosaicEntity;
import com.miui.gallery.editor.photo.screen.mosaic.shader.MosaicShaderHolder;
import com.miui.gallery.util.concurrent.ThreadManager;

/* loaded from: classes2.dex */
public class RenderThread extends HandlerThread implements Handler.Callback {
    public Handler mHandler;
    public Handler mMainHandler;
    public RenderListener mRenderListener;

    /* loaded from: classes2.dex */
    public interface RenderListener {
        void onShaderComplete(MosaicShaderHolder mosaicShaderHolder);
    }

    /* loaded from: classes2.dex */
    public static class RenderShaderData {
        public Matrix bitmapMatrix;
        public Bitmap current;
        public MosaicEntity mosaicEntity;
    }

    /* renamed from: $r8$lambda$kw2mUrD55-r5i7rV19V3p0_w_6o */
    public static /* synthetic */ void m933$r8$lambda$kw2mUrD55r5i7rV19V3p0_w_6o(RenderThread renderThread, MosaicShaderHolder mosaicShaderHolder) {
        renderThread.lambda$handleMessage$0(mosaicShaderHolder);
    }

    public RenderThread() {
        super("mosaic_render_thread");
        this.mMainHandler = ThreadManager.getMainHandler();
        start();
        this.mHandler = new Handler(getLooper(), this);
    }

    @Override // android.os.Handler.Callback
    public boolean handleMessage(Message message) {
        Bitmap bitmap;
        if (message.what != 0) {
            return true;
        }
        RenderShaderData renderShaderData = (RenderShaderData) message.obj;
        if (renderShaderData == null || (bitmap = renderShaderData.current) == null) {
            return false;
        }
        final MosaicShaderHolder generateShader = renderShaderData.mosaicEntity.generateShader(bitmap, renderShaderData.bitmapMatrix);
        if (this.mRenderListener == null) {
            return true;
        }
        this.mMainHandler.post(new Runnable() { // from class: com.miui.gallery.editor.photo.screen.mosaic.RenderThread$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                RenderThread.m933$r8$lambda$kw2mUrD55r5i7rV19V3p0_w_6o(RenderThread.this, generateShader);
            }
        });
        return true;
    }

    public /* synthetic */ void lambda$handleMessage$0(MosaicShaderHolder mosaicShaderHolder) {
        RenderListener renderListener = this.mRenderListener;
        if (renderListener != null) {
            renderListener.onShaderComplete(mosaicShaderHolder);
        }
    }

    public void sendGenerateShaderMsg(MosaicEntity mosaicEntity, Bitmap bitmap, Matrix matrix) {
        this.mHandler.removeMessages(0);
        RenderShaderData renderShaderData = new RenderShaderData();
        renderShaderData.mosaicEntity = mosaicEntity;
        renderShaderData.current = bitmap;
        renderShaderData.bitmapMatrix = matrix;
        Message obtain = Message.obtain();
        obtain.what = 0;
        obtain.obj = renderShaderData;
        this.mHandler.sendMessage(obtain);
    }

    public void setRenderListener(RenderListener renderListener) {
        this.mRenderListener = renderListener;
    }
}
