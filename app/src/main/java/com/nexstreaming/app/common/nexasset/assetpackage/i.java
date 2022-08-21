package com.nexstreaming.app.common.nexasset.assetpackage;

import android.content.Context;
import android.graphics.RectF;
import android.util.LruCache;
import android.util.Xml;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.nexstreaming.app.common.nexasset.assetpackage.g;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.mirror.synergy.CallMethod;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/* compiled from: XMLItemDefReader.java */
/* loaded from: classes3.dex */
public class i {
    private static h a = new h() { // from class: com.nexstreaming.app.common.nexasset.assetpackage.i.1
        @Override // com.nexstreaming.app.common.nexasset.assetpackage.h
        public int b() {
            return 0;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.h
        public int c() {
            return 0;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.h
        public int d() {
            return 0;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.h
        public int e() {
            return 0;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.h
        public List<g> a() {
            return Collections.emptyList();
        }
    };
    private static LruCache<String, h> b = new LruCache<>(100);

    public static h a(Context context, String str) throws XmlPullParserException, IOException {
        if (str == null) {
            return a;
        }
        h hVar = b.get(str);
        if (hVar != null) {
            return hVar;
        }
        f c2 = com.nexstreaming.app.common.nexasset.assetpackage.c.a(context).c(str);
        if (c2 == null) {
            return a;
        }
        int i = AnonymousClass2.a[c2.getType().ordinal()];
        if (i == 1 || i == 2 || i == 3) {
            h a2 = a(AssetPackageReader.a(context, c2.getPackageURI(), c2.getAssetPackage().getAssetId()).a(c2.getFilePath()));
            b.put(str, a2);
            return a2;
        }
        return a;
    }

    public static h a(InputStream inputStream) throws XmlPullParserException, IOException {
        try {
            XmlPullParser newPullParser = Xml.newPullParser();
            newPullParser.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", false);
            newPullParser.setInput(inputStream, null);
            newPullParser.nextTag();
            return a(newPullParser);
        } finally {
            inputStream.close();
        }
    }

    public static h a(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        c cVar = new c();
        String name = xmlPullParser.getName();
        if (xmlPullParser.getEventType() != 2 || (!name.equalsIgnoreCase("effect") && !name.equalsIgnoreCase("renderitem") && !name.equalsIgnoreCase("overlay"))) {
            throw new XmlPullParserException("expected <effect>, <overlay> or <renderitem>" + xmlPullParser.getPositionDescription());
        }
        if (name.equalsIgnoreCase("effect")) {
            if ("transition".equalsIgnoreCase(xmlPullParser.getAttributeValue(null, nexExportFormat.TAG_FORMAT_TYPE))) {
                cVar.a = a(xmlPullParser.getAttributeValue(null, "effectoffset"), 100);
                String attributeValue = xmlPullParser.getAttributeValue(null, "effectoverlap");
                if (attributeValue == null) {
                    attributeValue = xmlPullParser.getAttributeValue(null, "videooverlap");
                }
                cVar.b = a(attributeValue, 100);
            }
        } else if (name.equalsIgnoreCase("renderitem")) {
            if ("transition".equalsIgnoreCase(xmlPullParser.getAttributeValue(null, nexExportFormat.TAG_FORMAT_TYPE))) {
                cVar.a = a(xmlPullParser.getAttributeValue(null, "transitionoffset"), 100);
                cVar.b = a(xmlPullParser.getAttributeValue(null, "transitionoverlap"), 100);
            }
            cVar.c = a(xmlPullParser.getAttributeValue(null, nexExportFormat.TAG_FORMAT_WIDTH), 0);
            cVar.d = a(xmlPullParser.getAttributeValue(null, nexExportFormat.TAG_FORMAT_HEIGHT), 0);
        }
        cVar.e = new ArrayList();
        while (xmlPullParser.next() != 3) {
            if (xmlPullParser.getEventType() == 2) {
                String name2 = xmlPullParser.getName();
                if (!name2.equalsIgnoreCase("parameter")) {
                    if (name2.equalsIgnoreCase("userfield")) {
                        g b2 = b(xmlPullParser);
                        if (b2 != null) {
                            cVar.e.add(b2);
                        }
                    } else {
                        f(xmlPullParser);
                    }
                } else {
                    cVar.e.add(c(xmlPullParser));
                }
            }
        }
        return cVar;
    }

    private static g b(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        xmlPullParser.require(2, null, "userfield");
        a aVar = new a();
        aVar.e = xmlPullParser.getAttributeValue(null, "id");
        aVar.b = xmlPullParser.getAttributeValue(null, "default");
        aVar.f = Integer.MAX_VALUE;
        aVar.g = a(xmlPullParser.getAttributeValue(null, "maxlines"), 1) > 1;
        aVar.h = false;
        aVar.i = 0;
        aVar.j = 100;
        aVar.k = 1;
        aVar.l = a(xmlPullParser.getAttributeValue(null, "step"), (RectF) null);
        String attributeValue = xmlPullParser.getAttributeValue(null, "label");
        if (attributeValue != null) {
            HashMap hashMap = new HashMap();
            aVar.m = hashMap;
            hashMap.put("label", new HashMap());
            aVar.m.get("label").put("", attributeValue);
        }
        String attributeValue2 = xmlPullParser.getAttributeValue(null, nexExportFormat.TAG_FORMAT_TYPE);
        if ("selection".equalsIgnoreCase(attributeValue2)) {
            aVar.a = ItemParameterType.CHOICE;
        } else if ("color".equalsIgnoreCase(attributeValue2)) {
            aVar.a = ItemParameterType.RGB;
        } else if ("text".equalsIgnoreCase(attributeValue2)) {
            aVar.a = ItemParameterType.TEXT;
        } else if ("overlay".equalsIgnoreCase(attributeValue2)) {
            aVar.a = ItemParameterType.IMAGE;
        } else if ("undefined".equalsIgnoreCase(attributeValue2)) {
            f(xmlPullParser);
            return null;
        } else {
            throw new XmlPullParserException("unrecognized parameter type" + xmlPullParser.getPositionDescription());
        }
        while (xmlPullParser.next() != 3) {
            if (xmlPullParser.getEventType() == 2) {
                String name = xmlPullParser.getName();
                if (name.equalsIgnoreCase("fieldlabel")) {
                    if (aVar.m == null) {
                        aVar.m = new HashMap();
                    }
                    String attributeValue3 = xmlPullParser.getAttributeValue(null, "locale");
                    String attributeValue4 = xmlPullParser.getAttributeValue(null, "value");
                    f(xmlPullParser);
                    if (attributeValue3 != null && attributeValue4 != null) {
                        Map<String, String> map = aVar.m.get("label");
                        if (map == null) {
                            map = new HashMap<>();
                            aVar.m.put("label", map);
                        }
                        map.put(attributeValue3, attributeValue4);
                    }
                } else if (name.equalsIgnoreCase("option")) {
                    if (aVar.n == null) {
                        aVar.n = new ArrayList();
                    }
                    aVar.n.add(e(xmlPullParser));
                } else if (name.equalsIgnoreCase(CallMethod.RESULT_ICON)) {
                    if (aVar.o != null) {
                        throw new XmlPullParserException("multiple <icon> tags not allowed" + xmlPullParser.getPositionDescription());
                    }
                    aVar.o = xmlPullParser.getAttributeValue(null, MapBundleKey.MapObjKey.OBJ_SRC);
                } else {
                    f(xmlPullParser);
                }
            }
        }
        return aVar;
    }

    /* compiled from: XMLItemDefReader.java */
    /* loaded from: classes3.dex */
    public static class b implements g.a {
        public Map<String, Map<String, String>> a;
        public String b;
        public String c;

        private b() {
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.g.a
        public Map<String, Map<String, String>> a() {
            return this.a;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.g.a
        public String b() {
            return this.c;
        }
    }

    /* compiled from: XMLItemDefReader.java */
    /* loaded from: classes3.dex */
    public static class c implements h {
        private int a;
        private int b;
        private int c;
        private int d;
        private List<g> e;

        private c() {
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.h
        public List<g> a() {
            return this.e;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.h
        public int b() {
            return this.a;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.h
        public int c() {
            return this.b;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.h
        public int d() {
            return this.c;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.h
        public int e() {
            return this.d;
        }
    }

    /* compiled from: XMLItemDefReader.java */
    /* loaded from: classes3.dex */
    public static class a implements g {
        public ItemParameterType a;
        public String b;
        public String c;
        public String d;
        public String e;
        public int f;
        public boolean g;
        public boolean h;
        public int i;
        public int j;
        public int k;
        public RectF l;
        public Map<String, Map<String, String>> m;
        public List<g.a> n;
        public String o;

        private a() {
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.g
        public ItemParameterType a() {
            return this.a;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.g
        public String b() {
            return this.b;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.g
        public String c() {
            return this.c;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.g
        public String d() {
            return this.d;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.g
        public String e() {
            return k() + ":" + this.e;
        }

        private String k() {
            switch (AnonymousClass2.b[this.a.ordinal()]) {
                case 1:
                    return "selection";
                case 2:
                    return "switch";
                case 3:
                    return "image";
                case 4:
                    return "range";
                case 5:
                    return "rect";
                case 6:
                case 7:
                    return "color";
                case 8:
                    return "text";
                case 9:
                case 10:
                    return "point";
                default:
                    throw new IllegalStateException("Unknown type: " + String.valueOf(this.a));
            }
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.g
        public boolean f() {
            return this.g;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.g
        public int g() {
            return this.i;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.g
        public int h() {
            return this.j;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.g
        public Map<String, Map<String, String>> i() {
            return this.m;
        }

        @Override // com.nexstreaming.app.common.nexasset.assetpackage.g
        public List<g.a> j() {
            return this.n;
        }
    }

    /* compiled from: XMLItemDefReader.java */
    /* renamed from: com.nexstreaming.app.common.nexasset.assetpackage.i$2  reason: invalid class name */
    /* loaded from: classes3.dex */
    public static /* synthetic */ class AnonymousClass2 {
        public static final /* synthetic */ int[] a;
        public static final /* synthetic */ int[] b;

        static {
            int[] iArr = new int[ItemParameterType.values().length];
            b = iArr;
            try {
                iArr[ItemParameterType.CHOICE.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                b[ItemParameterType.SWITCH.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                b[ItemParameterType.IMAGE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                b[ItemParameterType.RANGE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                b[ItemParameterType.RECT.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                b[ItemParameterType.RGB.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                b[ItemParameterType.RGBA.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                b[ItemParameterType.TEXT.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                b[ItemParameterType.XY.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                b[ItemParameterType.XYZ.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            int[] iArr2 = new int[ItemType.values().length];
            a = iArr2;
            try {
                iArr2[ItemType.kedl.ordinal()] = 1;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                a[ItemType.renderitem.ordinal()] = 2;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                a[ItemType.overlay.ordinal()] = 3;
            } catch (NoSuchFieldError unused13) {
            }
        }
    }

    private static int a(String str, int i) {
        if (str == null) {
            return i;
        }
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException unused) {
            return i;
        }
    }

    private static RectF a(String str, RectF rectF) {
        if (str == null) {
            return rectF;
        }
        try {
            String[] split = str.split(" +");
            return split.length < 4 ? rectF : new RectF(Float.parseFloat(split[0]), Float.parseFloat(split[1]), Float.parseFloat(split[2]), Float.parseFloat(split[3]));
        } catch (NumberFormatException unused) {
            return rectF;
        }
    }

    private static boolean a(String str, boolean z) {
        if (str == null) {
            return z;
        }
        if ("true".equalsIgnoreCase(str)) {
            return true;
        }
        if (!"false".equalsIgnoreCase(str)) {
            return z;
        }
        return false;
    }

    private static g c(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        xmlPullParser.require(2, null, "parameter");
        a aVar = new a();
        aVar.e = xmlPullParser.getAttributeValue(null, "id");
        aVar.b = xmlPullParser.getAttributeValue(null, "default");
        aVar.f = a(xmlPullParser.getAttributeValue(null, "maxlen"), Integer.MAX_VALUE);
        aVar.g = a(xmlPullParser.getAttributeValue(null, "multiline"), false);
        aVar.h = a(xmlPullParser.getAttributeValue(null, "private"), false);
        aVar.i = a(xmlPullParser.getAttributeValue(null, "minvalue"), 0);
        aVar.j = a(xmlPullParser.getAttributeValue(null, "maxvalue"), 100);
        aVar.k = a(xmlPullParser.getAttributeValue(null, "step"), 1);
        aVar.l = a(xmlPullParser.getAttributeValue(null, "bounds"), (RectF) null);
        String attributeValue = xmlPullParser.getAttributeValue(null, nexExportFormat.TAG_FORMAT_TYPE);
        if ("choice".equalsIgnoreCase(attributeValue)) {
            aVar.a = ItemParameterType.CHOICE;
        } else if ("point".equalsIgnoreCase(attributeValue)) {
            int a2 = a(xmlPullParser.getAttributeValue(null, "dimensions"), 2);
            if (a2 == 2) {
                aVar.a = ItemParameterType.XY;
            } else if (a2 == 3) {
                aVar.a = ItemParameterType.XYZ;
            } else {
                throw new XmlPullParserException("unsupported number of parameter dimensions: " + a2);
            }
        } else if ("color".equalsIgnoreCase(attributeValue)) {
            if (a(xmlPullParser.getAttributeValue(null, "alpha"), false)) {
                aVar.a = ItemParameterType.RGBA;
            } else {
                aVar.a = ItemParameterType.RGB;
            }
        } else if ("range".equalsIgnoreCase(attributeValue)) {
            aVar.a = ItemParameterType.RANGE;
        } else if ("rect".equalsIgnoreCase(attributeValue)) {
            aVar.a = ItemParameterType.RECT;
        } else if ("text".equalsIgnoreCase(attributeValue)) {
            aVar.a = ItemParameterType.TEXT;
        } else if ("switch".equalsIgnoreCase(attributeValue)) {
            aVar.a = ItemParameterType.SWITCH;
            aVar.c = xmlPullParser.getAttributeValue(null, "off");
            aVar.d = xmlPullParser.getAttributeValue(null, "on");
        } else if ("image".equalsIgnoreCase(attributeValue)) {
            aVar.a = ItemParameterType.IMAGE;
        } else {
            throw new XmlPullParserException("unrecognized parameter type" + xmlPullParser.getPositionDescription());
        }
        while (xmlPullParser.next() != 3) {
            if (xmlPullParser.getEventType() == 2) {
                String name = xmlPullParser.getName();
                if (name.equalsIgnoreCase("string")) {
                    if (aVar.m == null) {
                        aVar.m = new HashMap();
                    }
                    String attributeValue2 = xmlPullParser.getAttributeValue(null, "name");
                    String attributeValue3 = xmlPullParser.getAttributeValue(null, "lang");
                    String g = g(xmlPullParser);
                    if (attributeValue2 != null && attributeValue3 != null && g != null) {
                        Map<String, String> map = aVar.m.get(attributeValue2);
                        if (map == null) {
                            map = new HashMap<>();
                            aVar.m.put(attributeValue2, map);
                        }
                        map.put(attributeValue3, g);
                    }
                } else if (name.equalsIgnoreCase("option")) {
                    if (aVar.n == null) {
                        aVar.n = new ArrayList();
                    }
                    aVar.n.add(d(xmlPullParser));
                } else if (name.equalsIgnoreCase(CallMethod.RESULT_ICON)) {
                    if (aVar.o != null) {
                        throw new XmlPullParserException("multiple <icon> tags not allowed" + xmlPullParser.getPositionDescription());
                    }
                    aVar.o = xmlPullParser.getAttributeValue(null, MapBundleKey.MapObjKey.OBJ_SRC);
                } else {
                    f(xmlPullParser);
                }
            }
        }
        return aVar;
    }

    private static g.a d(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        xmlPullParser.require(2, null, "option");
        b bVar = new b();
        bVar.c = xmlPullParser.getAttributeValue(null, "value");
        while (xmlPullParser.next() != 3) {
            if (xmlPullParser.getEventType() == 2) {
                String name = xmlPullParser.getName();
                if (name.equalsIgnoreCase("string")) {
                    String attributeValue = xmlPullParser.getAttributeValue(null, "name");
                    String attributeValue2 = xmlPullParser.getAttributeValue(null, "lang");
                    String g = g(xmlPullParser);
                    if (attributeValue != null && attributeValue2 != null && g != null) {
                        if (bVar.a == null) {
                            bVar.a = new HashMap();
                        }
                        Map<String, String> map = bVar.a.get(attributeValue);
                        if (map == null) {
                            map = new HashMap<>();
                            bVar.a.put(attributeValue, map);
                        }
                        map.put(attributeValue2, g);
                    }
                } else if (name.equalsIgnoreCase(CallMethod.RESULT_ICON)) {
                    if (bVar.b != null) {
                        throw new XmlPullParserException("multiple <icon> tags not allowed" + xmlPullParser.getPositionDescription());
                    }
                    bVar.b = xmlPullParser.getAttributeValue(null, MapBundleKey.MapObjKey.OBJ_SRC);
                } else {
                    f(xmlPullParser);
                }
            }
        }
        return bVar;
    }

    private static g.a e(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        xmlPullParser.require(2, null, "option");
        b bVar = new b();
        bVar.c = xmlPullParser.getAttributeValue(null, "value");
        bVar.b = xmlPullParser.getAttributeValue(null, CallMethod.RESULT_ICON);
        String attributeValue = xmlPullParser.getAttributeValue(null, "label");
        if (attributeValue != null) {
            HashMap hashMap = new HashMap();
            bVar.a = hashMap;
            hashMap.put("label", new HashMap());
            bVar.a.get("label").put("", attributeValue);
        }
        while (xmlPullParser.next() != 3) {
            if (xmlPullParser.getEventType() == 2) {
                if (xmlPullParser.getName().equalsIgnoreCase("fieldlabel")) {
                    if (bVar.a == null) {
                        bVar.a = new HashMap();
                    }
                    String attributeValue2 = xmlPullParser.getAttributeValue(null, "locale");
                    String attributeValue3 = xmlPullParser.getAttributeValue(null, "value");
                    f(xmlPullParser);
                    if (attributeValue2 != null && attributeValue3 != null) {
                        Map<String, String> map = bVar.a.get("label");
                        if (map == null) {
                            map = new HashMap<>();
                            bVar.a.put("label", map);
                        }
                        map.put(attributeValue2, attributeValue3);
                    }
                } else {
                    f(xmlPullParser);
                }
            }
        }
        return bVar;
    }

    private static void f(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        if (xmlPullParser.getEventType() == 2) {
            int i = 1;
            while (i != 0) {
                int next = xmlPullParser.next();
                if (next == 2) {
                    i++;
                } else if (next == 3) {
                    i--;
                }
            }
            return;
        }
        throw new IllegalStateException();
    }

    private static String g(XmlPullParser xmlPullParser) throws XmlPullParserException, IOException {
        if (xmlPullParser.getEventType() == 2) {
            int i = 1;
            String str = null;
            while (i != 0) {
                int next = xmlPullParser.next();
                if (next == 2) {
                    i++;
                } else if (next == 3) {
                    i--;
                } else if (next == 4) {
                    if (str == null) {
                        str = xmlPullParser.getText();
                    } else {
                        str = str + xmlPullParser.getText();
                    }
                }
            }
            return str;
        }
        throw new IllegalStateException();
    }
}
