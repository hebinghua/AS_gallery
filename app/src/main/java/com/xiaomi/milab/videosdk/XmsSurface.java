package com.xiaomi.milab.videosdk;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/* loaded from: classes3.dex */
public class XmsSurface extends SurfaceView implements SurfaceHolder.Callback {
    private boolean isCreated;
    private int mHeight;
    private Surface mSurface;
    private int mWidth;

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
    }

    public XmsSurface(Context context) {
        super(context);
        this.isCreated = false;
        getHolder().addCallback(this);
    }

    public XmsSurface(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.isCreated = false;
        getHolder().addCallback(this);
    }

    public Surface getSurface() {
        return this.mSurface;
    }

    public boolean isCreated() {
        return this.isCreated;
    }

    @Override // android.view.SurfaceView, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        this.isCreated = true;
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
        this.mSurface = surfaceHolder.getSurface();
        this.mWidth = i2;
        this.mHeight = i3;
    }
}
