package miui.cloud.sdk;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.util.Log;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import miui.cloud.sdk.Dependency;
import miuix.core.util.ResourcesUtils;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes3.dex */
public class ManifestParser {
    public static final Map<String, String> PACKAGE_RESOURCE_MAP;
    public XmlResourceParser mParser;
    public Resources mResources;

    public final int getDefaultMaxLevel(Dependency.Level level) {
        return Integer.MAX_VALUE;
    }

    public final int getDefaultMinLevel(Dependency.Level level) {
        return 1;
    }

    static {
        HashMap hashMap = new HashMap(3);
        PACKAGE_RESOURCE_MAP = hashMap;
        hashMap.put("com.miui.core", "miui");
        hashMap.put("com.miui.system", "miui.system");
        hashMap.put("com.miui.rom", "android.miui");
    }

    public ManifestParser(Resources resources, XmlResourceParser xmlResourceParser) {
        this.mResources = resources;
        this.mParser = xmlResourceParser;
    }

    public static ManifestParser createFromPackage(PackageManager packageManager, String str) {
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(str, 128);
            return createFromResources(ResourcesUtils.createResources(applicationInfo.sourceDir), str, applicationInfo.metaData);
        } catch (PackageManager.NameNotFoundException unused) {
            Log.w("miuisdk", "cannot find package " + str);
            return createFromXmlParser(null, null);
        }
    }

    public static ManifestParser createFromResources(Resources resources, String str, Bundle bundle) {
        int i = bundle != null ? bundle.getInt("com.miui.sdk.manifest") : 0;
        if (i == 0) {
            String str2 = PACKAGE_RESOURCE_MAP.get(str);
            if (str2 != null) {
                str = str2;
            }
            i = resources.getIdentifier("miui_manifest", "xml", str);
        }
        return createFromXmlParser(resources, i == 0 ? null : resources.getXml(i));
    }

    public static ManifestParser createFromXmlParser(Resources resources, XmlResourceParser xmlResourceParser) {
        return new ManifestParser(resources, xmlResourceParser);
    }

    public MiuiManifest parse(Map<String, Object> map) {
        if (map == null) {
            new HashMap();
        }
        MiuiManifest miuiManifest = new MiuiManifest();
        XmlResourceParser xmlResourceParser = this.mParser;
        try {
            if (xmlResourceParser != null) {
                try {
                    Resources resources = this.mResources;
                    while (true) {
                        int next = xmlResourceParser.next();
                        if (next == 2 || next == 1) {
                            break;
                        }
                    }
                    if ("manifest".equals(xmlResourceParser.getName())) {
                        parseManifestElement(miuiManifest, resources, xmlResourceParser);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e2) {
                    e2.printStackTrace();
                }
            }
            return buildCompleteManifest(miuiManifest);
        } finally {
            this.mParser.close();
        }
    }

    public final void parseManifestElement(MiuiManifest miuiManifest, Resources resources, XmlResourceParser xmlResourceParser) throws XmlPullParserException, IOException {
        int attributeCount = xmlResourceParser.getAttributeCount();
        Module module = new Module();
        for (int i = 0; i < attributeCount; i++) {
            String attributeName = xmlResourceParser.getAttributeName(i);
            if ("name".equals(attributeName)) {
                module.setName(xmlResourceParser.getAttributeValue(i));
            } else if ("level".equals(attributeName)) {
                module.setLevel(xmlResourceParser.getAttributeIntValue(i, 0));
            } else if ("moduleContent".equals(attributeName)) {
                module.setContent(xmlResourceParser.getAttributeIntValue(i, 0));
            }
        }
        miuiManifest.setModule(module);
        int depth = xmlResourceParser.getDepth();
        while (true) {
            int next = xmlResourceParser.next();
            if (next != 1) {
                if (next == 3 && xmlResourceParser.getDepth() <= depth) {
                    return;
                }
            } else {
                return;
            }
        }
    }

    public final MiuiManifest buildCompleteManifest(MiuiManifest miuiManifest) {
        if (miuiManifest.getModule() == null) {
            miuiManifest.setModule(new Module());
        }
        if (miuiManifest.getSdkDependency() == null) {
            Dependency.Level level = new Dependency.Level();
            level.setMinLevel(getDefaultMinLevel(level));
            level.setTargetLevel(getDefaultTargetLevel(level));
            level.setMaxLevel(getDefaultMaxLevel(level));
            miuiManifest.setSdkDependency(buildCompleteSdkDependency(level));
        }
        return miuiManifest;
    }

    public final Dependency buildCompleteSdkDependency(Dependency.Level level) {
        Dependency dependency = new Dependency();
        dependency.setName("com.miui.core");
        dependency.setType(2);
        dependency.setLevel(level);
        return dependency;
    }

    public final int getDefaultTargetLevel(Dependency.Level level) {
        return level.getMinLevel();
    }
}
