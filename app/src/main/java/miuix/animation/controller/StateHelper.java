package miuix.animation.controller;

import java.lang.reflect.Array;
import miuix.animation.IAnimTarget;
import miuix.animation.ValueTarget;
import miuix.animation.base.AnimConfig;
import miuix.animation.base.AnimConfigLink;
import miuix.animation.listener.TransitionListener;
import miuix.animation.property.FloatProperty;
import miuix.animation.property.IIntValueProperty;
import miuix.animation.property.IntValueProperty;
import miuix.animation.property.ValueProperty;
import miuix.animation.utils.EaseManager;

/* loaded from: classes3.dex */
public class StateHelper {
    public static final ValueProperty DEFAULT_PROPERTY = new ValueProperty("defaultProperty");
    public static final IntValueProperty DEFAULT_INT_PROPERTY = new IntValueProperty("defaultIntProperty");

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v3, types: [boolean] */
    /* JADX WARN: Type inference failed for: r0v4 */
    /* JADX WARN: Type inference failed for: r0v6, types: [int] */
    public void parse(IAnimTarget iAnimTarget, AnimState animState, AnimConfigLink animConfigLink, Object... objArr) {
        if (objArr.length == 0) {
            return;
        }
        ?? equals = objArr[0].equals(animState.getTag());
        while (true) {
            int i = equals;
            while (i < objArr.length) {
                Object obj = objArr[i];
                equals = i + 1;
                Object obj2 = equals < objArr.length ? objArr[equals] : null;
                if (!(obj instanceof String) || !(obj2 instanceof String)) {
                    i = setPropertyAndValue(iAnimTarget, animState, animConfigLink, obj, obj2, i, objArr);
                }
            }
            return;
        }
    }

    public final int setPropertyAndValue(IAnimTarget iAnimTarget, AnimState animState, AnimConfigLink animConfigLink, Object obj, Object obj2, int i, Object... objArr) {
        int i2;
        FloatProperty property;
        if (checkAndSetAnimConfig(animConfigLink, obj) || (property = getProperty(iAnimTarget, obj, obj2)) == null) {
            i2 = 0;
        } else {
            if (!isDefaultProperty(property)) {
                i++;
            }
            i2 = addProperty(iAnimTarget, animState, property, i, objArr);
        }
        return i2 > 0 ? i + i2 : i + 1;
    }

    public final boolean isDefaultProperty(FloatProperty floatProperty) {
        return floatProperty == DEFAULT_PROPERTY || floatProperty == DEFAULT_INT_PROPERTY;
    }

    public final boolean checkAndSetAnimConfig(AnimConfigLink animConfigLink, Object obj) {
        if ((obj instanceof TransitionListener) || (obj instanceof EaseManager.EaseStyle)) {
            setTempConfig(animConfigLink.getHead(), obj);
            return true;
        } else if (obj.getClass().isArray()) {
            int length = Array.getLength(obj);
            boolean z = false;
            for (int i = 0; i < length; i++) {
                z = addConfigToLink(animConfigLink, Array.get(obj, i)) || z;
            }
            return z;
        } else {
            return addConfigToLink(animConfigLink, obj);
        }
    }

    public final void setTempConfig(AnimConfig animConfig, Object obj) {
        if (obj instanceof TransitionListener) {
            animConfig.addListeners((TransitionListener) obj);
        } else if (!(obj instanceof EaseManager.EaseStyle)) {
        } else {
            animConfig.setEase((EaseManager.EaseStyle) obj);
        }
    }

    public final boolean addConfigToLink(AnimConfigLink animConfigLink, Object obj) {
        if (obj instanceof AnimConfig) {
            animConfigLink.add((AnimConfig) obj, new boolean[0]);
            return true;
        }
        if (obj instanceof AnimConfigLink) {
            animConfigLink.add((AnimConfigLink) obj, new boolean[0]);
        }
        return false;
    }

    public final FloatProperty getProperty(IAnimTarget iAnimTarget, Object obj, Object obj2) {
        Class<?> cls = null;
        if (obj instanceof FloatProperty) {
            return (FloatProperty) obj;
        }
        if ((obj instanceof String) && (iAnimTarget instanceof ValueTarget)) {
            if (obj2 != null) {
                cls = obj2.getClass();
            }
            return ((ValueTarget) iAnimTarget).createProperty((String) obj, cls);
        } else if (!(obj instanceof Float)) {
            return null;
        } else {
            return DEFAULT_PROPERTY;
        }
    }

    public final int addProperty(IAnimTarget iAnimTarget, AnimState animState, FloatProperty floatProperty, int i, Object... objArr) {
        Object propertyValue;
        if (floatProperty == null || (propertyValue = getPropertyValue(i, objArr)) == null || !addPropertyValue(animState, floatProperty, propertyValue)) {
            return 0;
        }
        return setInitVelocity(iAnimTarget, floatProperty, i + 1, objArr) ? 2 : 1;
    }

    public final Object getPropertyValue(int i, Object... objArr) {
        if (i < objArr.length) {
            return objArr[i];
        }
        return null;
    }

    public final boolean setInitVelocity(IAnimTarget iAnimTarget, FloatProperty floatProperty, int i, Object... objArr) {
        if (i >= objArr.length) {
            return false;
        }
        Object obj = objArr[i];
        if (!(obj instanceof Float)) {
            return false;
        }
        iAnimTarget.setVelocity(floatProperty, ((Float) obj).floatValue());
        return true;
    }

    public final boolean addPropertyValue(AnimState animState, FloatProperty floatProperty, Object obj) {
        boolean z = obj instanceof Integer;
        if (z || (obj instanceof Float) || (obj instanceof Double)) {
            if (floatProperty instanceof IIntValueProperty) {
                animState.add(floatProperty, toInt(obj, z));
                return true;
            }
            animState.add(floatProperty, toFloat(obj, z));
            return true;
        }
        return false;
    }

    public final int toInt(Object obj, boolean z) {
        return z ? ((Integer) obj).intValue() : (int) ((Float) obj).floatValue();
    }

    public final float toFloat(Object obj, boolean z) {
        return z ? ((Integer) obj).intValue() : ((Float) obj).floatValue();
    }
}
