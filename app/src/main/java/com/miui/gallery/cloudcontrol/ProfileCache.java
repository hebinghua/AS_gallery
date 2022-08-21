package com.miui.gallery.cloudcontrol;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import androidx.core.util.Pair;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.miui.gallery.R;
import com.miui.gallery.cloudcontrol.FeatureProfile;
import com.miui.gallery.cloudcontrol.observers.FeatureStatusObserver;
import com.miui.gallery.cloudcontrol.observers.FeatureStrategyObserver;
import com.miui.gallery.cloudcontrol.strategies.BaseStrategy;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.subjects.PublishSubject;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/* loaded from: classes.dex */
public class ProfileCache {
    public volatile boolean mIsLoadFinished;
    public volatile boolean mPendingNotify;
    public final Object mSyncLock = new Object();
    public HashMap<String, FeatureProfile> mCloudCache = new HashMap<>();
    public HashMap<String, Object> mCloudStrategyCache = new HashMap<>();
    public HashMap<String, FeatureProfile> mLocalCache = new HashMap<>();
    public HashMap<String, Object> mLocalStrategyCache = new HashMap<>();
    public final PublishSubject<Pair<String, FeatureProfile.Status>> mStatusSubject = PublishSubject.create();
    public final PublishSubject<Pair<String, String>> mStrategySubject = PublishSubject.create();

    /* renamed from: $r8$lambda$-ESSn9NKkc3IFBScw0IAKAlotZI */
    public static /* synthetic */ Object m715$r8$lambda$ESSn9NKkc3IFBScw0IAKAlotZI(ProfileCache profileCache, long j, Cursor cursor) {
        return profileCache.lambda$loadFromDB$4(j, cursor);
    }

    public static /* synthetic */ boolean $r8$lambda$PUJdH4JZctFvpqygUNxkhFxfDVY(String str, Pair pair) {
        return lambda$registerStatusObserver$0(str, pair);
    }

    public static /* synthetic */ boolean $r8$lambda$SEdAvO5evoq3CqjgmdZaTlWBpE8(String str, Pair pair) {
        return lambda$registerStrategyObserver$1(str, pair);
    }

    public static /* synthetic */ Pair $r8$lambda$TFsVq746y8Ci91waWO1krCFHxYw(ProfileCache profileCache, Class cls, Merger merger, Pair pair) {
        return profileCache.lambda$registerStrategyObserver$3(cls, merger, pair);
    }

    public static /* synthetic */ Pair $r8$lambda$_tIP687tMLW0cNjmkXZbp84qCqI(Class cls, Pair pair) {
        return lambda$registerStrategyObserver$2(cls, pair);
    }

    public void insertToCloudCache(FeatureProfile featureProfile) {
        String name = featureProfile.getName();
        String status = featureProfile.getStatus();
        String strategy = featureProfile.getStrategy();
        FeatureProfile featureProfile2 = this.mCloudCache.get(name);
        synchronized (this.mSyncLock) {
            if (FeatureProfile.Status.REMOVE.getValue().equals(status)) {
                if (featureProfile2 != null) {
                    this.mCloudCache.remove(name);
                    this.mCloudStrategyCache.remove(name);
                }
                notifyStatusChanged(name, status);
            } else if (featureProfile2 != null) {
                if (featureProfile2.getStatus() != null) {
                    String status2 = featureProfile2.getStatus();
                    FeatureProfile.Status status3 = FeatureProfile.Status.ENABLE;
                    if (status2.equals(status3.getValue())) {
                        status = status3.getValue();
                    }
                }
                if (!TextUtils.equals(featureProfile2.getStrategy(), strategy)) {
                    String strategy2 = featureProfile2.getStrategy();
                    featureProfile2.setStrategy(strategy);
                    this.mCloudStrategyCache.remove(name);
                    notifyStrategyChanged(name, strategy2);
                }
                if (!TextUtils.equals(featureProfile2.getStatus(), status)) {
                    featureProfile2.setStatus(status);
                    notifyStatusChanged(name, status);
                }
            } else {
                FeatureProfile featureProfile3 = new FeatureProfile();
                featureProfile3.setStrategy(strategy);
                featureProfile3.setStatus(status);
                featureProfile3.setName(name);
                this.mCloudCache.put(name, featureProfile3);
                notifyStatusChanged(name, status);
                notifyStrategyChanged(name, null);
            }
        }
    }

