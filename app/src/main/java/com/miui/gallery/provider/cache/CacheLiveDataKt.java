package com.miui.gallery.provider.cache;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelUtils;
import java.util.concurrent.Executor;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: CacheLiveData.kt */
/* loaded from: classes2.dex */
public final class CacheLiveDataKt {
    public static /* synthetic */ CacheLiveData cacheLiveData$default(ViewModel viewModel, Context context, Uri uri, String[] strArr, String str, String[] strArr2, String str2, Executor executor, IMediaProcessor iMediaProcessor, int i, Object obj) {
        Executor executor2;
        Uri uri2 = (i & 2) != 0 ? null : uri;
        String[] strArr3 = (i & 4) != 0 ? null : strArr;
        String str3 = (i & 8) != 0 ? null : str;
        String[] strArr4 = (i & 16) != 0 ? null : strArr2;
        String str4 = (i & 32) != 0 ? null : str2;
        if ((i & 64) != 0) {
            Executor THREAD_POOL_EXECUTOR = AsyncTask.THREAD_POOL_EXECUTOR;
            Intrinsics.checkNotNullExpressionValue(THREAD_POOL_EXECUTOR, "THREAD_POOL_EXECUTOR");
            executor2 = THREAD_POOL_EXECUTOR;
        } else {
            executor2 = executor;
        }
        return cacheLiveData(viewModel, context, uri2, strArr3, str3, strArr4, str4, executor2, iMediaProcessor);
    }

    public static final <T extends CacheItem, R> CacheLiveData<T, R> cacheLiveData(ViewModel viewModel, Context context, Uri uri, String[] strArr, String str, String[] strArr2, String str2, Executor executor, IMediaProcessor<? super T, R> processor) {
        Intrinsics.checkNotNullParameter(viewModel, "<this>");
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(executor, "executor");
        Intrinsics.checkNotNullParameter(processor, "processor");
        return (CacheLiveData) ViewModelUtils.closeOnClear(viewModel, new CacheLiveData(context, uri, strArr, str, strArr2, str2, executor, processor));
    }
}
