package com.miui.gallery.activity.facebaby;

import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.cloud.baby.BabyInfo;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureHandler;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.share.AlbumShareUIManager;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.xiaomi.stat.a.j;
import miuix.preference.TextPreference;

/* loaded from: classes.dex */
public class SharerBabyAlbumSettingsFragment extends BabyAlbumSettingsFragment {
    public String mAccountName;
    public String mAlbumId;
    public Future mRefreshFuture;
    public String mSelfRelationText;
    public Future mSyncFuture;

    /* renamed from: $r8$lambda$-cWiwhEUQ60VvNRrdUvPwx9RVSw */
    public static /* synthetic */ void m482$r8$lambda$cWiwhEUQ60VvNRrdUvPwx9RVSw(SharerBabyAlbumSettingsFragment sharerBabyAlbumSettingsFragment, Void r1, Void r2, int i, boolean z) {
        sharerBabyAlbumSettingsFragment.lambda$syncShareInfoFromServer$3(r1, r2, i, z);
    }

    /* renamed from: $r8$lambda$ITQkDYR7miR-TjZurL29ZJc73mk */
    public static /* synthetic */ Void m483$r8$lambda$ITQkDYR7miRTjZurL29ZJc73mk(SharerBabyAlbumSettingsFragment sharerBabyAlbumSettingsFragment, BabyInfo babyInfo, ThreadPool.JobContext jobContext) {
        return sharerBabyAlbumSettingsFragment.lambda$saveBabyInfo$0(babyInfo, jobContext);
    }

    public static /* synthetic */ String $r8$lambda$oqcgN8QBCvdZ03nnyCwaCQygRrU(SharerBabyAlbumSettingsFragment sharerBabyAlbumSettingsFragment, ThreadPool.JobContext jobContext) {
        return sharerBabyAlbumSettingsFragment.lambda$refreshSelfRelationText$1(jobContext);
    }

    @Override // com.miui.gallery.activity.facebaby.BabyAlbumSettingsFragment
    public int getPreferenceResourceId() {
        return R.xml.sharer_baby_album_preferences;
    }

