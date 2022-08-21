package miuix.internal.hybrid;

import com.xiaomi.stat.MiStat;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public class JsonConfigParser implements ConfigParser {
    public JSONObject mJson;

    public final Config buildCompleteConfig(Config config, Map<String, Object> map) {
        return config;
    }

    public JsonConfigParser(JSONObject jSONObject) {
        this.mJson = jSONObject;
    }

    public static JsonConfigParser createFromString(String str) throws HybridException {
        try {
            return createFromJSONObject(new JSONObject(str));
        } catch (JSONException e) {
            throw new HybridException(201, e.getMessage());
        }
    }

    public static JsonConfigParser createFromJSONObject(JSONObject jSONObject) {
        return new JsonConfigParser(jSONObject);
    }

    @Override // miuix.internal.hybrid.ConfigParser
    public Config parse(Map<String, Object> map) throws HybridException {
        Config config = new Config();
        try {
            JSONObject jSONObject = this.mJson;
            Security security = new Security();
            security.setSignature(jSONObject.getString("signature"));
            security.setTimestamp(jSONObject.getLong("timestamp"));
            config.setSecurity(security);
            config.setVendor(jSONObject.getString("vendor"));
            config.setContent(jSONObject.optString(MiStat.Param.CONTENT));
            parseFeatures(config, jSONObject);
            parsePermissions(config, jSONObject);
            return buildCompleteConfig(config, map);
        } catch (JSONException e) {
            throw new HybridException(201, e.getMessage());
        }
    }

    public final void parseFeatures(Config config, JSONObject jSONObject) throws JSONException {
        JSONArray optJSONArray = jSONObject.optJSONArray("features");
        if (optJSONArray != null) {
            for (int i = 0; i < optJSONArray.length(); i++) {
                JSONObject jSONObject2 = optJSONArray.getJSONObject(i);
                Feature feature = new Feature();
                feature.setName(jSONObject2.getString("name"));
                JSONArray optJSONArray2 = jSONObject2.optJSONArray("params");
                if (optJSONArray2 != null) {
                    for (int i2 = 0; i2 < optJSONArray2.length(); i2++) {
                        JSONObject jSONObject3 = optJSONArray2.getJSONObject(i2);
                        feature.setParam(jSONObject3.getString("name"), jSONObject3.getString("value"));
                    }
                }
                config.addFeature(feature);
            }
        }
    }

    public final void parsePermissions(Config config, JSONObject jSONObject) throws JSONException {
        JSONArray optJSONArray = jSONObject.optJSONArray("permissions");
        if (optJSONArray != null) {
            for (int i = 0; i < optJSONArray.length(); i++) {
                JSONObject jSONObject2 = optJSONArray.getJSONObject(i);
                Permission permission = new Permission();
                permission.setUri(jSONObject2.getString(MiStat.Param.ORIGIN));
                permission.setApplySubdomains(jSONObject2.optBoolean("subdomains"));
                config.addPermission(permission);
            }
        }
    }
}
