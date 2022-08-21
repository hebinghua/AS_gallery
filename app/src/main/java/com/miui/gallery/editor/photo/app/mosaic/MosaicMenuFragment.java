package com.miui.gallery.editor.photo.app.mosaic;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.MenuFragment;
import com.miui.gallery.editor.photo.app.menu.MosaicView;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.SdkProvider;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractMosaicFragment;
import com.miui.gallery.editor.photo.core.common.model.MosaicData;
import com.miui.gallery.editor.photo.utils.ScreenAdaptationHelper;
import com.miui.gallery.editor.photo.widgets.PaintSizePopupWindow;
import com.miui.gallery.editor.ui.view.BubbleSeekBar;
import com.miui.gallery.editor.ui.view.EditorBlankDivider;
import com.miui.gallery.editor.utils.EditorOrientationHelper;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerView;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class MosaicMenuFragment extends MenuFragment<AbstractMosaicFragment, SdkProvider<MosaicData, AbstractMosaicFragment>> {
    public int mMenuHeight;
    public MosaicAdapter mMosaicAdapter;
    public List<MosaicData> mMosaicDataList;
    public OnItemClickListener mOnItemClickListener;
    public PaintSizePopupWindow mPaintPopupWindow;
    public SimpleRecyclerView mRecyclerView;
    public BubbleSeekBar mSeekBar;
    public BubbleSeekBar.ProgressListener mSeekBarChangeListener;

    public MosaicMenuFragment() {
        super(Effect.MOSAIC);
        this.mOnItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.editor.photo.app.mosaic.MosaicMenuFragment.1
            @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
            public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
                if (MosaicMenuFragment.this.getRenderFragment() == null || !((AbstractMosaicFragment) MosaicMenuFragment.this.getRenderFragment()).isDrawingMosaic()) {
                    ((SimpleRecyclerView) recyclerView).setSpringEnabled(false);
                    recyclerView.smoothScrollToPosition(i);
                    MosaicMenuFragment.this.setSelection(i);
                    return true;
                }
                return true;
            }
        };
        this.mSeekBarChangeListener = new BubbleSeekBar.ProgressListener() { // from class: com.miui.gallery.editor.photo.app.mosaic.MosaicMenuFragment.2
            @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
            public void onStartTrackingTouch(BubbleSeekBar bubbleSeekBar) {
            }

            @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
            public void onProgressStartChange(BubbleSeekBar bubbleSeekBar, int i) {
                if (MosaicMenuFragment.this.isLayoutPortrait()) {
                    MosaicMenuFragment.this.mPaintPopupWindow.showAtLocation(MosaicMenuFragment.this.mRecyclerView, 17, 0, (-MosaicMenuFragment.this.mMenuHeight) / 2);
                } else {
                    MosaicMenuFragment.this.mPaintPopupWindow.showAtLocation(MosaicMenuFragment.this.mRecyclerView, 17, (-MosaicMenuFragment.this.mMenuHeight) / 2, 0);
                }
            }

            @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int i) {
                if (i == 0 || i == 100) {
                    LinearMotorHelper.performHapticFeedback(bubbleSeekBar, LinearMotorHelper.HAPTIC_MESH_HEAVY);
                }
                int round = Math.round((i * 126) / 100.0f) + 35;
                MosaicMenuFragment.this.mPaintPopupWindow.setPaintSize(round);
                ((AbstractMosaicFragment) MosaicMenuFragment.this.getRenderFragment()).setMosaicPaintSize(round);
            }

            @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
            public void onStopTrackingTouch(BubbleSeekBar bubbleSeekBar) {
                MosaicMenuFragment.this.mPaintPopupWindow.dismiss();
            }
        };
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return new MosaicView(viewGroup.getContext());
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, com.miui.gallery.editor.utils.LayoutOrientationTracker.OnLayoutOrientationChangeListener
    public void onLayoutOrientationChange() {
        super.onLayoutOrientationChange();
        EditorOrientationHelper.copyLayoutParams(new MosaicView(getContext()), getView(), true);
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mPaintPopupWindow = new PaintSizePopupWindow(getActivity());
        BubbleSeekBar bubbleSeekBar = (BubbleSeekBar) view.findViewById(R.id.seek_bar);
        this.mSeekBar = bubbleSeekBar;
        bubbleSeekBar.setProgressListener(this.mSeekBarChangeListener);
        this.mSeekBar.setHideBubble(true);
        this.mMosaicDataList = new ArrayList(this.mSdkProvider.list());
        SimpleRecyclerView simpleRecyclerView = (SimpleRecyclerView) view.findViewById(R.id.recycler_view);
        this.mRecyclerView = simpleRecyclerView;
        simpleRecyclerView.setEnableItemClickWhileSettling(true);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.editor_menu_mosaic_item_decoration);
        this.mRecyclerView.addItemDecoration(new EditorBlankDivider(dimensionPixelSize, dimensionPixelSize, getResources().getDimensionPixelSize(R.dimen.editor_menu_mosaic_item_decoration), 0, 0));
        MosaicAdapter mosaicAdapter = new MosaicAdapter(getActivity(), this.mMosaicDataList);
        this.mMosaicAdapter = mosaicAdapter;
        this.mRecyclerView.setAdapter(mosaicAdapter);
        this.mMosaicAdapter.setOnItemClickListener(this.mOnItemClickListener);
        BubbleSeekBar bubbleSeekBar2 = this.mSeekBar;
        bubbleSeekBar2.setCurrentProgress(bubbleSeekBar2.getMaxProgress() / 2);
        int round = Math.round((126 * this.mSeekBar.getCurrentProgress()) / 100.0f) + 35;
        this.mPaintPopupWindow.setPaintSize(round);
        getRenderFragment().setMosaicPaintSize(round);
        this.mMenuHeight = getResources().getDimensionPixelSize(R.dimen.photo_editor_menu_panel_height);
        setSelection(1);
    }

    @Override // androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        ScreenAdaptationHelper.updateBSBWidth(getContext(), this.mSeekBar);
    }

    public final void setSelection(int i) {
        this.mMosaicAdapter.setSelection(i);
        getRenderFragment().setMosaicData(this.mMosaicDataList.get(i));
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
    }
}
