package com.miui.gallery.magic.special.effects.image.menu;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import ch.qos.logback.core.pattern.parser.Parser;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.base.BaseFragment;
import com.miui.gallery.magic.util.MagicSampler;
import com.miui.gallery.magic.util.MagicSamplerSingleton;
import com.miui.gallery.magic.widget.scroll.ScrollLinearLayoutManager;
import com.miui.gallery.ui.CenterSmoothScrollerController;
import com.miui.gallery.widget.recyclerview.Adapter;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerView;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class MagicMenuFragment extends BaseFragment<MagicMenuPresenter, IMenu$VP> {
    public String lastFilterName = "";
    public SimpleRecyclerView mRecycle;

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.magic.base.BaseFragment
    /* renamed from: initContract */
    public IMenu$VP mo1066initContract() {
        return new IMenu$VP() { // from class: com.miui.gallery.magic.special.effects.image.menu.MagicMenuFragment.1
            @Override // com.miui.gallery.magic.special.effects.image.menu.IMenu$VP
            public void setAdapter(Adapter adapter) {
                MagicMenuFragment.this.mRecycle.setAdapter(adapter);
            }

            @Override // com.miui.gallery.magic.special.effects.image.menu.IMenu$VP
            public void scrollTo(int i) {
                MagicMenuFragment.this.mRecycle.setSpringEnabled(false);
                MagicMenuFragment.this.mRecycle.smoothScrollToPosition(i);
                String str = i != 1 ? i != 2 ? i != 3 ? i != 4 ? i != 5 ? "素描" : "故障风" : "雾窗" : "百叶窗" : "RGB" : "水彩";
                HashMap hashMap = new HashMap();
                hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, str);
                MagicSamplerSingleton.getInstance().setSelectArtName(str);
                MagicSampler.getInstance().recordCategory("art", "name", hashMap);
                if (!TextUtils.isEmpty(MagicMenuFragment.this.lastFilterName)) {
                    HashMap hashMap2 = new HashMap();
                    hashMap2.put("oldType", MagicMenuFragment.this.lastFilterName);
                    hashMap2.put("newType", str);
                    MagicSampler.getInstance().recordCategory("art", Parser.REPLACE_CONVERTER_WORD, hashMap2);
                }
                MagicMenuFragment.this.lastFilterName = str;
            }

            @Override // com.miui.gallery.magic.special.effects.image.menu.IMenu$VP
            public void loadListData() {
                ((MagicMenuPresenter) MagicMenuFragment.this.mPresenter).getContract().loadListData();
            }

            @Override // com.miui.gallery.magic.special.effects.image.menu.IMenu$VP
            public void loadFinish(Bitmap bitmap) {
                ((MagicMenuPresenter) MagicMenuFragment.this.mPresenter).getContract().loadFinish(bitmap);
            }

            @Override // com.miui.gallery.magic.special.effects.image.menu.IMenu$VP
            public boolean getNotFace() {
                return ((MagicMenuPresenter) MagicMenuFragment.this.mPresenter).getContract().getNotFace();
            }
        };
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public void initView() {
        this.mRecycle = (SimpleRecyclerView) findViewById(R$id.magic_recyclerview);
        ScrollLinearLayoutManager scrollLinearLayoutManager = new ScrollLinearLayoutManager(getActivity());
        scrollLinearLayoutManager.setSmoothScroller(new CenterSmoothScrollerController(getActivity()));
        scrollLinearLayoutManager.setOrientation(0);
        this.mRecycle.setLayoutManager(scrollLinearLayoutManager);
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public void initData() {
        getContract().loadListData();
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public MagicMenuPresenter getPresenterInstance() {
        return new MagicMenuPresenter();
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public int getLayoutId() {
        return R$layout.ts_magic_special_effects_menu;
    }
}
