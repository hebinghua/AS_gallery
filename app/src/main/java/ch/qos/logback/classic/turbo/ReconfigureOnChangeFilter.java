package ch.qos.logback.classic.turbo;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.joran.ReconfigureOnChangeTask;
import ch.qos.logback.core.android.AndroidContextUtil;
import ch.qos.logback.core.joran.GenericConfigurator;
import ch.qos.logback.core.joran.event.SaxEvent;
import ch.qos.logback.core.joran.spi.ConfigurationWatchList;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.joran.util.ConfigurationWatchListUtil;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.status.StatusUtil;
import java.io.File;
import java.net.URL;
import java.util.List;
import org.slf4j.Marker;

/* loaded from: classes.dex */
public class ReconfigureOnChangeFilter extends TurboFilter {
    public static final long DEFAULT_REFRESH_PERIOD = 60000;
    private static final long MASK_DECREASE_THRESHOLD = 800;
    private static final long MASK_INCREASE_THRESHOLD = 100;
    private static final int MAX_MASK = 65535;
    public ConfigurationWatchList configurationWatchList;
    public URL mainConfigurationURL;
    public volatile long nextCheck;
    public long refreshPeriod = 60000;
    private long invocationCounter = 0;
    private volatile long mask = 15;
    private volatile long lastMaskCheck = System.currentTimeMillis();

    @Override // ch.qos.logback.classic.turbo.TurboFilter, ch.qos.logback.core.spi.LifeCycle
    public void start() {
        ConfigurationWatchList configurationWatchList = ConfigurationWatchListUtil.getConfigurationWatchList(this.context);
        this.configurationWatchList = configurationWatchList;
        if (configurationWatchList != null) {
            URL mainURL = configurationWatchList.getMainURL();
            this.mainConfigurationURL = mainURL;
            if (mainURL == null) {
                addWarn("Due to missing top level configuration file, automatic reconfiguration is impossible.");
                return;
            }
            List<File> copyOfFileWatchList = this.configurationWatchList.getCopyOfFileWatchList();
            addInfo("Will scan for changes in [" + copyOfFileWatchList + "] every " + (this.refreshPeriod / 1000) + " seconds. ");
            synchronized (this.configurationWatchList) {
                updateNextCheck(System.currentTimeMillis());
            }
            super.start();
            return;
        }
        addWarn("Empty ConfigurationWatchList in context");
    }

    public String toString() {
        return "ReconfigureOnChangeFilter{invocationCounter=" + this.invocationCounter + '}';
    }

    @Override // ch.qos.logback.classic.turbo.TurboFilter
    public FilterReply decide(Marker marker, Logger logger, Level level, String str, Object[] objArr, Throwable th) {
        if (!isStarted()) {
            return FilterReply.NEUTRAL;
        }
        long j = this.invocationCounter;
        this.invocationCounter = 1 + j;
        if ((j & this.mask) != this.mask) {
            return FilterReply.NEUTRAL;
        }
        long currentTimeMillis = System.currentTimeMillis();
        synchronized (this.configurationWatchList) {
            updateMaskIfNecessary(currentTimeMillis);
            if (changeDetected(currentTimeMillis)) {
                disableSubsequentReconfiguration();
                detachReconfigurationToNewThread();
            }
        }
        return FilterReply.NEUTRAL;
    }

    private void updateMaskIfNecessary(long j) {
        long j2 = j - this.lastMaskCheck;
        this.lastMaskCheck = j;
        if (j2 < MASK_INCREASE_THRESHOLD && this.mask < 65535) {
            this.mask = (this.mask << 1) | 1;
        } else if (j2 <= MASK_DECREASE_THRESHOLD) {
        } else {
            this.mask >>>= 2;
        }
    }

    public void detachReconfigurationToNewThread() {
        addInfo("Detected change in [" + this.configurationWatchList.getCopyOfFileWatchList() + "]");
        this.context.getScheduledExecutorService().submit(new ReconfiguringThread());
    }

