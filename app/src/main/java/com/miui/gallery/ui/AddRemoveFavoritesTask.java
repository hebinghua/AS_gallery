package com.miui.gallery.ui;

import android.net.Uri;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.provider.CloudUtils;
import com.miui.gallery.ui.ProcessTask;
import com.miui.gallery.util.DebugUtil;
import com.miui.gallery.util.MediaAndAlbumOperations;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class AddRemoveFavoritesTask extends ProcessTask<Param, Void, long[]> {
    public boolean mIsAdd;
    public long mOperationStartTime;

    public AddRemoveFavoritesTask() {
        super(new ProcessTask.ProcessCallback<Param, Void, long[]>() { // from class: com.miui.gallery.ui.AddRemoveFavoritesTask.1
            @Override // com.miui.gallery.ui.ProcessTask.ProcessCallback
            public long[] doProcess(Param[] paramArr) {
                int i = paramArr[0].mOperationType;
                int i2 = paramArr[0].mAddRemoveBy;
                if (i2 == 1) {
                    if (i == 1) {
                        return CloudUtils.addToFavoritesByPath(GalleryApp.sGetAndroidContext(), paramArr[0].mData);
                    }
                    if (i != 2) {
                        return null;
                    }
                    return CloudUtils.removeFromFavoritesByPath(GalleryApp.sGetAndroidContext(), paramArr[0].mData);
                } else if (i2 == 2) {
                    if (i == 1) {
                        return CloudUtils.addToFavoritesById(GalleryApp.sGetAndroidContext(), paramArr[0].mIds);
                    }
                    if (i != 2) {
                        return null;
                    }
                    return CloudUtils.removeFromFavoritesById(GalleryApp.sGetAndroidContext(), paramArr[0].mIds);
                } else if (i2 != 3) {
                    return null;
                } else {
                    if (i == 1) {
                        return CloudUtils.addToFavoritesByUri(GalleryApp.sGetAndroidContext(), paramArr[0].mUriList);
                    }
                    if (i != 2) {
                        return null;
                    }
                    return CloudUtils.removeFromFavoritesByUri(GalleryApp.sGetAndroidContext(), paramArr[0].mUriList);
                }
            }
        });
        this.mOperationStartTime = 0L;
    }

    @Override // android.os.AsyncTask
    public void onPreExecute() {
        super.onPreExecute();
        this.mOperationStartTime = System.currentTimeMillis();
    }

    @Override // com.miui.gallery.ui.ProcessTask, android.os.AsyncTask
    public long[] doInBackground(Param... paramArr) {
        boolean z = true;
        if (paramArr[0].mOperationType != 1) {
            z = false;
        }
        this.mIsAdd = z;
        DebugUtil.logEventTime("operationTrace", z ? "add_to_favorites" : "remove_from_favorites", this.mOperationStartTime, false);
        return (long[]) super.doInBackground((Object[]) paramArr);
    }

    @Override // com.miui.gallery.ui.ProcessTask, android.os.AsyncTask
    public void onPostExecute(long[] jArr) {
        super.onPostExecute((AddRemoveFavoritesTask) jArr);
        DebugUtil.logEventTime("operationTrace", this.mIsAdd ? "add_to_favorites" : "remove_from_favorites", System.currentTimeMillis(), true);
    }

    public void setOperationCompleteListener(MediaAndAlbumOperations.OnCompleteListener onCompleteListener) {
        if (onCompleteListener != null) {
            setCompleteListener(new OnCompleteListenerAdapter(onCompleteListener));
        } else {
            setCompleteListener(null);
        }
    }

    /* loaded from: classes2.dex */
    public static class OnCompleteListenerAdapter implements ProcessTask.OnCompleteListener<long[]> {
        public WeakReference<MediaAndAlbumOperations.OnCompleteListener> mListenerRef;

        public OnCompleteListenerAdapter(MediaAndAlbumOperations.OnCompleteListener onCompleteListener) {
            this.mListenerRef = new WeakReference<>(onCompleteListener);
        }

        @Override // com.miui.gallery.ui.ProcessTask.OnCompleteListener
        public void onCompleteProcess(long[] jArr) {
            MediaAndAlbumOperations.OnCompleteListener onCompleteListener = this.mListenerRef.get();
            if (onCompleteListener != null) {
                onCompleteListener.onComplete(jArr);
            }
        }
    }

    /* loaded from: classes2.dex */
    public static class Param implements Serializable {
        private int mAddRemoveBy;
        private String[] mData;
        private long[] mIds;
        private int mOperationType;
        private ArrayList<Uri> mUriList;

        public Param(int i, int i2, ArrayList<Uri> arrayList) {
            this.mOperationType = i;
            this.mAddRemoveBy = i2;
            this.mUriList = arrayList;
        }

        public Param(int i, int i2, String[] strArr) {
            this.mOperationType = i;
            this.mAddRemoveBy = i2;
            this.mData = strArr;
        }

        public Param(int i, int i2, long[] jArr) {
            this.mOperationType = i;
            this.mAddRemoveBy = i2;
            this.mIds = jArr;
        }
    }
}
