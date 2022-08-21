package ch.qos.logback.core.pattern.parser;

/* loaded from: classes.dex */
public class CompositeNode extends SimpleKeywordNode {
    public Node childNode;

    public CompositeNode(String str) {
        super(2, str);
    }

    public Node getChildNode() {
        return this.childNode;
    }

    public void setChildNode(Node node) {
        this.childNode = node;
    }

    @Override // ch.qos.logback.core.pattern.parser.SimpleKeywordNode, ch.qos.logback.core.pattern.parser.FormattingNode, ch.qos.logback.core.pattern.parser.Node
    public boolean equals(Object obj) {
        if (super.equals(obj) && (obj instanceof CompositeNode)) {
            CompositeNode compositeNode = (CompositeNode) obj;
            Node node = this.childNode;
            return node != null ? node.equals(compositeNode.childNode) : compositeNode.childNode == null;
        }
        return false;
    }

    @Override // ch.qos.logback.core.pattern.parser.SimpleKeywordNode, ch.qos.logback.core.pattern.parser.FormattingNode, ch.qos.logback.core.pattern.parser.Node
    public int hashCode() {
        return super.hashCode();
    }

    @Override // ch.qos.logback.core.pattern.parser.SimpleKeywordNode, ch.qos.logback.core.pattern.parser.Node
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.childNode != null) {
            sb.append("CompositeNode(" + this.childNode + ")");
        } else {
            sb.append("CompositeNode(no child)");
        }
        sb.append(printNext());
        return sb.toString();
    }
}
