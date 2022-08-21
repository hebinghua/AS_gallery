package com.miui.mishare.app.util;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import java.net.URLDecoder;
import java.util.regex.Pattern;

/* loaded from: classes3.dex */
public class MiShareFileUtil {
    public static boolean isImageCanPrint(Context context, Uri uri) {
        String fileExtensionFromUri = getFileExtensionFromUri(uri);
        return fileExtensionFromUri.equals("jpg") || fileExtensionFromUri.equals("jpeg") || fileExtensionFromUri.equals("png") || fileExtensionFromUri.equals("gif") || fileExtensionFromUri.equals("bmp") || fileExtensionFromUri.equals("webp") || fileExtensionFromUri.equals("wbmp");
    }

    public static boolean isFilePdf(Context context, Uri uri) {
        return getFileExtensionFromUri(uri).equals("pdf");
    }

    public static String getFileNameFromUri(Uri uri) {
        if (uri == null || TextUtils.isEmpty(uri.toString())) {
            return "";
        }
        try {
            String decode = URLDecoder.decode(uri.toString(), "utf-8");
            int lastIndexOf = decode.lastIndexOf(35);
            if (lastIndexOf > 0) {
                decode = decode.substring(0, lastIndexOf);
            }
            int lastIndexOf2 = decode.lastIndexOf(63);
            if (lastIndexOf2 > 0) {
                decode = decode.substring(0, lastIndexOf2);
            }
            int lastIndexOf3 = decode.lastIndexOf(47);
            return lastIndexOf3 >= 0 ? decode.substring(lastIndexOf3 + 1) : decode;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getFileExtensionFromUri(Uri uri) {
        int lastIndexOf;
        if (uri == null || TextUtils.isEmpty(uri.toString())) {
            return "";
        }
        String uri2 = uri.toString();
        int lastIndexOf2 = uri2.lastIndexOf(35);
        if (lastIndexOf2 > 0) {
            uri2 = uri2.substring(0, lastIndexOf2);
        }
        int lastIndexOf3 = uri2.lastIndexOf(63);
        if (lastIndexOf3 > 0) {
            uri2 = uri2.substring(0, lastIndexOf3);
        }
        int lastIndexOf4 = uri2.lastIndexOf(47);
        if (lastIndexOf4 >= 0) {
            uri2 = uri2.substring(lastIndexOf4 + 1);
        }
        return (uri2.isEmpty() || !Pattern.matches("[a-zA-Z_0-9\\.\\-\\(\\)\\%]+", uri2) || (lastIndexOf = uri2.lastIndexOf(46)) < 0) ? "" : uri2.substring(lastIndexOf + 1).toLowerCase();
    }
}
