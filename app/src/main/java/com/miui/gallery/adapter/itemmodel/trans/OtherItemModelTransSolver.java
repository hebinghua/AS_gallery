package com.miui.gallery.adapter.itemmodel.trans;

import com.miui.epoxy.EpoxyModel;
import com.miui.gallery.adapter.itemmodel.AIAlbumGridCoverItemModel;
import com.miui.gallery.adapter.itemmodel.AlbumTabGroupTitleViewItemModel;
import com.miui.gallery.adapter.itemmodel.AlbumTabToolItemModel;
import com.miui.gallery.adapter.itemmodel.CloudAlbumItemModel;
import com.miui.gallery.adapter.itemmodel.CustomViewItemModel;
import com.miui.gallery.adapter.itemmodel.DefaultEmptyPageItemModel;
import com.miui.gallery.adapter.itemmodel.FourPalaceGridCoverItemModel;
import com.miui.gallery.adapter.itemmodel.HiddenAlbumItemModel;
import com.miui.gallery.adapter.itemmodel.LocationAndTagAlbumItemModel;
import com.miui.gallery.adapter.itemmodel.MapAlbumCoverItemModel;
import com.miui.gallery.adapter.itemmodel.MediaGroupItemModel;
import com.miui.gallery.adapter.itemmodel.MoreAlbumTipViewItemModel;
import com.miui.gallery.adapter.itemmodel.OtherAlbumGridCoverItemModel;
import com.miui.gallery.adapter.itemmodel.PeopleFaceItemModel;
import com.miui.gallery.adapter.itemmodel.RubbishItemModel;
import com.miui.gallery.adapter.itemmodel.TrashViewItemModel;
import com.miui.gallery.adapter.itemmodel.common.linear.CommonWrapperCheckableLinearAlbumItemModel;
import com.miui.gallery.ui.addtoalbum.AddToAlbumItemModel;
import com.miui.gallery.ui.addtoalbum.viewbean.AddToAlbumItemViewBean;
import com.miui.gallery.ui.album.aialbum.viewbean.LocationAndTagsAlbumItemViewBean;
import com.miui.gallery.ui.album.aialbum.viewbean.TagsAlbumItemViewBean;
import com.miui.gallery.ui.album.cloudalbum.viewbean.CloudAlbumItemViewBean;
import com.miui.gallery.ui.album.common.AlbumTabToolItemBean;
import com.miui.gallery.ui.album.common.CustomViewItemViewBean;
import com.miui.gallery.ui.album.common.MediaGroupTypeViewBean;
import com.miui.gallery.ui.album.common.RecyclerViewItemModel;
import com.miui.gallery.ui.album.common.viewbean.AlbumTabGroupTitleViewBean;
import com.miui.gallery.ui.album.common.viewbean.MoreAlbumTipViewBean;
import com.miui.gallery.ui.album.common.viewbean.TrashAlbumViewBean;
import com.miui.gallery.ui.album.hiddenalbum.viewbean.HiddenAlbumItemViewBean;
import com.miui.gallery.ui.album.main.viewbean.AIAlbumGridCoverViewBean;
import com.miui.gallery.ui.album.main.viewbean.FourPalaceGridCoverViewBean;
import com.miui.gallery.ui.album.main.viewbean.MapAlbumViewBean;
import com.miui.gallery.ui.album.main.viewbean.OtherAlbumGridCoverViewBean;
import com.miui.gallery.ui.album.main.viewbean.ai.PeopleFaceAlbumViewBean;
import com.miui.gallery.ui.album.rubbishalbum.viewbean.RubbishItemItemViewBean;
import com.miui.gallery.ui.autobackup.itemmodel.CloudGuideAutoBackupItemModel;
import com.miui.gallery.ui.autobackup.viewbean.CloudGuideAutoBackupItemViewBean;

