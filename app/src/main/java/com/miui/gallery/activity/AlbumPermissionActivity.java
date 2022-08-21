package com.miui.gallery.activity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.miui.gallery.R;
import com.miui.gallery.biz.albumpermission.AlbumPermissionFragment;

/* loaded from: classes.dex */
public class AlbumPermissionActivity extends Hilt_AlbumPermissionActivity {
    @Override // com.miui.gallery.activity.BaseActivity
    public int getFragmentContainerId() {
        return 16908290;
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setTitle(getString(R.string.album_permission_activity_title));
        startFragment(AlbumPermissionActivity$$ExternalSyntheticLambda0.INSTANCE, "StoragePermissionFragment", false, true);
    }

    public static /* synthetic */ Fragment lambda$onCreate$0(String str) {
        return new AlbumPermissionFragment();
    }
}
