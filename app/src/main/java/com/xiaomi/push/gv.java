package com.xiaomi.push;

import android.text.TextUtils;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.push.gl;
import com.xiaomi.push.gp;
import com.xiaomi.push.gr;
import com.xiaomi.push.service.bg;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/* loaded from: classes3.dex */
public class gv {
    public static XmlPullParser a;

    public static gk a(String str, String str2, XmlPullParser xmlPullParser) {
        Object a2 = gu.a().a("all", "xm:chat");
        if (a2 == null || !(a2 instanceof com.xiaomi.push.service.k)) {
            return null;
        }
        return ((com.xiaomi.push.service.k) a2).b(xmlPullParser);
    }

    public static gl a(XmlPullParser xmlPullParser, fw fwVar) {
        String attributeValue = xmlPullParser.getAttributeValue("", "id");
        String attributeValue2 = xmlPullParser.getAttributeValue("", "to");
        String attributeValue3 = xmlPullParser.getAttributeValue("", "from");
        String attributeValue4 = xmlPullParser.getAttributeValue("", "chid");
        gl.a a2 = gl.a.a(xmlPullParser.getAttributeValue("", nexExportFormat.TAG_FORMAT_TYPE));
        HashMap hashMap = new HashMap();
        boolean z = false;
        for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
            String attributeName = xmlPullParser.getAttributeName(i);
            hashMap.put(attributeName, xmlPullParser.getAttributeValue("", attributeName));
        }
        gl glVar = null;
        gr grVar = null;
        while (!z) {
            int next = xmlPullParser.next();
            if (next == 2) {
                String name = xmlPullParser.getName();
                String namespace = xmlPullParser.getNamespace();
                if (name.equals("error")) {
                    grVar = m2209a(xmlPullParser);
                } else {
                    glVar = new gl();
                    glVar.a(a(name, namespace, xmlPullParser));
                }
            } else if (next == 3 && xmlPullParser.getName().equals("iq")) {
                z = true;
            }
        }
        if (glVar == null) {
            if (gl.a.a == a2 || gl.a.b == a2) {
                gw gwVar = new gw();
                gwVar.k(attributeValue);
                gwVar.m(attributeValue3);
                gwVar.n(attributeValue2);
                gwVar.a(gl.a.d);
                gwVar.l(attributeValue4);
                gwVar.a(new gr(gr.a.e));
                fwVar.a(gwVar);
                com.xiaomi.channel.commonutils.logger.b.d("iq usage error. send packet in packet parser.");
                return null;
            }
            glVar = new gx();
        }
        glVar.k(attributeValue);
        glVar.m(attributeValue2);
        glVar.l(attributeValue4);
        glVar.n(attributeValue3);
        glVar.a(a2);
        glVar.a(grVar);
        glVar.a(hashMap);
        return glVar;
    }

    public static gn a(XmlPullParser xmlPullParser) {
        String str;
        boolean z = false;
        String str2 = null;
        if ("1".equals(xmlPullParser.getAttributeValue("", "s"))) {
            String attributeValue = xmlPullParser.getAttributeValue("", "chid");
            String attributeValue2 = xmlPullParser.getAttributeValue("", "id");
            String attributeValue3 = xmlPullParser.getAttributeValue("", "from");
            String attributeValue4 = xmlPullParser.getAttributeValue("", "to");
            String attributeValue5 = xmlPullParser.getAttributeValue("", nexExportFormat.TAG_FORMAT_TYPE);
            bg.b a2 = com.xiaomi.push.service.bg.a().a(attributeValue, attributeValue4);
            if (a2 == null) {
                a2 = com.xiaomi.push.service.bg.a().a(attributeValue, attributeValue3);
            }
            if (a2 == null) {
                throw new gh("the channel id is wrong while receiving a encrypted message");
            }
            gn gnVar = null;
            while (!z) {
                int next = xmlPullParser.next();
                if (next == 2) {
                    if (!"s".equals(xmlPullParser.getName())) {
                        throw new gh("error while receiving a encrypted message with wrong format");
                    }
                    if (xmlPullParser.next() != 4) {
                        throw new gh("error while receiving a encrypted message with wrong format");
                    }
                    String text = xmlPullParser.getText();
                    if ("5".equals(attributeValue) || "6".equals(attributeValue)) {
                        gm gmVar = new gm();
                        gmVar.l(attributeValue);
                        gmVar.b(true);
                        gmVar.n(attributeValue3);
                        gmVar.m(attributeValue4);
                        gmVar.k(attributeValue2);
                        gmVar.f(attributeValue5);
                        gk gkVar = new gk("s", null, null, null);
                        gkVar.m2195a(text);
                        gmVar.a(gkVar);
                        return gmVar;
                    }
                    a(com.xiaomi.push.service.bp.a(com.xiaomi.push.service.bp.a(a2.h, attributeValue2), text));
                    a.next();
                    gnVar = a(a);
                } else if (next == 3 && xmlPullParser.getName().equals("message")) {
                    z = true;
                }
            }
            if (gnVar == null) {
                throw new gh("error while receiving a encrypted message with wrong format");
            }
            return gnVar;
        }
        gm gmVar2 = new gm();
        String attributeValue6 = xmlPullParser.getAttributeValue("", "id");
        if (attributeValue6 == null) {
            attributeValue6 = "ID_NOT_AVAILABLE";
        }
        gmVar2.k(attributeValue6);
        gmVar2.m(xmlPullParser.getAttributeValue("", "to"));
        gmVar2.n(xmlPullParser.getAttributeValue("", "from"));
        gmVar2.l(xmlPullParser.getAttributeValue("", "chid"));
        gmVar2.a(xmlPullParser.getAttributeValue("", "appid"));
        try {
            str = xmlPullParser.getAttributeValue("", "transient");
        } catch (Exception unused) {
            str = null;
        }
        try {
            String attributeValue7 = xmlPullParser.getAttributeValue("", "seq");
            if (!TextUtils.isEmpty(attributeValue7)) {
                gmVar2.b(attributeValue7);
            }
        } catch (Exception unused2) {
        }
        try {
            String attributeValue8 = xmlPullParser.getAttributeValue("", "mseq");
            if (!TextUtils.isEmpty(attributeValue8)) {
                gmVar2.c(attributeValue8);
            }
        } catch (Exception unused3) {
        }
        try {
            String attributeValue9 = xmlPullParser.getAttributeValue("", "fseq");
            if (!TextUtils.isEmpty(attributeValue9)) {
                gmVar2.d(attributeValue9);
            }
        } catch (Exception unused4) {
        }
        try {
            String attributeValue10 = xmlPullParser.getAttributeValue("", "status");
            if (!TextUtils.isEmpty(attributeValue10)) {
                gmVar2.e(attributeValue10);
            }
        } catch (Exception unused5) {
        }
        gmVar2.a(!TextUtils.isEmpty(str) && str.equalsIgnoreCase("true"));
        gmVar2.f(xmlPullParser.getAttributeValue("", nexExportFormat.TAG_FORMAT_TYPE));
        String b = b(xmlPullParser);
        if (b == null || "".equals(b.trim())) {
            gn.q();
        } else {
            gmVar2.j(b);
        }
        while (!z) {
            int next2 = xmlPullParser.next();
            if (next2 == 2) {
                String name = xmlPullParser.getName();
                String namespace = xmlPullParser.getNamespace();
                if (TextUtils.isEmpty(namespace)) {
                    namespace = "xm";
                }
                if (name.equals("subject")) {
                    b(xmlPullParser);
                    gmVar2.g(m2210a(xmlPullParser));
                } else if (name.equals("body")) {
                    String attributeValue11 = xmlPullParser.getAttributeValue("", "encode");
                    String m2210a = m2210a(xmlPullParser);
                    if (!TextUtils.isEmpty(attributeValue11)) {
                        gmVar2.a(m2210a, attributeValue11);
                    } else {
                        gmVar2.h(m2210a);
                    }
                } else if (name.equals("thread")) {
                    if (str2 == null) {
                        str2 = xmlPullParser.nextText();
                    }
                } else if (name.equals("error")) {
                    gmVar2.a(m2209a(xmlPullParser));
                } else {
                    gmVar2.a(a(name, namespace, xmlPullParser));
                }
            } else if (next2 == 3 && xmlPullParser.getName().equals("message")) {
                z = true;
            }
        }
        gmVar2.i(str2);
        return gmVar2;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static gp m2207a(XmlPullParser xmlPullParser) {
        gp.b bVar = gp.b.available;
        String attributeValue = xmlPullParser.getAttributeValue("", nexExportFormat.TAG_FORMAT_TYPE);
        if (attributeValue != null && !attributeValue.equals("")) {
            try {
                bVar = gp.b.valueOf(attributeValue);
            } catch (IllegalArgumentException unused) {
                System.err.println("Found invalid presence type " + attributeValue);
            }
        }
        gp gpVar = new gp(bVar);
        gpVar.m(xmlPullParser.getAttributeValue("", "to"));
        gpVar.n(xmlPullParser.getAttributeValue("", "from"));
        gpVar.l(xmlPullParser.getAttributeValue("", "chid"));
        String attributeValue2 = xmlPullParser.getAttributeValue("", "id");
        if (attributeValue2 == null) {
            attributeValue2 = "ID_NOT_AVAILABLE";
        }
        gpVar.k(attributeValue2);
        boolean z = false;
        while (!z) {
            int next = xmlPullParser.next();
            if (next == 2) {
                String name = xmlPullParser.getName();
                String namespace = xmlPullParser.getNamespace();
                if (name.equals("status")) {
                    gpVar.a(xmlPullParser.nextText());
                } else if (name.equals(com.xiaomi.stat.a.j.k)) {
                    try {
                        gpVar.a(Integer.parseInt(xmlPullParser.nextText()));
                    } catch (NumberFormatException unused2) {
                    } catch (IllegalArgumentException unused3) {
                        gpVar.a(0);
                    }
                } else if (name.equals("show")) {
                    String nextText = xmlPullParser.nextText();
                    try {
                        gpVar.a(gp.a.valueOf(nextText));
                    } catch (IllegalArgumentException unused4) {
                        System.err.println("Found invalid presence mode " + nextText);
                    }
                } else if (name.equals("error")) {
                    gpVar.a(m2209a(xmlPullParser));
                } else {
                    gpVar.a(a(name, namespace, xmlPullParser));
                }
            } else if (next == 3 && xmlPullParser.getName().equals("presence")) {
                z = true;
            }
        }
        return gpVar;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static gq m2208a(XmlPullParser xmlPullParser) {
        gq gqVar = null;
        boolean z = false;
        while (!z) {
            int next = xmlPullParser.next();
            if (next == 2) {
                gqVar = new gq(xmlPullParser.getName());
            } else if (next == 3 && xmlPullParser.getName().equals("error")) {
                z = true;
            }
        }
        return gqVar;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static gr m2209a(XmlPullParser xmlPullParser) {
        ArrayList arrayList = new ArrayList();
        boolean z = false;
        String str = "-1";
        String str2 = null;
        String str3 = null;
        for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
            if (xmlPullParser.getAttributeName(i).equals("code")) {
                str = xmlPullParser.getAttributeValue("", "code");
            }
            if (xmlPullParser.getAttributeName(i).equals(nexExportFormat.TAG_FORMAT_TYPE)) {
                str3 = xmlPullParser.getAttributeValue("", nexExportFormat.TAG_FORMAT_TYPE);
            }
            if (xmlPullParser.getAttributeName(i).equals("reason")) {
                str2 = xmlPullParser.getAttributeValue("", "reason");
            }
        }
        String str4 = null;
        String str5 = null;
        while (!z) {
            int next = xmlPullParser.next();
            if (next == 2) {
                if (xmlPullParser.getName().equals("text")) {
                    str5 = xmlPullParser.nextText();
                } else {
                    String name = xmlPullParser.getName();
                    String namespace = xmlPullParser.getNamespace();
                    if ("urn:ietf:params:xml:ns:xmpp-stanzas".equals(namespace)) {
                        str4 = name;
                    } else {
                        arrayList.add(a(name, namespace, xmlPullParser));
                    }
                }
            } else if (next == 3) {
                if (xmlPullParser.getName().equals("error")) {
                    z = true;
                }
            } else if (next == 4) {
                str5 = xmlPullParser.getText();
            }
        }
        return new gr(Integer.parseInt(str), str3 == null ? "cancel" : str3, str2, str4, str5, arrayList);
    }

    /* renamed from: a  reason: collision with other method in class */
    public static String m2210a(XmlPullParser xmlPullParser) {
        int depth = xmlPullParser.getDepth();
        String str = "";
        while (true) {
            if (xmlPullParser.next() == 3 && xmlPullParser.getDepth() == depth) {
                return str;
            }
            str = str + xmlPullParser.getText();
        }
    }

    public static void a(byte[] bArr) {
        if (a == null) {
            try {
                XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
                a = newPullParser;
                newPullParser.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", true);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
        }
        a.setInput(new InputStreamReader(new ByteArrayInputStream(bArr)));
    }

    public static String b(XmlPullParser xmlPullParser) {
        for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
            String attributeName = xmlPullParser.getAttributeName(i);
            if ("xml:lang".equals(attributeName) || ("lang".equals(attributeName) && "xml".equals(xmlPullParser.getAttributePrefix(i)))) {
                return xmlPullParser.getAttributeValue(i);
            }
        }
        return null;
    }
}
