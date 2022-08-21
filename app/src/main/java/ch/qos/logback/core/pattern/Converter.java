package ch.qos.logback.core.pattern;

/* loaded from: classes.dex */
public abstract class Converter<E> {
    public Converter<E> next;

    public abstract String convert(E e);

    public void write(StringBuilder sb, E e) {
        sb.append(convert(e));
    }

    public final void setNext(Converter<E> converter) {
        if (this.next != null) {
            throw new IllegalStateException("Next converter has been already set");
        }
        this.next = converter;
    }

    public final Converter<E> getNext() {
        return this.next;
    }
}
