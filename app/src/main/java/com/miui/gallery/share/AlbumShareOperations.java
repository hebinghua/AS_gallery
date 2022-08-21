package com.miui.gallery.share;

import android.accounts.Account;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Pair;
import ch.qos.logback.classic.spi.CallerData;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.miui.account.AccountHelper;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.CheckResult;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.cloud.GalleryMiCloudServerException;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.cloud.NetworkUtils;
import com.miui.gallery.cloud.RequestAlbumItem;
import com.miui.gallery.cloud.ServerErrorCode;
import com.miui.gallery.cloud.SyncOwnerAll;
import com.miui.gallery.cloud.SyncOwnerUserForAlbum;
import com.miui.gallery.cloud.SyncSharerUserForAlbum;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.cloud.base.GallerySyncResult;
import com.miui.gallery.cloud.base.SyncRequest;
import com.miui.gallery.cloud.base.SyncType;
import com.miui.gallery.cloud.operation.create.CreateGroupItem;
import com.miui.gallery.data.DBAlbum;
import com.miui.gallery.lib.MiuiGalleryUtils;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.share.AlbumShareUIManager;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.deviceprovider.ApplicationHelper;
import com.miui.gallery.util.face.FaceRegionRectF;
import com.xiaomi.mirror.synergy.CallMethod;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.a.j;
import com.xiaomi.video.VideoDecoder;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.keyczar.Keyczar;

/* loaded from: classes2.dex */
public class AlbumShareOperations {
    public static UserInfo sSelf;

    /* loaded from: classes2.dex */
    public static final class OutgoingInvitation {
        public final String mDescription;
        public final String mUrl;

        public OutgoingInvitation(String str, String str2) {
            this.mDescription = str;
            this.mUrl = str2;
        }

        public String toMessage() {
            return this.mDescription + this.mUrl;
        }

        public String getDescription() {
            return this.mDescription;
        }

        public String getUrl() {
            return this.mUrl;
        }

        public String toCache() {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("text", this.mDescription);
                jSONObject.put(MapBundleKey.MapObjKey.OBJ_URL, this.mUrl);
                return jSONObject.toString();
            } catch (JSONException unused) {
                return null;
            }
        }

