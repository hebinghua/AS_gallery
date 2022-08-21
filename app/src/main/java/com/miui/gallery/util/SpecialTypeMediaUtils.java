package com.miui.gallery.util;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.util.FileSize;
import com.google.common.collect.Sets;
import com.miui.extraphoto.sdk.ExtraPhotoSDK;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.cloudcontrol.strategies.ScannerStrategy;
import com.miui.gallery.provider.cache.BurstInfo;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.BuildConfig;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import miuix.core.util.SoftReferenceSingleton;
import org.jcodec.containers.mp4.MP4Util;
import org.jcodec.containers.mp4.boxes.MetaBox;
import org.jcodec.containers.mp4.boxes.MetaValue;

/* loaded from: classes2.dex */
public class SpecialTypeMediaUtils {
    public static final Map<Long, FlagTypeParser> sFlagParser;
    public static final List<FlagTypeParser> sNeedDynamicCheckFlagParser;
    public static final boolean DEBUG_ENABLE = Log.isLoggable("SpecialTypeMedia", 3);
    public static Set<String> mVideoCompressPathSet = Sets.newConcurrentHashSet();
    public static final SoftReferenceSingleton<ScannerStrategy.SpecialTypeMediaStrategy> SPECIAL_TYPE_MEDIA_STRATEGY = new SoftReferenceSingleton<ScannerStrategy.SpecialTypeMediaStrategy>() { // from class: com.miui.gallery.util.SpecialTypeMediaUtils.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // miuix.core.util.SoftReferenceSingleton
        /* renamed from: createInstance */
        public ScannerStrategy.SpecialTypeMediaStrategy mo2622createInstance() {
            ScannerStrategy.SpecialTypeMediaStrategy specialTypeMediaStrategy = CloudControlStrategyHelper.getSpecialTypeMediaStrategy();
            if (SpecialTypeMediaUtils.DEBUG_ENABLE) {
                DefaultLogger.d("SpecialTypeMediaUtils", specialTypeMediaStrategy);
            }
            return specialTypeMediaStrategy;
        }
    };

    /* loaded from: classes2.dex */
    public interface FlagTypeParser {
        Long getSupportFlags();

        long parseFlags(String str);
    }

    public static boolean isBurstPhoto(long j) {
        return (j & 64) != 0;
    }

    public static boolean isTimeBurstPhoto(long j) {
        return (j & 8388608) != 0;
    }

    public static long parseMTSpecialAITypeByValue(int i) {
        if (i != 5) {
            if (i == 9) {
                return 256L;
            }
            if (i == 10) {
                return 512L;
            }
            switch (i) {
                case 13:
                    return FileSize.KB_COEFFICIENT;
                case 14:
                    return 2048L;
                case 15:
                    return 4096L;
                default:
                    return 0L;
            }
        }
        return 128L;
    }

    public static int tryGetHFRIndicatorResId(long j) {
        if ((FileSize.MB_COEFFICIENT & j) != 0) {
            return R.drawable.type_indicator_video_compress;
        }
        if ((4 & j) != 0 || (8 & j) != 0 || (4503599627370496L & j) != 0 || (9007199254740992L & j) != 0 || (18014398509481984L & j) != 0 || (36028797018963968L & j) != 0) {
            return R.drawable.type_indicator_hfr;
        }
        if ((72057594037927936L & j) != 0 || (134217728 & j) != 0 || (16 & j) != 0 || (67108864 & j) != 0) {
            return R.drawable.type_indicator_extra_hfr;
        }
        if ((16384 & j) != 0) {
            return R.drawable.type_indicator_subtitle;
        }
        if ((j & 131072) == 0) {
            return 0;
        }
        return R.drawable.type_indicator_log;
    }

    static {
        HashMap hashMap = new HashMap(2, 1.0f);
        sFlagParser = hashMap;
        ArrayList arrayList = new ArrayList(2);
        sNeedDynamicCheckFlagParser = arrayList;
        FlagParserUsePath flagParserUsePath = new FlagParserUsePath();
        hashMap.put(flagParserUsePath.getSupportFlags(), flagParserUsePath);
        FlagParserUseExif flagParserUseExif = new FlagParserUseExif();
        hashMap.put(flagParserUseExif.getSupportFlags(), flagParserUseExif);
        arrayList.add(new BurstPhotoParser());
    }

    public static long parseFlagsForImage(String str) {
        long currentTimeMillis = System.currentTimeMillis();
        long j = 0;
        if (!TextUtils.isEmpty(str)) {
            long j2 = 0;
            for (FlagTypeParser flagTypeParser : sFlagParser.values()) {
                long parseFlags = flagTypeParser.parseFlags(str);
                if (parseFlags != 0) {
                    j2 |= parseFlags;
                }
            }
            j = j2;
        }
        if (DEBUG_ENABLE) {
            DefaultLogger.d("SpecialTypeMediaUtils", "parseFlagsForImage costs [%dms], path [%s]", Long.valueOf(System.currentTimeMillis() - currentTimeMillis), str);
        }
        return j;
    }

    public static boolean isMTSpecialAITypeSupport() {
        return Build.DEVICE.equalsIgnoreCase("vela");
    }

