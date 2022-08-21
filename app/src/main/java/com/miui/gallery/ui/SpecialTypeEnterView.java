package com.miui.gallery.ui;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.asynclayoutinflater.view.GalleryAsyncLayoutInflater;
import androidx.asynclayoutinflater.view.OnInflateFinishedListener;
import androidx.core.view.OnApplyWindowInsetsListener;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.miui.gallery.R;
import com.miui.gallery.magic.util.ResourceUtil;
import com.miui.gallery.ui.PhotoPagerHelper;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.BaseScreenUtil;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.RecyclerLayoutCache;
import com.miui.gallery.util.ResourceUtils;
import com.miui.gallery.util.SpecialTypeMediaUtils;
import com.miui.gallery.util.VideoPlayerCompat;
import com.nexstreaming.nexeditorsdk.nexClip;
import java.util.List;
import miuix.view.animation.CubicEaseOutInterpolator;

/* loaded from: classes2.dex */
public class SpecialTypeEnterView {
    public View mCommonEnterView;
    public Animation mEnterHideAnim;
    public Animation mEnterShowAnim;
    public PhotoPagerHelper.OnSpecialTypeEnterListener mListener;
    public int mMarginBottom;
    public int mSystemWindowInsetBottom;

    public static /* synthetic */ void $r8$lambda$DR0QFo0wqCk8PwTXlm1htSmQgFM(SpecialTypeEnterView specialTypeEnterView, List list, View view) {
        specialTypeEnterView.lambda$update$1(list, view);
    }

    public static /* synthetic */ void $r8$lambda$PQmLG_NTakOKDLLTSFnzv4jwHQQ(RecyclerLayoutCache recyclerLayoutCache, View view, int i, ViewGroup viewGroup) {
        recyclerLayoutCache.put(view);
    }

    public static /* synthetic */ void $r8$lambda$UQmyW0enpYpL7vZLavc68Jb8tqE(SpecialTypeEnterView specialTypeEnterView, List list, View view) {
        specialTypeEnterView.lambda$update$3(list, view);
    }

    public static /* synthetic */ WindowInsetsCompat $r8$lambda$YmzWpy3meBhh3GUxOe5dTjSxCKM(SpecialTypeEnterView specialTypeEnterView, View view, WindowInsetsCompat windowInsetsCompat) {
        return specialTypeEnterView.lambda$setOnApplyWindowInsetsListener$0(view, windowInsetsCompat);
    }

    public static /* synthetic */ void $r8$lambda$Zee1pWODn4n4TXe30H6GAGKUzD4(SpecialTypeEnterView specialTypeEnterView, List list, View view) {
        specialTypeEnterView.lambda$update$2(list, view);
    }

    public static /* synthetic */ void $r8$lambda$ijQyhk5eZ973gF55bztKaYcNNbc(SpecialTypeEnterView specialTypeEnterView, List list, View view) {
        specialTypeEnterView.lambda$update$4(list, view);
    }

    public static /* synthetic */ void lambda$buildCache$6(View view, int i, ViewGroup viewGroup) {
    }

    public SpecialTypeEnterView(ViewGroup viewGroup, int i, PhotoPagerHelper.OnSpecialTypeEnterListener onSpecialTypeEnterListener) {
        this(viewGroup, LayoutInflater.from(viewGroup.getContext()).inflate(i, (ViewGroup) null, false), onSpecialTypeEnterListener);
    }

    public SpecialTypeEnterView(ViewGroup viewGroup, View view, PhotoPagerHelper.OnSpecialTypeEnterListener onSpecialTypeEnterListener) {
        this(view, onSpecialTypeEnterListener);
        viewGroup.addView(view);
    }

    public SpecialTypeEnterView(View view, PhotoPagerHelper.OnSpecialTypeEnterListener onSpecialTypeEnterListener) {
        this.mCommonEnterView = view;
        view.setVisibility(8);
        setListener(onSpecialTypeEnterListener);
    }

    public void setListener(PhotoPagerHelper.OnSpecialTypeEnterListener onSpecialTypeEnterListener) {
        this.mListener = onSpecialTypeEnterListener;
    }

