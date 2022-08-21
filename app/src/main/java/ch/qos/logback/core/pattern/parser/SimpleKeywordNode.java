package ch.qos.logback.core.pattern.parser;

import java.util.List;

/* loaded from: classes.dex */
public class SimpleKeywordNode extends FormattingNode {
    public List<String> optionList;

    public SimpleKeywordNode(Object obj) {
        super(1, obj);
    }

    public SimpleKeywordNode(int i, Object obj) {
        super(i, obj);
    }

    public List<String> getOptions() {
        return this.optionList;
    }

    public void setOptions(List<String> list) {
        this.optionList = list;
    }

    @Override // ch.qos.logback.core.pattern.parser.FormattingNode, ch.qos.logback.core.pattern.parser.Node
    public boolean equals(Object obj) {
        if (super.equals(obj) && (obj instanceof SimpleKeywordNode)) {
            SimpleKeywordNode simpleKeywordNode = (SimpleKeywordNode) obj;
            List<String> list = this.optionList;
            return list != null ? list.equals(simpleKeywordNode.optionList) : simpleKeywordNode.optionList == null;
        }
        return false;
    }

    @Override // ch.qos.logback.core.pattern.parser.FormattingNode, ch.qos.logback.core.pattern.parser.Node
    public int hashCode() {
        return super.hashCode();
    }

    @Override // ch.qos.logback.core.pattern.parser.Node
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.optionList == null) {
            sb.append("KeyWord(" + this.value + "," + this.formatInfo + ")");
        } else {
            sb.append("KeyWord(" + this.value + ", " + this.formatInfo + "," + this.optionList + ")");
        }
        sb.append(printNext());
        return sb.toString();
    }
}
