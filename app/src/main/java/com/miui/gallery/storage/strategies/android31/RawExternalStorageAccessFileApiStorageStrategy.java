package com.miui.gallery.storage.strategies.android31;

import android.app.role.RoleManager;
import android.content.Context;
import android.text.TextUtils;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.android26.FileApiStorageStrategy;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.LazyValue;
import com.miui.gallery.util.StorageUtils;

/* loaded from: classes2.dex */
public class RawExternalStorageAccessFileApiStorageStrategy extends FileApiStorageStrategy {
    public static final LazyValue<Context, Boolean> IS_SYSTEM_GALLERY = new LazyValue<Context, Boolean>() { // from class: com.miui.gallery.storage.strategies.android31.RawExternalStorageAccessFileApiStorageStrategy.1
        @Override // com.miui.gallery.util.LazyValue
        /* renamed from: onInit  reason: avoid collision after fix types in other method */
        public Boolean mo1272onInit(Context context) {
            RoleManager roleManager = (RoleManager) context.getSystemService(RoleManager.class);
            return Boolean.valueOf(roleManager.isRoleAvailable("android.app.role.SYSTEM_GALLERY") && roleManager.isRoleHeld("android.app.role.SYSTEM_GALLERY"));
        }
    };
    public final Context mApplicationContext;

    public RawExternalStorageAccessFileApiStorageStrategy(Context context) {
        super(context);
        this.mApplicationContext = context;
    }

    @Override // com.miui.gallery.storage.strategies.android26.FileApiStorageStrategy, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public IStoragePermissionStrategy.PermissionResult checkPermission(String str, IStoragePermissionStrategy.Permission permission) {
        IStoragePermissionStrategy.PermissionResult permissionResult = new IStoragePermissionStrategy.PermissionResult(str, permission);
        if (!BaseFileUtils.contains("MIUI/Gallery/cloud/secretAlbum", StorageUtils.getRelativePath(this.mApplicationContext, str)) && IS_SYSTEM_GALLERY.get(this.mApplicationContext).booleanValue()) {
            String mimeType = BaseFileMimeUtil.getMimeType(str);
            if (!BaseFileMimeUtil.isImageFromMimeType(mimeType) && !BaseFileMimeUtil.isVideoFromMimeType(mimeType) && !TextUtils.equals(BaseFileUtils.getFileName(str), ".nomedia")) {
                return permissionResult;
            }
            permissionResult.granted = true;
            return permissionResult;
        }
        return permissionResult;
    }
}
