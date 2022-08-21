package com.miui.gallery.loader;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.StringBuilderPrinter;
import androidx.fragment.app.FragmentActivity;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.joran.action.Action;
import com.miui.gallery.loader.StorageSetLoader;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.BaseDataSet;
import com.miui.gallery.model.StorageItem;
import com.miui.gallery.provider.cloudmanager.remark.RemarkManager;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.trash.TrashManager;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.ContentUtils;
import com.miui.gallery.util.MediaAndAlbumOperations;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.logger.TimingTracing;
import com.xiaomi.mirror.synergy.CallMethod;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class StorageSetLoader extends BaseLoader {
    public String mFolderPath;
    public String mInitFilePath;
    public Uri mInitUri;
    public boolean mIsDeletingIncludeCloud;
    public ArrayList<Uri> mLimitUris;

    public StorageSetLoader(Context context, Uri uri, Bundle bundle) {
        super(context);
        this.mInitFilePath = null;
        this.mInitUri = uri;
        this.mIsDeletingIncludeCloud = bundle != null && bundle.getBoolean("com.miui.gallery.extra.deleting_include_cloud", false);
        String path = uri.getPath();
        if (new File(path).isDirectory()) {
            this.mFolderPath = path;
            return;
        }
        this.mLimitUris = bundle.getParcelableArrayList("com.miui.gallery.extra.photo_items");
        if (bundle.getBoolean("com.miui.gallery.extra.preview_single_item", false)) {
            ArrayList<Uri> arrayList = new ArrayList<>(1);
            this.mLimitUris = arrayList;
            arrayList.add(Uri.fromFile(new File(path)));
        }
        if (isLimitedUris()) {
            return;
        }
        this.mFolderPath = BaseFileUtils.getParentFolderPath(path);
    }

    public final boolean isLimitedUris() {
        return this.mLimitUris != null;
    }

    @Override // androidx.loader.content.AsyncTaskLoader
    /* renamed from: loadInBackground */
    public BaseDataSet mo1444loadInBackground() {
        ArrayList arrayList;
        ArrayList arrayList2;
        TimingTracing.beginTracing(isLimitedUris() ? "StorageSetLoader_limited_load" : "StorageSetLoader_load", "loadInBackground");
        try {
            if (TextUtils.isEmpty(this.mInitFilePath)) {
                String validFilePathForContentUri = ContentUtils.getValidFilePathForContentUri(getContext(), this.mInitUri);
                this.mInitFilePath = validFilePathForContentUri;
                if (TextUtils.isEmpty(validFilePathForContentUri)) {
                    this.mInitFilePath = this.mInitUri.getPath();
                }
            }
            if (isLimitedUris()) {
                arrayList = new ArrayList(this.mLimitUris.size());
                Iterator<Uri> it = this.mLimitUris.iterator();
                while (it.hasNext()) {
                    Uri next = it.next();
                    if (next != null && Action.FILE_ATTRIBUTE.equalsIgnoreCase(next.getScheme())) {
                        String path = next.getPath();
                        if (isSupportFile(path)) {
                            arrayList.add(new File(path));
                        }
                    }
                }
            } else {
                File[] listFiles = new File(this.mFolderPath).listFiles(new MFileFilter());
                if (listFiles != null) {
                    long currentTimeMillis = System.currentTimeMillis();
                    Arrays.sort(listFiles, new MFileComparator());
                    DefaultLogger.d("StorageSetLoader", "file sort cost[%d], size[%d]", Long.valueOf(System.currentTimeMillis() - currentTimeMillis), Integer.valueOf(listFiles.length));
                    arrayList2 = new ArrayList(Arrays.asList(listFiles));
                } else {
                    if (!TextUtils.isEmpty(this.mInitFilePath)) {
                        File file = new File(this.mInitFilePath);
                        if (file.exists()) {
                            arrayList2 = new ArrayList(1);
                            arrayList2.add(file);
                        }
                    }
                    arrayList = null;
                }
                arrayList = arrayList2;
            }
            if (arrayList != null && arrayList.size() > 0) {
                Iterator it2 = arrayList.iterator();
                while (it2.hasNext()) {
                    File file2 = (File) it2.next();
                    if (file2 != null && RemarkManager.CacheMarkManager.isMarkPath(file2.getAbsolutePath())) {
                        it2.remove();
                    }
                }
            }
            return new StorageDataSet(arrayList, this.mInitFilePath);
        } finally {
            long stopTracing = TimingTracing.stopTracing(null);
            if (stopTracing > 500) {
                HashMap hashMap = new HashMap();
                hashMap.put("cost_time", isLimitedUris() + "_" + stopTracing);
                SamplingStatHelper.recordCountEvent("load_performance", "StorageSetLoader", hashMap);
            }
        }
    }

    public final boolean isSupportType(String str) {
        return BaseFileMimeUtil.isImageFromMimeType(str) || BaseFileMimeUtil.isVideoFromMimeType(str);
    }

    public final boolean isSupportFile(String str) {
        boolean isSupportType = isSupportType(BaseFileMimeUtil.getMimeType(str));
        return (isSupportType || !str.equalsIgnoreCase(this.mInitFilePath)) ? isSupportType : isSupportType | isSupportType(BaseFileMimeUtil.getMimeTypeByParseFile(str));
    }

    /* loaded from: classes2.dex */
    public class MFileFilter implements FileFilter {
        public MFileFilter() {
        }

        @Override // java.io.FileFilter
        public boolean accept(File file) {
            if (file.isFile()) {
                return StorageSetLoader.this.isSupportFile(file.getAbsolutePath());
            }
            return false;
        }
    }

    /* loaded from: classes2.dex */
    public static class MFileComparator implements Comparator<File> {
        public MFileComparator() {
        }

        @Override // java.util.Comparator
        public int compare(File file, File file2) {
            int i = (file2.lastModified() > file.lastModified() ? 1 : (file2.lastModified() == file.lastModified() ? 0 : -1));
            if (i > 0) {
                return 1;
            }
            return i < 0 ? -1 : 0;
        }
    }

    /* loaded from: classes2.dex */
    public class StorageDataSet extends BaseDataSet {
        public List<File> mFiles;
        public String mInitFilePath;

        @Override // com.miui.gallery.model.BaseDataSet
        public void onRelease() {
        }

        public StorageDataSet(List<File> list, String str) {
            this.mFiles = list;
            this.mInitFilePath = str;
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public int getInitPosition() {
            return getIndexOfItem(this.mInitFilePath);
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public BaseDataItem createItem(int i) {
            StorageItem storageItem = new StorageItem();
            bindItem(storageItem, i);
            return storageItem;
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public void bindItem(BaseDataItem baseDataItem, int i) {
            File item = getItem(i);
            if (item != null) {
                String absolutePath = item.getAbsolutePath();
                baseDataItem.setKey(item.getName().hashCode()).setTitle(BaseFileUtils.getFileTitle(BaseFileUtils.getFileName(absolutePath))).setFilePath(absolutePath).setThumbPath(absolutePath).setMimeType(BaseFileMimeUtil.getMimeType(absolutePath));
                if (!BaseFileMimeUtil.isRawFromMimeType(baseDataItem.getMimeType())) {
                    return;
                }
                baseDataItem.setSpecialTypeFlags(FileAppender.DEFAULT_BUFFER_SIZE);
            }
        }

        public final File getItem(int i) {
            List<File> list = this.mFiles;
            if (list == null || i <= -1 || i >= list.size()) {
                return null;
            }
            return this.mFiles.get(i);
        }

        public final int getIndexOfItem(String str) {
            if (this.mFiles != null && !TextUtils.isEmpty(str)) {
                int size = this.mFiles.size();
                for (int i = 0; i < size; i++) {
                    if (this.mFiles.get(i).getAbsolutePath().equalsIgnoreCase(str)) {
                        return i;
                    }
                }
            }
            return 0;
        }

        @Override // com.miui.gallery.model.BaseDataSet, com.miui.gallery.projection.IConnectController.DataSet
        public int getCount() {
            List<File> list = this.mFiles;
            if (list != null) {
                return list.size();
            }
            return 0;
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public boolean deletingIncludeCloud() {
            return StorageSetLoader.this.mIsDeletingIncludeCloud;
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public int doDelete(int i, BaseDataItem baseDataItem, boolean z) {
            File file;
            if (baseDataItem == null || TextUtils.isEmpty(baseDataItem.getOriginalPath())) {
                return 0;
            }
            TimingTracing.beginTracing("StorageSetLoader_delete", "delete");
            try {
                TimingTracing.addSplit("sdcard permission");
                String str = null;
                try {
                    TrashManager.SimpleResult moveFileToTrash = TrashManager.moveFileToTrash(baseDataItem.getOriginalPath(), 31, "StorageSetLoader");
                    if (moveFileToTrash != null) {
                        str = moveFileToTrash.getTrashPath();
                    }
                } catch (StoragePermissionMissingException unused) {
                    DefaultLogger.e("StorageSetLoader", "move file to trash failed for permission missing");
                }
                if (TextUtils.isEmpty(str)) {
                    DefaultLogger.w("StorageSetLoader", "Failed to move file to trash %s", baseDataItem.getOriginalPath());
                    return 0;
                }
                TimingTracing.addSplit("delete file");
                MediaAndAlbumOperations.deleteInService(StorageSetLoader.this.getContext(), z ? 0 : 1, 31, baseDataItem.getOriginalPath());
                TimingTracing.addSplit("service delete");
                if (StorageSetLoader.this.isLimitedUris() && (file = this.mFiles.get(i)) != null) {
                    StorageSetLoader.this.mLimitUris.remove(Uri.fromFile(file));
                }
                StringBuilder sb = new StringBuilder();
                if (TimingTracing.stopTracing(new StringBuilderPrinter(sb)) > 500) {
                    DefaultLogger.w("StorageSetLoader", "delete slowly: %s", sb.toString());
                    HashMap hashMap = new HashMap();
                    hashMap.put(CallMethod.ARG_EXTRA_STRING, sb.toString());
                    SamplingStatHelper.recordCountEvent("delete_performance", "StorageSetLoader", hashMap);
                }
                return 1;
            } finally {
                StringBuilder sb2 = new StringBuilder();
                if (TimingTracing.stopTracing(new StringBuilderPrinter(sb2)) > 500) {
                    DefaultLogger.w("StorageSetLoader", "delete slowly: %s", sb2.toString());
                    HashMap hashMap2 = new HashMap();
                    hashMap2.put(CallMethod.ARG_EXTRA_STRING, sb2.toString());
                    SamplingStatHelper.recordCountEvent("delete_performance", "StorageSetLoader", hashMap2);
                }
            }
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public boolean addToAlbum(FragmentActivity fragmentActivity, int i, boolean z, boolean z2, final MediaAndAlbumOperations.OnAddAlbumListener onAddAlbumListener) {
            File item = getItem(i);
            if (item != null) {
                ArrayList arrayList = new ArrayList(1);
                arrayList.add(Uri.fromFile(item));
                final String path = item.getPath();
                MediaAndAlbumOperations.addToAlbum(fragmentActivity, new MediaAndAlbumOperations.OnAddAlbumListener() { // from class: com.miui.gallery.loader.StorageSetLoader$StorageDataSet$$ExternalSyntheticLambda0
                    @Override // com.miui.gallery.util.MediaAndAlbumOperations.OnAddAlbumListener
                    public final void onComplete(long[] jArr, boolean z3) {
                        StorageSetLoader.StorageDataSet.lambda$addToAlbum$0(path, onAddAlbumListener, jArr, z3);
                    }
                }, arrayList, z2, BaseFileMimeUtil.isVideoFromMimeType(BaseFileMimeUtil.getMimeType(path)));
            }
            return true;
        }

        public static /* synthetic */ void lambda$addToAlbum$0(String str, MediaAndAlbumOperations.OnAddAlbumListener onAddAlbumListener, long[] jArr, boolean z) {
            if (z && jArr != null && jArr[0] > 0) {
                RemarkManager.CacheMarkManager.markData(str);
            }
            if (onAddAlbumListener != null) {
                onAddAlbumListener.onComplete(jArr, z);
            }
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public long getItemKey(int i) {
            File item = getItem(i);
            if (item != null) {
                return item.getName().hashCode();
            }
            return -1L;
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public String getItemPath(int i) {
            File item = getItem(i);
            if (item != null) {
                return item.getAbsolutePath();
            }
            return null;
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public boolean addNewFile(String str, int i) {
            if (!TextUtils.isEmpty(str) && StorageSetLoader.this.isLimitedUris()) {
                StorageSetLoader.this.mLimitUris.add(Math.min(i, StorageSetLoader.this.mLimitUris.size()), Uri.fromFile(new File(str)));
                return true;
            }
            return super.addNewFile(str, i);
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public boolean replaceFile(String str, String str2) {
            int indexOf;
            if (!TextUtils.isEmpty(str2) && StorageSetLoader.this.isLimitedUris() && (indexOf = StorageSetLoader.this.mLimitUris.indexOf(Uri.fromFile(new File(str)))) != -1) {
                StorageSetLoader.this.mLimitUris.set(indexOf, Uri.fromFile(new File(str2)));
                return true;
            }
            return super.replaceFile(str, str2);
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public boolean addToFavorites(FragmentActivity fragmentActivity, int i, MediaAndAlbumOperations.OnCompleteListener onCompleteListener) {
            BaseDataItem item = getItem(null, i);
            if (item != null && !TextUtils.isEmpty(item.getOriginalPath())) {
                MediaAndAlbumOperations.addToFavoritesByPath(fragmentActivity, onCompleteListener, item.getOriginalPath());
            }
            return true;
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public boolean removeFromFavorites(FragmentActivity fragmentActivity, int i, MediaAndAlbumOperations.OnCompleteListener onCompleteListener) {
            BaseDataItem item = getItem(null, i);
            if (item != null && !TextUtils.isEmpty(item.getOriginalPath())) {
                MediaAndAlbumOperations.removeFromFavoritesByPath(fragmentActivity, onCompleteListener, item.getOriginalPath());
            }
            return true;
        }
    }
}
