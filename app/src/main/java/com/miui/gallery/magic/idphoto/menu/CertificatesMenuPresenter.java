package com.miui.gallery.magic.idphoto.menu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.magic.R$string;
import com.miui.gallery.magic.base.BaseFragmentActivity;
import com.miui.gallery.magic.base.BasePresenter;
import com.miui.gallery.magic.idphoto.CertificatesMakeActivity;
import com.miui.gallery.magic.idphoto.adapter.CategoryAdapter;
import com.miui.gallery.magic.idphoto.adapter.CategoryColorAdapter;
import com.miui.gallery.magic.idphoto.bean.PhotoStyle;
import com.miui.gallery.magic.idphoto.menu.CertificatesMenuPresenter;
import com.miui.gallery.magic.idphoto.search.CertificatesSearchActivity;
import com.miui.gallery.magic.util.CaptionEditorDialogFragment;
import com.miui.gallery.magic.util.MagicLog;
import com.miui.gallery.magic.util.MagicSampler;
import com.miui.gallery.magic.util.MagicSamplerConstants;
import com.miui.gallery.magic.util.MagicSamplerSingleton;
import com.miui.gallery.magic.widget.MagicLoadingDialog;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.widget.recyclerview.Adapter;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.tablayout.SegmentTabLayout;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.MiStat;
import java.util.HashMap;

/* loaded from: classes2.dex */
public class CertificatesMenuPresenter extends BasePresenter<CertificatesMenuFragment, CertificatesMenuModel, IMenu$VP> {
    public static int FIRST_INDEX = 1;
    public static String PHOTOSTYLECALLBACK = "PHOTOSTYLECALLBACK";
    public PhotoStyle currentPhotoStyle = new PhotoStyle();
    public CategoryAdapter mAdapter;
    public CategoryColorAdapter mColorAdapter;
    public int mLastVisibleItemPosition;
    public MyPhotoStyleCallBack mPhotoStyleCallBack;

    @Override // com.miui.gallery.magic.base.BasePresenter
    public CertificatesMenuModel getModelInstance() {
        return new CertificatesMenuModel(this);
    }

    @Override // com.miui.gallery.magic.base.BasePresenter
    public void result(int i, int i2, Intent intent) {
        if (i2 != 201) {
            return;
        }
        int intExtra = intent.getIntExtra(MapBundleKey.MapObjKey.OBJ_SL_INDEX, 0);
        this.mAdapter.selectIndex(intExtra);
        getContract().scrollToPosition(intExtra);
        clickItem(intExtra, false);
        getContract().setCurrentTab(((CertificatesMenuModel) this.mModel).getContract().getTabIndex(this.mAdapter.getItemType(intExtra)));
    }

    /* renamed from: com.miui.gallery.magic.idphoto.menu.CertificatesMenuPresenter$1 */
    /* loaded from: classes2.dex */
    public class AnonymousClass1 implements IMenu$VP {
        public static /* synthetic */ boolean $r8$lambda$4U1PNTBybadgsz2A3XkcFlxdnSI(AnonymousClass1 anonymousClass1, RecyclerView recyclerView, View view, int i) {
            return anonymousClass1.lambda$setColorAdapter$1(recyclerView, view, i);
        }

        public static /* synthetic */ void $r8$lambda$7HVseWDR71T8HXenSEA0PTiJ0pk(AnonymousClass1 anonymousClass1, View view) {
            anonymousClass1.lambda$initTabsData$7(view);
        }

        public static /* synthetic */ void $r8$lambda$DjURkqpyPFQKHAM_gtSX7aLUNpU(BaseFragmentActivity baseFragmentActivity) {
            lambda$initListData$2(baseFragmentActivity);
        }

        public static /* synthetic */ void $r8$lambda$PY__0KuszglZPaMbjDI07DfE2hg(AnonymousClass1 anonymousClass1, View view) {
            anonymousClass1.lambda$initTabsData$6(view);
        }

