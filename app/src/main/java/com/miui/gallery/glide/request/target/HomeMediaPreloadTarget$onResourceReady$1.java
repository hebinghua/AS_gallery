package com.miui.gallery.glide.request.target;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.os.Handler;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.miui.gallery.dao.GalleryLiteEntityManager;
import com.miui.gallery.model.HomeMedia;
import com.miui.gallery.util.logger.DefaultLogger;
import java.nio.ByteBuffer;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref$ObjectRef;
import kotlinx.coroutines.CoroutineScope;

/* compiled from: HomeMediaPreloadTarget.kt */
@DebugMetadata(c = "com.miui.gallery.glide.request.target.HomeMediaPreloadTarget$onResourceReady$1", f = "HomeMediaPreloadTarget.kt", l = {}, m = "invokeSuspend")
/* loaded from: classes2.dex */
public final class HomeMediaPreloadTarget$onResourceReady$1 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
    public final /* synthetic */ Bitmap $resource;
    public int label;
    public final /* synthetic */ HomeMediaPreloadTarget this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public HomeMediaPreloadTarget$onResourceReady$1(Bitmap bitmap, HomeMediaPreloadTarget homeMediaPreloadTarget, Continuation<? super HomeMediaPreloadTarget$onResourceReady$1> continuation) {
        super(2, continuation);
        this.$resource = bitmap;
        this.this$0 = homeMediaPreloadTarget;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        return new HomeMediaPreloadTarget$onResourceReady$1(this.$resource, this.this$0, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
        return ((HomeMediaPreloadTarget$onResourceReady$1) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }

    /* JADX WARN: Finally extract failed */
    /* JADX WARN: Type inference failed for: r3v2, types: [T, java.lang.Object] */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Handler handler;
        ArrayPool arrayPool;
        ArrayPool arrayPool2;
        Handler handler2;
        ArrayPool arrayPool3;
        long j;
        long j2;
        ArrayPool arrayPool4;
        IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED();
        if (this.label == 0) {
            ResultKt.throwOnFailure(obj);
            Ref$ObjectRef ref$ObjectRef = new Ref$ObjectRef();
            try {
                int byteCount = this.$resource.getByteCount();
                arrayPool3 = this.this$0.arrayPool;
                ?? exact = arrayPool3.getExact(byteCount, byte[].class);
                Intrinsics.checkNotNull(exact);
                ref$ObjectRef.element = exact;
                this.$resource.copyPixelsToBuffer(ByteBuffer.wrap((byte[]) exact, 0, byteCount));
                ContentValues contentValues = new ContentValues();
                contentValues.put("thumbnail_blob", (byte[]) ref$ObjectRef.element);
                GalleryLiteEntityManager galleryLiteEntityManager = GalleryLiteEntityManager.getInstance();
                j = this.this$0.mediaId;
                galleryLiteEntityManager.update(HomeMedia.class, contentValues, Intrinsics.stringPlus("media_id = ", Boxing.boxLong(j)), null);
                StringBuilder sb = new StringBuilder();
                sb.append("Persist pixels for media: ");
                j2 = this.this$0.mediaId;
                sb.append(j2);
                sb.append(" with size: ");
                sb.append(byteCount);
                DefaultLogger.v("HomeMediaPreloadTarget", sb.toString());
                byte[] bArr = (byte[]) ref$ObjectRef.element;
                if (bArr != null) {
                    arrayPool4 = this.this$0.arrayPool;
                    arrayPool4.put(bArr);
                }
            } catch (Throwable th) {
                try {
                    DefaultLogger.e("HomeMediaPreloadTarget", th);
                    byte[] bArr2 = (byte[]) ref$ObjectRef.element;
                    if (bArr2 != null) {
                        arrayPool2 = this.this$0.arrayPool;
                        arrayPool2.put(bArr2);
                    }
                } catch (Throwable th2) {
                    byte[] bArr3 = (byte[]) ref$ObjectRef.element;
                    if (bArr3 != null) {
                        arrayPool = this.this$0.arrayPool;
                        arrayPool.put(bArr3);
                    }
                    handler = HomeMediaPreloadTarget.HANDLER;
                    handler.obtainMessage(1, this.this$0).sendToTarget();
                    throw th2;
                }
            }
            handler2 = HomeMediaPreloadTarget.HANDLER;
            handler2.obtainMessage(1, this.this$0).sendToTarget();
            return Unit.INSTANCE;
        }
        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
    }
}
