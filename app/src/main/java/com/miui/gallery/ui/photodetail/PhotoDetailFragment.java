package com.miui.gallery.ui.photodetail;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.data.LocationUtil;
import com.miui.gallery.map.utils.MapConfig;
import com.miui.gallery.map.utils.MapInitializerImpl;
import com.miui.gallery.map.utils.MapLibraryLoaderHelper;
import com.miui.gallery.map.utils.OnMapLoadedCallback;
import com.miui.gallery.map.view.IMapContainer;
import com.miui.gallery.map.view.StaticMapContainer;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.DownloadFragment;
import com.miui.gallery.ui.DownloadProgressFragment;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.ui.photodetail.PhotoRenameDialogFragment;
import com.miui.gallery.ui.photodetail.viewbean.PhotoDetailViewBean;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.BulkDownloadHelper;
import com.miui.gallery.util.DialogUtil;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.StringUtils;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.ViewUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.DebounceClickListener;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import miuix.appcompat.app.DatePickerDialog;
import miuix.appcompat.app.ProgressDialog;
import miuix.pickerwidget.widget.DatePicker;

/* loaded from: classes2.dex */
public class PhotoDetailFragment extends PhotoDetailContract$V<PhotoDetailPresenter> {
    public boolean isCanEditPhotoDate;
    public boolean isCanEditPhotoName;
    public boolean isFromLocked;
    public boolean isNeedDownLoadOriginPhoto;
    public View.OnClickListener mClickListener = new DebounceClickListener(1000) { // from class: com.miui.gallery.ui.photodetail.PhotoDetailFragment.1
        {
            PhotoDetailFragment.this = this;
        }

        @Override // com.miui.gallery.widget.DebounceClickListener
        public void onClickConfirmed(View view) {
            if (!((PhotoDetailPresenter) PhotoDetailFragment.this.getPresenter()).isCanClick()) {
                return;
            }
            switch (view.getId()) {
                case R.id.file_info_title /* 2131362399 */:
                    if (!PhotoDetailFragment.this.isCanEditPhotoName) {
                        return;
                    }
                    PhotoDetailFragment.this.showRenameDialog();
                    return;
                case R.id.location_title /* 2131362729 */:
                    if (!((PhotoDetailPresenter) PhotoDetailFragment.this.getPresenter()).isHaveLocation()) {
                        return;
                    }
                    double[] location = ((PhotoDetailPresenter) PhotoDetailFragment.this.getPresenter()).getLocation();
                    if (location != null && !IntentUtil.showOnMap(PhotoDetailFragment.this.getSafeActivity(), location[0], location[1])) {
                        ToastUtils.makeText(PhotoDetailFragment.this.getSafeActivity(), (int) R.string.no_map_app);
                    }
                    SamplingStatHelper.recordCountEvent("photo_detail", "show_on_map");
                    return;
                case R.id.path_title /* 2131363054 */:
                    if (!((PhotoDetailPresenter) PhotoDetailFragment.this.getPresenter()).isHaveLocalPath()) {
                        return;
                    }
                    IntentUtil.jumpToExplore(PhotoDetailFragment.this.getSafeActivity(), BaseFileUtils.getParentFolderPath(((PhotoDetailPresenter) PhotoDetailFragment.this.getPresenter()).getFileLocalPath()));
                    return;
                case R.id.time_item /* 2131363565 */:
                    if (!PhotoDetailFragment.this.isCanEditPhotoDate) {
                        return;
                    }
                    if (((PhotoDetailPresenter) PhotoDetailFragment.this.getPresenter()).isThumbFile() || PhotoDetailFragment.this.isNeedDownLoadOriginPhoto) {
                        PhotoDetailFragment.this.showDownLoadTipDialog();
                        return;
                    } else {
                        PhotoDetailFragment.this.showDateTimePicker();
                        return;
                    }
                default:
                    return;
            }
        }
    };
    public View mContentContainer;
    public View mFileInfoItem;
    public TextView mFileInfoSub;
    public TextView mFileInfoTitle;
    public View mFusionInfoItem;
    public TextView mFusionInfoTitle;
    public BaseDataItem mItem;
    public ProgressDialog mLoadingProgressDialog;
    public TextView mLocation;
    public View mLocationItem;
    public IMapContainer mMap;
    public ViewGroup mMapContainer;
    public View mParamsItem;
    public TextView mPath;
    public View mPathItem;
    public PhotoRenameDialogFragment mPhotoRenameDialogFragment;
    public ProgressBar mProgress;
    public Intent mResultIntent;
    public TextView mScreenshotPackageInfo;
    public View mScreenshotPackageItem;
    public TextView mTakenParamsSub;
    public TextView mTakenParamsTitle;
    public int mTextViewHighColor;
    public View mTimeItem;
    public TextView mTimeSub;
    public TextView mTimeTitle;
    public TextView mTipNoDownload;

