package com.miui.gallery.glide.load;

import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import java.util.WeakHashMap;

/* loaded from: classes2.dex */
public class ExtraInfoManager {
    public ThreadLocal<WeakHashMap<Object, Options>> mThreadLocalMap = ThreadLocal.withInitial(ExtraInfoManager$$ExternalSyntheticLambda0.INSTANCE);

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final ExtraInfoManager sInstance = new ExtraInfoManager();
    }

    public static ExtraInfoManager getInstance() {
        return SingletonHolder.sInstance;
    }

    public <T> T get(Object obj, Option<T> option) {
        Options options = this.mThreadLocalMap.get().get(obj);
        if (options != null) {
            return (T) options.get(option);
        }
        return option.getDefaultValue();
    }

    public <T> Options set(Object obj, Option<T> option, T t) {
        Options options = this.mThreadLocalMap.get().get(obj);
        if (options == null) {
            options = new Options();
            this.mThreadLocalMap.get().put(obj, options);
        }
        return options.set(option, t);
    }

    public void remove(Object obj) {
        this.mThreadLocalMap.get().remove(obj);
    }
}
