package com.miui.gallery.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.CloudUtils;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.InternalContract$Cloud;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.CheckDownloadOriginHelper;
import com.miui.gallery.util.DialogUtil;
import com.miui.gallery.util.MediaAndAlbumOperations;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.Utils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.GalleryDialogFragment;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.MiStat;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes2.dex */
public class CopyMoveDialogFragment extends GalleryDialogFragment {
    public WeakReference<Activity> mActivityRef;
    public long mAlbumId;
    public int mCount;
    public boolean mDeleteOrigin;
    public MediaAndAlbumOperations.OnAddAlbumListener mListener;
    public long[] mMediaIds;
    public ArrayList<Uri> mMediaUris;
    public boolean mOperateSync;
    public ProgressDialog mProgressDialog;

    public static void show(FragmentActivity fragmentActivity, long j, long[] jArr, boolean z, MediaAndAlbumOperations.OnAddAlbumListener onAddAlbumListener) {
        show(fragmentActivity, j, jArr, z, onAddAlbumListener, false);
    }

    public static void show(FragmentActivity fragmentActivity, long j, long[] jArr, boolean z, MediaAndAlbumOperations.OnAddAlbumListener onAddAlbumListener, boolean z2) {
        CopyMoveDialogFragment copyMoveDialogFragment = new CopyMoveDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("arg_album_id", j);
        bundle.putLongArray("arg_media_ids", jArr);
        bundle.putBoolean("arg_delete_origin", z);
        bundle.putBoolean("arg_operate_sync", z2);
        copyMoveDialogFragment.setArguments(bundle);
        copyMoveDialogFragment.setOnCompleteListener(onAddAlbumListener);
        copyMoveDialogFragment.showAllowingStateLoss(fragmentActivity.getSupportFragmentManager(), "CopyMoveDialogFragment");
    }

    public static void show(FragmentActivity fragmentActivity, long j, ArrayList<Uri> arrayList, boolean z, MediaAndAlbumOperations.OnAddAlbumListener onAddAlbumListener) {
        CopyMoveDialogFragment copyMoveDialogFragment = new CopyMoveDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("arg_album_id", j);
        bundle.putParcelableArrayList("arg_media_uris", arrayList);
        bundle.putBoolean("arg_delete_origin", z);
        copyMoveDialogFragment.setArguments(bundle);
        copyMoveDialogFragment.setOnCompleteListener(onAddAlbumListener);
        copyMoveDialogFragment.showAllowingStateLoss(fragmentActivity.getSupportFragmentManager(), "CopyMoveDialogFragment");
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mAlbumId = getArguments().getLong("arg_album_id");
        this.mMediaIds = getArguments().getLongArray("arg_media_ids");
        this.mDeleteOrigin = getArguments().getBoolean("arg_delete_origin");
        this.mOperateSync = getArguments().getBoolean("arg_operate_sync", false);
        this.mMediaUris = getArguments().getParcelableArrayList("arg_media_uris");
        this.mActivityRef = new WeakReference<>(getActivity());
        setCancelable(false);
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        ProgressDialog show = ProgressDialog.show(this.mActivityRef.get(), "", this.mActivityRef.get().getString(R.string.adding_to_album), true, false);
        this.mProgressDialog = show;
        return show;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        doCheckOrigin();
    }

    public void setOnCompleteListener(MediaAndAlbumOperations.OnAddAlbumListener onAddAlbumListener) {
        this.mListener = onAddAlbumListener;
    }

    public final void doCheckOrigin() {
        if (this.mActivityRef.get() == null) {
            DefaultLogger.e("CopyMoveDialogFragment", "activity is null");
        } else if (this.mMediaIds == null) {
            DefaultLogger.d("CopyMoveDialogFragment", "media ids is null");
            doCopyMove();
        } else {
            CheckDownloadOriginHelper checkDownloadOriginHelper = new CheckDownloadOriginHelper(this.mActivityRef.get(), getFragmentManager(), this.mAlbumId, this.mMediaIds);
            checkDownloadOriginHelper.setListener(new CheckDownloadOriginHelper.CheckDownloadOriginListener() { // from class: com.miui.gallery.ui.CopyMoveDialogFragment.1
                @Override // com.miui.gallery.util.CheckDownloadOriginHelper.CheckDownloadOriginListener
                public void onStartDownload() {
                    CopyMoveDialogFragment.this.mProgressDialog.hide();
                }

                @Override // com.miui.gallery.util.CheckDownloadOriginHelper.CheckDownloadOriginListener
                public void onComplete() {
                    CopyMoveDialogFragment.this.doCopyMove();
                }

                @Override // com.miui.gallery.util.CheckDownloadOriginHelper.CheckDownloadOriginListener
                public void onCanceled() {
                    CopyMoveDialogFragment.this.cancelAndFinish();
                }
            });
            checkDownloadOriginHelper.start();
        }
    }

