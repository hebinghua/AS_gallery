package com.miui.gallery.model.datalayer.repository.photo.datasource;

import android.util.Pair;
import com.miui.gallery.model.datalayer.repository.album.IBaseDataSource;
import io.reactivex.Flowable;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public interface ICloudDataSource extends IBaseDataSource {
    Flowable<List<Pair<String, Byte[]>>> queryItemPath(Long... lArr);

    Flowable<Map<Long, Integer>> queryMediaTypeCount(long[] jArr, int i, String[] strArr);
}
