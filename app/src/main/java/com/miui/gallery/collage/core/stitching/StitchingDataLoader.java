package com.miui.gallery.collage.core.stitching;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import com.google.gson.Gson;
import com.miui.gallery.collage.CollageUtils;
import com.miui.gallery.collage.app.common.IDataLoader;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class StitchingDataLoader implements IDataLoader {
    public static final String PATH_STITCHING;
    public static final String SEPARATOR;
    public AssetManager mAssetManager;
    public DataLoadListener mDataLoadListener;
    public AsyncTask mTask;

    /* loaded from: classes.dex */
    public interface DataLoadListener {
        void onDataLoad(List<StitchingModel> list);
    }

    static {
        String str = File.separator;
        SEPARATOR = str;
        PATH_STITCHING = "collage" + str + "stitching";
    }

    public StitchingDataLoader(AssetManager assetManager, DataLoadListener dataLoadListener) {
        this.mAssetManager = assetManager;
        this.mDataLoadListener = dataLoadListener;
    }

    @Override // com.miui.gallery.collage.app.common.IDataLoader
    public void loadData() {
        this.mTask = new LoadResourceTask(this.mDataLoadListener).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this.mAssetManager);
    }

    @Override // com.miui.gallery.collage.app.common.IDataLoader
    public void cancel() {
        AsyncTask asyncTask = this.mTask;
        if (asyncTask != null) {
            asyncTask.cancel(false);
        }
    }

    /* loaded from: classes.dex */
    public static class LoadResourceTask extends AsyncTask<AssetManager, Void, Void> {
        public DataLoadListener mDataLoadListener;
        public List<StitchingModel> mStitchingModelList;

        public LoadResourceTask(DataLoadListener dataLoadListener) {
            this.mDataLoadListener = dataLoadListener;
        }

        @Override // android.os.AsyncTask
        public Void doInBackground(AssetManager... assetManagerArr) {
            String[] list;
            this.mStitchingModelList = new ArrayList();
            Gson generateCustomGson = CollageUtils.generateCustomGson();
            AssetManager assetManager = assetManagerArr[0];
            try {
                for (String str : assetManager.list(StitchingDataLoader.PATH_STITCHING)) {
                    if (isCancelled()) {
                        return null;
                    }
                    this.mStitchingModelList.add(StitchingDataLoader.generateStitchingModelByName(assetManager, generateCustomGson, str));
                }
            } catch (IOException e) {
                DefaultLogger.d("StitchingDataLoader", (Throwable) e);
            }
            return null;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Void r2) {
            DataLoadListener dataLoadListener = this.mDataLoadListener;
            if (dataLoadListener != null) {
                dataLoadListener.onDataLoad(this.mStitchingModelList);
            }
        }
    }

    public static StitchingModel generateStitchingModelByName(AssetManager assetManager, Gson gson, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(PATH_STITCHING);
        String str2 = SEPARATOR;
        sb.append(str2);
        sb.append(str);
        String sb2 = sb.toString();
        StitchingModel stitchingModel = (StitchingModel) gson.fromJson(CollageUtils.loadResourceFileString(assetManager, sb2 + str2 + "main.json"), (Class<Object>) StitchingModel.class);
        stitchingModel.relativePath = sb2;
        stitchingModel.name = str;
        return stitchingModel;
    }
}
