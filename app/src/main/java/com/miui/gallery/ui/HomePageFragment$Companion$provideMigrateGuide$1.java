package com.miui.gallery.ui;

import android.content.Context;
import android.content.DialogInterface;
import androidx.fragment.app.Fragment;
import com.miui.gallery.R;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.migrate.StorageMigrationManager;
import com.miui.gallery.migrate.migrator.AbsMigrator;
import com.miui.gallery.migrate.migrator.StorageMigratorFactory;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.widget.GalleryDialogFragment;
import java.util.Iterator;
import java.util.List;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.Boxing;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CoroutineDispatcher;

/* compiled from: HomePageFragment.kt */
/* loaded from: classes2.dex */
public final class HomePageFragment$Companion$provideMigrateGuide$1 extends IntroductionPage<HomePageFragment, Boolean> {
    public final /* synthetic */ CoroutineDispatcher $dispatcher;
    public final /* synthetic */ HostProvider<HomePageFragment> $hostProvider;

    public static /* synthetic */ void $r8$lambda$K50SF9wNMMP66AFSS2Ida9Fq9Zg(HomePageFragment$Companion$provideMigrateGuide$1 homePageFragment$Companion$provideMigrateGuide$1, DialogInterface dialogInterface) {
        m1483show$lambda1(homePageFragment$Companion$provideMigrateGuide$1, dialogInterface);
    }

