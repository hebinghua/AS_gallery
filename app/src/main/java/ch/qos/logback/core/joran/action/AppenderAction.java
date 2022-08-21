package ch.qos.logback.core.joran.action;

import ch.qos.logback.core.Appender;
import ch.qos.logback.core.joran.spi.ActionException;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.core.spi.LifeCycle;
import ch.qos.logback.core.util.OptionHelper;
import java.util.HashMap;
import org.xml.sax.Attributes;

/* loaded from: classes.dex */
public class AppenderAction<E> extends Action {
    public Appender<E> appender;
    private boolean inError = false;

    @Override // ch.qos.logback.core.joran.action.Action
    public void begin(InterpretationContext interpretationContext, String str, Attributes attributes) throws ActionException {
        this.appender = null;
        this.inError = false;
        String value = attributes.getValue(Action.CLASS_ATTRIBUTE);
        if (OptionHelper.isEmpty(value)) {
            addError("Missing class name for appender. Near [" + str + "] line " + getLineNumber(interpretationContext));
            this.inError = true;
            return;
        }
        try {
            addInfo("About to instantiate appender of type [" + value + "]");
            warnDeprecated(value);
            Appender<E> appender = (Appender) OptionHelper.instantiateByClassName(value, Appender.class, this.context);
            this.appender = appender;
            appender.setContext(this.context);
            String subst = interpretationContext.subst(attributes.getValue("name"));
            if (OptionHelper.isEmpty(subst)) {
                addWarn("No appender name given for appender of type " + value + "].");
            } else {
                this.appender.setName(subst);
                addInfo("Naming appender as [" + subst + "]");
            }
            ((HashMap) interpretationContext.getObjectMap().get(ActionConst.APPENDER_BAG)).put(subst, this.appender);
            interpretationContext.pushObject(this.appender);
        } catch (Exception e) {
            this.inError = true;
            addError("Could not create an Appender of type [" + value + "].", e);
            throw new ActionException(e);
        }
    }

    private void warnDeprecated(String str) {
        if (str.equals("ch.qos.logback.core.ConsoleAppender")) {
            addWarn("ConsoleAppender is deprecated for LogcatAppender");
        }
    }

    @Override // ch.qos.logback.core.joran.action.Action
    public void end(InterpretationContext interpretationContext, String str) {
        if (this.inError) {
            return;
        }
        Appender<E> appender = this.appender;
        if (appender instanceof LifeCycle) {
            appender.start();
        }
        if (interpretationContext.peekObject() != this.appender) {
            addWarn("The object at the of the stack is not the appender named [" + this.appender.getName() + "] pushed earlier.");
            return;
        }
        interpretationContext.popObject();
    }
}
