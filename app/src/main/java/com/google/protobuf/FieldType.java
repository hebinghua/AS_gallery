package com.google.protobuf;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.List;

/* JADX WARN: Init of enum BOOL can be incorrect */
/* JADX WARN: Init of enum BOOL_LIST can be incorrect */
/* JADX WARN: Init of enum BOOL_LIST_PACKED can be incorrect */
/* JADX WARN: Init of enum BYTES can be incorrect */
/* JADX WARN: Init of enum BYTES_LIST can be incorrect */
/* JADX WARN: Init of enum DOUBLE can be incorrect */
/* JADX WARN: Init of enum DOUBLE_LIST can be incorrect */
/* JADX WARN: Init of enum DOUBLE_LIST_PACKED can be incorrect */
/* JADX WARN: Init of enum ENUM can be incorrect */
/* JADX WARN: Init of enum ENUM_LIST can be incorrect */
/* JADX WARN: Init of enum ENUM_LIST_PACKED can be incorrect */
/* JADX WARN: Init of enum FIXED32 can be incorrect */
/* JADX WARN: Init of enum FIXED32_LIST can be incorrect */
/* JADX WARN: Init of enum FIXED32_LIST_PACKED can be incorrect */
/* JADX WARN: Init of enum FIXED64 can be incorrect */
/* JADX WARN: Init of enum FIXED64_LIST can be incorrect */
/* JADX WARN: Init of enum FIXED64_LIST_PACKED can be incorrect */
/* JADX WARN: Init of enum FLOAT can be incorrect */
/* JADX WARN: Init of enum FLOAT_LIST can be incorrect */
/* JADX WARN: Init of enum FLOAT_LIST_PACKED can be incorrect */
/* JADX WARN: Init of enum GROUP can be incorrect */
/* JADX WARN: Init of enum GROUP_LIST can be incorrect */
/* JADX WARN: Init of enum INT32 can be incorrect */
/* JADX WARN: Init of enum INT32_LIST can be incorrect */
/* JADX WARN: Init of enum INT32_LIST_PACKED can be incorrect */
/* JADX WARN: Init of enum INT64 can be incorrect */
/* JADX WARN: Init of enum INT64_LIST can be incorrect */
/* JADX WARN: Init of enum INT64_LIST_PACKED can be incorrect */
/* JADX WARN: Init of enum MESSAGE can be incorrect */
/* JADX WARN: Init of enum MESSAGE_LIST can be incorrect */
/* JADX WARN: Init of enum SFIXED32 can be incorrect */
/* JADX WARN: Init of enum SFIXED32_LIST can be incorrect */
/* JADX WARN: Init of enum SFIXED32_LIST_PACKED can be incorrect */
/* JADX WARN: Init of enum SFIXED64 can be incorrect */
/* JADX WARN: Init of enum SFIXED64_LIST can be incorrect */
/* JADX WARN: Init of enum SFIXED64_LIST_PACKED can be incorrect */
/* JADX WARN: Init of enum SINT32 can be incorrect */
/* JADX WARN: Init of enum SINT32_LIST can be incorrect */
/* JADX WARN: Init of enum SINT32_LIST_PACKED can be incorrect */
/* JADX WARN: Init of enum SINT64 can be incorrect */
/* JADX WARN: Init of enum SINT64_LIST can be incorrect */
/* JADX WARN: Init of enum SINT64_LIST_PACKED can be incorrect */
/* JADX WARN: Init of enum STRING can be incorrect */
/* JADX WARN: Init of enum STRING_LIST can be incorrect */
/* JADX WARN: Init of enum UINT32 can be incorrect */
/* JADX WARN: Init of enum UINT32_LIST can be incorrect */
/* JADX WARN: Init of enum UINT32_LIST_PACKED can be incorrect */
/* JADX WARN: Init of enum UINT64 can be incorrect */
/* JADX WARN: Init of enum UINT64_LIST can be incorrect */
/* JADX WARN: Init of enum UINT64_LIST_PACKED can be incorrect */
/* loaded from: classes.dex */
public enum FieldType {
    DOUBLE(0, r7, r8),
    FLOAT(1, r7, r10),
    INT64(2, r7, r12),
    UINT64(3, r7, r12),
    INT32(4, r7, r15),
    FIXED64(5, r7, r12),
    FIXED32(6, r7, r15),
    BOOL(7, r7, r19),
    STRING(8, r7, r21),
    MESSAGE(9, r7, r23),
    BYTES(10, r7, r25),
    UINT32(11, r7, r15),
    ENUM(12, r7, r28),
    SFIXED32(13, r7, r15),
    SFIXED64(14, r7, r12),
    SINT32(15, r7, r15),
    SINT64(16, r7, r12),
    GROUP(17, r7, r23),
    DOUBLE_LIST(18, r34, r8),
    FLOAT_LIST(19, r34, r10),
    INT64_LIST(20, r34, r12),
    UINT64_LIST(21, r34, r12),
    INT32_LIST(22, r34, r15),
    FIXED64_LIST(23, r34, r12),
    FIXED32_LIST(24, r34, r15),
    BOOL_LIST(25, r34, r19),
    STRING_LIST(26, r34, r21),
    MESSAGE_LIST(27, r34, r23),
    BYTES_LIST(28, r34, r25),
    UINT32_LIST(29, r34, r15),
    ENUM_LIST(30, r34, r28),
    SFIXED32_LIST(31, r34, r15),
    SFIXED64_LIST(32, r34, r12),
    SINT32_LIST(33, r34, r15),
    SINT64_LIST(34, r34, r12),
    DOUBLE_LIST_PACKED(35, r50, r8),
    FLOAT_LIST_PACKED(36, r50, r10),
    INT64_LIST_PACKED(37, r50, r12),
    UINT64_LIST_PACKED(38, r50, r12),
    INT32_LIST_PACKED(39, r50, r15),
    FIXED64_LIST_PACKED(40, r50, r12),
    FIXED32_LIST_PACKED(41, r50, r15),
    BOOL_LIST_PACKED(42, r50, r19),
    UINT32_LIST_PACKED(43, r50, r15),
    ENUM_LIST_PACKED(44, r50, r28),
    SFIXED32_LIST_PACKED(45, r50, r15),
    SFIXED64_LIST_PACKED(46, r50, r12),
    SINT32_LIST_PACKED(47, r50, r15),
    SINT64_LIST_PACKED(48, r50, r12),
    GROUP_LIST(49, r34, r23),
    MAP(50, Collection.MAP, JavaType.VOID);
    
