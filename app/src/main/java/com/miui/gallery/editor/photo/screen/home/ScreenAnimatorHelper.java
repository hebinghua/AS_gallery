package com.miui.gallery.editor.photo.screen.home;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;
import com.android.internal.WindowCompat;
import com.miui.gallery.R;
import com.miui.gallery.ui.ShareStateRouter;
import com.miui.gallery.util.BackgroundServiceHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import miuix.view.animation.QuarticEaseInInterpolator;
import miuix.view.animation.QuarticEaseInOutInterpolator;
import miuix.view.animation.QuarticEaseOutInterpolator;

/* loaded from: classes2.dex */
public class ScreenAnimatorHelper {
    public int EDIT_BG_COLOR;
    public int SHARE_BG_COLOR;
    public ScreenEditorActivity mActivity;
    public AnimatorViewCallback mAnimatorViewCallback;
    public Resources mResources;
    public int mShellExtraHeight;
    public int[] mThumbnailRect = {0, 0, 0, 0};
    public List<AnimatorSet> mAnimatorSets = new CopyOnWriteArrayList();
    public ThumbnailAnimatorCallback mThumbnailAnimatorCallback = new ThumbnailAnimatorCallback() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenAnimatorHelper.1
        {
            ScreenAnimatorHelper.this = this;
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ThumbnailAnimatorCallback
        public void onPrepareAnimatorStart() {
            ScreenAnimatorHelper.this.mActivity.configEditModeView();
            ScreenAnimatorHelper.this.mAnimatorViewCallback.getEditBottomLine().setGuidelineEnd(0);
            ScreenAnimatorHelper.this.mAnimatorViewCallback.getEditTopLine().setGuidelineBegin(0);
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ThumbnailAnimatorCallback
        public void onAnimationStart() {
            ScreenAnimatorHelper.this.startQuitThumbnailService();
            ScreenAnimatorHelper.this.startEnterAnimator();
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ThumbnailAnimatorCallback
        public void onAnimationUpdate(float f) {
            ScreenAnimatorHelper.this.mAnimatorViewCallback.getEditBottomLine().setGuidelineEnd((int) (ScreenAnimatorHelper.this.mResources.getDimensionPixelSize(R.dimen.screen_editor_edit_mode_bottom_guideline_end) * f));
            ScreenAnimatorHelper.this.mAnimatorViewCallback.getEditTopLine().setGuidelineBegin((int) ((ScreenAnimatorHelper.this.mResources.getDimensionPixelSize(R.dimen.screen_editor_top_height) + ScreenAnimatorHelper.this.mShellExtraHeight) * f));
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ThumbnailAnimatorCallback
        public int getThumbnailStartTop() {
            return ScreenAnimatorHelper.this.mThumbnailRect[1];
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ThumbnailAnimatorCallback
        public int getThumbnailStartLeft() {
            return ScreenAnimatorHelper.this.mThumbnailRect[0];
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ThumbnailAnimatorCallback
        public int[] getThumbnailRect() {
            return ScreenAnimatorHelper.this.mThumbnailRect;
        }
    };
    public ThumbnailAnimatorCallback mShareViewAnimatorCallback = new ThumbnailAnimatorCallback() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenAnimatorHelper.2
        {
            ScreenAnimatorHelper.this = this;
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ThumbnailAnimatorCallback
        public void onPrepareAnimatorStart() {
            ScreenAnimatorHelper.this.mActivity.configShareModeView();
            ScreenAnimatorHelper.this.mAnimatorViewCallback.getShareBottomLine().setGuidelineEnd(0);
            ScreenAnimatorHelper.this.mAnimatorViewCallback.getShareTopLine().setGuidelineBegin(0);
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ThumbnailAnimatorCallback
        public void onAnimationStart() {
            ScreenAnimatorHelper.this.startQuitThumbnailService();
            ScreenAnimatorHelper.this.startEnterAnimator();
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ThumbnailAnimatorCallback
        public void onAnimationUpdate(float f) {
            ScreenAnimatorHelper.this.mAnimatorViewCallback.getShareBottomLine().setGuidelineEnd((int) (ScreenAnimatorHelper.this.getShareModeBottomGuidelineEnd() * f));
            ScreenAnimatorHelper.this.mAnimatorViewCallback.getShareTopLine().setGuidelineBegin((int) (ScreenAnimatorHelper.this.mResources.getDimensionPixelSize(R.dimen.screen_editor_share_mode_top_guideline_begin) * f));
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ThumbnailAnimatorCallback
        public int getThumbnailStartTop() {
            return ScreenAnimatorHelper.this.mThumbnailRect[1];
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ThumbnailAnimatorCallback
        public int getThumbnailStartLeft() {
            return ScreenAnimatorHelper.this.mThumbnailRect[0];
        }

        @Override // com.miui.gallery.editor.photo.screen.home.ThumbnailAnimatorCallback
        public int[] getThumbnailRect() {
            return ScreenAnimatorHelper.this.mThumbnailRect;
        }
    };

    /* loaded from: classes2.dex */
    public interface AnimatorViewCallback {
        View getBottomLayoutView();

        Guideline getCommonChangeLine();

        Guideline getEditBottomLine();

        Guideline getEditTopLine();

        View getEditTopView();

        View getScreenEditorBgView();

        Guideline getShareBottomLine();

        Guideline getShareTopLine();

        View getShareTopView();
    }

    public static /* synthetic */ void $r8$lambda$8axGIMp2LYfJOGTiv8nV7WJEaGY(ScreenAnimatorHelper screenAnimatorHelper, ValueAnimator valueAnimator) {
        screenAnimatorHelper.lambda$startSharePageEnterAnimator$0(valueAnimator);
    }

    /* renamed from: $r8$lambda$93wKXxcKX1dLAAAvCWpmpNcKw-8 */
    public static /* synthetic */ void m924$r8$lambda$93wKXxcKX1dLAAAvCWpmpNcKw8(ScreenAnimatorHelper screenAnimatorHelper, ValueAnimator valueAnimator) {
        screenAnimatorHelper.lambda$startSharePageExitAnimator$8(valueAnimator);
    }

    public static /* synthetic */ void $r8$lambda$9JG7xjGPsmn_jFjfao8Ro8TJ9e4(ScreenAnimatorHelper screenAnimatorHelper, ValueAnimator valueAnimator) {
        screenAnimatorHelper.lambda$startSharePageExitAnimator$6(valueAnimator);
    }

    public static /* synthetic */ void $r8$lambda$Cng1yqiA3uAJ_K9KRztnp7CFBv8(ScreenAnimatorHelper screenAnimatorHelper, ValueAnimator valueAnimator) {
        screenAnimatorHelper.lambda$startSharePageEnterAnimator$3(valueAnimator);
    }

    public static /* synthetic */ void $r8$lambda$EyVAzhZlWFSxWQm5uMQESySdv0M(ScreenAnimatorHelper screenAnimatorHelper, ValueAnimator valueAnimator) {
        screenAnimatorHelper.lambda$startEditPageEnterAnimator$4(valueAnimator);
    }

    public static /* synthetic */ void $r8$lambda$N9RxLFw1BqOrmh1E6kDweD4anGM(ScreenAnimatorHelper screenAnimatorHelper, ValueAnimator valueAnimator) {
        screenAnimatorHelper.lambda$startSharePageExitAnimator$7(valueAnimator);
    }

    public static /* synthetic */ void $r8$lambda$TWfHJVz9m3xm57tdf7RD0Ambukw(ScreenAnimatorHelper screenAnimatorHelper, ValueAnimator valueAnimator) {
        screenAnimatorHelper.lambda$startEditPageExitAnimator$9(valueAnimator);
    }

    public static /* synthetic */ void $r8$lambda$_L2kNHP2Xatg_IS8PmQcvhw44r4(ScreenAnimatorHelper screenAnimatorHelper, ValueAnimator valueAnimator) {
        screenAnimatorHelper.lambda$startSharePageEnterAnimator$1(valueAnimator);
    }

    /* renamed from: $r8$lambda$vdR5sMe4-ksvRj8NRcnhcoU00Q0 */
    public static /* synthetic */ void m925$r8$lambda$vdR5sMe4ksvRj8NRcnhcoU00Q0(ScreenAnimatorHelper screenAnimatorHelper, ValueAnimator valueAnimator) {
        screenAnimatorHelper.lambda$startSharePageEnterAnimator$2(valueAnimator);
    }

    public ScreenAnimatorHelper(ScreenEditorActivity screenEditorActivity, int[] iArr, AnimatorViewCallback animatorViewCallback) {
        this.mActivity = screenEditorActivity;
        this.mResources = screenEditorActivity.getResources();
        this.mAnimatorViewCallback = animatorViewCallback;
        setThumbnailRect(iArr);
        this.SHARE_BG_COLOR = this.mResources.getColor(R.color.screen_editor_view_background);
        this.EDIT_BG_COLOR = this.mResources.getColor(R.color.screen_editor_view_background);
        this.mShellExtraHeight = this.mActivity.getScreenShellExecutor().isShellFuncEnable() ? this.mResources.getDimensionPixelSize(R.dimen.screen_editor_top_height_extra_shell) : 0;
    }

    public final void setThumbnailRect(int[] iArr) {
        for (int i = 0; iArr != null && i < iArr.length; i++) {
            this.mThumbnailRect[i] = iArr[i];
        }
    }

    public void setViewsAlpha(float f, View... viewArr) {
        for (View view : viewArr) {
            view.setAlpha(f);
        }
    }

    public void startEnterAnimator() {
        final AnimatorSet animatorSet = new AnimatorSet();
        PropertyValuesHolder ofFloat = PropertyValuesHolder.ofFloat("alpha", 0.0f, 1.0f);
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(this.mActivity.isSharePage() ? this.mAnimatorViewCallback.getShareTopView() : this.mAnimatorViewCallback.getEditTopView(), ofFloat);
        ofPropertyValuesHolder.setDuration(500L);
        ofPropertyValuesHolder.setInterpolator(new QuarticEaseOutInterpolator());
        ObjectAnimator ofPropertyValuesHolder2 = ObjectAnimator.ofPropertyValuesHolder(this.mAnimatorViewCallback.getScreenEditorBgView(), ofFloat);
        ofPropertyValuesHolder2.setDuration(500L);
        ofPropertyValuesHolder2.setInterpolator(new QuarticEaseOutInterpolator());
        ObjectAnimator ofPropertyValuesHolder3 = ObjectAnimator.ofPropertyValuesHolder(this.mAnimatorViewCallback.getBottomLayoutView(), PropertyValuesHolder.ofFloat("translationY", this.mAnimatorViewCallback.getBottomLayoutView().getHeight(), 0.0f), ofFloat);
        ofPropertyValuesHolder3.setStartDelay(150L);
        ofPropertyValuesHolder3.setDuration(500L);
        ofPropertyValuesHolder3.setInterpolator(new QuarticEaseOutInterpolator());
        animatorSet.play(ofPropertyValuesHolder3).with(ofPropertyValuesHolder).with(ofPropertyValuesHolder2);
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenAnimatorHelper.3
            {
                ScreenAnimatorHelper.this = this;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ScreenAnimatorHelper.this.mAnimatorSets.remove(animatorSet);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                ScreenAnimatorHelper.this.mAnimatorSets.add(animatorSet);
            }
        });
        animatorSet.start();
    }

    public void startSharePageEnterAnimator(int i, int i2) {
        final AnimatorSet animatorSet = new AnimatorSet();
        int topNotchHeight = WindowCompat.isNotch(this.mActivity) ? WindowCompat.getTopNotchHeight(this.mActivity) : 0;
        int dimensionPixelSize = this.mResources.getDimensionPixelSize(R.dimen.screen_editor_share_mode_top_guideline_begin);
        int shareModeBottomGuidelineEnd = getShareModeBottomGuidelineEnd();
        int screenEditorBaseGuidelineEnd = getScreenEditorBaseGuidelineEnd();
        int dimensionPixelSize2 = (((this.mResources.getDimensionPixelSize(R.dimen.screen_editor_top_height) + this.mShellExtraHeight) + topNotchHeight) + this.mResources.getDimensionPixelSize(R.dimen.screen_editor_view_top_margin)) - i;
        int dimensionPixelSize3 = (this.mResources.getDimensionPixelSize(R.dimen.screen_editor_edit_mode_bottom_guideline_end) + this.mResources.getDimensionPixelSize(R.dimen.screen_editor_view_bottom_margin)) - i2;
        ValueAnimator ofInt = ValueAnimator.ofInt(dimensionPixelSize2, dimensionPixelSize);
        long j = 450;
        ofInt.setDuration(j);
        ofInt.setInterpolator(new QuarticEaseInOutInterpolator());
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenAnimatorHelper$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ScreenAnimatorHelper.$r8$lambda$8axGIMp2LYfJOGTiv8nV7WJEaGY(ScreenAnimatorHelper.this, valueAnimator);
            }
        });
        ValueAnimator ofInt2 = ValueAnimator.ofInt(dimensionPixelSize3, shareModeBottomGuidelineEnd);
        ofInt2.setDuration(j);
        ofInt2.setInterpolator(new QuarticEaseInOutInterpolator());
        ofInt2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenAnimatorHelper$$ExternalSyntheticLambda7
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ScreenAnimatorHelper.$r8$lambda$_L2kNHP2Xatg_IS8PmQcvhw44r4(ScreenAnimatorHelper.this, valueAnimator);
            }
        });
        ValueAnimator ofInt3 = ValueAnimator.ofInt(0, screenEditorBaseGuidelineEnd);
        ofInt3.setDuration(j);
        ofInt3.setInterpolator(new QuarticEaseInOutInterpolator());
        ofInt3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenAnimatorHelper$$ExternalSyntheticLambda8
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ScreenAnimatorHelper.m925$r8$lambda$vdR5sMe4ksvRj8NRcnhcoU00Q0(ScreenAnimatorHelper.this, valueAnimator);
            }
        });
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(this.mAnimatorViewCallback.getShareTopView(), PropertyValuesHolder.ofFloat("alpha", 0.0f, 1.0f));
        ofPropertyValuesHolder.setDuration(370L);
        ofPropertyValuesHolder.setInterpolator(new QuarticEaseInOutInterpolator());
        ValueAnimator ofObject = ValueAnimator.ofObject(new ScreenArgbEvaluator(), Integer.valueOf(this.EDIT_BG_COLOR), Integer.valueOf(this.SHARE_BG_COLOR));
        ofObject.setDuration(370L);
        ofObject.setInterpolator(new QuarticEaseInOutInterpolator());
        ofObject.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenAnimatorHelper$$ExternalSyntheticLambda3
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ScreenAnimatorHelper.$r8$lambda$Cng1yqiA3uAJ_K9KRztnp7CFBv8(ScreenAnimatorHelper.this, valueAnimator);
            }
        });
        animatorSet.play(ofInt).with(ofInt2).with(ofInt3).with(ofPropertyValuesHolder).with(ofObject);
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenAnimatorHelper.4
            {
                ScreenAnimatorHelper.this = this;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ScreenAnimatorHelper.this.mAnimatorSets.remove(animatorSet);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                ScreenAnimatorHelper.this.mAnimatorSets.add(animatorSet);
            }
        });
        animatorSet.start();
    }

    public /* synthetic */ void lambda$startSharePageEnterAnimator$0(ValueAnimator valueAnimator) {
        this.mAnimatorViewCallback.getShareTopLine().setGuidelineBegin(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    public /* synthetic */ void lambda$startSharePageEnterAnimator$1(ValueAnimator valueAnimator) {
        this.mAnimatorViewCallback.getShareBottomLine().setGuidelineEnd(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    public /* synthetic */ void lambda$startSharePageEnterAnimator$2(ValueAnimator valueAnimator) {
        this.mAnimatorViewCallback.getCommonChangeLine().setGuidelineEnd(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    public /* synthetic */ void lambda$startSharePageEnterAnimator$3(ValueAnimator valueAnimator) {
        this.mAnimatorViewCallback.getScreenEditorBgView().setBackgroundColor(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    public void startEditPageEnterAnimator() {
        int dimensionPixelSize = this.mResources.getDimensionPixelSize(R.dimen.screen_editor_edit_mode_bottom_guideline_end);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.mAnimatorViewCallback.getBottomLayoutView().getLayoutParams();
        ((ViewGroup.MarginLayoutParams) layoutParams).height = this.mResources.getDimensionPixelSize(R.dimen.screen_editor_edit_mode_bottom_guideline_end);
        layoutParams.topToBottom = R.id.common_base_guide_line;
        this.mAnimatorViewCallback.getBottomLayoutView().setLayoutParams(layoutParams);
        ValueAnimator ofInt = ValueAnimator.ofInt(0, dimensionPixelSize);
        ofInt.setDuration(370L);
        ofInt.setInterpolator(new QuarticEaseOutInterpolator());
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenAnimatorHelper$$ExternalSyntheticLambda4
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ScreenAnimatorHelper.$r8$lambda$EyVAzhZlWFSxWQm5uMQESySdv0M(ScreenAnimatorHelper.this, valueAnimator);
            }
        });
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(this.mAnimatorViewCallback.getEditTopView(), PropertyValuesHolder.ofFloat("alpha", 0.0f, 1.0f));
        ofPropertyValuesHolder.setDuration(370L);
        ofPropertyValuesHolder.setInterpolator(new QuarticEaseInOutInterpolator());
        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(ofInt).with(ofPropertyValuesHolder);
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenAnimatorHelper.5
            {
                ScreenAnimatorHelper.this = this;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ScreenAnimatorHelper.this.mAnimatorSets.remove(animatorSet);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                ScreenAnimatorHelper.this.mAnimatorSets.add(animatorSet);
            }
        });
        animatorSet.start();
    }

    public /* synthetic */ void lambda$startEditPageEnterAnimator$4(ValueAnimator valueAnimator) {
        this.mAnimatorViewCallback.getCommonChangeLine().setGuidelineEnd(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    public void startSharePageExitAnimator(int i, int i2, Animator.AnimatorListener animatorListener) {
        int dimensionPixelSize = this.mResources.getDimensionPixelSize(R.dimen.screen_editor_activity_status_height);
        int dimensionPixelSize2 = this.mResources.getDimensionPixelSize(R.dimen.screen_editor_share_mode_top_guideline_begin);
        int shareModeBottomGuidelineEnd = getShareModeBottomGuidelineEnd();
        int screenEditorBaseGuidelineEnd = getScreenEditorBaseGuidelineEnd();
        ValueAnimator ofInt = ValueAnimator.ofInt(dimensionPixelSize2, (((this.mResources.getDimensionPixelSize(R.dimen.screen_editor_top_height) + this.mShellExtraHeight) + dimensionPixelSize) + this.mResources.getDimensionPixelSize(R.dimen.screen_editor_view_top_margin)) - i);
        long j = 450;
        ofInt.setDuration(j);
        ofInt.setInterpolator(new QuarticEaseInOutInterpolator());
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenAnimatorHelper$$ExternalSyntheticLambda2
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ScreenAnimatorHelper.$r8$lambda$9JG7xjGPsmn_jFjfao8Ro8TJ9e4(ScreenAnimatorHelper.this, valueAnimator);
            }
        });
        ValueAnimator ofInt2 = ValueAnimator.ofInt(shareModeBottomGuidelineEnd, (this.mResources.getDimensionPixelSize(R.dimen.screen_editor_edit_mode_bottom_guideline_end) + this.mResources.getDimensionPixelSize(R.dimen.screen_editor_view_bottom_margin)) - i2);
        ofInt2.setDuration(j);
        ofInt2.setInterpolator(new QuarticEaseInOutInterpolator());
        ofInt2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenAnimatorHelper$$ExternalSyntheticLambda5
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ScreenAnimatorHelper.$r8$lambda$N9RxLFw1BqOrmh1E6kDweD4anGM(ScreenAnimatorHelper.this, valueAnimator);
            }
        });
        ValueAnimator ofInt3 = ValueAnimator.ofInt(screenEditorBaseGuidelineEnd, 0);
        ofInt3.setDuration(j);
        ofInt3.setInterpolator(new QuarticEaseInOutInterpolator());
        ofInt3.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenAnimatorHelper$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ScreenAnimatorHelper.m924$r8$lambda$93wKXxcKX1dLAAAvCWpmpNcKw8(ScreenAnimatorHelper.this, valueAnimator);
            }
        });
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(this.mAnimatorViewCallback.getShareTopView(), PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.0f));
        ofPropertyValuesHolder.setDuration(j);
        ofPropertyValuesHolder.setInterpolator(new QuarticEaseInOutInterpolator());
        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(ofInt).with(ofInt2).with(ofInt3).with(ofPropertyValuesHolder);
        animatorSet.addListener(animatorListener);
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenAnimatorHelper.7
            {
                ScreenAnimatorHelper.this = this;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ScreenAnimatorHelper.this.mAnimatorSets.remove(animatorSet);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                ScreenAnimatorHelper.this.mAnimatorSets.add(animatorSet);
            }
        });
        animatorSet.start();
    }

    public /* synthetic */ void lambda$startSharePageExitAnimator$6(ValueAnimator valueAnimator) {
        this.mAnimatorViewCallback.getShareTopLine().setGuidelineBegin(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    public /* synthetic */ void lambda$startSharePageExitAnimator$7(ValueAnimator valueAnimator) {
        this.mAnimatorViewCallback.getShareBottomLine().setGuidelineEnd(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    public /* synthetic */ void lambda$startSharePageExitAnimator$8(ValueAnimator valueAnimator) {
        this.mAnimatorViewCallback.getCommonChangeLine().setGuidelineEnd(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    public void startEditPageExitAnimator(Animator.AnimatorListener animatorListener) {
        Resources resources = this.mActivity.getResources();
        int dimensionPixelSize = resources.getDimensionPixelSize(R.dimen.screen_editor_edit_mode_bottom_guideline_end);
        this.mAnimatorViewCallback.getCommonChangeLine().setGuidelineEnd(dimensionPixelSize);
        int dimensionPixelSize2 = resources.getDimensionPixelSize(R.dimen.screen_editor_menu_top_guideline_end);
        int dimensionPixelSize3 = resources.getDimensionPixelSize(R.dimen.screen_editor_menu_bottom_guideline_end);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.mAnimatorViewCallback.getBottomLayoutView().getLayoutParams();
        layoutParams.topToBottom = R.id.common_base_guide_line;
        this.mAnimatorViewCallback.getBottomLayoutView().setLayoutParams(layoutParams);
        ValueAnimator ofInt = ValueAnimator.ofInt(dimensionPixelSize, dimensionPixelSize3 - dimensionPixelSize2);
        ofInt.setDuration(180L);
        ofInt.setInterpolator(new QuarticEaseInInterpolator());
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenAnimatorHelper$$ExternalSyntheticLambda6
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                ScreenAnimatorHelper.$r8$lambda$TWfHJVz9m3xm57tdf7RD0Ambukw(ScreenAnimatorHelper.this, valueAnimator);
            }
        });
        ofInt.addListener(animatorListener);
        ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(this.mAnimatorViewCallback.getEditTopView(), PropertyValuesHolder.ofFloat("alpha", 1.0f, 0.0f));
        ofPropertyValuesHolder.setDuration(370L);
        ofPropertyValuesHolder.setInterpolator(new QuarticEaseInOutInterpolator());
        final AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(ofInt).with(ofPropertyValuesHolder);
        animatorSet.addListener(new AnimatorListenerAdapter() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenAnimatorHelper.8
            {
                ScreenAnimatorHelper.this = this;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                ScreenAnimatorHelper.this.mAnimatorSets.remove(animatorSet);
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
                ScreenAnimatorHelper.this.mAnimatorSets.add(animatorSet);
            }
        });
        animatorSet.start();
    }

    public /* synthetic */ void lambda$startEditPageExitAnimator$9(ValueAnimator valueAnimator) {
        this.mAnimatorViewCallback.getCommonChangeLine().setGuidelineEnd(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    public int getScreenEditorBaseGuidelineEnd() {
        if (ShareStateRouter.IS_MISHARE_AVAILABLE.get(this.mActivity).booleanValue()) {
            if (ShareStateRouter.SHOULD_UPGRADE_NEARBY_SHARE.get(this.mActivity).booleanValue()) {
                return this.mActivity.getResources().getDimensionPixelSize(R.dimen.screen_editor_base_guideline_end_with_nearby_share);
            }
            return this.mActivity.getResources().getDimensionPixelSize(R.dimen.screen_editor_base_guideline_end);
        }
        return this.mActivity.getResources().getDimensionPixelSize(R.dimen.screen_editor_base_guideline_end_without_mishare);
    }

    public int getShareModeBottomGuidelineEnd() {
        if (ShareStateRouter.IS_MISHARE_AVAILABLE.get(this.mActivity).booleanValue()) {
            if (ShareStateRouter.SHOULD_UPGRADE_NEARBY_SHARE.get(this.mActivity).booleanValue()) {
                return this.mActivity.getResources().getDimensionPixelSize(R.dimen.screen_editor_share_mode_bottom_guideline_end_with_nearby_share);
            }
            return this.mActivity.getResources().getDimensionPixelSize(R.dimen.screen_editor_share_mode_bottom_guideline_end);
        }
        return this.mActivity.getResources().getDimensionPixelSize(R.dimen.screen_editor_share_mode_bottom_guideline_end_without_mishare);
    }

    public final void startQuitThumbnailService() {
        if (this.mActivity != null) {
            DefaultLogger.d("ScreenAnimatorHelper", "start ScreenShotService.");
            Intent intent = new Intent(this.mActivity, ScreenShotService.class);
            intent.putExtra("quit_thumnail", true);
            BackgroundServiceHelper.startForegroundServiceIfNeed(this.mActivity, intent);
        }
    }

    public void release() {
        startQuitThumbnailService();
        for (AnimatorSet animatorSet : this.mAnimatorSets) {
            if (animatorSet != null) {
                animatorSet.cancel();
            }
        }
    }
}
