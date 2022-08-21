package com.miui.gallery.ui.album.main.base;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.epoxy.EpoxyModel;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TimeMonitor;
import com.miui.gallery.ui.album.common.base.BaseViewBean;
import com.miui.gallery.ui.album.common.viewbean.AlbumTabGroupTitleViewBean;
import com.miui.gallery.ui.album.main.base.BaseAlbumTabPresenter;
import com.miui.gallery.ui.album.main.base.config.AlbumPageConfig;
import com.miui.gallery.ui.album.main.grid.AlbumTabGridPageView;
import com.miui.gallery.ui.album.main.linear.AlbumTabLinearPageView;
import com.miui.gallery.util.ResourceUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/* loaded from: classes2.dex */
public class BaseAlbumTabFragment<P extends BaseAlbumTabPresenter> extends BaseAlbumTabContract$V<P> {
    public Map<Long, AlbumTabGroupTitleViewBean> mGroupBeanCaches = new HashMap();
    public BaseAlbumListPageView mPageView;

    public static /* synthetic */ void $r8$lambda$lSil1XbbuBi17HMqF_qNNSjWOQs(BaseAlbumTabFragment baseAlbumTabFragment) {
        baseAlbumTabFragment.lambda$showAlbumDatas$1();
    }

    public static /* synthetic */ void $r8$lambda$m3vAmkTibjisQxknhrZ6aQEFVhE(BaseAlbumTabFragment baseAlbumTabFragment) {
        baseAlbumTabFragment.lambda$showAlbumDatas$0();
    }

    public long getAIAlbumId() {
        return 2147483639L;
    }

    @Override // com.miui.gallery.app.base.BaseListPageFragment, com.miui.gallery.base_optimization.mvp.view.Fragment
    public int getLayoutId() {
        return R.layout.album_tab_page_base;
    }

    public long getOtherAlbumId() {
        return 2147483641L;
    }

    public long getTrashAlbumId() {
        return 2147483638L;
    }

    @Override // com.miui.gallery.app.base.BaseFragment, com.miui.gallery.base_optimization.mvp.view.Fragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mPageView = AlbumPageConfig.getInstance().isGridPageMode() ? new AlbumTabGridPageView(this) : new AlbumTabLinearPageView(this);
    }

    @Override // com.miui.gallery.base_optimization.mvp.view.Fragment, miuix.appcompat.app.Fragment, miuix.appcompat.app.IFragment
    public View onInflateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View onInflateView = super.onInflateView(layoutInflater, viewGroup, bundle);
        this.mPageView.onInit(onInflateView);
        return onInflateView;
    }

    @Override // com.miui.gallery.app.base.BaseListPageFragment
    public void initRecyclerView(RecyclerView recyclerView) {
        setEmptyPage(R.layout.album_page_empty);
    }

    @Override // com.miui.gallery.app.base.BaseListPageFragment
    public RecyclerView.ItemDecoration[] getRecyclerViewDecorations() {
        return this.mPageView.getRecyclerViewDecorations();
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        doOnActivityCreated();
    }

    public void doOnActivityCreated() {
        ((BaseAlbumTabPresenter) getPresenter()).initPart();
    }

    @Override // com.miui.gallery.base_optimization.mvp.view.Fragment
    public void onFragmentViewVisible(boolean z) {
        super.onFragmentViewVisible(z);
        if (z) {
            ((BaseAlbumTabPresenter) getPresenter()).initAll();
        }
    }

    public BaseViewBean getOrGenerateTitleBean(long j, int i) {
        return getOrGenerateTitleBean(j, i, 0);
    }

    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$V
    public BaseViewBean getOrGenerateTitleBean(long j, int i, int i2) {
        if (this.mGroupBeanCaches.containsKey(Long.valueOf(j))) {
            AlbumTabGroupTitleViewBean albumTabGroupTitleViewBean = this.mGroupBeanCaches.get(Long.valueOf(j));
            if (i2 == 0) {
                return albumTabGroupTitleViewBean;
            }
            if (albumTabGroupTitleViewBean != null && albumTabGroupTitleViewBean.getState() == i2) {
                return albumTabGroupTitleViewBean;
            }
            AlbumTabGroupTitleViewBean albumTabGroupTitleViewBean2 = new AlbumTabGroupTitleViewBean(j, i, i2);
            this.mGroupBeanCaches.put(Long.valueOf(j), albumTabGroupTitleViewBean2);
            return albumTabGroupTitleViewBean2;
        }
        AlbumTabGroupTitleViewBean albumTabGroupTitleViewBean3 = new AlbumTabGroupTitleViewBean(j, i, i2);
        this.mGroupBeanCaches.put(Long.valueOf(j), albumTabGroupTitleViewBean3);
        return albumTabGroupTitleViewBean3;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.miui.gallery.ui.album.main.base.BaseAlbumTabContract$V
    public void showAlbumDatas(List<BaseViewBean> list, List<EpoxyModel<?>> list2, boolean z) {
        if (z) {
            setDatasAndModels(list, list2, false, new Runnable() { // from class: com.miui.gallery.ui.album.main.base.BaseAlbumTabFragment$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    BaseAlbumTabFragment.$r8$lambda$m3vAmkTibjisQxknhrZ6aQEFVhE(BaseAlbumTabFragment.this);
                }
            });
        } else {
            setDatasAndModels(list, list2, false, new Runnable() { // from class: com.miui.gallery.ui.album.main.base.BaseAlbumTabFragment$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    BaseAlbumTabFragment.$r8$lambda$lSil1XbbuBi17HMqF_qNNSjWOQs(BaseAlbumTabFragment.this);
                }
            });
        }
    }

    public /* synthetic */ void lambda$showAlbumDatas$0() {
        this.mRecyclerView.scrollToPosition(0);
        TimeMonitor.trackTimeMonitor("403.7.0.1.13764", getDataSize());
    }

    public /* synthetic */ void lambda$showAlbumDatas$1() {
        TimeMonitor.trackTimeMonitor("403.7.0.1.13764", getDataSize());
    }

    public String getFixedAlbumNameById(long j) {
        if (j == getAIAlbumId()) {
            return ResourceUtils.getString(R.string.album_ai_page_title);
        }
        if (j == getOtherAlbumId()) {
            return ResourceUtils.getString(R.string.other_album);
        }
        return j == getTrashAlbumId() ? ResourceUtils.getString(R.string.trash_bin) : "unknow";
    }

    /* renamed from: getPageView */
    public BaseAlbumListPageView mo1594getPageView() {
        return this.mPageView;
    }

    public void setPageView(BaseAlbumListPageView baseAlbumListPageView) {
        if (baseAlbumListPageView == null || baseAlbumListPageView == this.mPageView) {
            return;
        }
        this.mPageView = baseAlbumListPageView;
    }

    @Override // com.miui.gallery.app.base.BaseFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        mo1594getPageView().onConfigurationChanged(configuration);
    }
}
