package com.miui.gallery.gallerywidget.ui.editor;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Outline;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts$StartActivityForResult;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.SpringItemTouchHelper;
import com.miui.gallery.R;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.app.screenChange.IScreenChange;
import com.miui.gallery.editor.photo.core.imports.obsoletes.EditorView;
import com.miui.gallery.gallerywidget.common.GalleryWidgetUtils;
import com.miui.gallery.gallerywidget.common.IWidgetProviderConfig;
import com.miui.gallery.gallerywidget.common.WidgetStatisticsHelper;
import com.miui.gallery.gallerywidget.ui.InterceptorLayout;
import com.miui.gallery.gallerywidget.ui.editor.WidgetEditorAdapter;
import com.miui.gallery.gallerywidget.ui.editor.WidgetEditorPlugin;
import com.miui.gallery.movie.ui.adapter.BaseAdapter;
import com.miui.gallery.ui.CenterSmoothScrollerController;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BuildUtil;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.SystemUiUtil;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.recyclerview.CustomScrollerLinearLayoutManager;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerView;
import java.util.List;
import miuix.recyclerview.widget.RecyclerView;

/* loaded from: classes2.dex */
public class WidgetPhotoEditorActivity extends BaseActivity implements WidgetEditorContract$IWidgetEditorView, View.OnClickListener {
    public int mAppWidgetId;
    public View mBackView;
    public View mConfirmView;
    public InterceptorLayout mContentLayout;
    public long mCreateTime;
    public ConstraintLayout mDisplayArea;
    public WidgetEditorAdapter mEditAdapter;
    public WidgetEditorPlugin mEditorPlugin;
    public EditorView mEditorView;
    public ViewGroup mImageContentLayout;
    public boolean mIsFromWidgetEditor;
    public boolean mLargeScreenMode;
    public View mLeftPlaceholderView;
    public int mMiuiWidgetScreenSide;
    public ActivityResultLauncher<Intent> mPickLauncher;
    public ImageView mPlaceholderImage;
    public View mPlaceholderLayout;
    public View mProgressLayout;
    public View mRecyclerMaskView;
    public SimpleRecyclerView mRecyclerView;
    public String mRefStatus;
    public View mReselectView;
    public View mRightPlaceholderView;
    public View mRootView;
    public Intent mTargetIntent;
    public WidgetEditorPresenter mWidgetEditorPresenter;
    public final String TAG = "WidgetPhotoEditorActivity";
    public final int RECYCLER_ONE_SCREEN_COUNT = 3;
    public String mTypeStatus = "unmodified";
    public float mAspectRatio = 1.0f;
    public IWidgetProviderConfig.WidgetSize mWidgetSize = IWidgetProviderConfig.WidgetSize.SIZE_2_2;
    public boolean mActivityFirstIn = true;
    public final ViewTreeObserver.OnGlobalLayoutListener mImageContentLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.miui.gallery.gallerywidget.ui.editor.WidgetPhotoEditorActivity.3
        {
            WidgetPhotoEditorActivity.this = this;
        }

        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public void onGlobalLayout() {
            WidgetPhotoEditorActivity.this.mImageContentLayout.getViewTreeObserver().removeOnGlobalLayoutListener(WidgetPhotoEditorActivity.this.mImageContentLayoutListener);
            int width = (WidgetPhotoEditorActivity.this.mImageContentLayout.getWidth() - WidgetPhotoEditorActivity.this.getResources().getDimensionPixelSize(R.dimen.gallery_widget_editor_recycler_item_width)) - WidgetPhotoEditorActivity.this.getResources().getDimensionPixelSize(R.dimen.gallery_widget_editor_image_add_margin_end);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) WidgetPhotoEditorActivity.this.mRecyclerView.getLayoutParams();
            layoutParams.matchConstraintMaxWidth = width;
            WidgetPhotoEditorActivity.this.mRecyclerView.setLayoutParams(layoutParams);
        }
    };
    public WidgetEditorPlugin.CropChangedListener mCropChangedListener = new WidgetEditorPlugin.CropChangedListener() { // from class: com.miui.gallery.gallerywidget.ui.editor.WidgetPhotoEditorActivity$$ExternalSyntheticLambda4
        @Override // com.miui.gallery.gallerywidget.ui.editor.WidgetEditorPlugin.CropChangedListener
        public final void cropChanged() {
            WidgetPhotoEditorActivity.$r8$lambda$1FLIUhd0BeAsCl5GJ2wIYC2T28A(WidgetPhotoEditorActivity.this);
        }
    };
    public final View.OnScrollChangeListener mOnScrollChangeListener = new View.OnScrollChangeListener() { // from class: com.miui.gallery.gallerywidget.ui.editor.WidgetPhotoEditorActivity$$ExternalSyntheticLambda0
        @Override // android.view.View.OnScrollChangeListener
        public final void onScrollChange(View view, int i, int i2, int i3, int i4) {
            WidgetPhotoEditorActivity.$r8$lambda$PM_kGbuMs8CrXPRvULVFhW9XiDA(WidgetPhotoEditorActivity.this, view, i, i2, i3, i4);
        }
    };
    public final WidgetEditorAdapter.OnActionListener mEditActionListener = new WidgetEditorAdapter.OnActionListener() { // from class: com.miui.gallery.gallerywidget.ui.editor.WidgetPhotoEditorActivity.4
        {
            WidgetPhotoEditorActivity.this = this;
        }

        @Override // com.miui.gallery.gallerywidget.ui.editor.WidgetEditorAdapter.OnActionListener
        public void onMove(int i, int i2) {
            DefaultLogger.d("WidgetPhotoEditorActivity", "---log---onMove fromPos:%d,toPs:%d>", Integer.valueOf(i), Integer.valueOf(i2));
            WidgetPhotoEditorActivity.this.mWidgetEditorPresenter.setCurrentPicIndex(WidgetPhotoEditorActivity.this.mEditAdapter.getSelectedItemPosition());
        }

        @Override // com.miui.gallery.gallerywidget.ui.editor.WidgetEditorAdapter.OnActionListener
        public void onDelete(int i) {
            if (i < 0 || i >= WidgetPhotoEditorActivity.this.getImageEntityList().size()) {
                return;
            }
            boolean z = WidgetPhotoEditorActivity.this.mEditAdapter.getSelectedItemPosition() == i;
            if (i < WidgetPhotoEditorActivity.this.mEditAdapter.getSelectedItemPosition()) {
                WidgetPhotoEditorActivity.this.mEditAdapter.setSelectedItemPositionWithoutNotify(WidgetPhotoEditorActivity.this.mEditAdapter.getSelectedItemPosition() - 1);
                WidgetPhotoEditorActivity.this.mWidgetEditorPresenter.setCurrentPicIndex(WidgetPhotoEditorActivity.this.mEditAdapter.getSelectedItemPosition());
            }
            WidgetPhotoEditorActivity.this.getImageEntityList().remove(i);
            WidgetPhotoEditorActivity.this.mEditAdapter.notifyItemRemoved(i);
            if (z) {
                WidgetPhotoEditorActivity.this.mEditAdapter.setSelectedItemPositionWithoutNotify(-1);
                WidgetPhotoEditorActivity.this.mEditAdapter.setSelectedItemPosition(0);
                WidgetPhotoEditorActivity.this.mWidgetEditorPresenter.setCurrentPicIndex(0);
                WidgetPhotoEditorActivity.this.loadPicture();
            }
            if (WidgetPhotoEditorActivity.this.getImageEntityList().size() <= 1) {
                WidgetPhotoEditorActivity.this.mEditAdapter.setEditorMode(false);
                WidgetPhotoEditorActivity.this.mEditAdapter.notifyDataSetChanged();
            }
            WidgetPhotoEditorActivity.this.showRecyclerMaskView();
        }
    };
    public final View.OnLayoutChangeListener mOnLayoutChangeListener = new View.OnLayoutChangeListener() { // from class: com.miui.gallery.gallerywidget.ui.editor.WidgetPhotoEditorActivity.5
        {
            WidgetPhotoEditorActivity.this = this;
        }

        @Override // android.view.View.OnLayoutChangeListener
        public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            WidgetPhotoEditorActivity.this.mEditorView.removeOnLayoutChangeListener(WidgetPhotoEditorActivity.this.mOnLayoutChangeListener);
            WidgetPhotoEditorActivity.this.mWidgetEditorPresenter.loadData(WidgetPhotoEditorActivity.this.mTargetIntent != null ? WidgetPhotoEditorActivity.this.mTargetIntent : WidgetPhotoEditorActivity.this.getIntent());
            WidgetPhotoEditorActivity.this.mEditorPlugin.setRatio(WidgetPhotoEditorActivity.this.mAspectRatio);
        }
    };
    public final ActivityResultCallback<ActivityResult> mPickCallBack = new ActivityResultCallback() { // from class: com.miui.gallery.gallerywidget.ui.editor.WidgetPhotoEditorActivity$$ExternalSyntheticLambda1
        @Override // androidx.activity.result.ActivityResultCallback
        public final void onActivityResult(Object obj) {
            WidgetPhotoEditorActivity.$r8$lambda$azabd3USP82cChOF2jQljlnm910(WidgetPhotoEditorActivity.this, (ActivityResult) obj);
        }
    };

    public static /* synthetic */ void $r8$lambda$1FLIUhd0BeAsCl5GJ2wIYC2T28A(WidgetPhotoEditorActivity widgetPhotoEditorActivity) {
        widgetPhotoEditorActivity.lambda$new$2();
    }

    public static /* synthetic */ void $r8$lambda$PM_kGbuMs8CrXPRvULVFhW9XiDA(WidgetPhotoEditorActivity widgetPhotoEditorActivity, View view, int i, int i2, int i3, int i4) {
        widgetPhotoEditorActivity.lambda$new$3(view, i, i2, i3, i4);
    }

    public static /* synthetic */ void $r8$lambda$aB8KQk68aUc8k1lrsJwmTK85dsM(WidgetPhotoEditorActivity widgetPhotoEditorActivity, Configuration configuration) {
        widgetPhotoEditorActivity.lambda$init$0(configuration);
    }

    public static /* synthetic */ void $r8$lambda$azabd3USP82cChOF2jQljlnm910(WidgetPhotoEditorActivity widgetPhotoEditorActivity, ActivityResult activityResult) {
        widgetPhotoEditorActivity.lambda$new$4(activityResult);
    }

    public static /* synthetic */ void $r8$lambda$g4FJaR5axQQnB0RwNnlLYuOk3r8(WidgetPhotoEditorActivity widgetPhotoEditorActivity, Configuration configuration) {
        widgetPhotoEditorActivity.lambda$init$1(configuration);
    }

    @Override // com.miui.gallery.activity.BaseActivity
    public boolean hasCustomContentView() {
        return true;
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_widget_photo_editro);
        Intent intent = getIntent();
        this.mTargetIntent = intent;
        this.mWidgetEditorPresenter = new WidgetEditorPresenter(this, intent, this);
        init(bundle);
    }

    @Override // com.miui.gallery.app.activity.GalleryActivity, miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("isLargeScreenMode", this.mLargeScreenMode);
        bundle.putInt("miuiWidgetScreenSide", this.mMiuiWidgetScreenSide);
    }

    public void setDisplay() {
        int i;
        if (this.mLargeScreenMode && ((i = this.mMiuiWidgetScreenSide) == 0 || i == 1)) {
            setFloatDisplay();
        } else {
            setNormalDisplay();
        }
    }

    public final void setNormalDisplay() {
        this.mLeftPlaceholderView.setVisibility(8);
        this.mRightPlaceholderView.setVisibility(8);
        this.mDisplayArea.setClipToOutline(false);
        this.mRootView.setPadding(0, 0, 0, 0);
    }

    public final void setFloatDisplay() {
        int i = this.mMiuiWidgetScreenSide;
        if (i == 0) {
            this.mLeftPlaceholderView.setVisibility(0);
        } else if (i == 1) {
            this.mRightPlaceholderView.setVisibility(0);
        }
        this.mDisplayArea.setClipToOutline(true);
        if (MiscUtil.isLandModeAndSupportVersion(this)) {
            this.mRootView.setPadding(getResources().getDimensionPixelOffset(R.dimen.gallery_widget_editor_floatWindow_horizontalScreen_margin_left), MiscUtil.getStatusBarHeight(this), getResources().getDimensionPixelOffset(R.dimen.gallery_widget_editor_floatWindow_horizontalScreen_margin_right), getResources().getDimensionPixelOffset(R.dimen.gallery_widget_editor_floatWindow_horizontalScreen_margin_bottom));
        } else {
            this.mRootView.setPadding(getResources().getDimensionPixelOffset(R.dimen.gallery_widget_editor_floatWindow_margin_left), MiscUtil.getStatusBarHeight(this), getResources().getDimensionPixelOffset(R.dimen.gallery_widget_editor_floatWindow_margin_left), MiscUtil.getStatusBarHeight(this));
        }
        this.mRootView.setBackgroundResource(17170445);
    }

    @Override // com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (BuildUtil.isFoldingDevice()) {
            if ((configuration.screenLayout & 15) == 3) {
                this.mLargeScreenMode = true;
            } else {
                this.mLargeScreenMode = false;
            }
            setDisplay();
        }
    }

    public final void handleScreenOrientationChanged() {
        initCropView();
    }

    public final void handleOrientation() {
        if (BuildUtil.isFoldingDevice() && BaseBuildUtil.isLargeHorizontalWindow()) {
            SystemUiUtil.setRequestedOrientation(4, this);
        } else {
            SystemUiUtil.setRequestedOrientation(1, this);
        }
    }

    public final void init(Bundle bundle) {
        this.mIsFromWidgetEditor = getIntent().getBooleanExtra("is_from_widget_editor", false);
        this.mAppWidgetId = getIntent().getIntExtra("gallery_app_widget_id", -1);
        this.mWidgetSize = (IWidgetProviderConfig.WidgetSize) getIntent().getSerializableExtra("gallery_app_widget_size");
        this.mCreateTime = System.currentTimeMillis();
        this.mRefStatus = this.mIsFromWidgetEditor ? "selectedimages_ture" : "selectedimages_null";
        this.mProgressLayout = findViewById(R.id.layout_progress);
        ViewGroup viewGroup = (ViewGroup) findViewById(R.id.layout_image_content);
        this.mImageContentLayout = viewGroup;
        viewGroup.getViewTreeObserver().addOnGlobalLayoutListener(this.mImageContentLayoutListener);
        this.mRecyclerView = (SimpleRecyclerView) findViewById(R.id.recycler_view);
        this.mRecyclerMaskView = findViewById(R.id.recycle_view_mask);
        this.mPlaceholderLayout = findViewById(R.id.layout_placeholder);
        this.mPlaceholderImage = (ImageView) findViewById(R.id.img_placeholder);
        this.mContentLayout = (InterceptorLayout) findViewById(R.id.layout_content);
        this.mEditorView = (EditorView) findViewById(R.id.editor_view);
        WidgetEditorPlugin widgetEditorPlugin = new WidgetEditorPlugin(this);
        this.mEditorPlugin = widgetEditorPlugin;
        widgetEditorPlugin.setCropChangedListener(this.mCropChangedListener);
        this.mEditorView.install(this.mEditorPlugin);
        this.mEditorView.setDrawBoundLine(false);
        this.mEditorView.addOnLayoutChangeListener(this.mOnLayoutChangeListener);
        View findViewById = findViewById(R.id.image_add);
        this.mReselectView = findViewById;
        findViewById.setOnClickListener(this);
        View findViewById2 = findViewById(R.id.iv_back);
        this.mBackView = findViewById2;
        findViewById2.setOnClickListener(this);
        this.mBackView.setAccessibilityDelegate(new View.AccessibilityDelegate() { // from class: com.miui.gallery.gallerywidget.ui.editor.WidgetPhotoEditorActivity.1
            {
                WidgetPhotoEditorActivity.this = this;
            }

            @Override // android.view.View.AccessibilityDelegate
            public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                accessibilityNodeInfo.setClickable(false);
            }
        });
        View findViewById3 = findViewById(R.id.text_ok);
        this.mConfirmView = findViewById3;
        findViewById3.setOnClickListener(this);
        this.mPickLauncher = registerForActivityResult(new ActivityResultContracts$StartActivityForResult(), this.mPickCallBack);
        initCropView();
        initRecyclerView();
        loadPlaceHolderImage();
        showProgressBar();
        this.mLeftPlaceholderView = findViewById(R.id.left_placeholder_view);
        this.mRightPlaceholderView = findViewById(R.id.right_placeholder_view);
        this.mRootView = findViewById(R.id.gallery_widget_editor_root_view);
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.gallery_widget_editor_display_area);
        this.mDisplayArea = constraintLayout;
        constraintLayout.setOutlineProvider(new ViewOutlineProvider() { // from class: com.miui.gallery.gallerywidget.ui.editor.WidgetPhotoEditorActivity.2
            {
                WidgetPhotoEditorActivity.this = this;
            }

            @Override // android.view.ViewOutlineProvider
            public void getOutline(View view, Outline outline) {
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), WidgetPhotoEditorActivity.this.getResources().getDimensionPixelOffset(R.dimen.widget_select_list_float_radius));
            }
        });
        this.mDisplayArea.setClipToOutline(true);
        if (BuildUtil.isFoldingDevice()) {
            if (bundle != null) {
                this.mLargeScreenMode = bundle.getBoolean("isLargeScreenMode", false);
                this.mMiuiWidgetScreenSide = bundle.getInt("miuiWidgetScreenSide", -1);
            } else {
                this.mLargeScreenMode = this.mTargetIntent.getBooleanExtra("isLargeScreenMode", false);
                this.mMiuiWidgetScreenSide = this.mTargetIntent.getIntExtra("miuiWidgetScreenSide", -1);
            }
            setDisplay();
        } else {
            setNormalDisplay();
        }
        handleOrientation();
        addScreenChangeListener(new IScreenChange.OnScreenLayoutSizeChangeListener() { // from class: com.miui.gallery.gallerywidget.ui.editor.WidgetPhotoEditorActivity$$ExternalSyntheticLambda3
            @Override // com.miui.gallery.app.screenChange.IScreenChange.OnScreenLayoutSizeChangeListener
            public final void onScreenLayoutSizeChange(Configuration configuration) {
                WidgetPhotoEditorActivity.$r8$lambda$aB8KQk68aUc8k1lrsJwmTK85dsM(WidgetPhotoEditorActivity.this, configuration);
            }
        });
        addScreenChangeListener(new IScreenChange.OnOrientationChangeListener() { // from class: com.miui.gallery.gallerywidget.ui.editor.WidgetPhotoEditorActivity$$ExternalSyntheticLambda2
            @Override // com.miui.gallery.app.screenChange.IScreenChange.OnOrientationChangeListener
            public final void onOrientationChange(Configuration configuration) {
                WidgetPhotoEditorActivity.$r8$lambda$g4FJaR5axQQnB0RwNnlLYuOk3r8(WidgetPhotoEditorActivity.this, configuration);
            }
        });
    }

    public /* synthetic */ void lambda$init$0(Configuration configuration) {
        handleOrientation();
    }

    public /* synthetic */ void lambda$init$1(Configuration configuration) {
        handleScreenOrientationChanged();
    }

    public final void loadRecyclerData() {
        this.mEditAdapter.setList(getImageEntityList());
        WidgetEditorAdapter widgetEditorAdapter = this.mEditAdapter;
        boolean z = true;
        if (getImageEntityList().size() <= 1) {
            z = false;
        }
        widgetEditorAdapter.setEditorMode(z);
        this.mEditAdapter.setSelectedItemPosition(getCurrentPicIndex());
        this.mEditAdapter.notifyDataSetChanged();
    }

    public final List<ImageEntity> getImageEntityList() {
        return this.mWidgetEditorPresenter.getDataList();
    }

    public final int getCurrentPicIndex() {
        return this.mWidgetEditorPresenter.getCurrentPicIndex();
    }

    public /* synthetic */ void lambda$new$2() {
        this.mTypeStatus = "modified";
    }

    /* renamed from: com.miui.gallery.gallerywidget.ui.editor.WidgetPhotoEditorActivity$6 */
    /* loaded from: classes2.dex */
    public static /* synthetic */ class AnonymousClass6 {
        public static final /* synthetic */ int[] $SwitchMap$com$miui$gallery$gallerywidget$common$IWidgetProviderConfig$WidgetSize;

        static {
            int[] iArr = new int[IWidgetProviderConfig.WidgetSize.values().length];
            $SwitchMap$com$miui$gallery$gallerywidget$common$IWidgetProviderConfig$WidgetSize = iArr;
            try {
                iArr[IWidgetProviderConfig.WidgetSize.SIZE_2_2.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$miui$gallery$gallerywidget$common$IWidgetProviderConfig$WidgetSize[IWidgetProviderConfig.WidgetSize.SIZE_4_2.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$miui$gallery$gallerywidget$common$IWidgetProviderConfig$WidgetSize[IWidgetProviderConfig.WidgetSize.SIZE_2_3.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$miui$gallery$gallerywidget$common$IWidgetProviderConfig$WidgetSize[IWidgetProviderConfig.WidgetSize.SIZE_4_4.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    public final void initCropView() {
        int dimensionPixelSize;
        float dimensionPixelSize2;
        int dimensionPixelSize3;
        int i = AnonymousClass6.$SwitchMap$com$miui$gallery$gallerywidget$common$IWidgetProviderConfig$WidgetSize[this.mWidgetSize.ordinal()];
        if (i == 1) {
            dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.gallery_widget_editor_cropview_2_2_height);
            dimensionPixelSize2 = getResources().getDimensionPixelSize(R.dimen.gallery_widget_editor_crop_area_2_2_width);
            dimensionPixelSize3 = getResources().getDimensionPixelSize(R.dimen.gallery_widget_editor_crop_area_2_2_height);
        } else if (i == 2) {
            dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.gallery_widget_editor_cropview_4_2_height);
            dimensionPixelSize2 = getResources().getDimensionPixelSize(R.dimen.gallery_widget_editor_crop_area_4_2_width);
            dimensionPixelSize3 = getResources().getDimensionPixelSize(R.dimen.gallery_widget_editor_crop_area_4_2_height);
        } else if (i == 3) {
            dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.gallery_widget_editor_cropview_2_3_height);
            dimensionPixelSize2 = getResources().getDimensionPixelSize(R.dimen.gallery_widget_editor_crop_area_2_3_width);
            dimensionPixelSize3 = getResources().getDimensionPixelSize(R.dimen.gallery_widget_editor_crop_area_2_3_height);
        } else {
            dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.gallery_widget_editor_cropview_4_4_height);
            dimensionPixelSize2 = getResources().getDimensionPixelSize(R.dimen.gallery_widget_editor_crop_area_4_4_width);
            dimensionPixelSize3 = getResources().getDimensionPixelSize(R.dimen.gallery_widget_editor_crop_area_4_4_height);
        }
        this.mAspectRatio = dimensionPixelSize2 / dimensionPixelSize3;
        ViewGroup.LayoutParams layoutParams = this.mContentLayout.getLayoutParams();
        layoutParams.height = dimensionPixelSize + getResources().getDimensionPixelSize(R.dimen.gallery_widget_editor_image_content_height);
        this.mContentLayout.setLayoutParams(layoutParams);
    }

    public final void initRecyclerView() {
        CustomScrollerLinearLayoutManager customScrollerLinearLayoutManager = new CustomScrollerLinearLayoutManager(this);
        customScrollerLinearLayoutManager.setSmoothScroller(new CenterSmoothScrollerController(this));
        customScrollerLinearLayoutManager.setOrientation(0);
        this.mRecyclerView.setLayoutManager(customScrollerLinearLayoutManager);
        this.mRecyclerView.setAlwaysDisableSpring(true);
        this.mRecyclerView.setEnableItemClickWhileSettling(true);
        this.mRecyclerView.setOnScrollChangeListener(this.mOnScrollChangeListener);
        WidgetEditorAdapter widgetEditorAdapter = new WidgetEditorAdapter(this, this.mRecyclerView);
        this.mEditAdapter = widgetEditorAdapter;
        widgetEditorAdapter.setOnActionListener(this.mEditActionListener);
        this.mEditAdapter.setItemSelectChangeListener(new ItemSelectChangeListener());
        new SpringItemTouchHelper(this.mEditAdapter.getCallBack()).attachToRecyclerView(this.mRecyclerView);
        this.mRecyclerView.setAdapter(this.mEditAdapter);
    }

    public void showRecyclerMaskView() {
        int i = 0;
        boolean z = this.mWidgetEditorPresenter.getDataList().size() > 3 && this.mRecyclerView.canScrollHorizontally(-1);
        View view = this.mRecyclerMaskView;
        if (!z) {
            i = 4;
        }
        view.setVisibility(i);
    }

    public /* synthetic */ void lambda$new$3(View view, int i, int i2, int i3, int i4) {
        showRecyclerMaskView();
    }

    public final void loadPlaceHolderImage() {
        int i = AnonymousClass6.$SwitchMap$com$miui$gallery$gallerywidget$common$IWidgetProviderConfig$WidgetSize[this.mWidgetSize.ordinal()];
        this.mPlaceholderImage.setImageResource(i != 1 ? i != 2 ? i != 3 ? R.drawable.widget_editor_crop_placeholder_4_4 : R.drawable.widget_editor_crop_placeholder_2_3 : R.drawable.widget_editor_crop_placeholder_4_2 : R.drawable.widget_editor_crop_placeholder_2_2);
    }

    public final void loadPicture() {
        if (TextUtils.isEmpty(getCurrentPicPath())) {
            return;
        }
        this.mConfirmView.setOnClickListener(null);
        this.mEditorView.setBitmap(null);
        this.mEditorPlugin.stop();
        showProgressBar();
        this.mWidgetEditorPresenter.loadPicture();
    }

    public final void showProgressBar() {
        this.mContentLayout.setIntercept(true);
        this.mProgressLayout.setVisibility(0);
    }

    public final void dismissProgressBar() {
        this.mContentLayout.setIntercept(false);
        this.mProgressLayout.setVisibility(8);
    }

    @Override // com.miui.gallery.gallerywidget.ui.editor.WidgetEditorContract$IWidgetEditorView
    public void loadPictureFailed() {
        this.mConfirmView.setOnClickListener(null);
        this.mPlaceholderLayout.setVisibility(0);
        this.mEditorView.setVisibility(4);
        dismissProgressBar();
        ToastUtils.makeText(this, (int) R.string.widget_editor_load_fail);
    }

    @Override // com.miui.gallery.gallerywidget.ui.editor.WidgetEditorContract$IWidgetEditorView
    public void saveSuccess() {
        Intent intent = new Intent();
        intent.putExtra("extra_pick_finish_remove_task", true);
        setResult(-1, intent);
        finish();
        statisticsComplete();
    }

    @Override // com.miui.gallery.gallerywidget.ui.editor.WidgetEditorContract$IWidgetEditorView
    public void saveFailed() {
        DefaultLogger.d("WidgetPhotoEditorActivity", "---log---saveFailed !");
        saveSuccess();
    }

    @Override // com.miui.gallery.gallerywidget.ui.editor.WidgetEditorContract$IWidgetEditorView
    public void loadPictureSuccess() {
        this.mConfirmView.setOnClickListener(this);
        this.mEditorView.setVisibility(0);
        this.mPlaceholderLayout.setVisibility(8);
        dismissProgressBar();
    }

    @Override // com.miui.gallery.gallerywidget.ui.editor.WidgetEditorContract$IWidgetEditorView
    public void setPreviewBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        DefaultLogger.d("WidgetPhotoEditorActivity", "---log---setPreviewBitmap getWidth %d, getHeight %d>", Integer.valueOf(bitmap.getWidth()), Integer.valueOf(bitmap.getHeight()));
        this.mConfirmView.setOnClickListener(this);
        this.mEditorPlugin.start();
        this.mEditorView.setBitmap(bitmap);
        this.mEditorPlugin.setRatio(this.mAspectRatio);
        if (getCurrentImageEntity().getCropMatrix() != null && !BuildUtil.isFoldingDevice()) {
            this.mEditorPlugin.setImageMatrix(getCurrentImageEntity().getCropMatrix());
        }
        statisticsInit();
    }

    public final ImageEntity getCurrentImageEntity() {
        return getImageEntityList().get(getCurrentPicIndex());
    }

    public final String getCurrentPicPath() {
        if (getCurrentPicIndex() >= getImageEntityList().size()) {
            return null;
        }
        return getImageEntityList().get(getCurrentPicIndex()).getPicPath();
    }

    public /* synthetic */ void lambda$new$4(ActivityResult activityResult) {
        if (activityResult.getData() == null || activityResult.getResultCode() != -1) {
            return;
        }
        this.mTargetIntent = activityResult.getData();
        this.mEditorPlugin.start();
        this.mWidgetEditorPresenter.setCurrentPicIndex(0);
        this.mWidgetEditorPresenter.loadPicPathData(activityResult.getData());
        loadRecyclerData();
        loadPicture();
    }

    public final void onSave() {
        showProgressBar();
        fillCurrentImageEntityData();
        if (TextUtils.isEmpty(getCurrentPicPath())) {
            DefaultLogger.e("WidgetPhotoEditorActivity", "---log---onSave mCurrentPicPath is null>");
            return;
        }
        this.mConfirmView.setOnClickListener(null);
        this.mWidgetEditorPresenter.onSave();
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        statisticsBack();
        super.onBackPressed();
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.image_add) {
            Intent pickGalleryIntent = GalleryWidgetUtils.getPickGalleryIntent(this);
            pickGalleryIntent.putStringArrayListExtra("pick_sha1", this.mWidgetEditorPresenter.getPicIDList());
            this.mPickLauncher.launch(pickGalleryIntent);
            statisticsReselect();
        } else if (id == R.id.iv_back) {
            finish();
            statisticsBack();
        } else if (id != R.id.text_ok) {
        } else {
            onSave();
        }
    }

    @Override // miuix.appcompat.app.AppCompatActivity, android.app.Activity
    public void finish() {
        DefaultLogger.d("WidgetPhotoEditorActivity", "---log---finish>");
        WidgetEditorAdapter widgetEditorAdapter = this.mEditAdapter;
        if (widgetEditorAdapter != null) {
            widgetEditorAdapter.release();
        }
        this.mWidgetEditorPresenter.release();
        super.finishAndRemoveTask();
    }

    public final void statisticsInit() {
        if (!this.mActivityFirstIn) {
            return;
        }
        this.mActivityFirstIn = false;
        WidgetStatisticsHelper.statisticsWidgetEditorInit(this.mRefStatus, this.mWidgetSize, this.mWidgetEditorPresenter.getDataList().size(), System.currentTimeMillis() - this.mCreateTime);
    }

    public final void statisticsReselect() {
        WidgetStatisticsHelper.statisticsWidgetEditorReselect(this.mRefStatus, this.mWidgetSize);
    }

    public final void statisticsComplete() {
        WidgetStatisticsHelper.statisticsWidgetEditorComplete(this.mRefStatus, this.mWidgetSize, this.mWidgetEditorPresenter.getDataList().size(), this.mTypeStatus);
    }

    public final void statisticsBack() {
        WidgetStatisticsHelper.statisticsWidgetEditorBack(this.mRefStatus, this.mWidgetSize);
    }

    public final void fillCurrentImageEntityData() {
        this.mWidgetEditorPresenter.fillCurrentImageEntityData(this.mEditorPlugin.getImageMatrix(), this.mEditorPlugin.export().getExportInfo());
    }

    @Override // com.miui.gallery.gallerywidget.ui.editor.WidgetEditorContract$IWidgetEditorView
    public void loadDataFail() {
        DefaultLogger.w("WidgetPhotoEditorActivity", "loadDataFail");
        finish();
    }

    @Override // com.miui.gallery.gallerywidget.ui.editor.WidgetEditorContract$IWidgetEditorView
    public void loadDataSuccess() {
        this.mEditorPlugin.start();
        loadRecyclerData();
        loadPicture();
    }

    /* loaded from: classes2.dex */
    public class ItemSelectChangeListener implements BaseAdapter.ItemSelectChangeListener {
        public ItemSelectChangeListener() {
            WidgetPhotoEditorActivity.this = r1;
        }

        @Override // com.miui.gallery.movie.ui.adapter.BaseAdapter.ItemSelectChangeListener
        public boolean onItemSelect(RecyclerView recyclerView, BaseAdapter.BaseHolder baseHolder, int i, boolean z) {
            if (z) {
                if (i < 0 || i >= WidgetPhotoEditorActivity.this.getImageEntityList().size()) {
                    return true;
                }
                DefaultLogger.d("WidgetPhotoEditorActivity", "---log---onItemSelect position:%d>", Integer.valueOf(i));
                WidgetPhotoEditorActivity.this.fillCurrentImageEntityData();
                WidgetPhotoEditorActivity.this.mWidgetEditorPresenter.setCurrentPicIndex(i);
                WidgetPhotoEditorActivity.this.loadPicture();
                return true;
            }
            return false;
        }
    }
}
