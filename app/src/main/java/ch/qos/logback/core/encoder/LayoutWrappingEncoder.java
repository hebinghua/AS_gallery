package ch.qos.logback.core.encoder;

import ch.qos.logback.core.Appender;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.OutputStreamAppender;
import java.nio.charset.Charset;

/* loaded from: classes.dex */
public class LayoutWrappingEncoder<E> extends EncoderBase<E> {
    private Charset charset;
    public Boolean immediateFlush = null;
    public Layout<E> layout;
    public Appender<?> parent;

    @Override // ch.qos.logback.core.encoder.EncoderBase, ch.qos.logback.core.spi.LifeCycle
    public boolean isStarted() {
        return false;
    }

    public Layout<E> getLayout() {
        return this.layout;
    }

    public void setLayout(Layout<E> layout) {
        this.layout = layout;
    }

    public Charset getCharset() {
        return this.charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public void setImmediateFlush(boolean z) {
        addWarn("As of version 1.2.0 \"immediateFlush\" property should be set within the enclosing Appender.");
        addWarn("Please move \"immediateFlush\" property into the enclosing appender.");
        this.immediateFlush = Boolean.valueOf(z);
    }

    @Override // ch.qos.logback.core.encoder.Encoder
    public byte[] headerBytes() {
        if (this.layout == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        appendIfNotNull(sb, this.layout.getFileHeader());
        appendIfNotNull(sb, this.layout.getPresentationHeader());
        if (sb.length() > 0) {
            sb.append(CoreConstants.LINE_SEPARATOR);
        }
        return convertToBytes(sb.toString());
    }

    @Override // ch.qos.logback.core.encoder.Encoder
    public byte[] footerBytes() {
        if (this.layout == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        appendIfNotNull(sb, this.layout.getPresentationFooter());
        appendIfNotNull(sb, this.layout.getFileFooter());
        return convertToBytes(sb.toString());
    }

    private byte[] convertToBytes(String str) {
        Charset charset = this.charset;
        if (charset == null) {
            return str.getBytes();
        }
        return str.getBytes(charset);
    }

    @Override // ch.qos.logback.core.encoder.Encoder
    public byte[] encode(E e) {
        return convertToBytes(this.layout.doLayout(e));
    }

    @Override // ch.qos.logback.core.encoder.EncoderBase, ch.qos.logback.core.spi.LifeCycle
    public void start() {
        if (this.immediateFlush != null) {
            if (this.parent instanceof OutputStreamAppender) {
                addWarn("Setting the \"immediateFlush\" property of the enclosing appender to " + this.immediateFlush);
                ((OutputStreamAppender) this.parent).setImmediateFlush(this.immediateFlush.booleanValue());
            } else {
                addError("Could not set the \"immediateFlush\" property of the enclosing appender.");
            }
        }
        this.started = true;
    }

    @Override // ch.qos.logback.core.encoder.EncoderBase, ch.qos.logback.core.spi.LifeCycle
    public void stop() {
        this.started = false;
    }

    private void appendIfNotNull(StringBuilder sb, String str) {
        if (str != null) {
            sb.append(str);
        }
    }

    public void setParent(Appender<?> appender) {
        this.parent = appender;
    }
}
