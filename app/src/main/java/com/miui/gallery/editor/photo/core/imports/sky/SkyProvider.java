package com.miui.gallery.editor.photo.core.imports.sky;

import android.content.Context;
import android.os.AsyncTask;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.RenderEngine;
import com.miui.gallery.editor.photo.core.SdkManager;
import com.miui.gallery.editor.photo.core.SdkProvider;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment;
import com.miui.gallery.editor.photo.core.common.model.SkyCategory;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.IoUtils;
import com.miui.gallery.util.ZipUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/* loaded from: classes2.dex */
public class SkyProvider extends SdkProvider<SkyCategory, AbstractEffectFragment> {
    public File mCacheFile;

    public SkyProvider() {
        super(Effect.SKY);
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public void onActivityCreate() {
        super.onActivityCreate();
        this.mCacheFile = new File(getApplicationContext().getFilesDir(), "/sky_text_cache/.fccache/");
        install();
    }

    public final void install() {
        new InstallTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public List<? extends SkyCategory> list() {
        return SkyCategoryImpl.getCategoryData();
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public AbstractEffectFragment onCreateFragment() {
        return new SkyRenderFragment();
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public RenderEngine createEngine(Context context) {
        return new SkyEngine();
    }

    public final boolean doInstall() {
        Throwable th;
        File file;
        Exception e;
        File file2 = null;
        try {
            try {
                if (!this.mCacheFile.exists()) {
                    file = new File(getApplicationContext().getCacheDir(), "fccache.zip");
                    try {
                        copyZip(file);
                        unzipData(file);
                        file2 = file;
                    } catch (Exception e2) {
                        e = e2;
                        DefaultLogger.w("SkyProvider", e);
                        if (file == null || !file.exists() || file.delete()) {
                            return false;
                        }
                        DefaultLogger.w("SkyProvider", "delete zip file failed");
                        return false;
                    }
                }
                if (file2 == null || !file2.exists() || file2.delete()) {
                    return false;
                }
            } catch (Throwable th2) {
                th = th2;
                if (0 != 0 && file2.exists() && !file2.delete()) {
                    DefaultLogger.w("SkyProvider", "delete zip file failed");
                }
                throw th;
            }
        } catch (Exception e3) {
            file = null;
            e = e3;
        } catch (Throwable th3) {
            th = th3;
            if (0 != 0) {
                DefaultLogger.w("SkyProvider", "delete zip file failed");
            }
            throw th;
        }
        DefaultLogger.w("SkyProvider", "delete zip file failed");
        return false;
    }

    public final void copyZip(File file) {
        FileOutputStream fileOutputStream;
        InputStream open;
        InputStream inputStream = null;
        try {
            open = getApplicationContext().getAssets().open("fccache.zip");
            try {
                fileOutputStream = new FileOutputStream(file);
            } catch (IOException unused) {
                fileOutputStream = null;
            } catch (Throwable th) {
                th = th;
                fileOutputStream = null;
            }
        } catch (IOException unused2) {
            fileOutputStream = null;
        } catch (Throwable th2) {
            th = th2;
            fileOutputStream = null;
        }
        try {
            byte[] bArr = new byte[4096];
            while (true) {
                int read = open.read(bArr);
                if (read == -1) {
                    break;
                }
                fileOutputStream.write(bArr, 0, read);
            }
            fileOutputStream.flush();
            IoUtils.close("SkyProvider", open);
        } catch (IOException unused3) {
            inputStream = open;
            IoUtils.close("SkyProvider", inputStream);
            IoUtils.close("SkyProvider", fileOutputStream);
        } catch (Throwable th3) {
            th = th3;
            inputStream = open;
            IoUtils.close("SkyProvider", inputStream);
            IoUtils.close("SkyProvider", fileOutputStream);
            throw th;
        }
        IoUtils.close("SkyProvider", fileOutputStream);
    }

    public final void unzipData(File file) {
        long currentTimeMillis = System.currentTimeMillis();
        if (file.exists() && this.mCacheFile.mkdirs()) {
            try {
                ZipUtils.unzip(file, this.mCacheFile);
                DefaultLogger.d("SkyProvider", "unzip files finish, costs %dms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
            } catch (IOException unused) {
                DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(this.mCacheFile.getAbsolutePath(), IStoragePermissionStrategy.Permission.DELETE_DIRECTORY, FileHandleRecordHelper.appendInvokerTag("SkyProvider", "unzipData"));
                if (documentFile == null) {
                    return;
                }
                documentFile.delete();
            }
        }
    }

    /* loaded from: classes2.dex */
    public class InstallTask extends AsyncTask<Void, Void, Boolean> {
        public InstallTask() {
        }

        @Override // android.os.AsyncTask
        public Boolean doInBackground(Void... voidArr) {
            return Boolean.valueOf(SkyProvider.this.doInstall());
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Boolean bool) {
            super.onPostExecute((InstallTask) bool);
            SkyProvider.this.notifyInitializeFinish();
        }
    }

    static {
        SdkManager.INSTANCE.register(new SkyProvider());
    }
}
