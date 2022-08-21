package ch.qos.logback.core.joran.util;

import ch.qos.logback.core.joran.spi.DefaultClass;
import ch.qos.logback.core.joran.spi.DefaultNestedComponentRegistry;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.util.AggregationType;
import ch.qos.logback.core.util.PropertySetterException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Locale;

/* loaded from: classes.dex */
public class PropertySetter extends ContextAwareBase {
    public MethodDescriptor[] methodDescriptors;
    public Object obj;
    public Class<?> objClass;
    public PropertyDescriptor[] propertyDescriptors;

    public PropertySetter(Object obj) {
        this.obj = obj;
        this.objClass = obj.getClass();
    }

    public void introspect() {
        try {
            this.propertyDescriptors = Introspector.getPropertyDescriptors(this.objClass);
            this.methodDescriptors = Introspector.getMethodDescriptors(this.objClass);
        } catch (IntrospectionException e) {
            addError("Failed to introspect " + this.obj + ": " + e.getMessage());
            this.propertyDescriptors = new PropertyDescriptor[0];
            this.methodDescriptors = new MethodDescriptor[0];
        }
    }

    public void setProperty(String str, String str2) {
        if (str2 == null) {
            return;
        }
        String decapitalize = Introspector.decapitalize(str);
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(decapitalize);
        if (propertyDescriptor == null) {
            addWarn("No such property [" + decapitalize + "] in " + this.objClass.getName() + ".");
            return;
        }
        try {
            setProperty(propertyDescriptor, decapitalize, str2);
        } catch (PropertySetterException e) {
            addWarn("Failed to set property [" + decapitalize + "] to value \"" + str2 + "\". ", e);
        }
    }

    public void setProperty(PropertyDescriptor propertyDescriptor, String str, String str2) throws PropertySetterException {
        Method writeMethod = propertyDescriptor.getWriteMethod();
        if (writeMethod == null) {
            throw new PropertySetterException("No setter for property [" + str + "].");
        }
        Class<?>[] parameterTypes = writeMethod.getParameterTypes();
        if (parameterTypes.length != 1) {
            throw new PropertySetterException("#params for setter != 1");
        }
        try {
            Object convertArg = StringToObjectConverter.convertArg(this, str2, parameterTypes[0]);
            if (convertArg == null) {
                throw new PropertySetterException("Conversion to type [" + parameterTypes[0] + "] failed.");
            }
            try {
                writeMethod.invoke(this.obj, convertArg);
            } catch (Exception e) {
                throw new PropertySetterException(e);
            }
        } catch (Throwable th) {
            throw new PropertySetterException("Conversion to type [" + parameterTypes[0] + "] failed. ", th);
        }
    }

    public AggregationType computeAggregationType(String str) {
        Method findAdderMethod = findAdderMethod(str);
        if (findAdderMethod != null) {
            AggregationType computeRawAggregationType = computeRawAggregationType(findAdderMethod);
            int i = AnonymousClass1.$SwitchMap$ch$qos$logback$core$util$AggregationType[computeRawAggregationType.ordinal()];
            if (i == 1) {
                return AggregationType.NOT_FOUND;
            }
            if (i == 2) {
                return AggregationType.AS_BASIC_PROPERTY_COLLECTION;
            }
            if (i == 3) {
                return AggregationType.AS_COMPLEX_PROPERTY_COLLECTION;
            }
            if (i == 4 || i == 5) {
                addError("Unexpected AggregationType " + computeRawAggregationType);
            }
        }
        Method findSetterMethod = findSetterMethod(str);
        if (findSetterMethod != null) {
            return computeRawAggregationType(findSetterMethod);
        }
        return AggregationType.NOT_FOUND;
    }

