package com.miui.gallery.editor.photo.utils.svgparser;

import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.editor.photo.utils.svgparser.CSSParser;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.xml.sax.SAXException;

/* loaded from: classes2.dex */
public class SVG implements Serializable {
    private SVGExternalFileResolver fileResolver;
    private Svg rootElement = null;
    private float renderDPI = 96.0f;
    private CSSParser.Ruleset cssRules = new CSSParser.Ruleset();
    private Map<String, SvgElementBase> idToElementMap = new HashMap();

    /* loaded from: classes2.dex */
    public static class ClipPath extends Group {
        public Boolean clipPathUnitsAreUser;
    }

    /* loaded from: classes2.dex */
    public static class Defs extends Group {
    }

    /* loaded from: classes2.dex */
    public enum GradientSpread {
        pad,
        reflect,
        repeat
    }

    /* loaded from: classes2.dex */
    public interface HasTransform {
        void setTransform(Matrix matrix);
    }

    /* loaded from: classes2.dex */
    public static class Line extends GraphicsElement {
        public Length x1;
        public Length x2;
        public Length y1;
        public Length y2;
    }

    /* loaded from: classes2.dex */
    public static class Marker extends SvgViewBoxContainer {
        public Length markerHeight;
        public boolean markerUnitsAreUser;
        public Length markerWidth;
        public Float orient;
        public Length refX;
        public Length refY;
    }

    /* loaded from: classes2.dex */
    public static class Mask extends SvgConditionalContainer {
        public Length height;
        public Boolean maskContentUnitsAreUser;
        public Boolean maskUnitsAreUser;
        public Length width;
        public Length x;
        public Length y;
    }

    /* loaded from: classes2.dex */
    public interface PathInterface {
        void arcTo(float f, float f2, float f3, boolean z, boolean z2, float f4, float f5);

        void close();

        void cubicTo(float f, float f2, float f3, float f4, float f5, float f6);

        void lineTo(float f, float f2);

        void moveTo(float f, float f2);

        void quadTo(float f, float f2, float f3, float f4);
    }

    /* loaded from: classes2.dex */
    public static class Pattern extends SvgViewBoxContainer {
        public Length height;
        public String href;
        public Boolean patternContentUnitsAreUser;
        public Matrix patternTransform;
        public Boolean patternUnitsAreUser;
        public Length width;
        public Length x;
        public Length y;
    }

    /* loaded from: classes2.dex */
    public static class Polygon extends CloseShapeGraphicsElement {
        public float[] points;
    }

    /* loaded from: classes2.dex */
    public static class Style implements Cloneable, Serializable {
        public CSSClipRect clip;
        public String clipPath;
        public FillRule clipRule;
        public Colour color;
        public TextDirection direction;
        public Boolean display;
        public SvgPaint fill;
        public Float fillOpacity;
        public FillRule fillRule;
        public List<String> fontFamily;
        public Length fontSize;
        public FontStyle fontStyle;
        public Integer fontWeight;
        public String markerEnd;
        public String markerMid;
        public String markerStart;
        public String mask;
        public Float opacity;
        public Boolean overflow;
        public SvgPaint solidColor;
        public Float solidOpacity;
        public long specifiedFlags = 0;
        public SvgPaint stopColor;
        public Float stopOpacity;
        public SvgPaint stroke;
        public Length[] strokeDashArray;
        public Length strokeDashOffset;
        public LineCaps strokeLineCap;
        public LineJoin strokeLineJoin;
        public Float strokeMiterLimit;
        public Float strokeOpacity;
        public Length strokeWidth;
        public TextAnchor textAnchor;
        public TextDecoration textDecoration;
        public VectorEffect vectorEffect;
        public SvgPaint viewportFill;
        public Float viewportFillOpacity;
        public Boolean visibility;

        /* loaded from: classes2.dex */
        public enum FillRule {
            NonZero,
            EvenOdd
        }

        /* loaded from: classes2.dex */
        public enum FontStyle {
            Normal,
            Italic,
            Oblique
        }

