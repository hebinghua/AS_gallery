package com.google.gson_nex.internal;

import com.google.gson_nex.ExclusionStrategy;
import com.google.gson_nex.FieldAttributes;
import com.google.gson_nex.Gson;
import com.google.gson_nex.TypeAdapter;
import com.google.gson_nex.TypeAdapterFactory;
import com.google.gson_nex.annotations.Expose;
import com.google.gson_nex.annotations.Since;
import com.google.gson_nex.annotations.Until;
import com.google.gson_nex.reflect.TypeToken;
import com.google.gson_nex.stream.JsonReader;
import com.google.gson_nex.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/* loaded from: classes.dex */
public final class Excluder implements TypeAdapterFactory, Cloneable {
    public static final Excluder DEFAULT = new Excluder();
    private static final double IGNORE_VERSIONS = -1.0d;
    private boolean requireExpose;
    private double version = IGNORE_VERSIONS;
    private int modifiers = 136;
    private boolean serializeInnerClasses = true;
    private List<ExclusionStrategy> serializationStrategies = Collections.emptyList();
    private List<ExclusionStrategy> deserializationStrategies = Collections.emptyList();

    /* renamed from: clone */
    public Excluder m320clone() {
        try {
            return (Excluder) super.clone();
        } catch (CloneNotSupportedException unused) {
            throw new AssertionError();
        }
    }

    public Excluder withVersion(double d) {
        Excluder m320clone = m320clone();
        m320clone.version = d;
        return m320clone;
    }

    public Excluder withModifiers(int... iArr) {
        Excluder m320clone = m320clone();
        m320clone.modifiers = 0;
        for (int i : iArr) {
            m320clone.modifiers = i | m320clone.modifiers;
        }
        return m320clone;
    }

    public Excluder disableInnerClassSerialization() {
        Excluder m320clone = m320clone();
        m320clone.serializeInnerClasses = false;
        return m320clone;
    }

    public Excluder excludeFieldsWithoutExposeAnnotation() {
        Excluder m320clone = m320clone();
        m320clone.requireExpose = true;
        return m320clone;
    }

    public Excluder withExclusionStrategy(ExclusionStrategy exclusionStrategy, boolean z, boolean z2) {
        Excluder m320clone = m320clone();
        if (z) {
            ArrayList arrayList = new ArrayList(this.serializationStrategies);
            m320clone.serializationStrategies = arrayList;
            arrayList.add(exclusionStrategy);
        }
        if (z2) {
            ArrayList arrayList2 = new ArrayList(this.deserializationStrategies);
            m320clone.deserializationStrategies = arrayList2;
            arrayList2.add(exclusionStrategy);
        }
        return m320clone;
    }

    @Override // com.google.gson_nex.TypeAdapterFactory
    public <T> TypeAdapter<T> create(final Gson gson, final TypeToken<T> typeToken) {
        Class<? super T> rawType = typeToken.getRawType();
        final boolean excludeClass = excludeClass(rawType, true);
        final boolean excludeClass2 = excludeClass(rawType, false);
        if (excludeClass || excludeClass2) {
            return new TypeAdapter<T>() { // from class: com.google.gson_nex.internal.Excluder.1
                private TypeAdapter<T> delegate;

                /* JADX WARN: Type inference failed for: r2v1, types: [T, java.lang.Object] */
                @Override // com.google.gson_nex.TypeAdapter
                /* renamed from: read */
                public T mo352read(JsonReader jsonReader) throws IOException {
                    if (excludeClass2) {
                        jsonReader.skipValue();
                        return null;
                    }
                    return delegate().mo352read(jsonReader);
                }

                @Override // com.google.gson_nex.TypeAdapter
                public void write(JsonWriter jsonWriter, T t) throws IOException {
                    if (excludeClass) {
                        jsonWriter.nullValue();
                    } else {
                        delegate().write(jsonWriter, t);
                    }
                }

                private TypeAdapter<T> delegate() {
                    TypeAdapter<T> typeAdapter = this.delegate;
                    if (typeAdapter != 0) {
                        return typeAdapter;
                    }
                    TypeAdapter<T> delegateAdapter = gson.getDelegateAdapter(Excluder.this, typeToken);
                    this.delegate = delegateAdapter;
                    return delegateAdapter;
                }
            };
        }
        return null;
    }

    public boolean excludeField(Field field, boolean z) {
        Expose expose;
        if ((this.modifiers & field.getModifiers()) != 0) {
            return true;
        }
        if ((this.version != IGNORE_VERSIONS && !isValidVersion((Since) field.getAnnotation(Since.class), (Until) field.getAnnotation(Until.class))) || field.isSynthetic()) {
            return true;
        }
        if (this.requireExpose && ((expose = (Expose) field.getAnnotation(Expose.class)) == null || (!z ? !expose.deserialize() : !expose.serialize()))) {
            return true;
        }
        if ((!this.serializeInnerClasses && isInnerClass(field.getType())) || isAnonymousOrLocal(field.getType())) {
            return true;
        }
        List<ExclusionStrategy> list = z ? this.serializationStrategies : this.deserializationStrategies;
        if (list.isEmpty()) {
            return false;
        }
        FieldAttributes fieldAttributes = new FieldAttributes(field);
        for (ExclusionStrategy exclusionStrategy : list) {
            if (exclusionStrategy.shouldSkipField(fieldAttributes)) {
                return true;
            }
        }
        return false;
    }

    public boolean excludeClass(Class<?> cls, boolean z) {
        if (this.version == IGNORE_VERSIONS || isValidVersion((Since) cls.getAnnotation(Since.class), (Until) cls.getAnnotation(Until.class))) {
            if ((!this.serializeInnerClasses && isInnerClass(cls)) || isAnonymousOrLocal(cls)) {
                return true;
            }
            for (ExclusionStrategy exclusionStrategy : z ? this.serializationStrategies : this.deserializationStrategies) {
                if (exclusionStrategy.shouldSkipClass(cls)) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }

    private boolean isAnonymousOrLocal(Class<?> cls) {
        return !Enum.class.isAssignableFrom(cls) && (cls.isAnonymousClass() || cls.isLocalClass());
    }

    private boolean isInnerClass(Class<?> cls) {
        return cls.isMemberClass() && !isStatic(cls);
    }

    private boolean isStatic(Class<?> cls) {
        return (cls.getModifiers() & 8) != 0;
    }

    private boolean isValidVersion(Since since, Until until) {
        return isValidSince(since) && isValidUntil(until);
    }

    private boolean isValidSince(Since since) {
        return since == null || since.value() <= this.version;
    }

    private boolean isValidUntil(Until until) {
        return until == null || until.value() > this.version;
    }
}
