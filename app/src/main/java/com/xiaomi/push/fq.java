package com.xiaomi.push;

import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

/* loaded from: classes3.dex */
public class fq {
    public XmlPullParser a;

    public fq() {
        try {
            XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
            this.a = newPullParser;
            newPullParser.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", true);
        } catch (XmlPullParserException unused) {
        }
    }

    public gn a(byte[] bArr, fw fwVar) {
        String name;
        String str;
        this.a.setInput(new InputStreamReader(new ByteArrayInputStream(bArr)));
        this.a.next();
        int eventType = this.a.getEventType();
        String name2 = this.a.getName();
        if (eventType == 2) {
            if (name2.equals("message")) {
                return gv.a(this.a);
            }
            if (name2.equals("iq")) {
                return gv.a(this.a, fwVar);
            }
            if (name2.equals("presence")) {
                return gv.m2207a(this.a);
            }
            if (this.a.getName().equals("stream")) {
                return null;
            }
            if (this.a.getName().equals("error")) {
                throw new gh(gv.m2208a(this.a));
            }
            if (this.a.getName().equals("warning")) {
                this.a.next();
                name = this.a.getName();
                str = "multi-login";
            } else {
                name = this.a.getName();
                str = "bind";
            }
            name.equals(str);
            return null;
        }
        return null;
    }
}