        /* loaded from: classes2.dex */
        public enum LineCaps {
            Butt,
            Round,
            Square
        }

        /* loaded from: classes2.dex */
        public enum LineJoin {
            Miter,
            Round,
            Bevel
        }

        /* loaded from: classes2.dex */
        public enum TextAnchor {
            Start,
            Middle,
            End
        }

        /* loaded from: classes2.dex */
        public enum TextDecoration {
            None,
            Underline,
            Overline,
            LineThrough,
            Blink
        }

        /* loaded from: classes2.dex */
        public enum TextDirection {
            LTR,
            RTL
        }

        /* loaded from: classes2.dex */
        public enum VectorEffect {
            None,
            NonScalingStroke
        }
    }

    /* loaded from: classes2.dex */
    public interface SvgConditional {
        void setRequiredExtensions(String str);

        void setRequiredFeatures(Set<String> set);

        void setRequiredFonts(Set<String> set);

        void setRequiredFormats(Set<String> set);

        void setSystemLanguage(Set<String> set);
    }

    /* loaded from: classes2.dex */
    public interface SvgContainer {
        void addChild(SvgObject svgObject) throws SAXException;

        List<SvgObject> getChildren();
    }

    /* loaded from: classes2.dex */
    public static class SvgElement extends SvgElementBase {
        public Box boundingBox = null;
    }

    /* loaded from: classes2.dex */
    public static class SvgElementBase extends SvgObject {
        public String id = null;
        public Boolean spacePreserve = null;
        public Style baseStyle = null;
        public Style style = null;
        public List<String> classNames = null;
    }

    /* loaded from: classes2.dex */
    public static class SvgLinearGradient extends GradientElement {
        public Length x1;
        public Length x2;
        public Length y1;
        public Length y2;
    }

    /* loaded from: classes2.dex */
    public static abstract class SvgPaint implements Cloneable {
    }

    /* loaded from: classes2.dex */
    public static class SvgPreserveAspectRatioContainer extends SvgConditionalContainer {
        public PreserveAspectRatio preserveAspectRatio = null;
    }

    /* loaded from: classes2.dex */
    public static class SvgRadialGradient extends GradientElement {
        public Length cx;
        public Length cy;
        public Length fx;
        public Length fy;
        public Length r;
    }

    /* loaded from: classes2.dex */
    public static class SvgViewBoxContainer extends SvgPreserveAspectRatioContainer {
        public Box viewBox;
    }

    /* loaded from: classes2.dex */
    public static class Switch extends Group {
    }

    /* loaded from: classes2.dex */
    public static class Symbol extends SvgViewBoxContainer {
    }

    /* loaded from: classes2.dex */
    public interface TextChild {
        TextRoot getTextRoot();
    }

    /* loaded from: classes2.dex */
    public static class TextPositionedContainer extends TextContainer {
        public List<Length> dx;
        public List<Length> dy;
        public List<Length> x;
        public List<Length> y;
    }

    /* loaded from: classes2.dex */
    public interface TextRoot {
    }

    /* loaded from: classes2.dex */
    public enum Unit {
        px,
        em,
        ex,
        in,
        cm,
        mm,
        pt,
        pc,
        percent
    }

    /* loaded from: classes2.dex */
    public static class Use extends Group {
        public Length height;
        public String href;
        public Length width;
        public Length x;
        public Length y;
    }

    /* loaded from: classes2.dex */
    public static class View extends SvgViewBoxContainer {
    }

    public static SVG getFromInputStream(InputStream inputStream) throws SVGParseException {
        return new SVGParser().parse(inputStream);
    }

    public String toString() {
        return "SVG{rootElement=\n" + this.rootElement.toString() + '}';
    }

    public void setRootElement(Svg svg) {
        this.rootElement = svg;
    }

    public void addCSSRules(CSSParser.Ruleset ruleset) {
        this.cssRules.addAll(ruleset);
    }

    /* loaded from: classes2.dex */
    public static class Box implements Cloneable, Serializable {
        public float height;
        public float minX;
        public float minY;
        public float width;

