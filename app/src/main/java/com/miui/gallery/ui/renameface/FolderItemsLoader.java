package com.miui.gallery.ui.renameface;

import android.app.Activity;
import android.os.AsyncTask;
import com.miui.gallery.model.DisplayFolderItem;
import java.lang.ref.SoftReference;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public abstract class FolderItemsLoader {
    public SoftReference<Activity> mActivityRef;
    public boolean mCancelled;
    public ArrayList<DisplayFolderItem> mItems = new ArrayList<>();
    public ArrayList<DisplayFolderItem> mLoadedItems = null;
    public final LoaderUpdatedItems mOutListener;
    public String mPath;
    public long[] mSelectedFoldersLocalID;

    /* loaded from: classes2.dex */
    public interface LoaderUpdatedItems {
        void onLoaderUpdatedItems();
    }

    public abstract ArrayList<DisplayFolderItem> refreshCloudFolderItems();

    public FolderItemsLoader(Activity activity, String str, LoaderUpdatedItems loaderUpdatedItems, long[] jArr, boolean z) {
        this.mPath = str;
        this.mOutListener = loaderUpdatedItems;
        this.mSelectedFoldersLocalID = jArr;
        this.mActivityRef = new SoftReference<>(activity);
        reload();
    }

    public final ArrayList<DisplayFolderItem> unblockGetItems() {
        return this.mItems;
    }

    public final ArrayList<DisplayFolderItem> tryToGetLoadedItems(long j) {
        long currentTimeMillis = System.currentTimeMillis();
        do {
            ArrayList<DisplayFolderItem> arrayList = this.mLoadedItems;
            if (arrayList != null) {
                return arrayList;
            }
            try {
                Thread.sleep(100L);
            } catch (InterruptedException unused) {
            }
        } while (System.currentTimeMillis() - currentTimeMillis < j);
        return null;
    }

    public final void reload() {
        new AsyncTask<Void, Void, ArrayList<DisplayFolderItem>>() { // from class: com.miui.gallery.ui.renameface.FolderItemsLoader.1
            @Override // android.os.AsyncTask
            public ArrayList<DisplayFolderItem> doInBackground(Void... voidArr) {
                FolderItemsLoader folderItemsLoader = FolderItemsLoader.this;
                folderItemsLoader.mLoadedItems = folderItemsLoader.refreshCloudFolderItems();
                return FolderItemsLoader.this.mLoadedItems;
            }

            @Override // android.os.AsyncTask
            public void onPostExecute(ArrayList<DisplayFolderItem> arrayList) {
                FolderItemsLoader folderItemsLoader = FolderItemsLoader.this;
                folderItemsLoader.mItems = arrayList;
                if (folderItemsLoader.mCancelled || FolderItemsLoader.this.mOutListener == null) {
                    return;
                }
                FolderItemsLoader.this.mOutListener.onLoaderUpdatedItems();
            }
        }.execute(new Void[0]);
    }

    public void cancel() {
        this.mCancelled = true;
    }

    public boolean isMediaSetCandidate(String str) {
        boolean z = false;
        if (this.mSelectedFoldersLocalID != null) {
            int i = 0;
            while (true) {
                if (i >= this.mSelectedFoldersLocalID.length) {
                    break;
                } else if (Long.parseLong(str) == this.mSelectedFoldersLocalID[i]) {
                    z = true;
                    break;
                } else {
                    i++;
                }
            }
        }
        return !z;
    }
}
