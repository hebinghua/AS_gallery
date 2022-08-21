package com.miui.gallery.vlog.rule;

import com.nexstreaming.nexeditorsdk.BuildConfig;
import java.io.File;
import java.util.Collection;
import java.util.Map;
import org.jcodec.containers.mp4.MP4Util;
import org.jcodec.containers.mp4.boxes.MetaBox;
import org.jcodec.containers.mp4.boxes.MetaValue;

/* loaded from: classes2.dex */
public class Util {
    public static long milliSecond(double d) {
        return (long) (d * 1000.0d);
    }

    public static <T> boolean isEmpty(Collection<T> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean is8KVideo(String str, int i, int i2) {
        return is8KVideo(getVideoKeyedMeta(str), i, i2);
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x005a, code lost:
        if (r10.containsKey("rotation-degrees") == false) goto L25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x005c, code lost:
        r8 = r10.getInteger("rotation-degrees");
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0060, code lost:
        r5.setFrameRate(r10.getInteger("frame-rate"));
        r0 = r10.getInteger(com.nexstreaming.nexeditorsdk.nexExportFormat.TAG_FORMAT_HEIGHT);
        r2 = r10.getInteger(com.nexstreaming.nexeditorsdk.nexExportFormat.TAG_FORMAT_WIDTH);
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0071, code lost:
        if ((r8 % com.nexstreaming.nexeditorsdk.nexClip.kClip_Rotate_180) != 0) goto L36;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x0073, code lost:
        r3 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0075, code lost:
        r3 = r0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0076, code lost:
        r5.setWidth(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x007b, code lost:
        if ((r8 % com.nexstreaming.nexeditorsdk.nexClip.kClip_Rotate_180) != 0) goto L35;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x007e, code lost:
        r0 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x007f, code lost:
        r5.setHeight(r0);
        r5.setDuration(r10.getLong("durationUs"));
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static com.miui.gallery.vlog.rule.VideoInfo extractVideoInfo(java.lang.String r13) {
        /*
            java.lang.String r0 = "rotation-degrees"
            java.lang.String r1 = "durationUs"
            java.lang.String r2 = "height"
            java.lang.String r3 = "width"
            java.lang.String r4 = "frame-rate"
            com.miui.gallery.vlog.rule.VideoInfo r5 = new com.miui.gallery.vlog.rule.VideoInfo
            r5.<init>()
            boolean r6 = android.text.TextUtils.isEmpty(r13)
            if (r6 == 0) goto L16
            return r5
        L16:
            android.media.MediaExtractor r6 = new android.media.MediaExtractor
            r6.<init>()
            r6.setDataSource(r13)     // Catch: java.lang.Throwable -> L8d java.lang.Exception -> L8f
            int r7 = r6.getTrackCount()     // Catch: java.lang.Throwable -> L8d java.lang.Exception -> L8f
            r8 = 0
            r9 = r8
        L24:
            if (r9 >= r7) goto L93
            android.media.MediaFormat r10 = r6.getTrackFormat(r9)     // Catch: java.lang.Throwable -> L8d java.lang.Exception -> L8f
            java.lang.String r11 = "mime"
            java.lang.String r11 = r10.getString(r11)     // Catch: java.lang.Throwable -> L8d java.lang.Exception -> L8f
            boolean r12 = android.text.TextUtils.isEmpty(r11)     // Catch: java.lang.Throwable -> L8d java.lang.Exception -> L8f
            if (r12 != 0) goto L8a
            java.lang.String r12 = "video/"
            boolean r11 = r11.startsWith(r12)     // Catch: java.lang.Throwable -> L8d java.lang.Exception -> L8f
            if (r11 == 0) goto L8a
            boolean r11 = r10.containsKey(r4)     // Catch: java.lang.Throwable -> L8d java.lang.Exception -> L8f
            if (r11 == 0) goto L8a
            boolean r11 = r10.containsKey(r3)     // Catch: java.lang.Throwable -> L8d java.lang.Exception -> L8f
            if (r11 == 0) goto L8a
            boolean r11 = r10.containsKey(r2)     // Catch: java.lang.Throwable -> L8d java.lang.Exception -> L8f
            if (r11 == 0) goto L8a
            boolean r11 = r10.containsKey(r1)     // Catch: java.lang.Throwable -> L8d java.lang.Exception -> L8f
            if (r11 == 0) goto L8a
            boolean r7 = r10.containsKey(r0)     // Catch: java.lang.Throwable -> L8d java.lang.Exception -> L8f
            if (r7 == 0) goto L60
            int r8 = r10.getInteger(r0)     // Catch: java.lang.Throwable -> L8d java.lang.Exception -> L8f
        L60:
            int r0 = r10.getInteger(r4)     // Catch: java.lang.Throwable -> L8d java.lang.Exception -> L8f
            r5.setFrameRate(r0)     // Catch: java.lang.Throwable -> L8d java.lang.Exception -> L8f
            int r0 = r10.getInteger(r2)     // Catch: java.lang.Throwable -> L8d java.lang.Exception -> L8f
            int r2 = r10.getInteger(r3)     // Catch: java.lang.Throwable -> L8d java.lang.Exception -> L8f
            int r3 = r8 % 180
            if (r3 != 0) goto L75
            r3 = r2
            goto L76
        L75:
            r3 = r0
        L76:
            r5.setWidth(r3)     // Catch: java.lang.Throwable -> L8d java.lang.Exception -> L8f
            int r8 = r8 % 180
            if (r8 != 0) goto L7e
            goto L7f
        L7e:
            r0 = r2
        L7f:
            r5.setHeight(r0)     // Catch: java.lang.Throwable -> L8d java.lang.Exception -> L8f
            long r0 = r10.getLong(r1)     // Catch: java.lang.Throwable -> L8d java.lang.Exception -> L8f
            r5.setDuration(r0)     // Catch: java.lang.Throwable -> L8d java.lang.Exception -> L8f
            goto L93
        L8a:
            int r9 = r9 + 1
            goto L24
        L8d:
            r13 = move-exception
            goto La2
        L8f:
            r0 = move-exception
            r0.printStackTrace()     // Catch: java.lang.Throwable -> L8d
        L93:
            r6.release()
            java.lang.String r0 = r5.toString()
            java.lang.String r1 = "Util"
            java.lang.String r2 = "path [%s] %s"
            com.miui.gallery.util.logger.DefaultLogger.d(r1, r2, r13, r0)
            return r5
        La2:
            r6.release()
            throw r13
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.vlog.rule.Util.extractVideoInfo(java.lang.String):com.miui.gallery.vlog.rule.VideoInfo");
    }

    public static Map<String, MetaValue> getVideoKeyedMeta(String str) {
        try {
            MetaBox parseMeta = MP4Util.parseMeta(new File(str));
            if (parseMeta == null) {
                return null;
            }
            return parseMeta.getKeyedMeta();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean is8KVideo(Map<String, MetaValue> map, int i, int i2) {
        return isCaptureByXiaomi(map) && is8KResolution(i, i2);
    }

    public static boolean is8KResolution(int i, int i2) {
        return Math.min(i, i2) >= 4320 || Math.max(i, i2) >= 7680;
    }

    public static boolean isCaptureByXiaomi(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.android.manufacturer")) == null || !BuildConfig.KM_PROJECT.equalsIgnoreCase(metaValue.getString())) ? false : true;
    }
}
