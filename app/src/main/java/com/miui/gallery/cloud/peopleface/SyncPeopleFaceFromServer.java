package com.miui.gallery.cloud.peopleface;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import com.miui.gallery.cloud.CheckResult;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.GalleryCloudSyncTagUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.cloud.GalleryMiCloudServerException;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.cloud.SyncConditionManager;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.cloud.base.GallerySyncResult;
import com.miui.gallery.cloud.base.RetryRequestHelper;
import com.miui.gallery.cloud.base.SyncTask;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.Encode;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.Log2File;
import com.miui.gallery.util.SyncLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class SyncPeopleFaceFromServer {
    public Account mAccount;
    public Context mContext;
    public GalleryExtendedAuthToken mExtendedAuthToken;
    public boolean mShouldFlatPeopleRelationshipInDB = false;
    public Runnable mRunnable = new Runnable() { // from class: com.miui.gallery.cloud.peopleface.SyncPeopleFaceFromServer.2
        @Override // java.lang.Runnable
        public void run() {
        }
    };

    public int getSyncItemLimit() {
        return 100;
    }

    public SyncPeopleFaceFromServer(Context context, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken) {
        this.mContext = context;
        this.mAccount = account;
        this.mExtendedAuthToken = galleryExtendedAuthToken;
    }

    public String getPeopleFaceSyncUrl() {
        return HostManager.PeopleFace.getPeopleFaceSyncUrl();
    }

    public String getSyncFaceConcreteInfoUrl() {
        return HostManager.PeopleFace.getFaceInfoSyncUrl();
    }

    public final GallerySyncResult<JSONObject> sync() throws ClientProtocolException, IOException, JSONException, URISyntaxException, IllegalBlockSizeException, BadPaddingException, GalleryMiCloudServerException {
        long currentTimeMillis = System.currentTimeMillis();
        while (SyncConditionManager.check(0) != 2) {
            GallerySyncResult<JSONObject> retryTask = RetryRequestHelper.retryTask(new SyncTask<JSONObject>() { // from class: com.miui.gallery.cloud.peopleface.SyncPeopleFaceFromServer.1
                @Override // com.miui.gallery.cloud.base.SyncTask
                public String getIdentifier() {
                    return SyncPeopleFaceFromServer.this.getPeopleFaceSyncUrl();
                }

                @Override // com.miui.gallery.cloud.base.SyncTask
                public GallerySyncResult<JSONObject> run() throws Exception {
                    SyncPeopleFaceFromServer syncPeopleFaceFromServer = SyncPeopleFaceFromServer.this;
                    return CheckResult.checkXMResultCodeForFaceRequest(syncPeopleFaceFromServer.getPeopleFaceList(syncPeopleFaceFromServer.getSyncItemLimit()), null);
                }
            });
            JSONObject jSONObject = retryTask.data;
            if (ErrorHandler.simpleCheckWhetherNeedCleanTables(jSONObject, this.mAccount)) {
                SyncLogger.w("syncface", "needCleanData");
            } else {
                if (jSONObject != null) {
                    JSONObject optJSONObject = jSONObject.optJSONObject("data");
                    ArrayList<String> handlePeopleFaceDataJson = handlePeopleFaceDataJson(optJSONObject);
                    if (handlePeopleFaceDataJson != null && handlePeopleFaceDataJson.size() > 0) {
                        retryTask = getFaceConcreteInfoList(handlePeopleFaceDataJson);
                        if (retryTask.data == null) {
                        }
                    }
                    updateSyncInfo(optJSONObject);
                    if (optJSONObject != null && optJSONObject.length() != 0 && shouldContinue(optJSONObject)) {
                    }
                }
                flatDBAndCheckBabyAlbumAfterSync();
                SyncLogger.d("syncface", "sync people face from server finish, cost time: " + (System.currentTimeMillis() - currentTimeMillis));
                if (!GalleryPreferences.Face.isFirstSyncCompleted()) {
                    GalleryPreferences.Face.setFirstSyncCompleted();
                }
                if (retryTask.code != GallerySyncCode.OK) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("name", getClass().getSimpleName());
                    hashMap.put("result", retryTask.toString());
                    SamplingStatHelper.recordCountEvent("Sync", "sync_error_class", hashMap);
                }
                return retryTask;
            }
        }
        SyncLogger.w("syncface", "SyncConditionManager check false");
        return new GallerySyncResult.Builder().setCode(GallerySyncCode.CONDITION_INTERRUPTED).build();
    }

    public final void flatDBAndCheckBabyAlbumAfterSync() {
        if (this.mShouldFlatPeopleRelationshipInDB) {
            GalleryUtils.runOnMainThreadPostDelay(this.mRunnable, 4000);
            this.mShouldFlatPeopleRelationshipInDB = false;
        }
    }

    public final boolean shouldContinue(JSONObject jSONObject) throws JSONException {
        if (jSONObject == null) {
            return false;
        }
        return jSONObject.getBoolean("hasMore");
    }

    public final JSONObject getPeopleFaceList(int i) throws ClientProtocolException, IOException, JSONException, URISyntaxException, IllegalBlockSizeException, BadPaddingException, GalleryMiCloudServerException {
        if (Log2File.getInstance().canLog()) {
            Log2File.getInstance().flushLog("syncface", "getPeopleFaceList :", null);
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("syncToken", GalleryCloudSyncTagUtils.getFaceSyncToken(this.mAccount)));
        if (i > 0) {
            arrayList.add(new BasicNameValuePair("limit", Long.toString(i)));
        }
        JSONObject fromXiaomi = CloudUtils.getFromXiaomi(getPeopleFaceSyncUrl(), arrayList, this.mAccount, this.mExtendedAuthToken, 0, false);
        if (Log2File.getInstance().canLog()) {
            Log2File log2File = Log2File.getInstance();
            log2File.flushLog("syncface", "getPeopleFaceList allJson=" + fromXiaomi, null);
        }
        SyncLogger.d("syncface", "getPeopleFaceList success\n");
        return fromXiaomi;
    }

    public final GallerySyncResult<JSONObject> getFaceConcreteInfoList(ArrayList<String> arrayList) throws ClientProtocolException, IOException, JSONException, URISyntaxException, IllegalBlockSizeException, BadPaddingException, GalleryMiCloudServerException {
        SyncLogger.d("syncface", "getFaceConcreteInfoList begin");
        int size = arrayList.size();
        StringBuilder sb = new StringBuilder();
        GallerySyncResult<JSONObject> gallerySyncResult = null;
        int i = 0;
        for (int i2 = 0; i2 < size; i2++) {
            sb.append(arrayList.get(i2));
            i++;
            if (i < 40 && i2 < size - 1) {
                sb.append(",");
            }
            if (i == 40 || i2 == size - 1) {
                final String sb2 = sb.toString();
                gallerySyncResult = RetryRequestHelper.retryTask(new SyncTask<JSONObject>() { // from class: com.miui.gallery.cloud.peopleface.SyncPeopleFaceFromServer.3
                    @Override // com.miui.gallery.cloud.base.SyncTask
                    public String getIdentifier() {
                        return SyncPeopleFaceFromServer.this.getSyncFaceConcreteInfoUrl();
                    }

                    @Override // com.miui.gallery.cloud.base.SyncTask
                    public GallerySyncResult<JSONObject> run() throws Exception {
                        return CheckResult.checkXMResultCodeForFaceRequest(SyncPeopleFaceFromServer.this.getFaceConcreteInfoListOneBatch(sb2), null);
                    }
                });
                if (gallerySyncResult.data == null) {
                    break;
                }
                sb = new StringBuilder();
                i = 0;
            }
        }
        SyncLogger.d("syncface", "getFaceConcreteInfoList end");
        return gallerySyncResult;
    }

    public final JSONObject getFaceConcreteInfoListOneBatch(String str) throws ClientProtocolException, IOException, JSONException, URISyntaxException, IllegalBlockSizeException, BadPaddingException, GalleryMiCloudServerException {
        if (Log2File.getInstance().canLog()) {
            Log2File log2File = Log2File.getInstance();
            log2File.flushLog("syncface", "getPeopleFaceInfo  " + str, null);
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("newFaceInfos", str));
        JSONObject postToXiaomi = CloudUtils.postToXiaomi(getSyncFaceConcreteInfoUrl(), arrayList, null, this.mAccount, this.mExtendedAuthToken, 0, false);
        try {
            handleFaceInfosDataJson(postToXiaomi.optJSONObject("data"));
        } catch (Exception unused) {
        }
        if (Log2File.getInstance().canLog()) {
            Log2File log2File2 = Log2File.getInstance();
            log2File2.flushLog("syncface", "getFaceConcreteInfoListOneBatch end,allJson=" + postToXiaomi, null);
        }
        return postToXiaomi;
    }

    public final void updateSyncInfo(JSONObject jSONObject) throws JSONException {
        if (jSONObject == null) {
            return;
        }
        GalleryCloudSyncTagUtils.setFaceSyncWatermark(this.mAccount, jSONObject.getLong("watermark"));
        GalleryCloudSyncTagUtils.setFaceSyncToken(this.mAccount, jSONObject.getString("syncToken"));
    }

    public final ArrayList<String> handlePeopleFaceDataJson(JSONObject jSONObject) throws JSONException {
        ArrayList<String> arrayList = null;
        if (jSONObject != null && jSONObject.has("records")) {
            JSONObject jSONObject2 = jSONObject.getJSONObject("newFaceInfos");
            JSONArray jSONArray = jSONObject.getJSONArray("records");
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject3 = jSONArray.getJSONObject(i);
                handleItem(jSONObject3);
                String string = jSONObject3.getString("id");
                if (jSONObject2.has(string)) {
                    JSONObject optJSONObject = jSONObject3.optJSONObject("faceContent");
                    if (optJSONObject != null) {
                        SyncLogger.d("syncface", "to get FaceInfo from faceContent");
                        String optString = optJSONObject.optString("sha1Base64");
                        JSONObject optJSONObject2 = optJSONObject.optJSONObject("baseFaceInfo");
                        if (!TextUtils.isEmpty(optString) && optJSONObject2 != null) {
                            String optString2 = jSONObject3.optString("id");
                            JSONObject optJSONObject3 = optJSONObject2.optJSONObject("faceInfo");
                            try {
                                List<String> imageServerIdsBySHA1 = CloudUtils.getImageServerIdsBySHA1(Encode.byteArrayToHexString(Base64.decode(optString, 8)));
                                double optDouble = optJSONObject2.optDouble("faceCoverScore", -1.0d);
                                if (optJSONObject3 != null && BaseMiscUtil.isValid(imageServerIdsBySHA1)) {
                                    SyncLogger.d("syncface", "get FaceInfo from faceContent success");
                                    putFaceInfo2Db(optJSONObject3, optString2, optDouble);
                                    putFaceImageIdsInfo2Db(imageServerIdsBySHA1, optString2);
                                }
                            } catch (Exception e) {
                                SyncLogger.e("syncface", "handlePeopleFaceDataJson error", e);
                                e.printStackTrace();
                            }
                        }
                    }
                    SyncLogger.d("syncface", "get FaceInfo from faceContent fail");
                    if (arrayList == null) {
                        arrayList = new ArrayList<>();
                    }
                    arrayList.add(jSONObject2.getString(string));
                }
            }
            return arrayList;
        }
        return null;
    }

    public final void handleFaceInfosDataJson(JSONObject jSONObject) throws JSONException {
        if (jSONObject == null) {
            return;
        }
        JSONArray optJSONArray = jSONObject.optJSONArray("faceInfoRecords");
        if (optJSONArray == null || optJSONArray.length() <= 0) {
            SyncLogger.e("syncface", "response face info is empty");
            return;
        }
        SyncLogger.d("syncface", "handleFaceInfosDataJson one batch:");
        for (int i = 0; i < optJSONArray.length(); i++) {
            JSONObject jSONObject2 = optJSONArray.getJSONObject(i);
            String string = jSONObject2.getString("faceId");
            JSONObject jSONObject3 = jSONObject2.getJSONObject("faceInfo");
            JSONArray jSONArray = jSONObject2.getJSONArray("imageIds");
            putFaceInfo2Db(jSONObject3, string, jSONObject2.optDouble("faceCoverScore", -1.0d));
            putFaceImageIdsInfo2Db(jSONArray, string);
        }
    }

    public final void putFaceInfo2Db(JSONObject jSONObject, String str, double d) throws JSONException {
        double d2 = jSONObject.getDouble("faceXScale");
        double d3 = jSONObject.getDouble("faceYScale");
        double d4 = jSONObject.getDouble("faceWScale");
        double d5 = jSONObject.getDouble("faceHScale");
        double d6 = jSONObject.getDouble("eyeLeftXScale");
        double d7 = jSONObject.getDouble("eyeLeftYScale");
        double d8 = jSONObject.getDouble("eyeRightXScale");
        double d9 = jSONObject.getDouble("eyeRightYScale");
        ContentValues contentValues = new ContentValues();
        contentValues.put("faceXScale", Double.valueOf(d2));
        contentValues.put("faceYScale", Double.valueOf(d3));
        contentValues.put("faceWScale", Double.valueOf(d4));
        contentValues.put("faceHScale", Double.valueOf(d5));
        contentValues.put("leftEyeXScale", Double.valueOf(d6));
        contentValues.put("leftEyeYScale", Double.valueOf(d7));
        contentValues.put("RightEyeXScale", Double.valueOf(d8));
        contentValues.put("RightEyeYScale", Double.valueOf(d9));
        contentValues.put("faceCoverScore", Double.valueOf(d));
        FaceDataManager.safeUpdateFace(contentValues, String.format(Locale.US, "(%s = '%s')", "serverId", str), null, true);
    }

    public final void putFaceImageIdsInfo2Db(List<String> list, String str) {
        for (int i = 0; i < list.size(); i++) {
            insertFace2Image(str, list.get(i));
        }
    }

    public final void putFaceImageIdsInfo2Db(JSONArray jSONArray, String str) throws JSONException {
        for (int i = 0; i < jSONArray.length(); i++) {
            insertFace2Image(str, jSONArray.get(i).toString());
        }
    }

    public final void insertFace2Image(String str, String str2) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("faceId", str);
        contentValues.put("imageServerId", str2);
        FaceDataManager.safeInsertFace2Image(contentValues);
    }

    public final boolean handleItem(JSONObject jSONObject) throws JSONException {
        if (jSONObject == null) {
            return false;
        }
        String string = jSONObject.getString(nexExportFormat.TAG_FORMAT_TYPE);
        String string2 = jSONObject.getString("id");
        String string3 = jSONObject.getString("status");
        PeopleFace item = FaceDataManager.getItem(string2);
        ContentValues contentValuesForPeopleFace = FaceAlbumUtil.getContentValuesForPeopleFace(jSONObject);
        if (item == null) {
            if (string.equals("PEOPLE")) {
                PeopleFace groupByPeopleName = FaceDataManager.getGroupByPeopleName(this.mContext, FaceAlbumUtil.getPeopleName(jSONObject));
                if (groupByPeopleName != null) {
                    setShouldFlatPeopleRelationshipInDB(contentValuesForPeopleFace, groupByPeopleName);
                    CloudUtils.updateToLocalDBForSync(FaceDataManager.PEOPLE_FACE_URI, contentValuesForPeopleFace, groupByPeopleName._id);
                } else {
                    handleNewPeopleFace(contentValuesForPeopleFace);
                    this.mShouldFlatPeopleRelationshipInDB = true;
                }
                return false;
            }
            handleNewPeopleFace(contentValuesForPeopleFace);
            return true;
        } else if (item.serverTag >= CloudUtils.getLongAttributeFromJson(jSONObject, "eTag")) {
            return false;
        } else {
            if (string3.equals("normal")) {
                setShouldFlatPeopleRelationshipInDB(contentValuesForPeopleFace, item);
                handleCustom(contentValuesForPeopleFace);
            } else if (string3.equals("deleted")) {
                handleDelete(contentValuesForPeopleFace);
            }
            return false;
        }
    }

    public final void setShouldFlatPeopleRelationshipInDB(ContentValues contentValues, PeopleFace peopleFace) {
        if (!this.mShouldFlatPeopleRelationshipInDB) {
            if ((contentValues.containsKey("groupId") ? contentValues.getAsString("groupId") : "").equalsIgnoreCase(peopleFace.groupId)) {
                return;
            }
            this.mShouldFlatPeopleRelationshipInDB = true;
        }
    }

    public void handleNewPeopleFace(ContentValues contentValues) {
        contentValues.put("localFlag", (Integer) 0);
        FaceDataManager.safeInsertFace(contentValues, true);
    }

    public final void handleCustom(ContentValues contentValues) throws JSONException {
        CloudUtils.updateToPeopleFaceDBForSync(FaceDataManager.PEOPLE_FACE_URI, contentValues, contentValues.getAsString("serverId"));
    }

    public final void handleDelete(ContentValues contentValues) throws JSONException {
        CloudUtils.updateToPeopleFaceDBForDeleteItem(FaceDataManager.PEOPLE_FACE_URI, contentValues, contentValues.getAsString("serverId"));
    }

    /* loaded from: classes.dex */
    public static class ErrorHandler {
        public static void cleanPeopleFaceTables() {
            for (String str : GalleryDBHelper.getPeopleFaceTables()) {
                Uri build = GalleryCloudUtils.BASE_URI.buildUpon().appendPath(str).build();
                Log2File.getInstance().flushLog("syncface", String.format("clean %s finished, deleted rows=%d", build, Integer.valueOf(GalleryUtils.safeDelete(build, null, null))), null);
            }
        }

        public static boolean simpleCheckWhetherNeedCleanTables(JSONObject jSONObject, Account account) throws JSONException {
            if (jSONObject == null) {
                return false;
            }
            if (!jSONObject.has("code")) {
                Log2File.getInstance().flushLog("syncface", "don't have json_tag_code", null);
                return false;
            }
            int i = jSONObject.getInt("code");
            Log2File log2File = Log2File.getInstance();
            log2File.flushLog("syncface", "JSON_TAG_CODE is:" + i, null);
            if (i != 52000) {
                return false;
            }
            cleanPeopleFaceTables();
            GalleryCloudSyncTagUtils.setFaceSyncWatermark(account, 0L);
            GalleryCloudSyncTagUtils.setFaceSyncToken(account, "");
            return true;
        }
    }
}
