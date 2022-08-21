package com.miui.gallery.util.gifdecoder;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.HandlerThread;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.CryptoUtil;
import com.miui.gallery.util.ImageSizeUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes2.dex */
public class NSGifDecode implements Runnable {
    public DecodeRunnable mDecodeRunnable;
    public Handler mHandler;
    public NSGifGen mNSGif = null;
    public Bitmap mFrame = null;
    public volatile boolean mQuit = false;
    public volatile GifFrameUpdateListener mListener = null;
    public final Object mLock = new Object();

    /* loaded from: classes2.dex */
    public interface GifFrameUpdateListener {
        void onUpdateGifFrame(Bitmap bitmap);
    }

    /* loaded from: classes2.dex */
    public interface NSGifGen {
        NSGif gen();
    }

    public static int convertShort(byte[] bArr, int i) {
        return ((bArr[i + 1] & 255) << 8) | (bArr[i] & 255);
    }

    public static boolean checkGif(InputStream inputStream, byte[] bArr, int[] iArr) throws IOException {
        if (inputStream.read(bArr, 0, 10) != 10) {
            return false;
        }
        String str = new String(bArr, 0, 6);
        if (!"GIF87a".equals(str) && !"GIF89a".equals(str)) {
            DefaultLogger.d("NSGifDecode", "is not gif, tag: " + str);
            return false;
        }
        int convertShort = convertShort(bArr, 6);
        if (iArr != null && iArr.length > 0) {
            iArr[0] = convertShort;
        }
        if (convertShort <= 0 || convertShort > ImageSizeUtils.getMaxTextureSize()) {
            DefaultLogger.d("NSGifDecode", "invalid width: " + convertShort);
            return false;
        }
        int convertShort2 = convertShort(bArr, 8);
        if (iArr != null && iArr.length > 1) {
            iArr[1] = convertShort2;
        }
        if (convertShort2 > 0 && convertShort2 <= ImageSizeUtils.getMaxTextureSize()) {
            return true;
        }
        DefaultLogger.d("NSGifDecode", "invalid height: " + convertShort2);
        return false;
    }

    /* JADX WARN: Not initialized variable reg: 2, insn: 0x0046: MOVE  (r1 I:??[OBJECT, ARRAY]) = (r2 I:??[OBJECT, ARRAY]), block:B:24:0x0046 */
    public static NSGifDecode create(final String str) {
        FileInputStream fileInputStream;
        Closeable closeable;
        int[] iArr = new int[2];
        Closeable closeable2 = null;
        try {
            try {
                fileInputStream = new FileInputStream(str);
                try {
                    if (!checkGif(fileInputStream, new byte[10], iArr)) {
                        BaseMiscUtil.closeSilently(fileInputStream);
                        return null;
                    }
                    BaseMiscUtil.closeSilently(fileInputStream);
                    NSGifDecode create = create(new NSGifGen() { // from class: com.miui.gallery.util.gifdecoder.NSGifDecode.1
                        @Override // com.miui.gallery.util.gifdecoder.NSGifDecode.NSGifGen
                        public NSGif gen() {
                            return NSGif.create(str);
                        }
                    });
                    if (create != null) {
                        try {
                            create.mFrame = getFrame(iArr[0], iArr[1], Bitmap.Config.ARGB_8888);
                        } catch (OutOfMemoryError unused) {
                        }
                    }
                    return create;
                } catch (IOException e) {
                    e = e;
                    DefaultLogger.d("NSGifDecode", "read gif file", e);
                    BaseMiscUtil.closeSilently(fileInputStream);
                    return null;
                }
            } catch (Throwable th) {
                th = th;
                closeable2 = closeable;
                BaseMiscUtil.closeSilently(closeable2);
                throw th;
            }
        } catch (IOException e2) {
            e = e2;
            fileInputStream = null;
        } catch (Throwable th2) {
            th = th2;
            BaseMiscUtil.closeSilently(closeable2);
            throw th;
        }
    }

