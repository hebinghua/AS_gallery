package com.market.sdk;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import com.market.sdk.utils.AppGlobal;
import com.market.sdk.utils.Client;
import com.market.sdk.utils.Coder;
import com.market.sdk.utils.Connection;
import com.market.sdk.utils.Constants;
import com.market.sdk.utils.Log;
import com.market.sdk.utils.PrefUtils;
import com.market.sdk.utils.Utils;
import com.xiaomi.stat.d;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import miuix.appcompat.app.AlertDialog;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Marker;

/* loaded from: classes.dex */
public class XiaomiUpdateAgent {
    public static LocalAppInfo mAppInfo = null;
    public static boolean mIsInited = false;
    public static boolean mIsLoading = false;
    public static UpdateInfo mUpdateInfo;
    public static XiaomiUpdateListener mUpdateListener;
    public static Constants.UpdateMethod mUpdateMethod;
    public static AbTestIdentifier sAbTestIdentifer;
    public static boolean sUseSandbox;
    public static WeakReference<Context> mContext = new WeakReference<>(null);
    public static boolean mAutoPopup = true;
    public static boolean mCheckUpdateOnlyWifi = false;
    public static boolean mIsPathcerLibraryLoaded = false;
    public static boolean sUseImeiMd5AsIdentifier = false;

    static {
        mUpdateMethod = Utils.isMiuiPad() ? Constants.UpdateMethod.DOWNLOAD_MANAGER : Constants.UpdateMethod.MARKET;
        sAbTestIdentifer = AbTestIdentifier.ANDROID_ID;
    }

    public static synchronized void update(Context context, boolean z) {
        synchronized (XiaomiUpdateAgent.class) {
            if (mIsLoading) {
                return;
            }
            mIsLoading = true;
            Client.init(AppGlobal.getContext());
            mContext = new WeakReference<>(context);
            sUseSandbox = z;
            if (!mIsInited) {
                mAppInfo = null;
                mUpdateInfo = null;
                Constants.configURL();
                mIsInited = true;
            }
            new CheckUpdateTask().execute(AppGlobal.getContext().getPackageName());
        }
    }

    public static void setUpdateAutoPopup(boolean z) {
        mAutoPopup = z;
    }

    public static void arrange() {
        Context context = mContext.get();
        if (context == null) {
            return;
        }
        Client.init(context);
        openMarketOrArrange();
    }

    public static void setUpdateListener(XiaomiUpdateListener xiaomiUpdateListener) {
        mUpdateListener = xiaomiUpdateListener;
    }

    public static LocalAppInfo getAppInfo(Context context, String str) {
        PackageInfo packageInfo;
        ApplicationInfo applicationInfo;
        LocalAppInfo localAppInfo = LocalAppInfo.get(str);
        PackageManager packageManager = context.getPackageManager();
        try {
            packageInfo = packageManager.getPackageInfo(localAppInfo.packageName, 64);
        } catch (PackageManager.NameNotFoundException unused) {
            Log.e("MarketUpdateAgent", "get package info failed");
            packageInfo = null;
        }
        if (packageInfo == null || (applicationInfo = packageInfo.applicationInfo) == null) {
            return null;
        }
        localAppInfo.displayName = packageManager.getApplicationLabel(applicationInfo).toString();
        localAppInfo.versionCode = packageInfo.versionCode;
        localAppInfo.versionName = packageInfo.versionName;
        localAppInfo.signature = Coder.encodeMD5(String.valueOf(packageInfo.signatures[0].toChars()));
        localAppInfo.sourceDir = packageInfo.applicationInfo.sourceDir;
        localAppInfo.sourceMD5 = Coder.encodeMD5(new File(localAppInfo.sourceDir));
        return localAppInfo;
    }

    /* loaded from: classes.dex */
    public static class UpdateInfo {
        public String apkHash;
        public long apkSize;
        public String apkUrl;
        public long diffSize;
        public int fitness;
        public String host;
        public boolean matchLanguage;
        public int source;
        public String updateLog;
        public int versionCode;
        public String versionName;
        public String diffUrl = "";
        public String diffHash = "";

