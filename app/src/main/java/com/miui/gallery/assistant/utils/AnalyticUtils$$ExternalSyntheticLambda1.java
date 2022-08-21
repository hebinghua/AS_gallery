package com.miui.gallery.assistant.utils;

import android.database.Cursor;
import com.miui.gallery.assistant.utils.AnalyticUtils;
import java.util.List;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class AnalyticUtils$$ExternalSyntheticLambda1 implements AnalyticUtils.DataFetcher.DataQueryHandler {
    public static final /* synthetic */ AnalyticUtils$$ExternalSyntheticLambda1 INSTANCE = new AnalyticUtils$$ExternalSyntheticLambda1();

    @Override // com.miui.gallery.assistant.utils.AnalyticUtils.DataFetcher.DataQueryHandler
    public final Object handle(Cursor cursor) {
        List lambda$getImageCropRegion$0;
        lambda$getImageCropRegion$0 = AnalyticUtils.lambda$getImageCropRegion$0(cursor);
        return lambda$getImageCropRegion$0;
    }
}
