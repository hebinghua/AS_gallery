package com.miui.gallery.util;

import com.baidu.mapapi.UIMsg;
import com.xiaomi.milab.videosdk.message.MsgType;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes2.dex */
public class MediaFile {
    public static final HashMap<String, MediaFileType> sFileTypeMap = new HashMap<>();
    public static final HashMap<String, Integer> sMimeTypeMap = new HashMap<>();
    public static final HashMap<String, Integer> sFileTypeToFormatMap = new HashMap<>();
    public static final HashMap<String, Integer> sMimeTypeToFormatMap = new HashMap<>();
    public static final HashMap<Integer, String> sFormatToMimeTypeMap = new HashMap<>();

    /* loaded from: classes2.dex */
    public static class MediaFileType {
        public final int fileType;
        public final String mimeType;

        public MediaFileType(int i, String str) {
            this.fileType = i;
            this.mimeType = str;
        }
    }

    static {
        addFileType("MP3", 1, "audio/mpeg", 12297);
        addFileType("MPGA", 1, "audio/mpeg", 12297);
        addFileType("M4A", 2, "audio/mp4", 12299);
        addFileType("WAV", 3, "audio/x-wav", 12296);
        addFileType("AMR", 4, "audio/amr");
        addFileType("AWB", 5, "audio/amr-wb");
        addFileType("WMA", 6, "audio/x-ms-wma", 47361);
        addFileType("OGG", 7, "audio/ogg", 47362);
        addFileType("OGG", 7, "application/ogg", 47362);
        addFileType("OGA", 7, "application/ogg", 47362);
        addFileType("AAC", 8, "audio/aac", 47363);
        addFileType("AAC", 8, "audio/aac-adts", 47363);
        addFileType("MKA", 9, "audio/x-matroska");
        addFileType("MID", 11, "audio/midi");
        addFileType("MIDI", 11, "audio/midi");
        addFileType("XMF", 11, "audio/midi");
        addFileType("RTTTL", 11, "audio/midi");
        addFileType("SMF", 12, "audio/sp-midi");
        addFileType("IMY", 13, "audio/imelody");
        addFileType("RTX", 11, "audio/midi");
        addFileType("OTA", 11, "audio/midi");
        addFileType("MXMF", 11, "audio/midi");
        addFileType("MPEG", 21, "video/mpeg", 12299);
        addFileType("MPG", 21, "video/mpeg", 12299);
        addFileType("MP4", 21, "video/mp4", 12299);
        addFileType("M4V", 22, "video/mp4", 12299);
        addFileType("3GP", 23, "video/3gpp", 47492);
        addFileType("3GPP", 23, "video/3gpp", 47492);
        addFileType("3G2", 24, "video/3gpp2", 47492);
        addFileType("3GPP2", 24, "video/3gpp2", 47492);
        addFileType("MKV", 27, "video/x-matroska");
        addFileType("WEBM", 30, "video/webm");
        addFileType("TS", 28, "video/mp2ts");
        addFileType("AVI", 29, "video/avi");
        addFileType("WMV", 25, "video/x-ms-wmv", 47489);
        addFileType("ASF", 26, "video/x-ms-asf");
        addFileType("JPG", 31, "image/jpeg", 14337);
        addFileType("JPEG", 31, "image/jpeg", 14337);
        addFileType("GIF", 32, "image/gif", 14343);
        addFileType("PNG", 33, "image/png", 14347);
        addFileType("BMP", 34, "image/x-ms-bmp", 14340);
        addFileType("WBMP", 35, "image/vnd.wap.wbmp");
        addFileType("WEBP", 36, "image/webp");
        addFileType("DNG", 37, "image/x-adobe-dng");
        addFileType("HEIC", 38, "image/heif", 14354);
        addFileType("HEIF", 38, "image/heif", 14354);
        addFileType("M3U", 41, "audio/x-mpegurl", 47633);
        addFileType("M3U", 41, "application/x-mpegurl", 47633);
        addFileType("PLS", 42, "audio/x-scpls", 47636);
        addFileType("WPL", 43, "application/vnd.ms-wpl", 47632);
        addFileType("M3U8", 44, "application/vnd.apple.mpegurl");
        addFileType("M3U8", 44, "audio/mpegurl");
        addFileType("M3U8", 44, "audio/x-mpegurl");
        addFileType("FL", 51, "application/x-android-drm-fl");
        addFileType("DCF", 52, "application/vnd.oma.drm.content");
        addFileType("TXT", 100, "text/plain", 12292);
        addFileType("HTM", 101, "text/html", 12293);
        addFileType("HTML", 101, "text/html", 12293);
        addFileType("PDF", 102, "application/pdf");
        addFileType("DOC", 104, "application/msword", 47747);
        addFileType("XLS", 105, "application/vnd.ms-excel", 47749);
        addFileType("PPT", 106, "application/mspowerpoint", 47750);
        addFileType("FLAC", 10, "audio/flac", 47366);
        addFileType("ZIP", 107, "application/zip");
        addFileType("MPG", 200, "video/mp2p");
        addFileType("MPEG", 200, "video/mp2p");
        addFileType("DIVX", 201, "video/divx");
        addFileType("FLV", 202, "video/flv");
        addFileType("MPD", 45, "application/dash+xml");
        addFileType("QCP", MsgType.MsgEvent.PLAYER_EVENT_TRANSCODE_CANCEL, "audio/qcelp");
        addFileType("AC3", MsgType.MsgEvent.PLAYER_EVENT_TRANSCODE_SUCCESS, "audio/ac3");
        addFileType("EC3", 305, "audio/eac3");
        addFileType("AIF", 306, "audio/x-aiff");
        addFileType("AIFF", 306, "audio/x-aiff");
        addFileType("APE", 307, "audio/x-ape");
        addMiuiFileType();
    }

    public static void addFileType(String str, int i, String str2) {
        sFileTypeMap.put(str, new MediaFileType(i, str2));
        sMimeTypeMap.put(str2, Integer.valueOf(i));
    }

    public static void addFileType(String str, int i, String str2, int i2) {
        addFileType(str, i, str2);
        sFileTypeToFormatMap.put(str, Integer.valueOf(i2));
        sMimeTypeToFormatMap.put(str2, Integer.valueOf(i2));
        sFormatToMimeTypeMap.put(Integer.valueOf(i2), str2);
    }

    public static void addMiuiFileType() {
        addFileType("FLV", 2000, "video/x-flv");
        addFileType("RM", 2001, "video/x-pn-realvideo");
        addFileType("RMVB", 2002, "video/x-pn-realvideo");
        addFileType("MOV", 2003, "video/quicktime");
        addFileType("VOB", UIMsg.m_AppUI.MSG_APP_VERSION, "video/mpeg");
        addFileType("F4V", UIMsg.m_AppUI.MSG_APP_VERSION_FORCE, "video/mp4");
        addFileType("3G2B", UIMsg.m_AppUI.MSG_APP_VERSION_COMMEND, "video/3gpp");
    }

    public static String getExtensionFromMimeType(String str) {
        for (Map.Entry<String, MediaFileType> entry : sFileTypeMap.entrySet()) {
            if (entry.getValue().mimeType.equals(str)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static MediaFileType getFileType(String str) {
        int lastIndexOf = str.lastIndexOf(46);
        if (lastIndexOf < 0) {
            return null;
        }
        return sFileTypeMap.get(str.substring(lastIndexOf + 1).toUpperCase(Locale.ROOT));
    }
}
