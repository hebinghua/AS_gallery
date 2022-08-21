package com.miui.gallery.util;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import com.market.sdk.UpdateResponse;
import com.market.sdk.XiaomiUpdateAgent;
import com.market.sdk.XiaomiUpdateListener;
import com.market.sdk.utils.AppGlobal;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.card.scenario.DateUtils;
import com.miui.gallery.cloudcontrol.CloudControlManager;
import com.miui.gallery.cloudcontrol.FeatureProfile;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.ui.UpdateDialogFragment;
import java.util.Iterator;
import java.util.LinkedList;

/* loaded from: classes2.dex */
public class RequestUpdateHelper {
    public boolean mIsForceUpdate;
    public LinkedList<OnRequestUpdateFinishListener> mListeners;
    public XiaomiUpdateListener mUpdateListener;

    /* loaded from: classes2.dex */
    public interface OnRequestUpdateFinishListener {
        void onDialogCreate(int i, int i2, UpdateDialogFragment updateDialogFragment);

        void onFailure(int i);

        void onNotUpdate();

        void onRedDotShow();
    }

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final RequestUpdateHelper INSTANCE = new RequestUpdateHelper();
    }

    public RequestUpdateHelper() {
        this.mListeners = new LinkedList<>();
    }

    public static final RequestUpdateHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public boolean isRequestUpdateEnable() {
        return !BaseBuildUtil.isInternational() && CloudControlManager.getInstance().queryFeatureStatus("request-update").equals(FeatureProfile.Status.ENABLE);
    }

    public void requestUpdate(Context context, final int i) {
        LinkedList<OnRequestUpdateFinishListener> linkedList = this.mListeners;
        if (linkedList == null || linkedList.size() == 0) {
            return;
        }
        this.mUpdateListener = new XiaomiUpdateListener() { // from class: com.miui.gallery.util.RequestUpdateHelper.1
            @Override // com.market.sdk.XiaomiUpdateListener
            public void onUpdateReturned(int i2, UpdateResponse updateResponse) {
                if (i2 == 0) {
                    RequestUpdateHelper.this.createAndCallbackUpdateDialog(updateResponse, i);
                    return;
                }
                Iterator it = RequestUpdateHelper.this.mListeners.iterator();
                while (it.hasNext()) {
                    ((OnRequestUpdateFinishListener) it.next()).onFailure(i2);
                }
            }
        };
        AppGlobal.setContext(GalleryApp.sGetAndroidContext());
        XiaomiUpdateAgent.setUpdateAutoPopup(false);
        XiaomiUpdateAgent.setUpdateListener(this.mUpdateListener);
        XiaomiUpdateAgent.update(context, false);
    }

    public final void createAndCallbackUpdateDialog(UpdateResponse updateResponse, int i) {
        if (needCreateUpdateDialog(updateResponse, i)) {
            createUpdateDialog(updateResponse, i);
        }
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    public final boolean needCreateUpdateDialog(UpdateResponse updateResponse, int i) {
        if (i == 1) {
            if (GalleryPreferences.RequestUpdatePref.isIgnoreUpdate() && updateResponse.versionCode == GalleryPreferences.RequestUpdatePref.getNewestVersionCode()) {
                Iterator<OnRequestUpdateFinishListener> it = this.mListeners.iterator();
                while (it.hasNext()) {
                    it.next().onFailure(6);
                }
                return false;
            } else if (GalleryPreferences.RequestUpdatePref.isDelayUpdate() && updateResponse.versionCode == GalleryPreferences.RequestUpdatePref.getNewestVersionCode() && DateUtils.getDaysBetween(GalleryPreferences.RequestUpdatePref.getLastDelayDate(), System.currentTimeMillis()) < 7) {
                Iterator<OnRequestUpdateFinishListener> it2 = this.mListeners.iterator();
                while (it2.hasNext()) {
                    it2.next().onFailure(6);
                }
                return false;
            } else {
                switch (getUpdateDialogMode(updateResponse.versionName)) {
                    case 20:
                        Iterator<OnRequestUpdateFinishListener> it3 = this.mListeners.iterator();
                        while (it3.hasNext()) {
                            it3.next().onRedDotShow();
                        }
                        return false;
                    case 22:
                        this.mIsForceUpdate = true;
                        break;
                    case 23:
                        Iterator<OnRequestUpdateFinishListener> it4 = this.mListeners.iterator();
                        while (it4.hasNext()) {
                            it4.next().onNotUpdate();
                        }
                        return false;
                    case 24:
                        return false;
                }
            }
        }
        return true;
    }

    public final void createUpdateDialog(UpdateResponse updateResponse, int i) {
        Bundle bundle = new Bundle();
        bundle.putBoolean("key_is_force_update", this.mIsForceUpdate);
        Bundle bundle2 = new Bundle();
        bundle2.putString("key_update_response_version_name", updateResponse.versionName);
        bundle2.putInt("key_update_response_version_code", updateResponse.versionCode);
        bundle2.putString("key_update_response_update_log", updateResponse.updateLog);
        bundle2.putLong("key_update_response_apk_size", updateResponse.apkSize);
        bundle2.putLong("key_update_response_diff_size", updateResponse.diffSize);
        bundle.putBundle("key_update_response", bundle2);
        UpdateDialogFragment newInstance = UpdateDialogFragment.newInstance(bundle);
        newInstance.setCancelable(false);
        Iterator<OnRequestUpdateFinishListener> it = this.mListeners.iterator();
        while (it.hasNext()) {
            it.next().onDialogCreate(this.mIsForceUpdate ? 8 : 7, i, newInstance);
        }
    }

    public final int getUpdateDialogMode(String str) {
        if (TextUtils.isEmpty(str)) {
            return 24;
        }
        if (str.endsWith("-U")) {
            return 22;
        }
        if (str.endsWith("-N")) {
            return 23;
        }
        if (str.matches("\\d+(\\.\\d+){0,3}-U\\d+(\\.\\d+){0,3}")) {
            String[] split = "3.4.7.3".split("-");
            String[] split2 = str.split("-U");
            String str2 = split[0];
            if (str2.matches(split2[1] + "(\\.\\d+){0,3}")) {
                return 22;
            }
        }
        String[] split3 = str.split("\\.");
        String[] split4 = "3.4.7.3".split("-")[0].split("\\.");
        if (split4.length < 3) {
            split4 = new String[]{split4[0], split4[1], "0"};
        }
        for (int i = 0; i < 3; i++) {
            if (!split3[i].equals(split4[i])) {
                return 21;
            }
        }
        return 20;
    }

    public void release() {
        if (this.mUpdateListener != null) {
            this.mUpdateListener = null;
        }
    }

    public void registerOnRequestUpdateFinishListener(OnRequestUpdateFinishListener onRequestUpdateFinishListener) {
        if (onRequestUpdateFinishListener == null || this.mListeners.contains(onRequestUpdateFinishListener)) {
            return;
        }
        this.mListeners.add(onRequestUpdateFinishListener);
    }

    public void unregisterOnRequestUpdateFinishListener(OnRequestUpdateFinishListener onRequestUpdateFinishListener) {
        if (onRequestUpdateFinishListener != null) {
            this.mListeners.remove(onRequestUpdateFinishListener);
        }
    }
}
