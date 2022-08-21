package com.miui.gallery.card.scenario;

import android.content.Context;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.text.TextUtils;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.keyczar.Keyczar;

/* loaded from: classes.dex */
public class SceneTagCSVManager {
    public volatile boolean mInitDone;
    public CountDownLatch mInitDoneSignal;
    public volatile boolean mInitStarted;
    public AsyncTask<Context, Void, Void> mInitTask;
    public volatile boolean mInitialized;
    public BiMap<String, Integer> mKIdToTagIdMap;
    public BiMap<Integer, String> mTagIdToKIdMap;

    /* loaded from: classes.dex */
    public static class SingletonHolder {
        public static final SceneTagCSVManager INSTANCE = new SceneTagCSVManager();
    }

    public SceneTagCSVManager() {
        this.mInitTask = new AsyncTask<Context, Void, Void>() { // from class: com.miui.gallery.card.scenario.SceneTagCSVManager.1
            @Override // android.os.AsyncTask
            public Void doInBackground(Context... contextArr) {
                SceneTagCSVManager.this.load(contextArr[0]);
                return null;
            }
        };
        this.mInitDoneSignal = new CountDownLatch(1);
        this.mKIdToTagIdMap = HashBiMap.create();
    }

    public int getTargetTagId(String str) {
        Integer num;
        if (!initialized() || !BaseMiscUtil.isValid(this.mKIdToTagIdMap) || (num = this.mKIdToTagIdMap.get(str)) == null) {
            return -1;
        }
        return num.intValue();
    }

    public String getTargetKnowledgeId(int i) {
        return (!initialized() || !BaseMiscUtil.isValid(this.mTagIdToKIdMap)) ? "" : this.mTagIdToKIdMap.get(Integer.valueOf(i));
    }

    public static SceneTagCSVManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public final boolean initialized() {
        if (this.mInitDone) {
            return this.mInitialized;
        }
        if (!this.mInitStarted) {
            init(GalleryApp.sGetAndroidContext());
        }
        DefaultLogger.d("SceneTagCSVManager", "not initialized, waiting lock");
        try {
            long uptimeMillis = SystemClock.uptimeMillis();
            this.mInitDoneSignal.await();
            DefaultLogger.d("SceneTagCSVManager", "wait init done costs %d ms", Long.valueOf(SystemClock.uptimeMillis() - uptimeMillis));
        } catch (InterruptedException e) {
            DefaultLogger.e("SceneTagCSVManager", e);
        }
        DefaultLogger.d("SceneTagCSVManager", "initialized: %b", Boolean.valueOf(this.mInitialized));
        return this.mInitialized;
    }

    public synchronized void init(Context context) {
        if (!this.mInitDone && !this.mInitStarted) {
            DefaultLogger.d("SceneTagCSVManager", "start init");
            this.mInitStarted = true;
            this.mInitTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, context);
        }
    }

    public final void load(Context context) {
        InputStream inputStream = null;
        try {
            try {
                inputStream = context.getResources().getAssets().open("scene_tags.csv");
                Iterator<CSVRecord> it = CSVParser.parse(inputStream, Charset.forName(Keyczar.DEFAULT_ENCODING), CSVFormat.DEFAULT).iterator();
                while (it.hasNext()) {
                    CSVRecord next = it.next();
                    if (next.size() == 4) {
                        String str = next.get(1);
                        if (!TextUtils.isEmpty(str)) {
                            this.mKIdToTagIdMap.put(next.get(0), Integer.valueOf(str));
                        }
                    }
                }
                if (BaseMiscUtil.isValid(this.mKIdToTagIdMap)) {
                    this.mTagIdToKIdMap = this.mKIdToTagIdMap.inverse();
                }
                this.mInitialized = true;
            } catch (IOException e) {
                this.mInitialized = false;
                DefaultLogger.e("SceneTagCSVManager", "failed to parse tags from scene_tags.csv");
                e.printStackTrace();
            }
        } finally {
            BaseMiscUtil.closeSilently(inputStream);
            this.mInitDone = true;
            this.mInitDoneSignal.countDown();
        }
    }
}
