package ch.qos.logback.core.util;

import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.ContextAwareBase;
import com.xiaomi.stat.b.h;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/* loaded from: classes.dex */
public class FileUtil extends ContextAwareBase {
    public static final int BUF_SIZE = 32768;

    public FileUtil(Context context) {
        setContext(context);
    }

    public static URL fileToURL(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException("Unexpected exception on file [" + file + "]", e);
        }
    }

    public static boolean createMissingParentDirectories(File file) {
        File parentFile = file.getParentFile();
        if (parentFile == null) {
            return true;
        }
        parentFile.mkdirs();
        return parentFile.exists();
    }

    public static String prefixRelativePath(String str, String str2) {
        if (str == null || OptionHelper.isEmpty(str.trim()) || new File(str2).isAbsolute()) {
            return str2;
        }
        return str + h.g + str2;
    }

    /* JADX WARN: Removed duplicated region for block: B:39:0x0074 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:43:0x006f A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void copy(java.lang.String r8, java.lang.String r9) throws ch.qos.logback.core.rolling.RolloverFailure {
        /*
            r7 = this;
            r0 = 0
            java.io.BufferedInputStream r1 = new java.io.BufferedInputStream     // Catch: java.lang.Throwable -> L3d java.io.IOException -> L40
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L3d java.io.IOException -> L40
            r2.<init>(r8)     // Catch: java.lang.Throwable -> L3d java.io.IOException -> L40
            r1.<init>(r2)     // Catch: java.lang.Throwable -> L3d java.io.IOException -> L40
            java.io.BufferedOutputStream r2 = new java.io.BufferedOutputStream     // Catch: java.lang.Throwable -> L35 java.io.IOException -> L38
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> L35 java.io.IOException -> L38
            r3.<init>(r9)     // Catch: java.lang.Throwable -> L35 java.io.IOException -> L38
            r2.<init>(r3)     // Catch: java.lang.Throwable -> L35 java.io.IOException -> L38
            r3 = 32768(0x8000, float:4.5918E-41)
            byte[] r3 = new byte[r3]     // Catch: java.io.IOException -> L33 java.lang.Throwable -> L6b
        L1a:
            int r4 = r1.read(r3)     // Catch: java.io.IOException -> L33 java.lang.Throwable -> L6b
            r5 = -1
            if (r4 == r5) goto L26
            r5 = 0
            r2.write(r3, r5, r4)     // Catch: java.io.IOException -> L33 java.lang.Throwable -> L6b
            goto L1a
        L26:
            r1.close()     // Catch: java.io.IOException -> L33 java.lang.Throwable -> L6b
            r2.close()     // Catch: java.lang.Throwable -> L2d java.io.IOException -> L2f
            return
        L2d:
            r8 = move-exception
            goto L6d
        L2f:
            r1 = move-exception
            r6 = r1
            r1 = r0
            goto L3b
        L33:
            r0 = move-exception
            goto L44
        L35:
            r8 = move-exception
            r2 = r0
            goto L6c
        L38:
            r2 = move-exception
            r6 = r2
            r2 = r0
        L3b:
            r0 = r6
            goto L44
        L3d:
            r8 = move-exception
            r2 = r0
            goto L6d
        L40:
            r1 = move-exception
            r2 = r0
            r0 = r1
            r1 = r2
        L44:
            java.lang.StringBuilder r3 = new java.lang.StringBuilder     // Catch: java.lang.Throwable -> L6b
            r3.<init>()     // Catch: java.lang.Throwable -> L6b
            java.lang.String r4 = "Failed to copy ["
            r3.append(r4)     // Catch: java.lang.Throwable -> L6b
            r3.append(r8)     // Catch: java.lang.Throwable -> L6b
            java.lang.String r8 = "] to ["
            r3.append(r8)     // Catch: java.lang.Throwable -> L6b
            r3.append(r9)     // Catch: java.lang.Throwable -> L6b
            java.lang.String r8 = "]"
            r3.append(r8)     // Catch: java.lang.Throwable -> L6b
            java.lang.String r8 = r3.toString()     // Catch: java.lang.Throwable -> L6b
            r7.addError(r8, r0)     // Catch: java.lang.Throwable -> L6b
            ch.qos.logback.core.rolling.RolloverFailure r9 = new ch.qos.logback.core.rolling.RolloverFailure     // Catch: java.lang.Throwable -> L6b
            r9.<init>(r8)     // Catch: java.lang.Throwable -> L6b
            throw r9     // Catch: java.lang.Throwable -> L6b
        L6b:
            r8 = move-exception
        L6c:
            r0 = r1
        L6d:
            if (r0 == 0) goto L72
            r0.close()     // Catch: java.io.IOException -> L72
        L72:
            if (r2 == 0) goto L77
            r2.close()     // Catch: java.io.IOException -> L77
        L77:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: ch.qos.logback.core.util.FileUtil.copy(java.lang.String, java.lang.String):void");
    }
}
