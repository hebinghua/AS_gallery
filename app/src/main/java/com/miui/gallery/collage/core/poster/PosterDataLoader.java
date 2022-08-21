package com.miui.gallery.collage.core.poster;

import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.SparseArray;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.miui.gallery.collage.CollageUtils;
import com.miui.gallery.collage.app.common.IDataLoader;
import com.miui.gallery.collage.core.layout.LayoutModel;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class PosterDataLoader implements IDataLoader {
    public static final String PATH_POSTER;
    public static final String PATH_POSTER_LAYOUT;
    public static final String PATH_POSTER_MODE;
    public static final String SEPARATOR;
    public AssetManager mAssetManager;
    public DataLoadListener mDataLoadListener;
    public AsyncTask mTask;

    /* loaded from: classes.dex */
    public interface DataLoadListener {
        void onDataLoad(SparseArray<List<LayoutModel>> sparseArray, SparseArray<List<PosterModel>> sparseArray2);
    }

    static {
        String str = File.separator;
        SEPARATOR = str;
        String str2 = "collage" + str + "poster";
        PATH_POSTER = str2;
        PATH_POSTER_MODE = str2 + str + "mode";
        PATH_POSTER_LAYOUT = str2 + str + "layout";
    }

    public PosterDataLoader(AssetManager assetManager, DataLoadListener dataLoadListener) {
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
        public SparseArray<List<LayoutModel>> mPosterLayoutSparseArray = new SparseArray<>();
        public SparseArray<List<PosterModel>> mPosterSparseArray = new SparseArray<>();

        public LoadResourceTask(DataLoadListener dataLoadListener) {
            this.mDataLoadListener = dataLoadListener;
        }

        @Override // android.os.AsyncTask
        public Void doInBackground(AssetManager... assetManagerArr) {
            String[] list;
            String[] list2;
            Gson generateCustomGson = CollageUtils.generateCustomGson();
            AssetManager assetManager = assetManagerArr[0];
            try {
                JsonParser jsonParser = new JsonParser();
                for (String str : assetManager.list(PosterDataLoader.PATH_POSTER_LAYOUT)) {
                    if (isCancelled()) {
                        return null;
                    }
                    PosterDataLoader.addLayoutModel(this.mPosterLayoutSparseArray, PosterDataLoader.generatePosterLayoutModelByName(assetManager, generateCustomGson, str));
                }
                for (String str2 : assetManager.list(PosterDataLoader.PATH_POSTER_MODE)) {
                    if (isCancelled()) {
                        return null;
                    }
                    long currentTimeMillis = System.currentTimeMillis();
                    PosterDataLoader.addPosterModel(this.mPosterSparseArray, PosterDataLoader.generatePosterModelByName(assetManager, generateCustomGson, jsonParser, str2));
                    DefaultLogger.d("PosterDataLoader", "gson parse posterModel %s coast %d", str2, Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                }
            } catch (IOException e) {
                DefaultLogger.d("PosterDataLoader", (Throwable) e);
            }
            return null;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Void r3) {
            DataLoadListener dataLoadListener = this.mDataLoadListener;
            if (dataLoadListener != null) {
                dataLoadListener.onDataLoad(this.mPosterLayoutSparseArray, this.mPosterSparseArray);
            }
        }
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

    public static void addPosterModel(SparseArray<List<PosterModel>> sparseArray, PosterModel posterModel) {
        for (int i : posterModel.collageModels) {
            int i2 = i / 10;
            List<PosterModel> list = sparseArray.get(i2);
            if (list == null) {
                list = new ArrayList<>();
                sparseArray.put(i2, list);
            }
            list.add(posterModel);
        }
    }

    public static LayoutModel generatePosterLayoutModelByName(AssetManager assetManager, Gson gson, String str) {
        LayoutModel layoutModel = (LayoutModel) gson.fromJson(CollageUtils.loadResourceFileString(assetManager, PATH_POSTER_LAYOUT + SEPARATOR + str), (Class<Object>) LayoutModel.class);
        layoutModel.name = str;
        return layoutModel;
    }

    public static PosterModel generatePosterModelByName(AssetManager assetManager, Gson gson, JsonParser jsonParser, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(PATH_POSTER_MODE);
        String str2 = SEPARATOR;
        sb.append(str2);
        sb.append(str);
        String sb2 = sb.toString();
        PosterModel posterModel = (PosterModel) gson.fromJson(CollageUtils.loadResourceFileString(assetManager, sb2 + str2 + "main.json"), (Class<Object>) PosterModel.class);
        posterModel.relativePath = sb2;
        posterModel.name = str;
        ImageElementModel[] imageElementModelArr = posterModel.imageElementModels;
        if (imageElementModelArr != null) {
            for (ImageElementModel imageElementModel : imageElementModelArr) {
                imageElementModel.relativePath = sb2;
            }
        }
        CollagePositionModel[] collagePositionModelArr = posterModel.collagePositions;
        if (collagePositionModelArr != null) {
            for (CollagePositionModel collagePositionModel : collagePositionModelArr) {
                collagePositionModel.relativePath = sb2;
            }
        }
        return posterModel;
    }
}
