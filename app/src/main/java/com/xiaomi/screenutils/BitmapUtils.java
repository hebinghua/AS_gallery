package com.xiaomi.screenutils;

import com.nexstreaming.nexeditorsdk.nexClip;

/* loaded from: classes3.dex */
public class BitmapUtils {
    public static int getRotate(int i) {
        if (i != -1) {
            if (i == 3) {
                return nexClip.kClip_Rotate_180;
            }
            if (i == 6) {
                return 90;
            }
            if (i == 8) {
                return nexClip.kClip_Rotate_270;
            }
        }
        return 0;
    }
}
