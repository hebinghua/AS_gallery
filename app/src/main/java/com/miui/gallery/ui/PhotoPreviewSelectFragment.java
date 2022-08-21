package com.miui.gallery.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.R;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.adapter.PhotoPageAdapter;
import com.miui.gallery.adapter.PhotoPreviewAdapter;
import com.miui.gallery.app.fragment.GalleryFragment;
import com.miui.gallery.ktx.DisplayKt;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.BaseDataSet;
import com.miui.gallery.model.ImageLoadParams;
import com.miui.gallery.ui.photoPage.bars.data.IDataProvider;
import com.miui.gallery.ui.photoPage.bars.manager.ChoiceManagerBase;
import com.miui.gallery.ui.photoPage.bars.manager.IManagerOwner;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.GeneralDataHolder;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.SecurityShareHelper;
import com.miui.gallery.util.SystemUiUtil;
import com.miui.gallery.util.TalkBackUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.photoview.preload.PhotoPagePreloadHelper;
import com.miui.gallery.widget.slip.VerticalSlipLayout;
import com.miui.privacy.LockSettingsHelper;
import java.util.List;
import java.util.Objects;
import miui.gallery.support.MiuiSdkCompat;

/* loaded from: classes2.dex */
public class PhotoPreviewSelectFragment extends AbstractViewPagerFragment implements IManagerOwner {
    public ChoiceManager mChoiceManager;
    public View mCustomView;
    public View mPagerContainer;

