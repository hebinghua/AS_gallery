package miuix.io;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.util.Log;
import ch.qos.logback.core.joran.action.Action;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: classes3.dex */
public class ResettableInputStream extends InputStream {
    public final AssetManager mAssetManager;
    public final String mAssetPath;
    public final byte[] mByteArray;
    public final Context mContext;
    public final Object mFinalizeGuardian;
    public volatile InputStream mInputStream;
    public IOException mLastException;
    public Throwable mOpenStack;
    public final String mPath;
    public final Type mType;
    public final Uri mUri;

    /* loaded from: classes3.dex */
    public enum Type {
        File,
        Uri,
        Asset,
        ByteArray
    }

    public ResettableInputStream(String str) {
        this.mFinalizeGuardian = new Object() { // from class: miuix.io.ResettableInputStream.1
            public void finalize() throws Throwable {
                try {
                    if (ResettableInputStream.this.mOpenStack != null) {
                        Log.e("ResettableInputStream", "InputStream is opened but never closed here", ResettableInputStream.this.mOpenStack);
                    }
                    ResettableInputStream.this.close();
                } finally {
                    super.finalize();
                }
            }
        };
        this.mType = Type.File;
        this.mPath = str;
        this.mContext = null;
        this.mUri = null;
        this.mAssetManager = null;
        this.mAssetPath = null;
        this.mByteArray = null;
    }

    public ResettableInputStream(Context context, Uri uri) {
        this.mFinalizeGuardian = new Object() { // from class: miuix.io.ResettableInputStream.1
            public void finalize() throws Throwable {
                try {
                    if (ResettableInputStream.this.mOpenStack != null) {
                        Log.e("ResettableInputStream", "InputStream is opened but never closed here", ResettableInputStream.this.mOpenStack);
                    }
                    ResettableInputStream.this.close();
                } finally {
                    super.finalize();
                }
            }
        };
        if (Action.FILE_ATTRIBUTE.equals(uri.getScheme())) {
            this.mType = Type.File;
            this.mPath = uri.getPath();
            this.mContext = null;
            this.mUri = null;
            this.mAssetManager = null;
            this.mAssetPath = null;
            this.mByteArray = null;
            return;
        }
        this.mType = Type.Uri;
        this.mContext = context;
        this.mUri = uri;
        this.mPath = null;
        this.mAssetManager = null;
        this.mAssetPath = null;
        this.mByteArray = null;
    }

    public ResettableInputStream(byte[] bArr) {
        this.mFinalizeGuardian = new Object() { // from class: miuix.io.ResettableInputStream.1
            public void finalize() throws Throwable {
                try {
                    if (ResettableInputStream.this.mOpenStack != null) {
                        Log.e("ResettableInputStream", "InputStream is opened but never closed here", ResettableInputStream.this.mOpenStack);
                    }
                    ResettableInputStream.this.close();
                } finally {
                    super.finalize();
                }
            }
        };
        this.mType = Type.ByteArray;
        this.mByteArray = bArr;
        this.mPath = null;
        this.mContext = null;
        this.mUri = null;
        this.mAssetManager = null;
        this.mAssetPath = null;
    }

    public final void openStream() throws IOException {
        IOException iOException = this.mLastException;
        if (iOException != null) {
            throw iOException;
        }
        if (this.mInputStream != null) {
            return;
        }
        synchronized (this.mFinalizeGuardian) {
            if (this.mInputStream != null) {
                return;
            }
            int i = AnonymousClass2.$SwitchMap$miuix$io$ResettableInputStream$Type[this.mType.ordinal()];
            if (i == 1) {
                this.mInputStream = this.mContext.getContentResolver().openInputStream(this.mUri);
            } else if (i == 2) {
                this.mInputStream = new FileInputStream(this.mPath);
            } else if (i == 3) {
                this.mInputStream = this.mAssetManager.open(this.mAssetPath);
            } else if (i == 4) {
                this.mInputStream = new ByteArrayInputStream(this.mByteArray);
            } else {
                throw new IllegalStateException("Unkown type " + this.mType);
            }
            this.mOpenStack = new Throwable();
        }
    }

    /* renamed from: miuix.io.ResettableInputStream$2  reason: invalid class name */
    /* loaded from: classes3.dex */
    public static /* synthetic */ class AnonymousClass2 {
        public static final /* synthetic */ int[] $SwitchMap$miuix$io$ResettableInputStream$Type;

        static {
            int[] iArr = new int[Type.values().length];
            $SwitchMap$miuix$io$ResettableInputStream$Type = iArr;
            try {
                iArr[Type.Uri.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$miuix$io$ResettableInputStream$Type[Type.File.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$miuix$io$ResettableInputStream$Type[Type.Asset.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$miuix$io$ResettableInputStream$Type[Type.ByteArray.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        openStream();
        return this.mInputStream.available();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.mInputStream == null) {
            return;
        }
        synchronized (this.mFinalizeGuardian) {
            if (this.mInputStream == null) {
                return;
            }
            this.mInputStream.close();
            this.mOpenStack = null;
            this.mInputStream = null;
            this.mLastException = null;
        }
    }

    @Override // java.io.InputStream
    public void mark(int i) {
        try {
            openStream();
            this.mInputStream.mark(i);
        } catch (IOException e) {
            this.mLastException = e;
        }
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        try {
            openStream();
            return this.mInputStream.markSupported();
        } catch (IOException e) {
            this.mLastException = e;
            return super.markSupported();
        }
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        openStream();
        return this.mInputStream.read();
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr) throws IOException {
        openStream();
        return this.mInputStream.read(bArr);
    }

    @Override // java.io.InputStream
    public int read(byte[] bArr, int i, int i2) throws IOException {
        openStream();
        return this.mInputStream.read(bArr, i, i2);
    }

    @Override // java.io.InputStream
    public synchronized void reset() throws IOException {
        if (this.mInputStream != null) {
            if (this.mInputStream instanceof FileInputStream) {
                ((FileInputStream) this.mInputStream).getChannel().position(0L);
                return;
            }
            if (!(this.mInputStream instanceof AssetManager.AssetInputStream) && !(this.mInputStream instanceof ByteArrayInputStream)) {
                close();
            }
            this.mInputStream.reset();
        }
    }

    @Override // java.io.InputStream
    public long skip(long j) throws IOException {
        openStream();
        return this.mInputStream.skip(j);
    }
}
