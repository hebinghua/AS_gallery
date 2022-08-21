package com.miui.gallery.editor.photo.core.imports.frame;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.RenderEngine;
import com.miui.gallery.editor.photo.core.SdkManager;
import com.miui.gallery.editor.photo.core.SdkProvider;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractFrameFragment;
import com.miui.gallery.editor.photo.core.common.model.FrameData;
import com.miui.gallery.util.GsonUtils;
import com.miui.gallery.util.IoUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class FrameProvider extends SdkProvider<FrameData, AbstractFrameFragment> {
    public static final String SEPARATOR = File.separator;
    public List<FrameData> mFrameData;

    /* loaded from: classes2.dex */
    public interface ResourceListener {
        void onLoadFinish(List<FrameData> list);
    }

    static {
        SdkManager.INSTANCE.register(new FrameProvider());
    }

    private FrameProvider() {
        super(Effect.FRAME);
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public void onActivityCreate() {
        initialize();
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public AbstractFrameFragment onCreateFragment() {
        return new FrameFragment();
    }

    public final void initialize() {
        new LoadResourceTask(new ResourceListener() { // from class: com.miui.gallery.editor.photo.core.imports.frame.FrameProvider.1
            @Override // com.miui.gallery.editor.photo.core.imports.frame.FrameProvider.ResourceListener
            public void onLoadFinish(List<FrameData> list) {
                FrameProvider.this.mFrameData = list;
                FrameProvider.this.notifyInitializeFinish();
            }
        }).execute(getApplicationContext());
    }

    /* loaded from: classes2.dex */
    public static class LoadResourceTask extends AsyncTask<Application, Void, List<FrameData>> {
        public ResourceListener mResourceListener;

        public LoadResourceTask(ResourceListener resourceListener) {
            this.mResourceListener = resourceListener;
        }

        @Override // android.os.AsyncTask
        public List<FrameData> doInBackground(Application... applicationArr) {
            char c = 0;
            AssetManager assets = applicationArr[0].getAssets();
            ArrayList arrayList = new ArrayList();
            try {
                String[] list = assets.list("frame");
                int i = 0;
                while (i < list.length) {
                    String str = list[i];
                    Object[] objArr = new Object[3];
                    objArr[c] = "frame";
                    objArr[1] = FrameProvider.SEPARATOR;
                    objArr[2] = str;
                    FrameConfig frameConfig = (FrameConfig) GsonUtils.fromJson(FrameProvider.loadResourceFileString(assets, String.format("%s%s%s", objArr)), (Class<Object>) FrameConfig.class);
                    arrayList.add(new FrameData((short) i, str, frameConfig.getWidth(), frameConfig.getHeight(), frameConfig.getTalkbackName(), frameConfig.getIconRatio(), frameConfig.getIconHPadding(), frameConfig.getIconVPadding(), frameConfig.isCinemaStyle(), frameConfig.getIcon()));
                    i++;
                    c = 0;
                }
            } catch (Exception e) {
                DefaultLogger.e("FrameProvider", "FrameProvider load resource error", e);
            }
            return arrayList;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(List<FrameData> list) {
            ResourceListener resourceListener = this.mResourceListener;
            if (resourceListener != null) {
                resourceListener.onLoadFinish(list);
            }
        }
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public List<? extends FrameData> list() {
        return this.mFrameData;
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public RenderEngine createEngine(Context context) {
        return new FrameEngine();
    }

    public static String loadResourceFileString(AssetManager assetManager, String str) {
        InputStream inputStream;
        InputStream inputStream2 = null;
        String str2 = null;
        try {
            inputStream = assetManager.open(str);
            try {
                try {
                    str2 = IoUtils.readInputStreamToString("FrameProvider", inputStream);
                } catch (IOException e) {
                    e = e;
                    DefaultLogger.e("FrameProvider", e);
                    IoUtils.close(inputStream);
                    return str2;
                }
            } catch (Throwable th) {
                th = th;
                inputStream2 = inputStream;
                IoUtils.close(inputStream2);
                throw th;
            }
        } catch (IOException e2) {
            e = e2;
            inputStream = null;
        } catch (Throwable th2) {
            th = th2;
            IoUtils.close(inputStream2);
            throw th;
        }
        IoUtils.close(inputStream);
        return str2;
    }
}