        public String toString() {
            return "UpdateInfo:\nhost = " + this.host + "\nfitness = " + this.fitness + "\nupdateLog = " + this.updateLog + "\nversionCode = " + this.versionCode + "\nversionName = " + this.versionName + "\napkUrl = " + this.apkUrl + "\napkHash = " + this.apkHash + "\napkSize = " + this.apkSize + "\ndiffUrl = " + this.diffUrl + "\ndiffHash = " + this.diffHash + "\ndiffSize = " + this.diffSize + "\nmatchLanguage = " + this.matchLanguage;
        }
    }

    /* loaded from: classes.dex */
    public static class CheckUpdateTask extends AsyncTask<String, Void, Integer> {
        public CheckUpdateTask() {
        }

        @Override // android.os.AsyncTask
        public void onPreExecute() {
            Log.d("MarketUpdateAgent", "start to check update");
            if (!XiaomiUpdateAgent.mIsPathcerLibraryLoaded) {
                XiaomiUpdateAgent.mIsPathcerLibraryLoaded = Patcher.tryLoadLibrary();
            }
        }

        @Override // android.os.AsyncTask
        public Integer doInBackground(String... strArr) {
            Context context = (Context) XiaomiUpdateAgent.mContext.get();
            if (context == null) {
                return 4;
            }
            if (!Utils.isConnected(context)) {
                return 3;
            }
            if (Utils.isWifiConnected(context) || !XiaomiUpdateAgent.mCheckUpdateOnlyWifi) {
                int i = 0;
                LocalAppInfo unused = XiaomiUpdateAgent.mAppInfo = XiaomiUpdateAgent.getAppInfo(context, strArr[0]);
                if (XiaomiUpdateAgent.mAppInfo == null) {
                    return 5;
                }
                Connection connection = new Connection(Constants.UPDATE_URL);
                Connection.Parameter parameter = new Connection.Parameter(connection);
                parameter.add("info", getFilterParams());
                parameter.add("packageName", XiaomiUpdateAgent.mAppInfo.packageName);
                parameter.add("versionCode", XiaomiUpdateAgent.mAppInfo.versionCode + "");
                parameter.add("apkHash", XiaomiUpdateAgent.mAppInfo.sourceMD5);
                parameter.add("signature", XiaomiUpdateAgent.mAppInfo.signature);
                parameter.add("sdk", String.valueOf(Client.SDK_VERSION));
                parameter.add(d.l, Client.SYSTEM_VERSION);
                parameter.add("la", Client.getLanguage());
                parameter.add("co", Client.getCountry());
                parameter.add("lo", Client.getRegion());
                parameter.add("androidId", Client.ANDROID_ID);
                parameter.add("device", Client.getDevice());
                parameter.add("deviceType", String.valueOf(Client.getDeviceType()));
                parameter.add("cpuArchitecture", Client.getCpuArch());
                parameter.add("model", Client.getModel());
                parameter.add("xiaomiSDKVersion", "12");
                parameter.add("xiaomiSDKVersionName", context.getResources().getString(R$string.marketSdkVersion));
                parameter.add("debug", XiaomiUpdateAgent.sUseSandbox ? "1" : "0");
                parameter.add("miuiBigVersionName", Client.getMiuiBigVersionName());
                parameter.add("miuiBigVersionCode", Client.getMiuiBigVersionCode());
                parameter.add("ext_abTestIdentifier", String.valueOf(XiaomiUpdateAgent.sAbTestIdentifer.ordinal()));
                if (XiaomiUpdateAgent.sUseImeiMd5AsIdentifier || XiaomiUpdateAgent.sAbTestIdentifer == AbTestIdentifier.IMEI_MD5) {
                    parameter.add("imei", Client.getImeiMd5());
                }
                if (Connection.NetworkError.OK == connection.requestJSON()) {
                    UpdateInfo unused2 = XiaomiUpdateAgent.mUpdateInfo = parseUpdateInfo(connection.getResponse());
                    if (XiaomiUpdateAgent.mUpdateInfo != null) {
                        Log.i("MarketUpdateAgent", XiaomiUpdateAgent.mUpdateInfo.toString());
                        if (XiaomiUpdateAgent.mUpdateInfo.fitness != 0) {
                            i = 1;
                        }
                        return Integer.valueOf(i);
                    }
                }
                return 4;
            }
            return 2;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Integer num) {
            boolean unused = XiaomiUpdateAgent.mIsLoading = false;
            Context context = (Context) XiaomiUpdateAgent.mContext.get();
            if (context == null) {
                return;
            }
            UpdateResponse updateResponse = new UpdateResponse();
            if (num.intValue() == 0) {
                updateResponse.updateLog = XiaomiUpdateAgent.mUpdateInfo.updateLog;
                updateResponse.versionCode = XiaomiUpdateAgent.mUpdateInfo.versionCode;
                updateResponse.versionName = XiaomiUpdateAgent.mUpdateInfo.versionName;
                updateResponse.apkSize = XiaomiUpdateAgent.mUpdateInfo.apkSize;
                updateResponse.apkHash = XiaomiUpdateAgent.mUpdateInfo.apkHash;
                updateResponse.diffSize = XiaomiUpdateAgent.mUpdateInfo.diffSize;
                updateResponse.path = Connection.connect(XiaomiUpdateAgent.mUpdateInfo.host, XiaomiUpdateAgent.mUpdateInfo.apkUrl);
                updateResponse.matchLanguage = XiaomiUpdateAgent.mUpdateInfo.matchLanguage;
            }
            if (XiaomiUpdateAgent.mUpdateListener != null) {
                XiaomiUpdateAgent.mUpdateListener.onUpdateReturned(num.intValue(), updateResponse);
            }
            if (!XiaomiUpdateAgent.mAutoPopup || num.intValue() != 0 || !(context instanceof Activity) || !Client.isMiui()) {
                return;
            }
            new CheckDownloadTask().execute(new Void[0]);
        }

