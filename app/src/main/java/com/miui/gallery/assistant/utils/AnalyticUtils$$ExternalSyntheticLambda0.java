package com.miui.gallery.assistant.utils;

import android.database.Cursor;
import com.miui.gallery.assistant.utils.AnalyticUtils;
import java.util.List;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class AnalyticUtils$$ExternalSyntheticLambda0 implements AnalyticUtils.DataFetcher.DataQueryHandler {
    public static final /* synthetic */ AnalyticUtils$$ExternalSyntheticLambda0 INSTANCE = new AnalyticUtils$$ExternalSyntheticLambda0();

    @Override // com.miui.gallery.assistant.utils.AnalyticUtils.DataFetcher.DataQueryHandler
    public final Object handle(Cursor cursor) {
        List lambda$getImageCropRegion$1;
        lambda$getImageCropRegion$1 = AnalyticUtils.lambda$getImageCropRegion$1(cursor);
        return lambda$getImageCropRegion$1;
    }
}
