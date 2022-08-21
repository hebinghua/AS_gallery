package com.miui.gallery.movie.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import com.miui.gallery.app.fragment.AndroidFragment;
import com.miui.gallery.movie.R$dimen;
import com.miui.gallery.movie.R$id;
import com.miui.gallery.movie.R$integer;
import com.miui.gallery.movie.R$layout;
import com.miui.gallery.movie.core.MovieManager;
import com.miui.gallery.movie.entity.MovieInfo;
import com.miui.gallery.movie.ui.listener.MenuFragmentListener;
import com.miui.gallery.movie.ui.view.IMultiThumbnailSequenceView;
import com.miui.gallery.movie.ui.view.MoviePreviewMenuBottomView;
import com.miui.gallery.movie.ui.view.MoviePreviewMenuTitle;
import com.miui.gallery.movie.ui.view.StrokeTextView;
import com.miui.gallery.movie.utils.stat.MovieStatUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Locale;
import miuix.view.animation.CubicEaseOutInterpolator;
import miuix.view.animation.QuarticEaseOutInterpolator;

/* loaded from: classes2.dex */
public class MoviePreviewMenuFragment extends AndroidFragment {
    public StrokeTextView mCurrentTimeView;
    public int mEndPadding;
    public MoviePreviewMenuBottomView mMenuBottomView;
    public MenuFragmentListener mMenuFragmentListener;
    public MoviePreviewMenuTitle mMenuTitleView;
    public MovieInfo mMovieInfo;
    public MovieManager mMovieManager;
    public double mPixelPerMicroSeconds;
    public int mSequenceWidth;
    public int mStartPadding;
    public IMultiThumbnailSequenceView mThumbnailSequenceView;
    public View mWhiteView;
    public boolean mIsSeekTimeline = true;
    public MoviePreviewMenuTitle.IMenuTitleClickListener mMenuTitleClickListener = new MoviePreviewMenuTitle.IMenuTitleClickListener() { // from class: com.miui.gallery.movie.ui.fragment.MoviePreviewMenuFragment.2
        {
            MoviePreviewMenuFragment.this = this;
        }

        @Override // com.miui.gallery.movie.ui.view.MoviePreviewMenuTitle.IMenuTitleClickListener
        public boolean onShortVideoTitleViewClick() {
            if (!MoviePreviewMenuFragment.this.mMovieManager.isReadyForSwitch()) {
                return false;
            }
            if (MoviePreviewMenuFragment.this.mMovieInfo.changeToShortVideo()) {
                MoviePreviewMenuFragment.this.mMovieManager.resetImage(MoviePreviewMenuFragment.this.mMovieInfo.imageList, true);
                MoviePreviewMenuFragment.this.updateMultiThumbnailView();
            } else {
                MoviePreviewMenuFragment.this.mMovieManager.setIsShortVideo(true);
            }
            MoviePreviewMenuFragment.this.mMovieManager.replay();
            if (MoviePreviewMenuFragment.this.mMenuFragmentListener != null) {
                MoviePreviewMenuFragment.this.mMenuFragmentListener.updateShareData(true);
                MoviePreviewMenuFragment.this.mMenuFragmentListener.updateStorySha1Data();
            }
            MovieStatUtils.statDurationClick(MoviePreviewMenuFragment.this.mMovieInfo, true, true);
            return true;
        }

        @Override // com.miui.gallery.movie.ui.view.MoviePreviewMenuTitle.IMenuTitleClickListener
        public boolean onLongVideoTitleViewClick() {
            if (!MoviePreviewMenuFragment.this.mMovieManager.isReadyForSwitch()) {
                return false;
            }
            if (MoviePreviewMenuFragment.this.mMovieInfo.backToLongVideo()) {
                MoviePreviewMenuFragment.this.mMovieManager.resetImage(MoviePreviewMenuFragment.this.mMovieInfo.imageList, false);
                MoviePreviewMenuFragment.this.updateMultiThumbnailView();
            } else {
                MoviePreviewMenuFragment.this.mMovieManager.setIsShortVideo(false);
            }
            MoviePreviewMenuFragment.this.mMovieManager.replay();
            if (MoviePreviewMenuFragment.this.mMenuFragmentListener != null) {
                MoviePreviewMenuFragment.this.mMenuFragmentListener.updateShareData(false);
                MoviePreviewMenuFragment.this.mMenuFragmentListener.updateStorySha1Data();
            }
            MovieStatUtils.statDurationClick(MoviePreviewMenuFragment.this.mMovieInfo, true, false);
            return true;
        }
    };
    public MoviePreviewMenuBottomView.IMenuBottomViewClickListener mMenuBottomViewClickListener = new MoviePreviewMenuBottomView.IMenuBottomViewClickListener() { // from class: com.miui.gallery.movie.ui.fragment.MoviePreviewMenuFragment.3
        {
            MoviePreviewMenuFragment.this = this;
        }

        @Override // com.miui.gallery.movie.ui.view.MoviePreviewMenuBottomView.IMenuBottomViewClickListener
        public void onSaveBtnClick() {
            if (MoviePreviewMenuFragment.this.mMenuFragmentListener != null) {
                MoviePreviewMenuFragment.this.mMenuFragmentListener.export(true);
            }
        }

        @Override // com.miui.gallery.movie.ui.view.MoviePreviewMenuBottomView.IMenuBottomViewClickListener
        public void onEditBtnClick() {
            if (MoviePreviewMenuFragment.this.mMenuFragmentListener != null) {
                MoviePreviewMenuFragment.this.mMenuFragmentListener.changeEditor();
                MovieStatUtils.statPreviewEnterEditPage(MoviePreviewMenuFragment.this.mMovieInfo.isShortVideo);
            }
        }

        @Override // com.miui.gallery.movie.ui.view.MoviePreviewMenuBottomView.IMenuBottomViewClickListener
        public void onPlayAreaClick() {
            if (MoviePreviewMenuFragment.this.mMovieManager.getState() == 1) {
                MoviePreviewMenuFragment.this.mMovieManager.pause();
            } else {
                MoviePreviewMenuFragment.this.mMovieManager.resume();
            }
            MoviePreviewMenuFragment.this.updateMenuBottomView();
            MovieStatUtils.statPreviewPlayBtn(MoviePreviewMenuFragment.this.mMovieInfo.isShortVideo);
        }
    };
    public ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.miui.gallery.movie.ui.fragment.MoviePreviewMenuFragment.4
        {
            MoviePreviewMenuFragment.this = this;
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            if (MoviePreviewMenuFragment.this.mMovieManager == null) {
                DefaultLogger.e("MoviePreviewMenuFragment", "onGlobalLayout MovieManager is null");
                return;
            }
            MoviePreviewMenuFragment moviePreviewMenuFragment = MoviePreviewMenuFragment.this;
            moviePreviewMenuFragment.mStartPadding = (moviePreviewMenuFragment.mThumbnailSequenceView.getRealView().getWidth() / 2) - (MoviePreviewMenuFragment.this.mWhiteView.getWidth() / 2);
            MoviePreviewMenuFragment moviePreviewMenuFragment2 = MoviePreviewMenuFragment.this;
            moviePreviewMenuFragment2.mEndPadding = (moviePreviewMenuFragment2.mThumbnailSequenceView.getRealView().getWidth() / 2) - (MoviePreviewMenuFragment.this.mWhiteView.getWidth() / 2);
            MoviePreviewMenuFragment.this.updateMultiThumbnailView();
            MoviePreviewMenuFragment.this.updateMenuTitleView();
            MoviePreviewMenuFragment.this.initListener();
            MoviePreviewMenuFragment.this.removeOnGlobalLayoutListener();
        }
    };

    public static /* synthetic */ void $r8$lambda$WLxsC8uXZEexQTjh9xFqhlK1GBc(MoviePreviewMenuFragment moviePreviewMenuFragment, int i, int i2) {
        moviePreviewMenuFragment.lambda$initListener$0(i, i2);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R$layout.movie_fragment_preview, viewGroup, false);
        initView(inflate);
        return inflate;
    }

    public final void initView(View view) {
        this.mMenuTitleView = (MoviePreviewMenuTitle) view.findViewById(R$id.preview_title_area);
        this.mMenuBottomView = (MoviePreviewMenuBottomView) view.findViewById(R$id.preivew_menu_bottom);
        IMultiThumbnailSequenceView createThumbnailSequenceView = this.mMovieManager.createThumbnailSequenceView(getActivity());
        this.mThumbnailSequenceView = createThumbnailSequenceView;
        ((ViewGroup) view.findViewById(R$id.thumbnail_view_root)).addView(createThumbnailSequenceView.getRealView(), 0);
        this.mCurrentTimeView = (StrokeTextView) view.findViewById(R$id.tv_movie_duration);
        this.mWhiteView = view.findViewById(R$id.white_view);
        this.mPixelPerMicroSeconds = this.mThumbnailSequenceView.getPixelPerMicrosecond() * 2.0d;
        view.getViewTreeObserver().addOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
    }

    public final void removeOnGlobalLayoutListener() {
        getView().getViewTreeObserver().removeOnGlobalLayoutListener(this.mOnGlobalLayoutListener);
    }

    public void updateAfterSetInfo() {
        updateMultiThumbnailView();
        updateMenuBottomView();
    }

    public final void updateMultiThumbnailView() {
        this.mThumbnailSequenceView.setThumbnailAspectRatio(1.0f);
        this.mThumbnailSequenceView.setThumbnailImageFillMode(1);
        this.mThumbnailSequenceView.setThumbnailSequenceDescArray(this.mMovieManager.mo1161getThumbnailImages());
        this.mThumbnailSequenceView.setPixelPerMicrosecond(this.mPixelPerMicroSeconds);
        this.mThumbnailSequenceView.setStartPadding(this.mStartPadding);
        this.mThumbnailSequenceView.setEndPadding(this.mEndPadding);
        this.mSequenceWidth = this.mThumbnailSequenceView.getSequenceWidth(this.mMovieManager.getTotalTime(), this.mPixelPerMicroSeconds);
    }

    public final void updateMenuTitleView() {
        MovieInfo movieInfo = this.mMovieInfo;
        if (movieInfo != null) {
            this.mMenuTitleView.updateTitleViewState(movieInfo.isShortVideo);
        }
    }

    public final void updateMenuBottomView() {
        MoviePreviewMenuBottomView moviePreviewMenuBottomView = this.mMenuBottomView;
        boolean z = true;
        if (this.mMovieManager.getState() != 1) {
            z = false;
        }
        moviePreviewMenuBottomView.updatePlayBtnState(z);
    }

    public final void initListener() {
        this.mThumbnailSequenceView.setOnScrollChangeListener(new IMultiThumbnailSequenceView.OnScrollChangeListener() { // from class: com.miui.gallery.movie.ui.fragment.MoviePreviewMenuFragment$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.movie.ui.view.IMultiThumbnailSequenceView.OnScrollChangeListener
            public final void onScrollChanged(int i, int i2) {
                MoviePreviewMenuFragment.$r8$lambda$WLxsC8uXZEexQTjh9xFqhlK1GBc(MoviePreviewMenuFragment.this, i, i2);
            }
        });
        this.mMenuBottomView.setIMenuBottomViewClickListener(this.mMenuBottomViewClickListener);
        this.mMenuTitleView.setListener(this.mMenuTitleClickListener);
    }

    public /* synthetic */ void lambda$initListener$0(int i, int i2) {
        if (!this.mIsSeekTimeline) {
            return;
        }
        int floor = ((int) Math.floor((i / this.mPixelPerMicroSeconds) + 0.5d)) / 1000;
        MenuFragmentListener menuFragmentListener = this.mMenuFragmentListener;
        if (menuFragmentListener != null) {
            menuFragmentListener.seek(floor);
        }
        setDuration(floor);
    }

    public void onProgressChange(int i, int i2) {
        this.mIsSeekTimeline = false;
        this.mThumbnailSequenceView.scrollTo(Math.round(((i * 1.0f) / i2) * this.mSequenceWidth), 0);
        this.mIsSeekTimeline = true;
        setDuration(i);
    }

    public void onStateChanged(int i) {
        updateMenuBottomView();
    }

    public void onPlaybackEOF() {
        this.mIsSeekTimeline = false;
        this.mThumbnailSequenceView.fullScroll(17);
        this.mIsSeekTimeline = true;
        setDuration(0);
    }

    public final void setDuration(int i) {
        if (this.mCurrentTimeView != null) {
            this.mCurrentTimeView.setText(String.format(Locale.US, "00:%02d", Integer.valueOf(i / 1000)));
        }
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

    @Override // androidx.fragment.app.Fragment
    public void onHiddenChanged(boolean z) {
        super.onHiddenChanged(z);
        if (!z) {
            updateMultiThumbnailView();
            updateMenuTitleView();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public Animator onCreateAnimator(int i, boolean z, int i2) {
        ObjectAnimator objectAnimator = new ObjectAnimator();
        int dimension = (int) getResources().getDimension(R$dimen.movie_preview_height_offset);
        if (z) {
            objectAnimator.setValues(PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, dimension, 0.0f));
            objectAnimator.setInterpolator(new CubicEaseOutInterpolator());
            objectAnimator.setStartDelay(getResources().getInteger(R$integer.movie_preview_appear_delay));
            objectAnimator.setDuration(getResources().getInteger(R$integer.movie_preview_appear_duration));
            if (getView() != null) {
                getView().setAlpha(0.0f);
            }
            objectAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.movie.ui.fragment.MoviePreviewMenuFragment.1
                {
                    MoviePreviewMenuFragment.this = this;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    if (MoviePreviewMenuFragment.this.getView() != null) {
                        MoviePreviewMenuFragment.this.getView().setAlpha(1.0f);
                    }
                }
            });
        } else {
            objectAnimator.setValues(PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0.0f, dimension));
            objectAnimator.setInterpolator(new QuarticEaseOutInterpolator());
            objectAnimator.setDuration(getResources().getInteger(R$integer.movie_preview_disappear_duration));
        }
        return objectAnimator;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        MoviePreviewMenuTitle moviePreviewMenuTitle = this.mMenuTitleView;
        if (moviePreviewMenuTitle != null) {
            moviePreviewMenuTitle.removeListener();
        }
        MoviePreviewMenuBottomView moviePreviewMenuBottomView = this.mMenuBottomView;
        if (moviePreviewMenuBottomView != null) {
            moviePreviewMenuBottomView.removeListener();
        }
        removeOnGlobalLayoutListener();
    }
}