    public static int parseSpecialTypeDescriptionRes(long j) {
        if (j == 0) {
            return 0;
        }
        DefaultLogger.d("SpecialTypeMediaUtils", "parseSpecialTypeDescriptionRes flag:%d", Long.valueOf(j));
        if ((262144 & j) != 0) {
            return R.string.special_type_8k;
        }
        if ((4194304 & j) != 0) {
            return R.string.special_type_real_8k;
        }
        if ((2097152 & j) != 0) {
            return R.string.special_type_ai_audio;
        }
        if ((131072 & j) != 0) {
            return R.string.special_type_log;
        }
        if ((16777216 & j) != 0) {
            return R.string.special_type_hdr10;
        }
        if ((FileAppender.DEFAULT_BUFFER_SIZE & j) != 0) {
            return R.string.special_type_raw;
        }
        if ((33554432 & j) != 0) {
            return R.string.special_type_heif;
        }
        if (isMTSpecialAITypeSupport()) {
            if ((128 & j) != 0) {
                return R.string.special_type_hdr;
            }
            if ((256 & j) != 0) {
                return R.string.special_type_night_beauty;
            }
            if ((512 & j) != 0) {
                return R.string.special_type_night_mode;
            }
            if ((FileSize.KB_COEFFICIENT & j) != 0) {
                return R.string.special_type_portrait;
            }
            if ((2048 & j) != 0) {
                return R.string.special_type_eye_repair;
            }
            if ((j & 4096) != 0) {
                return R.string.special_type_wide_angle;
            }
        }
        return 0;
    }

    public static int parseSpecialTypeNameRes(long j, boolean z) {
        if (j == 0) {
            return 0;
        }
        DefaultLogger.d("SpecialTypeMediaUtils", "parseSpecialTypeNameRes flag:%d，parseChild:%b", Long.valueOf(j), Boolean.valueOf(z));
        if ((262144 & j) != 0) {
            return R.string.special_type_8k;
        }
        if ((4194304 & j) != 0) {
            return R.string.special_type_real_8k;
        }
        if ((2097152 & j) != 0) {
            return R.string.special_type_ai_audio;
        }
        if ((131072 & j) != 0) {
            return R.string.special_type_log;
        }
        if ((16777216 & j) != 0) {
            return R.string.special_type_hdr10;
        }
        if ((FileAppender.DEFAULT_BUFFER_SIZE & j) != 0) {
            return R.string.special_type_raw;
        }
        if ((33554432 & j) != 0) {
            return R.string.special_type_heif;
        }
        if ((268435456 & j) != 0) {
            return R.string.media_type_portrait_photo;
        }
        if ((536870912 & j) != 0) {
            return R.string.media_type_front_photo;
        }
        if ((FileSize.GB_COEFFICIENT & j) != 0) {
            return R.string.media_type_pano_photo;
        }
        if ((2147483648L & j) != 0 || (8796093022208L & j) != 0 || (4398046511104L & j) != 0) {
            return R.string.media_type_clone_photo;
        }
        if ((4294967296L & j) != 0) {
            return R.string.media_type_live_vv;
        }
        if (!z && ((j & 64) != 0 || (8388608 & j) != 0)) {
            return R.string.media_type_burst_photo;
        }
        if ((65536 & j) != 0) {
            return R.string.media_type_doc_photo;
        }
        if ((32 & j) != 0) {
            return R.string.media_type_motion_photo;
        }
        if ((8589934592L & j) != 0) {
            return R.string.media_type_gif;
        }
        if ((17179869184L & j) != 0) {
            return R.string.media_type_slow_motion_video;
        }
        if ((34359738368L & j) != 0) {
            return R.string.media_type_fast_motion_video;
        }
        if (z && (j & 64) != 0) {
            return R.string.media_type_burst_photo;
        }
        return 0;
    }

    public static int parseSpecialTypeCoverRes(long j) {
        DefaultLogger.d("SpecialTypeMediaUtils", "parseSpecialTypeCoverRes flag:%d", Long.valueOf(j));
        if ((268435456 & j) != 0) {
            return R.drawable.icon_media_type_portrait;
        }
        if ((FileAppender.DEFAULT_BUFFER_SIZE & j) != 0) {
            return R.drawable.icon_media_type_raw;
        }
        if ((536870912 & j) != 0) {
            return R.drawable.icon_media_type_front;
        }
        if ((FileSize.GB_COEFFICIENT & j) != 0) {
            return R.drawable.icon_media_type_pano;
        }
        if ((2147483648L & j) != 0) {
            return R.drawable.icon_media_type_clone;
        }
        if ((4294967296L & j) != 0) {
            return R.drawable.icon_media_type_vlog;
        }
        if ((64 & j) != 0 || (8388608 & j) != 0) {
            return R.drawable.icon_media_type_burst;
        }
        if ((65536 & j) != 0) {
            return R.drawable.icon_media_type_doc;
        }
        if ((32 & j) != 0) {
            return R.drawable.icon_media_type_motion;
        }
        if ((8589934592L & j) != 0) {
            return R.drawable.icon_media_type_gif;
        }
        if ((17179869184L & j) != 0) {
            return R.drawable.icon_media_type_slow_motion;
        }
        if ((j & 34359738368L) == 0) {
            return 0;
        }
        return R.drawable.icon_media_type_fast_motion;
    }

