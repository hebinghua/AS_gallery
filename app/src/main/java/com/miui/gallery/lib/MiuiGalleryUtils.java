package com.miui.gallery.lib;

import android.text.TextUtils;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;

/* loaded from: classes2.dex */
public class MiuiGalleryUtils {
    public static final String[] ALBUM_SHARE_BARCODE_URLS;
    public static final ArrayList<Pattern> ALBUM_SHARE_BARCODE_URL_PATTERNS;
    public static final ArrayList<Pattern> ALBUM_SHARE_BARCODE_URL_STRICT_PATTERNS;
    public static final String[] ALBUM_SHARE_INVITATION_URLS;
    public static final ArrayList<Pattern> ALBUM_SHARE_INVITATION_URL_PATTERNS;
    public static final ArrayList<Pattern> ALBUM_SHARE_INVITATION_URL_STRICT_PATTERNS;

    static {
        String[] strArr = {"http://mij.cc/[a-z]+/[a-zA-Z0-9\\-_]{16}#a", "http://mi1.cc/[a-zA-Z0-9\\-_]{16}#a", null};
        ALBUM_SHARE_INVITATION_URLS = strArr;
        ALBUM_SHARE_BARCODE_URLS = strArr;
        ArrayList<Pattern> newArrayList = Lists.newArrayList();
        ALBUM_SHARE_INVITATION_URL_PATTERNS = newArrayList;
        ArrayList<Pattern> newArrayList2 = Lists.newArrayList();
        ALBUM_SHARE_INVITATION_URL_STRICT_PATTERNS = newArrayList2;
        ArrayList<Pattern> newArrayList3 = Lists.newArrayList();
        ALBUM_SHARE_BARCODE_URL_PATTERNS = newArrayList3;
        ArrayList<Pattern> newArrayList4 = Lists.newArrayList();
        ALBUM_SHARE_BARCODE_URL_STRICT_PATTERNS = newArrayList4;
        initPatterns(strArr, newArrayList, newArrayList2);
        initPatterns(strArr, newArrayList3, newArrayList4);
    }

    public static void initPatterns(String[] strArr, ArrayList<Pattern> arrayList, ArrayList<Pattern> arrayList2) {
        for (String str : strArr) {
            if (str != null) {
                arrayList.add(Pattern.compile(str));
                arrayList2.add(Pattern.compile(String.format("^%s$", str)));
            }
        }
    }

    public static boolean isAlbumShareInvitationUrl(String str) {
        return hasMatch(ALBUM_SHARE_INVITATION_URL_STRICT_PATTERNS, str);
    }

    public static boolean isAlbumShareBarcodeUrl(String str) {
        return hasMatch(ALBUM_SHARE_BARCODE_URL_STRICT_PATTERNS, str);
    }

    public static boolean isAlbumShareUrl(String str) {
        return isAlbumShareInvitationUrl(str) || isAlbumShareBarcodeUrl(str);
    }

    public static boolean hasMatch(ArrayList<Pattern> arrayList, String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Iterator<Pattern> it = arrayList.iterator();
        while (it.hasNext()) {
            if (it.next().matcher(str).matches()) {
                return true;
            }
        }
        return false;
    }
}
