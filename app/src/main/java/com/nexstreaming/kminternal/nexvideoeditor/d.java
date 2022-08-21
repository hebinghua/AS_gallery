package com.nexstreaming.kminternal.nexvideoeditor;

import android.media.CamcorderProfile;
import com.nexstreaming.kminternal.kinemaster.config.NexEditorDeviceProfile;
import java.util.Vector;

/* compiled from: NexVisualClipChecker.java */
/* loaded from: classes3.dex */
public class d {
    private int a;
    private int b;
    private int c;
    private int d;
    private int e;
    private int f;
    private int g;
    private int h;
    private int i;
    private boolean j;
    private int k;
    private int l;
    private int m;
    private boolean n;
    private int o;
    private int p;
    private int q;
    private int r;
    private int s;
    private int t;
    private int u;
    private int v;
    private int w;
    private Vector<CamcorderProfile> x;

    private static int a(int i, int i2) {
        return i == 0 ? i2 : (i2 != 0 && i >= i2) ? i2 : i;
    }

    public d(NexEditor nexEditor) {
        this.a = 0;
        this.b = 0;
        this.c = 0;
        this.d = 0;
        this.e = 0;
        this.f = 0;
        this.g = 0;
        this.h = 0;
        this.i = 0;
        this.j = false;
        this.k = 0;
        this.l = 0;
        this.m = 0;
        this.n = false;
        this.o = 0;
        this.p = 0;
        this.q = 0;
        this.r = 55;
        this.s = 0;
        this.t = 0;
        this.u = 0;
        this.v = 0;
        this.w = 0;
        if (nexEditor == null) {
            return;
        }
        NexEditorDeviceProfile deviceProfile = NexEditorDeviceProfile.getDeviceProfile();
        int maxCamcorderProfileSizeForUnknownDevice = deviceProfile.getMaxCamcorderProfileSizeForUnknownDevice();
        maxCamcorderProfileSizeForUnknownDevice = maxCamcorderProfileSizeForUnknownDevice <= 0 ? Integer.MAX_VALUE : maxCamcorderProfileSizeForUnknownDevice;
        this.a = deviceProfile.getIntProperty("Device_Support_BaselineMaxLevel", 0);
        this.b = deviceProfile.getIntProperty("Device_Support_MainMaxLevel", 0);
        this.c = deviceProfile.getIntProperty("Device_Support_HighMaxLevel", 0);
        this.d = deviceProfile.getMCHWAVCDecBaselineSize();
        this.e = deviceProfile.getMCHWAVCDecMainSize();
        this.f = deviceProfile.getMCHWAVCDecHighSize();
        if (deviceProfile.isUnknownDevice()) {
            this.g = a(maxCamcorderProfileSizeForUnknownDevice, nexEditor.b("MCHWAVCEncBaselineLevelSize", 0));
            this.h = Math.min(maxCamcorderProfileSizeForUnknownDevice, nexEditor.b("MCHWAVCEncMainLevelSize", 0));
            this.i = Math.min(maxCamcorderProfileSizeForUnknownDevice, nexEditor.b("MCHWAVCEncHighLevelSize", 0));
        } else {
            this.g = nexEditor.b("MCHWAVCEncBaselineLevelSize", 0);
            this.h = nexEditor.b("MCHWAVCEncMainLevelSize", 0);
            this.i = nexEditor.b("MCHWAVCEncHighLevelSize", 0);
        }
        boolean a = nexEditor.a("canUseMCSoftwareCodec", false);
        this.j = a;
        if (a) {
            this.k = deviceProfile.getMCSWAVCDecBaselineSize();
            this.l = deviceProfile.getMCSWAVCDecMainSize();
            this.m = deviceProfile.getMCSWAVCDecHighSize();
        }
        boolean a2 = nexEditor.a("canUseSoftwareCodec", false);
        this.n = a2;
        if (a2) {
            this.o = deviceProfile.getNXSWAVCDecBaselineSize();
            this.p = deviceProfile.getNXSWAVCDecMainSize();
            this.q = deviceProfile.getNXSWAVCDecHighSize();
        }
        this.r = deviceProfile.getMaxSupportedFPS();
        this.u = deviceProfile.getMaxSupportedVideoBitrate(0);
        this.v = deviceProfile.getMaxSupportedAudioSamplingRate(0);
        this.w = deviceProfile.getMaxSupportedAudioChannels(0);
        if (deviceProfile.isUnknownDevice()) {
            int i = this.d;
            this.s = i;
            this.t = i;
        } else {
            this.s = deviceProfile.getMaxImportSize(true);
            this.t = deviceProfile.getMaxImportSize(false);
        }
        this.x = new Vector<>();
        int[] iArr = {6, 5, 4, 3, 7};
        for (int i2 = 0; i2 < 5; i2++) {
            if (CamcorderProfile.hasProfile(iArr[i2])) {
                this.x.add(CamcorderProfile.get(iArr[i2]));
            }
        }
    }

    public int a(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        int i9;
        int i10;
        int i11;
        int i12 = this.v;
        if (i12 <= 0 || i12 >= i7) {
            boolean z = this.n;
            int i13 = z ? this.s : this.t;
            if (i == 255) {
                int i14 = i3 * i4;
                if (i14 > i13) {
                    return i14 > (z ? this.o : this.d) ? 4 : 1;
                }
                return i5 > this.r ? 2 : 0;
            }
            if (z) {
                i9 = this.o;
                i10 = this.p;
                i11 = this.q;
            } else {
                i9 = this.d;
                i10 = this.e;
                i11 = this.f;
            }
            if (i == 66) {
                int i15 = this.a;
                if (i15 != 0 && i15 < i2) {
                    return 5;
                }
            } else if (i != 77) {
                if (i != 100 || i11 == 0) {
                    return 3;
                }
                int i16 = this.c;
                if (i16 != 0 && i16 < i2) {
                    return 5;
                }
                i9 = i11;
            } else if (i10 == 0) {
                return 3;
            } else {
                int i17 = this.b;
                if (i17 != 0 && i17 < i2) {
                    return 5;
                }
                i9 = i10;
            }
            int i18 = i3 * i4;
            if (i18 > i9) {
                if (!this.j) {
                    return 4;
                }
                return (i != 66 || i18 > this.k) ? 3 : 1;
            } else if (i18 > i13) {
                return 1;
            } else {
                return i5 > this.r ? 2 : 0;
            }
        }
        return 6;
    }

    public int a() {
        return this.g;
    }
}
