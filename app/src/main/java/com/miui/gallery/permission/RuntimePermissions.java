package com.miui.gallery.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.text.TextUtils;
import com.miui.gallery.permission.core.Permission;

/* loaded from: classes2.dex */
public class RuntimePermissions {
    public static final String[] PERMISSIONS_REQUIRED = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    public static final String[] PERMISSION_OPTIONAL = {"android.permission.READ_CONTACTS"};

    public static Permission parsePermission(Context context, String str, boolean z) {
        int i;
        int i2;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String permissionGroup = getPermissionGroup(context, str);
        if (!isValidGroup(permissionGroup)) {
            permissionGroup = getPermissionGroupGuaranty(str);
        }
        if ("android.permission-group.STORAGE".equalsIgnoreCase(permissionGroup)) {
            i = R$string.permission_storage_name;
            i2 = R$string.permission_storage_desc;
        } else if ("android.permission-group.CONTACTS".equalsIgnoreCase(permissionGroup)) {
            i = R$string.permission_contacts_name;
            i2 = R$string.permission_contacts_desc;
        } else if ("android.permission-group.PHONE".equalsIgnoreCase(permissionGroup)) {
            i = R$string.permission_phone_name;
            i2 = R$string.permission_phone_desc;
        } else {
            i = -1;
            i2 = -1;
        }
        String str2 = "";
        String string = i != -1 ? context.getResources().getString(i) : str2;
        if (i2 != -1) {
            str2 = context.getResources().getString(i2);
        }
        return new Permission(permissionGroup, string, str2, z);
    }

    public static boolean isValidGroup(String str) {
        return !TextUtils.isEmpty(str) && !TextUtils.equals("android.permission-group.UNDEFINED", str);
    }

    public static String getPermissionGroup(Context context, String str) {
        try {
            PermissionInfo permissionInfo = context.getPackageManager().getPermissionInfo(str, 0);
            return permissionInfo != null ? permissionInfo.group : "android.permission-group.UNDEFINED";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "android.permission-group.UNDEFINED";
        }
    }

    public static String getPermissionGroupGuaranty(String str) {
        return isBelongGroup(str, STORAGE()) ? "android.permission-group.STORAGE" : isBelongGroup(str, CONTACTS()) ? "android.permission-group.CONTACTS" : isBelongGroup(str, PHONE()) ? "android.permission-group.PHONE" : "android.permission-group.UNDEFINED";
    }

    public static boolean isBelongGroup(String str, String[] strArr) {
        if (strArr.length > 0) {
            return TextUtils.equals(strArr[0], str);
        }
        return false;
    }

    public static String[] STORAGE() {
        return new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    }

    public static String[] CONTACTS() {
        return new String[]{"android.permission.READ_CONTACTS", "android.permission.WRITE_CONTACTS", "android.permission.GET_ACCOUNTS"};
    }

    public static String[] PHONE() {
        return new String[]{"android.permission.READ_PHONE_STATE", "android.permission.CALL_PHONE", "android.permission.READ_CALL_LOG", "android.permission.WRITE_CALL_LOG", "com.android.voicemail.permission.ADD_VOICEMAIL", "android.permission.USE_SIP", "android.permission.PROCESS_OUTGOING_CALLS"};
    }
}
