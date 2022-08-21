package miuix.animation.internal;

import miuix.animation.base.AnimConfig;
import miuix.animation.base.AnimSpecialConfig;
import miuix.animation.utils.EaseManager;

/* loaded from: classes3.dex */
public class AnimConfigUtils {
    public static EaseManager.EaseStyle getEase(AnimConfig animConfig, AnimSpecialConfig animSpecialConfig) {
        EaseManager.EaseStyle easeStyle;
        if (animSpecialConfig == null || (easeStyle = animSpecialConfig.ease) == null || easeStyle == AnimConfig.sDefEase) {
            easeStyle = animConfig.ease;
        }
        return easeStyle == null ? AnimConfig.sDefEase : easeStyle;
    }

    public static long getDelay(AnimConfig animConfig, AnimSpecialConfig animSpecialConfig) {
        return Math.max(animConfig.delay, animSpecialConfig != null ? animSpecialConfig.delay : 0L);
    }

    public static int getTintMode(AnimConfig animConfig, AnimSpecialConfig animSpecialConfig) {
        return Math.max(animConfig.tintMode, animSpecialConfig != null ? animSpecialConfig.tintMode : -1);
    }

    public static float getFromSpeed(AnimConfig animConfig, AnimSpecialConfig animSpecialConfig) {
        if (animSpecialConfig != null && !AnimValueUtils.isInvalid(animSpecialConfig.fromSpeed)) {
            return animSpecialConfig.fromSpeed;
        }
        return animConfig.fromSpeed;
    }

    public static float chooseSpeed(float f, float f2) {
        return AnimValueUtils.isInvalid((double) f) ? f2 : AnimValueUtils.isInvalid((double) f2) ? f : Math.max(f, f2);
    }
}
