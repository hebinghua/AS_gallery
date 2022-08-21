package com.nexstreaming.app.common.nexasset.overlay.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.RectF;
import android.graphics.drawable.PictureDrawable;
import android.util.Log;
import android.util.LruCache;
import com.larvalabs.svgandroid.SVGParser;
import com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader;
import com.nexstreaming.app.common.nexasset.assetpackage.f;
import com.nexstreaming.app.common.nexasset.overlay.AwakeAsset;
import com.nexstreaming.app.common.nexasset.overlay.OverlaySpec;
import com.nexstreaming.app.common.util.b;
import com.nexstreaming.app.common.util.i;
import com.nexstreaming.kminternal.nexvideoeditor.LayerRenderer;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes3.dex */
public class AnimatedOverlayAsset extends AbstractOverlayAsset {
    private static final String LOG_TAG = "AnimOverlayAsset";
    private static final int MAX_TEX_SIZE = 2000;
    private String baseFile;
    private OverlaySpec overlaySpec;
    private AssetPackageReader reader;
    private float vectorScale;

    public AnimatedOverlayAsset(f fVar) throws IOException, XmlPullParserException {
        super(fVar);
        InputStream inputStream;
        AssetPackageReader assetPackageReader;
        List<OverlaySpec.Layer> list;
        OverlaySpec.Layer layer;
        List<OverlaySpec.Frame> list2;
        InputStream inputStream2 = null;
        try {
            assetPackageReader = getAssetPackageReader();
            try {
                inputStream = assetPackageReader.a(fVar.getFilePath());
                try {
                    OverlaySpec fromInputStream = OverlaySpec.fromInputStream(inputStream);
                    this.overlaySpec = fromInputStream;
                    if ((fromInputStream.width <= 0 || fromInputStream.height <= 0) && (list = fromInputStream.layers) != null && list.size() > 0 && (list2 = (layer = this.overlaySpec.layers.get(0)).frames) != null && list2.size() > 0) {
                        OverlaySpec.Frame frame = layer.frames.get(0);
                        if (!frame.blank) {
                            inputStream2 = assetPackageReader.a(frame.src);
                            Picture b = SVGParser.a(inputStream2).b();
                            this.overlaySpec.width = b.getWidth();
                            this.overlaySpec.height = b.getHeight();
                        }
                    }
                    OverlaySpec overlaySpec = this.overlaySpec;
                    if (overlaySpec.width <= 0 || overlaySpec.height <= 0) {
                        overlaySpec.width = 100;
                        overlaySpec.height = 100;
                    }
                    b.a(inputStream2);
                    b.a(inputStream);
                    b.a(assetPackageReader);
                } catch (Throwable th) {
                    th = th;
                    b.a(inputStream2);
                    b.a(inputStream);
                    b.a(assetPackageReader);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                inputStream = null;
            }
        } catch (Throwable th3) {
            th = th3;
            inputStream = null;
            assetPackageReader = null;
        }
    }

    @Override // com.nexstreaming.app.common.nexasset.overlay.OverlayAsset
    public int getIntrinsicWidth() {
        return this.overlaySpec.width;
    }

    @Override // com.nexstreaming.app.common.nexasset.overlay.OverlayAsset
    public int getIntrinsicHeight() {
        return this.overlaySpec.height;
    }

    @Override // com.nexstreaming.app.common.nexasset.overlay.OverlayAsset
    public int getDefaultDuration() {
        OverlaySpec overlaySpec = this.overlaySpec;
        int i = overlaySpec.duration;
        if (i > 0) {
            return (i * 1000) / overlaySpec.fps;
        }
        int i2 = 0;
        for (OverlaySpec.Layer layer : overlaySpec.layers) {
            int i3 = layer.iterationCount;
            if (i3 < 0) {
                return 0;
            }
            i2 = Math.max(i2, ((layer.duration * i3) * 1000) / this.overlaySpec.fps);
        }
        if (i2 > 30000) {
            return 0;
        }
        if (i2 >= 1000) {
            return i2;
        }
        return 1000;
    }

    @Override // com.nexstreaming.app.common.nexasset.overlay.OverlayAsset
    public AwakeAsset onAwake(LayerRenderer layerRenderer, RectF rectF, String str, Map<String, String> map) {
        AssetPackageReader assetPackageReader;
        try {
            assetPackageReader = getAssetPackageReader();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error getting package reader", e);
            assetPackageReader = null;
        }
        AssetPackageReader assetPackageReader2 = assetPackageReader;
        OverlaySpec overlaySpec = this.overlaySpec;
        int max = 2000 / Math.max(overlaySpec.width, overlaySpec.height);
        return new AwakeAssetImpl(rectF, this.overlaySpec, assetPackageReader2, getItemInfo().getFilePath(), 1.0f);
    }

    /* loaded from: classes3.dex */
    public static class AwakeAssetImpl implements AwakeAsset {
        private final String baseFile;
        private LruCache<String, Bitmap> bitmapCache = new LruCache<String, Bitmap>(41943040) { // from class: com.nexstreaming.app.common.nexasset.overlay.impl.AnimatedOverlayAsset.AwakeAssetImpl.1
            @Override // android.util.LruCache
            public int sizeOf(String str, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };
        private final RectF bounds;
        private final OverlaySpec overlaySpec;
        private final AssetPackageReader reader;
        private final float vectorScale;

        @Override // com.nexstreaming.app.common.nexasset.overlay.AwakeAsset
        public boolean needRendererReawakeOnEditResize() {
            return true;
        }

        @Override // com.nexstreaming.app.common.nexasset.overlay.AwakeAsset
        public boolean onRefresh(LayerRenderer layerRenderer, RectF rectF, String str) {
            return false;
        }

        public AwakeAssetImpl(RectF rectF, OverlaySpec overlaySpec, AssetPackageReader assetPackageReader, String str, float f) {
            this.bounds = new RectF(rectF);
            this.overlaySpec = overlaySpec;
            this.reader = assetPackageReader;
            this.baseFile = str;
            this.vectorScale = f;
        }

        @Override // com.nexstreaming.app.common.nexasset.overlay.AwakeAsset
        public void onAsleep(LayerRenderer layerRenderer) {
            this.bitmapCache.evictAll();
            b.a(this.reader);
        }

        /* JADX WARN: Removed duplicated region for block: B:15:0x003d  */
        /* JADX WARN: Removed duplicated region for block: B:16:0x003f  */
        /* JADX WARN: Removed duplicated region for block: B:19:0x004d  */
        /* JADX WARN: Removed duplicated region for block: B:28:0x005e  */
        /* JADX WARN: Removed duplicated region for block: B:32:0x006d  */
        /* JADX WARN: Removed duplicated region for block: B:37:0x0084  */
        /* JADX WARN: Removed duplicated region for block: B:46:0x009e A[ADDED_TO_REGION, SYNTHETIC] */
        @Override // com.nexstreaming.app.common.nexasset.overlay.AwakeAsset
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void onRender(com.nexstreaming.kminternal.nexvideoeditor.LayerRenderer r10, com.nexstreaming.app.common.nexasset.overlay.OverlayMotion r11, int r12, int r13) {
            /*
                r9 = this;
                com.nexstreaming.app.common.nexasset.overlay.OverlaySpec r11 = r9.overlaySpec
                if (r11 != 0) goto L5
                return
            L5:
                int r11 = r10.g()
                int r11 = r11 - r12
                com.nexstreaming.app.common.nexasset.overlay.OverlaySpec r12 = r9.overlaySpec
                int r13 = r12.fps
                int r11 = r11 * r13
                int r11 = r11 / 1000
                java.util.List<com.nexstreaming.app.common.nexasset.overlay.OverlaySpec$Layer> r12 = r12.layers
                int r12 = r12.size()
                r13 = 0
                r0 = r13
            L19:
                if (r0 >= r12) goto La2
                com.nexstreaming.app.common.nexasset.overlay.OverlaySpec r1 = r9.overlaySpec
                java.util.List<com.nexstreaming.app.common.nexasset.overlay.OverlaySpec$Layer> r1 = r1.layers
                java.lang.Object r1 = r1.get(r0)
                com.nexstreaming.app.common.nexasset.overlay.OverlaySpec$Layer r1 = (com.nexstreaming.app.common.nexasset.overlay.OverlaySpec.Layer) r1
                int r2 = r1.iterationCount
                r3 = 1
                if (r2 < 0) goto L34
                int r4 = r1.duration
                int r5 = r2 * r4
                int r5 = r5 - r3
                if (r11 <= r5) goto L34
                int r2 = r2 * r4
                int r2 = r2 - r3
                goto L35
            L34:
                r2 = r11
            L35:
                int r4 = r1.duration
                int r5 = r2 / r4
                r6 = 2
                int r5 = r5 % r6
                if (r5 != 0) goto L3f
                r5 = r3
                goto L40
            L3f:
                r5 = r13
            L40:
                int r2 = r2 % r4
                int[] r4 = com.nexstreaming.app.common.nexasset.overlay.impl.AnimatedOverlayAsset.AnonymousClass1.$SwitchMap$com$nexstreaming$app$common$nexasset$overlay$OverlaySpec$AnimDirection
                com.nexstreaming.app.common.nexasset.overlay.OverlaySpec$AnimDirection r7 = r1.direction
                int r7 = r7.ordinal()
                r4 = r4[r7]
                if (r4 == r6) goto L5e
                r6 = 3
                if (r4 == r6) goto L59
                r6 = 4
                if (r4 == r6) goto L54
                goto L62
            L54:
                if (r5 == 0) goto L62
                int r4 = r1.duration
                goto L60
            L59:
                if (r5 != 0) goto L62
                int r4 = r1.duration
                goto L60
            L5e:
                int r4 = r1.duration
            L60:
                int r2 = r4 - r2
            L62:
                java.util.List<com.nexstreaming.app.common.nexasset.overlay.OverlaySpec$Frame> r4 = r1.frames
                int r4 = r4.size()
                r5 = 0
                r6 = r13
                r7 = r6
            L6b:
                if (r6 >= r4) goto L82
                java.util.List<com.nexstreaming.app.common.nexasset.overlay.OverlaySpec$Frame> r5 = r1.frames
                java.lang.Object r5 = r5.get(r6)
                com.nexstreaming.app.common.nexasset.overlay.OverlaySpec$Frame r5 = (com.nexstreaming.app.common.nexasset.overlay.OverlaySpec.Frame) r5
                if (r2 > r7) goto L78
                goto L82
            L78:
                int r8 = r5.hold
                int r8 = java.lang.Math.max(r3, r8)
                int r7 = r7 + r8
                int r6 = r6 + 1
                goto L6b
            L82:
                if (r5 == 0) goto L9e
                boolean r1 = r5.blank
                if (r1 != 0) goto L9e
                java.lang.String r1 = r5.src
                android.graphics.Bitmap r3 = r9.getImage(r1)
                if (r3 == 0) goto L9e
                android.graphics.RectF r1 = r9.bounds
                float r4 = r1.left
                float r5 = r1.top
                float r6 = r1.right
                float r7 = r1.bottom
                r2 = r10
                r2.a(r3, r4, r5, r6, r7)
            L9e:
                int r0 = r0 + 1
                goto L19
            La2:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.app.common.nexasset.overlay.impl.AnimatedOverlayAsset.AwakeAssetImpl.onRender(com.nexstreaming.kminternal.nexvideoeditor.LayerRenderer, com.nexstreaming.app.common.nexasset.overlay.OverlayMotion, int, int):void");
        }

        private Bitmap getImage(String str) {
            if (this.reader == null) {
                return null;
            }
            Bitmap bitmap = this.bitmapCache.get(str);
            if (bitmap != null) {
                return bitmap;
            }
            Bitmap loadImage = loadImage(str);
            if (loadImage != null) {
                this.bitmapCache.put(str, loadImage);
            }
            return loadImage;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r1v0 */
        /* JADX WARN: Type inference failed for: r1v1, types: [java.io.Closeable] */
        /* JADX WARN: Type inference failed for: r1v2 */
        private Bitmap loadImage(String str) {
            InputStream inputStream;
            Bitmap loadBitmap;
            String b = i.b(this.baseFile, str);
            String a = i.a(str);
            ?? r1 = 0;
            try {
                try {
                    inputStream = this.reader.a(b);
                    try {
                        if (a.equalsIgnoreCase("svg")) {
                            loadBitmap = loadSVG(inputStream);
                        } else {
                            loadBitmap = loadBitmap(inputStream);
                        }
                        b.a(inputStream);
                        return loadBitmap;
                    } catch (IOException e) {
                        e = e;
                        Log.e(AnimatedOverlayAsset.LOG_TAG, "Error reading frame image", e);
                        b.a(inputStream);
                        return null;
                    }
                } catch (Throwable th) {
                    th = th;
                    r1 = b;
                    b.a(r1);
                    throw th;
                }
            } catch (IOException e2) {
                e = e2;
                inputStream = null;
            } catch (Throwable th2) {
                th = th2;
                b.a(r1);
                throw th;
            }
        }

        private Bitmap loadBitmap(InputStream inputStream) {
            return BitmapFactory.decodeStream(inputStream);
        }

        private Bitmap loadSVG(InputStream inputStream) {
            PictureDrawable a = SVGParser.a(inputStream).a();
            Bitmap createBitmap = Bitmap.createBitmap((int) Math.floor(this.overlaySpec.width * this.vectorScale), (int) Math.floor(this.overlaySpec.height * this.vectorScale), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(createBitmap);
            a.setBounds(0, 0, createBitmap.getWidth(), createBitmap.getHeight());
            a.draw(canvas);
            return createBitmap;
        }
    }

    /* renamed from: com.nexstreaming.app.common.nexasset.overlay.impl.AnimatedOverlayAsset$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$nexstreaming$app$common$nexasset$overlay$OverlaySpec$AnimDirection;

        static {
            int[] iArr = new int[OverlaySpec.AnimDirection.values().length];
            $SwitchMap$com$nexstreaming$app$common$nexasset$overlay$OverlaySpec$AnimDirection = iArr;
            try {
                iArr[OverlaySpec.AnimDirection.NORMAL.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$nexstreaming$app$common$nexasset$overlay$OverlaySpec$AnimDirection[OverlaySpec.AnimDirection.REVERSE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$nexstreaming$app$common$nexasset$overlay$OverlaySpec$AnimDirection[OverlaySpec.AnimDirection.ALTERNATE.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$nexstreaming$app$common$nexasset$overlay$OverlaySpec$AnimDirection[OverlaySpec.AnimDirection.ALTERNATE_REVERSE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }
}
