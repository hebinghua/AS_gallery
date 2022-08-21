package com.miui.gallery.ui.album.main.component.lock;

import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.storage.strategies.base.StorageStrategyManager;
import com.miui.gallery.ui.album.main.AlbumTabContract$V;
import com.miui.gallery.ui.album.main.AlbumTabPresenter;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.ResourceUtils;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.io.File;
import miuix.springback.trigger.BaseTrigger;

/* loaded from: classes2.dex */
public class AlbumTabLockAction extends BaseTrigger.SimpleAction {
    public boolean mHasEnterPrivateEntry;
    public ImageView mIconBody;
    public ImageView mIconHeader;
    public TextView mLabel;
    public PrivateEntryTriggerListener mListener;
    public View mLockView;
    public AlbumTabContract$V<AlbumTabPresenter> mView;

    @Override // miuix.springback.trigger.BaseTrigger.Action
    public void onExit() {
    }

    @Override // miuix.springback.trigger.BaseTrigger.Action
    public void onFinished() {
    }

    public AlbumTabLockAction(AlbumTabContract$V<AlbumTabPresenter> albumTabContract$V) {
        this.mListener = null;
        this.mView = albumTabContract$V;
        this.mListener = new PrivateEntryTriggerListener() { // from class: com.miui.gallery.ui.album.main.component.lock.AlbumTabLockAction.1
            @Override // com.miui.gallery.ui.album.main.component.lock.PrivateEntryTriggerListener
            public void onTriggered() {
                String pathInPriorStorage = StorageUtils.getPathInPriorStorage("MIUI/Gallery/cloud/secretAlbum");
                StorageStrategyManager storageStrategyManager = StorageSolutionProvider.get();
                StringBuilder sb = new StringBuilder();
                sb.append(pathInPriorStorage);
                String str = File.separator;
                sb.append(str);
                sb.append("test.txt");
                String sb2 = sb.toString();
                IStoragePermissionStrategy.Permission permission = IStoragePermissionStrategy.Permission.INSERT;
                IStoragePermissionStrategy.PermissionResult checkPermission = storageStrategyManager.checkPermission(sb2, permission);
                if (checkPermission.granted || !checkPermission.applicable) {
                    DefaultLogger.d("AlbumTabLockAction", "onTriggered => mHasEnterPrivateEntry: %b", Boolean.valueOf(AlbumTabLockAction.this.mHasEnterPrivateEntry));
                    if (AlbumTabLockAction.this.mHasEnterPrivateEntry) {
                        return;
                    }
                    IntentUtil.checkLoginAndSyncState(AlbumTabLockAction.this.mView);
                    AlbumTabLockAction.this.mHasEnterPrivateEntry = true;
                    AlbumTabLockAction.this.recordEnterPrivateAlbum();
                    return;
                }
                StorageStrategyManager storageStrategyManager2 = StorageSolutionProvider.get();
                FragmentActivity safeActivity = AlbumTabLockAction.this.mView.getSafeActivity();
                storageStrategyManager2.requestPermission(safeActivity, pathInPriorStorage + str + "test.txt", permission);
                DefaultLogger.d("AlbumTabLockAction", "onTriggered => request permission");
            }
        };
    }

    @Override // miuix.springback.trigger.BaseTrigger.Action
    public View onCreateIndicator(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        View inflate = layoutInflater.inflate(R.layout.miuix_sbl_simple_indicator, viewGroup, false);
        this.mLockView = inflate;
        this.mIconHeader = (ImageView) inflate.findViewById(R.id.indicator_locked_header);
        this.mIconBody = (ImageView) this.mLockView.findViewById(R.id.indicator_locked_body);
        this.mLabel = (TextView) this.mLockView.findViewById(R.id.label);
        return this.mLockView;
    }

    @Override // miuix.springback.trigger.BaseTrigger.Action
    public void onTriggered() {
        if (isInChoiceMode()) {
            return;
        }
        this.mListener.onTriggered();
    }

    @Override // miuix.springback.trigger.BaseTrigger.Action
    public void onEntered() {
        if (isInChoiceMode()) {
            this.mLockView.setVisibility(8);
            return;
        }
        LinearMotorHelper.performHapticFeedback(this.mLockView, LinearMotorHelper.HAPTIC_SWITCH);
        this.mLockView.setVisibility(0);
        this.mIconBody.setImageDrawable(getResources().getDrawable(R.drawable.miuix_sbl_simple_indicator_locked_body_gray));
        this.mIconHeader.setImageDrawable(getResources().getDrawable(R.drawable.miuix_sbl_simple_indicator_locked_header_gray));
        this.mLabel.setText(ResourceUtils.getString(R.string.private_entry_lock_state_enterd_text));
        this.mLabel.setTextColor(getResources().getColor(R.color.miuix_sbl_locked_text_gray));
    }

    @Override // miuix.springback.trigger.BaseTrigger.Action
    public void onActivated() {
        if (isInChoiceMode()) {
            this.mLockView.setVisibility(8);
            return;
        }
        this.mLockView.setVisibility(0);
        this.mIconBody.setImageDrawable(getResources().getDrawable(R.drawable.miuix_sbl_simple_indicator_locked_body_blue));
        this.mIconHeader.setImageDrawable(getResources().getDrawable(R.drawable.miuix_sbl_simple_indicator_locked_header_blue));
        this.mLabel.setText(ResourceUtils.getString(R.string.private_entry_lock_state_activated_text));
        this.mLabel.setTextColor(getResources().getColor(R.color.miuix_sbl_locked_text_blue));
    }

    public final Resources getResources() {
        return this.mView.getResources();
    }

    public final boolean isInChoiceMode() {
        return this.mView.isInChoiceMode();
    }

    public boolean onResult(int i, int i2, Intent intent) {
        if (i2 != -1) {
            DefaultLogger.d("AlbumTabLockAction", "disableEnterPrivateEntry");
            this.mHasEnterPrivateEntry = false;
        } else if (i == 29) {
            int intExtra = intent.getIntExtra("check_login_and_sync", -1);
            if (intExtra == 1 || intExtra == 2) {
                return false;
            }
            if (intExtra == 3) {
                DefaultLogger.d("AlbumTabLockAction", "gotoPrivateAlbum");
                IntentUtil.gotoPrivateAlbum(this.mView);
            }
            return true;
        } else if (i == 36) {
            IntentUtil.enterPrivateAlbum(this.mView.getSafeActivity());
            this.mHasEnterPrivateEntry = false;
            return true;
        }
        return false;
    }

    public final void recordEnterPrivateAlbum() {
        SamplingStatHelper.recordCountEvent("album", "enter_privacy_mode");
        TrackController.trackClick("403.7.2.1.11418", AutoTracking.getRef());
    }
}
