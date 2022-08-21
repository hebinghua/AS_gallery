package com.miui.gallery.activity;

import android.os.Bundle;
import com.miui.gallery.R;

/* loaded from: classes.dex */
public class AlbumNewPhotoDetailActivity extends BaseActivity {
    public int getContentId() {
        return R.layout.album_new_photo_detail_activity;
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(getContentId());
    }
}
