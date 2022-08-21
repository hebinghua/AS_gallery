package com.miui.gallery.gallerywidget.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.activity.ExternalPhotoPageActivity;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureListener;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.gallerywidget.common.GalleryWidgetUtils;
import com.miui.gallery.gallerywidget.common.IWidgetProviderConfig;
import com.miui.gallery.gallerywidget.common.WidgetInstallManager;
import com.miui.gallery.gallerywidget.db.CustomWidgetDBEntity;
import com.miui.gallery.gallerywidget.db.CustomWidgetDBManager;
import com.miui.gallery.gallerywidget.ui.editor.WidgetPhotoEditorActivity;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class CustomDispatchActivity extends BaseActivity {
    public Future<CustomWidgetDBEntity> mFuture;
    public boolean mIsLargeScreenMode;
    public int mMiuiWidgetScreenSide;
    public int mPhotoCount;
    public String mPicId;
    public String mPicPath;
    public String mPicPathList;
    public int mPosition;
    public int mWidgetId;
    public IWidgetProviderConfig.WidgetSize mWidgetSize = IWidgetProviderConfig.WidgetSize.SIZE_2_2;

    @Override // com.miui.gallery.activity.BaseActivity
    public boolean isCheckPermissionCustomized() {
        return true;
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        DefaultLogger.d("CustomDispatchActivity", "---log---getTaskId>" + getTaskId());
        super.onCreate(bundle);
        this.mIsLargeScreenMode = getIntent().getBooleanExtra("isLargeScreenMode", false);
        this.mMiuiWidgetScreenSide = getIntent().getIntExtra("miuiWidgetScreenSide", -1);
        DefaultLogger.d("CustomDispatchActivity", "---log---isLargeScreenMode: " + this.mIsLargeScreenMode + ", miuiWidgetScreenSide: " + this.mMiuiWidgetScreenSide);
        if (getIntent().getData() != null) {
            this.mWidgetId = Integer.parseInt(getIntent().getData().getQueryParameter("appWidgetId"));
            this.mWidgetSize = GalleryWidgetUtils.getWidgetSize(Integer.parseInt(getIntent().getData().getQueryParameter("gallery_app_widget_size")));
            if (this.mWidgetId > 0) {
                this.mFuture = ThreadManager.getMiscPool().submit(new SpecificJob(this.mWidgetId), new SpecificFutureListener(this));
                return;
            }
        }
        this.mWidgetId = getIntent().getIntExtra("gallery_app_widget_id", -1);
        this.mPicPath = getIntent().getStringExtra("selected_pic_path");
        this.mPicPathList = getIntent().getStringExtra("selected_pic_path_list");
        this.mPicId = getIntent().getStringExtra("selected_pic_id");
        this.mPhotoCount = getIntent().getIntExtra("selected_photo_count", 0);
        this.mPosition = getIntent().getIntExtra("selected_current_index", 0);
        int intExtra = getIntent().getIntExtra("gallery_app_widget_size_value", 0);
        if (intExtra != 0) {
            this.mWidgetSize = GalleryWidgetUtils.getWidgetSize(intExtra);
        } else {
            this.mWidgetSize = (IWidgetProviderConfig.WidgetSize) getIntent().getSerializableExtra("gallery_app_widget_size");
        }
        if (this.mWidgetId <= 0) {
            DefaultLogger.e("CustomDispatchActivity", "---log---invalid widget id !>");
            finish();
        } else if (TextUtils.isEmpty(this.mPicPath)) {
            jumpToPicker();
        } else {
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(this.mPicPath, IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("CustomDispatchActivity", "onCreate"));
            if (documentFile == null || !documentFile.exists()) {
                WidgetInstallManager.setCustomWidgetEmpty(this, this.mWidgetId, -1, this.mWidgetSize);
                jumpToPicker();
                return;
            }
            jumpToPhotoPage();
        }
    }

    @Override // com.miui.gallery.activity.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        Future<CustomWidgetDBEntity> future = this.mFuture;
        if (future != null) {
            future.cancel();
        }
        super.onDestroy();
    }

    @Override // android.app.Activity, android.content.ContextWrapper, android.content.Context
    public void startActivity(Intent intent) {
        intent.addFlags(268468224);
        super.startActivity(intent);
    }

    public final void jumpToEditor(CustomWidgetDBEntity customWidgetDBEntity) {
        ArrayList arrayList = (ArrayList) GalleryWidgetUtils.getDataList(customWidgetDBEntity.getPicPathList());
        Serializable serializable = (ArrayList) GalleryWidgetUtils.getDataList(customWidgetDBEntity.getPicIDList());
        if (BaseMiscUtil.isValid(arrayList)) {
            Intent intent = new Intent(this, WidgetPhotoEditorActivity.class);
            intent.putExtra("is_from_widget_editor", true);
            intent.putExtra("gallery_app_widget_id", customWidgetDBEntity.getWidgetId());
            intent.putExtra("gallery_app_widget_size", GalleryWidgetUtils.getWidgetSize(customWidgetDBEntity.getWidgetSize()));
            intent.putExtra("selected_pic_path_list", arrayList);
            intent.putExtra("pick_sha1", serializable);
            intent.putExtra("isLargeScreenMode", this.mIsLargeScreenMode);
            intent.putExtra("miuiWidgetScreenSide", this.mMiuiWidgetScreenSide);
            startActivity(intent);
        }
        finish();
    }

    public final void jumpToPicker() {
        Intent pickGalleryIntent = GalleryWidgetUtils.getPickGalleryIntent(this);
        Intent intent = new Intent(this, WidgetPhotoEditorActivity.class);
        intent.putExtra("gallery_app_widget_id", this.mWidgetId);
        intent.putExtra("gallery_app_widget_size", this.mWidgetSize);
        intent.putExtra("isLargeScreenMode", this.mIsLargeScreenMode);
        intent.putExtra("miuiWidgetScreenSide", this.mMiuiWidgetScreenSide);
        intent.putExtra("is_from_widget_click", true);
        pickGalleryIntent.putExtra("pick_intent", intent);
        pickGalleryIntent.putExtra("pick_close_type", 3);
        startActivity(pickGalleryIntent);
        finish();
    }

    public final void jumpToPhotoPage() {
        List<String> dataList = GalleryWidgetUtils.getDataList(this.mPicPathList);
        ArrayList arrayList = new ArrayList();
        if (dataList.size() > 0) {
            for (int i = 0; i < dataList.size(); i++) {
                arrayList.add(Uri.fromFile(new File(dataList.get(i))));
            }
            Intent intent = new Intent(this, ExternalPhotoPageActivity.class);
            intent.setData(Uri.fromFile(new File(this.mPicPath)));
            intent.putExtra("com.miui.gallery.extra.photo_items", arrayList);
            intent.putExtra("from_custom_widget", true);
            long j = 0;
            try {
                j = Long.parseLong(this.mPicId);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            intent.putExtra("photo_count", this.mPhotoCount);
            intent.putExtra("photo_id", j);
            intent.putExtra("photo_init_position", this.mPosition);
            startActivity(intent);
        }
        finish();
    }

    /* loaded from: classes2.dex */
    public static class SpecificJob implements ThreadPool.Job<CustomWidgetDBEntity> {
        public final long widgetId;

        public SpecificJob(long j) {
            this.widgetId = j;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // com.miui.gallery.concurrent.ThreadPool.Job
        /* renamed from: run */
        public CustomWidgetDBEntity mo1807run(ThreadPool.JobContext jobContext) {
            DefaultLogger.d("SpecificJob", "---log---SpecificJob findWidgetEntity > %d", Long.valueOf(this.widgetId));
            return CustomWidgetDBManager.getInstance().findWidgetEntity(this.widgetId);
        }
    }

    /* loaded from: classes2.dex */
    public static class SpecificFutureListener implements FutureListener<CustomWidgetDBEntity> {
        public WeakReference<CustomDispatchActivity> reference;

        public SpecificFutureListener(CustomDispatchActivity customDispatchActivity) {
            this.reference = new WeakReference<>(customDispatchActivity);
        }

        @Override // com.miui.gallery.concurrent.FutureListener
        public void onFutureDone(Future<CustomWidgetDBEntity> future) {
            CustomDispatchActivity customDispatchActivity = this.reference.get();
            if (customDispatchActivity == null) {
                return;
            }
            if (customDispatchActivity.mFuture != null && customDispatchActivity.mFuture.isCancelled()) {
                return;
            }
            CustomWidgetDBEntity customWidgetDBEntity = future.get();
            DefaultLogger.d("SpecificJob", "---log---onFutureDone entity >" + customWidgetDBEntity);
            if (customWidgetDBEntity == null) {
                customDispatchActivity.jumpToPicker();
                return;
            }
            DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(customWidgetDBEntity.getPicPath(), IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("CustomDispatchActivity", "onFutureDone"));
            if (documentFile != null && documentFile.exists()) {
                customDispatchActivity.jumpToEditor(customWidgetDBEntity);
                return;
            }
            WidgetInstallManager.setCustomWidgetEmpty(customDispatchActivity, customWidgetDBEntity.getWidgetId(), -1, GalleryWidgetUtils.getWidgetSize(customWidgetDBEntity.getWidgetSize()));
            customDispatchActivity.jumpToPicker();
        }
    }
}
