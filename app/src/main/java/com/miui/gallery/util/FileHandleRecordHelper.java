package com.miui.gallery.util;

/* loaded from: classes2.dex */
public class FileHandleRecordHelper {
    public static IEntityManagerInvoker mInvoker;

    /* loaded from: classes2.dex */
    public interface IEntityManagerInvoker {
        void recordFileHandle(String str, String str2, int i, String str3);
    }

    public static boolean recordFileCopy(String str, String str2, String str3, boolean z) {
        if (z) {
            recordFileHandle(str2, str, 2, str3);
        }
        return z;
    }

    public static boolean recordFileMove(String str, String str2, String str3, boolean z) {
        if (z) {
            recordFileHandle(str2, str, 1, str3);
        }
        return z;
    }

    public static void setInvoker(IEntityManagerInvoker iEntityManagerInvoker) {
        mInvoker = iEntityManagerInvoker;
    }

    public static void recordFileHandle(String str, String str2, int i, String str3) {
        try {
            IEntityManagerInvoker iEntityManagerInvoker = mInvoker;
            if (iEntityManagerInvoker == null) {
                return;
            }
            iEntityManagerInvoker.recordFileHandle(str, str2, i, str3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String appendInvokerTag(String str, String str2) {
        return str + "_" + str2;
    }
}
