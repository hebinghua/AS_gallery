package ch.qos.logback.classic.android;

import android.util.Log;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.UnsynchronizedAppenderBase;
import org.slf4j.Marker;

/* loaded from: classes.dex */
public class LogcatAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {
    private static final int MAX_TAG_LENGTH = 23;
    private PatternLayoutEncoder encoder = null;
    private PatternLayoutEncoder tagEncoder = null;
    private boolean checkLoggable = false;

    @Override // ch.qos.logback.core.UnsynchronizedAppenderBase, ch.qos.logback.core.spi.LifeCycle
    public void start() {
        PatternLayoutEncoder patternLayoutEncoder = this.encoder;
        if (patternLayoutEncoder == null || patternLayoutEncoder.getLayout() == null) {
            addError("No layout set for the appender named [" + this.name + "].");
            return;
        }
        PatternLayoutEncoder patternLayoutEncoder2 = this.tagEncoder;
        if (patternLayoutEncoder2 != null) {
            Layout<ILoggingEvent> layout = patternLayoutEncoder2.getLayout();
            if (layout == null) {
                addError("No tag layout set for the appender named [" + this.name + "].");
                return;
            } else if (layout instanceof PatternLayout) {
                String pattern = this.tagEncoder.getPattern();
                if (!pattern.contains("%nopex")) {
                    this.tagEncoder.stop();
                    PatternLayoutEncoder patternLayoutEncoder3 = this.tagEncoder;
                    patternLayoutEncoder3.setPattern(pattern + "%nopex");
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

    public PatternLayoutEncoder getEncoder() {
        return this.encoder;
    }

    public void setEncoder(PatternLayoutEncoder patternLayoutEncoder) {
        this.encoder = patternLayoutEncoder;
    }

    public PatternLayoutEncoder getTagEncoder() {
        return this.tagEncoder;
    }

    public void setTagEncoder(PatternLayoutEncoder patternLayoutEncoder) {
        this.tagEncoder = patternLayoutEncoder;
    }

    public void setCheckLoggable(boolean z) {
        this.checkLoggable = z;
    }

    public boolean getCheckLoggable() {
        return this.checkLoggable;
    }

    public String getTag(ILoggingEvent iLoggingEvent) {
        PatternLayoutEncoder patternLayoutEncoder = this.tagEncoder;
        String doLayout = patternLayoutEncoder != null ? patternLayoutEncoder.getLayout().doLayout(iLoggingEvent) : iLoggingEvent.getLoggerName();
        if (!this.checkLoggable || doLayout.length() <= 23) {
            return doLayout;
        }
        return doLayout.substring(0, 22) + Marker.ANY_MARKER;
    }
}