    public static int parseSpecialTypeId(long j, boolean z) {
        if (j == 0) {
            return 0;
        }
        DefaultLogger.d("SpecialTypeMediaUtils", "parseSpecialTypeId flag:%d,parseChild:%b", Long.valueOf(j), Boolean.valueOf(z));
        if ((268435456 & j) != 0) {
            return R.id.media_type_portrait_photo;
        }
        if ((536870912 & j) != 0) {
            return R.id.media_type_front_photo;
        }
        if ((FileSize.GB_COEFFICIENT & j) != 0) {
            return R.id.media_type_pano_photo;
        }
        if ((2147483648L & j) != 0 || (8796093022208L & j) != 0 || (4398046511104L & j) != 0) {
            return R.id.media_type_clone_photo;
        }
        if ((4294967296L & j) != 0) {
            return R.id.media_type_live_vv;
        }
        if (!z && ((j & 64) != 0 || (j & 8388608) != 0)) {
            return R.id.media_type_burst_photo;
        }
        if ((65536 & j) != 0) {
            return R.id.media_type_doc_photo;
        }
        if ((32 & j) != 0) {
            return R.id.media_type_motion_photo;
        }
        if ((8589934592L & j) != 0) {
            return R.id.media_type_gif;
        }
        if ((17179869184L & j) != 0) {
            return R.id.media_type_slow_motion_video;
        }
        if ((34359738368L & j) != 0) {
            return R.id.media_type_fast_motion_video;
        }
        if ((FileAppender.DEFAULT_BUFFER_SIZE & j) != 0) {
            return R.id.media_type_raw;
        }
        if (z) {
            if ((64 & j) != 0) {
                return R.id.media_type_burst_photo;
            }
            if ((j & 8388608) != 0) {
                return R.id.media_type_time_burst_photo;
            }
        }
        return 0;
    }

    public static boolean isHDR10(Uri uri) {
        return MediaExtractorUtils.uri(StaticContext.sGetAndroidContext(), uri, null).getCodecProfile() == 4096;
    }

