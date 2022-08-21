package ch.qos.logback.core.pattern;

/* loaded from: classes.dex */
public class IdentityCompositeConverter<E> extends CompositeConverter<E> {
    @Override // ch.qos.logback.core.pattern.CompositeConverter
    public String transform(E e, String str) {
        return str;
    }
}