        public static /* synthetic */ void $r8$lambda$f8OYUSoLzNBbnMW31piUMd8j5ks(AnonymousClass1 anonymousClass1, View view) {
            anonymousClass1.lambda$initTabsData$3(view);
        }

        public static /* synthetic */ void $r8$lambda$lk0XraYXFtmPu8Bmtro4juAeWqE(AnonymousClass1 anonymousClass1, View view) {
            anonymousClass1.lambda$initTabsData$5(view);
        }

        /* renamed from: $r8$lambda$llSGY-4nFtjeK1uKIvf8lrF3J_A */
        public static /* synthetic */ void m1023$r8$lambda$llSGY4nFtjeK1uKIvf8lrF3J_A(AnonymousClass1 anonymousClass1, View view) {
            anonymousClass1.lambda$initTabsData$8(view);
        }

        public static /* synthetic */ boolean $r8$lambda$p0OWZEnRUf024YzE1LOgDO4B96o(AnonymousClass1 anonymousClass1, RecyclerView recyclerView, View view, int i) {
            return anonymousClass1.lambda$setAdapter$0(recyclerView, view, i);
        }

        public static /* synthetic */ void $r8$lambda$v9dlRoWqEcNBYsElwaQwCPAggbg(AnonymousClass1 anonymousClass1, View view) {
            anonymousClass1.lambda$initTabsData$4(view);
        }

        public AnonymousClass1() {
            CertificatesMenuPresenter.this = r1;
        }

