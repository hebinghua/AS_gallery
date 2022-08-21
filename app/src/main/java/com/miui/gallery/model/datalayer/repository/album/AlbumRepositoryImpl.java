package com.miui.gallery.model.datalayer.repository.album;

import android.content.Context;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.model.datalayer.repository.AbstractAlbumRepository;
import com.miui.gallery.model.datalayer.repository.album.ai.AIAlbumModelImpl;
import com.miui.gallery.model.datalayer.repository.album.ai.IAIAlbumModel;
import com.miui.gallery.model.datalayer.repository.album.cloud.CloudAlbumModelImpl;
import com.miui.gallery.model.datalayer.repository.album.cloud.ICloudAlbumModel;
import com.miui.gallery.model.datalayer.repository.album.common.CommonAlbumModelImpl;
import com.miui.gallery.model.datalayer.repository.album.common.ICommonAlbumModel;
import com.miui.gallery.model.datalayer.repository.album.common.QueryParam;
import com.miui.gallery.model.datalayer.repository.album.hidden.HiddenAlbumModelImpl;
import com.miui.gallery.model.datalayer.repository.album.hidden.IHiddenAlbumModel;
import com.miui.gallery.model.datalayer.repository.album.other.IOtherAlbumModel;
import com.miui.gallery.model.datalayer.repository.album.other.OtherAlbumModelImpl;
import com.miui.gallery.model.datalayer.repository.album.rubbish.IRubbishAlbumModel;
import com.miui.gallery.model.datalayer.repository.album.rubbish.RubbishAlbumModelImpl;
import com.miui.gallery.model.datalayer.repository.album.share.IShareAlbumModel;
import com.miui.gallery.model.datalayer.repository.album.share.ShareAlbumModelImpl;
import com.miui.gallery.model.datalayer.repository.album.trash.ITrashAlbumModel;
import com.miui.gallery.model.datalayer.repository.album.trash.TrashAlbumModelImpl;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.CoverList;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.model.dto.ShareAlbum;
import com.miui.gallery.model.dto.SuggestionData;
import com.miui.gallery.provider.cloudmanager.method.album.DoReplaceAlbumCoverMethod;
import com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumManualHideResult;
import com.miui.gallery.util.face.PeopleItem;
import io.reactivex.Flowable;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class AlbumRepositoryImpl extends AbstractAlbumRepository {
    public final IAIAlbumModel mAIAlbumDataModel;
    public final ICloudAlbumModel mCloudAlbumDataModel;
    public final ICommonAlbumModel mCommonAlbumModel;
    public final IHiddenAlbumModel mHiddenAlbumDataModel;
    public final IOtherAlbumModel mOtherAlbumDataModel;
    public final IRubbishAlbumModel mRubbishAlbumDataModel;
    public final IShareAlbumModel mShareAlbumDataModel;
    public final ITrashAlbumModel mTrashAlbumModel;

    public AlbumRepositoryImpl() {
        this(GalleryApp.sGetAndroidContext());
    }

    public AlbumRepositoryImpl(Context context) {
        this.mAIAlbumDataModel = new AIAlbumModelImpl(context);
        this.mCloudAlbumDataModel = new CloudAlbumModelImpl(context);
        this.mHiddenAlbumDataModel = new HiddenAlbumModelImpl(context);
        this.mOtherAlbumDataModel = new OtherAlbumModelImpl(context);
        this.mRubbishAlbumDataModel = new RubbishAlbumModelImpl(context);
        this.mShareAlbumDataModel = new ShareAlbumModelImpl(context);
        this.mCommonAlbumModel = new CommonAlbumModelImpl(context);
        this.mTrashAlbumModel = new TrashAlbumModelImpl(context);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.share.IShareAlbumModel
    public Flowable<List<ShareAlbum>> queryAlbumListShareInfo() {
        return this.mShareAlbumDataModel.queryAlbumListShareInfo();
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.common.ICommonAlbumModel
    public Flowable<Boolean> doChangeAlbumShowInPhotoTabPage(boolean z, long j) {
        return this.mCommonAlbumModel.doChangeAlbumShowInPhotoTabPage(z, j);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.common.ICommonAlbumModel
    public Flowable<PageResults<List<Album>>> queryAlbums(long j, QueryParam queryParam) {
        return this.mCommonAlbumModel.queryAlbums(j, queryParam);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.common.ICommonAlbumModel
    public Flowable<PageResults<String>> queryAlbumName(long j) {
        return this.mCommonAlbumModel.queryAlbumName(j);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.common.ICommonAlbumModel
    public Flowable<Boolean> doChangeAlbumSortPosition(long[] jArr, String[] strArr) {
        return this.mCommonAlbumModel.doChangeAlbumSortPosition(jArr, strArr);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.common.ICommonAlbumModel
    public Flowable<ArrayList<DoReplaceAlbumCoverMethod.DoReplaceAlbumCoverResult>> doReplaceAlbumCover(long j, long[] jArr) {
        return this.mCommonAlbumModel.doReplaceAlbumCover(j, jArr);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.cloud.ICloudAlbumModel
    public Flowable<PageResults<List<Album>>> queryCloudAlbums() {
        return this.mCloudAlbumDataModel.queryCloudAlbums();
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.cloud.ICloudAlbumModel
    public Flowable<Boolean> doChangeAlbumBackupStatus(boolean z, long j) {
        return this.mCloudAlbumDataModel.doChangeAlbumBackupStatus(z, j);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.hidden.IHiddenAlbumModel
    public Flowable<PageResults<List<Album>>> queryHiddenAlbum() {
        return this.mHiddenAlbumDataModel.queryHiddenAlbum();
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.hidden.IHiddenAlbumModel
    public Flowable<Boolean> doChangeAlbumHiddenStatus(boolean z, long[] jArr) {
        return this.mHiddenAlbumDataModel.doChangeAlbumHiddenStatus(z, jArr);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.hidden.IHiddenAlbumModel
    public Flowable<Boolean> cancelAlbumHiddenStatus(long j) {
        return this.mHiddenAlbumDataModel.cancelAlbumHiddenStatus(j);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.other.IOtherAlbumModel
    public Flowable<PageResults<List<Album>>> queryOthersAlbum(Integer num) {
        return this.mOtherAlbumDataModel.queryOthersAlbum(num);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.other.IOtherAlbumModel
    public Flowable<PageResults<CoverList>> queryOtherAlbumCovers() {
        return this.mOtherAlbumDataModel.queryOtherAlbumCovers();
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.other.IOtherAlbumModel
    public Flowable<Boolean> doChangeAlbumShowInOtherAlbumPage(boolean z, long[] jArr) {
        return this.mOtherAlbumDataModel.doChangeAlbumShowInOtherAlbumPage(z, jArr);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.rubbish.IRubbishAlbumModel
    public Flowable<PageResults<List<Album>>> queryRubbishAlbum(Integer num) {
        return this.mRubbishAlbumDataModel.queryRubbishAlbum(num);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.rubbish.IRubbishAlbumModel
    public Flowable<PageResults<CoverList>> queryRubbishAlbumsAllPhoto(Integer num) {
        return this.mRubbishAlbumDataModel.queryRubbishAlbumsAllPhoto(num);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.rubbish.IRubbishAlbumModel
    public Flowable<Boolean> doChangeAlbumShowInRubbishPage(boolean z, long[] jArr) {
        return this.mRubbishAlbumDataModel.doChangeAlbumShowInRubbishPage(z, jArr);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.rubbish.IRubbishAlbumModel
    public Flowable<RubbishAlbumManualHideResult> doAddNoMediaForRubbishAlbum(List<String> list) {
        return this.mRubbishAlbumDataModel.doAddNoMediaForRubbishAlbum(list);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.rubbish.IRubbishAlbumModel
    public Flowable<RubbishAlbumManualHideResult> doRemoveNoMediaForRubbishAlbum(List<String> list) {
        return this.mRubbishAlbumDataModel.doRemoveNoMediaForRubbishAlbum(list);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.ai.IAIAlbumModel
    public Flowable<List<PeopleItem>> queryPersons(int i, boolean z) {
        return this.mAIAlbumDataModel.queryPersons(i, z);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.ai.IAIAlbumModel
    public Flowable<List<SuggestionData>> queryTagsAlbum(Integer num) {
        return this.mAIAlbumDataModel.queryTagsAlbum(num);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.ai.IAIAlbumModel
    public Flowable<List<SuggestionData>> queryLocationsAlbum(Integer num) {
        return this.mAIAlbumDataModel.queryLocationsAlbum(num);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.ai.IAIAlbumModel
    public Flowable<PageResults<CoverList>> queryAIAlbumCover(Integer num, Integer num2, Integer num3) {
        return this.mAIAlbumDataModel.queryAIAlbumCover(num, num2, num3);
    }

    @Override // com.miui.gallery.model.datalayer.repository.album.trash.ITrashAlbumModel
    public Flowable<Integer> queryTrashAlbumCount() {
        return this.mTrashAlbumModel.queryTrashAlbumCount();
    }
}
