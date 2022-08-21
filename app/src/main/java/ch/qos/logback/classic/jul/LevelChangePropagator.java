package ch.qos.logback.classic.jul;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.LogManager;
import java.util.logging.Logger;

@Deprecated
/* loaded from: classes.dex */
public class LevelChangePropagator extends ContextAwareBase implements LoggerContextListener, LifeCycle {
    private Set<Logger> julLoggerSet = new HashSet();
    public boolean isStarted = false;
    public boolean resetJUL = false;

    @Override // ch.qos.logback.classic.spi.LoggerContextListener
    public boolean isResetResistant() {
        return false;
    }

    @Override // ch.qos.logback.classic.spi.LoggerContextListener
    public void onReset(LoggerContext loggerContext) {
    }

    @Override // ch.qos.logback.classic.spi.LoggerContextListener
    public void onStart(LoggerContext loggerContext) {
    }

    @Override // ch.qos.logback.classic.spi.LoggerContextListener
    public void onStop(LoggerContext loggerContext) {
    }

    public void setResetJUL(boolean z) {
        this.resetJUL = z;
    }

    @Override // ch.qos.logback.classic.spi.LoggerContextListener
    public void onLevelChange(ch.qos.logback.classic.Logger logger, Level level) {
        propagate(logger, level);
    }

    private void propagate(ch.qos.logback.classic.Logger logger, Level level) {
        addInfo("Propagating " + level + " level on " + logger + " onto the JUL framework");
        Logger asJULLogger = JULHelper.asJULLogger(logger);
        this.julLoggerSet.add(asJULLogger);
        asJULLogger.setLevel(JULHelper.asJULLevel(level));
    }

    public void resetJULLevels() {
        LogManager logManager = LogManager.getLogManager();
        Enumeration<String> loggerNames = logManager.getLoggerNames();
        while (loggerNames.hasMoreElements()) {
            String nextElement = loggerNames.nextElement();
            Logger logger = logManager.getLogger(nextElement);
            if (JULHelper.isRegularNonRootLogger(logger) && logger.getLevel() != null) {
                addInfo("Setting level of jul logger [" + nextElement + "] to null");
                logger.setLevel(null);
            }
        }
    }

    private void propagateExistingLoggerLevels() {
        for (ch.qos.logback.classic.Logger logger : ((LoggerContext) this.context).getLoggerList()) {
            if (logger.getLevel() != null) {
                propagate(logger, logger.getLevel());
            }
        }
    }

    @Override // ch.qos.logback.core.spi.LifeCycle
    public void start() {
        if (this.resetJUL) {
            resetJULLevels();
        }
        propagateExistingLoggerLevels();
        this.isStarted = true;
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
