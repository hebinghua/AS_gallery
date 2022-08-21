package com.nexstreaming.app.common.nexasset.overlay;

import com.nexstreaming.app.common.nexasset.assetpackage.ItemType;
import com.nexstreaming.app.common.nexasset.assetpackage.c;
import com.nexstreaming.app.common.nexasset.assetpackage.f;
import com.nexstreaming.app.common.nexasset.overlay.impl.AnimatedOverlayAsset;
import com.nexstreaming.app.common.nexasset.overlay.impl.BitmapOverlayAsset;
import com.nexstreaming.app.common.nexasset.overlay.impl.RenderItemOverlayAsset;
import com.nexstreaming.app.common.nexasset.overlay.impl.SVGOverlayAsset;
import com.nexstreaming.app.common.util.j;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import org.xmlpull.v1.XmlPullParserException;

/* loaded from: classes3.dex */
public class OverlayAssetFactory {
    private static Map<String, WeakReference<OverlayAsset>> cache = new HashMap();
    private static int deadRefCheck = 0;

    private OverlayAssetFactory() {
    }

    public static OverlayAsset forItem(String str) throws IOException, XmlPullParserException {
        OverlayAsset overlayAsset;
        WeakReference<OverlayAsset> weakReference = cache.get(str);
        if (weakReference == null || (overlayAsset = weakReference.get()) == null) {
            OverlayAsset overlayAsset2 = null;
            f c = c.a().c(str);
            if (c == null) {
                throw new IOException("Asset not found: " + str);
            }
            int i = AnonymousClass1.$SwitchMap$com$nexstreaming$app$common$nexasset$assetpackage$ItemType[c.getType().ordinal()];
            if (i == 1) {
                String filePath = c.getFilePath();
                String substring = filePath.substring(filePath.lastIndexOf(46) + 1);
                if (substring.equalsIgnoreCase("png") || substring.equalsIgnoreCase("jpeg") || substring.equalsIgnoreCase("jpg") || substring.equalsIgnoreCase("webp")) {
                    overlayAsset2 = new BitmapOverlayAsset(c);
                } else if (substring.equalsIgnoreCase("svg")) {
                    overlayAsset2 = new SVGOverlayAsset(c);
                } else if (substring.equalsIgnoreCase("xml")) {
                    overlayAsset2 = new AnimatedOverlayAsset(c);
                } else {
                    throw new IOException("Asset load error: " + str + " (unknown overlay type for '" + filePath + "')");
                }
            } else if (i == 2) {
                return new RenderItemOverlayAsset(c);
            }
            if (overlayAsset2 != null) {
                cache.put(str, new WeakReference<>(overlayAsset2));
                int i2 = deadRefCheck;
                deadRefCheck = i2 + 1;
                if (i2 > 32) {
                    j.a(cache);
                    deadRefCheck = 0;
                }
            }
            return overlayAsset2;
        }
        return overlayAsset;
    }

    /* renamed from: com.nexstreaming.app.common.nexasset.overlay.OverlayAssetFactory$1  reason: invalid class name */
    /* loaded from: classes3.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$nexstreaming$app$common$nexasset$assetpackage$ItemType;

        static {
            int[] iArr = new int[ItemType.values().length];
            $SwitchMap$com$nexstreaming$app$common$nexasset$assetpackage$ItemType = iArr;
            try {
                iArr[ItemType.overlay.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$nexstreaming$app$common$nexasset$assetpackage$ItemType[ItemType.renderitem.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }
}
