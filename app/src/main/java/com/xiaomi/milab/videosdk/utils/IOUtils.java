package com.xiaomi.milab.videosdk.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.util.Log;
import ch.qos.logback.core.joran.action.Action;
import com.nexstreaming.nexeditorsdk.nexEngine;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import org.keyczar.Keyczar;

/* loaded from: classes3.dex */
public final class IOUtils {
    private static final String TAG = "IOUtils";

    private IOUtils() {
    }

    public static byte[] cache(InputStream inputStream, int i) {
        if (i <= 0) {
            i = nexEngine.ExportHEVCMainTierLevel6;
        }
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(i);
        byte[] bArr = new byte[nexEngine.ExportHEVCHighTierLevel52];
        while (true) {
            try {
                int read = inputStream.read(bArr);
                if (read != -1) {
                    byteArrayOutputStream.write(bArr, 0, read);
                } else {
                    return byteArrayOutputStream.toByteArray();
                }
            } catch (IOException e) {
                Log.w(TAG, e);
                return null;
            }
        }
    }

    public static InputStream openInputStream(Context context, Uri uri) throws FileNotFoundException {
        if (Action.FILE_ATTRIBUTE.equals(uri.getScheme())) {
            return new FileInputStream(uri.getPath());
        }
        return context.getContentResolver().openInputStream(uri);
    }

    public static InputStream openInputStream(String str, Context context, Uri uri) {
        try {
            return openInputStream(context, uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static OutputStream openOutputStream(Context context, Uri uri) throws FileNotFoundException {
        if (Action.FILE_ATTRIBUTE.equals(uri.getScheme())) {
            return new FileOutputStream(uri.getPath());
        }
        return context.getContentResolver().openOutputStream(uri);
    }

    public static OutputStream openOutputStream(String str, Context context, Uri uri) {
        try {
            return openOutputStream(context, uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String readInputStreamToString(String str, InputStream inputStream) {
        return readInputStreamToString(str, inputStream, Keyczar.DEFAULT_ENCODING);
    }

    public static String readInputStreamToString(String str, InputStream inputStream, String str2) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] bArr = new byte[1024];
        while (true) {
            try {
                try {
                    int read = inputStream.read(bArr);
                    if (read != -1) {
                        byteArrayOutputStream.write(bArr, 0, read);
                    } else {
                        return byteArrayOutputStream.toString(str2);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    close(str, inputStream);
                    close(str, byteArrayOutputStream);
                    return null;
                }
            } finally {
                close(str, inputStream);
                close(str, byteArrayOutputStream);
            }
        }
    }

    public static Writer openWriter(File file) {
        try {
            return new FileWriter(file);
        } catch (IOException e) {
            Log.w(TAG, e);
            return null;
        }
    }

    public static Reader openReader(File file) {
        try {
            return new FileReader(file);
        } catch (FileNotFoundException e) {
            Log.w(TAG, e);
            return null;
        }
    }

    public static final void close(Closeable closeable) {
        close(TAG, closeable);
    }

    public static final void close(String str, Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            } else {
                Log.d(str, "res is null");
            }
        } catch (IOException e) {
            Log.w(str, e);
        }
    }

    public static String loadAssetFileString(AssetManager assetManager, String str) {
        InputStream inputStream;
        InputStream inputStream2 = null;
        String str2 = null;
        try {
            inputStream = assetManager.open(str);
            try {
                try {
                    str2 = readInputStreamToString(TAG, inputStream);
                } catch (IOException e) {
                    e = e;
                    e.printStackTrace();
                    close(inputStream);
                    return str2;
                }
            } catch (Throwable th) {
                th = th;
                inputStream2 = inputStream;
                close(inputStream2);
                throw th;
            }
        } catch (IOException e2) {
            e = e2;
            inputStream = null;
        } catch (Throwable th2) {
            th = th2;
            close(inputStream2);
            throw th;
        }
        close(inputStream);
        return str2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0 */
    /* JADX WARN: Type inference failed for: r0v1, types: [java.io.InputStream] */
    /* JADX WARN: Type inference failed for: r0v2 */
    public static String readFile(Context context, String str) {
        InputStream inputStream;
        ?? r0 = 0;
        try {
            try {
                inputStream = context.getResources().getAssets().open(str);
                try {
                    byte[] bArr = new byte[inputStream.available()];
                    inputStream.read(bArr);
                    String str2 = new String(bArr, Keyczar.DEFAULT_ENCODING);
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return str2;
                } catch (FileNotFoundException e2) {
                    e = e2;
                    e.printStackTrace();
                    try {
                        inputStream.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                    return null;
                } catch (IOException e4) {
                    e = e4;
                    e.printStackTrace();
                    try {
                        inputStream.close();
                    } catch (IOException e5) {
                        e5.printStackTrace();
                    }
                    return null;
                }
            } catch (Throwable th) {
                th = th;
                r0 = context;
                try {
                    r0.close();
                } catch (IOException e6) {
                    e6.printStackTrace();
                }
                throw th;
            }
        } catch (FileNotFoundException e7) {
            e = e7;
            inputStream = null;
        } catch (IOException e8) {
            e = e8;
            inputStream = null;
        } catch (Throwable th2) {
            th = th2;
            r0.close();
            throw th;
        }
    }
}
