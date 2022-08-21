package ch.qos.logback.core.pattern;

/* loaded from: classes.dex */
public final class LiteralConverter<E> extends Converter<E> {
    public String literal;

    public LiteralConverter(String str) {
        this.literal = str;
    }

    @Override // ch.qos.logback.core.pattern.Converter
    public String convert(E e) {
        return this.literal;
    }
}
