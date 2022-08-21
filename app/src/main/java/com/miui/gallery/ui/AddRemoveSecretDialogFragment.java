package com.miui.gallery.ui;

import android.accounts.Account;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.miui.account.AccountHelper;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.CloudUtils;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.util.CheckDownloadOriginHelper;
import com.miui.gallery.util.GalleryIntent$CloudGuideSource;
import com.miui.gallery.util.MediaAndAlbumOperations;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.GalleryDialogFragment;
import com.miui.privacy.LockSettingsHelper;
import java.util.ArrayList;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes2.dex */
public class AddRemoveSecretDialogFragment extends GalleryDialogFragment {
    public long mAlbumId;
    public boolean mHasVideo;
    public MediaAndAlbumOperations.OnCompleteListener mListener;
    public long[] mMediaIds;
    public ArrayList<Uri> mMediaUris;
    public int mOperationType;
    public ProgressDialog mProgressDialog;

    public static void add(FragmentActivity fragmentActivity, MediaAndAlbumOperations.OnCompleteListener onCompleteListener, boolean z, long... jArr) {
        AddRemoveSecretDialogFragment addRemoveSecretDialogFragment = new AddRemoveSecretDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("arg_operation_type", 1);
        bundle.putLongArray("arg_media_ids", jArr);
        bundle.putBoolean("arg_has_video", z);
        addRemoveSecretDialogFragment.setArguments(bundle);
        addRemoveSecretDialogFragment.setOnCompleteListener(onCompleteListener);
        addRemoveSecretDialogFragment.showAllowingStateLoss(fragmentActivity.getSupportFragmentManager(), "AddRemoveSecretDialogFragment");
    }

    public static void add(FragmentActivity fragmentActivity, MediaAndAlbumOperations.OnCompleteListener onCompleteListener, boolean z, ArrayList<Uri> arrayList) {
        AddRemoveSecretDialogFragment addRemoveSecretDialogFragment = new AddRemoveSecretDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("arg_operation_type", 1);
        bundle.putParcelableArrayList("arg_media_uris", arrayList);
        bundle.putBoolean("arg_has_video", z);
        addRemoveSecretDialogFragment.setArguments(bundle);
        addRemoveSecretDialogFragment.setOnCompleteListener(onCompleteListener);
        addRemoveSecretDialogFragment.showAllowingStateLoss(fragmentActivity.getSupportFragmentManager(), "AddRemoveSecretDialogFragment");
    }

    public static void remove(FragmentActivity fragmentActivity, MediaAndAlbumOperations.OnCompleteListener onCompleteListener, long j, long... jArr) {
        AddRemoveSecretDialogFragment addRemoveSecretDialogFragment = new AddRemoveSecretDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("arg_operation_type", 2);
        bundle.putLongArray("arg_media_ids", jArr);
        bundle.putLong("arg_album_id", j);
        addRemoveSecretDialogFragment.setArguments(bundle);
        addRemoveSecretDialogFragment.setOnCompleteListener(onCompleteListener);
        addRemoveSecretDialogFragment.showAllowingStateLoss(fragmentActivity.getSupportFragmentManager(), "AddRemoveSecretDialogFragment");
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        int i = getArguments().getInt("arg_operation_type");
        this.mOperationType = i;
        if (i == 1) {
            this.mMediaIds = getArguments().getLongArray("arg_media_ids");
            ArrayList<Uri> parcelableArrayList = getArguments().getParcelableArrayList("arg_media_uris");
            this.mMediaUris = parcelableArrayList;
            if (this.mMediaIds == null && parcelableArrayList == null) {
                throw new IllegalArgumentException("ids or uris can't be null");
            }
            this.mHasVideo = getArguments().getBoolean("arg_has_video", false);
        } else if (i == 2) {
            long j = getArguments().getLong("arg_album_id");
            this.mAlbumId = j;
            if (j <= 0) {
                throw new IllegalArgumentException("albumId must > 0");
            }
            long[] longArray = getArguments().getLongArray("arg_media_ids");
            this.mMediaIds = longArray;
            if (longArray == null) {
                throw new IllegalArgumentException("ids can't be null");
            }
        } else {
            throw new IllegalArgumentException("unsupport operation");
        }
        setCancelable(false);
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        ProgressDialog show = ProgressDialog.show(getActivity(), "", getActivity().getString(R.string.adding_to_album), true, false);
        this.mProgressDialog = show;
        return show;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        addOrRemoveSecret();
    }

