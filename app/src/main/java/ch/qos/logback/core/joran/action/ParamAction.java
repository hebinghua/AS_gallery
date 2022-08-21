package ch.qos.logback.core.joran.action;

import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.core.joran.util.PropertySetter;
import org.xml.sax.Attributes;

/* loaded from: classes.dex */
public class ParamAction extends Action {
    public static String NO_NAME = "No name attribute in <param> element";
    public static String NO_VALUE = "No value attribute in <param> element";
    public boolean inError = false;

    @Override // ch.qos.logback.core.joran.action.Action
    public void end(InterpretationContext interpretationContext, String str) {
    }

    public void finish(InterpretationContext interpretationContext) {
    }

    @Override // ch.qos.logback.core.joran.action.Action
    public void begin(InterpretationContext interpretationContext, String str, Attributes attributes) {
        String value = attributes.getValue("name");
        String value2 = attributes.getValue("value");
        if (value == null) {
            this.inError = true;
            addError(NO_NAME);
        } else if (value2 == null) {
            this.inError = true;
            addError(NO_VALUE);
        } else {
            String trim = value2.trim();
            PropertySetter propertySetter = new PropertySetter(interpretationContext.peekObject());
            propertySetter.setContext(this.context);
            propertySetter.setProperty(interpretationContext.subst(value), interpretationContext.subst(trim));
        }
    }
}
