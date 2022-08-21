package com.miui.gallery.collage.core;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.format.DateFormat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.collage.BitmapManager;
import com.miui.gallery.collage.CollageActivity;
import com.miui.gallery.collage.app.common.CollageMenuFragment;
import com.miui.gallery.collage.app.common.CollageRenderFragment;
import com.miui.gallery.collage.app.common.IDataLoader;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.constants.StorageConstants;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.mediaeditor.utils.FilePermissionUtils;
import java.io.File;
import java.util.Locale;

/* loaded from: classes.dex */
public abstract class CollagePresenter {
    public IDataLoader mDataLoader;
    public CollageMenuFragment mMenuFragment;
    public CollageRenderFragment mRenderFragment;
    public SaveTask mSaveTask;
    public ViewInterface mViewInterface;
    public int mImageCount = -1;
    public CollageActivity.ReplaceImageListener mReplaceImageListener = new CollageActivity.ReplaceImageListener() { // from class: com.miui.gallery.collage.core.CollagePresenter.2
        @Override // com.miui.gallery.collage.CollageActivity.ReplaceImageListener
        public void onReplace(Bitmap bitmap) {
            ViewInterface viewInterface = CollagePresenter.this.mViewInterface;
            if (viewInterface != null) {
                viewInterface.onReplaceBitmap(bitmap);
            }
        }
    };

    /* loaded from: classes.dex */
    public interface DataLoadListener {
        void onDataLoad();
    }

    /* loaded from: classes.dex */
    public interface SaveListener {
        void onSaveFinish(String str, boolean z);
    }

    public abstract RenderEngine createEngine(Context context, BitmapManager bitmapManager);

    public abstract String getMenuFragmentTag();

    public abstract String getRenderFragmentTag();

    public abstract int getTitle();

    public abstract boolean hasResourceData();

    public abstract IDataLoader onCreateDataLoader(DataLoadListener dataLoadListener);

    public abstract CollageMenuFragment onCreateMenuFragment();

    public abstract CollageRenderFragment onCreateRenderFragment();

    public abstract void onDetach();

    public boolean supportImageSize(int i) {
        return true;
    }

    public void attach(ViewInterface viewInterface) {
        this.mViewInterface = viewInterface;
    }

    public final void detach() {
        this.mViewInterface = null;
        this.mMenuFragment = null;
        this.mRenderFragment = null;
        SaveTask saveTask = this.mSaveTask;
        if (saveTask != null) {
            saveTask.mSaveListener = null;
            this.mSaveTask.cancel(false);
        }
        IDataLoader iDataLoader = this.mDataLoader;
        if (iDataLoader != null) {
            iDataLoader.cancel();
        }
        onDetach();
    }

    public void doSave(BitmapManager bitmapManager) {
        if (this.mRenderFragment == null || this.mViewInterface == null) {
            return;
        }
        String format = String.format(Locale.US, "IMG_%s.jpg", DateFormat.format("yyyyMMdd_HHmmss", System.currentTimeMillis()));
        String pathInPriorStorage = StorageUtils.getPathInPriorStorage(StorageConstants.RELATIVE_DIRECTORY_CREATIVE + File.separator + format);
        if (pathInPriorStorage == null || !FilePermissionUtils.checkFileCreatePermission((FragmentActivity) this.mViewInterface.getContext(), pathInPriorStorage)) {
            return;
        }
        RenderData export = this.mRenderFragment.export();
        SamplingStatHelper.recordCountEvent("collage", "collage_save", this.mRenderFragment.onSimple());
        RenderEngine createEngine = createEngine(this.mViewInterface.getContext().getApplicationContext(), bitmapManager);
        if (export == null || createEngine == null) {
            this.mViewInterface.onSaveFinish(null, false);
            return;
        }
        this.mViewInterface.onSaving();
        SaveTask saveTask = new SaveTask(createEngine, export, pathInPriorStorage, this.mViewInterface.getContext().getApplicationContext());
        this.mSaveTask = saveTask;
        saveTask.mSaveListener = new SaveListener() { // from class: com.miui.gallery.collage.core.CollagePresenter.1
            @Override // com.miui.gallery.collage.core.CollagePresenter.SaveListener
            public void onSaveFinish(String str, boolean z) {
                ViewInterface viewInterface = CollagePresenter.this.mViewInterface;
                if (viewInterface != null) {
                    viewInterface.onSaveFinish(str, z);
                }
            }
        };
        this.mSaveTask.execute(new Void[0]);
    }