    /* JADX WARN: Removed duplicated region for block: B:47:0x00b7  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00bb  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x00fb  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0102  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x012e  */
    /* JADX WARN: Removed duplicated region for block: B:83:0x0136  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static long parseFlagsForVideo(java.lang.String r9, com.miui.gallery.cloudcontrol.strategies.ScannerStrategy.SpecialTypeMediaStrategy r10) {
        /*
            Method dump skipped, instructions count: 508
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.util.SpecialTypeMediaUtils.parseFlagsForVideo(java.lang.String, com.miui.gallery.cloudcontrol.strategies.ScannerStrategy$SpecialTypeMediaStrategy):long");
    }

    public static long parseFlagsForVideo(String str) {
        return parseFlagsForVideo(str, SPECIAL_TYPE_MEDIA_STRATEGY.get());
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

    public static boolean isCaptureByXiaomi(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.android.manufacturer")) == null || !BuildConfig.KM_PROJECT.equalsIgnoreCase(metaValue.getString())) ? false : true;
    }

    public static boolean is3840FpsVideo(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.xiaomi.capture_framerate")) == null || metaValue.getInt() != 3840) ? false : true;
    }

    public static boolean is3840VideoEditable(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.xiaomi.capture_origin_track")) == null || metaValue.getInt() < 0) ? false : true;
    }

    public static boolean is1920FpsVideo(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.xiaomi.capture_framerate")) == null || metaValue.getInt() != 1920) ? false : true;
    }

    public static boolean is1920VideoEditable(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.xiaomi.capture_origin_track")) == null || metaValue.getInt() < 0) ? false : true;
    }

    public static boolean is960FpsVideo(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.xiaomi.capture_framerate")) == null || metaValue.getInt() != 960) ? false : true;
    }

    public static boolean is960VideoEditable(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.xiaomi.capture_origin_track")) == null || metaValue.getInt() < 0) ? false : true;
    }

    public static boolean is480FpsVideo(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.xiaomi.capture_framerate")) == null || metaValue.getInt() != 480) ? false : true;
    }

    public static boolean is480VideoEditable(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.xiaomi.capture_origin_track")) == null || metaValue.getInt() < 0) ? false : true;
    }

    public static boolean isVideoSupportSubtitle(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.xiaomi.support_subtitle")) == null || metaValue.getInt() != 1) ? false : true;
    }

    public static boolean isVideoSupportTags(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.xiaomi.support_tags")) == null || metaValue.getInt() != 1) ? false : true;
    }

    public static boolean is8KVideo(Map<String, MetaValue> map, int i, int i2) {
        return isCaptureByXiaomi(map) && is8KResolution(i, i2);
    }

    public static boolean is8KResolution(int i, int i2) {
        return Math.min(i, i2) >= 4320 || Math.max(i, i2) >= 7680;
    }

    public static boolean is2KResolution(int i, int i2) {
        return Math.min(i, i2) >= 1080 || Math.max(i, i2) >= 1920;
    }

    public static boolean isReal8k(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.xiaomi.real_8k")) == null || metaValue.getInt() != 1) ? false : true;
    }

    public static boolean isHDR10NeedConvertColor(Uri uri) {
        MetaValue metaValue;
        Map<String, MetaValue> videoKeyedMeta = getVideoKeyedMeta(UriUtils.getFilePathWithUri(GalleryApp.sGetAndroidContext(), uri));
        return videoKeyedMeta == null || (metaValue = videoKeyedMeta.get("com.xiaomi.preview_video_cover")) == null || metaValue.getInt() != 1;
    }

    public static boolean isAIAudio(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.xiaomi.ai_audio")) == null || metaValue.getInt() != 1) ? false : true;
    }

    public static boolean isLogVideo(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.xiaomi.record_log")) == null || metaValue.getInt() != 1) ? false : true;
    }

    public static boolean isMovieVideo(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.xiaomi.record_mimovie")) == null || metaValue.getInt() != 1) ? false : true;
    }

    public static boolean isSlowMomentVideo(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.xiaomi.slow_moment")) == null || metaValue.getInt() != 1) ? false : true;
    }

    public static boolean isFastMomentVideo(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.xiaomi.fast_moment")) == null || metaValue.getInt() != 1) ? false : true;
    }

    public static boolean isLiveVideo(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.xiaomi.live_vv")) == null || metaValue.getInt() != 1) ? false : true;
    }

    public static boolean isCloneVideo(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.xiaomi.mode_clone_video")) == null || metaValue.getInt() != 1) ? false : true;
    }

    public static boolean isCloneCopyVideo(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.xiaomi.mode_clone_mcopy")) == null || metaValue.getInt() != 1) ? false : true;
    }

    public static boolean isFilmDollyZoomVideo(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.xiaomi.mode_dollyzoom")) == null || metaValue.getInt() != 1) ? false : true;
    }

    public static boolean isFilmSlowShutterVideo(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.xiaomi.film_slowshutter")) == null || metaValue.getInt() != 1) ? false : true;
    }

    public static boolean isFilmExposureDelayVideo(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.xiaomi.film_exposuredelay")) == null || metaValue.getInt() != 1) ? false : true;
    }

    public static boolean isFilmDreamVideo(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.xiaomi.film_dream")) == null || metaValue.getInt() != 1) ? false : true;
    }

    public static boolean isFilmTimeFreezeVideo(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.xiaomi.film_timefreeze")) == null || metaValue.getInt() != 1) ? false : true;
    }

    public static boolean isMiLiveVideo(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.xiaomi.mi_live")) == null || metaValue.getInt() != 1) ? false : true;
    }

    public static boolean isNightVideo(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.xiaomi.night_video")) == null || metaValue.getInt() != 1) ? false : true;
    }

    public static boolean isProVideo(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.xiaomi.pro_video")) == null || metaValue.getInt() != 1) ? false : true;
    }

    public static boolean isMiMoJiVideo(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.xiaomi.mimoji_module")) == null || metaValue.getInt() != 1) ? false : true;
    }

    public static boolean isDuoVideo(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.xiaomi.duo_video")) == null || metaValue.getInt() != 1) ? false : true;
    }

    public static boolean isDuoVideoRemote(Map<String, MetaValue> map) {
        MetaValue metaValue;
        return (map == null || (metaValue = map.get("com.xiaomi.duo_video_remote")) == null || metaValue.getInt() != 1) ? false : true;
    }

    public static boolean is1920VideoEditable(String str) {
        return is1920VideoEditable(getVideoKeyedMeta(str));
    }

    public static boolean is3840VideoEditable(String str) {
        return is3840VideoEditable(getVideoKeyedMeta(str));
    }

    public static boolean is960VideoEditable(String str) {
        return is960VideoEditable(getVideoKeyedMeta(str));
    }

    public static boolean is480VideoEditable(String str) {
        return is480VideoEditable(getVideoKeyedMeta(str));
    }

    public static boolean isRefocusSupported(Context context, long j) {
        return (j & 1) != 0 && ExtraPhotoSDK.isDeviceSupportRefocus(context);
    }

    public static boolean isMotionPhoto(Context context, long j) {
        return (j & 32) != 0 && ExtraPhotoSDK.isDeviceSupportMotionPhoto(context);
    }

    public static boolean isDocPhoto(Context context, long j) {
        return (j & 65536) != 0 && ExtraPhotoSDK.isDeviceSupportDocPhoto(context);
    }

    public static List<Integer> getSpecialTypeEnterIconId(Context context, long j, long j2) {
        ArrayList arrayList = new ArrayList();
        if ((8388608 & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.drawable.time_burst_btn));
        } else if ((64 & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.drawable.burst_photo_btn_large));
        } else if ((32 & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.drawable.motion_photo_btn));
        } else if ((1 & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.drawable.blur_refocus_btn));
        } else if ((4 & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.drawable.slow_motion_btn));
        } else if ((8 & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.drawable.slow_motion_btn));
        } else if ((4503599627370496L & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.drawable.slow_motion_btn));
        } else if ((9007199254740992L & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.drawable.slow_motion_btn));
        } else if ((18014398509481984L & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.drawable.slow_motion_btn));
        } else if ((36028797018963968L & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.drawable.slow_motion_btn));
        } else if ((72057594037927936L & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.drawable.super_slow_motion_btn));
        } else if ((134217728 & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.drawable.super_slow_motion_btn));
        } else if ((16 & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.drawable.super_slow_motion_btn));
        } else if ((67108864 & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.drawable.super_slow_motion_btn));
        } else if ((16384 & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.drawable.edit_subtitle_btn));
        } else if ((32768 & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.drawable.video_tags_btn));
        } else if ((j & 65536) != 0) {
            arrayList.add(Integer.valueOf((int) R.drawable.doc_photo_btn));
        }
        if (j2 != -1 && (17592186044416L & j2) != 0) {
            arrayList.add(Integer.valueOf((int) R.drawable.watermark_btn));
        }
        return arrayList;
    }

    public static List<Integer> getSpecialTypeEnterTextId(long j, long j2) {
        ArrayList arrayList = new ArrayList();
        if ((8388608 & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.string.time_burst_photo_enter));
        } else if ((64 & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.string.burst_photo_enter));
        } else if ((32 & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.string.motion_photo_enter));
        } else if ((1 & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.string.blur_refocus_enter));
        } else if ((4 & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.string.slow_motion_enter));
        } else if ((8 & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.string.slow_motion_enter));
        } else if ((4503599627370496L & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.string.slow_motion_enter));
        } else if ((9007199254740992L & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.string.slow_motion_enter));
        } else if ((18014398509481984L & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.string.slow_motion_enter));
        } else if ((36028797018963968L & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.string.slow_motion_enter));
        } else if ((67108864 & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.string.super_slow_motion_enter));
        } else if ((16 & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.string.super_slow_motion_enter));
        } else if ((134217728 & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.string.super_slow_motion_enter));
        } else if ((72057594037927936L & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.string.super_slow_motion_enter));
        } else if ((16384 & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.string.edit_subtitle_enter));
        } else if ((32768 & j) != 0) {
            arrayList.add(Integer.valueOf((int) R.string.video_tags_enter));
        } else if ((j & 65536) != 0) {
            arrayList.add(Integer.valueOf((int) R.string.doc_photo_enter));
        }
        if (j2 != -1 && (17592186044416L & j2) != 0) {
            arrayList.add(Integer.valueOf((int) R.string.watermark_enter));
        }
        return arrayList;
    }

    public static List<SpecialEnterIconAndText> getSpecialTypeEnterIconAndText(long j, long j2) {
        ArrayList arrayList = new ArrayList();
        if (j != -1) {
            if ((j & 8388608) != 0) {
                arrayList.add(new SpecialEnterIconAndText(8388608L, R.drawable.time_burst_btn, R.string.time_burst_photo_enter));
            } else if ((j & 64) != 0) {
                arrayList.add(new SpecialEnterIconAndText(64L, R.drawable.burst_photo_btn_large, R.string.burst_photo_enter));
            } else if ((j & 32) != 0) {
                arrayList.add(new SpecialEnterIconAndText(32L, R.drawable.motion_photo_btn, R.string.motion_photo_enter));
            } else if ((j & 1) != 0) {
                arrayList.add(new SpecialEnterIconAndText(1L, R.drawable.blur_refocus_btn, R.string.blur_refocus_enter));
            } else if ((j & 4) != 0) {
                arrayList.add(new SpecialEnterIconAndText(4L, R.drawable.slow_motion_btn, R.string.slow_motion_enter));
            } else if ((j & 8) != 0) {
                arrayList.add(new SpecialEnterIconAndText(8L, R.drawable.slow_motion_btn, R.string.slow_motion_enter));
            } else if ((j & 4503599627370496L) != 0) {
                arrayList.add(new SpecialEnterIconAndText(4503599627370496L, R.drawable.slow_motion_btn, R.string.slow_motion_enter));
            } else if ((j & 9007199254740992L) != 0) {
                arrayList.add(new SpecialEnterIconAndText(9007199254740992L, R.drawable.slow_motion_btn, R.string.slow_motion_enter));
            } else if ((j & 18014398509481984L) != 0) {
                arrayList.add(new SpecialEnterIconAndText(18014398509481984L, R.drawable.slow_motion_btn, R.string.slow_motion_enter));
            } else if ((j & 36028797018963968L) != 0) {
                arrayList.add(new SpecialEnterIconAndText(36028797018963968L, R.drawable.slow_motion_btn, R.string.slow_motion_enter));
            } else if ((j & 67108864) != 0) {
                arrayList.add(new SpecialEnterIconAndText(67108864L, R.drawable.super_slow_motion_btn, R.string.super_slow_motion_enter));
            } else if ((j & 16) != 0) {
                arrayList.add(new SpecialEnterIconAndText(16L, R.drawable.super_slow_motion_btn, R.string.super_slow_motion_enter));
            } else if ((j & 134217728) != 0) {
                arrayList.add(new SpecialEnterIconAndText(134217728L, R.drawable.super_slow_motion_btn, R.string.super_slow_motion_enter));
            } else if ((j & 72057594037927936L) != 0) {
                arrayList.add(new SpecialEnterIconAndText(72057594037927936L, R.drawable.super_slow_motion_btn, R.string.super_slow_motion_enter));
            } else if ((j & 16384) != 0) {
                arrayList.add(new SpecialEnterIconAndText(16384L, R.drawable.edit_subtitle_btn, R.string.edit_subtitle_enter));
            } else if ((j & 32768) != 0) {
                arrayList.add(new SpecialEnterIconAndText(32768L, R.drawable.video_tags_btn, R.string.video_tags_enter));
            } else if ((j & 65536) != 0) {
                arrayList.add(new SpecialEnterIconAndText(65536L, R.drawable.doc_photo_btn, R.string.doc_photo_enter));
            }
        }
        if (j2 >= 0 && (j2 & 17592186044416L) != 0) {
            arrayList.add(new SpecialEnterIconAndText(17592186044416L, R.drawable.watermark_btn, R.string.watermark_enter));
        }
        return arrayList;
    }

    /* JADX WARN: Removed duplicated region for block: B:5:0x000a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean needToBeHiddenWhenInLandMultiWindowMode(java.util.List<com.miui.gallery.util.SpecialTypeMediaUtils.SpecialEnterIconAndText> r3) {
        /*
            java.util.Iterator r3 = r3.iterator()
        L4:
            boolean r0 = r3.hasNext()
            if (r0 == 0) goto L36
            java.lang.Object r0 = r3.next()
            com.miui.gallery.util.SpecialTypeMediaUtils$SpecialEnterIconAndText r0 = (com.miui.gallery.util.SpecialTypeMediaUtils.SpecialEnterIconAndText) r0
            int r1 = r0.getTextId()
            r2 = 2131889064(0x7f120ba8, float:1.941278E38)
            if (r1 == r2) goto L34
            int r1 = r0.getTextId()
            r2 = 2131889200(0x7f120c30, float:1.9413057E38)
            if (r1 == r2) goto L34
            int r1 = r0.getTextId()
            r2 = 2131887268(0x7f1204a4, float:1.9409138E38)
            if (r1 == r2) goto L34
            int r0 = r0.getTextId()
            r1 = 2131889637(0x7f120de5, float:1.9413943E38)
            if (r0 != r1) goto L4
        L34:
            r3 = 1
            return r3
        L36:
            r3 = 0
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.util.SpecialTypeMediaUtils.needToBeHiddenWhenInLandMultiWindowMode(java.util.List):boolean");
    }

    /* loaded from: classes2.dex */
    public static class SpecialEnterIconAndText {
        public int iconId;
        public long specialEnterFlag;
        public int textId;

