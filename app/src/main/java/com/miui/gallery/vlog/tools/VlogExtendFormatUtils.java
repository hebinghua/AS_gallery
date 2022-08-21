package com.miui.gallery.vlog.tools;

import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Iterator;

/* loaded from: classes2.dex */
public class VlogExtendFormatUtils {
    public static ArrayList<String> mSupportedExtendFormatList;

    static {
        ArrayList<String> arrayList = new ArrayList<>();
        mSupportedExtendFormatList = arrayList;
        arrayList.add("MP4");
        mSupportedExtendFormatList.add("3GP");
        mSupportedExtendFormatList.add("MOV");
    }

    public static ArrayList<String> filterVideoByExtendFormat(ArrayList<String> arrayList) {
        if (arrayList == null || arrayList.size() == 0) {
            return arrayList;
        }
        ArrayList<String> arrayList2 = new ArrayList<>();
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            String next = it.next();
            String pathSuffix = PathNameUtil.getPathSuffix(next);
            if (!TextUtils.isEmpty(pathSuffix) && mSupportedExtendFormatList.contains(pathSuffix.toUpperCase())) {
                arrayList2.add(next);
            }
        }
        return arrayList2;
    }
}
