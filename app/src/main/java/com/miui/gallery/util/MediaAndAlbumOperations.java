package com.miui.gallery.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.fragment.app.FragmentActivity;
import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.R;
import com.miui.gallery.activity.AllPhotosActivity;
import com.miui.gallery.activity.HomePageActivity;
import com.miui.gallery.activity.facebaby.BabyAlbumDetailActivity;
import com.miui.gallery.adapter.CheckableAdapter;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.card.scenario.DateUtils;
import com.miui.gallery.picker.helper.Picker;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.CloudUtils;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.MediaOperationService;
import com.miui.gallery.provider.cloudmanager.method.album.DoReplaceAlbumCoverMethod;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.AddRemoveFavoritesTask;
import com.miui.gallery.ui.AddRemoveSecretDialogFragment;
import com.miui.gallery.ui.AlertDialogFragment;
import com.miui.gallery.ui.DeleteMediaDialogFragment;
import com.miui.gallery.ui.DeletionTask;
import com.miui.gallery.ui.ProduceCreationDialog;
import com.miui.gallery.ui.RichTipDialogFragment;
import com.miui.gallery.ui.addtoalbum.AddToAlbumPageActivity;
import com.miui.gallery.ui.album.common.AlbumConstants;
import com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumManualHideResult;
import com.miui.gallery.util.market.MediaEditorInstaller;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class MediaAndAlbumOperations {

    /* loaded from: classes2.dex */
    public interface OnAddAlbumListener {
        void onComplete(long[] jArr, boolean z);
    }

    /* loaded from: classes2.dex */
    public interface OnCompleteListener {
        void onComplete(long[] jArr);
    }

    /* loaded from: classes2.dex */
    public interface OnPicToPdfClickListener {
        void onPicToPdfClick();
    }

    public static void addToSecretAlbum(final FragmentActivity fragmentActivity, final OnCompleteListener onCompleteListener, final boolean z, final long... jArr) {
        Resources resources = fragmentActivity.getResources();
        new AlertDialogFragment.Builder().setTitle(resources.getString(R.string.add_secret_ensure_title)).setMessage(resources.getString(R.string.add_secret_ensure_message_new)).setNegativeButton(resources.getString(R.string.cancel), null).setPositiveButton(resources.getString(R.string.ok), new DialogInterface.OnClickListener() { // from class: com.miui.gallery.util.MediaAndAlbumOperations.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                AddRemoveSecretDialogFragment.add(FragmentActivity.this, onCompleteListener, z, jArr);
            }
        }).create().showAllowingStateLoss(fragmentActivity.getSupportFragmentManager(), "ensureSecret");
    }

    public static void doProduceCreation(FragmentActivity fragmentActivity, OnCompleteListener onCompleteListener, List<CheckableAdapter.CheckedItem> list) {
        if (!MediaEditorInstaller.getInstance().installIfNotExist(fragmentActivity, null, false)) {
            return;
        }
        doProduceCreation(fragmentActivity, onCompleteListener, list, false, false);
    }

    public static void doProduceCreation(final FragmentActivity fragmentActivity, final OnCompleteListener onCompleteListener, final List<CheckableAdapter.CheckedItem> list, boolean z, boolean z2) {
        ProduceCreationDialog produceCreationDialog = new ProduceCreationDialog();
        produceCreationDialog.setOnOperationSelectedListener(new ProduceCreationDialog.OnOperationSelectedListener() { // from class: com.miui.gallery.util.MediaAndAlbumOperations.2
            @Override // com.miui.gallery.ui.ProduceCreationDialog.OnOperationSelectedListener
            public boolean onOperationSelected(int i) {
                Picker.ImageType imageType = Picker.ImageType.THUMBNAIL;
                MediaAndAlbumOperations.trackProduceCreation(FragmentActivity.this, i, list.size());
                switch (i) {
                    case 3:
                        imageType = Picker.ImageType.ORIGIN;
                        break;
                    case 4:
                        SamplingStatHelper.recordCountEvent("creation", "creation_macaron");
                        break;
                    case 5:
                        SamplingStatHelper.recordCountEvent("creation", "creation_click_collage");
                        break;
                    case 6:
                        SamplingStatHelper.recordCountEvent("creation", "creation_click_photomovie");
                        break;
                    case 7:
                        imageType = Picker.ImageType.ORIGIN;
                        SamplingStatHelper.recordCountEvent("creation", "creation_click_vlog");
                        break;
                    case 8:
                        imageType = Picker.ImageType.ORIGIN_OR_DOWNLOAD_INFO;
                        SamplingStatHelper.recordCountEvent("creation", "creation_click_print");
                        break;
                }
                ArrayList arrayList = new ArrayList(list);
                if (ProduceCreationDialog.checkCreationCondition(FragmentActivity.this, i, arrayList)) {
                    IntentUtil.doCreation(FragmentActivity.this, i, arrayList, imageType);
                    OnCompleteListener onCompleteListener2 = onCompleteListener;
                    if (onCompleteListener2 == null) {
                        return true;
                    }
                    onCompleteListener2.onComplete(null);
                    return true;
                }
                return false;
            }
        });
        produceCreationDialog.showAllowingStateLoss(fragmentActivity.getSupportFragmentManager(), "ProduceCreationDialog");
    }

    public static void trackProduceCreation(FragmentActivity fragmentActivity, int i, int i2) {
        boolean z = true;
        boolean z2 = fragmentActivity.getClass() == HomePageActivity.class;
        boolean z3 = fragmentActivity.getClass() == AllPhotosActivity.class;
        if (fragmentActivity.getClass() != BabyAlbumDetailActivity.class) {
            z = false;
        }
        String str = z2 ? "403.1.8.1.9891" : z3 ? "403.44.3.1.11229" : z ? "403.42.3.1.11305" : "403.15.3.1.11200";
        String str2 = i != 5 ? i != 6 ? i != 7 ? "" : z2 ? "403.1.8.1.9896" : z ? "403.42.3.1.11425" : z3 ? "403.44.3.1.11227" : "403.15.3.1.11198" : z2 ? "403.1.8.1.9895" : z ? "403.42.3.1.11424" : z3 ? "403.44.3.1.11226" : "403.15.3.1.11197" : z2 ? "403.1.8.1.9894" : z ? "403.42.3.1.11426" : z3 ? "403.44.3.1.11225" : "403.15.3.1.11196";
        if (!TextUtils.isEmpty(str2) || !TextUtils.isEmpty(str)) {
            TrackController.trackClick(str2, str, i2);
        }
    }

    public static void addToSecretAlbum(final FragmentActivity fragmentActivity, final OnCompleteListener onCompleteListener, final ArrayList<Uri> arrayList, final boolean z) {
        Resources resources = fragmentActivity.getResources();
        new AlertDialogFragment.Builder().setTitle(resources.getString(R.string.add_secret_ensure_title)).setMessage(resources.getString(R.string.add_secret_ensure_message_new)).setNegativeButton(resources.getString(R.string.cancel), null).setPositiveButton(resources.getString(R.string.ok), new DialogInterface.OnClickListener() { // from class: com.miui.gallery.util.MediaAndAlbumOperations.3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                AddRemoveSecretDialogFragment.add(FragmentActivity.this, onCompleteListener, z, arrayList);
            }
        }).create().showAllowingStateLoss(fragmentActivity.getSupportFragmentManager(), "ensureSecret");
    }

    public static void removeFromSecretAlbum(FragmentActivity fragmentActivity, final OnCompleteListener onCompleteListener, long... jArr) {
        AddToAlbumPageActivity.start(fragmentActivity, new AddToAlbumPageActivity.AddToAlbumPageResult() { // from class: com.miui.gallery.util.MediaAndAlbumOperations.4
            @Override // com.miui.gallery.ui.addtoalbum.AddToAlbumPageActivity.AddToAlbumPageResult
            public void onComplete(Long l, long[] jArr2, Boolean bool) {
                OnCompleteListener.this.onComplete(jArr2);
            }
        }, AlbumConstants.AddToAlbumPage.secretScene(jArr), false);
    }

    public static void showTutorial(FragmentActivity fragmentActivity) {
        long currentTimeMillis = DateUtils.getCurrentTimeMillis();
        if (isLastEnterIn7Days(currentTimeMillis)) {
            return;
        }
        if (!isLastShowIn7Days(currentTimeMillis)) {
            GalleryPreferences.Secret.setSecretTutorialRestTime(5);
        }
        int secretTutorialRestTime = GalleryPreferences.Secret.getSecretTutorialRestTime() - 1;
        GalleryPreferences.Secret.setSecretTutorialRestTime(secretTutorialRestTime);
        if (secretTutorialRestTime < 0) {
            return;
        }
        Resources resources = fragmentActivity.getResources();
        RichTipDialogFragment.newInstance(R.raw.secret_album, resources.getString(R.string.alert_secret_album_title_new), resources.getString(R.string.alert_secret_album_message_new), null, resources.getString(R.string.alert_secret_album_enter), null).showAllowingStateLoss(fragmentActivity.getSupportFragmentManager(), "SecretTip");
    }

    public static boolean isLastEnterIn7Days(long j) {
        return j - GalleryPreferences.Secret.getLastEnterPrivateAlbumTime() <= CoreConstants.MILLIS_IN_ONE_WEEK;
    }

    public static boolean isLastShowIn7Days(long j) {
        long lastModifiedTimeAddToSecret = GalleryPreferences.Secret.getLastModifiedTimeAddToSecret();
        GalleryPreferences.Secret.setLastModifiedTimeAddToSecret(j);
        return j - lastModifiedTimeAddToSecret <= CoreConstants.MILLIS_IN_ONE_WEEK;
    }

    public static void addToAlbum(FragmentActivity fragmentActivity, final OnAddAlbumListener onAddAlbumListener, ArrayList<Uri> arrayList, boolean z, boolean z2) {
        if (fragmentActivity == null || fragmentActivity.isFinishing()) {
            return;
        }
        if (arrayList != null && arrayList.size() > 0) {
            DebugUtil.logEventTime("operationTrace", "show_add_album_page", false);
            AddToAlbumPageActivity.start(fragmentActivity, true, z, false, false, -1, z2, null, arrayList, onAddAlbumListener == null ? null : new AddToAlbumPageActivity.AddToAlbumPageResult() { // from class: com.miui.gallery.util.MediaAndAlbumOperations.5
                @Override // com.miui.gallery.ui.addtoalbum.AddToAlbumPageActivity.AddToAlbumPageResult
                public void onComplete(Long l, long[] jArr, Boolean bool) {
                    OnAddAlbumListener.this.onComplete(jArr, bool.booleanValue());
                }
            }, false);
        } else if (onAddAlbumListener == null) {
        } else {
            onAddAlbumListener.onComplete(new long[0], false);
        }
    }

    public static void addToAlbum(FragmentActivity fragmentActivity, OnAddAlbumListener onAddAlbumListener, boolean z, boolean z2, boolean z3, boolean z4, long... jArr) {
        addToAlbum(fragmentActivity, onAddAlbumListener, null, z, z2, z3, z4, jArr);
    }

    public static void addToAlbum(FragmentActivity fragmentActivity, final OnAddAlbumListener onAddAlbumListener, final OnPicToPdfClickListener onPicToPdfClickListener, boolean z, boolean z2, boolean z3, boolean z4, long... jArr) {
        if (fragmentActivity == null || fragmentActivity.isFinishing()) {
            return;
        }
        if (jArr != null && jArr.length > 0) {
            DebugUtil.logEventTime("operationTrace", "show_add_album_page", false);
            AddToAlbumPageActivity.start(fragmentActivity, true, z3, false, z, -1, z4, jArr, null, onAddAlbumListener == null ? null : new AddToAlbumPageActivity.AddToAlbumPageResult() { // from class: com.miui.gallery.util.MediaAndAlbumOperations.6
                @Override // com.miui.gallery.ui.addtoalbum.AddToAlbumPageActivity.AddToAlbumPageResult
                public void onComplete(Long l, long[] jArr2, Boolean bool) {
                    OnAddAlbumListener.this.onComplete(jArr2, bool.booleanValue());
                }

                @Override // com.miui.gallery.ui.addtoalbum.AddToAlbumPageActivity.AddToAlbumPageResult
                public void onPdfClick() {
                    onPicToPdfClickListener.onPicToPdfClick();
                }
            }, onPicToPdfClickListener != null);
        } else if (onAddAlbumListener == null) {
        } else {
            onAddAlbumListener.onComplete(new long[0], false);
        }
    }

    public static void delete(FragmentActivity fragmentActivity, String str, DeletionTask.OnDeletionCompleteListener onDeletionCompleteListener, DialogInterface.OnDismissListener onDismissListener, long j, String str2, int i, int i2, long... jArr) {
        delete(fragmentActivity, str, onDeletionCompleteListener, onDismissListener, j, str2, i, false, i2, jArr);
    }

    public static void delete(FragmentActivity fragmentActivity, String str, DeletionTask.OnDeletionCompleteListener onDeletionCompleteListener, DialogInterface.OnDismissListener onDismissListener, long j, String str2, int i, long... jArr) {
        delete(fragmentActivity, str, onDeletionCompleteListener, onDismissListener, j, str2, i, false, 0, jArr);
    }

    public static void delete(FragmentActivity fragmentActivity, String str, DeletionTask.OnDeletionCompleteListener onDeletionCompleteListener, DialogInterface.OnDismissListener onDismissListener, long j, String str2, int i, boolean z, int i2, long... jArr) {
        if (fragmentActivity == null || fragmentActivity.isFinishing()) {
            return;
        }
        if (jArr == null || jArr.length <= 0) {
            if (onDeletionCompleteListener == null) {
                return;
            }
            onDeletionCompleteListener.onDeleted(0, new long[0]);
            return;
        }
        DeleteMediaDialogFragment newInstance = DeleteMediaDialogFragment.newInstance(new DeletionTask.Param(jArr, j, str2, i, z, i2));
        newInstance.setOnDeletionCompleteListener(onDeletionCompleteListener);
        newInstance.setOnDismissListener(onDismissListener);
        newInstance.showAllowingStateLoss(fragmentActivity.getSupportFragmentManager(), str);
    }

    public static void deleteInService(Context context, int i, int i2, String... strArr) {
        Intent intent = new Intent(context, MediaOperationService.class);
        intent.putExtra(MediaOperationService.EXTRA_METHOD, "delete");
        Bundle bundle = new Bundle();
        bundle.putInt("delete_by", 1);
        bundle.putStringArray("extra_paths", strArr);
        bundle.putInt("extra_delete_options", i);
        bundle.putInt("extra_delete_reason", i2);
        intent.putExtra(MediaOperationService.EXTRA_BUNDLE, bundle);
        BackgroundServiceHelper.startForegroundServiceIfNeed(context, intent);
    }

    public static void doChangeShowInPhotosTab(Context context, long j, boolean z) {
        doChangeShowInPhotosTab(context, j, z, true);
    }

    public static void doChangeShowInPhotosTab(Context context, long j, boolean z, boolean z2) {
        doChangeShowInPhotosTab(context, new long[]{j}, z, z2);
    }

    public static void doChangeShowInPhotosTab(Context context, long[] jArr, boolean z, boolean z2) {
        CloudUtils.updateAlbumAttributes(context, GalleryContract.Cloud.CLOUD_URI, jArr, 4L, z, true, z2);
    }

    public static boolean doChangeHiddenStatus(Context context, long j, boolean z, boolean z2) {
        return doChangeHiddenStatus(context, new long[]{j}, z, z2);
    }

    public static boolean doChangeHiddenStatus(Context context, long[] jArr, boolean z, boolean z2) {
        return CloudUtils.updateAlbumAttributes(context, GalleryContract.Cloud.CLOUD_URI, jArr, 16L, z, true, z2);
    }

    public static void addToFavoritesByPath(FragmentActivity fragmentActivity, OnCompleteListener onCompleteListener, String... strArr) {
        if (fragmentActivity == null || fragmentActivity.isFinishing()) {
            return;
        }
        addRemoveFavorite(new AddRemoveFavoritesTask.Param(1, 1, strArr), onCompleteListener);
    }

    public static void removeFromFavoritesByPath(FragmentActivity fragmentActivity, OnCompleteListener onCompleteListener, String... strArr) {
        if (fragmentActivity == null || fragmentActivity.isFinishing()) {
            return;
        }
        addRemoveFavorite(new AddRemoveFavoritesTask.Param(2, 1, strArr), onCompleteListener);
    }

    public static void addToFavoritesById(FragmentActivity fragmentActivity, OnCompleteListener onCompleteListener, long... jArr) {
        if (fragmentActivity == null || fragmentActivity.isFinishing()) {
            return;
        }
        addRemoveFavorite(new AddRemoveFavoritesTask.Param(1, 2, jArr), onCompleteListener);
    }

    public static void addToFavoritesByUri(FragmentActivity fragmentActivity, OnCompleteListener onCompleteListener, ArrayList<Uri> arrayList) {
        if (fragmentActivity == null || fragmentActivity.isFinishing()) {
            return;
        }
        addRemoveFavorite(new AddRemoveFavoritesTask.Param(1, 3, arrayList), onCompleteListener);
    }

    public static void removeFromFavoritesById(FragmentActivity fragmentActivity, OnCompleteListener onCompleteListener, long... jArr) {
        if (fragmentActivity == null || fragmentActivity.isFinishing()) {
            return;
        }
        addRemoveFavorite(new AddRemoveFavoritesTask.Param(2, 2, jArr), onCompleteListener);
    }

    public static void addRemoveFavorite(AddRemoveFavoritesTask.Param param, OnCompleteListener onCompleteListener) {
        AddRemoveFavoritesTask addRemoveFavoritesTask = new AddRemoveFavoritesTask();
        addRemoveFavoritesTask.setOperationCompleteListener(onCompleteListener);
        addRemoveFavoritesTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, param);
    }

    public static void doChangeAutoUpload(Context context, long j, boolean z) {
        doChangeAutoUpload(context, j, z, true);
    }

    public static boolean doChangeAutoUpload(Context context, long j, boolean z, boolean z2) {
        return doChangeAutoUpload(context, new long[]{j}, z, z2);
    }

    public static boolean doChangeAutoUpload(Context context, long[] jArr, boolean z, boolean z2) {
        return CloudUtils.updateAlbumAttributes(context, GalleryContract.Cloud.CLOUD_URI, jArr, 1L, z, true, z2);
    }

    public static void doChangeShowInOtherAlbums(Context context, long j, boolean z) {
        doChangeShowInOtherAlbums(context, j, z, true);
    }

    public static boolean doChangeShowInOtherAlbums(Context context, long j, boolean z, boolean z2) {
        return doChangeShowInOtherAlbums(context, new long[]{j}, z, z2);
    }

    public static boolean doChangeShowInOtherAlbums(Context context, long[] jArr, boolean z, boolean z2) {
        return CloudUtils.updateAlbumAttributes(context, GalleryContract.Cloud.CLOUD_URI, jArr, 64L, z, true, z2);
    }

    public static void doChangeShowInRubbishAlbums(Context context, long[] jArr, boolean z, boolean z2) {
        CloudUtils.updateAlbumAttributes(context, GalleryContract.Cloud.CLOUD_URI, jArr, 2048L, z, true, z2);
    }

    public static RubbishAlbumManualHideResult doAddNoMediaForRubbishAlbum(List<String> list) {
        RubbishAlbumManualHideResult rubbishAlbumManualHideResult = new RubbishAlbumManualHideResult();
        if (BaseMiscUtil.isValid(list)) {
            for (String str : list) {
                rubbishAlbumManualHideResult.add(str, Boolean.valueOf(NoMediaUtil.tryAddNoMediaForAlbum(str)));
            }
        }
        return rubbishAlbumManualHideResult;
    }

    public static RubbishAlbumManualHideResult doRemoveNoMediaForRubbishAlbum(List<String> list) {
        RubbishAlbumManualHideResult rubbishAlbumManualHideResult = new RubbishAlbumManualHideResult();
        if (BaseMiscUtil.isValid(list)) {
            for (String str : list) {
                rubbishAlbumManualHideResult.add(str, Boolean.valueOf(NoMediaUtil.tryRemoveNoMediaForAlbum(str)));
            }
        }
        return rubbishAlbumManualHideResult;
    }

    public static void doChangeAlbumSortPosition(Context context, long[] jArr, String[] strArr, boolean z) {
        CloudUtils.updateAlbumSortPosition(context, GalleryContract.Cloud.CLOUD_URI, jArr, strArr, z);
    }

    public static ArrayList<DoReplaceAlbumCoverMethod.DoReplaceAlbumCoverResult> doReplaceAlbumCover(Context context, long j, long... jArr) {
        return CloudUtils.replaceAlbumCover(context, GalleryContract.Album.URI, j, false, jArr);
    }
}
