package org.apache.commons.csv;

/* loaded from: classes3.dex */
public final class Token {
    public boolean isQuoted;
    public boolean isReady;
    public Type type = Type.INVALID;
    public final StringBuilder content = new StringBuilder(50);

    /* loaded from: classes3.dex */
    public enum Type {
        INVALID,
        TOKEN,
        EOF,
        EORECORD,
        COMMENT
    }

    public void reset() {
        this.content.setLength(0);
        this.type = Type.INVALID;
        this.isReady = false;
        this.isQuoted = false;
    }

    public String toString() {
        return this.type.name() + " [" + this.content.toString() + "]";
    }
}
