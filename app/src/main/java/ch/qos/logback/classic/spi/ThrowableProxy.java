package ch.qos.logback.classic.spi;

import ch.qos.logback.core.CoreConstants;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

/* loaded from: classes.dex */
public class ThrowableProxy implements IThrowableProxy {
    private static final Method GET_SUPPRESSED_METHOD;
    private static final StackTraceElementProxy[] NO_STACK_TRACE;
    private static final ThrowableProxy[] NO_SUPPRESSED;
    private boolean calculatedPackageData;
    private ThrowableProxy cause;
    private String className;
    public int commonFrames;
    private String message;
    private transient PackagingDataCalculator packagingDataCalculator;
    public StackTraceElementProxy[] stackTraceElementProxyArray;
    private ThrowableProxy[] suppressed;
    private Throwable throwable;

    static {
        Method method;
        try {
            method = Throwable.class.getMethod("getSuppressed", new Class[0]);
        } catch (NoSuchMethodException unused) {
            method = null;
        }
        GET_SUPPRESSED_METHOD = method;
        NO_SUPPRESSED = new ThrowableProxy[0];
        NO_STACK_TRACE = new StackTraceElementProxy[0];
    }

    public ThrowableProxy(Throwable th) {
        this(th, Collections.newSetFromMap(new IdentityHashMap(1)));
    }

    private ThrowableProxy(Throwable th, Set<Throwable> set) {
        this.suppressed = NO_SUPPRESSED;
        this.calculatedPackageData = false;
        this.throwable = th;
        this.className = th.getClass().getName();
        this.message = th.getMessage();
        this.stackTraceElementProxyArray = ThrowableProxyUtil.steArrayToStepArray(th.getStackTrace());
        if (set.contains(th)) {
            this.className = "CIRCULAR REFERENCE:" + th.getClass().getName();
            this.stackTraceElementProxyArray = NO_STACK_TRACE;
            return;
        }
        set.add(th);
        Throwable cause = th.getCause();
        if (cause != null) {
            ThrowableProxy throwableProxy = new ThrowableProxy(cause, set);
            this.cause = throwableProxy;
            throwableProxy.commonFrames = ThrowableProxyUtil.findNumberOfCommonFrames(cause.getStackTrace(), this.stackTraceElementProxyArray);
        }
        Method method = GET_SUPPRESSED_METHOD;
        if (method == null) {
            return;
        }
        try {
            Object invoke = method.invoke(th, new Object[0]);
            if (!(invoke instanceof Throwable[])) {
                return;
            }
            Throwable[] thArr = (Throwable[]) invoke;
            if (thArr.length <= 0) {
                return;
            }
            this.suppressed = new ThrowableProxy[thArr.length];
            for (int i = 0; i < thArr.length; i++) {
                this.suppressed[i] = new ThrowableProxy(thArr[i], set);
                this.suppressed[i].commonFrames = ThrowableProxyUtil.findNumberOfCommonFrames(thArr[i].getStackTrace(), this.stackTraceElementProxyArray);
            }
        } catch (IllegalAccessException | InvocationTargetException unused) {
        }
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    @Override // ch.qos.logback.classic.spi.IThrowableProxy
    public String getMessage() {
        return this.message;
    }

    @Override // ch.qos.logback.classic.spi.IThrowableProxy
    public String getClassName() {
        return this.className;
    }

    @Override // ch.qos.logback.classic.spi.IThrowableProxy
    public StackTraceElementProxy[] getStackTraceElementProxyArray() {
        return this.stackTraceElementProxyArray;
    }

    @Override // ch.qos.logback.classic.spi.IThrowableProxy
    public int getCommonFrames() {
        return this.commonFrames;
    }

    @Override // ch.qos.logback.classic.spi.IThrowableProxy
    public IThrowableProxy getCause() {
        return this.cause;
    }

    @Override // ch.qos.logback.classic.spi.IThrowableProxy
    public IThrowableProxy[] getSuppressed() {
        return this.suppressed;
    }

    public PackagingDataCalculator getPackagingDataCalculator() {
        if (this.throwable != null && this.packagingDataCalculator == null) {
            this.packagingDataCalculator = new PackagingDataCalculator();
        }
        return this.packagingDataCalculator;
    }

    public void calculatePackagingData() {
        PackagingDataCalculator packagingDataCalculator;
        if (!this.calculatedPackageData && (packagingDataCalculator = getPackagingDataCalculator()) != null) {
            this.calculatedPackageData = true;
            packagingDataCalculator.calculate(this);
        }
    }

    public void fullDump() {
        StackTraceElementProxy[] stackTraceElementProxyArr;
        StringBuilder sb = new StringBuilder();
        for (StackTraceElementProxy stackTraceElementProxy : this.stackTraceElementProxyArray) {
            String stackTraceElementProxy2 = stackTraceElementProxy.toString();
            sb.append('\t');
            sb.append(stackTraceElementProxy2);
            ThrowableProxyUtil.subjoinPackagingData(sb, stackTraceElementProxy);
            sb.append(CoreConstants.LINE_SEPARATOR);
        }
        System.out.println(sb.toString());
    }
}
