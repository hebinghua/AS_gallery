package com.miui.gallery.share;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.common.collect.Lists;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.share.AlbumShareOperations;
import com.miui.gallery.share.AlbumShareUIManager;
import com.miui.gallery.share.ShareAlbumBaseFragment;
import com.miui.gallery.util.concurrent.ThreadManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import miuix.animation.Folme;
import miuix.animation.base.AnimConfig;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes2.dex */
public abstract class ShareAlbumOwnerBaseFragment extends ShareAlbumBaseFragment {
    public View mCloudScrollView;
    public View mCloudService;
    public Future<?> mFutureCreateItem;
    public Future<?> mFutureSms;
    public CloudObserver mObserver = new CloudObserver(ThreadManager.getMainHandler());

    public static /* synthetic */ void $r8$lambda$E7VKc_8PDOuEvemz1hPfyWdPhXQ(ShareAlbumOwnerBaseFragment shareAlbumOwnerBaseFragment, View view, int i, int i2, int i3, int i4) {
        shareAlbumOwnerBaseFragment.lambda$onViewCreated$1(view, i, i2, i3, i4);
    }

    public static /* synthetic */ void $r8$lambda$QUHXl4KpnkVTT5Lp8gSXp35ER60(ShareAlbumOwnerBaseFragment shareAlbumOwnerBaseFragment) {
        shareAlbumOwnerBaseFragment.lambda$refreshCloudServiceView$2();
    }

    /* renamed from: $r8$lambda$QXkOYN5F-05CplcCgRGld3BDdyI */
    public static /* synthetic */ void m1383$r8$lambda$QXkOYN5F05CplcCgRGld3BDdyI(ShareAlbumOwnerBaseFragment shareAlbumOwnerBaseFragment, CloudUserCacheEntry cloudUserCacheEntry, DialogInterface dialogInterface, int i) {
        shareAlbumOwnerBaseFragment.lambda$showAddSharerDialog$3(cloudUserCacheEntry, dialogInterface, i);
    }

    /* renamed from: $r8$lambda$RI64DEQ-UyPlZDgoqaXm398rut0 */
    public static /* synthetic */ void m1384$r8$lambda$RI64DEQUyPlZDgoqaXm398rut0(ShareAlbumOwnerBaseFragment shareAlbumOwnerBaseFragment, Void r1, String str, int i, boolean z) {
        shareAlbumOwnerBaseFragment.lambda$tryToCreateCloudItem$8(r1, str, i, z);
    }

    /* renamed from: $r8$lambda$S9mCUPk-yw6t6oFGOPCPm3XFbeo */
    public static /* synthetic */ void m1385$r8$lambda$S9mCUPkyw6t6oFGOPCPm3XFbeo(ShareAlbumOwnerBaseFragment shareAlbumOwnerBaseFragment) {
        shareAlbumOwnerBaseFragment.lambda$tryToCreateCloudItem$7();
    }

    /* renamed from: $r8$lambda$THDFKbF8BE9UCgDumH-OO9nDdG0 */
    public static /* synthetic */ void m1386$r8$lambda$THDFKbF8BE9UCgDumHOO9nDdG0(ShareAlbumOwnerBaseFragment shareAlbumOwnerBaseFragment, CloudUserCacheEntry cloudUserCacheEntry, DialogInterface dialogInterface, int i) {
        shareAlbumOwnerBaseFragment.lambda$showKickUserDialog$5(cloudUserCacheEntry, dialogInterface, i);
    }

    public static /* synthetic */ void $r8$lambda$eKdoU7_khY8wILV3vBdedMqjbpw(ShareAlbumOwnerBaseFragment shareAlbumOwnerBaseFragment, View view) {
        shareAlbumOwnerBaseFragment.lambda$onViewCreated$0(view);
    }

