package com.miui.gallery.ui.share;

import android.net.Uri;
import android.os.AsyncTask;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.editor.photo.sdk.CleanScheduler;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.ui.share.PrepareItem;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/* loaded from: classes2.dex */
public class PrepareTask<T extends PrepareItem> extends AsyncTask<ArrayList<T>, Integer, ArrayList<T>> {
    public final WeakReference<FragmentActivity> mActivityRef;
    public int mCurProgress;
    public final HashMap<Integer, PrepareFunc> mFuncs = new HashMap<>();
    public volatile int mHasPreparedSize;
    public float mItemMaxProgress;
    public ArrayList<T> mItemsHolder;
    public OnPrepareListener mListener;
    public final int mMaxProgress;
    public boolean mPaused;

    /* loaded from: classes2.dex */
    public interface OnPrepareListener<T extends PrepareItem> {
        void onCancelled(ArrayList<T> arrayList);

        void onPrepared(ArrayList<T> arrayList);

        void onProgressUpdate(int i);

        void onStarted(ArrayList<T> arrayList);
    }

    public static /* synthetic */ void $r8$lambda$sgvEdSpZYxswYhLMuxBPdIOf1DU(PrepareTask prepareTask, PrepareItem prepareItem, float f) {
        prepareTask.lambda$invokeFunc$0(prepareItem, f);
    }

    @Override // android.os.AsyncTask
    public /* bridge */ /* synthetic */ Object doInBackground(Object[] objArr) {
        return doInBackground((ArrayList[]) ((ArrayList[]) objArr));
    }

    @Override // android.os.AsyncTask
    public /* bridge */ /* synthetic */ void onCancelled(Object obj) {
        onCancelled((ArrayList) ((ArrayList) obj));
    }

    @Override // android.os.AsyncTask
    public /* bridge */ /* synthetic */ void onPostExecute(Object obj) {
        onPostExecute((ArrayList) ((ArrayList) obj));
    }

    public PrepareTask(FragmentActivity fragmentActivity, int i, OnPrepareListener onPrepareListener) {
        this.mActivityRef = new WeakReference<>(fragmentActivity);
        this.mListener = onPrepareListener;
        this.mMaxProgress = i;
    }

    @Override // android.os.AsyncTask
    public void onPreExecute() {
        super.onPreExecute();
        OnPrepareListener onPrepareListener = this.mListener;
        if (onPrepareListener != null) {
            onPrepareListener.onStarted(this.mItemsHolder);
        }
    }

    public void onCancelled(ArrayList<T> arrayList) {
        super.onCancelled((PrepareTask<T>) arrayList);
        OnPrepareListener onPrepareListener = this.mListener;
        if (onPrepareListener != null) {
            onPrepareListener.onCancelled(arrayList);
        }
    }

    public ArrayList<T> doInBackground(ArrayList<T>... arrayListArr) {
        boolean z;
        PrepareFunc func;
        ArrayList<T> arrayList = arrayListArr[0];
        Iterator<T> it = arrayList.iterator();
        int i = 0;
        while (it.hasNext()) {
            if (it.next().getFlags() > 0) {
                i++;
            }
        }
        this.mItemMaxProgress = (this.mMaxProgress * 1.0f) / i;
        Iterator<T> it2 = arrayList.iterator();
        while (it2.hasNext()) {
            T next = it2.next();
            if (isCancelled()) {
                break;
            }
            if (next.getFlags() > 0) {
                next.onStepPrepared(next.getSrcUri(), 0);
                int numberOfLeadingZeros = 32 - Integer.numberOfLeadingZeros(next.getFlags());
                for (int i2 = 0; i2 < numberOfLeadingZeros && !isCancelled(); i2++) {
                    int flags = next.getFlags() & (1 << i2);
                    if (flags > 0 && (func = getFunc(this.mActivityRef, flags)) != null) {
                        Uri invokeFunc = invokeFunc(func, next);
                        if (invokeFunc == null) {
                            z = false;
                            break;
                        }
                        next.onStepPrepared(invokeFunc, flags);
                    }
                }
                z = true;
                if (z) {
                    next.onPrepared(next.getPreparedUriInLastStep());
                }
                this.mCurProgress = (int) (this.mCurProgress + this.mItemMaxProgress);
            } else {
                next.onPrepared(next.getSrcUri());
            }
            this.mHasPreparedSize++;
        }
        scheduleClean();
        return arrayList;
    }

    public /* synthetic */ void lambda$invokeFunc$0(PrepareItem prepareItem, float f) {
        publishProgress(Integer.valueOf((int) (this.mCurProgress + (this.mItemMaxProgress * f))));
    }

    public final Uri invokeFunc(PrepareFunc prepareFunc, T t) {
        return prepareFunc.prepare(t, new PrepareProgressCallback() { // from class: com.miui.gallery.ui.share.PrepareTask$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.ui.share.PrepareProgressCallback
            public final void onPreparing(PrepareItem prepareItem, float f) {
                PrepareTask.$r8$lambda$sgvEdSpZYxswYhLMuxBPdIOf1DU(PrepareTask.this, prepareItem, f);
            }
        });
    }

