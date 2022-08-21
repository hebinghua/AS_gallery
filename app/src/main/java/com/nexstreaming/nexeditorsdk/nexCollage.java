package com.nexstreaming.nexeditorsdk;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;
import ch.qos.logback.core.CoreConstants;
import com.nexstreaming.nexeditorsdk.nexCollageManager;
import com.nexstreaming.nexeditorsdk.nexSaveDataFormat;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public final class nexCollage {
    private static String a = "nexCollage";
    private String b;
    private String c;
    private String d;
    private String e;
    private nexCollageManager.CollageType g;
    private int h;
    private float i;
    private String j;
    private int k;
    private String l;
    private float m;
    private String n;
    private boolean o;
    private float r;
    private float s;
    private nexProject v;
    private nexEngine w;
    private Context x;
    private Date y;
    private float f = 1.0f;
    private List<a> p = new ArrayList();
    private List<nexCollageTitleInfo> q = new ArrayList();
    private int t = 200;
    private int u = 1000;
    private boolean z = false;

    /* loaded from: classes3.dex */
    public interface CollageInfoChangedListener {
        String CollageTime(String str);

        void DrawInfoChanged(nexDrawInfo nexdrawinfo);

        RectF FaceRect(String str);

        void SourceChanged(nexClip nexclip, nexClip nexclip2);

        void TitleInfoChanged();

        String TitleInfoContentTime(String str, String str2);
    }

    public float a() {
        return this.i;
    }

    public int b() {
        return this.k;
    }

    public int c() {
        return this.h;
    }

    public nexCollageManager.CollageType d() {
        return this.g;
    }

    public float e() {
        return this.r / this.s;
    }

    public List<a> f() {
        return this.p;
    }

    public List<nexCollageTitleInfo> g() {
        return this.q;
    }

    public static void a(Rect rect, int i, int i2) {
        rect.left = (rect.left * i) / nexCrop.ABSTRACT_DIMENSION;
        rect.top = (rect.top * i2) / nexCrop.ABSTRACT_DIMENSION;
        rect.right = (rect.right * i) / nexCrop.ABSTRACT_DIMENSION;
        rect.bottom = (rect.bottom * i2) / nexCrop.ABSTRACT_DIMENSION;
    }

    public static void b(Rect rect, int i, int i2) {
        rect.left = (rect.left * nexCrop.ABSTRACT_DIMENSION) / i;
        rect.top = (rect.top * nexCrop.ABSTRACT_DIMENSION) / i2;
        rect.right = (rect.right * nexCrop.ABSTRACT_DIMENSION) / i;
        rect.bottom = (rect.bottom * nexCrop.ABSTRACT_DIMENSION) / i2;
    }

    public static void a(Rect rect, float f) {
        float width = rect.width() / rect.height();
        Log.d(a, String.format("shrinkToAspect(%f %f)", Float.valueOf(width), Float.valueOf(f)));
        if (width < f) {
            int width2 = (int) (rect.width() / f);
            int centerY = rect.centerY() - (width2 / 2);
            rect.top = centerY;
            rect.bottom = centerY + width2;
        } else {
            int height = (int) (rect.height() * f);
            int centerX = rect.centerX() - (height / 2);
            rect.left = centerX;
            rect.right = centerX + height;
        }
        String str = a;
        Log.d(str, String.format("shrinkToAspect: ", new Object[0]) + rect.toString());
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String a(int i) {
        StringBuilder sb = new StringBuilder();
        sb.append(0);
        sb.append(CoreConstants.COMMA_CHAR);
        sb.append(i);
        sb.append('?');
        sb.append('?');
        Iterator<nexCollageTitleInfo> it = this.q.iterator();
        while (it.hasNext()) {
            sb.append(it.next().b());
            if (it.hasNext()) {
                sb.append('&');
            }
        }
        return sb.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public RectF b(String str) {
        RectF rectF = new RectF();
        com.nexstreaming.kminternal.kinemaster.utils.facedetect.a a2 = com.nexstreaming.kminternal.kinemaster.utils.facedetect.a.a(str);
        if (a2 == null) {
            a2 = new com.nexstreaming.kminternal.kinemaster.utils.facedetect.a(new File(str), true, this.x);
            com.nexstreaming.kminternal.kinemaster.utils.facedetect.a.a(str, a2);
        }
        a2.a(rectF);
        return rectF;
    }

    public String a(nexProject nexproject, nexEngine nexengine, int i, Context context, boolean z) {
        nexDrawInfo nexdrawinfo;
        nexDrawInfo nexdrawinfo2;
        String str;
        boolean z2;
        nexColorEffect lutColorEffect;
        nexColorEffect lutColorEffect2;
        nexProject nexproject2 = nexproject;
        Context context2 = context;
        int i2 = 0;
        boolean z3 = true;
        Log.d(a, String.format("apply2Project sources(%d %d %d)", Integer.valueOf(this.k), Integer.valueOf(nexproject2.getTotalClipCount(true)), Integer.valueOf(i)));
        this.x = context2;
        int i3 = i == 0 ? this.h : i;
        for (int i4 = 0; i4 < nexproject2.getTotalClipCount(true); i4++) {
            nexClip clip = nexproject2.getClip(i4, true);
            if (clip.isMotionTrackedVideo() || clip.isAssetResource()) {
                nexproject2.remove(clip);
            }
        }
        for (int i5 = 0; i5 < nexproject2.getTotalClipCount(false); i5++) {
            nexClip clip2 = nexproject2.getClip(i5, false);
            if (clip2.isAssetResource()) {
                nexproject2.remove(clip2);
            }
        }
        ArrayList arrayList = new ArrayList();
        int i6 = 0;
        while (nexproject2.getTotalClipCount(true) > 0) {
            nexClip clip3 = nexproject2.getClip(0, true);
            if (clip3.getClipType() == 4) {
                clip3.setAudioOnOff(false);
            }
            arrayList.add(clip3);
            if (i6 <= clip3.getProjectDuration()) {
                i6 = clip3.getProjectDuration();
            }
            nexproject2.remove(clip3);
        }
        if (i3 == 0) {
            i3 = i6;
        }
        nexproject.getTopDrawInfo().clear();
        nexproject2.setTemplateApplyMode(3);
        nexDrawInfo nexdrawinfo3 = new nexDrawInfo();
        nexdrawinfo3.setID(1);
        nexdrawinfo3.setSubEffectID(65537);
        nexdrawinfo3.setEffectID(this.n);
        nexdrawinfo3.setStartTime(0);
        nexdrawinfo3.setEndTime(i3);
        nexproject.getTopDrawInfo().add(nexdrawinfo3);
        this.y = new Date();
        Iterator<a> it = this.p.iterator();
        int i7 = 1;
        loop3: while (true) {
            nexClip nexclip = null;
            while (it.hasNext()) {
                a next = it.next();
                if (next.a()) {
                    nexclip = nexClip.getSupportedClip(next.a(context2));
                    nexclip.setAssetResource(z3);
                    if (next.b()) {
                        nexclip.setMotionTrackedVideo(z3);
                    }
                } else if (nexclip == null && arrayList.size() > 0) {
                    nexclip = (nexClip) arrayList.get(i2);
                    arrayList.remove(nexclip);
                }
                if (nexclip != null) {
                    nexproject2.add(nexclip);
                    nexDrawInfo nexdrawinfo4 = nexclip.getDrawInfos().size() > 0 ? nexclip.getDrawInfos().get(i2) : null;
                    nexclip.clearDrawInfos();
                    if (nexclip.getClipType() == z3) {
                        nexclip.setImageClipDuration(i3);
                    } else {
                        nexclip.getVideoClipEdit().clearTrim();
                        if (nexclip.getTotalTime() > i3) {
                            nexclip.getVideoClipEdit().setTrim(i2, i3);
                        }
                    }
                    nexclip.mStartTime = i2;
                    nexclip.mEndTime = i3;
                    nexDrawInfo nexdrawinfo5 = new nexDrawInfo();
                    nexdrawinfo5.setTopEffectID(nexdrawinfo3.getID());
                    int i8 = 65536 | i7;
                    nexdrawinfo5.setID(i8);
                    nexdrawinfo5.setSubEffectID(i8);
                    int i9 = i7 + 1;
                    float f = i3;
                    int c = (int) (f * next.c());
                    int d = (int) (f * next.d());
                    ArrayList arrayList2 = arrayList;
                    Iterator<a> it2 = it;
                    Log.d(a, String.format("Collage setDrawInfo2Clip(dur:%d %d %d)", Integer.valueOf(i3), Integer.valueOf(c), Integer.valueOf(d)));
                    if (nexclip.getClipType() == 1) {
                        int i10 = nexclip.mStartTime;
                        if (c < i10) {
                            i10 = c;
                        }
                        nexclip.mStartTime = i10;
                        int i11 = nexclip.mEndTime;
                        if (d > i11) {
                            i11 = d;
                        }
                        nexclip.mEndTime = i11;
                        nexclip.setImageClipDuration(i11 - i10);
                    } else {
                        if (c < nexclip.getProjectStartTime()) {
                            c = nexclip.getProjectStartTime();
                        }
                        if (d > nexclip.getProjectEndTime()) {
                            d = nexclip.getProjectEndTime();
                        }
                    }
                    nexdrawinfo5.setStartTime(c);
                    nexdrawinfo5.setEndTime(d);
                    if (next.e() != null && next.e().compareTo("none") != 0 && (lutColorEffect2 = nexColorEffect.getLutColorEffect(next.e())) != null) {
                        nexdrawinfo5.setLUT(lutColorEffect2.getLUTId());
                    }
                    int width = nexclip.getWidth();
                    int height = nexclip.getHeight();
                    if (nexclip.getRotateInMeta() == 90 || nexclip.getRotateInMeta() == 270) {
                        width = nexclip.getHeight();
                        height = nexclip.getWidth();
                        if (nexclip.getClipType() == 4) {
                            nexdrawinfo5.setRotateState(nexclip.getRotateInMeta());
                        }
                    }
                    Rect rect = new Rect(0, 0, width, height);
                    Rect rect2 = new Rect(0, 0, 1, 1);
                    if (nexclip.getClipType() == 1) {
                        RectF b = b(nexclip.getPath());
                        float f2 = width;
                        nexdrawinfo = nexdrawinfo3;
                        str = "none";
                        float f3 = height;
                        nexdrawinfo2 = nexdrawinfo4;
                        rect.set((int) (b.left * f2), (int) (b.top * f3), (int) (b.right * f2), (int) (b.bottom * f3));
                        if (rect.isEmpty()) {
                            rect = new Rect(0, 0, width, height);
                        } else {
                            rect2.set((int) (b.left * f2), (int) (b.top * f3), (int) (b.right * f2), (int) (b.bottom * f3));
                            int i12 = rect.right;
                            int i13 = rect.left;
                            int i14 = (i12 - i13) / 2;
                            int i15 = i13 - i14;
                            rect.left = i15;
                            int i16 = i12 + i14;
                            rect.right = i16;
                            if (i15 < 0) {
                                rect.left = 0;
                            }
                            if (i16 > width) {
                                rect.right = width;
                            }
                            int i17 = rect.bottom;
                            int i18 = rect.top;
                            int i19 = (i17 - i18) / 2;
                            int i20 = i18 - i19;
                            rect.top = i20;
                            int i21 = i17 + i19;
                            rect.bottom = i21;
                            if (i20 < 0) {
                                rect.top = 0;
                            }
                            if (i21 > height) {
                                rect.bottom = height;
                            }
                        }
                    } else {
                        nexdrawinfo = nexdrawinfo3;
                        nexdrawinfo2 = nexdrawinfo4;
                        str = "none";
                    }
                    Log.d(a, String.format("Collage setDrawInfo2Clip", new Object[0]) + next.getRectangle().toString());
                    float width2 = (next.getRectangle().width() * this.r) / (next.getRectangle().height() * this.s);
                    float f4 = (float) next.f();
                    float g = (float) next.g();
                    if (f4 != 0.0f && g != 0.0f) {
                        width2 = f4 / g;
                    }
                    a(rect, width2);
                    Log.d(a, String.format("Collage setDrawInfo2Clip sr : ", new Object[0]) + rect.toString());
                    b(rect, width, height);
                    b(rect2, width, height);
                    Log.d(a, String.format("Collage setDrawInfo2Clip1 sr : ", new Object[0]) + rect.toString());
                    Log.d(a, String.format("Collage setDrawInfo2Clip clip size(%d %d)", Integer.valueOf(width), Integer.valueOf(height)));
                    nexdrawinfo5.setStartRect(rect);
                    nexdrawinfo5.setEndRect(rect);
                    nexdrawinfo5.setFaceRect(rect2);
                    nexclip.addDrawInfo(nexdrawinfo5);
                    next.a(nexclip);
                    next.b(width2);
                    nexproject2 = nexproject;
                    next.a(nexproject2, i3);
                    if (nexdrawinfo2 != null) {
                        nexdrawinfo5.setRotateState(nexdrawinfo2.getRotateState());
                        nexdrawinfo5.setUserTranslate(nexdrawinfo2.getUserTranslateX(), nexdrawinfo2.getUserTranslateY());
                        nexdrawinfo5.setUserRotateState(nexdrawinfo2.getUserRotateState());
                        nexdrawinfo5.setRealScale(nexdrawinfo2.getRealScale());
                        nexdrawinfo5.setBrightness(nexdrawinfo2.getBrightness());
                        nexdrawinfo5.setUserLUT(nexdrawinfo2.getUserLUT());
                        if (z) {
                            String userLUT = nexdrawinfo2.getUserLUT();
                            if (userLUT != null && userLUT.compareTo(str) != 0 && userLUT.compareTo("null") != 0 && (lutColorEffect = nexColorEffect.getLutColorEffect(userLUT)) != null && lutColorEffect.getLUTId() == nexdrawinfo2.getLUT()) {
                                nexdrawinfo5.setLUT(nexdrawinfo2.getLUT());
                            }
                            nexdrawinfo5.setStartRect(nexdrawinfo2.getStartRect());
                            nexdrawinfo5.setEndRect(nexdrawinfo2.getEndRect());
                            nexdrawinfo5.setFaceRect(nexdrawinfo2.getFaceRect());
                        }
                        z2 = false;
                    } else {
                        z2 = true;
                    }
                    next.a((RectF) null);
                    next.a(z2);
                    i2 = 0;
                    z3 = true;
                    Log.d(a, String.format("Collage apply rect(%s) %f", nexdrawinfo5.getStartRect().toString(), Float.valueOf(nexdrawinfo5.getRealScale())));
                    context2 = context;
                    it = it2;
                    arrayList = arrayList2;
                    i7 = i9;
                    nexdrawinfo3 = nexdrawinfo;
                }
            }
            break loop3;
        }
        nexdrawinfo3.setTitle(a(i3));
        String str2 = this.e;
        if (str2 != null && str2.length() > 0 && this.e.compareTo("none") != 0) {
            nexproject2.setBackgroundMusicPath(this.e);
            nexproject2.setBGMMasterVolumeScale(this.f);
        } else {
            nexproject2.setBackgroundMusicPath(null);
            nexproject2.setBGMMasterVolumeScale(0.0f);
        }
        nexproject2.setProjectAudioFadeInTime(this.t);
        nexproject2.setProjectAudioFadeOutTime(this.u);
        nexengine.setProperty("PreviewFPS", "30");
        nexengine.setProject(nexproject2);
        nexengine.updateProject();
        this.v = nexproject2;
        this.w = nexengine;
        return null;
    }

    public boolean a(String str) {
        nexProject nexproject = this.v;
        if (nexproject == null || this.w == null) {
            return false;
        }
        if (str == null) {
            String str2 = this.e;
            if (str2 != null) {
                nexproject.setBackgroundMusicPath(str2);
                this.v.setBGMMasterVolumeScale(this.f);
                return true;
            }
            nexproject.setBackgroundMusicPath(null);
        } else {
            nexproject.setBackgroundMusicPath(str);
        }
        this.w.updateProject();
        return true;
    }

    public boolean h() {
        return this.o;
    }

    public boolean a(a aVar, a aVar2) {
        nexColorEffect lutColorEffect;
        nexColorEffect lutColorEffect2;
        if (aVar == null || aVar2 == null) {
            return false;
        }
        nexClip bindSource = aVar.getBindSource();
        nexClip bindSource2 = aVar2.getBindSource();
        if (bindSource == null || bindSource2 == null) {
            return false;
        }
        nexClip dup = nexClip.dup(bindSource);
        nexClip dup2 = nexClip.dup(bindSource2);
        if (dup.getClipType() == 4) {
            dup.setAudioOnOff(false);
        }
        if (dup2.getClipType() == 4) {
            dup2.setAudioOnOff(false);
        }
        nexDrawInfo nexdrawinfo = bindSource.getDrawInfos().get(0);
        nexDrawInfo nexdrawinfo2 = bindSource2.getDrawInfos().get(0);
        int rotateState = nexdrawinfo.getRotateState();
        int userTranslateX = nexdrawinfo.getUserTranslateX();
        int userTranslateY = nexdrawinfo.getUserTranslateY();
        int userRotateState = nexdrawinfo.getUserRotateState();
        float realScale = nexdrawinfo.getRealScale();
        int lut = nexdrawinfo.getLUT();
        int customLUTA = nexdrawinfo.getCustomLUTA();
        nexClip nexclip = bindSource2;
        int customLUTB = nexdrawinfo.getCustomLUTB();
        nexClip nexclip2 = bindSource;
        int customLUTPower = nexdrawinfo.getCustomLUTPower();
        String userLUT = nexdrawinfo.getUserLUT();
        int brightness = nexdrawinfo.getBrightness();
        nexdrawinfo.setRotateState(nexdrawinfo2.getRotateState());
        nexdrawinfo.setUserTranslate(nexdrawinfo2.getUserTranslateX(), nexdrawinfo2.getUserTranslateY());
        nexdrawinfo.setUserRotateState(nexdrawinfo2.getUserRotateState());
        nexdrawinfo.setRealScale(nexdrawinfo2.getRealScale());
        nexdrawinfo.setLUT(0);
        String userLUT2 = nexdrawinfo2.getUserLUT();
        if (userLUT2 != null && userLUT2.compareTo("none") != 0 && userLUT2.compareTo("null") != 0) {
            nexdrawinfo.setLUT(nexdrawinfo2.getLUT());
        } else if (aVar.e() != null && aVar.e().compareTo("none") != 0 && aVar.e().compareTo("null") != 0 && (lutColorEffect = nexColorEffect.getLutColorEffect(aVar.e())) != null) {
            nexdrawinfo.setLUT(lutColorEffect.getLUTId());
        }
        nexdrawinfo.setCustomLUTA(nexdrawinfo2.getCustomLUTA());
        nexdrawinfo.setCustomLUTB(nexdrawinfo2.getCustomLUTB());
        nexdrawinfo.setCustomLUTPower(nexdrawinfo2.getCustomLUTPower());
        nexdrawinfo.setUserLUT(nexdrawinfo2.getUserLUT());
        nexdrawinfo.setBrightness(nexdrawinfo2.getBrightness());
        aVar.a(dup2, nexdrawinfo);
        nexdrawinfo2.setRotateState(rotateState);
        nexdrawinfo2.setUserTranslate(userTranslateX, userTranslateY);
        nexdrawinfo2.setUserRotateState(userRotateState);
        nexdrawinfo2.setRealScale(realScale);
        nexdrawinfo2.setLUT(0);
        if (userLUT != null && userLUT.compareTo("none") != 0 && userLUT.compareTo("null") != 0) {
            nexdrawinfo2.setLUT(lut);
        } else if (aVar2.e() != null && aVar2.e().compareTo("none") != 0 && aVar2.e().compareTo("null") != 0 && (lutColorEffect2 = nexColorEffect.getLutColorEffect(aVar2.e())) != null) {
            nexdrawinfo2.setLUT(lutColorEffect2.getLUTId());
        }
        nexdrawinfo2.setCustomLUTA(customLUTA);
        nexdrawinfo2.setCustomLUTB(customLUTB);
        nexdrawinfo2.setCustomLUTPower(customLUTPower);
        nexdrawinfo2.setUserLUT(userLUT);
        nexdrawinfo2.setBrightness(brightness);
        aVar2.a(dup, nexdrawinfo2);
        int i = 0;
        int i2 = 1;
        while (i < this.v.getTotalClipCount(true)) {
            nexClip nexclip3 = nexclip2;
            if (this.v.getClip(i, true).equals(nexclip3)) {
                this.v.add(i2 - 1, true, dup2);
                this.v.remove(nexclip3);
                this.w.removeClip(i2);
            }
            nexClip nexclip4 = nexclip;
            if (this.v.getClip(i, true).equals(nexclip4)) {
                this.v.add(i2 - 1, true, dup);
                this.v.remove(nexclip4);
                this.w.removeClip(i2);
            }
            i2++;
            i++;
            nexclip2 = nexclip3;
            nexclip = nexclip4;
        }
        this.w.updateProject();
        this.w.setThumbnailRoutine(2);
        this.w.seek((int) (this.h * this.i));
        return true;
    }

    public boolean i() {
        return this.z;
    }

    public String a(JSONObject jSONObject) {
        if (jSONObject == null) {
            return "Collage parsing error with invalid handle";
        }
        try {
            this.b = jSONObject.getString("collage_name");
            this.c = jSONObject.getString("collage_version");
            this.d = jSONObject.getString("collage_desc");
            this.e = jSONObject.getString("collage_bgm");
            this.f = Float.parseFloat(jSONObject.getString("collage_bgm_volume"));
            if (jSONObject.getString("collage_type").compareTo("static") == 0) {
                this.g = nexCollageManager.CollageType.StaticCollage;
            } else if (jSONObject.getString("collage_type").compareTo("dynamic") == 0) {
                this.g = nexCollageManager.CollageType.DynamicCollage;
            }
            this.h = Integer.parseInt(jSONObject.getString("collage_duration"));
            this.i = Float.parseFloat(jSONObject.getString("collage_edit_time"));
            String string = jSONObject.getString("collage_ratio");
            this.j = string;
            String[] split = string.toLowerCase().split("v");
            if (split == null) {
                return "Wrong ratio info was included";
            }
            this.r = Float.parseFloat(split[0]);
            this.s = Float.parseFloat(split[1]);
            this.k = Integer.parseInt(jSONObject.getString("collage_source_count"));
            this.l = jSONObject.getString("audio_res");
            this.m = Float.parseFloat(jSONObject.getString("audio_res_pos"));
            if (jSONObject.has("collage_project_vol_fade_in_time")) {
                this.t = Integer.parseInt(jSONObject.getString("collage_project_vol_fade_in_time"));
            }
            if (jSONObject.has("collage_project_vol_fade_out_time")) {
                this.u = Integer.parseInt(jSONObject.getString("collage_project_vol_fade_out_time"));
            }
            this.o = false;
            if (jSONObject.has("frame_collage")) {
                this.o = jSONObject.getString("frame_collage").compareTo("1") == 0;
            }
            this.n = jSONObject.getString("effect");
            if (jSONObject.has("draw_infos")) {
                JSONArray jSONArray = jSONObject.getJSONArray("draw_infos");
                for (int i = 0; i < jSONArray.length(); i++) {
                    a aVar = new a();
                    if (aVar.a(jSONArray.getJSONObject(i)) != null) {
                        this.p.clear();
                        this.q.clear();
                        return "Collage drawinfo parse error";
                    }
                    aVar.a(new CollageInfoChangedListener() { // from class: com.nexstreaming.nexeditorsdk.nexCollage.1
                        @Override // com.nexstreaming.nexeditorsdk.nexCollage.CollageInfoChangedListener
                        public String CollageTime(String str) {
                            return "";
                        }

                        @Override // com.nexstreaming.nexeditorsdk.nexCollage.CollageInfoChangedListener
                        public void TitleInfoChanged() {
                        }

                        @Override // com.nexstreaming.nexeditorsdk.nexCollage.CollageInfoChangedListener
                        public String TitleInfoContentTime(String str, String str2) {
                            return "";
                        }

                        @Override // com.nexstreaming.nexeditorsdk.nexCollage.CollageInfoChangedListener
                        public void SourceChanged(nexClip nexclip, nexClip nexclip2) {
                            if (nexCollage.this.w != null) {
                                int i2 = 1;
                                for (int i3 = 0; i3 < nexCollage.this.v.getTotalClipCount(true) && !nexCollage.this.v.getClip(i3, true).equals(nexclip); i3++) {
                                    i2++;
                                }
                                nexCollage.this.v.add(i2 - 1, true, nexclip2);
                                nexCollage.this.v.remove(nexclip);
                                nexCollage.this.w.removeClip(i2);
                                nexCollage.this.w.updateProject();
                            }
                        }

                        @Override // com.nexstreaming.nexeditorsdk.nexCollage.CollageInfoChangedListener
                        public void DrawInfoChanged(nexDrawInfo nexdrawinfo) {
                            if (nexCollage.this.w != null) {
                                nexCollage.this.w.updateDrawInfo(nexdrawinfo);
                            }
                        }

                        @Override // com.nexstreaming.nexeditorsdk.nexCollage.CollageInfoChangedListener
                        public RectF FaceRect(String str) {
                            return nexCollage.this.b(str);
                        }
                    });
                    this.p.add(aVar);
                }
            }
            if (jSONObject.has("title_infos")) {
                JSONArray jSONArray2 = jSONObject.getJSONArray("title_infos");
                for (int i2 = 0; i2 < jSONArray2.length(); i2++) {
                    nexCollageTitleInfo nexcollagetitleinfo = new nexCollageTitleInfo();
                    if (nexcollagetitleinfo.a(jSONArray2.getJSONObject(i2)) != null) {
                        this.p.clear();
                        this.q.clear();
                        return "Collage title info parse error";
                    }
                    nexcollagetitleinfo.a(new CollageInfoChangedListener() { // from class: com.nexstreaming.nexeditorsdk.nexCollage.2
                        @Override // com.nexstreaming.nexeditorsdk.nexCollage.CollageInfoChangedListener
                        public void DrawInfoChanged(nexDrawInfo nexdrawinfo) {
                        }

                        @Override // com.nexstreaming.nexeditorsdk.nexCollage.CollageInfoChangedListener
                        public void SourceChanged(nexClip nexclip, nexClip nexclip2) {
                        }

                        @Override // com.nexstreaming.nexeditorsdk.nexCollage.CollageInfoChangedListener
                        public String CollageTime(String str) {
                            if (nexCollage.this.y == null) {
                                nexCollage.this.y = new Date();
                            }
                            return new SimpleDateFormat(str).format(nexCollage.this.y);
                        }

                        @Override // com.nexstreaming.nexeditorsdk.nexCollage.CollageInfoChangedListener
                        public String TitleInfoContentTime(String str, String str2) {
                            for (a aVar2 : nexCollage.this.p) {
                                if (aVar2.getId().compareTo(str) == 0) {
                                    return aVar2.a(str2);
                                }
                            }
                            return "";
                        }

                        @Override // com.nexstreaming.nexeditorsdk.nexCollage.CollageInfoChangedListener
                        public void TitleInfoChanged() {
                            if (nexCollage.this.v != null) {
                                nexCollage nexcollage = nexCollage.this;
                                nexCollage.this.v.getTopDrawInfo().get(0).setTitle(nexcollage.a(nexcollage.v.getTotalTime()));
                                if (nexCollage.this.w == null) {
                                    return;
                                }
                                nexCollage.this.w.updateDrawInfo(nexCollage.this.v.getTopDrawInfo().get(0));
                            }
                        }

                        @Override // com.nexstreaming.nexeditorsdk.nexCollage.CollageInfoChangedListener
                        public RectF FaceRect(String str) {
                            return nexCollage.this.b(str);
                        }
                    });
                    nexcollagetitleinfo.c();
                    this.q.add(nexcollagetitleinfo);
                }
            }
            this.z = true;
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(a, "parse Collage header failed : " + e.getMessage());
            return e.getMessage();
        }
    }

    public nexSaveDataFormat j() {
        if (this.v == null) {
            return null;
        }
        nexSaveDataFormat nexsavedataformat = new nexSaveDataFormat();
        nexsavedataformat.project = this.v.getSaveData();
        nexsavedataformat.collage = new nexSaveDataFormat.nexCollageOf();
        if (this.q.size() == 0) {
            nexsavedataformat.collage.titleInfos = null;
        } else {
            nexsavedataformat.collage.titleInfos = new ArrayList();
            for (nexCollageTitleInfo nexcollagetitleinfo : this.q) {
                nexsavedataformat.collage.titleInfos.add(nexcollagetitleinfo.d());
            }
        }
        return nexsavedataformat;
    }

    public void a(nexSaveDataFormat.nexCollageOf nexcollageof) {
        if (nexcollageof.titleInfos != null) {
            int i = 0;
            for (nexCollageTitleInfo nexcollagetitleinfo : this.q) {
                if (i >= nexcollageof.titleInfos.size()) {
                    return;
                }
                nexcollagetitleinfo.a(nexcollageof.titleInfos.get(i));
                i++;
            }
        }
    }
}
