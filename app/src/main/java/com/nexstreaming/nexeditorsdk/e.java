package com.nexstreaming.nexeditorsdk;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import com.nexstreaming.nexeditorsdk.exception.nexSDKException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: nexTemplateEffect.java */
/* loaded from: classes3.dex */
final class e {
    private static String L = "nexTemplateEffect";
    public int G;
    public int I;
    public int a;
    public String b;
    public String c;
    public String d;
    public String e;
    public int f;
    public int g;
    public int h;
    public int i;
    public String j;
    public String u;
    public String v;
    public String w;
    public String x;
    public String y;
    public int k = -1;
    public int l = -1;
    public int m = -1;
    public int n = -1;
    public int o = 100;
    public int p = 0;
    public String q = "default";
    public int r = 101;
    public String s = "";
    public String t = "";
    public Map<String, String> z = new HashMap();
    public Map<String, String> A = new HashMap();
    public Map<String, String> B = new HashMap();
    public Map<String, String> C = new HashMap();
    public Map<String, String> D = new HashMap();
    public boolean E = true;
    public String F = "none";
    public String H = "none";
    public int J = 1;
    public List<d> K = new ArrayList();

    public static e a(JSONObject jSONObject, int i) {
        String a = a(jSONObject, nexExportFormat.TAG_FORMAT_TYPE);
        if (!a.equals("scene")) {
            return null;
        }
        e eVar = new e();
        eVar.a = i;
        eVar.b = a;
        eVar.c = a(jSONObject, "identifier");
        if (jSONObject.has("alternative_id")) {
            eVar.c = a(jSONObject, "alternative_id");
        }
        String a2 = a(jSONObject, "source_type");
        eVar.d = a2;
        if (!a2.equals("res_default")) {
            eVar.e = a(jSONObject, "res_path");
        }
        eVar.f = Integer.parseInt(a(jSONObject, "duration"));
        eVar.h = Integer.parseInt(a(jSONObject, "duration_max"));
        eVar.g = Integer.parseInt(a(jSONObject, "duration_min"));
        eVar.i = Integer.parseInt(a(jSONObject, "volume"));
        if (jSONObject.has("effect")) {
            eVar.j = a(jSONObject, "effect");
        } else {
            eVar.j = a(jSONObject, "effects");
        }
        if (jSONObject.has("draw_infos")) {
            try {
                int i2 = 1;
                Log.d(L, String.format("Effect : %s", eVar.j));
                JSONArray jSONArray = jSONObject.getJSONArray("draw_infos");
                for (int i3 = 0; i3 < jSONArray.length(); i3++) {
                    d a3 = d.a(jSONArray.getJSONObject(i3), (i << 16) | i2);
                    if (a3 != null) {
                        eVar.K.add(a3);
                        a3.a();
                    }
                    i2++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
                eVar.K.clear();
            }
            if (a(jSONObject, "apply_effect_on_res").equals("0")) {
                eVar.E = false;
            }
        }
        String a4 = a(jSONObject, "speed_control");
        if (!a4.equals("default")) {
            eVar.o = Integer.parseInt(a4);
        }
        String a5 = a(jSONObject, "brightness");
        if (!a5.equals("default")) {
            eVar.k = Integer.parseInt(a5);
        }
        String a6 = a(jSONObject, "contrast");
        if (!a6.equals("default")) {
            eVar.l = Integer.parseInt(a6);
        }
        String a7 = a(jSONObject, "saturation");
        if (!a7.equals("default")) {
            eVar.m = Integer.parseInt(a7);
        }
        String a8 = a(jSONObject, "color_filter");
        if (!a8.equals("default")) {
            eVar.n = Integer.parseInt(a8);
        }
        eVar.y = a(jSONObject, "vignette");
        eVar.u = a(jSONObject, "lut");
        eVar.v = a(jSONObject, "external_video_path");
        eVar.w = a(jSONObject, "external_image_path");
        eVar.x = a(jSONObject, "solid_color");
        if (jSONObject.has("apply_effect_on_res") && a(jSONObject, "apply_effect_on_res").equals("0")) {
            eVar.E = false;
        }
        if (jSONObject.has("audio_res")) {
            String a9 = a(jSONObject, "audio_res");
            if (!a9.equals("none")) {
                String a10 = a(jSONObject, "audio_res_pos");
                eVar.F = a9;
                eVar.G = Integer.parseInt(a10);
            }
        }
        eVar.q = a(jSONObject, "crop_mode");
        if (jSONObject.has("video_crop_mode")) {
            eVar.s = a(jSONObject, "video_crop_mode");
        }
        if (jSONObject.has("image_crop_mode")) {
            eVar.t = a(jSONObject, "image_crop_mode");
        }
        if (jSONObject.has("image_crop_speed")) {
            eVar.r = Integer.parseInt(a(jSONObject, "image_crop_speed"));
        }
        if (jSONObject.has("freeze_duration")) {
            eVar.p = Integer.parseInt(a(jSONObject, "freeze_duration"));
        }
        String a11 = a(jSONObject, "transition_name");
        eVar.H = a11;
        if (a11 == null || a11.compareTo("none") == 0) {
            eVar.I = 0;
        } else {
            eVar.I = Integer.parseInt(a(jSONObject, "transition_duration"));
        }
        if (jSONObject.has("use_particle_video")) {
            eVar.J = Integer.parseInt(a(jSONObject, "use_particle_video"));
        }
        if (jSONObject.has("alternative_effect")) {
            try {
                eVar.z.clear();
                JSONObject jSONObject2 = jSONObject.getJSONObject("alternative_effect");
                Iterator<String> keys = jSONObject2.keys();
                while (keys.hasNext()) {
                    String next = keys.next();
                    eVar.z.put(next, jSONObject2.getString(next));
                }
            } catch (JSONException e2) {
                e2.printStackTrace();
                eVar.z.clear();
            }
        }
        if (jSONObject.has("alternative_transition")) {
            try {
                eVar.A.clear();
                JSONObject jSONObject3 = jSONObject.getJSONObject("alternative_transition");
                Iterator<String> keys2 = jSONObject3.keys();
                while (keys2.hasNext()) {
                    String next2 = keys2.next();
                    eVar.A.put(next2, jSONObject3.getString(next2));
                }
            } catch (JSONException e3) {
                e3.printStackTrace();
                eVar.A.clear();
            }
        }
        if (jSONObject.has("alternative_lut")) {
            try {
                eVar.B.clear();
                JSONObject jSONObject4 = jSONObject.getJSONObject("alternative_lut");
                Iterator<String> keys3 = jSONObject4.keys();
                while (keys3.hasNext()) {
                    String next3 = keys3.next();
                    eVar.B.put(next3, jSONObject4.getString(next3));
                }
            } catch (JSONException e4) {
                e4.printStackTrace();
                eVar.B.clear();
            }
        }
        if (jSONObject.has("alternative_audio")) {
            try {
                eVar.C.clear();
                JSONObject jSONObject5 = jSONObject.getJSONObject("alternative_audio");
                Iterator<String> keys4 = jSONObject5.keys();
                while (keys4.hasNext()) {
                    String next4 = keys4.next();
                    eVar.C.put(next4, jSONObject5.getString(next4));
                }
            } catch (JSONException e5) {
                e5.printStackTrace();
                eVar.C.clear();
            }
        }
        if (jSONObject.has("alternative_audio_pos")) {
            try {
                eVar.D.clear();
                JSONObject jSONObject6 = jSONObject.getJSONObject("alternative_audio_pos");
                Iterator<String> keys5 = jSONObject6.keys();
                while (keys5.hasNext()) {
                    String next5 = keys5.next();
                    eVar.D.put(next5, jSONObject6.getString(next5));
                }
            } catch (JSONException e6) {
                e6.printStackTrace();
                eVar.D.clear();
            }
        }
        return eVar;
    }

    public String a(String str, nexClip nexclip, boolean z, float f, boolean z2) {
        Log.d(L, String.format("Template applyEffect %s  clip(%d %d) effect(%s) apply_transition(%b) transition(%s, %d)", str, Integer.valueOf(nexclip.getProjectStartTime()), Integer.valueOf(nexclip.getProjectDuration()), this.j, Boolean.valueOf(z), this.H, Integer.valueOf(this.I)));
        int i = this.a;
        if (z) {
            i |= nexEngine.ExportHEVCMainTierLevel62;
        }
        nexclip.setTemplateEffectID(i);
        try {
            if (nexclip.getClipType() == 4) {
                int i2 = this.i;
                if (i2 != -1) {
                    if (i2 == 0) {
                        nexclip.setAudioOnOff(false);
                    } else {
                        nexclip.setAudioOnOff(true);
                        nexclip.setClipVolume(this.i);
                    }
                }
                if (!z2 && this.o != -1) {
                    nexclip.getVideoClipEdit().setSpeedControl(this.o);
                }
                Log.d(L, String.format("freeze_duration(%d)", Integer.valueOf(this.p)));
                if (this.p != 0) {
                    nexclip.getVideoClipEdit().setFreezeDuration(this.p);
                }
            }
            if (this.j.equals("none")) {
                nexclip.getClipEffect(true).setEffectNone();
            } else {
                nexclip.getClipEffect(true).setEffect(this.j);
            }
            nexclip.getClipEffect(true).setEffectShowTime(0, 0);
            a(nexclip);
            a(nexclip, f);
            b(nexclip);
            if (z && !this.H.equals("none") && this.I > 0) {
                if (nexclip.getProjectDuration() <= this.I) {
                    Log.d(L, String.format("Template Apply transition was ignored on short clip", new Object[0]));
                    nexclip.getTransitionEffect(true).setEffectNone();
                    nexclip.getTransitionEffect(true).setDuration(0);
                    return null;
                }
                nexclip.getTransitionEffect(true).setTransitionEffect(this.H);
                nexclip.getTransitionEffect(true).setDuration(this.I);
                return null;
            }
            nexclip.getTransitionEffect(true).setEffectNone();
            nexclip.getTransitionEffect(true).setDuration(0);
            return null;
        } catch (nexSDKException e) {
            return e.getMessage();
        }
    }

    public void a(nexProject nexproject, nexClip nexclip, String str, boolean z) {
        String str2;
        nexClip supportedClip;
        nexColorEffect lutColorEffect;
        if (z && str != null && str.length() > 0) {
            if (this.z.containsKey(str)) {
                nexclip.getClipEffect(true).setEffect(this.z.get(str));
            }
            if (this.A.containsKey(str)) {
                nexclip.getTransitionEffect(true).setTransitionEffect(this.A.get(str));
            }
            if (this.B.containsKey(str) && !this.B.get(str).equals("null") && (lutColorEffect = nexColorEffect.getLutColorEffect(this.B.get(str))) != null) {
                nexclip.setColorEffect(lutColorEffect);
            }
            if (!this.C.containsKey(str) || (str2 = this.C.get(str)) == null || str2.length() <= 0 || str2.equals("none") || (supportedClip = nexClip.getSupportedClip(nexAssetPackageManager.getAssetPackageMediaPath(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), str2))) == null) {
                return;
            }
            int totalTime = supportedClip.getTotalTime();
            int parseInt = Integer.parseInt(this.D.get(str));
            supportedClip.setTemplateEffectID(this.a);
            supportedClip.setTemplateAudioPos(parseInt);
            for (int i = 0; i < nexproject.getTotalClipCount(false); i++) {
                nexClip clip = nexproject.getClip(0, false);
                if (clip != null && clip.getTemplateEffectID() == this.a) {
                    nexproject.remove(clip);
                }
            }
            nexproject.addAudio(supportedClip, nexclip.getProjectStartTime(), nexclip.getProjectStartTime() + totalTime);
        }
    }