    public boolean isActivating() {
        CollageRenderFragment collageRenderFragment = this.mRenderFragment;
        return collageRenderFragment != null && collageRenderFragment.isActivating();
    }

    public final void loadDataFromResourceAsync(DataLoadListener dataLoadListener) {
        if (hasResourceData()) {
            if (dataLoadListener == null) {
                return;
            }
            dataLoadListener.onDataLoad();
            return;
        }
        IDataLoader onCreateDataLoader = onCreateDataLoader(dataLoadListener);
        this.mDataLoader = onCreateDataLoader;
        onCreateDataLoader.loadData();
    }

    public void dismissControlWindow() {
        CollageRenderFragment collageRenderFragment = this.mRenderFragment;
        if (collageRenderFragment != null) {
            collageRenderFragment.dismissControlWindow();
        }
    }

    public void setImageSize(int i) {
        this.mImageCount = i;
    }

    public int getImageCount() {
        return this.mImageCount;
    }

    public void notifyReceiveBitmaps() {
        CollageRenderFragment collageRenderFragment = this.mRenderFragment;
        if (collageRenderFragment != null) {
            collageRenderFragment.setBitmap(this.mViewInterface.getBitmaps());
        }
    }

    public void notifyBitmapReplace(Bitmap bitmap, Bitmap bitmap2) {
        CollageRenderFragment collageRenderFragment = this.mRenderFragment;
        if (collageRenderFragment != null) {
            collageRenderFragment.onBitmapReplace(bitmap, bitmap2);
        }
    }

    public CollageMenuFragment getMenuFragment() {
        if (this.mMenuFragment == null) {
            this.mMenuFragment = onCreateMenuFragment();
        }
        return this.mMenuFragment;
    }

    public Fragment getRenderFragment() {
        if (this.mRenderFragment == null) {
            this.mRenderFragment = onCreateRenderFragment();
        }
        return this.mRenderFragment;
    }

    public void onAttachFragment(Fragment fragment) {
        if (fragment instanceof CollageRenderFragment) {
            Bitmap[] bitmaps = this.mViewInterface.getBitmaps();
            if (bitmaps != null) {
                this.mRenderFragment.setBitmap(bitmaps);
            }
            this.mRenderFragment.setReplaceImageListener(this.mReplaceImageListener);
        } else if (!(fragment instanceof CollageMenuFragment)) {
        } else {
            this.mMenuFragment.setPresenter(this);
            CollageRenderFragment collageRenderFragment = this.mRenderFragment;
            if (collageRenderFragment == null) {
                return;
            }
            this.mMenuFragment.setRenderFragment(collageRenderFragment);
        }
    }

    /* loaded from: classes.dex */
    public static class SaveTask extends AsyncTask<Void, Void, Void> {
        public final Context mContext;
        public final String mOutPath;
        public final RenderData mRenderData;
        public final RenderEngine mRenderEngine;
        public SaveListener mSaveListener;
        public boolean mSuccess;

        public SaveTask(RenderEngine renderEngine, RenderData renderData, String str, Context context) {
            this.mSuccess = false;
            this.mRenderEngine = renderEngine;
            this.mRenderData = renderData;
            this.mOutPath = str;
            this.mContext = context.getApplicationContext();
        }