    @Override // com.miui.gallery.activity.facebaby.BabyAlbumSettingsFragment, androidx.preference.PreferenceFragmentCompat, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        refreshSelfRelationText(true);
    }

    @Override // com.miui.gallery.activity.facebaby.BabyAlbumSettingsFragment
    public void saveBabyInfo() {
        if (!babyInfoChanged()) {
            return;
        }
        final BabyInfo babyInfo = toBabyInfo(this.mPeopleId, 0);
        ThreadManager.getMiscPool().submit(new ThreadPool.Job() { // from class: com.miui.gallery.activity.facebaby.SharerBabyAlbumSettingsFragment$$ExternalSyntheticLambda1
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public final Object mo1807run(ThreadPool.JobContext jobContext) {
                return SharerBabyAlbumSettingsFragment.m483$r8$lambda$ITQkDYR7miRTjZurL29ZJc73mk(SharerBabyAlbumSettingsFragment.this, babyInfo, jobContext);
            }
        });
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable("baby-info", babyInfo);
        intent.putExtras(bundle);
        getActivity().setResult(12, intent);
        this.mHaveSaveBabyInfo = true;
    }

    public /* synthetic */ Void lambda$saveBabyInfo$0(BabyInfo babyInfo, ThreadPool.JobContext jobContext) {
        BabyInfo babyInfoFromDB = getBabyInfoFromDB();
        if (babyInfoFromDB != null) {
            babyInfoFromDB.mAutoupdate = this.mIsAutoupdate.booleanValue();
            babyInfoFromDB.mPeopleId = this.mPeopleId;
            babyInfo = babyInfoFromDB;
        }
        String transformToEditedColumnsElement = GalleryCloudUtils.transformToEditedColumnsElement(25);
        Object[] objArr = new Object[12];
        objArr[0] = "shareAlbum";
        objArr[1] = "babyInfoJson";
        String str = "";
        objArr[2] = babyInfo == null ? str : babyInfo.toJSON();
        objArr[3] = "peopleId";
        if (babyInfo != null) {
            str = babyInfo.mPeopleId;
        }
        objArr[4] = str;
        objArr[5] = "editedColumns";
        objArr[6] = "editedColumns";
        objArr[7] = transformToEditedColumnsElement;
        objArr[8] = transformToEditedColumnsElement;
        objArr[9] = transformToEditedColumnsElement;
        objArr[10] = j.c;
        objArr[11] = this.mBabyAlbumLocalId;
        GalleryUtils.safeExec(String.format("update %s set %s=%s, %s='%s', %s=coalesce(replace(%s, '%s', '') || '%s', '%s') where %s=%s", objArr));
        GalleryApp.sGetAndroidContext().getContentResolver().notifyChange(GalleryContract.Album.URI, (ContentObserver) null, true);
        return null;
    }

    @Override // com.miui.gallery.activity.facebaby.BabyAlbumSettingsFragment
    public void justSaveInfo2DbByJson() {
        if (this.mHaveSaveBabyInfo) {
            return;
        }
        saveBabyInfo();
    }

    @Override // com.miui.gallery.activity.facebaby.BabyAlbumSettingsFragment
    public void setPreferencesValue() {
        super.setPreferencesValue();
        ((TextPreference) this.mBabyNicknamePre).setText(this.mNickName);
        ((TextPreference) this.mBabySexPre).setText(getEntry(this.mSex, R.array.baby_sexy_value, R.array.baby_sexy));
        ((TextPreference) this.mRelationPre).setText(this.mSelfRelationText);
        ((TextPreference) this.mChooseDate).setText(this.mBirthday);
    }

    public final String getEntry(String str, int i, int i2) {
        String[] stringArray = getResources().getStringArray(i);
        String[] stringArray2 = getResources().getStringArray(i2);
        for (int i3 = 0; i3 < stringArray.length; i3++) {
            if (TextUtils.equals(stringArray[i3], str)) {
                return stringArray2[i3];
            }
        }
        return null;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        Future future = this.mRefreshFuture;
        if (future != null) {
            future.cancel();
            this.mRefreshFuture = null;
        }
        Future future2 = this.mSyncFuture;
        if (future2 != null) {
            future2.cancel();
            this.mSyncFuture = null;
        }
        super.onDestroy();
    }

    public final void refreshSelfRelationText(final boolean z) {
        Future future = this.mRefreshFuture;
        if (future == null || future.isCancelled()) {
            this.mRefreshFuture = ThreadManager.getMiscPool().submit(new ThreadPool.Job() { // from class: com.miui.gallery.activity.facebaby.SharerBabyAlbumSettingsFragment$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.concurrent.ThreadPool.Job
                /* renamed from: run */
                public final Object mo1807run(ThreadPool.JobContext jobContext) {
                    return SharerBabyAlbumSettingsFragment.$r8$lambda$oqcgN8QBCvdZ03nnyCwaCQygRrU(SharerBabyAlbumSettingsFragment.this, jobContext);
                }
            }, new FutureHandler<String>() { // from class: com.miui.gallery.activity.facebaby.SharerBabyAlbumSettingsFragment.1
                {
                    SharerBabyAlbumSettingsFragment.this = this;
                }

                @Override // com.miui.gallery.concurrent.FutureHandler
                public void onPostExecute(Future<String> future2) {
                    if (!future2.isCancelled()) {
                        String str = future2.get();
                        if (!TextUtils.isEmpty(str)) {
                            if (!TextUtils.equals(SharerBabyAlbumSettingsFragment.this.mSelfRelationText, str)) {
                                SharerBabyAlbumSettingsFragment.this.mSelfRelationText = str;
                                ((TextPreference) SharerBabyAlbumSettingsFragment.this.mRelationPre).setText(str);
                            }
                        } else if (z) {
                            SharerBabyAlbumSettingsFragment.this.syncShareInfoFromServer();
                        }
                    }
                    SharerBabyAlbumSettingsFragment.this.mRefreshFuture = null;
                }
            });
        }
    }

    public /* synthetic */ String lambda$refreshSelfRelationText$1(ThreadPool.JobContext jobContext) {
        return getSelfRelationText();
    }

    public final String getSelfRelationText() {
        if (TextUtils.isEmpty(this.mAlbumId)) {
            this.mAlbumId = CloudUtils.getDBShareAlbumByLocalId(String.valueOf(this.mBabyAlbumLocalId)).getAlbumId();
        }
        if (TextUtils.isEmpty(this.mAccountName)) {
            this.mAccountName = GalleryCloudUtils.getAccountName();
        }
        return (String) GalleryUtils.safeQuery(GalleryCloudUtils.SHARE_USER_URI, new String[]{"relationText"}, String.format("%s=? AND %s=?", "albumId", "userId"), new String[]{this.mAlbumId, this.mAccountName}, (String) null, SharerBabyAlbumSettingsFragment$$ExternalSyntheticLambda3.INSTANCE);
    }

    public static /* synthetic */ String lambda$getSelfRelationText$2(Cursor cursor) {
        return (cursor == null || !cursor.moveToNext()) ? "" : cursor.getString(0);
    }

    public final void syncShareInfoFromServer() {
        if (!TextUtils.isEmpty(this.mAlbumId)) {
            this.mSyncFuture = AlbumShareUIManager.syncUserListForAlbumAsync(this.mAlbumId, true, new AlbumShareUIManager.OnCompletionListener() { // from class: com.miui.gallery.activity.facebaby.SharerBabyAlbumSettingsFragment$$ExternalSyntheticLambda2
                @Override // com.miui.gallery.share.AlbumShareUIManager.OnCompletionListener
                public final void onCompletion(Object obj, Object obj2, int i, boolean z) {
                    SharerBabyAlbumSettingsFragment.m482$r8$lambda$cWiwhEUQ60VvNRrdUvPwx9RVSw(SharerBabyAlbumSettingsFragment.this, (Void) obj, (Void) obj2, i, z);
                }
            });
        }
    }

    public /* synthetic */ void lambda$syncShareInfoFromServer$3(Void r1, Void r2, int i, boolean z) {
        if (i != 0 || z) {
            return;
        }
        refreshSelfRelationText(false);
    }

    public final BabyInfo getBabyInfoFromDB() {
        return (BabyInfo) GalleryUtils.safeQuery(GalleryCloudUtils.SHARE_ALBUM_URI, new String[]{"babyInfoJson"}, "_id = ?", new String[]{Long.toString(this.mBabyAlbumLocalId.longValue())}, (String) null, SharerBabyAlbumSettingsFragment$$ExternalSyntheticLambda4.INSTANCE);
    }

    public static /* synthetic */ BabyInfo lambda$getBabyInfoFromDB$4(Cursor cursor) {
        if (cursor == null || !cursor.moveToNext()) {
            return null;
        }
        return BabyInfo.fromJSON(cursor.getString(0));
    }

    @Override // com.miui.gallery.activity.facebaby.BabyAlbumSettingsFragment
    public Intent getIntentToAutoUploadPage() {
        Intent intentToAutoUploadPage = super.getIntentToAutoUploadPage();
        intentToAutoUploadPage.putExtra("allow_to_reassociate", true);
        return intentToAutoUploadPage;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i != 0) {
            super.onActivityResult(i, i2, intent);
        } else if (i2 != -1 || intent == null) {
        } else {
            BabyInfo babyInfo = (BabyInfo) intent.getParcelableExtra("baby-info");
            this.mPeopleId = babyInfo.mPeopleId;
            this.mIsAutoupdate = Boolean.valueOf(babyInfo.mAutoupdate);
            this.mFaceAlbumLocalId = Long.valueOf(intent.getLongExtra("associate_people_face_local_id", -1L));
        }
    }
}
