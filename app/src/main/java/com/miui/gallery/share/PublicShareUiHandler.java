package com.miui.gallery.share;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceScreen;
import com.miui.gallery.R;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.util.FeatureUtil;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes2.dex */
public abstract class PublicShareUiHandler {
    public String mAlbumId;
    public String mAlbumName;
    public CloudSharerMediaSet mCloudSingleMediaSet;
    public ShareAlbumBaseFragment mFragment;
    public Future<?> mFuturePublic;
    public final Handler mHandler = new Handler() { // from class: com.miui.gallery.share.PublicShareUiHandler.1
        {
            PublicShareUiHandler.this = this;
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            PublicShareUiHandler.this.updateProgressBarDirectly(message.arg1);
        }
    };
    public PreferenceCategory mPublicPreference;
    public DescriptPreference mPublicUrlDescriptPreference;
    public PreferenceScreen mSendPublicUrlPreference;
    public PreferenceCategory mSharersPreference;

    public static /* synthetic */ boolean $r8$lambda$t3GV7T7Kt6HrnmiKgJ4UNAzoUGI(PublicShareUiHandler publicShareUiHandler, String str, Preference preference) {
        return publicShareUiHandler.lambda$updatePublicPreference$0(str, preference);
    }

    public PublicShareUiHandler(ShareAlbumBaseFragment shareAlbumBaseFragment, String str, CloudSharerMediaSet cloudSharerMediaSet) {
        this.mFragment = shareAlbumBaseFragment;
        this.mAlbumName = str;
        this.mCloudSingleMediaSet = cloudSharerMediaSet;
    }

    public void initPreferences() {
        PreferenceCategory preferenceCategory = (PreferenceCategory) this.mFragment.findPreference("sharers");
        this.mSharersPreference = preferenceCategory;
        preferenceCategory.setTitle(this.mFragment.getString(R.string.album_member, ""));
        this.mPublicPreference = (PreferenceCategory) this.mFragment.findPreference("public_share");
        this.mSendPublicUrlPreference = (PreferenceScreen) this.mFragment.findPreference("send_public_url");
    }

    public void updatePublicPreference(boolean z, final String str) {
        if (!FeatureUtil.isSupportPublicShareAlbum()) {
            this.mFragment.getPreferenceScreen().removePreference(this.mPublicPreference);
        } else if (z) {
            if (this.mSendPublicUrlPreference != null) {
                sendPublicPreference(str);
            }
            if (this.mPublicUrlDescriptPreference == null) {
                DescriptPreference descriptPreference = new DescriptPreference(this.mFragment.getContext(), null);
                this.mPublicUrlDescriptPreference = descriptPreference;
                descriptPreference.setKey("public_url_descript");
                this.mPublicUrlDescriptPreference.setDescriptTitle(R.string.public_url_descript);
            }
            if (!TextUtils.isEmpty(str)) {
                this.mPublicUrlDescriptPreference.setDescriptDetail(str);
                this.mPublicUrlDescriptPreference.setDescriptDetailVisibility(0);
                this.mPublicUrlDescriptPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() { // from class: com.miui.gallery.share.PublicShareUiHandler$$ExternalSyntheticLambda0
                    @Override // androidx.preference.Preference.OnPreferenceClickListener
                    public final boolean onPreferenceClick(Preference preference) {
                        return PublicShareUiHandler.$r8$lambda$t3GV7T7Kt6HrnmiKgJ4UNAzoUGI(PublicShareUiHandler.this, str, preference);
                    }
                });
            }
            if (this.mPublicPreference.findPreference("public_url_descript") != null) {
                return;
            }
            this.mPublicPreference.addPreference(this.mPublicUrlDescriptPreference);
        } else {
            PreferenceScreen preferenceScreen = this.mSendPublicUrlPreference;
            if (preferenceScreen != null) {
                this.mPublicPreference.removePreference(preferenceScreen);
            }
            DescriptPreference descriptPreference2 = this.mPublicUrlDescriptPreference;
            if (descriptPreference2 == null) {
                return;
            }
            this.mPublicPreference.removePreference(descriptPreference2);
        }
    }

    public /* synthetic */ boolean lambda$updatePublicPreference$0(String str, Preference preference) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.setData(Uri.parse(str));
        try {
            this.mFragment.startActivity(intent);
            return false;
        } catch (ActivityNotFoundException unused) {
            return false;
        }
    }

    public final void sendPublicPreference(String str) {
        this.mSendPublicUrlPreference.setIntent(PhoneShareAlbumProvider.getInstance().getShareUrlIntent((AppCompatActivity) this.mFragment.getActivity(), this.mAlbumName, "", str, true));
        this.mPublicPreference.addPreference(this.mSendPublicUrlPreference);
    }

    public void onDestroy() {
        Future<?> future = this.mFuturePublic;
        if (future != null) {
            future.cancel();
        }
    }

    public void updateSharers(int i) {
        if (CloudUtils.isValidAlbumId(this.mAlbumId)) {
            if (i > 0) {
                this.mSharersPreference.setTitle(this.mFragment.getResources().getQuantityString(R.plurals.album_member, i, Integer.valueOf(i)));
            } else {
                this.mSharersPreference.setTitle(this.mFragment.getString(R.string.album_member, ""));
            }
        }
    }

    public void updateProgressBar(int i) {
        this.mHandler.removeMessages(1);
        if (i != 1) {
            this.mHandler.sendMessageDelayed(Message.obtain(this.mHandler, 1, i, 0), 2000L);
            return;
        }
        updateProgressBarDirectly(i);
    }

    public final void updateProgressBarDirectly(int i) {
        if (i == 1) {
            this.mSharersPreference.setSummary(R.string.loading_members);
        } else {
            this.mSharersPreference.setSummary((CharSequence) null);
        }
    }
}
