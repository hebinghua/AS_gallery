package com.miui.gallery.share.baby;

import com.miui.gallery.R;
import com.miui.gallery.share.ShareAlbumSharerBaseFragment;
import com.miui.gallery.share.ShareUserAdapterBase;

/* loaded from: classes2.dex */
public class BabyShareAlbumSharerFragment extends ShareAlbumSharerBaseFragment {
    @Override // com.miui.gallery.share.ShareAlbumBaseFragment
    public int getPreferencesResourceId() {
        return R.xml.baby_share_album_preference;
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseFragment
    public ShareUserAdapterBase createShareUserAdapter() {
        return new BabyAlbumShareUserAdapter(getContext(), this.mCreatorId);
    }
}
