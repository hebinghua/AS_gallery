package com.miui.gallery.cloud.peopleface;

import android.content.ContentValues;
import com.miui.gallery.model.PeopleContactInfo;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class FaceAlbumUtil {
    public static ContentValues getContentValuesForPeopleFace(JSONObject jSONObject) throws JSONException {
        if (jSONObject == null) {
            return null;
        }
        ContentValues contentValues = new ContentValues();
        if (jSONObject.has("id")) {
            contentValues.put("serverId", jSONObject.getString("id"));
        }
        if (jSONObject.has("status")) {
            contentValues.put("serverStatus", jSONObject.getString("status"));
        }
        if (jSONObject.has(nexExportFormat.TAG_FORMAT_TYPE)) {
            contentValues.put(nexExportFormat.TAG_FORMAT_TYPE, jSONObject.getString(nexExportFormat.TAG_FORMAT_TYPE));
        }
        if (jSONObject.has("eTag")) {
            contentValues.put("eTag", jSONObject.getString("eTag"));
        }
        if (jSONObject.has("parentId")) {
            contentValues.put("groupId", jSONObject.getString("parentId"));
        }
        if (jSONObject.has("peopleContent")) {
            JSONObject jSONObject2 = jSONObject.getJSONObject("peopleContent");
            if (jSONObject2.getBoolean("isSetPeopleName")) {
                contentValues.put("peopleName", jSONObject2.getString("peopleName"));
                contentValues.put("visibilityType", (Integer) 1);
            }
            if (jSONObject2.has("contact")) {
                String jSONObject3 = jSONObject2.getJSONObject("contact").toString();
                contentValues.put("peopleContactJsonInfo", jSONObject3);
                PeopleContactInfo fromJson = PeopleContactInfo.fromJson(jSONObject3);
                if (fromJson != null) {
                    contentValues.put("relationType", Integer.valueOf(PeopleContactInfo.getRelationType(fromJson.relationWithMe)));
                    String str = fromJson.relationWithMeText;
                    if (str != null) {
                        contentValues.put("relationText", str);
                    }
                }
            }
            if (!jSONObject2.getBoolean("isVisible")) {
                contentValues.put("visibilityType", (Integer) 2);
            }
            if (jSONObject2.has("coverId")) {
                contentValues.put("selectCoverId", jSONObject2.getString("coverId"));
            }
            if (jSONObject2.has("peopleInfo")) {
                JSONObject jSONObject4 = jSONObject2.getJSONObject("peopleInfo");
                if (jSONObject4.has("peopleType") && "baby".equalsIgnoreCase(jSONObject4.getString("peopleType"))) {
                    if (jSONObject4.has("gender")) {
                        if ("male".equalsIgnoreCase(jSONObject4.getString("gender"))) {
                            contentValues.put("peopleType", (Integer) 1);
                        } else {
                            contentValues.put("peopleType", (Integer) 2);
                        }
                    } else {
                        contentValues.put("peopleType", (Integer) 1);
                    }
                } else {
                    putPeopleTypeNoDefine(contentValues);
                }
            } else {
                putPeopleTypeNoDefine(contentValues);
            }
        }
        return contentValues;
    }

    public static void putPeopleTypeNoDefine(ContentValues contentValues) {
        contentValues.put("peopleType", (Integer) 0);
    }

    public static String getPeopleName(JSONObject jSONObject) throws JSONException {
        if (jSONObject.has("peopleContent")) {
            JSONObject jSONObject2 = jSONObject.getJSONObject("peopleContent");
            if (!jSONObject2.getBoolean("isSetPeopleName")) {
                return null;
            }
            return jSONObject2.getString("peopleName");
        }
        return null;
    }
}
