package com.miui.gallery.share;

import com.miui.gallery.app.AutoTracking;

/* loaded from: classes2.dex */
public class NormalShareAlbumSharerActivity extends ShareAlbumBaseActivity {
    @Override // com.miui.gallery.share.ShareAlbumBaseActivity
    public String getPageName() {
        return "album_normal_share_info";
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseActivity
    public ShareAlbumBaseFragment createFragment() {
        AutoTracking.track(NormalShareAlbumSharerFragment.class.getCanonicalName());
        return new NormalShareAlbumSharerFragment();
    }
}
