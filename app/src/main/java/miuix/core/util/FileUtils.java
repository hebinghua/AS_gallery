package miuix.core.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.Charset;
import java.util.regex.Pattern;
import org.keyczar.Keyczar;

/* loaded from: classes3.dex */
public class FileUtils {
    public static final Pattern SAFE_FILENAME_PATTERN = Pattern.compile("[\\w%+,./=_-]+");

    public static String getExtension(String str) {
        int lastIndexOf;
        return (str == null || str.length() == 0 || (lastIndexOf = str.lastIndexOf(46)) <= -1) ? "" : str.substring(lastIndexOf + 1);
    }

    public static String getName(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        int lastIndexOf = str.lastIndexOf(File.separatorChar);
        if (lastIndexOf < 0) {
            lastIndexOf = -1;
        }
        int lastIndexOf2 = str.lastIndexOf(".");
        return lastIndexOf2 < 0 ? str.substring(lastIndexOf + 1) : str.substring(lastIndexOf + 1, lastIndexOf2);
    }

    public static boolean addNoMedia(String str) {
        File file = new File(str);
        if (file.isDirectory()) {
            try {
                return new File(file, ".nomedia").createNewFile();
            } catch (IOException unused) {
                return false;
            }
        }
        return false;
    }

    public static String normalizeDirectoryName(String str) {
        if (str.charAt(str.length() - 1) == File.separatorChar) {
            return str;
        }
        return str + File.separator;
    }

    public static long getFolderSize(File file) {
        long j = 0;
        if (!file.exists()) {
            return 0L;
        }
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (listFiles != null) {
                for (File file2 : listFiles) {
                    j += getFolderSize(file2);
                }
            }
            return j;
        }
        return file.length();
    }

    public static String readFileAsString(String str) throws IOException {
        return new String(readFileAsBytes(str), Charset.forName(Keyczar.DEFAULT_ENCODING));
    }

    public static byte[] readFileAsBytes(String str) throws IOException {
        RandomAccessFile randomAccessFile = null;
        try {
            RandomAccessFile randomAccessFile2 = new RandomAccessFile(str, "r");
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream((int) randomAccessFile2.length());
                byte[] bArr = new byte[8192];
                while (true) {
                    int read = randomAccessFile2.read(bArr);
                    if (read == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(bArr, 0, read);
                }
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                try {
                    randomAccessFile2.close();
                } catch (IOException unused) {
                }
                return byteArray;
            } catch (Throwable th) {
                th = th;
                randomAccessFile = randomAccessFile2;
                if (randomAccessFile != null) {
                    try {
                        randomAccessFile.close();
                    } catch (IOException unused2) {
                    }
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }
}