    public final void checkPrivateGalleryEnabled() {
        Account xiaomiAccount = AccountHelper.getXiaomiAccount(getActivity());
        if (xiaomiAccount != null) {
            if (!ContentResolver.getSyncAutomatically(xiaomiAccount, "com.miui.gallery.cloud.provider") && this.mOperationType == 1 && !SyncUtil.setSyncAutomatically(getActivity(), true)) {
                return;
            }
            if (!new LockSettingsHelper(getActivity()).isPrivacyPasswordEnabled()) {
                GalleryPreferences.Secret.setIsFirstAddSecret(true);
                GalleryPreferences.Secret.setFirstAddSecretVideo(true);
                LockSettingsHelper.startSetPrivacyPasswordActivity(this, 28);
                return;
            }
            doAddOrRemoveSecret();
        }
    }

    public final void doAddOrRemoveSecret() {
        if (this.mOperationType != 2 || this.mMediaIds == null) {
            startAddOrRemoveSecretTask();
            return;
        }
        CheckDownloadOriginHelper checkDownloadOriginHelper = new CheckDownloadOriginHelper(getActivity(), getFragmentManager(), this.mAlbumId, this.mMediaIds);
        checkDownloadOriginHelper.setListener(new CheckDownloadOriginHelper.CheckDownloadOriginListener() { // from class: com.miui.gallery.ui.AddRemoveSecretDialogFragment.1
            @Override // com.miui.gallery.util.CheckDownloadOriginHelper.CheckDownloadOriginListener
            public void onStartDownload() {
                AddRemoveSecretDialogFragment.this.mProgressDialog.hide();
            }

            @Override // com.miui.gallery.util.CheckDownloadOriginHelper.CheckDownloadOriginListener
            public void onComplete() {
                AddRemoveSecretDialogFragment.this.startAddOrRemoveSecretTask();
            }

            @Override // com.miui.gallery.util.CheckDownloadOriginHelper.CheckDownloadOriginListener
            public void onCanceled() {
                if (AddRemoveSecretDialogFragment.this.mListener != null) {
                    AddRemoveSecretDialogFragment.this.mListener.onComplete(null);
                }
                AddRemoveSecretDialogFragment.this.dismissAllowingStateLoss();
            }
        });
        checkDownloadOriginHelper.start();
    }

    public final void startAddOrRemoveSecretTask() {
        this.mProgressDialog.show();
        new AddRemoveTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public final void addOrRemoveSecret() {
        Bundle bundle = new Bundle();
        if (this.mOperationType == 1) {
            bundle.putBoolean("key_check_gallery_sync", true);
        }
        bundle.putSerializable("cloud_guide_source", GalleryIntent$CloudGuideSource.SECRET);
        LoginAndSyncCheckFragment.checkLoginAndSyncState(this, bundle);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i2 != -1) {
            MediaAndAlbumOperations.OnCompleteListener onCompleteListener = this.mListener;
            if (onCompleteListener != null) {
                onCompleteListener.onComplete(null);
            }
            dismissAllowingStateLoss();
        } else if (i == 28) {
            doAddOrRemoveSecret();
        } else if (i != 29) {
        } else {
            checkPrivateGalleryEnabled();
        }
    }

    public void setOnCompleteListener(MediaAndAlbumOperations.OnCompleteListener onCompleteListener) {
        this.mListener = onCompleteListener;
    }

    /* loaded from: classes2.dex */
    public class AddRemoveTask extends AsyncTask<Void, Void, long[]> {
        public AddRemoveTask() {
        }