    private static final java.lang.reflect.Type[] EMPTY_TYPES;
    private static final FieldType[] VALUES;
    private final Collection collection;
    private final Class<?> elementType;
    private final int id;
    private final JavaType javaType;
    private final boolean primitiveScalar;

    static {
        Collection collection = Collection.SCALAR;
        JavaType javaType = JavaType.DOUBLE;
        JavaType javaType2 = JavaType.FLOAT;
        JavaType javaType3 = JavaType.LONG;
        JavaType javaType4 = JavaType.INT;
        JavaType javaType5 = JavaType.BOOLEAN;
        JavaType javaType6 = JavaType.STRING;
        JavaType javaType7 = JavaType.MESSAGE;
        JavaType javaType8 = JavaType.BYTE_STRING;
        JavaType javaType9 = JavaType.ENUM;
        Collection collection2 = Collection.VECTOR;
        Collection collection3 = Collection.PACKED_VECTOR;
        EMPTY_TYPES = new java.lang.reflect.Type[0];
        FieldType[] values = values();
        VALUES = new FieldType[values.length];
        for (FieldType fieldType : values) {
            VALUES[fieldType.id] = fieldType;
        }
    }

    FieldType(int i, Collection collection, JavaType javaType) {
        int i2;
        this.id = i;
        this.collection = collection;
        this.javaType = javaType;
        int i3 = AnonymousClass1.$SwitchMap$com$google$protobuf$FieldType$Collection[collection.ordinal()];
        boolean z = true;
        if (i3 == 1) {
            this.elementType = javaType.getBoxedType();
        } else if (i3 == 2) {
            this.elementType = javaType.getBoxedType();
        } else {
            this.elementType = null;
        }
        this.primitiveScalar = (collection != Collection.SCALAR || (i2 = AnonymousClass1.$SwitchMap$com$google$protobuf$JavaType[javaType.ordinal()]) == 1 || i2 == 2 || i2 == 3) ? false : z;
    }

    /* renamed from: com.google.protobuf.FieldType$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$google$protobuf$FieldType$Collection;
        public static final /* synthetic */ int[] $SwitchMap$com$google$protobuf$JavaType;

