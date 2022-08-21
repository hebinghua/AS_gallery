package ch.qos.logback.core.joran.util;

/* loaded from: classes.dex */
public class IntrospectionException extends RuntimeException {
    private static final long serialVersionUID = -6760181416658938878L;

    public IntrospectionException(String str) {
        super(str);
    }

    public IntrospectionException(Exception exc) {
        super(exc);
    }
}
