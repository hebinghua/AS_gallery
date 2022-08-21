package com.miui.gallery.util;

import android.content.ContentUris;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import com.miui.gallery.provider.ProcessingMedia;
import com.miui.gallery.provider.ProcessingMediaManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes2.dex */
public class ProcessingMediaHelper {
    public static final Object sLock = new Object();
    public final HashMap<Long, ProcessingItem> mProcessingItems;

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final ProcessingMediaHelper INSTANCE = new ProcessingMediaHelper(null);
    }

    public /* synthetic */ ProcessingMediaHelper(AnonymousClass1 anonymousClass1) {
        this();
    }

    public ProcessingMediaHelper() {
        HashMap<Long, ProcessingItem> hashMap = new HashMap<>();
        this.mProcessingItems = hashMap;
        hashMap.putAll(PreferenceHelper.getAll());
        new CalibrateTask(null).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public static ProcessingMediaHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void addProcessingItem(ProcessingItem processingItem) {
        synchronized (sLock) {
            PreferenceHelper.put(processingItem);
            this.mProcessingItems.put(Long.valueOf(processingItem.mMediaStoreId), processingItem);
            DefaultLogger.d("ProcessingMediaHelper", "add processing item: %s", processingItem);
        }
    }

    public boolean removeProcessingItem(ProcessingItem processingItem) {
        synchronized (sLock) {
            PreferenceHelper.remove(processingItem);
            if (processingItem.mMediaStoreId > 0) {
                if (this.mProcessingItems.remove(Long.valueOf(processingItem.mMediaStoreId)) == null) {
                    return false;
                }
                DefaultLogger.d("ProcessingMediaHelper", "remove [%s] from processing items by media id", processingItem.toString());
                return true;
            } else if (!TextUtils.isEmpty(processingItem.mPath)) {
                for (ProcessingItem processingItem2 : this.mProcessingItems.values()) {
                    if (TextUtils.equals(processingItem.mPath, processingItem2.mPath)) {
                        this.mProcessingItems.remove(Long.valueOf(processingItem2.mMediaStoreId));
                        DefaultLogger.d("ProcessingMediaHelper", "remove [%s] from processing items by file path", processingItem.toString());
                        return true;
                    }
                }
                return false;
            } else {
                DefaultLogger.d("ProcessingMediaHelper", "illegal arguments %s", processingItem);
                return false;
            }
        }
    }

    public boolean isMediaInProcessing(String str) {
        return matchItem(str) != null;
    }

    public boolean isBlurred(String str) {
        ProcessingItem matchItem = matchItem(str);
        return matchItem != null && matchItem.isBlurred;
    }

    public ProcessingItem matchItem(String str) {
        ProcessingItem processingItem;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        Scheme ofUri = Scheme.ofUri(str);
        if (Scheme.ofUri(str) == Scheme.UNKNOWN) {
            ofUri = Scheme.FILE;
            str = ofUri.wrap(str);
        }
        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$util$Scheme[ofUri.ordinal()];
        if (i == 1) {
            if (!TextUtils.equals(Uri.parse(str).getAuthority(), "media")) {
                return null;
            } else {
                try {
                    long parseId = ContentUris.parseId(Uri.parse(str));
                    synchronized (sLock) {
                        processingItem = this.mProcessingItems.get(Long.valueOf(parseId));
                    }
                    return processingItem;
                } catch (Exception e) {
                    DefaultLogger.w("ProcessingMediaHelper", "Illegal Uri format: %s", str);
                    DefaultLogger.w("ProcessingMediaHelper", e);
                    return null;
                }
            }
        }
        if (i != 2) {
            return null;
        }
        String substring = str.substring(7);
        synchronized (sLock) {
            for (ProcessingItem processingItem2 : this.mProcessingItems.values()) {
                if (TextUtils.equals(substring, processingItem2.mPath)) {
                    return processingItem2;
                }
            }
            return null;
        }
    }

    /* renamed from: com.miui.gallery.util.ProcessingMediaHelper$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$util$Scheme;

        static {
            int[] iArr = new int[Scheme.values().length];
            $SwitchMap$com$miui$gallery$util$Scheme = iArr;
            try {
                iArr[Scheme.CONTENT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$util$Scheme[Scheme.FILE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
        }
    }

    public void calibrateCache(List<ProcessingMedia> list) {
        DefaultLogger.d("ProcessingMediaHelper", "calibrateCache");
        synchronized (sLock) {
            this.mProcessingItems.clear();
            if (list != null) {
                for (ProcessingMedia processingMedia : list) {
                    ProcessingItem build = ProcessingItem.build(processingMedia.getMediaStoreId(), processingMedia.getPath(), processingMedia.isUsingGaussianForTemp());
                    this.mProcessingItems.put(Long.valueOf(build.mMediaStoreId), build);
                    DefaultLogger.d("ProcessingMediaHelper", "calibrateCache - [%s]", build);
                }
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class ProcessingItem {
        public final boolean isBlurred;
        public final long mMediaStoreId;
        public final String mPath;

        public ProcessingItem(long j, String str, boolean z) {
            this.mMediaStoreId = j;
            this.mPath = str;
            this.isBlurred = z;
        }

        public static ProcessingItem build(long j, String str, boolean z) {
            return new ProcessingItem(j, str, z);
        }

        public String toString() {
            return String.format(Locale.US, "media id: %s, path: %s, gaussian: %s", Long.valueOf(this.mMediaStoreId), this.mPath, Boolean.valueOf(this.isBlurred));
        }

        public long getMediaStoreId() {
            return this.mMediaStoreId;
        }

        public boolean isBlurred() {
            return this.isBlurred;
        }
    }

    /* loaded from: classes2.dex */
    public static class CalibrateTask extends AsyncTask<Void, Void, List<ProcessingMedia>> {
        public CalibrateTask() {
        }

        public /* synthetic */ CalibrateTask(AnonymousClass1 anonymousClass1) {
            this();
        }

        @Override // android.os.AsyncTask
        public List<ProcessingMedia> doInBackground(Void... voidArr) {
            return ProcessingMediaManager.queryProcessingMedias();
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(List<ProcessingMedia> list) {
            for (ProcessingItem processingItem : PreferenceHelper.getAll().values()) {
                ProcessingMediaHelper.getInstance().removeProcessingItem(processingItem);
            }
            PreferenceHelper.clear();
            list.forEach(ProcessingMediaHelper$CalibrateTask$$ExternalSyntheticLambda0.INSTANCE);
        }

        public static /* synthetic */ void lambda$onPostExecute$0(ProcessingMedia processingMedia) {
            ProcessingMediaHelper.getInstance().addProcessingItem(ProcessingItem.build(processingMedia.getMediaStoreId(), processingMedia.getPath(), processingMedia.isUsingGaussianForTemp()));
        }
    }

    /* loaded from: classes2.dex */
    public static class PreferenceHelper {
        public final SharedPreferences mSharedPreferences;

        public /* synthetic */ PreferenceHelper(AnonymousClass1 anonymousClass1) {
            this();
        }

        /* loaded from: classes2.dex */
        public static class SingletonHolder {
            public static final PreferenceHelper sInstance = new PreferenceHelper(null);
        }

        public PreferenceHelper() {
            this.mSharedPreferences = StaticContext.sGetAndroidContext().getSharedPreferences("com.miui.gallery_processing_media", 0);
        }

        public static SharedPreferences getPreferences() {
            return SingletonHolder.sInstance.mSharedPreferences;
        }

        public static void clear() {
            getPreferences().edit().clear().apply();
        }

        public static void put(ProcessingItem processingItem) {
            getPreferences().edit().putString(String.valueOf(processingItem.getMediaStoreId()), GsonUtils.toJson(processingItem)).apply();
        }

        public static void remove(ProcessingItem processingItem) {
            getPreferences().edit().remove(String.valueOf(processingItem.getMediaStoreId())).apply();
        }

        public static Map<Long, ProcessingItem> getAll() {
            HashMap hashMap = new HashMap();
            for (Object obj : getPreferences().getAll().values()) {
                if (obj instanceof String) {
                    ProcessingItem processingItem = (ProcessingItem) GsonUtils.fromJson((String) obj, (Class<Object>) ProcessingItem.class);
                    hashMap.put(Long.valueOf(processingItem.getMediaStoreId()), processingItem);
                }
            }
            return hashMap;
        }
    }
}
