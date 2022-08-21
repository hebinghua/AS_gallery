package com.miui.gallery.ui;

import android.content.Context;
import android.content.DialogInterface;
import com.miui.gallery.R;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineDispatcher;

/* compiled from: HomePageFragment.kt */
/* loaded from: classes2.dex */
public final class HomePageFragment$Companion$provideMultiViewGuide$1 extends IntroductionPage<HomePageFragment, Unit> {
    public final /* synthetic */ CoroutineDispatcher $dispatcher;
    public final /* synthetic */ HostProvider<HomePageFragment> $hostProvider;

    public static /* synthetic */ void $r8$lambda$yddjK5BIzePI9nnP8BdkHqJfrTc(HomePageFragment$Companion$provideMultiViewGuide$1 homePageFragment$Companion$provideMultiViewGuide$1, DialogInterface dialogInterface) {
        m1484show$lambda0(homePageFragment$Companion$provideMultiViewGuide$1, dialogInterface);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public HomePageFragment$Companion$provideMultiViewGuide$1(HostProvider<HomePageFragment> hostProvider, CoroutineDispatcher coroutineDispatcher) {
        super(hostProvider, coroutineDispatcher);
        this.$hostProvider = hostProvider;
        this.$dispatcher = coroutineDispatcher;
    }

    @Override // com.miui.gallery.ui.IntroductionPage
    public boolean prejudge(HomePageFragment host, boolean z) {
        boolean needShowMultiViewTip;
        Intrinsics.checkNotNullParameter(host, "host");
        needShowMultiViewTip = host.needShowMultiViewTip();
        return needShowMultiViewTip;
    }

    @Override // com.miui.gallery.ui.IntroductionPage
    public Object prepareInBackground(Continuation<? super Unit> continuation) {
        return Unit.INSTANCE;
    }

    @Override // com.miui.gallery.ui.IntroductionPage
    public ShowResult show(HomePageFragment host, Unit param) {
        Intrinsics.checkNotNullParameter(host, "host");
        Intrinsics.checkNotNullParameter(param, "param");
        Context requireContext = host.requireContext();
        Intrinsics.checkNotNullExpressionValue(requireContext, "host.requireContext()");
        RichTipDialogFragment newInstance = RichTipDialogFragment.newInstance(R.raw.zoom, requireContext.getString(R.string.multi_view_tip_title), requireContext.getString(R.string.multi_view_tip_sub_title), null, requireContext.getString(R.string.multi_view_tip_button_text), null);
        newInstance.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.miui.gallery.ui.HomePageFragment$Companion$provideMultiViewGuide$1$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                HomePageFragment$Companion$provideMultiViewGuide$1.$r8$lambda$yddjK5BIzePI9nnP8BdkHqJfrTc(HomePageFragment$Companion$provideMultiViewGuide$1.this, dialogInterface);
            }
        });
        newInstance.show(host.getParentFragmentManager());
        return ShowResult.SHOWN_N_WAITING;
    }

    /* renamed from: show$lambda-0 */
    public static final void m1484show$lambda0(HomePageFragment$Companion$provideMultiViewGuide$1 this$0, DialogInterface dialogInterface) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.scheduleNext(true);
    }
}
