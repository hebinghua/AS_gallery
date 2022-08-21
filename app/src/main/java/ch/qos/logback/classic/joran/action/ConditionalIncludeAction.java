package ch.qos.logback.classic.joran.action;

import ch.qos.logback.core.joran.action.AbstractIncludeAction;
import ch.qos.logback.core.joran.spi.ActionException;
import ch.qos.logback.core.joran.spi.InterpretationContext;
import ch.qos.logback.core.joran.spi.JoranException;
import java.io.FileNotFoundException;
import java.net.URL;
import java.net.UnknownHostException;
import org.xml.sax.Attributes;

/* loaded from: classes.dex */
public class ConditionalIncludeAction extends AbstractIncludeAction {

    /* loaded from: classes.dex */
    public class State {
        private URL url;

        public State() {
        }

        public URL getUrl() {
            return this.url;
        }

        public void setUrl(URL url) {
            this.url = url;
        }
    }

    @Override // ch.qos.logback.core.joran.action.AbstractIncludeAction
    public void handleError(String str, Exception exc) {
        if (exc != null && !(exc instanceof FileNotFoundException) && !(exc instanceof UnknownHostException)) {
            addWarn(str, exc);
        } else {
            addInfo(str);
        }
    }

    @Override // ch.qos.logback.core.joran.action.AbstractIncludeAction, ch.qos.logback.core.joran.action.Action
    public void begin(InterpretationContext interpretationContext, String str, Attributes attributes) throws ActionException {
        if (peekPath(interpretationContext) != null) {
            return;
        }
        super.begin(interpretationContext, str, attributes);
    }

    @Override // ch.qos.logback.core.joran.action.AbstractIncludeAction
    public void processInclude(InterpretationContext interpretationContext, URL url) throws JoranException {
        pushPath(interpretationContext, url);
    }

    private URL peekPath(InterpretationContext interpretationContext) {
        URL url;
        if (!interpretationContext.isEmpty()) {
            Object peekObject = interpretationContext.peekObject();
            if ((peekObject instanceof State) && (url = ((State) peekObject).getUrl()) != null) {
                return url;
            }
            return null;
        }
        return null;
    }

    private URL pushPath(InterpretationContext interpretationContext, URL url) {
        State state = new State();
        state.setUrl(url);
        interpretationContext.pushObject(state);
        return url;
    }
}
