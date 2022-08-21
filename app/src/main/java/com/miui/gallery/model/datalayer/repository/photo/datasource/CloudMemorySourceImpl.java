package com.miui.gallery.model.datalayer.repository.photo.datasource;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Pair;
import androidx.loader.content.Loader;
import com.miui.gallery.model.datalayer.utils.LoaderFlowableOnSubscribe;
import com.miui.gallery.model.datalayer.utils.loader.CustomCursorLoader;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.InternalContract$Media;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Callable;
import miuix.core.util.Pools;

/* loaded from: classes2.dex */
public class CloudMemorySourceImpl implements ICloudDataSource {
    public final WeakReference<Context> mContext;

    @Override // com.miui.gallery.model.datalayer.repository.album.IBaseDataSource
    public int getSourceType() {
        return 1;
    }

    public CloudMemorySourceImpl(Context context) {
        this.mContext = new WeakReference<>(context);
    }

    @Override // com.miui.gallery.model.datalayer.repository.photo.datasource.ICloudDataSource
    public Flowable<Map<Long, Integer>> queryMediaTypeCount(final long[] jArr, final int i, final String[] strArr) {
        return Flowable.create(new LoaderFlowableOnSubscribe<Cursor, Map<Long, Integer>>() { // from class: com.miui.gallery.model.datalayer.repository.photo.datasource.CloudMemorySourceImpl.1
            @Override // com.miui.gallery.model.datalayer.utils.LoaderFlowableOnSubscribe
            public Loader<Cursor> getLoader() {
                DefaultLogger.fd("CloudMemorySourceImpl", "build queryMediaTypeCount loader");
                StringBuilder acquire = Pools.getStringBuilderPool().acquire();
                try {
                    CustomCursorLoader customCursorLoader = new CustomCursorLoader((Context) CloudMemorySourceImpl.this.mContext.get(), true);
                    customCursorLoader.setProjection(new String[]{"specialTypeFlags", "mimeType", "title", "localFile", "thumbnailFile"});
                    long j = 0;
                    for (long j2 : jArr) {
                        j |= Long.valueOf(j2).longValue();
                    }
                    String format = String.format(Locale.US, InternalContract$Media.SELECTION_FORMAT_QUERY_MEDIA_GROUP, Long.valueOf(j));
                    int i2 = i;
                    if (1 == i2 || 2 == i2) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(format);
                        sb.append(" AND ");
                        sb.append(i == 1 ? "serverType=1" : "serverType=2");
                        format = sb.toString();
                    }
                    if (strArr != null) {
                        format = format + " AND " + String.format("%s NOT IN ('%s')", "mimeType", TextUtils.join("','", strArr));
                    }
                    customCursorLoader.setSelection(format);
                    customCursorLoader.setUri(GalleryContract.Media.URI);
                    return customCursorLoader;
                } finally {
                    Pools.getStringBuilderPool().release(acquire);
                }
            }

            @Override // com.miui.gallery.model.datalayer.utils.LoaderFlowableOnSubscribe
            public void subscribe(FlowableEmitter<Map<Long, Integer>> flowableEmitter, Cursor cursor) {
                if (cursor == null || cursor.isClosed() || cursor.getCount() == 0) {
                    flowableEmitter.onNext(new LinkedHashMap());
                    DefaultLogger.fd("CloudMemorySourceImpl", "queryMediaTypeCount result is empty");
                    return;
                }
                Map<Long, Integer> groupMediaByType = Utils.groupMediaByType(cursor, 0, 1, 2, 3, 4, jArr, false);
                DefaultLogger.fd("CloudMemorySourceImpl", "queryMediaTypeCount result is %s", groupMediaByType.toString());
                flowableEmitter.onNext(groupMediaByType);
            }
        }, BackpressureStrategy.LATEST);
    }

    @Override // com.miui.gallery.model.datalayer.repository.photo.datasource.ICloudDataSource
    public Flowable<List<Pair<String, Byte[]>>> queryItemPath(final Long... lArr) {
        return Flowable.fromCallable(new Callable<List<Pair<String, Byte[]>>>() { // from class: com.miui.gallery.model.datalayer.repository.photo.datasource.CloudMemorySourceImpl.2
            @Override // java.util.concurrent.Callable
            public List<Pair<String, Byte[]>> call() throws Exception {
                String str;
                Long[] lArr2 = lArr;
                if (lArr2 == null || lArr2.length == 0) {
                    return Collections.emptyList();
                }
                if (lArr2.length == 1) {
                    str = "_id = " + lArr[0];
                } else {
                    str = "_id IN (" + TextUtils.join(",", Arrays.asList(lArr)) + ")";
                }
                return (List) SafeDBUtil.safeQuery((Context) CloudMemorySourceImpl.this.mContext.get(), GalleryContract.Media.URI, new String[]{"alias_clear_thumbnail", "secretKey"}, str, (String[]) null, (String) null, new SafeDBUtil.QueryHandler<List<Pair<String, Byte[]>>>() { // from class: com.miui.gallery.model.datalayer.repository.photo.datasource.CloudMemorySourceImpl.2.1
                    @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
                    /* renamed from: handle  reason: collision with other method in class */
                    public List<Pair<String, Byte[]>> mo1808handle(Cursor cursor) {
                        if (cursor != null && cursor.moveToFirst()) {
                            LinkedList linkedList = new LinkedList();
                            do {
                                byte[] blob = cursor.getBlob(1);
                                Byte[] bArr = null;
                                if (blob != null) {
                                    bArr = new Byte[blob.length];
                                    for (int i = 0; i < blob.length; i++) {
                                        bArr[i] = Byte.valueOf(blob[i]);
                                    }
                                }
                                linkedList.add(Pair.create(cursor.getString(0), bArr));
                            } while (cursor.moveToNext());
                            return linkedList;
                        }
                        return Collections.emptyList();
                    }
                });
            }
        });
    }
}
