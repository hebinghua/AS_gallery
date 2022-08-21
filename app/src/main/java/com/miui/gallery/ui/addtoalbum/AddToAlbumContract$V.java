package com.miui.gallery.ui.addtoalbum;

import android.os.Bundle;
import android.util.Pair;
import com.miui.epoxy.EpoxyModel;
import com.miui.gallery.base_optimization.mvp.view.IView;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.pickdrag.base.BasePickerDragActivity;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class AddToAlbumContract$V extends BasePickerDragActivity implements IView<AddToAlbumContract$P> {
    public AddToAlbumContract$P mPresenter;

    public abstract void bindHeaderInfo(Pair<String, Byte[]> pair, int i);

    public abstract void dispatchAlbums(List<BaseViewBean> list, List<EpoxyModel<?>> list2);

    public abstract boolean isAddPicToPdf();

    @Override // com.miui.pickdrag.base.BasePickerDragActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        AddToAlbumPresenter addToAlbumPresenter = new AddToAlbumPresenter();
        this.mPresenter = addToAlbumPresenter;
        addToAlbumPresenter.onAttachView(this);
    }

    public AddToAlbumContract$P getPresenter() {
        return this.mPresenter;
    }
}