    public void requestInvitation(CloudUserCacheEntry cloudUserCacheEntry, AlbumShareUIManager.OnCompletionListener<Void, AlbumShareOperations.OutgoingInvitation> onCompletionListener) {
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        view.findViewById(R.id.retry_button).setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.share.ShareAlbumOwnerBaseFragment$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                ShareAlbumOwnerBaseFragment.$r8$lambda$eKdoU7_khY8wILV3vBdedMqjbpw(ShareAlbumOwnerBaseFragment.this, view2);
            }
        });
        this.mCloudService = view.findViewById(R.id.cloud_service_text);
        this.mCloudScrollView = view.findViewById(16908351);
        if (this.mCloudSingleMediaSet.getBabyInfo() == null) {
            this.mCloudScrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() { // from class: com.miui.gallery.share.ShareAlbumOwnerBaseFragment$$ExternalSyntheticLambda3
                @Override // android.view.View.OnScrollChangeListener
                public final void onScrollChange(View view2, int i, int i2, int i3, int i4) {
                    ShareAlbumOwnerBaseFragment.$r8$lambda$E7VKc_8PDOuEvemz1hPfyWdPhXQ(ShareAlbumOwnerBaseFragment.this, view2, i, i2, i3, i4);
                }
            });
            refreshCloudServiceView();
        }
        RecyclerView listView = getListView();
        listView.setPadding(listView.getPaddingLeft(), listView.getPaddingTop(), listView.getPaddingRight(), getResources().getDimensionPixelSize(R.dimen.album_share_bottom_cloud_text));
        tryToCreateCloudItem();
        lambda$tryToCreateCloudItem$7();
        GalleryApp.sGetAndroidContext().getContentResolver().registerContentObserver(GalleryCloudUtils.CLOUD_URI, true, this.mObserver);
    }

    public /* synthetic */ void lambda$onViewCreated$0(View view) {
        tryToCreateCloudItem();
        lambda$tryToCreateCloudItem$7();
    }

    public /* synthetic */ void lambda$onViewCreated$1(View view, int i, int i2, int i3, int i4) {
        refreshCloudServiceView();
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseFragment, com.miui.gallery.app.fragment.MiuiPreferenceFragment, miuix.preference.PreferenceFragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        refreshCloudServiceView();
    }

    public final void refreshCloudServiceView() {
        View view = this.mCloudScrollView;
        if (view != null) {
            view.post(new Runnable() { // from class: com.miui.gallery.share.ShareAlbumOwnerBaseFragment$$ExternalSyntheticLambda7
                @Override // java.lang.Runnable
                public final void run() {
                    ShareAlbumOwnerBaseFragment.$r8$lambda$QUHXl4KpnkVTT5Lp8gSXp35ER60(ShareAlbumOwnerBaseFragment.this);
                }
            });
        }
    }

    public /* synthetic */ void lambda$refreshCloudServiceView$2() {
        View view = this.mCloudScrollView;
        if (view == null || this.mCloudService == null) {
            return;
        }
        if (view.canScrollVertically(1)) {
            Folme.useAt(this.mCloudService).visible().hide(new AnimConfig[0]);
        } else {
            Folme.useAt(this.mCloudService).visible().show(new AnimConfig[0]);
        }
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        GalleryApp.sGetAndroidContext().getContentResolver().unregisterContentObserver(this.mObserver);
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseFragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        Future<?> future = this.mFutureCreateItem;
        if (future != null) {
            future.cancel();
        }
        Future<?> future2 = this.mFutureSms;
        if (future2 != null) {
            future2.cancel();
        }
        super.onDestroy();
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseFragment
    public CloudUserCache getUserCache() {
        return CloudUserCache.getOwnerUserCache();
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseFragment
    public void updateSharers() {
        CloudUtils.isValidAlbumId(this.mAlbumId);
        super.updateSharers();
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseFragment
    public List<CloudUserCacheEntry> getShareUsers() {
        ArrayList newArrayList = Lists.newArrayList();
        newArrayList.addAll(getUserCache().getCloudUserListByAlbumId(this.mAlbumId));
        Collections.sort(newArrayList, new ShareAlbumBaseFragment.UserCacheDescComparator());
        return newArrayList;
    }

    public void showAddSharerDialog(final CloudUserCacheEntry cloudUserCacheEntry) {
        int addSharerDialogItemsLength = PhoneShareAlbumProvider.getInstance().getAddSharerDialogItemsLength();
        if (addSharerDialogItemsLength < 1) {
            return;
        }
        if (addSharerDialogItemsLength == 1) {
            addSharer(0, cloudUserCacheEntry);
        } else {
            PhoneShareAlbumProvider.getInstance().showAddSharerDialog((AppCompatActivity) getActivity(), new DialogInterface.OnClickListener() { // from class: com.miui.gallery.share.ShareAlbumOwnerBaseFragment$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    ShareAlbumOwnerBaseFragment.m1383$r8$lambda$QXkOYN5F05CplcCgRGld3BDdyI(ShareAlbumOwnerBaseFragment.this, cloudUserCacheEntry, dialogInterface, i);
                }
            });
        }
    }

    public /* synthetic */ void lambda$showAddSharerDialog$3(CloudUserCacheEntry cloudUserCacheEntry, DialogInterface dialogInterface, int i) {
        addSharer(i, cloudUserCacheEntry);
        dialogInterface.dismiss();
    }

    public final void addSharer(final int i, CloudUserCacheEntry cloudUserCacheEntry) {
        requestInvitation(cloudUserCacheEntry, new AlbumShareUIManager.OnCompletionListener() { // from class: com.miui.gallery.share.ShareAlbumOwnerBaseFragment$$ExternalSyntheticLambda5
            @Override // com.miui.gallery.share.AlbumShareUIManager.OnCompletionListener
            public final void onCompletion(Object obj, Object obj2, int i2, boolean z) {
                ShareAlbumOwnerBaseFragment.this.lambda$addSharer$4(i, (Void) obj, (AlbumShareOperations.OutgoingInvitation) obj2, i2, z);
            }
        });
    }

    public /* synthetic */ void lambda$addSharer$4(int i, Void r2, AlbumShareOperations.OutgoingInvitation outgoingInvitation, int i2, boolean z) {
        if (!isAdded()) {
            return;
        }
        if (z) {
            UIHelper.toast(getActivity(), getString(R.string.cancel_hint));
        } else if (i2 == 0 && outgoingInvitation != null) {
            PhoneShareAlbumProvider.getInstance().shareOperation((AppCompatActivity) getActivity(), i, outgoingInvitation, this.mAlbumName);
        } else {
            UIHelper.toast(getActivity(), getString(R.string.fail_hint));
        }
    }

    public void showKickUserDialog(final CloudUserCacheEntry cloudUserCacheEntry) {
        if (cloudUserCacheEntry == null) {
            return;
        }
        PhoneShareAlbumProvider.getInstance().showKickUserDialog((AppCompatActivity) getActivity(), new DialogInterface.OnClickListener() { // from class: com.miui.gallery.share.ShareAlbumOwnerBaseFragment$$ExternalSyntheticLambda1
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                ShareAlbumOwnerBaseFragment.m1386$r8$lambda$THDFKbF8BE9UCgDumHOO9nDdG0(ShareAlbumOwnerBaseFragment.this, cloudUserCacheEntry, dialogInterface, i);
            }
        });
    }

    public /* synthetic */ void lambda$showKickUserDialog$5(CloudUserCacheEntry cloudUserCacheEntry, DialogInterface dialogInterface, int i) {
        kickUser(cloudUserCacheEntry.mUserId);
    }

    public final void kickUser(String str) {
        ShareAlbumOwnerBaseFragment$$ExternalSyntheticLambda6 shareAlbumOwnerBaseFragment$$ExternalSyntheticLambda6 = ShareAlbumOwnerBaseFragment$$ExternalSyntheticLambda6.INSTANCE;
        AlbumShareUIManager.BlockMessage create = AlbumShareUIManager.BlockMessage.create(getActivity(), null, getString(R.string.kick_sharer), false);
        ArrayList newArrayList = Lists.newArrayList();
        newArrayList.add(str);
        AlbumShareUIManager.kickSharersAsync(this.mAlbumId, newArrayList, shareAlbumOwnerBaseFragment$$ExternalSyntheticLambda6, create);
    }

    public static /* synthetic */ void lambda$kickUser$6(Void r0, Pair pair, int i, boolean z) {
        if (z) {
            UIHelper.toast((int) R.string.cancel_hint);
        } else if (i == 0 && pair != null && !((List) pair.first).isEmpty() && ((List) pair.second).isEmpty()) {
            UIHelper.toast((int) R.string.kick_sharer_successfully);
        } else {
            UIHelper.toast((int) R.string.kick_sharer_failed);
        }
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseFragment
    public void doSyncFromServer(AlbumShareUIManager.OnCompletionListener<Void, Void> onCompletionListener) {
        if (CloudUtils.isValidAlbumId(this.mAlbumId)) {
            getUserCache().syncFromServer(this.mAlbumId, onCompletionListener);
        }
    }

    /* renamed from: updateStatus */
    public void lambda$tryToCreateCloudItem$7() {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }
        String albumId = this.mCloudSingleMediaSet.getAlbumId();
        this.mAlbumId = albumId;
        if (CloudUtils.isValidAlbumId(albumId)) {
            getView().findViewById(16908351).setVisibility(0);
            getView().findViewById(R.id.status_wrapper).setVisibility(8);
            updateSharers();
            syncFromServer();
            return;
        }
        getView().findViewById(16908351).setVisibility(8);
        getView().findViewById(R.id.status_wrapper).setVisibility(0);
        Future<?> future = this.mFutureCreateItem;
        if (future != null && !future.isDone()) {
            getView().findViewById(R.id.loading_bar).setVisibility(0);
            ((TextView) getView().findViewById(R.id.loading_text)).setText(R.string.sync_album_in_proecess);
            getView().findViewById(R.id.retry_button).setVisibility(8);
            return;
        }
        getView().findViewById(R.id.loading_bar).setVisibility(8);
        ((TextView) getView().findViewById(R.id.loading_text)).setText(R.string.fail_to_sync_album);
        getView().findViewById(R.id.retry_button).setVisibility(0);
    }

    public final void tryToCreateCloudItem() {
        if (CloudUtils.isValidAlbumId(this.mCloudSingleMediaSet.getAlbumId())) {
            return;
        }
        this.mFutureCreateItem = AlbumShareUIManager.tryToCreateCloudAlbumAsync(String.valueOf(this.mCloudSingleMediaSet.getId()), new AlbumShareUIManager.OnCompletionListener() { // from class: com.miui.gallery.share.ShareAlbumOwnerBaseFragment$$ExternalSyntheticLambda4
            @Override // com.miui.gallery.share.AlbumShareUIManager.OnCompletionListener
            public final void onCompletion(Object obj, Object obj2, int i, boolean z) {
                ShareAlbumOwnerBaseFragment.m1384$r8$lambda$RI64DEQUyPlZDgoqaXm398rut0(ShareAlbumOwnerBaseFragment.this, (Void) obj, (String) obj2, i, z);
            }
        });
    }

    public /* synthetic */ void lambda$tryToCreateCloudItem$8(Void r1, String str, int i, boolean z) {
        this.mObserver.onChange(true);
        AlbumShareUIManager.doAfterCloudMediaSetSetReload(new Runnable() { // from class: com.miui.gallery.share.ShareAlbumOwnerBaseFragment$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                ShareAlbumOwnerBaseFragment.m1385$r8$lambda$S9mCUPkyw6t6oFGOPCPm3XFbeo(ShareAlbumOwnerBaseFragment.this);
            }
        }, this.mCloudSingleMediaSet.getPath(), true);
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseFragment, com.miui.gallery.share.GridPreferenceBase.OnItemClickListener
    public void onItemClick(GridPreferenceBase gridPreferenceBase, int i) {
        ShareUserAdapterBase shareUserAdapterBase = this.mShareUserGridAdapter;
        if (shareUserAdapterBase == null || i >= shareUserAdapterBase.getCount()) {
            return;
        }
        CloudUserCacheEntry mo1387getItem = this.mShareUserGridAdapter.mo1387getItem(i);
        if (mo1387getItem == null || (TextUtils.isEmpty(mo1387getItem.mUserId) && TextUtils.isEmpty(mo1387getItem.mServerStatus))) {
            showAddSharerDialog(mo1387getItem);
            TrackController.trackClick("403.23.0.1.11306", AutoTracking.getRef());
        } else if (!TextUtils.equals(mo1387getItem.mServerStatus, "normal")) {
        } else {
            if (TextUtils.equals(this.mCreatorId, mo1387getItem.mUserId)) {
                UIHelper.toast((int) R.string.cannot_remove_creator);
            } else {
                showKickUserDialog(mo1387getItem);
            }
        }
    }

    public void requestInvitation(String str, String str2, String str3, String str4, AlbumShareUIManager.OnCompletionListener<Void, AlbumShareOperations.OutgoingInvitation> onCompletionListener) {
        this.mFutureSms = AlbumShareUIManager.requestInvitationForSmsAsync(this.mAlbumId, str, str2, str3, str4, onCompletionListener, AlbumShareUIManager.BlockMessage.create(getActivity(), getString(R.string.request_sms_url_title), getString(R.string.request_sms_url_msg)));
    }

    /* loaded from: classes2.dex */
    public class CloudObserver extends ContentObserver {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public CloudObserver(Handler handler) {
            super(handler);
            ShareAlbumOwnerBaseFragment.this = r1;
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            super.onChange(z);
            ShareAlbumOwnerBaseFragment.this.mCloudSingleMediaSet.rereloadItem();
            ShareAlbumOwnerBaseFragment.this.lambda$tryToCreateCloudItem$7();
        }
    }
}
