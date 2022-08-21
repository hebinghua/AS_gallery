package com.miui.gallery.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.cloud.syncstate.SyncStateUtil;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureHandler;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.widget.RoundRectProgressView;

/* loaded from: classes2.dex */
public class CloudBackupStatusPreference extends Preference implements View.OnClickListener {
    public boolean mAutoBackup;
    public View mBackupContainer;
    public CloudButtonClickListener mCloudClickListener;
    public RoundRectProgressView mCloudSpaceBar;
    public TextView mCloudSpaceLeft;
    public TextView mCloudSpaceTotal;
    public View mDisableContainer;
    public View mEnableContainer;
    public boolean mForceSplit;
    public View mOpenButton;
    public View mRoot;
    public View mStorageContainer;
    public Future mStorageFuture;
    public long mTotalCloudSpace;
    public String mTotalSpaceStorage;
    public long mUseCloudStorage;
    public boolean mUseDialog;
    public String mUseSpaceStorage;

    /* loaded from: classes2.dex */
    public interface CloudButtonClickListener {
        void onBackupClick();

        void onSpaceClick();
    }

    public CloudBackupStatusPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public CloudBackupStatusPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setLayoutResource(R.layout.cloud_backup_status_preference);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        this.mRoot = preferenceViewHolder.findViewById(R.id.root_view);
        this.mDisableContainer = preferenceViewHolder.findViewById(R.id.disable_container);
        this.mStorageContainer = preferenceViewHolder.findViewById(R.id.space_container);
        this.mEnableContainer = preferenceViewHolder.findViewById(R.id.enable_container);
        this.mBackupContainer = preferenceViewHolder.findViewById(R.id.backup_container);
        this.mOpenButton = preferenceViewHolder.findViewById(R.id.open_button);
        this.mCloudSpaceLeft = (TextView) preferenceViewHolder.findViewById(R.id.storage_left_detail);
        this.mCloudSpaceTotal = (TextView) preferenceViewHolder.findViewById(R.id.storage_total_detail);
        this.mCloudSpaceBar = (RoundRectProgressView) preferenceViewHolder.findViewById(R.id.storage_bar);
        int i = 8;
        this.mEnableContainer.setVisibility(this.mAutoBackup ? 0 : 8);
        View view = this.mDisableContainer;
        if (!this.mAutoBackup) {
            i = 0;
        }
        view.setVisibility(i);
        this.mOpenButton.setOnClickListener(this);
        this.mStorageContainer.setOnClickListener(this);
        this.mBackupContainer.setOnClickListener(this);
        View view2 = this.mStorageContainer;
        FolmeUtil.setDefaultTouchAnim(view2, view2, null, false, true, false);
        View view3 = this.mBackupContainer;
        FolmeUtil.setDefaultTouchAnim(view3, view3, null, false, true, false);
        FolmeUtil.setDefaultTouchAnim(this.mOpenButton, null, false, false, true);
        onConfigChanged(getContext().getResources().getConfiguration(), this.mUseDialog, this.mForceSplit);
        if (this.mAutoBackup) {
            initSpaceBar();
        }
    }

    public final void initSpaceBar() {
        View view = this.mEnableContainer;
        if (view == null || view.getVisibility() == 8 || this.mStorageFuture != null) {
            return;
        }
        this.mStorageFuture = ThreadManager.getMiscPool().submit(new ThreadPool.Job<SyncStateUtil.CloudSpaceInfo>() { // from class: com.miui.gallery.ui.CloudBackupStatusPreference.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.concurrent.ThreadPool.Job
            /* renamed from: run */
            public SyncStateUtil.CloudSpaceInfo mo1807run(ThreadPool.JobContext jobContext) {
                return SyncStateUtil.getCloudSpaceInfo(GalleryApp.sGetAndroidContext());
            }
        }, new FutureHandler<SyncStateUtil.CloudSpaceInfo>() { // from class: com.miui.gallery.ui.CloudBackupStatusPreference.2
            @Override // com.miui.gallery.concurrent.FutureHandler
            public void onPostExecute(Future<SyncStateUtil.CloudSpaceInfo> future) {
                if (future == null || future.isCancelled()) {
                    return;
                }
                SyncStateUtil.CloudSpaceInfo cloudSpaceInfo = future.get();
                CloudBackupStatusPreference.this.mUseCloudStorage = cloudSpaceInfo.getUsed();
                CloudBackupStatusPreference.this.mTotalCloudSpace = cloudSpaceInfo.getTotal();
                CloudBackupStatusPreference cloudBackupStatusPreference = CloudBackupStatusPreference.this;
                cloudBackupStatusPreference.mUseSpaceStorage = SyncStateUtil.getQuantityStringWithUnit(cloudBackupStatusPreference.mUseCloudStorage);
                CloudBackupStatusPreference cloudBackupStatusPreference2 = CloudBackupStatusPreference.this;
                cloudBackupStatusPreference2.mTotalSpaceStorage = SyncStateUtil.getQuantityStringWithUnit(cloudBackupStatusPreference2.mTotalCloudSpace);
                CloudBackupStatusPreference.this.configCloudSpaceBar();
            }
        });
    }

    public final void configCloudSpaceBar() {
        TextView textView;
        if (this.mCloudSpaceBar == null || (textView = this.mCloudSpaceLeft) == null || this.mCloudSpaceTotal == null) {
            return;
        }
        textView.setText(this.mUseSpaceStorage);
        this.mCloudSpaceTotal.setText(this.mTotalSpaceStorage);
        this.mCloudSpaceBar.setProgress(this.mUseCloudStorage, this.mTotalCloudSpace);
    }

    public void setAutoBackupStatus(boolean z) {
        if (this.mAutoBackup != z) {
            this.mAutoBackup = z;
            View view = this.mEnableContainer;
            int i = 0;
            if (view != null) {
                view.setVisibility(z ? 0 : 8);
            }
            View view2 = this.mDisableContainer;
            if (view2 != null) {
                if (this.mAutoBackup) {
                    i = 8;
                }
                view2.setVisibility(i);
            }
            if (!this.mAutoBackup) {
                return;
            }
            initSpaceBar();
        }
    }

    public void setUseDialog(boolean z) {
        this.mUseDialog = z;
    }

    public void setForceSplit(boolean z) {
        this.mForceSplit = z;
    }

    public void onConfigChanged(Configuration configuration, boolean z, boolean z2) {
        int max;
        int dimensionPixelOffset;
        Context context = getContext();
        if (z) {
            max = context.getResources().getDimensionPixelSize(configuration.orientation == 1 ? R.dimen.setting_dialog_width : R.dimen.setting_dialog_width_land);
        } else if (context instanceof Activity) {
            max = ((Activity) getContext()).getWindow().getAttributes().width;
            if (max <= 0) {
                max = ScreenUtils.getCurDisplayWidth((Activity) getContext());
            }
        } else {
            int screenHeight = ScreenUtils.getScreenHeight();
            int screenWidth = ScreenUtils.getScreenWidth();
            max = configuration.orientation == 2 ? Math.max(screenHeight, screenWidth) : Math.min(screenHeight, screenWidth);
        }
        if (z2) {
            dimensionPixelOffset = getContext().getResources().getDimensionPixelOffset(R.dimen.miuix_appcompat_window_extra_padding_horizontal_small);
        } else if (z) {
            dimensionPixelOffset = getContext().getResources().getDimensionPixelOffset(R.dimen.floating_cloud_backup_space_container_margin);
        } else {
            dimensionPixelOffset = getContext().getResources().getDimensionPixelOffset(R.dimen.cloud_backup_space_container_margin);
        }
        View view = this.mEnableContainer;
        if (view != null) {
            FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) view.getLayoutParams();
            layoutParams.setMarginStart(dimensionPixelOffset);
            this.mEnableContainer.setLayoutParams(layoutParams);
        }
        View view2 = this.mDisableContainer;
        if (view2 != null) {
            FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) view2.getLayoutParams();
            layoutParams2.setMarginStart(dimensionPixelOffset);
            layoutParams2.setMarginEnd(dimensionPixelOffset);
            this.mDisableContainer.setLayoutParams(layoutParams2);
        }
        int dimensionPixelOffset2 = getContext().getResources().getDimensionPixelOffset(R.dimen.cloud_backup_space_container_divider);
        int i = ((max - (dimensionPixelOffset * 2)) - dimensionPixelOffset2) / 2;
        if (this.mStorageContainer == null || this.mBackupContainer == null) {
            return;
        }
        this.mStorageContainer.setLayoutParams(new FrameLayout.LayoutParams(i, -1));
        FrameLayout.LayoutParams layoutParams3 = new FrameLayout.LayoutParams(i, -1);
        layoutParams3.setMarginStart(i + dimensionPixelOffset2);
        this.mBackupContainer.setLayoutParams(layoutParams3);
        LinearLayout.LayoutParams layoutParams4 = new LinearLayout.LayoutParams(-1, getContext().getResources().getDimensionPixelOffset(R.dimen.cloud_backup_space_bar_height));
        RoundRectProgressView roundRectProgressView = this.mCloudSpaceBar;
        if (roundRectProgressView != null) {
            roundRectProgressView.setLayoutParams(layoutParams4);
        }
        configCloudSpaceBar();
    }

    public boolean getAutoBackupStatus() {
        return this.mAutoBackup;
    }

    public void setCloudClickListener(CloudButtonClickListener cloudButtonClickListener) {
        this.mCloudClickListener = cloudButtonClickListener;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R.id.space_container) {
            CloudButtonClickListener cloudButtonClickListener = this.mCloudClickListener;
            if (cloudButtonClickListener == null) {
                return;
            }
            cloudButtonClickListener.onSpaceClick();
            return;
        }
        CloudButtonClickListener cloudButtonClickListener2 = this.mCloudClickListener;
        if (cloudButtonClickListener2 == null) {
            return;
        }
        cloudButtonClickListener2.onBackupClick();
    }

    public void cancel() {
        Future future = this.mStorageFuture;
        if (future != null) {
            future.cancel();
        }
    }
}
