package com.meicam.themehelper;

import android.content.Context;
import android.media.ExifInterface;
import android.os.Build;
import android.util.Log;
import com.xiaomi.stat.b.h;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class NvsParseHelper {
    private static final String TAG = "NvsParseHelper";

    public static ArrayList<NvsMusicFileDesc> readMusicFileList(Context context, String str) {
        String readFile = Utils.readFile(str + "/info.json", context.getAssets());
        if (readFile == null) {
            Log.d(TAG, "read music info json error!");
            return new ArrayList<>();
        }
        ArrayList<NvsMusicFileDesc> arrayList = new ArrayList<>();
        try {
            JSONArray jSONArray = new JSONObject(readFile).getJSONArray("musicfiles");
            for (int i = 0; i < jSONArray.length(); i++) {
                arrayList.add(GetMusicFileFromJsonObject(jSONArray.getJSONObject(i), str));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return arrayList;
    }

    public static NvsMusicFileDesc GetMusicFileFromJsonObject(JSONObject jSONObject, String str) {
        NvsMusicFileDesc nvsMusicFileDesc = null;
        try {
            NvsMusicFileDesc nvsMusicFileDesc2 = new NvsMusicFileDesc();
            try {
                nvsMusicFileDesc2.id = str;
                nvsMusicFileDesc2.displayName = jSONObject.getString("name");
                nvsMusicFileDesc2.jsonFile = str + h.g + jSONObject.getString("json id") + ".json";
                nvsMusicFileDesc2.jsonFile10s = str + h.g + jSONObject.getString("json id10s") + ".json";
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(h.g);
                sb.append(jSONObject.getString("music id"));
                nvsMusicFileDesc2.musicFile = sb.toString();
                nvsMusicFileDesc2.musicFile10s = str + h.g + jSONObject.getString("music id10s");
                return nvsMusicFileDesc2;
            } catch (JSONException e) {
                e = e;
                nvsMusicFileDesc = nvsMusicFileDesc2;
                e.printStackTrace();
                return nvsMusicFileDesc;
            }
        } catch (JSONException e2) {
            e = e2;
        }
    }

    public static long readMusicPoint(String str, ArrayList<NvsMusicPointDesc> arrayList, NvsTransDesc nvsTransDesc) {
        arrayList.clear();
        long j = -1;
        try {
            JSONObject jSONObject = new JSONObject(str);
            j = jSONObject.getLong("music duration") * 1000;
            JSONArray jSONArray = jSONObject.getJSONArray("points");
            for (int i = 0; i < jSONArray.length(); i++) {
                try {
                    arrayList.add(GetDescFromJsonObject(jSONArray.getJSONObject(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            if (nvsTransDesc != null) {
                if (jSONObject.has("fullSizeTrans")) {
                    ArrayList<String> arrayList2 = new ArrayList<>();
                    nvsTransDesc.m_fulltransList = arrayList2;
                    getTransByType(jSONObject, "fullSizeTrans", arrayList2);
                }
                if (jSONObject.has("halfSizeTrans")) {
                    ArrayList<String> arrayList3 = new ArrayList<>();
                    nvsTransDesc.m_halftransList = arrayList3;
                    getTransByType(jSONObject, "halfSizeTrans", arrayList3);
                }
                if (jSONObject.has("h-fSizeTrans")) {
                    ArrayList<String> arrayList4 = new ArrayList<>();
                    nvsTransDesc.m_hftransList = arrayList4;
                    getTransByType(jSONObject, "h-fSizeTrans", arrayList4);
                }
                if (jSONObject.has("fullFx")) {
                    ArrayList<String> arrayList5 = new ArrayList<>();
                    nvsTransDesc.m_fulltransFxList = arrayList5;
                    getTransFxByType(jSONObject, "fullFx", arrayList5);
                }
                if (jSONObject.has("halfFx")) {
                    ArrayList<String> arrayList6 = new ArrayList<>();
                    nvsTransDesc.m_halftransFxList = arrayList6;
                    getTransFxByType(jSONObject, "halfFx", arrayList6);
                }
                if (jSONObject.has("h-fFx")) {
                    ArrayList<String> arrayList7 = new ArrayList<>();
                    nvsTransDesc.m_hftransFxList = arrayList7;
                    getTransFxByType(jSONObject, "h-fFx", arrayList7);
                }
                if (jSONObject.has("f-hFx")) {
                    ArrayList<String> arrayList8 = new ArrayList<>();
                    nvsTransDesc.m_fhtransFxList = arrayList8;
                    getTransFxByType(jSONObject, "f-hFx", arrayList8);
                }
            }
            return j;
        } catch (JSONException e2) {
            e2.printStackTrace();
            return j;
        }
    }

    public static NvsMusicPointDesc GetDescFromJsonObject(JSONObject jSONObject) throws JSONException {
        NvsMusicPointDesc nvsMusicPointDesc = new NvsMusicPointDesc();
        nvsMusicPointDesc.cutPoint = jSONObject.getInt("cutPoint");
        nvsMusicPointDesc.transLen = jSONObject.getInt("transLen");
        if (jSONObject.has("transName")) {
            JSONArray jSONArray = jSONObject.getJSONArray("transName");
            for (int i = 0; i < jSONArray.length(); i++) {
                nvsMusicPointDesc.transNames.add(jSONArray.getJSONObject(i).getString("id"));
            }
        }
        if (jSONObject.has("fxName")) {
            JSONArray jSONArray2 = jSONObject.getJSONArray("fxName");
            for (int i2 = 0; i2 < jSONArray2.length(); i2++) {
                nvsMusicPointDesc.fxNames.add(jSONArray2.getJSONObject(i2).getString("id"));
            }
        }
        return nvsMusicPointDesc;
    }

    private static void getTransByType(JSONObject jSONObject, String str, ArrayList<String> arrayList) throws JSONException {
        JSONArray jSONArray = jSONObject.getJSONArray(str);
        if (jSONArray == null) {
            Log.d(TAG, "getTransByType error!");
            return;
        }
        for (int i = 0; i < jSONArray.length(); i++) {
            try {
                arrayList.add(jSONArray.getJSONObject(i).getString("transName"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static void getTransFxByType(JSONObject jSONObject, String str, ArrayList<String> arrayList) throws JSONException {
        JSONArray jSONArray = jSONObject.getJSONArray(str);
        if (jSONArray == null) {
            Log.d(TAG, "getTransFxByType error!");
            return;
        }
        for (int i = 0; i < jSONArray.length(); i++) {
            try {
                arrayList.add(jSONArray.getJSONObject(i).getString("fxName"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void sortFileByModifyTime(ArrayList<NvsImageFileDesc> arrayList) {
        Collections.sort(arrayList, new Comparator<NvsImageFileDesc>() { // from class: com.meicam.themehelper.NvsParseHelper.1
            @Override // java.util.Comparator
            public int compare(NvsImageFileDesc nvsImageFileDesc, NvsImageFileDesc nvsImageFileDesc2) {
                if (nvsImageFileDesc.fileLastTime < 0) {
                    nvsImageFileDesc.fileLastTime = NvsParseHelper.getFileLastTime(nvsImageFileDesc.filePath);
                }
                if (nvsImageFileDesc2.fileLastTime < 0) {
                    nvsImageFileDesc2.fileLastTime = NvsParseHelper.getFileLastTime(nvsImageFileDesc2.filePath);
                }
                if (Build.VERSION.SDK_INT >= 19) {
                    return Long.compare(nvsImageFileDesc.fileLastTime, nvsImageFileDesc2.fileLastTime);
                }
                long j = nvsImageFileDesc.fileLastTime;
                long j2 = nvsImageFileDesc2.fileLastTime;
                if (j > j2) {
                    return 1;
                }
                return j == j2 ? 0 : -1;
            }
        });
    }

    public static void sortFileByName(ArrayList<NvsImageFileDesc> arrayList) {
        Collections.sort(arrayList, new Comparator<NvsImageFileDesc>() { // from class: com.meicam.themehelper.NvsParseHelper.2
            @Override // java.util.Comparator
            public int compare(NvsImageFileDesc nvsImageFileDesc, NvsImageFileDesc nvsImageFileDesc2) {
                return nvsImageFileDesc2.filePath.compareTo(nvsImageFileDesc.filePath);
            }
        });
    }

    public static void sortFileByScore(ArrayList<NvsImageFileDesc> arrayList) {
        Collections.sort(arrayList, new Comparator<NvsImageFileDesc>() { // from class: com.meicam.themehelper.NvsParseHelper.3
            @Override // java.util.Comparator
            public int compare(NvsImageFileDesc nvsImageFileDesc, NvsImageFileDesc nvsImageFileDesc2) {
                if (Build.VERSION.SDK_INT >= 19) {
                    return Float.compare(nvsImageFileDesc.score, nvsImageFileDesc2.score);
                }
                float f = nvsImageFileDesc.score;
                float f2 = nvsImageFileDesc2.score;
                if (f > f2) {
                    return -1;
                }
                return f == f2 ? 0 : 1;
            }
        });
    }

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
