package ch.qos.logback.core.html;

import ch.qos.logback.core.Context;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;
import ch.qos.logback.core.pattern.Converter;
import ch.qos.logback.core.pattern.ConverterUtil;
import ch.qos.logback.core.pattern.parser.Parser;
import ch.qos.logback.core.spi.AbstractComponentTracker;
import ch.qos.logback.core.spi.ScanException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public abstract class HTMLLayoutBase<E> extends LayoutBase<E> {
    public CssBuilder cssBuilder;
    public Converter<E> head;
    public String pattern;
    public String title = "Logback Log Messages";
    public long counter = 0;

    @Override // ch.qos.logback.core.LayoutBase, ch.qos.logback.core.Layout
    public String getContentType() {
        return "text/html";
    }

    public abstract Map<String, String> getDefaultConverterMap();

    public void setPattern(String str) {
        this.pattern = str;
    }

    public String getPattern() {
        return this.pattern;
    }

    public CssBuilder getCssBuilder() {
        return this.cssBuilder;
    }

    public void setCssBuilder(CssBuilder cssBuilder) {
        this.cssBuilder = cssBuilder;
    }

    @Override // ch.qos.logback.core.LayoutBase, ch.qos.logback.core.spi.LifeCycle
    public void start() {
        boolean z;
        try {
            Parser parser = new Parser(this.pattern);
            parser.setContext(getContext());
            Converter<E> compile = parser.compile(parser.parse(), getEffectiveConverterMap());
            this.head = compile;
            ConverterUtil.startConverters(compile);
            z = false;
        } catch (ScanException e) {
            addError("Incorrect pattern found", e);
            z = true;
        }
        if (!z) {
            this.started = true;
        }
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
        return hashMap;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getTitle() {
        return this.title;
    }

    @Override // ch.qos.logback.core.LayoutBase, ch.qos.logback.core.Layout
    public String getFileHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\"");
        sb.append(" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        String str = CoreConstants.LINE_SEPARATOR;
        sb.append(str);
        sb.append("<html>");
        sb.append(str);
        sb.append("  <head>");
        sb.append(str);
        sb.append("    <title>");
        sb.append(this.title);
        sb.append("</title>");
        sb.append(str);
        this.cssBuilder.addCss(sb);
        sb.append(str);
        sb.append("  </head>");
        sb.append(str);
        sb.append("<body>");
        sb.append(str);
        return sb.toString();
    }

    @Override // ch.qos.logback.core.LayoutBase, ch.qos.logback.core.Layout
    public String getPresentationHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append("<hr/>");
        String str = CoreConstants.LINE_SEPARATOR;
        sb.append(str);
        sb.append("<p>Log session start time ");
        sb.append(new Date());
        sb.append("</p><p></p>");
        sb.append(str);
        sb.append(str);
        sb.append("<table cellspacing=\"0\">");
        sb.append(str);
        buildHeaderRowForTable(sb);
        return sb.toString();
    }

    private void buildHeaderRowForTable(StringBuilder sb) {
        Converter<E> converter = this.head;
        sb.append("<tr class=\"header\">");
        sb.append(CoreConstants.LINE_SEPARATOR);
        while (converter != null) {
            if (computeConverterName(converter) == null) {
                converter = converter.getNext();
            } else {
                sb.append("<td class=\"");
                sb.append(computeConverterName(converter));
                sb.append("\">");
                sb.append(computeConverterName(converter));
                sb.append("</td>");
                sb.append(CoreConstants.LINE_SEPARATOR);
                converter = converter.getNext();
            }
        }
        sb.append("</tr>");
        sb.append(CoreConstants.LINE_SEPARATOR);
    }

    @Override // ch.qos.logback.core.LayoutBase, ch.qos.logback.core.Layout
    public String getPresentationFooter() {
        return "</table>";
    }

    @Override // ch.qos.logback.core.LayoutBase, ch.qos.logback.core.Layout
    public String getFileFooter() {
        return CoreConstants.LINE_SEPARATOR + "</body></html>";
    }

    public void startNewTableIfLimitReached(StringBuilder sb) {
        if (this.counter >= AbstractComponentTracker.LINGERING_TIMEOUT) {
            this.counter = 0L;
            sb.append("</table>");
            String str = CoreConstants.LINE_SEPARATOR;
            sb.append(str);
            sb.append("<p></p>");
            sb.append("<table cellspacing=\"0\">");
            sb.append(str);
            buildHeaderRowForTable(sb);
        }
    }

    public String computeConverterName(Converter<E> converter) {
        String simpleName = converter.getClass().getSimpleName();
        int indexOf = simpleName.indexOf("Converter");
        return indexOf == -1 ? simpleName : simpleName.substring(0, indexOf);
    }
}
