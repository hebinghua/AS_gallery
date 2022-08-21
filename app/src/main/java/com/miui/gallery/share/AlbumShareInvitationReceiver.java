package com.miui.gallery.share;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.baidu.mapapi.SDKInitializer;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.lib.MiuiGalleryUtils;
import com.miui.gallery.share.AlbumShareUIManager;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.deviceprovider.ApplicationHelper;
import com.nexstreaming.nexeditorsdk.nexExportFormat;

/* loaded from: classes2.dex */
public class AlbumShareInvitationReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("AlbumShareInvitationReceiver", "AlbumShareInvitationReceiveronReceive = " + action);
        if ("com.miui.gallery.ACTION_ALBUM_SHARE_INVITATION".equals(action)) {
            String stringExtra = intent.getStringExtra("invitation_url");
            String stringExtra2 = intent.getStringExtra("invitation_type");
            String stringExtra3 = intent.getStringExtra("invitation_title");
            String stringExtra4 = intent.getStringExtra("invitation_content");
            int intExtra = intent.getIntExtra("invitation_opt", 5);
            if (!MiuiGalleryUtils.isAlbumShareInvitationUrl(stringExtra)) {
                return;
            }
            if (!ApplicationHelper.supportShare()) {
                ToastUtils.makeText(context, (int) R.string.error_share_not_support);
            } else {
                handleInvitation(stringExtra, intExtra, stringExtra2, stringExtra3, stringExtra4);
            }
        }
    }

    public void handleInvitation(String str, int i, String str2, String str3, String str4) {
        handleInvitation(str, i, str2, str3, new AddListener(i, new UpdateListener()));
    }

    public static void handleInvitation(String str, int i, String str2, String str3, AddListener addListener) {
        AlbumShareUIManager.addInvitationToDBAsync(str, addListener);
    }

    public static void openInvitation(Path path, int i) {
        Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
        Intent intent = new Intent("com.miui.gallery.intent.action.OPEN_INVIATAION");
        intent.setPackage(sGetAndroidContext.getPackageName());
        intent.putExtra(nexExportFormat.TAG_FORMAT_PATH, path.toString());
        intent.putExtra(SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_CODE, i);
        sGetAndroidContext.sendOrderedBroadcast(intent, null);
    }

    /* loaded from: classes2.dex */
    public static class UpdateListener implements AlbumShareUIManager.OnCompletionListener<Path, String> {
        public UpdateListener() {
        }

        @Override // com.miui.gallery.share.AlbumShareUIManager.OnCompletionListener
        public void onCompletion(Path path, String str, int i, boolean z) {
            if ((i == 0 && str != null) || i == -9) {
                AlbumShareInvitationReceiver.openInvitation(path, i);
            } else {
                UIHelper.toastError(i);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class AddListener implements AlbumShareUIManager.OnCompletionListener<Void, Long> {
        public final int mOpt;
        public final AlbumShareUIManager.OnCompletionListener<Path, String> mUpdateListener;

        public AddListener(int i, AlbumShareUIManager.OnCompletionListener<Path, String> onCompletionListener) {
            this.mOpt = i;
            this.mUpdateListener = onCompletionListener;
        }

        @Override // com.miui.gallery.share.AlbumShareUIManager.OnCompletionListener
        public void onCompletion(Void r5, Long l, int i, boolean z) {
            Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
            int i2 = this.mOpt;
            if ((i2 & 1) != 0 && l != null) {
                l.longValue();
            }
            if ((i2 & 2) != 0 && l != null && l.longValue() != 0) {
                Intent intent = new Intent("com.miui.gallery.intent.action.CLOUD_VIEW");
                intent.setFlags(268435456);
                try {
                    sGetAndroidContext.startActivity(intent);
                } catch (ActivityNotFoundException unused) {
                }
            }
            if ((i2 & 4) != 0 || (i2 & 16) != 0) {
                if (l == null || l.longValue() == 0) {
                    return;
                }
                final Path buildPathById = CloudSharerMediaSet.buildPathById(l.longValue());
                AlbumShareUIManager.updateShareUrlLongAuto(buildPathById, new AlbumShareUIManager.OnCompletionListener<Path, Void>() { // from class: com.miui.gallery.share.AlbumShareInvitationReceiver.AddListener.1
                    @Override // com.miui.gallery.share.AlbumShareUIManager.OnCompletionListener
                    public void onCompletion(Path path, Void r3, int i3, boolean z2) {
                        AddListener.this.mUpdateListener.onCompletion(buildPathById, null, i3, z2);
                        Log.d("AlbumShareInvitationReceiver", "updateShareUrlLongAuto: error code=" + i3);
                    }
                }, this.mUpdateListener);
            } else if ((i2 & 8) == 0) {
            } else {
                if (l != null && l.longValue() != 0) {
                    AlbumShareInvitationReceiver.openInvitation(CloudSharerMediaSet.buildPathById(l.longValue()), 0);
                } else {
                    UIHelper.toastError(-2);
                }
            }
        }
    }
}
