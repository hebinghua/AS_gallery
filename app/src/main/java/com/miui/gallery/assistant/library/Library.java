package com.miui.gallery.assistant.library;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;
import com.google.gson.annotations.SerializedName;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.dao.base.Entity;
import com.miui.gallery.dao.base.TableColumn;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.GsonUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import org.json.JSONException;

/* loaded from: classes.dex */
public class Library extends Entity {
    @SerializedName("lastPage")
    private boolean mIsLastPage;
    public long mLibraryId;
    @SerializedName("galleryResourceInfoList")
    private ArrayList<LibraryItem> mLibraryItems;
    public LibraryStatus mLibraryStatus = LibraryStatus.STATE_DEFAULT;
    public long mRefreshTime;
    @SerializedName("syncToken")
    private String mSyncToken;

    /* loaded from: classes.dex */
    public enum LibraryStatus {
        STATE_DEFAULT,
        STATE_NO_LIBRARY_INFO,
        STATE_NOT_DOWNLOADED,
        STATE_DOWNLOADING,
        STATE_ABI_UNAVAILABLE,
        STATE_AVAILABLE,
        STATE_LOADED
    }

    public boolean isOverDue() {
        return System.currentTimeMillis() > this.mRefreshTime + 259200000;
    }

    public void setLibraryStatus(LibraryStatus libraryStatus) {
        this.mLibraryStatus = libraryStatus;
    }

    public LibraryStatus getLibraryStatus() {
        return this.mLibraryStatus;
    }

    public synchronized boolean load() {
        if (BaseMiscUtil.isValid(this.mLibraryItems)) {
            ensureLibraryDependency();
            Iterator<LibraryItem> it = this.mLibraryItems.iterator();
            while (it.hasNext()) {
                LibraryItem next = it.next();
                if (!next.isLoaded() && next.isTypeSo()) {
                    if (next.isLocal()) {
                        try {
                            System.loadLibrary(next.getName());
                            next.setLoaded(true);
                        } catch (Exception unused) {
                            DefaultLogger.e("Library", "Load internal local library %d error", Long.valueOf(next.getId()));
                        }
                    } else {
                        File file = new File(next.getTargetPath(GalleryApp.sGetAndroidContext(), this.mLibraryId));
                        if (file.exists()) {
                            if (loadInternal(file.getAbsolutePath())) {
                                DefaultLogger.d("Library", "Library loaded success:" + file);
                                next.setLoaded(true);
                            } else {
                                DefaultLogger.d("Library", "Library load failed:" + file);
                            }
                        }
                    }
                }
            }
        }
        return isLoaded();
    }

    public final void ensureLibraryDependency() {
        if (BaseMiscUtil.isValid(this.mLibraryItems)) {
            LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue(this.mLibraryItems.size());
            LinkedList<LibraryItem> linkedList = new LinkedList();
            LinkedList linkedList2 = new LinkedList(this.mLibraryItems);
            Iterator it = linkedList2.iterator();
            while (it.hasNext()) {
                LibraryItem libraryItem = (LibraryItem) it.next();
                if (libraryItem.getParentId() == -1) {
                    linkedList.add(libraryItem);
                    it.remove();
                }
            }
            LinkedList linkedList3 = new LinkedList();
            while (!linkedList.isEmpty()) {
                linkedList3.clear();
                linkedBlockingQueue.addAll(linkedList);
                for (LibraryItem libraryItem2 : linkedList) {
                    Iterator it2 = linkedList2.iterator();
                    while (it2.hasNext()) {
                        LibraryItem libraryItem3 = (LibraryItem) it2.next();
                        if (libraryItem3.getParentId() == libraryItem2.getId()) {
                            linkedList3.add(libraryItem3);
                            it2.remove();
                        }
                    }
                }
                linkedList.clear();
                linkedList.addAll(linkedList3);
            }
            synchronized (this) {
                this.mLibraryItems.clear();
                this.mLibraryItems.addAll(linkedBlockingQueue);
            }
        }
    }