        /* JADX WARN: Code restructure failed: missing block: B:22:0x009e, code lost:
            if (r9.mSuccess == false) goto L24;
         */
        /* JADX WARN: Code restructure failed: missing block: B:32:0x00ac, code lost:
            if (r9.mSuccess != false) goto L22;
         */
        /* JADX WARN: Code restructure failed: missing block: B:33:0x00ae, code lost:
            r0.recycle();
         */
        /* JADX WARN: Code restructure failed: missing block: B:34:0x00b1, code lost:
            com.miui.gallery.util.IoUtils.close(r4);
         */
        /* JADX WARN: Code restructure failed: missing block: B:35:0x00b4, code lost:
            return null;
         */
        /* JADX WARN: Not initialized variable reg: 4, insn: 0x00b6: MOVE  (r2 I:??[OBJECT, ARRAY]) = (r4 I:??[OBJECT, ARRAY]), block:B:37:0x00b6 */
        /* JADX WARN: Removed duplicated region for block: B:40:0x00bb  */
        @Override // android.os.AsyncTask
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        public java.lang.Void doInBackground(java.lang.Void... r10) {
            /*
                r9 = this;
                java.lang.String r10 = "CollagePresenter"
                r0 = 0
                r9.mSuccess = r0
                com.miui.gallery.collage.core.RenderEngine r0 = r9.mRenderEngine
                com.miui.gallery.collage.core.RenderData r1 = r9.mRenderData
                android.graphics.Bitmap r0 = r0.render(r1)
                boolean r1 = r9.isCancelled()
                r2 = 0
                if (r1 == 0) goto L15
                return r2
            L15:
                java.io.File r1 = new java.io.File
                java.lang.String r3 = r9.mOutPath
                r1.<init>(r3)
                java.lang.String r3 = "doInBackground"
                java.lang.String r3 = com.miui.gallery.util.FileHandleRecordHelper.appendInvokerTag(r10, r3)     // Catch: java.lang.Throwable -> La3 java.io.IOException -> La5
                java.io.File r4 = r1.getParentFile()     // Catch: java.lang.Throwable -> La3 java.io.IOException -> La5
                boolean r5 = r4.exists()     // Catch: java.lang.Throwable -> La3 java.io.IOException -> La5
                if (r5 != 0) goto L2f
                r4.mkdirs()     // Catch: java.lang.Throwable -> La3 java.io.IOException -> La5
            L2f:
                boolean r4 = r1.exists()     // Catch: java.lang.Throwable -> La3 java.io.IOException -> La5
                if (r4 == 0) goto L38
                r1.delete()     // Catch: java.lang.Throwable -> La3 java.io.IOException -> La5
            L38:
                java.io.FileOutputStream r4 = new java.io.FileOutputStream     // Catch: java.lang.Throwable -> La3 java.io.IOException -> La5
                r4.<init>(r1)     // Catch: java.lang.Throwable -> La3 java.io.IOException -> La5
                android.graphics.Bitmap$CompressFormat r5 = android.graphics.Bitmap.CompressFormat.JPEG     // Catch: java.io.IOException -> La1 java.lang.Throwable -> Lb5
                r6 = 100
                boolean r5 = r0.compress(r5, r6, r4)     // Catch: java.io.IOException -> La1 java.lang.Throwable -> Lb5
                r9.mSuccess = r5     // Catch: java.io.IOException -> La1 java.lang.Throwable -> Lb5
                r4.flush()     // Catch: java.io.IOException -> La1 java.lang.Throwable -> Lb5
                java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch: java.io.IOException -> La1 java.lang.Throwable -> Lb5
                r5.<init>()     // Catch: java.io.IOException -> La1 java.lang.Throwable -> Lb5
                java.lang.String r6 = "save bitmap state : "
                r5.append(r6)     // Catch: java.io.IOException -> La1 java.lang.Throwable -> Lb5
                boolean r6 = r9.mSuccess     // Catch: java.io.IOException -> La1 java.lang.Throwable -> Lb5
                r5.append(r6)     // Catch: java.io.IOException -> La1 java.lang.Throwable -> Lb5
                java.lang.String r5 = r5.toString()     // Catch: java.io.IOException -> La1 java.lang.Throwable -> Lb5
                com.miui.gallery.util.logger.DefaultLogger.d(r10, r5)     // Catch: java.io.IOException -> La1 java.lang.Throwable -> Lb5
                boolean r5 = r9.mSuccess     // Catch: java.io.IOException -> La1 java.lang.Throwable -> Lb5
                if (r5 == 0) goto L8b
                com.miui.gallery.scanner.core.ScannerEngine r5 = com.miui.gallery.scanner.core.ScannerEngine.getInstance()     // Catch: java.io.IOException -> La1 java.lang.Throwable -> Lb5
                android.content.Context r6 = r9.mContext     // Catch: java.io.IOException -> La1 java.lang.Throwable -> Lb5
                java.lang.String r7 = r1.getAbsolutePath()     // Catch: java.io.IOException -> La1 java.lang.Throwable -> Lb5
                r8 = 13
                r5.scanFile(r6, r7, r8)     // Catch: java.io.IOException -> La1 java.lang.Throwable -> Lb5
                com.miui.gallery.storage.strategies.base.StorageStrategyManager r5 = com.miui.gallery.storage.StorageSolutionProvider.get()     // Catch: java.io.IOException -> La1 java.lang.Throwable -> Lb5
                java.lang.String r1 = r1.getAbsolutePath()     // Catch: java.io.IOException -> La1 java.lang.Throwable -> Lb5
                com.miui.gallery.storage.strategies.IStoragePermissionStrategy$Permission r6 = com.miui.gallery.storage.strategies.IStoragePermissionStrategy.Permission.QUERY     // Catch: java.io.IOException -> La1 java.lang.Throwable -> Lb5
                androidx.documentfile.provider.DocumentFile r1 = r5.getDocumentFile(r1, r6, r3)     // Catch: java.io.IOException -> La1 java.lang.Throwable -> Lb5
                if (r1 == 0) goto L9c
                com.miui.gallery.storage.strategies.base.StorageStrategyManager r3 = com.miui.gallery.storage.StorageSolutionProvider.get()     // Catch: java.io.IOException -> La1 java.lang.Throwable -> Lb5
                r3.apply(r1)     // Catch: java.io.IOException -> La1 java.lang.Throwable -> Lb5
                goto L9c
            L8b:
                com.miui.gallery.storage.strategies.base.StorageStrategyManager r1 = com.miui.gallery.storage.StorageSolutionProvider.get()     // Catch: java.io.IOException -> La1 java.lang.Throwable -> Lb5
                java.lang.String r5 = r9.mOutPath     // Catch: java.io.IOException -> La1 java.lang.Throwable -> Lb5
                com.miui.gallery.storage.strategies.IStoragePermissionStrategy$Permission r6 = com.miui.gallery.storage.strategies.IStoragePermissionStrategy.Permission.DELETE     // Catch: java.io.IOException -> La1 java.lang.Throwable -> Lb5
                androidx.documentfile.provider.DocumentFile r1 = r1.getDocumentFile(r5, r6, r3)     // Catch: java.io.IOException -> La1 java.lang.Throwable -> Lb5
                if (r1 == 0) goto L9c
                r1.delete()     // Catch: java.io.IOException -> La1 java.lang.Throwable -> Lb5
            L9c:
                boolean r10 = r9.mSuccess
                if (r10 != 0) goto Lb1
                goto Lae
            La1:
                r1 = move-exception
                goto La7
            La3:
                r10 = move-exception
                goto Lb7
            La5:
                r1 = move-exception
                r4 = r2
            La7:
                com.miui.gallery.util.logger.DefaultLogger.d(r10, r1)     // Catch: java.lang.Throwable -> Lb5
                boolean r10 = r9.mSuccess
                if (r10 != 0) goto Lb1
            Lae:
                r0.recycle()
            Lb1:
                com.miui.gallery.util.IoUtils.close(r4)
                return r2
            Lb5:
                r10 = move-exception
                r2 = r4
            Lb7:
                boolean r1 = r9.mSuccess
                if (r1 != 0) goto Lbe
                r0.recycle()
            Lbe:
                com.miui.gallery.util.IoUtils.close(r2)
                throw r10
            */
            throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.collage.core.CollagePresenter.SaveTask.doInBackground(java.lang.Void[]):java.lang.Void");
        }

        @Override // android.os.AsyncTask
        public void onPostExecute(Void r3) {
            if (this.mSaveListener != null) {
                DefaultLogger.d("CollagePresenter", "save bitmap result in onPostExecute : " + this.mSuccess);
                this.mSaveListener.onSaveFinish(this.mOutPath, this.mSuccess);
            }
        }
    }
}
