package com.cdv.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaExtractor;
import android.media.MediaMuxer;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.util.Log;

/* loaded from: classes.dex */
public class NvAndroidUtils {
    private static final String TAG = "Meicam";

    public static long getSystemMemorySizeInBytes(Context context) {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ((ActivityManager) context.getSystemService("activity")).getMemoryInfo(memoryInfo);
        return memoryInfo.totalMem;
    }

    public static MediaExtractor createMediaExtractorFromMediaFilePath(Context context, String str) {
        MediaExtractor mediaExtractor;
        try {
            mediaExtractor = new MediaExtractor();
        } catch (Exception e) {
            e = e;
            mediaExtractor = null;
        }
        try {
            if (str.startsWith("content://")) {
                ParcelFileDescriptor openFileDescriptor = context.getContentResolver().openFileDescriptor(Uri.parse(str), "r");
                mediaExtractor.setDataSource(openFileDescriptor.getFileDescriptor());
                openFileDescriptor.close();
            } else if (str.startsWith("assets:/")) {
                AssetFileDescriptor openFd = context.getAssets().openFd(str.substring(8));
                mediaExtractor.setDataSource(openFd.getFileDescriptor(), openFd.getStartOffset(), openFd.getLength());
                openFd.close();
            } else {
                mediaExtractor.setDataSource(str);
            }
            return mediaExtractor;
        } catch (Exception e2) {
            e = e2;
            Log.e(TAG, "" + e.getMessage());
            e.printStackTrace();
            if (mediaExtractor != null) {
                mediaExtractor.release();
            }
            return null;
        }
    }

    public static MediaMuxer createMediaMuxerFromContentUrl(Context context, String str) {
        if (Build.VERSION.SDK_INT < 26) {
            return null;
        }
        try {
            ParcelFileDescriptor openFileDescriptor = context.getContentResolver().openFileDescriptor(Uri.parse(str), "rwt");
            MediaMuxer mediaMuxer = new MediaMuxer(openFileDescriptor.getFileDescriptor(), 0);
            openFileDescriptor.close();
            return mediaMuxer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int openFdForContentUrl(Context context, String str, String str2) {
        try {
            return context.getContentResolver().openFileDescriptor(Uri.parse(str), str2).detachFd();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static String getMimeTypeFromContentUrl(Context context, String str) {
        try {
            return context.getContentResolver().getType(Uri.parse(str));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