        public final String getFilterParams() {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("screenSize", Client.DISPLAY_WIDTH + Marker.ANY_MARKER + Client.DISPLAY_HEIGHT);
                jSONObject.put("resolution", Client.DISPLAY_RESOLUTION);
                jSONObject.put("density", Client.DISPLAY_DENSITY);
                jSONObject.put("touchScreen", Client.TOUCH_SCREEN);
                jSONObject.put("glEsVersion", Client.GLES_VERSION);
                jSONObject.put("feature", Client.FEATURE);
                jSONObject.put("library", Client.LIBRARY);
                jSONObject.put("glExtension", Client.GL_EXTENSION);
                jSONObject.put("sdk", Client.SDK_VERSION);
                jSONObject.put("version", Client.SYSTEM_VERSION);
                jSONObject.put("release", Client.RELEASE);
                return jSONObject.toString();
            } catch (JSONException unused) {
                return "";
            }
        }

        public final UpdateInfo parseUpdateInfo(JSONObject jSONObject) {
            if (jSONObject == null) {
                Log.e("MarketUpdateAgent", "update info json obj null");
                return null;
            }
            if (Utils.DEBUG) {
                Log.d("MarketUpdateAgent", "updateInfo : " + jSONObject.toString());
            }
            UpdateInfo updateInfo = new UpdateInfo();
            updateInfo.host = jSONObject.optString("host");
            updateInfo.fitness = jSONObject.optInt("fitness");
            updateInfo.source = jSONObject.optInt("source");
            updateInfo.updateLog = jSONObject.optString("updateLog");
            updateInfo.versionCode = jSONObject.optInt("versionCode");
            updateInfo.versionName = jSONObject.optString("versionName");
            updateInfo.apkUrl = jSONObject.optString("apk");
            updateInfo.apkHash = jSONObject.optString("apkHash");
            updateInfo.apkSize = jSONObject.optLong("apkSize");
            updateInfo.matchLanguage = jSONObject.optBoolean("matchLanguage");
            if (XiaomiUpdateAgent.mIsPathcerLibraryLoaded) {
                updateInfo.diffUrl = jSONObject.optString("diffFile");
                updateInfo.diffHash = jSONObject.optString("diffFileHash");
                updateInfo.diffSize = jSONObject.optLong("diffFileSize");
            }
            return updateInfo;
        }

        /* loaded from: classes.dex */
        public static class CheckDownloadTask extends AsyncTask<Void, Void, Boolean> {
            public CheckDownloadTask() {
            }

