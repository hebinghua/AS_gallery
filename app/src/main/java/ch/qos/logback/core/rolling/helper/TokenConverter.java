package ch.qos.logback.core.rolling.helper;

/* loaded from: classes.dex */
public class TokenConverter {
    public static final int DATE = 1;
    public static final int IDENTITY = 0;
    public static final int INTEGER = 1;
    public TokenConverter next;
    public int type;

    public TokenConverter(int i) {
        this.type = i;
    }

    public TokenConverter getNext() {
        return this.next;
    }

    public void setNext(TokenConverter tokenConverter) {
        this.next = tokenConverter;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }
}
