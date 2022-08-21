package com.miui.gallery.editor.photo.core.imports.text;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.text.TextUtils;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.RenderEngine;
import com.miui.gallery.editor.photo.core.SdkManager;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment;
import com.miui.gallery.editor.photo.core.common.model.TextData;
import com.miui.gallery.editor.photo.core.common.provider.AbstractTextProvider;
import com.miui.gallery.editor.photo.core.imports.text.dialog.BaseDialogModel;
import com.miui.gallery.editor.photo.core.imports.text.dialog.DialogManager;
import com.miui.gallery.editor.photo.core.imports.text.signature.SignatureInfo;
import com.miui.gallery.editor.photo.core.imports.text.watermark.WatermarkInfo;
import com.miui.gallery.util.GsonUtils;
import com.miui.gallery.util.IoUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
class TextProvider extends AbstractTextProvider {
    public DialogManager mDialogManager;
    public List<TextData> mTextBubbleList;
    public List<TextData> mTextCityList;
    public List<TextData> mTextClassicList;
    public List<TextData> mTextFestivalList;
    public List<TextData> mTextSceneList;
    public List<TextData> mTextWatermarkList;

    /* loaded from: classes2.dex */
    public interface ResourceListener {
        void onLoadFinish(List<WatermarkInfo> list);
    }

