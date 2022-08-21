package com.miui.gallery.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.FragmentActivity;
import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.R;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.cloud.base.SyncType;
import com.miui.gallery.cloud.syncstate.SyncStateInfo;
import com.miui.gallery.cloud.syncstate.SyncStateManager;
import com.miui.gallery.cloud.syncstate.SyncStatus;
import com.miui.gallery.cloud.syncstate.TextLinkPriority;
import com.miui.gallery.error.core.ErrorCode;
import com.miui.gallery.error.core.ErrorTip;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.search.statistics.SearchStatUtils;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.ui.SyncWidget;
import com.miui.gallery.util.ActionURIHandler;
import com.miui.gallery.util.FormatUtil;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.deprecated.Preference;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.PanelManager;
import com.miui.gallery.widget.VerticalImageSpan;
import java.util.HashMap;
import java.util.List;
import miuix.animation.Folme;
import miuix.animation.IStateStyle;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.property.ViewProperty;

/* loaded from: classes2.dex */
public class SyncTextLine extends SyncWidget {
    public ImageView mActionArrow;
    public TextView mActionTextView;
    public View mActionView;
    public TextView mDescView;
    public TextLinkData mDownloadPanelData;
    public AnimConfig mHideAnimConfig;
    public AnimState mHideAnimState;
    public boolean mIsErrorStatus;
    public PanelManager mPanelManager;
    public boolean mPreIsLongDesc;
    public int mPriority;
    public AnimConfig mShowAnimConfig;
    public AnimState mShowAnimState;
    public TextLinkData mSyncPanelData;
    public View mSyncView;
    public TextView mTitleView;

    public SyncTextLine(Context context, int i, PanelManager panelManager) {
        super(context);
        this.mPriority = i;
        this.mPanelManager = panelManager;
        this.mSyncClickListener = new SyncClickListener();
        this.mDownloadClickListener = new SyncWidget.DownloadClickListener();
        ensureView();
    }

    public void setPriority(int i) {
        this.mPriority = i;
    }

    public void ensureView() {
        if (this.mSyncView == null) {
            View inflate = LayoutInflater.from(this.mContext).inflate(R.layout.sync_text_line, (ViewGroup) null);
            this.mSyncView = inflate;
            this.mTitleView = (TextView) inflate.findViewById(R.id.text_line_title);
            this.mDescView = (TextView) this.mSyncView.findViewById(R.id.text_line_desc);
            this.mActionView = this.mSyncView.findViewById(R.id.text_line_action);
            this.mActionTextView = (TextView) this.mSyncView.findViewById(R.id.text_line_action_text);
            this.mActionArrow = (ImageView) this.mSyncView.findViewById(R.id.right_arrow);
        }
    }

    public void showOrHideSyncView(boolean z) {
        if (this.mSyncView == null) {
            return;
        }
        if (this.mShowAnimConfig == null) {
            this.mShowAnimConfig = new AnimConfig().setEase(-2, 0.95f, 0.3f);
        }
        if (this.mHideAnimState == null) {
            this.mHideAnimConfig = new AnimConfig().setEase(-2, 0.95f, 0.2f);
        }
        if (this.mShowAnimState == null) {
            this.mShowAnimState = new AnimState().add(ViewProperty.ALPHA, 1.0d);
        }
        if (this.mHideAnimState == null) {
            this.mHideAnimState = new AnimState().add(ViewProperty.ALPHA, SearchStatUtils.POW);
        }
        IStateStyle state = Folme.useAt(this.mSyncView).state();
        AnimState animState = z ? this.mShowAnimState : this.mHideAnimState;
        AnimConfig[] animConfigArr = new AnimConfig[1];
        animConfigArr[0] = z ? this.mShowAnimConfig : this.mHideAnimConfig;
        state.to(animState, animConfigArr);
    }

