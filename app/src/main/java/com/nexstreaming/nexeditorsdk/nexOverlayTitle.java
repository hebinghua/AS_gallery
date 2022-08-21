package com.nexstreaming.nexeditorsdk;

import android.animation.TimeInterpolator;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import com.nexstreaming.app.common.nexasset.assetpackage.ItemCategory;
import com.nexstreaming.app.common.nexasset.assetpackage.ItemType;
import com.nexstreaming.nexeditorsdk.nexOverlayManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes3.dex */
public final class nexOverlayTitle {
    private static String a = "nexOverlayTitle";
    private String b;
    private String c;
    private String d;
    private String e;
    private Priority f = Priority.NONE;
    private int h = 0;
    private ArrayList<HashMap<String, String>> g = new ArrayList<>();

    /* loaded from: classes3.dex */
    public enum Priority {
        NONE,
        START,
        END,
        START_END
    }

    /* loaded from: classes3.dex */
    public interface TitleInfoListener {
        void OnTitleInfoListener(int i, String str, int i2, String str2, int i3, String str3, String str4, int i4, int i5);
    }

    private boolean a(JSONObject jSONObject) {
        new HashMap();
        try {
            this.b = jSONObject.getString("overlay_name");
            this.c = jSONObject.getString("overlay_version");
            this.d = jSONObject.getString("overlay_desc");
            this.e = jSONObject.getString("overlay_priority");
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            String str = a;
            Log.d(str, "pasrse Overlay info failed : " + e.getMessage());
            return false;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:100:0x024a, code lost:
        r21 = java.lang.Integer.parseInt(r29.getString("text_max_len"));
        r19 = java.lang.Integer.parseInt(r29.getString("font_size"));
     */
    /* JADX WARN: Code restructure failed: missing block: B:101:0x025f, code lost:
        if (r29.has("position") == false) goto L92;
     */
    /* JADX WARN: Code restructure failed: missing block: B:102:0x0261, code lost:
        r0 = r9.get("position").replace(" ", "").split(",");
     */
    /* JADX WARN: Code restructure failed: missing block: B:103:0x0273, code lost:
        if (r0 == null) goto L92;
     */
    /* JADX WARN: Code restructure failed: missing block: B:104:0x0275, code lost:
        r2 = java.lang.Integer.parseInt(r0[0]);
        r3 = java.lang.Integer.parseInt(r0[1]);
        r4 = java.lang.Integer.parseInt(r0[2]);
        r25 = java.lang.Integer.parseInt(r0[3]) - r3;
        r24 = r4 - r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:105:0x0298, code lost:
        r24 = 0;
        r25 = r19;
     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x029c, code lost:
        r30.OnTitleInfoListener(r28.h, r18, r19, r20, r21, r22, r23, r24, r25);
     */
    /* JADX WARN: Code restructure failed: missing block: B:95:0x022d, code lost:
        if (r9.containsKey("text") == false) goto L94;
     */
    /* JADX WARN: Code restructure failed: missing block: B:96:0x022f, code lost:
        r18 = r29.getString("font");
        r20 = r29.getString("text");
        r22 = r29.getString("text_desc");
     */
    /* JADX WARN: Code restructure failed: missing block: B:97:0x023f, code lost:
        if (r29.has("group") == false) goto L93;
     */
    /* JADX WARN: Code restructure failed: missing block: B:98:0x0241, code lost:
        r23 = r29.getString("group");
     */
    /* JADX WARN: Code restructure failed: missing block: B:99:0x0248, code lost:
        r23 = "";
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private java.lang.String b(org.json.JSONObject r29, com.nexstreaming.nexeditorsdk.nexOverlayTitle.TitleInfoListener r30) {
        /*
            Method dump skipped, instructions count: 699
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.nexeditorsdk.nexOverlayTitle.b(org.json.JSONObject, com.nexstreaming.nexeditorsdk.nexOverlayTitle$TitleInfoListener):java.lang.String");
    }

    public String a(JSONObject jSONObject, TitleInfoListener titleInfoListener) {
        if (!a(jSONObject)) {
            return "parseOverlayAssetInfo parse error";
        }
        try {
            JSONArray jSONArray = jSONObject.getJSONArray("overlay");
            for (int i = 0; i < jSONArray.length(); i++) {
                String b = b(jSONArray.getJSONObject(i), titleInfoListener);
                if (b != null) {
                    this.g.clear();
                    String str = a;
                    Log.d(str, "Overlay parse error : " + b);
                    return b;
                }
            }
            Log.d(a, "parseOverlayAsset end");
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            this.g.clear();
            String str2 = a;
            Log.d(str2, "parseOverlayAsset failed" + e.getMessage());
            return e.getMessage();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:102:0x030e  */
    /* JADX WARN: Removed duplicated region for block: B:104:0x031d  */
    /* JADX WARN: Removed duplicated region for block: B:105:0x0324  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0327  */
    /* JADX WARN: Removed duplicated region for block: B:110:0x0344  */
    /* JADX WARN: Removed duplicated region for block: B:129:0x0404  */
    /* JADX WARN: Removed duplicated region for block: B:37:0x0112  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x028b  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x028e  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x029d  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x02ac  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.lang.String a(android.content.Context r53, com.nexstreaming.nexeditorsdk.nexProject r54, java.util.List<com.nexstreaming.nexeditorsdk.nexOverlayManager.nexTitleInfo> r55) {
        /*
            Method dump skipped, instructions count: 1746
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.nexeditorsdk.nexOverlayTitle.a(android.content.Context, com.nexstreaming.nexeditorsdk.nexProject, java.util.List):java.lang.String");
    }

    public String a(nexProject nexproject) {
        if (nexproject == null) {
            return "Null project";
        }
        ArrayList<Integer> arrayList = new ArrayList();
        for (nexOverlayItem nexoverlayitem : nexproject.getOverlayItems()) {
            if (nexoverlayitem.getOverlayTitle()) {
                arrayList.add(Integer.valueOf(nexoverlayitem.getId()));
            }
        }
        for (Integer num : arrayList) {
            nexproject.removeOverlay(num.intValue());
        }
        return null;
    }

    public int a(int i, List<nexOverlayManager.nexTitleInfo> list, int i2) {
        for (nexOverlayManager.nexTitleInfo nextitleinfo : list) {
            if (nextitleinfo.getId() == i) {
                int fontSize = nextitleinfo.getFontSize();
                String str = a;
                Log.d(str, "getFontSize() id=" + i + ", FontSize=" + fontSize);
                return fontSize <= 0 ? i2 : fontSize;
            }
        }
        String str2 = a;
        Log.d(str2, "getFontSize() id=" + i + ", defaultFontSize=" + i2);
        return i2;
    }

    public String a(int i, List<nexOverlayManager.nexTitleInfo> list, String str) {
        for (nexOverlayManager.nexTitleInfo nextitleinfo : list) {
            if (nextitleinfo.getId() == i) {
                return nextitleinfo.getFontID();
            }
        }
        return str;
    }

    public String b(int i, List<nexOverlayManager.nexTitleInfo> list, String str) {
        for (nexOverlayManager.nexTitleInfo nextitleinfo : list) {
            if (nextitleinfo.getId() == i) {
                return nextitleinfo.getText();
            }
        }
        return str;
    }

    public String a(String str, List<nexOverlayManager.nexTitleInfo> list) {
        for (nexOverlayManager.nexTitleInfo nextitleinfo : list) {
            if (nextitleinfo.getGroup().startsWith(str.substring(0, 1)) && nextitleinfo.getText() != null && nextitleinfo.getText().length() > 0) {
                return nextitleinfo.getText();
            }
        }
        return null;
    }

    public int a(String str, List<nexOverlayManager.nexTitleInfo> list, int i) {
        for (nexOverlayManager.nexTitleInfo nextitleinfo : list) {
            if (nextitleinfo.getGroup().startsWith(str.substring(0, 1)) && nextitleinfo.getText() != null && nextitleinfo.getText().length() > 0) {
                return nextitleinfo.getFontSize();
            }
        }
        return i;
    }

    public boolean a(List<nexOverlayManager.nexTitleInfo> list, String str) {
        boolean z = false;
        for (nexOverlayManager.nexTitleInfo nextitleinfo : list) {
            if (nextitleinfo.getGroup().startsWith(str.substring(0, 1)) && (nextitleinfo.getText() == null || nextitleinfo.getText().length() <= 0)) {
                z = true;
            }
        }
        return z;
    }

    public int a(boolean z) {
        Iterator<HashMap<String, String>> it = this.g.iterator();
        int i = 0;
        while (it.hasNext()) {
            HashMap<String, String> next = it.next();
            int parseInt = Integer.parseInt(next.get("start_time"));
            int parseInt2 = Integer.parseInt(next.get("duration"));
            if (z) {
                if (parseInt >= 0 && i < parseInt2) {
                    i = parseInt2;
                }
            } else if (parseInt < 0 && i < parseInt2) {
                i = parseInt2;
            }
        }
        return i;
    }

    public Priority b(nexProject nexproject) {
        if (nexproject.getTotalTime() >= a(true) + a(false)) {
            return Priority.START_END;
        }
        if (this.e.equals("start")) {
            return Priority.START;
        }
        return Priority.END;
    }

    public TimeInterpolator a(String str) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case -1539081405:
                if (str.equals("DecelerateInterpolator")) {
                    c = 0;
                    break;
                }
                break;
            case -1327597199:
                if (str.equals("AnticipateInterpolator")) {
                    c = 1;
                    break;
                }
                break;
            case -1275674606:
                if (str.equals("OvershootInterpolator")) {
                    c = 2;
                    break;
                }
                break;
            case -1163632952:
                if (str.equals("AnticipateOvershootInterpolator")) {
                    c = 3;
                    break;
                }
                break;
            case -142581660:
                if (str.equals("AccelerateInterpolator")) {
                    c = 4;
                    break;
                }
                break;
            case 593057964:
                if (str.equals("LinearInterpolator")) {
                    c = 5;
                    break;
                }
                break;
            case 1416217487:
                if (str.equals("BounceInterpolator")) {
                    c = 6;
                    break;
                }
                break;
            case 1682001069:
                if (str.equals("CycleInterpolator")) {
                    c = 7;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return new DecelerateInterpolator();
            case 1:
                return new AnticipateInterpolator();
            case 2:
                return new OvershootInterpolator();
            case 3:
                return new AnticipateOvershootInterpolator();
            case 4:
                return new AccelerateInterpolator();
            case 5:
                return new LinearInterpolator();
            case 6:
                return new BounceInterpolator();
            case 7:
                return new CycleInterpolator(1.0f);
            default:
                return new AccelerateDecelerateInterpolator();
        }
    }

    private nexOverlayImage b(String str) {
        com.nexstreaming.app.common.nexasset.assetpackage.f c = com.nexstreaming.app.common.nexasset.assetpackage.c.a().c(str);
        if (c != null && c.getCategory() == ItemCategory.overlay && c.getType() == ItemType.overlay) {
            return new nexOverlayImage(str, true);
        }
        return null;
    }
}
