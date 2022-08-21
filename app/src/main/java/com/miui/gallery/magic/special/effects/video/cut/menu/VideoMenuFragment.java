package com.miui.gallery.magic.special.effects.video.cut.menu;

import android.util.Size;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.R$string;
import com.miui.gallery.magic.base.BaseFragment;
import com.miui.gallery.magic.util.RecordSettings;
import com.miui.gallery.magic.widget.frame.CoverView;
import com.miui.gallery.magic.widget.frame.FrameSelectorView;

/* loaded from: classes2.dex */
public class VideoMenuFragment extends BaseFragment<VideoMenuPresenter, IMenu$VP> {
    public LinearLayout mBodyImage;
    public CoverView mCoverView;
    public FrameSelectorView mFrameSelectorView;
    public TextView mTotalTime;

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.magic.base.BaseFragment
    /* renamed from: initContract */
    public IMenu$VP mo1066initContract() {
        return new IMenu$VP() { // from class: com.miui.gallery.magic.special.effects.video.cut.menu.VideoMenuFragment.1
            @Override // com.miui.gallery.magic.special.effects.video.cut.menu.IMenu$VP
            public void loadListData() {
                ((VideoMenuPresenter) VideoMenuFragment.this.mPresenter).getContract().loadListData();
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.menu.IMenu$VP
            public void setProgress(int i) {
                VideoMenuFragment.this.mFrameSelectorView.setSeekBarProgress(i);
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.menu.IMenu$VP
            public void seekTo(long j, boolean z) {
                ((VideoMenuPresenter) VideoMenuFragment.this.mPresenter).getContract().seekTo(j, z);
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.menu.IMenu$VP
            public void onStopTrackingTouch() {
                ((VideoMenuPresenter) VideoMenuFragment.this.mPresenter).getContract().onStopTrackingTouch();
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.menu.IMenu$VP
            public void addImageToBody(ImageView imageView) {
                VideoMenuFragment.this.mBodyImage.addView(imageView);
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.menu.IMenu$VP
            public void initFinish(Size size) {
                ((VideoMenuPresenter) VideoMenuFragment.this.mPresenter).getContract().initFinish(size);
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.menu.IMenu$VP
            public void cutVideo() {
                ((VideoMenuPresenter) VideoMenuFragment.this.mPresenter).getContract().cutVideo();
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.menu.IMenu$VP
            public int[] getCurrentTimes(int i) {
                int[] currentTimes = VideoMenuFragment.this.mFrameSelectorView.getCurrentTimes(i);
                if (currentTimes.length > 1) {
                    String gapTime = RecordSettings.getGapTime((currentTimes[1] - currentTimes[0]) + 1);
                    VideoMenuFragment.this.mTotalTime.setText(gapTime);
                    TextView textView = VideoMenuFragment.this.mTotalTime;
                    textView.setContentDescription(gapTime.substring(0, 2) + VideoMenuFragment.this.getContext().getResources().getString(R$string.acc_minutes) + gapTime.substring(3) + VideoMenuFragment.this.getContext().getResources().getString(R$string.acc_seconds));
                }
                return currentTimes;
            }

            @Override // com.miui.gallery.magic.special.effects.video.cut.menu.IMenu$VP
            public void setDuration(int i) {
                VideoMenuFragment.this.mFrameSelectorView.setDuration(i);
            }
        };
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public void initView() {
        this.mCoverView = (CoverView) findViewById(R$id.cover_view);
        this.mFrameSelectorView = (FrameSelectorView) findViewById(R$id.magic_video_cut_selector);
        this.mBodyImage = (LinearLayout) findViewById(R$id.magic_cut_body_image);
        this.mTotalTime = (TextView) findViewById(R$id.magic_video_play_total_time);
        this.mFrameSelectorView.setHandlerSideBarListener(new FrameSelectorView.OnHandleSideBarListener() { // from class: com.miui.gallery.magic.special.effects.video.cut.menu.VideoMenuFragment.2
            @Override // com.miui.gallery.magic.widget.frame.FrameSelectorView.OnHandleSideBarListener
            public void handleSideBar(int i, boolean z) {
                VideoMenuFragment.this.getBaseActivity().event(11);
                VideoMenuFragment.this.getContract().seekTo(i, z);
            }
        });
        this.mFrameSelectorView.setHandlerBarPositionCallback(new FrameSelectorView.OnHandlerBarPositionCallback() { // from class: com.miui.gallery.magic.special.effects.video.cut.menu.VideoMenuFragment.3
            @Override // com.miui.gallery.magic.widget.frame.FrameSelectorView.OnHandlerBarPositionCallback
            public void update(int i, int i2) {
                VideoMenuFragment.this.mCoverView.updateRect(i, i2);
            }
        });
        this.mFrameSelectorView.setProgressChangeLister(new FrameSelectorView.ProgressChangeLister() { // from class: com.miui.gallery.magic.special.effects.video.cut.menu.VideoMenuFragment.4
            @Override // com.miui.gallery.magic.widget.frame.FrameSelectorView.ProgressChangeLister
            public void onProgress(int i, boolean z) {
                VideoMenuFragment.this.getContract().seekTo(i, z);
            }

            @Override // com.miui.gallery.magic.widget.frame.FrameSelectorView.ProgressChangeLister
            public void onStartTouch(int i) {
                VideoMenuFragment.this.getBaseActivity().event(12);
            }

            @Override // com.miui.gallery.magic.widget.frame.FrameSelectorView.ProgressChangeLister
            public void onTouchMove() {
                VideoMenuFragment.this.getContract().getCurrentTimes(((Integer) VideoMenuFragment.this.getBaseActivity().event(3)).intValue());
            }

            @Override // com.miui.gallery.magic.widget.frame.FrameSelectorView.ProgressChangeLister
            public void onTouchUpFrame() {
                VideoMenuFragment.this.getBaseActivity().event(9);
            }

            @Override // com.miui.gallery.magic.widget.frame.FrameSelectorView.ProgressChangeLister
            public void onStopTrackingTouch() {
                VideoMenuFragment.this.getContract().onStopTrackingTouch();
            }
        });
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public void initData() {
        getContract().loadListData();
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public VideoMenuPresenter getPresenterInstance() {
        return new VideoMenuPresenter();
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public int getLayoutId() {
        return R$layout.ts_magic_video_cut_menu;
    }
}
