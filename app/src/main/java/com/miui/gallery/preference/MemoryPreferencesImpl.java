package com.miui.gallery.preference;

import android.content.SharedPreferences;
import android.os.Looper;
import com.miui.gallery.util.concurrent.ThreadManager;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/* loaded from: classes2.dex */
public class MemoryPreferencesImpl implements SharedPreferences {
    public static final Object CONTENT = new Object();
    public final Object mLock = new Object();
    public final WeakHashMap<SharedPreferences.OnSharedPreferenceChangeListener, Object> mListeners = new WeakHashMap<>();
    public Map<String, Object> mMap = new HashMap();

    @Override // android.content.SharedPreferences
    public Map<String, ?> getAll() {
        HashMap hashMap;
        synchronized (this.mLock) {
            hashMap = new HashMap(this.mMap);
        }
        return hashMap;
    }

    @Override // android.content.SharedPreferences
    public String getString(String str, String str2) {
        synchronized (this.mLock) {
            String str3 = (String) this.mMap.get(str);
            if (str3 != null) {
                str2 = str3;
            }
        }
        return str2;
    }

    @Override // android.content.SharedPreferences
    public Set<String> getStringSet(String str, Set<String> set) {
        synchronized (this.mLock) {
            Set<String> set2 = (Set) this.mMap.get(str);
            if (set2 != null) {
                set = set2;
            }
        }
        return set;
    }

    @Override // android.content.SharedPreferences
    public int getInt(String str, int i) {
        synchronized (this.mLock) {
            Integer num = (Integer) this.mMap.get(str);
            if (num != null) {
                i = num.intValue();
            }
        }
        return i;
    }

    @Override // android.content.SharedPreferences
    public long getLong(String str, long j) {
        synchronized (this.mLock) {
            Long l = (Long) this.mMap.get(str);
            if (l != null) {
                j = l.longValue();
            }
        }
        return j;
    }

    @Override // android.content.SharedPreferences
    public float getFloat(String str, float f) {
        synchronized (this.mLock) {
            Float f2 = (Float) this.mMap.get(str);
            if (f2 != null) {
                f = f2.floatValue();
            }
        }
        return f;
    }

    @Override // android.content.SharedPreferences
    public boolean getBoolean(String str, boolean z) {
        synchronized (this.mLock) {
            Boolean bool = (Boolean) this.mMap.get(str);
            if (bool != null) {
                z = bool.booleanValue();
            }
        }
        return z;
    }

    @Override // android.content.SharedPreferences
    public boolean contains(String str) {
        boolean containsKey;
        synchronized (this.mLock) {
            containsKey = this.mMap.containsKey(str);
        }
        return containsKey;
    }

    @Override // android.content.SharedPreferences
    public SharedPreferences.Editor edit() {
        return new EditorImpl();
    }