        public Box(float f, float f2, float f3, float f4) {
            this.minX = f;
            this.minY = f2;
            this.width = f3;
            this.height = f4;
        }

        public String toString() {
            return "[" + this.minX + " " + this.minY + " " + this.width + " " + this.height + "]";
        }
    }

    /* loaded from: classes2.dex */
    public static class Colour extends SvgPaint {
        public static final Colour BLACK = new Colour(0);
        public int colour;

        public Colour(int i) {
            this.colour = i;
        }

        public String toString() {
            return String.format("#%06x", Integer.valueOf(this.colour));
        }
    }

    /* loaded from: classes2.dex */
    public static class CurrentColor extends SvgPaint {
        public static CurrentColor instance = new CurrentColor();

        public static CurrentColor getInstance() {
            return instance;
        }
    }

    /* loaded from: classes2.dex */
    public static class PaintReference extends SvgPaint {
        public SvgPaint fallback;
        public String href;

        public PaintReference(String str, SvgPaint svgPaint) {
            this.href = str;
            this.fallback = svgPaint;
        }

        public String toString() {
            return this.href + " " + this.fallback;
        }
    }

    /* loaded from: classes2.dex */
    public static class Length implements Cloneable, Serializable {
        public Unit unit;
        public float value;

        public Length(float f, Unit unit) {
            this.value = f;
            this.unit = unit;
        }

        public Length(float f) {
            this.value = f;
            this.unit = Unit.px;
        }

        public float floatValue() {
            return this.value;
        }

        public boolean isNegative() {
            return this.value < 0.0f;
        }

        public String toString() {
            return String.valueOf(this.value) + this.unit;
        }
    }

    /* loaded from: classes2.dex */
    public static class CSSClipRect {
        public Length bottom;
        public Length left;
        public Length right;
        public Length top;

        public CSSClipRect(Length length, Length length2, Length length3, Length length4) {
            this.top = length;
            this.right = length2;
            this.bottom = length3;
            this.left = length4;
        }
    }

    /* loaded from: classes2.dex */
    public static class SvgObject implements Serializable {
        public SVG document;
        public SvgContainer parent;

        public String toString() {
            return getClass().getSimpleName();
        }
    }

    /* loaded from: classes2.dex */
    public static class SvgConditionalElement extends SvgElement implements SvgConditional {
        public Set<String> requiredFeatures = null;
        public String requiredExtensions = null;
        public Set<String> systemLanguage = null;
        public Set<String> requiredFormats = null;
        public Set<String> requiredFonts = null;

        public void setRequiredFeatures(Set<String> set) {
            this.requiredFeatures = set;
        }

        public void setRequiredExtensions(String str) {
            this.requiredExtensions = str;
        }

        public void setSystemLanguage(Set<String> set) {
            this.systemLanguage = set;
        }

        public void setRequiredFormats(Set<String> set) {
            this.requiredFormats = set;
        }

        public void setRequiredFonts(Set<String> set) {
            this.requiredFonts = set;
        }
    }

