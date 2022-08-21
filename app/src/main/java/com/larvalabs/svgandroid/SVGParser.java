package com.larvalabs.svgandroid;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Picture;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.Log;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

/* loaded from: classes.dex */
public class SVGParser {

    /* loaded from: classes.dex */
    public enum Prefix {
        matrix,
        translate,
        scale,
        skewX,
        skewY,
        rotate
    }

    public static void a(Path path, float f2, float f3, float f4, float f5, float f6, float f7, float f8, int i, int i2) {
    }

    /* loaded from: classes.dex */
    public static class c {
        public boolean a;
        public int b;
        public boolean c;
        public int d;
        public float e;
        public int f;
        public int g;
        public Map<Integer, Integer> h;
        public Set<Integer> i;
        public int j;

        public c() {
            this.a = false;
            this.b = 0;
            this.c = false;
            this.d = 0;
            this.e = 0.0f;
            this.f = 0;
            this.g = 0;
            this.i = new HashSet();
        }

        public c(c cVar) {
            this.a = false;
            this.b = 0;
            this.c = false;
            this.d = 0;
            this.e = 0.0f;
            this.f = 0;
            this.g = 0;
            this.i = new HashSet();
            if (cVar != null) {
                this.a = cVar.a;
                this.b = cVar.b;
                this.c = cVar.c;
                this.d = cVar.d;
                this.e = cVar.e;
                this.f = cVar.f;
                this.g = cVar.g;
                if (cVar.h != null) {
                    HashMap hashMap = new HashMap();
                    this.h = hashMap;
                    hashMap.putAll(cVar.h);
                }
                this.j = cVar.j;
                this.i.addAll(cVar.i);
            }
        }

        public void a(Map<Integer, Integer> map, int i) {
            this.h = map;
            this.j = i;
        }

        public final int a(int i) {
            Log.d("SVGAndroid", "mapColor");
            Map<Integer, Integer> map = this.h;
            if (map != null && map.containsKey(Integer.valueOf(i))) {
                Log.d("SVGAndroid", "mapColor : EXACT");
                return this.h.get(Integer.valueOf(i)).intValue();
            }
            Map<Integer, Integer> map2 = this.h;
            if (map2 != null && this.j > 0) {
                for (Map.Entry<Integer, Integer> entry : map2.entrySet()) {
                    int intValue = entry.getKey().intValue();
                    if (Math.max(Math.max(Math.abs(Color.red(intValue) - Color.red(i)), Math.abs(Color.green(intValue) - Color.green(i))), Math.abs(Color.blue(intValue) - Color.blue(i))) <= this.j) {
                        Log.d("SVGAndroid", "mapColor : SIMILAR");
                        int intValue2 = entry.getValue().intValue();
                        return Color.rgb(Math.max(0, Math.min(255, (Color.red(i) - Color.red(intValue)) + Color.red(intValue2))), Math.max(0, Math.min(255, (Color.green(i) - Color.green(intValue)) + Color.green(intValue2))), Math.max(0, Math.min(255, (Color.blue(i) - Color.blue(intValue)) + Color.blue(intValue2))));
                    }
                }
            }
            Log.d("SVGAndroid", "mapColor : NO CHANGE");
            return i;
        }
    }

    public static com.larvalabs.svgandroid.b a(InputStream inputStream) throws SVGParseException {
        return a(inputStream, (c) null);
    }

    public static com.larvalabs.svgandroid.b a(InputStream inputStream, Map<Integer, Integer> map, int i) throws SVGParseException {
        c cVar = new c();
        cVar.a(map, i);
        return a(inputStream, cVar);
    }

    public static com.larvalabs.svgandroid.b a(InputStream inputStream, c cVar) throws SVGParseException {
        Log.d("SVGAndroid", "Parse IN");
        try {
            System.currentTimeMillis();
            XMLReader xMLReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            Picture picture = new Picture();
            e eVar = new e(picture, cVar, null);
            xMLReader.setContentHandler(eVar);
            xMLReader.parse(new InputSource(inputStream));
            com.larvalabs.svgandroid.b bVar = new com.larvalabs.svgandroid.b(picture, eVar.e, eVar.a().i);
            if (!Float.isInfinite(eVar.f.top)) {
                bVar.a(eVar.f);
            }
            Log.d("SVGAndroid", "Parse OUT");
            return bVar;
        } catch (Exception e2) {
            Log.w("SVGAndroid", "Parse Error", e2);
            throw new SVGParseException(e2);
        }
    }

