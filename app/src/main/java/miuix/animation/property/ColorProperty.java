package miuix.animation.property;

import java.util.Objects;

/* loaded from: classes3.dex */
public class ColorProperty<T> extends FloatProperty<T> implements IIntValueProperty<T> {
    public int mColorValue;

    @Override // miuix.animation.property.FloatProperty
    public float getValue(T t) {
        return 0.0f;
    }

    @Override // miuix.animation.property.FloatProperty
    public void setValue(T t, float f) {
    }

    @Override // miuix.animation.property.IIntValueProperty
    public void setIntValue(T t, int i) {
        this.mColorValue = i;
        if (t instanceof ValueTargetObject) {
            ((ValueTargetObject) t).setPropertyValue(getName(), Integer.TYPE, Integer.valueOf(i));
        }
    }

    @Override // miuix.animation.property.IIntValueProperty
    public int getIntValue(T t) {
        if (t instanceof ValueTargetObject) {
            this.mColorValue = ((Integer) ((ValueTargetObject) t).getPropertyValue(getName(), Integer.TYPE)).intValue();
        }
        return this.mColorValue;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj != null && getClass() == obj.getClass()) {
            return this.mPropertyName.equals(((ColorProperty) obj).mPropertyName);
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.mPropertyName);
    }
}
