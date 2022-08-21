package com.xiaomi.mipush.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.UIMsg;
import com.xiaomi.mipush.sdk.PushMessageHandler;
import com.xiaomi.push.eo;
import com.xiaomi.push.ey;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* loaded from: classes3.dex */
public class MessageHandleService extends BaseService {
    public static ConcurrentLinkedQueue<a> a = new ConcurrentLinkedQueue<>();

    /* renamed from: a  reason: collision with other field name */
    public static ExecutorService f24a = new ThreadPoolExecutor(1, 1, 15, TimeUnit.SECONDS, new LinkedBlockingQueue());

    /* loaded from: classes3.dex */
    public static class a {
        public Intent a;

        /* renamed from: a  reason: collision with other field name */
        public PushMessageReceiver f25a;

        public a(Intent intent, PushMessageReceiver pushMessageReceiver) {
            this.f25a = pushMessageReceiver;
            this.a = intent;
        }

        public Intent a() {
            return this.a;
        }

        /* renamed from: a  reason: collision with other method in class */
        public PushMessageReceiver m1879a() {
            return this.f25a;
        }
    }

    public static void a(Context context, Intent intent) {
        if (intent == null) {
            return;
        }
        b(context);
    }

    public static void a(Context context, a aVar) {
        String[] stringArrayExtra;
        if (aVar == null) {
            return;
        }
        try {
            PushMessageReceiver m1879a = aVar.m1879a();
            Intent a2 = aVar.a();
            int intExtra = a2.getIntExtra("message_type", 1);
            if (intExtra == 1) {
                PushMessageHandler.a a3 = am.a(context).a(a2);
                int intExtra2 = a2.getIntExtra("eventMessageType", -1);
                if (a3 == null) {
                    return;
                }
                if (a3 instanceof MiPushMessage) {
                    MiPushMessage miPushMessage = (MiPushMessage) a3;
                    if (!miPushMessage.isArrivedMessage()) {
                        m1879a.onReceiveMessage(context, miPushMessage);
                    }
                    if (miPushMessage.getPassThrough() == 1) {
                        eo.a(context.getApplicationContext()).a(context.getPackageName(), a2, UIMsg.m_AppUI.MSG_APP_VERSION, (String) null);
                        com.xiaomi.channel.commonutils.logger.b.e("begin execute onReceivePassThroughMessage from " + miPushMessage.getMessageId());
                        m1879a.onReceivePassThroughMessage(context, miPushMessage);
                        return;
                    } else if (!miPushMessage.isNotified()) {
                        com.xiaomi.channel.commonutils.logger.b.e("begin execute onNotificationMessageArrived from " + miPushMessage.getMessageId());
                        m1879a.onNotificationMessageArrived(context, miPushMessage);
                        return;
                    } else {
                        if (intExtra2 == 1000) {
                            eo.a(context.getApplicationContext()).a(context.getPackageName(), a2, com.xiaomi.stat.c.b.g, (String) null);
                        } else {
                            eo.a(context.getApplicationContext()).a(context.getPackageName(), a2, 3007, (String) null);
                        }
                        com.xiaomi.channel.commonutils.logger.b.e("begin execute onNotificationMessageClicked from\u3000" + miPushMessage.getMessageId());
                        m1879a.onNotificationMessageClicked(context, miPushMessage);
                        return;
                    }
                } else if (!(a3 instanceof MiPushCommandMessage)) {
                    return;
                } else {
                    MiPushCommandMessage miPushCommandMessage = (MiPushCommandMessage) a3;
                    com.xiaomi.channel.commonutils.logger.b.e("begin execute onCommandResult, command=" + miPushCommandMessage.getCommand() + ", resultCode=" + miPushCommandMessage.getResultCode() + ", reason=" + miPushCommandMessage.getReason());
                    m1879a.onCommandResult(context, miPushCommandMessage);
                    if (!TextUtils.equals(miPushCommandMessage.getCommand(), ey.COMMAND_REGISTER.f318a)) {
                        return;
                    }
                    m1879a.onReceiveRegisterResult(context, miPushCommandMessage);
                    PushMessageHandler.a(context, miPushCommandMessage);
                    if (miPushCommandMessage.getResultCode() != 0) {
                        return;
                    }
                }
            } else if (intExtra != 3) {
                if (intExtra != 5 || !"error_lack_of_permission".equals(a2.getStringExtra("error_type")) || (stringArrayExtra = a2.getStringArrayExtra(SDKInitializer.SDK_BROADTCAST_INTENT_EXTRA_INFO_KEY_ERROR_MESSAGE)) == null) {
                    return;
                }
                com.xiaomi.channel.commonutils.logger.b.e("begin execute onRequirePermissions, lack of necessary permissions");
                m1879a.onRequirePermissions(context, stringArrayExtra);
                return;
            } else {
                MiPushCommandMessage miPushCommandMessage2 = (MiPushCommandMessage) a2.getSerializableExtra("key_command");
                com.xiaomi.channel.commonutils.logger.b.e("(Local) begin execute onCommandResult, command=" + miPushCommandMessage2.getCommand() + ", resultCode=" + miPushCommandMessage2.getResultCode() + ", reason=" + miPushCommandMessage2.getReason());
                m1879a.onCommandResult(context, miPushCommandMessage2);
                if (!TextUtils.equals(miPushCommandMessage2.getCommand(), ey.COMMAND_REGISTER.f318a)) {
                    return;
                }
                m1879a.onReceiveRegisterResult(context, miPushCommandMessage2);
                PushMessageHandler.a(context, miPushCommandMessage2);
                if (miPushCommandMessage2.getResultCode() != 0) {
                    return;
                }
            }
            i.b(context);
        } catch (RuntimeException e) {
            com.xiaomi.channel.commonutils.logger.b.a(e);
        }
    }

    public static void addJob(Context context, a aVar) {
        if (aVar != null) {
            a.add(aVar);
            b(context);
            startService(context);
        }
    }

    public static void b(Context context) {
        if (!f24a.isShutdown()) {
            f24a.execute(new z(context));
        }
    }

    public static void c(Context context) {
        try {
            a(context, a.poll());
        } catch (RuntimeException e) {
            com.xiaomi.channel.commonutils.logger.b.a(e);
        }
    }

    public static void startService(Context context) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(context, MessageHandleService.class));
        com.xiaomi.push.al.a(context).a(new y(context, intent));
    }

    @Override // com.xiaomi.mipush.sdk.BaseService
    /* renamed from: a */
    public boolean mo1885a() {
        ConcurrentLinkedQueue<a> concurrentLinkedQueue = a;
        return concurrentLinkedQueue != null && concurrentLinkedQueue.size() > 0;
    }

    @Override // com.xiaomi.mipush.sdk.BaseService, android.app.Service
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override // com.xiaomi.mipush.sdk.BaseService, android.app.Service
    public void onStart(Intent intent, int i) {
        super.onStart(intent, i);
    }
}
