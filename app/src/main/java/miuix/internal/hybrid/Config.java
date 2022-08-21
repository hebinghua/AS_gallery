package miuix.internal.hybrid;

import java.util.HashMap;
import java.util.Map;

/* loaded from: classes3.dex */
public class Config {
    public String content;
    public Security security;
    public String vendor;
    public Map<String, Feature> features = new HashMap();
    public Map<String, String> preferences = new HashMap();
    public Map<String, Permission> permissions = new HashMap();

    public Security getSecurity() {
        return this.security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public String getVendor() {
        return this.vendor;
    }

    public void setVendor(String str) {
        this.vendor = str;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String str) {
        this.content = str;
    }

    public Map<String, Feature> getFeatures() {
        return this.features;
    }

    public Feature getFeature(String str) {
        return this.features.get(str);
    }

    public void addFeature(Feature feature) {
        this.features.put(feature.getName(), feature);
    }

    public void setPreference(String str, String str2) {
        this.preferences.put(str, str2);
    }

    public Map<String, Permission> getPermissions() {
        return this.permissions;
    }

    public Permission getPermission(String str) {
        return this.permissions.get(str);
    }

    public void addPermission(Permission permission) {
        this.permissions.put(permission.getUri(), permission);
    }
}
