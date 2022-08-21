package com.miui.gallery.sdk.download.downloader;

import com.miui.gallery.concurrent.PriorityThreadFactory;
import com.miui.gallery.sdk.download.assist.RequestItem;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import miuix.core.util.SoftReferenceSingleton;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class MicroBatchDownloader extends MicroThumbnailDownloader {
    public static final SoftReferenceSingleton<Scheduler> mScheduler = new SoftReferenceSingleton<Scheduler>() { // from class: com.miui.gallery.sdk.download.downloader.MicroBatchDownloader.1
        @Override // miuix.core.util.SoftReferenceSingleton
        /* renamed from: createInstance */
        public Scheduler mo2622createInstance() {
            return Schedulers.from(new ThreadPoolExecutor(4, 4, 10L, TimeUnit.SECONDS, new LinkedBlockingQueue(), new PriorityThreadFactory("micro-batch-download", 10)));
        }
    };

    /* renamed from: $r8$lambda$hs56DYc2IUKu_z_tlQ6-xMQbNZw */
    public static /* synthetic */ Boolean m1315$r8$lambda$hs56DYc2IUKu_z_tlQ6xMQbNZw(MicroBatchDownloader microBatchDownloader, JSONObject jSONObject, RequestItem requestItem) {
        return microBatchDownloader.lambda$doFileDownload$0(jSONObject, requestItem);
    }

    @Override // com.miui.gallery.sdk.download.downloader.MicroThumbnailDownloader, com.miui.gallery.sdk.download.downloader.AbsThumbnailDownloader
    public String getInvokerTag() {
        return "MicroBatchDownloader";
    }

    @Override // com.miui.gallery.sdk.download.downloader.MicroThumbnailDownloader, com.miui.gallery.sdk.download.downloader.AbsThumbnailDownloader
    public String getTag() {
        return "MicroBatchDownloader";
    }

    @Override // com.miui.gallery.sdk.download.downloader.AbsThumbnailDownloader
    public boolean shouldWaitUriLock() {
        return false;
    }

    @Override // com.miui.gallery.sdk.download.downloader.AbsThumbnailDownloader
    public void doFileDownload(List<RequestItem> list, final JSONObject jSONObject) {
        Flowable.fromIterable(list).parallel().runOn(mScheduler.get()).map(new Function() { // from class: com.miui.gallery.sdk.download.downloader.MicroBatchDownloader$$ExternalSyntheticLambda0
            @Override // io.reactivex.functions.Function
            /* renamed from: apply */
            public final Object mo2564apply(Object obj) {
                return MicroBatchDownloader.m1315$r8$lambda$hs56DYc2IUKu_z_tlQ6xMQbNZw(MicroBatchDownloader.this, jSONObject, (RequestItem) obj);
            }
        }).sequential().blockingSubscribe();
    }

    public /* synthetic */ Boolean lambda$doFileDownload$0(JSONObject jSONObject, RequestItem requestItem) throws Exception {
        return Boolean.valueOf(downloadFileItem(requestItem, jSONObject.optJSONObject(requestItem.mDBItem.getRequestId())));
    }
}
