package com.miui.gallery.magic.special.effects.video.effects.menu;

import android.graphics.Bitmap;
import android.graphics.Outline;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.magic.R$dimen;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.base.BaseFragment;
import com.miui.gallery.magic.special.effects.video.adapter.VideoMusicAdapter;
import com.miui.gallery.magic.special.effects.video.adapter.VideoSpecialAdapter;
import com.miui.gallery.magic.util.CacheManager;
import com.miui.gallery.magic.util.MagicLog;
import com.miui.gallery.magic.widget.VideoSpecialProgress;
import com.miui.gallery.magic.widget.scroll.ScrollLinearLayoutManager;
import com.miui.gallery.magic.widget.scroll.SimpleRecyclerViewMiuix;
import com.miui.gallery.ui.CenterSmoothScrollerController;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.xiaomi.stat.c.b;

/* loaded from: classes2.dex */
public class VideoMenuFragment extends BaseFragment<VideoMenuPresenter, IMenu$VP> {
    public boolean isShowLongHintBtn;
    public LinearLayout llVideoLongHint;
    public LinearLayout mBodyImage;
    public RelativeLayout mLayoutMagicBody;
    public RadioButton mRadioBtnEffect;
    public RadioButton mRadioBtnSound;
    public RadioGroup mRadioGroupTab;
    public SimpleRecyclerViewMiuix mRecycle;
    public SimpleRecyclerViewMiuix mSimpleRecycle;
    public VideoSpecialProgress mVideoSpecialProgress;
    public RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() { // from class: com.miui.gallery.magic.special.effects.video.effects.menu.VideoMenuFragment.4
        {
            VideoMenuFragment.this = this;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
        public void onScrollStateChanged(RecyclerView recyclerView, int i) {
            super.onScrollStateChanged(recyclerView, i);
            if (!VideoMenuFragment.this.isShowLongHintBtn) {
                return;
            }
            if (i == 0) {
                VideoMenuFragment.this.showOrHintTextView(true);
            } else if (i != 1) {
            } else {
                VideoMenuFragment.this.showOrHintTextView(false);
            }
        }
    };

    public static /* synthetic */ void $r8$lambda$C7bxRnbzXdt2sbxj1tHml7s2BnI(VideoMenuFragment videoMenuFragment, float f, VideoSpecialProgress.OnProgressType onProgressType) {
        videoMenuFragment.lambda$initData$1(f, onProgressType);
    }

    public static /* synthetic */ void $r8$lambda$ipZX81dg79TD5VllBa7hGoOlPXI(VideoMenuFragment videoMenuFragment, RadioGroup radioGroup, int i) {
        videoMenuFragment.lambda$initView$0(radioGroup, i);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    /* renamed from: initContract */
    public IMenu$VP mo1066initContract() {
        return new IMenu$VP() { // from class: com.miui.gallery.magic.special.effects.video.effects.menu.VideoMenuFragment.1
            {
                VideoMenuFragment.this = this;
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$VP
            public void loadListData() {
                ((VideoMenuPresenter) VideoMenuFragment.this.mPresenter).getContract().loadListData();
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$VP
            public void setAdapter(VideoSpecialAdapter videoSpecialAdapter) {
                VideoMenuFragment.this.mRecycle.setAdapter(videoSpecialAdapter);
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$VP
            public void setMusicAdapter(VideoMusicAdapter videoMusicAdapter) {
                VideoMenuFragment.this.mSimpleRecycle.setAdapter(videoMusicAdapter);
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$VP
            public void setProgress(float f, int i) {
                VideoMenuFragment.this.mVideoSpecialProgress.setProgress(f, i);
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$VP
            public void scrollToPositionMusicItem(int i) {
                VideoMenuFragment.this.mSimpleRecycle.setSpringEnabled(false);
                VideoMenuFragment.this.mSimpleRecycle.smoothScrollToPosition(i);
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$VP
            public void selectFile() {
                ((VideoMenuPresenter) VideoMenuFragment.this.mPresenter).getContract().selectFile();
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$VP
            public void changeToolBar(int i) {
                ((VideoMenuPresenter) VideoMenuFragment.this.mPresenter).getContract().changeToolBar(i);
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$VP
            public void addImageToBody(ImageView imageView, ViewGroup.LayoutParams layoutParams) {
                VideoMenuFragment.this.mBodyImage.addView(imageView, layoutParams);
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$VP
            public void setBodyImage(Bitmap bitmap, float f) {
                ((VideoMenuPresenter) VideoMenuFragment.this.mPresenter).getContract().setBodyImage(VideoMenuFragment.this.mBodyImage, bitmap, f);
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$VP
            public void setProgressDuration(float f) {
                VideoMenuFragment.this.mVideoSpecialProgress.setProgressDuration(f);
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$VP
            public void undo() {
                VideoMenuFragment.this.mVideoSpecialProgress.undo();
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$VP
            public void switchToVideoEffect() {
                VideoMenuFragment.this.getContract().changeToolBar(1);
                VideoMenuFragment.this.mLayoutMagicBody.setVisibility(0);
                VideoMenuFragment videoMenuFragment = VideoMenuFragment.this;
                videoMenuFragment.showOrHintTextView(videoMenuFragment.isShowLongHintBtn);
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$VP
            public void switchToAudioTrack() {
                VideoMenuFragment.this.getContract().changeToolBar(2);
                VideoMenuFragment.this.mLayoutMagicBody.setVisibility(8);
                VideoMenuFragment.this.showOrHintTextView(false);
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$VP
            public void startProgress(int i) {
                VideoMenuFragment.this.mVideoSpecialProgress.startType(i);
            }

            @Override // com.miui.gallery.magic.special.effects.video.effects.menu.IMenu$VP
            public void onActionUp(float f, float f2) {
                ((VideoMenuPresenter) VideoMenuFragment.this.mPresenter).getContract().onActionUp(f, f2);
            }
        };
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public void initView() {
        this.mRecycle = (SimpleRecyclerViewMiuix) findViewById(R$id.magic_video_recyclerview);
        this.mSimpleRecycle = (SimpleRecyclerViewMiuix) findViewById(R$id.srl_video_music);
        ScrollLinearLayoutManager scrollLinearLayoutManager = new ScrollLinearLayoutManager(getActivity());
        scrollLinearLayoutManager.setSmoothScroller(new CenterSmoothScrollerController(getActivity()));
        scrollLinearLayoutManager.setOrientation(0);
        this.mSimpleRecycle.setLayoutManager(scrollLinearLayoutManager);
        this.mRadioGroupTab = (RadioGroup) findViewById(R$id.magic_tab_radio_group);
        this.mRadioBtnEffect = (RadioButton) findViewById(R$id.magic_video_effect);
        this.mRadioBtnSound = (RadioButton) findViewById(R$id.magic_video_soundtrack);
        AnimParams build = new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build();
        FolmeUtil.setCustomTouchAnim(this.mRadioBtnEffect, build, null, null, null, true);
        FolmeUtil.setCustomTouchAnim(this.mRadioBtnSound, build, null, null, null, true);
        this.mLayoutMagicBody = (RelativeLayout) findViewById(R$id.rl_magic_body_image);
        this.mBodyImage = (LinearLayout) findViewById(R$id.magic_body_image);
        this.llVideoLongHint = (LinearLayout) findViewById(R$id.ll_video_long_hint);
        this.mVideoSpecialProgress = (VideoSpecialProgress) findViewById(R$id.magic_video_progress);
        this.mBodyImage.setClipToOutline(true);
        this.mBodyImage.setOutlineProvider(new ViewOutlineProvider() { // from class: com.miui.gallery.magic.special.effects.video.effects.menu.VideoMenuFragment.2
            {
                VideoMenuFragment.this = this;
            }

            @Override // android.view.ViewOutlineProvider
            public void getOutline(View view, Outline outline) {
                Rect rect = new Rect();
                view.getGlobalVisibleRect(rect);
                outline.setRoundRect(new Rect(0, 0, (rect.right - rect.left) - 0, (rect.bottom - rect.top) - 0), VideoMenuFragment.this.getActivity().getResources().getDimensionPixelSize(R$dimen.magic_18px));
            }
        });
        this.mLayoutMagicBody.setOutlineProvider(new ViewOutlineProvider() { // from class: com.miui.gallery.magic.special.effects.video.effects.menu.VideoMenuFragment.3
            {
                VideoMenuFragment.this = this;
            }

            @Override // android.view.ViewOutlineProvider
            public void getOutline(View view, Outline outline) {
                Rect rect = new Rect();
                view.getGlobalVisibleRect(rect);
                outline.setRoundRect(new Rect(0, 0, (rect.right - rect.left) - 0, (rect.bottom - rect.top) - 0), VideoMenuFragment.this.getActivity().getResources().getDimensionPixelSize(R$dimen.magic_18px));
            }
        });
        this.mRadioGroupTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.miui.gallery.magic.special.effects.video.effects.menu.VideoMenuFragment$$ExternalSyntheticLambda0
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public final void onCheckedChanged(RadioGroup radioGroup, int i) {
                VideoMenuFragment.$r8$lambda$ipZX81dg79TD5VllBa7hGoOlPXI(VideoMenuFragment.this, radioGroup, i);
            }
        });
        this.mRecycle.addOnScrollListener(this.scrollListener);
    }

    public /* synthetic */ void lambda$initView$0(RadioGroup radioGroup, int i) {
        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R$id.magic_video_effect) {
            ((VideoMenuPresenter) this.mPresenter).getActivity().event(1013);
            getContract().changeToolBar(1);
            this.mLayoutMagicBody.setVisibility(0);
            if (CacheManager.getBoolean("hide_long_text_msg", false)) {
                return;
            }
            showOrHintTextView(true);
        } else if (checkedRadioButtonId != R$id.magic_video_soundtrack) {
        } else {
            ((VideoMenuPresenter) this.mPresenter).getActivity().event(1014);
        }
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public void initData() {
        getContract().loadListData();
        setShowLongHintBtn(!CacheManager.getBoolean("hide_long_text_msg", false));
        this.mVideoSpecialProgress.setProgressChangeListener(new VideoSpecialProgress.OnProgressChangeListener() { // from class: com.miui.gallery.magic.special.effects.video.effects.menu.VideoMenuFragment$$ExternalSyntheticLambda1
            @Override // com.miui.gallery.magic.widget.VideoSpecialProgress.OnProgressChangeListener
            public final void onProgressChange(float f, VideoSpecialProgress.OnProgressType onProgressType) {
                VideoMenuFragment.$r8$lambda$C7bxRnbzXdt2sbxj1tHml7s2BnI(VideoMenuFragment.this, f, onProgressType);
            }
        });
    }

    /* renamed from: com.miui.gallery.magic.special.effects.video.effects.menu.VideoMenuFragment$5 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass5 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$magic$widget$VideoSpecialProgress$OnProgressType;

        static {
            int[] iArr = new int[VideoSpecialProgress.OnProgressType.values().length];
            $SwitchMap$com$miui$gallery$magic$widget$VideoSpecialProgress$OnProgressType = iArr;
            try {
                iArr[VideoSpecialProgress.OnProgressType.END.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$magic$widget$VideoSpecialProgress$OnProgressType[VideoSpecialProgress.OnProgressType.START.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$magic$widget$VideoSpecialProgress$OnProgressType[VideoSpecialProgress.OnProgressType.RUN.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    public /* synthetic */ void lambda$initData$1(float f, VideoSpecialProgress.OnProgressType onProgressType) {
        int i = AnonymousClass5.$SwitchMap$com$miui$gallery$magic$widget$VideoSpecialProgress$OnProgressType[onProgressType.ordinal()];
        if (i == 1) {
            if (!CacheManager.getBoolean("hide_long_text_msg", false)) {
                showOrHintTextView(true);
            }
            getBaseActivity().event(1016, Long.valueOf(f));
        } else if (i == 2) {
            showOrHintTextView(false);
            getBaseActivity().event(b.i);
        } else if (i != 3) {
        } else {
            getBaseActivity().event(1005, Long.valueOf(f));
        }
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public VideoMenuPresenter getPresenterInstance() {
        return new VideoMenuPresenter();
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public int getLayoutId() {
        return R$layout.ts_magic_video_menu;
    }

    @Override // com.miui.gallery.magic.base.BaseFragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        SimpleRecyclerViewMiuix simpleRecyclerViewMiuix = this.mRecycle;
        if (simpleRecyclerViewMiuix != null) {
            simpleRecyclerViewMiuix.removeOnScrollListener(this.scrollListener);
        }
    }

    public void showOrHintTextView(boolean z) {
        LinearLayoutManager linearLayoutManager;
        int findFirstVisibleItemPosition;
        if (z) {
            RecyclerView.LayoutManager layoutManager = this.mRecycle.getLayoutManager();
            if (!(layoutManager instanceof LinearLayoutManager) || (findFirstVisibleItemPosition = (linearLayoutManager = (LinearLayoutManager) layoutManager).findFirstVisibleItemPosition()) >= 2 || !this.isShowLongHintBtn || this.mLayoutMagicBody.getVisibility() != 0) {
                return;
            }
            this.llVideoLongHint.setVisibility(0);
            View view = null;
            if (findFirstVisibleItemPosition == 0) {
                view = linearLayoutManager.getChildAt(1);
            } else if (findFirstVisibleItemPosition == 1) {
                view = linearLayoutManager.getChildAt(0);
            }
            float x = view.getX();
            float width = ((view.getWidth() - this.llVideoLongHint.getWidth()) / 2.0f) + x;
            this.llVideoLongHint.setX(width);
            MagicLog magicLog = MagicLog.INSTANCE;
            magicLog.showLog("MagicLogger VideoMenuFragment", "getX " + x + " locationX " + width);
            return;
        }
        this.llVideoLongHint.setVisibility(4);
    }

    public void setShowLongHintBtn(boolean z) {
        this.isShowLongHintBtn = z;
    }
}
