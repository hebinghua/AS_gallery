package com.miui.gallery.biz.story.domain;

import com.miui.gallery.arch.function.Either;
import com.miui.gallery.arch.interactor.UseCase;
import com.miui.gallery.biz.story.data.source.CardRepository;
import com.miui.gallery.card.Card;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: RenameCard.kt */
/* loaded from: classes.dex */
public final class RenameCard extends UseCase<UseCase.None, Card> {
    public static final Companion Companion = new Companion(null);
    public final CardRepository repository;

    @Override // com.miui.gallery.arch.interactor.UseCase
    public /* bridge */ /* synthetic */ Object run(Card card, Continuation<? super Either<Object, ? extends UseCase.None>> continuation) {
        return run2(card, (Continuation<? super Either<Object, UseCase.None>>) continuation);
    }

    public RenameCard(CardRepository repository) {
        Intrinsics.checkNotNullParameter(repository, "repository");
        this.repository = repository;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0023  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0035  */
    /* renamed from: run  reason: avoid collision after fix types in other method */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.Object run2(com.miui.gallery.card.Card r5, kotlin.coroutines.Continuation<? super com.miui.gallery.arch.function.Either<java.lang.Object, com.miui.gallery.arch.interactor.UseCase.None>> r6) {
        /*
            r4 = this;
            boolean r0 = r6 instanceof com.miui.gallery.biz.story.domain.RenameCard$run$1
            if (r0 == 0) goto L13
            r0 = r6
            com.miui.gallery.biz.story.domain.RenameCard$run$1 r0 = (com.miui.gallery.biz.story.domain.RenameCard$run$1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r3 = r1 & r2
            if (r3 == 0) goto L13
            int r1 = r1 - r2
            r0.label = r1
            goto L18
        L13:
            com.miui.gallery.biz.story.domain.RenameCard$run$1 r0 = new com.miui.gallery.biz.story.domain.RenameCard$run$1
            r0.<init>(r4, r6)
        L18:
            java.lang.Object r6 = r0.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r0.label
            r3 = 1
            if (r2 == 0) goto L35
            if (r2 != r3) goto L2d
            java.lang.Object r5 = r0.L$0
            com.miui.gallery.card.Card r5 = (com.miui.gallery.card.Card) r5
            kotlin.ResultKt.throwOnFailure(r6)
            goto L45
        L2d:
            java.lang.IllegalStateException r5 = new java.lang.IllegalStateException
            java.lang.String r6 = "call to 'resume' before 'invoke' with coroutine"
            r5.<init>(r6)
            throw r5
        L35:
            kotlin.ResultKt.throwOnFailure(r6)
            com.miui.gallery.biz.story.data.source.CardRepository r6 = r4.repository
            r0.L$0 = r5
            r0.label = r3
            java.lang.Object r6 = r6.updateCard(r5, r0)
            if (r6 != r1) goto L45
            return r1
        L45:
            int r6 = r5.getScenarioId()
            java.lang.String r6 = java.lang.String.valueOf(r6)
            java.lang.String r0 = "scenario_id"
            kotlin.Pair r6 = kotlin.TuplesKt.to(r0, r6)
            java.util.Map r6 = kotlin.collections.MapsKt__MapsJVMKt.mapOf(r6)
            java.lang.String r0 = "assistant"
            java.lang.String r1 = "assistant_card_rename"
            com.miui.gallery.stat.SamplingStatHelper.recordCountEvent(r0, r1, r6)
            java.lang.String r5 = r5.getTitle()
            java.lang.String r6 = "Rename card to: "
            java.lang.String r5 = kotlin.jvm.internal.Intrinsics.stringPlus(r6, r5)
            java.lang.String r6 = "RenameCard"
            com.miui.gallery.util.logger.DefaultLogger.d(r6, r5)
            com.miui.gallery.arch.function.Either$Right r5 = new com.miui.gallery.arch.function.Either$Right
            com.miui.gallery.arch.interactor.UseCase$None r6 = new com.miui.gallery.arch.interactor.UseCase$None
            r6.<init>()
            r5.<init>(r6)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.biz.story.domain.RenameCard.run2(com.miui.gallery.card.Card, kotlin.coroutines.Continuation):java.lang.Object");
    }

    /* compiled from: RenameCard.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
