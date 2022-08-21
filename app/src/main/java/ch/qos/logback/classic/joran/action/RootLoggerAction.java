package ch.qos.logback.classic.joran.action;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.joran.action.Action;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.core.util.OptionHelper;
import org.xml.sax.Attributes;

/* loaded from: classes.dex */
public class RootLoggerAction extends Action {
    public boolean inError = false;
    public Logger root;

    public void finish(InterpretationContext interpretationContext) {
    }

    @Override // ch.qos.logback.core.joran.action.Action
    public void begin(InterpretationContext interpretationContext, String str, Attributes attributes) {
        this.inError = false;
        this.root = ((LoggerContext) this.context).mo166getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
        String subst = interpretationContext.subst(attributes.getValue("level"));
        if (!OptionHelper.isEmpty(subst)) {
            Level level = Level.toLevel(subst);
            addInfo("Setting level of ROOT logger to " + level);
            this.root.setLevel(level);
        }
        interpretationContext.pushObject(this.root);
    }

    @Override // ch.qos.logback.core.joran.action.Action
    public void end(InterpretationContext interpretationContext, String str) {
        if (this.inError) {
            return;
        }
        Object peekObject = interpretationContext.peekObject();
        if (peekObject != this.root) {
            addWarn("The object on the top the of the stack is not the root logger");
            addWarn("It is: " + peekObject);
            return;
        }
        interpretationContext.popObject();
    }
}
