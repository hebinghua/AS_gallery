package com.miui.gallery.ui.album.main.utils.splitgroup.version2;

import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Pair;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.ExtraSourceProvider;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.album.common.CommonAlbumItemViewBean;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.common.viewbean.AlbumTabGroupTitleViewBean;
import com.miui.gallery.ui.album.main.AlbumTabContract$P;
import com.miui.gallery.ui.album.main.AlbumTabContract$V;
import com.miui.gallery.ui.album.main.usecase.DoChangeSortPositionCase;
import com.miui.gallery.ui.album.main.utils.splitgroup.AlbumSplitGroupHelper;
import com.miui.gallery.ui.album.main.viewbean.AlbumDataListResult;
import com.miui.gallery.util.AlbumSortHelper;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.ViewUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.itemdrag.RecyclerViewDragItemManager;
import com.miui.itemdrag.RecyclerViewUtils;
import io.reactivex.subscribers.DisposableSubscriber;
import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/* loaded from: classes2.dex */
public class AlbumTabDragImpl<P extends AlbumTabContract$P> implements RecyclerViewDragItemManager.OnDragCallback {
    public boolean isMoved;
    public int mDragItemStartX;
    public int mDragItemStartY;
    public Boolean mIsEmptyHeadGroup;
    public boolean mIsEmptyMoreGroup;
    public boolean mIsEmptyUserGroup;
    public AlbumTabGroupTitleViewBean mMoreGroupTitleBean;
    public final WeakReference<AlbumTabContract$P> mPresenterRef;
    public AlbumTabGroupTitleViewBean mUserGroupTitleBean;

    public static /* synthetic */ boolean $r8$lambda$OBQnKSbGnL4IiFzltSpB0fw1dmA(AlbumTabDragImpl albumTabDragImpl, Map map, BaseViewBean baseViewBean) {
        return albumTabDragImpl.lambda$checkAndFixSort$0(map, baseViewBean);
    }

    public AlbumTabDragImpl(WeakReference weakReference) {
        this.mPresenterRef = weakReference;
    }

    public AlbumTabContract$V getView() {
        if (this.mPresenterRef.get() != null) {
            return (AlbumTabContract$V) this.mPresenterRef.get().getView();
        }
        return null;
    }

    public AlbumTabContract$P getPresenter() {
        return this.mPresenterRef.get();
    }

    public boolean canEnableSwapItem() {
        return AlbumSortHelper.isCustomSortOrder();
    }

    public final String getGroupTypeBySortInfo(String str) {
        return AlbumSplitModeImpl.internalGetGroupTypeBySortInfo(str);
    }

    public final double getSortPosition(String str) {
        return AlbumSplitGroupHelper.getSortPosition(str);
    }

    public final double getSortPosition(BaseViewBean baseViewBean) {
        return AlbumSplitGroupHelper.getSortPosition(baseViewBean);
    }

    public final String getGroupType(Album album) {
        return AlbumSplitModeImpl.internalGetGroupType(album);
    }

    public final Album getAlbumSource(BaseViewBean baseViewBean) {
        return AlbumSplitGroupHelper.getAlbumSource(baseViewBean);
    }

    @Override // com.miui.itemdrag.RecyclerViewDragItemManager.OnDragCallback
    public boolean canStartDrag(RecyclerView.ViewHolder viewHolder, int i, int i2) {
        if (getView() == null) {
            return false;
        }
        return getView().canDrag(viewHolder, i, i2);
    }

    @Override // com.miui.itemdrag.RecyclerViewDragItemManager.OnDragCallback
    public boolean onMoveItem(RecyclerView recyclerView, int i, int i2) {
        if (getView() == null) {
            return false;
        }
        getView().moveData(i, i2, false);
        LinearMotorHelper.performHapticFeedback(recyclerView, LinearMotorHelper.HAPTIC_MESH_NORMAL);
        return false;
    }