    @Override // com.miui.gallery.ui.DownloadManager.DownloadStatusUpdateListener
    public void onDownloadStatusUpdate(int i, int i2, int i3, ErrorTip errorTip) {
        this.mIsShowSync = false;
        DefaultLogger.i("SyncTextLine", "refreshByDownloadStatus,downloadStatus=%s", Integer.valueOf(i));
        if (i != 3) {
            if (i == 2) {
                refresh(new TextLinkData(null, null, null, i, null, this.mDownloadClickListener), false, this.mDownloadManager.getKey());
            } else if (i == 1) {
                if (!refresh(new TextLinkData(getContext().getResources().getQuantityString(R.plurals.downloading_title_link, i3, Integer.valueOf(i3), Integer.valueOf(i2)), null, null, i, null, this.mDownloadClickListener), false, this.mDownloadManager.getKey())) {
                    return;
                }
                this.mDownloadManager.setCurError(ErrorCode.NO_ERROR);
            } else if (i != 0) {
            } else {
                refresh(new TextLinkData(null, null, null, i, null, this.mDownloadClickListener), false, this.mDownloadManager.getKey());
            }
        } else if (errorTip == null) {
            refresh(new TextLinkData(null, null, null, i, null, this.mDownloadClickListener), false, this.mDownloadManager.getKey());
        } else if (AnonymousClass1.$SwitchMap$com$miui$gallery$error$core$ErrorCode[errorTip.getCode().ordinal()] == 1) {
            if (!refresh(new TextLinkData(errorTip.getTitle(getContext()), errorTip.getActionStr(getContext()), null, i, null, this.mDownloadClickListener), true, this.mDownloadManager.getKey())) {
                return;
            }
            this.mDownloadManager.setCurError(errorTip.getCode());
        } else {
            CharSequence title = errorTip.getTitle(getContext());
            if (TextUtils.isEmpty(title)) {
                refresh(new TextLinkData(null, null, null, i, null, this.mDownloadClickListener), false, this.mDownloadManager.getKey());
                return;
            }
            if (!refresh(new TextLinkData(title + getContext().getString(R.string.download_error_tip), null, null, i, null, this.mDownloadClickListener), true, this.mDownloadManager.getKey())) {
                return;
            }
            this.mDownloadManager.setCurError(errorTip.getCode());
        }
    }

