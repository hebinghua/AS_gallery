package com.miui.gallery.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import ch.qos.logback.core.spi.ComponentTracker;
import com.miui.gallery.R;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.cloud.CloudUtils;
import com.miui.gallery.cloud.base.SyncRequest;
import com.miui.gallery.cloud.base.SyncType;
import com.miui.gallery.cloud.syncstate.SyncStateInfo;
import com.miui.gallery.cloud.syncstate.SyncStateManager;
import com.miui.gallery.cloud.syncstate.SyncStateUtil;
import com.miui.gallery.cloud.syncstate.SyncStatus;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.ui.BackupTitle;
import com.miui.gallery.util.DialogUtil;
import com.miui.gallery.util.FileSizeFormatter;
import com.miui.gallery.util.FormatUtil;
import com.miui.gallery.util.GalleryIntent$CloudGuideSource;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.SyncUtil;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.ColorRingProgress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public class BackupTitle extends LinearLayout {
    public static Runnable sManualResponseStat = new Runnable() { // from class: com.miui.gallery.ui.BackupTitle.2
        @Override // java.lang.Runnable
        public void run() {
            HashMap hashMap = new HashMap();
            hashMap.put("state", SyncStateManager.getInstance().getSyncState().getSyncStatus().toString());
            SamplingStatHelper.recordCountEvent("Sync", "sync_manual_response", hashMap);
        }
    };
    public SyncStatus mCurSyncStatus;
    public SyncType mCurSyncType;
    public View mDirtyContainer;
    public TextView mDirtyCountText;
    public ViewGroup mItemsContainer;
    public MenuItem.OnMenuItemClickListener mMenuItemClickListener;
    public List<PauseMenuItem> mMenuItems;
    public Animation mSyncingIconAnim;

    public final void addOperationItem(TitleData titleData, SyncStateInfo syncStateInfo) {
    }

    public BackupTitle(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BackupTitle(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mMenuItemClickListener = new MenuItem.OnMenuItemClickListener() { // from class: com.miui.gallery.ui.BackupTitle.1
            {
                BackupTitle.this = this;
            }

            @Override // android.view.MenuItem.OnMenuItemClickListener
            public boolean onMenuItemClick(MenuItem menuItem) {
                PauseMenuItem findPauseItem = BackupTitle.this.findPauseItem(menuItem.getItemId());
                if (findPauseItem != null) {
                    if (findPauseItem.hour != Integer.MAX_VALUE) {
                        DefaultLogger.i("BackupTitle", "pause %d hours", Integer.valueOf(findPauseItem.hour));
                        SyncUtil.pauseSync(BackupTitle.this.getContext(), findPauseItem.hour * 60 * 60 * 1000);
                        return true;
                    }
                    SyncUtil.setSyncAutomatically(BackupTitle.this.getContext(), false);
                    return true;
                }
                return false;
            }
        };
        this.mCurSyncType = SyncType.UNKNOW;
        this.mCurSyncStatus = SyncStatus.UNAVAILABLE;
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mItemsContainer = (ViewGroup) findViewById(R.id.item_container);
        this.mDirtyContainer = findViewById(R.id.dirty_container);
        this.mDirtyCountText = (TextView) findViewById(R.id.dirty_count_num);
    }

    /* loaded from: classes2.dex */
    public static class PauseMenuItem {
        public final int hour;
        public final int itemId;
        public final String title;

        public PauseMenuItem(String str, int i) {
            this.itemId = Math.abs(str.hashCode());
            this.title = str;
            this.hour = i;
        }
    }

    private List<PauseMenuItem> getPauseMenuItems() {
        int[] intArray;
        if (this.mMenuItems == null) {
            this.mMenuItems = new ArrayList();
            for (int i : getContext().getResources().getIntArray(R.array.backup_pause_hours)) {
                this.mMenuItems.add(new PauseMenuItem(getContext().getResources().getQuantityString(R.plurals.backup_after_hours, i, Integer.valueOf(i)), i));
            }
        }
        return this.mMenuItems;
    }

    public final PauseMenuItem findPauseItem(int i) {
        for (PauseMenuItem pauseMenuItem : this.mMenuItems) {
            if (pauseMenuItem.itemId == i) {
                return pauseMenuItem;
            }
        }
        return null;
    }

    @Override // android.view.View
    public void onCreateContextMenu(ContextMenu contextMenu) {
        super.onCreateContextMenu(contextMenu);
        List<PauseMenuItem> pauseMenuItems = getPauseMenuItems();
        for (int i = 0; i < pauseMenuItems.size(); i++) {
            PauseMenuItem pauseMenuItem = pauseMenuItems.get(i);
            contextMenu.add(0, pauseMenuItem.itemId, i, pauseMenuItem.title).setOnMenuItemClickListener(this.mMenuItemClickListener);
        }
    }

    public SyncStatus getSyncState() {
        return this.mCurSyncStatus;
    }

    public void refreshSyncState(SyncStateInfo syncStateInfo) {
        TitleData genTitleData = genTitleData(syncStateInfo);
        int size = genTitleData.mItems.size();
        int childCount = this.mItemsContainer.getChildCount();
        for (int i = size; i < childCount; i++) {
            this.mItemsContainer.removeViewAt(i);
        }
        for (int i2 = 0; i2 < size; i2++) {
            View childAt = this.mItemsContainer.getChildAt(i2);
            View view = getView(genTitleData.mItems.get(i2), childAt, this.mItemsContainer);
            if (view != childAt) {
                this.mItemsContainer.addView(view);
            }
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mItemsContainer.getLayoutParams();
        if (genTitleData.mShowDirty) {
            layoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.backup_title_dirty_item_margin_bottom);
            this.mItemsContainer.setLayoutParams(layoutParams);
            this.mDirtyContainer.setVisibility(0);
            this.mDirtyCountText.setText(getContext().getResources().getString(R.string.backup_dirty_num, String.format("%d", Integer.valueOf(genTitleData.mDirtyPhoto + genTitleData.mDirtyVideo))));
            return;
        }
        layoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.backup_title_item_margin_bottom);
        this.mItemsContainer.setLayoutParams(layoutParams);
        this.mDirtyContainer.setVisibility(8);
    }

    public final View getView(TitleItemData titleItemData, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.backup_title_item, viewGroup, false);
            TitleItemViewHolder titleItemViewHolder = new TitleItemViewHolder();
            titleItemViewHolder.mIcon = (ImageView) view.findViewById(R.id.icon);
            titleItemViewHolder.mProgress = (ColorRingProgress) view.findViewById(R.id.progress);
            titleItemViewHolder.mTitle = (TextView) view.findViewById(R.id.title);
            titleItemViewHolder.mDesc = (TextView) view.findViewById(R.id.summary);
            titleItemViewHolder.mTextContainer = view.findViewById(R.id.text_container);
            TextView textView = (TextView) view.findViewById(R.id.btn);
            titleItemViewHolder.mBtn = textView;
            FolmeUtil.setDefaultTouchAnim(textView, null, false, false, true);
            view.setTag(titleItemViewHolder);
        }
        TitleItemViewHolder titleItemViewHolder2 = (TitleItemViewHolder) view.getTag();
        titleItemViewHolder2.mIcon.setImageResource(titleItemData.mIconRes);
        titleItemViewHolder2.mTitle.setText(titleItemData.mTitle);
        titleItemViewHolder2.mTextContainer.setOnClickListener(titleItemData.mTitleClickListener);
        if (titleItemData.mTitleClickListener != null) {
            titleItemViewHolder2.mTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.backup_title_arrow, 0);
        } else {
            titleItemViewHolder2.mTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        }
        if (TextUtils.isEmpty(titleItemData.mDesc)) {
            titleItemViewHolder2.mDesc.setVisibility(8);
        } else {
            titleItemViewHolder2.mDesc.setVisibility(0);
            titleItemViewHolder2.mDesc.setText(titleItemData.mDesc);
        }
        if (TextUtils.isEmpty(titleItemData.mBtnTxt)) {
            titleItemViewHolder2.mBtn.setVisibility(8);
        } else {
            titleItemViewHolder2.mBtn.setVisibility(0);
            titleItemViewHolder2.mBtn.setText(titleItemData.mBtnTxt);
            titleItemViewHolder2.mBtn.setBackgroundResource(titleItemData.mBtnBgRes);
            titleItemViewHolder2.mBtn.setOnClickListener(titleItemData.mBtnClickListener);
        }
        if (titleItemData.mProgress > 0.0f) {
            titleItemViewHolder2.mProgress.setVisibility(0);
            titleItemViewHolder2.mProgress.setProgress(titleItemData.mProgress);
        } else {
            titleItemViewHolder2.mProgress.setVisibility(4);
        }
        if (titleItemData.mIconAnim != null) {
            Animation animation = titleItemViewHolder2.mIcon.getAnimation();
            if (animation == null || !animation.hasStarted() || animation.hasEnded()) {
                DefaultLogger.d("BackupTitle", "startAnimation");
                titleItemViewHolder2.mIcon.startAnimation(titleItemData.mIconAnim);
            }
        } else {
            titleItemViewHolder2.mIcon.clearAnimation();
            DefaultLogger.d("BackupTitle", "clearAnimation");
        }
        return view;
    }

    public final TitleData genTitleData(SyncStateInfo syncStateInfo) {
        TitleData titleData = new TitleData();
        addSyncItem(titleData, syncStateInfo);
        addOperationItem(titleData, syncStateInfo);
        addDirtyItem(titleData, syncStateInfo);
        return titleData;
    }

    /* renamed from: com.miui.gallery.ui.BackupTitle$3 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass3 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus;

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
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.SYNCING_METADATA.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.SYNC_META_PENDING.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.SYNCED.ordinal()] = 5;
            } catch (NoSuchFieldError unused5) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.SYNC_PAUSE.ordinal()] = 6;
            } catch (NoSuchFieldError unused6) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.SYNC_ERROR.ordinal()] = 7;
            } catch (NoSuchFieldError unused7) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.UNKNOWN_ERROR.ordinal()] = 8;
            } catch (NoSuchFieldError unused8) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.NO_ACCOUNT.ordinal()] = 9;
            } catch (NoSuchFieldError unused9) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.MASTER_SYNC_OFF.ordinal()] = 10;
            } catch (NoSuchFieldError unused10) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.SYNC_OFF.ordinal()] = 11;
            } catch (NoSuchFieldError unused11) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.CTA_NOT_ALLOW.ordinal()] = 12;
            } catch (NoSuchFieldError unused12) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.MI_MOVER_RUNNING.ordinal()] = 13;
            } catch (NoSuchFieldError unused13) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.DISCONNECTED.ordinal()] = 14;
            } catch (NoSuchFieldError unused14) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.NO_WIFI.ordinal()] = 15;
            } catch (NoSuchFieldError unused15) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.BATTERY_LOW.ordinal()] = 16;
            } catch (NoSuchFieldError unused16) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.SYSTEM_SPACE_LOW.ordinal()] = 17;
            } catch (NoSuchFieldError unused17) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.CLOUD_SPACE_FULL.ordinal()] = 18;
            } catch (NoSuchFieldError unused18) {
            }
            try {
                $SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[SyncStatus.SYNCED_WITH_OVERSIZED_FILE.ordinal()] = 19;
            } catch (NoSuchFieldError unused19) {
            }
        }
    }

    public final void addDirtyItem(TitleData titleData, SyncStateInfo syncStateInfo) {
        int i = AnonymousClass3.$SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[syncStateInfo.getSyncStatus().ordinal()];
        boolean z = false;
        if (i == 1 || i == 2 || i == 3 || i == 4 || i == 5) {
            titleData.mShowDirty = false;
            return;
        }
        int[] dirtyCount = syncStateInfo.getDirtyCount();
        titleData.mDirtyPhoto = dirtyCount[0];
        titleData.mDirtyVideo = dirtyCount[1];
        if (dirtyCount[0] + dirtyCount[1] > 0) {
            z = true;
        }
        titleData.mShowDirty = z;
    }

    public final void addSyncItem(TitleData titleData, SyncStateInfo syncStateInfo) {
        TitleItemData titleItemData = new TitleItemData();
        Resources resources = getContext().getResources();
        int[] dirtyCount = syncStateInfo.getDirtyCount();
        int i = dirtyCount[0] + dirtyCount[1];
        DefaultLogger.d("BackupTitle", "refresh status: %s", syncStateInfo.getSyncStatus());
        SyncStatus syncStatus = syncStateInfo.getSyncStatus();
        switch (AnonymousClass3.$SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[syncStatus.ordinal()]) {
            case 1:
                titleItemData.setTitle(resources.getString(R.string.backup_pending));
                titleItemData.setBtnTxt(resources.getString(R.string.backup_immediately));
            case 3:
            case 4:
                if (i == 0) {
                    titleItemData.setTitle(resources.getString(R.string.syncing_metadata_title));
                }
            case 2:
                titleItemData.setIconRes(R.drawable.backup_icon_syncing).setBtnClickListener(genClickListener(syncStateInfo));
                SyncType syncType = syncStateInfo.getSyncType();
                SyncType syncType2 = SyncType.GPRS_FORCE;
                if (syncType == syncType2) {
                    if (titleItemData.mTitle == null) {
                        titleItemData.setTitle(resources.getString(R.string.backuping_title_gprs));
                    }
                    if (i > 0) {
                        titleItemData.setDesc(resources.getQuantityString(R.plurals.backuping_desc_gprs, i, Integer.valueOf(i), FormatUtil.formatFileSize(getContext(), syncStateInfo.getDirtySize())));
                    }
                } else {
                    if (titleItemData.mTitle == null) {
                        titleItemData.setTitle(resources.getString(R.string.backuping_title_normal));
                    }
                    if (i > 0) {
                        titleItemData.setDesc(resources.getQuantityString(R.plurals.backuping_desc_normal, i, Integer.valueOf(i)));
                    }
                }
                if (titleItemData.mBtnTxt == null) {
                    if (syncType == syncType2) {
                        titleItemData.setBtnTxt(resources.getString(R.string.backup_stop));
                    } else {
                        titleItemData.setBtnTxt(resources.getString(R.string.backup_pause));
                    }
                }
                titleItemData.setBtnBgRes(R.drawable.custom_page_bar_edit_background);
                if (syncStatus != SyncStatus.SYNC_PENDING) {
                    titleItemData.setIconAnim(getSyncingAnim());
                    break;
                }
                break;
            case 5:
                titleItemData.setIconRes(R.drawable.backup_icon_synced).setTitle(resources.getString(R.string.backuped_title)).setDesc(resources.getString(R.string.backuped_desc));
                break;
            case 6:
                titleItemData.setIconRes(R.drawable.backup_icon_syncing).setTitle(resources.getString(R.string.backup_pause_title)).setBtnTxt(resources.getString(R.string.backup_immediately)).setBtnBgRes(R.drawable.custom_page_bar_edit_background).setBtnClickListener(genClickListener(syncStateInfo));
                int formatMinutes = FormatUtil.formatMinutes(syncStateInfo.getResumeInterval());
                titleItemData.setDesc(resources.getQuantityString(R.plurals.backup_pause_desc, formatMinutes, Integer.valueOf(formatMinutes)));
                break;
            case 7:
                titleItemData.setIconRes(R.drawable.backup_icon_sync_error).setTitle(resources.getString(R.string.backup_sync_error_title)).setDesc(resources.getString(R.string.backup_sync_error_desc)).setBtnTxt(resources.getString(R.string.backup_sync_error_btn_text)).setBtnBgRes(R.drawable.custom_page_bar_edit_background).setBtnClickListener(genClickListener(syncStateInfo));
                break;
            case 8:
                titleItemData.setIconRes(R.drawable.backup_icon_sync_error).setTitle(resources.getString(R.string.backup_need_sync_title)).setDesc(resources.getString(R.string.backup_need_sync_desc)).setBtnTxt(resources.getString(R.string.backup_immediately)).setBtnBgRes(R.drawable.custom_page_bar_edit_background).setBtnClickListener(genClickListener(syncStateInfo));
                break;
            case 9:
            case 10:
            case 11:
                titleItemData.setIconRes(R.drawable.backup_icon_sync_setting_off).setTitle(resources.getString(R.string.backup_sync_off_title)).setDesc(resources.getString(R.string.backup_sync_off_desc)).setBtnTxt(resources.getString(R.string.backup_sync_off_btn_text)).setBtnBgRes(R.drawable.custom_page_bar_edit_background).setBtnClickListener(genClickListener(syncStateInfo));
                break;
            case 12:
                titleItemData.setIconRes(R.drawable.backup_icon_net_error).setTitle(resources.getString(R.string.backup_cta_not_allow_title)).setDesc(resources.getString(R.string.backup_cta_not_allow_desc)).setBtnTxt(resources.getString(R.string.backup_cta_not_allow_btn_text)).setBtnBgRes(R.drawable.custom_page_bar_edit_background).setBtnClickListener(genClickListener(syncStateInfo));
                break;
            case 13:
                titleItemData.setIconRes(R.drawable.backup_icon_sync_setting_off).setTitle(resources.getString(R.string.backup_pause_title)).setDesc(resources.getString(R.string.mi_mover_working_desc));
                break;
            case 14:
                titleItemData.setIconRes(R.drawable.backup_icon_net_error).setTitle(resources.getString(R.string.backup_no_network_title)).setDesc(resources.getString(R.string.backup_no_network_desc)).setBtnTxt(resources.getString(R.string.backup_no_network_btn_text)).setBtnBgRes(R.drawable.custom_page_bar_edit_background).setBtnClickListener(genClickListener(syncStateInfo));
                break;
            case 15:
                titleItemData.setIconRes(R.drawable.backup_icon_net_error).setTitle(resources.getString(R.string.backup_no_wifi_title)).setDesc(resources.getString(R.string.backup_no_wifi_desc)).setBtnTxt(resources.getString(R.string.backup_no_wifi_btn_text)).setBtnBgRes(R.drawable.custom_page_bar_edit_background).setBtnClickListener(genClickListener(syncStateInfo));
                break;
            case 16:
                titleItemData.setIconRes(R.drawable.backup_icon_battery_low).setTitle(resources.getString(R.string.backup_battery_low_title)).setDesc(resources.getString(R.string.backup_battery_low_desc)).setBtnTxt(resources.getString(R.string.backup_immediately)).setBtnBgRes(R.drawable.custom_page_bar_edit_background).setBtnClickListener(genClickListener(syncStateInfo));
                break;
            case 17:
                titleItemData.setIconRes(R.drawable.backup_icon_device_storage_low).setTitle(resources.getString(R.string.backup_device_space_low_title)).setDesc(resources.getString(R.string.backup_device_space_low_desc)).setBtnTxt(resources.getString(R.string.backup_device_space_low_btn_text)).setBtnBgRes(R.drawable.custom_page_bar_edit_background).setBtnClickListener(genClickListener(syncStateInfo));
                break;
            case 18:
                titleItemData.setIconRes(R.drawable.backup_space_progress_background).setTitle(resources.getString(R.string.backup_cloud_space_low_title)).setDesc(resources.getString(R.string.backup_cloud_space_low_desc, SyncStateUtil.getQuantityStringWithUnit(syncStateInfo.getCloudSpaceTotalSize()), SyncStateUtil.getQuantityStringWithUnit(syncStateInfo.getCloudSpaceRemainingSize()))).setBtnTxt(resources.getString(R.string.backup_cloud_space_low_btn_text)).setBtnBgRes(R.drawable.custom_page_bar_edit_background).setBtnClickListener(genClickListener(syncStateInfo));
                if (syncStateInfo.getCloudSpaceTotalSize() > 0) {
                    float cloudSpaceRemainingSize = 1.0f - ((((float) syncStateInfo.getCloudSpaceRemainingSize()) * 1.0f) / ((float) syncStateInfo.getCloudSpaceTotalSize()));
                    if (cloudSpaceRemainingSize > 0.99d) {
                        titleItemData.setIconRes(R.drawable.backup_space_full_background);
                    }
                    titleItemData.setProgress(cloudSpaceRemainingSize);
                    break;
                }
                break;
            case 19:
                titleItemData.setIconRes(R.drawable.backup_icon_sync_error).setTitle(resources.getQuantityString(R.plurals.backup_file_over_size_title, i, Integer.valueOf(i))).setDesc(resources.getString(R.string.backup_file_over_size_desc, FileSizeFormatter.formatShortFileSize(getContext(), CloudUtils.getMaxImageSizeLimit()), FileSizeFormatter.formatShortFileSize(getContext(), CloudUtils.getMaxVideoSizeLimit())));
                break;
        }
        if (syncStatus != SyncStatus.UNAVAILABLE) {
            titleData.mItems.add(titleItemData);
        }
        tryStatSyncStateError(syncStateInfo);
    }

    public boolean isNormalSyncStatus(SyncStatus syncStatus) {
        return syncStatus == SyncStatus.SYNC_PENDING || syncStatus == SyncStatus.SYNCING || syncStatus == SyncStatus.SYNCING_METADATA || syncStatus == SyncStatus.SYNC_META_PENDING || syncStatus == SyncStatus.SYNCED;
    }

    public final void tryStatSyncStateError(SyncStateInfo syncStateInfo) {
        SyncStatus syncStatus = syncStateInfo.getSyncStatus();
        if (!isNormalSyncStatus(syncStatus) && (this.mCurSyncType != syncStateInfo.getSyncType() || this.mCurSyncStatus != syncStatus)) {
            HashMap hashMap = new HashMap();
            hashMap.put("error", syncStateInfo.getSyncType().name() + "_" + syncStatus.name());
            SamplingStatHelper.recordCountEvent("Sync", "sync_state_error", hashMap);
        }
        this.mCurSyncType = syncStateInfo.getSyncType();
        this.mCurSyncStatus = syncStatus;
    }

    public final void tryStatSyncStateCorrect(String str) {
        SyncStatus syncStatus = this.mCurSyncStatus;
        if (syncStatus == null || this.mCurSyncType == null || isNormalSyncStatus(syncStatus)) {
            return;
        }
        HashMap hashMap = new HashMap();
        hashMap.put("error_correct", this.mCurSyncType.name() + "_" + this.mCurSyncStatus.name() + "_" + str);
        SamplingStatHelper.recordCountEvent("Sync", "sync_state_error_correct", hashMap);
    }

    private Animation getSyncingAnim() {
        if (this.mSyncingIconAnim == null) {
            this.mSyncingIconAnim = AnimationUtils.loadAnimation(getContext(), R.anim.photo_status_syncing_rotate_anim);
        }
        return this.mSyncingIconAnim;
    }

    public final SyncType getImmediateSyncType(SyncStateInfo syncStateInfo) {
        if (syncStateInfo.getSyncStatus() == SyncStatus.NO_WIFI) {
            return SyncType.GPRS_FORCE;
        }
        return SyncType.POWER_FORCE;
    }

    public final View.OnClickListener genClickListener(SyncStateInfo syncStateInfo) {
        switch (AnonymousClass3.$SwitchMap$com$miui$gallery$cloud$syncstate$SyncStatus[syncStateInfo.getSyncStatus().ordinal()]) {
            case 1:
            case 4:
            case 7:
            case 8:
            case 15:
            case 16:
                return new SyncImmediateListener(getImmediateSyncType(syncStateInfo));
            case 2:
            case 3:
                if (syncStateInfo.getSyncType() == SyncType.GPRS_FORCE) {
                    return new StopSyncListener();
                }
                return new PauseSyncListener();
            case 5:
            case 13:
            default:
                return null;
            case 6:
                return new ResumeSyncListener(getImmediateSyncType(syncStateInfo));
            case 9:
                return new LoginAccountListener();
            case 10:
            case 11:
                return new OpenSyncSwitchListener();
            case 12:
                return new SetCTAListener();
            case 14:
                return new SetNetworkListener();
            case 17:
                return new SlimDeviceSpaceListener();
            case 18:
                return new ExpanseCloudSpaceListener();
        }
    }

    /* loaded from: classes2.dex */
    public abstract class BaseClickListener implements View.OnClickListener {
        public abstract void onCustomClick(View view);

        public BaseClickListener() {
            BackupTitle.this = r1;
        }

        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            onCustomClick(view);
            BackupTitle.this.tryStatSyncStateCorrect(getClass().getSimpleName());
        }
    }

    /* loaded from: classes2.dex */
    public class StopSyncListener extends BaseClickListener {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public StopSyncListener() {
            super();
            BackupTitle.this = r2;
        }

        @Override // com.miui.gallery.ui.BackupTitle.BaseClickListener
        public void onCustomClick(View view) {
            SyncUtil.stopSync(BackupTitle.this.getContext());
        }
    }

    /* loaded from: classes2.dex */
    public class PauseSyncListener extends BaseClickListener {
        /* renamed from: $r8$lambda$4FO3_zAZr4YYVhV94831yH5-_WI */
        public static /* synthetic */ void m1429$r8$lambda$4FO3_zAZr4YYVhV94831yH5_WI(PauseSyncListener pauseSyncListener, DialogInterface dialogInterface, int i) {
            pauseSyncListener.lambda$onCustomClick$0(dialogInterface, i);
        }

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public PauseSyncListener() {
            super();
            BackupTitle.this = r2;
        }

        @Override // com.miui.gallery.ui.BackupTitle.BaseClickListener
        public void onCustomClick(View view) {
            String string = BackupTitle.this.getResources().getString(R.string.backup_pause_dialog_title);
            int formatMinutes = FormatUtil.formatMinutes(ComponentTracker.DEFAULT_TIMEOUT);
            DialogUtil.showInfoDialog(BackupTitle.this.getContext(), BackupTitle.this.getResources().getQuantityString(R.plurals.backup_pause_dialog_message, formatMinutes, Integer.valueOf(formatMinutes)), string, (int) R.string.ok, (int) R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.BackupTitle$PauseSyncListener$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    BackupTitle.PauseSyncListener.m1429$r8$lambda$4FO3_zAZr4YYVhV94831yH5_WI(BackupTitle.PauseSyncListener.this, dialogInterface, i);
                }
            }, (DialogInterface.OnClickListener) null);
            TrackController.trackClick("403.28.2.1.11284", AutoTracking.getRef());
        }

        public /* synthetic */ void lambda$onCustomClick$0(DialogInterface dialogInterface, int i) {
            SyncUtil.pauseSync(BackupTitle.this.getContext(), ComponentTracker.DEFAULT_TIMEOUT);
        }
    }

    /* loaded from: classes2.dex */
    public class SyncImmediateListener extends BaseClickListener {
        public SyncType mSyncType;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SyncImmediateListener(SyncType syncType) {
            super();
            BackupTitle.this = r2;
            this.mSyncType = syncType;
        }

        @Override // com.miui.gallery.ui.BackupTitle.BaseClickListener
        public void onCustomClick(View view) {
            SyncType syncType = this.mSyncType;
            if (syncType != SyncType.GPRS_FORCE) {
                BackupTitle.this.requestSync(syncType);
                TrackController.trackClick("403.28.2.1.11287", AutoTracking.getRef());
                return;
            }
            Resources resources = BackupTitle.this.getContext().getResources();
            DialogUtil.showInfoDialog(BackupTitle.this.getContext(), resources.getString(R.string.backup_with_gprs_dialog_message, FormatUtil.formatFileSize(BackupTitle.this.getContext(), SyncStateManager.getInstance().getSyncState().getDirtySize())), resources.getString(R.string.backup_with_gprs_dialog_title), (int) R.string.backup_with_gprs_dialog_btn, (int) R.string.cancel, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.BackupTitle.SyncImmediateListener.1
                {
                    SyncImmediateListener.this = this;
                }

                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    SyncImmediateListener syncImmediateListener = SyncImmediateListener.this;
                    BackupTitle.this.requestSync(syncImmediateListener.mSyncType);
                }
            }, (DialogInterface.OnClickListener) null);
        }
    }

    /* loaded from: classes2.dex */
    public class ResumeSyncListener extends SyncImmediateListener {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ResumeSyncListener(SyncType syncType) {
            super(syncType);
            BackupTitle.this = r1;
        }

        @Override // com.miui.gallery.ui.BackupTitle.SyncImmediateListener, com.miui.gallery.ui.BackupTitle.BaseClickListener
        public void onCustomClick(View view) {
            SyncUtil.resumeSync(BackupTitle.this.getContext());
            super.onCustomClick(view);
            TrackController.trackClick("403.28.2.1.11286", AutoTracking.getRef());
        }
    }

    public final void requestSync(SyncType syncType) {
        SyncUtil.requestSync(getContext(), new SyncRequest.Builder().setSyncType(syncType).setSyncReason(Long.MAX_VALUE).setDelayUpload(false).setManual(true).build());
        statManualResponse();
    }

    public final void statManualResponse() {
        ThreadManager.getWorkHandler().removeCallbacks(sManualResponseStat);
        ThreadManager.getWorkHandler().postDelayed(sManualResponseStat, 500L);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ThreadManager.getWorkHandler().removeCallbacks(sManualResponseStat);
    }

    /* loaded from: classes2.dex */
    public class LoginAccountListener extends BaseClickListener {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public LoginAccountListener() {
            super();
            BackupTitle.this = r2;
        }

        @Override // com.miui.gallery.ui.BackupTitle.BaseClickListener
        public void onCustomClick(View view) {
            IntentUtil.guideToLoginXiaomiAccount(BackupTitle.this.getContext(), GalleryIntent$CloudGuideSource.TOPBAR);
        }
    }

    /* loaded from: classes2.dex */
    public class OpenSyncSwitchListener extends BaseClickListener {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public OpenSyncSwitchListener() {
            super();
            BackupTitle.this = r2;
        }

        @Override // com.miui.gallery.ui.BackupTitle.BaseClickListener
        public void onCustomClick(View view) {
            IntentUtil.gotoTurnOnSyncSwitchInner(BackupTitle.this.getContext());
        }
    }

    /* loaded from: classes2.dex */
    public class SetNetworkListener extends BaseClickListener {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SetNetworkListener() {
            super();
            BackupTitle.this = r2;
        }

        @Override // com.miui.gallery.ui.BackupTitle.BaseClickListener
        public void onCustomClick(View view) {
            BackupTitle.this.getContext().startActivity(new Intent("android.settings.SETTINGS"));
            TrackController.trackClick("403.28.2.1.11285", AutoTracking.getRef());
        }
    }

    /* loaded from: classes2.dex */
    public class SetCTAListener extends BaseClickListener {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SetCTAListener() {
            super();
            BackupTitle.this = r2;
        }

        @Override // com.miui.gallery.ui.BackupTitle.BaseClickListener
        public void onCustomClick(View view) {
            AgreementsUtils.showNetworkingAgreement((FragmentActivity) BackupTitle.this.getContext(), null);
        }
    }

    /* loaded from: classes2.dex */
    public class ExpanseCloudSpaceListener extends BaseClickListener {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public ExpanseCloudSpaceListener() {
            super();
            BackupTitle.this = r2;
        }

        @Override // com.miui.gallery.ui.BackupTitle.BaseClickListener
        public void onCustomClick(View view) {
            TrackController.trackClick("403.28.2.1.11288", AutoTracking.getRef());
            IntentUtil.gotoMiCloudVipPage(BackupTitle.this.getContext(), new Pair("source", IntentUtil.getMiCloudVipPageSource("gallery_textlink_syncfail")));
        }
    }

    /* loaded from: classes2.dex */
    public class SlimDeviceSpaceListener extends BaseClickListener {
        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public SlimDeviceSpaceListener() {
            super();
            BackupTitle.this = r2;
        }

        @Override // com.miui.gallery.ui.BackupTitle.BaseClickListener
        public void onCustomClick(View view) {
            IntentUtil.gotoDeepClean(BackupTitle.this.getContext());
        }
    }

    /* loaded from: classes2.dex */
    public static class TitleItemData {
        public int mBtnBgRes;
        public View.OnClickListener mBtnClickListener;
        public CharSequence mBtnTxt;
        public CharSequence mDesc;
        public Animation mIconAnim;
        public int mIconRes;
        public float mProgress;
        public CharSequence mTitle;
        public View.OnClickListener mTitleClickListener;

        public TitleItemData() {
            this.mProgress = 0.0f;
        }

        public TitleItemData setIconRes(int i) {
            this.mIconRes = i;
            return this;
        }

        public TitleItemData setTitle(CharSequence charSequence) {
            this.mTitle = charSequence;
            return this;
        }

        public TitleItemData setDesc(CharSequence charSequence) {
            this.mDesc = charSequence;
            return this;
        }

        public TitleItemData setBtnTxt(CharSequence charSequence) {
            this.mBtnTxt = charSequence;
            return this;
        }

        public TitleItemData setBtnBgRes(int i) {
            this.mBtnBgRes = i;
            return this;
        }

        public TitleItemData setBtnClickListener(View.OnClickListener onClickListener) {
            this.mBtnClickListener = onClickListener;
            return this;
        }

        public TitleItemData setIconAnim(Animation animation) {
            this.mIconAnim = animation;
            return this;
        }

        public TitleItemData setProgress(float f) {
            this.mProgress = f;
            return this;
        }
    }

    /* loaded from: classes2.dex */
    public static class TitleItemViewHolder {
        public TextView mBtn;
        public TextView mDesc;
        public ImageView mIcon;
        public ColorRingProgress mProgress;
        public View mTextContainer;
        public TextView mTitle;

        public TitleItemViewHolder() {
        }
    }

    /* loaded from: classes2.dex */
    public static class TitleData {
        public int mDirtyPhoto;
        public int mDirtyVideo;
        public List<TitleItemData> mItems;
        public boolean mShowDirty;

        public TitleData() {
            this.mItems = new LinkedList();
        }
    }
}
