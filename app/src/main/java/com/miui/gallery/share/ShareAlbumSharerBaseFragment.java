package com.miui.gallery.share;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import com.google.common.collect.Lists;
import com.miui.gallery.R;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.share.AlbumShareUIManager;
import com.miui.gallery.share.CloudShareAlbumMediator;
import com.miui.gallery.share.ShareAlbumBaseFragment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public abstract class ShareAlbumSharerBaseFragment extends ShareAlbumBaseFragment implements View.OnClickListener, CloudShareAlbumMediator.OnShareAlbumExitedListener {
    public Handler mHandler;
    public Button mViewExitShare;

    public static /* synthetic */ void $r8$lambda$4NRhYLHU4wDC8pZ4If3uQKpnFXE(Activity activity, String str, Runnable runnable, Path path, Void r4, int i, boolean z) {
        lambda$exitShare$1(activity, str, runnable, path, r4, i, z);
    }

    public static /* synthetic */ void $r8$lambda$AM_rrEHwrWBfE4i_lczi9FJz_BI(String str, AlbumShareUIManager.OnCompletionListener onCompletionListener, AlbumShareUIManager.BlockMessage blockMessage, DialogInterface dialogInterface, int i) {
        lambda$exitShare$2(str, onCompletionListener, blockMessage, dialogInterface, i);
    }

    public static /* synthetic */ void $r8$lambda$jaIuyiOlHEM5tkIHZQpMFC9EfGQ(ShareAlbumSharerBaseFragment shareAlbumSharerBaseFragment) {
        shareAlbumSharerBaseFragment.lambda$onClick$0();
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseFragment, com.miui.gallery.app.fragment.MiuiPreferenceFragment, miuix.preference.PreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!CloudUtils.isValidAlbumId(this.mAlbumId)) {
            getActivity().finish();
        }
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        Button button = (Button) view.findViewById(R.id.exit_share);
        this.mViewExitShare = button;
        button.setOnClickListener(this);
        syncFromServer();
        this.mHandler = new Handler();
        CloudShareAlbumMediator.getInstance().addListener(this);
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseFragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        CloudShareAlbumMediator.getInstance().removeListener(this);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.exit_share) {
            exitShare(getActivity(), this.mPath, this.mAlbumName, new Runnable() { // from class: com.miui.gallery.share.ShareAlbumSharerBaseFragment$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    ShareAlbumSharerBaseFragment.$r8$lambda$jaIuyiOlHEM5tkIHZQpMFC9EfGQ(ShareAlbumSharerBaseFragment.this);
                }
            });
            return;
        }
        throw new UnsupportedOperationException("Invalid view id, id=" + view.getId());
    }

    public /* synthetic */ void lambda$onClick$0() {
        getActivity().setResult(-1);
        getActivity().finish();
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseFragment
    public CloudUserCache getUserCache() {
        return CloudUserCache.getSharerUserCache();
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseFragment
    public List<CloudUserCacheEntry> getShareUsers() {
        ArrayList newArrayList = Lists.newArrayList();
        newArrayList.addAll(getUserCache().getCloudUserListByAlbumId(this.mAlbumId));
        Collections.sort(newArrayList, new ShareAlbumBaseFragment.UserCacheDescComparator());
        String str = this.mCreatorId;
        String accountName = GalleryCloudUtils.getAccountName();
        Iterator it = newArrayList.iterator();
        CloudUserCacheEntry cloudUserCacheEntry = null;
        while (it.hasNext()) {
            CloudUserCacheEntry cloudUserCacheEntry2 = (CloudUserCacheEntry) it.next();
            String str2 = cloudUserCacheEntry2.mUserId;
            if (TextUtils.equals(str2, str)) {
                it.remove();
            } else if (TextUtils.equals(str2, accountName)) {
                it.remove();
                cloudUserCacheEntry = cloudUserCacheEntry2;
            }
        }
        if (cloudUserCacheEntry != null) {
            newArrayList.add(0, cloudUserCacheEntry);
        } else {
            newArrayList.add(0, new CloudUserCacheEntry(this.mAlbumId, accountName, 0L, null, null, null, null));
        }
        newArrayList.add(0, getOwnerEntry(str));
        return newArrayList;
    }

    public static void exitShare(final Activity activity, final String str, final String str2, final Runnable runnable) {
        final AlbumShareUIManager.OnCompletionListener onCompletionListener = new AlbumShareUIManager.OnCompletionListener() { // from class: com.miui.gallery.share.ShareAlbumSharerBaseFragment$$ExternalSyntheticLambda1
            @Override // com.miui.gallery.share.AlbumShareUIManager.OnCompletionListener
            public final void onCompletion(Object obj, Object obj2, int i, boolean z) {
                ShareAlbumSharerBaseFragment.$r8$lambda$4NRhYLHU4wDC8pZ4If3uQKpnFXE(activity, str2, runnable, (Path) obj, (Void) obj2, i, z);
            }
        };
        final AlbumShareUIManager.BlockMessage create = AlbumShareUIManager.BlockMessage.create(activity, null, activity.getString(R.string.exit_share_in_process, new Object[]{str2}), false);
        new AlertDialog.Builder(activity).setTitle(R.string.album_share_exit_confirm).setPositiveButton(R.string.exit, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.share.ShareAlbumSharerBaseFragment$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                ShareAlbumSharerBaseFragment.$r8$lambda$AM_rrEHwrWBfE4i_lczi9FJz_BI(str, onCompletionListener, create, dialogInterface, i);
            }
        }).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).show();
    }

    public static /* synthetic */ void lambda$exitShare$1(Activity activity, String str, Runnable runnable, Path path, Void r5, int i, boolean z) {
        if (z) {
            UIHelper.toast((int) R.string.cancel_hint);
        } else if (i == 0) {
            UIHelper.toast(activity, activity.getString(R.string.exit_share_successfully, new Object[]{activity.getString(R.string.quotation, new Object[]{str})}));
            runnable.run();
        } else {
            UIHelper.toast(activity, activity.getString(R.string.exit_share_failed, new Object[]{activity.getString(R.string.quotation, new Object[]{str})}));
        }
    }

    public static /* synthetic */ void lambda$exitShare$2(String str, AlbumShareUIManager.OnCompletionListener onCompletionListener, AlbumShareUIManager.BlockMessage blockMessage, DialogInterface dialogInterface, int i) {
        AlbumShareUIManager.exitAlbumShareAsync(Path.fromString(str), onCompletionListener, blockMessage);
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseFragment
    public void doSyncFromServer(AlbumShareUIManager.OnCompletionListener<Void, Void> onCompletionListener) {
        getUserCache().syncFromServer(this.mAlbumId, onCompletionListener);
    }
}
