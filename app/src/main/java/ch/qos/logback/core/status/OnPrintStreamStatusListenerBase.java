package ch.qos.logback.core.status;

import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.util.StatusPrinter;
import java.io.PrintStream;

/* loaded from: classes.dex */
public abstract class OnPrintStreamStatusListenerBase extends ContextAwareBase implements StatusListener, LifeCycle {
    public static final long DEFAULT_RETROSPECTIVE = 300;
    public String prefix;
    public boolean isStarted = false;
    public long retrospectiveThresold = 300;

    public abstract PrintStream getPrintStream();

    private void print(Status status) {
        StringBuilder sb = new StringBuilder();
        String str = this.prefix;
        if (str != null) {
            sb.append(str);
        }
        StatusPrinter.buildStr(sb, "", status);
        getPrintStream().print(sb);
    }

    @Override // ch.qos.logback.core.status.StatusListener
    public void addStatusEvent(Status status) {
        if (!this.isStarted) {
            return;
        }
        print(status);
    }

    private void retrospectivePrint() {
        if (this.context == null) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        for (Status status : this.context.getStatusManager().getCopyOfStatusList()) {
            if (isElapsedTimeLongerThanThreshold(currentTimeMillis, status.getDate().longValue())) {
                print(status);
            }
        }
    }

    private boolean isElapsedTimeLongerThanThreshold(long j, long j2) {
        return j - j2 < this.retrospectiveThresold;
    }

    @Override // ch.qos.logback.core.spi.LifeCycle
    public void start() {
        this.isStarted = true;
        if (this.retrospectiveThresold > 0) {
            retrospectivePrint();
        }
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String str) {
        this.prefix = str;
    }

    public void setRetrospective(long j) {
        this.retrospectiveThresold = j;
    }

    public long getRetrospective() {
        return this.retrospectiveThresold;
    }

    @Override // ch.qos.logback.core.spi.LifeCycle
    public void stop() {
        this.isStarted = false;
    }

    @Override // ch.qos.logback.core.spi.LifeCycle
    public boolean isStarted() {
        return this.isStarted;
    }
}
