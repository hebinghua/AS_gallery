package com.miui.gallery.ui;

import android.content.Context;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.adapter.CheckableAdapter;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.ResourceUtils;
import com.miui.gallery.util.ToastUtils;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class BaseProduceFilter {
    public List<CheckableAdapter.CheckedItem> mCheckItems;
    public int[] mSupportMimeRange;
    public List<String> mUnsupportedMimeTypeList = new LinkedList();
    public Context mContext = GalleryApp.sGetAndroidContext();

    public abstract void showOutOfRangeToast();

    public abstract void showUnsupportedToast();

    public BaseProduceFilter(List<CheckableAdapter.CheckedItem> list, int[] iArr, List<String> list2) {
        this.mCheckItems = list;
        this.mSupportMimeRange = iArr;
        this.mUnsupportedMimeTypeList.addAll(list2);
    }

    public boolean isSupported() {
        int size = this.mCheckItems.size();
        filter(this.mCheckItems);
        int size2 = size - this.mCheckItems.size();
        if (this.mCheckItems.size() == 0) {
            showUnsupportedToast();
            return false;
        } else if (this.mCheckItems.size() < this.mSupportMimeRange[0] || this.mCheckItems.size() > this.mSupportMimeRange[1]) {
            showOutOfRangeToast();
            return false;
        } else {
            if (size2 > 0) {
                showFilterFinishToast(size2);
            }
            return true;
        }
    }

    public void filter(List<CheckableAdapter.CheckedItem> list) {
        List<String> list2 = this.mUnsupportedMimeTypeList;
        if (list2 == null || list2.size() == 0) {
            return;
        }
        for (String str : this.mUnsupportedMimeTypeList) {
            str.hashCode();
            char c = 65535;
            switch (str.hashCode()) {
                case -1423313290:
                    if (str.equals("image/x-adobe-dng")) {
                        c = 0;
                        break;
                    }
                    break;
                case 452781974:
                    if (str.equals("video/*")) {
                        c = 1;
                        break;
                    }
                    break;
                case 1911932022:
                    if (str.equals("image/*")) {
                        c = 2;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    list.removeIf(BaseProduceFilter$$ExternalSyntheticLambda0.INSTANCE);
                    break;
                case 1:
                    list.removeIf(BaseProduceFilter$$ExternalSyntheticLambda1.INSTANCE);
                    break;
                case 2:
                    list.removeIf(BaseProduceFilter$$ExternalSyntheticLambda2.INSTANCE);
                    break;
            }
        }
    }

    public static /* synthetic */ boolean lambda$filter$0(CheckableAdapter.CheckedItem checkedItem) {
        return BaseFileMimeUtil.isVideoFromMimeType(checkedItem.getMimeType());
    }

    public static /* synthetic */ boolean lambda$filter$1(CheckableAdapter.CheckedItem checkedItem) {
        return BaseFileMimeUtil.isImageFromMimeType(checkedItem.getMimeType());
    }

    public static /* synthetic */ boolean lambda$filter$2(CheckableAdapter.CheckedItem checkedItem) {
        return BaseFileMimeUtil.isRawFromMimeType(checkedItem.getMimeType());
    }

    public void showFilterFinishToast(int i) {
        ToastUtils.makeText(this.mContext, ResourceUtils.getQuantityString(R.plurals.pic_to_pdf_filter_count, i, Integer.valueOf(i)));
    }
}
