package ch.qos.logback.core.hook;

import ch.qos.logback.core.util.Duration;
import com.miui.gallery.search.statistics.SearchStatUtils;

/* loaded from: classes.dex */
public class DefaultShutdownHook extends ShutdownHookBase {
    public static final Duration DEFAULT_DELAY = Duration.buildByMilliseconds(SearchStatUtils.POW);
    private Duration delay = DEFAULT_DELAY;

    public Duration getDelay() {
        return this.delay;
    }

    public void setDelay(Duration duration) {
        this.delay = duration;
    }

    @Override // java.lang.Runnable
    public void run() {
        if (this.delay.getMilliseconds() > 0) {
            addInfo("Sleeping for " + this.delay);
            try {
                Thread.sleep(this.delay.getMilliseconds());
            } catch (InterruptedException unused) {
            }
        }
        super.stop();
    }
}
