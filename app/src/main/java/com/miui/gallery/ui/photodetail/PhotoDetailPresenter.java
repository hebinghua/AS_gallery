package com.miui.gallery.ui.photodetail;

import android.location.Address;
import com.miui.gallery.R;
import com.miui.gallery.card.scenario.DateUtils;
import com.miui.gallery.data.LocationManager;
import com.miui.gallery.data.LocationUtil;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.CloudItem;
import com.miui.gallery.model.datalayer.config.ModelConfig;
import com.miui.gallery.model.datalayer.config.ModelManager;
import com.miui.gallery.model.datalayer.repository.AbstractCloudRepository;
import com.miui.gallery.model.datalayer.repository.ILocationRepository;
import com.miui.gallery.model.dto.PageResults;
import com.miui.gallery.provider.cache.ShareMediaManager;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.ui.DownloadFragment;
import com.miui.gallery.ui.photodetail.usecase.EditPhotoDateTime;
import com.miui.gallery.ui.photodetail.usecase.EditPhotoDateTimeAndRecord;
import com.miui.gallery.ui.photodetail.usecase.GetLocationInfo;
import com.miui.gallery.ui.photodetail.usecase.GetPhotoDetailInfo;
import com.miui.gallery.ui.photodetail.usecase.RenamePhotoCase;
import com.miui.gallery.ui.photodetail.viewbean.PhotoDetailViewBean;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.BulkDownloadHelper;
import com.miui.gallery.util.RxUtils;
import com.miui.gallery.util.StaticContext;
import com.miui.gallery.util.StringUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.subscribers.DisposableSubscriber;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import miuix.core.util.Pools;

/* loaded from: classes2.dex */
public class PhotoDetailPresenter extends PhotoDetailContract$P {
    public static final String TAG = "PhotoDetailPresenter";
    private boolean isFromInternal;
    private BaseDataItem mBaseDataItem;
    private Calendar mDateCalendar;
    private DownloadFragment.OnDownloadListener mDownLoadListener;
    private EditPhotoDateTime mEditPhotoDateTime;
    private String mFilePath;
    private GetLocationInfo mGetLocationInfo;
    private GetPhotoDetailInfo mGetPhotoDetailInfo;
    private PageResults<Address> mLastAddressResult;
    private long mMaxDateTime;
    private PhotoDetailViewBean mPhotoDetailInfo;
    private RenamePhotoCase mRenamePhoto;
    private Calendar mTempDateCalendar;
    private int[] mUserSelectDate;
    private long mUserSelectDateTime;
    private final int YEAR = 0;
    private final int MONTH = 1;
    private final int DAY = 2;

    @Override // com.miui.gallery.ui.photodetail.PhotoDetailContract$P
    public void init(final BaseDataItem baseDataItem) {
        this.mBaseDataItem = baseDataItem;
        GetPhotoDetailInfo getPhotoDetailInfo = new GetPhotoDetailInfo((AbstractCloudRepository) ModelManager.getInstance().getModel(ModelConfig.ModelNames.CLOUD_REPOSITORY));
        this.mGetPhotoDetailInfo = getPhotoDetailInfo;
        getPhotoDetailInfo.execute(new DisposableSubscriber<PhotoDetailViewBean>() { // from class: com.miui.gallery.ui.photodetail.PhotoDetailPresenter.1
            @Override // org.reactivestreams.Subscriber
            public void onComplete() {
            }

            @Override // org.reactivestreams.Subscriber
            public void onNext(PhotoDetailViewBean photoDetailViewBean) {
                boolean z;
                PhotoDetailPresenter.this.mPhotoDetailInfo = photoDetailViewBean;
                PhotoDetailPresenter.this.mPhotoDetailInfo.setId(baseDataItem.getKey());
                boolean z2 = false;
                if (PhotoDetailPresenter.this.mBaseDataItem instanceof CloudItem) {
                    List<BaseDataItem> burstGroup = ((CloudItem) PhotoDetailPresenter.this.mBaseDataItem).getBurstGroup();
                    z = burstGroup == null || burstGroup.isEmpty();
                    PhotoDetailPresenter.this.isFromInternal = true;
                } else {
                    z = true;
                }
                PhotoDetailContract$V view = PhotoDetailPresenter.this.getView();
                if (PhotoDetailUtils.isMimeTypeSupportEditDateTime(PhotoDetailPresenter.this.mBaseDataItem.getMimeType()) && z) {
                    z2 = true;
                }
                view.showDetailInfo(z2, photoDetailViewBean);
            }

            @Override // org.reactivestreams.Subscriber
            public void onError(Throwable th) {
                DefaultLogger.e("getPhotoDetailInfo error %s", th);
            }
        }, baseDataItem);
    }

