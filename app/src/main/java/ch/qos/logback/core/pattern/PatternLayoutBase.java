package ch.qos.logback.core.pattern;

import ch.qos.logback.core.Context;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;
import ch.qos.logback.core.pattern.parser.Parser;
import ch.qos.logback.core.spi.ScanException;
import ch.qos.logback.core.status.ErrorStatus;
import ch.qos.logback.core.status.StatusManager;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public abstract class PatternLayoutBase<E> extends LayoutBase<E> {
    public static final int INTIAL_STRING_BUILDER_SIZE = 256;
    public Converter<E> head;
    public Map<String, String> instanceConverterMap = new HashMap();
    public boolean outputPatternAsHeader = false;
    public String pattern;
    public PostCompileProcessor<E> postCompileProcessor;

    public abstract Map<String, String> getDefaultConverterMap();

    public String getPresentationHeaderPrefix() {
        return "";
    }

    public Map<String, String> getEffectiveConverterMap() {
        Map map;
        HashMap hashMap = new HashMap();
        Map<String, String> defaultConverterMap = getDefaultConverterMap();
        if (defaultConverterMap != null) {
            hashMap.putAll(defaultConverterMap);
        }
        Context context = getContext();
        if (context != null && (map = (Map) context.getObject(CoreConstants.PATTERN_RULE_REGISTRY)) != null) {
            hashMap.putAll(map);
        }
        hashMap.putAll(this.instanceConverterMap);
        return hashMap;
    }

    @Override // ch.qos.logback.core.LayoutBase, ch.qos.logback.core.spi.LifeCycle
    public void start() {
        String str = this.pattern;
        if (str == null || str.length() == 0) {
            addError("Empty or null pattern.");
            return;
        }
        try {
            Parser parser = new Parser(this.pattern);
            if (getContext() != null) {
                parser.setContext(getContext());
            }
            Converter<E> compile = parser.compile(parser.parse(), getEffectiveConverterMap());
            this.head = compile;
            PostCompileProcessor<E> postCompileProcessor = this.postCompileProcessor;
            if (postCompileProcessor != null) {
                postCompileProcessor.process(this.context, compile);
            }
            ConverterUtil.setContextForConverters(getContext(), this.head);
            ConverterUtil.startConverters(this.head);
            super.start();
        } catch (ScanException e) {
            StatusManager statusManager = getContext().getStatusManager();
            statusManager.add(new ErrorStatus("Failed to parse pattern \"" + getPattern() + "\".", this, e));
        }
    }

    public void setPostCompileProcessor(PostCompileProcessor<E> postCompileProcessor) {
        this.postCompileProcessor = postCompileProcessor;
    }

    public void setContextForConverters(Converter<E> converter) {
        ConverterUtil.setContextForConverters(getContext(), converter);
    }

    public String writeLoopOnConverters(E e) {
        StringBuilder sb = new StringBuilder(256);
        for (Converter<E> converter = this.head; converter != null; converter = converter.getNext()) {
            converter.write(sb, e);
        }
        return sb.toString();
    }

    public String getPattern() {
        return this.pattern;
    }

    public void setPattern(String str) {
        this.pattern = str;
    }

    public String toString() {
        return getClass().getName() + "(\"" + getPattern() + "\")";
    }

    public Map<String, String> getInstanceConverterMap() {
        return this.instanceConverterMap;
    }

    public boolean isOutputPatternAsHeader() {
        return this.outputPatternAsHeader;
    }

    public void setOutputPatternAsHeader(boolean z) {
        this.outputPatternAsHeader = z;
    }

    @Override // ch.qos.logback.core.LayoutBase, ch.qos.logback.core.Layout
    public String getPresentationHeader() {
        if (this.outputPatternAsHeader) {
            return getPresentationHeaderPrefix() + this.pattern;
        }
        return super.getPresentationHeader();
    }
}
