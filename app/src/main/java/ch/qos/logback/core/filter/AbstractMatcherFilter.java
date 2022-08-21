package ch.qos.logback.core.filter;

import ch.qos.logback.core.spi.FilterReply;

/* loaded from: classes.dex */
public abstract class AbstractMatcherFilter<E> extends Filter<E> {
    public FilterReply onMatch;
    public FilterReply onMismatch;

    public AbstractMatcherFilter() {
        FilterReply filterReply = FilterReply.NEUTRAL;
        this.onMatch = filterReply;
        this.onMismatch = filterReply;
    }

    public final void setOnMatch(FilterReply filterReply) {
        this.onMatch = filterReply;
    }

    public final void setOnMismatch(FilterReply filterReply) {
        this.onMismatch = filterReply;
    }

    public final FilterReply getOnMatch() {
        return this.onMatch;
    }

    public final FilterReply getOnMismatch() {
        return this.onMismatch;
    }
}