    public final void notifyStrategyChanged(String str, String str2) {
        this.mStrategySubject.onNext(new Pair<>(str, str2));
    }

    public final void notifyStatusChanged(String str, String str2) {
        this.mStatusSubject.onNext(new Pair<>(str, FeatureProfile.Status.fromValue(str2)));
    }

    public FeatureProfile.Status queryStatus(String str) {
        if (!TextUtils.isEmpty(str)) {
            synchronized (this.mSyncLock) {
                if (this.mIsLoadFinished) {
                    String str2 = null;
                    if (this.mCloudCache.get(str) != null) {
                        str2 = this.mCloudCache.get(str).getStatus();
                    } else if (this.mLocalCache.get(str) != null) {
                        str2 = this.mLocalCache.get(str).getStatus();
                    }
                    if (TextUtils.isEmpty(str2)) {
                        return FeatureProfile.Status.UNAVAILABLE;
                    }
                    try {
                        return FeatureProfile.Status.fromValue(str2);
                    } catch (Exception unused) {
                        return FeatureProfile.Status.UNAVAILABLE;
                    }
                }
                this.mPendingNotify = true;
                return FeatureProfile.Status.UNAVAILABLE;
            }
        }
        return FeatureProfile.Status.UNAVAILABLE;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:106:0x007d A[Catch: all -> 0x00c8, TRY_LEAVE, TryCatch #3 {, blocks: (B:78:0x000b, B:80:0x000f, B:82:0x0017, B:84:0x0021, B:99:0x005f, B:101:0x0067, B:103:0x0071, B:119:0x00b5, B:120:0x00bb, B:125:0x00c1, B:104:0x0075, B:106:0x007d, B:107:0x0089, B:109:0x0096, B:115:0x00a5, B:85:0x0024, B:87:0x002c, B:88:0x0038, B:90:0x0045, B:96:0x0052, B:127:0x00c3, B:128:0x00c6), top: B:139:0x000b }] */
    /* JADX WARN: Removed duplicated region for block: B:117:0x00b1 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:123:0x00bf  */
    /* JADX WARN: Removed duplicated region for block: B:124:0x00c0  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public <T extends com.miui.gallery.cloudcontrol.strategies.BaseStrategy> T queryStrategy(java.lang.String r8, java.lang.Class<T> r9, com.miui.gallery.cloudcontrol.Merger<T> r10) {
        /*
            r7 = this;
            boolean r0 = android.text.TextUtils.isEmpty(r8)
            r1 = 0
            if (r0 == 0) goto L8
            return r1
        L8:
            java.lang.Object r0 = r7.mSyncLock
            monitor-enter(r0)
            boolean r2 = r7.mIsLoadFinished     // Catch: java.lang.Throwable -> Lc8
            if (r2 == 0) goto Lc3
            java.util.HashMap<java.lang.String, java.lang.Object> r2 = r7.mCloudStrategyCache     // Catch: java.lang.Throwable -> Lc8
            java.lang.Object r2 = r2.get(r8)     // Catch: java.lang.Throwable -> Lc8
            if (r2 == 0) goto L24
            java.lang.Class r3 = r2.getClass()     // Catch: java.lang.Throwable -> Lc8
            boolean r3 = r9.isAssignableFrom(r3)     // Catch: java.lang.Throwable -> Lc8
            if (r3 == 0) goto L24
            com.miui.gallery.cloudcontrol.strategies.BaseStrategy r2 = (com.miui.gallery.cloudcontrol.strategies.BaseStrategy) r2     // Catch: java.lang.Throwable -> Lc8
            goto L5f
        L24:
            java.util.HashMap<java.lang.String, com.miui.gallery.cloudcontrol.FeatureProfile> r2 = r7.mCloudCache     // Catch: java.lang.Throwable -> Lc8
            java.lang.Object r2 = r2.get(r8)     // Catch: java.lang.Throwable -> Lc8
            if (r2 == 0) goto L5e
            java.util.HashMap<java.lang.String, com.miui.gallery.cloudcontrol.FeatureProfile> r2 = r7.mCloudCache     // Catch: java.lang.Throwable -> Lc8
            java.lang.Object r2 = r2.get(r8)     // Catch: java.lang.Throwable -> Lc8
            com.miui.gallery.cloudcontrol.FeatureProfile r2 = (com.miui.gallery.cloudcontrol.FeatureProfile) r2     // Catch: java.lang.Throwable -> Lc8
            java.lang.String r2 = r2.getStrategy()     // Catch: java.lang.Throwable -> Lc8
            com.google.gson.Gson r3 = new com.google.gson.Gson     // Catch: java.lang.Exception -> L50 java.lang.Throwable -> Lc8
            r3.<init>()     // Catch: java.lang.Exception -> L50 java.lang.Throwable -> Lc8
            java.lang.Object r3 = r3.fromJson(r2, r9)     // Catch: java.lang.Exception -> L50 java.lang.Throwable -> Lc8
            com.miui.gallery.cloudcontrol.strategies.BaseStrategy r3 = (com.miui.gallery.cloudcontrol.strategies.BaseStrategy) r3     // Catch: java.lang.Exception -> L50 java.lang.Throwable -> Lc8
            if (r3 == 0) goto L5c
            r3.doAdditionalProcessing()     // Catch: java.lang.Exception -> L4e java.lang.Throwable -> Lc8
            java.util.HashMap<java.lang.String, java.lang.Object> r4 = r7.mCloudStrategyCache     // Catch: java.lang.Exception -> L4e java.lang.Throwable -> Lc8
            r4.put(r8, r3)     // Catch: java.lang.Exception -> L4e java.lang.Throwable -> Lc8
            goto L5c
        L4e:
            r4 = move-exception
            goto L52
        L50:
            r4 = move-exception
            r3 = r1
        L52:
            java.lang.String r5 = "CloudControl.ProfileCache"
            java.lang.String r6 = "Failed to deserialize strategy: %s"
            com.miui.gallery.util.logger.DefaultLogger.e(r5, r6, r2)     // Catch: java.lang.Throwable -> Lc8
            r4.printStackTrace()     // Catch: java.lang.Throwable -> Lc8
        L5c:
            r2 = r3
            goto L5f
        L5e:
            r2 = r1
        L5f:
            java.util.HashMap<java.lang.String, java.lang.Object> r3 = r7.mLocalStrategyCache     // Catch: java.lang.Throwable -> Lc8
            java.lang.Object r3 = r3.get(r8)     // Catch: java.lang.Throwable -> Lc8
            if (r3 == 0) goto L75
            java.lang.Class r4 = r3.getClass()     // Catch: java.lang.Throwable -> Lc8
            boolean r4 = r9.isAssignableFrom(r4)     // Catch: java.lang.Throwable -> Lc8
            if (r4 == 0) goto L75
            r1 = r3
            com.miui.gallery.cloudcontrol.strategies.BaseStrategy r1 = (com.miui.gallery.cloudcontrol.strategies.BaseStrategy) r1     // Catch: java.lang.Throwable -> Lc8
            goto Laf
        L75:
            java.util.HashMap<java.lang.String, com.miui.gallery.cloudcontrol.FeatureProfile> r3 = r7.mLocalCache     // Catch: java.lang.Throwable -> Lc8
            java.lang.Object r3 = r3.get(r8)     // Catch: java.lang.Throwable -> Lc8
            if (r3 == 0) goto Laf
            java.util.HashMap<java.lang.String, com.miui.gallery.cloudcontrol.FeatureProfile> r3 = r7.mLocalCache     // Catch: java.lang.Throwable -> Lc8
            java.lang.Object r3 = r3.get(r8)     // Catch: java.lang.Throwable -> Lc8
            com.miui.gallery.cloudcontrol.FeatureProfile r3 = (com.miui.gallery.cloudcontrol.FeatureProfile) r3     // Catch: java.lang.Throwable -> Lc8
            java.lang.String r3 = r3.getStrategy()     // Catch: java.lang.Throwable -> Lc8
            com.google.gson.Gson r4 = new com.google.gson.Gson     // Catch: java.lang.Exception -> La4 java.lang.Throwable -> Lc8
            r4.<init>()     // Catch: java.lang.Exception -> La4 java.lang.Throwable -> Lc8
            java.lang.Object r9 = r4.fromJson(r3, r9)     // Catch: java.lang.Exception -> La4 java.lang.Throwable -> Lc8
            com.miui.gallery.cloudcontrol.strategies.BaseStrategy r9 = (com.miui.gallery.cloudcontrol.strategies.BaseStrategy) r9     // Catch: java.lang.Exception -> La4 java.lang.Throwable -> Lc8
            if (r9 == 0) goto La2
            r9.doAdditionalProcessing()     // Catch: java.lang.Exception -> L9f java.lang.Throwable -> Lc8
            java.util.HashMap<java.lang.String, java.lang.Object> r1 = r7.mLocalStrategyCache     // Catch: java.lang.Exception -> L9f java.lang.Throwable -> Lc8
            r1.put(r8, r9)     // Catch: java.lang.Exception -> L9f java.lang.Throwable -> Lc8
            goto La2
        L9f:
            r8 = move-exception
            r1 = r9
            goto La5
        La2:
            r1 = r9
            goto Laf
        La4:
            r8 = move-exception
        La5:
            java.lang.String r9 = "CloudControl.ProfileCache"
            java.lang.String r4 = "Failed to deserialize strategy: %s"
            com.miui.gallery.util.logger.DefaultLogger.e(r9, r4, r3)     // Catch: java.lang.Throwable -> Lc8
            r8.printStackTrace()     // Catch: java.lang.Throwable -> Lc8
        Laf:
            if (r10 == 0) goto Lbd
            if (r2 == 0) goto Lbd
            if (r1 == 0) goto Lbd
            java.lang.Object r8 = r10.merge(r1, r2)     // Catch: java.lang.Throwable -> Lc8
            com.miui.gallery.cloudcontrol.strategies.BaseStrategy r8 = (com.miui.gallery.cloudcontrol.strategies.BaseStrategy) r8     // Catch: java.lang.Throwable -> Lc8
            monitor-exit(r0)     // Catch: java.lang.Throwable -> Lc8
            return r8
        Lbd:
            if (r2 == 0) goto Lc0
            goto Lc1
        Lc0:
            r2 = r1
        Lc1:
            monitor-exit(r0)     // Catch: java.lang.Throwable -> Lc8
            return r2
        Lc3:
            r8 = 1
            r7.mPendingNotify = r8     // Catch: java.lang.Throwable -> Lc8
            monitor-exit(r0)     // Catch: java.lang.Throwable -> Lc8
            return r1
        Lc8:
            r8 = move-exception
            monitor-exit(r0)     // Catch: java.lang.Throwable -> Lc8
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.cloudcontrol.ProfileCache.queryStrategy(java.lang.String, java.lang.Class, com.miui.gallery.cloudcontrol.Merger):com.miui.gallery.cloudcontrol.strategies.BaseStrategy");
    }

    public FeatureProfile.Status registerStatusObserver(final String str, FeatureStatusObserver featureStatusObserver) {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("feature should not be empty or null.");
        }
        if (featureStatusObserver != null) {
            this.mStatusSubject.filter(new Predicate() { // from class: com.miui.gallery.cloudcontrol.ProfileCache$$ExternalSyntheticLambda3
                @Override // io.reactivex.functions.Predicate
                public final boolean test(Object obj) {
                    return ProfileCache.$r8$lambda$PUJdH4JZctFvpqygUNxkhFxfDVY(str, (Pair) obj);
                }
            }).observeOn(AndroidSchedulers.mainThread()).subscribe(featureStatusObserver);
        }
        return queryStatus(str);
    }

    public static /* synthetic */ boolean lambda$registerStatusObserver$0(String str, Pair pair) throws Exception {
        return !TextUtils.isEmpty((CharSequence) pair.first) && ((String) pair.first).equals(str);
    }

    public void unregisterStatusObserver(FeatureStatusObserver featureStatusObserver) {
        if (!featureStatusObserver.isDisposed()) {
            featureStatusObserver.dispose();
        }
    }

    public <T extends BaseStrategy> T registerStrategyObserver(final String str, final Class<T> cls, final Merger<T> merger, FeatureStrategyObserver<T> featureStrategyObserver) {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("feature should not be empty or null.");
        }
        if (featureStrategyObserver != null) {
            this.mStrategySubject.filter(new Predicate() { // from class: com.miui.gallery.cloudcontrol.ProfileCache$$ExternalSyntheticLambda4
                @Override // io.reactivex.functions.Predicate
                public final boolean test(Object obj) {
                    return ProfileCache.$r8$lambda$SEdAvO5evoq3CqjgmdZaTlWBpE8(str, (Pair) obj);
                }
            }).map(new Function() { // from class: com.miui.gallery.cloudcontrol.ProfileCache$$ExternalSyntheticLambda2
                @Override // io.reactivex.functions.Function
                /* renamed from: apply */
                public final Object mo2564apply(Object obj) {
                    return ProfileCache.$r8$lambda$_tIP687tMLW0cNjmkXZbp84qCqI(cls, (Pair) obj);
                }
            }).map(new Function() { // from class: com.miui.gallery.cloudcontrol.ProfileCache$$ExternalSyntheticLambda1
                @Override // io.reactivex.functions.Function
                /* renamed from: apply */
                public final Object mo2564apply(Object obj) {
                    return ProfileCache.$r8$lambda$TFsVq746y8Ci91waWO1krCFHxYw(ProfileCache.this, cls, merger, (Pair) obj);
                }
            }).observeOn(AndroidSchedulers.mainThread()).subscribe(featureStrategyObserver);
        }
        return (T) queryStrategy(str, cls, merger);
    }

    public static /* synthetic */ boolean lambda$registerStrategyObserver$1(String str, Pair pair) throws Exception {
        return TextUtils.equals((CharSequence) pair.first, str);
    }

    public static /* synthetic */ Pair lambda$registerStrategyObserver$2(Class cls, Pair pair) throws Exception {
        BaseStrategy baseStrategy;
        if (!TextUtils.isEmpty((CharSequence) pair.second)) {
            try {
                baseStrategy = (BaseStrategy) new Gson().fromJson((String) pair.second, (Class<Object>) cls);
            } catch (JsonSyntaxException e) {
                DefaultLogger.w("CloudControl.ProfileCache", e);
            }
            return new Pair((String) pair.first, baseStrategy);
        }
        baseStrategy = null;
        return new Pair((String) pair.first, baseStrategy);
    }

    public /* synthetic */ Pair lambda$registerStrategyObserver$3(Class cls, Merger merger, Pair pair) throws Exception {
        F f = pair.first;
        return new Pair((String) f, new Pair((BaseStrategy) pair.second, queryStrategy((String) f, cls, merger)));
    }

    public void unregisterStrategyObserver(FeatureStrategyObserver featureStrategyObserver) {
        if (!featureStrategyObserver.isDisposed()) {
            featureStrategyObserver.dispose();
        }
    }

    public void load(Context context) {
        loadFromDB(context);
        loadFromLocalFile(new InputStreamReader(context.getResources().openRawResource(R.raw.cloud_control)));
        this.mIsLoadFinished = true;
    }

    public void notifyAfterLoadFinished() {
        synchronized (this.mSyncLock) {
            if (this.mPendingNotify) {
                DefaultLogger.d("CloudControl.ProfileCache", "Notify all feature status after cache load finished:");
                for (Map.Entry<String, FeatureProfile> entry : this.mCloudCache.entrySet()) {
                    if (!TextUtils.isEmpty(entry.getKey()) && entry.getValue() != null && entry.getValue().getStatus() != null) {
                        DefaultLogger.d("CloudControl.ProfileCache", "Feature name: %s, feature status: %s", entry.getKey(), String.valueOf(entry.getValue().getStatus()));
                        notifyStatusChanged(entry.getKey(), entry.getValue().getStatus());
                        notifyStrategyChanged(entry.getKey(), null);
                    }
                }
                this.mPendingNotify = false;
            }
        }
    }

    public final void loadFromDB(Context context) {
        final long currentTimeMillis = System.currentTimeMillis();
        SafeDBUtil.safeQuery(context, GalleryContract.CloudControl.URI, FeatureDBItem.PROJECTION, (String) null, (String[]) null, (String) null, new SafeDBUtil.QueryHandler() { // from class: com.miui.gallery.cloudcontrol.ProfileCache$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public final Object mo1808handle(Cursor cursor) {
                return ProfileCache.m715$r8$lambda$ESSn9NKkc3IFBScw0IAKAlotZI(ProfileCache.this, currentTimeMillis, cursor);
            }
        });
    }

