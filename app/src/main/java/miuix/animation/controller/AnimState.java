package miuix.animation.controller;

import ch.qos.logback.core.CoreConstants;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import miuix.animation.IAnimTarget;
import miuix.animation.base.AnimConfig;
import miuix.animation.base.AnimSpecialConfig;
import miuix.animation.internal.AnimValueUtils;
import miuix.animation.listener.UpdateInfo;
import miuix.animation.property.FloatProperty;
import miuix.animation.property.ISpecificProperty;
import miuix.animation.property.IntValueProperty;
import miuix.animation.property.ValueProperty;
import miuix.animation.property.ViewProperty;
import miuix.animation.utils.CommonUtils;
import miuix.animation.utils.ObjectPool;

/* loaded from: classes3.dex */
public class AnimState {
    public static final AtomicInteger sId = new AtomicInteger();
    public long flags;
    public final boolean isTemporary;
    public final AnimConfig mConfig;
    public final Map<Object, Double> mMap;
    public volatile Object mTag;
    public IntValueProperty tempIntValueProperty;
    public ValueProperty tempValueProperty;

    public static void alignState(AnimState animState, Collection<UpdateInfo> collection) {
        UpdateInfo findByName;
        for (UpdateInfo updateInfo : collection) {
            if (!animState.contains(updateInfo.property)) {
                if (updateInfo.useInt) {
                    animState.add(updateInfo.property, (int) updateInfo.animInfo.startValue);
                } else {
                    animState.add(updateInfo.property, (float) updateInfo.animInfo.startValue);
                }
            }
        }
        List<Object> list = (List) ObjectPool.acquire(ArrayList.class, new Object[0]);
        for (Object obj : animState.keySet()) {
            if (obj instanceof FloatProperty) {
                findByName = UpdateInfo.findBy(collection, (FloatProperty) obj);
            } else {
                findByName = UpdateInfo.findByName(collection, (String) obj);
            }
            if (findByName == null) {
                list.add(obj);
            }
        }
        for (Object obj2 : list) {
            animState.remove(obj2);
        }
        ObjectPool.release(list);
    }

    public AnimState() {
        this(null, false);
    }

    public AnimState(Object obj) {
        this(obj, false);
    }

    public AnimState(Object obj, boolean z) {
        this.tempValueProperty = new ValueProperty("");
        this.tempIntValueProperty = new IntValueProperty("");
        this.mConfig = new AnimConfig();
        this.mMap = new ConcurrentHashMap();
        setTag(obj);
        this.isTemporary = z;
    }

    public final void setTag(Object obj) {
        if (obj == null) {
            obj = "TAG_" + sId.incrementAndGet();
        }
        this.mTag = obj;
    }

    public void clear() {
        this.mConfig.clear();
        this.mMap.clear();
    }

    public void set(AnimState animState) {
        if (animState == null) {
            return;
        }
        setTag(animState.mTag);
        append(animState);
    }

    public final void append(AnimState animState) {
        this.mConfig.copy(animState.mConfig);
        this.mMap.clear();
        this.mMap.putAll(animState.mMap);
    }

    public Object getTag() {
        return this.mTag;
    }

    public AnimState add(ViewProperty viewProperty, int i, long... jArr) {
        return add((FloatProperty) viewProperty, i, jArr);
    }

    public AnimState add(FloatProperty floatProperty, int i, long... jArr) {
        if (jArr.length > 0) {
            setConfigFlag(floatProperty, jArr[0] | 4);
        } else {
            setConfigFlag(floatProperty, getConfigFlags(floatProperty) | 4);
        }
        return add(floatProperty, i);
    }

    public AnimState add(Object obj, double d) {
        setMapValue(obj, d);
        return this;
    }

    public void setConfigFlag(Object obj, long j) {
        this.mConfig.queryAndCreateSpecial(obj instanceof FloatProperty ? ((FloatProperty) obj).getName() : (String) obj).flags = j;
    }

