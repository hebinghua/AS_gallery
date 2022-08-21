package ch.qos.logback.classic.sift;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.util.DefaultNestedComponentRules;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.joran.action.ActionConst;
import ch.qos.logback.core.joran.action.AppenderAction;
import ch.qos.logback.core.joran.spi.DefaultNestedComponentRegistry;
import ch.qos.logback.core.joran.spi.ElementPath;
import ch.qos.logback.core.joran.spi.ElementSelector;
import ch.qos.logback.core.joran.spi.RuleStore;
import ch.qos.logback.core.sift.SiftingJoranConfiguratorBase;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class SiftingJoranConfigurator extends SiftingJoranConfiguratorBase<ILoggingEvent> {
    public SiftingJoranConfigurator(String str, String str2, Map<String, String> map) {
        super(str, str2, map);
    }

    @Override // ch.qos.logback.core.joran.GenericConfigurator
    public ElementPath initialElementPath() {
        return new ElementPath("configuration");
    }

    @Override // ch.qos.logback.core.sift.SiftingJoranConfiguratorBase, ch.qos.logback.core.joran.GenericConfigurator
    public void addInstanceRules(RuleStore ruleStore) {
        super.addInstanceRules(ruleStore);
        ruleStore.addRule(new ElementSelector("configuration/appender"), new AppenderAction());
    }

    @Override // ch.qos.logback.core.joran.GenericConfigurator
    public void addDefaultNestedComponentRegistryRules(DefaultNestedComponentRegistry defaultNestedComponentRegistry) {
        DefaultNestedComponentRules.addDefaultNestedComponentRegistryRules(defaultNestedComponentRegistry);
    }

    @Override // ch.qos.logback.core.joran.GenericConfigurator
    public void buildInterpreter() {
        super.buildInterpreter();
        this.interpreter.getInterpretationContext().getObjectMap().put(ActionConst.APPENDER_BAG, new HashMap());
        HashMap hashMap = new HashMap();
        hashMap.putAll(this.parentPropertyMap);
        hashMap.put(this.key, this.value);
        this.interpreter.setInterpretationContextPropertiesMap(hashMap);
    }

    @Override // ch.qos.logback.core.sift.SiftingJoranConfiguratorBase
    public Appender<ILoggingEvent> getAppender() {
        HashMap hashMap = (HashMap) this.interpreter.getInterpretationContext().getObjectMap().get(ActionConst.APPENDER_BAG);
        oneAndOnlyOneCheck(hashMap);
        Collection values = hashMap.values();
        if (values.size() == 0) {
            return null;
        }
        return (Appender) values.iterator().next();
    }
}
