package com.miui.gallery.util;

import android.text.TextUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/* loaded from: classes2.dex */
public class GeneralDataHolder {
    public Map<String, SoftReference<Object>> mData;

    public GeneralDataHolder() {
        this.mData = new ConcurrentHashMap();
    }

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final GeneralDataHolder instance = new GeneralDataHolder();
    }

    public static GeneralDataHolder getInstance() {
        return SingletonHolder.instance;
    }

    public void save(String str, Object obj) {
        this.mData.put(str, new SoftReference<>(obj));
    }

    public Object retrieve(String str) {
        SoftReference<Object> softReference = this.mData.get(str);
        if (softReference != null) {
            return softReference.get();
        }
        return null;
    }

    public Object remove(String str) {
        if (TextUtils.isEmpty(str)) {
            DefaultLogger.w("GeneralDataHolder", "try to remove from generaldatahold with an empty key");
            return null;
        }
        SoftReference<Object> remove = this.mData.remove(str);
        if (remove == null) {
            return null;
        }
        return remove.get();
    }
}