    public /* synthetic */ Object lambda$loadFromDB$4(long j, Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            synchronized (this.mSyncLock) {
                while (cursor.moveToNext()) {
                    FeatureDBItem fromCursor = FeatureDBItem.fromCursor(cursor);
                    this.mCloudCache.put(fromCursor.getName(), fromCursor);
                }
            }
            DefaultLogger.d("CloudControl.ProfileCache", "Load %d items from database, cost %d ms.", Integer.valueOf(cursor.getCount()), Long.valueOf(System.currentTimeMillis() - j));
            return null;
        }
        DefaultLogger.e("CloudControl.ProfileCache", "Fill cache failed with a null cursor.");
        return null;
    }

    public void clearCloudCache() {
        synchronized (this.mSyncLock) {
            for (Map.Entry<String, FeatureProfile> entry : this.mCloudCache.entrySet()) {
                String key = entry.getKey();
                notifyStatusChanged(key, this.mLocalCache.get(key) != null ? this.mLocalCache.get(key).getStatus() : FeatureProfile.Status.UNAVAILABLE.getValue());
            }
            this.mCloudCache.clear();
            this.mCloudStrategyCache.clear();
        }
    }

    public final void loadFromLocalFile(Reader reader) {
        try {
            try {
                try {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    gsonBuilder.registerTypeAdapter(FeatureProfile.class, new FeatureProfile.Deserializer());
                    ArrayList arrayList = (ArrayList) gsonBuilder.create().fromJson(reader, new TypeToken<ArrayList<FeatureProfile>>() { // from class: com.miui.gallery.cloudcontrol.ProfileCache.1
                        {
                            ProfileCache.this = this;
                        }
                    }.getType());
                    synchronized (this.mSyncLock) {
                        Iterator it = arrayList.iterator();
                        while (it.hasNext()) {
                            FeatureProfile featureProfile = (FeatureProfile) it.next();
                            this.mLocalCache.put(featureProfile.getName(), featureProfile);
                        }
                    }
                    reader.close();
                } catch (Exception e) {
                    DefaultLogger.e("CloudControl.ProfileCache", "Failed to load from local file, errorClause: %s, errorMessage: %s.", e.getCause(), e.getMessage());
                    e.printStackTrace();
                    HashMap hashMap = new HashMap();
                    hashMap.put("error_extra", e.getMessage());
                    SamplingStatHelper.recordCountEvent("cloud_control", "parse_local_file_failed", hashMap);
                    reader.close();
                }
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        } catch (Throwable th) {
            try {
                reader.close();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
            throw th;
        }
    }
}
