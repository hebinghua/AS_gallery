package com.miui.gallery.projection;

import android.app.Activity;
import android.text.TextUtils;
import com.milink.api.v1.type.DeviceType;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.projection.DeviceListController;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes2.dex */
public class ConnectControllerImplV1 extends ConnectController implements DeviceListController.OnItemClickListener {
    public HashMap<String, String> mFoundDevices = new HashMap<>();
    public DeviceListController mWidget;

    public ConnectControllerImplV1() {
        DeviceListController deviceListController = new DeviceListController();
        this.mWidget = deviceListController;
        deviceListController.setOnItemClickListener(this);
    }

    @Override // com.miui.gallery.projection.ConnectController
    public void attachActivity(Activity activity) {
        DeviceListController deviceListController = this.mWidget;
        if (deviceListController != null) {
            deviceListController.attachActivity(new WeakReference<>(activity));
        }
    }

    @Override // com.miui.gallery.projection.ConnectController
    public void detachActivity(Activity activity) {
        DeviceListController deviceListController = this.mWidget;
        if (deviceListController != null) {
            deviceListController.detachActivity(new WeakReference<>(activity));
        }
    }

    @Override // com.miui.gallery.projection.ConnectController
    public boolean chooseDevice() {
        this.mWidget.show();
        return true;
    }

    @Override // com.miui.gallery.projection.ConnectController
    public void doOnConnectedFail() {
        super.doOnConnectedFail();
        this.mWidget.removeActive();
    }

    @Override // com.miui.gallery.projection.ConnectController
    public void doOnOpen() {
        HashMap<String, String> hashMap = this.mFoundDevices;
        if (hashMap != null) {
            for (String str : hashMap.keySet()) {
                this.mWidget.addNewDevice(str, false);
            }
        }
    }

    @Override // com.miui.gallery.projection.ConnectController
    public void doOnDeviceFound(String str, String str2, DeviceType deviceType) {
        if (deviceType != DeviceType.TV) {
            return;
        }
        this.mFoundDevices.put(str2, str);
        DefaultLogger.d("project_ConnectControllerV1", "onDevicesAdded %s", str2);
        this.mWidget.addNewDevice(str2, TextUtils.equals(str2, getConnectedDevice()));
    }

    @Override // com.miui.gallery.projection.ConnectController
    public void doOnDeviceLost(String str) {
        String str2;
        Iterator<String> it = this.mFoundDevices.keySet().iterator();
        while (true) {
            if (!it.hasNext()) {
                str2 = null;
                break;
            }
            str2 = it.next();
            if (this.mFoundDevices.get(str2).equals(str)) {
                break;
            }
        }
        if (str2 != null) {
            this.mFoundDevices.remove(str2);
            DefaultLogger.d("project_ConnectControllerV1", "onDeviceRemoved %s", str2);
            if (!this.mWidget.removeDevice(str2)) {
                return;
            }
            ToastUtils.makeText(GalleryApp.sGetAndroidContext(), (int) R.string.projection_device_connection_failed);
            closeService();
        }
    }

    @Override // com.miui.gallery.projection.DeviceListController.OnItemClickListener
    public void onItemClicked(String str) {
        DefaultLogger.d("project_ConnectControllerV1", "device %s selected", str);
        setToConnectDevice(str, this.mFoundDevices.get(str));
        this.mWidget.dismiss();
    }
}
