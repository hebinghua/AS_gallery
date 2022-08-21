package com.miui.gallery.movie.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.airbnb.lottie.LottieAnimationView;
import com.miui.gallery.app.fragment.AndroidFragment;
import com.miui.gallery.editor.ui.view.switchview.SlideSwitchView2;
import com.miui.gallery.imodule.loader.ModuleRegistry;
import com.miui.gallery.imodule.modules.MovieDependsModule;
import com.miui.gallery.listener.SingleClickListener;
import com.miui.gallery.movie.R$id;
import com.miui.gallery.movie.R$layout;
import com.miui.gallery.movie.R$string;
import com.miui.gallery.movie.core.MovieManager;
import com.miui.gallery.movie.entity.MovieInfo;
import com.miui.gallery.movie.ui.listener.MenuFragmentListener;
import com.miui.gallery.movie.utils.stat.MovieStatUtils;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.mediaeditor.utils.MediaEditorUtils;

/* loaded from: classes2.dex */
public class MovieEditorTopMenuFragment extends AndroidFragment {
    public boolean mDeleteClicked;
    public LottieAnimationView mDownloadMediaEditorAppView;
    public IVideoModeChangedCallback mIVideoModeChangedCallback;
    public boolean mIsShortVideo;
    public MenuFragmentListener mMenuFragmentListener;
    public MovieInfo mMovieInfo;
    public MovieManager mMovieManager;
    public SlideSwitchView2 mMovieSwitchView;
    public View mReturn;
    public View mSave;
    public SlideSwitchView2.SwitchClickableListener mSwitchClickableListener = new SlideSwitchView2.SwitchClickableListener() { // from class: com.miui.gallery.movie.ui.fragment.MovieEditorTopMenuFragment.1
        {
            MovieEditorTopMenuFragment.this = this;
        }

        @Override // com.miui.gallery.editor.ui.view.switchview.SlideSwitchView2.SwitchClickableListener
        public boolean canSwitchClick() {
            if (MovieEditorTopMenuFragment.this.mMovieSwitchView == null || MovieEditorTopMenuFragment.this.mMovieManager == null) {
                return true;
            }
            return MovieEditorTopMenuFragment.this.mMovieManager.isReadyForSwitch();
        }
    };
    public MovieDependsModule.Callback mInstallMediaEditorCallback = new MovieDependsModule.Callback() { // from class: com.miui.gallery.movie.ui.fragment.MovieEditorTopMenuFragment.2
        {
            MovieEditorTopMenuFragment.this = this;
        }

        @Override // com.miui.gallery.imodule.modules.MovieDependsModule.Callback
        public void onInstallSuccess() {
            if (MovieEditorTopMenuFragment.this.mDownloadMediaEditorAppView != null) {
                MovieEditorTopMenuFragment.this.mDownloadMediaEditorAppView.setVisibility(MediaEditorUtils.isMediaEditorAvailable() ? 8 : 0);
            }
        }
    };
    public SlideSwitchView2.OnSelectChangeListener mOnSelectChangeListener = new SlideSwitchView2.OnSelectChangeListener() { // from class: com.miui.gallery.movie.ui.fragment.MovieEditorTopMenuFragment.3
        {
            MovieEditorTopMenuFragment.this = this;
        }

        @Override // com.miui.gallery.editor.ui.view.switchview.SlideSwitchView2.OnSelectChangeListener
        public void onSelectChanged(int i) {
            if (i == 0) {
                if (MovieEditorTopMenuFragment.this.mMovieInfo.isShortVideo) {
                    return;
                }
                if (MovieEditorTopMenuFragment.this.mMovieManager.isReadyForSwitch()) {
                    MovieStatUtils.statDurationClick(MovieEditorTopMenuFragment.this.mMovieInfo, false, true);
                    MovieEditorTopMenuFragment.this.mIsShortVideo = true;
                    if (MovieEditorTopMenuFragment.this.mMovieInfo.changeToShortVideo()) {
                        if (MovieEditorTopMenuFragment.this.mIVideoModeChangedCallback != null) {
                            MovieEditorTopMenuFragment.this.mIVideoModeChangedCallback.videoModeChanged();
                        }
                        MovieEditorTopMenuFragment.this.mMovieManager.resetImage(MovieEditorTopMenuFragment.this.mMovieInfo.imageList, true);
                    } else {
                        MovieEditorTopMenuFragment.this.mMovieManager.setIsShortVideo(true);
                    }
                    MovieEditorTopMenuFragment.this.mMovieManager.replay();
                    if (!MovieEditorTopMenuFragment.this.mMovieInfo.isFromStory) {
                        return;
                    }
                    MovieEditorTopMenuFragment.this.mMenuFragmentListener.updateStorySha1Data();
                    return;
                }
                MovieEditorTopMenuFragment.this.setShortRadioButtonCheck(false);
            } else if (i != 1 || !MovieEditorTopMenuFragment.this.mMovieInfo.isShortVideo) {
            } else {
                if (MovieEditorTopMenuFragment.this.mMovieManager.isReadyForSwitch()) {
                    MovieStatUtils.statDurationClick(MovieEditorTopMenuFragment.this.mMovieInfo, false, false);
                    MovieEditorTopMenuFragment.this.mIsShortVideo = false;
                    if (MovieEditorTopMenuFragment.this.mMovieInfo.backToLongVideo()) {
                        if (MovieEditorTopMenuFragment.this.mIVideoModeChangedCallback != null) {
                            MovieEditorTopMenuFragment.this.mIVideoModeChangedCallback.videoModeChanged();
                        }
                        MovieEditorTopMenuFragment.this.mMovieManager.resetImage(MovieEditorTopMenuFragment.this.mMovieInfo.imageList, false);
                    } else {
                        MovieEditorTopMenuFragment.this.mMovieManager.setIsShortVideo(false);
                    }
                    MovieEditorTopMenuFragment.this.mMovieManager.replay();
                    if (!MovieEditorTopMenuFragment.this.mMovieInfo.isFromStory) {
                        return;
                    }
                    MovieEditorTopMenuFragment.this.mMenuFragmentListener.updateStorySha1Data();
                    return;
                }
                MovieEditorTopMenuFragment.this.setShortRadioButtonCheck(true);
            }
        }
    };
    public SingleClickListener mSingleClickListener = new SingleClickListener() { // from class: com.miui.gallery.movie.ui.fragment.MovieEditorTopMenuFragment.4
        {
            MovieEditorTopMenuFragment.this = this;
        }

        @Override // com.miui.gallery.listener.SingleClickListener
        public void onSingleClick(View view) {
            int id = view.getId();
            if (id == R$id.btn_movie_save) {
                LinearMotorHelper.performHapticFeedback(view, LinearMotorHelper.HAPTIC_TAP_NORMAL);
                MovieEditorTopMenuFragment.this.mMenuFragmentListener.export(false);
            } else if (id != R$id.btn_movie_return) {
            } else {
                LinearMotorHelper.performHapticFeedback(view, LinearMotorHelper.HAPTIC_TAP_NORMAL);
                MovieEditorTopMenuFragment.this.mMenuFragmentListener.returnClick();
            }
        }
    };

