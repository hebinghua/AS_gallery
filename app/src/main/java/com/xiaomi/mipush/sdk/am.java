package com.xiaomi.mipush.sdk;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.text.TextUtils;
import ch.qos.logback.core.spi.ComponentTracker;
import com.xiaomi.mipush.sdk.PushMessageHandler;
import com.xiaomi.push.Cif;
import com.xiaomi.push.bp;
import com.xiaomi.push.bx;
import com.xiaomi.push.db;
import com.xiaomi.push.en;
import com.xiaomi.push.eo;
import com.xiaomi.push.ey;
import com.xiaomi.push.hj;
import com.xiaomi.push.ho;
import com.xiaomi.push.ht;
import com.xiaomi.push.hv;
import com.xiaomi.push.hw;
import com.xiaomi.push.hx;
import com.xiaomi.push.hz;
import com.xiaomi.push.ia;
import com.xiaomi.push.ie;
import com.xiaomi.push.ig;
import com.xiaomi.push.ih;
import com.xiaomi.push.ii;
import com.xiaomi.push.ik;
import com.xiaomi.push.im;
import com.xiaomi.push.io;
import com.xiaomi.push.iq;
import com.xiaomi.push.is;
import com.xiaomi.push.it;
import com.xiaomi.push.iu;
import com.xiaomi.push.iz;
import com.xiaomi.push.service.ay;
import com.xiaomi.push.service.ba;
import com.xiaomi.push.service.bb;
import com.xiaomi.push.service.bk;
import com.xiaomi.push.service.br;
import com.xiaomi.push.w;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.TimeZone;

/* loaded from: classes3.dex */
public class am {
    public static am a;

    /* renamed from: a  reason: collision with other field name */
    public static Object f42a = new Object();

    /* renamed from: a  reason: collision with other field name */
    public static Queue<String> f43a;

    /* renamed from: a  reason: collision with other field name */
    public Context f44a;

    public am(Context context) {
        Context applicationContext = context.getApplicationContext();
        this.f44a = applicationContext;
        if (applicationContext == null) {
            this.f44a = context;
        }
    }

    public static Intent a(Context context, String str, Map<String, String> map, int i) {
        return com.xiaomi.push.service.al.b(context, str, map, i);
    }

    public static am a(Context context) {
        if (a == null) {
            a = new am(context);
        }
        return a;
    }

    public static boolean a(Context context, String str) {
        synchronized (f42a) {
            b.m1906a(context);
            SharedPreferences a2 = b.a(context);
            if (f43a == null) {
                String[] split = a2.getString("pref_msg_ids", "").split(",");
                f43a = new LinkedList();
                for (String str2 : split) {
                    f43a.add(str2);
                }
            }
            if (f43a.contains(str)) {
                return true;
            }
            f43a.add(str);
            if (f43a.size() > 25) {
                f43a.poll();
            }
            String a3 = bp.a(f43a, ",");
            SharedPreferences.Editor edit = a2.edit();
            edit.putString("pref_msg_ids", a3);
            com.xiaomi.push.t.a(edit);
            return false;
        }
    }

