package com.miui.gallery.editor.photo.app.beautify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.MenuFragment;
import com.miui.gallery.editor.photo.app.menu.BeautifyView;
import com.miui.gallery.editor.photo.core.Effect;
import com.miui.gallery.editor.photo.core.Metadata;
import com.miui.gallery.editor.photo.core.SdkProvider;
import com.miui.gallery.editor.photo.core.common.fragment.AbstractEffectFragment;
import com.miui.gallery.editor.photo.core.common.model.BeautifyData;
import com.miui.gallery.ui.CenterSmoothScrollerController;
import com.miui.gallery.widget.recyclerview.BlankDivider;
import com.miui.gallery.widget.recyclerview.CustomScrollerLinearLayoutManager;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerView;
import java.util.ArrayList;
import java.util.List;

@Deprecated
/* loaded from: classes2.dex */
public class BeautifyFragment extends MenuFragment<AbstractEffectFragment, SdkProvider<BeautifyData, AbstractEffectFragment>> {
    public BeautifyAdapter mAdapter;
    public List<BeautifyData> mDataList;
    public OnItemClickListener mOnItemClickListener;
    public SimpleRecyclerView mRecyclerView;
    public int[] mValues;

    public BeautifyFragment() {
        super(Effect.BEAUTIFY);
        this.mOnItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.editor.photo.app.beautify.BeautifyFragment.1
            @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
            public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
                ((SimpleRecyclerView) recyclerView).setSpringEnabled(false);
                if (BeautifyFragment.this.mAdapter.getSelection() == i) {
                    return true;
                }
                BeautifyFragment beautifyFragment = BeautifyFragment.this;
                beautifyFragment.performItemSelect(i, beautifyFragment.mValues[i], false);
                return true;
            }
        };
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, com.miui.gallery.editor.photo.app.EditorFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mDataList = new ArrayList(this.mSdkProvider.list());
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return new BeautifyView(viewGroup.getContext());
    }

    @Override // com.miui.gallery.editor.photo.app.MenuFragment, androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        this.mValues = new int[this.mDataList.size()];
        this.mRecyclerView = (SimpleRecyclerView) view.findViewById(R.id.recycler_view);
        this.mAdapter = new BeautifyAdapter(getActivity(), this.mDataList, null);
        this.mRecyclerView.setEnableItemClickWhileSettling(true);
        this.mRecyclerView.setAdapter(this.mAdapter);
        this.mRecyclerView.addItemDecoration(new BlankDivider(getResources().getDimensionPixelSize(R.dimen.editor_menu_common_content_item_gap)));
        CustomScrollerLinearLayoutManager customScrollerLinearLayoutManager = new CustomScrollerLinearLayoutManager(getActivity());
        customScrollerLinearLayoutManager.setSmoothScroller(new CenterSmoothScrollerController(getActivity()));
        customScrollerLinearLayoutManager.setOrientation(0);
        this.mRecyclerView.setLayoutManager(customScrollerLinearLayoutManager);
        this.mAdapter.setOnItemClickListener(this.mOnItemClickListener);
        performItemSelect(1, 0, false);
    }

    public final void doRender(Metadata metadata, Object obj) {
        getRenderFragment().clear();
        getRenderFragment().add(metadata, obj);
        getRenderFragment().render();
    }

    public final void performItemSelect(int i, int i2, boolean z) {
        this.mRecyclerView.smoothScrollToPosition(i);
        BeautifyData beautifyData = this.mDataList.get(i);
        this.mAdapter.setSelection(i);
        if (!z) {
            doRender(beautifyData, Integer.valueOf(i2));
        }
    }
}
