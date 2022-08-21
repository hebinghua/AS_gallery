package com.miui.gallery.video;

import android.text.TextUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.jcodec.containers.mp4.MP4Util;
import org.jcodec.containers.mp4.boxes.MtagBox;
import org.jcodec.containers.mp4.boxes.NodeBox;
import org.jcodec.containers.mp4.boxes.UdtaBox;
import org.keyczar.Keyczar;

/* loaded from: classes2.dex */
public class VideoTagsParser {
    public static List<Long> parse(String str) {
        MtagBox mtagBox;
        byte[] data;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            UdtaBox parseUdta = MP4Util.parseUdta(new File(str));
            if (parseUdta != null && (mtagBox = (MtagBox) NodeBox.findFirstPath(parseUdta, MtagBox.class, new String[]{MtagBox.fourcc()})) != null && (data = mtagBox.getData()) != null) {
                return parseTagList(new String(data, Charset.forName(Keyczar.DEFAULT_ENCODING)));
            }
            return null;
        } catch (Exception e) {
            DefaultLogger.w("VideoTagsParser", e);
            return null;
        }
    }

    public static List<Long> parseTagList(String str) {
        ArrayList arrayList = new ArrayList();
        if (str != null && str.length() != 0) {
            BufferedReader bufferedReader = new BufferedReader(new StringReader(str));
            while (true) {
                try {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    String trim = readLine.trim();
                    if (trim.length() >= 8) {
                        long longValue = parseTagLine(trim).longValue();
                        if (longValue >= 0) {
                            arrayList.add(Long.valueOf(longValue));
                        }
                    }
                } catch (Exception e) {
                    DefaultLogger.w("VideoTagsParser", e);
                }
            }
        }
        return arrayList;
    }

    public static Long parseTagLine(String str) {
        String[] split = str.split(",");
        if (split.length < 2) {
            return -1L;
        }
        int parseInt = Integer.parseInt(split[1].trim());
        String[] split2 = split[0].split(":");
        if (split2.length < 3) {
            return -1L;
        }
        return Long.valueOf((((Integer.parseInt(split2[0]) * 3600) + (Integer.parseInt(split2[1]) * 60) + Integer.parseInt(split2[2])) * 1000) + parseInt);
    }
}
