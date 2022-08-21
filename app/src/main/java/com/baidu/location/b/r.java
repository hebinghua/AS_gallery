package com.baidu.location.b;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/* loaded from: classes.dex */
public class r implements SensorEventListener {
    private static r d;
    private float[] a;
    private float[] b;
    private SensorManager c;
    private float e;
    private boolean f = false;
    private boolean g = false;
    private boolean h = false;

    private r() {
    }

    public static synchronized r a() {
        r rVar;
        synchronized (r.class) {
            if (d == null) {
                d = new r();
            }
            rVar = d;
        }
        return rVar;
    }

    public void a(boolean z) {
        this.f = z;
    }

    public synchronized void b() {
        if (this.h) {
            return;
        }
        if (!this.f) {
            return;
        }
        if (this.c == null) {
            this.c = (SensorManager) com.baidu.location.f.getServiceContext().getSystemService("sensor");
        }
        SensorManager sensorManager = this.c;
        if (sensorManager != null) {
            Sensor defaultSensor = sensorManager.getDefaultSensor(11);
            if (defaultSensor != null && this.f) {
                this.c.registerListener(this, defaultSensor, 3);
            }
            Sensor defaultSensor2 = this.c.getDefaultSensor(2);
            if (defaultSensor2 != null && this.f) {
                this.c.registerListener(this, defaultSensor2, 3);
            }
        }
        this.h = true;
    }

    public synchronized void c() {
        if (!this.h) {
            return;
        }
        SensorManager sensorManager = this.c;
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
            this.c = null;
        }
        this.h = false;
    }

    public boolean d() {
        return this.f;
    }

    public float e() {
        return this.e;
    }

    @Override // android.hardware.SensorEventListener
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override // android.hardware.SensorEventListener
    @SuppressLint({"NewApi"})
    public void onSensorChanged(SensorEvent sensorEvent) {
        int type = sensorEvent.sensor.getType();
        if (type == 2) {
            float[] fArr = (float[]) sensorEvent.values.clone();
            this.b = fArr;
            Math.sqrt((fArr[0] * fArr[0]) + (fArr[1] * fArr[1]) + (fArr[2] * fArr[2]));
        } else if (type != 11) {
        } else {
            float[] fArr2 = (float[]) sensorEvent.values.clone();
            this.a = fArr2;
            float[] fArr3 = new float[9];
            try {
                SensorManager.getRotationMatrixFromVector(fArr3, fArr2);
                float[] fArr4 = new float[3];
                SensorManager.getOrientation(fArr3, fArr4);
                float degrees = (float) Math.toDegrees(fArr4[0]);
                this.e = degrees;
                if (degrees < 0.0f) {
                    degrees += 360.0f;
                }
                this.e = (float) Math.floor(degrees);
            } catch (Exception unused) {
                this.e = 0.0f;
            }
        }
    }
}
