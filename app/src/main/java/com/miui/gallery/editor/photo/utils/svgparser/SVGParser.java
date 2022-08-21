package com.miui.gallery.editor.photo.utils.svgparser;

import android.graphics.Matrix;
import android.util.Log;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.joran.action.Action;
import ch.qos.logback.core.util.FileSize;
import com.baidu.platform.comapi.UIMsg;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.meicam.sdk.NvsMediaFileConvertor;
import com.miui.gallery.assistant.jni.filter.BaiduSceneResult;
import com.miui.gallery.editor.photo.utils.svgparser.CSSParser;
import com.miui.gallery.editor.photo.utils.svgparser.PreserveAspectRatio;
import com.miui.gallery.editor.photo.utils.svgparser.SVG;
import com.nexstreaming.nexeditorsdk.nexChecker;
import com.nexstreaming.nexeditorsdk.nexEngine;
import com.xiaomi.miai.api.StatusCode;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;
import miuix.hybrid.Response;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.ext.DefaultHandler2;

/* loaded from: classes2.dex */
public class SVGParser extends DefaultHandler2 {
    public int ignoreDepth;
    public SVG svgDocument = null;
    public SVG.SvgContainer currentElement = null;
    public boolean ignoring = false;
    public boolean inMetadataElement = false;
    public StringBuilder metadataElementContents = null;
    public boolean inStyleElement = false;
    public StringBuilder styleElementContents = null;

