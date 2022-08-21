package com.miui.gallery.arch.livedata;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelUtils;
import java.util.List;
import java.util.concurrent.Executor;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ContentLiveData.kt */
/* loaded from: classes.dex */
public final class ContentLiveDataKt {
    public static /* synthetic */ ContentLiveData contentLiveData$default(ViewModel viewModel, Context context, Uri uri, String[] strArr, String str, String[] strArr2, String str2, Executor executor, Function1 function1, int i, Object obj) {
        Executor executor2;
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
        return contentLiveData(viewModel, context, uri, strArr3, str3, strArr4, str4, executor2, function1);
    }

    public static final <T> ContentLiveData<T> contentLiveData(ViewModel viewModel, Context context, Uri uri, String[] strArr, String str, String[] strArr2, String str2, Executor executor, Function1<? super Cursor, ? extends List<? extends T>> converter) {
        Intrinsics.checkNotNullParameter(viewModel, "<this>");
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(uri, "uri");
        Intrinsics.checkNotNullParameter(executor, "executor");
        Intrinsics.checkNotNullParameter(converter, "converter");
        return (ContentLiveData) ViewModelUtils.closeOnClear(viewModel, new ContentLiveData(context, uri, strArr, str, strArr2, str2, executor, converter));
    }
}
