package ch.qos.logback.classic.sift;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.sift.AbstractDiscriminator;
import ch.qos.logback.core.util.OptionHelper;
import java.util.Map;

/* loaded from: classes.dex */
public class MDCBasedDiscriminator extends AbstractDiscriminator<ILoggingEvent> {
    private String defaultValue;
    private String key;

    @Override // ch.qos.logback.core.sift.Discriminator
    public String getDiscriminatingValue(ILoggingEvent iLoggingEvent) {
        Map<String, String> mDCPropertyMap = iLoggingEvent.getMDCPropertyMap();
        if (mDCPropertyMap == null) {
            return this.defaultValue;
        }
        String str = mDCPropertyMap.get(this.key);
        return str == null ? this.defaultValue : str;
    }

    @Override // ch.qos.logback.core.sift.AbstractDiscriminator, ch.qos.logback.core.spi.LifeCycle
    public void start() {
        int i;
        if (OptionHelper.isEmpty(this.key)) {
            addError("The \"Key\" property must be set");
            i = 1;
        } else {
            i = 0;
        }
        if (OptionHelper.isEmpty(this.defaultValue)) {
            i++;
            addError("The \"DefaultValue\" property must be set");
        }
        if (i == 0) {
            this.started = true;
        }
    }

    @Override // ch.qos.logback.core.sift.Discriminator
    public String getKey() {
        return this.key;
    }

    public void setKey(String str) {
        this.key = str;
    }

    public String getDefaultValue() {
        return this.defaultValue;
    }

    public void setDefaultValue(String str) {
        this.defaultValue = str;
    }
}
