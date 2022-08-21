package ch.qos.logback.core;

import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.spi.LogbackLock;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.util.ExecutorServiceUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

/* loaded from: classes.dex */
public class ContextBase implements Context, LifeCycle {
    private LifeCycleManager lifeCycleManager;
    private String name;
    private ScheduledExecutorService scheduledExecutorService;
    private boolean started;
    private long birthTime = System.currentTimeMillis();
    private StatusManager sm = new BasicStatusManager();
    public Map<String, String> propertyMap = new HashMap();
    public Map<String, Object> objectMap = new HashMap();
    public LogbackLock configurationLock = new LogbackLock();
    public List<ScheduledFuture<?>> scheduledFutures = new ArrayList(1);

    public ContextBase() {
        initCollisionMaps();
    }

    @Override // ch.qos.logback.core.Context
    public StatusManager getStatusManager() {
        return this.sm;
    }

    public void setStatusManager(StatusManager statusManager) {
        if (statusManager == null) {
            throw new IllegalArgumentException("null StatusManager not allowed");
        }
        this.sm = statusManager;
    }

    @Override // ch.qos.logback.core.Context, ch.qos.logback.core.spi.PropertyContainer
    public Map<String, String> getCopyOfPropertyMap() {
        return new HashMap(this.propertyMap);
    }

    @Override // ch.qos.logback.core.Context
    public void putProperty(String str, String str2) {
        this.propertyMap.put(str, str2);
    }

    public void initCollisionMaps() {
        putObject(CoreConstants.FA_FILENAME_COLLISION_MAP, new HashMap());
        putObject(CoreConstants.RFA_FILENAME_PATTERN_COLLISION_MAP, new HashMap());
    }

    @Override // ch.qos.logback.core.Context, ch.qos.logback.core.spi.PropertyContainer
    public String getProperty(String str) {
        if (CoreConstants.CONTEXT_NAME_KEY.equals(str)) {
            return getName();
        }
        return this.propertyMap.get(str);
    }

    @Override // ch.qos.logback.core.Context
    public Object getObject(String str) {
        return this.objectMap.get(str);
    }

    @Override // ch.qos.logback.core.Context
    public void putObject(String str, Object obj) {
        this.objectMap.put(str, obj);
    }

    public void removeObject(String str) {
        this.objectMap.remove(str);
    }

    @Override // ch.qos.logback.core.Context
    public String getName() {
        return this.name;
    }

    public void start() {
        this.started = true;
    }

    public void stop() {
        stopExecutorService();
        this.started = false;
    }

    @Override // ch.qos.logback.core.spi.LifeCycle
    public boolean isStarted() {
        return this.started;
    }

    public void reset() {
        removeShutdownHook();
        getLifeCycleManager().reset();
        this.propertyMap.clear();
        this.objectMap.clear();
    }

    @Override // ch.qos.logback.core.Context
    public void setName(String str) throws IllegalStateException {
        if (str == null || !str.equals(this.name)) {
            String str2 = this.name;
            if (str2 == null || "default".equals(str2)) {
                this.name = str;
                return;
            }
            throw new IllegalStateException("Context has been already given a name");
        }
    }

    @Override // ch.qos.logback.core.Context
    public long getBirthTime() {
        return this.birthTime;
    }

    @Override // ch.qos.logback.core.Context
    public Object getConfigurationLock() {
        return this.configurationLock;
    }

    @Override // ch.qos.logback.core.Context
    public synchronized ExecutorService getExecutorService() {
        return getScheduledExecutorService();
    }

    @Override // ch.qos.logback.core.Context
    public synchronized ScheduledExecutorService getScheduledExecutorService() {
        if (this.scheduledExecutorService == null) {
            this.scheduledExecutorService = ExecutorServiceUtil.newScheduledExecutorService();
        }
        return this.scheduledExecutorService;
    }

    private synchronized void stopExecutorService() {
        ScheduledExecutorService scheduledExecutorService = this.scheduledExecutorService;
        if (scheduledExecutorService != null) {
            ExecutorServiceUtil.shutdown(scheduledExecutorService);
            this.scheduledExecutorService = null;
        }
    }

    private void removeShutdownHook() {
        Thread thread = (Thread) getObject(CoreConstants.SHUTDOWN_HOOK_THREAD);
        if (thread != null) {
            removeObject(CoreConstants.SHUTDOWN_HOOK_THREAD);
            try {
                Runtime.getRuntime().removeShutdownHook(thread);
            } catch (IllegalStateException unused) {
            }
        }
    }

    @Override // ch.qos.logback.core.Context
    public void register(LifeCycle lifeCycle) {
        getLifeCycleManager().register(lifeCycle);
    }

    public synchronized LifeCycleManager getLifeCycleManager() {
        if (this.lifeCycleManager == null) {
            this.lifeCycleManager = new LifeCycleManager();
        }
        return this.lifeCycleManager;
    }

    public String toString() {
        return this.name;
    }

    @Override // ch.qos.logback.core.Context
    public void addScheduledFuture(ScheduledFuture<?> scheduledFuture) {
        this.scheduledFutures.add(scheduledFuture);
    }

    public List<ScheduledFuture<?>> getScheduledFutures() {
        return new ArrayList(this.scheduledFutures);
    }
}
