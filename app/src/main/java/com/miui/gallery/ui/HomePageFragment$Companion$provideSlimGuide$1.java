package com.miui.gallery.ui;

import android.content.DialogInterface;
import com.miui.gallery.cleaner.BaseScanner;
import com.miui.gallery.cleaner.ScanResult;
import com.miui.gallery.cleaner.ScannerManager;
import com.miui.gallery.cleaner.SlimTipUtil;
import com.miui.gallery.cleaner.slim.SlimScanner;
import com.miui.gallery.cloud.syncstate.SyncStateManager;
import com.miui.gallery.cloud.syncstate.SyncStateUtil;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Objects;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineDispatcher;

/* compiled from: HomePageFragment.kt */
/* loaded from: classes2.dex */
public final class HomePageFragment$Companion$provideSlimGuide$1 extends IntroductionPage<HomePageFragment, Boolean> {
    public final /* synthetic */ CoroutineDispatcher $dispatcher;
    public final /* synthetic */ HostProvider<HomePageFragment> $hostProvider;

    /* renamed from: $r8$lambda$K8N0WVf6_Vrx8PazlrjlO1Rm-j0 */
    public static /* synthetic */ void m1485$r8$lambda$K8N0WVf6_Vrx8PazlrjlO1Rmj0(HomePageFragment$Companion$provideSlimGuide$1 homePageFragment$Companion$provideSlimGuide$1, DialogInterface dialogInterface) {
        m1486show$lambda0(homePageFragment$Companion$provideSlimGuide$1, dialogInterface);
    }

    @Override // com.miui.gallery.ui.IntroductionPage
    public boolean prejudge(HomePageFragment host, boolean z) {
        Intrinsics.checkNotNullParameter(host, "host");
        return !z;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public HomePageFragment$Companion$provideSlimGuide$1(HostProvider<HomePageFragment> hostProvider, CoroutineDispatcher coroutineDispatcher) {
        super(hostProvider, coroutineDispatcher);
        this.$hostProvider = hostProvider;
        this.$dispatcher = coroutineDispatcher;
    }

    @Override // com.miui.gallery.ui.IntroductionPage
    public /* bridge */ /* synthetic */ ShowResult show(HomePageFragment homePageFragment, Boolean bool) {
        return show(homePageFragment, bool.booleanValue());
    }

    @Override // com.miui.gallery.ui.IntroductionPage
    public Object prepareInBackground(Continuation<? super Boolean> continuation) {
        if (!SlimTipUtil.isSlimEntranceEnable("homepage-dialog").booleanValue() && !SlimTipUtil.isSlimEntranceEnable("text-link").booleanValue()) {
            return Boxing.boxBoolean(false);
        }
        SyncStateUtil.CloudSpaceInfo mo1807run = new SlimTipUtil.CloudInfoQuery().mo1807run((ThreadPool.JobContext) null);
        if (mo1807run == null) {
            return Boxing.boxBoolean(false);
        }
        long total = mo1807run.getTotal() - mo1807run.getUsed();
        Boolean isNeedShowStorageLowTip = SlimTipUtil.isNeedShowStorageLowTip(StaticContext.sGetAndroidContext());
        Intrinsics.checkNotNullExpressionValue(isNeedShowStorageLowTip, "isNeedShowStorageLowTip(…ext.sGetAndroidContext())");
        if (isNeedShowStorageLowTip.booleanValue()) {
            BaseScanner scanner = ScannerManager.getInstance().getScanner(0);
            Objects.requireNonNull(scanner, "null cannot be cast to non-null type com.miui.gallery.cleaner.slim.SlimScanner");
            GalleryPreferences.Sync.setSlimLastScanTimestamp(System.currentTimeMillis());
            ScanResult scan = ((SlimScanner) scanner).scan();
            if (scan == null) {
                return Boxing.boxBoolean(false);
            }
            long size = scan.getSize();
            DefaultLogger.d("SlimTipUtil", "slimSize: %d, cloudUsableSpace: %d", Boxing.boxLong(size), Boxing.boxLong(total));
            if (size > 500000000 && size < total) {
                if (GalleryPreferences.Sync.isDeviceStorageTooLow()) {
                    GalleryPreferences.Sync.setSlimableSize(size);
                    return Boxing.boxBoolean(true);
                } else if (GalleryPreferences.Sync.isDeviceStorageLow()) {
                    GalleryPreferences.Sync.setSlimTextLinkShouldShow(Boxing.boxBoolean(true));
                    GalleryPreferences.Sync.setSlimableSize(size);
                    SyncStateManager.getInstance().getSyncState().setIsLocalSpaceFull(true);
                }
            }
        }
        return Boxing.boxBoolean(false);
    }

    public ShowResult show(HomePageFragment host, boolean z) {
        Intrinsics.checkNotNullParameter(host, "host");
        if (z) {
            SlimTipUtil.StorageLowDialogFragment storageLowDialogFragment = new SlimTipUtil.StorageLowDialogFragment();
            storageLowDialogFragment.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.miui.gallery.ui.HomePageFragment$Companion$provideSlimGuide$1$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    HomePageFragment$Companion$provideSlimGuide$1.m1485$r8$lambda$K8N0WVf6_Vrx8PazlrjlO1Rmj0(HomePageFragment$Companion$provideSlimGuide$1.this, dialogInterface);
                }
            });
            storageLowDialogFragment.show(host.getParentFragmentManager());
            return ShowResult.SHOWN_N_WAITING;
        }
        return ShowResult.SKIPPED;
    }

    /* renamed from: show$lambda-0 */
    public static final void m1486show$lambda0(HomePageFragment$Companion$provideSlimGuide$1 this$0, DialogInterface dialogInterface) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.scheduleNext(true);
    }
}
