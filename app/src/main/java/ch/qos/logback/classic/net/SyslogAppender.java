package ch.qos.logback.classic.net;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.pattern.SyslogStartConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.classic.util.LevelToSyslogSeverity;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.net.SyslogAppenderBase;
import ch.qos.logback.core.net.SyslogOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketException;
import java.net.UnknownHostException;

/* loaded from: classes.dex */
public class SyslogAppender extends SyslogAppenderBase<ILoggingEvent> {
    public static final String DEFAULT_STACKTRACE_PATTERN = "\t";
    public static final String DEFAULT_SUFFIX_PATTERN = "[%thread] %logger %msg";
    public PatternLayout stackTraceLayout = new PatternLayout();
    public String stackTracePattern = DEFAULT_STACKTRACE_PATTERN;
    public boolean throwableExcluded = false;

    public boolean stackTraceHeaderLine(StringBuilder sb, boolean z) {
        return false;
    }

    @Override // ch.qos.logback.core.net.SyslogAppenderBase, ch.qos.logback.core.AppenderBase, ch.qos.logback.core.spi.LifeCycle
    public void start() {
        super.start();
        setupStackTraceLayout();
    }

    public String getPrefixPattern() {
        return "%syslogStart{" + getFacility() + "}%nopex{}";
    }

    @Override // ch.qos.logback.core.net.SyslogAppenderBase
    public SyslogOutputStream createOutputStream() throws SocketException, UnknownHostException {
        return new SyslogOutputStream(getSyslogHost(), getPort());
    }

    @Override // ch.qos.logback.core.net.SyslogAppenderBase
    public int getSeverityForEvent(Object obj) {
        return LevelToSyslogSeverity.convert((ILoggingEvent) obj);
    }

    @Override // ch.qos.logback.core.net.SyslogAppenderBase
    public void postProcess(Object obj, OutputStream outputStream) {
        ILoggingEvent iLoggingEvent;
        IThrowableProxy throwableProxy;
        if (!this.throwableExcluded && (throwableProxy = (iLoggingEvent = (ILoggingEvent) obj).getThrowableProxy()) != null) {
            String doLayout = this.stackTraceLayout.doLayout(iLoggingEvent);
            boolean z = true;
            while (throwableProxy != null) {
                StackTraceElementProxy[] stackTraceElementProxyArray = throwableProxy.getStackTraceElementProxyArray();
                try {
                    handleThrowableFirstLine(outputStream, throwableProxy, doLayout, z);
                    for (StackTraceElementProxy stackTraceElementProxy : stackTraceElementProxyArray) {
                        outputStream.write((doLayout + stackTraceElementProxy).getBytes());
                        outputStream.flush();
                    }
                    throwableProxy = throwableProxy.getCause();
                    z = false;
                } catch (IOException unused) {
                    return;
                }
            }
        }
    }

    private void handleThrowableFirstLine(OutputStream outputStream, IThrowableProxy iThrowableProxy, String str, boolean z) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        if (!z) {
            sb.append(CoreConstants.CAUSED_BY);
        }
        sb.append(iThrowableProxy.getClassName());
        sb.append(": ");
        sb.append(iThrowableProxy.getMessage());
        outputStream.write(sb.toString().getBytes());
        outputStream.flush();
    }

    @Override // ch.qos.logback.core.net.SyslogAppenderBase
    public Layout<ILoggingEvent> buildLayout() {
        PatternLayout patternLayout = new PatternLayout();
        patternLayout.getInstanceConverterMap().put("syslogStart", SyslogStartConverter.class.getName());
        if (this.suffixPattern == null) {
            this.suffixPattern = DEFAULT_SUFFIX_PATTERN;
        }
        patternLayout.setPattern(getPrefixPattern() + this.suffixPattern);
        patternLayout.setContext(getContext());
        patternLayout.start();
        return patternLayout;
    }

    private void setupStackTraceLayout() {
        this.stackTraceLayout.getInstanceConverterMap().put("syslogStart", SyslogStartConverter.class.getName());
        PatternLayout patternLayout = this.stackTraceLayout;
        patternLayout.setPattern(getPrefixPattern() + this.stackTracePattern);
        this.stackTraceLayout.setContext(getContext());
        this.stackTraceLayout.start();
    }

    public boolean isThrowableExcluded() {
        return this.throwableExcluded;
    }

    public void setThrowableExcluded(boolean z) {
        this.throwableExcluded = z;
    }

    public String getStackTracePattern() {
        return this.stackTracePattern;
    }

    public void setStackTracePattern(String str) {
        this.stackTracePattern = str;
    }
}
