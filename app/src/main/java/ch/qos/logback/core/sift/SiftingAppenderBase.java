package ch.qos.logback.core.sift;

import ch.qos.logback.core.Appender;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.spi.ComponentTracker;
import ch.qos.logback.core.util.Duration;

/* loaded from: classes.dex */
public abstract class SiftingAppenderBase<E> extends AppenderBase<E> {
    public AppenderFactory<E> appenderFactory;
    public AppenderTracker<E> appenderTracker;
    public Discriminator<E> discriminator;
    public Duration timeout = new Duration(ComponentTracker.DEFAULT_TIMEOUT);
    public int maxAppenderCount = Integer.MAX_VALUE;

    public abstract boolean eventMarksEndOfLife(E e);

    public abstract long getTimestamp(E e);

    public Duration getTimeout() {
        return this.timeout;
    }

    public void setTimeout(Duration duration) {
        this.timeout = duration;
    }

    public int getMaxAppenderCount() {
        return this.maxAppenderCount;
    }

    public void setMaxAppenderCount(int i) {
        this.maxAppenderCount = i;
    }

    public void setAppenderFactory(AppenderFactory<E> appenderFactory) {
        this.appenderFactory = appenderFactory;
    }

    @Override // ch.qos.logback.core.AppenderBase, ch.qos.logback.core.spi.LifeCycle
    public void start() {
        int i;
        if (this.discriminator == null) {
            addError("Missing discriminator. Aborting");
            i = 1;
        } else {
            i = 0;
        }
        if (!this.discriminator.isStarted()) {
            addError("Discriminator has not started successfully. Aborting");
            i++;
        }
        AppenderFactory<E> appenderFactory = this.appenderFactory;
        if (appenderFactory == null) {
            addError("AppenderFactory has not been set. Aborting");
            i++;
        } else {
            AppenderTracker<E> appenderTracker = new AppenderTracker<>(this.context, appenderFactory);
            this.appenderTracker = appenderTracker;
            appenderTracker.setMaxComponents(this.maxAppenderCount);
            this.appenderTracker.setTimeout(this.timeout.getMilliseconds());
        }
        if (i == 0) {
            super.start();
        }
    }

    @Override // ch.qos.logback.core.AppenderBase, ch.qos.logback.core.spi.LifeCycle
    public void stop() {
        for (Appender<E> appender : this.appenderTracker.allComponents()) {
            appender.stop();
        }
    }

    @Override // ch.qos.logback.core.AppenderBase
    public void append(E e) {
        if (!isStarted()) {
            return;
        }
        String discriminatingValue = this.discriminator.getDiscriminatingValue(e);
        long timestamp = getTimestamp(e);
        Appender<E> orCreate = this.appenderTracker.getOrCreate(discriminatingValue, timestamp);
        if (eventMarksEndOfLife(e)) {
            this.appenderTracker.endOfLife(discriminatingValue);
        }
        this.appenderTracker.removeStaleComponents(timestamp);
        orCreate.doAppend(e);
    }

    public Discriminator<E> getDiscriminator() {
        return this.discriminator;
    }

    public void setDiscriminator(Discriminator<E> discriminator) {
        this.discriminator = discriminator;
    }

    public AppenderTracker<E> getAppenderTracker() {
        return this.appenderTracker;
    }

    public String getDiscriminatorKey() {
        Discriminator<E> discriminator = this.discriminator;
        if (discriminator != null) {
            return discriminator.getKey();
        }
        return null;
    }
}
