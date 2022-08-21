package com.miui.gallery.viewmodel;

import android.text.TextUtils;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.miui.gallery.adapter.SortBy;
import com.miui.gallery.arch.viewmodel.BaseViewModel;

/* loaded from: classes2.dex */
public class AlbumDetailViewModel extends BaseViewModel {
    public boolean mIsAlbumGroup;
    public SortBy mSortBy;
    public String mSortOrder;
    public boolean mIsTimeGroup = true;
    public final MutableLiveData<Boolean> mIsNeedReorder = new MutableLiveData<>();
    public final MutableLiveData<Integer> mSwitchOperationId = new MutableLiveData<>();

    public void initSortInfo(SortBy sortBy, String str) {
        if (this.mSortBy != null || !TextUtils.isEmpty(this.mSortOrder)) {
            return;
        }
        this.mSortBy = sortBy;
        this.mSortOrder = str;
    }

    public void notifyReorder() {
        this.mIsNeedReorder.setValue(Boolean.TRUE);
    }

    public void doSwitchOperation(int i) {
        this.mSwitchOperationId.setValue(Integer.valueOf(i));
    }

    public LiveData<Boolean> isNeedReorder() {
        return this.mIsNeedReorder;
    }

    public LiveData<Integer> getSwitchOperationId() {
        return this.mSwitchOperationId;
    }

    public void setSortBy(SortBy sortBy) {
        this.mSortBy = sortBy;
    }

    public SortBy getSortBy() {
        return this.mSortBy;
    }

    public void setSortOrder(String str) {
        this.mSortOrder = str;
    }

    public String getSortOrder() {
        return this.mSortOrder;
    }

    public boolean isTimeGroup() {
        return this.mIsTimeGroup;
    }

    public void setIsTimeGroup(boolean z) {
        this.mIsTimeGroup = z;
    }

    public void setIsAlbumGroup(boolean z) {
        this.mIsAlbumGroup = z;
    }

    public boolean isAlbumGroup() {
        return this.mIsAlbumGroup;
    }
}
