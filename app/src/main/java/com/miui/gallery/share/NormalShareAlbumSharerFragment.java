package com.miui.gallery.share;

import com.miui.gallery.R;
import com.miui.gallery.app.AutoTracking;

/* loaded from: classes2.dex */
public class NormalShareAlbumSharerFragment extends ShareAlbumSharerBaseFragment {
    public SharerPublicShareUiHandler mPublicShareUiHandler;

    @Override // com.miui.gallery.share.ShareAlbumBaseFragment
    public int getPreferencesResourceId() {
        return R.xml.share_album_sharer;
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseFragment
    public void initPreferences() {
        super.initPreferences();
        SharerPublicShareUiHandler sharerPublicShareUiHandler = new SharerPublicShareUiHandler(this, this.mAlbumName, this.mCloudSingleMediaSet);
        this.mPublicShareUiHandler = sharerPublicShareUiHandler;
        sharerPublicShareUiHandler.initPreferences();
        this.mPublicShareUiHandler.updatePublicPreference(this.mCloudSingleMediaSet.isPublic(), this.mCloudSingleMediaSet.getPublicUrl());
    }

    @Override // com.miui.gallery.share.ShareAlbumSharerBaseFragment, com.miui.gallery.share.ShareAlbumBaseFragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        this.mPublicShareUiHandler.onDestroy();
        super.onDestroy();
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseFragment
    public void updateSharers() {
        super.updateSharers();
        this.mPublicShareUiHandler.updateSharers(this.mSharerCount);
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseFragment
    public void onUpdateProgressChanged(int i) {
        this.mPublicShareUiHandler.updateProgressBar(i);
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseFragment
    public ShareUserAdapterBase createShareUserAdapter() {
        return new ShareUserAdapter(getContext(), false, this.mCreatorId);
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        AutoTracking.trackView("403.23.0.1.11308", AutoTracking.getRef(), getShareUsers() == null ? 0 : getShareUsers().size());
    }
}
