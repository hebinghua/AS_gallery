package com.miui.gallery.editor.photo.app.remover2;

import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.baidu.location.BDLocation;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.MenuFragment;
import com.miui.gallery.editor.photo.app.menu.Remover2View;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.SdkProvider;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractRemover2Fragment;
import com.miui.gallery.editor.photo.core.common.model.Remover2Data;
import com.miui.gallery.editor.photo.core.imports.remover2.Remover2RenderFragment;
import com.miui.gallery.editor.photo.utils.ScreenAdaptationHelper;
import com.miui.gallery.editor.photo.widgets.PaintSizePopupWindow;
import com.miui.gallery.editor.photo.widgets.recyclerview.Selectable;
import com.miui.gallery.editor.ui.view.BubbleSeekBar;
import com.miui.gallery.editor.ui.view.EditorBlankDivider;
import com.miui.gallery.editor.utils.EditorOrientationHelper;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerView;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public class Remover2MenuFragment extends MenuFragment<AbstractRemover2Fragment, SdkProvider<Remover2Data, AbstractRemover2Fragment>> {
    public Remover2Adapter mAdapter;
    public int mCurrentType;
    public List<Remover2Data> mDataList;
    public OnItemClickListener mOnItemClickListener;
    public PaintSizePopupWindow mPaintSizePopupWindow;
    public TextView mPeopleClear;
    public ColorStateList mPeopleClearDisableColor;
    public ColorStateList mPeopleClearEnableColor;
    public SimpleRecyclerView mRecyclerView;
    public Remover2RenderFragment.RenderCallback mRenderCallback;
    public BubbleSeekBar mSeekBar;
    public BubbleSeekBar.ProgressListener mSeekBarChangeListener;

    public Remover2MenuFragment() {
        super(Effect.REMOVER2);
        this.mCurrentType = 0;
        this.mOnItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.editor.photo.app.remover2.Remover2MenuFragment.2
            @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
            public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
                Remover2RenderFragment remover2RenderFragment;
                if (Inpaint2.getInstance().isInited() && Remover2MenuFragment.this.mAdapter.getSelection() != i && (remover2RenderFragment = (Remover2RenderFragment) Remover2MenuFragment.this.getRenderFragment()) != null && !remover2RenderFragment.isProcessing()) {
                    Remover2MenuFragment.this.mAdapter.setSelection(i);
                    Remover2Data remover2Data = (Remover2Data) Remover2MenuFragment.this.mDataList.get(i);
                    Remover2MenuFragment.this.mCurrentType = remover2Data.mType;
                    if (Remover2MenuFragment.this.mCurrentType != 2 || !Inpaint2.getInstance().isNeedSegment()) {
                        remover2RenderFragment.setRemover2Data(remover2Data);
                    }
                    Remover2MenuFragment remover2MenuFragment = Remover2MenuFragment.this;
                    remover2RenderFragment.setPaintSize(remover2MenuFragment.getPaintSizeByProgress((int) remover2MenuFragment.mSeekBar.getCurrentProgress()));
                    if (Remover2MenuFragment.this.mCurrentType == 2) {
                        Remover2MenuFragment.this.mPeopleClear.setVisibility(0);
                        Remover2MenuFragment.this.mSeekBar.setVisibility(8);
                    } else if (Remover2MenuFragment.this.mCurrentType == 0) {
                        Remover2MenuFragment.this.mPeopleClear.setVisibility(8);
                        Remover2MenuFragment.this.mSeekBar.setVisibility(0);
                    } else {
                        Remover2MenuFragment.this.mPeopleClear.setVisibility(8);
                        Remover2MenuFragment.this.mSeekBar.setVisibility(8);
                    }
                    HashMap hashMap = new HashMap();
                    hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, remover2Data.name);
                    Remover2MenuFragment.this.getHostAbility().sample("remover2_item_click", hashMap);
                    if (((Remover2Data) Remover2MenuFragment.this.mDataList.get(i)).mType != 2 || !Inpaint2.getInstance().isNeedSegment()) {
                        return true;
                    }
                    Remover2MenuFragment.this.mPeopleClear.setEnabled(false);
                    remover2RenderFragment.onSegment();
                    return false;
                }
                return false;
            }
        };
        this.mSeekBarChangeListener = new BubbleSeekBar.ProgressListener() { // from class: com.miui.gallery.editor.photo.app.remover2.Remover2MenuFragment.3
            @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int i) {
                if (i == 0 || i == 100) {
                    LinearMotorHelper.performHapticFeedback(bubbleSeekBar, LinearMotorHelper.HAPTIC_MESH_HEAVY);
                }
                int paintSizeByProgress = Remover2MenuFragment.this.getPaintSizeByProgress(i);
                Remover2MenuFragment.this.mPaintSizePopupWindow.setPaintSize(paintSizeByProgress);
                ((AbstractRemover2Fragment) Remover2MenuFragment.this.getRenderFragment()).setPaintSize(paintSizeByProgress);
            }

            @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
            public void onStartTrackingTouch(BubbleSeekBar bubbleSeekBar) {
                Remover2MenuFragment.this.mPaintSizePopupWindow.setPaintSize(Remover2MenuFragment.this.getPaintSizeByProgress((int) bubbleSeekBar.getCurrentProgress()));
                if (Remover2MenuFragment.this.isLayoutPortrait()) {
                    Remover2MenuFragment.this.mPaintSizePopupWindow.showAtLocation(Remover2MenuFragment.this.mRecyclerView, 17, 0, (-Remover2MenuFragment.this.getResources().getDimensionPixelSize(R.dimen.photo_editor_menu_panel_height)) / 2);
                } else {
                    Remover2MenuFragment.this.mPaintSizePopupWindow.showAtLocation(Remover2MenuFragment.this.mRecyclerView, 17, (-Remover2MenuFragment.this.getResources().getDimensionPixelSize(R.dimen.photo_editor_menu_panel_height)) / 2, 0);
                }
            }

            @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
            public void onStopTrackingTouch(BubbleSeekBar bubbleSeekBar) {
                Remover2MenuFragment.this.mPaintSizePopupWindow.dismiss();
            }
        };
        this.mRenderCallback = new Remover2RenderFragment.RenderCallback() { // from class: com.miui.gallery.editor.photo.app.remover2.Remover2MenuFragment.4
            @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2RenderFragment.RenderCallback
            public void initFinished() {
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2RenderFragment.RenderCallback
            public void inpaintFinished() {
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2RenderFragment.RenderCallback
            public void clearPeopleEnable(boolean z) {
                if (Remover2MenuFragment.this.mPeopleClear != null) {
                    if (z) {
                        Remover2MenuFragment.this.mPeopleClear.setTextColor(Remover2MenuFragment.this.mPeopleClearEnableColor);
                    } else {
                        Remover2MenuFragment.this.mPeopleClear.setTextColor(Remover2MenuFragment.this.mPeopleClearDisableColor);
                    }
                }
            }

            @Override // com.miui.gallery.editor.photo.core.imports.remover2.Remover2RenderFragment.RenderCallback
            public void segmentFinished() {
                Remover2MenuFragment.this.mPeopleClear.setEnabled(true);
                if (!Inpaint2.getInstance().isHavePeople()) {
                    Remover2MenuFragment.this.mPeopleClear.setTextColor(Remover2MenuFragment.this.mPeopleClearDisableColor);
                } else {
                    Remover2MenuFragment.this.mPeopleClear.setTextColor(Remover2MenuFragment.this.mPeopleClearEnableColor);
                }
                if (Remover2MenuFragment.this.getRenderFragment() != null) {
                    ((AbstractRemover2Fragment) Remover2MenuFragment.this.getRenderFragment()).setRemover2Data((Remover2Data) Remover2MenuFragment.this.mDataList.get(2));
                }
            }
        };
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, com.miui.gallery.editor.photo.app.EditorFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mDataList = new ArrayList(this.mSdkProvider.list());
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, com.miui.gallery.editor.utils.LayoutOrientationTracker.OnLayoutOrientationChangeListener
    public void onLayoutOrientationChange() {
        super.onLayoutOrientationChange();
        EditorOrientationHelper.copyLayoutParams(new Remover2View(getContext()), getView(), true);
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        SimpleRecyclerView simpleRecyclerView = (SimpleRecyclerView) view.findViewById(R.id.recycler_view);
        this.mRecyclerView = simpleRecyclerView;
        simpleRecyclerView.setEnableItemClickWhileSettling(true);
        this.mAdapter = new Remover2Adapter(getActivity(), this.mDataList, new Selectable.Selector(getResources().getColor(R.color.photo_editor_menu_item_highlight_color)));
        this.mRecyclerView.addItemDecoration(new EditorBlankDivider(getResources(), R.dimen.editor_remover2_item_gap));
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mAdapter.setOnItemClickListener(this.mOnItemClickListener);
        this.mAdapter.setSelection(0);
        this.mPaintSizePopupWindow = new PaintSizePopupWindow(getActivity());
        TextView textView = (TextView) view.findViewById(R.id.title);
        this.mPeopleClear = textView;
        textView.setText(R.string.photo_editor_remover2_people_clear);
        this.mPeopleClear.setVisibility(8);
        this.mPeopleClear.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.app.remover2.Remover2MenuFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                if (Remover2MenuFragment.this.getRenderFragment() != null) {
                    ((Remover2RenderFragment) Remover2MenuFragment.this.getRenderFragment()).clearAllPeople();
                    Remover2MenuFragment.this.getHostAbility().sample("remover2_clear_people");
                }
            }
        });
        this.mPeopleClearEnableColor = getResources().getColorStateList(R.color.photo_editor_remover2_people_clear_normal);
        this.mPeopleClearDisableColor = getResources().getColorStateList(R.color.photo_editor_remover2_people_clear_disable);
        BubbleSeekBar bubbleSeekBar = (BubbleSeekBar) view.findViewById(R.id.seek_bar);
        this.mSeekBar = bubbleSeekBar;
        bubbleSeekBar.setCurrentProgress(bubbleSeekBar.getMaxProgress() / 2);
        this.mSeekBar.setProgressListener(this.mSeekBarChangeListener);
        this.mSeekBar.setHideBubble(true);
        getRenderFragment().setPaintSize(this.mSeekBar.getCurrentProgress() + 35.0f);
        AbstractRemover2Fragment renderFragment = getRenderFragment();
        if (renderFragment instanceof Remover2RenderFragment) {
            ((Remover2RenderFragment) renderFragment).setRenderCallback(this.mRenderCallback);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return new Remover2View(viewGroup.getContext());
    }

    @Override // androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        ScreenAdaptationHelper.updateBSBWidth(getContext(), this.mSeekBar);
    }

    public final int getPaintSizeByProgress(int i) {
        int i2;
        int i3;
        if (this.mCurrentType != 1) {
            i2 = 35;
            i3 = BDLocation.TypeNetWorkLocation;
        } else {
            i2 = 20;
            i3 = 100;
        }
        return i2 + Math.round(((i3 - i2) * i) / this.mSeekBar.getMaxProgress());
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment
    public void notifyDiscard() {
        Remover2RenderFragment remover2RenderFragment = (Remover2RenderFragment) getRenderFragment();
        if (remover2RenderFragment == null || !remover2RenderFragment.isProcessing()) {
            super.notifyDiscard();
        }
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment
    public void notifySave() {
        Remover2RenderFragment remover2RenderFragment = (Remover2RenderFragment) getRenderFragment();
        if (remover2RenderFragment == null || !remover2RenderFragment.isProcessing()) {
            super.notifySave();
        }
    }
}
