package kotlin.jvm.internal;

import java.io.ObjectStreamException;
import java.io.Serializable;
import kotlin.jvm.KotlinReflectionNotSupportedError;
import kotlin.reflect.KCallable;
import kotlin.reflect.KDeclarationContainer;

/* loaded from: classes3.dex */
public abstract class CallableReference implements KCallable, Serializable {
    public static final Object NO_RECEIVER = NoReceiver.INSTANCE;
    private final boolean isTopLevel;
    private final String name;
    private final Class owner;
    public final Object receiver;
    public transient KCallable reflected;
    private final String signature;

    public abstract KCallable computeReflected();

    /* loaded from: classes3.dex */
    public static class NoReceiver implements Serializable {
        public static final NoReceiver INSTANCE = new NoReceiver();

        private NoReceiver() {
        }

        private Object readResolve() throws ObjectStreamException {
            return INSTANCE;
        }
    }

    public CallableReference() {
        this(NO_RECEIVER);
    }

    public CallableReference(Object obj) {
        this(obj, null, null, null, false);
    }

    public CallableReference(Object obj, Class cls, String str, String str2, boolean z) {
        this.receiver = obj;
        this.owner = cls;
        this.name = str;
        this.signature = str2;
        this.isTopLevel = z;
    }

    public Object getBoundReceiver() {
        return this.receiver;
    }

    public KCallable compute() {
        KCallable kCallable = this.reflected;
        if (kCallable == null) {
            KCallable computeReflected = computeReflected();
            this.reflected = computeReflected;
            return computeReflected;
        }
        return kCallable;
    }

    public KCallable getReflected() {
        KCallable compute = compute();
        if (compute != this) {
            return compute;
        }
        throw new KotlinReflectionNotSupportedError();
    }

    public KDeclarationContainer getOwner() {
        Class cls = this.owner;
        if (cls == null) {
            return null;
        }
        return this.isTopLevel ? Reflection.getOrCreateKotlinPackage(cls) : Reflection.getOrCreateKotlinClass(cls);
    }

    public String getName() {
        return this.name;
    }

    public String getSignature() {
        return this.signature;
    }
}
