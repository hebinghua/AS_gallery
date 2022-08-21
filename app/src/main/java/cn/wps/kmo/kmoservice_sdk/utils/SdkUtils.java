package cn.wps.kmo.kmoservice_sdk.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import cn.wps.kmo.kmoservice_sdk.common.TaskResult;

/* loaded from: classes.dex */
public class SdkUtils {

    /* loaded from: classes.dex */
    public interface ICallback {
        void callback(TaskResult taskResult);
    }

    public static void respCallback(ICallback iCallback, TaskResult taskResult) {
        if (iCallback != null) {
            iCallback.callback(taskResult);
            if (taskResult == null) {
                return;
            }
            CommonUtils.log("resultCode: " + taskResult.mResultCode + " message: " + taskResult.mBundle);
        }
    }

    public static boolean isInstalled(Context context, String str) {
        try {
            PackageManager packageManager = context.getPackageManager();
            packageManager.getApplicationInfo(KmoInfoConstant.getPackageWpsName(str), 8192).loadIcon(packageManager);
            return true;
        } catch (PackageManager.NameNotFoundException | Exception unused) {
            return false;
        }
    }

    public static boolean isSupport(Context context, String str) {
        try {
            return context.getPackageManager().getPackageInfo(KmoInfoConstant.getPackageWpsName(str), 8192).versionCode >= Version.getSupportMinVersionCode(str);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}
