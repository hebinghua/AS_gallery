package com.miui.gallery.editor.photo.app.adjust2;

import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.MenuFragment;
import com.miui.gallery.editor.photo.app.adjust2.Adjust2MenuFragment;
import com.miui.gallery.editor.photo.app.menu.AdjustView;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.SdkProvider;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment;
import com.miui.gallery.editor.photo.core.common.model.Adjust2Data;
import com.miui.gallery.editor.photo.utils.ScreenAdaptationHelper;
import com.miui.gallery.editor.ui.view.BubbleSeekBar;
import com.miui.gallery.editor.ui.view.EditorBlankDivider;
import com.miui.gallery.editor.utils.EditorOrientationHelper;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.widget.ViewUtils;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

/* loaded from: classes2.dex */
public class Adjust2MenuFragment extends MenuFragment<AbstractEffectFragment, SdkProvider<Adjust2Data, AbstractEffectFragment>> {
    public static final String TAG = Adjust2MenuFragment.class.getSimpleName();
    public Adjust2MenuAdapter mAdapter;
    public AdjustView mAdjustView;
    public BubbleSeekBar mBubbleSeekBar;
    public List<Adjust2Data> mDataList;
    public float mEnterHighlightProgress;
    public float mEnterShadowProgress;
    public TextView mHighlightProgressTv;
    public BubbleSeekBar mHighlightSeekBar;
    public ConstraintLayout mHighlightShadowLayout;
    public boolean mImmersive;
    public FrameLayout mLayoutBottomArea;
    public FrameLayout mLyaoutContentArea;
    public View.OnClickListener mOnClickListener;
    public OnItemClickListener mOnItemClickListener;
    public View.OnClickListener mOnShadowHighlightClickListener;
    public BubbleSeekBar.ProgressListener mProgressListener;
    public SimpleRecyclerView mRecyclerView;
    public TextView mShadowProgressTv;
    public BubbleSeekBar mShadowSeekBar;
    public TextView mTitle;
    public LinearLayout mTopPanel;
    public List<View> mViewAnimatorList;