            @Override // android.os.AsyncTask
            public Boolean doInBackground(Void... voidArr) {
                Context context = (Context) XiaomiUpdateAgent.mContext.get();
                if (context != null) {
                    return Boolean.valueOf(DownloadInstallManager.getManager(context).isDownloading(XiaomiUpdateAgent.mAppInfo));
                }
                return Boolean.FALSE;
            }

            @Override // android.os.AsyncTask
            public void onPostExecute(Boolean bool) {
                if (bool.booleanValue() || !CheckUpdateTask.isNeedShowDialog()) {
                    return;
                }
                CheckUpdateTask.showUpdateDialog();
            }
        }

        public static boolean isNeedShowDialog() {
            if (System.currentTimeMillis() - Long.valueOf(PrefUtils.getLong("sdkBeginTime", new PrefUtils.PrefFile[0])).longValue() < 259200000) {
                Long valueOf = Long.valueOf(PrefUtils.getLong("sdkWindowLastShowTime", new PrefUtils.PrefFile[0]));
                if (System.currentTimeMillis() - valueOf.longValue() < 21600000) {
                    return false;
                }
                int i = PrefUtils.getInt("sdkWindowShowTimes", new PrefUtils.PrefFile[0]);
                if (i < 2) {
                    PrefUtils.setInt("sdkWindowShowTimes", i + 1, new PrefUtils.PrefFile[0]);
                    PrefUtils.setLong("sdkWindowLastShowTime", System.currentTimeMillis(), new PrefUtils.PrefFile[0]);
                    return true;
                } else if (getDayOfMonth(Long.valueOf(System.currentTimeMillis())) == getDayOfMonth(valueOf)) {
                    return false;
                } else {
                    PrefUtils.setInt("sdkWindowShowTimes", 1, new PrefUtils.PrefFile[0]);
                    PrefUtils.setLong("sdkWindowLastShowTime", System.currentTimeMillis(), new PrefUtils.PrefFile[0]);
                    return true;
                }
            }
            return true;
        }

        public static int getDayOfMonth(Long l) {
            Date date = new Date(l.longValue());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.get(5);
        }

        public static void showUpdateDialog() {
            Context context = (Context) XiaomiUpdateAgent.mContext.get();
            if (context == null) {
                return;
            }
            if ((context instanceof Activity) && ((Activity) context).isFinishing()) {
                Log.e("MarketUpdateAgent", "activity not running!");
                return;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context, Build.VERSION.SDK_INT < 29 ? R$style.AlertDialog_Theme_Light : R$style.AlertDialog_Theme_DayNight);
            builder.setTitle(context.getString(R$string.xiaomi_market_sdk_update_dialog_title));
            builder.setMessage(XiaomiUpdateAgent.mUpdateInfo.updateLog);
            builder.setNegativeButton(R$string.xiaomi_market_sdk_update_dialog_cancel, (DialogInterface.OnClickListener) null).setPositiveButton(R$string.xiaomi_market_sdk_update_dialog_ok, new DialogInterface.OnClickListener() { // from class: com.market.sdk.XiaomiUpdateAgent.CheckUpdateTask.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    XiaomiUpdateAgent.openMarketOrArrange();
                }
            });
            builder.show();
        }
    }

    public static void openMarketOrArrange() {
        UpdateInfo updateInfo;
        ActivityInfo activityInfo;
        Context context = mContext.get();
        if (context == null || (updateInfo = mUpdateInfo) == null || mAppInfo == null) {
            return;
        }
        if (updateInfo.source != 1 && Utils.isMiuiMarketExisted(context)) {
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse("market://details?ref=update_sdk&back=true&startDownload=true&id=" + mAppInfo.packageName));
            intent.setPackage(Utils.getMarketPackageName());
            List<ResolveInfo> queryIntentActivities = context.getPackageManager().queryIntentActivities(intent, 0);
            if (queryIntentActivities == null || queryIntentActivities.size() != 1 || (activityInfo = queryIntentActivities.get(0).activityInfo) == null || !activityInfo.exported || !activityInfo.enabled) {
                return;
            }
            intent.addFlags(268435456);
            context.startActivity(intent);
            return;
        }
        Log.e("MarketUpdateAgent", "MiuiMarket doesn't exist");
    }
}
