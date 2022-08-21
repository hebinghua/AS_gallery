package com.baidu.vi;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;

/* loaded from: classes.dex */
class d implements SensorEventListener {
    public final /* synthetic */ VCompass a;

    public d(VCompass vCompass) {
        this.a = vCompass;
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override // android.hardware.SensorEventListener
    public void onSensorChanged(SensorEvent sensorEvent) {
        float a;
        if (sensorEvent.sensor.getType() != 3) {
            return;
        }
        a = this.a.a(sensorEvent.values[0]);
        this.a.updateCompass((int) a);
    }
}
