package com.miui.gallery.editor.photo.app.adjust;

import android.animation.Animator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.MenuFragment;
import com.miui.gallery.editor.photo.app.adjust.AdjustMenuFragment;
import com.miui.gallery.editor.photo.app.menu.AdjustView;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.Metadata;
import com.miui.gallery.editor.photo.core.SdkProvider;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment;
import com.miui.gallery.editor.photo.core.common.model.AdjustData;
import com.miui.gallery.editor.photo.core.imports.obsoletes.OneShotAnimateListener;
import com.miui.gallery.editor.photo.utils.EditorMiscHelper;
import com.miui.gallery.editor.ui.view.BubbleSeekBar;
import com.miui.gallery.ui.CenterSmoothScrollerController;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.recyclerview.BlankDivider;
import com.miui.gallery.widget.recyclerview.CustomScrollerLinearLayoutManager;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

@Deprecated
/* loaded from: classes2.dex */
public class AdjustMenuFragment extends MenuFragment<AbstractEffectFragment, SdkProvider<AdjustData, AbstractEffectFragment>> {
    public AdjustAdapter mAdapter;
    public List<AdjustData> mDataList;
    public boolean mImmersive;
    public FrameLayout mLayoutBottomArea;
    public FrameLayout mLyaoutContentArea;
    public OnItemClickListener mOnItemClickListener;
    public OneShotAnimateListener mOneShotAnimateListener;
    public SimpleRecyclerView mRecyclerView;
    public TextView mTitle;
    public LinearLayout mTopPanel;
    public List<View> mViewAnimatorList;

