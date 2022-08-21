package com.nexstreaming.nexeditorsdk;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;
import android.view.animation.AccelerateDecelerateInterpolator;
import com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader;
import com.nexstreaming.kminternal.kinemaster.config.EditorGlobal;
import com.nexstreaming.nexeditorsdk.exception.nexSDKException;
import com.nexstreaming.nexeditorsdk.nexCrop;
import com.nexstreaming.nexeditorsdk.nexOverlayImage;
import com.xiaomi.stat.b.h;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes3.dex */
public final class nexTemplateComposer {
    private static final String TAG = "nexTemplateComposer";
    private static final String TAG_BRIGHTNESS = "brightness";
    private static final String TAG_COLOR_FILTER = "color_filter";
    private static final String TAG_CONTRAST = "contrast";
    private static final String TAG_CROP_MODE = "crop_mode";
    private static final String TAG_CROP_SPEED = "image_crop_speed";
    private static final String TAG_DURATION = "duration";
    private static final String TAG_EFFECTS = "effects";
    private static final String TAG_EXTERNAL_IMAGE_PATH = "external_image_path";
    private static final String TAG_EXTERNAL_VIDEO_PATH = "external_video_path";
    private static final String TAG_ID = "id";
    private static final String TAG_IMAGE_CROP_MODE = "image_crop_mode";
    private static final String TAG_LUT = "lut";
    private static final String TAG_SATURATION = "saturation";
    private static final String TAG_SOLID_COLOR = "solid_color";
    private static final String TAG_SOURCE_TYPE = "source_type";
    private static final String TAG_SPEED_CONTROL = "speed_control";
    private static final String TAG_TEMPLATE = "template";
    private static final String TAG_TEMPLATE_BGM = "template_bgm";
    private static final String TAG_TEMPLATE_BGM_VOLUME = "template_bgm_volume";
    private static final String TAG_TEMPLATE_DESCRIPTION = "template_desc";
    private static final String TAG_TEMPLATE_INTRO = "template_intro";
    private static final String TAG_TEMPLATE_LETTERBOX = "template_letterbox";
    private static final String TAG_TEMPLATE_LOOP = "template_loop";
    private static final String TAG_TEMPLATE_NAME = "template_name";
    private static final String TAG_TEMPLATE_OUTRO = "template_outro";
    private static final String TAG_TEMPLATE_VERSION = "template_version";
    private static final String TAG_TYPE = "type";
    private static final String TAG_VIDEO_CROP_MODE = "video_crop_mode";
    private static final String TAG_VIGNETTE = "vignette";
    private static final String TAG_VOLUME = "volume";
    private static final String cancelMassage = "cancel template";
    private Context mAppContext;
    private InputStream mInputStream;
    private nexProject mProject;
    private Context mResContext;
    private String mTemplateFilePath;
    private String mTemplateID;
    private boolean mUseProjectSpeed;
    public ArrayList<HashMap<String, String>> templateList;
    private String mBGMPath = null;
    private boolean mBGMTempFile = false;
    private JSONArray mTemplateArray = null;
    private JSONArray mIntroTemplateArray = null;
    private JSONArray mLoopTemplateArray = null;
    private JSONArray mOutroTemplateArray = null;
    private c mTemplate = null;
    private String mTemplateVersion = null;
    private boolean mCancel = false;
    public int templateListID = 0;
    private boolean mOverlappedTransition = true;
    public int introCount = 0;
    public int loopCount = 0;
    public int tempClipID = 0;
    private ArrayList<HashMap<String, String>> mTemplateList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> mIntroTemplateList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> mLoopTemplateList = new ArrayList<>();
    private ArrayList<HashMap<String, String>> mOutroTemplateList = new ArrayList<>();
    private SparseArray<Integer> mTemplateTypeCountList = new SparseArray<>();
    private ArrayList<a> mOverlayList = new ArrayList<>();

    /* loaded from: classes3.dex */
    public class a {
        private int b;
        private int c;
        private int d;
        private boolean e;
        private int f;
        private int g;
        private int h;
        private int i;

        public a(int i, int i2, int i3, boolean z, int i4, int i5, int i6, int i7) {
            this.b = i;
            this.c = i2;
            this.d = i3;
            this.e = z;
            this.f = i4;
            this.g = i5;
            this.h = i6;
            this.i = i7;
        }

        public int a() {
            return this.b;
        }

        public int b() {
            return this.c;
        }

        public int c() {
            return this.d;
        }

        public boolean d() {
            return this.e;
        }

        public int e() {
            return this.f;
        }

        public int f() {
            return this.g;
        }

        public int g() {
            return this.h;
        }

        public int h() {
            return this.i;
        }
    }

    public nexProject createProject() {
        return new nexProject();
    }

    public void release() {
        this.introCount = 0;
        this.loopCount = 0;
        this.tempClipID = 0;
        this.mCancel = false;
        c cVar = this.mTemplate;
        if (cVar != null) {
            cVar.a();
        }
        ArrayList<HashMap<String, String>> arrayList = this.mTemplateList;
        if (arrayList != null) {
            arrayList.clear();
        }
        ArrayList<HashMap<String, String>> arrayList2 = this.mIntroTemplateList;
        if (arrayList2 != null) {
            arrayList2.clear();
        }
        ArrayList<HashMap<String, String>> arrayList3 = this.mLoopTemplateList;
        if (arrayList3 != null) {
            arrayList3.clear();
        }
        ArrayList<HashMap<String, String>> arrayList4 = this.mOutroTemplateList;
        if (arrayList4 != null) {
            arrayList4.clear();
        }
        SparseArray<Integer> sparseArray = this.mTemplateTypeCountList;
        if (sparseArray != null) {
            sparseArray.clear();
        }
        ArrayList<a> arrayList5 = this.mOverlayList;
        if (arrayList5 != null) {
            arrayList5.clear();
        }
        if (this.mBGMPath != null && this.mBGMTempFile) {
            new File(this.mBGMPath).delete();
        }
        this.templateListID = 0;
        this.mTemplateVersion = null;
    }

