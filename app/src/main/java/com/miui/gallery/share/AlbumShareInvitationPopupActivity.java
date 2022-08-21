package com.miui.gallery.share;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.miui.gallery.R;
import com.miui.gallery.share.AlbumShareInvitationActivityBase;
import com.miui.gallery.share.AlbumShareUIManager;
import com.nexstreaming.nexeditorsdk.nexExportFormat;

/* loaded from: classes2.dex */
public class AlbumShareInvitationPopupActivity extends AlbumShareInvitationActivityBase {
    @Override // com.miui.gallery.share.AlbumShareInvitationActivityBase, com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null && getIntent() != null) {
            bundle = getIntent().getExtras();
        }
        String string = bundle != null ? bundle.getString(nexExportFormat.TAG_FORMAT_PATH) : null;
        if (TextUtils.isEmpty(string)) {
            Log.e("AlbumShareInvitationPopupActivity", "bad path");
            finish();
            return;
        }
        AlbumShareInvitationHandler.applyToShareAlbum(this, Path.fromString(string), true, AlbumShareUIManager.BlockMessage.create(this, null, getString(R.string.get_album_info_in_process), true, new AlbumShareInvitationActivityBase.OnBlockMessageCancelled(this)));
    }
}
