package com.nexstreaming.app.common.nexasset.store;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Process;
import android.util.Log;
import ch.qos.logback.classic.pattern.CallerDataConverter;
import com.google.gson_nex.Gson;
import com.google.gson_nex.JsonIOException;
import com.google.gson_nex.JsonSyntaxException;
import com.nexstreaming.app.common.nexasset.assetpackage.AssetPackageReader;
import com.nexstreaming.app.common.nexasset.assetpackage.InstallSourceType;
import com.nexstreaming.app.common.nexasset.assetpackage.b;
import com.nexstreaming.app.common.nexasset.assetpackage.c;
import com.nexstreaming.app.common.nexasset.assetpackage.f;
import com.nexstreaming.app.common.nexasset.store.json.AssetStoreAPIData;
import com.nexstreaming.app.common.task.Task;
import com.nexstreaming.app.common.util.p;
import com.nexstreaming.kminternal.kinemaster.config.EditorGlobal;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URI;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: classes3.dex */
public class AssetLocalInstallDB {
    public static final int ASSET_UNINSTALL_FINISHED = 1;
    public static final int ASSET_UNINSTALL_NOT_YET = 0;
    private static final String TAG = "AssetLocalInstallDB";
    private static String assetStoreRootPath = null;
    private static final boolean commAS = false;
    private static AssetLocalInstallDB instance;
    private static String localFeaturedPath;
    private static String localRootPath;
    private static final Executor sInstallThreadPool = Executors.newSingleThreadExecutor();
    private static int supportedMimeType;
    private Context mContext;
    private List<String> readyToDeletePackages;
    private List<String> readyToInstallPackages;
    private List<String> readyToLoadPackages;
    private boolean installing = false;
    private Object m_assetdbLock = new Object();
    private Map<Integer, ArrayList<remoteAssetItem>> mFeaturedList = new HashMap();

    /* loaded from: classes3.dex */
    public static class remoteAssetItem {
        public String category;
        public String id;
        public int idx;
        public String name;
        public String thumbnailPath;
        public String thumbnailURL;
    }

