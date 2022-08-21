package com.baidu.vi;

/* loaded from: classes.dex */
class b extends Thread {
    public final /* synthetic */ AudioRecorder a;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public b(AudioRecorder audioRecorder, String str) {
        super(str);
        this.a = audioRecorder;
    }

    /* JADX WARN: Incorrect condition in loop: B:4:0x0016 */
    @Override // java.lang.Thread, java.lang.Runnable
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void run() {
        /*
            r4 = this;
            r0 = -19
            android.os.Process.setThreadPriority(r0)
            com.baidu.vi.AudioRecorder r0 = r4.a
            android.media.AudioRecord r0 = com.baidu.vi.AudioRecorder.b(r0)
            r0.startRecording()
            r0 = 0
            r1 = r0
        L10:
            com.baidu.vi.AudioRecorder r2 = r4.a
            boolean r2 = com.baidu.vi.AudioRecorder.a(r2)
            if (r2 == 0) goto L50
            com.baidu.vi.AudioRecorder r2 = r4.a
            int r2 = com.baidu.vi.AudioRecorder.c(r2)
            byte[] r2 = new byte[r2]
            com.baidu.vi.AudioRecorder r3 = r4.a
            android.media.AudioRecord r3 = com.baidu.vi.AudioRecorder.b(r3)
            if (r3 == 0) goto L38
            com.baidu.vi.AudioRecorder r1 = r4.a
            android.media.AudioRecord r1 = com.baidu.vi.AudioRecorder.b(r1)
            com.baidu.vi.AudioRecorder r3 = r4.a
            int r3 = com.baidu.vi.AudioRecorder.c(r3)
            int r1 = r1.read(r2, r0, r3)
        L38:
            r3 = -3
            if (r1 == r3) goto L4a
            r3 = -2
            if (r1 == r3) goto L4a
            r3 = -1
            if (r1 == r3) goto L4a
            if (r1 != 0) goto L44
            goto L4a
        L44:
            com.baidu.vi.AudioRecorder r3 = r4.a
            com.baidu.vi.AudioRecorder.a(r3, r2, r1)
            goto L10
        L4a:
            com.baidu.vi.AudioRecorder r2 = r4.a
            com.baidu.vi.AudioRecorder.d(r2)
            goto L10
        L50:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.baidu.vi.b.run():void");
    }
}