    /* renamed from: ch.qos.logback.core.joran.util.PropertySetter$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$ch$qos$logback$core$util$AggregationType;

        static {
            int[] iArr = new int[AggregationType.values().length];
            $SwitchMap$ch$qos$logback$core$util$AggregationType = iArr;
            try {
                iArr[AggregationType.NOT_FOUND.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$ch$qos$logback$core$util$AggregationType[AggregationType.AS_BASIC_PROPERTY.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$ch$qos$logback$core$util$AggregationType[AggregationType.AS_COMPLEX_PROPERTY.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$ch$qos$logback$core$util$AggregationType[AggregationType.AS_BASIC_PROPERTY_COLLECTION.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$ch$qos$logback$core$util$AggregationType[AggregationType.AS_COMPLEX_PROPERTY_COLLECTION.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
        }
    }

    private Method findAdderMethod(String str) {
        String capitalizeFirstLetter = capitalizeFirstLetter(str);
        return getMethod("add" + capitalizeFirstLetter);
    }

    private Method findSetterMethod(String str) {
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(Introspector.decapitalize(str));
        if (propertyDescriptor != null) {
            return propertyDescriptor.getWriteMethod();
        }
        return null;
    }

    private Class<?> getParameterClassForMethod(Method method) {
        if (method == null) {
            return null;
        }
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length == 1) {
            return parameterTypes[0];
        }
        return null;
    }

    private AggregationType computeRawAggregationType(Method method) {
        Class<?> parameterClassForMethod = getParameterClassForMethod(method);
        if (parameterClassForMethod == null) {
            return AggregationType.NOT_FOUND;
        }
        if (StringToObjectConverter.canBeBuiltFromSimpleString(parameterClassForMethod)) {
            return AggregationType.AS_BASIC_PROPERTY;
        }
        return AggregationType.AS_COMPLEX_PROPERTY;
    }

    private boolean isUnequivocallyInstantiable(Class<?> cls) {
        if (cls.isInterface()) {
            return false;
        }
        return cls.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]) != null;
    }

    public Class<?> getObjClass() {
        return this.objClass;
    }

    public void addComplexProperty(String str, Object obj) {
        Method findAdderMethod = findAdderMethod(str);
        if (findAdderMethod != null) {
            if (!isSanityCheckSuccessful(str, findAdderMethod, findAdderMethod.getParameterTypes(), obj)) {
                return;
            }
            invokeMethodWithSingleParameterOnThisObject(findAdderMethod, obj);
            return;
        }
        addError("Could not find method [add" + str + "] in class [" + this.objClass.getName() + "].");
    }

    public void invokeMethodWithSingleParameterOnThisObject(Method method, Object obj) {
        Class<?> cls = obj.getClass();
        try {
            method.invoke(this.obj, obj);
        } catch (Exception e) {
            addError("Could not invoke method " + method.getName() + " in class " + this.obj.getClass().getName() + " with parameter of type " + cls.getName(), e);
        }
    }

    public void addBasicProperty(String str, String str2) {
        if (str2 == null) {
            return;
        }
        String capitalizeFirstLetter = capitalizeFirstLetter(str);
        Method findAdderMethod = findAdderMethod(capitalizeFirstLetter);
        if (findAdderMethod == null) {
            addError("No adder for property [" + capitalizeFirstLetter + "].");
            return;
        }
        Class<?>[] parameterTypes = findAdderMethod.getParameterTypes();
        isSanityCheckSuccessful(capitalizeFirstLetter, findAdderMethod, parameterTypes, str2);
        try {
            if (StringToObjectConverter.convertArg(this, str2, parameterTypes[0]) == null) {
                return;
            }
            invokeMethodWithSingleParameterOnThisObject(findAdderMethod, str2);
        } catch (Throwable th) {
            addError("Conversion to type [" + parameterTypes[0] + "] failed. ", th);
        }
    }

    public void setComplexProperty(String str, Object obj) {
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(Introspector.decapitalize(str));
        if (propertyDescriptor == null) {
            addWarn("Could not find PropertyDescriptor for [" + str + "] in " + this.objClass.getName());
            return;
        }
        Method writeMethod = propertyDescriptor.getWriteMethod();
        if (writeMethod == null) {
            addWarn("Not setter method for property [" + str + "] in " + this.obj.getClass().getName());
        } else if (!isSanityCheckSuccessful(str, writeMethod, writeMethod.getParameterTypes(), obj)) {
        } else {
            try {
                invokeMethodWithSingleParameterOnThisObject(writeMethod, obj);
            } catch (Exception e) {
                addError("Could not set component " + this.obj + " for parent component " + this.obj, e);
            }
        }
    }

    private boolean isSanityCheckSuccessful(String str, Method method, Class<?>[] clsArr, Object obj) {
        Class<?> cls = obj.getClass();
        if (clsArr.length != 1) {
            addError("Wrong number of parameters in setter method for property [" + str + "] in " + this.obj.getClass().getName());
            return false;
        } else if (clsArr[0].isAssignableFrom(obj.getClass())) {
            return true;
        } else {
            addError("A \"" + cls.getName() + "\" object is not assignable to a \"" + clsArr[0].getName() + "\" variable.");
            StringBuilder sb = new StringBuilder();
            sb.append("The class \"");
            sb.append(clsArr[0].getName());
            sb.append("\" was loaded by ");
            addError(sb.toString());
            addError("[" + clsArr[0].getClassLoader() + "] whereas object of type ");
            addError("\"" + cls.getName() + "\" was loaded by [" + cls.getClassLoader() + "].");
            return false;
        }
    }

    private String capitalizeFirstLetter(String str) {
        return str.substring(0, 1).toUpperCase(Locale.US) + str.substring(1);
    }

    public Method getMethod(String str) {
        if (this.methodDescriptors == null) {
            introspect();
        }
        int i = 0;
        while (true) {
            MethodDescriptor[] methodDescriptorArr = this.methodDescriptors;
            if (i < methodDescriptorArr.length) {
                if (str.equals(methodDescriptorArr[i].getName())) {
                    return this.methodDescriptors[i].getMethod();
                }
                i++;
            } else {
                return null;
            }
        }
    }

    public PropertyDescriptor getPropertyDescriptor(String str) {
        if (this.propertyDescriptors == null) {
            introspect();
        }
        int i = 0;
        while (true) {
            PropertyDescriptor[] propertyDescriptorArr = this.propertyDescriptors;
            if (i < propertyDescriptorArr.length) {
                if (str.equals(propertyDescriptorArr[i].getName())) {
                    return this.propertyDescriptors[i];
                }
                i++;
            } else {
                return null;
            }
        }
    }

    public Object getObj() {
        return this.obj;
    }

    public Method getRelevantMethod(String str, AggregationType aggregationType) {
        String capitalizeFirstLetter = capitalizeFirstLetter(str);
        if (aggregationType == AggregationType.AS_COMPLEX_PROPERTY_COLLECTION) {
            return findAdderMethod(capitalizeFirstLetter);
        }
        if (aggregationType == AggregationType.AS_COMPLEX_PROPERTY) {
            return findSetterMethod(capitalizeFirstLetter);
        }
        throw new IllegalStateException(aggregationType + " not allowed here");
    }

    public <T extends Annotation> T getAnnotation(String str, Class<T> cls, Method method) {
        if (method != null) {
            return (T) method.getAnnotation(cls);
        }
        return null;
    }

    public Class<?> getDefaultClassNameByAnnonation(String str, Method method) {
        DefaultClass defaultClass = (DefaultClass) getAnnotation(str, DefaultClass.class, method);
        if (defaultClass != null) {
            return defaultClass.value();
        }
        return null;
    }

    public Class<?> getByConcreteType(String str, Method method) {
        Class<?> parameterClassForMethod = getParameterClassForMethod(method);
        if (parameterClassForMethod != null && isUnequivocallyInstantiable(parameterClassForMethod)) {
            return parameterClassForMethod;
        }
        return null;
    }

    public Class<?> getClassNameViaImplicitRules(String str, AggregationType aggregationType, DefaultNestedComponentRegistry defaultNestedComponentRegistry) {
        Class<?> findDefaultComponentType = defaultNestedComponentRegistry.findDefaultComponentType(this.obj.getClass(), str);
        if (findDefaultComponentType != null) {
            return findDefaultComponentType;
        }
        Method relevantMethod = getRelevantMethod(str, aggregationType);
        if (relevantMethod == null) {
            return null;
        }
        Class<?> defaultClassNameByAnnonation = getDefaultClassNameByAnnonation(str, relevantMethod);
        return defaultClassNameByAnnonation != null ? defaultClassNameByAnnonation : getByConcreteType(str, relevantMethod);
    }
}
