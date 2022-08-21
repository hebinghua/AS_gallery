package com.miui.gallery.storage.strategies.android28;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.storage.strategies.IFilePathResolverStorageStrategyHolder;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.IStorageStrategy;
import com.miui.gallery.storage.strategies.base.IExtendedStorageOperator;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.storage.utils.IFilePathResolver;
import com.miui.gallery.storage.utils.ISAFStoragePermissionRequester;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: PStorageStrategyManager.kt */
/* loaded from: classes2.dex */
public final class PStorageStrategyManager implements IFilePathResolverStorageStrategyHolder, IExtendedStorageOperator, StorageStrategyManager {
    public final IFilePathResolverStorageStrategyHolder holder;
    public final IExtendedStorageOperator operator;

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public PStorageStrategyManager(Context context, IFilePathResolver iFilePathResolver, ISAFStoragePermissionRequester iSAFStoragePermissionRequester) {
        this(context, iFilePathResolver, iSAFStoragePermissionRequester, null, null, 24, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder
    public void addAt(IStorageStrategy strategy, int i) {
        Intrinsics.checkNotNullParameter(strategy, "strategy");
        this.holder.addAt(strategy, i);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder
    public void append(IStorageStrategy strategy) {
        Intrinsics.checkNotNullParameter(strategy, "strategy");
        this.holder.append(strategy);
    }

    @Override // com.miui.gallery.storage.strategies.base.IExtendedStorageOperator
    public boolean apply(DocumentFile documentFile) {
        Intrinsics.checkNotNullParameter(documentFile, "documentFile");
        return this.operator.apply(documentFile);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public IStoragePermissionStrategy.PermissionResult checkPermission(String str, IStoragePermissionStrategy.Permission permission) {
        return this.holder.checkPermission(str, permission);
    }

    @Override // com.miui.gallery.storage.strategies.IFilePathResolverStorageStrategyHolder
    public List<IStoragePermissionStrategy.PermissionResult> checkPermission(Object obj, int i, IStoragePermissionStrategy.Permission permission) {
        return this.holder.checkPermission(obj, i, permission);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public List<IStoragePermissionStrategy.PermissionResult> checkPermission(List<String> paths, IStoragePermissionStrategy.Permission permission) {
        Intrinsics.checkNotNullParameter(paths, "paths");
        return this.holder.checkPermission(paths, permission);
    }

    @Override // com.miui.gallery.storage.strategies.base.IExtendedStorageOperator
    public boolean copyFile(String str, String str2, String invokerTag) {
        Intrinsics.checkNotNullParameter(invokerTag, "invokerTag");
        return this.operator.copyFile(str, str2, invokerTag);
    }

    @Override // com.miui.gallery.storage.strategies.IOrderedStorageStrategyHolder
    public DocumentFile getDocumentFile(String strategyOrder, String str, IStoragePermissionStrategy.Permission permission, Bundle bundle) {
        Intrinsics.checkNotNullParameter(strategyOrder, "strategyOrder");
        return this.holder.getDocumentFile(strategyOrder, str, permission, bundle);
    }

    @Override // com.miui.gallery.storage.strategies.base.IExtendedStorageOperator
    public boolean moveFile(String str, String str2, String invokerTag) {
        Intrinsics.checkNotNullParameter(invokerTag, "invokerTag");
        return this.operator.moveFile(str, str2, invokerTag);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public void onHandleRequestPermissionResult(Fragment fragment, Uri uri) {
        Intrinsics.checkNotNullParameter(fragment, "fragment");
        this.holder.onHandleRequestPermissionResult(fragment, uri);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public void onHandleRequestPermissionResult(FragmentActivity activity, int i, int i2, Intent intent) {
        Intrinsics.checkNotNullParameter(activity, "activity");
        this.holder.onHandleRequestPermissionResult(activity, i, i2, intent);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public void onHandleRequestPermissionResult(FragmentActivity host, Uri uri) {
        Intrinsics.checkNotNullParameter(host, "host");
        this.holder.onHandleRequestPermissionResult(host, uri);
    }

    @Override // com.miui.gallery.storage.strategies.base.IExtendedStorageOperator
    public ParcelFileDescriptor openFileDescriptor(DocumentFile documentFile, String mode) {
        Intrinsics.checkNotNullParameter(mode, "mode");
        return this.operator.openFileDescriptor(documentFile, mode);
    }

    @Override // com.miui.gallery.storage.strategies.base.IExtendedStorageOperator
    public InputStream openInputStream(DocumentFile documentFile) {
        return this.operator.openInputStream(documentFile);
    }

    @Override // com.miui.gallery.storage.strategies.base.IExtendedStorageOperator
    public OutputStream openOutputStream(DocumentFile documentFile) {
        return this.operator.openOutputStream(documentFile);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public void registerPermissionObserver(ContentObserver contentObserver) {
        this.holder.registerPermissionObserver(contentObserver);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public void requestPermission(FragmentActivity host, String str, Map<String, Object> params, IStoragePermissionStrategy.Permission... permissionArr) {
        Intrinsics.checkNotNullParameter(host, "host");
        Intrinsics.checkNotNullParameter(params, "params");
        this.holder.requestPermission(host, str, params, permissionArr);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public void requestPermission(FragmentActivity host, String str, IStoragePermissionStrategy.Permission... permissionArr) {
        Intrinsics.checkNotNullParameter(host, "host");
        this.holder.requestPermission(host, str, permissionArr);
    }

    @Override // com.miui.gallery.storage.strategies.base.IExtendedStorageOperator
    public boolean setLastModified(DocumentFile documentFile, long j) {
        Intrinsics.checkNotNullParameter(documentFile, "documentFile");
        return this.operator.setLastModified(documentFile, j);
    }

    @Override // com.miui.gallery.storage.strategies.IStorageStrategyHolder
    public void unregisterPermissionObserver(ContentObserver contentObserver) {
        this.holder.unregisterPermissionObserver(contentObserver);
    }

    public PStorageStrategyManager(Context context, IFilePathResolver iFilePathResolver, ISAFStoragePermissionRequester iSAFStoragePermissionRequester, IFilePathResolverStorageStrategyHolder holder, IExtendedStorageOperator operator) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(holder, "holder");
        Intrinsics.checkNotNullParameter(operator, "operator");
        this.holder = holder;
        this.operator = operator;
        append(new MIUIFileApiStorageStrategy(context));
        append(new SAFStorageStrategy(context, iSAFStoragePermissionRequester));
    }

    /* JADX WARN: Illegal instructions before constructor call */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public /* synthetic */ PStorageStrategyManager(android.content.Context r8, com.miui.gallery.storage.utils.IFilePathResolver r9, com.miui.gallery.storage.utils.ISAFStoragePermissionRequester r10, com.miui.gallery.storage.strategies.IFilePathResolverStorageStrategyHolder r11, com.miui.gallery.storage.strategies.base.IExtendedStorageOperator r12, int r13, kotlin.jvm.internal.DefaultConstructorMarker r14) {
        /*
            r7 = this;
            r14 = r13 & 8
            if (r14 == 0) goto L15
            com.miui.gallery.storage.strategies.base.BaseStorageStrategyHolder r11 = new com.miui.gallery.storage.strategies.base.BaseStorageStrategyHolder
            r11.<init>()
            com.miui.gallery.storage.strategies.base.MultiUserHackStorageStrategyHolderProxy r14 = new com.miui.gallery.storage.strategies.base.MultiUserHackStorageStrategyHolderProxy
            com.miui.gallery.storage.base.FilePathResolverStorageStrategyHolderProxy r0 = new com.miui.gallery.storage.base.FilePathResolverStorageStrategyHolderProxy
            r0.<init>(r11, r9)
            r14.<init>(r8, r0)
            r5 = r14
            goto L16
        L15:
            r5 = r11
        L16:
            r11 = r13 & 16
            if (r11 == 0) goto L1f
            com.miui.gallery.storage.strategies.android28.PExtendedStorageOperator r12 = new com.miui.gallery.storage.strategies.android28.PExtendedStorageOperator
            r12.<init>(r8, r5)
        L1f:
            r6 = r12
            r1 = r7
            r2 = r8
            r3 = r9
            r4 = r10
            r1.<init>(r2, r3, r4, r5, r6)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.storage.strategies.android28.PStorageStrategyManager.<init>(android.content.Context, com.miui.gallery.storage.utils.IFilePathResolver, com.miui.gallery.storage.utils.ISAFStoragePermissionRequester, com.miui.gallery.storage.strategies.IFilePathResolverStorageStrategyHolder, com.miui.gallery.storage.strategies.base.IExtendedStorageOperator, int, kotlin.jvm.internal.DefaultConstructorMarker):void");
    }
}
