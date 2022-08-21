package com.miui.gallery.magic.special.effects.video;

import android.content.Intent;
import android.graphics.Outline;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.miui.gallery.magic.R$dimen;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.base.BaseGuideActivity;
import com.miui.gallery.magic.fetch.VideoResourceFetcher;
import com.miui.gallery.magic.util.MagicSampler;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import java.io.IOException;
import miuix.animation.listener.TransitionListener;

/* loaded from: classes2.dex */
public class VideoEffectsGuideActivity extends BaseGuideActivity implements View.OnClickListener, MediaPlayer.OnPreparedListener {
    public RelativeLayout idpSeCancel;
    public ImageView ivVideoBack;
    public RelativeLayout mContent;
    public MediaPlayer mMediaPlayer;
    public Surface mSurface;
    public TextureView mTextureView;
    public TextureView.SurfaceTextureListener surfaceTextureListener = new TextureView.SurfaceTextureListener() { // from class: com.miui.gallery.magic.special.effects.video.VideoEffectsGuideActivity.1
        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
            VideoEffectsGuideActivity.this.playVideo();
        }

        @Override // android.view.TextureView.SurfaceTextureListener
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            if (VideoEffectsGuideActivity.this.mSurface != null) {
                VideoEffectsGuideActivity.this.mSurface.release();
                return false;
            }
            return false;
        }
    };
    public TextView tvVideoMake;
    public TextView tvVideoMsg;
    public TextView tvVideoTittle;

    public final void initData() {
    }

    @Override // com.miui.gallery.magic.base.BaseGuideActivity, com.miui.gallery.magic.base.BaseActivity, com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (BaseBuildUtil.isPad()) {
            setRequestedOrientation(4);
        } else {
            setRequestedOrientation(1);
        }
        setContentView(R$layout.ts_magic_video_effects_guide);
        initView();
        initData();
    }

    public final void initView() {
        View findViewById = findViewById(R$id.magic_video_guide);
        TextureView textureView = (TextureView) findViewById(R$id.magic_video_guide_view);
        this.mTextureView = textureView;
        textureView.setSurfaceTextureListener(this.surfaceTextureListener);
        this.mContent = (RelativeLayout) findViewById(R$id.content);
        TextView textView = (TextView) findViewById(R$id.tv_video_make);
        this.tvVideoMake = textView;
        textView.setSelected(true);
        TextView textView2 = (TextView) findViewById(R$id.magic_list_item_title);
        this.tvVideoTittle = textView2;
        textView2.setSelected(true);
        TextView textView3 = (TextView) findViewById(R$id.magic_list_item_dsc);
        this.tvVideoMsg = textView3;
        textView3.setSelected(true);
        this.idpSeCancel = (RelativeLayout) findViewById(R$id.idp_se_cancel);
        this.ivVideoBack = (ImageView) findViewById(R$id.iv_video_effect_back);
        if (BaseMiscUtil.isRTLDirection()) {
            this.ivVideoBack.setRotation(180.0f);
        }
        FolmeUtil.setCustomTouchAnim(this.idpSeCancel, new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build(), null, null, null, true);
        FolmeUtil.setDefaultTouchAnim(findViewById, new TransitionListener(), true);
        this.mContent.setOutlineProvider(new TextureVideoViewOutlineProvider(46.0f));
        this.mContent.setClipToOutline(true);
    }

    /* loaded from: classes2.dex */
    public class TextureVideoViewOutlineProvider extends ViewOutlineProvider {
        public float mRadius;

        public TextureVideoViewOutlineProvider(float f) {
            this.mRadius = f;
        }

        @Override // android.view.ViewOutlineProvider
        public void getOutline(View view, Outline outline) {
            Rect rect = new Rect();
            view.getGlobalVisibleRect(rect);
            outline.setRoundRect(new Rect(0, 0, (rect.right - rect.left) - 0, (int) VideoEffectsGuideActivity.this.getResources().getDimension(R$dimen.magic_video_guide_height)), this.mRadius);
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        MagicSampler.getInstance().recordCategory("video_post", "enter");
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R$id.idp_se_cancel) {
            finish();
        } else if (id != R$id.magic_video_guide && id != R$id.tv_video_make) {
        } else {
            startActivityForResult(getVideoIntent(), 1021);
        }
    }

    public final Intent getVideoIntent() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("video/*");
        intent.putExtra("disable_pending_transition", true);
        intent.putExtra("pick-need-origin", true);
        intent.setPackage("com.miui.gallery");
        return intent;
    }

    @Override // com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i != 1021 || intent == null || intent.getData() == null) {
            return;
        }
        openMakeActivity(intent.getData());
    }

    public final void openMakeActivity(Uri uri) {
        Intent intent = new Intent(this, VideoCutActivity.class);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override // com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer == null || mediaPlayer.isPlaying()) {
            return;
        }
        this.mMediaPlayer.start();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
            return;
        }
        this.mMediaPlayer.pause();
    }

    @Override // android.media.MediaPlayer.OnPreparedListener
    public void onPrepared(MediaPlayer mediaPlayer) {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    public final void playVideo() {
        this.mSurface = new Surface(this.mTextureView.getSurfaceTexture());
        initMediaPlayer();
    }

    public final void initMediaPlayer() {
        if (this.mMediaPlayer == null) {
            this.mMediaPlayer = new MediaPlayer();
            try {
                this.mMediaPlayer.setDataSource(this, Uri.parse(VideoResourceFetcher.INSTANCE.getGuideVideoPath()));
                this.mMediaPlayer.prepareAsync();
                this.mMediaPlayer.setOnPreparedListener(this);
                this.mMediaPlayer.setLooping(true);
            } catch (IOException | IllegalStateException e) {
                e.printStackTrace();
                Surface surface = this.mSurface;
                if (surface != null) {
                    surface.release();
                }
            }
        }
        this.mMediaPlayer.setSurface(this.mSurface);
    }
}
