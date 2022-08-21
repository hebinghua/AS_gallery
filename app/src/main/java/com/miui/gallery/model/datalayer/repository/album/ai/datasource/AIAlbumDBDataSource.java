package com.miui.gallery.model.datalayer.repository.album.ai.datasource;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.loader.content.Loader;
import com.miui.epoxy.common.CollectionUtils;
import com.miui.gallery.dao.LocationAndTagsAlbumTableServices;
import com.miui.gallery.dao.PeopleAlbumTableServices;
import com.miui.gallery.model.datalayer.repository.album.ai.utils.AIAlbumDataUtils;
import com.miui.gallery.model.datalayer.utils.CursorFlowableOnSubscribe;
import com.miui.gallery.model.datalayer.utils.LoaderFlowableOnSubscribe;
import com.miui.gallery.model.datalayer.utils.OnLoaderContentChange;
import com.miui.gallery.model.dto.AIAlbumCover;
import com.miui.gallery.model.dto.BaseAlbumCover;
import com.miui.gallery.model.dto.CoverList;
import com.miui.gallery.model.dto.FaceAlbumCover;
import com.miui.gallery.model.dto.SuggestionData;
import com.miui.gallery.provider.PeopleFaceSnapshotHelper;
import com.miui.gallery.search.resultpage.DataListSourceResult;
import com.miui.gallery.util.face.PeopleItem;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.functions.Function;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/* loaded from: classes2.dex */
public class AIAlbumDBDataSource implements IAIAlbumDBDataSource {
    public WeakReference<Context> mContext;

