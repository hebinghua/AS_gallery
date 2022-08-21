package com.miui.gallery.magic.idphoto.menu;

import android.content.res.Configuration;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import com.miui.gallery.magic.Contact;
import com.miui.gallery.magic.R$dimen;
import com.miui.gallery.magic.R$id;
import com.miui.gallery.magic.R$layout;
import com.miui.gallery.magic.base.BaseFragment;
import com.miui.gallery.magic.idphoto.CertificatesMakeActivity;
import com.miui.gallery.magic.idphoto.bean.PhotoStyle;
import com.miui.gallery.magic.widget.scroll.ScrollLinearLayoutManager;
import com.miui.gallery.magic.widget.scroll.SimpleRecyclerViewMiuix;
import com.miui.gallery.ui.CenterSmoothScrollerController;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.ScreenUtils;
import com.miui.gallery.util.anim.AnimParams;
import com.miui.gallery.util.anim.FolmeUtil;
import com.miui.gallery.widget.recyclerview.Adapter;
import com.miui.gallery.widget.tablayout.SegmentTabLayout;

/* loaded from: classes2.dex */
public class CertificatesMenuFragment extends BaseFragment<CertificatesMenuPresenter, IMenu$VP> {
    public SimpleRecyclerViewMiuix colorRecyclerview;
    public int indexTab = -1;
    public ScrollLinearLayoutManager linearLayoutManager;
    public Configuration mConfiguration;
    public SegmentTabLayout mMakeTab;
    public RadioButton mRadioBtnLabel1;
    public RadioButton mRadioBtnLabel2;
    public RadioButton mRadioBtnLabel3;
    public RadioButton mRadioBtnLabel4;
    public RadioButton mRadioBtnLabel5;
    public RadioButton mRadioBtnLabel6;
    public RadioButton mRadioButton1;
    public RadioButton mRadioButton2;
    public RadioGroup mRadioGroupLabel;
    public RadioGroup mRadioGroupTab;
    public SimpleRecyclerViewMiuix mRecyclerview;
    public ImageView mSearchImageView;
    public RelativeLayout mSearchLayout;
    public HorizontalScrollView scrollview;

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.magic.base.BaseFragment
    /* renamed from: initContract */
    public IMenu$VP mo1066initContract() {
        return new IMenu$VP() { // from class: com.miui.gallery.magic.idphoto.menu.CertificatesMenuFragment.1
            @Override // com.miui.gallery.magic.idphoto.menu.IMenu$VP
            public void setAdapter(Adapter adapter) {
                CertificatesMenuFragment.this.mRecyclerview.setAdapter(adapter);
            }

            @Override // com.miui.gallery.magic.idphoto.menu.IMenu$VP
            public void setColorAdapter(Adapter adapter) {
                CertificatesMenuFragment.this.colorRecyclerview.setAdapter(adapter);
            }

            @Override // com.miui.gallery.magic.idphoto.menu.IMenu$VP
            public void initListData() {
                ((CertificatesMenuPresenter) CertificatesMenuFragment.this.mPresenter).getContract().initListData();
            }

            @Override // com.miui.gallery.magic.idphoto.menu.IMenu$VP
            public void initTabsData() {
                ((CertificatesMenuPresenter) CertificatesMenuFragment.this.mPresenter).getContract().initTabsData();
            }

            @Override // com.miui.gallery.magic.idphoto.menu.IMenu$VP
            public void setTabs(String[] strArr) {
                CertificatesMenuFragment.this.mMakeTab.setTabData(strArr);
            }

            @Override // com.miui.gallery.magic.idphoto.menu.IMenu$VP
            public void setCurrentTab(int i) {
                if (CertificatesMenuFragment.this.indexTab != i) {
                    CertificatesMenuFragment.this.indexTab = i;
                    boolean isRTLDirection = BaseMiscUtil.isRTLDirection();
                    int i2 = CertificatesMenuFragment.this.indexTab;
                    if (i2 == 0) {
                        CertificatesMenuFragment.this.mRadioBtnLabel1.setChecked(true);
                        if (isRTLDirection) {
                            CertificatesMenuFragment.this.scrollview.fullScroll(66);
                        } else {
                            CertificatesMenuFragment.this.scrollview.fullScroll(17);
                        }
                    } else if (i2 == 1) {
                        CertificatesMenuFragment.this.mRadioBtnLabel2.setChecked(true);
                        if (isRTLDirection) {
                            CertificatesMenuFragment.this.scrollview.fullScroll(66);
                        } else {
                            CertificatesMenuFragment.this.scrollview.fullScroll(17);
                        }
                    } else if (i2 == 2) {
                        CertificatesMenuFragment.this.mRadioBtnLabel3.setChecked(true);
                    } else if (i2 == 3) {
                        CertificatesMenuFragment.this.mRadioBtnLabel4.setChecked(true);
                    } else if (i2 == 4) {
                        CertificatesMenuFragment.this.mRadioBtnLabel5.setChecked(true);
                        if (isRTLDirection) {
                            CertificatesMenuFragment.this.scrollview.fullScroll(17);
                        } else {
                            CertificatesMenuFragment.this.scrollview.fullScroll(66);
                        }
                    } else if (i2 != 5) {
                    } else {
                        CertificatesMenuFragment.this.mRadioBtnLabel6.setChecked(true);
                        if (isRTLDirection) {
                            CertificatesMenuFragment.this.scrollview.fullScroll(17);
                        } else {
                            CertificatesMenuFragment.this.scrollview.fullScroll(66);
                        }
                    }
                }
            }

            @Override // com.miui.gallery.magic.idphoto.menu.IMenu$VP
            public void scrollToPosition(int i) {
                CertificatesMenuFragment.this.linearLayoutManager.scrollToPositionWithOffset(i, 0);
            }

            @Override // com.miui.gallery.magic.idphoto.menu.IMenu$VP
            public void scrollToPositionItem(int i) {
                CertificatesMenuFragment.this.mRecyclerview.setSpringEnabled(false);
                CertificatesMenuFragment.this.mRecyclerview.smoothScrollToPosition(i);
            }

            @Override // com.miui.gallery.magic.idphoto.menu.IMenu$VP
            public void scrollToPositionColorItem(int i) {
                CertificatesMenuFragment.this.colorRecyclerview.setSpringEnabled(false);
                CertificatesMenuFragment.this.colorRecyclerview.smoothScrollToPosition(i);
            }

            @Override // com.miui.gallery.magic.idphoto.menu.IMenu$VP
            public void setBgColor(int i) {
                ((CertificatesMenuPresenter) CertificatesMenuFragment.this.mPresenter).getContract().setBgColor(i);
            }

            @Override // com.miui.gallery.magic.idphoto.menu.IMenu$VP
            public PhotoStyle getCurrentSize() {
                return ((CertificatesMenuPresenter) CertificatesMenuFragment.this.mPresenter).getContract().getCurrentSize();
            }

            @Override // com.miui.gallery.magic.idphoto.menu.IMenu$VP
            public void openSearch() {
                ((CertificatesMenuPresenter) CertificatesMenuFragment.this.mPresenter).getContract().openSearch();
            }
        };
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public void initView() {
        this.mConfiguration = new Configuration(getResources().getConfiguration());
        Contact.mPhotoMultiple = false;
        this.mMakeTab = (SegmentTabLayout) findViewById(R$id.magic_idp_tab);
        this.mRecyclerview = (SimpleRecyclerViewMiuix) findViewById(R$id.magic_idp_recyclerview);
        ScrollLinearLayoutManager scrollLinearLayoutManager = new ScrollLinearLayoutManager(getActivity());
        this.linearLayoutManager = scrollLinearLayoutManager;
        scrollLinearLayoutManager.setSmoothScroller(new CenterSmoothScrollerController(getActivity()));
        this.linearLayoutManager.setOrientation(0);
        this.mRecyclerview.setLayoutManager(this.linearLayoutManager);
        this.colorRecyclerview = (SimpleRecyclerViewMiuix) findViewById(R$id.magic_idp_color_recyclerview);
        ScrollLinearLayoutManager scrollLinearLayoutManager2 = new ScrollLinearLayoutManager(getActivity());
        scrollLinearLayoutManager2.setSmoothScroller(new CenterSmoothScrollerController(getActivity()));
        scrollLinearLayoutManager2.setOrientation(0);
        this.colorRecyclerview.setLayoutManager(scrollLinearLayoutManager2);
        this.scrollview = (HorizontalScrollView) findViewById(R$id.scrollview);
        this.mRadioGroupTab = (RadioGroup) findViewById(R$id.magic_tab_radio_group);
        this.mRadioButton1 = (RadioButton) findViewById(R$id.magic_idp_tab_category);
        this.mRadioButton2 = (RadioButton) findViewById(R$id.magic_idp_tab_bg);
        this.mRadioGroupLabel = (RadioGroup) findViewById(R$id.radio_group_label);
        this.mRadioBtnLabel1 = (RadioButton) findViewById(R$id.radio_button_idp1);
        this.mRadioBtnLabel2 = (RadioButton) findViewById(R$id.radio_button_idp2);
        this.mRadioBtnLabel3 = (RadioButton) findViewById(R$id.radio_button_idp3);
        this.mRadioBtnLabel4 = (RadioButton) findViewById(R$id.radio_button_idp4);
        this.mRadioBtnLabel5 = (RadioButton) findViewById(R$id.radio_button_idp5);
        this.mRadioBtnLabel6 = (RadioButton) findViewById(R$id.radio_button_idp6);
        AnimParams build = new AnimParams.Builder().setAlpha(0.6f).setTint(0.0f, 0.0f, 0.0f, 0.0f).setScale(1.0f).build();
        FolmeUtil.setCustomTouchAnim(this.mRadioButton1, build, null, null, null, true);
        FolmeUtil.setCustomTouchAnim(this.mRadioButton2, build, null, null, null, true);
        this.mRadioButton1.setChecked(true);
        this.mRadioGroupTab.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.miui.gallery.magic.idphoto.menu.CertificatesMenuFragment.2
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                CertificatesMakeActivity.setIsOperation(true);
                int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                if (checkedRadioButtonId == R$id.magic_idp_tab_bg) {
                    CertificatesMenuFragment.this.selectTable(true);
                } else if (checkedRadioButtonId != R$id.magic_idp_tab_category) {
                } else {
                    CertificatesMenuFragment.this.selectTable(false);
                }
            }
        });
        this.mSearchLayout = (RelativeLayout) findViewById(R$id.magic_idp_search);
        this.mSearchImageView = (ImageView) findViewById(R$id.magic_idp_search_image);
        fitRadioGroup();
    }

    @Override // androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        fitRadioGroup();
        if ((this.mConfiguration.updateFrom(configuration) & 1024) != 0) {
            resetMakeTable();
        }
    }

    public final void fitRadioGroup() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) this.mRadioGroupLabel.getLayoutParams();
        if (BaseBuildUtil.isPad() || getResources().getConfiguration().orientation == 2) {
            layoutParams.gravity = 1;
            if (ScreenUtils.getScreenWidth() < 1440) {
                layoutParams.gravity = 0;
            }
        } else {
            layoutParams.gravity = 0;
        }
        this.mRadioGroupLabel.setLayoutParams(layoutParams);
    }

    public final void resetMakeTable() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(this.scrollview.getLayoutParams());
        layoutParams.setMarginStart(getResources().getDimensionPixelSize(R$dimen.magic_idp_make_tab_margin_left));
        layoutParams.setMarginEnd(getResources().getDimensionPixelSize(R$dimen.magic_idp_make_tab_margin_right));
        this.scrollview.setLayoutParams(layoutParams);
        RelativeLayout.LayoutParams layoutParams2 = new RelativeLayout.LayoutParams(this.mSearchLayout.getLayoutParams());
        layoutParams2.width = getResources().getDimensionPixelSize(R$dimen.magic_idp_make_tab_width);
        this.mSearchLayout.setLayoutParams(layoutParams2);
        RelativeLayout.LayoutParams layoutParams3 = new RelativeLayout.LayoutParams(this.mSearchImageView.getLayoutParams());
        layoutParams3.setMarginStart(getResources().getDimensionPixelSize(R$dimen.magic_idp_make_search_height_left));
        layoutParams3.topMargin = getResources().getDimensionPixelSize(R$dimen.magic_idp_make_search_height_top);
        this.mSearchImageView.setLayoutParams(layoutParams3);
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.editor_menu_text_radio_button_bubble_width);
        RadioGroup.LayoutParams layoutParams4 = (RadioGroup.LayoutParams) this.mRadioBtnLabel1.getLayoutParams();
        layoutParams4.width = dimensionPixelSize;
        this.mRadioBtnLabel1.setLayoutParams(layoutParams4);
        this.mRadioBtnLabel2.setLayoutParams(layoutParams4);
        this.mRadioBtnLabel3.setLayoutParams(layoutParams4);
        this.mRadioBtnLabel4.setLayoutParams(layoutParams4);
        this.mRadioBtnLabel5.setLayoutParams(layoutParams4);
        this.mRadioBtnLabel6.setLayoutParams(layoutParams4);
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public void initData() {
        getContract().initTabsData();
        getContract().initListData();
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public CertificatesMenuPresenter getPresenterInstance() {
        return new CertificatesMenuPresenter();
    }

    @Override // com.miui.gallery.magic.base.BaseFragment
    public int getLayoutId() {
        return R$layout.ts_magic_id_photo_make_menu;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getId() == R$id.magic_idp_search) {
            CertificatesMakeActivity.setIsOperation(true);
            getContract().openSearch();
        }
    }

    public final void selectTable(boolean z) {
        int i = 8;
        findViewById(R$id.magic_idp_category).setVisibility(z ? 8 : 0);
        View findViewById = findViewById(R$id.magic_idp_color_recyclerview);
        if (z) {
            i = 0;
        }
        findViewById.setVisibility(i);
    }
}
