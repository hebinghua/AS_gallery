package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import com.google.common.collect.Maps;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.cloud.ServerErrorCode;
import com.miui.gallery.cloud.SpaceFullHandler;
import com.miui.gallery.cloud.base.GallerySyncCode;
import com.miui.gallery.cloud.base.SyncRequest;
import com.miui.gallery.cloud.base.SyncType;
import com.miui.gallery.cloud.operation.create.CreateGroupItem;
import com.miui.gallery.data.DBAlbum;
import com.miui.gallery.data.DBImage;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.ui.ProcessTask;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.deprecated.Preference;
import com.xiaomi.milab.videosdk.message.MsgType;
import com.xiaomi.stat.a.j;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class ServerErrorCode {
    public static final GalleryErrorHandler sAlbumNotEmptyHandler;
    public static final GalleryErrorHandler sAlbumNotExistsHandler;
    public static Map<Class<?>, Integer> sExceptionMap;
    public static final GalleryErrorHandler sGalleryNotFoundHandler;
    public static final HashMap<Integer, GalleryErrorCodeItem> sGalleryServerErrors;
    public static final GalleryErrorHandler sNoErrorHandler;
    public static final GalleryErrorHandler sNotRetryErrorHandler;
    public static final GalleryErrorHandler sPrivacyRejectedHandler;
    public static final List<Integer> sRetryErrorCode;
    public static final GalleryErrorHandler sRetryErrorHandler;
    public static final GalleryErrorHandler sSpaceFullHandler;

    /* loaded from: classes.dex */
    public static class ExceptionWithErrorCode extends Error {
        private static final long serialVersionUID = 1;
        public final int mErrorCode;
    }

    /* loaded from: classes.dex */
    public interface GalleryErrorHandler {
        void onError(JSONObject jSONObject, RequestItemBase requestItemBase, SpaceFullHandler.SpaceFullListener spaceFullListener) throws JSONException;
    }

    public static boolean isClientError(int i) {
        return i < 0;
    }

    /* loaded from: classes.dex */
    public static class GalleryErrorCodeItem {
        public int code;
        public GalleryErrorHandler errorHandler;
        public GallerySyncCode result;

        public GalleryErrorCodeItem(int i, GallerySyncCode gallerySyncCode, GalleryErrorHandler galleryErrorHandler) {
            this.code = i;
            this.result = gallerySyncCode;
            this.errorHandler = galleryErrorHandler;
        }
    }

    static {
        LinkedList<Integer> linkedList = new LinkedList();
        sRetryErrorCode = linkedList;
        HashMap<Integer, GalleryErrorCodeItem> hashMap = new HashMap<>();
        sGalleryServerErrors = hashMap;
        GalleryErrorHandler galleryErrorHandler = new GalleryErrorHandler() { // from class: com.miui.gallery.cloud.ServerErrorCode.1
            @Override // com.miui.gallery.cloud.ServerErrorCode.GalleryErrorHandler
            public void onError(JSONObject jSONObject, RequestItemBase requestItemBase, SpaceFullHandler.SpaceFullListener spaceFullListener) {
                if (spaceFullListener != null) {
                    spaceFullListener.handleSpaceNotFull((RequestCloudItem) requestItemBase);
                }
            }
        };
        sNoErrorHandler = galleryErrorHandler;
        sSpaceFullHandler = new GalleryErrorHandler() { // from class: com.miui.gallery.cloud.ServerErrorCode.2
            @Override // com.miui.gallery.cloud.ServerErrorCode.GalleryErrorHandler
            public void onError(JSONObject jSONObject, RequestItemBase requestItemBase, SpaceFullHandler.SpaceFullListener spaceFullListener) {
                SyncLogger.e("ServerErrorCode", "not retry error, gallery space full %s", jSONObject == null ? "" : jSONObject.toString());
                if (spaceFullListener != null) {
                    spaceFullListener.handleSpaceFullError((RequestCloudItem) requestItemBase);
                }
            }
        };
        sRetryErrorHandler = new GalleryErrorHandler() { // from class: com.miui.gallery.cloud.ServerErrorCode.3
            @Override // com.miui.gallery.cloud.ServerErrorCode.GalleryErrorHandler
            public void onError(JSONObject jSONObject, RequestItemBase requestItemBase, SpaceFullHandler.SpaceFullListener spaceFullListener) throws JSONException {
                if (requestItemBase != null && jSONObject.has("data")) {
                    JSONObject jSONObject2 = jSONObject.getJSONObject("data");
                    if (jSONObject2.has("retryAfter")) {
                        requestItemBase.retryAfter = CloudUtils.getLongAttributeFromJson(jSONObject2, "retryAfter");
                    }
                }
                SyncLogger.e("ServerErrorCode", "retry error: %s", jSONObject == null ? "" : jSONObject.toString());
            }
        };
        sNotRetryErrorHandler = new GalleryErrorHandler() { // from class: com.miui.gallery.cloud.ServerErrorCode.4
            @Override // com.miui.gallery.cloud.ServerErrorCode.GalleryErrorHandler
            public void onError(JSONObject jSONObject, RequestItemBase requestItemBase, SpaceFullHandler.SpaceFullListener spaceFullListener) throws JSONException {
                SyncLogger.e("ServerErrorCode", "not retry error: %s", jSONObject == null ? "" : jSONObject.toString());
            }
        };
        sPrivacyRejectedHandler = new AnonymousClass5();
        sGalleryNotFoundHandler = new GalleryErrorHandler() { // from class: com.miui.gallery.cloud.ServerErrorCode.6
            @Override // com.miui.gallery.cloud.ServerErrorCode.GalleryErrorHandler
            public void onError(JSONObject jSONObject, RequestItemBase requestItemBase, SpaceFullHandler.SpaceFullListener spaceFullListener) {
                SyncLogger.e("ServerErrorCode", "server can't found this item: %s", jSONObject == null ? "" : jSONObject.toString());
                if (requestItemBase == null || !(requestItemBase instanceof RequestCloudItem)) {
                    return;
                }
                CloudUtils.deleteDirty(((RequestCloudItem) requestItemBase).dbCloud);
            }
        };
        sAlbumNotExistsHandler = new GalleryErrorHandler() { // from class: com.miui.gallery.cloud.ServerErrorCode.7
            @Override // com.miui.gallery.cloud.ServerErrorCode.GalleryErrorHandler
            public void onError(JSONObject jSONObject, RequestItemBase requestItemBase, SpaceFullHandler.SpaceFullListener spaceFullListener) {
                SyncLogger.e("ServerErrorCode", "special error, album not exist: %s", jSONObject == null ? "" : jSONObject.toString());
                if (requestItemBase == null || !(requestItemBase instanceof RequestCloudItem)) {
                    return;
                }
                DBImage dBImage = ((RequestCloudItem) requestItemBase).dbCloud;
                if ((!dBImage.isItemType() && dBImage.getLocalFlag() == 2) || dBImage.isShareItem() || !dBImage.isItemType()) {
                    return;
                }
                if ((dBImage.getLocalFlag() != 6 && dBImage.getLocalFlag() != 9 && dBImage.getLocalFlag() != 8 && dBImage.getLocalFlag() != 5) || ServerErrorCode.hasUnCreatedGroupInLocalDB(dBImage.getLocalGroupId())) {
                    return;
                }
                SyncLogger.d("ServerErrorCode", "this album is need to recreate later %s", dBImage.getLocalGroupId());
                CreateGroupItem.recreateGroup(dBImage.getLocalGroupId());
            }
        };
        sAlbumNotEmptyHandler = new GalleryErrorHandler() { // from class: com.miui.gallery.cloud.ServerErrorCode.8
            @Override // com.miui.gallery.cloud.ServerErrorCode.GalleryErrorHandler
            public void onError(JSONObject jSONObject, RequestItemBase requestItemBase, SpaceFullHandler.SpaceFullListener spaceFullListener) {
                SyncLogger.e("ServerErrorCode", "special error, album not empty %s", jSONObject == null ? "" : jSONObject.toString());
                if (requestItemBase == null || !(requestItemBase instanceof RequestAlbumItem)) {
                    return;
                }
                DBAlbum dBAlbum = ((RequestAlbumItem) requestItemBase).dbAlbum;
                if (dBAlbum.getLocalFlag() != 2 || ServerErrorCode.hasDeleteItemsInAGroup(dBAlbum.getId())) {
                    return;
                }
                ServerErrorCode.cancelDeleteGroupOperation(dBAlbum.getId());
            }
        };
        linkedList.add(Integer.valueOf((int) MsgType.XMSCONTEXT));
        linkedList.add(10011);
        linkedList.add(10032);
        linkedList.add(10034);
        linkedList.add(20607);
        linkedList.add(23000);
        linkedList.add(50005);
        linkedList.add(50002);
        linkedList.add(50003);
        linkedList.add(50007);
        linkedList.add(50010);
        linkedList.add(50017);
        linkedList.add(50022);
        linkedList.add(50031);
        linkedList.add(50038);
        linkedList.add(50054);
        hashMap.put(0, new GalleryErrorCodeItem(0, GallerySyncCode.OK, galleryErrorHandler));
        for (Integer num : linkedList) {
            sGalleryServerErrors.put(num, new GalleryErrorCodeItem(num.intValue(), GallerySyncCode.RETRY_ERROR, sRetryErrorHandler));
        }
        HashMap<Integer, GalleryErrorCodeItem> hashMap2 = sGalleryServerErrors;
        GallerySyncCode gallerySyncCode = GallerySyncCode.NOT_CONTINUE_ERROR;
        hashMap2.put(50006, new GalleryErrorCodeItem(50006, gallerySyncCode, sSpaceFullHandler));
        hashMap2.put(50016, new GalleryErrorCodeItem(50016, gallerySyncCode, sRetryErrorHandler));
        GallerySyncCode gallerySyncCode2 = GallerySyncCode.NOT_RETRY_ERROR;
        GalleryErrorHandler galleryErrorHandler2 = sNotRetryErrorHandler;
        hashMap2.put(50019, new GalleryErrorCodeItem(50019, gallerySyncCode2, galleryErrorHandler2));
        hashMap2.put(10008, new GalleryErrorCodeItem(10008, gallerySyncCode2, galleryErrorHandler2));
        hashMap2.put(10017, new GalleryErrorCodeItem(10017, gallerySyncCode2, galleryErrorHandler2));
        hashMap2.put(20014, new GalleryErrorCodeItem(20014, gallerySyncCode2, galleryErrorHandler2));
        hashMap2.put(50001, new GalleryErrorCodeItem(50001, gallerySyncCode2, galleryErrorHandler2));
        hashMap2.put(50004, new GalleryErrorCodeItem(50004, gallerySyncCode2, galleryErrorHandler2));
        hashMap2.put(50008, new GalleryErrorCodeItem(50008, gallerySyncCode2, sGalleryNotFoundHandler));
        hashMap2.put(50009, new GalleryErrorCodeItem(50009, gallerySyncCode2, galleryErrorHandler2));
        hashMap2.put(50011, new GalleryErrorCodeItem(50011, gallerySyncCode2, galleryErrorHandler2));
        hashMap2.put(50013, new GalleryErrorCodeItem(50013, gallerySyncCode2, galleryErrorHandler2));
        hashMap2.put(50015, new GalleryErrorCodeItem(50015, gallerySyncCode2, galleryErrorHandler2));
        hashMap2.put(50018, new GalleryErrorCodeItem(50018, gallerySyncCode2, galleryErrorHandler2));
        hashMap2.put(50019, new GalleryErrorCodeItem(50019, gallerySyncCode2, galleryErrorHandler2));
        hashMap2.put(50020, new GalleryErrorCodeItem(50020, gallerySyncCode2, galleryErrorHandler2));
        hashMap2.put(50021, new GalleryErrorCodeItem(50021, gallerySyncCode2, galleryErrorHandler2));
        hashMap2.put(50023, new GalleryErrorCodeItem(50023, gallerySyncCode2, galleryErrorHandler2));
        hashMap2.put(50024, new GalleryErrorCodeItem(50024, gallerySyncCode2, galleryErrorHandler2));
        hashMap2.put(50025, new GalleryErrorCodeItem(50025, gallerySyncCode2, galleryErrorHandler2));
        hashMap2.put(50026, new GalleryErrorCodeItem(50026, gallerySyncCode2, galleryErrorHandler2));
        hashMap2.put(50027, new GalleryErrorCodeItem(50027, gallerySyncCode2, galleryErrorHandler2));
        hashMap2.put(50028, new GalleryErrorCodeItem(50028, gallerySyncCode2, galleryErrorHandler2));
        hashMap2.put(50029, new GalleryErrorCodeItem(50029, gallerySyncCode2, galleryErrorHandler2));
        hashMap2.put(50030, new GalleryErrorCodeItem(50030, gallerySyncCode2, galleryErrorHandler2));
        hashMap2.put(80004, new GalleryErrorCodeItem(80004, gallerySyncCode2, galleryErrorHandler2));
        hashMap2.put(50012, new GalleryErrorCodeItem(50012, GallerySyncCode.ALBUM_NOT_EXIST, sAlbumNotExistsHandler));
        hashMap2.put(50014, new GalleryErrorCodeItem(50014, GallerySyncCode.ALBUM_NOT_EMPTY, sAlbumNotEmptyHandler));
        hashMap2.put(40003, new GalleryErrorCodeItem(40003, gallerySyncCode2, galleryErrorHandler2));
        hashMap2.put(10016, new GalleryErrorCodeItem(10016, gallerySyncCode2, galleryErrorHandler2));
        hashMap2.put(52000, new GalleryErrorCodeItem(52000, GallerySyncCode.RESET_SYNC_TAG, null));
        hashMap2.put(53003, new GalleryErrorCodeItem(53003, GallerySyncCode.CONDITION_INTERRUPTED, sPrivacyRejectedHandler));
        hashMap2.put(50050, new GalleryErrorCodeItem(50050, GallerySyncCode.SERVER_INVALID, galleryErrorHandler2));
        HashMap newHashMap = Maps.newHashMap();
        sExceptionMap = newHashMap;
        newHashMap.put(IllegalBlockSizeException.class, -101);
        sExceptionMap.put(BadPaddingException.class, -102);
        sExceptionMap.put(JSONException.class, -103);
        sExceptionMap.put(IOException.class, -104);
        sExceptionMap.put(GalleryMiCloudServerException.class, -105);
        sExceptionMap.put(MalformedURLException.class, -106);
        sExceptionMap.put(UnsupportedEncodingException.class, -107);
        sExceptionMap.put(ClientProtocolException.class, -108);
        sExceptionMap.put(URISyntaxException.class, -109);
        sExceptionMap.put(UnsupportedOperationException.class, -110);
        sExceptionMap.put(SocketTimeoutException.class, -111);
        sExceptionMap.put(UnknownHostException.class, -112);
    }

    /* renamed from: com.miui.gallery.cloud.ServerErrorCode$5 */
    /* loaded from: classes.dex */
    public class AnonymousClass5 implements GalleryErrorHandler {
        /* renamed from: $r8$lambda$wFL-bXzT1aDeUAVlVDBeWzaPNjw */
        public static /* synthetic */ Void m680$r8$lambda$wFLbXzT1aDeUAVlVDBeWzaPNjw(Account account, Void[] voidArr) {
            return lambda$onError$0(account, voidArr);
        }

        @Override // com.miui.gallery.cloud.ServerErrorCode.GalleryErrorHandler
        public void onError(JSONObject jSONObject, RequestItemBase requestItemBase, SpaceFullHandler.SpaceFullListener spaceFullListener) throws JSONException {
            SyncLogger.e("ServerErrorCode", "privacy rejected error: %s", jSONObject == null ? "" : jSONObject.toString());
            SyncUtil.setSyncAutomatically(GalleryApp.sGetAndroidContext(), false, -1);
            final Account account = AccountCache.getAccount();
            if (account != null) {
                GalleryPreferences.MiCloud.setPrivacyPolicyRejected(account.name, true);
            }
            new ProcessTask(new ProcessTask.ProcessCallback() { // from class: com.miui.gallery.cloud.ServerErrorCode$5$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.ui.ProcessTask.ProcessCallback
                public final Object doProcess(Object[] objArr) {
                    return ServerErrorCode.AnonymousClass5.m680$r8$lambda$wFLbXzT1aDeUAVlVDBeWzaPNjw(account, (Void[]) objArr);
                }
            }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
        }

        public static /* synthetic */ Void lambda$onError$0(Account account, Void[] voidArr) {
            Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
            Preference.setSyncShouldClearDataBase(true);
            ClearDataManager.getInstance().clearDataBaseIfNeeded(sGetAndroidContext, account);
            return null;
        }
    }

    public static boolean isRetryCode(int i) {
        return sRetryErrorCode.contains(Integer.valueOf(i));
    }

    public static int fromThrowable(Throwable th) {
        if (th instanceof ExceptionWithErrorCode) {
            return ((ExceptionWithErrorCode) th).mErrorCode;
        }
        for (Class<?> cls = th.getClass(); cls != Object.class; cls = cls.getSuperclass()) {
            Integer num = sExceptionMap.get(th.getClass());
            if (num != null) {
                return num.intValue();
            }
        }
        return -2;
    }

    public static boolean hasUnCreatedGroupInLocalDB(String str) {
        Cursor cursor = null;
        try {
            cursor = GalleryApp.sGetAndroidContext().getContentResolver().query(CloudUtils.getCloudLimit1Uri(), new String[]{j.c}, "localFlag = ?  AND _id = ? ", new String[]{String.valueOf(8), str}, null);
            if (cursor != null) {
                if (cursor.moveToNext()) {
                    cursor.close();
                    return true;
                }
            }
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static boolean hasDeleteItemsInAGroup(String str) {
        Cursor cursor = null;
        try {
            cursor = GalleryApp.sGetAndroidContext().getContentResolver().query(CloudUtils.getCloudLimit1Uri(), new String[]{j.c}, "localFlag = ?  AND localGroupId = ? ", new String[]{String.valueOf(2), str}, null);
            if (cursor != null) {
                if (cursor.moveToNext()) {
                    cursor.close();
                    return true;
                }
            }
            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static void cancelDeleteGroupOperation(String str) {
        ContentValues contentValues = new ContentValues(1);
        contentValues.put("localFlag", (Integer) 0);
        GalleryUtils.safeUpdate(GalleryCloudUtils.ALBUM_URI, contentValues, "_id = ? ", new String[]{str});
    }

    /* loaded from: classes.dex */
    public static class MiCloudServerExceptionHandler {
        public static Exception sMicloudServerException;

        public static Exception getMiCloudServerException() {
            return sMicloudServerException;
        }

        public static boolean hasMiCloudServerException() {
            return sMicloudServerException != null;
        }

        public static void cleanMiCloudServerException() {
            sMicloudServerException = null;
        }

        public static synchronized boolean handleMiCloudException(Exception exc) {
            synchronized (MiCloudServerExceptionHandler.class) {
                SyncLogger.d("MiCloudServerExceptionHandler", "handleMiCloudException: " + exc);
                sMicloudServerException = exc;
                SyncUtil.requestSync(GalleryApp.sGetAndroidContext(), new SyncRequest.Builder().setSyncType(SyncType.NORMAL).setSyncReason(Long.MAX_VALUE).build());
            }
            return true;
        }

        public static synchronized void checkMiCloudServerException() throws GalleryMiCloudServerException {
            synchronized (MiCloudServerExceptionHandler.class) {
                if (hasMiCloudServerException()) {
                    Exception miCloudServerException = getMiCloudServerException();
                    cleanMiCloudServerException();
                    throw new GalleryMiCloudServerException(miCloudServerException);
                }
            }
        }
    }
}
