package xcrash;

import android.text.TextUtils;

/* loaded from: classes3.dex */
public class TombstoneManager {
    public static boolean appendSection(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || str3 == null) {
            return false;
        }
        FileManager fileManager = FileManager.getInstance();
        return fileManager.appendText(str, "\n\n" + str2 + ":\n" + str3 + "\n\n");
    }
}
