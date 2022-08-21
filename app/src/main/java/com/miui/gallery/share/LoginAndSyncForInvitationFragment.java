package com.miui.gallery.share;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.ui.BaseFragment;
import com.miui.gallery.ui.LoginAndSyncCheckFragment;
import com.miui.gallery.util.GalleryIntent$CloudGuideSource;

/* loaded from: classes2.dex */
public class LoginAndSyncForInvitationFragment extends BaseFragment {
    public Path mPath;

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return null;
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public int getThemeRes() {
        return 0;
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public boolean recordPageByDefault() {
        return false;
    }

    @Override // com.miui.gallery.ui.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mPath = (Path) arguments.getParcelable("invitation_path");
        }
        if (this.mPath == null) {
            finish();
            return;
        }
        Bundle bundle2 = new Bundle();
        bundle2.putBoolean("key_check_gallery_sync", true);
        bundle2.putSerializable("cloud_guide_source", GalleryIntent$CloudGuideSource.SHARE_INVITATION);
        LoginAndSyncCheckFragment.checkLoginAndSyncState(this, bundle2);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i2 == -1) {
            AlbumShareInvitationHandler.acceptShareInvitation(getActivity(), this.mPath);
        } else {
            finish();
        }
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public void finish() {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            activity.finish();
        }
    }
}