    @SuppressLint({"UnsafeDynamicallyLoadedCode"})
    public final boolean loadInternal(String str) {
        try {
            DefaultLogger.d("Library", "loadInternal System.load begin");
            System.load(str);
            DefaultLogger.d("Library", "loadInternal System.load success");
            return true;
        } catch (Error e) {
            DefaultLogger.d("Library", "loadInternal System.load fail error:" + e.getMessage());
            reportAlgorithmLoadError(e);
            return false;
        }
    }

    public final void reportAlgorithmLoadError(Throwable th) {
        DefaultLogger.e("Library", th);
        HashMap hashMap = new HashMap();
        hashMap.put("algorithm", getClass().getSimpleName());
        hashMap.put("error", th != null ? th.toString() : "");
        SamplingStatHelper.recordCountEvent("assistant", "assistant_algorithm_error", hashMap);
    }

    public synchronized boolean isLoaded() {
        if (BaseMiscUtil.isValid(this.mLibraryItems)) {
            Iterator<LibraryItem> it = this.mLibraryItems.iterator();
            while (it.hasNext()) {
                LibraryItem next = it.next();
                if (next.isTypeSo() && !next.isLoaded()) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public synchronized boolean isExist() {
        if (!BaseMiscUtil.isValid(this.mLibraryItems)) {
            return false;
        }
        Iterator<LibraryItem> it = this.mLibraryItems.iterator();
        while (it.hasNext()) {
            if (!it.next().isExist(this.mLibraryId)) {
                return false;
            }
        }
        return true;
    }

    public long getLibraryId() {
        return this.mLibraryId;
    }

    public void setLibraryId(long j) {
        this.mLibraryId = j;
    }

    public List<LibraryItem> getLibraryItems() {
        return this.mLibraryItems;
    }

    public void setRefreshTime(long j) {
        this.mRefreshTime = j;
    }

    public String toString() {
        return "Library{mLibraryId=" + this.mLibraryId + '}';
    }

    @Override // com.miui.gallery.dao.base.Entity
    public List<TableColumn> getTableColumns() {
        ArrayList arrayList = new ArrayList();
        Entity.addColumn(arrayList, "libraryId", "INTEGER");
        Entity.addColumn(arrayList, "libraryItems", "TEXT");
        Entity.addColumn(arrayList, "refreshTime", "INTEGER");
        return arrayList;
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onInitFromCursor(Cursor cursor) {
        this.mLibraryId = Entity.getLong(cursor, "libraryId");
        try {
            this.mLibraryItems = GsonUtils.getArray(Entity.getString(cursor, "libraryItems"), LibraryItem.class);
        } catch (JSONException unused) {
            DefaultLogger.e("Library", "Create libraryItems of library %d from cursor error", Long.valueOf(this.mLibraryId));
        }
        this.mRefreshTime = Entity.getLong(cursor, "refreshTime");
    }

    @Override // com.miui.gallery.dao.base.Entity
    public void onConvertToContents(ContentValues contentValues) {
        contentValues.put("libraryId", Long.valueOf(this.mLibraryId));
        contentValues.put("libraryItems", GsonUtils.toString(this.mLibraryItems));
        contentValues.put("refreshTime", Long.valueOf(this.mRefreshTime));
    }

    @Override // com.miui.gallery.dao.base.Entity
    public String[] getUniqueConstraints() {
        return new String[]{"libraryId"};
    }

    public boolean isLibraryItemInfosConsistent() {
        if (BaseMiscUtil.isValid(this.mLibraryItems)) {
            String extraInfo = this.mLibraryItems.get(0).getExtraInfo();
            Iterator<LibraryItem> it = this.mLibraryItems.iterator();
            while (it.hasNext()) {
                LibraryItem next = it.next();
                if (!TextUtils.equals(extraInfo, next.getExtraInfo())) {
                    DefaultLogger.e("Library", "isLibraryItemInfosConsistent: false,lib name:%s,lib extra:%s", next.getName(), next.getExtraInfo());
                    return false;
                }
            }
            return true;
        }
        return false;
    }
}