    public final void doCopyMove() {
        if (this.mActivityRef.get() == null || this.mActivityRef.get().isFinishing()) {
            return;
        }
        DefaultLogger.d("CopyMoveDialogFragment", "doCopyMove");
        try {
            this.mProgressDialog.show();
        } catch (Exception e) {
            DefaultLogger.w("CopyMoveDialogFragment", "show dialog ignore %s", e);
        }
        new CopyMoveTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public final void cancelAndFinish() {
        DefaultLogger.d("CopyMoveDialogFragment", "cancelAndFinish");
        MediaAndAlbumOperations.OnAddAlbumListener onAddAlbumListener = this.mListener;
        if (onAddAlbumListener != null) {
            onAddAlbumListener.onComplete(null, this.mDeleteOrigin);
        }
        dismissAllowingStateLoss();
    }

    /* loaded from: classes2.dex */
    public class CopyMoveTask extends AsyncTask<Void, List<IStoragePermissionStrategy.PermissionResult>, long[]> {
        public int mCloudPhotoCount;

        public CopyMoveTask() {
        }

        @Override // android.os.AsyncTask
        public long[] doInBackground(Void... voidArr) {
            if (GalleryPreferences.LocalMode.isOnlyShowLocalPhoto()) {
                this.mCloudPhotoCount = getCloudPhotoCount();
            }
            try {
                return copyOrMove();
            } catch (StoragePermissionMissingException e) {
                e.offer(CopyMoveDialogFragment.this.getActivity());
                return new long[]{-121};
            }
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(long[] jArr) {
            outputResult(jArr);
            CopyMoveDialogFragment.this.mProgressDialog.dismiss();
            int i = this.mCloudPhotoCount;
            if (i > 0) {
                showCloudPhotoCountDialog(i);
            }
            if (CopyMoveDialogFragment.this.mListener != null) {
                CopyMoveDialogFragment.this.mListener.onComplete(jArr, CopyMoveDialogFragment.this.mDeleteOrigin);
            }
        }

        public final long[] copyOrMove() throws StoragePermissionMissingException {
            if (CopyMoveDialogFragment.this.mActivityRef.get() == null) {
                return null;
            }
            if (CopyMoveDialogFragment.this.mMediaIds == null) {
                if (CopyMoveDialogFragment.this.mMediaUris == null) {
                    return null;
                }
                CopyMoveDialogFragment copyMoveDialogFragment = CopyMoveDialogFragment.this;
                copyMoveDialogFragment.mCount = copyMoveDialogFragment.mMediaUris.size();
                return CopyMoveDialogFragment.this.mDeleteOrigin ? CloudUtils.move((Context) CopyMoveDialogFragment.this.mActivityRef.get(), CopyMoveDialogFragment.this.mAlbumId, CopyMoveDialogFragment.this.mMediaUris) : CloudUtils.copy((Context) CopyMoveDialogFragment.this.mActivityRef.get(), CopyMoveDialogFragment.this.mAlbumId, CopyMoveDialogFragment.this.mMediaUris);
            }
            CopyMoveDialogFragment copyMoveDialogFragment2 = CopyMoveDialogFragment.this;
            copyMoveDialogFragment2.mCount = copyMoveDialogFragment2.mMediaIds.length;
            return CopyMoveDialogFragment.this.mDeleteOrigin ? CloudUtils.move((Context) CopyMoveDialogFragment.this.mActivityRef.get(), CopyMoveDialogFragment.this.mAlbumId, CopyMoveDialogFragment.this.mOperateSync, CopyMoveDialogFragment.this.mMediaIds) : CloudUtils.copy((Context) CopyMoveDialogFragment.this.mActivityRef.get(), CopyMoveDialogFragment.this.mAlbumId, CopyMoveDialogFragment.this.mMediaIds);
        }

        public final void outputResult(long[] jArr) {
            if (CopyMoveDialogFragment.this.mActivityRef.get() == null) {
                DefaultLogger.e("CopyMoveDialogFragment", "outputResult error,activity is null");
            } else if (jArr == null || jArr.length < 1) {
                ToastUtils.makeText((Context) CopyMoveDialogFragment.this.mActivityRef.get(), (int) R.string.add_to_album_failed);
                CopyMoveDialogFragment copyMoveDialogFragment = CopyMoveDialogFragment.this;
                copyMoveDialogFragment.trackCopyMoveOperation(false, copyMoveDialogFragment.mCount);
            } else {
                Resources resources = ((Activity) CopyMoveDialogFragment.this.mActivityRef.get()).getResources();
                int i = 0;
                int i2 = 0;
                int i3 = 0;
                int i4 = 0;
                int i5 = 0;
                int i6 = 0;
                for (long j : jArr) {
                    if (j > 0) {
                        i3++;
                    } else if (j == -103) {
                        i4++;
                    } else if (j == -118) {
                        i5++;
                    } else if (j == -121) {
                        i6++;
                    } else {
                        i++;
                        if (i2 == 0) {
                            i2 = (int) j;
                        }
                    }
                }
                String failReason = i > 0 ? getFailReason(resources, i2, i) : "";
                if (jArr.length > 1) {
                    if (i != 0) {
                        ToastUtils.makeText((Context) CopyMoveDialogFragment.this.mActivityRef.get(), resources.getQuantityString(R.plurals.add_to_album_failed_with_reason_and_count, i, failReason, Integer.valueOf(i)) + getSolution(resources, i2));
                        CopyMoveDialogFragment.this.trackCopyMoveOperation(false, i);
                    } else if (jArr.length == i4) {
                        ToastUtils.makeText((Context) CopyMoveDialogFragment.this.mActivityRef.get(), (int) R.string.add_to_album_exist);
                        CopyMoveDialogFragment.this.trackCopyMoveOperation(false, i4);
                    } else if (i6 == 0) {
                        ToastUtils.makeText((Context) CopyMoveDialogFragment.this.mActivityRef.get(), (int) R.string.add_to_album_success);
                        CopyMoveDialogFragment.this.trackCopyMoveOperation(true, i3);
                    }
                } else if (i3 == 1 || i5 == 1) {
                    CopyMoveDialogFragment.this.trackCopyMoveOperation(true, i3);
                    ToastUtils.makeText((Context) CopyMoveDialogFragment.this.mActivityRef.get(), (int) R.string.add_to_album_success);
                } else if (i4 == 1) {
                    CopyMoveDialogFragment.this.trackCopyMoveOperation(false, i4);
                    ToastUtils.makeText((Context) CopyMoveDialogFragment.this.mActivityRef.get(), (int) R.string.add_to_album_exist);
                } else if (i6 == 0) {
                    CopyMoveDialogFragment.this.trackCopyMoveOperation(false, i);
                    ToastUtils.makeText((Context) CopyMoveDialogFragment.this.mActivityRef.get(), resources.getString(R.string.add_to_album_failed_with_reason, failReason) + getSolution(resources, i2));
                }
            }
        }

        public final String getFailReason(Resources resources, int i, int i2) {
            if (i != -115) {
                if (i == -111) {
                    return resources.getQuantityString(R.plurals.fail_reason_processing_file, i2);
                }
                return resources.getString(R.string.secret_reason_unknow_reasons);
            }
            return resources.getString(R.string.recovery_not_synced_reason);
        }

        public final String getSolution(Resources resources, int i) {
            return i != -115 ? "" : resources.getString(R.string.recovery_not_synced_solution);
        }

        public final int getCloudPhotoCount() {
            int i = 0;
            if (CopyMoveDialogFragment.this.mActivityRef.get() != null && CopyMoveDialogFragment.this.mMediaIds != null) {
                Cursor cursor = null;
                try {
                    cursor = ((Activity) CopyMoveDialogFragment.this.mActivityRef.get()).getContentResolver().query(GalleryContract.Media.URI, new String[0], "_id IN (" + TextUtils.join(",", MiscUtil.arrayToList(CopyMoveDialogFragment.this.mMediaIds)) + ") AND " + InternalContract$Cloud.ALIAS_LOCAL_MEDIA, null, null);
                    if (cursor != null) {
                        i = CopyMoveDialogFragment.this.mMediaIds.length - cursor.getCount();
                    }
                } catch (Exception unused) {
                } catch (Throwable th) {
                    Utils.closeSilently(cursor);
                    throw th;
                }
                Utils.closeSilently(cursor);
            }
            return i;
        }

        public final void showCloudPhotoCountDialog(int i) {
            if (CopyMoveDialogFragment.this.mActivityRef.get() == null) {
                return;
            }
            DialogUtil.showInfoDialog((Context) CopyMoveDialogFragment.this.mActivityRef.get(), ((Activity) CopyMoveDialogFragment.this.mActivityRef.get()).getResources().getQuantityString(R.plurals.add_to_album_cloud_photo_count_tip, i, Integer.valueOf(i)), ((Activity) CopyMoveDialogFragment.this.mActivityRef.get()).getString(R.string.add_to_album_tip));
        }
    }

    public final void trackCopyMoveOperation(boolean z, int i) {
        if (getAlbumType() == null) {
            return;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("ref_tip", "403.26.0.1.11240");
        hashMap.put("tip", "403.26.0.1.11244");
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, getAlbumType());
        hashMap.put("status", this.mDeleteOrigin ? "move" : "copy");
        hashMap.put("success", Boolean.valueOf(z));
        hashMap.put(MiStat.Param.COUNT, Integer.valueOf(i));
        TrackController.trackClick(hashMap);
    }

    public final String getAlbumType() {
        if (Album.isSystemAlbum(String.valueOf(this.mAlbumId))) {
            return "system";
        }
        if (Album.isShareAlbum(this.mAlbumId)) {
            return "sharer";
        }
        if (Album.isVirtualAlbum(this.mAlbumId)) {
            return null;
        }
        return "owner";
    }
}
