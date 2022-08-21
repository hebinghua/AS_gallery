package com.miui.gallery.movie.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.SpringItemTouchHelper;
import androidx.viewpager.widget.PagerAdapter;
import com.airbnb.lottie.LottieAnimationView;
import com.miui.gallery.agreement.AgreementsUtils;
import com.miui.gallery.app.fragment.AndroidFragment;
import com.miui.gallery.imodule.loader.ModuleRegistry;
import com.miui.gallery.imodule.modules.MovieDependsModule;
import com.miui.gallery.movie.MovieConfig;
import com.miui.gallery.movie.R$dimen;
import com.miui.gallery.movie.R$id;
import com.miui.gallery.movie.R$integer;
import com.miui.gallery.movie.R$layout;
import com.miui.gallery.movie.R$raw;
import com.miui.gallery.movie.R$string;
import com.miui.gallery.movie.core.MovieManager;
import com.miui.gallery.movie.entity.AudioResource;
import com.miui.gallery.movie.entity.ImageEntity;
import com.miui.gallery.movie.entity.MovieInfo;
import com.miui.gallery.movie.entity.MovieResource;
import com.miui.gallery.movie.entity.TemplateResource;
import com.miui.gallery.movie.net.AudioResourceRequest;
import com.miui.gallery.movie.net.TemplateResourceRequest;
import com.miui.gallery.movie.ui.activity.MovieActivity;
import com.miui.gallery.movie.ui.adapter.AudioAdapter;
import com.miui.gallery.movie.ui.adapter.BaseAdapter;
import com.miui.gallery.movie.ui.adapter.EditAdapter;
import com.miui.gallery.movie.ui.adapter.TemplateAdapter;
import com.miui.gallery.movie.ui.factory.AudioFactory;
import com.miui.gallery.movie.ui.factory.TemplateFactory;
import com.miui.gallery.movie.ui.fragment.MovieEditorMenuFragment;
import com.miui.gallery.movie.ui.fragment.MovieEditorTopMenuFragment;
import com.miui.gallery.movie.ui.listener.MenuFragmentListener;
import com.miui.gallery.movie.ui.listener.MovieDownloadListener;
import com.miui.gallery.movie.utils.MovieDownloadManager;
import com.miui.gallery.movie.utils.MovieUtils;
import com.miui.gallery.movie.utils.stat.MovieStatUtils;
import com.miui.gallery.net.BaseGalleryRequest;
import com.miui.gallery.net.NetApi;
import com.miui.gallery.ui.CenterSmoothScrollerController;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.ConvertFilepathUtil;
import com.miui.gallery.util.OptionalResult;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.recyclerview.BlankDivider;
import com.miui.gallery.widget.recyclerview.CustomScrollerLinearLayoutManager;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerView;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import miuix.recyclerview.widget.RecyclerView;
import miuix.view.animation.QuarticEaseOutInterpolator;
import miuix.viewpager.widget.ViewPager;