    @Override // com.miui.itemdrag.RecyclerViewDragItemManager.OnDragCallback
    public void onTouchMoveWhenStartDrag(int i, int i2) {
        if (getView() != null && !this.isMoved) {
            int touchSlop = getView().getTouchSlop();
            boolean isMove = ViewUtils.isMove(this.mDragItemStartX, i, touchSlop);
            boolean isMove2 = ViewUtils.isMove(this.mDragItemStartY, i2, touchSlop);
            if (!isMove && !isMove2) {
                return;
            }
            this.isMoved = true;
            getView().onFirstMoveWhenDragItem();
            SamplingStatHelper.recordCountEvent("album", "start_drag_album", null);
        }
    }

    @Override // com.miui.itemdrag.RecyclerViewDragItemManager.OnDragCallback
    public void onDragItemStarted(RecyclerView.ViewHolder viewHolder, int i, int i2) {
        if (getView() == null) {
            return;
        }
        if (LinearMotorHelper.LINEAR_MOTOR_SUPPORTED.get(null).booleanValue()) {
            LinearMotorHelper.performHapticFeedback(getView().getRecyclerView(), LinearMotorHelper.HAPTIC_PICK_UP);
        } else {
            getView().getView().performHapticFeedback(0);
        }
        this.mDragItemStartX = i;
        this.mDragItemStartY = i2;
        getView().changeDragStatus(false, this.isMoved);
        displayEmptyGroupHead();
    }

    public final void displayEmptyGroupHead() {
        AlbumDataListResult albumDataResult = getPresenter().getAlbumDataResult();
        this.mIsEmptyUserGroup = !albumDataResult.isHaveGroupDatas("group_user");
        this.mIsEmptyMoreGroup = !albumDataResult.isHaveGroupDatas("group_third");
        if (this.mIsEmptyUserGroup) {
            AlbumTabGroupTitleViewBean userGroupTitleBean = getUserGroupTitleBean();
            userGroupTitleBean.setState(3);
            getView().updateDataById(userGroupTitleBean.getId(), userGroupTitleBean);
        }
        if (this.mIsEmptyMoreGroup) {
            AlbumTabGroupTitleViewBean moreGroupTitleBean = getMoreGroupTitleBean();
            moreGroupTitleBean.setState(3);
            getView().updateDataById(moreGroupTitleBean.getId(), moreGroupTitleBean);
        }
    }

    @Override // com.miui.itemdrag.RecyclerViewDragItemManager.OnDragCallback
    public boolean onBeforeDragItemStart(RecyclerView.ViewHolder viewHolder) {
        if (getView() == null) {
            return false;
        }
        getView().onBeforeDragItemStart();
        return getView().isInChoiceMode();
    }

    @Override // com.miui.itemdrag.RecyclerViewDragItemManager.OnDragCallback
    public RecyclerView.ViewHolder findSwapTargetItem(RecyclerView recyclerView, int i, int i2) {
        if (getView() == null) {
            return null;
        }
        RecyclerView.ViewHolder findSwapTargetItem = super.findSwapTargetItem(recyclerView, i, i2);
        if (!canStartDrag(findSwapTargetItem, 0, 0)) {
            return null;
        }
        return findSwapTargetItem;
    }