    public AssetLocalInstallDB(Context context) {
        this.mContext = null;
        if (assetStoreRootPath == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(EditorGlobal.e().getAbsolutePath());
            String str = File.separator;
            sb.append(str);
            sb.append(".nexassets");
            sb.append(str);
            sb.append(context.getPackageName());
            assetStoreRootPath = sb.toString();
        }
        if (localRootPath == null) {
            localRootPath = context.getFilesDir().getAbsolutePath() + File.separator + "assets";
            File file = new File(localRootPath);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        if (localFeaturedPath == null) {
            localFeaturedPath = context.getFilesDir().getAbsolutePath() + File.separator + "featured";
            File file2 = new File(localFeaturedPath);
            if (!file2.exists()) {
                file2.mkdirs();
            }
        }
        this.readyToInstallPackages = new ArrayList();
        this.readyToDeletePackages = new ArrayList();
        this.readyToLoadPackages = new ArrayList();
        this.mContext = context;
    }

    public static AssetLocalInstallDB getInstance(Context context) {
        if (instance == null) {
            instance = new AssetLocalInstallDB(context.getApplicationContext());
        }
        return instance;
    }

    private boolean isSamePath() {
        return localRootPath.compareTo(assetStoreRootPath) == 0;
    }

    public static void setAssetStoreRootPath(String str) {
        assetStoreRootPath = str;
    }

    public static String getAssetStoreRootPath() {
        return assetStoreRootPath;
    }

    public static void setMimeType(int i) {
        supportedMimeType = i;
    }

    private String getThumbnailOutputPath(String str) {
        String str2 = localRootPath;
        if (str2.startsWith(this.mContext.getFilesDir().getAbsolutePath())) {
            str2 = this.mContext.getFilesDir().getAbsolutePath() + File.separator + "thumb";
        }
        File file = new File(str2);
        if (!file.exists()) {
            file.mkdirs();
        }
        return str2 + File.separator + str + ".jpg";
    }

    public static void setInstalledAssetPath(String str) {
        localRootPath = str;
        File file = new File(localRootPath);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static String getInstalledAssetPath() {
        return localRootPath;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:35:0x0053 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r2v10, types: [java.io.FileOutputStream] */
    /* JADX WARN: Type inference failed for: r2v13 */
    /* JADX WARN: Type inference failed for: r2v14 */
    /* JADX WARN: Type inference failed for: r2v15 */
    /* JADX WARN: Type inference failed for: r2v2, types: [android.graphics.Bitmap$Config] */
    /* JADX WARN: Type inference failed for: r2v3 */
    /* JADX WARN: Type inference failed for: r2v6, types: [java.io.FileOutputStream] */
    /* JADX WARN: Type inference failed for: r2v7 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private void createDummyIcon(java.lang.String r6) {
        /*
            r5 = this;
            java.io.File r0 = new java.io.File
            java.lang.String r6 = r5.getThumbnailOutputPath(r6)
            r0.<init>(r6)
            r6 = 576(0x240, float:8.07E-43)
            int[] r1 = new int[r6]
            r2 = 0
        Le:
            if (r2 >= r6) goto L17
            r3 = -16777216(0xffffffffff000000, float:-1.7014118E38)
            r1[r2] = r3
            int r2 = r2 + 1
            goto Le
        L17:
            r6 = 0
            android.graphics.Bitmap$Config r2 = android.graphics.Bitmap.Config.ARGB_8888
            r3 = 32
            r4 = 18
            android.graphics.Bitmap r1 = android.graphics.Bitmap.createBitmap(r1, r3, r4, r2)
            r0.createNewFile()     // Catch: java.lang.Throwable -> L39 java.io.IOException -> L3d java.io.FileNotFoundException -> L46
            java.io.FileOutputStream r2 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> L39 java.io.IOException -> L3d java.io.FileNotFoundException -> L46
            r2.<init>(r0)     // Catch: java.lang.Throwable -> L39 java.io.IOException -> L3d java.io.FileNotFoundException -> L46
            android.graphics.Bitmap$CompressFormat r6 = android.graphics.Bitmap.CompressFormat.PNG     // Catch: java.io.IOException -> L35 java.io.FileNotFoundException -> L37 java.lang.Throwable -> L50
            r0 = 100
            r1.compress(r6, r0, r2)     // Catch: java.io.IOException -> L35 java.io.FileNotFoundException -> L37 java.lang.Throwable -> L50
        L31:
            r2.close()     // Catch: java.io.IOException -> L4f
            goto L4f
        L35:
            r6 = move-exception
            goto L40
        L37:
            r6 = move-exception
            goto L49
        L39:
            r0 = move-exception
            r2 = r6
            r6 = r0
            goto L51
        L3d:
            r0 = move-exception
            r2 = r6
            r6 = r0
        L40:
            r6.printStackTrace()     // Catch: java.lang.Throwable -> L50
            if (r2 == 0) goto L4f
            goto L31
        L46:
            r0 = move-exception
            r2 = r6
            r6 = r0
        L49:
            r6.printStackTrace()     // Catch: java.lang.Throwable -> L50
            if (r2 == 0) goto L4f
            goto L31
        L4f:
            return
        L50:
            r6 = move-exception
        L51:
            if (r2 == 0) goto L56
            r2.close()     // Catch: java.io.IOException -> L56
        L56:
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.app.common.nexasset.store.AssetLocalInstallDB.createDummyIcon(java.lang.String):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x003d, code lost:
        if (r5 == null) goto L11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x003f, code lost:
        r5.close();
        r5 = r5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0047, code lost:
        if (r5 == null) goto L11;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.io.File makeThumbnail(android.graphics.Bitmap r4, int r5) {
        /*
            r3 = this;
            java.io.File r0 = new java.io.File
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = ""
            r1.append(r2)
            r1.append(r5)
            java.lang.String r5 = r1.toString()
            java.lang.String r5 = r3.getThumbnailOutputPath(r5)
            r0.<init>(r5)
            r5 = 0
            r0.createNewFile()     // Catch: java.lang.Throwable -> L37 java.io.IOException -> L39 java.io.FileNotFoundException -> L43
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> L37 java.io.IOException -> L39 java.io.FileNotFoundException -> L43
            r1.<init>(r0)     // Catch: java.lang.Throwable -> L37 java.io.IOException -> L39 java.io.FileNotFoundException -> L43
            android.graphics.Bitmap$CompressFormat r5 = android.graphics.Bitmap.CompressFormat.PNG     // Catch: java.lang.Throwable -> L2e java.io.IOException -> L31 java.io.FileNotFoundException -> L34
            r2 = 100
            r4.compress(r5, r2, r1)     // Catch: java.lang.Throwable -> L2e java.io.IOException -> L31 java.io.FileNotFoundException -> L34
            r1.close()     // Catch: java.io.IOException -> L4a
            goto L4a
        L2e:
            r4 = move-exception
            r5 = r1
            goto L4b
        L31:
            r4 = move-exception
            r5 = r1
            goto L3a
        L34:
            r4 = move-exception
            r5 = r1
            goto L44
        L37:
            r4 = move-exception
            goto L4b
        L39:
            r4 = move-exception
        L3a:
            r4.printStackTrace()     // Catch: java.lang.Throwable -> L37
            if (r5 == 0) goto L4a
        L3f:
            r5.close()     // Catch: java.io.IOException -> L4a
            goto L4a
        L43:
            r4 = move-exception
        L44:
            r4.printStackTrace()     // Catch: java.lang.Throwable -> L37
            if (r5 == 0) goto L4a
            goto L3f
        L4a:
            return r0
        L4b:
            if (r5 == 0) goto L50
            r5.close()     // Catch: java.io.IOException -> L50
        L50:
            throw r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.app.common.nexasset.store.AssetLocalInstallDB.makeThumbnail(android.graphics.Bitmap, int):java.io.File");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x00c8, code lost:
        if (r2 == null) goto L27;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x00ca, code lost:
        r2.close();
        r2 = r2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x00d2, code lost:
        if (r2 == null) goto L27;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.io.File copyThumbnail(java.lang.String r6) {
        /*
            Method dump skipped, instructions count: 227
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.app.common.nexasset.store.AssetLocalInstallDB.copyThumbnail(java.lang.String):java.io.File");
    }

    private void createDummy(String str) throws IOException {
        if (isSamePath()) {
            return;
        }
        FileOutputStream fileOutputStream = new FileOutputStream(new File(assetStoreRootPath, str));
        fileOutputStream.write(new byte[]{80, 75});
        fileOutputStream.close();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkInstallFile(String str, String str2) {
        boolean z;
        File file = new File(str, str2 + ".json");
        int assetIdxInJson = getAssetIdxInJson(str2);
        boolean z2 = false;
        if (assetIdxInJson != Integer.parseInt(str2)) {
            Log.d(TAG, "invalid " + str2 + ".json. idx=" + assetIdxInJson);
            z = false;
        } else {
            z = true;
        }
        new File(str, "." + str2);
        File file2 = new File(str, str2 + ".jpg");
        if (!file2.isFile()) {
            Log.d(TAG, "thumbnail not found (" + str2 + ")");
        } else {
            z2 = z;
        }
        if (!z2) {
            if (file.isFile()) {
                file.delete();
            }
            if (file2.isFile()) {
                file2.delete();
            }
        }
        return z2;
    }

    public int checkStoreInstall() {
        String[] list;
        this.readyToInstallPackages.clear();
        String str = assetStoreRootPath;
        File file = new File(str);
        if (file.isDirectory()) {
            for (String str2 : file.list()) {
                File file2 = new File(str, str2);
                if (file2.isFile() && str2.endsWith(".zip") && file2.length() > 2) {
                    if (checkInstallFile(str, str2.substring(0, str2.length() - 4))) {
                        this.readyToInstallPackages.add(str2);
                    } else {
                        file2.delete();
                    }
                }
            }
        }
        return this.readyToInstallPackages.size();
    }

    public List<String> getReadyToInstallPackages() {
        checkStoreInstall();
        return this.readyToInstallPackages;
    }

    public void checkStore() {
        String[] list;
        this.readyToDeletePackages.clear();
        this.readyToLoadPackages.clear();
        File file = new File(localRootPath);
        if (file.isDirectory()) {
            for (String str : file.list()) {
                File file2 = new File(localRootPath, str);
                if (file2.isDirectory()) {
                    if (!new File(assetStoreRootPath + File.separator + str + ".zip").isFile()) {
                        this.readyToDeletePackages.add(str);
                        try {
                            uninstallPackage(Integer.parseInt(str));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (file2.length() <= 2) {
                        this.readyToLoadPackages.add(str);
                    }
                }
            }
        }
    }

    public boolean isInstallingPackages() {
        return this.installing;
    }

    public Task installPackageAsync(int i) {
        final Task task = new Task();
        this.installing = true;
        new AsyncTask<Integer, Void, Exception>() { // from class: com.nexstreaming.app.common.nexasset.store.AssetLocalInstallDB.1
            @Override // android.os.AsyncTask
            public void onPreExecute() {
                super.onPreExecute();
            }

            @Override // android.os.AsyncTask
            public Exception doInBackground(Integer... numArr) {
                String[] strArr;
                String[] strArr2;
                String str;
                String[] list;
                Process.setThreadPriority(0);
                int intValue = numArr[0].intValue();
                if (intValue != 0) {
                    if (!AssetLocalInstallDB.this.checkInstallFile(AssetLocalInstallDB.assetStoreRootPath, "" + intValue)) {
                        Log.d(AssetLocalInstallDB.TAG, "download asset package not found. AssetIdx=" + intValue);
                        return new Resources.NotFoundException("download asset package not found");
                    }
                    strArr = new String[1];
                    strArr2 = new String[]{"" + intValue + ".zip"};
                } else {
                    ArrayList arrayList = new ArrayList();
                    String str2 = AssetLocalInstallDB.assetStoreRootPath;
                    File file = new File(str2);
                    if (file.isDirectory()) {
                        for (String str3 : file.list()) {
                            File file2 = new File(str2, str3);
                            if (file2.isFile() && str3.endsWith(".zip") && file2.length() > 2) {
                                if (AssetLocalInstallDB.this.checkInstallFile(str2, str3.substring(0, str3.length() - 4))) {
                                    arrayList.add(str3);
                                } else {
                                    file2.delete();
                                }
                            }
                        }
                    }
                    if (arrayList.size() == 0) {
                        Log.d(AssetLocalInstallDB.TAG, "download asset package not found.");
                        return new Resources.NotFoundException("download asset package not found");
                    }
                    strArr2 = new String[arrayList.size()];
                    strArr = new String[arrayList.size()];
                    for (int i2 = 0; i2 < arrayList.size(); i2++) {
                        strArr2[i2] = (String) arrayList.get(i2);
                    }
                }
                int i3 = 0;
                while (i3 < strArr2.length) {
                    String substring = strArr2[i3].substring(0, str.length() - 4);
                    try {
                        int parseInt = Integer.parseInt(substring);
                        b b = c.a().b(parseInt);
                        if (b != null) {
                            Log.d(AssetLocalInstallDB.TAG, "already installed Asset=" + parseInt);
                            if (b.getInstallSourceType() == InstallSourceType.STORE) {
                                try {
                                    AssetLocalInstallDB.this.uninstallPackage(parseInt, false);
                                } catch (Exception unused) {
                                }
                            } else {
                                Log.d(AssetLocalInstallDB.TAG, "installed Asset is not store type. idx=" + parseInt);
                            }
                        }
                    } catch (NumberFormatException unused2) {
                        Log.d(AssetLocalInstallDB.TAG, "baseId is not Integer baseId=" + substring);
                    }
                    File copyThumbnail = AssetLocalInstallDB.this.copyThumbnail(substring);
                    int i4 = i3 + 1;
                    task.setProgress(i4, strArr2.length);
                    try {
                        try {
                            AssetLocalInstallDB.this.installPackage(strArr2[i3], substring, copyThumbnail, task, false, strArr[i3]);
                            Log.i(AssetLocalInstallDB.TAG, "install asset completed : asset = [" + strArr2[i3] + "]");
                            i3 = i4;
                        } catch (IOException e) {
                            Log.d(AssetLocalInstallDB.TAG, "install asset failed : asset = [" + strArr2[i3] + "]");
                            new File(AssetLocalInstallDB.assetStoreRootPath, strArr2[i3]).delete();
                            return e;
                        }
                    } catch (FileNotFoundException e2) {
                        Log.d(AssetLocalInstallDB.TAG, "install asset failed : asset = [" + strArr2[i3] + "]");
                        return e2;
                    }
                }
                return null;
            }

            @Override // android.os.AsyncTask
            public void onPostExecute(Exception exc) {
                super.onPostExecute((AnonymousClass1) exc);
                AssetLocalInstallDB.this.installing = false;
                if (exc != null) {
                    task.sendFailure(new InstallTaskError("asset_install_failed", exc));
                } else {
                    task.signalEvent(Task.Event.COMPLETE);
                }
            }
        }.executeOnExecutor(sInstallThreadPool, Integer.valueOf(i));
        return task;
    }

    /* loaded from: classes3.dex */
    public static class internalStoreAssetInfo implements StoreAssetInfo {
        private AssetStoreAPIData.AssetInfo info;

        @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
        public Map<String, String> getAssetDescriptionMap() {
            return null;
        }

        public internalStoreAssetInfo(AssetStoreAPIData.AssetInfo assetInfo) {
            this.info = assetInfo;
        }

        @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
        public int getAssetIndex() {
            return this.info.idx;
        }

        @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
        public String getAssetId() {
            return this.info.asset_id;
        }

        @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
        public String getAssetTitle() {
            return this.info.title;
        }

        @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
        public String getAssetDescription() {
            return this.info.description;
        }

        @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
        public int getCategoryIndex() {
            return this.info.category_idx;
        }

        @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
        public String getCategoryAliasName() {
            return this.info.category_aliasName;
        }

        @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
        public String getCategoryIconURL() {
            return this.info.categoryimagePath;
        }

        @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
        public int getSubCategoryIndex() {
            return this.info.subcategory_idx;
        }

        @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
        public Map<String, String> getSubCategoryNameMap() {
            HashMap hashMap = new HashMap();
            List<AssetStoreAPIData.LangString> list = this.info.subcategoryName;
            if (list != null) {
                for (AssetStoreAPIData.LangString langString : list) {
                    hashMap.put(langString.language_code.toLowerCase(Locale.ENGLISH), langString.string_title);
                }
            }
            return hashMap;
        }

        @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
        public Map<String, String> getAssetNameMap() {
            HashMap hashMap = new HashMap();
            List<AssetStoreAPIData.LangString> list = this.info.assetName;
            if (list != null) {
                for (AssetStoreAPIData.LangString langString : list) {
                    hashMap.put(langString.language_code.toLowerCase(Locale.ENGLISH), langString.string_title);
                }
            }
            return hashMap;
        }

        @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
        public String getAssetPackageDownloadURL() {
            return this.info.asset_filepath;
        }

        @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
        public String getPriceType() {
            return this.info.priceType;
        }

        @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
        public String getAssetThumbnailURL() {
            return this.info.thumbnail_path;
        }

        @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
        public String getAssetThumbnailURL_L() {
            return this.info.thumbnail_path_l;
        }

        @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
        public String getAssetThumbnailURL_S() {
            return this.info.thumbnail_path_s;
        }

        @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
        public List<String> getThumbnailPaths() {
            ArrayList arrayList = new ArrayList();
            List<AssetStoreAPIData.ThumbInfo> list = this.info.thumb;
            if (list != null) {
                for (AssetStoreAPIData.ThumbInfo thumbInfo : list) {
                    arrayList.add(thumbInfo.file_path);
                }
            }
            return arrayList;
        }

        @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
        public int getAssetVersion() {
            return this.info.asset_version;
        }

        @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
        public int getAssetScopeVersion() {
            return this.info.asset_sversion;
        }

        @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
        public int getAssetFilesize() {
            return this.info.asset_filesize;
        }

        @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
        public String getAssetVideoURL() {
            return this.info.videoclip_path;
        }

        @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
        public int getUpdateTime() {
            return this.info.update_time;
        }

        @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
        public String getSubCategoryAliasName() {
            return this.info.category_aliasName;
        }

        @Override // com.nexstreaming.app.common.nexasset.store.StoreAssetInfo
        public long getExpireTime() {
            return this.info.expire_time;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0045 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:16:0x0046  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private int getAssetIdxInJson(java.lang.String r6) {
        /*
            r5 = this;
            java.io.File r0 = new java.io.File
            java.lang.String r1 = com.nexstreaming.app.common.nexasset.store.AssetLocalInstallDB.assetStoreRootPath
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r6)
            java.lang.String r6 = ".json"
            r2.append(r6)
            java.lang.String r6 = r2.toString()
            r0.<init>(r1, r6)
            boolean r6 = r0.isFile()
            r1 = -1
            if (r6 == 0) goto L49
            r6 = 0
            java.io.FileInputStream r2 = new java.io.FileInputStream     // Catch: java.lang.Throwable -> L42
            r2.<init>(r0)     // Catch: java.lang.Throwable -> L42
            com.google.gson_nex.Gson r0 = new com.google.gson_nex.Gson     // Catch: java.lang.Throwable -> L3d
            r0.<init>()     // Catch: java.lang.Throwable -> L3d
            java.io.InputStreamReader r3 = new java.io.InputStreamReader     // Catch: java.lang.Throwable -> L3d
            r3.<init>(r2)     // Catch: java.lang.Throwable -> L3d
            java.lang.Class<com.nexstreaming.app.common.nexasset.store.json.AssetStoreAPIData$AssetInfo> r4 = com.nexstreaming.app.common.nexasset.store.json.AssetStoreAPIData.AssetInfo.class
            java.lang.Object r0 = r0.fromJson(r3, r4)     // Catch: java.lang.Throwable -> L3d
            com.nexstreaming.app.common.nexasset.store.json.AssetStoreAPIData$AssetInfo r0 = (com.nexstreaming.app.common.nexasset.store.json.AssetStoreAPIData.AssetInfo) r0     // Catch: java.lang.Throwable -> L3d
            r2.close()     // Catch: java.lang.Throwable -> L3b
            goto L43
        L3b:
            r6 = r0
            goto L42
        L3d:
            r0 = move-exception
            r2.close()     // Catch: java.lang.Throwable -> L42
            throw r0     // Catch: java.lang.Throwable -> L42
        L42:
            r0 = r6
        L43:
            if (r0 != 0) goto L46
            return r1
        L46:
            int r6 = r0.idx
            return r6
        L49:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.app.common.nexasset.store.AssetLocalInstallDB.getAssetIdxInJson(java.lang.String):int");
    }

    private StoreAssetInfo parseStoreAssetInfo(File file, String str) {
        AssetStoreAPIData.AssetInfo assetInfo;
        String str2 = assetStoreRootPath;
        File file2 = new File(str2, str + ".json");
        if (file2.isFile()) {
            AssetStoreAPIData.AssetInfo assetInfo2 = null;
            try {
                FileInputStream fileInputStream = new FileInputStream(file2);
                assetInfo = (AssetStoreAPIData.AssetInfo) new Gson().fromJson((Reader) new InputStreamReader(fileInputStream), (Class<Object>) AssetStoreAPIData.AssetInfo.class);
                try {
                    fileInputStream.close();
                } catch (FileNotFoundException | IOException unused) {
                    assetInfo2 = assetInfo;
                    assetInfo = assetInfo2;
                    file2.delete();
                    return new internalStoreAssetInfo(assetInfo);
                }
            } catch (FileNotFoundException | IOException unused2) {
            }
            file2.delete();
            return new internalStoreAssetInfo(assetInfo);
        }
        Log.d(TAG, "jsonFile file not found!");
        AssetStoreAPIData.AssetInfo assetInfo3 = new AssetStoreAPIData.AssetInfo();
        try {
            AssetPackageReader b = AssetPackageReader.b(file, str);
            if (b != null) {
                if (b.e() != null) {
                    assetInfo3.title = b.e().get("en");
                } else {
                    assetInfo3.title = str;
                }
                assetInfo3.idx = Integer.parseInt(str);
                assetInfo3.asset_id = str;
                b.close();
            } else {
                assetInfo3.idx = Integer.parseInt(str);
                assetInfo3.asset_id = str;
                assetInfo3.title = str;
            }
        } catch (IOException unused3) {
            assetInfo3.idx = Integer.parseInt(str);
            assetInfo3.asset_id = str;
            assetInfo3.title = str;
        }
        return new internalStoreAssetInfo(assetInfo3);
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x00e4  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x00fd  */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0124  */
    /* JADX WARN: Removed duplicated region for block: B:33:0x0142  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x015b  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x017b  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x0199  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean installPackageSync(int r14) {
        /*
            Method dump skipped, instructions count: 527
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.app.common.nexasset.store.AssetLocalInstallDB.installPackageSync(int):boolean");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void installPackage(String str, String str2, File file, Task task, boolean z, String str3) throws IOException {
        Log.d(TAG, "installPackage() called with: item = [" + str + "], thumbFile = [" + file + "]");
        File file2 = new File(assetStoreRootPath, str);
        if (file2.exists()) {
            if (p.a(file2)) {
                z = false;
            }
            File file3 = new File(localRootPath, str2);
            if (z) {
                try {
                    if (str3.compareTo(AssetStoreClient.none) == 0) {
                        Log.d(TAG, "installPackage() fail. key not found.");
                        file2.delete();
                        String str4 = assetStoreRootPath;
                        File file4 = new File(str4, "." + str2);
                        if (!file4.isFile()) {
                            return;
                        }
                        file4.delete();
                        return;
                    }
                } catch (IOException e) {
                    Log.w(TAG, "installPackage unzip error", e);
                    if (file3.exists()) {
                        file3.delete();
                    }
                    throw new IOException(e);
                }
            }
            try {
                try {
                    try {
                        unzip(file2, file3, task, z, str3);
                        String str5 = assetStoreRootPath;
                        File file5 = new File(str5, "." + str2);
                        if (file5.isFile()) {
                            file5.delete();
                        }
                    } catch (NoSuchPaddingException e2) {
                        e2.printStackTrace();
                        if (file3.exists()) {
                            file3.delete();
                        }
                    }
                } catch (NoSuchAlgorithmException e3) {
                    e3.printStackTrace();
                    if (file3.exists()) {
                        file3.delete();
                    }
                }
            } catch (InvalidKeyException e4) {
                e4.printStackTrace();
                if (file3.exists()) {
                    file3.delete();
                }
            }
            synchronized (this.m_assetdbLock) {
                StoreAssetInfo parseStoreAssetInfo = parseStoreAssetInfo(file3, str2);
                if (parseStoreAssetInfo != null) {
                    Log.d(TAG, "install StoreAssetItem, idx=" + parseStoreAssetInfo.getAssetIndex() + ", id=" + parseStoreAssetInfo.getAssetId() + ", SDKLevel=" + parseStoreAssetInfo.getAssetScopeVersion() + ", version=" + parseStoreAssetInfo.getAssetVersion());
                }
                c.a(this.mContext).a(file3, file, parseStoreAssetInfo);
                file2.delete();
            }
            return;
        }
        throw new FileNotFoundException("Not found asset file");
    }

    public f getAssetInstalledItemInfoByAssetIdx(int i) {
        Iterator<? extends f> it = c.a(this.mContext).c(i).iterator();
        if (it.hasNext()) {
            return it.next();
        }
        return null;
    }

    public List<? extends b> getAssetInstalledDownloadItemItems() {
        ArrayList arrayList = new ArrayList();
        for (b bVar : c.a(this.mContext).b()) {
            if (bVar.getInstallSourceType() == InstallSourceType.STORE) {
                arrayList.add(bVar);
            }
        }
        return arrayList;
    }

    public void checkInstallDB() {
        String[] list;
        int i = 0;
        for (b bVar : c.a(this.mContext).b()) {
            if (bVar.getInstallSourceType() == InstallSourceType.STORE) {
                i++;
            }
        }
        if (i == 0) {
            Log.d(TAG, "StoreAsset NotFound!");
            String str = assetStoreRootPath;
            File file = new File(str);
            if (!file.isDirectory()) {
                return;
            }
            for (String str2 : file.list()) {
                File file2 = new File(str, str2);
                if (file2.isFile() && (!str2.endsWith(".zip") || file2.length() <= 2)) {
                    file2.delete();
                }
            }
        }
    }

    public void uninstallFromAssetStoreApp() {
        String[] list;
        String str = assetStoreRootPath;
        File file = new File(str);
        if (file.isDirectory()) {
            for (String str2 : file.list()) {
                if (str2.endsWith(".del")) {
                    String substring = str2.substring(0, str2.length() - 4);
                    Log.d(TAG, "uninstallFromAssetStoreApp idx=" + substring);
                    try {
                        uninstallPackage(Integer.parseInt(substring));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    new File(str, str2).delete();
                }
            }
        }
    }

    private void deleteDir(File file) {
        File[] listFiles = file.listFiles();
        if (listFiles != null) {
            for (File file2 : listFiles) {
                deleteDir(file2);
            }
        }
        file.delete();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1 */
    /* JADX WARN: Type inference failed for: r0v10 */
    /* JADX WARN: Type inference failed for: r0v2, types: [int] */
    public int uninstallPackage(int i, boolean z) throws Exception {
        Log.d(TAG, "uninstallPackage() called with: assetIdx = [" + i + "]");
        ?? r0 = 1;
        boolean z2 = true;
        if (i == 0 || i == 1) {
            return 1;
        }
        synchronized (this.m_assetdbLock) {
            f assetInstalledItemInfoByAssetIdx = getAssetInstalledItemInfoByAssetIdx(i);
            if (assetInstalledItemInfoByAssetIdx != null) {
                File file = new File(URI.create(assetInstalledItemInfoByAssetIdx.getPackageURI()).getPath());
                File file2 = new File(getThumbnailOutputPath("" + i));
                if (file.isDirectory()) {
                    deleteDir(file);
                } else {
                    file.delete();
                }
                if (file2.isFile()) {
                    if (z) {
                        file2.delete();
                    } else if (!isSamePath()) {
                        file2.delete();
                    }
                }
                if (z) {
                    String str = assetStoreRootPath;
                    File file3 = new File(str, i + ".zip");
                    if (file3.isFile()) {
                        file3.delete();
                    }
                }
                if (file.exists()) {
                    z2 = file.delete();
                }
                c.a(this.mContext).a(i);
                r0 = z2;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("uninstallPackage() returned: ");
            int i2 = r0 == true ? 1 : 0;
            int i3 = r0 == true ? 1 : 0;
            int i4 = r0 == true ? 1 : 0;
            int i5 = r0 == true ? 1 : 0;
            sb.append(i2);
            Log.d(TAG, sb.toString());
        }
        return r0;
    }

    public int uninstallPackage(int i) throws Exception {
        return uninstallPackage(i, true);
    }

    /* loaded from: classes3.dex */
    public static final class InstallTaskError implements Task.TaskError {
        public final Exception exception;
        private final String message;

        public InstallTaskError(String str, Exception exc) {
            this.message = str;
            this.exception = exc;
        }

        public InstallTaskError(String str) {
            this.message = str;
            this.exception = null;
        }

        @Override // com.nexstreaming.app.common.task.Task.TaskError
        public Exception getException() {
            return this.exception;
        }

        @Override // com.nexstreaming.app.common.task.Task.TaskError
        public String getMessage() {
            return this.message;
        }

        @Override // com.nexstreaming.app.common.task.Task.TaskError
        public String getLocalizedMessage(Context context) {
            return this.message;
        }
    }

    /* JADX WARN: Finally extract failed */
    private static void unzip(File file, File file2, Task task, boolean z, String str) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        ZipInputStream zipInputStream;
        String str2;
        String str3;
        String str4;
        StringBuilder sb = new StringBuilder();
        sb.append("Unzipping '");
        sb.append(file);
        String str5 = "' to '";
        sb.append(str5);
        sb.append(file2);
        String str6 = "'";
        sb.append(str6);
        Log.d(TAG, sb.toString());
        String str7 = "Failed to create directory: ";
        if (!file2.mkdirs() && !file2.exists()) {
            throw new IOException(str7 + file2);
        }
        long length = file.length();
        task.setProgress(0, 100);
        if (length <= 0) {
            throw new IOException("Failed because file size is zero");
        }
        if (z) {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(2, new SecretKeySpec(str.getBytes(), "AES"));
            zipInputStream = new ZipInputStream(new CipherInputStream(new FileInputStream(file), cipher));
        } else {
            zipInputStream = new ZipInputStream(new FileInputStream(file));
        }
        long j = 0;
        while (true) {
            try {
                ZipEntry nextEntry = zipInputStream.getNextEntry();
                if (nextEntry != null) {
                    String name = nextEntry.getName();
                    if (name.contains(CallerDataConverter.DEFAULT_RANGE_DELIMITER)) {
                        throw new IOException("Relative paths not allowed");
                    }
                    File file3 = new File(file2, name);
                    if (nextEntry.isDirectory()) {
                        if (!file3.mkdirs() && !file3.exists()) {
                            throw new IOException(str7 + file3);
                        }
                        Log.d(TAG, "  - unzip: made folder '" + name + str6);
                        str4 = str6;
                        str2 = str7;
                        str3 = str5;
                    } else {
                        StringBuilder sb2 = new StringBuilder();
                        str2 = str7;
                        sb2.append("  - unzip: unzipping file '");
                        sb2.append(name);
                        sb2.append("' ");
                        str3 = str5;
                        str4 = str6;
                        sb2.append(nextEntry.getCompressedSize());
                        sb2.append("->");
                        sb2.append(nextEntry.getSize());
                        sb2.append(" (");
                        sb2.append(nextEntry.getMethod());
                        sb2.append(")");
                        Log.d(TAG, sb2.toString());
                        FileOutputStream fileOutputStream = new FileOutputStream(file3);
                        copy(zipInputStream, fileOutputStream);
                        com.nexstreaming.app.common.util.b.a(fileOutputStream);
                        j += nextEntry.getCompressedSize();
                        int i = (int) ((100 * j) / length);
                        if (i >= 100) {
                            i = 99;
                        }
                        task.setProgress(i, 100);
                    }
                    str5 = str3;
                    str6 = str4;
                    str7 = str2;
                } else {
                    com.nexstreaming.app.common.util.b.a(zipInputStream);
                    task.setProgress(100, 100);
                    Log.d(TAG, "Unzipping DONE for: '" + file + str5 + file2 + str6);
                    return;
                }
            } catch (Throwable th) {
                com.nexstreaming.app.common.util.b.a(zipInputStream);
                throw th;
            }
        }
    }

    public File getUnzipFolder(int i) {
        String str = localRootPath;
        return new File(str, "" + i);
    }

    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] bArr = new byte[4096];
        while (true) {
            int read = inputStream.read(bArr);
            if (-1 != read) {
                outputStream.write(bArr, 0, read);
            } else {
                return;
            }
        }
    }

    private static String readFromFile(File file) {
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(bufferedInputStream));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String readLine = bufferedReader.readLine();
                if (readLine != null) {
                    sb.append(readLine);
                    sb.append("\n");
                } else {
                    bufferedInputStream.close();
                    return sb.toString();
                }
            }
        } catch (FileNotFoundException e) {
            return e.getMessage();
        } catch (IOException e2) {
            return e2.getMessage();
        }
    }

    private boolean copyFile(File file, String str) {
        if (file == null || !file.exists()) {
            return false;
        }
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            FileOutputStream fileOutputStream = new FileOutputStream(str);
            byte[] bArr = new byte[1024];
            while (true) {
                int read = fileInputStream.read(bArr, 0, 1024);
                if (read == -1) {
                    break;
                }
                fileOutputStream.write(bArr, 0, read);
            }
            fileOutputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /* JADX WARN: Code restructure failed: missing block: B:34:0x0074, code lost:
        if (r1 == null) goto L30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0076, code lost:
        r1.close();
        r1 = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x007e, code lost:
        if (r1 == null) goto L30;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v12 */
    /* JADX WARN: Type inference failed for: r1v13 */
    /* JADX WARN: Type inference failed for: r1v8 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean copyThumbnail(java.lang.String r7, java.lang.String r8) {
        /*
            r6 = this;
            java.io.File r0 = new java.io.File
            r0.<init>(r7)
            java.io.File r7 = new java.io.File
            r7.<init>(r8)
            boolean r8 = r7.isFile()
            if (r8 == 0) goto L13
            r7.delete()
        L13:
            boolean r8 = r0.isFile()
            r1 = 0
            if (r8 == 0) goto L8b
            android.graphics.BitmapFactory$Options r8 = new android.graphics.BitmapFactory$Options
            r8.<init>()
            r2 = 1
            r8.inJustDecodeBounds = r2
            java.lang.String r3 = r0.getAbsolutePath()
            android.graphics.BitmapFactory.decodeFile(r3, r8)
            r8.inJustDecodeBounds = r1
            r3 = r2
        L2c:
            r4 = 8
            if (r3 >= r4) goto L41
            int r4 = r8.outWidth
            int r4 = r4 / r3
            r5 = 320(0x140, float:4.48E-43)
            if (r4 <= r5) goto L41
            int r4 = r8.outHeight
            int r4 = r4 / r3
            r5 = 180(0xb4, float:2.52E-43)
            if (r4 <= r5) goto L41
            int r3 = r3 * 2
            goto L2c
        L41:
            r8.inSampleSize = r3
            java.lang.String r3 = r0.getAbsolutePath()
            android.graphics.Bitmap r8 = android.graphics.BitmapFactory.decodeFile(r3, r8)
            if (r8 != 0) goto L51
            r0.delete()
            return r1
        L51:
            r1 = 0
            r7.createNewFile()     // Catch: java.lang.Throwable -> L6e java.io.IOException -> L70 java.io.FileNotFoundException -> L7a
            java.io.FileOutputStream r3 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> L6e java.io.IOException -> L70 java.io.FileNotFoundException -> L7a
            r3.<init>(r7)     // Catch: java.lang.Throwable -> L6e java.io.IOException -> L70 java.io.FileNotFoundException -> L7a
            android.graphics.Bitmap$CompressFormat r7 = android.graphics.Bitmap.CompressFormat.PNG     // Catch: java.lang.Throwable -> L65 java.io.IOException -> L68 java.io.FileNotFoundException -> L6b
            r1 = 100
            r8.compress(r7, r1, r3)     // Catch: java.lang.Throwable -> L65 java.io.IOException -> L68 java.io.FileNotFoundException -> L6b
            r3.close()     // Catch: java.io.IOException -> L81
            goto L81
        L65:
            r7 = move-exception
            r1 = r3
            goto L85
        L68:
            r7 = move-exception
            r1 = r3
            goto L71
        L6b:
            r7 = move-exception
            r1 = r3
            goto L7b
        L6e:
            r7 = move-exception
            goto L85
        L70:
            r7 = move-exception
        L71:
            r7.printStackTrace()     // Catch: java.lang.Throwable -> L6e
            if (r1 == 0) goto L81
        L76:
            r1.close()     // Catch: java.io.IOException -> L81
            goto L81
        L7a:
            r7 = move-exception
        L7b:
            r7.printStackTrace()     // Catch: java.lang.Throwable -> L6e
            if (r1 == 0) goto L81
            goto L76
        L81:
            r0.delete()
            return r2
        L85:
            if (r1 == 0) goto L8a
            r1.close()     // Catch: java.io.IOException -> L8a
        L8a:
            throw r7
        L8b:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.app.common.nexasset.store.AssetLocalInstallDB.copyThumbnail(java.lang.String, java.lang.String):boolean");
    }

    public static boolean isUpdatedFeaturedList(int i, String str) {
        File file = new File(localFeaturedPath + File.separator + "" + i + ".json");
        boolean z = true;
        if (file.isFile()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file);
                byte[] bArr = new byte[(int) file.length()];
                fileInputStream.read(bArr);
                if (Arrays.equals(bArr, str.getBytes())) {
                    Log.d(TAG, "FeaturedList equals index=" + i);
                    z = false;
                }
                fileInputStream.close();
            } catch (FileNotFoundException unused) {
            } catch (IOException unused2) {
                Log.d(TAG, "FeaturedList IOException");
            }
        }
        return z;
    }

    public static void saveFeaturedList(int i, String str) {
        String str2 = localFeaturedPath;
        try {
            try {
                FileOutputStream fileOutputStream = new FileOutputStream(str2 + File.separator + "" + i + ".json");
                try {
                    fileOutputStream.write(str.getBytes());
                    fileOutputStream.close();
                } catch (IOException unused) {
                    fileOutputStream.close();
                } catch (Throwable th) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    throw th;
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        } catch (FileNotFoundException unused2) {
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00b9 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r1v17 */
    /* JADX WARN: Type inference failed for: r1v18 */
    /* JADX WARN: Type inference failed for: r1v2 */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* JADX WARN: Type inference failed for: r1v6, types: [java.io.FileOutputStream] */
    /* JADX WARN: Type inference failed for: r1v7 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void saveFeaturedThumbnail(int r8, android.graphics.Bitmap r9) {
        /*
            java.io.File r0 = new java.io.File
            java.lang.String r1 = com.nexstreaming.app.common.nexasset.store.AssetLocalInstallDB.localFeaturedPath
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = ""
            r2.append(r3)
            r2.append(r8)
            java.lang.String r2 = r2.toString()
            r0.<init>(r1, r2)
            boolean r1 = r0.isFile()
            if (r1 == 0) goto L87
            android.graphics.BitmapFactory$Options r1 = new android.graphics.BitmapFactory$Options
            r1.<init>()
            r2 = 1
            r1.inJustDecodeBounds = r2
            java.lang.String r2 = r0.getAbsolutePath()
            android.graphics.BitmapFactory.decodeFile(r2, r1)
            int r2 = r1.outWidth
            int r3 = r9.getWidth()
            java.lang.String r4 = "saveFeaturedThumbnail assetIdx="
            java.lang.String r5 = "AssetLocalInstallDB"
            if (r2 == r3) goto L63
            int r1 = r1.outHeight
            int r2 = r9.getHeight()
            if (r1 == r2) goto L63
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r4)
            r1.append(r8)
            java.lang.String r8 = ", is not bmp . size="
            r1.append(r8)
            long r2 = r0.length()
            r1.append(r2)
            java.lang.String r8 = r1.toString()
            android.util.Log.d(r5, r8)
            r0.delete()
            goto L87
        L63:
            long r1 = java.lang.System.currentTimeMillis()
            long r6 = r0.lastModified()
            long r1 = r1 - r6
            java.lang.StringBuilder r9 = new java.lang.StringBuilder
            r9.<init>()
            r9.append(r4)
            r9.append(r8)
            java.lang.String r8 = ", exists. lastModified="
            r9.append(r8)
            r9.append(r1)
            java.lang.String r8 = r9.toString()
            android.util.Log.d(r5, r8)
            return
        L87:
            r8 = 0
            r0.createNewFile()     // Catch: java.lang.Throwable -> L9f java.io.IOException -> La3 java.io.FileNotFoundException -> Lac
            java.io.FileOutputStream r1 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> L9f java.io.IOException -> La3 java.io.FileNotFoundException -> Lac
            r1.<init>(r0)     // Catch: java.lang.Throwable -> L9f java.io.IOException -> La3 java.io.FileNotFoundException -> Lac
            android.graphics.Bitmap$CompressFormat r8 = android.graphics.Bitmap.CompressFormat.PNG     // Catch: java.io.IOException -> L9b java.io.FileNotFoundException -> L9d java.lang.Throwable -> Lb6
            r0 = 100
            r9.compress(r8, r0, r1)     // Catch: java.io.IOException -> L9b java.io.FileNotFoundException -> L9d java.lang.Throwable -> Lb6
        L97:
            r1.close()     // Catch: java.io.IOException -> Lb5
            goto Lb5
        L9b:
            r8 = move-exception
            goto La6
        L9d:
            r8 = move-exception
            goto Laf
        L9f:
            r9 = move-exception
            r1 = r8
            r8 = r9
            goto Lb7
        La3:
            r9 = move-exception
            r1 = r8
            r8 = r9
        La6:
            r8.printStackTrace()     // Catch: java.lang.Throwable -> Lb6
            if (r1 == 0) goto Lb5
            goto L97
        Lac:
            r9 = move-exception
            r1 = r8
            r8 = r9
        Laf:
            r8.printStackTrace()     // Catch: java.lang.Throwable -> Lb6
            if (r1 == 0) goto Lb5
            goto L97
        Lb5:
            return
        Lb6:
            r8 = move-exception
        Lb7:
            if (r1 == 0) goto Lbc
            r1.close()     // Catch: java.io.IOException -> Lbc
        Lbc:
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.nexstreaming.app.common.nexasset.store.AssetLocalInstallDB.saveFeaturedThumbnail(int, android.graphics.Bitmap):void");
    }

    private void moveFeaturedList(String str) {
        String[] list;
        File file = new File(assetStoreRootPath + File.separator + str);
        if (file.isDirectory()) {
            for (String str2 : file.list()) {
                if (!str2.startsWith(".")) {
                    if (str2.endsWith(".json")) {
                        File file2 = new File(file.getAbsolutePath(), str2);
                        if (!copyFile(file2, localFeaturedPath + File.separator + str2)) {
                            Log.d(TAG, "copyFile fail!");
                        }
                        file2.delete();
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append(file.getAbsolutePath());
                        String str3 = File.separator;
                        sb.append(str3);
                        sb.append(str2);
                        copyThumbnail(sb.toString(), localFeaturedPath + str3 + str2);
                    }
                }
            }
        }
    }

    private boolean getFeaturedList(String str, int i) {
        FileInputStream fileInputStream;
        byte[] bArr;
        FileInputStream fileInputStream2;
        File file = new File(str);
        ArrayList<remoteAssetItem> arrayList = this.mFeaturedList.get(Integer.valueOf(i));
        if (arrayList == null) {
            arrayList = new ArrayList<>();
            this.mFeaturedList.put(Integer.valueOf(i), arrayList);
        }
        boolean z = false;
        if (file.isDirectory()) {
            File file2 = new File(file.getAbsolutePath(), "" + i + ".json");
            try {
            } catch (JsonIOException e) {
                Log.e(TAG, "getFeaturedList err!");
                e.printStackTrace();
            } catch (JsonSyntaxException e2) {
                Log.e(TAG, "getFeaturedList err!");
                e2.printStackTrace();
            } catch (FileNotFoundException e3) {
                e3.printStackTrace();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
            if (file2.isFile()) {
                try {
                    fileInputStream2 = new FileInputStream(file2);
                    try {
                        AssetStoreAPIData.GetNewAssetList getNewAssetList = (AssetStoreAPIData.GetNewAssetList) new Gson().fromJson((Reader) new InputStreamReader(fileInputStream2), (Class<Object>) AssetStoreAPIData.GetNewAssetList.class);
                        if (getNewAssetList != null) {
                            List<AssetStoreAPIData.AssetInfo> list = getNewAssetList.objList;
                            if (list != null) {
                                if (list.size() > 0) {
                                    arrayList.clear();
                                    for (AssetStoreAPIData.AssetInfo assetInfo : getNewAssetList.objList) {
                                        if (!new File(str, "" + assetInfo.idx).isFile()) {
                                            Log.d(TAG, "getFeaturedList() mode=" + i + ", idx=" + i + ", thumbnail not found!");
                                        } else {
                                            remoteAssetItem remoteassetitem = new remoteAssetItem();
                                            remoteassetitem.id = assetInfo.asset_id;
                                            remoteassetitem.idx = assetInfo.idx;
                                            remoteassetitem.name = assetInfo.assetName.get(0).string_title;
                                            String language = this.mContext.getResources().getConfiguration().locale.getLanguage();
                                            int i2 = 0;
                                            while (true) {
                                                if (i2 >= assetInfo.assetName.size()) {
                                                    break;
                                                }
                                                if (assetInfo.assetName.get(i2).language_code.equals(language) && assetInfo.assetName.get(i2).string_title != null) {
                                                    remoteassetitem.name = assetInfo.assetName.get(i2).string_title;
                                                    break;
                                                }
                                                i2++;
                                            }
                                            remoteassetitem.thumbnailPath = str + File.separator + remoteassetitem.idx;
                                            remoteassetitem.thumbnailURL = assetInfo.thumbnail_path_s;
                                            String str2 = assetInfo.category_aliasName;
                                            if (str2 != null) {
                                                remoteassetitem.category = str2;
                                            } else {
                                                remoteassetitem.category = "None";
                                            }
                                            arrayList.add(remoteassetitem);
                                        }
                                    }
                                    z = true;
                                }
                                fileInputStream2.close();
                                try {
                                    if (!z) {
                                        try {
                                            FileInputStream fileInputStream3 = new FileInputStream(file2);
                                            try {
                                                fileInputStream3.read(new byte[(int) file2.length()]);
                                                Log.d(TAG, "ErrJson:" + String.valueOf(bArr));
                                                fileInputStream3.close();
                                            } catch (Throwable th) {
                                                th = th;
                                                fileInputStream = fileInputStream3;
                                                fileInputStream.close();
                                                throw th;
                                            }
                                        } catch (Throwable th2) {
                                            th = th2;
                                            fileInputStream = null;
                                        }
                                    }
                                } catch (FileNotFoundException e5) {
                                    e5.printStackTrace();
                                } catch (IOException e6) {
                                    e6.printStackTrace();
                                }
                            } else {
                                fileInputStream2.close();
                                return false;
                            }
                        } else {
                            fileInputStream2.close();
                            return false;
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        fileInputStream2.close();
                        throw th;
                    }
                } catch (Throwable th4) {
                    th = th4;
                    fileInputStream2 = null;
                }
            } else {
                Log.d(TAG, "file not found=" + file2.getAbsolutePath());
            }
        }
        return z;
    }

    public List<remoteAssetItem> getList(int i, String str) {
        getFeaturedList(localFeaturedPath, i);
        return this.mFeaturedList.get(Integer.valueOf(i));
    }

    public String getThumbnailUrl(int i) {
        ArrayList<remoteAssetItem> arrayList = this.mFeaturedList.get(1);
        if (arrayList != null) {
            for (remoteAssetItem remoteassetitem : arrayList) {
                if (remoteassetitem.idx == i) {
                    return remoteassetitem.thumbnailURL;
                }
            }
        }
        ArrayList<remoteAssetItem> arrayList2 = this.mFeaturedList.get(2);
        if (arrayList2 != null) {
            for (remoteAssetItem remoteassetitem2 : arrayList2) {
                if (remoteassetitem2.idx == i) {
                    return remoteassetitem2.thumbnailURL;
                }
            }
            return null;
        }
        return null;
    }
}
