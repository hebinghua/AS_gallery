package com.nexstreaming.kminternal.kinemaster.mediainfo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.util.Log;
import ch.qos.logback.core.net.SyslogConstants;
import com.nexstreaming.app.common.task.Task;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/* compiled from: ThumbnailConversionTask.java */
/* loaded from: classes3.dex */
abstract class f extends AsyncTask<Integer, Integer, Task.TaskError> {
    private File a;
    private File b;
    private File c;
    private File d;
    private long e;
    private Bitmap f;
    private Bitmap g;
    private Bitmap h;
    private int[] i;

    public abstract void a();

    public abstract void a(Task.TaskError taskError);

    public f(File file, File file2, File file3, File file4) {
        this.a = file2;
        this.d = file;
        this.b = file3;
        this.c = file4;
    }

    @Override // android.os.AsyncTask
    /* renamed from: a */
    public Task.TaskError doInBackground(Integer... numArr) {
        if (!this.d.exists()) {
            Log.d("KMMediaInfo_ThumbConv", "doInBackground : raw thumbnail file not found");
            return ThumbnailError.RawFileNotFound;
        }
        this.e = this.d.length();
        Log.d("KMMediaInfo_ThumbConv", "doInBackground : mThumbFileSize=" + this.e);
        if (this.e < 8) {
            Log.d("KMMediaInfo_ThumbConv", "doInBackground : raw thumbnail file too small");
            return ThumbnailError.RawFileTooSmall;
        }
        try {
            Task.TaskError b = b();
            if (b != null) {
                Log.d("KMMediaInfo_ThumbConv", "doInBackground : raw thumbnail file parse error");
                return b;
            }
            c();
            Log.d("KMMediaInfo_ThumbConv", "doInBackground : out");
            return null;
        } catch (IOException e) {
            Log.d("KMMediaInfo_ThumbConv", "doInBackground : EXCEPTION", e);
            return Task.makeTaskError(e);
        }
    }

    private Task.TaskError a(InputStream inputStream) throws IOException {
        return g.a(inputStream, this.e, 50, new d() { // from class: com.nexstreaming.kminternal.kinemaster.mediainfo.f.1
            public int a;
            public int b;
            public Bitmap c;
            public Canvas d;
            public Rect e;
            public Paint f;

            @Override // com.nexstreaming.kminternal.kinemaster.mediainfo.d
            public void a(Bitmap bitmap, int i, int i2, int i3) {
                if (i == 0) {
                    this.a = 90;
                    int i4 = i2 * SyslogConstants.LOG_LOCAL4;
                    this.b = i4;
                    Bitmap createBitmap = Bitmap.createBitmap(i4, 90, Bitmap.Config.RGB_565);
                    this.c = createBitmap;
                    f.this.f = createBitmap;
                    this.d = new Canvas(this.c);
                    this.e = new Rect(0, 0, SyslogConstants.LOG_LOCAL4, 90);
                    Paint paint = new Paint();
                    this.f = paint;
                    paint.setFilterBitmap(true);
                    f.this.i = new int[i2];
                    Log.d("KMMediaInfo_ThumbConv", "processRawFile : totalCount=" + i2);
                }
                f.this.i[i] = i3;
                if (bitmap == null) {
                    this.e.offset(SyslogConstants.LOG_LOCAL4, 0);
                    return;
                }
                if (i == 0) {
                    Log.d("KMMediaInfo_ThumbConv", "Make large thumnail at i==0");
                    Bitmap createBitmap2 = Bitmap.createBitmap(640, 360, Bitmap.Config.RGB_565);
                    new Canvas(createBitmap2).drawBitmap(bitmap, (Rect) null, new Rect(0, 0, 640, 360), this.f);
                    f.this.g = createBitmap2;
                } else if (i == i2 - 1) {
                    Log.d("KMMediaInfo_ThumbConv", "Make large end thumnail at i==" + i);
                    Bitmap createBitmap3 = Bitmap.createBitmap(640, 360, Bitmap.Config.RGB_565);
                    new Canvas(createBitmap3).drawBitmap(bitmap, (Rect) null, new Rect(0, 0, 640, 360), this.f);
                    f.this.h = createBitmap3;
                }
                this.d.save();
                this.d.scale(-1.0f, -1.0f, 80.0f, 45.0f);
                this.d.drawBitmap(bitmap, (Rect) null, this.e, this.f);
                this.d.restore();
                this.d.translate(160.0f, 0.0f);
            }
        });
    }

    private Task.TaskError b() throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(this.d));
        try {
            return a(bufferedInputStream);
        } finally {
            bufferedInputStream.close();
        }
    }

    @Override // android.os.AsyncTask
    /* renamed from: b */
    public final void onPostExecute(Task.TaskError taskError) {
        if (taskError == null) {
            a();
        } else {
            a(taskError);
        }
    }

    private void c() throws IOException {
        a(null, this.g, this.b);
        Bitmap bitmap = this.h;
        if (bitmap == null) {
            bitmap = this.g;
        }
        a(null, bitmap, this.c);
        a(this.i, this.f, this.a);
    }

    private void a(int[] iArr, Bitmap bitmap, File file) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("writeBitmapToFile(");
        sb.append(file);
        sb.append(") : ");
        sb.append(iArr == null ? "no index" : "width index");
        Log.d("KMMediaInfo_ThumbConv", sb.toString());
        DataOutputStream dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
        if (iArr != null) {
            try {
                dataOutputStream.writeInt(SyslogConstants.LOG_LOCAL4);
                dataOutputStream.writeInt(90);
                dataOutputStream.writeInt(iArr.length);
                for (int i : iArr) {
                    dataOutputStream.writeInt(i);
                }
            } finally {
                dataOutputStream.close();
                file.setReadable(true);
            }
        }
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, dataOutputStream);
    }
}
