package com.miui.gallery.util;

import android.content.Context;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class ShareComponentSorter {
    public static volatile ShareComponentSorter sInstance;
    public Context mApplication;
    public boolean mHasPendingSave;
    public boolean mInitialized;
    public Map<Tag, ComponentRecord> mRecords;
    public Map<Tag, List<Long>> mTmpRecords;
    public final Object mRecordsLock = new Object();
    public final Gauss mGauss = new Gauss(SearchStatUtils.POW, 1.0d);
    public final List<OnInitializedListener> mOnInitializedListeners = new ArrayList();

    /* loaded from: classes2.dex */
    public interface OnInitializedListener {
        void onInitialized();
    }

    public static synchronized ShareComponentSorter getInstance() {
        ShareComponentSorter shareComponentSorter;
        synchronized (ShareComponentSorter.class) {
            if (sInstance == null) {
                sInstance = new ShareComponentSorter();
            }
            shareComponentSorter = sInstance;
        }
        return shareComponentSorter;
    }

    public void registerOnInitializedListener(OnInitializedListener onInitializedListener) {
        this.mOnInitializedListeners.add(onInitializedListener);
    }

    public void removeOnInitializedListener(OnInitializedListener onInitializedListener) {
        this.mOnInitializedListeners.remove(onInitializedListener);
    }

    public boolean initialized() {
        return this.mInitialized;
    }

    public void initialize(Context context) {
        if (this.mInitialized) {
            return;
        }
        if (context != context.getApplicationContext()) {
            throw new IllegalArgumentException("please initialize with application");
        }
        this.mApplication = context;
        new LoadTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "components-history.json");
        this.mInitialized = true;
    }

    public Comparator<ResolveInfo> createComparator() {
        ComponentComparator componentComparator;
        if (this.mRecords != null) {
            synchronized (this.mRecordsLock) {
                long currentTimeMillis = System.currentTimeMillis();
                DefaultLogger.d("ShareComponentSorter", "initialized create a new comparator");
                HashMap hashMap = new HashMap();
                GaussEvaluator gaussEvaluator = new GaussEvaluator();
                for (Map.Entry<Tag, ComponentRecord> entry : this.mRecords.entrySet()) {
                    float calcPriority = entry.getValue().calcPriority(gaussEvaluator);
                    DefaultLogger.d("ShareComponentSorter", "[%s] -> %f", entry.getKey().mClass, Float.valueOf(calcPriority));
                    hashMap.put(entry.getKey(), Integer.valueOf((int) (calcPriority * 1000.0f)));
                }
                DefaultLogger.d("ShareComponentSorter", "create a gauss comparator costs %dms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                componentComparator = new ComponentComparator(hashMap);
            }
            return componentComparator;
        }
        DefaultLogger.d("ShareComponentSorter", "not initialized, use a dummy comparator");
        return new DummyComparator();
    }

    public void touch(String str, String str2) {
        append(str, str2, System.currentTimeMillis());
    }

    public final void append(String str, String str2, long j) {
        Tag tag = new Tag(str, str2);
        if (this.mRecords == null) {
            DefaultLogger.d("ShareComponentSorter", "not initialized, use a temp record");
            if (this.mTmpRecords == null) {
                this.mTmpRecords = new HashMap();
            }
            List<Long> list = this.mTmpRecords.get(tag);
            if (list == null) {
                list = new ArrayList<>();
                this.mTmpRecords.put(tag, list);
            }
            list.add(Long.valueOf(j));
            return;
        }
        synchronized (this.mRecordsLock) {
            DefaultLogger.d("ShareComponentSorter", "initialized, inserting");
            ComponentRecord componentRecord = this.mRecords.get(tag);
            if (componentRecord == null) {
                componentRecord = new ComponentRecord(str, str2);
                this.mRecords.put(tag, componentRecord);
            }
            componentRecord.append(j);
        }
    }

    public void save() {
        if (this.mRecords != null) {
            new SaveTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "components-history.json");
        } else {
            this.mHasPendingSave = true;
        }
    }

    /* loaded from: classes2.dex */
    public class LoadTask extends AsyncTask<String, Void, Map<Tag, ComponentRecord>> {
        public LoadTask() {
        }

        /* JADX WARN: Not initialized variable reg: 7, insn: 0x004d: MOVE  (r6 I:??[OBJECT, ARRAY]) = (r7 I:??[OBJECT, ARRAY]), block:B:17:0x004d */
        /* JADX WARN: Removed duplicated region for block: B:40:0x007f  */
        /* JADX WARN: Removed duplicated region for block: B:42:0x0085  */
        @Override // android.os.AsyncTask
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public java.util.Map<com.miui.gallery.util.ShareComponentSorter.Tag, com.miui.gallery.util.ShareComponentSorter.ComponentRecord> doInBackground(java.lang.String... r14) {
            /*
                Method dump skipped, instructions count: 242
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.util.ShareComponentSorter.LoadTask.doInBackground(java.lang.String[]):java.util.Map");
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Map<Tag, ComponentRecord> map) {
            super.onPostExecute((LoadTask) map);
            DefaultLogger.d("ShareComponentSorter", "initialized finish.");
            ShareComponentSorter.this.mRecords = map;
            if (ShareComponentSorter.this.mTmpRecords != null && !ShareComponentSorter.this.mTmpRecords.isEmpty()) {
                DefaultLogger.d("ShareComponentSorter", "has temp records, do transfer");
                for (Map.Entry entry : ShareComponentSorter.this.mTmpRecords.entrySet()) {
                    for (Long l : (List) entry.getValue()) {
                        ShareComponentSorter.this.append(((Tag) entry.getKey()).mPackage, ((Tag) entry.getKey()).mClass, l.longValue());
                    }
                }
            }
            if (ShareComponentSorter.this.mHasPendingSave) {
                DefaultLogger.d("ShareComponentSorter", "requested save when initializing, do save");
                ShareComponentSorter.this.save();
                ShareComponentSorter.this.mHasPendingSave = false;
            }
            for (OnInitializedListener onInitializedListener : ShareComponentSorter.this.mOnInitializedListeners) {
                onInitializedListener.onInitialized();
            }
        }
    }

    /* loaded from: classes2.dex */
    public class SaveTask extends AsyncTask<String, Void, Void> {
        public SaveTask() {
        }

        /* JADX WARN: Removed duplicated region for block: B:43:0x00a0 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        @Override // android.os.AsyncTask
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public java.lang.Void doInBackground(java.lang.String... r8) {
            /*
                r7 = this;
                long r0 = java.lang.System.currentTimeMillis()
                java.io.File r2 = new java.io.File
                com.miui.gallery.util.ShareComponentSorter r3 = com.miui.gallery.util.ShareComponentSorter.this
                android.content.Context r3 = com.miui.gallery.util.ShareComponentSorter.access$500(r3)
                java.io.File r3 = r3.getFilesDir()
                r4 = 0
                r8 = r8[r4]
                r2.<init>(r3, r8)
                org.json.JSONArray r8 = new org.json.JSONArray
                r8.<init>()
                com.miui.gallery.util.ShareComponentSorter r3 = com.miui.gallery.util.ShareComponentSorter.this
                java.lang.Object r3 = com.miui.gallery.util.ShareComponentSorter.access$1400(r3)
                monitor-enter(r3)
                com.miui.gallery.util.ShareComponentSorter r4 = com.miui.gallery.util.ShareComponentSorter.this     // Catch: java.lang.Throwable -> Lab
                java.util.Map r4 = com.miui.gallery.util.ShareComponentSorter.access$800(r4)     // Catch: java.lang.Throwable -> Lab
                java.util.Set r4 = r4.entrySet()     // Catch: java.lang.Throwable -> Lab
                java.util.Iterator r4 = r4.iterator()     // Catch: java.lang.Throwable -> Lab
            L30:
                boolean r5 = r4.hasNext()     // Catch: java.lang.Throwable -> Lab
                if (r5 == 0) goto L4a
                java.lang.Object r5 = r4.next()     // Catch: java.lang.Throwable -> Lab
                java.util.Map$Entry r5 = (java.util.Map.Entry) r5     // Catch: java.lang.Throwable -> Lab
                java.lang.Object r5 = r5.getValue()     // Catch: java.lang.Throwable -> Lab
                com.miui.gallery.util.ShareComponentSorter$ComponentRecord r5 = (com.miui.gallery.util.ShareComponentSorter.ComponentRecord) r5     // Catch: java.lang.Throwable -> Lab
                org.json.JSONObject r5 = com.miui.gallery.util.ShareComponentSorter.ComponentRecord.toJson(r5)     // Catch: java.lang.Throwable -> Lab
                r8.put(r5)     // Catch: java.lang.Throwable -> Lab
                goto L30
            L4a:
                java.lang.String r4 = "ShareComponentSorter"
                java.lang.String r5 = "write %d records to file"
                com.miui.gallery.util.ShareComponentSorter r6 = com.miui.gallery.util.ShareComponentSorter.this     // Catch: java.lang.Throwable -> Lab
                java.util.Map r6 = com.miui.gallery.util.ShareComponentSorter.access$800(r6)     // Catch: java.lang.Throwable -> Lab
                int r6 = r6.size()     // Catch: java.lang.Throwable -> Lab
                java.lang.Integer r6 = java.lang.Integer.valueOf(r6)     // Catch: java.lang.Throwable -> Lab
                com.miui.gallery.util.logger.DefaultLogger.d(r4, r5, r6)     // Catch: java.lang.Throwable -> Lab
                monitor-exit(r3)     // Catch: java.lang.Throwable -> Lab
                r3 = 0
                java.io.FileWriter r4 = new java.io.FileWriter     // Catch: java.lang.Throwable -> L76 java.io.IOException -> L78
                r4.<init>(r2)     // Catch: java.lang.Throwable -> L76 java.io.IOException -> L78
                java.lang.String r8 = r8.toString()     // Catch: java.io.IOException -> L74 java.lang.Throwable -> L9c
                r4.write(r8)     // Catch: java.io.IOException -> L74 java.lang.Throwable -> L9c
                r4.flush()     // Catch: java.io.IOException -> L74 java.lang.Throwable -> L9c
                r4.close()     // Catch: java.io.IOException -> L85
                goto L8b
            L74:
                r8 = move-exception
                goto L7a
            L76:
                r8 = move-exception
                goto L9e
            L78:
                r8 = move-exception
                r4 = r3
            L7a:
                java.lang.String r2 = "ShareComponentSorter"
                com.miui.gallery.util.logger.DefaultLogger.w(r2, r8)     // Catch: java.lang.Throwable -> L9c
                if (r4 == 0) goto L8b
                r4.close()     // Catch: java.io.IOException -> L85
                goto L8b
            L85:
                r8 = move-exception
                java.lang.String r2 = "ShareComponentSorter"
                com.miui.gallery.util.logger.DefaultLogger.w(r2, r8)
            L8b:
                java.lang.String r8 = "ShareComponentSorter"
                java.lang.String r2 = "write to file: %dms"
                long r4 = java.lang.System.currentTimeMillis()
                long r4 = r4 - r0
                java.lang.Long r0 = java.lang.Long.valueOf(r4)
                com.miui.gallery.util.logger.DefaultLogger.d(r8, r2, r0)
                return r3
            L9c:
                r8 = move-exception
                r3 = r4
            L9e:
                if (r3 == 0) goto Laa
                r3.close()     // Catch: java.io.IOException -> La4
                goto Laa
            La4:
                r0 = move-exception
                java.lang.String r1 = "ShareComponentSorter"
                com.miui.gallery.util.logger.DefaultLogger.w(r1, r0)
            Laa:
                throw r8
            Lab:
                r8 = move-exception
                monitor-exit(r3)     // Catch: java.lang.Throwable -> Lab
                throw r8
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.util.ShareComponentSorter.SaveTask.doInBackground(java.lang.String[]):java.lang.Void");
        }
    }

    /* loaded from: classes2.dex */
    public class GaussEvaluator implements ComponentRecord.Evaluator {
        public double mCoefficient;
        public long mToday;

        public GaussEvaluator() {
            this.mToday = System.currentTimeMillis() / ComponentRecord.ONE_DAY;
            this.mCoefficient = 10.0d;
        }

        @Override // com.miui.gallery.util.ShareComponentSorter.ComponentRecord.Evaluator
        public float evaluate(long j, int i) {
            return (float) (ShareComponentSorter.this.mGauss.value((this.mToday - j) / this.mCoefficient) * i);
        }
    }

    /* loaded from: classes2.dex */
    public static class ComponentRecord {
        public static final long ONE_DAY = TimeUnit.DAYS.toMillis(1);
        public String mComponent;
        public int mCountOfToday;
        public TreeMap<Long, Integer> mHistory;
        public String mPackage;
        public long mRecent;
        public long mToday;

        /* loaded from: classes2.dex */
        public interface Evaluator {
            float evaluate(long j, int i);
        }

        public ComponentRecord(String str, String str2, long j, TreeMap<Long, Integer> treeMap) {
            this.mPackage = str;
            this.mComponent = str2;
            this.mRecent = j;
            this.mHistory = treeMap;
            long j2 = j / ONE_DAY;
            this.mToday = j2;
            this.mCountOfToday = Numbers.unbox(treeMap.get(Long.valueOf(j2)), 0);
        }

        public ComponentRecord(String str, String str2) {
            this(str, str2, 0L, new TreeMap());
        }

        public float calcPriority(Evaluator evaluator) {
            float f = 0.0f;
            for (Map.Entry<Long, Integer> entry : this.mHistory.entrySet()) {
                f += evaluator.evaluate(Numbers.unbox(entry.getKey(), 0L), Numbers.unbox(entry.getValue(), 0));
            }
            return f;
        }

        public void append(long j) {
            if (Math.abs(j - this.mRecent) < 0) {
                return;
            }
            this.mRecent = j;
            long j2 = j / ONE_DAY;
            long j3 = this.mToday;
            if (j2 != j3) {
                DefaultLogger.d("ShareComponentSorter", "switch day: %d->%d", Long.valueOf(j3), Long.valueOf(j2));
                this.mToday = j2;
                this.mCountOfToday = Numbers.unbox(this.mHistory.get(Long.valueOf(j2)), 0);
            }
            TreeMap<Long, Integer> treeMap = this.mHistory;
            Long valueOf = Long.valueOf(this.mToday);
            int i = this.mCountOfToday + 1;
            this.mCountOfToday = i;
            treeMap.put(valueOf, Integer.valueOf(i));
        }

        public static TreeMap<Long, Integer> readHistory(JSONObject jSONObject) {
            TreeMap<Long, Integer> treeMap = new TreeMap<>();
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                try {
                    String next = keys.next();
                    treeMap.put(Long.valueOf(Long.parseLong(next)), Integer.valueOf(jSONObject.getInt(next)));
                } catch (NumberFormatException e) {
                    DefaultLogger.w("ShareComponentSorter", e);
                } catch (JSONException e2) {
                    DefaultLogger.w("ShareComponentSorter", e2);
                }
            }
            DefaultLogger.d("ShareComponentSorter", "finish read history[%d] from json[%d]", Integer.valueOf(treeMap.size()), Integer.valueOf(jSONObject.length()));
            return treeMap;
        }

        public static JSONObject writeHistory(TreeMap<Long, Integer> treeMap) {
            JSONObject jSONObject = new JSONObject();
            if (treeMap.isEmpty()) {
                return jSONObject;
            }
            long longValue = treeMap.navigableKeySet().last().longValue();
            for (Map.Entry<Long, Integer> entry : treeMap.descendingMap().entrySet()) {
                Long key = entry.getKey();
                if (key == null || key.longValue() == 0) {
                    DefaultLogger.d("ShareComponentSorter", "receive an invalid value[%s], skip", key);
                } else {
                    try {
                        if (longValue - key.longValue() > 30) {
                            DefaultLogger.d("ShareComponentSorter", "record is out of date(%dd), skip", Long.valueOf(longValue - key.longValue()));
                            break;
                        }
                        jSONObject.put(String.valueOf(entry.getKey()), entry.getValue());
                    } catch (JSONException e) {
                        DefaultLogger.w("ShareComponentSorter", e);
                    }
                }
            }
            DefaultLogger.d("ShareComponentSorter", "finish write history[%d] to json[%d]", Integer.valueOf(treeMap.size()), Integer.valueOf(jSONObject.length()));
            return jSONObject;
        }

        public static ComponentRecord fromJson(JSONObject jSONObject) {
            try {
                return new ComponentRecord(jSONObject.getString("package"), jSONObject.getString("component"), jSONObject.optLong("recent"), readHistory(jSONObject.getJSONObject("history")));
            } catch (JSONException e) {
                DefaultLogger.w("ShareComponentSorter", e);
                return null;
            }
        }

        public static JSONObject toJson(ComponentRecord componentRecord) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("package", componentRecord.mPackage);
                jSONObject.put("component", componentRecord.mComponent);
                jSONObject.put("recent", componentRecord.mRecent);
                jSONObject.put("history", writeHistory(componentRecord.mHistory));
            } catch (JSONException e) {
                DefaultLogger.w("ShareComponentSorter", e);
            }
            return jSONObject;
        }
    }

    /* loaded from: classes2.dex */
    public static class DummyComparator implements Comparator<ResolveInfo> {
        @Override // java.util.Comparator
        public int compare(ResolveInfo resolveInfo, ResolveInfo resolveInfo2) {
            return 0;
        }

        public DummyComparator() {
        }
    }

    /* loaded from: classes2.dex */
    public static class ComponentComparator implements Comparator<ResolveInfo> {
        public Map<Tag, Integer> mPriority;
        public Tag mTemp = new Tag();

        public ComponentComparator(Map<Tag, Integer> map) {
            this.mPriority = map;
        }

        @Override // java.util.Comparator
        public int compare(ResolveInfo resolveInfo, ResolveInfo resolveInfo2) {
            this.mTemp.mPackage = resolveInfo.activityInfo.packageName;
            this.mTemp.mClass = resolveInfo.activityInfo.name;
            int unbox = Numbers.unbox(this.mPriority.get(this.mTemp), 0);
            this.mTemp.mPackage = resolveInfo2.activityInfo.packageName;
            this.mTemp.mClass = resolveInfo2.activityInfo.name;
            int unbox2 = Numbers.unbox(this.mPriority.get(this.mTemp), 0);
            if (unbox > unbox2) {
                return -1;
            }
            return unbox == unbox2 ? 0 : 1;
        }
    }

    /* loaded from: classes2.dex */
    public static class Tag {
        public String mClass;
        public String mPackage;

        public Tag(String str, String str2) {
            this.mPackage = str;
            this.mClass = str2;
        }

        public Tag() {
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Tag)) {
                return false;
            }
            Tag tag = (Tag) obj;
            return this.mPackage.equals(tag.mPackage) && this.mClass.equals(tag.mClass);
        }

        public int hashCode() {
            return this.mPackage.hashCode() ^ this.mClass.hashCode();
        }
    }
}
