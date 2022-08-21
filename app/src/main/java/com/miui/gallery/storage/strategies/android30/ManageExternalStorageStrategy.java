package com.miui.gallery.storage.strategies.android30;

import android.content.Context;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.LazyThreadSafetyMode;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: ManageExternalStorageStrategy.kt */
/* loaded from: classes2.dex */
public final class ManageExternalStorageStrategy extends com.miui.gallery.storage.strategies.android28.MIUIFileApiStorageStrategy {
    public static final Companion Companion = new Companion(null);
    public final Context context;
    public final Lazy mManageExternalStorageGuarded$delegate;

    /* compiled from: ManageExternalStorageStrategy.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public Companion() {
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ManageExternalStorageStrategy(Context context) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
        this.context = context;
        this.mManageExternalStorageGuarded$delegate = LazyKt__LazyJVMKt.lazy(LazyThreadSafetyMode.NONE, ManageExternalStorageStrategy$mManageExternalStorageGuarded$2.INSTANCE);
    }

    public final boolean getMManageExternalStorageGuarded() {
        return ((Boolean) this.mManageExternalStorageGuarded$delegate.mo119getValue()).booleanValue();
    }

    @Override // com.miui.gallery.storage.strategies.android28.MIUIFileApiStorageStrategy, com.miui.gallery.storage.strategies.android28.FileApiStorageStrategy, com.miui.gallery.storage.strategies.android26.FileApiStorageStrategy, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public IStoragePermissionStrategy.PermissionResult checkPermission(String str, IStoragePermissionStrategy.Permission permission) {
        if (getMManageExternalStorageGuarded()) {
            IStoragePermissionStrategy.PermissionResult permissionResult = new IStoragePermissionStrategy.PermissionResult(str, permission);
            permissionResult.granted = true;
            return permissionResult;
        }
        IStoragePermissionStrategy.PermissionResult checkPermission = super.checkPermission(str, permission);
        Intrinsics.checkNotNullExpressionValue(checkPermission, "super.checkPermission(path, type)");
        return checkPermission;
    }
}
