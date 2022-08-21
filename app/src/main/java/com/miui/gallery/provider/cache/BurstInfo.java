package com.miui.gallery.provider.cache;

import android.text.TextUtils;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseFileUtils;

/* loaded from: classes2.dex */
public class BurstInfo {
    public int mBurstIndex;
    public long mGroupKey;
    public boolean mIsTimeBurst;

    public BurstInfo(long j, int i, boolean z) {
        this.mGroupKey = j;
        this.mBurstIndex = i;
        this.mIsTimeBurst = z;
    }

    public static BurstInfo generateBurstInfo(Long l, String str, String str2) {
        String fileTitle;
        String[] split;
        if (l != null && maybeBurst(str) && (split = (fileTitle = BaseFileUtils.getFileTitle(str)).split("_")) != null && split.length >= 4) {
            String concat = split[1].concat(split[2]);
            int indexOf = split[3].indexOf("BURST");
            if (indexOf < 0) {
                return null;
            }
            String substring = split[3].substring(indexOf + 5, split[3].length());
            try {
                long parseLong = Long.parseLong(concat);
                if (BaseFileMimeUtil.isRawFromMimeType(str2)) {
                    parseLong += 100000000000000L;
                }
                return new BurstInfo(l.longValue() + parseLong, Integer.parseInt(substring), fileTitle.contains("_TIMEBURST"));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String getBurstPhotoTypeByFileName(String str) {
        String fileTitle;
        String[] split;
        if (maybeBurst(str) && (split = (fileTitle = BaseFileUtils.getFileTitle(str)).split("_")) != null && split.length >= 4 && split[3].indexOf("BURST") >= 0) {
            return fileTitle.contains("_TIMEBURST") ? "time_burst" : "burst";
        }
        return null;
    }

    public static boolean maybeBurst(String str) {
        int length;
        if (!TextUtils.isEmpty(str) && (length = str.length()) >= 25) {
            if (str.contains("_BURST")) {
                return true;
            }
            return length >= 29 && str.contains("_TIMEBURST");
        }
        return false;
    }
}
