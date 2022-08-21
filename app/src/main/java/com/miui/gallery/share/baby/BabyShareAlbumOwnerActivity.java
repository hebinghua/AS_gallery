package com.miui.gallery.share.baby;

import com.miui.gallery.share.ShareAlbumBaseActivity;
import com.miui.gallery.share.ShareAlbumBaseFragment;

/* loaded from: classes2.dex */
public class BabyShareAlbumOwnerActivity extends ShareAlbumBaseActivity {
    @Override // com.miui.gallery.share.ShareAlbumBaseActivity
    public String getPageName() {
        return "album_baby_share_owner_info";
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseActivity
    public ShareAlbumBaseFragment createFragment() {
        return new BabyShareAlbumOwnerFragment();
    }
}
