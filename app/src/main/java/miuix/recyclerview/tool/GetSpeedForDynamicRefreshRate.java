package miuix.recyclerview.tool;

import android.app.Activity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import com.xiaomi.mirror.synergy.CallMethod;
import miuix.recyclerview.widget.RecyclerView;

/* loaded from: classes3.dex */
public class GetSpeedForDynamicRefreshRate {
    public static int sControlViewHashCode = 0;
    public static boolean sHasGetProperty = false;
    public static int[] sRefreshRateList;
    public static int[] sRefreshRateSpeedLimits;
    public int mCurrentRefreshRate;
    public final Display mDisplay;
    public final boolean mIsEnable;
    public final Window mWindow;
    public volatile boolean mIsTouch = false;
    public boolean mHasFocus = false;
    public int mCountIndex = 0;
    public long mStartTime = -1;
    public long mTotalDistance = 0;
    public boolean mNeedAbandon = false;
    public int mOldScrollState = 0;

    public GetSpeedForDynamicRefreshRate(RecyclerView recyclerView) {
        Window window = null;
        Display display = recyclerView.getContext() instanceof Activity ? recyclerView.getContext().getDisplay() : null;
        this.mDisplay = display;
        window = recyclerView.getContext() instanceof Activity ? ((Activity) recyclerView.getContext()).getWindow() : window;
        this.mWindow = window;
        boolean z = (!getParam() || display == null || window == null) ? false : true;
        this.mIsEnable = z;
        if (!z) {
            Log.e("DynamicRefreshRate recy", "dynamic is not enable");
        } else {
            this.mCurrentRefreshRate = sRefreshRateList[0];
        }
    }

    public void calculateSpeed(int i, int i2) {
        int calculateRefreshRate;
        if (this.mIsEnable) {
            if ((i == 0 && i2 == 0) || this.mIsTouch || (calculateRefreshRate = calculateRefreshRate(Math.max(Math.abs(i), Math.abs(i2)))) == -1) {
                return;
            }
            setRefreshRate(calculateRefreshRate, false);
        }
    }

    public void touchEvent(MotionEvent motionEvent) {
        if (!this.mIsEnable) {
            return;
        }
        if (motionEvent.getActionMasked() == 0) {
            this.mIsTouch = true;
            int[] iArr = sRefreshRateList;
            this.mCurrentRefreshRate = iArr[0];
            this.mCountIndex = 0;
            setRefreshRate(iArr[0], true);
            this.mHasFocus = true;
            this.mNeedAbandon = false;
        } else if (motionEvent.getActionMasked() != 1) {
        } else {
            this.mIsTouch = false;
        }
    }

    public void scrollState(RecyclerView recyclerView, int i) {
        if (!this.mIsEnable) {
            return;
        }
        if (this.mNeedAbandon || this.mIsTouch || this.mOldScrollState != 2) {
            this.mOldScrollState = i;
            return;
        }
        this.mOldScrollState = i;
        if ((!recyclerView.canScrollVertically(-1) || !recyclerView.canScrollVertically(1)) && (!recyclerView.canScrollHorizontally(-1) || !recyclerView.canScrollVertically(1))) {
            return;
        }
        int[] iArr = sRefreshRateList;
        setRefreshRate(iArr[iArr.length - 1], false);
    }

    public void onFocusChange(boolean z) {
        if (!this.mIsEnable) {
            return;
        }
        this.mHasFocus = z;
        this.mNeedAbandon = true;
        setRefreshRate(sRefreshRateList[0], false);
    }

    public final void setRefreshRate(int i, boolean z) {
        Display.Mode[] supportedModes = this.mDisplay.getSupportedModes();
        WindowManager.LayoutParams attributes = this.mWindow.getAttributes();
        int i2 = attributes.preferredDisplayModeId;
        if (z || i2 == 0 || Math.abs(supportedModes[i2 - 1].getRefreshRate() - i) >= 1.0f) {
            for (Display.Mode mode : supportedModes) {
                if (Math.abs(mode.getRefreshRate() - i) <= 1.0f) {
                    if (!z && i2 != 0 && hashCode() != sControlViewHashCode && mode.getRefreshRate() <= supportedModes[i2 - 1].getRefreshRate()) {
                        return;
                    }
                    sControlViewHashCode = hashCode();
                    Log.e("DynamicRefreshRate recy", sControlViewHashCode + " set Refresh rate to: " + i + ", mode is: " + mode.getModeId());
                    attributes.preferredDisplayModeId = mode.getModeId();
                    this.mWindow.setAttributes(attributes);
                    return;
                }
            }
        }
    }

