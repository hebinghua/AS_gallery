package com.nexstreaming.checkcaps;

import android.annotation.TargetApi;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaCrypto;
import android.media.MediaFormat;
import android.view.Surface;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

/* compiled from: Checker.java */
/* loaded from: classes3.dex */
public class a {
    private static String a = "CapChecker";
    private boolean b;
    private InterfaceC0103a d;
    private com.nexstreaming.checkcaps.b c = null;
    private MediaCodec e = null;
    private MediaCodec.BufferInfo f = null;
    private MediaFormat g = null;
    private ByteBuffer[] h = null;

    /* compiled from: Checker.java */
    /* renamed from: com.nexstreaming.checkcaps.a$a  reason: collision with other inner class name */
    /* loaded from: classes3.dex */
    public interface InterfaceC0103a {
        void a(a aVar, int i);

        void a(a aVar, String str);
    }

    public void a(InterfaceC0103a interfaceC0103a) {
        this.d = interfaceC0103a;
    }

    private static int a(String str) {
        int codecCount = MediaCodecList.getCodecCount();
        int i = 0;
        for (int i2 = 0; i2 < codecCount; i2++) {
            MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i2);
            if (codecInfoAt.isEncoder()) {
                String[] supportedTypes = codecInfoAt.getSupportedTypes();
                int i3 = 0;
                while (true) {
                    if (i3 >= supportedTypes.length) {
                        break;
                    } else if (supportedTypes[i3].equalsIgnoreCase(str)) {
                        i++;
                        break;
                    } else {
                        i3++;
                    }
                }
            }
        }
        return i;
    }

    private static MediaCodecInfo[] b(String str) {
        int a2 = a(str);
        MediaCodecInfo[] mediaCodecInfoArr = new MediaCodecInfo[a2];
        if (a2 == 0) {
            return mediaCodecInfoArr;
        }
        int codecCount = MediaCodecList.getCodecCount();
        int i = 0;
        for (int i2 = 0; i2 < codecCount; i2++) {
            MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(i2);
            if (codecInfoAt.isEncoder()) {
                String[] supportedTypes = codecInfoAt.getSupportedTypes();
                int i3 = 0;
                while (true) {
                    if (i3 >= supportedTypes.length) {
                        break;
                    } else if (supportedTypes[i3].equalsIgnoreCase(str)) {
                        mediaCodecInfoArr[i] = codecInfoAt;
                        i++;
                        break;
                    } else {
                        i3++;
                    }
                }
            }
        }
        return mediaCodecInfoArr;
    }

    @TargetApi(21)
    private MediaCodec a(MediaCodecInfo mediaCodecInfo, MediaFormat mediaFormat, AtomicReference<Surface> atomicReference) {
        boolean z;
        MediaCodec mediaCodec;
        boolean z2 = true;
        try {
            mediaCodec = MediaCodec.createByCodecName(mediaCodecInfo.getName());
            z = false;
        } catch (IOException e) {
            e.printStackTrace();
            z = true;
            mediaCodec = null;
        }
        if (z) {
            return null;
        }
        try {
            mediaCodec.configure(mediaFormat, (Surface) null, (MediaCrypto) null, 1);
            z2 = z;
        } catch (MediaCodec.CodecException unused) {
        } catch (IllegalArgumentException e2) {
            e = e2;
            e.printStackTrace();
        } catch (IllegalStateException e3) {
            e = e3;
            e.printStackTrace();
        }
        if (z2) {
            return null;
        }
        atomicReference.set(mediaCodec.createInputSurface());
        mediaCodec.start();
        return mediaCodec;
    }

    public a(boolean z) {
        this.b = false;
        this.b = z;
    }

    /* compiled from: Checker.java */
    /* loaded from: classes3.dex */
    public static class b extends Thread {
        private a a;
        private AbstractMap<String, Object> b;

        private b(a aVar, AbstractMap<String, Object> abstractMap) {
            this.a = null;
            this.b = null;
            this.a = aVar;
            this.b = abstractMap;
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            AbstractMap<String, Object> abstractMap = this.b;
            if (abstractMap != null && ((Integer) abstractMap.get("command")).intValue() == 1) {
                this.a.d.a(this.a, this.a.b(((Integer) this.b.get(nexExportFormat.TAG_FORMAT_WIDTH)).intValue(), ((Integer) this.b.get(nexExportFormat.TAG_FORMAT_HEIGHT)).intValue()));
            }
        }

        public static void a(a aVar, AbstractMap<String, Object> abstractMap) {
            new b(aVar, abstractMap).start();
        }
    }

    public void a(int i, int i2) {
        HashMap hashMap = new HashMap();
        hashMap.put("command", 1);
        hashMap.put(nexExportFormat.TAG_FORMAT_WIDTH, Integer.valueOf(i));
        hashMap.put(nexExportFormat.TAG_FORMAT_HEIGHT, Integer.valueOf(i2));
        b.a(this, hashMap);
    }

    /* JADX INFO: Access modifiers changed from: private */
    @TargetApi(18)
    public int b(int i, int i2) {
        MediaCodecInfo[] b2 = b("video/avc");
        InterfaceC0103a interfaceC0103a = this.d;
        if (interfaceC0103a != null) {
            interfaceC0103a.a(this, "The count of 'video/avc' Encoder : " + b2.length);
            for (int i3 = 0; i3 < b2.length; i3++) {
                InterfaceC0103a interfaceC0103a2 = this.d;
                interfaceC0103a2.a(this, i3 + " th encoder's name is '" + b2[i3].getName() + "'");
            }
        }
        MediaFormat createVideoFormat = MediaFormat.createVideoFormat("video/avc", i, i2);
        createVideoFormat.setInteger("color-format", 2130708361);
        createVideoFormat.setInteger("bitrate", 3000000);
        createVideoFormat.setInteger("frame-rate", 30);
        createVideoFormat.setInteger("i-frame-interval", 2);
        InterfaceC0103a interfaceC0103a3 = this.d;
        if (interfaceC0103a3 != null) {
            interfaceC0103a3.a(this, "Video Format of Encoder : " + createVideoFormat);
        }
        AtomicReference<Surface> atomicReference = new AtomicReference<>();
        MediaCodec a2 = a(b2[0], createVideoFormat, atomicReference);
        this.e = a2;
        if (a2 != null) {
            com.nexstreaming.checkcaps.b bVar = new com.nexstreaming.checkcaps.b(atomicReference.get());
            this.c = bVar;
            bVar.b();
            this.e.stop();
            this.e.release();
            this.e = null;
            this.c.a();
            this.c = null;
            return 0;
        }
        return -1;
    }
}
