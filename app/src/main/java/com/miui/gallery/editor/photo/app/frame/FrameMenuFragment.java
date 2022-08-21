package com.miui.gallery.editor.photo.app.frame;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.MenuFragment;
import com.miui.gallery.editor.photo.app.menu.FrameView;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.SdkProvider;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractFrameFragment;
import com.miui.gallery.editor.photo.core.common.model.FrameData;
import com.miui.gallery.editor.photo.utils.ScreenAdaptationHelper;
import com.miui.gallery.editor.photo.widgets.CircularRingView;
import com.miui.gallery.editor.photo.widgets.ColorSelector.ColorSelectorView;
import com.miui.gallery.editor.ui.view.BubbleSeekBar;
import com.miui.gallery.editor.ui.view.EditorBlankDivider;
import com.miui.gallery.editor.utils.EditorOrientationHelper;
import com.miui.gallery.editor.utils.SpringAnimationUtils;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerView;
import java.util.ArrayList;
import java.util.List;
import miuix.animation.base.AnimConfig;
import miuix.animation.utils.EaseManager;

/* loaded from: classes2.dex */
public class FrameMenuFragment extends MenuFragment<AbstractFrameFragment, SdkProvider<FrameData, AbstractFrameFragment>> {
    public boolean mColorPickerIsExpand;
    public ValueAnimator mColorSelectorAnimator;
    public CircularRingView mColorSelectorOrDelete;
    public View.OnClickListener mColorSelectorOrDeleteClickListener;
    public FrameLayout mColorSelectorOrDeleteLayout;
    public ColorSelectorView mColorSelectorView;
    public String[] mColors;
    public int mCurrentColor;
    public FrameAdapter mFrameAdapter;
    public List<FrameData> mFrameDataList;
    public ViewGroup mFrameMenuTopLayout;
    public SparseIntArray mItemStatusArray;
    public ConstraintLayout.LayoutParams mLayoutParams;
    public OnItemClickListener mOnColorItemClickListener;
    public OnItemClickListener mOnItemClickListener;
    public SimpleRecyclerView mRecyclerView;
    public BubbleSeekBar mSeekBar;
    public BubbleSeekBar.ProgressListener mSeekBarChangeListener;

    public static /* synthetic */ void $r8$lambda$xy4JIBMSkBb2HWcusXQjO763bA4(FrameMenuFragment frameMenuFragment, ConstraintLayout.LayoutParams layoutParams, int i, ValueAnimator valueAnimator) {
        frameMenuFragment.lambda$startExpandColorSelectorAnimation$0(layoutParams, i, valueAnimator);
    }

