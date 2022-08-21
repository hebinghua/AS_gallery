package com.miui.gallery.job.workers;

import com.miui.gallery.job.workers.StatisticsWorkerProvider;
import com.miui.gallery.model.dto.PageResults;
import io.reactivex.functions.Consumer;

/* compiled from: R8$$SyntheticClass */
/* loaded from: classes2.dex */
public final /* synthetic */ class StatisticsWorkerProvider$StatisticsReportWorker$$ExternalSyntheticLambda0 implements Consumer {
    public static final /* synthetic */ StatisticsWorkerProvider$StatisticsReportWorker$$ExternalSyntheticLambda0 INSTANCE = new StatisticsWorkerProvider$StatisticsReportWorker$$ExternalSyntheticLambda0();

    @Override // io.reactivex.functions.Consumer
    public final void accept(Object obj) {
        StatisticsWorkerProvider.StatisticsReportWorker.m1001recordMediaTypeCount$lambda8((PageResults) obj);
    }
}
