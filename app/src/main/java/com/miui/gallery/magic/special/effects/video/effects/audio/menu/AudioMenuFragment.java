package com.miui.gallery.magic.special.effects.video.effects.audio.menu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.base.BaseFragment;
import com.miui.gallery.magic.base.BasePresenter;
import com.miui.gallery.magic.special.effects.video.VideoEffectsActivity;
import com.miui.gallery.magic.widget.MagicBubbleSeekBar;
import com.xiaomi.stat.c.b;

/* loaded from: classes2.dex */
public class AudioMenuFragment extends BaseFragment {
    public VideoEffectsActivity mRootActivity;
    public MagicBubbleSeekBar mSeekBar;
    public View rootView;

    @Override // com.miui.gallery.magic.base.BaseFragment
    public BasePresenter getPresenterInstance() {
        return null;
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    /* renamed from: initContract */
    public Object mo1066initContract() {
        return null;
    }

    @Override // com.miui.gallery.magic.base.BaseFragment, androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        super.onCreateView(layoutInflater, viewGroup, bundle);
        View inflate = layoutInflater.inflate(getLayoutId(), viewGroup, false);
        this.rootView = inflate;
        return inflate;
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public int getLayoutId() {
        return R$layout.ts_magic_video_effects_audio_menu;
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public void initData() {
        this.mRootActivity = (VideoEffectsActivity) getActivity();
        this.mSeekBar.setProgressListener(new MagicBubbleSeekBar.ProgressListener() { // from class: com.miui.gallery.magic.special.effects.video.effects.audio.menu.AudioMenuFragment.1
            @Override // com.miui.gallery.magic.widget.MagicBubbleSeekBar.ProgressListener
            public void onProgressChanged(MagicBubbleSeekBar magicBubbleSeekBar, int i) {
            }

            @Override // com.miui.gallery.magic.widget.MagicBubbleSeekBar.ProgressListener
            public void onStartTrackingTouch(MagicBubbleSeekBar magicBubbleSeekBar) {
            }

            @Override // com.miui.gallery.magic.widget.MagicBubbleSeekBar.ProgressListener
            public void onStopTrackingTouch(MagicBubbleSeekBar magicBubbleSeekBar) {
                AudioMenuFragment audioMenuFragment = AudioMenuFragment.this;
                audioMenuFragment.mRootActivity.event(b.h, Float.valueOf(audioMenuFragment.mSeekBar.getCurrentProgress() / 100.0f));
            }
        });
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public View findViewById(int i) {
        return this.rootView.findViewById(i);
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public void initView() {
        this.mSeekBar = (MagicBubbleSeekBar) findViewById(R$id.magic_video_audio_seek);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R$id.magic_video_close || id == R$id.magic_video_open) {
            return;
        }
        if (id != R$id.magic_cancel && id != R$id.magic_ok) {
            return;
        }
        this.mRootActivity.changeMenu();
    }
}