    @Override // com.miui.gallery.ui.photodetail.PhotoDetailContract$P
    public PhotoDetailViewBean getDetailInfo() {
        return this.mPhotoDetailInfo;
    }

    @Override // com.miui.gallery.ui.photodetail.PhotoDetailContract$P
    public void requestLocation() {
        if (this.mBaseDataItem == null) {
            return;
        }
        if (this.mGetLocationInfo == null) {
            this.mGetLocationInfo = new GetLocationInfo((ILocationRepository) ModelManager.getInstance().getModel(ModelConfig.ModelNames.LOCATION_REPOSITORY));
        }
        double[] location = this.mPhotoDetailInfo.getLocation();
        BaseDataItem baseDataItem = this.mBaseDataItem;
        GetLocationInfo.RequestBean requestBean = new GetLocationInfo.RequestBean((!(baseDataItem instanceof CloudItem) || ShareMediaManager.isOtherShareMediaId(baseDataItem.getKey())) ? 0L : this.mBaseDataItem.getKey(), location[0], location[1]);
        final String locationText = this.mPhotoDetailInfo.getLocationText();
        if (!StringUtils.isEmpty(locationText)) {
            DefaultLogger.fd(TAG, "request location: %s", locationText);
            this.mGetLocationInfo.execute(new DisposableSubscriber<PageResults<Address>>() { // from class: com.miui.gallery.ui.photodetail.PhotoDetailPresenter.2
                @Override // org.reactivestreams.Subscriber
                public void onComplete() {
                }

                @Override // org.reactivestreams.Subscriber
                public void onNext(PageResults<Address> pageResults) {
                    Address result = pageResults.getResult();
                    if (LocationUtil.isInvalidAddress(result)) {
                        DefaultLogger.fd(PhotoDetailPresenter.TAG, "request location is failed,address is invalid,source type:%s", Integer.valueOf(pageResults.getFromType()));
                        if (PhotoDetailPresenter.this.mLastAddressResult == null || LocationUtil.isInvalidAddress((Address) PhotoDetailPresenter.this.mLastAddressResult.getResult())) {
                            PhotoDetailPresenter.this.getView().bindLocation(locationText);
                        }
                    } else {
                        DefaultLogger.fd(PhotoDetailPresenter.TAG, "request location success: %s ,source type:%s", result, Integer.valueOf(pageResults.getFromType()));
                        PhotoDetailPresenter.this.parseAddress(result);
                    }
                    PhotoDetailPresenter.this.mLastAddressResult = pageResults;
                }

                @Override // org.reactivestreams.Subscriber
                public void onError(Throwable th) {
                    DefaultLogger.e(PhotoDetailPresenter.TAG, th.getMessage());
                }
            }, requestBean);
            return;
        }
        DefaultLogger.fd(TAG, "file path: %s no location", getFileLocalPath());
        getView().bindLocation(null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void parseAddress(Address address) {
        if (address != null) {
            addDispose(Flowable.just(address).map(new Function<Address, String>() { // from class: com.miui.gallery.ui.photodetail.PhotoDetailPresenter.4
                @Override // io.reactivex.functions.Function
                /* renamed from: apply  reason: avoid collision after fix types in other method */
                public String mo2564apply(Address address2) throws Exception {
                    StringBuilder acquire = Pools.getStringBuilderPool().acquire();
                    String addressLine = address2.getAddressLine(0);
                    boolean filterName = LocationManager.filterName(address2.getCountryName());
                    if (StringUtils.isEmpty(addressLine) || filterName) {
                        String[] strArr = new String[9];
                        strArr[0] = address2.getAdminArea();
                        strArr[1] = address2.getSubAdminArea();
                        strArr[2] = address2.getLocality();
                        strArr[3] = address2.getSubLocality();
                        strArr[4] = address2.getThoroughfare();
                        strArr[5] = address2.getSubThoroughfare();
                        strArr[6] = address2.getPremises();
                        String str = "";
                        strArr[7] = filterName ? str : address2.getPostalCode();
                        if (!filterName) {
                            str = address2.getCountryName();
                        }
                        strArr[8] = str;
                        for (int i = 0; i < 9; i++) {
                            if (!StringUtils.isEmpty(strArr[i])) {
                                if (acquire.length() > 0) {
                                    acquire.append(", ");
                                }
                                acquire.append(strArr[i]);
                            }
                        }
                    } else {
                        acquire.append(addressLine);
                    }
                    String sb = acquire.toString();
                    Pools.getStringBuilderPool().release(acquire);
                    return sb;
                }
            }).compose(RxUtils.ioAndMainThread()).subscribe(new Consumer<String>() { // from class: com.miui.gallery.ui.photodetail.PhotoDetailPresenter.3
                @Override // io.reactivex.functions.Consumer
                public void accept(String str) throws Exception {
                    PhotoDetailPresenter.this.getView().bindLocation(str);
                }
            }));
        }
    }

    @Override // com.miui.gallery.ui.photodetail.PhotoDetailContract$P
    public boolean isCanClick() {
        return getDetailInfo() != null;
    }

    @Override // com.miui.gallery.ui.photodetail.PhotoDetailContract$P
    public void updatePhotoDateTime(int i, int i2, int i3) {
        if (this.mBaseDataItem == null) {
            return;
        }
        if (this.mTempDateCalendar == null) {
            this.mTempDateCalendar = Calendar.getInstance();
        }
        this.mTempDateCalendar.setTimeInMillis(this.mBaseDataItem.getCreateTime());
        this.mTempDateCalendar.set(1, i);
        this.mTempDateCalendar.set(2, i2);
        this.mTempDateCalendar.set(5, i3);
        long timeInMillis = this.mTempDateCalendar.getTimeInMillis();
        if (StringUtils.isEmpty(this.mBaseDataItem.getOriginalPath()) || DateUtils.isSameDay(timeInMillis, this.mUserSelectDateTime)) {
            return;
        }
        SamplingStatHelper.recordCountEvent("photo_detail", "click_photo_datetimepicker_confirm_button");
        if (this.mEditPhotoDateTime == null) {
            this.mEditPhotoDateTime = new EditPhotoDateTimeAndRecord((AbstractCloudRepository) ModelManager.getInstance().getModel(ModelConfig.ModelNames.CLOUD_REPOSITORY), this.mBaseDataItem.getLocalGroupId(), this.mBaseDataItem.getCreateTime());
            this.mDateCalendar = Calendar.getInstance();
        }
        this.mDateCalendar.setTimeInMillis(System.currentTimeMillis());
        final long j = timeInMillis + this.mDateCalendar.get(14);
        EditPhotoDateTime.RequestBean requestBean = new EditPhotoDateTime.RequestBean(j, this.mBaseDataItem.getKey());
        requestBean.setFavorite(this.mBaseDataItem.queryFavoriteInfo(false).isFavorite());
        this.mEditPhotoDateTime.execute(new DisposableSubscriber<String>() { // from class: com.miui.gallery.ui.photodetail.PhotoDetailPresenter.5
            @Override // org.reactivestreams.Subscriber
            public void onComplete() {
            }

            @Override // io.reactivex.subscribers.DisposableSubscriber
            public void onStart() {
                super.onStart();
                PhotoDetailPresenter.this.getView().showLoadingDialog();
            }

            @Override // org.reactivestreams.Subscriber
            public void onNext(String str) {
                PhotoDetailPresenter.this.dispatchUpdateDateTimeResult(j, str);
            }

            @Override // org.reactivestreams.Subscriber
            public void onError(Throwable th) {
                PhotoDetailPresenter.this.getView().showUpdateDateTimeResult(null);
                if (th instanceof StoragePermissionMissingException) {
                    ((StoragePermissionMissingException) th).offer(PhotoDetailPresenter.this.getView().getSafeActivity());
                }
            }
        }, requestBean);
    }

    @Override // com.miui.gallery.ui.photodetail.PhotoDetailContract$P
    public void renamePhoto(String str) {
        RenamePhotoCase.RequestBean requestBean;
        if (this.mBaseDataItem == null) {
            return;
        }
        if (this.mRenamePhoto == null) {
            this.mRenamePhoto = new RenamePhotoCase((AbstractCloudRepository) ModelManager.getInstance().getModel(ModelConfig.ModelNames.CLOUD_REPOSITORY));
            this.mFilePath = this.mBaseDataItem.getPathDisplayBetter();
        }
        if (this.isFromInternal) {
            requestBean = new RenamePhotoCase.RequestBean(this.mFilePath, str, this.mBaseDataItem.getKey());
        } else {
            requestBean = new RenamePhotoCase.RequestBean(this.mFilePath, str, -1L);
        }
        this.mRenamePhoto.execute(new DisposableSubscriber<String>() { // from class: com.miui.gallery.ui.photodetail.PhotoDetailPresenter.6
            @Override // org.reactivestreams.Subscriber
            public void onComplete() {
            }

            @Override // io.reactivex.subscribers.DisposableSubscriber
            public void onStart() {
                super.onStart();
            }

            @Override // org.reactivestreams.Subscriber
            public void onNext(String str2) {
                PhotoDetailPresenter.this.mFilePath = Paths.get(Paths.get(PhotoDetailPresenter.this.mBaseDataItem.getPathDisplayBetter(), new String[0]).getParent().toString(), str2).toString();
                PhotoDetailPresenter.this.getView().showRenameResult(str2);
            }

            @Override // org.reactivestreams.Subscriber
            public void onError(Throwable th) {
                if (th instanceof FileAlreadyExistsException) {
                    PhotoDetailPresenter.this.getView().showRenameFail(PhotoDetailPresenter.this.getView().getResources().getString(R.string.photodetail_failed_already_exist));
                } else if (th instanceof StoragePermissionMissingException) {
                    PhotoDetailPresenter.this.getView().showRenameFail(null);
                    ((StoragePermissionMissingException) th).offer(PhotoDetailPresenter.this.getView().getSafeActivity());
                } else {
                    PhotoDetailPresenter.this.getView().showRenameFail(null);
                }
            }
        }, requestBean);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void dispatchUpdateDateTimeResult(long j, String str) {
        this.mUserSelectDateTime = j;
        this.mDateCalendar.setTimeInMillis(j);
        this.mDateCalendar.set(14, 0);
        getView().showUpdateDateTimeResult(str);
    }

    @Override // com.miui.gallery.ui.photodetail.PhotoDetailContract$P
    public int getStartYear() {
        initDateIfNeed();
        return this.mUserSelectDate[0];
    }

    @Override // com.miui.gallery.ui.photodetail.PhotoDetailContract$P
    public int getStartMonth() {
        initDateIfNeed();
        return this.mUserSelectDate[1];
    }

    @Override // com.miui.gallery.ui.photodetail.PhotoDetailContract$P
    public int getStartDay() {
        initDateIfNeed();
        return this.mUserSelectDate[2];
    }

    private void initDateIfNeed() {
        int[] iArr;
        if (this.mUserSelectDateTime == 0 || (iArr = this.mUserSelectDate) == null || iArr.length == 0) {
            Calendar calendar = Calendar.getInstance();
            if (getDetailInfo().isHaveDateTime()) {
                long dateTime = getDetailInfo().getDateTime();
                this.mUserSelectDateTime = dateTime;
                calendar.setTimeInMillis(dateTime);
                this.mUserSelectDate = r1;
                int[] iArr2 = {calendar.get(1)};
                this.mUserSelectDate[1] = calendar.get(2);
                this.mUserSelectDate[2] = calendar.get(5);
                return;
            }
            calendar.setTimeInMillis(System.currentTimeMillis());
            this.mUserSelectDate = r1;
            int[] iArr3 = {calendar.get(1)};
            this.mUserSelectDate[1] = calendar.get(2);
            this.mUserSelectDate[2] = calendar.get(5);
        }
    }

    @Override // com.miui.gallery.ui.photodetail.PhotoDetailContract$P
    public long getDatePickerMaxTime() {
        if (getDetailInfo().isHaveDateTime()) {
            if (this.mMaxDateTime == 0) {
                Calendar calendar = Calendar.getInstance();
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTimeInMillis(getDetailInfo().getDateTime());
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(12, calendar2.get(12));
                this.mMaxDateTime = calendar.getTimeInMillis();
            }
            return this.mMaxDateTime;
        }
        return System.currentTimeMillis();
    }

    @Override // com.miui.gallery.ui.photodetail.PhotoDetailContract$P
    public boolean isHaveLocation() {
        if (getDetailInfo() == null) {
            return false;
        }
        return getDetailInfo().isHaveLocation();
    }

    @Override // com.miui.gallery.ui.photodetail.PhotoDetailContract$P
    public double[] getLocation() {
        if (getDetailInfo() == null) {
            return null;
        }
        return getDetailInfo().getLocation();
    }

    @Override // com.miui.gallery.ui.photodetail.PhotoDetailContract$P
    public boolean isHaveLocalPath() {
        BaseDataItem baseDataItem = this.mBaseDataItem;
        if (baseDataItem == null) {
            return false;
        }
        return !StringUtils.isEmpty(baseDataItem.getOriginalPath()) || !StringUtils.isEmpty(this.mBaseDataItem.getThumnailPath());
    }

    @Override // com.miui.gallery.ui.photodetail.PhotoDetailContract$P
    public String getFileLocalPath() {
        BaseDataItem baseDataItem = this.mBaseDataItem;
        if (baseDataItem == null) {
            return null;
        }
        return StringUtils.isEmpty(baseDataItem.getOriginalPath()) ? this.mBaseDataItem.getThumnailPath() : this.mBaseDataItem.getOriginalPath();
    }

    @Override // com.miui.gallery.ui.photodetail.PhotoDetailContract$P
    public boolean canDownLoad() {
        return this.mBaseDataItem != null;
    }

    @Override // com.miui.gallery.ui.photodetail.PhotoDetailContract$P
    public void downLoadOrigin() {
        BulkDownloadHelper.BulkDownloadItem bulkDownloadItem;
        if (this.mDownLoadListener == null) {
            this.mDownLoadListener = new DownloadFragment.OnDownloadListener() { // from class: com.miui.gallery.ui.photodetail.PhotoDetailPresenter.7
                @Override // com.miui.gallery.ui.DownloadFragment.OnDownloadListener
                public void onCanceled() {
                }

                @Override // com.miui.gallery.ui.DownloadFragment.OnDownloadListener
                public void onDownloadComplete(List<BulkDownloadHelper.BulkDownloadItem> list, List<BulkDownloadHelper.BulkDownloadItem> list2) {
                    if (list != null && list.size() >= 1) {
                        PhotoDetailPresenter.this.mBaseDataItem.setFilePath(list.get(0).getDownloadPath());
                        PhotoDetailPresenter.this.mPhotoDetailInfo.setFileSize(PhotoDetailPresenter.this.mBaseDataItem.getSize());
                        PhotoDetailPresenter.this.getView().showDownLoadSuccess(PhotoDetailPresenter.this.mPhotoDetailInfo);
                        return;
                    }
                    PhotoDetailPresenter.this.getView().showDownLoadFailed();
                }
            };
        }
        if (BaseNetworkUtils.isActiveNetworkMetered()) {
            bulkDownloadItem = new BulkDownloadHelper.BulkDownloadItem(this.mBaseDataItem.getDownloadUri(), DownloadType.ORIGIN_FORCE, this.mBaseDataItem.getSize());
        } else {
            bulkDownloadItem = new BulkDownloadHelper.BulkDownloadItem(this.mBaseDataItem.getDownloadUri(), DownloadType.ORIGIN, this.mBaseDataItem.getSize());
        }
        ArrayList<BulkDownloadHelper.BulkDownloadItem> arrayList = new ArrayList<>();
        arrayList.add(bulkDownloadItem);
        getView().showDownLoadProgressDialog(arrayList, this.mDownLoadListener);
    }

    @Override // com.miui.gallery.ui.photodetail.PhotoDetailContract$P
    public boolean isThumbFile() {
        BaseDataItem baseDataItem = this.mBaseDataItem;
        return baseDataItem != null && StringUtils.isEmpty(baseDataItem.getOriginalPath()) && StringUtils.isEmpty(this.mBaseDataItem.getThumnailPath());
    }

    @Override // com.miui.gallery.ui.photodetail.PhotoDetailContract$P
    public String formatSelectedDate(int i, int i2, int i3) {
        if (this.mTempDateCalendar == null) {
            this.mTempDateCalendar = Calendar.getInstance();
        }
        this.mTempDateCalendar.set(1, i);
        this.mTempDateCalendar.set(2, i2);
        this.mTempDateCalendar.set(5, i3);
        return miuix.pickerwidget.date.DateUtils.formatDateTime(StaticContext.sGetAndroidContext(), this.mTempDateCalendar.getTimeInMillis(), 896);
    }

    @Override // com.miui.gallery.base_optimization.mvp.presenter.BasePresenter
    public void onDestroy() {
        super.onDestroy();
        GetLocationInfo getLocationInfo = this.mGetLocationInfo;
        if (getLocationInfo != null) {
            getLocationInfo.dispose();
        }
        EditPhotoDateTime editPhotoDateTime = this.mEditPhotoDateTime;
        if (editPhotoDateTime != null) {
            editPhotoDateTime.dispose();
        }
        RenamePhotoCase renamePhotoCase = this.mRenamePhoto;
        if (renamePhotoCase != null) {
            renamePhotoCase.dispose();
        }
        GetPhotoDetailInfo getPhotoDetailInfo = this.mGetPhotoDetailInfo;
        if (getPhotoDetailInfo != null) {
            getPhotoDetailInfo.dispose();
        }
        this.mTempDateCalendar = null;
        this.mDateCalendar = null;
    }
}
