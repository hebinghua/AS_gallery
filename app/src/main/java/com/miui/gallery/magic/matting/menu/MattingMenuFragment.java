package com.miui.gallery.magic.matting.menu;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.RadioGroup;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.base.BaseFragment;
import com.miui.gallery.magic.fetch.MattingResourceFetcher;
import com.miui.gallery.magic.matting.bean.BackgroundItem;
import com.miui.gallery.magic.widget.PaintSelectorPanel;
import com.miui.gallery.magic.widget.scroll.ScrollLinearLayoutManager;
import com.miui.gallery.ui.CenterSmoothScrollerController;
import com.miui.gallery.widget.recyclerview.Adapter;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerView;

/* loaded from: classes2.dex */
public class MattingMenuFragment extends BaseFragment<MattingMenuPresenter, IMenu$VP> {
    public View mPaintColor;
    public PaintSelectorPanel mPaintSelector;
    public RadioGroup mRadioGroup;
    public SimpleRecyclerView mRecyclerview;

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.magic.base.BaseFragment
    /* renamed from: initContract */
    public IMenu$VP mo1066initContract() {
        return new IMenu$VP() { // from class: com.miui.gallery.magic.matting.menu.MattingMenuFragment.1
            @Override // com.miui.gallery.magic.matting.menu.IMenu$VP
            public void setStrokeWidthToProgress(int i) {
            }

            @Override // com.miui.gallery.magic.matting.menu.IMenu$VP
            public void setAdapter(Adapter adapter) {
                MattingMenuFragment.this.mRecyclerview.setAdapter(adapter);
            }

            @Override // com.miui.gallery.magic.matting.menu.IMenu$VP
            public void scrollTo(int i) {
                MattingMenuFragment.this.mRecyclerview.setSpringEnabled(false);
                MattingMenuFragment.this.mRecyclerview.smoothScrollToPosition(i);
            }

            @Override // com.miui.gallery.magic.matting.menu.IMenu$VP
            public void initListData() {
                ((MattingMenuPresenter) MattingMenuFragment.this.mPresenter).getContract().initListData();
            }

            @Override // com.miui.gallery.magic.matting.menu.IMenu$VP
            public void onPaintColorSelected(int i) {
                ((MattingMenuPresenter) MattingMenuFragment.this.mPresenter).getContract().onPaintColorSelected(i);
            }

            @Override // com.miui.gallery.magic.matting.menu.IMenu$VP
            public void onPaintSizeSelected(int i) {
                ((MattingMenuPresenter) MattingMenuFragment.this.mPresenter).getContract().onPaintSizeSelected(i);
            }

            @Override // com.miui.gallery.magic.matting.menu.IMenu$VP
            public void showPaintSelect(boolean z) {
                if (MattingMenuFragment.this.mPaintSelector.getVisibility() == 8 && z) {
                    MattingMenuFragment.this.mPaintSelector.setVisibility(0);
                }
                if (MattingMenuFragment.this.mPaintSelector.getVisibility() != 0 || z) {
                    return;
                }
                MattingMenuFragment.this.mPaintSelector.setVisibility(8);
            }

            @Override // com.miui.gallery.magic.matting.menu.IMenu$VP
            public BackgroundItem getBackgroundItem() {
                return ((MattingMenuPresenter) MattingMenuFragment.this.mPresenter).getContract().getBackgroundItem();
            }

            @Override // com.miui.gallery.magic.matting.menu.IMenu$VP
            public Bitmap getBackgroundBitmap(String str) {
                return ((MattingMenuPresenter) MattingMenuFragment.this.mPresenter).getContract().getBackgroundBitmap(str);
            }

            @Override // com.miui.gallery.magic.matting.menu.IMenu$VP
            public void scrollToPosition(int i) {
                MattingMenuFragment.this.mRecyclerview.scrollToPosition(i);
            }

            @Override // com.miui.gallery.magic.matting.menu.IMenu$VP
            public void setSelectBackgroundIndex(int i) {
                ((MattingMenuPresenter) MattingMenuFragment.this.mPresenter).getContract().setSelectBackgroundIndex(i);
            }
        };
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public void initView() {
        SimpleRecyclerView simpleRecyclerView = (SimpleRecyclerView) findViewById(R$id.magic_matting_recyclerview);
        this.mRecyclerview = simpleRecyclerView;
        simpleRecyclerView.setEnableItemClickWhileSettling(true);
        this.mRadioGroup = (RadioGroup) findViewById(R$id.magic_tab_radio_group);
        this.mPaintSelector = (PaintSelectorPanel) findViewById(R$id.magic_matting_paint);
        this.mPaintColor = findViewById(R$id.magic_paint_size_image);
        this.mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.miui.gallery.magic.matting.menu.MattingMenuFragment.2
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
            }
        });
        this.mPaintSelector.setOnPaintSelectorListener(new PaintSelectorPanel.OnPaintSelectorListener() { // from class: com.miui.gallery.magic.matting.menu.MattingMenuFragment.3
            @Override // com.miui.gallery.magic.widget.PaintSelectorPanel.OnPaintSelectorListener
            public void onPaintColorSelected(int i) {
                MattingMenuFragment.this.getContract().onPaintColorSelected(i);
            }

            @Override // com.miui.gallery.magic.widget.PaintSelectorPanel.OnPaintSelectorListener
            public void onPaintSizeSelected(int i) {
                MattingMenuFragment.this.getContract().onPaintSizeSelected(i);
            }
        });
        ScrollLinearLayoutManager scrollLinearLayoutManager = new ScrollLinearLayoutManager(getActivity());
        scrollLinearLayoutManager.setSmoothScroller(new CenterSmoothScrollerController(getActivity()));
        scrollLinearLayoutManager.setOrientation(0);
        this.mRecyclerview.setLayoutManager(scrollLinearLayoutManager);
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public void initData() {
        getContract().initListData();
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public MattingMenuPresenter getPresenterInstance() {
        return new MattingMenuPresenter();
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public int getLayoutId() {
        return R$layout.ts_magic_matting_menu;
    }

    @Override // com.miui.gallery.magic.base.BaseFragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        MattingResourceFetcher.INSTANCE.cancelAll();
    }
}
