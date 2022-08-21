package com.miui.gallery.editor.photo.app.remover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.baidu.location.BDLocation;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.MenuFragment;
import com.miui.gallery.editor.photo.app.menu.RemoverView;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.SdkProvider;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractRemoverFragment;
import com.miui.gallery.editor.photo.core.common.model.RemoverData;
import com.miui.gallery.editor.photo.widgets.PaintSizePopupWindow;
import com.miui.gallery.editor.photo.widgets.recyclerview.Selectable;
import com.miui.gallery.editor.ui.view.BubbleSeekBar;
import com.miui.gallery.editor.ui.view.EditorBlankDivider;
import com.miui.gallery.editor.utils.EditorOrientationHelper;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerView;
import java.util.ArrayList;
import java.util.List;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes2.dex */
public class RemoverMenuFragment extends MenuFragment<AbstractRemoverFragment, SdkProvider<RemoverData, AbstractRemoverFragment>> {
    public RemoverAdapter mAdapter;
    public int mCurrentType;
    public List<RemoverData> mDataList;
    public OnItemClickListener mOnItemClickListener;
    public PaintSizePopupWindow mPaintSizePopupWindow;
    public ProgressDialog mProgressDialog;
    public SimpleRecyclerView mRecyclerView;
    public BubbleSeekBar mSeekBar;
    public BubbleSeekBar.ProgressListener mSeekBarChangeListener;

    public RemoverMenuFragment() {
        super(Effect.REMOVER);
        this.mCurrentType = 0;
        this.mOnItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.editor.photo.app.remover.RemoverMenuFragment.1
            @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
            public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
                ((SimpleRecyclerView) recyclerView).setSpringEnabled(false);
                if (RemoverMenuFragment.this.mAdapter.getSelection() == i) {
                    return false;
                }
                RemoverData removerData = (RemoverData) RemoverMenuFragment.this.mDataList.get(i);
                RemoverMenuFragment.this.mCurrentType = removerData.mType;
                ((AbstractRemoverFragment) RemoverMenuFragment.this.getRenderFragment()).setRemoverData(removerData);
                RemoverMenuFragment.this.mAdapter.setSelection(i);
                RemoverMenuFragment removerMenuFragment = RemoverMenuFragment.this;
                ((AbstractRemoverFragment) RemoverMenuFragment.this.getRenderFragment()).setPaintSize(removerMenuFragment.getPaintSizeByProgress((int) removerMenuFragment.mSeekBar.getCurrentProgress()));
                return true;
            }
        };
        this.mSeekBarChangeListener = new BubbleSeekBar.ProgressListener() { // from class: com.miui.gallery.editor.photo.app.remover.RemoverMenuFragment.2
            @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
            public void onStartTrackingTouch(BubbleSeekBar bubbleSeekBar) {
            }

            @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
            public void onProgressStartChange(BubbleSeekBar bubbleSeekBar, int i) {
                RemoverMenuFragment.this.mPaintSizePopupWindow.setPaintSize(RemoverMenuFragment.this.getPaintSizeByProgress((int) bubbleSeekBar.getCurrentProgress()));
                if (RemoverMenuFragment.this.isLayoutPortrait()) {
                    RemoverMenuFragment.this.mPaintSizePopupWindow.showAtLocation(RemoverMenuFragment.this.mRecyclerView, 17, 0, (-RemoverMenuFragment.this.getResources().getDimensionPixelSize(R.dimen.photo_editor_menu_panel_height)) / 2);
                } else {
                    RemoverMenuFragment.this.mPaintSizePopupWindow.showAtLocation(RemoverMenuFragment.this.mRecyclerView, 17, (-RemoverMenuFragment.this.getResources().getDimensionPixelSize(R.dimen.photo_editor_menu_panel_height)) / 2, 0);
                }
            }

            @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int i) {
                if (i == 0 || i == 100) {
                    LinearMotorHelper.performHapticFeedback(bubbleSeekBar, LinearMotorHelper.HAPTIC_MESH_HEAVY);
                }
                int paintSizeByProgress = RemoverMenuFragment.this.getPaintSizeByProgress(i);
                RemoverMenuFragment.this.mPaintSizePopupWindow.setPaintSize(paintSizeByProgress);
                ((AbstractRemoverFragment) RemoverMenuFragment.this.getRenderFragment()).setPaintSize(paintSizeByProgress);
            }

            @Override // com.miui.gallery.editor.ui.view.BubbleSeekBar.ProgressListener
            public void onStopTrackingTouch(BubbleSeekBar bubbleSeekBar) {
                RemoverMenuFragment.this.mPaintSizePopupWindow.dismiss();
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
        EditorOrientationHelper.copyLayoutParams(new RemoverView(getContext()), getView(), true);
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        SimpleRecyclerView simpleRecyclerView = (SimpleRecyclerView) view.findViewById(R.id.recycler_view);
        this.mRecyclerView = simpleRecyclerView;
        simpleRecyclerView.setEnableItemClickWhileSettling(true);
        this.mAdapter = new RemoverAdapter(getActivity(), this.mDataList, new Selectable.Selector(getResources().getColor(R.color.photo_editor_highlight_color)));
        this.mRecyclerView.addItemDecoration(new EditorBlankDivider(getResources(), R.dimen.editor_remover_item_gap));
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mAdapter.setOnItemClickListener(this.mOnItemClickListener);
        this.mAdapter.setSelection(0);
        this.mPaintSizePopupWindow = new PaintSizePopupWindow(getActivity());
        BubbleSeekBar bubbleSeekBar = (BubbleSeekBar) view.findViewById(R.id.seek_bar);
        this.mSeekBar = bubbleSeekBar;
        bubbleSeekBar.setCurrentProgress(bubbleSeekBar.getMaxProgress() / 2);
        this.mSeekBar.setProgressListener(this.mSeekBarChangeListener);
        this.mSeekBar.setHideBubble(true);
        getRenderFragment().setPaintSize(this.mSeekBar.getCurrentProgress() + 35.0f);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return new RemoverView(viewGroup.getContext());
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
    public void showProcessDialog() {
        ProgressDialog genProgressDialog = genProgressDialog(getActivity().getString(R.string.remover_menu_processing));
        this.mProgressDialog = genProgressDialog;
        genProgressDialog.show();
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment
    public void hideProcessDialog() {
        ProgressDialog progressDialog = this.mProgressDialog;
        if (progressDialog == null || !progressDialog.isShowing()) {
            return;
        }
        this.mProgressDialog.dismiss();
    }
}
