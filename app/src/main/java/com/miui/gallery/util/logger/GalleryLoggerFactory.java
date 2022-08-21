package com.miui.gallery.util.logger;

import android.util.Log;
import androidx.tracing.Trace;
import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.ClassicConstants;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.hook.DefaultShutdownHook;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.SizeAndTimeBasedRollingPolicy;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.status.InfoStatus;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.util.FileSize;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.xiaomi.stat.d;
import dagger.hilt.android.EntryPointAccessors;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.LoggerFactoryHijacker;
import org.slf4j.Marker;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.helpers.SubstituteLogger;
import splitties.init.AppCtxKt;

/* loaded from: classes2.dex */
public class GalleryLoggerFactory {
    public final CountDownLatch mConfigureSignal;
    public volatile boolean mIsConfigureFinished;
    public volatile boolean mIsConfigureStarted;
    public volatile boolean mIsConfigureSuccessful;
    public static final LoadingCache<Logger, RedirectLogger> sLoggerCache = CacheBuilder.newBuilder().build(new CacheLoader<Logger, RedirectLogger>() { // from class: com.miui.gallery.util.logger.GalleryLoggerFactory.1
        @Override // com.google.common.cache.CacheLoader
        public RedirectLogger load(Logger logger) {
            return new RedirectLogger(logger);
        }
    });
    public static final LoggerConfigurator sConfigurator = ((LoggerEntryPoint) EntryPointAccessors.fromApplication(AppCtxKt.getAppCtx(), LoggerEntryPoint.class)).configurator();