    public static b d(String str) {
        int length = str.length();
        ArrayList arrayList = new ArrayList();
        int i = 0;
        boolean z = false;
        for (int i2 = 1; i2 < length; i2++) {
            if (z) {
                z = false;
            } else {
                char charAt = str.charAt(i2);
                switch (charAt) {
                    case '\t':
                    case '\n':
                    case ' ':
                    case ',':
                        String substring = str.substring(i, i2);
                        if (substring.trim().length() > 0) {
                            arrayList.add(Float.valueOf(Float.parseFloat(substring)));
                            if (charAt == '-') {
                                i = i2;
                                break;
                            } else {
                                i = i2 + 1;
                                z = true;
                                break;
                            }
                        } else {
                            i++;
                            continue;
                        }
                    case ')':
                    case 'A':
                    case 'C':
                    case 'H':
                    case 'L':
                    case 'M':
                    case 'Q':
                    case 'S':
                    case 'T':
                    case 'V':
                    case 'Z':
                    case 'a':
                    case 'c':
                    case 'h':
                    case 'l':
                    case 'm':
                    case 'q':
                    case 's':
                    case 't':
                    case 'v':
                    case 'z':
                        String substring2 = str.substring(i, i2);
                        if (substring2.trim().length() > 0) {
                            arrayList.add(Float.valueOf(Float.parseFloat(substring2)));
                        }
                        return new b(arrayList, i2);
                }
            }
        }
        String substring3 = str.substring(i);
        if (substring3.length() > 0) {
            try {
                arrayList.add(Float.valueOf(Float.parseFloat(substring3)));
            } catch (NumberFormatException unused) {
            }
            i = str.length();
        }
        return new b(arrayList, i);
    }

    /* loaded from: classes.dex */
    public static class h {
        public final String a;
        public Matrix b = null;

        public h(String str) {
            this.a = str;
        }

        public Matrix a() {
            if (this.b == null) {
                b();
            }
            return this.b;
        }

        public final void b() {
            Prefix[] values;
            this.b = new Matrix();
            int length = this.a.length();
            int i = 0;
            while (i < length) {
                char charAt = this.a.charAt(i);
                if (charAt >= 'a' && charAt <= 'z') {
                    for (Prefix prefix : Prefix.values()) {
                        String name = prefix.name();
                        if (this.a.startsWith(name, i) && this.a.charAt(name.length() + i) == '(') {
                            int length2 = i + name.length();
                            b d = SVGParser.d(this.a.substring(length2 + 1));
                            i = length2 + d.a();
                            Matrix b = SVGParser.b(prefix, d);
                            if (b != null) {
                                this.b.preConcat(b);
                            }
                        }
                    }
                }
                i++;
            }
        }
    }

