package com.miui.gallery.ui.album.main.usecase;

import com.miui.gallery.base_optimization.clean.HotUseCase;
import com.miui.gallery.cleaner.ScanResult;
import com.miui.gallery.cleaner.ScannerManager;
import com.miui.gallery.cloudcontrol.CloudControlStrategyHelper;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.ui.album.common.AlbumTabToolItemBean;
import com.miui.gallery.util.StringUtils;
import com.miui.gallery.util.thread.RxGalleryExecutors;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;

/* loaded from: classes2.dex */
public class ScanCleanerCase extends HotUseCase<AlbumTabToolItemBean, Void> {
    public ScannerManager.ScanObserver mScanObserver;
    public ScannerManager mScannerManager;

    public ScanCleanerCase() {
        super(RxGalleryExecutors.getInstance().getUserThreadExecutor(), RxGalleryExecutors.getInstance().getUiThreadExecutor());
    }

    @Override // com.miui.gallery.base_optimization.clean.HotUseCase
    public Flowable<AlbumTabToolItemBean> buildFlowable(Void r2) {
        return Flowable.create(new FlowableOnSubscribe<AlbumTabToolItemBean>() { // from class: com.miui.gallery.ui.album.main.usecase.ScanCleanerCase.1
            @Override // io.reactivex.FlowableOnSubscribe
            public void subscribe(FlowableEmitter<AlbumTabToolItemBean> flowableEmitter) throws Exception {
                AlbumTabToolItemBean cleanerBean = ScanCleanerCase.this.getCleanerBean();
                if (cleanerBean != null) {
                    long albumCleanableCount = GalleryPreferences.Album.getAlbumCleanableCount();
                    if (albumCleanableCount <= 0) {
                        ScanCleanerCase.this.scanForResult(cleanerBean, flowableEmitter);
                        return;
                    }
                    cleanerBean.setSubTitle(StringUtils.getNumberStringInRange(albumCleanableCount));
                    flowableEmitter.onNext(cleanerBean);
                    return;
                }
                flowableEmitter.onError(new Throwable("cleaner is null"));
            }
        }, BackpressureStrategy.LATEST);
    }

    public final AlbumTabToolItemBean getCleanerBean() {
        return CloudControlStrategyHelper.getAlbumTabToolsStrategy().getToolById(2147483635L);
    }

    public final void scanForResult(final AlbumTabToolItemBean albumTabToolItemBean, final FlowableEmitter<AlbumTabToolItemBean> flowableEmitter) {
        if (this.mScannerManager == null) {
            this.mScannerManager = ScannerManager.getInstance();
        }
        if (this.mScanObserver == null) {
            ScannerManager.ScanObserver scanObserver = new ScannerManager.ScanObserver() { // from class: com.miui.gallery.ui.album.main.usecase.ScanCleanerCase.2
                @Override // com.miui.gallery.cleaner.ScannerManager.ScanObserver
                public void onScanCanceled() {
                }

                @Override // com.miui.gallery.cleaner.ScannerManager.ScanObserver
                public void onScanProgress(long j) {
                }

                @Override // com.miui.gallery.cleaner.ScannerManager.ScanObserver
                public void onScanResultUpdate(long j) {
                }

                @Override // com.miui.gallery.cleaner.ScannerManager.ScanObserver
                public void onScanStart() {
                }

                @Override // com.miui.gallery.cleaner.ScannerManager.ScanObserver
                public void onScanFinish(long j) {
                    long j2 = 0;
                    for (ScanResult scanResult : ScanCleanerCase.this.mScannerManager.getScanResults()) {
                        j2 += scanResult.getCount();
                    }
                    GalleryPreferences.Album.setAlbumCleanableCount(j2);
                    albumTabToolItemBean.setSubTitle(StringUtils.getNumberStringInRange(j2));
                    flowableEmitter.onNext(albumTabToolItemBean);
                }
            };
            this.mScanObserver = scanObserver;
            this.mScannerManager.registerObserver(scanObserver);
        }
        this.mScannerManager.startScan();
    }

    @Override // com.miui.gallery.base_optimization.clean.HotUseCase, com.miui.gallery.base_optimization.clean.LifecycleUseCase
    public void onDestroy() {
        ScannerManager.ScanObserver scanObserver;
        super.onDestroy();
        ScannerManager scannerManager = this.mScannerManager;
        if (scannerManager == null || (scanObserver = this.mScanObserver) == null) {
            return;
        }
        scannerManager.unregisterObserver(scanObserver);
    }
}
