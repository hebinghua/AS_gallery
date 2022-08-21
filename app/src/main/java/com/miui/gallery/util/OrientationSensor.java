package com.miui.gallery.util;

import android.app.Activity;
import android.view.OrientationEventListener;
import com.nexstreaming.nexeditorsdk.nexClip;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public class OrientationSensor extends OrientationEventListener {
    public boolean mEnabled;
    public WeakReference<OnOrientationChangedListener> mListener;
    public int mOrientation;

    /* loaded from: classes2.dex */
    public interface OnOrientationChangedListener {
        void onOrientationChanged(int i, int i2);
    }

    public static int restoreFromSurfaceRotation(int i) {
        if (i != 0) {
            return i != 1 ? i != 2 ? i != 3 ? -1 : 90 : nexClip.kClip_Rotate_180 : nexClip.kClip_Rotate_270;
        }
        return 0;
    }

    public static int transfer2SurfaceRotation(int i) {
        if (i != 0) {
            if (i == 90) {
                return 3;
            }
            if (i == 180) {
                return 2;
            }
            return i != 270 ? -1 : 1;
        }
        return 0;
    }

    public OrientationSensor(Activity activity) {
        super(activity.getApplicationContext());
        this.mOrientation = -1;
        this.mEnabled = false;
        this.mOrientation = restoreFromSurfaceRotation(activity.getWindowManager().getDefaultDisplay().getRotation());
    }

    public void setOrientationChangedListener(OnOrientationChangedListener onOrientationChangedListener) {
        WeakReference<OnOrientationChangedListener> weakReference;
        if (onOrientationChangedListener == null && (weakReference = this.mListener) != null && weakReference.get() != null) {
            this.mListener.clear();
            this.mListener = null;
            return;
        }
        this.mListener = new WeakReference<>(onOrientationChangedListener);
    }

    @Override // android.view.OrientationEventListener
    public void enable() {
        super.enable();
        this.mEnabled = true;
    }

    @Override // android.view.OrientationEventListener
    public void disable() {
        super.disable();
        this.mEnabled = false;
    }

    public boolean isEnabled() {
        return this.mEnabled;
    }

    @Override // android.view.OrientationEventListener
    public void onOrientationChanged(int i) {
        int roundOrientation = roundOrientation(i, this.mOrientation);
        int i2 = this.mOrientation;
        if (roundOrientation != i2) {
            if (roundOrientation % nexClip.kClip_Rotate_180 == 0 && i2 % nexClip.kClip_Rotate_180 == 0) {
                return;
            }
            WeakReference<OnOrientationChangedListener> weakReference = this.mListener;
            if (weakReference != null && weakReference.get() != null) {
                this.mListener.get().onOrientationChanged(this.mOrientation, roundOrientation);
            }
            this.mOrientation = roundOrientation;
        }
    }

    public final int roundOrientation(int i, int i2) {
        boolean z = true;
        if (i2 != -1) {
            int abs = Math.abs(i - i2);
            if (Math.min(abs, 360 - abs) < 50) {
                z = false;
            }
        }
        return z ? (((i + 45) / 90) * 90) % 360 : i2;
    }
}