    /* loaded from: classes2.dex */
    public static class SvgConditionalContainer extends SvgElement implements SvgContainer, SvgConditional {
        public List<SvgObject> children = new ArrayList();
        public Set<String> requiredFeatures = null;
        public String requiredExtensions = null;
        public Set<String> systemLanguage = null;
        public Set<String> requiredFormats = null;
        public Set<String> requiredFonts = null;

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgObject
        public String toString() {
            return "SvgConditionalContainer{children=\n" + this.children.toString() + '}';
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgContainer
        public List<SvgObject> getChildren() {
            return this.children;
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgContainer
        public void addChild(SvgObject svgObject) throws SAXException {
            this.children.add(svgObject);
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgConditional
        public void setRequiredFeatures(Set<String> set) {
            this.requiredFeatures = set;
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgConditional
        public void setRequiredExtensions(String str) {
            this.requiredExtensions = str;
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgConditional
        public void setSystemLanguage(Set<String> set) {
            this.systemLanguage = set;
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgConditional
        public void setRequiredFormats(Set<String> set) {
            this.requiredFormats = set;
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgConditional
        public void setRequiredFonts(Set<String> set) {
            this.requiredFonts = set;
        }
    }

    /* loaded from: classes2.dex */
    public static class Svg extends SvgViewBoxContainer {
        public Length height;
        public String version;
        public Length width;
        public Length x;
        public Length y;

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgConditionalContainer, com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgObject
        public String toString() {
            return "Svg{\n" + super.toString() + "\nx=" + this.x + ", y=" + this.y + ", width=" + this.width + ", height=" + this.height + ", version='" + this.version + CoreConstants.SINGLE_QUOTE_CHAR + '}';
        }
    }

    /* loaded from: classes2.dex */
    public static class Group extends SvgConditionalContainer implements HasTransform {
        public Matrix transform;

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.HasTransform
        public void setTransform(Matrix matrix) {
            this.transform = matrix;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class GraphicsElement extends SvgConditionalElement implements HasTransform {
        public Matrix transform;

        public void setTransform(Matrix matrix) {
            this.transform = matrix;
        }
    }

    /* loaded from: classes2.dex */
    public static abstract class CloseShapeGraphicsElement extends GraphicsElement {
        public static Paint spaint = new Paint();
        public static Paint fpaint = new Paint();

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgConditionalElement, com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgConditional
        public /* bridge */ /* synthetic */ void setRequiredExtensions(String str) {
            super.setRequiredExtensions(str);
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgConditionalElement, com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgConditional
        public /* bridge */ /* synthetic */ void setRequiredFeatures(Set set) {
            super.setRequiredFeatures(set);
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgConditionalElement, com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgConditional
        public /* bridge */ /* synthetic */ void setRequiredFonts(Set set) {
            super.setRequiredFonts(set);
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgConditionalElement, com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgConditional
        public /* bridge */ /* synthetic */ void setRequiredFormats(Set set) {
            super.setRequiredFormats(set);
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgConditionalElement, com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgConditional
        public /* bridge */ /* synthetic */ void setSystemLanguage(Set set) {
            super.setSystemLanguage(set);
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.GraphicsElement, com.miui.gallery.editor.photo.utils.svgparser.SVG.HasTransform
        public /* bridge */ /* synthetic */ void setTransform(Matrix matrix) {
            super.setTransform(matrix);
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgObject
        public /* bridge */ /* synthetic */ String toString() {
            return super.toString();
        }

        static {
            spaint.setStyle(Paint.Style.STROKE);
            spaint.setColor(-1);
            spaint.setAntiAlias(true);
            fpaint.setStyle(Paint.Style.FILL);
            fpaint.setColor(-15658735);
            fpaint.setAntiAlias(true);
        }
    }

    /* loaded from: classes2.dex */
    public static class Path extends CloseShapeGraphicsElement {
        public PathDefinition d;
        private List<List<PointF>> list;
        public Float pathLength;

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.CloseShapeGraphicsElement, com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgObject
        public String toString() {
            return "\nPath{d=" + this.d + ", pathLength=" + this.pathLength + '}';
        }
    }

    /* loaded from: classes2.dex */
    public static class Rect extends CloseShapeGraphicsElement {
        public Length height;
        public Length rx;
        public Length ry;
        public Length width;
        public Length x;
        public Length y;

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.CloseShapeGraphicsElement, com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgObject
        public String toString() {
            return "\nRect{x=" + this.x + ", y=" + this.y + ", width=" + this.width + ", height=" + this.height + ", rx=" + this.rx + ", ry=" + this.ry + '}';
        }
    }

    /* loaded from: classes2.dex */
    public static class Circle extends CloseShapeGraphicsElement {
        public Length cx;
        public Length cy;
        public Length r;

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.CloseShapeGraphicsElement, com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgObject
        public String toString() {
            return "\nCircle{cx=" + this.cx + ", cy=" + this.cy + ", r=" + this.r + '}';
        }
    }

    /* loaded from: classes2.dex */
    public static class Ellipse extends CloseShapeGraphicsElement {
        public Length cx;
        public Length cy;
        public Length rx;
        public Length ry;

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.CloseShapeGraphicsElement, com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgObject
        public String toString() {
            return "\nEllipse{cx=" + this.cx + ", cy=" + this.cy + ", rx=" + this.rx + ", ry=" + this.ry + '}';
        }
    }

    /* loaded from: classes2.dex */
    public static class PolyLine extends GraphicsElement {
        public float[] points;

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgObject
        public String toString() {
            return "\nPolyLine{points=" + Arrays.toString(this.points) + '}';
        }
    }

    /* loaded from: classes2.dex */
    public static class TextContainer extends SvgConditionalContainer {
        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgConditionalContainer, com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgContainer
        public void addChild(SvgObject svgObject) throws SAXException {
            if (svgObject instanceof TextChild) {
                this.children.add(svgObject);
                return;
            }
            throw new SAXException("Text content elements cannot contain " + svgObject + " elements.");
        }
    }

    /* loaded from: classes2.dex */
    public static class Text extends TextPositionedContainer implements TextRoot, HasTransform {
        public Matrix transform;

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.HasTransform
        public void setTransform(Matrix matrix) {
            this.transform = matrix;
        }
    }

    /* loaded from: classes2.dex */
    public static class TSpan extends TextPositionedContainer implements TextChild {
        private TextRoot textRoot;

        public void setTextRoot(TextRoot textRoot) {
            this.textRoot = textRoot;
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.TextChild
        public TextRoot getTextRoot() {
            return this.textRoot;
        }
    }

    /* loaded from: classes2.dex */
    public static class TextSequence extends SvgObject implements TextChild {
        public String text;
        private TextRoot textRoot;

        public TextSequence(String str) {
            this.text = str;
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgObject
        public String toString() {
            return getClass().getSimpleName() + " '" + this.text + "'";
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.TextChild
        public TextRoot getTextRoot() {
            return this.textRoot;
        }
    }

    /* loaded from: classes2.dex */
    public static class TRef extends TextContainer implements TextChild {
        public String href;
        private TextRoot textRoot;

        public void setTextRoot(TextRoot textRoot) {
            this.textRoot = textRoot;
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.TextChild
        public TextRoot getTextRoot() {
            return this.textRoot;
        }
    }

    /* loaded from: classes2.dex */
    public static class TextPath extends TextContainer implements TextChild {
        public String href;
        public Length startOffset;
        private TextRoot textRoot;

        public void setTextRoot(TextRoot textRoot) {
            this.textRoot = textRoot;
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.TextChild
        public TextRoot getTextRoot() {
            return this.textRoot;
        }
    }

    /* loaded from: classes2.dex */
    public static class GradientElement extends SvgElementBase implements SvgContainer {
        public List<SvgObject> children = new ArrayList();
        public Matrix gradientTransform;
        public Boolean gradientUnitsAreUser;
        public String href;
        public GradientSpread spreadMethod;

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgContainer
        public List<SvgObject> getChildren() {
            return this.children;
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgContainer
        public void addChild(SvgObject svgObject) throws SAXException {
            if (svgObject instanceof Stop) {
                this.children.add(svgObject);
                return;
            }
            throw new SAXException("Gradient elements cannot contain " + svgObject + " elements.");
        }
    }

    /* loaded from: classes2.dex */
    public static class Stop extends SvgElementBase implements SvgContainer {
        public Float offset;

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgContainer
        public void addChild(SvgObject svgObject) {
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgContainer
        public List<SvgObject> getChildren() {
            return Collections.emptyList();
        }
    }

    /* loaded from: classes2.dex */
    public static class Image extends SvgPreserveAspectRatioContainer implements HasTransform {
        public Length height;
        public String href;
        public Matrix transform;
        public Length width;
        public Length x;
        public Length y;

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.HasTransform
        public void setTransform(Matrix matrix) {
            this.transform = matrix;
        }
    }

    /* loaded from: classes2.dex */
    public static class SolidColor extends SvgElementBase implements SvgContainer {
        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgContainer
        public void addChild(SvgObject svgObject) {
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.SvgContainer
        public List<SvgObject> getChildren() {
            return Collections.emptyList();
        }
    }

    /* loaded from: classes2.dex */
    public static class PathDefinition implements PathInterface {
        public int commandsLength = 0;
        public int coordsLength = 0;
        public byte[] commands = new byte[8];
        public float[] coords = new float[16];

        public String toString() {
            byte[] bArr;
            StringBuilder sb = new StringBuilder();
            sb.append("{ ");
            for (byte b : this.commands) {
                if (b == 0) {
                    sb.append("M ");
                } else if (b == 1) {
                    sb.append("L ");
                } else {
                    if (b != 2) {
                        if (b == 3) {
                            sb.append("Q ");
                        } else if (b == 4) {
                            sb.append("A ");
                        } else if (b != 8) {
                        }
                    }
                    sb.append("C ");
                }
            }
            sb.append("}");
            return "\nPathDefinition{commands=" + sb.toString() + ", commandsLength=" + this.commandsLength + ", coords=" + Arrays.toString(this.coords) + ", coordsLength=" + this.coordsLength + '}';
        }

        public boolean isEmpty() {
            return this.commandsLength == 0;
        }

        public final void addCommand(byte b) {
            int i = this.commandsLength;
            byte[] bArr = this.commands;
            if (i == bArr.length) {
                byte[] bArr2 = new byte[bArr.length * 2];
                System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
                this.commands = bArr2;
            }
            byte[] bArr3 = this.commands;
            int i2 = this.commandsLength;
            this.commandsLength = i2 + 1;
            bArr3[i2] = b;
        }

        public final void coordsEnsure(int i) {
            float[] fArr = this.coords;
            if (fArr.length < this.coordsLength + i) {
                float[] fArr2 = new float[fArr.length * 2];
                System.arraycopy(fArr, 0, fArr2, 0, fArr.length);
                this.coords = fArr2;
            }
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.PathInterface
        public void moveTo(float f, float f2) {
            addCommand((byte) 0);
            coordsEnsure(2);
            float[] fArr = this.coords;
            int i = this.coordsLength;
            int i2 = i + 1;
            this.coordsLength = i2;
            fArr[i] = f;
            this.coordsLength = i2 + 1;
            fArr[i2] = f2;
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.PathInterface
        public void lineTo(float f, float f2) {
            addCommand((byte) 1);
            coordsEnsure(2);
            float[] fArr = this.coords;
            int i = this.coordsLength;
            int i2 = i + 1;
            this.coordsLength = i2;
            fArr[i] = f;
            this.coordsLength = i2 + 1;
            fArr[i2] = f2;
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.PathInterface
        public void cubicTo(float f, float f2, float f3, float f4, float f5, float f6) {
            addCommand((byte) 2);
            coordsEnsure(6);
            float[] fArr = this.coords;
            int i = this.coordsLength;
            int i2 = i + 1;
            this.coordsLength = i2;
            fArr[i] = f;
            int i3 = i2 + 1;
            this.coordsLength = i3;
            fArr[i2] = f2;
            int i4 = i3 + 1;
            this.coordsLength = i4;
            fArr[i3] = f3;
            int i5 = i4 + 1;
            this.coordsLength = i5;
            fArr[i4] = f4;
            int i6 = i5 + 1;
            this.coordsLength = i6;
            fArr[i5] = f5;
            this.coordsLength = i6 + 1;
            fArr[i6] = f6;
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.PathInterface
        public void quadTo(float f, float f2, float f3, float f4) {
            addCommand((byte) 3);
            coordsEnsure(4);
            float[] fArr = this.coords;
            int i = this.coordsLength;
            int i2 = i + 1;
            this.coordsLength = i2;
            fArr[i] = f;
            int i3 = i2 + 1;
            this.coordsLength = i3;
            fArr[i2] = f2;
            int i4 = i3 + 1;
            this.coordsLength = i4;
            fArr[i3] = f3;
            this.coordsLength = i4 + 1;
            fArr[i4] = f4;
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.PathInterface
        public void arcTo(float f, float f2, float f3, boolean z, boolean z2, float f4, float f5) {
            addCommand((byte) ((z ? 2 : 0) | 4 | (z2 ? 1 : 0)));
            coordsEnsure(5);
            float[] fArr = this.coords;
            int i = this.coordsLength;
            int i2 = i + 1;
            this.coordsLength = i2;
            fArr[i] = f;
            int i3 = i2 + 1;
            this.coordsLength = i3;
            fArr[i2] = f2;
            int i4 = i3 + 1;
            this.coordsLength = i4;
            fArr[i3] = f3;
            int i5 = i4 + 1;
            this.coordsLength = i5;
            fArr[i4] = f4;
            this.coordsLength = i5 + 1;
            fArr[i5] = f5;
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.PathInterface
        public void close() {
            addCommand((byte) 8);
        }

        public void enumeratePath(PathInterface pathInterface) {
            int i;
            int i2 = 0;
            for (int i3 = 0; i3 < this.commandsLength; i3++) {
                byte b = this.commands[i3];
                if (b == 0) {
                    float[] fArr = this.coords;
                    int i4 = i2 + 1;
                    i = i4 + 1;
                    pathInterface.moveTo(fArr[i2], fArr[i4]);
                } else if (b != 1) {
                    if (b == 2) {
                        float[] fArr2 = this.coords;
                        int i5 = i2 + 1;
                        float f = fArr2[i2];
                        int i6 = i5 + 1;
                        float f2 = fArr2[i5];
                        int i7 = i6 + 1;
                        float f3 = fArr2[i6];
                        int i8 = i7 + 1;
                        float f4 = fArr2[i7];
                        int i9 = i8 + 1;
                        float f5 = fArr2[i8];
                        i2 = i9 + 1;
                        pathInterface.cubicTo(f, f2, f3, f4, f5, fArr2[i9]);
                    } else if (b == 3) {
                        float[] fArr3 = this.coords;
                        int i10 = i2 + 1;
                        int i11 = i10 + 1;
                        int i12 = i11 + 1;
                        pathInterface.quadTo(fArr3[i2], fArr3[i10], fArr3[i11], fArr3[i12]);
                        i2 = i12 + 1;
                    } else if (b == 8) {
                        pathInterface.close();
                    } else {
                        boolean z = (b & 2) != 0;
                        boolean z2 = (b & 1) != 0;
                        float[] fArr4 = this.coords;
                        int i13 = i2 + 1;
                        float f6 = fArr4[i2];
                        int i14 = i13 + 1;
                        float f7 = fArr4[i13];
                        int i15 = i14 + 1;
                        float f8 = fArr4[i14];
                        int i16 = i15 + 1;
                        pathInterface.arcTo(f6, f7, f8, z, z2, fArr4[i15], fArr4[i16]);
                        i2 = i16 + 1;
                    }
                } else {
                    float[] fArr5 = this.coords;
                    int i17 = i2 + 1;
                    i = i17 + 1;
                    pathInterface.lineTo(fArr5[i2], fArr5[i17]);
                }
                i2 = i;
            }
        }
    }

    public List<CloseShapeGraphicsElement> getLayoutElements() {
        return getGroupElements(this.rootElement);
    }

    public final List<CloseShapeGraphicsElement> getGroupElements(SvgContainer svgContainer) {
        ArrayList arrayList = new ArrayList();
        for (SvgObject svgObject : svgContainer.getChildren()) {
            if (svgObject instanceof CloseShapeGraphicsElement) {
                arrayList.add((CloseShapeGraphicsElement) svgObject);
            } else if (svgObject instanceof SvgContainer) {
                SvgContainer svgContainer2 = (SvgContainer) svgObject;
                if (svgContainer2.getChildren() != null && svgContainer2.getChildren().size() > 0) {
                    arrayList.addAll(getGroupElements(svgContainer2));
                }
            }
        }
        return arrayList;
    }
}