    public final void debug(String str, Object... objArr) {
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endDocument() {
    }

    /* loaded from: classes2.dex */
    public enum SVGElem {
        svg,
        a,
        circle,
        clipPath,
        defs,
        desc,
        ellipse,
        g,
        image,
        line,
        linearGradient,
        marker,
        mask,
        path,
        pattern,
        polygon,
        polyline,
        radialGradient,
        rect,
        solidColor,
        stop,
        style,
        SWITCH,
        symbol,
        text,
        textPath,
        title,
        tref,
        tspan,
        use,
        view,
        UNSUPPORTED;
        
        public static final Map<String, SVGElem> cache = new HashMap();

        public static SVGElem fromString(String str) {
            Map<String, SVGElem> map = cache;
            SVGElem sVGElem = map.get(str);
            if (sVGElem != null) {
                return sVGElem;
            }
            if (str.equals("switch")) {
                SVGElem sVGElem2 = SWITCH;
                map.put(str, sVGElem2);
                return sVGElem2;
            }
            try {
                SVGElem valueOf = valueOf(str);
                if (valueOf != SWITCH) {
                    map.put(str, valueOf);
                    return valueOf;
                }
            } catch (IllegalArgumentException unused) {
            }
            Map<String, SVGElem> map2 = cache;
            SVGElem sVGElem3 = UNSUPPORTED;
            map2.put(str, sVGElem3);
            return sVGElem3;
        }
    }

    /* loaded from: classes2.dex */
    public enum SVGAttr {
        CLASS,
        clip,
        clip_path,
        clipPathUnits,
        clip_rule,
        color,
        cx,
        cy,
        direction,
        dx,
        dy,
        fx,
        fy,
        d,
        display,
        fill,
        fill_rule,
        fill_opacity,
        font,
        font_family,
        font_size,
        font_weight,
        font_style,
        gradientTransform,
        gradientUnits,
        height,
        href,
        id,
        marker,
        marker_start,
        marker_mid,
        marker_end,
        markerHeight,
        markerUnits,
        markerWidth,
        mask,
        maskContentUnits,
        maskUnits,
        media,
        offset,
        opacity,
        orient,
        overflow,
        pathLength,
        patternContentUnits,
        patternTransform,
        patternUnits,
        points,
        preserveAspectRatio,
        r,
        refX,
        refY,
        requiredFeatures,
        requiredExtensions,
        requiredFormats,
        requiredFonts,
        rx,
        ry,
        solid_color,
        solid_opacity,
        spreadMethod,
        startOffset,
        stop_color,
        stop_opacity,
        stroke,
        stroke_dasharray,
        stroke_dashoffset,
        stroke_linecap,
        stroke_linejoin,
        stroke_miterlimit,
        stroke_opacity,
        stroke_width,
        style,
        systemLanguage,
        text_anchor,
        text_decoration,
        transform,
        type,
        vector_effect,
        version,
        viewBox,
        width,
        x,
        y,
        x1,
        y1,
        x2,
        y2,
        viewport_fill,
        viewport_fill_opacity,
        visibility,
        UNSUPPORTED;
        
        public static final Map<String, SVGAttr> cache = new HashMap();

        public static SVGAttr fromString(String str) {
            Map<String, SVGAttr> map = cache;
            SVGAttr sVGAttr = map.get(str);
            if (sVGAttr != null) {
                return sVGAttr;
            }
            if (str.equals(Action.CLASS_ATTRIBUTE)) {
                SVGAttr sVGAttr2 = CLASS;
                map.put(str, sVGAttr2);
                return sVGAttr2;
            } else if (str.indexOf(95) != -1) {
                SVGAttr sVGAttr3 = UNSUPPORTED;
                map.put(str, sVGAttr3);
                return sVGAttr3;
            } else {
                try {
                    SVGAttr valueOf = valueOf(str.replace(CoreConstants.DASH_CHAR, '_'));
                    if (valueOf != CLASS) {
                        map.put(str, valueOf);
                        return valueOf;
                    }
                } catch (IllegalArgumentException unused) {
                }
                Map<String, SVGAttr> map2 = cache;
                SVGAttr sVGAttr4 = UNSUPPORTED;
                map2.put(str, sVGAttr4);
                return sVGAttr4;
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class ColourKeywords {
        public static final Map<String, Integer> colourKeywords;

        static {
            HashMap hashMap = new HashMap(47);
            colourKeywords = hashMap;
            hashMap.put("aliceblue", 15792383);
            hashMap.put("antiquewhite", 16444375);
            Integer valueOf = Integer.valueOf((int) NvsMediaFileConvertor.CONVERTOR_ERROR_UNKNOWN);
            hashMap.put("aqua", valueOf);
            hashMap.put("aquamarine", 8388564);
            hashMap.put("azure", 15794175);
            hashMap.put("beige", 16119260);
            hashMap.put("bisque", 16770244);
            hashMap.put("black", 0);
            hashMap.put("blanchedalmond", 16772045);
            hashMap.put("blue", 255);
            hashMap.put("blueviolet", 9055202);
            hashMap.put("brown", 10824234);
            hashMap.put("burlywood", 14596231);
            hashMap.put("cadetblue", 6266528);
            hashMap.put("chartreuse", 8388352);
            hashMap.put("chocolate", 13789470);
            hashMap.put("coral", 16744272);
            hashMap.put("cornflowerblue", 6591981);
            hashMap.put("cornsilk", 16775388);
            hashMap.put("crimson", 14423100);
            hashMap.put("cyan", valueOf);
            hashMap.put("darkblue", Integer.valueOf((int) BaiduSceneResult.FASHION_OTHER));
            hashMap.put("darkcyan", 35723);
            hashMap.put("darkgoldenrod", 12092939);
            hashMap.put("darkgray", 11119017);
            hashMap.put("darkgreen", 25600);
            hashMap.put("darkgrey", 11119017);
            hashMap.put("darkkhaki", 12433259);
            hashMap.put("darkmagenta", 9109643);
            hashMap.put("darkolivegreen", 5597999);
            hashMap.put("darkorange", 16747520);
            hashMap.put("darkorchid", 10040012);
            hashMap.put("darkred", 9109504);
            hashMap.put("darksalmon", 15308410);
            hashMap.put("darkseagreen", 9419919);
            hashMap.put("darkslateblue", 4734347);
            hashMap.put("darkslategray", 3100495);
            hashMap.put("darkslategrey", 3100495);
            hashMap.put("darkturquoise", 52945);
            hashMap.put("darkviolet", 9699539);
            hashMap.put("deeppink", 16716947);
            hashMap.put("deepskyblue", 49151);
            hashMap.put("dimgray", 6908265);
            hashMap.put("dimgrey", 6908265);
            hashMap.put("dodgerblue", 2003199);
            hashMap.put("firebrick", 11674146);
            hashMap.put("floralwhite", 16775920);
            hashMap.put("forestgreen", 2263842);
            hashMap.put("fuchsia", 16711935);
            hashMap.put("gainsboro", 14474460);
            hashMap.put("ghostwhite", 16316671);
            hashMap.put("gold", 16766720);
            hashMap.put("goldenrod", 14329120);
            hashMap.put("gray", 8421504);
            hashMap.put("green", 32768);
            hashMap.put("greenyellow", 11403055);
            hashMap.put("grey", 8421504);
            hashMap.put("honeydew", 15794160);
            hashMap.put("hotpink", 16738740);
            hashMap.put("indianred", 13458524);
            hashMap.put("indigo", 4915330);
            hashMap.put("ivory", 16777200);
            hashMap.put("khaki", 15787660);
            hashMap.put("lavender", 15132410);
            hashMap.put("lavenderblush", 16773365);
            hashMap.put("lawngreen", 8190976);
            hashMap.put("lemonchiffon", 16775885);
            hashMap.put("lightblue", 11393254);
            hashMap.put("lightcoral", 15761536);
            hashMap.put("lightcyan", 14745599);
            hashMap.put("lightgoldenrodyellow", 16448210);
            hashMap.put("lightgray", 13882323);
            hashMap.put("lightgreen", 9498256);
            hashMap.put("lightgrey", 13882323);
            hashMap.put("lightpink", 16758465);
            hashMap.put("lightsalmon", 16752762);
            hashMap.put("lightseagreen", 2142890);
            hashMap.put("lightskyblue", 8900346);
            hashMap.put("lightslategray", 7833753);
            hashMap.put("lightslategrey", 7833753);
            hashMap.put("lightsteelblue", 11584734);
            hashMap.put("lightyellow", 16777184);
            hashMap.put("lime", 65280);
            hashMap.put("limegreen", 3329330);
            hashMap.put("linen", 16445670);
            hashMap.put("magenta", 16711935);
            hashMap.put("maroon", Integer.valueOf((int) nexEngine.ExportHEVCHighTierLevel61));
            hashMap.put("mediumaquamarine", 6737322);
            hashMap.put("mediumblue", Integer.valueOf((int) Response.CODE_ACTION_ERROR));
            hashMap.put("mediumorchid", 12211667);
            hashMap.put("mediumpurple", 9662683);
            hashMap.put("mediumseagreen", 3978097);
            hashMap.put("mediumslateblue", 8087790);
            hashMap.put("mediumspringgreen", 64154);
            hashMap.put("mediumturquoise", 4772300);
            hashMap.put("mediumvioletred", 13047173);
            hashMap.put("midnightblue", 1644912);
            hashMap.put("mintcream", 16121850);
            hashMap.put("mistyrose", 16770273);
            hashMap.put("moccasin", 16770229);
            hashMap.put("navajowhite", 16768685);
            hashMap.put("navy", 128);
            hashMap.put("oldlace", 16643558);
            hashMap.put("olive", 8421376);
            hashMap.put("olivedrab", 7048739);
            hashMap.put("orange", 16753920);
            hashMap.put("orangered", 16729344);
            hashMap.put("orchid", 14315734);
            hashMap.put("palegoldenrod", 15657130);
            hashMap.put("palegreen", 10025880);
            hashMap.put("paleturquoise", 11529966);
            hashMap.put("palevioletred", 14381203);
            hashMap.put("papayawhip", 16773077);
            hashMap.put("peachpuff", 16767673);
            hashMap.put("peru", 13468991);
            hashMap.put("pink", 16761035);
            hashMap.put("plum", 14524637);
            hashMap.put("powderblue", 11591910);
            hashMap.put("purple", 8388736);
            hashMap.put("red", 16711680);
            hashMap.put("rosybrown", 12357519);
            hashMap.put("royalblue", 4286945);
            hashMap.put("saddlebrown", 9127187);
            hashMap.put("salmon", 16416882);
            hashMap.put("sandybrown", 16032864);
            hashMap.put("seagreen", 3050327);
            hashMap.put("seashell", 16774638);
            hashMap.put("sienna", 10506797);
            hashMap.put("silver", 12632256);
            hashMap.put("skyblue", 8900331);
            hashMap.put("slateblue", 6970061);
            hashMap.put("slategray", 7372944);
            hashMap.put("slategrey", 7372944);
            hashMap.put("snow", 16775930);
            hashMap.put("springgreen", 65407);
            hashMap.put("steelblue", 4620980);
            hashMap.put("tan", 13808780);
            hashMap.put("teal", 32896);
            hashMap.put("thistle", 14204888);
            hashMap.put("tomato", 16737095);
            hashMap.put("turquoise", 4251856);
            hashMap.put("violet", 15631086);
            hashMap.put("wheat", 16113331);
            hashMap.put("white", 16777215);
            hashMap.put("whitesmoke", 16119285);
            hashMap.put("yellow", 16776960);
            hashMap.put("yellowgreen", 10145074);
        }

        public static Integer get(String str) {
            return colourKeywords.get(str);
        }
    }

    /* loaded from: classes2.dex */
    public static class FontSizeKeywords {
        public static final Map<String, SVG.Length> fontSizeKeywords;

        static {
            HashMap hashMap = new HashMap(9);
            fontSizeKeywords = hashMap;
            SVG.Unit unit = SVG.Unit.pt;
            hashMap.put("xx-small", new SVG.Length(0.694f, unit));
            hashMap.put("x-small", new SVG.Length(0.833f, unit));
            hashMap.put("small", new SVG.Length(10.0f, unit));
            hashMap.put("medium", new SVG.Length(12.0f, unit));
            hashMap.put("large", new SVG.Length(14.4f, unit));
            hashMap.put("x-large", new SVG.Length(17.3f, unit));
            hashMap.put("xx-large", new SVG.Length(20.7f, unit));
            SVG.Unit unit2 = SVG.Unit.percent;
            hashMap.put("smaller", new SVG.Length(83.33f, unit2));
            hashMap.put("larger", new SVG.Length(120.0f, unit2));
        }

        public static SVG.Length get(String str) {
            return fontSizeKeywords.get(str);
        }
    }

    /* loaded from: classes2.dex */
    public static class FontWeightKeywords {
        public static final Map<String, Integer> fontWeightKeywords;

        static {
            HashMap hashMap = new HashMap(13);
            fontWeightKeywords = hashMap;
            Integer valueOf = Integer.valueOf((int) StatusCode.BAD_REQUEST);
            hashMap.put("normal", valueOf);
            hashMap.put("bold", 700);
            hashMap.put("bolder", 1);
            hashMap.put("lighter", -1);
            hashMap.put("100", 100);
            hashMap.put("200", 200);
            hashMap.put("300", Integer.valueOf((int) UIMsg.MsgDefine.RENDER_STATE_FIRST_FRAME));
            hashMap.put("400", valueOf);
            hashMap.put("500", 500);
            hashMap.put("600", Integer.valueOf((int) UIMsg.MSG_MAP_PANO_DATA));
            hashMap.put("700", 700);
            hashMap.put("800", 800);
            hashMap.put("900", 900);
        }

        public static Integer get(String str) {
            return fontWeightKeywords.get(str);
        }
    }

    /* loaded from: classes2.dex */
    public static class AspectRatioKeywords {
        public static final Map<String, PreserveAspectRatio.Alignment> aspectRatioKeywords;

        static {
            HashMap hashMap = new HashMap(10);
            aspectRatioKeywords = hashMap;
            hashMap.put("none", PreserveAspectRatio.Alignment.None);
            hashMap.put("xMinYMin", PreserveAspectRatio.Alignment.XMinYMin);
            hashMap.put("xMidYMin", PreserveAspectRatio.Alignment.XMidYMin);
            hashMap.put("xMaxYMin", PreserveAspectRatio.Alignment.XMaxYMin);
            hashMap.put("xMinYMid", PreserveAspectRatio.Alignment.XMinYMid);
            hashMap.put("xMidYMid", PreserveAspectRatio.Alignment.XMidYMid);
            hashMap.put("xMaxYMid", PreserveAspectRatio.Alignment.XMaxYMid);
            hashMap.put("xMinYMax", PreserveAspectRatio.Alignment.XMinYMax);
            hashMap.put("xMidYMax", PreserveAspectRatio.Alignment.XMidYMax);
            hashMap.put("xMaxYMax", PreserveAspectRatio.Alignment.XMaxYMax);
        }

        public static PreserveAspectRatio.Alignment get(String str) {
            return aspectRatioKeywords.get(str);
        }
    }

    public SVG parse(InputStream inputStream) throws SVGParseException {
        if (!inputStream.markSupported()) {
            inputStream = new BufferedInputStream(inputStream);
        }
        try {
            inputStream.mark(3);
            int read = inputStream.read() + (inputStream.read() << 8);
            inputStream.reset();
            if (read == 35615) {
                inputStream = new GZIPInputStream(inputStream);
            }
        } catch (IOException unused) {
            Log.e("SVGParser", "error");
        }
        try {
            try {
                try {
                    XMLReader xMLReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
                    xMLReader.setContentHandler(this);
                    xMLReader.setProperty("http://xml.org/sax/properties/lexical-handler", this);
                    xMLReader.parse(new InputSource(inputStream));
                    try {
                        inputStream.close();
                    } catch (IOException unused2) {
                        Log.e("SVGParser", "Exception thrown closing input stream");
                    }
                    return this.svgDocument;
                } catch (IOException e) {
                    throw new SVGParseException("File error", e);
                }
            } catch (ParserConfigurationException e2) {
                throw new SVGParseException("XML Parser problem", e2);
            } catch (SAXException e3) {
                throw new SVGParseException("SVG parse error: " + e3.getMessage(), e3);
            }
        } catch (Throwable th) {
            try {
                inputStream.close();
            } catch (IOException unused3) {
                Log.e("SVGParser", "Exception thrown closing input stream");
            }
            throw th;
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startDocument() {
        this.svgDocument = new SVG();
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String str, String str2, String str3, Attributes attributes) throws SAXException {
        if (this.ignoring) {
            this.ignoreDepth++;
        } else if (!"http://www.w3.org/2000/svg".equals(str) && !"".equals(str)) {
        } else {
            switch (AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.fromString(str2).ordinal()]) {
                case 1:
                    svg(attributes);
                    return;
                case 2:
                case 3:
                    g(attributes);
                    return;
                case 4:
                    defs(attributes);
                    return;
                case 5:
                    use(attributes);
                    return;
                case 6:
                    path(attributes);
                    return;
                case 7:
                    rect(attributes);
                    return;
                case 8:
                    circle(attributes);
                    return;
                case 9:
                    ellipse(attributes);
                    return;
                case 10:
                    line(attributes);
                    return;
                case 11:
                    polyline(attributes);
                    return;
                case 12:
                    polygon(attributes);
                    return;
                case 13:
                    text(attributes);
                    return;
                case 14:
                    tspan(attributes);
                    return;
                case 15:
                    tref(attributes);
                    return;
                case 16:
                    zwitch(attributes);
                    return;
                case 17:
                    symbol(attributes);
                    return;
                case 18:
                    marker(attributes);
                    return;
                case 19:
                    linearGradient(attributes);
                    return;
                case 20:
                    radialGradient(attributes);
                    return;
                case 21:
                    stop(attributes);
                    return;
                case 22:
                case 23:
                    this.inMetadataElement = true;
                    return;
                case 24:
                    clipPath(attributes);
                    return;
                case 25:
                    textPath(attributes);
                    return;
                case 26:
                    pattern(attributes);
                    return;
                case 27:
                    image(attributes);
                    return;
                case 28:
                    view(attributes);
                    return;
                case 29:
                    mask(attributes);
                    return;
                case 30:
                    style(attributes);
                    return;
                case 31:
                    solidColor(attributes);
                    return;
                default:
                    this.ignoring = true;
                    this.ignoreDepth = 1;
                    return;
            }
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] cArr, int i, int i2) throws SAXException {
        if (this.ignoring) {
            return;
        }
        if (this.inMetadataElement) {
            if (this.metadataElementContents == null) {
                this.metadataElementContents = new StringBuilder(i2);
            }
            this.metadataElementContents.append(cArr, i, i2);
        } else if (this.inStyleElement) {
            if (this.styleElementContents == null) {
                this.styleElementContents = new StringBuilder(i2);
            }
            this.styleElementContents.append(cArr, i, i2);
        } else {
            SVG.SvgContainer svgContainer = this.currentElement;
            if (!(svgContainer instanceof SVG.TextContainer)) {
                return;
            }
            SVG.SvgConditionalContainer svgConditionalContainer = (SVG.SvgConditionalContainer) svgContainer;
            int size = svgConditionalContainer.children.size();
            SVG.SvgObject svgObject = size == 0 ? null : svgConditionalContainer.children.get(size - 1);
            if (svgObject instanceof SVG.TextSequence) {
                StringBuilder sb = new StringBuilder();
                SVG.TextSequence textSequence = (SVG.TextSequence) svgObject;
                sb.append(textSequence.text);
                sb.append(new String(cArr, i, i2));
                textSequence.text = sb.toString();
                return;
            }
            this.currentElement.addChild(new SVG.TextSequence(new String(cArr, i, i2)));
        }
    }

    @Override // org.xml.sax.ext.DefaultHandler2, org.xml.sax.ext.LexicalHandler
    public void comment(char[] cArr, int i, int i2) {
        if (!this.ignoring && this.inStyleElement) {
            if (this.styleElementContents == null) {
                this.styleElementContents = new StringBuilder(i2);
            }
            this.styleElementContents.append(cArr, i, i2);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String str, String str2, String str3) throws SAXException {
        if (this.ignoring) {
            int i = this.ignoreDepth - 1;
            this.ignoreDepth = i;
            if (i == 0) {
                this.ignoring = false;
                return;
            }
        }
        if ("http://www.w3.org/2000/svg".equals(str) || "".equals(str)) {
            int i2 = AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.fromString(str2).ordinal()];
            if (i2 != 1 && i2 != 2 && i2 != 4 && i2 != 5 && i2 != 13 && i2 != 14) {
                switch (i2) {
                    case 16:
                    case 17:
                    case 18:
                    case 19:
                    case 20:
                    case 21:
                    case 24:
                    case 25:
                    case 26:
                    case 27:
                    case 28:
                    case 29:
                    case 31:
                        break;
                    case 22:
                    case 23:
                        this.inMetadataElement = false;
                        this.metadataElementContents.setLength(0);
                        return;
                    case 30:
                        StringBuilder sb = this.styleElementContents;
                        if (sb == null) {
                            return;
                        }
                        this.inStyleElement = false;
                        parseCSSStyleSheet(sb.toString());
                        this.styleElementContents.setLength(0);
                        return;
                    default:
                        return;
                }
            }
            this.currentElement = ((SVG.SvgObject) this.currentElement).parent;
        }
    }

    public final void svg(Attributes attributes) throws SAXException {
        debug("<svg>", new Object[0]);
        SVG.Svg svg = new SVG.Svg();
        svg.document = this.svgDocument;
        svg.parent = this.currentElement;
        parseAttributesCore(svg, attributes);
        parseAttributesStyle(svg, attributes);
        parseAttributesConditional(svg, attributes);
        parseAttributesViewBox(svg, attributes);
        parseAttributesSVG(svg, attributes);
        SVG.SvgContainer svgContainer = this.currentElement;
        if (svgContainer == null) {
            this.svgDocument.setRootElement(svg);
        } else {
            svgContainer.addChild(svg);
        }
        this.currentElement = svg;
    }

    public final void parseAttributesSVG(SVG.Svg svg, Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); i++) {
            String trim = attributes.getValue(i).trim();
            int i2 = AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()];
            if (i2 == 1) {
                svg.x = parseLength(trim);
            } else if (i2 == 2) {
                svg.y = parseLength(trim);
            } else if (i2 == 3) {
                SVG.Length parseLength = parseLength(trim);
                svg.width = parseLength;
                if (parseLength.isNegative()) {
                    throw new SAXException("Invalid <svg> element. width cannot be negative");
                }
            } else if (i2 == 4) {
                SVG.Length parseLength2 = parseLength(trim);
                svg.height = parseLength2;
                if (parseLength2.isNegative()) {
                    throw new SAXException("Invalid <svg> element. height cannot be negative");
                }
            } else if (i2 == 5) {
                svg.version = trim;
            }
        }
    }

    /* renamed from: com.miui.gallery.editor.photo.utils.svgparser.SVGParser$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr;
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem;

        static {
            int[] iArr = new int[SVGAttr.values().length];
            $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr = iArr;
            try {
                iArr[SVGAttr.x.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.y.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.width.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.height.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.version.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.href.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.preserveAspectRatio.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.d.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.pathLength.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.rx.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.ry.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.cx.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.cy.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.r.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.x1.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.y1.ordinal()] = 16;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.x2.ordinal()] = 17;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.y2.ordinal()] = 18;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.dx.ordinal()] = 19;
            } catch (NoSuchFieldError unused19) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.dy.ordinal()] = 20;
            } catch (NoSuchFieldError unused20) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.requiredFeatures.ordinal()] = 21;
            } catch (NoSuchFieldError unused21) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.requiredExtensions.ordinal()] = 22;
            } catch (NoSuchFieldError unused22) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.systemLanguage.ordinal()] = 23;
            } catch (NoSuchFieldError unused23) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.requiredFormats.ordinal()] = 24;
            } catch (NoSuchFieldError unused24) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.requiredFonts.ordinal()] = 25;
            } catch (NoSuchFieldError unused25) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.refX.ordinal()] = 26;
            } catch (NoSuchFieldError unused26) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.refY.ordinal()] = 27;
            } catch (NoSuchFieldError unused27) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.markerWidth.ordinal()] = 28;
            } catch (NoSuchFieldError unused28) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.markerHeight.ordinal()] = 29;
            } catch (NoSuchFieldError unused29) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.markerUnits.ordinal()] = 30;
            } catch (NoSuchFieldError unused30) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.orient.ordinal()] = 31;
            } catch (NoSuchFieldError unused31) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.gradientUnits.ordinal()] = 32;
            } catch (NoSuchFieldError unused32) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.gradientTransform.ordinal()] = 33;
            } catch (NoSuchFieldError unused33) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.spreadMethod.ordinal()] = 34;
            } catch (NoSuchFieldError unused34) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.fx.ordinal()] = 35;
            } catch (NoSuchFieldError unused35) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.fy.ordinal()] = 36;
            } catch (NoSuchFieldError unused36) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.startOffset.ordinal()] = 37;
            } catch (NoSuchFieldError unused37) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.patternUnits.ordinal()] = 38;
            } catch (NoSuchFieldError unused38) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.patternContentUnits.ordinal()] = 39;
            } catch (NoSuchFieldError unused39) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.patternTransform.ordinal()] = 40;
            } catch (NoSuchFieldError unused40) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.maskUnits.ordinal()] = 41;
            } catch (NoSuchFieldError unused41) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.maskContentUnits.ordinal()] = 42;
            } catch (NoSuchFieldError unused42) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.style.ordinal()] = 43;
            } catch (NoSuchFieldError unused43) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.CLASS.ordinal()] = 44;
            } catch (NoSuchFieldError unused44) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.fill.ordinal()] = 45;
            } catch (NoSuchFieldError unused45) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.fill_rule.ordinal()] = 46;
            } catch (NoSuchFieldError unused46) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.fill_opacity.ordinal()] = 47;
            } catch (NoSuchFieldError unused47) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.stroke.ordinal()] = 48;
            } catch (NoSuchFieldError unused48) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.stroke_opacity.ordinal()] = 49;
            } catch (NoSuchFieldError unused49) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.stroke_width.ordinal()] = 50;
            } catch (NoSuchFieldError unused50) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.stroke_linecap.ordinal()] = 51;
            } catch (NoSuchFieldError unused51) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.stroke_linejoin.ordinal()] = 52;
            } catch (NoSuchFieldError unused52) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.stroke_miterlimit.ordinal()] = 53;
            } catch (NoSuchFieldError unused53) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.stroke_dasharray.ordinal()] = 54;
            } catch (NoSuchFieldError unused54) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.stroke_dashoffset.ordinal()] = 55;
            } catch (NoSuchFieldError unused55) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.opacity.ordinal()] = 56;
            } catch (NoSuchFieldError unused56) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.color.ordinal()] = 57;
            } catch (NoSuchFieldError unused57) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.font.ordinal()] = 58;
            } catch (NoSuchFieldError unused58) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.font_family.ordinal()] = 59;
            } catch (NoSuchFieldError unused59) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.font_size.ordinal()] = 60;
            } catch (NoSuchFieldError unused60) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.font_weight.ordinal()] = 61;
            } catch (NoSuchFieldError unused61) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.font_style.ordinal()] = 62;
            } catch (NoSuchFieldError unused62) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.text_decoration.ordinal()] = 63;
            } catch (NoSuchFieldError unused63) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.direction.ordinal()] = 64;
            } catch (NoSuchFieldError unused64) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.text_anchor.ordinal()] = 65;
            } catch (NoSuchFieldError unused65) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.overflow.ordinal()] = 66;
            } catch (NoSuchFieldError unused66) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.marker.ordinal()] = 67;
            } catch (NoSuchFieldError unused67) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.marker_start.ordinal()] = 68;
            } catch (NoSuchFieldError unused68) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.marker_mid.ordinal()] = 69;
            } catch (NoSuchFieldError unused69) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.marker_end.ordinal()] = 70;
            } catch (NoSuchFieldError unused70) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.display.ordinal()] = 71;
            } catch (NoSuchFieldError unused71) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.visibility.ordinal()] = 72;
            } catch (NoSuchFieldError unused72) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.stop_color.ordinal()] = 73;
            } catch (NoSuchFieldError unused73) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.stop_opacity.ordinal()] = 74;
            } catch (NoSuchFieldError unused74) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.clip.ordinal()] = 75;
            } catch (NoSuchFieldError unused75) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.clip_path.ordinal()] = 76;
            } catch (NoSuchFieldError unused76) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.clip_rule.ordinal()] = 77;
            } catch (NoSuchFieldError unused77) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.mask.ordinal()] = 78;
            } catch (NoSuchFieldError unused78) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.solid_color.ordinal()] = 79;
            } catch (NoSuchFieldError unused79) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.solid_opacity.ordinal()] = 80;
            } catch (NoSuchFieldError unused80) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.viewport_fill.ordinal()] = 81;
            } catch (NoSuchFieldError unused81) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.viewport_fill_opacity.ordinal()] = 82;
            } catch (NoSuchFieldError unused82) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.vector_effect.ordinal()] = 83;
            } catch (NoSuchFieldError unused83) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.viewBox.ordinal()] = 84;
            } catch (NoSuchFieldError unused84) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.type.ordinal()] = 85;
            } catch (NoSuchFieldError unused85) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.media.ordinal()] = 86;
            } catch (NoSuchFieldError unused86) {
            }
            int[] iArr2 = new int[SVGElem.values().length];
            $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem = iArr2;
            try {
                iArr2[SVGElem.svg.ordinal()] = 1;
            } catch (NoSuchFieldError unused87) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.g.ordinal()] = 2;
            } catch (NoSuchFieldError unused88) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.a.ordinal()] = 3;
            } catch (NoSuchFieldError unused89) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.defs.ordinal()] = 4;
            } catch (NoSuchFieldError unused90) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.use.ordinal()] = 5;
            } catch (NoSuchFieldError unused91) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.path.ordinal()] = 6;
            } catch (NoSuchFieldError unused92) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.rect.ordinal()] = 7;
            } catch (NoSuchFieldError unused93) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.circle.ordinal()] = 8;
            } catch (NoSuchFieldError unused94) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.ellipse.ordinal()] = 9;
            } catch (NoSuchFieldError unused95) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.line.ordinal()] = 10;
            } catch (NoSuchFieldError unused96) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.polyline.ordinal()] = 11;
            } catch (NoSuchFieldError unused97) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.polygon.ordinal()] = 12;
            } catch (NoSuchFieldError unused98) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.text.ordinal()] = 13;
            } catch (NoSuchFieldError unused99) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.tspan.ordinal()] = 14;
            } catch (NoSuchFieldError unused100) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.tref.ordinal()] = 15;
            } catch (NoSuchFieldError unused101) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.SWITCH.ordinal()] = 16;
            } catch (NoSuchFieldError unused102) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.symbol.ordinal()] = 17;
            } catch (NoSuchFieldError unused103) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.marker.ordinal()] = 18;
            } catch (NoSuchFieldError unused104) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.linearGradient.ordinal()] = 19;
            } catch (NoSuchFieldError unused105) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.radialGradient.ordinal()] = 20;
            } catch (NoSuchFieldError unused106) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.stop.ordinal()] = 21;
            } catch (NoSuchFieldError unused107) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.title.ordinal()] = 22;
            } catch (NoSuchFieldError unused108) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.desc.ordinal()] = 23;
            } catch (NoSuchFieldError unused109) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.clipPath.ordinal()] = 24;
            } catch (NoSuchFieldError unused110) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.textPath.ordinal()] = 25;
            } catch (NoSuchFieldError unused111) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.pattern.ordinal()] = 26;
            } catch (NoSuchFieldError unused112) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.image.ordinal()] = 27;
            } catch (NoSuchFieldError unused113) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.view.ordinal()] = 28;
            } catch (NoSuchFieldError unused114) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.mask.ordinal()] = 29;
            } catch (NoSuchFieldError unused115) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.style.ordinal()] = 30;
            } catch (NoSuchFieldError unused116) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGElem[SVGElem.solidColor.ordinal()] = 31;
            } catch (NoSuchFieldError unused117) {
            }
        }
    }

    public final void g(Attributes attributes) throws SAXException {
        debug("<g>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        SVG.Group group = new SVG.Group();
        group.document = this.svgDocument;
        group.parent = this.currentElement;
        parseAttributesCore(group, attributes);
        parseAttributesStyle(group, attributes);
        parseAttributesTransform(group, attributes);
        parseAttributesConditional(group, attributes);
        this.currentElement.addChild(group);
        this.currentElement = group;
    }

    public final void defs(Attributes attributes) throws SAXException {
        debug("<defs>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        SVG.Defs defs = new SVG.Defs();
        defs.document = this.svgDocument;
        defs.parent = this.currentElement;
        parseAttributesCore(defs, attributes);
        parseAttributesStyle(defs, attributes);
        parseAttributesTransform(defs, attributes);
        this.currentElement.addChild(defs);
        this.currentElement = defs;
    }

    public final void use(Attributes attributes) throws SAXException {
        debug("<use>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        SVG.Use use = new SVG.Use();
        use.document = this.svgDocument;
        use.parent = this.currentElement;
        parseAttributesCore(use, attributes);
        parseAttributesStyle(use, attributes);
        parseAttributesTransform(use, attributes);
        parseAttributesConditional(use, attributes);
        parseAttributesUse(use, attributes);
        this.currentElement.addChild(use);
        this.currentElement = use;
    }

    public final void parseAttributesUse(SVG.Use use, Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); i++) {
            String trim = attributes.getValue(i).trim();
            int i2 = AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()];
            if (i2 == 1) {
                use.x = parseLength(trim);
            } else if (i2 == 2) {
                use.y = parseLength(trim);
            } else if (i2 == 3) {
                SVG.Length parseLength = parseLength(trim);
                use.width = parseLength;
                if (parseLength.isNegative()) {
                    throw new SAXException("Invalid <use> element. width cannot be negative");
                }
            } else if (i2 == 4) {
                SVG.Length parseLength2 = parseLength(trim);
                use.height = parseLength2;
                if (parseLength2.isNegative()) {
                    throw new SAXException("Invalid <use> element. height cannot be negative");
                }
            } else if (i2 == 6 && "http://www.w3.org/1999/xlink".equals(attributes.getURI(i))) {
                use.href = trim;
            }
        }
    }

    public final void image(Attributes attributes) throws SAXException {
        debug("<image>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        SVG.Image image = new SVG.Image();
        image.document = this.svgDocument;
        image.parent = this.currentElement;
        parseAttributesCore(image, attributes);
        parseAttributesStyle(image, attributes);
        parseAttributesTransform(image, attributes);
        parseAttributesConditional(image, attributes);
        parseAttributesImage(image, attributes);
        this.currentElement.addChild(image);
        this.currentElement = image;
    }

    public final void parseAttributesImage(SVG.Image image, Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); i++) {
            String trim = attributes.getValue(i).trim();
            int i2 = AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()];
            if (i2 == 1) {
                image.x = parseLength(trim);
            } else if (i2 == 2) {
                image.y = parseLength(trim);
            } else if (i2 == 3) {
                SVG.Length parseLength = parseLength(trim);
                image.width = parseLength;
                if (parseLength.isNegative()) {
                    throw new SAXException("Invalid <use> element. width cannot be negative");
                }
            } else if (i2 == 4) {
                SVG.Length parseLength2 = parseLength(trim);
                image.height = parseLength2;
                if (parseLength2.isNegative()) {
                    throw new SAXException("Invalid <use> element. height cannot be negative");
                }
            } else if (i2 != 6) {
                if (i2 == 7) {
                    parsePreserveAspectRatio(image, trim);
                }
            } else if ("http://www.w3.org/1999/xlink".equals(attributes.getURI(i))) {
                image.href = trim;
            }
        }
    }

    public final void path(Attributes attributes) throws SAXException {
        debug("<path>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        SVG.Path path = new SVG.Path();
        path.document = this.svgDocument;
        path.parent = this.currentElement;
        parseAttributesCore(path, attributes);
        parseAttributesStyle(path, attributes);
        parseAttributesTransform(path, attributes);
        parseAttributesConditional(path, attributes);
        parseAttributesPath(path, attributes);
        this.currentElement.addChild(path);
    }

    public final void parseAttributesPath(SVG.Path path, Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); i++) {
            String trim = attributes.getValue(i).trim();
            int i2 = AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()];
            if (i2 == 8) {
                path.d = parsePath(trim);
            } else if (i2 != 9) {
                continue;
            } else {
                Float valueOf = Float.valueOf(parseFloat(trim));
                path.pathLength = valueOf;
                if (valueOf.floatValue() < 0.0f) {
                    throw new SAXException("Invalid <path> element. pathLength cannot be negative");
                }
            }
        }
    }

    public final void rect(Attributes attributes) throws SAXException {
        debug("<rect>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        SVG.Rect rect = new SVG.Rect();
        rect.document = this.svgDocument;
        rect.parent = this.currentElement;
        parseAttributesCore(rect, attributes);
        parseAttributesStyle(rect, attributes);
        parseAttributesTransform(rect, attributes);
        parseAttributesConditional(rect, attributes);
        parseAttributesRect(rect, attributes);
        this.currentElement.addChild(rect);
    }

    public final void parseAttributesRect(SVG.Rect rect, Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); i++) {
            String trim = attributes.getValue(i).trim();
            int i2 = AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()];
            if (i2 == 1) {
                rect.x = parseLength(trim);
            } else if (i2 == 2) {
                rect.y = parseLength(trim);
            } else if (i2 == 3) {
                SVG.Length parseLength = parseLength(trim);
                rect.width = parseLength;
                if (parseLength.isNegative()) {
                    throw new SAXException("Invalid <rect> element. width cannot be negative");
                }
            } else if (i2 == 4) {
                SVG.Length parseLength2 = parseLength(trim);
                rect.height = parseLength2;
                if (parseLength2.isNegative()) {
                    throw new SAXException("Invalid <rect> element. height cannot be negative");
                }
            } else if (i2 == 10) {
                SVG.Length parseLength3 = parseLength(trim);
                rect.rx = parseLength3;
                if (parseLength3.isNegative()) {
                    throw new SAXException("Invalid <rect> element. rx cannot be negative");
                }
            } else if (i2 != 11) {
                continue;
            } else {
                SVG.Length parseLength4 = parseLength(trim);
                rect.ry = parseLength4;
                if (parseLength4.isNegative()) {
                    throw new SAXException("Invalid <rect> element. ry cannot be negative");
                }
            }
        }
    }

    public final void circle(Attributes attributes) throws SAXException {
        debug("<circle>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        SVG.Circle circle = new SVG.Circle();
        circle.document = this.svgDocument;
        circle.parent = this.currentElement;
        parseAttributesCore(circle, attributes);
        parseAttributesStyle(circle, attributes);
        parseAttributesTransform(circle, attributes);
        parseAttributesConditional(circle, attributes);
        parseAttributesCircle(circle, attributes);
        this.currentElement.addChild(circle);
    }

    public final void parseAttributesCircle(SVG.Circle circle, Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); i++) {
            String trim = attributes.getValue(i).trim();
            switch (AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()]) {
                case 12:
                    circle.cx = parseLength(trim);
                    break;
                case 13:
                    circle.cy = parseLength(trim);
                    break;
                case 14:
                    SVG.Length parseLength = parseLength(trim);
                    circle.r = parseLength;
                    if (parseLength.isNegative()) {
                        throw new SAXException("Invalid <circle> element. r cannot be negative");
                    }
                    break;
            }
        }
    }

    public final void ellipse(Attributes attributes) throws SAXException {
        debug("<ellipse>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        SVG.Ellipse ellipse = new SVG.Ellipse();
        ellipse.document = this.svgDocument;
        ellipse.parent = this.currentElement;
        parseAttributesCore(ellipse, attributes);
        parseAttributesStyle(ellipse, attributes);
        parseAttributesTransform(ellipse, attributes);
        parseAttributesConditional(ellipse, attributes);
        parseAttributesEllipse(ellipse, attributes);
        this.currentElement.addChild(ellipse);
    }

    public final void parseAttributesEllipse(SVG.Ellipse ellipse, Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); i++) {
            String trim = attributes.getValue(i).trim();
            switch (AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()]) {
                case 10:
                    SVG.Length parseLength = parseLength(trim);
                    ellipse.rx = parseLength;
                    if (parseLength.isNegative()) {
                        throw new SAXException("Invalid <ellipse> element. rx cannot be negative");
                    }
                    break;
                case 11:
                    SVG.Length parseLength2 = parseLength(trim);
                    ellipse.ry = parseLength2;
                    if (parseLength2.isNegative()) {
                        throw new SAXException("Invalid <ellipse> element. ry cannot be negative");
                    }
                    break;
                case 12:
                    ellipse.cx = parseLength(trim);
                    break;
                case 13:
                    ellipse.cy = parseLength(trim);
                    break;
            }
        }
    }

    public final void line(Attributes attributes) throws SAXException {
        debug("<line>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        SVG.Line line = new SVG.Line();
        line.document = this.svgDocument;
        line.parent = this.currentElement;
        parseAttributesCore(line, attributes);
        parseAttributesStyle(line, attributes);
        parseAttributesTransform(line, attributes);
        parseAttributesConditional(line, attributes);
        parseAttributesLine(line, attributes);
        this.currentElement.addChild(line);
    }

    public final void parseAttributesLine(SVG.Line line, Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); i++) {
            String trim = attributes.getValue(i).trim();
            switch (AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()]) {
                case 15:
                    line.x1 = parseLength(trim);
                    break;
                case 16:
                    line.y1 = parseLength(trim);
                    break;
                case 17:
                    line.x2 = parseLength(trim);
                    break;
                case 18:
                    line.y2 = parseLength(trim);
                    break;
            }
        }
    }

    public final void polyline(Attributes attributes) throws SAXException {
        debug("<polyline>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        SVG.PolyLine polyLine = new SVG.PolyLine();
        polyLine.document = this.svgDocument;
        polyLine.parent = this.currentElement;
        parseAttributesCore(polyLine, attributes);
        parseAttributesStyle(polyLine, attributes);
        parseAttributesTransform(polyLine, attributes);
        parseAttributesConditional(polyLine, attributes);
        parseAttributesPolyLine(polyLine, attributes);
        this.currentElement.addChild(polyLine);
    }

    public final void parseAttributesPolyLine(SVG.PolyLine polyLine, Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); i++) {
            if (SVGAttr.fromString(attributes.getLocalName(i)) == SVGAttr.points) {
                TextScanner textScanner = new TextScanner(attributes.getValue(i));
                ArrayList<Float> arrayList = new ArrayList();
                textScanner.skipWhitespace();
                while (!textScanner.empty()) {
                    float nextFloat = textScanner.nextFloat();
                    if (Float.isNaN(nextFloat)) {
                        throw new SAXException("Invalid <polyline> points attribute. Non-coordinate content found in list.");
                    }
                    textScanner.skipCommaWhitespace();
                    float nextFloat2 = textScanner.nextFloat();
                    if (Float.isNaN(nextFloat2)) {
                        throw new SAXException("Invalid <polyline> points attribute. There should be an even number of coordinates.");
                    }
                    textScanner.skipCommaWhitespace();
                    arrayList.add(Float.valueOf(nextFloat));
                    arrayList.add(Float.valueOf(nextFloat2));
                }
                polyLine.points = new float[arrayList.size()];
                int i2 = 0;
                for (Float f : arrayList) {
                    polyLine.points[i2] = f.floatValue();
                    i2++;
                }
            }
        }
    }

    public final void parseAttributesPolygon(SVG.Polygon polygon, Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); i++) {
            if (SVGAttr.fromString(attributes.getLocalName(i)) == SVGAttr.points) {
                TextScanner textScanner = new TextScanner(attributes.getValue(i));
                ArrayList<Float> arrayList = new ArrayList();
                textScanner.skipWhitespace();
                while (!textScanner.empty()) {
                    float nextFloat = textScanner.nextFloat();
                    if (Float.isNaN(nextFloat)) {
                        throw new SAXException("Invalid <polygon> points attribute. Non-coordinate content found in list.");
                    }
                    textScanner.skipCommaWhitespace();
                    float nextFloat2 = textScanner.nextFloat();
                    if (Float.isNaN(nextFloat2)) {
                        throw new SAXException("Invalid <polygon> points attribute. There should be an even number of coordinates.");
                    }
                    textScanner.skipCommaWhitespace();
                    arrayList.add(Float.valueOf(nextFloat));
                    arrayList.add(Float.valueOf(nextFloat2));
                }
                polygon.points = new float[arrayList.size()];
                int i2 = 0;
                for (Float f : arrayList) {
                    polygon.points[i2] = f.floatValue();
                    i2++;
                }
            }
        }
    }

    public final void polygon(Attributes attributes) throws SAXException {
        debug("<polygon>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        SVG.Polygon polygon = new SVG.Polygon();
        polygon.document = this.svgDocument;
        polygon.parent = this.currentElement;
        parseAttributesCore(polygon, attributes);
        parseAttributesStyle(polygon, attributes);
        parseAttributesTransform(polygon, attributes);
        parseAttributesConditional(polygon, attributes);
        parseAttributesPolygon(polygon, attributes);
        this.currentElement.addChild(polygon);
    }

    public final void text(Attributes attributes) throws SAXException {
        debug("<text>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        SVG.Text text = new SVG.Text();
        text.document = this.svgDocument;
        text.parent = this.currentElement;
        parseAttributesCore(text, attributes);
        parseAttributesStyle(text, attributes);
        parseAttributesTransform(text, attributes);
        parseAttributesConditional(text, attributes);
        parseAttributesTextPosition(text, attributes);
        this.currentElement.addChild(text);
        this.currentElement = text;
    }

    public final void parseAttributesTextPosition(SVG.TextPositionedContainer textPositionedContainer, Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); i++) {
            String trim = attributes.getValue(i).trim();
            int i2 = AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()];
            if (i2 == 1) {
                textPositionedContainer.x = parseLengthList(trim);
            } else if (i2 == 2) {
                textPositionedContainer.y = parseLengthList(trim);
            } else if (i2 == 19) {
                textPositionedContainer.dx = parseLengthList(trim);
            } else if (i2 == 20) {
                textPositionedContainer.dy = parseLengthList(trim);
            }
        }
    }

    public final void tspan(Attributes attributes) throws SAXException {
        debug("<tspan>", new Object[0]);
        SVG.SvgContainer svgContainer = this.currentElement;
        if (svgContainer == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        if (!(svgContainer instanceof SVG.TextContainer)) {
            throw new SAXException("Invalid document. <tspan> elements are only valid inside <text> or other <tspan> elements.");
        }
        SVG.TSpan tSpan = new SVG.TSpan();
        tSpan.document = this.svgDocument;
        tSpan.parent = this.currentElement;
        parseAttributesCore(tSpan, attributes);
        parseAttributesStyle(tSpan, attributes);
        parseAttributesConditional(tSpan, attributes);
        parseAttributesTextPosition(tSpan, attributes);
        this.currentElement.addChild(tSpan);
        this.currentElement = tSpan;
        SVG.SvgContainer svgContainer2 = tSpan.parent;
        if (svgContainer2 instanceof SVG.TextRoot) {
            tSpan.setTextRoot((SVG.TextRoot) svgContainer2);
        } else {
            tSpan.setTextRoot(((SVG.TextChild) svgContainer2).getTextRoot());
        }
    }

    public final void tref(Attributes attributes) throws SAXException {
        debug("<tref>", new Object[0]);
        SVG.SvgContainer svgContainer = this.currentElement;
        if (svgContainer == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        if (!(svgContainer instanceof SVG.TextContainer)) {
            throw new SAXException("Invalid document. <tref> elements are only valid inside <text> or <tspan> elements.");
        }
        SVG.TRef tRef = new SVG.TRef();
        tRef.document = this.svgDocument;
        tRef.parent = this.currentElement;
        parseAttributesCore(tRef, attributes);
        parseAttributesStyle(tRef, attributes);
        parseAttributesConditional(tRef, attributes);
        parseAttributesTRef(tRef, attributes);
        this.currentElement.addChild(tRef);
        SVG.SvgContainer svgContainer2 = tRef.parent;
        if (svgContainer2 instanceof SVG.TextRoot) {
            tRef.setTextRoot((SVG.TextRoot) svgContainer2);
        } else {
            tRef.setTextRoot(((SVG.TextChild) svgContainer2).getTextRoot());
        }
    }

    public final void parseAttributesTRef(SVG.TRef tRef, Attributes attributes) {
        for (int i = 0; i < attributes.getLength(); i++) {
            String trim = attributes.getValue(i).trim();
            if (SVGAttr.fromString(attributes.getLocalName(i)) == SVGAttr.href && "http://www.w3.org/1999/xlink".equals(attributes.getURI(i))) {
                tRef.href = trim;
            }
        }
    }

    public final void zwitch(Attributes attributes) throws SAXException {
        debug("<switch>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        SVG.Switch r0 = new SVG.Switch();
        r0.document = this.svgDocument;
        r0.parent = this.currentElement;
        parseAttributesCore(r0, attributes);
        parseAttributesStyle(r0, attributes);
        parseAttributesTransform(r0, attributes);
        parseAttributesConditional(r0, attributes);
        this.currentElement.addChild(r0);
        this.currentElement = r0;
    }

    public final void parseAttributesConditional(SVG.SvgConditional svgConditional, Attributes attributes) {
        for (int i = 0; i < attributes.getLength(); i++) {
            String trim = attributes.getValue(i).trim();
            switch (AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()]) {
                case 21:
                    svgConditional.setRequiredFeatures(parseRequiredFeatures(trim));
                    break;
                case 22:
                    svgConditional.setRequiredExtensions(trim);
                    break;
                case 23:
                    svgConditional.setSystemLanguage(parseSystemLanguage(trim));
                    break;
                case 24:
                    svgConditional.setRequiredFormats(parseRequiredFormats(trim));
                    break;
                case 25:
                    List<String> parseFontFamily = parseFontFamily(trim);
                    svgConditional.setRequiredFonts(parseFontFamily != null ? new HashSet(parseFontFamily) : new HashSet(0));
                    break;
            }
        }
    }

    public final void symbol(Attributes attributes) throws SAXException {
        debug("<symbol>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        SVG.SvgViewBoxContainer symbol = new SVG.Symbol();
        symbol.document = this.svgDocument;
        symbol.parent = this.currentElement;
        parseAttributesCore(symbol, attributes);
        parseAttributesStyle(symbol, attributes);
        parseAttributesConditional(symbol, attributes);
        parseAttributesViewBox(symbol, attributes);
        this.currentElement.addChild(symbol);
        this.currentElement = symbol;
    }

    public final void marker(Attributes attributes) throws SAXException {
        debug("<marker>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        SVG.Marker marker = new SVG.Marker();
        marker.document = this.svgDocument;
        marker.parent = this.currentElement;
        parseAttributesCore(marker, attributes);
        parseAttributesStyle(marker, attributes);
        parseAttributesConditional(marker, attributes);
        parseAttributesViewBox(marker, attributes);
        parseAttributesMarker(marker, attributes);
        this.currentElement.addChild(marker);
        this.currentElement = marker;
    }

    public final void parseAttributesMarker(SVG.Marker marker, Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); i++) {
            String trim = attributes.getValue(i).trim();
            switch (AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()]) {
                case 26:
                    marker.refX = parseLength(trim);
                    break;
                case 27:
                    marker.refY = parseLength(trim);
                    break;
                case 28:
                    SVG.Length parseLength = parseLength(trim);
                    marker.markerWidth = parseLength;
                    if (parseLength.isNegative()) {
                        throw new SAXException("Invalid <marker> element. markerWidth cannot be negative");
                    }
                    break;
                case 29:
                    SVG.Length parseLength2 = parseLength(trim);
                    marker.markerHeight = parseLength2;
                    if (parseLength2.isNegative()) {
                        throw new SAXException("Invalid <marker> element. markerHeight cannot be negative");
                    }
                    break;
                case 30:
                    if ("strokeWidth".equals(trim)) {
                        marker.markerUnitsAreUser = false;
                        break;
                    } else if ("userSpaceOnUse".equals(trim)) {
                        marker.markerUnitsAreUser = true;
                        break;
                    } else {
                        throw new SAXException("Invalid value for attribute markerUnits");
                    }
                case 31:
                    if ("auto".equals(trim)) {
                        marker.orient = Float.valueOf(Float.NaN);
                        break;
                    } else {
                        marker.orient = Float.valueOf(parseFloat(trim));
                        break;
                    }
            }
        }
    }

    public final void linearGradient(Attributes attributes) throws SAXException {
        debug("<linearGradiant>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        SVG.SvgLinearGradient svgLinearGradient = new SVG.SvgLinearGradient();
        svgLinearGradient.document = this.svgDocument;
        svgLinearGradient.parent = this.currentElement;
        parseAttributesCore(svgLinearGradient, attributes);
        parseAttributesStyle(svgLinearGradient, attributes);
        parseAttributesGradient(svgLinearGradient, attributes);
        parseAttributesLinearGradient(svgLinearGradient, attributes);
        this.currentElement.addChild(svgLinearGradient);
        this.currentElement = svgLinearGradient;
    }

    /* JADX WARN: Code restructure failed: missing block: B:36:0x0081, code lost:
        continue;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void parseAttributesGradient(com.miui.gallery.editor.photo.utils.svgparser.SVG.GradientElement r5, org.xml.sax.Attributes r6) throws org.xml.sax.SAXException {
        /*
            r4 = this;
            r0 = 0
        L1:
            int r1 = r6.getLength()
            if (r0 >= r1) goto L85
            java.lang.String r1 = r6.getValue(r0)
            java.lang.String r1 = r1.trim()
            int[] r2 = com.miui.gallery.editor.photo.utils.svgparser.SVGParser.AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr
            java.lang.String r3 = r6.getLocalName(r0)
            com.miui.gallery.editor.photo.utils.svgparser.SVGParser$SVGAttr r3 = com.miui.gallery.editor.photo.utils.svgparser.SVGParser.SVGAttr.fromString(r3)
            int r3 = r3.ordinal()
            r2 = r2[r3]
            r3 = 6
            if (r2 == r3) goto L72
            switch(r2) {
                case 32: goto L50;
                case 33: goto L49;
                case 34: goto L26;
                default: goto L25;
            }
        L25:
            goto L81
        L26:
            com.miui.gallery.editor.photo.utils.svgparser.SVG$GradientSpread r2 = com.miui.gallery.editor.photo.utils.svgparser.SVG.GradientSpread.valueOf(r1)     // Catch: java.lang.IllegalArgumentException -> L2d
            r5.spreadMethod = r2     // Catch: java.lang.IllegalArgumentException -> L2d
            goto L81
        L2d:
            org.xml.sax.SAXException r5 = new org.xml.sax.SAXException
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r0 = "Invalid spreadMethod attribute. \""
            r6.append(r0)
            r6.append(r1)
            java.lang.String r0 = "\" is not a valid value."
            r6.append(r0)
            java.lang.String r6 = r6.toString()
            r5.<init>(r6)
            throw r5
        L49:
            android.graphics.Matrix r1 = r4.parseTransformList(r1)
            r5.gradientTransform = r1
            goto L81
        L50:
            java.lang.String r2 = "objectBoundingBox"
            boolean r2 = r2.equals(r1)
            if (r2 == 0) goto L5d
            java.lang.Boolean r1 = java.lang.Boolean.FALSE
            r5.gradientUnitsAreUser = r1
            goto L81
        L5d:
            java.lang.String r2 = "userSpaceOnUse"
            boolean r1 = r2.equals(r1)
            if (r1 == 0) goto L6a
            java.lang.Boolean r1 = java.lang.Boolean.TRUE
            r5.gradientUnitsAreUser = r1
            goto L81
        L6a:
            org.xml.sax.SAXException r5 = new org.xml.sax.SAXException
            java.lang.String r6 = "Invalid value for attribute gradientUnits"
            r5.<init>(r6)
            throw r5
        L72:
            java.lang.String r2 = r6.getURI(r0)
            java.lang.String r3 = "http://www.w3.org/1999/xlink"
            boolean r2 = r3.equals(r2)
            if (r2 != 0) goto L7f
            goto L81
        L7f:
            r5.href = r1
        L81:
            int r0 = r0 + 1
            goto L1
        L85:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.editor.photo.utils.svgparser.SVGParser.parseAttributesGradient(com.miui.gallery.editor.photo.utils.svgparser.SVG$GradientElement, org.xml.sax.Attributes):void");
    }

    public final void parseAttributesLinearGradient(SVG.SvgLinearGradient svgLinearGradient, Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); i++) {
            String trim = attributes.getValue(i).trim();
            switch (AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()]) {
                case 15:
                    svgLinearGradient.x1 = parseLength(trim);
                    break;
                case 16:
                    svgLinearGradient.y1 = parseLength(trim);
                    break;
                case 17:
                    svgLinearGradient.x2 = parseLength(trim);
                    break;
                case 18:
                    svgLinearGradient.y2 = parseLength(trim);
                    break;
            }
        }
    }

    public final void radialGradient(Attributes attributes) throws SAXException {
        debug("<radialGradient>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        SVG.SvgRadialGradient svgRadialGradient = new SVG.SvgRadialGradient();
        svgRadialGradient.document = this.svgDocument;
        svgRadialGradient.parent = this.currentElement;
        parseAttributesCore(svgRadialGradient, attributes);
        parseAttributesStyle(svgRadialGradient, attributes);
        parseAttributesGradient(svgRadialGradient, attributes);
        parseAttributesRadialGradient(svgRadialGradient, attributes);
        this.currentElement.addChild(svgRadialGradient);
        this.currentElement = svgRadialGradient;
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x005b, code lost:
        continue;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void parseAttributesRadialGradient(com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgRadialGradient r5, org.xml.sax.Attributes r6) throws org.xml.sax.SAXException {
        /*
            r4 = this;
            r0 = 0
        L1:
            int r1 = r6.getLength()
            if (r0 >= r1) goto L5e
            java.lang.String r1 = r6.getValue(r0)
            java.lang.String r1 = r1.trim()
            int[] r2 = com.miui.gallery.editor.photo.utils.svgparser.SVGParser.AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr
            java.lang.String r3 = r6.getLocalName(r0)
            com.miui.gallery.editor.photo.utils.svgparser.SVGParser$SVGAttr r3 = com.miui.gallery.editor.photo.utils.svgparser.SVGParser.SVGAttr.fromString(r3)
            int r3 = r3.ordinal()
            r2 = r2[r3]
            r3 = 35
            if (r2 == r3) goto L55
            r3 = 36
            if (r2 == r3) goto L4e
            switch(r2) {
                case 12: goto L47;
                case 13: goto L40;
                case 14: goto L2b;
                default: goto L2a;
            }
        L2a:
            goto L5b
        L2b:
            com.miui.gallery.editor.photo.utils.svgparser.SVG$Length r1 = parseLength(r1)
            r5.r = r1
            boolean r1 = r1.isNegative()
            if (r1 != 0) goto L38
            goto L5b
        L38:
            org.xml.sax.SAXException r5 = new org.xml.sax.SAXException
            java.lang.String r6 = "Invalid <radialGradient> element. r cannot be negative"
            r5.<init>(r6)
            throw r5
        L40:
            com.miui.gallery.editor.photo.utils.svgparser.SVG$Length r1 = parseLength(r1)
            r5.cy = r1
            goto L5b
        L47:
            com.miui.gallery.editor.photo.utils.svgparser.SVG$Length r1 = parseLength(r1)
            r5.cx = r1
            goto L5b
        L4e:
            com.miui.gallery.editor.photo.utils.svgparser.SVG$Length r1 = parseLength(r1)
            r5.fy = r1
            goto L5b
        L55:
            com.miui.gallery.editor.photo.utils.svgparser.SVG$Length r1 = parseLength(r1)
            r5.fx = r1
        L5b:
            int r0 = r0 + 1
            goto L1
        L5e:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.editor.photo.utils.svgparser.SVGParser.parseAttributesRadialGradient(com.miui.gallery.editor.photo.utils.svgparser.SVG$SvgRadialGradient, org.xml.sax.Attributes):void");
    }

    public final void stop(Attributes attributes) throws SAXException {
        debug("<stop>", new Object[0]);
        SVG.SvgContainer svgContainer = this.currentElement;
        if (svgContainer == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        if (!(svgContainer instanceof SVG.GradientElement)) {
            throw new SAXException("Invalid document. <stop> elements are only valid inside <linearGradiant> or <radialGradient> elements.");
        }
        SVG.Stop stop = new SVG.Stop();
        stop.document = this.svgDocument;
        stop.parent = this.currentElement;
        parseAttributesCore(stop, attributes);
        parseAttributesStyle(stop, attributes);
        parseAttributesStop(stop, attributes);
        this.currentElement.addChild(stop);
        this.currentElement = stop;
    }

    public final void parseAttributesStop(SVG.Stop stop, Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); i++) {
            String trim = attributes.getValue(i).trim();
            if (SVGAttr.fromString(attributes.getLocalName(i)) == SVGAttr.offset) {
                stop.offset = parseGradiantOffset(trim);
            }
        }
    }

    public final Float parseGradiantOffset(String str) throws SAXException {
        if (str.length() == 0) {
            throw new SAXException("Invalid offset value in <stop> (empty string)");
        }
        int length = str.length();
        boolean z = false;
        if (str.charAt(str.length() - 1) == '%') {
            length--;
            z = true;
        }
        try {
            float parseFloat = parseFloat(str, length);
            float f = 100.0f;
            if (z) {
                parseFloat /= 100.0f;
            }
            if (parseFloat < 0.0f) {
                f = 0.0f;
            } else if (parseFloat <= 100.0f) {
                f = parseFloat;
            }
            return Float.valueOf(f);
        } catch (NumberFormatException e) {
            throw new SAXException("Invalid offset value in <stop>: " + str, e);
        }
    }

    public final void solidColor(Attributes attributes) throws SAXException {
        debug("<solidColor>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        SVG.SolidColor solidColor = new SVG.SolidColor();
        solidColor.document = this.svgDocument;
        solidColor.parent = this.currentElement;
        parseAttributesCore(solidColor, attributes);
        parseAttributesStyle(solidColor, attributes);
        this.currentElement.addChild(solidColor);
        this.currentElement = solidColor;
    }

    public final void clipPath(Attributes attributes) throws SAXException {
        debug("<clipPath>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        SVG.ClipPath clipPath = new SVG.ClipPath();
        clipPath.document = this.svgDocument;
        clipPath.parent = this.currentElement;
        parseAttributesCore(clipPath, attributes);
        parseAttributesStyle(clipPath, attributes);
        parseAttributesTransform(clipPath, attributes);
        parseAttributesConditional(clipPath, attributes);
        parseAttributesClipPath(clipPath, attributes);
        this.currentElement.addChild(clipPath);
        this.currentElement = clipPath;
    }

    public final void parseAttributesClipPath(SVG.ClipPath clipPath, Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); i++) {
            String trim = attributes.getValue(i).trim();
            if (SVGAttr.fromString(attributes.getLocalName(i)) == SVGAttr.clipPathUnits) {
                if ("objectBoundingBox".equals(trim)) {
                    clipPath.clipPathUnitsAreUser = Boolean.FALSE;
                } else if ("userSpaceOnUse".equals(trim)) {
                    clipPath.clipPathUnitsAreUser = Boolean.TRUE;
                } else {
                    throw new SAXException("Invalid value for attribute clipPathUnits");
                }
            }
        }
    }

    public final void textPath(Attributes attributes) throws SAXException {
        debug("<textPath>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        SVG.TextPath textPath = new SVG.TextPath();
        textPath.document = this.svgDocument;
        textPath.parent = this.currentElement;
        parseAttributesCore(textPath, attributes);
        parseAttributesStyle(textPath, attributes);
        parseAttributesConditional(textPath, attributes);
        parseAttributesTextPath(textPath, attributes);
        this.currentElement.addChild(textPath);
        this.currentElement = textPath;
        SVG.SvgContainer svgContainer = textPath.parent;
        if (svgContainer instanceof SVG.TextRoot) {
            textPath.setTextRoot((SVG.TextRoot) svgContainer);
        } else {
            textPath.setTextRoot(((SVG.TextChild) svgContainer).getTextRoot());
        }
    }

    public final void parseAttributesTextPath(SVG.TextPath textPath, Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); i++) {
            String trim = attributes.getValue(i).trim();
            int i2 = AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()];
            if (i2 != 6) {
                if (i2 == 37) {
                    textPath.startOffset = parseLength(trim);
                }
            } else if ("http://www.w3.org/1999/xlink".equals(attributes.getURI(i))) {
                textPath.href = trim;
            }
        }
    }

    public final void pattern(Attributes attributes) throws SAXException {
        debug("<pattern>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        SVG.Pattern pattern = new SVG.Pattern();
        pattern.document = this.svgDocument;
        pattern.parent = this.currentElement;
        parseAttributesCore(pattern, attributes);
        parseAttributesStyle(pattern, attributes);
        parseAttributesConditional(pattern, attributes);
        parseAttributesViewBox(pattern, attributes);
        parseAttributesPattern(pattern, attributes);
        this.currentElement.addChild(pattern);
        this.currentElement = pattern;
    }

    /* JADX WARN: Code restructure failed: missing block: B:66:0x00c4, code lost:
        continue;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void parseAttributesPattern(com.miui.gallery.editor.photo.utils.svgparser.SVG.Pattern r6, org.xml.sax.Attributes r7) throws org.xml.sax.SAXException {
        /*
            r5 = this;
            r0 = 0
        L1:
            int r1 = r7.getLength()
            if (r0 >= r1) goto Lc8
            java.lang.String r1 = r7.getValue(r0)
            java.lang.String r1 = r1.trim()
            int[] r2 = com.miui.gallery.editor.photo.utils.svgparser.SVGParser.AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr
            java.lang.String r3 = r7.getLocalName(r0)
            com.miui.gallery.editor.photo.utils.svgparser.SVGParser$SVGAttr r3 = com.miui.gallery.editor.photo.utils.svgparser.SVGParser.SVGAttr.fromString(r3)
            int r3 = r3.ordinal()
            r2 = r2[r3]
            r3 = 1
            if (r2 == r3) goto Lbe
            r3 = 2
            if (r2 == r3) goto Lb7
            r3 = 3
            if (r2 == r3) goto La2
            r3 = 4
            if (r2 == r3) goto L8d
            r3 = 6
            if (r2 == r3) goto L7d
            java.lang.String r3 = "userSpaceOnUse"
            java.lang.String r4 = "objectBoundingBox"
            switch(r2) {
                case 38: goto L5f;
                case 39: goto L3f;
                case 40: goto L37;
                default: goto L35;
            }
        L35:
            goto Lc4
        L37:
            android.graphics.Matrix r1 = r5.parseTransformList(r1)
            r6.patternTransform = r1
            goto Lc4
        L3f:
            boolean r2 = r4.equals(r1)
            if (r2 == 0) goto L4b
            java.lang.Boolean r1 = java.lang.Boolean.FALSE
            r6.patternContentUnitsAreUser = r1
            goto Lc4
        L4b:
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L57
            java.lang.Boolean r1 = java.lang.Boolean.TRUE
            r6.patternContentUnitsAreUser = r1
            goto Lc4
        L57:
            org.xml.sax.SAXException r6 = new org.xml.sax.SAXException
            java.lang.String r7 = "Invalid value for attribute patternContentUnits"
            r6.<init>(r7)
            throw r6
        L5f:
            boolean r2 = r4.equals(r1)
            if (r2 == 0) goto L6a
            java.lang.Boolean r1 = java.lang.Boolean.FALSE
            r6.patternUnitsAreUser = r1
            goto Lc4
        L6a:
            boolean r1 = r3.equals(r1)
            if (r1 == 0) goto L75
            java.lang.Boolean r1 = java.lang.Boolean.TRUE
            r6.patternUnitsAreUser = r1
            goto Lc4
        L75:
            org.xml.sax.SAXException r6 = new org.xml.sax.SAXException
            java.lang.String r7 = "Invalid value for attribute patternUnits"
            r6.<init>(r7)
            throw r6
        L7d:
            java.lang.String r2 = r7.getURI(r0)
            java.lang.String r3 = "http://www.w3.org/1999/xlink"
            boolean r2 = r3.equals(r2)
            if (r2 != 0) goto L8a
            goto Lc4
        L8a:
            r6.href = r1
            goto Lc4
        L8d:
            com.miui.gallery.editor.photo.utils.svgparser.SVG$Length r1 = parseLength(r1)
            r6.height = r1
            boolean r1 = r1.isNegative()
            if (r1 != 0) goto L9a
            goto Lc4
        L9a:
            org.xml.sax.SAXException r6 = new org.xml.sax.SAXException
            java.lang.String r7 = "Invalid <pattern> element. height cannot be negative"
            r6.<init>(r7)
            throw r6
        La2:
            com.miui.gallery.editor.photo.utils.svgparser.SVG$Length r1 = parseLength(r1)
            r6.width = r1
            boolean r1 = r1.isNegative()
            if (r1 != 0) goto Laf
            goto Lc4
        Laf:
            org.xml.sax.SAXException r6 = new org.xml.sax.SAXException
            java.lang.String r7 = "Invalid <pattern> element. width cannot be negative"
            r6.<init>(r7)
            throw r6
        Lb7:
            com.miui.gallery.editor.photo.utils.svgparser.SVG$Length r1 = parseLength(r1)
            r6.y = r1
            goto Lc4
        Lbe:
            com.miui.gallery.editor.photo.utils.svgparser.SVG$Length r1 = parseLength(r1)
            r6.x = r1
        Lc4:
            int r0 = r0 + 1
            goto L1
        Lc8:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.editor.photo.utils.svgparser.SVGParser.parseAttributesPattern(com.miui.gallery.editor.photo.utils.svgparser.SVG$Pattern, org.xml.sax.Attributes):void");
    }

    public final void view(Attributes attributes) throws SAXException {
        debug("<view>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        SVG.SvgViewBoxContainer view = new SVG.View();
        view.document = this.svgDocument;
        view.parent = this.currentElement;
        parseAttributesCore(view, attributes);
        parseAttributesConditional(view, attributes);
        parseAttributesViewBox(view, attributes);
        this.currentElement.addChild(view);
        this.currentElement = view;
    }

    public final void mask(Attributes attributes) throws SAXException {
        debug("<mask>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        SVG.Mask mask = new SVG.Mask();
        mask.document = this.svgDocument;
        mask.parent = this.currentElement;
        parseAttributesCore(mask, attributes);
        parseAttributesStyle(mask, attributes);
        parseAttributesConditional(mask, attributes);
        parseAttributesMask(mask, attributes);
        this.currentElement.addChild(mask);
        this.currentElement = mask;
    }

    public final void parseAttributesMask(SVG.Mask mask, Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); i++) {
            String trim = attributes.getValue(i).trim();
            int i2 = AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()];
            if (i2 == 1) {
                mask.x = parseLength(trim);
            } else if (i2 == 2) {
                mask.y = parseLength(trim);
            } else if (i2 == 3) {
                SVG.Length parseLength = parseLength(trim);
                mask.width = parseLength;
                if (parseLength.isNegative()) {
                    throw new SAXException("Invalid <mask> element. width cannot be negative");
                }
            } else if (i2 == 4) {
                SVG.Length parseLength2 = parseLength(trim);
                mask.height = parseLength2;
                if (parseLength2.isNegative()) {
                    throw new SAXException("Invalid <mask> element. height cannot be negative");
                }
            } else if (i2 != 41) {
                if (i2 != 42) {
                    continue;
                } else if ("objectBoundingBox".equals(trim)) {
                    mask.maskContentUnitsAreUser = Boolean.FALSE;
                } else if ("userSpaceOnUse".equals(trim)) {
                    mask.maskContentUnitsAreUser = Boolean.TRUE;
                } else {
                    throw new SAXException("Invalid value for attribute maskContentUnits");
                }
            } else if ("objectBoundingBox".equals(trim)) {
                mask.maskUnitsAreUser = Boolean.FALSE;
            } else if ("userSpaceOnUse".equals(trim)) {
                mask.maskUnitsAreUser = Boolean.TRUE;
            } else {
                throw new SAXException("Invalid value for attribute maskUnits");
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class TextScanner {
        public String input;
        public int inputLength;
        public int position = 0;
        public NumberParser numberParser = new NumberParser();

        public boolean isEOL(int i) {
            return i == 10 || i == 13;
        }

        public boolean isWhitespace(int i) {
            return i == 32 || i == 10 || i == 13 || i == 9;
        }

        public TextScanner(String str) {
            String trim = str.trim();
            this.input = trim;
            this.inputLength = trim.length();
        }

        public boolean empty() {
            return this.position == this.inputLength;
        }

        public void skipWhitespace() {
            while (true) {
                int i = this.position;
                if (i >= this.inputLength || !isWhitespace(this.input.charAt(i))) {
                    return;
                }
                this.position++;
            }
        }

        public boolean skipCommaWhitespace() {
            skipWhitespace();
            int i = this.position;
            if (i != this.inputLength && this.input.charAt(i) == ',') {
                this.position++;
                skipWhitespace();
                return true;
            }
            return false;
        }

        public float nextFloat() {
            float parseNumber = this.numberParser.parseNumber(this.input, this.position, this.inputLength);
            if (!Float.isNaN(parseNumber)) {
                this.position = this.numberParser.getEndPos();
            }
            return parseNumber;
        }

        public float possibleNextFloat() {
            skipCommaWhitespace();
            float parseNumber = this.numberParser.parseNumber(this.input, this.position, this.inputLength);
            if (!Float.isNaN(parseNumber)) {
                this.position = this.numberParser.getEndPos();
            }
            return parseNumber;
        }

        public float checkedNextFloat(float f) {
            if (Float.isNaN(f)) {
                return Float.NaN;
            }
            skipCommaWhitespace();
            return nextFloat();
        }

        public Integer nextChar() {
            int i = this.position;
            if (i == this.inputLength) {
                return null;
            }
            String str = this.input;
            this.position = i + 1;
            return Integer.valueOf(str.charAt(i));
        }

        public SVG.Length nextLength() {
            float nextFloat = nextFloat();
            if (Float.isNaN(nextFloat)) {
                return null;
            }
            SVG.Unit nextUnit = nextUnit();
            if (nextUnit == null) {
                return new SVG.Length(nextFloat, SVG.Unit.px);
            }
            return new SVG.Length(nextFloat, nextUnit);
        }

        public Boolean nextFlag() {
            int i = this.position;
            if (i == this.inputLength) {
                return null;
            }
            char charAt = this.input.charAt(i);
            if (charAt != '0' && charAt != '1') {
                return null;
            }
            boolean z = true;
            this.position++;
            if (charAt != '1') {
                z = false;
            }
            return Boolean.valueOf(z);
        }

        public Boolean checkedNextFlag(Object obj) {
            if (obj == null) {
                return null;
            }
            skipCommaWhitespace();
            return nextFlag();
        }

        public boolean consume(char c) {
            int i = this.position;
            boolean z = i < this.inputLength && this.input.charAt(i) == c;
            if (z) {
                this.position++;
            }
            return z;
        }

        public boolean consume(String str) {
            int length = str.length();
            int i = this.position;
            boolean z = i <= this.inputLength - length && this.input.substring(i, i + length).equals(str);
            if (z) {
                this.position += length;
            }
            return z;
        }

        public int advanceChar() {
            int i = this.position;
            int i2 = this.inputLength;
            if (i == i2) {
                return -1;
            }
            int i3 = i + 1;
            this.position = i3;
            if (i3 >= i2) {
                return -1;
            }
            return this.input.charAt(i3);
        }

        public String nextToken() {
            return nextToken(' ');
        }

        public String nextToken(char c) {
            if (empty()) {
                return null;
            }
            char charAt = this.input.charAt(this.position);
            if (isWhitespace(charAt) || charAt == c) {
                return null;
            }
            int i = this.position;
            int advanceChar = advanceChar();
            while (advanceChar != -1 && advanceChar != c && !isWhitespace(advanceChar)) {
                advanceChar = advanceChar();
            }
            return this.input.substring(i, this.position);
        }

        public String nextFunction() {
            if (empty()) {
                return null;
            }
            int i = this.position;
            int charAt = this.input.charAt(i);
            while (true) {
                if ((charAt < 97 || charAt > 122) && (charAt < 65 || charAt > 90)) {
                    break;
                }
                charAt = advanceChar();
            }
            int i2 = this.position;
            while (isWhitespace(charAt)) {
                charAt = advanceChar();
            }
            if (charAt == 40) {
                this.position++;
                return this.input.substring(i, i2);
            }
            this.position = i;
            return null;
        }

        public String ahead() {
            int i = this.position;
            while (!empty() && !isWhitespace(this.input.charAt(this.position))) {
                this.position++;
            }
            String substring = this.input.substring(i, this.position);
            this.position = i;
            return substring;
        }

        public SVG.Unit nextUnit() {
            if (empty()) {
                return null;
            }
            if (this.input.charAt(this.position) == '%') {
                this.position++;
                return SVG.Unit.percent;
            }
            int i = this.position;
            if (i > this.inputLength - 2) {
                return null;
            }
            try {
                SVG.Unit valueOf = SVG.Unit.valueOf(this.input.substring(i, i + 2).toLowerCase(Locale.US));
                this.position += 2;
                return valueOf;
            } catch (IllegalArgumentException unused) {
                return null;
            }
        }

        public boolean hasLetter() {
            int i = this.position;
            if (i == this.inputLength) {
                return false;
            }
            char charAt = this.input.charAt(i);
            return (charAt >= 'a' && charAt <= 'z') || (charAt >= 'A' && charAt <= 'Z');
        }

        public String nextQuotedString() {
            if (empty()) {
                return null;
            }
            int i = this.position;
            char charAt = this.input.charAt(i);
            if (charAt != '\'' && charAt != '\"') {
                return null;
            }
            int advanceChar = advanceChar();
            while (advanceChar != -1 && advanceChar != charAt) {
                advanceChar = advanceChar();
            }
            if (advanceChar == -1) {
                this.position = i;
                return null;
            }
            int i2 = this.position + 1;
            this.position = i2;
            return this.input.substring(i + 1, i2 - 1);
        }

        public String restOfText() {
            if (empty()) {
                return null;
            }
            int i = this.position;
            this.position = this.inputLength;
            return this.input.substring(i);
        }
    }

    public final void parseAttributesCore(SVG.SvgElementBase svgElementBase, Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); i++) {
            String qName = attributes.getQName(i);
            if (qName.equals("id") || qName.equals("xml:id")) {
                svgElementBase.id = attributes.getValue(i).trim();
                return;
            } else if (qName.equals("xml:space")) {
                String trim = attributes.getValue(i).trim();
                if ("default".equals(trim)) {
                    svgElementBase.spacePreserve = Boolean.FALSE;
                    return;
                } else if ("preserve".equals(trim)) {
                    svgElementBase.spacePreserve = Boolean.TRUE;
                    return;
                } else {
                    throw new SAXException("Invalid value for \"xml:space\" attribute: " + trim);
                }
            }
        }
    }

    public final void parseAttributesStyle(SVG.SvgElementBase svgElementBase, Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); i++) {
            String trim = attributes.getValue(i).trim();
            if (trim.length() != 0) {
                int i2 = AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()];
                if (i2 == 43) {
                    parseStyle(svgElementBase, trim);
                } else if (i2 == 44) {
                    svgElementBase.classNames = CSSParser.parseClassAttribute(trim);
                } else {
                    if (svgElementBase.baseStyle == null) {
                        svgElementBase.baseStyle = new SVG.Style();
                    }
                    processStyleProperty(svgElementBase.baseStyle, attributes.getLocalName(i), attributes.getValue(i).trim());
                }
            }
        }
    }

    public static void parseStyle(SVG.SvgElementBase svgElementBase, String str) throws SAXException {
        TextScanner textScanner = new TextScanner(str.replaceAll("/\\*.*?\\*/", ""));
        while (true) {
            String nextToken = textScanner.nextToken(CoreConstants.COLON_CHAR);
            textScanner.skipWhitespace();
            if (!textScanner.consume(CoreConstants.COLON_CHAR)) {
                return;
            }
            textScanner.skipWhitespace();
            String nextToken2 = textScanner.nextToken(';');
            if (nextToken2 == null) {
                return;
            }
            textScanner.skipWhitespace();
            if (textScanner.empty() || textScanner.consume(';')) {
                if (svgElementBase.style == null) {
                    svgElementBase.style = new SVG.Style();
                }
                processStyleProperty(svgElementBase.style, nextToken, nextToken2);
                textScanner.skipWhitespace();
            }
        }
    }

    public static void processStyleProperty(SVG.Style style, String str, String str2) throws SAXException {
        if (str2.length() != 0 && !str2.equals("inherit")) {
            switch (AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.fromString(str).ordinal()]) {
                case 45:
                    style.fill = parsePaintSpecifier(str2, "fill");
                    style.specifiedFlags |= 1;
                    return;
                case 46:
                    style.fillRule = parseFillRule(str2);
                    style.specifiedFlags |= 2;
                    return;
                case 47:
                    style.fillOpacity = Float.valueOf(parseOpacity(str2));
                    style.specifiedFlags |= 4;
                    return;
                case 48:
                    style.stroke = parsePaintSpecifier(str2, "stroke");
                    style.specifiedFlags |= 8;
                    return;
                case 49:
                    style.strokeOpacity = Float.valueOf(parseOpacity(str2));
                    style.specifiedFlags |= 16;
                    return;
                case 50:
                    style.strokeWidth = parseLength(str2);
                    style.specifiedFlags |= 32;
                    return;
                case 51:
                    style.strokeLineCap = parseStrokeLineCap(str2);
                    style.specifiedFlags |= 64;
                    return;
                case 52:
                    style.strokeLineJoin = parseStrokeLineJoin(str2);
                    style.specifiedFlags |= 128;
                    return;
                case 53:
                    style.strokeMiterLimit = Float.valueOf(parseFloat(str2));
                    style.specifiedFlags |= 256;
                    return;
                case 54:
                    if ("none".equals(str2)) {
                        style.strokeDashArray = null;
                    } else {
                        style.strokeDashArray = parseStrokeDashArray(str2);
                    }
                    style.specifiedFlags |= 512;
                    return;
                case 55:
                    style.strokeDashOffset = parseLength(str2);
                    style.specifiedFlags |= FileSize.KB_COEFFICIENT;
                    return;
                case 56:
                    style.opacity = Float.valueOf(parseOpacity(str2));
                    style.specifiedFlags |= 2048;
                    return;
                case 57:
                    style.color = parseColour(str2);
                    style.specifiedFlags |= 4096;
                    return;
                case 58:
                    parseFont(style, str2);
                    return;
                case 59:
                    style.fontFamily = parseFontFamily(str2);
                    style.specifiedFlags |= FileAppender.DEFAULT_BUFFER_SIZE;
                    return;
                case 60:
                    style.fontSize = parseFontSize(str2);
                    style.specifiedFlags |= 16384;
                    return;
                case 61:
                    style.fontWeight = parseFontWeight(str2);
                    style.specifiedFlags |= 32768;
                    return;
                case 62:
                    style.fontStyle = parseFontStyle(str2);
                    style.specifiedFlags |= 65536;
                    return;
                case 63:
                    style.textDecoration = parseTextDecoration(str2);
                    style.specifiedFlags |= 131072;
                    return;
                case 64:
                    style.direction = parseTextDirection(str2);
                    style.specifiedFlags |= 68719476736L;
                    return;
                case 65:
                    style.textAnchor = parseTextAnchor(str2);
                    style.specifiedFlags |= 262144;
                    return;
                case 66:
                    style.overflow = parseOverflow(str2);
                    style.specifiedFlags |= 524288;
                    return;
                case 67:
                    String parseFunctionalIRI = parseFunctionalIRI(str2, str);
                    style.markerStart = parseFunctionalIRI;
                    style.markerMid = parseFunctionalIRI;
                    style.markerEnd = parseFunctionalIRI;
                    style.specifiedFlags |= 14680064;
                    return;
                case 68:
                    style.markerStart = parseFunctionalIRI(str2, str);
                    style.specifiedFlags |= 2097152;
                    return;
                case 69:
                    style.markerMid = parseFunctionalIRI(str2, str);
                    style.specifiedFlags |= 4194304;
                    return;
                case 70:
                    style.markerEnd = parseFunctionalIRI(str2, str);
                    style.specifiedFlags |= 8388608;
                    return;
                case 71:
                    if (str2.indexOf(124) < 0) {
                        if ("|inline|block|list-item|run-in|compact|marker|table|inline-table|table-row-group|table-header-group|table-footer-group|table-row|table-column-group|table-column|table-cell|table-caption|none|".contains('|' + str2 + '|')) {
                            style.display = Boolean.valueOf(!str2.equals("none"));
                            style.specifiedFlags |= 16777216;
                            return;
                        }
                    }
                    throw new SAXException("Invalid value for \"display\" attribute: " + str2);
                case 72:
                    if (str2.indexOf(124) < 0) {
                        if ("|visible|hidden|collapse|".contains('|' + str2 + '|')) {
                            style.visibility = Boolean.valueOf(str2.equals(MapBundleKey.MapObjKey.OBJ_SL_VISI));
                            style.specifiedFlags |= 33554432;
                            return;
                        }
                    }
                    throw new SAXException("Invalid value for \"visibility\" attribute: " + str2);
                case 73:
                    if (str2.equals("currentColor")) {
                        style.stopColor = SVG.CurrentColor.getInstance();
                    } else {
                        style.stopColor = parseColour(str2);
                    }
                    style.specifiedFlags |= 67108864;
                    return;
                case 74:
                    style.stopOpacity = Float.valueOf(parseOpacity(str2));
                    style.specifiedFlags |= 134217728;
                    return;
                case 75:
                    style.clip = parseClip(str2);
                    style.specifiedFlags |= FileSize.MB_COEFFICIENT;
                    return;
                case 76:
                    style.clipPath = parseFunctionalIRI(str2, str);
                    style.specifiedFlags |= 268435456;
                    return;
                case 77:
                    style.clipRule = parseFillRule(str2);
                    style.specifiedFlags |= 536870912;
                    return;
                case 78:
                    style.mask = parseFunctionalIRI(str2, str);
                    style.specifiedFlags |= FileSize.GB_COEFFICIENT;
                    return;
                case 79:
                    if (str2.equals("currentColor")) {
                        style.solidColor = SVG.CurrentColor.getInstance();
                    } else {
                        style.solidColor = parseColour(str2);
                    }
                    style.specifiedFlags |= 2147483648L;
                    return;
                case 80:
                    style.solidOpacity = Float.valueOf(parseOpacity(str2));
                    style.specifiedFlags |= 4294967296L;
                    return;
                case 81:
                    if (str2.equals("currentColor")) {
                        style.viewportFill = SVG.CurrentColor.getInstance();
                    } else {
                        style.viewportFill = parseColour(str2);
                    }
                    style.specifiedFlags |= 8589934592L;
                    return;
                case 82:
                    style.viewportFillOpacity = Float.valueOf(parseOpacity(str2));
                    style.specifiedFlags |= 17179869184L;
                    return;
                case 83:
                    style.vectorEffect = parseVectorEffect(str2);
                    style.specifiedFlags |= 34359738368L;
                    return;
                default:
                    return;
            }
        }
    }

