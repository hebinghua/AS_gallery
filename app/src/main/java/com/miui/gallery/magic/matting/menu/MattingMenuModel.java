package com.miui.gallery.magic.matting.menu;

import android.content.res.Resources;
import com.miui.gallery.magic.R$array;
import com.miui.gallery.magic.R$string;
import com.miui.gallery.magic.base.BaseModel;
import com.miui.gallery.magic.fetch.MattingResourceFetcher;
import com.miui.gallery.magic.matting.adapter.BackgroundIconItem;
import com.miui.gallery.magic.matting.adapter.IconItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/* loaded from: classes2.dex */
public class MattingMenuModel extends BaseModel<MattingMenuPresenter, IMenu$M> {
    public ArrayList<IconItem> mBgList;

    public MattingMenuModel(MattingMenuPresenter mattingMenuPresenter) {
        super(mattingMenuPresenter);
    }

    public List<BackgroundIconItem> getBackgroundList() {
        String[] stringArray = ((MattingMenuPresenter) this.mPresenter).getActivity().getResources().getStringArray(R$array.acc_magic_matting_background_description);
        Resources resources = ((MattingMenuPresenter) this.mPresenter).getActivity().getResources();
        int i = R$string.background_picture;
        return Arrays.asList(new BackgroundIconItem("magic_recycler_clear_icon", true, "", 0L, ((MattingMenuPresenter) this.mPresenter).getActivity().getResources().getString(R$string.acc_bg_first_item_description), false, ""), new BackgroundIconItem("magic_recycler_matting_0", false, "", 0L, stringArray[0], false, ""), new BackgroundIconItem("magic_recycler_matting_1", false, "", 0L, stringArray[1], false, ""), new BackgroundIconItem("magic_recycler_matting_2", false, "matting_2", 14820408891146464L, resources.getString(i, 1), true, "matting_2.png"), new BackgroundIconItem("magic_recycler_matting_3", false, "matting_3", 14820411921793216L, ((MattingMenuPresenter) this.mPresenter).getActivity().getResources().getString(i, 2), true, "matting_3.png"), new BackgroundIconItem("magic_recycler_matting_4", false, "matting_4", 14820413909369024L, ((MattingMenuPresenter) this.mPresenter).getActivity().getResources().getString(i, 3), true, "matting_4.png"), new BackgroundIconItem("magic_recycler_matting_5", false, "matting_5", 14820416129466592L, ((MattingMenuPresenter) this.mPresenter).getActivity().getResources().getString(i, 4), true, "matting_5.png"), new BackgroundIconItem("magic_recycler_matting_6", false, "matting_6", 14820417650819072L, ((MattingMenuPresenter) this.mPresenter).getActivity().getResources().getString(i, 5), true, "matting_6.png"), new BackgroundIconItem("magic_recycler_matting_7", false, "matting_7", 14820421408653376L, ((MattingMenuPresenter) this.mPresenter).getActivity().getResources().getString(i, 6), true, "matting_7.png"), new BackgroundIconItem("magic_recycler_matting_8", false, "matting_8", 14820423948238880L, ((MattingMenuPresenter) this.mPresenter).getActivity().getResources().getString(i, 7), true, "matting_8.png"), new BackgroundIconItem("magic_recycler_matting_9", false, "matting_9", 14820425770991808L, ((MattingMenuPresenter) this.mPresenter).getActivity().getResources().getString(i, 8), true, "matting_9.png"), new BackgroundIconItem("magic_recycler_matting_10", false, "matting_10", 14820427138007040L, ((MattingMenuPresenter) this.mPresenter).getActivity().getResources().getString(i, 9), true, "matting_10.png"), new BackgroundIconItem("magic_recycler_matting_11", false, "matting_11", 14820428373884992L, ((MattingMenuPresenter) this.mPresenter).getActivity().getResources().getString(i, 10), true, "matting_11.png"), new BackgroundIconItem("magic_recycler_matting_12", false, "matting_12", 14820429672349920L, ((MattingMenuPresenter) this.mPresenter).getActivity().getResources().getString(i, 11), true, "matting_12.png"));
    }

    @Override // com.miui.gallery.magic.base.SuperBase
    /* renamed from: initContract */
    public IMenu$M mo1070initContract() {
        return new IMenu$M() { // from class: com.miui.gallery.magic.matting.menu.MattingMenuModel.1
            @Override // com.miui.gallery.magic.matting.menu.IMenu$M
            public ArrayList<IconItem> getBgData() {
                if (MattingMenuModel.this.mBgList != null) {
                    return MattingMenuModel.this.mBgList;
                }
                MattingMenuModel.this.mBgList = new ArrayList();
                MattingMenuModel.this.mBgList.addAll(MattingMenuModel.this.getBackgroundList());
                for (int i = 0; i < MattingMenuModel.this.mBgList.size(); i++) {
                    BackgroundIconItem backgroundIconItem = (BackgroundIconItem) MattingMenuModel.this.mBgList.get(i);
                    if (backgroundIconItem.getResId() != 0 && MattingResourceFetcher.INSTANCE.isExistResource(backgroundIconItem)) {
                        backgroundIconItem.setDownload(false);
                    }
                }
                return MattingMenuModel.this.mBgList;
            }
        };
    }
}
