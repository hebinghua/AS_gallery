package com.meicam.sdk;

import java.util.concurrent.atomic.AtomicReference;

/* loaded from: classes.dex */
public class NvsUnityPluginHelper {
    private static AtomicReference<EventCallback> m_eventCallback = new AtomicReference<>(null);

    /* loaded from: classes.dex */
    public interface EventCallback {
        void onPluginEvent(int i);
    }

    public static void setEventCallback(EventCallback eventCallback) {
        m_eventCallback.set(eventCallback);
    }

    public static void onPluginEvent(int i) {
        EventCallback eventCallback = m_eventCallback.get();
        if (eventCallback != null) {
            try {
                eventCallback.onPluginEvent(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
