package com.bumptech.glide.gifdecoder;

import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class GifHeader {
    public int bgColor;
    public int bgIndex;
    public GifFrame currentFrame;
    public boolean gctFlag;
    public int gctSize;
    public int height;
    public int pixelAspect;
    public int width;
    public int[] gct = null;
    public int status = 0;
    public int frameCount = 0;
    public final List<GifFrame> frames = new ArrayList();
    public int loopCount = -1;

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public int getNumFrames() {
        return this.frameCount;
    }

    public int getStatus() {
        return this.status;
    }
}
