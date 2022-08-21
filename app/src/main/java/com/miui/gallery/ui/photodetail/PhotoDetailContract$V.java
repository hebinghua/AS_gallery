package com.miui.gallery.ui.photodetail;

import com.miui.gallery.app.base.BaseFragment;
import com.miui.gallery.base_optimization.mvp.presenter.IPresenter;
import com.miui.gallery.ui.DownloadFragment;
import com.miui.gallery.ui.photodetail.viewbean.PhotoDetailViewBean;
import com.miui.gallery.util.BulkDownloadHelper;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public abstract class PhotoDetailContract$V<P extends IPresenter> extends BaseFragment<P> {
    public abstract void bindLocation(String str);

    public abstract void showDetailInfo(boolean z, PhotoDetailViewBean photoDetailViewBean);

    public abstract void showDownLoadFailed();

    public abstract void showDownLoadProgressDialog(ArrayList<BulkDownloadHelper.BulkDownloadItem> arrayList, DownloadFragment.OnDownloadListener onDownloadListener);

    public abstract void showDownLoadSuccess(PhotoDetailViewBean photoDetailViewBean);

    public abstract void showLoadingDialog();

    public abstract void showRenameFail(String str);

    public abstract void showRenameResult(String str);

    public abstract void showUpdateDateTimeResult(String str);
}
