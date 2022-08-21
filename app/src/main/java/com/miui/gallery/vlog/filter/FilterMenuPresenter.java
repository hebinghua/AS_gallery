package com.miui.gallery.vlog.filter;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.base.BasePresenter;
import com.miui.gallery.vlog.base.interfaces.IBaseModel;
import com.miui.gallery.vlog.entity.FilterData;
import com.miui.gallery.vlog.filter.FilterMenuModel;
import com.miui.gallery.vlog.home.VlogConfig;
import com.miui.gallery.vlog.home.VlogModel;
import com.miui.gallery.vlog.sdk.interfaces.IFilterManager;
import com.miui.gallery.vlog.tools.VlogUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class FilterMenuPresenter extends BasePresenter {
    public FilterData mCurrentFilterData;
    public IFilterManager mFilterManager;
    public FilterMenuContract$IFilterMenuView mIFilterMenuView;
    public boolean mVaryingMasterStatus;
    public VlogModel mVlogModel;

    public static /* synthetic */ void $r8$lambda$0rl0F1qBgclYzzX2yy2vcaizpBQ(FilterMenuPresenter filterMenuPresenter, Object obj) {
        filterMenuPresenter.lambda$updateDataList$1(obj);
    }

    /* renamed from: $r8$lambda$wkm7btZGhyrrZoCPUMK2-ww9Vo8 */
    public static /* synthetic */ void m1793$r8$lambda$wkm7btZGhyrrZoCPUMK2ww9Vo8(FilterMenuPresenter filterMenuPresenter, List list, ObservableEmitter observableEmitter) {
        filterMenuPresenter.lambda$updateDataList$0(list, observableEmitter);
    }

    public FilterMenuPresenter(Context context, FilterMenuContract$IFilterMenuView filterMenuContract$IFilterMenuView, FilterZipFileConfig filterZipFileConfig) {
        super(context);
        this.mIFilterMenuView = filterMenuContract$IFilterMenuView;
        this.mIBaseModel = new FilterMenuModel(new FilterMenuModel.Callback() { // from class: com.miui.gallery.vlog.filter.FilterMenuPresenter.1
            {
                FilterMenuPresenter.this = this;
            }

            @Override // com.miui.gallery.vlog.filter.FilterMenuModel.Callback
            public void loadDataSuccess(ArrayList<FilterData> arrayList) {
                Collections.sort(arrayList);
                FilterMenuPresenter.this.refreshData(arrayList);
                FilterMenuPresenter.this.refreshLutStrength(arrayList);
                FilterMenuPresenter.this.mIFilterMenuView.loadRecyclerView(arrayList);
                FilterMenuPresenter.this.updateApplyViewPlayState();
            }

            @Override // com.miui.gallery.vlog.filter.FilterMenuModel.Callback
            public void loadDataFail() {
                ToastUtils.makeText(FilterMenuPresenter.this.mContext, FilterMenuPresenter.this.mContext.getString(R$string.vlog_download_failed_for_notwork));
                ArrayList<FilterData> arrayList = new ArrayList<>();
                arrayList.add(FilterData.getDefaultItem());
                FilterMenuPresenter.this.mIFilterMenuView.loadRecyclerView(arrayList);
                FilterMenuPresenter.this.updateApplyViewPlayState();
            }
        });
        VlogModel vlogModel = (VlogModel) VlogUtils.getViewModel((FragmentActivity) this.mContext, VlogModel.class);
        this.mVlogModel = vlogModel;
        IFilterManager iFilterManager = (IFilterManager) vlogModel.getSdkManager().getManagerService(0);
        this.mFilterManager = iFilterManager;
        this.mVaryingMasterStatus = iFilterManager.isMasterFilterOpen();
    }

    public /* synthetic */ void lambda$updateDataList$0(List list, ObservableEmitter observableEmitter) throws Exception {
        observableEmitter.onNext(Boolean.valueOf(refreshData(list)));
    }

    public void updateDataList(final List<FilterData> list) {
        Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.vlog.filter.FilterMenuPresenter$$ExternalSyntheticLambda0
            @Override // io.reactivex.ObservableOnSubscribe
            public final void subscribe(ObservableEmitter observableEmitter) {
                FilterMenuPresenter.m1793$r8$lambda$wkm7btZGhyrrZoCPUMK2ww9Vo8(FilterMenuPresenter.this, list, observableEmitter);
            }
        }).subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.miui.gallery.vlog.filter.FilterMenuPresenter$$ExternalSyntheticLambda1
            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                FilterMenuPresenter.$r8$lambda$0rl0F1qBgclYzzX2yy2vcaizpBQ(FilterMenuPresenter.this, obj);
            }
        });
    }

    public /* synthetic */ void lambda$updateDataList$1(Object obj) throws Exception {
        this.mIFilterMenuView.updateSelectItem();
    }

    public int findFilterIndexByLabel(ArrayList<FilterData> arrayList) {
        if (!TextUtils.isEmpty(this.mFilterManager.getFilterLabel()) && BaseMiscUtil.isValid(arrayList)) {
            Iterator<FilterData> it = arrayList.iterator();
            while (it.hasNext()) {
                FilterData next = it.next();
                if (next != null && this.mFilterManager.getFilterLabel().equals(next.getKey())) {
                    return arrayList.indexOf(next);
                }
            }
        }
        return -1;
    }

    public int getCurrentFilterStrength() {
        return this.mFilterManager.getCurrentFilterStrength();
    }

    public final boolean refreshData(List<FilterData> list) {
        for (FilterData filterData : list) {
            if (filterData != null) {
                String str = VlogConfig.FILTER_PATH + File.separator + filterData.getFileName();
                if (new File(str).exists()) {
                    filterData.setDownloadState(17);
                    filterData.setPath(str);
                } else {
                    String templateKey = filterData.getTemplateKey();
                    if (!TextUtils.isEmpty(templateKey)) {
                        if (templateKey.contains("-")) {
                            for (String str2 : templateKey.split("-")) {
                                VlogUtils.checkResExistWithTemplate((FragmentActivity) this.mContext, filterData, str2);
                            }
                        } else {
                            VlogUtils.checkResExistWithTemplate((FragmentActivity) this.mContext, filterData, templateKey);
                        }
                    }
                }
            }
        }
        return true;
    }

    public final void refreshLutStrength(List<FilterData> list) {
        for (FilterData filterData : list) {
            if (filterData != null && filterData.getIndex() >= 1 && filterData.getIndex() <= 12) {
                filterData.setProgress(100);
            }
        }
    }

    public void updateFilterIntensity(FilterData filterData) {
        this.mFilterManager.updateTrackFilterIntensity(filterData == null ? 0 : filterData.getProgress());
        if (!isPlaying() && !this.mVlogModel.getSdkManager().isPlayEnd()) {
            this.mVlogModel.getSdkManager().flushTimeline();
        }
        updateApplyViewPlayState();
    }

    public void buildFilter(FilterData filterData) {
        this.mCurrentFilterData = filterData;
        if (filterData == null) {
            return;
        }
        this.mFilterManager.buildTrackFilter(filterData.getPath(), filterData.getProgress(), filterData.getKey(), this.mVaryingMasterStatus);
    }

    public boolean changeMasterStatus() {
        this.mVaryingMasterStatus = !isMasterOpened();
        buildFilter(this.mCurrentFilterData);
        return this.mVaryingMasterStatus;
    }

    public void updateApplyViewPlayState() {
        this.mIFilterMenuView.updatePlayViewState(isPlaying());
    }

    public void removeFilterEffect() {
        this.mCurrentFilterData = null;
        this.mVlogModel.getSdkManager().disconnect();
        this.mFilterManager.removeTrackFilter();
        this.mVlogModel.getSdkManager().reconnect();
    }

    public void doPlayViewClickEvent() {
        if (this.mVlogModel.getSdkManager().isPlay()) {
            this.mIFilterMenuView.updatePlayViewState(false);
            this.mVlogModel.getSdkManager().pause();
            return;
        }
        this.mIFilterMenuView.updatePlayViewState(true);
        this.mVlogModel.getSdkManager().resume();
    }

    public void tryToPlayVideo() {
        if (this.mVlogModel.getSdkManager().isPlayEnd()) {
            this.mIFilterMenuView.updatePlayViewState(true);
            this.mVlogModel.getSdkManager().resume();
        }
    }

    public boolean isMasterOpened() {
        return this.mFilterManager.isMasterFilterOpen();
    }

    @Override // com.miui.gallery.vlog.base.BasePresenter
    public void destroy() {
        this.mCurrentFilterData = null;
        IBaseModel iBaseModel = this.mIBaseModel;
        if (iBaseModel instanceof FilterMenuModel) {
            ((FilterMenuModel) iBaseModel).setCallback(null);
            ((FilterMenuModel) this.mIBaseModel).clear();
        }
    }
}
