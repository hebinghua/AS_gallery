package com.miui.gallery.activity.facebaby;

import android.os.Bundle;
import com.miui.gallery.R;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.permission.core.Permission;
import com.miui.gallery.ui.renameface.InputFaceNameFragment;
import com.miui.gallery.util.ArrayUtils;
import com.miui.gallery.util.IntentUtil;
import miui.gallery.support.actionbar.ActionBarCompat;

/* loaded from: classes.dex */
public class InputFaceNameActivity extends BaseActivity {
    public InputFaceNameFragment mFragment;

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ActionBarCompat.setExpandState(this, 0);
        setContentView(R.layout.input_face_name_activity);
        this.mFragment = (InputFaceNameFragment) getSupportFragmentManager().findFragmentById(R.id.input_face_name_fragment);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        this.mFragment.onBackPressed();
        super.onBackPressed();
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.permission.core.PermissionCheckCallback
    public Permission[] getRuntimePermissions() {
        return !IntentUtil.isContactPackageInstalled() ? super.getRuntimePermissions() : (Permission[]) ArrayUtils.concat(super.getRuntimePermissions(), getOptionalPermissions());
    }

    public final Permission[] getOptionalPermissions() {
        return new Permission[]{new Permission("android.permission.READ_CONTACTS", getString(R.string.permission_contacts_desc), false)};
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.permission.core.PermissionCheckCallback
    public void onPermissionsChecked(Permission[] permissionArr, int[] iArr, boolean[] zArr) {
        super.onPermissionsChecked(permissionArr, iArr, zArr);
        InputFaceNameFragment inputFaceNameFragment = this.mFragment;
        if (inputFaceNameFragment != null) {
            inputFaceNameFragment.updateNameList();
        }
    }
}