        public SpecialEnterIconAndText(long j, int i, int i2) {
            this.specialEnterFlag = j;
            this.iconId = i;
            this.textId = i2;
        }

        public long getSpecialEnterFlag() {
            return this.specialEnterFlag;
        }

        public int getIconId() {
            return this.iconId;
        }

        public int getTextId() {
            return this.textId;
        }
    }

    public static void addVideoCompressPath(String str) {
        mVideoCompressPathSet.add(str);
    }

    public static void removeVideoCompressPath(String str) {
        mVideoCompressPathSet.remove(str);
    }

    public static List<FlagTypeParser> getNeedDynamicCheckFlagTypeParser() {
        return sNeedDynamicCheckFlagParser;
    }

    /* loaded from: classes2.dex */
    public static class FlagParserUsePath implements FlagTypeParser {
        @Override // com.miui.gallery.util.SpecialTypeMediaUtils.FlagTypeParser
        public long parseFlags(String str) {
            String mimeType = BaseFileMimeUtil.getMimeType(str);
            if (BaseFileMimeUtil.isHeifMimeType(mimeType)) {
                return 33554432L;
            }
            if (!BaseFileMimeUtil.isRawFromMimeType(mimeType)) {
                return 0L;
            }
            return FileAppender.DEFAULT_BUFFER_SIZE;
        }

