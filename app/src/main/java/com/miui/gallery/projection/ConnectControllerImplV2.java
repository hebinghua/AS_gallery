package com.miui.gallery.projection;

import android.text.TextUtils;
import com.milink.api.v1.MiLinkClientScanListCallback;
import com.milink.api.v1.type.DeviceType;
import com.milink.api.v1.type.ReturnCode;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/* loaded from: classes2.dex */
public class ConnectControllerImplV2 extends ConnectController implements MiLinkClientScanListCallback {
    public AtomicBoolean mChooseTillOpen = new AtomicBoolean(false);

    @Override // com.miui.gallery.projection.ConnectController
    public void doOnDeviceFound(String str, String str2, DeviceType deviceType) {
    }

    @Override // com.milink.api.v1.MiLinkClientScanListCallback
    public void onConnectFail(String str, String str2) {
    }

    @Override // com.milink.api.v1.MiLinkClientScanListCallback
    public void onConnectSuccess(String str, String str2) {
    }

    @Override // com.miui.gallery.projection.ConnectController
    public boolean chooseDevice() {
        ReturnCode showScanList = this.mPhotoManager.showScanList(this, 2);
        if (showScanList == ReturnCode.OK) {
            return true;
        }
        if (showScanList != ReturnCode.NotConnected) {
            return false;
        }
        this.mChooseTillOpen.set(true);
        return false;
    }

    @Override // com.milink.api.v1.MiLinkClientScanListCallback
    public void onSelectDevice(String str, String str2, String str3) {
        setToConnectDevice(str2, str);
    }

    @Override // com.miui.gallery.projection.ConnectController
    public void doOnOpen() {
        if (this.mChooseTillOpen.get()) {
            this.mChooseTillOpen.set(false);
            chooseDevice();
        }
    }

    @Override // com.miui.gallery.projection.ConnectController
    public void doOnDeviceLost(String str) {
        Map.Entry<String, String> entry = this.mConnectedDevice;
        if (entry == null || !TextUtils.equals(entry.getValue(), str)) {
            return;
        }
        DefaultLogger.d("project_ConnectControllerV2", "onDeviceRemoved %s", this.mConnectedDevice.getValue());
        ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.projection_device_connection_failed);
        closeService();
    }
}
