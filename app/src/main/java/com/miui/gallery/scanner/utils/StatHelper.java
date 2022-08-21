package com.miui.gallery.scanner.utils;

import ch.qos.logback.core.joran.action.Action;
import com.miui.gallery.stat.SamplingStatHelper;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class StatHelper {
    public static void recordHiddenAlbum(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(Action.FILE_ATTRIBUTE, str);
        SamplingStatHelper.recordCountEvent("media_scanner", "scanner_directory_regard_as_hidden", hashMap);
    }

    public static void recordInsertAlbumError(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put(Action.FILE_ATTRIBUTE, str);
        SamplingStatHelper.recordCountEvent("media_scanner", "insert_album_error", hashMap);
    }

    public static void statFuzzyMatch(String str, String str2, String str3) {
        String format = String.format("scanner_fuzzy_match_%s", str);
        HashMap hashMap = new HashMap();
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, String.format("%s_%s", str2, str3));
        SamplingStatHelper.recordCountEvent("media_scanner", format, hashMap);
    }
}