    @Override // com.miui.gallery.ui.SyncManager.SyncStatusListener
    public void onSyncStatusChange(SyncStateInfo syncStateInfo) {
        String string;
        String str;
        Drawable drawable;
        String str2;
        String quantityString;
        String quantityString2;
        Drawable drawable2;
        String format;
        String string2;
        String string3;
        this.mIsShowSync = true;
        Resources resources = getContext().getResources();
        int[] dirtyCount = syncStateInfo.getDirtyCount();
        int i = dirtyCount[0] + dirtyCount[1];
        int[] syncedCount = syncStateInfo.getSyncedCount();
        SyncStatus syncStatus = syncStateInfo.getSyncStatus();
        setPriority(TextLinkPriority.getPriority(syncStatus.name(), 3));
        this.mIsErrorStatus = false;
        switch (AnonymousClass1.$SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[syncStatus.ordinal()]) {
            case 1:
                Drawable drawable3 = resources.getDrawable(R.drawable.synchronizing);
                if (i > 0) {
                    quantityString = resources.getString(R.string.sync_pending_title);
                    if (this.mSyncManager.hasShowSyncStatusPersistent()) {
                        this.mSyncManager.removeSyncStatusPersistent();
                    }
                } else {
                    quantityString = syncStateInfo.getSyncType() == SyncType.GPRS_FORCE ? resources.getQuantityString(R.plurals.syncing_gprs_title_link, i, Integer.valueOf(i)) : resources.getQuantityString(R.plurals.syncing_title_link, i, Integer.valueOf(i));
                }
                str = quantityString;
                drawable = drawable3;
                str2 = null;
                break;
            case 2:
                quantityString2 = syncStateInfo.getSyncType() == SyncType.GPRS_FORCE ? resources.getQuantityString(R.plurals.syncing_gprs_title_link, i, Integer.valueOf(i)) : resources.getQuantityString(R.plurals.syncing_title_link, i, Integer.valueOf(i));
                if (i > 0 && this.mSyncManager.hasShowSyncStatusPersistent()) {
                    this.mSyncManager.removeSyncStatusPersistent();
                }
                drawable2 = resources.getDrawable(R.drawable.synchronizing);
                str = quantityString2;
                drawable = drawable2;
                str2 = null;
                break;
            case 3:
                if (GalleryPreferences.Sync.getSlimTextLinkShouldShow().booleanValue()) {
                    GalleryPreferences.Sync.setSlimTextLinkShowedTimestamp(System.currentTimeMillis());
                    format = String.format(resources.getString(R.string.device_storage_space_low_textline_des), FormatUtil.formatFileSize(this.mContext, GalleryPreferences.Sync.getSlimableSize()));
                    string2 = resources.getString(R.string.device_storage_space_low_textline_button);
                    str = format;
                    str2 = string2;
                    drawable = null;
                    break;
                } else {
                    str = null;
                    str2 = null;
                    drawable = str2;
                    break;
                }
            case 4:
            case 5:
                string3 = resources.getString(R.string.syncing_metadata_title);
                str = string3;
                str2 = null;
                drawable = str2;
                break;
            case 6:
                if (syncedCount[0] == 0) {
                    quantityString2 = resources.getQuantityString(R.plurals.synced_videos_count, syncedCount[1], Integer.valueOf(syncedCount[1]));
                } else if (syncedCount[1] == 0) {
                    quantityString2 = resources.getQuantityString(R.plurals.synced_photo_count, syncedCount[0], Integer.valueOf(syncedCount[0]));
                } else {
                    quantityString2 = resources.getString(R.string.synced_photo_video_count, resources.getQuantityString(R.plurals.photo_count, syncedCount[0], Integer.valueOf(syncedCount[0])), resources.getQuantityString(R.plurals.videos_count, syncedCount[1], Integer.valueOf(syncedCount[1])));
                }
                drawable2 = resources.getDrawable(R.drawable.synced);
                str = quantityString2;
                drawable = drawable2;
                str2 = null;
                break;
            case 7:
            case 8:
            case 9:
                format = resources.getString(R.string.sync_off_title);
                string2 = resources.getString(R.string.cloud_action_open);
                str = format;
                str2 = string2;
                drawable = null;
                break;
            case 10:
                format = resources.getString(R.string.sync_not_allow_cta_title);
                string2 = resources.getString(R.string.cloud_action_open);
                str = format;
                str2 = string2;
                drawable = null;
                break;
            case 11:
                string3 = resources.getQuantityString(R.plurals.sync_oversized_count, i, Integer.valueOf(i));
                str = string3;
                str2 = null;
                drawable = str2;
                break;
            case 12:
                format = resources.getQuantityString(R.plurals.cloud_storage_warn_title_link, i, Integer.valueOf(i));
                string2 = resources.getString(R.string.cloud_action_upgrade);
                str = format;
                str2 = string2;
                drawable = null;
                break;
            case 13:
                List<Exception> expectedExceptions = syncStateInfo.getExpectedExceptions();
                if (expectedExceptions.size() > 0 && (expectedExceptions.get(0) instanceof StoragePermissionMissingException)) {
                    format = resources.getString(R.string.error_storage_no_write_permission_tip_title);
                    string2 = resources.getString(R.string.error_storage_no_write_permission_tip_action);
                    str = format;
                    str2 = string2;
                    drawable = null;
                    break;
                }
                break;
            default:
                if (i > 0) {
                    string = resources.getQuantityString(R.plurals.sync_error_title, i, Integer.valueOf(i));
                } else if (!Preference.sIsFirstSynced()) {
                    string = resources.getString(R.string.sync_metatata_wait_title);
                } else {
                    string = resources.getString(R.string.sync_state_unknow);
                }
                this.mIsErrorStatus = true;
                String string4 = resources.getString(R.string.cloud_action_view);
                str = string;
                drawable = resources.getDrawable(R.drawable.sync_error);
                str2 = string4;
                break;
        }
        boolean canAutoShowSyncBar = this.mSyncManager.canAutoShowSyncBar(syncStatus);
        boolean refresh = refresh(new TextLinkData(str, str2, drawable, 0, syncStatus, this.mSyncClickListener), canAutoShowSyncBar, this.mSyncManager.getKey());
        this.mSyncClickListener.setAutoShow(canAutoShowSyncBar);
        if (!this.mSyncManager.needShowSyncBar(syncStatus)) {
            this.mSyncManager.setCurSyncStatus(syncStatus);
        } else if (refresh) {
            this.mSyncManager.setCurSyncStatus(syncStatus);
            if (canAutoShowSyncBar) {
                this.mSyncManager.statSyncBarAutoShowEvent(syncStatus.name());
                SyncWidget.TrackStatusType.trackExpose(syncStatus, true);
            }
        }
        DefaultLogger.i("SyncTextLine", "refreshBySyncStatus:syncStatus.name=%s,syncStatus.ordinary=%s", syncStatus.name(), Integer.valueOf(syncStatus.ordinal()));
    }

