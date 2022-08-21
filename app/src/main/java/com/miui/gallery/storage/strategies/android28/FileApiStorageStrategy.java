package com.miui.gallery.storage.strategies.android28;

import android.content.Context;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StrategyType;
import com.miui.gallery.storage.utils.Utils;
import com.miui.gallery.util.StorageUtils;

@StrategyType(type = 0)
/* loaded from: classes2.dex */
public class FileApiStorageStrategy extends com.miui.gallery.storage.strategies.android26.FileApiStorageStrategy {
    public FileApiStorageStrategy(Context context) {
        super(context);
    }

    /* renamed from: com.miui.gallery.storage.strategies.android28.FileApiStorageStrategy$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission;

        static {
            int[] iArr = new int[IStoragePermissionStrategy.Permission.values().length];
            $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission = iArr;
            try {
                iArr[IStoragePermissionStrategy.Permission.QUERY_DIRECTORY.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.INSERT_DIRECTORY.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.QUERY.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.UPDATE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.APPEND.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.DELETE.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.INSERT.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.UPDATE_DIRECTORY.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[IStoragePermissionStrategy.Permission.DELETE_DIRECTORY.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
        }
    }

    @Override // com.miui.gallery.storage.strategies.android26.FileApiStorageStrategy, com.miui.gallery.storage.strategies.IStoragePermissionStrategy
    public IStoragePermissionStrategy.PermissionResult checkPermission(String str, IStoragePermissionStrategy.Permission permission) {
        IStoragePermissionStrategy.PermissionResult permissionResult = new IStoragePermissionStrategy.PermissionResult(str, permission);
        boolean z = true;
        switch (AnonymousClass1.$SwitchMap$com$miui$gallery$storage$strategies$IStoragePermissionStrategy$Permission[permission.ordinal()]) {
            case 1:
            case 2:
                permissionResult.granted = true;
                return permissionResult;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
                if (StorageUtils.isInSecondaryStorage(str) || !super.checkPermission(str, permission).granted) {
                    z = false;
                }
                permissionResult.granted = z;
                return permissionResult;
            case 8:
            case 9:
                permissionResult.granted = Utils.isUnderAppSpecificDirectory(this.mApplicationContext, str);
                return permissionResult;
            default:
                return permissionResult;
        }
    }
}