    public static /* synthetic */ void $r8$lambda$TcDP88zVlYFQN66AUdTVY_w1qUk(Context context, Fragment fragment, int i) {
        m1482show$lambda0(context, fragment, i);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public HomePageFragment$Companion$provideMigrateGuide$1(HostProvider<HomePageFragment> hostProvider, CoroutineDispatcher coroutineDispatcher) {
        super(hostProvider, coroutineDispatcher);
        this.$hostProvider = hostProvider;
        this.$dispatcher = coroutineDispatcher;
    }

    @Override // com.miui.gallery.ui.IntroductionPage
    public /* bridge */ /* synthetic */ ShowResult show(HomePageFragment homePageFragment, Boolean bool) {
        return show(homePageFragment, bool.booleanValue());
    }

    @Override // com.miui.gallery.ui.IntroductionPage
    public boolean prejudge(HomePageFragment host, boolean z) {
        Intrinsics.checkNotNullParameter(host, "host");
        return host.getUserVisibleHint();
    }

    @Override // com.miui.gallery.ui.IntroductionPage
    public Object prepareInBackground(Continuation<? super Boolean> continuation) {
        boolean z;
        Context sGetAndroidContext = StaticContext.sGetAndroidContext();
        long albumMigrationState = GalleryPreferences.Album.getAlbumMigrationState();
        if (albumMigrationState == CloudControlStrategyHelper.getMigrateStrategyVersion()) {
            return Boxing.boxBoolean(false);
        }
        List<AbsMigrator> create = StorageMigratorFactory.create(sGetAndroidContext, albumMigrationState);
        if (create == null || create.isEmpty()) {
            return Boxing.boxBoolean(false);
        }
        Iterator<AbsMigrator> it = create.iterator();
        while (true) {
            if (!it.hasNext()) {
                z = false;
                break;
            } else if (it.next().check()) {
                z = true;
                break;
            }
        }
        if (!z) {
            return Boxing.boxBoolean(false);
        }
        if (StorageMigrationManager.checkPermission(sGetAndroidContext)) {
            return Boxing.boxBoolean(false);
        }
        return Boxing.boxBoolean(true);
    }

    public ShowResult show(HomePageFragment host, boolean z) {
        Intrinsics.checkNotNullParameter(host, "host");
        if (z) {
            final Context requireContext = host.requireContext();
            Intrinsics.checkNotNullExpressionValue(requireContext, "host.requireContext()");
            NewStoragePermissionDialogFragment newInstance = NewStoragePermissionDialogFragment.newInstance(requireContext.getString(R.string.migrate_msg), requireContext.getString(R.string.scope_storage_operation_btn_2), new GalleryDialogFragment.OnClickListener() { // from class: com.miui.gallery.ui.HomePageFragment$Companion$provideMigrateGuide$1$$ExternalSyntheticLambda1
                @Override // com.miui.gallery.widget.GalleryDialogFragment.OnClickListener
                public final void onClick(Fragment fragment, int i) {
                    HomePageFragment$Companion$provideMigrateGuide$1.$r8$lambda$TcDP88zVlYFQN66AUdTVY_w1qUk(requireContext, fragment, i);
                }
            }, null, null);
            newInstance.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.miui.gallery.ui.HomePageFragment$Companion$provideMigrateGuide$1$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnDismissListener
                public final void onDismiss(DialogInterface dialogInterface) {
                    HomePageFragment$Companion$provideMigrateGuide$1.$r8$lambda$K50SF9wNMMP66AFSS2Ida9Fq9Zg(HomePageFragment$Companion$provideMigrateGuide$1.this, dialogInterface);
                }
            });
            newInstance.show(host.getParentFragmentManager());
            return ShowResult.SHOWN_N_WAITING;
        }
        return ShowResult.SKIPPED;
    }

    /* JADX WARN: Removed duplicated region for block: B:40:0x002d A[LOOP:0: B:39:0x002b->B:40:0x002d, LOOP_END] */
    /* renamed from: show$lambda-0 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static final void m1482show$lambda0(android.content.Context r8, androidx.fragment.app.Fragment r9, int r10) {
        /*
            java.lang.String r10 = "$context"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r8, r10)
            java.lang.String r10 = "MIUI/Gallery"
            java.lang.String[] r8 = com.miui.gallery.util.StorageUtils.getAbsolutePath(r8, r10)
            r10 = 0
            r0 = 1
            if (r8 == 0) goto L1a
            int r1 = r8.length
            if (r1 != 0) goto L14
            r1 = r0
            goto L15
        L14:
            r1 = r10
        L15:
            if (r1 == 0) goto L18
            goto L1a
        L18:
            r1 = r10
            goto L1b
        L1a:
            r1 = r0
        L1b:
            if (r1 == 0) goto L1e
            return
        L1e:
            androidx.fragment.app.FragmentActivity r9 = r9.getActivity()
            if (r9 == 0) goto L52
            java.lang.String r1 = "paths"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r8, r1)
            int r1 = r8.length
            r2 = r10
        L2b:
            if (r2 >= r1) goto L52
            r3 = r8[r2]
            int r2 = r2 + 1
            com.miui.gallery.storage.strategies.base.StorageStrategyManager r4 = com.miui.gallery.storage.StorageSolutionProvider.get()
            java.lang.Integer r5 = java.lang.Integer.valueOf(r0)
            java.lang.String r6 = "type"
            kotlin.Pair r5 = kotlin.TuplesKt.to(r6, r5)
            java.util.Map r5 = kotlin.collections.MapsKt__MapsJVMKt.mapOf(r5)
            r6 = 2
            com.miui.gallery.storage.strategies.IStoragePermissionStrategy$Permission[] r6 = new com.miui.gallery.storage.strategies.IStoragePermissionStrategy.Permission[r6]
            com.miui.gallery.storage.strategies.IStoragePermissionStrategy$Permission r7 = com.miui.gallery.storage.strategies.IStoragePermissionStrategy.Permission.INSERT
            r6[r10] = r7
            com.miui.gallery.storage.strategies.IStoragePermissionStrategy$Permission r7 = com.miui.gallery.storage.strategies.IStoragePermissionStrategy.Permission.DELETE
            r6[r0] = r7
            r4.requestPermission(r9, r3, r5, r6)
            goto L2b
        L52:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.ui.HomePageFragment$Companion$provideMigrateGuide$1.m1482show$lambda0(android.content.Context, androidx.fragment.app.Fragment, int):void");
    }

    /* renamed from: show$lambda-1 */
    public static final void m1483show$lambda1(HomePageFragment$Companion$provideMigrateGuide$1 this$0, DialogInterface dialogInterface) {
        Intrinsics.checkNotNullParameter(this$0, "this$0");
        this$0.scheduleNext(true);
    }
}
