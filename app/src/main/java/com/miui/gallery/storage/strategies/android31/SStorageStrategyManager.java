package com.miui.gallery.storage.strategies.android31;

import android.content.Context;
import com.miui.gallery.storage.strategies.android30.RStorageStrategyManager;
import com.miui.gallery.storage.utils.IFilePathResolver;
import com.miui.gallery.storage.utils.IMediaStoreIdResolver;
import com.miui.gallery.storage.utils.ISAFStoragePermissionRequester;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: SStorageStrategyManager.kt */
/* loaded from: classes2.dex */
public final class SStorageStrategyManager extends RStorageStrategyManager {
    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SStorageStrategyManager(Context context, IFilePathResolver iFilePathResolver, ISAFStoragePermissionRequester iSAFStoragePermissionRequester, IMediaStoreIdResolver iMediaStoreIdResolver) {
        super(context, iFilePathResolver, iSAFStoragePermissionRequester, iMediaStoreIdResolver, null, null, 48, null);
        Intrinsics.checkNotNullParameter(context, "context");
        addAt(new RawExternalStorageAccessFileApiStorageStrategy(context), 1);
    }
}
