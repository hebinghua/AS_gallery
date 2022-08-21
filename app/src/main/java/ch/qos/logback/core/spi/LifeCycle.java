package ch.qos.logback.core.spi;

/* loaded from: classes.dex */
public interface LifeCycle {
    boolean isStarted();

    void start();

    void stop();
}
