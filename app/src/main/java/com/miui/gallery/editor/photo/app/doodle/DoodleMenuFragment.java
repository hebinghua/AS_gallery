package com.miui.gallery.editor.photo.app.doodle;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.MenuFragment;
import com.miui.gallery.editor.photo.app.doodle.DoodlePaintView;
import com.miui.gallery.editor.photo.app.menu.DoodleView;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.SdkProvider;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractDoodleFragment;
import com.miui.gallery.editor.photo.core.common.model.DoodleData;
import com.miui.gallery.editor.photo.widgets.ColorSelector.ColorSelectorView;
import com.miui.gallery.editor.photo.widgets.PaintSizePopupWindow;
import com.miui.gallery.editor.ui.view.EditorBlankDivider;
import com.miui.gallery.editor.utils.EditorOrientationHelper;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerView;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/* loaded from: classes2.dex */
public class DoodleMenuFragment extends MenuFragment<AbstractDoodleFragment, SdkProvider<DoodleData, AbstractDoodleFragment>> {
    public ColorSelectorView mColorSelectorView;
    public String[] mColors;
    public Runnable mDismissPaintPopupWindowRunnable;
    public List<DoodleData> mDoodleDataList;
    public DoodlePaintView mDoodlePaintView;
    public FrameLayout mDoodlePaintViewLayout;
    public DoodleAdapter mDooodleAdapter;
    public Handler mHandler;
    public int mMenuHeight;
    public OnItemClickListener mOnItemClickListener;
    public PaintSizePopupWindow mPaintPopupWindow;
    public View.OnClickListener mPaintViewSelectClickListener;
    public SimpleRecyclerView mRecyclerView;

    /* renamed from: $r8$lambda$ZYhwBrTucBO1-uh6mRfjH9HzU7Y */
    public static /* synthetic */ void m757$r8$lambda$ZYhwBrTucBO1uh6mRfjH9HzU7Y(DoodleMenuFragment doodleMenuFragment, View view) {
        doodleMenuFragment.lambda$new$1(view);
    }

    /* renamed from: $r8$lambda$a5SYHNNODc858LVdwYAo-XvT608 */
    public static /* synthetic */ void m758$r8$lambda$a5SYHNNODc858LVdwYAoXvT608(DoodleMenuFragment doodleMenuFragment) {
        doodleMenuFragment.lambda$new$0();
    }

