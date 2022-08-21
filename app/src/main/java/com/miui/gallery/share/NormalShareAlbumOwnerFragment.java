package com.miui.gallery.share;

import android.content.Intent;
import android.view.View;
import androidx.preference.Preference;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.share.AlbumShareOperations;
import com.miui.gallery.share.AlbumShareUIManager;
import com.miui.gallery.share.QRPreference;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public class NormalShareAlbumOwnerFragment extends ShareAlbumOwnerBaseFragment {
    public Future<?> mFutureQR;
    public Future<?> mFutureShareDevice;
    public OwnerPublicShareUiHandler mPublicShareUiHandler;
    public QRPreference mQRCodePreference;
    public ShareCheckBoxPreference mShareToTVPreference;

    public static /* synthetic */ void $r8$lambda$JsrDXSJAMb61ZQ8SleR426ZlhfE(NormalShareAlbumOwnerFragment normalShareAlbumOwnerFragment, View view) {
        normalShareAlbumOwnerFragment.lambda$initPreferences$0(view);
    }

    /* renamed from: $r8$lambda$osp0SjAC-HmGkd3YNOtrTUr6CNM */
    public static /* synthetic */ void m1375$r8$lambda$osp0SjACHmGkd3YNOtrTUr6CNM(NormalShareAlbumOwnerFragment normalShareAlbumOwnerFragment, Void r1, String str, int i, boolean z) {
        normalShareAlbumOwnerFragment.lambda$tryToRequestBarcodeUrl$1(r1, str, i, z);
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseFragment
    public int getPreferencesResourceId() {
        return R.xml.share_album_owner;
    }

    @Override // com.miui.gallery.share.ShareAlbumOwnerBaseFragment, com.miui.gallery.share.ShareAlbumBaseFragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        OwnerPublicShareUiHandler ownerPublicShareUiHandler = this.mPublicShareUiHandler;
        if (ownerPublicShareUiHandler != null) {
            ownerPublicShareUiHandler.onDestroy();
        }
        Future<?> future = this.mFutureQR;
        if (future != null) {
            future.cancel();
        }
        Future<?> future2 = this.mFutureShareDevice;
        if (future2 != null) {
            future2.cancel();
        }
        super.onDestroy();
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseFragment, androidx.preference.Preference.OnPreferenceChangeListener
    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (this.mPublicShareUiHandler.onPreferenceTreeClick(preference)) {
            return true;
        }
        if (!"open_share_to_tv".equals(preference.getKey())) {
            return false;
        }
        TrackController.trackClick("403.23.0.1.11307", AutoTracking.getRef(), ((Boolean) obj).booleanValue() ? "open" : "close");
        SamplingStatHelper.recordCountEvent("album", "share_album_to_device_switch");
        tryToSwitchShareToDevice();
        return true;
    }

    @Override // com.miui.gallery.share.ShareAlbumOwnerBaseFragment
    public void updateStatus() {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }
        super.lambda$tryToCreateCloudItem$7();
        if (CloudUtils.isValidAlbumId(this.mAlbumId)) {
            tryToRequestBarcodeUrl();
        }
        this.mShareToTVPreference.setChecked(this.mIsSharedToTv);
        this.mPublicShareUiHandler.updateStatus();
        HashMap hashMap = new HashMap(1);
        hashMap.put("state", String.valueOf(this.mIsSharedToTv));
        SamplingStatHelper.recordCountEvent("album", "share_album_to_device_switch", hashMap);
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseFragment
    public void initPreferences() {
        super.initPreferences();
        OwnerPublicShareUiHandler ownerPublicShareUiHandler = new OwnerPublicShareUiHandler(this, this.mAlbumName, this.mCloudSingleMediaSet);
        this.mPublicShareUiHandler = ownerPublicShareUiHandler;
        ownerPublicShareUiHandler.initPreferences();
        this.mQRCodePreference = (QRPreference) findPreference("qr_code");
        ShareCheckBoxPreference shareCheckBoxPreference = (ShareCheckBoxPreference) findPreference("open_share_to_tv");
        this.mShareToTVPreference = shareCheckBoxPreference;
        shareCheckBoxPreference.setOnPreferenceChangeListener(this);
        this.mQRCodePreference.setRetryListener(new View.OnClickListener() { // from class: com.miui.gallery.share.NormalShareAlbumOwnerFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                NormalShareAlbumOwnerFragment.$r8$lambda$JsrDXSJAMb61ZQ8SleR426ZlhfE(NormalShareAlbumOwnerFragment.this, view);
            }
        });
    }

    public /* synthetic */ void lambda$initPreferences$0(View view) {
        tryToRequestBarcodeUrl();
    }

    public final void tryToRequestBarcodeUrl() {
        if (this.mQRCodePreference.getQRText() == null) {
            Future<?> future = this.mFutureQR;
            if (future != null && !future.isDone() && !this.mFutureQR.isCancelled()) {
                return;
            }
            this.mQRCodePreference.setStatus(QRPreference.Status.REQUESTING);
            this.mFutureQR = AlbumShareUIManager.requestUrlForBarcodeAsync(this.mAlbumId, new AlbumShareUIManager.OnCompletionListener() { // from class: com.miui.gallery.share.NormalShareAlbumOwnerFragment$$ExternalSyntheticLambda1
                @Override // com.miui.gallery.share.AlbumShareUIManager.OnCompletionListener
                public final void onCompletion(Object obj, Object obj2, int i, boolean z) {
                    NormalShareAlbumOwnerFragment.m1375$r8$lambda$osp0SjACHmGkd3YNOtrTUr6CNM(NormalShareAlbumOwnerFragment.this, (Void) obj, (String) obj2, i, z);
                }
            });
        }
    }

    public /* synthetic */ void lambda$tryToRequestBarcodeUrl$1(Void r1, String str, int i, boolean z) {
        if (z) {
            return;
        }
        if (i == 0 && str != null) {
            this.mQRCodePreference.setQRText(str);
            this.mQRCodePreference.setStatus(QRPreference.Status.SUCCESS);
            return;
        }
        this.mQRCodePreference.setStatus(QRPreference.Status.FAILED);
    }

    public final void tryToSwitchShareToDevice() {
        Future<?> future = this.mFutureShareDevice;
        if (future == null || future.isDone() || this.mFutureShareDevice.isCancelled()) {
            if (!this.mIsSharedToTv) {
                this.mShareToTVPreference.setSummary(R.string.cloud_authorizing_to_device);
            } else {
                this.mShareToTVPreference.setSummary(R.string.cloud_cancel_authorizing_to_device);
            }
            this.mShareToTVPreference.setEnabled(false);
            this.mFutureShareDevice = AlbumShareUIManager.requestSwitchShareDevice(this.mAlbumId, "TV", !this.mIsSharedToTv, new AlbumShareUIManager.OnCompletionListener<Void, List<String>>() { // from class: com.miui.gallery.share.NormalShareAlbumOwnerFragment.1
                {
                    NormalShareAlbumOwnerFragment.this = this;
                }

                @Override // com.miui.gallery.share.AlbumShareUIManager.OnCompletionListener
                public void onCompletion(Void r2, List<String> list, int i, boolean z) {
                    NormalShareAlbumOwnerFragment.this.mShareToTVPreference.setEnabled(true);
                    if (z) {
                        return;
                    }
                    if (i != 0 || list == null) {
                        if (i == -6) {
                            ToastUtils.makeText(NormalShareAlbumOwnerFragment.this.getActivity(), (int) R.string.no_network_connect);
                        } else {
                            DefaultLogger.d("NormalShareAlbumOwnerFragment", "switch share to tv resultCode is %s", Integer.valueOf(i));
                            NormalShareAlbumOwnerFragment normalShareAlbumOwnerFragment = NormalShareAlbumOwnerFragment.this;
                            if (!normalShareAlbumOwnerFragment.mIsSharedToTv) {
                                normalShareAlbumOwnerFragment.showShareFailedDialog(R.string.authorize_watch_on_tv_failed, R.string.alert_secret_album_enter);
                            } else {
                                normalShareAlbumOwnerFragment.showShareFailedDialog(R.string.cancel_authorize_watch_on_tv_failed, R.string.alert_secret_album_enter);
                            }
                        }
                    } else if (list.contains("TV")) {
                        NormalShareAlbumOwnerFragment normalShareAlbumOwnerFragment2 = NormalShareAlbumOwnerFragment.this;
                        normalShareAlbumOwnerFragment2.mIsSharedToTv = true;
                        ToastUtils.makeText(normalShareAlbumOwnerFragment2.getActivity(), (int) R.string.already_authorize_to_device);
                    } else {
                        NormalShareAlbumOwnerFragment normalShareAlbumOwnerFragment3 = NormalShareAlbumOwnerFragment.this;
                        normalShareAlbumOwnerFragment3.mIsSharedToTv = false;
                        ToastUtils.makeText(normalShareAlbumOwnerFragment3.getActivity(), (int) R.string.already_cancel_authorize_to_device);
                    }
                    NormalShareAlbumOwnerFragment.this.mShareToTVPreference.setChecked(NormalShareAlbumOwnerFragment.this.mIsSharedToTv);
                    NormalShareAlbumOwnerFragment.this.mShareToTVPreference.setSummary(R.string.cloud_authorize_to_share_to_device);
                    HashMap hashMap = new HashMap(1);
                    hashMap.put("state", "true");
                    SamplingStatHelper.recordCountEvent("album", "share_album_to_device_opened", hashMap);
                }
            });
        }
    }

    @Override // com.miui.gallery.share.ShareAlbumOwnerBaseFragment, com.miui.gallery.share.ShareAlbumBaseFragment
    public List<CloudUserCacheEntry> getShareUsers() {
        List<CloudUserCacheEntry> shareUsers = super.getShareUsers();
        shareUsers.add(0, getOwnerEntry(GalleryCloudUtils.getAccountName()));
        return shareUsers;
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseFragment
    public ShareUserAdapterBase createShareUserAdapter() {
        return new ShareUserAdapter(getContext(), true, GalleryCloudUtils.getAccountName());
    }

    @Override // com.miui.gallery.share.ShareAlbumOwnerBaseFragment, com.miui.gallery.share.ShareAlbumBaseFragment
    public void updateSharers() {
        super.updateSharers();
        this.mPublicShareUiHandler.updateSharers(this.mSharerCount);
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseFragment
    public void onUpdateProgressChanged(int i) {
        this.mPublicShareUiHandler.updateProgressBar(i);
    }

    @Override // com.miui.gallery.share.ShareAlbumOwnerBaseFragment
    public void requestInvitation(CloudUserCacheEntry cloudUserCacheEntry, AlbumShareUIManager.OnCompletionListener<Void, AlbumShareOperations.OutgoingInvitation> onCompletionListener) {
        requestInvitation(null, null, null, null, onCompletionListener);
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseFragment
    public void fillResult(Intent intent) {
        super.fillResult(intent);
        intent.putExtra("extra_is_share_to_device", this.mIsSharedToTv);
    }

    @Override // com.miui.gallery.share.ShareAlbumBaseFragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        AutoTracking.trackView("403.23.0.1.11308", AutoTracking.getRef(), getShareUsers() == null ? 0 : getShareUsers().size());
    }
}