    /* JADX WARN: Not initialized variable reg: 3, insn: 0x0065: MOVE  (r2 I:??[OBJECT, ARRAY]) = (r3 I:??[OBJECT, ARRAY]), block:B:28:0x0065 */
    public static NSGifDecode create(FileDescriptor fileDescriptor, byte[] bArr) {
        InputStream inputStream;
        Closeable closeable;
        int[] iArr = new int[2];
        Closeable closeable2 = null;
        try {
            try {
                inputStream = new FileInputStream(fileDescriptor);
                try {
                    if (inputStream.available() > 5242880) {
                        DefaultLogger.d("NSGifDecode", "file is too large");
                        BaseMiscUtil.closeSilently(inputStream);
                        return null;
                    }
                    if (bArr != null && bArr.length > 0) {
                        inputStream = CryptoUtil.getDecryptCipherInputStream(inputStream, bArr);
                    }
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] bArr2 = new byte[4096];
                    if (!checkGif(inputStream, bArr2, iArr)) {
                        BaseMiscUtil.closeSilently(inputStream);
                        return null;
                    }
                    byteArrayOutputStream.write(bArr2, 0, 10);
                    while (true) {
                        int read = inputStream.read(bArr2);
                        if (read < 0) {
                            break;
                        }
                        byteArrayOutputStream.write(bArr2, 0, read);
                    }
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    NSGifDecode create = create(byteArray, 0, byteArray.length);
                    if (create != null) {
                        try {
                            create.mFrame = getFrame(iArr[0], iArr[1], Bitmap.Config.ARGB_8888);
                        } catch (OutOfMemoryError unused) {
                        }
                    }
                    BaseMiscUtil.closeSilently(inputStream);
                    return create;
                } catch (Exception e) {
                    e = e;
                    DefaultLogger.d("NSGifDecode", "load gif data", e);
                    BaseMiscUtil.closeSilently(inputStream);
                    return null;
                }
            } catch (Throwable th) {
                th = th;
                closeable2 = closeable;
                BaseMiscUtil.closeSilently(closeable2);
                throw th;
            }
        } catch (Exception e2) {
            e = e2;
            inputStream = null;
        } catch (Throwable th2) {
            th = th2;
            BaseMiscUtil.closeSilently(closeable2);
            throw th;
        }
    }

    public static NSGifDecode create(final byte[] bArr, final int i, final int i2) {
        return create(new NSGifGen() { // from class: com.miui.gallery.util.gifdecoder.NSGifDecode.2
            @Override // com.miui.gallery.util.gifdecoder.NSGifDecode.NSGifGen
            public NSGif gen() {
                return NSGif.create(bArr, i, i2);
            }
        });
    }

    public static NSGifDecode create(NSGifGen nSGifGen) {
        if (nSGifGen != null) {
            try {
                NSGifDecode nSGifDecode = new NSGifDecode();
                nSGifDecode.mNSGif = nSGifGen;
                return nSGifDecode;
            } catch (OutOfMemoryError unused) {
                return null;
            }
        }
        return null;
    }

    public void cancel() {
        this.mQuit = true;
        synchronized (this.mLock) {
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.removeCallbacks(this.mDecodeRunnable);
                this.mHandler.getLooper().quitSafely();
                this.mDecodeRunnable = null;
                this.mHandler = null;
            }
        }
    }

    @Override // java.lang.Runnable
    public void run() {
        try {
            NSGifGen nSGifGen = this.mNSGif;
            if (nSGifGen == null) {
                return;
            }
            NSGif gen = nSGifGen.gen();
            this.mNSGif = null;
            if (gen == null) {
                return;
            }
            if (this.mFrame == null) {
                try {
                    this.mFrame = getFrame(gen, Bitmap.Config.ARGB_8888);
                } catch (OutOfMemoryError e) {
                    DefaultLogger.e("NSGifDecode", "OOM on create bitmap", e);
                    return;
                }
            }
            if (gen.getFrameCount() == 1) {
                GifFrameUpdateListener gifFrameUpdateListener = this.mListener;
                if (!gen.decodeFrame(0) || !gen.writeTo(this.mFrame) || gifFrameUpdateListener == null) {
                    return;
                }
                gifFrameUpdateListener.onUpdateGifFrame(this.mFrame);
                return;
            }
            if (!gen.decodeFrame(0)) {
                this.mQuit = true;
            }
            if (this.mQuit) {
                return;
            }
            synchronized (this.mLock) {
                HandlerThread handlerThread = new HandlerThread("NSGifDecode");
                handlerThread.start();
                this.mHandler = new Handler(handlerThread.getLooper());
                DecodeRunnable decodeRunnable = new DecodeRunnable(gen, 0);
                this.mDecodeRunnable = decodeRunnable;
                this.mHandler.post(decodeRunnable);
            }
        } catch (Exception e2) {
            DefaultLogger.w("NSGifDecode", e2);
        }
    }

    public void setListener(GifFrameUpdateListener gifFrameUpdateListener) {
        this.mListener = gifFrameUpdateListener;
    }

    public static Bitmap getFrame(NSGif nSGif, Bitmap.Config config) {
        return getFrame(nSGif.getWidth(), nSGif.getHeight(), config);
    }

    public static Bitmap getFrame(int i, int i2, Bitmap.Config config) {
        double expectedScale = FrameSizeUtil.getExpectedScale(i, i2, config);
        return Bitmap.createBitmap((int) (i * expectedScale), (int) (i2 * expectedScale), config);
    }

    /* loaded from: classes2.dex */
    public class DecodeRunnable implements Runnable {
        public int mIndex;
        public NSGif mNSGif;

        public DecodeRunnable(NSGif nSGif, int i) {
            this.mNSGif = nSGif;
            this.mIndex = i;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (NSGifDecode.this.mQuit) {
                return;
            }
            long currentTimeMillis = System.currentTimeMillis() + Math.max(20, this.mNSGif.getFrameDelay(this.mIndex));
            if (!NSGifDecode.this.mQuit && this.mNSGif.writeTo(NSGifDecode.this.mFrame)) {
                GifFrameUpdateListener gifFrameUpdateListener = NSGifDecode.this.mListener;
                if (gifFrameUpdateListener != null) {
                    gifFrameUpdateListener.onUpdateGifFrame(NSGifDecode.this.mFrame);
                }
                int i = this.mIndex + 1;
                this.mIndex = i;
                if (i >= this.mNSGif.getFrameCount()) {
                    this.mIndex = 0;
                }
                if (NSGifDecode.this.mQuit || !this.mNSGif.decodeFrame(this.mIndex) || NSGifDecode.this.mQuit) {
                    return;
                }
                synchronized (NSGifDecode.this.mLock) {
                    if (NSGifDecode.this.mHandler != null) {
                        long currentTimeMillis2 = System.currentTimeMillis();
                        if (currentTimeMillis2 < currentTimeMillis) {
                            NSGifDecode.this.mHandler.postDelayed(this, currentTimeMillis - currentTimeMillis2);
                        } else {
                            NSGifDecode.this.mHandler.post(this);
                        }
                    }
                }
                return;
            }
            DefaultLogger.e("NSGifDecode", "write frame " + this.mIndex + " failed");
        }
    }
}
