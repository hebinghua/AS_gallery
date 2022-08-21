package ch.qos.logback.classic;

import ch.qos.logback.classic.spi.LoggerComparator;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.classic.spi.LoggerContextVO;
import ch.qos.logback.classic.spi.TurboFilterList;
import ch.qos.logback.classic.turbo.TurboFilter;
import ch.qos.logback.classic.util.LoggerNameUtil;
import ch.qos.logback.core.ContextBase;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.status.StatusListener;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.status.WarnStatus;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import org.slf4j.ILoggerFactory;
import org.slf4j.Marker;

/* loaded from: classes.dex */
public class LoggerContext extends ContextBase implements ILoggerFactory {
    public static final boolean DEFAULT_PACKAGING_DATA = false;
    private List<String> frameworkPackages;
    public final Logger root;
    private int size;
    private int noAppenderWarning = 0;
    private final List<LoggerContextListener> loggerContextListenerList = new ArrayList();
    private final TurboFilterList turboFilterList = new TurboFilterList();
    private boolean packagingDataEnabled = false;
    private int maxCallerDataDepth = 8;
    public int resetCount = 0;
    private Map<String, Logger> loggerCache = new ConcurrentHashMap();
    private LoggerContextVO loggerContextRemoteView = new LoggerContextVO(this);

    public LoggerContext() {
        Logger logger = new Logger(org.slf4j.Logger.ROOT_LOGGER_NAME, null, this);
        this.root = logger;
        logger.setLevel(Level.DEBUG);
        this.loggerCache.put(org.slf4j.Logger.ROOT_LOGGER_NAME, logger);
        initEvaluatorMap();
        this.size = 1;
        this.frameworkPackages = new ArrayList();
    }

    public void initEvaluatorMap() {
        putObject(CoreConstants.EVALUATOR_MAP, new HashMap());
    }

    private void updateLoggerContextVO() {
        this.loggerContextRemoteView = new LoggerContextVO(this);
    }

    @Override // ch.qos.logback.core.ContextBase, ch.qos.logback.core.Context
    public void putProperty(String str, String str2) {
        super.putProperty(str, str2);
        updateLoggerContextVO();
    }

    public void putProperties(Properties properties) {
        for (String str : properties.stringPropertyNames()) {
            super.putProperty(str, properties.getProperty(str));
        }
        updateLoggerContextVO();
    }

    @Override // ch.qos.logback.core.ContextBase, ch.qos.logback.core.Context
    public void setName(String str) {
        super.setName(str);
        updateLoggerContextVO();
    }

    public final Logger getLogger(Class<?> cls) {
        return mo166getLogger(cls.getName());
    }

    @Override // org.slf4j.ILoggerFactory
    /* renamed from: getLogger */
    public Logger mo166getLogger(String str) {
        Logger childByName;
        if (str == null) {
            throw new IllegalArgumentException("name argument cannot be null");
        }
        if (org.slf4j.Logger.ROOT_LOGGER_NAME.equalsIgnoreCase(str)) {
            return this.root;
        }
        Logger logger = this.root;
        Logger logger2 = this.loggerCache.get(str);
        if (logger2 != null) {
            return logger2;
        }
        int i = 0;
        while (true) {
            int separatorIndexOf = LoggerNameUtil.getSeparatorIndexOf(str, i);
            String substring = separatorIndexOf == -1 ? str : str.substring(0, separatorIndexOf);
            int i2 = separatorIndexOf + 1;
            synchronized (logger) {
                childByName = logger.getChildByName(substring);
                if (childByName == null) {
                    childByName = logger.createChildByName(substring);
                    this.loggerCache.put(substring, childByName);
                    incSize();
                }
            }
            if (separatorIndexOf == -1) {
                return childByName;
            }
            i = i2;
            logger = childByName;
        }
    }

    private void incSize() {
        this.size++;
    }

    public int size() {
        return this.size;
    }

    public Logger exists(String str) {
        return this.loggerCache.get(str);
    }

    public final void noAppenderDefinedWarning(Logger logger) {
        int i = this.noAppenderWarning;
        this.noAppenderWarning = i + 1;
        if (i == 0) {
            StatusManager statusManager = getStatusManager();
            statusManager.add(new WarnStatus("No appenders present in context [" + getName() + "] for logger [" + logger.getName() + "].", logger));
        }
    }

    public List<Logger> getLoggerList() {
        ArrayList arrayList = new ArrayList(this.loggerCache.values());
        Collections.sort(arrayList, new LoggerComparator());
        return arrayList;
    }

    public LoggerContextVO getLoggerContextRemoteView() {
        return this.loggerContextRemoteView;
    }

    public void setPackagingDataEnabled(boolean z) {
        this.packagingDataEnabled = z;
    }

