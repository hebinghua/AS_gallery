package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.text.TextUtils;
import ch.qos.logback.core.CoreConstants;
import com.xiaomi.clientreport.data.Config;
import com.xiaomi.clientreport.manager.ClientReportClient;
import com.xiaomi.mipush.sdk.MiTinyDataClient;
import com.xiaomi.push.Cdo;
import com.xiaomi.push.bp;
import com.xiaomi.push.dd;
import com.xiaomi.push.dn;
import com.xiaomi.push.el;
import com.xiaomi.push.em;
import com.xiaomi.push.en;
import com.xiaomi.push.ey;
import com.xiaomi.push.hj;
import com.xiaomi.push.hn;
import com.xiaomi.push.ho;
import com.xiaomi.push.ht;
import com.xiaomi.push.hw;
import com.xiaomi.push.hx;
import com.xiaomi.push.id;
import com.xiaomi.push.ii;
import com.xiaomi.push.ij;
import com.xiaomi.push.in;
import com.xiaomi.push.ip;
import com.xiaomi.push.ir;
import com.xiaomi.push.service.ba;
import com.xiaomi.push.service.bd;
import com.xiaomi.push.service.receivers.NetworkStatusReceiver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/* loaded from: classes3.dex */
public abstract class MiPushClient {
    public static Context sContext;
    public static long sCurMsgId = System.currentTimeMillis();

    /* loaded from: classes3.dex */
    public interface ICallbackResult<R> {
    }

    @Deprecated
    /* loaded from: classes3.dex */
    public static abstract class MiPushClientCallback {
        public String category;

        public String getCategory() {
            return this.category;
        }

        public void onCommandResult(String str, long j, String str2, List<String> list) {
        }

        public void onInitializeResult(long j, String str, String str2) {
        }

        public void onReceiveMessage(MiPushMessage miPushMessage) {
        }

        public void onReceiveMessage(String str, String str2, String str3, boolean z) {
        }

        public void onSubscribeResult(long j, String str, String str2) {
        }

        public void onUnsubscribeResult(long j, String str, String str2) {
        }
    }

