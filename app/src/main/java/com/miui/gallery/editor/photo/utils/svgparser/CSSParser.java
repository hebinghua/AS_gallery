package com.miui.gallery.editor.photo.utils.svgparser;

import android.util.Log;
import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.editor.photo.utils.svgparser.SVG;
import com.miui.gallery.editor.photo.utils.svgparser.SVGParser;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Marker;
import org.xml.sax.SAXException;

/* loaded from: classes2.dex */
public class CSSParser {
    public boolean inMediaRule = false;
    public MediaType rendererMediaType;

    /* loaded from: classes2.dex */
    public enum AttribOp {
        EXISTS,
        EQUALS,
        INCLUDES,
        DASHMATCH
    }

    /* loaded from: classes2.dex */
    public enum Combinator {
        DESCENDANT,
        CHILD,
        FOLLOWS
    }

    /* loaded from: classes2.dex */
    public enum MediaType {
        all,
        aural,
        braille,
        embossed,
        handheld,
        print,
        projection,
        screen,
        tty,
        tv
    }

    /* loaded from: classes2.dex */
    public static class Attrib {
        public String name;
        public AttribOp operation;
        public String value;

        public Attrib(String str, AttribOp attribOp, String str2) {
            this.name = str;
            this.operation = attribOp;
            this.value = str2;
        }
    }

    /* loaded from: classes2.dex */
    public static class SimpleSelector {
        public Combinator combinator;
        public String tag;
        public List<Attrib> attribs = null;
        public List<String> pseudos = null;

        public SimpleSelector(Combinator combinator, String str) {
            this.combinator = combinator == null ? Combinator.DESCENDANT : combinator;
            this.tag = str;
        }

        public void addAttrib(String str, AttribOp attribOp, String str2) {
            if (this.attribs == null) {
                this.attribs = new ArrayList();
            }
            this.attribs.add(new Attrib(str, attribOp, str2));
        }

        public void addPseudo(String str) {
            if (this.pseudos == null) {
                this.pseudos = new ArrayList();
            }
            this.pseudos.add(str);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            Combinator combinator = this.combinator;
            if (combinator == Combinator.CHILD) {
                sb.append("> ");
            } else if (combinator == Combinator.FOLLOWS) {
                sb.append("+ ");
            }
            String str = this.tag;
            if (str == null) {
                str = Marker.ANY_MARKER;
            }
            sb.append(str);
            List<Attrib> list = this.attribs;
            if (list != null) {
                for (Attrib attrib : list) {
                    sb.append('[');
                    sb.append(attrib.name);
                    int i = AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$CSSParser$AttribOp[attrib.operation.ordinal()];
                    if (i == 1) {
                        sb.append('=');
                        sb.append(attrib.value);
                    } else if (i == 2) {
                        sb.append("~=");
                        sb.append(attrib.value);
                    } else if (i == 3) {
                        sb.append("|=");
                        sb.append(attrib.value);
                    }
                    sb.append(']');
                }
            }
            List<String> list2 = this.pseudos;
            if (list2 != null) {
                for (String str2 : list2) {
                    sb.append(CoreConstants.COLON_CHAR);
                    sb.append(str2);
                }
            }
            return sb.toString();
        }
    }

    /* renamed from: com.miui.gallery.editor.photo.utils.svgparser.CSSParser$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$CSSParser$AttribOp;

        static {
            int[] iArr = new int[AttribOp.values().length];
            $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$CSSParser$AttribOp = iArr;
            try {
                iArr[AttribOp.EQUALS.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$CSSParser$AttribOp[AttribOp.INCLUDES.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$utils$svgparser$CSSParser$AttribOp[AttribOp.DASHMATCH.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class Ruleset {
        public List<Rule> rules = null;

        public void add(Rule rule) {
            if (this.rules == null) {
                this.rules = new ArrayList();
            }
            for (int i = 0; i < this.rules.size(); i++) {
                if (this.rules.get(i).selector.specificity > rule.selector.specificity) {
                    this.rules.add(i, rule);
                    return;
                }
            }
            this.rules.add(rule);
        }

        public void addAll(Ruleset ruleset) {
            if (ruleset.rules == null) {
                return;
            }
            if (this.rules == null) {
                this.rules = new ArrayList(ruleset.rules.size());
            }
            this.rules.addAll(ruleset.rules);
        }

        public String toString() {
            if (this.rules == null) {
                return "";
            }
            StringBuilder sb = new StringBuilder();
            for (Rule rule : this.rules) {
                sb.append(rule.toString());
                sb.append('\n');
            }
            return sb.toString();
        }
    }

    /* loaded from: classes2.dex */
    public static class Rule {
        public Selector selector;
        public SVG.Style style;

