package miui.cloud.sdk;

import java.util.LinkedHashMap;
import java.util.Map;

/* loaded from: classes3.dex */
public class MiuiManifest {
    public Map<String, Dependency> dependencies = new LinkedHashMap();
    public Module module;
    public Dependency sdkDependency;

    public Module getModule() {
        return this.module;
    }

    public void setModule(Module module) {
        this.module = module;
    }

    public Dependency getSdkDependency() {
        return this.sdkDependency;
    }

    public void setSdkDependency(Dependency dependency) {
        this.sdkDependency = dependency;
    }
}
