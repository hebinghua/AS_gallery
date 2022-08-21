package com.miui.gallery.trash;

import android.accounts.Account;
import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.FragmentActivity;
import ch.qos.logback.core.CoreConstants;
import com.miui.account.AccountHelper;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.cloud.HostManager;
import com.miui.gallery.cloud.SyncOwnerAll;
import com.miui.gallery.cloud.base.GalleryExtendedAuthToken;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.scanner.core.ScannerEngine;
import com.miui.gallery.scanner.core.model.SaveParams;
import com.miui.gallery.scanner.core.task.eventual.ScanResult;
import com.miui.gallery.scanner.utils.SaveToCloudUtil;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.ui.ProcessTask;
import com.miui.gallery.ui.ProcessTaskForStoragePermissionMiss;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.FileSizeFormatter;
import com.miui.gallery.util.LazyValue;
import com.miui.gallery.util.MediaAndAlbumOperations;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.logger.TimingTracing;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.a.j;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class TrashUtils {
    public static final Object USER_INFO_LOCK = new Object();
    public static volatile LazyValue<Void, UserInfo> sUserInfo;

    public static /* synthetic */ Object $r8$lambda$DSo9Yk51I_R45VtgE2jX7UN17ck(List list, Context context, Context context2, int i, ThreadPool.JobContext jobContext) {
        return lambda$requestServerSide$1(list, context, context2, i, jobContext);
    }

    public static /* synthetic */ void $r8$lambda$aGg1Za2g5sMSFkZgOG1fq__xf8g(ArrayList arrayList, FragmentActivity fragmentActivity) {
        lambda$doRecoveryDBWork$0(arrayList, fragmentActivity);
    }

    public static void requestVipInfo() {
        Account xiaomiAccount;
        GalleryExtendedAuthToken extToken;
        Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
        try {
            if (!BaseGalleryPreferences.CTA.canConnectNetwork() || !BaseNetworkUtils.isNetworkConnected() || (xiaomiAccount = AccountHelper.getXiaomiAccount(sGetAndroidContext)) == null || (extToken = CloudUtils.getExtToken(sGetAndroidContext, xiaomiAccount)) == null) {
                return;
            }
            boolean isVip = isVip();
            ArrayList arrayList = new ArrayList();
            arrayList.add(new BasicNameValuePair("locale", FileSizeFormatter.localeFromContext(sGetAndroidContext).toString()));
            JSONObject fromXiaomi = CloudUtils.getFromXiaomi(HostManager.TrashBin.getVipInfoUrl(), arrayList, xiaomiAccount, extToken, 0, false);
            if (fromXiaomi == null) {
                return;
            }
            UserInfo fromJson = UserInfo.fromJson(fromXiaomi.getJSONObject("data"));
            if (fromJson != null) {
                GalleryPreferences.Trash.setUserInfo(fromJson.toJSON());
            }
            initUserInfo();
            if (isVip || !isVip()) {
                return;
            }
            GalleryPreferences.Trash.setWhite2VipTime(System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static UserInfo getLastUserInfo() {
        if (sUserInfo == null) {
            synchronized (USER_INFO_LOCK) {
                if (sUserInfo == null) {
                    initUserInfo();
                }
            }
        }
        return sUserInfo.get(null);
    }

    public static boolean isVip() {
        return !TextUtils.equals(getLastUserInfo().mLevel, "None");
    }

    public static void initUserInfo() {
        synchronized (USER_INFO_LOCK) {
            sUserInfo = new LazyValue<Void, UserInfo>() { // from class: com.miui.gallery.trash.TrashUtils.1
                @Override // com.miui.gallery.util.LazyValue
                /* renamed from: onInit */
                public UserInfo mo1272onInit(Void r13) {
                    UserInfo fromJson = UserInfo.fromJson(GalleryPreferences.Trash.getUserInfo());
                    return fromJson == null ? new UserInfo("None", "Free", "https://i.mi.com/vip", 2592000000L, System.currentTimeMillis() + 2592000000L, System.currentTimeMillis() - 2592000000L, 2592000000L) : fromJson;
                }
            };
        }
    }

    public static synchronized void pullDeleteListFromServer() {
        JSONObject optJSONObject;
        synchronized (TrashUtils.class) {
            Context sGetAndroidContext = GalleryApp.sGetAndroidContext();
            if (!BaseGalleryPreferences.CTA.canConnectNetwork()) {
                return;
            }
            Account xiaomiAccount = AccountHelper.getXiaomiAccount(sGetAndroidContext);
            if (xiaomiAccount == null) {
                return;
            }
            if (!SyncUtil.isGalleryCloudSyncable(sGetAndroidContext)) {
                return;
            }
            GalleryExtendedAuthToken extToken = CloudUtils.getExtToken(sGetAndroidContext, xiaomiAccount);
            if (extToken == null) {
                return;
            }
            TrashSyncTag syncTagByAccount = getSyncTagByAccount(xiaomiAccount);
            long j = 0;
            if (syncTagByAccount != null) {
                long syncTag = syncTagByAccount.getSyncTag();
                if (syncTag == 0 && !syncTagByAccount.isContinue()) {
                    return;
                }
                j = syncTag;
            }
            do {
                try {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(new BasicNameValuePair("syncTag", Long.toString(j)));
                    arrayList.add(new BasicNameValuePair("limit", Long.toString(20L)));
                    JSONObject fromXiaomi = CloudUtils.getFromXiaomi(HostManager.TrashBin.getDeleteListUrl(), arrayList, xiaomiAccount, extToken, 0, false);
                    if (fromXiaomi == null || (optJSONObject = fromXiaomi.optJSONObject("data")) == null || optJSONObject.length() == 0) {
                        break;
                    }
                    j = Long.parseLong(optJSONObject.optString("syncTag"));
                    setSyncTag(xiaomiAccount, j, optJSONObject.optBoolean("lastPage"));
                    JSONArray optJSONArray = optJSONObject.optJSONArray(MiStat.Param.CONTENT);
                    if (optJSONArray != null) {
                        SyncOwnerAll syncOwnerAll = new SyncOwnerAll(sGetAndroidContext, xiaomiAccount, extToken);
                        for (int i = 0; i < optJSONArray.length(); i++) {
                            JSONObject jSONObject = optJSONArray.getJSONObject(i);
                            if (jSONObject != null) {
                                try {
                                    syncOwnerAll.handleItem(jSONObject);
                                } catch (Exception unused) {
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (!optJSONObject.optBoolean("lastPage", false));
        }
    }

    public static TrashSyncTag getSyncTagByAccount(Account account) {
        if (account != null) {
            List query = GalleryEntityManager.getInstance().query(TrashSyncTag.class, getSyncTagSelection(account), null, null, String.format(Locale.US, "%s,%s", 0, 1));
            if (!BaseMiscUtil.isValid(query)) {
                return null;
            }
            return (TrashSyncTag) query.get(0);
        }
        return null;
    }

    public static void clearSyncTag() {
        GalleryEntityManager.getInstance().deleteAll(TrashSyncTag.class);
    }

    public static void setSyncTag(Account account, long j, boolean z) {
        if (account == null) {
            return;
        }
        if (!BaseMiscUtil.isValid(GalleryEntityManager.getInstance().query(TrashSyncTag.class, getSyncTagSelection(account), null))) {
            TrashSyncTag trashSyncTag = new TrashSyncTag(account);
            trashSyncTag.setSyncTag(j);
            trashSyncTag.setContinue(!z);
            GalleryEntityManager.getInstance().insert(trashSyncTag);
            return;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("syncTag", Long.valueOf(j));
        contentValues.put("isContinue", Integer.valueOf(z ? TrashSyncTag.BREAK : TrashSyncTag.CONTINUE));
        GalleryEntityManager.getInstance().update(TrashSyncTag.class, contentValues, getSyncTagSelection(account), null);
    }

    public static String getSyncTagSelection(Account account) {
        return "accountName = '" + account.name + "' AND accountType = '" + account.type + "'";
    }

    public static ArrayList<Long> addToFavoritesIds(final HashMap<Long, Long> hashMap, FragmentActivity fragmentActivity) {
        final ArrayList<Long> arrayList = new ArrayList<>();
        Set<Long> keySet = hashMap.keySet();
        SafeDBUtil.safeQuery(fragmentActivity, GalleryContract.Favorites.URI, new String[]{"isFavorite", "cloud_id"}, "cloud_id IN (" + TextUtils.join(",", keySet) + ")", (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Object>() { // from class: com.miui.gallery.trash.TrashUtils.2
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public Object mo1808handle(Cursor cursor) {
                if (cursor == null || !cursor.moveToFirst()) {
                    return null;
                }
                do {
                    long longValue = ((Long) hashMap.get(Long.valueOf(cursor.getLong(1)))).longValue();
                    if (1 == cursor.getInt(0) && longValue > 0) {
                        arrayList.add(Long.valueOf(longValue));
                    }
                } while (cursor.moveToNext());
                return null;
            }
        });
        return arrayList;
    }

    public static void doRecovery(FragmentActivity fragmentActivity, List<TrashBinItem> list) {
        if (!BaseMiscUtil.isValid(list) || fragmentActivity == null || !doRecoveryDBWork(fragmentActivity, list)) {
            return;
        }
        RecoveryFileTask recoveryFileTask = new RecoveryFileTask(false);
        recoveryFileTask.setFragmentActivityForStoragePermissionMiss(fragmentActivity);
        recoveryFileTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, list);
    }

    /* JADX WARN: Removed duplicated region for block: B:135:0x01c2  */
    /* JADX WARN: Removed duplicated region for block: B:138:0x01d5  */
    /* JADX WARN: Removed duplicated region for block: B:140:0x01ac A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean doRecoveryDBWork(final androidx.fragment.app.FragmentActivity r20, java.util.List<com.miui.gallery.trash.TrashBinItem> r21) {
        /*
            Method dump skipped, instructions count: 512
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.trash.TrashUtils.doRecoveryDBWork(androidx.fragment.app.FragmentActivity, java.util.List):boolean");
    }

    public static /* synthetic */ void lambda$doRecoveryDBWork$0(ArrayList arrayList, FragmentActivity fragmentActivity) {
        MediaAndAlbumOperations.addToFavoritesById(fragmentActivity, null, MiscUtil.listToArray(arrayList));
    }

    public static List<IStoragePermissionStrategy.PermissionResult> checkPermissionForRecovery(FragmentActivity fragmentActivity, List<TrashBinItem> list) {
        String filePathUnder;
        TimingTracing.beginTracing(String.format("recoverypermissioncheck{%s}", Long.valueOf(Thread.currentThread().getId())), String.format("count{%s}", Integer.valueOf(list.size())));
        LinkedList linkedList = new LinkedList();
        for (TrashBinItem trashBinItem : list) {
            String trashFilePath = trashBinItem.getTrashFilePath();
            boolean z = trashBinItem.getAlbumLocalId() == -1000;
            if (z) {
                filePathUnder = StorageUtils.getPathInPriorStorage("MIUI/Gallery/cloud/secretAlbum");
            } else {
                filePathUnder = StorageUtils.getFilePathUnder(StorageUtils.getPrimaryStoragePath(), trashBinItem.getAlbumPath());
            }
            List<IStoragePermissionStrategy.PermissionResult> checkRecoveryPermission = TrashManager.checkRecoveryPermission(trashFilePath, BaseFileUtils.concat(filePathUnder, TrashManager.getTargetFileName(trashFilePath, trashBinItem.getFileName(), z)), filePathUnder);
            if (BaseMiscUtil.isValid(checkRecoveryPermission)) {
                linkedList.addAll(checkRecoveryPermission);
            }
        }
        TimingTracing.stopTracing(null);
        return linkedList;
    }

    public static long recoveryLocalItem(FragmentActivity fragmentActivity, TrashBinItem trashBinItem, long j) {
        String trashFilePath = trashBinItem.getTrashFilePath();
        if (TextUtils.isEmpty(trashFilePath)) {
            return -1L;
        }
        ScanResult saveToCloudDB = SaveToCloudUtil.saveToCloudDB(fragmentActivity, new SaveParams.Builder().setSaveFile(new File(trashFilePath)).setSpecifiedModifiedTime(trashBinItem.getMixedDateTime()).setSpecifiedTakenTime(trashBinItem.getMixedDateTime()).setBulkNotify(false).setLocalFlag(-3).setAlbumId(j).setFileName(trashBinItem.getFileName()).setMimeType(trashBinItem.getMimeType()).setCredible(true).build());
        if (saveToCloudDB != null) {
            return saveToCloudDB.getMediaId();
        }
        return -1L;
    }

    public static void doPurge(Context context, List<TrashBinItem> list) {
        if (!BaseMiscUtil.isValid(list) || context == null) {
            return;
        }
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        for (TrashBinItem trashBinItem : list) {
            arrayList.add(Long.valueOf(trashBinItem.getRowId()));
            if (!TextUtils.isEmpty(trashBinItem.getCloudServerId())) {
                arrayList2.add(trashBinItem.getCloudServerId());
            }
        }
        if (BaseMiscUtil.isValid(arrayList2)) {
            String format = String.format("%s IN ('%s')", "serverId", TextUtils.join("','", arrayList2));
            ContentValues contentValues = new ContentValues();
            contentValues.put("serverStatus", "toBePurged");
            SafeDBUtil.safeUpdate(context, GalleryCloudUtils.CLOUD_URI, contentValues, format, (String[]) null);
        }
        if (BaseMiscUtil.isValid(arrayList)) {
            String format2 = String.format("%s IN (%s)", j.c, TextUtils.join(",", arrayList));
            ContentValues contentValues2 = new ContentValues();
            contentValues2.put("status", (Integer) 2);
            TrashManager.getInstance().updateTrashBinItem(contentValues2, format2, null);
        }
        new PurgeFileTask(list).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[0]);
        DefaultLogger.d("TrashUtils", "do purge finish");
    }

    public static void requestServerSide(final Context context, final List<RequestItemInfo> list, final int i) {
        final Context applicationContext = context.getApplicationContext();
        ThreadManager.getMiscPool().submit(new ThreadPool.Job() { // from class: com.miui.gallery.trash.TrashUtils$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public final Object mo1807run(ThreadPool.JobContext jobContext) {
                return TrashUtils.$r8$lambda$DSo9Yk51I_R45VtgE2jX7UN17ck(list, context, applicationContext, i, jobContext);
            }
        });
    }

    public static /* synthetic */ Object lambda$requestServerSide$1(List list, Context context, Context context2, int i, ThreadPool.JobContext jobContext) {
        if (list == null || list.size() <= 0 || context == null) {
            return null;
        }
        purgeOrRecoveryRequest(context2, list, i);
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:60:0x0038  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void purgeOrRecoveryRequest(android.content.Context r4, java.util.List<com.miui.gallery.trash.TrashUtils.RequestItemInfo> r5, int r6) {
        /*
            boolean r0 = com.miui.gallery.util.BaseMiscUtil.isValid(r5)
            if (r0 != 0) goto L7
            return
        L7:
            boolean r0 = com.miui.gallery.util.BaseNetworkUtils.isNetworkConnected()
            if (r0 != 0) goto L2c
            java.util.HashSet r0 = new java.util.HashSet
            r0.<init>()
            java.util.Iterator r5 = r5.iterator()
        L16:
            boolean r1 = r5.hasNext()
            if (r1 == 0) goto L28
            java.lang.Object r1 = r5.next()
            com.miui.gallery.trash.TrashUtils$RequestItemInfo r1 = (com.miui.gallery.trash.TrashUtils.RequestItemInfo) r1
            java.lang.String r1 = r1.mServerId
            r0.add(r1)
            goto L16
        L28:
            com.miui.gallery.trash.TrashJobScheduler.schedule(r4, r6, r0)
            return
        L2c:
            java.util.Iterator r5 = r5.iterator()
            r0 = 0
        L31:
            r1 = r0
        L32:
            boolean r2 = r5.hasNext()
            if (r2 == 0) goto L69
            java.lang.Object r2 = r5.next()
            com.miui.gallery.trash.TrashUtils$RequestItemInfo r2 = (com.miui.gallery.trash.TrashUtils.RequestItemInfo) r2
            if (r1 != 0) goto L45
            org.json.JSONArray r1 = new org.json.JSONArray
            r1.<init>()
        L45:
            org.json.JSONObject r2 = r2.toJSON()
            r1.put(r2)
            int r2 = r1.length()
            r3 = 10
            if (r2 == r3) goto L5a
            boolean r2 = r5.hasNext()
            if (r2 != 0) goto L32
        L5a:
            r2 = 1
            if (r6 == r2) goto L65
            r2 = 2
            if (r6 == r2) goto L61
            goto L31
        L61:
            doRecoveryRequest(r1, r4)
            goto L31
        L65:
            doPurgeRequest(r1, r4)
            goto L31
        L69:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.trash.TrashUtils.purgeOrRecoveryRequest(android.content.Context, java.util.List, int):void");
    }

    public static void doPurgeRequest(JSONArray jSONArray, Context context) {
        postRequest(jSONArray, context, HostManager.TrashBin.getPurgeUrl());
    }

    public static void doRecoveryRequest(JSONArray jSONArray, Context context) {
        postRequest(jSONArray, context, HostManager.TrashBin.getRecoveryUrl());
    }

    public static void postRequest(JSONArray jSONArray, Context context, String str) {
        Account xiaomiAccount;
        GalleryExtendedAuthToken extToken;
        JSONObject optJSONObject;
        JSONArray optJSONArray;
        if (context == null || jSONArray == null || jSONArray.length() == 0 || !BaseGalleryPreferences.CTA.canConnectNetwork() || (xiaomiAccount = AccountHelper.getXiaomiAccount(context)) == null || (extToken = CloudUtils.getExtToken(context, xiaomiAccount)) == null) {
            return;
        }
        try {
            ArrayList arrayList = new ArrayList();
            arrayList.add(new BasicNameValuePair(MiStat.Param.CONTENT, jSONArray.toString()));
            JSONObject postToXiaomi = CloudUtils.postToXiaomi(str, arrayList, null, xiaomiAccount, extToken, 0, false);
            if (postToXiaomi == null || !postToXiaomi.has("data") || (optJSONObject = postToXiaomi.optJSONObject("data")) == null || (optJSONArray = optJSONObject.optJSONArray("succList")) == null) {
                return;
            }
            SyncOwnerAll syncOwnerAll = new SyncOwnerAll(context, xiaomiAccount, extToken);
            for (int i = 0; i < optJSONArray.length(); i++) {
                JSONObject jSONObject = optJSONArray.getJSONObject(i);
                if (jSONObject != null) {
                    syncOwnerAll.handleItem(jSONObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String translateVipName(Context context, String str) {
        str.hashCode();
        char c = 65535;
        switch (str.hashCode()) {
            case 2198156:
                if (str.equals("Free")) {
                    c = 0;
                    break;
                }
                break;
            case 2394258:
                if (str.equals("Mega")) {
                    c = 1;
                    break;
                }
                break;
            case 81831820:
                if (str.equals("Ultra")) {
                    c = 2;
                    break;
                }
                break;
            case 1346201143:
                if (str.equals("Premium")) {
                    c = 3;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return context.getString(R.string.member_menu_vipname_free);
            case 1:
                return context.getString(R.string.member_menu_vipname_mega);
            case 2:
                return context.getString(R.string.member_menu_vipname_ultra);
            case 3:
                return context.getString(R.string.member_menu_vipname_premium);
            default:
                return str;
        }
    }

    public static long getTrashBinStartMs(UserInfo userInfo) {
        long j;
        if (System.currentTimeMillis() - userInfo.mVipEndTime < 0) {
            j = Math.max(userInfo.mDuration, userInfo.mDefaultRetention);
        } else {
            j = userInfo.mDefaultRetention;
        }
        if (j == 0) {
            j = 2592000000L;
        }
        return System.currentTimeMillis() - j;
    }

    public static long getRetentionTime(long j, UserInfo userInfo) {
        if (userInfo == null) {
            return 0L;
        }
        long currentTimeMillis = userInfo.mVipEndTime - System.currentTimeMillis();
        if (currentTimeMillis < 0) {
            currentTimeMillis = 0;
        }
        long currentTimeMillis2 = System.currentTimeMillis() - j;
        if (currentTimeMillis2 < 14400000) {
            currentTimeMillis2 = 0;
        }
        long j2 = userInfo.mDefaultRetention;
        if (j2 <= 0) {
            j2 = 2592000000L;
        }
        long j3 = j2 - currentTimeMillis2;
        long j4 = userInfo.mDuration;
        return currentTimeMillis >= j4 ? j4 - currentTimeMillis2 : (currentTimeMillis >= j4 || currentTimeMillis <= j3) ? j3 : Math.min(j4 - currentTimeMillis2, currentTimeMillis);
    }

    public static void cleanExpireItems() {
        ArrayList arrayList = new ArrayList();
        requestVipInfo();
        UserInfo lastUserInfo = getLastUserInfo();
        List<TrashBinItem> query = GalleryEntityManager.getInstance().query(TrashBinItem.class, "deleteTime < " + getTrashBinStartMs(lastUserInfo) + " AND status != 1", null, null, null);
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("TrashUtils", "cleanExpireItems");
        for (TrashBinItem trashBinItem : query) {
            if (TextUtils.isEmpty(trashBinItem.getCloudServerId())) {
                arrayList.add(Long.valueOf(trashBinItem.getRowId()));
                DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(trashBinItem.getTrashFilePath(), IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
                if (documentFile != null) {
                    documentFile.delete();
                }
            }
        }
        TrashManager.getInstance().removeTrashBinItems(arrayList);
        String str = AccountCache.getAccount() == null ? null : AccountCache.getAccount().name;
        TrashManager.getInstance().handleFilesAndDb(TextUtils.isEmpty(str) ? "cloudServerId IS NOT NULL  AND status != 1" : "cloudServerId IS NOT NULL  AND creatorId IS NOT NULL  AND creatorId != '" + str + "' AND status != 1");
    }

    public static void cleanInvalidTrashFile() {
        File[] listFiles;
        DocumentFile documentFile;
        String filePathUnder = StorageUtils.getFilePathUnder(StorageUtils.getPrimaryStoragePath(), "/Android/data/com.miui.gallery/files/trashBin");
        File file = new File(filePathUnder);
        if (!file.exists() || !file.isDirectory() || (listFiles = file.listFiles()) == null || listFiles.length <= 0) {
            return;
        }
        Set<String> queryExistTrashFilePath = queryExistTrashFilePath();
        queryExistTrashFilePath.addAll(queryExistCloudFileWithPath(filePathUnder));
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("TrashUtils", "cleanInvalidTrashFile");
        for (File file2 : listFiles) {
            if (file2.isFile() && !queryExistTrashFilePath.contains(file2.getAbsolutePath()) && System.currentTimeMillis() - file2.lastModified() >= CoreConstants.MILLIS_IN_ONE_WEEK && (documentFile = StorageSolutionProvider.get().getDocumentFile(file2.getAbsolutePath(), IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag)) != null) {
                documentFile.delete();
            }
        }
    }

    public static Set<String> queryExistTrashFilePath() {
        HashSet hashSet = new HashSet();
        List<TrashBinItem> query = GalleryEntityManager.getInstance().query(TrashBinItem.class, String.format("%s IS NOT NULL AND  %s != ''", "trashFilePath", "trashFilePath"), null, null, null);
        if (BaseMiscUtil.isValid(query)) {
            for (TrashBinItem trashBinItem : query) {
                if (!TextUtils.isEmpty(trashBinItem.getTrashFilePath())) {
                    hashSet.add(trashBinItem.getTrashFilePath());
                }
            }
        }
        return hashSet;
    }

    public static Set<String> queryExistCloudFileWithPath(String str) {
        Locale locale = Locale.US;
        String format = String.format(locale, "(%s LIKE '%s')", "localFile", str + "/%");
        return (Set) SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.Cloud.CLOUD_URI, new String[]{"localFile", "thumbnailFile"}, String.format(locale, "%s AND (%s OR %s)", "(localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))", format, String.format(locale, "(%s LIKE '%s')", "thumbnailFile", str + "/%")), (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Set<String>>() { // from class: com.miui.gallery.trash.TrashUtils.4
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public Set<String> mo1808handle(Cursor cursor) {
                HashSet hashSet = new HashSet();
                if (cursor == null || !cursor.moveToNext()) {
                    return hashSet;
                }
                do {
                    String string = cursor.getString(0);
                    String string2 = cursor.getString(1);
                    if (!TextUtils.isEmpty(string)) {
                        hashSet.add(string);
                    }
                    if (!TextUtils.isEmpty(string2)) {
                        hashSet.add(string2);
                    }
                } while (cursor.moveToNext());
                return hashSet;
            }
        });
    }

    public static void cleanRecoveryTrashFile() {
        GalleryEntityManager galleryEntityManager = GalleryEntityManager.getInstance();
        List query = galleryEntityManager.query(TrashBinItem.class, "status = 2", null, null, null);
        if (BaseMiscUtil.isValid(query)) {
            new PurgeFileTask(query).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Object[0]);
        }
        List query2 = galleryEntityManager.query(TrashBinItem.class, "status = 1", null, null, null);
        if (BaseMiscUtil.isValid(query2)) {
            RecoveryFileTask recoveryFileTask = new RecoveryFileTask(true);
            recoveryFileTask.setOnDoProcessExceptionHandler(new ProcessTask.OnDoProcessExceptionHandler<Void>() { // from class: com.miui.gallery.trash.TrashUtils.5
                @Override // com.miui.gallery.ui.ProcessTask.OnDoProcessExceptionHandler
                public Void handle(Exception exc) {
                    return null;
                }

                @Override // com.miui.gallery.ui.ProcessTask.OnDoProcessExceptionHandler
                public boolean shouldContinueComplete() {
                    return true;
                }

                @Override // com.miui.gallery.ui.ProcessTask.OnDoProcessExceptionHandler
                public boolean shouldHandle(Exception exc) {
                    return true;
                }
            });
            recoveryFileTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, query2);
        }
        cleanCloudRecoveryFile();
    }

    public static void cleanCloudRecoveryFile() {
        SafeDBUtil.safeQuery(GalleryApp.sGetAndroidContext(), GalleryContract.Cloud.CLOUD_URI, new String[]{"localFile", "thumbnailFile", "serverId", j.c}, "localFlag = -3", (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Object>() { // from class: com.miui.gallery.trash.TrashUtils.6
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public Object mo1808handle(Cursor cursor) {
                boolean z;
                if (cursor != null && cursor.moveToFirst()) {
                    ArrayList<ContentProviderOperation> arrayList = new ArrayList<>();
                    do {
                        String string = cursor.getString(0);
                        boolean z2 = true;
                        String string2 = cursor.getString(1);
                        String string3 = cursor.getString(2);
                        long j = cursor.getLong(3);
                        if (TextUtils.isEmpty(string3)) {
                            if (TextUtils.isEmpty(string) || !new File(string).exists()) {
                                arrayList.add(ContentProviderOperation.newDelete(GalleryCloudUtils.CLOUD_URI).withSelection("_id=" + j, null).build());
                            }
                        } else {
                            ContentValues contentValues = new ContentValues();
                            if (TextUtils.isEmpty(string) || new File(string).exists()) {
                                z = false;
                            } else {
                                contentValues.putNull("localFile");
                                z = true;
                            }
                            if (TextUtils.isEmpty(string2) || new File(string2).exists()) {
                                z2 = z;
                            } else {
                                contentValues.putNull("thumbnailFile");
                            }
                            if (z2) {
                                contentValues.putNull("thumbnailFile");
                                contentValues.put("localFlag", (Integer) 0);
                                arrayList.add(ContentProviderOperation.newUpdate(GalleryCloudUtils.CLOUD_URI).withValues(contentValues).withSelection("_id=" + j, null).build());
                            }
                        }
                    } while (cursor.moveToNext());
                    if (!arrayList.isEmpty()) {
                        try {
                            GalleryApp.sGetAndroidContext().getContentResolver().applyBatch("com.miui.gallery.cloud.provider", arrayList);
                        } catch (Exception e) {
                            DefaultLogger.e("TrashUtils", "recovery failed", e);
                        }
                    }
                }
                return 0;
            }
        });
    }

    /* loaded from: classes2.dex */
    public static class UserInfo {
        public long mDefaultRetention;
        public long mDuration;
        public String mLevel;
        public long mRecycleBinStartTime;
        public long mVipEndTime;
        public String mVipName;
        public String mVipPageUrl;

        public UserInfo(String str, String str2, String str3, long j, long j2, long j3, long j4) {
            this.mLevel = str;
            this.mVipName = str2;
            this.mVipPageUrl = str3;
            this.mDuration = j;
            this.mVipEndTime = j2;
            this.mRecycleBinStartTime = j3;
            this.mDefaultRetention = j4;
        }

        public boolean isTopLevel() {
            return "ThirdLevel".equalsIgnoreCase(this.mLevel);
        }

        public String toJSON() {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("level", this.mLevel);
                jSONObject.put("vipName", this.mVipName);
                jSONObject.put("vipPageUrl", this.mVipPageUrl);
                jSONObject.put("recycleBinRetainMs", this.mDuration);
                jSONObject.put("vipExpireTime", this.mVipEndTime);
                jSONObject.put("recycleBinStartTime", this.mRecycleBinStartTime);
                jSONObject.put("defaultRetainMs", this.mDefaultRetention);
                return jSONObject.toString();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public static UserInfo fromJson(String str) {
            if (TextUtils.isEmpty(str)) {
                return null;
            }
            try {
                JSONObject jSONObject = new JSONObject(str);
                return new UserInfo(jSONObject.optString("level"), jSONObject.optString("vipName"), jSONObject.optString("vipPageUrl"), jSONObject.optLong("recycleBinRetainMs"), jSONObject.optLong("vipExpireTime"), jSONObject.optLong("recycleBinStartTime"), jSONObject.optLong("defaultRetainMs"));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        public static UserInfo fromJson(JSONObject jSONObject) {
            if (jSONObject == null) {
                return null;
            }
            try {
                return new UserInfo(jSONObject.optString("level"), jSONObject.optString("vipName"), jSONObject.optString("vipPageUrl"), jSONObject.optLong("recycleBinRetainMs"), jSONObject.optLong("vipExpireTime"), jSONObject.optLong("recycleBinStartTime"), jSONObject.optLong("defaultRetainMs"));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class RequestItemInfo {
        public String mServerId;
        public long mServerTag;

        public RequestItemInfo(String str, long j) {
            this.mServerId = str;
            this.mServerTag = j;
        }

        public JSONObject toJSON() {
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("id", this.mServerId);
                jSONObject.put(nexExportFormat.TAG_FORMAT_TAG, this.mServerTag);
                return jSONObject;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class PurgeFileTask extends AsyncTask {
        public List<TrashBinItem> mPurgeList;

        public PurgeFileTask(List<TrashBinItem> list) {
            this.mPurgeList = list;
        }

        @Override // android.os.AsyncTask
        public Object doInBackground(Object... objArr) {
            if (!BaseMiscUtil.isValid(this.mPurgeList)) {
                DefaultLogger.d("TrashUtils", "empty list when purge");
                return null;
            }
            DefaultLogger.d("TrashUtils", "start purge file");
            long currentTimeMillis = System.currentTimeMillis();
            ArrayList arrayList = new ArrayList();
            ArrayList arrayList2 = new ArrayList();
            String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("TrashUtils", "PurgeFileTask");
            for (TrashBinItem trashBinItem : this.mPurgeList) {
                DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(trashBinItem.getTrashFilePath(), IStoragePermissionStrategy.Permission.DELETE, appendInvokerTag);
                if (documentFile != null) {
                    documentFile.delete();
                    arrayList.add(Long.valueOf(trashBinItem.getRowId()));
                    if (!TextUtils.isEmpty(trashBinItem.getCloudServerId())) {
                        arrayList2.add(new RequestItemInfo(trashBinItem.getCloudServerId(), trashBinItem.getServerTag()));
                    }
                }
            }
            TrashManager.getInstance().removeTrashBinItems(arrayList);
            TrashUtils.requestServerSide(GalleryApp.sGetAndroidContext(), arrayList2, 1);
            DefaultLogger.d("TrashUtils", "finish purge file, file count %d, time cost %d", Integer.valueOf(this.mPurgeList.size()), Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            return null;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Object obj) {
            HashMap hashMap = new HashMap();
            hashMap.put("tip", "403.21.0.1.13769");
            hashMap.put("ref_tip", "403.21.2.1.11281");
            hashMap.put(MiStat.Param.COUNT, Integer.valueOf(this.mPurgeList.size()));
            TimeMonitor.trackTimeMonitor("403.21.0.1.13769", hashMap);
        }
    }

    /* loaded from: classes2.dex */
    public static class RecoveryFileTask extends ProcessTaskForStoragePermissionMiss<List<TrashBinItem>, Void, Void> {
        public RecoveryFileTask(final boolean z) {
            super(new ProcessTask.ProcessCallback<List<TrashBinItem>, Void, Void>() { // from class: com.miui.gallery.trash.TrashUtils.RecoveryFileTask.1
                /* JADX WARN: Can't wrap try/catch for region: R(14:9|(1:11)(1:44)|(1:13)(1:43)|14|(2:16|(3:18|19|20))|21|22|23|24|(5:26|(1:28)(1:39)|29|(1:31)(1:38)|32)(1:40)|33|(2:35|36)(1:37)|20|7) */
                /* JADX WARN: Code restructure failed: missing block: B:86:0x009f, code lost:
                    com.miui.gallery.util.logger.DefaultLogger.e("TrashUtils", "move file from trash failed due to permission missing");
                    r0 = "";
                 */
                @Override // com.miui.gallery.ui.ProcessTask.ProcessCallback
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct code enable 'Show inconsistent code' option in preferences
                */
                public java.lang.Void doProcess(java.util.List<com.miui.gallery.trash.TrashBinItem>[] r20) {
                    /*
                        Method dump skipped, instructions count: 388
                        To view this dump change 'Code comments level' option to 'DEBUG'
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.trash.TrashUtils.RecoveryFileTask.AnonymousClass1.doProcess(java.util.List[]):java.lang.Void");
                }
            }, new ProcessTask.OnCompleteListener() { // from class: com.miui.gallery.trash.TrashUtils.RecoveryFileTask.2
                @Override // com.miui.gallery.ui.ProcessTask.OnCompleteListener
                public void onCompleteProcess(Object obj) {
                    ScannerEngine.getInstance().triggerScan();
                    HashMap hashMap = new HashMap();
                    hashMap.put("tip", "403.21.0.1.13768");
                    hashMap.put("ref_tip", "403.21.2.1.11282");
                    TimeMonitor.trackTimeMonitor("403.21.0.1.13768", hashMap);
                }
            });
        }
    }
}
