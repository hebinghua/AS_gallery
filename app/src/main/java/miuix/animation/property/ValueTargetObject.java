package miuix.animation.property;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import miuix.animation.utils.CommonUtils;
import miuix.animation.utils.FieldManager;

/* loaded from: classes3.dex */
public class ValueTargetObject {
    public WeakReference<Object> mRef;
    public Object mTempObj;
    public FieldManager mFieldManager = new FieldManager();
    public Map<String, Object> mValueMap = new ConcurrentHashMap();

    public ValueTargetObject(Object obj) {
        if (CommonUtils.isBuiltInClass(obj.getClass())) {
            this.mTempObj = obj;
        } else {
            this.mRef = new WeakReference<>(obj);
        }
    }

    public boolean isValid() {
        return getRealObject() != null;
    }

    public Object getRealObject() {
        WeakReference<Object> weakReference = this.mRef;
        return weakReference != null ? weakReference.get() : this.mTempObj;
    }

    public <T> T getPropertyValue(String str, Class<T> cls) {
        Object realObject = getRealObject();
        if (realObject == null || this.mTempObj == realObject) {
            return (T) this.mValueMap.get(str);
        }
        T t = (T) this.mValueMap.get(str);
        return t != null ? t : (T) this.mFieldManager.getField(realObject, str, cls);
    }

    public <T> void setPropertyValue(String str, Class<T> cls, T t) {
        Object realObject = getRealObject();
        if (realObject == null || this.mTempObj == realObject) {
            this.mValueMap.put(str, t);
        } else if (!this.mValueMap.containsKey(str) && this.mFieldManager.setField(realObject, str, cls, t)) {
        } else {
            this.mValueMap.put(str, t);
        }
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != getClass()) {
            Object obj2 = this.mTempObj;
            if (obj2 != null) {
                return Objects.equals(obj2, obj);
            }
            Object realObject = getRealObject();
            if (realObject == null) {
                return false;
            }
            return realObject.equals(obj);
        }
        ValueTargetObject valueTargetObject = (ValueTargetObject) obj;
        Object obj3 = this.mTempObj;
        if (obj3 != null) {
            return Objects.equals(obj3, valueTargetObject.mTempObj);
        }
        return Objects.equals(getRealObject(), valueTargetObject.getRealObject());
    }

    public int hashCode() {
        Object obj = this.mTempObj;
        if (obj != null) {
            return obj.hashCode();
        }
        Object realObject = getRealObject();
        if (realObject == null) {
            return 0;
        }
        return realObject.hashCode();
    }

    public String toString() {
        return "ValueTargetObject{" + getRealObject() + "}";
    }
}