        public Rule(Selector selector, SVG.Style style) {
            this.selector = selector;
            this.style = style;
        }

        public String toString() {
            return this.selector + " {}";
        }
    }

    /* loaded from: classes2.dex */
    public static class Selector {
        public List<SimpleSelector> selector = null;
        public int specificity = 0;

        public void add(SimpleSelector simpleSelector) {
            if (this.selector == null) {
                this.selector = new ArrayList();
            }
            this.selector.add(simpleSelector);
        }

        public boolean isEmpty() {
            List<SimpleSelector> list = this.selector;
            return list == null || list.isEmpty();
        }

        public void addedIdAttribute() {
            this.specificity += 10000;
        }

        public void addedAttributeOrPseudo() {
            this.specificity += 100;
        }

        public void addedElement() {
            this.specificity++;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (SimpleSelector simpleSelector : this.selector) {
                sb.append(simpleSelector);
                sb.append(' ');
            }
            sb.append(CoreConstants.LEFT_PARENTHESIS_CHAR);
            sb.append(this.specificity);
            sb.append(CoreConstants.RIGHT_PARENTHESIS_CHAR);
            return sb.toString();
        }
    }

    public CSSParser(MediaType mediaType) {
        this.rendererMediaType = mediaType;
    }

    public Ruleset parse(String str) throws SAXException {
        CSSTextScanner cSSTextScanner = new CSSTextScanner(str);
        cSSTextScanner.skipWhitespace();
        return parseRuleset(cSSTextScanner);
    }

    public static boolean mediaMatches(String str) throws SAXException {
        CSSTextScanner cSSTextScanner = new CSSTextScanner(str);
        cSSTextScanner.skipWhitespace();
        List<MediaType> parseMediaList = parseMediaList(cSSTextScanner);
        if (!cSSTextScanner.empty()) {
            throw new SAXException("Invalid @media type list");
        }
        return mediaMatches(parseMediaList, MediaType.screen);
    }

    public static void warn(Object... objArr) {
        Log.w("AndroidSVG CSSParser", String.format("Ignoring @%s rule", objArr));
    }

    /* loaded from: classes2.dex */
    public static class CSSTextScanner extends SVGParser.TextScanner {
        public CSSTextScanner(String str) {
            super(str.replaceAll("(?s)/\\*.*?\\*/", ""));
        }

        public String nextIdentifier() {
            int scanForIdentifier = scanForIdentifier();
            int i = this.position;
            if (scanForIdentifier == i) {
                return null;
            }
            String substring = this.input.substring(i, scanForIdentifier);
            this.position = scanForIdentifier;
            return substring;
        }

        public final int scanForIdentifier() {
            int i;
            if (empty()) {
                return this.position;
            }
            int i2 = this.position;
            int charAt = this.input.charAt(i2);
            if (charAt == 45) {
                charAt = advanceChar();
            }
            if ((charAt < 65 || charAt > 90) && ((charAt < 97 || charAt > 122) && charAt != 95)) {
                i = i2;
            } else {
                int advanceChar = advanceChar();
                while (true) {
                    if ((advanceChar < 65 || advanceChar > 90) && ((advanceChar < 97 || advanceChar > 122) && !((advanceChar >= 48 && advanceChar <= 57) || advanceChar == 45 || advanceChar == 95))) {
                        break;
                    }
                    advanceChar = advanceChar();
                }
                i = this.position;
            }
            this.position = i2;
            return i;
        }