/* loaded from: classes2.dex */
public class MovieEditorMenuFragment extends AndroidFragment {
    public AudioAdapter mAudioAdapter;
    public AudioResourceRequest mAudioResourceRequest;
    public Context mContext;
    public int mCurrentTabPos;
    public LottieAnimationView mDeleteAnimationView;
    public FrameLayout mDeleteArea;
    public boolean mDeleteClicked;
    public ImageView mDivide;
    public DownloadListener mDownloadListener;
    public EditAdapter mEditAdapter;
    public boolean mIsShortVideo;
    public String mLocalAudioPath;
    public MenuFragmentListener mMenuFragmentListener;
    public MovieEditorTopMenuFragment mMovieEditorPlayMenuFragment;
    public MovieInfo mMovieInfo;
    public MovieManager mMovieManager;
    public FrameLayout mPlayArea;
    public LottieAnimationView mPlayButtonAnimationView;
    public RecyclerView[] mRecyclerViews;
    public long mStoryMovieCardId;
    public TemplateAdapter mTemplateAdapter;
    public TemplateResourceRequest mTemplateResourceRequest;
    public TextView mTimeNow;
    public TextView mTimeTotal;
    public ViewPager mViewPager;
    public int mWaitPosition;
    public RecyclerView mWaitRecyclerView;
    public int mMovieLastStatus = 1;
    public MovieEditorTopMenuFragment.IVideoModeChangedCallback mIVideoModeChangedCallback = new MovieEditorTopMenuFragment.IVideoModeChangedCallback() { // from class: com.miui.gallery.movie.ui.fragment.MovieEditorMenuFragment.1
        {
            MovieEditorMenuFragment.this = this;
        }

        @Override // com.miui.gallery.movie.ui.fragment.MovieEditorTopMenuFragment.IVideoModeChangedCallback
        public void videoModeChanged() {
            MovieEditorMenuFragment.this.refreshEditAdapter();
        }
    };
    public RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() { // from class: com.miui.gallery.movie.ui.fragment.MovieEditorMenuFragment.2
        {
            MovieEditorMenuFragment.this = this;
        }

        @Override // android.widget.RadioGroup.OnCheckedChangeListener
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            boolean z = true;
            if (i == R$id.radio_button_template) {
                MovieEditorMenuFragment.this.onTabSelect(0);
                MovieEditorMenuFragment.this.showDeleteIcon(false);
            } else if (i == R$id.radio_button_music) {
                MovieEditorMenuFragment.this.onTabSelect(1);
                MovieEditorMenuFragment.this.showDeleteIcon(false);
            } else if (i == R$id.radio_button_edit) {
                MovieEditorMenuFragment.this.onTabSelect(2);
                MovieEditorMenuFragment.this.showDeleteIcon(true);
            }
            MenuFragmentListener menuFragmentListener = MovieEditorMenuFragment.this.mMenuFragmentListener;
            if (i != R$id.radio_button_edit) {
                z = false;
            }
            menuFragmentListener.setShowDeleteMode(z);
        }
    };
    public View.OnClickListener mPlayOnClickListener = new View.OnClickListener() { // from class: com.miui.gallery.movie.ui.fragment.MovieEditorMenuFragment.3
        {
            MovieEditorMenuFragment.this = this;
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (MovieEditorMenuFragment.this.mMovieManager.getState() == 1) {
                MovieEditorMenuFragment.this.mMovieManager.pause();
            } else {
                MovieEditorMenuFragment.this.mMovieManager.resume();
                MovieEditorMenuFragment.this.mDeleteAnimationView.setAlpha(0.3f);
                MovieEditorMenuFragment.this.mDeleteArea.setClickable(false);
                MovieEditorMenuFragment.this.mEditAdapter.setSelectedItemPosition(-1);
            }
            MovieStatUtils.statPreviewPlayBtn(MovieEditorMenuFragment.this.mMovieInfo.isShortVideo);
        }
    };
    public Animator.AnimatorListener mAnimatorListener = new Animator.AnimatorListener() { // from class: com.miui.gallery.movie.ui.fragment.MovieEditorMenuFragment.4
        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationRepeat(Animator animator) {
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationStart(Animator animator) {
        }

        {
            MovieEditorMenuFragment.this = this;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (MovieEditorMenuFragment.this.mMovieManager.getState() == 1) {
                MovieEditorMenuFragment.this.mDeleteAnimationView.setAlpha(0.3f);
                MovieEditorMenuFragment.this.mDeleteArea.setClickable(false);
            }
        }
    };

    /* renamed from: $r8$lambda$1aheZvHrfjRSzK7Z-qRZ_wACDdg */
    public static /* synthetic */ void m1154$r8$lambda$1aheZvHrfjRSzK7ZqRZ_wACDdg(MovieEditorMenuFragment movieEditorMenuFragment, int i, int i2) {
        movieEditorMenuFragment.lambda$initView$0(i, i2);
    }

    /* renamed from: $r8$lambda$A-nT6HjYc670ro5qMsdCNpLZdhw */
    public static /* synthetic */ void m1155$r8$lambda$AnT6HjYc670ro5qMsdCNpLZdhw(MovieEditorMenuFragment movieEditorMenuFragment, MovieResource movieResource, int i) {
        movieEditorMenuFragment.lambda$notifyResourceAdapter$9(movieResource, i);
    }

    /* renamed from: $r8$lambda$GN_xqMWuX1XWUh-I4GjXR5dUj2Q */
    public static /* synthetic */ boolean m1156$r8$lambda$GN_xqMWuX1XWUhI4GjXR5dUj2Q(MovieEditorMenuFragment movieEditorMenuFragment, TemplateResource templateResource) {
        return movieEditorMenuFragment.lambda$loadTemplate$3(templateResource);
    }

    public static /* synthetic */ void $r8$lambda$K6m0DjG5exHwIDmUD3yYepZWE8I(MovieEditorMenuFragment movieEditorMenuFragment, List list, TemplateResource templateResource) {
        movieEditorMenuFragment.lambda$loadTemplate$5(list, templateResource);
    }

    public static /* synthetic */ void $r8$lambda$TAT4Acw3eDcy2IYJcjmnJ52LdAQ(MovieEditorMenuFragment movieEditorMenuFragment, List list) {
        movieEditorMenuFragment.lambda$loadTemplate$6(list);
    }

    /* renamed from: $r8$lambda$Tfj6bPtpiASy4Z9N-kAP6hXH-HE */
    public static /* synthetic */ void m1157$r8$lambda$Tfj6bPtpiASy4Z9NkAP6hXHHE(MovieEditorMenuFragment movieEditorMenuFragment, View view) {
        movieEditorMenuFragment.lambda$initView$1(view);
    }

    public static /* synthetic */ boolean $r8$lambda$g8JV6zcROTWiGbMK4mAtlZRB9kk(MovieEditorMenuFragment movieEditorMenuFragment, TemplateResource templateResource) {
        return movieEditorMenuFragment.lambda$loadTemplate$4(templateResource);
    }

    public static /* synthetic */ void $r8$lambda$ul7POS0oA4LyYXT_YOArHdwF1gg(MovieEditorMenuFragment movieEditorMenuFragment, List list) {
        movieEditorMenuFragment.lambda$loadAudio$8(list);
    }

    @Override // com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (this.mMovieInfo.isFromStory) {
            Bundle arguments = getArguments();
            long j = 0;
            if (arguments != null) {
                j = arguments.getLong("card_id", 0L);
            }
            this.mStoryMovieCardId = j;
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R$layout.movie_menu_layout, (ViewGroup) null);
        if (!AgreementsUtils.isNetworkingAgreementAccepted()) {
            ToastUtils.makeText(getActivity(), R$string.movie_download_failed_for_notwork);
        }
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
        this.mMovieEditorPlayMenuFragment = ((MovieActivity) getActivity()).getMovieEditorPlayMenuFragment();
    }

    @SuppressLint({"RestrictedApi"})
    public final void initView(View view) {
        Context context = view.getContext();
        this.mContext = context;
        TemplateAdapter templateAdapter = new TemplateAdapter(context);
        this.mTemplateAdapter = templateAdapter;
        templateAdapter.setList(TemplateFactory.getLocalTemplateEntities());
        this.mRecyclerViews = new RecyclerView[3];
        AudioAdapter audioAdapter = new AudioAdapter(this.mContext);
        this.mAudioAdapter = audioAdapter;
        audioAdapter.setList(AudioFactory.getAllLocalAudios());
        this.mEditAdapter = new EditAdapter(this.mContext);
        refreshEditAdapter();
        this.mEditAdapter.setOnActionListener(new EditAdapter.OnActionListener() { // from class: com.miui.gallery.movie.ui.fragment.MovieEditorMenuFragment$$ExternalSyntheticLambda1
            @Override // com.miui.gallery.movie.ui.adapter.EditAdapter.OnActionListener
            public final void onMove(int i, int i2) {
                MovieEditorMenuFragment.m1154$r8$lambda$1aheZvHrfjRSzK7ZqRZ_wACDdg(MovieEditorMenuFragment.this, i, i2);
            }
        });
        ViewPager viewPager = (ViewPager) view.findViewById(R$id.view_pager);
        this.mViewPager = viewPager;
        viewPager.setAdapter(new ControllerPagerAdapter());
        ((RadioGroup) view.findViewById(R$id.radio_group_controller)).setOnCheckedChangeListener(this.mOnCheckedChangeListener);
        FrameLayout frameLayout = (FrameLayout) view.findViewById(R$id.movie_video_play_area);
        this.mPlayArea = frameLayout;
        frameLayout.setOnClickListener(this.mPlayOnClickListener);
        FrameLayout frameLayout2 = (FrameLayout) view.findViewById(R$id.movie_video_delete_area);
        this.mDeleteArea = frameLayout2;
        frameLayout2.setContentDescription(getContext().getResources().getString(R$string.movie_content_describe_delete));
        LottieAnimationView lottieAnimationView = (LottieAnimationView) view.findViewById(R$id.movie_video_delete);
        this.mDeleteAnimationView = lottieAnimationView;
        lottieAnimationView.setScale(1.0f / getResources().getDisplayMetrics().density);
        this.mDeleteAnimationView.setAnimation(R$raw.delete);
        this.mDeleteAnimationView.loop(false);
        this.mTimeNow = (TextView) view.findViewById(R$id.movie_video_time_now);
        this.mTimeTotal = (TextView) view.findViewById(R$id.movie_video_time_total);
        this.mDivide = (ImageView) view.findViewById(R$id.movie_video_time_divide);
        onTabSelect(this.mCurrentTabPos);
        this.mDeleteArea.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.movie.ui.fragment.MovieEditorMenuFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view2) {
                MovieEditorMenuFragment.m1157$r8$lambda$Tfj6bPtpiASy4Z9NkAP6hXHHE(MovieEditorMenuFragment.this, view2);
            }
        });
        loadTemplate();
        loadAudio();
        AnimParams build = new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(0.95f).build();
        FolmeUtil.setCustomTouchAnim(this.mPlayArea, build, null, null, null, true);
        FolmeUtil.setCustomTouchAnim(this.mDeleteArea, build, null, null, null, true);
        AnimParams build2 = new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build();
        FolmeUtil.setCustomTouchAnim(view.findViewById(R$id.radio_button_template), build2, null, null, null, true);
        FolmeUtil.setCustomTouchAnim(view.findViewById(R$id.radio_button_music), build2, null, null, null, true);
        FolmeUtil.setCustomTouchAnim(view.findViewById(R$id.radio_button_edit), build2, null, null, null, true);
        LottieAnimationView lottieAnimationView2 = (LottieAnimationView) view.findViewById(R$id.play_btn);
        this.mPlayButtonAnimationView = lottieAnimationView2;
        lottieAnimationView2.setScale(1.0f / getResources().getDisplayMetrics().density);
        this.mPlayButtonAnimationView.setAnimation(R$raw.pause_to_play);
        this.mPlayButtonAnimationView.setContentDescription(getContext().getResources().getString(R$string.movie_content_describe_play));
        this.mPlayButtonAnimationView.loop(false);
        MovieEditorTopMenuFragment movieEditorTopMenuFragment = this.mMovieEditorPlayMenuFragment;
        if (movieEditorTopMenuFragment != null) {
            movieEditorTopMenuFragment.setVideoModeChangedCallback(this.mIVideoModeChangedCallback);
        }
    }

    public /* synthetic */ void lambda$initView$0(int i, int i2) {
        MovieStatUtils.statEditorMove(this.mMovieInfo.imageList.size());
        this.mMovieManager.moveImage(i, i2);
        this.mDeleteAnimationView.setAlpha(0.3f);
        this.mDeleteArea.setClickable(false);
    }

    public /* synthetic */ void lambda$initView$1(View view) {
        onDeleteClick();
    }

    public void setTime(int i, int i2) {
        TextView textView;
        if (!isAdded() || (textView = this.mTimeNow) == null || this.mTimeTotal == null || this.mDivide == null) {
            return;
        }
        textView.setText(String.format(Locale.getDefault(), "%d:%02d", 0, Integer.valueOf(i / 1000)));
        this.mTimeTotal.setText(String.format(Locale.getDefault(), "%d:%02d", 0, Integer.valueOf(i2 / 1000)));
        this.mDivide.setVisibility(0);
    }

    public final void refreshEditAdapter() {
        this.mEditAdapter.setList(this.mMovieInfo.imageList);
        this.mEditAdapter.notifyDataSetChanged();
        this.mIsShortVideo = this.mMovieInfo.isShortVideo;
    }

    public final void setPlayButton(boolean z) {
        String string;
        this.mPlayButtonAnimationView.setAnimation(z ? R$raw.play_to_pause : R$raw.pause_to_play);
        LottieAnimationView lottieAnimationView = this.mPlayButtonAnimationView;
        if (z) {
            string = getContext().getResources().getString(R$string.movie_content_describe_pause);
        } else {
            string = getContext().getResources().getString(R$string.movie_content_describe_play);
        }
        lottieAnimationView.setContentDescription(string);
        this.mPlayButtonAnimationView.setProgress(1.0f);
    }

    public void changePlayButton() {
        int state = this.mMovieManager.getState();
        int i = this.mMovieLastStatus;
        if (state == i || ((i == 2 && this.mMovieManager.getState() == 3) || (this.mMovieLastStatus == 3 && this.mMovieManager.getState() == 2))) {
            this.mMovieLastStatus = this.mMovieManager.getState();
            return;
        }
        if (this.mMovieManager.getState() == 1) {
            setPlayButton(true);
        } else {
            setPlayButton(false);
        }
        this.mMovieLastStatus = this.mMovieManager.getState();
        this.mPlayButtonAnimationView.playAnimation();
    }

    public final void loadTemplate() {
        TemplateResourceRequest templateResourceRequest = new TemplateResourceRequest();
        this.mTemplateResourceRequest = templateResourceRequest;
        NetApi.create((BaseGalleryRequest) templateResourceRequest).observeOn(Schedulers.io()).map(MovieEditorMenuFragment$$ExternalSyntheticLambda6.INSTANCE).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.miui.gallery.movie.ui.fragment.MovieEditorMenuFragment$$ExternalSyntheticLambda2
            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                MovieEditorMenuFragment.$r8$lambda$TAT4Acw3eDcy2IYJcjmnJ52LdAQ(MovieEditorMenuFragment.this, (List) obj);
            }
        });
    }

    public static /* synthetic */ List lambda$loadTemplate$2(OptionalResult optionalResult) throws Exception {
        List list = (List) optionalResult.getIncludeNull();
        List<TemplateResource> localTemplateEntities = TemplateFactory.getLocalTemplateEntities();
        if (list != null) {
            MovieUtils.checkResourceExist(list);
            localTemplateEntities.addAll(list);
        }
        return localTemplateEntities;
    }

    public /* synthetic */ void lambda$loadTemplate$6(final List list) throws Exception {
        Observable.fromIterable(list).filter(new Predicate() { // from class: com.miui.gallery.movie.ui.fragment.MovieEditorMenuFragment$$ExternalSyntheticLambda7
            @Override // io.reactivex.functions.Predicate
            public final boolean test(Object obj) {
                return MovieEditorMenuFragment.m1156$r8$lambda$GN_xqMWuX1XWUhI4GjXR5dUj2Q(MovieEditorMenuFragment.this, (TemplateResource) obj);
            }
        }).filter(new Predicate() { // from class: com.miui.gallery.movie.ui.fragment.MovieEditorMenuFragment$$ExternalSyntheticLambda8
            @Override // io.reactivex.functions.Predicate
            public final boolean test(Object obj) {
                return MovieEditorMenuFragment.$r8$lambda$g8JV6zcROTWiGbMK4mAtlZRB9kk(MovieEditorMenuFragment.this, (TemplateResource) obj);
            }
        }).subscribe(new Consumer() { // from class: com.miui.gallery.movie.ui.fragment.MovieEditorMenuFragment$$ExternalSyntheticLambda4
            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                MovieEditorMenuFragment.$r8$lambda$K6m0DjG5exHwIDmUD3yYepZWE8I(MovieEditorMenuFragment.this, list, (TemplateResource) obj);
            }
        });
        this.mTemplateAdapter.setList(list);
        this.mTemplateAdapter.notifyDataSetChanged();
    }

    public /* synthetic */ boolean lambda$loadTemplate$3(TemplateResource templateResource) throws Exception {
        String str = this.mMovieInfo.template;
        return str != null && !TextUtils.equals(str, "movieAssetsNormal");
    }

    public /* synthetic */ boolean lambda$loadTemplate$4(TemplateResource templateResource) throws Exception {
        String str = templateResource.pathKey;
        return str != null && this.mMovieInfo.template.contains(str);
    }

    public /* synthetic */ void lambda$loadTemplate$5(List list, TemplateResource templateResource) throws Exception {
        templateResource.pathKey = this.mMovieInfo.template;
        this.mTemplateAdapter.setSelectedItemPositionWithoutNotify(list.indexOf(templateResource));
    }

    public final void loadAudio() {
        AudioResourceRequest audioResourceRequest = new AudioResourceRequest();
        this.mAudioResourceRequest = audioResourceRequest;
        NetApi.create((BaseGalleryRequest) audioResourceRequest).observeOn(Schedulers.io()).map(MovieEditorMenuFragment$$ExternalSyntheticLambda5.INSTANCE).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer() { // from class: com.miui.gallery.movie.ui.fragment.MovieEditorMenuFragment$$ExternalSyntheticLambda3
            @Override // io.reactivex.functions.Consumer
            public final void accept(Object obj) {
                MovieEditorMenuFragment.$r8$lambda$ul7POS0oA4LyYXT_YOArHdwF1gg(MovieEditorMenuFragment.this, (List) obj);
            }
        });
    }

    public static /* synthetic */ List lambda$loadAudio$7(OptionalResult optionalResult) throws Exception {
        List list = (List) optionalResult.getIncludeNull();
        ArrayList<AudioResource> allLocalAudios = AudioFactory.getAllLocalAudios();
        if (list != null) {
            MovieUtils.checkResourceExist(list);
            allLocalAudios.addAll(2, list);
        }
        return allLocalAudios;
    }

    public /* synthetic */ void lambda$loadAudio$8(List list) throws Exception {
        this.mAudioAdapter.setList(list);
        this.mAudioAdapter.notifyDataSetChanged();
    }

    public final boolean checkChangeLongVideo() {
        return this.mMovieInfo.imageList.size() > 5 && this.mMovieInfo.isShortVideo;
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        String path;
        super.onActivityResult(i, i2, intent);
        if (i == 1002 && i2 == -1) {
            List<ImageEntity> imageFromClipData = MovieUtils.getImageFromClipData(this.mContext, intent);
            filterUnsupportFile(imageFromClipData);
            if (imageFromClipData.size() <= 0) {
                ToastUtils.makeText(this.mContext, R$string.unsupport_type);
                return;
            }
            this.mMovieInfo.imageList.addAll(imageFromClipData);
            if (checkChangeLongVideo()) {
                this.mMovieInfo.discardToLongVideo();
                this.mMovieEditorPlayMenuFragment.setShortRadioButtonCheck(false);
                ToastUtils.makeText(this.mContext, getString(R$string.movie_change_to_long_video, 5));
                this.mMovieManager.resetImage(this.mMovieInfo.imageList, false);
                this.mMovieManager.replay();
            } else {
                this.mMovieManager.addImage(imageFromClipData);
            }
            this.mEditAdapter.setList(this.mMovieInfo.imageList);
            this.mEditAdapter.notifyDataSetChanged();
            this.mEditAdapter.setSelectedItemPosition(-1);
            resetMovieShareData();
        } else if (i == 1000 && i2 == -1) {
            ClipData clipData = intent.getClipData();
            if (clipData == null) {
                path = ConvertFilepathUtil.getPath(getActivity(), intent.getData());
            } else {
                path = ConvertFilepathUtil.getPath(getActivity(), clipData.getItemAt(0).getUri());
            }
            if (!TextUtils.isEmpty(path)) {
                if (this.mMovieManager.isSupportVideo(path)) {
                    setAudio(path);
                } else {
                    ToastUtils.makeText(getActivity(), R$string.movie_customer_unsupport_audio_file);
                }
            }
            resetMovieShareData();
        } else if (i != 1001 || i2 != -1) {
        } else {
            List<ImageEntity> imageFromClipData2 = MovieUtils.getImageFromClipData(this.mContext, intent);
            filterUnsupportFile(imageFromClipData2);
            if (imageFromClipData2.size() <= 0) {
                ToastUtils.makeText(this.mContext, R$string.unsupport_type);
                return;
            }
            this.mMovieInfo.imageList.clear();
            this.mMovieInfo.imageList.addAll(imageFromClipData2);
            if (this.mMovieInfo.imageList.size() > 5) {
                if (this.mIsShortVideo) {
                    ToastUtils.makeText(this.mContext, String.format(getResources().getString(R$string.movie_change_to_long_video), 5));
                }
                this.mMovieInfo.discardToLongVideo();
                this.mMovieEditorPlayMenuFragment.setShortRadioButtonCheck(false);
                this.mMovieManager.resetImage(this.mMovieInfo.imageList, false);
            } else {
                this.mMovieInfo.discardToShortVideo();
                this.mMovieEditorPlayMenuFragment.setShortRadioButtonCheck(true);
                this.mMovieManager.resetImage(this.mMovieInfo.imageList, true);
            }
            refreshEditAdapter();
            this.mEditAdapter.setSelectedItemPosition(-1);
            resetMovieShareData();
            this.mMenuFragmentListener.updateStorySha1Data();
            this.mMovieManager.replay();
        }
    }

    public final void setAudio(String str) {
        AudioResource audioResource = new AudioResource(str);
        this.mMovieInfo.audio = audioResource.nameKey;
        this.mMovieManager.setAudio(audioResource);
        this.mAudioAdapter.setSelectedItemPosition(1);
        this.mLocalAudioPath = str;
    }

    public final void filterUnsupportFile(List<ImageEntity> list) {
        Iterator<ImageEntity> it = list.iterator();
        while (it.hasNext()) {
            if (!MovieConfig.isMimeTypeSupport(BaseFileMimeUtil.getMimeType(it.next().image))) {
                it.remove();
            }
        }
    }

    public void onDeleteClick() {
        if (this.mEditAdapter.getItemCount() - 1 <= 3) {
            ToastUtils.makeText(this.mContext, String.format(getResources().getString(R$string.movie_delete_disable), 3));
            return;
        }
        this.mDeleteAnimationView.playAnimation();
        this.mDeleteAnimationView.addAnimatorListener(this.mAnimatorListener);
        int selectedItemPosition = this.mEditAdapter.getSelectedItemPosition();
        if (selectedItemPosition < 0 || selectedItemPosition >= this.mMovieInfo.imageList.size() || !this.mMenuFragmentListener.getMovieManager().isReadyForSwitch()) {
            return;
        }
        this.mMovieInfo.imageList.remove(selectedItemPosition);
        this.mMenuFragmentListener.getMovieManager().removeImageAtIndex(selectedItemPosition);
        this.mEditAdapter.setSelectedItemPosition(-1);
        this.mEditAdapter.notifyItemRemoved(selectedItemPosition);
        resetMovieShareData();
        this.mDeleteClicked = true;
        this.mMenuFragmentListener.updateStorySha1Data();
    }

    @SuppressLint({"RestrictedApi"})
    public final void onTabSelect(int i) {
        this.mCurrentTabPos = i;
        boolean z = false;
        int i2 = 0;
        while (true) {
            RecyclerView[] recyclerViewArr = this.mRecyclerViews;
            if (i2 >= recyclerViewArr.length) {
                break;
            }
            if (recyclerViewArr[i2] != null) {
                recyclerViewArr[i2].setVisibility(i == i2 ? 0 : 4);
            }
            i2++;
        }
        this.mViewPager.setCurrentItem(i);
        this.mViewPager.setClipChildren(i != 2);
        ViewPager viewPager = this.mViewPager;
        if (i != 2) {
            z = true;
        }
        viewPager.setClipToPadding(z);
    }

    /* loaded from: classes2.dex */
    public class ControllerPagerAdapter extends PagerAdapter {
        public List<BaseAdapter> mAdapters;

        @Override // androidx.viewpager.widget.PagerAdapter
        public boolean isViewFromObject(View view, Object obj) {
            return obj == view;
        }

        public ControllerPagerAdapter() {
            MovieEditorMenuFragment.this = r3;
            ArrayList arrayList = new ArrayList(3);
            this.mAdapters = arrayList;
            arrayList.add(r3.mTemplateAdapter);
            this.mAdapters.add(r3.mAudioAdapter);
            this.mAdapters.add(r3.mEditAdapter);
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public int getCount() {
            return this.mAdapters.size();
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public Object instantiateItem(ViewGroup viewGroup, int i) {
            RecyclerView recyclerView = MovieEditorMenuFragment.this.mRecyclerViews[i];
            SimpleRecyclerView simpleRecyclerView = recyclerView;
            if (recyclerView == null) {
                SimpleRecyclerView simpleRecyclerView2 = new SimpleRecyclerView(viewGroup.getContext());
                simpleRecyclerView2.setEnableItemClickWhileSettling(true);
                CustomScrollerLinearLayoutManager customScrollerLinearLayoutManager = new CustomScrollerLinearLayoutManager(MovieEditorMenuFragment.this.mContext);
                customScrollerLinearLayoutManager.setSmoothScroller(new CenterSmoothScrollerController(MovieEditorMenuFragment.this.mContext));
                int i2 = 0;
                customScrollerLinearLayoutManager.setOrientation(0);
                simpleRecyclerView2.setLayoutManager(customScrollerLinearLayoutManager);
                int dimensionPixelSize = MovieEditorMenuFragment.this.getResources().getDimensionPixelSize(R$dimen.movie_list_interval);
                simpleRecyclerView2.addItemDecoration(new BlankDivider(dimensionPixelSize, dimensionPixelSize, dimensionPixelSize, 0, 0));
                if (i == 0) {
                    this.mAdapters.get(i).setItemSelectChangeListener(new MyTemplateItemSelectChangeListener());
                } else if (i == 1) {
                    this.mAdapters.get(i).setItemSelectChangeListener(new MyAudioItemSelectChangeListener());
                } else if (i == 2) {
                    this.mAdapters.get(i).setItemSelectChangeListener(new MyEditItemSelectChangeListener());
                    new SpringItemTouchHelper(MovieEditorMenuFragment.this.mEditAdapter.getCallBack()).attachToRecyclerView(simpleRecyclerView2);
                    simpleRecyclerView2.setItemAnimator(new DefaultItemAnimator());
                    simpleRecyclerView2.setAlwaysDisableSpring(true);
                }
                MovieEditorMenuFragment.this.mRecyclerViews[i] = simpleRecyclerView2;
                simpleRecyclerView2.setAdapter(this.mAdapters.get(i));
                if (MovieEditorMenuFragment.this.mCurrentTabPos != i) {
                    i2 = 4;
                }
                simpleRecyclerView2.setVisibility(i2);
                simpleRecyclerView = simpleRecyclerView2;
            }
            viewGroup.addView(simpleRecyclerView, new ViewGroup.LayoutParams(-1, -1));
            return simpleRecyclerView;
        }

        @Override // androidx.viewpager.widget.PagerAdapter
        public void destroyItem(ViewGroup viewGroup, int i, Object obj) {
            viewGroup.removeView(MovieEditorMenuFragment.this.mRecyclerViews[i]);
        }
    }

    /* loaded from: classes2.dex */
    public class MyTemplateItemSelectChangeListener implements BaseAdapter.ItemSelectChangeListener {
        public MyTemplateItemSelectChangeListener() {
            MovieEditorMenuFragment.this = r1;
        }

        @Override // com.miui.gallery.movie.ui.adapter.BaseAdapter.ItemSelectChangeListener
        public boolean onItemSelect(RecyclerView recyclerView, BaseAdapter.BaseHolder baseHolder, int i, boolean z) {
            TemplateResource mo1153getItemData;
            ((SimpleRecyclerView) recyclerView).setSpringEnabled(false);
            MovieEditorMenuFragment.this.mWaitPosition = i;
            MovieEditorMenuFragment.this.mWaitRecyclerView = recyclerView;
            if (z && (mo1153getItemData = MovieEditorMenuFragment.this.mTemplateAdapter.mo1153getItemData(i)) != null) {
                int downloadState = mo1153getItemData.getDownloadState();
                if (downloadState == 17 || downloadState == 0) {
                    MovieEditorMenuFragment.this.selectItem(recyclerView, i, mo1153getItemData);
                    return true;
                } else if (downloadState != 19 && downloadState != 20) {
                    return false;
                } else {
                    MovieEditorMenuFragment.this.downloadResource(mo1153getItemData, recyclerView, i);
                    return false;
                }
            }
            return false;
        }
    }

    public final void selectItem(RecyclerView recyclerView, int i, MovieResource movieResource) {
        if (recyclerView == this.mWaitRecyclerView && i == this.mWaitPosition && this.mMovieManager.isReadyForSwitch()) {
            if (movieResource instanceof TemplateResource) {
                this.mAudioAdapter.setSelectedItemPosition(0);
                this.mMovieInfo.template = movieResource.nameKey;
                this.mMovieManager.setTemplate((TemplateResource) movieResource);
                this.mTemplateAdapter.setSelectedItemPosition(i);
            } else if (movieResource instanceof AudioResource) {
                this.mMovieInfo.audio = movieResource.nameKey;
                this.mMovieManager.setAudio((AudioResource) movieResource);
                this.mAudioAdapter.setSelectedItemPosition(i);
            }
            recyclerView.smoothScrollToPosition(i);
            resetMovieShareData();
            this.mMenuFragmentListener.showLoadingView();
            MovieStatUtils.statItemChoose(movieResource);
        }
    }

    /* loaded from: classes2.dex */
    public class MyAudioItemSelectChangeListener implements BaseAdapter.ItemSelectChangeListener {
        public MyAudioItemSelectChangeListener() {
            MovieEditorMenuFragment.this = r1;
        }

        @Override // com.miui.gallery.movie.ui.adapter.BaseAdapter.ItemSelectChangeListener
        public boolean onItemSelect(RecyclerView recyclerView, BaseAdapter.BaseHolder baseHolder, int i, boolean z) {
            ((SimpleRecyclerView) recyclerView).setSpringEnabled(false);
            AudioResource mo1153getItemData = MovieEditorMenuFragment.this.mAudioAdapter.mo1153getItemData(i);
            if (mo1153getItemData != null) {
                if (mo1153getItemData.getResType() == 2) {
                    if (z && !TextUtils.isEmpty(MovieEditorMenuFragment.this.mLocalAudioPath)) {
                        MovieEditorMenuFragment movieEditorMenuFragment = MovieEditorMenuFragment.this;
                        movieEditorMenuFragment.setAudio(movieEditorMenuFragment.mLocalAudioPath);
                        return true;
                    }
                    Intent intent = new Intent();
                    intent.setType("audio/*");
                    intent.setAction("android.intent.action.GET_CONTENT");
                    intent.setPackage("com.miui.player");
                    try {
                        MovieEditorMenuFragment.this.startActivityForResult(intent, 1000);
                    } catch (ActivityNotFoundException unused) {
                        DefaultLogger.e("MovieEditorMenuFragment", "com.miui.player not found,try all picker");
                        try {
                            Intent intent2 = new Intent();
                            intent2.setType("audio/*");
                            intent2.setAction("android.intent.action.GET_CONTENT");
                            MovieEditorMenuFragment.this.startActivityForResult(intent2, 1000);
                        } catch (ActivityNotFoundException unused2) {
                            DefaultLogger.e("MovieEditorMenuFragment", "picker not found");
                        }
                    }
                    return false;
                }
                MovieEditorMenuFragment.this.mWaitPosition = i;
                MovieEditorMenuFragment.this.mWaitRecyclerView = recyclerView;
                if (z) {
                    int downloadState = mo1153getItemData.getDownloadState();
                    if (downloadState == 17 || downloadState == 0) {
                        MovieEditorMenuFragment.this.selectItem(recyclerView, i, mo1153getItemData);
                        return true;
                    } else if (downloadState != 19 && downloadState != 20) {
                        return false;
                    } else {
                        MovieEditorMenuFragment.this.downloadResource(mo1153getItemData, recyclerView, i);
                        return false;
                    }
                }
            }
            return false;
        }
    }

    /* loaded from: classes2.dex */
    public static class DownloadListener implements MovieDownloadListener {
        public MovieResource mData;
        public WeakReference<MovieEditorMenuFragment> mMovieEditorMenuFragment;
        public WeakReference<RecyclerView> mParent;
        public int mPosition;

        /* renamed from: $r8$lambda$FPcYAYayjWBsnURE-_F2cmHJdJM */
        public static /* synthetic */ void m1159$r8$lambda$FPcYAYayjWBsnURE_F2cmHJdJM(DownloadListener downloadListener, MovieEditorMenuFragment movieEditorMenuFragment, RecyclerView recyclerView) {
            downloadListener.lambda$onCompleted$0(movieEditorMenuFragment, recyclerView);
        }

        public DownloadListener(MovieEditorMenuFragment movieEditorMenuFragment, MovieResource movieResource, RecyclerView recyclerView, int i) {
            this.mMovieEditorMenuFragment = new WeakReference<>(movieEditorMenuFragment);
            this.mParent = new WeakReference<>(recyclerView);
            this.mData = movieResource;
            this.mPosition = i;
        }

        @Override // com.miui.gallery.movie.ui.listener.MovieDownloadListener
        public void onCompleted(boolean z) {
            final MovieEditorMenuFragment movieEditorMenuFragment = this.mMovieEditorMenuFragment.get();
            final RecyclerView recyclerView = this.mParent.get();
            if (movieEditorMenuFragment == null || recyclerView == null) {
                return;
            }
            MovieStatUtils.statDownloadResult(this.mData, z);
            this.mData.downloadState = z ? 0 : 20;
            if (!z) {
                ToastUtils.makeText(movieEditorMenuFragment.mContext, R$string.movie_download_failed);
            }
            movieEditorMenuFragment.notifyResourceAdapter(this.mData, this.mPosition);
            AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() { // from class: com.miui.gallery.movie.ui.fragment.MovieEditorMenuFragment$DownloadListener$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    MovieEditorMenuFragment.DownloadListener.m1159$r8$lambda$FPcYAYayjWBsnURE_F2cmHJdJM(MovieEditorMenuFragment.DownloadListener.this, movieEditorMenuFragment, recyclerView);
                }
            });
        }

        public /* synthetic */ void lambda$onCompleted$0(MovieEditorMenuFragment movieEditorMenuFragment, RecyclerView recyclerView) {
            movieEditorMenuFragment.selectItem(recyclerView, this.mPosition, this.mData);
        }

        @Override // com.miui.gallery.movie.ui.listener.MovieDownloadListener
        public void onStart() {
            MovieEditorMenuFragment movieEditorMenuFragment = this.mMovieEditorMenuFragment.get();
            if (movieEditorMenuFragment == null) {
                return;
            }
            MovieStatUtils.statDownload(this.mData);
            MovieResource movieResource = this.mData;
            movieResource.downloadState = 18;
            movieEditorMenuFragment.notifyResourceAdapter(movieResource, this.mPosition);
        }
    }

    public final void downloadResource(MovieResource movieResource, RecyclerView recyclerView, int i) {
        this.mDownloadListener = new DownloadListener(this, movieResource, recyclerView, i);
        MovieDownloadManager.getInstance().downloadResourceWithCheck(getActivity(), movieResource, this.mDownloadListener);
    }

    public final void notifyResourceAdapter(final MovieResource movieResource, final int i) {
        AndroidSchedulers.mainThread().createWorker().schedule(new Runnable() { // from class: com.miui.gallery.movie.ui.fragment.MovieEditorMenuFragment$$ExternalSyntheticLambda9
            @Override // java.lang.Runnable
            public final void run() {
                MovieEditorMenuFragment.m1155$r8$lambda$AnT6HjYc670ro5qMsdCNpLZdhw(MovieEditorMenuFragment.this, movieResource, i);
            }
        });
    }

    public /* synthetic */ void lambda$notifyResourceAdapter$9(MovieResource movieResource, int i) {
        if (movieResource instanceof TemplateResource) {
            this.mTemplateAdapter.notifyItemChanged(i);
        } else {
            this.mAudioAdapter.notifyItemChanged(i);
        }
    }

    /* loaded from: classes2.dex */
    public class MyEditItemSelectChangeListener implements BaseAdapter.ItemSelectChangeListener {
        public MyEditItemSelectChangeListener() {
            MovieEditorMenuFragment.this = r1;
        }

        @Override // com.miui.gallery.movie.ui.adapter.BaseAdapter.ItemSelectChangeListener
        public boolean onItemSelect(RecyclerView recyclerView, BaseAdapter.BaseHolder baseHolder, int i, boolean z) {
            if (MovieEditorMenuFragment.this.mEditAdapter.isAddItem(i)) {
                if (MovieEditorMenuFragment.this.mMovieInfo.isFromStory) {
                    if (MovieEditorMenuFragment.this.mDeleteClicked) {
                        MovieEditorMenuFragment.this.mMenuFragmentListener.updateStorySha1Data();
                        MovieEditorMenuFragment.this.mDeleteClicked = false;
                    }
                    MovieDependsModule movieDependsModule = (MovieDependsModule) ModuleRegistry.getModule(MovieDependsModule.class);
                    if (movieDependsModule != null) {
                        Intent intent = new Intent(MovieEditorMenuFragment.this.getActivity(), movieDependsModule.getStoryPickClass());
                        intent.setType("image/*");
                        intent.putExtra("card_id", MovieEditorMenuFragment.this.mStoryMovieCardId);
                        intent.putStringArrayListExtra("pick_sha1", MovieEditorMenuFragment.this.mMenuFragmentListener.getStoryMovieSha1());
                        intent.putExtra("pick-upper-bound", 20);
                        intent.putExtra("pick-lower-bound", 3);
                        intent.putExtra("extra_filter_media_type", new String[]{"image/x-adobe-dng", "video/*"});
                        MovieEditorMenuFragment.this.startActivityForResult(intent, 1001);
                    }
                } else if (MovieEditorMenuFragment.this.mEditAdapter.getListSize() >= 20) {
                    ToastUtils.makeText(MovieEditorMenuFragment.this.getActivity(), String.format(MovieEditorMenuFragment.this.getResources().getString(R$string.movie_add_disable), 20));
                } else {
                    Intent intent2 = new Intent("android.intent.action.GET_CONTENT");
                    intent2.setType("image/*");
                    intent2.putExtra("pick-upper-bound", 20 - MovieEditorMenuFragment.this.mEditAdapter.getList().size());
                    intent2.putExtra("extra_filter_media_type", new String[]{"image/x-adobe-dng"});
                    intent2.setPackage("com.miui.gallery");
                    MovieEditorMenuFragment.this.startActivityForResult(intent2, 1002);
                }
            } else if (z) {
                MovieEditorMenuFragment.this.mMenuFragmentListener.seekToIndex(i);
                MovieEditorMenuFragment.this.mMenuFragmentListener.setDeleteVisible(true);
                MovieEditorMenuFragment.this.mDeleteAnimationView.setAlpha(1.0f);
                MovieEditorMenuFragment.this.mDeleteArea.setClickable(true);
                return true;
            }
            return false;
        }
    }

    public final void showDeleteIcon(boolean z) {
        if (z) {
            this.mDeleteArea.setVisibility(0);
        } else {
            this.mDeleteArea.setVisibility(8);
        }
        if (this.mMovieManager.getState() == 1) {
            this.mEditAdapter.setSelectedItemPosition(-1);
        }
        if (this.mEditAdapter.getSelectedItemPosition() == -1) {
            this.mDeleteAnimationView.setAlpha(0.3f);
            this.mDeleteArea.setClickable(false);
        }
    }

    public final void resetMovieShareData() {
        MenuFragmentListener menuFragmentListener = this.mMenuFragmentListener;
        if (menuFragmentListener != null) {
            menuFragmentListener.resetShareData();
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        TemplateResourceRequest templateResourceRequest = this.mTemplateResourceRequest;
        if (templateResourceRequest != null) {
            templateResourceRequest.cancel();
        }
        AudioResourceRequest audioResourceRequest = this.mAudioResourceRequest;
        if (audioResourceRequest != null) {
            audioResourceRequest.cancel();
        }
        MovieDownloadManager.getInstance().cancelAll();
        this.mMovieManager = null;
    }

    @Override // androidx.fragment.app.Fragment
    public Animator onCreateAnimator(int i, boolean z, int i2) {
        ObjectAnimator objectAnimator = new ObjectAnimator();
        int dimensionPixelSize = getActivity().getResources().getDimensionPixelSize(com.miui.gallery.editor.R$dimen.photo_movie_edit_panel_height);
        if (z) {
            objectAnimator.setValues(PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, dimensionPixelSize, 0.0f));
            objectAnimator.setInterpolator(new QuarticEaseOutInterpolator());
            objectAnimator.setStartDelay(getResources().getInteger(R$integer.movie_editor_appear_delay));
            objectAnimator.setDuration(getResources().getInteger(R$integer.movie_editor_appear_duration));
            getView().setAlpha(0.0f);
            objectAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.movie.ui.fragment.MovieEditorMenuFragment.5
                {
                    MovieEditorMenuFragment.this = this;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    MovieEditorMenuFragment.this.getView().setAlpha(1.0f);
                }
            });
        } else {
            objectAnimator.setValues(PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0.0f, dimensionPixelSize));
            objectAnimator.setInterpolator(new QuarticEaseOutInterpolator());
            objectAnimator.setDuration(getResources().getInteger(R$integer.movie_editor_disappear_duration));
        }
        return objectAnimator;
    }
}
