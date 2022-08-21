package com.miui.gallery.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.cleaner.slim.SlimController;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.FormatUtil;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.StorageUtils;
import com.miui.gallery.util.anim.FolmeUtil;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.MiStat;
import java.util.HashMap;
import miuix.appcompat.app.AlertDialog;
import miuix.appcompat.app.AppCompatActivity;

/* loaded from: classes2.dex */
public class PhotoSlimFragment extends BaseFragment implements SlimController.SpaceSlimObserver {
    public Button mActionButton;
    public View.OnClickListener mActionButtonClickListener = new View.OnClickListener() { // from class: com.miui.gallery.ui.PhotoSlimFragment.2
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            PhotoSlimFragment.this.onBackPressed();
            TrackController.trackClick("4403.27.7.1.14550", AutoTracking.getRef());
        }
    };
    public TextView mActionDescription;
    public TextView mActionSubDescription;
    public TextView mActionTitle;
    public AlertDialog mExitConfirmDialog;
    public boolean mIsNightMode;
    public SlimRotateProgressBar mProgressBar;
    public SlimController mSlimController;

    @Override // com.miui.gallery.ui.BaseFragment
    public String getPageName() {
        return "photo_slim";
    }

    @Override // com.miui.gallery.ui.BaseFragment
    public int getThemeRes() {
        return 2131952019;
    }

    @Override // com.miui.gallery.cleaner.slim.SlimController.SpaceSlimObserver
    public void onSlimResumed() {
    }

    @Override // miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.photo_slim_fragment, viewGroup, false);
        TextView textView = (TextView) inflate.findViewById(R.id.title);
        this.mActionTitle = textView;
        textView.setText(R.string.slim_running);
        this.mActionDescription = (TextView) inflate.findViewById(R.id.description);
        this.mActionSubDescription = (TextView) inflate.findViewById(R.id.sub_description);
        Button button = (Button) inflate.findViewById(R.id.action_button);
        this.mActionButton = button;
        button.setText(R.string.slim_stop_v2);
        this.mActionButton.setOnClickListener(this.mActionButtonClickListener);
        int i = 1;
        FolmeUtil.setDefaultTouchAnim(this.mActionButton, null, false, false, true);
        SlimRotateProgressBar slimRotateProgressBar = (SlimRotateProgressBar) inflate.findViewById(R.id.progress_bar);
        this.mProgressBar = slimRotateProgressBar;
        slimRotateProgressBar.setDescription(getString(R.string.slim_space));
        this.mProgressBar.setNumber(0L, false, null);
        this.mIsNightMode = MiscUtil.isNightMode(getActivity());
        AppCompatActivity appCompatActivity = getAppCompatActivity();
        if (this.mIsNightMode) {
            i = 2;
        }
        appCompatActivity.setTranslucentStatus(i);
        this.mSlimController = SlimController.getInstance();
        return inflate;
    }

    public void trackSlimInfo(boolean z) {
        HashMap hashMap = new HashMap();
        hashMap.put("tip", "403.27.7.1.14549");
        hashMap.put("ref_tip", AutoTracking.getRef());
        hashMap.put(MiStat.Param.COUNT, Integer.valueOf(this.mSlimController.getSlimCount()));
        hashMap.put("value", Long.valueOf((System.currentTimeMillis() - this.mSlimController.getStartTime()) / 1000));
        hashMap.put("status", z ? "finished" : "stop");
        hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, Integer.valueOf(FormatUtil.getDeviceSpaceRange(StorageUtils.getTotalBytes(StorageUtils.getPrimaryStoragePath()) / 1000000000)));
        hashMap.put("count_extra", Long.valueOf(StorageUtils.getAvailableBytes(StorageUtils.getPrimaryStoragePath()) / 1000000));
        AutoTracking.trackView(hashMap);
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        if (!this.mSlimController.isSlimStarted()) {
            this.mActivity.finish();
        }
        this.mSlimController.registerObserver(this);
        if (this.mSlimController.isSlimming()) {
            long releaseSize = this.mSlimController.getReleaseSize();
            this.mProgressBar.setNumber(releaseSize, false);
            if (this.mSlimController.isSlimPaused()) {
                showExitConfirmDialog();
                onSlimPaused();
                return;
            }
            onSlimProgress(-1L, releaseSize, this.mSlimController.getRemainCount());
            return;
        }
        this.mProgressBar.setNumber(this.mSlimController.getReleaseSize());
        showFinishState();
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        this.mSlimController.unregisterObserver(this);
    }

    @Override // miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        AlertDialog alertDialog = this.mExitConfirmDialog;
        if (alertDialog == null || !alertDialog.isShowing()) {
            return;
        }
        this.mExitConfirmDialog.dismiss();
    }

    @Override // com.miui.gallery.cleaner.slim.SlimController.SpaceSlimObserver
    public void onSlimProgress(long j, long j2, int i) {
        if (getActivity() == null || this.mSlimController.isSlimPaused()) {
            return;
        }
        this.mActionTitle.setText(remainTime((int) Math.ceil((i + 1) * 0.2f)));
        if (i >= 1) {
            this.mProgressBar.setNumber(j2, false, null);
            this.mActionDescription.setText(getResources().getQuantityString(R.plurals.slim_remain_photo_count_v2, i, Integer.valueOf(i)));
            return;
        }
        this.mProgressBar.setNumber(j2, true, new Runnable() { // from class: com.miui.gallery.ui.PhotoSlimFragment.1
            @Override // java.lang.Runnable
            public void run() {
                PhotoSlimFragment.this.showFinishState();
            }
        });
    }

    @Override // com.miui.gallery.cleaner.slim.SlimController.SpaceSlimObserver
    public void onSlimPaused() {
        int remainCount = SlimController.getInstance().getRemainCount();
        this.mActionDescription.setText(getResources().getQuantityString(R.plurals.slim_remain_photo_count_v2, remainCount, Integer.valueOf(remainCount)));
        this.mProgressBar.pause();
    }

    public final void showFinishState() {
        String str;
        trackSlimInfo(true);
        if (getActivity() == null) {
            return;
        }
        int slimPhotoCount = this.mSlimController.getSlimPhotoCount();
        int slimVideoCountCount = this.mSlimController.getSlimVideoCountCount();
        if (slimPhotoCount > 0 && slimVideoCountCount > 0) {
            this.mActionDescription.setText(getResources().getString(R.string.slim_complete_count, getResources().getQuantityString(R.plurals.slim_photo_count, slimPhotoCount, Integer.valueOf(slimPhotoCount)), getResources().getQuantityString(R.plurals.slim_videos_count, slimVideoCountCount, Integer.valueOf(slimVideoCountCount))));
        } else if (slimPhotoCount > 0) {
            this.mActionDescription.setText(getResources().getQuantityString(R.plurals.slim_complete_photo_count_v2, slimPhotoCount, Integer.valueOf(slimPhotoCount)));
        } else if (slimVideoCountCount > 0) {
            this.mActionDescription.setText(getResources().getQuantityString(R.plurals.slim_complete_video_count, slimVideoCountCount, Integer.valueOf(slimVideoCountCount)));
        }
        if (this.mActivity.getIntent() != null && this.mActivity.getIntent().getBooleanExtra("download_type_changed", false)) {
            this.mActionSubDescription.setVisibility(0);
            str = "403.27.7.1.16297";
        } else {
            this.mActionSubDescription.setVisibility(8);
            str = "403.27.7.1.14551";
        }
        this.mActionTitle.setText(getResources().getString(R.string.slim_success_tip, FormatUtil.formatFileSize(getActivity(), this.mSlimController.getReleaseSize())));
        this.mActionButton.setText(R.string.slim_complete);
        SamplingStatHelper.recordCountEvent("cleaner", "slim_finish_page");
        HashMap hashMap = new HashMap();
        hashMap.put("tip", str);
        hashMap.put("ref_tip", AutoTracking.getRef());
        AutoTracking.trackView(hashMap);
    }

    public final String remainTime(int i) {
        int i2 = i / 60;
        int i3 = i % 60;
        return (i2 <= 0 || i3 <= 0) ? i3 > 0 ? getResources().getString(R.string.slim_cost_time_second, Integer.valueOf(i3)) : getResources().getString(R.string.slim_cost_time_minute, Integer.valueOf(i2)) : getResources().getString(R.string.slim_cost_time_minute_and_second, Integer.valueOf(i2), Integer.valueOf(i3));
    }

    public boolean onBackPressed() {
        if (!this.mSlimController.isSlimming()) {
            if (this.mActionSubDescription.getVisibility() == 8) {
                TrackController.trackClick("403.27.7.1.17059", AutoTracking.getRef(), MiStat.Param.ORIGIN);
            } else {
                TrackController.trackClick("403.27.7.1.17059", AutoTracking.getRef(), "changed");
            }
            onExit();
            return true;
        }
        this.mSlimController.pause();
        showExitConfirmDialog();
        HashMap hashMap = new HashMap();
        int currentTimeMillis = (int) ((System.currentTimeMillis() - this.mSlimController.getStartTime()) / 1000);
        hashMap.put("elapse_time", SamplingStatHelper.formatValueStage(currentTimeMillis, SlimController.TIME_COST_STAGE));
        SamplingStatHelper.recordCountEvent("cleaner", "slim_exit", hashMap);
        int remainCount = this.mSlimController.getRemainCount();
        HashMap hashMap2 = new HashMap();
        hashMap2.put("tip", "403.27.7.1.16846");
        hashMap2.put("ref_tip", AutoTracking.getRef());
        hashMap2.put("value", Integer.valueOf(currentTimeMillis));
        hashMap2.put("status", Integer.valueOf((int) Math.ceil((remainCount + 1) * 0.2f)));
        hashMap2.put("asset_id", Integer.valueOf(this.mSlimController.getSlimCount()));
        TrackController.trackExpose(hashMap2);
        return true;
    }

    public final void showExitConfirmDialog() {
        AlertDialog alertDialog = this.mExitConfirmDialog;
        if (alertDialog == null || !alertDialog.isShowing()) {
            int remainCount = SlimController.getInstance().getRemainCount();
            if (this.mExitConfirmDialog == null) {
                this.mExitConfirmDialog = new AlertDialog.Builder(this.mActivity).setPositiveButton(R.string.slim_exit_dialog_positive, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.PhotoSlimFragment.5
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PhotoSlimFragment.this.onExit();
                        SamplingStatHelper.recordCountEvent("cleaner", "slim_exit_confirm");
                        PhotoSlimFragment.this.trackSlimInfo(false);
                        TrackController.trackClick("403.27.7.1.16833", AutoTracking.getRef());
                    }
                }).setNegativeButton(17039360, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.PhotoSlimFragment.4
                    @Override // android.content.DialogInterface.OnClickListener
                    public void onClick(DialogInterface dialogInterface, int i) {
                        PhotoSlimFragment.this.onExitCancel();
                    }
                }).setTitle(R.string.slim_exit_dialog_title).setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.miui.gallery.ui.PhotoSlimFragment.3
                    @Override // android.content.DialogInterface.OnCancelListener
                    public void onCancel(DialogInterface dialogInterface) {
                        PhotoSlimFragment.this.onExitCancel();
                    }
                }).create();
            }
            this.mExitConfirmDialog.setMessage(getResources().getQuantityString(R.plurals.slim_exit_dialog_message, remainCount, Integer.valueOf(remainCount)));
            this.mExitConfirmDialog.show();
        }
    }

    public final void onExitCancel() {
        SlimController.getInstance().resume();
        SamplingStatHelper.recordCountEvent("cleaner", "slim_exit_cancel");
    }

    public final void onExit() {
        SlimController.getInstance().stop();
        this.mActivity.finish();
    }
}
