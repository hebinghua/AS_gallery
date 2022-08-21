package com.miui.gallery.vlog.template;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.base.BasePresenter;
import com.miui.gallery.vlog.base.interfaces.IBaseModel;
import com.miui.gallery.vlog.base.manager.MiVideoSdkManager;
import com.miui.gallery.vlog.home.VlogConfig;
import com.miui.gallery.vlog.home.VlogContract$IVlogView;
import com.miui.gallery.vlog.home.VlogModel;
import com.miui.gallery.vlog.template.TemplateMenuModel;
import com.miui.gallery.vlog.tools.DebugLogUtils;
import com.miui.gallery.vlog.tools.VlogUtils;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/* loaded from: classes2.dex */
public class TemplateMenuPresenter extends BasePresenter {
    public Handler mHandler;
    public TemplateMenuContract$ITemplateMenuView mITemplateMenuView;
    public VlogContract$IVlogView mIVlogView;
    public boolean mIsBuildingTemplate;
    public ThreadPoolExecutor mThreadPoolExecutor;
    public VlogModel mVlogModel;
    public Disposable mloadAssetFileDisposable;

    /* renamed from: $r8$lambda$7jmtcAUZzOk0QBC1wb1Q-s6HM-8 */
    public static /* synthetic */ void m1802$r8$lambda$7jmtcAUZzOk0QBC1wb1Qs6HM8(TemplateMenuPresenter templateMenuPresenter, Boolean bool) {
        templateMenuPresenter.lambda$loadTemplateFiles$1(bool);
    }

    public static /* synthetic */ void $r8$lambda$SQXImVjeUVx0lcYG_QvmawUEpsQ(TemplateMenuPresenter templateMenuPresenter, TemplateResource templateResource) {
        templateMenuPresenter.lambda$build$3(templateResource);
    }

    public static /* synthetic */ void $r8$lambda$X8jB3PZf3uDKebvRP9hnU9fYZKI(TemplateMenuPresenter templateMenuPresenter, String str, String str2, List list, TemplateResource templateResource, ObservableEmitter observableEmitter) {
        templateMenuPresenter.lambda$loadTemplateFiles$0(str, str2, list, templateResource, observableEmitter);
    }

    public static /* synthetic */ void $r8$lambda$r5NzPVXXOGWb9FKMbSQSDshyZI0(TemplateMenuPresenter templateMenuPresenter) {
        templateMenuPresenter.lambda$loadNoneTemplate$2();
    }

    public TemplateMenuPresenter(Context context, TemplateMenuContract$ITemplateMenuView templateMenuContract$ITemplateMenuView) {
        super(context);
        this.mIsBuildingTemplate = false;
        this.mHandler = new Handler();
        this.mThreadPoolExecutor = new ThreadPoolExecutor(1, 20, 3L, TimeUnit.SECONDS, new SynchronousQueue());
        this.mVlogModel = (VlogModel) VlogUtils.getViewModel((FragmentActivity) this.mContext, VlogModel.class);
        this.mITemplateMenuView = templateMenuContract$ITemplateMenuView;
        if (context instanceof VlogContract$IVlogView) {
            this.mIVlogView = (VlogContract$IVlogView) context;
        }
        this.mIBaseModel = new TemplateMenuModel(new TemplateMenuModel.Callback() { // from class: com.miui.gallery.vlog.template.TemplateMenuPresenter.1
            {
                TemplateMenuPresenter.this = this;
            }

            @Override // com.miui.gallery.vlog.template.TemplateMenuModel.Callback
            public void loadDataSuccess(List<TemplateResource> list) {
                TemplateMenuPresenter.this.refreshData(list);
                TemplateMenuPresenter.this.mITemplateMenuView.loadRecyclerView(list, TemplateMenuPresenter.this.intentTemplatePosition(list));
            }

            @Override // com.miui.gallery.vlog.template.TemplateMenuModel.Callback
            public void loadDataFail() {
                ToastUtils.makeText(TemplateMenuPresenter.this.mContext, TemplateMenuPresenter.this.mContext.getString(R$string.vlog_download_failed_for_notwork));
                ArrayList arrayList = new ArrayList();
                arrayList.add(TemplateResource.getDefaultItem());
                TemplateMenuPresenter.this.mITemplateMenuView.loadRecyclerView(arrayList, TemplateMenuPresenter.this.intentTemplatePosition(arrayList));
                TemplateMenuPresenter.this.mITemplateMenuView.updatePlayViewState(TemplateMenuPresenter.this.mVlogModel.getSdkManager().isPlay());
            }
        });
    }

