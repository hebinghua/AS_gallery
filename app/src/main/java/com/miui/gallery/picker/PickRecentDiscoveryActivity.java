package com.miui.gallery.picker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class PickRecentDiscoveryActivity extends PickAlbumDetailActivityBase {
    @Override // com.miui.gallery.picker.PickAlbumDetailActivityBase, com.miui.gallery.picker.PickerActivity, com.miui.gallery.picker.PickerCompatActivity, com.miui.gallery.picker.PickerBaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (this.mPicker == null) {
            return;
        }
        setContentView(R.layout.picker_recent_discovery_activity);
        this.mAlbumDetailImpl = (PickRecentDiscoveryFragment) getSupportFragmentManager().findFragmentById(R.id.album_recent);
        this.mISelectAllDecor = (PickRecentDiscoveryFragment) getSupportFragmentManager().findFragmentById(R.id.album_recent);
        Intent intent = getIntent();
        long longExtra = intent.getLongExtra("album_id", -1L);
        String stringExtra = intent.getStringExtra("album_name");
        if (longExtra == -1) {
            finish();
            return;
        }
        this.mISelectAllDecor.setItemStateListener(this.mItemStateListener);
        if (TextUtils.isEmpty(stringExtra)) {
            return;
        }
        setTitle(stringExtra);
    }
}
