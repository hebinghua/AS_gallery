package com.miui.gallery.ui.photoPage.bars.manager;

import android.app.Activity;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.miui.gallery.R;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.adapter.PhotoPageAdapter;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.app.fragment.GalleryFragment;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.provider.GalleryOpenProvider;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.ui.ChooserFragment;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.ui.ShareStateRouter;
import com.miui.gallery.ui.photoPage.bars.data.IDataProvider;
import com.miui.gallery.ui.share.MultiFuncItem;
import com.miui.gallery.ui.share.OnPrepareListener;
import com.miui.gallery.ui.share.ShareFilePrepareFragment;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.DialogUtil;
import com.miui.gallery.util.IncompatibleMediaType;
import com.miui.gallery.util.SecurityShareHelper;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.imagecleanlib.ImageCleanManager;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public abstract class ChoiceManagerBase implements ChooserFragment.OnIntentSelectedListener, ChooserFragment.OnProjectClickedListener, ChooserFragment.OnMishareClickedListener, PhotoPageAdapter.MultiChoiceModeListener {
    public PhotoPageAdapter mAdapter;
    public PhotoPageAdapter.ChoiceMode mChoiceMode;
    public BaseActivity mContext;
    public IDataProvider mDataProvider;
    public GalleryFragment mFragment;
    public AlertDialog mLoadingDialog;
    public ChooserFragment.OnFilesProcessedListener mOnFilesProcessedListener;
    public final IManagerOwner mOwner;
    public String mShareClassName;
    public String mSharePackageName;
    public Intent mSharePendingIntent;
    public Runnable mShowLoadingDialogRunnable;
    public Disposable mSubtitleUpdateDisposable;
    public String[] mSupportShareBurstItemPackages = {"com.miui.mishare.connectivity", "com.android.bluetooth"};
    public boolean mNeedShowLoadingDialog = false;
    public SecurityShareHelper.SecureShareProgressDialogHelper mSecureShareProgressDialogHelper = new SecurityShareHelper.SecureShareProgressDialogHelper();
    public View.OnClickListener mSubTitleOnClickListener = new View.OnClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.manager.ChoiceManagerBase.1
        {
            ChoiceManagerBase.this = this;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            PhotoPageAdapter.ChoiceMode choiceMode = ChoiceManagerBase.this.mChoiceMode;
            if (choiceMode == null || choiceMode.getSelectItems().size() == 0) {
                return;
            }
            TrackController.trackClick("403.37.0.1.11236", AutoTracking.getRef());
            if (ChoiceManagerBase.this.mChoiceMode.getSelectItems().size() == 1) {
                ChoiceManagerBase choiceManagerBase = ChoiceManagerBase.this;
                SecurityShareHelper.startPrivacyProtectSettingsActivity(ChoiceManagerBase.this.mContext, false, choiceManagerBase.mAdapter.getDataItem(choiceManagerBase.mChoiceMode.getSelectItems().get(0).intValue()));
            } else if (ChoiceManagerBase.this.mChoiceMode.getSelectItems().size() <= 1) {
            } else {
                SecurityShareHelper.startPrivacyProtectSettingsActivity(ChoiceManagerBase.this.mContext, true, null);
            }
        }
    };

    public static /* synthetic */ void $r8$lambda$2OotkNqkPo48zP55y6hREtti4Lg(ChoiceManagerBase choiceManagerBase, DialogInterface dialogInterface) {
        choiceManagerBase.lambda$promptUnsupportedMedia$3(dialogInterface);
    }

    public static /* synthetic */ void $r8$lambda$6sjfTu4cMHJNhWumZDIUMRBzc3U(ChoiceManagerBase choiceManagerBase, ArrayList arrayList, DialogInterface dialogInterface, int i) {
        choiceManagerBase.lambda$promptUnsupportedMedia$1(arrayList, dialogInterface, i);
    }

    public static /* synthetic */ void $r8$lambda$JthN8mIVoisdum0Cq_PaXj8iGL8(ChoiceManagerBase choiceManagerBase, DialogInterface dialogInterface) {
        choiceManagerBase.lambda$doPrepareFiles$5(dialogInterface);
    }

    public static /* synthetic */ void $r8$lambda$VBrlb3RW85oaUQ2l3eRC8iYN2NM(ChoiceManagerBase choiceManagerBase, ArrayList arrayList, boolean z, boolean z2) {
        choiceManagerBase.lambda$promptNetwork$4(arrayList, z, z2);
    }

    /* renamed from: $r8$lambda$uV7br6JLCGE8MgA-wtjMFjdC2UM */
    public static /* synthetic */ void m1621$r8$lambda$uV7br6JLCGE8MgAwtjMFjdC2UM(ChoiceManagerBase choiceManagerBase, List list, ArrayList arrayList, DialogInterface dialogInterface, int i) {
        choiceManagerBase.lambda$promptUnsupportedMedia$2(list, arrayList, dialogInterface, i);
    }

    public abstract TextView getChoiceTitle();

    public abstract int getContainerId();

    public abstract TextView getSubTitle();

    public ChoiceManagerBase(IManagerOwner iManagerOwner) {
        this.mOwner = iManagerOwner;
        this.mContext = iManagerOwner.getBaseActivity();
        this.mFragment = iManagerOwner.getFragment();
        this.mDataProvider = iManagerOwner.getDataProvider();
    }

    public void initFunction() {
        this.mAdapter = this.mOwner.getAdapter();
    }

    public void release() {
        Disposable disposable = this.mSubtitleUpdateDisposable;
        if (disposable == null || disposable.isDisposed()) {
            return;
        }
        this.mSubtitleUpdateDisposable.dispose();
    }

    public void setUpChooserFragment(int i, boolean z, int i2, String str) {
        Fragment findFragmentByTag = this.mFragment.getChildFragmentManager().findFragmentByTag("ChooserFragment");
        if (findFragmentByTag == null) {
            FragmentTransaction beginTransaction = this.mFragment.getChildFragmentManager().beginTransaction();
            Intent intent = new Intent("android.intent.action.SEND");
            intent.setType(str);
            ChooserFragment newInstance = ChooserFragment.newInstance(intent, i, z, i2);
            newInstance.setOnIntentSelectedListener(this);
            newInstance.setOnProjectClickedListener(this);
            newInstance.setOnMishareClickedListener(this);
            beginTransaction.add(getContainerId(), newInstance, "ChooserFragment");
            beginTransaction.commitNowAllowingStateLoss();
        } else {
            ChooserFragment chooserFragment = (ChooserFragment) findFragmentByTag;
            chooserFragment.setOnIntentSelectedListener(this);
            chooserFragment.setOnProjectClickedListener(this);
            chooserFragment.setOnMishareClickedListener(this);
            chooserFragment.onVisibilityChanged(z);
        }
        if (getSubTitle() != null) {
            getSubTitle().setOnClickListener(this.mSubTitleOnClickListener);
        }
    }

    public void showChooserFragment() {
        Fragment findFragmentByTag = this.mFragment.getChildFragmentManager().findFragmentByTag("ChooserFragment");
        if (findFragmentByTag instanceof ChooserFragment) {
            ChooserFragment chooserFragment = (ChooserFragment) findFragmentByTag;
            if (!equals(chooserFragment.getOnIntentSelectedListener())) {
                chooserFragment.setOnIntentSelectedListener(this);
            }
            if (!equals(chooserFragment.getOnProjectClickedListener())) {
                chooserFragment.setOnProjectClickedListener(this);
            }
            if (!equals(chooserFragment.getOnMishareClickedListener())) {
                chooserFragment.setOnMishareClickedListener(this);
            }
            FragmentTransaction beginTransaction = this.mFragment.getChildFragmentManager().beginTransaction();
            beginTransaction.show(findFragmentByTag);
            beginTransaction.commitAllowingStateLoss();
            chooserFragment.onVisibilityChanged(true);
            if (getSubTitle() == null) {
                return;
            }
            getSubTitle().setOnClickListener(this.mSubTitleOnClickListener);
        }
    }

    public void hideChooserFragment() {
        Fragment findFragmentByTag = this.mFragment.getChildFragmentManager().findFragmentByTag("ChooserFragment");
        if (findFragmentByTag instanceof ChooserFragment) {
            FragmentTransaction beginTransaction = this.mFragment.getChildFragmentManager().beginTransaction();
            beginTransaction.hide(findFragmentByTag);
            beginTransaction.commitAllowingStateLoss();
            ((ChooserFragment) findFragmentByTag).onVisibilityChanged(false);
        }
    }

    public boolean getPrintStatus(int i) {
        BaseDataItem dataItem;
        if (i == 1 && (dataItem = this.mAdapter.getDataItem(this.mChoiceMode.getSelectItems().get(0).intValue())) != null) {
            String mimeType = dataItem.getMimeType();
            if (TextUtils.isEmpty(mimeType)) {
                return false;
            }
            return BaseFileMimeUtil.isJpgFromMimeType(mimeType) || BaseFileMimeUtil.isJpegImageFromMimeType(mimeType) || BaseFileMimeUtil.isPngImageFromMimeType(mimeType) || BaseFileMimeUtil.isGifFromMimeType(mimeType) || BaseFileMimeUtil.isBmpFromMimeType(mimeType) || BaseFileMimeUtil.isWebpFromMimeType(mimeType) || BaseFileMimeUtil.isWbmpFromMimeType(mimeType);
        }
        return false;
    }

    public void updateSelectMode() {
        int size = this.mChoiceMode.getSelectItems().size();
        updateSubTitle();
        if (getChoiceTitle() != null) {
            if (size == 0) {
                getChoiceTitle().setText(this.mFragment.getResources().getString(R.string.custom_select_title_quickly_share));
            } else {
                getChoiceTitle().setText(this.mFragment.getResources().getQuantityString(R.plurals.custom_select_title_items_selected, size, Integer.valueOf(size)));
            }
        }
        Intent intent = new Intent();
        configTargetIntentBySelected(size, intent);
        Fragment findFragmentByTag = this.mFragment.getChildFragmentManager().findFragmentByTag("ChooserFragment");
        if (findFragmentByTag != null) {
            ((ChooserFragment) findFragmentByTag).requery(intent);
        }
    }

    public void updateSubTitle() {
        boolean z;
        if (getSubTitle() == null || this.mChoiceMode == null) {
            return;
        }
        if (!SecurityShareHelper.isSupportMiui12(this.mContext)) {
            getSubTitle().setVisibility(8);
            return;
        }
        int size = this.mChoiceMode.getSelectItems().size();
        String str = null;
        Iterator<Integer> it = this.mChoiceMode.getSelectItems().iterator();
        while (true) {
            if (!it.hasNext()) {
                z = false;
                break;
            }
            int intValue = it.next().intValue();
            String itemImagePath = SecurityShareHelper.getItemImagePath(this.mAdapter.getDataItem(intValue));
            if (TextUtils.isEmpty(itemImagePath)) {
                DefaultLogger.e("zman_share", "imagePath isEmpty");
                return;
            } else if (BaseFileMimeUtil.isImageFromMimeType(this.mAdapter.getDataItem(intValue).getMimeType())) {
                str = itemImagePath;
                z = true;
                break;
            } else {
                str = itemImagePath;
            }
        }
        if (size == 0 || !z) {
            getSubTitle().setVisibility(8);
            return;
        }
        getSubTitle().setVisibility(0);
        if (size == 1) {
            Disposable disposable = this.mSubtitleUpdateDisposable;
            if (disposable != null && !disposable.isDisposed()) {
                this.mSubtitleUpdateDisposable.dispose();
            }
            this.mSubtitleUpdateDisposable = Observable.just(str).map(ChoiceManagerBase$$ExternalSyntheticLambda5.INSTANCE).subscribeOn(Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR)).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Boolean>() { // from class: com.miui.gallery.ui.photoPage.bars.manager.ChoiceManagerBase.2
                {
                    ChoiceManagerBase.this = this;
                }

                @Override // io.reactivex.functions.Consumer
                public void accept(Boolean bool) throws Exception {
                    if (bool.booleanValue()) {
                        ChoiceManagerBase.this.getSubTitle().setText(SecurityShareHelper.isHideLocationInfoEnable(ChoiceManagerBase.this.mContext) ? R.string.security_share_subtitle_hide : R.string.security_share_subtitle_no_hide);
                    }
                }
            }, new Consumer<Throwable>() { // from class: com.miui.gallery.ui.photoPage.bars.manager.ChoiceManagerBase.3
                {
                    ChoiceManagerBase.this = this;
                }

                @Override // io.reactivex.functions.Consumer
                public void accept(Throwable th) throws Exception {
                    DefaultLogger.e("ChoiceManagerBase", "security share subtitle error -> " + th.toString());
                }
            });
        }
        getSubTitle().setText(R.string.security_share_subtitle_default);
    }

    public static /* synthetic */ Boolean lambda$updateSubTitle$0(String str) throws Exception {
        return Boolean.valueOf(SecurityShareHelper.haveLocationInfo(new File(str)));
    }

    public void configTargetIntentBySelected(int i, Intent intent) {
        String str = "video/*";
        int i2 = 1;
        if (i <= 1) {
            intent.setAction("android.intent.action.SEND");
            BaseDataItem baseDataItem = null;
            if (i > 0) {
                baseDataItem = this.mAdapter.getDataItem(this.mChoiceMode.getSelectItems().get(0).intValue());
            }
            if (baseDataItem == null || !baseDataItem.isVideo()) {
                str = "image/*";
            }
            intent.setType(str);
        } else {
            intent.setAction("android.intent.action.SEND_MULTIPLE");
            BaseDataItem dataItem = this.mAdapter.getDataItem(this.mChoiceMode.getSelectItems().get(0).intValue());
            String str2 = (dataItem == null || !dataItem.isVideo()) ? "image/*" : str;
            while (true) {
                if (i2 >= i) {
                    break;
                }
                BaseDataItem dataItem2 = this.mAdapter.getDataItem(this.mChoiceMode.getSelectItems().get(i2).intValue());
                if (!TextUtils.equals(str2, (dataItem2 == null || !dataItem2.isVideo()) ? "image/*" : str)) {
                    str2 = "*/*";
                    break;
                }
                i2++;
            }
            intent.setType(str2);
        }
        ShareStateRouter.getInstance().broadcastPrintState(getPrintStatus(i));
    }

    public void onShared(boolean z) {
        trackShared(z);
    }

    public final void trackShared(boolean z) {
        if (this.mChoiceMode != null) {
            HashMap hashMap = new HashMap();
            hashMap.put("tip", "403.37.0.1.13796");
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, this.mChoiceMode.getType());
            hashMap.put("status", SecurityShareHelper.isNeedSecurityShare(this.mContext) ? "open" : "close");
            hashMap.put("success", Boolean.valueOf(z));
            TrackController.trackClick(hashMap);
        }
    }

    @Override // com.miui.gallery.adapter.PhotoPageAdapter.MultiChoiceModeListener
    public final void onItemCheckedStateChanged(int i, long j, boolean z) {
        updateSelectMode();
    }

    @Override // com.miui.gallery.adapter.PhotoPageAdapter.MultiChoiceModeListener
    public final void onAllItemsCheckedStateChanged(boolean z) {
        updateSelectMode();
    }

    @Override // com.miui.gallery.ui.ChooserFragment.OnProjectClickedListener
    public void onProjectedClicked() {
        if (this.mChoiceMode != null) {
            TrackController.trackClick("403.37.0.1.11232", AutoTracking.getRef(), this.mChoiceMode.getSelectItems().size());
        }
    }

    @Override // com.miui.gallery.ui.ChooserFragment.OnMishareClickedListener
    public void onMishareClicked(ChooserFragment.OnFilesProcessedListener onFilesProcessedListener) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.miui.mishare.connectivity", "MiShareGalleryTransferView"));
        this.mOnFilesProcessedListener = onFilesProcessedListener;
        onIntentSelected(intent);
    }

    public final void promptUnsupportedMedia(final ArrayList<MultiFuncItem> arrayList, final List<Integer> list) {
        String string = this.mFragment.getResources().getString(R.string.download_tip);
        DialogUtil.showInfoDialog(this.mContext, IncompatibleMediaType.getUnsupportedMediaDownloadingTip(this.mContext, ""), string, (int) R.string.download_tip, 17039360, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.manager.ChoiceManagerBase$$ExternalSyntheticLambda2
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                ChoiceManagerBase.$r8$lambda$6sjfTu4cMHJNhWumZDIUMRBzc3U(ChoiceManagerBase.this, arrayList, dialogInterface, i);
            }
        }, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.manager.ChoiceManagerBase$$ExternalSyntheticLambda3
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                ChoiceManagerBase.m1621$r8$lambda$uV7br6JLCGE8MgAwtjMFjdC2UM(ChoiceManagerBase.this, list, arrayList, dialogInterface, i);
            }
        }, new DialogInterface.OnCancelListener() { // from class: com.miui.gallery.ui.photoPage.bars.manager.ChoiceManagerBase$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnCancelListener
            public final void onCancel(DialogInterface dialogInterface) {
                ChoiceManagerBase.$r8$lambda$2OotkNqkPo48zP55y6hREtti4Lg(ChoiceManagerBase.this, dialogInterface);
            }
        });
    }

    public /* synthetic */ void lambda$promptUnsupportedMedia$1(ArrayList arrayList, DialogInterface dialogInterface, int i) {
        if (BaseNetworkUtils.isActiveNetworkMetered()) {
            promptNetwork(arrayList);
        } else {
            doPrepareFiles(arrayList);
        }
    }

    public /* synthetic */ void lambda$promptUnsupportedMedia$2(List list, ArrayList arrayList, DialogInterface dialogInterface, int i) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            int intValue = ((Integer) it.next()).intValue();
            MultiFuncItem build = new MultiFuncItem.Builder().cloneFrom((MultiFuncItem) arrayList.get(intValue)).setDownloadType(DownloadType.THUMBNAIL).build();
            arrayList.remove(intValue);
            arrayList.add(intValue, build);
        }
        doPrepareFiles(arrayList);
    }

    public /* synthetic */ void lambda$promptUnsupportedMedia$3(DialogInterface dialogInterface) {
        this.mSharePendingIntent = null;
    }

    public final void promptNetwork(final ArrayList<MultiFuncItem> arrayList) {
        NetworkConsider.consider(this.mContext, Boolean.FALSE, new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.ui.photoPage.bars.manager.ChoiceManagerBase$$ExternalSyntheticLambda4
            @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
            public final void onConfirmed(boolean z, boolean z2) {
                ChoiceManagerBase.$r8$lambda$VBrlb3RW85oaUQ2l3eRC8iYN2NM(ChoiceManagerBase.this, arrayList, z, z2);
            }
        });
    }

    public /* synthetic */ void lambda$promptNetwork$4(ArrayList arrayList, boolean z, boolean z2) {
        if (z) {
            doPrepareFiles(arrayList);
        } else {
            this.mSharePendingIntent = null;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:215:0x01ed  */
    /* JADX WARN: Removed duplicated region for block: B:216:0x0205  */
    /* JADX WARN: Removed duplicated region for block: B:219:0x0211 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:227:0x0236 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:231:0x023e A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:236:0x0256  */
    /* JADX WARN: Removed duplicated region for block: B:241:0x0263 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:245:0x0289  */
    @Override // com.miui.gallery.ui.ChooserFragment.OnIntentSelectedListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void onIntentSelected(android.content.Intent r30) {
        /*
            Method dump skipped, instructions count: 852
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.ui.photoPage.bars.manager.ChoiceManagerBase.onIntentSelected(android.content.Intent):void");
    }

    public final boolean supportShareBurst(String str) {
        for (String str2 : this.mSupportShareBurstItemPackages) {
            if (str2.equalsIgnoreCase(str)) {
                return true;
            }
        }
        return false;
    }

    public final void doPrepareFiles(ArrayList<MultiFuncItem> arrayList) {
        ShareFilePrepareFragment newInstance = ShareFilePrepareFragment.newInstance(arrayList, new OnPrepareListener<MultiFuncItem>() { // from class: com.miui.gallery.ui.photoPage.bars.manager.ChoiceManagerBase.4
            {
                ChoiceManagerBase.this = this;
            }

            @Override // com.miui.gallery.ui.share.OnPrepareListener
            public void onPrepareComplete(final ArrayList<MultiFuncItem> arrayList2) {
                boolean z = false;
                int i = 0;
                int i2 = 0;
                int i3 = 0;
                for (int i4 = 0; i4 < arrayList2.size(); i4++) {
                    MultiFuncItem multiFuncItem = arrayList2.get(i4);
                    if (multiFuncItem.getFlags() > 0) {
                        if ((multiFuncItem.getFlags() & 1) == 1) {
                            i++;
                        }
                        if ((multiFuncItem.getFlags() & 8) == 8) {
                            i2++;
                        }
                        if (multiFuncItem.getSecretId() > 0) {
                            i3++;
                        }
                        MultiFuncItem build = new MultiFuncItem.Builder().cloneFrom(multiFuncItem).setSrcUri(multiFuncItem.getPreparedUriInLastStep()).build();
                        arrayList2.remove(i4);
                        arrayList2.add(i4, build);
                    }
                }
                if (i > 0) {
                    ChoiceManagerBase choiceManagerBase = ChoiceManagerBase.this;
                    DialogUtil.showInfoDialog(choiceManagerBase.mContext, choiceManagerBase.mFragment.getResources().getQuantityString(R.plurals.download_retry_message, i, Integer.valueOf(i)), ChoiceManagerBase.this.mFragment.getResources().getString(R.string.download_retry_title), (int) R.string.download_retry_text, 17039360, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.manager.ChoiceManagerBase.4.1
                        {
                            AnonymousClass4.this = this;
                        }

                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i5) {
                            ChoiceManagerBase.this.doPrepareFiles(arrayList2);
                        }
                    }, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.photoPage.bars.manager.ChoiceManagerBase.4.2
                        {
                            AnonymousClass4.this = this;
                        }

                        @Override // android.content.DialogInterface.OnClickListener
                        public void onClick(DialogInterface dialogInterface, int i5) {
                            ChoiceManagerBase.this.mSharePendingIntent = null;
                        }
                    }, new DialogInterface.OnCancelListener() { // from class: com.miui.gallery.ui.photoPage.bars.manager.ChoiceManagerBase.4.3
                        {
                            AnonymousClass4.this = this;
                        }

                        @Override // android.content.DialogInterface.OnCancelListener
                        public void onCancel(DialogInterface dialogInterface) {
                            ChoiceManagerBase.this.mSharePendingIntent = null;
                        }
                    });
                    return;
                }
                if (i2 > 0) {
                    ToastUtils.makeText(ChoiceManagerBase.this.mContext, ChoiceManagerBase.this.mContext.getResources().getQuantityString(R.plurals.fast_share_auto_render_failed, i2, Integer.valueOf(i2)));
                }
                ChoiceManagerBase choiceManagerBase2 = ChoiceManagerBase.this;
                if (i3 > 0) {
                    z = true;
                }
                choiceManagerBase2.doShare(arrayList2, z);
                ChoiceManagerBase.this.mDataProvider.onContentChanged();
            }
        });
        newInstance.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.miui.gallery.ui.photoPage.bars.manager.ChoiceManagerBase$$ExternalSyntheticLambda1
            @Override // android.content.DialogInterface.OnCancelListener
            public final void onCancel(DialogInterface dialogInterface) {
                ChoiceManagerBase.$r8$lambda$JthN8mIVoisdum0Cq_PaXj8iGL8(ChoiceManagerBase.this, dialogInterface);
            }
        });
        newInstance.showAllowingStateLoss(this.mContext.getSupportFragmentManager(), "DownloadFragment");
    }

    public /* synthetic */ void lambda$doPrepareFiles$5(DialogInterface dialogInterface) {
        this.mSharePendingIntent = null;
    }

    public final void doShare(List<MultiFuncItem> list, boolean z) {
        if (this.mSharePendingIntent == null) {
            return;
        }
        ArrayList<? extends Parcelable> arrayList = new ArrayList<>(list.size());
        for (MultiFuncItem multiFuncItem : list) {
            if (multiFuncItem.getPreparedUri() != null) {
                arrayList.add(multiFuncItem.getPreparedUri());
            } else if (multiFuncItem.getPreparedUriInLastStep() != null) {
                arrayList.add(multiFuncItem.getPreparedUriInLastStep());
            } else {
                arrayList.add(multiFuncItem.getSrcUri());
            }
        }
        if (z || GalleryOpenProvider.needReturnContentUri(this.mContext, this.mSharePendingIntent)) {
            for (int i = 0; i < arrayList.size(); i++) {
                Uri translateToContent = GalleryOpenProvider.translateToContent(((Uri) arrayList.get(i)).getPath());
                arrayList.set(i, translateToContent);
                String str = this.mSharePackageName;
                if (str != null) {
                    this.mContext.grantUriPermission(str, translateToContent, 1);
                    BaseActivity baseActivity = this.mContext;
                    baseActivity.grantUriPermission(SecurityShareHelper.getSharePackageName(baseActivity), translateToContent, 1);
                }
                if ("com.miui.mishare.connectivity".equals(this.mSharePackageName) && ShareStateRouter.SHOULD_UPGRADE_NEARBY_SHARE.get(this.mContext).booleanValue()) {
                    this.mContext.grantUriPermission("com.google.android.gms", translateToContent, 1);
                }
            }
        }
        if ("com.miui.mishare.connectivity".equals(this.mSharePackageName)) {
            this.mOnFilesProcessedListener.onFilesProcessed(arrayList);
            this.mSharePendingIntent = null;
            return;
        }
        DefaultLogger.d("ChoiceManagerBase", "prepare to share: %s", arrayList);
        int size = arrayList.size();
        if (size > 0) {
            if (size > 1) {
                this.mSharePendingIntent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList);
            } else {
                this.mSharePendingIntent.setAction("android.intent.action.SEND");
                this.mSharePendingIntent.putExtra("android.intent.extra.STREAM", (Parcelable) arrayList.get(0));
            }
            this.mSharePendingIntent.addFlags(268435456);
            this.mSharePendingIntent.addFlags(134742016);
            if (SecurityShareHelper.isSupportMiui11(this.mContext, this.mSharePackageName)) {
                Intent intent = new Intent("miui.intent.action.XMAN_SHARE_MANAGER");
                intent.setPackage(SecurityShareHelper.getSharePackageName(this.mContext));
                intent.putExtra("android.intent.extra.INTENT", this.mSharePendingIntent);
                DefaultLogger.d("ChoiceManagerBase", "supportmiui11 sharepackagename %s uris : %s", this.mSharePackageName, arrayList);
                this.mFragment.startActivityForResult(intent, 1);
            } else if (SecurityShareHelper.isNeedSecurityShare(this.mContext)) {
                DefaultLogger.d("ChoiceManagerBase", "security share sharepackagename %s uris : %s", this.mSharePackageName, arrayList);
                BaseActivity baseActivity2 = this.mContext;
                SecurityShareHelper.doSecurityShare(baseActivity2, this.mSharePendingIntent, arrayList, new ImageCleanProgressListener(this.mSecureShareProgressDialogHelper, baseActivity2));
            } else {
                DefaultLogger.d("ChoiceManagerBase", "normal share sharepackagename %s uris : %s", this.mSharePackageName, arrayList);
                this.mFragment.startActivityForResult(this.mSharePendingIntent, 1);
                if (this.mShowLoadingDialogRunnable == null) {
                    this.mShowLoadingDialogRunnable = new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.manager.ChoiceManagerBase.5
                        {
                            ChoiceManagerBase.this = this;
                        }

                        @Override // java.lang.Runnable
                        public void run() {
                            String string;
                            if (ChoiceManagerBase.this.mNeedShowLoadingDialog) {
                                ChoiceManagerBase choiceManagerBase = ChoiceManagerBase.this;
                                String shareDisplayLabel = choiceManagerBase.getShareDisplayLabel(choiceManagerBase.mSharePackageName, choiceManagerBase.mShareClassName);
                                if (TextUtils.isEmpty(shareDisplayLabel)) {
                                    string = ChoiceManagerBase.this.mContext.getResources().getString(R.string.app_is_launching_common);
                                } else {
                                    string = ChoiceManagerBase.this.mContext.getResources().getString(R.string.app_is_launching, shareDisplayLabel);
                                }
                                ChoiceManagerBase choiceManagerBase2 = ChoiceManagerBase.this;
                                choiceManagerBase2.mLoadingDialog = DialogUtil.createLoadingDialog(choiceManagerBase2.mContext, string);
                                if (ChoiceManagerBase.this.mLoadingDialog == null) {
                                    return;
                                }
                                ChoiceManagerBase.this.mLoadingDialog.show();
                            }
                        }
                    };
                }
                if (!ThreadManager.getMainHandler().hasCallbacksCompat(this.mShowLoadingDialogRunnable)) {
                    this.mNeedShowLoadingDialog = true;
                    ThreadManager.getMainHandler().postDelayed(this.mShowLoadingDialogRunnable, 500L);
                }
            }
        }
        this.mSharePendingIntent = null;
    }

    public final String getShareDisplayLabel(String str, String str2) {
        try {
            Resources resources = this.mContext.getResources();
            int identifier = resources.getIdentifier(String.format(Locale.US, "%s_%s", str, str2), "string", this.mContext.getPackageName());
            if (identifier == 0) {
                return null;
            }
            return resources.getString(identifier);
        } catch (Exception unused) {
            return null;
        }
    }

    public void onPause() {
        dismissLoadingDialog();
        this.mNeedShowLoadingDialog = false;
    }

    public void onDetach() {
        if (this.mShowLoadingDialogRunnable != null) {
            ThreadManager.getMainHandler().removeCallbacks(this.mShowLoadingDialogRunnable);
        }
        dismissLoadingDialog();
        this.mSecureShareProgressDialogHelper.dismissDialog();
    }

    public final void dismissLoadingDialog() {
        AlertDialog alertDialog = this.mLoadingDialog;
        if (alertDialog == null || !alertDialog.isShowing()) {
            return;
        }
        this.mLoadingDialog.dismiss();
        this.mLoadingDialog = null;
    }

    /* loaded from: classes2.dex */
    public static class ImageCleanProgressListener implements ImageCleanManager.CleanProgressListener {
        public WeakReference<Activity> mActivityRef;
        public WeakReference<SecurityShareHelper.SecureShareProgressDialogHelper> mSecureShareProgressDialogHelperRef;

        public ImageCleanProgressListener(SecurityShareHelper.SecureShareProgressDialogHelper secureShareProgressDialogHelper, Activity activity) {
            this.mSecureShareProgressDialogHelperRef = new WeakReference<>(secureShareProgressDialogHelper);
            this.mActivityRef = new WeakReference<>(activity);
        }

        @Override // com.miui.imagecleanlib.ImageCleanManager.CleanProgressListener
        public void onProgress(final int i, final int i2) {
            if (i2 > 10) {
                ThreadManager.getMainHandler().post(new Runnable() { // from class: com.miui.gallery.ui.photoPage.bars.manager.ChoiceManagerBase.ImageCleanProgressListener.1
                    {
                        ImageCleanProgressListener.this = this;
                    }

                    @Override // java.lang.Runnable
                    public void run() {
                        if (ImageCleanProgressListener.this.mSecureShareProgressDialogHelperRef == null || ImageCleanProgressListener.this.mSecureShareProgressDialogHelperRef.get() == null || ImageCleanProgressListener.this.mActivityRef == null || ImageCleanProgressListener.this.mActivityRef.get() == null) {
                            return;
                        }
                        ((SecurityShareHelper.SecureShareProgressDialogHelper) ImageCleanProgressListener.this.mSecureShareProgressDialogHelperRef.get()).showProgressDialog(ImageCleanProgressListener.this.mActivityRef, i, i2);
                        if (i != i2) {
                            return;
                        }
                        ((SecurityShareHelper.SecureShareProgressDialogHelper) ImageCleanProgressListener.this.mSecureShareProgressDialogHelperRef.get()).dismissDialog();
                    }
                });
            }
        }
    }
}
