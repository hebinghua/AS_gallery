package com.miui.gallery.permission.korea;

import android.content.Context;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.permission.RuntimePermissions;
import com.miui.gallery.permission.core.OnPermissionIntroduced;
import com.miui.gallery.permission.core.Permission;
import com.miui.gallery.preference.BaseGalleryPreferences;
import java.util.ArrayList;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class RuntimePermissionsIntroduction {
    public void introduce(FragmentActivity fragmentActivity, String str, final OnPermissionIntroduced onPermissionIntroduced) {
        if (BaseGalleryPreferences.PermissionIntroduction.isRuntimePermissionsIntroduced()) {
            onPermissionIntroduced.onPermissionIntroduced(true);
        } else {
            PermissionsDialogFragment.newInstance(parsePermissions(fragmentActivity), new OnPermissionIntroduced() { // from class: com.miui.gallery.permission.korea.RuntimePermissionsIntroduction.1
                @Override // com.miui.gallery.permission.core.OnPermissionIntroduced
                public void onPermissionIntroduced(boolean z) {
                    BaseGalleryPreferences.PermissionIntroduction.setRuntimePermissionsIntroduced(true);
                    onPermissionIntroduced.onPermissionIntroduced(z);
                }
            }).invoke(fragmentActivity);
        }
    }

    public static ArrayList<Permission> parsePermissions(Context context) {
        ArrayList<Permission> arrayList = new ArrayList<>();
        HashMap hashMap = new HashMap();
        String[] allRequiredPermissions = getAllRequiredPermissions();
        if (allRequiredPermissions != null) {
            for (String str : allRequiredPermissions) {
                Permission parsePermission = RuntimePermissions.parsePermission(context, str, true);
                if (parsePermission != null && !hashMap.containsKey(parsePermission.mPermissionGroup)) {
                    hashMap.put(parsePermission.mPermissionGroup, parsePermission);
                    arrayList.add(parsePermission);
                }
            }
        }
        String[] allOptionalPermissions = getAllOptionalPermissions();
        if (allOptionalPermissions != null) {
            for (String str2 : allOptionalPermissions) {
                Permission parsePermission2 = RuntimePermissions.parsePermission(context, str2, false);
                if (parsePermission2 != null && !hashMap.containsKey(parsePermission2.mPermissionGroup)) {
                    hashMap.put(parsePermission2.mPermissionGroup, parsePermission2);
                    arrayList.add(parsePermission2);
                }
            }
        }
        return arrayList;
    }

    public static String[] getAllRequiredPermissions() {
        return RuntimePermissions.PERMISSIONS_REQUIRED;
    }

    public static String[] getAllOptionalPermissions() {
        return RuntimePermissions.PERMISSION_OPTIONAL;
    }
}
