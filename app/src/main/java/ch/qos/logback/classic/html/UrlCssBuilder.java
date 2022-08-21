package ch.qos.logback.classic.html;

import ch.qos.logback.core.html.CssBuilder;

/* loaded from: classes.dex */
public class UrlCssBuilder implements CssBuilder {
    public String url = "http://logback.qos.ch/css/classic.css";

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String str) {
        this.url = str;
    }

    @Override // ch.qos.logback.core.html.CssBuilder
    public void addCss(StringBuilder sb) {
        sb.append("<link REL=StyleSheet HREF=\"");
        sb.append(this.url);
        sb.append("\" TITLE=\"Basic\" />");
    }
}