        static {
            int[] iArr = new int[JavaType.values().length];
            $SwitchMap$com$google$protobuf$JavaType = iArr;
            try {
                iArr[JavaType.BYTE_STRING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$google$protobuf$JavaType[JavaType.MESSAGE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$google$protobuf$JavaType[JavaType.STRING.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            int[] iArr2 = new int[Collection.values().length];
            $SwitchMap$com$google$protobuf$FieldType$Collection = iArr2;
            try {
                iArr2[Collection.MAP.ordinal()] = 1;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$google$protobuf$FieldType$Collection[Collection.VECTOR.ordinal()] = 2;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$google$protobuf$FieldType$Collection[Collection.SCALAR.ordinal()] = 3;
            } catch (NoSuchFieldError unused6) {
            }
        }
    }

    public int id() {
        return this.id;
    }

    public JavaType getJavaType() {
        return this.javaType;
    }

    public boolean isPacked() {
        return Collection.PACKED_VECTOR.equals(this.collection);
    }

    public boolean isPrimitiveScalar() {
        return this.primitiveScalar;
    }

    public boolean isScalar() {
        return this.collection == Collection.SCALAR;
    }

    public boolean isList() {
        return this.collection.isList();
    }

    public boolean isMap() {
        return this.collection == Collection.MAP;
    }

    public boolean isValidForField(java.lang.reflect.Field field) {
        if (Collection.VECTOR.equals(this.collection)) {
            return isValidForList(field);
        }
        return this.javaType.getType().isAssignableFrom(field.getType());
    }

    private boolean isValidForList(java.lang.reflect.Field field) {
        Class<?> type = field.getType();
        if (!this.javaType.getType().isAssignableFrom(type)) {
            return false;
        }
        java.lang.reflect.Type[] typeArr = EMPTY_TYPES;
        if (field.getGenericType() instanceof ParameterizedType) {
            typeArr = ((ParameterizedType) field.getGenericType()).getActualTypeArguments();
        }
        java.lang.reflect.Type listParameter = getListParameter(type, typeArr);
        if (listParameter instanceof Class) {
            return this.elementType.isAssignableFrom((Class) listParameter);
        }
        return true;
    }

    public static FieldType forId(int i) {
        if (i >= 0) {
            FieldType[] fieldTypeArr = VALUES;
            if (i < fieldTypeArr.length) {
                return fieldTypeArr[i];
            }
            return null;
        }
        return null;
    }

    private static java.lang.reflect.Type getGenericSuperList(Class<?> cls) {
        java.lang.reflect.Type[] genericInterfaces;
        for (java.lang.reflect.Type type : cls.getGenericInterfaces()) {
            if ((type instanceof ParameterizedType) && List.class.isAssignableFrom((Class) ((ParameterizedType) type).getRawType())) {
                return type;
            }
        }
        java.lang.reflect.Type genericSuperclass = cls.getGenericSuperclass();
        if (!(genericSuperclass instanceof ParameterizedType) || !List.class.isAssignableFrom((Class) ((ParameterizedType) genericSuperclass).getRawType())) {
            return null;
        }
        return genericSuperclass;
    }

    private static java.lang.reflect.Type getListParameter(Class<?> cls, java.lang.reflect.Type[] typeArr) {
        boolean z;
        while (true) {
            int i = 0;
            if (cls != List.class) {
                java.lang.reflect.Type genericSuperList = getGenericSuperList(cls);
                if (genericSuperList instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) genericSuperList;
                    java.lang.reflect.Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                    for (int i2 = 0; i2 < actualTypeArguments.length; i2++) {
                        java.lang.reflect.Type type = actualTypeArguments[i2];
                        if (type instanceof TypeVariable) {
                            TypeVariable<Class<?>>[] typeParameters = cls.getTypeParameters();
                            if (typeArr.length != typeParameters.length) {
                                throw new RuntimeException("Type array mismatch");
                            }
                            int i3 = 0;
                            while (true) {
                                if (i3 >= typeParameters.length) {
                                    z = false;
                                    break;
                                } else if (type == typeParameters[i3]) {
                                    actualTypeArguments[i2] = typeArr[i3];
                                    z = true;
                                    break;
                                } else {
                                    i3++;
                                }
                            }
                            if (!z) {
                                throw new RuntimeException("Unable to find replacement for " + type);
                            }
                        }
                    }
                    cls = (Class) parameterizedType.getRawType();
                    typeArr = actualTypeArguments;
                } else {
                    typeArr = EMPTY_TYPES;
                    Class<?>[] interfaces = cls.getInterfaces();
                    int length = interfaces.length;
                    while (true) {
                        if (i < length) {
                            Class<?> cls2 = interfaces[i];
                            if (List.class.isAssignableFrom(cls2)) {
                                cls = cls2;
                                break;
                            }
                            i++;
                        } else {
                            cls = cls.getSuperclass();
                            break;
                        }
                    }
                }
            } else if (typeArr.length != 1) {
                throw new RuntimeException("Unable to identify parameter type for List<T>");
            } else {
                return typeArr[0];
            }
        }
    }

    /* loaded from: classes.dex */
    public enum Collection {
        SCALAR(false),
        VECTOR(true),
        PACKED_VECTOR(true),
        MAP(false);
        
        private final boolean isList;

        Collection(boolean z) {
            this.isList = z;
        }

        public boolean isList() {
            return this.isList;
        }
    }
}