    /* renamed from: com.larvalabs.svgandroid.SVGParser$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] a;

        static {
            int[] iArr = new int[Prefix.values().length];
            a = iArr;
            try {
                iArr[Prefix.matrix.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                a[Prefix.translate.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                a[Prefix.scale.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                a[Prefix.skewX.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                a[Prefix.skewY.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                a[Prefix.rotate.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    public static Matrix b(Prefix prefix, b bVar) {
        float f2;
        float f3 = 0.0f;
        switch (AnonymousClass1.a[prefix.ordinal()]) {
            case 1:
                if (bVar.a.size() != 6) {
                    return null;
                }
                Matrix matrix = new Matrix();
                matrix.setValues(new float[]{((Float) bVar.a.get(0)).floatValue(), ((Float) bVar.a.get(2)).floatValue(), ((Float) bVar.a.get(4)).floatValue(), ((Float) bVar.a.get(1)).floatValue(), ((Float) bVar.a.get(3)).floatValue(), ((Float) bVar.a.get(5)).floatValue(), 0.0f, 0.0f, 1.0f});
                return matrix;
            case 2:
                if (bVar.a.size() <= 0) {
                    return null;
                }
                float floatValue = ((Float) bVar.a.get(0)).floatValue();
                if (bVar.a.size() > 1) {
                    f3 = ((Float) bVar.a.get(1)).floatValue();
                }
                Matrix matrix2 = new Matrix();
                matrix2.postTranslate(floatValue, f3);
                return matrix2;
            case 3:
                if (bVar.a.size() <= 0) {
                    return null;
                }
                float floatValue2 = ((Float) bVar.a.get(0)).floatValue();
                if (bVar.a.size() > 1) {
                    f3 = ((Float) bVar.a.get(1)).floatValue();
                }
                Matrix matrix3 = new Matrix();
                matrix3.postScale(floatValue2, f3);
                return matrix3;
            case 4:
                if (bVar.a.size() <= 0) {
                    return null;
                }
                float floatValue3 = ((Float) bVar.a.get(0)).floatValue();
                Matrix matrix4 = new Matrix();
                matrix4.postSkew((float) Math.tan(floatValue3), 0.0f);
                return matrix4;
            case 5:
                if (bVar.a.size() <= 0) {
                    return null;
                }
                float floatValue4 = ((Float) bVar.a.get(0)).floatValue();
                Matrix matrix5 = new Matrix();
                matrix5.postSkew(0.0f, (float) Math.tan(floatValue4));
                return matrix5;
            case 6:
                if (bVar.a.size() <= 0) {
                    return null;
                }
                float floatValue5 = ((Float) bVar.a.get(0)).floatValue();
                if (bVar.a.size() > 2) {
                    f3 = ((Float) bVar.a.get(1)).floatValue();
                    f2 = ((Float) bVar.a.get(2)).floatValue();
                } else {
                    f2 = 0.0f;
                }
                Matrix matrix6 = new Matrix();
                matrix6.postTranslate(f3, f2);
                matrix6.postRotate(floatValue5);
                matrix6.postTranslate(-f3, -f2);
                return matrix6;
            default:
                return null;
        }
    }

    public static Matrix e(String str) {
        return new h(str).a();
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x004a, code lost:
        if (r4 != 'L') goto L9;
     */
    /* JADX WARN: Removed duplicated region for block: B:28:0x005f  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0067  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x0075  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x008a  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x00c6  */
    /* JADX WARN: Removed duplicated region for block: B:43:0x00e1  */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00fb  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x0111  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x014c  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x018a  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x018e A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static android.graphics.Path f(java.lang.String r24) {
        /*
            Method dump skipped, instructions count: 496
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.larvalabs.svgandroid.SVGParser.f(java.lang.String):android.graphics.Path");
    }

    public static b d(String str, Attributes attributes) {
        int length = attributes.getLength();
        for (int i = 0; i < length; i++) {
            if (attributes.getLocalName(i).equals(str)) {
                return d(attributes.getValue(i));
            }
        }
        return null;
    }

    public static String e(String str, Attributes attributes) {
        int length = attributes.getLength();
        for (int i = 0; i < length; i++) {
            if (attributes.getLocalName(i).equals(str)) {
                return attributes.getValue(i);
            }
        }
        return null;
    }

    public static Float f(String str, Attributes attributes) {
        return b(str, attributes, null);
    }

    public static Float b(String str, Attributes attributes, Float f2) {
        String e2 = e(str, attributes);
        if (e2 == null) {
            return f2;
        }
        if (e2.endsWith("%")) {
            return Float.valueOf(Float.parseFloat(e2.substring(0, e2.length() - 1)) / 100.0f);
        }
        if (e2.endsWith("px")) {
            e2 = e2.substring(0, e2.length() - 2);
        }
        return Float.valueOf(Float.parseFloat(e2));
    }

    /* loaded from: classes.dex */
    public static class b {
        public ArrayList<Float> a;
        public int b;

        public b(ArrayList<Float> arrayList, int i) {
            this.a = arrayList;
            this.b = i;
        }

        public int a() {
            return this.b;
        }
    }

    /* loaded from: classes.dex */
    public static class a {
        public String a;
        public String b;
        public boolean c;
        public float d;
        public float e;
        public float f;
        public float g;
        public float h;
        public float i;
        public float j;
        public ArrayList<Float> k;
        public ArrayList<Integer> l;
        public Matrix m;

        public a() {
            this.k = new ArrayList<>();
            this.l = new ArrayList<>();
            this.m = null;
        }

        public /* synthetic */ a(AnonymousClass1 anonymousClass1) {
            this();
        }

        public a a(a aVar) {
            a aVar2 = new a();
            aVar2.a = aVar.a;
            aVar2.b = this.a;
            aVar2.c = aVar.c;
            aVar2.d = aVar.d;
            aVar2.f = aVar.f;
            aVar2.e = aVar.e;
            aVar2.g = aVar.g;
            aVar2.h = aVar.h;
            aVar2.i = aVar.i;
            aVar2.j = aVar.j;
            aVar2.k = this.k;
            aVar2.l = this.l;
            aVar2.m = this.m;
            Matrix matrix = aVar.m;
            if (matrix != null) {
                if (this.m == null) {
                    aVar2.m = matrix;
                } else {
                    Matrix matrix2 = new Matrix(this.m);
                    matrix2.preConcat(aVar.m);
                    aVar2.m = matrix2;
                }
            }
            return aVar2;
        }
    }

    /* loaded from: classes.dex */
    public static class g {
        public HashMap<String, String> a;

        public /* synthetic */ g(String str, AnonymousClass1 anonymousClass1) {
            this(str);
        }

        public g(String str) {
            this.a = new HashMap<>();
            for (String str2 : str.split(";")) {
                String[] split = str2.split(":");
                if (split.length == 2) {
                    this.a.put(split[0], split[1]);
                }
            }
        }

        public String a(String str) {
            return this.a.get(str);
        }
    }

    /* loaded from: classes.dex */
    public static class d {
        public g a;
        public Attributes b;
        public Set<String> c;

        public /* synthetic */ d(Attributes attributes, AnonymousClass1 anonymousClass1) {
            this(attributes);
        }

        public d(Attributes attributes) {
            this.a = null;
            this.c = null;
            this.b = attributes;
            String e = SVGParser.e("style", attributes);
            if (e != null) {
                this.a = new g(e, null);
            }
        }

        public void a(String str) {
            if (this.c == null) {
                this.c = new HashSet();
            }
            this.c.add(str);
        }

        public String b(String str) {
            Set<String> set = this.c;
            String str2 = null;
            if (set == null || !set.contains(str)) {
                g gVar = this.a;
                if (gVar != null) {
                    str2 = gVar.a(str);
                }
                return str2 == null ? SVGParser.e(str, this.b) : str2;
            }
            return null;
        }

        public String c(String str) {
            return b(str);
        }

        public Integer d(String str) {
            String b = b(str);
            if (b != null && b.startsWith("#")) {
                try {
                    return Integer.valueOf(Integer.parseInt(b.substring(1), 16));
                } catch (NumberFormatException unused) {
                }
            }
            return null;
        }

        public Float e(String str) {
            String b = b(str);
            if (b == null) {
                return null;
            }
            try {
                return Float.valueOf(Float.parseFloat(b));
            } catch (NumberFormatException unused) {
                return null;
            }
        }
    }

    /* loaded from: classes.dex */
    public static class f {
        public int a;
        public int b;
        public Shader c;
        public float d;
        public Paint.Cap e;
        public Paint.Join f;
        public boolean g;
        public boolean h;
        public Path.FillType i;

        public f() {
            this.a = -16777216;
            this.b = -16777216;
            this.c = null;
            this.d = 1.0f;
            this.e = Paint.Cap.BUTT;
            this.f = Paint.Join.MITER;
            this.g = false;
            this.h = false;
        }

        public f(f fVar) {
            this.a = -16777216;
            this.b = -16777216;
            this.c = null;
            this.d = 1.0f;
            this.e = Paint.Cap.BUTT;
            this.f = Paint.Join.MITER;
            this.g = false;
            this.h = false;
            this.a = fVar.a;
            this.b = fVar.b;
            this.c = fVar.c;
            this.d = fVar.d;
            this.e = fVar.e;
            this.f = fVar.f;
            this.g = fVar.g;
            this.h = fVar.h;
        }

        public void a(int i) {
            this.a = i;
            this.c = null;
            this.h = true;
        }

        public void b(int i) {
            this.b = i;
            this.g = true;
        }

        public boolean a(Paint paint) {
            if (!this.h) {
                return false;
            }
            paint.setStyle(Paint.Style.FILL);
            paint.setShader(this.c);
            paint.setColor(this.a);
            return true;
        }

        public boolean b(Paint paint) {
            if (!this.g) {
                return false;
            }
            paint.setStyle(Paint.Style.STROKE);
            paint.setShader(null);
            paint.setColor(this.b);
            paint.setStrokeWidth(this.d);
            paint.setStrokeCap(this.e);
            paint.setStrokeJoin(this.f);
            return true;
        }

        public void a(float f) {
            this.d = f;
        }

        public void a(Shader shader) {
            this.c = shader;
            this.a = -16777216;
            this.h = true;
        }

        public void a(Paint.Cap cap) {
            this.e = cap;
        }

        public void a(Paint.Join join) {
            this.f = join;
        }

        public void a(Path.FillType fillType) {
            this.i = fillType;
        }

        public Path.FillType a() {
            Path.FillType fillType = this.i;
            return fillType == null ? Path.FillType.WINDING : fillType;
        }
    }

    /* loaded from: classes.dex */
    public static class e extends DefaultHandler {
        public Picture a;
        public Canvas b;
        public Paint c;
        public RectF d;
        public RectF e;
        public RectF f;
        public Deque<f> g;
        public c h;
        public boolean i;
        public HashMap<String, Shader> j;
        public HashMap<String, a> k;
        public a l;
        public boolean m;
        public int n;
        public boolean o;

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void characters(char[] cArr, int i, int i2) {
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void endDocument() throws SAXException {
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startDocument() throws SAXException {
        }

        public /* synthetic */ e(Picture picture, c cVar, AnonymousClass1 anonymousClass1) {
            this(picture, cVar);
        }

        public e(Picture picture, c cVar) {
            this.d = new RectF();
            this.e = null;
            this.f = new RectF(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);
            this.g = new ArrayDeque();
            this.i = false;
            this.j = new HashMap<>();
            this.k = new HashMap<>();
            this.l = null;
            this.m = false;
            this.n = 0;
            this.o = false;
            Log.d("SVGAndroid", "SVGHandler Constructed");
            this.a = picture;
            Paint paint = new Paint();
            this.c = paint;
            paint.setAntiAlias(true);
            f fVar = new f();
            fVar.h = true;
            this.g.push(fVar);
            this.h = new c(cVar);
        }

        public c a() {
            return this.h;
        }

        public final boolean a(d dVar, HashMap<String, Shader> hashMap, f fVar) {
            Log.d("SVGAndroid", "doFill : IN");
            if ("none".equals(dVar.c("display"))) {
                return false;
            }
            String c = dVar.c("fill-rule");
            if (c != null) {
                if ("nonzero".equals(c)) {
                    fVar.a(Path.FillType.WINDING);
                } else if ("evenodd".equals(c)) {
                    fVar.a(Path.FillType.EVEN_ODD);
                }
            }
            if (this.h.a) {
                fVar.a(this.h.b);
                return true;
            }
            String c2 = dVar.c("fill");
            if (c2 != null && c2.startsWith("url(#")) {
                Log.d("SVGAndroid", "doFill :   gradient>>" + c2);
                Shader shader = hashMap.get(c2.substring(5, c2.length() - 1));
                if (shader == null) {
                    return false;
                }
                fVar.a(shader);
                return true;
            }
            if (c2 != null && c2.equals("none")) {
                Log.d("SVGAndroid", "doFill :   none");
                fVar.h = false;
            } else {
                Integer d = dVar.d("fill");
                if (d != null) {
                    Log.d("SVGAndroid", "doFill :   c=" + com.nexstreaming.app.common.util.c.a(d.intValue()));
                    fVar.a(a(dVar, d, true));
                    return true;
                } else if (dVar.c("fill") == null && dVar.c("stroke") == null) {
                    Log.d("SVGAndroid", "doFill :   no fill & no stroke");
                    return true;
                }
            }
            Log.d("SVGAndroid", "doFill :   no fill");
            return false;
        }

        public final boolean a(d dVar, f fVar) {
            Log.d("SVGAndroid", "doStroke : IN");
            if ("none".equals(dVar.c("display"))) {
                return false;
            }
            if (this.h.c) {
                fVar.b(this.h.d);
                fVar.a(this.h.e);
                return true;
            }
            String c = dVar.c("stroke");
            if (c != null && c.equals("none")) {
                fVar.g = false;
                return true;
            }
            Integer d = dVar.d("stroke");
            if (d == null) {
                return false;
            }
            fVar.b(a(dVar, d, false));
            Float e = dVar.e("stroke-width");
            if (e != null) {
                fVar.a(e.floatValue());
            }
            String c2 = dVar.c("stroke-linecap");
            if ("round".equals(c2)) {
                fVar.a(Paint.Cap.ROUND);
            } else if ("square".equals(c2)) {
                fVar.a(Paint.Cap.SQUARE);
            } else if ("butt".equals(c2)) {
                fVar.a(Paint.Cap.BUTT);
            }
            String c3 = dVar.c("stroke-linejoin");
            if ("miter".equals(c3)) {
                fVar.a(Paint.Join.MITER);
            } else if ("round".equals(c3)) {
                fVar.a(Paint.Join.ROUND);
            } else if ("bevel".equals(c3)) {
                fVar.a(Paint.Join.BEVEL);
            }
            return true;
        }

        public final a a(boolean z, Attributes attributes) {
            Log.d("SVGAndroid", "doGradient : IN");
            a aVar = new a(null);
            aVar.a = SVGParser.e("id", attributes);
            aVar.c = z;
            Float valueOf = Float.valueOf(0.0f);
            if (z) {
                aVar.d = SVGParser.b("x1", attributes, valueOf).floatValue();
                aVar.f = SVGParser.b("x2", attributes, valueOf).floatValue();
                aVar.e = SVGParser.b("y1", attributes, valueOf).floatValue();
                aVar.g = SVGParser.b("y2", attributes, valueOf).floatValue();
            } else {
                aVar.h = SVGParser.b("cx", attributes, valueOf).floatValue();
                aVar.i = SVGParser.b("cy", attributes, valueOf).floatValue();
                aVar.j = SVGParser.b("r", attributes, valueOf).floatValue();
            }
            String e = SVGParser.e("gradientTransform", attributes);
            if (e != null) {
                aVar.m = SVGParser.e(e);
            }
            String e2 = SVGParser.e("href", attributes);
            if (e2 != null) {
                if (e2.startsWith("#")) {
                    e2 = e2.substring(1);
                }
                aVar.b = e2;
            }
            return aVar;
        }

        public final int a(d dVar, Integer num, boolean z) {
            Log.d("SVGAndroid", "getColor : fillMode=" + z + " color=" + com.nexstreaming.app.common.util.c.a(num.intValue()));
            if (z && this.h.a) {
                Log.d("SVGAndroid", "getColor : FILL OVERRIDE");
                return this.h.b;
            }
            int intValue = (num.intValue() & 16777215) | (-16777216);
            if (this.h.i != null) {
                this.h.i.add(Integer.valueOf(intValue));
            }
            if (this.h.f == intValue) {
                Log.d("SVGAndroid", "getColor : REPLACE COLOR");
                intValue = this.h.g;
            }
            if (this.h.h != null) {
                Log.d("SVGAndroid", "getColor : MAP COLOR");
                intValue = this.h.a(intValue);
            }
            Float e = dVar.e("opacity");
            if (e == null) {
                e = dVar.e(z ? "fill-opacity" : "stroke-opacity");
            }
            if (e == null) {
                return intValue;
            }
            return ((((int) (e.floatValue() * 255.0f)) << 24) & (-16777216)) | (intValue & 16777215);
        }

        public final void a(float f, float f2) {
            RectF rectF = this.f;
            if (f < rectF.left) {
                rectF.left = f;
            }
            if (f > rectF.right) {
                rectF.right = f;
            }
            if (f2 < rectF.top) {
                rectF.top = f2;
            }
            if (f2 > rectF.bottom) {
                rectF.bottom = f2;
            }
        }

        public final void a(float f, float f2, float f3, float f4) {
            a(f, f2);
            a(f + f3, f2 + f4);
        }

        public final void a(Path path) {
            path.computeBounds(this.d, false);
            RectF rectF = this.d;
            a(rectF.left, rectF.top);
            RectF rectF2 = this.d;
            a(rectF2.right, rectF2.bottom);
        }

        public final void a(Attributes attributes) {
            String e = SVGParser.e("transform", attributes);
            boolean z = e != null;
            this.i = z;
            if (z) {
                Matrix e2 = SVGParser.e(e);
                this.b.save();
                this.b.concat(e2);
            }
        }

        public final void b() {
            if (this.i) {
                this.b.restore();
            }
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void startElement(String str, String str2, String str3, Attributes attributes) throws SAXException {
            int i;
            int i2;
            int ceil;
            int i3;
            float f;
            Log.d("SVGAndroid", "startElement : " + str2 + " atts=" + attributes.toString());
            boolean z = this.o;
            float f2 = 0.0f;
            Float valueOf = Float.valueOf(0.0f);
            if (z) {
                if (!str2.equals("rect")) {
                    return;
                }
                Float f3 = SVGParser.f("x", attributes);
                if (f3 == null) {
                    f3 = valueOf;
                }
                Float f4 = SVGParser.f("y", attributes);
                if (f4 != null) {
                    valueOf = f4;
                }
                Float f5 = SVGParser.f(nexExportFormat.TAG_FORMAT_WIDTH, attributes);
                SVGParser.f(nexExportFormat.TAG_FORMAT_HEIGHT, attributes);
                this.e = new RectF(f3.floatValue(), valueOf.floatValue(), f3.floatValue() + f5.floatValue(), valueOf.floatValue() + f5.floatValue());
            } else if (str2.equals("svg")) {
                Float f6 = SVGParser.f(nexExportFormat.TAG_FORMAT_WIDTH, attributes);
                Float f7 = SVGParser.f(nexExportFormat.TAG_FORMAT_HEIGHT, attributes);
                if (f6 == null || f7 == null) {
                    String e = SVGParser.e("viewBox", attributes);
                    if (e != null) {
                        b d = SVGParser.d(e);
                        if (d.a.size() >= 4) {
                            f2 = ((Float) d.a.get(0)).floatValue();
                            float floatValue = ((Float) d.a.get(1)).floatValue();
                            int ceil2 = (int) Math.ceil(((Float) d.a.get(2)).floatValue());
                            Log.d("SVGAndroid", "Used viewBox");
                            ceil = (int) Math.ceil(((Float) d.a.get(3)).floatValue());
                            i3 = ceil2;
                            f = floatValue;
                            Log.d("SVGAndroid", "BEGIN REC: dx,dy=" + f2 + "," + f + "  w,h=" + i3 + "," + ceil);
                            Canvas beginRecording = this.a.beginRecording(i3, ceil);
                            this.b = beginRecording;
                            beginRecording.translate(f2, f);
                        }
                        Log.e("SVGHandler", "Malformed SVG bounds! Defaulting to 100x100");
                    } else {
                        Log.e("SVGHandler", "Missing SVG bounds! Defaulting to 100x100");
                    }
                    i3 = 100;
                    ceil = 100;
                } else {
                    i3 = (int) Math.ceil(f6.floatValue());
                    ceil = (int) Math.ceil(f7.floatValue());
                    Log.d("SVGAndroid", "Used width, height");
                }
                f = 0.0f;
                Log.d("SVGAndroid", "BEGIN REC: dx,dy=" + f2 + "," + f + "  w,h=" + i3 + "," + ceil);
                Canvas beginRecording2 = this.a.beginRecording(i3, ceil);
                this.b = beginRecording2;
                beginRecording2.translate(f2, f);
            } else if (!str2.equals("defs")) {
                if (str2.equals("linearGradient")) {
                    this.l = a(true, attributes);
                } else if (str2.equals("radialGradient")) {
                    this.l = a(false, attributes);
                } else {
                    int i4 = 255;
                    if (str2.equals("stop")) {
                        if (this.l == null) {
                            return;
                        }
                        float floatValue2 = SVGParser.f("offset", attributes).floatValue();
                        SVGParser.e("style", attributes);
                        d dVar = new d(attributes, null);
                        String b = dVar.b("stop-color");
                        if (b == null) {
                            i = -16777216;
                        } else if (b.startsWith("#")) {
                            i = Integer.parseInt(b.substring(1), 16);
                        } else {
                            i = Integer.parseInt(b, 16);
                        }
                        String b2 = dVar.b("stop-opacity");
                        if (b2 != null) {
                            int round = Math.round(Float.parseFloat(b2) * 255.0f);
                            if (round <= 255) {
                                i4 = round;
                            }
                            i2 = i | (i4 << 24);
                        } else {
                            i2 = i | (-16777216);
                        }
                        this.l.k.add(Float.valueOf(floatValue2));
                        if (this.h.i != null) {
                            this.h.i.add(Integer.valueOf(i2));
                        }
                        this.l.l.add(Integer.valueOf(this.h.a(i2)));
                    } else if (str2.equals("g")) {
                        if ("bounds".equalsIgnoreCase(SVGParser.e("id", attributes))) {
                            this.o = true;
                        }
                        if (this.m) {
                            this.n++;
                        }
                        f fVar = new f(this.g.peek());
                        d dVar2 = new d(attributes, null);
                        Float e2 = dVar2.e("opacity");
                        int max = e2 == null ? 255 : Math.max(0, Math.min(255, (int) (e2.floatValue() * 255.0f)));
                        if (max < 255 && max > 0) {
                            this.b.saveLayerAlpha(null, max, 31);
                            dVar2.a("opacity");
                        } else {
                            this.b.save();
                        }
                        a(dVar2, this.j, fVar);
                        a(dVar2, fVar);
                        this.g.push(fVar);
                        if ((max > 0 && !"none".equals(SVGParser.e("display", attributes))) || this.m) {
                            return;
                        }
                        this.m = true;
                        this.n = 1;
                    } else if (!this.m && str2.equals("rect")) {
                        Float f8 = SVGParser.f("x", attributes);
                        if (f8 == null) {
                            f8 = valueOf;
                        }
                        Float f9 = SVGParser.f("y", attributes);
                        if (f9 == null) {
                            f9 = valueOf;
                        }
                        Float f10 = SVGParser.f("rx", attributes);
                        Float f11 = SVGParser.f("ry", attributes);
                        if (f10 == null && f11 == null) {
                            f11 = valueOf;
                        } else {
                            if (f10 != null) {
                                if (f11 == null) {
                                    f11 = f10;
                                } else {
                                    valueOf = f10;
                                }
                            }
                            valueOf = f11;
                        }
                        Float f12 = SVGParser.f(nexExportFormat.TAG_FORMAT_WIDTH, attributes);
                        Float f13 = SVGParser.f(nexExportFormat.TAG_FORMAT_HEIGHT, attributes);
                        a(attributes);
                        d dVar3 = new d(attributes, null);
                        f fVar2 = new f(this.g.peek());
                        a(dVar3, this.j, fVar2);
                        a(dVar3, fVar2);
                        if (fVar2.a(this.c)) {
                            a(f8.floatValue(), f9.floatValue(), f12.floatValue(), f13.floatValue());
                            if (valueOf.floatValue() > 0.0f && f11.floatValue() > 0.0f) {
                                this.b.drawRoundRect(new RectF(f8.floatValue(), f9.floatValue(), f8.floatValue() + f12.floatValue(), f9.floatValue() + f13.floatValue()), valueOf.floatValue(), f11.floatValue(), this.c);
                            } else {
                                this.b.drawRect(f8.floatValue(), f9.floatValue(), f8.floatValue() + f12.floatValue(), f9.floatValue() + f13.floatValue(), this.c);
                            }
                        }
                        if (fVar2.b(this.c)) {
                            if (valueOf.floatValue() > 0.0f && f11.floatValue() > 0.0f) {
                                this.b.drawRoundRect(new RectF(f8.floatValue(), f9.floatValue(), f8.floatValue() + f12.floatValue(), f9.floatValue() + f13.floatValue()), valueOf.floatValue(), f11.floatValue(), this.c);
                            } else {
                                this.b.drawRect(f8.floatValue(), f9.floatValue(), f8.floatValue() + f12.floatValue(), f9.floatValue() + f13.floatValue(), this.c);
                            }
                        }
                        b();
                    } else if (!this.m && str2.equals("line")) {
                        Float f14 = SVGParser.f("x1", attributes);
                        Float f15 = SVGParser.f("x2", attributes);
                        Float f16 = SVGParser.f("y1", attributes);
                        Float f17 = SVGParser.f("y2", attributes);
                        d dVar4 = new d(attributes, null);
                        f fVar3 = new f(this.g.peek());
                        a(dVar4, fVar3);
                        if (!fVar3.b(this.c)) {
                            return;
                        }
                        a(attributes);
                        a(f14.floatValue(), f16.floatValue());
                        a(f15.floatValue(), f17.floatValue());
                        this.b.drawLine(f14.floatValue(), f16.floatValue(), f15.floatValue(), f17.floatValue(), this.c);
                        b();
                    } else if (!this.m && str2.equals("circle")) {
                        Float f18 = SVGParser.f("cx", attributes);
                        Float f19 = SVGParser.f("cy", attributes);
                        Float f20 = SVGParser.f("r", attributes);
                        if (f18 == null || f19 == null || f20 == null) {
                            return;
                        }
                        a(attributes);
                        d dVar5 = new d(attributes, null);
                        f fVar4 = new f(this.g.peek());
                        a(dVar5, this.j, fVar4);
                        a(dVar5, fVar4);
                        if (fVar4.a(this.c)) {
                            a(f18.floatValue() - f20.floatValue(), f19.floatValue() - f20.floatValue());
                            a(f18.floatValue() + f20.floatValue(), f19.floatValue() + f20.floatValue());
                            this.b.drawCircle(f18.floatValue(), f19.floatValue(), f20.floatValue(), this.c);
                        }
                        if (fVar4.b(this.c)) {
                            this.b.drawCircle(f18.floatValue(), f19.floatValue(), f20.floatValue(), this.c);
                        }
                        b();
                    } else if (!this.m && str2.equals("ellipse")) {
                        Float f21 = SVGParser.f("cx", attributes);
                        Float f22 = SVGParser.f("cy", attributes);
                        Float f23 = SVGParser.f("rx", attributes);
                        Float f24 = SVGParser.f("ry", attributes);
                        if (f21 == null || f22 == null || f23 == null || f24 == null) {
                            return;
                        }
                        a(attributes);
                        d dVar6 = new d(attributes, null);
                        f fVar5 = new f(this.g.peek());
                        a(dVar6, this.j, fVar5);
                        a(dVar6, fVar5);
                        this.d.set(f21.floatValue() - f23.floatValue(), f22.floatValue() - f24.floatValue(), f21.floatValue() + f23.floatValue(), f22.floatValue() + f24.floatValue());
                        if (fVar5.a(this.c)) {
                            a(f21.floatValue() - f23.floatValue(), f22.floatValue() - f24.floatValue());
                            a(f21.floatValue() + f23.floatValue(), f22.floatValue() + f24.floatValue());
                            this.b.drawOval(this.d, this.c);
                        }
                        if (fVar5.b(this.c)) {
                            this.b.drawOval(this.d, this.c);
                        }
                        b();
                    } else if (!this.m && (str2.equals("polygon") || str2.equals("polyline"))) {
                        b d2 = SVGParser.d("points", attributes);
                        if (d2 == null) {
                            return;
                        }
                        Path path = new Path();
                        ArrayList arrayList = d2.a;
                        if (arrayList.size() <= 1) {
                            return;
                        }
                        a(attributes);
                        d dVar7 = new d(attributes, null);
                        f fVar6 = new f(this.g.peek());
                        a(dVar7, this.j, fVar6);
                        a(dVar7, fVar6);
                        path.moveTo(((Float) arrayList.get(0)).floatValue(), ((Float) arrayList.get(1)).floatValue());
                        for (int i5 = 2; i5 < arrayList.size(); i5 += 2) {
                            path.lineTo(((Float) arrayList.get(i5)).floatValue(), ((Float) arrayList.get(i5 + 1)).floatValue());
                        }
                        if (str2.equals("polygon")) {
                            path.close();
                        }
                        if (fVar6.a(this.c)) {
                            path.setFillType(fVar6.a());
                            a(path);
                            this.b.drawPath(path, this.c);
                        }
                        if (fVar6.b(this.c)) {
                            this.b.drawPath(path, this.c);
                        }
                        b();
                    } else if (!this.m && str2.equals(nexExportFormat.TAG_FORMAT_PATH)) {
                        Path f25 = SVGParser.f(SVGParser.e("d", attributes));
                        a(attributes);
                        d dVar8 = new d(attributes, null);
                        f fVar7 = new f(this.g.peek());
                        a(dVar8, this.j, fVar7);
                        a(dVar8, fVar7);
                        if (fVar7.a(this.c)) {
                            f25.setFillType(fVar7.a());
                            a(f25);
                            this.b.drawPath(f25, this.c);
                        }
                        if (fVar7.b(this.c)) {
                            this.b.drawPath(f25, this.c);
                        }
                        b();
                    } else if (!this.m) {
                        Log.w("SVGAndroid", "UNRECOGNIZED SVG COMMAND: " + str2);
                    }
                }
            }
        }

        @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
        public void endElement(String str, String str2, String str3) throws SAXException {
            a aVar;
            a aVar2;
            a aVar3;
            if (str2.equals("svg")) {
                this.a.endRecording();
                return;
            }
            int i = 0;
            if (str2.equals("linearGradient")) {
                a aVar4 = this.l;
                if (aVar4.a == null) {
                    return;
                }
                String str4 = aVar4.b;
                if (str4 != null && (aVar3 = this.k.get(str4)) != null) {
                    this.l = aVar3.a(this.l);
                }
                int size = this.l.l.size();
                int[] iArr = new int[size];
                for (int i2 = 0; i2 < size; i2++) {
                    iArr[i2] = this.l.l.get(i2).intValue();
                }
                int size2 = this.l.k.size();
                float[] fArr = new float[size2];
                while (i < size2) {
                    fArr[i] = this.l.k.get(i).floatValue();
                    i++;
                }
                if (size == 0) {
                    Log.d("BAD", "BAD");
                }
                a aVar5 = this.l;
                LinearGradient linearGradient = new LinearGradient(aVar5.d, aVar5.e, aVar5.f, aVar5.g, iArr, fArr, Shader.TileMode.CLAMP);
                Matrix matrix = this.l.m;
                if (matrix != null) {
                    linearGradient.setLocalMatrix(matrix);
                }
                this.j.put(this.l.a, linearGradient);
                HashMap<String, a> hashMap = this.k;
                a aVar6 = this.l;
                hashMap.put(aVar6.a, aVar6);
            } else if (str2.equals("radialGradient")) {
                a aVar7 = this.l;
                if (aVar7.a == null) {
                    return;
                }
                String str5 = aVar7.b;
                if (str5 != null && (aVar2 = this.k.get(str5)) != null) {
                    this.l = aVar2.a(this.l);
                }
                int size3 = this.l.l.size();
                int[] iArr2 = new int[size3];
                for (int i3 = 0; i3 < size3; i3++) {
                    iArr2[i3] = this.l.l.get(i3).intValue();
                }
                int size4 = this.l.k.size();
                float[] fArr2 = new float[size4];
                while (i < size4) {
                    fArr2[i] = this.l.k.get(i).floatValue();
                    i++;
                }
                String str6 = this.l.b;
                if (str6 != null && (aVar = this.k.get(str6)) != null) {
                    this.l = aVar.a(this.l);
                }
                a aVar8 = this.l;
                RadialGradient radialGradient = new RadialGradient(aVar8.h, aVar8.i, aVar8.j, iArr2, fArr2, Shader.TileMode.CLAMP);
                Matrix matrix2 = this.l.m;
                if (matrix2 != null) {
                    radialGradient.setLocalMatrix(matrix2);
                }
                this.j.put(this.l.a, radialGradient);
                HashMap<String, a> hashMap2 = this.k;
                a aVar9 = this.l;
                hashMap2.put(aVar9.a, aVar9);
            } else if (str2.equals("g")) {
                this.g.pop();
                if (this.o) {
                    this.o = false;
                }
                this.b.restore();
                if (!this.m) {
                    return;
                }
                int i4 = this.n - 1;
                this.n = i4;
                if (i4 != 0) {
                    return;
                }
                this.m = false;
            }
        }
    }
}
