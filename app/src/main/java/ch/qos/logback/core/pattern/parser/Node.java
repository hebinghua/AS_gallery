package ch.qos.logback.core.pattern.parser;

/* loaded from: classes.dex */
public class Node {
    public static final int COMPOSITE_KEYWORD = 2;
    public static final int LITERAL = 0;
    public static final int SIMPLE_KEYWORD = 1;
    public Node next;
    public final int type;
    public final Object value;

    public Node(int i) {
        this(i, null);
    }

    public Node(int i, Object obj) {
        this.type = i;
        this.value = obj;
    }

    public int getType() {
        return this.type;
    }

    public Object getValue() {
        return this.value;
    }

    public Node getNext() {
        return this.next;
    }

    public void setNext(Node node) {
        this.next = node;
    }

    public boolean equals(Object obj) {
        Object obj2;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Node)) {
            return false;
        }
        Node node = (Node) obj;
        if (this.type == node.type && ((obj2 = this.value) == null ? node.value == null : obj2.equals(node.value))) {
            Node node2 = this.next;
            if (node2 != null) {
                if (node2.equals(node.next)) {
                    return true;
                }
            } else if (node.next == null) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int i = this.type * 31;
        Object obj = this.value;
        return i + (obj != null ? obj.hashCode() : 0);
    }

    public String printNext() {
        if (this.next != null) {
            return " -> " + this.next;
        }
        return "";
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        if (this.type == 0) {
            stringBuffer.append("LITERAL(" + this.value + ")");
        } else {
            stringBuffer.append(super.toString());
        }
        stringBuffer.append(printNext());
        return stringBuffer.toString();
    }
}
