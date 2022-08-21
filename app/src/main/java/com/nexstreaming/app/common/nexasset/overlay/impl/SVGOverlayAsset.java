package com.nexstreaming.app.common.nexasset.overlay.impl;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.RectF;
import android.util.Log;
import com.larvalabs.svgandroid.b;
import com.nexstreaming.app.common.nexasset.assetpackage.f;
import com.nexstreaming.app.common.nexasset.overlay.AwakeAsset;
import com.nexstreaming.app.common.nexasset.overlay.OverlayMotion;
import com.nexstreaming.kminternal.nexvideoeditor.LayerRenderer;
import java.io.IOException;
import java.util.Map;

/* loaded from: classes3.dex */
public class SVGOverlayAsset extends AbstractOverlayAsset {
    public static final int COLOR_REPLACEMENT_TOLERANCE = 50;
    private static final String LOG_TAG = "SVGOverlayAsset";
    private static final int MAX_TEX_SIZE = 2000;
    private static int serial;
    private int height;
    private int width;

    @Override // com.nexstreaming.app.common.nexasset.overlay.OverlayAsset
    public int getDefaultDuration() {
        return 0;
    }

    public SVGOverlayAsset(f fVar) throws IOException {
        super(fVar);
        b svg = getSVG(null);
        if (svg != null) {
            this.width = svg.b().getWidth();
            this.height = svg.b().getHeight();
        }
    }

