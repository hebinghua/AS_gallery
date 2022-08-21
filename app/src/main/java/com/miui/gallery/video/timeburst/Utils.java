package com.miui.gallery.video.timeburst;

import android.app.ActivityManager;
import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.text.TextUtils;
import android.text.format.DateFormat;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.constants.StorageConstants;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.FileUtils;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes2.dex */
public class Utils {
    public static Map<ResolutionLevel, Resolution> getSupportResolutionList(String str, int i, int i2) {
        MediaCodec mediaCodec;
        try {
            mediaCodec = MediaCodec.createEncoderByType(str);
        } catch (IOException e) {
            e.printStackTrace();
            mediaCodec = null;
        }
        if (mediaCodec == null) {
            DefaultLogger.d("BurstPhoto", "encoder is null");
            return null;
        }
        MediaCodecInfo.VideoCapabilities videoCapabilities = mediaCodec.getCodecInfo().getCapabilitiesForType(str).getVideoCapabilities();
        if (videoCapabilities != null) {
            int widthAlignment = videoCapabilities.getWidthAlignment();
            int heightAlignment = videoCapabilities.getHeightAlignment();
            long currentTimeMillis = System.currentTimeMillis();
            HashMap hashMap = new HashMap();
            ResolutionLevel resolutionLevel = ResolutionLevel.K4;
            Resolution calculateResolution = calculateResolution(resolutionLevel, i, i2, widthAlignment, heightAlignment);
            if (calculateResolution != null && videoCapabilities.isSizeSupported(calculateResolution.getWidth(), calculateResolution.getHeight())) {
                hashMap.put(resolutionLevel, calculateResolution);
                DefaultLogger.d("BurstPhoto", "[Time Burst] support 4k size %dx%d", Integer.valueOf(calculateResolution.getWidth()), Integer.valueOf(calculateResolution.getHeight()));
            }
            ResolutionLevel resolutionLevel2 = ResolutionLevel.P1080;
            Resolution calculateResolution2 = calculateResolution(resolutionLevel2, i, i2, widthAlignment, heightAlignment);
            if (calculateResolution2 != null && videoCapabilities.isSizeSupported(calculateResolution2.getWidth(), calculateResolution2.getHeight())) {
                hashMap.put(resolutionLevel2, calculateResolution2);
                DefaultLogger.d("BurstPhoto", "[Time Burst] support 1080 size %dx%d", Integer.valueOf(calculateResolution2.getWidth()), Integer.valueOf(calculateResolution2.getHeight()));
            }
            ResolutionLevel resolutionLevel3 = ResolutionLevel.P720;
            Resolution calculateResolution3 = calculateResolution(resolutionLevel3, i, i2, widthAlignment, heightAlignment);
            if (calculateResolution3 != null && videoCapabilities.isSizeSupported(calculateResolution3.getWidth(), calculateResolution3.getHeight())) {
                hashMap.put(resolutionLevel3, calculateResolution3);
                DefaultLogger.d("BurstPhoto", "[Time Burst] support 720 size %dx%d", Integer.valueOf(calculateResolution3.getWidth()), Integer.valueOf(calculateResolution3.getHeight()));
            }
            DefaultLogger.d("BurstPhoto", "[Time Burst] check size support consume %d", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            mediaCodec.release();
            return hashMap;
        }
        DefaultLogger.d("BurstPhoto", "VideoCapabilities is null");
        mediaCodec.release();
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:16:0x002d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.miui.gallery.video.timeburst.Resolution calculateResolution(com.miui.gallery.video.timeburst.ResolutionLevel r5, int r6, int r7, int r8, int r9) {
        /*
            int r0 = java.lang.Math.min(r6, r7)
            int r1 = java.lang.Math.max(r6, r7)
            float r2 = (float) r1
            float r3 = (float) r0
            float r2 = r2 / r3
            com.miui.gallery.video.timeburst.ResolutionLevel r3 = com.miui.gallery.video.timeburst.ResolutionLevel.P720
            if (r5 != r3) goto L1b
            int r3 = r5.getSmallEdge()
            if (r0 >= r3) goto L16
            goto L2a
        L16:
            int r0 = r5.getSmallEdge()
            goto L27
        L1b:
            int r1 = r5.getSmallEdge()
            if (r0 >= r1) goto L23
            r5 = 0
            return r5
        L23:
            int r0 = r5.getSmallEdge()
        L27:
            float r5 = (float) r0
            float r5 = r5 * r2
            int r1 = (int) r5
        L2a:
            if (r6 >= r7) goto L2d
            goto L30
        L2d:
            r4 = r1
            r1 = r0
            r0 = r4
        L30:
            com.miui.gallery.video.timeburst.Resolution r5 = new com.miui.gallery.video.timeburst.Resolution
            int r6 = r0 % r8
            int r0 = r0 - r6
            int r6 = r1 % r9
            int r1 = r1 - r6
            r5.<init>(r0, r1)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.video.timeburst.Utils.calculateResolution(com.miui.gallery.video.timeburst.ResolutionLevel, int, int, int, int):com.miui.gallery.video.timeburst.Resolution");
    }

    public static long getPhoneTotalMem(Context context) {
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ((ActivityManager) context.getSystemService("activity")).getMemoryInfo(memoryInfo);
        return memoryInfo.totalMem;
    }

    public static String createOutputFile(String str) {
        String format = String.format(Locale.US, "VID_%s.%s", DateFormat.format("yyyyMMdd_HHmmss", System.currentTimeMillis()), "mp4");
        if (!TextUtils.isEmpty(str) || (str = StorageUtils.getPathInPriorStorage(StorageConstants.RELATIVE_DIRECTORY_CREATIVE)) != null) {
            StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
            String concat = BaseFileUtils.concat(str, format);
            IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.INSERT;
            if (!storageStrategyManager.checkPermission(concat, permission).granted) {
                str = StorageUtils.getPathInPrimaryStorage(StorageUtils.getRelativePath(GalleryApp.sGetAndroidContext(), str));
                if (!StorageSolutionProvider.get().checkPermission(BaseFileUtils.concat(str, format), permission).granted && (str = StorageUtils.getPathInPriorStorage(StorageConstants.RELATIVE_DIRECTORY_CREATIVE)) == null) {
                    return null;
                }
                StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("BurstPhoto", "createOutputFile"));
            }
            return FileUtils.forceCreate(str, format, 0).getAbsolutePath();
        }
        return null;
    }
}
