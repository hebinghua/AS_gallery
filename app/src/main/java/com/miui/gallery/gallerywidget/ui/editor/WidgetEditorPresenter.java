package com.miui.gallery.gallerywidget.ui.editor;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.TextUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.miui.gallery.gallerywidget.common.GalleryWidgetUtils;
import com.miui.gallery.gallerywidget.common.IWidgetProviderConfig;
import com.miui.gallery.gallerywidget.db.CustomWidgetDBEntity;
import com.miui.gallery.gallerywidget.db.CustomWidgetDBManager;
import com.miui.gallery.glide.load.model.GalleryModel;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class WidgetEditorPresenter {
    public int mAppWidgetId;
    public Context mContext;
    public int mCurrentPicIndex;
    public Disposable mDbDisposable;
    public Disposable mInitDataDisposable;
    public boolean mIsFromWidgetEditor;
    public final WidgetEditorContract$IWidgetEditorModel<ImageEntity> mWidgetEditorModel;
    public WidgetEditorContract$IWidgetEditorView mWidgetEditorView;
    public IWidgetProviderConfig.WidgetSize mWidgetSize;
    public final String TAG = "WidgetEditorPresenter";
    public int mPicCount = 1;

    public static /* synthetic */ void $r8$lambda$MJIf17z2MVlqG6rkAAHSlH1ZHR8(WidgetEditorPresenter widgetEditorPresenter, Throwable th) {
        widgetEditorPresenter.lambda$loadData$2(th);
    }

    public static /* synthetic */ void $r8$lambda$RyrPAA6hME0Ar1ybvzAoTDjJyX8(WidgetEditorPresenter widgetEditorPresenter, Object obj) {
        widgetEditorPresenter.lambda$onSave$4(obj);
    }

    public static /* synthetic */ void $r8$lambda$X4mHt4qwhnNMIPBp4dEjmukXoJY(WidgetEditorPresenter widgetEditorPresenter, ObservableEmitter observableEmitter) {
        widgetEditorPresenter.lambda$onSave$3(observableEmitter);
    }

    public static /* synthetic */ void $r8$lambda$_GCKXi46adzbtIPlNfZfQ_iy9GU(WidgetEditorPresenter widgetEditorPresenter, Object obj) {
        widgetEditorPresenter.lambda$loadData$1(obj);
    }

    public static /* synthetic */ void $r8$lambda$hoU_jdFA51zygS8esd8gXDJIszI(WidgetEditorPresenter widgetEditorPresenter, Intent intent, ObservableEmitter observableEmitter) {
        widgetEditorPresenter.lambda$loadData$0(intent, observableEmitter);
    }

    /* renamed from: $r8$lambda$uOyLdgbvCAD3_t5uiCDp545W-6E */
    public static /* synthetic */ void m942$r8$lambda$uOyLdgbvCAD3_t5uiCDp545W6E(WidgetEditorPresenter widgetEditorPresenter, Throwable th) {
        widgetEditorPresenter.lambda$onSave$5(th);
    }

    public WidgetEditorPresenter(Context context, Intent intent, WidgetEditorContract$IWidgetEditorView widgetEditorContract$IWidgetEditorView) {
        this.mWidgetSize = IWidgetProviderConfig.WidgetSize.SIZE_2_2;
        this.mContext = context;
        this.mWidgetEditorView = widgetEditorContract$IWidgetEditorView;
        this.mWidgetEditorModel = new WidgetEditorModel(context);
        this.mIsFromWidgetEditor = intent.getBooleanExtra("is_from_widget_editor", false);
        this.mAppWidgetId = intent.getIntExtra("gallery_app_widget_id", -1);
        this.mWidgetSize = (IWidgetProviderConfig.WidgetSize) intent.getSerializableExtra("gallery_app_widget_size");
    }

    public void loadData(final Intent intent) {
        if (intent == null) {
            return;
        }
        this.mInitDataDisposable = Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.gallerywidget.ui.editor.WidgetEditorPresenter$$ExternalSyntheticLambda1
            @Override // io.reactivex.ObservableOnSubscribe
            public final void subscribe(ObservableEmitter observableEmitter) {
                WidgetEditorPresenter.$r8$lambda$hoU_jdFA51zygS8esd8gXDJIszI(WidgetEditorPresenter.this, intent, observableEmitter);
            }
        }).subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.miui.gallery.gallerywidget.ui.editor.WidgetEditorPresenter$$ExternalSyntheticLambda5
            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                WidgetEditorPresenter.$r8$lambda$_GCKXi46adzbtIPlNfZfQ_iy9GU(WidgetEditorPresenter.this, obj);
            }
        }, new Consumer() { // from class: com.miui.gallery.gallerywidget.ui.editor.WidgetEditorPresenter$$ExternalSyntheticLambda2
            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                WidgetEditorPresenter.$r8$lambda$MJIf17z2MVlqG6rkAAHSlH1ZHR8(WidgetEditorPresenter.this, (Throwable) obj);
            }
        });
    }

    public /* synthetic */ void lambda$loadData$0(Intent intent, ObservableEmitter observableEmitter) throws Exception {
        loadPicPathData(intent);
        if (this.mIsFromWidgetEditor) {
            restoreDBData();
        }
        observableEmitter.onNext(new Object());
        observableEmitter.onComplete();
    }

    public /* synthetic */ void lambda$loadData$1(Object obj) throws Exception {
        if (this.mCurrentPicIndex >= getDataList().size() || this.mAppWidgetId < 0) {
            this.mWidgetEditorView.loadDataFail();
        } else {
            this.mWidgetEditorView.loadDataSuccess();
        }
    }

    public /* synthetic */ void lambda$loadData$2(Throwable th) throws Exception {
        th.printStackTrace();
        this.mWidgetEditorView.loadDataFail();
    }

    public void loadPicPathData(Intent intent) {
        if (intent == null) {
            return;
        }
        this.mWidgetEditorModel.loadData(intent);
        this.mPicCount = this.mWidgetEditorModel.getDataList().size();
    }

    public final void restoreDBData() {
        CustomWidgetDBEntity findWidgetEntity = CustomWidgetDBManager.getInstance().findWidgetEntity(this.mAppWidgetId);
        this.mCurrentPicIndex = findWidgetEntity.getCurrentIndex();
        String picCropList = findWidgetEntity.getPicCropList();
        String picMatrixList = findWidgetEntity.getPicMatrixList();
        if (!TextUtils.isEmpty(picCropList)) {
            List<String> dataList = GalleryWidgetUtils.getDataList(picCropList);
            if (dataList.size() == getDataList().size()) {
                for (int i = 0; i < dataList.size(); i++) {
                    getDataList().get(i).setCropInfo(GalleryWidgetUtils.getCropInfo(dataList.get(i)));
                }
            }
        }
        if (!TextUtils.isEmpty(picMatrixList)) {
            List<String> dataList2 = GalleryWidgetUtils.getDataList(picMatrixList);
            if (dataList2.size() != getDataList().size()) {
                return;
            }
            for (int i2 = 0; i2 < dataList2.size(); i2++) {
                getDataList().get(i2).setCropMatrix(GalleryWidgetUtils.getMatrix(dataList2.get(i2)));
            }
        }
    }

    public void loadPicture() {
        String currentPicPath = getCurrentPicPath();
        if (TextUtils.isEmpty(currentPicPath)) {
            return;
        }
        Glide.with(this.mContext).mo985asBitmap().mo962load(GalleryModel.of(currentPicPath)).mo946apply((BaseRequestOptions<?>) GalleryWidgetUtils.getWidgetGlideOptions(currentPicPath, WidgetEditorManager.getRegionRect(new float[]{0.0f, 0.0f, 1.0f, 1.0f}), ScreenUtils.getScreenWidth() * 2)).into((RequestBuilder<Bitmap>) new WidgetCustomTarget(this.mWidgetEditorView, 1));
    }

    public ArrayList<String> getPicIDList() {
        ArrayList<String> arrayList = new ArrayList<>();
        for (ImageEntity imageEntity : getDataList()) {
            if (!String.valueOf(0L).equals(imageEntity.getId())) {
                arrayList.add(imageEntity.getId());
            }
        }
        return arrayList;
    }

    public void onSave() {
        this.mDbDisposable = Observable.create(new ObservableOnSubscribe() { // from class: com.miui.gallery.gallerywidget.ui.editor.WidgetEditorPresenter$$ExternalSyntheticLambda0
            @Override // io.reactivex.ObservableOnSubscribe
            public final void subscribe(ObservableEmitter observableEmitter) {
                WidgetEditorPresenter.$r8$lambda$X4mHt4qwhnNMIPBp4dEjmukXoJY(WidgetEditorPresenter.this, observableEmitter);
            }
        }).subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.miui.gallery.gallerywidget.ui.editor.WidgetEditorPresenter$$ExternalSyntheticLambda4
            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                WidgetEditorPresenter.$r8$lambda$RyrPAA6hME0Ar1ybvzAoTDjJyX8(WidgetEditorPresenter.this, obj);
            }
        }, new Consumer() { // from class: com.miui.gallery.gallerywidget.ui.editor.WidgetEditorPresenter$$ExternalSyntheticLambda3
            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                WidgetEditorPresenter.m942$r8$lambda$uOyLdgbvCAD3_t5uiCDp545W6E(WidgetEditorPresenter.this, (Throwable) obj);
            }
        });
    }

    public /* synthetic */ void lambda$onSave$3(ObservableEmitter observableEmitter) throws Exception {
        updateEntityToDB();
        observableEmitter.onNext(new Object());
        observableEmitter.onComplete();
    }

    public /* synthetic */ void lambda$onSave$4(Object obj) throws Exception {
        this.mWidgetEditorView.saveSuccess();
    }

    public /* synthetic */ void lambda$onSave$5(Throwable th) throws Exception {
        this.mWidgetEditorView.saveFailed();
        if (th != null) {
            th.printStackTrace();
        }
    }

    public void release() {
        Disposable disposable = this.mInitDataDisposable;
        if (disposable != null && !disposable.isDisposed()) {
            this.mInitDataDisposable.dispose();
        }
        Disposable disposable2 = this.mDbDisposable;
        if (disposable2 == null || disposable2.isDisposed()) {
            return;
        }
        this.mDbDisposable.dispose();
    }

    public final void updateEntityToDB() {
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        for (ImageEntity imageEntity : getDataList()) {
            arrayList.add(GalleryWidgetUtils.getCropInfoString(imageEntity.getCropInfo()));
            arrayList2.add(GalleryWidgetUtils.getMatrixValueString(imageEntity.getCropMatrix()));
            arrayList3.add(imageEntity.getPicPath());
            arrayList4.add(imageEntity.getId());
        }
        CustomWidgetDBEntity build = new CustomWidgetDBEntity.Builder().setWidgetId(this.mAppWidgetId).setWidgetSize(this.mWidgetSize.getValue()).setPicPath(getCurrentPicPath()).setPicCropList(GalleryWidgetUtils.getDataListString(arrayList)).setPicMatrixList(GalleryWidgetUtils.getDataListString(arrayList2)).setPicPathList(GalleryWidgetUtils.getDataListString(arrayList3)).setPicIDList(GalleryWidgetUtils.getDataListString(arrayList4)).setCurrentIndex(getCurrentPicIndex()).build();
        if (CustomWidgetDBManager.getInstance().findWidgetEntity((long) build.getWidgetId()) == null) {
            CustomWidgetDBManager.getInstance().add(build);
        } else {
            CustomWidgetDBManager.getInstance().update(build, build.getWidgetId());
        }
        DefaultLogger.d("WidgetEditorPresenter", "---log---updateEntityToDB appWidgetId:%d  getPicPath:%s  currentIndex:%d>", Integer.valueOf(build.getWidgetId()), build.getPicPath(), Integer.valueOf(build.getCurrentIndex()));
        GalleryWidgetUtils.updateCustomWidgetStatus(build.getWidgetId(), GalleryWidgetUtils.getWidgetSize(build.getWidgetSize()));
    }

    public void fillCurrentImageEntityData(Matrix matrix, float[] fArr) {
        getCurrentImageEntity().setCropMatrix(new Matrix(matrix));
        getCurrentImageEntity().setCropInfo(fArr);
    }

    public final ImageEntity getCurrentImageEntity() {
        return getDataList().get(getCurrentPicIndex());
    }

    public String getCurrentPicPath() {
        if (this.mCurrentPicIndex >= getDataList().size()) {
            return null;
        }
        return getDataList().get(this.mCurrentPicIndex).getPicPath();
    }

    public int getCurrentPicIndex() {
        return this.mCurrentPicIndex;
    }

    public void setCurrentPicIndex(int i) {
        this.mCurrentPicIndex = i;
    }

    public List<ImageEntity> getDataList() {
        return this.mWidgetEditorModel.getDataList();
    }

    /* loaded from: classes2.dex */
    public class WidgetCustomTarget extends CustomTarget<Bitmap> {
        public WeakReference<WidgetEditorContract$IWidgetEditorView> mReference;
        public int mRequestCode;

        @Override // com.bumptech.glide.request.target.Target
        public /* bridge */ /* synthetic */ void onResourceReady(Object obj, Transition transition) {
            onResourceReady((Bitmap) obj, (Transition<? super Bitmap>) transition);
        }

        public WidgetCustomTarget(WidgetEditorContract$IWidgetEditorView widgetEditorContract$IWidgetEditorView, int i) {
            WidgetEditorPresenter.this = r1;
            this.mReference = new WeakReference<>(widgetEditorContract$IWidgetEditorView);
            this.mRequestCode = i;
        }

        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
            WidgetEditorContract$IWidgetEditorView widgetEditorContract$IWidgetEditorView = this.mReference.get();
            if (widgetEditorContract$IWidgetEditorView == null) {
                return;
            }
            Bitmap bitmapFitOrientation = GalleryWidgetUtils.getBitmapFitOrientation(bitmap, GalleryWidgetUtils.getPicOrientation(WidgetEditorPresenter.this.getCurrentPicPath()));
            if (this.mRequestCode == 1) {
                widgetEditorContract$IWidgetEditorView.setPreviewBitmap(bitmapFitOrientation);
            }
            widgetEditorContract$IWidgetEditorView.loadPictureSuccess();
        }

        @Override // com.bumptech.glide.request.target.Target
        public void onLoadCleared(Drawable drawable) {
            DefaultLogger.d("WidgetCustomTarget", "---log---onLoadCleared>");
        }

        @Override // com.bumptech.glide.request.target.CustomTarget, com.bumptech.glide.request.target.Target
        public void onLoadFailed(Drawable drawable) {
            DefaultLogger.d("WidgetCustomTarget", "---log---onLoadFailed>");
            WidgetEditorContract$IWidgetEditorView widgetEditorContract$IWidgetEditorView = this.mReference.get();
            if (widgetEditorContract$IWidgetEditorView == null) {
                return;
            }
            widgetEditorContract$IWidgetEditorView.loadPictureFailed();
        }
    }
}
