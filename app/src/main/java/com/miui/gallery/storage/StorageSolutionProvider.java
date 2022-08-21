package com.miui.gallery.storage;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import com.miui.gallery.storage.strategies.android26.OStorageStrategyManager;
import com.miui.gallery.storage.strategies.android28.PStorageStrategyManager;
import com.miui.gallery.storage.strategies.android30.RStorageStrategyManager;
import com.miui.gallery.storage.strategies.android31.SStorageStrategyManager;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.storage.utils.IFilePathResolver;
import com.miui.gallery.storage.utils.IMediaStoreIdResolver;
import com.miui.gallery.storage.utils.ISAFStoragePermissionRequester;
import dagger.hilt.android.EntryPointAccessors;
import java.util.Iterator;

/* loaded from: classes2.dex */
public class StorageSolutionProvider {
    public static volatile StorageSolutionProvider provider;
    public static Context sApplicationContext;
    public IFilePathResolver mFilePathResolver;
    public IMediaStoreIdResolver mMediaStoreIdResolver;
    public ISAFStoragePermissionRequester mSAFStoragePermissionRequester;
    public final StorageStrategyManager mStorageStrategyManager;

    @TargetApi(30)
    public StorageSolutionProvider(Context context) {
        resolveBindings(context);
        switch (Build.VERSION.SDK_INT) {
            case 26:
            case 27:
                this.mStorageStrategyManager = new OStorageStrategyManager(context, this.mFilePathResolver);
                return;
            case 28:
            case 29:
                this.mStorageStrategyManager = new PStorageStrategyManager(context, this.mFilePathResolver, this.mSAFStoragePermissionRequester);
                return;
            case 30:
                if (context.getApplicationInfo().targetSdkVersion == 29) {
                    this.mStorageStrategyManager = new PStorageStrategyManager(context, this.mFilePathResolver, this.mSAFStoragePermissionRequester);
                    return;
                } else {
                    this.mStorageStrategyManager = new RStorageStrategyManager(context, this.mFilePathResolver, this.mSAFStoragePermissionRequester, this.mMediaStoreIdResolver);
                    return;
                }
            default:
                this.mStorageStrategyManager = new SStorageStrategyManager(context, this.mFilePathResolver, this.mSAFStoragePermissionRequester, this.mMediaStoreIdResolver);
                return;
        }
    }

    public final void resolveBindings(Context context) {
        StorageSolutionEntryPoint storageSolutionEntryPoint = (StorageSolutionEntryPoint) EntryPointAccessors.fromApplication(context, StorageSolutionEntryPoint.class);
        int i = context.getApplicationInfo().targetSdkVersion;
        Iterator<ISAFStoragePermissionRequester> it = storageSolutionEntryPoint.getSAFPermissionRequesterRegistry().iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            ISAFStoragePermissionRequester next = it.next();
            if (next.handles(context, Build.VERSION.SDK_INT, i)) {
                this.mSAFStoragePermissionRequester = next;
                break;
            }
        }
        Iterator<IFilePathResolver> it2 = storageSolutionEntryPoint.getFilePathResolverRegistry().iterator();
        while (true) {
            if (!it2.hasNext()) {
                break;
            }
            IFilePathResolver next2 = it2.next();
            if (next2.handles(context, Build.VERSION.SDK_INT, i)) {
                this.mFilePathResolver = next2;
                break;
            }
        }
        for (IMediaStoreIdResolver iMediaStoreIdResolver : storageSolutionEntryPoint.getMediaStoreIdResolverRegistry()) {
            if (iMediaStoreIdResolver.handles(context, Build.VERSION.SDK_INT, i)) {
                this.mMediaStoreIdResolver = iMediaStoreIdResolver;
                return;
            }
        }
    }

    public static void init(Context context) {
        sApplicationContext = context;
    }

    public static StorageStrategyManager get() {
        if (provider == null) {
            synchronized (StorageSolutionProvider.class) {
                if (provider == null) {
                    provider = new StorageSolutionProvider(sApplicationContext);
                }
            }
        }
        return provider.mStorageStrategyManager;
    }
}