    public GalleryLoggerFactory() {
        this.mConfigureSignal = new CountDownLatch(1);
        configure();
    }

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final GalleryLoggerFactory INSTANCE = new GalleryLoggerFactory();
    }

    public static GalleryLoggerFactory instance() {
        return SingletonHolder.INSTANCE;
    }

    public static Logger getSyncLogger(String str) {
        if (instance().initialized()) {
            return LoggerFactory.getLogger("SyncLogger." + str);
        }
        ILoggerFactory loggerFactory = LoggerFactoryHijacker.getLoggerFactory();
        Logger mo166getLogger = loggerFactory.mo166getLogger("SyncLogger." + str);
        if (!(mo166getLogger instanceof SubstituteLogger)) {
            return mo166getLogger;
        }
        Log.w("GalleryLoggerFactory", "getSyncLogger failed, SubstituteLogger was returned for " + str);
        return sLoggerCache.getUnchecked(mo166getLogger);
    }

    public static Logger getDefaultLogger(String str) {
        if (instance().initialized()) {
            return LoggerFactory.getLogger("DefaultLogger." + str);
        }
        ILoggerFactory loggerFactory = LoggerFactoryHijacker.getLoggerFactory();
        Logger mo166getLogger = loggerFactory.mo166getLogger("DefaultLogger." + str);
        if (!(mo166getLogger instanceof SubstituteLogger)) {
            return mo166getLogger;
        }
        Log.w("GalleryLoggerFactory", "getSyncLogger failed, SubstituteLogger was returned for " + str);
        return sLoggerCache.getUnchecked(mo166getLogger);
    }

    public static void updateLogLevel() {
        Level determineLogLevel = sConfigurator.determineLogLevel();
        ((LoggerContext) LoggerFactory.getILoggerFactory()).mo166getLogger(Logger.ROOT_LOGGER_NAME).setLevel(determineLogLevel);
        LogcatFilter.setLevel(determineLogLevel);
    }

    public final synchronized void configure() {
        if (!this.mIsConfigureFinished && !this.mIsConfigureStarted) {
            Log.d("GalleryLoggerFactory", "Starting configure");
            this.mIsConfigureStarted = true;
            new Thread(new ConfigurationTask(), "LoggerInit").start();
        }
    }

    public final boolean initialized() {
        if (this.mIsConfigureFinished) {
            return this.mIsConfigureSuccessful;
        }
        return LoggerFactoryHijacker.isInitializeStarted();
    }

    public static void configureLogger(LoggerContext loggerContext) {
        StatusManager statusManager = loggerContext.getStatusManager();
        if (statusManager != null) {
            statusManager.add(GalleryLoggerFactory$$ExternalSyntheticLambda0.INSTANCE);
            statusManager.add(new InfoStatus("Setting up root logger.", loggerContext));
        }
        LoggerConfigurator loggerConfigurator = sConfigurator;
        String fileLogPath = loggerConfigurator.getFileLogPath();
        Level determineLogLevel = loggerConfigurator.determineLogLevel();
        LogcatFilter.setLevel(determineLogLevel);
        FileLoggingFilter fileLoggingFilter = new FileLoggingFilter();
        fileLoggingFilter.start();
        loggerContext.addTurboFilter(fileLoggingFilter);
        LayoutWrappingEncoder<ILoggingEvent> layoutWrappingEncoder = new LayoutWrappingEncoder<>();
        layoutWrappingEncoder.setContext(loggerContext);
        LogcatLayout logcatLayout = new LogcatLayout();
        logcatLayout.setContext(loggerContext);
        logcatLayout.start();
        layoutWrappingEncoder.setLayout(logcatLayout);
        layoutWrappingEncoder.start();
        LayoutWrappingEncoder<ILoggingEvent> layoutWrappingEncoder2 = new LayoutWrappingEncoder<>();
        layoutWrappingEncoder2.setContext(loggerContext);
        LogcatTagLayout logcatTagLayout = new LogcatTagLayout("MiuiGallery2");
        logcatTagLayout.setContext(loggerContext);
        logcatTagLayout.start();
        layoutWrappingEncoder2.setLayout(logcatTagLayout);
        layoutWrappingEncoder2.start();
        LogcatAppender logcatAppender = new LogcatAppender();
        logcatAppender.setContext(loggerContext);
        logcatAppender.setName("LOGCAT");
        logcatAppender.setEncoder(layoutWrappingEncoder);
        logcatAppender.setTagEncoder(layoutWrappingEncoder2);
        logcatAppender.addFilter(new LogcatFilter());
        logcatAppender.start();
        ch.qos.logback.classic.Logger mo166getLogger = loggerContext.mo166getLogger(Logger.ROOT_LOGGER_NAME);
        mo166getLogger.setLevel(determineLogLevel);
        mo166getLogger.addAppender(logcatAppender);
        LayoutWrappingEncoder layoutWrappingEncoder3 = new LayoutWrappingEncoder();
        layoutWrappingEncoder3.setContext(loggerContext);
        TTLLLayout tTLLLayout = new TTLLLayout();
        tTLLLayout.setContext(loggerContext);
        tTLLLayout.start();
        layoutWrappingEncoder3.setLayout(tTLLLayout);
        RollingFileAppender rollingFileAppender = new RollingFileAppender();
        rollingFileAppender.setContext(loggerContext);
        rollingFileAppender.setName("DEFAULT_FILE");
        rollingFileAppender.setLazy(true);
        StringBuilder sb = new StringBuilder();
        sb.append(fileLogPath);
        String str = File.separator;
        sb.append(str);
        sb.append("gallery.log");
        rollingFileAppender.setFile(sb.toString());
        rollingFileAppender.setEncoder(layoutWrappingEncoder3);
        rollingFileAppender.addFilter(new Filter<ILoggingEvent>() { // from class: com.miui.gallery.util.logger.GalleryLoggerFactory.2
            @Override // ch.qos.logback.core.filter.Filter
            public FilterReply decide(ILoggingEvent iLoggingEvent) {
                Marker marker = iLoggingEvent.getMarker();
                if (marker != null && (marker.contains(Markers.getFileMarker()) || marker.contains(Markers.getFileOnlyMarker()))) {
                    return FilterReply.ACCEPT;
                }
                int i = iLoggingEvent.getLevel().toInt();
                if (i == 30000 || i == 40000) {
                    return FilterReply.ACCEPT;
                }
                return FilterReply.DENY;
            }
        });
        SizeAndTimeBasedRollingPolicy sizeAndTimeBasedRollingPolicy = new SizeAndTimeBasedRollingPolicy();
        sizeAndTimeBasedRollingPolicy.setContext(loggerContext);
        sizeAndTimeBasedRollingPolicy.setMaxHistory(14);
        sizeAndTimeBasedRollingPolicy.setMaxFileSize(new FileSize(15728640L));
        sizeAndTimeBasedRollingPolicy.setTotalSizeCap(FileSize.valueOf("35 mb"));
        sizeAndTimeBasedRollingPolicy.setCleanHistoryOnStart(true);
        sizeAndTimeBasedRollingPolicy.setFileNamePattern(fileLogPath + str + "gallery-%d{yyyy-MM-dd}.%i.gz");
        sizeAndTimeBasedRollingPolicy.setParent(rollingFileAppender);
        rollingFileAppender.setRollingPolicy(sizeAndTimeBasedRollingPolicy);
        sizeAndTimeBasedRollingPolicy.start();
        rollingFileAppender.start();
        AsyncAppender asyncAppender = new AsyncAppender();
        asyncAppender.setContext(loggerContext);
        asyncAppender.setName("ASYNC_DEFAULT_FILE");
        asyncAppender.setQueueSize(256);
        asyncAppender.setDiscardingThreshold(32);
        asyncAppender.setNeverBlock(true);
        asyncAppender.addAppender(rollingFileAppender);
        asyncAppender.start();
        loggerContext.mo166getLogger("DefaultLogger").addAppender(asyncAppender);
        RollingFileAppender rollingFileAppender2 = new RollingFileAppender();
        rollingFileAppender2.setContext(loggerContext);
        rollingFileAppender2.setName("SYNC_FILE");
        rollingFileAppender2.setLazy(true);
        rollingFileAppender2.setFile(fileLogPath + str + "sync.log");
        rollingFileAppender2.setEncoder(layoutWrappingEncoder3);
        rollingFileAppender2.addFilter(new Filter<ILoggingEvent>() { // from class: com.miui.gallery.util.logger.GalleryLoggerFactory.3
            @Override // ch.qos.logback.core.filter.Filter
            public FilterReply decide(ILoggingEvent iLoggingEvent) {
                Marker marker = iLoggingEvent.getMarker();
                if (marker != null && marker.contains(Markers.getFileMarker())) {
                    return FilterReply.ACCEPT;
                }
                int i = iLoggingEvent.getLevel().toInt();
                if (i == 30000 || i == 40000) {
                    return FilterReply.ACCEPT;
                }
                return FilterReply.DENY;
            }
        });
        SizeAndTimeBasedRollingPolicy sizeAndTimeBasedRollingPolicy2 = new SizeAndTimeBasedRollingPolicy();
        sizeAndTimeBasedRollingPolicy2.setContext(loggerContext);
        sizeAndTimeBasedRollingPolicy2.setMaxHistory(21);
        sizeAndTimeBasedRollingPolicy2.setMaxFileSize(new FileSize(15728640L));
        sizeAndTimeBasedRollingPolicy2.setTotalSizeCap(FileSize.valueOf("50 mb"));
        sizeAndTimeBasedRollingPolicy2.setCleanHistoryOnStart(true);
        sizeAndTimeBasedRollingPolicy2.setFileNamePattern(fileLogPath + str + "sync-%d{yyyy-MM-dd}.%i.gz");
        sizeAndTimeBasedRollingPolicy2.setParent(rollingFileAppender2);
        rollingFileAppender2.setRollingPolicy(sizeAndTimeBasedRollingPolicy2);
        sizeAndTimeBasedRollingPolicy2.start();
        rollingFileAppender2.start();
        AsyncAppender asyncAppender2 = new AsyncAppender();
        asyncAppender2.setContext(loggerContext);
        asyncAppender2.setName("ASYNC_SYNC_FILE");
        asyncAppender2.setQueueSize(128);
        asyncAppender2.setDiscardingThreshold(16);
        asyncAppender2.setNeverBlock(true);
        asyncAppender2.addAppender(rollingFileAppender2);
        asyncAppender2.start();
        loggerContext.mo166getLogger("SyncLogger").addAppender(asyncAppender2);
    }

    public static /* synthetic */ void lambda$configureLogger$0(Status status) {
        int level = status.getLevel();
        if (level == 1) {
            Log.w("logback", status.getMessage(), status.getThrowable());
        } else if (level == 2) {
            Log.e("logback", status.getMessage(), status.getThrowable());
        } else {
            Log.d("logback", status.getMessage(), status.getThrowable());
        }
    }

    public void doInit(LoggerContext loggerContext) {
        long currentTimeMillis = System.currentTimeMillis();
        configureLogger(loggerContext);
        if (((Thread) loggerContext.getObject(CoreConstants.SHUTDOWN_HOOK_THREAD)) == null) {
            DefaultShutdownHook defaultShutdownHook = new DefaultShutdownHook();
            Thread thread = new Thread(defaultShutdownHook, "Logback shutdown hook [" + loggerContext.getName() + "]");
            loggerContext.putObject(CoreConstants.SHUTDOWN_HOOK_THREAD, thread);
            Runtime.getRuntime().addShutdownHook(thread);
        }
        Log.d("GalleryLoggerFactory", "doInit costs: " + (System.currentTimeMillis() - currentTimeMillis));
    }

    /* loaded from: classes2.dex */
    public class ConfigurationTask implements Runnable {
        public ConfigurationTask() {
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                try {
                    Trace.beginSection("configureLoggers");
                    long currentTimeMillis = System.currentTimeMillis();
                    System.setProperty(ClassicConstants.LOGBACK_CONTEXT_SELECTOR, "com.miui.gallery.util.logger.ContextSelectorHijacker");
                    LoggerFactory.getILoggerFactory();
                    GalleryLoggerFactory.this.mIsConfigureSuccessful = true;
                    Log.d("GalleryLoggerFactory", "Configure loggers costs " + (System.currentTimeMillis() - currentTimeMillis) + d.H);
                    GalleryLoggerFactory.sLoggerCache.invalidateAll();
                    System.setProperty(ClassicConstants.LOGBACK_CONTEXT_SELECTOR, "");
                } catch (Exception e) {
                    GalleryLoggerFactory.this.mIsConfigureSuccessful = false;
                    Log.e("GalleryLoggerFactory", "Configure failed, what should not happen.", e);
                }
            } finally {
                GalleryLoggerFactory.this.mIsConfigureFinished = true;
                GalleryLoggerFactory.this.mConfigureSignal.countDown();
                Trace.endSection();
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class RedirectLogger implements Logger {
        public static final LoggerConfigurator sConfigurator;
        public static int sLogLevel;
        public final Logger mSubstituteLogger;
        public final String mTag;

        static {
            LoggerConfigurator configurator = ((LoggerEntryPoint) EntryPointAccessors.fromApplication(AppCtxKt.getAppCtx(), LoggerEntryPoint.class)).configurator();
            sConfigurator = configurator;
            int i = configurator.determineLogLevel().levelInt;
            if (i == Integer.MIN_VALUE || i == 5000) {
                sLogLevel = 2;
            } else if (i == 10000) {
                sLogLevel = 3;
            } else if (i == 20000) {
                sLogLevel = 4;
            } else if (i == 30000) {
                sLogLevel = 5;
            } else if (i == 40000) {
                sLogLevel = 6;
            } else {
                sLogLevel = 7;
            }
        }

        public RedirectLogger(Logger logger) {
            String name = logger.getName();
            int indexOf = name != null ? name.indexOf(46) : -1;
            if (name != null && indexOf > 0) {
                this.mTag = "MiuiGallery2_" + name.substring(indexOf + 1);
            } else {
                this.mTag = "MiuiGallery2_" + name;
            }
            this.mSubstituteLogger = logger;
        }

        public static boolean needLog(int i) {
            return i >= sLogLevel || sConfigurator.isPrintLog();
        }

        public static Marker wrapReplayMarker(Marker marker) {
            Marker replayMarker = Markers.getReplayMarker();
            if (marker == null) {
                return replayMarker;
            }
            if (marker.contains(replayMarker)) {
                return marker;
            }
            marker.add(replayMarker);
            return marker;
        }

        @Override // org.slf4j.Logger
        public String getName() {
            return this.mSubstituteLogger.getName();
        }

        @Override // org.slf4j.Logger
        public boolean isTraceEnabled() {
            return needLog(2);
        }

        @Override // org.slf4j.Logger
        public boolean isTraceEnabled(Marker marker) {
            return isTraceEnabled();
        }

        @Override // org.slf4j.Logger
        public void trace(String str) {
            trace(Markers.getReplayMarker(), str);
        }

        @Override // org.slf4j.Logger
        public void trace(String str, Object obj) {
            trace(Markers.getReplayMarker(), str, obj);
        }

        @Override // org.slf4j.Logger
        public void trace(String str, Object obj, Object obj2) {
            trace(Markers.getReplayMarker(), str, obj, obj2);
        }

        @Override // org.slf4j.Logger
        public void trace(String str, Object... objArr) {
            trace(Markers.getReplayMarker(), str, objArr);
        }

        @Override // org.slf4j.Logger
        public void trace(String str, Throwable th) {
            trace(Markers.getReplayMarker(), str, th);
        }

        @Override // org.slf4j.Logger
        public void trace(Marker marker, String str) {
            if (isTraceEnabled(marker)) {
                Log.v(this.mTag, str);
            }
            this.mSubstituteLogger.trace(wrapReplayMarker(marker), str);
        }

        @Override // org.slf4j.Logger
        public void trace(Marker marker, String str, Object obj) {
            if (isTraceEnabled(marker)) {
                Log.v(this.mTag, MessageFormatter.format(str, obj).getMessage());
            }
            this.mSubstituteLogger.trace(wrapReplayMarker(marker), str, obj);
        }

        @Override // org.slf4j.Logger
        public void trace(Marker marker, String str, Object obj, Object obj2) {
            if (isTraceEnabled(marker)) {
                Log.v(this.mTag, MessageFormatter.format(str, obj, obj2).getMessage());
            }
            this.mSubstituteLogger.trace(wrapReplayMarker(marker), str, obj, obj2);
        }

        @Override // org.slf4j.Logger
        public void trace(Marker marker, String str, Object... objArr) {
            if (isTraceEnabled(marker)) {
                Log.v(this.mTag, MessageFormatter.format(str, objArr).getMessage());
            }
            this.mSubstituteLogger.trace(wrapReplayMarker(marker), str, objArr);
        }

        @Override // org.slf4j.Logger
        public void trace(Marker marker, String str, Throwable th) {
            if (isTraceEnabled(marker)) {
                Log.v(this.mTag, str, th);
            }
            this.mSubstituteLogger.trace(wrapReplayMarker(marker), str, th);
        }

        @Override // org.slf4j.Logger
        public boolean isDebugEnabled() {
            return needLog(3);
        }

        @Override // org.slf4j.Logger
        public boolean isDebugEnabled(Marker marker) {
            return isDebugEnabled();
        }

        @Override // org.slf4j.Logger
        public void debug(String str) {
            debug(Markers.getReplayMarker(), str);
        }

        @Override // org.slf4j.Logger
        public void debug(String str, Object obj) {
            debug(Markers.getReplayMarker(), str, obj);
        }

        @Override // org.slf4j.Logger
        public void debug(String str, Object obj, Object obj2) {
            debug(Markers.getReplayMarker(), str, obj, obj2);
        }

        @Override // org.slf4j.Logger
        public void debug(String str, Object... objArr) {
            debug(Markers.getReplayMarker(), str, objArr);
        }

        @Override // org.slf4j.Logger
        public void debug(String str, Throwable th) {
            debug(Markers.getReplayMarker(), str, th);
        }

        @Override // org.slf4j.Logger
        public void debug(Marker marker, String str) {
            if (isDebugEnabled(marker)) {
                Log.d(this.mTag, str);
            }
            this.mSubstituteLogger.debug(wrapReplayMarker(marker), str);
        }

        @Override // org.slf4j.Logger
        public void debug(Marker marker, String str, Object obj) {
            if (isDebugEnabled(marker)) {
                Log.d(this.mTag, MessageFormatter.format(str, obj).getMessage());
            }
            this.mSubstituteLogger.debug(wrapReplayMarker(marker), str, obj);
        }

        @Override // org.slf4j.Logger
        public void debug(Marker marker, String str, Object obj, Object obj2) {
            if (isDebugEnabled(marker)) {
                Log.d(this.mTag, MessageFormatter.format(str, obj, obj2).getMessage());
            }
            this.mSubstituteLogger.debug(wrapReplayMarker(marker), str, obj, obj2);
        }

        @Override // org.slf4j.Logger
        public void debug(Marker marker, String str, Object... objArr) {
            if (isDebugEnabled(marker)) {
                Log.d(this.mTag, MessageFormatter.format(str, objArr).getMessage());
            }
            this.mSubstituteLogger.debug(wrapReplayMarker(marker), str, objArr);
        }

        @Override // org.slf4j.Logger
        public void debug(Marker marker, String str, Throwable th) {
            if (isDebugEnabled(marker)) {
                Log.d(this.mTag, str, th);
            }
            this.mSubstituteLogger.debug(wrapReplayMarker(marker), str, th);
        }

        @Override // org.slf4j.Logger
        public boolean isInfoEnabled() {
            return needLog(4);
        }

        @Override // org.slf4j.Logger
        public boolean isInfoEnabled(Marker marker) {
            return isInfoEnabled();
        }

        @Override // org.slf4j.Logger
        public void info(String str) {
            info(Markers.getReplayMarker(), str);
        }

        @Override // org.slf4j.Logger
        public void info(String str, Object obj) {
            info(Markers.getReplayMarker(), str, obj);
        }

        @Override // org.slf4j.Logger
        public void info(String str, Object obj, Object obj2) {
            info(Markers.getReplayMarker(), str, obj, obj2);
        }

        @Override // org.slf4j.Logger
        public void info(String str, Object... objArr) {
            info(Markers.getReplayMarker(), str, objArr);
        }

        @Override // org.slf4j.Logger
        public void info(String str, Throwable th) {
            info(Markers.getReplayMarker(), str, th);
        }

        @Override // org.slf4j.Logger
        public void info(Marker marker, String str) {
            if (isInfoEnabled(marker)) {
                Log.i(this.mTag, str);
            }
            this.mSubstituteLogger.info(wrapReplayMarker(marker), str);
        }

        @Override // org.slf4j.Logger
        public void info(Marker marker, String str, Object obj) {
            if (isInfoEnabled(marker)) {
                Log.i(this.mTag, MessageFormatter.format(str, obj).getMessage());
            }
            this.mSubstituteLogger.info(wrapReplayMarker(marker), str, obj);
        }

        @Override // org.slf4j.Logger
        public void info(Marker marker, String str, Object obj, Object obj2) {
            if (isInfoEnabled(marker)) {
                Log.i(this.mTag, MessageFormatter.format(str, obj, obj2).getMessage());
            }
            this.mSubstituteLogger.info(wrapReplayMarker(marker), str, obj, obj2);
        }

        @Override // org.slf4j.Logger
        public void info(Marker marker, String str, Object... objArr) {
            if (isInfoEnabled(marker)) {
                Log.i(this.mTag, MessageFormatter.format(str, objArr).getMessage());
            }
            this.mSubstituteLogger.info(wrapReplayMarker(marker), str, objArr);
        }

        @Override // org.slf4j.Logger
        public void info(Marker marker, String str, Throwable th) {
            if (isInfoEnabled(marker)) {
                Log.i(this.mTag, str, th);
            }
            this.mSubstituteLogger.info(wrapReplayMarker(marker), str, th);
        }

        @Override // org.slf4j.Logger
        public boolean isWarnEnabled() {
            return needLog(5);
        }

        @Override // org.slf4j.Logger
        public boolean isWarnEnabled(Marker marker) {
            return isWarnEnabled();
        }

        @Override // org.slf4j.Logger
        public void warn(String str) {
            warn(Markers.getReplayMarker(), str);
        }

        @Override // org.slf4j.Logger
        public void warn(String str, Object obj) {
            warn(Markers.getReplayMarker(), str, obj);
        }

        @Override // org.slf4j.Logger
        public void warn(String str, Object obj, Object obj2) {
            warn(Markers.getReplayMarker(), str, obj, obj2);
        }

        @Override // org.slf4j.Logger
        public void warn(String str, Object... objArr) {
            warn(Markers.getReplayMarker(), str, objArr);
        }

        @Override // org.slf4j.Logger
        public void warn(String str, Throwable th) {
            warn(Markers.getReplayMarker(), str, th);
        }

        @Override // org.slf4j.Logger
        public void warn(Marker marker, String str) {
            if (isWarnEnabled(marker)) {
                Log.w(this.mTag, str);
            }
            this.mSubstituteLogger.warn(wrapReplayMarker(marker), str);
        }

        @Override // org.slf4j.Logger
        public void warn(Marker marker, String str, Object obj) {
            if (isWarnEnabled(marker)) {
                Log.w(this.mTag, MessageFormatter.format(str, obj).getMessage());
            }
            this.mSubstituteLogger.warn(wrapReplayMarker(marker), str, obj);
        }

        @Override // org.slf4j.Logger
        public void warn(Marker marker, String str, Object obj, Object obj2) {
            if (isWarnEnabled(marker)) {
                Log.w(this.mTag, MessageFormatter.format(str, obj, obj2).getMessage());
            }
            this.mSubstituteLogger.warn(wrapReplayMarker(marker), str, obj, obj2);
        }

        @Override // org.slf4j.Logger
        public void warn(Marker marker, String str, Object... objArr) {
            if (isWarnEnabled(marker)) {
                Log.w(this.mTag, MessageFormatter.format(str, objArr).getMessage());
            }
            this.mSubstituteLogger.warn(wrapReplayMarker(marker), str, objArr);
        }

        @Override // org.slf4j.Logger
        public void warn(Marker marker, String str, Throwable th) {
            if (isWarnEnabled(marker)) {
                Log.w(this.mTag, str, th);
            }
            this.mSubstituteLogger.warn(wrapReplayMarker(marker), str, th);
        }

        @Override // org.slf4j.Logger
        public boolean isErrorEnabled() {
            return needLog(6);
        }

        @Override // org.slf4j.Logger
        public boolean isErrorEnabled(Marker marker) {
            return isErrorEnabled();
        }

        @Override // org.slf4j.Logger
        public void error(String str) {
            error(Markers.getReplayMarker(), str);
        }

        @Override // org.slf4j.Logger
        public void error(String str, Object obj) {
            error(Markers.getReplayMarker(), str, obj);
        }

        @Override // org.slf4j.Logger
        public void error(String str, Object obj, Object obj2) {
            error(Markers.getReplayMarker(), str, obj, obj2);
        }

        @Override // org.slf4j.Logger
        public void error(String str, Object... objArr) {
            error(Markers.getReplayMarker(), str, objArr);
        }

        @Override // org.slf4j.Logger
        public void error(String str, Throwable th) {
            error(Markers.getReplayMarker(), str, th);
        }

        @Override // org.slf4j.Logger
        public void error(Marker marker, String str) {
            if (isErrorEnabled(marker)) {
                Log.e(this.mTag, str);
            }
            this.mSubstituteLogger.error(wrapReplayMarker(marker), str);
        }

        @Override // org.slf4j.Logger
        public void error(Marker marker, String str, Object obj) {
            if (isErrorEnabled(marker)) {
                Log.e(this.mTag, MessageFormatter.format(str, obj).getMessage());
            }
            this.mSubstituteLogger.error(wrapReplayMarker(marker), str, obj);
        }

        @Override // org.slf4j.Logger
        public void error(Marker marker, String str, Object obj, Object obj2) {
            if (isErrorEnabled(marker)) {
                Log.e(this.mTag, MessageFormatter.format(str, obj, obj2).getMessage());
            }
            this.mSubstituteLogger.error(wrapReplayMarker(marker), str, obj, obj2);
        }

        @Override // org.slf4j.Logger
        public void error(Marker marker, String str, Object... objArr) {
            if (isErrorEnabled(marker)) {
                Log.e(this.mTag, MessageFormatter.format(str, objArr).getMessage());
            }
            this.mSubstituteLogger.error(wrapReplayMarker(marker), str, objArr);
        }

        @Override // org.slf4j.Logger
        public void error(Marker marker, String str, Throwable th) {
            if (isErrorEnabled(marker)) {
                Log.e(this.mTag, str, th);
            }
            this.mSubstituteLogger.error(wrapReplayMarker(marker), str, th);
        }
    }
}
