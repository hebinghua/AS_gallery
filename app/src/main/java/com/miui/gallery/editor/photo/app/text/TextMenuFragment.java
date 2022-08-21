package com.miui.gallery.editor.photo.app.text;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.CategoryAdapter;
import com.miui.gallery.editor.photo.app.MenuFragment;
import com.miui.gallery.editor.photo.app.menu.TextWaterMarkView;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.RenderFragment;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment;
import com.miui.gallery.editor.photo.core.common.model.TextData;
import com.miui.gallery.editor.photo.core.common.provider.AbstractTextProvider;
import com.miui.gallery.editor.photo.core.imports.text.TextCategoryData;
import com.miui.gallery.editor.photo.core.imports.text.TextConfig;
import com.miui.gallery.editor.ui.view.EditorBlankDivider;
import com.miui.gallery.editor.utils.EditorOrientationHelper;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class TextMenuFragment extends MenuFragment<AbstractEffectFragment, AbstractTextProvider> {
    public TextBubbleAdapter mAdapter;
    public List<TextData> mBubbleList;
    public RenderFragment.Callbacks mCallback;
    public List<TextCategoryData> mCategories;
    public List<TextData> mFestivalList;
    public boolean mIsItemClick;
    public RecyclerView.OnScrollListener mOnScrollListener;
    public OnItemClickListener mOnTextCategoryItemClickLister;
    public List<TextData> mSceneList;
    public SimpleRecyclerView mSimpleRecyclerView;
    public List<TextData> mSpotList;
    public CategoryAdapter mTextCategoryAdapter;
    public SimpleRecyclerView mTextCategoryRecyclerView;
    public Map<Integer, List<? extends TextData>> mTextDataMap;
    public OnItemClickListener mTextItemClickListener;
    public List<TextData> mWaterList;
    public List<TextData> mWaterMarkDataList;

    public TextMenuFragment() {
        super(Effect.TEXT);
        this.mTextDataMap = new HashMap();
        this.mOnScrollListener = new RecyclerView.OnScrollListener() { // from class: com.miui.gallery.editor.photo.app.text.TextMenuFragment.1
            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrollStateChanged(RecyclerView recyclerView, int i) {
                super.onScrollStateChanged(recyclerView, i);
                if (i != 0) {
                    return;
                }
                if (TextMenuFragment.this.mIsItemClick) {
                    TextMenuFragment.this.mIsItemClick = false;
                    return;
                }
                TextMenuFragment.this.setSelectedTabPosition(((LinearLayoutManager) TextMenuFragment.this.mSimpleRecyclerView.getLayoutManager()).findFirstVisibleItemPosition());
            }

            @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                super.onScrolled(recyclerView, i, i2);
            }
        };
        this.mOnTextCategoryItemClickLister = new OnItemClickListener() { // from class: com.miui.gallery.editor.photo.app.text.TextMenuFragment.2
            @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
            public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
                ((SimpleRecyclerView) recyclerView).setSpringEnabled(false);
                recyclerView.smoothScrollToPosition(i);
                if (TextMenuFragment.this.mTextCategoryAdapter.getSelection() == i) {
                    return true;
                }
                TextMenuFragment.this.mTextCategoryAdapter.setSelection(i);
                TextMenuFragment.this.setSelectedTabWithOffset(i);
                return true;
            }
        };
        this.mTextItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.editor.photo.app.text.TextMenuFragment.3
            @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
            public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
                ((SimpleRecyclerView) recyclerView).setSpringEnabled(false);
                TextData itemData = TextMenuFragment.this.mAdapter.getItemData(i);
                AbstractEffectFragment abstractEffectFragment = (AbstractEffectFragment) TextMenuFragment.this.getRenderFragment();
                if (!(itemData instanceof TextConfig) || !((TextConfig) itemData).isSignature()) {
                    ((AbstractEffectFragment) TextMenuFragment.this.getRenderFragment()).add(itemData, Integer.valueOf(i));
                    TextMenuFragment.this.mAdapter.setSelection(i);
                } else {
                    abstractEffectFragment.setCallbacks(TextMenuFragment.this.mCallback);
                    abstractEffectFragment.addSignature(itemData, Integer.valueOf(i));
                    SamplingStatHelper.recordCountEvent("photo_editor", "signature_click");
                }
                TextMenuFragment.this.setSelectedTabOnClick(recyclerView, i);
                return true;
            }
        };
        this.mCallback = new RenderFragment.Callbacks() { // from class: com.miui.gallery.editor.photo.app.text.TextMenuFragment.4
            @Override // com.miui.gallery.editor.photo.core.RenderFragment.Callbacks
            public void onSelected(int i) {
                if (i == -1) {
                    return;
                }
                TextMenuFragment.this.mAdapter.setSelection(i);
            }
        };
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        TextWaterMarkView textWaterMarkView = new TextWaterMarkView(viewGroup.getContext());
        this.mSimpleRecyclerView = (SimpleRecyclerView) textWaterMarkView.findViewById(R.id.recycler_view);
        this.mTextCategoryRecyclerView = (SimpleRecyclerView) textWaterMarkView.findViewById(R.id.filter_sky_type_list);
        return textWaterMarkView;
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, com.miui.gallery.editor.utils.LayoutOrientationTracker.OnLayoutOrientationChangeListener
    public void onLayoutOrientationChange() {
        super.onLayoutOrientationChange();
        EditorOrientationHelper.copyLayoutParams(new TextWaterMarkView(getContext()), getView(), true);
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mCategories = TextCategoryData.getCategoryData();
        this.mSimpleRecyclerView.setEnableItemClickWhileSettling(true);
        this.mWaterMarkDataList = initTextDataList(view);
        TextBubbleAdapter textBubbleAdapter = new TextBubbleAdapter(getActivity(), this.mWaterMarkDataList, -1);
        this.mAdapter = textBubbleAdapter;
        textBubbleAdapter.setOnItemClickListener(this.mTextItemClickListener);
        this.mSimpleRecyclerView.setAdapter(this.mAdapter);
        this.mSimpleRecyclerView.addItemDecoration(new EditorBlankDivider(getResources(), R.dimen.editor_menu_text_view_pager_horizontal_interval));
        this.mSimpleRecyclerView.addOnScrollListener(this.mOnScrollListener);
        this.mAdapter.setSelection(2);
        this.mTextCategoryRecyclerView.setEnableItemClickWhileSettling(true);
        CategoryAdapter categoryAdapter = new CategoryAdapter(view.getContext(), this.mCategories);
        this.mTextCategoryAdapter = categoryAdapter;
        categoryAdapter.setOnItemClickListener(this.mOnTextCategoryItemClickLister);
        this.mTextCategoryAdapter.setSelection(0);
        this.mTextCategoryRecyclerView.setAdapter(this.mTextCategoryAdapter);
        getRenderFragment().setCallbacks(this.mCallback);
        AnimParams build = new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build();
        FolmeUtil.setCustomTouchAnim(this.mSaveBtn, build, null, null, null, true);
        FolmeUtil.setCustomTouchAnim(this.mDiscardBtn, build, null, null, null, true);
    }

    public final List<TextData> initTextDataList(View view) {
        ArrayList arrayList = new ArrayList();
        List list = ((AbstractTextProvider) this.mSdkProvider).list();
        this.mBubbleList = list;
        int size = list.size();
        if (size > 0) {
            this.mBubbleList.get(size - 1).setLast(true);
        }
        arrayList.addAll(this.mBubbleList);
        this.mTextDataMap.put(1, this.mBubbleList);
        List<TextData> listWatermark = ((AbstractTextProvider) this.mSdkProvider).listWatermark();
        this.mWaterList = listWatermark;
        int size2 = listWatermark.size();
        if (size2 > 0) {
            this.mWaterList.get(size2 - 1).setLast(true);
        }
        arrayList.addAll(this.mWaterList);
        this.mTextDataMap.put(2, this.mWaterList);
        if (!BaseBuildUtil.isInternational()) {
            List<TextData> listSpot = ((AbstractTextProvider) this.mSdkProvider).listSpot();
            this.mSpotList = listSpot;
            int size3 = listSpot.size();
            if (size3 > 0) {
                this.mSpotList.get(size3 - 1).setLast(true);
            }
            arrayList.addAll(this.mSpotList);
            this.mTextDataMap.put(3, this.mSpotList);
            List<TextData> listFestival = ((AbstractTextProvider) this.mSdkProvider).listFestival();
            this.mFestivalList = listFestival;
            int size4 = listFestival.size();
            if (size4 > 0) {
                this.mFestivalList.get(size4 - 1).setLast(true);
            }
            arrayList.addAll(this.mFestivalList);
            this.mTextDataMap.put(4, this.mFestivalList);
            List<TextData> listScene = ((AbstractTextProvider) this.mSdkProvider).listScene();
            this.mSceneList = listScene;
            int size5 = listScene.size();
            if (size5 > 0) {
                this.mSceneList.get(size5 - 1).setLast(true);
            }
            arrayList.addAll(this.mSceneList);
            this.mTextDataMap.put(5, this.mSceneList);
            arrayList.addAll(((AbstractTextProvider) this.mSdkProvider).listCity());
            this.mTextDataMap.put(6, this.mSpotList);
        }
        return arrayList;
    }

    public final void setSelectedTabWithOffset(int i) {
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.editor_menu_filter_category_gap);
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            List<? extends TextData> list = this.mTextDataMap.get(Integer.valueOf(this.mCategories.get(i3).id));
            if (list != null) {
                i2 += list.size();
            }
        }
        ((LinearLayoutManager) this.mSimpleRecyclerView.getLayoutManager()).scrollToPositionWithOffset(i2, dimensionPixelSize);
    }

    public final void setSelectedTabOnClick(RecyclerView recyclerView, int i) {
        this.mIsItemClick = true;
        recyclerView.smoothScrollToPosition(i);
        setSelectedTabPosition(i);
    }

    public final void setSelectedTabPosition(int i) {
        int i2 = 0;
        for (int i3 = 0; i3 < this.mCategories.size(); i3++) {
            List<? extends TextData> list = this.mTextDataMap.get(Integer.valueOf(this.mCategories.get(i3).id));
            if (list != null && i < (i2 = i2 + list.size())) {
                this.mTextCategoryAdapter.setSelection(i3);
                this.mTextCategoryRecyclerView.setSpringEnabled(false);
                this.mTextCategoryRecyclerView.smoothScrollToPosition(i3);
                return;
            }
        }
    }
}
