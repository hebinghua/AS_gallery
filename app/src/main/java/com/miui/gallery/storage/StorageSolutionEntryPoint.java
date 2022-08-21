package com.miui.gallery.storage;

import com.miui.gallery.storage.utils.IFilePathResolver;
import com.miui.gallery.storage.utils.IMediaStoreIdResolver;
import com.miui.gallery.storage.utils.ISAFStoragePermissionRequester;
import java.util.Set;

/* compiled from: StorageSolutionEntryPoint.kt */
/* loaded from: classes2.dex */
public interface StorageSolutionEntryPoint {
    Set<IFilePathResolver> getFilePathResolverRegistry();

    Set<IMediaStoreIdResolver> getMediaStoreIdResolverRegistry();

    Set<ISAFStoragePermissionRequester> getSAFPermissionRequesterRegistry();
}