    public DoodleMenuFragment() {
        super(Effect.DOODLE);
        this.mOnItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.editor.photo.app.doodle.DoodleMenuFragment.2
            {
                DoodleMenuFragment.this = this;
            }

            @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
            public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
                ((SimpleRecyclerView) recyclerView).setSpringEnabled(false);
                recyclerView.smoothScrollToPosition(i);
                ((AbstractDoodleFragment) DoodleMenuFragment.this.getRenderFragment()).setDoodleData((DoodleData) DoodleMenuFragment.this.mDoodleDataList.get(i));
                if (i == DoodleMenuFragment.this.mDooodleAdapter.getSelection()) {
                    return false;
                }
                DoodleMenuFragment.this.mDooodleAdapter.setSelection(i);
                DoodleMenuFragment.this.mDooodleAdapter.notifyDataSetChanged();
                return true;
            }
        };
        this.mDismissPaintPopupWindowRunnable = new Runnable() { // from class: com.miui.gallery.editor.photo.app.doodle.DoodleMenuFragment$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                DoodleMenuFragment.m758$r8$lambda$a5SYHNNODc858LVdwYAoXvT608(DoodleMenuFragment.this);
            }
        };
        this.mPaintViewSelectClickListener = new View.OnClickListener() { // from class: com.miui.gallery.editor.photo.app.doodle.DoodleMenuFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                DoodleMenuFragment.m757$r8$lambda$ZYhwBrTucBO1uh6mRfjH9HzU7Y(DoodleMenuFragment.this, view);
            }
        };
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return new DoodleView(viewGroup.getContext());
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, com.miui.gallery.editor.utils.LayoutOrientationTracker.OnLayoutOrientationChangeListener
    public void onLayoutOrientationChange() {
        super.onLayoutOrientationChange();
        EditorOrientationHelper.copyLayoutParams(new DoodleView(getContext()), getView(), true);
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mDoodlePaintViewLayout = (FrameLayout) view.findViewById(R.id.doodle_paint_select_layout);
        DoodlePaintView doodlePaintView = (DoodlePaintView) view.findViewById(R.id.doodle_paint_select);
        this.mDoodlePaintView = doodlePaintView;
        doodlePaintView.setOuterColor(getResources().getColor(R.color.doodle_paint_out_color));
        this.mDoodlePaintViewLayout.setOnClickListener(this.mPaintViewSelectClickListener);
        FolmeUtil.setDefaultTouchAnim(this.mDoodlePaintViewLayout, null, true);
        SimpleRecyclerView simpleRecyclerView = (SimpleRecyclerView) view.findViewById(R.id.recycler_view);
        this.mRecyclerView = simpleRecyclerView;
        simpleRecyclerView.setEnableItemClickWhileSettling(true);
        this.mPaintPopupWindow = new PaintSizePopupWindow(getActivity());
        this.mColorSelectorView = (ColorSelectorView) view.findViewById(R.id.color_selector_view);
        String[] stringArray = getResources().getStringArray(R.array.doodle_color_selector_array);
        this.mColors = stringArray;
        this.mColorSelectorView.init(stringArray);
        this.mColorSelectorView.setOnItemClickListener(new OnItemClickListener() { // from class: com.miui.gallery.editor.photo.app.doodle.DoodleMenuFragment.1
            {
                DoodleMenuFragment.this = this;
            }

            @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
            public boolean OnItemClick(RecyclerView recyclerView, View view2, int i) {
                int parseColor = Color.parseColor(DoodleMenuFragment.this.mColors[i]);
                if (DoodleMenuFragment.this.mColors[i].equals("#3D3D3D")) {
                    ((AbstractDoodleFragment) DoodleMenuFragment.this.getRenderFragment()).setColor(-16777216);
                } else {
                    ((AbstractDoodleFragment) DoodleMenuFragment.this.getRenderFragment()).setColor(parseColor);
                }
                DoodleMenuFragment.this.mColorSelectorView.setSelection(i, true);
                DoodleMenuFragment.this.mDoodlePaintView.setColor(parseColor);
                HashMap hashMap = new HashMap();
                hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, Integer.toHexString(parseColor));
                DoodleMenuFragment.this.getHostAbility().sample("select_doodle_color", hashMap);
                return true;
            }
        });
        String[] strArr = this.mColors;
        if (strArr.length >= 2) {
            int parseColor = Color.parseColor(strArr[2]);
            this.mColorSelectorView.setSelectionWithoutMove(2);
            getRenderFragment().setColor(parseColor);
            this.mDoodlePaintView.setColor(parseColor);
        }
        initRecyclerView();
        this.mMenuHeight = getResources().getDimensionPixelSize(R.dimen.photo_editor_menu_panel_height);
    }

    public final void initRecyclerView() {
        this.mRecyclerView.addItemDecoration(new EditorBlankDivider(getResources().getDimensionPixelSize(R.dimen.editor_menu_common_content_item_gap)));
        this.mDoodleDataList = new ArrayList(this.mSdkProvider.list());
        DoodleAdapter doodleAdapter = new DoodleAdapter(getActivity(), this.mDoodleDataList);
        this.mDooodleAdapter = doodleAdapter;
        this.mRecyclerView.setAdapter(doodleAdapter);
        this.mDooodleAdapter.setOnItemClickListener(this.mOnItemClickListener);
        this.mDooodleAdapter.setSelection(0);
        this.mDooodleAdapter.notifyDataSetChanged();
    }

    public /* synthetic */ void lambda$new$0() {
        this.mPaintPopupWindow.dismiss();
    }

    public /* synthetic */ void lambda$new$1(View view) {
        this.mDoodlePaintView.updateInnerRadiusPercent();
        DoodlePaintView.PaintType paintType = this.mDoodlePaintView.getPaintType();
        float f = paintType.paintSize;
        this.mDoodlePaintView.setContentDescription(paintType.talkbackName);
        float f2 = f * getResources().getDisplayMetrics().density;
        PaintSizePopupWindow paintSizePopupWindow = this.mPaintPopupWindow;
        if (paintSizePopupWindow != null && !paintSizePopupWindow.isShowing()) {
            if (isLayoutPortrait()) {
                this.mPaintPopupWindow.showAtLocation(this.mRecyclerView, 17, 0, (-this.mMenuHeight) / 2);
            } else {
                this.mPaintPopupWindow.showAtLocation(this.mRecyclerView, 17, (-this.mMenuHeight) / 2, 0);
            }
        }
        PaintSizePopupWindow paintSizePopupWindow2 = this.mPaintPopupWindow;
        if (paintSizePopupWindow2 != null) {
            paintSizePopupWindow2.setPaintTypeWithAnimation(getContext(), paintType);
        }
        if (this.mHandler == null) {
            this.mHandler = new Handler();
        }
        this.mHandler.removeCallbacks(this.mDismissPaintPopupWindowRunnable);
        this.mHandler.postDelayed(this.mDismissPaintPopupWindowRunnable, 1500L);
        getRenderFragment().setPaintSize(f2);
        HashMap hashMap = new HashMap();
        hashMap.put(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, Float.toString(f2));
        getHostAbility().sample("select_doodle_size", hashMap);
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.removeCallbacks(this.mDismissPaintPopupWindowRunnable);
        }
        PaintSizePopupWindow paintSizePopupWindow = this.mPaintPopupWindow;
        if (paintSizePopupWindow == null || !paintSizePopupWindow.isShowing()) {
            return;
        }
        this.mPaintPopupWindow.dismiss();
    }
}