    /* JADX WARN: Can't wrap try/catch for region: R(12:1|(10:3|(2:4|(3:6|(5:8|9|(1:11)|12|13)(1:15)|14)(0))|17|18|19|20|21|22|23|24)(1:43)|16|17|18|19|20|21|22|23|24|(1:(0))) */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x006b, code lost:
        r1 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x006d, code lost:
        r1 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x006e, code lost:
        r2 = 0;
        r0 = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0071, code lost:
        r1 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0072, code lost:
        r2 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0074, code lost:
        r7 = move-exception;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0075, code lost:
        r2 = 0;
        r0 = r7;
        r7 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0079, code lost:
        r1 = e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x007a, code lost:
        r7 = null;
        r2 = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x007c, code lost:
        android.util.Log.e(com.nexstreaming.app.common.nexasset.overlay.impl.SVGOverlayAsset.LOG_TAG, r1.getMessage(), r1);
        r2 = r2;
        r7 = r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x008d, code lost:
        com.nexstreaming.app.common.util.b.a(r2);
        com.nexstreaming.app.common.util.b.a(r7);
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0093, code lost:
        throw r0;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v1 */
    /* JADX WARN: Type inference failed for: r2v12, types: [boolean] */
    /* JADX WARN: Type inference failed for: r2v19 */
    /* JADX WARN: Type inference failed for: r2v20 */
    /* JADX WARN: Type inference failed for: r2v3 */
    /* JADX WARN: Type inference failed for: r2v4, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r2v6, types: [java.io.Closeable] */
    /* JADX WARN: Type inference failed for: r2v7 */
    /* JADX WARN: Type inference failed for: r7v0, types: [java.util.Map<java.lang.String, java.lang.String>, java.util.Map] */
    /* JADX WARN: Type inference failed for: r7v11, types: [java.util.Iterator] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private com.larvalabs.svgandroid.b getSVG(java.util.Map<java.lang.String, java.lang.String> r7) throws java.io.IOException {
        /*
            r6 = this;
            r0 = 0
            if (r7 == 0) goto L53
            java.util.Set r7 = r7.entrySet()
            java.util.Iterator r7 = r7.iterator()
            r1 = r0
        Lc:
            boolean r2 = r7.hasNext()
            if (r2 == 0) goto L54
            java.lang.Object r2 = r7.next()
            java.util.Map$Entry r2 = (java.util.Map.Entry) r2
            java.lang.Object r3 = r2.getKey()
            java.lang.String r3 = (java.lang.String) r3
            java.lang.String r4 = "color:svgcolor_"
            boolean r3 = r3.startsWith(r4)
            if (r3 == 0) goto Lc
            java.lang.Object r3 = r2.getKey()
            java.lang.String r3 = (java.lang.String) r3
            java.lang.String r5 = "#"
            java.lang.String r3 = r3.replace(r4, r5)
            int r3 = com.nexstreaming.app.common.util.c.a(r3)
            java.lang.Object r2 = r2.getValue()
            java.lang.String r2 = (java.lang.String) r2
            int r2 = com.nexstreaming.app.common.util.c.a(r2)
            if (r1 != 0) goto L47
            java.util.HashMap r1 = new java.util.HashMap
            r1.<init>()
        L47:
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
            java.lang.Integer r2 = java.lang.Integer.valueOf(r2)
            r1.put(r3, r2)
            goto Lc
        L53:
            r1 = r0
        L54:
            com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader r7 = r6.getAssetPackageReader()     // Catch: java.lang.Throwable -> L74 com.larvalabs.svgandroid.SVGParseException -> L79
            com.nexstreaming.app.common.nexasset.assetpackage.f r2 = r6.getItemInfo()     // Catch: java.lang.Throwable -> L6d com.larvalabs.svgandroid.SVGParseException -> L71
            java.lang.String r2 = r2.getFilePath()     // Catch: java.lang.Throwable -> L6d com.larvalabs.svgandroid.SVGParseException -> L71
            java.io.InputStream r2 = r7.a(r2)     // Catch: java.lang.Throwable -> L6d com.larvalabs.svgandroid.SVGParseException -> L71
            r3 = 50
            com.larvalabs.svgandroid.b r0 = com.larvalabs.svgandroid.SVGParser.a(r2, r1, r3)     // Catch: com.larvalabs.svgandroid.SVGParseException -> L6b java.lang.Throwable -> L8c
            goto L85
        L6b:
            r1 = move-exception
            goto L7c
        L6d:
            r1 = move-exception
            r2 = r0
            r0 = r1
            goto L8d
        L71:
            r1 = move-exception
            r2 = r0
            goto L7c
        L74:
            r7 = move-exception
            r2 = r0
            r0 = r7
            r7 = r2
            goto L8d
        L79:
            r1 = move-exception
            r7 = r0
            r2 = r7
        L7c:
            java.lang.String r3 = "SVGOverlayAsset"
            java.lang.String r4 = r1.getMessage()     // Catch: java.lang.Throwable -> L8c
            android.util.Log.e(r3, r4, r1)     // Catch: java.lang.Throwable -> L8c
        L85:
            com.nexstreaming.app.common.util.b.a(r2)
            com.nexstreaming.app.common.util.b.a(r7)
            return r0
        L8c:
            r0 = move-exception
        L8d:
            com.nexstreaming.app.common.util.b.a(r2)
            com.nexstreaming.app.common.util.b.a(r7)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.app.common.nexasset.overlay.impl.SVGOverlayAsset.getSVG(java.util.Map):com.larvalabs.svgandroid.b");
    }

    @Override // com.nexstreaming.app.common.nexasset.overlay.OverlayAsset
    public int getIntrinsicWidth() {
        return this.width;
    }

    @Override // com.nexstreaming.app.common.nexasset.overlay.OverlayAsset
    public int getIntrinsicHeight() {
        return this.height;
    }

    @Override // com.nexstreaming.app.common.nexasset.overlay.OverlayAsset
    public AwakeAsset onAwake(LayerRenderer layerRenderer, final RectF rectF, String str, Map<String, String> map) {
        final int i = serial;
        serial = i + 1;
        try {
            b svg = getSVG(map);
            if (svg == null) {
                return null;
            }
            int max = 2000 / Math.max(this.width, this.height);
            Picture b = svg.b();
            final Bitmap createBitmap = Bitmap.createBitmap((int) Math.floor(this.width * 1.0f), (int) Math.floor(this.height * 1.0f), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            canvas.scale(1.0f, 1.0f);
            b.draw(canvas);
            Log.d(LOG_TAG, "onAwake OUT : [#" + i + "] actualScale=1.0 bm=" + createBitmap.getWidth() + "x" + createBitmap.getHeight());
            return new AwakeAsset() { // from class: com.nexstreaming.app.common.nexasset.overlay.impl.SVGOverlayAsset.1
                @Override // com.nexstreaming.app.common.nexasset.overlay.AwakeAsset
                public boolean needRendererReawakeOnEditResize() {
                    return true;
                }

                @Override // com.nexstreaming.app.common.nexasset.overlay.AwakeAsset
                public boolean onRefresh(LayerRenderer layerRenderer2, RectF rectF2, String str2) {
                    return false;
                }

                @Override // com.nexstreaming.app.common.nexasset.overlay.AwakeAsset
                public void onAsleep(LayerRenderer layerRenderer2) {
                    Log.d(SVGOverlayAsset.LOG_TAG, "onAsleep [#" + i + "]");
                }

                @Override // com.nexstreaming.app.common.nexasset.overlay.AwakeAsset
                public void onRender(LayerRenderer layerRenderer2, OverlayMotion overlayMotion, int i2, int i3) {
                    Log.d(SVGOverlayAsset.LOG_TAG, "onRender [#" + i + "]=" + rectF.toString());
                    Bitmap bitmap = createBitmap;
                    RectF rectF2 = rectF;
                    layerRenderer2.a(bitmap, rectF2.left, rectF2.top, rectF2.right, rectF2.bottom);
                }
            };
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error loading asset", e);
            return null;
        }
    }
}
