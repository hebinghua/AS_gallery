package com.miui.gallery.biz.story.domain;

import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.arch.function.Either;
import com.miui.gallery.arch.interactor.UseCase;
import com.miui.gallery.biz.story.data.source.CardRepository;
import com.miui.gallery.card.Card;
import java.util.Set;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: UpdateCard.kt */
/* loaded from: classes.dex */
public final class UpdateCard extends UseCase<UseCase.None, Params> {
    public static final Companion Companion = new Companion(null);
    public final CardRepository repository;

    @Override // com.miui.gallery.arch.interactor.UseCase
    public /* bridge */ /* synthetic */ Object run(Params params, Continuation<? super Either<Object, ? extends UseCase.None>> continuation) {
        return run2(params, (Continuation<? super Either<Object, UseCase.None>>) continuation);
    }

    public UpdateCard(CardRepository repository) {
        Intrinsics.checkNotNullParameter(repository, "repository");
        this.repository = repository;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0023  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x0032  */
    /* renamed from: run  reason: avoid collision after fix types in other method */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.Object run2(com.miui.gallery.biz.story.domain.UpdateCard.Params r13, kotlin.coroutines.Continuation<? super com.miui.gallery.arch.function.Either<java.lang.Object, com.miui.gallery.arch.interactor.UseCase.None>> r14) {
        /*
            r12 = this;
            boolean r0 = r14 instanceof com.miui.gallery.biz.story.domain.UpdateCard$run$1
            if (r0 == 0) goto L13
            r0 = r14
            com.miui.gallery.biz.story.domain.UpdateCard$run$1 r0 = (com.miui.gallery.biz.story.domain.UpdateCard$run$1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r3 = r1 & r2
            if (r3 == 0) goto L13
            int r1 = r1 - r2
            r0.label = r1
            goto L18
        L13:
            com.miui.gallery.biz.story.domain.UpdateCard$run$1 r0 = new com.miui.gallery.biz.story.domain.UpdateCard$run$1
            r0.<init>(r12, r14)
        L18:
            java.lang.Object r14 = r0.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r0.label
            r3 = 1
            if (r2 == 0) goto L32
            if (r2 != r3) goto L2a
            kotlin.ResultKt.throwOnFailure(r14)
            goto Le1
        L2a:
            java.lang.IllegalStateException r13 = new java.lang.IllegalStateException
            java.lang.String r14 = "call to 'resume' before 'invoke' with coroutine"
            r13.<init>(r14)
            throw r13
        L32:
            kotlin.ResultKt.throwOnFailure(r14)
            com.miui.gallery.card.Card r14 = r13.getCard()
            java.util.Set r2 = r13.getSelectedSha1s()
            boolean r13 = r13.getForceUpdate()
            if (r2 == 0) goto L4c
            boolean r4 = r2.isEmpty()
            if (r4 == 0) goto L4a
            goto L4c
        L4a:
            r4 = 0
            goto L4d
        L4c:
            r4 = r3
        L4d:
            java.lang.String r5 = "UpdateCard"
            java.lang.String r6 = "removeByUser"
            if (r4 != 0) goto Lca
            java.util.List r4 = r14.getAllMediaSha1s()
            java.util.List r7 = r14.getSelectedMediaSha1s()
            if (r7 != 0) goto L62
            java.util.ArrayList r7 = new java.util.ArrayList
            r7.<init>()
        L62:
            java.lang.Boolean r8 = kotlin.coroutines.jvm.internal.Boxing.boxBoolean(r13)
            int r9 = r2.size()
            java.lang.Integer r9 = kotlin.coroutines.jvm.internal.Boxing.boxInt(r9)
            int r10 = r7.size()
            java.lang.Integer r10 = kotlin.coroutines.jvm.internal.Boxing.boxInt(r10)
            java.lang.String r11 = "force update: %s, mSelectedPhotoSha1s.size() is %d, selectedIds.size() is %d"
            com.miui.gallery.util.logger.DefaultLogger.d(r5, r11, r8, r9, r10)
            if (r13 != 0) goto L92
            int r13 = r7.size()
            int r5 = r2.size()
            if (r13 != r5) goto L92
            com.miui.gallery.arch.function.Either$Right r13 = new com.miui.gallery.arch.function.Either$Right
            com.miui.gallery.arch.interactor.UseCase$None r14 = new com.miui.gallery.arch.interactor.UseCase$None
            r14.<init>()
            r13.<init>(r14)
            return r13
        L92:
            if (r4 != 0) goto L9d
            java.util.ArrayList r4 = new java.util.ArrayList
            int r13 = r7.size()
            r4.<init>(r13)
        L9d:
            boolean r13 = r4.isEmpty()
            if (r13 == 0) goto La6
            r4.addAll(r7)
        La6:
            java.util.Iterator r13 = r7.iterator()
        Laa:
            boolean r5 = r13.hasNext()
            if (r5 == 0) goto Lc3
            java.lang.Object r5 = r13.next()
            java.lang.String r5 = (java.lang.String) r5
            boolean r8 = r2.contains(r5)
            if (r8 != 0) goto Laa
            r13.remove()
            r4.remove(r5)
            goto Laa
        Lc3:
            r14.setSelectedMediaSha1s(r7, r6)
            r14.setAllMediaSha1s(r4)
            goto Ld6
        Lca:
            r13 = 0
            r14.setSelectedMediaSha1s(r13, r6)
            r14.setAllMediaSha1s(r13)
            java.lang.String r13 = "force update:mSelectedPhotoSha1s.size() is null, selectedIds.size() is null"
            com.miui.gallery.util.logger.DefaultLogger.d(r5, r13)
        Ld6:
            com.miui.gallery.biz.story.data.source.CardRepository r13 = r12.repository
            r0.label = r3
            java.lang.Object r13 = r13.updateCard(r14, r0)
            if (r13 != r1) goto Le1
            return r1
        Le1:
            com.miui.gallery.arch.function.Either$Right r13 = new com.miui.gallery.arch.function.Either$Right
            com.miui.gallery.arch.interactor.UseCase$None r14 = new com.miui.gallery.arch.interactor.UseCase$None
            r14.<init>()
            r13.<init>(r14)
            return r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.biz.story.domain.UpdateCard.run2(com.miui.gallery.biz.story.domain.UpdateCard$Params, kotlin.coroutines.Continuation):java.lang.Object");
    }

    /* compiled from: UpdateCard.kt */
    /* loaded from: classes.dex */
    public static final class Params {
        public final Card card;
        public final boolean forceUpdate;
        public final Set<String> selectedSha1s;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Params)) {
                return false;
            }
            Params params = (Params) obj;
            return Intrinsics.areEqual(this.card, params.card) && Intrinsics.areEqual(this.selectedSha1s, params.selectedSha1s) && this.forceUpdate == params.forceUpdate;
        }

        public int hashCode() {
            int hashCode = this.card.hashCode() * 31;
            Set<String> set = this.selectedSha1s;
            int hashCode2 = (hashCode + (set == null ? 0 : set.hashCode())) * 31;
            boolean z = this.forceUpdate;
            if (z) {
                z = true;
            }
            int i = z ? 1 : 0;
            int i2 = z ? 1 : 0;
            return hashCode2 + i;
        }

        public String toString() {
            return "Params(card=" + this.card + ", selectedSha1s=" + this.selectedSha1s + ", forceUpdate=" + this.forceUpdate + CoreConstants.RIGHT_PARENTHESIS_CHAR;
        }

        public Params(Card card, Set<String> set, boolean z) {
            Intrinsics.checkNotNullParameter(card, "card");
            this.card = card;
            this.selectedSha1s = set;
            this.forceUpdate = z;
        }

        public final Card getCard() {
            return this.card;
        }

        public final boolean getForceUpdate() {
            return this.forceUpdate;
        }

        public final Set<String> getSelectedSha1s() {
            return this.selectedSha1s;
        }
    }

    /* compiled from: UpdateCard.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }
}