        @Override // com.miui.gallery.util.SpecialTypeMediaUtils.FlagTypeParser
        public Long getSupportFlags() {
            return 33562624L;
        }
    }

    /* loaded from: classes2.dex */
    public static class FlagParserUseExif implements FlagTypeParser {
        /* JADX WARN: Removed duplicated region for block: B:41:0x0093 A[Catch: Exception -> 0x00b6, ExifInvalidFormatException -> 0x00bd, FileNotFoundException -> 0x00c2, TryCatch #2 {ExifInvalidFormatException -> 0x00bd, FileNotFoundException -> 0x00c2, Exception -> 0x00b6, blocks: (B:3:0x0004, B:5:0x000a, B:8:0x001a, B:11:0x0022, B:39:0x008d, B:41:0x0093, B:42:0x0097, B:44:0x009d, B:45:0x00a1, B:47:0x00a7, B:48:0x00aa, B:50:0x00b0, B:12:0x0025, B:15:0x002f, B:18:0x0039, B:21:0x0045, B:24:0x0051, B:27:0x005d, B:30:0x0069, B:33:0x0075, B:36:0x0081), top: B:57:0x0004 }] */
        /* JADX WARN: Removed duplicated region for block: B:44:0x009d A[Catch: Exception -> 0x00b6, ExifInvalidFormatException -> 0x00bd, FileNotFoundException -> 0x00c2, TryCatch #2 {ExifInvalidFormatException -> 0x00bd, FileNotFoundException -> 0x00c2, Exception -> 0x00b6, blocks: (B:3:0x0004, B:5:0x000a, B:8:0x001a, B:11:0x0022, B:39:0x008d, B:41:0x0093, B:42:0x0097, B:44:0x009d, B:45:0x00a1, B:47:0x00a7, B:48:0x00aa, B:50:0x00b0, B:12:0x0025, B:15:0x002f, B:18:0x0039, B:21:0x0045, B:24:0x0051, B:27:0x005d, B:30:0x0069, B:33:0x0075, B:36:0x0081), top: B:57:0x0004 }] */
        /* JADX WARN: Removed duplicated region for block: B:47:0x00a7 A[Catch: Exception -> 0x00b6, ExifInvalidFormatException -> 0x00bd, FileNotFoundException -> 0x00c2, TryCatch #2 {ExifInvalidFormatException -> 0x00bd, FileNotFoundException -> 0x00c2, Exception -> 0x00b6, blocks: (B:3:0x0004, B:5:0x000a, B:8:0x001a, B:11:0x0022, B:39:0x008d, B:41:0x0093, B:42:0x0097, B:44:0x009d, B:45:0x00a1, B:47:0x00a7, B:48:0x00aa, B:50:0x00b0, B:12:0x0025, B:15:0x002f, B:18:0x0039, B:21:0x0045, B:24:0x0051, B:27:0x005d, B:30:0x0069, B:33:0x0075, B:36:0x0081), top: B:57:0x0004 }] */
        /* JADX WARN: Removed duplicated region for block: B:50:0x00b0 A[Catch: Exception -> 0x00b6, ExifInvalidFormatException -> 0x00bd, FileNotFoundException -> 0x00c2, TRY_LEAVE, TryCatch #2 {ExifInvalidFormatException -> 0x00bd, FileNotFoundException -> 0x00c2, Exception -> 0x00b6, blocks: (B:3:0x0004, B:5:0x000a, B:8:0x001a, B:11:0x0022, B:39:0x008d, B:41:0x0093, B:42:0x0097, B:44:0x009d, B:45:0x00a1, B:47:0x00a7, B:48:0x00aa, B:50:0x00b0, B:12:0x0025, B:15:0x002f, B:18:0x0039, B:21:0x0045, B:24:0x0051, B:27:0x005d, B:30:0x0069, B:33:0x0075, B:36:0x0081), top: B:57:0x0004 }] */
        /* JADX WARN: Removed duplicated region for block: B:60:? A[RETURN, SYNTHETIC] */
        @Override // com.miui.gallery.util.SpecialTypeMediaUtils.FlagTypeParser
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public long parseFlags(java.lang.String r7) {
            /*
                r6 = this;
                java.lang.String r0 = "SpecialTypeMediaUtils"
                r1 = 0
                boolean r3 = com.miui.gallery.util.BaseFileMimeUtil.hasExif(r7)     // Catch: java.lang.Exception -> Lb6 com.miui.gallery3d.exif.ExifInvalidFormatException -> Lbd java.io.FileNotFoundException -> Lc2
                if (r3 == 0) goto Lc2
                com.miui.gallery3d.exif.ExifInterface r3 = new com.miui.gallery3d.exif.ExifInterface     // Catch: java.lang.Exception -> Lb6 com.miui.gallery3d.exif.ExifInvalidFormatException -> Lbd java.io.FileNotFoundException -> Lc2
                r3.<init>()     // Catch: java.lang.Exception -> Lb6 com.miui.gallery3d.exif.ExifInvalidFormatException -> Lbd java.io.FileNotFoundException -> Lc2
                r3.readExif(r7)     // Catch: java.lang.Exception -> Lb6 com.miui.gallery3d.exif.ExifInvalidFormatException -> Lbd java.io.FileNotFoundException -> Lc2
                boolean r4 = com.miui.gallery.util.ExifUtil.supportRefocus(r3)     // Catch: java.lang.Exception -> Lb6 com.miui.gallery3d.exif.ExifInvalidFormatException -> Lbd java.io.FileNotFoundException -> Lc2
                if (r4 == 0) goto L1a
                r1 = 1
            L1a:
                boolean r4 = com.miui.gallery.util.ExifUtil.isMotionPhoto(r3)     // Catch: java.lang.Exception -> Lb6 com.miui.gallery3d.exif.ExifInvalidFormatException -> Lbd java.io.FileNotFoundException -> Lc2
                if (r4 == 0) goto L25
                r4 = 32
            L22:
                long r1 = r1 | r4
                goto L8d
            L25:
                boolean r4 = com.miui.gallery.util.ExifUtil.isDocPhoto(r3)     // Catch: java.lang.Exception -> Lb6 com.miui.gallery3d.exif.ExifInvalidFormatException -> Lbd java.io.FileNotFoundException -> Lc2
                if (r4 == 0) goto L2f
                r4 = 65536(0x10000, double:3.2379E-319)
                goto L22
            L2f:
                boolean r4 = com.miui.gallery.util.ExifUtil.isPanoPhoto(r3)     // Catch: java.lang.Exception -> Lb6 com.miui.gallery3d.exif.ExifInvalidFormatException -> Lbd java.io.FileNotFoundException -> Lc2
                if (r4 == 0) goto L39
                r4 = 1073741824(0x40000000, double:5.304989477E-315)
                goto L22
            L39:
                boolean r4 = com.miui.gallery.util.ExifUtil.isClonePhoto(r3)     // Catch: java.lang.Exception -> Lb6 com.miui.gallery3d.exif.ExifInvalidFormatException -> Lbd java.io.FileNotFoundException -> Lc2
                if (r4 == 0) goto L45
                r4 = 2147483648(0x80000000, double:1.0609978955E-314)
                goto L22
            L45:
                boolean r4 = com.miui.gallery.util.ExifUtil.isSuperMoonPhoto(r3)     // Catch: java.lang.Exception -> Lb6 com.miui.gallery3d.exif.ExifInvalidFormatException -> Lbd java.io.FileNotFoundException -> Lc2
                if (r4 == 0) goto L51
                r4 = 68719476736(0x1000000000, double:3.39519326554E-313)
                goto L22
            L51:
                boolean r4 = com.miui.gallery.util.ExifUtil.isProAmbilightPhoto(r3)     // Catch: java.lang.Exception -> Lb6 com.miui.gallery3d.exif.ExifInvalidFormatException -> Lbd java.io.FileNotFoundException -> Lc2
                if (r4 == 0) goto L5d
                r4 = 137438953472(0x2000000000, double:6.7903865311E-313)
                goto L22
            L5d:
                boolean r4 = com.miui.gallery.util.ExifUtil.isSuperNightPhoto(r3)     // Catch: java.lang.Exception -> Lb6 com.miui.gallery3d.exif.ExifInvalidFormatException -> Lbd java.io.FileNotFoundException -> Lc2
                if (r4 == 0) goto L69
                r4 = 274877906944(0x4000000000, double:1.358077306218E-312)
                goto L22
            L69:
                boolean r4 = com.miui.gallery.util.ExifUtil.isProVideoPhoto(r3)     // Catch: java.lang.Exception -> Lb6 com.miui.gallery3d.exif.ExifInvalidFormatException -> Lbd java.io.FileNotFoundException -> Lc2
                if (r4 == 0) goto L75
                r4 = 549755813888(0x8000000000, double:2.716154612436E-312)
                goto L22
            L75:
                boolean r4 = com.miui.gallery.util.ExifUtil.isMiMoJiPhoto(r3)     // Catch: java.lang.Exception -> Lb6 com.miui.gallery3d.exif.ExifInvalidFormatException -> Lbd java.io.FileNotFoundException -> Lc2
                if (r4 == 0) goto L81
                r4 = 1099511627776(0x10000000000, double:5.43230922487E-312)
                goto L22
            L81:
                boolean r4 = com.miui.gallery.util.ExifUtil.isAiWatermarkPhoto(r3)     // Catch: java.lang.Exception -> Lb6 com.miui.gallery3d.exif.ExifInvalidFormatException -> Lbd java.io.FileNotFoundException -> Lc2
                if (r4 == 0) goto L8d
                r4 = 2199023255552(0x20000000000, double:1.0864618449742E-311)
                goto L22
            L8d:
                boolean r4 = com.miui.gallery.util.ExifUtil.isFrontPhoto(r3)     // Catch: java.lang.Exception -> Lb6 com.miui.gallery3d.exif.ExifInvalidFormatException -> Lbd java.io.FileNotFoundException -> Lc2
                if (r4 == 0) goto L97
                r4 = 536870912(0x20000000, double:2.652494739E-315)
                long r1 = r1 | r4
            L97:
                boolean r4 = com.miui.gallery.util.ExifUtil.isPortraitPhoto(r3)     // Catch: java.lang.Exception -> Lb6 com.miui.gallery3d.exif.ExifInvalidFormatException -> Lbd java.io.FileNotFoundException -> Lc2
                if (r4 == 0) goto La1
                r4 = 268435456(0x10000000, double:1.32624737E-315)
                long r1 = r1 | r4
            La1:
                boolean r4 = com.miui.gallery.util.ExifUtil.isWatermarkAdded(r3)     // Catch: java.lang.Exception -> Lb6 com.miui.gallery3d.exif.ExifInvalidFormatException -> Lbd java.io.FileNotFoundException -> Lc2
                if (r4 == 0) goto Laa
                r4 = -9223372036854775808
                long r1 = r1 | r4
            Laa:
                int r3 = com.miui.gallery.util.ExifUtil.getMTSpecialAITypeValue(r3)     // Catch: java.lang.Exception -> Lb6 com.miui.gallery3d.exif.ExifInvalidFormatException -> Lbd java.io.FileNotFoundException -> Lc2
                if (r3 <= 0) goto Lc2
                long r3 = com.miui.gallery.util.SpecialTypeMediaUtils.parseMTSpecialAITypeByValue(r3)     // Catch: java.lang.Exception -> Lb6 com.miui.gallery3d.exif.ExifInvalidFormatException -> Lbd java.io.FileNotFoundException -> Lc2
                long r1 = r1 | r3
                goto Lc2
            Lb6:
                r7 = move-exception
                java.lang.String r3 = "FlagParserUseExif error:\n%s"
                com.miui.gallery.util.logger.DefaultLogger.e(r0, r3, r7)
                goto Lc2
            Lbd:
                java.lang.String r3 = "FlagParserUseExif ExifInvalidFormatException,filePath:[%s]"
                com.miui.gallery.util.logger.DefaultLogger.e(r0, r3, r7)
            Lc2:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.util.SpecialTypeMediaUtils.FlagParserUseExif.parseFlags(java.lang.String):long");
        }

        @Override // com.miui.gallery.util.SpecialTypeMediaUtils.FlagTypeParser
        public Long getSupportFlags() {
            return -9223367703467581535L;
        }
    }

    /* loaded from: classes2.dex */
    public static class BurstPhotoParser implements FlagTypeParser {
        @Override // com.miui.gallery.util.SpecialTypeMediaUtils.FlagTypeParser
        public long parseFlags(String str) {
            String burstPhotoTypeByFileName = BurstInfo.getBurstPhotoTypeByFileName(str);
            if (burstPhotoTypeByFileName != null) {
                return TextUtils.equals(burstPhotoTypeByFileName, "time_burst") ? 8388608L : 64L;
            }
            return 0L;
        }

        @Override // com.miui.gallery.util.SpecialTypeMediaUtils.FlagTypeParser
        public Long getSupportFlags() {
            return 8388672L;
        }
    }
}