    @Override // com.miui.gallery.ui.photoPage.bars.manager.IManagerOwner
    public GalleryFragment getFragment() {
        return this;
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment
    public String getPageName() {
        return "PhotoPreviewSelectFragment";
    }

    public static PhotoPreviewSelectFragment newInstance(Uri uri, Bundle bundle) {
        if (uri == null) {
            throw new IllegalArgumentException("uri is null");
        }
        if (bundle == null) {
            bundle = new Bundle();
        }
        bundle.putString("photo_uri", uri.toString());
        PhotoPreviewSelectFragment photoPreviewSelectFragment = new PhotoPreviewSelectFragment();
        photoPreviewSelectFragment.setArguments(bundle);
        return photoPreviewSelectFragment;
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, com.miui.gallery.app.fragment.GalleryFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        PhotoPageAdapter.ChoiceMode choiceMode;
        super.onSaveInstanceState(bundle);
        ChoiceManager choiceManager = this.mChoiceManager;
        if (choiceManager == null || (choiceMode = choiceManager.mChoiceMode) == null) {
            return;
        }
        List<Long> selectIds = choiceMode.getSelectIds();
        List<Long> renderIds = this.mChoiceManager.mChoiceMode.getRenderIds();
        List<Long> originIds = this.mChoiceManager.mChoiceMode.getOriginIds();
        List<Integer> selectItems = this.mChoiceManager.mChoiceMode.getSelectItems();
        List<Integer> renderItems = this.mChoiceManager.mChoiceMode.getRenderItems();
        List<Integer> originItems = this.mChoiceManager.mChoiceMode.getOriginItems();
        GeneralDataHolder generalDataHolder = GeneralDataHolder.getInstance();
        generalDataHolder.save("save_photo_preview_selected_status_from_orientation_and_fold", Integer.valueOf(getActivity().hashCode()));
        if (selectIds != null && selectIds.size() != 0) {
            generalDataHolder.save("save_photo_preview_selected_ids_retrieve_key", selectIds);
        }
        if (renderIds != null && renderIds.size() != 0) {
            generalDataHolder.save("save_photo_preview_selected_render_ids", renderIds);
        }
        if (originIds != null && originIds.size() != 0) {
            generalDataHolder.save("save_photo_preview_selected_origin_ids", originIds);
        }
        if (selectItems != null && selectItems.size() != 0) {
            generalDataHolder.save("save_photo_preview_selected_positions", selectItems);
        }
        if (renderItems != null && renderItems.size() != 0) {
            generalDataHolder.save("save_photo_preview_selected_render_positions", renderItems);
        }
        if (originItems == null || originItems.size() == 0) {
            return;
        }
        generalDataHolder.save("save_photo_preview_selected_origin_positions", originItems);
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroy() {
        super.onDestroy();
        if (GeneralDataHolder.getInstance().retrieve("save_photo_preview_selected_status_from_orientation_and_fold") == null || Objects.equals(GeneralDataHolder.getInstance().retrieve("save_photo_preview_selected_status_from_orientation_and_fold"), Integer.valueOf(getActivity().hashCode()))) {
            return;
        }
        GeneralDataHolder.getInstance().remove("save_photo_preview_selected_positions");
        GeneralDataHolder.getInstance().remove("save_photo_preview_selected_ids_retrieve_key");
        GeneralDataHolder.getInstance().remove("save_photo_preview_selected_render_positions");
        GeneralDataHolder.getInstance().remove("save_photo_preview_selected_render_ids");
        GeneralDataHolder.getInstance().remove("save_photo_preview_selected_origin_positions");
        GeneralDataHolder.getInstance().remove("save_photo_preview_selected_origin_ids");
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment
    public void restoreState(Bundle bundle) {
        super.restoreState(bundle);
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setThemeRes(2131952018);
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment
    public PhotoPageAdapter getPhotoAdapter() {
        ImageLoadParams imageLoadParams = getImageLoadParams();
        long key = imageLoadParams != null ? imageLoadParams.getKey() : -1L;
        FragmentActivity activity = getActivity();
        int initCount = getInitCount();
        PhotoPagePreloadHelper photoPagePreloadHelper = this.mPreloadHelper;
        return new PhotoPreviewAdapter(activity, initCount, imageLoadParams, photoPagePreloadHelper != null ? photoPagePreloadHelper.getPhotoPageViewProviderIfHave() : null, getEnterViewInfo(getArguments().getInt("photo_init_position", 0), key), this, getPhotoPageInteractionListener());
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment
    public void onViewInflated(View view) {
        this.mPagerContainer = view.findViewById(R.id.photo_pager_container);
        ((VerticalSlipLayout) view.findViewById(R.id.slip_layout)).setDraggable(false);
        if (view.isAttachedToWindow()) {
            configViewLayout(view, getResources().getDimensionPixelSize(R.dimen.preview_select_top_area_distance));
        } else {
            view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.miui.gallery.ui.PhotoPreviewSelectFragment.1
                @Override // android.view.View.OnAttachStateChangeListener
                public void onViewDetachedFromWindow(View view2) {
                }

                @Override // android.view.View.OnAttachStateChangeListener
                public void onViewAttachedToWindow(View view2) {
                    view2.removeOnAttachStateChangeListener(this);
                    PhotoPreviewSelectFragment.this.configViewLayout(view2, view2.getResources().getDimensionPixelSize(R.dimen.preview_select_top_area_distance));
                }
            });
        }
        this.mChoiceManager = new ChoiceManager();
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        SecurityShareHelper.startPrivacyProtectTipSettingsActivity(getContext());
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        BaseActivity baseActivity;
        if (i == 27) {
            if (i2 != -1 && (baseActivity = this.mActivity) != null && !baseActivity.isFinishing()) {
                this.mActivity.finish();
            } else {
                this.mDataProvider.getFieldData().isNeedConfirmPassWord = false;
            }
        }
        super.onActivityResult(i, i2, intent);
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment
    public void onShared(boolean z) {
        super.onShared(z);
        this.mChoiceManager.onShared(z);
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment
    public View getLayout(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return layoutInflater.inflate(R.layout.photo_preview_select_layout, viewGroup, false);
    }

    @Override // com.miui.gallery.ui.photoPage.bars.manager.IManagerOwner
    public BaseActivity getBaseActivity() {
        return this.mActivity;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.manager.IManagerOwner
    public PhotoPageAdapter getAdapter() {
        return this.mAdapter;
    }

    @Override // com.miui.gallery.ui.photoPage.bars.manager.IManagerOwner
    public IDataProvider getDataProvider() {
        return this.mDataProvider;
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment
    public void onDataSetLoaded(BaseDataSet baseDataSet, boolean z) {
        super.onDataSetLoaded(baseDataSet, z);
        if (z) {
            long currentTimeMillis = System.currentTimeMillis();
            this.mChoiceManager.initSelectedItems(baseDataSet);
            DefaultLogger.d("PhotoPreviewSelectFragment", "initialize selection costs %dms", Long.valueOf(System.currentTimeMillis() - currentTimeMillis));
        }
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onResume() {
        super.onResume();
        if (isNeedConfirmPassWord(this.mAdapter.getDataItem(this.mPager.getCurrentItem()))) {
            this.mDataProvider.getFieldData().isNeedConfirmPassWord = false;
            LockSettingsHelper.startAuthenticatePasswordActivity(this, 27);
        }
        ChoiceManager choiceManager = this.mChoiceManager;
        if (choiceManager != null) {
            choiceManager.updateSubTitle();
        }
        checkAndSetOrientation();
    }

    public final void checkAndSetOrientation() {
        FragmentActivity activity = getActivity();
        if (activity == null) {
            return;
        }
        boolean isLargeScreenIndependentOrientation = BaseBuildUtil.isLargeScreenIndependentOrientation();
        if (!isLargeScreenIndependentOrientation && MiscUtil.isLandMode(activity)) {
            SystemUiUtil.setRequestedOrientation(1, activity);
        } else if (!isLargeScreenIndependentOrientation) {
        } else {
            SystemUiUtil.setRequestedOrientation(2, activity);
        }
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        this.mDataProvider.getFieldData().isNeedConfirmPassWord = true;
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        ChoiceManager choiceManager = this.mChoiceManager;
        if (choiceManager != null) {
            choiceManager.release();
        }
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, androidx.fragment.app.Fragment
    public void onPause() {
        super.onPause();
        ChoiceManager choiceManager = this.mChoiceManager;
        if (choiceManager != null) {
            choiceManager.onPause();
        }
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, androidx.fragment.app.Fragment
    public void onDetach() {
        super.onDetach();
        ChoiceManager choiceManager = this.mChoiceManager;
        if (choiceManager != null) {
            choiceManager.onDetach();
        }
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment
    public void onItemSettled(int i) {
        super.onItemSettled(i);
        BaseDataItem dataItem = this.mAdapter.getDataItem(i);
        if (dataItem != null) {
            TalkBackUtil.requestAnnouncementEvent(this.mPager, dataItem.getContentDescription(this.mActivity));
        }
    }

    /* loaded from: classes2.dex */
    public class ChoiceManager extends ChoiceManagerBase implements View.OnClickListener {
        public Button mCancelBtn;
        public Button mSelectAllBtn;
        public TextView mSubTitleView;
        public TextView mTitleView;

        @Override // com.miui.gallery.ui.photoPage.bars.manager.ChoiceManagerBase
        public int getContainerId() {
            return R.id.child_container;
        }

        public ChoiceManager() {
            super(PhotoPreviewSelectFragment.this);
            initFunction();
            initTopBar();
            setUpChooserFragment(0, true, 2, "image/*");
            if (ShareStateRouter.IS_MISHARE_AVAILABLE.get(PhotoPreviewSelectFragment.this.getActivity()).booleanValue()) {
                this.mAdapter.setUseSlipModeV2(true);
                this.mAdapter.setSlipProgress(1.0f);
            }
            this.mChoiceMode = this.mAdapter.startActionMode(this);
        }

        public void initSelectedItems(BaseDataSet baseDataSet) {
            initSelected(baseDataSet);
            initRenderSelected(baseDataSet);
            initOriginSelected(baseDataSet);
        }

        public final void initSelected(BaseDataSet baseDataSet) {
            long[] jArr = (long[]) GeneralDataHolder.getInstance().remove(PhotoPreviewSelectFragment.this.getArguments().getString("photo_preview_initial_selected_ids_retrieve_key"));
            int[] intArray = PhotoPreviewSelectFragment.this.getArguments().getIntArray("photo_preview_initial_selected_positions");
            int i = 0;
            if (jArr != null && intArray != null && baseDataSet != null) {
                if (jArr.length != intArray.length) {
                    throw new IllegalArgumentException("ids and positions not match");
                }
                BaseDataItem baseDataItem = new BaseDataItem();
                while (i < jArr.length) {
                    long j = jArr[i];
                    baseDataItem.setKey(j);
                    int indexOfItem = baseDataSet.getIndexOfItem(baseDataItem, intArray[i]);
                    if (indexOfItem != -1) {
                        this.mChoiceMode.appendCheck(indexOfItem, j);
                    }
                    i++;
                }
                this.mChoiceMode.finishInit();
                PhotoPreviewSelectFragment.this.getArguments().remove("photo_preview_initial_selected_ids_retrieve_key");
                PhotoPreviewSelectFragment.this.getArguments().remove("photo_preview_initial_selected_positions");
            } else if (baseDataSet == null) {
            } else {
                List list = (List) GeneralDataHolder.getInstance().remove("save_photo_preview_selected_ids_retrieve_key");
                List list2 = (List) GeneralDataHolder.getInstance().remove("save_photo_preview_selected_positions");
                if (list2 != null && list != null) {
                    BaseDataItem baseDataItem2 = new BaseDataItem();
                    if (list.size() != list2.size()) {
                        throw new IllegalArgumentException("ids and positions not match");
                    }
                    int i2 = 0;
                    while (i < list.size()) {
                        long longValue = ((Long) list.get(i)).longValue();
                        baseDataItem2.setKey(longValue);
                        int indexOfItem2 = baseDataSet.getIndexOfItem(baseDataItem2, ((Integer) list2.get(i)).intValue() + i2);
                        int intValue = indexOfItem2 - ((Integer) list2.get(i)).intValue();
                        if (indexOfItem2 != -1) {
                            this.mChoiceMode.appendCheck(indexOfItem2, longValue);
                        }
                        i++;
                        i2 = intValue;
                    }
                    this.mChoiceMode.finishInit();
                    return;
                }
                this.mChoiceMode.finishInit();
            }
        }

        public final void initRenderSelected(BaseDataSet baseDataSet) {
            List list = (List) GeneralDataHolder.getInstance().remove("save_photo_preview_selected_render_ids");
            if (list == null || baseDataSet == null) {
                return;
            }
            BaseDataItem baseDataItem = new BaseDataItem();
            List list2 = (List) GeneralDataHolder.getInstance().remove("save_photo_preview_selected_render_positions");
            if (list2 != null) {
                if (list.size() != list2.size()) {
                    throw new IllegalArgumentException("ids and positions not match");
                }
                int i = 0;
                int i2 = 0;
                while (i < list.size()) {
                    long longValue = ((Long) list.get(i)).longValue();
                    baseDataItem.setKey(longValue);
                    int indexOfItem = baseDataSet.getIndexOfItem(baseDataItem, ((Integer) list2.get(i)).intValue() + i2);
                    int intValue = indexOfItem - ((Integer) list2.get(i)).intValue();
                    if (indexOfItem != -1) {
                        this.mChoiceMode.setRenderChecked(indexOfItem, longValue, true);
                    }
                    i++;
                    i2 = intValue;
                }
                this.mChoiceMode.finishInit();
                return;
            }
            this.mChoiceMode.finishInit();
        }

        public final void initOriginSelected(BaseDataSet baseDataSet) {
            List list = (List) GeneralDataHolder.getInstance().remove("save_photo_preview_selected_origin_ids");
            if (list == null || baseDataSet == null) {
                return;
            }
            BaseDataItem baseDataItem = new BaseDataItem();
            List list2 = (List) GeneralDataHolder.getInstance().remove("save_photo_preview_selected_origin_positions");
            if (list2 != null) {
                if (list.size() != list2.size()) {
                    throw new IllegalArgumentException("ids and positions not match");
                }
                int i = 0;
                int i2 = 0;
                while (i < list.size()) {
                    baseDataItem.setKey(((Long) list.get(i)).longValue());
                    int indexOfItem = baseDataSet.getIndexOfItem(baseDataItem, ((Integer) list2.get(i)).intValue() + i2);
                    int intValue = indexOfItem - ((Integer) list2.get(i)).intValue();
                    if (indexOfItem != -1) {
                        this.mChoiceMode.appendOriginChecked(indexOfItem, ((Long) list.get(i)).longValue(), true);
                    }
                    i++;
                    i2 = intValue;
                }
                this.mChoiceMode.finishInit();
                return;
            }
            this.mChoiceMode.finishInit();
        }

        public final void initTopBar() {
            PhotoPreviewSelectFragment photoPreviewSelectFragment = PhotoPreviewSelectFragment.this;
            photoPreviewSelectFragment.mCustomView = LayoutInflater.from(photoPreviewSelectFragment.mActivity).inflate(R.layout.custom_select_mode_title, (ViewGroup) null);
            this.mCancelBtn = (Button) PhotoPreviewSelectFragment.this.mCustomView.findViewById(R.id.select_mode_button1);
            this.mTitleView = (TextView) PhotoPreviewSelectFragment.this.mCustomView.findViewById(R.id.select_mode_title);
            this.mSubTitleView = (TextView) PhotoPreviewSelectFragment.this.mCustomView.findViewById(R.id.choice_mode_subtitle);
            this.mSelectAllBtn = (Button) PhotoPreviewSelectFragment.this.mCustomView.findViewById(R.id.select_mode_button2);
            this.mCancelBtn.setOnClickListener(this);
            this.mSelectAllBtn.setOnClickListener(this);
            if (BaseBuildUtil.isLargeScreenDevice()) {
                this.mSelectAllBtn.setVisibility(4);
            } else {
                this.mSelectAllBtn.setOnClickListener(this);
                MiuiSdkCompat.setEditActionModeButton(PhotoPreviewSelectFragment.this.mActivity, this.mSelectAllBtn, 0);
            }
            this.mCancelBtn.setText("");
            MiuiSdkCompat.setEditActionModeButton(PhotoPreviewSelectFragment.this.mActivity, this.mSelectAllBtn, 0);
            PhotoPreviewSelectFragment.this.mActivity.getAppCompatActionBar().setDisplayOptions(16);
            PhotoPreviewSelectFragment.this.mActivity.getAppCompatActionBar().setCustomView(PhotoPreviewSelectFragment.this.mCustomView, new ActionBar.LayoutParams(-1, -2, 19));
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.select_mode_button1 /* 2131363334 */:
                    PhotoPreviewSelectFragment.this.finish();
                    return;
                case R.id.select_mode_button2 /* 2131363335 */:
                    if (this.mChoiceMode.getSelectItems().size() < this.mChoiceMode.getCount()) {
                        this.mChoiceMode.chooseAll();
                        return;
                    } else {
                        this.mChoiceMode.unChooseAll();
                        return;
                    }
                default:
                    return;
            }
        }

        @Override // com.miui.gallery.ui.photoPage.bars.manager.ChoiceManagerBase
        public void updateSelectMode() {
            int size = this.mChoiceMode.getSelectItems().size();
            MiuiSdkCompat.setEditActionModeButton(PhotoPreviewSelectFragment.this.mActivity, this.mSelectAllBtn, (size <= 0 || size != this.mChoiceMode.getCount()) ? 0 : 1);
            super.updateSelectMode();
        }

        @Override // com.miui.gallery.ui.photoPage.bars.manager.ChoiceManagerBase
        public TextView getChoiceTitle() {
            return this.mTitleView;
        }

        @Override // com.miui.gallery.ui.photoPage.bars.manager.ChoiceManagerBase
        public TextView getSubTitle() {
            return this.mSubTitleView;
        }

        @Override // com.miui.gallery.ui.photoPage.bars.manager.ChoiceManagerBase
        public void onShared(boolean z) {
            super.onShared(z);
        }
    }

    @Override // com.miui.gallery.ui.AbstractViewPagerFragment, com.miui.gallery.app.fragment.GalleryFragment, com.miui.gallery.app.fragment.MiuiFragment, miuix.appcompat.app.Fragment, androidx.fragment.app.Fragment, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        int dimensionPixelSize;
        super.onConfigurationChanged(configuration);
        if (this.isLargeScreenAndWindow && getView() != null && this.mAdapter != null) {
            int dimensionPixelSize2 = getResources().getDimensionPixelSize(R.dimen.photo_choice_title_height);
            if (ShareStateRouter.IS_MISHARE_AVAILABLE.get(this.mActivity).booleanValue()) {
                if (!ShareStateRouter.SHOULD_UPGRADE_NEARBY_SHARE.get(this.mActivity).booleanValue()) {
                    dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.fast_share_height);
                } else {
                    dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.fast_share_height_with_nearby_share);
                }
            } else {
                dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.fast_share_height_without_mishare);
            }
            int i = (DisplayKt.getDisplaySize(getView()).y - dimensionPixelSize2) - dimensionPixelSize;
            this.mPager.getLayoutParams().height = i;
            this.mAdapter.setSlippedRect(dimensionPixelSize2, dimensionPixelSize2 + i);
            this.mPager.setPageMargin(getResources().getDimensionPixelSize(R.dimen.viewpager_page_margin));
            this.mPager.bringToFront();
            this.mPagerContainer.getLayoutParams().height = i;
        }
        checkAndSetOrientation();
    }
}
