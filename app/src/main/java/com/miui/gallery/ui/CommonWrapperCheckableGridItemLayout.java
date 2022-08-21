package com.miui.gallery.ui;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.ImageView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.miui.gallery.R;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;

/* loaded from: classes2.dex */
public class CommonWrapperCheckableGridItemLayout extends ConstraintLayout implements Checkable {
    public boolean isAlreadyRunEnterAnim;
    public boolean isAlreadyRunExitAnim;
    public boolean isCheckable;
    public CheckBox mCheckBox;
    public ConstraintLayout mCheckBoxContainer;
    public ViewStub mCheckBoxContainerStub;
    public ImageView mCoverImageView;
    public final ValueAnimator mEnterAnimator;
    public Animator.AnimatorListener mEnterListener;
    public final ValueAnimator mExitAnimator;
    public Animator.AnimatorListener mExitListener;

    public CommonWrapperCheckableGridItemLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, -1);
    }

    public CommonWrapperCheckableGridItemLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mExitListener = new Animator.AnimatorListener() { // from class: com.miui.gallery.ui.CommonWrapperCheckableGridItemLayout.1
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                CommonWrapperCheckableGridItemLayout.this.mCheckBoxContainer.setVisibility(8);
                CommonWrapperCheckableGridItemLayout.this.isAlreadyRunExitAnim = false;
                CommonWrapperCheckableGridItemLayout.this.setChecked(false);
            }
        };
        this.mEnterListener = new Animator.AnimatorListener() { // from class: com.miui.gallery.ui.CommonWrapperCheckableGridItemLayout.2
            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationRepeat(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationStart(Animator animator) {
            }

            @Override // android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                CommonWrapperCheckableGridItemLayout.this.isAlreadyRunEnterAnim = false;
            }
        };
        this.mEnterAnimator = AlbumPageConfig.AlbumTabPage.getCurrentAlbumConfig().getEnterAnimator();
        this.mExitAnimator = AlbumPageConfig.AlbumTabPage.getCurrentAlbumConfig().getExitAnimator();
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        ViewStub viewStub = (ViewStub) findViewById(R.id.album_common_checkable_state_container_stub);
        this.mCheckBoxContainerStub = viewStub;
        viewStub.setLayoutResource(R.layout.album_common_checkable_view);
        this.isAlreadyRunEnterAnim = false;
        this.isAlreadyRunExitAnim = false;
    }

    @Override // com.miui.gallery.ui.Checkable
    public void setCheckable(boolean z) {
        this.isCheckable = z;
        refreshCheckStatus();
    }

    public final void checkStubInflate() {
        ViewStub viewStub = this.mCheckBoxContainerStub;
        if (viewStub == null) {
            return;
        }
        ConstraintLayout constraintLayout = (ConstraintLayout) viewStub.inflate();
        this.mCheckBoxContainer = constraintLayout;
        this.mCheckBox = (CheckBox) constraintLayout.findViewById(16908289);
        checkImageView();
        this.mCheckBoxContainerStub = null;
    }

    public final void checkImageView() {
        if (this.mCoverImageView == null) {
            this.mCoverImageView = (ImageView) findViewById(R.id.album_common_cover);
        }
    }

    @Override // android.widget.Checkable
    public void setChecked(boolean z) {
        if (z) {
            checkStubInflate();
            this.mCheckBox.setChecked(true);
            return;
        }
        CheckBox checkBox = this.mCheckBox;
        if (checkBox == null || this.isAlreadyRunExitAnim) {
            return;
        }
        checkBox.setChecked(false);
    }

    @Override // android.widget.Checkable
    public boolean isChecked() {
        CheckBox checkBox = this.mCheckBox;
        if (checkBox == null) {
            return false;
        }
        return checkBox.isChecked();
    }

    @Override // android.widget.Checkable
    public void toggle() {
        checkStubInflate();
        this.mCheckBox.toggle();
    }

    public void refreshCheckStatus() {
        if (this.isCheckable) {
            if (this.mCheckBox == null) {
                checkStubInflate();
            }
            setChecked(isChecked());
            setCheckboxVisible(true);
        } else if (this.mCheckBox == null) {
        } else {
            setCheckboxVisible(false);
        }
    }

    public void setCheckboxVisible(boolean z) {
        ConstraintLayout constraintLayout = this.mCheckBoxContainer;
        if (constraintLayout == null) {
            return;
        }
        if (z) {
            if (this.isAlreadyRunEnterAnim || constraintLayout.getVisibility() == 0) {
                return;
            }
            this.mCheckBoxContainer.setVisibility(0);
            this.isAlreadyRunEnterAnim = true;
            this.mEnterAnimator.setTarget(this.mCheckBoxContainer);
            this.mEnterAnimator.addListener(this.mEnterListener);
            this.mEnterAnimator.start();
        } else if (constraintLayout.getVisibility() == 8 || this.mCheckBoxContainer.getAlpha() == 0.0f || this.mExitAnimator.isRunning()) {
        } else {
            this.isAlreadyRunExitAnim = true;
            this.mExitAnimator.setTarget(this.mCheckBoxContainer);
            this.mExitAnimator.addListener(this.mExitListener);
            this.mExitAnimator.start();
        }
    }
}
