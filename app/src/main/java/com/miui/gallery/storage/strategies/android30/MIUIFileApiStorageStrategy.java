package com.miui.gallery.storage.strategies.android30;

import android.content.Context;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.LazyValue;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;

/* loaded from: classes2.dex */
public class MIUIFileApiStorageStrategy extends FileApiStorageStrategy {
    public static final LazyValue<Void, Boolean> HAS_MIUI_HACKED = new LazyValue<Void, Boolean>() { // from class: com.miui.gallery.storage.strategies.android30.MIUIFileApiStorageStrategy.1
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Boolean mo1272onInit(Void r5) {
            String secondaryStoragePath = StorageUtils.getSecondaryStoragePath();
            if (secondaryStoragePath == null) {
                return Boolean.FALSE;
            }
            boolean z = false;
            File file = new File(secondaryStoragePath);
            try {
                File createTempFile = File.createTempFile("." + System.currentTimeMillis(), "miuigallery", file);
                if (createTempFile != null && createTempFile.exists()) {
                    z = true;
                    createTempFile.delete();
                }
            } catch (Exception unused) {
                DefaultLogger.e("MIUIFileApiStorageStrategy", "testing if miui hacked, creating test file failed");
            }
            return Boolean.valueOf(z);
        }
    };

    public MIUIFileApiStorageStrategy(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.storage.strategies.android30.FileApiStorageStrategy, com.miui.gallery.storage.strategies.android28.FileApiStorageStrategy, com.miui.gallery.storage.strategies.android26.FileApiStorageStrategy, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public IStoragePermissionStrategy.PermissionResult checkPermission(String str, IStoragePermissionStrategy.Permission permission) {
        IStoragePermissionStrategy.PermissionResult permissionResult = new IStoragePermissionStrategy.PermissionResult(str, permission);
        if (HAS_MIUI_HACKED.get(null).booleanValue()) {
            permissionResult.granted = true;
            return permissionResult;
        }
        return super.checkPermission(str, permission);
    }
}