    /* renamed from: com.miui.gallery.ui.SyncTextLine$1  reason: invalid class name */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass1 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus;
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$error$core$ErrorCode;

        static {
            int[] iArr = new int[SyncStatus.values().length];
            $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus = iArr;
            try {
                iArr[SyncStatus.SYNC_PENDING.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.SYNCING.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.SYSTEM_SPACE_LOW.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.SYNCING_METADATA.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.SYNC_META_PENDING.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.SYNCED.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.NO_ACCOUNT.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.MASTER_SYNC_OFF.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.SYNC_OFF.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.CTA_NOT_ALLOW.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.SYNCED_WITH_OVERSIZED_FILE.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.CLOUD_SPACE_FULL.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.EXCEPTED_ERROR.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            int[] iArr2 = new int[ErrorCode.values().length];
            $SwitchMap$com$miui$gallery$error$core$ErrorCode = iArr2;
            try {
                iArr2[ErrorCode.STORAGE_NO_WRITE_PERMISSION.ordinal()] = 1;
            } catch (NoSuchFieldError unused14) {
            }
        }
    }

    public boolean refresh(TextLinkData textLinkData, boolean z, int i) {
        boolean z2;
        Drawable drawable;
        ensureView();
        if (!this.mSyncManager.needShow() && !this.mDownloadManager.needShow()) {
            this.mPanelManager.addItem(this, false);
            this.mDescView.setVisibility(8);
            this.mActionView.setVisibility(8);
            return false;
        }
        if (i == 1) {
            this.mDownloadPanelData = textLinkData;
            z2 = this.mDownloadManager.needShow() && (!this.mSyncManager.needShow() || SyncWidget.sDownloadOverlayStatus.contains(this.mSyncManager.getCurSyncStatus()));
            if (!z2) {
                z2 = this.mSyncManager.needShow() && this.mSyncPanelData != null;
                SyncManager syncManager = this.mSyncManager;
                z = syncManager.canAutoShowSyncBar(syncManager.getCurSyncStatus());
                textLinkData = this.mSyncPanelData;
            }
        } else {
            z2 = false;
        }
        if (i == 0) {
            this.mSyncPanelData = textLinkData;
            if (this.mSyncManager.needShow()) {
                z2 = !this.mDownloadManager.needShow() || !SyncWidget.sDownloadOverlayStatus.contains(this.mSyncManager.getCurSyncStatus());
            }
            if (!z2) {
                z2 = this.mDownloadManager.needShow() && this.mDownloadPanelData != null;
                textLinkData = this.mDownloadPanelData;
            }
        }
        if (z2) {
            if (textLinkData.statusDrawable != null) {
                SpannableString spannableString = new SpannableString(" " + ((Object) textLinkData.desc));
                Drawable drawable2 = textLinkData.statusDrawable;
                drawable2.setBounds(0, 0, drawable2.getMinimumWidth(), drawable2.getMinimumHeight());
                spannableString.setSpan(new VerticalImageSpan(drawable2, 0, this.mContext.getResources().getDimensionPixelOffset(R.dimen.sync_text_line_icon_padding)), 0, 1, 33);
                this.mDescView.setText(spannableString);
            } else {
                this.mDescView.setText(textLinkData.desc);
            }
            this.mDescView.setVisibility(0);
            if (!TextUtils.isEmpty(textLinkData.actionText)) {
                this.mActionTextView.setText(textLinkData.actionText);
                this.mActionView.setVisibility(0);
                this.mActionView.setOnClickListener(textLinkData.listener);
                if (this.mIsErrorStatus) {
                    this.mActionTextView.setTextColor(this.mContext.getColor(R.color.sync_text_line_desc_color));
                    drawable = this.mContext.getDrawable(R.drawable.sync_gray_arrow);
                } else {
                    drawable = this.mContext.getDrawable(R.drawable.sync_blue_arrow);
                    this.mActionTextView.setTextColor(this.mContext.getColor(R.color.sync_text_line_action_color));
                }
                this.mActionArrow.setImageDrawable(drawable);
            } else {
                this.mActionView.setVisibility(8);
            }
            boolean isLongText = isLongText(this.mDescView);
            if (isLongText != this.mPreIsLongDesc) {
                refreshSyncActionParams(isLongText);
                this.mPreIsLongDesc = isLongText;
            }
            if (!this.mPanelManager.addItem(this, z) && this.mSyncManager.getCurSyncStatus() == SyncStatus.SYSTEM_SPACE_LOW) {
                TrackController.trackExpose("403.1.10.1.16307", "403.1.2.1.9881");
            }
        }
        return z2;
    }

    public final boolean isLongText(TextView textView) {
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, 0);
        textView.measure(makeMeasureSpec, makeMeasureSpec);
        return ((float) textView.getMeasuredWidth()) > this.mContext.getResources().getDimension(R.dimen.sync_text_line_desc_max_width);
    }

