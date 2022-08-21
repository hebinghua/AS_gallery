package com.nexstreaming.nexeditorsdk;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: nexTemplateDrawInfo.java */
/* loaded from: classes3.dex */
public final class d {
    private static String r = "nexTemplateDrawInfo";
    public float d;
    public float e;
    public float k;
    public float l;
    public float q;
    public int a = 0;
    public String b = "";
    public String c = "";
    public nexColorEffect f = null;
    public Map<String, String> g = new HashMap();
    public String h = "default";
    public String i = "";
    public String j = "";
    public int m = 0;
    public int n = 0;
    public int o = 0;
    public String p = "none";

    public static String a(JSONObject jSONObject, String str) {
        try {
            return jSONObject.getString(str);
        } catch (JSONException unused) {
            return str.equals("source_type") ? "ALL" : str.equals("source_path") ? "default" : str.equals("start") ? "0" : str.equals("end") ? "1" : (str.equals("crop_mode") || str.equals("video_crop_mode") || str.equals("image_crop_speed")) ? "default" : str.equals("lut") ? "null" : (str.equals("draw_width") || str.equals("draw_height") || str.equals("volume") || str.equals("system_source_width") || str.equals("system_source_height")) ? "0" : str.equals("audio_res") ? "none" : str.equals("audio_res_pos") ? "0" : "default";
        }
    }

