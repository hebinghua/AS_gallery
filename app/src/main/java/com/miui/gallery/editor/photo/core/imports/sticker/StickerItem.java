package com.miui.gallery.editor.photo.core.imports.sticker;

import com.miui.gallery.editor.photo.core.common.model.StickerData;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.mirror.synergy.CallMethod;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.a.j;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class StickerItem extends StickerData {
    public String cateName;
    public String content;
    public long id;

    public StickerItem(short s, String str, long j, String str2, String str3, String str4) {
        super(s, str, str2);
        this.content = str3;
        this.id = j;
        this.cateName = str4;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof StickerItem) && ((StickerItem) obj).id == this.id;
    }

    public int hashCode() {
        long j = this.id;
        return (int) (j ^ (j >>> 32));
    }

    public static JSONObject toJson(StickerItem stickerItem) {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put(j.k, stickerItem.priority);
            jSONObject.put("name", stickerItem.name);
            jSONObject.put("id", stickerItem.id);
            jSONObject.put(CallMethod.RESULT_ICON, stickerItem.icon);
            jSONObject.put(MiStat.Param.CONTENT, stickerItem.content);
            jSONObject.put("cateName", stickerItem.cateName);
            return jSONObject;
        } catch (JSONException e) {
            DefaultLogger.w("StickerItem", e);
            return null;
        }
    }

    public static StickerItem fromJson(JSONObject jSONObject) throws JSONException {
        return new StickerItem((short) jSONObject.getInt(j.k), jSONObject.optString("name"), jSONObject.getLong("id"), jSONObject.getString(CallMethod.RESULT_ICON), jSONObject.getString(MiStat.Param.CONTENT), jSONObject.optString("cateName"));
    }
}
