package com.nexstreaming.kminternal.kinemaster.codeccolorformat;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaFormat;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import com.nexstreaming.app.common.task.ResultTask;
import com.nexstreaming.app.common.task.Task;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Vector;

/* loaded from: classes3.dex */
public class ColorFormatChecker {
    private static ResultTask<ColorFormat> a;
    private c b;
    private b c;
    private boolean d;
    private boolean e;
    private MediaFormat f;
    private Vector<byte[]> g;
    private Vector<byte[]> h;

    /* loaded from: classes3.dex */
    public enum ColorFormat {
        UNKNOWN,
        NV12,
        NV21
    }

    public static ResultTask<ColorFormat> a(Context context) {
        ColorFormat[] values;
        if (a == null) {
            a = new ResultTask<>();
            final SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            if (defaultSharedPreferences.contains("km_detected_codec_color_format")) {
                String string = defaultSharedPreferences.getString("km_detected_codec_color_format", null);
                for (ColorFormat colorFormat : ColorFormat.values()) {
                    if (colorFormat.name().equals(string)) {
                        a.setResult(colorFormat);
                        return a;
                    }
                }
            }
            new AsyncTask<Void, Void, ColorFormat>() { // from class: com.nexstreaming.kminternal.kinemaster.codeccolorformat.ColorFormatChecker.1
                public Task.TaskError a = null;

                @Override // android.os.AsyncTask
                /* renamed from: a */
                public ColorFormat doInBackground(Void... voidArr) {
                    try {
                        return new ColorFormatChecker().b();
                    } catch (Exception e) {
                        this.a = Task.makeTaskError("Error getting color format", e);
                        return null;
                    }
                }

                @Override // android.os.AsyncTask
                /* renamed from: a */
                public void onPostExecute(ColorFormat colorFormat2) {
                    if (colorFormat2 == null) {
                        ColorFormatChecker.a.sendFailure(this.a);
                    } else {
                        defaultSharedPreferences.edit().putString("km_detected_codec_color_format", colorFormat2.name()).commit();
                        ColorFormatChecker.a.sendResult(colorFormat2);
                    }
                    super.onPostExecute(colorFormat2);
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, null);
        }
        return a;
    }

    private ColorFormatChecker() {
        this.d = false;
        this.e = false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public ColorFormat b() throws IOException {
        ColorFormat colorFormat = ColorFormat.UNKNOWN;
        this.g = new Vector<>();
        this.h = new Vector<>();
        c cVar = new c(1280, 720);
        this.b = cVar;
        cVar.a(new d() { // from class: com.nexstreaming.kminternal.kinemaster.codeccolorformat.ColorFormatChecker.2
            @Override // com.nexstreaming.kminternal.kinemaster.codeccolorformat.d
            public boolean a(int i, long j) {
                return true;
            }

            @Override // com.nexstreaming.kminternal.kinemaster.codeccolorformat.d
            public boolean a(byte[] bArr, int i, int i2, long j) {
                ColorFormatChecker.this.g.add(bArr);
                Log.d("ColorFormatChecker", String.format("Encoder One Frame Received(Type:%d, Length:%d, Time:%d)", Integer.valueOf(i), Integer.valueOf(i2), Long.valueOf(j)));
                return true;
            }

            @Override // com.nexstreaming.kminternal.kinemaster.codeccolorformat.d
            public boolean a(byte[] bArr, byte[] bArr2) {
                if (ColorFormatChecker.this.f == null) {
                    ColorFormatChecker.this.f = MediaFormat.createVideoFormat("video/avc", 1280, 720);
                }
                ByteBuffer wrap = ByteBuffer.wrap(bArr);
                ByteBuffer wrap2 = ByteBuffer.wrap(bArr2);
                ColorFormatChecker.this.f.setByteBuffer("csd-0", wrap);
                ColorFormatChecker.this.f.setByteBuffer("csd-1", wrap2);
                Log.d("ColorFormatChecker", String.format("Encoder Frame config Received", new Object[0]));
                return true;
            }

            @Override // com.nexstreaming.kminternal.kinemaster.codeccolorformat.d
            public boolean a(int i) {
                Log.d("ColorFormatChecker", "Receive flag from Encoder : " + a.a(i));
                if (i == 4) {
                    ColorFormatChecker.this.d = true;
                }
                return true;
            }
        });
        byte[] bArr = new byte[1382400];
        Arrays.fill(bArr, 0, 921600, (byte) 45);
        for (int i = 0; i < 460800; i += 2) {
            int i2 = i + 921600;
            bArr[i2] = -44;
            bArr[i2 + 1] = -127;
        }
        long j = 0;
        long j2 = 0;
        for (int i3 = 0; i3 < 5; i3++) {
            this.b.a(bArr, j2);
            j2 += 30000;
        }
        while (!this.d) {
            this.b.a(null, j2);
        }
        try {
            this.b.a();
            this.b = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        MediaFormat mediaFormat = this.f;
        if (mediaFormat != null) {
            b bVar = new b(mediaFormat, null);
            this.c = bVar;
            bVar.a(new d() { // from class: com.nexstreaming.kminternal.kinemaster.codeccolorformat.ColorFormatChecker.3
                @Override // com.nexstreaming.kminternal.kinemaster.codeccolorformat.d
                public boolean a(int i4, long j3) {
                    return true;
                }

                @Override // com.nexstreaming.kminternal.kinemaster.codeccolorformat.d
                public boolean a(byte[] bArr2, byte[] bArr3) {
                    return true;
                }

                @Override // com.nexstreaming.kminternal.kinemaster.codeccolorformat.d
                public boolean a(byte[] bArr2, int i4, int i5, long j3) {
                    if (ColorFormatChecker.this.h.size() <= 0) {
                        ColorFormatChecker.this.h.add(bArr2);
                    }
                    Log.d("ColorFormatChecker", String.format("Decoder One Frame Received(Type:%d, Length:%d, Time:%d)", Integer.valueOf(i4), Integer.valueOf(i5), Long.valueOf(j3)));
                    return true;
                }

                @Override // com.nexstreaming.kminternal.kinemaster.codeccolorformat.d
                public boolean a(int i4) {
                    Log.d("ColorFormatChecker", "Receive flag from Decoder : " + a.a(i4));
                    if (i4 == 4) {
                        ColorFormatChecker.this.e = true;
                        Log.d("ColorFormatChecker", "Decoder EOS received");
                    }
                    return true;
                }
            });
            while (this.g.size() > 0) {
                byte[] bArr2 = this.g.get(0);
                if (bArr2 != null) {
                    this.c.a(bArr2, j);
                    this.g.remove(0);
                    j += 30000;
                }
            }
            while (!this.e) {
                this.c.a((ByteBuffer) null, j);
            }
            try {
                this.c.a();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
            if (this.h.size() <= 0) {
                return colorFormat;
            }
            int c = this.c.c();
            int d = this.c.d();
            byte[] bArr3 = this.h.get(0);
            if (bArr3 == null) {
                return colorFormat;
            }
            int i4 = c * d;
            if ((((Math.abs(bArr[921600] - bArr3[i4]) + Math.abs(bArr[921601] - bArr3[i4 + 1])) + Math.abs(bArr[921602] - bArr3[i4 + 2])) + Math.abs(bArr[921603] - bArr3[i4 + 3])) / 4 > 10) {
                return ColorFormat.NV21;
            }
            return ColorFormat.NV12;
        }
        return colorFormat;
    }
}