    public boolean a(nexProject nexproject, Context context, float f) {
        nexClip nexclip = null;
        if (this.d.equals("res_video")) {
            String assetPackageMediaPath = nexAssetPackageManager.getAssetPackageMediaPath(context, this.e);
            if (assetPackageMediaPath != null) {
                nexclip = nexClip.getSupportedClip(assetPackageMediaPath);
            }
            String str = L;
            Log.d(str, String.format("Template applyResource2Project file : ", new Object[0]) + assetPackageMediaPath);
            if (nexclip != null) {
                nexproject.add(nexclip);
                nexproject.updateProject();
                nexclip.setAssetResource(true);
                a("res : ", nexclip, true, f, false);
                a(nexproject, nexclip);
            }
            return true;
        } else if (this.d.equals("res_image")) {
            String assetPackageMediaPath2 = nexAssetPackageManager.getAssetPackageMediaPath(context, this.e);
            if (assetPackageMediaPath2 != null) {
                nexclip = nexClip.getSupportedClip(assetPackageMediaPath2);
            }
            if (nexclip != null) {
                nexclip.setImageClipDuration(this.f + this.I);
                nexproject.add(nexclip);
                nexproject.updateProject();
                nexclip.setAssetResource(true);
                a("res : ", nexclip, true, f, false);
                a(nexproject, nexclip);
            }
            return true;
        } else if (!this.d.equals("res_solid")) {
            return false;
        } else {
            nexClip solidClip = nexClip.getSolidClip(Color.parseColor(this.e));
            if (solidClip != null) {
                solidClip.setImageClipDuration(this.f + this.I);
                nexproject.add(solidClip);
                nexproject.updateProject();
                solidClip.setAssetResource(true);
                a("res : ", solidClip, true, f, false);
                a(nexproject, solidClip);
            }
            return true;
        }
    }

