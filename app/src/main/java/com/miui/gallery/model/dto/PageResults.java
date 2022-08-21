package com.miui.gallery.model.dto;

import com.miui.gallery.util.CheckEmptyDataSubscriber;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import java.util.Collection;

/* loaded from: classes2.dex */
public class PageResults<T> implements CheckEmptyDataSubscriber.onCheckEmpty {
    public int fromType;
    public T result;

    public PageResults(T t) {
        this.result = t;
    }

    public PageResults(int i, T t) {
        this.fromType = i;
        this.result = t;
    }

    public int getFromType() {
        return this.fromType;
    }

    public boolean isFromMemory() {
        return this.fromType == 1;
    }

    public boolean isFromDB() {
        return this.fromType == 2;
    }

    public boolean isFromFile() {
        return this.fromType == 4;
    }

    public T getResult() {
        return this.result;
    }

    public static <T> Flowable<PageResults<T>> wrapperDataToPageResult(final int i, Flowable<T> flowable) {
        return (Flowable<PageResults<T>>) flowable.map((Function<T, PageResults<T>>) new Function<T, PageResults<T>>() { // from class: com.miui.gallery.model.dto.PageResults.1
            @Override // io.reactivex.functions.Function
            /* renamed from: apply  reason: collision with other method in class */
            public /* bridge */ /* synthetic */ Object mo2564apply(Object obj) throws Exception {
                return mo2564apply((AnonymousClass1) obj);
            }

            @Override // io.reactivex.functions.Function
            /* renamed from: apply */
            public PageResults<T> mo2564apply(T t) throws Exception {
                return new PageResults<>(i, t);
            }
        });
    }

    @Override // com.miui.gallery.util.CheckEmptyDataSubscriber.onCheckEmpty
    public boolean isEmpty() {
        T t = this.result;
        return (t instanceof Collection) && ((Collection) t).isEmpty();
    }
}
