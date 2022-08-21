package com.miui.gallery.movie.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.util.Property;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.android.internal.WindowCompat;
import com.miui.gallery.app.StrategyContext;
import com.miui.gallery.app.activity.GalleryActivity;
import com.miui.gallery.app.screenChange.IScreenChange;
import com.miui.gallery.editor.R$dimen;
import com.miui.gallery.imodule.loader.ModuleRegistry;
import com.miui.gallery.imodule.modules.MovieDependsModule;
import com.miui.gallery.listener.SingleClickListener;
import com.miui.gallery.movie.MovieConfig;
import com.miui.gallery.movie.R$id;
import com.miui.gallery.movie.R$integer;
import com.miui.gallery.movie.R$layout;
import com.miui.gallery.movie.R$string;
import com.miui.gallery.movie.R$style;
import com.miui.gallery.movie.core.MovieManager;
import com.miui.gallery.movie.entity.ImageEntity;
import com.miui.gallery.movie.entity.MovieInfo;
import com.miui.gallery.movie.entity.MovieShareData;
import com.miui.gallery.movie.ui.factory.MovieFactory;
import com.miui.gallery.movie.ui.fragment.MovieEditorMenuFragment;
import com.miui.gallery.movie.ui.fragment.MovieEditorTopMenuFragment;
import com.miui.gallery.movie.ui.fragment.MoviePreviewMenuFragment;
import com.miui.gallery.movie.ui.fragment.MovieSavingFragment;
import com.miui.gallery.movie.ui.listener.MenuFragmentListener;
import com.miui.gallery.movie.ui.view.MovieControllerView;
import com.miui.gallery.movie.utils.AudioFocusHelper;
import com.miui.gallery.movie.utils.MovieStorage;
import com.miui.gallery.movie.utils.MovieUtils;
import com.miui.gallery.movie.utils.stat.MovieStatUtils;
import com.miui.gallery.permission.core.Permission;
import com.miui.gallery.permission.core.PermissionCheckCallback;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.GalleryOpenProviderUtils;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.SystemUiUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.mediaeditor.utils.FilePermissionUtils;
import com.miui.mediaeditor.utils.MediaEditorUtils;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import miuix.appcompat.app.ActionBar;
import miuix.view.animation.CubicEaseInInterpolator;
import miuix.view.animation.CubicEaseInOutInterpolator;
import miuix.view.animation.CubicEaseOutInterpolator;
import miuix.view.animation.QuarticEaseInOutInterpolator;
import miuix.view.animation.QuarticEaseOutInterpolator;