    public final void setOnApplyWindowInsetsListener() {
        ViewCompat.setOnApplyWindowInsetsListener(this.mCommonEnterView, new OnApplyWindowInsetsListener() { // from class: com.miui.gallery.ui.SpecialTypeEnterView$$ExternalSyntheticLambda6
            @Override // androidx.core.view.OnApplyWindowInsetsListener
            public final WindowInsetsCompat onApplyWindowInsets(View view, WindowInsetsCompat windowInsetsCompat) {
                return SpecialTypeEnterView.$r8$lambda$YmzWpy3meBhh3GUxOe5dTjSxCKM(SpecialTypeEnterView.this, view, windowInsetsCompat);
            }
        });
    }

    public /* synthetic */ WindowInsetsCompat lambda$setOnApplyWindowInsetsListener$0(View view, WindowInsetsCompat windowInsetsCompat) {
        int systemWindowInsetBottom = com.miui.gallery.compat.view.ViewCompat.getSystemWindowInsetBottom(this.mCommonEnterView);
        if (systemWindowInsetBottom != this.mSystemWindowInsetBottom) {
            this.mSystemWindowInsetBottom = systemWindowInsetBottom;
            if (view == null) {
                return windowInsetsCompat;
            }
            int rotation = ((WindowManager) view.getContext().getSystemService("window")).getDefaultDisplay().getRotation();
            updatePosition(rotation == 1 ? 90 : rotation == 3 ? nexClip.kClip_Rotate_270 : 0);
        }
        return windowInsetsCompat;
    }

    public void updatePosition(int i) {
        View view = this.mCommonEnterView;
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        if (layoutParams != null) {
            Configuration configuration = view.getResources().getConfiguration();
            boolean isPortMode = ResourceUtils.isPortMode(configuration);
            boolean z = true;
            int i2 = 0;
            boolean z2 = configuration.screenHeightDp <= 500 && configuration.screenWidthDp <= 500;
            if (isPortMode || z2 || (BaseBuildUtil.isFoldableDevice() && configuration.smallestScreenWidthDp > BaseBuildUtil.BIG_HORIZONTAL_WINDOW_STANDARD)) {
                layoutParams.removeRule(10);
                layoutParams.removeRule(21);
                layoutParams.addRule(14);
                layoutParams.addRule(12);
                layoutParams.bottomMargin = getInsetBottom(view) + this.mMarginBottom;
                layoutParams.setMarginEnd(0);
                layoutParams.setMarginStart(0);
                layoutParams.width = -1;
            } else {
                layoutParams.removeRule(14);
                layoutParams.removeRule(12);
                layoutParams.addRule(10);
                layoutParams.addRule(21);
                layoutParams.topMargin = view.getResources().getDimensionPixelSize(R.dimen.specify_type_video_enter_landscape_margin_top) + MiscUtil.getNotchHeight(view.getContext());
                int dimensionPixelSize = view.getResources().getDimensionPixelSize(R.dimen.specify_type_video_enter_landscape_margin_right);
                int dimension = (int) view.getResources().getDimension(R.dimen.photo_page_top_menu_left_padding);
                if (i != 90) {
                    dimension = 0;
                }
                View rootView = view.getRootView();
                if (rootView == null || (com.miui.gallery.compat.view.ViewCompat.getSystemWindowInsetLeft(rootView) <= 0 && com.miui.gallery.compat.view.ViewCompat.getSystemWindowInsetRight(rootView) <= 0)) {
                    z = false;
                }
                if (!BaseScreenUtil.isFullScreenGestureNav(view.getContext()) && z) {
                    i2 = dimension;
                }
                int i3 = dimensionPixelSize + i2;
                layoutParams.setMarginEnd(i3);
                layoutParams.setMarginStart(i3);
                layoutParams.width = -2;
            }
            view.setLayoutParams(layoutParams);
        }
    }

    public final int getInsetBottom(View view) {
        return com.miui.gallery.compat.view.ViewCompat.getSystemWindowInsetBottom(view);
    }

