package com.miui.gallery.view;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.provider.Settings;
import android.view.WindowManager;
import com.miui.gallery.util.BrightnessProvider;
import com.miui.gallery.util.BrightnessUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public class BrightnessManager implements BrightnessProvider {
    public WeakReference<Activity> mActivityRef;
    public BrightnessAsynTask mBrightnessAsynTask;
    public float mCurrentBrightnessAuto;
    public float mCurrentBrightnessManual;
    public static final Uri BRIGHTNESS_URI = Settings.System.getUriFor("screen_brightness");
    public static final Uri BRIGHTNESS_ADJ_URI = Settings.System.getUriFor("screen_auto_brightness_adj");
    public int mBrightMode = -1;
    public boolean mSystemBrightnessChanged = false;
    public boolean mFocus = true;
    public ContentObserver mBrightnessObserver = new ContentObserver(new Handler()) { // from class: com.miui.gallery.view.BrightnessManager.1
        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            super.onChange(z);
            if (BrightnessManager.this.getActivity() == null || z) {
                return;
            }
            BrightnessManager.this.mSystemBrightnessChanged = true;
            BrightnessManager.this.adjustBrightness();
        }
    };

    /* loaded from: classes2.dex */
    public class BrightnessAsynTask extends AsyncTask<Void, Void, Float> {
        public BrightnessAsynTask() {
        }

        @Override // android.os.AsyncTask
        public Float doInBackground(Void... voidArr) {
            try {
                Activity activity = (Activity) BrightnessManager.this.mActivityRef.get();
                if (activity != null) {
                    BrightnessManager.this.mBrightMode = Settings.System.getInt(activity.getContentResolver(), "screen_brightness_mode");
                    if (BrightnessManager.this.mBrightMode == 1) {
                        if (BrightnessManager.this.mCurrentBrightnessManual != -1.0f && BrightnessManager.this.mCurrentBrightnessAuto == 0.0f) {
                            BrightnessManager.this.mBrightMode = 0;
                            return Float.valueOf(BrightnessManager.this.mCurrentBrightnessManual);
                        }
                        return Float.valueOf(BrightnessManager.this.mCurrentBrightnessAuto);
                    } else if (BrightnessManager.this.mBrightMode == 0) {
                        return Float.valueOf(BrightnessManager.this.mCurrentBrightnessManual);
                    }
                }
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            return Float.valueOf(-1.0f);
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Float f) {
            super.onPostExecute((BrightnessAsynTask) f);
            if (!isCancelled()) {
                BrightnessManager brightnessManager = BrightnessManager.this;
                brightnessManager.adjustBrightnessTask(brightnessManager.mSystemBrightnessChanged, f.floatValue(), BrightnessManager.this.mBrightMode);
            }
        }
    }

    public BrightnessManager(Activity activity, float f, float f2) {
        this.mCurrentBrightnessAuto = 0.0f;
        float f3 = -1.0f;
        this.mCurrentBrightnessManual = -1.0f;
        this.mCurrentBrightnessAuto = f2 >= 0.0f ? ensureBrightness(f2) : 0.0f;
        this.mCurrentBrightnessManual = f > 0.0f ? ensureBrightness(f) : f3;
        this.mActivityRef = new WeakReference<>(activity);
    }

    public void adjustBrightnessTask(boolean z, float f, int i) {
        Activity activity = getActivity();
        if (activity != null) {
            WindowManager.LayoutParams attributes = activity.getWindow().getAttributes();
            if (i == 1) {
                if (this.mFocus) {
                    setTemporaryScreenAutoBrightnessAdjustmentRatio(f);
                } else {
                    setTemporaryScreenAutoBrightnessAdjustmentRatio(0.0f);
                }
            } else if (i != 0) {
            } else {
                float f2 = -1.0f;
                if (this.mFocus) {
                    if (!z) {
                        f2 = this.mCurrentBrightnessManual;
                    }
                    attributes.screenBrightness = f2;
                } else {
                    attributes.screenBrightness = -1.0f;
                }
                activity.getWindow().setAttributes(attributes);
            }
        }
    }

    public final void adjustBrightness() {
        BrightnessAsynTask brightnessAsynTask = this.mBrightnessAsynTask;
        if (brightnessAsynTask != null) {
            brightnessAsynTask.cancel(true);
        }
        BrightnessAsynTask brightnessAsynTask2 = new BrightnessAsynTask();
        this.mBrightnessAsynTask = brightnessAsynTask2;
        brightnessAsynTask2.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, new Void[0]);
    }

    public void setTemporaryScreenAutoBrightnessAdjustmentRatio(float f) {
        BrightnessUtils.setTemporaryAutoBrightness(getActivity(), f);
    }

    @Override // com.miui.gallery.util.BrightnessProvider
    public float getManualBrightness() {
        if (this.mSystemBrightnessChanged) {
            return -1.0f;
        }
        return this.mCurrentBrightnessManual;
    }

    @Override // com.miui.gallery.util.BrightnessProvider
    public float getAutoBrightness() {
        return this.mCurrentBrightnessAuto;
    }

    public final float ensureBrightness(float f) {
        return Math.max(0.0f, Math.min(f, 1.0f));
    }

    public final Activity getActivity() {
        WeakReference<Activity> weakReference = this.mActivityRef;
        if (weakReference != null) {
            return weakReference.get();
        }
        return null;
    }

    public void onWindowFocusChanged(boolean z) {
        this.mFocus = z;
        adjustBrightness();
    }

    public void onResume() {
        registerObserver();
        adjustBrightness();
    }

    public void onPause() {
        adjustBrightness();
        unregisterObserver();
    }

    public final void registerObserver() {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        try {
            ContentResolver contentResolver = activity.getContentResolver();
            contentResolver.registerContentObserver(BRIGHTNESS_URI, true, this.mBrightnessObserver);
            contentResolver.registerContentObserver(BRIGHTNESS_ADJ_URI, true, this.mBrightnessObserver);
        } catch (Exception e) {
            DefaultLogger.e("BrightnessManager", "Register BrightnessObserver error: %s" + e);
        }
    }

    public final void unregisterObserver() {
        Activity activity = getActivity();
        if (activity == null) {
            return;
        }
        try {
            activity.getContentResolver().unregisterContentObserver(this.mBrightnessObserver);
        } catch (Exception e) {
            DefaultLogger.e("BrightnessManager", "Unregister BrightnessObserver error: %s" + e);
        }
    }
}
