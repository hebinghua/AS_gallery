package com.miui.gallery.ui;

import android.content.Context;
import android.content.DialogInterface;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.R;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.DeleteDataUtil;
import com.miui.gallery.widget.GalleryDialogFragment;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import kotlin.TuplesKt;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.MapsKt__MapsJVMKt;
import kotlin.coroutines.Continuation;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
import kotlinx.coroutines.CoroutineDispatcher;

/* compiled from: HomePageFragment.kt */
/* loaded from: classes2.dex */
public final class HomePageFragment$Companion$provideDeletingPermissionGuide$1 extends IntroductionPage<HomePageFragment, List<? extends String>> {
    public final /* synthetic */ CoroutineDispatcher $dispatcher;
    public final /* synthetic */ HostProvider<HomePageFragment> $hostProvider;

    /* renamed from: $r8$lambda$lKhSChbsrnfQA3kdiz-hV03jPe0 */
    public static /* synthetic */ void m1479$r8$lambda$lKhSChbsrnfQA3kdizhV03jPe0(List list, Fragment fragment, int i) {
        m1480show$lambda0(list, fragment, i);
    }

    public static /* synthetic */ void $r8$lambda$uu9CM0NAmKYr1Y2m2txjmE0hMD4(HomePageFragment$Companion$provideDeletingPermissionGuide$1 homePageFragment$Companion$provideDeletingPermissionGuide$1, DialogInterface dialogInterface) {
        m1481show$lambda1(homePageFragment$Companion$provideDeletingPermissionGuide$1, dialogInterface);
    }

    @Override // com.miui.gallery.ui.IntroductionPage
    public boolean prejudge(HomePageFragment host, boolean z) {
        Intrinsics.checkNotNullParameter(host, "host");
        return true;
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public HomePageFragment$Companion$provideDeletingPermissionGuide$1(HostProvider<HomePageFragment> hostProvider, CoroutineDispatcher coroutineDispatcher) {
        super(hostProvider, coroutineDispatcher);
        this.$hostProvider = hostProvider;
        this.$dispatcher = coroutineDispatcher;
    }

    @Override // com.miui.gallery.ui.IntroductionPage
    public /* bridge */ /* synthetic */ ShowResult show(HomePageFragment homePageFragment, List<? extends String> list) {
        return show2(homePageFragment, (List<String>) list);
    }

    @Override // com.miui.gallery.ui.IntroductionPage
    public Object prepareInBackground(Continuation<? super List<? extends String>> continuation) {
        List<String> mo1807run = new DeleteDataUtil.DeletePathQueryJob().mo1807run((ThreadPool.JobContext) null);
        return mo1807run == null ? CollectionsKt__CollectionsKt.emptyList() : mo1807run;
    }

    /* renamed from: show */
    public ShowResult show2(HomePageFragment host, final List<String> param) {
        String format;
        Intrinsics.checkNotNullParameter(host, "host");
        Intrinsics.checkNotNullParameter(param, "param");
        if (param.isEmpty()) {
            return ShowResult.SKIPPED;
        }
        Context requireContext = host.requireContext();
        Intrinsics.checkNotNullExpressionValue(requireContext, "host.requireContext()");
        if (AccountCache.getAccount() == null) {
            StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
            String string = requireContext.getString(R.string.saf_operation_guide_2);
            Intrinsics.checkNotNullExpressionValue(string, "context.getString(R.string.saf_operation_guide_2)");
            String format2 = String.format(string, Arrays.copyOf(new Object[]{requireContext.getString(R.string.saf_operation_guide_param1), requireContext.getString(R.string.saf_operation_guide_param2)}, 2));
            Intrinsics.checkNotNullExpressionValue(format2, "format(format, *args)");
            format = String.format("%s%s", Arrays.copyOf(new Object[]{requireContext.getString(R.string.delete_account_message), format2}, 2));
            Intrinsics.checkNotNullExpressionValue(format, "format(format, *args)");
        } else {
            StringCompanionObject stringCompanionObject2 = StringCompanionObject.INSTANCE;
            String string2 = requireContext.getString(R.string.saf_operation_guide_2);
            Intrinsics.checkNotNullExpressionValue(string2, "context.getString(R.string.saf_operation_guide_2)");
            String format3 = String.format(string2, Arrays.copyOf(new Object[]{requireContext.getString(R.string.saf_operation_guide_param1), requireContext.getString(R.string.saf_operation_guide_param2)}, 2));
            Intrinsics.checkNotNullExpressionValue(format3, "format(format, *args)");
            format = String.format("%s%s", Arrays.copyOf(new Object[]{requireContext.getString(R.string.backup_disable_message), format3}, 2));
            Intrinsics.checkNotNullExpressionValue(format, "format(format, *args)");
        }
        NewStoragePermissionDialogFragment newInstance = NewStoragePermissionDialogFragment.newInstance(format, requireContext.getString(R.string.scope_storage_operation_btn_2), new GalleryDialogFragment.OnClickListener() { // from class: com.miui.gallery.ui.HomePageFragment$Companion$provideDeletingPermissionGuide$1$$ExternalSyntheticLambda1
            @Override // com.miui.gallery.widget.GalleryDialogFragment.OnClickListener
            public final void onClick(Fragment fragment, int i) {
                HomePageFragment$Companion$provideDeletingPermissionGuide$1.m1479$r8$lambda$lKhSChbsrnfQA3kdizhV03jPe0(param, fragment, i);
            }
        }, null, null);
        newInstance.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.miui.gallery.ui.HomePageFragment$Companion$provideDeletingPermissionGuide$1$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                HomePageFragment$Companion$provideDeletingPermissionGuide$1.$r8$lambda$uu9CM0NAmKYr1Y2m2txjmE0hMD4(HomePageFragment$Companion$provideDeletingPermissionGuide$1.this, dialogInterface);
            }
        });
        newInstance.show(host.getParentFragmentManager());
        return ShowResult.SHOWN_N_WAITING;
    }

    /* renamed from: show$lambda-0 */
    public static final void m1480show$lambda0(List param, Fragment fragment, int i) {
        Intrinsics.checkNotNullParameter(param, "$param");
        FragmentActivity activity = fragment.getActivity();
        if (activity != null) {
            Iterator it = param.iterator();
            while (it.hasNext()) {
                StorageSolutionProvider.get().requestPermission(activity, (String) it.next(), MapsKt__MapsJVMKt.mapOf(TuplesKt.to(nexExportFormat.TAG_FORMAT_TYPE, 1)), IStoragePermissionStrategy.Permission.DELETE);
            }
        }
    }

    /* renamed from: show$lambda-1 */
    public static final void m1481show$lambda1(HomePageFragment$Companion$provideDeletingPermissionGuide$1 this$0, DialogInterface dialogInterface) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.scheduleNext(true);
    }
}
