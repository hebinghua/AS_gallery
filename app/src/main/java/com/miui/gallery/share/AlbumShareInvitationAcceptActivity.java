package com.miui.gallery.share;

import android.os.Bundle;
import com.miui.gallery.R;
import com.miui.gallery.lib.MiuiGalleryUtils;
import com.miui.gallery.share.AlbumShareInvitationActivityBase;
import com.miui.gallery.share.AlbumShareUIManager;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.deviceprovider.ApplicationHelper;

/* loaded from: classes2.dex */
public class AlbumShareInvitationAcceptActivity extends AlbumShareInvitationActivityBase {
    @Override // com.miui.gallery.share.AlbumShareInvitationActivityBase, com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!ApplicationHelper.supportShare()) {
            ToastUtils.makeText(this, (int) R.string.error_share_not_support);
            finish();
            return;
        }
        if (bundle == null && getIntent() != null) {
            bundle = getIntent().getExtras();
        }
        if (bundle != null && bundle.containsKey("invitation-url")) {
            String string = bundle.getString("invitation-url");
            if (MiuiGalleryUtils.isAlbumShareInvitationUrl(string)) {
                AlbumShareInvitationHandler.acceptShareInvitation(this, string, 16, AlbumShareUIManager.BlockMessage.create(this, null, getString(R.string.get_album_info_in_process), true, new AlbumShareInvitationActivityBase.OnBlockMessageCancelled(this)));
                return;
            }
        }
        ToastUtils.makeText(this, (int) R.string.invalid_invition_url);
        finish();
    }
}
