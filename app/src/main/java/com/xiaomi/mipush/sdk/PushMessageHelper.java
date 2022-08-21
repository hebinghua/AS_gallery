package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import com.xiaomi.push.hw;
import com.xiaomi.push.im;
import java.util.List;

/* loaded from: classes3.dex */
public class PushMessageHelper {
    public static int pushMode;

    public static MiPushCommandMessage generateCommandMessage(String str, List<String> list, long j, String str2, String str3, List<String> list2) {
        MiPushCommandMessage miPushCommandMessage = new MiPushCommandMessage();
        miPushCommandMessage.setCommand(str);
        miPushCommandMessage.setCommandArguments(list);
        miPushCommandMessage.setResultCode(j);
        miPushCommandMessage.setReason(str2);
        miPushCommandMessage.setCategory(str3);
        miPushCommandMessage.setAutoMarkPkgs(list2);
        return miPushCommandMessage;
    }

    public static MiPushMessage generateMessage(im imVar, hw hwVar, boolean z) {
        MiPushMessage miPushMessage = new MiPushMessage();
        miPushMessage.setMessageId(imVar.m2331a());
        if (!TextUtils.isEmpty(imVar.d())) {
            miPushMessage.setMessageType(1);
            miPushMessage.setAlias(imVar.d());
        } else if (!TextUtils.isEmpty(imVar.c())) {
            miPushMessage.setMessageType(2);
            miPushMessage.setTopic(imVar.c());
        } else if (!TextUtils.isEmpty(imVar.f())) {
            miPushMessage.setMessageType(3);
            miPushMessage.setUserAccount(imVar.f());
        } else {
            miPushMessage.setMessageType(0);
        }
        miPushMessage.setCategory(imVar.e());
        if (imVar.a() != null) {
            miPushMessage.setContent(imVar.a().c());
        }
        if (hwVar != null) {
            if (TextUtils.isEmpty(miPushMessage.getMessageId())) {
                miPushMessage.setMessageId(hwVar.m2259a());
            }
            if (TextUtils.isEmpty(miPushMessage.getTopic())) {
                miPushMessage.setTopic(hwVar.m2264b());
            }
            miPushMessage.setDescription(hwVar.d());
            miPushMessage.setTitle(hwVar.m2267c());
            miPushMessage.setNotifyType(hwVar.a());
            miPushMessage.setNotifyId(hwVar.c());
            miPushMessage.setPassThrough(hwVar.b());
            miPushMessage.setExtra(hwVar.m2260a());
        }
        miPushMessage.setNotified(z);
        return miPushMessage;
    }

    public static int getPushMode(Context context) {
        if (pushMode == 0) {
            setPushMode(isUseCallbackPushMode(context) ? 1 : 2);
        }
        return pushMode;
    }

    public static boolean isIntentAvailable(Context context, Intent intent) {
        try {
            List<ResolveInfo> queryBroadcastReceivers = context.getPackageManager().queryBroadcastReceivers(intent, 32);
            if (queryBroadcastReceivers != null) {
                if (!queryBroadcastReceivers.isEmpty()) {
                    return true;
                }
            }
            return false;
        } catch (Exception unused) {
            return true;
        }
    }

    public static boolean isUseCallbackPushMode(Context context) {
        Intent intent = new Intent("com.xiaomi.mipush.RECEIVE_MESSAGE");
        intent.setClassName(context.getPackageName(), "com.xiaomi.mipush.sdk.PushServiceReceiver");
        return isIntentAvailable(context, intent);
    }

    public static void sendCommandMessageBroadcast(Context context, MiPushCommandMessage miPushCommandMessage) {
        Intent intent = new Intent("com.xiaomi.mipush.RECEIVE_MESSAGE");
        intent.setPackage(context.getPackageName());
        intent.putExtra("message_type", 3);
        intent.putExtra("key_command", miPushCommandMessage);
        new PushServiceReceiver().onReceive(context, intent);
    }

    public static void setPushMode(int i) {
        pushMode = i;
    }
}
