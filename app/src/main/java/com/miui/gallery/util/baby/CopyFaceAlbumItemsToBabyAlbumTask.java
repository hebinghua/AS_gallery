package com.miui.gallery.util.baby;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import com.miui.gallery.R;
import com.miui.gallery.cloud.SpaceFullHandler;
import com.miui.gallery.model.SendToCloudFolderItem;
import com.miui.gallery.provider.FaceManager;
import com.miui.gallery.share.Path;
import com.miui.gallery.share.UIHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.ToastUtils;

/* loaded from: classes2.dex */
public class CopyFaceAlbumItemsToBabyAlbumTask extends ProgressDialogTask {
    public Activity mActivity;
    public final SendToCloudFolderItem mFolderItem;
    public ProgressEndListener mListener;
    public int mProgress;
    public Cursor mSourceItems;

    /* loaded from: classes2.dex */
    public interface ProgressEndListener {
        void onProgressEnd(int i);
    }

    @Override // com.miui.gallery.util.baby.ProgressDialogTask
    public /* bridge */ /* synthetic */ void dismissDialog() {
        super.dismissDialog();
    }

    public CopyFaceAlbumItemsToBabyAlbumTask(Activity activity, Cursor cursor, SendToCloudFolderItem sendToCloudFolderItem, int i) {
        super(activity, cursor.getCount(), i, 0, false, true);
        this.mSourceItems = cursor;
        this.mFolderItem = sendToCloudFolderItem;
        this.mActivity = activity;
    }

    public void setProgressFinishListener(ProgressEndListener progressEndListener) {
        this.mListener = progressEndListener;
    }

    @Override // android.os.AsyncTask
    public Void doInBackground(Void... voidArr) {
        this.mProgress = 0;
        Cursor cursor = this.mSourceItems;
        if (cursor == null) {
            return null;
        }
        while (cursor.moveToNext()) {
            if (handleOneFile(FaceManager.changeCursorData2ContentValuesOfCloudTable(cursor, this.mFolderItem.getLocalGroupId())) == 0) {
                this.mProgress++;
                if (isDialogShowing()) {
                    publishProgress(Integer.valueOf(this.mProgress));
                }
                if (isCancelled()) {
                    break;
                }
            }
        }
        BaseMiscUtil.closeSilently(cursor);
        return null;
    }

    public final int handleOneFile(ContentValues contentValues) {
        int i = SpaceFullHandler.isOwnerSpaceFull() ? 2 : 0;
        return i != 0 ? i : !FaceManager.localCopyFaceImages2BabyAlbum(contentValues, this.mFolderItem.isShareAlbum()) ? 1 : 0;
    }

    @Override // android.os.AsyncTask
    public void onPostExecute(Void r6) {
        super.onPostExecute(r6);
        ProgressEndListener progressEndListener = this.mListener;
        if (progressEndListener != null) {
            progressEndListener.onProgressEnd(this.mProgress);
            return;
        }
        Activity activity = this.mActivity;
        if (activity == null || activity.isDestroyed()) {
            return;
        }
        Activity activity2 = this.mActivity;
        ToastUtils.makeTextLong(activity2, activity2.getString(R.string.begin_share, new Object[]{this.mFolderItem.getFolderName()}));
        UIHelper.showAlbumShareInfo(this.mActivity, new Path(Long.parseLong(this.mFolderItem.getLocalGroupId()), false, true), 0);
        this.mActivity.finish();
    }

    @Override // com.miui.gallery.util.baby.ProgressDialogTask, android.os.AsyncTask
    public void onCancelled() {
        super.onCancelled();
        ProgressEndListener progressEndListener = this.mListener;
        if (progressEndListener != null) {
            progressEndListener.onProgressEnd(this.mProgress);
        }
    }

    public static CopyFaceAlbumItemsToBabyAlbumTask instance(Activity activity, Cursor cursor, SendToCloudFolderItem sendToCloudFolderItem, int i, int i2) {
        return new CopyFaceAlbumItemsToBabyAlbumTask(activity, cursor, sendToCloudFolderItem, i2);
    }
}
