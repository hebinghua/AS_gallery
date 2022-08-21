package com.miui.gallery.magic.special.effects.video.cut.preview;

import android.content.Intent;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.base.BaseFragment;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;

/* loaded from: classes2.dex */
public class VideoCutFragment extends BaseFragment<VideoCutPresenter, ICut$VP> {
    public static long lastClickTime;
    public TextureView mTextureView;

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.magic.base.BaseFragment
    /* renamed from: initContract */
    public ICut$VP mo1066initContract() {
        return new ICut$VP() { // from class: com.miui.gallery.magic.special.effects.video.cut.preview.VideoCutFragment.1
            @Override // com.miui.gallery.magic.special.effects.video.cut.preview.ICut$VP
            public void onPlayVideo() {
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.preview.ICut$VP
            public void setVideoTime(String str, String str2) {
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.preview.ICut$VP
            public void pauseVideo() {
                ((VideoCutPresenter) VideoCutFragment.this.mPresenter).getContract().pauseVideo();
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.preview.ICut$VP
            public void onStopTrackingTouch() {
                ((VideoCutPresenter) VideoCutFragment.this.mPresenter).getContract().onStopTrackingTouch();
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.preview.ICut$VP
            public void initVideoData(Intent intent) {
                ((VideoCutPresenter) VideoCutFragment.this.mPresenter).getContract().initVideoData(intent);
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.preview.ICut$VP
            public void setSurfaceTextureListener() {
                ((VideoCutPresenter) VideoCutFragment.this.mPresenter).getContract().setSurfaceTextureListener();
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.preview.ICut$VP
            public String getVideoPath() {
                return ((VideoCutPresenter) VideoCutFragment.this.mPresenter).getContract().getVideoPath();
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.preview.ICut$VP
            public int getTotalTime() {
                return ((VideoCutPresenter) VideoCutFragment.this.mPresenter).getContract().getTotalTime();
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.preview.ICut$VP
            public void seekTo(long j, boolean z) {
                ((VideoCutPresenter) VideoCutFragment.this.mPresenter).getContract().seekTo(j, z);
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.preview.ICut$VP
            public void stop() {
                ((VideoCutPresenter) VideoCutFragment.this.mPresenter).getContract().stop();
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.preview.ICut$VP
            public void onPause() {
                ((VideoCutPresenter) VideoCutFragment.this.mPresenter).getContract().onPause();
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.preview.ICut$VP
            public void onResume() {
                ((VideoCutPresenter) VideoCutFragment.this.mPresenter).getContract().onResume();
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.preview.ICut$VP
            public void onRePlayVideo() {
                ((VideoCutPresenter) VideoCutFragment.this.mPresenter).getContract().onRePlayVideo();
            }
        };
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public void initView() {
        this.mTextureView = (TextureView) findViewById(R$id.magic_video_preview);
        TextView textView = (TextView) findViewById(R$id.magic_a_bar_cancel);
        AnimParams build = new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build();
        FolmeUtil.setCustomTouchAnim(textView, build, null, null, null, true);
        FolmeUtil.setCustomTouchAnim((TextView) findViewById(R$id.magic_a_bar_save), build, null, null, null, true);
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public void initData() {
        getContract().initVideoData(getActivity().getIntent());
        getContract().setSurfaceTextureListener();
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public VideoCutPresenter getPresenterInstance() {
        return new VideoCutPresenter();
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public int getLayoutId() {
        return R$layout.ts_magic_video_cut;
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        getContract().onResume();
        super.onResume();
    }

    @Override // androidx.fragment.app.Fragment
    public void onPause() {
        getContract().onPause();
        super.onPause();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R$id.magic_video_play_btn) {
            getContract().onPlayVideo();
        } else if (id == R$id.magic_a_bar_save) {
            if (System.currentTimeMillis() - lastClickTime <= 500) {
                return;
            }
            getContract().onPause();
            getBaseActivity().event(6);
        } else if (id != R$id.magic_a_bar_cancel) {
        } else {
            getBaseActivity().finish();
        }
    }
}