/* loaded from: classes2.dex */
public class MovieActivity extends GalleryActivity implements MenuFragmentListener, View.OnClickListener, PermissionCheckCallback {
    public ActionBar mActionBar;
    public FragmentActivity mActivity;
    public AudioFocusHelper mAudioFocusHelper;
    public MovieEditorMenuFragment mEditorMenuFragment;
    public View mEditorMenuView;
    public RelativeLayout.LayoutParams mEditorMovieLps;
    public boolean mIsEditorPreview;
    public boolean mIsStartActivityForResult;
    public MovieEditorTopMenuFragment mMovieEditorPlayMenuFragment;
    public MovieInfo mMovieInfo;
    public MovieManager mMovieManager;
    public MovieSavingFragment mMovieSavingFragment;
    public MovieShareData mMovieShareData;
    public View mMovieTopMenuView;
    public MovieControllerView mMovieView;
    public MoviePreviewMenuFragment mPreviewMenuFragment;
    public View mPreviewMenuView;
    public RelativeLayout.LayoutParams mPreviewMovieLps;
    public ViewGroup mRootView;
    public long mStoryMovieCardId;
    public ArrayList<String> mStorySha1List;
    public int mShowMode = -1;
    public boolean mIsDestroyed = false;
    public MovieDependsModule.Callback mMovieDependsModuleCallback = new MovieDependsModule.Callback() { // from class: com.miui.gallery.movie.ui.activity.MovieActivity.1
        {
            MovieActivity.this = this;
        }

        @Override // com.miui.gallery.imodule.modules.MovieDependsModule.Callback
        public void onDialogConfirm() {
            MovieActivity.this.finish();
        }

        @Override // com.miui.gallery.imodule.modules.MovieDependsModule.Callback
        public void onDialogCancel() {
            MovieActivity.this.finish();
        }
    };
    public SingleClickListener mSingleClickListener = new SingleClickListener() { // from class: com.miui.gallery.movie.ui.activity.MovieActivity.2
        {
            MovieActivity.this = this;
        }

        @Override // com.miui.gallery.listener.SingleClickListener
        public void onSingleClick(View view) {
            int id = view.getId();
            if (id == R$id.movie_title) {
                MovieActivity.this.finish();
            } else if (id != R$id.movie_share) {
            } else {
                MovieActivity.this.mMovieManager.pause();
                if (MovieActivity.this.mMovieShareData == null) {
                    MovieActivity.this.mMovieShareData = new MovieShareData();
                    MovieActivity.this.mMovieShareData.setShortVideo(MovieActivity.this.mMovieInfo.isShortVideo);
                }
                String videoPath = MovieActivity.this.mMovieShareData.getVideoPath();
                if (TextUtils.isEmpty(videoPath)) {
                    if (!FilePermissionUtils.checkFileCreatePermission(MovieActivity.this.mo546getActivity(), MovieStorage.getOutputMediaFilePath())) {
                        return;
                    }
                    if (MovieActivity.this.mMovieSavingFragment == null) {
                        MovieActivity.this.mMovieSavingFragment = new MovieSavingFragment();
                    }
                    MovieActivity.this.mMovieSavingFragment.showAndShare(MovieActivity.this.mActivity, MovieActivity.this.mMovieManager, MovieActivity.this.mMovieInfo);
                    return;
                }
                MovieActivity.this.handleShareEvent(videoPath);
            }
        }
    };
    public ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.miui.gallery.movie.ui.activity.MovieActivity.3
        {
            MovieActivity.this = this;
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            MovieActivity.this.mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            MovieActivity.this.initMovieViewLayout();
        }
    };

    public static /* synthetic */ void $r8$lambda$4nMG4oU0IEIZ303r4PPVpyGQADQ(MovieActivity movieActivity, View view) {
        movieActivity.lambda$initView$3(view);
    }

    public static /* synthetic */ void $r8$lambda$C05I9SRfDbUOGyex_ZPQGENZyuU(MovieActivity movieActivity) {
        movieActivity.lambda$changeEditor$5();
    }

    public static /* synthetic */ void $r8$lambda$TGBlACoazj4reVIDdjUTNox1nmU(MovieActivity movieActivity) {
        movieActivity.lambda$initView$2();
    }

    public static /* synthetic */ void $r8$lambda$cB14B7J_ONDMK0rpwjNR0gErK6A(MovieActivity movieActivity, View view) {
        movieActivity.lambda$initView$4(view);
    }

    public static /* synthetic */ void $r8$lambda$gAxBXqsWsx80SXBnPcJkfad5biw(MovieActivity movieActivity, boolean z, boolean z2, String str) {
        movieActivity.lambda$export$6(z, z2, str);
    }

    public static /* synthetic */ void $r8$lambda$hkafNTYEpoTroJWtOlZty7VjEuU(MovieActivity movieActivity, Configuration configuration) {
        movieActivity.lambda$onCreate$0(configuration);
    }

    /* renamed from: $r8$lambda$jX1wPrZINr2pAF-lBaZOi0TLEZM */
    public static /* synthetic */ void m1150$r8$lambda$jX1wPrZINr2pAFlBaZOi0TLEZM(MovieActivity movieActivity, Configuration configuration) {
        movieActivity.lambda$onCreate$1(configuration);
    }

    @Override // com.miui.gallery.permission.core.PermissionCheckCallback
    public void onPermissionsChecked(Permission[] permissionArr, int[] iArr, boolean[] zArr) {
    }

    @Override // com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    @SuppressLint({"SourceLockedOrientationActivity"})
    public void onCreate(Bundle bundle) {
        MovieDependsModule movieDependsModule = (MovieDependsModule) ModuleRegistry.getModule(MovieDependsModule.class);
        boolean z = false;
        if (!movieDependsModule.installIfNotExist(this, this.mMovieDependsModuleCallback, false)) {
            return;
        }
        if (MediaEditorUtils.isMediaEditorAvailable() && movieDependsModule.isPhotoMovieUseMiSDK() && movieDependsModule.isDeviceSupportPhotoMovie()) {
            if (movieDependsModule.isPhotoMovieAvailable()) {
                super.onCreate(bundle);
                Intent intent = getIntent();
                intent.setComponent(new ComponentName("com.miui.mediaeditor", "com.miui.gallery.movie.ui.activity.MovieActivity"));
                intent.setPackage("com.miui.mediaeditor");
                boolean booleanExtra = getIntent().getBooleanExtra("is_start_activity_for_result", false);
                this.mIsStartActivityForResult = booleanExtra;
                if (booleanExtra) {
                    startActivityForResult(intent, 100);
                    return;
                }
                startActivity(intent);
                finish();
                return;
            }
            Intent intent2 = new Intent();
            intent2.putExtra("loadType", "photoMovie");
            intent2.setComponent(new ComponentName("com.miui.mediaeditor", "com.miui.mediaeditor.activity.DownloadLibraryActivity"));
            startActivity(intent2);
            finish();
        }
        setTheme(R$style.MovieTheme);
        if (bundle != null) {
            this.mMovieInfo = (MovieInfo) bundle.getParcelable("bundle_movie_info");
            this.mShowMode = bundle.getInt("bundle_show_mode");
        }
        this.mActivity = this;
        this.mMovieManager = MovieFactory.createMovieManager(this);
        super.onCreate(bundle);
        SystemUiUtil.setRequestedOrientation(1, this);
        if (!parseIntent()) {
            return;
        }
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= 24 && isInMultiWindowMode()) {
            z = true;
        }
        SystemUiUtil.setLayoutFullScreen(decorView, true, z);
        WindowCompat.setCutoutModeShortEdges(getWindow());
        initStoryAlbumData();
        setContentView(R$layout.movie_activity);
        configureActionBar();
        initView();
        initMode();
        this.mAudioFocusHelper = new AudioFocusHelper(this);
        addScreenChangeListener(new IScreenChange.OnScreenLayoutSizeChangeListener() { // from class: com.miui.gallery.movie.ui.activity.MovieActivity$$ExternalSyntheticLambda3
            @Override // com.miui.gallery.app.screenChange.IScreenChange.OnScreenLayoutSizeChangeListener
            public final void onScreenLayoutSizeChange(Configuration configuration) {
                MovieActivity.$r8$lambda$hkafNTYEpoTroJWtOlZty7VjEuU(MovieActivity.this, configuration);
            }
        });
        addScreenChangeListener(new IScreenChange.OnOrientationChangeListener() { // from class: com.miui.gallery.movie.ui.activity.MovieActivity$$ExternalSyntheticLambda2
            @Override // com.miui.gallery.app.screenChange.IScreenChange.OnOrientationChangeListener
            public final void onOrientationChange(Configuration configuration) {
                MovieActivity.m1150$r8$lambda$jX1wPrZINr2pAFlBaZOi0TLEZM(MovieActivity.this, configuration);
            }
        });
        requestDisableStrategy(StrategyContext.DisableStrategyType.NAVIGATION_BAR);
    }

    public /* synthetic */ void lambda$onCreate$0(Configuration configuration) {
        initMovieViewLayout();
    }

    public /* synthetic */ void lambda$onCreate$1(Configuration configuration) {
        initMovieViewLayout();
    }

    @Override // com.miui.gallery.app.activity.MiuiActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 100) {
            setResult(i2);
            finish();
        }
    }

    @Override // com.miui.gallery.app.activity.GalleryActivity, miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putParcelable("bundle_movie_info", this.mMovieInfo);
        bundle.putInt("bundle_show_mode", this.mShowMode);
    }

    public final void configureActionBar() {
        ActionBar appCompatActionBar = getAppCompatActionBar();
        this.mActionBar = appCompatActionBar;
        appCompatActionBar.setDisplayShowCustomEnabled(true);
        this.mActionBar.setDisplayShowHomeEnabled(false);
        this.mActionBar.setDisplayShowTitleEnabled(false);
        this.mActionBar.setHomeButtonEnabled(true);
        this.mActionBar.setCustomView(R$layout.movie_fragment_top);
        View customView = this.mActionBar.getCustomView();
        ImageView imageView = (ImageView) customView.findViewById(R$id.movie_share);
        FolmeUtil.setCustomTouchAnim(imageView, new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build(), null, null, true);
        ((TextView) customView.findViewById(R$id.movie_title)).setOnClickListener(this);
        imageView.setOnClickListener(this.mSingleClickListener);
        this.mActionBar.hide();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R$id.movie_title) {
            finish();
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mIsStartActivityForResult) {
            return;
        }
        initMovieViewLayout();
        setSystemBarVisible(this.mShowMode == 2);
    }

    @Override // com.miui.gallery.app.activity.MiuiActivity, android.app.Activity
    public void onMultiWindowModeChanged(boolean z, Configuration configuration) {
        super.onMultiWindowModeChanged(z, configuration);
        DefaultLogger.d("MovieActivity_", "onMultiWindowModeChanged " + z);
        if (!z) {
            setSystemBarVisible(this.mShowMode == 2);
            getWindow().addFlags(1024);
        }
        this.mRootView.getViewTreeObserver().addOnGlobalLayoutListener(this.mGlobalLayoutListener);
    }

    @Override // com.miui.gallery.app.activity.GalleryActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        MovieControllerView movieControllerView = this.mMovieView;
        if (movieControllerView != null) {
            movieControllerView.onResume();
        }
        AudioFocusHelper audioFocusHelper = this.mAudioFocusHelper;
        if (audioFocusHelper != null) {
            audioFocusHelper.requestAudioFocus();
        }
    }

    public final void doDestroy() {
        if (this.mIsDestroyed) {
            return;
        }
        this.mMovieShareData = null;
        MovieManager movieManager = this.mMovieManager;
        if (movieManager != null) {
            movieManager.destroy();
        }
        MovieControllerView movieControllerView = this.mMovieView;
        if (movieControllerView != null) {
            movieControllerView.destroy();
        }
        this.mIsDestroyed = true;
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        AudioFocusHelper audioFocusHelper = this.mAudioFocusHelper;
        if (audioFocusHelper != null) {
            audioFocusHelper.abandonAudioFocus();
        }
        MovieControllerView movieControllerView = this.mMovieView;
        if (movieControllerView != null) {
            movieControllerView.onPause();
        }
        if (isFinishing()) {
            doDestroy();
        }
    }

    @Override // com.miui.gallery.app.activity.MiuiActivity, android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (z) {
            setSystemBarVisible(this.mShowMode == 2);
        }
    }

    public final void initView() {
        MovieControllerView movieControllerView = (MovieControllerView) findViewById(R$id.movie_controller_view);
        this.mMovieView = movieControllerView;
        movieControllerView.init(this.mMovieManager);
        this.mMovieView.post(new Runnable() { // from class: com.miui.gallery.movie.ui.activity.MovieActivity$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public final void run() {
                MovieActivity.$r8$lambda$TGBlACoazj4reVIDdjUTNox1nmU(MovieActivity.this);
            }
        });
        this.mMovieManager = this.mMovieView.getMovieManager();
        this.mPreviewMenuView = findViewById(R$id.preview_panel);
        this.mEditorMenuView = findViewById(R$id.editor_panel);
        this.mMovieTopMenuView = findViewById(R$id.nav_area);
        this.mRootView = (ViewGroup) findViewById(R$id.movie_root);
        this.mMovieView.setProgressChangeListener(new MovieControllerView.ProgressChangeListener() { // from class: com.miui.gallery.movie.ui.activity.MovieActivity.4
            {
                MovieActivity.this = this;
            }

            @Override // com.miui.gallery.movie.ui.view.MovieControllerView.ProgressChangeListener
            public void onChanged(int i, int i2) {
                if (MovieActivity.this.mPreviewMenuFragment != null) {
                    MovieActivity.this.mPreviewMenuFragment.onProgressChange(i, i2);
                }
                if (MovieActivity.this.mEditorMenuFragment != null) {
                    MovieActivity.this.mEditorMenuFragment.setTime(i, i2);
                }
            }

            @Override // com.miui.gallery.movie.ui.view.MovieControllerView.ProgressChangeListener
            public void onStateChanged(int i) {
                if (MovieActivity.this.mPreviewMenuFragment != null) {
                    MovieActivity.this.mPreviewMenuFragment.onStateChanged(i);
                }
                if (MovieActivity.this.mEditorMenuFragment != null) {
                    MovieActivity.this.mEditorMenuFragment.changePlayButton();
                }
            }

            @Override // com.miui.gallery.movie.ui.view.MovieControllerView.ProgressChangeListener
            public void onPlaybackEOF() {
                if (MovieActivity.this.mPreviewMenuFragment != null) {
                    MovieActivity.this.mPreviewMenuFragment.onPlaybackEOF();
                }
            }
        });
        this.mMovieView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.movie.ui.activity.MovieActivity$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MovieActivity.$r8$lambda$4nMG4oU0IEIZ303r4PPVpyGQADQ(MovieActivity.this, view);
            }
        });
        this.mMovieView.setPreviewBtnClickListener(new SingleClickListener() { // from class: com.miui.gallery.movie.ui.activity.MovieActivity.5
            {
                MovieActivity.this = this;
            }

            @Override // com.miui.gallery.listener.SingleClickListener
            public void onSingleClick(View view) {
                MovieActivity.this.switchBetweenEditAndFullScreen();
            }
        });
        this.mRootView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.movie.ui.activity.MovieActivity$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                MovieActivity.$r8$lambda$cB14B7J_ONDMK0rpwjNR0gErK6A(MovieActivity.this, view);
            }
        });
    }

    public /* synthetic */ void lambda$initView$2() {
        this.mMovieView.setMovieInfo(this.mMovieInfo);
        MoviePreviewMenuFragment moviePreviewMenuFragment = this.mPreviewMenuFragment;
        if (moviePreviewMenuFragment != null) {
            moviePreviewMenuFragment.updateAfterSetInfo();
        }
    }

    public /* synthetic */ void lambda$initView$3(View view) {
        int i = this.mShowMode;
        if (i == 2) {
            this.mShowMode = 1;
            changeFullScreen(true);
        } else if (i == 1) {
            this.mShowMode = 2;
            changeFullScreen(false);
        } else if (this.mIsEditorPreview) {
            switchBetweenEditAndFullScreen();
        } else {
            this.mMovieManager.pauseOrResume();
        }
    }

    public /* synthetic */ void lambda$initView$4(View view) {
        if (this.mShowMode == 1) {
            changeFullScreen(false);
        }
    }

    public final void initMovieViewLayout() {
        int curDisplayFullScreenHeight;
        DefaultLogger.d("MovieActivity_", "initMovieViewLayout");
        DefaultLogger.d("MovieActivity_", getResources().getConfiguration().toString());
        int curDisplayWidth = ScreenUtils.getCurDisplayWidth(this);
        if (isInMultiWindowMode()) {
            curDisplayFullScreenHeight = ScreenUtils.getCurDisplayHeight(this);
        } else {
            curDisplayFullScreenHeight = ScreenUtils.getCurDisplayFullScreenHeight(this);
        }
        if (curDisplayWidth > curDisplayFullScreenHeight) {
            curDisplayWidth = (int) (curDisplayFullScreenHeight * MovieConfig.getHeightToWidth());
        }
        float dimension = curDisplayFullScreenHeight - getResources().getDimension(R$dimen.photo_movie_edit_panel_height);
        Resources resources = getResources();
        int i = com.miui.gallery.movie.R$dimen.movie_view_margin_top;
        int dimension2 = (int) ((dimension - resources.getDimension(i)) - getResources().getDimension(com.miui.gallery.movie.R$dimen.movie_view_margin_bottom));
        int heightToWidth = (int) (dimension2 * MovieConfig.getHeightToWidth());
        if (this.mMovieView == null) {
            return;
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(this.mMovieView.getLayoutParams());
        this.mEditorMovieLps = layoutParams;
        layoutParams.width = heightToWidth;
        layoutParams.height = dimension2;
        layoutParams.topMargin = (int) getResources().getDimension(i);
        this.mEditorMovieLps.addRule(14);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(this.mMovieView.getLayoutParams());
        this.mPreviewMovieLps = layoutParams2;
        layoutParams2.width = curDisplayWidth;
        layoutParams2.height = curDisplayFullScreenHeight;
        layoutParams2.topMargin = 0;
        layoutParams2.addRule(14);
        if (this.mShowMode == 3 && !this.mIsEditorPreview) {
            RelativeLayout.LayoutParams layoutParams3 = (RelativeLayout.LayoutParams) this.mMovieView.getLayoutParams();
            RelativeLayout.LayoutParams layoutParams4 = this.mEditorMovieLps;
            layoutParams3.width = layoutParams4.width;
            layoutParams3.height = layoutParams4.height;
            layoutParams3.topMargin = layoutParams4.topMargin;
            this.mMovieView.setLayoutParams(layoutParams3);
            return;
        }
        RelativeLayout.LayoutParams layoutParams5 = (RelativeLayout.LayoutParams) this.mMovieView.getLayoutParams();
        RelativeLayout.LayoutParams layoutParams6 = this.mPreviewMovieLps;
        layoutParams5.width = layoutParams6.width;
        layoutParams5.height = layoutParams6.height;
        layoutParams5.topMargin = layoutParams6.topMargin;
        this.mMovieView.setLayoutParams(layoutParams5);
    }

    public MovieEditorTopMenuFragment getMovieEditorPlayMenuFragment() {
        return this.mMovieEditorPlayMenuFragment;
    }

    public final void initMode() {
        if (this.mShowMode == -1) {
            this.mShowMode = this.mMovieInfo.isFromStory ? 1 : 3;
        }
        if (this.mShowMode == 3) {
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            int i = R$id.editor_panel;
            this.mEditorMenuFragment = (MovieEditorMenuFragment) supportFragmentManager.findFragmentById(i);
            FragmentManager supportFragmentManager2 = getSupportFragmentManager();
            int i2 = R$id.nav_area;
            this.mMovieEditorPlayMenuFragment = (MovieEditorTopMenuFragment) supportFragmentManager2.findFragmentById(i2);
            if (this.mEditorMenuFragment == null) {
                this.mEditorMenuFragment = new MovieEditorMenuFragment();
                getSupportFragmentManager().beginTransaction().add(i, this.mEditorMenuFragment).commit();
            }
            if (this.mMovieEditorPlayMenuFragment == null) {
                this.mMovieEditorPlayMenuFragment = new MovieEditorTopMenuFragment();
                getSupportFragmentManager().beginTransaction().add(i2, this.mMovieEditorPlayMenuFragment).commit();
            }
        } else {
            FragmentManager supportFragmentManager3 = getSupportFragmentManager();
            int i3 = R$id.preview_panel;
            MoviePreviewMenuFragment moviePreviewMenuFragment = (MoviePreviewMenuFragment) supportFragmentManager3.findFragmentById(i3);
            if (moviePreviewMenuFragment == null) {
                moviePreviewMenuFragment = new MoviePreviewMenuFragment();
                getSupportFragmentManager().beginTransaction().add(i3, moviePreviewMenuFragment).commitAllowingStateLoss();
                getSupportFragmentManager().executePendingTransactions();
                if (this.mShowMode == 1) {
                    this.mPreviewMenuView.setVisibility(4);
                }
            }
            this.mPreviewMenuFragment = moviePreviewMenuFragment;
        }
        setForMode();
    }

    public final void setSystemBarVisible(boolean z) {
        boolean z2 = true;
        if (z) {
            View decorView = getWindow().getDecorView();
            if (Build.VERSION.SDK_INT < 24 || !isInMultiWindowMode()) {
                z2 = false;
            }
            SystemUiUtil.showSystemBars(decorView, z2);
            this.mActionBar.show();
            return;
        }
        View decorView2 = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT < 24 || !isInMultiWindowMode()) {
            z2 = false;
        }
        SystemUiUtil.hideSystemBars(decorView2, z2);
        this.mActionBar.hide();
    }

    public final void setForMode() {
        if (this.mShowMode == 3) {
            this.mMovieView.setPlayProgressVisible(true);
            this.mMovieView.setTouchAvailable(true);
            this.mMovieView.showPreviewBtn(true);
            return;
        }
        this.mMovieView.setTouchAvailable(false);
        this.mMovieView.setPlayProgressVisible(false);
        this.mMovieView.showPreviewBtn(false);
    }

    @Override // androidx.fragment.app.FragmentActivity
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    public final boolean parseIntent() {
        List<ImageEntity> imageFromClipData = MovieUtils.getImageFromClipData(this, getIntent());
        if (imageFromClipData == null || imageFromClipData.size() < 3) {
            ToastUtils.makeText(this.mActivity, String.format(getResources().getString(R$string.movie_delete_disable), 3));
            finish();
            return false;
        } else if (containUnsupportFile(imageFromClipData)) {
            ToastUtils.makeText(this.mActivity, R$string.unsupport_type);
            finish();
            return false;
        } else {
            if (this.mMovieInfo == null) {
                MovieInfo movieInfo = new MovieInfo(imageFromClipData);
                this.mMovieInfo = movieInfo;
                movieInfo.isFromStory = getIntent().getBooleanExtra("movie_extra_preview_mode", false);
                this.mMovieInfo.title = getIntent().getStringExtra("card_title");
                this.mMovieInfo.subTitle = getIntent().getStringExtra("card_sub_title");
                this.mMovieInfo.template = MovieFactory.getTemplateNameById(getIntent().getIntExtra("movie_extra_template", 0));
                MovieStatUtils.statEnter(imageFromClipData.size(), this.mMovieInfo.isFromStory);
            }
            return true;
        }
    }

    public final void initStoryAlbumData() {
        Intent intent = getIntent();
        if (!this.mMovieInfo.isFromStory || intent == null) {
            return;
        }
        this.mStoryMovieCardId = intent.getLongExtra("card_id", 0L);
        if (this.mStorySha1List == null) {
            this.mStorySha1List = new ArrayList<>();
        }
        ClipData clipData = intent.getClipData();
        for (int i = 0; i < clipData.getItemCount(); i++) {
            String charSequence = clipData.getItemAt(i).getText().toString();
            if (!this.mStorySha1List.contains(charSequence) && isMovieInfoContainsStorySha1(charSequence)) {
                this.mStorySha1List.add(charSequence);
            }
        }
    }

    public final boolean isMovieInfoContainsStorySha1(String str) {
        MovieInfo movieInfo = this.mMovieInfo;
        if (movieInfo == null) {
            return false;
        }
        for (ImageEntity imageEntity : movieInfo.imageList) {
            if (imageEntity.sha1.equals(str)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.miui.gallery.movie.ui.listener.MenuFragmentListener
    public MovieInfo getMovieInfo() {
        return this.mMovieInfo;
    }

    @Override // com.miui.gallery.movie.ui.listener.MenuFragmentListener
    public MovieManager getMovieManager() {
        return this.mMovieManager;
    }

    @Override // com.miui.gallery.movie.ui.listener.MenuFragmentListener
    public void changeEditor() {
        if (this.mShowMode == 3) {
            this.mShowMode = 2;
            FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
            beginTransaction.hide(this.mEditorMenuFragment);
            beginTransaction.hide(this.mMovieEditorPlayMenuFragment);
            FragmentManager supportFragmentManager = getSupportFragmentManager();
            int i = R$id.preview_panel;
            MoviePreviewMenuFragment moviePreviewMenuFragment = (MoviePreviewMenuFragment) supportFragmentManager.findFragmentById(i);
            this.mPreviewMenuFragment = moviePreviewMenuFragment;
            if (moviePreviewMenuFragment == null) {
                MoviePreviewMenuFragment moviePreviewMenuFragment2 = new MoviePreviewMenuFragment();
                this.mPreviewMenuFragment = moviePreviewMenuFragment2;
                beginTransaction.add(i, moviePreviewMenuFragment2);
            } else {
                beginTransaction.show(moviePreviewMenuFragment);
            }
            beginTransaction.commit();
            this.mMovieView.postDelayed(new Runnable() { // from class: com.miui.gallery.movie.ui.activity.MovieActivity$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public final void run() {
                    MovieActivity.$r8$lambda$C05I9SRfDbUOGyex_ZPQGENZyuU(MovieActivity.this);
                }
            }, getResources().getInteger(R$integer.movie_preview_appear_delay));
        } else {
            this.mShowMode = 3;
            FragmentTransaction beginTransaction2 = getSupportFragmentManager().beginTransaction();
            beginTransaction2.hide(this.mPreviewMenuFragment);
            FragmentManager supportFragmentManager2 = getSupportFragmentManager();
            int i2 = R$id.editor_panel;
            MovieEditorMenuFragment movieEditorMenuFragment = (MovieEditorMenuFragment) supportFragmentManager2.findFragmentById(i2);
            this.mEditorMenuFragment = movieEditorMenuFragment;
            if (movieEditorMenuFragment == null) {
                MovieEditorMenuFragment movieEditorMenuFragment2 = new MovieEditorMenuFragment();
                this.mEditorMenuFragment = movieEditorMenuFragment2;
                beginTransaction2.add(i2, movieEditorMenuFragment2);
                Bundle bundle = new Bundle();
                bundle.putLong("card_id", this.mStoryMovieCardId);
                this.mEditorMenuFragment.setArguments(bundle);
            } else {
                beginTransaction2.show(movieEditorMenuFragment);
            }
            MovieEditorTopMenuFragment movieEditorTopMenuFragment = this.mMovieEditorPlayMenuFragment;
            if (movieEditorTopMenuFragment == null) {
                MovieEditorTopMenuFragment movieEditorTopMenuFragment2 = new MovieEditorTopMenuFragment();
                this.mMovieEditorPlayMenuFragment = movieEditorTopMenuFragment2;
                beginTransaction2.add(R$id.nav_area, movieEditorTopMenuFragment2);
            } else {
                beginTransaction2.show(movieEditorTopMenuFragment);
            }
            beginTransaction2.commit();
            setSystemBarVisible(false);
        }
        setForMode();
        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setInterpolator(new QuarticEaseOutInterpolator());
        changeBounds.setStartDelay(getResources().getInteger(R$integer.movie_editor_appear_delay));
        changeBounds.setDuration(getResources().getInteger(R$integer.movie_editor_appear_duration));
        TransitionManager.beginDelayedTransition(this.mRootView, changeBounds);
        if (this.mShowMode == 3) {
            this.mMovieView.setLayoutParams(this.mEditorMovieLps);
        } else {
            this.mMovieView.setLayoutParams(this.mPreviewMovieLps);
        }
    }

    public /* synthetic */ void lambda$changeEditor$5() {
        setSystemBarVisible(true);
    }

    public void switchBetweenEditAndFullScreen() {
        this.mIsEditorPreview = !this.mIsEditorPreview;
        ChangeBounds changeBounds = new ChangeBounds();
        changeBounds.setInterpolator(new QuarticEaseInOutInterpolator());
        changeBounds.setDuration(getResources().getInteger(R$integer.movie_editor_preview_duration));
        TransitionManager.beginDelayedTransition(this.mRootView, changeBounds);
        if (this.mIsEditorPreview) {
            enterFullScreen();
        } else {
            exitFullScreen();
        }
    }

    public final void exitFullScreen() {
        AnimatorSet animatorSet = new AnimatorSet();
        View view = this.mEditorMenuView;
        Property property = View.TRANSLATION_Y;
        float[] fArr = {view.getHeight(), 0.0f};
        View view2 = this.mMovieTopMenuView;
        animatorSet.playTogether(ObjectAnimator.ofFloat(view, property, fArr), ObjectAnimator.ofFloat(view2, View.TRANSLATION_Y, (-view2.getHeight()) * 2, 0.0f));
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.movie.ui.activity.MovieActivity.6
            {
                MovieActivity.this = this;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                MovieActivity.this.mMovieTopMenuView.setVisibility(0);
                MovieActivity.this.mEditorMenuView.setVisibility(0);
            }
        });
        animatorSet.setupStartValues();
        animatorSet.setInterpolator(new CubicEaseInOutInterpolator());
        animatorSet.setDuration(getResources().getInteger(R$integer.movie_editor_preview_duration));
        animatorSet.start();
        this.mMovieView.showExtraContent(true);
        this.mMovieView.setLoopPlay(false);
        this.mMovieView.setSeekDisable(false);
        this.mMovieView.setLayoutParams(this.mEditorMovieLps);
        this.mMovieView.setTouchAvailable(true);
    }

    public final void enterFullScreen() {
        AnimatorSet animatorSet = new AnimatorSet();
        View view = this.mEditorMenuView;
        Property property = View.TRANSLATION_Y;
        float[] fArr = {0.0f, view.getHeight()};
        View view2 = this.mMovieTopMenuView;
        animatorSet.playTogether(ObjectAnimator.ofFloat(view, property, fArr), ObjectAnimator.ofFloat(view2, View.TRANSLATION_Y, 0.0f, (-view2.getHeight()) * 2));
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.movie.ui.activity.MovieActivity.7
            {
                MovieActivity.this = this;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                MovieActivity.this.mMovieTopMenuView.setVisibility(4);
                MovieActivity.this.mEditorMenuView.setVisibility(4);
            }
        });
        animatorSet.setupStartValues();
        animatorSet.setInterpolator(new CubicEaseInOutInterpolator());
        animatorSet.setDuration(getResources().getInteger(R$integer.movie_editor_preview_duration));
        animatorSet.start();
        this.mMovieView.showExtraContent(false);
        this.mMovieView.setLoopPlay(true);
        this.mMovieView.setSeekDisable(true);
        this.mMovieView.setLayoutParams(this.mPreviewMovieLps);
        this.mMovieView.setTouchAvailable(false);
        this.mMovieManager.replay();
    }

    public void changeFullScreen(boolean z) {
        this.mShowMode = z ? 1 : 2;
        setSystemBarVisible(!z);
        if (z) {
            doFullScreenChangeAnimal(this.mPreviewMenuView, false);
        } else {
            doFullScreenChangeAnimal(this.mPreviewMenuView, true);
        }
    }

    public final void doFullScreenChangeAnimal(final View view, boolean z) {
        AnimatorSet animatorSet = new AnimatorSet();
        int height = view.getHeight();
        if (z) {
            animatorSet.playTogether(ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, height, 0.0f));
            animatorSet.setInterpolator(new CubicEaseOutInterpolator());
            animatorSet.setDuration(getResources().getInteger(R$integer.movie_background_appear_duration));
            animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.movie.ui.activity.MovieActivity.8
                {
                    MovieActivity.this = this;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    view.setVisibility(0);
                }
            });
        } else {
            animatorSet.playTogether(ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, 0.0f, height));
            animatorSet.setInterpolator(new CubicEaseInInterpolator());
            animatorSet.setDuration(getResources().getInteger(R$integer.movie_background_disappear_duration));
            animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.movie.ui.activity.MovieActivity.9
                {
                    MovieActivity.this = this;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    view.setVisibility(8);
                }
            });
            animatorSet.setupStartValues();
        }
        animatorSet.start();
    }

    @Override // com.miui.gallery.movie.ui.listener.MenuFragmentListener
    public void returnClick() {
        if (this.mIsEditorPreview) {
            switchBetweenEditAndFullScreen();
        } else if (this.mShowMode == 3 && this.mMovieInfo.isFromStory) {
            setShowDeleteMode(false);
            changeEditor();
        } else {
            finish();
        }
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        returnClick();
    }

    @Override // com.miui.gallery.movie.ui.listener.MenuFragmentListener
    public void showLoadingView() {
        this.mMovieView.showLoadingView(true);
    }

    @Override // com.miui.gallery.movie.ui.listener.MenuFragmentListener
    public void setDeleteVisible(boolean z) {
        this.mMovieView.setShowDeleteMode(true);
        this.mMovieView.setDeleteVisible(z);
    }

    @Override // com.miui.gallery.movie.ui.listener.MenuFragmentListener
    public void setShowDeleteMode(boolean z) {
        this.mMovieView.setShowDeleteMode(z);
    }

    @Override // com.miui.gallery.movie.ui.listener.MenuFragmentListener
    public void seekToIndex(int i) {
        int seekToIndex = this.mMovieManager.seekToIndex(i);
        this.mMovieView.setCurrentPlayTime(seekToIndex, seekToIndex / this.mMovieManager.getTotalTime());
        MoviePreviewMenuFragment moviePreviewMenuFragment = this.mPreviewMenuFragment;
        if (moviePreviewMenuFragment != null) {
            moviePreviewMenuFragment.onProgressChange(seekToIndex, this.mMovieManager.getTotalTime());
        }
    }

    @Override // com.miui.gallery.movie.ui.listener.MenuFragmentListener
    public void seek(int i) {
        this.mMovieManager.seek(i);
        this.mMovieManager.pause();
        this.mMovieView.setCurrentPlayTime(i, i / this.mMovieManager.getTotalTime());
    }

    @Override // com.miui.gallery.movie.ui.listener.MenuFragmentListener
    public void export(boolean z) {
        if (!FilePermissionUtils.checkFileCreatePermission(this, MovieStorage.getOutputMediaFilePath())) {
            return;
        }
        if (this.mMovieSavingFragment == null) {
            this.mMovieSavingFragment = new MovieSavingFragment();
        }
        this.mMovieSavingFragment.show(this.mActivity, this.mMovieManager, this.mMovieInfo, z, new MovieSavingFragment.OnSavingFinishListener() { // from class: com.miui.gallery.movie.ui.activity.MovieActivity$$ExternalSyntheticLambda4
            @Override // com.miui.gallery.movie.ui.fragment.MovieSavingFragment.OnSavingFinishListener
            public final void onFinish(boolean z2, boolean z3, String str) {
                MovieActivity.$r8$lambda$gAxBXqsWsx80SXBnPcJkfad5biw(MovieActivity.this, z2, z3, str);
            }
        });
    }

    public /* synthetic */ void lambda$export$6(boolean z, boolean z2, String str) {
        if (isDestroyed()) {
            DefaultLogger.w("MovieActivity_", "movie activity is finish on saving finish");
        } else if (!this.mMovieInfo.isFromStory) {
            setResult(-1);
            finish();
            MovieUtils.goDetail(this.mActivity, Uri.fromFile(new File(str)));
        } else {
            finish();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        doDestroy();
        this.mMovieManager = null;
    }

    @Override // com.miui.gallery.movie.ui.listener.MenuFragmentListener
    public void updateStorySha1Data() {
        ArrayList<String> arrayList = this.mStorySha1List;
        if (arrayList == null) {
            DefaultLogger.e("MovieActivity_", "mStorySha1List is null. ");
            return;
        }
        arrayList.clear();
        for (int i = 0; i < this.mMovieInfo.imageList.size(); i++) {
            this.mStorySha1List.add(this.mMovieInfo.imageList.get(i).sha1);
        }
    }

    @Override // com.miui.gallery.movie.ui.listener.MenuFragmentListener
    public ArrayList<String> getStoryMovieSha1() {
        return this.mStorySha1List;
    }

    @Override // com.miui.gallery.movie.ui.listener.IShareDataCallback
    public void updateShareData(boolean z) {
        MovieShareData movieShareData = this.mMovieShareData;
        if (movieShareData != null) {
            movieShareData.setShortVideo(z);
        }
    }

    @Override // com.miui.gallery.movie.ui.listener.IShareDataCallback
    public void resetShareData() {
        MovieShareData movieShareData = this.mMovieShareData;
        if (movieShareData != null) {
            movieShareData.reset(this.mMovieInfo.isShortVideo);
        }
    }

    @Override // com.miui.gallery.movie.ui.listener.IShareDataCallback
    public void handleShareEvent(String str) {
        MovieShareData movieShareData = this.mMovieShareData;
        if (movieShareData != null) {
            movieShareData.setVideoPath(str, this.mMovieInfo.isShortVideo);
        }
        doShare(str);
    }

    public final void doShare(String str) {
        if (TextUtils.isEmpty(str)) {
            DefaultLogger.e("MovieActivity_", "share outFilePath is null");
            return;
        }
        Uri translateToContent = GalleryOpenProviderUtils.translateToContent(str);
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.setType("video/*");
        intent.putExtra("android.intent.extra.STREAM", translateToContent);
        intent.addFlags(268435456);
        intent.addFlags(1);
        intent.addFlags(134742016);
        List<ResolveInfo> queryIntentActivities = this.mActivity.getPackageManager().queryIntentActivities(intent, 65536);
        if (!BaseMiscUtil.isValid(queryIntentActivities)) {
            DefaultLogger.e("MovieActivity_", "doShare: resInfoList is invalid.");
            return;
        }
        for (ResolveInfo resolveInfo : queryIntentActivities) {
            this.mActivity.grantUriPermission(resolveInfo.activityInfo.packageName, translateToContent, 1);
        }
        this.mActivity.startActivityForResult(Intent.createChooser(intent, this.mActivity.getString(R$string.movie_preview_share_title)), 1);
    }

    @Override // com.miui.gallery.movie.ui.listener.IShareDataCallback
    public void cancelExport() {
        this.mMovieManager.cancelExport();
    }

    @Override // com.miui.gallery.permission.core.PermissionCheckCallback
    public Permission[] getRuntimePermissions() {
        return new Permission[]{new Permission("android.permission.WRITE_EXTERNAL_STORAGE", getString(com.miui.gallery.permission.R$string.permission_storage_desc), true), new Permission("android.permission.INTERNET", "", true)};
    }

    public final boolean containUnsupportFile(List<ImageEntity> list) {
        Iterator<ImageEntity> it = list.iterator();
        while (it.hasNext()) {
            if (!MovieConfig.isMimeTypeSupport(BaseFileMimeUtil.getMimeType(it.next().image))) {
                it.remove();
            }
        }
        return list.size() < 3;
    }
}
