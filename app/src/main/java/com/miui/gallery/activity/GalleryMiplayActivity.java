package com.miui.gallery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import androidx.fragment.app.Fragment;
import com.miui.gallery.R;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.app.StrategyContext;
import com.miui.gallery.miplay.GalleryMiplayControlFragment;
import com.miui.gallery.miplay.ScreenListener;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.ScreenStateSenorFrameLayout;
import com.xiaomi.miplay.phoneclientsdk.info.MediaMetaData;

/* loaded from: classes.dex */
public class GalleryMiplayActivity extends BaseActivity {
    public GalleryMiplayControlFragment mFragment;
    public ScreenStateSenorFrameLayout mRootView;
    public volatile boolean mNeedStopPlay = true;
    public final Handler mMainHandler = new Handler(Looper.getMainLooper());
    public final Runnable mStopPlayTask = new Runnable() { // from class: com.miui.gallery.activity.GalleryMiplayActivity.1
        {
            GalleryMiplayActivity.this = this;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (GalleryMiplayActivity.this.mFragment == null || !GalleryMiplayActivity.this.mNeedStopPlay) {
                return;
            }
            GalleryMiplayActivity.this.mFragment.stopPlay(true);
        }
    };

    public static /* synthetic */ Fragment $r8$lambda$he7ptWoZtcQqaoOI_EG2wAPdasI(MediaMetaData mediaMetaData, String str) {
        return lambda$onCreate$0(mediaMetaData, str);
    }

    @Override // com.miui.gallery.activity.BaseActivity
    public int getFragmentContainerId() {
        return R.id.miplay_fragment_container;
    }

    @Override // com.miui.gallery.activity.BaseActivity
    public boolean hasCustomContentView() {
        return true;
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestDisableStrategy(StrategyContext.DisableStrategyType.LANDSCAPE_DIRECTION);
        setContentView(R.layout.gallery_miplay_control_activity);
        ScreenStateSenorFrameLayout screenStateSenorFrameLayout = (ScreenStateSenorFrameLayout) findViewById(R.id.gallery_miplay_root);
        this.mRootView = screenStateSenorFrameLayout;
        screenStateSenorFrameLayout.registerScreenStateListener(new ScreenListener() { // from class: com.miui.gallery.activity.GalleryMiplayActivity.2
            {
                GalleryMiplayActivity.this = this;
            }

            @Override // com.miui.gallery.miplay.ScreenListener
            public void onScreenOn() {
                DefaultLogger.d("GalleryMiplayActivity", "onScreenOn");
                GalleryMiplayActivity.this.mMainHandler.removeCallbacks(GalleryMiplayActivity.this.mStopPlayTask);
                GalleryMiplayActivity.this.mNeedStopPlay = true;
            }

            @Override // com.miui.gallery.miplay.ScreenListener
            public void onScreenOff() {
                DefaultLogger.d("GalleryMiplayActivity", "onScreenOff");
                GalleryMiplayActivity.this.mNeedStopPlay = false;
            }
        });
        Intent intent = getIntent();
        if (intent.getExtras() == null || intent.getExtras().getParcelable("video_miplay_target") == null) {
            DefaultLogger.d("GalleryMiplayActivity", "Extras is null ,finish");
            finish();
            return;
        }
        final MediaMetaData mediaMetaData = (MediaMetaData) intent.getExtras().getParcelable("video_miplay_target");
        this.mFragment = (GalleryMiplayControlFragment) startFragment(new BaseActivity.FragmentCreator() { // from class: com.miui.gallery.activity.GalleryMiplayActivity$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.activity.BaseActivity.FragmentCreator
            public final Fragment create(String str) {
                return GalleryMiplayActivity.$r8$lambda$he7ptWoZtcQqaoOI_EG2wAPdasI(MediaMetaData.this, str);
            }
        }, "GalleryMiplayControlFragment", true, true);
    }

    public static /* synthetic */ Fragment lambda$onCreate$0(MediaMetaData mediaMetaData, String str) {
        return new GalleryMiplayControlFragment(mediaMetaData);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyUp(int i, KeyEvent keyEvent) {
        GalleryMiplayControlFragment galleryMiplayControlFragment;
        if ((i == 24 || i == 25) && (galleryMiplayControlFragment = this.mFragment) != null) {
            galleryMiplayControlFragment.onVolumeBtnKeyUp();
            return true;
        }
        return super.onKeyUp(i, keyEvent);
    }

    @Override // com.miui.gallery.activity.BaseActivity, android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        GalleryMiplayControlFragment galleryMiplayControlFragment;
        if ((i == 24 || i == 25) && (galleryMiplayControlFragment = this.mFragment) != null) {
            galleryMiplayControlFragment.onVolumeBtnKeyDown(i == 24);
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }

    @Override // com.miui.gallery.activity.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        this.mMainHandler.postDelayed(this.mStopPlayTask, 200L);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        GalleryMiplayControlFragment galleryMiplayControlFragment = this.mFragment;
        if (galleryMiplayControlFragment != null) {
            galleryMiplayControlFragment.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override // com.miui.gallery.activity.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        this.mMainHandler.removeCallbacks(this.mStopPlayTask);
        this.mRootView.unregisterScreenStateListener();
        super.onDestroy();
    }
}