        /* JADX WARN: Code restructure failed: missing block: B:85:0x0154, code lost:
            if (r4 == null) goto L81;
         */
        /* JADX WARN: Code restructure failed: missing block: B:86:0x0156, code lost:
            r11.add(r4);
         */
        /* JADX WARN: Code restructure failed: missing block: B:87:0x0159, code lost:
            return true;
         */
        /* JADX WARN: Code restructure failed: missing block: B:88:0x015a, code lost:
            r10.position = r0;
         */
        /* JADX WARN: Code restructure failed: missing block: B:89:0x015c, code lost:
            return false;
         */
        /* JADX WARN: Removed duplicated region for block: B:16:0x0036  */
        /* JADX WARN: Removed duplicated region for block: B:17:0x003c  */
        /* JADX WARN: Removed duplicated region for block: B:23:0x0054  */
        /* JADX WARN: Removed duplicated region for block: B:90:0x0154 A[EDGE_INSN: B:90:0x0154->B:85:0x0154 ?: BREAK  , SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public boolean nextSimpleSelector(com.miui.gallery.editor.photo.utils.svgparser.CSSParser.Selector r11) throws org.xml.sax.SAXException {
            /*
                Method dump skipped, instructions count: 349
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.editor.photo.utils.svgparser.CSSParser.CSSTextScanner.nextSimpleSelector(com.miui.gallery.editor.photo.utils.svgparser.CSSParser$Selector):boolean");
        }

        public final String nextAttribValue() {
            if (empty()) {
                return null;
            }
            String nextQuotedString = nextQuotedString();
            return nextQuotedString != null ? nextQuotedString : nextIdentifier();
        }

        public String nextPropertyValue() {
            if (empty()) {
                return null;
            }
            int i = this.position;
            int charAt = this.input.charAt(i);
            int i2 = i;
            while (charAt != -1 && charAt != 59 && charAt != 125 && charAt != 33 && !isEOL(charAt)) {
                if (!isWhitespace(charAt)) {
                    i2 = this.position + 1;
                }
                charAt = advanceChar();
            }
            if (this.position > i) {
                return this.input.substring(i, i2);
            }
            this.position = i;
            return null;
        }
    }

    public static boolean mediaMatches(List<MediaType> list, MediaType mediaType) {
        for (MediaType mediaType2 : list) {
            if (mediaType2 == MediaType.all) {
                return true;
            }
            if (mediaType2 == mediaType) {
                return true;
            }
        }
        return false;
    }

    public static List<MediaType> parseMediaList(CSSTextScanner cSSTextScanner) throws SAXException {
        ArrayList arrayList = new ArrayList();
        while (!cSSTextScanner.empty()) {
            try {
                arrayList.add(MediaType.valueOf(cSSTextScanner.nextToken(CoreConstants.COMMA_CHAR)));
                if (!cSSTextScanner.skipCommaWhitespace()) {
                    break;
                }
            } catch (IllegalArgumentException unused) {
                throw new SAXException("Invalid @media type list");
            }
        }
        return arrayList;
    }

    public final void parseAtRule(Ruleset ruleset, CSSTextScanner cSSTextScanner) throws SAXException {
        String nextIdentifier = cSSTextScanner.nextIdentifier();
        cSSTextScanner.skipWhitespace();
        if (nextIdentifier == null) {
            throw new SAXException("Invalid '@' rule in <style> element");
        }
        if (!this.inMediaRule && nextIdentifier.equals("media")) {
            List<MediaType> parseMediaList = parseMediaList(cSSTextScanner);
            if (!cSSTextScanner.consume('{')) {
                throw new SAXException("Invalid @media rule: missing rule set");
            }
            cSSTextScanner.skipWhitespace();
            if (mediaMatches(parseMediaList, this.rendererMediaType)) {
                this.inMediaRule = true;
                ruleset.addAll(parseRuleset(cSSTextScanner));
                this.inMediaRule = false;
            } else {
                parseRuleset(cSSTextScanner);
            }
            if (!cSSTextScanner.consume('}')) {
                throw new SAXException("Invalid @media rule: expected '}' at end of rule set");
            }
        } else {
            warn(nextIdentifier);
            skipAtRule(cSSTextScanner);
        }
        cSSTextScanner.skipWhitespace();
    }

    public final void skipAtRule(CSSTextScanner cSSTextScanner) {
        int i = 0;
        while (!cSSTextScanner.empty()) {
            int intValue = cSSTextScanner.nextChar().intValue();
            if (intValue == 59 && i == 0) {
                return;
            }
            if (intValue == 123) {
                i++;
            } else if (intValue == 125 && i > 0 && i - 1 == 0) {
                return;
            }
        }
    }

    public final Ruleset parseRuleset(CSSTextScanner cSSTextScanner) throws SAXException {
        Ruleset ruleset = new Ruleset();
        while (!cSSTextScanner.empty()) {
            if (!cSSTextScanner.consume("<!--") && !cSSTextScanner.consume("-->")) {
                if (cSSTextScanner.consume('@')) {
                    parseAtRule(ruleset, cSSTextScanner);
                } else if (!parseRule(ruleset, cSSTextScanner)) {
                    break;
                }
            }
        }
        return ruleset;
    }

    public final boolean parseRule(Ruleset ruleset, CSSTextScanner cSSTextScanner) throws SAXException {
        List<Selector> parseSelectorGroup = parseSelectorGroup(cSSTextScanner);
        if (parseSelectorGroup == null || parseSelectorGroup.isEmpty()) {
            return false;
        }
        if (!cSSTextScanner.consume('{')) {
            throw new SAXException("Malformed rule block in <style> element: missing '{'");
        }
        cSSTextScanner.skipWhitespace();
        SVG.Style parseDeclarations = parseDeclarations(cSSTextScanner);
        cSSTextScanner.skipWhitespace();
        for (Selector selector : parseSelectorGroup) {
            ruleset.add(new Rule(selector, parseDeclarations));
        }
        return true;
    }

    public final List<Selector> parseSelectorGroup(CSSTextScanner cSSTextScanner) throws SAXException {
        if (cSSTextScanner.empty()) {
            return null;
        }
        ArrayList arrayList = new ArrayList(1);
        Selector selector = new Selector();
        while (!cSSTextScanner.empty() && cSSTextScanner.nextSimpleSelector(selector)) {
            if (cSSTextScanner.skipCommaWhitespace()) {
                arrayList.add(selector);
                selector = new Selector();
            }
        }
        if (!selector.isEmpty()) {
            arrayList.add(selector);
        }
        return arrayList;
    }

    public final SVG.Style parseDeclarations(CSSTextScanner cSSTextScanner) throws SAXException {
        SVG.Style style = new SVG.Style();
        do {
            String nextIdentifier = cSSTextScanner.nextIdentifier();
            cSSTextScanner.skipWhitespace();
            if (!cSSTextScanner.consume(CoreConstants.COLON_CHAR)) {
                break;
            }
            cSSTextScanner.skipWhitespace();
            String nextPropertyValue = cSSTextScanner.nextPropertyValue();
            if (nextPropertyValue == null) {
                break;
            }
            cSSTextScanner.skipWhitespace();
            if (cSSTextScanner.consume('!')) {
                cSSTextScanner.skipWhitespace();
                if (!cSSTextScanner.consume("important")) {
                    throw new SAXException("Malformed rule set in <style> element: found unexpected '!'");
                }
                cSSTextScanner.skipWhitespace();
            }
            cSSTextScanner.consume(';');
            SVGParser.processStyleProperty(style, nextIdentifier, nextPropertyValue);
            cSSTextScanner.skipWhitespace();
            if (cSSTextScanner.consume('}')) {
                return style;
            }
        } while (!cSSTextScanner.empty());
        throw new SAXException("Malformed rule set in <style> element");
    }

    public static List<String> parseClassAttribute(String str) throws SAXException {
        CSSTextScanner cSSTextScanner = new CSSTextScanner(str);
        ArrayList arrayList = null;
        while (!cSSTextScanner.empty()) {
            String nextIdentifier = cSSTextScanner.nextIdentifier();
            if (nextIdentifier == null) {
                throw new SAXException("Invalid value for \"class\" attribute: " + str);
            }
            if (arrayList == null) {
                arrayList = new ArrayList();
            }
            arrayList.add(nextIdentifier);
            cSSTextScanner.skipWhitespace();
        }
        return arrayList;
    }
}
