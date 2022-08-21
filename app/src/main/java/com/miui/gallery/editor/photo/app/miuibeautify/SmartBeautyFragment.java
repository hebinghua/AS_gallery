package com.miui.gallery.editor.photo.app.miuibeautify;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.filtersdk.beauty.BeautyProcessorManager;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.miuibeautify.SmartBeautyLevelItemAdapter;
import com.miui.gallery.widget.recyclerview.BlankDivider;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerViewNoSpring;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class SmartBeautyFragment extends ChildMenuFragment {
    public SmartBeautyLevelItemAdapter mBeautyLevelAdapter;
    public SimpleRecyclerViewNoSpring mLevelItemList;
    public boolean mListItemClickable = true;
    public OnItemClickListener mOnItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.editor.photo.app.miuibeautify.SmartBeautyFragment.1
        public int mPrePos = 0;

        @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
        public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
            if (i != this.mPrePos && SmartBeautyFragment.this.mListItemClickable) {
                this.mPrePos = i;
                SmartBeautyFragment.this.setBeautyParameterTable(BeautyProcessorManager.INSTANCE.getBeautyProcessor().getIntelligentLevelParams(i));
                SmartBeautyFragment.this.notifyBeautyParameterChanged();
                SmartBeautyFragment.this.mBeautyLevelAdapter.setSelection(i);
            }
            return true;
        }
    };

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_beauty_level, viewGroup, false);
        initView(inflate);
        return inflate;
    }

    public final void initView(View view) {
        this.mLevelItemList = (SimpleRecyclerViewNoSpring) view.findViewById(R.id.beauty_level_item_list);
        ArrayList arrayList = new ArrayList();
        arrayList.add(new SmartBeautyLevelItemAdapter.LevelItem(R.drawable.icon_level_0_n, R.drawable.icon_level_0_p));
        arrayList.add(new SmartBeautyLevelItemAdapter.LevelItem(R.drawable.icon_level_1_n, R.drawable.icon_level_1_p));
        arrayList.add(new SmartBeautyLevelItemAdapter.LevelItem(R.drawable.icon_level_2_n, R.drawable.icon_level_2_p));
        arrayList.add(new SmartBeautyLevelItemAdapter.LevelItem(R.drawable.icon_level_3_n, R.drawable.icon_level_3_p));
        arrayList.add(new SmartBeautyLevelItemAdapter.LevelItem(R.drawable.icon_level_4_n, R.drawable.icon_level_4_p));
        arrayList.add(new SmartBeautyLevelItemAdapter.LevelItem(R.drawable.icon_level_5_n, R.drawable.icon_level_5_p));
        SmartBeautyLevelItemAdapter smartBeautyLevelItemAdapter = new SmartBeautyLevelItemAdapter(getActivity(), arrayList);
        this.mBeautyLevelAdapter = smartBeautyLevelItemAdapter;
        this.mLevelItemList.setAdapter(smartBeautyLevelItemAdapter);
        this.mBeautyLevelAdapter.setOnItemClickListener(this.mOnItemClickListener);
        this.mBeautyLevelAdapter.notifyDataSetChanged();
        this.mCallbacks.changeTitle(getResources().getString(R.string.photo_editor_miui_beauty_menu_smart_beauty));
        this.mOnItemClickListener.OnItemClick(null, null, 3);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.editor_beauty_smart_start);
        this.mLevelItemList.addItemDecoration(new BlankDivider(dimensionPixelSize, dimensionPixelSize, getResources().getDimensionPixelSize(R.dimen.editor_beauty_smart_horizontal_interval), 0, 0));
    }

    @Override // com.miui.gallery.editor.photo.app.miuibeautify.ChildMenuFragment, com.miui.gallery.editor.photo.core.imports.miuibeauty.MiuiBeautyRenderFragment.OnBeautyProcessListener
    public void onBeautyProcessStart() {
        super.onBeautyProcessStart();
        this.mListItemClickable = false;
    }

    @Override // com.miui.gallery.editor.photo.app.miuibeautify.ChildMenuFragment, com.miui.gallery.editor.photo.core.imports.miuibeauty.MiuiBeautyRenderFragment.OnBeautyProcessListener
    public void onBeautyProcessEnd() {
        super.onBeautyProcessEnd();
        this.mListItemClickable = true;
    }
}
