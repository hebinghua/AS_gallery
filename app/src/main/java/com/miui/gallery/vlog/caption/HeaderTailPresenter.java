package com.miui.gallery.vlog.caption;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.base.BasePresenter;
import com.miui.gallery.vlog.base.interfaces.IBaseModel;
import com.miui.gallery.vlog.caption.HeaderTailModel;
import com.miui.gallery.vlog.entity.HeaderTailData;
import com.miui.gallery.vlog.home.VlogConfig;
import com.miui.gallery.vlog.home.VlogModel;
import com.miui.gallery.vlog.sdk.interfaces.IHeaderTailManager;
import com.miui.gallery.vlog.sdk.interfaces.IRemoveHeadTailCallback;
import com.miui.gallery.vlog.tools.VlogUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/* loaded from: classes2.dex */
public class HeaderTailPresenter extends BasePresenter {
    public IHeaderTailManager mIHeaderTailManager;
    public HeaderTailContract$ITitleStyleView mITitleStyleView;
    public LoadDataCallback mLoadDataCallback;
    public VlogModel mVlogModel;

    public static /* synthetic */ void $r8$lambda$fNZzG07aLec1HLrEL_j1GZMGn68(HeaderTailPresenter headerTailPresenter, ArrayList arrayList, ObservableEmitter observableEmitter) {
        headerTailPresenter.lambda$updateDataList$1(arrayList, observableEmitter);
    }

    /* renamed from: $r8$lambda$gl0799rXCI9ip-XllLcl_muJ69U */
    public static /* synthetic */ void m1779$r8$lambda$gl0799rXCI9ipXllLcl_muJ69U(HeaderTailPresenter headerTailPresenter) {
        headerTailPresenter.lambda$new$0();
    }

    public static /* synthetic */ void $r8$lambda$pscR_vBcgrLCkcMNuDF8ILJ8M0Y(HeaderTailPresenter headerTailPresenter, Object obj) {
        headerTailPresenter.lambda$updateDataList$2(obj);
    }

    public HeaderTailPresenter(Context context, HeaderTailContract$ITitleStyleView headerTailContract$ITitleStyleView) {
        super(context);
        this.mITitleStyleView = headerTailContract$ITitleStyleView;
        LoadDataCallback loadDataCallback = new LoadDataCallback(this);
        this.mLoadDataCallback = loadDataCallback;
        this.mIBaseModel = new HeaderTailModel(loadDataCallback);
        VlogModel vlogModel = (VlogModel) VlogUtils.getViewModel((FragmentActivity) this.mContext, VlogModel.class);
        this.mVlogModel = vlogModel;
        IHeaderTailManager iHeaderTailManager = (IHeaderTailManager) vlogModel.getSdkManager().getManagerService(8);
        this.mIHeaderTailManager = iHeaderTailManager;
        iHeaderTailManager.setIRemoveHeadTail(new IRemoveHeadTailCallback() { // from class: com.miui.gallery.vlog.caption.HeaderTailPresenter$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.vlog.sdk.interfaces.IRemoveHeadTailCallback
            public final void onRemoved() {
                HeaderTailPresenter.m1779$r8$lambda$gl0799rXCI9ipXllLcl_muJ69U(HeaderTailPresenter.this);
            }
        });
    }

    public /* synthetic */ void lambda$new$0() {
        this.mITitleStyleView.onRemoveHeadTail();
    }

    /* loaded from: classes2.dex */
    public static class LoadDataCallback implements HeaderTailModel.Callback {
        public WeakReference<HeaderTailPresenter> mHeaderTailPresenter;

        public LoadDataCallback(HeaderTailPresenter headerTailPresenter) {
            this.mHeaderTailPresenter = new WeakReference<>(headerTailPresenter);
        }

        @Override // com.miui.gallery.vlog.caption.HeaderTailModel.Callback
        public void loadDataSuccess(ArrayList<HeaderTailData> arrayList) {
            HeaderTailPresenter headerTailPresenter = this.mHeaderTailPresenter.get();
            if (headerTailPresenter == null) {
                return;
            }
            Collections.sort(arrayList);
            headerTailPresenter.refreshData(arrayList);
            headerTailPresenter.mITitleStyleView.loadRecyclerView(arrayList);
        }