    public PushMessageHandler.a a(Intent intent) {
        String str;
        eo a2;
        String packageName;
        String str2;
        eo a3;
        String packageName2;
        String format;
        String action = intent.getAction();
        com.xiaomi.channel.commonutils.logger.b.m1859a("receive an intent from server, action=" + action);
        String stringExtra = intent.getStringExtra("mrt");
        if (stringExtra == null) {
            stringExtra = Long.toString(System.currentTimeMillis());
        }
        String stringExtra2 = intent.getStringExtra("messageId");
        int intExtra = intent.getIntExtra("eventMessageType", -1);
        if ("com.xiaomi.mipush.RECEIVE_MESSAGE".equals(action)) {
            byte[] byteArrayExtra = intent.getByteArrayExtra("mipush_payload");
            boolean booleanExtra = intent.getBooleanExtra("mipush_notified", false);
            if (byteArrayExtra == null) {
                com.xiaomi.channel.commonutils.logger.b.d("receiving an empty message, drop");
                eo.a(this.f44a).a(this.f44a.getPackageName(), intent, "12");
                return null;
            }
            Cif cif = new Cif();
            try {
                it.a(cif, byteArrayExtra);
                b m1906a = b.m1906a(this.f44a);
                hw m2293a = cif.m2293a();
                hj a4 = cif.a();
                hj hjVar = hj.SendMessage;
                if (a4 == hjVar && m2293a != null && !m1906a.m1915e() && !booleanExtra) {
                    m2293a.a("mrt", stringExtra);
                    m2293a.a("mat", Long.toString(System.currentTimeMillis()));
                    if (!m1888a(cif)) {
                        b(cif);
                    } else {
                        com.xiaomi.channel.commonutils.logger.b.b("this is a mina's message, ack later");
                        m2293a.a("__hybrid_message_ts", String.valueOf(m2293a.m2257a()));
                        m2293a.a("__hybrid_device_status", String.valueOf((int) it.a(this.f44a, cif)));
                    }
                }
                String str3 = "";
                if (cif.a() == hjVar && !cif.m2301b()) {
                    if (com.xiaomi.push.service.al.m2463a(cif)) {
                        Object[] objArr = new Object[2];
                        objArr[0] = cif.b();
                        if (m2293a != null) {
                            str3 = m2293a.m2259a();
                        }
                        objArr[1] = str3;
                        com.xiaomi.channel.commonutils.logger.b.m1859a(String.format("drop an un-encrypted wake-up messages. %1$s, %2$s", objArr));
                        a3 = eo.a(this.f44a);
                        packageName2 = this.f44a.getPackageName();
                        format = String.format("13: %1$s", cif.b());
                    } else {
                        Object[] objArr2 = new Object[2];
                        objArr2[0] = cif.b();
                        if (m2293a != null) {
                            str3 = m2293a.m2259a();
                        }
                        objArr2[1] = str3;
                        com.xiaomi.channel.commonutils.logger.b.m1859a(String.format("drop an un-encrypted messages. %1$s, %2$s", objArr2));
                        a3 = eo.a(this.f44a);
                        packageName2 = this.f44a.getPackageName();
                        format = String.format("14: %1$s", cif.b());
                    }
                    a3.a(packageName2, intent, format);
                    s.a(this.f44a, cif, booleanExtra);
                    return null;
                }
                if (cif.a() == hjVar && cif.m2301b() && com.xiaomi.push.service.al.m2463a(cif) && (!booleanExtra || m2293a == null || m2293a.m2260a() == null || !m2293a.m2260a().containsKey("notify_effect"))) {
                    Object[] objArr3 = new Object[2];
                    objArr3[0] = cif.b();
                    if (m2293a != null) {
                        str3 = m2293a.m2259a();
                    }
                    objArr3[1] = str3;
                    com.xiaomi.channel.commonutils.logger.b.m1859a(String.format("drop a wake-up messages which not has 'notify_effect' attr. %1$s, %2$s", objArr3));
                    eo.a(this.f44a).a(this.f44a.getPackageName(), intent, String.format("25: %1$s", cif.b()));
                    s.b(this.f44a, cif, booleanExtra);
                    return null;
                }
                if (m1906a.m1913c() || cif.f609a == hj.Registration) {
                    if (!m1906a.m1913c() || !m1906a.f()) {
                        return a(cif, booleanExtra, byteArrayExtra, stringExtra2, intExtra, intent);
                    }
                    if (cif.f609a != hj.UnRegistration) {
                        s.e(this.f44a, cif, booleanExtra);
                        MiPushClient.unregisterPush(this.f44a);
                    } else if (cif.m2301b()) {
                        m1906a.m1908a();
                        MiPushClient.clearExtras(this.f44a);
                        PushMessageHandler.a();
                    } else {
                        com.xiaomi.channel.commonutils.logger.b.d("receiving an un-encrypt unregistration message");
                    }
                } else if (com.xiaomi.push.service.al.m2463a(cif)) {
                    return a(cif, booleanExtra, byteArrayExtra, stringExtra2, intExtra, intent);
                } else {
                    s.e(this.f44a, cif, booleanExtra);
                    boolean m1914d = m1906a.m1914d();
                    com.xiaomi.channel.commonutils.logger.b.d("receive message without registration. need re-register!registered?" + m1914d);
                    eo.a(this.f44a).a(this.f44a.getPackageName(), intent, "15");
                    if (m1914d) {
                        a();
                    }
                }
            } catch (iz e) {
                e = e;
                a2 = eo.a(this.f44a);
                packageName = this.f44a.getPackageName();
                str2 = "16";
                a2.a(packageName, intent, str2);
                com.xiaomi.channel.commonutils.logger.b.a(e);
                return null;
            } catch (Exception e2) {
                e = e2;
                a2 = eo.a(this.f44a);
                packageName = this.f44a.getPackageName();
                str2 = "17";
                a2.a(packageName, intent, str2);
                com.xiaomi.channel.commonutils.logger.b.a(e);
                return null;
            }
        } else if ("com.xiaomi.mipush.ERROR".equals(action)) {
            MiPushCommandMessage miPushCommandMessage = new MiPushCommandMessage();
            Cif cif2 = new Cif();
            try {
                byte[] byteArrayExtra2 = intent.getByteArrayExtra("mipush_payload");
                if (byteArrayExtra2 != null) {
                    it.a(cif2, byteArrayExtra2);
                }
            } catch (iz unused) {
            }
            miPushCommandMessage.setCommand(String.valueOf(cif2.a()));
            miPushCommandMessage.setResultCode(intent.getIntExtra("mipush_error_code", 0));
            miPushCommandMessage.setReason(intent.getStringExtra("mipush_error_msg"));
            com.xiaomi.channel.commonutils.logger.b.d("receive a error message. code = " + intent.getIntExtra("mipush_error_code", 0) + ", msg= " + intent.getStringExtra("mipush_error_msg"));
            return miPushCommandMessage;
        } else if ("com.xiaomi.mipush.MESSAGE_ARRIVED".equals(action)) {
            byte[] byteArrayExtra3 = intent.getByteArrayExtra("mipush_payload");
            if (byteArrayExtra3 == null) {
                com.xiaomi.channel.commonutils.logger.b.d("message arrived: receiving an empty message, drop");
                return null;
            }
            Cif cif3 = new Cif();
            try {
                it.a(cif3, byteArrayExtra3);
                b m1906a2 = b.m1906a(this.f44a);
                if (com.xiaomi.push.service.al.m2463a(cif3)) {
                    str = "message arrived: receive ignore reg message, ignore!";
                } else if (!m1906a2.m1913c()) {
                    str = "message arrived: receive message without registration. need unregister or re-register!";
                } else if (!m1906a2.m1913c() || !m1906a2.f()) {
                    return a(cif3, byteArrayExtra3);
                } else {
                    str = "message arrived: app info is invalidated";
                }
                com.xiaomi.channel.commonutils.logger.b.d(str);
            } catch (Exception e3) {
                com.xiaomi.channel.commonutils.logger.b.d("fail to deal with arrived message. " + e3);
            }
        }
        return null;
    }

