package miuix.internal.hybrid;

import com.xiaomi.stat.MiStat;
import java.util.TreeSet;

/* loaded from: classes3.dex */
public class ConfigUtils {
    public static String getRawConfig(Config config) {
        return "{timestamp:" + config.getSecurity().getTimestamp() + ",vendor:\"" + config.getVendor() + "\"," + buildFeatures(config) + "," + buildPermissions(config) + "}";
    }

    public static String buildFeatures(Config config) {
        return "features:[" + buildFeature(config) + "]";
    }

    public static String buildFeature(Config config) {
        StringBuilder sb = new StringBuilder();
        TreeSet<String> treeSet = new TreeSet(config.getFeatures().keySet());
        if (treeSet.isEmpty()) {
            return "";
        }
        for (String str : treeSet) {
            sb.append("{");
            sb.append("name");
            sb.append(":");
            sb.append("\"");
            sb.append(str);
            sb.append("\"");
            sb.append(",");
            sb.append("params");
            sb.append(":");
            sb.append("[");
            sb.append(buildParam(config.getFeature(str)));
            sb.append("]");
            sb.append("}");
            sb.append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }

    public static String buildParam(Feature feature) {
        StringBuilder sb = new StringBuilder();
        TreeSet<String> treeSet = new TreeSet(feature.getParams().keySet());
        if (treeSet.isEmpty()) {
            return "";
        }
        for (String str : treeSet) {
            sb.append("{");
            sb.append("name");
            sb.append(":");
            sb.append("\"");
            sb.append(str);
            sb.append("\"");
            sb.append(",");
            sb.append("value");
            sb.append(":");
            sb.append("\"");
            sb.append(feature.getParam(str));
            sb.append("\"");
            sb.append("}");
            sb.append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }

    public static String buildPermissions(Config config) {
        return "permissions:[" + buildPermission(config) + "]";
    }

    public static Object buildPermission(Config config) {
        StringBuilder sb = new StringBuilder();
        TreeSet<String> treeSet = new TreeSet(config.getPermissions().keySet());
        if (treeSet.isEmpty()) {
            return "";
        }
        for (String str : treeSet) {
            sb.append("{");
            sb.append(MiStat.Param.ORIGIN);
            sb.append(":");
            sb.append("\"");
            sb.append(str);
            sb.append("\"");
            sb.append(",");
            sb.append("subdomains");
            sb.append(":");
            sb.append(config.getPermission(str).isApplySubdomains());
            sb.append("}");
            sb.append(",");
        }
        return sb.substring(0, sb.length() - 1);
    }
}