    public static long accountSetTime(Context context, String str) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("mipush_extra", 0);
        return sharedPreferences.getLong("account_" + str, -1L);
    }

    public static synchronized void addAcceptTime(Context context, String str, String str2) {
        synchronized (MiPushClient.class) {
            SharedPreferences.Editor edit = context.getSharedPreferences("mipush_extra", 0).edit();
            edit.putString("accept_time", str + "," + str2);
            com.xiaomi.push.t.a(edit);
        }
    }

    public static synchronized void addAccount(Context context, String str) {
        synchronized (MiPushClient.class) {
            SharedPreferences.Editor edit = context.getSharedPreferences("mipush_extra", 0).edit();
            edit.putLong("account_" + str, System.currentTimeMillis()).commit();
        }
    }

    public static synchronized void addAlias(Context context, String str) {
        synchronized (MiPushClient.class) {
            SharedPreferences.Editor edit = context.getSharedPreferences("mipush_extra", 0).edit();
            edit.putLong("alias_" + str, System.currentTimeMillis()).commit();
        }
    }

    public static void addPullNotificationTime(Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences("mipush_extra", 0).edit();
        edit.putLong("last_pull_notification", System.currentTimeMillis());
        com.xiaomi.push.t.a(edit);
    }

    public static void addRegRequestTime(Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences("mipush_extra", 0).edit();
        edit.putLong("last_reg_request", System.currentTimeMillis());
        com.xiaomi.push.t.a(edit);
    }

    public static synchronized void addTopic(Context context, String str) {
        synchronized (MiPushClient.class) {
            SharedPreferences.Editor edit = context.getSharedPreferences("mipush_extra", 0).edit();
            edit.putLong("topic_" + str, System.currentTimeMillis()).commit();
        }
    }

    public static long aliasSetTime(Context context, String str) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("mipush_extra", 0);
        return sharedPreferences.getLong("alias_" + str, -1L);
    }

    public static void checkNotNull(Object obj, String str) {
        if (obj != null) {
            return;
        }
        throw new IllegalArgumentException("param " + str + " is not nullable");
    }

    public static void clearExtras(Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences("mipush_extra", 0).edit();
        edit.clear();
        edit.commit();
    }

    public static void clearExtrasForInitialize(Context context) {
        SharedPreferences.Editor edit = context.getSharedPreferences("mipush_extra", 0).edit();
        Iterator<String> it = getAllAlias(context).iterator();
        while (it.hasNext()) {
            edit.remove("alias_" + it.next());
        }
        Iterator<String> it2 = getAllUserAccount(context).iterator();
        while (it2.hasNext()) {
            edit.remove("account_" + it2.next());
        }
        Iterator<String> it3 = getAllTopic(context).iterator();
        while (it3.hasNext()) {
            edit.remove("topic_" + it3.next());
        }
        edit.remove("accept_time");
        edit.commit();
    }

    public static void clearLocalNotificationType(Context context) {
        ao.a(context).f();
    }

    public static void clearNotification(Context context) {
        ao.a(context).a(-1);
    }

    public static void clearNotification(Context context, int i) {
        ao.a(context).a(i);
    }

    public static void clearNotification(Context context, String str, String str2) {
        ao.a(context).a(str, str2);
    }

    public static void disablePush(Context context) {
        ao.a(context).a(true);
    }

    public static void enablePush(Context context) {
        ao.a(context).a(false);
    }

    public static String getAcceptTime(Context context) {
        return context.getSharedPreferences("mipush_extra", 0).getString("accept_time", "00:00-23:59");
    }

    public static List<String> getAllAlias(Context context) {
        ArrayList arrayList = new ArrayList();
        for (String str : context.getSharedPreferences("mipush_extra", 0).getAll().keySet()) {
            if (str.startsWith("alias_")) {
                arrayList.add(str.substring(6));
            }
        }
        return arrayList;
    }

    public static List<String> getAllTopic(Context context) {
        ArrayList arrayList = new ArrayList();
        for (String str : context.getSharedPreferences("mipush_extra", 0).getAll().keySet()) {
            if (str.startsWith("topic_") && !str.contains("**ALL**")) {
                arrayList.add(str.substring(6));
            }
        }
        return arrayList;
    }

    public static List<String> getAllUserAccount(Context context) {
        ArrayList arrayList = new ArrayList();
        for (String str : context.getSharedPreferences("mipush_extra", 0).getAll().keySet()) {
            if (str.startsWith("account_")) {
                arrayList.add(str.substring(8));
            }
        }
        return arrayList;
    }

    public static boolean getDefaultSwitch() {
        return com.xiaomi.push.m.m2403b();
    }

    public static boolean getOpenFCMPush(Context context) {
        checkNotNull(context, CoreConstants.CONTEXT_SCOPE_VALUE);
        return f.a(context).b(e.ASSEMBLE_PUSH_FCM);
    }

    public static boolean getOpenHmsPush(Context context) {
        checkNotNull(context, CoreConstants.CONTEXT_SCOPE_VALUE);
        return f.a(context).b(e.ASSEMBLE_PUSH_HUAWEI);
    }

    public static boolean getOpenOPPOPush(Context context) {
        checkNotNull(context, CoreConstants.CONTEXT_SCOPE_VALUE);
        return f.a(context).b(e.ASSEMBLE_PUSH_COS);
    }

    public static boolean getOpenVIVOPush(Context context) {
        return f.a(context).b(e.ASSEMBLE_PUSH_FTOS);
    }

    public static String getRegId(Context context) {
        if (b.m1906a(context).m1913c()) {
            return b.m1906a(context).c();
        }
        return null;
    }

    public static void initEventPerfLogic(final Context context) {
        en.a(new en.a() { // from class: com.xiaomi.mipush.sdk.MiPushClient.5
            @Override // com.xiaomi.push.en.a
            public void uploader(Context context2, hn hnVar) {
                MiTinyDataClient.upload(context2, hnVar);
            }
        });
        Config a = en.a(context);
        com.xiaomi.clientreport.manager.a.a(context).a("4_9_1");
        ClientReportClient.init(context, a, new el(context), new em(context));
        a.a(context);
        t.a(context, a);
        ba.a(context).a(new ba.a(100, "perf event job update") { // from class: com.xiaomi.mipush.sdk.MiPushClient.6
            @Override // com.xiaomi.push.service.ba.a
            public void onCallback() {
                en.m2127a(context);
            }
        });
    }

    public static void initialize(Context context, String str, String str2, MiPushClientCallback miPushClientCallback, String str3, ICallbackResult iCallbackResult) {
        try {
            com.xiaomi.channel.commonutils.logger.b.a(context.getApplicationContext());
            com.xiaomi.channel.commonutils.logger.b.e("sdk_version = 4_9_1");
            com.xiaomi.push.ba.a(context).mo1967a();
            dd.a(context);
            if (miPushClientCallback != null) {
                PushMessageHandler.a(miPushClientCallback);
            }
            if (iCallbackResult != null) {
                PushMessageHandler.a(iCallbackResult);
            }
            if (com.xiaomi.push.v.m2554a(sContext)) {
                v.a(sContext);
            }
            boolean z = b.m1906a(sContext).a() != Constants.a();
            if (!z && !shouldSendRegRequest(sContext)) {
                ao.a(sContext).m1895a();
                com.xiaomi.channel.commonutils.logger.b.m1859a("Could not send  register message within 5s repeatly .");
                return;
            }
            if (z || !b.m1906a(sContext).a(str, str2) || b.m1906a(sContext).f()) {
                String a = bp.a(6);
                b.m1906a(sContext).m1908a();
                b.m1906a(sContext).a(Constants.a());
                b.m1906a(sContext).a(str, str2, a);
                MiTinyDataClient.a.a().b("com.xiaomi.xmpushsdk.tinydataPending.appId");
                clearExtras(sContext);
                clearNotification(context);
                ij ijVar = new ij();
                ijVar.a(bd.b());
                ijVar.b(str);
                ijVar.e(str2);
                ijVar.d(sContext.getPackageName());
                ijVar.f(a);
                Context context2 = sContext;
                ijVar.c(com.xiaomi.push.h.m2213a(context2, context2.getPackageName()));
                Context context3 = sContext;
                ijVar.b(com.xiaomi.push.h.a(context3, context3.getPackageName()));
                ijVar.h("4_9_1");
                ijVar.a(40091);
                ijVar.a(hx.Init);
                if (!TextUtils.isEmpty(str3)) {
                    ijVar.g(str3);
                }
                if (!com.xiaomi.push.m.m2405d()) {
                    String e = com.xiaomi.push.j.e(sContext);
                    if (!TextUtils.isEmpty(e)) {
                        ijVar.i(bp.a(e) + "," + com.xiaomi.push.j.h(sContext));
                    }
                }
                int a2 = com.xiaomi.push.j.a();
                if (a2 >= 0) {
                    ijVar.c(a2);
                }
                ao.a(sContext).a(ijVar, z);
                sContext.getSharedPreferences("mipush_extra", 4).getBoolean("mipush_registed", true);
            } else {
                if (1 == PushMessageHelper.getPushMode(sContext)) {
                    checkNotNull(miPushClientCallback, "callback");
                    miPushClientCallback.onInitializeResult(0L, null, b.m1906a(sContext).c());
                } else {
                    ArrayList arrayList = new ArrayList();
                    arrayList.add(b.m1906a(sContext).c());
                    PushMessageHelper.sendCommandMessageBroadcast(sContext, PushMessageHelper.generateCommandMessage(ey.COMMAND_REGISTER.f318a, arrayList, 0L, null, null, null));
                }
                ao.a(sContext).m1895a();
                if (b.m1906a(sContext).m1909a()) {
                    ii iiVar = new ii();
                    iiVar.b(b.m1906a(sContext).m1907a());
                    iiVar.c(ht.ClientInfoUpdate.f489a);
                    iiVar.a(bd.a());
                    HashMap hashMap = new HashMap();
                    iiVar.f628a = hashMap;
                    Context context4 = sContext;
                    hashMap.put("app_version", com.xiaomi.push.h.m2213a(context4, context4.getPackageName()));
                    Map<String, String> map = iiVar.f628a;
                    Context context5 = sContext;
                    map.put("app_version_code", Integer.toString(com.xiaomi.push.h.a(context5, context5.getPackageName())));
                    iiVar.f628a.put("push_sdk_vn", "4_9_1");
                    iiVar.f628a.put("push_sdk_vc", Integer.toString(40091));
                    String e2 = b.m1906a(sContext).e();
                    if (!TextUtils.isEmpty(e2)) {
                        iiVar.f628a.put("deviceid", e2);
                    }
                    ao.a(sContext).a((ao) iiVar, hj.Notification, false, (hw) null);
                }
                if (!com.xiaomi.push.n.m2407a(sContext, "update_devId", false)) {
                    updateImeiOrOaid();
                    com.xiaomi.push.n.a(sContext, "update_devId", true);
                }
                if (shouldUseMIUIPush(sContext) && shouldPullNotification(sContext)) {
                    ii iiVar2 = new ii();
                    iiVar2.b(b.m1906a(sContext).m1907a());
                    iiVar2.c(ht.PullOfflineMessage.f489a);
                    iiVar2.a(bd.a());
                    iiVar2.a(false);
                    ao.a(sContext).a((ao) iiVar2, hj.Notification, false, (hw) null, false);
                    addPullNotificationTime(sContext);
                }
            }
            addRegRequestTime(sContext);
            scheduleOcVersionCheckJob();
            scheduleDataCollectionJobs(sContext);
            initEventPerfLogic(sContext);
            av.a(sContext);
            if (!sContext.getPackageName().equals(com.xiaomi.stat.c.c.a)) {
                if (Logger.getUserLogger() != null) {
                    Logger.setLogger(sContext, Logger.getUserLogger());
                }
                com.xiaomi.channel.commonutils.logger.b.a(2);
            }
            operateSyncAction(context);
        } catch (Throwable th) {
            com.xiaomi.channel.commonutils.logger.b.a(th);
        }
    }

    public static void operateSyncAction(Context context) {
        if ("syncing".equals(af.a(sContext).a(au.DISABLE_PUSH))) {
            disablePush(sContext);
        }
        if ("syncing".equals(af.a(sContext).a(au.ENABLE_PUSH))) {
            enablePush(sContext);
        }
        if ("syncing".equals(af.a(sContext).a(au.UPLOAD_HUAWEI_TOKEN))) {
            syncAssemblePushToken(sContext);
        }
        if ("syncing".equals(af.a(sContext).a(au.UPLOAD_FCM_TOKEN))) {
            syncAssembleFCMPushToken(sContext);
        }
        if ("syncing".equals(af.a(sContext).a(au.UPLOAD_COS_TOKEN))) {
            syncAssembleCOSPushToken(context);
        }
        if ("syncing".equals(af.a(sContext).a(au.UPLOAD_FTOS_TOKEN))) {
            syncAssembleFTOSPushToken(context);
        }
    }

    public static void reInitialize(Context context, hx hxVar) {
        com.xiaomi.channel.commonutils.logger.b.e("re-register reason: " + hxVar);
        String a = bp.a(6);
        String m1907a = b.m1906a(context).m1907a();
        String b = b.m1906a(context).b();
        b.m1906a(context).m1908a();
        clearExtrasForInitialize(context);
        clearNotification(context);
        b.m1906a(context).a(Constants.a());
        b.m1906a(context).a(m1907a, b, a);
        ij ijVar = new ij();
        ijVar.a(bd.b());
        ijVar.b(m1907a);
        ijVar.e(b);
        ijVar.f(a);
        ijVar.d(context.getPackageName());
        ijVar.c(com.xiaomi.push.h.m2213a(context, context.getPackageName()));
        ijVar.b(com.xiaomi.push.h.a(context, context.getPackageName()));
        ijVar.h("4_9_1");
        ijVar.a(40091);
        ijVar.a(hxVar);
        int a2 = com.xiaomi.push.j.a();
        if (a2 >= 0) {
            ijVar.c(a2);
        }
        ao.a(context).a(ijVar, false);
    }

    public static void registerNetworkReceiver(Context context) {
        try {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
            intentFilter.addCategory("android.intent.category.DEFAULT");
            com.xiaomi.push.o.a(context.getApplicationContext(), new NetworkStatusReceiver(null), intentFilter);
            com.xiaomi.push.o.a(context, NetworkStatusReceiver.class);
        } catch (Throwable th) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("dynamic register network status receiver failed:" + th);
        }
    }

    public static void registerPush(Context context, String str, String str2) {
        registerPush(context, str, str2, new PushConfiguration());
    }

    public static void registerPush(Context context, String str, String str2, PushConfiguration pushConfiguration) {
        registerPush(context, str, str2, pushConfiguration, null, null);
    }

    public static void registerPush(Context context, final String str, final String str2, PushConfiguration pushConfiguration, final String str3, final ICallbackResult iCallbackResult) {
        checkNotNull(context, CoreConstants.CONTEXT_SCOPE_VALUE);
        checkNotNull(str, "appID");
        checkNotNull(str2, "appToken");
        Context applicationContext = context.getApplicationContext();
        sContext = applicationContext;
        if (applicationContext == null) {
            sContext = context;
        }
        Context context2 = sContext;
        com.xiaomi.push.v.a(context2);
        if (!NetworkStatusReceiver.a()) {
            registerNetworkReceiver(sContext);
        }
        f.a(sContext).a(pushConfiguration);
        com.xiaomi.push.al.a(context2).a(new Runnable() { // from class: com.xiaomi.mipush.sdk.MiPushClient.1
            @Override // java.lang.Runnable
            public void run() {
                MiPushClient.initialize(MiPushClient.sContext, str, str2, null, str3, iCallbackResult);
            }
        });
    }

    public static synchronized void removeAcceptTime(Context context) {
        synchronized (MiPushClient.class) {
            SharedPreferences.Editor edit = context.getSharedPreferences("mipush_extra", 0).edit();
            edit.remove("accept_time");
            com.xiaomi.push.t.a(edit);
        }
    }

    public static synchronized void removeAccount(Context context, String str) {
        synchronized (MiPushClient.class) {
            SharedPreferences.Editor edit = context.getSharedPreferences("mipush_extra", 0).edit();
            edit.remove("account_" + str).commit();
        }
    }

    public static synchronized void removeAlias(Context context, String str) {
        synchronized (MiPushClient.class) {
            SharedPreferences.Editor edit = context.getSharedPreferences("mipush_extra", 0).edit();
            edit.remove("alias_" + str).commit();
        }
    }

    public static synchronized void removeAllAccounts(Context context) {
        synchronized (MiPushClient.class) {
            for (String str : getAllUserAccount(context)) {
                removeAccount(context, str);
            }
        }
    }

    public static synchronized void removeAllAliases(Context context) {
        synchronized (MiPushClient.class) {
            for (String str : getAllAlias(context)) {
                removeAlias(context, str);
            }
        }
    }

    public static synchronized void removeAllTopics(Context context) {
        synchronized (MiPushClient.class) {
            for (String str : getAllTopic(context)) {
                removeTopic(context, str);
            }
        }
    }

    public static synchronized void removeTopic(Context context, String str) {
        synchronized (MiPushClient.class) {
            SharedPreferences.Editor edit = context.getSharedPreferences("mipush_extra", 0).edit();
            edit.remove("topic_" + str).commit();
        }
    }

    public static void reportIgnoreRegMessageClicked(Context context, String str, hw hwVar, String str2, String str3) {
        ii iiVar = new ii();
        if (TextUtils.isEmpty(str3)) {
            com.xiaomi.channel.commonutils.logger.b.d("do not report clicked message");
            return;
        }
        iiVar.b(str3);
        iiVar.c("bar:click");
        iiVar.a(str);
        iiVar.a(false);
        ao.a(context).a(iiVar, hj.Notification, false, true, hwVar, true, str2, str3);
    }

    public static void reportMessageClicked(Context context, String str, hw hwVar, String str2) {
        ii iiVar = new ii();
        if (TextUtils.isEmpty(str2)) {
            if (!b.m1906a(context).m1911b()) {
                com.xiaomi.channel.commonutils.logger.b.d("do not report clicked message");
                return;
            }
            str2 = b.m1906a(context).m1907a();
        }
        iiVar.b(str2);
        iiVar.c("bar:click");
        iiVar.a(str);
        iiVar.a(false);
        ao.a(context).a((ao) iiVar, hj.Notification, false, hwVar);
    }

    public static void scheduleDataCollectionJobs(Context context) {
        if (ba.a(sContext).a(ho.DataCollectionSwitch.a(), getDefaultSwitch())) {
            dn.a().a(new r(context));
            com.xiaomi.push.al.a(sContext).a(new Runnable() { // from class: com.xiaomi.mipush.sdk.MiPushClient.2
                @Override // java.lang.Runnable
                public void run() {
                    Cdo.a(MiPushClient.sContext);
                }
            }, 10);
        }
    }

    public static void scheduleOcVersionCheckJob() {
        com.xiaomi.push.al.a(sContext).a(new ae(sContext), ba.a(sContext).a(ho.OcVersionCheckFrequency.a(), 86400), 5);
    }

    public static void setAlias(Context context, String str, String str2) {
        if (!TextUtils.isEmpty(str)) {
            setCommand(context, ey.COMMAND_SET_ALIAS.f318a, str, str2);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0031, code lost:
        if (1 == com.xiaomi.mipush.sdk.PushMessageHelper.getPushMode(r11)) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:11:0x0033, code lost:
        com.xiaomi.mipush.sdk.PushMessageHandler.a(r11, r14, r12, 0, null, r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:12:0x003e, code lost:
        com.xiaomi.mipush.sdk.PushMessageHelper.sendCommandMessageBroadcast(r11, com.xiaomi.mipush.sdk.PushMessageHelper.generateCommandMessage(r0.f318a, r6, 0, null, r14, null));
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x00a9, code lost:
        if (1 == com.xiaomi.mipush.sdk.PushMessageHelper.getPushMode(r11)) goto L10;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:?, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:?, code lost:
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static void setCommand(android.content.Context r11, java.lang.String r12, java.lang.String r13, java.lang.String r14) {
        /*
            java.util.ArrayList r6 = new java.util.ArrayList
            r6.<init>()
            boolean r0 = android.text.TextUtils.isEmpty(r13)
            if (r0 != 0) goto Le
            r6.add(r13)
        Le:
            com.xiaomi.push.ey r0 = com.xiaomi.push.ey.COMMAND_SET_ALIAS
            java.lang.String r1 = r0.f318a
            boolean r1 = r1.equalsIgnoreCase(r12)
            r2 = 1
            if (r1 == 0) goto L50
            long r3 = java.lang.System.currentTimeMillis()
            long r7 = aliasSetTime(r11, r13)
            long r3 = r3 - r7
            long r3 = java.lang.Math.abs(r3)
            r7 = 86400000(0x5265c00, double:4.2687272E-316)
            int r1 = (r3 > r7 ? 1 : (r3 == r7 ? 0 : -1))
            if (r1 >= 0) goto L50
            int r13 = com.xiaomi.mipush.sdk.PushMessageHelper.getPushMode(r11)
            if (r2 != r13) goto L3e
        L33:
            r3 = 0
            r5 = 0
            r0 = r11
            r1 = r14
            r2 = r12
            com.xiaomi.mipush.sdk.PushMessageHandler.a(r0, r1, r2, r3, r5, r6)
            goto Lc9
        L3e:
            java.lang.String r0 = r0.f318a
            r2 = 0
            r4 = 0
            r12 = 0
            r1 = r6
            r5 = r14
            r6 = r12
            com.xiaomi.mipush.sdk.MiPushCommandMessage r12 = com.xiaomi.mipush.sdk.PushMessageHelper.generateCommandMessage(r0, r1, r2, r4, r5, r6)
            com.xiaomi.mipush.sdk.PushMessageHelper.sendCommandMessageBroadcast(r11, r12)
            goto Lc9
        L50:
            com.xiaomi.push.ey r0 = com.xiaomi.push.ey.COMMAND_UNSET_ALIAS
            java.lang.String r0 = r0.f318a
            boolean r0 = r0.equalsIgnoreCase(r12)
            java.lang.String r1 = " is unseted"
            r3 = 3
            r4 = 0
            if (r0 == 0) goto L87
            long r7 = aliasSetTime(r11, r13)
            int r0 = (r7 > r4 ? 1 : (r7 == r4 ? 0 : -1))
            if (r0 >= 0) goto L87
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "Don't cancel alias for "
        L6e:
            r11.append(r12)
            java.lang.String r12 = r6.toString()
            java.lang.String r12 = com.xiaomi.push.bp.a(r12, r3)
            r11.append(r12)
            r11.append(r1)
            java.lang.String r11 = r11.toString()
            com.xiaomi.channel.commonutils.logger.b.m1859a(r11)
            goto Lc9
        L87:
            com.xiaomi.push.ey r0 = com.xiaomi.push.ey.COMMAND_SET_ACCOUNT
            java.lang.String r7 = r0.f318a
            boolean r7 = r7.equalsIgnoreCase(r12)
            if (r7 == 0) goto Lac
            long r7 = java.lang.System.currentTimeMillis()
            long r9 = accountSetTime(r11, r13)
            long r7 = r7 - r9
            long r7 = java.lang.Math.abs(r7)
            r9 = 3600000(0x36ee80, double:1.7786363E-317)
            int r7 = (r7 > r9 ? 1 : (r7 == r9 ? 0 : -1))
            if (r7 >= 0) goto Lac
            int r13 = com.xiaomi.mipush.sdk.PushMessageHelper.getPushMode(r11)
            if (r2 != r13) goto L3e
            goto L33
        Lac:
            com.xiaomi.push.ey r0 = com.xiaomi.push.ey.COMMAND_UNSET_ACCOUNT
            java.lang.String r0 = r0.f318a
            boolean r0 = r0.equalsIgnoreCase(r12)
            if (r0 == 0) goto Lc6
            long r7 = accountSetTime(r11, r13)
            int r13 = (r7 > r4 ? 1 : (r7 == r4 ? 0 : -1))
            if (r13 >= 0) goto Lc6
            java.lang.StringBuilder r11 = new java.lang.StringBuilder
            r11.<init>()
            java.lang.String r12 = "Don't cancel account for "
            goto L6e
        Lc6:
            setCommand(r11, r12, r6, r14)
        Lc9:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.mipush.sdk.MiPushClient.setCommand(android.content.Context, java.lang.String, java.lang.String, java.lang.String):void");
    }

    public static void setCommand(Context context, String str, ArrayList<String> arrayList, String str2) {
        if (TextUtils.isEmpty(b.m1906a(context).m1907a())) {
            return;
        }
        id idVar = new id();
        String a = bd.a();
        idVar.a(a);
        idVar.b(b.m1906a(context).m1907a());
        idVar.c(str);
        Iterator<String> it = arrayList.iterator();
        while (it.hasNext()) {
            idVar.m2284a(it.next());
        }
        idVar.e(str2);
        idVar.d(context.getPackageName());
        com.xiaomi.channel.commonutils.logger.b.e("cmd:" + str + ", " + a);
        ao.a(context).a((ao) idVar, hj.Command, (hw) null);
    }

    public static void setUserAccount(Context context, String str, String str2) {
        if (!TextUtils.isEmpty(str)) {
            setCommand(context, ey.COMMAND_SET_ACCOUNT.f318a, str, str2);
        }
    }

    public static boolean shouldPullNotification(Context context) {
        return Math.abs(System.currentTimeMillis() - context.getSharedPreferences("mipush_extra", 0).getLong("last_pull_notification", -1L)) > 300000;
    }

    public static boolean shouldSendRegRequest(Context context) {
        return Math.abs(System.currentTimeMillis() - context.getSharedPreferences("mipush_extra", 0).getLong("last_reg_request", -1L)) > 5000;
    }

    public static boolean shouldUseMIUIPush(Context context) {
        return ao.a(context).m1897a();
    }

    public static void subscribe(Context context, String str, String str2) {
        if (TextUtils.isEmpty(b.m1906a(context).m1907a()) || TextUtils.isEmpty(str)) {
            return;
        }
        if (Math.abs(System.currentTimeMillis() - topicSubscribedTime(context, str)) <= 86400000) {
            if (1 == PushMessageHelper.getPushMode(context)) {
                PushMessageHandler.a(context, str2, 0L, null, str);
                return;
            }
            ArrayList arrayList = new ArrayList();
            arrayList.add(str);
            PushMessageHelper.sendCommandMessageBroadcast(context, PushMessageHelper.generateCommandMessage(ey.COMMAND_SUBSCRIBE_TOPIC.f318a, arrayList, 0L, null, null, null));
            return;
        }
        in inVar = new in();
        String a = bd.a();
        inVar.a(a);
        inVar.b(b.m1906a(context).m1907a());
        inVar.c(str);
        inVar.d(context.getPackageName());
        inVar.e(str2);
        com.xiaomi.channel.commonutils.logger.b.e("cmd:" + ey.COMMAND_SUBSCRIBE_TOPIC + ", " + a);
        ao.a(context).a((ao) inVar, hj.Subscription, (hw) null);
    }

    public static void syncAssembleCOSPushToken(Context context) {
        ao.a(context).a((String) null, au.UPLOAD_COS_TOKEN, e.ASSEMBLE_PUSH_COS);
    }

    public static void syncAssembleFCMPushToken(Context context) {
        ao.a(context).a((String) null, au.UPLOAD_FCM_TOKEN, e.ASSEMBLE_PUSH_FCM);
    }

    public static void syncAssembleFTOSPushToken(Context context) {
        ao.a(context).a((String) null, au.UPLOAD_FTOS_TOKEN, e.ASSEMBLE_PUSH_FTOS);
    }

    public static void syncAssemblePushToken(Context context) {
        ao.a(context).a((String) null, au.UPLOAD_HUAWEI_TOKEN, e.ASSEMBLE_PUSH_HUAWEI);
    }

    public static long topicSubscribedTime(Context context, String str) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("mipush_extra", 0);
        return sharedPreferences.getLong("topic_" + str, -1L);
    }

    public static void unregisterPush(Context context) {
        i.c(context);
        ba.a(context).a();
        if (!b.m1906a(context).m1911b()) {
            return;
        }
        ip ipVar = new ip();
        ipVar.a(bd.a());
        ipVar.b(b.m1906a(context).m1907a());
        ipVar.c(b.m1906a(context).c());
        ipVar.e(b.m1906a(context).b());
        ipVar.d(context.getPackageName());
        ao.a(context).a(ipVar);
        PushMessageHandler.a();
        PushMessageHandler.b();
        b.m1906a(context).m1910b();
        clearLocalNotificationType(context);
        clearNotification(context);
        clearExtras(context);
    }

    public static void unsetUserAccount(Context context, String str, String str2) {
        setCommand(context, ey.COMMAND_UNSET_ACCOUNT.f318a, str, str2);
    }

    public static void unsubscribe(Context context, String str, String str2) {
        if (!b.m1906a(context).m1911b()) {
            return;
        }
        if (topicSubscribedTime(context, str) < 0) {
            com.xiaomi.channel.commonutils.logger.b.m1859a("Don't cancel subscribe for " + str + " is unsubscribed");
            return;
        }
        ir irVar = new ir();
        String a = bd.a();
        irVar.a(a);
        irVar.b(b.m1906a(context).m1907a());
        irVar.c(str);
        irVar.d(context.getPackageName());
        irVar.e(str2);
        com.xiaomi.channel.commonutils.logger.b.e("cmd:" + ey.COMMAND_UNSUBSCRIBE_TOPIC + ", " + a);
        ao.a(context).a((ao) irVar, hj.UnSubscription, (hw) null);
    }

    public static void updateImeiOrOaid() {
        new Thread(new Runnable() { // from class: com.xiaomi.mipush.sdk.MiPushClient.3
            @Override // java.lang.Runnable
            public void run() {
                String d;
                if (!com.xiaomi.push.m.m2405d()) {
                    if (com.xiaomi.push.j.d(MiPushClient.sContext) == null && !com.xiaomi.push.ba.a(MiPushClient.sContext).mo1967a()) {
                        return;
                    }
                    ii iiVar = new ii();
                    iiVar.b(b.m1906a(MiPushClient.sContext).m1907a());
                    iiVar.c(ht.ClientInfoUpdate.f489a);
                    iiVar.a(bd.a());
                    iiVar.a(new HashMap());
                    String str = "";
                    if (!TextUtils.isEmpty(com.xiaomi.push.j.d(MiPushClient.sContext))) {
                        str = str + bp.a(d);
                    }
                    String f = com.xiaomi.push.j.f(MiPushClient.sContext);
                    if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(f)) {
                        str = str + "," + f;
                    }
                    if (!TextUtils.isEmpty(str)) {
                        iiVar.m2309a().put("imei_md5", str);
                    }
                    com.xiaomi.push.ba.a(MiPushClient.sContext).a(iiVar.m2309a());
                    int a = com.xiaomi.push.j.a();
                    if (a >= 0) {
                        iiVar.m2309a().put("space_id", Integer.toString(a));
                    }
                    ao.a(MiPushClient.sContext).a((ao) iiVar, hj.Notification, false, (hw) null);
                }
            }
        }).start();
    }
}
