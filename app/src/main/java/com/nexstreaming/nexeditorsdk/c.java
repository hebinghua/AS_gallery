package com.nexstreaming.nexeditorsdk;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: nexTemplate.java */
/* loaded from: classes3.dex */
public final class c {
    private static String w = "nexTemplate";
    public String a;
    public String b;
    public String c;
    public String d;
    public String e;
    public String f;
    public int n;
    public String o;
    public float q;
    private int x = 1;
    public float g = 1.0f;
    public int h = 2500;
    public int i = -1;
    public int j = -1;
    public int k = -1;
    public int l = -1;
    public int m = 0;
    public boolean p = true;
    public boolean r = false;
    public String s = "";
    public boolean t = true;
    private boolean D = true;
    private int E = 10368000;
    private boolean F = false;
    public g u = null;
    public boolean v = false;
    private ArrayList<e> y = new ArrayList<>();
    private ArrayList<e> z = new ArrayList<>();
    private ArrayList<e> A = new ArrayList<>();
    private ArrayList<e> B = new ArrayList<>();
    private Map<String, ArrayList<e>> C = new HashMap();

    public void a() {
        this.F = false;
        ArrayList<e> arrayList = this.y;
        if (arrayList != null) {
            arrayList.clear();
        }
        ArrayList<e> arrayList2 = this.z;
        if (arrayList2 != null) {
            arrayList2.clear();
        }
        ArrayList<e> arrayList3 = this.A;
        if (arrayList3 != null) {
            arrayList3.clear();
        }
        ArrayList<e> arrayList4 = this.B;
        if (arrayList4 != null) {
            arrayList4.clear();
        }
    }

    public String a(JSONObject jSONObject) {
        try {
            this.a = jSONObject.getString("template_name");
            this.b = jSONObject.getString("template_version");
            this.c = jSONObject.getString("template_desc");
            String string = jSONObject.getString("template_mode");
            this.d = string;
            this.q = 1.7777778f;
            if (string.equals("16v9")) {
                this.q = 1.7777778f;
            } else if (this.d.equals("9v16")) {
                this.q = 0.5625f;
            } else if (this.d.equals("1v1")) {
                this.q = 1.0f;
            } else if (this.d.equals("2v1")) {
                this.q = 2.0f;
            } else if (this.d.equals("1v2")) {
                this.q = 0.5f;
            }
            this.e = jSONObject.getString("template_bgm");
            if (jSONObject.has("template_single_bgm")) {
                this.f = jSONObject.getString("template_single_bgm");
            }
            if (jSONObject.has("template_bgm_volume")) {
                this.g = Float.parseFloat(jSONObject.getString("template_bgm_volume"));
            }
            if (jSONObject.has("template_min_duration")) {
                this.n = Integer.parseInt(jSONObject.getString("template_min_duration"));
            }
            if (jSONObject.has("template_default_effect")) {
                this.o = jSONObject.getString("template_default_effect");
            }
            if (jSONObject.has("template_default_effect_scale")) {
                this.p = !jSONObject.getString("template_default_effect_scale").equals("0");
            }
            if (jSONObject.has("template_default_image_duration") && !jSONObject.getString("template_default_image_duration").equals("default")) {
                this.h = Integer.parseInt(jSONObject.getString("template_default_image_duration"));
            }
            if (jSONObject.has("template_project_vol_fade_in_time") && !jSONObject.getString("template_project_vol_fade_in_time").equals("default")) {
                this.i = Integer.parseInt(jSONObject.getString("template_project_vol_fade_in_time"));
            }
            if (jSONObject.has("template_project_vol_fade_out_time") && !jSONObject.getString("template_project_vol_fade_out_time").equals("default")) {
                this.j = Integer.parseInt(jSONObject.getString("template_project_vol_fade_out_time"));
            }
            if (jSONObject.has("template_single_project_vol_fade_in_time") && !jSONObject.getString("template_single_project_vol_fade_in_time").equals("default")) {
                this.k = Integer.parseInt(jSONObject.getString("template_single_project_vol_fade_in_time"));
            }
            if (jSONObject.has("template_single_project_vol_fade_out_time") && !jSONObject.getString("template_single_project_vol_fade_out_time").equals("default")) {
                this.l = Integer.parseInt(jSONObject.getString("template_single_project_vol_fade_out_time"));
            }
            if (!jSONObject.has("template_single_video")) {
                return null;
            }
            this.m = Integer.parseInt(jSONObject.getString("template_single_video"));
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            String str = w;
            Log.d(str, "parse Template failed : " + e.getMessage());
            Log.d(w, "case1 1");
            return e.getMessage();
        }
    }

