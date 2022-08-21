package com.miui.gallery.share;

import android.content.Intent;
import com.miui.gallery.app.AutoTracking;

/* loaded from: classes2.dex */
public class NormalShareAlbumOwnerActivity extends ShareAlbumBaseActivity {
    @Override // com.miui.gallery.share.ShareAlbumBaseActivity
    public String getPageName() {
        return "album_normal_share_owner_info";
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseActivity
    public ShareAlbumBaseFragment createFragment() {
        AutoTracking.track(NormalShareAlbumOwnerFragment.class.getCanonicalName());
        return new NormalShareAlbumOwnerFragment();
    }

    @Override // miuix.appcompat.app.AppCompatActivity, android.app.Activity
    public void finish() {
        Intent intent = new Intent();
        this.mFragment.fillResult(intent);
        setResult(-1, intent);
        super.finish();
    }
}
