package com.miui.gallery.share;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.cloud.base.SyncRequest;
import com.miui.gallery.cloud.base.SyncType;
import com.miui.gallery.provider.ShareAlbumHelper;
import com.miui.gallery.share.AlbumShareInvitationReceiver;
import com.miui.gallery.share.AlbumShareUIManager;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.SyncUtil;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public class AlbumShareInvitationHandler {
    public static void applyToShareAlbum(Activity activity, Path path, boolean z, AlbumShareUIManager.BlockMessage blockMessage) {
        AlbumShareUIManager.applyToShareAlbum(activity, path, new ApplyListener(activity, z, true), new AcceptListener(activity, z), new DenyListener(activity, z), new CancelListener(activity, z), blockMessage, true);
    }

    public static void acceptShareInvitation(final Activity activity, String str, int i, final AlbumShareUIManager.BlockMessage blockMessage) {
        AlbumShareInvitationReceiver.handleInvitation(str, i, (String) null, (String) null, new AlbumShareInvitationReceiver.AddListener(i, new AlbumShareUIManager.OnCompletionListener<Path, String>() { // from class: com.miui.gallery.share.AlbumShareInvitationHandler.1
            @Override // com.miui.gallery.share.AlbumShareUIManager.OnCompletionListener
            public void onCompletion(Path path, String str2, int i2, boolean z) {
                if ((i2 == 0 && str2 != null) || i2 == -9) {
                    Activity activity2 = activity;
                    AlbumShareUIManager.applyToShareAlbum(activity2, path, new ApplyListener(activity2, true, false), new AcceptListener(activity, true), null, new CancelListener(activity, true), blockMessage, false);
                    return;
                }
                UIHelper.toastError(activity, i2);
                activity.finish();
            }
        }));
    }

    public static void acceptShareInvitation(Activity activity, Path path) {
        AlbumShareUIManager.tryToAccept(path, activity, new AcceptListener(activity, true), new CancelListener(activity, true), null);
    }

    /* loaded from: classes2.dex */
    public static class AbsListener {
        public final WeakReference<Activity> mActivityRef;
        public final boolean mFinishActivity;

        public AbsListener(Activity activity, boolean z) {
            this.mActivityRef = new WeakReference<>(activity);
            this.mFinishActivity = z;
        }

        public void tryToFinishActivity() {
            Activity activity;
            if (!this.mFinishActivity || (activity = this.mActivityRef.get()) == null) {
                return;
            }
            activity.finish();
        }

        public static void removeNotification() {
            ((NotificationManager) GalleryApp.sGetAndroidContext().getSystemService("notification")).cancel(1);
        }
    }

    /* loaded from: classes2.dex */
    public static class AlreadyApplyListener extends AbsListener implements DialogInterface.OnCancelListener, DialogInterface.OnClickListener {
        public final Path mPath;

        public AlreadyApplyListener(Activity activity, Path path, boolean z) {
            super(activity, z);
            this.mPath = path;
        }

        @Override // android.content.DialogInterface.OnCancelListener
        public void onCancel(DialogInterface dialogInterface) {
            AbsListener.removeNotification();
            tryToFinishActivity();
        }

        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            Activity activity;
            AbsListener.removeNotification();
            tryToFinishActivity();
            if (i == -1 && (activity = this.mActivityRef.get()) != null) {
                AlbumShareInvitationHandler.startShareAlbumView(activity, this.mPath);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class DenyListener extends AbsListener implements AlbumShareUIManager.OnCompletionListener<Path, Void> {
        public DenyListener(Activity activity, boolean z) {
            super(activity, z);
        }

        @Override // com.miui.gallery.share.AlbumShareUIManager.OnCompletionListener
        public void onCompletion(Path path, Void r2, int i, boolean z) {
            AbsListener.removeNotification();
            tryToFinishActivity();
        }
    }

    /* loaded from: classes2.dex */
    public static class AcceptListener extends AbsListener implements AlbumShareUIManager.OnCompletionListener<Path, Long> {
        public AcceptListener(Activity activity, boolean z) {
            super(activity, z);
        }

        @Override // com.miui.gallery.share.AlbumShareUIManager.OnCompletionListener
        public void onCompletion(Path path, Long l, int i, boolean z) {
            Activity activity;
            AbsListener.removeNotification();
            if (i == -1003) {
                tryToFinishActivity();
                return;
            }
            if (z) {
                UIHelper.toast((int) R.string.cancel_hint);
            } else {
                UIHelper.toastError(i);
            }
            if (i == -4) {
                Activity activity2 = this.mActivityRef.get();
                if (activity2 != null) {
                    AlbumShareInvitationHandler.startCloudPage(activity2);
                }
            } else {
                if (i != 0) {
                    path = (i != -10 || l == null || l.longValue() == 0) ? null : CloudSharerMediaSet.buildPathById(l.longValue());
                }
                if (path != null && (activity = this.mActivityRef.get()) != null) {
                    AlbumShareInvitationHandler.startShareAlbumView(activity, path);
                }
            }
            tryToFinishActivity();
        }
    }

    /* loaded from: classes2.dex */
    public static class ApplyListener extends AbsListener implements AlbumShareUIManager.OnCompletionListener<Path, Void> {
        public boolean mNeedAlert;

        public ApplyListener(Activity activity, boolean z, boolean z2) {
            super(activity, z);
            this.mNeedAlert = z2;
        }

        @Override // com.miui.gallery.share.AlbumShareUIManager.OnCompletionListener
        public void onCompletion(Path path, Void r2, int i, boolean z) {
            if (this.mNeedAlert) {
                UIHelper.toastError(i);
            }
            Activity activity = this.mActivityRef.get();
            if (activity != null) {
                if (i == -9) {
                    if (this.mNeedAlert) {
                        new AlreadyApplyListener(activity, path, this.mFinishActivity);
                        return;
                    }
                    AbsListener.removeNotification();
                    tryToFinishActivity();
                    AlbumShareInvitationHandler.startShareAlbumView(activity, path);
                    return;
                }
                activity.finish();
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class CancelListener extends AbsListener implements DialogInterface.OnCancelListener {
        public CancelListener(Activity activity, boolean z) {
            super(activity, z);
        }

        @Override // android.content.DialogInterface.OnCancelListener
        public void onCancel(DialogInterface dialogInterface) {
            tryToFinishActivity();
        }
    }

    public static void startShareAlbumView(Activity activity, Path path) {
        Intent intent = new Intent(!TextUtils.isEmpty(path.getMediaSet().getSharerInfo()) ? "com.miui.gallery.action.VIEW_SHARED_BABY_ALBUM" : "com.miui.gallery.action.VIEW_SHARED_ALBUM");
        intent.setFlags(268435456);
        intent.putExtra("album_id", ShareAlbumHelper.getUniformAlbumId(path.getId()));
        intent.putExtra("album_name", path.getMediaSet().getDisplayName());
        intent.putExtra("other_share_album", true);
        try {
            activity.startActivity(intent);
            SyncUtil.requestSync(StaticContext.sGetAndroidContext(), new SyncRequest.Builder().setSyncType(SyncType.NORMAL).setSyncReason(17L).build());
        } catch (ActivityNotFoundException unused) {
        }
    }

    public static void startCloudPage(Activity activity) {
        Intent intent = new Intent("com.miui.gallery.action.VIEW_ALBUM");
        intent.setFlags(67108864);
        try {
            activity.startActivity(intent);
        } catch (ActivityNotFoundException unused) {
        }
    }
}