        @Override // com.miui.gallery.magic.idphoto.menu.IMenu$VP
        public void setAdapter(Adapter adapter) {
            ((CertificatesMenuFragment) CertificatesMenuPresenter.this.mView.get()).getContract().setAdapter(adapter);
            adapter.setOnItemClickListener(new OnItemClickListener() { // from class: com.miui.gallery.magic.idphoto.menu.CertificatesMenuPresenter$1$$ExternalSyntheticLambda8
                @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
                public final boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
                    return CertificatesMenuPresenter.AnonymousClass1.$r8$lambda$p0OWZEnRUf024YzE1LOgDO4B96o(CertificatesMenuPresenter.AnonymousClass1.this, recyclerView, view, i);
                }
            });
            ((CertificatesMenuFragment) CertificatesMenuPresenter.this.mView.get()).mRecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() { // from class: com.miui.gallery.magic.idphoto.menu.CertificatesMenuPresenter.1.1
                {
                    AnonymousClass1.this = this;
                }

                @Override // androidx.recyclerview.widget.RecyclerView.OnScrollListener
                public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                    int findLastVisibleItemPosition;
                    super.onScrolled(recyclerView, i, i2);
                    if (i == 0 || CertificatesMenuPresenter.this.mLastVisibleItemPosition == (findLastVisibleItemPosition = ((LinearLayoutManager) ((CertificatesMenuFragment) CertificatesMenuPresenter.this.mView.get()).mRecyclerview.getLayoutManager()).findLastVisibleItemPosition())) {
                        return;
                    }
                    CertificatesMenuPresenter.this.mLastVisibleItemPosition = findLastVisibleItemPosition;
                    AnonymousClass1 anonymousClass1 = AnonymousClass1.this;
                    anonymousClass1.setCurrentTab(((CertificatesMenuModel) CertificatesMenuPresenter.this.mModel).getContract().getTabIndex(CertificatesMenuPresenter.this.mAdapter.getItemType(CertificatesMenuPresenter.this.mLastVisibleItemPosition)));
                }
            });
            setCurrentTab(0);
            CertificatesMenuPresenter.this.mAdapter.selectIndex(CertificatesMenuPresenter.FIRST_INDEX);
        }

        public /* synthetic */ boolean lambda$setAdapter$0(RecyclerView recyclerView, View view, int i) {
            CertificatesMakeActivity.setIsOperation(true);
            if (CertificatesMenuPresenter.this.mAdapter.checkSelectIndex(i)) {
                return false;
            }
            CertificatesMenuPresenter.this.clickItem(i, false);
            return false;
        }

        @Override // com.miui.gallery.magic.idphoto.menu.IMenu$VP
        public void setColorAdapter(Adapter adapter) {
            ((CertificatesMenuFragment) CertificatesMenuPresenter.this.mView.get()).getContract().setColorAdapter(adapter);
            adapter.setOnItemClickListener(new OnItemClickListener() { // from class: com.miui.gallery.magic.idphoto.menu.CertificatesMenuPresenter$1$$ExternalSyntheticLambda7
                @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
                public final boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
                    return CertificatesMenuPresenter.AnonymousClass1.$r8$lambda$4U1PNTBybadgsz2A3XkcFlxdnSI(CertificatesMenuPresenter.AnonymousClass1.this, recyclerView, view, i);
                }
            });
        }

        public /* synthetic */ boolean lambda$setColorAdapter$1(RecyclerView recyclerView, View view, int i) {
            CertificatesMakeActivity.setIsOperation(true);
            if (CertificatesMenuPresenter.this.mColorAdapter.selectIndex(i).booleanValue()) {
                return false;
            }
            CertificatesMenuPresenter.this.clickColorItem(i);
            return false;
        }

        @Override // com.miui.gallery.magic.idphoto.menu.IMenu$VP
        public void setCurrentTab(int i) {
            ((CertificatesMenuFragment) CertificatesMenuPresenter.this.mView.get()).getContract().setCurrentTab(i);
        }

        @Override // com.miui.gallery.magic.idphoto.menu.IMenu$VP
        public void initListData() {
            CertificatesMenuPresenter certificatesMenuPresenter = CertificatesMenuPresenter.this;
            certificatesMenuPresenter.mAdapter = new CategoryAdapter(((CertificatesMenuModel) certificatesMenuPresenter.mModel).getContract().getListData(), CertificatesMenuPresenter.this.getActivity());
            setAdapter(CertificatesMenuPresenter.this.mAdapter);
            CertificatesMenuPresenter certificatesMenuPresenter2 = CertificatesMenuPresenter.this;
            certificatesMenuPresenter2.mColorAdapter = new CategoryColorAdapter(((CertificatesMenuModel) certificatesMenuPresenter2.mModel).getContract().getListColorData(), CertificatesMenuPresenter.this.getActivity());
            setColorAdapter(CertificatesMenuPresenter.this.mColorAdapter);
            final BaseFragmentActivity activityWithSync = CertificatesMenuPresenter.this.getActivityWithSync();
            if (activityWithSync == null) {
                return;
            }
            activityWithSync.showLoading(false, new MagicLoadingDialog.Callback() { // from class: com.miui.gallery.magic.idphoto.menu.CertificatesMenuPresenter$1$$ExternalSyntheticLambda6
                @Override // com.miui.gallery.magic.widget.MagicLoadingDialog.Callback
                public final void doCancel() {
                    CertificatesMenuPresenter.AnonymousClass1.$r8$lambda$DjURkqpyPFQKHAM_gtSX7aLUNpU(BaseFragmentActivity.this);
                }
            });
            new Handler().postDelayed(new Runnable() { // from class: com.miui.gallery.magic.idphoto.menu.CertificatesMenuPresenter.1.2
                {
                    AnonymousClass1.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    CertificatesMenuPresenter.this.clickItem(CertificatesMenuPresenter.FIRST_INDEX, true);
                }
            }, 200L);
        }

        public static /* synthetic */ void lambda$initListData$2(BaseFragmentActivity baseFragmentActivity) {
            baseFragmentActivity.removeLoadingDialog();
            baseFragmentActivity.finish();
        }

        @Override // com.miui.gallery.magic.idphoto.menu.IMenu$VP
        public void initTabsData() {
            setTabs(((CertificatesMenuModel) CertificatesMenuPresenter.this.mModel).getContract().getTabsData());
            ((CertificatesMenuFragment) CertificatesMenuPresenter.this.mView.get()).mMakeTab.setOnTabSelectListener(new SegmentTabLayout.OnTabSelectListener() { // from class: com.miui.gallery.magic.idphoto.menu.CertificatesMenuPresenter.1.3
                @Override // com.miui.gallery.widget.tablayout.SegmentTabLayout.OnTabSelectListener
                public void onTabReselect(int i) {
                }

                {
                    AnonymousClass1.this = this;
                }

                @Override // com.miui.gallery.widget.tablayout.SegmentTabLayout.OnTabSelectListener
                public void onTabSelect(int i) {
                    CertificatesMakeActivity.setIsOperation(true);
                    int i2 = 0;
                    for (int i3 = 0; i3 < i; i3++) {
                        i2 += ((CertificatesMenuModel) CertificatesMenuPresenter.this.mModel).getContract().getMapCategorySizeFromTabs(i3);
                    }
                    AnonymousClass1.this.scrollToPosition(i2);
                }
            });
            ((CertificatesMenuFragment) CertificatesMenuPresenter.this.mView.get()).mRadioBtnLabel1.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.magic.idphoto.menu.CertificatesMenuPresenter$1$$ExternalSyntheticLambda2
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    CertificatesMenuPresenter.AnonymousClass1.$r8$lambda$f8OYUSoLzNBbnMW31piUMd8j5ks(CertificatesMenuPresenter.AnonymousClass1.this, view);
                }
            });
            ((CertificatesMenuFragment) CertificatesMenuPresenter.this.mView.get()).mRadioBtnLabel2.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.magic.idphoto.menu.CertificatesMenuPresenter$1$$ExternalSyntheticLambda5
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    CertificatesMenuPresenter.AnonymousClass1.$r8$lambda$v9dlRoWqEcNBYsElwaQwCPAggbg(CertificatesMenuPresenter.AnonymousClass1.this, view);
                }
            });
            ((CertificatesMenuFragment) CertificatesMenuPresenter.this.mView.get()).mRadioBtnLabel3.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.magic.idphoto.menu.CertificatesMenuPresenter$1$$ExternalSyntheticLambda3
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    CertificatesMenuPresenter.AnonymousClass1.$r8$lambda$lk0XraYXFtmPu8Bmtro4juAeWqE(CertificatesMenuPresenter.AnonymousClass1.this, view);
                }
            });
            ((CertificatesMenuFragment) CertificatesMenuPresenter.this.mView.get()).mRadioBtnLabel4.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.magic.idphoto.menu.CertificatesMenuPresenter$1$$ExternalSyntheticLambda1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    CertificatesMenuPresenter.AnonymousClass1.$r8$lambda$PY__0KuszglZPaMbjDI07DfE2hg(CertificatesMenuPresenter.AnonymousClass1.this, view);
                }
            });
            ((CertificatesMenuFragment) CertificatesMenuPresenter.this.mView.get()).mRadioBtnLabel5.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.magic.idphoto.menu.CertificatesMenuPresenter$1$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    CertificatesMenuPresenter.AnonymousClass1.$r8$lambda$7HVseWDR71T8HXenSEA0PTiJ0pk(CertificatesMenuPresenter.AnonymousClass1.this, view);
                }
            });
            ((CertificatesMenuFragment) CertificatesMenuPresenter.this.mView.get()).mRadioBtnLabel6.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.magic.idphoto.menu.CertificatesMenuPresenter$1$$ExternalSyntheticLambda4
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    CertificatesMenuPresenter.AnonymousClass1.m1023$r8$lambda$llSGY4nFtjeK1uKIvf8lrF3J_A(CertificatesMenuPresenter.AnonymousClass1.this, view);
                }
            });
        }

        public /* synthetic */ void lambda$initTabsData$3(View view) {
            postIndexItem(0);
            if (BaseMiscUtil.isRTLDirection()) {
                ((CertificatesMenuFragment) CertificatesMenuPresenter.this.mView.get()).scrollview.fullScroll(66);
            } else {
                ((CertificatesMenuFragment) CertificatesMenuPresenter.this.mView.get()).scrollview.fullScroll(17);
            }
        }

        public /* synthetic */ void lambda$initTabsData$4(View view) {
            postIndexItem(1);
            if (BaseMiscUtil.isRTLDirection()) {
                ((CertificatesMenuFragment) CertificatesMenuPresenter.this.mView.get()).scrollview.fullScroll(66);
            } else {
                ((CertificatesMenuFragment) CertificatesMenuPresenter.this.mView.get()).scrollview.fullScroll(17);
            }
        }

        public /* synthetic */ void lambda$initTabsData$5(View view) {
            postIndexItem(2);
        }

        public /* synthetic */ void lambda$initTabsData$6(View view) {
            postIndexItem(3);
        }

        public /* synthetic */ void lambda$initTabsData$7(View view) {
            postIndexItem(4);
            if (BaseMiscUtil.isRTLDirection()) {
                ((CertificatesMenuFragment) CertificatesMenuPresenter.this.mView.get()).scrollview.fullScroll(17);
            } else {
                ((CertificatesMenuFragment) CertificatesMenuPresenter.this.mView.get()).scrollview.fullScroll(66);
            }
        }

        public /* synthetic */ void lambda$initTabsData$8(View view) {
            postIndexItem(5);
            if (BaseMiscUtil.isRTLDirection()) {
                ((CertificatesMenuFragment) CertificatesMenuPresenter.this.mView.get()).scrollview.fullScroll(17);
            } else {
                ((CertificatesMenuFragment) CertificatesMenuPresenter.this.mView.get()).scrollview.fullScroll(66);
            }
        }

        public final void postIndexItem(int i) {
            int i2 = 0;
            for (int i3 = 0; i3 < i; i3++) {
                i2 += ((CertificatesMenuModel) CertificatesMenuPresenter.this.mModel).getContract().getMapCategorySizeFromTabs(i3);
            }
            scrollToPosition(i2);
        }

        @Override // com.miui.gallery.magic.idphoto.menu.IMenu$VP
        public void setTabs(String[] strArr) {
            if (strArr == null || strArr.length < 1) {
                return;
            }
            ((CertificatesMenuFragment) CertificatesMenuPresenter.this.mView.get()).getContract().setTabs(strArr);
        }

        @Override // com.miui.gallery.magic.idphoto.menu.IMenu$VP
        public void scrollToPosition(int i) {
            ((CertificatesMenuFragment) CertificatesMenuPresenter.this.mView.get()).getContract().scrollToPosition(i);
        }

        @Override // com.miui.gallery.magic.idphoto.menu.IMenu$VP
        public void scrollToPositionItem(int i) {
            ((CertificatesMenuFragment) CertificatesMenuPresenter.this.mView.get()).getContract().scrollToPositionItem(i);
        }

        @Override // com.miui.gallery.magic.idphoto.menu.IMenu$VP
        public void scrollToPositionColorItem(int i) {
            ((CertificatesMenuFragment) CertificatesMenuPresenter.this.mView.get()).getContract().scrollToPositionColorItem(i);
        }

        @Override // com.miui.gallery.magic.idphoto.menu.IMenu$VP
        public void setBgColor(int i) {
            if (i == CertificatesMenuPresenter.this.currentPhotoStyle.getColor()) {
                return;
            }
            CertificatesMenuPresenter.this.currentPhotoStyle.setBgColor(i);
            CertificatesMenuPresenter.this.getActivity().event(2, CertificatesMenuPresenter.this.currentPhotoStyle);
        }

        @Override // com.miui.gallery.magic.idphoto.menu.IMenu$VP
        public PhotoStyle getCurrentSize() {
            return CertificatesMenuPresenter.this.currentPhotoStyle;
        }

        @Override // com.miui.gallery.magic.idphoto.menu.IMenu$VP
        public void openSearch() {
            MagicSampler.getInstance().recordCategory("idcard", MiStat.Event.SEARCH);
            ((CertificatesMenuFragment) CertificatesMenuPresenter.this.mView.get()).startActivityForResult(new Intent(((CertificatesMenuFragment) CertificatesMenuPresenter.this.mView.get()).getContext(), CertificatesSearchActivity.class), 1211);
        }
    }

    @Override // com.miui.gallery.magic.base.SuperBase
    /* renamed from: initContract */
    public IMenu$VP mo1070initContract() {
        return new AnonymousClass1();
    }

    public final void clickItem(int i, boolean z) {
        if (i == 0) {
            if (this.mPhotoStyleCallBack == null) {
                this.mPhotoStyleCallBack = new MyPhotoStyleCallBack();
            }
            Bundle bundle = new Bundle();
            bundle.putParcelable(PHOTOSTYLECALLBACK, this.mPhotoStyleCallBack);
            CaptionEditorDialogFragment.newInstance(bundle).show(getActivity().getSupportFragmentManager(), getActivity().getResources().getString(R$string.magic_custom_edit));
        } else {
            this.mAdapter.selectIndex(i);
            int[] widthHeight = ((CertificatesMenuModel) this.mModel).getContract().getWidthHeight(this.mAdapter.getItem(i).getText(), this.mAdapter.getItem(i).getTextmm());
            String text = this.mAdapter.getItem(i).getText();
            HashMap hashMap = new HashMap();
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, text);
            MagicSamplerSingleton.getInstance().setSelectIdCardSize(text);
            MagicSampler.getInstance().recordCategory("idcard", "spec_sel", hashMap);
            this.currentPhotoStyle.setWidth(widthHeight[0]);
            this.currentPhotoStyle.setHeight(widthHeight[1]);
            this.currentPhotoStyle.setwMM(widthHeight[2]);
            this.currentPhotoStyle.sethMM(widthHeight[3]);
            this.currentPhotoStyle.setQ();
            MagicLog.INSTANCE.startLog("idp_size_bg", "证件照切换尺寸");
            if (getActivityWithSync() == null) {
                return;
            }
            if (z) {
                getActivity().event(3, this.currentPhotoStyle);
            } else {
                getActivity().event(2, this.currentPhotoStyle);
            }
            MagicLog.INSTANCE.endLog("idp_size_bg", "证件照切换尺寸");
        }
        scrollToPositionItem(i);
    }

    public final void clickColorItem(int i) {
        HashMap hashMap = new HashMap();
        String[] strArr = MagicSamplerConstants.COLORS;
        if (i < strArr.length) {
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, strArr[i]);
            MagicSamplerSingleton.getInstance().setSelectIdCardColor(MagicSamplerConstants.COLORS[i]);
        } else {
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, strArr[0]);
            MagicSamplerSingleton.getInstance().setSelectIdCardColor(MagicSamplerConstants.COLORS[0]);
        }
        MagicSampler.getInstance().recordCategory("idcard", "bg_sel", hashMap);
        getContract().setBgColor(i);
        scrollToPositionColorItem(i);
    }

    public final void scrollToPositionColorItem(int i) {
        ((CertificatesMenuFragment) this.mView.get()).getContract().scrollToPositionColorItem(i);
    }

    public final void scrollToPositionItem(int i) {
        ((CertificatesMenuFragment) this.mView.get()).getContract().scrollToPositionItem(i);
    }

    /* loaded from: classes2.dex */
    public class MyPhotoStyleCallBack implements Parcelable {
        public final Parcelable.Creator<MyPhotoStyleCallBack> CREATOR;
        public int index;
        public PhotoStyle mCurrent;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public MyPhotoStyleCallBack() {
            CertificatesMenuPresenter.this = r1;
            this.index = 0;
            this.CREATOR = new Parcelable.Creator<MyPhotoStyleCallBack>() { // from class: com.miui.gallery.magic.idphoto.menu.CertificatesMenuPresenter.MyPhotoStyleCallBack.1
                {
                    MyPhotoStyleCallBack.this = this;
                }

                @Override // android.os.Parcelable.Creator
                /* renamed from: createFromParcel */
                public MyPhotoStyleCallBack mo1024createFromParcel(Parcel parcel) {
                    return new MyPhotoStyleCallBack(parcel);
                }

                @Override // android.os.Parcelable.Creator
                /* renamed from: newArray */
                public MyPhotoStyleCallBack[] mo1025newArray(int i) {
                    return new MyPhotoStyleCallBack[i];
                }
            };
        }

        public MyPhotoStyleCallBack(Parcel parcel) {
            CertificatesMenuPresenter.this = r1;
            this.index = 0;
            this.CREATOR = new Parcelable.Creator<MyPhotoStyleCallBack>() { // from class: com.miui.gallery.magic.idphoto.menu.CertificatesMenuPresenter.MyPhotoStyleCallBack.1
                {
                    MyPhotoStyleCallBack.this = this;
                }

                @Override // android.os.Parcelable.Creator
                /* renamed from: createFromParcel */
                public MyPhotoStyleCallBack mo1024createFromParcel(Parcel parcel2) {
                    return new MyPhotoStyleCallBack(parcel2);
                }

                @Override // android.os.Parcelable.Creator
                /* renamed from: newArray */
                public MyPhotoStyleCallBack[] mo1025newArray(int i) {
                    return new MyPhotoStyleCallBack[i];
                }
            };
            this.index = parcel.readInt();
            this.mCurrent = (PhotoStyle) parcel.readParcelable(PhotoStyle.class.getClassLoader());
        }

        public PhotoStyle getCurrent() {
            return this.mCurrent;
        }

        public void dismissCallBack(PhotoStyle photoStyle) {
            this.mCurrent = photoStyle;
            CertificatesMenuPresenter.this.currentPhotoStyle.setWidth(photoStyle.getWidth());
            CertificatesMenuPresenter.this.currentPhotoStyle.setHeight(photoStyle.getHeight());
            CertificatesMenuPresenter.this.currentPhotoStyle.setwMM(photoStyle.getwMM());
            CertificatesMenuPresenter.this.currentPhotoStyle.sethMM(photoStyle.gethMM());
            CertificatesMenuPresenter.this.currentPhotoStyle.setQ();
            if (photoStyle.isUseDpi()) {
                CertificatesMenuPresenter.this.currentPhotoStyle.useDpi();
            }
            CertificatesMenuPresenter.this.mAdapter.selectIndex(this.index);
            CertificatesMenuPresenter.this.mAdapter.setNewItem(photoStyle.getWidth(), photoStyle.getHeight(), photoStyle.getSizeType());
            CertificatesMenuPresenter.this.getActivity().event(2, CertificatesMenuPresenter.this.currentPhotoStyle);
            HashMap hashMap = new HashMap();
            hashMap.put(nexExportFormat.TAG_FORMAT_TYPE, photoStyle.getWidth() + "x" + photoStyle.getHeight() + " " + photoStyle.getSizeType());
            MagicSamplerSingleton magicSamplerSingleton = MagicSamplerSingleton.getInstance();
            magicSamplerSingleton.setSelectIdCardSize(photoStyle.getWidth() + "x" + photoStyle.getHeight() + " " + photoStyle.getSizeType());
            MagicSampler.getInstance().recordCategory("idcard", "spec_sel", hashMap);
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeInt(this.index);
            parcel.writeParcelable(this.mCurrent, 0);
        }
    }
}
