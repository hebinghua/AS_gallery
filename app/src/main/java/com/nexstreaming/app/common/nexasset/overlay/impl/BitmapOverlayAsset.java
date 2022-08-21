package com.nexstreaming.app.common.nexasset.overlay.impl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.RectF;
import android.util.Log;
import com.nexstreaming.app.common.nexasset.assetpackage.f;
import com.nexstreaming.app.common.nexasset.overlay.AwakeAsset;
import com.nexstreaming.app.common.nexasset.overlay.OverlayMotion;
import com.nexstreaming.app.common.util.b;
import com.nexstreaming.kminternal.nexvideoeditor.LayerRenderer;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/* loaded from: classes3.dex */
public class BitmapOverlayAsset extends AbstractOverlayAsset {
    private static final String LOG_TAG = "BitmapOverlayAsset";
    private static final int MAX_TEX_SIZE = 2000;
    private int height;
    private int width;

    @Override // com.nexstreaming.app.common.nexasset.overlay.OverlayAsset
    public int getDefaultDuration() {
        return 0;
    }

    public BitmapOverlayAsset(f fVar) throws IOException {
        super(fVar);
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        InputStream a = getAssetPackageReader().a(fVar.getFilePath());
        try {
            BitmapFactory.decodeStream(a, null, options);
            b.a(a);
            this.width = options.outWidth;
            this.height = options.outHeight;
        } catch (Throwable th) {
            b.a(a);
            throw th;
        }
    }

    @Override // com.nexstreaming.app.common.nexasset.overlay.OverlayAsset
    public int getIntrinsicWidth() {
        return this.width;
    }

    @Override // com.nexstreaming.app.common.nexasset.overlay.OverlayAsset
    public int getIntrinsicHeight() {
        return this.height;
    }

    /* JADX WARN: Not initialized variable reg: 1, insn: 0x0028: MOVE  (r0 I:??[OBJECT, ARRAY]) = (r1 I:??[OBJECT, ARRAY]), block:B:14:0x0028 */
    private Bitmap loadBitmap(BitmapFactory.Options options) {
        InputStream inputStream;
        Closeable closeable;
        Closeable closeable2 = null;
        try {
            try {
                inputStream = getAssetPackageReader().a(getItemInfo().getFilePath());
                try {
                    Bitmap decodeStream = BitmapFactory.decodeStream(inputStream, null, options);
                    b.a(inputStream);
                    return decodeStream;
                } catch (IOException unused) {
                    Log.e(LOG_TAG, "");
                    b.a(inputStream);
                    return null;
                }
            } catch (Throwable th) {
                th = th;
                closeable2 = closeable;
                b.a(closeable2);
                throw th;
            }
        } catch (IOException unused2) {
            inputStream = null;
        } catch (Throwable th2) {
            th = th2;
            b.a(closeable2);
            throw th;
        }
    }

    @Override // com.nexstreaming.app.common.nexasset.overlay.OverlayAsset
    public AwakeAsset onAwake(LayerRenderer layerRenderer, final RectF rectF, String str, Map<String, String> map) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        while (true) {
            int i = options.inSampleSize;
            if (i >= 16 || (this.width / i <= 2000 && this.height / i <= 2000)) {
                break;
            }
            options.inSampleSize = i * 2;
        }
        final Bitmap loadBitmap = loadBitmap(options);
        return new AwakeAsset() { // from class: com.nexstreaming.app.common.nexasset.overlay.impl.BitmapOverlayAsset.1
            @Override // com.nexstreaming.app.common.nexasset.overlay.AwakeAsset
            public boolean needRendererReawakeOnEditResize() {
                return false;
            }

            @Override // com.nexstreaming.app.common.nexasset.overlay.AwakeAsset
            public void onAsleep(LayerRenderer layerRenderer2) {
            }

            @Override // com.nexstreaming.app.common.nexasset.overlay.AwakeAsset
            public boolean onRefresh(LayerRenderer layerRenderer2, RectF rectF2, String str2) {
                return false;
            }

            @Override // com.nexstreaming.app.common.nexasset.overlay.AwakeAsset
            public void onRender(LayerRenderer layerRenderer2, OverlayMotion overlayMotion, int i2, int i3) {
                RectF rectF2 = rectF;
                float f = rectF2.left;
                if (f == 0.0f && rectF2.top == 0.0f && rectF2.right == 0.0f && rectF2.bottom == 0.0f) {
                    layerRenderer2.a(loadBitmap, 0.0f, 0.0f);
                } else {
                    layerRenderer2.a(loadBitmap, f, rectF2.top, rectF2.right, rectF2.bottom);
                }
            }
        };
    }

    public Bitmap getBitmap(float f, int i) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 1;
        while (true) {
            int i2 = options.inSampleSize;
            if (i2 >= 16 || (this.width / i2 <= 2000 && this.height / i2 <= 2000)) {
                break;
            }
            options.inSampleSize = i2 * 2;
        }
        return loadBitmap(options);
    }
}