    public String setTemplateEffects2Project(nexProject nexproject, Context context, Context context2, String str, boolean z) throws nexSDKException {
        initTemplateComposer(nexproject, context, context2, str);
        return setTemplateEffect();
    }

    public void initTemplateComposer(nexProject nexproject, Context context, Context context2, String str) throws nexSDKException {
        this.mProject = nexproject;
        this.mAppContext = context;
        this.mResContext = context2;
        this.mCancel = false;
        this.mTemplateID = null;
        this.mTemplateFilePath = null;
        this.mInputStream = null;
        this.mTemplateID = str;
        release();
        this.mTemplateTypeCountList.append(0, 0);
        this.mTemplateTypeCountList.append(1, 0);
        this.mTemplateTypeCountList.append(2, 0);
    }

    public void addTemplateOverlay(int i, String str) {
        String[] split = str.split(",");
        String str2 = split[0];
        String str3 = split[1];
        String str4 = split[2];
        String str5 = split[3];
        String str6 = split[4];
        String str7 = split[5];
        String str8 = split[6];
        String str9 = split[7];
        Log.d(TAG, "clipID=" + i + " /type=" + str2 + " /duration=" + str3 + " /variation=" + str4 + " /activeAnimation=" + str5 + " /inAnimationStartTime=" + str6 + " /inAnimationTime=" + str7 + " /outAnimationStartTime=" + str8 + " /outAnimationTime=" + str9);
        if (str2.equals("overlay")) {
            this.mOverlayList.add(new a(i, Integer.parseInt(str3), Integer.parseInt(str4), Boolean.parseBoolean(str5), Integer.parseInt(str6), Integer.parseInt(str7), Integer.parseInt(str8), Integer.parseInt(str9)));
        }
    }

