package com.miui.gallery.util.logger;

import android.util.Log;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import org.slf4j.Marker;

/* loaded from: classes2.dex */
public class LogcatAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    public LayoutWrappingEncoder<ILoggingEvent> encoder = null;
    public LayoutWrappingEncoder<ILoggingEvent> tagEncoder = null;
    public boolean checkLoggable = false;

    @Override // ch.qos.logback.core.UnsynchronizedAppenderBase, ch.qos.logback.core.spi.LifeCycle
    public void start() {
        LayoutWrappingEncoder<ILoggingEvent> layoutWrappingEncoder = this.encoder;
        if (layoutWrappingEncoder == null || layoutWrappingEncoder.getLayout() == null) {
            addError("No layout set for the appender named [" + this.name + "].");
            return;
        }
        LayoutWrappingEncoder<ILoggingEvent> layoutWrappingEncoder2 = this.tagEncoder;
        if (layoutWrappingEncoder2 != null) {
            Layout<ILoggingEvent> layout = layoutWrappingEncoder2.getLayout();
            if (layout == null) {
                addError("No tag layout set for the appender named [" + this.name + "].");
                return;
            }
            LayoutWrappingEncoder<ILoggingEvent> layoutWrappingEncoder3 = this.tagEncoder;
            if (layoutWrappingEncoder3 instanceof PatternLayoutEncoder) {
                String pattern = ((PatternLayoutEncoder) layoutWrappingEncoder3).getPattern();
                if (!pattern.contains("%nopex")) {
                    this.tagEncoder.stop();
                    ((PatternLayoutEncoder) this.tagEncoder).setPattern(pattern + "%nopex");
                    this.tagEncoder.start();
                }
                ((PatternLayout) layout).setPostCompileProcessor(null);
            }
        }
        super.start();
    }

    @Override // ch.qos.logback.core.UnsynchronizedAppenderBase
    public void append(ILoggingEvent iLoggingEvent) {
        if (!isStarted()) {
            return;
        }
        String tag = getTag(iLoggingEvent);
        int i = iLoggingEvent.getLevel().levelInt;
        if (i == Integer.MIN_VALUE || i == 5000) {
            if (this.checkLoggable && !Log.isLoggable(tag, 2)) {
                return;
            }
            Log.v(tag, this.encoder.getLayout().doLayout(iLoggingEvent));
        } else if (i == 10000) {
            if (this.checkLoggable && !Log.isLoggable(tag, 3)) {
                return;
            }
            Log.d(tag, this.encoder.getLayout().doLayout(iLoggingEvent));
        } else if (i == 20000) {
            if (this.checkLoggable && !Log.isLoggable(tag, 4)) {
                return;
            }
            Log.i(tag, this.encoder.getLayout().doLayout(iLoggingEvent));
        } else if (i == 30000) {
            if (this.checkLoggable && !Log.isLoggable(tag, 5)) {
                return;
            }
            Log.w(tag, this.encoder.getLayout().doLayout(iLoggingEvent));
        } else if (i != 40000) {
        } else {
            if (this.checkLoggable && !Log.isLoggable(tag, 6)) {
                return;
            }
            Log.e(tag, this.encoder.getLayout().doLayout(iLoggingEvent));
        }
    }

    public void setEncoder(LayoutWrappingEncoder<ILoggingEvent> layoutWrappingEncoder) {
        this.encoder = layoutWrappingEncoder;
    }

    public void setTagEncoder(LayoutWrappingEncoder<ILoggingEvent> layoutWrappingEncoder) {
        this.tagEncoder = layoutWrappingEncoder;
    }

    public String getTag(ILoggingEvent iLoggingEvent) {
        LayoutWrappingEncoder<ILoggingEvent> layoutWrappingEncoder = this.tagEncoder;
        String doLayout = layoutWrappingEncoder != null ? layoutWrappingEncoder.getLayout().doLayout(iLoggingEvent) : iLoggingEvent.getLoggerName();
        if (!this.checkLoggable || doLayout.length() <= 23) {
            return doLayout;
        }
        return doLayout.substring(0, 22) + Marker.ANY_MARKER;
    }
}
