package com.miui.gallery.ui.album.common.base;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;
import ch.qos.logback.core.joran.action.Action;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.base_optimization.support.UtilsMethodSupportDelegate;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.cloudmanager.method.album.DoReplaceAlbumCoverMethod;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.AddPhotosFragment;
import com.miui.gallery.ui.AlbumCreatorDialogFragment;
import com.miui.gallery.ui.AlbumRenameDialogFragment;
import com.miui.gallery.ui.AlertDialogFragment;
import com.miui.gallery.ui.BaseAlbumOperationDialogFragment;
import com.miui.gallery.ui.ConfirmDialog;
import com.miui.gallery.ui.album.common.CommonAlbumItemViewBean;
import com.miui.gallery.ui.album.common.ReplaceAlbumCoverUtils;
import com.miui.gallery.ui.album.common.base.BaseAlbumPagePresenter;
import com.miui.gallery.ui.album.main.usecase.DoChangeAlbumSortTypeCase;
import com.miui.gallery.ui.album.main.utils.AlbumTabSortImmersionMenuHelper;
import com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumManualHideResult;
import com.miui.gallery.util.AlbumSortHelper;
import com.miui.gallery.util.DialogUtil;
import com.miui.gallery.util.MediaAndAlbumOperations;
import com.miui.gallery.util.SoundUtils;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.TimerDialog;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.b.h;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public abstract class BaseAlbumPageFragment<P extends BaseAlbumPagePresenter> extends BaseAlbumPageContract$V<P> {
    public static Boolean sIsGalleryCloudSyncable;
    public Collection<Album> mOperationAlbums;

    public static /* synthetic */ void $r8$lambda$AC73YDe1u11DARVyfYaPUKX0jeU(BaseAlbumPageFragment baseAlbumPageFragment, Collection collection, DialogInterface dialogInterface, int i) {
        baseAlbumPageFragment.lambda$doAlbumRemoveFromOtherAlbums$0(collection, dialogInterface, i);
    }

    public void onOperationEnd() {
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        RecyclerView recyclerView = this.mRecyclerView;
        if (recyclerView != null) {
            registerForContextMenu(recyclerView);
        }
    }

    @Override // com.miui.gallery.base_optimization.mvp.view.Fragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        RecyclerView recyclerView = this.mRecyclerView;
        if (recyclerView != null) {
            unregisterForContextMenu(recyclerView);
        }
        sIsGalleryCloudSyncable = null;
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$V
    public void onAddNoMediaForAlbumFailed(int i, Collection<Album> collection) {
        onOperationEnd();
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$V
    public void onAddNoMediaForAlbumSuccess(Collection<Album> collection, RubbishAlbumManualHideResult rubbishAlbumManualHideResult) {
        onOperationEnd();
        recordAlbumOperation("add_no_media", collection);
    }

    public void doCreateAlbum() {
        AlbumCreatorDialogFragment newInstance = AlbumCreatorDialogFragment.newInstance();
        newInstance.setOnAlbumOperationListener(new BaseAlbumOperationDialogFragment.OnAlbumOperationListener() { // from class: com.miui.gallery.ui.album.common.base.BaseAlbumPageFragment.1
            {
                BaseAlbumPageFragment.this = this;
            }

            @Override // com.miui.gallery.ui.BaseAlbumOperationDialogFragment.OnAlbumOperationListener
            public void onOperationDone(long j, String str, Bundle bundle) {
                BaseAlbumPageFragment.this.onCreateAlbumOperationDone(j, str, bundle);
            }
        });
        newInstance.showAllowingStateLoss(getFragmentManager(), "AlbumCreatorDialogFragment");
        SamplingStatHelper.recordCountEvent("album", "create_album");
        TrackController.trackClick("403.7.0.1.10330", "403.7.0.1.10328");
    }

    public void onCreateAlbumOperationDone(long j, String str, Bundle bundle) {
        if (j > 0 && isAdded()) {
            final Album album = (Album) bundle.getParcelable("album_source");
            onCreateAlbumIsSuccess(album);
            AddPhotosFragment.addPhotos(this, j, new MediaAndAlbumOperations.OnAddAlbumListener() { // from class: com.miui.gallery.ui.album.common.base.BaseAlbumPageFragment.2
                {
                    BaseAlbumPageFragment.this = this;
                }

                @Override // com.miui.gallery.util.MediaAndAlbumOperations.OnAddAlbumListener
                public void onComplete(long[] jArr, boolean z) {
                    Album album2 = album;
                    if (album2 == null || jArr == null || jArr.length <= 0) {
                        return;
                    }
                    album2.setPhotoCount(jArr.length);
                    BaseAlbumPageFragment.this.onCreateAlbumIsSuccess(album);
                }
            });
            return;
        }
        onCreateAlbumIsFailed(0, str);
    }

    public void onCreateAlbumIsFailed(int i, String str) {
        onOperationEnd();
    }

    public void onCreateAlbumIsSuccess(Album album) {
        BaseViewBean findDataById = findDataById(album.getAlbumId());
        if (findDataById == null) {
            ((BaseAlbumPagePresenter) getPresenter()).addAlbum(album);
        } else if (findDataById instanceof CommonAlbumItemViewBean) {
            CommonAlbumItemViewBean commonAlbumItemViewBean = new CommonAlbumItemViewBean();
            commonAlbumItemViewBean.mapping((CommonAlbumItemViewBean) album);
            updateDataByIdIfNeed(findDataById.getId(), commonAlbumItemViewBean);
        }
        recordAlbumOperation("create_album", album);
        onOperationEnd();
    }

    public void doChangeAlbumsShowInPhotosTab(boolean z, Collection<Album> collection) {
        ((BaseAlbumPagePresenter) getPresenter()).doChangeAlbumsShowInPhotosTab(z, collection);
        TrackController.trackClick("403.7.4.1.10345", "403.7.4.1.10542", collection.size());
        onOperationEnd();
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$V
    public void onAlbumShowInPhotoTabSuccess(long[] jArr, Collection<Album> collection) {
        ((BaseAlbumPagePresenter) getPresenter()).updateAlbumShowInPhotoTab(true, jArr);
        recordAlbumOperation("show_in_home_enable", collection);
        onOperationEnd();
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$V
    public void onAlbumShowInPhotoTabFailed(int i, Collection<Album> collection) {
        onOperationEnd();
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$V
    public void onCancelAlbumShowInPhotoTabSuccess(long[] jArr, Collection<Album> collection) {
        ((BaseAlbumPagePresenter) getPresenter()).updateAlbumShowInPhotoTab(false, jArr);
        recordAlbumOperation("show_in_home_disable", collection);
        onOperationEnd();
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$V
    public void onCancelAlbumShowInPhotoTabFailed(int i, Collection<Album> collection) {
        onOperationEnd();
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$V
    public void onChangeAlbumBackupStatusFailed(int i, boolean z, Collection<Album> collection) {
        onOperationEnd();
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$V
    public void onChangeAlbumBackupStatusSuccess(long[] jArr, boolean z, Collection<Album> collection) {
        onOperationEnd();
        recordAlbumOperation(z ? "auto_upload_enable" : "auto_upload_disable", collection);
    }

    public void doAlbumMoveToOtherAlbums(final Collection<Album> collection) {
        if (UtilsMethodSupportDelegate.getInstance().isEmpty((Collection) collection)) {
            return;
        }
        ConfirmDialog.showConfirmDialog(getActivity(), (int) R.string.operation_move_to_other_albums, (int) R.string.move_to_other_albums_tip, 17039360, 17039370, new ConfirmDialog.ConfirmDialogInterface() { // from class: com.miui.gallery.ui.album.common.base.BaseAlbumPageFragment.5
            @Override // com.miui.gallery.ui.ConfirmDialog.ConfirmDialogInterface
            public void onCancel(DialogFragment dialogFragment) {
            }

            {
                BaseAlbumPageFragment.this = this;
            }

            @Override // com.miui.gallery.ui.ConfirmDialog.ConfirmDialogInterface
            public void onConfirm(DialogFragment dialogFragment) {
                TimeMonitor.createNewTimeMonitor("403.7.0.1.13791");
                ((BaseAlbumPagePresenter) BaseAlbumPageFragment.this.getPresenter()).doChangeAlbumsShowInOtherAlbum(true, collection);
                TrackController.trackClick("403.7.4.1.10346", "403.7.4.1.10542", collection.size());
            }
        });
    }

    public void doAlbumRemoveFromOtherAlbums(final Collection<Album> collection) {
        if (UtilsMethodSupportDelegate.getInstance().isEmpty((Collection) collection)) {
            return;
        }
        DialogUtil.showInfoDialog(getActivity(), (int) R.string.remove_from_other_albums_tip, (int) R.string.operation_remove_from_other_albums, 17039370, 17039360, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.album.common.base.BaseAlbumPageFragment$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                BaseAlbumPageFragment.$r8$lambda$AC73YDe1u11DARVyfYaPUKX0jeU(BaseAlbumPageFragment.this, collection, dialogInterface, i);
            }
        }, (DialogInterface.OnClickListener) null);
    }

    public /* synthetic */ void lambda$doAlbumRemoveFromOtherAlbums$0(Collection collection, DialogInterface dialogInterface, int i) {
        TimeMonitor.createNewTimeMonitor("403.40.0.1.13794");
        ((BaseAlbumPagePresenter) getPresenter()).doChangeAlbumsShowInOtherAlbum(false, (Collection<Album>) collection);
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$V
    public void onRemoveAlbumsFromOtherAlbumsSuccess(long[] jArr, Collection<Album> collection) {
        ((BaseAlbumPagePresenter) getPresenter()).removeData(jArr);
        TimeMonitor.trackTimeMonitor("403.40.0.1.13794", collection.size(), getDataSize());
        recordAlbumOperation("remove_from_other_albums", collection);
        ((BaseAlbumPagePresenter) getPresenter()).trackAlbumOperation("403.40.2.1.11127", "403.40.2.1.11129", collection.size(), true);
        onOperationEnd();
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$V
    public void onRemoveAlbumsFromOtherAlbumsFailed(int i, Collection<Album> collection) {
        TimeMonitor.cancelTimeMonitor("403.40.0.1.13794");
        ((BaseAlbumPagePresenter) getPresenter()).trackAlbumOperation("403.40.2.1.11127", "403.40.2.1.11129", collection.size(), false);
        onOperationEnd();
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$V
    public void onMoveAlbumToOtherAlbumsFailed(int i, Collection<Album> collection) {
        onOperationEnd();
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$V
    public void onMoveAlbumToOtherAlbumsSuccess(long[] jArr, Collection<Album> collection) {
        ((BaseAlbumPagePresenter) getPresenter()).removeData(jArr);
        TimeMonitor.trackTimeMonitor("403.7.0.1.13791", collection.size(), getDataSize());
        recordAlbumOperation("move_to_other_albums", collection);
        onOperationEnd();
    }

    public void doAlbumMoveToRubbishAlbums(final Collection<Album> collection) {
        if (UtilsMethodSupportDelegate.getInstance().isEmpty((Collection) collection)) {
            return;
        }
        new TimerDialog.Builder(getActivity()).setTitle(R.string.operation_move_to_rubbish_albums).setMessage(R.string.move_to_rubbish_albums_tip).setCheckBox(R.string.add_nomedia_with_move_to_rubbish, false, 5000L).setPositiveButton(17039370, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.album.common.base.BaseAlbumPageFragment.6
            {
                BaseAlbumPageFragment.this = this;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (((TimerDialog) dialogInterface).isChecked() ? ((BaseAlbumPagePresenter) BaseAlbumPageFragment.this.getPresenter()).doAddNoMediaForAlbums(collection) : true) {
                    ((BaseAlbumPagePresenter) BaseAlbumPageFragment.this.getPresenter()).doChangeAlbumsMoveToRubbishAlbums(true, collection);
                    TimeMonitor.createNewTimeMonitor("403.40.0.1.14002");
                }
            }
        }).setNegativeButton(17039360, null).build().show();
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$V
    public void onAlbumMoveToRubbishAlbumsSuccess(long[] jArr, Collection<Album> collection) {
        ((BaseAlbumPagePresenter) getPresenter()).removeData(jArr);
        TimeMonitor.trackTimeMonitor("403.40.0.1.14002", collection.size(), getDataSize());
        recordAlbumOperation("move_to_rubbish_albums", collection);
        ((BaseAlbumPagePresenter) getPresenter()).trackAlbumOperation("403.40.2.1.11128", "403.40.2.1.11129", collection.size(), true);
        onOperationEnd();
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$V
    public void onAlbumMoveToRubbishAlbumsFailed(int i, boolean z, Collection<Album> collection) {
        if (i == 1000) {
            ToastUtils.makeText(getActivity(), getActivity().getString(R.string.album_share_cant_move_to_rubbish_tip));
        }
        TimeMonitor.cancelTimeMonitor("403.40.0.1.14002");
        ((BaseAlbumPagePresenter) getPresenter()).trackAlbumOperation("403.40.2.1.11128", "403.40.2.1.11129", collection.size(), false);
        onOperationEnd();
    }

    public void doHideAlbums(final Collection<Album> collection) {
        if (UtilsMethodSupportDelegate.getInstance().isEmpty((Collection) collection)) {
            return;
        }
        ConfirmDialog.showConfirmDialog(getActivity().getSupportFragmentManager(), getActivity().getResources().getString(R.string.album_hidden_page_title), getActivity().getResources().getString(R.string.confirm_hide_selected_albums), getActivity().getResources().getString(17039360), getActivity().getResources().getString(R.string.ok), new ConfirmDialog.ConfirmDialogInterface() { // from class: com.miui.gallery.ui.album.common.base.BaseAlbumPageFragment.7
            @Override // com.miui.gallery.ui.ConfirmDialog.ConfirmDialogInterface
            public void onCancel(DialogFragment dialogFragment) {
            }

            {
                BaseAlbumPageFragment.this = this;
            }

            @Override // com.miui.gallery.ui.ConfirmDialog.ConfirmDialogInterface
            public void onConfirm(DialogFragment dialogFragment) {
                TimeMonitor.createNewTimeMonitor("403.7.0.1.13789");
                ((BaseAlbumPagePresenter) BaseAlbumPageFragment.this.getPresenter()).doChangeAlbumHiddenStatus(true, collection);
                TrackController.trackClick("403.7.4.1.10343", "403.7.4.1.10542", collection.size());
            }
        });
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$V
    public void onChangeAlbumHideStatusSuccess(long[] jArr, boolean z, Collection<Album> collection) {
        if (z) {
            ((BaseAlbumPagePresenter) getPresenter()).removeData(jArr);
            TimeMonitor.trackTimeMonitor("403.7.0.1.13789", collection.size(), getDataSize());
        }
        recordAlbumOperation(z ? "hide_album" : "unhide_album", collection);
        onOperationEnd();
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$V
    public void onChangeAlbumHideStatusFailed(int i, boolean z, Collection<Album> collection) {
        TimeMonitor.cancelTimeMonitor("403.7.0.1.13789");
        onOperationEnd();
    }

    public void doDeleteAlbums(final Collection<Album> collection) {
        boolean z;
        String string;
        if (UtilsMethodSupportDelegate.getInstance().isEmpty((Collection) collection)) {
            return;
        }
        int operationAlbumsPhotoCount = ((BaseAlbumPagePresenter) getPresenter()).getOperationAlbumsPhotoCount(collection);
        int size = collection.size();
        Iterator<Album> it = collection.iterator();
        while (true) {
            if (!it.hasNext()) {
                z = false;
                break;
            } else if (it.next().isAutoUploadedAlbum()) {
                z = true;
                break;
            }
        }
        if (sIsGalleryCloudSyncable == null) {
            updateGalleryCloudSyncableState();
        }
        boolean existXiaomiAccount = SyncUtil.existXiaomiAccount(getActivity());
        final boolean z2 = existXiaomiAccount && z && GalleryPreferences.LocalMode.isOnlyShowLocalPhoto() && sIsGalleryCloudSyncable.booleanValue();
        String quantityString = operationAlbumsPhotoCount > 0 ? getResources().getQuantityString(R.plurals.album_item_msg_format, operationAlbumsPhotoCount, Integer.valueOf(operationAlbumsPhotoCount)) : "";
        if (existXiaomiAccount && !z2) {
            string = getActivity().getResources().getString(R.string.delete_album_msg_format_cloud, getResources().getQuantityString(R.plurals.album_item_msg_format_new, size, Integer.valueOf(size)), quantityString);
        } else {
            string = getActivity().getResources().getString(R.string.delete_album_msg_format_local, getResources().getQuantityString(R.plurals.album_item_msg_format_new, size, Integer.valueOf(size)), quantityString);
        }
        showDeleteDialog(string, z2, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.album.common.base.BaseAlbumPageFragment.8
            {
                BaseAlbumPageFragment.this = this;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (i == -1) {
                    TrackController.trackClick("403.45.0.1.11238", "403.7.4.1.10344");
                    TimeMonitor.createNewTimeMonitor("403.7.0.1.13790");
                    ((BaseAlbumPagePresenter) BaseAlbumPageFragment.this.getPresenter()).doDeleteAlbums((!z2 || ((AlertDialog) dialogInterface).isChecked()) ? 0 : 1, collection);
                }
                if (i == -2) {
                    TrackController.trackClick("403.45.0.1.11239", "403.7.4.1.10344");
                    BaseAlbumPageFragment.this.trackDeleteAlbums(-2, collection, false);
                }
            }
        });
    }

    public final void showDeleteDialog(String str, boolean z, DialogInterface.OnClickListener onClickListener) {
        AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder();
        if (z) {
            builder.setCheckBox(false, getString(R.string.delete_from_cloud));
        }
        builder.setTitle(getActivity().getResources().getString(R.string.delete)).setMessage(str).setPositiveButton(getActivity().getResources().getString(17039370), onClickListener).setNegativeButton(getActivity().getResources().getString(17039360), onClickListener).create().showAllowingStateLoss(getActivity().getSupportFragmentManager(), "DeleteAlbumDialog");
        TrackController.trackExpose("403.45.0.1.11237", "403.7.4.1.10344");
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$V
    public void onDeleteAlbumsSuccess(long[] jArr, int i, Collection<Album> collection) {
        ((BaseAlbumPagePresenter) getPresenter()).removeData(jArr);
        if (getActivity() == null) {
            TimeMonitor.cancelTimeMonitor("403.7.0.1.13790");
            onOperationEnd();
            return;
        }
        ToastUtils.makeText(getActivity(), getActivity().getString(R.string.delete_album_success));
        SoundUtils.playSoundForOperation(getActivity(), 0);
        TimeMonitor.trackTimeMonitor("403.7.0.1.13790", collection.size(), getDataSize());
        recordAlbumOperation("delete_album", collection);
        onOperationEnd();
        trackDeleteAlbums(-1, collection, true);
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$V
    public void onDeleteAlbumsFailed(int i, int i2, Collection<Album> collection) {
        onOperationEnd();
        ToastUtils.makeText(getActivity(), getActivity().getString(R.string.delete_album_failed));
        TimeMonitor.cancelTimeMonitor("403.7.0.1.13790");
        trackDeleteAlbums(-1, collection, false);
    }

    public void doRenameAlbum(Album album) {
        if (Objects.isNull(album)) {
            return;
        }
        showRenameTipDialog(album);
    }

    public void showRenameTipDialog(final Album album) {
        AlbumRenameDialogFragment.newInstance(album.getAlbumId(), album.getAlbumName(), "BaseAlbumPageFragment", new BaseAlbumOperationDialogFragment.OnAlbumOperationListener() { // from class: com.miui.gallery.ui.album.common.base.BaseAlbumPageFragment$$ExternalSyntheticLambda1
            @Override // com.miui.gallery.ui.BaseAlbumOperationDialogFragment.OnAlbumOperationListener
            public final void onOperationDone(long j, String str, Bundle bundle) {
                BaseAlbumPageFragment.this.lambda$showRenameTipDialog$1(album, j, str, bundle);
            }
        }).showAllowingStateLoss(getFragmentManager(), "AlbumRenameDialogFragment");
    }

    public /* synthetic */ void lambda$showRenameTipDialog$1(Album album, long j, String str, Bundle bundle) {
        if (j > 0 && isAdded()) {
            album.setAlbumName(str);
            onRenameAlbumSuccess(album);
            return;
        }
        onRenameAlbumFailed(0, album);
    }

    public void onRenameAlbumFailed(int i, Album album) {
        onOperationEnd();
    }

    public void onRenameAlbumSuccess(Album album) {
        ((BaseAlbumPagePresenter) getPresenter()).updateAlbumName(album.getAlbumId(), album.getAlbumName());
        recordAlbumOperation("rename_album", album);
        onOperationEnd();
    }

    public void startReplaceAlbumCover(Collection<Album> collection) {
        if (collection == null) {
            return;
        }
        this.mOperationAlbums = collection;
        ReplaceAlbumCoverUtils.startReplaceAlbumCoverProcess(collection, this, ((BaseAlbumPagePresenter) getPresenter()).getReplaceAlbumCoverCallBack(collection), 1013);
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$V
    public void onReplaceAlbumCoverIsSuccess(Collection<Album> collection, List<Pair<Album, DoReplaceAlbumCoverMethod.DoReplaceAlbumCoverResult>> list) {
        for (Pair<Album, DoReplaceAlbumCoverMethod.DoReplaceAlbumCoverResult> pair : list) {
            ((BaseAlbumPagePresenter) getPresenter()).updateAlbumCover((Album) pair.first, (DoReplaceAlbumCoverMethod.DoReplaceAlbumCoverResult) pair.second);
        }
        TimeMonitor.trackTimeMonitor("403.7.0.1.13792", collection.size());
        recordAlbumOperation("replace_album_cover", collection);
        DefaultLogger.d("BaseAlbumPageFragment", "封面替换成功");
        this.mOperationAlbums = null;
        onOperationEnd();
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$V
    public void onReplaceAlbumCoverIsFailed(Collection<Album> collection, Long l) {
        TimeMonitor.cancelTimeMonitor("403.7.0.1.13792");
        DefaultLogger.d("BaseAlbumPageFragment", "封面替换失败:[%s]", TextUtils.join("\n\t", Thread.currentThread().getStackTrace()));
        onOperationEnd();
        this.mOperationAlbums = null;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        ReplaceAlbumCoverUtils.handleActivityResultByReplaceAlbumCover(this, i, i2, intent, this.mOperationAlbums, ((BaseAlbumPagePresenter) getPresenter()).getReplaceAlbumCoverCallBack(this.mOperationAlbums));
    }

    public void showSortImmersionMenu(View view) {
        if (view == null) {
            return;
        }
        new AlbumTabSortImmersionMenuHelper(getContext(), new AlbumTabSortImmersionMenuHelper.OnItemClickListener() { // from class: com.miui.gallery.ui.album.common.base.BaseAlbumPageFragment.9
            {
                BaseAlbumPageFragment.this = this;
            }

            @Override // com.miui.gallery.ui.album.main.utils.AlbumTabSortImmersionMenuHelper.OnItemClickListener
            public void onItemClick(int i) {
                DefaultLogger.v("BaseAlbumPageFragment", "改变排序模式：%s", AlbumSortHelper.getCurrentSortBasis());
                HashMap hashMap = new HashMap(1, 1.0f);
                hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, AlbumSortHelper.getCurrentSortBasis());
                SamplingStatHelper.recordCountEvent("album", "change_album_sort_type", hashMap);
                AlbumSortHelper.trackSortChange();
                ((BaseAlbumPagePresenter) BaseAlbumPageFragment.this.getPresenter()).doChangeAlbumSortType(BaseAlbumPageFragment.this.mAdapter.getDatas(), i);
            }
        }).showImmersionMenu(view);
    }

    @Override // com.miui.gallery.ui.album.common.base.BaseAlbumPageContract$V
    public void onChangeAlbumSortTypeSuccess(DoChangeAlbumSortTypeCase.SortResult sortResult) {
        onOperationEnd();
    }

    public final void recordAlbumOperation(String str, Collection<Album> collection) {
        HashMap hashMap;
        if (UtilsMethodSupportDelegate.getInstance().isEmpty((Collection) collection)) {
            hashMap = new HashMap(1, 1.0f);
        } else {
            HashMap hashMap2 = new HashMap(collection.size(), 1.0f);
            hashMap2.put(Action.FILE_ATTRIBUTE, (String) collection.stream().map(new Function<Album, String>() { // from class: com.miui.gallery.ui.album.common.base.BaseAlbumPageFragment.10
                {
                    BaseAlbumPageFragment.this = this;
                }

                @Override // java.util.function.Function
                public String apply(Album album) {
                    return album.getLocalPath();
                }
            }).collect(Collectors.joining(",\n")));
            hashMap = hashMap2;
        }
        SamplingStatHelper.recordCountEvent("album", str, hashMap);
    }

    public final void recordAlbumOperation(String str, Album album) {
        if (album != null) {
            recordAlbumOperation(str, Collections.singleton(album));
        }
    }

    public final void updateGalleryCloudSyncableState() {
        boolean isGalleryCloudSyncable = SyncUtil.isGalleryCloudSyncable(getContext());
        Boolean bool = sIsGalleryCloudSyncable;
        if (bool == null || bool.booleanValue() != isGalleryCloudSyncable) {
            sIsGalleryCloudSyncable = Boolean.valueOf(isGalleryCloudSyncable);
        }
    }

    public final void trackDeleteAlbums(int i, Collection<Album> collection, boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put("tip", "403.7.4.1.10344");
        hashMap.put(MiStat.Param.COUNT, String.valueOf(collection.size()));
        hashMap.put("ref_tip", "403.7.4.1.10542");
        int i2 = 0;
        int i3 = 0;
        for (Album album : collection) {
            if (album.isUserCreateAlbum()) {
                i2++;
            } else {
                i3++;
            }
        }
        hashMap.put(nexExportFormat.TAG_FORMAT_PATH, "owner:" + i2 + h.g + "others:" + i3);
        if (i == -1) {
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "sure");
            hashMap.put("success", Boolean.valueOf(z));
        } else if (i == -2) {
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, "cancel");
        }
        TrackController.trackClick(hashMap);
    }
}
