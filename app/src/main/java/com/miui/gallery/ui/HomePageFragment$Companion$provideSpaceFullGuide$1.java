package com.miui.gallery.ui;

import android.content.Context;
import android.content.DialogInterface;
import com.miui.gallery.cloud.GalleryMiCloudUtil;
import com.miui.gallery.concurrent.ThreadPool;
import java.util.Optional;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineDispatcher;

/* compiled from: HomePageFragment.kt */
/* loaded from: classes2.dex */
public final class HomePageFragment$Companion$provideSpaceFullGuide$1 extends IntroductionPage<HomePageFragment, Optional<String>> {
    public final /* synthetic */ CoroutineDispatcher $dispatcher;
    public final /* synthetic */ HostProvider<HomePageFragment> $hostProvider;

    /* renamed from: $r8$lambda$8UWn3wYGA-ZybKBCgKp9AiDNDX8 */
    public static /* synthetic */ void m1487$r8$lambda$8UWn3wYGAZybKBCgKp9AiDNDX8(HomePageFragment$Companion$provideSpaceFullGuide$1 homePageFragment$Companion$provideSpaceFullGuide$1, DialogInterface dialogInterface) {
        m1488show$lambda0(homePageFragment$Companion$provideSpaceFullGuide$1, dialogInterface);
    }

    @Override // com.miui.gallery.ui.IntroductionPage
    public boolean prejudge(HomePageFragment host, boolean z) {
        Intrinsics.checkNotNullParameter(host, "host");
        return !z;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public HomePageFragment$Companion$provideSpaceFullGuide$1(HostProvider<HomePageFragment> hostProvider, CoroutineDispatcher coroutineDispatcher) {
        super(hostProvider, coroutineDispatcher);
        this.$hostProvider = hostProvider;
        this.$dispatcher = coroutineDispatcher;
    }

    @Override // com.miui.gallery.ui.IntroductionPage
    public Object prepareInBackground(Continuation<? super Optional<String>> continuation) {
        Optional ofNullable = Optional.ofNullable(GalleryMiCloudUtil.getRate(new GalleryMiCloudUtil.SpaceQueryJob(true).mo1807run((ThreadPool.JobContext) null)));
        Intrinsics.checkNotNullExpressionValue(ofNullable, "ofNullable(GalleryMiCloudUtil.getRate(cloudSpace))");
        return ofNullable;
    }

    @Override // com.miui.gallery.ui.IntroductionPage
    public ShowResult show(HomePageFragment host, Optional<String> param) {
        Intrinsics.checkNotNullParameter(host, "host");
        Intrinsics.checkNotNullParameter(param, "param");
        Context requireContext = host.requireContext();
        Intrinsics.checkNotNullExpressionValue(requireContext, "host.requireContext()");
        if (param.isPresent()) {
            GalleryMiCloudUtil.FullSpaceTipDialogFragment fullSpaceTipDialogFragment = new GalleryMiCloudUtil.FullSpaceTipDialogFragment(requireContext, param.get());
            fullSpaceTipDialogFragment.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.miui.gallery.ui.HomePageFragment$Companion$provideSpaceFullGuide$1$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    HomePageFragment$Companion$provideSpaceFullGuide$1.m1487$r8$lambda$8UWn3wYGAZybKBCgKp9AiDNDX8(HomePageFragment$Companion$provideSpaceFullGuide$1.this, dialogInterface);
                }
            });
            fullSpaceTipDialogFragment.show(host.getParentFragmentManager());
            return ShowResult.SHOWN_N_WAITING;
        }
        return ShowResult.SKIPPED;
    }

    /* renamed from: show$lambda-0 */
    public static final void m1488show$lambda0(HomePageFragment$Companion$provideSpaceFullGuide$1 this$0, DialogInterface dialogInterface) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.scheduleNext(true);
    }
}