    public final void refreshSyncActionParams(boolean z) {
        if (this.mSyncView instanceof ConstraintLayout) {
            ConstraintSet constraintSet = new ConstraintSet();
            ConstraintLayout constraintLayout = (ConstraintLayout) this.mSyncView;
            constraintSet.clone(constraintLayout);
            int id = this.mDescView.getId();
            int id2 = this.mActionView.getId();
            if (z) {
                constraintSet.connect(id, 3, this.mTitleView.getId(), 4);
                constraintSet.connect(id, 6, 0, 6);
                constraintSet.connect(id, 7, 0, 7);
                constraintSet.connect(id2, 3, this.mDescView.getId(), 4);
                constraintSet.connect(id2, 6, 0, 6);
                constraintSet.connect(id2, 7, 0, 7);
                constraintSet.clear(id2, 4);
            } else {
                constraintSet.connect(id, 3, this.mTitleView.getId(), 4);
                constraintSet.connect(id, 6, 0, 6);
                constraintSet.connect(id, 7, this.mActionView.getId(), 6);
                constraintSet.connect(id2, 3, this.mDescView.getId(), 3);
                constraintSet.connect(id2, 6, this.mDescView.getId(), 7);
                constraintSet.connect(id2, 7, 0, 7);
                constraintSet.connect(id2, 4, this.mDescView.getId(), 4);
                constraintSet.setHorizontalChainStyle(id, 2);
            }
            constraintSet.applyTo(constraintLayout);
        }
    }

    public void setPhotoCountAndVideoCount(int i, int i2) {
        String quantityString = getContext().getResources().getQuantityString(R.plurals.photo_count, i, Integer.valueOf(i));
        String quantityString2 = getContext().getResources().getQuantityString(R.plurals.videos_count, i2, Integer.valueOf(i2));
        TextView textView = this.mTitleView;
        if (textView != null) {
            textView.setText(getContext().getString(R.string.photo_video_count, quantityString, quantityString2));
            if (!this.mSyncManager.needShow() && !this.mDownloadManager.needShow()) {
                this.mTitleView.setTextColor(this.mContext.getColor(R.color.sync_text_line_single_title_color));
                this.mTitleView.setTextSize(0, this.mContext.getResources().getDimensionPixelSize(R.dimen.sync_text_line_single_title_size));
                return;
            }
            this.mTitleView.setTextColor(this.mContext.getColor(R.color.sync_text_line_title_color));
            this.mTitleView.setTextSize(0, this.mContext.getResources().getDimensionPixelSize(R.dimen.sync_text_line_title_size));
        }
    }

