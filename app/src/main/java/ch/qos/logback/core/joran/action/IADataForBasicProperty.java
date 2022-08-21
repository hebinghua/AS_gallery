package ch.qos.logback.core.joran.action;

import ch.qos.logback.core.joran.util.PropertySetter;
import ch.qos.logback.core.util.AggregationType;

/* loaded from: classes.dex */
class IADataForBasicProperty {
    public final AggregationType aggregationType;
    public boolean inError;
    public final PropertySetter parentBean;
    public final String propertyName;

    public IADataForBasicProperty(PropertySetter propertySetter, AggregationType aggregationType, String str) {
        this.parentBean = propertySetter;
        this.aggregationType = aggregationType;
        this.propertyName = str;
    }
}
