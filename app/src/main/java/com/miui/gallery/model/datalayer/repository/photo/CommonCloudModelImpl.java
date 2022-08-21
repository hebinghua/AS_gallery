package com.miui.gallery.model.datalayer.repository.photo;

import android.content.Context;
import android.net.Uri;
import android.util.Pair;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.PhotoDetailInfo;
import com.miui.gallery.model.datalayer.repository.GalleryDataRepositoryConfig$AlbumRepositoryConfig$CloudModel;
import com.miui.gallery.model.datalayer.repository.photo.datasource.ICloudDataSource;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.provider.CloudUtils;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.StorageUtils;
import io.reactivex.Flowable;
import java.lang.ref.WeakReference;
import java.nio.file.FileAlreadyExistsException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.IntFunction;
import org.reactivestreams.Publisher;

/* loaded from: classes2.dex */
public class CommonCloudModelImpl implements ICommonCloudModel {
    public WeakReference<Context> mContext;

    public CommonCloudModelImpl(Context context) {
        this.mContext = new WeakReference<>(context);
    }

    @Override // com.miui.gallery.model.datalayer.repository.photo.ICommonCloudModel
    public Flowable<PageResults<Map<Long, Integer>>> queryMediaTypeCount(long[] jArr) {
        return queryMediaTypeCount(jArr, -1, null);
    }

    @Override // com.miui.gallery.model.datalayer.repository.photo.ICommonCloudModel
    public Flowable<PageResults<Map<Long, Integer>>> queryMediaTypeCount(final long[] jArr, final int i, final String[] strArr) {
        if (i != -1 && i != 1 && i != 2) {
            return Flowable.error(new IllegalArgumentException("param mediaType only image or video"));
        }
        return Flowable.concatArrayDelayError((Publisher[]) Arrays.stream(GalleryDataRepositoryConfig$AlbumRepositoryConfig$CloudModel.getDataSources(this.mContext.get())).map(new Function<ICloudDataSource, Flowable<PageResults<Map<Long, Integer>>>>() { // from class: com.miui.gallery.model.datalayer.repository.photo.CommonCloudModelImpl.2
            @Override // java.util.function.Function
            public Flowable<PageResults<Map<Long, Integer>>> apply(ICloudDataSource iCloudDataSource) {
                return PageResults.wrapperDataToPageResult(iCloudDataSource.getSourceType(), iCloudDataSource.queryMediaTypeCount(jArr, i, strArr));
            }
        }).toArray(new IntFunction<Flowable<PageResults<Map<Long, Integer>>>[]>() { // from class: com.miui.gallery.model.datalayer.repository.photo.CommonCloudModelImpl.1
            @Override // java.util.function.IntFunction
            public Flowable<PageResults<Map<Long, Integer>>>[] apply(int i2) {
                return new Flowable[i2];
            }
        }));
    }

    @Override // com.miui.gallery.model.datalayer.repository.photo.ICommonCloudModel
    public Flowable<PhotoDetailInfo> getPhotoDetailInfo(final BaseDataItem baseDataItem) {
        return Flowable.fromCallable(new Callable<PhotoDetailInfo>() { // from class: com.miui.gallery.model.datalayer.repository.photo.CommonCloudModelImpl.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.concurrent.Callable
            /* renamed from: call */
            public PhotoDetailInfo mo1127call() throws Exception {
                PhotoDetailInfo detailInfo = baseDataItem.getDetailInfo(GalleryApp.sGetAndroidContext());
                Object detail = detailInfo.getDetail(200);
                if (detail != null) {
                    String str = (String) detail;
                    detailInfo.addDetail(201, StorageUtils.getPathForDisplay(GalleryApp.sGetAndroidContext(), str));
                    detailInfo.addDetail(109, Boolean.valueOf(StorageUtils.isInExternalStorage(GalleryApp.sGetAndroidContext(), str)));
                }
                return detailInfo;
            }
        });
    }

    @Override // com.miui.gallery.model.datalayer.repository.photo.ICommonCloudModel
    public Flowable<String> doEditPhotoDateTime(final long j, final long j2, final boolean z) {
        return Flowable.fromCallable(new Callable<String>() { // from class: com.miui.gallery.model.datalayer.repository.photo.CommonCloudModelImpl.4
            @Override // java.util.concurrent.Callable
            public String call() throws Exception {
                Pair<String, Long[]> editPhotoTimeInfoBy = CloudUtils.editPhotoTimeInfoBy(GalleryApp.sGetAndroidContext(), j, j2, null, z);
                if (editPhotoTimeInfoBy == null || CommonCloudModelImpl.this.checkIsFailed((Long[]) editPhotoTimeInfoBy.second)) {
                    throw new IllegalStateException("failed edit photoDateTime");
                }
                return (String) editPhotoTimeInfoBy.first;
            }
        });
    }