    @Override // com.miui.itemdrag.RecyclerViewDragItemManager.OnDragCallback
    public int onBeforeDragItemEnd(RecyclerView recyclerView, int i, int i2, int i3) {
        Rect rect;
        if (getView() != null && canEnableSwapItem() && super.findSwapTargetItem(recyclerView, i, i2) == null) {
            RecyclerView.ViewHolder findViewHolderForItemId = recyclerView.findViewHolderForItemId(2131362624L);
            RecyclerView.ViewHolder findViewHolderForItemId2 = recyclerView.findViewHolderForItemId(2131362621L);
            if (isEmptyHeadGroupDatas() && findViewHolderForItemId != null && i2 < findViewHolderForItemId.itemView.getTop()) {
                if (i3 == 0) {
                    return -1;
                }
                getView().onFillItemWhenEmptyHeadGroup(i3, 0);
                return 0;
            } else if (this.mIsEmptyUserGroup && findViewHolderForItemId != null && findViewHolderForItemId2 != null && i2 > findViewHolderForItemId.itemView.getTop() && i2 < findViewHolderForItemId2.itemView.getBottom()) {
                int childAdapterPosition = recyclerView.getChildAdapterPosition(findViewHolderForItemId.itemView);
                if (i3 > childAdapterPosition) {
                    childAdapterPosition++;
                }
                if (i3 == childAdapterPosition) {
                    return -1;
                }
                getView().moveData(i3, childAdapterPosition);
                return childAdapterPosition;
            } else if (this.mIsEmptyMoreGroup && findViewHolderForItemId2 != null && i2 > findViewHolderForItemId2.itemView.getTop()) {
                int childAdapterPosition2 = recyclerView.getChildAdapterPosition(findViewHolderForItemId2.itemView);
                if (i3 == childAdapterPosition2) {
                    return -1;
                }
                getView().moveData(i3, childAdapterPosition2);
                return childAdapterPosition2;
            } else {
                Pair<Integer, Rect> findAdjacentItemByPoint = getView().findAdjacentItemByPoint(recyclerView, i, i2);
                if (findAdjacentItemByPoint == null || i3 == RecyclerViewUtils.getChildAdapterPotision(recyclerView, ((Integer) findAdjacentItemByPoint.first).intValue()) || (rect = (Rect) findAdjacentItemByPoint.second) == null || !rect.contains(i, i2)) {
                    return -1;
                }
                int min = Math.min(RecyclerViewUtils.getChildAdapterPotision(recyclerView, ((Integer) findAdjacentItemByPoint.first).intValue()), recyclerView.getAdapter().getItemCount() - 1);
                if (i3 > min) {
                    min++;
                }
                int i4 = min;
                getView().moveData(i3, i4);
                return i4;
            }
        }
        return -1;
    }

    @Override // com.miui.itemdrag.RecyclerViewDragItemManager.OnDragCallback
    public void onDragItemEnd(int i, int i2) {
        if (getView() == null) {
            return;
        }
        if (canEnableSwapItem()) {
            onMoveItem(i, i2);
        } else {
            getView().changeDragStatus(true, this.isMoved);
            if (this.isMoved) {
                getView().onMoveAlbumFailed(1);
            }
            getPresenter().doChangeDataPendingStatus(false);
        }
        this.isMoved = false;
        updateGroupHeadAfterDrag();
    }

    public final void updateGroupHeadAfterDrag() {
        AlbumTabGroupTitleViewBean userGroupTitleBean = getUserGroupTitleBean();
        int i = 1;
        userGroupTitleBean.setState(this.mIsEmptyUserGroup ? 1 : 2);
        getView().updateDataById(userGroupTitleBean.getId(), userGroupTitleBean);
        AlbumTabGroupTitleViewBean moreGroupTitleBean = getMoreGroupTitleBean();
        if (!this.mIsEmptyMoreGroup) {
            i = 2;
        }
        moreGroupTitleBean.setState(i);
        getView().updateDataById(moreGroupTitleBean.getId(), moreGroupTitleBean);
    }

    public final boolean isGroupHeaderBean(int i) {
        return isGroupHeaderBean(getView().getData(i));
    }

    public final boolean isGroupHeaderBean(BaseViewBean baseViewBean) {
        return baseViewBean instanceof AlbumTabGroupTitleViewBean;
    }

    public final String getSortInfo(BaseViewBean baseViewBean) {
        Object source = baseViewBean.getSource();
        return source instanceof Album ? ((Album) source).getAlbumSortInfo() : GalleryPreferences.Album.getFixedAlbumSortInfo(baseViewBean.getId());
    }

    public final boolean isEqGroup(BaseViewBean baseViewBean, BaseViewBean baseViewBean2) {
        return TextUtils.equals(getGroupType(getAlbumSource(baseViewBean)), getGroupType(getAlbumSource(baseViewBean2)));
    }

