package ch.qos.logback.classic.html;

import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.pattern.MDCConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.helpers.Transform;
import ch.qos.logback.core.html.HTMLLayoutBase;
import ch.qos.logback.core.html.IThrowableRenderer;
import ch.qos.logback.core.pattern.Converter;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes.dex */
public class HTMLLayout extends HTMLLayoutBase<ILoggingEvent> {
    public static final String DEFAULT_CONVERSION_PATTERN = "%date%thread%level%logger%mdc%msg";
    public IThrowableRenderer<ILoggingEvent> throwableRenderer;

    public HTMLLayout() {
        this.pattern = DEFAULT_CONVERSION_PATTERN;
        this.throwableRenderer = new DefaultThrowableRenderer();
        this.cssBuilder = new DefaultCssBuilder();
    }

    @Override // ch.qos.logback.core.html.HTMLLayoutBase, ch.qos.logback.core.LayoutBase, ch.qos.logback.core.spi.LifeCycle
    public void start() {
        boolean z;
        if (this.throwableRenderer == null) {
            addError("ThrowableRender cannot be null.");
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            super.start();
        }
    }

    @Override // ch.qos.logback.core.html.HTMLLayoutBase
    public Map<String, String> getDefaultConverterMap() {
        return PatternLayout.defaultConverterMap;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // ch.qos.logback.core.Layout
    public String doLayout(ILoggingEvent iLoggingEvent) {
        StringBuilder sb = new StringBuilder();
        startNewTableIfLimitReached(sb);
        long j = this.counter;
        this.counter = j + 1;
        boolean z = (j & 1) != 0;
        String lowerCase = iLoggingEvent.getLevel().toString().toLowerCase(Locale.US);
        String str = CoreConstants.LINE_SEPARATOR;
        sb.append(str);
        sb.append("<tr class=\"");
        sb.append(lowerCase);
        if (z) {
            sb.append(" odd\">");
        } else {
            sb.append(" even\">");
        }
        sb.append(str);
        for (Converter converter = this.head; converter != null; converter = converter.getNext()) {
            appendEventToBuffer(sb, converter, iLoggingEvent);
        }
        sb.append("</tr>");
        sb.append(CoreConstants.LINE_SEPARATOR);
        if (iLoggingEvent.getThrowableProxy() != null) {
            this.throwableRenderer.render(sb, iLoggingEvent);
        }
        return sb.toString();
    }

    private void appendEventToBuffer(StringBuilder sb, Converter<ILoggingEvent> converter, ILoggingEvent iLoggingEvent) {
        sb.append("<td class=\"");
        sb.append(computeConverterName(converter));
        sb.append("\">");
        sb.append(Transform.escapeTags(converter.convert(iLoggingEvent)));
        sb.append("</td>");
        sb.append(CoreConstants.LINE_SEPARATOR);
    }

    public IThrowableRenderer<ILoggingEvent> getThrowableRenderer() {
        return this.throwableRenderer;
    }

    public void setThrowableRenderer(IThrowableRenderer<ILoggingEvent> iThrowableRenderer) {
        this.throwableRenderer = iThrowableRenderer;
    }

    @Override // ch.qos.logback.core.html.HTMLLayoutBase
    public String computeConverterName(Converter<ILoggingEvent> converter) {
        if (converter instanceof MDCConverter) {
            String firstOption = ((MDCConverter) converter).getFirstOption();
            return firstOption != null ? firstOption : "MDC";
        }
        return super.computeConverterName(converter);
    }
}
