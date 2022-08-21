package ch.qos.logback.classic.turbo;

import ch.qos.logback.core.spi.FilterReply;

/* loaded from: classes.dex */
public abstract class MatchingFilter extends TurboFilter {
    public FilterReply onMatch;
    public FilterReply onMismatch;

    public MatchingFilter() {
        FilterReply filterReply = FilterReply.NEUTRAL;
        this.onMatch = filterReply;
        this.onMismatch = filterReply;
    }

    public final void setOnMatch(String str) {
        if ("NEUTRAL".equals(str)) {
            this.onMatch = FilterReply.NEUTRAL;
        } else if ("ACCEPT".equals(str)) {
            this.onMatch = FilterReply.ACCEPT;
        } else if (!"DENY".equals(str)) {
        } else {
            this.onMatch = FilterReply.DENY;
        }
    }

    public final void setOnMismatch(String str) {
        if ("NEUTRAL".equals(str)) {
            this.onMismatch = FilterReply.NEUTRAL;
        } else if ("ACCEPT".equals(str)) {
            this.onMismatch = FilterReply.ACCEPT;
        } else if (!"DENY".equals(str)) {
        } else {
            this.onMismatch = FilterReply.DENY;
        }
    }
}
