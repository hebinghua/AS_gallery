package miuix.internal.hybrid;

import java.util.HashMap;
import java.util.Map;

/* loaded from: classes3.dex */
public class Feature {
    public String name;
    public Map<String, String> params = new HashMap();

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public Map<String, String> getParams() {
        return this.params;
    }

    public String getParam(String str) {
        return this.params.get(str);
    }

    public void setParam(String str, String str2) {
        this.params.put(str, str2);
    }
}