    /* renamed from: $r8$lambda$J0KdFk1R-8BitcQZhzMpHnyPAV8 */
    public static /* synthetic */ void m1645$r8$lambda$J0KdFk1R8BitcQZhzMpHnyPAV8(PhotoDetailFragment photoDetailFragment, String str, double[] dArr) {
        photoDetailFragment.lambda$loadMap$2(str, dArr);
    }

    /* renamed from: $r8$lambda$N51vGQJSEGlwBkLzdYI--48v5fc */
    public static /* synthetic */ void m1646$r8$lambda$N51vGQJSEGlwBkLzdYI48v5fc(PhotoDetailFragment photoDetailFragment, String str, double[] dArr, View view) {
        photoDetailFragment.lambda$loadMap$1(str, dArr, view);
    }

    public static /* synthetic */ void $r8$lambda$S4KqRnOqH7GqwHcOH3L4lqHtk60(PhotoDetailFragment photoDetailFragment, boolean z, boolean z2) {
        photoDetailFragment.lambda$downloadOrigin$0(z, z2);
    }

    @Override // com.miui.gallery.base_optimization.mvp.view.Fragment
    public int getLayoutId() {
        return R.layout.photo_detail;
    }

    public static PhotoDetailFragment newInstance() {
        Bundle bundle = new Bundle();
        PhotoDetailFragment photoDetailFragment = new PhotoDetailFragment();
        photoDetailFragment.setArguments(bundle);
        return photoDetailFragment;
    }

    @Override // com.miui.gallery.base_optimization.mvp.view.Fragment
    public void initView(View view, Bundle bundle, View view2) {
        Bundle extras = getSafeActivity().getIntent().getExtras();
        if (extras != null) {
            BaseDataItem baseDataItem = (BaseDataItem) extras.getSerializable("photo_detail_target");
            this.mItem = baseDataItem;
            if (baseDataItem != null) {
                this.isCanEditPhotoDate = extras.getBoolean("photodetail_is_photo_datetime_editable", false);
                this.isCanEditPhotoName = extras.getBoolean("photodetail_is_photo_renamable", false);
                this.isNeedDownLoadOriginPhoto = extras.getBoolean("photo_detail_is_need_download_originphoto");
                this.isFromLocked = extras.getBoolean("StartActivityWhenLocked", false);
                ViewUtils.setRootViewClickable(view2);
                this.mTimeItem = view.findViewById(R.id.time_item);
                this.mTimeTitle = (TextView) view.findViewById(R.id.time_title);
                this.mTimeSub = (TextView) view.findViewById(R.id.time_subtitle);
                this.mFileInfoItem = view.findViewById(R.id.file_info_item);
                this.mFileInfoTitle = (TextView) view.findViewById(R.id.file_info_title);
                this.mTipNoDownload = (TextView) view.findViewById(R.id.tip_no_download);
                this.mFileInfoSub = (TextView) view.findViewById(R.id.file_info_subtitle);
                this.mParamsItem = view.findViewById(R.id.params_item);
                this.mTakenParamsTitle = (TextView) view.findViewById(R.id.params_title);
                this.mTakenParamsSub = (TextView) view.findViewById(R.id.params_subtitle);
                this.mPathItem = view.findViewById(R.id.path_item);
                this.mPath = (TextView) view.findViewById(R.id.path_title);
                this.mLocationItem = view.findViewById(R.id.location_item);
                this.mLocation = (TextView) view.findViewById(R.id.location_title);
                this.mContentContainer = view.findViewById(R.id.content_container);
                this.mProgress = (ProgressBar) view.findViewById(R.id.progress);
                this.mScreenshotPackageItem = view.findViewById(R.id.screenshot_package_info);
                this.mScreenshotPackageInfo = (TextView) view.findViewById(R.id.screenshot_package_name);
                this.mFusionInfoItem = view.findViewById(R.id.fusion_info);
                this.mFusionInfoTitle = (TextView) view.findViewById(R.id.fusion_info_name);
                this.mLocation.setOnClickListener(this.mClickListener);
                this.mTextViewHighColor = getResources().getColor(R.color.highlighted_text);
                this.mMapContainer = (ViewGroup) view.findViewById(R.id.map_container);
                return;
            }
        }
        getSafeActivity().finish();
        DefaultLogger.e("PhotoDetailFragment", "PhotoDetailFragment bundle is null,now finish page");
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        ((PhotoDetailPresenter) getPresenter()).init(this.mItem);
        BaseDataItem baseDataItem = this.mItem;
        if (baseDataItem == null || !baseDataItem.isSecret()) {
            return;
        }
        requireActivity().getWindow().addFlags(8192);
    }

