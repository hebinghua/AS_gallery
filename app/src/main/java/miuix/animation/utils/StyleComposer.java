package miuix.animation.utils;

import android.util.Log;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/* loaded from: classes3.dex */
public class StyleComposer {

    /* loaded from: classes3.dex */
    public interface IInterceptor<T> {
        Object onMethod(Method method, Object[] objArr, T... tArr);

        boolean shouldIntercept(Method method, Object[] objArr);
    }

    public static <T> T compose(final Class<T> cls, final IInterceptor iInterceptor, final T... tArr) {
        Object newProxyInstance = Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, new InvocationHandler() { // from class: miuix.animation.utils.StyleComposer.1
            @Override // java.lang.reflect.InvocationHandler
            public Object invoke(Object obj, Method method, Object[] objArr) {
                Object[] objArr2;
                Object obj2;
                Object[] objArr3;
                IInterceptor iInterceptor2 = IInterceptor.this;
                if (iInterceptor2 != null && iInterceptor2.shouldIntercept(method, objArr)) {
                    obj2 = IInterceptor.this.onMethod(method, objArr, tArr);
                } else {
                    Object obj3 = null;
                    for (Object obj4 : tArr) {
                        try {
                            obj3 = method.invoke(obj4, objArr);
                        } catch (Exception e) {
                            Log.w("StyleComposer", "failed to invoke " + method + " for " + obj4, e.getCause());
                        }
                    }
                    obj2 = obj3;
                }
                if (obj2 != null) {
                    if (obj2 == tArr[objArr3.length - 1]) {
                        return cls.cast(obj);
                    }
                }
                return obj2;
            }
        });
        if (cls.isInstance(newProxyInstance)) {
            return cls.cast(newProxyInstance);
        }
        return null;
    }
}
