package com.miui.gallery.util.photoview;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Size;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.miui.gallery.Config$ThumbConfig;
import com.miui.gallery.R;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.util.ProcessingMediaHelper;
import com.miui.gallery.util.Scheme;
import com.miui.gallery.util.logger.DefaultLogger;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public class PhotoPageDataCache {
    public static volatile PhotoPageDataCache sInstance;
    public List<ItemViewInfo> mAppointedInfos;
    public HashMap<String, Bundle> mArgumentsMap;
    public long mIdForPicker = -1;
    public HashMap<String, WeakReference<ViewGroup>> mItemViewParentMap;
    public long mPairKey;

    public static PhotoPageDataCache getInstance() {
        if (sInstance == null) {
            synchronized (PhotoPageDataCache.class) {
                if (sInstance == null) {
                    sInstance = new PhotoPageDataCache();
                }
            }
        }
        return sInstance;
    }

    public void setArguments(Bundle bundle, String str) {
        this.mPairKey++;
        if (this.mArgumentsMap == null) {
            this.mArgumentsMap = new HashMap<>();
        }
        this.mArgumentsMap.put(str, bundle);
    }

    public Bundle getArguments(String str) {
        HashMap<String, Bundle> hashMap = this.mArgumentsMap;
        if (hashMap == null) {
            return null;
        }
        return hashMap.get(str);
    }

    public void setItemViewParent(ViewGroup viewGroup, String str) {
        if (viewGroup != null) {
            if (this.mItemViewParentMap == null) {
                this.mItemViewParentMap = new HashMap<>();
            }
            this.mItemViewParentMap.put(str, new WeakReference<>(viewGroup));
        }
    }

    public final ViewGroup getViewParent(String str) {
        WeakReference<ViewGroup> weakReference;
        HashMap<String, WeakReference<ViewGroup>> hashMap = this.mItemViewParentMap;
        if (hashMap == null || hashMap.isEmpty() || str == null || (weakReference = this.mItemViewParentMap.get(str)) == null || weakReference.get() == null) {
            return null;
        }
        return weakReference.get();
    }

    public void setPickerEnterItemId(long j) {
        this.mIdForPicker = j;
    }

    public ItemViewInfo getItemViewInfo(String str, long j, int i) {
        long j2 = this.mIdForPicker;
        if (j2 != -1 && i == 0) {
            j = j2;
        }
        ViewGroup viewParent = getViewParent(str);
        if (viewParent != null) {
            int childCount = viewParent.getChildCount();
            if (j != -1) {
                for (int i2 = 0; i2 < childCount; i2++) {
                    View childAt = viewParent.getChildAt(i2);
                    Object tag = childAt.getTag(R.id.tag_item_unique_id);
                    if (tag != null && ((Long) tag).longValue() == j) {
                        return ItemViewInfo.getImageInfo(childAt, j);
                    }
                }
                List<ItemViewInfo> list = this.mAppointedInfos;
                if (list != null) {
                    for (ItemViewInfo itemViewInfo : list) {
                        if (itemViewInfo.getItemId() == j) {
                            return itemViewInfo;
                        }
                    }
                }
            }
            if ((viewParent instanceof ScrollableView) && (viewParent instanceof RecyclerView)) {
                RecyclerView recyclerView = (RecyclerView) viewParent;
                int translatePosition = ((ScrollableView) viewParent).translatePosition(i);
                if (translatePosition != -1) {
                    int i3 = Integer.MAX_VALUE;
                    int i4 = Integer.MIN_VALUE;
                    View view = null;
                    View view2 = null;
                    for (int i5 = 0; i5 < childCount; i5++) {
                        View childAt2 = viewParent.getChildAt(i5);
                        int childAdapterPosition = recyclerView.getChildAdapterPosition(childAt2);
                        if (childAdapterPosition != -1) {
                            if (childAdapterPosition == translatePosition) {
                                return ItemViewInfo.getImageInfo(childAt2, translatePosition);
                            }
                            if (childAdapterPosition < i3) {
                                view = childAt2;
                                i3 = childAdapterPosition;
                            }
                            if (childAdapterPosition > i4) {
                                view2 = childAt2;
                                i4 = childAdapterPosition;
                            }
                        }
                    }
                    if (view != null && translatePosition < i3) {
                        ItemViewInfo imageInfo = ItemViewInfo.getImageInfo(view, i3);
                        return new ItemViewInfo(translatePosition, imageInfo.getX(), imageInfo.getY() - imageInfo.getHeight(), imageInfo.getWidth(), imageInfo.getHeight());
                    } else if (view2 != null && translatePosition > i4) {
                        ItemViewInfo imageInfo2 = ItemViewInfo.getImageInfo(view2, i3);
                        return new ItemViewInfo(translatePosition, imageInfo2.getX(), imageInfo2.getY() + imageInfo2.getHeight(), imageInfo2.getWidth(), imageInfo2.getHeight());
                    } else {
                        i = translatePosition;
                    }
                }
            }
        }
        List<ItemViewInfo> list2 = this.mAppointedInfos;
        if (list2 != null) {
            for (ItemViewInfo itemViewInfo2 : list2) {
                if (itemViewInfo2.getPosition() == i) {
                    return itemViewInfo2;
                }
            }
        }
        return null;
    }

    public final ArrayList<ItemViewInfo> getAllItemInfos(String str) {
        ViewGroup viewParent = getViewParent(str);
        ArrayList<ItemViewInfo> arrayList = new ArrayList<>();
        if (viewParent != null) {
            int childCount = viewParent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = viewParent.getChildAt(i);
                Object tag = childAt.getTag(R.id.tag_item_unique_id);
                if (tag != null) {
                    arrayList.add(ItemViewInfo.getImageInfo(childAt, ((Long) tag).longValue()));
                }
            }
        } else {
            List<ItemViewInfo> list = this.mAppointedInfos;
            if (list != null) {
                arrayList.addAll(list);
            }
        }
        return arrayList;
    }

    public void saveInstance(Bundle bundle, String str) {
        Bundle arguments = getArguments(str);
        if (arguments != null) {
            arguments.putBoolean("photo_enter_transit", false);
            bundle.putBundle("key_save_arguments", getArguments(str));
        }
        bundle.putParcelableArrayList("key_save_iteminfos", getAllItemInfos(str));
    }

    public void restoreInstance(Bundle bundle, String str) {
        setArguments(bundle.getBundle("key_save_arguments"), str);
        setAppointedItemViewInfos(bundle.getParcelableArrayList("key_save_iteminfos"));
    }

    public void setAppointedItemViewInfos(List<ItemViewInfo> list) {
        this.mAppointedInfos = list;
    }

    public void clear(String str) {
        long j = this.mPairKey - 1;
        this.mPairKey = j;
        if (j == 0) {
            DefaultLogger.d("PhotoPageDataCache", "clear");
            HashMap<String, Bundle> hashMap = this.mArgumentsMap;
            if (hashMap != null && str != null) {
                hashMap.remove(str);
            }
            this.mAppointedInfos = null;
            HashMap<String, WeakReference<ViewGroup>> hashMap2 = this.mItemViewParentMap;
            if (hashMap2 != null && str != null) {
                hashMap2.remove(str);
            }
            this.mIdForPicker = -1L;
        }
    }

    public static void preload(Context context, ImageLoadParams imageLoadParams) {
        if (imageLoadParams == null || imageLoadParams.getRequestOptions() == null) {
            return;
        }
        RequestOptions requestOptions = imageLoadParams.getRequestOptions();
        String path = imageLoadParams.getPath();
        if (Scheme.ofUri(path) == Scheme.UNKNOWN) {
            path = Scheme.FILE.wrap(imageLoadParams.getPath());
        }
        if (ProcessingMediaHelper.getInstance().isMediaInProcessing(path)) {
            requestOptions = Config$ThumbConfig.markAsTemp(requestOptions);
        }
        if (ProcessingMediaHelper.getInstance().isBlurred(path)) {
            requestOptions = Config$ThumbConfig.applyProcessingOptions(requestOptions);
        }
        Size targetSize = imageLoadParams.getTargetSize();
        if (targetSize == null) {
            return;
        }
        Glide.with(context).mo985asBitmap().mo962load(GalleryModel.of(path)).mo946apply((BaseRequestOptions<?>) requestOptions).mo974priority(Priority.IMMEDIATE).preload(targetSize.getWidth(), targetSize.getHeight());
    }

    public boolean isItemVisible(long j, int i, String str) {
        int childCount;
        long j2 = this.mIdForPicker;
        if (j2 != -1 && i == 0) {
            j = j2;
        }
        ViewGroup viewParent = getViewParent(str);
        if (viewParent != null && (childCount = viewParent.getChildCount()) > 0) {
            if (j != -1) {
                for (int i2 = 0; i2 < childCount; i2++) {
                    Object tag = viewParent.getChildAt(i2).getTag(R.id.tag_item_unique_id);
                    if (tag != null && j == ((Long) tag).longValue()) {
                        return true;
                    }
                }
            }
            if ((viewParent instanceof ScrollableView) && (viewParent instanceof RecyclerView)) {
                RecyclerView recyclerView = (RecyclerView) viewParent;
                int translatePosition = ((ScrollableView) viewParent).translatePosition(i);
                if (translatePosition != -1) {
                    for (int i3 = 0; i3 < childCount; i3++) {
                        int childAdapterPosition = recyclerView.getChildAdapterPosition(viewParent.getChildAt(i3));
                        if (childAdapterPosition != -1 && translatePosition == childAdapterPosition) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean viewToPosition(int i, String str) {
        if (this.mIdForPicker == -1 || i != 0) {
            ViewGroup viewParent = getViewParent(str);
            if (!(viewParent instanceof ScrollableView)) {
                return false;
            }
            ((ScrollableView) viewParent).viewToPosition(i);
            return true;
        }
        return false;
    }

    public void postShowPlaceholderItem(int i, String str) {
        ViewGroup viewParent = getViewParent(str);
        if (this.mIdForPicker != -1 && i == 0 && (viewParent instanceof ScrollableView)) {
            int childCount = viewParent.getChildCount();
            for (int i2 = 0; i2 < childCount; i2++) {
                View childAt = viewParent.getChildAt(i2);
                Object tag = childAt.getTag(R.id.tag_item_unique_id);
                if (tag != null && this.mIdForPicker == ((Long) tag).longValue()) {
                    ((ScrollableView) viewParent).postShowPlaceholderItem(childAt);
                    return;
                }
            }
        } else if (viewParent instanceof ScrollableView) {
            ((ScrollableView) viewParent).postShowPlaceholderItem(i);
        }
    }

    public void exeShowPlaceholderItem(String str) {
        ViewGroup viewParent = getViewParent(str);
        if (viewParent instanceof ScrollableView) {
            ((ScrollableView) viewParent).exeShowPlaceholderItem();
        }
    }

    public void hidePlaceholderItem(String str) {
        ViewGroup viewParent = getViewParent(str);
        if (viewParent instanceof ScrollableView) {
            ((ScrollableView) viewParent).hidePlaceholderItem();
        }
    }

    public String getString(String str, String str2, String str3) {
        if (this.mArgumentsMap == null) {
            return null;
        }
        if (TextUtils.isEmpty(str3)) {
            for (String str4 : this.mArgumentsMap.keySet()) {
                if (this.mArgumentsMap.get(str4) != null && this.mArgumentsMap.get(str4).containsKey(str4)) {
                    return this.mArgumentsMap.get(str4).getString(str);
                }
            }
        }
        return str2;
    }
}
