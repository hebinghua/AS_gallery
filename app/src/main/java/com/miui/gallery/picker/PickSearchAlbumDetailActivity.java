package com.miui.gallery.picker;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.miui.gallery.R;
import com.miui.gallery.picker.albumdetail.IPickAlbumDetail;
import com.miui.gallery.picker.albumdetail.ISelectAllDecor;
import com.nexstreaming.nexeditorsdk.nexExportFormat;

/* loaded from: classes2.dex */
public class PickSearchAlbumDetailActivity extends PickAlbumDetailActivityBase {
    @Override // com.miui.gallery.picker.PickAlbumDetailActivityBase, com.miui.gallery.picker.PickerActivity, com.miui.gallery.picker.PickerCompatActivity, com.miui.gallery.picker.PickerBaseActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (this.mPicker == null) {
            return;
        }
        setContentView(R.layout.picker_search_album_detail_activity);
        this.mAlbumDetailImpl = (IPickAlbumDetail) getSupportFragmentManager().findFragmentById(R.id.album_detail);
        ISelectAllDecor iSelectAllDecor = (ISelectAllDecor) getSupportFragmentManager().findFragmentById(R.id.album_detail);
        this.mISelectAllDecor = iSelectAllDecor;
        iSelectAllDecor.setItemStateListener(this.mItemStateListener);
        Uri data = getIntent().getData();
        if (data == null || data.getLastPathSegment() == null) {
            finish();
            return;
        }
        String str = null;
        if (data.getLastPathSegment().equals("result")) {
            str = data.getQueryParameter(nexExportFormat.TAG_FORMAT_TYPE);
        }
        if (str == null) {
            finish();
            return;
        }
        Uri build = data.buildUpon().appendQueryParameter(nexExportFormat.TAG_FORMAT_TYPE, str).build();
        getIntent().setData(build);
        String queryParameter = build.getQueryParameter("title");
        if (TextUtils.isEmpty(queryParameter)) {
            return;
        }
        setTitle(queryParameter);
    }
}
