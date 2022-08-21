package ch.qos.logback.core.property;

import ch.qos.logback.core.PropertyDefinerBase;
import ch.qos.logback.core.util.OptionHelper;
import java.io.File;

/* loaded from: classes.dex */
public class FileExistsPropertyDefiner extends PropertyDefinerBase {
    public String path;

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    @Override // ch.qos.logback.core.spi.PropertyDefiner
    public String getPropertyValue() {
        if (OptionHelper.isEmpty(this.path)) {
            addError("The \"path\" property must be set.");
            return null;
        }
        return PropertyDefinerBase.booleanAsStr(new File(this.path).exists());
    }
}