    public final Pair<String, Double> getAndSplitSortInfo(BaseViewBean baseViewBean) {
        Album albumSource = getAlbumSource(baseViewBean);
        if (albumSource == null) {
            return null;
        }
        return Pair.create(getGroupType(albumSource), Double.valueOf(getSortPosition(albumSource.getAlbumSortInfo())));
    }

    public final void onMoveItem(int i, int i2) {
        double randomPrevSortPosition;
        String str;
        double randomNextSortPosition;
        if (i < 0 || i2 < 0 || i == i2 || i2 > getView().getDataSize()) {
            getView().changeDragStatus(true, this.isMoved);
            getPresenter().doChangeDataPendingStatus(false);
            return;
        }
        BaseViewBean data = getView().getData(i);
        BaseViewBean data2 = getView().getData(i2);
        boolean z = Math.abs(i - i2) == 1 && !isGroupHeaderBean(i) && isEqGroup(data, data2);
        boolean isFixedAlbum = GalleryPreferences.Album.isFixedAlbum(data2.getId());
        boolean isContainsCloudControlSetSortAlbum = isContainsCloudControlSetSortAlbum(data);
        boolean isContainsCloudControlSetSortAlbum2 = isContainsCloudControlSetSortAlbum(data2);
        if (isContainsCloudControlSetSortAlbum || isContainsCloudControlSetSortAlbum2) {
            checkAndFixSort();
            data = getView().getData(i);
            data2 = getView().getData(i2);
        }
        if (z) {
            String groupType = getGroupType(getAlbumSource(data));
            moveDataSort(new BaseViewBean[]{data, data2}, new String[]{AlbumSplitGroupHelper.packSortInfo(getSortPosition(data2), groupType), AlbumSplitGroupHelper.packSortInfo(getSortPosition(data), groupType)});
            return;
        }
        String str2 = null;
        BaseViewBean data3 = i2 > 0 ? getView().getData(i2 - 1) : null;
        boolean isGroupHeaderBean = data3 != null ? isGroupHeaderBean(data3) : false;
        int i3 = i2 + 1;
        if (i3 < getView().getDataSize()) {
            BaseViewBean data4 = getView().getData(i3);
            randomPrevSortPosition = SearchStatUtils.POW;
            if (data3 == null && i2 == 0) {
                if (data4 != null && isGroupHeaderBean(data4)) {
                    randomNextSortPosition = GalleryPreferences.Album.getNextHeadGroupFirstAlbumSort();
                } else {
                    randomNextSortPosition = AlbumSortHelper.randomNextSortPosition(getSortPosition(getSortInfo(data4)));
                    GalleryPreferences.Album.setNextHeadGroupFirstAlbumSort(randomNextSortPosition);
                }
                randomPrevSortPosition = randomNextSortPosition;
                str = "group_head";
            } else {
                Pair<String, Double> andSplitSortInfo = getAndSplitSortInfo(data3);
                if (andSplitSortInfo != null) {
                    str2 = (String) andSplitSortInfo.first;
                }
                double doubleValue = andSplitSortInfo == null ? 0.0d : ((Double) andSplitSortInfo.second).doubleValue();
                if (doubleValue == SearchStatUtils.POW && isGroupHeaderBean) {
                    if (isGroupHeaderBean(data4)) {
                        if (data4.getId() == 2131362621) {
                            this.mIsEmptyUserGroup = false;
                            str = "group_user";
                        } else {
                            this.mIsEmptyMoreGroup = false;
                            str = "group_third";
                        }
                    } else {
                        Pair<String, Double> andSplitSortInfo2 = getAndSplitSortInfo(data4);
                        if (isContainsCloudControlSetSortAlbum(data4)) {
                            checkAndFixSort();
                            andSplitSortInfo2 = getAndSplitSortInfo(data4);
                        }
                        randomPrevSortPosition = AlbumSortHelper.randomNextSortPosition(((Double) andSplitSortInfo2.second).doubleValue());
                        str = (String) andSplitSortInfo2.first;
                    }
                } else {
                    if (isGroupHeaderBean(data4)) {
                        randomPrevSortPosition = AlbumSortHelper.randomPrevSortPosition(doubleValue);
                    } else {
                        randomPrevSortPosition = AlbumSortHelper.randomSortPosition(getSortPosition(getSortInfo(data4)), doubleValue);
                    }
                    str = str2;
                }
            }
        } else if (isGroupHeaderBean) {
            DefaultLogger.e("TabDelegateVersion2", "error,position is invalid");
            getView().onMoveAlbumFailed(2);
            getView().changeDragStatus(true, this.isMoved);
            getPresenter().doChangeDataPendingStatus(false);
            return;
        } else {
            Pair<String, Double> andSplitSortInfo3 = getAndSplitSortInfo(data3);
            randomPrevSortPosition = AlbumSortHelper.randomPrevSortPosition(((Double) andSplitSortInfo3.second).doubleValue());
            str = (String) andSplitSortInfo3.first;
        }
        if (randomPrevSortPosition == -1.0d) {
            getView().onMoveAlbumFailed(2);
            getPresenter().doChangeDataPendingStatus(false);
        } else if (isFixedAlbum) {
            onFinishSwapData(new BaseViewBean[]{data2}, new String[]{AlbumSplitGroupHelper.packSortInfo(randomPrevSortPosition, str)});
        } else {
            moveDataSort(new BaseViewBean[]{data2}, new String[]{AlbumSplitGroupHelper.packSortInfo(randomPrevSortPosition, str)});
            TrackController.trackDrag("403.7.2.1.10331");
        }
    }

