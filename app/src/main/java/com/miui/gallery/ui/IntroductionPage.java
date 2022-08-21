package com.miui.gallery.ui;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwnerKt;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineDispatcher;
import kotlinx.coroutines.Dispatchers;

/* compiled from: IntroductionPage.kt */
/* loaded from: classes2.dex */
public abstract class IntroductionPage<HOST extends Fragment, PARAM> {
    public static final Companion Companion = new Companion(null);
    public static final boolean DEBUG = false;
    public final CoroutineDispatcher dispatcher;
    public final HostProvider<HOST> hostProvider;
    public IntroductionPage<HOST, ?> next;

    public abstract boolean prejudge(HOST host, boolean z);

    public abstract Object prepareInBackground(Continuation<? super PARAM> continuation);

    public abstract ShowResult show(HOST host, PARAM param);

    public IntroductionPage(HostProvider<HOST> hostProvider, CoroutineDispatcher dispatcher) {
        Intrinsics.checkNotNullParameter(hostProvider, "hostProvider");
        Intrinsics.checkNotNullParameter(dispatcher, "dispatcher");
        this.hostProvider = hostProvider;
        this.dispatcher = dispatcher;
    }

    /* JADX WARN: Removed duplicated region for block: B:10:0x0023  */
    /* JADX WARN: Removed duplicated region for block: B:14:0x003a  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x006d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final java.lang.Object checkAndShow(boolean r9, kotlin.coroutines.Continuation<? super kotlin.Unit> r10) {
        /*
            r8 = this;
            boolean r0 = r10 instanceof com.miui.gallery.ui.IntroductionPage$checkAndShow$1
            if (r0 == 0) goto L13
            r0 = r10
            com.miui.gallery.ui.IntroductionPage$checkAndShow$1 r0 = (com.miui.gallery.ui.IntroductionPage$checkAndShow$1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r3 = r1 & r2
            if (r3 == 0) goto L13
            int r1 = r1 - r2
            r0.label = r1
            goto L18
        L13:
            com.miui.gallery.ui.IntroductionPage$checkAndShow$1 r0 = new com.miui.gallery.ui.IntroductionPage$checkAndShow$1
            r0.<init>(r8, r10)
        L18:
            java.lang.Object r10 = r0.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt__IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r0.label
            r3 = 1
            if (r2 == 0) goto L3a
            if (r2 != r3) goto L32
            boolean r9 = r0.Z$0
            java.lang.Object r0 = r0.L$0
            com.miui.gallery.ui.IntroductionPage r0 = (com.miui.gallery.ui.IntroductionPage) r0
            kotlin.ResultKt.throwOnFailure(r10)
            r6 = r9
            r5 = r10
            r3 = r0
            goto L64
        L32:
            java.lang.IllegalStateException r9 = new java.lang.IllegalStateException
            java.lang.String r10 = "call to 'resume' before 'invoke' with coroutine"
            r9.<init>(r10)
            throw r9
        L3a:
            kotlin.ResultKt.throwOnFailure(r10)
            com.miui.gallery.ui.HostProvider<HOST extends androidx.fragment.app.Fragment> r10 = r8.hostProvider
            androidx.fragment.app.Fragment r10 = r10.provide()
            if (r10 != 0) goto L46
            goto L7f
        L46:
            boolean r10 = r8.prejudge(r10, r9)
            if (r10 == 0) goto L7c
            kotlinx.coroutines.CoroutineDispatcher r10 = r8.dispatcher
            com.miui.gallery.ui.IntroductionPage$checkAndShow$2$param$1 r2 = new com.miui.gallery.ui.IntroductionPage$checkAndShow$2$param$1
            r4 = 0
            r2.<init>(r8, r4)
            r0.L$0 = r8
            r0.Z$0 = r9
            r0.label = r3
            java.lang.Object r10 = kotlinx.coroutines.BuildersKt.withContext(r10, r2, r0)
            if (r10 != r1) goto L61
            return r1
        L61:
            r3 = r8
            r6 = r9
            r5 = r10
        L64:
            com.miui.gallery.ui.HostProvider<HOST extends androidx.fragment.app.Fragment> r9 = r3.hostProvider
            androidx.fragment.app.Fragment r4 = r9.provide()
            if (r4 != 0) goto L6d
            goto L7f
        L6d:
            androidx.lifecycle.LifecycleCoroutineScope r9 = androidx.lifecycle.LifecycleOwnerKt.getLifecycleScope(r4)
            com.miui.gallery.ui.IntroductionPage$checkAndShow$2$1$1 r10 = new com.miui.gallery.ui.IntroductionPage$checkAndShow$2$1$1
            r7 = 0
            r2 = r10
            r2.<init>(r3, r4, r5, r6, r7)
            r9.launchWhenResumed(r10)
            goto L7f
        L7c:
            r8.scheduleNext(r9)
        L7f:
            kotlin.Unit r9 = kotlin.Unit.INSTANCE
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.ui.IntroductionPage.checkAndShow(boolean, kotlin.coroutines.Continuation):java.lang.Object");
    }

    public final void scheduleNext(boolean z) {
        HOST provide;
        if (this.next == null || (provide = this.hostProvider.provide()) == null) {
            return;
        }
        BuildersKt__Builders_commonKt.launch$default(LifecycleOwnerKt.getLifecycleScope(provide), Dispatchers.getMain().mo2585getImmediate(), null, new IntroductionPage$scheduleNext$1$1(this, z, null), 2, null);
    }

    /* compiled from: IntroductionPage.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }

        public final boolean getDEBUG() {
            return IntroductionPage.DEBUG;
        }

        public final <HOST extends Fragment> IntroductionPage<HOST, ?> chain(IntroductionPage<HOST, ?>... pages) {
            Intrinsics.checkNotNullParameter(pages, "pages");
            int i = 0;
            if (!(!(pages.length == 0))) {
                throw new IllegalArgumentException("Failed requirement.".toString());
            }
            IntroductionPage<HOST, ?> introductionPage = pages[0];
            int length = pages.length - 1;
            while (i < length) {
                int i2 = i + 1;
                pages[i].next = pages[i2];
                i = i2;
            }
            return introductionPage;
        }
    }
}
