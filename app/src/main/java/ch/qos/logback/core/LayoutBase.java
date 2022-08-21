package ch.qos.logback.core;

import ch.qos.logback.core.spi.ContextAwareBase;

/* loaded from: classes.dex */
public abstract class LayoutBase<E> extends ContextAwareBase implements Layout<E> {
    public String fileFooter;
    public String fileHeader;
    public String presentationFooter;
    public String presentationHeader;
    public boolean started;

    public String getContentType() {
        return "text/plain";
    }

    @Override // ch.qos.logback.core.spi.ContextAwareBase, ch.qos.logback.core.spi.ContextAware
    public void setContext(Context context) {
        this.context = context;
    }

    @Override // ch.qos.logback.core.spi.ContextAwareBase, ch.qos.logback.core.spi.ContextAware
    public Context getContext() {
        return this.context;
    }

    public void start() {
        this.started = true;
    }

    @Override // ch.qos.logback.core.spi.LifeCycle
    public void stop() {
        this.started = false;
    }

    @Override // ch.qos.logback.core.spi.LifeCycle
    public boolean isStarted() {
        return this.started;
    }

    @Override // ch.qos.logback.core.Layout
    public String getFileHeader() {
        return this.fileHeader;
    }

    @Override // ch.qos.logback.core.Layout
    public String getPresentationHeader() {
        return this.presentationHeader;
    }

    @Override // ch.qos.logback.core.Layout
    public String getPresentationFooter() {
        return this.presentationFooter;
    }

    @Override // ch.qos.logback.core.Layout
    public String getFileFooter() {
        return this.fileFooter;
    }

    public void setFileHeader(String str) {
        this.fileHeader = str;
    }

    public void setFileFooter(String str) {
        this.fileFooter = str;
    }

    public void setPresentationHeader(String str) {
        this.presentationHeader = str;
    }

    public void setPresentationFooter(String str) {
        this.presentationFooter = str;
    }
}
