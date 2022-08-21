package com.miui.gallery.editor.photo.core.imports.mosaic;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.RenderEngine;
import com.miui.gallery.editor.photo.core.SdkManager;
import com.miui.gallery.editor.photo.core.SdkProvider;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractMosaicFragment;
import com.miui.gallery.editor.photo.core.common.model.MosaicData;
import com.miui.gallery.editor.photo.core.imports.mosaic.MosaicGLEntity;
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
public class MosaicProvider extends SdkProvider<MosaicData, AbstractMosaicFragment> {
    public static final String PATH_MOSAIC;
    public static final String PEN_MASK_PATH;
    public static final String SEPARATOR;
    public List<MosaicData> mMosaicDataList;

    /* loaded from: classes2.dex */
    public interface ResourceListener {
        void onLoadFinish(List<MosaicGLEntity> list);
    }

    static {
        String str = File.separator;
        SEPARATOR = str;
        PATH_MOSAIC = "mosaic" + str + "entities";
        PEN_MASK_PATH = "mosaic" + str + "pen" + str + "pen_mask.png";
        SdkManager.INSTANCE.register(new MosaicProvider());
    }

    private MosaicProvider() {
        super(Effect.MOSAIC);
        this.mMosaicDataList = new ArrayList();
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public List<? extends MosaicData> list() {
        return this.mMosaicDataList;
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public void onActivityCreate() {
        super.onActivityCreate();
        initialize();
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public AbstractMosaicFragment onCreateFragment() {
        return new MosaicFragment();
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public RenderEngine createEngine(Context context) {
        return new MosaicEngine();
    }

    public final void initialize() {
        new LoadMosaicTask(new ResourceListener() { // from class: com.miui.gallery.editor.photo.core.imports.mosaic.MosaicProvider.1
            @Override // com.miui.gallery.editor.photo.core.imports.mosaic.MosaicProvider.ResourceListener
            public void onLoadFinish(List<MosaicGLEntity> list) {
                MosaicProvider.this.mMosaicDataList.clear();
                MosaicProvider.this.mMosaicDataList.addAll(list);
                MosaicProvider.this.notifyInitializeFinish();
            }
        }, getApplicationContext().getAssets()).execute(getApplicationContext());
    }

    /* loaded from: classes2.dex */
    public static class LoadMosaicTask extends AsyncTask<Application, Void, List<MosaicGLEntity>> {
        public AssetManager mAssetManager;
        public ResourceListener mResourceListener;

        public LoadMosaicTask(ResourceListener resourceListener, AssetManager assetManager) {
            this.mResourceListener = resourceListener;
            this.mAssetManager = assetManager;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r10v1, types: [com.miui.gallery.editor.photo.core.imports.mosaic.MosaicGLEffectEntity] */
        /* JADX WARN: Type inference failed for: r11v0, types: [com.miui.gallery.editor.photo.core.imports.mosaic.MosaicGLResourceEntity] */
        @Override // android.os.AsyncTask
        public List<MosaicGLEntity> doInBackground(Application... applicationArr) {
            AssetManager assetManager = this.mAssetManager;
            ArrayList arrayList = new ArrayList();
            try {
                String[] list = assetManager.list(MosaicProvider.PATH_MOSAIC);
                for (int i = 0; i < list.length; i++) {
                    String str = list[i];
                    String mosaicConfigPath = MosaicProvider.getMosaicConfigPath(str);
                    String mosaicIconPath = MosaicProvider.getMosaicIconPath(str);
                    MosaicConfig mosaicConfig = (MosaicConfig) GsonUtils.fromJson(MosaicProvider.loadResourceFileString(assetManager, mosaicConfigPath), (Class<Object>) MosaicConfig.class);
                    MosaicGLOriginEntity mosaicGLOriginEntity = null;
                    int i2 = AnonymousClass2.$SwitchMap$com$miui$gallery$editor$photo$core$imports$mosaic$MosaicGLEntity$TYPE[mosaicConfig.type.ordinal()];
                    if (i2 == 1) {
                        mosaicGLOriginEntity = new MosaicGLOriginEntity((short) i, str, mosaicIconPath, mosaicConfig.talkbackName);
                    } else if (i2 == 2) {
                        mosaicGLOriginEntity = new MosaicGLResourceEntity((short) i, str, mosaicIconPath, MosaicProvider.getMosaicResourceAssetPath(str), mosaicConfig.tileMode, mosaicConfig.talkbackName);
                    } else if (i2 == 3) {
                        mosaicGLOriginEntity = new MosaicGLEffectEntity((short) i, str, mosaicIconPath, mosaicConfig.effectType, mosaicConfig.talkbackName);
                    }
                    arrayList.add(mosaicGLOriginEntity);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return arrayList;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(List<MosaicGLEntity> list) {
            ResourceListener resourceListener = this.mResourceListener;
            if (resourceListener != null) {
                resourceListener.onLoadFinish(list);
            }
        }
    }

    /* renamed from: com.miui.gallery.editor.photo.core.imports.mosaic.MosaicProvider$2  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass2 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$editor$photo$core$imports$mosaic$MosaicGLEntity$TYPE;

        static {
            int[] iArr = new int[MosaicGLEntity.TYPE.values().length];
            $SwitchMap$com$miui$gallery$editor$photo$core$imports$mosaic$MosaicGLEntity$TYPE = iArr;
            try {
                iArr[MosaicGLEntity.TYPE.ORIGIN.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$mosaic$MosaicGLEntity$TYPE[MosaicGLEntity.TYPE.RESOURCE.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$core$imports$mosaic$MosaicGLEntity$TYPE[MosaicGLEntity.TYPE.EFFECT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public static String getMosaicConfigPath(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(PATH_MOSAIC);
        String str2 = SEPARATOR;
        sb.append(str2);
        sb.append(str);
        sb.append(str2);
        sb.append("config.json");
        return sb.toString();
    }

    public static String getMosaicIconPath(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("assets://");
        sb.append(PATH_MOSAIC);
        String str2 = SEPARATOR;
        sb.append(str2);
        sb.append(str);
        sb.append(str2);
        sb.append("icon.png");
        return sb.toString();
    }

    public static String getMosaicResourceAssetPath(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("assets://");
        sb.append(PATH_MOSAIC);
        String str2 = SEPARATOR;
        sb.append(str2);
        sb.append(str);
        sb.append(str2);
        sb.append("resource.png");
        return sb.toString();
    }

    public static String loadResourceFileString(AssetManager assetManager, String str) {
        InputStream inputStream;
        InputStream inputStream2 = null;
        String str2 = null;
        try {
            inputStream = assetManager.open(str);
            try {
                try {
                    str2 = IoUtils.readInputStreamToString("MosaicProvider", inputStream);
                } catch (IOException e) {
                    e = e;
                    DefaultLogger.e("MosaicProvider", e);
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
