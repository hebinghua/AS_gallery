package miuix.animation;

import java.util.concurrent.atomic.AtomicInteger;
import miuix.animation.property.ColorProperty;
import miuix.animation.property.FloatProperty;
import miuix.animation.property.IIntValueProperty;
import miuix.animation.property.IntValueProperty;
import miuix.animation.property.ValueProperty;
import miuix.animation.property.ValueTargetObject;
import miuix.animation.property.ViewProperty;

/* loaded from: classes3.dex */
public class ValueTarget extends IAnimTarget {
    public static ITargetCreator sCreator = new ITargetCreator() { // from class: miuix.animation.ValueTarget.1
        @Override // miuix.animation.ITargetCreator
        public IAnimTarget createTarget(Object obj) {
            return new ValueTarget(obj);
        }
    };
    public AtomicInteger mMaxType;
    public ValueTargetObject mTargetObj;

    @Override // miuix.animation.IAnimTarget
    public void clean() {
    }

    @Override // miuix.animation.IAnimTarget
    public float getDefaultMinVisible() {
        return 0.002f;
    }

    public ValueTarget() {
        this(null);
    }

    public ValueTarget(Object obj) {
        this.mMaxType = new AtomicInteger(1000);
        this.mTargetObj = new ValueTargetObject(obj == null ? Integer.valueOf(getId()) : obj);
    }

    @Override // miuix.animation.IAnimTarget
    public void setValue(FloatProperty floatProperty, float f) {
        if (isPredefinedProperty(floatProperty)) {
            this.mTargetObj.setPropertyValue(floatProperty.getName(), Float.TYPE, Float.valueOf(f));
        } else {
            floatProperty.setValue(this.mTargetObj.getRealObject(), f);
        }
    }

    @Override // miuix.animation.IAnimTarget
    public float getValue(FloatProperty floatProperty) {
        if (isPredefinedProperty(floatProperty)) {
            Float f = (Float) this.mTargetObj.getPropertyValue(floatProperty.getName(), Float.TYPE);
            if (f != null) {
                return f.floatValue();
            }
            return Float.MAX_VALUE;
        }
        return floatProperty.getValue(this.mTargetObj.getRealObject());
    }

    @Override // miuix.animation.IAnimTarget
    public void setIntValue(IIntValueProperty iIntValueProperty, int i) {
        if (isPredefinedProperty(iIntValueProperty)) {
            this.mTargetObj.setPropertyValue(iIntValueProperty.getName(), Integer.TYPE, Integer.valueOf(i));
        } else {
            iIntValueProperty.setIntValue(this.mTargetObj.getRealObject(), i);
        }
    }

    @Override // miuix.animation.IAnimTarget
    public int getIntValue(IIntValueProperty iIntValueProperty) {
        if (isPredefinedProperty(iIntValueProperty)) {
            Integer num = (Integer) this.mTargetObj.getPropertyValue(iIntValueProperty.getName(), Integer.TYPE);
            if (num != null) {
                return num.intValue();
            }
            return Integer.MAX_VALUE;
        }
        return iIntValueProperty.getIntValue(this.mTargetObj.getRealObject());
    }

    @Override // miuix.animation.IAnimTarget
    public float getMinVisibleChange(Object obj) {
        if (!(obj instanceof IIntValueProperty) || (obj instanceof ColorProperty)) {
            return super.getMinVisibleChange(obj);
        }
        return 1.0f;
    }

    @Override // miuix.animation.IAnimTarget
    public boolean isValid() {
        return this.mTargetObj.isValid();
    }

    @Override // miuix.animation.IAnimTarget
    /* renamed from: getTargetObject */
    public Object mo2588getTargetObject() {
        return this.mTargetObj;
    }

    public FloatProperty createProperty(String str, Class<?> cls) {
        return (cls == Integer.TYPE || cls == Integer.class) ? new IntValueProperty(str) : new ValueProperty(str);
    }

    public final boolean isPredefinedProperty(Object obj) {
        return (obj instanceof ValueProperty) || (obj instanceof ViewProperty) || (obj instanceof ColorProperty);
    }

    public FloatProperty getFloatProperty(String str) {
        return createProperty(str, Float.TYPE);
    }
}