    public boolean contains(Object obj) {
        if (obj == null) {
            return false;
        }
        if (this.mMap.containsKey(obj)) {
            return true;
        }
        if (!(obj instanceof FloatProperty)) {
            return false;
        }
        return this.mMap.containsKey(((FloatProperty) obj).getName());
    }

    public Set<Object> keySet() {
        return this.mMap.keySet();
    }

    public float getFloat(FloatProperty floatProperty) {
        Double mapValue = getMapValue(floatProperty);
        if (mapValue != null) {
            return mapValue.floatValue();
        }
        return Float.MAX_VALUE;
    }

    public double get(IAnimTarget iAnimTarget, FloatProperty floatProperty) {
        Double mapValue = getMapValue(floatProperty);
        if (mapValue != null) {
            return getProperValue(iAnimTarget, floatProperty, mapValue.doubleValue());
        }
        return Double.MAX_VALUE;
    }

    public final Double getMapValue(Object obj) {
        Double d = this.mMap.get(obj);
        return (d != null || !(obj instanceof FloatProperty)) ? d : this.mMap.get(((FloatProperty) obj).getName());
    }

    public final void setMapValue(Object obj, double d) {
        if (obj instanceof FloatProperty) {
            FloatProperty floatProperty = (FloatProperty) obj;
            if (this.mMap.containsKey(floatProperty.getName())) {
                this.mMap.put(floatProperty.getName(), Double.valueOf(d));
                return;
            }
        }
        this.mMap.put(obj, Double.valueOf(d));
    }

    public final double getProperValue(IAnimTarget iAnimTarget, FloatProperty floatProperty, double d) {
        long configFlags = getConfigFlags(floatProperty);
        boolean hasFlags = CommonUtils.hasFlags(configFlags, 1L);
        if (hasFlags || d == 1000000.0d || d == 1000100.0d || (floatProperty instanceof ISpecificProperty)) {
            double value = AnimValueUtils.getValue(iAnimTarget, floatProperty, d);
            if (!hasFlags || AnimValueUtils.isInvalid(d)) {
                return value;
            }
            setConfigFlag(floatProperty, configFlags & (-2));
            double d2 = value + d;
            setMapValue(floatProperty, d2);
            return d2;
        }
        return d;
    }

    public long getConfigFlags(Object obj) {
        AnimSpecialConfig specialConfig = this.mConfig.getSpecialConfig(obj instanceof FloatProperty ? ((FloatProperty) obj).getName() : (String) obj);
        if (specialConfig != null) {
            return specialConfig.flags;
        }
        return 0L;
    }

    public AnimConfig getConfig() {
        return this.mConfig;
    }

    public AnimState remove(Object obj) {
        this.mMap.remove(obj);
        if (obj instanceof FloatProperty) {
            this.mMap.remove(((FloatProperty) obj).getName());
        }
        return this;
    }

    public FloatProperty getProperty(Object obj) {
        if (obj instanceof FloatProperty) {
            return (FloatProperty) obj;
        }
        String str = (String) obj;
        if (CommonUtils.hasFlags(getConfigFlags(str), 4L)) {
            return new IntValueProperty(str);
        }
        return new ValueProperty(str);
    }

    public FloatProperty getTempProperty(Object obj) {
        if (obj instanceof FloatProperty) {
            return (FloatProperty) obj;
        }
        String str = (String) obj;
        ValueProperty valueProperty = CommonUtils.hasFlags(getConfigFlags(str), 4L) ? this.tempIntValueProperty : this.tempValueProperty;
        valueProperty.setName(str);
        return valueProperty;
    }

    public String toString() {
        return "\nAnimState{mTag='" + this.mTag + CoreConstants.SINGLE_QUOTE_CHAR + ", flags:" + this.flags + ", mConfig:" + this.mConfig + ", mMaps=" + ((Object) CommonUtils.mapToString(this.mMap, "    ")) + '}';
    }
}
