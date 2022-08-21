package com.miui.gallery.base_optimization.support;

import android.text.TextUtils;
import java.util.Collection;
import java.util.Objects;

/* loaded from: classes.dex */
public class UtilsMethodSupportDelegate implements IUtilsMethodSupport {
    @Override // com.miui.gallery.base_optimization.support.IUtilsMethodSupport
    public boolean isEmpty(Object obj) {
        return obj == null;
    }

    public UtilsMethodSupportDelegate() {
    }

    public static UtilsMethodSupportDelegate getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* loaded from: classes.dex */
    public static class SingletonHolder {
        public static final UtilsMethodSupportDelegate INSTANCE = new UtilsMethodSupportDelegate();
    }

    public boolean isEmpty(Collection collection) {
        return collection == null || collection.isEmpty();
    }

    @Override // com.miui.gallery.base_optimization.support.IUtilsMethodSupport
    public boolean isEmpty(CharSequence charSequence) {
        return TextUtils.isEmpty(charSequence);
    }

    @Override // com.miui.gallery.base_optimization.support.IUtilsMethodSupport
    public boolean isEquals(Object obj, Object obj2) {
        return Objects.equals(obj, obj2);
    }

    public <T> boolean isEmpty(T... tArr) {
        return tArr == null || tArr.length == 0;
    }
}
