package com.miui.gallery.collage.core.layout;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.SparseArray;
import com.google.gson.Gson;
import com.miui.gallery.collage.CollageUtils;
import com.miui.gallery.collage.app.common.IDataLoader;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class LayoutDataLoader implements IDataLoader {
    public static final String PATH_LAYOUT;
    public static final String SEPARATOR;
    public AssetManager mAssetManager;
    public DataLoadListener mDataLoadListener;
    public AsyncTask mTask;

    /* loaded from: classes.dex */
    public interface DataLoadListener {
        void onDataLoad(SparseArray<List<LayoutModel>> sparseArray);
    }

    static {
        String str = File.separator;
        SEPARATOR = str;
        PATH_LAYOUT = "collage" + str + "layout";
    }

    public LayoutDataLoader(AssetManager assetManager, DataLoadListener dataLoadListener) {
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
        public SparseArray<List<LayoutModel>> mLayoutSparseArray = new SparseArray<>();

        public LoadResourceTask(DataLoadListener dataLoadListener) {
            this.mDataLoadListener = dataLoadListener;
        }

        @Override // android.os.AsyncTask
        public Void doInBackground(AssetManager... assetManagerArr) {
            String[] list;
            Gson generateCustomGson = CollageUtils.generateCustomGson();
            AssetManager assetManager = assetManagerArr[0];
            try {
                for (String str : assetManager.list(LayoutDataLoader.PATH_LAYOUT)) {
                    if (isCancelled()) {
                        return null;
                    }
                    LayoutDataLoader.addLayoutModel(this.mLayoutSparseArray, LayoutDataLoader.generateLayoutModelByName(assetManager, generateCustomGson, str));
                }
            } catch (IOException e) {
                DefaultLogger.d("LayoutDataLoader", (Throwable) e);
            }
            return null;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Void r2) {
            DataLoadListener dataLoadListener = this.mDataLoadListener;
            if (dataLoadListener != null) {
                dataLoadListener.onDataLoad(this.mLayoutSparseArray);
            }
        }
    }

    public static LayoutModel generateLayoutModelByName(AssetManager assetManager, Gson gson, String str) {
        LayoutModel layoutModel = (LayoutModel) gson.fromJson(CollageUtils.loadResourceFileString(assetManager, PATH_LAYOUT + SEPARATOR + str), (Class<Object>) LayoutModel.class);
        layoutModel.name = str;
        return layoutModel;
    }

    public static void addLayoutModel(SparseArray<List<LayoutModel>> sparseArray, LayoutModel layoutModel) {
        int i = layoutModel.size;
        List<LayoutModel> list = sparseArray.get(i);
        if (list == null) {
            list = new ArrayList<>();
            sparseArray.put(i, list);
        }
        list.add(layoutModel);
    }
}
