package com.nexstreaming.kminternal.kinemaster.mediainfo;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import com.nexstreaming.app.common.task.Task;
import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/* compiled from: ThumbnailParser.java */
/* loaded from: classes3.dex */
class g {
    private static int a(int i) {
        return ((i & 255) << 24) | (((-16777216) & i) >>> 24) | ((16711680 & i) >>> 8) | ((65280 & i) << 8);
    }

    public static Task.TaskError a(File file, int i, c cVar) {
        if (!file.exists()) {
            return ThumbnailError.RawFileNotFound;
        }
        long length = file.length();
        if (length < 8) {
            return ThumbnailError.RawFileTooSmall;
        }
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
            long filePointer = randomAccessFile.getFilePointer();
            int readInt = randomAccessFile.readInt();
            int readInt2 = randomAccessFile.readInt();
            int readInt3 = randomAccessFile.readInt();
            int a2 = a(readInt);
            int a3 = a(readInt2);
            int a4 = a(readInt3);
            int i2 = ((a3 * a4) * a2) / 8;
            int min = (int) Math.min(i, (length - 8) / (i2 + 4));
            if (min < 1) {
                return ThumbnailError.NoThumbailsFound;
            }
            byte[] bArr = new byte[i2];
            ByteBuffer.wrap(bArr);
            ArrayList arrayList = new ArrayList();
            int i3 = 0;
            int i4 = 0;
            int i5 = 0;
            while (i4 < min) {
                int readInt4 = randomAccessFile.readInt();
                int a5 = a(readInt4);
                if (i5 > a5) {
                    Log.d("ThumbnailParser", "thumbnail needSort");
                }
                arrayList.add(new b(a5, readInt4, randomAccessFile.getFilePointer()));
                randomAccessFile.read(bArr);
                i4++;
                i5 = a5;
            }
            randomAccessFile.seek(filePointer);
            Collections.sort(arrayList, new a());
            Log.d("ThumbnailParser", "Sort thumbnail time stamp");
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                b bVar = (b) it.next();
                randomAccessFile.seek(bVar.b());
                randomAccessFile.read(bArr);
                a(bArr, bVar.a(), a3, a4, a2, i3, min, cVar);
                i3++;
            }
            randomAccessFile.close();
            arrayList.clear();
            return null;
        } catch (IOException e) {
            return Task.makeTaskError(e);
        }
    }

    public static ThumbnailError a(byte[] bArr, int i, int i2, int i3, int i4, int i5, int i6, c cVar) throws IOException {
        d dVar;
        e eVar;
        Bitmap createBitmap;
        Bitmap bitmap;
        Canvas canvas;
        if (cVar == null) {
            return ThumbnailError.ParameterError;
        }
        boolean z = cVar instanceof e;
        if (z) {
            eVar = (e) cVar;
            dVar = null;
        } else {
            dVar = (d) cVar;
            eVar = null;
        }
        Log.d("ThumbnailParser", "processRawFile: w/h/time=" + i2 + com.xiaomi.stat.b.h.g + i3 + com.xiaomi.stat.b.h.g + i + ", format=" + i4);
        if (i4 == 32) {
            if (!z) {
                createBitmap = Bitmap.createBitmap(i2, i3, Bitmap.Config.ARGB_8888);
            }
            createBitmap = null;
        } else if (i4 == 16) {
            if (!z) {
                createBitmap = Bitmap.createBitmap(i2, i3, Bitmap.Config.RGB_565);
            }
            createBitmap = null;
        } else if (i4 != 8) {
            return ThumbnailError.UnknownFormat;
        } else {
            if (!z) {
                createBitmap = Bitmap.createBitmap(i2, i3, Bitmap.Config.ARGB_8888);
            }
            createBitmap = null;
        }
        if (!z) {
            bitmap = Bitmap.createBitmap(i2, i3, createBitmap.getConfig());
            canvas = new Canvas(bitmap);
            canvas.scale(1.0f, -1.0f);
        } else {
            bitmap = null;
            canvas = null;
        }
        ByteBuffer wrap = ByteBuffer.wrap(bArr);
        Log.d("ThumbnailParser", "processRawFile : thumbCount=" + i6);
        if (z) {
            eVar.a(bArr, i5, i6, i);
        } else {
            createBitmap.copyPixelsFromBuffer(wrap);
            canvas.drawBitmap(createBitmap, 0.0f, -i3, (Paint) null);
            dVar.a(bitmap, i5, i6, i);
        }
        return null;
    }

    public static ThumbnailError a(InputStream inputStream, long j, int i, c cVar) throws IOException {
        d dVar;
        e eVar;
        boolean z;
        Bitmap createBitmap;
        Bitmap bitmap;
        Canvas canvas;
        Bitmap bitmap2;
        Object obj;
        if (cVar == null) {
            return ThumbnailError.ParameterError;
        }
        DataInputStream dataInputStream = new DataInputStream(inputStream);
        int readInt = dataInputStream.readInt();
        int readInt2 = dataInputStream.readInt();
        int readInt3 = dataInputStream.readInt();
        boolean z2 = cVar instanceof e;
        if (z2) {
            eVar = (e) cVar;
            dVar = null;
        } else {
            dVar = (d) cVar;
            eVar = null;
        }
        if ((readInt2 & (-65536)) == 0 && ((-65536) & readInt3) == 0) {
            z = false;
        } else {
            readInt = a(readInt);
            readInt2 = a(readInt2);
            readInt3 = a(readInt3);
            z = true;
        }
        Log.d("ThumbnailParser", "processRawFile: w/h/swap=" + readInt2 + com.xiaomi.stat.b.h.g + readInt3 + com.xiaomi.stat.b.h.g + z + " format=" + readInt);
        if (readInt == 32) {
            if (!z2) {
                createBitmap = Bitmap.createBitmap(readInt2, readInt3, Bitmap.Config.ARGB_8888);
            }
            createBitmap = null;
        } else if (readInt == 16) {
            if (!z2) {
                createBitmap = Bitmap.createBitmap(readInt2, readInt3, Bitmap.Config.RGB_565);
            }
            createBitmap = null;
        } else if (readInt != 8) {
            return ThumbnailError.UnknownFormat;
        } else {
            if (!z2) {
                createBitmap = Bitmap.createBitmap(readInt2, readInt3, Bitmap.Config.ARGB_8888);
            }
            createBitmap = null;
        }
        if (!z2) {
            bitmap = Bitmap.createBitmap(readInt2, readInt3, createBitmap.getConfig());
            canvas = new Canvas(bitmap);
            canvas.scale(1.0f, -1.0f);
        } else {
            bitmap = null;
            canvas = null;
        }
        int i2 = ((readInt2 * readInt3) * readInt) / 8;
        Bitmap bitmap3 = bitmap;
        int min = (int) Math.min(i, (j - 8) / (i2 + 4));
        if (min < 1) {
            return ThumbnailError.NoThumbailsFound;
        }
        byte[] bArr = new byte[i2];
        ByteBuffer wrap = ByteBuffer.wrap(bArr);
        Log.d("ThumbnailParser", "processRawFile : thumbCount=" + min);
        int i3 = 0;
        while (i3 < min) {
            StringBuilder sb = new StringBuilder();
            Bitmap bitmap4 = bitmap3;
            sb.append("processRawFile : i=");
            sb.append(i3);
            Log.d("ThumbnailParser", sb.toString());
            int readInt4 = dataInputStream.readInt();
            if (z) {
                readInt4 = a(readInt4);
            }
            StringBuilder sb2 = new StringBuilder();
            boolean z3 = z;
            sb2.append("processRawFile : time=");
            sb2.append(readInt4);
            Log.d("ThumbnailParser", sb2.toString());
            DataInputStream dataInputStream2 = dataInputStream;
            if (dataInputStream.read(bArr) < i2 - 1) {
                if (z2) {
                    obj = null;
                    eVar.a(null, i3, min, readInt4);
                } else {
                    obj = null;
                    dVar.a(null, i3, min, readInt4);
                }
                bitmap2 = bitmap4;
            } else if (z2) {
                eVar.a(bArr, i3, min, readInt4);
                wrap.rewind();
                bitmap2 = bitmap4;
            } else {
                createBitmap.copyPixelsFromBuffer(wrap);
                wrap.rewind();
                canvas.drawBitmap(createBitmap, 0.0f, -readInt3, (Paint) null);
                bitmap2 = bitmap4;
                dVar.a(bitmap2, i3, min, readInt4);
            }
            i3++;
            bitmap3 = bitmap2;
            z = z3;
            dataInputStream = dataInputStream2;
        }
        return null;
    }

    /* compiled from: ThumbnailParser.java */
    /* loaded from: classes3.dex */
    public static class b {
        private int a;
        private int b;
        private long c;

        public b(int i, int i2, long j) {
            this.a = i;
            this.b = i2;
            this.c = j;
        }

        public int a() {
            return this.a;
        }

        public long b() {
            return this.c;
        }
    }

    /* compiled from: ThumbnailParser.java */
    /* loaded from: classes3.dex */
    public static class a implements Comparator<b> {
        @Override // java.util.Comparator
        /* renamed from: a */
        public int compare(b bVar, b bVar2) {
            if (bVar.a() < bVar2.a()) {
                return -1;
            }
            return bVar.a() > bVar2.a() ? 1 : 0;
        }
    }
}
