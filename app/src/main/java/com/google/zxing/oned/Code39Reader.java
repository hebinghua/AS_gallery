package com.google.zxing.oned;

import ch.qos.logback.core.net.SyslogConstants;
import com.baidu.location.BDLocation;
import com.miui.gallery.assistant.jni.filter.BaiduSceneResult;
import com.xiaomi.miai.api.StatusCode;
import com.xiaomi.milab.videosdk.message.MsgType;

/* loaded from: classes.dex */
public final class Code39Reader extends OneDReader {
    public static final char[] ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ-. *$/+%".toCharArray();
    public static final int ASTERISK_ENCODING;
    public static final int[] CHARACTER_ENCODINGS;

    static {
        int[] iArr = {52, 289, 97, 352, 49, MsgType.MsgEvent.PLAYER_EVENT_TRANSCODE_FAIL, 112, 37, 292, 100, 265, 73, 328, 25, 280, 88, 13, 268, 76, 28, 259, 67, 322, 19, 274, 82, 7, 262, 70, 22, 385, 193, 448, 145, StatusCode.BAD_REQUEST, 208, BaiduSceneResult.EXPRESS_ORDER, 388, 196, 148, SyslogConstants.LOG_LOCAL5, BDLocation.TypeServerDecryptError, BaiduSceneResult.COSMETICS, 42};
        CHARACTER_ENCODINGS = iArr;
        ASTERISK_ENCODING = iArr[39];
    }
}