    /* loaded from: classes2.dex */
    public interface IVideoModeChangedCallback {
        void videoModeChanged();
    }

    public static /* synthetic */ void $r8$lambda$D9R9uqJuH8HzA1nkOb7etuBQiMU(MovieEditorTopMenuFragment movieEditorTopMenuFragment) {
        movieEditorTopMenuFragment.lambda$initView$0();
    }

    public static /* synthetic */ void $r8$lambda$xLNSxwp_VR5NtMqRDfjxt9EOLM0(MovieEditorTopMenuFragment movieEditorTopMenuFragment, View view) {
        movieEditorTopMenuFragment.lambda$initView$1(view);
    }

    public void setVideoModeChangedCallback(IVideoModeChangedCallback iVideoModeChangedCallback) {
        this.mIVideoModeChangedCallback = iVideoModeChangedCallback;
    }

    @Override // com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public void setShortRadioButtonCheck(boolean z) {
        this.mIsShortVideo = z;
        refreshRadioButtonState(z);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = View.inflate(viewGroup.getContext(), R$layout.movie_top_layout, null);
        initView(inflate);
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MenuFragmentListener) {
            MenuFragmentListener menuFragmentListener = (MenuFragmentListener) context;
            this.mMenuFragmentListener = menuFragmentListener;
            this.mMovieManager = menuFragmentListener.getMovieManager();
            this.mMovieInfo = this.mMenuFragmentListener.getMovieInfo();
        }
    }

    public final void initView(View view) {
        this.mMovieSwitchView = (SlideSwitchView2) view.findViewById(R$id.switch_button);
        LottieAnimationView lottieAnimationView = (LottieAnimationView) view.findViewById(com.miui.gallery.editor.R$id.download_mediaeditor_app_view);
        this.mDownloadMediaEditorAppView = lottieAnimationView;
        lottieAnimationView.post(new Runnable() { // from class: com.miui.gallery.movie.ui.fragment.MovieEditorTopMenuFragment$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                MovieEditorTopMenuFragment.$r8$lambda$D9R9uqJuH8HzA1nkOb7etuBQiMU(MovieEditorTopMenuFragment.this);
            }
        });
        this.mDownloadMediaEditorAppView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.movie.ui.fragment.MovieEditorTopMenuFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                MovieEditorTopMenuFragment.$r8$lambda$xLNSxwp_VR5NtMqRDfjxt9EOLM0(MovieEditorTopMenuFragment.this, view2);
            }
        });
        this.mMovieSwitchView.initTexts(getResources().getString(R$string.movie_video_short, 10), getResources().getString(R$string.movie_video_long));
        this.mMovieSwitchView.setSwitchClickableListener(this.mSwitchClickableListener);
        this.mMovieSwitchView.setOnSelectChangeListener(this.mOnSelectChangeListener);
        boolean z = this.mMovieInfo.isShortVideo;
        this.mIsShortVideo = z;
        refreshRadioButtonState(z);
        this.mSave = view.findViewById(R$id.btn_movie_save);
        this.mReturn = view.findViewById(R$id.btn_movie_return);
        this.mSave.setOnClickListener(this.mSingleClickListener);
        this.mReturn.setOnClickListener(this.mSingleClickListener);
        AnimParams build = new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build();
        FolmeUtil.setCustomTouchAnim(this.mSave, build, null, null, null, true);
        FolmeUtil.setCustomTouchAnim(this.mReturn, build, null, null, null, true);
    }

    public /* synthetic */ void lambda$initView$0() {
        this.mDownloadMediaEditorAppView.setVisibility(MediaEditorUtils.isMediaEditorAvailable() ? 8 : 0);
        this.mDownloadMediaEditorAppView.playAnimation();
    }

    public /* synthetic */ void lambda$initView$1(View view) {
        ((MovieDependsModule) ModuleRegistry.getModule(MovieDependsModule.class)).installIfNotExist(getActivity(), this.mInstallMediaEditorCallback, true);
    }

    @Override // androidx.fragment.app.Fragment
    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
        if (!z) {
            boolean z2 = this.mIsShortVideo;
            boolean z3 = this.mMovieInfo.isShortVideo;
            if (z2 != z3) {
                setShortRadioButtonCheck(z3);
            }
            if (this.mDeleteClicked) {
                this.mDeleteClicked = false;
            }
            IVideoModeChangedCallback iVideoModeChangedCallback = this.mIVideoModeChangedCallback;
            if (iVideoModeChangedCallback == null) {
                return;
            }
            iVideoModeChangedCallback.videoModeChanged();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        ((MovieDependsModule) ModuleRegistry.getModule(MovieDependsModule.class)).removeInstallListener();
    }

    public final void refreshRadioButtonState(boolean z) {
        DefaultLogger.d("MovieEditorTopMenuFragment", "isShortVideoSelected  " + z);
        this.mMovieSwitchView.setSelected(!z ? 1 : 0);
    }
}
