package ch.qos.logback.core.rolling;

import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.rolling.helper.CompressionMode;
import ch.qos.logback.core.rolling.helper.FileNamePattern;
import ch.qos.logback.core.spi.ContextAwareBase;

/* loaded from: classes.dex */
public abstract class RollingPolicyBase extends ContextAwareBase implements RollingPolicy {
    public CompressionMode compressionMode = CompressionMode.NONE;
    public FileNamePattern fileNamePattern;
    public String fileNamePatternStr;
    private FileAppender<?> parent;
    private boolean started;
    public FileNamePattern zipEntryFileNamePattern;

    public void determineCompressionMode() {
        if (this.fileNamePatternStr.endsWith(".gz")) {
            addInfo("Will use gz compression");
            this.compressionMode = CompressionMode.GZ;
        } else if (this.fileNamePatternStr.endsWith(".zip")) {
            addInfo("Will use zip compression");
            this.compressionMode = CompressionMode.ZIP;
        } else {
            addInfo("No compression will be used");
            this.compressionMode = CompressionMode.NONE;
        }
    }

    public void setFileNamePattern(String str) {
        this.fileNamePatternStr = str;
    }

    public String getFileNamePattern() {
        return this.fileNamePatternStr;
    }

    @Override // ch.qos.logback.core.rolling.RollingPolicy
    public CompressionMode getCompressionMode() {
        return this.compressionMode;
    }

    @Override // ch.qos.logback.core.spi.LifeCycle
    public boolean isStarted() {
        return this.started;
    }

    public void start() {
        this.started = true;
    }

    @Override // ch.qos.logback.core.spi.LifeCycle
    public void stop() {
        this.started = false;
    }

    @Override // ch.qos.logback.core.rolling.RollingPolicy
    public void setParent(FileAppender<?> fileAppender) {
        this.parent = fileAppender;
    }

    public boolean isParentPrudent() {
        return this.parent.isPrudent();
    }

    public String getParentsRawFileProperty() {
        return this.parent.rawFileProperty();
    }
}