    public boolean a() {
        return this.d.equals("res_video") || this.d.equals("res_image") || this.d.equals("res_solid");
    }

    public String b() {
        return this.d;
    }

    public int c() {
        return this.f;
    }

    public int d() {
        return this.g;
    }

    public int e() {
        return this.h;
    }

    public boolean f() {
        return this.E;
    }

    public boolean g() {
        return this.J == 1;
    }

    public int h() {
        return this.I;
    }

    public int a(boolean z) {
        List<d> list = this.K;
        int i = 0;
        if (list != null) {
            for (d dVar : list) {
                if (!z || dVar.b.compareTo("res_default") == 0 || dVar.b.compareTo("user") == 0) {
                    i++;
                }
            }
        }
        return i;
    }

    public List<nexDrawInfo> a(int i, int i2, String str) {
        ArrayList arrayList = new ArrayList();
        if (i2 == 0) {
            i2 = this.f;
        }
        nexDrawInfo nexdrawinfo = new nexDrawInfo();
        nexdrawinfo.setID(this.a);
        nexdrawinfo.setSubEffectID((this.a << 16) | 1);
        if (str != null && this.z.containsKey(str)) {
            nexdrawinfo.setEffectID(this.z.get(str));
        } else {
            nexdrawinfo.setEffectID(this.j);
        }
        nexdrawinfo.setStartTime(i);
        nexdrawinfo.setEndTime(i + i2);
        nexdrawinfo.setSubTemplateDrawInfos(this.K);
        arrayList.add(nexdrawinfo);
        if (this.H.compareTo("none") != 0 && this.I > 0) {
            nexDrawInfo nexdrawinfo2 = new nexDrawInfo();
            if (str != null && this.A.containsKey(str)) {
                nexdrawinfo2.setEffectID(this.A.get(str));
            } else {
                nexdrawinfo2.setEffectID(this.H);
            }
            nexdrawinfo2.setIsTransition(1);
            nexdrawinfo2.setStartTime(nexdrawinfo.getEndTime() - this.I);
            nexdrawinfo2.setEndTime(nexdrawinfo.getEndTime());
            arrayList.add(nexdrawinfo2);
        }
        return arrayList;
    }

