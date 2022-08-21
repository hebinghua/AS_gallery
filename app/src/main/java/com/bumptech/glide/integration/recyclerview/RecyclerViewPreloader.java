package com.bumptech.glide.integration.recyclerview;

import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.ListPreloader;
import com.bumptech.glide.RequestManager;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: RecyclerViewPreloader.kt */
/* loaded from: classes.dex */
public final class RecyclerViewPreloader<T> extends RecyclerView.OnScrollListener {
    public final RecyclerToListViewScrollListener recyclerScrollListener;

    public RecyclerViewPreloader(RequestManager requestManager, ListPreloader.PreloadModelProvider<T> preloadModelProvider, ListPreloader.PreloadSizeProvider<T> preloadDimensionProvider, int i) {
        Intrinsics.checkNotNullParameter(requestManager, "requestManager");
        Intrinsics.checkNotNullParameter(preloadModelProvider, "preloadModelProvider");
        Intrinsics.checkNotNullParameter(preloadDimensionProvider, "preloadDimensionProvider");
        this.recyclerScrollListener = new RecyclerToListViewScrollListener(new ListPreloader(requestManager, preloadModelProvider, preloadDimensionProvider, i));
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public RecyclerViewPreloader(androidx.fragment.app.Fragment r2, com.bumptech.glide.ListPreloader.PreloadModelProvider<T> r3, com.bumptech.glide.ListPreloader.PreloadSizeProvider<T> r4, int r5) {
        /*
            r1 = this;
            java.lang.String r0 = "fragment"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r2, r0)
            java.lang.String r0 = "preloadModelProvider"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r3, r0)
            java.lang.String r0 = "preloadDimensionProvider"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r4, r0)
            com.bumptech.glide.RequestManager r2 = com.bumptech.glide.Glide.with(r2)
            java.lang.String r0 = "with(fragment)"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r2, r0)
            r1.<init>(r2, r3, r4, r5)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.bumptech.glide.integration.recyclerview.RecyclerViewPreloader.<init>(androidx.fragment.app.Fragment, com.bumptech.glide.ListPreloader$PreloadModelProvider, com.bumptech.glide.ListPreloader$PreloadSizeProvider, int):void");
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
    public void onScrolled(RecyclerView recyclerView, int i, int i2) {
        Intrinsics.checkNotNullParameter(recyclerView, "recyclerView");
        this.recyclerScrollListener.onScrolled(recyclerView, i, i2);
    }
}