    @Override // com.miui.gallery.model.datalayer.repository.photo.ICommonCloudModel
    public Flowable<String> doEditPhotoDateTime(final String str, final long j, final boolean z) {
        return Flowable.fromCallable(new Callable<String>() { // from class: com.miui.gallery.model.datalayer.repository.photo.CommonCloudModelImpl.5
            @Override // java.util.concurrent.Callable
            public String call() throws Exception {
                Pair<String, Long[]> editPhotoTimeInfoBy = CloudUtils.editPhotoTimeInfoBy(GalleryApp.sGetAndroidContext(), 0L, j, str, z);
                if (editPhotoTimeInfoBy == null || CommonCloudModelImpl.this.checkIsFailed((Long[]) editPhotoTimeInfoBy.second)) {
                    throw new IllegalStateException("failed edit photoDateTime");
                }
                return (String) editPhotoTimeInfoBy.first;
            }
        });
    }

    @Override // com.miui.gallery.model.datalayer.repository.photo.ICommonCloudModel
    public Flowable<String> doRenamePhoto(final long j, final String str, final String str2) {
        return Flowable.fromCallable(new Callable<String>() { // from class: com.miui.gallery.model.datalayer.repository.photo.CommonCloudModelImpl.6
            @Override // java.util.concurrent.Callable
            public String call() throws Exception {
                long renameById;
                DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(BaseFileUtils.concat(BaseFileUtils.getParentFolderPath(str), str2), IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("CommonCloudModelImpl", "doRenamePhoto"));
                if (documentFile != null && documentFile.exists()) {
                    throw new FileAlreadyExistsException(str2);
                }
                if (j == -1) {
                    renameById = CloudUtils.renameByPath(GalleryApp.sGetAndroidContext(), str, str2);
                } else {
                    renameById = CloudUtils.renameById(GalleryApp.sGetAndroidContext(), j, str2);
                }
                if (renameById <= 0) {
                    throw new IllegalArgumentException("invalid param,id cant be 0");
                }
                return str2;
            }
        });
    }

    @Override // com.miui.gallery.model.datalayer.repository.photo.ICommonCloudModel
    public Flowable<List<Pair<String, Byte[]>>> queryItemPath(final Uri... uriArr) {
        return Flowable.fromCallable(new Callable<List<Pair<String, Byte[]>>>() { // from class: com.miui.gallery.model.datalayer.repository.photo.CommonCloudModelImpl.7
            @Override // java.util.concurrent.Callable
            public List<Pair<String, Byte[]>> call() throws Exception {
                Uri[] uriArr2 = uriArr;
                if (uriArr2 == null || uriArr2.length == 0) {
                    return Collections.emptyList();
                }
                LinkedList linkedList = new LinkedList();
                for (Uri uri : uriArr) {
                    linkedList.add(CloudUtils.queryPhotoPathByUri((Context) CommonCloudModelImpl.this.mContext.get(), uri));
                }
                return linkedList;
            }
        });
    }

    @Override // com.miui.gallery.model.datalayer.repository.photo.ICommonCloudModel
    public Flowable<PageResults<List<Pair<String, Byte[]>>>> queryItemPath(final Long... lArr) {
        return Flowable.concatArrayDelayError((Publisher[]) Arrays.stream(GalleryDataRepositoryConfig$AlbumRepositoryConfig$CloudModel.getDataSources(this.mContext.get())).map(new Function<ICloudDataSource, Flowable<PageResults<List<Pair<String, Byte[]>>>>>() { // from class: com.miui.gallery.model.datalayer.repository.photo.CommonCloudModelImpl.9
            @Override // java.util.function.Function
            public Flowable<PageResults<List<Pair<String, Byte[]>>>> apply(ICloudDataSource iCloudDataSource) {
                return PageResults.wrapperDataToPageResult(iCloudDataSource.getSourceType(), iCloudDataSource.queryItemPath(lArr));
            }
        }).toArray(new IntFunction<Flowable<PageResults<List<Pair<String, Byte[]>>>>[]>() { // from class: com.miui.gallery.model.datalayer.repository.photo.CommonCloudModelImpl.8
            @Override // java.util.function.IntFunction
            public Flowable<PageResults<List<Pair<String, Byte[]>>>>[] apply(int i) {
                return new Flowable[i];
            }
        }));
    }

    public final boolean checkIsFailed(Long[] lArr) {
        return lArr == null || lArr.length <= 0;
    }
}