    public void update(ViewGroup viewGroup, boolean z, final List<SpecialTypeMediaUtils.SpecialEnterIconAndText> list) {
        if (list.size() <= 0) {
            return;
        }
        View view = this.mCommonEnterView;
        View findViewById = view.findViewById(R.id.special_type_enter_middle);
        ImageView imageView = (ImageView) view.findViewById(R.id.special_type_enter_right);
        int i = 0;
        if (findViewById instanceof ImageView) {
            ((ImageView) findViewById).setImageResource(list.get(0).getIconId());
            if (list.get(0).getSpecialEnterFlag() != 0) {
                findViewById.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.SpecialTypeEnterView$$ExternalSyntheticLambda0
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        SpecialTypeEnterView.$r8$lambda$DR0QFo0wqCk8PwTXlm1htSmQgFM(SpecialTypeEnterView.this, list, view2);
                    }
                });
            }
            if (list.size() == 2) {
                imageView.setImageResource(list.get(1).getIconId());
                if (list.get(1).getSpecialEnterFlag() != 0) {
                    imageView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.SpecialTypeEnterView$$ExternalSyntheticLambda2
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            SpecialTypeEnterView.$r8$lambda$Zee1pWODn4n4TXe30H6GAGKUzD4(SpecialTypeEnterView.this, list, view2);
                        }
                    });
                }
                imageView.setVisibility(0);
            } else {
                imageView.setVisibility(8);
            }
        } else if (findViewById instanceof LinearLayout) {
            ((TextView) view.findViewById(R.id.text)).setText(list.get(0).getTextId());
            ((ImageView) view.findViewById(R.id.icon)).setImageResource(list.get(0).getIconId());
            if (list.get(0).getSpecialEnterFlag() != 0) {
                findViewById.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.SpecialTypeEnterView$$ExternalSyntheticLambda1
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view2) {
                        SpecialTypeEnterView.$r8$lambda$UQmyW0enpYpL7vZLavc68Jb8tqE(SpecialTypeEnterView.this, list, view2);
                    }
                });
            }
            if (list.size() == 2) {
                imageView.setImageResource(list.get(1).getIconId());
                imageView.setVisibility(0);
                if (list.get(1).getSpecialEnterFlag() != 0) {
                    imageView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.ui.SpecialTypeEnterView$$ExternalSyntheticLambda3
                        @Override // android.view.View.OnClickListener
                        public final void onClick(View view2) {
                            SpecialTypeEnterView.$r8$lambda$ijQyhk5eZ973gF55bztKaYcNNbc(SpecialTypeEnterView.this, list, view2);
                        }
                    });
                }
            } else {
                imageView.setVisibility(8);
            }
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = -1;
            view.setLayoutParams(layoutParams);
        }
        view.setContentDescription(ResourceUtil.getString(list.get(0).getTextId()));
        int i2 = R.dimen.specify_type_common_enter_margin_bottom;
        if (z && VideoPlayerCompat.isSupportNewVideoPlayer()) {
            i2 = R.dimen.specify_type_video_enter_margin_bottom;
        }
        this.mMarginBottom = view.getResources().getDimensionPixelSize(i2);
        if (view.getParent() == null) {
            viewGroup.addView(view);
        }
        setOnApplyWindowInsetsListener();
        int rotation = ((WindowManager) view.getContext().getSystemService("window")).getDefaultDisplay().getRotation();
        if (rotation == 1) {
            i = 90;
        } else if (rotation == 3) {
            i = nexClip.kClip_Rotate_270;
        }
        updatePosition(i);
    }

    public /* synthetic */ void lambda$update$1(List list, View view) {
        PhotoPagerHelper.OnSpecialTypeEnterListener onSpecialTypeEnterListener = this.mListener;
        if (onSpecialTypeEnterListener != null) {
            onSpecialTypeEnterListener.onEntersClick(null, ((SpecialTypeMediaUtils.SpecialEnterIconAndText) list.get(0)).getSpecialEnterFlag());
        }
    }

    public /* synthetic */ void lambda$update$2(List list, View view) {
        PhotoPagerHelper.OnSpecialTypeEnterListener onSpecialTypeEnterListener = this.mListener;
        if (onSpecialTypeEnterListener != null) {
            onSpecialTypeEnterListener.onEntersClick(null, ((SpecialTypeMediaUtils.SpecialEnterIconAndText) list.get(1)).getSpecialEnterFlag());
        }
    }

    public /* synthetic */ void lambda$update$3(List list, View view) {
        PhotoPagerHelper.OnSpecialTypeEnterListener onSpecialTypeEnterListener = this.mListener;
        if (onSpecialTypeEnterListener != null) {
            onSpecialTypeEnterListener.onEntersClick(null, ((SpecialTypeMediaUtils.SpecialEnterIconAndText) list.get(0)).getSpecialEnterFlag());
        }
    }

    public /* synthetic */ void lambda$update$4(List list, View view) {
        PhotoPagerHelper.OnSpecialTypeEnterListener onSpecialTypeEnterListener = this.mListener;
        if (onSpecialTypeEnterListener != null) {
            onSpecialTypeEnterListener.onEntersClick(null, ((SpecialTypeMediaUtils.SpecialEnterIconAndText) list.get(1)).getSpecialEnterFlag());
        }
    }

    public void setCommonEnterClickListener(View.OnClickListener onClickListener) {
        View view = this.mCommonEnterView;
        if (view != null) {
            view.setOnClickListener(onClickListener);
        }
    }

    public void show(boolean z) {
        View view = this.mCommonEnterView;
        if (view.getVisibility() == 8 || BaseMiscUtil.floatEquals(0.0f, view.getAlpha())) {
            if (z) {
                if (this.mEnterShowAnim == null) {
                    this.mEnterShowAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.special_type_enter_in);
                }
                view.startAnimation(this.mEnterShowAnim);
            }
            view.setAlpha(1.0f);
            view.setVisibility(0);
        }
    }

    public void hide(boolean z) {
        View view = this.mCommonEnterView;
        if (view.getVisibility() == 0) {
            if (z) {
                if (this.mEnterHideAnim == null) {
                    this.mEnterHideAnim = AnimationUtils.loadAnimation(view.getContext(), R.anim.special_type_enter_out);
                }
                view.startAnimation(this.mEnterHideAnim);
            }
            view.setVisibility(8);
        }
    }

    public void setVisible(boolean z) {
        this.mCommonEnterView.setVisibility(z ? 0 : 8);
    }

    public void startEnterAlphaAnim(boolean z) {
        startViewAlphaAnim(this.mCommonEnterView, z);
    }

    public View getView() {
        return this.mCommonEnterView;
    }

    public final void startViewAlphaAnim(View view, boolean z) {
        if (view == null || view.getVisibility() != 0) {
            return;
        }
        view.animate().alpha(z ? 1.0f : 0.0f).setDuration(100L).setInterpolator(new CubicEaseOutInterpolator()).start();
    }

    public static RecyclerLayoutCache buildCache(Context context) {
        if (context == null) {
            return null;
        }
        final RecyclerLayoutCache recyclerLayoutCache = new RecyclerLayoutCache(5);
        RelativeLayout relativeLayout = new RelativeLayout(context);
        for (int i = 0; i < 5; i++) {
            new GalleryAsyncLayoutInflater(context).inflate(R.layout.special_type_enter_layout, relativeLayout, new OnInflateFinishedListener() { // from class: com.miui.gallery.ui.SpecialTypeEnterView$$ExternalSyntheticLambda4
                @Override // androidx.asynclayoutinflater.view.OnInflateFinishedListener
                public final void onInflateFinished(View view, int i2, ViewGroup viewGroup) {
                    SpecialTypeEnterView.$r8$lambda$PQmLG_NTakOKDLLTSFnzv4jwHQQ(RecyclerLayoutCache.this, view, i2, viewGroup);
                }
            }, SpecialTypeEnterView$$ExternalSyntheticLambda5.INSTANCE);
        }
        return recyclerLayoutCache;
    }
}
