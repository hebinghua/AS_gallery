package com.meicam.sdk;

import java.nio.ByteBuffer;

/* loaded from: classes.dex */
public class NvsCustomVideoFx {

    /* loaded from: classes.dex */
    public static class RenderContext {
        public long effectEndTime;
        public long effectStartTime;
        public long effectTime;
        public boolean hasBuddyVideoFrame;
        public RenderHelper helper;
        public NvsVideoFrameInfo inputBuddyVideoFrameInfo;
        public ByteBuffer inputBuddyVideoFramebuffer;
        public VideoFrame inputVideoFrame;
        public VideoFrame outputVideoFrame;
    }

    /* loaded from: classes.dex */
    public interface RenderHelper {
        int allocateRGBATexture(int i, int i2);

        void reclaimTexture(int i);
    }

    /* loaded from: classes.dex */
    public interface Renderer {
        void onCleanup();

        void onInit();

        void onPreloadResources();

        void onRender(RenderContext renderContext);
    }

    /* loaded from: classes.dex */
    public static class VideoFrame {
        public int height;
        public boolean isUpsideDownTexture;
        public int texId;
        public int width;
    }
}
