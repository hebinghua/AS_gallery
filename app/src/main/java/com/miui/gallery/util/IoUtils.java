package com.miui.gallery.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import ch.qos.logback.core.joran.action.Action;
import com.miui.gallery.util.logger.DefaultLogger;
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

/* loaded from: classes2.dex */
public final class IoUtils {
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
            DefaultLogger.e(str, e);
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
            DefaultLogger.e(str, e);
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
                    DefaultLogger.e(str, e);
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
            DefaultLogger.w("IoUtils", e);
            return null;
        }
    }

    public static Reader openReader(File file) {
        try {
            return new FileReader(file);
        } catch (FileNotFoundException e) {
            DefaultLogger.w("IoUtils", e);
            return null;
        }
    }

    public static final void close(Closeable closeable) {
        close("IoUtils", closeable);
    }

    public static final void close(String str, Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            } else {
                DefaultLogger.d(str, "res is null");
            }
        } catch (IOException e) {
            DefaultLogger.w(str, e);
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
                    str2 = readInputStreamToString("IoUtils", inputStream);
                } catch (IOException e) {
                    e = e;
                    DefaultLogger.e("IoUtils", e);
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
}
