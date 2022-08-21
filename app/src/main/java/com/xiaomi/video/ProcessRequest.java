package com.xiaomi.video;

import com.xiaomi.video.FrameHandler;
import java.io.File;

/* loaded from: classes3.dex */
public class ProcessRequest {
    public final File inputFile;
    public final File outputFile;
    public FrameHandler.VideoProcessor processor;

    public ProcessRequest(File file, File file2, FrameHandler.VideoProcessor videoProcessor) {
        this.inputFile = file;
        this.outputFile = file2;
        this.processor = videoProcessor;
    }

    public String toString() {
        return "inputFile:" + this.inputFile.getName() + ", outputFile:" + this.outputFile.getName();
    }

    /* loaded from: classes3.dex */
    public static class Builder {
        private File inputFile;
        private File outputFile;
        private FrameHandler.VideoProcessor processor;

        public Builder from(String str) {
            return from(new File(str));
        }

        public Builder from(File file) {
            this.inputFile = file;
            return this;
        }

        public Builder handleBy(FrameHandler.VideoProcessor videoProcessor) {
            this.processor = videoProcessor;
            return this;
        }

        public Builder to(String str) {
            return to(new File(str));
        }

        public Builder to(File file) {
            this.outputFile = file;
            return this;
        }

        public ProcessRequest build() {
            if (!checkParam()) {
                return null;
            }
            return new ProcessRequest(this.inputFile, this.outputFile, this.processor);
        }

        private boolean checkParam() {
            return (this.inputFile == null || this.outputFile == null || this.processor == null) ? false : true;
        }
    }
}
