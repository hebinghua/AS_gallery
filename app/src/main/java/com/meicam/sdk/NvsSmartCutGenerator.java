package com.meicam.sdk;

import android.content.Context;
import android.media.ExifInterface;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

/* loaded from: classes.dex */
public class NvsSmartCutGenerator {
    private static final String TAG = "Meicam";
    private static NvsSmartCutGenerator m_instance;
    private SmartCutGeneratorCallback m_SmartCutGeneratorCallback;

    /* loaded from: classes.dex */
    public static class NvsSmartCutCaptionInfo {
        public long duration;
        public String style;
        public int templateSlotIndex;
        public String text;
    }

    /* loaded from: classes.dex */
    public static class NvsSmartCutClipData {
        public boolean imageMotion;
        public boolean isImage;
        public boolean nospeed;
        public int templateSlotIndex;
        public long trimIn;
        public long trimOut;
        public String videoFilePath;
    }

    /* loaded from: classes.dex */
    public static class NvsSmartCutFilterInfo {
        public long duration;
        public String name;
        public int templateSlotIndex;
    }

    /* loaded from: classes.dex */
    public static class NvsSmartCutResultData {
        public NvsSmartCutCaptionInfo endingCaption;
        public String endingFilePath;
        public NvsSmartCutFilterInfo endingFilter;
        public String musicFilePath;
        public ArrayList<NvsSmartCutCaptionInfo> smartCutCaptions;
        public ArrayList<NvsSmartCutFilterInfo> smartCutClipFilters;
        public ArrayList<NvsSmartCutClipData> smartCutClips;
        public ArrayList<NvsSmartCutSpeedList> smartCutSpeeds;
        public ArrayList<NvsSmartCutTransitionInfo> smartCutTransitions;
        public String smartFilter;
        public String templatePath;
        public ArrayList<String> timelineFilters;
        public NvsSmartCutCaptionInfo titleCaption;
        public String titleFilePath;
        public String videoDate;
    }

    /* loaded from: classes.dex */
    public static class NvsSmartCutSCDData {
        public int environment;
        public long frameIndex;
        public int location;
        public int placeTypeL1;
        public int placeTypeL2;
        public int sceneType;
    }

    /* loaded from: classes.dex */
    public static class NvsSmartCutSCDInfo {
        public ArrayList<NvsSmartCutSCDData> scdDataArray;
        public String videoFilePath;
    }

    /* loaded from: classes.dex */
    public static class NvsSmartCutSpeedInfo {
        public long duration;
        public float speed0;
        public float speed1;
    }

    /* loaded from: classes.dex */
    public static class NvsSmartCutSpeedList {
        public ArrayList<NvsSmartCutSpeedInfo> speeds;
        public int templateSlotIndex;
    }

    /* loaded from: classes.dex */
    public static class NvsSmartCutTransitionInfo {
        public long duration;
        public String name;
        public int templateSlotIndex;
    }

    /* loaded from: classes.dex */
    public interface SmartCutGeneratorCallback {
        void onSmartCutError(int i);

        void onSmartCutFinished(NvsSmartCutResultData nvsSmartCutResultData);

        void onSmartCutProgress(float f);
    }

    private static native boolean nativeChangeTemplate(String str);

    private static native void nativeClose();

    private static native boolean nativeInit(Context context, int i);

    private static native void nativeSetSmartCutGeneratorCallback(SmartCutGeneratorCallback smartCutGeneratorCallback);

    private static native boolean nativeStartSmartCut(ArrayList<String> arrayList, ArrayList<String> arrayList2, ArrayList<String> arrayList3, String str, String str2, String str3, String str4, String str5, String str6);

    private static native boolean nativeStartSmartCut2(ArrayList<NvsSmartCutSCDInfo> arrayList, ArrayList<String> arrayList2, String str, String str2, String str3, String str4, String str5, String str6);

    private NvsSmartCutGenerator() {
    }

    public static NvsSmartCutGenerator init(Context context, int i) {
        NvsSmartCutGenerator nvsSmartCutGenerator = m_instance;
        if (nvsSmartCutGenerator != null) {
            return nvsSmartCutGenerator;
        }
        if (!nativeInit(context, i)) {
            return null;
        }
        NvsSmartCutGenerator nvsSmartCutGenerator2 = new NvsSmartCutGenerator();
        m_instance = nvsSmartCutGenerator2;
        return nvsSmartCutGenerator2;
    }

    public static void close() {
        NvsSmartCutGenerator nvsSmartCutGenerator = m_instance;
        if (nvsSmartCutGenerator == null) {
            return;
        }
        nvsSmartCutGenerator.setSmartCutGeneratorCallback(null);
        m_instance = null;
        nativeClose();
    }

    public boolean startSmartCut(ArrayList<String> arrayList, ArrayList<String> arrayList2, ArrayList<String> arrayList3, String str, String str2, String str3, String str4, String str5) {
        String str6;
        if (arrayList2 == null || arrayList == null || arrayList3 == null || arrayList2.isEmpty() || arrayList.isEmpty() || arrayList3.isEmpty() || arrayList.size() != arrayList2.size()) {
            return false;
        }
        ArrayList arrayList4 = new ArrayList();
        Iterator<String> it = arrayList2.iterator();
        while (it.hasNext()) {
            arrayList4.add(it.next());
        }
        sortFileByModifyTime(arrayList4);
        StringBuilder sb = new StringBuilder();
        getCaptureTime((String) arrayList4.get(0), sb);
        StringBuilder sb2 = new StringBuilder();
        getCaptureTime((String) arrayList4.get(arrayList4.size() - 1), sb2);
        if (sb.length() == 0 || sb2.length() == 0) {
            str6 = "";
        } else if (sb.toString().compareTo(sb2.toString()) == 0) {
            str6 = sb.toString();
        } else {
            str6 = sb.toString() + "~" + sb2.toString();
        }
        String str7 = str6;
        ArrayList arrayList5 = new ArrayList();
        for (int i = 0; i < arrayList4.size(); i++) {
            int i2 = 0;
            while (true) {
                if (i2 >= arrayList2.size()) {
                    break;
                } else if (arrayList2.get(i2).equals(arrayList4.get(i))) {
                    arrayList5.add(arrayList.get(i2));
                    break;
                } else {
                    i2++;
                }
            }
        }
        return nativeStartSmartCut(arrayList5, arrayList4, arrayList3, str, str2, str3, str4, str7, str5);
    }

