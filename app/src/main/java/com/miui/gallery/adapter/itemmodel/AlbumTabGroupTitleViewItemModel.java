package com.miui.gallery.adapter.itemmodel;

import android.view.View;
import android.widget.TextView;
import com.miui.epoxy.EpoxyAdapter;
import com.miui.epoxy.EpoxyModel;
import com.miui.epoxy.EpoxyViewHolder;
import com.miui.epoxy.common.BaseItemModel;
import com.miui.gallery.R;
import com.miui.gallery.adapter.itemmodel.base.BaseGalleryItemModel;
import com.miui.gallery.adapter.itemmodel.base.BaseGalleryViewHolder;
import com.miui.gallery.ui.album.common.viewbean.AlbumTabGroupTitleViewBean;
import java.util.List;
import java.util.Objects;

/* loaded from: classes.dex */
public class AlbumTabGroupTitleViewItemModel extends BaseGalleryItemModel<AlbumTabGroupTitleViewBean, VH> {
    @Override // com.miui.epoxy.EpoxyModel
    public int getLayoutRes() {
        return R.layout.album_group_header;
    }

    @Override // com.miui.epoxy.EpoxyModel
    public int getSpanSize(int i, int i2, int i3) {
        return i;
    }

    @Override // com.miui.epoxy.EpoxyModel
    public /* bridge */ /* synthetic */ void bindPartialData(EpoxyViewHolder epoxyViewHolder, List list) {
        bindPartialData((VH) epoxyViewHolder, (List<Object>) list);
    }

    public AlbumTabGroupTitleViewItemModel(AlbumTabGroupTitleViewBean albumTabGroupTitleViewBean) {
        super(albumTabGroupTitleViewBean.getId(), albumTabGroupTitleViewBean);
    }

    @Override // com.miui.epoxy.EpoxyModel
    public void bindData(VH vh) {
        super.bindData((AlbumTabGroupTitleViewItemModel) vh);
        int i = 0;
        boolean z = getItemData().getTitleRes() != 0;
        String text = z ? getThemeContext(vh).getText(getItemData().getTitleRes()) : "";
        vh.mTvTitle.setText(text);
        vh.itemView.setContentDescription(text);
        vh.itemView.setId((int) getItemData().getId());
        int state = getItemData().getState();
        vh.itemView.setTag(R.id.tag_album_group_title_state, Integer.valueOf(state));
        vh.mTvTitle.setVisibility(state == 1 ? 8 : 0);
        if (z) {
            View view = vh.mGapView;
            if (state == 1) {
                i = 8;
            }
            view.setVisibility(i);
        }
    }

    public void bindPartialData(VH vh, List<Object> list) {
        super.bindPartialData((AlbumTabGroupTitleViewItemModel) vh, list);
        int i = 0;
        int state = ((AlbumTabGroupTitleViewBean) list.get(0)).getState();
        vh.itemView.setTag(Integer.valueOf(state));
        vh.mTvTitle.setVisibility(state == 1 ? 8 : 0);
        View view = vh.mGapView;
        if (state == 1) {
            i = 8;
        }
        view.setVisibility(i);
    }

    @Override // com.miui.epoxy.EpoxyModel
    /* renamed from: getViewHolderCreator */
    public EpoxyAdapter.IViewHolderCreator<VH> mo541getViewHolderCreator() {
        return new EpoxyAdapter.IViewHolderCreator<VH>() { // from class: com.miui.gallery.adapter.itemmodel.AlbumTabGroupTitleViewItemModel.1
            @Override // com.miui.epoxy.EpoxyAdapter.IViewHolderCreator
            /* renamed from: create  reason: collision with other method in class */
            public VH mo1603create(View view, View view2) {
                return new VH(view);
            }
        };
    }

    @Override // com.miui.epoxy.EpoxyModel
    public Object getDiffChangeResult(EpoxyModel epoxyModel) {
        if (epoxyModel instanceof AlbumTabGroupTitleViewItemModel) {
            AlbumTabGroupTitleViewBean itemData = ((AlbumTabGroupTitleViewItemModel) epoxyModel).getItemData();
            int state = getItemData().getState();
            int state2 = itemData.getState();
            if (state != state2) {
                return new AlbumTabGroupTitleViewBean(itemData.getId(), itemData.getTitleRes(), state2);
            }
        }
        return super.getDiffChangeResult(epoxyModel);
    }

    @Override // com.miui.epoxy.common.BaseItemModel, com.miui.epoxy.EpoxyModel
    public boolean isContentTheSame(EpoxyModel<?> epoxyModel) {
        if (epoxyModel instanceof BaseItemModel) {
            return Objects.equals((AlbumTabGroupTitleViewBean) ((BaseItemModel) epoxyModel).getItemData(), getItemData());
        }
        return super.isContentTheSame(epoxyModel);
    }

    /* loaded from: classes.dex */
    public static class VH extends BaseGalleryViewHolder {
        public final View mGapView;
        public final TextView mTvTitle;

        public VH(View view) {
            super(view);
            TextView textView = (TextView) view.findViewById(R.id.tv_title);
            this.mTvTitle = textView;
            this.mGapView = view.findViewById(R.id.item_group_album_tip);
            textView.getPaint().setFakeBoldText(true);
        }
    }
}
