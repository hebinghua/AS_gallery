package com.miui.gallery.widget.recyclerview;

import com.miui.gallery.assistant.jni.filter.BaiduSceneResult;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;

/* compiled from: AsyncListDiffer.kt */
@DebugMetadata(c = "com.miui.gallery.widget.recyclerview.AsyncListDiffer", f = "AsyncListDiffer.kt", l = {BaiduSceneResult.BLACK_WHITE}, m = "submitList")
/* loaded from: classes3.dex */
public final class AsyncListDiffer$submitList$1 extends ContinuationImpl {
    public int I$0;
    public Object L$0;
    public Object L$1;
    public Object L$2;
    public Object L$3;
    public int label;
    public /* synthetic */ Object result;
    public final /* synthetic */ AsyncListDiffer<T> this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AsyncListDiffer$submitList$1(AsyncListDiffer<T> asyncListDiffer, Continuation<? super AsyncListDiffer$submitList$1> continuation) {
        super(continuation);
        this.this$0 = asyncListDiffer;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return this.this$0.submitList(null, false, 0L, null, this);
    }
}
