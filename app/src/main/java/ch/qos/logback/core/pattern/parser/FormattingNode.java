package ch.qos.logback.core.pattern.parser;

import ch.qos.logback.core.pattern.FormatInfo;

/* loaded from: classes.dex */
public class FormattingNode extends Node {
    public FormatInfo formatInfo;

    public FormattingNode(int i) {
        super(i);
    }

    public FormattingNode(int i, Object obj) {
        super(i, obj);
    }

    public FormatInfo getFormatInfo() {
        return this.formatInfo;
    }

    public void setFormatInfo(FormatInfo formatInfo) {
        this.formatInfo = formatInfo;
    }

    @Override // ch.qos.logback.core.pattern.parser.Node
    public boolean equals(Object obj) {
        if (super.equals(obj) && (obj instanceof FormattingNode)) {
            FormattingNode formattingNode = (FormattingNode) obj;
            FormatInfo formatInfo = this.formatInfo;
            return formatInfo != null ? formatInfo.equals(formattingNode.formatInfo) : formattingNode.formatInfo == null;
        }
        return false;
    }

    @Override // ch.qos.logback.core.pattern.parser.Node
    public int hashCode() {
        int hashCode = super.hashCode() * 31;
        FormatInfo formatInfo = this.formatInfo;
        return hashCode + (formatInfo != null ? formatInfo.hashCode() : 0);
    }
}
