package miuix.animation.internal;

import android.util.ArrayMap;
import com.miui.gallery.search.statistics.SearchStatUtils;
import java.lang.ref.WeakReference;
import java.util.Map;
import miuix.animation.IAnimTarget;
import miuix.animation.property.FloatProperty;
import miuix.animation.utils.VelocityMonitor;

/* loaded from: classes3.dex */
public class TargetVelocityTracker {
    public Map<FloatProperty, MonitorInfo> mMonitors = new ArrayMap();

    /* loaded from: classes3.dex */
    public static class MonitorInfo {
        public VelocityMonitor monitor;
        public ResetRunnable resetTask;

        public MonitorInfo() {
            this.monitor = new VelocityMonitor();
            this.resetTask = new ResetRunnable(this);
        }
    }

    /* loaded from: classes3.dex */
    public static class ResetRunnable implements Runnable {
        public MonitorInfo mMonitorInfo;
        public FloatProperty mProperty;
        public WeakReference<IAnimTarget> mTargetRef;

        public ResetRunnable(MonitorInfo monitorInfo) {
            this.mMonitorInfo = monitorInfo;
        }

        public void post(IAnimTarget iAnimTarget, FloatProperty floatProperty) {
            iAnimTarget.handler.removeCallbacks(this);
            WeakReference<IAnimTarget> weakReference = this.mTargetRef;
            if (weakReference == null || weakReference.get() != iAnimTarget) {
                this.mTargetRef = new WeakReference<>(iAnimTarget);
            }
            this.mProperty = floatProperty;
            iAnimTarget.handler.postDelayed(this, 600L);
        }

        @Override // java.lang.Runnable
        public void run() {
            IAnimTarget iAnimTarget = this.mTargetRef.get();
            if (iAnimTarget != null) {
                if (!iAnimTarget.isAnimRunning(this.mProperty)) {
                    iAnimTarget.setVelocity(this.mProperty, SearchStatUtils.POW);
                }
                this.mMonitorInfo.monitor.clear();
            }
        }
    }

    public void trackVelocity(IAnimTarget iAnimTarget, FloatProperty floatProperty, double d) {
        MonitorInfo monitor = getMonitor(floatProperty);
        monitor.monitor.update(d);
        float velocity = monitor.monitor.getVelocity(0);
        if (velocity != 0.0f) {
            monitor.resetTask.post(iAnimTarget, floatProperty);
            iAnimTarget.setVelocity(floatProperty, velocity);
        }
    }

    public final MonitorInfo getMonitor(FloatProperty floatProperty) {
        MonitorInfo monitorInfo = this.mMonitors.get(floatProperty);
        if (monitorInfo == null) {
            MonitorInfo monitorInfo2 = new MonitorInfo();
            this.mMonitors.put(floatProperty, monitorInfo2);
            return monitorInfo2;
        }
        return monitorInfo;
    }
}
