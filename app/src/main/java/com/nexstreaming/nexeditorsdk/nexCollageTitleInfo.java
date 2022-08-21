package com.nexstreaming.nexeditorsdk;

import android.graphics.Color;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.Log;
import com.nexstreaming.nexeditorsdk.nexCollage;
import com.nexstreaming.nexeditorsdk.nexSaveDataFormat;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;
import org.keyczar.Keyczar;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public class nexCollageTitleInfo implements nexCollageInfo, nexCollageInfoTitle {
    private static String b = "nexCollageTitleInfo";
    public nexCollage.CollageInfoChangedListener a;
    private float c;
    private float d;
    private RectF e;
    private String g;
    private Type h;
    private String i;
    private List<PointF> f = new ArrayList();
    private int j = 20;
    private int k = 20;
    private int l = 1;
    private Map<String, String> m = new HashMap();
    private String n = null;
    private String o = "";
    private String p = "";
    private String q = "";
    private String r = "";

    /* loaded from: classes3.dex */
    public enum Type {
        User,
        System
    }

    public void a(nexCollage.CollageInfoChangedListener collageInfoChangedListener) {
        this.a = collageInfoChangedListener;
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfo
    public String getId() {
        return this.g;
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfo
    public RectF getRectangle() {
        return this.e;
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfo
    public List<PointF> getPositions() {
        return this.f;
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfoTitle
    public String getTitle(String str) {
        String str2;
        String str3 = this.n;
        if (str3 != null) {
            return str3;
        }
        if (str != null && this.m.containsKey(str)) {
            str2 = this.m.get(str);
        } else {
            String language = Locale.getDefault().getLanguage();
            if (this.m.containsKey(language)) {
                str2 = this.m.get(language);
            } else {
                str2 = this.m.containsKey("en") ? this.m.get("en") : "";
            }
        }
        if (str2.startsWith("@content=")) {
            if (this.a == null) {
                return str2;
            }
            return this.a.TitleInfoContentTime(this.i, str2.substring(9));
        } else if (!str2.startsWith("@collage=") || this.a == null) {
            return str2;
        } else {
            return this.a.CollageTime(str2.substring(9));
        }
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfoTitle
    public boolean setTitle(String str) {
        this.n = str;
        nexCollage.CollageInfoChangedListener collageInfoChangedListener = this.a;
        if (collageInfoChangedListener != null) {
            collageInfoChangedListener.TitleInfoChanged();
            return true;
        }
        return true;
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfoTitle
    public String getTitleFont() {
        return this.o;
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfoTitle
    public boolean setTitleFont(String str) {
        this.o = str;
        return true;
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfoTitle
    public int getTitleFillColor() {
        String str = this.p;
        if (str == null || str.length() <= 0) {
            return 0;
        }
        String str2 = this.p;
        int parseInt = Integer.parseInt(str2.substring(1, str2.length()));
        return Color.argb(Color.blue(parseInt), Color.alpha(parseInt), Color.red(parseInt), Color.green(parseInt));
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfoTitle
    public boolean setTitleFillColor(int i) {
        int alpha = Color.alpha(i);
        int red = Color.red(i);
        int green = Color.green(i);
        int blue = Color.blue(i);
        if (Color.alpha(i) == 0) {
            this.p = "";
        } else {
            this.p = String.format("#%8X", Integer.valueOf(Color.argb(red, green, blue, alpha)));
        }
        return true;
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfoTitle
    public int getTitleStrokeColor() {
        String str = this.p;
        if (str == null || str.length() <= 0) {
            return 0;
        }
        String str2 = this.p;
        int parseInt = Integer.parseInt(str2.substring(1, str2.length()));
        return Color.argb(Color.blue(parseInt), Color.alpha(parseInt), Color.red(parseInt), Color.green(parseInt));
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfoTitle
    public boolean setTitleStrokeColor(int i) {
        int alpha = Color.alpha(i);
        int red = Color.red(i);
        int green = Color.green(i);
        int blue = Color.blue(i);
        if (Color.alpha(i) == 0) {
            this.q = "";
        } else {
            this.q = String.format("#%8X", Integer.valueOf(Color.argb(red, green, blue, alpha)));
        }
        return true;
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfoTitle
    public int getTitleDropShadowColor() {
        String str = this.p;
        if (str == null || str.length() <= 0) {
            return 0;
        }
        String str2 = this.p;
        int parseInt = Integer.parseInt(str2.substring(1, str2.length()));
        return Color.argb(Color.blue(parseInt), Color.alpha(parseInt), Color.red(parseInt), Color.green(parseInt));
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfoTitle
    public boolean setTitleDropShadowColor(int i) {
        int alpha = Color.alpha(i);
        int red = Color.red(i);
        int green = Color.green(i);
        int blue = Color.blue(i);
        if (Color.alpha(i) == 0) {
            this.r = "";
        } else {
            this.r = String.format("#%8X", Integer.valueOf(Color.argb(red, green, blue, alpha)));
        }
        return true;
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfoTitle
    public int getTitleMaxLength() {
        return this.j;
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfoTitle
    public int getTitleRecommendLength() {
        return this.k;
    }

    @Override // com.nexstreaming.nexeditorsdk.nexCollageInfoTitle
    public int getTitleMaxLines() {
        return this.l;
    }

    public boolean a() {
        return this.h == Type.User;
    }

    public String b() {
        String str;
        StringBuilder sb = new StringBuilder();
        try {
            if (this.o.length() > 0) {
                sb.append(URLEncoder.encode(getId() + "_font", Keyczar.DEFAULT_ENCODING));
                sb.append("=");
                sb.append(URLEncoder.encode("fontid:" + this.o, Keyczar.DEFAULT_ENCODING));
                sb.append("&");
            }
            if (this.p.length() > 0) {
                sb.append(URLEncoder.encode(getId() + "_fill_color", Keyczar.DEFAULT_ENCODING));
                sb.append("=");
                sb.append(URLEncoder.encode(this.p, Keyczar.DEFAULT_ENCODING));
                sb.append("&");
            }
            if (this.q.length() > 0) {
                sb.append(URLEncoder.encode(getId() + "_stroke_color", Keyczar.DEFAULT_ENCODING));
                sb.append("=");
                sb.append(URLEncoder.encode(this.q, Keyczar.DEFAULT_ENCODING));
                sb.append("&");
            }
            if (this.r.length() > 0) {
                sb.append(URLEncoder.encode(getId() + "_dropshadow_color", Keyczar.DEFAULT_ENCODING));
                sb.append("=");
                sb.append(URLEncoder.encode(this.r, Keyczar.DEFAULT_ENCODING));
                sb.append("&");
            }
            sb.append(URLEncoder.encode(getId(), Keyczar.DEFAULT_ENCODING));
            sb.append("=");
            String str2 = this.n;
            if (str2 == null) {
                String language = Locale.getDefault().getLanguage();
                if (this.m.containsKey(language)) {
                    str = this.m.get(language);
                } else {
                    str = this.m.get("en");
                }
                if (str.startsWith("@content=")) {
                    if (this.a != null) {
                        str = this.a.TitleInfoContentTime(this.i, str.substring(9));
                    }
                    str = "";
                } else if (str.startsWith("@collage=")) {
                    if (this.a != null) {
                        str = this.a.CollageTime(str.substring(9));
                    }
                    str = "";
                }
                sb.append(URLEncoder.encode(str, Keyczar.DEFAULT_ENCODING));
            } else if (str2.length() <= 0) {
                sb.append(URLEncoder.encode(" ", Keyczar.DEFAULT_ENCODING));
            } else {
                sb.append(URLEncoder.encode(this.n, Keyczar.DEFAULT_ENCODING));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public float a(PointF pointF, PointF pointF2, PointF pointF3) {
        float f = pointF2.x;
        float f2 = pointF.x;
        float f3 = pointF3.y;
        float f4 = pointF.y;
        return ((f - f2) * (f3 - f4)) - ((pointF3.x - f2) * (pointF2.y - f4));
    }

    public int a(PointF pointF, List<PointF> list) {
        int i = 0;
        for (int i2 = 0; i2 < list.size() - 1; i2++) {
            if (list.get(i2).y <= pointF.y) {
                int i3 = i2 + 1;
                if (list.get(i3).y > pointF.y && a(list.get(i2), list.get(i3), pointF) > 0.0f) {
                    i++;
                }
            } else {
                int i4 = i2 + 1;
                if (list.get(i4).y <= pointF.y && a(list.get(i2), list.get(i4), pointF) < 0.0f) {
                    i--;
                }
            }
        }
        return i;
    }

    public boolean a(float f, float f2) {
        List<PointF> list = this.f;
        if (list == null || list.size() <= 0 || a(new PointF(f, f2), this.f) <= 0) {
            RectF rectF = this.e;
            if (rectF == null) {
                return false;
            }
            return rectF.contains(f, f2);
        }
        return true;
    }

    public String c() {
        return this.i;
    }

    public String a(JSONObject jSONObject) {
        try {
            this.c = Float.parseFloat(jSONObject.getString("start"));
            this.d = Float.parseFloat(jSONObject.getString("end"));
            if (jSONObject.has("position")) {
                String[] split = jSONObject.getString("position").replace(" ", "").split(",");
                if (split == null) {
                    return "Wrong position data of titleinfo";
                }
                this.f.clear();
                if (split.length == 4) {
                    float parseFloat = Float.parseFloat(split[0]);
                    float parseFloat2 = Float.parseFloat(split[1]);
                    float parseFloat3 = Float.parseFloat(split[2]);
                    float parseFloat4 = Float.parseFloat(split[3]);
                    PointF pointF = new PointF(parseFloat, parseFloat2);
                    PointF pointF2 = new PointF(parseFloat3, parseFloat2);
                    PointF pointF3 = new PointF(parseFloat3, parseFloat4);
                    PointF pointF4 = new PointF(parseFloat, parseFloat4);
                    this.f.add(pointF);
                    this.f.add(pointF2);
                    this.f.add(pointF3);
                    this.f.add(pointF4);
                    this.f.add(pointF);
                    this.e = new RectF(parseFloat, parseFloat2, parseFloat3, parseFloat4);
                } else {
                    float f = Float.MIN_VALUE;
                    float f2 = Float.MAX_VALUE;
                    float f3 = Float.MIN_VALUE;
                    float f4 = Float.MAX_VALUE;
                    for (int i = 0; i < split.length; i += 2) {
                        float parseFloat5 = Float.parseFloat(split[i]);
                        float parseFloat6 = Float.parseFloat(split[i + 1]);
                        if (f4 > parseFloat5) {
                            f4 = parseFloat5;
                        }
                        if (f < parseFloat5) {
                            f = parseFloat5;
                        }
                        if (f2 > parseFloat6) {
                            f2 = parseFloat6;
                        }
                        if (f3 < parseFloat6) {
                            f3 = parseFloat6;
                        }
                        this.f.add(new PointF(parseFloat5, parseFloat6));
                    }
                    this.e = new RectF(f4, f2, f, f3);
                }
            }
            this.g = jSONObject.getString("title_id");
            this.h = jSONObject.getString("title_type").compareTo("user") == 0 ? Type.User : Type.System;
            if (jSONObject.has("draw_id")) {
                this.i = jSONObject.getString("draw_id");
            }
            JSONObject jSONObject2 = jSONObject.getJSONObject("title_default");
            Iterator<String> keys = jSONObject2.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                this.m.put(next, jSONObject2.getString(next));
            }
            if (jSONObject.has("max_length")) {
                this.j = Integer.parseInt(jSONObject.getString("max_length"));
            }
            if (jSONObject.has("recommend_length")) {
                this.k = Integer.parseInt(jSONObject.getString("recommend_length"));
            }
            if (!jSONObject.has("title_max_lines")) {
                return null;
            }
            this.l = Integer.parseInt(jSONObject.getString("title_max_lines"));
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(b, "parse Collage failed : " + e.getMessage());
            return e.getMessage();
        }
    }

    public nexSaveDataFormat.nexCollageTitleInfoOf d() {
        nexSaveDataFormat.nexCollageTitleInfoOf nexcollagetitleinfoof = new nexSaveDataFormat.nexCollageTitleInfoOf();
        nexcollagetitleinfoof.userDropShadowColor = this.r;
        nexcollagetitleinfoof.userFillColor = this.p;
        nexcollagetitleinfoof.userFont = this.o;
        nexcollagetitleinfoof.userStrokeColor = this.q;
        nexcollagetitleinfoof.userText = this.n;
        return nexcollagetitleinfoof;
    }

    public void a(nexSaveDataFormat.nexCollageTitleInfoOf nexcollagetitleinfoof) {
        this.r = nexcollagetitleinfoof.userDropShadowColor;
        this.p = nexcollagetitleinfoof.userFillColor;
        this.o = nexcollagetitleinfoof.userFont;
        this.q = nexcollagetitleinfoof.userStrokeColor;
        this.n = nexcollagetitleinfoof.userText;
    }
}
