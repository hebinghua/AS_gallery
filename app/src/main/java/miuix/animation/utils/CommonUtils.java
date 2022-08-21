package miuix.animation.utils;

import android.animation.ArgbEvaluator;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import miuix.animation.IAnimTarget;
import miuix.animation.property.FloatProperty;
import miuix.animation.property.ViewProperty;

/* loaded from: classes3.dex */
public class CommonUtils {
    public static float sTouchSlop;
    public static final ArgbEvaluator sArgbEvaluator = new ArgbEvaluator();
    public static final Class<?>[] BUILT_IN = {String.class, Integer.TYPE, Integer.class, Long.TYPE, Long.class, Short.TYPE, Short.class, Float.TYPE, Float.class, Double.TYPE, Double.class};

    /* loaded from: classes3.dex */
    public interface PreDrawTask {
        boolean call();
    }

    public static boolean hasFlags(long j, long j2) {
        return (j & j2) != 0;
    }

    /* loaded from: classes3.dex */
    public static class OnPreDrawTask implements ViewTreeObserver.OnPreDrawListener {
        public PreDrawTask mTask;
        public WeakReference<View> mView;

        public OnPreDrawTask(PreDrawTask preDrawTask) {
            this.mTask = preDrawTask;
        }

        public void start(View view) {
            ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
            this.mView = new WeakReference<>(view);
            viewTreeObserver.addOnPreDrawListener(this);
        }

        /* JADX WARN: Removed duplicated region for block: B:10:0x0017  */
        @Override // android.view.ViewTreeObserver.OnPreDrawListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public boolean onPreDraw() {
            /*
                r3 = this;
                java.lang.ref.WeakReference<android.view.View> r0 = r3.mView
                java.lang.Object r0 = r0.get()
                android.view.View r0 = (android.view.View) r0
                r1 = 1
                if (r0 == 0) goto L1f
                miuix.animation.utils.CommonUtils$PreDrawTask r2 = r3.mTask
                if (r2 == 0) goto L14
                boolean r2 = r2.call()     // Catch: java.lang.Exception -> L14
                goto L15
            L14:
                r2 = r1
            L15:
                if (r2 == 0) goto L20
                android.view.ViewTreeObserver r0 = r0.getViewTreeObserver()
                r0.removeOnPreDrawListener(r3)
                goto L20
            L1f:
                r2 = r1
            L20:
                if (r2 == 0) goto L25
                r0 = 0
                r3.mTask = r0
            L25:
                return r1
            */
            throw new UnsupportedOperationException("Method not decompiled: miuix.animation.utils.CommonUtils.OnPreDrawTask.onPreDraw():boolean");
        }
    }

    public static void runOnPreDraw(View view, PreDrawTask preDrawTask) {
        if (view == null) {
            return;
        }
        new OnPreDrawTask(preDrawTask).start(view);
    }

    public static <K, V> StringBuilder mapToString(Map<K, V> map, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        if (map != null && map.size() > 0) {
            for (Map.Entry<K, V> entry : map.entrySet()) {
                sb.append('\n');
                sb.append(str);
                sb.append(entry.getKey());
                sb.append('=');
                sb.append(entry.getValue());
            }
            sb.append('\n');
        }
        sb.append('}');
        return sb;
    }

    @SafeVarargs
    public static <T> T[] mergeArray(T[] tArr, T... tArr2) {
        if (tArr == null) {
            return tArr2;
        }
        if (tArr2 == null) {
            return tArr;
        }
        Object newInstance = Array.newInstance(tArr.getClass().getComponentType(), tArr.length + tArr2.length);
        System.arraycopy(tArr, 0, newInstance, 0, tArr.length);
        System.arraycopy(tArr2, 0, newInstance, tArr.length, tArr2.length);
        return (T[]) ((Object[]) newInstance);
    }

    public static <T> boolean isArrayEmpty(T[] tArr) {
        return tArr == null || tArr.length == 0;
    }

    public static <T> boolean inArray(T[] tArr, T t) {
        if (t != null && tArr != null && tArr.length > 0) {
            for (T t2 : tArr) {
                if (t2.equals(t)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static float getTouchSlop(View view) {
        if (sTouchSlop == 0.0f && view != null) {
            sTouchSlop = ViewConfiguration.get(view.getContext()).getScaledTouchSlop();
        }
        return sTouchSlop;
    }

    public static double getDistance(float f, float f2, float f3, float f4) {
        return Math.sqrt(Math.pow(f3 - f, 2.0d) + Math.pow(f4 - f2, 2.0d));
    }

    public static float getSize(IAnimTarget iAnimTarget, FloatProperty floatProperty) {
        if (floatProperty == ViewProperty.X) {
            floatProperty = ViewProperty.WIDTH;
        } else if (floatProperty == ViewProperty.Y) {
            floatProperty = ViewProperty.HEIGHT;
        } else if (floatProperty != ViewProperty.WIDTH && floatProperty != ViewProperty.HEIGHT) {
            floatProperty = null;
        }
        if (floatProperty == null) {
            return 0.0f;
        }
        return iAnimTarget.getValue(floatProperty);
    }

    public static boolean isBuiltInClass(Class<?> cls) {
        return inArray(BUILT_IN, cls);
    }

    public static <T> void addTo(Collection<T> collection, Collection<T> collection2) {
        for (T t : collection) {
            if (!collection2.contains(t)) {
                collection2.add(t);
            }
        }
    }

    public static String readProp(String str) {
        InputStreamReader inputStreamReader;
        Throwable th;
        BufferedReader bufferedReader;
        IOException e;
        try {
            inputStreamReader = new InputStreamReader(Runtime.getRuntime().exec("getprop " + str).getInputStream());
            try {
                bufferedReader = new BufferedReader(inputStreamReader);
                try {
                    try {
                        String readLine = bufferedReader.readLine();
                        closeQuietly(bufferedReader);
                        closeQuietly(inputStreamReader);
                        return readLine;
                    } catch (IOException e2) {
                        e = e2;
                        Log.i("miuix_anim", "readProp failed", e);
                        closeQuietly(bufferedReader);
                        closeQuietly(inputStreamReader);
                        return "";
                    }
                } catch (Throwable th2) {
                    th = th2;
                    closeQuietly(bufferedReader);
                    closeQuietly(inputStreamReader);
                    throw th;
                }
            } catch (IOException e3) {
                e = e3;
                bufferedReader = null;
            } catch (Throwable th3) {
                th = th3;
                bufferedReader = null;
                closeQuietly(bufferedReader);
                closeQuietly(inputStreamReader);
                throw th;
            }
        } catch (IOException e4) {
            inputStreamReader = null;
            e = e4;
            bufferedReader = null;
        } catch (Throwable th4) {
            inputStreamReader = null;
            th = th4;
            bufferedReader = null;
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                Log.w("miuix_anim", "close " + closeable + " failed", e);
            }
        }
    }

    public static <T> T getLocal(ThreadLocal<T> threadLocal, Class cls) {
        T t = threadLocal.get();
        if (t != null || cls == null) {
            return t;
        }
        T t2 = (T) ObjectPool.acquire(cls, new Object[0]);
        threadLocal.set(t2);
        return t2;
    }
}
