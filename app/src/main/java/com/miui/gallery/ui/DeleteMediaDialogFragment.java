package com.miui.gallery.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.DeletionTask;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.widget.GalleryDialogFragment;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.MiStat;
import java.util.HashMap;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class DeleteMediaDialogFragment extends GalleryDialogFragment {
    public DeletionTask mDeleteTask;
    public DeletionTask.OnDeletionCompleteListener mDeletionFinishListener;
    public DialogInterface.OnDismissListener mDismissListener;
    public boolean mExistXiaomiAccount;
    public boolean mIsFirstDelete;
    public DeletionTask.Param mParam;
    public static final int[] TYPE_RESOURCE_MATRIX = {R.plurals.delete_media_from_local, R.plurals.delete_media_from_all_devices_and_cloud_msg};
    public static final int[] TYPE_BURST_RESOURCE_MATRIX = {R.plurals.delete_burst_photo_from_local, R.plurals.delete_burst_photo_from_all_devices_and_cloud_msg};

    public static DeleteMediaDialogFragment newInstance(DeletionTask.Param param) {
        DeleteMediaDialogFragment deleteMediaDialogFragment = new DeleteMediaDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("delete_params", param);
        deleteMediaDialogFragment.setArguments(bundle);
        return deleteMediaDialogFragment;
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        this.mParam = (DeletionTask.Param) getArguments().getSerializable("delete_params");
        TrackController.trackExpose("403.45.0.1.11237", getSourceTip());
        boolean existXiaomiAccount = SyncUtil.existXiaomiAccount(getActivity());
        this.mExistXiaomiAccount = existXiaomiAccount;
        if (existXiaomiAccount) {
            if (isOperateHomePage(this.mParam)) {
                if (GalleryPreferences.Delete.isFirstDeleteFromHomePage()) {
                    this.mIsFirstDelete = true;
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    View inflate = getActivity().getLayoutInflater().inflate(R.layout.first_delete, (ViewGroup) null);
                    builder.setView(inflate);
                    ImageView imageView = (ImageView) inflate.findViewById(R.id.delete_tip_icon);
                    if (!needShowIcon(getResources().getConfiguration())) {
                        imageView.setVisibility(8);
                    }
                    ((TextView) inflate.findViewById(R.id.delete_sub_title)).setText(getString(R.string.delete_in_home_page, 30));
                    DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.DeleteMediaDialogFragment.1
                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (i == -1) {
                                TimeMonitor.createNewTimeMonitor("403.45.0.1.13761");
                                DeleteMediaDialogFragment.this.doDelete();
                            }
                            if (i == -2) {
                                DeleteMediaDialogFragment.this.statDelete(false);
                                DeleteMediaDialogFragment.this.trackCancelDelete();
                            }
                        }
                    };
                    builder.setPositiveButton(R.string.delete, onClickListener);
                    builder.setNegativeButton(17039360, onClickListener);
                    return builder.create();
                }
            } else if (isOperateAlbum(this.mParam) && !isLocalMode(this.mParam.mAlbumId) && GalleryPreferences.Delete.isFirstDeleteFromAlbum()) {
                this.mIsFirstDelete = true;
                AlertDialog.Builder builder2 = new AlertDialog.Builder(getActivity());
                View inflate2 = getActivity().getLayoutInflater().inflate(R.layout.first_delete, (ViewGroup) null);
                builder2.setView(inflate2);
                ImageView imageView2 = (ImageView) inflate2.findViewById(R.id.delete_tip_icon);
                if (getResources().getConfiguration().orientation == 2) {
                    imageView2.setVisibility(8);
                }
                ((TextView) inflate2.findViewById(R.id.delete_sub_title)).setText(getString(R.string.delete_in_home_page, 30));
                DialogInterface.OnClickListener onClickListener2 = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.DeleteMediaDialogFragment.2
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == -1) {
                            TimeMonitor.createNewTimeMonitor("403.45.0.1.13761");
                            DeleteMediaDialogFragment.this.doDelete();
                        }
                        if (i == -2) {
                            DeleteMediaDialogFragment.this.statDelete(false);
                            DeleteMediaDialogFragment.this.trackCancelDelete();
                        }
                    }
                };
                builder2.setPositiveButton(R.string.delete, onClickListener2);
                builder2.setNegativeButton(17039360, onClickListener2);
                return builder2.create();
            }
        }
        AlertDialog.Builder builder3 = new AlertDialog.Builder(getActivity());
        if (shouldShowCheckbox()) {
            builder3.setCheckBox(false, getString(R.string.delete_from_cloud));
        }
        return builder3.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.DeleteMediaDialogFragment.4
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                DeleteMediaDialogFragment.this.mParam.mDeleteOptions = 0;
                AlertDialog alertDialog = (AlertDialog) dialogInterface;
                if (DeleteMediaDialogFragment.this.shouldShowCheckbox() && !alertDialog.isChecked()) {
                    DeleteMediaDialogFragment.this.mParam.mDeleteOptions = 1;
                }
                TimeMonitor.createNewTimeMonitor("403.45.0.1.13761");
                DeleteMediaDialogFragment.this.doDelete();
            }
        }).setNegativeButton(17039360, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.DeleteMediaDialogFragment.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                DeleteMediaDialogFragment.this.statDelete(false);
                DeleteMediaDialogFragment.this.trackCancelDelete();
            }
        }).setTitle(getString(R.string.delete)).setMessage(genDeleteDialogMessage()).create();
    }

    @Override // com.miui.gallery.widget.GalleryDialogFragment, androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        DialogInterface.OnDismissListener onDismissListener = this.mDismissListener;
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialogInterface);
        }
        DeletionTask deletionTask = this.mDeleteTask;
        if (deletionTask == null || deletionTask.mIsDone) {
            return;
        }
        this.mDeleteTask.showProgress(getActivity());
    }

    public final void statDelete(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put(MiStat.Param.COUNT, String.valueOf(this.mParam.mIds.length));
        if (isOperateHomePage(this.mParam)) {
            hashMap.put("from", "home");
        } else if (isOperateAlbum(this.mParam)) {
            hashMap.put("from", AlbumDataHelper.isSystemAlbum(String.valueOf(this.mParam.mAlbumId)) ? "System" : "Others");
        } else {
            hashMap.put("from", "photo");
        }
        SamplingStatHelper.recordCountEvent("delete_dialog", z ? "sure" : "cancel", hashMap);
    }

    public final void trackCancelDelete() {
        TrackController.trackClick("403.45.0.1.11239", getSourceTip());
        HashMap hashMap = new HashMap();
        if (isOperateHomePage(this.mParam)) {
            hashMap.put("tip", "403.1.8.1.9892");
            hashMap.put("ref_tip", "403.1.8.1.9891");
        } else if (!isOperateAlbum(this.mParam)) {
            return;
        } else {
            if (isOperateAllPhotoAlbum(this.mParam)) {
                hashMap.put("tip", "403.44.3.1.11223");
                hashMap.put("ref_tip", "403.44.3.1.11229");
            } else if (isOperateBabyAlbum(this.mParam)) {
                hashMap.put("tip", "403.42.3.1.11301");
                hashMap.put("ref_tip", "403.42.3.1.11305");
            } else {
                hashMap.put("tip", "403.15.3.1.11194");
                hashMap.put("ref_tip", "403.15.3.1.11200");
            }
        }
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "cancel");
        hashMap.put(MiStat.Param.COUNT, String.valueOf(this.mParam.mIds.length));
        TrackController.trackClick(hashMap);
    }

    public final boolean isOperateHomePage(DeletionTask.Param param) {
        return param.mSource == 1;
    }

    public final boolean isOperateAlbum(DeletionTask.Param param) {
        if (!isOperateHomePage(param)) {
            long j = param.mAlbumId;
            if (j > 0 || j == -1000) {
                return true;
            }
        }
        return false;
    }

    public final boolean isOperateAllPhotoAlbum(DeletionTask.Param param) {
        return param.mSource == 2;
    }

    public final boolean isOperateBabyAlbum(DeletionTask.Param param) {
        return param.mSource == 3;
    }

    public static boolean isLocalMode(long j) {
        return GalleryPreferences.LocalMode.isOnlyShowLocalPhoto() && j != -1000;
    }

    public final boolean shouldShowCheckbox() {
        return this.mExistXiaomiAccount && isOperateAlbum(this.mParam) && isLocalMode(this.mParam.mAlbumId);
    }

    public final boolean isSyncable() {
        return this.mExistXiaomiAccount && SyncUtil.isGalleryCloudSyncable(getActivity().getApplicationContext());
    }

    public final String genDeleteDialogMessage() {
        int i = (!isSyncable() || shouldShowCheckbox()) ? 0 : 1;
        return this.mParam.mIsBurstItems ? getResources().getQuantityString(getBurstMessageRes(i), this.mParam.getItemsCount(), Integer.valueOf(this.mParam.getItemsCount())) : getResources().getQuantityString(getMessageRes(i), this.mParam.getItemsCount(), Integer.valueOf(this.mParam.getItemsCount()));
    }

    @Override // com.miui.gallery.widget.GalleryDialogFragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        Dialog dialog = getDialog();
        if (dialog == null || !this.mIsFirstDelete || dialog.findViewById(R.id.delete_tip_icon) == null) {
            return;
        }
        dialog.findViewById(R.id.delete_tip_icon).setVisibility(needShowIcon(configuration) ? 0 : 8);
    }

    public final boolean needShowIcon(Configuration configuration) {
        return configuration.screenHeightDp >= BaseBuildUtil.BIG_HORIZONTAL_WINDOW_STANDARD || configuration.orientation == 1;
    }

    public final void doDelete() {
        DeletionTask deletionTask = new DeletionTask();
        this.mDeleteTask = deletionTask;
        deletionTask.setFragmentActivityForStoragePermissionMiss(getActivity());
        this.mDeleteTask.setOnDeletionCompleteListener(this.mDeletionFinishListener);
        this.mDeleteTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this.mParam);
        statDelete(true);
        TrackController.trackClick("403.45.0.1.11238", getSourceTip());
    }

    public void setOnDeletionCompleteListener(DeletionTask.OnDeletionCompleteListener onDeletionCompleteListener) {
        this.mDeletionFinishListener = onDeletionCompleteListener;
    }

    public void setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.mDismissListener = onDismissListener;
    }

    public final int getMessageRes(int i) {
        return TYPE_RESOURCE_MATRIX[i];
    }

    public final int getBurstMessageRes(int i) {
        return TYPE_BURST_RESOURCE_MATRIX[i];
    }

    public final String getSourceTip() {
        return isOperateHomePage(this.mParam) ? "403.1.8.1.9892" : isOperateAlbum(this.mParam) ? isOperateAllPhotoAlbum(this.mParam) ? "403.44.3.1.11223" : isOperateBabyAlbum(this.mParam) ? "403.42.3.1.11301" : "403.15.3.1.11194" : "403.11.5.1.11162";
    }
}