    public static d a(JSONObject jSONObject, int i) {
        d dVar = new d();
        dVar.a = i;
        String a = a(jSONObject, "source_type");
        dVar.b = a;
        if (!a.equals("user")) {
            dVar.c = a(jSONObject, "source_path");
            if (jSONObject.has("system_source_width")) {
                dVar.m = Integer.parseInt(a(jSONObject, "system_source_width"));
            }
            if (jSONObject.has("system_source_height")) {
                dVar.n = Integer.parseInt(a(jSONObject, "system_source_height"));
            }
        }
        dVar.d = Float.parseFloat(a(jSONObject, "start"));
        dVar.e = Float.parseFloat(a(jSONObject, "end"));
        String a2 = a(jSONObject, "lut");
        if (a2 != null && a2.compareTo("null") != 0 && a2.compareTo("none") != 0) {
            dVar.f = nexColorEffect.getLutColorEffect(a2);
        }
        if (jSONObject.has("alternative_lut")) {
            try {
                dVar.g.clear();
                JSONObject jSONObject2 = jSONObject.getJSONObject("alternative_lut");
                Iterator<String> keys = jSONObject2.keys();
                while (keys.hasNext()) {
                    String next = keys.next();
                    dVar.g.put(next, jSONObject2.getString(next));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                dVar.g.clear();
            }
        }
        dVar.h = a(jSONObject, "crop_mode");
        dVar.i = a(jSONObject, "video_crop_mode");
        dVar.j = a(jSONObject, "image_crop_speed");
        dVar.k = Float.parseFloat(a(jSONObject, "draw_width"));
        dVar.l = Float.parseFloat(a(jSONObject, "draw_height"));
        dVar.o = Integer.parseInt(a(jSONObject, "volume"));
        if (jSONObject.has("audio_res")) {
            String a3 = a(jSONObject, "audio_res");
            if (!a3.equals("none")) {
                String a4 = a(jSONObject, "audio_res_pos");
                dVar.p = a3;
                dVar.q = Float.parseFloat(a4);
            }
        }
        return dVar;
    }

    public boolean a(nexProject nexproject, Context context, e eVar, int i, float f) {
        String str;
        if ((this.b.compareTo("system") == 0 || this.b.compareTo("system_mt") == 0) && (str = this.c) != null && str.length() > 0) {
            nexClip nexclip = null;
            String assetPackageMediaPath = nexAssetPackageManager.getAssetPackageMediaPath(context, this.c);
            if (assetPackageMediaPath != null) {
                nexclip = nexClip.getSupportedClip(assetPackageMediaPath);
            }
            nexClip nexclip2 = nexclip;
            if (nexclip2 != null) {
                nexclip2.setAssetResource(true);
                if (this.b.compareTo("system_mt") == 0) {
                    nexclip2.setMotionTrackedVideo(true);
                }
                nexproject.add(nexclip2);
                nexclip2.clearDrawInfos();
                nexclip2.mStartTime = i;
                nexclip2.mEndTime = nexclip2.getTotalTime() + i;
                a(nexclip2, eVar.a, eVar.i(), i, f, null, false);
            }
            return true;
        }
        return false;
    }

    public void a(nexClip nexclip, int i, int i2, int i3, float f, String str, boolean z) {
        if (nexclip == null) {
            return;
        }
        nexDrawInfo nexdrawinfo = new nexDrawInfo();
        nexdrawinfo.setTopEffectID(i);
        nexdrawinfo.setID(this.a);
        nexdrawinfo.setSubEffectID(this.a);
        float f2 = i2;
        int i4 = ((int) (this.d * f2)) + i3;
        int i5 = ((int) (this.e * f2)) + i3;
        int i6 = 0;
        Log.d(r, String.format("Template setDrawInfo2Clip(dur:%d start:%d %d %d)", Integer.valueOf(i2), Integer.valueOf(i3), Integer.valueOf(i4), Integer.valueOf(i5)));
        if (nexclip.getClipType() == 1) {
            int i7 = nexclip.mStartTime;
            if (i4 < i7) {
                i7 = i4;
            }
            nexclip.mStartTime = i7;
            int i8 = nexclip.mEndTime;
            if (i5 > i8) {
                i8 = i5;
            }
            nexclip.mEndTime = i8;
            nexclip.setImageClipDuration(i8 - i7);
        } else {
            int i9 = nexclip.mStartTime;
            if (i4 < i9) {
                i9 = i4;
            }
            nexclip.mStartTime = i9;
            int i10 = nexclip.mEndTime;
            if (i5 > i10) {
                i10 = i5;
            }
            nexclip.mEndTime = i10;
            if (z) {
                int i11 = (int) (f2 * (this.e - this.d));
                if (i11 >= nexclip.getVideoDuration()) {
                    i11 = nexclip.getVideoDuration();
                }
                nexclip.getVideoClipEdit().setTrim(0, i11);
            }
            nexclip.setClipVolume(this.o);
        }
        nexdrawinfo.setStartTime(i4);
        nexdrawinfo.setEndTime(i5);
        if (str != null && this.g.containsKey(str)) {
            nexColorEffect lutColorEffect = nexColorEffect.getLutColorEffect(this.g.get(str));
            if (lutColorEffect != null) {
                i6 = lutColorEffect.getLUTId();
            }
            nexdrawinfo.setLUT(i6);
        } else {
            nexColorEffect nexcoloreffect = this.f;
            if (nexcoloreffect != null) {
                i6 = nexcoloreffect.getLUTId();
            }
            nexdrawinfo.setLUT(i6);
        }
        float f3 = this.k;
        if (f3 != 0.0f) {
            float f4 = this.l;
            if (f4 != 0.0f) {
                f = f3 / f4;
            }
        }
        nexdrawinfo.setRatio(f);
        a(nexclip, f);
        Rect rect = new Rect();
        Rect rect2 = new Rect();
        nexclip.getCrop().getStartPositionRaw(rect);
        nexclip.getCrop().getEndPositionRaw(rect2);
        nexdrawinfo.setRotateState(nexclip.getRotateDegree());
        nexdrawinfo.setStartRect(rect);
        nexdrawinfo.setEndRect(rect2);
        nexclip.addDrawInfo(nexdrawinfo);
    }

    /* JADX WARN: Removed duplicated region for block: B:57:0x00d0  */
    /* JADX WARN: Removed duplicated region for block: B:58:0x00da  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String a(com.nexstreaming.nexeditorsdk.nexClip r14, float r15) {
        /*
            Method dump skipped, instructions count: 340
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.nexeditorsdk.d.a(com.nexstreaming.nexeditorsdk.nexClip, float):java.lang.String");
    }

    public String a(nexProject nexproject, int i, int i2) {
        String str = this.p;
        if (str == null || str.length() <= 0 || this.p.equals("none")) {
            return null;
        }
        nexproject.updateProject();
        nexClip supportedClip = nexClip.getSupportedClip(nexAssetPackageManager.getAssetPackageMediaPath(com.nexstreaming.kminternal.kinemaster.config.a.a().b(), this.p));
        if (supportedClip == null) {
            return null;
        }
        int totalTime = supportedClip.getTotalTime();
        supportedClip.setAssetResource(true);
        int i3 = i + ((int) (i2 * this.q));
        nexproject.addAudio(supportedClip, i3, totalTime + i3);
        return null;
    }

    public void a() {
        Log.d(r, String.format("subId : %d", Integer.valueOf(this.a)));
        Log.d(r, String.format("start : %f", Float.valueOf(this.d)));
        Log.d(r, String.format("end : %f", Float.valueOf(this.e)));
        String str = r;
        Object[] objArr = new Object[1];
        nexColorEffect nexcoloreffect = this.f;
        objArr[0] = Integer.valueOf(nexcoloreffect == null ? 0 : nexcoloreffect.getLUTId());
        Log.d(str, String.format("lut : %d", objArr));
        Log.d(r, String.format("cropMode : %s", this.h));
        Log.d(r, String.format("videoCropMode : %s", this.i));
        Log.d(r, String.format("draw size : %f %f", Float.valueOf(this.k), Float.valueOf(this.l)));
        Log.d(r, String.format("volume : %d", Integer.valueOf(this.o)));
        Log.d(r, "---------------------------------------------------");
    }
}
