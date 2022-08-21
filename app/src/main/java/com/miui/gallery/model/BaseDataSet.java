package com.miui.gallery.model;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.text.TextUtils;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.R;
import com.miui.gallery.concurrent.PriorityThreadFactory;
import com.miui.gallery.projection.IConnectController;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.ui.DeletionTask;
import com.miui.gallery.ui.ProcessTask;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.DialogUtil;
import com.miui.gallery.util.MediaAndAlbumOperations;
import com.miui.gallery.util.SyncUtil;
import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public abstract class BaseDataSet implements IConnectController.DataSet {
    public Map<Integer, BaseDataItem> mBaseDataCache;
    public ExecutorService mExecutor;
    public boolean mHasSetInitPos;
    public int mInitPosition;

    public abstract boolean addToAlbum(FragmentActivity fragmentActivity, int i, boolean z, boolean z2, MediaAndAlbumOperations.OnAddAlbumListener onAddAlbumListener);

    public boolean addToFavorites(FragmentActivity fragmentActivity, int i, MediaAndAlbumOperations.OnCompleteListener onCompleteListener) {
        return false;
    }

    public abstract void bindItem(BaseDataItem baseDataItem, int i);

    public abstract BaseDataItem createItem(int i);

    public boolean deletingIncludeCloud() {
        return false;
    }

    public abstract int doDelete(int i, BaseDataItem baseDataItem, boolean z);

    public boolean foldBurst() {
        return false;
    }

    @Override // com.miui.gallery.projection.IConnectController.DataSet
    public abstract int getCount();

    public abstract long getItemKey(int i);

    public abstract String getItemPath(int i);

    public abstract void onRelease();

    public boolean removeFromFavorites(FragmentActivity fragmentActivity, int i, MediaAndAlbumOperations.OnCompleteListener onCompleteListener) {
        return false;
    }

    public boolean removeFromSecret(FragmentActivity fragmentActivity, int i, MediaAndAlbumOperations.OnCompleteListener onCompleteListener) {
        return false;
    }

    public BaseDataSet() {
        this(0);
    }

    public BaseDataSet(int i) {
        this.mBaseDataCache = new ConcurrentHashMap();
        this.mInitPosition = i;
        this.mExecutor = Executors.newSingleThreadExecutor(new PriorityThreadFactory("BaseDataSet-" + hashCode(), 5));
    }

    @Override // com.miui.gallery.projection.IConnectController.DataSet
    public BaseDataItem getItem(BaseDataItem baseDataItem, int i) {
        if (baseDataItem == null) {
            baseDataItem = this.mBaseDataCache.get(Integer.valueOf(i));
            if (baseDataItem == null && (baseDataItem = createItem(i)) != null) {
                this.mBaseDataCache.put(Integer.valueOf(i), baseDataItem);
            }
        } else {
            bindItem(baseDataItem, i);
        }
        return baseDataItem;
    }

    public int getInitPosition() {
        return this.mInitPosition;
    }

    public void setInitPosition(int i) {
        this.mInitPosition = i;
        this.mHasSetInitPos = true;
    }

    public boolean hasSetInitPosition() {
        return this.mHasSetInitPos;
    }

    public int getIndexOfItem(BaseDataItem baseDataItem, int i) {
        if (baseDataItem == null) {
            return -1;
        }
        int count = getCount();
        if (i >= 0) {
            if (baseDataItem.getKey() == getItemKey(i)) {
                return i;
            }
            int i2 = i + 20;
            int indexOfItem = getIndexOfItem(baseDataItem, i, i2);
            if (indexOfItem != -1) {
                return indexOfItem;
            }
            int i3 = i - 20;
            int indexOfItem2 = getIndexOfItem(baseDataItem, i3, i);
            if (indexOfItem2 != -1) {
                return indexOfItem2;
            }
            int indexOfItem3 = getIndexOfItem(baseDataItem, 0, i3);
            return indexOfItem3 != -1 ? indexOfItem3 : getIndexOfItem(baseDataItem, i2, count);
        }
        return getIndexOfItem(baseDataItem, 0, count);
    }

    public final int getIndexOfItem(BaseDataItem baseDataItem, int i, int i2) {
        int count = getCount();
        int clamp = BaseMiscUtil.clamp(i2, 0, count);
        for (int clamp2 = BaseMiscUtil.clamp(i, 0, count - 1); clamp2 < clamp; clamp2++) {
            if (baseDataItem.getKey() == getItemKey(clamp2)) {
                return clamp2;
            }
        }
        return -1;
    }

    @Override // com.miui.gallery.projection.IConnectController.DataSet
    public int getIndexOfItem(String str, int i) {
        if (!TextUtils.isEmpty(str)) {
            if (i >= 0) {
                int i2 = i - 20;
                int indexOfItem = getIndexOfItem(str, i2, i);
                if (indexOfItem != -1) {
                    return indexOfItem;
                }
                int i3 = i + 20;
                int indexOfItem2 = getIndexOfItem(str, i, i3);
                if (indexOfItem2 != -1) {
                    return indexOfItem2;
                }
                int indexOfItem3 = getIndexOfItem(str, 0, i2);
                return indexOfItem3 != -1 ? indexOfItem3 : getIndexOfItem(str, i3, getCount());
            }
            return getIndexOfItem(str, 0, getCount());
        }
        return -1;
    }

    public final int getIndexOfItem(String str, int i, int i2) {
        int count = getCount();
        int clamp = BaseMiscUtil.clamp(i2, 0, count);
        for (int clamp2 = BaseMiscUtil.clamp(i, 0, count - 1); clamp2 < clamp; clamp2++) {
            if (str.equalsIgnoreCase(getItemPath(clamp2))) {
                return clamp2;
            }
        }
        return -1;
    }

    public int getIndexOfItem(long j, int i) {
        if (i >= 0) {
            int i2 = i - 20;
            int indexOfItem = getIndexOfItem(j, i2, i);
            if (indexOfItem != -1) {
                return indexOfItem;
            }
            int i3 = i + 20;
            int indexOfItem2 = getIndexOfItem(j, i, i3);
            if (indexOfItem2 != -1) {
                return indexOfItem2;
            }
            int indexOfItem3 = getIndexOfItem(j, 0, i2);
            return indexOfItem3 != -1 ? indexOfItem3 : getIndexOfItem(j, i3, getCount());
        }
        return getIndexOfItem(j, 0, getCount());
    }

    public final int getIndexOfItem(long j, int i, int i2) {
        int count = getCount();
        int clamp = BaseMiscUtil.clamp(i2, 0, count);
        for (int clamp2 = BaseMiscUtil.clamp(i, 0, count - 1); clamp2 < clamp; clamp2++) {
            if (getItemKey(clamp2) == j) {
                return clamp2;
            }
        }
        return -1;
    }

    public final void release() {
        this.mBaseDataCache.clear();
        onRelease();
    }

    public AlertDialog delete(final FragmentActivity fragmentActivity, final int i, final DeletionTask.OnDeletionCompleteListener onDeletionCompleteListener, DialogInterface.OnDismissListener onDismissListener) {
        final BaseDataItem item = getItem(null, i);
        if (item != null) {
            return DialogUtil.showInfoDialog(fragmentActivity, getDeleteDialogMessage(item, fragmentActivity), fragmentActivity.getResources().getString(R.string.delete), 17039370, 17039360, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.model.BaseDataSet$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i2) {
                    BaseDataSet.this.lambda$delete$3(item, i, onDeletionCompleteListener, fragmentActivity, dialogInterface, i2);
                }
            }, BaseDataSet$$ExternalSyntheticLambda1.INSTANCE);
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public /* synthetic */ void lambda$delete$3(final BaseDataItem baseDataItem, final int i, final DeletionTask.OnDeletionCompleteListener onDeletionCompleteListener, FragmentActivity fragmentActivity, DialogInterface dialogInterface, int i2) {
        ProcessTask processTask = new ProcessTask(new ProcessTask.ProcessCallback<Void, List<IStoragePermissionStrategy.PermissionResult>, Integer>() { // from class: com.miui.gallery.model.BaseDataSet.1
            @Override // com.miui.gallery.ui.ProcessTask.ProcessCallback
            public List<IStoragePermissionStrategy.PermissionResult> doPrepare(Void[] voidArr) {
                return StorageSolutionProvider.get().checkPermission(baseDataItem, 2, IStoragePermissionStrategy.Permission.DELETE);
            }

            @Override // com.miui.gallery.ui.ProcessTask.ProcessCallback
            public Integer doProcess(Void[] voidArr) {
                BaseDataSet baseDataSet = BaseDataSet.this;
                return Integer.valueOf(baseDataSet.doDelete(i, baseDataItem, baseDataSet.deletingIncludeCloud()));
            }
        });
        processTask.setCompleteListener(new ProcessTask.OnCompleteListener() { // from class: com.miui.gallery.model.BaseDataSet$$ExternalSyntheticLambda2
            @Override // com.miui.gallery.ui.ProcessTask.OnCompleteListener
            public final void onCompleteProcess(Object obj) {
                BaseDataSet.lambda$delete$1(DeletionTask.OnDeletionCompleteListener.this, (Integer) obj);
            }
        });
        final WeakReference weakReference = new WeakReference(fragmentActivity);
        processTask.setOnPrepareCompleteListener(new ProcessTask.OnPrepareCompleteListener() { // from class: com.miui.gallery.model.BaseDataSet$$ExternalSyntheticLambda3
            @Override // com.miui.gallery.ui.ProcessTask.OnPrepareCompleteListener
            public final boolean onPrepareComplete(Object obj) {
                boolean lambda$delete$2;
                lambda$delete$2 = BaseDataSet.lambda$delete$2(weakReference, (List) obj);
                return lambda$delete$2;
            }
        });
        processTask.showProgress(fragmentActivity, fragmentActivity.getString(R.string.delete_in_process));
        processTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    public static /* synthetic */ void lambda$delete$1(DeletionTask.OnDeletionCompleteListener onDeletionCompleteListener, Integer num) {
        if (onDeletionCompleteListener != null) {
            onDeletionCompleteListener.onDeleted(num == null ? 0 : num.intValue(), null);
        }
    }

    public static /* synthetic */ boolean lambda$delete$2(WeakReference weakReference, List list) {
        Iterator it = list.iterator();
        boolean z = true;
        while (it.hasNext()) {
            IStoragePermissionStrategy.PermissionResult permissionResult = (IStoragePermissionStrategy.PermissionResult) it.next();
            if (!permissionResult.granted) {
                FragmentActivity fragmentActivity = (FragmentActivity) weakReference.get();
                if (fragmentActivity == null) {
                    return false;
                }
                StorageSolutionProvider.get().requestPermission(fragmentActivity, permissionResult.path, permissionResult.type);
                z = false;
            }
        }
        return z;
    }

    public final String getDeleteDialogMessage(BaseDataItem baseDataItem, Context context) {
        if (deletingIncludeCloud() && SyncUtil.existXiaomiAccount(context)) {
            if (!baseDataItem.isBurstItem()) {
                return context.getResources().getQuantityString(BaseFileMimeUtil.isVideoFromMimeType(baseDataItem.getMimeType()) ? R.plurals.delete_video_from_all_devices_and_cloud_msg : R.plurals.delete_photo_from_all_devices_and_cloud_msg, 1, 1);
            }
            int size = baseDataItem.getBurstGroup() == null ? 0 : baseDataItem.getBurstGroup().size();
            return context.getResources().getQuantityString(R.plurals.delete_burst_photo_from_all_devices_and_cloud_msg, size, Integer.valueOf(size));
        } else if (baseDataItem.isBurstItem()) {
            int size2 = baseDataItem.getBurstGroup() == null ? 0 : baseDataItem.getBurstGroup().size();
            return context.getResources().getQuantityString(R.plurals.delete_third_party_burst_photo, size2, Integer.valueOf(size2));
        } else {
            return context.getResources().getString(baseDataItem.isVideo() ? R.string.delete_third_party_video : R.string.delete_third_party_photo);
        }
    }

    public boolean addNewFile(String str, int i) {
        this.mBaseDataCache.clear();
        return false;
    }

    public boolean replaceFile(String str, String str2) {
        this.mBaseDataCache.clear();
        return false;
    }
}
