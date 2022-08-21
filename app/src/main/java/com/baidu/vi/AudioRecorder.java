package com.baidu.vi;

import android.media.AudioRecord;
import android.os.Handler;

/* loaded from: classes.dex */
public class AudioRecorder {
    private static Handler j = new com.baidu.vi.a();
    private volatile AudioRecord a;
    private int b;
    private int c;
    private int d;
    private boolean e;
    private int f;
    private int g;
    private volatile boolean h = false;
    private Object i = new Object();
    private Thread k = new b(this, AudioRecorder.class.getSimpleName() + "-Record");

    /* loaded from: classes.dex */
    public class a {
        public AudioRecorder a;
        public byte[] b;
        public int c;

        public a(AudioRecorder audioRecorder, byte[] bArr, int i) {
            this.a = audioRecorder;
            this.b = bArr;
            this.c = i;
        }
    }

    public AudioRecorder(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        boolean z = true;
        this.e = true;
        if (i3 == 8) {
            this.d = 3;
        } else {
            this.d = 2;
        }
        if (i4 == 2) {
            this.c = 3;
        } else {
            this.c = 2;
        }
        this.e = i7 != 1 ? false : z;
        this.b = i2;
        this.g = i5;
        this.f = i6;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a() {
        if (this.e) {
            a aVar = new a(this, null, 0);
            Handler handler = j;
            handler.sendMessage(handler.obtainMessage(2, aVar));
        } else if (!this.h) {
        } else {
            onReadError();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void a(byte[] bArr, int i) {
        if (this.e) {
            a aVar = new a(this, bArr, i);
            Handler handler = j;
            handler.sendMessage(handler.obtainMessage(1, aVar));
        } else if (!this.h) {
        } else {
            onReadData(bArr, i);
        }
    }

    public native void onReadData(byte[] bArr, int i);

    public native void onReadError();
}
