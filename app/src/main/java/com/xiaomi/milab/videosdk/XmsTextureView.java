package com.xiaomi.milab.videosdk;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;
import com.xiaomi.milab.videosdk.interfaces.SurfaceCreatedCallback;

/* loaded from: classes3.dex */
public class XmsTextureView extends TextureView implements TextureView.SurfaceTextureListener {
    public int height;
    private boolean isCreated;
    private Surface mSurface;
    private SurfaceCreatedCallback surfaceCreatedLister;
    public int width;

    @Override // android.view.TextureView.SurfaceTextureListener
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }

    public XmsTextureView(Context context) {
        super(context);
        this.isCreated = false;
        setSurfaceTextureListener(this);
    }

    public XmsTextureView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.isCreated = false;
        setSurfaceTextureListener(this);
    }

    public XmsTextureView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.isCreated = false;
        setSurfaceTextureListener(this);
    }

    public XmsTextureView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.isCreated = false;
        setSurfaceTextureListener(this);
    }

    public Surface getSurface() {
        return this.mSurface;
    }

    public void setSurface(Surface surface) {
        this.mSurface = surface;
    }

    public boolean isCreated() {
        return this.isCreated;
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        this.isCreated = true;
        this.mSurface = new Surface(surfaceTexture);
        this.width = i;
        this.height = i2;
        SurfaceCreatedCallback surfaceCreatedCallback = this.surfaceCreatedLister;
        if (surfaceCreatedCallback != null) {
            surfaceCreatedCallback.SurfaceCreated();
        }
    }

    @Override // android.view.TextureView.SurfaceTextureListener
    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
        this.width = i;
        this.height = i2;
    }

    public void setCreatedLister(SurfaceCreatedCallback surfaceCreatedCallback) {
        this.surfaceCreatedLister = surfaceCreatedCallback;
    }
}
