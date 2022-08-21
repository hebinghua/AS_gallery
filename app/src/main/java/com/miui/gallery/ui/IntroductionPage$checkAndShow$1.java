package com.miui.gallery.ui;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;

/* compiled from: IntroductionPage.kt */
@DebugMetadata(c = "com.miui.gallery.ui.IntroductionPage", f = "IntroductionPage.kt", l = {42}, m = "checkAndShow")
/* loaded from: classes2.dex */
public final class IntroductionPage$checkAndShow$1 extends ContinuationImpl {
    public Object L$0;
    public boolean Z$0;
    public int label;
    public /* synthetic */ Object result;
    public final /* synthetic */ IntroductionPage<HOST, PARAM> this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public IntroductionPage$checkAndShow$1(IntroductionPage<HOST, PARAM> introductionPage, Continuation<? super IntroductionPage$checkAndShow$1> continuation) {
        super(continuation);
        this.this$0 = introductionPage;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        return this.this$0.checkAndShow(false, this);
    }
}
