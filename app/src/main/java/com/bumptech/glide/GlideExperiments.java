package com.bumptech.glide;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/* loaded from: classes.dex */
public class GlideExperiments {
    public final Map<Class<?>, Object> experiments;

    public GlideExperiments(Builder builder) {
        this.experiments = Collections.unmodifiableMap(new HashMap(builder.experiments));
    }

    public boolean isEnabled(Class<Object> cls) {
        return this.experiments.containsKey(cls);
    }

    /* loaded from: classes.dex */
    public static final class Builder {
        public final Map<Class<?>, Object> experiments = new HashMap();

        public GlideExperiments build() {
            return new GlideExperiments(this);
        }
    }
}
