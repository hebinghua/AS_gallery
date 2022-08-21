package com.meicam.sdk;

/* loaded from: classes.dex */
public class NvsCustomAudioFx {

    /* loaded from: classes.dex */
    public static class RenderContext {
        public long effectEndTime;
        public long effectStartTime;
        public long effectTime;
        public NvsAudioSampleBuffers inputAudioSample;
    }

    /* loaded from: classes.dex */
    public interface Renderer {
        void onCleanup();

        NvsAudioSampleBuffers onFlush();

        void onInit();

        NvsAudioSampleBuffers onRender(RenderContext renderContext);

        int querySupportedInputAudioSampleFormat();
    }
}
