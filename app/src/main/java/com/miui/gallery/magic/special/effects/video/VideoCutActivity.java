package com.miui.gallery.magic.special.effects.video;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Size;
import com.miui.gallery.magic.R$string;
import com.miui.gallery.magic.base.BaseFragmentActivity;
import com.miui.gallery.magic.special.effects.video.cut.menu.VideoMenuFragment;
import com.miui.gallery.magic.special.effects.video.cut.preview.ICut$VP;
import com.miui.gallery.magic.special.effects.video.cut.preview.VideoCutFragment;
import com.miui.gallery.magic.tools.MagicUtils;
import com.miui.gallery.magic.util.GetPathFromUri;
import com.miui.gallery.magic.util.MagicLog;
import com.miui.gallery.magic.util.MagicToast;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.milab.videosdk.FrameRetriever;
import com.xiaomi.milab.videosdk.XmsContext;
import java.util.Objects;

/* loaded from: classes2.dex */
public class VideoCutActivity extends BaseFragmentActivity {
    public boolean isCutVideoInCurrPage = false;
    public VideoCutFragment mCutFragment;
    public VideoMenuFragment mMenuFragment;
    public BaseCutBroadcastReceiver receiver;

    @Override // com.miui.gallery.magic.base.BaseFragmentActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (BaseBuildUtil.isPad()) {
            setRequestedOrientation(4);
        } else {
            setRequestedOrientation(1);
        }
        initBroadcastReceiver();
        Uri data = getIntent().getData();
        if (data == null || is8KVideo(data.getLastPathSegment()) || is3gpVideo(data.getLastPathSegment())) {
            MagicToast.showToast(this, getResources().getString(R$string.magic_cut_video_no_support_video_edit));
            finish();
            return;
        }
        initVideoLibs();
        Uri data2 = getIntent().getData();
        if (data2 == null) {
            finish();
        }
        FrameRetriever frameRetriever = new FrameRetriever();
        frameRetriever.setDataSource(GetPathFromUri.getPath(getApplicationContext(), data2));
        float fps = frameRetriever.getFPS();
        int width = frameRetriever.getWidth();
        int height = frameRetriever.getHeight();
        int max = Math.max(width, height);
        int min = Math.min(width, height);
        MagicLog magicLog = MagicLog.INSTANCE;
        magicLog.showLog("MagicLogger VideoCutActivity", "onCreate  fps " + fps + " frameRetriever.getWidth() " + frameRetriever.getWidth() + " frameRetriever.getHeight() " + frameRetriever.getHeight());
        frameRetriever.release();
        if (((max >= 3000 || min >= 2000) && fps > 100.0f) || min <= 480) {
            MagicToast.showToast(this, getResources().getString(R$string.magic_cut_video_no_support_video_edit));
            finish();
            return;
        }
        this.mCutFragment = new VideoCutFragment();
        this.mMenuFragment = new VideoMenuFragment();
        initView();
    }

    public final void initView() {
        addPreview(this.mCutFragment);
        addMenu(this.mMenuFragment);
    }

    public final void initVideoLibs() {
        String absolutePath = getDir("libs", 0).getAbsolutePath();
        System.load(absolutePath + "/libmiffmpeg.so");
        System.load(absolutePath + "/libMiVideoSDK.so");
        XmsContext.getInstance().setContext(getApplication());
        XmsContext.getInstance().createTimeline();
    }

    @Override // com.miui.gallery.magic.base.BaseFragmentActivity
    public Object event(int i, Object obj) {
        switch (i) {
            case 1:
                this.mMenuFragment.getContract().setProgress(((Integer) obj).intValue());
                return null;
            case 2:
                Message message = (Message) obj;
                ICut$VP contract = this.mCutFragment.getContract();
                long longValue = ((Long) message.obj).longValue();
                boolean z = true;
                if (message.arg1 != 1) {
                    z = false;
                }
                contract.seekTo(longValue, z);
                return null;
            case 3:
                return Integer.valueOf(this.mCutFragment.getContract().getTotalTime());
            case 4:
                return this.mCutFragment.getContract().getVideoPath();
            case 5:
                this.mMenuFragment.getContract().initFinish((Size) obj);
                return null;
            case 6:
                if (this.isCutVideoInCurrPage) {
                    this.mMenuFragment.getContract().cutVideo();
                    return null;
                }
                skipEffectActivity(this.mMenuFragment.getContract().getCurrentTimes(this.mCutFragment.getContract().getTotalTime()), this.mCutFragment.getContract().getVideoPath());
                return null;
            case 7:
                return this.mMenuFragment.getContract().getCurrentTimes(((Integer) obj).intValue());
            case 8:
            default:
                return null;
            case 9:
                this.mCutFragment.getContract().onRePlayVideo();
                return null;
            case 10:
                this.mCutFragment.getContract().onStopTrackingTouch();
                return null;
            case 11:
                this.mCutFragment.getContract().pauseVideo();
                return null;
            case 12:
                this.mCutFragment.getContract().onPause();
                return null;
        }
    }

    public final void skipEffectActivity(int[] iArr, String str) {
        Intent intent = new Intent(this, VideoEffectsActivity.class);
        intent.putExtra("video_slice", this.isCutVideoInCurrPage);
        intent.putExtra(nexExportFormat.TAG_FORMAT_PATH, str);
        intent.putExtra("duration", iArr);
        startActivity(intent);
    }

    public final boolean is8KVideo(String str) {
        boolean is8KVideo = MagicUtils.is8KVideo(str);
        DefaultLogger.d("MagicLogger VideoCutActivity", "is8KVideo=%b", Boolean.valueOf(is8KVideo));
        return is8KVideo;
    }

    public final boolean is3gpVideo(String str) {
        return BaseFileMimeUtil.getMimeType(str).equalsIgnoreCase("video/3gpp");
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(this.receiver);
        MagicLog.INSTANCE.showLog("MagicLogger VideoCutActivity", "onDestroy  ");
    }

    public final void initBroadcastReceiver() {
        this.receiver = new BaseCutBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("receiver_action_save_finish");
        registerReceiver(this.receiver, intentFilter);
    }

    /* loaded from: classes2.dex */
    public class BaseCutBroadcastReceiver extends BroadcastReceiver {
        public BaseCutBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Objects.requireNonNull(action);
            if ("receiver_action_save_finish".equals(action)) {
                VideoCutActivity.this.finish();
            }
        }
    }
}
