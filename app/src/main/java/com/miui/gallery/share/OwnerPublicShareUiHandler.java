package com.miui.gallery.share;

import androidx.preference.CheckBoxPreference;
import androidx.preference.Preference;
import com.miui.gallery.R;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.share.AlbumShareUIManager;
import java.util.LinkedList;

/* loaded from: classes2.dex */
public class OwnerPublicShareUiHandler extends PublicShareUiHandler {
    public static long CLICK_PUBLIC_INTERNAL = 60000;
    public static long CLICK_PUBLIC_LIMIT = 10;
    public static LinkedList<Long> sClickTimes;
    public CheckBoxPreference mOpenPublicPreference;
    public PublishProgressPreference mProgressPreference;

    /* renamed from: $r8$lambda$wauwRWFbAfC2q-LJqN3c6kdwW5o */
    public static /* synthetic */ void m1376$r8$lambda$wauwRWFbAfC2qLJqN3c6kdwW5o(OwnerPublicShareUiHandler ownerPublicShareUiHandler, Void r1, String str, int i, boolean z) {
        ownerPublicShareUiHandler.lambda$tryToChangePublicStatus$0(r1, str, i, z);
    }

    public OwnerPublicShareUiHandler(NormalShareAlbumOwnerFragment normalShareAlbumOwnerFragment, String str, CloudSharerMediaSet cloudSharerMediaSet) {
        super(normalShareAlbumOwnerFragment, str, cloudSharerMediaSet);
    }

    @Override // com.miui.gallery.share.PublicShareUiHandler
    public void initPreferences() {
        super.initPreferences();
        CheckBoxPreference checkBoxPreference = (CheckBoxPreference) this.mFragment.findPreference("open_public_share");
        this.mOpenPublicPreference = checkBoxPreference;
        checkBoxPreference.setOnPreferenceChangeListener(this.mFragment);
    }

    public final void tryToChangePublicStatus() {
        Future<?> future = this.mFuturePublic;
        if (future == null || future.isDone() || this.mFuturePublic.isCancelled()) {
            if (!canClickPublic()) {
                UIHelper.toast((int) R.string.operation_too_often);
                CheckBoxPreference checkBoxPreference = this.mOpenPublicPreference;
                checkBoxPreference.setChecked(true ^ checkBoxPreference.isChecked());
                return;
            }
            if (this.mProgressPreference == null) {
                this.mProgressPreference = new PublishProgressPreference(this.mFragment.getContext());
            }
            this.mPublicPreference.addPreference(this.mProgressPreference);
            this.mProgressPreference.setProgress(true, this.mOpenPublicPreference.isChecked());
            this.mFuturePublic = AlbumShareUIManager.changePublicStatusAsync(this.mAlbumId, this.mOpenPublicPreference.isChecked(), new AlbumShareUIManager.OnCompletionListener() { // from class: com.miui.gallery.share.OwnerPublicShareUiHandler$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.share.AlbumShareUIManager.OnCompletionListener
                public final void onCompletion(Object obj, Object obj2, int i, boolean z) {
                    OwnerPublicShareUiHandler.m1376$r8$lambda$wauwRWFbAfC2qLJqN3c6kdwW5o(OwnerPublicShareUiHandler.this, (Void) obj, (String) obj2, i, z);
                }
            });
            return;
        }
        this.mPublicPreference.removePreference(this.mProgressPreference);
        Future<?> future2 = this.mFuturePublic;
        if (future2 == null) {
            return;
        }
        future2.cancel();
    }

    public /* synthetic */ void lambda$tryToChangePublicStatus$0(Void r2, String str, int i, boolean z) {
        this.mPublicPreference.removePreference(this.mProgressPreference);
        if (z) {
            return;
        }
        if (i == 0) {
            updatePublicPreference(this.mOpenPublicPreference.isChecked(), str);
            UIHelper.toast(this.mOpenPublicPreference.isChecked() ? R.string.publish_succeeded : R.string.unpublish_succeeded);
            CheckBoxPreference checkBoxPreference = this.mOpenPublicPreference;
            checkBoxPreference.setChecked(checkBoxPreference.isChecked());
            return;
        }
        updatePublicPreference(!this.mOpenPublicPreference.isChecked(), str);
        UIHelper.toast(this.mOpenPublicPreference.isChecked() ? R.string.publish_failed : R.string.unpublish_failed);
        CheckBoxPreference checkBoxPreference2 = this.mOpenPublicPreference;
        checkBoxPreference2.setChecked(!checkBoxPreference2.isChecked());
    }

    public void updateStatus() {
        String albumId = this.mCloudSingleMediaSet.getAlbumId();
        this.mAlbumId = albumId;
        if (CloudUtils.isValidAlbumId(albumId)) {
            updatePublicPreference(this.mCloudSingleMediaSet.isPublic(), this.mCloudSingleMediaSet.getPublicUrl());
            this.mOpenPublicPreference.setChecked(this.mCloudSingleMediaSet.isPublic());
            return;
        }
        updatePublicPreference(false, null);
        this.mOpenPublicPreference.setChecked(false);
    }

    public boolean onPreferenceTreeClick(Preference preference) {
        if ("open_public_share".equals(preference.getKey())) {
            tryToChangePublicStatus();
            return true;
        }
        return false;
    }

    public final boolean canClickPublic() {
        long currentTimeMillis = System.currentTimeMillis();
        LinkedList<Long> linkedList = sClickTimes;
        if (linkedList == null) {
            LinkedList<Long> linkedList2 = new LinkedList<>();
            sClickTimes = linkedList2;
            linkedList2.addLast(Long.valueOf(currentTimeMillis));
            return true;
        } else if (linkedList.size() < CLICK_PUBLIC_LIMIT) {
            sClickTimes.addLast(Long.valueOf(currentTimeMillis));
            return true;
        } else if (currentTimeMillis - sClickTimes.getFirst().longValue() > CLICK_PUBLIC_INTERNAL) {
            sClickTimes.removeFirst();
            sClickTimes.addLast(Long.valueOf(currentTimeMillis));
            return true;
        } else if (currentTimeMillis - sClickTimes.getFirst().longValue() <= CLICK_PUBLIC_INTERNAL && currentTimeMillis - sClickTimes.getFirst().longValue() >= 0 && sClickTimes.size() >= CLICK_PUBLIC_LIMIT) {
            return false;
        } else {
            sClickTimes.clear();
            return true;
        }
    }
}