    public AdjustMenuFragment() {
        super(Effect.ADJUST);
        this.mOnItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.editor.photo.app.adjust.AdjustMenuFragment.1
            {
                AdjustMenuFragment.this = this;
            }

            @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
            public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
                ((SimpleRecyclerView) recyclerView).setSpringEnabled(false);
                if (AdjustMenuFragment.this.mAdapter.getSelection() == i) {
                    return true;
                }
                AdjustMenuFragment.this.performItemSelect(i, false);
                return true;
            }
        };
        this.mOneShotAnimateListener = new AnonymousClass3();
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, com.miui.gallery.editor.photo.app.EditorFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mDataList = new ArrayList(this.mSdkProvider.list());
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return new AdjustView(viewGroup.getContext());
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mTopPanel = (LinearLayout) view.findViewById(R.id.top_panel);
        this.mLyaoutContentArea = (FrameLayout) view.findViewById(R.id.layout_content_area);
        this.mLayoutBottomArea = (FrameLayout) view.findViewById(R.id.layout_bottom_area);
        SimpleRecyclerView simpleRecyclerView = (SimpleRecyclerView) view.findViewById(R.id.recycler_view);
        this.mRecyclerView = simpleRecyclerView;
        simpleRecyclerView.setEnableItemClickWhileSettling(true);
        AdjustAdapter adjustAdapter = new AdjustAdapter(getActivity(), this.mDataList, null);
        this.mAdapter = adjustAdapter;
        this.mRecyclerView.setAdapter(adjustAdapter);
        CustomScrollerLinearLayoutManager customScrollerLinearLayoutManager = new CustomScrollerLinearLayoutManager(getActivity());
        customScrollerLinearLayoutManager.setSmoothScroller(new CenterSmoothScrollerController(getActivity()));
        customScrollerLinearLayoutManager.setOrientation(0);
        this.mRecyclerView.setLayoutManager(customScrollerLinearLayoutManager);
        this.mRecyclerView.addItemDecoration(new BlankDivider(getResources().getDimensionPixelSize(R.dimen.editor_menu_common_content_item_gap)));
        this.mAdapter.setOnItemClickListener(this.mOnItemClickListener);
        TextView textView = (TextView) view.findViewById(R.id.title);
        this.mTitle = textView;
        textView.setText(R.string.photo_editor_adjust);
        this.mTitle.setVisibility(8);
        performItemSelect(0, true);
    }

    public final void doRender(Metadata metadata) {
        this.mAdapter.setIsPlayAnimation(true);
        getRenderFragment().remove(metadata);
        getRenderFragment().add(metadata, null);
        getRenderFragment().render();
    }

    public final void doConfig(int i) {
        if (this.mAdapter.getSelection() == -1) {
            DefaultLogger.d("AdjustMenuFragment", "no effect rendered, skip");
            return;
        }
        DefaultLogger.d("AdjustMenuFragment", "AdjustData progress:" + i);
        AdjustData adjustData = this.mDataList.get(this.mAdapter.getSelection());
        adjustData.progress = i;
        this.mAdapter.setSelectedChanged();
        getRenderFragment().remove(adjustData);
        getRenderFragment().add(adjustData, null);
        getRenderFragment().render();
    }

    public final void performItemSelect(int i, boolean z) {
        this.mRecyclerView.smoothScrollToPosition(i);
        final AdjustData adjustData = this.mDataList.get(i);
        if (!z) {
            doRender(adjustData);
        }
        this.mAdapter.setSelection(i);
        if (((BubbleSeekBar) this.mTopPanel.getChildAt(0)) != null) {
            this.mTopPanel.removeAllViews();
        }
        BubbleSeekBar bubbleSeekBar = (BubbleSeekBar) LayoutInflater.from(getContext()).inflate(R.layout.seek_bar_view, (ViewGroup) null);
        bubbleSeekBar.setContentDescription(getResources().getString(R.string.photo_editor_talkback_seekbar));
        if (!adjustData.isMid()) {
            bubbleSeekBar.setMaxProgress(adjustData.getMax());
            bubbleSeekBar.setCurrentProgress(adjustData.progress);
        } else {
            bubbleSeekBar.setMinProgress(adjustData.getMin());
            bubbleSeekBar.setMaxProgress(adjustData.getMax());
            bubbleSeekBar.setCurrentProgress(adjustData.progress);
        }
        this.mTopPanel.addView(bubbleSeekBar, -1, -2);
        bubbleSeekBar.setProgressListener(new BubbleSeekBar.ProgressListener() { // from class: com.miui.gallery.editor.photo.app.adjust.AdjustMenuFragment.2
            @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
            public void onStartTrackingTouch(BubbleSeekBar bubbleSeekBar2) {
            }

            {
                AdjustMenuFragment.this = this;
            }

            @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
            public void onProgressStartChange(BubbleSeekBar bubbleSeekBar2, int i2) {
                AdjustMenuFragment.this.mImmersive = true;
                AdjustMenuFragment.this.mViewAnimatorList = new ArrayList(Arrays.asList(AdjustMenuFragment.this.mLayoutBottomArea, AdjustMenuFragment.this.mLyaoutContentArea, ((AbstractEffectFragment) AdjustMenuFragment.this.getRenderFragment()).getTitleView()));
                EditorMiscHelper.enterImmersive(AdjustMenuFragment.this.mViewAnimatorList, AdjustMenuFragment.this.mOneShotAnimateListener);
            }

            @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar2, int i2) {
                AdjustMenuFragment.this.doConfig(i2);
                if (i2 == 0 || i2 == 100 || i2 == -100) {
                    LinearMotorHelper.performHapticFeedback(bubbleSeekBar2, LinearMotorHelper.HAPTIC_MESH_HEAVY);
                }
            }

            @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
            public void onStopTrackingTouch(BubbleSeekBar bubbleSeekBar2) {
                if (!AdjustMenuFragment.this.mImmersive) {
                    return;
                }
                AdjustMenuFragment.this.mImmersive = false;
                EditorMiscHelper.exitImmersive(AdjustMenuFragment.this.mViewAnimatorList, AdjustMenuFragment.this.mOneShotAnimateListener);
                bubbleSeekBar2.setContentDescription(String.format("%s%s%d", adjustData.name, AdjustMenuFragment.this.getResources().getString(R.string.photo_editor_talkback_seekbar), Integer.valueOf(adjustData.progress)));
            }
        });
    }

    /* renamed from: com.miui.gallery.editor.photo.app.adjust.AdjustMenuFragment$3 */
    /* loaded from: classes2.dex */
    public class AnonymousClass3 extends OneShotAnimateListener {
        /* renamed from: $r8$lambda$FxjiFT56bC6Lm-M9g-4B4wbtub0 */
        public static /* synthetic */ void m746$r8$lambda$FxjiFT56bC6LmM9g4B4wbtub0(AnonymousClass3 anonymousClass3, View view) {
            anonymousClass3.lambda$onAnimationEnd$0(view);
        }

        public AnonymousClass3() {
            AdjustMenuFragment.this = r1;
        }

        @Override // android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator, boolean z) {
            animator.removeListener(this);
            AdjustMenuFragment.this.mViewAnimatorList.forEach(new Consumer() { // from class: com.miui.gallery.editor.photo.app.adjust.AdjustMenuFragment$3$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    AdjustMenuFragment.AnonymousClass3.m746$r8$lambda$FxjiFT56bC6LmM9g4B4wbtub0(AdjustMenuFragment.AnonymousClass3.this, (View) obj);
                }
            });
        }

        public /* synthetic */ void lambda$onAnimationEnd$0(View view) {
            view.setVisibility(AdjustMenuFragment.this.mImmersive ? 4 : 0);
        }
    }
}
