package com.miui.gallery.ui.photoPage.bars.data;

import androidx.lifecycle.LifecycleOwner;
import androidx.loader.content.Loader;
import com.miui.gallery.app.fragment.GalleryFragment;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.ui.photoPage.bars.data.DataProvider;

/* loaded from: classes2.dex */
public interface IDataProvider {
    void cancelBackgroundLoad();

    Loader getCurrentPhotoLoader();

    DataProvider.FieldData getFieldData();

    DataProvider.ViewModelData getViewModelData();

    boolean isProcessingMedia(BaseDataItem baseDataItem);

    void loadInBackground();

    void onContentChanged();

    void release(GalleryFragment galleryFragment);

    void startLoadData(LifecycleOwner lifecycleOwner, DataProvider.ViewModelData.IBaseDataSetObserver iBaseDataSetObserver, DataProvider.ViewModelData.IProcessingMediaMapObserver iProcessingMediaMapObserver, DataProvider.ViewModelData.IIsFirstLoadProcessingMediaObserver iIsFirstLoadProcessingMediaObserver);
}
