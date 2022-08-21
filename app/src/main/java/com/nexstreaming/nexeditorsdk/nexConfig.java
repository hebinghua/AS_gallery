package com.nexstreaming.nexeditorsdk;

import android.os.Environment;
import com.nexstreaming.nexeditorsdk.exception.InvalidRangeException;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes3.dex */
public final class nexConfig {
    public static final int kDeviceMaxGamma = 10;
    public static final int kDeviceMaxLightLevel = 9;
    public static final int kForceDirectExport = 6;
    public static final int kHardwareCodecMemSize = 1;
    public static final int kHardwareDecMaxCount = 2;
    public static final int kMaxResolution = 5;
    public static final int kMaxSupportedFPS = 3;
    public static final int kNativeLogLevel = 7;
    public static final int kSetUserConfig = 0;
    public static final int kSupportMPEGV4 = 4;
    public static final int kUseTimeChecker = 8;
    public static int[] sConfigProperty = {0, 8912896, 4, 120, 0, 8912896, 0, 2, 1, 550, 2400, 0};

    public static void set(int i, int i2, int i3, boolean z, int i4) {
        int[] iArr = sConfigProperty;
        iArr[0] = 1;
        iArr[1] = i;
        iArr[2] = i2;
        iArr[3] = i3;
        iArr[4] = z ? 1 : 0;
        iArr[5] = i4;
    }

    public static void setCapability(InputStream inputStream) {
        BufferedInputStream bufferedInputStream;
        com.nexstreaming.app.common.util.a aVar = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(new File(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "CodecCapacity"), "CapabilityResult.txt")));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            bufferedInputStream = null;
        }
        try {
            aVar = new com.nexstreaming.app.common.util.g().a(bufferedInputStream);
        } catch (IOException e2) {
            e2.printStackTrace();
        }
        int f = aVar.e() > aVar.f() ? aVar.f() : aVar.e();
        int[] iArr = sConfigProperty;
        iArr[0] = 1;
        iArr[1] = aVar.a();
        int[] iArr2 = sConfigProperty;
        iArr2[2] = f;
        iArr2[3] = aVar.b();
        sConfigProperty[4] = aVar.d() ? 1 : 0;
        sConfigProperty[5] = aVar.c();
    }

    public static int getProperty(int i) {
        int[] iArr = sConfigProperty;
        if (iArr.length <= i) {
            throw new InvalidRangeException(0, sConfigProperty.length - 1, i);
        }
        return iArr[i];
    }

    public static boolean setProperty(int i, int i2) {
        int[] iArr = sConfigProperty;
        if (iArr.length <= i) {
            return false;
        }
        iArr[0] = 1;
        iArr[i] = i2;
        return true;
    }
}
