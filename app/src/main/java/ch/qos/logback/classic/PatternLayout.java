package ch.qos.logback.classic;

import ch.qos.logback.classic.pattern.CallerDataConverter;
import ch.qos.logback.classic.pattern.ClassOfCallerConverter;
import ch.qos.logback.classic.pattern.ContextNameConverter;
import ch.qos.logback.classic.pattern.DateConverter;
import ch.qos.logback.classic.pattern.EnsureExceptionHandling;
import ch.qos.logback.classic.pattern.ExtendedThrowableProxyConverter;
import ch.qos.logback.classic.pattern.FileOfCallerConverter;
import ch.qos.logback.classic.pattern.LevelConverter;
import ch.qos.logback.classic.pattern.LineOfCallerConverter;
import ch.qos.logback.classic.pattern.LineSeparatorConverter;
import ch.qos.logback.classic.pattern.LocalSequenceNumberConverter;
import ch.qos.logback.classic.pattern.LoggerConverter;
import ch.qos.logback.classic.pattern.MDCConverter;
import ch.qos.logback.classic.pattern.MarkerConverter;
import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.pattern.MethodOfCallerConverter;
import ch.qos.logback.classic.pattern.NopThrowableInformationConverter;
import ch.qos.logback.classic.pattern.PropertyConverter;
import ch.qos.logback.classic.pattern.RelativeTimeConverter;
import ch.qos.logback.classic.pattern.RootCauseFirstThrowableProxyConverter;
import ch.qos.logback.classic.pattern.ThreadConverter;
import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.joran.action.Action;
import ch.qos.logback.core.pattern.PatternLayoutBase;
import ch.qos.logback.core.pattern.parser.Parser;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.d;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class PatternLayout extends PatternLayoutBase<ILoggingEvent> {
    public static final Map<String, String> defaultConverterMap;

    static {
        HashMap hashMap = new HashMap();
        defaultConverterMap = hashMap;
        hashMap.putAll(Parser.DEFAULT_COMPOSITE_CONVERTER_MAP);
        hashMap.put("d", DateConverter.class.getName());
        hashMap.put("date", DateConverter.class.getName());
        hashMap.put("r", RelativeTimeConverter.class.getName());
        hashMap.put("relative", RelativeTimeConverter.class.getName());
        hashMap.put("level", LevelConverter.class.getName());
        hashMap.put("le", LevelConverter.class.getName());
        hashMap.put(d.e, LevelConverter.class.getName());
        hashMap.put("t", ThreadConverter.class.getName());
        hashMap.put("thread", ThreadConverter.class.getName());
        hashMap.put("lo", LoggerConverter.class.getName());
        hashMap.put("logger", LoggerConverter.class.getName());
        hashMap.put("c", LoggerConverter.class.getName());
        hashMap.put(d.V, MessageConverter.class.getName());
        hashMap.put("msg", MessageConverter.class.getName());
        hashMap.put("message", MessageConverter.class.getName());
        hashMap.put("C", ClassOfCallerConverter.class.getName());
        hashMap.put(Action.CLASS_ATTRIBUTE, ClassOfCallerConverter.class.getName());
        hashMap.put("M", MethodOfCallerConverter.class.getName());
        hashMap.put(MiStat.Param.METHOD, MethodOfCallerConverter.class.getName());
        hashMap.put("L", LineOfCallerConverter.class.getName());
        hashMap.put("line", LineOfCallerConverter.class.getName());
        hashMap.put("F", FileOfCallerConverter.class.getName());
        hashMap.put(Action.FILE_ATTRIBUTE, FileOfCallerConverter.class.getName());
        hashMap.put("X", MDCConverter.class.getName());
        hashMap.put("mdc", MDCConverter.class.getName());
        hashMap.put("ex", ThrowableProxyConverter.class.getName());
        hashMap.put("exception", ThrowableProxyConverter.class.getName());
        hashMap.put("rEx", RootCauseFirstThrowableProxyConverter.class.getName());
        hashMap.put("rootException", RootCauseFirstThrowableProxyConverter.class.getName());
        hashMap.put("throwable", ThrowableProxyConverter.class.getName());
        hashMap.put("xEx", ExtendedThrowableProxyConverter.class.getName());
        hashMap.put("xException", ExtendedThrowableProxyConverter.class.getName());
        hashMap.put("xThrowable", ExtendedThrowableProxyConverter.class.getName());
        hashMap.put("nopex", NopThrowableInformationConverter.class.getName());
        hashMap.put("nopexception", NopThrowableInformationConverter.class.getName());
        hashMap.put("cn", ContextNameConverter.class.getName());
        hashMap.put("contextName", ContextNameConverter.class.getName());
        hashMap.put("caller", CallerDataConverter.class.getName());
        hashMap.put("marker", MarkerConverter.class.getName());
        hashMap.put("property", PropertyConverter.class.getName());
        hashMap.put("n", LineSeparatorConverter.class.getName());
        hashMap.put("lsn", LocalSequenceNumberConverter.class.getName());
    }

    public PatternLayout() {
        this.postCompileProcessor = new EnsureExceptionHandling();
    }

    @Override // ch.qos.logback.core.pattern.PatternLayoutBase
    public Map<String, String> getDefaultConverterMap() {
        return defaultConverterMap;
    }

    @Override // ch.qos.logback.core.Layout
    public String doLayout(ILoggingEvent iLoggingEvent) {
        return !isStarted() ? "" : writeLoopOnConverters(iLoggingEvent);
    }
}
