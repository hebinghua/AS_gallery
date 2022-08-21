package com.miui.gallery.editor.photo.core.imports.sticker;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.LongSparseArray;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.RenderEngine;
import com.miui.gallery.editor.photo.core.SdkManager;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment;
import com.miui.gallery.editor.photo.core.common.model.StickerCategory;
import com.miui.gallery.editor.photo.core.common.model.StickerData;
import com.miui.gallery.editor.photo.core.common.provider.AbstractStickerProvider;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.IoUtils;
import com.miui.gallery.util.ZipUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.settings.Settings;
import com.xiaomi.stat.a;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class StickerProvider extends AbstractStickerProvider {
    public static final StickerProvider INSTANCE;
    public Map<StickerItem, WeakReference<Bitmap>> mCache;
    public LongSparseArray<CategoryData> mCategories;
    public Deque<StickerItem> mHistory;
    public File mIndexFile;
    public File mRecentFile;

    static {
        StickerProvider stickerProvider = new StickerProvider();
        INSTANCE = stickerProvider;
        SdkManager.INSTANCE.register(stickerProvider);
    }

    public StickerProvider() {
        super(Effect.STICKER);
        this.mHistory = new LinkedList();
        this.mCategories = new LongSparseArray<>();
        this.mCache = new ArrayMap();
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public void onActivityCreate() {
        super.onActivityCreate();
        DefaultLogger.d("StickerProvider", "perform application creating, install stickers");
        this.mIndexFile = new File(getApplicationContext().getFilesDir(), "photo_editor/stickers/package.json");
        this.mRecentFile = new File(getApplicationContext().getFilesDir(), "photo_editor/stickers/recent.json");
        install();
    }

    public void install() {
        new InstallTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public final boolean doInstall() {
        JSONObject jSONObject;
        boolean z;
        File file = null;
        try {
            try {
                if (this.mIndexFile.exists()) {
                    jSONObject = new JSONObject(loadPackageInfo());
                    z = !checkResourceVersion(jSONObject);
                } else {
                    DefaultLogger.d("StickerProvider", "index file not exist");
                    jSONObject = null;
                    z = true;
                }
                if (z) {
                    File file2 = new File(getApplicationContext().getCacheDir(), "stickers.zip");
                    try {
                        releaseZip(file2);
                        unzipData(file2);
                        if (this.mRecentFile.exists() && !this.mRecentFile.delete()) {
                            DefaultLogger.d("StickerProvider", "delete recent stickers failed");
                        }
                        jSONObject = new JSONObject(loadPackageInfo());
                        file = file2;
                    } catch (InitializeException e) {
                        e = e;
                        file = file2;
                        DefaultLogger.w("StickerProvider", e);
                        deleteIndexFile();
                        if (file == null || !file.exists() || file.delete()) {
                            return false;
                        }
                        DefaultLogger.w("StickerProvider", "delete zip file failed");
                        return false;
                    } catch (JSONException e2) {
                        e = e2;
                        file = file2;
                        DefaultLogger.w("StickerProvider", e);
                        deleteIndexFile();
                        if (file == null || !file.exists() || file.delete()) {
                            return false;
                        }
                        DefaultLogger.w("StickerProvider", "delete zip file failed");
                        return false;
                    } catch (Throwable th) {
                        th = th;
                        file = file2;
                        if (file != null && file.exists() && !file.delete()) {
                            DefaultLogger.w("StickerProvider", "delete zip file failed");
                        }
                        throw th;
                    }
                }
                long currentTimeMillis = System.currentTimeMillis();
                loadData(jSONObject);
                readRecentFromFile();
                DefaultLogger.d("StickerProvider", "load package info costs %dms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
                if (file != null && file.exists() && !file.delete()) {
                    DefaultLogger.w("StickerProvider", "delete zip file failed");
                }
                return true;
            } catch (Throwable th2) {
                th = th2;
            }
        } catch (InitializeException e3) {
            e = e3;
        } catch (JSONException e4) {
            e = e4;
        }
    }

    public final boolean checkResourceVersion(JSONObject jSONObject) {
        boolean z = false;
        try {
            if (jSONObject.getInt("version") == 15) {
                z = true;
            }
        } catch (JSONException e) {
            DefaultLogger.w("StickerProvider", e);
        }
        if (!z) {
            String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("StickerProvider", "checkResourceVersion");
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(getApplicationContext().getFilesDir() + File.separator + "photo_editor/stickers/", IStoragePermissionStrategy.Permission.DELETE_DIRECTORY, appendInvokerTag);
            if (documentFile != null) {
                documentFile.delete();
            }
        }
        return z;
    }

    public final void deleteIndexFile() {
        if (!this.mIndexFile.exists()) {
            DefaultLogger.d("StickerProvider", "index file not found");
        } else if (this.mIndexFile.delete()) {
        } else {
            DefaultLogger.d("StickerProvider", "delete index file failed");
        }
    }

    public final void releaseZip(File file) throws InitializeException {
        FileOutputStream fileOutputStream;
        InputStream inputStream = null;
        try {
            try {
                InputStream open = getApplicationContext().getAssets().open("stickers.zip");
                try {
                    try {
                        fileOutputStream = new FileOutputStream(file);
                        try {
                            byte[] bArr = new byte[4096];
                            while (true) {
                                try {
                                    int read = open.read(bArr);
                                    if (read != -1) {
                                        fileOutputStream.write(bArr, 0, read);
                                    } else {
                                        fileOutputStream.flush();
                                        IoUtils.close("StickerProvider", open);
                                        IoUtils.close("StickerProvider", fileOutputStream);
                                        return;
                                    }
                                } catch (IOException e) {
                                    throw new InitializeException("release failed during release zip file", e);
                                }
                            }
                        } catch (Throwable th) {
                            th = th;
                            inputStream = open;
                            IoUtils.close("StickerProvider", inputStream);
                            IoUtils.close("StickerProvider", fileOutputStream);
                            throw th;
                        }
                    } catch (FileNotFoundException e2) {
                        throw new InitializeException("release assets's zip file failed", e2);
                    }
                } catch (Throwable th2) {
                    th = th2;
                    fileOutputStream = null;
                }
            } catch (Throwable th3) {
                th = th3;
                fileOutputStream = null;
            }
        } catch (IOException e3) {
            throw new InitializeException("missing assets file", e3);
        }
    }

    public final void unzipData(File file) throws InitializeException {
        long currentTimeMillis = System.currentTimeMillis();
        if (!file.exists()) {
            throw new InitializeException("resource missing");
        }
        File file2 = new File(getApplicationContext().getFilesDir(), "photo_editor/stickers/");
        if (file2.exists()) {
            String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("StickerProvider", "unzipData");
            StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
            DocumentFile documentFile = storageStrategyManager.getDocumentFile(getApplicationContext().getFilesDir() + File.separator + "photo_editor/stickers/", IStoragePermissionStrategy.Permission.DELETE_DIRECTORY, appendInvokerTag);
            if (documentFile != null) {
                documentFile.delete();
            }
        }
        if (file2.exists()) {
            DefaultLogger.w("StickerProvider", "delete folder failed");
        } else if (!file2.mkdirs()) {
            throw new InitializeException("create folder stickers/ failed");
        }
        try {
            ZipUtils.unzip(file, file2);
            DefaultLogger.d("StickerProvider", "unzip files finish, costs %dms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        } catch (IOException e) {
            throw new InitializeException("copy directory failed", e);
        }
    }

    public final String loadPackageInfo() throws InitializeException {
        FileReader fileReader;
        StringBuilder sb = new StringBuilder();
        FileReader fileReader2 = null;
        try {
            try {
                fileReader = new FileReader(this.mIndexFile);
            } catch (Throwable th) {
                th = th;
            }
        } catch (FileNotFoundException unused) {
        } catch (IOException e) {
            e = e;
        }
        try {
            char[] cArr = new char[1024];
            while (fileReader.read(cArr) != -1) {
                sb.append(cArr);
            }
            String sb2 = sb.toString();
            DefaultLogger.d("StickerProvider", "load pkg info finished");
            try {
                fileReader.close();
            } catch (IOException e2) {
                DefaultLogger.w("StickerProvider", e2);
            }
            return sb2;
        } catch (FileNotFoundException unused2) {
            throw new InitializeException("index file is not found");
        } catch (IOException e3) {
            e = e3;
            throw new InitializeException("read index file failed", e);
        } catch (Throwable th2) {
            th = th2;
            fileReader2 = fileReader;
            if (fileReader2 != null) {
                try {
                    fileReader2.close();
                } catch (IOException e4) {
                    DefaultLogger.w("StickerProvider", e4);
                }
            }
            throw th;
        }
    }

    public final void loadData(JSONObject jSONObject) throws InitializeException {
        String str;
        String str2;
        String str3;
        String str4 = "com.miui.gallery";
        try {
            JSONArray jSONArray = jSONObject.getJSONArray("categories");
            String region = Settings.getRegion();
            int i = 10;
            int i2 = 8;
            Resources resources = getApplicationContext().getResources();
            int i3 = 0;
            while (i3 < jSONArray.length()) {
                JSONObject jSONObject2 = jSONArray.getJSONObject(i3);
                String optString = jSONObject2.optString("region");
                if (TextUtils.isEmpty(optString) || optString.equalsIgnoreCase(region)) {
                    String string = jSONObject2.getString("name");
                    String string2 = jSONObject2.getString("talkback");
                    if (string.startsWith("@drawable/")) {
                        String substring = string.substring(i);
                        if (string2.startsWith("@string/")) {
                            String substring2 = string2.substring(i2);
                            int i4 = jSONObject2.getInt("id");
                            if (resources.getIdentifier(substring, "drawable", str4) == 0) {
                                DefaultLogger.e("StickerProvider", "not valid category name string resource:" + substring);
                            } else {
                                int identifier = resources.getIdentifier(substring2, "string", str4);
                                if (identifier != 0) {
                                    JSONArray jSONArray2 = jSONObject2.getJSONArray("items");
                                    ArrayList arrayList = new ArrayList();
                                    int i5 = 0;
                                    while (i5 < jSONArray2.length()) {
                                        JSONObject jSONObject3 = jSONArray2.getJSONObject(i5);
                                        int i6 = jSONObject3.getInt("id");
                                        String string3 = jSONObject3.getString(a.d);
                                        if (Uri.parse(string3).isRelative()) {
                                            str2 = str4;
                                            File file = new File(this.mIndexFile.getParentFile(), string3);
                                            if (file.exists()) {
                                                str3 = file.getPath();
                                            } else {
                                                throw new InitializeException(String.format("main url is not found %s", string3));
                                            }
                                        } else {
                                            str2 = str4;
                                            str3 = string3;
                                        }
                                        arrayList.add(new StickerItem((short) 0, null, i6, str3, str3, substring));
                                        i5++;
                                        str4 = str2;
                                    }
                                    str = str4;
                                    this.mCategories.put(i4, new CategoryData(i4, (short) -1, substring, resources.getString(identifier), arrayList));
                                }
                            }
                        }
                    } else {
                        str = str4;
                        DefaultLogger.e("StickerProvider", "not string type category name: " + string);
                    }
                    i3++;
                    str4 = str;
                    i = 10;
                    i2 = 8;
                }
                str = str4;
                i3++;
                str4 = str;
                i = 10;
                i2 = 8;
            }
        } catch (JSONException e) {
            throw new InitializeException("package info missing", e);
        }
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public List<? extends StickerCategory> list() {
        ArrayList arrayList = new ArrayList(this.mCategories.size());
        for (int i = 0; i < this.mCategories.size(); i++) {
            arrayList.add(this.mCategories.valueAt(i));
        }
        return arrayList;
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public AbstractEffectFragment onCreateFragment() {
        return new StickerFragment();
    }

    @Override // com.miui.gallery.editor.photo.core.SdkProvider
    public RenderEngine createEngine(Context context) {
        return new StickerEngine();
    }

    @Override // com.miui.gallery.editor.photo.core.common.provider.AbstractStickerProvider
    public List<StickerData> list(long j) {
        return this.mCategories.get(j).stickerList;
    }

    @Override // com.miui.gallery.editor.photo.core.common.provider.AbstractStickerProvider
    public List<StickerData> recent() {
        return new ArrayList(this.mHistory);
    }

    public void touch(StickerItem stickerItem) {
        if (this.mHistory.contains(stickerItem)) {
            this.mHistory.remove(stickerItem);
        }
        this.mHistory.push(stickerItem);
    }

    public void writeRecentToFile() {
        if (this.mHistory.isEmpty()) {
            return;
        }
        long currentTimeMillis = System.currentTimeMillis();
        Writer openWriter = IoUtils.openWriter(this.mRecentFile);
        if (openWriter == null) {
            DefaultLogger.w("StickerProvider", "open recent file failed.");
        }
        JSONArray jSONArray = new JSONArray();
        int i = 0;
        for (StickerItem stickerItem : this.mHistory) {
            JSONObject json = StickerItem.toJson(stickerItem);
            if (json == null) {
                DefaultLogger.w("StickerProvider", "jsonfy failed");
            } else {
                jSONArray.put(json);
                i++;
            }
            if (i >= 10) {
                break;
            }
        }
        DefaultLogger.d("StickerProvider", "write %d recent items to file", Integer.valueOf(i));
        try {
            try {
                openWriter.write(jSONArray.toString());
            } catch (IOException e) {
                DefaultLogger.w("StickerProvider", e);
            }
            DefaultLogger.d("StickerProvider", "write to file costs %dms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        } finally {
            IoUtils.close("StickerProvider", openWriter);
        }
    }

    public Bitmap fromCache(StickerItem stickerItem) {
        WeakReference<Bitmap> weakReference = this.mCache.get(stickerItem);
        if (weakReference == null) {
            return null;
        }
        return weakReference.get();
    }

    public void putToCache(StickerItem stickerItem, Bitmap bitmap) {
        this.mCache.put(stickerItem, new WeakReference<>(bitmap));
    }

    public final void readRecentFromFile() {
        Reader openReader = IoUtils.openReader(this.mRecentFile);
        if (openReader == null) {
            return;
        }
        try {
            try {
                char[] cArr = new char[128];
                StringBuilder sb = new StringBuilder();
                while (true) {
                    int read = openReader.read(cArr);
                    if (read == -1) {
                        break;
                    }
                    sb.append(cArr, 0, read);
                }
                fillRecent(new JSONArray(sb.toString()));
            } catch (IOException e) {
                DefaultLogger.w("StickerProvider", e);
            } catch (JSONException e2) {
                DefaultLogger.w("StickerProvider", e2);
            }
        } finally {
            IoUtils.close("StickerProvider", openReader);
        }
    }

    public final void fillRecent(JSONArray jSONArray) {
        for (int i = 0; i < jSONArray.length(); i++) {
            try {
                this.mHistory.add(StickerItem.fromJson(jSONArray.getJSONObject(i)));
            } catch (JSONException e) {
                DefaultLogger.w("StickerProvider", e);
            }
        }
        DefaultLogger.d("StickerProvider", "read %d recent items from file", Integer.valueOf(this.mHistory.size()));
    }

    /* loaded from: classes2.dex */
    public static class InitializeException extends Exception {
        public InitializeException(String str) {
            super(str);
        }

        public InitializeException(String str, Throwable th) {
            super(str, th);
        }
    }

    /* loaded from: classes2.dex */
    public class InstallTask extends AsyncTask<Void, Void, Boolean> {
        public InstallTask() {
        }

        @Override // android.os.AsyncTask
        public Boolean doInBackground(Void... voidArr) {
            return Boolean.valueOf(StickerProvider.this.doInstall());
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Boolean bool) {
            super.onPostExecute((InstallTask) bool);
            DefaultLogger.d("StickerProvider", "stickers installed: %d categories", Integer.valueOf(StickerProvider.this.mCategories.size()));
            StickerProvider.this.notifyInitializeFinish();
        }
    }
}