    public final void checkAndFixSort() {
        boolean z;
        String calculateSortPositionByNormalAlbum;
        List<T> groupDatas = getPresenter().getGroupDatas("group_third");
        Iterator it = groupDatas.iterator();
        BaseViewBean baseViewBean = null;
        while (true) {
            if (!it.hasNext()) {
                z = false;
                break;
            }
            BaseViewBean baseViewBean2 = (BaseViewBean) it.next();
            if (baseViewBean == null) {
                baseViewBean = baseViewBean2;
            } else if (getSortPosition(baseViewBean) <= getSortPosition(baseViewBean2)) {
                z = true;
                break;
            }
        }
        if (z) {
            DefaultLogger.fd("TabDelegateVersion2", "check success,now reGenerate preset Albums sortValue");
            final Map<String, Integer> sorts = CloudControlStrategyHelper.getThirdAlbumSorts().getSorts();
            List list = (List) groupDatas.stream().filter(new Predicate() { // from class: com.miui.gallery.ui.album.main.utils.splitgroup.version2.AlbumTabDragImpl$$ExternalSyntheticLambda0
                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return AlbumTabDragImpl.$r8$lambda$OBQnKSbGnL4IiFzltSpB0fw1dmA(AlbumTabDragImpl.this, sorts, (BaseViewBean) obj);
                }
            }).collect(Collectors.toList());
            BaseViewBean baseViewBean3 = (BaseViewBean) groupDatas.get(0);
            if (!isContainsCloudControlSetSortAlbum(baseViewBean3)) {
                calculateSortPositionByNormalAlbum = String.valueOf(getSortPosition(baseViewBean3));
            } else {
                calculateSortPositionByNormalAlbum = AlbumSortHelper.calculateSortPositionByNormalAlbum(System.currentTimeMillis());
            }
            BigDecimal bigDecimal = new BigDecimal(10);
            BigDecimal bigDecimal2 = new BigDecimal(calculateSortPositionByNormalAlbum);
            BaseViewBean[] baseViewBeanArr = new BaseViewBean[list.size()];
            String[] strArr = new String[list.size()];
            for (int size = list.size() - 1; size >= 0; size--) {
                bigDecimal2 = bigDecimal2.add(bigDecimal);
                baseViewBeanArr[size] = (BaseViewBean) list.get(size);
                strArr[size] = AlbumSplitGroupHelper.packSortInfo(bigDecimal2.doubleValue(), "group_third");
            }
            updateMemoryAlbumSortValue(baseViewBeanArr, strArr);
            moveDataSort(baseViewBeanArr, strArr);
        }
    }

    public /* synthetic */ boolean lambda$checkAndFixSort$0(Map map, BaseViewBean baseViewBean) {
        return map.containsKey(getAlbumSource(baseViewBean).getLocalPath());
    }

    public final boolean isContainsCloudControlSetSortAlbum(BaseViewBean baseViewBean) {
        Album albumSource = getAlbumSource(baseViewBean);
        if (albumSource != null && !albumSource.isManualMovePosition() && getGroupType(albumSource).equals("group_third")) {
            return CloudControlStrategyHelper.getThirdAlbumSorts().getSorts().containsKey(albumSource.getLocalPath());
        }
        return false;
    }

    public final void moveDataSort(final BaseViewBean[] baseViewBeanArr, final String[] strArr, final boolean z) {
        int length = baseViewBeanArr.length;
        if (length != strArr.length) {
            return;
        }
        long[] jArr = new long[length];
        boolean z2 = true;
        for (int i = 0; i < length; i++) {
            jArr[i] = baseViewBeanArr[i].getId();
            if (!GalleryPreferences.Album.isFixedAlbum(jArr[i])) {
                z2 = false;
            }
        }
        if (z2) {
            onFinishSwapData(baseViewBeanArr, strArr);
            return;
        }
        if (baseViewBeanArr.length == 2) {
            DefaultLogger.fd("TabDelegateVersion2", "onMoveItem,from:[%s ---> sortPosition:%s],to:[%s ---> sortPosition:%s ]", baseViewBeanArr[0].toString(), strArr[0], baseViewBeanArr[1].toString(), strArr[1]);
        }
        getPresenter().doChangeSortPosition(new DoChangeSortPositionCase.Param(jArr, strArr), new DisposableSubscriber<Boolean>() { // from class: com.miui.gallery.ui.album.main.utils.splitgroup.version2.AlbumTabDragImpl.1
            @Override // org.reactivestreams.Subscriber
            public void onComplete() {
            }

            @Override // org.reactivestreams.Subscriber
            public void onError(Throwable th) {
            }

            {
                AlbumTabDragImpl.this = this;
            }

            @Override // org.reactivestreams.Subscriber
            public void onNext(Boolean bool) {
                AlbumTabDragImpl.this.onFinishSwapData(baseViewBeanArr, strArr, z);
            }
        });
    }

    public final void moveDataSort(BaseViewBean[] baseViewBeanArr, String[] strArr) {
        moveDataSort(baseViewBeanArr, strArr, true);
    }

    public final void onFinishSwapData(BaseViewBean[] baseViewBeanArr, String[] strArr, boolean z) {
        if (z) {
            updateMemoryAlbumSortValue(baseViewBeanArr, strArr);
        }
        getView().changeDragStatus(true, this.isMoved);
        getPresenter().doChangeDataPendingStatus(false);
        checkHeadGroupStatus();
    }

    public final void onFinishSwapData(BaseViewBean[] baseViewBeanArr, String[] strArr) {
        onFinishSwapData(baseViewBeanArr, strArr, true);
    }

    public final void updateMemoryAlbumSortValue(BaseViewBean[] baseViewBeanArr, String[] strArr) {
        int length = baseViewBeanArr.length;
        for (int i = 0; i < length; i++) {
            updateMemoryAlbumSortValue(baseViewBeanArr[i], strArr[i]);
        }
    }

    public final void updateMemoryAlbumSortValue(BaseViewBean baseViewBean, String str) {
        String groupType = getGroupType(getAlbumSource(baseViewBean));
        String groupTypeBySortInfo = getGroupTypeBySortInfo(String.valueOf(str));
        if (baseViewBean instanceof CommonAlbumItemViewBean) {
            CommonAlbumItemViewBean commonAlbumItemViewBean = (CommonAlbumItemViewBean) baseViewBean;
            ((Album) commonAlbumItemViewBean.getSource()).setSortInfo(str);
            DefaultLogger.d("TabDelegateVersion2", "要交换的Bean:%s->newSort:%s", commonAlbumItemViewBean.getTitle(), str);
        } else {
            updateMemoryFixedAlbumSortPosition(baseViewBean.getId(), str);
            DefaultLogger.d("TabDelegateVersion2", "要交换的Bean:%s->newSort:%s", getView().getFixedAlbumNameById(baseViewBean.getId()), str);
        }
        if (getPresenter().getAlbumDataResult() != null) {
            AlbumDataListResult albumDataResult = getPresenter().getAlbumDataResult();
            albumDataResult.removeDataById(groupType, baseViewBean, false);
            albumDataResult.addGroupItem(groupTypeBySortInfo, -1, baseViewBean, false);
            albumDataResult.sortBy(groupTypeBySortInfo, AlbumSortHelper.getViewBeanComparator(), false);
            if (albumDataResult.isHaveGroupDatas(groupType)) {
                return;
            }
            if (groupType.equals("group_user")) {
                this.mIsEmptyUserGroup = true;
            } else if (groupType.equals("group_third")) {
                this.mIsEmptyMoreGroup = true;
            }
            Pair<Integer, BaseViewBean> groupGapDecorator = albumDataResult.getGroupGapDecorator(groupType);
            if (groupGapDecorator == null) {
                return;
            }
            BaseViewBean baseViewBean2 = (BaseViewBean) groupGapDecorator.second;
            ((AlbumTabGroupTitleViewBean) baseViewBean2).setState(1);
            getView().updateDataById(baseViewBean2.getId(), baseViewBean2);
        }
    }

    public final void updateMemoryFixedAlbumSortPosition(long j, String str) {
        BaseViewBean otherAlbumBean;
        if (j == 2147483639) {
            otherAlbumBean = getPresenter().getAIAlbumBean();
        } else {
            otherAlbumBean = j == 2147483641 ? getPresenter().getOtherAlbumBean() : null;
        }
        if (otherAlbumBean instanceof ExtraSourceProvider) {
            ((Album) ((ExtraSourceProvider) otherAlbumBean).mo1601provider()).setSortInfo(str);
        }
        GalleryPreferences.Album.setFixedAlbumSortInfo(j, str);
    }

    public final void checkHeadGroupStatus() {
        boolean isEmptyHeadGroupDatas = isEmptyHeadGroupDatas();
        Boolean bool = this.mIsEmptyHeadGroup;
        if (bool == null || isEmptyHeadGroupDatas != bool.booleanValue()) {
            this.mIsEmptyHeadGroup = Boolean.valueOf(isEmptyHeadGroupDatas);
            getView().onChangeHeadGroupEmptyStatus(this.mIsEmptyHeadGroup.booleanValue());
        }
    }

    public final boolean isEmptyHeadGroupDatas() {
        BaseViewBean data = getView().getData(0);
        if (!(data instanceof BaseViewBean)) {
            return false;
        }
        return isGroupHeaderBean(data);
    }

    public final AlbumTabGroupTitleViewBean getUserGroupTitleBean() {
        if (this.mUserGroupTitleBean == null) {
            this.mUserGroupTitleBean = (AlbumTabGroupTitleViewBean) getView().getOrGenerateTitleBean(2131362624L, R.string.group_header_title_user_create_album);
        }
        return this.mUserGroupTitleBean;
    }

    public final AlbumTabGroupTitleViewBean getMoreGroupTitleBean() {
        if (this.mMoreGroupTitleBean == null) {
            this.mMoreGroupTitleBean = (AlbumTabGroupTitleViewBean) getView().getOrGenerateTitleBean(2131362621L, R.string.more_album_tip);
        }
        return this.mMoreGroupTitleBean;
    }
}
