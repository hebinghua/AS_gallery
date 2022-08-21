package ch.qos.logback.core.joran.spi;

import ch.qos.logback.core.Context;
import ch.qos.logback.core.joran.event.InPlayListener;
import ch.qos.logback.core.joran.event.SaxEvent;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.PropertyContainer;
import ch.qos.logback.core.util.OptionHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Stack;
import org.xml.sax.Locator;

/* loaded from: classes.dex */
public class InterpretationContext extends ContextAwareBase implements PropertyContainer {
    public Interpreter joranInterpreter;
    public Map<String, Object> objectMap;
    public Stack<Object> objectStack;
    public Map<String, String> propertiesMap;
    public final List<InPlayListener> listenerList = new ArrayList();
    public DefaultNestedComponentRegistry defaultNestedComponentRegistry = new DefaultNestedComponentRegistry();

    public InterpretationContext(Context context, Interpreter interpreter) {
        this.context = context;
        this.joranInterpreter = interpreter;
        this.objectStack = new Stack<>();
        this.objectMap = new HashMap(5);
        this.propertiesMap = new HashMap(5);
    }

    public DefaultNestedComponentRegistry getDefaultNestedComponentRegistry() {
        return this.defaultNestedComponentRegistry;
    }

    @Override // ch.qos.logback.core.spi.PropertyContainer
    public Map<String, String> getCopyOfPropertyMap() {
        return new HashMap(this.propertiesMap);
    }

    public void setPropertiesMap(Map<String, String> map) {
        this.propertiesMap = map;
    }

    public String updateLocationInfo(String str) {
        Locator locator = this.joranInterpreter.getLocator();
        if (locator != null) {
            return str + locator.getLineNumber() + ":" + locator.getColumnNumber();
        }
        return str;
    }

    public Locator getLocator() {
        return this.joranInterpreter.getLocator();
    }

    public Interpreter getJoranInterpreter() {
        return this.joranInterpreter;
    }

    public Stack<Object> getObjectStack() {
        return this.objectStack;
    }

    public boolean isEmpty() {
        return this.objectStack.isEmpty();
    }

    public Object peekObject() {
        return this.objectStack.peek();
    }

    public void pushObject(Object obj) {
        this.objectStack.push(obj);
    }

    public Object popObject() {
        return this.objectStack.pop();
    }

    public Object getObject(int i) {
        return this.objectStack.get(i);
    }

    public Map<String, Object> getObjectMap() {
        return this.objectMap;
    }

    public void addSubstitutionProperty(String str, String str2) {
        if (str == null || str2 == null) {
            return;
        }
        this.propertiesMap.put(str, str2.trim());
    }

    public void addSubstitutionProperties(Properties properties) {
        if (properties == null) {
            return;
        }
        for (String str : properties.keySet()) {
            addSubstitutionProperty(str, properties.getProperty(str));
        }
    }

    @Override // ch.qos.logback.core.spi.PropertyContainer
    public String getProperty(String str) {
        String str2 = this.propertiesMap.get(str);
        return str2 != null ? str2 : this.context.getProperty(str);
    }

    public String subst(String str) {
        if (str == null) {
            return null;
        }
        return OptionHelper.substVars(str, this, this.context);
    }

    public boolean isListenerListEmpty() {
        return this.listenerList.isEmpty();
    }

    public void addInPlayListener(InPlayListener inPlayListener) {
        if (this.listenerList.contains(inPlayListener)) {
            addWarn("InPlayListener " + inPlayListener + " has been already registered");
            return;
        }
        this.listenerList.add(inPlayListener);
    }

    public boolean removeInPlayListener(InPlayListener inPlayListener) {
        return this.listenerList.remove(inPlayListener);
    }

    public void fireInPlay(SaxEvent saxEvent) {
        for (InPlayListener inPlayListener : this.listenerList) {
            inPlayListener.inPlay(saxEvent);
        }
    }
}