    public final int calculateRefreshRate(int i) {
        int[] iArr;
        int i2 = sRefreshRateList[iArr.length - 1];
        if (!this.mHasFocus || this.mNeedAbandon) {
            return -1;
        }
        if (i == 0) {
            return i2;
        }
        if (this.mCountIndex == 0) {
            this.mTotalDistance = 0L;
            this.mStartTime = System.currentTimeMillis();
        }
        int i3 = this.mCountIndex + 1;
        this.mCountIndex = i3;
        this.mTotalDistance += i;
        if (i3 < 3) {
            return -1;
        }
        int abs = Math.abs(Math.round(((float) this.mTotalDistance) / (((float) (System.currentTimeMillis() - this.mStartTime)) / 1000.0f)));
        this.mCountIndex = 0;
        int i4 = 0;
        while (true) {
            int[] iArr2 = sRefreshRateSpeedLimits;
            if (i4 >= iArr2.length) {
                break;
            } else if (abs > iArr2[i4]) {
                i2 = sRefreshRateList[i4];
                break;
            } else {
                i4++;
            }
        }
        int i5 = this.mCurrentRefreshRate;
        if (i2 >= i5) {
            int[] iArr3 = sRefreshRateList;
            if (i5 != iArr3[iArr3.length - 1] || i2 != iArr3[0]) {
                return -1;
            }
        }
        this.mCurrentRefreshRate = i2;
        return i2;
    }

    public static boolean getParam() {
        boolean z = false;
        if (sHasGetProperty) {
            return (sRefreshRateList == null || sRefreshRateSpeedLimits == null) ? false : true;
        }
        try {
            try {
                String str = (String) Class.forName("android.os.SystemProperties").getDeclaredMethod(CallMethod.METHOD_GET, String.class).invoke(null, "ro.vendor.display.dynamic_refresh_rate");
                if (str == null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("dynamic params is ");
                    sb.append((sRefreshRateList == null || sRefreshRateSpeedLimits == null) ? false : true);
                    Log.e("DynamicRefreshRate recy", sb.toString());
                    sHasGetProperty = true;
                    return false;
                }
                String[] split = str.split(":");
                if (split.length != 2) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("dynamic params is ");
                    sb2.append((sRefreshRateList == null || sRefreshRateSpeedLimits == null) ? false : true);
                    Log.e("DynamicRefreshRate recy", sb2.toString());
                    sHasGetProperty = true;
                    return false;
                }
                String[] split2 = split[0].split(",");
                String[] split3 = split[1].split(",");
                if (split3.length != split2.length - 1) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("dynamic params is ");
                    sb3.append((sRefreshRateList == null || sRefreshRateSpeedLimits == null) ? false : true);
                    Log.e("DynamicRefreshRate recy", sb3.toString());
                    sHasGetProperty = true;
                    return false;
                }
                sRefreshRateList = new int[split2.length];
                for (int i = 0; i < split2.length; i++) {
                    sRefreshRateList[i] = Integer.parseInt(split2[i]);
                }
                sRefreshRateSpeedLimits = new int[split3.length];
                for (int i2 = 0; i2 < split3.length; i2++) {
                    sRefreshRateSpeedLimits[i2] = Integer.parseInt(split3[i2]);
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                StringBuilder sb4 = new StringBuilder();
                sb4.append("dynamic params is ");
                sb4.append((sRefreshRateList == null || sRefreshRateSpeedLimits == null) ? false : true);
                Log.e("DynamicRefreshRate recy", sb4.toString());
                sHasGetProperty = true;
                sRefreshRateList = null;
                sRefreshRateSpeedLimits = null;
                return false;
            }
        } finally {
            StringBuilder sb5 = new StringBuilder();
            sb5.append("dynamic params is ");
            if (sRefreshRateList != null && sRefreshRateSpeedLimits != null) {
                z = true;
            }
            sb5.append(z);
            Log.e("DynamicRefreshRate recy", sb5.toString());
            sHasGetProperty = true;
        }
    }
}