/* loaded from: classes.dex */
public class OtherItemModelTransSolver implements TransDataToModelSolver {
    @Override // com.miui.gallery.adapter.itemmodel.trans.TransDataToModelSolver
    public EpoxyModel<?> transDataToModel(Object obj) {
        if (obj instanceof RecyclerViewItemModel.ConfigBean) {
            return new RecyclerViewItemModel((RecyclerViewItemModel.ConfigBean) obj);
        }
        if (obj instanceof AddToAlbumItemViewBean) {
            return new AddToAlbumItemModel((AddToAlbumItemViewBean) obj);
        }
        if (obj instanceof MediaGroupTypeViewBean) {
            return new MediaGroupItemModel((MediaGroupTypeViewBean) obj);
        }
        if (obj instanceof AlbumTabGroupTitleViewBean) {
            return new AlbumTabGroupTitleViewItemModel((AlbumTabGroupTitleViewBean) obj);
        }
        if (obj instanceof AlbumTabToolItemBean) {
            return new AlbumTabToolItemModel((AlbumTabToolItemBean) obj);
        }
        if (obj instanceof CustomViewItemViewBean) {
            return new CustomViewItemModel((CustomViewItemViewBean) obj);
        }
        if (obj instanceof AIAlbumGridCoverViewBean) {
            return new AIAlbumGridCoverItemModel((AIAlbumGridCoverViewBean) obj);
        }
        if (obj instanceof OtherAlbumGridCoverViewBean) {
            return new OtherAlbumGridCoverItemModel((OtherAlbumGridCoverViewBean) obj);
        }
        if (obj instanceof FourPalaceGridCoverViewBean) {
            return new FourPalaceGridCoverItemModel((FourPalaceGridCoverViewBean) obj);
        }
        if (obj instanceof DefaultEmptyPageItemModel.DefaultEmptyPageBean) {
            return new DefaultEmptyPageItemModel((DefaultEmptyPageItemModel.DefaultEmptyPageBean) obj);
        }
        if (obj instanceof TrashAlbumViewBean) {
            return new TrashViewItemModel((TrashAlbumViewBean) obj);
        }
        if (obj instanceof MoreAlbumTipViewBean) {
            return new MoreAlbumTipViewItemModel((MoreAlbumTipViewBean) obj);
        }
        if (obj instanceof HiddenAlbumItemViewBean) {
            return new HiddenAlbumItemModel((HiddenAlbumItemViewBean) obj);
        }
        if (obj instanceof CloudAlbumItemViewBean) {
            return new CloudAlbumItemModel((CloudAlbumItemViewBean) obj);
        }
        if (obj instanceof RubbishItemItemViewBean) {
            return new CommonWrapperCheckableLinearAlbumItemModel(new RubbishItemModel((RubbishItemItemViewBean) obj));
        }
        if (obj instanceof CloudGuideAutoBackupItemViewBean) {
            return new CloudGuideAutoBackupItemModel((CloudGuideAutoBackupItemViewBean) obj);
        }
        if (obj instanceof PeopleFaceAlbumViewBean) {
            return new PeopleFaceItemModel((PeopleFaceAlbumViewBean) obj);
        }
        if (obj instanceof LocationAndTagsAlbumItemViewBean) {
            return new LocationAndTagAlbumItemModel((LocationAndTagsAlbumItemViewBean) obj);
        }
        if (!(obj instanceof MapAlbumViewBean)) {
            return null;
        }
        return new MapAlbumCoverItemModel((MapAlbumViewBean) obj);
    }

    @Override // com.miui.gallery.adapter.itemmodel.trans.TransDataToModelSolver
    public Class[] supportTypes() {
        return new Class[]{MediaGroupTypeViewBean.class, HiddenAlbumItemViewBean.class, CloudAlbumItemViewBean.class, RubbishItemItemViewBean.class, CustomViewItemViewBean.class, AIAlbumGridCoverViewBean.class, OtherAlbumGridCoverViewBean.class, FourPalaceGridCoverViewBean.class, DefaultEmptyPageItemModel.DefaultEmptyPageBean.class, TrashAlbumViewBean.class, MoreAlbumTipViewBean.class, AddToAlbumItemViewBean.class, CloudGuideAutoBackupItemViewBean.class, PeopleFaceAlbumViewBean.class, LocationAndTagsAlbumItemViewBean.class, TagsAlbumItemViewBean.class, AlbumTabGroupTitleViewBean.class, AlbumTabToolItemBean.class, MapAlbumViewBean.class, RecyclerViewItemModel.ConfigBean.class};
    }
}
