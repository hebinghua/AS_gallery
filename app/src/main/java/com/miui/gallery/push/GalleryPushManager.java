package com.miui.gallery.push;

import android.accounts.Account;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.assistant.manager.MediaFeatureManager;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.push.messagehandler.MessageHandler;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.Encode;
import com.miui.gallery.util.PrivacyAgreementUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.market.PrintInstaller;
import com.miui.os.Rom;
import com.xiaomi.mipush.sdk.MiPushClient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/* loaded from: classes2.dex */
public class GalleryPushManager {

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final GalleryPushManager INSTANCE = new GalleryPushManager();
    }

    public GalleryPushManager() {
    }

    public static GalleryPushManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public final void subscribeTopics(Context context, boolean z, String... strArr) {
        if (context == null) {
            return;
        }
        List<String> allTopic = MiPushClient.getAllTopic(context);
        if (strArr == null || strArr.length == 0) {
            if (!z || !BaseMiscUtil.isValid(allTopic)) {
                return;
            }
            for (String str : allTopic) {
                MiPushClient.unsubscribe(context, str, null);
            }
            return;
        }
        ArrayList arrayList = new ArrayList(Arrays.asList(strArr));
        if (BaseMiscUtil.isValid(allTopic)) {
            for (String str2 : allTopic) {
                if (arrayList.contains(str2)) {
                    arrayList.remove(str2);
                } else if (z) {
                    MiPushClient.unsubscribe(context, str2, null);
                }
            }
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            MiPushClient.subscribe(context, (String) it.next(), null);
        }
    }

    public final void setUserAccount(Context context, String str, boolean z) {
        String SHA1Encode = !TextUtils.isEmpty(str) ? Encode.SHA1Encode(str.getBytes()) : null;
        List<String> allUserAccount = MiPushClient.getAllUserAccount(context);
        boolean z2 = false;
        if (BaseMiscUtil.isValid(allUserAccount)) {
            for (String str2 : allUserAccount) {
                if (!TextUtils.isEmpty(str2)) {
                    if (TextUtils.equals(str2, SHA1Encode)) {
                        z2 = true;
                    } else if (z) {
                        MiPushClient.unsetUserAccount(context, str2, null);
                    }
                }
            }
        }
        if (z2 || SHA1Encode == null) {
            return;
        }
        MiPushClient.setUserAccount(context, SHA1Encode, null);
    }

    public void onAddAccount(Context context, Account account) {
        if (context == null) {
            return;
        }
        if (account != null && !TextUtils.isEmpty(account.name)) {
            setUserAccount(context, account.name, true);
        } else {
            setUserAccount(context, null, true);
        }
        for (MessageHandler messageHandler : MessageHandlerFactory.getAllMessageHandlers()) {
            messageHandler.onAddAccount();
        }
    }

    public void onDeleteAccount(Context context) {
        if (context == null) {
            return;
        }
        setUserAccount(context, null, true);
        for (MessageHandler messageHandler : MessageHandlerFactory.getAllMessageHandlers()) {
            messageHandler.onDeleteAccount();
        }
        unsubscribeAccountTopic(context);
        subscribeTopics(context, false, getAccountTopic(null));
    }

    public void setUserAccountAndTopics(Context context) {
        if (context == null) {
            return;
        }
        Account account = AccountCache.getAccount();
        if (account != null && !TextUtils.isEmpty(account.name)) {
            setUserAccount(context, account.name, true);
        } else {
            setUserAccount(context, null, true);
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add("cloud_control");
        String printTopic = getPrintTopic();
        if (!TextUtils.isEmpty(printTopic)) {
            arrayList.add(printTopic);
        }
        String storyTopic = getStoryTopic();
        if (!TextUtils.isEmpty(storyTopic)) {
            arrayList.add(storyTopic);
        }
        String miuiVersionTopic = getMiuiVersionTopic();
        if (!TextUtils.isEmpty(miuiVersionTopic)) {
            arrayList.add(miuiVersionTopic);
        }
        arrayList.add(getAccountTopic(account));
        subscribeTopics(context, true, (String[]) arrayList.toArray(new String[arrayList.size()]));
    }

    public final String getPrintTopic() {
        if (PrintInstaller.getInstance().isPhotoPrintEnable()) {
            return "photo_print";
        }
        return null;
    }

    public final String getStoryTopic() {
        if (MediaFeatureManager.isStoryGenerateEnable()) {
            return "gallery_story";
        }
        return null;
    }

    public final String getMiuiVersionTopic() {
        if (Rom.IS_STABLE) {
            return "miui_version_stable";
        }
        if (Rom.IS_ALPHA) {
            return "miui_version_alpha";
        }
        if (!Rom.IS_DEV) {
            return null;
        }
        return "miui_version_dev";
    }

    public final String getAccountTopic(Account account) {
        String str = account == null ? null : account.name;
        return !TextUtils.isEmpty(str) ? String.format(Locale.US, "%s_%s", "xiaomi_account", str.substring(account.name.length() - 1)) : String.format(Locale.US, "%s_%s", "xiaomi_account_sample", Long.valueOf(Math.abs(BaseGalleryPreferences.SampleStatistic.getUniqId()) % 10));
    }

    public final void unsubscribeAccountTopic(Context context) {
        List<String> allTopic = MiPushClient.getAllTopic(context);
        if (BaseMiscUtil.isValid(allTopic)) {
            for (String str : allTopic) {
                if (str != null && str.startsWith("xiaomi_account")) {
                    MiPushClient.unsubscribe(context, str, null);
                }
            }
        }
    }

    public final boolean shouldInit(Context context) {
        try {
            List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
            if (runningAppProcesses == null) {
                return false;
            }
            String packageName = context.getPackageName();
            int myPid = Process.myPid();
            for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                if (runningAppProcessInfo.pid == myPid && packageName.equals(runningAppProcessInfo.processName)) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            DefaultLogger.e("GalleryPushManager", e);
            return false;
        }
    }

    public void registerPush(Context context) {
        if (!PrivacyAgreementUtils.isGalleryAgreementEnable(context)) {
            DefaultLogger.w("GalleryPushManager", "Register push failed: privacy agreement disabled");
            unregisterPush(context);
        } else if (!AgreementsUtils.isNetworkingAgreementAccepted() || !shouldInit(context)) {
        } else {
            MiPushClient.registerPush(context, "2882303761517492012", "5601749292012");
        }
    }

    public final void unregisterPush(Context context) {
        if (shouldInit(context)) {
            MiPushClient.unregisterPush(context);
        }
    }
}
