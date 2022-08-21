package ch.qos.logback.core.joran.spi;

import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.ContextAwareImpl;
import org.xml.sax.Locator;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Interpreter.java */
/* loaded from: classes.dex */
public class CAI_WithLocatorSupport extends ContextAwareImpl {
    public CAI_WithLocatorSupport(Context context, Interpreter interpreter) {
        super(context, interpreter);
    }

    @Override // ch.qos.logback.core.spi.ContextAwareImpl
    public Object getOrigin() {
        Locator locator = ((Interpreter) super.getOrigin()).locator;
        if (locator != null) {
            return Interpreter.class.getName() + "@" + locator.getLineNumber() + ":" + locator.getColumnNumber();
        }
        return Interpreter.class.getName() + "@NA:NA";
    }
}