    public boolean isPackagingDataEnabled() {
        return this.packagingDataEnabled;
    }

    @Override // ch.qos.logback.core.ContextBase
    public void reset() {
        this.resetCount++;
        super.reset();
        initEvaluatorMap();
        initCollisionMaps();
        this.root.recursiveReset();
        resetTurboFilterList();
        cancelScheduledTasks();
        fireOnReset();
        resetListenersExceptResetResistant();
        resetStatusListeners();
    }

    private void cancelScheduledTasks() {
        for (ScheduledFuture<?> scheduledFuture : this.scheduledFutures) {
            scheduledFuture.cancel(false);
        }
        this.scheduledFutures.clear();
    }

    private void resetStatusListeners() {
        StatusManager statusManager = getStatusManager();
        for (StatusListener statusListener : statusManager.getCopyOfStatusListenerList()) {
            statusManager.remove(statusListener);
        }
    }

    public TurboFilterList getTurboFilterList() {
        return this.turboFilterList;
    }

    public void addTurboFilter(TurboFilter turboFilter) {
        this.turboFilterList.add(turboFilter);
    }

    public void resetTurboFilterList() {
        Iterator<TurboFilter> it = this.turboFilterList.iterator();
        while (it.hasNext()) {
            it.next().stop();
        }
        this.turboFilterList.clear();
    }

    public final FilterReply getTurboFilterChainDecision_0_3OrMore(Marker marker, Logger logger, Level level, String str, Object[] objArr, Throwable th) {
        if (this.turboFilterList.size() == 0) {
            return FilterReply.NEUTRAL;
        }
        return this.turboFilterList.getTurboFilterChainDecision(marker, logger, level, str, objArr, th);
    }

    public final FilterReply getTurboFilterChainDecision_1(Marker marker, Logger logger, Level level, String str, Object obj, Throwable th) {
        return this.turboFilterList.size() == 0 ? FilterReply.NEUTRAL : this.turboFilterList.getTurboFilterChainDecision(marker, logger, level, str, new Object[]{obj}, th);
    }

    public final FilterReply getTurboFilterChainDecision_2(Marker marker, Logger logger, Level level, String str, Object obj, Object obj2, Throwable th) {
        return this.turboFilterList.size() == 0 ? FilterReply.NEUTRAL : this.turboFilterList.getTurboFilterChainDecision(marker, logger, level, str, new Object[]{obj, obj2}, th);
    }

    public void addListener(LoggerContextListener loggerContextListener) {
        this.loggerContextListenerList.add(loggerContextListener);
    }

    public void removeListener(LoggerContextListener loggerContextListener) {
        this.loggerContextListenerList.remove(loggerContextListener);
    }

    private void resetListenersExceptResetResistant() {
        ArrayList arrayList = new ArrayList();
        for (LoggerContextListener loggerContextListener : this.loggerContextListenerList) {
            if (loggerContextListener.isResetResistant()) {
                arrayList.add(loggerContextListener);
            }
        }
        this.loggerContextListenerList.retainAll(arrayList);
    }

    private void resetAllListeners() {
        this.loggerContextListenerList.clear();
    }

    public List<LoggerContextListener> getCopyOfListenerList() {
        return new ArrayList(this.loggerContextListenerList);
    }

    public void fireOnLevelChange(Logger logger, Level level) {
        for (LoggerContextListener loggerContextListener : this.loggerContextListenerList) {
            loggerContextListener.onLevelChange(logger, level);
        }
    }

    private void fireOnReset() {
        for (LoggerContextListener loggerContextListener : this.loggerContextListenerList) {
            loggerContextListener.onReset(this);
        }
    }

    private void fireOnStart() {
        for (LoggerContextListener loggerContextListener : this.loggerContextListenerList) {
            loggerContextListener.onStart(this);
        }
    }

    private void fireOnStop() {
        for (LoggerContextListener loggerContextListener : this.loggerContextListenerList) {
            loggerContextListener.onStop(this);
        }
    }

    @Override // ch.qos.logback.core.ContextBase, ch.qos.logback.core.spi.LifeCycle
    public void start() {
        super.start();
        fireOnStart();
    }

    @Override // ch.qos.logback.core.ContextBase, ch.qos.logback.core.spi.LifeCycle
    public void stop() {
        reset();
        fireOnStop();
        resetAllListeners();
        super.stop();
    }

    @Override // ch.qos.logback.core.ContextBase
    public String toString() {
        return getClass().getName() + "[" + getName() + "]";
    }

    public int getMaxCallerDataDepth() {
        return this.maxCallerDataDepth;
    }

    public void setMaxCallerDataDepth(int i) {
        this.maxCallerDataDepth = i;
    }

    public List<String> getFrameworkPackages() {
        return this.frameworkPackages;
    }
}
