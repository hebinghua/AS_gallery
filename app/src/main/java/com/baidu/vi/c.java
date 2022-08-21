package com.baidu.vi;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Message;
import java.util.List;

/* loaded from: classes.dex */
final class c extends Handler {
    @Override // android.os.Handler
    public void handleMessage(Message message) {
        SensorManager sensorManager;
        SensorManager sensorManager2;
        SensorManager sensorManager3;
        SensorEventListener sensorEventListener;
        SensorManager sensorManager4;
        SensorEventListener sensorEventListener2;
        VCompass vCompass = (VCompass) message.obj;
        if (vCompass == null) {
            return;
        }
        int i = message.what;
        if (i != 1) {
            if (i != 2) {
                return;
            }
            sensorManager4 = vCompass.a;
            sensorEventListener2 = vCompass.f;
            sensorManager4.unregisterListener(sensorEventListener2);
            return;
        }
        Context context = VIContext.getContext();
        sensorManager = vCompass.a;
        if (sensorManager == null) {
            vCompass.a = (SensorManager) context.getSystemService("sensor");
        }
        sensorManager2 = vCompass.a;
        List<Sensor> sensorList = sensorManager2.getSensorList(3);
        if (sensorList.size() <= 0) {
            return;
        }
        sensorManager3 = vCompass.a;
        sensorEventListener = vCompass.f;
        sensorManager3.registerListener(sensorEventListener, sensorList.get(0), 1);
    }
}
