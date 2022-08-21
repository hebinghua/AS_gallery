package ch.qos.logback.classic.spi;

import ch.qos.logback.classic.LoggerContext;
import java.io.Serializable;

/* loaded from: classes.dex */
public class LoggerRemoteView implements Serializable {
    public static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final long serialVersionUID = 5028223666108713696L;
    public final LoggerContextVO loggerContextView;
    public final String name;

    public LoggerRemoteView(String str, LoggerContext loggerContext) {
        this.name = str;
        this.loggerContextView = loggerContext.getLoggerContextRemoteView();
    }

    public LoggerContextVO getLoggerContextView() {
        return this.loggerContextView;
    }

    public String getName() {
        return this.name;
    }
}
