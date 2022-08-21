package com.nexstreaming.app.common.nexasset.overlay.impl;

import android.graphics.RectF;
import android.util.Log;
import com.nexstreaming.app.common.nexasset.assetpackage.f;
import com.nexstreaming.app.common.nexasset.assetpackage.h;
import com.nexstreaming.app.common.nexasset.assetpackage.i;
import com.nexstreaming.app.common.nexasset.overlay.AwakeAsset;
import com.nexstreaming.app.common.nexasset.overlay.OverlayMotion;
import com.nexstreaming.kminternal.kinemaster.config.EditorGlobal;
import com.nexstreaming.kminternal.kinemaster.config.a;
import com.nexstreaming.kminternal.nexvideoeditor.LayerRenderer;
import com.nexstreaming.kminternal.nexvideoeditor.NexEditor;
import java.io.IOException;
import java.util.Map;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes3.dex */
public class RenderItemOverlayAsset extends AbstractOverlayAsset {
    private static final String LOG_TAG = "RenderItemOverlayAsset";
    private int[] effect_id_;
    private int height;
    private int width;

    @Override // com.nexstreaming.app.common.nexasset.overlay.OverlayAsset
    public int getDefaultDuration() {
        return 0;
    }

    public RenderItemOverlayAsset(f fVar) {
        super(fVar);
        this.effect_id_ = new int[]{-1, -1};
        try {
            h a = i.a(a.a().b(), fVar.getId());
            this.width = a.d();
            this.height = a.e();
            Log.d(LOG_TAG, "Read itemdef: " + fVar.getId() + " : w,h=" + a.d() + "," + a.e());
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error reading itemdef: " + fVar.getId(), e);
        } catch (XmlPullParserException e2) {
            Log.e(LOG_TAG, "Error reading itemdef: " + fVar.getId(), e2);
        }
    }

    @Override // com.nexstreaming.app.common.nexasset.overlay.OverlayAsset
    public int getIntrinsicWidth() {
        int i = this.width;
        if (i <= 0 || this.height <= 0) {
            return 700;
        }
        return i;
    }

    @Override // com.nexstreaming.app.common.nexasset.overlay.OverlayAsset
    public int getIntrinsicHeight() {
        int i;
        if (this.width <= 0 || (i = this.height) <= 0) {
            return 700;
        }
        return i;
    }

    @Override // com.nexstreaming.app.common.nexasset.overlay.OverlayAsset
    public AwakeAsset onAwake(LayerRenderer layerRenderer, RectF rectF, String str, Map<String, String> map) {
        return new AwakeAsset(str, rectF) { // from class: com.nexstreaming.app.common.nexasset.overlay.impl.RenderItemOverlayAsset.1
            private RectF currentBound;
            private String currentEffectOptions;
            public final /* synthetic */ RectF val$bounds;
            public final /* synthetic */ String val$effectOptions;

            @Override // com.nexstreaming.app.common.nexasset.overlay.AwakeAsset
            public boolean needRendererReawakeOnEditResize() {
                return false;
            }

            {
                this.val$effectOptions = str;
                this.val$bounds = rectF;
                this.currentEffectOptions = str;
                this.currentBound = rectF;
            }

            @Override // com.nexstreaming.app.common.nexasset.overlay.AwakeAsset
            public void onAsleep(LayerRenderer layerRenderer2) {
                NexEditor a = EditorGlobal.a();
                if (a == null || RenderItemOverlayAsset.this.effect_id_[layerRenderer2.o().id] < 0) {
                    return;
                }
                a.c(RenderItemOverlayAsset.this.effect_id_[layerRenderer2.o().id], layerRenderer2.o().id);
                RenderItemOverlayAsset.this.effect_id_[layerRenderer2.o().id] = -1;
            }

            @Override // com.nexstreaming.app.common.nexasset.overlay.AwakeAsset
            public void onRender(LayerRenderer layerRenderer2, OverlayMotion overlayMotion, int i, int i2) {
                NexEditor a;
                if (RenderItemOverlayAsset.this.effect_id_[layerRenderer2.o().id] < 0 && (a = EditorGlobal.a()) != null) {
                    RenderItemOverlayAsset.this.effect_id_[layerRenderer2.o().id] = a.a(RenderItemOverlayAsset.this.getItemInfo().getId(), layerRenderer2.o().id);
                }
                if (RenderItemOverlayAsset.this.effect_id_[layerRenderer2.o().id] >= 0) {
                    int i3 = RenderItemOverlayAsset.this.effect_id_[layerRenderer2.o().id];
                    String str2 = this.currentEffectOptions;
                    int g = layerRenderer2.g();
                    RectF rectF2 = this.currentBound;
                    layerRenderer2.a(i3, str2, g, i, i2, rectF2.left, rectF2.top, rectF2.right, rectF2.bottom, layerRenderer2.k());
                }
            }

            @Override // com.nexstreaming.app.common.nexasset.overlay.AwakeAsset
            public boolean onRefresh(LayerRenderer layerRenderer2, RectF rectF2, String str2) {
                this.currentEffectOptions = str2;
                this.currentBound = rectF2;
                return true;
            }
        };
    }
}