    public final void parseAttributesViewBox(SVG.SvgViewBoxContainer svgViewBoxContainer, Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); i++) {
            String trim = attributes.getValue(i).trim();
            int i2 = AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()];
            if (i2 == 7) {
                parsePreserveAspectRatio(svgViewBoxContainer, trim);
            } else if (i2 == 84) {
                svgViewBoxContainer.viewBox = parseViewBox(trim);
            }
        }
    }

    public final void parseAttributesTransform(SVG.HasTransform hasTransform, Attributes attributes) throws SAXException {
        for (int i = 0; i < attributes.getLength(); i++) {
            if (SVGAttr.fromString(attributes.getLocalName(i)) == SVGAttr.transform) {
                hasTransform.setTransform(parseTransformList(attributes.getValue(i)));
            }
        }
    }

    public final Matrix parseTransformList(String str) throws SAXException {
        Matrix matrix = new Matrix();
        TextScanner textScanner = new TextScanner(str);
        textScanner.skipWhitespace();
        while (!textScanner.empty()) {
            String nextFunction = textScanner.nextFunction();
            if (nextFunction == null) {
                throw new SAXException("Bad transform function encountered in transform list: " + str);
            }
            char c = 65535;
            switch (nextFunction.hashCode()) {
                case -1081239615:
                    if (nextFunction.equals("matrix")) {
                        c = 0;
                        break;
                    }
                    break;
                case -925180581:
                    if (nextFunction.equals("rotate")) {
                        c = 1;
                        break;
                    }
                    break;
                case 109250890:
                    if (nextFunction.equals("scale")) {
                        c = 2;
                        break;
                    }
                    break;
                case 109493390:
                    if (nextFunction.equals("skewX")) {
                        c = 3;
                        break;
                    }
                    break;
                case 109493391:
                    if (nextFunction.equals("skewY")) {
                        c = 4;
                        break;
                    }
                    break;
                case 1052832078:
                    if (nextFunction.equals("translate")) {
                        c = 5;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    textScanner.skipWhitespace();
                    float nextFloat = textScanner.nextFloat();
                    textScanner.skipCommaWhitespace();
                    float nextFloat2 = textScanner.nextFloat();
                    textScanner.skipCommaWhitespace();
                    float nextFloat3 = textScanner.nextFloat();
                    textScanner.skipCommaWhitespace();
                    float nextFloat4 = textScanner.nextFloat();
                    textScanner.skipCommaWhitespace();
                    float nextFloat5 = textScanner.nextFloat();
                    textScanner.skipCommaWhitespace();
                    float nextFloat6 = textScanner.nextFloat();
                    textScanner.skipWhitespace();
                    if (Float.isNaN(nextFloat6) || !textScanner.consume(CoreConstants.RIGHT_PARENTHESIS_CHAR)) {
                        throw new SAXException("Invalid transform list: " + str);
                    }
                    Matrix matrix2 = new Matrix();
                    matrix2.setValues(new float[]{nextFloat, nextFloat3, nextFloat5, nextFloat2, nextFloat4, nextFloat6, 0.0f, 0.0f, 1.0f});
                    matrix.preConcat(matrix2);
                    break;
                    break;
                case 1:
                    textScanner.skipWhitespace();
                    float nextFloat7 = textScanner.nextFloat();
                    float possibleNextFloat = textScanner.possibleNextFloat();
                    float possibleNextFloat2 = textScanner.possibleNextFloat();
                    textScanner.skipWhitespace();
                    if (Float.isNaN(nextFloat7) || !textScanner.consume(CoreConstants.RIGHT_PARENTHESIS_CHAR)) {
                        throw new SAXException("Invalid transform list: " + str);
                    } else if (Float.isNaN(possibleNextFloat)) {
                        matrix.preRotate(nextFloat7);
                        break;
                    } else if (!Float.isNaN(possibleNextFloat2)) {
                        matrix.preRotate(nextFloat7, possibleNextFloat, possibleNextFloat2);
                        break;
                    } else {
                        throw new SAXException("Invalid transform list: " + str);
                    }
                case 2:
                    textScanner.skipWhitespace();
                    float nextFloat8 = textScanner.nextFloat();
                    float possibleNextFloat3 = textScanner.possibleNextFloat();
                    textScanner.skipWhitespace();
                    if (Float.isNaN(nextFloat8) || !textScanner.consume(CoreConstants.RIGHT_PARENTHESIS_CHAR)) {
                        throw new SAXException("Invalid transform list: " + str);
                    } else if (Float.isNaN(possibleNextFloat3)) {
                        matrix.preScale(nextFloat8, nextFloat8);
                        break;
                    } else {
                        matrix.preScale(nextFloat8, possibleNextFloat3);
                        break;
                    }
                    break;
                case 3:
                    textScanner.skipWhitespace();
                    float nextFloat9 = textScanner.nextFloat();
                    textScanner.skipWhitespace();
                    if (Float.isNaN(nextFloat9) || !textScanner.consume(CoreConstants.RIGHT_PARENTHESIS_CHAR)) {
                        throw new SAXException("Invalid transform list: " + str);
                    }
                    matrix.preSkew((float) Math.tan(Math.toRadians(nextFloat9)), 0.0f);
                    break;
                    break;
                case 4:
                    textScanner.skipWhitespace();
                    float nextFloat10 = textScanner.nextFloat();
                    textScanner.skipWhitespace();
                    if (Float.isNaN(nextFloat10) || !textScanner.consume(CoreConstants.RIGHT_PARENTHESIS_CHAR)) {
                        throw new SAXException("Invalid transform list: " + str);
                    }
                    matrix.preSkew(0.0f, (float) Math.tan(Math.toRadians(nextFloat10)));
                    break;
                case 5:
                    textScanner.skipWhitespace();
                    float nextFloat11 = textScanner.nextFloat();
                    float possibleNextFloat4 = textScanner.possibleNextFloat();
                    textScanner.skipWhitespace();
                    if (Float.isNaN(nextFloat11) || !textScanner.consume(CoreConstants.RIGHT_PARENTHESIS_CHAR)) {
                        throw new SAXException("Invalid transform list: " + str);
                    } else if (Float.isNaN(possibleNextFloat4)) {
                        matrix.preTranslate(nextFloat11, 0.0f);
                        break;
                    } else {
                        matrix.preTranslate(nextFloat11, possibleNextFloat4);
                        break;
                    }
                    break;
                default:
                    throw new SAXException("Invalid transform list fn: " + nextFunction + ")");
            }
            if (textScanner.empty()) {
                return matrix;
            }
            textScanner.skipCommaWhitespace();
        }
        return matrix;
    }

    public static SVG.Length parseLength(String str) throws SAXException {
        if (str.length() == 0) {
            throw new SAXException("Invalid length value (empty string)");
        }
        int length = str.length();
        SVG.Unit unit = SVG.Unit.px;
        char charAt = str.charAt(length - 1);
        if (charAt == '%') {
            length--;
            unit = SVG.Unit.percent;
        } else if (length > 2 && Character.isLetter(charAt) && Character.isLetter(str.charAt(length - 2))) {
            length -= 2;
            try {
                unit = SVG.Unit.valueOf(str.substring(length).toLowerCase(Locale.US));
            } catch (IllegalArgumentException unused) {
                throw new SAXException("Invalid length unit specifier: " + str);
            }
        }
        try {
            return new SVG.Length(parseFloat(str, length), unit);
        } catch (NumberFormatException e) {
            throw new SAXException("Invalid length value: " + str, e);
        }
    }

    public static List<SVG.Length> parseLengthList(String str) throws SAXException {
        if (str.length() == 0) {
            throw new SAXException("Invalid length list (empty string)");
        }
        ArrayList arrayList = new ArrayList(1);
        TextScanner textScanner = new TextScanner(str);
        textScanner.skipWhitespace();
        while (!textScanner.empty()) {
            float nextFloat = textScanner.nextFloat();
            if (Float.isNaN(nextFloat)) {
                throw new SAXException("Invalid length list value: " + textScanner.ahead());
            }
            SVG.Unit nextUnit = textScanner.nextUnit();
            if (nextUnit == null) {
                nextUnit = SVG.Unit.px;
            }
            arrayList.add(new SVG.Length(nextFloat, nextUnit));
            textScanner.skipCommaWhitespace();
        }
        return arrayList;
    }

    public static float parseFloat(String str) throws SAXException {
        int length = str.length();
        if (length == 0) {
            throw new SAXException("Invalid float value (empty string)");
        }
        return parseFloat(str, length);
    }

    public static float parseFloat(String str, int i) throws SAXException {
        float parseNumber = new NumberParser().parseNumber(str, 0, i);
        if (!Float.isNaN(parseNumber)) {
            return parseNumber;
        }
        throw new SAXException("Invalid float value: " + str);
    }

    public static float parseOpacity(String str) throws SAXException {
        float parseFloat = parseFloat(str);
        if (parseFloat < 0.0f) {
            return 0.0f;
        }
        return Math.min(parseFloat, 1.0f);
    }

    public static SVG.Box parseViewBox(String str) throws SAXException {
        TextScanner textScanner = new TextScanner(str);
        textScanner.skipWhitespace();
        float nextFloat = textScanner.nextFloat();
        textScanner.skipCommaWhitespace();
        float nextFloat2 = textScanner.nextFloat();
        textScanner.skipCommaWhitespace();
        float nextFloat3 = textScanner.nextFloat();
        textScanner.skipCommaWhitespace();
        float nextFloat4 = textScanner.nextFloat();
        if (Float.isNaN(nextFloat) || Float.isNaN(nextFloat2) || Float.isNaN(nextFloat3) || Float.isNaN(nextFloat4)) {
            throw new SAXException("Invalid viewBox definition - should have four numbers");
        }
        if (nextFloat3 < 0.0f) {
            throw new SAXException("Invalid viewBox. width cannot be negative");
        }
        if (nextFloat4 < 0.0f) {
            throw new SAXException("Invalid viewBox. height cannot be negative");
        }
        return new SVG.Box(nextFloat, nextFloat2, nextFloat3, nextFloat4);
    }

    public static void parsePreserveAspectRatio(SVG.SvgPreserveAspectRatioContainer svgPreserveAspectRatioContainer, String str) throws SAXException {
        PreserveAspectRatio.Scale scale;
        TextScanner textScanner = new TextScanner(str);
        textScanner.skipWhitespace();
        String nextToken = textScanner.nextToken();
        if ("defer".equals(nextToken)) {
            textScanner.skipWhitespace();
            nextToken = textScanner.nextToken();
        }
        PreserveAspectRatio.Alignment alignment = AspectRatioKeywords.get(nextToken);
        textScanner.skipWhitespace();
        if (!textScanner.empty()) {
            String nextToken2 = textScanner.nextToken();
            if (nextToken2.equals("meet")) {
                scale = PreserveAspectRatio.Scale.Meet;
            } else if (nextToken2.equals("slice")) {
                scale = PreserveAspectRatio.Scale.Slice;
            } else {
                throw new SAXException("Invalid preserveAspectRatio definition: " + str);
            }
        } else {
            scale = null;
        }
        svgPreserveAspectRatioContainer.preserveAspectRatio = new PreserveAspectRatio(alignment, scale);
    }

    public static SVG.SvgPaint parsePaintSpecifier(String str, String str2) throws SAXException {
        if (str.startsWith("url(")) {
            int indexOf = str.indexOf(")");
            if (indexOf == -1) {
                throw new SAXException("Bad " + str2 + " attribute. Unterminated url() reference");
            }
            String trim = str.substring(4, indexOf).trim();
            SVG.SvgPaint svgPaint = null;
            String trim2 = str.substring(indexOf + 1).trim();
            if (trim2.length() > 0) {
                svgPaint = parseColourSpecifer(trim2);
            }
            return new SVG.PaintReference(trim, svgPaint);
        }
        return parseColourSpecifer(str);
    }

    public static SVG.SvgPaint parseColourSpecifer(String str) throws SAXException {
        if (str.equals("none")) {
            return null;
        }
        if (str.equals("currentColor")) {
            return SVG.CurrentColor.getInstance();
        }
        return parseColour(str);
    }

    public static SVG.Colour parseColour(String str) throws SAXException {
        if (str.charAt(0) == '#') {
            IntegerParser parseHex = IntegerParser.parseHex(str, str.length());
            if (parseHex == null) {
                throw new SAXException("Bad hex colour value: " + str);
            }
            int endPos = parseHex.getEndPos();
            if (endPos == 7) {
                return new SVG.Colour(parseHex.value());
            }
            if (endPos == 4) {
                int value = parseHex.value();
                int i = value & nexChecker.UHD_WIDTH;
                int i2 = value & 240;
                int i3 = value & 15;
                return new SVG.Colour(i3 | (i << 12) | (i << 16) | (i2 << 8) | (i2 << 4) | (i3 << 4));
            }
            throw new SAXException("Bad hex colour value: " + str);
        } else if (str.toLowerCase(Locale.US).startsWith("rgb(")) {
            TextScanner textScanner = new TextScanner(str.substring(4));
            textScanner.skipWhitespace();
            float nextFloat = textScanner.nextFloat();
            if (!Float.isNaN(nextFloat) && textScanner.consume(CoreConstants.PERCENT_CHAR)) {
                nextFloat = (nextFloat * 256.0f) / 100.0f;
            }
            float checkedNextFloat = textScanner.checkedNextFloat(nextFloat);
            if (!Float.isNaN(checkedNextFloat) && textScanner.consume(CoreConstants.PERCENT_CHAR)) {
                checkedNextFloat = (checkedNextFloat * 256.0f) / 100.0f;
            }
            float checkedNextFloat2 = textScanner.checkedNextFloat(checkedNextFloat);
            if (!Float.isNaN(checkedNextFloat2) && textScanner.consume(CoreConstants.PERCENT_CHAR)) {
                checkedNextFloat2 = (checkedNextFloat2 * 256.0f) / 100.0f;
            }
            textScanner.skipWhitespace();
            if (Float.isNaN(checkedNextFloat2) || !textScanner.consume(CoreConstants.RIGHT_PARENTHESIS_CHAR)) {
                throw new SAXException("Bad rgb() colour value: " + str);
            }
            return new SVG.Colour((clamp255(nextFloat) << 16) | (clamp255(checkedNextFloat) << 8) | clamp255(checkedNextFloat2));
        } else {
            return parseColourKeyword(str);
        }
    }

    public static int clamp255(float f) {
        if (f < 0.0f) {
            return 0;
        }
        if (f <= 255.0f) {
            return Math.round(f);
        }
        return 255;
    }

    public static SVG.Colour parseColourKeyword(String str) throws SAXException {
        Integer num = ColourKeywords.get(str.toLowerCase(Locale.US));
        if (num == null) {
            throw new SAXException("Invalid colour keyword: " + str);
        }
        return new SVG.Colour(num.intValue());
    }

    public static void parseFont(SVG.Style style, String str) throws SAXException {
        String nextToken;
        if ("|caption|icon|menu|message-box|small-caption|status-bar|".contains('|' + str + '|')) {
            return;
        }
        TextScanner textScanner = new TextScanner(str);
        Integer num = null;
        SVG.Style.FontStyle fontStyle = null;
        String str2 = null;
        while (true) {
            nextToken = textScanner.nextToken('/');
            textScanner.skipWhitespace();
            if (nextToken == null) {
                throw new SAXException("Invalid font style attribute: missing font size and family");
            }
            if (num != null && fontStyle != null) {
                break;
            } else if (!nextToken.equals("normal") && (num != null || (num = FontWeightKeywords.get(nextToken)) == null)) {
                if (fontStyle != null || (fontStyle = fontStyleKeyword(nextToken)) == null) {
                    if (str2 != null || !nextToken.equals("small-caps")) {
                        break;
                    }
                    str2 = nextToken;
                }
            }
        }
        SVG.Length parseFontSize = parseFontSize(nextToken);
        if (textScanner.consume('/')) {
            textScanner.skipWhitespace();
            String nextToken2 = textScanner.nextToken();
            if (nextToken2 == null) {
                throw new SAXException("Invalid font style attribute: missing line-height");
            }
            parseLength(nextToken2);
            textScanner.skipWhitespace();
        }
        style.fontFamily = parseFontFamily(textScanner.restOfText());
        style.fontSize = parseFontSize;
        style.fontWeight = Integer.valueOf(num == null ? StatusCode.BAD_REQUEST : num.intValue());
        if (fontStyle == null) {
            fontStyle = SVG.Style.FontStyle.Normal;
        }
        style.fontStyle = fontStyle;
        style.specifiedFlags |= 122880;
    }

    public static List<String> parseFontFamily(String str) {
        TextScanner textScanner = new TextScanner(str);
        ArrayList arrayList = null;
        do {
            String nextQuotedString = textScanner.nextQuotedString();
            if (nextQuotedString == null) {
                nextQuotedString = textScanner.nextToken(CoreConstants.COMMA_CHAR);
            }
            if (nextQuotedString == null) {
                break;
            }
            if (arrayList == null) {
                arrayList = new ArrayList();
            }
            arrayList.add(nextQuotedString);
            textScanner.skipCommaWhitespace();
        } while (!textScanner.empty());
        return arrayList;
    }

    public static SVG.Length parseFontSize(String str) throws SAXException {
        SVG.Length length = FontSizeKeywords.get(str);
        return length == null ? parseLength(str) : length;
    }

    public static Integer parseFontWeight(String str) throws SAXException {
        Integer num = FontWeightKeywords.get(str);
        if (num != null) {
            return num;
        }
        throw new SAXException("Invalid font-weight property: " + str);
    }

    public static SVG.Style.FontStyle parseFontStyle(String str) throws SAXException {
        SVG.Style.FontStyle fontStyleKeyword = fontStyleKeyword(str);
        if (fontStyleKeyword != null) {
            return fontStyleKeyword;
        }
        throw new SAXException("Invalid font-style property: " + str);
    }

    public static SVG.Style.FontStyle fontStyleKeyword(String str) {
        if ("italic".equals(str)) {
            return SVG.Style.FontStyle.Italic;
        }
        if ("normal".equals(str)) {
            return SVG.Style.FontStyle.Normal;
        }
        if (!"oblique".equals(str)) {
            return null;
        }
        return SVG.Style.FontStyle.Oblique;
    }

    public static SVG.Style.TextDecoration parseTextDecoration(String str) throws SAXException {
        if ("none".equals(str)) {
            return SVG.Style.TextDecoration.None;
        }
        if ("underline".equals(str)) {
            return SVG.Style.TextDecoration.Underline;
        }
        if ("overline".equals(str)) {
            return SVG.Style.TextDecoration.Overline;
        }
        if ("line-through".equals(str)) {
            return SVG.Style.TextDecoration.LineThrough;
        }
        if ("blink".equals(str)) {
            return SVG.Style.TextDecoration.Blink;
        }
        throw new SAXException("Invalid text-decoration property: " + str);
    }

    public static SVG.Style.TextDirection parseTextDirection(String str) throws SAXException {
        if ("ltr".equals(str)) {
            return SVG.Style.TextDirection.LTR;
        }
        if ("rtl".equals(str)) {
            return SVG.Style.TextDirection.RTL;
        }
        throw new SAXException("Invalid direction property: " + str);
    }

    public static SVG.Style.FillRule parseFillRule(String str) throws SAXException {
        if ("nonzero".equals(str)) {
            return SVG.Style.FillRule.NonZero;
        }
        if ("evenodd".equals(str)) {
            return SVG.Style.FillRule.EvenOdd;
        }
        throw new SAXException("Invalid fill-rule property: " + str);
    }

    public static SVG.Style.LineCaps parseStrokeLineCap(String str) throws SAXException {
        if ("butt".equals(str)) {
            return SVG.Style.LineCaps.Butt;
        }
        if ("round".equals(str)) {
            return SVG.Style.LineCaps.Round;
        }
        if ("square".equals(str)) {
            return SVG.Style.LineCaps.Square;
        }
        throw new SAXException("Invalid stroke-linecap property: " + str);
    }

    public static SVG.Style.LineJoin parseStrokeLineJoin(String str) throws SAXException {
        if ("miter".equals(str)) {
            return SVG.Style.LineJoin.Miter;
        }
        if ("round".equals(str)) {
            return SVG.Style.LineJoin.Round;
        }
        if ("bevel".equals(str)) {
            return SVG.Style.LineJoin.Bevel;
        }
        throw new SAXException("Invalid stroke-linejoin property: " + str);
    }

    public static SVG.Length[] parseStrokeDashArray(String str) throws SAXException {
        SVG.Length nextLength;
        TextScanner textScanner = new TextScanner(str);
        textScanner.skipWhitespace();
        if (!textScanner.empty() && (nextLength = textScanner.nextLength()) != null) {
            if (nextLength.isNegative()) {
                throw new SAXException("Invalid stroke-dasharray. Dash segemnts cannot be negative: " + str);
            }
            float floatValue = nextLength.floatValue();
            ArrayList arrayList = new ArrayList();
            arrayList.add(nextLength);
            while (!textScanner.empty()) {
                textScanner.skipCommaWhitespace();
                SVG.Length nextLength2 = textScanner.nextLength();
                if (nextLength2 == null) {
                    throw new SAXException("Invalid stroke-dasharray. Non-Length content found: " + str);
                } else if (nextLength2.isNegative()) {
                    throw new SAXException("Invalid stroke-dasharray. Dash segemnts cannot be negative: " + str);
                } else {
                    arrayList.add(nextLength2);
                    floatValue += nextLength2.floatValue();
                }
            }
            if (floatValue != 0.0f) {
                return (SVG.Length[]) arrayList.toArray(new SVG.Length[0]);
            }
            return null;
        }
        return null;
    }

    public static SVG.Style.TextAnchor parseTextAnchor(String str) throws SAXException {
        if ("start".equals(str)) {
            return SVG.Style.TextAnchor.Start;
        }
        if ("middle".equals(str)) {
            return SVG.Style.TextAnchor.Middle;
        }
        if ("end".equals(str)) {
            return SVG.Style.TextAnchor.End;
        }
        throw new SAXException("Invalid text-anchor property: " + str);
    }

    public static Boolean parseOverflow(String str) throws SAXException {
        if (MapBundleKey.MapObjKey.OBJ_SL_VISI.equals(str) || "auto".equals(str)) {
            return Boolean.TRUE;
        }
        if ("hidden".equals(str) || "scroll".equals(str)) {
            return Boolean.FALSE;
        }
        throw new SAXException("Invalid toverflow property: " + str);
    }

    public static SVG.CSSClipRect parseClip(String str) throws SAXException {
        if ("auto".equals(str)) {
            return null;
        }
        if (!str.toLowerCase(Locale.US).startsWith("rect(")) {
            throw new SAXException("Invalid clip attribute shape. Only rect() is supported.");
        }
        TextScanner textScanner = new TextScanner(str.substring(5));
        textScanner.skipWhitespace();
        SVG.Length parseLengthOrAuto = parseLengthOrAuto(textScanner);
        textScanner.skipCommaWhitespace();
        SVG.Length parseLengthOrAuto2 = parseLengthOrAuto(textScanner);
        textScanner.skipCommaWhitespace();
        SVG.Length parseLengthOrAuto3 = parseLengthOrAuto(textScanner);
        textScanner.skipCommaWhitespace();
        SVG.Length parseLengthOrAuto4 = parseLengthOrAuto(textScanner);
        textScanner.skipWhitespace();
        if (!textScanner.consume(CoreConstants.RIGHT_PARENTHESIS_CHAR)) {
            throw new SAXException("Bad rect() clip definition: " + str);
        }
        return new SVG.CSSClipRect(parseLengthOrAuto, parseLengthOrAuto2, parseLengthOrAuto3, parseLengthOrAuto4);
    }

    public static SVG.Length parseLengthOrAuto(TextScanner textScanner) {
        if (textScanner.consume("auto")) {
            return new SVG.Length(0.0f);
        }
        return textScanner.nextLength();
    }

    public static SVG.Style.VectorEffect parseVectorEffect(String str) throws SAXException {
        if ("none".equals(str)) {
            return SVG.Style.VectorEffect.None;
        }
        if ("non-scaling-stroke".equals(str)) {
            return SVG.Style.VectorEffect.NonScalingStroke;
        }
        throw new SAXException("Invalid vector-effect property: " + str);
    }

    public static SVG.PathDefinition parsePath(String str) {
        float f;
        float checkedNextFloat;
        float f2;
        float f3;
        float f4;
        float f5;
        TextScanner textScanner = new TextScanner(str);
        SVG.PathDefinition pathDefinition = new SVG.PathDefinition();
        if (textScanner.empty()) {
            return pathDefinition;
        }
        int intValue = textScanner.nextChar().intValue();
        int i = 109;
        if (intValue != 77 && intValue != 109) {
            return pathDefinition;
        }
        int i2 = intValue;
        float f6 = 0.0f;
        float f7 = 0.0f;
        float f8 = 0.0f;
        float f9 = 0.0f;
        float f10 = 0.0f;
        float f11 = 0.0f;
        while (true) {
            textScanner.skipWhitespace();
            int i3 = 108;
            switch (i2) {
                case 65:
                case 97:
                    float nextFloat = textScanner.nextFloat();
                    float checkedNextFloat2 = textScanner.checkedNextFloat(nextFloat);
                    float checkedNextFloat3 = textScanner.checkedNextFloat(checkedNextFloat2);
                    Boolean checkedNextFlag = textScanner.checkedNextFlag(Float.valueOf(checkedNextFloat3));
                    Boolean checkedNextFlag2 = textScanner.checkedNextFlag(checkedNextFlag);
                    if (checkedNextFlag2 == null) {
                        checkedNextFloat = Float.NaN;
                        f = Float.NaN;
                    } else {
                        float possibleNextFloat = textScanner.possibleNextFloat();
                        f = possibleNextFloat;
                        checkedNextFloat = textScanner.checkedNextFloat(possibleNextFloat);
                    }
                    if (!Float.isNaN(checkedNextFloat) && nextFloat >= 0.0f && checkedNextFloat2 >= 0.0f) {
                        if (i2 == 97) {
                            f += f6;
                            checkedNextFloat += f8;
                        }
                        float f12 = checkedNextFloat;
                        pathDefinition.arcTo(nextFloat, checkedNextFloat2, checkedNextFloat3, checkedNextFlag.booleanValue(), checkedNextFlag2 == null ? false : checkedNextFlag2.booleanValue(), f, f12);
                        f8 = f12;
                        f9 = f8;
                        f6 = f;
                        f7 = f6;
                        break;
                    }
                    break;
                case 67:
                case 99:
                    float nextFloat2 = textScanner.nextFloat();
                    float checkedNextFloat4 = textScanner.checkedNextFloat(nextFloat2);
                    float checkedNextFloat5 = textScanner.checkedNextFloat(checkedNextFloat4);
                    float checkedNextFloat6 = textScanner.checkedNextFloat(checkedNextFloat5);
                    float checkedNextFloat7 = textScanner.checkedNextFloat(checkedNextFloat6);
                    float checkedNextFloat8 = textScanner.checkedNextFloat(checkedNextFloat7);
                    if (!Float.isNaN(checkedNextFloat8)) {
                        if (i2 == 99) {
                            checkedNextFloat7 += f6;
                            checkedNextFloat8 += f8;
                            nextFloat2 += f6;
                            checkedNextFloat4 += f8;
                            checkedNextFloat5 += f6;
                            checkedNextFloat6 += f8;
                        }
                        f2 = checkedNextFloat5;
                        f3 = checkedNextFloat6;
                        f4 = checkedNextFloat8;
                        f5 = checkedNextFloat7;
                        pathDefinition.cubicTo(nextFloat2, checkedNextFloat4, f2, f3, f5, f4);
                        f6 = f5;
                        f7 = f2;
                        f8 = f4;
                        f9 = f3;
                        break;
                    } else {
                        return pathDefinition;
                    }
                case 72:
                case 104:
                    float nextFloat3 = textScanner.nextFloat();
                    if (!Float.isNaN(nextFloat3)) {
                        if (i2 == 104) {
                            nextFloat3 += f6;
                        }
                        f6 = nextFloat3;
                        pathDefinition.lineTo(f6, f8);
                        f7 = f6;
                        break;
                    } else {
                        return pathDefinition;
                    }
                case 76:
                case 108:
                    float nextFloat4 = textScanner.nextFloat();
                    float checkedNextFloat9 = textScanner.checkedNextFloat(nextFloat4);
                    if (!Float.isNaN(checkedNextFloat9)) {
                        if (i2 == 108) {
                            nextFloat4 += f6;
                            checkedNextFloat9 += f8;
                        }
                        f6 = nextFloat4;
                        f8 = checkedNextFloat9;
                        pathDefinition.lineTo(f6, f8);
                        f7 = f6;
                        f9 = f8;
                        break;
                    } else {
                        return pathDefinition;
                    }
                case 77:
                case 109:
                    float nextFloat5 = textScanner.nextFloat();
                    float checkedNextFloat10 = textScanner.checkedNextFloat(nextFloat5);
                    if (!Float.isNaN(checkedNextFloat10)) {
                        if (i2 == i && !pathDefinition.isEmpty()) {
                            nextFloat5 += f6;
                            checkedNextFloat10 += f8;
                        }
                        f6 = nextFloat5;
                        f8 = checkedNextFloat10;
                        pathDefinition.moveTo(f6, f8);
                        if (i2 != i) {
                            i3 = 76;
                        }
                        f7 = f6;
                        f10 = f7;
                        f9 = f8;
                        f11 = f9;
                        i2 = i3;
                        break;
                    } else {
                        return pathDefinition;
                    }
                    break;
                case 81:
                case 113:
                    f7 = textScanner.nextFloat();
                    f9 = textScanner.checkedNextFloat(f7);
                    float checkedNextFloat11 = textScanner.checkedNextFloat(f9);
                    float checkedNextFloat12 = textScanner.checkedNextFloat(checkedNextFloat11);
                    if (!Float.isNaN(checkedNextFloat12)) {
                        if (i2 == 113) {
                            checkedNextFloat11 += f6;
                            checkedNextFloat12 += f8;
                            f7 += f6;
                            f9 += f8;
                        }
                        f6 = checkedNextFloat11;
                        f8 = checkedNextFloat12;
                        pathDefinition.quadTo(f7, f9, f6, f8);
                        break;
                    } else {
                        return pathDefinition;
                    }
                case 83:
                case 115:
                    float f13 = (f6 * 2.0f) - f7;
                    float f14 = (2.0f * f8) - f9;
                    float nextFloat6 = textScanner.nextFloat();
                    float checkedNextFloat13 = textScanner.checkedNextFloat(nextFloat6);
                    float checkedNextFloat14 = textScanner.checkedNextFloat(checkedNextFloat13);
                    float checkedNextFloat15 = textScanner.checkedNextFloat(checkedNextFloat14);
                    if (!Float.isNaN(checkedNextFloat15)) {
                        if (i2 == 115) {
                            checkedNextFloat14 += f6;
                            checkedNextFloat15 += f8;
                            nextFloat6 += f6;
                            checkedNextFloat13 += f8;
                        }
                        f2 = nextFloat6;
                        f3 = checkedNextFloat13;
                        f4 = checkedNextFloat15;
                        f5 = checkedNextFloat14;
                        pathDefinition.cubicTo(f13, f14, f2, f3, f5, f4);
                        f6 = f5;
                        f7 = f2;
                        f8 = f4;
                        f9 = f3;
                        break;
                    } else {
                        return pathDefinition;
                    }
                case 84:
                case 116:
                    f7 = (f6 * 2.0f) - f7;
                    f9 = (2.0f * f8) - f9;
                    float nextFloat7 = textScanner.nextFloat();
                    float checkedNextFloat16 = textScanner.checkedNextFloat(nextFloat7);
                    if (!Float.isNaN(checkedNextFloat16)) {
                        if (i2 == 116) {
                            nextFloat7 += f6;
                            checkedNextFloat16 += f8;
                        }
                        f6 = nextFloat7;
                        f8 = checkedNextFloat16;
                        pathDefinition.quadTo(f7, f9, f6, f8);
                        break;
                    } else {
                        return pathDefinition;
                    }
                case 86:
                case 118:
                    float nextFloat8 = textScanner.nextFloat();
                    if (!Float.isNaN(nextFloat8)) {
                        if (i2 == 118) {
                            nextFloat8 += f8;
                        }
                        f8 = nextFloat8;
                        pathDefinition.lineTo(f6, f8);
                        f9 = f8;
                        break;
                    } else {
                        return pathDefinition;
                    }
                case 90:
                case 122:
                    pathDefinition.close();
                    f6 = f10;
                    f7 = f6;
                    f8 = f11;
                    f9 = f8;
                    break;
                default:
                    return pathDefinition;
            }
            textScanner.skipCommaWhitespace();
            if (textScanner.empty()) {
                return pathDefinition;
            }
            if (textScanner.hasLetter()) {
                i2 = textScanner.nextChar().intValue();
            }
            i = 109;
        }
    }

    public static Set<String> parseRequiredFeatures(String str) {
        TextScanner textScanner = new TextScanner(str);
        HashSet hashSet = new HashSet();
        while (!textScanner.empty()) {
            String nextToken = textScanner.nextToken();
            if (nextToken.startsWith("http://www.w3.org/TR/SVG11/feature#")) {
                hashSet.add(nextToken.substring(35));
            } else {
                hashSet.add("UNSUPPORTED");
            }
            textScanner.skipWhitespace();
        }
        return hashSet;
    }

    public static Set<String> parseSystemLanguage(String str) {
        TextScanner textScanner = new TextScanner(str);
        HashSet hashSet = new HashSet();
        while (!textScanner.empty()) {
            String nextToken = textScanner.nextToken();
            int indexOf = nextToken.indexOf(45);
            if (indexOf != -1) {
                nextToken = nextToken.substring(0, indexOf);
            }
            hashSet.add(new Locale(nextToken, "", "").getLanguage());
            textScanner.skipWhitespace();
        }
        return hashSet;
    }

    public static Set<String> parseRequiredFormats(String str) {
        TextScanner textScanner = new TextScanner(str);
        HashSet hashSet = new HashSet();
        while (!textScanner.empty()) {
            hashSet.add(textScanner.nextToken());
            textScanner.skipWhitespace();
        }
        return hashSet;
    }

    public static String parseFunctionalIRI(String str, String str2) throws SAXException {
        if (str.equals("none")) {
            return null;
        }
        if (!str.startsWith("url(") || !str.endsWith(")")) {
            throw new SAXException("Bad " + str2 + " attribute. Expected \"none\" or \"url()\" format");
        }
        return str.substring(4, str.length() - 1).trim();
    }

    public final void style(Attributes attributes) throws SAXException {
        debug("<style>", new Object[0]);
        if (this.currentElement == null) {
            throw new SAXException("Invalid document. Root element must be <svg>");
        }
        String str = "all";
        boolean z = true;
        for (int i = 0; i < attributes.getLength(); i++) {
            String trim = attributes.getValue(i).trim();
            int i2 = AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$SVGParser$SVGAttr[SVGAttr.fromString(attributes.getLocalName(i)).ordinal()];
            if (i2 == 85) {
                z = trim.equals("text/css");
            } else if (i2 == 86) {
                str = trim;
            }
        }
        if (z && CSSParser.mediaMatches(str)) {
            this.inStyleElement = true;
            return;
        }
        this.ignoring = true;
        this.ignoreDepth = 1;
    }

    public final void parseCSSStyleSheet(String str) throws SAXException {
        this.svgDocument.addCSSRules(new CSSParser(CSSParser.MediaType.screen).parse(str));
    }
}
