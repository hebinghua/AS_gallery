package com.miui.gallery.cloud;

import android.accounts.Account;
import android.app.Activity;
import android.content.ContentResolver;
import android.os.AsyncTask;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.cloudcontrol.CloudControlRequestHelper;
import com.miui.gallery.push.GalleryPushManager;
import com.miui.gallery.util.DeleteDataUtil;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.recorder.RecorderManager;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes.dex */
public class DeleteAccount {
    public static boolean sNeedDeleteAfterSync = false;
    public static int sWipeDataStrategyAfterSync = 2;

    /* loaded from: classes.dex */
    public interface DeleteAccountListener {
        void onFinish();
    }

    public static void deleteAccountInTask(Activity activity, Account account, int i, DeleteAccountListener deleteAccountListener) {
        if (ContentResolver.isSyncActive(account, "com.miui.gallery.cloud.provider")) {
            sNeedDeleteAfterSync = true;
            sWipeDataStrategyAfterSync = i;
            SyncLogger.d("DeleteAccount", "cancel sync...");
            ContentResolver.cancelSync(account, "com.miui.gallery.cloud.provider");
        }
        new DeleteAccountTask(activity, i, deleteAccountListener).execute(new Void[0]);
    }

    /* loaded from: classes.dex */
    public static class DeleteAccountTask extends AsyncTask<Void, Integer, Void> {
        public Activity mActivity;
        public ProgressDialog mDialog;
        public DeleteAccountListener mListener;
        public int mWipeDataStrategy;

        public DeleteAccountTask(Activity activity, int i, DeleteAccountListener deleteAccountListener) {
            this.mActivity = activity;
            this.mWipeDataStrategy = i;
            this.mListener = deleteAccountListener;
        }

        @Override // android.os.AsyncTask
        public void onPreExecute() {
            Activity activity = this.mActivity;
            if (activity != null) {
                this.mDialog = GalleryUtils.showProgressDialog(activity, R.string.initializing_cloud, R.string.initializing_cloud, 0, false);
            }
        }

        @Override // android.os.AsyncTask
        public Void doInBackground(Void... voidArr) {
            DeleteAccount.executeDeleteAccount(this.mWipeDataStrategy);
            return null;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Void r1) {
            ProgressDialog progressDialog = this.mDialog;
            if (progressDialog != null) {
                progressDialog.dismiss();
                this.mDialog = null;
            }
            DeleteAccountListener deleteAccountListener = this.mListener;
            if (deleteAccountListener != null) {
                deleteAccountListener.onFinish();
            }
        }
    }

    public static boolean executeDeleteAccount(int i) {
        GalleryPushManager.getInstance().onDeleteAccount(GalleryApp.sGetAndroidContext());
        AlbumShareOperations.resetAccountInfo();
        SyncLogger.d("DeleteAccount", "reset AlbumShareOperations when account is deleted");
        RecorderManager.getInstance().onDeleteAccount();
        GallerySyncAdapterImpl.resetAccountInfo(null, null);
        boolean delete = DeleteDataUtil.delete(GalleryApp.sGetAndroidContext(), i);
        SyncLogger.d("DeleteAccount", "delete data result %s", Boolean.valueOf(delete));
        new CloudControlRequestHelper(GalleryApp.sGetAndroidContext()).execRequestSync();
        SyncLogger.i("DeleteAccount", "finish deleting account, strategy=" + i + ", result=" + delete);
        return delete;
    }

    public static void deleteAccountAfterSyncIfNeeded() {
        if (sNeedDeleteAfterSync) {
            executeDeleteAccount(sWipeDataStrategyAfterSync);
            sNeedDeleteAfterSync = false;
        }
    }
}