    public void updateNextCheck(long j) {
        this.nextCheck = j + this.refreshPeriod;
    }

    public boolean changeDetected(long j) {
        if (j >= this.nextCheck) {
            updateNextCheck(j);
            return this.configurationWatchList.changeDetected();
        }
        return false;
    }

    public void disableSubsequentReconfiguration() {
        this.nextCheck = Long.MAX_VALUE;
    }

    public long getRefreshPeriod() {
        return this.refreshPeriod;
    }

    public void setRefreshPeriod(long j) {
        this.refreshPeriod = j;
    }

    /* loaded from: classes.dex */
    public class ReconfiguringThread implements Runnable {
        public ReconfiguringThread() {
        }

        @Override // java.lang.Runnable
        public void run() {
            ReconfigureOnChangeFilter reconfigureOnChangeFilter = ReconfigureOnChangeFilter.this;
            if (reconfigureOnChangeFilter.mainConfigurationURL != null) {
                LoggerContext loggerContext = (LoggerContext) reconfigureOnChangeFilter.context;
                ReconfigureOnChangeFilter reconfigureOnChangeFilter2 = ReconfigureOnChangeFilter.this;
                reconfigureOnChangeFilter2.addInfo("Will reset and reconfigure context named [" + ReconfigureOnChangeFilter.this.context.getName() + "]");
                if (!ReconfigureOnChangeFilter.this.mainConfigurationURL.toString().endsWith("xml")) {
                    return;
                }
                performXMLConfiguration(loggerContext);
                return;
            }
            reconfigureOnChangeFilter.addInfo("Due to missing top level configuration file, skipping reconfiguration");
        }

        private void performXMLConfiguration(LoggerContext loggerContext) {
            JoranConfigurator joranConfigurator = new JoranConfigurator();
            joranConfigurator.setContext(loggerContext);
            StatusUtil statusUtil = new StatusUtil(loggerContext);
            List<SaxEvent> recallSafeConfiguration = joranConfigurator.recallSafeConfiguration();
            URL mainWatchURL = ConfigurationWatchListUtil.getMainWatchURL(loggerContext);
            loggerContext.reset();
            new AndroidContextUtil().setupProperties(loggerContext);
            long currentTimeMillis = System.currentTimeMillis();
            try {
                joranConfigurator.doConfigure(ReconfigureOnChangeFilter.this.mainConfigurationURL);
                if (!statusUtil.hasXMLParsingErrors(currentTimeMillis)) {
                    return;
                }
                fallbackConfiguration(loggerContext, recallSafeConfiguration, mainWatchURL);
            } catch (JoranException unused) {
                fallbackConfiguration(loggerContext, recallSafeConfiguration, mainWatchURL);
            }
        }

        private void fallbackConfiguration(LoggerContext loggerContext, List<SaxEvent> list, URL url) {
            JoranConfigurator joranConfigurator = new JoranConfigurator();
            joranConfigurator.setContext(loggerContext);
            if (list != null) {
                ReconfigureOnChangeFilter.this.addWarn("Falling back to previously registered safe configuration.");
                try {
                    loggerContext.reset();
                    new AndroidContextUtil().setupProperties(loggerContext);
                    GenericConfigurator.informContextOfURLUsedForConfiguration(loggerContext, url);
                    joranConfigurator.doConfigure(list);
                    ReconfigureOnChangeFilter.this.addInfo(ReconfigureOnChangeTask.RE_REGISTERING_PREVIOUS_SAFE_CONFIGURATION);
                    joranConfigurator.registerSafeConfiguration(list);
                    return;
                } catch (JoranException e) {
                    ReconfigureOnChangeFilter.this.addError("Unexpected exception thrown by a configuration considered safe.", e);
                    return;
                }
            }
            ReconfigureOnChangeFilter.this.addWarn("No previous configuration to fall back on.");
        }
    }
}