    @Override // androidx.fragment.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public final void showDownLoadTipDialog() {
        if (getSafeActivity() != null && ((PhotoDetailPresenter) getPresenter()).canDownLoad()) {
            DefaultLogger.d("PhotoDetailFragment", "show DownLoad Tip Dialog");
            DialogUtil.showInfoDialog(getSafeActivity(), (int) R.string.editphoto_datetime_needdownload_origin_subtitle, (int) R.string.editphoto_datetime_needdownload_origin_title, (int) R.string.yes, (int) R.string.no, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.photodetail.PhotoDetailFragment.2
                {
                    PhotoDetailFragment.this = this;
                }

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    PhotoDetailFragment.this.downloadOrigin();
                }
            }, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.photodetail.PhotoDetailFragment.3
                {
                    PhotoDetailFragment.this = this;
                }

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    DefaultLogger.d("PhotoDetailFragment", "cancel DownLoad Tip Dialog");
                }
            });
        }
    }

    public final void downloadOrigin() {
        if (getSafeActivity() == null) {
            return;
        }
        if (BaseNetworkUtils.isActiveNetworkMetered()) {
            NetworkConsider.consider(getSafeActivity(), new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.ui.photodetail.PhotoDetailFragment$$ExternalSyntheticLambda2
                @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
                public final void onConfirmed(boolean z, boolean z2) {
                    PhotoDetailFragment.$r8$lambda$S4KqRnOqH7GqwHcOH3L4lqHtk60(PhotoDetailFragment.this, z, z2);
                }
            });
        } else {
            ((PhotoDetailPresenter) getPresenter()).downLoadOrigin();
        }
    }

    public /* synthetic */ void lambda$downloadOrigin$0(boolean z, boolean z2) {
        if (z) {
            DefaultLogger.d("PhotoDetailFragment", "pre DownLoad Origin");
            ((PhotoDetailPresenter) getPresenter()).downLoadOrigin();
            return;
        }
        DefaultLogger.d("PhotoDetailFragment", "cancel DownLoad Origin");
    }

    @Override // com.miui.gallery.ui.photodetail.PhotoDetailContract$V
    public void showDownLoadProgressDialog(ArrayList<BulkDownloadHelper.BulkDownloadItem> arrayList, DownloadFragment.OnDownloadListener onDownloadListener) {
        DefaultLogger.d("PhotoDetailFragment", "show DownLoad Dialog");
        DownloadProgressFragment newInstance = DownloadProgressFragment.newInstance(arrayList);
        newInstance.setOnDownloadListener(onDownloadListener);
        newInstance.showAllowingStateLoss(getFragmentManager(), "DownloadFragment");
    }

    @Override // com.miui.gallery.ui.photodetail.PhotoDetailContract$V
    public void showDownLoadSuccess(PhotoDetailViewBean photoDetailViewBean) {
        DefaultLogger.d("PhotoDetailFragment", "DownLoad %s is Success", photoDetailViewBean.getDisplayFilePath());
        this.isNeedDownLoadOriginPhoto = false;
        bindFileInfo(photoDetailViewBean);
        showDateTimePicker();
        putResult(photoDetailViewBean.getDisplayFilePath(), false);
    }

    @Override // com.miui.gallery.ui.photodetail.PhotoDetailContract$V
    public void showDownLoadFailed() {
        DialogUtil.showInfoDialog(getSafeActivity(), getResources().getString(R.string.download_retry_message), getResources().getString(R.string.download_retry_title), (int) R.string.download_retry_text, 17039360, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.photodetail.PhotoDetailFragment.4
            {
                PhotoDetailFragment.this = this;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                DefaultLogger.d("PhotoDetailFragment", "pre show DownLoad Dialog");
                ((PhotoDetailPresenter) PhotoDetailFragment.this.getPresenter()).downLoadOrigin();
            }
        }, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.photodetail.PhotoDetailFragment.5
            {
                PhotoDetailFragment.this = this;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                DefaultLogger.d("PhotoDetailFragment", "User network status is not good, download failed");
            }
        });
    }

    public final void showRenameDialog() {
        PhotoRenameDialogFragment newInstance = PhotoRenameDialogFragment.newInstance(this.mFileInfoTitle.getText().toString(), ((PhotoDetailPresenter) getPresenter()).getFileLocalPath(), 2, new PhotoRenameDialogFragment.OnIntendToRename() { // from class: com.miui.gallery.ui.photodetail.PhotoDetailFragment.6
            {
                PhotoDetailFragment.this = this;
            }

            @Override // com.miui.gallery.ui.photodetail.PhotoRenameDialogFragment.OnIntendToRename
            public void onIntendToRename(String str) {
                ((PhotoDetailPresenter) PhotoDetailFragment.this.getPresenter()).renamePhoto(str);
            }
        });
        this.mPhotoRenameDialogFragment = newInstance;
        newInstance.showAllowingStateLoss(getFragmentManager(), "PhotoRenameDialogFragment");
    }

    public final void showDateTimePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getSafeActivity(), new DatePickerDialog.OnDateSetListener() { // from class: com.miui.gallery.ui.photodetail.PhotoDetailFragment.7
            {
                PhotoDetailFragment.this = this;
            }

            @Override // miuix.appcompat.app.DatePickerDialog.OnDateSetListener
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                ((PhotoDetailPresenter) PhotoDetailFragment.this.getPresenter()).updatePhotoDateTime(i, i2, i3);
            }
        }, ((PhotoDetailPresenter) getPresenter()).getStartYear(), ((PhotoDetailPresenter) getPresenter()).getStartMonth(), ((PhotoDetailPresenter) getPresenter()).getStartDay());
        datePickerDialog.setLunarMode(false);
        datePickerDialog.setTitle(getResources().getString(R.string.datetimepicker_dialog_title));
        datePickerDialog.getDatePicker().setMinDate(-28800L);
        datePickerDialog.getDatePicker().setMaxDate(((PhotoDetailPresenter) getPresenter()).getDatePickerMaxTime());
        datePickerDialog.show();
    }

    @Override // com.miui.gallery.ui.photodetail.PhotoDetailContract$V
    public void showDetailInfo(boolean z, PhotoDetailViewBean photoDetailViewBean) {
        if (this.isCanEditPhotoDate) {
            this.isCanEditPhotoDate = z;
        }
        bindDetail(photoDetailViewBean);
    }

    public final void bindDetail(PhotoDetailViewBean photoDetailViewBean) {
        try {
            bindTime(photoDetailViewBean);
            bindFileInfo(photoDetailViewBean);
            bindNotDownloadTip(photoDetailViewBean);
            bindTakenParams(photoDetailViewBean);
            bindPath(photoDetailViewBean);
            if (photoDetailViewBean.isHaveLocation()) {
                ((PhotoDetailPresenter) getPresenter()).requestLocation();
                bindMap(photoDetailViewBean);
            }
            bindScreenshotPackageInfo(photoDetailViewBean);
            bindSmartFusion(photoDetailViewBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.mContentContainer.setVisibility(0);
        this.mProgress.setVisibility(8);
    }

    public final void bindSmartFusion(PhotoDetailViewBean photoDetailViewBean) {
        View view;
        if (photoDetailViewBean == null || (view = this.mFusionInfoItem) == null) {
            return;
        }
        setItemVisible(view, photoDetailViewBean.isSmartFusion());
    }

    public final void bindTime(PhotoDetailViewBean photoDetailViewBean) {
        if (photoDetailViewBean.isHaveDateTime()) {
            setTimeToView(photoDetailViewBean.getDateText(), photoDetailViewBean.getTimeText());
            setItemVisible(this.mTimeItem, true);
            if (!this.isCanEditPhotoDate) {
                return;
            }
            setTextColor(this.mTimeTitle, this.mTextViewHighColor);
            setTextColor(this.mTimeSub, this.mTextViewHighColor);
            this.mTimeItem.setOnClickListener(this.mClickListener);
            return;
        }
        setItemVisible(this.mTimeItem, false);
    }

    public final void setTimeToView(String str, String str2) {
        this.mTimeTitle.setText(str);
        this.mTimeSub.setText(str2);
    }

    public final void bindFileInfo(PhotoDetailViewBean photoDetailViewBean) {
        boolean isHaveFileInfo = photoDetailViewBean.isHaveFileInfo();
        if (isHaveFileInfo) {
            this.mFileInfoTitle.setText(photoDetailViewBean.getFileName());
        }
        if (isHaveFileInfo && ((PhotoDetailPresenter) getPresenter()).isHaveLocalPath() && this.isCanEditPhotoName) {
            setTextColor(this.mFileInfoTitle, this.mTextViewHighColor);
            this.mFileInfoTitle.setOnClickListener(this.mClickListener);
        }
        String fileSizeText = photoDetailViewBean.getFileSizeText();
        if (fileSizeText.length() > 0) {
            this.mFileInfoSub.setText(fileSizeText);
            setItemVisible(this.mFileInfoSub, true);
            isHaveFileInfo = true;
        } else {
            setItemVisible(this.mFileInfoSub, false);
        }
        setItemVisible(this.mFileInfoItem, isHaveFileInfo);
    }

    public final void bindNotDownloadTip(PhotoDetailViewBean photoDetailViewBean) {
        if (photoDetailViewBean.isHaveDownLoadTip()) {
            this.mTipNoDownload.setText(photoDetailViewBean.getNotDownLoad());
            setItemVisible(this.mTipNoDownload, true);
            return;
        }
        setItemVisible(this.mTipNoDownload, false);
    }

    public final void bindTakenParams(PhotoDetailViewBean photoDetailViewBean) {
        if (!StringUtils.isEmpty(photoDetailViewBean.getPhoneModel())) {
            this.mTakenParamsTitle.setText(photoDetailViewBean.getPhoneModel());
            String takenParam = photoDetailViewBean.getTakenParam();
            if (takenParam.length() > 0) {
                this.mTakenParamsSub.setText(takenParam);
                setItemVisible(this.mTakenParamsSub, true);
                return;
            }
            setItemVisible(this.mTakenParamsSub, false);
            return;
        }
        setItemVisible(this.mParamsItem, false);
    }

    public final void bindPath(PhotoDetailViewBean photoDetailViewBean) {
        View.OnClickListener onClickListener;
        int color = getResources().getColor(R.color.info_title_color);
        if (photoDetailViewBean.isHaveFilePath()) {
            this.mPath.setText(photoDetailViewBean.getDisplayFilePath());
            setItemVisible(this.mPathItem, true);
            if (photoDetailViewBean.isFile()) {
                onClickListener = this.mClickListener;
                color = this.mTextViewHighColor;
                this.mPath.setOnClickListener(onClickListener);
                setTextColor(this.mPath, color);
            }
        } else {
            setItemVisible(this.mPathItem, false);
        }
        onClickListener = null;
        this.mPath.setOnClickListener(onClickListener);
        setTextColor(this.mPath, color);
    }

    public final void setTextColor(TextView textView, int i) {
        if (textView == null) {
            return;
        }
        textView.setTextColor(i);
    }

    @Override // com.miui.gallery.ui.photodetail.PhotoDetailContract$V
    public void bindLocation(String str) {
        if (!StringUtils.isEmpty(str)) {
            this.mLocation.setText(str);
            setTextColor(this.mLocation, this.mTextViewHighColor);
            setItemVisible(this.mLocationItem, true);
            return;
        }
        setItemVisible(this.mLocationItem, false);
    }

    public void loadMap(final String str, final String str2, final double[] dArr) {
        if (TextUtils.isEmpty(str2) || TextUtils.isEmpty(str) || !MapInitializerImpl.checkInitialized()) {
            return;
        }
        this.mMapContainer.removeAllViews();
        this.mMapContainer.setVisibility(4);
        IMapContainer iMapContainer = this.mMap;
        if (iMapContainer != null && iMapContainer.hasMapLoaded()) {
            this.mMap.clearOverlays();
            this.mMap.addMarkerAndFocus(str, 1, dArr[0], dArr[1], MapConfig.FOCUS_ZOOM_LEVEL.floatValue());
        } else {
            IMapContainer newInstance = StaticMapContainer.newInstance(requireContext(), MapConfig.FOCUS_ZOOM_LEVEL, dArr[0], dArr[1]);
            this.mMap = newInstance;
            if (!this.isFromLocked) {
                newInstance.getView().setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.photodetail.PhotoDetailFragment$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        PhotoDetailFragment.m1646$r8$lambda$N51vGQJSEGlwBkLzdYI48v5fc(PhotoDetailFragment.this, str2, dArr, view);
                    }
                });
            }
            this.mMap.setMapLoadedCallback(new OnMapLoadedCallback() { // from class: com.miui.gallery.ui.photodetail.PhotoDetailFragment$$ExternalSyntheticLambda1
                @Override // com.miui.gallery.map.utils.OnMapLoadedCallback
                public final void onMapLoaded() {
                    PhotoDetailFragment.m1645$r8$lambda$J0KdFk1R8BitcQZhzMpHnyPAV8(PhotoDetailFragment.this, str, dArr);
                }
            });
            this.mMapContainer.setVisibility(0);
        }
        this.mMapContainer.addView(this.mMap.getView(), new FrameLayout.LayoutParams(-1, -1));
    }

    public /* synthetic */ void lambda$loadMap$1(String str, double[] dArr, View view) {
        TrackController.trackClick("403.11.7.1.15339");
        if (MapLibraryLoaderHelper.getInstance().checkAbleOrLoaded(requireActivity(), false)) {
            IntentUtil.checkAndGoToMapAlbum(getContext(), str, dArr, false);
        }
    }

    public /* synthetic */ void lambda$loadMap$2(String str, double[] dArr) {
        this.mMap.addMarker(str, 1, dArr[0], dArr[1]);
    }

    public final void bindMap(PhotoDetailViewBean photoDetailViewBean) {
        double[] location = photoDetailViewBean.getLocation();
        if (this.mItem.isSecret() || !AgreementsUtils.isNetworkingAgreementAccepted() || getView() == null || !MapInitializerImpl.checkMapAvailable() || !MapLibraryLoaderHelper.getInstance().checkAbleOrLoaded(requireActivity(), false) || !LocationUtil.isValidateCoordinate(location[0], location[1])) {
            return;
        }
        loadMap(this.mItem.getPathDisplayBetter(), this.mItem.getTitle(), location);
    }

    public final void bindScreenshotPackageInfo(PhotoDetailViewBean photoDetailViewBean) {
        if (photoDetailViewBean.isHaveCacheLocation() && this.mItem != null) {
            String fileName = photoDetailViewBean.getFileName();
            if (this.mItem.isScreenshot() && fileName != null) {
                this.mScreenshotPackageInfo.setText(photoDetailViewBean.getCacheLocation());
                setItemVisible(this.mScreenshotPackageItem, true);
                return;
            }
            setItemVisible(this.mScreenshotPackageItem, false);
            return;
        }
        setItemVisible(this.mScreenshotPackageItem, false);
    }

    public final void setItemVisible(View view, boolean z) {
        if (z) {
            view.setVisibility(0);
        } else {
            view.setVisibility(8);
        }
    }

    @Override // com.miui.gallery.app.base.BaseFragment
    public boolean onBackPressed() {
        finishActivity(-1);
        return false;
    }

    public final void finishActivity(int i) {
        if (getActivity() == null) {
            return;
        }
        if (this.mResultIntent != null) {
            getActivity().setResult(i, this.mResultIntent);
        } else {
            getActivity().setResult(i);
        }
        getActivity().finish();
    }

    @Override // com.miui.gallery.ui.photodetail.PhotoDetailContract$V
    public void showUpdateDateTimeResult(String str) {
        ProgressDialog progressDialog = this.mLoadingProgressDialog;
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        if (str != null) {
            putResult(str, false);
            ToastUtils.makeText(getSafeActivity(), (int) R.string.datetimepicker_updatetime_success);
            onBackPressed();
            return;
        }
        ToastUtils.makeText(getSafeActivity(), (int) R.string.datetimepicker_updatetime_failed);
    }

    @Override // com.miui.gallery.ui.photodetail.PhotoDetailContract$V
    public void showRenameResult(String str) {
        if (str != null) {
            Path path = Paths.get(Paths.get(this.mItem.getPathDisplayBetter(), new String[0]).getParent().toString(), str);
            putResult(path.toString(), true);
            this.mFileInfoTitle.setText(str);
            this.mPath.setText(path.toString());
            ToastUtils.makeText(getSafeActivity(), (int) R.string.photodetail_rename_success);
            PhotoRenameDialogFragment photoRenameDialogFragment = this.mPhotoRenameDialogFragment;
            if (photoRenameDialogFragment == null) {
                return;
            }
            photoRenameDialogFragment.dismissSafely();
        }
    }

    @Override // com.miui.gallery.ui.photodetail.PhotoDetailContract$V
    public void showRenameFail(String str) {
        if (str != null && !str.isEmpty()) {
            ToastUtils.makeText(getSafeActivity(), str);
            return;
        }
        ToastUtils.makeText(getSafeActivity(), (int) R.string.photodetail_rename_failed);
        PhotoRenameDialogFragment photoRenameDialogFragment = this.mPhotoRenameDialogFragment;
        if (photoRenameDialogFragment == null) {
            return;
        }
        photoRenameDialogFragment.dismissSafely();
    }

    public final void putResult(String str, boolean z) {
        if (this.mResultIntent == null) {
            this.mResultIntent = new Intent();
        }
        this.mResultIntent.putExtra("photo_focused_path", str);
        this.mResultIntent.putExtra("photodetail_is_photo_renamed", z);
    }

    @Override // com.miui.gallery.ui.photodetail.PhotoDetailContract$V
    public void showLoadingDialog() {
        ProgressDialog progressDialog = new ProgressDialog(getSafeActivity());
        this.mLoadingProgressDialog = progressDialog;
        progressDialog.setMessage(getString(R.string.photo_editor_saving));
        this.mLoadingProgressDialog.setCancelable(false);
        this.mLoadingProgressDialog.setCanceledOnTouchOutside(false);
        this.mLoadingProgressDialog.setIndeterminate(true);
        this.mLoadingProgressDialog.show();
    }

    @Override // com.miui.gallery.app.base.BaseFragment, com.miui.gallery.base_optimization.mvp.view.Fragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        IMapContainer iMapContainer = this.mMap;
        if (iMapContainer != null) {
            iMapContainer.onResume();
        }
    }

    @Override // com.miui.gallery.app.base.BaseFragment, com.miui.gallery.base_optimization.mvp.view.Fragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        IMapContainer iMapContainer = this.mMap;
        if (iMapContainer != null) {
            iMapContainer.onPause();
        }
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        IMapContainer iMapContainer = this.mMap;
        if (iMapContainer != null) {
            iMapContainer.onDestroy();
            this.mMap.setMapLoadedCallback(null);
        }
    }
}