    public int i() {
        return this.f;
    }

    public List<d> j() {
        return this.K;
    }

    public static String a(JSONObject jSONObject, String str) {
        try {
            return jSONObject.getString(str);
        } catch (JSONException unused) {
            if (str.equals("effects")) {
                return "none";
            }
            if (str.equals("id") || str.equals("transition_duration") || str.equals("audio_res_pos")) {
                return "0";
            }
            if (str.equals("volume") || str.equals("speed_control")) {
                return "100";
            }
            if (str.equals("duration")) {
                return "3000";
            }
            if (str.equals("duration_max")) {
                return "5000";
            }
            if (str.equals("duration_min")) {
                return "2000";
            }
            if (str.equals("brightness") || str.equals("contrast") || str.equals("saturation") || str.equals("color_filter")) {
                return "-1";
            }
            if (str.equals("source_type")) {
                return "ALL";
            }
            if (str.equals("external_video_path") || str.equals("external_image_path") || str.equals("solid_color") || str.equals("lut")) {
                return null;
            }
            return str.equals("vignette") ? "clip,no" : str.equals("crop_mode") ? "default" : str.equals("transition_name") ? "none" : str.equals("identifier") ? "" : str.equals("freeze_duration") ? "0" : str.equals("use_particle_video") ? "1" : "default";
        }
    }

