package com.xiaomi.push;

import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Vector;
import org.keyczar.Keyczar;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/* loaded from: classes3.dex */
public final class gc {
    public static int a = 5000;

    /* renamed from: a  reason: collision with other field name */
    public static Vector<String> f406a = new Vector<>();
    public static int b = 330000;
    public static int c = 600000;

    static {
        try {
            for (ClassLoader classLoader : m2189a()) {
                Enumeration<URL> resources = classLoader.getResources("META-INF/smack-config.xml");
                while (resources.hasMoreElements()) {
                    InputStream inputStream = null;
                    try {
                        inputStream = resources.nextElement().openStream();
                        XmlPullParser newPullParser = XmlPullParserFactory.newInstance().newPullParser();
                        newPullParser.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", true);
                        newPullParser.setInput(inputStream, Keyczar.DEFAULT_ENCODING);
                        int eventType = newPullParser.getEventType();
                        do {
                            if (eventType == 2) {
                                if (newPullParser.getName().equals("className")) {
                                    a(newPullParser);
                                } else if (newPullParser.getName().equals("packetReplyTimeout")) {
                                    a = a(newPullParser, a);
                                } else if (newPullParser.getName().equals("keepAliveInterval")) {
                                    b = a(newPullParser, b);
                                } else if (newPullParser.getName().equals("mechName")) {
                                    f406a.add(newPullParser.nextText());
                                }
                            }
                            eventType = newPullParser.next();
                        } while (eventType != 1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        inputStream.close();
                    } catch (Exception unused) {
                    }
                }
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public static int a() {
        return b;
    }

    public static int a(XmlPullParser xmlPullParser, int i) {
        try {
            return Integer.parseInt(xmlPullParser.nextText());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return i;
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public static String m2188a() {
        return "3.1.0";
    }

    public static void a(XmlPullParser xmlPullParser) {
        String nextText = xmlPullParser.nextText();
        try {
            Class.forName(nextText);
        } catch (ClassNotFoundException unused) {
            PrintStream printStream = System.err;
            printStream.println("Error! A startup class specified in smack-config.xml could not be loaded: " + nextText);
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public static ClassLoader[] m2189a() {
        ClassLoader[] classLoaderArr = {gc.class.getClassLoader(), Thread.currentThread().getContextClassLoader()};
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < 2; i++) {
            ClassLoader classLoader = classLoaderArr[i];
            if (classLoader != null) {
                arrayList.add(classLoader);
            }
        }
        return (ClassLoader[]) arrayList.toArray(new ClassLoader[arrayList.size()]);
    }

    public static int b() {
        return c;
    }
}
