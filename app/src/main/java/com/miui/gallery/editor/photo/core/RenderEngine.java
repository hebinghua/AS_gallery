package com.miui.gallery.editor.photo.core;

import android.content.Context;
import android.graphics.Bitmap;
import com.miui.gallery.util.BaseBitmapUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class RenderEngine {
    public void release() {
    }

    public abstract Bitmap render(Bitmap bitmap, RenderData renderData, boolean z);

    public static Bitmap render(Context context, Bitmap bitmap, List<RenderData> list, RenderEngine[] renderEngineArr) {
        return render(context, bitmap, list, renderEngineArr, false);
    }

    public static Bitmap render(Context context, Bitmap bitmap, List<RenderData> list, RenderEngine[] renderEngineArr, boolean z) {
        if (bitmap == null) {
            DefaultLogger.w("RenderEngine", "no bitmap need to render");
            return null;
        }
        int size = list.size();
        int i = 0;
        Bitmap bitmap2 = bitmap;
        while (i < size) {
            RenderData renderData = list.get(i);
            i++;
            while (true) {
                if (i >= list.size()) {
                    break;
                }
                RenderData renderData2 = list.get(i);
                DefaultLogger.d("RenderEngine", "try merge %s with %s", renderData.getType(), renderData2.getType());
                RenderData merge = renderData.merge(renderData2);
                if (merge != null) {
                    DefaultLogger.d("RenderEngine", "merged");
                    i++;
                    renderData = merge;
                } else {
                    DefaultLogger.d("RenderEngine", "not support");
                    break;
                }
            }
            RenderEngine findEngine = findEngine(context, renderData, renderEngineArr);
            if (findEngine == null) {
                return null;
            }
            Bitmap render = findEngine.render(bitmap2, renderData, z);
            if (render != bitmap2) {
                BaseBitmapUtils.recycleSilently(bitmap2);
                bitmap2 = render;
            }
            if (render == null) {
                DefaultLogger.w("RenderEngine", "render %s failed, return", renderData.getType());
                return null;
            }
            bitmap = render;
        }
        DefaultLogger.w("RenderEngine", "render %d finish", Integer.valueOf(list.size()));
        return bitmap;
    }

    public static RenderEngine findEngine(Context context, RenderData renderData, RenderEngine[] renderEngineArr) {
        SdkProvider provider;
        Effect type = renderData.getType();
        RenderEngine renderEngine = renderEngineArr[type.ordinal()];
        if (renderEngine != null || (provider = SdkManager.INSTANCE.getProvider(type)) == null) {
            return renderEngine;
        }
        RenderEngine createEngine = provider.createEngine(context);
        renderEngineArr[type.ordinal()] = createEngine;
        return createEngine;
    }
}