        @Override // com.miui.gallery.vlog.caption.HeaderTailModel.Callback
        public void loadDataFail() {
            HeaderTailPresenter headerTailPresenter = this.mHeaderTailPresenter.get();
            if (headerTailPresenter == null) {
                return;
            }
            ToastUtils.makeText(headerTailPresenter.mContext, headerTailPresenter.mContext.getString(R$string.vlog_download_failed_for_notwork));
            ArrayList<HeaderTailData> arrayList = new ArrayList<>();
            arrayList.add(HeaderTailData.getDefaultItem());
            arrayList.add(HeaderTailData.getCustomItem());
            headerTailPresenter.mITitleStyleView.loadRecyclerView(arrayList);
        }

        public void release() {
            WeakReference<HeaderTailPresenter> weakReference = this.mHeaderTailPresenter;
            if (weakReference != null) {
                weakReference.clear();
                this.mHeaderTailPresenter = null;
            }
        }
    }

    public /* synthetic */ void lambda$updateDataList$1(ArrayList arrayList, ObservableEmitter observableEmitter) throws Exception {
        observableEmitter.onNext(Boolean.valueOf(refreshData(arrayList)));
    }

    public void updateDataList(final ArrayList<HeaderTailData> arrayList) {
        Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.vlog.caption.HeaderTailPresenter$$ExternalSyntheticLambda1
            @Override // io.reactivex.ObservableOnSubscribe
            public final void subscribe(ObservableEmitter observableEmitter) {
                HeaderTailPresenter.$r8$lambda$fNZzG07aLec1HLrEL_j1GZMGn68(HeaderTailPresenter.this, arrayList, observableEmitter);
            }
        }).subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.miui.gallery.vlog.caption.HeaderTailPresenter$$ExternalSyntheticLambda2
            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                HeaderTailPresenter.$r8$lambda$pscR_vBcgrLCkcMNuDF8ILJ8M0Y(HeaderTailPresenter.this, obj);
            }
        });
    }

    public /* synthetic */ void lambda$updateDataList$2(Object obj) throws Exception {
        this.mITitleStyleView.updateSelectItem();
    }

    public int findIndexByLabel(ArrayList<HeaderTailData> arrayList) {
        String headTailLabel = this.mIHeaderTailManager.getHeadTailLabel();
        if (!TextUtils.isEmpty(headTailLabel) && BaseMiscUtil.isValid(arrayList)) {
            Iterator<HeaderTailData> it = arrayList.iterator();
            while (it.hasNext()) {
                HeaderTailData next = it.next();
                if (next != null && headTailLabel.equals(next.getKey())) {
                    return arrayList.indexOf(next);
                }
            }
        }
        return -1;
    }

    public boolean refreshData(ArrayList<HeaderTailData> arrayList) {
        Iterator<HeaderTailData> it = arrayList.iterator();
        while (it.hasNext()) {
            HeaderTailData next = it.next();
            if (next != null) {
                if (new File(VlogConfig.HEADER_TAIL_ASSET_PATH + File.separator + next.getFileName()).exists()) {
                    next.setDownloadState(17);
                } else {
                    String templateKey = next.getTemplateKey();
                    if (!TextUtils.isEmpty(templateKey)) {
                        VlogUtils.checkResExistWithTemplate((FragmentActivity) this.mContext, next, templateKey);
                    }
                }
            }
        }
        return true;
    }

    public void setHeaderTailText(String str, String str2) {
        this.mIHeaderTailManager.setHeaderTailText(str, str2);
    }

    public void setCustomHeaderTail(boolean z, String str) {
        this.mIHeaderTailManager.setCustomHeaderTail(z, str);
    }

    @Override // com.miui.gallery.vlog.base.BasePresenter
    public void destroy() {
        IBaseModel iBaseModel = this.mIBaseModel;
        if (iBaseModel instanceof HeaderTailModel) {
            ((HeaderTailModel) iBaseModel).setCallback(null);
            ((HeaderTailModel) this.mIBaseModel).clear();
        }
        IHeaderTailManager iHeaderTailManager = this.mIHeaderTailManager;
        if (iHeaderTailManager != null) {
            iHeaderTailManager.setIRemoveHeadTail(null);
        }
        LoadDataCallback loadDataCallback = this.mLoadDataCallback;
        if (loadDataCallback != null) {
            loadDataCallback.release();
        }
    }

    public void doPlayViewClickEvent() {
        if (this.mVlogModel.getSdkManager().isPlay()) {
            this.mITitleStyleView.updatePlayViewState(false);
            this.mVlogModel.getSdkManager().pause();
            return;
        }
        this.mITitleStyleView.updatePlayViewState(true);
        this.mVlogModel.getSdkManager().resume();
    }
}