    public final PushMessageHandler.a a(Cif cif, boolean z, byte[] bArr, String str, int i, Intent intent) {
        eo a2;
        String packageName;
        String m2126a;
        String str2;
        String str3;
        String str4;
        MiPushMessage miPushMessage;
        eo a3;
        String packageName2;
        String m2126a2;
        int i2;
        String str5;
        ArrayList arrayList = null;
        try {
            iu a4 = ai.a(this.f44a, cif);
            if (a4 == null) {
                com.xiaomi.channel.commonutils.logger.b.d("receiving an un-recognized message. " + cif.f609a);
                eo.a(this.f44a).b(this.f44a.getPackageName(), en.m2126a(i), str, "18");
                s.c(this.f44a, cif, z);
                return null;
            }
            hj a5 = cif.a();
            com.xiaomi.channel.commonutils.logger.b.m1859a("processing a message, action=" + a5);
            switch (an.a[a5.ordinal()]) {
                case 1:
                    if (!cif.m2301b()) {
                        com.xiaomi.channel.commonutils.logger.b.d("receiving an un-encrypt message(SendMessage).");
                        return null;
                    } else if (b.m1906a(this.f44a).m1915e() && !z) {
                        com.xiaomi.channel.commonutils.logger.b.m1859a("receive a message in pause state. drop it");
                        eo.a(this.f44a).a(this.f44a.getPackageName(), en.m2126a(i), str, "12");
                        return null;
                    } else {
                        im imVar = (im) a4;
                        hv a6 = imVar.a();
                        if (a6 == null) {
                            com.xiaomi.channel.commonutils.logger.b.d("receive an empty message without push content, drop it");
                            eo.a(this.f44a).b(this.f44a.getPackageName(), en.m2126a(i), str, "22");
                            s.d(this.f44a, cif, z);
                            return null;
                        }
                        int intExtra = intent.getIntExtra("notification_click_button", 0);
                        if (z) {
                            if (com.xiaomi.push.service.al.m2463a(cif)) {
                                MiPushClient.reportIgnoreRegMessageClicked(this.f44a, a6.m2251a(), cif.m2293a(), cif.f616b, a6.b());
                            } else {
                                hw hwVar = cif.m2293a() != null ? new hw(cif.m2293a()) : new hw();
                                if (hwVar.m2260a() == null) {
                                    hwVar.a(new HashMap());
                                }
                                hwVar.m2260a().put("notification_click_button", String.valueOf(intExtra));
                                MiPushClient.reportMessageClicked(this.f44a, a6.m2251a(), hwVar, a6.b());
                            }
                        }
                        if (!z) {
                            if (!TextUtils.isEmpty(imVar.d()) && MiPushClient.aliasSetTime(this.f44a, imVar.d()) < 0) {
                                MiPushClient.addAlias(this.f44a, imVar.d());
                            } else if (!TextUtils.isEmpty(imVar.c()) && MiPushClient.topicSubscribedTime(this.f44a, imVar.c()) < 0) {
                                MiPushClient.addTopic(this.f44a, imVar.c());
                            }
                        }
                        hw hwVar2 = cif.f610a;
                        if (hwVar2 == null || hwVar2.m2260a() == null) {
                            str3 = null;
                            str4 = null;
                        } else {
                            str3 = cif.f610a.f524a.get("jobkey");
                            str4 = str3;
                        }
                        if (TextUtils.isEmpty(str3)) {
                            str3 = a6.m2251a();
                        }
                        if (z || !a(this.f44a, str3)) {
                            MiPushMessage generateMessage = PushMessageHelper.generateMessage(imVar, cif.m2293a(), z);
                            if (generateMessage.getPassThrough() == 0 && !z && com.xiaomi.push.service.al.m2464a(generateMessage.getExtra())) {
                                com.xiaomi.push.service.al.m2459a(this.f44a, cif, bArr);
                                return null;
                            }
                            com.xiaomi.channel.commonutils.logger.b.m1859a("receive a message, msgid=" + a6.m2251a() + ", jobkey=" + str3 + ", btn=" + intExtra);
                            String a7 = com.xiaomi.push.service.al.a(generateMessage.getExtra(), intExtra);
                            if (z && generateMessage.getExtra() != null && !TextUtils.isEmpty(a7)) {
                                Map<String, String> extra = generateMessage.getExtra();
                                if (intExtra != 0 && cif.m2293a() != null) {
                                    ao.a(this.f44a).a(cif.m2293a().c(), intExtra);
                                }
                                if (com.xiaomi.push.service.al.m2463a(cif)) {
                                    Intent a8 = a(this.f44a, cif.f616b, extra, intExtra);
                                    a8.putExtra("eventMessageType", i);
                                    a8.putExtra("messageId", str);
                                    a8.putExtra("jobkey", str4);
                                    String c = a6.c();
                                    if (!TextUtils.isEmpty(c)) {
                                        a8.putExtra("payload", c);
                                    }
                                    this.f44a.startActivity(a8);
                                    s.a(this.f44a, cif);
                                    eo.a(this.f44a).a(this.f44a.getPackageName(), en.m2126a(i), str, 3006, a7);
                                    return null;
                                }
                                Context context = this.f44a;
                                Intent a9 = a(context, context.getPackageName(), extra, intExtra);
                                if (a9 == null) {
                                    return null;
                                }
                                if (!a7.equals(bk.c)) {
                                    a9.putExtra("key_message", generateMessage);
                                    a9.putExtra("eventMessageType", i);
                                    a9.putExtra("messageId", str);
                                    a9.putExtra("jobkey", str4);
                                }
                                this.f44a.startActivity(a9);
                                s.a(this.f44a, cif);
                                com.xiaomi.channel.commonutils.logger.b.m1859a("start activity succ");
                                eo.a(this.f44a).a(this.f44a.getPackageName(), en.m2126a(i), str, 1006, a7);
                                if (!a7.equals(bk.c)) {
                                    return null;
                                }
                                eo.a(this.f44a).a(this.f44a.getPackageName(), en.m2126a(i), str, "13");
                                return null;
                            }
                            miPushMessage = generateMessage;
                        } else {
                            com.xiaomi.channel.commonutils.logger.b.m1859a("drop a duplicate message, key=" + str3);
                            eo a10 = eo.a(this.f44a);
                            String packageName3 = this.f44a.getPackageName();
                            String m2126a3 = en.m2126a(i);
                            a10.c(packageName3, m2126a3, str, "2:" + str3);
                            miPushMessage = null;
                        }
                        if (cif.m2293a() == null && !z) {
                            a(imVar, cif);
                        }
                        return miPushMessage;
                    }
                case 2:
                    ik ikVar = (ik) a4;
                    String str6 = b.m1906a(this.f44a).f61a;
                    if (TextUtils.isEmpty(str6) || !TextUtils.equals(str6, ikVar.m2322a())) {
                        com.xiaomi.channel.commonutils.logger.b.m1859a("bad Registration result:");
                        eo.a(this.f44a).b(this.f44a.getPackageName(), en.m2126a(i), str, "21");
                        return null;
                    }
                    long m1892a = ao.a(this.f44a).m1892a();
                    if (m1892a > 0 && SystemClock.elapsedRealtime() - m1892a > 900000) {
                        com.xiaomi.channel.commonutils.logger.b.m1859a("The received registration result has expired.");
                        eo.a(this.f44a).b(this.f44a.getPackageName(), en.m2126a(i), str, "26");
                        return null;
                    }
                    b.m1906a(this.f44a).f61a = null;
                    if (ikVar.f672a == 0) {
                        b.m1906a(this.f44a).b(ikVar.f684e, ikVar.f685f, ikVar.f691l);
                        FCMPushHelper.persistIfXmsfSupDecrypt(this.f44a);
                        a3 = eo.a(this.f44a);
                        packageName2 = this.f44a.getPackageName();
                        m2126a2 = en.m2126a(i);
                        i2 = 6006;
                        str5 = "1";
                    } else {
                        a3 = eo.a(this.f44a);
                        packageName2 = this.f44a.getPackageName();
                        m2126a2 = en.m2126a(i);
                        i2 = 6006;
                        str5 = "2";
                    }
                    a3.a(packageName2, m2126a2, str, i2, str5);
                    if (!TextUtils.isEmpty(ikVar.f684e)) {
                        arrayList = new ArrayList();
                        arrayList.add(ikVar.f684e);
                    }
                    MiPushCommandMessage generateCommandMessage = PushMessageHelper.generateCommandMessage(ey.COMMAND_REGISTER.f318a, arrayList, ikVar.f672a, ikVar.f683d, null, ikVar.m2323a());
                    ao.a(this.f44a).m1903d();
                    return generateCommandMessage;
                case 3:
                    if (!cif.m2301b()) {
                        com.xiaomi.channel.commonutils.logger.b.d("receiving an un-encrypt message(UnRegistration).");
                        return null;
                    }
                    if (((iq) a4).f750a == 0) {
                        b.m1906a(this.f44a).m1908a();
                        MiPushClient.clearExtras(this.f44a);
                    }
                    PushMessageHandler.a();
                    break;
                case 4:
                    io ioVar = (io) a4;
                    if (ioVar.f725a == 0) {
                        MiPushClient.addTopic(this.f44a, ioVar.b());
                    }
                    if (!TextUtils.isEmpty(ioVar.b())) {
                        arrayList = new ArrayList();
                        arrayList.add(ioVar.b());
                    }
                    StringBuilder sb = new StringBuilder();
                    sb.append("resp-cmd:");
                    ey eyVar = ey.COMMAND_SUBSCRIBE_TOPIC;
                    sb.append(eyVar);
                    sb.append(", ");
                    sb.append(ioVar.a());
                    com.xiaomi.channel.commonutils.logger.b.e(sb.toString());
                    return PushMessageHelper.generateCommandMessage(eyVar.f318a, arrayList, ioVar.f725a, ioVar.f731d, ioVar.c(), null);
                case 5:
                    is isVar = (is) a4;
                    if (isVar.f770a == 0) {
                        MiPushClient.removeTopic(this.f44a, isVar.b());
                    }
                    if (!TextUtils.isEmpty(isVar.b())) {
                        arrayList = new ArrayList();
                        arrayList.add(isVar.b());
                    }
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("resp-cmd:");
                    ey eyVar2 = ey.COMMAND_UNSUBSCRIBE_TOPIC;
                    sb2.append(eyVar2);
                    sb2.append(", ");
                    sb2.append(isVar.a());
                    com.xiaomi.channel.commonutils.logger.b.e(sb2.toString());
                    return PushMessageHelper.generateCommandMessage(eyVar2.f318a, arrayList, isVar.f770a, isVar.f776d, isVar.c(), null);
                case 6:
                    db.a(this.f44a.getPackageName(), this.f44a, a4, hj.Command, bArr.length);
                    ie ieVar = (ie) a4;
                    String b = ieVar.b();
                    List<String> m2287a = ieVar.m2287a();
                    if (ieVar.f597a == 0) {
                        if (TextUtils.equals(b, ey.COMMAND_SET_ACCEPT_TIME.f318a) && m2287a != null && m2287a.size() > 1) {
                            MiPushClient.addAcceptTime(this.f44a, m2287a.get(0), m2287a.get(1));
                            if (!"00:00".equals(m2287a.get(0)) || !"00:00".equals(m2287a.get(1))) {
                                b.m1906a(this.f44a).a(false);
                            } else {
                                b.m1906a(this.f44a).a(true);
                            }
                            m2287a = a(TimeZone.getTimeZone("GMT+08"), TimeZone.getDefault(), m2287a);
                        } else if (TextUtils.equals(b, ey.COMMAND_SET_ALIAS.f318a) && m2287a != null && m2287a.size() > 0) {
                            MiPushClient.addAlias(this.f44a, m2287a.get(0));
                        } else if (TextUtils.equals(b, ey.COMMAND_UNSET_ALIAS.f318a) && m2287a != null && m2287a.size() > 0) {
                            MiPushClient.removeAlias(this.f44a, m2287a.get(0));
                        } else if (TextUtils.equals(b, ey.COMMAND_SET_ACCOUNT.f318a) && m2287a != null && m2287a.size() > 0) {
                            MiPushClient.addAccount(this.f44a, m2287a.get(0));
                        } else if (TextUtils.equals(b, ey.COMMAND_UNSET_ACCOUNT.f318a) && m2287a != null && m2287a.size() > 0) {
                            MiPushClient.removeAccount(this.f44a, m2287a.get(0));
                        } else if (TextUtils.equals(b, ey.COMMAND_CHK_VDEVID.f318a)) {
                            return null;
                        }
                    }
                    List<String> list = m2287a;
                    com.xiaomi.channel.commonutils.logger.b.e("resp-cmd:" + b + ", " + ieVar.a());
                    return PushMessageHelper.generateCommandMessage(b, list, ieVar.f597a, ieVar.f605d, ieVar.c(), null);
                case 7:
                    db.a(this.f44a.getPackageName(), this.f44a, a4, hj.Notification, bArr.length);
                    if (a4 instanceof ia) {
                        ia iaVar = (ia) a4;
                        String a11 = iaVar.a();
                        com.xiaomi.channel.commonutils.logger.b.e("resp-type:" + iaVar.b() + ", code:" + iaVar.f567a + ", " + a11);
                        if (ht.DisablePushMessage.f489a.equalsIgnoreCase(iaVar.f574d)) {
                            if (iaVar.f567a == 0) {
                                synchronized (af.class) {
                                    if (af.a(this.f44a).m1887a(a11)) {
                                        af.a(this.f44a).c(a11);
                                        af a12 = af.a(this.f44a);
                                        au auVar = au.DISABLE_PUSH;
                                        if ("syncing".equals(a12.a(auVar))) {
                                            af.a(this.f44a).a(auVar, "synced");
                                            MiPushClient.clearNotification(this.f44a);
                                            MiPushClient.clearLocalNotificationType(this.f44a);
                                            PushMessageHandler.a();
                                            ao.a(this.f44a).m1899b();
                                        }
                                    }
                                }
                                break;
                            } else if ("syncing".equals(af.a(this.f44a).a(au.DISABLE_PUSH))) {
                                synchronized (af.class) {
                                    if (af.a(this.f44a).m1887a(a11)) {
                                        if (af.a(this.f44a).a(a11) < 10) {
                                            af.a(this.f44a).b(a11);
                                            ao.a(this.f44a).a(true, a11);
                                        } else {
                                            af.a(this.f44a).c(a11);
                                        }
                                    }
                                }
                                break;
                            }
                        } else if (ht.EnablePushMessage.f489a.equalsIgnoreCase(iaVar.f574d)) {
                            if (iaVar.f567a == 0) {
                                synchronized (af.class) {
                                    if (af.a(this.f44a).m1887a(a11)) {
                                        af.a(this.f44a).c(a11);
                                        af a13 = af.a(this.f44a);
                                        au auVar2 = au.ENABLE_PUSH;
                                        if ("syncing".equals(a13.a(auVar2))) {
                                            af.a(this.f44a).a(auVar2, "synced");
                                        }
                                    }
                                }
                                break;
                            } else if ("syncing".equals(af.a(this.f44a).a(au.ENABLE_PUSH))) {
                                synchronized (af.class) {
                                    if (af.a(this.f44a).m1887a(a11)) {
                                        if (af.a(this.f44a).a(a11) < 10) {
                                            af.a(this.f44a).b(a11);
                                            ao.a(this.f44a).a(false, a11);
                                        } else {
                                            af.a(this.f44a).c(a11);
                                        }
                                    }
                                }
                                break;
                            }
                        } else if (ht.ThirdPartyRegUpdate.f489a.equalsIgnoreCase(iaVar.f574d)) {
                            b(iaVar);
                            break;
                        } else if (ht.UploadTinyData.f489a.equalsIgnoreCase(iaVar.f574d)) {
                            a(iaVar);
                            break;
                        }
                        af.a(this.f44a).c(a11);
                        break;
                    } else if (a4 instanceof ii) {
                        ii iiVar = (ii) a4;
                        if ("registration id expired".equalsIgnoreCase(iiVar.f633d)) {
                            List<String> allAlias = MiPushClient.getAllAlias(this.f44a);
                            List<String> allTopic = MiPushClient.getAllTopic(this.f44a);
                            List<String> allUserAccount = MiPushClient.getAllUserAccount(this.f44a);
                            String acceptTime = MiPushClient.getAcceptTime(this.f44a);
                            com.xiaomi.channel.commonutils.logger.b.e("resp-type:" + iiVar.f633d + ", " + iiVar.m2308a());
                            MiPushClient.reInitialize(this.f44a, hx.RegIdExpired);
                            for (String str7 : allAlias) {
                                MiPushClient.removeAlias(this.f44a, str7);
                                MiPushClient.setAlias(this.f44a, str7, null);
                            }
                            for (String str8 : allTopic) {
                                MiPushClient.removeTopic(this.f44a, str8);
                                MiPushClient.subscribe(this.f44a, str8, null);
                            }
                            for (String str9 : allUserAccount) {
                                MiPushClient.removeAccount(this.f44a, str9);
                                MiPushClient.setUserAccount(this.f44a, str9, null);
                            }
                            String[] split = acceptTime.split(",");
                            if (split.length == 2) {
                                MiPushClient.removeAcceptTime(this.f44a);
                                MiPushClient.addAcceptTime(this.f44a, split[0], split[1]);
                                break;
                            }
                        } else if (ht.ClientInfoUpdateOk.f489a.equalsIgnoreCase(iiVar.f633d)) {
                            if (iiVar.m2309a() != null && iiVar.m2309a().containsKey("app_version")) {
                                b.m1906a(this.f44a).a(iiVar.m2309a().get("app_version"));
                                break;
                            }
                        } else if (ht.AwakeApp.f489a.equalsIgnoreCase(iiVar.f633d)) {
                            if (cif.m2301b() && iiVar.m2309a() != null && iiVar.m2309a().containsKey("awake_info")) {
                                Context context2 = this.f44a;
                                o.a(context2, b.m1906a(context2).m1907a(), ba.a(this.f44a).a(ho.AwakeInfoUploadWaySwitch.a(), 0), iiVar.m2309a().get("awake_info"));
                                break;
                            }
                        } else {
                            try {
                                if (ht.NormalClientConfigUpdate.f489a.equalsIgnoreCase(iiVar.f633d)) {
                                    ih ihVar = new ih();
                                    it.a(ihVar, iiVar.m2314a());
                                    bb.a(ba.a(this.f44a), ihVar);
                                } else if (ht.CustomClientConfigUpdate.f489a.equalsIgnoreCase(iiVar.f633d)) {
                                    ig igVar = new ig();
                                    it.a(igVar, iiVar.m2314a());
                                    bb.a(ba.a(this.f44a), igVar);
                                } else if (ht.SyncInfoResult.f489a.equalsIgnoreCase(iiVar.f633d)) {
                                    av.a(this.f44a, iiVar);
                                    break;
                                } else if (ht.ForceSync.f489a.equalsIgnoreCase(iiVar.f633d)) {
                                    com.xiaomi.channel.commonutils.logger.b.m1859a("receive force sync notification");
                                    av.a(this.f44a, false);
                                    break;
                                } else if (ht.CancelPushMessage.f489a.equals(iiVar.f633d)) {
                                    com.xiaomi.channel.commonutils.logger.b.e("resp-type:" + iiVar.f633d + ", " + iiVar.m2308a());
                                    if (iiVar.m2309a() != null) {
                                        int i3 = -2;
                                        if (iiVar.m2309a().containsKey(bk.M)) {
                                            String str10 = iiVar.m2309a().get(bk.M);
                                            if (!TextUtils.isEmpty(str10)) {
                                                try {
                                                    i3 = Integer.parseInt(str10);
                                                } catch (NumberFormatException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                        if (i3 >= -1) {
                                            MiPushClient.clearNotification(this.f44a, i3);
                                        } else {
                                            String str11 = "";
                                            String str12 = "";
                                            if (iiVar.m2309a().containsKey(bk.K)) {
                                                str11 = iiVar.m2309a().get(bk.K);
                                            }
                                            if (iiVar.m2309a().containsKey(bk.L)) {
                                                str12 = iiVar.m2309a().get(bk.L);
                                            }
                                            MiPushClient.clearNotification(this.f44a, str11, str12);
                                        }
                                    }
                                    a(iiVar);
                                    break;
                                } else {
                                    try {
                                        if (ht.HybridRegisterResult.f489a.equals(iiVar.f633d)) {
                                            ik ikVar2 = new ik();
                                            it.a(ikVar2, iiVar.m2314a());
                                            MiPushClient4Hybrid.onReceiveRegisterResult(this.f44a, ikVar2);
                                        } else if (ht.HybridUnregisterResult.f489a.equals(iiVar.f633d)) {
                                            iq iqVar = new iq();
                                            it.a(iqVar, iiVar.m2314a());
                                            MiPushClient4Hybrid.onReceiveUnregisterResult(this.f44a, iqVar);
                                        } else if (!ht.PushLogUpload.f489a.equals(iiVar.f633d)) {
                                            if (ht.DetectAppAlive.f489a.equals(iiVar.f633d)) {
                                                com.xiaomi.channel.commonutils.logger.b.b("receive detect msg");
                                                b(iiVar);
                                                break;
                                            } else if (com.xiaomi.push.service.i.a(iiVar)) {
                                                com.xiaomi.channel.commonutils.logger.b.b("receive notification handle by cpra");
                                                break;
                                            }
                                        }
                                        break;
                                    } catch (iz e2) {
                                        com.xiaomi.channel.commonutils.logger.b.a(e2);
                                        break;
                                    }
                                }
                                break;
                            } catch (iz unused) {
                                break;
                            }
                        }
                    }
                    break;
            }
            return null;
        } catch (u e3) {
            com.xiaomi.channel.commonutils.logger.b.a(e3);
            a(cif);
            a2 = eo.a(this.f44a);
            packageName = this.f44a.getPackageName();
            m2126a = en.m2126a(i);
            str2 = "19";
            a2.b(packageName, m2126a, str, str2);
            s.c(this.f44a, cif, z);
            return null;
        } catch (iz e4) {
            com.xiaomi.channel.commonutils.logger.b.a(e4);
            com.xiaomi.channel.commonutils.logger.b.d("receive a message which action string is not valid. is the reg expired?");
            a2 = eo.a(this.f44a);
            packageName = this.f44a.getPackageName();
            m2126a = en.m2126a(i);
            str2 = "20";
            a2.b(packageName, m2126a, str, str2);
            s.c(this.f44a, cif, z);
            return null;
        }
    }

    public final PushMessageHandler.a a(Cif cif, byte[] bArr) {
        String str;
        iu a2;
        String str2 = null;
        try {
            a2 = ai.a(this.f44a, cif);
        } catch (u e) {
            com.xiaomi.channel.commonutils.logger.b.a(e);
            str = "message arrived: receive a message but decrypt failed. report when click.";
        } catch (iz e2) {
            com.xiaomi.channel.commonutils.logger.b.a(e2);
            str = "message arrived: receive a message which action string is not valid. is the reg expired?";
        }
        if (a2 == null) {
            com.xiaomi.channel.commonutils.logger.b.d("message arrived: receiving an un-recognized message. " + cif.f609a);
            return null;
        }
        hj a3 = cif.a();
        com.xiaomi.channel.commonutils.logger.b.m1859a("message arrived: processing an arrived message, action=" + a3);
        if (an.a[a3.ordinal()] != 1) {
            return null;
        }
        if (!cif.m2301b()) {
            str = "message arrived: receiving an un-encrypt message(SendMessage).";
        } else {
            im imVar = (im) a2;
            hv a4 = imVar.a();
            if (a4 != null) {
                hw hwVar = cif.f610a;
                if (hwVar != null && hwVar.m2260a() != null) {
                    str2 = cif.f610a.f524a.get("jobkey");
                }
                MiPushMessage generateMessage = PushMessageHelper.generateMessage(imVar, cif.m2293a(), false);
                generateMessage.setArrivedMessage(true);
                com.xiaomi.channel.commonutils.logger.b.m1859a("message arrived: receive a message, msgid=" + a4.m2251a() + ", jobkey=" + str2);
                return generateMessage;
            }
            str = "message arrived: receive an empty message without push content, drop it";
        }
        com.xiaomi.channel.commonutils.logger.b.d(str);
        return null;
    }

    public List<String> a(TimeZone timeZone, TimeZone timeZone2, List<String> list) {
        if (timeZone.equals(timeZone2)) {
            return list;
        }
        long rawOffset = ((timeZone.getRawOffset() - timeZone2.getRawOffset()) / 1000) / 60;
        long parseLong = ((((Long.parseLong(list.get(0).split(":")[0]) * 60) + Long.parseLong(list.get(0).split(":")[1])) - rawOffset) + 1440) % 1440;
        long parseLong2 = ((((Long.parseLong(list.get(1).split(":")[0]) * 60) + Long.parseLong(list.get(1).split(":")[1])) - rawOffset) + 1440) % 1440;
        ArrayList arrayList = new ArrayList();
        arrayList.add(String.format("%1$02d:%2$02d", Long.valueOf(parseLong / 60), Long.valueOf(parseLong % 60)));
        arrayList.add(String.format("%1$02d:%2$02d", Long.valueOf(parseLong2 / 60), Long.valueOf(parseLong2 % 60)));
        return arrayList;
    }

    public final void a() {
        SharedPreferences sharedPreferences = this.f44a.getSharedPreferences("mipush_extra", 0);
        long currentTimeMillis = System.currentTimeMillis();
        if (Math.abs(currentTimeMillis - sharedPreferences.getLong("last_reinitialize", 0L)) > ComponentTracker.DEFAULT_TIMEOUT) {
            MiPushClient.reInitialize(this.f44a, hx.PackageUnregistered);
            sharedPreferences.edit().putLong("last_reinitialize", currentTimeMillis).commit();
        }
    }

    public final void a(ia iaVar) {
        String a2 = iaVar.a();
        com.xiaomi.channel.commonutils.logger.b.b("receive ack " + a2);
        Map<String, String> m2274a = iaVar.m2274a();
        if (m2274a != null) {
            String str = m2274a.get("real_source");
            if (TextUtils.isEmpty(str)) {
                return;
            }
            com.xiaomi.channel.commonutils.logger.b.b("receive ack : messageId = " + a2 + "  realSource = " + str);
            bx.a(this.f44a).a(a2, str, Boolean.valueOf(iaVar.f567a == 0));
        }
    }

    public final void a(Cif cif) {
        com.xiaomi.channel.commonutils.logger.b.m1859a("receive a message but decrypt failed. report now.");
        ii iiVar = new ii(cif.m2293a().f522a, false);
        iiVar.c(ht.DecryptMessageFail.f489a);
        iiVar.b(cif.m2294a());
        iiVar.d(cif.f616b);
        HashMap hashMap = new HashMap();
        iiVar.f628a = hashMap;
        hashMap.put("regid", MiPushClient.getRegId(this.f44a));
        ao.a(this.f44a).a((ao) iiVar, hj.Notification, false, (hw) null);
    }

    public final void a(ii iiVar) {
        ia iaVar = new ia();
        iaVar.c(ht.CancelPushMessageACK.f489a);
        iaVar.a(iiVar.m2308a());
        iaVar.a(iiVar.a());
        iaVar.b(iiVar.b());
        iaVar.e(iiVar.c());
        iaVar.a(0L);
        iaVar.d("success clear push message.");
        ao.a(this.f44a).a(iaVar, hj.Notification, false, true, null, false, this.f44a.getPackageName(), b.m1906a(this.f44a).m1907a(), false);
    }

    public final void a(im imVar, Cif cif) {
        hw m2293a = cif.m2293a();
        if (m2293a != null) {
            m2293a = br.a(m2293a.m2258a());
        }
        hz hzVar = new hz();
        hzVar.b(imVar.b());
        hzVar.a(imVar.m2331a());
        hzVar.a(imVar.a().a());
        if (!TextUtils.isEmpty(imVar.c())) {
            hzVar.c(imVar.c());
        }
        if (!TextUtils.isEmpty(imVar.d())) {
            hzVar.d(imVar.d());
        }
        hzVar.a(it.a(this.f44a, cif));
        ao.a(this.f44a).a((ao) hzVar, hj.AckMessage, m2293a);
    }

    public final void a(String str, long j, e eVar) {
        au a2 = l.a(eVar);
        if (a2 == null) {
            return;
        }
        if (j == 0) {
            synchronized (af.class) {
                if (af.a(this.f44a).m1887a(str)) {
                    af.a(this.f44a).c(str);
                    if ("syncing".equals(af.a(this.f44a).a(a2))) {
                        af.a(this.f44a).a(a2, "synced");
                    }
                }
            }
        } else if (!"syncing".equals(af.a(this.f44a).a(a2))) {
            af.a(this.f44a).c(str);
        } else {
            synchronized (af.class) {
                if (af.a(this.f44a).m1887a(str)) {
                    if (af.a(this.f44a).a(str) < 10) {
                        af.a(this.f44a).b(str);
                        ao.a(this.f44a).a(str, a2, eVar);
                    } else {
                        af.a(this.f44a).c(str);
                    }
                }
            }
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public final boolean m1888a(Cif cif) {
        Map<String, String> m2260a = cif.m2293a() == null ? null : cif.m2293a().m2260a();
        if (m2260a == null) {
            return false;
        }
        String str = m2260a.get("push_server_action");
        return TextUtils.equals(str, "hybrid_message") || TextUtils.equals(str, "platform_message");
    }

    public final void b(ia iaVar) {
        Context context;
        e eVar;
        com.xiaomi.channel.commonutils.logger.b.c("ASSEMBLE_PUSH : " + iaVar.toString());
        String a2 = iaVar.a();
        Map<String, String> m2274a = iaVar.m2274a();
        if (m2274a != null) {
            String str = m2274a.get("RegInfo");
            if (TextUtils.isEmpty(str)) {
                return;
            }
            if (str.contains("brand:" + ag.FCM.name())) {
                com.xiaomi.channel.commonutils.logger.b.m1859a("ASSEMBLE_PUSH : receive fcm token sync ack");
                context = this.f44a;
                eVar = e.ASSEMBLE_PUSH_FCM;
            } else {
                if (str.contains("brand:" + ag.HUAWEI.name())) {
                    com.xiaomi.channel.commonutils.logger.b.m1859a("ASSEMBLE_PUSH : receive hw token sync ack");
                    context = this.f44a;
                    eVar = e.ASSEMBLE_PUSH_HUAWEI;
                } else {
                    if (str.contains("brand:" + ag.OPPO.name())) {
                        com.xiaomi.channel.commonutils.logger.b.m1859a("ASSEMBLE_PUSH : receive COS token sync ack");
                        context = this.f44a;
                        eVar = e.ASSEMBLE_PUSH_COS;
                    } else {
                        if (!str.contains("brand:" + ag.VIVO.name())) {
                            return;
                        }
                        com.xiaomi.channel.commonutils.logger.b.m1859a("ASSEMBLE_PUSH : receive FTOS token sync ack");
                        context = this.f44a;
                        eVar = e.ASSEMBLE_PUSH_FTOS;
                    }
                }
            }
            i.b(context, eVar, str);
            a(a2, iaVar.f567a, eVar);
        }
    }

    public final void b(Cif cif) {
        hw m2293a = cif.m2293a();
        if (m2293a != null) {
            m2293a = br.a(m2293a.m2258a());
        }
        hz hzVar = new hz();
        hzVar.b(cif.m2294a());
        hzVar.a(m2293a.m2259a());
        hzVar.a(m2293a.m2257a());
        if (!TextUtils.isEmpty(m2293a.m2264b())) {
            hzVar.c(m2293a.m2264b());
        }
        hzVar.a(it.a(this.f44a, cif));
        ao.a(this.f44a).a((ao) hzVar, hj.AckMessage, false, m2293a);
    }

    public final void b(ii iiVar) {
        String str;
        Map<String, String> m2309a = iiVar.m2309a();
        if (m2309a == null) {
            str = "detect failed because null";
        } else {
            String str2 = (String) ay.a(m2309a, "pkgList", (Object) null);
            if (!TextUtils.isEmpty(str2)) {
                try {
                    List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = ((ActivityManager) this.f44a.getSystemService("activity")).getRunningAppProcesses();
                    if (com.xiaomi.push.w.a(runningAppProcesses)) {
                        com.xiaomi.channel.commonutils.logger.b.m1859a("detect failed because params illegal");
                        return;
                    }
                    String[] split = str2.split(",");
                    HashMap hashMap = new HashMap();
                    for (String str3 : split) {
                        String[] split2 = str3.split("~");
                        if (split2.length >= 2) {
                            hashMap.put(split2[1], split2[0]);
                        }
                    }
                    w.a aVar = new w.a("~", ",");
                    for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
                        if (hashMap.containsKey(runningAppProcessInfo.processName)) {
                            aVar.a((String) hashMap.get(runningAppProcessInfo.processName), String.valueOf(runningAppProcessInfo.importance));
                            hashMap.remove(runningAppProcessInfo.processName);
                        }
                    }
                    if (aVar.toString().length() <= 0) {
                        com.xiaomi.channel.commonutils.logger.b.b("detect failed because no alive process");
                        return;
                    }
                    ii iiVar2 = new ii();
                    iiVar2.a(iiVar.m2308a());
                    iiVar2.b(iiVar.b());
                    iiVar2.d(iiVar.c());
                    iiVar2.c(ht.DetectAppAliveResult.f489a);
                    HashMap hashMap2 = new HashMap();
                    iiVar2.f628a = hashMap2;
                    hashMap2.put("alive", aVar.toString());
                    if (Boolean.parseBoolean((String) ay.a(m2309a, "reportNotAliveApp", "false")) && hashMap.size() > 0) {
                        w.a aVar2 = new w.a("", ",");
                        for (String str4 : hashMap.keySet()) {
                            aVar2.a((String) hashMap.get(str4), "");
                        }
                        iiVar2.f628a.put("notAlive", aVar2.toString());
                    }
                    ao.a(this.f44a).a((ao) iiVar2, hj.Notification, false, (hw) null);
                    return;
                } catch (Throwable th) {
                    com.xiaomi.channel.commonutils.logger.b.m1859a("detect failed " + th);
                    return;
                }
            }
            str = "detect failed because empty";
        }
        com.xiaomi.channel.commonutils.logger.b.m1859a(str);
    }
}