    public AIAlbumDBDataSource(Context context) {
        this.mContext = new WeakReference<>(context);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.ai.datasource.IAIAlbumDBDataSource
    public Flowable<CoverList> queryAIAlbumCover(Integer num, Integer num2, Integer num3) {
        AtomicInteger atomicInteger = new AtomicInteger();
        if (num.intValue() != 0) {
            atomicInteger.incrementAndGet();
        }
        if (num3.intValue() != 0) {
            atomicInteger.incrementAndGet();
        }
        if (num2.intValue() != 0) {
            atomicInteger.incrementAndGet();
        }
        if (atomicInteger.get() == 0) {
            return Flowable.error(new IllegalArgumentException("arguments[faceNum locationNum tagsNum sum] can't is 0"));
        }
        Flowable<List<FaceAlbumCover>> flowable = null;
        if (num.intValue() != 0) {
            flowable = internalQueryFaceCovers(num);
        }
        if (num2.intValue() != 0) {
            Flowable<R> map = queryLocationsAlbum(num2).map(new Function<List<SuggestionData>, List<? extends BaseAlbumCover>>() { // from class: com.miui.gallery.model.datalayer.repository.album.ai.datasource.AIAlbumDBDataSource.1
                @Override // io.reactivex.functions.Function
                /* renamed from: apply  reason: avoid collision after fix types in other method */
                public List<? extends BaseAlbumCover> mo2564apply(List<SuggestionData> list) throws Exception {
                    return AIAlbumDBDataSource.this.internalConvertSuggestionDataToAIAlbumCoverDatas(list, "ai_cover_location");
                }
            });
            if (flowable == null) {
                flowable = map.onErrorReturnItem(CollectionUtils.emptyList());
            } else {
                flowable = flowable.mergeWith(map);
            }
        }
        if (num3.intValue() != 0) {
            Flowable<R> map2 = queryTagsAlbum(num3).map(new Function<List<SuggestionData>, List<? extends BaseAlbumCover>>() { // from class: com.miui.gallery.model.datalayer.repository.album.ai.datasource.AIAlbumDBDataSource.2
                @Override // io.reactivex.functions.Function
                /* renamed from: apply  reason: avoid collision after fix types in other method */
                public List<? extends BaseAlbumCover> mo2564apply(List<SuggestionData> list) throws Exception {
                    return AIAlbumDBDataSource.this.internalConvertSuggestionDataToAIAlbumCoverDatas(list, "ai_cover_tag");
                }
            });
            if (flowable == null) {
                flowable = map2.onErrorReturnItem(CollectionUtils.emptyList());
            } else {
                flowable = flowable.mergeWith(map2);
            }
        }
        return flowable.map(new Function<List<? extends BaseAlbumCover>, CoverList>() { // from class: com.miui.gallery.model.datalayer.repository.album.ai.datasource.AIAlbumDBDataSource.3
            @Override // io.reactivex.functions.Function
            /* renamed from: apply  reason: avoid collision after fix types in other method */
            public CoverList mo2564apply(List<? extends BaseAlbumCover> list) throws Exception {
                CoverList coverList = new CoverList();
                if (!list.isEmpty()) {
                    ArrayList arrayList = new ArrayList(list.size());
                    arrayList.addAll(list);
                    coverList.setCovers(arrayList);
                    return coverList;
                }
                coverList.setCovers(list);
                return coverList;
            }
        });
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.ai.datasource.IAIAlbumDBDataSource
    public Flowable<List<PeopleItem>> queryPersons(final int i, final boolean z) {
        return Flowable.create(new LoaderFlowableOnSubscribe<Cursor, List<PeopleItem>>() { // from class: com.miui.gallery.model.datalayer.repository.album.ai.datasource.AIAlbumDBDataSource.4
            @Override // com.miui.gallery.model.datalayer.utils.LoaderFlowableOnSubscribe
            public Loader<Cursor> getLoader() {
                return PeopleAlbumTableServices.getInstance().getQueryPersonsLoader((Context) AIAlbumDBDataSource.this.mContext.get(), i, z);
            }

            @Override // com.miui.gallery.model.datalayer.utils.LoaderFlowableOnSubscribe
            public void subscribe(FlowableEmitter<List<PeopleItem>> flowableEmitter, Cursor cursor) {
                if (cursor == null || cursor.isClosed()) {
                    flowableEmitter.onNext(CollectionUtils.emptyList());
                } else {
                    flowableEmitter.onNext(PeopleFaceSnapshotHelper.cursor2Entities(cursor));
                }
            }
        }, BackpressureStrategy.LATEST);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.ai.datasource.IAIAlbumDBDataSource
    public Flowable<List<PeopleItem>> queryPeopleFaceSnapList(final int i) {
        return Flowable.create(new CursorFlowableOnSubscribe<List<PeopleItem>>() { // from class: com.miui.gallery.model.datalayer.repository.album.ai.datasource.AIAlbumDBDataSource.5
            @Override // com.miui.gallery.model.datalayer.utils.CursorFlowableOnSubscribe
            public Cursor getCursor() {
                return PeopleAlbumTableServices.getInstance().queryPeopleFaceCoversSnapShot((Context) AIAlbumDBDataSource.this.mContext.get(), i);
            }

            @Override // com.miui.gallery.model.datalayer.utils.CursorFlowableOnSubscribe
            public void subscribe(Cursor cursor, FlowableEmitter<List<PeopleItem>> flowableEmitter) {
                if (cursor == null || cursor.isClosed()) {
                    flowableEmitter.onNext(CollectionUtils.emptyList());
                } else {
                    flowableEmitter.onNext(PeopleFaceSnapshotHelper.cursor2Entities(cursor));
                }
            }
        }, BackpressureStrategy.LATEST);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.ai.datasource.IAIAlbumDBDataSource
    public Flowable<List<SuggestionData>> queryTagsAlbum(final Integer num) {
        return Flowable.create(new LoaderFlowableOnSubscribe<Object, List<SuggestionData>>() { // from class: com.miui.gallery.model.datalayer.repository.album.ai.datasource.AIAlbumDBDataSource.6
            @Override // com.miui.gallery.model.datalayer.utils.LoaderFlowableOnSubscribe
            public Loader<Object> getLoader() {
                return LocationAndTagsAlbumTableServices.getInstance().buildTagsLoader((Context) AIAlbumDBDataSource.this.mContext.get(), num.intValue());
            }

            @Override // com.miui.gallery.model.datalayer.utils.LoaderFlowableOnSubscribe
            public void subscribe(FlowableEmitter<List<SuggestionData>> flowableEmitter, Object obj) {
                flowableEmitter.onNext(AIAlbumDBDataSource.this.internalCommandDataListSourceResult(obj));
            }
        }, BackpressureStrategy.LATEST).onErrorReturnItem(AIAlbumDataUtils.getEmptySuggestionDatas());
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.ai.datasource.IAIAlbumDBDataSource
    public Flowable<List<SuggestionData>> queryLocationsAlbum(final Integer num) {
        return Flowable.create(new LoaderFlowableOnSubscribe<Object, List<SuggestionData>>() { // from class: com.miui.gallery.model.datalayer.repository.album.ai.datasource.AIAlbumDBDataSource.7
            @Override // com.miui.gallery.model.datalayer.utils.LoaderFlowableOnSubscribe
            public Loader<Object> getLoader() {
                return LocationAndTagsAlbumTableServices.getInstance().buildLocationsLoader((Context) AIAlbumDBDataSource.this.mContext.get(), num.intValue());
            }

            @Override // com.miui.gallery.model.datalayer.utils.LoaderFlowableOnSubscribe
            public void subscribe(FlowableEmitter<List<SuggestionData>> flowableEmitter, Object obj) {
                flowableEmitter.onNext(AIAlbumDBDataSource.this.internalCommandDataListSourceResult(obj));
            }
        }, BackpressureStrategy.LATEST).onErrorReturnItem(AIAlbumDataUtils.getEmptySuggestionDatas());
    }

    public final List<AIAlbumCover> internalConvertSuggestionDataToAIAlbumCoverDatas(List<SuggestionData> list, String str) {
        if (list == null || list.isEmpty()) {
            return getEmptySuggestionDataListByType(str);
        }
        ArrayList arrayList = new ArrayList(list.size());
        for (SuggestionData suggestionData : list) {
            AIAlbumCover aIAlbumCover = new AIAlbumCover();
            String icon = suggestionData.getIcon();
            aIAlbumCover.coverUri = Uri.parse(icon);
            aIAlbumCover.coverId = LocationAndTagsAlbumTableServices.getInstance().parseAlbumCoverServerId(icon);
            aIAlbumCover.actionUri = suggestionData.getIntentActionURI();
            String title = suggestionData.getTitle();
            aIAlbumCover.albumName = title;
            aIAlbumCover.id = aIAlbumCover.coverId + title.hashCode();
            aIAlbumCover.type = str;
            arrayList.add(aIAlbumCover);
        }
        return arrayList;
    }

    /* JADX WARN: Type inference failed for: r4v3, types: [com.miui.gallery.search.core.suggestion.SuggestionCursor, com.miui.gallery.search.core.suggestion.Suggestion, android.database.Cursor] */
    public final List<SuggestionData> internalCommandDataListSourceResult(Object obj) {
        if (obj instanceof DataListSourceResult) {
            ?? data = ((DataListSourceResult) obj).getData();
            if (data == 0 || !data.moveToFirst()) {
                return AIAlbumDataUtils.getEmptySuggestionDatas();
            }
            ArrayList arrayList = new ArrayList(data.getCount());
            data.moveToFirst();
            while (!data.isAfterLast()) {
                SuggestionData suggestionData = new SuggestionData();
                suggestionData.setIcon(data.getSuggestionIcon());
                suggestionData.setBackupIcons(data.getBackupIcons());
                suggestionData.setExtras(data.getSuggestionExtras());
                suggestionData.setIntentActionURI(data.getIntentActionURI());
                suggestionData.setSubTitle(data.getSuggestionSubTitle());
                suggestionData.setTitle(data.getSuggestionTitle());
                arrayList.add(suggestionData);
                data.moveToNext();
            }
            return arrayList;
        }
        return AIAlbumDataUtils.getEmptySuggestionDatas();
    }

    public final List<? extends BaseAlbumCover> getEmptySuggestionDataListByType(String str) {
        if (TextUtils.equals(str, "ai_cover_location")) {
            return AIAlbumDataUtils.getLocationDataEmptyList();
        }
        if (TextUtils.equals(str, "ai_cover_tag")) {
            return AIAlbumDataUtils.getTagDataEmptyList();
        }
        return CollectionUtils.emptyList();
    }

    public final Flowable<List<FaceAlbumCover>> internalQueryFaceCovers(final Integer num) {
        return Flowable.create(new LoaderFlowableOnSubscribe<Cursor, List<FaceAlbumCover>>() { // from class: com.miui.gallery.model.datalayer.repository.album.ai.datasource.AIAlbumDBDataSource.8
            @Override // com.miui.gallery.model.datalayer.utils.LoaderFlowableOnSubscribe
            public boolean saveNextValue() {
                return true;
            }

            @Override // com.miui.gallery.model.datalayer.utils.LoaderFlowableOnSubscribe
            public Loader<Cursor> getLoader() {
                return PeopleAlbumTableServices.getInstance().getQueryPeopleFaceCoversLoader((Context) AIAlbumDBDataSource.this.mContext.get(), num.intValue(), new OnLoaderContentChange() { // from class: com.miui.gallery.model.datalayer.repository.album.ai.datasource.AIAlbumDBDataSource.8.1
                    /* JADX WARN: Removed duplicated region for block: B:10:0x0024  */
                    @Override // com.miui.gallery.model.datalayer.utils.OnLoaderContentChange
                    /*
                        Code decompiled incorrectly, please refer to instructions dump.
                        To view partially-correct code enable 'Show inconsistent code' option in preferences
                    */
                    public boolean onContentChange() {
                        /*
                            r6 = this;
                            com.miui.gallery.model.datalayer.repository.album.ai.datasource.AIAlbumDBDataSource$8 r0 = com.miui.gallery.model.datalayer.repository.album.ai.datasource.AIAlbumDBDataSource.AnonymousClass8.this
                            java.lang.Object r0 = com.miui.gallery.model.datalayer.repository.album.ai.datasource.AIAlbumDBDataSource.AnonymousClass8.access$300(r0)
                            java.util.List r0 = (java.util.List) r0
                            r1 = 0
                            if (r0 == 0) goto L41
                            boolean r2 = r0.isEmpty()
                            if (r2 == 0) goto L12
                            goto L41
                        L12:
                            java.lang.String r2 = "AIAlbumDbDataSource"
                            java.lang.String r3 = "onContentChange"
                            java.lang.String r2 = com.miui.gallery.util.FileHandleRecordHelper.appendInvokerTag(r2, r3)
                            java.util.Iterator r0 = r0.iterator()
                        L1e:
                            boolean r3 = r0.hasNext()
                            if (r3 == 0) goto L3f
                            java.lang.Object r3 = r0.next()
                            com.miui.gallery.model.dto.FaceAlbumCover r3 = (com.miui.gallery.model.dto.FaceAlbumCover) r3
                            com.miui.gallery.storage.strategies.base.StorageStrategyManager r4 = com.miui.gallery.storage.StorageSolutionProvider.get()
                            java.lang.String r3 = r3.coverPath
                            com.miui.gallery.storage.strategies.IStoragePermissionStrategy$Permission r5 = com.miui.gallery.storage.strategies.IStoragePermissionStrategy.Permission.QUERY
                            androidx.documentfile.provider.DocumentFile r3 = r4.getDocumentFile(r3, r5, r2)
                            if (r3 == 0) goto L3e
                            boolean r3 = r3.exists()
                            if (r3 != 0) goto L1e
                        L3e:
                            return r1
                        L3f:
                            r0 = 1
                            return r0
                        L41:
                            return r1
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.model.datalayer.repository.album.ai.datasource.AIAlbumDBDataSource.AnonymousClass8.AnonymousClass1.onContentChange():boolean");
                    }
                });
            }

            @Override // com.miui.gallery.model.datalayer.utils.LoaderFlowableOnSubscribe
            public void subscribe(FlowableEmitter<List<FaceAlbumCover>> flowableEmitter, Cursor cursor) {
                if (cursor == null || cursor.isClosed()) {
                    flowableEmitter.onNext(AIAlbumDataUtils.getFaceDataEmptyList());
                    return;
                }
                Bundle extras = cursor.getExtras();
                extras.setClassLoader(FaceAlbumCover.class.getClassLoader());
                List<FaceAlbumCover> parcelableArrayList = extras.getParcelableArrayList("face_album_cover");
                if (parcelableArrayList == null) {
                    parcelableArrayList = AIAlbumDataUtils.getFaceDataEmptyList();
                }
                flowableEmitter.onNext(parcelableArrayList);
            }
        }, BackpressureStrategy.LATEST).onErrorReturnItem(AIAlbumDataUtils.getFaceDataEmptyList());
    }
}
