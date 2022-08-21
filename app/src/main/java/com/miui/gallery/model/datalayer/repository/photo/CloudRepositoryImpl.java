package com.miui.gallery.model.datalayer.repository.photo;

import android.content.Context;
import android.net.Uri;
import android.util.Pair;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.PhotoDetailInfo;
import com.miui.gallery.model.datalayer.repository.AbstractCloudRepository;
import com.miui.gallery.model.dto.PageResults;
import io.reactivex.Flowable;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class CloudRepositoryImpl extends AbstractCloudRepository {
    public final ICommonCloudModel mCommonModel;

    public CloudRepositoryImpl() {
        this(GalleryApp.sGetAndroidContext());
    }

    public CloudRepositoryImpl(Context context) {
        this.mCommonModel = new CommonCloudModelImpl(context);
    }

    @Override // com.miui.gallery.model.datalayer.repository.photo.ICommonCloudModel
    public Flowable<PageResults<Map<Long, Integer>>> queryMediaTypeCount(long[] jArr) {
        return this.mCommonModel.queryMediaTypeCount(jArr);
    }

    @Override // com.miui.gallery.model.datalayer.repository.photo.ICommonCloudModel
    public Flowable<PageResults<Map<Long, Integer>>> queryMediaTypeCount(long[] jArr, int i, String[] strArr) {
        return this.mCommonModel.queryMediaTypeCount(jArr, i, strArr);
    }

    @Override // com.miui.gallery.model.datalayer.repository.photo.ICommonCloudModel
    public Flowable<PhotoDetailInfo> getPhotoDetailInfo(BaseDataItem baseDataItem) {
        return this.mCommonModel.getPhotoDetailInfo(baseDataItem);
    }

    @Override // com.miui.gallery.model.datalayer.repository.photo.ICommonCloudModel
    public Flowable<String> doEditPhotoDateTime(long j, long j2, boolean z) {
        return this.mCommonModel.doEditPhotoDateTime(j, j2, z);
    }

    @Override // com.miui.gallery.model.datalayer.repository.photo.ICommonCloudModel
    public Flowable<String> doEditPhotoDateTime(String str, long j, boolean z) {
        return this.mCommonModel.doEditPhotoDateTime(str, j, z);
    }

    @Override // com.miui.gallery.model.datalayer.repository.photo.ICommonCloudModel
    public Flowable<String> doRenamePhoto(long j, String str, String str2) {
        return this.mCommonModel.doRenamePhoto(j, str, str2);
    }

    @Override // com.miui.gallery.model.datalayer.repository.photo.ICommonCloudModel
    public Flowable<List<Pair<String, Byte[]>>> queryItemPath(Uri... uriArr) {
        return this.mCommonModel.queryItemPath(uriArr);
    }

    @Override // com.miui.gallery.model.datalayer.repository.photo.ICommonCloudModel
    public Flowable<PageResults<List<Pair<String, Byte[]>>>> queryItemPath(Long... lArr) {
        return this.mCommonModel.queryItemPath(lArr);
    }
}