    public final int intentTemplatePosition(List<TemplateResource> list) {
        String templateName = this.mVlogModel.getTemplateName();
        if (TextUtils.isEmpty(templateName)) {
            return 0;
        }
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getNameKey().equals(templateName)) {
                return i;
            }
        }
        return 0;
    }

    public void refreshData(List<TemplateResource> list) {
        Collections.sort(list);
        for (TemplateResource templateResource : list) {
            if (templateResource != null) {
                String str = VlogConfig.TEMPALTE_PATH + File.separator + templateResource.getKey();
                if (new File(str).exists()) {
                    templateResource.setDownloadState(17);
                    templateResource.setFilePath(str);
                }
            }
        }
    }

    public boolean isBuildingTemplate() {
        return this.mIsBuildingTemplate;
    }

    public void loadTemplateFiles(final TemplateResource templateResource) {
        DebugLogUtils.HAS_LOADED_SELECT_TEMPLATE = false;
        DebugLogUtils.IS_FIRST_FRAME_LOADED_SELECT_TEMPLATE = false;
        DebugLogUtils.startDebugLogSpecialTime("TemplateMenuPresenter", "vlog select Template");
        DebugLogUtils.startDebugLog("TemplateMenuPresenter", "vlog loadTemplateFiles");
        this.mIsBuildingTemplate = true;
        VlogContract$IVlogView vlogContract$IVlogView = this.mIVlogView;
        if (vlogContract$IVlogView != null) {
            vlogContract$IVlogView.showProgressBar();
        }
        final String assetName = templateResource.getAssetName();
        final String nameKey = templateResource.getNameKey();
        destroyLoadAssetFileDisposable();
        final List<String> currentVideoPaths = this.mVlogModel.shouldRematchTemplate(nameKey) ? this.mVlogModel.getCurrentVideoPaths() : null;
        this.mloadAssetFileDisposable = Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.vlog.template.TemplateMenuPresenter$$ExternalSyntheticLambda2
            @Override // io.reactivex.ObservableOnSubscribe
            public final void subscribe(ObservableEmitter observableEmitter) {
                TemplateMenuPresenter.$r8$lambda$X8jB3PZf3uDKebvRP9hnU9fYZKI(TemplateMenuPresenter.this, assetName, nameKey, currentVideoPaths, templateResource, observableEmitter);
            }
        }).subscribeOn(Schedulers.from(this.mThreadPoolExecutor)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.miui.gallery.vlog.template.TemplateMenuPresenter$$ExternalSyntheticLambda3
            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                TemplateMenuPresenter.m1802$r8$lambda$7jmtcAUZzOk0QBC1wb1Qs6HM8(TemplateMenuPresenter.this, (Boolean) obj);
            }
        });
    }

    public /* synthetic */ void lambda$loadTemplateFiles$0(String str, String str2, List list, TemplateResource templateResource, ObservableEmitter observableEmitter) throws Exception {
        boolean loadTemplate = this.mVlogModel.getTemplateFilesManager().loadTemplate(str, str2, list);
        DebugLogUtils.endDebugLog("TemplateMenuPresenter", "vlog loadTemplateFiles");
        DebugLogUtils.HAS_LOADED_TEMPLATE_DEFAULT = true;
        if (loadTemplate) {
            build(templateResource);
            play();
        }
        observableEmitter.onNext(Boolean.valueOf(loadTemplate));
    }

    public /* synthetic */ void lambda$loadTemplateFiles$1(Boolean bool) throws Exception {
        VlogContract$IVlogView vlogContract$IVlogView = this.mIVlogView;
        if (vlogContract$IVlogView != null) {
            vlogContract$IVlogView.dismissProgressBar();
        }
        this.mIsBuildingTemplate = false;
    }

    public void loadNoneTemplate() {
        DebugLogUtils.HAS_LOADED_TEMPLATE_DEFAULT = true;
        if (!this.mVlogModel.getTemplateFilesManager().isTemplateApplied()) {
            return;
        }
        DebugLogUtils.startDebugLog("TemplateMenuPresenter", "vlog loadNoneTemplate");
        this.mIsBuildingTemplate = true;
        this.mVlogModel.updateVideoPaths();
        this.mVlogModel.getSdkManager().doOperationCombined(new MiVideoSdkManager.IDoOperationCombined() { // from class: com.miui.gallery.vlog.template.TemplateMenuPresenter$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.vlog.base.manager.MiVideoSdkManager.IDoOperationCombined
            public final void combined() {
                TemplateMenuPresenter.$r8$lambda$r5NzPVXXOGWb9FKMbSQSDshyZI0(TemplateMenuPresenter.this);
            }
        });
        play();
        this.mIsBuildingTemplate = false;
        this.mITemplateMenuView.updatePlayViewState(true);
        DebugLogUtils.endDebugLog("TemplateMenuPresenter", "vlog loadNoneTemplate");
    }

    public /* synthetic */ void lambda$loadNoneTemplate$2() {
        this.mVlogModel.getTemplateFilesManager().remove(this.mVlogModel.getCurrentVideoPaths());
        this.mVlogModel.getSdkManager().seek(0L);
    }

    public void play() {
        this.mVlogModel.getTemplateFilesManager().play();
        this.mITemplateMenuView.updatePlayViewState(true);
    }

    @Override // com.miui.gallery.vlog.base.BasePresenter
    public void pause() {
        super.pause();
        this.mITemplateMenuView.updatePlayViewState(false);
    }

    public void build(final TemplateResource templateResource) {
        this.mVlogModel.getSdkManager().doOperationCombined(new MiVideoSdkManager.IDoOperationCombined() { // from class: com.miui.gallery.vlog.template.TemplateMenuPresenter$$ExternalSyntheticLambda1
            @Override // com.miui.gallery.vlog.base.manager.MiVideoSdkManager.IDoOperationCombined
            public final void combined() {
                TemplateMenuPresenter.$r8$lambda$SQXImVjeUVx0lcYG_QvmawUEpsQ(TemplateMenuPresenter.this, templateResource);
            }
        });
    }

    public /* synthetic */ void lambda$build$3(TemplateResource templateResource) {
        this.mVlogModel.getTemplateFilesManager().build(this.mVlogModel.getVideoClips(), templateResource.key);
        DebugLogUtils.HAS_LOADED_SELECT_TEMPLATE = true;
        this.mVlogModel.getSdkManager().seek(0L);
        this.mVlogModel.getSdkManager().updateBaseSpeed();
    }

    @Override // com.miui.gallery.vlog.base.BasePresenter
    public void destroy() {
        destroyLoadAssetFileDisposable();
        if (!this.mThreadPoolExecutor.isShutdown()) {
            this.mThreadPoolExecutor.shutdown();
        }
        IBaseModel iBaseModel = this.mIBaseModel;
        if (iBaseModel instanceof TemplateMenuModel) {
            ((TemplateMenuModel) iBaseModel).setCallback(null);
            ((TemplateMenuModel) this.mIBaseModel).clear();
        }
    }

    public final void destroyLoadAssetFileDisposable() {
        Disposable disposable = this.mloadAssetFileDisposable;
        if (disposable == null || disposable.isDisposed()) {
            return;
        }
        this.mloadAssetFileDisposable.dispose();
    }
}
