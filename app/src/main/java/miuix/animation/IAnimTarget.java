package miuix.animation;

import android.os.SystemClock;
import android.util.ArrayMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import miuix.animation.base.AnimConfigLink;
import miuix.animation.controller.AnimState;
import miuix.animation.internal.AnimManager;
import miuix.animation.internal.NotifyManager;
import miuix.animation.internal.TargetHandler;
import miuix.animation.internal.TargetVelocityTracker;
import miuix.animation.listener.ListenerNotifier;
import miuix.animation.property.FloatProperty;
import miuix.animation.property.IIntValueProperty;
import miuix.animation.property.ViewProperty;
import miuix.animation.property.ViewPropertyExt;
import miuix.animation.utils.CommonUtils;
import miuix.animation.utils.LogUtils;

/* loaded from: classes3.dex */
public abstract class IAnimTarget<T> {
    public static final AtomicInteger sTargetIds = new AtomicInteger(Integer.MAX_VALUE);
    public final AnimManager animManager;
    public final TargetHandler handler = new TargetHandler(this);
    public final int id;
    public float mDefaultMinVisible;
    public long mFlags;
    public long mFlagsSetTime;
    public Map<Object, Float> mMinVisibleChanges;
    public final TargetVelocityTracker mTracker;
    public NotifyManager notifyManager;

    public abstract void clean();

    public float getDefaultMinVisible() {
        return 1.0f;
    }

    /* renamed from: getTargetObject */
    public abstract T mo2588getTargetObject();

    public boolean isValid() {
        return true;
    }

    public IAnimTarget() {
        AnimManager animManager = new AnimManager();
        this.animManager = animManager;
        this.notifyManager = new NotifyManager(this);
        this.mDefaultMinVisible = Float.MAX_VALUE;
        this.mMinVisibleChanges = new ArrayMap();
        this.id = sTargetIds.decrementAndGet();
        this.mTracker = new TargetVelocityTracker();
        if (LogUtils.isLogEnabled()) {
            LogUtils.debug("IAnimTarget create ! ", new Object[0]);
        }
        animManager.setTarget(this);
        setMinVisibleChange(0.1f, ViewProperty.ROTATION, ViewProperty.ROTATION_X, ViewProperty.ROTATION_Y);
        setMinVisibleChange(0.00390625f, ViewProperty.ALPHA, ViewProperty.AUTO_ALPHA, ViewPropertyExt.FOREGROUND, ViewPropertyExt.BACKGROUND);
        setMinVisibleChange(0.002f, ViewProperty.SCALE_X, ViewProperty.SCALE_Y);
    }

    public ListenerNotifier getNotifier() {
        return this.notifyManager.getNotifier();
    }

    public void setToNotify(AnimState animState, AnimConfigLink animConfigLink) {
        this.notifyManager.setToNotify(animState, animConfigLink);
    }

    public boolean isAnimRunning(FloatProperty... floatPropertyArr) {
        return this.animManager.isAnimRunning(floatPropertyArr);
    }

    public int getId() {
        return this.id;
    }

    public void setFlags(long j) {
        this.mFlags = j;
        this.mFlagsSetTime = SystemClock.elapsedRealtime();
    }

    public boolean isValidFlag() {
        return SystemClock.elapsedRealtime() - this.mFlagsSetTime > 3;
    }

    public boolean hasFlags(long j) {
        return CommonUtils.hasFlags(this.mFlags, j);
    }

    public float getMinVisibleChange(Object obj) {
        Float f = this.mMinVisibleChanges.get(obj);
        if (f != null) {
            return f.floatValue();
        }
        float f2 = this.mDefaultMinVisible;
        return f2 != Float.MAX_VALUE ? f2 : getDefaultMinVisible();
    }

    public IAnimTarget setMinVisibleChange(float f, FloatProperty... floatPropertyArr) {
        for (FloatProperty floatProperty : floatPropertyArr) {
            this.mMinVisibleChanges.put(floatProperty, Float.valueOf(f));
        }
        return this;
    }

    public void executeOnInitialized(Runnable runnable) {
        post(runnable);
    }

    public float getValue(FloatProperty floatProperty) {
        T mo2588getTargetObject = mo2588getTargetObject();
        if (mo2588getTargetObject != null) {
            return floatProperty.getValue(mo2588getTargetObject);
        }
        return Float.MAX_VALUE;
    }

    public void setValue(FloatProperty floatProperty, float f) {
        T mo2588getTargetObject = mo2588getTargetObject();
        if (mo2588getTargetObject == null || Math.abs(f) == Float.MAX_VALUE) {
            return;
        }
        floatProperty.setValue(mo2588getTargetObject, f);
    }

    public int getIntValue(IIntValueProperty iIntValueProperty) {
        T mo2588getTargetObject = mo2588getTargetObject();
        if (mo2588getTargetObject != null) {
            return iIntValueProperty.getIntValue(mo2588getTargetObject);
        }
        return Integer.MAX_VALUE;
    }

    public void setIntValue(IIntValueProperty iIntValueProperty, int i) {
        T mo2588getTargetObject = mo2588getTargetObject();
        if (mo2588getTargetObject == null || Math.abs(i) == Integer.MAX_VALUE) {
            return;
        }
        iIntValueProperty.setIntValue(mo2588getTargetObject, i);
    }

    public void setVelocity(FloatProperty floatProperty, double d) {
        if (d != 3.4028234663852886E38d) {
            this.animManager.setVelocity(floatProperty, (float) d);
        }
    }

    public void post(Runnable runnable) {
        if (this.handler.threadId == Thread.currentThread().getId()) {
            runnable.run();
        } else {
            this.handler.post(runnable);
        }
    }

    public void trackVelocity(FloatProperty floatProperty, double d) {
        this.mTracker.trackVelocity(this, floatProperty, d);
    }

    public String toString() {
        return "IAnimTarget{" + mo2588getTargetObject() + "}";
    }

    public void finalize() throws Throwable {
        if (LogUtils.isLogEnabled()) {
            LogUtils.debug("IAnimTarget was destroyed ！", new Object[0]);
        }
        super.finalize();
    }
}
