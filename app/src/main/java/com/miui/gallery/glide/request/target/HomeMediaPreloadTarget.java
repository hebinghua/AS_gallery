package com.miui.gallery.glide.request.target;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.miui.gallery.util.concurrent.GalleryDispatchers;
import com.miui.gallery.util.concurrent.GlobalMainScope;
import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;

/* compiled from: HomeMediaPreloadTarget.kt */
/* loaded from: classes2.dex */
public final class HomeMediaPreloadTarget extends CustomTarget<Bitmap> {
    public static final Companion Companion = new Companion(null);
    public static final Handler HANDLER = new Handler(Looper.getMainLooper(), HomeMediaPreloadTarget$$ExternalSyntheticLambda0.INSTANCE);
    public final ArrayPool arrayPool;
    public final long mediaId;
    public final RequestManager requestManager;

    @Override // com.bumptech.glide.request.target.Target
    public void onLoadCleared(Drawable drawable) {
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public HomeMediaPreloadTarget(Context context, long j) {
        super(Integer.MIN_VALUE, Integer.MIN_VALUE);
        Intrinsics.checkNotNullParameter(context, "context");
        this.mediaId = j;
        RequestManager with = Glide.with(context);
        Intrinsics.checkNotNullExpressionValue(with, "with(context)");
        this.requestManager = with;
        ArrayPool arrayPool = Glide.get(context).getArrayPool();
        Intrinsics.checkNotNullExpressionValue(arrayPool, "get(context).arrayPool");
        this.arrayPool = arrayPool;
    }

    @Override // com.bumptech.glide.request.target.Target
    public /* bridge */ /* synthetic */ void onResourceReady(Object obj, Transition transition) {
        onResourceReady((Bitmap) obj, (Transition<? super Bitmap>) transition);
    }

    public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
        Intrinsics.checkNotNullParameter(resource, "resource");
        BuildersKt__Builders_commonKt.launch$default(GlobalMainScope.INSTANCE, GalleryDispatchers.INSTANCE.getMISC(), null, new HomeMediaPreloadTarget$onResourceReady$1(resource, this, null), 2, null);
    }

    public final void clear() {
        this.requestManager.clear(this);
    }

    /* compiled from: HomeMediaPreloadTarget.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    /* renamed from: HANDLER$lambda-0  reason: not valid java name */
    public static final boolean m995HANDLER$lambda0(Message it) {
        Intrinsics.checkNotNullParameter(it, "it");
        if (it.what == 1) {
            Object obj = it.obj;
            Objects.requireNonNull(obj, "null cannot be cast to non-null type com.miui.gallery.glide.request.target.HomeMediaPreloadTarget");
            ((HomeMediaPreloadTarget) obj).clear();
            return true;
        }
        return false;
    }
}
