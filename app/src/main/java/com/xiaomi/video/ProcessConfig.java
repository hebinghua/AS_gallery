package com.xiaomi.video;

/* loaded from: classes3.dex */
public class ProcessConfig {
    public final int decodeFrameInterval;
    public final Class inputClazz;
    public final Class outputClazz;

    public ProcessConfig(Class cls, Class cls2, int i) {
        if (cls == null) {
            new FrameInfo();
            this.inputClazz = FrameInfo.class;
        } else {
            this.inputClazz = cls;
        }
        if (cls2 == null) {
            new FrameInfo();
            this.outputClazz = FrameInfo.class;
        } else {
            this.outputClazz = cls2;
        }
        this.decodeFrameInterval = i;
    }

    /* loaded from: classes3.dex */
    public static class Builder {
        private int decodeFrameInterval = 1;
        private Class inputClazz;
        private Class outputClazz;

        public Builder inputClazz(Class cls) {
            this.inputClazz = cls;
            return this;
        }

        public Builder outputClazz(Class cls) {
            this.outputClazz = cls;
            return this;
        }

        public Builder decodeFrameInterval(int i) {
            this.decodeFrameInterval = i;
            return this;
        }

        public ProcessConfig build() {
            return new ProcessConfig(this.inputClazz, this.outputClazz, this.decodeFrameInterval);
        }
    }
}
