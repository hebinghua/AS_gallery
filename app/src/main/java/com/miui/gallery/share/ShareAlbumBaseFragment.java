package com.miui.gallery.share;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import androidx.preference.Preference;
import com.miui.gallery.R;
import com.miui.gallery.app.fragment.MiuiPreferenceFragment;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.cloud.baby.BabyInfo;
import com.miui.gallery.share.AlbumShareUIManager;
import com.miui.gallery.share.DBCache;
import com.miui.gallery.share.GridPreferenceBase;
import java.util.Comparator;
import java.util.List;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public abstract class ShareAlbumBaseFragment extends MiuiPreferenceFragment implements GridPreferenceBase.OnItemClickListener, Preference.OnPreferenceChangeListener {
    public String mAlbumId;
    public String mAlbumName;
    public CloudSharerMediaSet mCloudSingleMediaSet;
    public String mCreatorId;
    public boolean mIsSharedToTv;
    public String mPath;
    public Dialog mShareFailedDialog;
    public ShareUserAdapterBase mShareUserGridAdapter;
    public GridPreferenceBase mSharerGridPreference;
    public int mSharerCount = 0;
    public final DBCache.OnDBCacheChangedListener<String, UserInfo> mUserInfoCacheListener = new DBCache.OnDBCacheChangedListener() { // from class: com.miui.gallery.share.ShareAlbumBaseFragment$$ExternalSyntheticLambda1
        @Override // com.miui.gallery.share.DBCache.OnDBCacheChangedListener
        public final void onDBCacheChanged(DBCache dBCache) {
            ShareAlbumBaseFragment.m1379$r8$lambda$2FpnKgc0zVd4hVB74Xh_OkCbo(ShareAlbumBaseFragment.this, dBCache);
        }
    };
    public final DBCache.OnDBCacheChangedListener<String, List<CloudUserCacheEntry>> mAlbumUserCacheListener = new DBCache.OnDBCacheChangedListener() { // from class: com.miui.gallery.share.ShareAlbumBaseFragment$$ExternalSyntheticLambda2
        @Override // com.miui.gallery.share.DBCache.OnDBCacheChangedListener
        public final void onDBCacheChanged(DBCache dBCache) {
            ShareAlbumBaseFragment.m1380$r8$lambda$Ecqj0aNQIkMTUI10AFmMX6F5sA(ShareAlbumBaseFragment.this, dBCache);
        }
    };

    /* renamed from: $r8$lambda$2FpnKgc--0zVd4hVB74Xh_OkCbo */
    public static /* synthetic */ void m1379$r8$lambda$2FpnKgc0zVd4hVB74Xh_OkCbo(ShareAlbumBaseFragment shareAlbumBaseFragment, DBCache dBCache) {
        shareAlbumBaseFragment.lambda$new$0(dBCache);
    }

    /* renamed from: $r8$lambda$Ecqj0a-NQIkMTUI10AFmMX6F5sA */
    public static /* synthetic */ void m1380$r8$lambda$Ecqj0aNQIkMTUI10AFmMX6F5sA(ShareAlbumBaseFragment shareAlbumBaseFragment, DBCache dBCache) {
        shareAlbumBaseFragment.lambda$new$1(dBCache);
    }

    /* renamed from: $r8$lambda$we4LVK8wZVcKrB274l201eJYC-w */
    public static /* synthetic */ void m1381$r8$lambda$we4LVK8wZVcKrB274l201eJYCw(ShareAlbumBaseFragment shareAlbumBaseFragment, Void r1, Void r2, int i, boolean z) {
        shareAlbumBaseFragment.lambda$syncFromServer$2(r1, r2, i, z);
    }

    public abstract ShareUserAdapterBase createShareUserAdapter();

    public abstract void doSyncFromServer(AlbumShareUIManager.OnCompletionListener<Void, Void> onCompletionListener);

    public void fillResult(Intent intent) {
    }

    public abstract int getPreferencesResourceId();

    public abstract List<CloudUserCacheEntry> getShareUsers();

    public abstract CloudUserCache getUserCache();

    @Override // com.miui.gallery.share.GridPreferenceBase.OnItemClickListener
    public void onItemClick(GridPreferenceBase gridPreferenceBase, int i) {
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        return false;
    }

    public void onUpdateProgressChanged(int i) {
    }

    public /* synthetic */ void lambda$new$0(DBCache dBCache) {
        updateSharers();
    }

    public /* synthetic */ void lambda$new$1(DBCache dBCache) {
        updateSharers();
    }

    @Override // androidx.preference.PreferenceFragmentCompat
    public void onCreatePreferences(Bundle bundle, String str) {
        setPreferencesFromResource(getPreferencesResourceId(), str);
    }

    @Override // com.miui.gallery.app.fragment.MiuiPreferenceFragment, miuix.preference.PreferenceFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        if (bundle == null) {
            bundle = getActivity().getIntent().getExtras();
        }
        if (bundle != null) {
            this.mPath = bundle.getString("share_path");
        }
        if (TextUtils.isEmpty(this.mPath)) {
            Log.e("ShareAlbumBaseFragment", "album path is invalid:" + this.mPath);
            getActivity().finish();
            return;
        }
        CloudSharerMediaSet mediaSet = Path.fromString(this.mPath).getMediaSet();
        this.mCloudSingleMediaSet = mediaSet;
        this.mAlbumName = mediaSet.getDisplayName();
        this.mAlbumId = this.mCloudSingleMediaSet.getAlbumId();
        if (this.mCloudSingleMediaSet.isOtherSharerAlbum()) {
            this.mCreatorId = this.mCloudSingleMediaSet.getCreatorId();
        } else {
            this.mCreatorId = GalleryCloudUtils.getAccountName();
        }
        this.mIsSharedToTv = this.mCloudSingleMediaSet.isSharedToTv();
        Log.d("ShareAlbumBaseFragment", "isSharedToTv=" + this.mIsSharedToTv);
        super.onCreate(bundle);
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("share_path", this.mPath);
    }

    @Override // com.miui.gallery.app.fragment.MiuiPreferenceFragment, miuix.preference.PreferenceFragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        handleOrientationConfiguration(configuration);
    }

    @Override // androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        initPreferences();
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        getUserCache().addListener(this.mAlbumUserCacheListener);
        UserInfoCache.getInstance().addListener(this.mUserInfoCacheListener);
        updateSharers();
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        getUserCache().removeListener(this.mAlbumUserCacheListener);
        UserInfoCache.getInstance().removeListener(this.mUserInfoCacheListener);
        super.onPause();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        GridPreferenceBase gridPreferenceBase = this.mSharerGridPreference;
        if (gridPreferenceBase != null) {
            gridPreferenceBase.setAdapter(null);
        }
        Dialog dialog = this.mShareFailedDialog;
        if (dialog != null && dialog.isShowing()) {
            this.mShareFailedDialog.dismiss();
        }
        super.onDestroy();
    }

    public void initPreferences() {
        this.mSharerGridPreference = (GridPreferenceBase) findPreference("sharer_grid");
        handleOrientationConfiguration(getResources().getConfiguration());
        this.mSharerGridPreference.setOnItemClickListener(this);
        ShareUserAdapterBase createShareUserAdapter = createShareUserAdapter();
        this.mShareUserGridAdapter = createShareUserAdapter;
        this.mSharerGridPreference.setAdapter(createShareUserAdapter);
    }

    public final void handleOrientationConfiguration(Configuration configuration) {
        int integer = getResources().getInteger(R.integer.share_grid_column_count);
        if (configuration.orientation == 2) {
            this.mSharerGridPreference.setColumnCountAndWidth(integer, getResources().getDimensionPixelSize(R.dimen.share_user_item_width_landscape));
        } else {
            this.mSharerGridPreference.setColumnCountAndWidth(integer, getResources().getDimensionPixelSize(R.dimen.share_user_item_width));
        }
    }

    public /* synthetic */ void lambda$syncFromServer$2(Void r1, Void r2, int i, boolean z) {
        onUpdateProgressChanged(0);
    }

    public final void syncFromServer() {
        AlbumShareUIManager.OnCompletionListener<Void, Void> onCompletionListener = new AlbumShareUIManager.OnCompletionListener() { // from class: com.miui.gallery.share.ShareAlbumBaseFragment$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.share.AlbumShareUIManager.OnCompletionListener
            public final void onCompletion(Object obj, Object obj2, int i, boolean z) {
                ShareAlbumBaseFragment.m1381$r8$lambda$we4LVK8wZVcKrB274l201eJYCw(ShareAlbumBaseFragment.this, (Void) obj, (Void) obj2, i, z);
            }
        };
        onUpdateProgressChanged(1);
        doSyncFromServer(onCompletionListener);
    }

    public void updateSharers() {
        if (!CloudUtils.isValidAlbumId(this.mAlbumId)) {
            return;
        }
        SystemClock.uptimeMillis();
        List<CloudUserCacheEntry> shareUsers = getShareUsers();
        this.mShareUserGridAdapter.setShareUsers(shareUsers);
        this.mSharerCount = shareUsers.size();
    }

    public CloudUserCacheEntry getOwnerEntry(String str) {
        BabyInfo babyInfo = this.mCloudSingleMediaSet.getBabyInfo();
        if (babyInfo != null) {
            return new CloudUserCacheEntry(this.mAlbumId, str, 0L, babyInfo.mRelation, babyInfo.mRelationText, "normal", null);
        }
        return new CloudUserCacheEntry(this.mAlbumId, str, 0L, null, null, null, null);
    }

    /* loaded from: classes2.dex */
    public static class UserCacheDescComparator implements Comparator<CloudUserCacheEntry> {
        @Override // java.util.Comparator
        public int compare(CloudUserCacheEntry cloudUserCacheEntry, CloudUserCacheEntry cloudUserCacheEntry2) {
            int i = (cloudUserCacheEntry.mCreateTime > cloudUserCacheEntry2.mCreateTime ? 1 : (cloudUserCacheEntry.mCreateTime == cloudUserCacheEntry2.mCreateTime ? 0 : -1));
            if (i == 0) {
                return 0;
            }
            return i < 0 ? 1 : -1;
        }
    }

    public void showShareFailedDialog(int i, int i2) {
        Dialog dialog = this.mShareFailedDialog;
        if (dialog != null && dialog.isShowing()) {
            this.mShareFailedDialog.dismiss();
        }
        AlertDialog create = new AlertDialog.Builder(getActivity()).setMessage(i).setPositiveButton(i2, (DialogInterface.OnClickListener) null).create();
        this.mShareFailedDialog = create;
        create.show();
    }
}