    @Override // android.content.SharedPreferences
    public void registerOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        synchronized (this.mLock) {
            this.mListeners.put(onSharedPreferenceChangeListener, CONTENT);
        }
    }

    @Override // android.content.SharedPreferences
    public void unregisterOnSharedPreferenceChangeListener(SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener) {
        synchronized (this.mLock) {
            this.mListeners.remove(onSharedPreferenceChangeListener);
        }
    }

    /* loaded from: classes2.dex */
    public final class EditorImpl implements SharedPreferences.Editor {
        public final Object mEditLock = new Object();
        public final Map<String, Object> mModified = new HashMap();
        public boolean mClear = false;

        public EditorImpl() {
        }

        @Override // android.content.SharedPreferences.Editor
        public SharedPreferences.Editor putString(String str, String str2) {
            synchronized (this.mEditLock) {
                this.mModified.put(str, str2);
            }
            return this;
        }

        @Override // android.content.SharedPreferences.Editor
        public SharedPreferences.Editor putStringSet(String str, Set<String> set) {
            synchronized (this.mEditLock) {
                this.mModified.put(str, set == null ? null : new HashSet(set));
            }
            return this;
        }

        @Override // android.content.SharedPreferences.Editor
        public SharedPreferences.Editor putInt(String str, int i) {
            synchronized (this.mEditLock) {
                this.mModified.put(str, Integer.valueOf(i));
            }
            return this;
        }

        @Override // android.content.SharedPreferences.Editor
        public SharedPreferences.Editor putLong(String str, long j) {
            synchronized (this.mEditLock) {
                this.mModified.put(str, Long.valueOf(j));
            }
            return this;
        }

        @Override // android.content.SharedPreferences.Editor
        public SharedPreferences.Editor putFloat(String str, float f) {
            synchronized (this.mEditLock) {
                this.mModified.put(str, Float.valueOf(f));
            }
            return this;
        }

        @Override // android.content.SharedPreferences.Editor
        public SharedPreferences.Editor putBoolean(String str, boolean z) {
            synchronized (this.mEditLock) {
                this.mModified.put(str, Boolean.valueOf(z));
            }
            return this;
        }

        @Override // android.content.SharedPreferences.Editor
        public SharedPreferences.Editor remove(String str) {
            synchronized (this.mEditLock) {
                this.mModified.put(str, this);
            }
            return this;
        }

        @Override // android.content.SharedPreferences.Editor
        public SharedPreferences.Editor clear() {
            synchronized (this.mEditLock) {
                this.mClear = true;
            }
            return this;
        }

        @Override // android.content.SharedPreferences.Editor
        public boolean commit() {
            commitToMemory();
            return true;
        }

        @Override // android.content.SharedPreferences.Editor
        public void apply() {
            commitToMemory();
        }

        /* JADX WARN: Removed duplicated region for block: B:62:0x00b4 A[SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:67:0x0058 A[SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public final void commitToMemory() {
            /*
                r9 = this;
                com.miui.gallery.preference.MemoryPreferencesImpl r0 = com.miui.gallery.preference.MemoryPreferencesImpl.this
                java.util.WeakHashMap r0 = com.miui.gallery.preference.MemoryPreferencesImpl.access$000(r0)
                int r0 = r0.size()
                r1 = 0
                if (r0 <= 0) goto Lf
                r0 = 1
                goto L10
            Lf:
                r0 = r1
            L10:
                r2 = 0
                if (r0 == 0) goto L28
                java.util.ArrayList r2 = new java.util.ArrayList
                r2.<init>()
                java.util.HashSet r3 = new java.util.HashSet
                com.miui.gallery.preference.MemoryPreferencesImpl r4 = com.miui.gallery.preference.MemoryPreferencesImpl.this
                java.util.WeakHashMap r4 = com.miui.gallery.preference.MemoryPreferencesImpl.access$000(r4)
                java.util.Set r4 = r4.keySet()
                r3.<init>(r4)
                goto L29
            L28:
                r3 = r2
            L29:
                com.miui.gallery.preference.MemoryPreferencesImpl r4 = com.miui.gallery.preference.MemoryPreferencesImpl.this
                java.lang.Object r4 = com.miui.gallery.preference.MemoryPreferencesImpl.access$100(r4)
                monitor-enter(r4)
                java.lang.Object r5 = r9.mEditLock     // Catch: java.lang.Throwable -> Lc6
                monitor-enter(r5)     // Catch: java.lang.Throwable -> Lc6
                boolean r6 = r9.mClear     // Catch: java.lang.Throwable -> Lc3
                if (r6 == 0) goto L4e
                com.miui.gallery.preference.MemoryPreferencesImpl r6 = com.miui.gallery.preference.MemoryPreferencesImpl.this     // Catch: java.lang.Throwable -> Lc3
                java.util.Map r6 = com.miui.gallery.preference.MemoryPreferencesImpl.access$200(r6)     // Catch: java.lang.Throwable -> Lc3
                boolean r6 = r6.isEmpty()     // Catch: java.lang.Throwable -> Lc3
                if (r6 != 0) goto L4c
                com.miui.gallery.preference.MemoryPreferencesImpl r6 = com.miui.gallery.preference.MemoryPreferencesImpl.this     // Catch: java.lang.Throwable -> Lc3
                java.util.Map r6 = com.miui.gallery.preference.MemoryPreferencesImpl.access$200(r6)     // Catch: java.lang.Throwable -> Lc3
                r6.clear()     // Catch: java.lang.Throwable -> Lc3
            L4c:
                r9.mClear = r1     // Catch: java.lang.Throwable -> Lc3
            L4e:
                java.util.Map<java.lang.String, java.lang.Object> r1 = r9.mModified     // Catch: java.lang.Throwable -> Lc3
                java.util.Set r1 = r1.entrySet()     // Catch: java.lang.Throwable -> Lc3
                java.util.Iterator r1 = r1.iterator()     // Catch: java.lang.Throwable -> Lc3
            L58:
                boolean r6 = r1.hasNext()     // Catch: java.lang.Throwable -> Lc3
                if (r6 == 0) goto Lb8
                java.lang.Object r6 = r1.next()     // Catch: java.lang.Throwable -> Lc3
                java.util.Map$Entry r6 = (java.util.Map.Entry) r6     // Catch: java.lang.Throwable -> Lc3
                java.lang.Object r7 = r6.getKey()     // Catch: java.lang.Throwable -> Lc3
                java.lang.String r7 = (java.lang.String) r7     // Catch: java.lang.Throwable -> Lc3
                java.lang.Object r6 = r6.getValue()     // Catch: java.lang.Throwable -> Lc3
                if (r6 == r9) goto L9c
                if (r6 != 0) goto L73
                goto L9c
            L73:
                com.miui.gallery.preference.MemoryPreferencesImpl r8 = com.miui.gallery.preference.MemoryPreferencesImpl.this     // Catch: java.lang.Throwable -> Lc3
                java.util.Map r8 = com.miui.gallery.preference.MemoryPreferencesImpl.access$200(r8)     // Catch: java.lang.Throwable -> Lc3
                boolean r8 = r8.containsKey(r7)     // Catch: java.lang.Throwable -> Lc3
                if (r8 == 0) goto L92
                com.miui.gallery.preference.MemoryPreferencesImpl r8 = com.miui.gallery.preference.MemoryPreferencesImpl.this     // Catch: java.lang.Throwable -> Lc3
                java.util.Map r8 = com.miui.gallery.preference.MemoryPreferencesImpl.access$200(r8)     // Catch: java.lang.Throwable -> Lc3
                java.lang.Object r8 = r8.get(r7)     // Catch: java.lang.Throwable -> Lc3
                if (r8 == 0) goto L92
                boolean r8 = r8.equals(r6)     // Catch: java.lang.Throwable -> Lc3
                if (r8 == 0) goto L92
                goto L58
            L92:
                com.miui.gallery.preference.MemoryPreferencesImpl r8 = com.miui.gallery.preference.MemoryPreferencesImpl.this     // Catch: java.lang.Throwable -> Lc3
                java.util.Map r8 = com.miui.gallery.preference.MemoryPreferencesImpl.access$200(r8)     // Catch: java.lang.Throwable -> Lc3
                r8.put(r7, r6)     // Catch: java.lang.Throwable -> Lc3
                goto Lb2
            L9c:
                com.miui.gallery.preference.MemoryPreferencesImpl r6 = com.miui.gallery.preference.MemoryPreferencesImpl.this     // Catch: java.lang.Throwable -> Lc3
                java.util.Map r6 = com.miui.gallery.preference.MemoryPreferencesImpl.access$200(r6)     // Catch: java.lang.Throwable -> Lc3
                boolean r6 = r6.containsKey(r7)     // Catch: java.lang.Throwable -> Lc3
                if (r6 != 0) goto La9
                goto L58
            La9:
                com.miui.gallery.preference.MemoryPreferencesImpl r6 = com.miui.gallery.preference.MemoryPreferencesImpl.this     // Catch: java.lang.Throwable -> Lc3
                java.util.Map r6 = com.miui.gallery.preference.MemoryPreferencesImpl.access$200(r6)     // Catch: java.lang.Throwable -> Lc3
                r6.remove(r7)     // Catch: java.lang.Throwable -> Lc3
            Lb2:
                if (r0 == 0) goto L58
                r2.add(r7)     // Catch: java.lang.Throwable -> Lc3
                goto L58
            Lb8:
                java.util.Map<java.lang.String, java.lang.Object> r0 = r9.mModified     // Catch: java.lang.Throwable -> Lc3
                r0.clear()     // Catch: java.lang.Throwable -> Lc3
                monitor-exit(r5)     // Catch: java.lang.Throwable -> Lc3
                monitor-exit(r4)     // Catch: java.lang.Throwable -> Lc6
                r9.notifyListeners(r3, r2)
                return
            Lc3:
                r0 = move-exception
                monitor-exit(r5)     // Catch: java.lang.Throwable -> Lc3
                throw r0     // Catch: java.lang.Throwable -> Lc6
            Lc6:
                r0 = move-exception
                monitor-exit(r4)     // Catch: java.lang.Throwable -> Lc6
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.preference.MemoryPreferencesImpl.EditorImpl.commitToMemory():void");
        }

        public final void notifyListeners(final Set<SharedPreferences.OnSharedPreferenceChangeListener> set, final List<String> list) {
            if (set == null || list == null || list.size() == 0) {
                return;
            }
            Runnable runnable = new Runnable() { // from class: com.miui.gallery.preference.MemoryPreferencesImpl.EditorImpl.1
                @Override // java.lang.Runnable
                public void run() {
                    for (int size = list.size() - 1; size >= 0; size--) {
                        String str = (String) list.get(size);
                        for (SharedPreferences.OnSharedPreferenceChangeListener onSharedPreferenceChangeListener : set) {
                            if (onSharedPreferenceChangeListener != null) {
                                onSharedPreferenceChangeListener.onSharedPreferenceChanged(MemoryPreferencesImpl.this, str);
                            }
                        }
                    }
                }
            };
            if (Looper.myLooper() == Looper.getMainLooper()) {
                runnable.run();
            } else {
                ThreadManager.getMainHandler().post(runnable);
            }
        }
    }
}
