package com.miui.gallery.cloud;

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
import com.baidu.platform.comapi.map.MapBundleKey;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.miui.account.AccountHelper;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.cloud.base.SyncRequest;
import com.miui.gallery.cloud.base.SyncType;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.util.AsyncResult;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.deviceprovider.ApplicationHelper;
import com.xiaomi.mirror.synergy.CallMethod;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Callable;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class AlbumShareOperations {
    public static UserInfo sSelf;

    public static void resetAccountInfo() {
        synchronized (AlbumShareOperations.class) {
            sSelf = null;
        }
    }

    public static String myName() {
        UserInfo self = self();
        if (self != null) {
            return self.getDisplayName();
        }
        return GalleryCloudUtils.getAccountName();
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

    public static AsyncResult<String> requestUrlForBarcode(final String str) {
        return execute(new Callable<AsyncResult<String>>() { // from class: com.miui.gallery.cloud.AlbumShareOperations.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public AsyncResult<String> mo670call() throws Exception {
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

    public static void addNameValuePair(List<NameValuePair> list, String str, String str2, GalleryExtendedAuthToken galleryExtendedAuthToken) throws IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
        list.add(new BasicNameValuePair(str, str2));
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

    public static AsyncResult<Void> syncAllUserInfoFromNetwork() {
        return execute(new Callable<AsyncResult<Void>>() { // from class: com.miui.gallery.cloud.AlbumShareOperations.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public AsyncResult<Void> mo671call() throws Exception {
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
                        String concatAll = GalleryUtils.concatAll(list, ",", new GalleryUtils.ConcatConverter<UserInfo>() { // from class: com.miui.gallery.cloud.AlbumShareOperations.2.1
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
        return execute(new Callable<AsyncResult<List<UserInfo>>>() { // from class: com.miui.gallery.cloud.AlbumShareOperations.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public AsyncResult<List<UserInfo>> mo672call() throws Exception {
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

    public static AsyncResult<String> requestPublicUrl(final String str, final boolean z) {
        return execute(new Callable<AsyncResult<String>>() { // from class: com.miui.gallery.cloud.AlbumShareOperations.4
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public AsyncResult<String> mo673call() throws Exception {
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

    public static <V> AsyncResult<V> execute(Callable<AsyncResult<V>> callable) {
        AsyncResult<V> create;
        StringBuilder sb;
        long uptimeMillis = SystemClock.uptimeMillis();
        String methodName = new Throwable().getStackTrace()[1].getMethodName();
        SyncLogger.d("AlbumShareOperations", "start callable: " + methodName);
        if (!ApplicationHelper.supportShare()) {
            return AsyncResult.create(-1);
        }
        try {
            create = callable.call();
            sb = new StringBuilder();
        } catch (Throwable th) {
            try {
                int fromThrowable = ServerErrorCode.fromThrowable(th);
                if (fromThrowable == -105 && th.getStatusCode() == 401) {
                    requestSyncToHandleUnauthorized();
                }
                SyncLogger.e("AlbumShareOperations", "Error code=" + fromThrowable, th);
                create = AsyncResult.create(fromThrowable);
                sb = new StringBuilder();
            } catch (Throwable th2) {
                SyncLogger.d("AlbumShareOperations", "end callable: " + methodName + ", cost time=" + (SystemClock.uptimeMillis() - uptimeMillis));
                throw th2;
            }
        }
        sb.append("end callable: ");
        sb.append(methodName);
        sb.append(", cost time=");
        sb.append(SystemClock.uptimeMillis() - uptimeMillis);
        SyncLogger.d("AlbumShareOperations", sb.toString());
        SyncLogger.d("AlbumShareOperations", String.format("result error=%d", Integer.valueOf(create.mError)));
        return create;
    }

    public static void requestSyncToHandleUnauthorized() {
        SyncUtil.requestSync(StaticContext.sGetAndroidContext(), new SyncRequest.Builder().setSyncType(SyncType.NORMAL).setSyncReason(16L).build());
    }
}
