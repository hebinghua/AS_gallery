package com.miui.mishare.app.model2;

import android.net.Uri;
import com.miui.mishare.RemoteDevice;
import java.util.List;
import java.util.Random;

/* loaded from: classes3.dex */
public class MiShareDevice {
    public String deviceId;
    public String deviceModelName;
    public String deviceName;
    public int deviceStatus = 1;
    public int deviceType;
    public List<Uri> files;
    public boolean isGlobalDevice;
    public boolean isUwbHit;
    public float progressPercent;
    public RemoteDevice remoteDevice;
    public boolean showProgress;
    public String taskId;
    public int uwbRank;
    public int vendorId;

    public static boolean checkGlobalDeviceUpdate(boolean z, boolean z2) {
        return z != z2;
    }

    public static boolean checkUwbHitUpdate(boolean z, boolean z2) {
        return z != z2;
    }

    public MiShareDevice(int i) {
        if (i == 2) {
            this.taskId = generatePCTaskId();
        } else {
            this.taskId = Long.toString(TokenGenerator.get());
        }
    }

    public boolean isPC() {
        RemoteDevice remoteDevice = this.remoteDevice;
        return remoteDevice != null && remoteDevice.isPC();
    }

    public boolean isPad() {
        try {
            RemoteDevice remoteDevice = this.remoteDevice;
            if (remoteDevice == null) {
                return false;
            }
            return remoteDevice.getExtras().getBoolean("isPad");
        } catch (Throwable unused) {
            return false;
        }
    }

    /* loaded from: classes3.dex */
    public static class TokenGenerator {
        public static final Random RANDOM = new Random();

        public static long get() {
            return RANDOM.nextLong();
        }
    }

    public static String generatePCTaskId() {
        return Integer.toString(TokenGenerator.RANDOM.nextInt(65532) + 2);
    }
}