    public Adjust2MenuFragment() {
        super(Effect.ADJUST2);
        this.mOnItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.editor.photo.app.adjust2.Adjust2MenuFragment.1
            {
                Adjust2MenuFragment.this = this;
            }

            @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
            public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
                ((SimpleRecyclerView) recyclerView).setSpringEnabled(false);
                if (Adjust2MenuFragment.this.mAdapter.getSelection() == i) {
                    return true;
                }
                if (((Adjust2Data) Adjust2MenuFragment.this.mDataList.get(i)).getId() == 10) {
                    Adjust2MenuFragment.this.showHighlightShadowPanel(i);
                    ((AbstractEffectFragment) Adjust2MenuFragment.this.getRenderFragment()).getTitleView().requestFocus();
                    return true;
                }
                Adjust2MenuFragment.this.performItemSelect(i, false);
                return true;
            }
        };
        this.mOnClickListener = new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.app.adjust2.Adjust2MenuFragment.2
            {
                Adjust2MenuFragment.this = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (view.getId() == R.id.title) {
                    Adjust2MenuFragment.this.doReset();
                }
            }
        };
        this.mOnShadowHighlightClickListener = new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.app.adjust2.Adjust2MenuFragment.3
            {
                Adjust2MenuFragment.this = this;
            }

            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                int id = view.getId();
                if (id == R.id.cancel) {
                    Adjust2MenuFragment.this.discardHighlightShadow();
                } else if (id != R.id.ok) {
                } else {
                    Adjust2MenuFragment.this.saveHighlightShadow();
                }
            }
        };
        this.mProgressListener = new AnonymousClass4();
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, com.miui.gallery.editor.photo.app.EditorFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mDataList = new ArrayList(this.mSdkProvider.list());
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        AdjustView adjustView = new AdjustView(viewGroup.getContext());
        this.mAdjustView = adjustView;
        return adjustView;
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.editor_menu_common_content_item_gap);
        this.mTopPanel = (LinearLayout) view.findViewById(R.id.top_panel);
        this.mLyaoutContentArea = (FrameLayout) view.findViewById(R.id.layout_content_area);
        this.mLayoutBottomArea = (FrameLayout) view.findViewById(R.id.layout_bottom_area);
        this.mRecyclerView = (SimpleRecyclerView) view.findViewById(R.id.recycler_view);
        this.mBubbleSeekBar = (BubbleSeekBar) view.findViewById(R.id.seek_bar);
        this.mAdapter = new Adjust2MenuAdapter(getActivity(), this.mDataList, null);
        this.mBubbleSeekBar.setContentDescription(getResources().getString(R.string.photo_editor_talkback_seekbar));
        this.mRecyclerView.setEnableItemClickWhileSettling(true);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.addItemDecoration(new EditorBlankDivider(dimensionPixelSize));
        this.mAdapter.setOnItemClickListener(this.mOnItemClickListener);
        TextView textView = (TextView) view.findViewById(R.id.title);
        this.mTitle = textView;
        textView.setText(R.string.adjust_reset);
        this.mTitle.setOnClickListener(this.mOnClickListener);
        this.mTitle.setTypeface(Typeface.create("mipro-medium", 0));
        this.mTitle.setVisibility(8);
        FolmeUtil.setCustomTouchAnim(this.mTitle, new AnimParams.Builder().setAlpha(0.6f).setScale(1.0f).build(), null, null, true);
        performItemSelect(0, true);
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, com.miui.gallery.editor.utils.LayoutOrientationTracker.OnLayoutOrientationChangeListener
    public void onLayoutOrientationChange() {
        super.onLayoutOrientationChange();
        EditorOrientationHelper.copyLayoutParams(new AdjustView(getContext()), getView(), true);
        if (this.mHighlightShadowLayout != null) {
            EditorOrientationHelper.copyLayoutParams(LayoutInflater.from(getContext()).inflate(R.layout.adjust_highlight_shadow_view, (ViewGroup) null), this.mHighlightShadowLayout, true);
        }
    }

    @Override // androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        ScreenAdaptationHelper.updateBSBWidth(getContext(), this.mBubbleSeekBar);
        if (this.mHighlightShadowLayout != null) {
            ScreenAdaptationHelper.updateBSBWidth(getContext(), this.mHighlightSeekBar);
            ScreenAdaptationHelper.updateBSBWidth(getContext(), this.mShadowSeekBar);
        }
    }

    /* renamed from: com.miui.gallery.editor.photo.app.adjust2.Adjust2MenuFragment$4 */
    /* loaded from: classes2.dex */
    public class AnonymousClass4 implements BubbleSeekBar.ProgressListener {
        /* renamed from: $r8$lambda$3Y6P5MT30FFUHvEpPkD2AcF8-pQ */
        public static /* synthetic */ void m749$r8$lambda$3Y6P5MT30FFUHvEpPkD2AcF8pQ(AnonymousClass4 anonymousClass4, View view) {
            anonymousClass4.lambda$onStopTrackingTouch$2(view);
        }

        /* renamed from: $r8$lambda$A1stG87Zqff8YB4YXr1_H6Yb-pE */
        public static /* synthetic */ void m750$r8$lambda$A1stG87Zqff8YB4YXr1_H6YbpE(AnonymousClass4 anonymousClass4, View view) {
            anonymousClass4.lambda$onProgressStartChange$1(view);
        }

        /* renamed from: $r8$lambda$n-ZQ-O4NOvg94AVXAWgAUq4_CIM */
        public static /* synthetic */ void m751$r8$lambda$nZQO4NOvg94AVXAWgAUq4_CIM(AnonymousClass4 anonymousClass4, View view) {
            anonymousClass4.lambda$onProgressStartChange$0(view);
        }

        @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
        public void onStartTrackingTouch(BubbleSeekBar bubbleSeekBar) {
        }

        public AnonymousClass4() {
            Adjust2MenuFragment.this = r1;
        }

        @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
        public void onProgressStartChange(BubbleSeekBar bubbleSeekBar, int i) {
            Adjust2MenuFragment.this.mImmersive = true;
            if (!Adjust2MenuFragment.this.isShowHighlightShadow()) {
                Adjust2MenuFragment.this.mViewAnimatorList = new ArrayList(Arrays.asList(Adjust2MenuFragment.this.mLayoutBottomArea, Adjust2MenuFragment.this.mLyaoutContentArea, ((AbstractEffectFragment) Adjust2MenuFragment.this.getRenderFragment()).getTitleView()));
                Adjust2MenuFragment.this.mViewAnimatorList.forEach(new Consumer() { // from class: com.miui.gallery.editor.photo.app.adjust2.Adjust2MenuFragment$4$$ExternalSyntheticLambda1
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        Adjust2MenuFragment.AnonymousClass4.m750$r8$lambda$A1stG87Zqff8YB4YXr1_H6YbpE(Adjust2MenuFragment.AnonymousClass4.this, (View) obj);
                    }
                });
                return;
            }
            Adjust2MenuFragment adjust2MenuFragment = Adjust2MenuFragment.this;
            adjust2MenuFragment.mViewAnimatorList = ViewUtils.getAllChildViews(adjust2MenuFragment.mHighlightShadowLayout);
            Adjust2MenuFragment.this.mViewAnimatorList.remove(bubbleSeekBar);
            Adjust2MenuFragment.this.mViewAnimatorList.add(Adjust2MenuFragment.this.mLayoutBottomArea);
            Adjust2MenuFragment.this.mViewAnimatorList.add(((AbstractEffectFragment) Adjust2MenuFragment.this.getRenderFragment()).getTitleView());
            Adjust2MenuFragment.this.mViewAnimatorList.forEach(new Consumer() { // from class: com.miui.gallery.editor.photo.app.adjust2.Adjust2MenuFragment$4$$ExternalSyntheticLambda2
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    Adjust2MenuFragment.AnonymousClass4.m751$r8$lambda$nZQO4NOvg94AVXAWgAUq4_CIM(Adjust2MenuFragment.AnonymousClass4.this, (View) obj);
                }
            });
        }

        public /* synthetic */ void lambda$onProgressStartChange$0(View view) {
            view.setVisibility(Adjust2MenuFragment.this.mImmersive ? 4 : 0);
        }

        public /* synthetic */ void lambda$onProgressStartChange$1(View view) {
            view.setVisibility(Adjust2MenuFragment.this.mImmersive ? 4 : 0);
        }

        @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
        public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int i) {
            Adjust2MenuFragment.this.doConfig(bubbleSeekBar.getCurrentProgress(), (Adjust2Data) Adjust2MenuFragment.this.mDataList.get(((Integer) bubbleSeekBar.getTag()).intValue()));
            if (i == 0 || i == 100 || i == -100) {
                LinearMotorHelper.performHapticFeedback(bubbleSeekBar, LinearMotorHelper.HAPTIC_MESH_HEAVY);
            }
        }

        @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
        public void onStopTrackingTouch(BubbleSeekBar bubbleSeekBar) {
            if (!Adjust2MenuFragment.this.mImmersive) {
                return;
            }
            Adjust2MenuFragment.this.mImmersive = false;
            Adjust2MenuFragment.this.mViewAnimatorList.forEach(new Consumer() { // from class: com.miui.gallery.editor.photo.app.adjust2.Adjust2MenuFragment$4$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    Adjust2MenuFragment.AnonymousClass4.m749$r8$lambda$3Y6P5MT30FFUHvEpPkD2AcF8pQ(Adjust2MenuFragment.AnonymousClass4.this, (View) obj);
                }
            });
            int intValue = ((Integer) bubbleSeekBar.getTag()).intValue();
            if (Adjust2MenuFragment.this.isShowHighlightShadow()) {
                Adjust2MenuFragment.this.mViewAnimatorList.add(bubbleSeekBar);
                Adjust2MenuFragment adjust2MenuFragment = Adjust2MenuFragment.this;
                adjust2MenuFragment.setProgressText((Adjust2Data) adjust2MenuFragment.mDataList.get(intValue));
            }
            bubbleSeekBar.setContentDescription(String.format("%s%s%d", ((Adjust2Data) Adjust2MenuFragment.this.mDataList.get(intValue)).name, Adjust2MenuFragment.this.getResources().getString(R.string.photo_editor_talkback_seekbar), Integer.valueOf((int) ((Adjust2Data) Adjust2MenuFragment.this.mDataList.get(intValue)).progress)));
        }

        public /* synthetic */ void lambda$onStopTrackingTouch$2(View view) {
            view.setVisibility(Adjust2MenuFragment.this.mImmersive ? 4 : 0);
        }
    }

    public final void performItemSelect(int i, boolean z) {
        this.mRecyclerView.smoothScrollToPosition(i);
        this.mAdapter.setIsPlayAnimation(false);
        if (!z) {
            doRender(this.mDataList.get(i));
        }
        Adjust2MenuAdapter adjust2MenuAdapter = this.mAdapter;
        adjust2MenuAdapter.notifyItemChanged(adjust2MenuAdapter.getSelection());
        this.mAdapter.setSelection(i);
        this.mBubbleSeekBar.setTag(Integer.valueOf(i));
        initSeekBar(this.mBubbleSeekBar);
    }

    public final void doReset() {
        getRenderFragment().clear();
        getRenderFragment().render();
        ViewUtils.getAllChildViews(this.mTopPanel).forEach(Adjust2MenuFragment$$ExternalSyntheticLambda0.INSTANCE);
        this.mAdapter.setIsPlayAnimation(false);
        for (int i = 0; i < this.mDataList.size(); i++) {
            if (this.mDataList.get(i).progress != 0.0f) {
                this.mDataList.get(i).progress = 0.0f;
                this.mAdapter.notifyItemChanged(i);
            }
        }
        if (isShowHighlightShadow()) {
            this.mHighlightProgressTv.setText("0");
            this.mShadowProgressTv.setText("0");
        }
        this.mTitle.setVisibility(8);
        getHostAbility().sample("adjust_reset_click");
    }

    public static /* synthetic */ void lambda$doReset$0(View view) {
        if (view instanceof BubbleSeekBar) {
            ((BubbleSeekBar) view).setCurrentProgress(0.0f);
        }
    }

    public final boolean isShowHighlightShadow() {
        ConstraintLayout constraintLayout = this.mHighlightShadowLayout;
        return constraintLayout != null && constraintLayout.getVisibility() == 0;
    }

    public final void doRender(Adjust2Data adjust2Data) {
        this.mAdapter.setIsPlayAnimation(true);
        getRenderFragment().remove(adjust2Data);
        getRenderFragment().add(adjust2Data, null);
        getRenderFragment().render();
    }

    public final void doConfig(float f, Adjust2Data adjust2Data) {
        if (this.mAdapter.getSelection() == -1) {
            DefaultLogger.d(TAG, "no effect rendered, skip");
            return;
        }
        if (!isShowHighlightShadow()) {
            this.mTitle.setVisibility(0);
        }
        setProgressText(adjust2Data);
        DefaultLogger.d(TAG, "AdjustData progress: %d", Float.valueOf(f));
        this.mAdapter.setIsPlayAnimation(false);
        adjust2Data.progress = f;
        Adjust2MenuAdapter adjust2MenuAdapter = this.mAdapter;
        adjust2MenuAdapter.notifyItemChanged(adjust2MenuAdapter.getSelection());
        getRenderFragment().remove(adjust2Data);
        getRenderFragment().add(adjust2Data, null);
        getRenderFragment().render();
    }

    public final void showHighlightShadowPanel(int i) {
        getRenderFragment().getTitleView().setText(R.string.adjust_highlight_shadow);
        ConstraintLayout constraintLayout = this.mHighlightShadowLayout;
        if (constraintLayout == null) {
            ConstraintLayout constraintLayout2 = (ConstraintLayout) LayoutInflater.from(getContext()).inflate(R.layout.adjust_highlight_shadow_view, (ViewGroup) null);
            this.mHighlightShadowLayout = constraintLayout2;
            this.mAdjustView.addView(constraintLayout2);
        } else {
            constraintLayout.setVisibility(0);
        }
        BubbleSeekBar bubbleSeekBar = (BubbleSeekBar) this.mHighlightShadowLayout.findViewById(R.id.seek_bar_highlight);
        BubbleSeekBar bubbleSeekBar2 = (BubbleSeekBar) this.mHighlightShadowLayout.findViewById(R.id.seek_bar_shadow);
        this.mHighlightProgressTv = (TextView) this.mHighlightShadowLayout.findViewById(R.id.tv_highlight_progress);
        this.mShadowProgressTv = (TextView) this.mHighlightShadowLayout.findViewById(R.id.tv_shadow_progress);
        bubbleSeekBar.setTag(Integer.valueOf(i));
        bubbleSeekBar2.setTag(Integer.valueOf(this.mDataList.size() - 1));
        initSeekBar(bubbleSeekBar);
        initSeekBar(bubbleSeekBar2);
        this.mHighlightShadowLayout.findViewById(R.id.ok).setOnClickListener(this.mOnShadowHighlightClickListener);
        this.mHighlightShadowLayout.findViewById(R.id.cancel).setOnClickListener(this.mOnShadowHighlightClickListener);
        for (Adjust2Data adjust2Data : this.mDataList) {
            if (adjust2Data.getId() == 10) {
                this.mEnterHighlightProgress = adjust2Data.progress;
            }
            if (adjust2Data.getId() == 11) {
                this.mEnterShadowProgress = adjust2Data.progress;
            }
        }
    }

    public final void initSeekBar(BubbleSeekBar bubbleSeekBar) {
        Adjust2Data adjust2Data = this.mDataList.get(((Integer) bubbleSeekBar.getTag()).intValue());
        if (adjust2Data.isMid()) {
            bubbleSeekBar.setMinProgress(adjust2Data.getMin());
            bubbleSeekBar.setMaxProgress(adjust2Data.getMax());
            bubbleSeekBar.setCurrentProgress(adjust2Data.progress);
        } else {
            bubbleSeekBar.setMinProgress(0);
            bubbleSeekBar.setMaxProgress(adjust2Data.getMax());
            bubbleSeekBar.setCurrentProgress(adjust2Data.progress);
        }
        bubbleSeekBar.setProgressListener(this.mProgressListener);
        setProgressText(adjust2Data);
    }

    public final void setProgressText(Adjust2Data adjust2Data) {
        TextView textView;
        TextView textView2;
        int id = adjust2Data.getId();
        if (id == 10 && (textView2 = this.mHighlightProgressTv) != null) {
            textView2.setText(Integer.toString((int) adjust2Data.progress));
        } else if (id != 11 || (textView = this.mShadowProgressTv) == null) {
        } else {
            textView.setText(Integer.toString((int) adjust2Data.progress));
        }
    }

    public final void saveHighlightShadow() {
        getRenderFragment().getTitleView().setText(R.string.photo_editor_adjust);
        this.mHighlightShadowLayout.setVisibility(8);
        this.mTitle.setVisibility(8);
        Iterator<Adjust2Data> it = this.mDataList.iterator();
        while (it.hasNext()) {
            if (it.next().progress != 0.0f) {
                this.mTitle.setVisibility(0);
            }
        }
    }

    public final void discardHighlightShadow() {
        getRenderFragment().getTitleView().setText(R.string.photo_editor_adjust);
        this.mHighlightShadowLayout.setVisibility(8);
        for (Adjust2Data adjust2Data : this.mDataList) {
            if (adjust2Data.getId() == 10) {
                adjust2Data.progress = this.mEnterHighlightProgress;
            }
            if (adjust2Data.getId() == 11) {
                adjust2Data.progress = this.mEnterShadowProgress;
            }
        }
        getRenderFragment().render();
    }
}
