package com.google.zxing.datamatrix.encoder;

import ch.qos.logback.core.net.SyslogConstants;
import com.baidu.location.BDLocation;
import com.miui.gallery.assistant.jni.filter.BaiduSceneResult;
import com.nexstreaming.nexeditorsdk.nexClip;
import com.xiaomi.milab.videosdk.message.MsgType;
import miuix.hybrid.Response;

/* loaded from: classes.dex */
public final class ErrorCorrection {
    public static final int[] FACTOR_SETS = {5, 7, 10, 11, 12, 14, 18, 20, 24, 28, 36, 42, 48, 56, 62, 68};
    public static final int[][] FACTORS = {new int[]{228, 48, 15, 111, 62}, new int[]{23, 68, 144, BaiduSceneResult.SCREEN_TEXT, 240, 92, 254}, new int[]{28, 24, 185, 166, 223, 248, 116, 255, 110, 61}, new int[]{175, BaiduSceneResult.COSMETICS, Response.CODE_ACTION_ERROR, 12, 194, SyslogConstants.LOG_LOCAL5, 39, 245, 60, 97, 120}, new int[]{41, 153, 158, 91, 61, 42, BaiduSceneResult.DIGITAL_PRODUCT, 213, 97, 178, 100, 242}, new int[]{156, 97, 192, 252, 95, 9, 157, 119, BaiduSceneResult.COSMETICS, 45, 18, 186, 83, 185}, new int[]{83, 195, 100, 39, 188, 75, 66, 61, 241, 213, 109, BaiduSceneResult.ACCOUNT_BOOK, 94, 254, 225, 48, 90, 188}, new int[]{15, 195, nexClip.AVC_Profile_High444, 9, 233, 71, SyslogConstants.LOG_LOCAL5, 2, 188, SyslogConstants.LOG_LOCAL4, 153, 145, 253, 79, 108, 82, 27, 174, 186, 172}, new int[]{52, 190, 88, Response.CODE_ACTION_ERROR, 109, 39, SyslogConstants.LOG_LOCAL6, 21, 155, 197, 251, 223, 155, 21, 5, 172, 254, 124, 12, 181, SyslogConstants.LOG_LOCAL7, 96, 50, 193}, new int[]{211, 231, 43, 97, 71, 96, 103, 174, 37, 151, 170, 53, 75, 34, 249, 121, 17, BaiduSceneResult.COSMETICS, 110, 213, BaiduSceneResult.GAME, 136, 120, 151, 233, SyslogConstants.LOG_LOCAL5, 93, 255}, new int[]{245, BaiduSceneResult.BANK_CARD, 242, 218, BaiduSceneResult.VISA, 250, BDLocation.TypeServerDecryptError, 181, 102, 120, 84, 179, 220, 251, 80, 182, 229, 18, 2, 4, 68, 33, 101, BaiduSceneResult.JEWELRY, 95, 119, 115, 44, 175, SyslogConstants.LOG_LOCAL7, 59, 25, 225, 98, 81, 112}, new int[]{77, 193, BaiduSceneResult.JEWELRY, 31, 19, 38, 22, 153, 247, 105, 122, 2, 245, BaiduSceneResult.EXPRESS_ORDER, 242, 8, 175, 95, 100, 9, BDLocation.TypeServerError, 105, 214, 111, 57, 121, 21, 1, 253, 57, 54, 101, 248, 202, 69, 50, 150, 177, 226, 5, 9, 5}, new int[]{245, BaiduSceneResult.VARIOUS_TICKETS, 172, 223, 96, 32, 117, 22, 238, BaiduSceneResult.EXPRESS_ORDER, 238, 231, Response.CODE_ACTION_ERROR, 188, 237, 87, 191, 106, 16, 147, 118, 23, 37, 90, 170, Response.CODE_ACTION_ERROR, BaiduSceneResult.INVOICE, 88, 120, 100, 66, BaiduSceneResult.COSMETICS, 186, 240, 82, 44, SyslogConstants.LOG_LOCAL6, 87, 187, 147, SyslogConstants.LOG_LOCAL4, 175, 69, 213, 92, 253, 225, 19}, new int[]{175, 9, 223, 238, 12, 17, 220, 208, 100, 29, 175, 170, 230, 192, 215, 235, 150, 159, 36, 223, 38, 200, BaiduSceneResult.VARIOUS_TICKETS, 54, 228, 146, 218, 234, 117, 203, 29, 232, 144, 238, 22, 150, 201, 117, 62, 207, 164, 13, BaiduSceneResult.JEWELRY, 245, BaiduSceneResult.BANK_CARD, 67, 247, 28, 155, 43, 203, 107, 233, 53, BaiduSceneResult.BLACK_WHITE, 46}, new int[]{242, 93, 169, 50, 144, 210, 39, 118, 202, 188, 201, 189, BaiduSceneResult.BLACK_WHITE, 108, 196, 37, 185, 112, BaiduSceneResult.SCREEN_TEXT, 230, 245, 63, 197, 190, 250, 106, 185, 221, 175, 64, 114, 71, BDLocation.TypeNetWorkLocation, 44, 147, 6, 27, 218, 51, 63, 87, 10, 40, BaiduSceneResult.VISA, 188, 17, 163, 31, SyslogConstants.LOG_LOCAL6, 170, 4, 107, 232, 7, 94, 166, 224, 124, 86, 47, 11, 204}, new int[]{220, 228, 173, 89, 251, 149, 159, 56, 89, 33, 147, nexClip.AVC_Profile_High444, 154, 36, 73, BaiduSceneResult.BANK_CARD, 213, 136, 248, nexClip.kClip_Rotate_180, 234, 197, 158, 177, 68, 122, 93, 213, 15, SyslogConstants.LOG_LOCAL4, 227, 236, 66, BaiduSceneResult.FASHION_OTHER, 153, 185, 202, BDLocation.TypeServerError, 179, 25, 220, 232, 96, 210, 231, 136, 223, 239, 181, 241, 59, 52, 172, 25, 49, 232, 211, 189, 64, 54, 108, 153, BaiduSceneResult.VARIOUS_TICKETS, 63, 96, 103, 82, 186}};
    public static final int[] LOG = new int[256];
    public static final int[] ALOG = new int[255];

