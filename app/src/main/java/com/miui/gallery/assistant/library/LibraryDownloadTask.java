package com.miui.gallery.assistant.library;

import android.annotation.SuppressLint;
import com.miui.gallery.assistant.library.Library;
import com.miui.gallery.assistant.library.LibraryManager;
import com.miui.gallery.card.CardManager;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.pendingtask.PendingTaskManager;
import com.miui.gallery.pendingtask.base.PendingTask;
import com.miui.gallery.util.GsonUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.micloudsdk.request.utils.CloudUtils;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;

/* loaded from: classes.dex */
public class LibraryDownloadTask extends PendingTask<JSONObject> {
    @SuppressLint({"UseSparseArrays"})
    public final Map<Long, Future> mDownloadFutures;
    public final LibraryManager.DownloadListener mDownloadListener;
    public JSONObject mOriginalData;
    public int mOriginalType;

    @Override // com.miui.gallery.pendingtask.base.PendingTask
    public int getNetworkType() {
        return 1;
    }

    @Override // com.miui.gallery.pendingtask.base.PendingTask
    public boolean requireCharging() {
        return false;
    }

    @Override // com.miui.gallery.pendingtask.base.PendingTask
    public boolean requireDeviceIdle() {
        return false;
    }

    public LibraryDownloadTask(int i) {
        super(i);
        this.mDownloadFutures = Collections.synchronizedMap(new HashMap());
        this.mDownloadListener = new LibraryManager.DownloadListener() { // from class: com.miui.gallery.assistant.library.LibraryDownloadTask.1
            @Override // com.miui.gallery.assistant.library.LibraryManager.DownloadListener
            public void onDownloadProgress(long j, int i2) {
            }

            @Override // com.miui.gallery.assistant.library.LibraryManager.DownloadListener
            public void onDownloadResult(long j, int i2) {
                LibraryDownloadTask.this.mDownloadFutures.remove(Long.valueOf(j));
                if (i2 == 0 && LibraryDownloadTask.this.mDownloadFutures.size() == 0 && LibraryDownloadTask.this.mOriginalType != -1) {
                    DefaultLogger.d("LibraryDownloadTask", "LibraryDownloadTask download success!");
                    PendingTaskManager.getInstance().postTask(LibraryDownloadTask.this.mOriginalType, LibraryDownloadTask.this.mOriginalData);
                    if (!LibraryManager.getInstance().isLibrarysExist(LibraryConstantsHelper.sStoryLibraries) || CloudUtils.getXiaomiAccount() != null) {
                        return;
                    }
                    CardManager.getInstance().triggerGuaranteeScenarios(false);
                }
            }
        };
    }

    @Override // com.miui.gallery.pendingtask.base.PendingTask
    /* renamed from: parseData  reason: avoid collision after fix types in other method */
    public JSONObject mo1252parseData(byte[] bArr) throws Exception {
        return new JSONObject(new String(bArr, "utf-8"));
    }

    @Override // com.miui.gallery.pendingtask.base.PendingTask
    public byte[] wrapData(JSONObject jSONObject) throws Exception {
        return jSONObject.toString().getBytes("utf-8");
    }

    @Override // com.miui.gallery.pendingtask.base.PendingTask
    public boolean process(JSONObject jSONObject) throws Exception {
        if (jSONObject != null) {
            try {
                this.mOriginalType = jSONObject.optInt("originalType");
                this.mOriginalData = jSONObject.optJSONObject("originalData");
                long[] jArr = (long[]) GsonUtils.fromJson(jSONObject.getString("libraryIds"), (Class<Object>) long[].class);
                if (jArr != null && jArr.length > 0) {
                    for (long j : jArr) {
                        Library librarySync = LibraryManager.getInstance().getLibrarySync(j);
                        if (librarySync != null && librarySync.getLibraryStatus() == Library.LibraryStatus.STATE_NOT_DOWNLOADED) {
                            this.mDownloadFutures.put(Long.valueOf(j), LibraryManager.getInstance().downloadLibrary(librarySync, false, this.mDownloadListener));
                        }
                    }
                }
            } catch (Exception e) {
                DefaultLogger.e("LibraryDownloadTask", e);
            }
        }
        return false;
    }
}
