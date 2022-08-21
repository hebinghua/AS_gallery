package miuix.os;

import android.util.Log;
import java.io.IOException;
import java.util.Locale;
import miuix.core.util.FileUtils;

/* loaded from: classes3.dex */
public class ProcessUtils {
    public static String getProcessNameByPid(int i) {
        String format = String.format(Locale.US, "/proc/%d/cmdline", Integer.valueOf(i));
        try {
            String readFileAsString = FileUtils.readFileAsString(format);
            if (readFileAsString == null) {
                return null;
            }
            int indexOf = readFileAsString.indexOf(0);
            return indexOf >= 0 ? readFileAsString.substring(0, indexOf) : readFileAsString;
        } catch (IOException e) {
            Log.e("ProcessUtils", "Fail to read cmdline: " + format, e);
            return null;
        }
    }
}