    @Override // com.miui.gallery.widget.PanelItem
    public View getView() {
        ensureView();
        return this.mSyncView;
    }

    @Override // com.miui.gallery.widget.PanelItem
    public int getPriority() {
        return this.mPriority;
    }

    @Override // com.miui.gallery.widget.PanelItem
    public void setEnable(boolean z) {
        getView().setEnabled(z);
    }

    @Override // com.miui.gallery.widget.PanelItem
    public void setClickable(boolean z) {
        getView().setClickable(z);
        this.mActionView.setClickable(z);
    }

    @Override // com.miui.gallery.widget.PanelItem
    public void setAlpha(float f) {
        getView().setAlpha(f);
    }

    /* loaded from: classes2.dex */
    public class SyncClickListener extends SyncWidget.SyncClickListener {
        public SyncClickListener() {
            super();
        }

        @Override // com.miui.gallery.ui.SyncWidget.SyncClickListener, android.view.View.OnClickListener
        public void onClick(View view) {
            SyncStatus syncStatus = SyncStateManager.getInstance().getSyncState().getSyncStatus();
            HashMap<String, String> hashMap = new HashMap<>();
            int i = AnonymousClass1.$SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[syncStatus.ordinal()];
            if (i == 3) {
                ActionURIHandler.handleUri((FragmentActivity) SyncTextLine.this.mContext, GalleryContract.Common.URI_CLEANER_PAGE);
                GalleryPreferences.Sync.setSlimTextLinkShouldShow(Boolean.FALSE);
                TrackController.trackClick("403.1.10.1.16305", "403.1.2.1.9881");
            } else if (i == 10) {
                AgreementsUtils.showNetworkingAgreement((FragmentActivity) SyncTextLine.this.getContext(), null);
            } else if (i == 12) {
                String miCloudVipPageSource = IntentUtil.getMiCloudVipPageSource("gallery_textlink_syncfail");
                TrackController.trackClick("403.28.2.1.11288", AutoTracking.getRef());
                IntentUtil.gotoMiCloudVipPage(SyncTextLine.this.mContext, new Pair("source", miCloudVipPageSource));
            } else {
                super.onClick(view);
                return;
            }
            SyncTextLine.this.statSyncBarClickEvent(syncStatus.name(), hashMap);
            SyncTextLine.this.trackSyncClickEvent(syncStatus.name());
            SyncWidget.TrackStatusType.trackClick(syncStatus, this.autoShow);
        }
    }

    /* loaded from: classes2.dex */
    public static final class TextLinkData {
        public final CharSequence actionText;
        public final CharSequence desc;
        public final int downloadType;
        public final View.OnClickListener listener;
        public final Drawable statusDrawable;
        public final SyncStatus syncStatus;

        public TextLinkData(CharSequence charSequence, CharSequence charSequence2, Drawable drawable, int i, SyncStatus syncStatus, View.OnClickListener onClickListener) {
            this.desc = charSequence;
            this.actionText = charSequence2;
            this.statusDrawable = drawable;
            this.downloadType = i;
            this.syncStatus = syncStatus;
            this.listener = onClickListener;
        }

        public String toString() {
            return "TextLinkData{desc='" + ((Object) this.desc) + CoreConstants.SINGLE_QUOTE_CHAR + ", actionText='" + ((Object) this.actionText) + CoreConstants.SINGLE_QUOTE_CHAR + ", statusDrawable=" + this.statusDrawable + ", downloadType=" + this.downloadType + ", syncStatus=" + this.syncStatus + ", listener=" + this.listener + '}';
        }
    }
}
