package miuix.internal.hybrid;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.xiaomi.stat.MiStat;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes3.dex */
public class XmlConfigParser implements ConfigParser {
    public XmlResourceParser mParser;

    public final Config buildCompleteConfig(Config config, Map<String, Object> map) {
        return config;
    }

    public XmlConfigParser(XmlResourceParser xmlResourceParser) {
        this.mParser = xmlResourceParser;
    }

    public static XmlConfigParser create(Context context) throws HybridException {
        try {
            Bundle bundle = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128).metaData;
            int i = bundle != null ? bundle.getInt("com.miui.sdk.hybrid.config") : 0;
            if (i == 0) {
                i = context.getResources().getIdentifier("miui_hybrid_config", "xml", context.getPackageName());
            }
            return createFromResId(context, i);
        } catch (PackageManager.NameNotFoundException e) {
            throw new HybridException(201, e.getMessage());
        }
    }

    public static XmlConfigParser createFromResId(Context context, int i) throws HybridException {
        try {
            return createFromXmlParser(context.getResources().getXml(i));
        } catch (Resources.NotFoundException e) {
            throw new HybridException(201, e.getMessage());
        }
    }

    public static XmlConfigParser createFromXmlParser(XmlResourceParser xmlResourceParser) {
        return new XmlConfigParser(xmlResourceParser);
    }

    @Override // miuix.internal.hybrid.ConfigParser
    public Config parse(Map<String, Object> map) throws HybridException {
        if (map == null) {
            map = new HashMap<>();
        }
        Config config = new Config();
        XmlResourceParser xmlResourceParser = this.mParser;
        if (xmlResourceParser != null) {
            while (true) {
                try {
                    try {
                        int next = xmlResourceParser.next();
                        if (next == 2 || next == 1) {
                            break;
                        }
                    } catch (IOException e) {
                        throw new HybridException(201, e.getMessage());
                    } catch (XmlPullParserException e2) {
                        throw new HybridException(201, e2.getMessage());
                    }
                } finally {
                    this.mParser.close();
                }
            }
            if ("widget".equals(xmlResourceParser.getName())) {
                parseWidgetElement(config, xmlResourceParser);
            }
        }
        return buildCompleteConfig(config, map);
    }

    public final void parseWidgetElement(Config config, XmlResourceParser xmlResourceParser) throws XmlPullParserException, IOException {
        int depth = xmlResourceParser.getDepth();
        while (true) {
            int next = xmlResourceParser.next();
            if (next != 1) {
                if (next == 3 && xmlResourceParser.getDepth() <= depth) {
                    return;
                }
                if (next != 3 && next != 4) {
                    String name = xmlResourceParser.getName();
                    if (MiStat.Param.CONTENT.equals(name)) {
                        parseContentElement(config, xmlResourceParser);
                    } else if ("feature".equals(name)) {
                        parseFeatureElement(config, xmlResourceParser);
                    } else if ("preference".equals(name)) {
                        parsePreferenceElement(config, xmlResourceParser);
                    } else if ("access".equals(name)) {
                        parseAccessElement(config, xmlResourceParser);
                    }
                }
            } else {
                return;
            }
        }
    }

    public final void parseContentElement(Config config, XmlResourceParser xmlResourceParser) {
        config.setContent(xmlResourceParser.getAttributeValue(null, MapBundleKey.MapObjKey.OBJ_SRC));
    }

    public final void parseFeatureElement(Config config, XmlResourceParser xmlResourceParser) throws XmlPullParserException, IOException {
        Feature feature = new Feature();
        feature.setName(xmlResourceParser.getAttributeValue(null, "name"));
        int depth = xmlResourceParser.getDepth();
        while (true) {
            int next = xmlResourceParser.next();
            if (next == 1 || (next == 3 && xmlResourceParser.getDepth() <= depth)) {
                break;
            } else if (next != 3 && next != 4 && "param".equals(xmlResourceParser.getName())) {
                parseParamElement(feature, xmlResourceParser);
            }
        }
        config.addFeature(feature);
    }

    public final void parseParamElement(Feature feature, XmlResourceParser xmlResourceParser) throws XmlPullParserException, IOException {
        feature.setParam(xmlResourceParser.getAttributeValue(null, "name").toLowerCase(), xmlResourceParser.getAttributeValue(null, "value"));
    }

    public final void parsePreferenceElement(Config config, XmlResourceParser xmlResourceParser) {
        String lowerCase = xmlResourceParser.getAttributeValue(null, "name").toLowerCase();
        String attributeValue = xmlResourceParser.getAttributeValue(null, "value");
        if ("signature".equals(lowerCase)) {
            getSecurity(config).setSignature(attributeValue);
        } else if ("timestamp".equals(lowerCase)) {
            getSecurity(config).setTimestamp(Long.parseLong(attributeValue));
        } else if ("vendor".equals(lowerCase)) {
            config.setVendor(attributeValue);
        } else {
            config.setPreference(lowerCase, attributeValue);
        }
    }

    public final Security getSecurity(Config config) {
        Security security = config.getSecurity();
        if (security == null) {
            Security security2 = new Security();
            config.setSecurity(security2);
            return security2;
        }
        return security;
    }

    public final void parseAccessElement(Config config, XmlResourceParser xmlResourceParser) {
        Permission permission = new Permission();
        permission.setUri(xmlResourceParser.getAttributeValue(null, MiStat.Param.ORIGIN));
        permission.setApplySubdomains(xmlResourceParser.getAttributeBooleanValue(null, "subdomains", false));
        permission.setForbidden(false);
        config.addPermission(permission);
    }
}