    public final PrepareFunc getFunc(WeakReference<FragmentActivity> weakReference, int i) {
        PrepareFunc prepareFunc = this.mFuncs.get(Integer.valueOf(i));
        if (prepareFunc == null) {
            PrepareFunc create = PrepareFuncFactory.create(weakReference, i);
            this.mFuncs.put(Integer.valueOf(i), create);
            return create;
        }
        return prepareFunc;
    }

    public void onPostExecute(ArrayList<T> arrayList) {
        super.onPostExecute((PrepareTask<T>) arrayList);
        OnPrepareListener onPrepareListener = this.mListener;
        if (onPrepareListener == null || this.mPaused) {
            return;
        }
        onPrepareListener.onPrepared(arrayList);
    }

    @Override // android.os.AsyncTask
    public void onProgressUpdate(Integer... numArr) {
        OnPrepareListener onPrepareListener;
        super.onProgressUpdate((Object[]) numArr);
        if (this.mPaused || (onPrepareListener = this.mListener) == null) {
            return;
        }
        onPrepareListener.onProgressUpdate(numArr[0].intValue());
    }

    public final void checkFinished() {
        OnPrepareListener onPrepareListener;
        if (!this.mPaused && getStatus() == AsyncTask.Status.FINISHED && (onPrepareListener = this.mListener) != null) {
            onPrepareListener.onPrepared(this.mItemsHolder);
            this.mItemsHolder = null;
        }
    }

    public PrepareTask invoke(ArrayList<T> arrayList) {
        this.mItemsHolder = arrayList;
        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, arrayList);
        return this;
    }

    public int getRemainSize() {
        return this.mItemsHolder.size() - this.mHasPreparedSize;
    }

    public void pause() {
        DefaultLogger.d("PrepareTask", "prepare pause");
        this.mPaused = true;
    }

    public void resume() {
        DefaultLogger.d("PrepareTask", "downloader resume");
        this.mPaused = false;
        checkFinished();
    }

    public void release() {
        this.mListener = null;
        cancel(true);
        for (PrepareFunc prepareFunc : this.mFuncs.values()) {
            prepareFunc.release();
        }
    }

    public final void scheduleClean() {
        CleanScheduler.schedule(StaticContext.sGetAndroidContext(), "PrepareTask#clean_new", getDecryptFolder().getAbsolutePath(), StorageUtils.getShareTempDirectory());
    }

    public static File getDecryptFolder() {
        return new File(StaticContext.sGetAndroidContext().getCacheDir(), "dc_tmp_files");
    }

    public static File getConvertFolder() {
        return new File(StaticContext.sGetAndroidContext().getExternalCacheDir(), ".convert_tmp_files");
    }

    public static File getRenderFolder() {
        return new File(StaticContext.sGetAndroidContext().getExternalCacheDir(), "render_tmp_files");
    }

    public static File getNewConvertFolder() {
        DefaultLogger.d("PrepareTask", "getNewConvertFolder");
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("PrepareTask", "getNewConvertFolder");
        String concat = BaseFileUtils.concat(StorageUtils.getShareTempDirectory(), ".convert_tmp_files");
        if (StorageSolutionProvider.get().getDocumentFile(concat, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, appendInvokerTag) != null) {
            DefaultLogger.d("PrepareTask", "create folder under primary storage for convert");
            return new File(concat);
        }
        DefaultLogger.d("PrepareTask", "failed to create folder under primary storage for convert");
        return getConvertFolder();
    }

    public static File getNewRenderFolder() {
        DefaultLogger.d("PrepareTask", "getNewRenderFolder");
        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("PrepareTask", "getNewRenderFolder");
        String concat = BaseFileUtils.concat(StorageUtils.getShareTempDirectory(), ".render_tmp_files");
        if (StorageSolutionProvider.get().getDocumentFile(concat, IStoragePermissionStrategy.Permission.INSERT_DIRECTORY, appendInvokerTag) != null) {
            DefaultLogger.d("PrepareTask", "create folder under primary storage for render");
            return new File(concat);
        }
        DefaultLogger.d("PrepareTask", "failed to create folder under primary storage for render");
        return getRenderFolder();
    }

    /* loaded from: classes2.dex */
    public static class PrepareFuncFactory {
        public static PrepareFunc create(WeakReference<FragmentActivity> weakReference, int i) {
            if (i != 1) {
                if (i == 2) {
                    return new DecryptFunc(PrepareTask.getDecryptFolder());
                }
                if (i == 4) {
                    return new ConvertFunc(PrepareTask.getNewConvertFolder());
                }
                if (i == 8) {
                    return new RenderFunc(PrepareTask.getNewRenderFolder());
                }
                return null;
            }
            return new DownloadFunc(weakReference);
        }
    }
}
