package com.miui.gallery.util;

import com.miui.gallery.util.thread.RxGalleryExecutors;
import io.reactivex.Flowable;
import io.reactivex.FlowableTransformer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import org.reactivestreams.Publisher;

/* loaded from: classes2.dex */
public class RxUtils {
    public static <T> FlowableTransformer<T, T> ioAndMainThread() {
        return new FlowableTransformer<T, T>() { // from class: com.miui.gallery.util.RxUtils.1
            @Override // io.reactivex.FlowableTransformer
            public Publisher<T> apply(Flowable<T> flowable) {
                return flowable.subscribeOn(Schedulers.from(RxGalleryExecutors.getInstance().getUserThreadExecutor())).observeOn(RxGalleryExecutors.getInstance().getUiThreadExecutor().getScheduler(), true);
            }
        };
    }

    public static <T extends List> FlowableTransformer<T, T> emptyListCheck() {
        return RxUtils$$ExternalSyntheticLambda0.INSTANCE;
    }

    public static /* synthetic */ Publisher lambda$emptyListCheck$0(List list) throws Exception {
        return (list == null || list.isEmpty()) ? Flowable.empty() : Flowable.just(list);
    }

    public static /* synthetic */ Publisher lambda$emptyListCheck$1(Flowable flowable) {
        return flowable.flatMap(RxUtils$$ExternalSyntheticLambda1.INSTANCE);
    }
}