    public String a(String str, JSONObject jSONObject) {
        this.F = false;
        if (str != null) {
            File file = new File(str);
            Log.d(w, String.format("Template templateFile path(%s)", file.getAbsolutePath()));
            file.getAbsolutePath().endsWith("txt");
        }
        this.x = 1;
        String a = a(jSONObject);
        if (a != null) {
            return "Template header parse error : " + a;
        }
        try {
            JSONArray jSONArray = jSONObject.getJSONArray("template_intro");
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                int i2 = this.x;
                this.x = i2 + 1;
                e a2 = e.a(jSONObject2, i2);
                if (a2 == null) {
                    return "Template intro parse error";
                }
                this.y.add(a2);
            }
            JSONArray jSONArray2 = jSONObject.getJSONArray("template_loop");
            for (int i3 = 0; i3 < jSONArray2.length(); i3++) {
                JSONObject jSONObject3 = jSONArray2.getJSONObject(i3);
                int i4 = this.x;
                this.x = i4 + 1;
                e a3 = e.a(jSONObject3, i4);
                if (a3 == null) {
                    return "Template Loop parse error";
                }
                this.z.add(a3);
            }
            JSONArray jSONArray3 = jSONObject.getJSONArray("template_outro");
            for (int i5 = 0; i5 < jSONArray3.length(); i5++) {
                JSONObject jSONObject4 = jSONArray3.getJSONObject(i5);
                if (jSONObject4 != null) {
                    if (jSONObject4.has("alternative_id") && jSONObject4.has("alternative_outro")) {
                        String string = jSONObject4.getString("alternative_id");
                        JSONArray jSONArray4 = jSONObject4.getJSONArray("alternative_outro");
                        ArrayList<e> arrayList = new ArrayList<>();
                        for (int i6 = 0; i6 < jSONArray4.length(); i6++) {
                            JSONObject jSONObject5 = jSONArray4.getJSONObject(i6);
                            int i7 = this.x;
                            this.x = i7 + 1;
                            e a4 = e.a(jSONObject5, i7);
                            if (a4 == null) {
                                return "Template Outro parse error(alternative outro)";
                            }
                            arrayList.add(a4);
                        }
                        this.C.put(string, arrayList);
                    } else {
                        JSONObject jSONObject6 = jSONArray3.getJSONObject(i5);
                        int i8 = this.x;
                        this.x = i8 + 1;
                        e a5 = e.a(jSONObject6, i8);
                        if (a5 == null) {
                            return "Template Outro parse error";
                        }
                        this.A.add(a5);
                    }
                }
            }
            if (jSONObject.has("template_single")) {
                JSONArray jSONArray5 = jSONObject.getJSONArray("template_single");
                for (int i9 = 0; i9 < jSONArray5.length(); i9++) {
                    JSONObject jSONObject7 = jSONArray5.getJSONObject(i9);
                    int i10 = this.x;
                    this.x = i10 + 1;
                    e a6 = e.a(jSONObject7, i10);
                    if (a6 == null) {
                        return "Template Single parse error";
                    }
                    this.B.add(a6);
                }
            }
            Log.d(w, "parseTemplate end");
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            this.y.clear();
            this.z.clear();
            this.A.clear();
            this.B.clear();
            Log.d(w, "parseTemplate failed : " + e.getMessage());
            return "parseTemplate failed : " + e.getMessage();
        }
    }

    public int b() {
        return this.n;
    }

    public int c() {
        Map<String, ArrayList<e>> map = this.C;
        int i = 0;
        if (map != null && map.size() > 0) {
            Iterator<ArrayList<e>> it = this.C.values().iterator();
            if (it.hasNext()) {
                Iterator<e> it2 = it.next().iterator();
                while (it2.hasNext()) {
                    e next = it2.next();
                    if (!next.b().equals("res_video") && !next.b().equals("res_image") && !next.b().equals("res_solid")) {
                        i += next.c();
                    }
                }
                return i;
            }
        }
        ArrayList<e> arrayList = this.A;
        if (arrayList != null && !arrayList.isEmpty()) {
            Iterator<e> it3 = this.A.iterator();
            while (it3.hasNext()) {
                e next2 = it3.next();
                if (!next2.b().equals("res_video") && !next2.b().equals("res_image") && !next2.b().equals("res_solid")) {
                    i += next2.c() + next2.h();
                }
            }
        }
        return i;
    }

    public int d() {
        ArrayList<e> arrayList = this.B;
        int i = 0;
        if (arrayList != null && !arrayList.isEmpty()) {
            Iterator<e> it = this.B.iterator();
            while (it.hasNext()) {
                e next = it.next();
                if (!next.b().equals("res_video") && !next.b().equals("res_image") && !next.b().equals("res_solid")) {
                    i += next.c() + next.h();
                }
            }
        }
        return i;
    }

    public int e() {
        Iterator<e> it = this.A.iterator();
        int i = 0;
        while (it.hasNext()) {
            e next = it.next();
            if (!next.b().equals("res_video") && !next.b().equals("res_image") && !next.b().equals("res_solid")) {
                i++;
            }
        }
        return i;
    }

    public int f() {
        Map<String, ArrayList<e>> map = this.C;
        int i = 0;
        if (map != null && map.size() > 0) {
            Iterator<e> it = this.C.values().iterator().next().iterator();
            while (it.hasNext()) {
                i += it.next().a(true);
            }
            return i;
        }
        Iterator<e> it2 = this.A.iterator();
        while (it2.hasNext()) {
            e next = it2.next();
            if (!next.b().equals("res_video") && !next.b().equals("res_image") && !next.b().equals("res_solid")) {
                i += next.a(true);
            }
        }
        return i;
    }

    public int g() {
        return this.h;
    }

    public String a(nexProject nexproject, Context context, Context context2) {
        nexproject.setBackgroundMusicPath(this.e);
        nexproject.setBGMMasterVolumeScale(this.g);
        return null;
    }

    public String b(nexProject nexproject, Context context, Context context2) {
        nexproject.setBackgroundMusicPath(this.f);
        nexproject.setBGMMasterVolumeScale(this.g);
        return null;
    }

    public String a(nexProject nexproject, Context context, Context context2, boolean z, boolean z2) {
        this.r = z;
        this.t = z2;
        Log.d(w, String.format("updateProjectWithTemplate(%b %b)", Boolean.valueOf(z), Boolean.valueOf(z2)));
        if (this.b.startsWith("2.0")) {
            nexproject.setTemplateApplyMode(2);
            nexproject.clearDrawInfo();
            return c(nexproject, context, context2);
        } else if (this.b.startsWith("3.0")) {
            nexproject.setTemplateApplyMode(3);
            String d = d(nexproject, context, context2);
            int i = this.i;
            if (i >= 0) {
                nexproject.setProjectAudioFadeInTime(i);
            }
            int i2 = this.j;
            if (i2 >= 0) {
                nexproject.setProjectAudioFadeOutTime(i2);
            }
            nexproject.updateProject();
            a(nexproject, context, context2);
            return d;
        } else {
            return "Unsupported Template version : " + this.b;
        }
    }

    public boolean a(nexProject nexproject) {
        for (int i = 0; i < nexproject.getTotalClipCount(true); i++) {
            if (nexproject.getClip(i, true).getClipType() == 1) {
                return true;
            }
        }
        return false;
    }

    public String c(nexProject nexproject, Context context, Context context2) {
        int totalTime = nexproject.getTotalTime();
        Log.d(w, String.format("applyTemplate20_Project ( ProjectTime:%d TemplateMinDur:%d)", Integer.valueOf(totalTime), Integer.valueOf(b())));
        nexProject clone = nexProject.clone(nexproject);
        nexproject.allClear(true);
        int e = e();
        int c = c();
        ArrayList arrayList = new ArrayList();
        if (!this.B.isEmpty() && ((clone.getTotalClipCount(true) == 1 && clone.getClip(0, true).getClipType() == 1) || (clone.getTotalTime() < d() && !a(clone)))) {
            Log.d(w, String.format("Template Apply Single Start(%d %d)", Integer.valueOf(nexproject.getTotalClipCount(true)), Integer.valueOf(clone.getTotalClipCount(true))));
            String a = a(clone, nexproject, context, this.B, true);
            if (a != null) {
                return a;
            }
            nexproject.updateTimeLine(false);
            int i = this.k;
            if (i >= 0) {
                nexproject.setProjectAudioFadeInTime(i);
            }
            int i2 = this.l;
            if (i2 >= 0) {
                nexproject.setProjectAudioFadeOutTime(i2);
            }
            nexproject.updateProject();
            if (this.f != null) {
                b(nexproject, context, context2);
            } else {
                a(nexproject, context, context2);
            }
            return a;
        }
        if (this.m != 1) {
            int totalClipCount = clone.getTotalClipCount(true) - 1;
            while (true) {
                if (totalClipCount < 0 || c < 800 || e <= 0) {
                    break;
                }
                nexClip clip = clone.getClip(totalClipCount, true);
                if (clip.getClipType() == 1) {
                    arrayList.add(0, clip);
                    clone.remove(clip);
                    c -= clip.getProjectDuration();
                    e--;
                } else if (clip.getProjectDuration() <= c) {
                    arrayList.add(0, clip);
                    clone.remove(clip);
                    c -= clip.getProjectDuration();
                } else if (this.y.get(0).d() > totalTime - c) {
                    arrayList.add(0, clip);
                    clone.remove(clip);
                } else {
                    int speedControl = clip.getVideoClipEdit().getSpeedControl();
                    nexClip dup = nexClip.dup(clip);
                    clone.add(totalClipCount, true, dup);
                    dup.getVideoClipEdit().setSpeedControl(speedControl);
                    dup.setRotateDegree(clip.getRotateDegree());
                    if (speedControl != 100) {
                        c = (c * speedControl) / 100;
                    }
                    int startTrimTime = clip.getVideoClipEdit().getStartTrimTime();
                    int endTrimTime = clip.getVideoClipEdit().getEndTrimTime();
                    int i3 = endTrimTime - c;
                    dup.getVideoClipEdit().setTrim(startTrimTime, i3);
                    clip.getVideoClipEdit().setTrim(i3, endTrimTime);
                    Log.d(w, String.format("Template Apply 1(%d %d %d) 2(%d %d %d)", Integer.valueOf(dup.getProjectDuration()), Integer.valueOf(dup.getVideoClipEdit().getStartTrimTime()), Integer.valueOf(dup.getVideoClipEdit().getEndTrimTime()), Integer.valueOf(clip.getProjectDuration()), Integer.valueOf(clip.getVideoClipEdit().getStartTrimTime()), Integer.valueOf(clip.getVideoClipEdit().getEndTrimTime())));
                    arrayList.add(0, clip);
                    clone.remove(clip);
                }
                totalClipCount--;
            }
        }
        Log.d(w, String.format("Template Apply Intro Start(%d %d)", Integer.valueOf(nexproject.getTotalClipCount(true)), Integer.valueOf(clone.getTotalClipCount(true))));
        String a2 = a(clone, nexproject, context, this.y, false);
        if (a2 != null) {
            return a2;
        }
        Log.d(w, String.format("Template Apply Intro End(%d %d)", Integer.valueOf(nexproject.getTotalClipCount(true)), Integer.valueOf(clone.getTotalClipCount(true))));
        while (clone.getTotalClipCount(true) > 0 && !this.v) {
            String a3 = a(clone, nexproject, context, this.z, false);
            if (a3 != null) {
                return a3;
            }
            if (this.F) {
                Log.d(w, "cancel template");
                return "cancel template";
            }
        }
        Log.d(w, String.format("Template Apply Loop End(%d %d)", Integer.valueOf(nexproject.getTotalClipCount(true)), Integer.valueOf(clone.getTotalClipCount(true))));
        while (arrayList.size() > 0) {
            clone.add((nexClip) arrayList.get(0));
            arrayList.remove(0);
        }
        Log.d(w, String.format("Template Apply Outpro Start(%d %d)", Integer.valueOf(nexproject.getTotalClipCount(true)), Integer.valueOf(clone.getTotalClipCount(true))));
        String a4 = a(clone, nexproject, context, this.A, true);
        if (a4 != null) {
            return a4;
        }
        Log.d(w, String.format("Template Apply Outro End(%d %d)", Integer.valueOf(nexproject.getTotalClipCount(true)), Integer.valueOf(clone.getTotalClipCount(true))));
        nexproject.updateTimeLine(false);
        int i4 = this.i;
        if (i4 >= 0) {
            nexproject.setProjectAudioFadeInTime(i4);
        }
        int i5 = this.j;
        if (i5 >= 0) {
            nexproject.setProjectAudioFadeOutTime(i5);
        }
        nexproject.updateProject();
        if (!this.t) {
            int totalClipCount2 = nexproject.getTotalClipCount(true);
            for (int i6 = 0; i6 < totalClipCount2; i6++) {
                if (nexproject.getClip(i6, true).getClipEffect().getId().endsWith(".force_effect")) {
                    Rect rect = new Rect();
                    nexproject.getClip(i6 - 1, true).getCrop().getEndPositionRaw(rect);
                    nexproject.getClip(i6, true).getCrop().setStartPositionRaw(rect);
                    int i7 = i6 + 1;
                    if (i7 < totalClipCount2) {
                        nexproject.getClip(i7, true).getCrop().getStartPositionRaw(rect);
                        nexproject.getClip(i6, true).getCrop().setEndPositionRaw(rect);
                    }
                }
            }
        }
        a(nexproject, context, context2);
        return a4;
    }

    public String a(nexProject nexproject, nexProject nexproject2, Context context, ArrayList<e> arrayList, boolean z) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        ArrayList<e> arrayList2 = arrayList;
        int c = c();
        int size = arrayList.size();
        int i7 = 0;
        int i8 = 0;
        while (i8 < size) {
            e eVar = arrayList2.get(i8);
            int h = eVar.h();
            int c2 = eVar.c() + h;
            int d = eVar.d() + h;
            int e = eVar.e() + h;
            if (e <= c2) {
                e = c2;
            }
            boolean f = eVar.f();
            int i9 = eVar.r;
            int totalClipCount = nexproject.getTotalClipCount(true);
            if (totalClipCount <= 0 && !z) {
                Log.d(w, String.format("setProjectWithEffects clip apply end", new Object[i7]));
                return null;
            }
            int i10 = e;
            if (eVar.a(nexproject2, context, this.q)) {
                if (!z) {
                    this.s = eVar.c;
                } else {
                    nexClip clip = nexproject2.getClip(nexproject2.getTotalClipCount(true) - 1, true);
                    if (clip != null) {
                        eVar.a(nexproject2, clip, this.s, z);
                    }
                }
            } else if (totalClipCount <= 0) {
                Log.d(w, String.format("setProjectWithEffects clip apply end", new Object[0]));
                return null;
            } else if (this.m == 1 && !z && nexproject.getClip(0, true).getProjectDuration() - c2 < c) {
                this.v = true;
                Log.d(w, String.format("setProjectWithEffects clip apply end for outro on single video", new Object[0]));
                return null;
            } else {
                if (!z) {
                    this.s = eVar.c;
                }
                int i11 = c2;
                int i12 = i11;
                int i13 = i10;
                boolean z2 = true;
                while (nexproject.getTotalClipCount(z2) > 0) {
                    i = c;
                    nexClip clip2 = nexproject.getClip(0, z2);
                    int projectDuration = clip2.getProjectDuration();
                    int i14 = i13;
                    if (clip2.getClipType() == z2) {
                        clip2.getCrop().setFaceDetectSpeed(i9);
                        if (clip2.getCrop().getEndPosionLock()) {
                            Rect rect = new Rect();
                            Rect rect2 = new Rect();
                            clip2.getCrop().getStartPosition(rect);
                            clip2.getCrop().getEndPosition(rect2);
                            clip2.getCrop().applyCropSpeed(rect, rect2, clip2.getWidth(), clip2.getHeight(), i9, projectDuration);
                            i6 = i9;
                            clip2.getCrop().shrinkToAspect(rect, nexApplicationConfig.getAspectRatio());
                            clip2.getCrop().shrinkToAspect(rect2, nexApplicationConfig.getAspectRatio());
                            clip2.getCrop().setStartPosition(rect);
                            clip2.getCrop().setEndPosition(rect2);
                        } else {
                            i6 = i9;
                        }
                        if (projectDuration > i11) {
                            i2 = size;
                            i4 = i8;
                            int i15 = i6;
                            clip2.setImageClipDuration(i11);
                            if (clip2.getCrop().getEndPosionLock()) {
                                Rect rect3 = new Rect();
                                Rect rect4 = new Rect();
                                clip2.getCrop().getStartPosition(rect3);
                                clip2.getCrop().getEndPosition(rect4);
                                clip2.getCrop().applyCropSpeed(rect3, rect4, clip2.getWidth(), clip2.getHeight(), i15, clip2.getImageClipDuration());
                                clip2.getCrop().shrinkToAspect(rect3, nexApplicationConfig.getAspectRatio());
                                clip2.getCrop().shrinkToAspect(rect4, nexApplicationConfig.getAspectRatio());
                                clip2.getCrop().setStartPosition(rect3);
                                clip2.getCrop().setEndPosition(rect4);
                            }
                            nexproject.remove(clip2);
                            nexproject2.add(clip2);
                            eVar.a("0", clip2, true, this.q, this.r);
                            eVar.a(nexproject2, clip2);
                            eVar.a(nexproject2, clip2, this.s, z);
                            b(nexproject2);
                        } else if (projectDuration >= d) {
                            nexproject.remove(clip2);
                            nexproject2.add(clip2);
                            eVar.a("0", clip2, true, this.q, this.r);
                            eVar.a(nexproject2, clip2);
                            eVar.a(nexproject2, clip2, this.s, z);
                            b(nexproject2);
                            break;
                        } else if (i11 <= 5000 || nexproject.getTotalClipCount(true) == 1 || !f) {
                            i2 = size;
                            i4 = i8;
                            int i16 = i6;
                            clip2.setImageClipDuration(i12);
                            if (clip2.getCrop().getEndPosionLock()) {
                                Rect rect5 = new Rect();
                                Rect rect6 = new Rect();
                                clip2.getCrop().getStartPosition(rect5);
                                clip2.getCrop().getEndPosition(rect6);
                                clip2.getCrop().applyCropSpeed(rect5, rect6, clip2.getWidth(), clip2.getHeight(), i16, clip2.getImageClipDuration());
                                clip2.getCrop().shrinkToAspect(rect5, nexApplicationConfig.getAspectRatio());
                                clip2.getCrop().shrinkToAspect(rect6, nexApplicationConfig.getAspectRatio());
                                clip2.getCrop().setStartPosition(rect5);
                                clip2.getCrop().setEndPosition(rect6);
                            }
                            nexproject.remove(clip2);
                            nexproject2.add(clip2);
                            eVar.a("0", clip2, true, this.q, this.r);
                            eVar.a(nexproject2, clip2);
                            eVar.a(nexproject2, clip2, this.s, z);
                            b(nexproject2);
                        } else {
                            int i17 = i12 - projectDuration;
                            nexproject.remove(clip2);
                            nexproject2.add(clip2);
                            if (i17 <= 0) {
                                eVar.a("1", clip2, true, this.q, this.r);
                                eVar.a(nexproject2, clip2);
                                eVar.a(nexproject2, clip2, this.s, z);
                                b(nexproject2);
                                break;
                            }
                            i2 = size;
                            i4 = i8;
                            i3 = i6;
                            eVar.a("1", clip2, false, this.q, this.r);
                            eVar.a(nexproject2, clip2);
                            eVar.a(nexproject2, clip2, this.s, z);
                            i13 = i14;
                            d = d;
                            z2 = true;
                            i12 = i17;
                            i9 = i3;
                            size = i2;
                            c = i;
                            i8 = i4;
                        }
                        i5 = i2;
                    } else {
                        i2 = size;
                        int i18 = d;
                        i3 = i9;
                        i4 = i8;
                        boolean z3 = z2;
                        if (projectDuration < i18) {
                            if (!eVar.g()) {
                                nexproject.remove(clip2);
                                i13 = i14;
                                d = i18;
                                z2 = z3;
                            } else {
                                nexproject.remove(clip2);
                                nexproject2.add(clip2);
                                int i19 = i12;
                                eVar.a("2", clip2, false, this.q, this.r);
                                eVar.a(nexproject2, clip2);
                                eVar.a(nexproject2, clip2, this.s, z);
                                if (!this.t) {
                                    clip2.getVideoClipEdit().setSpeedControl(100);
                                    z2 = true;
                                    if (nexproject.getTotalClipCount(true) <= 0) {
                                        clip2.setTemplateEffectID(clip2.getTemplateEffectID() & nexEngine.ExportHEVCMainTierLevel62);
                                    }
                                } else {
                                    z2 = true;
                                }
                                d = i18 - projectDuration;
                                i11 -= projectDuration;
                                i13 = i14 - projectDuration;
                                i12 = i19;
                            }
                            i9 = i3;
                            size = i2;
                            c = i;
                            i8 = i4;
                        } else if (projectDuration <= i14) {
                            nexproject.remove(clip2);
                            nexproject2.add(clip2);
                            eVar.a("3", clip2, true, this.q, this.r);
                            eVar.a(nexproject2, clip2);
                            eVar.a(nexproject2, clip2, this.s, z);
                            b(nexproject2);
                            if (!this.t) {
                                clip2.getVideoClipEdit().setSpeedControl(100);
                                c(nexproject2);
                            }
                            i5 = i2;
                        } else {
                            int i20 = i4 + 1;
                            if (i20 >= i2) {
                                i20 = 0;
                            }
                            e eVar2 = arrayList.get(i20);
                            int i21 = projectDuration - i11;
                            int i22 = projectDuration - i18;
                            int d2 = eVar2.d() + eVar2.h();
                            if (i21 <= d2 && !eVar2.a() && eVar.g()) {
                                if (i22 >= d2) {
                                    i11 = i18;
                                } else {
                                    nexproject.remove(clip2);
                                    nexproject2.add(clip2);
                                    i5 = i2;
                                    eVar.a("3", clip2, true, this.q, this.r);
                                    eVar.a(nexproject2, clip2);
                                    eVar.a(nexproject2, clip2, this.s, z);
                                    b(nexproject2);
                                    if (!this.t) {
                                        clip2.getVideoClipEdit().setSpeedControl(100);
                                        c(nexproject2);
                                    }
                                }
                            }
                            i5 = i2;
                            int speedControl = clip2.getVideoClipEdit().getSpeedControl();
                            nexClip dup = nexClip.dup(clip2);
                            nexproject2.add(dup);
                            dup.setRotateDegree(clip2.getRotateDegree());
                            dup.getVideoClipEdit().setSpeedControl(speedControl);
                            if (speedControl != 100) {
                                i11 = (i11 * speedControl) / 100;
                            }
                            dup.getVideoClipEdit().setTrim(clip2.getVideoClipEdit().getStartTrimTime(), clip2.getVideoClipEdit().getStartTrimTime() + i11);
                            clip2.getVideoClipEdit().setTrim(clip2.getVideoClipEdit().getStartTrimTime() + i11, clip2.getVideoClipEdit().getEndTrimTime());
                            eVar.a("4", dup, true, this.q, this.r);
                            eVar.a(nexproject2, dup);
                            eVar.a(nexproject2, dup, this.s, z);
                            if (!this.t) {
                                clip2.getVideoClipEdit().setSpeedControl(100);
                                dup.getVideoClipEdit().setSpeedControl(100);
                                c(nexproject2);
                            } else {
                                b(nexproject2);
                            }
                        }
                    }
                    i8 = i4 + 1;
                    size = i5;
                    c = i;
                    i7 = 0;
                    arrayList2 = arrayList;
                }
            }
            i = c;
            i5 = size;
            i4 = i8;
            i8 = i4 + 1;
            size = i5;
            c = i;
            i7 = 0;
            arrayList2 = arrayList;
        }
        return null;
    }

    public String d(nexProject nexproject, Context context, Context context2) {
        Log.d(w, String.format("applyTemplate30_Project ( ProjectTime:%d TemplateMinDur:%d)", Integer.valueOf(nexproject.getTotalTime()), Integer.valueOf(b())));
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < nexproject.getTotalClipCount(true); i4++) {
            nexClip clip = nexproject.getClip(i4, true);
            if (clip.getClipType() == 1) {
                i++;
            } else {
                if (i3 < clip.getProjectDuration()) {
                    i3 = clip.getProjectDuration();
                }
                clip.getWidth();
                clip.getHeight();
                i2++;
            }
        }
        Log.d(w, String.format("applyTemplate30_Project(I:%d V:%d)", Integer.valueOf(i), Integer.valueOf(i2)));
        if (i > 0 || i2 > 0) {
            if (i2 > 0) {
                return "Template applying failed(variable content) with preprocessing fail";
            }
            nexProject clone = nexProject.clone(nexproject);
            nexproject.allClear(true);
            nexproject.getTopDrawInfo().clear();
            String a = a(clone, nexproject, context, context2);
            nexproject.updateTimeLine(false);
            Log.d(w, "Template3.0 make Project end with only images: " + a);
            return a;
        }
        return "Not support project on 3.0 template";
    }

    public String a(nexProject nexproject, nexProject nexproject2, Context context, Context context2) {
        nexClip nexclip;
        nexClip nexclip2;
        nexClip nexclip3;
        Log.d(w, String.format("Template imageProject Apply Start(%d %d)", Integer.valueOf(nexproject2.getTotalClipCount(true)), Integer.valueOf(nexproject.getTotalClipCount(true))));
        if (!this.B.isEmpty()) {
            int totalClipCount = nexproject.getTotalClipCount(true);
            Iterator<e> it = this.B.iterator();
            while (it.hasNext()) {
                e next = it.next();
                if (next.a(true) >= totalClipCount) {
                    List<nexDrawInfo> a = next.a(0, 0, (String) null);
                    nexClip nexclip4 = null;
                    for (d dVar : next.j()) {
                        List<nexDrawInfo> list = a;
                        e eVar = next;
                        if (dVar.a(nexproject2, context, next, 0, this.q)) {
                            a = list;
                            next = eVar;
                        } else {
                            if (nexproject.getTotalClipCount(true) > 0) {
                                nexClip clip = nexproject.getClip(0, true);
                                nexproject.remove(clip);
                                nexproject2.add(clip);
                                clip.clearDrawInfos();
                                clip.mStartTime = Integer.MAX_VALUE;
                                clip.mEndTime = Integer.MIN_VALUE;
                                nexclip4 = clip;
                            }
                            dVar.a(nexclip4, eVar.a, eVar.i(), 0, this.q, null, false);
                            dVar.a(nexproject2, 0, eVar.i());
                            next = eVar;
                            a = list;
                        }
                    }
                    a(nexproject2, a);
                    nexproject2.updateProject();
                    b(nexproject2, context, context2);
                    Log.d(w, String.format("Template imageProject Apply single effect(%d %d)", Integer.valueOf(nexproject2.getTotalClipCount(true)), Integer.valueOf(nexproject.getTotalClipCount(true))));
                    return null;
                }
            }
        }
        String str = null;
        int f = f();
        this.s = "";
        nexClip nexclip5 = null;
        boolean z = true;
        boolean z2 = true;
        int i = 0;
        while (true) {
            if (nexproject.getTotalClipCount(true) <= 0 || this.F) {
                break;
            }
            if (z) {
                int i2 = i;
                int i3 = 0;
                while (i3 < this.y.size()) {
                    e eVar2 = this.y.get(i3);
                    List<nexDrawInfo> a2 = eVar2.a(i2, 0, str);
                    List<d> j = eVar2.j();
                    this.s = eVar2.c;
                    nexClip nexclip6 = nexclip5;
                    for (d dVar2 : j) {
                        e eVar3 = eVar2;
                        int i4 = i3;
                        int i5 = i2;
                        int i6 = f;
                        if (dVar2.a(nexproject2, context, eVar3, i5, this.q)) {
                            f = i6;
                            eVar2 = eVar3;
                            i3 = i4;
                            i2 = i5;
                        } else {
                            if (nexproject.getTotalClipCount(true) > i6) {
                                nexclip3 = nexproject.getClip(0, true);
                                nexproject.remove(nexclip3);
                                nexproject2.add(nexclip3);
                                nexclip3.clearDrawInfos();
                                nexclip3.mStartTime = Integer.MAX_VALUE;
                                nexclip3.mEndTime = Integer.MIN_VALUE;
                            } else {
                                nexclip3 = nexclip6;
                            }
                            eVar2 = eVar3;
                            nexclip6 = nexclip3;
                            dVar2.a(nexclip6, eVar2.a, eVar2.i(), i5, this.q, null, false);
                            i2 = i5;
                            dVar2.a(nexproject2, i2, eVar2.i());
                            f = i6;
                            i3 = i4;
                        }
                    }
                    i2 = a(nexproject2, a2);
                    i3++;
                    nexclip5 = nexclip6;
                    str = null;
                }
                int i7 = f;
                if (nexproject.getTotalClipCount(true) <= i7) {
                    z2 = false;
                }
                Log.d(w, String.format("Template imageProject Apply Intro End(%d %d) (%d)", Integer.valueOf(nexproject2.getTotalClipCount(true)), Integer.valueOf(nexproject.getTotalClipCount(true)), Integer.valueOf(i7)));
                i = i2;
                f = i7;
                z = false;
            } else {
                int i8 = f;
                if (z2) {
                    int i9 = i;
                    int i10 = 0;
                    while (true) {
                        if (i10 >= this.z.size()) {
                            i = i9;
                            break;
                        }
                        e eVar4 = this.z.get(i10);
                        List<nexDrawInfo> a3 = eVar4.a(i9, 0, (String) null);
                        List<d> j2 = eVar4.j();
                        this.s = eVar4.c;
                        nexClip nexclip7 = nexclip5;
                        for (d dVar3 : j2) {
                            List<nexDrawInfo> list2 = a3;
                            e eVar5 = eVar4;
                            int i11 = i10;
                            if (dVar3.a(nexproject2, context, eVar4, i9, this.q)) {
                                i10 = i11;
                                a3 = list2;
                                eVar4 = eVar5;
                            } else {
                                if (nexproject.getTotalClipCount(true) > i8) {
                                    nexclip2 = nexproject.getClip(0, true);
                                    nexproject.remove(nexclip2);
                                    nexproject2.add(nexclip2);
                                    nexclip2.clearDrawInfos();
                                    nexclip2.mStartTime = Integer.MAX_VALUE;
                                    nexclip2.mEndTime = Integer.MIN_VALUE;
                                } else {
                                    nexclip2 = nexclip7;
                                }
                                dVar3.a(nexclip2, eVar5.a, eVar5.i(), i9, this.q, null, false);
                                dVar3.a(nexproject2, i9, eVar5.i());
                                nexclip7 = nexclip2;
                                eVar4 = eVar5;
                                i10 = i11;
                                a3 = list2;
                            }
                        }
                        int i12 = i10;
                        i9 = a(nexproject2, a3);
                        if (nexproject.getTotalClipCount(true) <= i8) {
                            Log.d(w, String.format("Template imageProject Apply Loop End(%d %d) (%d)", Integer.valueOf(nexproject2.getTotalClipCount(true)), Integer.valueOf(nexproject.getTotalClipCount(true)), Integer.valueOf(i8)));
                            z2 = false;
                            i = i9;
                            nexclip5 = nexclip7;
                            break;
                        }
                        i10 = i12 + 1;
                        nexclip5 = nexclip7;
                    }
                    f = i8;
                } else {
                    ArrayList<e> arrayList = this.A;
                    Map<String, ArrayList<e>> map = this.C;
                    if (map != null && map.size() > 0) {
                        if (this.C.containsKey(this.s)) {
                            arrayList = this.C.get(this.s);
                            Log.d(w, String.format("Template imageProject select Outro(%s)", this.s));
                        } else {
                            arrayList = this.C.values().iterator().next();
                        }
                    }
                    ArrayList<e> arrayList2 = arrayList;
                    int i13 = i;
                    int i14 = 0;
                    while (i14 < arrayList2.size()) {
                        e eVar6 = arrayList2.get(i14);
                        List<nexDrawInfo> a4 = eVar6.a(i13, 0, this.s);
                        nexClip nexclip8 = null;
                        for (d dVar4 : eVar6.j()) {
                            List<nexDrawInfo> list3 = a4;
                            e eVar7 = eVar6;
                            int i15 = i14;
                            int i16 = i13;
                            if (dVar4.a(nexproject2, context, eVar6, i13, this.q)) {
                                eVar6 = eVar7;
                                i14 = i15;
                                i13 = i16;
                            } else {
                                if (nexproject.getTotalClipCount(true) > 0) {
                                    nexclip = nexproject.getClip(0, true);
                                    nexproject.remove(nexclip);
                                    nexproject2.add(nexclip);
                                    nexclip.clearDrawInfos();
                                    nexclip.mStartTime = Integer.MAX_VALUE;
                                    nexclip.mEndTime = Integer.MIN_VALUE;
                                } else {
                                    nexclip = nexclip8;
                                }
                                dVar4.a(nexclip, eVar7.a, eVar7.i(), i16, this.q, this.s, false);
                                dVar4.a(nexproject2, i16, eVar7.i());
                                nexclip8 = nexclip;
                                eVar6 = eVar7;
                                i13 = i16;
                                i14 = i15;
                            }
                            a4 = list3;
                        }
                        i13 = a(nexproject2, a4);
                        i14++;
                    }
                    Log.d(w, String.format("Template imageProject Apply Outro End(%d %d) (%d)", Integer.valueOf(nexproject2.getTotalClipCount(true)), Integer.valueOf(nexproject.getTotalClipCount(true)), Integer.valueOf(i8)));
                }
            }
            str = null;
        }
        if (this.F) {
            return "apply Template user canceled";
        }
        nexproject2.updateProject();
        a(nexproject2, context, context2);
        return null;
    }

    public int a(nexProject nexproject, List<nexDrawInfo> list) {
        int i = 0;
        for (nexDrawInfo nexdrawinfo : list) {
            nexproject.getTopDrawInfo().add(nexdrawinfo);
            if (nexdrawinfo.getIsTransition() == 1) {
                i = nexdrawinfo.getStartTime();
            } else {
                i = nexdrawinfo.getEndTime();
            }
        }
        return i;
    }

    public void b(nexProject nexproject) {
        int totalClipCount = nexproject.getTotalClipCount(true);
        if (totalClipCount <= 1) {
            return;
        }
        nexClip clip = nexproject.getClip(totalClipCount - 2, true);
        int templateEffectID = clip.getTemplateEffectID() & (-251658241);
        nexClip clip2 = nexproject.getClip(totalClipCount - 1, true);
        int templateEffectID2 = clip2.getTemplateEffectID() & (-251658241);
        if (templateEffectID != templateEffectID2 && clip.getTransitionEffect().getDuration() >= clip2.getProjectDuration() - clip2.getTransitionEffect().getDuration()) {
            clip.getTransitionEffect(true).setEffectNone();
            clip.getTransitionEffect(true).setDuration(0);
        }
        if (templateEffectID == templateEffectID2 || clip2.getProjectDuration() > clip2.getTransitionEffect().getDuration()) {
            return;
        }
        clip2.getTransitionEffect(true).setEffectNone();
        clip2.getTransitionEffect(true).setDuration(0);
    }

    public void c(nexProject nexproject) {
        nexClip clip;
        int totalClipCount = nexproject.getTotalClipCount(true);
        if (totalClipCount > 0 && (clip = nexproject.getClip(totalClipCount - 1, true)) != null && !clip.getTransitionEffect().getId().equals("none") && clip.getTransitionEffect().getDuration() > 0) {
            int duration = clip.getTransitionEffect().getDuration();
            if (clip.getVideoClipEdit().getSpeedControl() != 100) {
                duration = (duration * clip.getVideoClipEdit().getSpeedControl()) / 100;
            }
            nexClip dup = nexClip.dup(clip);
            nexproject.add(dup);
            dup.setRotateDegree(clip.getRotateDegree());
            dup.getVideoClipEdit().setSpeedControl(clip.getVideoClipEdit().getSpeedControl());
            dup.setColorEffect(clip.getColorEffect());
            dup.setBrightness(clip.getBrightness());
            dup.setContrast(clip.getContrast());
            dup.setSaturation(clip.getSaturation());
            dup.setTemplateEffectID(clip.getTemplateEffectID());
            dup.setClipVolume(clip.getClipVolume());
            dup.setAudioOnOff(clip.getAudioOnOff());
            nexClipEffect clipEffect = dup.getClipEffect();
            clipEffect.setEffect(clip.getTransitionEffect().getId() + ".force_effect");
            dup.getTransitionEffect().setTransitionEffect("none");
            dup.getTransitionEffect().setDuration(0);
            clip.getTransitionEffect().setTransitionEffect("none");
            clip.getTransitionEffect().setDuration(0);
            dup.getVideoClipEdit().setTrim(clip.getVideoClipEdit().getEndTrimTime() - duration, clip.getVideoClipEdit().getEndTrimTime());
            clip.getVideoClipEdit().setTrim(clip.getVideoClipEdit().getStartTrimTime(), clip.getVideoClipEdit().getEndTrimTime() - duration);
            Log.d(w, String.format("Template split clip(%d %d) next(%d %d %s)", Integer.valueOf(clip.getProjectStartTime()), Integer.valueOf(clip.getVideoClipEdit().getStartTrimTime() + duration), Integer.valueOf(clip.getVideoClipEdit().getStartTrimTime() + duration), Integer.valueOf(dup.getProjectEndTime()), dup.getTransitionEffect().getId()));
        }
    }

    public void h() {
        Log.d(w, "setCancel");
        this.F = true;
    }
}