        @Override // android.os.AsyncTask
        public long[] doInBackground(Void... voidArr) {
            try {
                return addOrRemove();
            } catch (StoragePermissionMissingException e) {
                e.offer(AddRemoveSecretDialogFragment.this.getActivity());
                return null;
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:30:0x009f  */
        @Override // android.os.AsyncTask
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public void onPostExecute(long[] r6) {
            /*
                r5 = this;
                com.miui.gallery.ui.AddRemoveSecretDialogFragment r0 = com.miui.gallery.ui.AddRemoveSecretDialogFragment.this
                com.miui.gallery.util.MediaAndAlbumOperations$OnCompleteListener r0 = com.miui.gallery.ui.AddRemoveSecretDialogFragment.access$200(r0)
                if (r0 == 0) goto L11
                com.miui.gallery.ui.AddRemoveSecretDialogFragment r0 = com.miui.gallery.ui.AddRemoveSecretDialogFragment.this
                com.miui.gallery.util.MediaAndAlbumOperations$OnCompleteListener r0 = com.miui.gallery.ui.AddRemoveSecretDialogFragment.access$200(r0)
                r0.onComplete(r6)
            L11:
                com.miui.gallery.ui.AddRemoveSecretDialogFragment r0 = com.miui.gallery.ui.AddRemoveSecretDialogFragment.this
                androidx.fragment.app.FragmentActivity r0 = r0.getActivity()
                if (r0 != 0) goto L1a
                return
            L1a:
                android.util.Pair r6 = r5.outputResult(r6)
                java.lang.Object r0 = r6.second
                java.lang.String r0 = (java.lang.String) r0
                java.lang.Object r6 = r6.first
                java.lang.Boolean r6 = (java.lang.Boolean) r6
                boolean r6 = r6.booleanValue()
                r1 = 1
                r2 = 0
                if (r6 == 0) goto L9c
                com.miui.gallery.ui.AddRemoveSecretDialogFragment r6 = com.miui.gallery.ui.AddRemoveSecretDialogFragment.this
                int r6 = com.miui.gallery.ui.AddRemoveSecretDialogFragment.access$300(r6)
                if (r6 != r1) goto L9c
                boolean r6 = com.miui.gallery.preference.GalleryPreferences.Secret.isFirstAddSecret()
                if (r6 == 0) goto L57
                com.miui.gallery.ui.AddRemoveSecretDialogFragment r6 = com.miui.gallery.ui.AddRemoveSecretDialogFragment.this
                boolean r6 = com.miui.gallery.ui.AddRemoveSecretDialogFragment.access$400(r6)
                if (r6 == 0) goto L9c
                boolean r6 = com.miui.gallery.preference.GalleryPreferences.Secret.isFirstAddSecretVideo()
                if (r6 == 0) goto L9c
                com.miui.gallery.ui.AddRemoveSecretDialogFragment r6 = com.miui.gallery.ui.AddRemoveSecretDialogFragment.this
                r0 = 2131887594(0x7f1205ea, float:1.94098E38)
                java.lang.String r0 = r6.getString(r0)
                com.miui.gallery.preference.GalleryPreferences.Secret.setFirstAddSecretVideo(r2)
                goto L9d
            L57:
                boolean r6 = android.text.TextUtils.isEmpty(r0)
                if (r6 != 0) goto L80
                r6 = 3
                java.lang.Object[] r6 = new java.lang.Object[r6]
                r6[r2] = r0
                com.miui.gallery.ui.AddRemoveSecretDialogFragment r0 = com.miui.gallery.ui.AddRemoveSecretDialogFragment.this
                r3 = 2131888204(0x7f12084c, float:1.9411037E38)
                java.lang.String r0 = r0.getString(r3)
                r6[r1] = r0
                r0 = 2
                com.miui.gallery.ui.AddRemoveSecretDialogFragment r3 = com.miui.gallery.ui.AddRemoveSecretDialogFragment.this
                r4 = 2131886274(0x7f1200c2, float:1.9407122E38)
                java.lang.String r3 = r3.getString(r4)
                r6[r0] = r3
                java.lang.String r0 = "%s%s%s"
                java.lang.String r6 = java.lang.String.format(r0, r6)
                goto L89
            L80:
                com.miui.gallery.ui.AddRemoveSecretDialogFragment r6 = com.miui.gallery.ui.AddRemoveSecretDialogFragment.this
                r0 = 2131886273(0x7f1200c1, float:1.940712E38)
                java.lang.String r6 = r6.getString(r0)
            L89:
                r0 = r6
                com.miui.gallery.ui.AddRemoveSecretDialogFragment r6 = com.miui.gallery.ui.AddRemoveSecretDialogFragment.this
                boolean r6 = com.miui.gallery.ui.AddRemoveSecretDialogFragment.access$400(r6)
                if (r6 == 0) goto L9d
                boolean r6 = com.miui.gallery.preference.GalleryPreferences.Secret.isFirstAddSecretVideo()
                if (r6 == 0) goto L9d
                com.miui.gallery.preference.GalleryPreferences.Secret.setFirstAddSecretVideo(r2)
                goto L9d
            L9c:
                r1 = r2
            L9d:
                if (r0 == 0) goto La8
                com.miui.gallery.ui.AddRemoveSecretDialogFragment r6 = com.miui.gallery.ui.AddRemoveSecretDialogFragment.this
                androidx.fragment.app.FragmentActivity r6 = r6.getActivity()
                com.miui.gallery.util.ToastUtils.makeText(r6, r0, r1)
            La8:
                com.miui.gallery.ui.AddRemoveSecretDialogFragment r6 = com.miui.gallery.ui.AddRemoveSecretDialogFragment.this
                r6.dismissAllowingStateLoss()
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.ui.AddRemoveSecretDialogFragment.AddRemoveTask.onPostExecute(long[]):void");
        }

        public final long[] addOrRemove() throws StoragePermissionMissingException {
            Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
            if (AddRemoveSecretDialogFragment.this.mOperationType == 1) {
                return AddRemoveSecretDialogFragment.this.mMediaIds == null ? CloudUtils.addToSecret(sGetAndroidContext, AddRemoveSecretDialogFragment.this.mMediaUris) : CloudUtils.addToSecret(sGetAndroidContext, AddRemoveSecretDialogFragment.this.mMediaIds);
            } else if (AddRemoveSecretDialogFragment.this.mOperationType != 2) {
                return null;
            } else {
                return CloudUtils.removeFromSecret(sGetAndroidContext, AddRemoveSecretDialogFragment.this.mAlbumId, AddRemoveSecretDialogFragment.this.mMediaIds);
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:99:0x0180  */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final android.util.Pair<java.lang.Boolean, java.lang.String> outputResult(long[] r21) {
            /*
                Method dump skipped, instructions count: 407
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.ui.AddRemoveSecretDialogFragment.AddRemoveTask.outputResult(long[]):android.util.Pair");
        }

        public final String getAddFailTip(Resources resources, int i, int i2, int i3) {
            if (i == -107) {
                return resources.getString(R.string.secret_reason_video_not_support);
            }
            String failReason = getFailReason(resources, i, i3);
            getSolution(resources, i);
            if (i2 > 1) {
                return resources.getQuantityString(R.plurals.add_to_secret_failed_with_reason_and_count, i3, failReason, Integer.valueOf(i3)) + getSolution(resources, i);
            }
            return resources.getString(R.string.add_to_secret_failed_with_reason, failReason) + getSolution(resources, i);
        }

        public final String getFailReason(Resources resources, int i, int i2) {
            if (i != -115) {
                if (i != -111) {
                    switch (i) {
                        case -108:
                            return resources.getString(R.string.secret_reason_too_large);
                        case -107:
                            return resources.getString(R.string.secret_reason_type_not_support);
                        case -106:
                            return resources.getString(R.string.secret_reason_space_full);
                        default:
                            DefaultLogger.e("AddRemoveSecretDialogFragment", "AddRemoveSecretFailReasonCode: %d", Integer.valueOf(i));
                            return resources.getString(R.string.secret_reason_unknow_reasons);
                    }
                }
                return resources.getQuantityString(R.plurals.fail_reason_processing_file, i2);
            }
            return resources.getString(R.string.recovery_not_synced_reason);
        }

        public final String getSolution(Resources resources, int i) {
            return i != -115 ? "" : resources.getString(R.string.recovery_not_synced_solution);
        }
    }
}
