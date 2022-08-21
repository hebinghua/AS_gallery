package com.miui.gallery.picker;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class PickAlbumDetailActivity extends PickAlbumDetailActivityBase {
    @Override // com.miui.gallery.picker.PickAlbumDetailActivityBase, com.miui.gallery.picker.PickerActivity, com.miui.gallery.picker.PickerCompatActivity, com.miui.gallery.picker.PickerBaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (this.mPicker == null) {
            return;
        }
        setContentView(R.layout.picker_album_detail_activity);
        this.mAlbumDetailImpl = (PickAlbumDetailFragment) getSupportFragmentManager().findFragmentById(R.id.album_detail);
        this.mISelectAllDecor = (PickAlbumDetailFragment) getSupportFragmentManager().findFragmentById(R.id.album_detail);
        this.mShowSortMenuListener = (PickAlbumDetailFragment) getSupportFragmentManager().findFragmentById(R.id.album_detail);
        Intent intent = getIntent();
        long longExtra = intent.getLongExtra("album_id", -1L);
        String stringExtra = intent.getStringExtra("album_name");
        String stringExtra2 = intent.getStringExtra("album_server_id");
        String stringExtra3 = intent.getStringExtra("album_local_path");
        boolean booleanExtra = intent.getBooleanExtra("other_share_album", false);
        boolean booleanExtra2 = intent.getBooleanExtra("screenshot_album", false);
        boolean booleanExtra3 = intent.getBooleanExtra("screenrecorder_album", false);
        String stringExtra4 = intent.getStringExtra("photo_selection");
        if (longExtra == -1) {
            finish();
            return;
        }
        this.mISelectAllDecor.setItemStateListener(this.mItemStateListener);
        ((PickAlbumDetailFragment) this.mAlbumDetailImpl).setAlbumId(longExtra);
        ((PickAlbumDetailFragment) this.mAlbumDetailImpl).setAlbumLocalPath(stringExtra3);
        ((PickAlbumDetailFragment) this.mAlbumDetailImpl).setIsCameraAlbum(stringExtra2);
        ((PickAlbumDetailFragment) this.mAlbumDetailImpl).setIsOtherSharedAlbum(booleanExtra);
        ((PickAlbumDetailFragment) this.mAlbumDetailImpl).setIsScreenshotAlbum(booleanExtra2);
        ((PickAlbumDetailFragment) this.mAlbumDetailImpl).setIsScreenRecorderAlbum(booleanExtra3);
        ((PickAlbumDetailFragment) this.mAlbumDetailImpl).setExtraSelection(stringExtra4);
        if (TextUtils.isEmpty(stringExtra)) {
            return;
        }
        setTitle(stringExtra);
    }
}