    public nexOverlayItem vignetteOverlayViaRatioMode(final String str, final int i, final int i2, int i3, int i4) {
        return new nexOverlayItem(new nexOverlayImage("template_overlay", i, i2, new nexOverlayImage.runTimeMakeBitMap() { // from class: com.nexstreaming.nexeditorsdk.nexTemplateComposer.1
            @Override // com.nexstreaming.nexeditorsdk.nexOverlayImage.runTimeMakeBitMap
            public int getBitmapID() {
                return 100;
            }

            @Override // com.nexstreaming.nexeditorsdk.nexOverlayImage.runTimeMakeBitMap
            public boolean isAniMate() {
                return false;
            }

            @Override // com.nexstreaming.nexeditorsdk.nexOverlayImage.runTimeMakeBitMap
            public Bitmap makeBitmap() {
                try {
                    return Bitmap.createScaledBitmap(BitmapFactory.decodeStream(nexTemplateComposer.this.mResContext.getResources().getAssets().open(str), null, null), i, i2, true);
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }), i / 2, i2 / 2, i3, i3 + i4);
    }

    public void setOverlay2Project() {
        Iterator<a> it = this.mOverlayList.iterator();
        while (it.hasNext()) {
            a next = it.next();
            nexOverlayItem vignetteOverlayViaRatioMode = vignetteOverlayViaRatioMode("vignette.png", nexApplicationConfig.getAspectProfile().getWidth(), nexApplicationConfig.getAspectProfile().getHeight(), this.mProject.getClip(next.a(), true).mStartTime + next.c(), next.b());
            this.mProject.addOverlay(vignetteOverlayViaRatioMode);
            if (next.d()) {
                vignetteOverlayViaRatioMode.clearAnimate();
                vignetteOverlayViaRatioMode.addAnimate(nexAnimate.getAlpha(next.e(), next.f(), 0.0f, 1.0f).setInterpolator(new AccelerateDecelerateInterpolator()));
                vignetteOverlayViaRatioMode.addAnimate(nexAnimate.getAlpha(next.g(), next.h(), 1.0f, 0.0f).setInterpolator(new AccelerateDecelerateInterpolator()));
            }
        }
    }

    public String setTemplateEffect() {
        String applyTemplateOnProject;
        String parsingJSONFile = parsingJSONFile(this.mProject);
        if (parsingJSONFile != null) {
            return parsingJSONFile;
        }
        if (this.mTemplateVersion.equals("template 1.0")) {
            consistProjectViaVer1(this.mProject);
            if (this.mCancel) {
                return cancelMassage;
            }
            applyTemplateOnProject = setProperty2Clips(this.mProject, this.mTemplateVersion);
        } else if (this.mTemplateVersion.equals("template 1.x")) {
            consistProjectViaVer2(this.mProject);
            if (this.mCancel) {
                return cancelMassage;
            }
            applyTemplateOnProject = setProperty2Clips(this.mProject, this.mTemplateVersion);
        } else {
            applyTemplateOnProject = applyTemplateOnProject(this.mProject);
        }
        if (applyTemplateOnProject != null) {
            return applyTemplateOnProject;
        }
        setOverlay2Project();
        return null;
    }

    public String getValue(JSONObject jSONObject, String str) {
        try {
            return jSONObject.getString(str);
        } catch (JSONException unused) {
            if (str.equals(TAG_EFFECTS)) {
                return "none";
            }
            if (str.equals("id") || str.equals(TAG_VOLUME)) {
                return "0";
            }
            if (str.equals(TAG_SOURCE_TYPE)) {
                return "ALL";
            }
            if (str.equals(TAG_EXTERNAL_VIDEO_PATH) || str.equals(TAG_EXTERNAL_IMAGE_PATH) || str.equals(TAG_SOLID_COLOR) || str.equals(TAG_LUT)) {
                return null;
            }
            return str.equals(TAG_VIGNETTE) ? "clip,no" : str.equals(TAG_CROP_MODE) ? "" : str.equals(TAG_CROP_SPEED) ? "0" : "default";
        }
    }

    public HashMap<String, String> setParameter2List(JSONObject jSONObject) {
        HashMap<String, String> hashMap = new HashMap<>();
        String value = getValue(jSONObject, "type");
        if (value.equals("scene")) {
            String value2 = getValue(jSONObject, "id");
            String value3 = getValue(jSONObject, TAG_SOURCE_TYPE);
            String value4 = getValue(jSONObject, TAG_DURATION);
            String value5 = getValue(jSONObject, TAG_VOLUME);
            String value6 = getValue(jSONObject, TAG_EFFECTS);
            String value7 = getValue(jSONObject, TAG_BRIGHTNESS);
            String value8 = getValue(jSONObject, TAG_CONTRAST);
            String value9 = getValue(jSONObject, TAG_SATURATION);
            String value10 = getValue(jSONObject, TAG_COLOR_FILTER);
            String value11 = getValue(jSONObject, TAG_SPEED_CONTROL);
            String value12 = getValue(jSONObject, TAG_VIGNETTE);
            String value13 = getValue(jSONObject, TAG_LUT);
            String value14 = getValue(jSONObject, TAG_EXTERNAL_VIDEO_PATH);
            String value15 = getValue(jSONObject, TAG_EXTERNAL_IMAGE_PATH);
            String value16 = getValue(jSONObject, TAG_SOLID_COLOR);
            String value17 = getValue(jSONObject, TAG_CROP_MODE);
            String value18 = getValue(jSONObject, TAG_CROP_SPEED);
            String value19 = getValue(jSONObject, TAG_VIDEO_CROP_MODE);
            String value20 = getValue(jSONObject, TAG_IMAGE_CROP_MODE);
            hashMap.put("type", value);
            hashMap.put("id", value2);
            hashMap.put(TAG_SOURCE_TYPE, value3);
            hashMap.put(TAG_DURATION, value4);
            hashMap.put(TAG_VOLUME, value5);
            hashMap.put(TAG_EFFECTS, value6);
            hashMap.put(TAG_BRIGHTNESS, value7);
            hashMap.put(TAG_CONTRAST, value8);
            hashMap.put(TAG_SATURATION, value9);
            hashMap.put(TAG_COLOR_FILTER, value10);
            hashMap.put(TAG_SPEED_CONTROL, value11);
            hashMap.put(TAG_LUT, value13);
            hashMap.put(TAG_CROP_MODE, value17);
            hashMap.put(TAG_CROP_SPEED, value18);
            hashMap.put(TAG_VIDEO_CROP_MODE, value19);
            hashMap.put(TAG_IMAGE_CROP_MODE, value20);
            hashMap.put(TAG_EXTERNAL_VIDEO_PATH, value14);
            hashMap.put(TAG_EXTERNAL_IMAGE_PATH, value15);
            hashMap.put(TAG_SOLID_COLOR, value16);
            hashMap.put(TAG_VIGNETTE, value12);
        } else if (value.equals("transition")) {
            String value21 = getValue(jSONObject, TAG_EFFECTS);
            String value22 = getValue(jSONObject, TAG_DURATION);
            hashMap.put("type", value);
            hashMap.put(TAG_EFFECTS, value21);
            hashMap.put(TAG_DURATION, value22);
        }
        return hashMap;
    }

    public static boolean checkEffectId(nexEffectLibrary nexeffectlibrary, String str) {
        if (str == null || str.compareToIgnoreCase("none") == 0 || str.compareToIgnoreCase("null") == 0 || nexeffectlibrary.checkEffectID(str)) {
            return true;
        }
        Log.d(TAG, "missing effect: (" + str + "))");
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x008b A[Catch: JSONException -> 0x00e1, TryCatch #0 {JSONException -> 0x00e1, blocks: (B:5:0x0023, B:7:0x0032, B:8:0x0035, B:10:0x0043, B:13:0x004c, B:18:0x005b, B:19:0x0085, B:21:0x008b, B:23:0x0099, B:24:0x009c, B:25:0x009f, B:26:0x00a6, B:28:0x00ac, B:30:0x00ba, B:31:0x00bd, B:32:0x00c0, B:33:0x00c7, B:35:0x00cd, B:37:0x00db), top: B:59:0x0023, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x00ac A[Catch: JSONException -> 0x00e1, TryCatch #0 {JSONException -> 0x00e1, blocks: (B:5:0x0023, B:7:0x0032, B:8:0x0035, B:10:0x0043, B:13:0x004c, B:18:0x005b, B:19:0x0085, B:21:0x008b, B:23:0x0099, B:24:0x009c, B:25:0x009f, B:26:0x00a6, B:28:0x00ac, B:30:0x00ba, B:31:0x00bd, B:32:0x00c0, B:33:0x00c7, B:35:0x00cd, B:37:0x00db), top: B:59:0x0023, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00cd A[Catch: JSONException -> 0x00e1, TryCatch #0 {JSONException -> 0x00e1, blocks: (B:5:0x0023, B:7:0x0032, B:8:0x0035, B:10:0x0043, B:13:0x004c, B:18:0x005b, B:19:0x0085, B:21:0x008b, B:23:0x0099, B:24:0x009c, B:25:0x009f, B:26:0x00a6, B:28:0x00ac, B:30:0x00ba, B:31:0x00bd, B:32:0x00c0, B:33:0x00c7, B:35:0x00cd, B:37:0x00db), top: B:59:0x0023, outer: #1 }] */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00ff A[Catch: JSONException | Exception -> 0x0120, TryCatch #1 {JSONException | Exception -> 0x0120, blocks: (B:4:0x0018, B:40:0x00f9, B:42:0x00ff, B:43:0x0106, B:45:0x010c, B:47:0x011a, B:39:0x00e1, B:5:0x0023, B:7:0x0032, B:8:0x0035, B:10:0x0043, B:13:0x004c, B:18:0x005b, B:19:0x0085, B:21:0x008b, B:23:0x0099, B:24:0x009c, B:25:0x009f, B:26:0x00a6, B:28:0x00ac, B:30:0x00ba, B:31:0x00bd, B:32:0x00c0, B:33:0x00c7, B:35:0x00cd, B:37:0x00db), top: B:61:0x0018, inners: #0 }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String[] checkMissEffects(com.nexstreaming.nexeditorsdk.nexEffectLibrary r12, java.io.InputStream r13) {
        /*
            Method dump skipped, instructions count: 324
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.nexeditorsdk.nexTemplateComposer.checkMissEffects(com.nexstreaming.nexeditorsdk.nexEffectLibrary, java.io.InputStream):java.lang.String[]");
    }

    /* JADX WARN: Removed duplicated region for block: B:42:0x014f A[Catch: Exception -> 0x0179, JSONException -> 0x017f, TryCatch #0 {Exception -> 0x0179, blocks: (B:13:0x002b, B:15:0x0037, B:17:0x0047, B:18:0x004b, B:20:0x005c, B:23:0x0065, B:40:0x0147, B:42:0x014f, B:43:0x0157, B:45:0x015f, B:46:0x0171, B:26:0x0079, B:27:0x00bf, B:29:0x00c7, B:30:0x00d9, B:31:0x00e2, B:33:0x00ea, B:34:0x00fc, B:35:0x0105, B:37:0x010d, B:39:0x011f), top: B:56:0x002b }] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String parsingJSONFile(com.nexstreaming.nexeditorsdk.nexProject r14) {
        /*
            Method dump skipped, instructions count: 392
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.nexeditorsdk.nexTemplateComposer.parsingJSONFile(com.nexstreaming.nexeditorsdk.nexProject):java.lang.String");
    }

    public void applyBGM2Project(nexProject nexproject, String str) {
        if (str == null) {
            return;
        }
        Log.d(TAG, "bgm path1=" + str);
        if (this.mTemplateID != null) {
            if (str.equalsIgnoreCase("null")) {
                nexproject.setBackgroundMusicPath(null);
            } else {
                nexproject.setBackgroundMusicPath(nexAssetPackageManager.getAssetPackageMediaPath(this.mAppContext, str));
            }
        } else if (str.equalsIgnoreCase("null")) {
            nexproject.setBackgroundMusicPath(null);
        } else if (str.regionMatches(true, 0, TAG_TEMPLATE, 0, 8)) {
            try {
                nexproject.setBackgroundMusicPath(raw2file(this.mAppContext, this.mResContext, str));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (str.contains("/storage/")) {
                String absolutePath = Environment.getExternalStorageDirectory().getAbsolutePath();
                if (str.contains("/storage/emulated/0")) {
                    str = str.replace("/storage/emulated/0", absolutePath);
                } else {
                    str = str.replace("/storage", absolutePath);
                }
            }
            nexproject.setBackgroundMusicPath(str);
        }
    }

    public void setUseProjectSpeed(boolean z) {
        this.mUseProjectSpeed = z;
    }

    public void setOverlappedTransitionFlag(boolean z) {
        this.mOverlappedTransition = z;
    }

    /* JADX WARN: Removed duplicated region for block: B:55:0x026c A[LOOP:0: B:3:0x0016->B:55:0x026c, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:57:0x0268 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void consistProjectViaVer1(com.nexstreaming.nexeditorsdk.nexProject r17) {
        /*
            Method dump skipped, instructions count: 624
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.nexeditorsdk.nexTemplateComposer.consistProjectViaVer1(com.nexstreaming.nexeditorsdk.nexProject):void");
    }

    public boolean addSpecialClip2Project(nexProject nexproject) {
        if (this.templateList.get(this.templateListID).get(TAG_SOURCE_TYPE).equals("EXTERNAL_VIDEO") || this.templateList.get(this.templateListID).get(TAG_SOURCE_TYPE).equals("EXTERNAL_IMAGE") || this.templateList.get(this.templateListID).get(TAG_SOURCE_TYPE).equals("SOLID")) {
            if (this.templateList.get(this.templateListID).get(TAG_SOURCE_TYPE).equals("EXTERNAL_VIDEO")) {
                nexClip supportedClip = nexClip.getSupportedClip(this.templateList.get(this.templateListID).get(TAG_EXTERNAL_VIDEO_PATH));
                if (supportedClip != null) {
                    nexproject.add(supportedClip);
                    ArrayList<HashMap<String, String>> arrayList = this.templateList;
                    if (arrayList == this.mIntroTemplateList) {
                        this.introCount++;
                    } else if (arrayList == this.mLoopTemplateList) {
                        this.loopCount++;
                    }
                    this.tempClipID++;
                }
            } else {
                int parseInt = Integer.parseInt(this.templateList.get(this.templateListID).get(TAG_DURATION));
                if (this.templateList.get(this.templateListID).get(TAG_SOURCE_TYPE).equals("EXTERNAL_IMAGE")) {
                    nexClip supportedClip2 = nexClip.getSupportedClip(this.templateList.get(this.templateListID).get(TAG_EXTERNAL_IMAGE_PATH));
                    if (supportedClip2 != null) {
                        supportedClip2.setImageClipDuration(parseInt);
                        nexproject.add(supportedClip2);
                        ArrayList<HashMap<String, String>> arrayList2 = this.templateList;
                        if (arrayList2 == this.mIntroTemplateList) {
                            this.introCount++;
                        } else if (arrayList2 == this.mLoopTemplateList) {
                            this.loopCount++;
                        }
                        this.tempClipID++;
                    }
                } else {
                    nexClip solidClip = nexClip.getSolidClip(Color.parseColor(this.templateList.get(this.templateListID).get(TAG_SOLID_COLOR)));
                    if (solidClip != null) {
                        solidClip.setImageClipDuration(parseInt);
                        nexproject.add(solidClip);
                        ArrayList<HashMap<String, String>> arrayList3 = this.templateList;
                        if (arrayList3 == this.mIntroTemplateList) {
                            this.introCount++;
                        } else if (arrayList3 == this.mLoopTemplateList) {
                            this.loopCount++;
                        }
                        this.tempClipID++;
                    }
                }
            }
            manageTemplateList(true);
            return true;
        }
        return false;
    }

    public void manageTemplateList(boolean z) {
        ArrayList<HashMap<String, String>> arrayList = this.templateList;
        if (arrayList == this.mIntroTemplateList) {
            this.mTemplateTypeCountList.append(0, Integer.valueOf(this.introCount));
        } else if (arrayList == this.mLoopTemplateList) {
            this.mTemplateTypeCountList.append(1, Integer.valueOf(this.loopCount));
        }
        this.templateListID += 2;
        Log.d(TAG, "templateListID/templateList.size:" + this.templateListID + h.g + this.templateList.size());
        if (z) {
            if (this.templateListID < this.templateList.size()) {
                return;
            }
            ArrayList<HashMap<String, String>> arrayList2 = this.templateList;
            if (arrayList2 == this.mIntroTemplateList) {
                this.templateListID = 0;
                this.templateList = this.mLoopTemplateList;
                Log.d(TAG, "intro -> loop");
                return;
            } else if (arrayList2 != this.mLoopTemplateList) {
                return;
            } else {
                this.templateListID = 0;
                Log.d(TAG, "loop -> loop");
                return;
            }
        }
        ArrayList<HashMap<String, String>> arrayList3 = this.templateList;
        if (arrayList3 == this.mIntroTemplateList) {
            this.templateListID = 0;
            this.templateList = this.mOutroTemplateList;
            this.mTemplateTypeCountList.append(2, 1);
            Log.d(TAG, "intro -> outro");
        } else if (arrayList3 != this.mLoopTemplateList) {
        } else {
            this.templateListID = 0;
            this.templateList = this.mOutroTemplateList;
            this.mTemplateTypeCountList.append(2, 1);
            Log.d(TAG, "loop -> outro");
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:121:0x046e A[LOOP:3: B:25:0x011e->B:121:0x046e, LOOP_END] */
    /* JADX WARN: Removed duplicated region for block: B:150:0x0465 A[SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r6v2, types: [boolean, int] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void consistProjectViaVer2(com.nexstreaming.nexeditorsdk.nexProject r32) {
        /*
            Method dump skipped, instructions count: 1457
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.nexeditorsdk.nexTemplateComposer.consistProjectViaVer2(com.nexstreaming.nexeditorsdk.nexProject):void");
    }

    public String setProperty2VideoClip(nexProject nexproject, nexClip nexclip, int i) {
        boolean z;
        int parseInt = Integer.parseInt(this.templateList.get(this.templateListID).get(TAG_VOLUME).equals("default") ? "-1" : this.templateList.get(this.templateListID).get(TAG_VOLUME));
        int parseInt2 = Integer.parseInt(this.templateList.get(this.templateListID).get(TAG_CONTRAST).equals("default") ? "-1" : this.templateList.get(this.templateListID).get(TAG_CONTRAST));
        int parseInt3 = Integer.parseInt(this.templateList.get(this.templateListID).get(TAG_BRIGHTNESS).equals("default") ? "-1" : this.templateList.get(this.templateListID).get(TAG_BRIGHTNESS));
        int parseInt4 = Integer.parseInt(this.templateList.get(this.templateListID).get(TAG_SATURATION).equals("default") ? "-1" : this.templateList.get(this.templateListID).get(TAG_SATURATION));
        int parseInt5 = Integer.parseInt(this.templateList.get(this.templateListID).get(TAG_COLOR_FILTER).equals("default") ? "-1" : this.templateList.get(this.templateListID).get(TAG_COLOR_FILTER));
        int parseInt6 = Integer.parseInt(this.templateList.get(this.templateListID).get(TAG_SPEED_CONTROL).equals("default") ? "-1" : this.templateList.get(this.templateListID).get(TAG_SPEED_CONTROL));
        String str = this.templateList.get(this.templateListID).get(TAG_EFFECTS);
        int i2 = parseInt6;
        String str2 = this.templateList.get(this.templateListID).get(TAG_LUT);
        String str3 = this.templateList.get(this.templateListID).get(TAG_VIGNETTE);
        String str4 = this.templateList.get(this.templateListID).get(TAG_CROP_MODE);
        if (str4.equals("")) {
            str4 = this.templateList.get(this.templateListID).get(TAG_VIDEO_CROP_MODE);
        }
        try {
            if (str.equals("none")) {
                nexclip.getClipEffect(true).setEffectNone();
            } else {
                nexclip.getClipEffect(true).setEffect(str);
                if (this.mUseProjectSpeed && nexclip.getVideoClipEdit().getSpeedControl() != 100) {
                    i2 = nexclip.getVideoClipEdit().getSpeedControl();
                }
                nexclip.getClipEffect(true).setEffectShowTime(0, 0);
                String substring = str.substring(str.lastIndexOf("."));
                if (substring.equals(".opening") || substring.equals(".middle") || substring.equals(".ending")) {
                    nexclip.getClipEffect(true).setTitle(0, " ");
                    nexclip.getClipEffect(true).setTitle(1, " ");
                }
            }
            int i3 = i2;
            if (parseInt != -1) {
                nexclip.setClipVolume(parseInt);
            }
            if (parseInt3 != -1) {
                nexclip.setBrightness(parseInt3);
            }
            if (parseInt2 != -1) {
                nexclip.setContrast(parseInt2);
            }
            if (parseInt4 != -1) {
                nexclip.setSaturation(parseInt4);
            }
            if (parseInt5 != -1) {
                nexclip.setColorEffect(getColorEffect(parseInt5 - 1));
            }
            if (i3 != -1) {
                nexclip.getVideoClipEdit().setSpeedControl(i3);
            }
            switch (str4.hashCode()) {
                case 101393:
                    if (str4.equals("fit")) {
                        z = true;
                        break;
                    }
                    z = true;
                    break;
                case 3143043:
                    if (str4.equals("fill")) {
                        z = false;
                        break;
                    }
                    z = true;
                    break;
                case 1054849215:
                    if (str4.equals("pan_face")) {
                        z = true;
                        break;
                    }
                    z = true;
                    break;
                case 1055207047:
                    if (str4.equals("pan_rand")) {
                        z = true;
                        break;
                    }
                    z = true;
                    break;
                default:
                    z = true;
                    break;
            }
            if (!z) {
                nexclip.getCrop().randomizeStartEndPosition(false, nexCrop.CropMode.FILL);
            } else if (z) {
                nexclip.getCrop().randomizeStartEndPosition(false, nexCrop.CropMode.PAN_RAND);
            } else if (z) {
                nexclip.getCrop().randomizeStartEndPosition(false, nexCrop.CropMode.PAN_FACE);
            } else if (z) {
                nexclip.getCrop().randomizeStartEndPosition(false, nexCrop.CropMode.FIT);
            }
            if (str2 != null && !str2.equals("null")) {
                if (this.mTemplateID != null) {
                    nexColorEffect lutColorEffect = nexColorEffect.getLutColorEffect(str2);
                    if (lutColorEffect != null) {
                        Log.d(TAG, "lut color effect set =" + lutColorEffect.getPresetName());
                        nexclip.setColorEffect(lutColorEffect);
                    }
                } else {
                    Iterator<nexColorEffect> it = nexColorEffect.getPresetList().iterator();
                    while (true) {
                        if (it.hasNext()) {
                            nexColorEffect next = it.next();
                            if (next.getPresetName().equals(str2)) {
                                nexclip.setColorEffect(next);
                            }
                        }
                    }
                }
            }
            String[] split = str3.split(",");
            if (split[0].equals("clip")) {
                if (split[1].equals("yes")) {
                    nexclip.setVignetteEffect(true);
                } else {
                    nexclip.setVignetteEffect(false);
                }
            } else if (split[0].equals("overlay")) {
                addTemplateOverlay(i, str3);
            }
            int i4 = this.templateListID + 1;
            this.templateListID = i4;
            if (!this.templateList.get(i4).get("type").equals("transition")) {
                return null;
            }
            String str5 = this.templateList.get(this.templateListID).get(TAG_EFFECTS);
            int parseInt7 = Integer.parseInt(this.templateList.get(this.templateListID).get(TAG_DURATION).equals("default") ? "-1" : this.templateList.get(this.templateListID).get(TAG_DURATION));
            if (this.mOverlappedTransition) {
                int[] transitionDurationTimeGuideLine = nexproject.getTransitionDurationTimeGuideLine(i, parseInt7);
                if (transitionDurationTimeGuideLine[0] == 0 && transitionDurationTimeGuideLine[1] == 0) {
                    nexclip.getTransitionEffect(true).setEffectNone();
                    nexclip.getTransitionEffect(true).setDuration(0);
                    return null;
                } else if (str5.equals("none")) {
                    nexclip.getTransitionEffect(true).setEffectNone();
                    nexclip.getTransitionEffect(true).setDuration(0);
                    return null;
                } else {
                    nexclip.getTransitionEffect(true).setTransitionEffect(str5);
                    if (parseInt7 == -1) {
                        return null;
                    }
                    nexclip.getTransitionEffect(true).setDuration(parseInt7);
                    return null;
                }
            }
            nexclip.getTransitionEffect(true).setEffectNone();
            nexclip.getTransitionEffect(true).setDuration(0);
            return null;
        } catch (nexSDKException e) {
            return e.getMessage();
        }
    }

    public String setProperty2ImageClip(nexClip nexclip, int i) {
        Object obj;
        boolean z;
        if (this.templateList.get(this.templateListID).get("type").equals("scene")) {
            int parseInt = Integer.parseInt(this.templateList.get(this.templateListID).get(TAG_CONTRAST).equals("default") ? "-1" : this.templateList.get(this.templateListID).get(TAG_CONTRAST));
            int parseInt2 = Integer.parseInt(this.templateList.get(this.templateListID).get(TAG_BRIGHTNESS).equals("default") ? "-1" : this.templateList.get(this.templateListID).get(TAG_BRIGHTNESS));
            int parseInt3 = Integer.parseInt(this.templateList.get(this.templateListID).get(TAG_SATURATION).equals("default") ? "-1" : this.templateList.get(this.templateListID).get(TAG_SATURATION));
            int parseInt4 = Integer.parseInt(this.templateList.get(this.templateListID).get(TAG_COLOR_FILTER).equals("default") ? "-1" : this.templateList.get(this.templateListID).get(TAG_COLOR_FILTER));
            String str = this.templateList.get(this.templateListID).get(TAG_EFFECTS);
            String str2 = this.templateList.get(this.templateListID).get(TAG_LUT);
            String str3 = this.templateList.get(this.templateListID).get(TAG_VIGNETTE);
            String str4 = this.templateList.get(this.templateListID).get(TAG_CROP_MODE);
            if (str4.equals("")) {
                str4 = this.templateList.get(this.templateListID).get(TAG_IMAGE_CROP_MODE);
            }
            try {
                nexclip.setClipVolume(0);
                if (str.equals("none")) {
                    nexclip.getClipEffect(true).setEffectNone();
                    obj = "none";
                } else {
                    obj = "none";
                    nexclip.getClipEffect(true).setEffect(str);
                    nexclip.getClipEffect(true).setEffectShowTime(0, nexclip.getProjectEndTime() - nexclip.getProjectStartTime());
                }
                if (parseInt2 != -1) {
                    nexclip.setBrightness(parseInt2);
                }
                if (parseInt != -1) {
                    nexclip.setContrast(parseInt);
                }
                if (parseInt3 != -1) {
                    nexclip.setSaturation(parseInt3);
                }
                if (parseInt4 != -1) {
                    nexclip.setColorEffect(getColorEffect(parseInt4 - 1));
                }
                switch (str4.hashCode()) {
                    case 101393:
                        if (str4.equals("fit")) {
                            z = true;
                            break;
                        }
                        z = true;
                        break;
                    case 3143043:
                        if (str4.equals("fill")) {
                            z = false;
                            break;
                        }
                        z = true;
                        break;
                    case 1054849215:
                        if (str4.equals("pan_face")) {
                            z = true;
                            break;
                        }
                        z = true;
                        break;
                    case 1055207047:
                        if (str4.equals("pan_rand")) {
                            z = true;
                            break;
                        }
                        z = true;
                        break;
                    default:
                        z = true;
                        break;
                }
                if (!z) {
                    nexclip.getCrop().randomizeStartEndPosition(false, nexCrop.CropMode.FILL);
                } else if (z) {
                    nexclip.getCrop().randomizeStartEndPosition(false, nexCrop.CropMode.PAN_RAND);
                } else if (z) {
                    nexclip.getCrop().randomizeStartEndPosition(false, nexCrop.CropMode.PAN_FACE);
                } else if (z) {
                    nexclip.getCrop().randomizeStartEndPosition(false, nexCrop.CropMode.FIT);
                }
                if (str2 != null && !str2.equals("null")) {
                    Iterator<nexColorEffect> it = nexColorEffect.getPresetList().iterator();
                    while (true) {
                        if (it.hasNext()) {
                            nexColorEffect next = it.next();
                            if (next.getPresetName().equals(str2)) {
                                nexclip.setColorEffect(next);
                            }
                        }
                    }
                }
                String[] split = str3.split(",");
                if (split[0].equals("clip")) {
                    if (split[1].equals("yes")) {
                        nexclip.setVignetteEffect(true);
                    } else {
                        nexclip.setVignetteEffect(false);
                    }
                } else if (split[0].equals("overlay")) {
                    addTemplateOverlay(i, str3);
                }
                int i2 = this.templateListID + 1;
                this.templateListID = i2;
                if (!this.templateList.get(i2).get("type").equals("transition")) {
                    return null;
                }
                String str5 = this.templateList.get(this.templateListID).get(TAG_EFFECTS);
                int parseInt5 = Integer.parseInt(this.templateList.get(this.templateListID).get(TAG_DURATION).equals("default") ? "-1" : this.templateList.get(this.templateListID).get(TAG_DURATION));
                if (this.mOverlappedTransition) {
                    if (str5.equals(obj)) {
                        nexclip.getTransitionEffect(true).setEffectNone();
                        nexclip.getTransitionEffect(true).setDuration(0);
                        return null;
                    }
                    nexclip.getTransitionEffect(true).setTransitionEffect(str5);
                    if (parseInt5 == -1) {
                        return null;
                    }
                    nexclip.getTransitionEffect(true).setDuration(parseInt5);
                    return null;
                }
                nexclip.getTransitionEffect(true).setEffectNone();
                nexclip.getTransitionEffect(true).setDuration(0);
                return null;
            } catch (nexSDKException e) {
                return e.getMessage();
            }
        }
        return null;
    }

    public String setProperty2Clips(nexProject nexproject, String str) {
        String property2ImageClip;
        if (nexproject.getTotalClipCount(true) == 0) {
            return "no clip in the project";
        }
        if (str.equals("template 1.0")) {
            this.templateList = this.mTemplateList;
        } else {
            this.templateList = this.mIntroTemplateList;
        }
        this.templateListID = 0;
        int i = 0;
        while (!this.mCancel) {
            nexClip clip = nexproject.getClip(i, true);
            if (clip.getClipType() == 4) {
                String property2VideoClip = setProperty2VideoClip(nexproject, clip, i);
                if (property2VideoClip != null) {
                    return property2VideoClip;
                }
            } else if (clip.getClipType() == 1 && (property2ImageClip = setProperty2ImageClip(clip, i)) != null) {
                return property2ImageClip;
            }
            int i2 = this.templateListID + 1;
            this.templateListID = i2;
            if (i2 == this.templateList.size()) {
                this.templateListID = 0;
            }
            i++;
            if (!str.equals("template 1.0")) {
                if (i == this.mTemplateTypeCountList.get(0).intValue()) {
                    if (this.mTemplateTypeCountList.get(1).intValue() == 0) {
                        this.templateList = this.mOutroTemplateList;
                    } else {
                        this.templateList = this.mLoopTemplateList;
                    }
                    this.templateListID = 0;
                }
                if (this.templateList == this.mLoopTemplateList && i == this.mTemplateTypeCountList.get(0).intValue() + this.mTemplateTypeCountList.get(1).intValue()) {
                    this.templateList = this.mOutroTemplateList;
                    this.templateListID = 0;
                }
            }
            if (i == nexproject.getTotalClipCount(true)) {
                clip.getTransitionEffect(true).setEffectNone();
                clip.getTransitionEffect(true).setDuration(0);
            }
            if (i >= nexproject.getTotalClipCount(true)) {
                nexproject.updateProject();
                return null;
            }
        }
        this.mCancel = false;
        return cancelMassage;
    }

    public String applyTemplateOnProject(nexProject nexproject) {
        c cVar = this.mTemplate;
        if (cVar == null) {
            return "Template did not exist while apply template 2.0";
        }
        int g = cVar.g();
        for (int i = 0; i < nexproject.getTotalClipCount(true); i++) {
            nexClip clip = nexproject.getClip(i, true);
            if (clip.getClipType() == 1) {
                clip.setImageClipDuration(g);
            }
        }
        nexproject.updateProject();
        nexproject.setTemplageOverlappedTransitionMode(this.mOverlappedTransition);
        String a2 = this.mTemplate.a(nexproject, this.mAppContext, this.mResContext, this.mUseProjectSpeed, this.mOverlappedTransition);
        if (a2 == null) {
            return null;
        }
        return a2;
    }

    public nexColorEffect getColorEffect(int i) {
        return nexColorEffect.getPresetList().get(i);
    }

    public static String readFromFile(String str) {
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(new File(str)));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    sb.append(readLine);
                    sb.append("\n");
                } else {
                    bufferedInputStream.close();
                    bufferedInputStream.close();
                    return sb.toString();
                }
            }
        } catch (FileNotFoundException e) {
            return e.getMessage();
        } catch (IOException e2) {
            return e2.getMessage();
        }
    }

    public static String readFromFile(InputStream inputStream) {
        if (inputStream != null) {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder sb = new StringBuilder();
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        sb.append(readLine);
                        sb.append("\n");
                    } else {
                        inputStream.close();
                        return sb.toString();
                    }
                }
            } catch (IOException e) {
                return e.getMessage();
            }
        } else {
            return null;
        }
    }

    public String raw2file(Context context, Context context2, String str) throws Exception {
        int lastIndexOf = str.lastIndexOf(h.g);
        StringBuilder sb = new StringBuilder();
        sb.append(EditorGlobal.f().getAbsolutePath());
        String str2 = File.separator;
        sb.append(str2);
        sb.append(TAG_TEMPLATE);
        sb.append(str2);
        int i = lastIndexOf + 1;
        sb.append(str.substring(i, str.length()));
        String sb2 = sb.toString();
        File file = new File(sb2);
        AssetManager assets = context2.getAssets();
        if (file.isFile()) {
            try {
                AssetFileDescriptor openFd = assets.openFd(str);
                if (file.length() == openFd.getLength()) {
                    openFd.close();
                    Log.d(TAG, "bgm file found in sdcard.");
                    this.mBGMPath = sb2;
                    return sb2;
                }
                openFd.close();
            } catch (IOException unused) {
                Log.d(TAG, "bgm file found in sdcard.");
                this.mBGMPath = sb2;
                return sb2;
            }
        }
        InputStream open = assets.open(str);
        if (open != null) {
            try {
                File file2 = new File(EditorGlobal.f().getAbsolutePath() + str2 + TAG_TEMPLATE);
                if (!file2.exists()) {
                    file2.mkdirs();
                }
                in2sdcard(open, file);
                Log.d(TAG, "bgm file copy assete to sdcard.");
                open.close();
                this.mBGMPath = sb2;
                return sb2;
            } catch (Exception unused2) {
                String substring = str.substring(i, str.length());
                try {
                    in2file(context, open, substring);
                    Log.d(TAG, "bgm file copy assete to temp data.");
                    this.mBGMTempFile = true;
                    String str3 = context.getFilesDir().getAbsolutePath() + File.separator + substring;
                    this.mBGMPath = str3;
                    return str3;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return null;
    }

    public static void in2sdcard(InputStream inputStream, File file) throws IOException {
        byte[] bArr = new byte[1024];
        FileOutputStream fileOutputStream = null;
        try {
            try {
                FileOutputStream fileOutputStream2 = new FileOutputStream(file);
                while (true) {
                    try {
                        int read = inputStream.read(bArr);
                        if (read > 0) {
                            fileOutputStream2.write(bArr, 0, read);
                        } else {
                            fileOutputStream2.close();
                            return;
                        }
                    } catch (IOException e) {
                        throw e;
                    } catch (Throwable th) {
                        th = th;
                        fileOutputStream = fileOutputStream2;
                        if (fileOutputStream != null) {
                            fileOutputStream.close();
                        }
                        throw th;
                    }
                }
            } catch (IOException e2) {
                throw e2;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public void in2file(Context context, InputStream inputStream, String str) throws Exception {
        byte[] bArr = new byte[1024];
        FileOutputStream fileOutputStream = null;
        try {
            try {
                fileOutputStream = context.openFileOutput(str, 1);
                while (true) {
                    int read = inputStream.read(bArr);
                    if (read <= 0) {
                        break;
                    }
                    fileOutputStream.write(bArr, 0, read);
                }
                inputStream.close();
                if (fileOutputStream == null) {
                    return;
                }
                fileOutputStream.close();
            } catch (Exception e) {
                throw e;
            }
        } catch (Throwable th) {
            if (inputStream != null) {
                inputStream.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
            throw th;
        }
    }

    public static String AssetPackageTemplateJsonToString(String str) {
        com.nexstreaming.app.common.nexasset.assetpackage.f c = com.nexstreaming.app.common.nexasset.assetpackage.c.a().c(str);
        if (c == null) {
            Log.e(TAG, "AssetPackageTemplateJsonToString info fail=" + str);
            return null;
        } else if (com.nexstreaming.app.common.nexasset.assetpackage.c.a().a(c.getAssetPackage())) {
            Log.e(TAG, "AssetPackageTemplateJsonToString expire item id=" + str);
            return null;
        } else {
            try {
                AssetPackageReader a2 = AssetPackageReader.a(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), c.getPackageURI(), c.getAssetPackage().getAssetId());
                Log.d(TAG, "Template(" + str + ") Asset(" + c.getAssetPackage().getAssetIdx() + ") version(In DB)=" + c.getAssetPackage().getPackageVersion() + ", version(In reader)=" + a2.b());
                try {
                    try {
                        InputStream a3 = a2.a(c.getFilePath());
                        if (a3 != null) {
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(a3));
                            StringBuilder sb = new StringBuilder();
                            while (true) {
                                String readLine = bufferedReader.readLine();
                                if (readLine != null) {
                                    sb.append(readLine);
                                    sb.append("\n");
                                } else {
                                    a3.close();
                                    return sb.toString();
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                } finally {
                    com.nexstreaming.app.common.util.b.a(a2);
                }
            } catch (IOException e2) {
                e2.printStackTrace();
                return null;
            }
        }
    }

    public void setCancel() {
        this.mCancel = true;
        if (this.mTemplate != null) {
            Log.d(TAG, "setCancel");
            this.mTemplate.h();
            return;
        }
        Log.d(TAG, "setCancel mTemplate is null");
    }
}