    public TextProvider() {
        super(Effect.TEXT);
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public void onActivityCreate() {
        super.onActivityCreate();
        initialize();
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public List<? extends TextData> list() {
        return this.mTextBubbleList;
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public AbstractEffectFragment onCreateFragment() {
        return new TextFragment();
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public RenderEngine createEngine(Context context) {
        return new TextEngine();
    }

    public final void initialize() {
        this.mDialogManager = new DialogManager();
        initTextBubbleData();
        initTextWaterMarkData();
        initTextClassicData();
        initTextFestivalData();
        initTextSceneData();
        initTextCityData();
    }

    public final void initTextBubbleData() {
        String str;
        TextConfig textConfig;
        this.mTextBubbleList = new ArrayList();
        List<BaseDialogModel> dialogModelList = this.mDialogManager.getDialogModelList();
        for (int i = 0; i < dialogModelList.size(); i++) {
            BaseDialogModel baseDialogModel = dialogModelList.get(i);
            if (baseDialogModel != null && (str = baseDialogModel.name) != null) {
                if (str.equals(DialogManager.LocalDialog.SIGNATURE.name())) {
                    textConfig = new TextConfig((short) 0, new SignatureInfo(), baseDialogModel.name, baseDialogModel);
                } else {
                    textConfig = new TextConfig((short) 0, baseDialogModel.name, baseDialogModel);
                }
                this.mTextBubbleList.add(textConfig);
            }
        }
        if (this.mTextBubbleList.size() > 1) {
            this.mTextBubbleList.add(1, new TextConfig((short) 0, "", null, "", 1));
        }
    }

    public final void initTextWaterMarkData() {
        new LoadWatermarkTask(new ResourceListener() { // from class: com.miui.gallery.editor.photo.core.imports.text.TextProvider.1
            @Override // com.miui.gallery.editor.photo.core.imports.text.TextProvider.ResourceListener
            public void onLoadFinish(List<WatermarkInfo> list) {
                TextProvider.this.mTextWatermarkList = new ArrayList();
                for (WatermarkInfo watermarkInfo : list) {
                    TextProvider.this.mTextWatermarkList.add(new TextConfig((short) 0, watermarkInfo));
                }
                TextProvider.this.notifyInitializeFinish();
            }
        }, getApplicationContext().getAssets()).execute(getApplicationContext());
    }

    public final void initTextClassicData() {
        this.mTextClassicList = new ArrayList();
        List<BaseDialogModel> spotModelList = this.mDialogManager.getSpotModelList();
        for (int i = 0; i < spotModelList.size(); i++) {
            BaseDialogModel baseDialogModel = spotModelList.get(i);
            this.mTextClassicList.add(new TextConfig((short) 0, baseDialogModel.name, baseDialogModel));
        }
    }

    public final void initTextFestivalData() {
        this.mTextFestivalList = new ArrayList();
        List<BaseDialogModel> festivalModelList = this.mDialogManager.getFestivalModelList();
        for (int i = 0; i < festivalModelList.size(); i++) {
            BaseDialogModel baseDialogModel = festivalModelList.get(i);
            this.mTextFestivalList.add(new TextConfig((short) 0, baseDialogModel.name, baseDialogModel));
        }
    }

    public final void initTextSceneData() {
        this.mTextSceneList = new ArrayList();
        List<BaseDialogModel> sceneModelList = this.mDialogManager.getSceneModelList();
        for (int i = 0; i < sceneModelList.size(); i++) {
            BaseDialogModel baseDialogModel = sceneModelList.get(i);
            this.mTextSceneList.add(new TextConfig((short) 0, baseDialogModel.name, baseDialogModel));
        }
    }

    public final void initTextCityData() {
        this.mTextCityList = new ArrayList();
        List<BaseDialogModel> cityModelList = this.mDialogManager.getCityModelList();
        for (int i = 0; i < cityModelList.size(); i++) {
            BaseDialogModel baseDialogModel = cityModelList.get(i);
            this.mTextCityList.add(new TextConfig((short) 0, baseDialogModel.name, baseDialogModel));
        }
    }

    static {
        SdkManager.INSTANCE.register(new TextProvider());
    }

    @Override // com.miui.gallery.editor.photo.core.common.provider.AbstractTextProvider
    public List<TextData> listWatermark() {
        return this.mTextWatermarkList;
    }

    @Override // com.miui.gallery.editor.photo.core.common.provider.AbstractTextProvider
    public List<TextData> listSpot() {
        return this.mTextClassicList;
    }

    @Override // com.miui.gallery.editor.photo.core.common.provider.AbstractTextProvider
    public List<TextData> listFestival() {
        return this.mTextFestivalList;
    }

    @Override // com.miui.gallery.editor.photo.core.common.provider.AbstractTextProvider
    public List<TextData> listScene() {
        return this.mTextSceneList;
    }

    @Override // com.miui.gallery.editor.photo.core.common.provider.AbstractTextProvider
    public List<TextData> listCity() {
        return this.mTextCityList;
    }

    /* loaded from: classes2.dex */
    public static class LoadWatermarkTask extends AsyncTask<Application, Void, List<WatermarkInfo>> {
        public AssetManager mAssetManager;
        public ResourceListener mResourceListener;

        public LoadWatermarkTask(ResourceListener resourceListener, AssetManager assetManager) {
            this.mResourceListener = resourceListener;
            this.mAssetManager = assetManager;
        }

        @Override // android.os.AsyncTask
        public List<WatermarkInfo> doInBackground(Application... applicationArr) {
            String[] list;
            AssetManager assetManager = this.mAssetManager;
            ArrayList arrayList = new ArrayList();
            try {
                for (String str : assetManager.list("watermark")) {
                    WatermarkInfo watermarkInfo = (WatermarkInfo) GsonUtils.fromJson(IoUtils.loadAssetFileString(assetManager, getConfigPath(str)), (Class<Object>) WatermarkInfo.class);
                    watermarkInfo.icon = getAssetFilePath(str, watermarkInfo.icon);
                    watermarkInfo.bgPost = getAssetFilePath(str, watermarkInfo.bgPost);
                    arrayList.add(watermarkInfo);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return arrayList;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(List<WatermarkInfo> list) {
            ResourceListener resourceListener = this.mResourceListener;
            if (resourceListener != null) {
                resourceListener.onLoadFinish(list);
            }
        }

        public static String getConfigPath(String str) {
            StringBuilder sb = new StringBuilder();
            sb.append("watermark");
            String str2 = File.separator;
            sb.append(str2);
            sb.append(str);
            sb.append(str2);
            sb.append("config.json");
            return sb.toString();
        }

        public static String getAssetFilePath(String str, String str2) {
            if (TextUtils.isEmpty(str2)) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("assets://watermark");
            String str3 = File.separator;
            sb.append(str3);
            sb.append(str);
            sb.append(str3);
            sb.append(str2);
            return sb.toString();
        }
    }
}