    static {
        int i = 1;
        for (int i2 = 0; i2 < 255; i2++) {
            ALOG[i2] = i;
            LOG[i] = i2;
            i *= 2;
            if (i >= 256) {
                i ^= MsgType.MsgEvent.PLAYER_EVENT_TRANSCODE_PROGRESS;
            }
        }
    }

    public static String encodeECC200(String str, SymbolInfo symbolInfo) {
        if (str.length() != symbolInfo.getDataCapacity()) {
            throw new IllegalArgumentException("The number of codewords does not match the selected symbol");
        }
        StringBuilder sb = new StringBuilder(symbolInfo.getDataCapacity() + symbolInfo.getErrorCodewords());
        sb.append(str);
        int interleavedBlockCount = symbolInfo.getInterleavedBlockCount();
        if (interleavedBlockCount == 1) {
            sb.append(createECCBlock(str, symbolInfo.getErrorCodewords()));
        } else {
            sb.setLength(sb.capacity());
            int[] iArr = new int[interleavedBlockCount];
            int[] iArr2 = new int[interleavedBlockCount];
            int[] iArr3 = new int[interleavedBlockCount];
            int i = 0;
            while (i < interleavedBlockCount) {
                int i2 = i + 1;
                iArr[i] = symbolInfo.getDataLengthForInterleavedBlock(i2);
                iArr2[i] = symbolInfo.getErrorLengthForInterleavedBlock(i2);
                iArr3[i] = 0;
                if (i > 0) {
                    iArr3[i] = iArr3[i - 1] + iArr[i];
                }
                i = i2;
            }
            for (int i3 = 0; i3 < interleavedBlockCount; i3++) {
                StringBuilder sb2 = new StringBuilder(iArr[i3]);
                for (int i4 = i3; i4 < symbolInfo.getDataCapacity(); i4 += interleavedBlockCount) {
                    sb2.append(str.charAt(i4));
                }
                String createECCBlock = createECCBlock(sb2.toString(), iArr2[i3]);
                int i5 = 0;
                int i6 = i3;
                while (i6 < iArr2[i3] * interleavedBlockCount) {
                    sb.setCharAt(symbolInfo.getDataCapacity() + i6, createECCBlock.charAt(i5));
                    i6 += interleavedBlockCount;
                    i5++;
                }
            }
        }
        return sb.toString();
    }

    public static String createECCBlock(CharSequence charSequence, int i) {
        return createECCBlock(charSequence, 0, charSequence.length(), i);
    }

    public static String createECCBlock(CharSequence charSequence, int i, int i2, int i3) {
        int i4 = 0;
        while (true) {
            int[] iArr = FACTOR_SETS;
            if (i4 >= iArr.length) {
                i4 = -1;
                break;
            } else if (iArr[i4] == i3) {
                break;
            } else {
                i4++;
            }
        }
        if (i4 < 0) {
            throw new IllegalArgumentException("Illegal number of error correction codewords specified: " + i3);
        }
        int[] iArr2 = FACTORS[i4];
        char[] cArr = new char[i3];
        for (int i5 = 0; i5 < i3; i5++) {
            cArr[i5] = 0;
        }
        for (int i6 = i; i6 < i + i2; i6++) {
            int i7 = i3 - 1;
            int charAt = cArr[i7] ^ charSequence.charAt(i6);
            while (i7 > 0) {
                if (charAt != 0 && iArr2[i7] != 0) {
                    char c = cArr[i7 - 1];
                    int[] iArr3 = ALOG;
                    int[] iArr4 = LOG;
                    cArr[i7] = (char) (c ^ iArr3[(iArr4[charAt] + iArr4[iArr2[i7]]) % 255]);
                } else {
                    cArr[i7] = cArr[i7 - 1];
                }
                i7--;
            }
            if (charAt != 0 && iArr2[0] != 0) {
                int[] iArr5 = ALOG;
                int[] iArr6 = LOG;
                cArr[0] = (char) iArr5[(iArr6[charAt] + iArr6[iArr2[0]]) % 255];
            } else {
                cArr[0] = 0;
            }
        }
        char[] cArr2 = new char[i3];
        for (int i8 = 0; i8 < i3; i8++) {
            cArr2[i8] = cArr[(i3 - i8) - 1];
        }
        return String.valueOf(cArr2);
    }
}
