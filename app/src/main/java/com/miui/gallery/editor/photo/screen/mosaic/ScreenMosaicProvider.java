package com.miui.gallery.editor.photo.screen.mosaic;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import com.miui.gallery.editor.photo.screen.core.ScreenProvider;
import com.miui.gallery.editor.photo.screen.mosaic.shader.MosaicData;
import com.miui.gallery.editor.photo.screen.mosaic.shader.MosaicEntity;
import com.miui.gallery.editor.photo.screen.mosaic.shader.MosaicEntityBitmap;
import com.miui.gallery.editor.photo.screen.mosaic.shader.MosaicEntityBlur;
import com.miui.gallery.editor.photo.screen.mosaic.shader.MosaicEntityNormal;
import com.miui.gallery.editor.photo.screen.mosaic.shader.MosaicEntityOrigin;
import com.miui.gallery.editor.photo.screen.mosaic.shader.MosaicEntityTriangle;
import com.miui.gallery.editor.photo.screen.mosaic.shader.MosaicEntityTriangleRect;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.GsonUtils;
import com.miui.gallery.util.IoUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class ScreenMosaicProvider extends ScreenProvider {
    public static final String PATH_MOSAIC;
    public static final String SEPARATOR;
    public List<MosaicData> mMosaicDataList = new ArrayList();

    /* loaded from: classes2.dex */
    public interface ResourceListener {
        void onLoadFinish(List<MosaicEntity> list);
    }

    public static /* synthetic */ void $r8$lambda$5KpNWdvb3qBh2ieWgjiZWQSa8yM(ScreenMosaicProvider screenMosaicProvider, List list) {
        screenMosaicProvider.lambda$initialize$0(list);
    }

    @Override // com.miui.gallery.editor.photo.screen.core.ScreenProvider
    public void onActivityDestroy() {
    }

    static {
        String str = File.separator;
        SEPARATOR = str;
        PATH_MOSAIC = "screen_mosaic" + str + "entities";
    }

    public List<? extends MosaicData> list() {
        return this.mMosaicDataList;
    }

    @Override // com.miui.gallery.editor.photo.screen.core.ScreenProvider
    public void onActivityCreate(Context context) {
        initialize(context);
    }

    public MosaicData getDefaultData() {
        if (BaseMiscUtil.isValid(this.mMosaicDataList)) {
            for (MosaicData mosaicData : this.mMosaicDataList) {
                if (mosaicData instanceof MosaicEntityNormal) {
                    return mosaicData;
                }
            }
            return null;
        }
        return null;
    }

    public final void initialize(Context context) {
        if (this.mIsInitialized) {
            return;
        }
        new LoadMosaicTask(new ResourceListener() { // from class: com.miui.gallery.editor.photo.screen.mosaic.ScreenMosaicProvider$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.editor.photo.screen.mosaic.ScreenMosaicProvider.ResourceListener
            public final void onLoadFinish(List list) {
                ScreenMosaicProvider.$r8$lambda$5KpNWdvb3qBh2ieWgjiZWQSa8yM(ScreenMosaicProvider.this, list);
            }
        }, context.getApplicationContext()).execute(new Void[0]);
    }

    public /* synthetic */ void lambda$initialize$0(List list) {
        this.mMosaicDataList.clear();
        this.mMosaicDataList.addAll(list);
        this.mIsInitialized = true;
    }

    public void clearShader() {
        Iterator<MosaicData> it = this.mMosaicDataList.iterator();
        while (it.hasNext()) {
            ((MosaicEntity) it.next()).clearShader();
        }
    }

    /* loaded from: classes2.dex */
    public static class LoadMosaicTask extends AsyncTask<Void, Void, List<MosaicEntity>> {
        public Context mContext;
        public ResourceListener mResourceListener;

        public LoadMosaicTask(ResourceListener resourceListener, Context context) {
            this.mResourceListener = resourceListener;
            this.mContext = context;
        }

        @Override // android.os.AsyncTask
        public List<MosaicEntity> doInBackground(Void... voidArr) {
            String[] list;
            MosaicData mosaicEntityOrigin;
            AssetManager assets = this.mContext.getAssets();
            ArrayList arrayList = new ArrayList();
            try {
                for (String str : assets.list(ScreenMosaicProvider.PATH_MOSAIC)) {
                    String mosaicConfigPath = ScreenMosaicProvider.getMosaicConfigPath(str);
                    String mosaicIconPath = ScreenMosaicProvider.getMosaicIconPath(str);
                    ScreenMosaicConfig screenMosaicConfig = (ScreenMosaicConfig) GsonUtils.fromJson(ScreenMosaicProvider.loadResourceFileString(assets, mosaicConfigPath), (Class<Object>) ScreenMosaicConfig.class);
                    if (screenMosaicConfig.supportScreenEditor) {
                        int i = AnonymousClass1.$SwitchMap$com$miui$gallery$editor$photo$screen$mosaic$shader$MosaicEntity$TYPE[screenMosaicConfig.effectType.ordinal()];
                        if (i == 1) {
                            mosaicEntityOrigin = new MosaicEntityOrigin(str, mosaicIconPath);
                        } else if (i == 2) {
                            mosaicEntityOrigin = new MosaicEntityBitmap(str, mosaicIconPath, ScreenMosaicProvider.getMosaicResourcePath(str), screenMosaicConfig.tileMode);
                        } else if (i == 3) {
                            mosaicEntityOrigin = new MosaicEntityBlur(str, mosaicIconPath, this.mContext);
                        } else if (i == 4) {
                            mosaicEntityOrigin = new MosaicEntityTriangle(str, mosaicIconPath);
                        } else if (i == 5) {
                            mosaicEntityOrigin = new MosaicEntityTriangleRect(str, mosaicIconPath);
                        } else {
                            mosaicEntityOrigin = new MosaicEntityNormal(str, mosaicIconPath);
                        }
                        arrayList.add(mosaicEntityOrigin);
                        if (arrayList.size() >= 6) {
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return arrayList;
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(List<MosaicEntity> list) {
            ResourceListener resourceListener = this.mResourceListener;
            if (resourceListener != null) {
                resourceListener.onLoadFinish(list);
            }
        }
    }

    /* renamed from: com.miui.gallery.editor.photo.screen.mosaic.ScreenMosaicProvider$1 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$editor$photo$screen$mosaic$shader$MosaicEntity$TYPE;

        static {
            int[] iArr = new int[MosaicEntity.TYPE.values().length];
            $SwitchMap$com$miui$gallery$editor$photo$screen$mosaic$shader$MosaicEntity$TYPE = iArr;
            try {
                iArr[MosaicEntity.TYPE.ORIGIN.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$screen$mosaic$shader$MosaicEntity$TYPE[MosaicEntity.TYPE.BITMAP.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$screen$mosaic$shader$MosaicEntity$TYPE[MosaicEntity.TYPE.BLUR.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$screen$mosaic$shader$MosaicEntity$TYPE[MosaicEntity.TYPE.TRIANGLE.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$screen$mosaic$shader$MosaicEntity$TYPE[MosaicEntity.TYPE.TRIANGLE_RECT.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$miui$gallery$editor$photo$screen$mosaic$shader$MosaicEntity$TYPE[MosaicEntity.TYPE.NORMAL.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
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

    public static String getMosaicResourcePath(String str) {
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
                    str2 = IoUtils.readInputStreamToString("ScreenMosaicProvider", inputStream);
                } catch (IOException e) {
                    e = e;
                    DefaultLogger.e("ScreenMosaicProvider", e);
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