        public static OutgoingInvitation fromCache(String str) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                return new OutgoingInvitation(jSONObject.getString("text"), jSONObject.getString(MapBundleKey.MapObjKey.OBJ_URL));
            } catch (JSONException unused) {
                return null;
            }
        }
    }

    /* loaded from: classes2.dex */
    public static final class IncomingInvitation {
        public String mAlbumName;
        public boolean mHasSharerInfo;
        public String mOwnerName;

        public IncomingInvitation(List<NameValuePair> list) {
            for (NameValuePair nameValuePair : list) {
                if ("albumName".equals(nameValuePair.getName())) {
                    this.mAlbumName = AlbumShareOperations.decode(nameValuePair.getValue());
                } else if ("userName".equals(nameValuePair.getName())) {
                    this.mOwnerName = AlbumShareOperations.decode(nameValuePair.getValue());
                } else if ("sharerInfo".equals(nameValuePair.getName())) {
                    this.mHasSharerInfo = TextUtils.equals("true", nameValuePair.getValue());
                }
                if (this.mAlbumName != null && this.mOwnerName != null && this.mHasSharerInfo) {
                    return;
                }
            }
        }

        public String getOwnerName() {
            return this.mOwnerName;
        }

        public String getAlbumName() {
            return this.mAlbumName;
        }

        public boolean hasSharerInfo() {
            return this.mHasSharerInfo;
        }
    }

    public static String decode(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        try {
            return new String(Base64.decode(str, 8), Keyczar.DEFAULT_ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    /* loaded from: classes2.dex */
    public static final class SharerInfo {
        public final String mAlbumNickName;
        public final FaceRegionRectF mFaceRelativePos;
        public final String mOwnerRelationText;
        public final String mSharerRelationText;
        public final String mThumbnailUrl;

        public SharerInfo(String str) throws JSONException {
            JSONObject jSONObject = new JSONObject(str);
            this.mAlbumNickName = jSONObject.getString("albumNickName");
            this.mOwnerRelationText = jSONObject.getString("ownerRelationText");
            this.mSharerRelationText = jSONObject.getString("sharerRelationText");
            this.mThumbnailUrl = jSONObject.optString("thumbnail");
            JSONObject optJSONObject = jSONObject.optJSONObject("thumbnailFaceInfo");
            if (optJSONObject != null) {
                float f = (float) optJSONObject.getDouble("faceXScale");
                float f2 = (float) optJSONObject.getDouble("faceYScale");
                this.mFaceRelativePos = new FaceRegionRectF(f, f2, f + ((float) optJSONObject.getDouble("faceWScale")), f2 + ((float) optJSONObject.getDouble("faceHScale")), 0);
                return;
            }
            this.mFaceRelativePos = null;
        }
    }

    public static AsyncResult<String> requestUrlForBarcode(final String str) {
        return execute(new Callable<AsyncResult<String>>() { // from class: com.miui.gallery.share.AlbumShareOperations.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public AsyncResult<String> mo1346call() throws Exception {
                String readCache = AlbumShareOperations.readCache(str, "barcodeData", "barcodeDataDeadline", 43200000);
                if (!TextUtils.isEmpty(readCache)) {
                    return AsyncResult.create(0, readCache);
                }
                if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
                    return AsyncResult.create(-11);
                }
                Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
                Account xiaomiAccount = AccountHelper.getXiaomiAccount(sGetAndroidContext);
                if (xiaomiAccount == null) {
                    return AsyncResult.create(-4);
                }
                GalleryExtendedAuthToken extToken = CloudUtils.getExtToken(sGetAndroidContext, xiaomiAccount);
                if (extToken == null) {
                    return AsyncResult.create(-3);
                }
                String myName = AlbumShareOperations.myName();
                if (TextUtils.isEmpty(myName)) {
                    return AsyncResult.create(-5);
                }
                String barcodeShareUrl = HostManager.AlbumShareOperation.getBarcodeShareUrl(str);
                ArrayList newArrayList = Lists.newArrayList();
                AlbumShareOperations.addNameValuePair(newArrayList, "userName", myName, extToken);
                JSONObject postToXiaomi = CloudUtils.postToXiaomi(barcodeShareUrl, newArrayList, null, xiaomiAccount, extToken, 0, false);
                int parseErrorCode = CheckResult.parseErrorCode(postToXiaomi);
                if (parseErrorCode != 0) {
                    return AsyncResult.create(parseErrorCode);
                }
                String string = postToXiaomi.getJSONObject("data").getString(MapBundleKey.MapObjKey.OBJ_URL);
                if (!TextUtils.isEmpty(string)) {
                    AlbumShareOperations.persisitCache(str, "barcodeData", "barcodeDataDeadline", string, 43200000);
                }
                SyncLogger.d("AlbumShareOperations", "request barcode url success.");
                return AsyncResult.create(0, string);
            }
        });
    }

    public static AsyncResult<String> changePublicStatus(final String str, final boolean z) {
        return execute(new Callable<AsyncResult<String>>() { // from class: com.miui.gallery.share.AlbumShareOperations.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public AsyncResult<String> mo1353call() throws Exception {
                if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
                    return AsyncResult.create(-11);
                }
                Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
                Account xiaomiAccount = AccountHelper.getXiaomiAccount(sGetAndroidContext);
                if (xiaomiAccount == null) {
                    return AsyncResult.create(-4);
                }
                GalleryExtendedAuthToken extToken = CloudUtils.getExtToken(sGetAndroidContext, xiaomiAccount);
                if (extToken == null) {
                    return AsyncResult.create(-3);
                }
                String changePublicUrl = HostManager.AlbumShareOperation.getChangePublicUrl(str);
                ArrayList newArrayList = Lists.newArrayList();
                AlbumShareOperations.addNameValuePair(newArrayList, "isPublic", String.valueOf(z), extToken);
                JSONObject postToXiaomi = CloudUtils.postToXiaomi(changePublicUrl, newArrayList, null, xiaomiAccount, extToken, 0, false);
                int parseErrorCode = CheckResult.parseErrorCode(postToXiaomi);
                if (parseErrorCode != 0) {
                    return AsyncResult.create(parseErrorCode);
                }
                String optString = postToXiaomi.getJSONObject("data").optString(MapBundleKey.MapObjKey.OBJ_URL);
                AlbumShareOperations.savePublicInfo(str, z, optString, false);
                return AsyncResult.create(0, optString);
            }
        });
    }

    public static AsyncResult<String> requestPublicUrl(final String str, final boolean z) {
        return execute(new Callable<AsyncResult<String>>() { // from class: com.miui.gallery.share.AlbumShareOperations.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public AsyncResult<String> mo1354call() throws Exception {
                if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
                    return AsyncResult.create(-11);
                }
                Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
                Account xiaomiAccount = AccountHelper.getXiaomiAccount(sGetAndroidContext);
                if (xiaomiAccount == null) {
                    return AsyncResult.create(-4);
                }
                GalleryExtendedAuthToken extToken = CloudUtils.getExtToken(sGetAndroidContext, xiaomiAccount);
                if (extToken == null) {
                    return AsyncResult.create(-3);
                }
                String sharerRequestPublicUrl = z ? HostManager.AlbumShareOperation.getSharerRequestPublicUrl() : HostManager.AlbumShareOperation.getOwnerRequestPublicUrl(str);
                ArrayList arrayList = new ArrayList();
                if (z) {
                    arrayList.add(new BasicNameValuePair("sharedAlbumId", str));
                }
                JSONObject fromXiaomi = CloudUtils.getFromXiaomi(sharerRequestPublicUrl, arrayList, xiaomiAccount, extToken, 0, false);
                int parseErrorCode = CheckResult.parseErrorCode(fromXiaomi);
                if (parseErrorCode != 0) {
                    return AsyncResult.create(parseErrorCode);
                }
                String optString = fromXiaomi.getJSONObject("data").optString(MapBundleKey.MapObjKey.OBJ_URL);
                AlbumShareOperations.savePublicInfo(str, !TextUtils.isEmpty(optString), optString, z);
                return AsyncResult.create(0, optString);
            }
        });
    }

    public static void savePublicInfo(String str, boolean z, String str2, boolean z2) {
        Uri uri;
        ContentValues contentValues = new ContentValues();
        contentValues.put("isPublic", Boolean.valueOf(z));
        contentValues.put("publicUrl", str2);
        if (z2) {
            uri = GalleryCloudUtils.SHARE_ALBUM_URI;
        } else {
            uri = GalleryCloudUtils.ALBUM_URI;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(z2 ? "albumId" : "serverId");
        sb.append(" = ? ");
        GalleryUtils.safeUpdate(uri, contentValues, sb.toString(), new String[]{str});
    }

    public static AsyncResult<OutgoingInvitation> requestInvitationForSms(final String str, final String str2, final String str3, final String str4, final String str5) {
        return execute(new Callable<AsyncResult<OutgoingInvitation>>() { // from class: com.miui.gallery.share.AlbumShareOperations.5
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public AsyncResult<OutgoingInvitation> mo1355call() throws Exception {
                OutgoingInvitation fromCache;
                String readCache = AlbumShareOperations.readCache(str, "smsShareData", "smsShareDataDeadline", 43200000);
                if (!TextUtils.isEmpty(readCache) && (fromCache = OutgoingInvitation.fromCache(readCache)) != null) {
                    return AsyncResult.create(0, fromCache);
                }
                if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
                    return AsyncResult.create(-11);
                }
                Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
                Account xiaomiAccount = AccountHelper.getXiaomiAccount(sGetAndroidContext);
                if (xiaomiAccount == null) {
                    return AsyncResult.create(-4);
                }
                GalleryExtendedAuthToken extToken = CloudUtils.getExtToken(sGetAndroidContext, xiaomiAccount);
                if (extToken == null) {
                    return AsyncResult.create(-3);
                }
                String myName = AlbumShareOperations.myName();
                if (TextUtils.isEmpty(myName)) {
                    return AsyncResult.create(-5);
                }
                String smsShareUrl = HostManager.AlbumShareOperation.getSmsShareUrl(str);
                ArrayList newArrayList = Lists.newArrayList();
                AlbumShareOperations.addNameValuePair(newArrayList, "userName", myName, extToken);
                if (!TextUtils.isEmpty(str2)) {
                    try {
                        JSONObject jSONObject = new JSONObject();
                        jSONObject.put("ownerRelation", str4);
                        jSONObject.put("ownerRelationText", str5);
                        jSONObject.put("sharerRelation", str2);
                        jSONObject.put("sharerRelationText", str3);
                        AlbumShareOperations.addNameValuePair(newArrayList, "relationShip", jSONObject.toString(), extToken);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return AsyncResult.create(-7);
                    }
                }
                JSONObject postToXiaomi = CloudUtils.postToXiaomi(smsShareUrl, newArrayList, null, xiaomiAccount, extToken, 0, false);
                int parseErrorCode = CheckResult.parseErrorCode(postToXiaomi);
                if (parseErrorCode != 0) {
                    return AsyncResult.create(parseErrorCode);
                }
                JSONObject jSONObject2 = postToXiaomi.getJSONObject("data");
                OutgoingInvitation outgoingInvitation = new OutgoingInvitation(jSONObject2.getString("text"), jSONObject2.getString(MapBundleKey.MapObjKey.OBJ_URL));
                if (TextUtils.isEmpty(str2)) {
                    AlbumShareOperations.persisitCache(str, "smsShareData", "smsShareDataDeadline", outgoingInvitation.toCache(), 43200000);
                }
                SyncLogger.d("AlbumShareOperations", "request sms url success.");
                return AsyncResult.create(0, outgoingInvitation);
            }
        });
    }

    public static AsyncResult<Void> exitAlbumShare(final String str, final String str2) {
        return execute(new Callable<AsyncResult<Void>>() { // from class: com.miui.gallery.share.AlbumShareOperations.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public AsyncResult<Void> mo1356call() throws Exception {
                if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
                    return AsyncResult.create(-11);
                }
                Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
                Account xiaomiAccount = AccountHelper.getXiaomiAccount(sGetAndroidContext);
                if (xiaomiAccount == null) {
                    return AsyncResult.create(-4);
                }
                GalleryExtendedAuthToken extToken = CloudUtils.getExtToken(sGetAndroidContext, xiaomiAccount);
                if (extToken == null) {
                    return AsyncResult.create(-3);
                }
                String exitShareUrl = HostManager.AlbumShareOperation.getExitShareUrl();
                ArrayList arrayList = new ArrayList();
                arrayList.add(new BasicNameValuePair("sharedAlbumId", str));
                int parseErrorCode = CheckResult.parseErrorCode(CloudUtils.postToXiaomi(exitShareUrl, arrayList, null, xiaomiAccount, extToken, 0, false));
                if (parseErrorCode == 0) {
                    CloudUtils.deleteShareAlbumInLocal(str, str2);
                }
                SyncLogger.d("AlbumShareOperations", "exit share album success.");
                return AsyncResult.create(parseErrorCode);
            }
        });
    }

    public static Pair<List<String>, List<String>> doDeleteSharers(ContentResolver contentResolver, String str, List<String> list, Account account, GalleryExtendedAuthToken galleryExtendedAuthToken) throws IllegalBlockSizeException, BadPaddingException, JSONException, IOException, GalleryMiCloudServerException {
        String deleteSharerUrl = HostManager.AlbumShareOperation.getDeleteSharerUrl(str);
        ArrayList newArrayList = Lists.newArrayList();
        addNameValuePair(newArrayList, "sharerIdList", GalleryUtils.concatAll(list, ","), galleryExtendedAuthToken);
        JSONObject postToXiaomi = CloudUtils.postToXiaomi(deleteSharerUrl, newArrayList, null, account, galleryExtendedAuthToken, 0, false);
        if (CheckResult.parseErrorCode(postToXiaomi) == 0) {
            JSONObject jSONObject = postToXiaomi.getJSONObject("data");
            JSONArray jSONArray = jSONObject.getJSONArray("succ");
            JSONArray jSONArray2 = jSONObject.getJSONArray("fail");
            ArrayList newArrayList2 = Lists.newArrayList();
            ArrayList newArrayList3 = Lists.newArrayList();
            for (int i = 0; i < jSONArray.length(); i++) {
                newArrayList2.add(jSONArray.getString(i));
            }
            for (int i2 = 0; i2 < jSONArray2.length(); i2++) {
                newArrayList3.add(jSONArray2.getString(i2));
            }
            if (!newArrayList2.isEmpty()) {
                SyncLogger.d("AlbumShareOperations", "Local delete count=" + contentResolver.delete(GalleryCloudUtils.CLOUD_USER_URI, "userId in (?) AND albumId = ?", new String[]{GalleryUtils.concatAll(newArrayList2, ","), str}) + ", Server delete count=" + newArrayList2.size());
            }
            return Pair.create(newArrayList2, newArrayList3);
        }
        return null;
    }

    public static AsyncResult<Pair<List<String>, List<String>>> deleteSharers(final ContentResolver contentResolver, final String str, final List<String> list) {
        return execute(new Callable<AsyncResult<Pair<List<String>, List<String>>>>() { // from class: com.miui.gallery.share.AlbumShareOperations.7
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public AsyncResult<Pair<List<String>, List<String>>> mo1357call() throws Exception {
                if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
                    return AsyncResult.create(-11);
                }
                Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
                Account xiaomiAccount = AccountHelper.getXiaomiAccount(sGetAndroidContext);
                if (xiaomiAccount == null) {
                    return AsyncResult.create(-4);
                }
                GalleryExtendedAuthToken extToken = CloudUtils.getExtToken(sGetAndroidContext, xiaomiAccount);
                if (extToken == null) {
                    return AsyncResult.create(-3);
                }
                ArrayList newArrayList = Lists.newArrayList();
                ArrayList newArrayList2 = Lists.newArrayList();
                int i = 0;
                while (i < list.size()) {
                    int i2 = i + 50;
                    Pair doDeleteSharers = AlbumShareOperations.doDeleteSharers(contentResolver, str, list.subList(i, Math.min(list.size(), i2)), xiaomiAccount, extToken);
                    if (doDeleteSharers != null) {
                        newArrayList.addAll((Collection) doDeleteSharers.first);
                        newArrayList2.addAll((Collection) doDeleteSharers.second);
                    }
                    i = i2;
                }
                return AsyncResult.create(0, Pair.create(newArrayList, newArrayList2));
            }
        });
    }

    public static AsyncResult<String> updateInvitation(final ContentResolver contentResolver, final String str, final String str2, final CloudSharerMediaSet cloudSharerMediaSet) {
        return execute(new Callable<AsyncResult<String>>() { // from class: com.miui.gallery.share.AlbumShareOperations.8
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public AsyncResult<String> mo1358call() throws Exception {
                String parseRedirectLocation;
                AsyncResult<String> create;
                if (!MiuiGalleryUtils.isAlbumShareUrl(str) || !ApplicationHelper.supportShare()) {
                    return AsyncResult.create(VideoDecoder.DecodeCallback.ERROR_START);
                }
                if (TextUtils.isEmpty(str2)) {
                    parseRedirectLocation = AlbumShareOperations.parseRedirectLocation(AlbumShareOperations.requestHeaders(str));
                } else {
                    parseRedirectLocation = str2;
                }
                if (TextUtils.isEmpty(parseRedirectLocation)) {
                    return AsyncResult.create(-7);
                }
                ContentValues contentValues = new ContentValues();
                contentValues.put("shareUrlLong", parseRedirectLocation);
                IncomingInvitation parseInvitation = AlbumShareOperations.parseInvitation(parseRedirectLocation);
                if (parseInvitation != null && parseInvitation.hasSharerInfo()) {
                    AsyncResult<String> requestSharerInfo = AlbumShareOperations.requestSharerInfo(contentResolver, str, parseRedirectLocation);
                    if (requestSharerInfo.mError != 0) {
                        return requestSharerInfo;
                    }
                    contentValues.put("sharerInfo", requestSharerInfo.mData);
                    create = AsyncResult.create(requestSharerInfo.mError, parseRedirectLocation);
                } else {
                    create = AsyncResult.create(0, parseRedirectLocation);
                }
                if (parseInvitation != null && !TextUtils.isEmpty(parseInvitation.getAlbumName())) {
                    contentValues.put("fileName", parseInvitation.getAlbumName());
                }
                AlbumShareOperations.updateShareAlbumInfos(contentResolver, str, contentValues, cloudSharerMediaSet);
                return create;
            }
        });
    }

    public static void updateShareAlbumInfos(ContentResolver contentResolver, String str, ContentValues contentValues, CloudSharerMediaSet cloudSharerMediaSet) {
        cloudSharerMediaSet.setLongUrl(contentValues.getAsString("shareUrlLong"));
        cloudSharerMediaSet.setSharerInfo(contentValues.getAsString("sharerInfo"));
        contentResolver.update(GalleryCloudUtils.SHARE_ALBUM_URI, contentValues, String.format("%s=? AND %s=?", "shareUrl", "albumStatus"), new String[]{str, "invited"});
    }

    public static AsyncResult<String> requestSharerInfo(ContentResolver contentResolver, String str, String str2) throws Exception {
        if (TextUtils.isEmpty(str2)) {
            return AsyncResult.create(-7);
        }
        String httpGetRequestForString = NetworkUtils.httpGetRequestForString(String.format("%s&%s=%s", str2, "ability", "baby"), null);
        if (!TextUtils.isEmpty(httpGetRequestForString)) {
            JSONObject jSONObject = new JSONObject(httpGetRequestForString);
            int parseErrorCode = CheckResult.parseErrorCode(jSONObject);
            if (parseErrorCode == 0) {
                String string = jSONObject.getString("data");
                if (!TextUtils.isEmpty(string)) {
                    return AsyncResult.create(0, string);
                }
                return AsyncResult.create(-7);
            }
            deleteInvitationIfInvalid(contentResolver, parseErrorCode, str);
            return AsyncResult.create(parseErrorCode);
        }
        return AsyncResult.create(-7);
    }

    public static ArrayList<NameValuePair> requestInvitationDetail(ContentResolver contentResolver, String str, CloudSharerMediaSet cloudSharerMediaSet) {
        Cursor query;
        AsyncResult<String> updateInvitation;
        if (!TextUtils.isEmpty(str) && (query = contentResolver.query(CloudUtils.getLimitUri(GalleryCloudUtils.SHARE_ALBUM_URI, 1), new String[]{"shareUrlLong", "sharerInfo", "shareUrl"}, "shareUrl=?", new String[]{str}, null)) != null) {
            try {
                if (!query.moveToFirst()) {
                    return null;
                }
                String string = query.getString(0);
                String string2 = query.getString(1);
                if ((TextUtils.isEmpty(string) || TextUtils.isEmpty(string2)) && (updateInvitation = updateInvitation(contentResolver, str, string2, cloudSharerMediaSet)) != null && updateInvitation.mError == 0) {
                    string = updateInvitation.mData;
                }
                return parseParamsFromUrl(string);
            } finally {
                query.close();
            }
        }
        return null;
    }

    public static Map<String, List<String>> requestHeaders(String str) throws MalformedURLException, IOException {
        HttpURLConnection httpURLConnection = null;
        try {
            HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL(str).openConnection();
            try {
                httpURLConnection2.setInstanceFollowRedirects(false);
                httpURLConnection2.setConnectTimeout(30000);
                httpURLConnection2.setReadTimeout(30000);
                httpURLConnection2.setUseCaches(false);
                httpURLConnection2.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpURLConnection2.setRequestProperty("User-Agent", ApplicationHelper.getMiCloudProvider().getCloudManager().getUserAgent());
                httpURLConnection2.setDoInput(true);
                httpURLConnection2.setRequestMethod("GET");
                httpURLConnection2.connect();
                int responseCode = httpURLConnection2.getResponseCode();
                SamplingStatHelper.trackHttpEvent(str, responseCode);
                if (responseCode == 301 || responseCode == 302) {
                    Map<String, List<String>> headerFields = httpURLConnection2.getHeaderFields();
                    httpURLConnection2.disconnect();
                    return headerFields;
                }
                httpURLConnection2.disconnect();
                return null;
            } catch (Throwable th) {
                th = th;
                httpURLConnection = httpURLConnection2;
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
        }
    }

    public static String parseRedirectLocation(Map<String, List<String>> map) {
        List<String> list;
        if (map == null || (list = map.get("Location")) == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    public static ArrayList<NameValuePair> parseParamsFromUrl(String str) {
        String[] split;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        ArrayList<NameValuePair> newArrayList = Lists.newArrayList();
        int indexOf = str.indexOf(CallerData.NA);
        if (indexOf >= 0) {
            for (String str2 : str.substring(indexOf + 1).split("&")) {
                int indexOf2 = str2.indexOf("=");
                if (indexOf2 > 0) {
                    newArrayList.add(new BasicNameValuePair(str2.substring(0, indexOf2), str2.substring(indexOf2 + 1)));
                }
            }
        }
        return newArrayList;
    }

    public static IncomingInvitation parseInvitation(String str) {
        ArrayList<NameValuePair> parseParamsFromUrl = parseParamsFromUrl(str);
        if (parseParamsFromUrl != null) {
            return new IncomingInvitation(parseParamsFromUrl);
        }
        return null;
    }

    public static SharerInfo parseSharerInfo(String str) {
        try {
            return new SharerInfo(str);
        } catch (JSONException e) {
            SyncLogger.e("AlbumShareOperations", "parseSharerInfo error.", e);
            return null;
        }
    }

    public static AsyncResult<Long> acceptInvitation(final ContentResolver contentResolver, final String str, final CloudSharerMediaSet cloudSharerMediaSet) {
        return execute(new Callable<AsyncResult<Long>>() { // from class: com.miui.gallery.share.AlbumShareOperations.9
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public AsyncResult<Long> mo1359call() throws Exception {
                if (BaseGalleryPreferences.CTA.canConnectNetwork()) {
                    ArrayList requestInvitationDetail = AlbumShareOperations.requestInvitationDetail(contentResolver, str, cloudSharerMediaSet);
                    if (requestInvitationDetail == null) {
                        return AsyncResult.create(VideoDecoder.DecodeCallback.ERROR_DEQUEUE_INPUT_BUFFER);
                    }
                    Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
                    Account xiaomiAccount = AccountHelper.getXiaomiAccount(sGetAndroidContext);
                    if (xiaomiAccount == null) {
                        return AsyncResult.create(-4);
                    }
                    GalleryExtendedAuthToken extToken = CloudUtils.getExtToken(sGetAndroidContext, xiaomiAccount);
                    if (extToken == null) {
                        return AsyncResult.create(-3);
                    }
                    String acceptInvitationUrl = HostManager.AlbumShareOperation.getAcceptInvitationUrl();
                    ArrayList newArrayList = Lists.newArrayList();
                    Iterator it = requestInvitationDetail.iterator();
                    while (it.hasNext()) {
                        NameValuePair nameValuePair = (NameValuePair) it.next();
                        AlbumShareOperations.addNameValuePair(newArrayList, nameValuePair.getName(), nameValuePair.getValue(), extToken);
                    }
                    JSONObject postToXiaomi = CloudUtils.postToXiaomi(acceptInvitationUrl, newArrayList, null, xiaomiAccount, extToken, 0, false);
                    int parseErrorCode = CheckResult.parseErrorCode(postToXiaomi);
                    if (parseErrorCode != 0) {
                        AlbumShareOperations.deleteInvitationIfInvalid(contentResolver, parseErrorCode, str);
                        return AsyncResult.create(parseErrorCode);
                    }
                    JSONObject jSONObject = postToXiaomi.getJSONObject("data");
                    String string = jSONObject.getString("shareId");
                    String string2 = jSONObject.getString("creatorId");
                    int i = -2;
                    long j = 0;
                    if (!TextUtils.isEmpty(string)) {
                        Uri uri = GalleryCloudUtils.SHARE_ALBUM_URI;
                        synchronized (uri) {
                            long itemId = CloudUtils.getItemId(uri, "albumId", string);
                            if (itemId == 0) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("albumStatus", "normal");
                                contentValues.put("albumId", string);
                                contentValues.put("creatorId", string2);
                                contentResolver.update(uri, contentValues, String.format("%s=? AND %s=?", "shareUrl", "albumStatus"), new String[]{str, "invited"});
                                itemId = CloudUtils.getItemId(uri, "albumId", string);
                                i = 0;
                            } else {
                                ContentValues contentValues2 = new ContentValues();
                                contentValues2.put("sharerInfo", cloudSharerMediaSet.getSharerInfo());
                                contentResolver.update(uri, contentValues2, String.format("%s=?", "albumId"), new String[]{string});
                                contentResolver.delete(uri, String.format("%s=? AND %s=?", "shareUrl", "albumStatus"), new String[]{str, "invited"});
                                i = -10;
                            }
                            j = itemId;
                        }
                    }
                    AlbumShareUIManager.syncUserListForAlbumAsync(string, true, new AlbumShareUIManager.OnCompletionListener<Void, Void>() { // from class: com.miui.gallery.share.AlbumShareOperations.9.1
                        @Override // com.miui.gallery.share.AlbumShareUIManager.OnCompletionListener
                        public void onCompletion(Void r1, Void r2, int i2, boolean z) {
                            AlbumShareUIManager.syncAllUserInfoFromNetworkAsync(null);
                        }
                    });
                    return AsyncResult.create(i, Long.valueOf(j));
                }
                return AsyncResult.create(-11);
            }
        });
    }

    public static void deleteInvitationIfInvalid(ContentResolver contentResolver, int i, String str) {
        if (i == 50019 || i == 50025 || i == 50030 || i == 50012) {
            contentResolver.delete(GalleryCloudUtils.SHARE_ALBUM_URI, String.format("%s=? AND %s=?", "shareUrl", "albumStatus"), new String[]{str, "invited"});
        }
    }

    public static AsyncResult<Void> denyInvitation(final ContentResolver contentResolver, final String str, final CloudSharerMediaSet cloudSharerMediaSet) {
        return execute(new Callable<AsyncResult<Void>>() { // from class: com.miui.gallery.share.AlbumShareOperations.10
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public AsyncResult<Void> mo1347call() throws Exception {
                if (BaseGalleryPreferences.CTA.canConnectNetwork()) {
                    ArrayList requestInvitationDetail = AlbumShareOperations.requestInvitationDetail(contentResolver, str, cloudSharerMediaSet);
                    if (requestInvitationDetail == null) {
                        return AsyncResult.create(VideoDecoder.DecodeCallback.ERROR_DEQUEUE_INPUT_BUFFER);
                    }
                    String refuseInvitationUrl = HostManager.AlbumShareOperation.getRefuseInvitationUrl();
                    ArrayList newArrayList = Lists.newArrayList();
                    Iterator it = requestInvitationDetail.iterator();
                    while (it.hasNext()) {
                        NameValuePair nameValuePair = (NameValuePair) it.next();
                        AlbumShareOperations.addNameValuePair(newArrayList, nameValuePair.getName(), nameValuePair.getValue(), null);
                    }
                    int parseErrorCode = CheckResult.parseErrorCode(CloudUtils.postToXiaomi(refuseInvitationUrl, newArrayList, null, null, null, 0, false));
                    if (ServerErrorCode.isClientError(parseErrorCode)) {
                        return AsyncResult.create(parseErrorCode);
                    }
                    contentResolver.delete(GalleryCloudUtils.SHARE_ALBUM_URI, String.format("%s=? AND %s=?", "shareUrl", "albumStatus"), new String[]{str, "invited"});
                    return AsyncResult.create(parseErrorCode);
                }
                return AsyncResult.create(-11);
            }
        });
    }

    public static List<UserInfo> doRequestUserInfo(List<String> list) throws BadPaddingException, IllegalBlockSizeException, IOException, GalleryMiCloudServerException, JSONException {
        Context sGetAndroidContext;
        Account xiaomiAccount;
        GalleryExtendedAuthToken extToken;
        ArrayList newArrayList = Lists.newArrayList();
        if (!BaseGalleryPreferences.CTA.canConnectNetwork() || (xiaomiAccount = AccountHelper.getXiaomiAccount((sGetAndroidContext = GalleryApp.sGetAndroidContext()))) == null || (extToken = CloudUtils.getExtToken(sGetAndroidContext, xiaomiAccount)) == null) {
            return newArrayList;
        }
        String requestUserInfoUrl = HostManager.AlbumShareOperation.getRequestUserInfoUrl();
        ArrayList arrayList = new ArrayList();
        arrayList.add(new BasicNameValuePair("users", GalleryUtils.concatAll(list, ",")));
        JSONObject postToXiaomi = CloudUtils.postToXiaomi(requestUserInfoUrl, arrayList, null, xiaomiAccount, extToken, 0, false);
        if (CheckResult.parseErrorCode(postToXiaomi) == 0) {
            JSONArray jSONArray = postToXiaomi.getJSONObject("data").getJSONArray("list");
            for (int i = 0; i < jSONArray.length(); i++) {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                String string = jSONObject.getString("userId");
                if (!TextUtils.isEmpty(string)) {
                    UserInfo userInfo = new UserInfo(string);
                    userInfo.setAliasNick(jSONObject.optString("aliasNick"));
                    userInfo.setMiliaoNick(jSONObject.optString("nickname"));
                    userInfo.setMiliaoIconUrl(jSONObject.optString(CallMethod.RESULT_ICON));
                    newArrayList.add(userInfo);
                }
            }
        }
        return newArrayList;
    }

    public static AsyncResult<List<UserInfo>> requestUserInfo(final List<String> list) {
        return execute(new Callable<AsyncResult<List<UserInfo>>>() { // from class: com.miui.gallery.share.AlbumShareOperations.11
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public AsyncResult<List<UserInfo>> mo1348call() throws Exception {
                ArrayList newArrayList = Lists.newArrayList();
                int i = 0;
                while (i < list.size()) {
                    int i2 = i + 20;
                    List doRequestUserInfo = AlbumShareOperations.doRequestUserInfo(list.subList(i, Math.min(list.size(), i2)));
                    if (doRequestUserInfo != null) {
                        newArrayList.addAll(doRequestUserInfo);
                    }
                    i = i2;
                }
                return AsyncResult.create(0, newArrayList);
            }
        });
    }

    public static AsyncResult<Void> syncAllUserInfoFromNetwork() {
        return execute(new Callable<AsyncResult<Void>>() { // from class: com.miui.gallery.share.AlbumShareOperations.12
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public AsyncResult<Void> mo1349call() throws Exception {
                List<UserInfo> list;
                ContentResolver contentResolver = GalleryApp.sGetAndroidContext().getContentResolver();
                HashSet newHashSet = Sets.newHashSet();
                AlbumShareOperations.collectAllUserId(contentResolver, GalleryCloudUtils.CLOUD_USER_URI, "userId", newHashSet);
                AlbumShareOperations.collectAllUserId(contentResolver, GalleryCloudUtils.SHARE_USER_URI, "userId", newHashSet);
                String accountName = GalleryCloudUtils.getAccountName();
                if (!TextUtils.isEmpty(accountName)) {
                    newHashSet.add(accountName);
                }
                Uri uri = GalleryCloudUtils.USER_INFO_URI;
                synchronized (uri) {
                    GalleryUtils.safeDelete(uri, String.format("%s NOT IN (%s)", "user_id", GalleryUtils.concatAll(newHashSet, ",")), null);
                }
                long currentTimeMillis = System.currentTimeMillis();
                Cursor query = contentResolver.query(uri, new String[]{"user_id"}, String.format("(%s<?)AND(%s>?)", "date_modified", "date_modified"), new String[]{String.valueOf(currentTimeMillis), String.valueOf(currentTimeMillis - 86400000)}, null);
                if (query != null) {
                    while (query.moveToNext()) {
                        try {
                            newHashSet.remove(String.valueOf(query.getString(0)));
                        } finally {
                            query.close();
                        }
                    }
                }
                if (!newHashSet.isEmpty()) {
                    ArrayList newArrayList = Lists.newArrayList();
                    newArrayList.addAll(newHashSet);
                    AsyncResult<List<UserInfo>> requestUserInfo = AlbumShareOperations.requestUserInfo(newArrayList);
                    if (requestUserInfo != null && requestUserInfo.mError == 0 && (list = requestUserInfo.mData) != null && !list.isEmpty()) {
                        String concatAll = GalleryUtils.concatAll(list, ",", new GalleryUtils.ConcatConverter<UserInfo>() { // from class: com.miui.gallery.share.AlbumShareOperations.12.1
                            @Override // com.miui.gallery.util.GalleryUtils.ConcatConverter
                            public String convertToString(UserInfo userInfo) {
                                return userInfo.getUserId();
                            }
                        });
                        Uri uri2 = GalleryCloudUtils.USER_INFO_URI;
                        synchronized (uri2) {
                            int safeDelete = GalleryUtils.safeDelete(uri2, String.format("%s IN (%s)", "user_id", concatAll), null);
                            AlbumShareOperations.insertUserInfoToDB(contentResolver, uri2, list);
                            SyncLogger.d("AlbumShareOperations", String.format("syncAllUserNameFromNetwork: delete=%d, insert=%d", Integer.valueOf(safeDelete), Integer.valueOf(list.size())));
                        }
                    }
                }
                return AsyncResult.create(0);
            }
        });
    }

    public static void insertUserInfoToDB(ContentResolver contentResolver, Uri uri, List<UserInfo> list) {
        ArrayList<ContentProviderOperation> newArrayList = Lists.newArrayList();
        for (UserInfo userInfo : list) {
            newArrayList.add(ContentProviderOperation.newInsert(GalleryCloudUtils.USER_INFO_URI).withValue("user_id", userInfo.getUserId()).withValue("alias_nick", userInfo.getAliasNick()).withValue("miliao_nick", userInfo.getMiliaoNick()).withValue("miliao_icon_url", userInfo.getMiliaoIconUrl()).build());
            if (newArrayList.size() > 100) {
                try {
                    try {
                        contentResolver.applyBatch("com.miui.gallery.cloud.provider", newArrayList);
                    } catch (OperationApplicationException e) {
                        e.printStackTrace();
                    } catch (RemoteException e2) {
                        e2.printStackTrace();
                    }
                } finally {
                    newArrayList.clear();
                }
            }
        }
        if (!newArrayList.isEmpty()) {
            try {
                contentResolver.applyBatch("com.miui.gallery.cloud.provider", newArrayList);
            } catch (OperationApplicationException e3) {
                e3.printStackTrace();
            } catch (RemoteException e4) {
                e4.printStackTrace();
            }
        }
    }

    public static void collectAllUserId(ContentResolver contentResolver, Uri uri, String str, Collection<String> collection) {
        Cursor query = contentResolver.query(CloudUtils.getCloudDistinctUri(uri), new String[]{str}, null, null, null);
        if (query == null) {
            return;
        }
        while (query.moveToNext()) {
            try {
                collection.add(String.valueOf(query.getLong(0)));
            } finally {
                query.close();
            }
        }
    }

    public static AsyncResult<Void> syncUserListForAlbum(final String str, final boolean z) {
        return execute(new Callable<AsyncResult<Void>>() { // from class: com.miui.gallery.share.AlbumShareOperations.13
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public AsyncResult<Void> mo1350call() throws Exception {
                if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
                    return AsyncResult.create(-11);
                }
                Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
                Account xiaomiAccount = AccountHelper.getXiaomiAccount(sGetAndroidContext);
                if (xiaomiAccount == null) {
                    return AsyncResult.create(-4);
                }
                GalleryExtendedAuthToken extToken = CloudUtils.getExtToken(sGetAndroidContext, xiaomiAccount);
                if (extToken == null) {
                    return AsyncResult.create(-3);
                }
                if (!BaseNetworkUtils.isNetworkConnected()) {
                    return AsyncResult.create(-6);
                }
                if (z) {
                    new SyncSharerUserForAlbum(sGetAndroidContext, xiaomiAccount, extToken, str).sync();
                } else {
                    new SyncOwnerUserForAlbum(sGetAndroidContext, xiaomiAccount, extToken, str).sync();
                }
                AlbumShareOperations.syncAllUserInfoFromNetwork();
                return AsyncResult.create(0);
            }
        });
    }

    public static long addInvitation(String str) {
        Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
        Uri uri = GalleryCloudUtils.SHARE_ALBUM_URI;
        synchronized (uri) {
            long itemId = CloudUtils.getItemId(uri, "shareUrl", str);
            if (itemId != 0) {
                return itemId;
            }
            ContentValues contentValues = new ContentValues(2);
            contentValues.put("shareUrl", str);
            contentValues.put("albumStatus", "invited");
            contentValues.put("fileName", "");
            Uri insert = sGetAndroidContext.getContentResolver().insert(uri, contentValues);
            if (insert == null) {
                SyncLogger.e("AlbumShareOperations", "Insert invitation error.");
                return 0L;
            }
            return Long.valueOf(insert.getLastPathSegment()).longValue();
        }
    }

    public static AsyncResult<String> tryToCreateCloudAlbum(final String str) {
        return execute(new Callable<AsyncResult<String>>() { // from class: com.miui.gallery.share.AlbumShareOperations.14
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public AsyncResult<String> mo1351call() throws Exception {
                if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
                    return AsyncResult.create(-11);
                }
                Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
                DBAlbum albumById = AlbumDataHelper.getAlbumById(sGetAndroidContext, str, null);
                if (albumById == null) {
                    return AsyncResult.create(-2);
                }
                String serverId = albumById.getServerId();
                if (CloudUtils.isValidAlbumId(serverId)) {
                    return AsyncResult.create(0, serverId);
                }
                Account xiaomiAccount = AccountHelper.getXiaomiAccount(sGetAndroidContext);
                if (xiaomiAccount == null) {
                    return AsyncResult.create(-4);
                }
                GalleryExtendedAuthToken extToken = CloudUtils.getExtToken(sGetAndroidContext, xiaomiAccount);
                if (extToken == null) {
                    return AsyncResult.create(-3);
                }
                CreateGroupItem createGroupItem = new CreateGroupItem(sGetAndroidContext);
                RequestAlbumItem requestAlbumItem = new RequestAlbumItem(0, albumById);
                requestAlbumItem.setSpecificType(SyncType.GPRS_FORCE);
                GallerySyncResult<JSONObject> execute = createGroupItem.execute(xiaomiAccount, extToken, null, requestAlbumItem);
                if (execute != null && execute.code == GallerySyncCode.OK) {
                    return AsyncResult.create(0, CloudUtils.getItem(GalleryCloudUtils.CLOUD_URI, sGetAndroidContext, j.c, str).getServerId());
                }
                return AsyncResult.create(-2);
            }
        });
    }

    public static AsyncResult<List<String>> requestShareToDevice(final String str, final String str2, final boolean z) {
        return execute(new Callable<AsyncResult<List<String>>>() { // from class: com.miui.gallery.share.AlbumShareOperations.15
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public AsyncResult<List<String>> mo1352call() throws Exception {
                if (!BaseNetworkUtils.isNetworkConnected()) {
                    return AsyncResult.create(-6);
                }
                if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
                    return AsyncResult.create(-11);
                }
                Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
                Account xiaomiAccount = AccountHelper.getXiaomiAccount(sGetAndroidContext);
                if (xiaomiAccount == null) {
                    return AsyncResult.create(-4);
                }
                GalleryExtendedAuthToken extToken = CloudUtils.getExtToken(sGetAndroidContext, xiaomiAccount);
                if (extToken == null) {
                    return AsyncResult.create(-3);
                }
                String deviceShareUrl = HostManager.AlbumShareOperation.getDeviceShareUrl();
                ArrayList newArrayList = Lists.newArrayList();
                AlbumShareOperations.addNameValuePair(newArrayList, "albumId", str, null);
                AlbumShareOperations.addNameValuePair(newArrayList, "shareDeviceType", str2, null);
                AlbumShareOperations.addNameValuePair(newArrayList, CallMethod.RESULT_ENABLE_BOOLEAN, String.valueOf(z), null);
                JSONObject postToXiaomi = CloudUtils.postToXiaomi(deviceShareUrl, newArrayList, null, xiaomiAccount, extToken, 0, false);
                int parseErrorCode = CheckResult.parseErrorCode(postToXiaomi);
                if (parseErrorCode != 0) {
                    return AsyncResult.create(parseErrorCode);
                }
                JSONObject jSONObject = postToXiaomi.getJSONObject("data").getJSONObject(MiStat.Param.CONTENT);
                JSONArray jSONArray = jSONObject.getJSONArray("shareDeviceType");
                ArrayList newArrayList2 = Lists.newArrayList();
                for (int i = 0; i < jSONArray.length(); i++) {
                    newArrayList2.add(jSONArray.getString(i));
                }
                new SyncOwnerAll(sGetAndroidContext, xiaomiAccount, extToken).handleItem(jSONObject);
                SyncLogger.d("AlbumShareOperations", "request share device switch success.");
                return AsyncResult.create(0, newArrayList2);
            }
        });
    }

    public static void addNameValuePair(List<NameValuePair> list, String str, String str2, GalleryExtendedAuthToken galleryExtendedAuthToken) throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        list.add(new BasicNameValuePair(str, str2));
    }

    public static synchronized UserInfo self() {
        UserInfo userInfo;
        Account xiaomiAccount;
        List<UserInfo> list;
        synchronized (AlbumShareOperations.class) {
            if (sSelf == null && (xiaomiAccount = AccountHelper.getXiaomiAccount(GalleryApp.sGetAndroidContext())) != null) {
                ArrayList newArrayList = Lists.newArrayList();
                newArrayList.add(xiaomiAccount.name);
                AsyncResult<List<UserInfo>> requestUserInfo = requestUserInfo(newArrayList);
                if (requestUserInfo != null && requestUserInfo.mError == 0 && (list = requestUserInfo.mData) != null && !list.isEmpty()) {
                    UserInfo userInfo2 = list.get(0);
                    sSelf = userInfo2;
                    if (userInfo2 != null) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("user_id", userInfo2.getUserId());
                        contentValues.put("alias_nick", userInfo2.getAliasNick());
                        contentValues.put("miliao_nick", userInfo2.getMiliaoNick());
                        contentValues.put("miliao_icon_url", userInfo2.getMiliaoIconUrl());
                        Uri uri = GalleryCloudUtils.USER_INFO_URI;
                        synchronized (uri) {
                            if (GalleryUtils.safeUpdate(uri, contentValues, "user_id=?", new String[]{sSelf.getUserId()}) == 0) {
                                GalleryUtils.safeInsert(uri, contentValues);
                            }
                        }
                    }
                }
            }
            userInfo = sSelf;
        }
        return userInfo;
    }

    public static String myName() {
        UserInfo self = self();
        if (self != null) {
            return self.getDisplayName();
        }
        return GalleryCloudUtils.getAccountName();
    }

    public static String readCache(String str, String str2, String str3, int i) {
        Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
        long currentTimeMillis = System.currentTimeMillis();
        Cursor query = sGetAndroidContext.getContentResolver().query(CloudUtils.getLimitUri(GalleryCloudUtils.CLOUD_CACHE_URI, 1), new String[]{str2}, String.format(Locale.US, "(%s=?) AND (%s<%d) AND (%s>%d) AND (%s)", "serverId", str3, Long.valueOf(i + currentTimeMillis), str3, Long.valueOf(currentTimeMillis), CloudUtils.sqlNotEmptyStr(str2)), new String[]{str}, null);
        if (query == null) {
            return null;
        }
        try {
            if (!query.moveToFirst()) {
                return null;
            }
            return query.getString(0);
        } finally {
            query.close();
        }
    }

    public static void persisitCache(String str, String str2, String str3, String str4, int i) {
        if (TextUtils.isEmpty(str4)) {
            return;
        }
        ContentResolver contentResolver = GalleryApp.sGetAndroidContext().getContentResolver();
        ContentValues contentValues = new ContentValues();
        contentValues.put(str2, str4);
        contentValues.put(str3, Long.valueOf(System.currentTimeMillis() + i));
        Uri uri = GalleryCloudUtils.CLOUD_CACHE_URI;
        if (contentResolver.update(uri, contentValues, "serverId=?", new String[]{str}) != 0) {
            return;
        }
        contentValues.put("serverId", str);
        contentResolver.insert(uri, contentValues);
    }

    public static <V> AsyncResult<V> execute(Callable<AsyncResult<V>> callable) {
        AsyncResult<V> create;
        SystemClock.uptimeMillis();
        new Throwable().getStackTrace()[1].getMethodName();
        if (!ApplicationHelper.supportShare()) {
            return AsyncResult.create(-1);
        }
        try {
            create = callable.call();
        } finally {
            SyncLogger.d("AlbumShareOperations", String.format("result error=%d", Integer.valueOf(create.mError)));
            return create;
        }
        SyncLogger.d("AlbumShareOperations", String.format("result error=%d", Integer.valueOf(create.mError)));
        return create;
    }

    public static void requestSyncToHandleUnauthorized() {
        SyncUtil.requestSync(StaticContext.sGetAndroidContext(), new SyncRequest.Builder().setSyncType(SyncType.NORMAL).setSyncReason(16L).build());
    }
}
