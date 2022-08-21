package com.miui.gallery.magic.special.effects.video;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.miui.gallery.magic.base.BaseFragmentActivity;
import com.miui.gallery.magic.special.effects.video.adapter.ListItem;
import com.miui.gallery.magic.special.effects.video.effects.audio.menu.AudioMenuFragment;
import com.miui.gallery.magic.special.effects.video.effects.menu.VideoMenuFragment;
import com.miui.gallery.magic.special.effects.video.effects.preview.VideoPreviewFragment;
import com.miui.gallery.magic.util.CacheManager;
import com.miui.gallery.magic.util.FileUtils;
import com.miui.gallery.util.BaseBuildUtil;
import com.xiaomi.stat.c.b;
import java.io.File;

/* loaded from: classes2.dex */
public class VideoEffectsActivity extends BaseFragmentActivity {
    public static final String VIDEO_EFFECTS_PNG;
    public AudioMenuFragment mAudioMenuFragment;
    public VideoPreviewFragment mEffectsFragment;
    public VideoMenuFragment mMenuFragment;

    public static /* synthetic */ void $r8$lambda$sXdYOZfMFWiYcEWD60_IvnmCQNA(VideoEffectsActivity videoEffectsActivity) {
        videoEffectsActivity.lambda$onCreate$0();
    }

    static {
        StringBuilder sb = new StringBuilder();
        sb.append("video_effects");
        String str = File.separator;
        sb.append(str);
        sb.append("video");
        sb.append(str);
        sb.append("dark.png");
        VIDEO_EFFECTS_PNG = sb.toString();
    }

    @Override // com.miui.gallery.magic.base.BaseFragmentActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (BaseBuildUtil.isPad()) {
            setRequestedOrientation(4);
        } else {
            setRequestedOrientation(1);
        }
        this.mEffectsFragment = new VideoPreviewFragment();
        this.mMenuFragment = new VideoMenuFragment();
        this.mAudioMenuFragment = new AudioMenuFragment();
        showLoading();
        new Thread(new Runnable() { // from class: com.miui.gallery.magic.special.effects.video.VideoEffectsActivity$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                VideoEffectsActivity.$r8$lambda$sXdYOZfMFWiYcEWD60_IvnmCQNA(VideoEffectsActivity.this);
            }
        }).start();
    }

    public /* synthetic */ void lambda$onCreate$0() {
        FileUtils.copyAssetsDspToDirPhone(this, "video_effects");
        FileUtils.copyAssetsDspToDirPhoneForPng(this, VIDEO_EFFECTS_PNG);
        runOnUiThread(new Runnable() { // from class: com.miui.gallery.magic.special.effects.video.VideoEffectsActivity.1
            {
                VideoEffectsActivity.this = this;
            }

            @Override // java.lang.Runnable
            public void run() {
                VideoEffectsActivity.this.removeLoadingDialog();
                VideoEffectsActivity.this.initView();
            }
        });
    }

    public final void initView() {
        addPreviewForEffectVideo(this.mEffectsFragment);
        addMenuForEffectVideo(this.mMenuFragment);
    }

    @Override // com.miui.gallery.magic.base.BaseFragmentActivity, android.view.View.OnClickListener
    public void onClick(View view) {
        this.mEffectsFragment.onClick(view);
        this.mMenuFragment.onClick(view);
        this.mAudioMenuFragment.onClick(view);
    }

    @Override // com.miui.gallery.magic.base.BaseFragmentActivity
    public Object event(int i, Object obj) {
        return execEffects(i, obj);
    }

    public final Object execEffects(int i, Object obj) {
        if (this.mEffectsFragment.isAdded()) {
            switch (i) {
                case 1001:
                    this.mMenuFragment.showOrHintTextView(false);
                    this.mMenuFragment.setShowLongHintBtn(false);
                    this.mEffectsFragment.showVideoBackButton(true);
                    CacheManager.addCache(new String[]{"hide_long_text_msg"}, new Object[]{Boolean.TRUE});
                    this.mEffectsFragment.getContract().setStartSegEffectId(((Integer) obj).intValue(), false);
                    return null;
                case 1002:
                    return this.mEffectsFragment.getContract().getVideoPath();
                case 1003:
                    return Long.valueOf(this.mEffectsFragment.getContract().getTotalTime());
                case 1004:
                    this.mMenuFragment.getContract().setProgress(((Float) obj).floatValue(), this.mEffectsFragment.getContract().getCurrentType());
                    return null;
                case 1005:
                    this.mEffectsFragment.getContract().seekToVideo(((Long) obj).longValue());
                    return null;
                case 1006:
                    this.mEffectsFragment.getContract().selectAudioFile((String) obj);
                    return null;
                case b.g /* 1007 */:
                    this.mEffectsFragment.mBanner.setVisibility(4);
                    addMenu(this.mAudioMenuFragment);
                    return null;
                case b.h /* 1008 */:
                    this.mEffectsFragment.getContract().setVolume(((Float) obj).floatValue());
                    return null;
                case b.i /* 1009 */:
                    this.mEffectsFragment.getContract().stopVideo();
                    return null;
                case b.j /* 1010 */:
                    this.mMenuFragment.getContract().undo();
                    return null;
                case b.k /* 1011 */:
                    this.mEffectsFragment.getContract().setStartSegEffectId(((Integer) obj).intValue(), true);
                    return null;
                case b.l /* 1012 */:
                    this.mEffectsFragment.getContract().selectAudio((ListItem) obj);
                    return null;
                case 1013:
                    this.mEffectsFragment.getContract().switchToVideoEffect();
                    this.mMenuFragment.getContract().switchToVideoEffect();
                    return null;
                case 1014:
                    this.mEffectsFragment.getContract().switchToAudioTrack();
                    this.mMenuFragment.getContract().switchToAudioTrack();
                    return null;
                case 1015:
                    this.mMenuFragment.getContract().startProgress(((Integer) obj).intValue());
                    return null;
                case 1016:
                    this.mEffectsFragment.getContract().seekToEnd(((Long) obj).longValue());
                    return null;
                case 1017:
                    this.mMenuFragment.mo1066initContract().onActionUp(-1.0f, -1.0f);
                    return null;
                case 1018:
                    return 0;
                default:
                    switch (i) {
                        case 2001:
                            if (!CacheManager.getBoolean("hide_long_text_msg", false)) {
                                this.mMenuFragment.showOrHintTextView(true);
                                this.mMenuFragment.setShowLongHintBtn(true);
                            }
                            this.mEffectsFragment.mo1066initContract().dismissDialog();
                            return null;
                        case 2002:
                            this.mEffectsFragment.mo1066initContract().showDialogProgress(obj, 2002);
                            return null;
                        case 2003:
                            this.mEffectsFragment.mo1066initContract().showDialogProgress(obj, 2003);
                            return null;
                        default:
                            return null;
                    }
            }
        }
        return null;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        this.mEffectsFragment.cancelEdit();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        VideoPreviewFragment.setIsEdit(false);
    }

    public void changeMenu() {
        this.mEffectsFragment.mBanner.setVisibility(0);
        addMenu(this.mMenuFragment);
    }

    @Override // com.miui.gallery.magic.base.BaseFragmentActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        this.mMenuFragment.onActivityResult(i, i2, intent);
    }
}