    public boolean startSmartCut(ArrayList<NvsSmartCutSCDInfo> arrayList, ArrayList<String> arrayList2, String str, String str2, String str3, String str4, String str5) {
        String str6;
        if (arrayList == null || arrayList2 == null || arrayList.isEmpty() || arrayList2.isEmpty()) {
            return false;
        }
        ArrayList arrayList3 = new ArrayList();
        Iterator<NvsSmartCutSCDInfo> it = arrayList.iterator();
        while (it.hasNext()) {
            arrayList3.add(it.next().videoFilePath);
        }
        sortFileByModifyTime(arrayList3);
        StringBuilder sb = new StringBuilder();
        getCaptureTime((String) arrayList3.get(0), sb);
        StringBuilder sb2 = new StringBuilder();
        getCaptureTime((String) arrayList3.get(arrayList3.size() - 1), sb2);
        if (sb.length() == 0 || sb2.length() == 0) {
            str6 = "";
        } else if (sb.toString().compareTo(sb2.toString()) == 0) {
            str6 = sb.toString();
        } else {
            str6 = sb.toString() + "~" + sb2.toString();
        }
        String str7 = str6;
        ArrayList arrayList4 = new ArrayList();
        for (int i = 0; i < arrayList3.size(); i++) {
            int i2 = 0;
            while (true) {
                if (i2 >= arrayList.size()) {
                    break;
                } else if (arrayList.get(i2).videoFilePath.equals(arrayList3.get(i))) {
                    arrayList4.add(arrayList.get(i2));
                    break;
                } else {
                    i2++;
                }
            }
        }
        return nativeStartSmartCut2(arrayList4, arrayList2, str, str2, str3, str4, str7, str5);
    }

    public boolean changeTemplate(String str) {
        return nativeChangeTemplate(str);
    }

    public void setSmartCutGeneratorCallback(SmartCutGeneratorCallback smartCutGeneratorCallback) {
        NvsUtils.checkFunctionInMainThread();
        this.m_SmartCutGeneratorCallback = smartCutGeneratorCallback;
        nativeSetSmartCutGeneratorCallback(smartCutGeneratorCallback);
    }

    public static NvsSmartCutGenerator getInstance() {
        NvsUtils.checkFunctionInMainThread();
        return m_instance;
    }

    private static void sortFileByModifyTime(ArrayList<String> arrayList) {
        Collections.sort(arrayList, new Comparator<String>() { // from class: com.meicam.sdk.NvsSmartCutGenerator.1
            @Override // java.util.Comparator
            public int compare(String str, String str2) {
                long captureTime = NvsSmartCutGenerator.getCaptureTime(str, null);
                if (captureTime == 0) {
                    captureTime = NvsSmartCutGenerator.getFileLastTime(str);
                }
                long captureTime2 = NvsSmartCutGenerator.getCaptureTime(str2, null);
                if (captureTime2 == 0) {
                    captureTime2 = NvsSmartCutGenerator.getFileLastTime(str2);
                }
                if (Build.VERSION.SDK_INT >= 19) {
                    return Long.compare(captureTime, captureTime2);
                }
                int i = (captureTime > captureTime2 ? 1 : (captureTime == captureTime2 ? 0 : -1));
                if (i > 0) {
                    return 1;
                }
                return i == 0 ? 0 : -1;
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long getCaptureTime(String str, StringBuilder sb) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        long j = 0;
        try {
            try {
                mediaMetadataRetriever.setDataSource(str);
                String extractMetadata = mediaMetadataRetriever.extractMetadata(5);
                if (extractMetadata != null) {
                    Date date = null;
                    try {
                        date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.CHINA).parse(extractMetadata);
                    } catch (ParseException e) {
                        e.printStackTrace();
                        try {
                            date = new SimpleDateFormat("yyyyMMdd'T'HHmmss.SSS", Locale.CHINA).parse(extractMetadata);
                        } catch (ParseException e2) {
                            e2.printStackTrace();
                        }
                    }
                    if (date != null && date.getYear() >= 50) {
                        if (sb != null) {
                            sb.append((date.getYear() + 1900) + "." + (date.getMonth() + 1));
                        }
                        j = date.getTime();
                    }
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            return j;
        } finally {
            mediaMetadataRetriever.release();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long getFileLastTime(String str) {
        ExifInterface exifInterface;
        String attribute;
        Date date = null;
        try {
            exifInterface = new ExifInterface(str);
        } catch (IOException e) {
            e.printStackTrace();
            exifInterface = null;
        }
        if (exifInterface != null && (attribute = exifInterface.getAttribute("DateTime")) != null) {
            try {
                date = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").parse(attribute);
            } catch (ParseException e2) {
                e2.printStackTrace();
            }
            if (date == null) {
                try {
                    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(attribute).getTime();
                } catch (ParseException e3) {
                    e3.printStackTrace();
                }
            } else {
                return date.getTime();
            }
        }
        try {
            return new File(str).lastModified();
        } catch (Exception e4) {
            e4.printStackTrace();
            return 0L;
        }
    }
}
