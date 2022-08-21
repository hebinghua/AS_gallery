package com.miui.gallery.model;

import android.content.Context;
import android.text.TextUtils;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import java.util.ArrayList;
import java.util.Collections;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class PeopleContactInfo {
    public static String JSON_TAG_PHONE_NUMBER = "phoneNumbers";
    public static String JSON_TAG_RELATION = "relation";
    public static String JSON_TAG_RELATIONSHIP = "relationship";
    public static String JSON_TAG_RELATIONTEXT = "relationText";
    public static String[] sRelationItemsValue;
    public String coverPath;
    public boolean isRepeatName;
    public String localGroupId;
    public String name;
    public String phone;
    public String relationWithMe;
    public String relationWithMeText;

    public static String getUserDefineRelationOrderSql() {
        return "(CASE WHEN relationType = 7 THEN relationText ELSE NULL END) COLLATE LOCALIZED ";
    }

    public PeopleContactInfo() {
        initRelationItemsValue();
    }

    public static void initRelationItemsValue() {
        if (sRelationItemsValue == null) {
            sRelationItemsValue = GalleryApp.sGetAndroidContext().getResources().getStringArray(R.array.relation_with_me_value);
        }
    }

    public static PeopleContactInfo fromJson(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            PeopleContactInfo peopleContactInfo = new PeopleContactInfo();
            if (jSONObject.has(JSON_TAG_PHONE_NUMBER)) {
                peopleContactInfo.phone = (String) jSONObject.getJSONArray(JSON_TAG_PHONE_NUMBER).get(0);
            }
            if (jSONObject.has(JSON_TAG_RELATIONSHIP)) {
                String optString = jSONObject.getJSONObject(JSON_TAG_RELATIONSHIP).optString(JSON_TAG_RELATION);
                peopleContactInfo.relationWithMe = optString;
                if (isUserDefineRelation(getRelationType(optString))) {
                    peopleContactInfo.relationWithMeText = jSONObject.getJSONObject(JSON_TAG_RELATIONSHIP).optString(JSON_TAG_RELATIONTEXT);
                } else {
                    peopleContactInfo.relationWithMeText = peopleContactInfo.relationWithMe;
                }
            }
            return peopleContactInfo;
        } catch (Exception unused) {
            return null;
        }
    }

    public String formatContactJson() {
        boolean z = !TextUtils.isEmpty(this.phone);
        boolean z2 = !TextUtils.isEmpty(this.relationWithMe);
        if (z || z2) {
            JSONObject jSONObject = new JSONObject();
            if (z) {
                try {
                    JSONArray jSONArray = new JSONArray();
                    jSONArray.put(0, this.phone);
                    jSONObject.put(JSON_TAG_PHONE_NUMBER, jSONArray);
                } catch (JSONException unused) {
                    return null;
                }
            }
            if (z2) {
                JSONObject jSONObject2 = new JSONObject();
                jSONObject2.put(JSON_TAG_RELATION, this.relationWithMe);
                jSONObject2.put(JSON_TAG_RELATIONTEXT, this.relationWithMeText);
                jSONObject.put(JSON_TAG_RELATIONSHIP, jSONObject2);
            }
            return jSONObject.toString();
        }
        return null;
    }

    /* loaded from: classes2.dex */
    public enum Relation {
        unknown(0),
        friend(1),
        classmate(2),
        collegue(3),
        family(4),
        child(5),
        myself(6),
        userDefine(7);
        
        private final int mRelationType;

        Relation(int i) {
            this.mRelationType = i;
        }

        public int getRelationType() {
            return this.mRelationType;
        }

        public static Relation fromRelationType(int i) {
            Relation relation = friend;
            if (i == relation.mRelationType) {
                return relation;
            }
            Relation relation2 = classmate;
            if (i == relation2.mRelationType) {
                return relation2;
            }
            Relation relation3 = collegue;
            if (i == relation3.mRelationType) {
                return relation3;
            }
            Relation relation4 = family;
            if (i == relation4.mRelationType) {
                return relation4;
            }
            Relation relation5 = child;
            if (i == relation5.mRelationType) {
                return relation5;
            }
            Relation relation6 = myself;
            if (i == relation6.mRelationType) {
                return relation6;
            }
            Relation relation7 = userDefine;
            return i == relation7.mRelationType ? relation7 : unknown;
        }
    }

    public static int getRelationTypesCount() {
        return Relation.values().length;
    }

    public static String getRelationOrderSql() {
        return "CASE relationType WHEN " + Relation.myself.getRelationType() + " THEN 0 WHEN " + Relation.child.getRelationType() + " THEN 1 WHEN " + Relation.family.getRelationType() + " THEN 2 WHEN " + Relation.collegue.getRelationType() + " THEN 3 WHEN " + Relation.classmate.getRelationType() + " THEN 4 WHEN " + Relation.friend.getRelationType() + " THEN 5 WHEN " + Relation.userDefine.getRelationType() + " THEN 6 WHEN " + Relation.unknown.getRelationType() + " THEN 7 ELSE 8 END ";
    }

    public int getRelationType() {
        Relation[] values;
        if (TextUtils.isEmpty(this.relationWithMe)) {
            return Relation.unknown.getRelationType();
        }
        for (Relation relation : Relation.values()) {
            if (relation == Relation.valueOf(this.relationWithMe)) {
                return relation.getRelationType();
            }
        }
        return Relation.unknown.getRelationType();
    }

    public static int getRelationType(String str) {
        Relation[] values;
        if (TextUtils.isEmpty(str)) {
            return Relation.unknown.getRelationType();
        }
        for (Relation relation : Relation.values()) {
            if (relation == Relation.valueOf(str)) {
                return relation.getRelationType();
            }
        }
        return Relation.unknown.getRelationType();
    }

    public static Relation getRelation(int i) {
        return Relation.fromRelationType(i);
    }

    public static String getRelationValue(int i) {
        return getRelationValue(getRelation(i));
    }

    public static String getRelationValue(Relation relation) {
        String[] strArr;
        if (relation == null) {
            return null;
        }
        initRelationItemsValue();
        for (String str : sRelationItemsValue) {
            if (relation == Relation.valueOf(str)) {
                return str;
            }
        }
        return null;
    }

    public static String getRelationShow(int i) {
        if (i < 0) {
            i = Relation.unknown.getRelationType();
        }
        Relation relation = getRelation(i);
        String[] stringArray = GalleryApp.sGetAndroidContext().getResources().getStringArray(R.array.relation_with_me);
        initRelationItemsValue();
        for (int i2 = 0; i2 < sRelationItemsValue.length; i2++) {
            if (relation.name().equalsIgnoreCase(sRelationItemsValue[i2])) {
                return stringArray[i2];
            }
        }
        return "";
    }

    public static String getDefaultNameForMyself(Context context) {
        return context.getResources().getString(R.string.mark_myself_default_name);
    }

    public static ArrayList<String> getSystemRelationNameItems() {
        initRelationItemsValue();
        String[] stringArray = GalleryApp.sGetAndroidContext().getResources().getStringArray(R.array.relation_with_me);
        String str = Relation.unknown.toString();
        ArrayList<String> arrayList = new ArrayList<>();
        for (int i = 0; i < stringArray.length; i++) {
            if (!sRelationItemsValue[i].equals(str)) {
                arrayList.add(stringArray[i]);
            }
        }
        return arrayList;
    }

    public static String getRelationName(Relation relation) {
        initRelationItemsValue();
        String relationValue = getRelationValue(relation);
        String[] stringArray = GalleryApp.sGetAndroidContext().getResources().getStringArray(R.array.relation_with_me);
        for (int i = 0; i < stringArray.length; i++) {
            if (sRelationItemsValue[i].equals(relationValue)) {
                return stringArray[i];
            }
        }
        return null;
    }

    public static ArrayList<String> getSystemRelationValueItems() {
        initRelationItemsValue();
        ArrayList<String> arrayList = new ArrayList<>();
        String str = Relation.unknown.toString();
        int i = 0;
        while (true) {
            String[] strArr = sRelationItemsValue;
            if (i < strArr.length) {
                if (!strArr[i].equals(str)) {
                    arrayList.add(sRelationItemsValue[i]);
                }
                i++;
            } else {
                return arrayList;
            }
        }
    }

    public static boolean isUnKnownRelation(int i) {
        return i == Relation.unknown.getRelationType();
    }

    public static boolean isUserDefineRelation(int i) {
        return i == Relation.userDefine.getRelationType();
    }

    public static boolean isBabyRelation(int i) {
        return i == Relation.child.getRelationType();
    }

    public static boolean isMyselfRelation(int i) {
        return i == Relation.myself.getRelationType();
    }

    public static String getUserDefineRelation() {
        return Relation.userDefine.toString();
    }

    public static int getUserDefineRelationIndex() {
        return Relation.userDefine.getRelationType();
    }

    /* loaded from: classes2.dex */
    public static class UserDefineRelation {
        public static ArrayList<String> mUserDefineRelations;

        public static synchronized void setUserDefineRelations(ArrayList<String> arrayList) {
            synchronized (UserDefineRelation.class) {
                mUserDefineRelations = arrayList;
            }
        }

        public static synchronized ArrayList<String> getUserDefineRelations() {
            synchronized (UserDefineRelation.class) {
                if (mUserDefineRelations != null) {
                    return new ArrayList<>(mUserDefineRelations);
                }
                return null;
            }
        }

        public static synchronized void addRelation(String str) {
            synchronized (UserDefineRelation.class) {
                ArrayList<String> arrayList = mUserDefineRelations;
                if (arrayList != null && str != null && arrayList.indexOf(str) == -1) {
                    mUserDefineRelations.add(str);
                    Collections.sort(mUserDefineRelations);
                }
            }
        }
    }
}