    public FrameMenuFragment() {
        super(Effect.FRAME);
        this.mItemStatusArray = new SparseIntArray();
        this.mOnColorItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.editor.photo.app.frame.FrameMenuFragment.1
            {
                FrameMenuFragment.this = this;
            }

            @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
            public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
                if (FrameMenuFragment.this.mColors[i].equals("#3D3D3D")) {
                    FrameMenuFragment.this.mCurrentColor = -16777216;
                    FrameMenuFragment.this.mColorSelectorOrDelete.setInnerColor(Color.parseColor(FrameMenuFragment.this.mColors[i]));
                } else {
                    FrameMenuFragment frameMenuFragment = FrameMenuFragment.this;
                    frameMenuFragment.mCurrentColor = Color.parseColor(frameMenuFragment.mColors[i]);
                    FrameMenuFragment.this.mColorSelectorOrDelete.setInnerColor(FrameMenuFragment.this.mCurrentColor);
                }
                FrameMenuFragment.this.mColorSelectorView.setSelection(i, true);
                ((AbstractFrameFragment) FrameMenuFragment.this.getRenderFragment()).setFrameColor(FrameMenuFragment.this.mCurrentColor);
                return true;
            }
        };
        this.mColorSelectorOrDeleteClickListener = new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.app.frame.FrameMenuFragment.2
            {
                FrameMenuFragment.this = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (FrameMenuFragment.this.mColorPickerIsExpand) {
                    FrameMenuFragment.this.hideColorPicker();
                    FrameMenuFragment.this.mColorSelectorOrDeleteLayout.setContentDescription(FrameMenuFragment.this.getResources().getString(R.string.frame_color_selector_talkback_show));
                    return;
                }
                FrameMenuFragment.this.startExpandColorSelectorAnimation();
                FrameMenuFragment.this.mColorSelectorOrDeleteLayout.setContentDescription(FrameMenuFragment.this.getResources().getString(R.string.frame_color_selector_talkback_hide));
            }
        };
        this.mOnItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.editor.photo.app.frame.FrameMenuFragment.5
            {
                FrameMenuFragment.this = this;
            }

            @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
            public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
                ((SimpleRecyclerView) recyclerView).setSpringEnabled(false);
                recyclerView.smoothScrollToPosition(i);
                FrameMenuFragment.this.mFrameAdapter.setSelection(i);
                FrameMenuFragment.this.onSelect(i);
                return true;
            }
        };
        this.mSeekBarChangeListener = new BubbleSeekBar.ProgressListener() { // from class: com.miui.gallery.editor.photo.app.frame.FrameMenuFragment.6
            @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
            public void onStartTrackingTouch(BubbleSeekBar bubbleSeekBar) {
            }

            {
                FrameMenuFragment.this = this;
            }

            @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int i) {
                float maxProgress = i / bubbleSeekBar.getMaxProgress();
                if (i == 0 || i == 100) {
                    LinearMotorHelper.performHapticFeedback(bubbleSeekBar, LinearMotorHelper.HAPTIC_MESH_HEAVY);
                }
                ((AbstractFrameFragment) FrameMenuFragment.this.getRenderFragment()).setScaleProgress(maxProgress);
            }

            @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
            public void onStopTrackingTouch(BubbleSeekBar bubbleSeekBar) {
                FrameMenuFragment frameMenuFragment = FrameMenuFragment.this;
                frameMenuFragment.putStatus(frameMenuFragment.mFrameAdapter.getSelection(), (int) bubbleSeekBar.getCurrentProgress());
            }
        };
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return new FrameView(viewGroup.getContext());
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, com.miui.gallery.editor.utils.LayoutOrientationTracker.OnLayoutOrientationChangeListener
    public void onLayoutOrientationChange() {
        super.onLayoutOrientationChange();
        EditorOrientationHelper.copyLayoutParams(new FrameView(getContext()), getView(), true);
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mColorSelectorOrDeleteLayout = (FrameLayout) view.findViewById(R.id.color_selector_or_delete_layout);
        this.mColorSelectorOrDelete = (CircularRingView) view.findViewById(R.id.color_selector_or_delete);
        FolmeUtil.setDefaultTouchAnim(this.mColorSelectorOrDeleteLayout, null, true);
        this.mColorSelectorOrDeleteLayout.setOnClickListener(this.mColorSelectorOrDeleteClickListener);
        this.mColorSelectorOrDeleteLayout.setContentDescription(getResources().getString(R.string.frame_color_selector_talkback_show));
        this.mColorSelectorOrDelete.setViewBgDrawable(getResources().getDrawable(R.drawable.photo_editor_frame_menu_close_icon));
        this.mColorSelectorView = (ColorSelectorView) view.findViewById(R.id.color_selector_view);
        String[] stringArray = getResources().getStringArray(R.array.frame_color_selector_array);
        this.mColors = stringArray;
        this.mColorSelectorView.init(stringArray);
        this.mColorSelectorView.setOnItemClickListener(this.mOnColorItemClickListener);
        this.mRecyclerView = (SimpleRecyclerView) view.findViewById(R.id.recycler_view);
        BubbleSeekBar bubbleSeekBar = (BubbleSeekBar) view.findViewById(R.id.seek_bar);
        this.mSeekBar = bubbleSeekBar;
        bubbleSeekBar.setProgressListener(this.mSeekBarChangeListener);
        this.mFrameDataList = new ArrayList(this.mSdkProvider.list());
        this.mFrameMenuTopLayout = (ViewGroup) view.findViewById(R.id.frame_menu_top_layout);
        int dimension = (int) getResources().getDimension(R.dimen.editor_menu_common_content_item_gap);
        this.mRecyclerView.setEnableItemClickWhileSettling(true);
        FrameAdapter frameAdapter = new FrameAdapter(this.mFrameDataList, getActivity());
        this.mFrameAdapter = frameAdapter;
        frameAdapter.setOnItemClickListener(this.mOnItemClickListener);
        this.mRecyclerView.setAdapter(this.mFrameAdapter);
        this.mRecyclerView.addItemDecoration(new EditorBlankDivider(0, 0, dimension, 0, 0));
        onSelect(0);
    }

    @Override // androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        ScreenAdaptationHelper.updateBSBWidth(getContext(), this.mSeekBar);
        ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.mSeekBar.getLayoutParams();
        this.mLayoutParams = layoutParams;
        layoutParams.setMarginEnd(getResources().getDimensionPixelSize(R.dimen.photo_editor_frame_seek_bar_margin_end));
        this.mSeekBar.setLayoutParams(this.mLayoutParams);
        ConstraintLayout.LayoutParams layoutParams2 = (ConstraintLayout.LayoutParams) this.mColorSelectorOrDeleteLayout.getLayoutParams();
        this.mLayoutParams = layoutParams2;
        layoutParams2.setMarginEnd(getResources().getDimensionPixelSize(R.dimen.photo_editor_frame_color_select_layout_margin_end));
        this.mColorSelectorOrDeleteLayout.setLayoutParams(this.mLayoutParams);
    }

    public final void hideColorPicker() {
        this.mSeekBar.setVisibility(0);
        SpringAnimationUtils.setFractionAnimationWithSpring(new SpringAnimationUtils.SpringTransitionListener() { // from class: com.miui.gallery.editor.photo.app.frame.FrameMenuFragment.3
            {
                FrameMenuFragment.this = this;
            }

            @Override // com.miui.gallery.editor.utils.SpringAnimationUtils.SpringTransitionListener
            public void onUpdate(float f) {
                FrameMenuFragment.this.mSeekBar.setAlpha(f);
            }
        }, new AnimConfig().setEase(EaseManager.getStyle(6, 200.0f)));
        this.mColorSelectorView.setVisibility(4);
        this.mColorPickerIsExpand = false;
        this.mColorSelectorOrDelete.setDrawBitmap(false);
    }

    public final void startExpandColorSelectorAnimation() {
        final int width = this.mColorSelectorView.getWidth();
        final ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) this.mColorSelectorView.getLayoutParams();
        if (this.mColorSelectorAnimator == null) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
            this.mColorSelectorAnimator = ofFloat;
            ofFloat.setDuration(200L);
            this.mColorSelectorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.app.frame.FrameMenuFragment$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    FrameMenuFragment.$r8$lambda$xy4JIBMSkBb2HWcusXQjO763bA4(FrameMenuFragment.this, layoutParams, width, valueAnimator);
                }
            });
            this.mColorSelectorAnimator.addListener(new Animator.AnimatorListener() { // from class: com.miui.gallery.editor.photo.app.frame.FrameMenuFragment.4
                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationRepeat(Animator animator) {
                }

                {
                    FrameMenuFragment.this = this;
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationStart(Animator animator) {
                    layoutParams.startToStart = -1;
                    FrameMenuFragment.this.mColorSelectorView.setVisibility(0);
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    FrameMenuFragment.this.mColorPickerIsExpand = true;
                    layoutParams.startToStart = 0;
                    FrameMenuFragment.this.mSeekBar.setVisibility(8);
                    FrameMenuFragment.this.mColorSelectorOrDelete.setDrawBitmap(true);
                }

                @Override // android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    FrameMenuFragment.this.mColorPickerIsExpand = false;
                    layoutParams.startToStart = 0;
                    FrameMenuFragment.this.mColorSelectorOrDelete.setDrawBitmap(false);
                }
            });
        }
        this.mColorSelectorAnimator.start();
    }

    public /* synthetic */ void lambda$startExpandColorSelectorAnimation$0(ConstraintLayout.LayoutParams layoutParams, int i, ValueAnimator valueAnimator) {
        this.mSeekBar.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
        ((ViewGroup.MarginLayoutParams) layoutParams).width = ((int) (i * valueAnimator.getAnimatedFraction())) + 1;
        this.mColorSelectorView.setLayoutParams(layoutParams);
        this.mColorSelectorView.requestLayout();
    }

    public final void onSelect(int i) {
        FrameData frameData = this.mFrameDataList.get(i);
        getRenderFragment().setFrameData(frameData);
        if (frameData.cinemaStyle) {
            if (this.mColorPickerIsExpand) {
                hideColorPicker();
            }
            this.mSeekBar.setEnabled(false);
            this.mSeekBar.setAlpha(0.3f);
            this.mColorSelectorOrDelete.setEnabled(false);
            this.mColorSelectorOrDelete.setAlpha(0.3f);
            hideTopLayout();
        } else {
            this.mSeekBar.setEnabled(true);
            this.mSeekBar.setAlpha(1.0f);
            this.mColorSelectorOrDelete.setEnabled(true);
            this.mColorSelectorOrDelete.setAlpha(1.0f);
            showTopLayout();
        }
        BubbleSeekBar bubbleSeekBar = this.mSeekBar;
        bubbleSeekBar.setCurrentProgress(getStatus(i, bubbleSeekBar.getMaxProgress()));
        getRenderFragment().setScaleProgress(this.mSeekBar.getCurrentProgress() / this.mSeekBar.getMaxProgress());
    }

    public final void showTopLayout() {
        this.mFrameMenuTopLayout.setVisibility(0);
    }

    public final void hideTopLayout() {
        this.mFrameMenuTopLayout.setVisibility(8);
    }

    public void putStatus(int i, int i2) {
        this.mItemStatusArray.put(i, i2);
    }

    public int getStatus(int i, int i2) {
        return this.mItemStatusArray.get(i, i2);
    }
}
