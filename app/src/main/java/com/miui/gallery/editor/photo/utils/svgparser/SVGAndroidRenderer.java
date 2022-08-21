package com.miui.gallery.editor.photo.utils.svgparser;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.PointF;
import com.miui.gallery.editor.photo.utils.svgparser.SVG;
import com.miui.gallery.search.statistics.SearchStatUtils;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class SVGAndroidRenderer {

    /* loaded from: classes2.dex */
    public static class PathConverter implements SVG.PathInterface {
        public float lastX;
        public float lastY;
        public Path subChild;
        public Path path = new Path();
        public List<PointF> list = new ArrayList();
        public List<Path> subPath = new ArrayList();

        public PathConverter(SVG.PathDefinition pathDefinition) {
            if (pathDefinition == null) {
                return;
            }
            pathDefinition.enumeratePath(this);
        }

        public Path getPath() {
            return this.path;
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.PathInterface
        public void moveTo(float f, float f2) {
            Path path = this.subChild;
            if (path != null) {
                path.close();
            }
            Path path2 = new Path();
            this.subChild = path2;
            this.subPath.add(path2);
            this.path.moveTo(f, f2);
            this.subChild.moveTo(f, f2);
            this.list.add(new PointF(f, f2));
            this.lastX = f;
            this.lastY = f2;
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.PathInterface
        public void lineTo(float f, float f2) {
            this.path.lineTo(f, f2);
            this.subChild.lineTo(f, f2);
            this.list.add(new PointF(f, f2));
            this.lastX = f;
            this.lastY = f2;
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.PathInterface
        public void cubicTo(float f, float f2, float f3, float f4, float f5, float f6) {
            this.path.cubicTo(f, f2, f3, f4, f5, f6);
            this.subChild.cubicTo(f, f2, f3, f4, f5, f6);
            this.list.add(new PointF(f, f2));
            this.list.add(new PointF(f3, f4));
            this.list.add(new PointF(f5, f6));
            this.lastX = f5;
            this.lastY = f6;
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.PathInterface
        public void quadTo(float f, float f2, float f3, float f4) {
            this.path.quadTo(f, f2, f3, f4);
            this.subChild.quadTo(f, f2, f3, f4);
            this.list.add(new PointF(f, f2));
            this.list.add(new PointF(f3, f4));
            this.lastX = f3;
            this.lastY = f4;
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.PathInterface
        public void arcTo(float f, float f2, float f3, boolean z, boolean z2, float f4, float f5) {
            SVGAndroidRenderer.arcTo(this.lastX, this.lastY, f, f2, f3, z, z2, f4, f5, this);
            this.list.add(new PointF(f4, f5));
            this.lastX = f4;
            this.lastY = f5;
        }

        @Override // com.miui.gallery.editor.photo.utils.svgparser.SVG.PathInterface
        public void close() {
            this.path.close();
            if (this.subPath != null) {
                this.subChild.close();
                this.subChild = null;
            }
        }
    }

    public static void arcTo(float f, float f2, float f3, float f4, float f5, boolean z, boolean z2, float f6, float f7, SVG.PathInterface pathInterface) {
        float f8;
        SVG.PathInterface pathInterface2;
        double d;
        if (f == f6 && f2 == f7) {
            return;
        }
        if (f3 == 0.0f) {
            f8 = f6;
            pathInterface2 = pathInterface;
        } else if (f4 != 0.0f) {
            float abs = Math.abs(f3);
            float abs2 = Math.abs(f4);
            double radians = (float) Math.toRadians(f5 % 360.0d);
            double cos = Math.cos(radians);
            double sin = Math.sin(radians);
            double d2 = (f - f6) / 2.0d;
            double d3 = (f2 - f7) / 2.0d;
            double d4 = (cos * d2) + (sin * d3);
            double d5 = ((-sin) * d2) + (d3 * cos);
            double d6 = abs * abs;
            double d7 = abs2 * abs2;
            double d8 = d4 * d4;
            double d9 = d5 * d5;
            double d10 = (d8 / d6) + (d9 / d7);
            double d11 = 1.0d;
            if (d10 > 1.0d) {
                abs *= (float) Math.sqrt(d10);
                abs2 *= (float) Math.sqrt(d10);
                d6 = abs * abs;
                d7 = abs2 * abs2;
            }
            double d12 = z == z2 ? -1.0d : 1.0d;
            double d13 = d6 * d7;
            double d14 = d6 * d9;
            double d15 = d7 * d8;
            double d16 = ((d13 - d14) - d15) / (d14 + d15);
            if (d16 < SearchStatUtils.POW) {
                d16 = 0.0d;
            }
            double sqrt = d12 * Math.sqrt(d16);
            double d17 = abs;
            double d18 = abs2;
            double d19 = ((d17 * d5) / d18) * sqrt;
            float f9 = abs;
            float f10 = abs2;
            double d20 = sqrt * (-((d18 * d4) / d17));
            double d21 = ((f + f6) / 2.0d) + ((cos * d19) - (sin * d20));
            double d22 = ((f2 + f7) / 2.0d) + (sin * d19) + (cos * d20);
            double d23 = (d4 - d19) / d17;
            double d24 = (d5 - d20) / d18;
            double d25 = ((-d4) - d19) / d17;
            double d26 = ((-d5) - d20) / d18;
            double d27 = (d23 * d23) + (d24 * d24);
            double degrees = Math.toDegrees((d24 < SearchStatUtils.POW ? -1.0d : 1.0d) * Math.acos(d23 / Math.sqrt(d27)));
            double sqrt2 = Math.sqrt(d27 * ((d25 * d25) + (d26 * d26)));
            double d28 = (d23 * d25) + (d24 * d26);
            if ((d23 * d26) - (d24 * d25) < SearchStatUtils.POW) {
                d11 = -1.0d;
            }
            double degrees2 = Math.toDegrees(d11 * Math.acos(d28 / sqrt2));
            if (z2 || degrees2 <= SearchStatUtils.POW) {
                d = 360.0d;
                if (z2 && degrees2 < SearchStatUtils.POW) {
                    degrees2 += 360.0d;
                }
            } else {
                d = 360.0d;
                degrees2 -= 360.0d;
            }
            float[] arcToBeziers = arcToBeziers(degrees % d, degrees2 % d);
            Matrix matrix = new Matrix();
            matrix.postScale(f9, f10);
            matrix.postRotate(f5);
            matrix.postTranslate((float) d21, (float) d22);
            matrix.mapPoints(arcToBeziers);
            arcToBeziers[arcToBeziers.length - 2] = f6;
            arcToBeziers[arcToBeziers.length - 1] = f7;
            for (int i = 0; i < arcToBeziers.length; i += 6) {
                pathInterface.cubicTo(arcToBeziers[i], arcToBeziers[i + 1], arcToBeziers[i + 2], arcToBeziers[i + 3], arcToBeziers[i + 4], arcToBeziers[i + 5]);
            }
            return;
        } else {
            pathInterface2 = pathInterface;
            f8 = f6;
        }
        pathInterface2.lineTo(f8, f7);
    }

    public static float[] arcToBeziers(double d, double d2) {
        float radians;
        int ceil = (int) Math.ceil(Math.abs(d2) / 90.0d);
        double radians2 = Math.toRadians(d);
        double radians3 = (float) (Math.toRadians(d2) / ceil);
        double d3 = radians3 / 2.0d;
        double sin = (Math.sin(d3) * 1.3333333333333333d) / (Math.cos(d3) + 1.0d);
        float[] fArr = new float[ceil * 6];
        int i = 0;
        int i2 = 0;
        while (i < ceil) {
            double d4 = (i * radians) + radians2;
            double cos = Math.cos(d4);
            double sin2 = Math.sin(d4);
            int i3 = i2 + 1;
            int i4 = ceil;
            double d5 = radians2;
            fArr[i2] = (float) (cos - (sin * sin2));
            int i5 = i3 + 1;
            fArr[i3] = (float) (sin2 + (cos * sin));
            double d6 = d4 + radians3;
            double cos2 = Math.cos(d6);
            double sin3 = Math.sin(d6);
            int i6 = i5 + 1;
            fArr[i5] = (float) ((sin * sin3) + cos2);
            int i7 = i6 + 1;
            fArr[i6] = (float) (sin3 - (sin * cos2));
            int i8 = i7 + 1;
            fArr[i7] = (float) cos2;
            fArr[i8] = (float) sin3;
            i++;
            radians2 = d5;
            i2 = i8 + 1;
            ceil = i4;
        }
        return fArr;
    }
}
