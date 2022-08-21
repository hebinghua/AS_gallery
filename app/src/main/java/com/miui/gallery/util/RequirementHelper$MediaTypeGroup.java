package com.miui.gallery.util;

import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.util.FileSize;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class RequirementHelper$MediaTypeGroup {
    public static String getEventTipById(int i) {
        switch (i) {
            case R.id.media_type_burst_photo /* 2131362863 */:
                return "403.7.2.1.16667";
            case R.id.media_type_clone_photo /* 2131362864 */:
                return "403.7.2.1.16671";
            case R.id.media_type_doc_photo /* 2131362865 */:
                return "403.7.2.1.16669";
            case R.id.media_type_fast_motion_video /* 2131362866 */:
                return "403.7.2.1.16666";
            case R.id.media_type_front_photo /* 2131362867 */:
                return "403.7.2.1.16661";
            case R.id.media_type_gif /* 2131362868 */:
                return "403.7.2.1.16663";
            case R.id.media_type_live_vv /* 2131362869 */:
                return "403.7.2.1.16670";
            case R.id.media_type_motion_photo /* 2131362870 */:
                return "403.7.2.1.16664";
            case R.id.media_type_pano_photo /* 2131362871 */:
                return "403.7.2.1.16665";
            case R.id.media_type_portrait_photo /* 2131362872 */:
                return "403.7.2.1.16660";
            case R.id.media_type_raw /* 2131362873 */:
                return "403.7.2.1.16668";
            case R.id.media_type_slow_motion_video /* 2131362874 */:
                return "403.7.2.1.16662";
            case R.id.media_type_time_burst_photo /* 2131362875 */:
                return "403.7.2.1.16672";
            default:
                return null;
        }
    }

    public static String getTypeNameByFlag(long j) {
        if (j == 268435456) {
            return "portrait_photo";
        }
        if (j == 536870912) {
            return "front_photo";
        }
        if (j == FileSize.GB_COEFFICIENT) {
            return "pano_photo";
        }
        if (j == 13196287016960L) {
            return "clone_photo";
        }
        if (j == 4294967296L) {
            return "vlog";
        }
        if (j == 8388672) {
            return "burst_photo";
        }
        if (j == 65536) {
            return "doc_photo";
        }
        if (j == 32) {
            return "motion_photo";
        }
        if (j == 8589934592L) {
            return "gif";
        }
        if (j == 17179869184L) {
            return "slow_motion_video";
        }
        if (j == 34359738368L) {
            return "fast_motion_video";
        }
        if (j != FileAppender.DEFAULT_BUFFER_SIZE) {
            return null;
        }
        return "raw";
    }
}
