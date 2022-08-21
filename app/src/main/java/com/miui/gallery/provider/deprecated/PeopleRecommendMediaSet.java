package com.miui.gallery.provider.deprecated;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import com.miui.gallery.cloud.peopleface.FaceDataManager;
import com.miui.gallery.provider.FaceManager;
import com.miui.gallery.util.face.FeedbackIgnoredPeople2Server;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class PeopleRecommendMediaSet {
    public static Object sLock = new Object();
    public HashMap<String, String> mGroupIdOfFaceMap = new HashMap<>();
    public HashMap<String, Boolean> mPeopleRecommondHistoryMap = new HashMap<>();
    public NormalPeopleFaceMediaSet mRecommendSourcePeople;

    public PeopleRecommendMediaSet(NormalPeopleFaceMediaSet normalPeopleFaceMediaSet) {
        this.mRecommendSourcePeople = normalPeopleFaceMediaSet;
    }

    public void refreshRecommendInfo() {
        synchronized (sLock) {
            queryTableOfPeopleRecommendInfo(this.mRecommendSourcePeople.getServerId());
        }
    }

    public final void queryTableOfPeopleRecommendInfo(String str) {
        Cursor cursor = null;
        try {
            cursor = FaceDataManager.queryPeopleRecommondTableToCursor(new String[]{" * "}, "peopleServerId = ? ", new String[]{str}, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToNext();
                fillMapGroupIdOfFace(cursor.getString(2));
                fillMapPeopleRecommondHistory(cursor.getString(3));
            }
            if (cursor == null) {
                return;
            }
        } catch (Exception unused) {
            if (cursor == null) {
                return;
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
        cursor.close();
    }

    public final void fillMapPeopleRecommondHistory(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                Iterator<String> keys = jSONObject.keys();
                while (keys != null) {
                    if (!keys.hasNext()) {
                        return;
                    }
                    String next = keys.next();
                    this.mPeopleRecommondHistoryMap.put(next, Boolean.valueOf(jSONObject.getBoolean(next)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static PeopleRecommendMediaSet refreshRecommendHistoryToTrue(ArrayList<String> arrayList, NormalPeopleFaceMediaSet normalPeopleFaceMediaSet) {
        PeopleRecommendMediaSet peopleRecommendMediaSet = new PeopleRecommendMediaSet(normalPeopleFaceMediaSet);
        peopleRecommendMediaSet.refreshRecommendInfo();
        peopleRecommendMediaSet.refreshRecommendHistoryToTrue(arrayList);
        return peopleRecommendMediaSet;
    }

    public static void addSelectItemsToRecommendedMediaSet(ArrayList<String> arrayList, NormalPeopleFaceMediaSet normalPeopleFaceMediaSet) {
        PeopleRecommendMediaSet peopleRecommendMediaSet = new PeopleRecommendMediaSet(normalPeopleFaceMediaSet);
        peopleRecommendMediaSet.refreshRecommendInfo();
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            normalPeopleFaceMediaSet.mergeAnAlbumToThis(peopleRecommendMediaSet.mGroupIdOfFaceMap.get(it.next()));
        }
    }

    public static void feedbackIgnoredPeople2Server(ArrayList<String> arrayList, NormalPeopleFaceMediaSet normalPeopleFaceMediaSet) {
        PeopleRecommendMediaSet peopleRecommendMediaSet = new PeopleRecommendMediaSet(normalPeopleFaceMediaSet);
        peopleRecommendMediaSet.refreshRecommendInfo();
        ArrayList arrayList2 = new ArrayList();
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            arrayList2.add(peopleRecommendMediaSet.mGroupIdOfFaceMap.get(it.next()));
        }
        FeedbackIgnoredPeople2Server.startFeedbackIgnoredPeople2Server(normalPeopleFaceMediaSet.getServerId(), arrayList2);
    }

    public static String hashMapToJson(HashMap hashMap) {
        String str = "{";
        for (Map.Entry entry : hashMap.entrySet()) {
            str = (str + "'" + entry.getKey() + "':") + "'" + entry.getValue() + "',";
        }
        return str.substring(0, str.lastIndexOf(",")) + "}";
    }

    public void refreshRecommendHistoryToTrue(ArrayList<String> arrayList) {
        synchronized (sLock) {
            Iterator<String> it = arrayList.iterator();
            while (it.hasNext()) {
                this.mPeopleRecommondHistoryMap.put(it.next(), Boolean.TRUE);
            }
            saveHistoryToDB();
        }
    }

    public final void saveHistoryToDB() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("recommendHistoryJson", hashMapToJson(this.mPeopleRecommondHistoryMap));
        FaceDataManager.safeUpdatePeopleRecommend(contentValues, "peopleServerId = ?", new String[]{this.mRecommendSourcePeople.getServerId()});
    }

    public final ArrayList<String> getNeedRecommendPeopleFaceId() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (String str : this.mGroupIdOfFaceMap.keySet()) {
            if (!this.mPeopleRecommondHistoryMap.containsKey(str)) {
                this.mPeopleRecommondHistoryMap.put(str, Boolean.FALSE);
                arrayList.add(str);
            } else if (!this.mPeopleRecommondHistoryMap.get(str).booleanValue()) {
                arrayList.add(str);
            }
        }
        return arrayList;
    }

    public int getActualNeedRecommendPeopleFacePhotoNumber() {
        return FaceManager.queryCountOfPhotosOfOneRecommendAlbum(getServerIdsIn());
    }

    public final void fillMapGroupIdOfFace(String str) {
        try {
            JSONArray jSONArray = new JSONObject(str).getJSONObject("data").getJSONArray("recommendPeoples");
            if (jSONArray == null) {
                return;
            }
            ArrayList<String> queryAllPeopleAlbumServerIds = FaceManager.queryAllPeopleAlbumServerIds();
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i).getJSONObject("coreFace");
                Iterator<String> keys = jSONObject.keys();
                while (true) {
                    if (keys != null && keys.hasNext()) {
                        String next = keys.next();
                        String string = jSONArray.getJSONObject(i).getString("peopleId");
                        if (jSONObject.getInt(next) == 1 && queryAllPeopleAlbumServerIds.indexOf(string) != -1) {
                            this.mGroupIdOfFaceMap.put(next, string);
                            break;
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getServerIdsIn() {
        ArrayList<String> needRecommendPeopleFaceId = getNeedRecommendPeopleFaceId();
        if (needRecommendPeopleFaceId.size() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        Iterator<String> it = needRecommendPeopleFaceId.iterator();
        while (it.hasNext()) {
            String next = it.next();
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append("'");
            sb.append(next);
            sb.append("'");
        }
        return sb.toString();
    }
}
