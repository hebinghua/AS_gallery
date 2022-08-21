package miuix.internal.hybrid;

import java.util.HashMap;
import java.util.Map;
import miuix.hybrid.HybridFeature;

/* loaded from: classes3.dex */
public class FeatureManager {
    public Config mConfig;
    public Map<String, HybridFeature> mFeatures = new HashMap();
    public ClassLoader mLoader;

    public FeatureManager(Config config, ClassLoader classLoader) {
        this.mConfig = config;
        this.mLoader = classLoader;
    }

    public final HybridFeature initFeature(String str) throws HybridException {
        try {
            return (HybridFeature) this.mLoader.loadClass(str).newInstance();
        } catch (ClassNotFoundException unused) {
            throw new HybridException(204, "feature not found: " + str);
        } catch (IllegalAccessException unused2) {
            throw new HybridException(204, "feature cannot be accessed: " + str);
        } catch (InstantiationException unused3) {
            throw new HybridException(204, "feature cannot be instantiated: " + str);
        }
    }

    public HybridFeature lookupFeature(String str) throws HybridException {
        HybridFeature hybridFeature = this.mFeatures.get(str);
        if (hybridFeature == null) {
            Feature feature = this.mConfig.getFeature(str);
            if (feature == null) {
                throw new HybridException(204, "feature not declared: " + str);
            }
            HybridFeature initFeature = initFeature(str);
            initFeature.setParams(feature.getParams());
            this.mFeatures.put(str, initFeature);
            return initFeature;
        }
        return hybridFeature;
    }
}
