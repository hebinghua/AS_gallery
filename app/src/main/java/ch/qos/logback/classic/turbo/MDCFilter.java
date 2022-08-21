package ch.qos.logback.classic.turbo;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.MDC;
import org.slf4j.Marker;

/* loaded from: classes.dex */
public class MDCFilter extends MatchingFilter {
    public String MDCKey;
    public String value;

    @Override // ch.qos.logback.classic.turbo.TurboFilter, ch.qos.logback.core.spi.LifeCycle
    public void start() {
        int i;
        if (this.value == null) {
            addError("'value' parameter is mandatory. Cannot start.");
            i = 1;
        } else {
            i = 0;
        }
        if (this.MDCKey == null) {
            addError("'MDCKey' parameter is mandatory. Cannot start.");
            i++;
        }
        if (i == 0) {
            this.start = true;
        }
    }

    @Override // ch.qos.logback.classic.turbo.TurboFilter
    public FilterReply decide(Marker marker, Logger logger, Level level, String str, Object[] objArr, Throwable th) {
        if (!isStarted()) {
            return FilterReply.NEUTRAL;
        }
        if (this.value.equals(MDC.get(this.MDCKey))) {
            return this.onMatch;
        }
        return this.onMismatch;
    }

    public void setValue(String str) {
        this.value = str;
    }

    public void setMDCKey(String str) {
        this.MDCKey = str;
    }
}
