package com.miui.gallery.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewStub;
import android.widget.CheckBox;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.common.linear.CommonWrapperCheckableLinearAlbumItemModel;
import com.miui.gallery.adapter.itemmodel.common.linear.CommonWrapperLinearAlbumItemModel;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;

/* loaded from: classes2.dex */
public class CommonWrapperCheckableLinearItemLayout extends ConstraintLayout implements Checkable {
    public boolean isAlreadyRunEnterAnim;
    public boolean isAlreadyRunExitAnim;
    public boolean isCheckable;
    public CheckBox mCheckBox;
    public ViewStub mCheckBoxContainerStub;
    public final ValueAnimator mEnterAnimator;
    public final Animator.AnimatorListener mEnterListener;
    public final ValueAnimator mExitAnimator;
    public final Animator.AnimatorListener mExitListener;

    /* loaded from: classes2.dex */
    public interface OnChangeCheckableStatusCallback {
        void onChangeCheckableStatus(ConstraintLayout constraintLayout, boolean z);
    }

    public CommonWrapperCheckableLinearItemLayout(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, -1);
    }

    public CommonWrapperCheckableLinearItemLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mExitListener = new AnimatorListenerAdapter() { // from class: com.miui.gallery.ui.CommonWrapperCheckableLinearItemLayout.1
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                CommonWrapperCheckableLinearItemLayout.this.mCheckBox.setVisibility(8);
                CommonWrapperCheckableLinearItemLayout.this.dispatchCheckableEventStatus(false);
                CommonWrapperCheckableLinearItemLayout.this.isAlreadyRunExitAnim = false;
            }
        };
        this.mEnterListener = new AnimatorListenerAdapter() { // from class: com.miui.gallery.ui.CommonWrapperCheckableLinearItemLayout.2
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                CommonWrapperCheckableLinearItemLayout.this.isAlreadyRunEnterAnim = false;
            }
        };
        this.mEnterAnimator = AlbumPageConfig.AlbumTabPage.getCurrentAlbumConfig().getEnterAnimator();
        this.mExitAnimator = AlbumPageConfig.AlbumTabPage.getCurrentAlbumConfig().getExitAnimator();
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        ViewStub viewStub = (ViewStub) findViewById(R.id.vsCheckBox);
        this.mCheckBoxContainerStub = viewStub;
        viewStub.setLayoutResource(R.layout.album_common_vertical_check_box_layout);
    }

    @Override // com.miui.gallery.ui.Checkable
    public void setCheckable(boolean z) {
        this.isCheckable = z;
        refreshCheckStatus();
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

    public final void checkViewConstraint() {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(this);
        int id = this.mCheckBox.getId();
        int id2 = getId();
        constraintSet.constrainWidth(R.id.album_common_wrapper_main, 0);
        constraintSet.connect(R.id.album_common_wrapper_main, 6, id2, 6, 0);
        constraintSet.connect(R.id.album_common_wrapper_main, 7, id, 6, 0);
        constraintSet.connect(id, 7, id2, 7, 0);
        constraintSet.connect(id, 3, id2, 3);
        constraintSet.connect(id, 4, id2, 4);
        constraintSet.applyTo(this);
        this.mCheckBox.getLayoutParams().resolveLayoutDirection(getLayoutDirection());
    }

    @Override // android.view.View, android.view.ViewParent
    public boolean canResolveLayoutDirection() {
        return super.canResolveLayoutDirection();
    }

    public final void checkStubInflate() {
        ViewStub viewStub = this.mCheckBoxContainerStub;
        if (viewStub == null) {
            return;
        }
        this.mCheckBox = (CheckBox) viewStub.inflate();
        this.mCheckBoxContainerStub = null;
        checkViewConstraint();
    }

    public void setCheckboxVisible(boolean z) {
        CheckBox checkBox = this.mCheckBox;
        if (checkBox == null) {
            return;
        }
        if (z) {
            if (this.isAlreadyRunEnterAnim || checkBox.getVisibility() == 0) {
                return;
            }
            this.mCheckBox.setVisibility(0);
            dispatchCheckableEventStatus(true);
            this.isAlreadyRunEnterAnim = true;
            this.mEnterAnimator.setTarget(this.mCheckBox);
            this.mEnterAnimator.addListener(this.mEnterListener);
            this.mEnterAnimator.start();
        } else if (checkBox.getVisibility() == 8) {
        } else {
            if (this.mEnterAnimator.isRunning()) {
                this.mEnterAnimator.end();
            }
            if (this.mCheckBox.getAlpha() == 0.0f || this.mExitAnimator.isRunning()) {
                return;
            }
            this.isAlreadyRunExitAnim = true;
            this.mExitAnimator.setTarget(this.mCheckBox);
            this.mExitAnimator.addListener(this.mExitListener);
            this.mExitAnimator.start();
        }
    }

    public final void dispatchCheckableEventStatus(boolean z) {
        OnChangeCheckableStatusCallback findCallback = findCallback(getTag(R.id.tag_item_model));
        if (findCallback != null) {
            findCallback.onChangeCheckableStatus(this, z);
        }
    }

    public final OnChangeCheckableStatusCallback findCallback(Object obj) {
        if (obj instanceof CommonWrapperCheckableLinearAlbumItemModel) {
            return findCallback(((CommonWrapperCheckableLinearAlbumItemModel) obj).getChildModel());
        }
        if (obj instanceof CommonWrapperLinearAlbumItemModel) {
            return findCallback(((CommonWrapperLinearAlbumItemModel) obj).getChildModel());
        }
        if (!(obj instanceof OnChangeCheckableStatusCallback)) {
            return null;
        }
        return (OnChangeCheckableStatusCallback) obj;
    }
}