    public nexColorEffect a(int i) {
        return nexColorEffect.getPresetList().get(i);
    }

    public String a(nexClip nexclip) {
        int i = this.k;
        if (i != -1) {
            nexclip.setBrightness(i);
        }
        int i2 = this.l;
        if (i2 != -1) {
            nexclip.setContrast(i2);
        }
        int i3 = this.m;
        if (i3 != -1) {
            nexclip.setSaturation(i3);
        }
        int i4 = this.n;
        if (i4 != -1) {
            nexclip.setColorEffect(a(i4 - 1));
            return null;
        }
        return null;
    }

    public String a(nexProject nexproject, nexClip nexclip) {
        String str = this.F;
        if (str == null || str.length() <= 0 || this.F.equals("none")) {
            return null;
        }
        nexproject.updateProject();
        nexClip supportedClip = nexClip.getSupportedClip(nexAssetPackageManager.getAssetPackageMediaPath(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), this.F));
        if (supportedClip == null) {
            return null;
        }
        int totalTime = supportedClip.getTotalTime();
        supportedClip.setTemplateEffectID(this.a);
        supportedClip.setTemplateAudioPos(this.G);
        nexproject.addAudio(supportedClip, nexclip.getProjectStartTime(), nexclip.getProjectStartTime() + totalTime);
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:64:0x00eb  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x00f5  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String a(com.nexstreaming.nexeditorsdk.nexClip r14, float r15) {
        /*
            Method dump skipped, instructions count: 366
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.nexeditorsdk.e.a(com.nexstreaming.nexeditorsdk.nexClip, float):java.lang.String");
    }

    public String b(nexClip nexclip) {
        nexColorEffect lutColorEffect;
        String str = this.u;
        if (str == null || str.equals("null") || (lutColorEffect = nexColorEffect.getLutColorEffect(this.u)) == null) {
            return null;
        }
        String str2 = L;
        Log.d(str2, "Template applyLUT2Clip effect set =xte" + lutColorEffect.getPresetName());
        nexclip.setColorEffect(lutColorEffect);
        return null;
    }
}
