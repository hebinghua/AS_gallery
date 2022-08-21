package com.miui.gallery.model.datalayer.repository.photo;

import android.net.Uri;
import android.util.Pair;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.PhotoDetailInfo;
import com.miui.gallery.model.dto.PageResults;
import io.reactivex.Flowable;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public interface ICommonCloudModel {
    Flowable<String> doEditPhotoDateTime(long j, long j2, boolean z);

    Flowable<String> doEditPhotoDateTime(String str, long j, boolean z);

    Flowable<String> doRenamePhoto(long j, String str, String str2);

    Flowable<PhotoDetailInfo> getPhotoDetailInfo(BaseDataItem baseDataItem);

    @Deprecated
    Flowable<List<Pair<String, Byte[]>>> queryItemPath(Uri... uriArr);

    @Deprecated
    Flowable<PageResults<List<Pair<String, Byte[]>>>> queryItemPath(Long... lArr);

    Flowable<PageResults<Map<Long, Integer>>> queryMediaTypeCount(long[] jArr);

    Flowable<PageResults<Map<Long, Integer>>> queryMediaTypeCount(long[] jArr, int i, String[] strArr);
}
