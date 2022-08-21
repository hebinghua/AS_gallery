package com.miui.gallery.push;

import com.miui.gallery.push.messagehandler.CloudControlMessageHandler;
import com.miui.gallery.push.messagehandler.FeatureRedDotMessageHandler;
import com.miui.gallery.push.messagehandler.MessageHandler;
import com.miui.gallery.push.messagehandler.NotificationMessageHandler;
import com.miui.gallery.push.messagehandler.StoryNotificationMessageHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class MessageHandlerFactory {
    public static Map<String, Class<? extends MessageHandler>> sModuleHandlerMap;

    static {
        HashMap hashMap = new HashMap();
        sModuleHandlerMap = hashMap;
        hashMap.put("cloud_control", CloudControlMessageHandler.class);
        sModuleHandlerMap.put("notification", NotificationMessageHandler.class);
        sModuleHandlerMap.put("feature_red_dot", FeatureRedDotMessageHandler.class);
        sModuleHandlerMap.put("story_notification", StoryNotificationMessageHandler.class);
    }

    public static MessageHandler create(String str) {
        Class<? extends MessageHandler> cls = sModuleHandlerMap.get(str);
        if (cls != null) {
            try {
                return cls.newInstance();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e2) {
                e2.printStackTrace();
            }
        }
        return null;
    }

    public static List<MessageHandler> getAllMessageHandlers() {
        ArrayList arrayList = new ArrayList();
        for (Class<? extends MessageHandler> cls : sModuleHandlerMap.values()) {
            try {
                arrayList.add(cls.newInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e2) {
                e2.printStackTrace();
            }
        }
        return arrayList;
    }
}
